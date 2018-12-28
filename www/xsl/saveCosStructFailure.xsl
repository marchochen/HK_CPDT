<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/><xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes" />
	<!--================================================= -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!--================================================= -->
	<xsl:template match="user">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
function saveCourseStructFailure() {
	parent.saveCourseStructFailure();
}
]]>
</script>
</head>
<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="saveCourseStructFailure()">
</body>
	</xsl:template>
	<!--================================================= -->
</xsl:stylesheet>
