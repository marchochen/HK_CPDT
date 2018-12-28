<?xml version="1.0" encoding="UTF-8" ?>
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
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	
	<xsl:variable name="meta_cur_usr" select="/user_manager/meta/cur_usr/@ent_id"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="user_manager"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template match="user_manager">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js"/>		
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>		
			<script language="JavaScript" src="/static/js/cwn_utils.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			usr = new wbUserGroup;				
			var active_tab = getUrlParam('active_tab');		
		]]></SCRIPT>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
		</head>
		<BODY leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="wb_utils_gen_form_focus(document.frmXml);">
			<xsl:call-template name="wb_init_lab"/>
		</BODY>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_edit_grp">修改<xsl:value-of select="$lab_group"/>資訊</xsl:with-param>
			<xsl:with-param name="lab_code"><xsl:value-of select="$lab_group"/>編號</xsl:with-param>
			<xsl:with-param name="lab_usr_manager">用戶管理</xsl:with-param>
			<xsl:with-param name="lab_all_users">用戶資訊</xsl:with-param>
			<xsl:with-param name="lab_inst">標題</xsl:with-param>
			<xsl:with-param name="lab_delete_immune">防止因整合系統而被刪除</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_desc">簡介</xsl:with-param>
			<xsl:with-param name="lab_code_desc">(不可超過80個位數及不可有逗號)</xsl:with-param>
			<xsl:with-param name="lab_name_desc">(不可超過80個字符及不可有斜線、雙引號)</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_edit_grp">修改<xsl:value-of select="$lab_group"/>资料</xsl:with-param>
			<xsl:with-param name="lab_all_users">用户信息</xsl:with-param>
			<xsl:with-param name="lab_code"><xsl:value-of select="$lab_group"/>编号</xsl:with-param>
			<xsl:with-param name="lab_usr_manager">用户管理</xsl:with-param>
			<xsl:with-param name="lab_inst">标题</xsl:with-param>
			<xsl:with-param name="lab_delete_immune">记录不被同步删除</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_desc">简介</xsl:with-param>
			<xsl:with-param name="lab_code_desc">(长度不能超过80字符，且不能包含逗号)</xsl:with-param>
			<xsl:with-param name="lab_name_desc">(长度不能超过80字符，且不能包含斜线、双引号)</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_edit_grp">Edit group</xsl:with-param>
			<xsl:with-param name="lab_all_users">User profile</xsl:with-param>
			<xsl:with-param name="lab_code"><xsl:value-of select="$lab_group"/> code</xsl:with-param>
			<xsl:with-param name="lab_usr_manager">User management</xsl:with-param>
			<xsl:with-param name="lab_inst">Title</xsl:with-param>
			<xsl:with-param name="lab_delete_immune"> deletion immuned</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_desc">Description</xsl:with-param>
			<xsl:with-param name="lab_code_desc">(Not more than 80 characters and must not contain comma)</xsl:with-param>
			<xsl:with-param name="lab_name_desc">(Not more than 80 characters and must not contain slash, back slash and double quote)</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_edit_grp"/>
		<xsl:param name="lab_code"/>
		<xsl:param name="lab_usr_manager"/>
		<xsl:param name="lab_inst"/>
		<xsl:param name="lab_delete_immune"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_code_desc"/>
		<xsl:param name="lab_name_desc"/>
		<xsl:param name="lab_all_users"/>
		<FORM name="frmXml" onSubmit="" method="post">
			<input type="hidden" name="ent_syn_ind" value=""/>
			<input type="hidden" name="cmd" value=""/>
			<input type="hidden" name="ent_id" value=""/>
			<input type="hidden" name="usg_timestamp" value=""/>
			<input type="hidden" name="url_success" value=""/>
			<input type="hidden" name="url_failure" value=""/>
			<input type="hidden" name="url_failure1" value=""/>
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_USR_INFO</xsl:with-param>
			</xsl:call-template>
			
<!-- 
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_all_users"/>
				</xsl:call-template>	
			
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_edit_grp"/>
				</xsl:call-template>	
 -->				
				<xsl:call-template name="wb_ui_nav_link">
						<xsl:with-param name="width">100%</xsl:with-param>
						<xsl:with-param name="text">
							
									
									<a class="NavLink" href="javascript:wb_utils_nav_go('FTN_AMD_USR_INFO',{$meta_cur_usr},'{$wb_lang}')">
										<xsl:value-of select="$lab_all_users"/>
									</a>
									<xsl:text>&#160;&gt;&#160;</xsl:text>
									<xsl:value-of select="$lab_edit_grp"/>
						</xsl:with-param>
					</xsl:call-template>
			<table>
				<tr>
					<td class="wzb-form-label" valign="top">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_code"/>：
					</td>
					<td class="wzb-form-control">
						<input style="width:300px" type="text" name="ent_ste_uid" size="27" maxlength="80" class="wzb-inputText" value="{group_member_list/uid}"/>
						<input type="hidden" name="lab_group" value="{$lab_group}"/><br/>
						<xsl:value-of select="$lab_code_desc"/>
					</td>
				</tr>		
				<tr>
					<td class="wzb-form-label" valign="top">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_inst"/>：
					</td>
					<td class="wzb-form-control">
						<input style="width:300px" type="text" class="wzb-inputText" name="usg_display_bil" size="27" maxlength="50" value="{group_member_list/desc}"/><br/>
						<xsl:value-of select="$lab_name_desc"/>
					</td>
				</tr>
		
				<tr>
					<td class="wzb-form-label" valign="top">
						<xsl:value-of select="$lab_desc"/>：
					</td>
					<td class="wzb-form-control">
						<textarea rows="6" wrap="VIRTUAL" class="wzb-inputTextArea" style="width:300px;" cols="30" name="usg_desc">
							<xsl:value-of select="group_member_list/long_desc"/>
						</textarea>
					</td>
				</tr>
				<!--
				<tr>
					<td width="20%"  align="right">
						<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					</td>
					<td width="80%" >
						<span class="wbFormRightText">
							<input type="checkbox" name="ent_syn_ind_chk">
								<xsl:if test="group_member_list/@syn_ind='false'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
							</input><xsl:value-of select="$lab_delete_immune"/>
						</span>
					</td>
				</tr>
			-->
			<tr>
				<td align="right">
					</td>
					<td align="left" class="wzb-ui-module-text">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
					</td>
			</tr>
			</table>
			<!-- for  department budget
			<table cellspacing="0" cellpadding="3" border="0" width="{$wb_gen_table_width}">
				<tr>
					<td width="20%"  align="right">
						<span class="TitleText" align="right"><xsl:value-of select="$lab_usg_budget"/>:</span>
					</td>
					<td width="80%" >
						<input style="width:300px" type="text" name="usg_budget" size="27" maxlength="60" value="{group_member_list/budget}"/>
					</td>
				</tr>
			</table>
			 -->
			<div class="wzb-bar">
				<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[var timestamp_tmp = ']]><xsl:value-of select="group_member_list/@timestamp"/><![CDATA[']]></SCRIPT>
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:usr.group.edit_exec(document.frmXml,'<xsl:value-of select="group_member_list/@id"/>','<xsl:value-of select="group_member_list/@grp_role"/>',timestamp_tmp,'<xsl:value-of select="$wb_lang"/>',active_tab)</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:usr.group.manage_grp(<xsl:value-of select="group_member_list/@id"/>,'','','','',active_tab)</xsl:with-param>
				</xsl:call-template>
			</div>
		</FORM>
	</xsl:template>
	<xsl:template match="cur_usr"/>
	<!-- =============================================================== -->
</xsl:stylesheet>
