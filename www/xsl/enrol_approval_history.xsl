<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<!-- itm utils -->
	<xsl:import href="share/itm_gen_details_share.xsl"/>
	<xsl:import href="share/enrol_approval_share.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="itm_id" select="applyeasy/item/@id"/>
	<xsl:variable name="itm_updated_timestamp" select="/applyeasy/item/last_updated/@timestamp"/>
	<xsl:variable name="app_id" select="/applyeasy/application/@id"/>
	<xsl:variable name="app_ent_id" select="/applyeasy/application/applicant/@ent_id"/>
	<xsl:variable name="app_name" select="/applyeasy/application/applicant/display_name"/>
	<xsl:variable name="cos_info" select="/applyeasy/item_template/item/valued_template/section/*"/>
	<xsl:variable name="enrollment_log" select="/applyeasy/application/history/action_history"/>
	<xsl:variable name="app_update_timestamp" select="/applyeasy/application/@update_datetime"/>
	<xsl:variable name="application_process" select="/applyeasy/application/application_process"/>
	<xsl:variable name="my_role" select="/applyeasy/meta/cur_usr/role/@id"/>
	<xsl:variable name="root_ent_id" select="/applyeasy/meta/cur_usr/@root_ent_id"/>
	<xsl:variable name="process_id" select="/applyeasy/application/application_process/process/@id"/>
	<xsl:variable name="status_id" select="/applyeasy/application/application_process/process/status/@id"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript"><![CDATA[
					usr = new wbUserGroup;
					itm = new wbItem;
					app =  new wbApplication;
					function srh_lrn_history_prep(ent_id){			
												var url = wb_utils_invoke_disp_servlet('module', 'report.ReportModule', 'cmd', 'lrn_soln_hist', 'usr_ent_id', ent_id,  'stylesheet', 'lrn_history_view.xsl', 'p', '1', 'sort_col', 'att_timestamp', 'sort_order', 'desc');						var str_feature = 'width='+ '780'
							+ ',height=' 				+ '500'
							+ ',scrollbars='			+ 'yes'
							+ ',resizable='			+ 'yes'
							+ ',toolbar='				+ 'no'
							+ ',screenX='				+ '10'
							+ ',screenY='				+ '10'
							+ ',status='				+ 'yes';
						wbUtilsOpenWin(url,'view_lrn_history',false,str_feature);
					}
				]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmAction">
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="app_id_lst" value=""/>
				<input type="hidden" name="app_id" value="{$app_id}"/>
				<input type="hidden" name="upd_timestamp" value="{$app_update_timestamp}"/>
				<input type="hidden" name="app_upd_timestamp_lst" value=""/>
				<input type="hidden" name="process_id" value=""/>
				<input type="hidden" name="status_id" value=""/>
				<input type="hidden" name="action_id" value=""/>
				<input type="hidden" name="fr" value=""/>
				<input type="hidden" name="to" value=""/>
				<input type="hidden" name="verb" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates>
			<xsl:with-param name="lab_enrollment_approval">
				<xsl:value-of select="$lab_const_enrollment"/>審批記錄</xsl:with-param>
			<xsl:with-param name="lab_enrollment_details">
				<xsl:value-of select="$lab_const_enrollment"/>詳情</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_view_lrn_his">查看學習記錄</xsl:with-param>
			<xsl:with-param name="lab_learner">學員</xsl:with-param>
			<xsl:with-param name="lab_course">課程</xsl:with-param>
			<xsl:with-param name="lab_enrollment_log">
				<xsl:value-of select="$lab_const_enrollment"/>日誌</xsl:with-param>
			<xsl:with-param name="lab_action">操作</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_by">由</xsl:with-param>
			<xsl:with-param name="lab_remarks">原因</xsl:with-param>
			<xsl:with-param name="lab_no_action">無操作</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_pending_approver">等候審批者</xsl:with-param>
			<xsl:with-param name="lab_DIRECT_SUPERVISE">直屬上司</xsl:with-param>
			<xsl:with-param name="lab_TADM">培訓管理員</xsl:with-param>
			<xsl:with-param name="lab_SUPERVISE">用戶組上司</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>			
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates>
			<xsl:with-param name="lab_enrollment_approval">
				<xsl:value-of select="$lab_const_enrollment"/>审批记录</xsl:with-param>
			<xsl:with-param name="lab_enrollment_details">
				<xsl:value-of select="$lab_const_enrollment"/>详情</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_view_lrn_his">查看学习记录</xsl:with-param>
			<xsl:with-param name="lab_learner">学员</xsl:with-param>
			<xsl:with-param name="lab_course">课程</xsl:with-param>
			<xsl:with-param name="lab_enrollment_log">
				<xsl:value-of select="$lab_const_enrollment"/>日志</xsl:with-param>
			<xsl:with-param name="lab_action">操作</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_by">由</xsl:with-param>
			<xsl:with-param name="lab_remarks">原因</xsl:with-param>
			<xsl:with-param name="lab_no_action">无操作</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_pending_approver">等候审批者</xsl:with-param>
			<xsl:with-param name="lab_DIRECT_SUPERVISE">直属领导</xsl:with-param>
			<xsl:with-param name="lab_TADM">培训管理员</xsl:with-param>
			<xsl:with-param name="lab_SUPERVISE">用户组领导</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>			
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates>
			<xsl:with-param name="lab_enrollment_approval">
				<xsl:value-of select="$lab_const_enrollment"/> approval</xsl:with-param>
			<xsl:with-param name="lab_enrollment_details">
				<xsl:value-of select="$lab_const_enrollment"/> details</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_view_lrn_his">View learning history</xsl:with-param>
			<xsl:with-param name="lab_learner">Learner</xsl:with-param>
			<xsl:with-param name="lab_course">Course</xsl:with-param>
			<xsl:with-param name="lab_enrollment_log">
				<xsl:value-of select="$lab_const_enrollment"/> log</xsl:with-param>
			<xsl:with-param name="lab_action">Action</xsl:with-param>
			<xsl:with-param name="lab_date">Date</xsl:with-param>
			<xsl:with-param name="lab_by">By</xsl:with-param>
			<xsl:with-param name="lab_remarks">Remarks</xsl:with-param>
			<xsl:with-param name="lab_no_action">No action</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">Back</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_pending_approver">Pending approver</xsl:with-param>
			<xsl:with-param name="lab_DIRECT_SUPERVISE">Direct supervisor</xsl:with-param>
			<xsl:with-param name="lab_TADM">Training administrator</xsl:with-param>
			<xsl:with-param name="lab_SUPERVISE">Group supervisor</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>			
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="applyeasy">
		<xsl:param name="lab_enrollment_approval"/>
		<xsl:param name="lab_enrollment_details"/>
		<xsl:param name="lab_g_txt_btn_view_lrn_his"/>
		<xsl:param name="lab_learner"/>
		<xsl:param name="lab_course"/>
		<xsl:param name="lab_enrollment_log"/>
		<xsl:param name="lab_action"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_by"/>
		<xsl:param name="lab_remarks"/>
		<xsl:param name="lab_no_action"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_pending_approver"/>
		<xsl:param name="lab_DIRECT_SUPERVISE"/>
		<xsl:param name="lab_TADM"/>
		<xsl:param name="lab_SUPERVISE"/>
		<xsl:param name="lab_na"/>		
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
			
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_enrollment_approval"/>
		</xsl:call-template>
		<xsl:call-template name="enrol_approval_details">
			<xsl:with-param name="lab_enrollment_details" select="$lab_enrollment_details"/>
			<xsl:with-param name="lab_g_txt_btn_view_lrn_his" select="$lab_g_txt_btn_view_lrn_his"/>
			<xsl:with-param name="lab_learner" select="$lab_learner"/>
			<xsl:with-param name="lab_course" select="$lab_course"/>
			<xsl:with-param name="lab_enrollment_log" select="$lab_enrollment_log"/>
			<xsl:with-param name="lab_action" select="$lab_action"/>
			<xsl:with-param name="lab_date" select="$lab_date"/>
			<xsl:with-param name="lab_by" select="$lab_by"/>
			<xsl:with-param name="lab_remarks" select="$lab_remarks"/>
			<xsl:with-param name="lab_no_action" select="$lab_no_action"/>
			<xsl:with-param name="lab_status" select="$lab_status"/>
			<xsl:with-param name="lab_pending_approver" select="$lab_pending_approver"/>
			<xsl:with-param name="lab_DIRECT_SUPERVISE" select="$lab_DIRECT_SUPERVISE"/>
			<xsl:with-param name="lab_TADM" select="$lab_TADM"/>
			<xsl:with-param name="lab_SUPERVISE" select="$lab_SUPERVISE"/>
			<xsl:with-param name="lab_na" select="$lab_na"/>			
		</xsl:call-template>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_back"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:app.enrol_approval.get_my_appn_approval_list('HISTORY')</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!--DENNIS-->
	<!-- =============================================================== -->
</xsl:stylesheet>
