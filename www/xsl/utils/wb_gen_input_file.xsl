<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<!-- =================================================================== -->
<xsl:template name="wb_gen_input_file">
	<xsl:param name="name"/>
	<xsl:param name="onBlur"/>
	<xsl:param name="onfocus"/>
	<xsl:param name="disabled"/>
	<xsl:param name="onclick"/>
	<xsl:param name="title"/>
	<xsl:param name="id"/>
	<xsl:param name="onchange"/>
	<xsl:param name="value"/>
	
	<xsl:variable name="select_file">
		<xsl:choose>
			<xsl:when test="$wb_lang='gb'">请选择文件</xsl:when>
			<xsl:when test="$wb_lang='ch'">請選擇文件</xsl:when>
			<xsl:otherwise>No file selected</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="glanced">
		<xsl:choose>
		<xsl:when test="$wb_lang='gb'">浏览</xsl:when>
		<xsl:when test="$wb_lang='ch'">瀏覽</xsl:when>
		<xsl:otherwise>browse</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<div class="file" style=" margin: 2px 0 0 0; height: 0;">
		<xsl:choose>
			<xsl:when test="$disabled=''">
				<input id="{$id}" value="{$value}" title="{$title}" onclick="{$onclick}" onBlur="{$onBlur}" onfocus="{$onfocus}"  class="file_file" name="{$name}" type="file" onchange="$(this).siblings('.file_txt').val(this.value);{$onchange}"/>
			</xsl:when>
			<xsl:otherwise>
				<input id="{$id}" value="{$value}" title="{$title}" onclick="{$onclick}" onBlur="{$onBlur}" onfocus="{$onfocus}" disabled="{$disabled}"  class="file_file" name="{$name}" type="file" onchange="$(this).siblings('.file_txt').val(this.value);{$onchange}"/>
			</xsl:otherwise>
		</xsl:choose>

 		<input  class="file_txt" name = "select_file" value="{$select_file}"/>
 		<div class="file_button-blue" style="height:28px"><xsl:value-of select="$glanced"/> </div>
    </div> 
	<br/>
</xsl:template>
<!-- =================================================================== -->
</xsl:stylesheet>
