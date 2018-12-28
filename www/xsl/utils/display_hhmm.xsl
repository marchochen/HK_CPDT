<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!-- Display HH:MM of Timestamp -->
<xsl:template name="display_hhmm">
	<xsl:param name="my_timestamp"/>
	<xsl:param name="mode"/>
	<xsl:param name="use_label_time_separator">yes</xsl:param>
	<xsl:variable name="tmp_timestamp" select="translate($my_timestamp,':- ','')"/>
	<xsl:variable name="my_hrs" select="substring($tmp_timestamp,9,2)"/>
	<xsl:variable name="my_mins" select="substring($tmp_timestamp,11,2)"/>
	<xsl:variable name="lab_time_const_hrs">
		<xsl:choose>
			<xsl:when test="$use_label_time_separator = 'no'">:</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$lab_const_hrs"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_time_lab_const_mins">
		<xsl:choose>
			<xsl:when test="$use_label_time_separator = 'no'"></xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$lab_const_mins"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:choose>
		<!-- JavaScript -->
		<xsl:when test="$mode = 'js'">
			<script language="javascript"><![CDATA[
				var time_str = ''
				time_str += ']]><xsl:value-of select="$my_hrs"/><![CDATA['
				time_str += ']]><xsl:value-of select="$lab_time_const_hrs"/><![CDATA['
				time_str += ']]><xsl:value-of select="$my_mins"/><![CDATA['
				time_str += ']]><xsl:value-of select="$lab_time_lab_const_mins"/><![CDATA['
				document.write(time_str);
			]]></script>
		</xsl:when>
		<xsl:otherwise>
			<xsl:if test="$tmp_timestamp != ''">			
				<xsl:value-of select="$my_hrs"/>				
				<xsl:value-of select="$lab_time_const_hrs"/>
				<xsl:value-of select="$my_mins"/>
				<xsl:value-of select="$lab_time_lab_const_mins"/>
			</xsl:if>	
		</xsl:otherwise>
	</xsl:choose>	
</xsl:template>	

<xsl:template name="display_hhmm_sample">
	<xsl:param name="my_timestamp"/>
	<xsl:param name="mode"/>
	<xsl:param name="use_label_time_separator">yes</xsl:param>
	<xsl:variable name="tmp_timestamp" select="translate($my_timestamp,':- ','')"/>
	<xsl:variable name="my_hrs" select="substring($tmp_timestamp,9,2)"/>
	<xsl:variable name="my_mins" select="substring($tmp_timestamp,11,2)"/>
	<xsl:variable name="lab_time_const_hrs">:</xsl:variable>
	<xsl:variable name="lab_time_lab_const_mins"></xsl:variable>
	<xsl:choose>
		<!-- JavaScript -->
		<xsl:when test="$mode = 'js'">
			<script language="javascript"><![CDATA[
				var time_str = ''
				time_str += ']]><xsl:value-of select="$my_hrs"/><![CDATA['
				time_str += ']]><xsl:value-of select="$lab_time_const_hrs"/><![CDATA['
				time_str += ']]><xsl:value-of select="$my_mins"/><![CDATA['
				time_str += ']]><xsl:value-of select="$lab_time_lab_const_mins"/><![CDATA['
				document.write(time_str);
			]]></script>
		</xsl:when>
		<xsl:otherwise>
			<xsl:if test="$tmp_timestamp != ''">			
				<xsl:value-of select="$my_hrs"/>				
				<xsl:value-of select="$lab_time_const_hrs"/>
				<xsl:value-of select="$my_mins"/>
				<xsl:value-of select="$lab_time_lab_const_mins"/>
			</xsl:if>	
		</xsl:otherwise>
	</xsl:choose>	
</xsl:template>	
</xsl:stylesheet>
