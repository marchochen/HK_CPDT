<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	
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
			<script language="JavaScript" src="{$wb_js_path}wb_ugr.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>

			<script language="JavaScript" type="text/javascript"><![CDATA[
function save_ugr_success() {
	parent.frames[1].save_ugr_success();
}
]]></script>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="save_ugr_success()"/>
	</xsl:template>
	<!--================================================= -->
</xsl:stylesheet>
