<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<!-- ========================================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!--========================BODY=============================-->
	<xsl:template match="user">
		<head>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_{$wb_cur_lang}.js"></script>
			<!--alert样式  -->
		    <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> 
			
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			<!--alert样式  end -->
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				var app = new wbApplication
			]]></SCRIPT>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="new_css"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="wb_utils_gen_form_focus(document.frmXml)">
			<form name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_comment">添加備註</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_reset">重新設置</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_comment">添加备注</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_reset">重新设置</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_comment">Add remarks</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_reset">Reset</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_comment"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_reset"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text_class" >pop-up-title</xsl:with-param>
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_comment"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line">
		</xsl:call-template>
		<table class="Bg" width="{$wb_gen_table_width}" border="0" cellpadding="3" cellspacing="0">
			<tr>
				<td>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
			<tr>
				<td align="center" colspan="2">
					<textarea cols="50" rows="10" class="wzb-inputTextArea" name="content"/>
					<input type="hidden" name="cmd"/>
					<input type="hidden" name="app_id"/>
					<input type="hidden" name="upd_timestamp"/>
					<input type="hidden" name="url_success"/>
				</td>
			</tr>
			<tr>
				<td>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_line">
		</xsl:call-template>
		<table width="{$wb_gen_table_width}" border="0" cellpadding="3" cellspacing="0">
			<tr>
				<td align="center">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:app.ins_comment_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
					</xsl:call-template>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:window.close()</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="cur_usr"/>
</xsl:stylesheet>
