<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="wb_ui_line.xsl"/>
	<xsl:template name="wb_ui_footer"></xsl:template>
	<xsl:template name="wb_ui_footer__">
		<xsl:param name="width" select="$wb_gen_table_width"/>
		<xsl:param name="text_class">Text</xsl:param>
		<xsl:param name="table_class"/>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="height">2</xsl:with-param>
			<xsl:with-param name="bg_class">HomeStepBottomLine</xsl:with-param>
		</xsl:call-template>
		<table width="{$width}" cellpadding="0" cellspacing="0" border="0">
			<xsl:if test="$table_class != ''">
				<xsl:attribute name="class"><xsl:value-of select="$table_class"/></xsl:attribute>
			</xsl:if>
			<tr>
				<td align="center">
					<img src="{$wb_img_skin_path}footer.jpg"/> 
				<!--	<a class="{$text_class}" href="http://www.cyberwisdom.net" target="_blank">
						<xsl:choose>
							<xsl:when test="$wb_lang = 'ch'">此網站由wizBank<xsl:text disable-output-escaping="yes">&amp;trade;</xsl:text>支援</xsl:when>
							<xsl:when test="$wb_lang = 'gb'">This site is powered by wizBank<xsl:text disable-output-escaping="yes">&amp;trade;</xsl:text></xsl:when>
							<xsl:when test="$wb_lang = 'en'">This site is powered by wizBank<xsl:text disable-output-escaping="yes">&amp;trade;</xsl:text></xsl:when>
						</xsl:choose>
					</a>
					-->
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
