<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="module">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<xsl:call-template name="new_css"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frm_obj" method="post">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="sso_link_query"/>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="header">
			<xsl:with-param name="lab_instruction">指導</xsl:with-param>
			<xsl:with-param name="lab_instruction_1">您能通過使用左邊的面板完成下列操作：</xsl:with-param>
			<xsl:with-param name="lab_instruction_2">要添加一個新的題目，請點擊“添加”按鈕。</xsl:with-param>
			<xsl:with-param name="lab_instruction_3">要查看或者修改一個題目，請點擊某個問題的鏈結。</xsl:with-param>
			<xsl:with-param name="lab_instruction_4">要以某種形式改變題目排序，請點擊“重新排序”按鈕。</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="header">
			<xsl:with-param name="lab_instruction">指导</xsl:with-param>
			<xsl:with-param name="lab_instruction_1">您能通过使用左边的面板完成下列操作：</xsl:with-param>
			<xsl:with-param name="lab_instruction_2">要添加一个新的题目，请点击“添加”按钮。</xsl:with-param>
			<xsl:with-param name="lab_instruction_3">要查看或者修改一个题目，请点击某个问题的链接。</xsl:with-param>
			<xsl:with-param name="lab_instruction_4">要以某种形式改变题目排序，请点击“重新排序”按钮。</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="header">
			<xsl:with-param name="lab_instruction">Instruction</xsl:with-param>
			<xsl:with-param name="lab_instruction_1">You can perform the following actions using the left panel:</xsl:with-param>
			<xsl:with-param name="lab_instruction_2">To add a new question, click <b>Add</b>.</xsl:with-param>
			<xsl:with-param name="lab_instruction_3">To view or modify a question, click the question link.</xsl:with-param>
			<xsl:with-param name="lab_instruction_4">To change the question order in the form, click <b>Reorder</b>.</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="header">
		
		<xsl:param name="lab_instruction"/>
		<xsl:param name="lab_instruction_1"/>
		<xsl:param name="lab_instruction_2"/>
		<xsl:param name="lab_instruction_3"/>
		<xsl:param name="lab_instruction_4"/>
		<!-- <xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_instruction"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/> -->
		<table>
			<tr>
				<td valign="top" align="left">
					<table>
						<tr class="wzb-ui-module-text">
							<td height="10" style="padding:5px 0 0 10px">
								<xsl:value-of select="$lab_instruction_1"/>
								<ul>
									<li>
										<xsl:copy-of select="$lab_instruction_2"/>
									</li>
									<li>
										<xsl:copy-of select="$lab_instruction_3"/>
									</li>
									<li>
										<xsl:copy-of select="$lab_instruction_4"/>
									</li>
								</ul>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="eval_target_lst"/>
</xsl:stylesheet>
