<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<!-- customize utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	
	<xsl:variable name="lab_g_form_btn_user" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_288')" />
	<xsl:variable name="lab_g_form_btn_group" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_289')" />
	
	<!-- others -->
	<xsl:strip-space elements="*"/>
	<xsl:output indent="no" />
	<!-- ======================================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="survey_report"/>
		</html>
	</xsl:template>
	<xsl:template match="survey_report">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<style type="text/css">
			td{
				border: #CCCCCC;
			}
			</style>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:apply-templates select="survey"/>
		</body>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="survey">
		<table border="1">
			<tr>
				<td><xsl:value-of select="$lab_g_form_btn_user" /></td>
				<td><xsl:value-of select="$lab_g_form_btn_group" /></td>
				<xsl:for-each select="question">
					<xsl:call-template name="que_display"/>
				</xsl:for-each>
			</tr>
			<xsl:for-each select="attempt_usr">
				<tr>
					<xsl:call-template name="usr"/>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template name="que_display">
		<td>
			<xsl:value-of select="@order"/>. <xsl:value-of select="header/title"/>
		</td>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template name="usr" xml:space="preserve">
		<xsl:variable name="separator">&amp;</xsl:variable>
		<td>
			<xsl:value-of select="text()"/>
		</td>
		<td>
			<xsl:choose>
				<xsl:when test="count(group) = 0">Recycle Bin</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="full_path"/>
				</xsl:otherwise>
			</xsl:choose>
		</td>
		<xsl:variable name="usr_id" select="@usr_id"/>
		<xsl:for-each select="../question">
			<td>
				<xsl:variable name="my_id" select="@id"/>
				<xsl:choose>
					<xsl:when test="body/interaction/@type='MC'">
						<xsl:if test="body/interaction/@logic='SINGLE'">
							<xsl:variable name="usr_order" select="../attempt_usr[@usr_id = $usr_id]/attempt[$my_id = @que_id]/interaction/response"/>
							<xsl:value-of select="body/interaction/option[@id = $usr_order]"/>
						</xsl:if>
						<xsl:if test="body/interaction/@logic='OR'">
							<xsl:for-each select="../attempt_usr[@usr_id = $usr_id]/attempt[$my_id = @que_id]/interaction/response">
								<xsl:variable name="opt_id" select="@id"/>
								<xsl:value-of select="//../question[@id=$my_id]/body/interaction/option[@id = $opt_id]"/>;&#160;
							</xsl:for-each>	
						</xsl:if>	
					</xsl:when>
					<xsl:when test="body/interaction/@type='FB'">
						<xsl:value-of select="../attempt_usr[@usr_id = $usr_id]/attempt[$my_id=@que_id]/interaction/response"/>
					</xsl:when>
				</xsl:choose>
			</td>
		</xsl:for-each>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="cur_usr"/>
</xsl:stylesheet>
