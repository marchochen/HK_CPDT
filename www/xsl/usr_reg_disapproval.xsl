<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/escape_doub_quo.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_gen_tab.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>	
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>	
	<!-- cust utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- others -->
	<xsl:import href="share/usr_detail_form_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
<!-- =============================================================== -->
<xsl:variable name="disable_role_list" select="/user_manager/user/disable_role_list"/>
<xsl:variable name="user" select="/user_manager/user"/>
<xsl:variable name="target_list"/>
<xsl:variable name="supervise_target_list" select="/user_manager/user/supervise_target_list"/>
<xsl:variable name="direct_supervisor" select="/user_manager/user/direct_supervisor"/>
<xsl:variable name="app_approval_usg" select="/user_manager/user/app_approval_usg"/>
<xsl:variable name="profile_attributes" select="/user_manager/meta/profile_attributes"/>
<xsl:variable name="cur_user_role" select="/user_manager/meta/cur_usr/role/@id"/>
<xsl:variable name="usr_role_lst" select="/user_manager/user/role_list"/>
<xsl:variable name="user_attribute_list" select="$user/user_attribute_list"/>
<xsl:variable name="all_role_list" select="/user_manager/user/all_role_list"/>
<xsl:variable name="usr_role_full_lst" select="/user_manager/user/usr_role_full_list"/>
<xsl:variable name="auth_role_list" select="/user_manager/user/auth_rol_list"/>
<xsl:variable name="role_list" select="/user_manager/user/role_list"/>
<xsl:variable name="group_value" select="/user_manager/user/user_attribute_list/attribute_list[@type = 'USG' ]/entity[@relation_type = 'USR_PARENT_USG']/@id"/>
<xsl:variable name="group_text" select="/user_manager/user/user_attribute_list/attribute_list[@type = 'USG' ]/entity[@relation_type = 'USR_PARENT_USG']/@display_bil"/>
<xsl:variable name="root_ent_id" select="/user_manager/meta/cur_usr/@root_ent_id"/>
<xsl:variable name="pwd_min_length" select="/user_manager/meta/profile_attributes/password/@min_length"/>
<xsl:variable name="pwd_max_length" select="/user_manager/meta/profile_attributes/password/@max_length"/>
<xsl:variable name="usr_id_min_length" select="/user_manager/meta/profile_attributes/user_id/@min_length"/>
<xsl:variable name="usr_id_max_length" select="/user_manager/meta/profile_attributes/user_id/@max_length"/>

	<xsl:variable name="entity_assignment_cnt">
		<xsl:value-of select="count(/user_manager/user/all_role_list/role/entity_assignment)"/>
	</xsl:variable>
	<xsl:variable name="filter_user_group"/>
	<!-- =============================================================== -->
	<xsl:template match="/user_manager">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_reg_approval">拒絕用戶註冊</xsl:with-param>
			<xsl:with-param name="lab_desc">拒絕用戶註冊，請輸入拒絕原因，按“拒絕"按鈕拒絕該用戶註冊。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_decline">拒絕</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_comment">拒絕原因</xsl:with-param>
			<xsl:with-param name="lab_desc_4">輸入800字符(400中文)</xsl:with-param>
			<xsl:with-param name="lab_msg_subject_approve">成功註冊</xsl:with-param>
			<xsl:with-param name="lab_msg_subject_disapprove">拒絕註冊</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_reg_approval">拒绝用户注册</xsl:with-param>
			<xsl:with-param name="lab_desc">拒绝用户注册，请输入拒绝原因，按“拒绝”按钮拒绝该用户注册。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_decline">拒绝</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_comment">拒绝原因</xsl:with-param>
			<xsl:with-param name="lab_desc_4">输入800字符(400中文)</xsl:with-param>
			<xsl:with-param name="lab_msg_subject_approve">成功注册</xsl:with-param>
			<xsl:with-param name="lab_msg_subject_disapprove">拒绝注册</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_reg_approval">Decline registration</xsl:with-param>
			<xsl:with-param name="lab_desc">Fill in the <b>Decline reason</b> and click <b>Decline</b> at the bottom of the page.</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_decline">Decline</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_comment">Decline reason</xsl:with-param>
			<xsl:with-param name="lab_desc_4">(Not more than 800 characters)</xsl:with-param>
			<xsl:with-param name="lab_msg_subject_approve">Successful registration</xsl:with-param>
			<xsl:with-param name="lab_msg_subject_disapprove">Registration declined</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- parentinfo start -->
	<xsl:template name="content">
		<xsl:param name="lab_usr_prof_maintain"/>
		<xsl:param name="lab_usr_bat_upload"/>
		<xsl:param name="lab_usr_reg_approval"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_desc_2"/>
		<xsl:param name="lab_desc_3"/>
		<xsl:param name="lab_g_form_btn_decline"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_comment"/>
		<xsl:param name="lab_desc_4"/>
		<xsl:param name="lab_msg_subject_approve"/>
		<xsl:param name="lab_msg_subject_disapprove"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js"/>			
			<script language="JavaScript"><![CDATA[
			var goldenman = new wbGoldenMan	
			usr = new wbUserGroup				
			stype = getUrlParam('stype')
			stimestamp = getUrlParam('stimestamp')
			ent_id = getUrlParam('ent_id')		
					
		]]></script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="wb_utils_gen_form_focus(document.frmXml)">
			<form name="frmXml">
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="usr_ent_id" value="{user/@ent_id}"/>
				<input type="hidden" name="charset" value="{$encoding}"/>
				<input type="hidden" name="usr_occupation_bil" value="{user/occupation}"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="usr_timestamp" value="{user/@timestamp}"/>
				<input type="hidden" name="usr_attribute_ent_id_lst" value=""/>
				<input type="hidden" name="usr_attribute_relation_type_lst" value=""/>
				<input type="hidden" name="msg_subject_approve">					
