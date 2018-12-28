<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/escape_backslash.xsl"/>

<xsl:import href="wb_const.xsl" />
<xsl:import href="cust/wb_cust_const.xsl"/><xsl:import href="utils/wb_utils.xsl" />
<xsl:output  indent="yes"/>

<xsl:variable name="fileName"><xsl:value-of select="/module/header/source"/></xsl:variable>
<xsl:variable name="file_ext"><xsl:value-of select="substring-after($fileName,'.')"/></xsl:variable>
<xsl:variable name="file_type">
	<xsl:if test="$file_ext != 'docx' and $file_ext != 'doc' and $file_ext != 'pptx' and $file_ext != 'ppt' and $file_ext != 'txt' and $file_ext != 'htm' and $file_ext != 'html' and $file_ext != 'swf' and $file_ext != 'gif' and $file_ext != 'jpg' and $file_ext != 'xls' and $file_ext != 'xlsx'">unknow</xsl:if>
</xsl:variable>

<xsl:template match="/">
	<html>
	<head>
	<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js" ></script>
	<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>	
	<script type="text/javascript" src="../static/js/jquery.js"></script>
	<xsl:choose>
<xsl:when test="module/header/source/@type = 'URL' and not ( starts-with(module/header/source, 'callto:') )
 "><script LANGUAGE="JavaScript">
<![CDATA[
url = ']]><xsl:value-of select="module/header/source" /><![CDATA[';
/*if (window.opener) { // if it is opened by javascript (TODO: need testing with netscape)
	window.open(url);
	window.close();
} else {
	window.location.href = url;
}*/
	// for Affirm Score course
	if (url.substring(0, 22) == "http://www.score2k.com") {	
		url = url + "&userid=" + "]]><xsl:value-of select="//cur_usr/@id"/><![CDATA[";
		wbUtilsOpenWin(url);
		parent.resizeTo(600, 400);
		window.location.href = "../htm/loading_course.htm";
	}
	// normal URL
	else {
		window.location.href = url;
	}

]]>	</script></xsl:when>
<xsl:otherwise>
<xsl:choose>
	<xsl:when test="$file_type != 'unknow' ">
		<script ><![CDATA[
			var fileExt = ']]><xsl:value-of select="$file_ext"/><![CDATA[';
			var fileArray = ['doc','docx','xls','xlsx','ppt','pptx','pdf','txt'];  
			var flg=false;
			for(var i=0;i<fileArray.length;i++)
			{
			    if (fileArray[i] == fileExt.toLowerCase()) { 
	                        flg=true;
	                    }
			}
			if(fileExt && (fileExt == 'jpg' || fileExt == 'png' || fileExt == 'gif' || fileExt == 'jpeg')){
				
				window.onload = function(){
					var contentContainer = "<div style='text-align:center;margin-top:28px'>{{content}}</div>";
					source =  ']]><xsl:call-template name="escape_backslash"><xsl:with-param name="my_right_value"><xsl:value-of select="$fileName"/></xsl:with-param></xsl:call-template><![CDATA[';
					var url = '../resource/]]><xsl:value-of select="module/@id"/><![CDATA[/' + source;
		   			contentContainer = contentContainer.replace("{{content}}","<img src='"+url+"'/>");
		   			document.body.appendChild($(contentContainer)[0]);
				};
	   			
	   		} else if(flg){
	   			source =  ']]><xsl:call-template name="escape_backslash"><xsl:with-param name="my_right_value"><xsl:value-of select="$fileName"/></xsl:with-param></xsl:call-template><![CDATA['
			    window.location.href = wb_utils_controller_base+'idv/preview?filePath='+'resource/]]><xsl:value-of select="module/@id"/><![CDATA[/' + source
	   		}else {
	   		
	   			window.location.href = url;
	   			
	   		}
		]]></script>
	</xsl:when>
	<xsl:when test="$file_ext = 'pdf'">
		<script ><![CDATA[
			var userAgent = navigator.userAgent.toLowerCase();
			var browser = {
		        version: (userAgent.match(/.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/) || [])[1],
		        safari: /webkit/.test(userAgent),
		        opera: /opera/.test(userAgent),
		        msie: /msie/.test(userAgent) && !/opera/.test(userAgent),
		        mozilla: /mozilla/.test(userAgent) && !/(compatible|webkit)/.test(userAgent),
		        chrome: /chrome/.test(userAgent)
		    };
	        var supportsCanvasDrawing = (browser.mozilla && browser.version.split(".")[0] >= 4) ||
	            (browser.chrome && browser.version.split(".") >= 535) ||
	            (browser.msie && browser.version.split(".")[0] >= 10) ||
	            (browser.safari && browser.version.split(".")[0] >= 535);
	        if(supportsCanvasDrawing){
				window.location.href = wb_utils_invoke_servlet('cmd','get_mod','stylesheet','tpl_web_browser_pdf.xsl','mod_id',]]><xsl:value-of select="/module/@id"/><![CDATA[);
			} else {
				source =  ']]><xsl:call-template name="escape_backslash"><xsl:with-param name="my_right_value"><xsl:value-of select="$fileName"/></xsl:with-param></xsl:call-template><![CDATA['
				url = '../resource/]]><xsl:value-of select="module/@id"/><![CDATA[/' + source
				$.ajax({
					url : url + '.js',
				 	type : 'HEAD',
				   	error : function() {
						window.location.href = url;
					},
					success: function() {
						window.location.href = wb_utils_invoke_servlet('cmd','get_mod','stylesheet','tpl_web_browser_pdf.xsl','mod_id',]]><xsl:value-of select="/module/@id"/><![CDATA[);
					}
				});
			}
		]]></script>	
	</xsl:when>
	<xsl:otherwise>
		<script ><![CDATA[
			var url = wb_utils_invoke_servlet('cmd','get_mod','stylesheet','blank_template.xsl','mod_id',]]><xsl:value-of select="/module/@id"/><![CDATA[);
			window.location.href=url;
		]]></script>	
	</xsl:otherwise>
</xsl:choose>


</xsl:otherwise>
</xsl:choose>
	</head>
	<body></body>
	</html>
</xsl:template>

</xsl:stylesheet>
