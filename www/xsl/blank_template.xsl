<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:output  indent="yes"/>
	<xsl:variable name="subtype"><xsl:value-of select="/module/header/@subtype"/></xsl:variable>
	<xsl:variable name="course_id"><xsl:value-of select="/module/header/@course_id"/></xsl:variable>
	<xsl:variable name="mod_id"><xsl:value-of select="/module/header/@mod_id"/></xsl:variable>
	<xsl:variable name="fileName"><xsl:value-of select="/module/header/source"/></xsl:variable>
	<xsl:variable name="file_ext"><xsl:value-of select="substring-after($fileName,'.')"/></xsl:variable>
	<xsl:template match="/">
	<html>
		<xsl:apply-templates select="module"/>
	</html>
	</xsl:template>
	<xsl:template match="/module">
			<head>
				<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script type="text/javascript" src="../static/js/jquery.js"></script>
				<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
					var mod_lst = new wbModule;
				]]>
				</script>
				<TITLE><xsl:value-of select="$wb_wizbank"/></TITLE>
			</head>
			<body>
				<xsl:choose>
					<xsl:when test="header/source/@type = 'URL'">
						<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
							window.location.href = ']]><xsl:value-of select="header/source"/><![CDATA['
						]]>
						</script>
					</xsl:when>
					<xsl:when test="header/source/@type = 'FILE' or header/source/@type = 'ZIPFILE'">
						<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
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
					        if(supportsCanvasDrawing && ']]><xsl:value-of select="$file_ext"/><![CDATA[' == 'pdf'){
					        	window.localStorage.setItem("hide_full_screen_el","hide");
								window.location.href = wb_utils_invoke_servlet('cmd','get_mod','stylesheet','tpl_web_browser_pdf.xsl','mod_id',]]><xsl:value-of select="@id"/><![CDATA[);
							} else {
								var contentContainer = "<div style='text-align:center;margin-top:28px'>{{content}}</div>";
								var fileExt = ']]><xsl:value-of select="$file_ext"/><![CDATA[';
								var fileArray = ['doc','docx','xls','xlsx','ppt','pptx','pdf','txt'];  
								var flg=false;
								for(var i=0;i<fileArray.length;i++)
								{
								    if (fileArray[i] == fileExt.toLowerCase()) { 
				                          flg=true;
				                      }
								}
								      if(flg){
										url = wb_utils_controller_base+'idv/preview?filePath=resource/'+']]><xsl:value-of select="@id"/><![CDATA[/' + ']]><xsl:value-of select="$fileName"/><![CDATA[';
										window.location.href = url;
								 	    }else{
										url = '../resource/' + ']]><xsl:value-of select="@id"/><![CDATA[/' + ']]><xsl:value-of select="$fileName"/><![CDATA[';
							
										$.ajax({
											url : url + '.js',
										 	type : 'HEAD',
										   	error : function() {
										   		if(fileExt && (fileExt == 'jpg' || fileExt == 'png' || fileExt == 'gif' || fileExt == 'jpeg')){
										   			contentContainer = contentContainer.replace("{{content}}","<img src='"+url+"'/>");
										   			document.body.appendChild($(contentContainer)[0]);
										   		} else {
										   			if((']]><xsl:value-of select="$subtype"/><![CDATA[' == 'RDG' || ']]><xsl:value-of select="$subtype"/><![CDATA[' == 'ASS') && fileExt && (fileExt == 'mp4' || fileExt == 'MP4') ){
										   				//mod_lst.preview_exec('RDG',]]><xsl:value-of select="$mod_id"/><![CDATA[,'ist_vod.xsl',]]><xsl:value-of select="$course_id"/><![CDATA[);
										   				var isFullScreen = true;
														var str_feature = _wbModuleGetContentWindowSize(']]><xsl:value-of select="$subtype"/><![CDATA[',isFullScreen);
										   				wb_utils_set_cookie('isWizpack','false');
										   				var url_success = wb_utils_invoke_servlet(
											   				'cmd','get_mod','stylesheet', 'ist_vod.xsl','mod_id',]]><xsl:value-of select="$mod_id"/><![CDATA[,
															'cos_id',]]><xsl:value-of select="$course_id"/><![CDATA[,
															'url_failure','../htm/close_window.htm',
															'page','0'
														);
										   				window.location.href = url_success;
										   				return;
										   			}
										   			window.location.href = url;
										   		}
											},
											success: function() {
												
												window.location.href = wb_utils_invoke_servlet('cmd','get_mod','stylesheet','tpl_web_browser_pdf.xsl','mod_id',]]><xsl:value-of select="@id"/><![CDATA[);
											}
										});
									}
							 ]]>
							}
						</script>					
					</xsl:when>
					<xsl:when test="header/@subtype = 'ASS' and not(header/instruction = '')">
						<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
						window.location.href = mod_lst.start_content_url(']]><xsl:value-of select="header/@subtype"/><![CDATA[', ]]><xsl:value-of select="@id"/><![CDATA[,'ass_instruction.xsl')
						]]>
						</script>					
					</xsl:when>
					<xsl:otherwise>Unknown Source Type</xsl:otherwise>
				</xsl:choose>			
			</body>
	</xsl:template>
</xsl:stylesheet>
