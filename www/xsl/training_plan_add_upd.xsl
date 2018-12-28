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
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="root_ent_id" select="/tptrainingplan/meta/cur_usr/@root_ent_id"/>
	<xsl:variable name="cur_time" select="tptrainingplan/meta/current_timestamp/text()"/>
	<xsl:variable name="tpn_id" select="tptrainingplan/plan/@id"/>
	<xsl:variable name="tpn_status" select="tptrainingplan/plan/status/text()"/>
	<xsl:variable name="tpn_type" select="tptrainingplan/plan/@type"/>
	<xsl:variable name="entrance" select="/tptrainingplan/meta/entrance"/>
	<xsl:variable name="def_tcr_id">
		<xsl:choose>
			<xsl:when test="/tptrainingplan/plan/training_center/@id !=''"><xsl:value-of select="tptrainingplan/plan/training_center/@id"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="/tptrainingplan/default_tc/@tcr_id"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="upd_timestamp" select="tptrainingplan/plan/upd_timestamp/text()"/>
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
			<script language="Javascript" TYPE="text/javascript"><![CDATA[
				goldenman = new wbGoldenMan;
				usr = new wbUserGroup;
				tp = new wbTrainingPlan;
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
				<input type="hidden" name="tpn_status" value="{$tpn_status}"/>
				<input type="hidden" name="tpn_id" value="{$tpn_id}"/>
				<input type="hidden" name="tcr_id"/>
				<input type="hidden" name="tpn_upd_timestamp" value="{$upd_timestamp}"/>
				<input type="hidden" name="tpn_type" value="{$tpn_type}"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_desc_add">添加培訓計劃</xsl:with-param>
			<xsl:with-param name="lab_desc_edit">編輯培訓計劃</xsl:with-param>
			<xsl:with-param name="lab_training_plan">培訓計劃</xsl:with-param>
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
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_start_date">開始時間</xsl:with-param>
			<xsl:with-param name="lab_end_date">結束時間</xsl:with-param>		
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_desc_add">添加培训计划</xsl:with-param>
			<xsl:with-param name="lab_desc_edit">编辑培训计划</xsl:with-param>
			<xsl:with-param name="lab_training_plan">培训计划</xsl:with-param>
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
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_start_date">开始时间</xsl:with-param>
			<xsl:with-param name="lab_end_date">结束时间</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_desc_add">Add additional training plan</xsl:with-param>
			<xsl:with-param name="lab_desc_edit">Edit additional training plan</xsl:with-param>
			<xsl:with-param name="lab_training_plan">Training plan</xsl:with-param>
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
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_start_date">Available after</xsl:with-param>
			<xsl:with-param name="lab_end_date">Available until</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_desc_add"/>
		<xsl:param name="lab_desc_edit"/>
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
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_start_date"/>
		<xsl:param name="lab_end_date"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_MAKEUP_PLAN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_MAKEUP_PLAN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$tpn_id !='0' and $tpn_id !=''">
						<xsl:value-of select="$lab_desc_edit"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_desc_add"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<!-- <xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_training_plan"/>
			</xsl:with-param>
		</xsl:call-template> -->
		<xsl:variable name="tpn_tcr_id" select="plan/training_center/@id"/>
		<table>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_plan_date"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:variable name="plan_date">
						<xsl:choose>
							<xsl:when test="plan/plan_date/text() !=''">
								<xsl:value-of select="plan/plan_date/text()"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$cur_time"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<input type="text" name="tpn_date_yy" size="4" maxlength="4" class="wzb-inputText">
						<xsl:attribute name="value"><xsl:value-of select="substring($plan_date, 1,4)"/></xsl:attribute>
					</input><xsl:text>-</xsl:text>
					<input type="text" name="tpn_date_mm" size="2" maxlength="2" class="wzb-inputText">
						<xsl:attribute name="value"><xsl:value-of select="substring($plan_date, 6,2)"/></xsl:attribute>
					</input>
					<xsl:text>&#160;</xsl:text><xsl:value-of select="$lab_yy_mm"/>
					<input type="hidden" name="tpn_date_dd" value="1"/>
					<input type="hidden" name="tpn_date" value="{plan/plan_date}"/>
					<input type="hidden" name="tpn_date_label" value="{$lab_plan_date}"/>
				</td>
			</tr>
			<xsl:if test="plan/code/text() !=''">
				<tr>
					<td class="wzb-form-label">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_code"/>：
					</td>
					<td class="wzb-form-control">
						<!--
						<input type="text" name="tpn_code" maxlength="255" size="20" class="wzb-inputText">
							<xsl:if test="plan/code/text() !=''">
								<xsl:attribute name="value"><xsl:value-of select="plan/code/text()"/></xsl:attribute>
							</xsl:if>
						</input>
						-->
						<xsl:value-of select="plan/code/text()"/>
						<input type="hidden" name="tpn_code_label" value="{$lab_code}"/>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_name"/>：
				</td>
				<td class="wzb-form-control">
					<input type="text" name="tpn_name" maxlength="200" size="20" class="wzb-inputText">
						<xsl:if test="plan/name/text() !=''">
							<xsl:attribute name="value"><xsl:value-of select="plan/name/text()"/></xsl:attribute>
						</xsl:if>
					</input>
					<input type="hidden" name="tpn_name_label" value="{$lab_name}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_type"/>：
				</td>
				<td class="wzb-form-control">
					<input type="text" name="tpn_cos_type" maxlength="255" size="20" class="wzb-inputText">
						<xsl:if test="plan/name/text() !=''">
							<xsl:attribute name="value"><xsl:value-of select="plan/cos_type/text()"/></xsl:attribute>
						</xsl:if>
					</input>
					<input type="hidden" name="tpn_cos_type_label" value="{$lab_type}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_tcr_title"/>：
				</td>
				<td class="wzb-form-control">
					<select name="tpn_tcr_id" class="wzb-form-select">
						<xsl:for-each select="my_charge_tc_lst/tc">
							<option value="{@tcr_id}">
								<xsl:if test="$def_tcr_id=@tcr_id">
									<xsl:attribute name="selected">selected</xsl:attribute>
								</xsl:if>
								<xsl:value-of select="text()"/>
							</option>
						</xsl:for-each>
						<xsl:value-of select="$lab_year"/>
					</select>
					<input type="hidden" name="tcr_title_label" value="{$lab_tcr_title}"/>
					<input type="hidden" name="tpn_usg_ent_id"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_catalog"/>：
				</td>
				<td class="wzb-form-control">
					<textarea rows="4" cols="60" style="width: 300; height: 60" name="tpn_tnd_title" class="wzb-inputTextArea">
						<xsl:if test="plan/tnd_title/text() != ''">
							<xsl:value-of select="plan/tnd_title/text()"/>
						</xsl:if>
					</textarea>
					<input type="hidden" name="tpn_tnd_title_label" value="{$lab_catalog}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_intr"/>：
				</td>
				<td class="wzb-form-control">
					<textarea rows="4" cols="60" style="width: 300; height: 60" name="tpn_introduction" class="wzb-inputTextArea">
						<xsl:if test="plan/introduction/text() != ''">
							<xsl:value-of select="plan/introduction/text()"/>
						</xsl:if>
					</textarea>
					<input type="hidden" name="tpn_introduction_label" value="{$lab_intr}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_aim"/>：
				</td>
				<td class="wzb-form-control">
					<textarea rows="4" cols="60" style="width: 300; height: 60" name="tpn_aim" class="wzb-inputTextArea">
						<xsl:if test="plan/aim/text() != ''">
							<xsl:value-of select="plan/aim/text()"/>
						</xsl:if>
					</textarea>
					<input type="hidden" name="tpn_aim_label" value="{$lab_aim}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_tatget"/>：
				</td>
				<td class="wzb-form-control">
					<textarea rows="4" cols="60" style="width: 300; height: 60" name="tpn_target" class="wzb-inputTextArea">
						<xsl:if test="plan/target/text() != ''">
							<xsl:value-of select="plan/target/text()"/>
						</xsl:if>
					</textarea>
					<input type="hidden" name="tpn_target_label" value="{$lab_tatget}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_rector"/>：
				</td>
				<td class="wzb-form-control">
					<textarea rows="4" cols="60" style="width: 300; height: 60" name="tpn_responser" maxlength="255" class="wzb-inputTextArea">
						<xsl:if test="plan/responser/text() != ''">
							<xsl:value-of select="plan/responser/text()"/>
						</xsl:if>
					</textarea>
					<input type="hidden" name="tpn_responser_label" value="{$lab_rector}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_days"/>：
				</td>
				<td class="wzb-form-control">
					<textarea rows="4" cols="60" style="width: 300; height: 60" name="tpn_duration" class="wzb-inputTextArea">
						<xsl:if test="plan/duration/text() !=''">
							<xsl:value-of select="plan/duration/text()"/>
						</xsl:if>
					</textarea>
					<input type="hidden" name="tpn_duration_label" value="{$lab_days}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_ftf_start_time"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">tpn_ftf_start_datetime</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">tpn_ftf_start_datetime</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
						<xsl:with-param name="timestamp">
							<xsl:value-of select="plan/ftf_start_date/text()"/>
						</xsl:with-param>
					</xsl:call-template>
					<input type="hidden" name="tpn_ftf_start_date" value="{plan/ftf_start_date/text()}"/>
					<input type="hidden" name="tpn_ftf_start_date_label" value="{$lab_ftf_start_time}"/>
					<input type="hidden" name="lab_start_date" value="{$lab_start_date}"/>
					<input type="hidden" name="lab_end_date" value="{$lab_end_date}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_ftf_end_time"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">tpn_ftf_end_datetime</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">tpn_ftf_end_datetime</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
						<xsl:with-param name="timestamp">
							<xsl:value-of select="plan/ftf_end_date/text()"/>
						</xsl:with-param>
					</xsl:call-template>
					<input type="hidden" name="tpn_ftf_end_date" value="{plan/ftf_end_date/text()}"/>
					<input type="hidden" name="tpn_ftf_end_date_label" value="{$lab_ftf_end_time}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_wb_start_time"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">tpn_wb_start_datetime</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">tpn_wb_start_datetime</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
						<xsl:with-param name="timestamp">
							<xsl:value-of select="plan/wb_start_date/text()"/>
						</xsl:with-param>
					</xsl:call-template>
					<input type="hidden" name="tpn_wb_start_date" value="{plan/wb_start_date/text()}"/>
					<input type="hidden" name="tpn_wb_start_date_label" value="{$lab_wb_start_time}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_wb_end_time"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">tpn_wb_end_datetime</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">tpn_wb_end_datetime</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
						<xsl:with-param name="timestamp">
							<xsl:value-of select="plan/wb_end_date/text()"/>
						</xsl:with-param>
					</xsl:call-template>
					<input type="hidden" name="tpn_wb_end_date" value="{plan/wb_end_date/text()}"/>
					<input type="hidden" name="tpn_wb_end_date_label" value="{$lab_wb_end_time}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_lrn_count"/>：
				</td>
				<td class="wzb-form-control">
					<input type="text" name="tpn_lrn_count" maxlength="3" size="3" class="wzb-inputText">
						<xsl:if test="plan/lrn_count/text() !=''and plan/lrn_count/text() !='0'">
							<xsl:attribute name="value"><xsl:value-of select="plan/lrn_count/text()"/></xsl:attribute>
						</xsl:if>
					</input>
					<input type="hidden" name="tpn_lrn_count_label" value="{$lab_lrn_count}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_fee"/>：
				</td>
				<td class="wzb-form-control">
					<input type="text" name="tpn_fee" maxlength="10" size="20" class="wzb-inputText">
						<xsl:if test="plan/fee/text() !=''and plan/fee/text() !='0.0'">
							<xsl:attribute name="value"><xsl:value-of select="plan/fee/text()"/></xsl:attribute>
						</xsl:if>
					</input>
					<input type="hidden" name="tpn_fee_label" value="{$lab_fee}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_remark"/>：
				</td>
				<td class="wzb-form-control">
					<textarea rows="4" cols="60" style="width: 300; height: 100" name="tpn_remark" class="wzb-inputTextArea">
						<xsl:if test="plan/remarks/text() !=''">
							<xsl:value-of select="plan/remarks/text()"/>
						</xsl:if>
					</textarea>
					<input type="hidden" name="tpn_remark_label" value="{$lab_remark}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
				</td>
				<td class="wzb-ui-module-text">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">
					<xsl:choose>
						<xsl:when test="$tpn_id !='0' and $tpn_id !=''">javascript: tp.upd_makeup(document.frmXml,'<xsl:value-of select="$tpn_tcr_id"/>', '<xsl:value-of select="$tpn_id"/>', '<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$entrance"/>')</xsl:when>
						<xsl:otherwise>javascript: tp.add_makeup(document.frmXml, '', '<xsl:value-of select="$wb_lang"/>')</xsl:otherwise>
					</xsl:choose>
				</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<xsl:template match="data">
		<option value="{@value}">
			<xsl:value-of select="@display"/>
		</option>
	</xsl:template>
</xsl:stylesheet>
