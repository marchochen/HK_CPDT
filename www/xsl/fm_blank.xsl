<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/><xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes"/>
	<!-- ============================================================= -->
	<xsl:template match="/">
		<html>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_fm.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			
			var isExcludes = getUrlParam('isExcludes');
			fm = new wbFm(isExcludes);
			
			function return_url(){
				if(wb_utils_fm_get_cookie("rsv_return_url")!="" && wb_utils_fm_get_cookie("url_success")=="")
					if(parent.opener)
						parent.opener.location.reload();
			}
			
			]]>
		</SCRIPT>
		</head>
		<body onunload="fm.flush_cart();return_url();">
		</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
