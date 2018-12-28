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
<xsl:text>
load data
infile 'D:\temp\</xsl:text>
<xsl:value-of select="$groupname"/>
<xsl:value-of select="position()"/>
<xsl:text>_</xsl:text>
<xsl:value-of select="@name"/>
<xsl:text>.txt'
badfile 'D:\temp\</xsl:text>
<xsl:value-of select="$groupname"/>
<xsl:value-of select="position()"/>
<xsl:text>_</xsl:text>
<xsl:value-of select="@name"/>
<xsl:text>_bad.txt'
append into table </xsl:text>
<xsl:value-of select="@name"/>
<xsl:text>
fields terminated by X'09'
(
</xsl:text>
		<xsl:for-each select="column">
			<xsl:value-of select="@name"/>
			<xsl:if test="@type = 'date'">
				<xsl:text> date "YYYY-MM-DD HH24:MI:SS"</xsl:text>
			</xsl:if>
			<xsl:if test="@type = 'xml'">
				<xsl:text> CHAR(2000)</xsl:text>
			</xsl:if>
			<xsl:if test="position() &lt; last()">
				<xsl:text>,</xsl:text>
			</xsl:if>
<xsl:text>
</xsl:text>
		</xsl:for-each>
<xsl:text>)
</xsl:text>
	</xsl:template>
</xsl:stylesheet>
