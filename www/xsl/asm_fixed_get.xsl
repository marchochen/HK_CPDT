<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/wb_layout_share.xsl"/>
	<xsl:import href="share/asm_get_init.xsl"/>
	<xsl:import href="share/asm_get_utils.xsl"/>
	<xsl:variable name="root_tag" select="/fixed_assessment"/>
	<xsl:variable name="wb_gen_table_width" select="$wb_frame_table_width"/>
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<xsl:template match="fixed_assessment ">
		<xsl:variable name="preview_function">
			<xsl:choose>
				<xsl:when test="header/objective/@type != 'SYS'">javascript:asm.preview('<xsl:value-of select="@id"/>')</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="upd_function">
			<xsl:choose>
				<xsl:when test="header/objective/@type != 'SYS'">javascript:asm.upd_prep_no_failure('<xsl:value-of select="@id"/>','<xsl:value-of select="header/@subtype"/>')</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="edit_q_function">
			<xsl:choose>
				<xsl:when test="header/objective/@type != 'SYS'">javascript:wb_utils_set_cookie('res_subtype','<xsl:value-of select="header/@subtype"/>');wb_utils_set_cookie('res_id','<xsl:value-of select="@id"/>');asm.asm_q('<xsl:value-of select="@id"/>','<xsl:value-of select="header/@subtype"/>')</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="del_function">
			<xsl:choose>
				<xsl:when test="header/objective/@type != 'SYS'">javascript:res.del('<xsl:value-of select="@id"/>',<xsl:value-of select="header/objective/@id"/>,'<xsl:value-of select="$wb_lang"/>')</xsl:when>
				<xsl:otherwise>javascript:res.perm_del('<xsl:value-of select="@id"/>','<xsl:value-of select="$wb_lang"/>')</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:call-template name="asm_get">
			<xsl:with-param name="upd_function">
				<xsl:value-of select="$upd_function"/>
			</xsl:with-param>
			<xsl:with-param name="edit_q_function">
				<xsl:value-of select="$edit_q_function"/>
			</xsl:with-param>
			<xsl:with-param name="del_function">
				<xsl:value-of select="$del_function"/>
			</xsl:with-param>
			<xsl:with-param name="header">YES</xsl:with-param>
			<xsl:with-param name="body_width" select="$wb_gen_table_width"/>
			<xsl:with-param name="type">FAS</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
</xsl:stylesheet>
