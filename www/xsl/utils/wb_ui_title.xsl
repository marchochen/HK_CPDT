<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:template name="wb_ui_title">
		<xsl:param name="text"/>
		<xsl:param name="text_class">wzb-title-11 wzb-banner-bg14 work_input_desc</xsl:param>
		<xsl:param name="wihe_class">wb-ui-title-wihe</xsl:param>
		<xsl:param name="width" select="$wb_gen_table_width"/>
		<xsl:param name="new_template">false</xsl:param>
		<xsl:param name="close_but">false</xsl:param>
		<xsl:param name="text_style"></xsl:param>
		<xsl:param name="upper_style"></xsl:param>
		<xsl:variable name="left_width">
			<xsl:choose>
				<xsl:when test="contains($width,'%')"><xsl:value-of select="number(substring-before($width,'%')) - 2"/>%</xsl:when>
				<xsl:otherwise><xsl:value-of select="$width - 8"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$new_template = 'true'">
				<div class="upper" style="{$upper_style}">
					<xsl:choose>
						<xsl:when test="$close_but = 'true'">
							<img src="{$wb_img_path}w-tanhao.png" style="float:left;height:40px;margin:10px 0 0 30px;"/>
							<div class="upper_info" style="height:60px;">
								<div class="upper_tit" style="padding:8px 0 0 20px;">
									<!-- <img src="../wb_image/lang/{$wb_cur_lang}/prompt.jpg"/> -->
									<xsl:copy-of select="$text"/>
								</div>
								<div class="upper_tool" style="margin:-15px -10px 0 0;">
									<input type="button" name="wiztxt" class="exit_btn orangeCfeb" value="{java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'button_exit')}" onclick="javascript:submitToServer(true)"/>
								</div>
							</div> 
						</xsl:when>
						<xsl:otherwise>
							
							<div class="upper_info">
								<div class="upper_tit" style="{$text_style}">
									<xsl:copy-of select="$text"/>
								</div>
							</div> 
						</xsl:otherwise>
					</xsl:choose>
				</div>
			</xsl:when>
			<xsl:otherwise>
			
				<table border="0" width="{$width}" cellspacing="0" cellpadding="2">
					<tr>
						<td><!-- 新样式通过{$text_class}传递引入，不要修改原有样式 -->
						<div  class="{$wihe_class}">
							<div class="{$text_class} wzb-module-title" align="left" style="{$text_style}">
								<xsl:copy-of select="$text"/>
							</div>
						</div>
						</td>
					</tr>
				</table>
			
				<div class="clear"></div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
