<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:import href="wb_const.xsl"/>
<xsl:variable name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:variable>

	<!-- =============================================================== -->
	<xsl:template match="/tableofcontents">
		<xsl:call-template name="tree_xml"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="tree_xml">	
		<tree srcid="{@identifier}" text="{@title}" type="mode">
			<xsl:apply-templates select="item" mode="node"/>
		</tree>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="item" mode="node">
	<xsl:variable name="node_image">
		<xsl:call-template name="image">
			<xsl:with-param name="type" select="restype/text()"/>
		</xsl:call-template>
	</xsl:variable>	
	<xsl:variable name="str_action">
		<xsl:choose>
			<xsl:when test="itemtype/text()='FDR'">javascript:folder_read_mode('<xsl:value-of select="@title"/>');</xsl:when>
			<xsl:otherwise>javascript:mod_read_mode('<xsl:value-of select="@identifierref"/>');</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:variable name="is_folder">
		<xsl:choose>
			<xsl:when test="itemtype/text()='FDR'">YES</xsl:when>
			<xsl:otherwise>NO</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="has_child">
		<xsl:choose>
			<xsl:when test="count(item) &gt; 0">YES</xsl:when>
			<xsl:otherwise>NO</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
		
		<tree srcid="{@identifierref}" text="{@title}" title="{@title}" identifier="{@identifier}" itemtype="{itemtype/text()}" restype="{restype/text()}" is_folder="{$is_folder}" has_child="{$has_child}" icon="{$node_image}" openIcon="{$node_image}" action="{$str_action}">
			<xsl:apply-templates select="item" mode="node"/>
		</tree>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="image">
	<xsl:param name="type"/>
	
	<xsl:variable name="img_file">
	<xsl:choose>
		<xsl:when test="$type='ROOT'">root.gif</xsl:when>
		<xsl:when test="$type='ADO'">sico_ado.gif</xsl:when>
		<xsl:when test="$type='AICC_AU'">sico_aicc_au.gif</xsl:when>
		<xsl:when test="$type='ASS'">sico_ass.gif</xsl:when>
		<xsl:when test="$type='CHT'">sico_cht.gif</xsl:when>
		<xsl:when test="$type='DXT'">sico_dxt.gif</xsl:when>
		<xsl:when test="$type='EAS'">sico_eas.gif</xsl:when>
		<xsl:when test="$type='EXC'">sico_exc.gif</xsl:when>
		<xsl:when test="$type='EXM'">sico_exm.gif</xsl:when>
		<xsl:when test="$type='EXP'">sico_exp.gif</xsl:when>
		<xsl:when test="$type='FAQ'">sico_faq.gif</xsl:when>
		<xsl:when test="$type='FIG'">sico_fig.gif</xsl:when>
		<xsl:when test="$type='FOR'">sico_for.gif</xsl:when>
		<xsl:when test="$type='FWK'">sico_fwk.gif</xsl:when>
		<xsl:when test="$type='GAG'">sico_gag.gif</xsl:when>
		<xsl:when test="$type='GLO'">sico_glo.gif</xsl:when>
		<xsl:when test="$type='GRP'">sico_grp.gif</xsl:when>
		<xsl:when test="$type='LCT'">sico_lct.gif</xsl:when>
		<xsl:when test="$type='NETG_COK'">sico_netg_cok.gif</xsl:when>
		<xsl:when test="$type='ORI'">sico_ori.gif</xsl:when>
		<xsl:when test="$type='RDG'">sico_rdg.gif</xsl:when>
		<xsl:when test="$type='REF'">sico_ref.gif</xsl:when>
		<xsl:when test="$type='SCO'">sico_sco.gif</xsl:when>
		<xsl:when test="$type='stx'">sico_stx.gif</xsl:when>
		<xsl:when test="$type='SVY'">sico_svy.gif</xsl:when>
		<xsl:when test="$type='TST'">sico_tst.gif</xsl:when>
		<xsl:when test="$type='TUT'">sico_tut.gif</xsl:when>
		<xsl:when test="$type='VCR'">sico_vcr.gif</xsl:when>
		<xsl:when test="$type='VOD'">sico_vod.gif</xsl:when>
		<xsl:when test="$type='VST'">sico_vst.gif</xsl:when>		
	</xsl:choose>
	</xsl:variable>
	<xsl:value-of select="$img_path"/><xsl:value-of select="$img_file"/>
	</xsl:template>
</xsl:stylesheet>
