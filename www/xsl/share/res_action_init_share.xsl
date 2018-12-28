<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../utils/escape_backslash.xsl"/>
	<!-- =============================================================== -->
	<xsl:template name="res_action">
		<xsl:param name="mode"/>
		<xsl:param name="save_function"/>
		<xsl:param name="cancel_function"/>
		<xsl:param name="width"/>
		<xsl:param name="header"/>
		<xsl:param name="url_success"/>
		<xsl:param name="url_failure"/>
		<xsl:variable name="src">
			<xsl:call-template name="escape_backslash">
				<xsl:with-param name="my_right_value">
					<xsl:value-of select="/resource/body/source"/>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<!-- =============================================================== -->
		<xsl:call-template name="html_header">
			<xsl:with-param name="src" select="$src"/>
			<xsl:with-param name="mode" select="$mode"/>
		</xsl:call-template>
		<!-- =============================================================== -->
		<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0" onunload="wb_utils_close_preloading()" onLoad="feeddata();init();">
			<form name="frmXml" enctype="multipart/form-data">
				<xsl:if test="$header ='YES' ">
					<xsl:call-template name="draw_header"/>
				</xsl:if>
				<!-- =============================================================== -->
				<xsl:call-template name="res_body">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="url_success" select="$url_success"/>
					<xsl:with-param name="url_failure" select="$url_failure"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_space">
					<xsl:with-param name="width" select="$width"/>
				</xsl:call-template>
				<!-- =============================================================== -->
				<xsl:call-template name="res_additional_information">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="save_function" select="$save_function"/>
					<xsl:with-param name="cancel_function" select="$cancel_function"/>
				</xsl:call-template>
				<!-- =============================================================== -->
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>
