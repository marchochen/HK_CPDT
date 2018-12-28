<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:template match="/">
		<!-- usr_lst -->
		
		<xsl:call-template name="wb_goldenman">
			<xsl:with-param name="frm">formRpt</xsl:with-param>
			<xsl:with-param name="field_name">ent_id_lst</xsl:with-param>
			<xsl:with-param name="name">ent_id_lst</xsl:with-param>
			<xsl:with-param name="box_size">4</xsl:with-param>
			<xsl:with-param name="tree_type">user_group_and_user</xsl:with-param>
			<xsl:with-param name="select_type">1</xsl:with-param>
			<xsl:with-param name="pick_leave">0</xsl:with-param>
			<xsl:with-param name="pick_root">0</xsl:with-param>
			<xsl:with-param name="get_supervise_group">1</xsl:with-param>
			<xsl:with-param name="get_direct_supervise">1</xsl:with-param>
			<xsl:with-param name="search">true</xsl:with-param>
			<xsl:with-param name="search_function">javascript:mystaff.popup_search_prep();</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
</xsl:stylesheet>
