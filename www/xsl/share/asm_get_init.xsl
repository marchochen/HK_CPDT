<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="../share/asm_body.xsl"/>
	<xsl:import href="../share/asm_export_body.xsl"/>
	<xsl:import href="../utils/wb_ui_footer.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:template name="asm_get">
		<xsl:param name="upd_function"/>
		<xsl:param name="del_function"/>
		<xsl:param name="edit_q_function"/>
		<xsl:param name="preview_function"/>
		<xsl:param name="header"/>
		<xsl:param name="body_width"/>
		<xsl:param name="type"/>
		<xsl:param name="mode"/>
		<xsl:variable name="id_que" select="@id"/>
		<xsl:variable name="id_obj" select="header/objective/@id"/>
		<xsl:variable name="timestamp" select="@timestamp"/>
		<xsl:call-template name="html_header"/>
		<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0" onload="gen_set_cookie('url_success',self.location.href)">

			<xsl:call-template name="additional_information">
				<xsl:with-param name="header"><xsl:value-of select="$header"/></xsl:with-param>
				<xsl:with-param name="timestamp">
					<xsl:value-of select="$timestamp"/>
				</xsl:with-param>
				<xsl:with-param name="id_que">
					<xsl:value-of select="$id_que"/>
				</xsl:with-param>
				<xsl:with-param name="width">
					<xsl:value-of select="$body_width"/>
				</xsl:with-param>
				<xsl:with-param name="upd_function">
					<xsl:value-of select="$upd_function"/>
				</xsl:with-param>
				<xsl:with-param name="preview_function">
					<xsl:value-of select="$preview_function"/>
				</xsl:with-param>
				<xsl:with-param name="edit_q_function">
					<xsl:value-of select="$edit_q_function"/>
				</xsl:with-param>
				<xsl:with-param name="del_function">
					<xsl:value-of select="$del_function"/>
				</xsl:with-param>
				<xsl:with-param name="type">
					<xsl:value-of select="$type"/>
				</xsl:with-param>
				<xsl:with-param name="mode" select="$mode"/>
			</xsl:call-template>
			<xsl:call-template name="resource_body">
				<xsl:with-param name="width">
					<xsl:value-of select="$body_width"/>
				</xsl:with-param>
			</xsl:call-template>
			<xsl:if test="$upd_function != ''">
				<xsl:call-template name="draw_export_body">
					<xsl:with-param name="width">
						<xsl:value-of select="$body_width"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:if>
		</body>
	</xsl:template>
</xsl:stylesheet>
