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
		<html style="height:100%">
			<meta name="viewport" content="initial-scale=1,user-scalable=no,maximum-scale=1,width=device-width" />
			<head>
			<style type="text/css" media="screen"> 
	 			body { margin:0; padding:0; overflow:auto; }    
	        </style> 
			<script type="text/javascript" src="../static/js/jquery.js"></script>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			</head>
			<body>
				<div style="position:absolute;width:100%;height:100%">
					<iframe id="ifm" allowtransparency="true" allowfullscreen="true" width="100%" height="100%" frameBorder="0" scrolling="no"></iframe>
				</div>

		        <script type="text/javascript">
					sessionStorage.clear();
					var pdfUrl = '../<xsl:value-of select="concat('resource/', /module/@id, '/', $source_link)"/>';
					var items = getPdfImgItems(pdfUrl);
					
					var url=wb_utils_controller_base+'idv/preview?filePath=<xsl:value-of select="concat('resource/', /module/@id, '/', $source_link)"/>';
					sessionStorage.setItem("photoItems",JSON.stringify(items));
					$("#ifm").attr("src", url);
					
					function getPdfImgItems(fileUrl){
						var url = fileUrl+'.js';
						var pages;
						var width;
						var height;
						var items =" [ ";
						$.ajaxSettings.async = false; 
						$.getJSON(url, function(data) {
							if(typeof(data)== "string"){
								data =$.parseJSON(data);
							}
							pages = data[0].pages;
							width = data[0].width;
							height = data[0].height;
							for(var i=1;i <![CDATA[<]]>= pages;i++){
								var str_json = " {";
								str_json += "src: '../" + fileUrl + "_"+i+".jpg',"+"w:"+width+", h:"+height+"}";
								if(i!=pages){
									str_json += ",";
								}
								items += str_json;
							}
							items += "]";
							items = eval("("+items+")");
							
						 });
						
						return items;
					}
					
					
		        </script>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
