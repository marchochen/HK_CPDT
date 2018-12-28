<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="display_eng_month_format.xsl"/>
	<xsl:import href="display_hhmm.xsl"/>
	<xsl:import href="display_hhmmss.xsl"/>

<!-- ==================================================================== -->
<!-- Display Timestamp -->
<xsl:template name="display_time">
	<xsl:param name="my_timestamp"/>
	<xsl:param name="dis_time"/>
	<xsl:param name="mode"/>
	<xsl:param name="use_label_time_separator">no</xsl:param>
	<xsl:param name="date_format">4</xsl:param><!-- { [1] 31-12-2002 | [2] 31 Dec 2002 | [3] 2002 Dec 31 | [4] 2002-12-31} -->
	<xsl:variable name="_tmp_timestamp" select="translate($my_timestamp,':- ','')"/>
	<xsl:variable name="my_year" select="number(substring($_tmp_timestamp,1,4))"/>
	<xsl:variable name="my_month">
		<xsl:choose>
			<xsl:when test="string-length(number(substring($_tmp_timestamp,5,2))) = 1">0<xsl:value-of select="number(substring($_tmp_timestamp,5,2))"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="number(substring($_tmp_timestamp,5,2))"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="my_day">
		<xsl:choose>
			<xsl:when test="string-length(number(substring($_tmp_timestamp,7,2))) = 1">0<xsl:value-of select="number(substring($_tmp_timestamp,7,2))"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="number(substring($_tmp_timestamp,7,2))"/></xsl:otherwise>
		</xsl:choose>	
	</xsl:variable>
	<xsl:choose>
		<!-- JavaScript -->
		<xsl:when test="$mode = 'js'">
			<xsl:if test="$my_timestamp != ''">
				<xsl:choose>
					<!-- [1] -->
					<xsl:when test="$date_format = '1'">						
						<script language="javascript"><![CDATA[
							var date_str = ''
							date_str += ']]><xsl:value-of select="$my_day"/><![CDATA['
							date_str += '-'
							date_str += ']]><xsl:value-of select="$my_month"/><![CDATA['
							date_str += '-'
							date_str += ']]><xsl:value-of select="$my_year"/><![CDATA['
							]]><xsl:if test="$dis_time = 'T'"><![CDATA[date_str += ' ']]></xsl:if><![CDATA[
							document.write(date_str);
						]]></script>
					</xsl:when>
					<!-- [2] -->
					<xsl:when test="$date_format = '2'">						
						<script language="javascript"><![CDATA[
							var date_str = ''
							date_str += ']]><xsl:value-of select="$my_day"/><![CDATA[ '
							date_str += ']]><xsl:call-template name="display_eng_month_format"><xsl:with-param name="month"><xsl:value-of select="$my_month"/></xsl:with-param></xsl:call-template><![CDATA[ '
							date_str += ']]><xsl:value-of select="$my_year"/><![CDATA['
							]]><xsl:if test="$dis_time = 'T'"><![CDATA[date_str += ' ']]></xsl:if><![CDATA[
							document.write(date_str);
						]]></script>
					</xsl:when>
					<!-- [3] -->
					<xsl:when test="$date_format = '3'">						
						<script language="javascript"><![CDATA[
							var date_str = ''
							date_str += ']]><xsl:value-of select="$my_year"/><![CDATA[ '
							date_str += ']]><xsl:call-template name="display_eng_month_format"><xsl:with-param name="month"><xsl:value-of select="$my_month"/></xsl:with-param></xsl:call-template><![CDATA[ '
							date_str += ']]><xsl:value-of select="$my_day"/><![CDATA['
							]]><xsl:if test="$dis_time = 'T'"><![CDATA[date_str += ' ']]></xsl:if><![CDATA[
							document.write(date_str);
						]]></script>
					</xsl:when>
					<!-- [4] -->
					<xsl:when test="$date_format = '4'">
						<script language="javascript"><![CDATA[
							var date_str = ''
							date_str += ']]><xsl:value-of select="$my_year"/><![CDATA['
							date_str += '-'
							date_str += ']]><xsl:value-of select="$my_month"/><![CDATA['
							date_str += '-'
							date_str += ']]><xsl:value-of select="$my_day"/><![CDATA['
							]]><xsl:if test="$dis_time = 'T'"><![CDATA[date_str += ' ']]></xsl:if><![CDATA[
							document.write(date_str);
						]]></script>
					</xsl:when>
				</xsl:choose>
			</xsl:if>
			<!-- display HH:MM -->
			<xsl:if test="$dis_time = 'T'">
				<xsl:call-template name="display_hhmm">
					<xsl:with-param name="my_timestamp"><xsl:value-of select="$my_timestamp"/></xsl:with-param>
					<xsl:with-param name="mode"><xsl:value-of select="$mode"/></xsl:with-param>
					<xsl:with-param name="use_label_time_separator"><xsl:value-of select="$use_label_time_separator"/></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
		</xsl:when>
		<!-- CSV -->
		<xsl:when test="$mode = 'csv'">			
			<xsl:if test="$my_timestamp != ''">
				<xsl:choose>
					<!-- [1] -->
					<xsl:when test="$date_format = '1'">
						<xsl:value-of select="$my_day"/>
						<xsl:text>-</xsl:text>
						<xsl:value-of select="$my_month"/>
						<xsl:text>-</xsl:text>
						<xsl:value-of select="$my_year"/>
					</xsl:when>
					<!-- [2] -->
					<xsl:when test="$date_format = '2'">
						<xsl:value-of select="$my_day"/>
						<xsl:text> </xsl:text>
						<xsl:call-template name="display_eng_month_format"><xsl:with-param name="month"><xsl:value-of select="$my_month"/></xsl:with-param></xsl:call-template>
						<xsl:text> </xsl:text>
						<xsl:value-of select="$my_year"/>
					</xsl:when>
					<!-- [3] -->
					<xsl:when test="$date_format = '3'">
						<xsl:value-of select="$my_year"/>
						<xsl:text> </xsl:text>
						<xsl:call-template name="display_eng_month_format"><xsl:with-param name="month"><xsl:value-of select="$my_month"/></xsl:with-param></xsl:call-template>
						<xsl:text> </xsl:text>
						<xsl:value-of select="$my_day"/>
					</xsl:when>
					<!-- [4] -->
					<xsl:when test="$date_format = '4'">
						<xsl:value-of select="$my_year"/>
						<xsl:text>-</xsl:text>
						<xsl:value-of select="$my_month"/>
						<xsl:text>-</xsl:text>
						<xsl:value-of select="$my_day"/>
					</xsl:when>
				</xsl:choose>				
			</xsl:if>
			<!-- display HH:MM -->
			<xsl:if test="$dis_time = 'T'">
				<xsl:text> </xsl:text>
				<xsl:call-template name="display_hhmm">
				<xsl:with-param name="my_timestamp"><xsl:value-of select="$my_timestamp"/></xsl:with-param>
				<xsl:with-param name="mode"><xsl:value-of select="$mode"/></xsl:with-param>
				<xsl:with-param name="use_label_time_separator"><xsl:value-of select="$use_label_time_separator"/></xsl:with-param>
				</xsl:call-template>
			</xsl:if>							
			<!-- display HH:MM:SS -->
			<xsl:if test="$dis_time = 'HHMMSS'">
				<xsl:text> </xsl:text>
				<xsl:call-template name="display_hhmmss">
					<xsl:with-param name="my_timestamp"><xsl:value-of select="$my_timestamp"/></xsl:with-param>
					<xsl:with-param name="use_label_time_separator"><xsl:value-of select="$use_label_time_separator"/></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
		</xsl:when>
		<!-- HTML -->
		<xsl:otherwise>
			<xsl:if test="$my_timestamp != ''">
				<xsl:choose>
					<!-- [1] -->
					<xsl:when test="$date_format = '1'">
						<xsl:value-of select="$my_day"/>
						<xsl:text>-</xsl:text>
						<xsl:value-of select="$my_month"/>
						<xsl:text>-</xsl:text>
						<xsl:value-of select="$my_year"/>
					</xsl:when>
					<!-- [2] -->
					<xsl:when test="$date_format = '2'">
						<xsl:value-of select="$my_day"/><xsl:text>&#160;</xsl:text>
						<xsl:call-template name="display_eng_month_format"><xsl:with-param name="month"><xsl:value-of select="$my_month"/></xsl:with-param></xsl:call-template>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$my_year"/>
					</xsl:when>
					<!-- [3] -->
					<xsl:when test="$date_format = '3'">
						<xsl:value-of select="$my_year"/><xsl:text>&#160;</xsl:text>
						<xsl:call-template name="display_eng_month_format"><xsl:with-param name="month"><xsl:value-of select="$my_month"/></xsl:with-param></xsl:call-template>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$my_day"/>
					</xsl:when>
					<!-- [4] -->
					<xsl:when test="$date_format = '4'">
						<xsl:value-of select="$my_year"/>
						<xsl:text>-</xsl:text>
						<xsl:value-of select="$my_month"/>
						<xsl:text>-</xsl:text>
						<xsl:value-of select="$my_day"/>
					</xsl:when>
				</xsl:choose>				
				<!-- display HH:MM -->
				<xsl:if test="$dis_time = 'T'">
					<xsl:text>&#160;</xsl:text>
					<xsl:call-template name="display_hhmm">
						<xsl:with-param name="my_timestamp"><xsl:value-of select="$my_timestamp"/></xsl:with-param>
						<xsl:with-param name="use_label_time_separator"><xsl:value-of select="$use_label_time_separator"/></xsl:with-param>
					</xsl:call-template>
				</xsl:if>
				<!-- display HH:MM:SS -->
				<xsl:if test="$dis_time = 'HHMMSS'">
					<xsl:text>&#160;</xsl:text>
					<xsl:call-template name="display_hhmmss">
						<xsl:with-param name="my_timestamp"><xsl:value-of select="$my_timestamp"/></xsl:with-param>
						<xsl:with-param name="use_label_time_separator"><xsl:value-of select="$use_label_time_separator"/></xsl:with-param>
					</xsl:call-template>
				</xsl:if>
			</xsl:if>				
		</xsl:otherwise>
	</xsl:choose>		
</xsl:template>
</xsl:stylesheet>
