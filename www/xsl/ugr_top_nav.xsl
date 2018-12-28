<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_gen_tab.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<!-- others -->	
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/usr_gen_tab_share.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->	
	<xsl:template match="/">
		<html><xsl:call-template name="main"/></html>
	</xsl:template>
	<!-- =============================================================== -->	
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE><xsl:value-of select="$wb_wizbank"/></TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_ugr.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">tab</xsl:with-param>
			</xsl:call-template>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			usr = new wbUserGroup;
			var ugr = new wbUgr();
			var ugr_order = '';

			function init() {
				var obj1 = document.getElementById("NavPage");
				obj1.src = ugr.ugr_nav_url();
				var obj2 = document.getElementById("ContentPage");
				obj2.src = ugr.ugr_home_url();
			}

			// show page
			function show_frame_content(myURL) {
				document.getElementById("ContentPage").src = myURL; 
			}
			function home() {
				url = ugr.ugr_home_url();
				show_frame_content(url);
			}
			function save_ugr_order(order) {
				ugr_order = order;
				document.getElementById("CallServletPage").src = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','ugr_order_upd.xsl');
			}
			]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="init()">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->	
	<xsl:template name="lang_ch">
		<xsl:apply-templates>
			<xsl:with-param name="lab_title">職級管理</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates>
			<xsl:with-param name="lab_title">职级管理</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates>
			<xsl:with-param name="lab_title">Grade management</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>	
	<!-- =============================================================== -->	
	<xsl:template match="tree">
		<xsl:param name="lab_title"/>
		<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_GRADE_MAIN</xsl:with-param>
			</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text"><xsl:value-of select="$lab_title"/></xsl:with-param>
		</xsl:call-template>
		<!--  
		<xsl:call-template name="usr_gen_tab">
			<xsl:with-param name="usr_target_tab">4</xsl:with-param>
		</xsl:call-template>-->
		<!-- <img src="{$wb_img_path}tp.gif" width="1" height="1" border="0" id="bottom_img"/> -->
		<table width="100%" align="top" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="0">
					<iframe width="0" src="../htm/empty.htm" scrolling="no" frameborder="0" id="CallServletPage"/>
				</td>
				<td width="386px">
					<iframe width="100%" height="500" frameborder="0" id="NavPage" name="NavPage"/>
				</td>
				<td>
					<iframe width="100%" height="445" frameborder="0" id="ContentPage" name="ContentPage"/>
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
