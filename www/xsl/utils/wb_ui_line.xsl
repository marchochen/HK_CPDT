<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="wb_ui_line">
		<xsl:param name="width">
			<xsl:text>100%</xsl:text>
		</xsl:param>
		<xsl:param name="bg_class">
			<xsl:text>wzb-ui-line</xsl:text>
		</xsl:param>
		<xsl:param name="height">
			<xsl:text>1</xsl:text>
		</xsl:param>
		<xsl:param name="margin">
			<xsl:text>10px</xsl:text>
		</xsl:param>
		<div class="{$bg_class}" style="margin-bottom:{$margin};width: {$width}; height: {$height}px;"></div>
	</xsl:template>
</xsl:stylesheet>
