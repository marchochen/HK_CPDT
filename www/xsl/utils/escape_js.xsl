<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="escape_backslash.xsl"/>
	<xsl:import href="escape_sing_quo.xsl"/>
	<xsl:import href="escape_doub_quo.xsl"/>

<xsl:template name="escape_js">
	<xsl:param name="input_str"/>
	<xsl:variable name="next_str"><xsl:call-template name="escape_backslash"><xsl:with-param name="my_right_value"><xsl:value-of select="$input_str"/></xsl:with-param><xsl:with-param name="my_left_value"/></xsl:call-template></xsl:variable>
	<xsl:variable name="next_next_str"><xsl:call-template name="escape_sing_quo"><xsl:with-param name="my_right_value"><xsl:value-of select="$next_str"/></xsl:with-param><xsl:with-param name="my_left_value"/></xsl:call-template></xsl:variable>
	<xsl:variable name="next_next_next_str"><xsl:call-template name="escape_doub_quo"><xsl:with-param name="my_right_value"><xsl:value-of select="$next_next_str"/></xsl:with-param><xsl:with-param name="my_left_value"/></xsl:call-template></xsl:variable>
	<xsl:value-of select="$next_next_next_str"/>
</xsl:template>
</xsl:stylesheet>
