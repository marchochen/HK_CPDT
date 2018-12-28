<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
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
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/usr_search_form_share.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl" />
	<xsl:output indent="yes"/>
	
	<xsl:variable name="profile_attributes" select="/user_manager/meta/profile_attributes"/>
	<xsl:variable name="filter_user_group">1</xsl:variable>
	
	<!-- =============================================================== -->
	<xsl:template match="/user_manager">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_adv_search">用戶進階搜索</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_adv_search">用户高级搜索</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_adv_search">User advanced search</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_adv_search"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
			usr = new wbUserGroup;
			var goldenman = new wbGoldenMan;
			function status(){return false;}
			function disable_usergroup(){
					frmXml.usr_group_lst.options[0].text =  ''
					frmXml.usr_group_lst_single.value =  ''
					frmXml.usr_group_lst.options[0].value =  ''
					frmXml.usr_group_lst.disabled = 'disabled';
					frmXml.usr_group_lst.value = '';
			}
			
			function enable_usergroup(){
					frmXml.usr_group_lst.disabled = '';
			}			
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="wb_utils_s_usr_display_focus(document.frmXml);">
			<form name="frmXml" method="post" onsubmit="return status()">
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="ent_id" value="{group_member_list/@id}"/>
				<input type="hidden" name="s_role_types" value=""/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="filter_user_group" value="1"/>
				<!-- navigation -->
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_USR_INFO</xsl:with-param>
					<xsl:with-param name="parent_code">FTN_AMD_USR_INFO</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_nav_link">
					<xsl:with-param name="text" select="$lab_adv_search"/>
				</xsl:call-template>
				<table>
					<xsl:apply-templates select="$profile_attributes/*[(not(@active) or @active = 'true') and (@searchable = 'adv' or @searchable = 'all')]" mode="profile_attributes"/>
					<xsl:call-template name="usr_sort"/>
					<xsl:call-template name="usr_status_or"/>
					<tr>
						<td width="35%" align="right">
						</td>
						<td width="65%" align="left">
						</td>
					</tr>
				</table>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_g_form_btn_ok"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:usr.search.adv_search_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_g_form_btn_cancel"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:wb_utils_nav_go('FTN_AMD_USR_INFO','','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>
				</div>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>
