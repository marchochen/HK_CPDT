<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	<!-- ======================================================================= -->
	<!-- Image rollover -->
	<xsl:template name="wb_gen_button" priority="10">
		<xsl:param name="wb_gen_btn_a_name" />
		<xsl:param name="wb_gen_btn_name" />
		<!-- image name -->
		<xsl:param name="wb_gen_btn_href" />
		<!-- image refrence -->
		<xsl:param name="wb_gen_btn_target" />
		<xsl:param name="class">btn wzb-btn-blue margin-right4</xsl:param>
		<xsl:param name="style" />
		<!-- image target -->
		<a  class="{$class}"> <!-- style="margin-top:-3px;"  不要写死 -->
			<xsl:if test="$style != ''">
				<xsl:attribute name="style"><xsl:value-of select="$style" /></xsl:attribute>
			</xsl:if>
			<xsl:attribute name="href"><xsl:value-of select="$wb_gen_btn_href" /></xsl:attribute>
			<xsl:if test="$wb_gen_btn_a_name != ''">
				<xsl:attribute name="name"><xsl:value-of select="$wb_gen_btn_a_name" /></xsl:attribute>
			</xsl:if>
			<xsl:if test="$wb_gen_btn_target != ''">
				<xsl:attribute name="target"><xsl:value-of select="$wb_gen_btn_target" /></xsl:attribute>
			</xsl:if>
			<xsl:value-of select="$wb_gen_btn_name" />
		</a>
	</xsl:template>
	<!-- ======================================================================= -->
	<!-- Image rollover   btn-Orange.-->
	<xsl:template name="wb_gen_button_orange" priority="10">
		<xsl:param name="wb_gen_btn_a_name" />
		<xsl:param name="wb_gen_btn_name" />
		<!-- image name -->
		<xsl:param name="wb_gen_btn_href" />
		<!-- image refrence -->
		<xsl:param name="wb_gen_btn_target" />
		<xsl:param name="class">
			btn wzb-btn-orange margin-right4
		</xsl:param>
		<xsl:param name="style" />
		<!-- image target -->
		<a class="{$class}">
			<xsl:if test="$style != ''">
				<xsl:attribute name="style"><xsl:value-of select="$style" /></xsl:attribute>
			</xsl:if>
			<xsl:attribute name="href"><xsl:value-of select="$wb_gen_btn_href" /></xsl:attribute>
			<xsl:if test="$wb_gen_btn_a_name != ''">
				<xsl:attribute name="name"><xsl:value-of select="$wb_gen_btn_a_name" /></xsl:attribute>
			</xsl:if>
			<xsl:if test="$wb_gen_btn_target != ''">
				<xsl:attribute name="target"><xsl:value-of select="$wb_gen_btn_target" /></xsl:attribute>
			</xsl:if>
			<xsl:value-of select="$wb_gen_btn_name" />
		</a>
	</xsl:template>
	<!-- ======================================================================= -->
</xsl:stylesheet>
