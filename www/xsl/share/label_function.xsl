<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:template name="get_ftn_title">
		<xsl:param name="ftn_ext_id">
			<xsl:value-of select="@id"/>
		</xsl:param>
		<xsl:variable name="lab_ftn_id">
			<xsl:value-of select="concat('lab_ftn_',$ftn_ext_id)"/>
		</xsl:variable>
		<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, $lab_ftn_id)"/>
	</xsl:template>
</xsl:stylesheet>