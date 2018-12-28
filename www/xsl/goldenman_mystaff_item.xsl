<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:template match="/">
		<!-- itm_id_lst -->
		<xsl:call-template name="wb_goldenman">
			<xsl:with-param name="frm">formRpt</xsl:with-param>
			<xsl:with-param name="field_name">itm_id_lst</xsl:with-param>
			<xsl:with-param name="custom_js_code">course_change(formRpt.course_sel_type[1]);formRpt.course_sel_type[1].checked = true;</xsl:with-param>
			<xsl:with-param name="name">itm_id_lst</xsl:with-param>
			<xsl:with-param name="box_size">4</xsl:with-param>
			<xsl:with-param name="tree_type">item</xsl:with-param>
			<xsl:with-param name="select_type">3</xsl:with-param>
			<xsl:with-param name="args_type">row</xsl:with-param>
			<xsl:with-param name="complusory_tree">0</xsl:with-param>
			<xsl:with-param name="pick_root">0</xsl:with-param>
			<xsl:with-param name="parent_tcr_id">parent_tcr_id</xsl:with-param>
			<xsl:with-param name="custom_js_code_extra">args = goldenman.set_global_catalog_label(args,Wzb.wb_lan)</xsl:with-param>																								
		</xsl:call-template>
	</xsl:template>
</xsl:stylesheet>
