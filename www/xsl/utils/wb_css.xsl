<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- CSS Style sheet template & Meta Keywords Tag-->
	<xsl:template name="wb_css">
		<xsl:param name="view">wb_ui</xsl:param>
	</xsl:template>
	<!-- Global CSS Style sheet template & Meta Keywords Tag-->
	<xsl:template name="wb_glb_css">
	</xsl:template>
	<!-- home -->
	<xsl:template name="wb_home_css">
	</xsl:template>
	
	<xsl:template name="new_css">
		<link rel="stylesheet" href="{$wb_new_css}bootstrap.css"/>
		
		<link rel="stylesheet" href="{$wb_new_font_css}font-awesome.min.css"/>
	    <link rel="stylesheet" href="../static/css/base.css"/>
		<link rel="stylesheet" href="{$wb_new_admin_css}admin.css"/> 
		<xsl:choose>
		   <xsl:when test="not($wb_cur_lang = 'en-us')">
		   </xsl:when>
		   <xsl:otherwise>
		        <!-- 兼容英文的css -->
			<link rel="stylesheet" href="../static/css/base-en.css"/>
			<link rel="stylesheet" href="{$wb_new_admin_css}admin-en.css"/>
		   </xsl:otherwise>
		</xsl:choose>
		
		  
	</xsl:template>
</xsl:stylesheet>