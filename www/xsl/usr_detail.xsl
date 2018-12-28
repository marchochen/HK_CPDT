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
	<xsl:import href="utils/wb_ui_head.xsl"/>	
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>	


<xsl:import href="utils/escape_js.xsl"/>
<xsl:import href="utils/wb_gen_button.xsl"/>
<xsl:import href="utils/display_time.xsl"/>
<!-- others -->
	<xsl:import href="share/usr_detail_form_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
<xsl:output indent="yes"/>
<!-- =============================================================== -->
<xsl:variable name="user" select="/user_manager/user"/>
<xsl:variable name="user_attribute_list" select="/user_manager/group_member_list/default_grade"/>
<xsl:variable name="supervise_target_list" select="/user_manager/user/supervise_target_list"/>
<xsl:variable name="direct_supervisor" select="/user_manager/user/direct_supervisor"/>
<xsl:variable name="app_approval_usg" select="/user_manager/user/app_approval_usg"/>
<!-- ==============================================-->
<xsl:variable name="target_list" select="/user_manager/user/target_list"/>
<xsl:variable name="all_role_list" select="/user_manager/user/all_role_list"/>
<xsl:variable name="auth_role_list" select="/user_manager/user/auth_rol_list"/>
<xsl:variable name="role_list" select="/user_manager/user/role_list"/>
<xsl:variable name="group_value" select="/user_manager/group_member_list/@id"/>
<xsl:variable name="group_text" select="/user_manager/group_member_list/desc"/>
<xsl:variable name="usr_role_lst" select="/user_manager/user/role_list"/>
<xsl:variable name="usr_role_full_lst" select="/user_manager/user/usr_role_full_list"/>
<xsl:variable name="profile_attributes" select="/user_manager/meta/profile_attributes"/>
<xsl:variable name="meta_usr_ent_id"><xsl:value-of select="/user_manager/meta/cur_usr/@ent_id"/></xsl:variable>
<xsl:variable name="usr_ent_id"><xsl:value-of select="/user_manager/user/@ent_id"/></xsl:variable>
<xsl:variable name="page_variant" select="/user_manager/meta/page_variant"/>
<xsl:variable name="root_ent_id" select="/user_manager/meta/cur_usr/@root_ent_id"/>
<xsl:variable name="disable_role_list" select="/user_manager/user/disable_role_list"/>
<xsl:variable name="parent_ent_id">
<xsl:choose>
	<xsl:when test="count(/user_manager/user/ancestor_node_list/node) = 0"><xsl:value-of select="$root_ent_id"/></xsl:when>
		<xsl:otherwise><xsl:value-of select="/user_manager/user/ancestor_node_list/node[last()]/@id"/></xsl:otherwise>
	</xsl:choose>					
</xsl:variable>	
<xsl:variable name="filter_user_group"/>

<xsl:variable name="lab_select_default_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_select_default_image')"/>
<xsl:variable name="lab_default_images" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_default_images')"/>
<xsl:variable name="lab_upload_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_upload_image')"/>
<xsl:variable name="lab_button_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, '329')"/>
<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/> 	
<!-- =============================================================== -->
<xsl:template match="/">
	<html><xsl:apply-templates/></html>
</xsl:template>
<!-- =============================================================== -->
<xsl:template match="user_manager">
	<head>
		<title><xsl:value-of select="$wb_wizbank"/></title>
		<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
		<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
		<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
		<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
		<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
		<script LANGUAGE="Javascript" TYPE="text/javascript"><![CDATA[
			//]]><xsl:value-of select="$meta_usr_ent_id"/><![CDATA[
			usr = new wbUserGroup;
			stype = getUrlParam('stype')
			stimestamp = getUrlParam('stimestamp')
			ent_id = wb_utils_get_cookie('parent_id');		
		
		]]></script>
		<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
	</head>
	<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
		<form><xsl:call-template name="wb_init_lab"/></form>
	</body>
