<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>		
	<xsl:import href="share/res_get_init_share.xsl"/>
	<xsl:variable name="isLrn">
		<xsl:choose>
			<xsl:when test="/resource/cur_usr/granted_functions/functions/function[@id = 'LRN_RES_CENTER']">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
 	</xsl:variable>	
	<xsl:output  indent="yes"/>
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<xsl:template match="resource">
		<xsl:variable name="upd_function">javascript:res.edit_in_search('<xsl:value-of select="/resource/@id"/>','<xsl:value-of select="/resource/header/@subtype"/>')</xsl:variable>
		<xsl:variable name="del_function">javascript:res.del_in_search('<xsl:value-of select="/resource/@id"/>','<xsl:value-of select="/resource/header/objective/@id"/>','<xsl:value-of select="$wb_lang"/>')	</xsl:variable>
		<xsl:variable name="cp_function">javascript:aicc.copy('<xsl:value-of select="/resource/@id"/>','<xsl:value-of select="/resource/header/@type"/>','<xsl:value-of select="$wb_lang"/>')</xsl:variable>
             <xsl:call-template name="res_get">
					<xsl:with-param name="upd_function" >
						<xsl:choose>
							<xsl:when test="/resource/page_variant/@hasEditResBtn ='true'and $isLrn='false'">
								<xsl:value-of select="$upd_function"/>
							</xsl:when>
							<xsl:otherwise></xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
					<xsl:with-param name="del_function" >
						<xsl:choose>
							<xsl:when test="/resource/page_variant/@hasRemoveResBtn='true' and $isLrn='false'">
								<xsl:value-of select="$del_function"/>
							</xsl:when>
						</xsl:choose>
					</xsl:with-param>
					<xsl:with-param name="cp_function">
						<xsl:choose>
							<xsl:when test="$isLrn='false'">
								<xsl:value-of   select="$cp_function"/>
						 	</xsl:when>
						 	<xsl:otherwise/>
						</xsl:choose>
					</xsl:with-param>
					<xsl:with-param name="header">YES</xsl:with-param>
					<xsl:with-param name="body_width" select="$wb_gen_table_width"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
</xsl:stylesheet>
