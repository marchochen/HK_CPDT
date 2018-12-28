<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>	
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- =============================================================== -->
	<xsl:variable name="module_id" select="/*/@id"/>
	<xsl:variable name="course_id" select="/*/header/@course_id"/>
	<xsl:variable name="course_title" select="/*/header/@course_title"/>
	<xsl:variable name="module_title" select="/*/header/title"/>	
	<xsl:variable name="module_type" select="/*/header/@type"/>	
	<xsl:variable name="module_subtype" select="/*/header/@subtype"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<xsl:template match="module | fixed_assessment">
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
				
]]></script>
		</head>
		<body bgcolor="#eeeeee" onload="parent.load_frame()">
		
		<form name="frmXml">
		<table><tr><td>
			mod_id:<input type="text" name="mod_id" value="{$module_id}" style="width:50px;"/>
			mod_title:<input type="text" name="mod_title" value="{$module_title}" style="width:100px;"/>
			cos_id:<input type="text" name="cos_id" value="{$course_id}" style="width:50px;"/>
			cos_title:<input type="text" name="cos_title" value="{$course_title}" style="width:100px;"/>
			mod_type:<input type="text" name="mod_type" value="{$module_type}" style="width:50px;"/>
			mod_subtype:<input type="text" name="mod_subtype" value="{$module_subtype}" style="width:50px;"/>
			obj_title:<input type="text" name="obj_title" value="" style="width:100px;"/>
			</td></tr></table>
		</form>	
		</body>
	</xsl:template>	
</xsl:stylesheet>
