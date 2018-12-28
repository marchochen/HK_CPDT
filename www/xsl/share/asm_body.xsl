<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="../utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="../utils/wb_ui_head.xsl"/>
	<xsl:import href="../utils/wb_ui_line.xsl"/>
	<xsl:template name="resource_body">
		<xsl:param name="width"/>
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:apply-templates select="body" mode="res">
					<xsl:with-param name="width">
						<xsl:value-of select="$width"/>
					</xsl:with-param>
					<xsl:with-param name="lab_annotation">註釋</xsl:with-param>
					<xsl:with-param name="lab_desc">簡介</xsl:with-param>
					<xsl:with-param name="lab_no_desc">沒有簡介</xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="$wb_lang='gb'">
				<xsl:apply-templates select="body" mode="res">
					<xsl:with-param name="width">
						<xsl:value-of select="$width"/>
					</xsl:with-param>
					<xsl:with-param name="lab_annotation">注释</xsl:with-param>
					<xsl:with-param name="lab_desc">简介</xsl:with-param>
					<xsl:with-param name="lab_no_desc">没有简介</xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="body" mode="res">
					<xsl:with-param name="width">
						<xsl:value-of select="$width"/>
					</xsl:with-param>
					<xsl:with-param name="lab_annotation">Annotation</xsl:with-param>
					<xsl:with-param name="lab_desc">Description</xsl:with-param>
					<xsl:with-param name="lab_no_desc">No description</xsl:with-param>
				</xsl:apply-templates>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="body" mode="res">
		<xsl:param name="width"/>
		<xsl:param name="lab_annotation"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_no_desc"/>
		<!-- ==========Resourece Desc================= -->
		<xsl:if test="desc != ''">
			<!-- ParInf Header Bar-->
			<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="text">
					<xsl:value-of select="$lab_desc"/>
				</xsl:with-param>
				<xsl:with-param name="width" select="$width"/>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_line">
				<xsl:with-param name="width" select="$width"/>
			</xsl:call-template>
			<!-- Parinf Content-->
			<table width="{$width}" cellspacing="0" cellpadding="3" border="0" class="Bg">
				<tr>
					<td>
						<span class="Text">
							<xsl:if test="desc = ''">
								<xsl:value-of select="$lab_no_desc"/>
							</xsl:if>
							<xsl:call-template name="unescape_html_linefeed">
								<xsl:with-param name="my_right_value">
									<xsl:value-of select="desc"/>
								</xsl:with-param>
							</xsl:call-template>
						</span>
					</td>
				</tr>
				<tr>
					<td>
						<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
					</td>
				</tr>
			</table>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
