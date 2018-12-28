<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:variable name="lab_code_en_us">
	<xsl:choose>
		<xsl:when test="$wb_lang = 'ch'">英文</xsl:when>
		<xsl:when test="$wb_lang = 'gb'">英语</xsl:when>
		<xsl:otherwise>English</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="lab_code_zh_cn">
	<xsl:choose>
		<xsl:when test="$wb_lang = 'ch'">簡體中文</xsl:when>
		<xsl:when test="$wb_lang = 'gb'">简体中文</xsl:when>
		<xsl:otherwise>Simplified Chinese</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="lab_code_zh_hk">
	<xsl:choose>
		<xsl:when test="$wb_lang = 'ch'">繁體中文</xsl:when>
		<xsl:when test="$wb_lang = 'gb'">繁体中文</xsl:when>
		<xsl:otherwise>Traditional Chinese</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:template name="get_lang_code_label">
	<xsl:param name="code"/>
	<xsl:choose>
		<xsl:when test="$code = 'en-us'"><xsl:value-of select="$lab_code_en_us"/></xsl:when>
		<xsl:when test="$code = 'zh-cn'"><xsl:value-of select="$lab_code_zh_cn"/></xsl:when>
		<xsl:when test="$code = 'zh-hk'"><xsl:value-of select="$lab_code_zh_hk"/></xsl:when>
	</xsl:choose>
</xsl:template>
</xsl:stylesheet>
