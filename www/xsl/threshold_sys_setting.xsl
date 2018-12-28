<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="share/sys_tab_share.xsl"/>
	<!-- -->
	<xsl:output indent="yes"/>
	<!-- ================================================================ -->
	<xsl:template match="/">
		<xsl:apply-templates select="setting"/>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:variable name="lab_sys_setting" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_sys_setting')" />
	<xsl:variable name="lab_sys_wechat" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_sys_wechat')" />
	<xsl:variable name="label_core_system_setting_145" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_145')"/>
	<xsl:variable name="label_core_system_setting_146" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_146')"/>
	<xsl:variable name="label_core_system_setting_101" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_101')"/>
	<xsl:variable name="label_core_system_setting_102" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_102')"/>
	<xsl:variable name="label_core_system_setting_147" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_147')"/>
	<xsl:variable name="label_core_system_setting_148" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_148')"/>
	<xsl:variable name="label_core_system_setting_149" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_149')"/>
	<!-- ================================================================ -->
	<xsl:template match="setting">
		<html>
			<head>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
				<xsl:call-template name="wb_css">
					<xsl:with-param name="view">wb_ui</xsl:with-param>
				</xsl:call-template>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_threshold.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_system_setting.js" />
				<script language="Javascript"><![CDATA[
					threshold = new wbThreshold();
					systemSetting = new wbSystemSetting();
				]]></script>
			</head>
			<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0">
				<form name="frmXml">
					<xsl:call-template name="wb_init_lab"/>
					<input type="hidden" name="cmd"/>
					<input type="hidden" name="url_success" value=""/>
					<input type="hidden" name="url_failure" value=""/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_threshold_set">設置</xsl:with-param>
			<xsl:with-param name="lab_active_user">登錄用戶</xsl:with-param>
			<xsl:with-param name="lab_log">日誌</xsl:with-param>
			<xsl:with-param name="lab_action_log">執行日誌</xsl:with-param>
			<xsl:with-param name="lab_cur_active_user">當前在線人數</xsl:with-param>
			<xsl:with-param name="lab_cur_active_user_warn">當前在線人數已達到了用量警告值</xsl:with-param>
			<xsl:with-param name="lab_cur_active_user_block">當前在線人數已達到了最大用戶數</xsl:with-param>
			<xsl:with-param name="lab_btn_save">確定</xsl:with-param>
			<xsl:with-param name="lab_warn_thre_val">用量警告值</xsl:with-param>
			<xsl:with-param name="lab_warn_thre_val_detail">當用戶數達到此值後，提示用量警告</xsl:with-param>
			<xsl:with-param name="lab_block_thre_val">最大用戶數</xsl:with-param>
			<xsl:with-param name="lab_block_thre_val_detail">當用戶數達到此值後，不允許用戶登錄</xsl:with-param>
			<xsl:with-param name="lab_support_email">通知郵件接收者</xsl:with-param>
			<xsl:with-param name="lab_support_email_detail">當登錄用戶數達到用量警告值或者最大用戶數時發送通知郵件</xsl:with-param>
			<xsl:with-param name="lab_unassigned">未指定</xsl:with-param>
			<xsl:with-param name="lab_threshold_log">用量警告日誌</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_threshold_set">设置</xsl:with-param>
			<xsl:with-param name="lab_active_user">登录用户</xsl:with-param>
			<xsl:with-param name="lab_log">日志</xsl:with-param>
			<xsl:with-param name="lab_action_log">执行日志</xsl:with-param>
			<xsl:with-param name="lab_cur_active_user">当前在线人数</xsl:with-param>
			<xsl:with-param name="lab_cur_active_user_warn">当前在线人数已达到了用量警告值</xsl:with-param>
			<xsl:with-param name="lab_cur_active_user_block">当前在线人数已达到了最大用户数</xsl:with-param>
			<xsl:with-param name="lab_btn_save">确定</xsl:with-param>
			<xsl:with-param name="lab_warn_thre_val">用量警告值</xsl:with-param>
			<xsl:with-param name="lab_warn_thre_val_detail">当用户数达到此值后，提示用量警告</xsl:with-param>
			<xsl:with-param name="lab_block_thre_val">最大用户数</xsl:with-param>
			<xsl:with-param name="lab_block_thre_val_detail">当用户数达到此值后，不允许用户登录</xsl:with-param>
			<xsl:with-param name="lab_support_email">通知邮件接收者</xsl:with-param>
			<xsl:with-param name="lab_support_email_detail">当登录用户数达到用量警告值或者最大用户数时发送通知邮件</xsl:with-param>
			<xsl:with-param name="lab_unassigned">未指定</xsl:with-param>
			<xsl:with-param name="lab_threshold_log">用量警告日志</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_threshold_set">Configuration</xsl:with-param>
			<xsl:with-param name="lab_active_user">Active user</xsl:with-param>
			<xsl:with-param name="lab_log">Logs</xsl:with-param>
			<xsl:with-param name="lab_action_log">Action log</xsl:with-param>
			<xsl:with-param name="lab_cur_active_user">Online users</xsl:with-param>
			<xsl:with-param name="lab_cur_active_user_warn">Performance warning level has been reached</xsl:with-param>
			<xsl:with-param name="lab_cur_active_user_block">Maximum online user has been reached</xsl:with-param>
			<xsl:with-param name="lab_btn_save">OK</xsl:with-param>
			<xsl:with-param name="lab_warn_thre_val">Performance warning level</xsl:with-param>
			<xsl:with-param name="lab_warn_thre_val_detail">Issue performance warning when active user reaches this level</xsl:with-param>
			<xsl:with-param name="lab_block_thre_val">Maximum active user</xsl:with-param>
			<xsl:with-param name="lab_block_thre_val_detail">User cannot login when active user reaches this level</xsl:with-param>
			<xsl:with-param name="lab_support_email">E-mail notification recipient</xsl:with-param>
			<xsl:with-param name="lab_support_email_detail">Send notification when performance warning level or maximum active user has been reached</xsl:with-param>
			<xsl:with-param name="lab_unassigned">Unassigned</xsl:with-param>
			<xsl:with-param name="lab_threshold_log">Performance warning log</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="content">
		<xsl:param name="lab_threshold_set"/>
		<xsl:param name="lab_active_user"/>
		<xsl:param name="lab_log"/>
		<xsl:param name="lab_action_log"/>
		<xsl:param name="lab_cur_active_user"/>
		<xsl:param name="lab_cur_active_user_warn"/>
		<xsl:param name="lab_cur_active_user_block"/>
		<xsl:param name="lab_warn_thre_val"/>
		<xsl:param name="lab_warn_thre_val_detail"/>
		<xsl:param name="lab_block_thre_val"/>
		<xsl:param name="lab_block_thre_val_detail"/>
		<xsl:param name="lab_support_email"/>
		<xsl:param name="lab_support_email_detail"/>
		<xsl:param name="lab_unassigned"/>
		<xsl:param name="lab_threshold_log"/>
		<xsl:param name="lab_btn_save"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_SYS_SETTING_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_SYS_SETTING_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$label_core_system_setting_145"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="sys_gen_tab">
			<xsl:with-param name="tab_1" select="$label_core_system_setting_145"/>
			<xsl:with-param name="tab_1_href">javascript:wb_utils_nav_go('FTN_AMD_SYS_SETTING_MAIN')</xsl:with-param>
			<xsl:with-param name="tab_2" select="$label_core_system_setting_149" />
			<xsl:with-param name="tab_2_href">
				<xsl:text>javascript:systemSetting.prep('sys')</xsl:text>
			</xsl:with-param>
			
			<xsl:with-param name="current_tab" select="$label_core_system_setting_145"/>
		</xsl:call-template>
		<xsl:apply-templates select="/setting/sys_setting">
			<xsl:with-param name="lab_cur_active_user" select="$lab_cur_active_user"/>
			<xsl:with-param name="lab_cur_active_user_warn" select="$lab_cur_active_user_warn"/>
			<xsl:with-param name="lab_cur_active_user_block" select="$lab_cur_active_user_block"/>
			<xsl:with-param name="lab_warn_thre_val" select="$lab_warn_thre_val"/>
			<xsl:with-param name="lab_warn_thre_val_detail" select="$lab_warn_thre_val_detail"/>
			<xsl:with-param name="lab_block_thre_val" select="$lab_block_thre_val"/>
			<xsl:with-param name="lab_block_thre_val_detail" select="$lab_block_thre_val_detail"/>
			<xsl:with-param name="lab_support_email" select="$lab_support_email"/>
			<xsl:with-param name="lab_support_email_detail" select="$lab_support_email_detail"/>
			<xsl:with-param name="lab_unassigned" select="$lab_unassigned"/>
			<xsl:with-param name="lab_threshold_log" select="$lab_threshold_log"/>
		</xsl:apply-templates>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_btn_save"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:threshold.upd_sys_setting(document.frmXml)</xsl:with-param>
			</xsl:call-template>
		</div>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="sys_setting">
		<xsl:param name="lab_cur_active_user"/>
		<xsl:param name="lab_cur_active_user_warn"/>
		<xsl:param name="lab_cur_active_user_block"/>
		<xsl:param name="lab_warn_thre_val"/>
		<xsl:param name="lab_warn_thre_val_detail"/>
		<xsl:param name="lab_block_thre_val"/>
		<xsl:param name="lab_block_thre_val_detail"/>
		<xsl:param name="lab_support_email"/>
		<xsl:param name="lab_support_email_detail"/>
		<xsl:param name="lab_unassigned"/>
		<xsl:param name="lab_threshold_log"/>

		<xsl:variable name="block_value" select="sys_set[@type='THR_BLOCK']/@value"/>
		<xsl:variable name="warn_value" select="sys_set[@type='THR_WARN']/@value"/>
		<xsl:variable name="email_value" select="sys_set[@type='THR_SPT_EMAIL']/@value"/>
		<xsl:variable name="multiple_login_ind" select="sys_set[@type='MULTIPLE_LOGIN_IND']/@value"/>

		<table>
			<!-- blank row -->
			<tr>
				<td colspan="2">
				</td>
			</tr>
			<!-- current active users -->
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_cur_active_user"/><xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control" valign="bottom">
					<table>
                        <tbody>
                            <tr>
                                <td>
	                                <font>
										<xsl:attribute name="color">
											<xsl:choose>
												<xsl:when test="$block_value != '' and @current_active_user &gt;= $block_value">red</xsl:when>
												<xsl:when test="$warn_value != '' and @current_active_user &gt;= $warn_value">#FFCC00</xsl:when>
												<xsl:otherwise>green</xsl:otherwise>
											</xsl:choose>
										</xsl:attribute>
										<a href="javascript:threshold.get_cur_act_user()" name="thr_log" class="NavLink">
											<xsl:value-of select="@current_active_user"/>
										</a>
										<xsl:choose>
											<xsl:when test="$block_value != '' and @current_active_user &gt;= $block_value">
												(<xsl:value-of select="$lab_cur_active_user_block"/>)
											</xsl:when>
											<xsl:when test="$warn_value != '' and @current_active_user &gt;= $warn_value">
												(<xsl:value-of select="$lab_cur_active_user_warn"/>)
											</xsl:when>
										</xsl:choose>
									</font>
                                </td>
                            </tr>
                        </tbody>
                    </table>
				</td>
			</tr>
			
			
			<!-- allow multiple login of an account, [ true | false ] -->
			<tr>
                <td class="wzb-form-label" valign="top"><xsl:value-of select="$label_core_system_setting_146"/>：</td>
                <td class="wzb-form-control">
                    <table>
                        <tbody>
                            <tr>
                                <td>
                                    <span class="wbFormRightText">
                                        <label>
                                        	<input id="multiple_login_ind_1" type="radio" name="multiple_login_ind" value="1">
												<xsl:if test="$multiple_login_ind = 1"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
											</input>
                                        	<xsl:value-of select="$label_core_system_setting_101"/>
                                        </label>
                                        <label>
                                        	<input id="multiple_login_ind_0" type="radio" name="multiple_login_ind" value="0">
												<xsl:if test="$multiple_login_ind = 0"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
											</input>
                                        	<xsl:value-of select="$label_core_system_setting_102"/>
                                        </label>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="wzb-ui-desc-text"><xsl:value-of select="$label_core_system_setting_148"/></td>
                            </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
			
			
			<!-- warning threshold value -->
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_warn_thre_val"/><xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<input id="rdo_thr_warn_val_0" type="radio" name="warn_value" value="0">
						<xsl:if test="$warn_value = ''"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
					</input>
					<label for="rdo_thr_warn_val_0">
						<xsl:value-of select="$lab_unassigned"/>
					</label>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
				</td>
				<td class="wzb-form-control">
					<input id="rdo_thr_warn_val_1" type="radio" name="warn_value" value="1">
						<xsl:if test="$warn_value != ''"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
					</input>
					<input type="Text" name="thr_warn_value" maxlength="10" size="6" value="{$warn_value}" onfocus="this.form.warn_value[1].checked=true" class="wzb-inputText"/>
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td class="wzb-form-control">
					(<xsl:value-of select="$lab_warn_thre_val_detail"/>)
				</td>
			</tr>
			<input type="hidden" name="lab_threshold_warn" value="{$lab_warn_thre_val}"/>
			<input type="hidden" name="threshold_warn"/>
			<tr>
				<td colspan="2">
				</td>
			</tr>
			<!-- blocking threshold value -->
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_block_thre_val"/><xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<input id="rdo_thr_blk_val_0" type="radio" name="block_value" value="0">
						<xsl:if test="$block_value = ''"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
					</input>
					<label for="rdo_thr_blk_val_0">
						<xsl:value-of select="$lab_unassigned"/>
					</label>
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td class="wzb-form-control">
					<input id="rdo_thr_blk_val_1" type="radio" name="block_value" value="1">
						<xsl:if test="$block_value != ''"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
					</input>
					<input type="Text" name="thr_block_value" maxlength="10" size="6" value="{$block_value}" onfocus="this.form.block_value[1].checked=true" class="wzb-inputText"/>
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td class="wzb-form-control">
					(<xsl:value-of select="$lab_block_thre_val_detail"/>)
				</td>
			</tr>
			<input type="hidden" name="lab_threshold_block" value="{$lab_block_thre_val}"/>
			<input type="hidden" name="threshold_block"/>
			<tr>
				<td colspan="2">
				</td>
			</tr>
			<!-- support email -->
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_support_email"/><xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<input id="rdo_sup_email_0" type="radio" name="sup_email" value="0">
						<xsl:if test="$email_value = ''"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
					</input>
					<label for="rdo_sup_email_0">
						<xsl:value-of select="$lab_unassigned"/>
					</label>
				</td>
			</tr>
			<tr>
				<td >
				</td>
				<td class="wzb-form-control">
					<input id="rdo_sup_email_1" type="radio" name="sup_email" value="1">
						<xsl:if test="$email_value != ''"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
					</input>
					<input type="Text" name="sup_email_value" maxlength="120" size="24" value="{$email_value}" onfocus="this.form.sup_email[1].checked=true" class="wzb-inputText"/>
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td class="wzb-form-control">
					(<xsl:value-of select="$lab_support_email_detail"/>)
				</td>
			</tr>
			<input type="hidden" name="lab_support_email" value="{$lab_support_email}"/>
			<input type="hidden" name="support_email"/>
			<tr>
				<td colspan="2">
				</td>
			</tr>
			
			<!-- threshold log 
			<tr>
				<td class="wzb-form-label">
					<a href="javascript:threshold.get_thd_syn_log_prep()" name="thr_log" class="NavLink">
						<xsl:value-of select="$lab_threshold_log"/>
					</a>
				</td>
				<td>
				</td>
			</tr>
			-->
			<!-- blank row -->
			<tr>
				<td colspan="2">
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
