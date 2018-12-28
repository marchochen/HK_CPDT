<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="wb_updown_box">
		<xsl:param name="width"/>
		<xsl:param name="height">100%</xsl:param>
		<xsl:param name="margintop"/>
		<xsl:param name="size"/>
		<xsl:param name="name"/>
		<xsl:param name="frm">document.frmXml</xsl:param>
		<xsl:param name="multiple">true</xsl:param>
		<xsl:param name="onclick"></xsl:param>
		<xsl:param name="up_function">javascript:moveOptionsUp(<xsl:value-of select="$frm"/>.<xsl:value-of select="$name"/>)</xsl:param>
		<xsl:param name="down_function">javascript:moveOptionsDown(<xsl:value-of select="$frm"/>.<xsl:value-of select="$name"/>)</xsl:param>
		<xsl:param name="option_list"/>
		<!-- smallest size cap-->
		<xsl:variable name="_size">
			<xsl:choose>
				<xsl:when test="$size &lt; 2">2</xsl:when>
				<xsl:otherwise><xsl:value-of select="$size"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<table>
			<tr>
				<td rowspan="2">
					<select name="{$name}" class="wzb-select" size="{$_size}">
						<xsl:if test="$width != ''">
							<xsl:attribute name="style">width:<xsl:value-of select="$width"/>px;height:<xsl:value-of select="$height"/>px; margin-top:<xsl:value-of select="$margintop"/>px;</xsl:attribute>
						</xsl:if>
						<xsl:if test="$onclick != ''">
							<xsl:attribute name="onclick"><xsl:value-of select="$onclick"/></xsl:attribute>
						</xsl:if>						
						<xsl:if test="$multiple = 'true'">
							<xsl:attribute name="multiple">multiple</xsl:attribute>
						</xsl:if>
						<xsl:copy-of select="$option_list"/>
					</select>
				</td>
				<td valign="top">
					<a href="{$up_function}" class="wzb-icon-title-up"></a>
				</td>
			</tr>
			<tr><td valign="bottom" style="padding-right:20px;" ><a   href="{$down_function}" class="wzb-icon-title-down"></a></td></tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
