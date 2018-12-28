<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<!-- ==================================================================== -->
<!-- Display form input time function -->
<xsl:template name="display_form_input_hhmm">
	<xsl:param name="def_time"/>
	<xsl:param name="fld_name"/>
	<xsl:param name="timestamp"/>
	<xsl:param name="frm">document.frmXml</xsl:param>
	<xsl:param name="show_label">Y</xsl:param>
	<xsl:param name="focus_rad_btn_name"/>
	<xsl:param name="caching_function"/>
	<xsl:param name="class">wzb-inputText</xsl:param>
	<xsl:element name="input">
		<xsl:attribute name="type">text</xsl:attribute>
		<xsl:attribute name="name"><xsl:value-of select="$fld_name"/>_hour</xsl:attribute>
		<xsl:attribute name="maxlength">2</xsl:attribute>
		<xsl:attribute name="size">2</xsl:attribute>
		<xsl:attribute name="class" ><xsl:value-of select="$class"/></xsl:attribute>
		<xsl:choose>
			<xsl:when test="$def_time != '' and $timestamp = ''">
				<xsl:attribute name="value"><xsl:value-of select="substring($def_time,1,2)"/></xsl:attribute>
			</xsl:when>
			<xsl:otherwise>
				<xsl:attribute name="value"><xsl:value-of select="substring($timestamp,12,2)"/></xsl:attribute>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:attribute name="onkeyup">javascript:auto_focus_field(<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_hour,2,<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_min)</xsl:attribute>
		<xsl:if test="$focus_rad_btn_name != ''"><xsl:attribute name="onfocus">javascript:<xsl:value-of select="$frm"/>.<xsl:value-of select="$focus_rad_btn_name"/>.click()</xsl:attribute></xsl:if>
		<xsl:if test="not($caching_function = '')"><xsl:attribute name="onblur"><xsl:copy-of select="$caching_function"/></xsl:attribute></xsl:if>
	</xsl:element>
	<xsl:text>：</xsl:text>
	<xsl:element name="input">
		<xsl:attribute name="type">text</xsl:attribute>
		<xsl:attribute name="name"><xsl:value-of select="$fld_name"/>_min</xsl:attribute>
		<xsl:attribute name="maxlength">2</xsl:attribute>
		<xsl:attribute name="size">2</xsl:attribute>
		<xsl:attribute name="class" ><xsl:value-of select="$class"/></xsl:attribute>
		<xsl:choose>
			<xsl:when test="$def_time != '' and $timestamp = ''">
				<xsl:attribute name="value"><xsl:value-of select="substring($def_time,4,2)"/></xsl:attribute>
			</xsl:when>
			<xsl:otherwise>
				<xsl:attribute name="value"><xsl:value-of select="substring($timestamp,15,2)"/></xsl:attribute>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if test="$focus_rad_btn_name != ''"><xsl:attribute name="onfocus">javascript:<xsl:value-of select="$frm"/>.<xsl:value-of select="$focus_rad_btn_name"/>.click()</xsl:attribute></xsl:if>
		<xsl:if test="not($caching_function = '')"><xsl:attribute name="onblur"><xsl:copy-of select="$caching_function"/></xsl:attribute></xsl:if>
	</xsl:element>					
	<xsl:if test="$show_label = 'Y'"><xsl:text>&#160;</xsl:text><xsl:value-of select="$lab_hh_mm"/></xsl:if>
</xsl:template>
</xsl:stylesheet>
