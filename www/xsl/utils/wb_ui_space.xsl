<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="wb_ui_space">
		<xsl:param name="height">5</xsl:param>
		<xsl:param name="width" select="$wb_gen_table_width"/>
		<table cellspacing="0" cellpadding="0" border="0" width="{$width}">
			<tr >
				<td><img width="1" src="{$wb_img_path}tp.gif" height="{$height}" border="0"/></td>
			</tr>
		</table>	
	</xsl:template>
</xsl:stylesheet>
