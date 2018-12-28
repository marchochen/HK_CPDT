<?xml version="1.0"?>
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
	<xsl:variable name="disable_role_list" select="/user_manager/user/disable_role_list"/>
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
	<xsl:variable name="parent_ent_id">
		<xsl:choose>
			<xsl:when test="count(/user_manager/user/ancestor_node_list/node) = 0"><xsl:value-of select="$root_ent_id"/></xsl:when>
				<xsl:otherwise><xsl:value-of select="/user_manager/user/ancestor_node_list/node[last()]/@id"/></xsl:otherwise>
		</xsl:choose>					
	</xsl:variable>	
	<xsl:variable name="cur_user_role" select="/user_manager/meta/cur_usr/role/@id"/>
	<xsl:variable name="entity_assignment_cnt">
		<xsl:value-of select="count(/user_manager/user/all_role_list/role/entity_assignment)"/>
	</xsl:variable>
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
			<xsl:with-param name="lab_usr_acc_reactivation">戶口復原</xsl:with-param>
			<xsl:with-param name="lab_usr_acc_reactivation_desc">要復原用戶戶口，請按頁底的<b>復原</b>按鈕。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_reactivate">復原</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_acc_reactivation">激活用户</xsl:with-param>
			<xsl:with-param name="lab_usr_acc_reactivation_desc">要激活用户帐号，请点击页面底部的<b>激活</b>按钮。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_reactivate">激活</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_acc_reactivation">Account reactivation</xsl:with-param>
			<xsl:with-param name="lab_usr_acc_reactivation_desc">To reactivate the account, click <b>Reactivate</b> at the bottom of the page.</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_reactivate">Reactivate</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- parentinfo start -->
	<xsl:template name="content">
		<xsl:param name="lab_usr_acc_reactivation"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_reactivate"/>
		<xsl:param name="lab_usr_acc_reactivation_desc"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			var goldenman = new wbGoldenMan	
			usr = new wbUserGroup				
			stype = getUrlParam('stype')
			stimestamp = getUrlParam('stimestamp')
			ent_id = getUrlParam('ent_id')		
					
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="wb_utils_gen_form_focus(document.frmXml)">
			<form name="frmXml">
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="usr_ent_id" value="{@ent_id}"/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="ent_id_lst"/>
					
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_USR_ACTIVATE</xsl:with-param>
					<xsl:with-param name="parent_code">FTN_AMD_USR_ACTIVATE</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_usr_acc_reactivation"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text" select="$lab_usr_acc_reactivation_desc"/>
				</xsl:call-template>
				<xsl:call-template name="draw_detail_lst">
				</xsl:call-template>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_reactivate"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:usr.activate.reactivate_exec(document.frmXml, document.frmXml.usr_ent_id.value)</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_g_form_btn_cancel"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:usr.activate.reactivate_lst()</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>
				</div>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="draw_detail_lst">
		<!-- section 1 -->
		<table>
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
					<xsl:with-param name="hiddenNoValue">true</xsl:with-param>
					<xsl:with-param name="is_end_user">true</xsl:with-param>
					<xsl:with-param name="readonly">true</xsl:with-param>
				</xsl:apply-templates>
				</xsl:if>
			</xsl:for-each>	
		</table>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
