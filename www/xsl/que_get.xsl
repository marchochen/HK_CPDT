<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/res_get_init_share.xsl"/>
	<xsl:output  indent="yes"/>
	<xsl:variable name="page_var" select="/question/page_variant" />
	<xsl:template match="/">
		<html>
			<xsl:apply-templates mode="question"/>
		</html>
	</xsl:template>
	
	<xsl:template match="question" mode="question">
	  
		<xsl:variable name="preview_function">
		<xsl:if test="$page_var[@hasEditResBtn = 'true']" > 
			<xsl:choose>
				<xsl:when test="header/objective/@type != 'SYS'">javascript:asm.preview_learning_res('<xsl:value-of select="@id"/>')</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
				</xsl:if>
		</xsl:variable>
		
		<xsl:variable name="upd_function">
		<xsl:if test="$page_var[@hasEditResBtn = 'true']" > 
			<xsl:choose>
				<xsl:when test="header/objective/@type != 'SYS'">javascript:que.edit('<xsl:value-of select="@id"/>')</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
				</xsl:if>
		</xsl:variable>

	 
		<xsl:variable name="del_function">
		<xsl:if test="$page_var[@hasRemoveResBtn = 'true']" >
			<xsl:choose>
				<xsl:when test="header/objective/@type != 'SYS'">javascript:res.del('<xsl:value-of select="@id"/>',<xsl:value-of select="header/objective/@id"/>,'<xsl:value-of select="$wb_lang"/>')</xsl:when>
				<xsl:otherwise>javascript:res.perm_del('<xsl:value-of select="@id"/>','<xsl:value-of select="$wb_lang"/>')</xsl:otherwise>
			</xsl:choose>
			   </xsl:if>
		</xsl:variable>

		<xsl:variable name="cp_function">
			<xsl:choose>
				<xsl:when test="header/objective/@type != 'SYS'">javascript:que.copy('<xsl:value-of select="@id"/>','<xsl:value-of select="$wb_lang"/>')</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:variable name="sc_add_que_function">javascript:sc.get_Que_Content('<xsl:value-of select="@id"/>', <xsl:value-of select="header/objective/@id"/>,'<xsl:value-of select="header/@type"/>');</xsl:variable>
		<xsl:call-template name="res_get">
			<xsl:with-param name="preview_function"  select="$preview_function"/>
			<xsl:with-param name="upd_function"  select="$upd_function"/>
			<xsl:with-param name="del_function" select="$del_function"/>
			<xsl:with-param name="cp_function" select="$cp_function"/>
			<xsl:with-param name="sc_add_que_function" select="$sc_add_que_function"/>
			<xsl:with-param name="body_width" select="$wb_frame_table_width"/>
		</xsl:call-template>
	</xsl:template>
</xsl:stylesheet>
