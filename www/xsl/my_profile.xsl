<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
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
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<!-- cust utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- others -->
	<xsl:import href="share/usr_detail_form_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:output indent="yes" encoding="UTF-8" />
	<!-- =============================================================== -->
	<xsl:variable name="disable_role_list" select="/user_manager/user/disable_role_list"/>
	<xsl:variable name="user" select="/user_manager/user"/>
	<xsl:variable name="target_list" select="/user_manager/user/target_list"/>		
	<xsl:variable name="supervise_target_list" select="/user_manager/user/supervise_target_list"/>
	<xsl:variable name="direct_supervisor" select="/user_manager/user/direct_supervisor"/>
	<xsl:variable name="profile_attributes" select="/user_manager/meta/profile_attributes"/>
	<xsl:variable name="cur_user_role" select="/user_manager/meta/cur_usr/role/@id"/>
	<xsl:variable name="user_attribute_list" select="$user/user_attribute_list"/>
	<xsl:variable name="auth_role_list" select="/user_manager/user/auth_rol_list"/>
	<xsl:variable name="all_role_list" select="/user_manager/user/all_role_list"/>
	<xsl:variable name="role_list" select="/user_manager/user/role_list"/>
	<xsl:variable name="group_value" select="/user_manager/user/user_attribute_list/attribute_list[@type = 'USG' ]/entity[@relation_type = 'USR_PARENT_USG']/@id"/>
	<xsl:variable name="group_text" select="/user_manager/user/user_attribute_list/attribute_list[@type = 'USG' ]/entity[@relation_type = 'USR_PARENT_USG']/@display_bil"/>
	<xsl:variable name="not_syn_gpm_type" select="/user_manager/user/@not_syn_gpm_type"/>
	<xsl:variable name="usr_role_lst" select="/user_manager/user/role_list"/>
	<xsl:variable name="usr_role_full_lst" select="/user_manager/user/usr_role_full_list"/>
	<xsl:variable name="app_approval_usg" select="/user_manager/user/app_approval_usg"/>
	<xsl:variable name="root_ent_id" select="/user_manager/meta/cur_usr/@root_ent_id"/>	
	<xsl:variable name="entity_assignment_cnt">
		<xsl:value-of select="count(/user_manager/user/all_role_list/role/entity_assignment)"/>
	</xsl:variable>
	<!-- to determine if the current profile is editable to the user -->
	<xsl:variable name="editable">
		<xsl:choose>
			<xsl:when test="$profile_attributes/*[not(name() = 'user_id') and not(@active = 'false') and not(@hidden = 'true') and not(@readonly = 'true')]">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="filter_user_group"></xsl:variable>
	
	<xsl:variable name="lab_select_default_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_select_default_image')"/>
	<xsl:variable name="lab_default_images" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_default_images')"/>
	<xsl:variable name="lab_upload_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_upload_image')"/>
	<xsl:variable name="lab_button_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, '329')"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/> 	
	<!-- =============================================================== -->
	<xsl:template match="/user_manager">
		<xsl:apply-templates select="user"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="user">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_info">用戶資料</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_personal_prof">我的檔案</xsl:with-param>
			<xsl:with-param name="lab_usr_info_desc">查看或編輯你的個人檔案</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_info">用户资料</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_personal_prof">我的档案</xsl:with-param>
			<xsl:with-param name="lab_usr_info_desc">查看或编辑用户信息。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_info">User information</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_personal_prof">My profile – personal information</xsl:with-param>
			<xsl:with-param name="lab_usr_info_desc">View or edit your personal information here.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_usr_info"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_personal_prof"/>
		<xsl:param name="lab_usr_info_desc"/>
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
			<script language="JavaScript" src="{$wb_js_path}wb_defaultImage.js"/>
			<link rel="stylesheet" type="text/css" href="../static/css/thickbox.css" />
				<script type="text/javascript" src="../static/js/thickbox-compressed.js"/>
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
			<form name="frmXml" enctype="multipart/form-data" >
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="usr_ent_id" value="{@ent_id}"/>
				<input type="hidden" name="charset" value="{$encoding}"/>
				<input type="hidden" name="usr_occupation_bil" value="{occupation}"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="usr_timestamp" value="{@timestamp}"/>
				<input type="hidden" name="usr_attribute_ent_id_lst" value=""/>
				<input type="hidden" name="usr_attribute_relation_type_lst" value=""/>
				<input type="hidden" name="usr_extra_multipleoption_31_lst" value=""/>
				<input type="hidden" name="usr_extra_multipleoption_32_lst" value=""/>
				<input type="hidden" name="usr_extra_multipleoption_33_lst" value=""/>
				<input type="hidden" name="usr_extra_multipleoption_34_lst" value=""/>
				<input type="hidden" name="usr_extra_multipleoption_35_lst" value=""/>
				<input type="hidden" name="usr_extra_multipleoption_36_lst" value=""/>
				<input type="hidden" name="usr_extra_multipleoption_37_lst" value=""/>
				<input type="hidden" name="usr_extra_multipleoption_38_lst" value=""/>
				<input type="hidden" name="usr_extra_multipleoption_39_lst" value=""/>
				<input type="hidden" name="usr_extra_multipleoption_40_lst" value=""/>

				<!-- navigation -->
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_USR_INFO</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_personal_prof"/>
					</xsl:with-param>
				</xsl:call-template>
				 <xsl:call-template name="wb_ui_desc">
					 <xsl:with-param name="text" select="$lab_usr_info_desc"/> 
				</xsl:call-template>  
				
				<!-- <xsl:call-template name="profile_gen_tab">
					<xsl:with-param name="profile_target_tab">1</xsl:with-param>
				</xsl:call-template> -->	
				
				    <!-- <xsl:call-template name="wb_ui_space"/>  -->
				 <xsl:call-template name="draw_detail_lst"/>
				 <xsl:call-template name="wb_ui_space"/> 
				<xsl:call-template name="wb_ui_line"/> 
				<xsl:call-template name="wb_ui_space"/> 
				 <xsl:if test="$editable = 'true'"> 
					<table cellpadding="3" cellspacing="0" width="{$wb_gen_table_width}" border="0">
						<tr>
							<td align="center">
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.edit_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>',1)</xsl:with-param>
								</xsl:call-template>
								<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:wb_utils_gen_home()</xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
					</table>
				</xsl:if>
				 <xsl:call-template name="wb_ui_footer"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	 <xsl:template name="draw_detail_lst"> 
		<!-- section 1 -->
		<!-- <xsl:call-template name="wb_ui_line"/> -->
		<table class="Bg" cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
		<!-- 	<tr>
				<td width="20%" height="10">
					<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
				</td>
				<td width="80%">
					<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
				</td>
			</tr>   标题与内容之间距离修改  如其他地方引用可去掉该注释-->
			<xsl:for-each select="$profile_attributes/*">
				<xsl:variable name="show">
					<xsl:choose>
						<xsl:when test="name() = 'user_id'">true</xsl:when>
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
				<!--
				<xsl:if test="$show='true'">
					<xsl:value-of select="name()"/>:<xsl:value-of select="$show"/><br/>
				</xsl:if>
				-->

				<xsl:if test="$show = 'true'">
					<xsl:apply-templates select="." mode="profile_attributes">
						<xsl:with-param name="is_end_user">true</xsl:with-param>
						<xsl:with-param name="showempty" select="@showempty"/>
					</xsl:apply-templates>
				</xsl:if>
			</xsl:for-each>
			<tr>
				<td width="20%" height="10">
					<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
				</td>
				<td width="80%">
					<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="usr_detail_seperate_footer">
			<xsl:with-param name="show_required">
			<xsl:value-of select="$editable"/>
			
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
