<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:import href="wb_const.xsl"/>
<xsl:variable name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:variable>
<xsl:variable name="ent_id" select="/tableofcontents/@end_id"/>
<xsl:variable name="cos_id" select="/tableofcontents/@cos_id"/>
<xsl:variable name="tkh_id" select="/tableofcontents/@tkh_id"/>
	<!-- =============================================================== -->
	<xsl:template match="/tableofcontents">
		<xsl:call-template name="tree_xml"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="tree_xml">	
		<tree srcid="{@identifier}" text="{@title}" type="mode" end_id="{$ent_id}" cos_id="{$cos_id}" tkh_id="{$tkh_id}">
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
		<xsl:if test="itemtype/text()='MOD'">javascript:module_lst.point_scorm('<xsl:value-of select="link"/>','<xsl:value-of select="$ent_id"/>','<xsl:value-of select="$cos_id"/>','<xsl:value-of select="@identifierref"/>','','<xsl:value-of select="$tkh_id"/>');</xsl:if>
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
	<xsl:variable name="is_sco">
		<xsl:choose>
			<xsl:when test="restype/text()='SCO'">YES</xsl:when>
			<xsl:otherwise>NO</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="statu">
		<xsl:value-of select="statu/text()"/>
	</xsl:variable>
	<xsl:choose>
		<xsl:when test="$tkh_id &gt; 1">
			<xsl:if test="($is_folder = 'YES' and item//restype/text()='SCO' and item//statu/text()='ON') or ($is_folder = 'NO' and $is_sco = 		'YES') and $statu ='ON'">
				<tree srcid="{@identifierref}" text="{@title}" title="{@title}" identifier="{@identifier}" itemtype="{itemtype/text()}" 		restype="{restype/text()}" is_folder="{$is_folder}" has_child="{$has_child}" icon="{$node_image}" openIcon="{$node_image}" 		action="{$str_action}" link="{link/text()}">
					<xsl:apply-templates select="item" mode="node"/>
				</tree>
			</xsl:if>	
		</xsl:when>
		<xsl:otherwise>
			<xsl:if test="($is_folder = 'YES' and item//restype/text()='SCO') or ($is_folder = 'NO' and $is_sco =	'YES')">
				<tree srcid="{@identifierref}" text="{@title}" title="{@title}" identifier="{@identifier}" itemtype="{itemtype/text()}" 		restype="{restype/text()}" is_folder="{$is_folder}" has_child="{$has_child}" icon="{$node_image}" openIcon="{$node_image}" 		action="{$str_action}" link="{link/text()}">
					<xsl:apply-templates select="item" mode="node"/>
				</tree>
			</xsl:if>			
		</xsl:otherwise>
	</xsl:choose>
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
