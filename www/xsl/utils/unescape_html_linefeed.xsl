<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template name="unescape_html_linefeed">
	<xsl:param name="replace_char">&lt;br&gt;</xsl:param>
	<xsl:param name="my_right_value"/>
	<xsl:choose>
		<xsl:when test="not(contains($my_right_value,'&#10;'))">
			<xsl:value-of select="$my_right_value"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:variable name="bef_value" select='substring-before($my_right_value,"&#10;")'/>
			<xsl:variable name="aft_value" select='substring-after($my_right_value,"&#10;")'/>
			<xsl:value-of select="$bef_value"/>
			<xsl:value-of disable-output-escaping="yes" select="$replace_char"/>
			<xsl:call-template name="unescape_html_linefeed"><xsl:with-param name="replace_char" select="$replace_char"/><xsl:with-param name="my_right_value" select="$aft_value"/></xsl:call-template>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>
</xsl:stylesheet>
