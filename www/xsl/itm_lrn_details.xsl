<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<!-- itm utils -->
	<xsl:import href="share/itm_gen_details_share.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	
<xsl:output indent="yes"  />

<xsl:variable name="cos_res_id" select="/applyeasy/item/@cos_res_id"/>
<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
<xsl:variable name="itm_type" select="/applyeasy/item/@type"/>
<xsl:variable name="ent_id" select="/applyeasy/meta/cur_usr/@ent_id"/>
<xsl:variable name="itm_updated_timestamp" select="/applyeasy/item/last_updated/@timestamp"/>
<xsl:variable name="itm_status" select="/applyeasy/item/@status"/>
<xsl:variable name="turn_itm_status"><xsl:choose><xsl:when test="$itm_status = 'OFF'">ON</xsl:when><xsl:otherwise>OFF</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="page_variant" select="/applyeasy/meta/page_variant"/>	
<xsl:variable name="create_run_ind" select="/applyeasy/item/@create_run_ind"/>
<xsl:variable name="cur_month" select="/applyeasy/item/child_items/cur_time/@month"/>
<xsl:variable name="cur_year" select="/applyeasy/item/child_items/cur_time/@year"/>	
<xsl:variable name="itm_type_list_root" select="/applyeasy/item/child_items/item_type_list"/>
<xsl:variable name="itm_apply_ind" select="/applyeasy/item/item_type_meta/@apply_ind"/>
<xsl:variable name="run_ind" select="/applyeasy/item/@run_ind"/>
<xsl:variable name="usr_id" select="/applyeasy/meta/cur_usr/@ent_id"/>	
<!-- =============================================================== -->
<xsl:template match="/">
	<xsl:apply-templates select="applyeasy"/>
</xsl:template>
<!-- =============================================================== -->
<xsl:template match="applyeasy">
	<html>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
			<title><xsl:value-of select="$wb_wizbank"/></title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_message.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_announcement.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_report.js"/>
			<script language="javascript" type="text/javascript" src="{$wb_js_path}wb_criteria.js" />
			<script language="javascript" type="text/javascript" src="{$wb_js_path}wb_lrn_soln.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				crit = new wbCriteria
				msg = new wbMessage
				itm_lst = new wbItem
				app = new wbApplication
				ann = new wbAnnouncement
				course_lst = new wbCourse	
				rpt = new wbReport			
				crit = new wbCriteria	
				lrn_soln =  new wbLearnSolution	
				usr = new wbUserGroup	
			]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</html>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="lang_ch">
	<xsl:apply-templates select="item">
		<xsl:with-param name="lab_learning_solution_details">課程詳情 - </xsl:with-param>
		<xsl:with-param name="lab_course_started">課程已開始</xsl:with-param>
		<xsl:with-param name="lab_enrolled"><xsl:value-of select="$lab_const_enrolled"/></xsl:with-param>
		<xsl:with-param name="lab_another_run_has_been_applied">已申請了另一個<xsl:value-of select="$lab_const_enrolled_sm"/>。<xsl:value-of select="$lab_const_run"/> .</xsl:with-param>
		<xsl:with-param name="lab_open_for_enrollment">開始報名<xsl:value-of select="$lab_const_enrollment_sm"/></xsl:with-param>
		<xsl:with-param name="lab_enrollment_closed"><xsl:value-of select="$lab_const_enrollment"/>報名已結束</xsl:with-param>
		<xsl:with-param name="lab_enrollment_not_ready"><xsl:value-of select="$lab_const_enrollment"/>報名還未準備好</xsl:with-param>
		<xsl:with-param name="lab_enrollment_not_started"><xsl:value-of select="$lab_const_enrollment"/>報名尚未開始</xsl:with-param>
		<xsl:with-param name="lab_run_cancelled"><xsl:value-of select="$lab_const_run"/>已取消</xsl:with-param>
		<xsl:with-param name="lab_item_status"><xsl:value-of select="$lab_const_enrollment"/>狀態</xsl:with-param>
		<xsl:with-param name="lab_enrollment_for_workflow"><xsl:value-of select="$lab_const_enrollment"/></xsl:with-param>
		<xsl:with-param name="lab_home">首頁</xsl:with-param>
		<xsl:with-param name="lab_code">編碼</xsl:with-param>
		<xsl:with-param name="lab_name">課程名稱</xsl:with-param>
		<xsl:with-param name="lab_type">類型</xsl:with-param>
		<xsl:with-param name="lab_ppl_allow">人數</xsl:with-param>
		<xsl:with-param name="lab_status">狀態</xsl:with-param>
		<xsl:with-param name="lab_prpts">屬性</xsl:with-param>
		<xsl:with-param name="lab_stdy_mrks">學分</xsl:with-param>
		<xsl:with-param name="lab_start_time">開始日期</xsl:with-param>
		<xsl:with-param name="lab_status_on">在線</xsl:with-param>
		<xsl:with-param name="lab_status_off">離線</xsl:with-param>
		<xsl:with-param name="lab_require">必修</xsl:with-param>
		<xsl:with-param name="lab_elective">選修</xsl:with-param>
		<xsl:with-param name="lab_day">日</xsl:with-param>
		<xsl:with-param name="lab_type_other">其他</xsl:with-param>
		<xsl:with-param name="lab_itm_empty">此類沒有課程</xsl:with-param>
		<xsl:with-param name="lab_month">月</xsl:with-param>
		<xsl:with-param name="lab_unlimit">無限</xsl:with-param>
		<xsl:with-param name="lab_prog_dtl">課程</xsl:with-param>
		<xsl:with-param name="lab_self_enrol_ineligible">不能自已報名</xsl:with-param>
		<xsl:with-param name="lab_enrolled_item">你已報讀了此課程。</xsl:with-param>
		<xsl:with-param name="lab_enrolled_run_item">你已報讀了此班級。</xsl:with-param>	
		<xsl:with-param name="lab_g_txt_btn_add_learning_solution">新增到學習計劃</xsl:with-param>
		<xsl:with-param name="lab_g_txt_btn_launch">開始</xsl:with-param>
		<xsl:with-param name="lab_g_txt_btn_enrol_info"><xsl:value-of select="$lab_const_enrollment"/>訊息</xsl:with-param>
		<xsl:with-param name="lab_g_txt_btn_enrol"><xsl:value-of select="$lab_const_enroll"/></xsl:with-param>						
		<xsl:with-param name="lab_g_txt_btn_qr">快速參考</xsl:with-param>	
		<xsl:with-param name="lab_yes">是</xsl:with-param>
		<xsl:with-param name="lab_no">否</xsl:with-param>
		<xsl:with-param name="lab_item_quota">名額</xsl:with-param>
		<xsl:with-param name="lab_item_quota_full">已滿</xsl:with-param>
		<xsl:with-param name="lab_cancel">取消</xsl:with-param>

	</xsl:apply-templates>
