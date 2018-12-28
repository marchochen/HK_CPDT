<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:import href="../utils/wb_gen_tab.xsl"/>

<xsl:variable name="lab_user_report"><xsl:choose><xsl:when test="$wb_lang = 'ch'">用戶報告</xsl:when><xsl:when test="$wb_lang = 'gb'">用户报告</xsl:when><xsl:otherwise>Learner report</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_usage_report"><xsl:choose><xsl:when test="$wb_lang = 'ch'">使用報告</xsl:when><xsl:when test="$wb_lang = 'gb'">使用报告</xsl:when><xsl:otherwise>Usage report</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="lab_export_data"><xsl:choose><xsl:when test="$wb_lang = 'ch'">詳細報告</xsl:when><xsl:when test="$wb_lang = 'gb'">详细报告</xsl:when><xsl:otherwise>Details report</xsl:otherwise></xsl:choose></xsl:variable>

<xsl:template name="tracking_rpt_gen_tab">
	<xsl:param name="rpt_target_tab">1</xsl:param>
	<xsl:param name="itm_id">0</xsl:param>
	<xsl:param name="rpt_tab_name_lst">
		<!-- 1 -->
		<xsl:value-of select="$lab_user_report"/>
		<!-- 2 -->
		<xsl:text>:_:_:</xsl:text><xsl:value-of select="$lab_usage_report"/>

	</xsl:param>
	<xsl:param name="rpt_tab_link_lst">
		<!-- 1 -->
		<xsl:text>javascript:rpt.open_cos_lrn_lst(</xsl:text><xsl:value-of select="$itm_id"/><xsl:text>)</xsl:text>
		<!-- 2 -->
		<xsl:text>:_:_:</xsl:text><xsl:text>javascript:rpt.usage_mod_lst_prep(</xsl:text><xsl:value-of select="$itm_id"/><xsl:text>)</xsl:text>

	</xsl:param>	
	<xsl:call-template name="wb_gen_tab">
		<xsl:with-param name="tab_name_lst"><xsl:value-of select="$rpt_tab_name_lst"/></xsl:with-param>
		<xsl:with-param name="tab_link_lst"><xsl:value-of select="$rpt_tab_link_lst"/></xsl:with-param>
		<xsl:with-param name="target_tab"><xsl:value-of select="$rpt_target_tab"/></xsl:with-param>			
	</xsl:call-template>
</xsl:template>
<!-- ======================================================================== -->
</xsl:stylesheet>	

