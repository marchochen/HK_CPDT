<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="share/fm_share.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output  indent="yes"/>
	<xsl:strip-space elements="*"/>
	<!-- =================================================================	-->
	<xsl:template match="/">
		<xsl:apply-templates select="fm"/>
	</xsl:template>
	<!-- =================================================================	-->
	<xsl:template match="fm">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_fm.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
						var isExcludes = getUrlParam('isExcludes');
						fm = new wbFm(isExcludes);
					]]></script>
				<xsl:call-template name="new_css"/>
				<style>
					.CalCellAvailBg{
						background-color : #EEEEEE;
					}
					
					.CalCellPartlyRsvBg{
						background-color : #888888;
					}
					
					.CalCellAlmostFullBg{
						background : #444444;
					}
					
					.CalCellFullRsvBg{
						background : Red;
					}
				</style>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
				<form name="frmAction">
					<input type="hidden" name="cmd"/>
					<input type="hidden" name="module"/>
					<input type="hidden" name="fac_id"/>
					<input type="hidden" name="start_date"/>
					<input type="hidden" name="end_date"/>
					<input type="hidden" name="status"/>
					<input type="hidden" name="own_type"/>
					<input type="hidden" name="url_failure"/>
					<input type="hidden" name="stylesheet"/>
					<input type="hidden" name="ext_fac_id"/>
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="calendar">
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_mon">星期一</xsl:with-param>
			<xsl:with-param name="lab_tue">星期二</xsl:with-param>
			<xsl:with-param name="lab_wed">星期三</xsl:with-param>
			<xsl:with-param name="lab_thr">星期四</xsl:with-param>
			<xsl:with-param name="lab_fri">星期五</xsl:with-param>
			<xsl:with-param name="lab_sat">星期六</xsl:with-param>
			<xsl:with-param name="lab_sun">星期日</xsl:with-param>
			<xsl:with-param name="lab_to">-</xsl:with-param>
			<xsl:with-param name="lab_legend">圖例:</xsl:with-param>
			<xsl:with-param name="lab_avail">可用</xsl:with-param>
			<xsl:with-param name="lab_party_rsv">部分預訂</xsl:with-param>
			<xsl:with-param name="lab_almost_full">幾乎訂滿</xsl:with-param>
			<xsl:with-param name="lab_fully_rsv">公眾假期</xsl:with-param>
			<xsl:with-param name="lab_rsv_cal">預定日曆</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="calendar">
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_mon">星期一</xsl:with-param>
			<xsl:with-param name="lab_tue">星期二</xsl:with-param>
			<xsl:with-param name="lab_wed">星期三</xsl:with-param>
			<xsl:with-param name="lab_thr">星期四</xsl:with-param>
			<xsl:with-param name="lab_fri">星期五</xsl:with-param>
			<xsl:with-param name="lab_sat">星期六</xsl:with-param>
			<xsl:with-param name="lab_sun">星期日</xsl:with-param>
			<xsl:with-param name="lab_to">-</xsl:with-param>
			<xsl:with-param name="lab_legend">图例：</xsl:with-param>
			<xsl:with-param name="lab_avail">可用</xsl:with-param>
			<xsl:with-param name="lab_party_rsv">部分预订</xsl:with-param>
			<xsl:with-param name="lab_almost_full">全部订满</xsl:with-param>
			<xsl:with-param name="lab_fully_rsv">公众假期</xsl:with-param>
			<xsl:with-param name="lab_rsv_cal">预定日历</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="calendar">
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">Back</xsl:with-param>
			<xsl:with-param name="lab_mon">Mon</xsl:with-param>
			<xsl:with-param name="lab_tue">Tue</xsl:with-param>
			<xsl:with-param name="lab_wed">Wed</xsl:with-param>
			<xsl:with-param name="lab_thr">Thr</xsl:with-param>
			<xsl:with-param name="lab_fri">Fri</xsl:with-param>
			<xsl:with-param name="lab_sat">Sat</xsl:with-param>
			<xsl:with-param name="lab_sun">Sun</xsl:with-param>
			<xsl:with-param name="lab_to">-</xsl:with-param>
			<xsl:with-param name="lab_legend">Legend:</xsl:with-param>
			<xsl:with-param name="lab_avail">Available</xsl:with-param>
			<xsl:with-param name="lab_party_rsv">Partly reserved</xsl:with-param>
			<xsl:with-param name="lab_almost_full">Fully reserved</xsl:with-param>
			<xsl:with-param name="lab_fully_rsv">Public holiday</xsl:with-param>
			<xsl:with-param name="lab_rsv_cal">Reservation calendar</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="calendar">
		<xsl:param name="lab_mon"/>
		<xsl:param name="lab_tue"/>
		<xsl:param name="lab_wed"/>
		<xsl:param name="lab_thr"/>
		<xsl:param name="lab_fri"/>
		<xsl:param name="lab_sat"/>
		<xsl:param name="lab_sun"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_legend"/>
		<xsl:param name="lab_avail"/>
		<xsl:param name="lab_party_rsv"/>
		<xsl:param name="lab_almost_full"/>
		<xsl:param name="lab_fully_rsv"/>
		<xsl:param name="lab_rsv_cal"/>
		<xsl:param name="legend_width">12</xsl:param>
		<xsl:param name="legend_height">12</xsl:param>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:call-template name="fm_head"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_rsv_calendar"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_rsv_cal"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
			<tr>
				<td>
					<xsl:variable name="fac_id_lst">
						<xsl:for-each select="facility_usage_list[1]/facility_availability/facility">
							<xsl:value-of select="@id"/>
							<xsl:if test="position() != last()">~</xsl:if>
						</xsl:for-each>
					</xsl:variable>
					<xsl:call-template name="wb_ui_desc">
						<xsl:with-param name="text">
							&#160;<a class="Text" href="javascript:fm.get_rsv_calendar_prev(document.frmAction,'{$fac_id_lst}','{date_range/@last_week}')">
								<IMG hspace="2" src="{$wb_img_path}fm_prev_week.gif" align="absmiddle" border="0"/>
							</a>
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="date_range/@from"/>
								</xsl:with-param>
							</xsl:call-template>&#160;<xsl:value-of select="$lab_to"/>&#160;<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="date_range/@to"/>
								</xsl:with-param>
							</xsl:call-template>
							<a class="Text" href="javascript:fm.get_rsv_calendar_next(document.frmAction,'{$fac_id_lst}','{date_range/@next_week}')">
								<IMG src="{$wb_img_path}fm_next_week.gif" hspace="2" align="absmiddle" border="0"/>
							</a>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		<!--		
		<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
			<tr>
				<td class="wbFmHeadRefBg">
					<xsl:variable name="fac_id_lst">
						<xsl:for-each select="facility_usage_list[1]/facility_availability/facility"><xsl:value-of select="@id"/><xsl:if test="position() != last()">~</xsl:if></xsl:for-each>
					</xsl:variable>
					<span class="Text"></span>
				</td>
			</tr>
		</table>
