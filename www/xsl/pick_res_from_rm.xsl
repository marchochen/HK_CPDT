<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes"/>
	<!-- ========================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ========================================================== -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="pragma" content="no-cache"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
			isReplace = getUrlParam('replace')		
			mod_subtype = getUrlParam('res_subtype')	
				
			function set_form(frm){
			
					var win = null;
        if (window.ActiveXObject)
        {
           win=window.opener;
        }
        else if (document.getBoxObjectFor)
        {
             win=window.opener;
        }
        else if (window.MessageEvent && !document.getBoxObjectFor)
        {
            win=window.parent.opener.parent;
        }
        else if (window.opera)
        {
            alert('请使用IE,firefox,chome浏览器进行操作！');
        }
        else if (window.openDatabase){
            alert('请使用IE,firefox,chome浏览器进行操作！');
        };
            
					var oFrm;
					if(win.ContentPage){
						oFrm = win.ContentPage.document.frmXml;
					}else{
						oFrm = window.opener.document.frmXml;
					}
					
					
					
					oFrm.source_content.value = frm.source_link.value	
					oFrm.source_type.value = frm.source_type.value	
					oFrm.copy_media_from.value = frm.res_id.value
					
				if ( isReplace == 'Y' ){
				
				]]><xsl:if test="resource/display/option/progress/@difficulty = 'true'">
				if(oFrm.mod_difficulty) {
					 oFrm.mod_difficulty.selectedIndex = frm.difficulty.value -1;				
				}
				</xsl:if>
				<xsl:if test="resource/display/option/progress/@duration= 'true' ">
				if(oFrm.mod_duration) {
					 oFrm.mod_duration.value = frm.duration.value	
				}
				</xsl:if>
				<xsl:if test="resource/display/option/general/@title= 'true' ">
				if(oFrm.mod_title) {
					 if(mod_subtype == 'AICC_AU') {
					   if(oFrm.mod_title.value == ''){
                            oFrm.mod_title.value = frm.title.value  
					   }
					 }else{
                            oFrm.mod_title.value = frm.title.value  
					 }
				}
				</xsl:if>
				<xsl:if test="resource/display/option/general/@desc= 'true' ">
				if(oFrm.mod_desc) {
					 oFrm.mod_desc.value = frm.desc.value	
				}
				</xsl:if><![CDATA[
				
					if (mod_subtype!= 'VOD' & mod_subtype != 'AICC_AU' & mod_subtype != 'NETG_COK'){				
						oFrm.mod_annotation.value = frm.annotation.value;
						if ( frm.ann_ashtml.value == 'Y'){
							oFrm.asHTML.checked = true;
						}else{
							oFrm.asHTML.checked = false;
						}
					}
					
					if (oFrm.mod_type){
						oFrm.mod_type.value = frm.mod_type.value	
					}
				}
			
				if(win.invoke_after_pick){
					win.invoke_after_pick()	
				}
				parent.window.close();
			}
			]]></script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="javascript:set_form(document.frmXml)">
			<form name="frmXml">
				<xsl:call-template name="content"/>
			</form>
		</body>
	</xsl:template>
	<!-- ========================================================== -->
	<xsl:template name="content">
		<input type="hidden" name="res_id" value="{/resource/@id}"/>
		<input type="hidden" name="difficulty" value="{/resource/header/@difficulty}"/>
		<input type="hidden" name="duration" value="{/resource/header/@duration}"/>
		<input type="hidden" name="mod_type" value="{/resource/header/@subtype}"/>
		<input type="hidden" name="title" value="{/resource/body/title}"/>
		<textarea rows="0" cols="0" name="desc">
			<xsl:value-of select="/resource/body/desc"/>
		</textarea>
		<input type="hidden" name="source_type" value="{/resource/body/source/@type}"/>
		<input type="hidden" name="source_link" value="{/resource/body/source}"/>
		<xsl:choose>
			<xsl:when test="/resource/body/annotation/html">
				<textarea rows="1" cols="1" name="annotation">
					<xsl:value-of disable-output-escaping="yes" select="/resource/body/annotation/html"/>
				</textarea>
				<input type="hidden" name="ann_ashtml" value="Y"/>
			</xsl:when>
			<xsl:otherwise>
				<textarea rows="1" cols="1" name="annotation">
					<xsl:call-template name="unescape_html_linefeed">
						<xsl:with-param name="my_right_value">
							<xsl:value-of select="/resource/body/annotation"/>
						</xsl:with-param>
					</xsl:call-template>
				</textarea>
				<input type="hidden" name="ann_ashtml" value="N"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
