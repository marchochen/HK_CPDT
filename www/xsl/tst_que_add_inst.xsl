<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="wb_gen_table_width" select="$wb_frame_table_width"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="user"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="user">
		<head>
			<title>
				<xsl:value-of select="wb_wizbank"/>
			</title>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="new_css"/>
		</head>
		<xsl:call-template name="wb_init_lab"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_inst">在側顯示的是文件夾中當前可用的題目列表。按連結可以查看題目的詳細訊息，要為測驗添加題目，可以使用題目前面的方格標記您需要添加的題目，然後按<b>加到試卷</b>將選取了的題目加入到當前的測驗。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_inst">左侧显示的是文件夹中当前可用的题目列表。点击链接可以查看题目的详细信息，要为测验添加题目，可以使用题目前面的复选框标记您要添加的题目，然后点击<b>加入测验</b>将选中的题目加入到当前测验。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_inst">Listed on the left are the questions available in this folder.  You can view the question details by clicking the links. To add questions to the test, use the checkboxes to mark your selection and click <b>Add to test</b>.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_inst"/>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="wb_ui_line"/>		
			<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="10" class="Bg">
				<tr>
					<td height="10">
						<IMG border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
					</td>
				</tr>
				<tr>
					<td >
						<span class="wzb-ui-desc wzb-ui-module-text" style="font-size:13px"><xsl:copy-of select="$lab_inst"/></span>
					</td>
				</tr>				
				<tr>
					<td height="10">
						<IMG border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
					</td>
				</tr>
			</table>
			<xsl:call-template name="wb_ui_line"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
