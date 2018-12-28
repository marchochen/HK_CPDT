<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>

	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="wb_gen_table_width" select="$wb_frame_table_width"/>
	<!-- ===================================================================== -->
	<xsl:template match="/">
		<html>
		<head>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				]]></SCRIPT>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="new_css"/>
		</head>
		<BODY marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="wb_utils_set_cookie('url_prev',self.location.href);">
			<FORM name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
			</FORM>
		</BODY>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">职业发展学习任务維護</xsl:with-param>
			<xsl:with-param name="lab_instruction_desc_1">职业发展学习任务的結構以列表結構顯示在頁面左側。</xsl:with-param>
			<xsl:with-param name="lab_instruction_desc_2">要添加职业发展学习任务，點擊上面的“添加”按鈕。</xsl:with-param>
			<xsl:with-param name="lab_instruction_desc_3">要查看或編輯职业发展学习任务的屬性，選擇相應的節點，詳細資訊會顯示出來。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">职业发展学习任务维护</xsl:with-param>
			<xsl:with-param name="lab_instruction_desc_1">职业发展学习任务的结构以列表结构显示在页面左侧。</xsl:with-param>
			<xsl:with-param name="lab_instruction_desc_2">要添加职业发展学习任务，点击上面的“添加”按钮。</xsl:with-param>
			<xsl:with-param name="lab_instruction_desc_3">要查看或编辑职业发展学习任务的属性，选择相应的节点，详细信息会显示出来。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">Profession maintenance</xsl:with-param>
			<xsl:with-param name="lab_instruction_desc_1">TheProfession structure is shown on the left panel as a list format.</xsl:with-param>
			<xsl:with-param name="lab_instruction_desc_2">To add a profession, click the "Add" button above. </xsl:with-param>
			<xsl:with-param name="lab_instruction_desc_3">To view or edit the properties of a profession, highlight the specific node and the details will be displayed.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_instruction_desc_1"/>
		<xsl:param name="lab_instruction_desc_2"/>
		<xsl:param name="lab_instruction_desc_3"/>
		
		<table>
			<tr>
			<td colspan="2" style="height:71px;">
				<xsl:call-template name="wb_ui_line"/>
			</td></tr>
			<tr>
				<td align="right" height="10">
				
				</td>
				<td height="10">
					<ul>
						<li>
							<xsl:copy-of select="$lab_instruction_desc_2"/>
						</li>
						<li>
							<xsl:copy-of select="$lab_instruction_desc_3"/>
						</li>
					</ul>
				</td>
			</tr>
		</table>
		
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="env"/>
	<xsl:template match="cur_usr"/>
</xsl:stylesheet>
