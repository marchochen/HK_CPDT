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
	<xsl:variable name="lab_openoffice_host_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_openoffice_host_desc')" />
	<xsl:variable name="lab_openoffice_port_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_openoffice_port_desc')" />
	<xsl:variable name="lab_mail_server" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_mail_server')" />
	<!-- =============================================================== -->
	<xsl:variable name="config" select="/system_setting/config"></xsl:variable>
	<xsl:variable name="lab_sys_wechat" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_sys_wechat')" />
	<xsl:variable name="lab_wechat_token_url" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_wechat_token_url')" />
	<xsl:variable name="lab_wechat_menu" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_wechat_menu')" />
	<xsl:variable name="lab_update_wechat_menu" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_update_wechat_menu')" />
	<xsl:variable name="lab_wechat_token_url_tip" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_wechat_token_url_tip')" />
	<xsl:variable name="label_core_system_setting_145" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_145')"/>
	<xsl:variable name="label_core_system_setting_149" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_149')"/>
	<!-- =============================================================== -->
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
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}jquery.js" />
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">
					<xsl:text>wb_ui</xsl:text>
				</xsl:with-param>
			</xsl:call-template>
			<script language="Javascript"><![CDATA[
					threshold = new wbThreshold();
					systemSetting = new wbSystemSetting();
			]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="">
			<form name="frmXml">
				<input type="hidden" name="module" />
				<input type="hidden" name="cmd" />
				<input type="hidden" name="url_success" />
				<input type="hidden" name="url_failure" />
				<input type="hidden" name="type" value='wechat' />

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
				<xsl:value-of select="$lab_sys_wechat"/>
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
			<xsl:with-param name="tab_3" select="$lab_sys_wechat" />
			<xsl:with-param name="tab_3_href">
				<xsl:text>javascript:systemSetting.prep('wechat')</xsl:text>
			</xsl:with-param>
			<xsl:with-param name="current_tab" select="$lab_sys_wechat" />
		</xsl:call-template>
		<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
			
			
			<!-- ================================= 微信 ================================= -->
			
			
			<tr>
				<td width="20%" align="right" nowrap="nowrap" valign="bottom" class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_wechat_domain" />
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td width="80%" align="left" valign="bottom" class="wzb-form-control">
					<input class="wzb-inputText" id="wechat_domain" type="text" maxlength="255" size="60" name="wechat_domain">
						<xsl:if test="$config/wechat/wechat_domain != ''">
							<xsl:attribute name="value">
								<xsl:value-of select="$config/wechat/wechat_domain" />
							</xsl:attribute>
						</xsl:if>
					</input>
					<span class="wzb-ui-desc-text">
						（<xsl:value-of select="$lab_wechat_domain_desc" />）
					</span>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right" nowrap="nowrap" valign="bottom" class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_wechat_server_id" />
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td width="80%" align="left" valign="bottom" class="wzb-form-control">
					<input readonly="readonly" class="wzb-inputText" id="wechat_server_id" type="text" maxlength="255" size="60" name="wechat_server_id">
						<xsl:choose>
							<xsl:when test="$config/wechat/wechat_server_id != ''">
								<xsl:attribute name="value">
									<xsl:value-of select="$config/wechat/wechat_server_id" />
								</xsl:attribute>
							</xsl:when>
							<xsl:when test="$config/wechat/wechat_server_id = ''">
								<xsl:attribute name="value">
									<xsl:text>offline</xsl:text>
								</xsl:attribute>
							</xsl:when>
						</xsl:choose>
					</input>
					<span class="wzb-ui-desc-text">
						（<xsl:value-of select="$lab_wechat_server_id_desc" />）
					</span>
				</td>
			</tr>
			
			<tr>
				<td width="20%" align="right" nowrap="nowrap" valign="bottom" class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_wechat_port" />
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td width="80%" align="left" valign="bottom" class="wzb-form-control">
					<input readonly="readonly" class="wzb-inputText" id="wechat_port" type="text" maxlength="255" size="60" name="wechat_port">
						
						<xsl:choose>
							<xsl:when test="$config/wechat/wechat_port = 0">
								<xsl:attribute name="value">
									<xsl:text>80</xsl:text>
								</xsl:attribute>
							</xsl:when>
							<xsl:when test="$config/wechat/wechat_port != ''">
								<xsl:attribute name="value">
									<xsl:value-of select="$config/wechat/wechat_port" />
								</xsl:attribute>
							</xsl:when>
						</xsl:choose>
						
					</input>
					<span class="wzb-ui-desc-text">
						（<xsl:value-of select="$lab_wechat_port_desc" />）
					</span>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right" nowrap="nowrap" valign="bottom" class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_wechat_max_message" />
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td width="80%" align="left" valign="bottom" class="wzb-form-control">
					<input class="wzb-inputText" id="wechat_max_message" type="text" maxlength="9" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" size="60" name="wechat_max_message">
						<xsl:choose>
							<xsl:when test="$config/wechat/wechat_max_message = 0">
								<xsl:attribute name="value">
									<xsl:text>3</xsl:text>
								</xsl:attribute>
							</xsl:when>
							<xsl:when test="$config/wechat/wechat_max_message != ''">
								<xsl:attribute name="value">
									<xsl:value-of select="$config/wechat/wechat_max_message" />
								</xsl:attribute>
							</xsl:when>
						</xsl:choose>
					</input>
					<span class="wzb-ui-desc-text">
						（<xsl:value-of select="$lab_wechat_max_message_desc" />）
					</span>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right" nowrap="nowrap" valign="bottom" class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_wechat_trial_account_enabled" />
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td width="80%" align="left" valign="bottom" class="wzb-form-control">
					<span>
						<input id="wechat_trial_account_enabled_val_0" type="radio" name="wechat_trial_account_enabled" value="true">
							<xsl:if test="$config/wechat/wechat_trial_account_enabled = 'true'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<label for="wechat_trial_account_enabled_val_0">
							<xsl:value-of select="$lab_sys_enabled" />
						</label>
					</span>
					<span class="margin-left10">
						<input id="wechat_trial_account_enabled_val_1" type="radio" name="wechat_trial_account_enabled" value="false">
							<xsl:choose>
								<xsl:when test="not($config/wechat/wechat_trial_account_enabled)">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:when>
								<xsl:when test="$config/wechat/wechat_trial_account_enabled = ''">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:when>
								<xsl:when test="$config/wechat/wechat_trial_account_enabled = 'false'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:when>
							</xsl:choose>
						</input>
						<label for="wechat_trial_account_enabled_val_1">
							<xsl:value-of select="$lab_sys_disabled" />
						</label>
					</span>
				</td>
			</tr>
		</table>
		
		<table id="wechatMenu" cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
			<tr>
				<td width="20%" align="right" nowrap="nowrap" valign="bottom" class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_wechat_token_url" />
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td width="80%" align="left" valign="bottom" class="wzb-form-control">
					<input class="wzb-inputText" id="wechat_token_url" type="text" maxlength="255" size="110" name="wechat_token_url">
						<xsl:if test="$config/wechat/wechat_token_url != ''">
							<xsl:attribute name="value">
								<xsl:value-of select="$config/wechat/wechat_token_url" />
							</xsl:attribute>
						</xsl:if>
					</input>
				</td>
			</tr>
			
			<tr>
			
				<td align="right" class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_wechat_token_url_tip" />
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td class="wzb-form-control">
					1.进入微信公众平台后台设置页面 【开发者中心】<br /><br />
					2.拷贝页面的AppId(应用ID) 和AppSecret(应用密钥)的值<br /><br />
					3.分别替换掉下面链接中的AppId和AppSecret,替换参数值后的URL即为获取微信token的url<br /><br />
					<![CDATA[
						https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=]]><font color='red'>AppId</font><![CDATA[&secret=]]><font color='red'>AppSecret</font>						
				</td>
			</tr>
			
			<tr>
				<td width="20%" align="right" nowrap="nowrap" valign="top" class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_wechat_menu" />
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td width="80%" align="left" valign="bottom" class="wzb-form-control">
					<textarea rows="10" cols="60" class="wzb-inputTextArea" id="wechat_menu" name="wechat_menu">
						<xsl:if test="$config/wechat/wechat_menu != ''">
							<xsl:value-of select="$config/wechat/wechat_menu" />
						</xsl:if>
					</textarea>
					
					<span style="vertical-align:80px; margin-left:12px;" id="updateBtnParent">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="id">
							<xsl:text>update-btn</xsl:text>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_update_wechat_menu" />
						<xsl:with-param name="wb_gen_btn_href">
							<xsl:text>javascript:systemSetting.updateWechatMenu(document.frmXml)</xsl:text>
						</xsl:with-param>
					</xsl:call-template>
					
					<script language="Javascript"><![CDATA[
						var textarea= document.getElementById("wechat_menu"); 
						textarea.style.height=textarea.scrollHeight;
						document.getElementById("updateBtnParent").style.verticalAlign = document.getElementById("wechat_menu").scrollHeight/2
					]]></script>
					
					</span>
				</td>
			</tr>
			
		</table>
		
		<div class="wzb-bar" style="padding:0">
			<table width="{$wb_gen_table_width}" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td colspan="2">
						<img src="{$wb_img_path}tp.gif" width="1" height="4" border="0" />
					</td>
				</tr>
				<tr>
					<td align="center">
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="id">
								<xsl:text>submit-btn</xsl:text>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_save" />
							<xsl:with-param name="wb_gen_btn_href">
								<xsl:text>javascript:systemSetting.exec(document.frmXml)</xsl:text>
							</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</table>
		</div>
		<xsl:call-template name="wb_ui_footer" />
	</xsl:template>
</xsl:stylesheet>
