<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!-- Pagination -->
<xsl:template name="wb_sortorder_cursor">
	<xsl:param name="img_path"/>
	<xsl:param name="sort_order"/>
	<xsl:choose>
		<xsl:when test="$sort_order = 'asc' or $sort_order = 'ASC'">
<!-- 			<img src="{$img_path}tp_arrow_asc.gif" border="0" align="bottom"/> -->
			<i class="fa skin-color fa-caret-up" style="margin-left:3px;"></i>
		</xsl:when>
		<xsl:when test="$sort_order = 'desc' or $sort_order = 'DESC'">
<!-- 			<img src="{$img_path}tp_arrow_desc.gif" border="0" align="bottom"/> -->
			<i class="fa fa-caret-down skin-color" style="margin-left:3px;"></i>
		</xsl:when>
	</xsl:choose>
</xsl:template>
</xsl:stylesheet>
