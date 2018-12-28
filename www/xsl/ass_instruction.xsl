<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:template match="/">
		<html>
			<head>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<xsl:call-template name="new_css"/>
				<link rel="stylesheet" href="../static/css/three.css"/>
				
				<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
				<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			</head>
			<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
				<form name="frmXml" enctype="multipart/form-data">
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_inst">內容</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_inst">内容</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_inst">Instruction</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_inst"/>
		<xsl:param name="lab_g_form_btn_close"/>
		
		<div class="work_input_desc wzb-title-11 wzb-banner-bg14" align="left" style="padding: 0 0 0 24px; background:#27c5b8;">
			<xsl:value-of select="/module/header/title/text()"/>
		</div>
		
		
		
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_inst"/>
			</xsl:with-param>
			<xsl:with-param name="new_template">true</xsl:with-param>
		</xsl:call-template>
		
		<div class="content_info report_title " style="padding:10px 0px;">
			<p class="work_font">
				<xsl:call-template name="unescape_html_linefeed">
					<xsl:with-param name="my_right_value"><xsl:value-of disable-output-escaping="yes" select="module/header/instruction"/></xsl:with-param>
				</xsl:call-template>
			</p>
		</div>
		
	</xsl:template>
</xsl:stylesheet>
