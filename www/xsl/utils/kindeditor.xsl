<?xml version='1.0' encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template name="kindeditor_css">
		<script language="javascript" type="text/javascript" src="{$wb_js_path}kindeditor/kindeditor.js" />
		<script language="javascript" type="text/javascript" src="{$wb_js_path}kindeditor/lang/en.js" />
		<link type="text/css" rel="stylesheet" href="{$wb_js_path}kindeditor/themes/default/default.css" />
		<script language="javascript" type="text/javascript"><![CDATA[
			var kindeditorOptions = {
				uploadJson 		: wb_utils_app_base+'servlet/Dispatcher',
				langType 		: ']]><xsl:value-of select="$wb_lang"/><![CDATA[',
				urlType 		: 'domain',
				fieldName 		: 'imgFile',
				items			: [
			        'source','undo', 'redo', '|', 'cut', 'copy', 'paste', '|', 
			        'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline', 'strikethrough', 'removeformat', '|', '/',
			        'justifyleft', 'justifycenter', 'justifyright', 'justifyfull', 'insertorderedlist', 'insertunorderedlist', 
			        'image', 'insertfile', 'table', 'hr', 'emoticons', 'link', 'unlink', '|', 'fullscreen', 'preview', 'selectall', '|', '|', 'about' 
				]
			};
			
			var kindeditorMCOptionsUpd = {
				uploadJson 		: wb_utils_app_base+'servlet/Dispatcher',
				langType 		: ']]><xsl:value-of select="$wb_lang"/><![CDATA[',
				urlType 		: 'domain',
				fieldName 		: 'imgFile',
				newlineTag		: 'br',
				items			: [
			        'source','undo', 'redo', '|', 'cut', 'copy', 'paste', '|', 
			        'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline', 'strikethrough', 
			        'removeformat', '|', 'image',
				]
			};
			
			var minKindeditorOptions = $.extend({}, kindeditorOptions, {
				width			: 460,
				minWidth		: 460
			});
			
			var kindeditorMCOptions = $.extend({}, kindeditorOptions, {
				newlineTag		: 'br',
				items : [
			        'source','undo', 'redo', '|', 'cut', 'copy', 'paste', '|', 
			        'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline', 'strikethrough', 
			        'removeformat', '|', 'image',
				]
			});
			
			var kindeditorFBOptions = $.extend({}, kindeditorOptions, {
				minWidth : 550,
				zIndex : 14000,
				items : [
			        'source','undo', 'redo', '|', 'cut', 'copy', 'paste', '|', 
			        'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline', 'strikethrough',  
			        'removeformat', '|', 'fb',
				]
			});
		]]></script>
	</xsl:template>

	<xsl:template name="kindeditor_panel">
		<xsl:param name="editor_id">editor</xsl:param>
		<xsl:param name="upload_ind">true</xsl:param>
		<xsl:param name="option">kindeditorOptions</xsl:param>
		<xsl:param name="rows">12</xsl:param>
		<xsl:param name="cols">68</xsl:param>
		<xsl:param name="body" />
		<xsl:param name="frm" />
		<xsl:param name="fld_name" />
		
		<textarea rows="{$rows}" cols="{$cols}" id="{$editor_id}" name="{$fld_name}">
			<xsl:copy-of select="$body" />
			<!-- <xsl:value-of disable-output-escaping="yes" select="$body" /> -->
		</textarea>
		
		<script language="javascript" type="text/javascript"><![CDATA[
			var ]]><xsl:value-of select="$editor_id" /><![CDATA[;
        	KindEditor.ready(function(K) {
                ]]><xsl:value-of select="$editor_id" /><![CDATA[ = K.create('#]]><xsl:value-of select="$editor_id" /><![CDATA[', ]]><xsl:value-of select="$option" /><![CDATA[);
        	});
		]]></script>
	</xsl:template>
</xsl:stylesheet>