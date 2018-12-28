<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
    <!-- const -->
	<xsl:import href="wb_const.xsl" />
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl" />
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl" />
	<xsl:import href="utils/wb_ui_hdr.xsl" />
	<xsl:import href="utils/wb_ui_title.xsl" />
	<xsl:import href="utils/wb_ui_nav_link.xsl" />
	<xsl:import href="utils/wb_ui_head.xsl" />
	<xsl:import href="utils/wb_ui_line.xsl" />
	<xsl:import href="utils/wb_ui_space.xsl" />
	<xsl:import href="utils/wb_ui_footer.xsl" />
	<xsl:import href="utils/wb_gen_form_button.xsl" />
	<xsl:import href="utils/wb_gen_button.xsl" />
	<xsl:import href="utils/wb_goldenman.xsl" />

	<xsl:variable name="lab_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '329')" />
	<xsl:variable name="lab_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')" />
	<xsl:variable name="lab_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN044')" />
	<xsl:variable name="lab_LN045" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN045')" />
	<xsl:variable name="lab_LN046" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN046')" />
	<xsl:variable name="lab_LN047" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN047')" />
	<xsl:variable name="lab_LN048" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN048')" />
	<xsl:variable name="lab_LN049" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN049')" />
	<xsl:variable name="lab_LN050" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN050')" />
	<!-- ================================================================== -->
	<xsl:template match="/course">
		<xsl:call-template name="main" />
	</xsl:template>
	<!--=================================================================== -->
	<xsl:template name="main">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}" />
				<title>
					<xsl:value-of select="$wb_wizbank" />
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js" />
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[
					goldenman = new wbGoldenMan;
					course = new wbCourse;
			    ]]></script>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
				<form name="frmXml" enctype="multipart/form-data">
					<input type="hidden" name="module" />
					<input type="hidden" name="cmd" />
					<input type="hidden" name="url_failure" />
					<input type="hidden" name="url_success" />
					<input type="hidden" name="lab_LN049" value="{$lab_LN049}" />
					<input type="hidden" name="lab_LN050" value="{$lab_LN050}" />
					<xsl:call-template name="content" />
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template name="content">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_COURSE_ASSIGN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_title" />
		</xsl:call-template>
		<table>
			<tr>
				<td align="left">
					<span class="wzb-form-star">*</span>
					<xsl:value-of select="$lab_LN045" />
					<xsl:text>：</xsl:text>
				</td>
			</tr>
			
			<tr>
				<td width="45%" valign="top">
					<div style="width: 100%;">
						<xsl:choose>
							<xsl:when test="count(tc_list/training_center) = 0">
							</xsl:when>
							<xsl:otherwise>
								<label>
									<input onclick="javascript:gen_frm_sel_all_checkbox(document.frmXml, this, 'tcr_id_lsts')" id="sel_all" type="checkbox" name="sel_all_checkbox" />
									<span style="margin-left:5px"><xsl:value-of select="$lab_LN046" /></span>
								</label>
								<br />
								
<!-- 递归循环出所有培训中心 -->
								<xsl:for-each select="tc_list/training_center">
<!-- 									<div style="width: 49%; float: left;">
										<label>
											<input type="checkbox" name="tcr_id_lsts" value="{@id}" />
											<xsl:value-of select="@name" />
										</label>
									</div> -->
									<xsl:call-template name="show_tcenter">
										<xsl:with-param name="tc" select="."/>
									</xsl:call-template>
								</xsl:for-each>
							</xsl:otherwise>
						</xsl:choose>
					</div>
				</td>
				<td width="40%" valign="bottom">
					<xsl:value-of select="$lab_LN047" />
					<xsl:call-template name="wb_goldenman">
						<xsl:with-param name="field_name">itm_id_lsts</xsl:with-param>
						<xsl:with-param name="name">itm_id_lst</xsl:with-param>
						<xsl:with-param name="box_size">20</xsl:with-param>
						<xsl:with-param name="select_type">3</xsl:with-param>
						<xsl:with-param name="args_type">row</xsl:with-param>
						<xsl:with-param name="complusory_tree">0</xsl:with-param>
						<xsl:with-param name="pick_root">0</xsl:with-param>
						<xsl:with-param name="show_box">true</xsl:with-param>
						<xsl:with-param name="remove_btn">true</xsl:with-param>
						<xsl:with-param name="tree_type">TC_CATALOG_ITEM_SELF</xsl:with-param>
						<xsl:with-param name="height">303</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_ok" />
				<xsl:with-param name="wb_gen_btn_href">
					<xsl:text>javascript: course.course_designate(document.frmXml);</xsl:text>
				</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_cancel" />
				<xsl:with-param name="wb_gen_btn_href">
					<xsl:text>javascript:wb_utils_gen_home();</xsl:text>
				</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	
	
	<xsl:template name="show_tcenter">
		<xsl:param name="tc"/>
		<xsl:variable name="index" select="$tc/@index"/>
		
		<xsl:choose>
			<xsl:when test="$index = 0">
				<div style="width: 100%; float: left;">
					<label>
						<input type="checkbox" name="tcr_id_lsts" value="{$tc/@id}" />
						<span style="margin-left:5px"><xsl:value-of select="$tc/@name" /></span>
					</label>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<div style="width: 25%; float: left;padding-left:15px;">
					<label>
						<input type="checkbox" name="tcr_id_lsts" value="{$tc/@id}" />
						<span style="margin-left:5px"><xsl:value-of select="$tc/@name" /></span>
					</label>
				</div>
			</xsl:otherwise>
		</xsl:choose>

 		<xsl:if test="count($tc/training_center) &gt; 0">
			<xsl:for-each select="$tc/training_center">
				<xsl:call-template name="show_tcenter">
					<xsl:with-param name="tc" select="."/>
				</xsl:call-template>
			</xsl:for-each>
		</xsl:if> 

	</xsl:template>
	
</xsl:stylesheet>