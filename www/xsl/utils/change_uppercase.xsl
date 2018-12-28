<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template name="change_uppercase">
	<xsl:param name="input_value"/>
	<xsl:value-of select="translate($input_value,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
</xsl:template>
</xsl:stylesheet>
