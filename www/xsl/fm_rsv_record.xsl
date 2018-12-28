<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="share/fm_share.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/display_hhmm.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:output  indent="yes"/>
	<xsl:strip-space elements="*"/>
	<xsl:variable name="cur_usr_id" select="/fm/meta/cur_usr/@usr_id"/>
	<xsl:variable name="param_start_date" select="/fm/meta/param/@start_date"/>
	<xsl:variable name="param_end_date" select="/fm/meta/param/@end_date"/>
	<xsl:variable name="param_status" select="/fm/meta/param/@status"/>
	<xsl:variable name="param_fac_id" select="/fm/meta/param/@fac_id"/>
	<xsl:variable name="param_own_type" select="/fm/meta/param/@own_type"/>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_cost" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '805')"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_fm.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				var isExcludes = getUrlParam('isExcludes');
				fm = new wbFm(isExcludes);
			]]></script>
				<xsl:call-template name="new_css"/>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
				<form name="frmAction">
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="fm">
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_download">下載</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_new_rsv">新增預訂</xsl:with-param>
			<xsl:with-param name="lab_rsv_record">預訂記錄</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_time">時間</xsl:with-param>
			<xsl:with-param name="lab_facility">設施</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_event">活動</xsl:with-param>
			<xsl:with-param name="lab_rsv_for">預訂者</xsl:with-param>
			<xsl:with-param name="lab_no_of_participants">參加人數</xsl:with-param>
			<xsl:with-param name="lab_to"> - </xsl:with-param>
			<xsl:with-param name="lab_pencilled_in">暫定</xsl:with-param>
			<xsl:with-param name="lab_reserved">已預訂</xsl:with-param>
			<xsl:with-param name="lab_cancelled">已取消</xsl:with-param>
			<xsl:with-param name="lab_unknow">未知</xsl:with-param>
			<xsl:with-param name="lab_details">詳細資料</xsl:with-param>
			<xsl:with-param name="lab_remove_facility">刪除設施</xsl:with-param>
			<xsl:with-param name="lab_to_text">到</xsl:with-param>
			<xsl:with-param name="lab_rsv_from">預訂從</xsl:with-param>
			<xsl:with-param name="lab_no_item">找不到預訂記錄</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="fm">
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_download">下载</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_new_rsv">新增预订</xsl:with-param>
			<xsl:with-param name="lab_rsv_record">预订记录</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_time">时间</xsl:with-param>
			<xsl:with-param name="lab_facility">设施</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_event">活动</xsl:with-param>
			<xsl:with-param name="lab_rsv_for">预订者</xsl:with-param>
			<xsl:with-param name="lab_no_of_participants">参加人数</xsl:with-param>
			<xsl:with-param name="lab_to"> - </xsl:with-param>
			<xsl:with-param name="lab_pencilled_in">暂定</xsl:with-param>
			<xsl:with-param name="lab_reserved">预订</xsl:with-param>
			<xsl:with-param name="lab_cancelled">取消</xsl:with-param>
			<xsl:with-param name="lab_unknow">未知</xsl:with-param>
			<xsl:with-param name="lab_details">详细资料</xsl:with-param>
			<xsl:with-param name="lab_remove_facility">删除设施</xsl:with-param>
			<xsl:with-param name="lab_to_text">到</xsl:with-param>
			<xsl:with-param name="lab_rsv_from">预订从</xsl:with-param>
			<xsl:with-param name="lab_no_item">没有找到预订记录</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="fm">
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">Back</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_download">Download</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_new_rsv">New reservation</xsl:with-param>
			<xsl:with-param name="lab_rsv_record">Reservation record</xsl:with-param>
			<xsl:with-param name="lab_date">Date</xsl:with-param>
			<xsl:with-param name="lab_time">Time</xsl:with-param>
			<xsl:with-param name="lab_facility">Facility</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_event">Event</xsl:with-param>
			<xsl:with-param name="lab_rsv_for">Reserved for</xsl:with-param>
			<xsl:with-param name="lab_no_of_participants">No. of participants</xsl:with-param>
			<xsl:with-param name="lab_to"> - </xsl:with-param>
			<xsl:with-param name="lab_pencilled_in">Pencilled-in</xsl:with-param>
			<xsl:with-param name="lab_reserved">Reserved</xsl:with-param>
			<xsl:with-param name="lab_cancelled">Cancelled</xsl:with-param>
			<xsl:with-param name="lab_unknow">Unknow</xsl:with-param>
			<xsl:with-param name="lab_details">Details</xsl:with-param>
			<xsl:with-param name="lab_remove_facility">Remove facility</xsl:with-param>
			<xsl:with-param name="lab_to_text"> to </xsl:with-param>
			<xsl:with-param name="lab_rsv_from">Reservation from </xsl:with-param>
			<xsl:with-param name="lab_no_item">No reservation record found</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="fm">
		<xsl:param name="lab_rsv_record"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_time"/>
		<xsl:param name="lab_facility"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_event"/>
		<xsl:param name="lab_rsv_for"/>
		<xsl:param name="lab_no_of_participants"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_pencilled_in"/>
		<xsl:param name="lab_reserved"/>
		<xsl:param name="lab_cancelled"/>
		<xsl:param name="lab_unknow"/>
		<xsl:param name="lab_details"/>
		<xsl:param name="lab_remove_facility"/>
		<xsl:param name="lab_to_text"/>
		<xsl:param name="lab_rsv_from"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:param name="lab_g_txt_btn_download"/>
		<xsl:param name="lab_g_txt_btn_new_rsv"/>
		<xsl:call-template name="fm_head"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_rsv_record"/>
		</xsl:call-template>
		<xsl:apply-templates select="reservation_record">
			<xsl:with-param name="lab_date" select="$lab_date"/>
			<xsl:with-param name="lab_time" select="$lab_time"/>
			<xsl:with-param name="lab_facility" select="$lab_facility"/>
			<xsl:with-param name="lab_status" select="$lab_status"/>
			<xsl:with-param name="lab_event" select="$lab_event"/>
			<xsl:with-param name="lab_rsv_for" select="$lab_rsv_for"/>
			<xsl:with-param name="lab_no_of_participants" select="$lab_no_of_participants"/>
			<xsl:with-param name="lab_to" select="$lab_to"/>
			<xsl:with-param name="lab_reserved" select="$lab_reserved"/>
			<xsl:with-param name="lab_pencilled_in" select="$lab_pencilled_in"/>
			<xsl:with-param name="lab_cancelled" select="$lab_cancelled"/>
			<xsl:with-param name="lab_unknow" select="$lab_unknow"/>
			<xsl:with-param name="lab_details" select="$lab_details"/>
			<xsl:with-param name="lab_remove_facility" select="$lab_remove_facility"/>
			<xsl:with-param name="lab_to_text" select="$lab_to_text"/>
			<xsl:with-param name="lab_rsv_from" select="$lab_rsv_from"/>
			<xsl:with-param name="lab_no_item" select="$lab_no_item"/>
			<xsl:with-param name="lab_na" select="$lab_na"/>
			<xsl:with-param name="lab_g_form_btn_close" select="$lab_g_form_btn_close"/>
			<xsl:with-param name="lab_g_form_btn_back" select="$lab_g_form_btn_back"/>
			<xsl:with-param name="lab_g_txt_btn_download" select="$lab_g_txt_btn_download"/>
			<xsl:with-param name="lab_g_txt_btn_new_rsv" select="$lab_g_txt_btn_new_rsv"/>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="reservation_record">
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_time"/>
		<xsl:param name="lab_facility"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_event"/>
		<xsl:param name="lab_rsv_for"/>
		<xsl:param name="lab_no_of_participants"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_pencilled_in"/>
		<xsl:param name="lab_cancelled"/>
		<xsl:param name="lab_unknow"/>
		<xsl:param name="lab_reserved"/>
		<xsl:param name="lab_details"/>
		<xsl:param name="lab_remove_facility"/>
		<xsl:param name="lab_to_text"/>
		<xsl:param name="lab_rsv_from"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:param name="lab_g_txt_btn_download"/>
		<xsl:param name="lab_g_txt_btn_new_rsv"/>
		<table>
			<tr>
				<td>
					<xsl:value-of select="$lab_rsv_from"/>
					<xsl:call-template name="display_time">
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="date_range/@from"/>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:value-of select="$lab_to_text"/>
					<xsl:call-template name="display_time">
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="date_range/@to"/>
						</xsl:with-param>
					</xsl:call-template>
				</td>
				<td align="right">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_new_rsv"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:fm.clear_rsv_id();fm.get_new_rsv();</xsl:with-param>
					</xsl:call-template>
					<xsl:if test="count(facility_schedule_list/facility_schedule) != 0">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_download"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:fm.get_rsv_record_csv('<xsl:value-of select="$param_start_date"/>','<xsl:value-of select="$param_end_date"/>','<xsl:value-of select="$param_fac_id"/>','<xsl:value-of select="$param_status"/>','<xsl:value-of select="$param_own_type"/>');</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
				</td>
			</tr>
		</table>
		<xsl:choose>
			<xsl:when test="count(facility_schedule_list/facility_schedule) = 0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_item"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td width="10%" align="center">
							<xsl:value-of select="$lab_date"/>
						</td>
						<td width="15%">
							<xsl:value-of select="$lab_time"/>
						</td>
						<td width="10%">
							<xsl:value-of select="$lab_facility"/>
						</td>
						<td width="10%">
							<xsl:value-of select="$lab_status"/>
						</td>
						<td width="10%">
							<xsl:value-of select="$lab_event"/>
						</td>
						<td width="10%">
							<xsl:value-of select="$lab_rsv_for"/>
						</td>
						<td width="10%" align="center">
							<xsl:value-of select="$lab_no_of_participants"/>
						</td>
						<td width="10%" align="center">
							<xsl:value-of select="$lab_cost"/>
						</td>
						<td width="15%" align="right"></td>
					</tr>
					<xsl:for-each select="facility_schedule_list/facility_schedule">
						<tr>
							<td align="center" style="border-bottom:0px;">
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="@date"/>
									</xsl:with-param>
								</xsl:call-template>
							</td>
							<td style="border-bottom:0px;">
								<xsl:call-template name="display_hhmm">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="@start_time"/>
									</xsl:with-param>
								</xsl:call-template>
								<xsl:value-of select="$lab_to"/>
								<xsl:call-template name="display_hhmm">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="@end_time"/>
									</xsl:with-param>
								</xsl:call-template>
							</td>
							<td style="border-bottom:0px;">
								<xsl:value-of select="facility/basic/title"/>
							</td>
							<td style="border-bottom:0px;">
								<xsl:choose>
									<xsl:when test="@status = 'PENCILLED_IN'">
										<xsl:value-of select="$lab_pencilled_in"/>
									</xsl:when>
									<xsl:when test="@status = 'RESERVED'">
										<xsl:value-of select="$lab_reserved"/>
									</xsl:when>
									<xsl:when test="@status = 'CANCELLED'">
										<xsl:value-of select="$lab_cancelled"/> - <xsl:value-of select="cancel_user/@type"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_unknow"/>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td style="border-bottom:0px;">
								<xsl:choose>
									<xsl:when test="reservation">
										<xsl:value-of select="reservation/reservation_details/purpose"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_na"/>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td style="border-bottom:0px;">
								<xsl:choose>
									<xsl:when test="reservation">
										<xsl:value-of select="reservation/reservation_details/reserve_user/@display_name"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_na"/>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td align="center" style="border-bottom:0px;">
								<xsl:choose>
									<xsl:when test="reservation">
										<xsl:value-of select="reservation/reservation_details/participant_no"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_na"/>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td align="center" style="border-bottom:0px;">
								<xsl:choose>
									<xsl:when test="facility/basic/fac_fee > 0">
										<xsl:value-of select="facility/basic/fac_fee"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_na"/>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<xsl:variable name="rsv_title">
								<xsl:call-template name="escape_js">
									<xsl:with-param name="input_str" select="reservation/reservation_details/purpose"/>
								</xsl:call-template>
							</xsl:variable>
							<td align="right" style="border-bottom:0px;">
								<xsl:choose>
									<xsl:when test="reservation">
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="wb_gen_btn_name" select="$lab_details"/>
											<xsl:with-param name="wb_gen_btn_href">javascript:fm.get_rsv_details('<xsl:value-of select="reservation/@id"/>', '<xsl:value-of select="$rsv_title"/>')</xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
									</xsl:otherwise>
								</xsl:choose>
							</td>
						</tr>
					</xsl:for-each>
				</table>
			</xsl:otherwise>
		</xsl:choose>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_back"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