</xsl:template>
<xsl:template name="lang_gb">
	<xsl:apply-templates select="item">
		<xsl:with-param name="lab_learning_solution_details">课程详情 - </xsl:with-param>
		<xsl:with-param name="lab_course_started">课程已开始</xsl:with-param>
		<xsl:with-param name="lab_enrolled">已报名</xsl:with-param>
		<xsl:with-param name="lab_another_run_has_been_applied">已申请了另一个<xsl:value-of select="$lab_const_run"/>。</xsl:with-param>
		<xsl:with-param name="lab_open_for_enrollment">开放报名</xsl:with-param>
		<xsl:with-param name="lab_enrollment_closed">报名已结束</xsl:with-param>
		<xsl:with-param name="lab_enrollment_not_ready">报名还未准备好</xsl:with-param>
		<xsl:with-param name="lab_enrollment_not_started">报名未开始</xsl:with-param>
		<xsl:with-param name="lab_run_cancelled"><xsl:value-of select="$lab_const_run"/>已取消</xsl:with-param>
		<xsl:with-param name="lab_item_status">报名状态</xsl:with-param>
		<xsl:with-param name="lab_enrollment_for_workflow"><xsl:value-of select="$lab_const_enrollment"/></xsl:with-param>
		<xsl:with-param name="lab_home">首页</xsl:with-param>
		<xsl:with-param name="lab_code">编码</xsl:with-param>
		<xsl:with-param name="lab_name">课程名称</xsl:with-param>
		<xsl:with-param name="lab_type">类型</xsl:with-param>
		<xsl:with-param name="lab_ppl_allow">人数</xsl:with-param>
		<xsl:with-param name="lab_status">状态</xsl:with-param>
		<xsl:with-param name="lab_prpts">属性</xsl:with-param>
		<xsl:with-param name="lab_stdy_mrks">学分</xsl:with-param>
		<xsl:with-param name="lab_start_time">开始日期</xsl:with-param>
		<xsl:with-param name="lab_status_on">在线</xsl:with-param>
		<xsl:with-param name="lab_status_off">离线</xsl:with-param>
		<xsl:with-param name="lab_require">必选</xsl:with-param>
		<xsl:with-param name="lab_elective">选修</xsl:with-param>
		<xsl:with-param name="lab_day">日</xsl:with-param>
		<xsl:with-param name="lab_type_other">其他</xsl:with-param>
		<xsl:with-param name="lab_itm_empty">此类没有课程</xsl:with-param>
		<xsl:with-param name="lab_month">月</xsl:with-param>
		<xsl:with-param name="lab_unlimit">无限</xsl:with-param>
		<xsl:with-param name="lab_prog_dtl">课程</xsl:with-param>
		<xsl:with-param name="lab_self_enrol_ineligible">不能自报名</xsl:with-param>
		<xsl:with-param name="lab_enrolled_item">你已经报读了此课程。</xsl:with-param>
		<xsl:with-param name="lab_enrolled_run_item">你已经报读了此班级。</xsl:with-param>
		<xsl:with-param name="lab_g_txt_btn_add_learning_solution">添加到学习计划</xsl:with-param>
		<xsl:with-param name="lab_g_txt_btn_launch">开始</xsl:with-param>
		<xsl:with-param name="lab_g_txt_btn_enrol_info"><xsl:value-of select="$lab_const_enrollment"/>信息</xsl:with-param>
		<xsl:with-param name="lab_g_txt_btn_enrol"><xsl:value-of select="$lab_const_enroll"/></xsl:with-param>				
		<xsl:with-param name="lab_g_txt_btn_qr">快速参考</xsl:with-param>	
		<xsl:with-param name="lab_yes">是</xsl:with-param>
		<xsl:with-param name="lab_no">否</xsl:with-param>
		<xsl:with-param name="lab_item_quota">名额</xsl:with-param>
		<xsl:with-param name="lab_item_quota_full">已满</xsl:with-param>
		<xsl:with-param name="lab_cancel">取消</xsl:with-param>
	
	</xsl:apply-templates>
</xsl:template>
<xsl:template name="lang_en">
	<xsl:apply-templates select="item">
		<xsl:with-param name="lab_learning_solution_details">Learning solution details - </xsl:with-param>
		<xsl:with-param name="lab_course_started">Course started</xsl:with-param>
		<xsl:with-param name="lab_enrolled"><xsl:value-of select="$lab_const_enrolled"/></xsl:with-param>
		<xsl:with-param name="lab_another_run_has_been_applied">You have <xsl:value-of select="$lab_const_enrolled_sm"/> to another <xsl:value-of select="$lab_const_run"/>.</xsl:with-param>
		<xsl:with-param name="lab_open_for_enrollment">Open for <xsl:value-of select="$lab_const_enrollment_sm"/></xsl:with-param>
		<xsl:with-param name="lab_enrollment_closed"><xsl:value-of select="$lab_const_enrollment"/> closed</xsl:with-param>
		<xsl:with-param name="lab_enrollment_not_ready"><xsl:value-of select="$lab_const_enrollment"/> not ready</xsl:with-param>
		<xsl:with-param name="lab_enrollment_not_started"><xsl:value-of select="$lab_const_enrollment"/> not started</xsl:with-param>
		<xsl:with-param name="lab_run_cancelled"><xsl:value-of select="$lab_const_run"/> cancelled</xsl:with-param>
		<xsl:with-param name="lab_item_status"><xsl:value-of select="$lab_const_enrollment"/> status</xsl:with-param>
		<xsl:with-param name="lab_enrollment_for_workflow"><xsl:text> </xsl:text><xsl:value-of select="$lab_const_enrollment"/></xsl:with-param>
		<xsl:with-param name="lab_home">Home</xsl:with-param>
		<xsl:with-param name="lab_code">Code</xsl:with-param>
		<xsl:with-param name="lab_name">Course name</xsl:with-param>
		<xsl:with-param name="lab_type">Type</xsl:with-param>
		<xsl:with-param name="lab_ppl_allow">Capacity</xsl:with-param>
		<xsl:with-param name="lab_status">Status</xsl:with-param>
		<xsl:with-param name="lab_prpts">Properties</xsl:with-param>
		<xsl:with-param name="lab_stdy_mrks">Marks</xsl:with-param>
		<xsl:with-param name="lab_start_time">Start date</xsl:with-param>
		<xsl:with-param name="lab_status_on">Online</xsl:with-param>
		<xsl:with-param name="lab_status_off">Offline</xsl:with-param>
		<xsl:with-param name="lab_require">Require</xsl:with-param>
		<xsl:with-param name="lab_elective">Elective</xsl:with-param>
		<xsl:with-param name="lab_day">day</xsl:with-param>
		<xsl:with-param name="lab_type_other">Other</xsl:with-param>
		<xsl:with-param name="lab_itm_empty">No learning solution found</xsl:with-param>
		<xsl:with-param name="lab_month">Month</xsl:with-param>
		<xsl:with-param name="lab_unlimit">Unlimit</xsl:with-param>
		<xsl:with-param name="lab_prog_dtl">Item</xsl:with-param>
		<xsl:with-param name="lab_self_enrol_ineligible">Self-<xsl:value-of select="$lab_const_enrollment_sm"/> ineligible</xsl:with-param>
		<xsl:with-param name="lab_enrolled_item">You have <xsl:value-of select="$lab_const_enrolled_sm"/> to this course.</xsl:with-param>
		<xsl:with-param name="lab_enrolled_run_item">You have <xsl:value-of select="$lab_const_enrolled_sm"/> to this class.</xsl:with-param>
		<xsl:with-param name="lab_g_txt_btn_add_learning_solution">Add to learning plan</xsl:with-param>
		<xsl:with-param name="lab_g_txt_btn_launch">Start</xsl:with-param>
		<xsl:with-param name="lab_g_txt_btn_enrol_info"><xsl:value-of select="$lab_const_enrollment"/> information</xsl:with-param>
		<xsl:with-param name="lab_g_txt_btn_enrol"><xsl:value-of select="$lab_const_enroll"/> now</xsl:with-param>		
		<xsl:with-param name="lab_g_txt_btn_qr">Quick reference</xsl:with-param>		
		<xsl:with-param name="lab_yes">Yes</xsl:with-param>
		<xsl:with-param name="lab_no">No</xsl:with-param>
		<xsl:with-param name="lab_item_quota">Quota</xsl:with-param>
		<xsl:with-param name="lab_item_quota_full">Full</xsl:with-param>
		<xsl:with-param name="lab_cancel">Cancel</xsl:with-param>
	</xsl:apply-templates>
