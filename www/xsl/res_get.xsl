<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="share/res_get_init_share.xsl"/>
	<xsl:output  indent="yes"/>
	<xsl:variable name="page_var" select="/resource/page_variant" />
	<xsl:variable name="share_mode" select="/resource/share_mode/text()"/>
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<xsl:template match="resource">
		<xsl:variable name="upd_function">
		   <xsl:if test="$page_var[@hasEditResBtn = 'true'] and $share_mode!='true'" > 
			<xsl:choose>
				<xsl:when test="header/objective/@type != 'SYS'">
					<xsl:choose>
						<xsl:when test="header/@type ='AICC'">javascript:aicc.upd_prep('<xsl:value-of select="@id"/>')</xsl:when>
						<xsl:when test="header/@type ='SCORM'">javascript:wiz.upd_res_scorm('<xsl:value-of select="@id"/>')</xsl:when>
						<xsl:when test="header/@type ='NETGCOK'">javascript:cos.upd_res_netg('<xsl:value-of select="@id"/>')</xsl:when>
						<xsl:otherwise>javascript:res.upd_prep('<xsl:value-of select="@id"/>')</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
		  </xsl:if>	
		</xsl:variable>
		<xsl:variable name="del_function">
		  <xsl:if test="$page_var[@hasRemoveResBtn = 'true'] and $share_mode!='true'" > 
			<xsl:choose>
				<xsl:when test="header/objective/@type != 'SYS'">javascript:res.del('<xsl:value-of select="@id"/>',<xsl:value-of select="header/objective/@id"/>,'<xsl:value-of select="$wb_lang"/>')</xsl:when>
				<xsl:otherwise>javascript:res.perm_del('<xsl:value-of select="@id"/>','<xsl:value-of select="$wb_lang"/>')</xsl:otherwise>
			</xsl:choose>
			</xsl:if>
		</xsl:variable>
		<xsl:variable name="cp_function">
			<xsl:choose>
				<xsl:when test="header/objective/@type != 'SYS'">
					<xsl:choose>
				<xsl:when test="header/@type ='AICC'">
					javascript:aicc.copy('<xsl:value-of select="@id"/>','<xsl:value-of select="header/@type"/>','<xsl:value-of select="$wb_lang"/>')
				</xsl:when>
						<xsl:otherwise>
					javascript:res.copy('<xsl:value-of select="@id"/>','<xsl:value-of select="header/@type"/>','<xsl:value-of select="$wb_lang"/>')
				</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
		</xsl:variable>
		<!--<xsl:choose>-->
			<!--<xsl:when test="//acl[@read = 'true']">-->
				<xsl:call-template name="res_get">
					<xsl:with-param name="upd_function"  select="$upd_function"/>
					<xsl:with-param name="del_function"  select="$del_function"/>
					<xsl:with-param name="cp_function"  select="$cp_function"/>
					<xsl:with-param name="header">YES</xsl:with-param>
					<xsl:with-param name="body_width" select="$wb_frame_table_width"/>
				</xsl:call-template>
			<!--</xsl:when>
		</xsl:choose>-->
	</xsl:template>
</xsl:stylesheet>
