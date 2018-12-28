<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="resource_body_share.xsl"/>
	<xsl:import href="question_body_share.xsl"/>
	<xsl:import href="res_get_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->	
	<xsl:template name="res_get">
		<xsl:param name="preview_function"/>
		<xsl:param name="upd_function"/>
		<xsl:param name="del_function"/>
		<xsl:param name="cp_function"/>
		<xsl:param name="sc_add_que_function"/>
		<xsl:param name="header"/>
		<xsl:param name="body_width"/>
		<xsl:param name="introduction">true</xsl:param>
		<xsl:variable name="id_que" select="@id"/>
		<xsl:variable name="id_obj" select="header/objective/@id"/>
		<xsl:variable name="timestamp" select="@timestamp"/>
		<xsl:call-template name="html_header"/>
		<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0" onload="gen_set_cookie('url_success',self.location.href)">
			<div style="margin:10px">
				<xsl:call-template name="additional_information">
					<xsl:with-param name="timestamp"><xsl:value-of select="$timestamp"/></xsl:with-param>
					<xsl:with-param name="id_que"><xsl:value-of select="$id_que"/></xsl:with-param>
					<xsl:with-param name="width"><xsl:value-of select="$body_width"/></xsl:with-param>
					<xsl:with-param name="preview_function"><xsl:value-of select="$preview_function"/></xsl:with-param>
					<xsl:with-param name="upd_function"><xsl:value-of select="$upd_function"/></xsl:with-param>
					<xsl:with-param name="del_function"><xsl:value-of select="$del_function"/></xsl:with-param>
					<xsl:with-param name="cp_function"><xsl:value-of select="$cp_function"/></xsl:with-param>
					<xsl:with-param name="sc_add_que_function" select="$sc_add_que_function"></xsl:with-param>
					<xsl:with-param name="header" select="$header"></xsl:with-param>
				</xsl:call-template>
				<xsl:if test="$header ='YES' ">
					<xsl:call-template name="draw_header"/>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="/question">
						<xsl:call-template name="question_body">
						    <xsl:with-param name="introduction" select="$introduction"/>
							<xsl:with-param name="width" select="$body_width"/>
							<xsl:with-param name="que_id" select="$id_que"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="resource_body">
							<xsl:with-param name="width" select="$body_width"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</div>
		</body>
	</xsl:template>
</xsl:stylesheet>
