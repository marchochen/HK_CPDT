<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/asm_get_init.xsl"/>
	<xsl:import href="share/asm_get_utils.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:template match="/">
		<html>
			<xsl:call-template name="new_css"/>
			<xsl:apply-templates select="fixed_assessment"/>
		</html>
	</xsl:template>
	<xsl:template match="fixed_assessment">
		<xsl:variable name="edit_q_function">javascript:wb_utils_set_cookie('res_subtype','<xsl:value-of select="header/@subtype"/>');wb_utils_set_cookie('res_id','<xsl:value-of select="@id"/>');asm.asm_q('<xsl:value-of select="@id"/>','<xsl:value-of select="header/@subtype"/>', 'READONLY')</xsl:variable>
		<xsl:call-template name="asm_get">
			<xsl:with-param name="header">NO</xsl:with-param>
			<xsl:with-param name="edit_q_function" select="$edit_q_function"/>
			<xsl:with-param name="body_width" select="$wb_frame_table_width"/>
			<xsl:with-param name="type" select="header/@subtype"/>
			<xsl:with-param name="mode">READONLY</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
</xsl:stylesheet>
