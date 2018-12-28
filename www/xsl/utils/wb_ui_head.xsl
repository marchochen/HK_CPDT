<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="wb_ui_head">
		<xsl:param name="text"/>
		<xsl:param name="text_class">Head</xsl:param>
		<xsl:param name="bg_class"></xsl:param>
		<xsl:param name="extra_td"/>
		<xsl:param name="width" select="$wb_gen_table_width"/>
		<xsl:param name="new_template">false</xsl:param>
		<xsl:param name="title_style"/>
		<xsl:param name="table_style">border-bottom: 1px solid #ddd;</xsl:param>
		
		
		<xsl:choose>
			<xsl:when test="$new_template = 'true'">
				<div class="ui_head">
					<xsl:choose>
						<xsl:when test="$text = ''"><img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/></xsl:when>
						<xsl:otherwise><span class="report_outline"><xsl:copy-of select="$text"/></span></xsl:otherwise>
					</xsl:choose>
				</div>
			</xsl:when>
			<xsl:otherwise>

				<table  style="{$table_style}" cellspacing="0" width="{$width}" border="0" cellpadding="0" >
					<xsl:if test="$bg_class != ''">
						<xsl:attribute name="class"><xsl:value-of select="$bg_class"/></xsl:attribute>
					</xsl:if>
					<tr height="20">
						<td valign="bottom" class="wzb-ui-head-left text-left" style="padding:10px;">
							<xsl:choose>
								<xsl:when test="$text = ''"><img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/></xsl:when>
								<xsl:otherwise><div class="wzb-before" style="{$title_style}"><xsl:copy-of select="$text"/></div></xsl:otherwise>
							</xsl:choose>
						</td>
						<xsl:copy-of select="$extra_td"/>
					</tr>
				</table>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
