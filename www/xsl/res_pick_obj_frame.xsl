<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- =============================================================== -->
<xsl:variable name="tpl_type" select="/template_list/type"/>		
<xsl:variable name="tpl_subtype" select="/template_list/subtype"/>
<xsl:variable name="tcr_id" select="/template_list/training_center/@id"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="template_list">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_objective.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_resource.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript"><![CDATA[
				var obj = new wbObjective
				var res = new wbResource
				wb_utils_set_cookie('tcr_id',']]><xsl:value-of select="$tcr_id"/><![CDATA[')
				var tpl_type = ']]><xsl:value-of select="$tpl_type"/><![CDATA['
				
				//Man: To avoid getting "undefined" value at "frame" page, "hidden" page should be loaded before "frame" page load
				//page loading flow should be:
				//1.  frameset -->"hidden" page , loading.htm
				//2.  hidden frame loaded ,hidden value loaded, and call frameset function "load_frame()"
				//3.  frameset -->"hidden" page , "frame" page
				
				function load_frame(){
				   	var url = obj.pick_obj_url(getUrlParam('res_type'),']]><xsl:value-of select="$tcr_id"/><![CDATA[')	
					frame.location.href = url
				}				
]]></script>
		</head>
		<script language="JavaScript">
		<![CDATA[
		//=====
		var debug = false;
		//=====
		
		var str = '';
		wb_standard_width = ]]><xsl:value-of select="$wb_gen_table_width"/><![CDATA[;
		str+='<frameset cols="*,' + wb_standard_width +',*" frameborder=0>'
		str+='<frame src="../htm/empty.htm" scrolling="NO" noresize/>'
		//str+='<textarea rows="22" cols="30">'
		str+='<frameset rows="'
		str+= (debug == true) ? 50 : 0; 
		str+=',*" id="fs" border="0" framespacing="0">'
		str+='<frame name="hidden" src="'
		str+= res.pick_res_frame_url(']]><xsl:value-of select="$tpl_type"/><![CDATA[',']]><xsl:value-of select="$tpl_subtype"/><![CDATA[')
		str+='" marginwidth="0" marginheight="0" scrolling="no" frameborder="0" noresize="noresize">'
		str+='<frame name="frame" src="../htm/loading.htm" marginwidth="0" marginheight="0" scrolling="auto" frameborder="0" noresize="noresize">'
		str+='</frameset>'
		str+='<frame src="../htm/empty.htm" scrolling="NO" noresize/>'
		str+='</frameset>'
		//str+='</textarea>'
		document.write(str)
		]]>
		</script>
		
	</xsl:template>
</xsl:stylesheet>
