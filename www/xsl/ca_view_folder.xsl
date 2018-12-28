<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<!-- =============================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================== -->	
	<xsl:template name="main">
		<head>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<!-- <script language="javascript" type="text/javascript">
				function folder_edit_mode(){
					url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','ca_edit_folder.xsl')
					document.location.href = url;
				}
			</script> -->
			<xsl:call-template name="new_css"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================== -->	
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_folder">模塊夾</xsl:with-param>
			<!-- <xsl:with-param name="lab_edit">編輯</xsl:with-param> -->
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_folder">模块夹</xsl:with-param>
			<!-- <xsl:with-param name="lab_edit">编辑</xsl:with-param> -->
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_folder">Folder</xsl:with-param>
			<!-- <xsl:with-param name="lab_edit">Edit</xsl:with-param> -->
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_folder"/>
		<!-- <xsl:param name="lab_edit"/> -->
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<script><![CDATA[							
						document.write(wb_utils_get_cookie('folder_title'));
					]]></script>
			</xsl:with-param>
		</xsl:call-template>
		<!-- <table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td align="right">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_edit"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:folder_edit_mode();</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table> -->
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_title"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<script><![CDATA[
						str = wb_utils_get_cookie('folder_title')
						document.write(str)
					]]></script>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_type"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="$lab_folder"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_line"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="env"/>
	<xsl:template match="cur_usr"/>
	<xsl:template match="cur_time"/>
</xsl:stylesheet>
