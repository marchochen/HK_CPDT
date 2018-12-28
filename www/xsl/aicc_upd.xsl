<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="share/aicc_action_init_share.xsl"/>
	<xsl:variable name="res_id" select="/resource/@id"/>
	<xsl:variable name="page_title">
		<xsl:value-of select="//body/title"/>
	</xsl:variable>
	<xsl:output indent="yes"/>
	<xsl:template match="/">
		<html>
			<xsl:call-template name="aicc_action">
				<xsl:with-param name="mode">UPD</xsl:with-param>
				<xsl:with-param name="save_function">javascript:aicc.sendFrm(window.document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
				<xsl:with-param name="cancel_function">javascript:history.back()</xsl:with-param>
				<!-- url_success/url_failure: Firefox 不可以使用 location.reload()，因为在Firefox，
				     parent.location.reload()会触发子frame的reload()。如果该子frame是一个form post的结果，
				      浏览器会提示一般刷新form post的警告。
				-->
				<xsl:with-param name="url_success">javascript:aicc.upd_prep(<xsl:value-of select="$res_id"/>);</xsl:with-param>
				<xsl:with-param name="url_failure">javascript:aicc.upd_prep(<xsl:value-of select="$res_id"/>);</xsl:with-param>
				<xsl:with-param name="header">YES</xsl:with-param>
				<xsl:with-param name="width" select="$wb_frame_table_width"/>
				<xsl:with-param name="page_title">
					<xsl:value-of select="$page_title"/>
				</xsl:with-param>
			</xsl:call-template>
		</html>
	</xsl:template>
</xsl:stylesheet>
