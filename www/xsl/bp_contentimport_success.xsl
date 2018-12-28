<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>	
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>	
	<xsl:variable name="wb_gen_table_width">585</xsl:variable>
	
	<xsl:variable name="success_entity" select="/Upload/que_import /success_entity/total"/>
	<xsl:variable name="unsuccess_entity" select="/Upload/que_import /unsuccess_entity/total"/>
	<xsl:output indent="yes"/>
	<xsl:strip-space elements="*"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="//que_import"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="que_import ">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" src="{$wb_js_path}wb_content.js"/>
			<script language="Javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" TYPE="text/javascript"><![CDATA[
			cont = new wbContent;
		]]></script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="new_css"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_title">匯入题目资源 - 結果</xsl:with-param>
			<xsl:with-param name="lab_success_entries">成功的輸入項</xsl:with-param>
			<xsl:with-param name="lab_failed_entries">未成功的輸入項</xsl:with-param>
			<xsl:with-param name="lab_view_log_file">查看記錄檔</xsl:with-param>
			<xsl:with-param name="lab_failed">匯入過程失敗</xsl:with-param>
			<xsl:with-param name="lab_reason">原因</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_title">导入题目资源 – 结果</xsl:with-param>
			<xsl:with-param name="lab_success_entries">成功的输入项</xsl:with-param>
			<xsl:with-param name="lab_failed_entries">未成功的输入项</xsl:with-param>
			<xsl:with-param name="lab_view_log_file">查看记录文件</xsl:with-param>
			<xsl:with-param name="lab_failed">导入过程失败</xsl:with-param>
			<xsl:with-param name="lab_reason">原因</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_title">Import question - result</xsl:with-param>
			<xsl:with-param name="lab_success_entries">Success entries</xsl:with-param>
			<xsl:with-param name="lab_failed_entries">Failed entries</xsl:with-param>
			<xsl:with-param name="lab_view_log_file">View log file</xsl:with-param>
			<xsl:with-param name="lab_failed">Import process failed to complete. </xsl:with-param>
			<xsl:with-param name="lab_reason">Reason</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_success_entries"/>
		<xsl:param name="lab_failed_entries"/>
		<xsl:param name="lab_view_log_file"/>
		<xsl:param name="lab_failed"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_reason"/>

        <xsl:value-of select="$lab_title"/>
		<!-- <xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text"><xsl:value-of select="$lab_title"/></xsl:with-param>
		</xsl:call-template> -->
		<xsl:call-template name="wb_ui_line"/>
			
		<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="3" class="Bg">
			<tr>
				<td colspan="3" height="10">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
			<xsl:if test="success_entity/total != 0 and success_entity/total != '' ">
				<tr>
					<td width="10%">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
					<td width="20%">
						<span class="Text">
							<xsl:value-of select="$lab_success_entries"/>
							<xsl:text>&#160;:&#160;</xsl:text>
							<xsl:value-of select="$success_entity"/>
						</span>
					</td>
					<td width="70%">
						<xsl:choose>
							<xsl:when test="/Upload/que_import /success_entity/log_file/uri != ''">
								<a class="Text" href="..{/Upload/que_import /success_entity/log_file/uri}" target="_blank"><xsl:value-of select="$lab_view_log_file"/></a>
							</xsl:when>
							<xsl:otherwise>
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
				<tr><td  COLSPAN="4" ><xsl:text>&#160;</xsl:text></td></tr>
				<tr><td  COLSPAN="4" ><xsl:call-template name="wb_ui_line"/></td></tr>
			</xsl:if>
			<xsl:if test="unsuccess_entity/total != 0 and unsuccess_entity/total != '' ">
				<tr>
					<td width="10%">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
					<td width="20%">
						<span class="Text">
							<font color="red">
								<xsl:value-of select="$lab_failed_entries"/>
								<xsl:text>&#160;:&#160;</xsl:text>
								<xsl:value-of select="$unsuccess_entity"/>
							</font>
						</span>
					</td>
					<td width="70%">
						<xsl:choose>
							<xsl:when test="/Upload/que_import /unsuccess_entity/log_file/uri != ''">
								<a class="Text" href="..{/Upload/que_import /unsuccess_entity/log_file/uri}" target="_blank"><font color="red"><xsl:value-of select="$lab_view_log_file"/></font></a>
							</xsl:when>
							<xsl:otherwise>
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="error/log_file/reason != ''">
				<tr class="wbRowRefBg">
					<td colspan="3">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
				</tr>
				<tr>
					<td width="10%">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
					<td colspan="2">
						<span class="Text">
							<font color="red">
								<xsl:value-of select="$lab_failed"/>
							</font>
						</span>
					</td>
				</tr>
				<tr>
					<td width="10%">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
					<td colspan="2">
						<span class="Text">
							<font color="red">
								<xsl:value-of select="$lab_reason"/>
								<xsl:text>&#160;:&#160;</xsl:text>
								<xsl:value-of select="/Upload/que_import /error/reason"/>
							</font>
						</span>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td colspan="3" height="10">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</table>

		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td align="center">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:cont.prep_page()</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
