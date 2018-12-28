<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<!-- customized utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- =============================================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="errors">
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_error">錯誤</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">返回</xsl:with-param>
			<xsl:with-param name="lab_msg">操作失敗：</xsl:with-param>
			<xsl:with-param name="line_start">第</xsl:with-param>
			<xsl:with-param name="line_end">行</xsl:with-param>
			<xsl:with-param name="F001">不存在。</xsl:with-param>
			<xsl:with-param name="F002">不是一個有效的XML文檔。</xsl:with-param>
			<xsl:with-param name="AICC001">必須是一個整數。</xsl:with-param>
			<xsl:with-param name="AICC002">不是一個有效的時間(hh:mm:ss)。</xsl:with-param>
			<xsl:with-param name="SCORM001">必須是一個整數。</xsl:with-param>
			<xsl:with-param name="SCORM002">不是一個有效的時間(hh:mm:ss)。</xsl:with-param>
			<xsl:with-param name="NETG001">不是一個有效的數字。</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="errors">
			<xsl:with-param name="lab_status">狀況</xsl:with-param>
			<xsl:with-param name="lab_error">错误</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">返回</xsl:with-param>
			<xsl:with-param name="lab_msg">操作失败：</xsl:with-param>
			<xsl:with-param name="line_start">第</xsl:with-param>
			<xsl:with-param name="line_end">行</xsl:with-param>
			<xsl:with-param name="F001">不存在。</xsl:with-param>
			<xsl:with-param name="F002">不是一个有效的XML文档。</xsl:with-param>
			<xsl:with-param name="AICC001">必须是一个整数。</xsl:with-param>
			<xsl:with-param name="AICC002">不是一个有效的时间(hh:mm:ss)。</xsl:with-param>
			<xsl:with-param name="SCORM001">必须是一个整数。</xsl:with-param>
			<xsl:with-param name="SCORM002">不是一个有效的时间(hh:mm:ss)。</xsl:with-param>
			<xsl:with-param name="NETG001">不是一个有效的数字。</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="errors">
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_error">Error</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">Back</xsl:with-param>
			<xsl:with-param name="lab_msg">Action failed:</xsl:with-param>
			<xsl:with-param name="line_start">line</xsl:with-param>
			<xsl:with-param name="line_end"/>
			<xsl:with-param name="F001">does not exist.</xsl:with-param>
			<xsl:with-param name="F002">is not a valid xml document.</xsl:with-param>
			<xsl:with-param name="AICC001">must be an integer.</xsl:with-param>
			<xsl:with-param name="AICC002">is not a valid time(hh:mm:ss).</xsl:with-param>
			<xsl:with-param name="SCORM001">must be an integer.</xsl:with-param>
			<xsl:with-param name="SCORM002">is not a valid time(hh:mm:ss).</xsl:with-param>
			<xsl:with-param name="NETG001">is not a valid number.</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================================== -->
	<xsl:template match="errors">
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_error"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_msg"/>
		<xsl:param name="line_start"/>
		<xsl:param name="line_end"/>
		<xsl:param name="F001"/>
		<xsl:param name="F002"/>
		<xsl:param name="AICC001"/>
		<xsl:param name="AICC002"/>
		<xsl:param name="SCORM001"/>
		<xsl:param name="SCORM002"/>
		<xsl:param name="NETG001"/>
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
				<xsl:text> - </xsl:text>
				<xsl:value-of select="$lab_error"/>
			</title>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="new_css"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
		</head>
		<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0">
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td>
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="width">
									100%
								</xsl:with-param>
							<xsl:with-param name="text">
								<xsl:value-of select="$lab_error"/>
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line">
							<xsl:with-param name="width">
									100%
								</xsl:with-param>
						</xsl:call-template>
						<table cellpadding="0" cellspacing="0" width="100%" border="0">
							<tr>
								<td align="center" width="60px" height="45px">
									<img src="{$wb_img_path}ico_warning.gif" width="30" height="24" border="0"/>
								</td>
								<td>
									<xsl:value-of select="$lab_msg"/>
								</td>
							</tr>
							<xsl:for-each select="error">
								<tr>
									<td align="right" style="padding-right: 10px;">
										<xsl:value-of select="position()"/>:
									</td>
									<td>
										<span class="TitleText">
											<xsl:choose>
												<xsl:when test="@code = 'F001'">
													<xsl:value-of select="@file"/>
													<xsl:text>&#160;&#160;</xsl:text>
													<xsl:value-of select="$F001"/>
												</xsl:when>
												<xsl:when test="@code = 'F002'">
													<xsl:value-of select="@file"/>
													<xsl:text>&#160;&#160;</xsl:text>
													<xsl:value-of select="$F002"/>
												</xsl:when>
												<xsl:when test="@code = 'AICC001'">
													<xsl:value-of select="@file"/>
													<xsl:text>&#160;&#160;</xsl:text>
													<xsl:value-of select="$line_start"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="$line_end"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@field"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="$AICC001"/>
												</xsl:when>
												<xsl:when test="@code = 'AICC002'">
													<xsl:value-of select="@file"/>
													<xsl:text>&#160;&#160;</xsl:text>
													<xsl:value-of select="$line_start"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="$line_end"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@field"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="$AICC002"/>
												</xsl:when>
												<xsl:when test="@code = 'SCORM001'">
													<xsl:value-of select="@file"/>
													<xsl:text>&#160;&#160;</xsl:text>
													<xsl:value-of select="$line_start"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="$line_end"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@field"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="$SCORM001"/>
												</xsl:when>
												<xsl:when test="@code = 'SCORM002'">
													<xsl:value-of select="@file"/>
													<xsl:text>&#160;&#160;</xsl:text>
													<xsl:value-of select="$line_start"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="$line_end"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@field"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="$SCORM002"/>
												</xsl:when>
												<xsl:when test="@code = 'NETG001'">
													<xsl:value-of select="@file"/>
													<xsl:text>&#160;&#160;</xsl:text>
													<xsl:value-of select="$line_start"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="$line_end"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@field"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="$NETG001"/>
												</xsl:when>
											</xsl:choose>
										</span>
									</td>
								</tr>
							</xsl:for-each>
						</table>
						<xsl:call-template name="wb_ui_line">
							<xsl:with-param name="width">100%</xsl:with-param>
						</xsl:call-template>
						<table cellpadding="3" cellspacing="0" border="0" width="100%">
							<tr>
								<td align="center">
									<xsl:call-template name="wb_gen_form_button">
										<xsl:with-param name="wb_gen_btn_name">
											<xsl:value-of select="$lab_g_form_btn_ok"/>
										</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">javascript:history.back();</xsl:with-param>
									</xsl:call-template>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</body>
	</xsl:template>
</xsl:stylesheet>
