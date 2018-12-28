<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="share/wb_layout_share.xsl"/>
	<xsl:import href="share/asm_action_init.xsl"/>
	<xsl:import href="share/asm_action_utils.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- <xsl:import href="res_action_utils.xsl"/>	-->
	<xsl:output indent="yes"/>
	<xsl:variable name="root_tag" select="/dynamic_que_container"/>
	<xsl:variable name="wb_gen_table_width" select="$wb_frame_table_width"/>
	<xsl:template match="/">
		<html>
			<xsl:call-template name="asm_action">
				<xsl:with-param name="save_function">javascript:asm.sendFrm(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
				<xsl:with-param name="cancel_function">javascript:history.go(-1)</xsl:with-param>
				<xsl:with-param name="header">NO</xsl:with-param>
				<xsl:with-param name="width" select="$wb_gen_table_width"/>
				<xsl:with-param name="mode">INS</xsl:with-param>
				<xsl:with-param name="type">DAS</xsl:with-param>
			</xsl:call-template>
		</html>
	</xsl:template>
</xsl:stylesheet>