</xsl:template>
<!-- =============================================================== -->
<xsl:template match="item">
	<xsl:param name="lab_learning_solution_details"/>
	<xsl:param name="lab_course_started"/>
	<xsl:param name="lab_enrolled"/>
	<xsl:param name="lab_another_run_has_been_applied"/>
	<xsl:param name="lab_open_for_enrollment"/>
	<xsl:param name="lab_enrollment_closed"/>
	<xsl:param name="lab_enrollment_not_ready"/>
	<xsl:param name="lab_enrollment_not_started"/>
	<xsl:param name="lab_run_cancelled"/>
	<xsl:param name="lab_item_status"/>
	<xsl:param name="lab_enrollment_for_workflow"/>
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
	<xsl:param name="lab_self_enrol_ineligible"/>
	<xsl:param name="lab_enrolled_item"/>
	<xsl:param name="lab_enrolled_run_item"/>
	<xsl:param name="lab_g_txt_btn_add_learning_solution"/>
	<xsl:param name="lab_g_txt_btn_launch"/>
	<xsl:param name="lab_g_txt_btn_enrol_info"/>
	<xsl:param name="lab_g_txt_btn_enrol"/>
	<xsl:param name="lab_g_txt_btn_qr"/>
	<xsl:param name="lab_yes"/>
	<xsl:param name="lab_no"/>
	<xsl:param name="lab_item_quota"/>
	<xsl:param name="lab_item_quota_full"/>
	<xsl:param name="lab_cancel"/>

	<xsl:call-template name="wb_ui_hdr">
		<xsl:with-param name="belong_module">FTN_AMD_CAT_MAIN</xsl:with-param>
	</xsl:call-template>
	<xsl:call-template name="wb_ui_title">
		<xsl:with-param name="text">
			<xsl:value-of select="title"/>		
		</xsl:with-param>		
	</xsl:call-template>
	
	<form name="frmAction">
	<xsl:apply-templates select="valued_template/section">
		<xsl:with-param name="lab_item_status" select="$lab_item_status"/>
		<xsl:with-param name="lab_enrollment_for_workflow" select="$lab_enrollment_for_workflow"/>
		<xsl:with-param name="lab_course_started" select="$lab_course_started"/>
		<xsl:with-param name="lab_enrolled" select="$lab_enrolled"/>
		<xsl:with-param name="lab_another_run_has_been_applied" select="$lab_another_run_has_been_applied"/>
		<xsl:with-param name="lab_open_for_enrollment" select="$lab_open_for_enrollment"/>
		<xsl:with-param name="lab_enrollment_closed" select="$lab_enrollment_closed"/>
		<xsl:with-param name="lab_enrollment_not_ready" select="$lab_enrollment_not_ready"/>
		<xsl:with-param name="lab_enrollment_not_started" select="$lab_enrollment_not_started"/>
		<xsl:with-param name="lab_run_cancelled" select="$lab_run_cancelled"/>
		<xsl:with-param name="lab_self_enrol_ineligible" select="$lab_self_enrol_ineligible"/>
		<xsl:with-param name="lab_enrolled_item" select="$lab_enrolled_item"/>
		<xsl:with-param name="lab_enrolled_run_item" select="$lab_enrolled_run_item"/>
		<xsl:with-param name="lab_g_txt_btn_add_learning_solution" select="$lab_g_txt_btn_add_learning_solution"/>
		<xsl:with-param name="lab_g_txt_btn_launch" select="$lab_g_txt_btn_launch"/>
		<xsl:with-param name="lab_g_txt_btn_enrol_info" select="$lab_g_txt_btn_enrol_info"/>
		<xsl:with-param name="lab_g_txt_btn_enrol" select="$lab_g_txt_btn_enrol"/>
		<xsl:with-param name="lab_g_txt_btn_qr" select="$lab_g_txt_btn_qr"/>
		<xsl:with-param name="is_root">true</xsl:with-param>
		<xsl:with-param name="show_title">false</xsl:with-param>
		<xsl:with-param name="lab_yes" select="$lab_yes"/>
		<xsl:with-param name="lab_no" select="$lab_no"/>
		<xsl:with-param name="lab_item_quota" select="$lab_item_quota"/>
		<xsl:with-param name="lab_item_quota_full" select="$lab_item_quota_full"/>
		<xsl:with-param name="lab_cancel" select="$lab_cancel"/>
	</xsl:apply-templates>
	<xsl:if test="count(run_item_list/item/valued_template/section) != 0">
	<xsl:call-template name="wb_ui_head">
		<xsl:with-param name="text">
		<xsl:value-of select="run_item_list/item/valued_template/section/title/desc[@lan = $wb_lang_encoding]/@name"/>
		</xsl:with-param>
	</xsl:call-template>
	</xsl:if>
	<xsl:apply-templates select="run_item_list/item/valued_template/section">
		<xsl:with-param name="lab_item_status" select="$lab_item_status"/>
		<xsl:with-param name="lab_enrollment_for_workflow" select="$lab_enrollment_for_workflow"/>
		<xsl:with-param name="lab_course_started" select="$lab_course_started"/>
		<xsl:with-param name="lab_enrolled" select="$lab_enrolled"/>
		<xsl:with-param name="lab_another_run_has_been_applied" select="$lab_another_run_has_been_applied"/>
		<xsl:with-param name="lab_open_for_enrollment" select="$lab_open_for_enrollment"/>
		<xsl:with-param name="lab_enrollment_closed" select="$lab_enrollment_closed"/>
		<xsl:with-param name="lab_enrollment_not_ready" select="$lab_enrollment_not_ready"/>
		<xsl:with-param name="lab_enrollment_not_started" select="$lab_enrollment_not_started"/>
		<xsl:with-param name="lab_run_cancelled" select="$lab_run_cancelled"/>
		<xsl:with-param name="lab_self_enrol_ineligible" select="$lab_self_enrol_ineligible"/>
		<xsl:with-param name="lab_enrolled_item" select="$lab_enrolled_item"/>
		<xsl:with-param name="lab_enrolled_run_item" select="$lab_enrolled_run_item"/>
		<xsl:with-param name="lab_g_txt_btn_add_learning_solution" select="$lab_g_txt_btn_add_learning_solution"/>
		<xsl:with-param name="lab_g_txt_btn_launch" select="$lab_g_txt_btn_launch"/>
		<xsl:with-param name="lab_g_txt_btn_enrol_info" select="$lab_g_txt_btn_enrol_info"/>
		<xsl:with-param name="lab_g_txt_btn_enrol" select="$lab_g_txt_btn_enrol"/>
		<xsl:with-param name="lab_g_txt_btn_qr" select="$lab_g_txt_btn_qr"/>
		<xsl:with-param name="is_root">false</xsl:with-param>
		<xsl:with-param name="show_title">false</xsl:with-param>
		<xsl:with-param name="indent">1</xsl:with-param>
		<xsl:with-param name="lab_item_quota" select="$lab_item_quota"/>
		<xsl:with-param name="lab_item_quota_full" select="$lab_item_quota_full"/>
		<xsl:with-param name="lab_cancel" select="$lab_cancel"/>
	</xsl:apply-templates>
	<xsl:if test="count(session_item_list/item/valued_template/section) != 0">
	<xsl:call-template name="wb_ui_space"/>
	<xsl:call-template name="wb_ui_head">
		<xsl:with-param name="text">
		<xsl:value-of select="run_item_list/item/valued_template/section/title/desc[@lan = $wb_lang_encoding]/@namec"/>
		</xsl:with-param>
	</xsl:call-template>
	</xsl:if>
	<xsl:apply-templates select="session_item_list/item/valued_template/section">
		<xsl:with-param name="lab_item_status" select="$lab_item_status"/>
		<xsl:with-param name="lab_enrollment_for_workflow" select="$lab_enrollment_for_workflow"/>
		<xsl:with-param name="lab_course_started" select="$lab_course_started"/>
		<xsl:with-param name="lab_enrolled" select="$lab_enrolled"/>
		<xsl:with-param name="lab_another_run_has_been_applied" select="$lab_another_run_has_been_applied"/>
		<xsl:with-param name="lab_open_for_enrollment" select="$lab_open_for_enrollment"/>
		<xsl:with-param name="lab_enrollment_closed" select="$lab_enrollment_closed"/>
		<xsl:with-param name="lab_enrollment_not_ready" select="$lab_enrollment_not_ready"/>
		<xsl:with-param name="lab_enrollment_not_started" select="$lab_enrollment_not_started"/>
		<xsl:with-param name="lab_run_cancelled" select="$lab_run_cancelled"/>
		<xsl:with-param name="lab_self_enrol_ineligible" select="$lab_self_enrol_ineligible"/>
		<xsl:with-param name="lab_enrolled_item" select="$lab_enrolled_item"/>
		<xsl:with-param name="lab_enrolled_run_item" select="$lab_enrolled_run_item"/>
		<xsl:with-param name="lab_g_txt_btn_add_learning_solution" select="$lab_g_txt_btn_add_learning_solution"/>
		<xsl:with-param name="lab_g_txt_btn_launch" select="$lab_g_txt_btn_launch"/>
		<xsl:with-param name="lab_g_txt_btn_enrol_info" select="$lab_g_txt_btn_enrol_info"/>
		<xsl:with-param name="lab_g_txt_btn_enrol" select="$lab_g_txt_btn_enrol"/>
		<xsl:with-param name="lab_g_txt_btn_qr" select="$lab_g_txt_btn_qr"/>
		<xsl:with-param name="is_root">false</xsl:with-param>
		<xsl:with-param name="show_title">false</xsl:with-param>
		<xsl:with-param name="indent">1</xsl:with-param>
		<xsl:with-param name="lab_yes" select="$lab_yes"/>
		<xsl:with-param name="lab_no" select="$lab_no"/>
		<xsl:with-param name="lab_cancel" select="$lab_cancel"/>
		
	</xsl:apply-templates>
	
	<xsl:apply-templates select="child_items">
		<xsl:with-param name="lab_code" select="$lab_code"/>
		<xsl:with-param name="lab_name" select="$lab_name"/>
		<xsl:with-param name="lab_type" select="$lab_type"/>
		<xsl:with-param name="lab_ppl_allow" select="$lab_ppl_allow"/>
		<xsl:with-param name="lab_status" select="$lab_status"/>
		<xsl:with-param name="lab_prpts" select="$lab_prpts"/>
		<xsl:with-param name="lab_stdy_mrks" select="$lab_stdy_mrks"/>
		<xsl:with-param name="lab_start_time" select="$lab_start_time"/>
		<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
		<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
		<xsl:with-param name="lab_require" select="$lab_require"/>
		<xsl:with-param name="lab_elective" select="$lab_elective"/>
		<xsl:with-param name="lab_day" select="$lab_day"/>
		<xsl:with-param name="lab_type_other" select="$lab_type_other"/>
		<xsl:with-param name="lab_itm_empty" select="$lab_itm_empty"/>
		<xsl:with-param name="lab_month" select="$lab_month"/>
		<xsl:with-param name="lab_unlimit" select="$lab_unlimit"/>
		<xsl:with-param name="lab_prog_dtl" select="$lab_prog_dtl"/>
		<xsl:with-param name="lab_cancel" select="$lab_cancel"/>
	</xsl:apply-templates>
	
	<input type="hidden" name="url_success" value=""/>
	<input type="hidden" name="url_failure" value=""/>	
	<input type="hidden" name="cmd" value=""/>
	<input type="hidden" name="app_id" value=""/>
	<input type="hidden" name="process_id" value=""/>
	<input type="hidden" name="action_id" value=""/>
	<input type="hidden" name="status_id" value=""/>
	<input type="hidden" name="fr" value=""/>
	<input type="hidden" name="to" value=""/>		
	<input type="hidden" name="verb" value=""/>							
	</form>		

