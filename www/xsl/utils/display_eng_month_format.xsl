<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<!-- Display time function -->
<xsl:template name="display_eng_month_format">
	<xsl:param name="month"/>
	<xsl:choose>
		<xsl:when test="$month = '01' or $month = '1'"><xsl:value-of select="$lab_mm_jan"/></xsl:when>
		<xsl:when test="$month = '02' or $month = '2'"><xsl:value-of select="$lab_mm_feb"/></xsl:when>
		<xsl:when test="$month = '03' or $month = '3'"><xsl:value-of select="$lab_mm_mar"/></xsl:when>
		<xsl:when test="$month = '04' or $month = '4'"><xsl:value-of select="$lab_mm_apr"/></xsl:when>
		<xsl:when test="$month = '05' or $month = '5'"><xsl:value-of select="$lab_mm_may"/></xsl:when>
		<xsl:when test="$month = '06' or $month = '6'"><xsl:value-of select="$lab_mm_jun"/></xsl:when>
		<xsl:when test="$month = '07' or $month = '7'"><xsl:value-of select="$lab_mm_jul"/></xsl:when>
		<xsl:when test="$month = '08' or $month = '8'"><xsl:value-of select="$lab_mm_aug"/></xsl:when>
		<xsl:when test="$month = '09' or $month = '9'"><xsl:value-of select="$lab_mm_sep"/></xsl:when>
		<xsl:when test="$month = '10'"><xsl:value-of select="$lab_mm_oct"/></xsl:when>
		<xsl:when test="$month = '11'"><xsl:value-of select="$lab_mm_nov"/></xsl:when>
		<xsl:when test="$month = '12'"><xsl:value-of select="$lab_mm_dec"/></xsl:when>
		<xsl:otherwise/>
	</xsl:choose>
</xsl:template>
</xsl:stylesheet>
