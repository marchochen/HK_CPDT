<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/><xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->	
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->	
	<xsl:template match="module">
		<xsl:variable name="module_id" select="@id"/>
		<xsl:variable name="mod_id" select="@id"/>
		<xsl:variable name="course_id" select="header/@course_id"/>	
		<xsl:variable name="mod_type" select="header/@subtype"/>	
		<xsl:variable name="mod_timestamp" select="@timestamp"/>	
		<xsl:variable name="mode" select="/*/@view_mode"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}que_send_mc.js"/>
			<script language="JavaScript" src="{$wb_js_path}que_send_mt.js"/>
			<script language="JavaScript" src="{$wb_js_path}que_send_fb.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_media.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_course.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_module.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			module_lst = new wbModule;
			wb_svy = new wbSvy;
			course_lst = new wbCourse;
			
			function get_mod_timestamp(){
				var mod_timestamp = ']]><xsl:value-of select="$mod_timestamp"/><![CDATA['
				return mod_timestamp;
			}
			function get_mod_id(){
				var mod_id = ']]><xsl:value-of select="$mod_id"/><![CDATA['
				return mod_id;
			}	
					
			function get_mod_type(){
				var mod_type = ']]><xsl:value-of select="$mod_type"/><![CDATA['
				return mod_type;
			}	


			function resize_fs(h){
				if(document.all){
					document.all['fs'].rows = h + ",*"
				}else if(document.getElementById != null){
					document.getElementById("fs").rows = h + ",*"
				}
			}
			]]></SCRIPT>
		</head>
		<script language="JavaScript"><![CDATA[
		mod_id = ']]><xsl:value-of select="$module_id"/><![CDATA[';
		course_id =  ']]><xsl:value-of select="$course_id"/><![CDATA[';

		mod_attempted = getUrlParam("attempted")
		usr_id = getUrlParam("usr_id")

		// for subsequent actions' use
		//wb_utils_set_cookie('mod_id', mod_id)
		//wb_utils_set_cookie('mod_attempted', mod_attempted)
		//wb_utils_set_cookie('url_prev', window.location.href)
		//wb_utils_set_cookie('mod_url',self.location.href)
		with (document) {
		
		wb_standard_width = ]]><xsl:value-of select="$wb_gen_table_width"/><![CDATA[;
			
		str=''
		str+='<frameset cols="*,' + wb_standard_width +',*" frameborder=0>'
		str+='<frame src="../htm/empty.htm" scrolling="NO" noresize/>'
		str+='<frameset rows="20,*" frameborder="0" framespacing="0" border="0"  id="fs">'
		str+='<frame src="'
		
		str+= wb_svy.info_nav_url(course_id,mod_id)
		
		str+='" frameborder="NO" marginheight="0" marginwidth="10" name="navigation" noresize="noresize" scrolling="NO">'
		str+='<frameset cols="240,*" frameborder="0" framespacing="0">'
		str+='<frame src="'
		
		str+= wb_svy.info_menu_url(mod_id)
		
		str+='" name="menu" style="width:240px;" marginwidth="0" marginheight="0" scrolling="yes" frameborder="no" noresize="noresize">'
		str+='<frame src="'
		
		str+= wb_svy.info_content_url(mod_id,course_id)
		
		str+='" name="content" id="content" style="width:744px" marginwidth="0" marginheight="0" scrolling="auto" frameborder="no">'
		str+='</frameset>'
		str+='</frameset>'
		str+='<frame src="../htm/empty.htm" scrolling="NO" noresize/>'
		str+='</frameset>'
		
		write(str);
		
		}
		]]></script>
	</xsl:template>
</xsl:stylesheet>
