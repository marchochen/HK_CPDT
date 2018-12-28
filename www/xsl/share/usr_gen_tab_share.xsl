<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="../utils/wb_gen_tab.xsl"/>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_position_maintenance" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lan1')"/> 	
	
	<!-- ======================================================================== -->
	<xsl:variable name="lab_usr_reg_approval">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">用戶登記審批</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">注册审批</xsl:when>
			<xsl:otherwise>Registration approval</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_usr_prof_maintain">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">檔案維護</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">信息维护</xsl:when>
			<xsl:otherwise>Profile maintenance</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_usr_reactivation">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">戶口復原</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">用户激活</xsl:when>
			<xsl:otherwise>Account reactivation</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_grade_maintenance">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'"><xsl:value-of select="$lab_grade"/>維護</xsl:when>
			<xsl:when test="$wb_lang = 'gb'"><xsl:value-of select="$lab_grade"/>维护</xsl:when>
			<xsl:otherwise><xsl:value-of select="$lab_grade"/> maintenance</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- ======================================================================== -->
	<xsl:variable name="hasUsrBatchUploadTag">
		<xsl:choose>
			<xsl:when test="//page_variant/@hasUsrBatchUploadTag = 'true'">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="hasUsrReactivateTab">
		<xsl:choose>
			<xsl:when test="//page_variant/@hasUsrReactivateTab= 'true'">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="hasUsrApprovalTab">
		<xsl:choose>
			<xsl:when test="//page_variant/@hasUsrApprovalTab= 'true'">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="hasUsrGradeMgtTab">
		<xsl:choose>
			<xsl:when test="//page_variant/@hasUsrGradeMgtTab = 'true'">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="hasUsrPositionMgtTab">
		<xsl:choose>
			<xsl:when test="//page_variant/@hasUsrPositionMgtTab = 'true'">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:template name="usr_gen_tab">
		<xsl:param name="usr_target_tab">1</xsl:param>
		<xsl:param name="usr_tab_name_lst">
			<!-- 1 -->
			<xsl:value-of select="$lab_usr_prof_maintain"/>
			<!-- 2 -->
			<xsl:if test="$hasUsrApprovalTab = 'true'">
				<xsl:text>:_:_:</xsl:text>
				<xsl:value-of select="$lab_usr_reg_approval"/>
			</xsl:if>
			<!-- 3 -->
			<xsl:if test="$hasUsrReactivateTab = 'true' or $usr_target_tab = '3'">
				<xsl:text>:_:_:</xsl:text>
				<xsl:value-of select="$lab_usr_reactivation"/>
			</xsl:if>
			<!-- 1/2 -->
			<xsl:if test="$hasUsrGradeMgtTab = 'true'">
				<xsl:text>:_:_:</xsl:text>
				<xsl:value-of select="$lab_grade_maintenance"/>
			</xsl:if>
			<xsl:if test="$hasUsrPositionMgtTab = 'true'">
				<xsl:text>:_:_:</xsl:text>
				<xsl:value-of select="$lab_position_maintenance"/>
			</xsl:if>
		</xsl:param>
		<xsl:param name="usr_tab_link_lst">
			<!-- 1 -->
			<xsl:text>javascript:usr.prof_maintain()</xsl:text>
			<!-- 2 -->
			<xsl:if test="$hasUsrApprovalTab = 'true'">
				<xsl:text>:_:_:</xsl:text>
				<xsl:text>javascript:usr.register.reg_usr_approval_lst()</xsl:text>
			</xsl:if>
			<!-- 3 -->
			<xsl:if test="$hasUsrReactivateTab = 'true' or $usr_target_tab = '3'">
				<xsl:text>:_:_:</xsl:text>
				<xsl:text>javascript:usr.activate.reactivate_lst()</xsl:text>
			</xsl:if>
			<!-- 1/2 -->
			<xsl:if test="$hasUsrGradeMgtTab = 'true'">
				<xsl:text>:_:_:</xsl:text>
				<xsl:text>javascript:usr.grade.maintain()</xsl:text>
			</xsl:if>
			<xsl:if test="$hasUsrPositionMgtTab = 'true'">
				<xsl:text>:_:_:</xsl:text>
				<xsl:text>javascript:usr.position.maintain()</xsl:text>
			</xsl:if>
		</xsl:param>
		<xsl:call-template name="wb_gen_tab">
			<xsl:with-param name="tab_name_lst">
				<xsl:value-of select="$usr_tab_name_lst"/>
			</xsl:with-param>
			<xsl:with-param name="tab_link_lst">
				<xsl:value-of select="$usr_tab_link_lst"/>
			</xsl:with-param>
			<xsl:with-param name="target_tab">
				<xsl:choose>
					<xsl:when test="$hasUsrApprovalTab != 'true'">
						<xsl:choose>
							<xsl:when test="$usr_target_tab = '3'">2</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$usr_target_tab"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$usr_target_tab"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ======================================================================== -->
</xsl:stylesheet>
