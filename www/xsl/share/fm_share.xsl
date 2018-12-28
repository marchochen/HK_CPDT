<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../utils/select_all_checkbox.xsl"/>
	<xsl:import href="../utils/display_hhmm.xsl"/>
	<xsl:import href="../utils/wb_gen_button.xsl"/>
	<xsl:import href="../utils/display_time.xsl"/>
	<xsl:import href="../utils/wb_ui_hdr.xsl"/>
	<xsl:import href="label_fm.xsl"/>

	<!-- Welcome to wizBank Version 3.5 Facility Management XSL Utility ===================== -->
	<!-- =============================================================== -->
	<xsl:variable name="_no_conflicts">
		<xsl:if test="count(/fm/facility_availability/facility_schedule_list/facility_schedule/conflict_list/facility_schedule) = 0">true</xsl:if>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:variable name="lab_new_rsv">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">新增預訂</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">新增预订</xsl:when>
			<xsl:otherwise>New Reservation</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_rsv_cart">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">我的預訂</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">我的预订</xsl:when>
			<xsl:otherwise>Reservation Cart</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_rsv_calendar">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">預訂日曆</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">预订日历</xsl:when>
			<xsl:otherwise>Reservation Calendar</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_rsv_record">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">預訂記錄</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">预订记录</xsl:when>
			<xsl:otherwise>Reservation Record</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_facility_info">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">設施資料</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">设施信息</xsl:when>
			<xsl:otherwise>Facility Information</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_add">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">新增</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">添加</xsl:when>
			<xsl:otherwise>Add</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_facility_fee_search">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">設施費用查詢</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">设施费用查询</xsl:when>
			<xsl:otherwise>Search training equipment expense</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="facility_subtype" mode="fm">
		<xsl:param name="hasSelectAll">false</xsl:param>
		<xsl:param name="hideOffItem">false</xsl:param>
		<xsl:param name="hasAddBtn">false</xsl:param>
		<xsl:param name="classBg">SecBg</xsl:param>
		<xsl:param name="classFsBg">RowsEven</xsl:param>
		<xsl:param name="classFsText">Text</xsl:param>
		<xsl:param name="classFsHeadText">TitleText</xsl:param>
		<xsl:param name="classFsHRBarBg">Line</xsl:param>
		<xsl:param name="PageWidth" select="$wb_gen_table_width"/>
		<xsl:param name="col_count">4</xsl:param>
		<xsl:param name="frm_name">frmXml</xsl:param>
		<xsl:param name="xsl_prefix"/>
		<xsl:param name="ext_venue_list">false</xsl:param>
		<xsl:param name="ext_venue_name"/>	
		<xsl:param name="fac_details_edit">false</xsl:param>
		
		<xsl:variable name="fac_id" select="@id"/>
		<xsl:variable name="AddFunction">javascript:fm.ins_facility_details_prep(<xsl:value-of select="$fac_id"/>,'<xsl:value-of select="$xsl_prefix"/>')</xsl:variable>
		<xsl:variable name="TableWidth">
			<xsl:value-of select="floor(($PageWidth div $col_count) - 10)"/>
		</xsl:variable>
		<xsl:variable name="fac_subtype_name"><xsl:call-template name="fm_display_title"/></xsl:variable>
		<xsl:variable name="is_ext">
			<xsl:choose>
				<xsl:when test="$ext_venue_name = $fac_subtype_name">true</xsl:when>
				<xsl:otherwise>false</xsl:otherwise>
			</xsl:choose>					
		</xsl:variable>		
		
	 	
		
		<table>
			<tr>
				<xsl:if test="$hasSelectAll = 'true'">
					
					<td height="20" align="left" width="2%">
			 
						<xsl:call-template name="select_all_checkbox">
							<xsl:with-param name="chkbox_lst_cnt"><xsl:value-of select="count(facility)"/></xsl:with-param>
							<xsl:with-param name="chkbox_lst_nm"><xsl:choose>
								<xsl:when test="$is_ext = 'true'">	fm_ext_fac_id_lst_<xsl:value-of select="$fac_id"/></xsl:when>
								<xsl:otherwise>fm_fac_id_lst_<xsl:value-of select="$fac_id"/></xsl:otherwise>
							</xsl:choose></xsl:with-param>
							<xsl:with-param name="frm_name"><xsl:value-of select="$frm_name"/></xsl:with-param>
							<xsl:with-param name="sel_all_chkbox_nm">select_all_<xsl:value-of select="$fac_id"/></xsl:with-param>
							<xsl:with-param name="display_icon">false</xsl:with-param>
							
				 
						</xsl:call-template>
						</td>
					</xsl:if>
					<td>
					<xsl:if test="$hasSelectAll = 'true'">
						<xsl:attribute name="width"></xsl:attribute>
					</xsl:if>
					<xsl:choose>
						<xsl:when test="$hasAddBtn = 'true'">
							<table>
								<tr>
									<td>
										<xsl:call-template name="fm_display_title"/>&#160;
										<!--/td>
										<td align="right"-->
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_add"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_href"><xsl:value-of select="$AddFunction"/></xsl:with-param>
										</xsl:call-template>

