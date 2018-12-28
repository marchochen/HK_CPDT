<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="share/res_get_init_share.xsl"/>
	<xsl:output  indent="yes"/>
	<xsl:template match="/">
		<html>
			<xsl:apply-templates mode="question"/>
		</html>
	</xsl:template>
	<xsl:template match="question" mode="question">
		<xsl:variable name="sc_add_que_function">javascript:sc.get_Que_Content('<xsl:value-of select="@id"/>', <xsl:value-of select="header/objective/@id"/>,'<xsl:value-of select="header/@type"/>');</xsl:variable>
		<xsl:variable name="upd_function">javascript:que.edit_in_search('<xsl:value-of select="/question/@id"/>',document)                          </xsl:variable>
		<xsl:variable name="del_function">javascript:que.del_in_search('<xsl:value-of select="/question/@id"/>','<xsl:value-of select="/question/header/objective/@id"/>','<xsl:value-of select="$wb_lang"/>')</xsl:variable>
		<xsl:variable name="cp_function">javascript:que.copy('<xsl:value-of select="/question/@id"/>','<xsl:value-of select="$wb_lang"/>',document)</xsl:variable>
		<xsl:call-template name="res_get">
			<xsl:with-param name="upd_function">
				<xsl:choose>
					<xsl:when test="/question/page_variant/@hasEditResBtn= 'true'">
						<xsl:value-of select="$upd_function"/>
					</xsl:when>
					<xsl:otherwise/>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="del_function">
				<xsl:choose>
					<xsl:when test="/question/page_variant/@hasRemoveResBtn= 'true'">
						<xsl:value-of select="$del_function"/>
					</xsl:when>
					<xsl:otherwise/>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="cp_function" select="$cp_function"/>
			<xsl:with-param name="sc_add_que_function" select="$sc_add_que_function"/>
			<xsl:with-param name="body_width" select="$wb_gen_table_width"/>
			<xsl:with-param name="header">YES</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
</xsl:stylesheet>
