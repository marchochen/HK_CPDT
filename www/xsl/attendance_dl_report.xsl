<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<!-- customize utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- others -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="utils/display_score.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:output/>
	<!--============================================================================-->
	<xsl:variable name="create_session_ind" select="/applyeasy/attendance_maintance/item/@create_session_ind"/>
	<xsl:variable name="session_ind" select="/applyeasy/attendance_maintance/item/@session_ind"/>
	<xsl:variable name="is_integrated" select="/applyeasy/attendance_maintance/item/@itm_integrated_ind"/>
	<xsl:variable name="itm_type" select="/applyeasy/attendance_maintance/item/item_type/@id"/>
	<xsl:variable name="lab_862" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '862')"/>
	<!-- ======================================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="applyeasy"/>
		</html>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="applyeasy">
		<xsl:apply-templates select="attendance_maintance"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="attendance_maintance">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<style type="text/css">
<![CDATA[
<!--
]]>
table{border:none}
td {mso-number-format:"\@";border-color:#D0D0D0;white-space: nowrap;}
.trNormal td{border-right-width:0.6px;border-right-style:solid;padding-top:5px;}
.trHeader td{text-align:center;background-color:#bfbfbf;border-right-width:0.6px;border-right-color:black;border-right-style:solid}
.trHeaderFontStyle {margin-top:5px}
<![CDATA[
-->
]]>
</style>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_session_attd">
				<xsl:value-of select="$lab_const_session"/>到課率</xsl:with-param>
			<xsl:with-param name="lab_remark">備註</xsl:with-param>
			<xsl:with-param name="lab_score">分數</xsl:with-param>
			<xsl:with-param name="lab_status">學習狀態</xsl:with-param>
			<xsl:with-param name="lab_attendance">出席率</xsl:with-param>
			<xsl:with-param name="lab_other_criteria">已滿足學習模塊條件</xsl:with-param>
			<xsl:with-param name="lab_completion_date">結訓日期</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_session_attd">
				<xsl:value-of select="$lab_const_session"/>到课率</xsl:with-param>
			<xsl:with-param name="lab_remark">备注</xsl:with-param>
			<xsl:with-param name="lab_score">分数</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_attendance">出席率</xsl:with-param>
			<xsl:with-param name="lab_other_criteria">已满足学习模块条件</xsl:with-param>
			<xsl:with-param name="lab_completion_date">结训日期</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_session_attd">
				<xsl:value-of select="$lab_const_session"/> attendance rate</xsl:with-param>
			<xsl:with-param name="lab_remark">Remark</xsl:with-param>
			<xsl:with-param name="lab_score">Score</xsl:with-param>
			<xsl:with-param name="lab_status">Learning status</xsl:with-param>
			<xsl:with-param name="lab_attendance">Attendance(%)</xsl:with-param>
			<xsl:with-param name="lab_other_criteria">Fulfilled learning module criteria</xsl:with-param>
			<xsl:with-param name="lab_completion_date">Completion date</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_session_attd"/>
		<xsl:param name="lab_remark"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_attendance"/>
		<xsl:param name="lab_other_criteria"/>
		<xsl:param name="lab_completion_date"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<table border="1" cellpadding="0" cellspacing="0">
			<xsl:call-template name="draw_header">
				<xsl:with-param name="lab_session_attd" select="$lab_session_attd"/>
				<xsl:with-param name="lab_remark" select="$lab_remark"/>
				<xsl:with-param name="lab_score" select="$lab_score"/>
				<xsl:with-param name="lab_status" select="$lab_status"/>
				<xsl:with-param name="lab_attendance" select="$lab_attendance"/>
				<xsl:with-param name="lab_other_criteria" select="$lab_other_criteria"/>
				<xsl:with-param name="lab_completion_date" select="$lab_completion_date"/>
				<xsl:with-param name="lab_yes" select="$lab_yes"/>
				<xsl:with-param name="lab_no" select="$lab_no"/>
			</xsl:call-template>
			<xsl:apply-templates select="attendance_list/attendance">
				<xsl:with-param name="lab_yes" select="$lab_yes"/>
				<xsl:with-param name="lab_no" select="$lab_no"/>
			</xsl:apply-templates>
		</table>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template name="draw_header">
		<xsl:param name="lab_session_attd"/>
		<xsl:param name="lab_remark"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_attendance"/>
		<xsl:param name="lab_other_criteria"/>
		<xsl:param name="lab_completion_date"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<tr class="trHeader">
			<td>
				<xsl:value-of select="$lab_dis_name"/>
			</td>
			<td>
				<xsl:value-of select="$lab_usr_id"/>
			</td>
			<td>
				<xsl:value-of select="$lab_group"/>
			</td>
			<td>
				<xsl:value-of select="$lab_grade"/>
			</td>
			<xsl:if test="$is_integrated != 'true'">
				<td>
					<span class="TitleText">
						<xsl:value-of select="$lab_862"/>
					</span>
				</td>
			</xsl:if>
			<xsl:if test="$create_session_ind = 'true'">
				<td>
					<xsl:value-of select="$lab_session_attd"/>
				</td>
			</xsl:if>
			<xsl:if test="$session_ind = 'false'">
				<td align="right">
					<xsl:value-of select="$lab_score"/>
				</td>
			</xsl:if>
			<xsl:if test="$itm_type = 'CLASSROOM'">
				<td align="right">
					<xsl:value-of select="$lab_attendance"/>
				</td>
			</xsl:if>
			<td>
				<xsl:value-of select="$lab_other_criteria"/>
			</td>
			<td>
				<xsl:value-of select="$lab_status"/>
			</td>
			<td align="left">
				<xsl:value-of select="$lab_completion_date"/>
			</td>
			<!-- accreditation -->
			<xsl:if test="item/item_type_meta/@iad_ind = 'true'">
				<xsl:for-each select="credits/credit">
					<td>
						<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
					</td>
				</xsl:for-each>
			</xsl:if>
		</tr>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="attendance">
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<tr class="trNormal">
			<td>
				<xsl:value-of select="user/name/@display_name"/>
			</td>
			<td>
				<xsl:value-of select="user/@id"/>
			</td>
			<td>
				<xsl:value-of select="full_path/text()"/>
			</td>
			<td>
				<xsl:apply-templates select="user/user_attribute_list/attribute_list[@type='UGR']/entity[@type='UGR']"/>
			</td>
			<xsl:if test="$is_integrated != 'true'">
				<td align="left">
					<span class="Text">
						<xsl:choose>
							<xsl:when test="count(be_integrated_lrn_lst/intg_lrn) > 0">
								<xsl:for-each select="be_integrated_lrn_lst/intg_lrn">
									<xsl:value-of select="text()"/>
									<xsl:if test="position() != last()"><br/></xsl:if>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>--</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					
					</span>
				</td>
			</xsl:if>
			<xsl:if test="$create_session_ind = 'true'">
				<td>
					<xsl:choose>
						<xsl:when test="@rate != ''">
							<xsl:call-template name="display_score">
								<xsl:with-param name="score" select="@rate"/>
							</xsl:call-template>
							<!--<xsl:value-of select="format-number(@rate,'0.00')"/>%-->
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>--</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
			<xsl:if test="$session_ind = 'false'">
				<td align="right">
					<xsl:choose>
						<xsl:when test="cov_score != ''">
							<xsl:call-template name="display_score">
								<xsl:with-param name="score" select="cov_score"/>
							</xsl:call-template>
							<!--<xsl:value-of select="format-number(cov_score,'0.00')"/>%-->
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>--</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
			<xsl:if test="$itm_type = 'CLASSROOM'">
			<td align="right">
				<xsl:choose>
					<xsl:when test="@rate = ''">--</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="number(substring-before(@rate,'.'))"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			</xsl:if>
			<td>
				<xsl:choose>
					<xsl:when test="pass_other_criteria='true'">
						<xsl:value-of select="$lab_yes"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_no"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<xsl:variable name="usr_attd_id">
				<xsl:value-of select="@status"/>
			</xsl:variable>
			<td>
				<xsl:call-template name="get_ats_title">
					<xsl:with-param name="ats_id" select="$usr_attd_id"/>
				</xsl:call-template>
			</td>
			<xsl:variable name="app_id" select="@app_id"/>
			<xsl:if test="/applyeasy/attendance_maintance/item/item_type_meta/@iad_ind = 'true'">
				<xsl:for-each select="../../credits/credit">
					<xsl:variable name="ict_id" select="@id"/>
					<xsl:variable name="icv_value" select="../../attendance_list/attendance[@app_id = $app_id]/credit_value_list/credit_value[@ict_id = $ict_id]"/>
					<td>
						<xsl:choose>
							<xsl:when test="$icv_value/@icv_value">
								<xsl:value-of select="$icv_value/@icv_value"/>
							</xsl:when>
							<xsl:otherwise>-</xsl:otherwise>
						</xsl:choose>
					</td>
				</xsl:for-each>
			</xsl:if>
			<td align="right">
				<xsl:choose>
					<xsl:when test="@att_timestamp = ''">-</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="@att_timestamp"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="entity">
		<xsl:if test="position()!=1">
			<xsl:text>, </xsl:text>
		</xsl:if>
		<xsl:value-of select="@display_bil"/>
	</xsl:template>
	<!-- ======================================================================== -->
</xsl:stylesheet>
