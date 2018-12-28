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
	<xsl:import href="share/bp_usr_import_confirm_share.xsl"/>
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
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_confirm"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:param name="lab_usr_profiles"/>
		<xsl:param name="lab_errors_are_found"/>
		<xsl:param name="lab_unknown_col"/>
		<xsl:param name="lab_detected_from_file"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_error_title"/>
		<xsl:param name="lab_error_msg"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_male"/>
		<xsl:param name="lab_female"/>
		<xsl:param name="lab_group_code_ref"/>
		<xsl:param name="lab_upload_failed_invalid_gradecode_cnt"/>
		<xsl:param name="lab_number_of_users"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:choose>
			<xsl:when test="$upload_failure_cnt != 0">
				<xsl:call-template name="wb_ui_line"/>
				<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="3" class="Bg">
					<tr>
						<td width="10">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td>
							<xsl:if test="$upload_failure_cnt != '' and $upload_failure_cnt != '0'">
								<table cellpadding="3" cellspacing="0" border="0" width="100%">
									<tr>
										<td>
											<span class="Text">
												<xsl:value-of select="$upload_failure_cnt"/>
												<xsl:text>&#160;</xsl:text>
												<xsl:value-of select="$lab_errors_are_found"/>
											</span>
										</td>
									</tr>
								</table>
							</xsl:if>
							<table cellpadding="3" cellspacing="0" border="0" width="100%">
								<xsl:if test="$missing_field_failure_cnt &gt;=1">
									<tr>
										<td width="10">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
										<td>
											<span class="Text">
												<xsl:value-of select="$lab_upload_failed_missing_field"/>
												<xsl:text>：&#160;</xsl:text>
												<xsl:for-each select="missing_field_lines/line">
													<xsl:value-of select="$lab_line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@num"/><xsl:text>(</xsl:text><xsl:value-of select="@field"/><xsl:text>)</xsl:text>
													<xsl:choose>
														<xsl:when test="position() = last()"/>
														<xsl:otherwise>
															<xsl:text>,&#160;</xsl:text>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
											</span>
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="$dup_id_failure_cnt &gt;=1">
									<tr>
										<td width="10">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
										<td>
											<span class="Text">
												<xsl:value-of select="$lab_upload_failed_duplicated_id_lines"/>
												<xsl:text>：&#160;</xsl:text>
												<xsl:for-each select="duplicated_id_lines/line">
													<xsl:value-of select="$lab_line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@num"/>
													<xsl:choose>
														<xsl:when test="position() = last()"/>
														<xsl:otherwise>
															<xsl:text>,&#160;</xsl:text>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
											</span>
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="$invalid_field_cnt &gt;=1">
									<tr>
										<td width="10">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
										<td>
											<span class="Text">
												<xsl:value-of select="$lab_upload_failed_invalid_field"/>
												<xsl:text>：&#160;</xsl:text>
												<xsl:for-each select="invalid_field_lines/line">
													<xsl:value-of select="$lab_line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@num"/><xsl:text>(</xsl:text><xsl:value-of select="@field"/><xsl:text>)</xsl:text>
													<xsl:choose>
														<xsl:when test="position() = last()"/>
														<xsl:otherwise>
															<xsl:text>,&#160;</xsl:text>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
											</span>
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="$other_failure_cnt &gt;=1">
									<tr>
										<td width="10">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
										<td>
											<span class="Text">
												<xsl:value-of select="$lab_upload_failed_others_failed_lines"/>
												<xsl:text>：&#160;</xsl:text>
												<xsl:for-each select="others_failed_lines/line">
													<xsl:value-of select="$lab_line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@num"/>
													<xsl:choose>
														<xsl:when test="position() = last()"/>
														<xsl:otherwise>
															<xsl:text>,&#160;</xsl:text>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
											</span>
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="$invalid_groupcode_cnt &gt;= 1">
									<tr>
										<td width="10">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
										<td>
											<span class="Text">
												<xsl:value-of select="$lab_upload_failed_invalid_groupcode"/>
												<xsl:text>：&#160;</xsl:text>
												<xsl:for-each select="invalid_groupcode_lines/line">
													<xsl:value-of select="$lab_line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@num"/>
													<xsl:choose>
														<xsl:when test="position() = last()"/>
														<xsl:otherwise>
															<xsl:text>,&#160;</xsl:text>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
											</span>
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="$empty_password_cnt &gt;= 1">
									<tr>
										<td width="10">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
										<td>
											<span class="Text">
												<xsl:value-of select="$lab_upload_failed_empty_password"/>
												<xsl:text>：&#160;</xsl:text>
												<xsl:for-each select="empty_password_lines/line">
													<xsl:value-of select="$lab_line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@num"/>
													<xsl:choose>
														<xsl:when test="position() = last()"/>
														<xsl:otherwise>
															<xsl:text>,&#160;</xsl:text>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
											</span>
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="$invalid_role_cnt &gt;= 1">
									<tr>
										<td width="10">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
										<td>
											<span class="Text">
												<xsl:value-of select="$lab_upload_failed_invalid_role_line"/>
												<xsl:text>：&#160;</xsl:text>
												<xsl:for-each select="invalid_role_lines/line">
													<xsl:value-of select="$lab_line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@num"/>
													<xsl:choose>
														<xsl:when test="position() = last()"/>
														<xsl:otherwise>
															<xsl:text>,&#160;</xsl:text>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
											</span>
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="$no_group_specified_cnt &gt;= 1">
									<tr>
										<td width="10">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
										<td>
											<span class="Text">
												<xsl:value-of select="$lab_upload_failed_no_group_specified"/>
												<xsl:text>：&#160;</xsl:text>
												<xsl:for-each select="no_group_specified/line">
													<xsl:value-of select="$lab_line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@num"/>
													<xsl:choose>
														<xsl:when test="position() = last()"/>
														<xsl:otherwise>
															<xsl:text>,&#160;</xsl:text>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
											</span>
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="$invalid_gradecode_cnt &gt;= 1">
									<tr>
										<td width="10">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
										<td>
											<span class="Text">
												<xsl:value-of select="$lab_upload_failed_invalid_gradecode_cnt"/>
												<xsl:text>：&#160;</xsl:text>
												<xsl:for-each select="no_group_specified/line">
													<xsl:value-of select="$lab_line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@num"/>
													<xsl:choose>
														<xsl:when test="position() = last()"/>
														<xsl:otherwise>
															<xsl:text>,&#160;</xsl:text>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
											</span>
										</td>
									</tr>
								</xsl:if>								
								<xsl:if test="$invalid_supervise_groupcode_cnt &gt;= 1">
									<tr>
										<td width="10">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
										<td>
											<span class="Text">
												<xsl:value-of select="$lab_upload_failed_invalid_supervise_groupcode"/>
												<xsl:text>：&#160;</xsl:text>
												<xsl:for-each select="invalid_supervise_groupcode_lines/line">
													<xsl:value-of select="$lab_line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@num"/>
													<xsl:choose>
														<xsl:when test="position() = last()"/>
														<xsl:otherwise>
															<xsl:text>,&#160;</xsl:text>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
												<xsl:text>：</xsl:text>
												<xsl:text>&#160;</xsl:text>
											</span>
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="$invalid_direct_supervisor_id_cnt &gt;= 1">
									<tr>
										<td width="10">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
										<td>
											<span class="Text">
												<xsl:value-of select="$lab_upload_failed_invalid_direct_supervise_id"/>
												<xsl:text>：&#160;</xsl:text>
												<xsl:for-each select="invalid_direct_supervisor_id_lines/line">
													<xsl:value-of select="$lab_line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@num"/>
													<xsl:choose>
														<xsl:when test="position() = last()"/>
														<xsl:otherwise>
															<xsl:text>,&#160;</xsl:text>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
											</span>
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="$invalid_group_supervisor_group_cnt &gt;= 1">
									<tr>
										<td width="10">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
										<td>
											<span class="Text">
												<xsl:value-of select="$lab_upload_failed_invalid_group_supervisor_group_lines"/>
												<xsl:text>：&#160;</xsl:text>
												<xsl:for-each select="invalid_group_supervisor_group_lines/line">
													<xsl:value-of select="$lab_line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@num"/>
													<xsl:choose>
														<xsl:when test="position() = last()"/>
														<xsl:otherwise>
															<xsl:text>,&#160;</xsl:text>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
											</span>
										</td>
									</tr>
								</xsl:if>
								<!-- held_by_pending_appn-->
								<xsl:if test="$held_by_pending_appn_cnt &gt;1">
									<tr>
										<td width="10">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
										<td>
											<span class="Text">
												<xsl:value-of select="$lab_upload_failed_held_by_pending_appn"/>
												<xsl:text>：&#160;</xsl:text>
												<xsl:for-each select="held_by_pending_appn/line">
													<xsl:value-of select="$lab_line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@num"/>
													<xsl:choose>
														<xsl:when test="position() = last()"/>
														<xsl:otherwise>
															<xsl:text>,&#160;</xsl:text>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
											</span>
										</td>
									</tr>
								</xsl:if>
								<!-- held_by_pending_approval_appn-->
								<xsl:if test="$held_by_pending_approval_appn_cnt &gt;1">
									<tr>
										<td width="10">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
										<td>
											<span class="Text">
												<xsl:value-of select="$lab_upload_failed_held_by_pending_approval_appn"/>
												<xsl:text>：&#160;</xsl:text>
												<xsl:for-each select="held_by_pending_approval_appn/line">
													<xsl:value-of select="$lab_line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="@num"/>
													<xsl:choose>
														<xsl:when test="position() = last()"/>
														<xsl:otherwise>
															<xsl:text>,&#160;</xsl:text>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
											</span>
										</td>
									</tr>								
								</xsl:if>									
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td>
							<table cellpadding="3" cellspacing="0" border="0" width="100%">
								<tr>
									<td>
										<span class="Text">
											<xsl:value-of select="$lab_error_msg"/>
										</span>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<xsl:call-template name="wb_ui_line"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_line"/>
				<xsl:choose>
					<xsl:when test="count(enterprise/person) &gt;=1">
						<xsl:if test="unknown_col_list/unknown_col">
							<table border="0" cellspacing="0" cellpadding="3" width="{$wb_gen_table_width}" >
								<tr>
								<td width="4">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								<td>
									<font color="red">
									<xsl:value-of select="$lab_unknown_col"/>
									<xsl:for-each select="unknown_col_list/unknown_col">
										<xsl:value-of select="text()"/>
										<xsl:if test="position() != last()">
											<xsl:text>,&#160;</xsl:text>
										</xsl:if>
									</xsl:for-each>
									</font>
								</td>
								</tr>
							</table>
						</xsl:if>

						<table border="0" cellspacing="0" cellpadding="3" width="{$wb_gen_table_width}">
							<tr class="SecBg">
								<td width="4">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								<xsl:apply-templates select="used_column/column[text() != 'Role Type']" mode="draw_header">
									<xsl:with-param name="lab_group_code_ref" select="$lab_group_code_ref"/>
								</xsl:apply-templates>
								<td width="4">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
							</tr>
							<xsl:apply-templates select="enterprise">
								<xsl:with-param name="lab_male" select="$lab_male"/>
								<xsl:with-param name="lab_female" select="$lab_female"/>
							</xsl:apply-templates>
						</table>
						<xsl:call-template name="wb_ui_line"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_no_upload_usr"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
		<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="3">
			<tr>
				<td align="center">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_close"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:window.close()</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
