<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../utils/wb_css.xsl"/>
	<xsl:import href="../utils/escape_backslash.xsl"/>
	<xsl:import href="../share/aicc_action_share.xsl"/>
	<!-- ===============================================-->	
	<xsl:template name="aicc_action">
		<xsl:param name="mode"/>
		<xsl:param name="save_function"/>
		<xsl:param name="cancel_function"/>
		<xsl:param name="width"/>
		<xsl:param name="header"/>
		<xsl:param name="url_success"/>
		<xsl:param name="url_failure"/>
		<xsl:call-template name="aicc_html_header">
			<xsl:with-param name="mode">
				<xsl:value-of select="$mode"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:variable name="src">
			<xsl:call-template name="escape_backslash">
				<xsl:with-param name="my_right_value">
					<xsl:value-of select="/resource/body/source"/>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0" onunload="wb_utils_close_preloading()" onLoad="document.frmXml.res_src_link.value='{$src}';document.frmXml.res_src_type.value='AICC_FILES';init();">
			<form name="frmXml" enctype="multipart/form-data">
				<input type="hidden" name="rename" value="no"/>
				<xsl:call-template name="aicc_body">
					<xsl:with-param name="width"  select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="url_success" select="$url_success"/>
					<xsl:with-param name="url_failure" select="$url_failure"/>
					<xsl:with-param name="src" select="$src" />
				</xsl:call-template>
				<xsl:call-template name="wb_ui_space">
					<xsl:with-param name="width" select="$width"/>
				</xsl:call-template>
				<xsl:call-template name="aicc_additional_information">
					<xsl:with-param name="width"  select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="url_success" select="$url_success"/>
					<xsl:with-param name="save_function" select="$save_function"/>
					<xsl:with-param name="cancel_function" select="$cancel_function"/>
					<xsl:with-param name="url_failure" select="$url_failure"/>
					<!-- <xsl:with-param name="page_title" select="$page_title"/> -->
					<xsl:with-param name="header">YES</xsl:with-param>
				</xsl:call-template>
			</form>
		</body>
	</xsl:template>
	<!-- ===============================================-->	
</xsl:stylesheet>
