<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<xsl:variable name="text_class">wzb-title-2 wzb-before</xsl:variable>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="module"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="module">
		<head>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> 
			
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_reference.js" type="text/javascript"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js" language="JavaScript"/>
			<!--alert样式  -->
			<!-- <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>  -->
			
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			<!--alert样式  end -->
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			var ref = new wbReference();
		]]></SCRIPT>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="new_css"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="wb_utils_gen_form_focus(document.frmXml)">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_reference">新增參考</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_url">URL</xsl:with-param>
			<xsl:with-param name="lab_description">內容簡介</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_reference">新增参考</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_url">URL</xsl:with-param>
			<xsl:with-param name="lab_description">内容简介</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_reference">Add reference</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_url">URL</xsl:with-param>
			<xsl:with-param name="lab_description">Description</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_reference"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_url"/>
		<xsl:param name="lab_description"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<form name="frmXml" onSubmit="return status()">
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text" select="$lab_reference"/>
				<xsl:with-param name="text_class" select="$text_class"/>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_line"/>
			<table>
				<tr>
					<td class="wzb-form-label">
						<xsl:value-of select="$lab_title"/>:
					</td>
					<td class="wzb-form-control">
						<input type="text" name="ref_title" size="15" style="width:300px;" value="" class="wzb-inputText"/>
					</td>
				</tr>
				<tr>
					<td class="wzb-form-label">
						<xsl:value-of select="$lab_url"/>:
					</td>
					<td class="wzb-form-control">
						<input type="text" name="ref_url" size="15" style="width:300px;" value="http://" class="wzb-inputText"/>
					</td>
				</tr>
				<tr>
					<td class="wzb-form-label" valign="top">
						<xsl:value-of select="$lab_description"/>:
					</td>
					<td class="wzb-form-control">
						<textarea rows="6" wrap="VIRTUAL" style="width:300px;" cols="35" name="ref_description" class="wzb-inputTextArea"/>
					</td>
				</tr>
			</table>
			<div class="wzb-bar">
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:ref.ins_ref(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:history.back(-1)</xsl:with-param>
				</xsl:call-template>
			</div>
			<input type="hidden" name="cmd" value=""/>
			<input type="hidden" name="module" value=""/>
			<input type="hidden" name="url_success" value=""/>
			<input type="hidden" name="url_failure" value=""/>
			<input type="hidden" name="mod_id" value="{/module/@id}"/>
		</form>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="env"/>
	<xsl:template match="cur_usr"/>
	<xsl:template match="cur_time"/>
</xsl:stylesheet>
