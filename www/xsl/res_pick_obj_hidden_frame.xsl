<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>	
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- =============================================================== -->
<xsl:variable name="tpl_type" select="/template_list/type"/>		
<xsl:variable name="tpl_subtype" select="/template_list/subtype"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<xsl:template match="template_list">
		<head>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>		
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_objective.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript"><![CDATA[
				var obj = new wbObjective
				function init(){
					document.frmXml.res_type.value = getParentUrlParam("res_type")
				}
]]></script>
		</head>
		<body bgcolor="#eeeeee" onload="init();parent.load_frame()">
		
		<form name="frmXml">
		<table><tr><td>
			tpl_type:<input type="text" name="tpl_type" value="{$tpl_type}" style="width:50px;"/>
			mod_subtype:<input type="text" name="mod_subtype" value="{$tpl_type}" style="width:50px;"/>
			res_type:<input type="text" name="res_type" value="" style="width:50px;"/>
			obj_title: <input type="text" name="obj_title" value="" style="width:50px;"/>
			</td></tr></table>
		</form>	
		</body>
	</xsl:template>	
</xsl:stylesheet>
