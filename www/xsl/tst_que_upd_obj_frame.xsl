<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
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
	<!-- =============================================================== -->
	<xsl:template match="module | fixed_assessment | dynamic_que_container">
		<head>
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
				var obj_id = getUrlParam('obj_id')
				var msp_id = getUrlParam('msp_id')
				//Man: To avoid getting "undefined" value at "frame" page, "hidden" page should be loaded before "frame" page load
				//page loading flow should be:
				//1.  frameset -->"hidden" page , loading.htm
				//2.  hidden frame loaded ,hidden value loaded, and call frameset function "load_frame()"
				//3.  frameset -->"hidden" page , "frame" page
				
				function load_frame(){
					var url = obj.upd_obj_url(]]><xsl:value-of select="$module_id"/><![CDATA[,obj_id, msp_id, ']]><xsl:value-of select="$module_subtype"/><![CDATA[')
					frame.location.href = url
				}


				
]]></script>
		</head>
		<script language="JavaScript">
		<![CDATA[
		//=====
		var debug = false;
		//=====
		
		wb_standard_width = ]]><xsl:value-of select="$wb_gen_table_width"/><![CDATA[;   
		var str = '';
		str+='<frameset cols="*,' + wb_standard_width +',*" frameborder=0>'    
		str+='<frame src="../htm/empty.htm" scrolling="NO" noresize/>'         
		//str+='<textarea rows="22" cols="30">'
		str+='<frameset rows="'
		str+= (debug == true) ? 50 : 0; 
		str+=',*" id="fs" border="0" framespacing="0">'
		str+='<frame name="hidden" src="'
		str+= obj.pick_obj_hidden_frame_url(]]>'<xsl:value-of select="$course_id"/>',<xsl:value-of select="$module_id"/><![CDATA[)
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
