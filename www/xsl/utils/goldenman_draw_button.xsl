<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:import href="wb_gen_button.xsl"/>

<xsl:template name="goldenman_draw_button">
	<xsl:param name="button_list"/>
	<xsl:param name="button_list_img"/>
	<xsl:param name="idx">1</xsl:param>
	<xsl:if test="not($button_list='')">
		<xsl:choose>
			<xsl:when test="substring-before($button_list,'~')=''">
				<img src="{$wb_img_path}tp.gif" width="2" height="1" border="0"/>
				<xsl:choose>
					<xsl:when test="$button_list_img=''">
						<input type="button" value="{$button_list}" onclick="btn_{$idx}_ftn()" class="Btn"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$button_list_img"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">Javascript:btn_<xsl:value-of select="$idx"/>_ftn()</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<img src="{$wb_img_path}tp.gif" width="2" height="1" border="0"/>
				<xsl:choose>
					<xsl:when test="substring-before($button_list_img,'~')=''">
						<input type="button" value="{substring-before($button_list,'~')}" onclick="btn_{$idx}_ftn()" class="Btn"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="substring-before($button_list_img,'~')"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">Javascript:btn_<xsl:value-of select="$idx"/>_ftn()</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:call-template name="goldenman_draw_button">
					<xsl:with-param name="button_list"><xsl:value-of select="substring-after($button_list,'~')"/></xsl:with-param>
					<xsl:with-param name="button_list_img"><xsl:value-of select="substring-after($button_list_img,'~')"/></xsl:with-param>
					<xsl:with-param name="idx"><xsl:value-of select="number($idx)+1"/></xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:if>
</xsl:template>
</xsl:stylesheet>
