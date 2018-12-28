<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="applyeasy"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="applyeasy">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<style type="text/css"><![CDATA[
<!--
]]>
td {mso-number-format:"\@";}
<![CDATA[
-->
]]></style>
		</head>
		<body>
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="attendance_maintance">
			<xsl:with-param name="lab_attd_rate">出席率(%)</xsl:with-param>
			<xsl:with-param name="lab_remark">備註</xsl:with-param>
			<xsl:with-param name="lab_attend">已出席</xsl:with-param>
			<xsl:with-param name="lab_attendpartly">部分已出席</xsl:with-param>
			<xsl:with-param name="lab_absent">缺席</xsl:with-param>
			<xsl:with-param name="lab_learner">學員</xsl:with-param>
			<xsl:with-param name="lab_app_id">報名ID</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="attendance_maintance">
			<xsl:with-param name="lab_attd_rate">出席率(%)</xsl:with-param>
			<xsl:with-param name="lab_remark">备注</xsl:with-param>
			<xsl:with-param name="lab_attend">已出席</xsl:with-param>
			<xsl:with-param name="lab_attendpartly">部分已出席</xsl:with-param>
			<xsl:with-param name="lab_absent">缺席</xsl:with-param>
			<xsl:with-param name="lab_learner">学员</xsl:with-param>
			<xsl:with-param name="lab_app_id">报名ID</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="attendance_maintance">
			<xsl:with-param name="lab_attd_rate">Attendance rate(%)</xsl:with-param>
			<xsl:with-param name="lab_remark">Remark</xsl:with-param>
			<xsl:with-param name="lab_attend">Attended</xsl:with-param>
			<xsl:with-param name="lab_attendpartly">Partly attended</xsl:with-param>
			<xsl:with-param name="lab_absent">Absent</xsl:with-param>
			<xsl:with-param name="lab_learner">Learner</xsl:with-param>
			<xsl:with-param name="lab_app_id">Application ID</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ================================================================================ -->
	<xsl:template match="attendance_maintance">
		<xsl:param name="lab_attd_rate"/>
		<xsl:param name="lab_remark"/>
		<xsl:param name="lab_attend"/>
		<xsl:param name="lab_attendpartly"/>
		<xsl:param name="lab_absent"/>
		<xsl:param name="lab_learner"/>
		<xsl:param name="lab_app_id"/>
		<table cellspacing="0" cellpadding="3" border="1" width="{$wb_gen_table_width}">
			<tr>
				<td align="center">
					<xsl:value-of select="$lab_app_id"/>
				</td>
				<td align="center">
					<xsl:value-of select="$lab_learner"/>
				</td>
				<td align="center">
					<xsl:value-of select="$lab_attd_rate"/>
				</td>
				<td align="center">
					<xsl:value-of select="$lab_remark"/>
				</td>
			</tr>
			<xsl:apply-templates select="attendance_list/attendance">
				<xsl:with-param name="lab_attend" select="$lab_attend"/>
				<xsl:with-param name="lab_absent" select="$lab_absent"/>
				<xsl:with-param name="lab_attendpartly" select="$lab_attendpartly"/>
				<xsl:with-param name="lab_remark" select="$lab_remark"/>
			</xsl:apply-templates>
		</table>
	</xsl:template>
	<!-- ========================================================================= -->
	<xsl:template match="attendance">
		<xsl:param name="lab_attend"/>
		<xsl:param name="lab_absent"/>
		<xsl:param name="lab_attendpartly"/>
		<xsl:param name="lab_remark"/>
		<xsl:param name="att_rate">
			<xsl:value-of select="number(substring-before(@rate,'.'))"/>
		</xsl:param>
		<tr>
			<td>
				<xsl:value-of select="@app_id"/>
			</td>
			<td>
				<xsl:value-of select="user/name/@display_name"/>
			</td>
			<td align="left">
				<xsl:choose>
					<xsl:when test="@rate=''">--</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$att_rate"/></xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="left">
				<xsl:choose>
					<xsl:when test="att_rate_remark != ''">
						<xsl:call-template name="unescape_html_linefeed">
							<xsl:with-param name="my_right_value">
								<xsl:value-of select="att_rate_remark"/>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>--</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
	<!-- ===================================================================== -->
</xsl:stylesheet>