</xsl:template>
<!-- =============================================================== -->	
<xsl:template match="valued_template/section | run_item_list/item/valued_template/section">
	<xsl:param name="lab_learning_solution_details"/>
	<xsl:param name="lab_course_started"/>
	<xsl:param name="lab_enrolled"/>
	<xsl:param name="lab_another_run_has_been_applied"/>
	<xsl:param name="lab_open_for_enrollment"/>
	<xsl:param name="lab_enrollment_closed"/>
	<xsl:param name="lab_enrollment_not_ready"/>
	<xsl:param name="lab_enrollment_not_started"/>
	<xsl:param name="lab_run_cancelled"/>
	<xsl:param name="lab_item_status"/>
	<xsl:param name="lab_enrollment_for_workflow"/>
	<xsl:param name="lab_self_enrol_ineligible"/>
	<xsl:param name="lab_enrolled_item"/>
	<xsl:param name="lab_enrolled_run_item"/>
	<xsl:param name="lab_g_txt_btn_add_learning_solution"/>
	<xsl:param name="lab_g_txt_btn_launch"/>
	<xsl:param name="lab_g_txt_btn_enrol_info"/>
	<xsl:param name="lab_g_txt_btn_enrol"/>
	<xsl:param name="lab_g_txt_btn_qr"/>
	<xsl:param name="is_root">false</xsl:param>
	<xsl:param name="show_title">true</xsl:param>
	<xsl:param name="indent">0</xsl:param>
	<xsl:param name="lab_yes"/>
	<xsl:param name="lab_no"/>
	<xsl:param name="lab_item_quota"/>
	<xsl:param name="lab_item_quota_full"/>
	<xsl:param name="lab_cancel"/>
	

	<table>
		<xsl:attribute name="class"><xsl:if test="$indent &gt; 0">Bg</xsl:if></xsl:attribute>
		<xsl:attribute name="width"><xsl:choose><xsl:when test="$indent = 2">740</xsl:when><xsl:otherwise><xsl:value-of select="wb_gen_table_width"/></xsl:otherwise></xsl:choose></xsl:attribute>
		<tr>
			<xsl:if test="$is_root != 'true'">
				<td width="20" valign="top">
				</td>
			</xsl:if>
			<!--define width-->
			<xsl:variable name="_width">
				<xsl:choose>
					<xsl:when test="$indent = 1">740</xsl:when>
					<xsl:when test="$indent = 2">720</xsl:when>
					<xsl:otherwise><xsl:value-of select="$wb_gen_table_width"/></xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<!-- ========-->
			<td>
				<xsl:attribute name="width"><xsl:value-of select="$_width"/></xsl:attribute>
				<!-- check @is_root, if it is first level, show the couse/class title at the top bar-->
				<xsl:choose>
					<xsl:when test="$show_title = 'false'">
						<xsl:choose>
							<xsl:when test="$is_root = 'true'">
								<xsl:call-template name="wb_ui_head">
									<!--<xsl:with-param name="text">
											<xsl:choose>
												<xsl:when test="*[@paramname = 'itm_title']/@value != ''"><xsl:value-of select="*[@paramname = 'itm_title']/@value"/></xsl:when>
												<xsl:otherwise><xsl:value-of select="title/desc[@lan=$wb_lang_encoding]/@name"/></xsl:otherwise>
											</xsl:choose>									
									</xsl:with-param>-->
									<xsl:with-param name="extra_td">
										<td align="right">
											<xsl:call-template name="item_status_tracking">
												<xsl:with-param name="lab_g_txt_btn_add_learning_solution" select="$lab_g_txt_btn_add_learning_solution"/>
												<xsl:with-param name="lab_g_txt_btn_launch" select="$lab_g_txt_btn_launch"/>
												<xsl:with-param name="lab_g_txt_btn_enrol_info" select="$lab_g_txt_btn_enrol_info"/>
												<xsl:with-param name="lab_g_txt_btn_enrol" select="$lab_g_txt_btn_enrol"/>
												<xsl:with-param name="lab_g_txt_btn_qr" select="$lab_g_txt_btn_qr"/>
											</xsl:call-template>
										</td>									
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test="position() != 1">
								<xsl:call-template name="wb_ui_line">
									<xsl:with-param name="width" select="$_width"/>
								</xsl:call-template>
								</xsl:if>
								<table>
									<tr>
										<td>
											<xsl:choose>
												<xsl:when test="*[@paramname = 'itm_title']/@value != ''"><xsl:value-of select="*[@paramname = 'itm_title']/@value"/></xsl:when>
												<xsl:otherwise><xsl:value-of select="title/desc[@lan=$wb_lang_encoding]/@name"/></xsl:otherwise>
											</xsl:choose>											
										</td>
										<td width="{$_width - 190}" align="right">
											<xsl:call-template name="item_status_tracking">
												<xsl:with-param name="lab_g_txt_btn_add_learning_solution" select="$lab_g_txt_btn_add_learning_solution"/>
												<xsl:with-param name="lab_g_txt_btn_launch" select="$lab_g_txt_btn_launch"/>
												<xsl:with-param name="lab_g_txt_btn_enrol_info" select="$lab_g_txt_btn_enrol_info"/>
												<xsl:with-param name="lab_g_txt_btn_enrol" select="$lab_g_txt_btn_enrol"/>
												<xsl:with-param name="lab_g_txt_btn_qr" select="$lab_g_txt_btn_qr"/>
											</xsl:call-template>
										</td>										
									</tr>
								</table>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<!-- for backward -->
						<table>
							<tr>
								<td class="wzb-form-label">
									<xsl:value-of select="title/desc[@lan=$wb_lang_encoding]/@name"/>
								</td>
								<td class="wzb-form-control">
									<xsl:call-template name="item_status_tracking">
										<xsl:with-param name="lab_g_txt_btn_add_learning_solution" select="$lab_g_txt_btn_add_learning_solution"/>
										<xsl:with-param name="lab_g_txt_btn_launch" select="$lab_g_txt_btn_launch"/>
										<xsl:with-param name="lab_g_txt_btn_enrol_info" select="$lab_g_txt_btn_enrol_info"/>
										<xsl:with-param name="lab_g_txt_btn_enrol" select="$lab_g_txt_btn_enrol"/>
										<xsl:with-param name="lab_g_txt_btn_qr" select="$lab_g_txt_btn_qr"/>
									</xsl:call-template>
								</td>
							</tr>
						</table>
					</xsl:otherwise>
				</xsl:choose>
				<table>
					<tr>
						<td height="10" align="right" width="20%">
						</td>
						<td width="80%">
						</td>
					</tr>
					<xsl:apply-templates select="*" mode="draw_row">
						<xsl:with-param name="is_root" select="$is_root"/>
						<xsl:with-param name="show_title" select="$show_title"/>
						<xsl:with-param name="lab_yes" select="$lab_yes"/>
						<xsl:with-param name="lab_no" select="$lab_no"/>
					</xsl:apply-templates>
					<xsl:if test="../../@run_ind = 'true' or  ../../@create_run_ind = 'false'">
						<xsl:call-template name="display_item_status">
							<xsl:with-param name="lab_item_status" select="$lab_item_status"/>
							<xsl:with-param name="lab_enrollment_for_workflow" select="$lab_enrollment_for_workflow"/>
							<xsl:with-param name="lab_course_started" select="$lab_course_started"/>
							<xsl:with-param name="lab_enrolled" select="$lab_enrolled"/>
							<xsl:with-param name="lab_another_run_has_been_applied" select="$lab_another_run_has_been_applied"/>
							<xsl:with-param name="lab_open_for_enrollment" select="$lab_open_for_enrollment"/>
							<xsl:with-param name="lab_enrollment_closed" select="$lab_enrollment_closed"/>
							<xsl:with-param name="lab_enrollment_not_ready" select="$lab_enrollment_not_ready"/>
							<xsl:with-param name="lab_enrollment_not_started" select="$lab_enrollment_not_started"/>
							<xsl:with-param name="lab_run_cancelled" select="$lab_run_cancelled"/>
							<xsl:with-param name="lab_self_enrol_ineligible" select="$lab_self_enrol_ineligible"/>
							<xsl:with-param name="lab_enrolled_item" select="$lab_enrolled_item"/>
							<xsl:with-param name="lab_enrolled_run_item" select="$lab_enrolled_run_item"/>
							<xsl:with-param name="lab_item_quota" select="$lab_item_quota"/>
							<xsl:with-param name="lab_item_quota_full" select="$lab_item_quota_full"/>
							<xsl:with-param name="_width" select="$_width"/>
							<xsl:with-param name="lab_cancel" select="$lab_cancel"/>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="not(../../@refuse_exceeded_quato_enrol) or ../../@refuse_exceeded_quato_enrol != 'true'">
					<tr>
						<td colspan="2">
							<xsl:call-template name="enrollment_status">
								<xsl:with-param name="lab_g_txt_btn_enrol_info" select="$lab_g_txt_btn_enrol_info"/>
								<xsl:with-param name="lab_g_txt_btn_enrol" select="$lab_g_txt_btn_enrol"/>
							</xsl:call-template>
						</td>
					</tr>
					</xsl:if>
					<xsl:if test="(../../@run_ind = 'true' or  ../../@create_run_ind = 'false') and ../../cur_usr_enrolled/text() = 'true' and ../../aicc_data/@course_id > 0 and ../../aicc_data/@tkh_id > 0">
					<tr>
						<td colspan="2" align="center">
						<br/>
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$lab_g_txt_btn_launch"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">Javascript:course_lst.start('<xsl:value-of select="../../aicc_data/@course_id"/>', '', '<xsl:value-of select="../../aicc_data/@tkh_id"/>')</xsl:with-param>
								<xsl:with-param name="style">font:16px !important;</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
					</xsl:if>
				</table>
				<!-- if it is run, process session item list-->
				<xsl:if test="../../@run_ind = 'true'">				
				<xsl:if test="count(../../session_item_list/item/valued_template/section) != 0">
					<table>
						<tr>
							<td>
								<xsl:value-of select="../../session_item_list/item/valued_template/section/title/desc[@lan = $wb_lang_encoding]/@name"/>
							</td>
						</tr>
					</table>
					<table>
						<tr>
							<td>
							</td>
						</tr>
					</table>
				</xsl:if>
				<xsl:apply-templates select="../../session_item_list/item/valued_template/section">
					<xsl:with-param name="lab_item_status" select="$lab_item_status"/>
					<xsl:with-param name="lab_enrollment_for_workflow" select="$lab_enrollment_for_workflow"/>
					<xsl:with-param name="lab_course_started" select="$lab_course_started"/>
					<xsl:with-param name="lab_enrolled" select="$lab_enrolled"/>
					<xsl:with-param name="lab_another_run_has_been_applied" select="$lab_another_run_has_been_applied"/>
					<xsl:with-param name="lab_open_for_enrollment" select="$lab_open_for_enrollment"/>
					<xsl:with-param name="lab_enrollment_closed" select="$lab_enrollment_closed"/>
					<xsl:with-param name="lab_enrollment_not_ready" select="$lab_enrollment_not_ready"/>
					<xsl:with-param name="lab_enrollment_not_started" select="$lab_enrollment_not_started"/>
					<xsl:with-param name="lab_run_cancelled" select="$lab_run_cancelled"/>
					<xsl:with-param name="lab_self_enrol_ineligible" select="$lab_self_enrol_ineligible"/>
					<xsl:with-param name="lab_enrolled_item" select="$lab_enrolled_item"/>
					<xsl:with-param name="lab_enrolled_run_item" select="$lab_enrolled_run_item"/>
					<xsl:with-param name="lab_g_txt_btn_add_learning_solution" select="$lab_g_txt_btn_add_learning_solution"/>
					<xsl:with-param name="lab_g_txt_btn_launch" select="$lab_g_txt_btn_launch"/>
					<xsl:with-param name="lab_g_txt_btn_enrol_info" select="$lab_g_txt_btn_enrol_info"/>
					<xsl:with-param name="lab_g_txt_btn_enrol" select="$lab_g_txt_btn_enrol"/>
					<xsl:with-param name="lab_g_txt_btn_qr" select="$lab_g_txt_btn_qr"/>
					<xsl:with-param name="is_root">false</xsl:with-param>
					<xsl:with-param name="show_title">false</xsl:with-param>
					<xsl:with-param name="indent">2</xsl:with-param>
					<xsl:with-param name="lab_yes" select="$lab_yes"/>
					<xsl:with-param name="lab_no" select="$lab_no"/>
				</xsl:apply-templates>
				</xsl:if>
				<xsl:if test="$show_title = 'false' and $is_root = 'false' and position() != last()">
					<table>
						<tr>
							<td>
							</td>
						</tr>
					</table>
				</xsl:if>
			</td>
		</tr>
	</table>
