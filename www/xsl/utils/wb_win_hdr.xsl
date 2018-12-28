<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template name="wb_win_hdr">
<xsl:param name="navigation"></xsl:param>
<xsl:param name="width" select="$wb_gen_table_width"/>
<table cellpadding="0" cellspacing="0" border="0" width="{$width}" background="{$wb_img_path}navigate_bg.gif">
	<tr width="{$width}" valign="middle">
		<td width="{$width}" valign="bottom">
			<xsl:text>&#160;</xsl:text>
			<xsl:copy-of select="$navigation"/>
	   </td>
	</tr>
</table>
</xsl:template>
</xsl:stylesheet>
