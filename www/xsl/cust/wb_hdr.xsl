<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../utils/wb_ui_hdr.xsl"/>
	<xsl:import href="../utils/wb_ui_title.xsl"/>	
	

<!-- Head bar-->
<xsl:template name="wb_hdr">
	<xsl:param name="navigation"/>
	<xsl:param name="head_bar"/>
	<xsl:param name="head_button"/>
	<xsl:param name="tab_highlight"/>
	<xsl:param name="width"><xsl:value-of select="$wb_gen_table_width"/></xsl:param>
	<xsl:param name="title">&#160;</xsl:param>
	<xsl:param name="role_box"/>
	<xsl:param name="mode"/>		
	<xsl:param name="show_home">true</xsl:param>
	<xsl:param name="show_back">true</xsl:param>
	<xsl:param name="show_functions">true</xsl:param>
	<xsl:call-template name="wb_ui_hdr">
		<xsl:with-param name="show_home" select="$show_home"/>
		<xsl:with-param name="show_back" select="$show_back"/>
		<xsl:with-param name="show_functions" select="$show_functions"/>
		<xsl:with-param name="width" select="$width"/>
	</xsl:call-template>
	<xsl:call-template name="wb_ui_title">
		<xsl:with-param name="text" select="$navigation"/>
	</xsl:call-template>

</xsl:template>
</xsl:stylesheet>
