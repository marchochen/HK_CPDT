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
<xsl:value-of select="$groupname"/>
<xsl:value-of select="position()"/>
<xsl:text>_</xsl:text>
<xsl:value-of select="@name"/>
<xsl:text>.fmt
</xsl:text>
<xsl:text>8.0
</xsl:text>
<xsl:value-of select="count(column)"/>
<xsl:text>
</xsl:text>
		<xsl:for-each select="column">
			<xsl:value-of select="position()"/>
			<xsl:text>	SQLNCHAR	0	0	</xsl:text>
			<xsl:choose>
				<xsl:when test="position() = last()">
					<xsl:text>"\r\0\n\0"	</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>"\t\0"		</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:value-of select="@order"/>
			<xsl:text>	</xsl:text>
			<xsl:value-of select="@name"/>
<xsl:text>	""
</xsl:text>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
