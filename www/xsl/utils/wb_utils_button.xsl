<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<!-- =================================================================== -->
<xsl:template name="wb_utils_button">
	<xsl:param name="text"/>
	<xsl:param name="href"/>
	<xsl:param name="class">Text</xsl:param>
	<table border="0" cellspacing="0" cellpadding="0" onmouseover="wbUtilsButtonUp(this)" onmouseout="wbUtilsButtonOut(this)">
		<tr>
			<td style="background-image: url({$wb_img_path}wb_utils_button_left.gif);" width="3">
				&#160;&#160;
			</td>
			<td align="center" height="24" style="background-image: url({$wb_img_path}wb_utils_button_center.gif)">
				<a class="{$class}" href="{$href}" style="padding: 6px;color:#000000;text-decoration: none"  onclick="this.blur();">
					<xsl:value-of select="$text"/>
				</a>
			</td>
			<td style="background-image: url({$wb_img_path}wb_utils_button_right.gif);" width="3">
				&#160;&#160;
			</td>
		</tr>
	</table>
</xsl:template>
<!-- =================================================================== -->
</xsl:stylesheet>
