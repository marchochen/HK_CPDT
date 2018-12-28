<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/escape_sing_quo.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/display_score.xsl"/>
	<xsl:import href="utils/change_lowercase.xsl"/>
	<xsl:import href="share/wb_module_status_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- other utils -->
	<xsl:import href="share/ist_lrn_rpt_gen_tab.xsl"/>
	<xsl:import href="share/gen_mod_date.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="cur_node" select="/report/@cur_node"/>
	<xsl:variable name="cos_id" select="/report/course/@id"/>
	<xsl:variable name="itm_id" select="/report/course/@itm_id"/>
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
			<xsl:with-param name="lab_name">標題</xsl:with-param>
			<xsl:with-param name="lab_online_performance">成績</xsl:with-param>
			<xsl:with-param name="lab_status">學習情況</xsl:with-param>
			<xsl:with-param name="lab_avg_per_learner">每個學生平均訪問時間</xsl:with-param>
			<xsl:with-param name="lab_hits">訪問次數</xsl:with-param>
			<xsl:with-param name="lab_avg_per_hit">每次平均訪問時間</xsl:with-param>
			<xsl:with-param name="lab_last_access">上次訪問時間</xsl:with-param>
			<xsl:with-param name="lab_avg_score">平均成績(%)</xsl:with-param>
			<xsl:with-param name="lab_detail">詳細報告</xsl:with-param>
			<xsl:with-param name="lab_status_viewed">己查閱</xsl:with-param>
			<xsl:with-param name="lab_status_not_viewed">未查閱</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_module_list">模塊清單</xsl:with-param>
			<xsl:with-param name="lab_folder_list">模塊夾清單</xsl:with-param>
			<xsl:with-param name="lab_no_module_rpt">還沒有任何模塊報告</xsl:with-param>
			<xsl:with-param name="lab_item_type">請選擇類型:</xsl:with-param>
			<xsl:with-param name="lab_user_report">用戶報告</xsl:with-param>
			<xsl:with-param name="lab_usage_report">使用報告</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">跟蹤報告</xsl:with-param>
			<xsl:with-param name="lab_tracking_report_desc">按用戶或課程模塊查看在線內容的跟蹤報告</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_folder">模塊夾</xsl:with-param>
			<xsl:with-param name="lab_module">模塊</xsl:with-param>
			<xsl:with-param name="lab_A">甲</xsl:with-param>
			<xsl:with-param name="lab_B">乙</xsl:with-param>
			<xsl:with-param name="lab_C">丙</xsl:with-param>
			<xsl:with-param name="lab_D">丁</xsl:with-param>
			<xsl:with-param name="lab_F">不合格</xsl:with-param>
			<xsl:with-param name="current_role" select="$current_role"/>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="pre_content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_name">标题</xsl:with-param>
			<xsl:with-param name="lab_online_performance">成绩</xsl:with-param>
			<xsl:with-param name="lab_status">学习情况</xsl:with-param>
			<xsl:with-param name="lab_avg_per_learner">每个学员平均访问时间</xsl:with-param>
			<xsl:with-param name="lab_hits">访问次数</xsl:with-param>
			<xsl:with-param name="lab_avg_per_hit">每次平均访问时间</xsl:with-param>
			<xsl:with-param name="lab_last_access">上次访问时间</xsl:with-param>
			<xsl:with-param name="lab_avg_score">平均成绩(%)</xsl:with-param>
			<xsl:with-param name="lab_detail">详细报告</xsl:with-param>
			<xsl:with-param name="lab_status_viewed">己查阅</xsl:with-param>
			<xsl:with-param name="lab_status_not_viewed">未查阅</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_module_list">模块清单</xsl:with-param>
			<xsl:with-param name="lab_folder_list">模块夹清单</xsl:with-param>
			<xsl:with-param name="lab_no_module_rpt">还没有任何模块报告</xsl:with-param>
			<xsl:with-param name="lab_item_type">请选择类型：</xsl:with-param>
			<xsl:with-param name="lab_user_report">用户报告</xsl:with-param>
			<xsl:with-param name="lab_usage_report">使用报告</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">跟踪报告</xsl:with-param>
			<xsl:with-param name="lab_tracking_report_desc">按用户或按课程模块查看在线内容的跟踪报告。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_folder">模块夹</xsl:with-param>
			<xsl:with-param name="lab_module">模块</xsl:with-param>
			<xsl:with-param name="lab_A">甲</xsl:with-param>
			<xsl:with-param name="lab_B">乙</xsl:with-param>
			<xsl:with-param name="lab_C">丙</xsl:with-param>
			<xsl:with-param name="lab_D">丁</xsl:with-param>
			<xsl:with-param name="lab_F">不合格</xsl:with-param>
			<xsl:with-param name="current_role" select="$current_role"/>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="pre_content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
			<xsl:with-param name="lab_name">Title</xsl:with-param>
			<xsl:with-param name="lab_online_performance">Performance</xsl:with-param>
			<xsl:with-param name="lab_status">Status summary</xsl:with-param>
			<xsl:with-param name="lab_avg_per_learner">Average time spent per learner</xsl:with-param>
			<xsl:with-param name="lab_hits">Hits</xsl:with-param>
			<xsl:with-param name="lab_avg_per_hit">Average time spent per hit</xsl:with-param>
			<xsl:with-param name="lab_last_access">Last accessed</xsl:with-param>
			<xsl:with-param name="lab_avg_score">Average score(%)</xsl:with-param>
			<xsl:with-param name="lab_detail">Detail report</xsl:with-param>
			<xsl:with-param name="lab_status_viewed">Viewed</xsl:with-param>
			<xsl:with-param name="lab_status_not_viewed">Not viewed</xsl:with-param>
			<xsl:with-param name="lab_all">All</xsl:with-param>
			<xsl:with-param name="lab_module_list">Module list</xsl:with-param>
			<xsl:with-param name="lab_folder_list">Foler list</xsl:with-param>
			<xsl:with-param name="lab_no_module_rpt">There is no module report</xsl:with-param>
			<xsl:with-param name="lab_item_type">Please select type :</xsl:with-param>
			<xsl:with-param name="lab_user_report">User report</xsl:with-param>
			<xsl:with-param name="lab_usage_report">Usage report</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">Tracking report</xsl:with-param>
			<xsl:with-param name="lab_tracking_report_desc">View the online content tracking report by users or course modules.</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_folder">Folders</xsl:with-param>
			<xsl:with-param name="lab_module">Modules</xsl:with-param>
			<xsl:with-param name="lab_A">A</xsl:with-param>
			<xsl:with-param name="lab_B">B</xsl:with-param>
			<xsl:with-param name="lab_C">C</xsl:with-param>
			<xsl:with-param name="lab_D">D</xsl:with-param>
			<xsl:with-param name="lab_F">Fail</xsl:with-param>
			<xsl:with-param name="current_role" select="$current_role"/>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="pre_content">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_name"/>
		<xsl:param name="lab_online_performance"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_avg_per_learner"/>
		<xsl:param name="lab_hits"/>
		<xsl:param name="lab_avg_per_hit"/>
		<xsl:param name="lab_last_access"/>
		<xsl:param name="lab_avg_score"/>
		<xsl:param name="lab_detail"/>
		<xsl:param name="lab_status_viewed"/>
		<xsl:param name="lab_status_not_viewed"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_module_list"/>
		<xsl:param name="lab_folder_list"/>
		<xsl:param name="lab_no_module_rpt"/>
		<xsl:param name="lab_item_type"/>
		<xsl:param name="lab_user_report"/>
		<xsl:param name="lab_usage_report"/>
		<xsl:param name="lab_tracking_report"/>
		<xsl:param name="lab_tracking_report_desc"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_folder"/>
		<xsl:param name="lab_module"/>
		<xsl:param name="lab_A"/>
		<xsl:param name="lab_B"/>
		<xsl:param name="lab_C"/>
		<xsl:param name="lab_D"/>
		<xsl:param name="lab_F"/>
		<xsl:param name="current_role" />
		<xsl:param name="parent_code"/>
		<xsl:call-template name="content">
		<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
		<xsl:with-param name="lab_name" select="$lab_name"/>
		<xsl:with-param name="lab_online_performance" select="$lab_online_performance"/>
		<xsl:with-param name="lab_status" select="$lab_status"/>
		<xsl:with-param name="lab_avg_per_learner" select="$lab_avg_per_learner"/>
		<xsl:with-param name="lab_hits" select="$lab_hits"/>
		<xsl:with-param name="lab_avg_per_hit" select="$lab_avg_per_hit"/>
		<xsl:with-param name="lab_last_access" select="$lab_last_access"/>
		<xsl:with-param name="lab_avg_score" select="$lab_avg_score"/>
		<xsl:with-param name="lab_detail" select="$lab_detail"/>
		<xsl:with-param name="lab_status_viewed" select="$lab_status_viewed"/>
		<xsl:with-param name="lab_status_not_viewed" select="$lab_status_not_viewed"/>
		<xsl:with-param name="lab_all" select="$lab_all"/>
		<xsl:with-param name="lab_module_list" select="$lab_module_list"/>
		<xsl:with-param name="lab_folder_list" select="$lab_folder_list"/>
		<xsl:with-param name="lab_no_module_rpt" select="$lab_no_module_rpt"/>
		<xsl:with-param name="lab_item_type" select="$lab_item_type"/>
		<xsl:with-param name="lab_user_report" select="$lab_user_report"/>
		<xsl:with-param name="lab_usage_report" select="$lab_usage_report"/>
		<xsl:with-param name="lab_tracking_report" select="$lab_tracking_report"/>
		<xsl:with-param name="lab_tracking_report_desc" select="$lab_tracking_report_desc"/>
		<xsl:with-param name="lab_g_form_btn_close" select="$lab_g_form_btn_close"/>
		<xsl:with-param name="lab_folder" select="$lab_folder"/>
		<xsl:with-param name="lab_module" select="$lab_module"/>
		<xsl:with-param name="lab_A" select="$lab_A"/>
		<xsl:with-param name="lab_B" select="$lab_B"/>
		<xsl:with-param name="lab_C" select="$lab_C"/>
		<xsl:with-param name="lab_D" select="$lab_D"/>
		<xsl:with-param name="lab_F" select="$lab_F"/>
		<xsl:with-param name="parent_code" select="$parent_code"/>
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
				<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_tracking_report"/>
				</span>
			</xsl:with-param>
			<xsl:with-param name="btn_ok_link">javascript:rpt.usage_mod_lst_prep</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
</xsl:stylesheet>
