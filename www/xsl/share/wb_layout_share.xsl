<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="l1">
		<p>
			<blockquote>
				<xsl:number format="a."/>
				<xsl:apply-templates/>
			</blockquote>
		</p>
	</xsl:template>
	<xsl:template match="l2">
		<p>
			<blockquote>
				<xsl:number format="i."/>
				<xsl:apply-templates/>
			</blockquote>
		</p>
	</xsl:template>
	<xsl:template match="l3">
		<p>
			<blockquote>
				<xsl:number format="1."/>
				<xsl:apply-templates/>
			</blockquote>
		</p>
	</xsl:template>
</xsl:stylesheet>
