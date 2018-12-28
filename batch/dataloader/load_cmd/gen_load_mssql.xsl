<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text"/>
	<xsl:template match="/data_loader">
		<xsl:for-each select="table_group[@enabled = 'true']">
			<xsl:apply-templates select="table">
				<xsl:with-param name="groupname" select="@name"/>
			</xsl:apply-templates>
		</xsl:for-each>
	</xsl:template>
	<!-- ===== -->
	<xsl:template match="table">
		<xsl:param name="groupname"/>
		<xsl:text>bcp wbv50..</xsl:text>
		<xsl:value-of select="@name"/>
		<xsl:text> in D:\temp\</xsl:text>
		<xsl:value-of select="$groupname"/>
		<xsl:value-of select="position()"/>
		<xsl:text>_</xsl:text>
		<xsl:value-of select="@name"/>
		<xsl:text>.txt -f D:\temp\</xsl:text>
		<xsl:value-of select="$groupname"/>
		<xsl:value-of select="position()"/>
		<xsl:text>_</xsl:text>
		<xsl:value-of select="@name"/>
		<xsl:text>.fmt -E -S ws37 -U cyber -P wisdom</xsl:text>
<xsl:text>
</xsl:text>
	</xsl:template>
</xsl:stylesheet>