</xsl:template>
<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_manager">用戶管理 - 資訊維護</xsl:with-param>
			<xsl:with-param name="lab_usr_info">用戶資料</xsl:with-param>	
			<xsl:with-param name="lab_all_users">用戶資訊 </xsl:with-param>			
			<xsl:with-param name="lab_g_txt_btn_cut">剪下</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_restore">復原</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_change_id">重新命名</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_copy">複製</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_reset_pwd">重新設定密碼</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del">刪除</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_manager">用户管理 - 信息维护</xsl:with-param>
			<xsl:with-param name="lab_usr_info">用户资料</xsl:with-param>	
			<xsl:with-param name="lab_all_users">用户信息</xsl:with-param>					
			<xsl:with-param name="lab_g_txt_btn_cut">剪切</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_restore">还原</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_change_id">修改用户名</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_copy">复制</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_reset_pwd">重新设定密码</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del">删除</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_manager">User management - profile maintenance</xsl:with-param>
			<xsl:with-param name="lab_usr_info">User information</xsl:with-param>	
			<xsl:with-param name="lab_all_users">User profile</xsl:with-param>				
			<xsl:with-param name="lab_g_txt_btn_cut">Cut</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_restore">Restore</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_change_id">Change user name</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_copy">Copy</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_reset_pwd">Reset password</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del">Delete</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">Recycle bin</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
