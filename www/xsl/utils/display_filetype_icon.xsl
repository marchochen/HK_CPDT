<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template name="display_filetype_icon">
		<xsl:param name="imageSize">32</xsl:param>
		<xsl:param name="vspace">4</xsl:param>
		<xsl:param name="hspace">4</xsl:param>
		<xsl:param name="align"/>
		<xsl:param name="fileName"/>
		<xsl:choose>
			<xsl:when test="substring($fileName,string-length($fileName)-2)='doc' or substring($fileName,string-length($fileName)-3)='docx'">
				<img src="{$wb_img_path}icon_word.gif" border="0" hspace="{$hspace}" vspace="{$vspace}" width="{$imageSize}" height="{$imageSize}" align="{$align}"/>
			</xsl:when>
			<xsl:when test="substring($fileName,string-length($fileName)-2)='ppt' or substring($fileName,string-length($fileName)-3)='pptx'">
				<img src="{$wb_img_path}icon_ppt.gif" border="0" hspace="{$hspace}" vspace="{$vspace}" width="{$imageSize}" height="{$imageSize}" align="{$align}"/>
			</xsl:when>
			<xsl:when test="substring($fileName,string-length($fileName)-2)='txt'">
				<img src="{$wb_img_path}icon_notepad.gif" border="0" hspace="{$hspace}" vspace="{$vspace}" width="{$imageSize}" height="{$imageSize}" align="{$align}"/>
			</xsl:when>
			<xsl:when test="substring($fileName,string-length($fileName)-2)='htm'">
				<img src="{$wb_img_path}icon_web_browser.gif" border="0" hspace="{$hspace}" vspace="{$vspace}" width="{$imageSize}" height="{$imageSize}" align="{$align}"/>
			</xsl:when>
			<xsl:when test="substring($fileName,string-length($fileName)-3)='html'">
				<img src="{$wb_img_path}icon_web_browser.gif" border="0" hspace="{$hspace}" vspace="{$vspace}" width="{$imageSize}" height="{$imageSize}" align="{$align}"/>
			</xsl:when>
			<xsl:when test="substring($fileName,string-length($fileName)-2)='swf'">
				<img src="{$wb_img_path}icon_swf.gif" border="0" hspace="{$hspace}" vspace="{$vspace}" width="{$imageSize}" height="{$imageSize}" align="{$align}"/>
			</xsl:when>
			<xsl:when test="substring($fileName,string-length($fileName)-2)='gif'">
				<img src="{$wb_img_path}icon_gif.gif" border="0" hspace="{$hspace}" vspace="{$vspace}" width="{$imageSize}" height="{$imageSize}" align="{$align}"/>
			</xsl:when>
			<xsl:when test="substring($fileName,string-length($fileName)-2)='jpg' or substring($fileName,string-length($fileName)-3)='jpge' or substring($fileName,string-length($fileName)-2)='png'">
				<img src="{$wb_img_path}icon_jpg.gif" border="0" hspace="{$hspace}" vspace="{$vspace}" width="{$imageSize}" height="{$imageSize}" align="{$align}"/>
			</xsl:when>
			<xsl:when test="substring($fileName,string-length($fileName)-2)='exe'">
				<img src="{$wb_img_path}icon_exe.gif" border="0" hspace="{$hspace}" vspace="{$vspace}" width="{$imageSize}" height="{$imageSize}" align="{$align}"/>
			</xsl:when>
			<xsl:when test="substring($fileName,string-length($fileName)-2)='xls' or substring($fileName,string-length($fileName)-3)='xlsx'">
				<img src="{$wb_img_path}icon_xls.gif" border="0" hspace="{$hspace}" vspace="{$vspace}" width="{$imageSize}" height="{$imageSize}" align="{$align}"/>
			</xsl:when>
			<xsl:when test="substring($fileName,string-length($fileName)-2)='pdf'">
				<img src="{$wb_img_path}icon_pdf.gif" border="0" hspace="{$hspace}" vspace="{$vspace}" width="{$imageSize}" height="{$imageSize}" align="{$align}"/>
			</xsl:when>
			<xsl:otherwise>
				<img src="{$wb_img_path}icon_unknow.gif" border="0" hspace="{$hspace}" vspace="{$vspace}" width="{$imageSize}" height="{$imageSize}" align="{$align}"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
