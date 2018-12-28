<?xml version='1.0' encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- Welcome to wizBank Version 3.5 ================================================ -->
	<!-- Depreciated Variable ======================================================= -->
	<xsl:variable name="wb_lab_head_course_manager" />
	<xsl:variable name="wb_lab_head_resource_manager" />
	<xsl:variable name="wb_lab_head_user_information" />

	<xsl:template name="wb_utils_label">
		<xsl:param name="key" />
		<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, $key)" />
	</xsl:template>
</xsl:stylesheet> 