<!--					<xsl:attribute name="value"><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$lab_msg_subject_approve"/></xsl:with-param></xsl:call-template></xsl:attribute>-->
				</input>
				<input type="hidden" name="msg_subject_disapprove">
<!--					<xsl:attribute name="value"><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$lab_msg_subject_disapprove"/></xsl:with-param></xsl:call-template></xsl:attribute>-->
				</input>
				<input type="hidden" name="msg_subject" value=""/>
				<input type="hidden" name="pwd_min_length" value="{$pwd_min_length}"/>
				<input type="hidden" name="pwd_max_length" value="{$pwd_max_length}"/>
				<input type="hidden" name="usr_id_min_length" value="{$usr_id_min_length}"/>
				<input type="hidden" name="usr_id_max_length" value="{$usr_id_max_length}"/>
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_USR_REGIETER_APP</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_usr_reg_approval"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text" select="$lab_desc"/>
				</xsl:call-template>
				
				<xsl:call-template name="draw_detail_lst">
					<xsl:with-param name="lab_comment" select="$lab_comment"/>
					<xsl:with-param name="lab_desc_4" select="$lab_desc_4"/>
				</xsl:call-template>
				<div class="wzb-bar">
					<table cellpadding="0" cellspacing="0" width="{$wb_gen_table_width}" border="0">
						<tr>
							<td align="center">
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_g_form_btn_decline"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:usr.register.reg_usr_approval_exec(document.frmXml, document.frmXml.usr_ent_id.value, 'disappr', '<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
								</xsl:call-template>
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_g_form_btn_cancel"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:usr.register.reg_usr_approval_lst()</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
					</table>
				</div>
				<xsl:call-template name="wb_ui_footer"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="draw_detail_lst">
		<xsl:param name="lab_comment"/>
		<xsl:param name="lab_desc_4"/>
		<!-- section 1 -->
		<xsl:call-template name="usr_detail_seperate_hdr"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
<!--
		<xsl:for-each select="$profile_attributes/*">
			<xsl:variable name="show">
				<xsl:choose>
					<xsl:when test="@active">
						<xsl:choose>
							<xsl:when test="@active = 'false'">false</xsl:when>
							<xsl:otherwise>true</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="name() = 'password'">false</xsl:when>
					<xsl:when test="not(@active)">true</xsl:when>
				</xsl:choose>
			</xsl:variable>
			<xsl:if test="$show = 'true'">
			<xsl:apply-templates select="." mode="profile_attributes">
				<xsl:with-param name="is_end_user">false</xsl:with-param>
			</xsl:apply-templates>
			</xsl:if>
		</xsl:for-each>	
-->
			<tr>
				<td width="20%" align="right" valign="top" class="wzb-form-label">
					<span class="wzb-form-star">*</span><span class="TitleText">
						<xsl:value-of select="$lab_comment"/>：</span>
				</td>
				<td width="80%" class="wzb-form-control">
					<textarea style="width:400px" rows="5" name="usr_approve_reason" class="Inputfrm"/>
					<br/>
					<span class="Text">
						<xsl:value-of select="$lab_desc_4"/>
					</span>
				</td>
			</tr>			
		</table>
		<xsl:call-template name="usr_detail_seperate_footer"/>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