<!--										<input type="button" name="" value="{$lab_add}" class="wbGenSubBtnFrm">
											<xsl:if test="$AddFunction != ''">
												<xsl:attribute name="onclick"><xsl:value-of select="$AddFunction"/></xsl:attribute>
											</xsl:if>
										</input>-->
									</td>
								</tr>
							</table>
						</xsl:when>
						<xsl:otherwise>
							<span class="{$classFsHeadText}"><xsl:call-template name="fm_display_title"/></span>
						</xsl:otherwise>
					</xsl:choose>
					</td>
			</tr>
		</table>
		<xsl:if test="facility">
		
	
			<table cellpadding="2" cellspacing="1" border="0" width="{$TableWidth}" class="{$classBg}">
				<xsl:apply-templates mode="fm">
					<xsl:with-param name="hideOffItem"><xsl:value-of select="$hideOffItem"/></xsl:with-param>
					<xsl:with-param name="hasSelectAll"><xsl:value-of select="$hasSelectAll"/></xsl:with-param>
					<xsl:with-param name="classFsBg"><xsl:value-of select="$classFsBg"/></xsl:with-param>
					<xsl:with-param name="classFsText"><xsl:value-of select="$classFsText"/></xsl:with-param>
					<xsl:with-param name="fac_id"><xsl:value-of select="$fac_id"/></xsl:with-param>
					<xsl:with-param name="xsl_prefix"><xsl:value-of select="$xsl_prefix"/></xsl:with-param>
					<xsl:with-param name="is_ext"><xsl:value-of select="$is_ext"/></xsl:with-param>
					<xsl:with-param name="fac_details_edit"><xsl:value-of select="$fac_details_edit"/></xsl:with-param>
				</xsl:apply-templates>
			</table>
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="fm_display_title">
		<xsl:choose>
			<xsl:when test="@id and @id != ''">
				<xsl:call-template name="get_ftp_title"/>
			</xsl:when>
			<xsl:otherwise>&#160;</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="facility" mode="fm">
		<xsl:param name="classFsBg">RowsEven</xsl:param>
		<xsl:param name="classFsText">Text</xsl:param>
		<xsl:param name="hasSelectAll">false</xsl:param>
		<xsl:param name="hideOffItem">false</xsl:param>
		<xsl:param name="xsl_prefix"/>
		<xsl:param name="fac_id"/>
		<xsl:param name="is_ext"/>
		<xsl:param name="fac_details_edit">false</xsl:param>
		<tr>
	 
			<xsl:if test="$hasSelectAll = 'true'">
			
			
				<td align="left" width="2%">
					<xsl:choose>
						<xsl:when test="$hideOffItem = 'true'">
						<xsl:if test="@status = 'ON'">
						<input type="checkbox" value="{@id}">
						<xsl:attribute name="name"><xsl:choose>
							<xsl:when test="$is_ext = 'true'">fm_ext_fac_id_lst_<xsl:value-of select="$fac_id"/></xsl:when>
							<xsl:otherwise>fm_fac_id_lst_<xsl:value-of select="$fac_id"/></xsl:otherwise>
						</xsl:choose></xsl:attribute>
						</input></xsl:if>
						</xsl:when>
						<xsl:otherwise><input type="checkbox" value="{@id}">
						<xsl:attribute name="name"><xsl:choose>
							<xsl:when test="$is_ext = 'true'">fm_ext_fac_id_lst_<xsl:value-of select="$fac_id"/></xsl:when>
							<xsl:otherwise>fm_fac_id_lst_<xsl:value-of select="$fac_id"/></xsl:otherwise>
						</xsl:choose></xsl:attribute>					
						</input></xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
			<td align="left" >
				<xsl:if test="$hasSelectAll = 'true'">
				</xsl:if>
				<xsl:choose>
					<xsl:when test="$fac_details_edit = 'true'">
						<a href="javascript:fm.get_facility_details({@id},'{$xsl_prefix}')" class="{$classFsText}">
							<xsl:value-of select="@title"/>
						</a>					
					</xsl:when>
					<xsl:otherwise>
						<a href="javascript:fm.get_facility_details_win({@id},'{$xsl_prefix}')" class="{$classFsText}">
							<xsl:value-of select="@title"/>
						</a>						
					</xsl:otherwise>
				</xsl:choose>

			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="fm_hdr">
		<xsl:param name="TableWidth" select="$wb_gen_table_width"/>
	<xsl:param name="navigation"></xsl:param>

	<table width="{$TableWidth}" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="top_menu_2">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="top_menu_left_2">
							<div class="horizontalcssmenu_2">
								<ul class="nav nav-tabs page-tabs" role="tablist">
										<li>
											<a href="javascript:fm.set_cart('true');fm.get_new_rsv();fm.clear_rsv_id()" style="width:125px;">
												<xsl:value-of select="$lab_new_rsv"/>
											</a>
										</li>
										<li class="horizontalcssmenu_first_2">
											<a href="javascript:fm.clear_rsv_id();fm.get_rsv_calendar_prep()" style="width:125px;">
												<xsl:value-of select="$lab_rsv_calendar"/>
											</a>
										</li>
										<li class="horizontalcssmenu_first_2">
											<a href="javascript:fm.get_rsv_record_srh()" style="width:125px;">
												<xsl:value-of select="$lab_rsv_record"/>
											</a>
										</li>
										<li class="horizontalcssmenu_first_2">
											<a href="javascript:fm.get_facility_info_lst()" style="width:125px;">
												<xsl:value-of select="$lab_facility_info"/>
											</a>
										</li>
										<li class="horizontalcssmenu_first_2">
											<xsl:if test="starts-with(//cur_usr/role/@id, 'ADM_')">
												<img hspace="1" align="absmiddle" border="0" src="{$wb_img_skin_path}wb_nav_sep.gif"/>
												<a href="javascript:fm.search_facility_fee_prep()" style="width:145px;">
													<xsl:value-of select="$lab_facility_fee_search"/>
												</a>
											</xsl:if>
										</li>
								</ul>
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="display_clash">
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_by"/>
		<xsl:param name="lab_commar"/>
		<xsl:param name="lab_no_of_participant"/>
		<xsl:param name="lab_reserved_by_other"/>
		<div class="Text">
			<xsl:choose>
				<xsl:when test="reservation/reservation_details/reserve_user/@display_name">
			<xsl:call-template name="display_hhmm">
				<xsl:with-param name="my_timestamp"><xsl:value-of select="@start_time"/></xsl:with-param>
			</xsl:call-template>
			&#160;<xsl:value-of select="$lab_to"/>&#160;
			<xsl:call-template name="display_hhmm">
				<xsl:with-param name="my_timestamp"><xsl:value-of select="@end_time"/></xsl:with-param>
			</xsl:call-template>
			<xsl:value-of select="$lab_commar"/>				
					<xsl:value-of select="reservation/reservation_details/purpose"/>
					<xsl:value-of select="$lab_by"/>
					<xsl:value-of select="reservation/reservation_details/reserve_user/@display_name"/>
					<xsl:value-of select="$lab_commar"/>
					<xsl:value-of select="$lab_no_of_participant"/>
					<xsl:value-of select="reservation/reservation_details/participant_no"/>	
				</xsl:when>
				<xsl:when test="create_user/@display_name != ''">
					<xsl:call-template name="display_hhmm">
						<xsl:with-param name="my_timestamp"><xsl:value-of select="@start_time"/></xsl:with-param>
					</xsl:call-template>
					&#160;<xsl:value-of select="$lab_to"/>&#160;
					<xsl:call-template name="display_hhmm">
						<xsl:with-param name="my_timestamp"><xsl:value-of select="@end_time"/></xsl:with-param>
					</xsl:call-template>
					<xsl:value-of select="$lab_commar"/>				
								<xsl:value-of select="$lab_by"/>
				<xsl:value-of select="create_user/@display_name"/>				
				</xsl:when>
				<xsl:otherwise>
				<xsl:value-of select="$lab_reserved_by_other"/>
				</xsl:otherwise>
			</xsl:choose>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="facility_availability" mode="fac_avail">
		<xsl:param name="lab_facility_avail"/>
		<xsl:param name="lab_facility"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_time"/>
		<xsl:param name="lab_conflict"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_by"/>
		<xsl:param name="lab_commar"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_no_of_participant"/>
		<xsl:param name="default_checked">false</xsl:param>
		<xsl:param name="lab_reserved_by_other"/>
		
		
		<table class="table wzb-ui-table">
			<tr class="wzb-ui-table-head">
				<td aligin="left" width="25%"> 
					<xsl:call-template name="select_all_checkbox">
						<xsl:with-param name="chkbox_lst_cnt"><xsl:value-of select="count(facility_schedule_list/facility_schedule[count(conflict_list/facility_schedule) = 0])"/></xsl:with-param>
						<xsl:with-param name="display_icon">true</xsl:with-param>
						<xsl:with-param name="sel_all_chkbox_nm">sel_all_checkbox</xsl:with-param>
						<xsl:with-param name="frm_name">frmAction</xsl:with-param>
 
					</xsl:call-template>
						<xsl:value-of select="$lab_facility"/>
				</td>
			 
				<td align="center" width="25%">
					<xsl:value-of select="$lab_date"/>
				</td>
				<td align="center" width="25%">
					<xsl:value-of select="$lab_time"/>
				</td>
				<xsl:if test="$_no_conflicts != 'true'">
					<td width="25%">
						<xsl:value-of select="$lab_conflict"/>
					</td>
				</xsl:if>
			</tr>
			<xsl:for-each select="facility_schedule_list/facility_schedule">
				<tr>
					<td align="left"  >
						<xsl:choose>
							<xsl:when test="conflict_list/facility_schedule">
								<IMG src="{$wb_img_path}fm_clash.gif" hspace="2" vspace="2" border="0" align="absmiddle"/>
							</xsl:when>
							<xsl:otherwise>
								<input type="checkbox" value="{position()}" name="fm_fac_id_lst_{facility/@id}">
									<xsl:if test="$default_checked = 'true'">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:value-of select="facility/basic/title"/>
					</td>
				 
					<td align="center" valign="top">
						<xsl:call-template name="display_time"><xsl:with-param name="my_timestamp"><xsl:value-of select="@date"/></xsl:with-param></xsl:call-template>
					</td>
					<td align="center" valign="top">
						<xsl:call-template name="display_hhmm"><xsl:with-param name="my_timestamp"><xsl:value-of select="@start_time"/></xsl:with-param></xsl:call-template>&#160;<xsl:value-of select="$lab_to"/>&#160;<xsl:call-template name="display_hhmm"><xsl:with-param name="my_timestamp"><xsl:value-of select="@end_time"/></xsl:with-param></xsl:call-template>
					</td>
					<xsl:if test="$_no_conflicts != 'true'">
						<td valign="top">
							<xsl:choose>
								<xsl:when test="conflict_list/facility_schedule">
									<xsl:for-each select="conflict_list/facility_schedule">
										<xsl:call-template name="display_clash">
											<xsl:with-param name="lab_to" select="$lab_to"/>
											<xsl:with-param name="lab_by" select="$lab_by"/>
											<xsl:with-param name="lab_commar" select="$lab_commar"/>
											<xsl:with-param name="lab_no_of_participant" select="$lab_no_of_participant"/>
											<xsl:with-param name="lab_reserved_by_other" select="$lab_reserved_by_other"/>
										</xsl:call-template>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_na"/>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</xsl:if>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
