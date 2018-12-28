<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- <xsl:import href="../utils/wb_ui_space.xsl"/> -->
	<xsl:template name="wb_ui_nav_link">
		<xsl:param name="text"/>
		<xsl:param name="text_class">NavLink</xsl:param>
		<xsl:param name="width" select="$wb_gen_table_width"/>
		<xsl:variable name="left_width">
			<xsl:choose>
				<xsl:when test="contains($width,'%')"><xsl:value-of select="number(substring-before($width,'%')) - 4"/>%</xsl:when>
				<xsl:otherwise><xsl:value-of select="$width - 6"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- <xsl:call-template name="wb_ui_space">
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template> -->
		<table >
			<tr>
				<td>
				<xsl:attribute name="width">
					<xsl:choose>
						<xsl:when test="contains($width,'%')">2%</xsl:when>
						<xsl:otherwise>14</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
				</td>
				<td width="{$left_width}">
					<div class="{$text_class}">
						<xsl:copy-of select="$text"/>
					</div>
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
