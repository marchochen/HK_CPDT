<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!-- Select all checkbox list -->
<xsl:template name="select_all_checkbox">
<xsl:param name="chkbox_lst_cnt"/><!-- checkbox list count -->
<xsl:param name="display_icon">true</xsl:param><!-- {true | false} -->
<xsl:param name="chkbox_lst_nm"/><!-- checkbox list name -->
<xsl:param name="sel_all_chkbox_nm">sel_all_checkbox</xsl:param><!-- select all checkbox name -->
<xsl:param name="frm_name">frmXml</xsl:param>
<xsl:param name="style"></xsl:param>
<xsl:choose>
<xsl:when test="$chkbox_lst_cnt &lt; 1">
</xsl:when>
	<xsl:otherwise>
		<xsl:choose>
			<xsl:when test="$display_icon = 'true'">
				<input onclick="javascript:gen_frm_sel_all_checkbox(document.{$frm_name},document.{$frm_name}.{$sel_all_chkbox_nm},'{$chkbox_lst_nm}')" type="checkbox"/>
				<input type="hidden" name="{$sel_all_chkbox_nm}" value="false" id="sel_all"/>
			</xsl:when>
			<xsl:otherwise>
				<input style="{$style}" name="{$sel_all_chkbox_nm}" type="checkbox" id="sel_all" onclick="javascript:gen_frm_sel_all_checkbox(document.{$frm_name},this,'{$chkbox_lst_nm}')"/>
			</xsl:otherwise>
		</xsl:choose>	
	</xsl:otherwise>
</xsl:choose>
</xsl:template>
</xsl:stylesheet>
