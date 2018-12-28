<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl" />
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl" />
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl" />
	<xsl:import href="utils/wb_ui_title.xsl" />
	<xsl:import href="utils/wb_ui_desc.xsl" />
	<xsl:import href="utils/wb_ui_hdr.xsl" />
	<xsl:import href="utils/wb_ui_line.xsl" />
	<xsl:import href="utils/wb_ui_footer.xsl" />
	<xsl:import href="utils/wb_ui_pagination.xsl" />
	<xsl:import href="utils/wb_ui_show_no_item.xsl" />
	<xsl:import href="utils/wb_goldenman.xsl" />
	<xsl:import href="utils/wb_gen_form_button.xsl" />
	<xsl:import href="utils/display_form_input_time.xsl" />
	<xsl:import href="utils/escape_js.xsl" />
	<xsl:import href="utils/select_all_checkbox.xsl" />
	<xsl:import href="share/sys_tab_share.xsl" />
	<xsl:output indent="yes" />
	<!-- =============================================================== -->
	<xsl:variable name="lab_setting" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_setting')" />
	<xsl:variable name="lab_sys_user" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_sys_user')" />
	<xsl:variable name="lab_sys_log" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_sys_log')" />
	<xsl:variable name="lab_sys_setting" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_sys_setting')" />
	<xsl:variable name="lab_btn_save" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '159')" />
	<xsl:variable name="lab_sys_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_sys_status')" />
	<xsl:variable name="lab_sys_enabled" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_sys_enabled')" />
	<xsl:variable name="lab_sys_disabled" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_sys_disabled')" />
	<xsl:variable name="lab_ldap_url" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_ldap_url')" />
	<xsl:variable name="lab_ldap_suffix" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_ldap_suffix')" />
	<xsl:variable name="lab_openoffice_environment" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_openoffice_environment')" />
	<xsl:variable name="lab_openoffice_path" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_openoffice_path')" />
	<xsl:variable name="lab_openoffice_host" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_openoffice_host')" />
	<xsl:variable name="lab_openoffice_port" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_openoffice_port')" />
	<xsl:variable name="lab_wechat" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_wechat')"/>
	<xsl:variable name="lab_wechat_domain" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_wechat_domain')" />
	<xsl:variable name="lab_wechat_mobile_domain" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_wechat_mobile_domain')" />
	<xsl:variable name="lab_wechat_server_id" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_wechat_server_id')" />
	<xsl:variable name="lab_wechat_port" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_wechat_port')" />
	<xsl:variable name="lab_wechat_max_message" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_wechat_max_message')" />
	<xsl:variable name="lab_wechat_trial_account_enabled" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_wechat_trial_account_enabled')" />
	<xsl:variable name="lab_mail_server_host" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_mail_server_host')" />
	<xsl:variable name="lab_mail_server_auth_enabled" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_mail_server_auth_enabled')" />
	<xsl:variable name="lab_mail_server_account_type" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_mail_server_account_type')" />
	<xsl:variable name="lab_mail_server_user" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_mail_server_user')" />
	<xsl:variable name="lab_mail_server_password" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_mail_server_password')" />
	<xsl:variable name="lab_mail_scheduler_domain" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_mail_scheduler_domain')" />
	<xsl:variable name="lab_openoffice_host_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_openoffice_host_desc')" />
	<xsl:variable name="lab_mail_server_host_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_mail_server_host_desc')" />
	<xsl:variable name="lab_mail_server_auth_enabled_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_mail_server_auth_enabled_desc')" />
	<xsl:variable name="lab_mail_server_user_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_mail_server_user_desc')" />
	<xsl:variable name="lab_mail_server_password_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_mail_server_password_desc')" />
	<xsl:variable name="lab_wechat_domain_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_wechat_domain_desc')" />
	<xsl:variable name="lab_mail_scheduler_domain_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_mail_scheduler_domain_desc')" />
	<xsl:variable name="lab_wechat_server_id_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_wechat_server_id_desc')" />
	<xsl:variable name="lab_wechat_port_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_wechat_port_desc')" />
	<xsl:variable name="lab_wechat_max_message_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_wechat_max_message_desc')" />
	<xsl:variable name="lab_ldap_url_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_ldap_url_desc')" />
	<xsl:variable name="lab_ldap_suffix_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_ldap_suffix_desc')" />
	<xsl:variable name="lab_sys_status_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_sys_status_desc')" />
	<xsl:variable name="lab_openoffice_environment_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_openoffice_environment_desc')" />
	<xsl:variable name="lab_openoffice_path_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_openoffice_path_desc')" />
	<xsl:variable name="lab_openoffice_port_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_openoffice_port_desc')" />
	<xsl:variable name="lab_upload_forbidden_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_upload_forbidden_desc')" />
	<xsl:variable name="lab_mail_server" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_mail_server')" />
	<xsl:variable name="label_core_system_setting_129" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_129')" />
	<xsl:variable name="label_core_system_setting_130" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_130')" />
	<xsl:variable name="label_core_system_setting_131" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_131')" />
	<xsl:variable name="label_core_system_setting_132" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_132')" />
	<xsl:variable name="label_core_system_setting_133" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_133')" />
	<xsl:variable name="label_core_system_setting_134" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_134')" />
	<xsl:variable name="label_core_system_setting_135" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_135')" />
	<xsl:variable name="label_core_system_setting_136" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_136')" />
	<xsl:variable name="label_core_system_setting_137" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_137')" />
	<!-- =============================================================== -->
	<xsl:variable name="config" select="/system_setting/config"></xsl:variable>
	<xsl:variable name="lab_sys_wechat" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_sys_wechat')" />
	<!-- =============================================================== -->
	<xsl:variable name="lab_upload_constraint" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_upload_constraint')" />
	<xsl:variable name="lab_upload_forbidden" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_upload_forbidden')" />
	<xsl:variable name="label_core_system_setting_145" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_145')"/>
	<xsl:variable name="label_core_system_setting_149" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_149')"/>
	<xsl:variable name="label_core_system_setting_150" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_150')"/>
	<xsl:variable name="label_core_system_setting_151" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_151')"/>
	<xsl:variable name="label_core_system_setting_152" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_152')"/>
	<xsl:variable name="label_core_system_setting_153" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_153')"/>
	<xsl:variable name="label_core_system_setting_154" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_154')"/>
	<xsl:variable name="label_core_system_setting_155" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_155')"/>
	<xsl:variable name="label_core_system_setting_156" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_156')"/>
	<xsl:variable name="label_core_system_setting_157" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_157')"/>
	<xsl:variable name="label_core_system_setting_203" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_203')"/>
	<xsl:variable name="label_core_system_setting_204" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_204')"/>
	<xsl:variable name="lab_days" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_days')"/>
	<xsl:variable name="lab_times" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '429')"/>
	<xsl:variable name="minLength" select="$config/password_policy/@minLength"/>
	<xsl:variable name="maxLength" select="$config/password_policy/@maxLength"/>
	
	<xsl:variable name="lab_passwd_length">
		<xsl:choose><xsl:when test="$wb_lang = 'ch'">請使用英文字母/數字/底線/橫線的組合，必须同时包含英文字母和数字。<br/>長度不短過: <xsl:value-of select="$minLength"/><br/>長度不超過: <xsl:value-of select="$maxLength"/></xsl:when><xsl:when test="$wb_lang = 'gb'">请使用纯英文字母/数字/下划线/横线的组合，必须同时包含英文字母和数字。<br/>最小长度：<xsl:value-of select="$minLength"/><br/>最大长度： <xsl:value-of select="$maxLength"/></xsl:when><xsl:otherwise>Please use a combination of lower letters or uppercase letters / numbers / underline / hyphen, must contain letters and numbers.<br/>Minimum characters required: <xsl:value-of select="$minLength"/><br/>Maximum characters allowed: <xsl:value-of select="$maxLength"/></xsl:otherwise></xsl:choose>
	</xsl:variable>
	
	<xsl:template match="/">
		<html>
			<xsl:call-template name="main" />
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}" />
			<title>
				<xsl:value-of select="$wb_wizbank" />
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js" />
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js" />
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js" />
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_threshold.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_system_setting.js" />
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js" />
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js" language="JavaScript"/>
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/label_ss_{$wb_cur_lang}.js" language="JavaScript"/>
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/label_{$wb_cur_lang}.js" language="JavaScript"/>
			<script language="Javascript"><![CDATA[
					threshold = new wbThreshold();
					systemSetting = new wbSystemSetting();
			]]>
			</script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0"  >
			<form name="frmXml">
				<input type="hidden" name="module" />
				<input type="hidden" name="cmd" />
				<input type="hidden" name="url_success" />
				<input type="hidden" name="url_failure" />
				<input type="hidden" name="type" value='sys' />

				<xsl:call-template name="content" />
			</form>
		</body>
	</xsl:template>
	<xsl:template name="content">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_SYS_SETTING_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_SYS_SETTING_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$label_core_system_setting_149"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="sys_gen_tab">
			<xsl:with-param name="tab_1" select="$label_core_system_setting_145" />
			<xsl:with-param name="tab_1_href">
				<xsl:text>javascript:wb_utils_nav_go('FTN_AMD_SYS_SETTING_MAIN')</xsl:text>
			</xsl:with-param>
			<xsl:with-param name="tab_2" select="$label_core_system_setting_149" />
			<xsl:with-param name="tab_2_href">
				<xsl:text>javascript:systemSetting.prep('sys')</xsl:text>
			</xsl:with-param>
			
			<xsl:with-param name="current_tab" select="$label_core_system_setting_149" />
		</xsl:call-template>
		<table>
			<!-- ================================= 邮件服务器 ================================= -->
			<tr>
				<td class="wzb-form-label">
					<b>
						<xsl:value-of select="$lab_mail_server" />
					</b>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_mail_server_host" />
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<input class="wzb-inputText" id="mail_server_host" type="text" maxlength="255" size="60" name="mail_server_host">
						<xsl:if test="$config/mail/mail_server_host != ''">
							<xsl:attribute name="value">
								<xsl:value-of select="$config/mail/mail_server_host" />
							</xsl:attribute>
						</xsl:if>
					</input>
					<span style="margin-left:26px;" class="wzb-ui-desc-text"><xsl:value-of select="$lab_mail_server_host_desc" /></span>
				</td>
			</tr>
			
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_mail_server_auth_enabled" />
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<span>
						<input id="wechat_trial_account_enabled_val_0" type="radio" name="mail_server_auth_enabled" value="true">
							<xsl:if test="$config/mail/mail_server_auth_enabled = 'true'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<label for="wechat_trial_account_enabled_val_0" class="margin-right4">
							<xsl:value-of select="$lab_sys_enabled" />
						</label>
					</span>
					<span class="margin-left10">
						<input id="wechat_trial_account_enabled_val_1" type="radio" name="mail_server_auth_enabled" value="false">
							<xsl:if test="$config/mail/mail_server_auth_enabled = 'false'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<label for="wechat_trial_account_enabled_val_1">
							<xsl:value-of select="$lab_sys_disabled" />
						</label>
					</span>
					<span style="margin-left:26px;" class="wzb-ui-desc-text"><xsl:value-of select="$lab_mail_server_auth_enabled_desc" /></span>
				</td>
			</tr>
			
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_mail_server_user" />
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<input class="wzb-inputText" id="mail_server_user" type="text" maxlength="255" size="60" name="mail_server_user">
						<xsl:if test="$config/mail/mail_server_user != ''">
							<xsl:attribute name="value">
								<xsl:value-of select="$config/mail/mail_server_user" />
							</xsl:attribute>
						</xsl:if>
					</input>
					<span style="margin-left:26px;" class="wzb-ui-desc-text"><xsl:value-of select="$lab_mail_server_user_desc" /></span>
				</td>
			</tr>
			
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_mail_server_password" />
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<input class="wzb-inputText" id="mail_server_password" type="password" maxlength="255" size="60" name="mail_server_password">
						<xsl:if test="$config/mail/mail_server_password != ''">
							<xsl:attribute name="value">
								<xsl:value-of select="$config/mail/mail_server_password" />
							</xsl:attribute>
						</xsl:if>
					</input>
					<span style="margin-left:26px;" class="wzb-ui-desc-text"><xsl:value-of select="$lab_mail_server_password_desc" /></span>
				</td>
			</tr>
			
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_mail_scheduler_domain" />
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<select name="protocol" id="protocol" style="width:85px;">
						<xsl:choose>
							<xsl:when test="contains($config/mail/mail_scheduler_domain, 'https://')">
								<option value="http://">http://</option>
								<option value="https://" selected="selected">https://</option>
							</xsl:when>
							<xsl:otherwise>
								<option value="http://" selected="selected">http://</option>
								<option value="https://">https://</option>
							</xsl:otherwise>
						</xsl:choose>>
					</select>
					<input class="wzb-inputText" id="mail_scheduler_domain" type="text" maxlength="255" size="48" name="mail_scheduler_domain">
						<xsl:if test="$config/mail/mail_scheduler_domain != ''">
							<xsl:attribute name="value">
								<xsl:choose>
									<xsl:when test="contains($config/mail/mail_scheduler_domain, 'https://')">
										<xsl:value-of select="substring-after($config/mail/mail_scheduler_domain, 'https://')"></xsl:value-of>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="substring-after($config/mail/mail_scheduler_domain, 'http://')"></xsl:value-of>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
						</xsl:if>
					</input>
					<span style="margin-left:26px;" class="wzb-ui-desc-text"><xsl:value-of select="$lab_mail_scheduler_domain_desc" /></span>
				</td>
			</tr>
			

			<!-- ================================= LDAP ================================= -->
		<!--
			<tr>
				<td class="wzb-form-label">
					<b>
						<xsl:text>LDAP</xsl:text>
					</b>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_ldap_url" />
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<input class="wzb-inputText" id="ldap_url" type="text" maxlength="255" size="60" name="ldap_url">
						<xsl:if test="$config/ldap/ldap_url != ''">
							<xsl:attribute name="value">
								<xsl:value-of select="$config/ldap/ldap_url" />
							</xsl:attribute>
						</xsl:if>
					</input>
					<span style="margin-left:26px;" class="wzb-ui-desc-text"><xsl:value-of select="$lab_ldap_url_desc" /></span>
				</td>
			</tr>
			
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_ldap_suffix" />
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<input class="wzb-inputText" id="ldap_suffix" type="text" maxlength="255" size="60" name="ldap_suffix">
						<xsl:if test="$config/ldap/ldap_suffix != ''">
							<xsl:attribute name="value">
								<xsl:value-of select="$config/ldap/ldap_suffix" />
							</xsl:attribute>
						</xsl:if>
					</input>
					<span style="margin-left:26px;" class="wzb-ui-desc-text"><xsl:value-of select="$lab_ldap_suffix_desc" /></span>
				</td>
			</tr>
			-->
			
			<!-- ================================= OpenOffice ================================= --> 
			<!-- <table id="open">
				<tr>
					<td class="wzb-form-label">
						<b>
							<xsl:value-of select="$label_core_system_setting_131" />
						</b>
					</td>
				</tr>
				<tr>
					<td class="wzb-form-label">
						<xsl:value-of select="$lab_sys_status" />
						<xsl:text>：</xsl:text>
					</td>
					<td class="wzb-form-control">
						<span>
							<input id="openoffice_enabled_val_0" type="radio" name="openoffice_enabled" value="true">
								<xsl:if test="$config/openoffice/openoffice_enabled = 'true'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
							</input>
							<label for="openoffice_enabled_val_0" class="margin-right4">
								<xsl:value-of select="$lab_sys_enabled" />
							</label>
						</span>
						<span class="margin-left10">
							<input id="openoffice_enabled_val_1" type="radio" name="openoffice_enabled" value="false">
								<xsl:choose>
									<xsl:when test="$config/openoffice/openoffice_enabled = ''">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:when>
									<xsl:when test="$config/openoffice/openoffice_enabled = 'false'">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:when>
								</xsl:choose>
							</input>
							<label for="openoffice_enabled_val_1">
								<xsl:value-of select="$lab_sys_disabled" />
							</label>
						</span>
						<span style="margin-left:26px;" class="wzb-ui-desc-text"><xsl:value-of select="$lab_sys_status_desc" /></span>
					</td>
				</tr>
				
				<tr>
					<td class="wzb-form-label">
						<xsl:value-of select="$lab_openoffice_environment" />
						<xsl:text>：</xsl:text>
					</td>
					<td class="wzb-form-control">
						<span>
							<input id="openoffice_environment_val_0" type="radio" name="openoffice_environment" value="Windows">
								<xsl:if test="$config/openoffice/openoffice_environment = 'Windows'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
							</input>
							<label for="openoffice_environment_val_0" class="margin-right4">
								<xsl:text>Windows</xsl:text>
							</label>
						</span>
						<span  class="margin-left10">
							<input id="openoffice_environment_val_1" type="radio" name="openoffice_environment" value="Linux">
								<xsl:if test="$config/openoffice/openoffice_environment = 'Linux'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
							</input>
							<label for="openoffice_environment_val_1">
								<xsl:text>Linux</xsl:text>
							</label>
						</span>
						<span style="margin-left:26px;" class="wzb-ui-desc-text"><xsl:value-of select="$lab_openoffice_environment_desc" /></span>
					</td>
				</tr>
				
				<tr>
					<td class="wzb-form-label">
						<xsl:value-of select="$lab_openoffice_path" />
						<xsl:text>：</xsl:text>
					</td>
					<td class="wzb-form-control">
						<input class="wzb-inputText" id="openoffice_path" type="text" maxlength="255" size="60" name="openoffice_path">
							<xsl:if test="$config/openoffice/openoffice_path != ''">
								<xsl:attribute name="value">
									<xsl:value-of select="$config/openoffice/openoffice_path" />
								</xsl:attribute>
							</xsl:if>
						</input>
						<span style="margin-left:26px;" class="wzb-ui-desc-text"><xsl:value-of select="$lab_openoffice_path_desc" /></span>
					</td>
				</tr>
				
				<tr>
					<td class="wzb-form-label">
						<xsl:value-of select="$lab_openoffice_host" />
						<xsl:text>：</xsl:text>
					</td>
					<td class="wzb-form-control">
						<input class="wzb-inputText" id="openoffice_host" type="text" maxlength="255" size="60" name="openoffice_host">
							<xsl:if test="$config/openoffice/openoffice_host != ''">
								<xsl:attribute name="value">
									<xsl:value-of select="$config/openoffice/openoffice_host" />
								</xsl:attribute>
							</xsl:if>
						</input>
						<span style="margin-left:26px;" class="wzb-ui-desc-text"><xsl:value-of select="$lab_openoffice_host_desc" /></span>
					</td>
				</tr>
				
				<tr>
					<td class="wzb-form-label">
						<xsl:value-of select="$lab_openoffice_port" />
						<xsl:text>：</xsl:text>
					</td>
					<td class="wzb-form-control">
						<input class="wzb-inputText" id="openoffice_port" type="text" maxlength="255" size="6" name="openoffice_port">
							<xsl:if test="$config/openoffice/openoffice_port != ''">
								<xsl:attribute name="value">
									<xsl:value-of select="$config/openoffice/openoffice_port" />
								</xsl:attribute>
							</xsl:if>
						</input>
						<span style="margin-left:26px;" class="wzb-ui-desc-text"><xsl:value-of select="$lab_openoffice_port_desc" /></span>
					</td>
				</tr>
			</table>	 -->
			<!-- ================================= I Doc View ================================= --> 
			<table id="idoc">
			   <tr>
				<td class="wzb-form-label">
					<b>
						<xsl:value-of select="$label_core_system_setting_130" />
					</b>
				</td>
			 </tr>
			 <tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$label_core_system_setting_136" />
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control"> 
					<input class="wzb-inputText" id="i_doc_view_preview_IP" type="text" maxlength="255" size="60" name="i_doc_view_preview_IP">
						<xsl:if test="$config/i_doc_view/preview_IP != ''">
							<xsl:attribute name="value">
								<xsl:value-of select="$config/i_doc_view/preview_IP" />
							</xsl:attribute>
						</xsl:if>
					</input>
				</td>
			</tr>
			 <tr>
			  <td></td>
			  <td class="wzb-form-control"><span class="wzb-ui-desc-text"><xsl:value-of select="$label_core_system_setting_137" /></span></td>
			</tr>
			 <tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$label_core_system_setting_135" />
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<input class="wzb-inputText" id="i_doc_view_host" type="text" maxlength="255" size="60" name="i_doc_view_host">
						<xsl:if test="$config/i_doc_view/host != ''">
							<xsl:attribute name="value">
								<xsl:value-of select="$config/i_doc_view/host" />
							</xsl:attribute>
						</xsl:if>
					</input>
				</td>
			</tr>
			<tr>
			  <td></td>
			  <td class="wzb-form-control"><span class="wzb-ui-desc-text"><xsl:value-of select="$label_core_system_setting_132" /></span></td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$label_core_system_setting_129" />
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<input class="wzb-inputText" id="i_doc_view_preview_host" type="text" maxlength="255" size="60" name="i_doc_view_preview_host">
						<xsl:if test="$config/i_doc_view/preview_host != ''">
							<xsl:attribute name="value">
								<xsl:value-of select="$config/i_doc_view/preview_host" />
							</xsl:attribute>
						</xsl:if>
					</input>
				</td>
			</tr>
			 <tr>
			  <td></td>
			  <td class="wzb-form-control"><span class="wzb-ui-desc-text"><xsl:value-of select="$label_core_system_setting_131" /></span></td>
			</tr>
			
			<!--<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$label_core_system_setting_133" />
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">  -->
					<input  class="wzb-inputText" id="i_doc_view_token" type="hidden" maxlength="255" size="60" name="i_doc_view_token">
						<xsl:if test="$config/i_doc_view/token != ''">
							<xsl:attribute name="value">
								<xsl:value-of select="$config/i_doc_view/token" />
							</xsl:attribute>
						</xsl:if>
					</input>
			 <!--  </td>
			</tr>
			<tr>
			  <td></td>
			  <td class="wzb-form-control"><span class="wzb-ui-desc-text"><xsl:value-of select="$label_core_system_setting_134" /></span></td>
			</tr> -->
		  </table>
			
			<table>
			<!-- ================================= WeTeam ================================= --> 
			<!-- <tr>
				<td width="20%" align="right" nowrap="nowrap" valign="bottom" class="wzb-form-label">
					<span class="TitleText">
						<b>
							<xsl:text>WeTeam</xsl:text>
						</b>
					</span>
				</td>
			</tr>
			
			<tr>
				<td width="20%" align="right" nowrap="nowrap" valign="bottom" class="wzb-form-label">
					<span class="TitleText">
						url：
					</span>
				</td>
				<td width="80%" align="left" valign="bottom"  class="wzb-form-control">
					<input class="wzb-inputText" id="openoffice_port" type="text" maxlength="255" size="25" name="webTeam_url">
						<xsl:if test="$config/weTeam/url != ''">
							<xsl:attribute name="value">
								<xsl:value-of select="$config/weTeam/url" />
							</xsl:attribute>
						</xsl:if>
					</input>
				</td>
			</tr> -->
			
			<tr>
				<td width="20%" align="right" nowrap="nowrap" valign="bottom" class="wzb-form-label">
					<span class="TitleText">
						<b>
							<xsl:value-of select="$lab_upload_constraint" />
						</b>
					</span>
				</td>
			</tr>
			
			<tr>
				<td width="20%" align="right" nowrap="nowrap" valign="bottom" class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_upload_forbidden" />：
					</span>
				</td>
				<td class="wzb-form-control">
					<input class="wzb-inputText" id="upload_forbidden" type="text" maxlength="255" size="60" name="upload_forbidden">
						<xsl:if test="$config/upload/forbidden != ''">
							<xsl:attribute name="value">
								<xsl:value-of select="$config/upload/forbidden" />
							</xsl:attribute>
						</xsl:if>
						<xsl:if test="$config/upload/forbidden = ''">
						   <xsl:attribute name="value">Jsp, asp,exe,bat,bin,php,sys,com, Mach-O,ELF,dll, reg</xsl:attribute>
					    </xsl:if>
					</input>
				</td>
				
			</tr>
			<tr>
				<td></td>
				<td>
					<span style="margin-left:10px;" class="wzb-ui-desc-text">
						<xsl:value-of select="$lab_upload_forbidden_desc" />
					</span>
				</td>
			</tr>
			
			<!-- 密码安全设置策略 -->
			<tr>
               <td class="wzb-form-label" valign="bottom">
                   <span class="font"><b><xsl:value-of select="$label_core_system_setting_154"/></b></span>
               </td>
            </tr>
            <tr>
                <td></td>
                <td class="wzb-form-control">
                    <div style="color:#999;">
                    	   <xsl:value-of select="$label_core_system_setting_157"></xsl:value-of><br/>
			               <xsl:copy-of select="$lab_passwd_length"></xsl:copy-of>
                    </div>
                </td>
            </tr> 
