<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="wb_ui_desc">
		<xsl:param name="text"/>
		<xsl:param name="text_class">Desc</xsl:param>
		<xsl:param name="width" select="$wb_gen_table_width"/>
		<xsl:param name="extra_td"/>
		<xsl:param name="new_template">false</xsl:param>
		<xsl:choose>
			<xsl:when test="$new_template = 'true'">
				<div class="work_input_desc">
					<xsl:copy-of select="$text"/>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<table class="wzb-ui-desc" >
					<tr>
						<td >
							<div class="wzb-ui-module-text">
								<xsl:copy-of select="$text"/>
							</div>
						</td>
						<xsl:copy-of select="$extra_td"/>
					</tr>
				</table>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
