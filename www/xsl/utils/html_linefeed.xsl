<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template name="html_linefeed">
	<xsl:param name="my_right_value"/>
	<xsl:param name="my_left_value"/>
	<xsl:param name="js"/>
	<xsl:variable name="bef_value" select='substring-before($my_right_value,"&#10;")'/>
	<xsl:variable name="aft_value" select='substring-after($my_right_value,"&#10;")'/>
	<xsl:choose>
		<xsl:when test='$bef_value = "" and $aft_value = "" and not(contains($my_right_value,"&#10;"))'>
			<xsl:value-of select="concat($my_left_value, $my_right_value)"/>
		</xsl:when>
		<xsl:otherwise>
			<!--<xsl:choose>
				<xsl:when test="js = 'true'"><xsl:variable name="my_left_value" select='concat($my_left_value, $bef_value, "###")'/></xsl:when>
				<xsl:otherwise><xsl:variable name="my_left_value" select='concat($my_left_value, $bef_value, " &lt;br> ")'/></xsl:otherwise>
			</xsl:choose>-->
			<xsl:variable name="my_left_value">
				<xsl:choose>
					<xsl:when test="$js = 'true'"><xsl:value-of select='concat($my_left_value, $bef_value, "\n")'/></xsl:when>
					<xsl:otherwise><xsl:value-of select='concat($my_left_value, $bef_value, " &lt;br> ")'/></xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:call-template name="html_linefeed">
				<xsl:with-param name="js"><xsl:value-of select="$js"/></xsl:with-param>
				<xsl:with-param name="my_right_value"><xsl:value-of select="$aft_value"/></xsl:with-param><xsl:with-param name="my_left_value"><xsl:value-of select="$my_left_value"/></xsl:with-param>
			</xsl:call-template>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>
</xsl:stylesheet>
