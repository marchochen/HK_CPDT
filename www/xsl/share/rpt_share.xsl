<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="omit_title">
		<xsl:param name="title"/>
		<xsl:param name="retain_length">10</xsl:param>
		<xsl:param name="display_length">23</xsl:param>
		<xsl:param name="showtitle">True</xsl:param>
		<xsl:choose>
			<xsl:when test="string-length($title) > $display_length">
			<xsl:if test="$showtitle = 'True'">
			<xsl:attribute name="title"><xsl:value-of select="$title"/></xsl:attribute>
			</xsl:if>
				
				<xsl:value-of select="substring($title,1,$retain_length)"/>
				<xsl:text>...</xsl:text>
				<xsl:variable name="title_length">
					<xsl:value-of select="string-length($title)"/>
				</xsl:variable>
				<xsl:value-of select="substring($title,$title_length - $retain_length + 1,$retain_length)"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$title"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
