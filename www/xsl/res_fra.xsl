<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="que_obj_id_ass" select="/objective_list/header/node[@type='ASS']/@id "/>
	<xsl:variable name="que_obj_id" select="/objective_list/header/node[position() = last()]/@id "/>
	<xsl:variable name="obj_id" select="/objective_list/objective/@id"/>
	<xsl:variable name="syb_id" select="/objective_list/objective/syllabus/@id"/>
	<xsl:variable name="choice" select="/objective_list/folders/text()" />
	<xsl:variable name="show_all" select="/objective_list/meta/show_all"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="objective_list"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="objective_list">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_objective.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript">
			obj = new wbObjective;
			location.href = obj.get_trash_obj_lst_hdr_url('<xsl:value-of select="$obj_id"/>','<xsl:value-of select="$que_obj_id_ass"/>','<xsl:value-of select="$choice" />','<xsl:value-of select="$show_all"/>');
		</script>
		</head>
		<!--  
		<frameset cols="*,{$wb_gen_table_width},*" frameborder="0" onload="init()">
			<frame src="../htm/empty.htm" scrolling="no" noresize="noresize"/>
			<frame src="" id="nav" name="nav" frameborder="0"/>
			<frame src="../htm/empty.htm" scrolling="no" noresize="noresize"/>
		</frameset>-->
	</xsl:template>
</xsl:stylesheet>
