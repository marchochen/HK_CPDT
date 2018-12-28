<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<!--custom-->
	<xsl:import href="share/label_for_eval_mgt.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="evalmanagement"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="evalmanagement">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<style>
				<![CDATA[
					<!--
					td	{mso-number-format:"\@";}
					-->
				]]>
			</style>
		</head>
		<body>
			<table cellspacing="0" cellpadding="3" border="1" width="{$wb_gen_table_width}">
			<tr>
				<td><xsl:value-of select="$lab_app_id"/></td>
				<td><xsl:value-of select="$lab_lrn"/></td>
				<xsl:for-each select ="scoring_item[@type='offline']">
					<td><xsl:value-of select="@cmt_title"/></td>
				</xsl:for-each>
			</tr>
				<xsl:call-template name="wb_init_lab"/>
			</table>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="scoring_item[@type='offline'][position() = 1]"/>
<!--
		<xsl:for-each select="scoring_item[@type='offline']">
			<xsl:if test="position() = 1">
				<xsl:apply-templates select="."/>
			</xsl:if>
		</xsl:for-each>
-->	
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="scoring_item[@type='offline'][position() = 1]"/>
<!--
		<xsl:for-each select="scoring_item[@type='offline']">
			<xsl:if test="position() = 1">
				<xsl:apply-templates select="."/>
			</xsl:if>
		</xsl:for-each>
-->	
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="scoring_item[@type='offline'][position() = 1]"/>
<!--
		<xsl:for-each select="scoring_item[@type='offline']">
			<xsl:if test="position() = 1">
				<xsl:apply-templates select="."/>
			</xsl:if>
		</xsl:for-each>
-->	
	</xsl:template>
	<!-- ================================================================================ -->
	<xsl:template match="scoring_item">
		<xsl:apply-templates select="eval_item"/>
	</xsl:template>
	<!-- ========================================================================= -->
	<xsl:template match="eval_item">
		<xsl:variable name="app_id" select= "@app_id"/>
		<tr>
			<td><xsl:value-of select="@app_id"/></td>
			<td><xsl:value-of select="@lrn_name"/></td>
			<xsl:for-each select="/evalmanagement/scoring_item[@type='offline']/eval_item[@app_id=$app_id]">
				<td>
					<xsl:choose>
						<xsl:when test="@cmt_score=''">--</xsl:when>
						<xsl:otherwise><xsl:number format="0" value="@cmt_score"/></xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:for-each>
		</tr>
	</xsl:template>
	<!-- ===================================================================== -->
</xsl:stylesheet>
