<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- UI -->
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/escape_doub_quo.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<!-- cust utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- others -->
	<xsl:import href="share/usr_detail_form_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:output indent="yes"/>
<!-- =============================================================== -->
<xsl:variable name="user"/>
<xsl:variable name="user_attribute_list"/>
<xsl:variable name="group_text"/>
<xsl:variable name="group_value"/>
<xsl:variable name="direct_supervisor"/>
<xsl:variable name="app_approval_usg"/>
<xsl:variable name="auth_role_list"/>
<xsl:variable name="role_list"/>
<xsl:variable name="all_role_list"/>
<xsl:variable name="target_list"/>
<xsl:variable name="supervise_target_list"/>
<xsl:variable name="disable_role_list" select="/user_manager/user/disable_role_list"/>	
	<!-- =============================================================== -->
	<xsl:variable name="cur_user_role" select="/user_manager/meta/cur_usr/role/@id"/>
	<xsl:variable name="usr_role_lst" select="/user_manager/user/role_list"/>
	<xsl:variable name="usr_role_full_lst" select="/user_manager/user/usr_role_full_list"/>
	<xsl:variable name="pwd_min_length" select="/user_manager/meta/profile_attributes/password/@min_length"/>
	<xsl:variable name="pwd_max_length" select="/user_manager/meta/profile_attributes/password/@max_length"/>

	<xsl:variable name="root_ent_id" select="/user_manager/meta/cur_usr/@root_ent_id"/>
	<xsl:variable name="entity_assignment_cnt">
		<xsl:value-of select="count(/user_manager/user/all_role_list/role/entity_assignment[contains(@type,'USG')])"/>
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
			<xsl:with-param name="lab_change_pwd">修改<xsl:value-of select="$lab_passwd"/></xsl:with-param>
			<xsl:with-param name="lab_pwd_info"><xsl:value-of select="$lab_passwd"/>資料</xsl:with-param>
			<xsl:with-param name="lab_pwd_desc">輸入舊<xsl:value-of select="$lab_passwd"/>及新<xsl:value-of select="$lab_passwd"/>。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_change_pwd">修改<xsl:value-of select="$lab_passwd"/></xsl:with-param>
			<xsl:with-param name="lab_pwd_info"><xsl:value-of select="$lab_passwd"/>资料</xsl:with-param>
			<xsl:with-param name="lab_pwd_desc">输入旧<xsl:value-of select="$lab_passwd"/>及新<xsl:value-of select="$lab_passwd"/>。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_change_pwd">Change password</xsl:with-param>
			<xsl:with-param name="lab_pwd_info"><xsl:value-of select="$lab_passwd"/> information</xsl:with-param>
			<xsl:with-param name="lab_pwd_desc">Enter your old and new <xsl:value-of select="$lab_passwd"/>.</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- parentinfo start -->
	<xsl:template name="content">
		<xsl:param name="lab_change_pwd"/>
		<xsl:param name="lab_pwd_info"/>
		<xsl:param name="lab_pwd_desc"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			usr = new wbUserGroup				
			stype = getUrlParam('stype')
			stimestamp = getUrlParam('stimestamp')
			ent_id = getUrlParam('ent_id')		
			
			function init(){
			}			
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="init();wb_utils_gen_form_focus(document.frmXml)">
			<form name="frmXml">
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="usr_ent_id" value="{@ent_id}"/>
				<input type="hidden" name="charset" value="{$encoding}"/>
				<input type="hidden" name="usr_occupation_bil" value="{occupation}"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="usr_timestamp" value="{@timestamp}"/>
				<input type="hidden" name="pwd_min_length" value="{$pwd_min_length}"/>
				<input type="hidden" name="pwd_max_length" value="{$pwd_max_length}"/>				
				<!-- navigation -->
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_USR_INFO</xsl:with-param>
				</xsl:call-template>
				
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text"><xsl:value-of select="$lab_change_pwd"/></xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text"><xsl:value-of select="$lab_pwd_desc"/></xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="draw_detail_lst">
					<xsl:with-param name="lab_pwd_info"><xsl:value-of select="$lab_pwd_info"/></xsl:with-param>
				</xsl:call-template>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_ok"/></xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.upd_pwd_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_cancel"/></xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:wb_utils_gen_home()</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>
				</div>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="draw_detail_lst">
		<xsl:param name="lab_pwd_info"/>

		<!-- section 1 -->
		<table>
			<xsl:call-template name="usr_id">
				<xsl:with-param name="element_value"><xsl:value-of select="@id"/></xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="usr_old_pwd">
				<xsl:with-param name="element_value"/>
			</xsl:call-template>
			<xsl:call-template name="usr_new_pwd">
				<xsl:with-param name="max_length" select="../meta/profile_attributes/password/@max_length"/>
				<xsl:with-param name="element_value"/>
			</xsl:call-template>
			<xsl:call-template name="confirm_usr_pwd">
				<xsl:with-param name="element_value"/>
			</xsl:call-template>
		</table>
		<xsl:call-template name="usr_detail_seperate_footer"/>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
