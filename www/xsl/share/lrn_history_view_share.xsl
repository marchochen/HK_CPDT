<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="../utils/wb_ui_hdr.xsl"/>
	<xsl:import href="../utils/wb_ui_title.xsl"/>
	<xsl:import href="../utils/wb_ui_desc.xsl"/>
	<xsl:import href="../utils/wb_ui_head.xsl"/>
	<xsl:import href="../utils/wb_ui_line.xsl"/>
	<xsl:import href="../utils/wb_ui_space.xsl"/>
	<xsl:import href="../utils/wb_ui_footer.xsl"/>
	<xsl:import href="../utils/display_score.xsl"/>
	<xsl:import href="../utils/wb_gen_button.xsl"/>
	<xsl:import href="../utils/wb_gen_form_button.xsl"/>
	<xsl:import href="../utils/wb_ui_pagination.xsl"/>
	<xsl:import href="../utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="../utils/display_time.xsl"/>
	<xsl:import href="../utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="../utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="../utils/display_form_input_time.xsl"/>	
	<xsl:import href="label_lrn_soln.xsl"/>
	<xsl:template name="content">
		<xsl:param name="lab_home"/>
		<xsl:param name="lab_cal_yr"/>
		<xsl:param name="lab_lrn_history"/>
		<xsl:param name="lab_lrn_rpt"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_group"/>
		<xsl:param name="lab_grade"/>
		<xsl:param name="lab_compl_date"/>
		<xsl:param name="lab_enroll_date"/>
		<xsl:param name="lab_category"/>
		<xsl:param name="lab_duration"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_result"/>
		<xsl:param name="lab_prev"/>
		<xsl:param name="lab_next"/>
		<xsl:param name="lab_total"/>
		<xsl:param name="lab_grand_total"/>
		<xsl:param name="lab_showing"/>
		<xsl:param name="lab_learner"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_recycle_bin"/>
		<xsl:param name="lab_remark"/>
		<xsl:param name="lab_details"/>
		<xsl:param name="lab_track_report"/>
		<xsl:param name="lab_lrn_history_of"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_course_title_keywords"/>
		<xsl:param name="lab_attendance_status"/>
		<xsl:param name="lab_number_of_courses_taken"/>
		<xsl:param name="lab_total_credit_earned"/>
		<xsl:param name="lab_credit"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_detailed_records"/>
		<xsl:param name="lab_lrn_history_desc"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_g_txt_btn_refresh"/>
		<xsl:param name="lab_lrn_history_from"/>
		<xsl:param name="is_student_own_view"/>
		<xsl:param name="lab_g_txt_btn_review"/>
		<xsl:param name="lab_last_access"/>
		<xsl:param name="lab_time_spent"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="popup"/>
		<xsl:param name="lab_tc_title"/>
		<xsl:param name="tc_enabled"/>
		<xsl:param name="lab_compl_completedate"/>
		<xsl:variable name="cur_sort_col" select="report_body/pagination/@sort_col"/>
		<xsl:variable name="cur_sort_order" select="report_body/pagination/@sort_order"/>
		<xsl:variable name="sort_order_by">
			<xsl:choose>
				<xsl:when test="$cur_sort_order = 'ASC' or $cur_sort_order = 'asc' ">DESC</xsl:when>
				<xsl:otherwise>ASC</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="$popup = 'false'">
			<xsl:call-template name="wb_ui_hdr"/>
		</xsl:if>
		<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text_class" >pop-up-title</xsl:with-param>
				<xsl:with-param name="text">
					<xsl:value-of select="$lab_lrn_history"/>
						<xsl:if test="$is_student_own_view = 'false'"> - <xsl:value-of select="report_body/student/@display_bil"/>
					   </xsl:if>
				</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_desc">
				<xsl:with-param name="text"><xsl:value-of select="$lab_lrn_history_desc"/></xsl:with-param>
			</xsl:call-template>
			<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
				<tr>
					<td align="right">
						<xsl:if test="report_body/student">
							<input type="hidden" name="cmd"/>
							<input type="hidden" name="module"/>
							<input type="hidden" name="download"/>
							<input type="hidden" name="spec_name"/>
							<input type="hidden" name="spec_value"/>
							<input type="hidden" name="stylesheet"/>
							<input type="hidden" name="rpt_type" value="learner"/>
							<input type="hidden" name="usr_ent_id" value="{report_body/student/@ent_id}"/>
							<table cellpadding="0" cellspacing="0" border="0">
								<tr>
									<td align="right">
										<span class="Text"><xsl:value-of select="$lab_lrn_history_from"/>&#160;</span>
									</td><td>
									<xsl:call-template name="display_form_input_time">
										<xsl:with-param name="timestamp"><xsl:value-of select="report_body/spec/data_list/data[@name = 'att_create_start_datetime']/@value"/></xsl:with-param>
										<xsl:with-param name="fld_name">att_create_start_datetime</xsl:with-param>
										<xsl:with-param name="hidden_fld_name">att_create_start_datetime</xsl:with-param>
										<xsl:with-param name="show_label">Y</xsl:with-param>
									</xsl:call-template>
									</td><td><span class="Text">
									<xsl:text>&#160;</xsl:text>
									<xsl:value-of select="$lab_to"/>
									<xsl:text>&#160;</xsl:text></span>
									</td><td>
									<xsl:call-template name="display_form_input_time">
										<xsl:with-param name="timestamp"><xsl:value-of select="report_body/spec/data_list/data[@name = 'att_create_end_datetime']/@value"/></xsl:with-param>
										<xsl:with-param name="fld_name">att_create_end_datetime</xsl:with-param>
										<xsl:with-param name="hidden_fld_name">att_create_end_datetime</xsl:with-param>
										<xsl:with-param name="show_label">Y</xsl:with-param>
									</xsl:call-template>
									</td><td>
									
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_txt_btn_refresh"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_href">Javascript:lrn_soln.refresh_lrn_history(document.frmXml,"<xsl:value-of select="$wb_lang"/>")</xsl:with-param>
										</xsl:call-template>
									</td>
								</tr>
							</table>
						</xsl:if>
					</td>
				</tr>
			</table>
		
		<xsl:choose>
			<xsl:when test="count(report_body/report_list/record)=0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_item"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_line"/>
			</xsl:when>
			<xsl:otherwise>
			
				<table cellpadding="3" cellspacing="0" style="margin-top:20px;" width="{$wb_gen_table_width}" border="0">
					<tr class="SecBg">
						<td valign="middle">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td valign="middle" height="19" align="left" nowrap="nowrap">
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 't_title' ">
									<a href="Javascript:sortCol('t_title','{$sort_order_by}')" class="TitleText">
										<xsl:value-of select="$lab_title"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
											<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="Javascript:sortCol('t_title','asc')" class="TitleText">
										<xsl:value-of select="$lab_title"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<xsl:if test="$is_student_own_view = 'true'">
						<td valign="middle" align="center">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						</xsl:if>						
						<td valign="middle" nowrap="nowrap" align="left">
							<span class="TitleText" style="color:#999999;"><xsl:value-of select="$lab_type"/></span>
						</td>
						<xsl:if test="$tc_enabled = 'true'">
							<td valign="middle" nowrap="nowrap" align="left">
								<span class="TitleText" style="color:#999999;"><xsl:value-of select="$lab_tc_title"/></span>
							</td>
						</xsl:if>
						<td valign="middle" nowrap="nowrap" align="left">
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'att_create_timestamp' ">
									<a href="Javascript:sortCol('att_create_timestamp','{$sort_order_by}')" class="TitleText">
										<xsl:value-of select="$lab_enroll_date"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
											<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="Javascript:sortCol('att_create_timestamp','asc')" class="TitleText">
										<xsl:value-of select="$lab_enroll_date"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>

						<td valign="middle" align="center">
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'att_ats_id' ">
									<a href="Javascript:sortCol('att_ats_id','{$sort_order_by}')" class="TitleText">
										<xsl:value-of select="$lab_attendance_status"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
											<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="Javascript:sortCol('att_ats_id','asc')" class="TitleText">
										<xsl:value-of select="$lab_attendance_status"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td valign="middle" align="center">
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'att_timestamp' ">
									<a href="Javascript:sortCol('att_timestamp','{$sort_order_by}')" class="TitleText">
										<xsl:value-of select="$lab_compl_completedate"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
											<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="Javascript:sortCol('att_timestamp','asc')" class="TitleText">
										<xsl:value-of select="$lab_compl_completedate"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td valign="middle" nowrap="nowrap" align="center">
							<span class="TitleText" style="color:#999999;"><xsl:value-of select="$lab_last_access"/></span>
						</td>
						<td valign="middle" align="center">
							<span class="TitleText" style="color:#999999;"><xsl:value-of select="$lab_time_spent"/></span>
						</td>
						<td valign="middle" align="center">
							<span class="TitleText" style="color:#999999;"><xsl:value-of select="$lab_score"/></span>
						</td>
						<td valign="middle">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<tr>
					  <td colspan="20"><xsl:call-template name="wb_ui_line"/></td>
					</tr>
					<xsl:apply-templates select="report_body/report_list/record">
						<xsl:with-param name="lab_na" select="$lab_na"/>
						<xsl:with-param name="lab_recycle_bin" select="$lab_recycle_bin"/>
						<xsl:with-param name="lab_details" select="$lab_details"/>
						<xsl:with-param name="is_student_own_view" select="$is_student_own_view"/>
						<xsl:with-param name="lab_g_txt_btn_review" select="$lab_g_txt_btn_review"/>
					</xsl:apply-templates>
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="report_body/pagination/@cur_page"/>
					<xsl:with-param name="page_size" select="report_body/pagination/@page_size"/>
					<xsl:with-param name="total" select="report_body/pagination/@total_rec"/>
					<xsl:with-param name="width" select="$wb_gen_table_width"/>
					<xsl:with-param name="timestamp" select="report_body/pagination/@timestamp"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="$popup = 'true'">
				<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr>
						<td align="center">
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_close"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:window.close()</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_target">_parent</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_footer"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="report_body/report_list/record">
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_recycle_bin"/>
		<xsl:param name="lab_details"/>
		<xsl:param name="is_student_own_view"/>
		<xsl:param name="lab_g_txt_btn_review"/>
		<xsl:variable name="usr_ent_id" select="student/@ent_id"/>
		<xsl:variable name="child_itm_title" select="application/@itm_title"/>
		<tr>
			<xsl:attribute name="class"><xsl:choose><xsl:when test="position() mod 2 =1">RowsOdd</xsl:when><xsl:otherwise>RowsEven</xsl:otherwise></xsl:choose></xsl:attribute>
			<td valign="middle">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td height="19" align="left">
			<!--
				<span class="Text"><xsl:value-of select="item/@title"/>
				<xsl:text> - </xsl:text>
				<xsl:value-of select="application/@itm_title"/>
			    </span>
			    -->
			<span class="Text">
				<xsl:choose>
					<xsl:when test="item/@type = 'CLASSROOM'">
						<xsl:value-of select="item/@title"/>
						<xsl:text> - </xsl:text>
						<xsl:value-of select="$child_itm_title"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="item/@title"/>
					</xsl:otherwise>
				</xsl:choose>
			</span>
			</td>
			<xsl:if test="$is_student_own_view = 'true'">
			<td align="left">
				<xsl:choose>
					<xsl:when test="(item[@course_id]/@course_id &gt; 0) and (item[@course_mod_cnt]/@course_mod_cnt &gt; 0)">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_review"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:lrn_soln.lrn_tracking_rpt(<xsl:value-of select="student/@ent_id"/>,<xsl:value-of select="item[@course_id]/@course_id"/>,p,<xsl:value-of select="application/@app_tkh_id"/>)</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></xsl:otherwise>
				</xsl:choose>				
				
				<!--<xsl:choose>
					<xsl:when test="(item/@course_id &gt; 0) and (item/@course_mod_cnt &gt; 0)">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_href">
								<xsl:text>javascript:lrn_soln.lrn_tracking_rpt(</xsl:text><xsl:value-of select="$student_id"/><xsl:text>,</xsl:text><xsl:value-of select="item/@course_id"/><xsl:text>,p,</xsl:text><xsl:value-of select="application/@app_tkh_id"/><xsl:text>)</xsl:text>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_details"/>
						</xsl:call-template>									
					</xsl:when>
					<xsl:otherwise><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></xsl:otherwise>
				</xsl:choose>	
				-->			
			</td>
			</xsl:if>			
			<td align="left">
				<span class="Text">
					<xsl:variable name="cur_record_type" select="item/@dummy_type"/>
					<xsl:call-template name="get_ity_title">
						<xsl:with-param name="itm_type" select="$cur_record_type"/> 
					</xsl:call-template>
				</span>
			</td>
			<xsl:if test="$tc_enabled = 'true'">
				<td align="left">
					<span class="Text"><xsl:value-of select="training_center"/></span>
				</td>
			</xsl:if>
			<td align="left" nowrap="nowrap">
				<span class="Text">
					<xsl:call-template name="display_time">
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="attendance/@create_date"/>
						</xsl:with-param>
					</xsl:call-template>
				</span>
			</td>
			<td align="center">
				<span class="Text"><xsl:variable name="status_id"><xsl:choose><xsl:when test="attendance/@status=''">0</xsl:when><xsl:otherwise><xsl:value-of select="attendance/@status"/></xsl:otherwise></xsl:choose></xsl:variable><xsl:choose><xsl:when test="$status_id='0'"><xsl:value-of select="$lab_na"/></xsl:when><xsl:otherwise><xsl:apply-templates select="../../meta/attendance_status_list/status[@id=$status_id]"/></xsl:otherwise></xsl:choose></span>
			</td>
			<td align="center" nowrap="nowrap">
				<xsl:choose>
					<xsl:when test="attendance/@datetime!='' and attendance/@datetime">
						<span class="Text"><xsl:call-template name="display_time"><xsl:with-param name="my_timestamp"><xsl:value-of select="attendance/@datetime"/></xsl:with-param></xsl:call-template></span>
					</xsl:when>
					<xsl:otherwise>
						<span class="Text"><xsl:value-of select="$lab_na"/></span>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td height="19" align="center" nowrap="nowrap">
				<span class="Text"><xsl:choose><xsl:when test="aicc_data/@last_acc_datetime!='' and aicc_data/@last_acc_datetime"><xsl:call-template name="display_time"><xsl:with-param name="my_timestamp"><xsl:value-of select="aicc_data/@last_acc_datetime"/></xsl:with-param></xsl:call-template></xsl:when><xsl:otherwise><xsl:value-of select="$lab_na"/></xsl:otherwise></xsl:choose></span>
			</td>
			<td height="19" align="center">
				<span class="Text">
					<xsl:choose>
						<xsl:when test="aicc_data/@used_time != '' and aicc_data/@used_time">
							<xsl:value-of select="substring-before(aicc_data/@used_time,'.')"/>
						</xsl:when>
					<xsl:otherwise><xsl:value-of select="$lab_na"/></xsl:otherwise></xsl:choose></span>
			</td>
			<td height="19" align="center">
				<span class="Text"><xsl:choose>
				<xsl:when test="aicc_data/@score != '' and aicc_data/@score">
				<xsl:call-template name="display_score">
					<xsl:with-param name="score"><xsl:value-of select="aicc_data/@score"/></xsl:with-param>
				</xsl:call-template>					
				</xsl:when><xsl:otherwise><xsl:value-of select="$lab_na"/></xsl:otherwise></xsl:choose></span>
			</td>
			<td valign="middle">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="report_body/meta/attendance_status_list/status">
		<xsl:call-template name="get_ats_title">
			<xsl:with-param name="ats_id" select="@id"/>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
