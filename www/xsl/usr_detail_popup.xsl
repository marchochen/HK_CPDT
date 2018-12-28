<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<!-- usr utils -->
	<xsl:import href="share/usr_detail_form_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="usr_ent_id">
		<xsl:value-of select="/user_manager/user/@ent_id"/>
	</xsl:variable>
	<xsl:variable name="page_variant" select="/user_manager/meta/page_variant"/>
	<!-- =============================================================== -->
	<xsl:variable name="user" select="/user_manager/user"/>
	<xsl:variable name="user_attribute_list" select="/user_manager/group_member_list/default_grade"/>
	<xsl:variable name="supervise_target_list" select="/user_manager/user/supervise_target_list"/>
	<xsl:variable name="direct_supervisor" select="/user_manager/user/direct_supervisor"/>
	<xsl:variable name="target_list" select="/user_manager/user/target_list"/>
	<xsl:variable name="all_role_list" select="/user_manager/user/all_role_list"/>
	<xsl:variable name="auth_role_list" select="/user_manager/user/auth_rol_list"/>
	<xsl:variable name="role_list" select="/user_manager/user/role_list"/>
	<xsl:variable name="app_approval_usg" select="/user_manager/user/app_approval_usg"/>
	<xsl:variable name="root_ent_id" select="/user_manager/meta/cur_usr/@root_ent_id"/>
	<xsl:variable name="group_value"/>
	<xsl:variable name="group_text"/>
	<xsl:variable name="usr_role_lst"/>
	<xsl:variable name="usr_role_full_lst"/>
	<xsl:variable name="disable_role_list" select="/user_manager/user/disable_role_list"/>
	<!-- ==============================================-->
	<xsl:variable name="profile_attributes" select="/user_manager/meta/profile_attributes"/>
	<xsl:variable name="filter_user_group"/>
	
	<xsl:variable name="lab_select_default_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_select_default_image')"/>
	<xsl:variable name="lab_default_images" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_default_images')"/>
	<xsl:variable name="lab_upload_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_upload_image')"/>
	<xsl:variable name="lab_button_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, '329')"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/> 
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="user_manager">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
				usr = new wbUserGroup
		]]></script>
			<xsl:call-template name="new_css"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_usr_info">用戶資料</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_usr_info">用户资料</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_info">User information</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_usr_info"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_usr_info"/>
			</xsl:with-param>
			<xsl:with-param name="width">100%</xsl:with-param>
		</xsl:call-template>
		<xsl:apply-templates select="user" mode="display_details"/>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_close"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:window.close()</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="user" mode="display_details">
		<table>
			<xsl:for-each select="$profile_attributes/*">
				<xsl:variable name="show">
					<xsl:choose>
						<xsl:when test="@active">
							<xsl:choose>
								<xsl:when test="@active = 'false'">false</xsl:when>
								<xsl:otherwise>
									<xsl:choose>
										<xsl:when test="@hidden = 'true'">false</xsl:when>
										<xsl:otherwise>true</xsl:otherwise>
									</xsl:choose>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="not(@active)">
							<xsl:choose>
								<xsl:when test="@hidden = 'true'">false</xsl:when>
								<xsl:otherwise>true</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:variable>
				<!--Debug : <xsl:value-of select="name()"/>:<xsl:value-of select="$show"/><br/>-->
				<xsl:if test="$show = 'true'">
					<xsl:apply-templates select="." mode="profile_attributes">
						<xsl:with-param name="hiddenNoValue">true</xsl:with-param>
						<xsl:with-param name="is_end_user">true</xsl:with-param>
						<xsl:with-param name="readonly">true</xsl:with-param>
					</xsl:apply-templates>
				</xsl:if>
			</xsl:for-each>
		</table>
	</xsl:template>
</xsl:stylesheet>
