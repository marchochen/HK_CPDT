<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
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
			<meta http-equiv="Content-Type" content="text/html; charset={@encoding}"/>
			<script language="JavaScript" src="{$wb_js_path}wb_ugr.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>

			<script language="JavaScript" type="text/javascript"><![CDATA[
				URL_FRM_ACTION = wb_utils_servlet_url
				FLD_CMD = "ugr_order_upd";
				FLD_ENV = "wizb";
				FLD_URL_SUCCESS = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','ugr_order_upd_success.xsl');
				FLD_URL_FAILURE = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','ugr_order_upd_failure.xsl');
function save_ugr_order() {
	frm = document.frmSaveToc;
	frm.ugr_order.value=parent.ugr_order;
	submitFrm();
}

function submitFrm() {
	frm = document.frmSaveToc;
	
	// populate hidden form data
	frm.action = URL_FRM_ACTION;
	frm.env.value=FLD_ENV;
	frm.cmd.value=FLD_CMD;
	frm.url_failure.value=FLD_URL_FAILURE;
	frm.url_success.value=FLD_URL_SUCCESS;
	frm.submit();
}]]></script>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="save_ugr_order()">
			<form name="frmSaveToc" method="post" onSubmit="submitFrm()">
				<input type="hidden" name="env"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="ugr_order"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
			</form>
		</body>
	</xsl:template>
	<!--================================================= -->
</xsl:stylesheet>
