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
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>		
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="share/bp_usr_del_confirm_share.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
<xsl:variable name="spawn_threshold">
	<xsl:value-of select="Upload/upload_user/spawn_threshold"/>
</xsl:variable>
  
<xsl:variable name="spawn_thread">
<xsl:choose>
	<xsl:when test="number(Upload/upload_user/user_count/text())>$spawn_threshold">true</xsl:when>
	<xsl:otherwise>false</xsl:otherwise>
</xsl:choose>
</xsl:variable>

<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_usr_manager"/>
		<xsl:param name="lab_usr_reg_approval"/>
		<xsl:param name="lab_usr_prof_maintain"/>
		<xsl:param name="lab_usr_bat_upload"/>
		<xsl:param name="lab_upload_usr_info"/>
		<xsl:param name="lab_preview"/>
		<xsl:param name="lab_upload_usr"/>
		<xsl:param name="lab_upload_failed"/>
		<xsl:param name="lab_upload_failed_missing_field"/>
		<xsl:param name="lab_upload_failed_duplicated_id_lines"/>
		<xsl:param name="lab_upload_failed_invalid_field"/>
		<xsl:param name="lab_upload_failed_others_failed_lines"/>
		<xsl:param name="lab_upload_failed_invalid_groupcode"/>
		<xsl:param name="lab_upload_failed_empty_password"/>
		<xsl:param name="lab_upload_failed_invalid_supervise_groupcode"/>
		<xsl:param name="lab_upload_failed_invalid_direct_supervise_id"/>
		<xsl:param name="lab_upload_failed_invalid_role_line"/>
		<xsl:param name="lab_upload_failed_invalid_org_grp_name_lines"/>
		<xsl:param name="lab_upload_failed_invalid_group_supervisor_group"/>
		<xsl:param name="lab_upload_failed_no_group_specified"/>
		<xsl:param name="lab_upload_failed_invalid_group_supervisor_group_lines"/>
		<xsl:param name="lab_upload_failed_held_by_pending_appn"/>
		<xsl:param name="lab_upload_failed_held_by_pending_approval_appn"/>
		<xsl:param name="lab_no_upload_usr"/>
		<xsl:param name="lab_first_name"/>
		<xsl:param name="lab_last_name"/>
		<xsl:param name="lab_form"/>
		<xsl:param name="lab_class"/>
		<xsl:param name="lab_class_num"/>
		<xsl:param name="lab_email"/>
		<xsl:param name="lab_student_id"/>
		<xsl:param name="lab_club_1"/>
		<xsl:param name="lab_club_2"/>
		<xsl:param name="lab_club_3"/>
		<xsl:param name="lab_club_4"/>
		<xsl:param name="lab_club_5"/>
		<xsl:param name="lab_total_upload"/>
		<xsl:param name="lab_upload_success"/>
		<xsl:param name="lab_upload_failure"/>
		<xsl:param name="lab_line"/>
		<xsl:param name="lab_usr_display_bil"/>
		<xsl:param name="lab_password"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_confirm"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:param name="lab_g_form_btn_show_all"/>
		<xsl:param name="lab_g_form_btn_confirm_imm"/>
		<xsl:param name="lab_usr_profiles"/>
		<xsl:param name="lab_errors_are_found"/>
		<xsl:param name="lab_unknown_col"/>
		<xsl:param name="lab_detected_from_file"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_error_title"/>
		<xsl:param name="lab_error_msg"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_desc2_1"/>
		<xsl:param name="lab_desc2_2"/>
		<xsl:param name="lab_male"/>
		<xsl:param name="lab_female"/>
		<xsl:param name="lab_group_code_ref"/>
		<xsl:param name="lab_upload_failed_invalid_gradecode_cnt"/>
		<xsl:param name="lab_number_of_users"/>
		<xsl:param name="lab_showing_usr"/>
		<xsl:param name="lab_page_of_usr"/>
		<xsl:param name="lab_page_piece_usr"/>
		<xsl:param name="lab_get_source"/>
		<xsl:param name="lab_get_count"/>
		<xsl:param name="lab_get_pass"/>
		<xsl:param name="lab_get_unpass"/>
		<xsl:param name="lab_get_check_msg"/>
		<xsl:param name="lab_have_no_msg"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_USR_INFO</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_USR_INFO</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text"><xsl:choose><xsl:when test="$upload_failure_cnt = 0"><xsl:value-of select="$lab_title"/></xsl:when><xsl:otherwise><xsl:value-of select="$lab_error_title"/></xsl:otherwise></xsl:choose></xsl:with-param>
		</xsl:call-template>

							
						<xsl:call-template name="wb_ui_line"/>
						<table border="0" cellspacing="0" cellpadding="3" width="{$wb_gen_table_width}">
						<tr>
									<td width="4">
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
									<td></td>
									<td height="10" rowspan="3" align="right">
										<xsl:call-template name="wb_gen_form_button">
											<xsl:with-param name="wb_gen_btn_name" select="$lab_get_source"/>
											<xsl:with-param name="wb_gen_btn_href">javascript:this.disabled='true';Batch.User.Import.get_source('<xsl:value-of select="sourceFile"/>');</xsl:with-param>
										</xsl:call-template>
									</td>
									
							</tr>
							<tr>
									<td width="4">
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
									<td height="10">
										<xsl:value-of select="$lab_get_count"/>[<xsl:value-of select="record_count"/>]
									</td>
									<td></td>
									
							</tr>
							<tr>
									<td width="4">
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
									<td height="10">
			
										<xsl:value-of select="$lab_get_pass"/>[<xsl:value-of select="pass_count"/>]
						
									</td>
									<td></td>
									
							</tr>
							<tr>
									<td width="4">
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
									<td height="10">
										<xsl:value-of select="$lab_get_unpass"/>[<xsl:value-of select="unpass_count"/>]
									</td>
									<xsl:variable name="usr_pwd_need_change_ind" select="usr_pwd_need_change_ind"/>
									<td><input name="usr_pwd_need_change_ind" type="hidden" value="{$usr_pwd_need_change_ind}" /></td>
									<xsl:variable name="identical_usr_no_import" select="identical_usr_no_import"/>
									<td><input name="identical_usr_no_import" type="hidden" value="{$identical_usr_no_import}" /></td>
									<xsl:variable name="oldusr_pwd_need_update_ind" select="oldusr_pwd_need_update_ind"/>
									<td><input name="oldusr_pwd_need_update_ind" type="hidden" value="{$oldusr_pwd_need_update_ind}" /></td>
							</tr>
						</table>
							<xsl:if test="errMessage">
								<table border="0" cellspacing="0" cellpadding="3" width="{$wb_gen_table_width}">
									<xsl:for-each select="errMessage">
										<tr>
											<td width="4">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
											<td>
												<xsl:value-of select="err"></xsl:value-of>
											</td>
										</tr>
									</xsl:for-each>
								</table>
							</xsl:if>
						<xsl:call-template name="wb_ui_line"/>

		<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="3">
			<tr>
				<td align="center">
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_back"/></xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:Batch.User.Import.del()</xsl:with-param>
							</xsl:call-template>
	
							<xsl:choose>
								<xsl:when test="unpass_count = 0 and record_count>0">
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_confirm_imm"/>
									<xsl:with-param name="wb_gen_btn_href">
				
											javascript:this.disabled='true';Batch.User.Import.del_confirm(document.frmXml,'<xsl:value-of select="$wb_lang"/>')
							
				
									</xsl:with-param>
								</xsl:call-template>
								</xsl:when>
								
							</xsl:choose>
								<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
</xsl:stylesheet>
