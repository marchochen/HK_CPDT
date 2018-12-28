<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<!-- const -->
<xsl:import href="../wb_const.xsl"/>
<!-- =============================================================== -->
<!-- Initial label variable -->
<xsl:template name="wb_init_lab">
	<xsl:choose>
		<xsl:when test="$wb_lang='ch'"><xsl:call-template name="lang_ch"/></xsl:when>
		<xsl:when test="$wb_lang ='gb'"><xsl:call-template name="lang_gb"/></xsl:when>
		<xsl:otherwise><xsl:call-template name="lang_en"/></xsl:otherwise>
	</xsl:choose>
</xsl:template>
<!-- =============================================================== -->
</xsl:stylesheet>
