<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="wb_ui_line.xsl"/>
<xsl:template name="wb_ui_show_no_item">
	<xsl:param name="top_line">false</xsl:param>
	<xsl:param name="bottom_line">false</xsl:param>
	<xsl:param name="width" select="$wb_gen_table_width"/>
	<xsl:param name="text"/>
	<xsl:param name="text_class">Text</xsl:param>
	<xsl:param name="bg_class">RowsEven</xsl:param>
	<xsl:param name="new_template">true</xsl:param>
	<xsl:choose>
		<xsl:when test="$new_template = 'true'">
			<xsl:if test="$top_line = 'true'">
				<xsl:call-template name="wb_ui_line"/>
			</xsl:if>	
			<div class="losedata" style="margin-top:10px">
				<i class="fa fa-folder-open-o"></i>
				<p><xsl:value-of select="$text"/></p>
			</div>
			<xsl:if test="$top_line = 'true'">
				<xsl:call-template name="wb_ui_line"/>
			</xsl:if>	
		</xsl:when>
		<xsl:otherwise>
			<xsl:if test="$top_line = 'true'">
				<xsl:call-template name="wb_ui_line"/>
			</xsl:if>		
			<table>
				<tr>
					<td colspan="3"></td>
				</tr>
				<tr class="{$bg_class}" valign="middle">
					<td width="8"></td>
					<td align="center"><span class="{$text_class}"><xsl:copy-of select="$text"/></span></td>
					<td width="8"></td>
				</tr>
				<tr class="{$bg_class}" valign="middle">
					<td colspan="3"></td>
				</tr>
			</table>
			<xsl:if test="$bottom_line = 'true'">
				<xsl:call-template name="wb_ui_line"/>
			</xsl:if>		
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>
</xsl:stylesheet>
