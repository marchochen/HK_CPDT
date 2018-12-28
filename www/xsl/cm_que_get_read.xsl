<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/res_get_init_share.xsl"/>
	<xsl:output  indent="yes"/>
	<xsl:template match="/">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<xsl:call-template name="new_css"/>
			</head>
			<xsl:apply-templates mode="question"/>
		</html>
	</xsl:template>
	<xsl:template match="question" mode="question">
		<xsl:call-template name="res_get">
			<xsl:with-param name="header">NO</xsl:with-param>
			<xsl:with-param name="body_width" select="$wb_frame_table_width"/>
		</xsl:call-template>
	</xsl:template>
</xsl:stylesheet>
