<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:template name="get_rol_title">
		<xsl:param name="rol_ext_id">
			<xsl:value-of select="@id"/>
		</xsl:param>
		<xsl:param name="rol_title">
			<xsl:value-of select="@tilte"/>
		</xsl:param>
		<xsl:param name="is_menu">false</xsl:param>
		<xsl:variable name="lab_rol_id">
			<xsl:choose>
				<xsl:when test="not(contains($rol_ext_id,'_'))"></xsl:when>
				<xsl:otherwise><xsl:value-of select="concat('lab_rol_',substring-before($rol_ext_id,'_'))"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$lab_rol_id =''"><xsl:value-of select="@title"/></xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$rol_ext_id = 'TADM_1' and $is_menu = 'false'"><xsl:value-of select="$lab_const_role"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, $lab_rol_id)"/></xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>