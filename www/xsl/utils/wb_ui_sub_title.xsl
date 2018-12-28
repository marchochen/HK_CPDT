<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="wb_ui_sub_title">
		<xsl:param name="text"/>
		<xsl:param name="text_class">SubTitle</xsl:param>
		<xsl:param name="text_class_title">wzb-title-2</xsl:param>
		<xsl:param name="width" select="$wb_gen_table_width"/>
		<xsl:variable name="left_width">
			<xsl:choose>
				<xsl:when test="contains($width,'%')"><xsl:value-of select="number(substring-before($width,'%')) - 3"/>%</xsl:when>
				<xsl:otherwise><xsl:value-of select="$width - 7"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<table border="0" width="{$width}" cellspacing="0" cellpadding="2">
			<tr>
				<!--  
				<td>
				<xsl:attribute name="width">
					<xsl:choose>
						<xsl:when test="contains($width,'%')">3%</xsl:when>
						<xsl:otherwise>12</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
				<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/></td>
				-->
				<td width="{$left_width}">
					<div class="{$text_class}" style="padding:0 10px;">
					<span class="{$text_class_title}"><xsl:copy-of select="$text"/></span>
					</div>
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
