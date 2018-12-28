<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="enrol_approval_details">
		<xsl:param name="lab_enrollment_details"/>
		<xsl:param name="lab_g_txt_btn_view_lrn_his"/>
		<xsl:param name="lab_learner"/>
		<xsl:param name="lab_course"/>
		<xsl:param name="lab_enrollment_log"/>
		<xsl:param name="lab_action"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_by"/>
		<xsl:param name="lab_remarks"/>
		<xsl:param name="lab_no_action"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_pending_approver"/>
		<xsl:param name="lab_DIRECT_SUPERVISE"/>
		<xsl:param name="lab_TADM"/>
		<xsl:param name="lab_SUPERVISE"/>
		<xsl:param name="lab_na"/>
		<table>
			<tr>
				<td width="50%" align="left">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="$lab_enrollment_details"/>
					</xsl:call-template>
			</td>
				<td width="50%" align="right">
					<xsl:apply-templates select="/applyeasy/workflow/process[@id = $process_id]/status[@id = $status_id]" mode="status"/>
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_view_lrn_his"/>
						<xsl:with-param name="class" >btn wzb-btn-blue margin-right4 </xsl:with-param>
					
					<!-- 
					<xsl:with-param name="wb_gen_btn_href">javascript:srh_lrn_history_prep(<xsl:value-of select="$app_ent_id"/>)</xsl:with-param>
					 -->
					<!-- 
											<xsl:with-param name="wb_gen_btn_href"><xsl:value-of select="$app_ent_id"/></xsl:with-param>
					 -->
					 
					<xsl:with-param name="wb_gen_btn_href">javascript:srh_lrn_history_prep(<xsl:value-of select="$app_ent_id"/>)</xsl:with-param>
					</xsl:call-template>	
				</td>
			</tr>
		
		</table>	
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_learner"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<table>
						<tr>
							<td>
								<a href="javascript:usr.user.manage_usr_popup({$app_ent_id},{$root_ent_id},'','')" class="Text">
									<xsl:value-of select="$app_name"/>
								</a>
							</td>
							<td>
								
							</td>
						</tr>
					</table>				
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_course"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:variable name="itm_id" select="item_template/item[@run_ind='false']/@id"/>
					<a href="javascript:itm.itm_view_popup({$itm_id})" class="Text">
						<xsl:value-of select="item_template/item[@run_ind='false']/title/text()"/>
						<xsl:if test="item_template/item[@run_ind='true']">
							<xsl:text> - </xsl:text>
							<xsl:value-of select="item_template/item[@run_ind='true']/title/text()"/>
						</xsl:if>
					</a>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_status"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="application/application_process/process/status/@name"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_pending_approver"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:variable name="pending_approval_role" select="application/pending_approval_role/text()"/>
					<xsl:choose>
						<xsl:when test="$pending_approval_role = 'DIRECT_SUPERVISE'">
							<xsl:value-of select="$lab_DIRECT_SUPERVISE"/>
						</xsl:when>
						<xsl:when test="$pending_approval_role = 'TADM'">
							<xsl:value-of select="$lab_TADM"/>
						</xsl:when>
						<xsl:when test="$pending_approval_role = 'SUPERVISE'">
							<xsl:value-of select="$lab_SUPERVISE"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_na"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>			
			<xsl:if test="/applyeasy/current_approver_ind/text() = 'true'">
			<!--
				<xsl:if test="/applyeasy/workflow/process[@id = $process_id]/status[@id = $status_id]/action/access/role[@id = $my_role]">
			-->
					<tr>
						<td class="wzb-form-label">
						</td>
						<td class="wzb-form-control">
							
						</td>
					</tr>
			<!--
				</xsl:if>
			-->
			</xsl:if>
		</table>
		<xsl:call-template name="wb_ui_space"/>
		<xsl:choose>
			<xsl:when test="$enrollment_log/action">
				<xsl:apply-templates select="$enrollment_log" mode="enrollment_log">
					<xsl:with-param name="lab_enrollment_log" select="$lab_enrollment_log"/>
					<xsl:with-param name="lab_action" select="$lab_action"/>
					<xsl:with-param name="lab_date" select="$lab_date"/>
					<xsl:with-param name="lab_by" select="$lab_by"/>
					<xsl:with-param name="lab_remarks" select="$lab_remarks"/>
					<xsl:with-param name="lab_DIRECT_SUPERVISE" select="$lab_DIRECT_SUPERVISE"/>
					<xsl:with-param name="lab_TADM" select="$lab_TADM"/>
					<xsl:with-param name="lab_SUPERVISE" select="$lab_SUPERVISE"/>					
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_enrollment_log"/>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_action"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="enrollment_log">
		<xsl:param name="lab_enrollment_log"/>
		<xsl:param name="lab_action"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_by"/>
		<xsl:param name="lab_remarks"/>
		<xsl:param name="lab_DIRECT_SUPERVISE"/>
		<xsl:param name="lab_TADM"/>
		<xsl:param name="lab_SUPERVISE"/>		
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_enrollment_log"/>
			</xsl:with-param>
		</xsl:call-template>
		<table class="table wzb-ui-table">
			<tr class="wzb-ui-table-head">
				<td align="left" width="25%">
					<xsl:value-of select="$lab_action"/>
				</td>
				<td align="center" width="25%">
					<xsl:value-of select="$lab_by"/>
				</td>
				<td align="center" width="25%">
					<xsl:value-of select="$lab_date"/>
				</td>
				<td align="center" width="25%">
					<xsl:value-of select="$lab_remarks"/>
				</td>
			</tr>
			<xsl:for-each select="action">
				<xsl:variable name="action_id" select="@id"/>
				<tr>
					<td align="left">
						<xsl:value-of select="@verb"/>
					</td>
					<td align="center">
						<xsl:value-of select="display_name"/>
						<xsl:choose>
							<xsl:when test="approval_role">
									<xsl:choose>
										<xsl:when test="approval_role = 'DIRECT_SUPERVISE'">
											(<xsl:value-of select="$lab_DIRECT_SUPERVISE"/>)
										</xsl:when>
										<xsl:when test="approval_role = 'TADM'">
											(<xsl:value-of select="$lab_TADM"/>)
										</xsl:when>
										<xsl:when test="approval_role = 'SUPERVISE'">
											(<xsl:value-of select="$lab_SUPERVISE"/>)
										</xsl:when>
									</xsl:choose>								
							</xsl:when>
						</xsl:choose>
					</td>
					<td align="center">
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp">
								<xsl:value-of select="@upd_timestamp"/>
							</xsl:with-param>
							<xsl:with-param name="dis_time">T</xsl:with-param>
						</xsl:call-template>
					</td>
					<td align="center">
						<xsl:choose>
							<xsl:when test="../../comment_history/comment[@action_id = $action_id]">
								<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:value-of select="../../comment_history/comment[@action_id = $action_id]/content/text()"/>										
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="status">
		<xsl:for-each select="action">
			<xsl:if test="access/approver[@type = 'current_approvers']">
				<xsl:variable name="require_reason" select="@require_reason"/>
				<xsl:variable name="process_id" select="../../@id"/>
				<xsl:variable name="next_status_id" select="@next_status"/>
				<xsl:variable name="next_status" select="/applyeasy/workflow/process[@id = $process_id]/status[@id=$next_status_id]/@name"/>
				<!--<input type="button" class="Btn" value="{@name}" onclick="javascript:app.action_exec(frmAction,{$app_id},{../../@id},{../@id},{@id},'{../@name}','{$next_status}','{@verb}')"/>-->
				<input type="button" class="btn wzb-btn-orange margin-right4" value="{@name}" onclick="javascript:app.enrol_approval.process_enrol(document.frmAction, '{$wb_lang}', {$app_id}, '{$app_update_timestamp}', {../../@id}, {../@id}, {@id}, '{../@name}',  '{$next_status}', '{@verb}',  '{$require_reason}')"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