<!--
            <tr>
                <td></td>
                <td class="wzb-form-control">
                   	<xsl:value-of select="$label_core_system_setting_155"></xsl:value-of>
                </td>
            </tr>  
-->
            <tr>
               <td class="wzb-form-label" valign="top"><xsl:value-of select="$label_core_system_setting_150"></xsl:value-of>：</td>
               <td class="wzb-form-control">
                   <table>
                       <tbody>
                           <tr>
                               <td>
                                   <span class="wbFormRightText">
                                       <label>
                                       	<input class="wzb-inputText" style="margin-right:5px;" value="" size="6" maxlength="10" name="password_policy_period" type="Text">
	                                       	<xsl:if test="$config/password_policy/period != ''">
												<xsl:attribute name="value">
													<xsl:value-of select="$config/password_policy/period"/>
												</xsl:attribute>
											</xsl:if>
                                       	</input>
                                       	<xsl:value-of select="$lab_days"></xsl:value-of>
									   </label>
                                       <label>
                                       	<input name="password_policy_period_force" type="checkbox" value="1">
	                                       	<xsl:if test="$config/password_policy/period/@force = '1'">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
                                       	</input>
                                       	<xsl:value-of select="$label_core_system_setting_152"></xsl:value-of>
                                       </label>
                                   </span>
                               </td>
                           </tr>
                           <tr>
                               <td class="wzb-ui-desc-text">
						       	 <xsl:value-of select="$label_core_system_setting_153" disable-output-escaping="yes"></xsl:value-of>
                               </td>
                           </tr>
                       </tbody>
                   </table>
               </td>
            </tr>
            <tr>
                <td class="wzb-form-label" valign="top"><xsl:value-of select="$label_core_system_setting_151"></xsl:value-of>：</td>
                <td class="wzb-form-control">
                    <table>
                        <tbody>
                            <tr>
                                <td>
                                    <input class="wzb-inputText" style="margin-right:5px;" value="" size="6" maxlength="10" name="password_policy_compare_count" type="Text">
                                    		<xsl:if test="$config/password_policy/compare_count != ''">
												<xsl:attribute name="value">
													<xsl:value-of select="$config/password_policy/compare_count"/>
												</xsl:attribute>
											</xsl:if>
                                    </input>
                                    <xsl:value-of select="$lab_times"></xsl:value-of>
                                </td>
                            </tr>
                            <tr>
                                <td class="wzb-ui-desc-text">
                                    <xsl:value-of select="$label_core_system_setting_156" disable-output-escaping="yes"></xsl:value-of>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </td>
             </tr>
			<tr>
                <td class="wzb-form-label" valign="top"><xsl:value-of select="$label_core_system_setting_203"/>：</td>
                <td class="wzb-form-control">
                    <table>
                        <tbody>
                            <tr>
                                <td>
                                    <input class="wzb-inputText" style="margin-right:5px;" value="" id="system_default_user_password"  name="system_default_user_password" type="password">
                                    		<xsl:if test="$config/default_user_password/user_password != ''">
												<xsl:attribute name="value">
													<xsl:value-of select="$config/default_user_password/user_password"/>
												</xsl:attribute>
											</xsl:if>
                                    </input>
                                    <input value="{$minLength}" id="minLength"  name="minLength" type="hidden"/>
                                    <input value="{$maxLength}" id="maxLength"  name="maxLength" type="hidden"/>
                                    
                                </td>
                            </tr>
                            <tr>
                                <td class="wzb-ui-desc-text">
                                    <xsl:value-of select="$label_core_system_setting_204" disable-output-escaping="yes"></xsl:value-of>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </td>
             </tr>
			</table>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="id">
					<xsl:text>submit-btn</xsl:text>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_save" />
				<xsl:with-param name="wb_gen_btn_href">
					<xsl:text>javascript:systemSetting.exec(document.frmXml)</xsl:text>
				</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
</xsl:stylesheet>
