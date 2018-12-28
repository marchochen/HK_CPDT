<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="../wb_const.xsl"/>
	<xsl:template name="get_rte_title">
		<xsl:param name="rte_type">
			<xsl:value-of select="@type"/>
		</xsl:param>
		<xsl:param name="lang">
			<xsl:value-of select="$wb_lang"/>
		</xsl:param>
		<xsl:param name="home">no</xsl:param>
		<xsl:variable name="lab_rte_id">
			<xsl:choose>
				<xsl:when test="$wb_lang = 'en' and $home = 'no'"><xsl:value-of select="concat('lab_rte_2_',$rte_type)"/></xsl:when>
				<xsl:otherwise><xsl:value-of select="concat('lab_rte_',$rte_type)"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, $lab_rte_id)"/>
	</xsl:template>
</xsl:stylesheet>