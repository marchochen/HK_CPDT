<?xml version="1.0" encoding="UTF-8" ?>
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
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>

	<xsl:import href="share/usr_detail_form_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
<xsl:output indent="yes" />
<!-- =============================================================== -->
<xsl:variable name="usr_role_lst" select="/user_manager/user/role_list"/>
<xsl:variable name="usr_role_full_lst" select="/user_manager/user/usr_role_full_list"/>      
<xsl:variable name="root_ent_id" select="/user_manager/meta/cur_usr/@root_ent_id"/>
<!-- =============================================================== -->
<!-- "user" & "user_attribute_list" should always set to "/user_manager/user" , to prevent getting wrong data -->
<xsl:variable name="user" select="/user_manager/user"/>
<xsl:variable name="user_attribute_list" select="/user_manager/group_member_list/default_grade"/>
<xsl:variable name="supervise_target_list" select="/user_manager/user/supervise_target_list"/>
<xsl:variable name="direct_supervisor" select="/user_manager/user/direct_supervisor"/>     
<xsl:variable name="app_approval_usg" select="/user_manager/user/app_approval_usg"/>
<!-- ==============================================-->
<xsl:variable name="disable_role_list" select="/user_manager/group_member_list/disable_role_list"/>
<xsl:variable name="target_list" select="/user_manager/user/target_list"/>
<xsl:variable name="all_role_list" select="/user_manager/group_member_list/all_role_list"/>
<xsl:variable name="auth_role_list" select="/user_manager/group_member_list/auth_rol_list"/>
<xsl:variable name="role_list" select="/user_manager/group_member_list/role_list"/>
<xsl:variable name="profile_attributes" select="/user_manager/meta/profile_attributes"/>
<xsl:variable name="pwd_min_length" select="/user_manager/meta/profile_attributes/password/@min_length"/>
<xsl:variable name="pwd_max_length" select="/user_manager/meta/profile_attributes/password/@max_length"/>
<xsl:variable name="usr_id_min_length" select="/user_manager/meta/profile_attributes/user_id/@min_length"/>
<xsl:variable name="usr_id_max_length" select="/user_manager/meta/profile_attributes/user_id/@max_length"/>
<xsl:variable name="group_value" select="/user_manager/group_member_list/@id"/>
<xsl:variable name="group_text" select="/user_manager/group_member_list/desc"/>
<xsl:variable name="entity_assignment_cnt"><xsl:value-of select="count(/user_manager/group_member_list/auth_rol_list/role/entity_assignment[contains(@type,'USG')])"/></xsl:variable>
<xsl:variable name="filter_user_group"/>

<xsl:variable name="lab_select_default_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_select_default_image')"/>
<xsl:variable name="lab_default_images" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_default_images')"/>
<xsl:variable name="lab_upload_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_upload_image')"/>
<xsl:variable name="lab_button_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, '329')"/>
<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/> 	
<!-- =============================================================== -->
<xsl:template match="/user_manager">
	<xsl:apply-templates select="group_member_list"/>
</xsl:template>
<!-- =============================================================== -->
<xsl:template match="group_member_list">
	<html><xsl:call-template name="wb_init_lab"/></html>
</xsl:template>
<!-- =============================================================== -->

<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_manager">用戶管理</xsl:with-param>
			<xsl:with-param name="lab_add_usr">添加用戶</xsl:with-param>
 			<xsl:with-param name="lab_usr_prof_maintain">用戶文件夾管理</xsl:with-param>
  			<xsl:with-param name="lab_usr_bat_upload">上載一批用戶</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_msg_subject">你的學生戶口</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_user">添加用户</xsl:with-param>
			<xsl:with-param name="lab_usr_pwd_need_change_ind">第一次登錄時必須修改密碼</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_manager">用户管理</xsl:with-param>
			<xsl:with-param name="lab_add_usr">添加用户</xsl:with-param>
			<xsl:with-param name="lab_usr_prof_maintain">用户管理</xsl:with-param>
			<xsl:with-param name="lab_usr_bat_upload">批量用户上传</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_msg_subject">你的学生资料</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_user">添加用户</xsl:with-param>
			<xsl:with-param name="lab_usr_pwd_need_change_ind">第一次登录时必须修改密码</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_manager">User management</xsl:with-param>
			<xsl:with-param name="lab_add_usr">Add user</xsl:with-param>	
			<xsl:with-param name="lab_usr_prof_maintain">User profile maintenance</xsl:with-param>
			<xsl:with-param name="lab_usr_bat_upload">Batch user upload</xsl:with-param>		
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_msg_subject">Your student account</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_user">Add user</xsl:with-param>
			<xsl:with-param name="lab_usr_pwd_need_change_ind">New user must change Password at first logon</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
