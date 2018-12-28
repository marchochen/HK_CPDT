<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:output indent="yes"/>
	<!-- ================================================================ -->
	<xsl:template match="/">
		<xsl:apply-templates select="uploadpoint"/>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="uploadpoint">
		<html>
			<xsl:call-template name="draw_header"/>
			<xsl:call-template name="draw_body"/>
		</html>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_header">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_credit.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript"><![CDATA[
			var credit = new wbCredit;
					]]></script>
		</head>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_body">
		<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0">
			<form name="frmAction" onsubmit="return status()">
				<xsl:call-template name="wb_init_lab"/>
				<input type="hidden" name="search_type"/>
				<input type="hidden" name="ubd_usr_ent_id"/>
				<input type="hidden" name="deduction_ind"/>
				<input type="hidden" name="change_point"/>
				<input type="hidden" name="bpt_id"/>
				<input type="hidden" name="module"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
			</form>
		</body>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_rd_admin_main">積分管理</xsl:with-param>
			<xsl:with-param name="lab_set_point">設置學員積分</xsl:with-param>
			<xsl:with-param name="lab_usr_name">積分操作類型</xsl:with-param>
			<xsl:with-param name="lab_usr_id">用戶ID</xsl:with-param>
			<xsl:with-param name="lab_point">積分(扣分)點名稱</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_score">分數</xsl:with-param>
			<xsl:with-param name="lab_import_result">匯入结果</xsl:with-param>
			<xsl:with-param name="lab_secceed_count">匯入成功條數：</xsl:with-param>
			<xsl:with-param name="lab_fail_count">匯入成功條數：</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_fail_desc">以下列出所有失敗記錄：</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_rd_admin_main">积分管理</xsl:with-param>
			<xsl:with-param name="lab_set_point">设置学员积分</xsl:with-param>
			<xsl:with-param name="lab_usr_name">积分操作类型</xsl:with-param>
			<xsl:with-param name="lab_usr_id">用户ID</xsl:with-param>
			<xsl:with-param name="lab_point">积分(扣分)点名称</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_score">分数</xsl:with-param>
			<xsl:with-param name="lab_import_result">导入结果</xsl:with-param>
			<xsl:with-param name="lab_secceed_count">导入成功条数：</xsl:with-param>
			<xsl:with-param name="lab_fail_count">导入失败条数：</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_fail_desc">以下列出所有失败记录：</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_rd_admin_main">Redemption management</xsl:with-param>
			<xsl:with-param name="lab_set_point">Set learner activate point calculation</xsl:with-param>
			<xsl:with-param name="lab_usr_name">Credit operational type</xsl:with-param>
			<xsl:with-param name="lab_usr_id">User ID</xsl:with-param>
			<xsl:with-param name="lab_point">Credit accumulation(deduction) name</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_ok">Ok</xsl:with-param>
			<xsl:with-param name="lab_score">Score</xsl:with-param>
			<xsl:with-param name="lab_import_result">Import result</xsl:with-param>
			<xsl:with-param name="lab_secceed_count">Successful count:</xsl:with-param>
			<xsl:with-param name="lab_fail_count">Fail count:</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_fail_desc">Below lists all failed records:</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="content">
		<xsl:param name="lab_rd_admin_main"/>
		<xsl:param name="lab_set_point"/>
		<xsl:param name="lab_usr_name"/>
		<xsl:param name="lab_usr_id"/>
		<xsl:param name="lab_point"/>
		<xsl:param name="lab_g_txt_btn_ok"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_import_result"/>
		<xsl:param name="lab_secceed_count"/>
		<xsl:param name="lab_fail_count"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_fail_desc"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_CREDIT_SETTING_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_set_point"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<a href="javascript:wb_utils_nav_go('JF_MGT');" class="NavLink">
					<xsl:value-of select="$lab_rd_admin_main"/>
				</a>
				&#160;&gt;&#160;
				<a href="javascript:rd.set_lrn_point();" class="NavLink">
					<xsl:value-of select="$lab_set_point"/>
				</a>
				<span class="NavLink">&#160;&gt;&#160;
					<xsl:value-of select="$lab_import_result"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<tr>
				<td>
					<table>
						<tr>
							<td width="2%">
							</td>
							<td align="left">
								<xsl:value-of select="$lab_secceed_count"/>
								<xsl:value-of select="succeed_count"/>
							</td>
						</tr>
						<tr>
							<td>
							</td>
							<td align="left">
								<xsl:value-of select="$lab_fail_count"/>
								<xsl:value-of select="fail_list/@count"/>
							</td>
						</tr>
						<xsl:if test="fail_list/@count &gt; 0">
							<tr>
								<td>
								</td>
								<td align="left">
									<xsl:value-of select="$lab_fail_desc"/>
								</td>
							</tr>
						</xsl:if>
					</table>
					<xsl:if test="fail_list/@count &gt; 0">
						<table class="table wzb-ui-table">
							<tr class="wzb-ui-table-head">
								<td>
									<xsl:value-of select="$lab_usr_id"/>
								</td>
								<td>
									<xsl:value-of select="$lab_usr_name"/>
								</td>
								<td>
									<xsl:value-of select="$lab_point"/>
								</td>
								<td align="right">
									<xsl:value-of select="$lab_score"/>
								</td>
							</tr>
							<xsl:apply-templates select="fail_list/record">
								<xsl:with-param name="lab_na" select="$lab_na"/>
							</xsl:apply-templates>
						</table>
					</xsl:if>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:credit.import_prep()</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="record">
		<xsl:param name="lab_na"/>
		<tr>
			<td>
				<xsl:choose>
					<xsl:when test="usr_id">
						<xsl:value-of select="usr_id"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_na"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td>
				<xsl:choose>
					<xsl:when test="usr_name">
						<xsl:value-of select="usr_name"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_na"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td>
				<xsl:choose>
					<xsl:when test="bonus_name">
						<xsl:value-of select="bonus_name"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_na"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="right">
				<xsl:choose>
					<xsl:when test="score">
						<xsl:value-of select="score"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_na"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="bpt">
		<option value="{@id}">
			<xsl:value-of select="title"/>
		</option>
	</xsl:template>
	<!-- ================================================================ -->
</xsl:stylesheet>
