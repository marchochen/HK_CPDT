<?xml version="1.0" encoding="UTF-8"?>
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
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<!-- others -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/sys_tab_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="cur_act_user_cnt" select="count(/currentusers/cur_act_user_list/active_user)"/>
	<!-- paginatoin variables -->
	<xsl:variable name="page_size" select="/currentusers/pagination/@page_size"/>
	<xsl:variable name="cur_page" select="/currentusers/pagination/@cur_page"/>
	<xsl:variable name="total" select="/currentusers/pagination/@total_rec"/>
	<xsl:variable name="timestamp" select="/currentusers/pagination/@timestamp"/>
	<!-- sorting variable -->
	<xsl:variable name="cur_sort_col" select="/currentusers/pagination/@sort_col"/>
	<xsl:variable name="cur_sort_order" select="/currentusers/pagination/@sort_order"/>
	<xsl:variable name="sort_order_by">
		<xsl:choose>
			<xsl:when test="$cur_sort_order = 'ASC' or $cur_sort_order ='asc'">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
		<xsl:variable name="lab_sys_setting" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_sys_setting')" />
		<xsl:variable name="lab_sys_wechat" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_sys_wechat')" />
		<xsl:variable name="label_core_system_setting_145" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_145')"/>
		<xsl:variable name="label_core_system_setting_149" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_149')"/>
		<xsl:variable name="label_core_system_setting_163" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_163')"/>
	
		<xsl:variable name="label_core_system_setting_174" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_174')"/>
		<xsl:variable name="label_core_system_setting_175" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_175')"/>
		<xsl:variable name="label_core_system_setting_176" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_176')"/>
		<xsl:variable name="label_core_system_setting_221" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_221')"/>
	
	<!-- ================================================================ -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="currentusers"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="currentusers">
		<xsl:apply-templates select="cur_act_user_list"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="cur_act_user_list">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_threshold.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_system_setting.js" />
			
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			    usr = new wbUserGroup;
				threshold = new wbThreshold();
				systemSetting = new wbSystemSetting();
			]]></SCRIPT>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml" method="" action="">
				<xsl:call-template name="wb_init_lab"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_threshold_set">設置</xsl:with-param>
			<xsl:with-param name="lab_active_user">當前線上用戶</xsl:with-param>
			<xsl:with-param name="lab_log">系統日誌</xsl:with-param>
			<xsl:with-param name="lab_login_time">登錄時間</xsl:with-param>
			<xsl:with-param name="lab_no_item">沒有登錄用戶。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_threshold_set">设置</xsl:with-param>
			<xsl:with-param name="lab_active_user">当前在线用户</xsl:with-param>
			<xsl:with-param name="lab_log">系统日志</xsl:with-param>
			<xsl:with-param name="lab_login_time">登录时间</xsl:with-param>
			<xsl:with-param name="lab_no_item">没有登录用户。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_threshold_set">Configuration</xsl:with-param>
			<xsl:with-param name="lab_active_user">Active user</xsl:with-param>
			<xsl:with-param name="lab_log">Logs</xsl:with-param>
			<xsl:with-param name="lab_login_time">Login time</xsl:with-param>
			<xsl:with-param name="lab_no_item">No active user.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_threshold_set"/>
		<xsl:param name="lab_active_user"/>
		<xsl:param name="lab_log"/>
		<xsl:param name="lab_login_time"/>
		<xsl:param name="lab_no_item"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_SYS_SETTING_LOG</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_SYS_SETTING_LOG</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_active_user"/>
			</xsl:with-param>
		</xsl:call-template>

		<xsl:call-template name="sys_gen_tab">
			<xsl:with-param name="tab_1" select="$lab_log"/>
			<xsl:with-param name="tab_1_href">javascript:threshold.get_thd_syn_log_prep()</xsl:with-param>
			<xsl:with-param name="tab_2" select="$lab_active_user"/>
			<xsl:with-param name="tab_2_href">javascript:threshold.get_cur_act_user()</xsl:with-param>
			<xsl:with-param name="current_tab" select="$lab_active_user"/>
		</xsl:call-template>

		<xsl:choose>
			<xsl:when test="$cur_act_user_cnt &gt;= 1">
				
				<div>
					<span class="wzb-ui-desc-text"><xsl:value-of select="$label_core_system_setting_221"></xsl:value-of></span>
				</div>
				
				<table class="table wzb-form-table">
					<tr class="wzb-form-table-head">
						<!-- login user id -->
						<td width="20%" style="BORDER-TOP:0px">
							<xsl:variable name="_order">
								<xsl:choose>
									<xsl:when test="$cur_sort_col = 'usr_ste_usr_id'">
										<xsl:value-of select="$sort_order_by"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$cur_sort_order"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'usr_ste_usr_id'">
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','usr_ste_usr_id','sortOrder','{$_order}','cur_page','1')" class="TitleText">
										<xsl:value-of select="$lab_usr_id"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
											<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','usr_ste_usr_id','sortOrder','asc','cur_page','1')" class="TitleText">
										<xsl:value-of select="$lab_usr_id"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<!-- user name -->
						<td width="20%" style="BORDER-TOP:0px">
							<xsl:variable name="_order">
								<xsl:choose>
									<xsl:when test="$cur_sort_col = 'usr_display_bil'">
										<xsl:value-of select="$sort_order_by"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$cur_sort_order"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'usr_display_bil'">
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','usr_display_bil','sortOrder','{$_order}','cur_page','1')" class="TitleText">
										<xsl:value-of select="$lab_dis_name"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
											<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','usr_display_bil','sortOrder','asc','cur_page','1')" class="TitleText">
										<xsl:value-of select="$lab_dis_name"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<!-- group fullpath -->
						<td width="20%" style="BORDER-TOP:0px">
							<xsl:value-of select="$lab_group"/>
						</td>
						<!-- login entrance -->
						<td width="20%" style="BORDER-TOP:0px">
							<xsl:variable name="_order">
								<xsl:choose>
									<xsl:when test="$cur_sort_col = 'cau_login_type'">
										<xsl:value-of select="$sort_order_by"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$cur_sort_order"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'cau_login_type'">
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','cau_login_type','sortOrder','{$_order}','cur_page','1')" class="TitleText">
										<xsl:value-of select="$label_core_system_setting_163"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
											<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','cau_login_type','sortOrder','desc','cur_page','1')" class="TitleText">
										<xsl:value-of select="$label_core_system_setting_163"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<!-- login time -->
						<td width="20%" align="center" style="BORDER-TOP:0px">
							<xsl:variable name="_order">
								<xsl:choose>
									<xsl:when test="$cur_sort_col = 'cau_login_date'">
										<xsl:value-of select="$sort_order_by"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$cur_sort_order"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'cau_login_date'">
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','cau_login_date','sortOrder','{$_order}','cur_page','1')" class="TitleText">
										<xsl:value-of select="$lab_login_time"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
											<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','cau_login_date','sortOrder','desc','cur_page','1')" class="TitleText">
										<xsl:value-of select="$lab_login_time"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
					<xsl:apply-templates select="active_user"/>
				</table>
				<!-- Pagination -->
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="timestamp" select="$timestamp"/>
					<xsl:with-param name="total" select="$total"/>
					<xsl:with-param name="width"><xsl:value-of select="$wb_gen_table_width"/></xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_item"/>
					<xsl:with-param name="top_line">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="active_user">
		<tr>
			<!-- user id -->
			<td>
				<a href="javascript:usr.user.manage_usr('{@usr_ent_id}','','','')" class="Text">
					<xsl:value-of select="@usr_ste_usr_id"/>
				</a>
			</td>
			<!-- user name -->
			<td>
				<xsl:value-of select="@usr_display_bil"/>
			</td>
			<!-- group fullpath -->
			<td>
				<xsl:value-of select="@group"/>
			</td>
			<!-- login type -->
			<td>
				<xsl:choose>
					<xsl:when test="@login_type = 'mobile'">
						<xsl:value-of select="$label_core_system_setting_175"/>
					</xsl:when>
					<xsl:when test="@login_type = 'weixin'">
						<xsl:value-of select="$label_core_system_setting_176"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$label_core_system_setting_174"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<!-- login time -->
			<td align="center">
				<xsl:call-template name="display_time">
					<xsl:with-param name="my_timestamp"><xsl:value-of select="@login_time"/></xsl:with-param>
					<xsl:with-param name="dis_time">T</xsl:with-param>
				</xsl:call-template>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>