-->
		<table>
			<tr>
				<td>
					<xsl:apply-templates mode="cal">
						<xsl:with-param name="lab_mon">
							<xsl:value-of select="$lab_mon"/>
						</xsl:with-param>
						<xsl:with-param name="lab_tue">
							<xsl:value-of select="$lab_tue"/>
						</xsl:with-param>
						<xsl:with-param name="lab_wed">
							<xsl:value-of select="$lab_wed"/>
						</xsl:with-param>
						<xsl:with-param name="lab_thr">
							<xsl:value-of select="$lab_thr"/>
						</xsl:with-param>
						<xsl:with-param name="lab_fri">
							<xsl:value-of select="$lab_fri"/>
						</xsl:with-param>
						<xsl:with-param name="lab_sat">
							<xsl:value-of select="$lab_sat"/>
						</xsl:with-param>
						<xsl:with-param name="lab_sun">
							<xsl:value-of select="$lab_sun"/>
						</xsl:with-param>
						<xsl:with-param name="lab_to">
							<xsl:value-of select="$lab_to"/>
						</xsl:with-param>
					</xsl:apply-templates>
				</td>
				<td valign="top">
					<table>
						<tr>
							<td colspan="2">
								<xsl:value-of select="$lab_legend"/>
							</td>
						</tr>
						<tr>
							<td align="center" valign="middle">
								<table>
									<tr>
										<td class="CalCellAvailBg">
											<IMG src="{$wb_img_path}tp.gif" width="{$legend_width}" height="{$legend_height}" border="0"/>
										</td>
									</tr>
								</table>
							</td>
							<td>
								<span class="Text">
									<xsl:value-of select="$lab_avail"/>
								</span>
							</td>
						</tr>
						<tr>
							<td align="center" valign="middle">
								<table>
									<tr>
										<td class="CalCellPartlyRsvBg">
											<IMG src="{$wb_img_path}tp.gif" width="{$legend_width}" height="{$legend_height}" border="0"/>
										</td>
									</tr>
								</table>
							</td>
							<td>
								<xsl:value-of select="$lab_party_rsv"/>
							</td>
						</tr>
						<tr>
							<td align="center" valign="middle">
								<table>
									<tr>
										<td class="CalCellAlmostFullBg">
											<IMG src="{$wb_img_path}tp.gif" width="{$legend_width}" height="{$legend_height}" border="0"/>
										</td>
									</tr>
								</table>
							</td>
							<td>
								<xsl:value-of select="$lab_almost_full"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_back"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="facility_usage_list" mode="cal">
		<xsl:param name="lab_mon"/>
		<xsl:param name="lab_tue"/>
		<xsl:param name="lab_wed"/>
		<xsl:param name="lab_thr"/>
		<xsl:param name="lab_fri"/>
		<xsl:param name="lab_sat"/>
		<xsl:param name="lab_sun"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="cell_height">15</xsl:param>
		<xsl:param name="width">500</xsl:param>
		<xsl:if test="week">
			<table border="0" cellpadding="3" cellspacing="1" width="{$width}" class="SecBg">
				<tr>
					<td>
						<span class="TitleText">
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="week/day[1]"/>
								</xsl:with-param>
							</xsl:call-template>&#160;<xsl:value-of select="$lab_to"/>&#160;<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="week/day[last()]"/>
								</xsl:with-param>
							</xsl:call-template>
						</span>
					</td>
				</tr>
			</table>
			<table border="0" cellpadding="0" cellspacing="1" width="{$width}" class="RowsEven">
				<tr>
					<td class="SecBg" width="{floor($width div (count(week/day)+1))}" align="center">
						<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
					<xsl:for-each select="week/day">
						<td width="{floor($width div (last()+1))}">
							<xsl:attribute name="class"><xsl:choose><xsl:when test="@today = 'YES'">RowsEven</xsl:when><xsl:otherwise>SecBg</xsl:otherwise></xsl:choose></xsl:attribute>
							<table cellpadding="3" cellspacing="0" border="0">
								<tr>
									<td>
										<span>
											<xsl:attribute name="class"><xsl:choose><xsl:when test="@today = 'YES'">wbFmCalColTodayDateText</xsl:when><xsl:otherwise>wbFmCalColDateText</xsl:otherwise></xsl:choose></xsl:attribute>
											<xsl:choose>
												<xsl:when test="position() = 1">
													<xsl:value-of select="$lab_sun"/>
												</xsl:when>
												<xsl:when test="position() = 2">
													<xsl:value-of select="$lab_mon"/>
												</xsl:when>
												<xsl:when test="position() = 3">
													<xsl:value-of select="$lab_tue"/>
												</xsl:when>
												<xsl:when test="position() = 4">
													<xsl:value-of select="$lab_wed"/>
												</xsl:when>
												<xsl:when test="position() = 5">
													<xsl:value-of select="$lab_thr"/>
												</xsl:when>
												<xsl:when test="position() = 6">
													<xsl:value-of select="$lab_fri"/>
												</xsl:when>
												<xsl:when test="position() = 7">
													<xsl:value-of select="$lab_sat"/>
												</xsl:when>
											</xsl:choose>&#160;
										<xsl:value-of select="number(substring-after(substring-after(substring-before(.,' '),'-'),'-'))"/>
										</span>
									</td>
								</tr>
							</table>
						</td>
					</xsl:for-each>
				</tr>
				<xsl:apply-templates select="facility_availability" mode="cal">
					<xsl:with-param name="cell_height">
						<xsl:value-of select="$cell_height"/>
					</xsl:with-param>
					<xsl:with-param name="cell_width">
						<xsl:value-of select="floor($width div (count(week/day)+1))"/>
					</xsl:with-param>
				</xsl:apply-templates>
			</table>
			<br/>
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="facility_availability" mode="cal">
		<xsl:param name="cell_height"/>
		<xsl:param name="cell_width"/>
		<tr>
			<xsl:apply-templates mode="cal">
				<xsl:with-param name="cell_height">
					<xsl:value-of select="$cell_height"/>
				</xsl:with-param>
				<xsl:with-param name="cell_width">
					<xsl:value-of select="$cell_width"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="facility" mode="cal">
		<td class="SecBg" align="center" valign="middle">
			<span class="TitleText">
				<xsl:value-of select="@title"/>
			</span>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="day_usage " mode="cal">
		<xsl:param name="cell_height"/>
		<xsl:param name="cell_width"/>
		<xsl:variable name="my_position">
			<xsl:value-of select="position() -1"/>
		</xsl:variable>
		<xsl:variable name="isPublicHoliday">
			<xsl:choose>
				<xsl:when test="count(@*[contains(name(),'slot') and .='FULL'])=count(@*[contains(name(),'slot')])">true</xsl:when>
				<xsl:otherwise>false</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<td valign="top">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<xsl:apply-templates select="@*[contains(name(),'slot') = 'true']" mode="cell">
					<xsl:with-param name="cell_height">
						<xsl:value-of select="$cell_height"/>
					</xsl:with-param>
					<xsl:with-param name="cell_width">
						<xsl:value-of select="$cell_width"/>
					</xsl:with-param>
					<xsl:with-param name="fac_id">
						<xsl:value-of select="../facility/@id"/>
					</xsl:with-param>
					<xsl:with-param name="week_day">
						<xsl:value-of select="../../week/day[position() = $my_position]"/>
					</xsl:with-param>
					<xsl:with-param name="isPublicHoliday">
						<xsl:value-of select="$isPublicHoliday"/>
					</xsl:with-param>
				</xsl:apply-templates>
			</table>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@*" mode="cell">
		<xsl:param name="cell_height"/>
		<xsl:param name="cell_width"/>
		<xsl:param name="fac_id"/>
		<xsl:param name="week_day"/>
		<xsl:param name="isPublicHoliday"/>
		<xsl:variable name="start_time">
			<xsl:choose>
				<xsl:when test="name()='slot_1'">08:30:00.0</xsl:when>
				<xsl:when test="name()='slot_2'">13:30:00.0</xsl:when>
				<xsl:when test="name()='slot_3'">17:30:00.0</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="end_time">
			<xsl:choose>
				<xsl:when test="name()='slot_1'">12:30:00.0</xsl:when>
				<xsl:when test="name()='slot_2'">17:30:00.0</xsl:when>
				<xsl:when test="name()='slot_3'">22:00:00.0</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
		</xsl:variable>
		<tr>
			<td height="{$cell_height}" valign="top">
				<xsl:choose>
					<xsl:when test=". = 'AVAILABLE'">
						<xsl:attribute name="class">CalCellAvailBg</xsl:attribute>
						<a href="javascript:wb_utils_fm_set_cookie('cur_rsv_id',wb_utils_fm_get_cookie('work_rsv_id'));fm.get_new_rsv('',{$fac_id},'{concat(substring-before($week_day,' '),' ',$start_time)}','{concat(substring-before($week_day,' '),' ',$end_time)}');">
							<IMG src="{$wb_img_path}tp.gif" width="{$cell_width}" height="{$cell_height}" border="0"/>
						</a>
					</xsl:when>
					<xsl:when test=". = 'PART'">
						<xsl:attribute name="class">CalCellPartlyRsvBg</xsl:attribute>
						<a href="javascript:fm.get_rsv_record_srh_exec(document.frmAction,'{$wb_lang}',{$fac_id},'{$week_day}','{substring-before($week_day,' ')} {$start_time}');">
							<IMG src="{$wb_img_path}tp.gif" width="{$cell_width}" height="{$cell_height}" border="0"/>
						</a>
					</xsl:when>
					<xsl:when test=". = 'ALMOSTFULL' or .='FULL'">
						<xsl:attribute name="class">CalCellAlmostFullBg</xsl:attribute>
						<a href="javascript:fm.get_rsv_record_srh_exec(document.frmAction,'{$wb_lang}',{$fac_id},'{$week_day}','{substring-before($week_day,' ')} {$start_time}');">
							<IMG src="{$wb_img_path}tp.gif" width="{$cell_width}" height="{$cell_height}" border="0"/>
						</a>
					</xsl:when>
					<!--
					<xsl:when test=". = 'ALMOSTFULL' or ($isPublicHoliday='false' and .='FULL')">
						<xsl:attribute name="class">CalCellAlmostFullBg</xsl:attribute>
						<a href="javascript:fm.get_rsv_record_srh_exec(document.frmAction,'{$wb_lang}',{$fac_id},'{$week_day}','{substring-before($week_day,' ')} {$start_time}');">
							<IMG src="{$wb_img_path}fm_almostl_bg.gif" width="{$cell_width}" height="{$cell_height}" border="0"/>
						</a>						
					</xsl:when>
					<xsl:when test=".='FULL' and $isPublicHoliday='true'">
						<xsl:attribute name="class">CalCellFullRsvBg</xsl:attribute>
						<a href="javascript:fm.get_rsv_record_srh_exec(document.frmAction,'{$wb_lang}',{$fac_id},'{$week_day}','{substring-before($week_day,' ')} {$start_time}');">
							<IMG src="{$wb_img_path}fm_full_bg.gif" width="{$cell_width}" height="{$cell_height}" border="0"/>
						</a>							
					</xsl:when>
					-->
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
