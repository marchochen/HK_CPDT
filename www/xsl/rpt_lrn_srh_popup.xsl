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
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<!-- others -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:strip-space elements="*"/>
	<xsl:variable name="role_list" select="/report/report_body/meta/role_list"/>
	<xsl:variable name="rpt_type" select="/report/report_body/template/@type"/>
	<xsl:variable name="delimiter_field">:_:_:</xsl:variable>
	<xsl:variable name="delimiter_value">~</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/report">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_mgt_rpt.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
				var mgt_rpt = new wbManagementReport;
				
				function status() {
					return false;
				}
			]]></script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
		</head>
		<BODY leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="wb_utils_gen_form_focus(document.frmXml)">
			<form name="frmXml" onsubmit="return status()">
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="rte_id" value="{report_body/template/@id}"/>
				<input type="hidden" name="rpt_type" value="{report_body/template/@type}"/>
				<input type="hidden" name="rpt_type_lst" value="{report_body/template/@type}"/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<xsl:variable name="spec_name">
					<xsl:apply-templates select="/report/report_body/spec/data_list" mode="spec_name"/>
				</xsl:variable>
				<xsl:variable name="spec_value">
					<xsl:apply-templates select="/report/report_body/spec/data_list" mode="spec_value"/>
				</xsl:variable>
				<input type="hidden" name="spec_name" value="{$spec_name}"/>
				<input type="hidden" name="spec_value" value="{$spec_value}"/>
				<input type="hidden" name="download" value="0"/>
				<input type="hidden" name="page_size" value=""/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</BODY>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_header">保存為報告模板</xsl:with-param>
			<xsl:with-param name="lab_instruction">請輸入模板名稱，然後點擊<b>確定</b>保存為模板。</xsl:with-param>
			<xsl:with-param name="lab_rpt_name">模板名稱</xsl:with-param>
			<xsl:with-param name="lab_rpt_name_length">(不超過80字元)</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<!-- header text -->
			<xsl:with-param name="lab_header">保存为报告模板</xsl:with-param>
			<xsl:with-param name="lab_instruction">请输入模板名称，然后点击<b>确定</b>保存为模板。</xsl:with-param>
			<xsl:with-param name="lab_rpt_name_length">（长度不超过80字符）</xsl:with-param>
			<xsl:with-param name="lab_rpt_name">模板名称</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_header">Save as report template</xsl:with-param>
			<xsl:with-param name="lab_instruction">Please specify the template name and click <b>OK</b> to save.</xsl:with-param>
			<xsl:with-param name="lab_rpt_name_length">(Not more than 80 characters.)</xsl:with-param>
			<xsl:with-param name="lab_rpt_name">Template name</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_header"/>
		<xsl:param name="lab_instruction"/>
		<xsl:param name="lab_rpt_name"/>
		<xsl:param name="lab_rpt_name_length"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_TRAINING_REPORT_MGT</xsl:with-param>
			<xsl:with-param name="parent_code" >FTN_AMD_TRAINING_REPORT_MGT</xsl:with-param>
			<xsl:with-param name="page_title" select="$lab_header" ></xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_header"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_instruction"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
			<tr>
				<td width="20%">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td width="80%">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
			<tr>
				<td align="right" valign="top" class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_rpt_name"/>：</span>
				</td>
				<td class="wzb-form-control">
					<span class="Text">
						<input type="text" name="rsp_title" maxlength="200" style="width:300px;" class="wzb-inputText"/>
						<br/>
						<xsl:value-of select="$lab_rpt_name_length"/>
					</span>
				</td>
			</tr>
			<tr>
				<td width="20%">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td width="80%">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_line"/>
		<xsl:call-template name="wb_ui_space"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td align="center">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_g_form_btn_ok"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.ins_rpt_exec_popup(document.frmXml,"<xsl:value-of select="$wb_lang"/>")</xsl:with-param>
					</xsl:call-template>
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_g_form_btn_cancel"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:history.back();</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data_list" mode="spec_name">
		<!-- for each distinct element, generate a delimited name list -->
		<xsl:for-each select="data[not(preceding-sibling::*/@name = ./@name)]">
			<xsl:value-of select="@name"/>
			<xsl:value-of select="$delimiter_field"/>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data_list" mode="spec_value">
		<!-- for each distinct element, generate a delimited value list -->
		<xsl:for-each select="data[not(preceding-sibling::*/@name = ./@name)]">
			<xsl:variable name="cur_data_name" select="@name"/>
			<xsl:for-each select="../data[@name = $cur_data_name]">
				<xsl:value-of select="@value"/>
				<xsl:if test="not(position() = last())">
					<xsl:value-of select="$delimiter_value"/>
				</xsl:if>
			</xsl:for-each>
			<xsl:value-of select="$delimiter_field"/>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
