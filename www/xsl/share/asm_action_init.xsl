<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template name="asm_action">
		<xsl:param name="mode"/>
		<xsl:param name="save_function"/>
		<xsl:param name="cancel_function"/>
		<xsl:param name="width"/>
		<xsl:param name="header"/>
		<xsl:param name="url_success"/>
		<xsl:param name="url_failure"/>
		<xsl:param name="type"/>
		<xsl:call-template name="asm_html_header">
			<xsl:with-param name="mode">
				<xsl:value-of select="$mode"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:variable name="src">
			<xsl:call-template name="escape_backslash">
				<xsl:with-param name="my_right_value">
					<xsl:value-of select="/resource/body/source"/>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="page_title">
			<xsl:choose>
			<xsl:when  test="$type='FAS'">
						<xsl:choose>
						<xsl:when test="$mode='INS'">
							<xsl:value-of select="$lab_operate_add"/><xsl:value-of select="$lab_fas"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_operate_upd"/><xsl:value-of select="$lab_fas"/>
						</xsl:otherwise>
						</xsl:choose>
			</xsl:when>
			<xsl:when test="$type='DAS'">
				<xsl:choose>
						<xsl:when  test="$mode='INS'">
							<xsl:value-of select="$lab_operate_add"/><xsl:value-of select="$lab_das"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_operate_upd"/><xsl:value-of select="$lab_das"/>
						</xsl:otherwise>
						</xsl:choose>
			</xsl:when>
		</xsl:choose>
		</xsl:variable>
		
		<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0" onLoad="init()">
			<form name="frmXml" onsubmit="return false">
				<input type="hidden" name="rename" value="no"/>
				
				<xsl:call-template name="asm_additional_information">
					
					<xsl:with-param name="page_title">
						<xsl:value-of select="$page_title"/>
					</xsl:with-param>
					<xsl:with-param name="width">
						<xsl:value-of select="$width"/>
					</xsl:with-param>
					<xsl:with-param name="mode">
						<xsl:value-of select="$mode"/>
					</xsl:with-param>
					<xsl:with-param name="save_function">
						<xsl:value-of select="$save_function"/>
					</xsl:with-param>
					<xsl:with-param name="cancel_function">
						<xsl:value-of select="$cancel_function"/>
					</xsl:with-param>
					<xsl:with-param name="type">
						<xsl:value-of select="$type"/>
					</xsl:with-param>
				</xsl:call-template>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>
