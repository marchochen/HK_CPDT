<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- == Utils for wizBank Core only ===================================-->
<!-- init Resource Manager & Resource Manager Lost and Found Frameset height -->
<!-- == Variable ============================================================-->
<xsl:variable name="wb_res_frame_height">130</xsl:variable>
<xsl:variable name="wb_km_frame_height">60</xsl:variable>
<!-- wizbank title name -->
<xsl:variable name="wb_wizbank">
	  <xsl:choose>
		<xsl:when test="$wb_lang= 'ch'">Cyberwisdom wizBank 6.3  匯思整合移動學習整合管理平臺</xsl:when>
		<xsl:when test="$wb_lang= 'gb'">Cyberwisdom wizBank 6.3  汇思整合移动学习整合管理平台</xsl:when>
		<xsl:when test="$wb_lang= 'en'">Cyberwisdom wizBank 6.3 Learning Management System</xsl:when>
		<xsl:otherwise>Cyberwisdom wizBank  汇思整合移动学习整合管理平台 </xsl:otherwise>
	  </xsl:choose>
</xsl:variable>
<!-- init General Table Width -->
<xsl:variable name="wb_gen_table_width">984</xsl:variable>
<xsl:variable name="wb_gen_msg_box_width">400</xsl:variable>
<xsl:variable name="wb_frame_table_width">100%</xsl:variable>
<!-- init credit -->
<xsl:variable name="wb_gen_num_input_length">3</xsl:variable>
<xsl:variable name="wb_gen_num_input_size">4</xsl:variable>
</xsl:stylesheet>