</xsl:template>
<!-- =============================================================== -->
<!-- Block for draw_row mode =============================================-->	
<xsl:template match="title | hidden" mode="draw_row"/>
<!-- =============================================================== -->	
<xsl:template match="*" mode="draw_row">
	<xsl:param name="is_root">false</xsl:param>
	<xsl:param name="show_title">true</xsl:param>
	<xsl:param name="lab_yes"/>
	<xsl:param name="lab_no"/>

	<xsl:variable name="draw">
		<xsl:call-template name="get_show"/>
	</xsl:variable>
	<xsl:if test="contains($draw,'true')">
		<xsl:if test="not($show_title = 'false' and @paramname = 'itm_title')">
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:variable name="text_class">
						<xsl:choose>
							<xsl:when test="@marked='yes'">TitleTextBold</xsl:when>
							<xsl:otherwise>TitleText</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:if test="name() != 'link_list'"><xsl:variable name="field_desc"><xsl:call-template name="get_desc"/></xsl:variable><xsl:if test="$field_desc != ''"><xsl:value-of select="$field_desc"/><xsl:text>：</xsl:text></xsl:if></xsl:if>
				</td>
				<td class="wzb-form-control">
					<xsl:if test="prefix">
						<xsl:call-template name="prefix"/>
					</xsl:if>
					<xsl:apply-templates select="." mode="gen_field">
						<xsl:with-param name="text_class">Text</xsl:with-param>
						<xsl:with-param name="lab_yes" select="$lab_yes"/>
						<xsl:with-param name="lab_no" select="$lab_no"/>
					</xsl:apply-templates>
					<xsl:if test="suffix">
						<xsl:call-template name="suffix"/>
					</xsl:if>
				</td>
			</tr>
		</xsl:if>
	</xsl:if>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="display_item_status">
	<xsl:param name="lab_learning_solution_details"/>	
	<xsl:param name="lab_course_started" />		
	<xsl:param name="lab_enrolled" />
	<xsl:param name="lab_another_run_has_been_applied" />
	<xsl:param name="lab_open_for_enrollment" />
	<xsl:param name="lab_enrollment_closed" />
	<xsl:param name="lab_enrollment_not_ready" />
	<xsl:param name="lab_enrollment_not_started" />
	<xsl:param name="lab_run_cancelled" />	
	<xsl:param name="lab_item_status"/>
	<xsl:param name="lab_enrollment_for_workflow"/>
	<xsl:param name="lab_self_enrol_ineligible"/>
	<xsl:param name="lab_enrolled_item"/>
	<xsl:param name="lab_enrolled_run_item"/>	
	<xsl:param name="_width"/>
	<xsl:param name="lab_item_quota"/>
	<xsl:param name="lab_item_quota_full"/>
	<xsl:param name="lab_cancel"/>
	

	<xsl:if test="../../application/@id != ''">
		<xsl:variable name="app_id" select="../../application/@id"/>
		<tr>
			<td align="right" width="190" valign="middle">
				<font color="blue"><b><xsl:value-of select="$lab_item_status"/>:</b></font>
			</td>
			<td width="{$_width - 190}" valign="middle">

				<table>
					<tr>
						<td>
							<font color="blue">
								<b>
									<xsl:value-of select="../../application/@process_status"/>
								</b>
							</font>
						</td>
						<td>
							<xsl:variable name="can_cancel" select="../../application/@can_cancel"/>
							<xsl:for-each select="../../application_process/process/status/action">
								<xsl:variable name="js_var_suffix" select="concat('_', ../../@id, '_', ../@id, '_', @id)"/>
								<script LANGUAGE="JavaScript" TYPE="text/javascript">
									var action_this_status_name<xsl:value-of select="$js_var_suffix"/> = '<xsl:value-of select="../@name"/>';
									var action_next_status_name<xsl:value-of select="$js_var_suffix"/> = '<xsl:value-of select="next_status/@name"/>';
									var action_verb<xsl:value-of select="$js_var_suffix"/> = '<xsl:value-of select="@verb"/>';
								</script>
								<xsl:if test="$can_cancel = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_cancel"/><xsl:value-of select="$lab_enrollment_for_workflow"/></xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:app.lrn_action_exec(document.frmAction, <xsl:value-of select="$app_id"/>, <xsl:value-of select="../../@id"/>, <xsl:value-of select="../@id"/>, <xsl:value-of select="@id"/>, action_this_status_name<xsl:value-of select="$js_var_suffix"/>, action_next_status_name<xsl:value-of select="$js_var_suffix"/>, action_verb<xsl:value-of select="$js_var_suffix"/>, '<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
								</xsl:call-template>
								</xsl:if>
							</xsl:for-each>
						</td>
					
					</tr>
				</table>
			</td>
		</tr>		
	</xsl:if>
	<xsl:if test="../../application/@id = '' and ../../@refuse_exceeded_quato_enrol = 'true'">
		<tr>
			<td align="right" width="190" valign="middle">
				<font color="red"><b><xsl:value-of select="$lab_item_quota"/>:</b></font>
			</td>
			<td width="{$_width - 190}" valign="middle">
				<table>
					<tr>
						<td>
							<font color="red"><b><xsl:value-of select="$lab_item_quota_full"/></b></font>
						</td>
					</tr>
				</table>
			</td>
		</tr>		
	</xsl:if>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="item_status_tracking">
	<xsl:param name="lab_g_txt_btn_add_learning_solution" />
	<xsl:param name="lab_g_txt_btn_launch" />
	<xsl:param name="lab_g_txt_btn_enrol_info" />
	<xsl:param name="lab_g_txt_btn_enrol" />
	<xsl:param name="lab_g_txt_btn_qr" />
	<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
	<xsl:if test="not(../../@life_status = 'CANCELLED' or ../../@deprecated_ind = 'true' or (( ../../@life_status != '') and (../../@life_status != 'CANCELLED'))  or ../../@usr_can_qr_ind = 'false'  or ../../@qdb_ind='false' or ../../@content_started_ind='false' or ../../@content_closed_ind='true')">
		<!-- Quick Reference -->
		<xsl:call-template name="wb_gen_button">
			<xsl:with-param name="wb_gen_btn_name"  select="$lab_g_txt_btn_qr"/>
			<xsl:with-param name="wb_gen_btn_href">javascript:course_lst.start(<xsl:value-of select="../../@cos_res_id"/>,'','','true')</xsl:with-param>
		</xsl:call-template>
	</xsl:if>

	<xsl:if test="$page_variant/@canSelfEnrol='true'">
		<xsl:if test="../../@run_ind != 'true' and $page_variant/@hasAddLrnPlanBtn = 'true' and ../../application/@has_applied != 'true'">
			<xsl:call-template name="wb_gen_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_add_learning_solution"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:lrn_soln.add_lrn_soln(<xsl:value-of select="$ent_id"/>,<xsl:value-of select="$itm_id"/>)</xsl:with-param>
			</xsl:call-template>				
		</xsl:if>
	</xsl:if>
</xsl:template>

<!-- ========================================================= -->
<xsl:template name="enrollment_status">
	<xsl:param name="lab_g_txt_btn_enrol_info"/>
	<xsl:param name="lab_g_txt_btn_enrol"/>
	<xsl:param name="lab_g_txt_btn_launch"/>
	<xsl:choose>
		<xsl:when test="../../@auto_enrol_qdb_ind = 'true'">
			<!-- launch-->			
			<xsl:call-template name="wb_gen_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_launch"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.auto_enrol_cos(<xsl:value-of select="../../@id"/>,<xsl:value-of select="$cos_res_id"/>)</xsl:with-param>
			</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
			<xsl:if test="../../applicable/text() = 'true'">
				<div align="center">
				<br/>
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_enrol"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:app.get_application_form(<xsl:value-of select="../../@id"/>,<xsl:value-of select="$ent_id"/>,'0')</xsl:with-param>
					<xsl:with-param name="style">font:16px !important;</xsl:with-param>
				</xsl:call-template>	
				</div>			
			</xsl:if>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="item_unavailable">
	<xsl:param name="lab_g_txt_btn_enrol_info" />
	<xsl:param name="lab_g_txt_btn_enrol" />
	<xsl:param name="run_ind"/>
	<xsl:if test="$page_variant/@canSelfEnrol='true'">
		<xsl:choose>
			<xsl:when test="$run_ind = 'true'">
				<xsl:choose>
					<xsl:when test="../../../@applied_run_item_id != ''"></xsl:when>
					<xsl:otherwise>
						<!-- enrollment -->
						<xsl:if test="not(../../@life_status = 'CANCELLED' or ../../@deprecated_ind = 'true' or ../../item_type_meta/@apply_ind = 'false' or(( ../../@life_status != '') and (../../@life_status != 'CANCELLED')) or ../../@appn_started_ind = 'false' or ../../@appn_closed_ind = 'true')">
							<xsl:if test="$page_variant/@canSelfEnrol='true' and ../../@apply_ind='true'">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_enrol"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:app.get_application_form(<xsl:value-of select="../../@id"/>,<xsl:value-of select="$ent_id"/>,'0')</xsl:with-param>
							</xsl:call-template>
							</xsl:if>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$run_ind = 'false'">
				<xsl:choose>
					<xsl:when test="../../application/@id != '' and ../../application/@has_applied='true'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name"  select="$lab_g_txt_btn_enrol_info"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:app.process_lrn_application(<xsl:value-of select="../../application/@id"/>)</xsl:with-param>
						</xsl:call-template>									
					</xsl:when>
					<xsl:otherwise>
						<!-- enrollment -->
						<xsl:if test="not(../../@life_status = 'CANCELLED' or ../../@deprecated_ind = 'true' or ../../item_type_meta/@apply_ind = 'false' or(( ../../@life_status != '') and (../../@life_status != 'CANCELLED')) or ../../@appn_started_ind = 'false' or ../../@appn_closed_ind = 'true')">
							<xsl:if test="$page_variant/@canSelfEnrol='true'">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_enrol"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:app.get_application_form(<xsl:value-of select="../../@id"/>,<xsl:value-of select="$ent_id"/>,'0')</xsl:with-param>
							</xsl:call-template>
							</xsl:if>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
		</xsl:choose>
	</xsl:if>
</xsl:template>
<!-- =============================================================== -->
<xsl:template match="child_items">
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
		
		<xsl:if test="$create_run_ind = 'false'">
			<xsl:call-template name="wb_ui_space"/>
			<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="text"><xsl:value-of select="$lab_prog_dtl"/></xsl:with-param>
			</xsl:call-template>
			<xsl:choose>
				<xsl:when test="count(child) = 0 ">
					<xsl:call-template name="wb_ui_show_no_item">
						<xsl:with-param name="text" select="$lab_itm_empty"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<table class="table wzb-ui-table">
						<tr class="wzb-ui-table-head">
							<td rowspan="2">
								<xsl:value-of select="$lab_code"/>
							</td>
							<td rowspan="2">
								<xsl:value-of select="$lab_name"/>
							</td>
							<td rowspan="2" align="center">
								<xsl:value-of select="$lab_type"/>
							</td>
							<td rowspan="2" align="center">
								<xsl:value-of select="$lab_ppl_allow"/>
							</td>
							<td rowspan="2" align="center">
								<xsl:value-of select="$lab_status"/>
							</td>
							<td rowspan="2" align="center">
								<xsl:value-of select="$lab_prpts"/>
							</td>
							<td rowspan="2" align="center">
								<xsl:value-of select="$lab_stdy_mrks"/>
							</td>
							<td align="center" colspan="4">
								<xsl:value-of select="$lab_start_time"/>
							</td>
						</tr>
						<tr>
							<td align="center">
								<xsl:value-of select="$cur_month"/>
								<xsl:value-of select="$lab_month"/>
							</td>
							<td align="center">
								<xsl:value-of select="$cur_month + 1"/>
								<xsl:value-of select="$lab_month"/>
							</td>
							<td align="center">
								<xsl:value-of select="$cur_month + 2"/>
								<xsl:value-of select="$lab_month"/>
							</td>
							<td align="center">
								&gt;&gt;
							</td>
						</tr>
						<xsl:for-each select="child">
							<xsl:call-template name="course_lst">
								<xsl:with-param name="lab_status_on"><xsl:value-of select="$lab_status_on"/></xsl:with-param>
								<xsl:with-param name="lab_status_off"><xsl:value-of select="$lab_status_off"/></xsl:with-param>
								<xsl:with-param name="lab_require"><xsl:value-of select="$lab_require"/></xsl:with-param>
								<xsl:with-param name="lab_elective"><xsl:value-of select="$lab_elective"/></xsl:with-param>
								<xsl:with-param name="lab_day"><xsl:value-of select="$lab_day"/></xsl:with-param>
								<xsl:with-param name="lab_type_other"><xsl:value-of select="$lab_type_other"/></xsl:with-param>
								<xsl:with-param name="lab_unlimit"><xsl:value-of select="$lab_unlimit"/></xsl:with-param>
								<xsl:with-param name="lab_month"><xsl:value-of select="$lab_month"/></xsl:with-param>
							</xsl:call-template>
						</xsl:for-each>
					</table>
				</xsl:otherwise>
			</xsl:choose>
			<input type="hidden" name="cmd"/>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<input type="hidden" name="pgm_itm_id" value="{/applyeasy/item/@id}"/>
			<input type="hidden" name="itm_id_lst"/>
			<input type="hidden" name="node_id" value="{/applyeasy/node/@node_id}"/>
			<input type="hidden" name="pdt_upd_timestamp_lst"/>
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
		<xsl:variable name="row_class"><xsl:choose><xsl:when test="position() mod 2">wbRowsEven</xsl:when><xsl:otherwise>wbRowsOdd</xsl:otherwise></xsl:choose></xsl:variable>		
		<tr valign="middle" class="{$row_class}">
			<td>
				<xsl:value-of select="@item_code"/>&#160;
			</td>
			<td>
				<a href="javascript:itm_lst.get_item_lrn_detail({@item_id})">
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
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
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
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test=" @unit != '' ">
						<xsl:value-of select="@unit"/>
					</xsl:when>
					<xsl:otherwise>
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
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
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
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
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
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
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
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
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>

</xsl:stylesheet>
