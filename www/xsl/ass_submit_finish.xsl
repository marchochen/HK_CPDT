<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>	
	<!-- ========================================================== -->
	<xsl:output indent="yes"/>
	<xsl:variable name="mod_id" select="/module/@id"/>
	<xsl:variable name="usr_id" select="/module/cur_usr/@id"/>
	<xsl:variable name="max_upload" select="/module/header/@max_upload"/>
	<xsl:variable name="no_of_files" select="count(/module/body/uploadPathTemp/file)"/>
	<!-- ========================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="module"/>
		</html>
	</xsl:template>
	<!-- ========================================================== -->
	<xsl:template match="module">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
<!-- 			<xsl:call-template name="wb_css"> -->
<!-- 				<xsl:with-param name="view">wb_ui</xsl:with-param> -->
<!-- 			</xsl:call-template> -->
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<link rel="stylesheet" href="../static/css/three.css"/>
			
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_assignment.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
ass = new wbAssignment

function finish(){
	if (window.opener) {
		window.opener.document.location.href = window.opener.document.location.href ;
	}
	gen_close_win(window);
	
}

]]></SCRIPT>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="ass_submit" enctype="multipart/form-data">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ========================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="header">
			<xsl:with-param name="lab_ass_submission">作業提交</xsl:with-param>
			<xsl:with-param name="lab_finish">完成</xsl:with-param>
			<xsl:with-param name="lab_congratulations">恭喜你!你已成功提交你的作業。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="header">
			<xsl:with-param name="lab_ass_submission">作业提交</xsl:with-param>
			<xsl:with-param name="lab_finish">完成</xsl:with-param>
			<xsl:with-param name="lab_congratulations">恭喜! 作业已成功提交并即将批阅！</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="header">
			<xsl:with-param name="lab_ass_submission">Assignment submission</xsl:with-param>
			<xsl:with-param name="lab_finish">Finished</xsl:with-param>
			<xsl:with-param name="lab_congratulations">Congratulations! You have successfully submitted your assignment.</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ========================================================== -->
	<xsl:template name="header">
		<xsl:param name="lab_ass_submission"/>
		<xsl:param name="lab_finish"/>
		<xsl:param name="lab_congratulations"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_ass_submission"/>
				<xsl:text>&#160;-&#160;</xsl:text>
				<xsl:value-of select="header/title"/>
			</xsl:with-param>
			<xsl:with-param name="new_template">true</xsl:with-param>
		</xsl:call-template>
		<!-- Content -->
		
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text"/>
			<xsl:with-param name="new_template">true</xsl:with-param>
		</xsl:call-template>
		<div class="content_info report_title" style="padding:10px 0px;">
			<p class="work_font">
				<xsl:value-of select="$lab_congratulations"/>
			</p>
			<p class="work_font">
				<xsl:call-template name="unescape_html_linefeed">
					<xsl:with-param name="my_right_value"><xsl:value-of disable-output-escaping="yes" select="header/submission"/></xsl:with-param>
				</xsl:call-template>
			</p>
		</div>
		<div class="report_botton">
			<a href="javascript:;" onclick="javascript:ass.cancel('{/module/@tkh_id}',true)">
				<xsl:value-of select="$lab_g_form_btn_close"/>
			</a>
		</div>
		<input type="hidden" name="cmd" value="submit_ass"/>
		<input type="hidden" name="mod_id" value="{$mod_id}"/>
		<input type="hidden" name="step"/>
		<input type="hidden" name="ass_max_uplad" value="{$max_upload}"/>
		<input type="hidden" name="stylesheet"/>
		<input type="hidden" name="num_of_files" value="{$no_of_files}"/>
		<xsl:for-each select="body/uploadPathTemp/file">
			<input type="hidden" name="file{@id}" value="{@name}"/>
			<input type="hidden" name="comment{@id}" value="{@comment}"/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="cur_usr"/>
	<xsl:template match="env"/>
</xsl:stylesheet>
