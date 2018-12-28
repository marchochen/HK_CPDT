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
		<xsl:text>load from D:\temp\</xsl:text>
		<xsl:value-of select="$groupname"/>
		<xsl:value-of select="position()"/>
		<xsl:text>_</xsl:text>
		<xsl:value-of select="@name"/>
		<xsl:text>.txt of del modified by </xsl:text>
		<xsl:if test="@identity != ''">
			<xsl:text>identityoverride </xsl:text>
		</xsl:if>
		<xsl:text>coldel0x09 messages D:\temp\</xsl:text>
		<xsl:value-of select="$groupname"/>
		<xsl:value-of select="position()"/>
		<xsl:text>_</xsl:text>
		<xsl:value-of select="@name"/>
		<xsl:text>.log insert into </xsl:text>
		<xsl:value-of select="@name"/>
		<xsl:text> (</xsl:text>
		<xsl:for-each select="column">
			<xsl:value-of select="@name"/>
			<xsl:if test="position() &lt; last()">
				<xsl:text>, </xsl:text>
			</xsl:if>
		</xsl:for-each>
		<xsl:text>)</xsl:text>
<xsl:text>
</xsl:text>
		<xsl:if test="@has_foreign_key = 'true'">
			<xsl:text>SET INTEGRITY FOR </xsl:text>
			<xsl:value-of select="@name"/>
			<xsl:text> IMMEDIATE CHECKED</xsl:text>
<xsl:text>
</xsl:text>
		</xsl:if>
		<xsl:if test="@identity != ''">
			<xsl:variable name="identity_name">
				<xsl:value-of select="@identity"/>
			</xsl:variable>
			<xsl:text>ALTER TABLE </xsl:text>
			<xsl:value-of select="@name"/>
			<xsl:text> ALTER </xsl:text>
			<xsl:value-of select="$identity_name"/>
			<xsl:text> RESTART WITH </xsl:text>
			<xsl:value-of select="column[@name=$identity_name]/@start_value + ../@count"/>
<xsl:text>
</xsl:text>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
