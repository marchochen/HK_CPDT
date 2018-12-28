<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:template name="get_cty_name">
		<xsl:param name="cty_code"/>
		<xsl:variable name="lab_cyt_code">
			<xsl:value-of select="concat('lab_cty_',$cty_code)"/>
		</xsl:variable>
		<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, $lab_cyt_code)"/>
	</xsl:template>
</xsl:stylesheet>