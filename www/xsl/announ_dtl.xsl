<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
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
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<xsl:variable name="key" select="//announcement/meta/key/text()"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="announcement/message"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="message">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">
			<![CDATA[
			function resize_announce(){
				var height_cap = 500 ;// Max height
				if(document.page_end && (document.all || document.getElementById!=null)){
					var new_height = document.page_end.offsetTop + 60
					if(new_height > height_cap){
						new_height = height_cap
						
					}
					window.resizeTo(480,new_height);
				}
			}
			]]>
			</SCRIPT>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="resize_announce()" scroll="auto">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_ann">通告</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_ann">公告</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_ann">Announcement</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_ann"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:if test="not($key) or $key != 'lcms'">
			<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="text" select="$lab_ann"/>
			</xsl:call-template>
		</xsl:if>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<tr>
				<td width="2%">
				</td>
				<td width="96%">
							<xsl:value-of select="item/title/text()"/>
					<fieldset class="Box" style="background:none;">
						<table align="center" width="90%" border="0" cellspacing="0" cellpadding="5">
							<tr>
								<td>
									<xsl:value-of select="item/body" disable-output-escaping="yes"/>
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
		</table>
		<xsl:if test="not($key) or $key != 'lcms'">
			<div class="wzb-bar">
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_close"/></xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">javascript:self.close()</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
				</xsl:call-template>
			</div>
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