<xsl:template match="facility_type" mode="rsv">
		<xsl:param name="rsv_status"/>
		<xsl:param name="lab_ref"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_time"/>
		<xsl:param name="lab_pencil_in"/>
		<xsl:param name="lab_to"> - </xsl:param>
		<xsl:param name="hasAddBtn">false</xsl:param>
		<xsl:param name="mode">read</xsl:param>
		<xsl:param name="lab_remove"/>
		<xsl:param name="lab_add"/>
		<xsl:param name="rsv_id"/>
		<xsl:param name="lab_cancel_reason"/>
		<xsl:param name="start_datetime"/>
		<xsl:param name="end_datetime"/>
		<xsl:param name="fac_details_edit">false</xsl:param>
		<xsl:variable name="xsl_prefix" select="@xsl_prefix"/>
		<xsl:variable name="_col">
			<xsl:choose>
				<xsl:when test="$mode = 'edit' or $mode='fs_edit'">7</xsl:when>
				<xsl:otherwise>6</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="count(*[name() != 'title']) != 0">
		<table>
			<tr>
				<td colspan="{$_col}" align="right">
					<xsl:choose>					
					<xsl:when test="$mode='edit'">
						<xsl:choose>
							<xsl:when test="$start_datetime='' or $end_datetime=''">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_add"/></xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:fm.set_cart();fm.get_new_rsv('<xsl:value-of select="@id"/>','','','','<xsl:value-of select="$rsv_id"/>')</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_add"/></xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:fm.set_cart();fm.get_new_rsv('<xsl:value-of select="@id"/>','',<xsl:value-of select="$start_datetime"/>,<xsl:value-of select="$end_datetime"/>,'<xsl:value-of select="$rsv_id"/>')</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_remove"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:fm.cancel_rsv_fac_prep(document.frmAction, '<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$rsv_id"/>', Fsh,'<xsl:value-of select="@id"/>','<xsl:value-of select="//reservation/@main_fac_id"/>','<xsl:value-of select="count(facility_schedule[@status!='CANCELLED']/facility[@id=//reservation/@main_fac_id])"/>')</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="$mode='fs_edit'">
						<xsl:choose>
							<xsl:when test="$start_datetime='' or $end_datetime=''">
							<!--<input type="button" value="{$lab_add}" class="wbGenSubBtnFrm" onclick="fm.set_cart();fm.get_new_rsv({@id},'','','')" />-->
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_add"/></xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:fm.set_cart();fm.get_new_rsv('<xsl:value-of select="@id"/>','','','')</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_add"/></xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:fm.set_cart();fm.get_new_rsv('<xsl:value-of select="@id"/>')</xsl:with-param>
								</xsl:call-template>
							<!--<input type="button" value="{$lab_add}" class="wbGenSubBtnFrm" onclick="fm.set_cart();fm.get_new_rsv({@id})" />-->
							</xsl:otherwise>
						</xsl:choose>
						<!--
						<input type="button" value="{$lab_add}" class="wbGenSubBtnFrm" onclick="fm.get_new_rsv({@id})" />
						-->
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_remove"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:fm.cancel_fs_exec(document.frmAction,'<xsl:value-of select="$wb_lang"/>',Fsh,'<xsl:value-of select="@id"/>')</xsl:with-param>
						</xsl:call-template>
				
						<!--<input type="button" value="{$lab_remove}" class="wbGenSubBtnFrm" onclick="fm.cancel_fs_exec(frm,'{$wb_lang}',Fsh,{@id})"/>-->
					</xsl:when>
					</xsl:choose>
				</td>
			</tr>
		</table>
		<table class="table wzb-ui-table">
			<tr>
				<td width="27%" style="border-top:none;">
					<xsl:if test="$mode = 'edit' or $mode = 'fs_edit'">
						<xsl:call-template name="select_all_checkbox">
							<xsl:with-param name="chkbox_lst_cnt"><xsl:value-of select="count(facility_schedule/facility)"/></xsl:with-param>
							<xsl:with-param name="chkbox_lst_nm">fm_fac_id_lst_<xsl:value-of select="@id"/></xsl:with-param>
							<xsl:with-param name="display_icon">false</xsl:with-param>
							<xsl:with-param name="sel_all_chkbox_nm">sel_all_checkbox_<xsl:value-of select="@id"/></xsl:with-param>
							<xsl:with-param name="frm_name">frmAction</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					<xsl:call-template name="fm_display_title"/>
				</td>
				<td width="9%" style="border-top:none;">
					<xsl:value-of select="$lab_date"/>
				</td>
				<td width="16%" style="border-top:none;">
					<xsl:value-of select="$lab_time"/>
				</td>
				<td width="4%" style="border-top:none;">
					<xsl:value-of select="$lab_pencil_in"/>
				</td>
				<td style="border-top:none;">
					<xsl:attribute name="width"><xsl:choose><xsl:when test="$mode = 'edit'">35%</xsl:when><xsl:otherwise>40%</xsl:otherwise></xsl:choose></xsl:attribute>
					<xsl:if test="count(facility_schedule[@status = 'CANCELLED']) != 0">
					<span class="TitleText"><xsl:value-of select="$lab_cancel_reason"/></span>
					</xsl:if>				
				</td>
			</tr>
			
			<xsl:apply-templates select="facility_schedule" mode="rsv">
				<xsl:with-param name="lab_to"><xsl:value-of select="$lab_to"/></xsl:with-param>
				<xsl:with-param name="xsl_prefix"><xsl:value-of select="$xsl_prefix"/></xsl:with-param>
				<xsl:with-param name="mode"><xsl:value-of select="$mode"/></xsl:with-param>
				<xsl:with-param name="type_id"><xsl:value-of select="@id"/></xsl:with-param>
				<xsl:with-param name="prev_count"><xsl:value-of select="count(preceding-sibling::*/facility_schedule)"/></xsl:with-param>
				<xsl:with-param name="rsv_status"><xsl:value-of select="$rsv_status"/></xsl:with-param>
			</xsl:apply-templates>
		</table>
		</xsl:if>
	</xsl:template>
	
	
	<!-- =============================================================== -->
	<xsl:template match="facility_schedule" mode="rsv">
		<xsl:param name="lab_to"/>
		<xsl:param name="xsl_prefix"/>
		<xsl:param name="mode">read</xsl:param>
		<xsl:param name="type_id"/>
		<xsl:param name="prev_count"/>
		<xsl:param name="rsv_status"/>
		<xsl:param name="fac_details_edit">false</xsl:param>
		<xsl:variable name="_class">
		<xsl:choose>
			<xsl:when test="$rsv_status = 'CANCEL'">GreyText</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="@status = 'CANCELLED'">GreyText</xsl:when>
					<xsl:otherwise>Text</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
		</xsl:variable>
		<tr>
			<td class="RowsEven">
				<xsl:if test="$mode = 'edit' or  $mode = 'fs_edit'">
					<xsl:choose>
						<xsl:when test="@status = 'CANCELLED'"><IMG src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/></xsl:when>
						<xsl:otherwise><input type="checkbox" name="fm_fac_id_lst_{$type_id}" value="{$prev_count + position()}"/></xsl:otherwise>
					</xsl:choose>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="$fac_details_edit = 'true'">
						<a href="javascript:fm.get_facility_details({facility/@id},'{$xsl_prefix}')" class="{$_class}">
							<xsl:value-of select="facility/basic/title"/>
						</a>					
					</xsl:when>
					<xsl:otherwise>
						<a href="javascript:fm.get_facility_details_win({facility/@id},'{$xsl_prefix}')" class="{$_class}">
							<xsl:value-of select="facility/basic/title"/>
						</a>						
					</xsl:otherwise>
				</xsl:choose>			
			</td>
			<td class="RowsEven">
				<span class="{$_class}"><xsl:call-template name="display_time"><xsl:with-param name="my_timestamp"><xsl:value-of select="@date"/></xsl:with-param></xsl:call-template></span>
			</td>
			<td class="RowsEven">
				<span class="{$_class}"><xsl:call-template name="display_hhmm"><xsl:with-param name="my_timestamp"><xsl:value-of select="@start_time"/></xsl:with-param></xsl:call-template><xsl:value-of select="$lab_to"/><xsl:call-template name="display_hhmm"><xsl:with-param name="my_timestamp"><xsl:value-of select="@end_time"/></xsl:with-param></xsl:call-template></span>
			</td>
			<td class="RowsEven">
				<xsl:choose>
					<xsl:when test="$mode = 'edit' or $mode ='fs_edit'">
					<xsl:choose>
						<xsl:when test="@status = 'CANCELLED'"><IMG src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/></xsl:when>
						<xsl:otherwise>			
						<input type="checkbox" name="fsh_status_{facility/@id}" value="{$prev_count + position()}">
							<xsl:if test="@status = 'PENCILLED_IN'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						</xsl:otherwise>		
					</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="@status = 'CANCELLED'"><IMG src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/></xsl:when>
						<xsl:otherwise>			
						<xsl:choose>
							<xsl:when test="@status = 'PENCILLED_IN'">
								<IMG src="{$wb_img_path}fm_pencil_in.gif" align="absmiddle" border="0"/>
							</xsl:when>
							<xsl:otherwise>
								<IMG src="{$wb_img_path}tp.gif" align="absmiddle" width="1" height="1" border="0"/>
							</xsl:otherwise>
						</xsl:choose>
						</xsl:otherwise>	
					</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td>
				<xsl:if test="@status = 'CANCELLED'">
				<span class="{$_class}"><xsl:value-of select="cancel_user/@type"/></span>
				</xsl:if>
				<IMG src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/>
			</td>
		</tr>
	</xsl:template>
<xsl:template name="fm_head">
	<xsl:choose>
		<xsl:when test="starts-with(//cur_usr/role/@id, 'TADM_')">
			<xsl:call-template name="fm_hdr"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_FACILITY_BOOK_CREATE</xsl:with-param>
			</xsl:call-template>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>
</xsl:stylesheet>