<!-- =============================================================== -->
<xsl:template name="content">
	<xsl:param name="lab_usr_manager"/>
	<xsl:param name="lab_usr_info"/>
	<xsl:param name="lab_all_users"/>
	<xsl:param name="lab_g_txt_btn_cut"/>
	<xsl:param name="lab_g_txt_btn_restore"/>
	<xsl:param name="lab_g_txt_btn_change_id"/>
	<xsl:param name="lab_g_txt_btn_copy"/>
	<xsl:param name="lab_g_txt_btn_edit"/>
	<xsl:param name="lab_g_txt_btn_reset_pwd"/>
	<xsl:param name="lab_g_txt_btn_del"/>
	<xsl:param name="lab_lost_and_found"/>
			
	<xsl:call-template name="wb_ui_hdr">
		<xsl:with-param name="belong_module">FTN_AMD_USR_INFO</xsl:with-param>
	</xsl:call-template>
	
	<xsl:call-template name="wb_ui_nav_link">
		<xsl:with-param name="text">
		<xsl:choose>
			<xsl:when test="user/@status = 'DELETED'">
			<a class="NavLink" href="javascript:wb_utils_nav_go('FTN_AMD_USR_INFO',{meta/cur_usr/@ent_id},'{$wb_lang}')">
				<xsl:value-of select="$lab_all_users"/>
			</a>		
			<xsl:text>&#160;&gt;&#160;</xsl:text>	
			<a href="javascript:usr.group.manage_grp_del_usr('1','','','recycleLish')" class="NavLink">
				<xsl:value-of select="$lab_lost_and_found"/>
			</a>
			<xsl:text>&#160;&gt;&#160;</xsl:text>
			<xsl:value-of select="user/name/@display_name"/></xsl:when>
			<xsl:otherwise>
			<a class="NavLink" href="javascript:wb_utils_nav_go('FTN_AMD_USR_INFO',{meta/cur_usr/@ent_id},'{$wb_lang}')">
				<xsl:value-of select="$lab_all_users"/>
			</a>
			<xsl:text>&#160;&gt;&#160;</xsl:text>
			<xsl:for-each select="user/ancestor_node_list/node">
				<a class="NavLink" href="javascript:usr.group.manage_grp('{@id}','','{../../../meta/cur_usr/@root_ent_id}')">
					<xsl:value-of select="title/."/>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
			</xsl:for-each>
			<xsl:value-of select="user/name/@display_name"/>			
			</xsl:otherwise>
		</xsl:choose>

		</xsl:with-param>
	</xsl:call-template>
	
	<!-- parentinfo start -->
	<xsl:call-template name="wb_ui_head">
		<xsl:with-param name="text" select="$lab_usr_info"/>
		<xsl:with-param name="extra_td">
			<td align="right">
				<script LANGUAGE="Javascript" TYPE="text/javascript"><![CDATA[					
					var timestamp_tmp = ']]><xsl:value-of select="user/@timestamp"/><![CDATA['
					usrGroup  = new Array()
					]]><xsl:for-each select="user/user_attribute_list/attribute_list[@type='USG']/entity"><![CDATA[
					usrGroup[]]><xsl:value-of select="position()-1"/><![CDATA[] = ']]><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="@id"/></xsl:with-param></xsl:call-template><![CDATA[';
				]]></xsl:for-each><![CDATA[
				]]></script>
				
				<xsl:choose>
					<xsl:when test="user/user_attribute_list/attribute_list[@type='USG']/entity/@role = 'SYSTEM'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_txt_btn_cut"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:usr.utils.cut(usrGroup,'<xsl:value-of select="$usr_ent_id"/>','USR','<xsl:value-of select="role"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="user/@status = 'DELETED'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_txt_btn_cut"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:usr.utils.cut(usrGroup,'<xsl:value-of select="$usr_ent_id"/>','USR','<xsl:value-of select="role"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_txt_btn_restore"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.restore_exec('<xsl:value-of select="user/@ent_id"/>', 'usr_detail')</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_txt_btn_change_id"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.rename_prep('<xsl:value-of select="user/@ent_id"/>')</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
				</xsl:choose>
				<xsl:if test="$page_variant/@canMaitainUsr = 'true'">
					<xsl:if test="$page_variant/@hasUsrEditBtn = 'true'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_txt_btn_edit"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.edit_prep('<xsl:value-of select="$usr_ent_id"/>','',ent_id,stype,stimestamp,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_txt_btn_reset_pwd"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.reset_pwd_prep('<xsl:value-of select="$usr_ent_id"/>')</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="$page_variant/@hasUsrDelBtn = 'true' and not($meta_usr_ent_id = $usr_ent_id)">
					
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_txt_btn_del"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.del_usr('<xsl:value-of select="$usr_ent_id"/>',<xsl:value-of select="$parent_ent_id"/>,timestamp_tmp,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
				</xsl:if>
<!-- comment for soft-delete
				<xsl:if test="$page_variant/@hasUsrTrashBtn = 'true'">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_txt_btn_del"/></xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.del_trash_usr('<xsl:value-of select="$usr_ent_id"/>',wb_utils_get_cookie('parent_id'),timestamp_tmp,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
					</xsl:call-template>
				</xsl:if>			
-->
			</td>		
		</xsl:with-param>
	</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
	<xsl:apply-templates select="user" mode="display_details"/>
</xsl:template>
<!-- =============================================================== -->
<xsl:template match="user" mode="display_details">
	<table>
		<xsl:call-template name="empty_row"/>
		<xsl:for-each select="$profile_attributes/*">
			<xsl:variable name="show">
				<xsl:choose>
					<xsl:when test="@active"><xsl:value-of select="@active"/></xsl:when>
					<xsl:when test="not(@active)">true</xsl:when>
				</xsl:choose>
			</xsl:variable>
			<!--Debug : <xsl:value-of select="name()"/>:<xsl:value-of select="$show"/><br/>-->
			<xsl:if test="$show = 'true'">
			<xsl:apply-templates select="." mode="profile_attributes">
				<xsl:with-param name="is_end_user">true</xsl:with-param>
				<xsl:with-param name="readonly">all</xsl:with-param>
				<xsl:with-param name="showempty" select="@showempty"/>
			</xsl:apply-templates>
			</xsl:if>
		</xsl:for-each>	
	</table>
</xsl:template>
</xsl:stylesheet>
