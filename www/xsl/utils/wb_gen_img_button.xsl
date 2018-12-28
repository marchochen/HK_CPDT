<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!-- Image rollover  -->
<xsl:template name="wb_gen_img_button">
	<xsl:param name="wb_gen_btn_a_name"/>
	<xsl:param name="wb_gen_btn_name"/>
	<!-- image name-->
	<xsl:param name="wb_gen_btn_href"/>
	<!-- image refrence-->
	<xsl:param name="wb_gen_btn_multi"/>
	<!-- image unique id-->
	<xsl:param name="wb_gen_btn_target"/>
	<!-- image target-->
	<xsl:param name="wb_gen_btn_relative_path"/>
	<!-- image relative path -->
	<xsl:param name="wb_gen_btn_hspace"/>
	<!-- attribute hspace-->
	<xsl:param name="wb_gen_btn_vspace"/>
	<!-- attribute hspace-->
	<xsl:param name="wb_gen_btn_align"/>
	<xsl:param name="wb_gen_btn_alt"/>
	<xsl:param name="wb_gen_btn_rollover">true</xsl:param>
	<xsl:variable name="con_name" select="concat($wb_gen_btn_name,$wb_gen_btn_multi)"/>
	<xsl:variable name="con_path" select="concat($wb_img_path,$wb_gen_btn_relative_path,$wb_gen_btn_name)"/>
	<xsl:if test="$wb_gen_btn_rollover = 'true'">
		<script language="JavaScript" type="text/javascript">
			<xsl:value-of select="$con_name"/><![CDATA[on = new Image;]]><xsl:value-of select="$con_name"/><![CDATA[on.src = "]]><xsl:value-of select="$con_path"/><![CDATA[_on.gif"; ]]><xsl:value-of select="$con_name"/><![CDATA[off = new Image;]]><xsl:value-of select="$con_name"/><![CDATA[off.src = "]]><xsl:value-of select="$con_path"/><![CDATA[_off.gif";]]>
		</script>
	</xsl:if>
	<a href="{$wb_gen_btn_href}">
		<xsl:if test="$wb_gen_btn_rollover = 'true'">
			<xsl:attribute name="onMouseOver">gen_img_act('<xsl:value-of select="$wb_gen_btn_name"/><xsl:value-of select="$wb_gen_btn_multi"/>')</xsl:attribute>
			<xsl:attribute name="onMouseOut">gen_img_inact('<xsl:value-of select="$wb_gen_btn_name"/><xsl:value-of select="$wb_gen_btn_multi"/>')</xsl:attribute>
		</xsl:if>
		<xsl:if test="$wb_gen_btn_target"><xsl:attribute name="target"><xsl:value-of select="$wb_gen_btn_target"/></xsl:attribute></xsl:if>
		<xsl:if test="$wb_gen_btn_a_name"><xsl:attribute name="name"><xsl:value-of select="$wb_gen_btn_a_name"/></xsl:attribute></xsl:if>
		<xsl:if test="$wb_gen_btn_alt"><xsl:attribute name="title"><xsl:value-of select="$wb_gen_btn_alt"/></xsl:attribute></xsl:if>
		<img border="0" name="{$wb_gen_btn_name}{$wb_gen_btn_multi}">
			<xsl:choose>
				<xsl:when test="$wb_gen_btn_rollover = 'true'"><xsl:attribute name="src"><xsl:value-of select="$con_path"/>_off.gif</xsl:attribute></xsl:when>
				<xsl:otherwise><xsl:attribute name="src"><xsl:value-of select="$con_path"/>.gif</xsl:attribute></xsl:otherwise>
			</xsl:choose>
			<xsl:if test="$wb_gen_btn_hspace"><xsl:attribute name="hspace"><xsl:value-of select="$wb_gen_btn_hspace"/></xsl:attribute></xsl:if>
			<xsl:if test="$wb_gen_btn_vspace"><xsl:attribute name="vspace"><xsl:value-of select="$wb_gen_btn_vspace"/></xsl:attribute></xsl:if>
			<xsl:if test="$wb_gen_btn_align"><xsl:attribute name="align"><xsl:value-of select="$wb_gen_btn_align"/></xsl:attribute></xsl:if>
			<xsl:if test="$wb_gen_btn_alt"><xsl:attribute name="alt"><xsl:value-of select="$wb_gen_btn_alt"/></xsl:attribute></xsl:if>
		</img>
	</a>
</xsl:template>
</xsl:stylesheet>
