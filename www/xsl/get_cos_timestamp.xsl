<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output  indent="yes"/>
	<xsl:variable name="course_name" select="/course/header/title"/>
	<!-- =wizBank V3 start============================================== -->
	<xsl:template match="/">
		<html>
			<head>
				<TITLE>
					<xsl:value-of select="$wb_wizbank"/>
				</TITLE>
				<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">
				<![CDATA[
				function set_cos_timestamp() {
					parent.set_cos_timestamp(']]><xsl:value-of select="/course/@timestamp"/><![CDATA[');
				}
				]]>
				</SCRIPT>
			</head>
			<body onload="set_cos_timestamp()"/>
		</html>
	</xsl:template>
	<xsl:template match="course"/>
</xsl:stylesheet>
