<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/wb_applet_color.xsl"/>

	<xsl:output  indent="yes"/>

	<!-- =============================================================== -->
	<xsl:template match="profession">
		<html>
		<head>
			<TITLE><xsl:value-of select="$wb_wizbank"/></TITLE>
			
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<script language="JavaScript" src="{$wb_js_path}wb_pfs.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
pfs = new wbpfs();

function init() {
	document.getElementById("content").src = pfs.pfs_top_nav_url();
}
]]></SCRIPT>
		</head>
		<frameset cols="{$wb_gen_table_width}" frameborder="0" onload="init()">
			<frame src="" frameborder="0" id="content"/>
		</frameset>
	</html>
	</xsl:template>
	<xsl:template match="env"/>
	<xsl:template match="cur_usr"/>
	<xsl:template match="header"/>
</xsl:stylesheet>
