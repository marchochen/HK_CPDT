<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="root_ent_id" select="/tptrainingplan/meta/cur_usr/@root_ent_id"/>
	<xsl:variable name="cur_time" select="tptrainingplan/meta/current_timestamp/text()"/>
	<xsl:variable name="tpn_id" select="tptrainingplan/plan/@id"/>
	<xsl:variable name="tpn_type" select="tptrainingplan/plan/@type"/>
	<xsl:variable name="upd_timestamp" select="tptrainingplan/plan/upd_timestamp/text()"/>
	<xsl:variable name="page_variant" select="/tptrainingplan/meta/page_variant"/>
	<xsl:variable name="status" select="/tptrainingplan/plan/status/text()"/>
	<xsl:variable name="entrance" select="/tptrainingplan/meta/entrance"/>
	<xsl:variable name="itm_id" select="/tptrainingplan/tp_item/@itm_id"/>
	<xsl:variable name="itm_run_ind" select="/tptrainingplan/tp_item/@itm_run_ind"/>
	<!-- =============================================================== -->
	<xsl:template match="/tptrainingplan">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}wb_objective.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}wb_search.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}wb_batchprocess.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}wb_goldenman.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_usergroup.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_training_plan.js" type="text/javascript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" TYPE="text/javascript"><![CDATA[
				goldenman = new wbGoldenMan;
				usr = new wbUserGroup;
				tp = new wbTrainingPlan;
				itm = new wbItem;
			]]></script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="">
			<form name="frmXml" onsubmit="return status()">
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="module"/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="tpn_id" value="{$tpn_id}"/>
				<input type="hidden" name="tpn_upd_timestamp" value="{$upd_timestamp}"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_training_lst">計劃實施</xsl:with-param>
			<xsl:with-param name="lab_makeup_lst">編外計劃</xsl:with-param>
			<xsl:with-param name="lab_makeup_appr">編外計劃審批</xsl:with-param>
			<xsl:with-param name="lab_training_plan">計劃資料</xsl:with-param>
			<xsl:with-param name="lab_plan_date">推出月份</xsl:with-param>
			<xsl:with-param name="lab_code">計劃編號</xsl:with-param>
			<xsl:with-param name="lab_name">培訓活動名稱</xsl:with-param>
			<xsl:with-param name="lab_type">培訓類型</xsl:with-param>
			<xsl:with-param name="lab_tcr_title">負責培訓中心</xsl:with-param>
			<xsl:with-param name="lab_catalog">目錄</xsl:with-param>
			<xsl:with-param name="lab_intr">簡介</xsl:with-param>
			<xsl:with-param name="lab_aim">目標</xsl:with-param>
			<xsl:with-param name="lab_tatget">對像</xsl:with-param>
			<xsl:with-param name="lab_rector">負責人</xsl:with-param>
			<xsl:with-param name="lab_days">時長</xsl:with-param>
			<xsl:with-param name="lab_ftf_start_time">面授開始日</xsl:with-param>
			<xsl:with-param name="lab_ftf_end_time">面授結束日</xsl:with-param>
			<xsl:with-param name="lab_wb_start_time">網上內容開始日</xsl:with-param>
			<xsl:with-param name="lab_wb_end_time">網上內容結束日</xsl:with-param>
			<xsl:with-param name="lab_lrn_count">預計參訓人數</xsl:with-param>
			<xsl:with-param name="lab_fee">預計費用</xsl:with-param>
			<xsl:with-param name="lab_remark">備註</xsl:with-param>
			<xsl:with-param name="lab_year">年</xsl:with-param>
			<xsl:with-param name="lab_month">月</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_submit">提交</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_del">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_impl">培訓計劃實施</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_appr">批准</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_decl">拒絕</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_implemented">查看培訓</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_prepared">準備中</xsl:with-param>
			<xsl:with-param name="lab_pending">等待審批</xsl:with-param>
			<xsl:with-param name="lab_approved">審批通過</xsl:with-param>
			<xsl:with-param name="lab_declined">審批未通過</xsl:with-param>
			<xsl:with-param name="lab_implemented">已實施</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_training_lst">计划实施</xsl:with-param>
			<xsl:with-param name="lab_makeup_lst">编外计划</xsl:with-param>
			<xsl:with-param name="lab_makeup_appr">编外计划审批</xsl:with-param>
			<xsl:with-param name="lab_training_plan">计划信息</xsl:with-param>
			<xsl:with-param name="lab_plan_date">推出月份</xsl:with-param>
			<xsl:with-param name="lab_code">计划编号</xsl:with-param>
			<xsl:with-param name="lab_name">培训活动名称</xsl:with-param>
			<xsl:with-param name="lab_type">培训类型</xsl:with-param>
			<xsl:with-param name="lab_tcr_title">负责培训中心</xsl:with-param>
			<xsl:with-param name="lab_catalog">目录</xsl:with-param>
			<xsl:with-param name="lab_intr">简介</xsl:with-param>
			<xsl:with-param name="lab_aim">目标</xsl:with-param>
			<xsl:with-param name="lab_tatget">对象</xsl:with-param>
			<xsl:with-param name="lab_rector">负责人</xsl:with-param>
			<xsl:with-param name="lab_days">时长</xsl:with-param>
			<xsl:with-param name="lab_ftf_start_time">面授开始日</xsl:with-param>
			<xsl:with-param name="lab_ftf_end_time">面授结束日</xsl:with-param>
			<xsl:with-param name="lab_wb_start_time">网上内容开始日</xsl:with-param>
			<xsl:with-param name="lab_wb_end_time">网上内容结束日</xsl:with-param>
			<xsl:with-param name="lab_lrn_count">预计参训人数</xsl:with-param>
			<xsl:with-param name="lab_fee">预计费用</xsl:with-param>
			<xsl:with-param name="lab_remark">备注</xsl:with-param>
			<xsl:with-param name="lab_year">年</xsl:with-param>
			<xsl:with-param name="lab_month">月</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_submit">提交</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_del">删除</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_impl">培训计划实施</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_appr">批准</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_decl">拒绝</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_implemented">查看培训</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_prepared">准备中</xsl:with-param>
			<xsl:with-param name="lab_pending">等待审批</xsl:with-param>
			<xsl:with-param name="lab_approved">审批通过</xsl:with-param>
			<xsl:with-param name="lab_declined">审批未通过</xsl:with-param>
			<xsl:with-param name="lab_implemented">已实施</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_training_lst">Training plan implementation</xsl:with-param>
			<xsl:with-param name="lab_makeup_lst">Additional training plan</xsl:with-param>
			<xsl:with-param name="lab_makeup_appr">Additional training plan approval</xsl:with-param>
			<xsl:with-param name="lab_training_plan">Training plan information</xsl:with-param>
			<xsl:with-param name="lab_plan_date">Budgeted time</xsl:with-param>
			<xsl:with-param name="lab_code">Code</xsl:with-param>
			<xsl:with-param name="lab_name">Training title</xsl:with-param>
			<xsl:with-param name="lab_type">Training type</xsl:with-param>
			<xsl:with-param name="lab_tcr_title">Training center</xsl:with-param>
			<xsl:with-param name="lab_catalog">Categories</xsl:with-param>
			<xsl:with-param name="lab_intr">Summary</xsl:with-param>
			<xsl:with-param name="lab_aim">Objective</xsl:with-param>
			<xsl:with-param name="lab_tatget">Target audience</xsl:with-param>
			<xsl:with-param name="lab_rector">Responsible person</xsl:with-param>
			<xsl:with-param name="lab_days">Duration</xsl:with-param>
			<xsl:with-param name="lab_ftf_start_time">Class start date</xsl:with-param>
			<xsl:with-param name="lab_ftf_end_time">Class end date</xsl:with-param>
			<xsl:with-param name="lab_wb_start_time">Online content start date</xsl:with-param>
			<xsl:with-param name="lab_wb_end_time">Online content end date</xsl:with-param>
			<xsl:with-param name="lab_lrn_count">Estimated participants</xsl:with-param>
			<xsl:with-param name="lab_fee">Estimated training cost</xsl:with-param>
			<xsl:with-param name="lab_remark">Remarks</xsl:with-param>
			<xsl:with-param name="lab_year">Year</xsl:with-param>
			<xsl:with-param name="lab_month">Month</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_submit">Submit</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_del">Delete</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">Back</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_impl">Implement</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_appr">Approve</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_decl">Decline</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_implemented">View implemented training</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_prepared">Preparing</xsl:with-param>
			<xsl:with-param name="lab_pending">Pending approval</xsl:with-param>
			<xsl:with-param name="lab_approved">Approved</xsl:with-param>
			<xsl:with-param name="lab_declined">Declined</xsl:with-param>
			<xsl:with-param name="lab_implemented">Implemented</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_training_lst"/>
		<xsl:param name="lab_makeup_lst"/>
		<xsl:param name="lab_makeup_appr"/>
		<xsl:param name="lab_training_plan"/>
		<xsl:param name="lab_plan_date"/>
		<xsl:param name="lab_code"/>
		<xsl:param name="lab_name"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_tcr_title"/>
		<xsl:param name="lab_catalog"/>
		<xsl:param name="lab_intr"/>
		<xsl:param name="lab_aim"/>
		<xsl:param name="lab_tatget"/>
		<xsl:param name="lab_rector"/>
		<xsl:param name="lab_days"/>
		<xsl:param name="lab_ftf_start_time"/>
		<xsl:param name="lab_ftf_end_time"/>
		<xsl:param name="lab_wb_start_time"/>
		<xsl:param name="lab_wb_end_time"/>
		<xsl:param name="lab_lrn_count"/>
		<xsl:param name="lab_fee"/>
		<xsl:param name="lab_remark"/>
		<xsl:param name="lab_year"/>
		<xsl:param name="lab_month"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_prepared"/>
		<xsl:param name="lab_pending"/>
		<xsl:param name="lab_approved"/>
		<xsl:param name="lab_declined"/>
		<xsl:param name="lab_implemented"/>
		<xsl:param name="lab_g_form_btn_edit"/>
		<xsl:param name="lab_g_form_btn_submit"/>
		<xsl:param name="lab_g_form_btn_del"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:param name="lab_g_form_btn_impl"/>
		<xsl:param name="lab_g_form_btn_appr"/>
		<xsl:param name="lab_g_form_btn_decl"/>
		<xsl:param name="lab_g_form_btn_implemented"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_PLAN_CARRY_OUT</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="plan/name/text()"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$entrance='FTN_AMD_MAKEUP_PLAN'">
						<a href="javascript:wb_utils_nav_go('FTN_AMD_MAKEUP_PLAN')" class="NavLink">
							<xsl:value-of select="$lab_makeup_lst"/>
						</a>
					</xsl:when>
					<xsl:when test="$entrance='FTN_AMD_PLAN_CARRY_OUT'">
						<a href="javascript:wb_utils_nav_go('FTN_AMD_PLAN_CARRY_OUT')" class="NavLink">
							<xsl:value-of select="$lab_training_lst"/>
						</a>
					</xsl:when>
					<xsl:when test="$entrance='FTN_AMD_MAKEUP_PLAN_APPR'">
						<a href="javascript:wb_utils_nav_go('FTN_AMD_MAKEUP_PLAN_APPR')" class="NavLink">
							<xsl:value-of select="$lab_makeup_appr"/>
						</a>
					</xsl:when>
				</xsl:choose>
				<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="plan/name/text()"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_head">
			<!-- <xsl:with-param name="text">
				<xsl:value-of select="$lab_training_plan"/>
			</xsl:with-param> -->
			<xsl:with-param name="extra_td">
				<td align="right">
					<xsl:variable name="tpn_tcr_id">
						<xsl:value-of select="plan/@tcr_id"/>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="$entrance='FTN_AMD_MAKEUP_PLAN_APPR'">
							<xsl:if test="$status = 'PENDING' and $page_variant/@hasApprovedPlanBtn ='true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_g_form_btn_appr"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:tp.upd_status_exec(<xsl:value-of select="plan/@id"/>,'A','<xsl:value-of select="plan/upd_timestamp/text()"/>','<xsl:value-of select="plan/training_center/@id"/>','MAKEUP','<xsl:value-of select="$entrance"/>')</xsl:with-param>
								</xsl:call-template>						
							</xsl:if>
							<xsl:if test="$status = 'PENDING'and $page_variant/@hasApprovedPlanBtn ='true' ">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_g_form_btn_decl"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:tp.upd_status_exec(<xsl:value-of select="plan/@id"/>,'D','<xsl:value-of select="plan/upd_timestamp/text()"/>','<xsl:value-of select="plan/training_center/@id"/>','MAKEUP','<xsl:value-of select="$entrance"/>')</xsl:with-param>
								</xsl:call-template>						
							</xsl:if>	
						</xsl:when>
						<xsl:otherwise>
							<xsl:if test="$page_variant/@hasImplementPlanBtn = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_g_form_btn_impl"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:tp.implement_plan_prep('<xsl:value-of select="plan/training_center/@id"/>','<xsl:value-of select="plan/@id"/>','<xsl:value-of select="$upd_timestamp"/>','<xsl:value-of select="$entrance"/>')</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="$page_variant/@hasEditPlanBtn = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_g_form_btn_edit"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:tp.edit_plan_prep('<xsl:value-of select="plan/@id"/>','<xsl:value-of select="plan/training_center/@id"/>','<xsl:value-of select="$entrance"/>')</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="$page_variant/@hasReferPlanBtn = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_g_form_btn_submit"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:tp.refer_makeup_plan('<xsl:value-of select="plan/@id"/>', '<xsl:value-of select="plan/training_center/@id"/>','<xsl:value-of select="plan/upd_timestamp/text()"/>','<xsl:value-of select="$entrance"/>')</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="$page_variant/@hasDeletePlanBtn = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_g_form_btn_del"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:tp.del_makeup_plan('<xsl:value-of select="plan/training_center/@id"/>', '<xsl:value-of select="plan/@id"/>', '<xsl:value-of select="plan/upd_timestamp/text()"/>','<xsl:value-of select="$entrance"/>')</xsl:with-param>
								</xsl:call-template>
							</xsl:if>	
							<xsl:if test="$status = 'IMPLEMENTED'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_g_form_btn_implemented"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">
										<xsl:choose>
											<xsl:when test="$itm_run_ind='true'">javascript:itm.get_item_run_detail('<xsl:value-of select="$itm_id"/>')</xsl:when>
											<xsl:otherwise>javascript:itm.get_item_detail('<xsl:value-of select="$itm_id"/>')</xsl:otherwise>
										</xsl:choose>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:if>	
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<xsl:variable name="tpn_tcr_id" select="//training_center/@id"/>
		<table>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_plan_date"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:variable name="plan_date">
						<xsl:value-of select="plan/plan_date/text()"/>
					</xsl:variable>
					<xsl:value-of select="substring($plan_date, 1,4)"/>
					<xsl:text>-</xsl:text>
					<xsl:value-of select="substring($plan_date, 6,2)"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_code"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="plan/code/text()"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_name"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="plan/name/text()"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_type"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="plan/cos_type/text()"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_tcr_title"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="plan/training_center/title/text()"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_catalog"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="plan/tnd_title=''">--</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="unescape_html_linefeed">
								<xsl:with-param name="my_right_value">
									<xsl:value-of select="plan/tnd_title/text()"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_intr"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="plan/introduction=''">--</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="unescape_html_linefeed">
								<xsl:with-param name="my_right_value">
									<xsl:value-of select="plan/introduction/text()"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_aim"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="plan/aim=''">--</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="unescape_html_linefeed">
								<xsl:with-param name="my_right_value">
									<xsl:value-of select="plan/aim/text()"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
			</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_tatget"/>：
				</td>
				<td class="wzb-form-control">
					<span>
						<xsl:choose>
							<xsl:when test="plan/target=''">--</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:value-of select="plan/target/text()"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_rector"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="plan/responser =''">--</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="unescape_html_linefeed">
								<xsl:with-param name="my_right_value">
									<xsl:value-of select="plan/responser/text()"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_status"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="plan/status/text() != ''">
							<xsl:choose>
								<xsl:when test="plan/status/text()='PREPARED'">
									<xsl:value-of select="$lab_prepared"/>
								</xsl:when>
								<xsl:when test="plan/status/text()='PENDING'">
									<xsl:value-of select="$lab_pending"/>
								</xsl:when>
								<xsl:when test="plan/status/text()='APPROVED'">
									<xsl:value-of select="$lab_approved"/>
								</xsl:when>
								<xsl:when test="plan/status/text()='DECLINED'">
									<xsl:value-of select="$lab_declined"/>
								</xsl:when>
								<xsl:when test="plan/status/text()='IMPLEMENTED'">
									<xsl:value-of select="$lab_implemented"/>
								</xsl:when>
								<xsl:otherwise>--</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>--</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_days"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="plan/duration =''">--</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="unescape_html_linefeed">
								<xsl:with-param name="my_right_value">
									<xsl:value-of select="plan/duration/text()"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_ftf_start_time"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="plan/ftf_start_date=''">--</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="plan/ftf_start_date/text()"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_ftf_end_time"/>：
				</td>
				<td class="wzb-form-control">
					<span>
						<xsl:choose>
							<xsl:when test="plan/ftf_end_date=''">--</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="plan/ftf_end_date/text()"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_wb_start_time"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="plan/wb_start_date=''">--</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="plan/wb_start_date/text()"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_wb_end_time"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="plan/wb_end_date=''">--</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="plan/wb_end_date/text()"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_lrn_count"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="plan/lrn_count/text()='0'">--</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="plan/lrn_count/text()"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_fee"/>：
				</td>
				<td class="wzb-form-control">
					<span>
						<xsl:choose>
							<xsl:when test="plan/fee='0.0'">--</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="plan/fee/text()"/>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_remark"/>：
				</td>
				<td class="wzb-form-control">
					<span>
						<xsl:choose>
							<xsl:when test="plan/remarks=''">--</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:value-of select="plan/remarks/text()"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
			</tr>
		</table>
		<!--
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td align="center" height="19" valign="middle">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_back"/>
						<xsl:with-param name="wb_gen_btn_href">javascript: history.back();</xsl:with-param>
					</xsl:call-template>
					<img border="0" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
		</table>
		-->
	</xsl:template>
</xsl:stylesheet>
