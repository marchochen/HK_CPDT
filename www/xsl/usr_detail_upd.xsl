<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	
	<!-- others -->
	<xsl:import href="share/usr_detail_form_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="user" select="/user_manager/user"/>
	<xsl:variable name="supervise_target_list" select="/user_manager/user/supervise_target_list"/>
	<xsl:variable name="direct_supervisor" select="/user_manager/user/direct_supervisor"/>
	<xsl:variable name="app_approval_usg" select="/user_manager/user/app_approval_usg"/>
	<xsl:variable name="profile_attributes" select="/user_manager/meta/profile_attributes"/>
	<xsl:variable name="cur_user_role" select="/user_manager/meta/cur_usr/role/@id"/>
	<xsl:variable name="usr_role_lst" select="/user_manager/user/role_list"/>
	<xsl:variable name="user_attribute_list" select="$user/user_attribute_list"/>
	<xsl:variable name="auth_role_list" select="/user_manager/user/auth_rol_list"/>
	<xsl:variable name="disable_role_list" select="/user_manager/user/disable_role_list"/>
	<xsl:variable name="all_role_list" select="/user_manager/user/all_role_list"/>
	<xsl:variable name="role_list" select="/user_manager/user/role_list"/>
	<xsl:variable name="group_value" select="/user_manager/user/user_attribute_list/attribute_list[@type = 'USG' ]/entity[@relation_type = 'USR_PARENT_USG']/@id"/>
	<xsl:variable name="group_text" select="/user_manager/user/user_attribute_list/attribute_list[@type = 'USG' ]/entity[@relation_type = 'USR_PARENT_USG']/@display_bil"/>
	<xsl:variable name="root_ent_id" select="/user_manager/meta/cur_usr/@root_ent_id"/>
	<xsl:variable name="ent_id" select="/user_manager/user/@ent_id"/>
	<xsl:variable name="usr_role_full_lst" select="/user_manager/user/usr_role_full_list"/>
	<xsl:variable name="not_syn_gpm_type" select="/user_manager/user/@not_syn_gpm_type"/>
	<xsl:variable name="entity_assignment_cnt">
		<xsl:value-of select="count(/user_manager/user/auth_rol_list/role/entity_assignment[contains(@type,'USG')])"/>
	</xsl:variable>
	<xsl:variable name="usr_id_min_length" select="/user_manager/meta/profile_attributes/user_id/@min_length"/>
	<xsl:variable name="usr_id_max_length" select="/user_manager/meta/profile_attributes/user_id/@max_length"/>
	<xsl:variable name="target_list" select="/user_manager/user/target_list"/>
	<xsl:variable name="filter_user_group"/>
	
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
			<xsl:with-param name="lab_usr_edit_profile">編輯資訊</xsl:with-param>
			<xsl:with-param name="lab_usr_manager">用戶管理</xsl:with-param>
			<xsl:with-param name="lab_usr_info">用戶資料</xsl:with-param>
			<xsl:with-param name="lab_usr_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_usr_prof_maintain">用戶文件夾管理</xsl:with-param>
			<xsl:with-param name="lab_usr_bat_upload">上載一批用戶</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_edit_profile">编辑信息</xsl:with-param>
			<xsl:with-param name="lab_usr_manager">用户管理</xsl:with-param>
			<xsl:with-param name="lab_usr_info">用户资料</xsl:with-param>
			<xsl:with-param name="lab_usr_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_usr_prof_maintain">用户管理</xsl:with-param>
			<xsl:with-param name="lab_usr_bat_upload">批量用户上传</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_edit_profile">Edit user</xsl:with-param>
			<xsl:with-param name="lab_usr_manager">User management</xsl:with-param>
			<xsl:with-param name="lab_usr_info">User information</xsl:with-param>
			<xsl:with-param name="lab_usr_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_usr_prof_maintain">User profile maintenance</xsl:with-param>
			<xsl:with-param name="lab_usr_bat_upload">Batch user upload</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- parentinfo start -->
	<xsl:template name="content">
		<xsl:param name="lab_usr_manager"/>
		<xsl:param name="lab_usr_info"/>
		<xsl:param name="lab_usr_edit"/>
		<xsl:param name="lab_usr_edit_profile"/>
		<xsl:param name="lab_usr_prof_maintain"/>
		<xsl:param name="lab_usr_bat_upload"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<link rel="stylesheet" type="text/css" href="../static/css/thickbox.css" />
		
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js"/>
			
			<script type="text/javascript" src="../static/js/thickbox-compressed.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_defaultImage.js"/>	
			<script language="JavaScript"><![CDATA[
			var goldenman = new wbGoldenMan	
			usr = new wbUserGroup				
			stype = getUrlParam('stype')
			stimestamp = getUrlParam('stimestamp')
			ent_id = getUrlParam('ent_id')		
			
			function init(){
			}		
			
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="init();wb_utils_gen_form_focus(document.frmXml)">
			<form name="frmXml" enctype="multipart/form-data">
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="usr_ent_id" value="{@ent_id}"/>
				<input type="hidden" name="charset" value="{$encoding}"/>
				<input type="hidden" name="usr_occupation_bil" value="{occupation}"/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="url_failure1" value=""/>
				<input type="hidden" name="usr_timestamp" value="{@timestamp}"/>
				<input type="hidden" name="usr_attribute_ent_id_lst" value=""/>
				<input type="hidden" name="usr_not_syn_relation_type_lst" value=""/>
				<input type="hidden" name="usr_attribute_relation_type_lst" value=""/>
				<input type="hidden" name="usr_id_min_length" value="{$usr_id_min_length}"/>
				<input type="hidden" name="usr_id_max_length" value="{$usr_id_max_length}"/>
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
					<xsl:with-param name="parent_code">FTN_AMD_USR_INFO</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_usr_edit_profile"/>
				</xsl:call-template>

				<xsl:call-template name="draw_detail_lst"/>
				<div class="wzb-bar">
					<script><![CDATA[ent_name]]><![CDATA[=']]><xsl:call-template name="escape_js">
							<xsl:with-param name="input_str">
								<xsl:value-of select="name/@display_name"/> (<xsl:value-of select="@id"/>)</xsl:with-param>
						</xsl:call-template><![CDATA[';]]></script>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_g_form_btn_ok"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.edit_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_g_form_btn_cancel"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.manage_usr(<xsl:value-of select="$ent_id"/>,'','')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>
				</div>
			</form>
			
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="draw_detail_lst">
		<!-- section 1 -->
		<xsl:call-template name="usr_detail_seperate_hdr"/>
 
		<table>
			<xsl:for-each select="$profile_attributes/*">
				<xsl:variable name="show">
					<xsl:choose>
						<xsl:when test="name() = 'password'">false</xsl:when>
						<xsl:when test="@active">
							<xsl:value-of select="@active"/>
						</xsl:when>
						<xsl:when test="not(@active)">true</xsl:when>
					</xsl:choose>
				</xsl:variable>
				<xsl:if test="$show = 'true'">
					<xsl:apply-templates select="." mode="profile_attributes">
						<xsl:with-param name="is_end_user">false</xsl:with-param>
						<xsl:with-param name="showempty" select="@showempty"/>
					</xsl:apply-templates>
				</xsl:if>
			</xsl:for-each>
			  
		</table>
		  
		<xsl:call-template name="usr_detail_seperate_footer"/>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
