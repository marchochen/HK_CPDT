<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<!-- =============================================================== -->
	<xsl:variable name="source_link" select="//source"/>
	<!-- =============================================================== -->
	<xsl:template name="DOCTYPE">
		<![CDATA[<!doctype html>]]>
	</xsl:template>
	<xsl:template match="module">
		<xsl:value-of select="document('')/*/xsl:template[@name='DOCTYPE']/node()" disable-output-escaping="yes"/>  
		<html>
			<meta name="viewport" content="initial-scale=1,user-scalable=no,maximum-scale=1,width=device-width" />
			<head>
			<style type="text/css" media="screen"> 
	 			body { margin:0; padding:0; overflow:auto; }    
	        </style> 
			
			<link rel="stylesheet" type="text/css" href="../static/js/flexPaper/css/flexpaper.css" />
			<script type="text/javascript" src="../static/js/jquery.js"></script>
			<script type="text/javascript" src="../static/js/flexPaper/js/jquery.extensions.min.js"></script>
			<script type="text/javascript" src="../static/js/flexPaper/js/flexpaper.js"></script>
			<script type="text/javascript" src="../static/js/flexPaper/js/flexpaper_handlers.js"></script>
			</head>
			<body>
				<div id="documentViewer" class="flexpaper_viewer" style="position:absolute;width:100%;height:100%"></div>

		        <script type="text/javascript">
					$.ajax({
						url : '../<xsl:value-of select="concat('resource/', /module/@id, '/', $source_link)"/>.js',
					 	type : 'HEAD',
					   	error : function() {
							$('#documentViewer').FlexPaperViewer({ 
				         		config : {
				        			PDFFile : '../<xsl:value-of select="concat('resource/', /module/@id, '/', $source_link)"/>',
				       				localeChain: '<xsl:value-of select="$wb_cur_lang"/>'
				     			}
							}); 
						},
						success: function() {
							$('#documentViewer').FlexPaperViewer({ 
				      			config : {
				      				IMGFiles : '../<xsl:value-of select="concat('resource/', /module/@id, '/', $source_link)"/>_{page}.jpg',
		                     		JSONFile : '../<xsl:value-of select="concat('resource/', /module/@id, '/', $source_link)"/>.js',
				              		PDFFile : '../<xsl:value-of select="concat('resource/', /module/@id, '/', $source_link)"/>',
				             		localeChain: '<xsl:value-of select="$wb_cur_lang"/>'
				            	}
							}); 
						}
					});
		        </script>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
