<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!-- =============================================================== -->
<!-- New Meun Style Navigation for wizbank v3.5-->
<xsl:template name="wb_gen_nav_home">
	<!--
	<xsl:if test="//cur_usr/@role_url_home!=''">
	-->
	<xsl:variable name="go_home">
		<xsl:choose>
			<xsl:when test="//cur_usr/@role_url_home!=''">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
		<a  href="javascript:wb_utils_gen_home({$go_home})">
			<xsl:choose>
				<xsl:when test="$wb_lang = 'gb'">首页</xsl:when>
				<xsl:when test="$wb_lang = 'ch'">首頁</xsl:when>
				<xsl:otherwise>Home</xsl:otherwise>
			</xsl:choose>
		</a>
	<!--
	</xsl:if>
	-->
</xsl:template>
</xsl:stylesheet>
