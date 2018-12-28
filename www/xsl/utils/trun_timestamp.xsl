<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- ==================================================================== -->
	<!-- Truncate Timestamp -->
	<xsl:template name="trun_timestamp">
		<xsl:param name="my_timestamp"/>
		<xsl:variable name="tmp_timestamp" select="substring-after(substring-after($my_timestamp,':'),':')"/>
		<xsl:variable name="date_length" select="string-length($my_timestamp) - string-length($tmp_timestamp) - 1"/>
		<xsl:variable name="final_timestamp" select="substring($my_timestamp,'1',$date_length)"/>
		<xsl:value-of select="$final_timestamp"/>
	</xsl:template>
</xsl:stylesheet>
