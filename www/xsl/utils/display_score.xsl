<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template name="display_score">
	<xsl:param name="score"/>
	<xsl:variable name="before_dot" select="substring-before($score,'.')"/>
	<xsl:variable name="after_dot" select="substring-after($score,'.')"/>
	<xsl:choose>
		<xsl:when test="string(number($score)) = 'NaN'"><xsl:value-of select="$score"/></xsl:when>
		<xsl:when test="$after_dot = ''"><xsl:value-of select="$score"/></xsl:when>
		<xsl:otherwise>
			<xsl:if test="$before_dot = ''">0</xsl:if>
			<xsl:value-of select="$before_dot"/>
			<xsl:variable name="trun_after_dot" select="substring($after_dot,1,1)"/>
			<xsl:if test="$trun_after_dot != 0">
			   <xsl:text>.</xsl:text><xsl:value-of select="$trun_after_dot"/>
			</xsl:if>		
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>
</xsl:stylesheet>
