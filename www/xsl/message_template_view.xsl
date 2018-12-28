<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
<!-- 	<xsl:import href="utils/wb_init_lab.xsl"/> -->
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<!-- cust-->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- ============================================================= -->
	<xsl:variable name="supplierName" select="//spl_name"/>
	<xsl:variable name="order_by" select="//pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="//pagination/@sort_order"/>
	<xsl:variable name="sel_status" select="//sel_statu/@name"/>
	<xsl:variable name="sort_order">
		<xsl:choose>
			<xsl:when test="$cur_order = 'asc'">desc</xsl:when>
			<xsl:otherwise>asc</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_template_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1144')"/> 	

	<xsl:variable name="lab_g_form_btn_close" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '772')"/>
	<xsl:variable name="lab_remark" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1145')"/>
	<xsl:variable name="mtp_remark_label" select="/message_module/message_template/mtp_remark_label/text()"/>
	<xsl:variable name="lab_remark_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, $mtp_remark_label)"/> 
	<xsl:variable name="lab_condition" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1150')"/>
	<xsl:variable name="mtp_type" select="/message_module/message_template/mtp_type/text()"/>
	<xsl:variable name="lab_condition_lable" select="concat('lab_condition_', $mtp_type)"/>
	<xsl:variable name="lab_condition_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding,$lab_condition_lable)"/>
	<!-- =============================================================== -->
	<xsl:template match="/message_module">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>			
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>

			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
		</head>
		<xsl:call-template name="new_css" />
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="content"/>
		</body>
	</xsl:template>	
	<!-- =============================================================== -->
	<xsl:template name="content" >
		<form name="frmXml">
		
			
			<table width="680" cellspacing="0" cellpadding="3" border="0" class="Bg">
			<tr>
				<td width="2%">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
				<td width="96%">							
					<span class="TitleText">
						<xsl:value-of select="message_template/mtp_subject/text()"/>
					</span>				
					<fieldset class="Box" style="background:none;">
						<table align="center" width="90%" border="0" cellspacing="0" cellpadding="5">
							<tr>
								<td>
									<span class="Text">
										<xsl:value-of select="message_template/mtp_content" disable-output-escaping="yes"/>
									</span>
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
				<td width="2%">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
			
			<!--条件
			<xsl:if test="$lab_condition_desc !=''">			
			<tr>
				<td width="2%">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
				<td width="96%">
					<span class="TitleText">
						<xsl:value-of select="$lab_condition"/>:<br/></span>
					<span class="Text">
						<xsl:value-of select="$lab_condition_desc" disable-output-escaping="yes"/>
					</span>
				</td>
				<td width="2%">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
			
			</xsl:if>
			-->
			<!--备注
			<xsl:if test="$lab_remark_desc !=''">
			<tr>
				<td width="2%">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
				<td width="96%">
					<span class="TitleText">
						<xsl:value-of select="$lab_remark"/>:<br/></span>
					<span class="Text">
						<xsl:value-of select="$lab_remark_desc" disable-output-escaping="yes"/>
					</span>
				</td>
				<td width="2%">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
			</xsl:if>
			-->
			<tr>
				<td colspan="3" height="10">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_close"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:self.close()</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
		</div>
	<!-- =============================================================== -->
		</form>
	</xsl:template>

	<!-- =============================================================== -->
		
</xsl:stylesheet>