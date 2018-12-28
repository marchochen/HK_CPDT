<?xml version="1.0" encoding="UTF-8"?>
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
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<!-- cust utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- others -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="cur_user_role" select="/user_manager/meta/cur_usr/role/@id"/>
	<xsl:variable name="usr_role_lst" select="/user_manager/user/role_list"/>
	<xsl:variable name="usr_role_full_lst" select="/user_manager/user/usr_role_full_list"/>
	<xsl:variable name="pwd_min_length" select="/user_manager/meta/profile_attributes/password/@min_length"/>
	<xsl:variable name="pwd_max_length" select="/user_manager/meta/profile_attributes/password/@max_length"/>
	<xsl:variable name="usr_id_min_length" select="/user_manager/meta/profile_attributes/user_id/@min_length"/>
	<xsl:variable name="usr_id_max_length" select="/user_manager/meta/profile_attributes/user_id/@max_length"/>
	
	<xsl:variable name="entity_assignment_cnt">
		<xsl:value-of select="count(/user_manager/user/all_role_list/role/entity_assignment)"/>
	</xsl:variable>
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
			<xsl:with-param name="lab_change_id">更改用戶</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_original_id">原始<xsl:value-of select="$lab_login_id"/></xsl:with-param>
			<xsl:with-param name="lab_new_id">新建<xsl:value-of select="$lab_login_id"/></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_change_id">更改用户</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_original_id">原始<xsl:value-of select="$lab_login_id"/></xsl:with-param>
			<xsl:with-param name="lab_new_id">新建<xsl:value-of select="$lab_login_id"/></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_change_id">Change ID</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_original_id">Original <xsl:value-of select="$lab_login_id"/></xsl:with-param>
			<xsl:with-param name="lab_new_id">New <xsl:value-of select="$lab_login_id"/></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- parentinfo start -->
	<xsl:template name="content">
		<xsl:param name="lab_change_id"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_original_id"/>
		<xsl:param name="lab_new_id"/>
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
			ent_id = getUrlParam('ent_id')		
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="wb_utils_gen_form_focus(document.frmXml)">
			<form name="frmXml" onsubmit="return false;">
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="usr_ent_id" value="{@ent_id}"/>
				<input type="hidden" name="charset" value="{$encoding}"/>
				<input type="hidden" name="usr_occupation_bil" value="{occupation}"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="usr_timestamp" value="{@timestamp}"/>
				<input type="hidden" name="usr_attribute_ent_id_lst" value=""/>
				<input type="hidden" name="usr_attribute_relation_type_lst" value=""/>
				<input type="hidden" name="pwd_min_length" value="{$pwd_min_length}"/>
				<input type="hidden" name="pwd_max_length" value="{$pwd_max_length}"/>
				<input type="hidden" name="usr_id_min_length" value="{$usr_id_min_length}"/>
				<input type="hidden" name="usr_id_max_length" value="{$usr_id_max_length}"/>

				<!-- navigation -->
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_USR_INFO</xsl:with-param>
					<xsl:with-param name="parent_code">FTN_AMD_USR_INFO</xsl:with-param>
				    <xsl:with-param name="page_title">
				   <!--   <xsl:call-template name="get_lab">
					    <xsl:with-param name="lab_title">lab_change_usr</xsl:with-param>
				      </xsl:call-template>  --> 
			        </xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text"><xsl:value-of select="$lab_change_id"/><xsl:text>&#160;-&#160;</xsl:text><xsl:value-of select="name/@display_name"/></xsl:with-param>
				</xsl:call-template>
				<table>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_original_id"/>：
						</td>
						<td class="wzb-form-control">
							<xsl:value-of select="@id"/>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$lab_new_id"/>：
						</td>
						<td class="wzb-form-control">
								<input class="wzb-inputText" type="text" name="usr_id" size="{../meta/profile_attributes/user_id/@max_length}" style="width:300px;" maxlength="{../meta/profile_attributes/user_id/@max_length}"/>
								<br/>
								<xsl:copy-of select="$lab_id_requirement"/>
							<input name="usr_id_req_fld" type="hidden" value="true"/>
							<input name="lab_login_id" type="hidden" value="{$lab_login_id}"/>
						</td>
					</tr>
				</table>
				<input type="hidden" name="usr_display_bil" value="{name/@display_name}"/>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.rename_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.manage_usr('<xsl:value-of select="@ent_id"/>','','','','')</xsl:with-param>
					</xsl:call-template>
				</div>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>