<!-- =============================================================== -->
<xsl:template name="content">
	<xsl:param name="lab_usr_manager"/>	
	<xsl:param name="lab_add_usr"/>
	<xsl:param name="lab_usr_prof_maintain"/>
	<xsl:param name="lab_usr_bat_upload"/>
	<xsl:param name="lab_g_form_btn_ok"/>
	<xsl:param name="lab_g_form_btn_cancel"/>
	<xsl:param name="lab_msg_subject"/>
	<xsl:param name="lab_g_txt_btn_add_user"/>
	<xsl:param name="lab_usr_pwd_need_change_ind"/>
	<head>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		<title><xsl:value-of select="$wb_wizbank"/></title>
		<link rel="stylesheet" type="text/css" href="../static/css/thickbox.css" />
		
		<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
		<script language="JavaScript" src="{$wb_js_path}wb_usergroup.js"/>
		<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
		<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
		<script language="JavaScript" src="{$wb_js_path}wb_goldenman.js"/>
		<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js"/>	
		
		<script language="JavaScript" src="../static/js/cwn_utils.js"/>	
		
		<script type="text/javascript" src="../static/js/thickbox-compressed.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_defaultImage.js"/>		
		<script language="JavaScript"><![CDATA[
			usr = new wbUserGroup
			var goldenman = new wbGoldenMan	
			function status(){return false;}
			var active_tab = getUrlParam('active_tab');
		]]></script>
		<!-- CSS -->
	</head>
	<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onLoad="wb_utils_gen_form_focus(document.frmXml);">
		<form name="frmXml" method="post" onsubmit="return status()" autocomplete = "off" enctype="multipart/form-data">			
			<input type="hidden" name="cmd" value=""/>
			<input type="hidden" name="ent_id_parent" value=""/>
			<input type="hidden" name="url_success" value=""/>
			<input type="hidden" name="url_failure" value=""/>			
			<input type="hidden" name="url_failure1" value=""/>			
			<input type="hidden" name="usr_attribute_ent_id_lst" value=""/>
			<input type="hidden" name="usr_attribute_relation_type_lst" value=""/>
			<input type="hidden" name="usr_not_syn_relation_type_lst" value=""/>
			<input type="hidden" name="pwd_min_length" value="{$pwd_min_length}"/>
			<input type="hidden" name="pwd_max_length" value="{$pwd_max_length}"/>
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

			
<!--			<input type="hidden" name="msg_subject">
				<xsl:attribute name="value">
					<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$lab_msg_subject"/></xsl:with-param></xsl:call-template>
				</xsl:attribute>
			</input>-->
			<!-- navigation -->
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_USR_INFO</xsl:with-param>
				<xsl:with-param name="parent_code">FTN_AMD_USR_INFO</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_nav_link">
				<xsl:with-param name="text">
					<xsl:for-each select="ancestor_node_list/node">
						<a class="NavLink" href="javascript:usr.group.manage_grp('{@id}','','{$root_ent_id}')">
							<xsl:value-of select="title/."/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
					</xsl:for-each>
					<a class="NavLink" href="javascript:usr.group.manage_grp('{@id}','','{$root_ent_id}')">
						<xsl:value-of select="desc"/>
					</a>
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:value-of select="$lab_g_txt_btn_add_user"/>
				</xsl:with-param>
			</xsl:call-template>
			
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text" select="$lab_add_usr"/>
			</xsl:call-template>
			<xsl:call-template name="draw_detail_lst">
				<xsl:with-param name="lab_usr_pwd_need_change_ind" select="$lab_usr_pwd_need_change_ind"/>
			</xsl:call-template>			
			<div class="wzb-bar">
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_ok"/></xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.ins_exec(document.frmXml,'<xsl:value-of select="@id"/>','<xsl:value-of select="$wb_lang"/>',active_tab);</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_cancel"/></xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">javascript:usr.group.manage_grp(<xsl:value-of select="@id"/>)</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
				</xsl:call-template>
			</div>
		</form>
	</body>
</xsl:template>
<!-- =========================================================================================== -->
<xsl:template name="draw_detail_lst">
	<xsl:param name="lab_usr_pwd_need_change_ind"/>
	<!-- section 1 -->
	<xsl:call-template name="usr_detail_seperate_hdr"/>
	<table>
		<xsl:for-each select="$profile_attributes/*">
			<xsl:variable name="show">
				<xsl:choose>
					<xsl:when test="@active"><xsl:value-of select="@active"/></xsl:when>
					<xsl:when test="not(@active)">true</xsl:when>
				</xsl:choose>
			</xsl:variable>
			<xsl:if test="$show = 'true'">
				<xsl:apply-templates select="." mode="profile_attributes">
					<xsl:with-param name="is_end_user">false</xsl:with-param>
					<xsl:with-param name="readonly" select="@readonly"/>
					<xsl:with-param name="showempty" select="@showempty"/>
				</xsl:apply-templates>
				<xsl:if test="@fieldname = 'usr_pwd'">
					<td class="wzb-form-label"></td>
					<td class="wzb-form-control">
						<input name="usr_pwd_need_change_ind" type="checkbox" value="true" />
						<xsl:value-of select="$lab_usr_pwd_need_change_ind"></xsl:value-of>
					</td>
				</xsl:if>
			</xsl:if>
		</xsl:for-each>		
	</table>	
	<xsl:call-template name="usr_detail_seperate_footer"/>
</xsl:template>
<!-- =========================================================================================== -->
</xsl:stylesheet>
