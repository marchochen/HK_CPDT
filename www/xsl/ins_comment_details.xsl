<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl" />
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<!-- cust utils -->
	<xsl:output indent="yes"/>
	<xsl:variable name="mod_cnt" select="count(instructor/instructorcomments/instructorcomment)"/>
	<xsl:variable name="page_size" select="/instructor/pagination/@page_size"/>
	<xsl:variable name="cur_page" select="/instructor/pagination/@cur_page"/>
	<xsl:variable name="total" select="/instructor/pagination/@total_rec"/>
	<xsl:variable name="timestamp" select="/instructor/pagination/@timestamp"/>
	<xsl:variable name="wb_gen_table_width">800</xsl:variable>
	<xsl:variable name="itm_title" select="/instructor/item/@title"/>
	<xsl:variable name="ins_name" select="/instructor/item/@instructor"/>
	
	<xsl:variable name="lab_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '972')"/>
	<xsl:variable name="lab_datime" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '973')"/>
	<xsl:variable name="lab_style_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '924')"/>
	<xsl:variable name="lab_quality_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '925')"/>
	<xsl:variable name="lab_structure_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '926')"/>
	<xsl:variable name="lab_interaction_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '927')"/>
	<xsl:variable name="lab_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '928')"/>
	<xsl:variable name="lab_no_result" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '974')"/>
	<xsl:variable name="lab_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '960')"/>
	<xsl:variable name="lab_itm_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '962')"/>
	<xsl:variable name="lab_ins_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1074')"/>
	<xsl:variable name="lab_close" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '772')"/>
	<!-- 按培训中心显示 -->
	<!-- ================================================================ -->
	<xsl:template match="/">
		<xsl:apply-templates select="instructor"/>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="instructor">
		<html>
			<xsl:call-template name="draw_header"/>
			<xsl:call-template name="draw_body"/>
		</html>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_header">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="new_css"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript"><![CDATA[
				
			]]></script>
		</head>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_body">
		<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0" >
			<form name="frmXml">
				<xsl:call-template name="content"/>
			</form>
		</body>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="content">
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_title" />
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="$mod_cnt='0'">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_result"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="wzb-ui-table"> <!-- table  -->
					<tr class="SecBg ">
						<td width="1" >
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td width="16%" >
							<span class="TitleText" style="color:#999999;">
								<xsl:value-of select="$lab_itm_title"/>
							</span>
						</td>
						<td width="11%" >
							<span class="TitleText" style="color:#999999;">
								<xsl:value-of select="$lab_ins_name"/>
							</span>
						</td>
						<td width="11%" >
							<span class="TitleText" style="color:#999999;">
								<xsl:value-of select="$lab_name"/>
							</span>
						</td>
						<td width="11%" >
							<span class="TitleText" style="color:#999999;">
								<xsl:value-of select="$lab_datime"/>
							</span>
						</td>
						<td width="9%" align="right">
							<span class="TitleText" style="color:#999999;">
								<xsl:value-of select="$lab_style_score"/>
							</span>
						</td>
						<td width="9%" align="right">
							<span class="TitleText" style="color:#999999;">
								<xsl:value-of select="$lab_quality_score"/>
							</span>
						</td>
						<td width="9%" align="right">
							<span class="TitleText" style="color:#999999;">
								<xsl:value-of select="$lab_structure_score"/>
							</span>
						</td>
						<td width="9%" align="right">
							<span class="TitleText" style="color:#999999;">
								<xsl:value-of select="$lab_interaction_score"/>
							</span>
						</td>
						<td width="10%" align="right">
							<span class="TitleText" style="color:#999999;">
								<xsl:value-of select="$lab_score"/>
							</span>
						</td>
						<td width="1">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<xsl:apply-templates select="instructorcomments/instructorcomment">
					</xsl:apply-templates>
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page" />
					<xsl:with-param name="page_size" select="$page_size" />
					<xsl:with-param name="timestamp" select="$timestamp" />
					<xsl:with-param name="total" select="$total" />
					<xsl:with-param name="width">
						<xsl:value-of select="$wb_gen_table_width" />
					</xsl:with-param>
				</xsl:call-template>
				 <!-- <xsl:call-template name="wb_ui_line" /> -->
			</xsl:otherwise>
		</xsl:choose>
		 <div class="wzb-bar" style="margin-top:0px">
			 <xsl:call-template name="wb_gen_form_button">
					<!-- 
						<xsl:with-param name="style">line-height:normal</xsl:with-param>
					 -->
					<xsl:with-param name="wb_gen_btn_name" select="$lab_close" />
					<xsl:with-param name="wb_gen_btn_href">javascript:window.close()</xsl:with-param>
				</xsl:call-template>
		</div>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="instructorcomment">
		<xsl:variable name="mod_id" select="@id" />
		<tr>
			<xsl:attribute name="class"><xsl:choose><xsl:when test="position() mod 2 = 1">RowsOdd</xsl:when><xsl:otherwise>RowsEven</xsl:otherwise></xsl:choose></xsl:attribute>
			<td width="1">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0" />
				<input type="hidden" name="cert_id" value="{@id}" />
			</td>
			<td nowrap="nowrap" width="15%">
				<span class="Text">
					<xsl:value-of select="$itm_title" />
				</span>
			</td>
			<td nowrap="nowrap" width="11%">
				<span class="Text">
					<xsl:value-of select="$ins_name" />
				</span>
			</td>
			<td nowrap="nowrap" width="11%">
				<span class="Text">
					<xsl:value-of select="name" />
				</span>
			</td>
			<td nowrap="nowrap" width="11%" >
				<span class="Text">
					<xsl:value-of select="substring(upd_datetime,1,16)" />
				</span>
			</td>
			<td width="8%" align="right">
				<span class="Text">
					<xsl:value-of select="style_score" />
				</span>
			</td>
			<td width="8%" align="right">
				<span class="Text">
					<xsl:value-of select="quality_score" />
				</span>
			</td>
			<td width="8%" align="right">
				<span class="Text">
					<xsl:value-of select="structure_score" />
				</span>
			</td>
			<td width="8%" align="right">
				<span class="Text">
					<xsl:value-of select="interaction_score" />
				</span>
			</td>
			<td width="12%" align="right">
				<span class="Text">
					<xsl:value-of select="score" />
				</span>
			</td>
			<td width="1">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0" />
			</td>
		</tr>
	</xsl:template>
	<!-- ================================================================ -->
</xsl:stylesheet>