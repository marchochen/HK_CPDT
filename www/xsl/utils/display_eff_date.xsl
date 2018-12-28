<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="display_time.xsl"/>
	<xsl:import href="display_hhmm.xsl"/>

	<!-- ==================================================================== -->
	<xsl:template name="display_eff_date">
		<xsl:param name="from_timestamp"/>
		<xsl:param name="to_timestamp"/>
		<xsl:param name="dis_time"/>
		<xsl:param name="isEnrollment_related">true</xsl:param>
		<xsl:variable name="from_time"><xsl:value-of select="translate($from_timestamp,':- ','')"/></xsl:variable>
		<xsl:variable name="from_year"><xsl:value-of select="substring($from_time,1,8)"/></xsl:variable>
		<xsl:variable name="to_time"><xsl:value-of select="translate($to_timestamp,':- ','')"/></xsl:variable>
		<xsl:variable name="to_year"><xsl:value-of select="substring($to_time,1,8)"/></xsl:variable>
		<xsl:choose>
			<xsl:when test="$isEnrollment_related = 'false'">
				<xsl:text>--</xsl:text>
			</xsl:when>
			<xsl:when test="($from_timestamp = '') and ($to_timestamp = '')">
				<xsl:choose>
					<xsl:when test="$wb_lang = 'gb'">没有生效日期</xsl:when>
					<xsl:when test="$wb_lang = 'ch'">有生效日期</xsl:when>
					<xsl:otherwise>No effective date</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$to_year =$from_year">
						<!-- same day-->
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp"><xsl:value-of select="$from_timestamp"/></xsl:with-param>
						</xsl:call-template>
						<xsl:if test="not((substring($from_time,9,4) = '0000') and (substring($to_time,9,4) = '2359')) and $dis_time = 'T'">
							<xsl:text>&#160;</xsl:text>
							<xsl:call-template name="display_hhmm">
								<xsl:with-param name="my_timestamp"><xsl:value-of select="$from_timestamp"/></xsl:with-param>
							</xsl:call-template>
							<xsl:value-of select="$lab_const_to"/>
							<xsl:call-template name="display_hhmm">
								<xsl:with-param name="my_timestamp"><xsl:value-of select="$to_timestamp"/></xsl:with-param>
							</xsl:call-template>
						</xsl:if>
					</xsl:when>
					<xsl:when test="$to_timestamp = 'UNLIMITED'">
						<!-- unlimited end date-->
						<xsl:value-of select="$lab_const_from"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp"><xsl:value-of select="$from_timestamp"/></xsl:with-param>
						</xsl:call-template>
						<xsl:if test="not(substring($from_time,9,4) = '0000') and $dis_time = 'T'">
							<xsl:text>&#160;</xsl:text>
							<xsl:call-template name="display_hhmm">
								<xsl:with-param name="my_timestamp"><xsl:value-of select="$from_timestamp"/></xsl:with-param>
							</xsl:call-template>
						</xsl:if>
						<xsl:value-of select="$lab_const_up"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp"><xsl:value-of select="$from_timestamp"/></xsl:with-param>
						</xsl:call-template>
						<xsl:if test="not(substring($from_time,9,4) = '0000') and $dis_time = 'T'">
							<xsl:text>&#160;</xsl:text>
							<xsl:call-template name="display_hhmm">
								<xsl:with-param name="my_timestamp"><xsl:value-of select="$from_timestamp"/></xsl:with-param>
							</xsl:call-template>
						</xsl:if>
						<xsl:text>&#160;</xsl:text><xsl:value-of select="$lab_const_to"/><xsl:text>&#160;</xsl:text>
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp"><xsl:value-of select="$to_timestamp"/></xsl:with-param>
						</xsl:call-template>
						<xsl:if test="not(substring($to_time,9,4) = '2359') and $dis_time = 'T'">
							<xsl:text>&#160;</xsl:text>
							<xsl:call-template name="display_hhmm">
								<xsl:with-param name="my_timestamp"><xsl:value-of select="$to_timestamp"/></xsl:with-param>
							</xsl:call-template>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
