<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<!--
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="share/wb_layout_share.xsl"/>
	<xsl:import href="share/test_player_share.xsl"/>
	-->
	<xsl:import href="utils/wb_init_lab.xsl"/>
    <xsl:import href="cust/wb_cust_const.xsl"/>
    <xsl:import href="utils/escape_doub_quo.xsl"/>
    <xsl:import href="utils/wb_gen_button.xsl"/>
    <xsl:import href="utils/wb_css.xsl"/>
    <xsl:import href="utils/unescape_html_linefeed.xsl"/>
    <xsl:import href="share/test_player_share.xsl"/>
 	<xsl:import href="share/wb_object_share.xsl"/>
    
	<xsl:variable name="quiz" select="/quiz"/>
	<xsl:variable name="swf">../htm/template/tst_player3.swf</xsl:variable>
	<!-- =============================================================== -->
	<xsl:output indent="yes"/>
	<xsl:template match="/">
		<html>
			<xsl:call-template name="show_test_player"/>
		</html>
	</xsl:template>
</xsl:stylesheet>
