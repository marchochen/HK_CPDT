<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="display_time.xsl"/>

<!-- Truncate Timestamp to Date -->
<xsl:template name="trun_date">
<xsl:param name="my_timestamp"></xsl:param>
	<xsl:call-template name="display_time"><xsl:with-param name="my_timestamp"><xsl:value-of select="$my_timestamp"/></xsl:with-param></xsl:call-template>
</xsl:template>
</xsl:stylesheet>
