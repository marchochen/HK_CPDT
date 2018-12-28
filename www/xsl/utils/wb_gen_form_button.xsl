<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<!-- =================================================================== -->
<xsl:template name="wb_gen_form_button">
	<xsl:param name="frm">document.frmXml</xsl:param>
	<xsl:param name="field_name">frmSubmitBtn</xsl:param>
	<xsl:param name="wb_gen_btn_name"/>
	<xsl:param name="wb_gen_btn_href"/>
	<xsl:param name="id"/>
	<xsl:param name="type">button</xsl:param>
	<xsl:param name="style"></xsl:param>
	<xsl:param name="class">btn wzb-btn-blue margin-right10 wzb-btn-big</xsl:param>
	<input class="{$class}" type="{$type}" value="{$wb_gen_btn_name}" name="{$field_name}" style="{$style}">
		<xsl:if test="$id != ''">
			<xsl:attribute name="id"><xsl:value-of select="$id"/></xsl:attribute>
		</xsl:if>
		<xsl:if test="$type = 'button'">
			<xsl:attribute name="onclick"><xsl:value-of select="$wb_gen_btn_href"/></xsl:attribute>
		</xsl:if>
	</input>
</xsl:template>
<!-- =================================================================== -->
</xsl:stylesheet>
