<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/res_get_init_share.xsl"/>
	<xsl:output  indent="yes"/>
	<xsl:template match="/">
		<html>
			<xsl:call-template name="new_css"/>
			<xsl:apply-templates mode="question"/>
		</html>
	</xsl:template>
	<xsl:template match="question" mode="question">
		<xsl:call-template name="res_get">
			<xsl:with-param name="sc_add_que_function">javascript:sc.get_Que_Content('<xsl:value-of select="@id"/>', <xsl:value-of select="header/objective/@id"/>,'<xsl:value-of select="header/@type"/>');</xsl:with-param>
			<xsl:with-param name="upd_function">javascript:que.edit_in_module('<xsl:value-of select="@id"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
			<xsl:with-param name="del_function"><xsl:choose><xsl:when test="@mod_type='TST'">javascript:que.del_in_module('<xsl:value-of select="@id"/>','<xsl:value-of select="$wb_lang"/>')</xsl:when><xsl:otherwise>javascript:que.del_in_module('<xsl:value-of select="@id"/>','<xsl:value-of select="$wb_lang"/>', '<xsl:value-of select="@mod_type"/>')</xsl:otherwise></xsl:choose></xsl:with-param>  
			<xsl:with-param name="header">NO</xsl:with-param>
			<xsl:with-param name="introduction">false</xsl:with-param>
			<xsl:with-param name="body_width" select="$wb_frame_table_width"/>
		</xsl:call-template>
	</xsl:template>
</xsl:stylesheet>
