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
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:output  indent="yes"/>
	<xsl:variable name="main_fac_id" select="/fm/reservation/@main_fac_id"/>
	<xsl:variable name="main_room" select="/fm/reservation/facility_schedule_list/facility_type[@main = 'YES']/facility_schedule/facility[@id = $main_fac_id]/basic/title"/>
	<xsl:variable name="rsv_status" select="/fm/reservation/@status"/>
	<xsl:variable name="page_variant_root" select="/fm/meta/page_variant"/>
	<xsl:variable name="fac_total_cost" select="/fm/reservation/fac_total_cost"/>
	<xsl:strip-space elements="*"/>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_total_cost" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '737')"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<xsl:template match="*"/>
	<!-- =============================================================== -->
	<xsl:template match="fm">
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
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmAction">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove_rsv">取消預訂</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_event">活動</xsl:with-param>
			<xsl:with-param name="lab_rsv_info">預訂資料</xsl:with-param>
			<xsl:with-param name="lab_venue">地點</xsl:with-param>
			<xsl:with-param name="lab_rsv_for">預訂者</xsl:with-param>
			<xsl:with-param name="lab_main_room">主要房間</xsl:with-param>
			<xsl:with-param name="lab_no_of_participants">參加人數</xsl:with-param>
			<xsl:with-param name="lab_facilities_rsv">預訂的設施</xsl:with-param>
			<xsl:with-param name="lab_ref">以下是你已預訂的設施</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_time">時間</xsl:with-param>
			<xsl:with-param name="lab_pencil_in">暫定</xsl:with-param>
			<xsl:with-param name="lab_to"> - </xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_cancel">已取消</xsl:with-param>
			<xsl:with-param name="lab_cancel_type">取消類型</xsl:with-param>
			<xsl:with-param name="lab_cancel_reason">取消原因</xsl:with-param>
			<xsl:with-param name="lab_last_modified_by">最後一次修改者</xsl:with-param>
			<xsl:with-param name="lab_last_accessed">最後到訪者</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove_rsv">取消预订</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_event">活动</xsl:with-param>
			<xsl:with-param name="lab_rsv_info">预订信息</xsl:with-param>
			<xsl:with-param name="lab_venue">地点</xsl:with-param>
			<xsl:with-param name="lab_rsv_for">预订者</xsl:with-param>
			<xsl:with-param name="lab_main_room">主房间</xsl:with-param>
			<xsl:with-param name="lab_no_of_participants">参加人数</xsl:with-param>
			<xsl:with-param name="lab_facilities_rsv">预订的设施</xsl:with-param>
			<xsl:with-param name="lab_ref">以下是您已预订的设施。</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_time">时间</xsl:with-param>
			<xsl:with-param name="lab_pencil_in">暂定</xsl:with-param>
			<xsl:with-param name="lab_to"> - </xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_cancel">已取消</xsl:with-param>
			<xsl:with-param name="lab_cancel_type">取消类型</xsl:with-param>
			<xsl:with-param name="lab_cancel_reason">取消原因</xsl:with-param>
			<xsl:with-param name="lab_last_modified_by">最后一次修改者</xsl:with-param>
			<xsl:with-param name="lab_last_accessed">最后访问者</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">Back</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove_rsv">Remove reservation</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_event">Event</xsl:with-param>
			<xsl:with-param name="lab_rsv_info">Reservation info</xsl:with-param>
			<xsl:with-param name="lab_venue">Venue</xsl:with-param>
			<xsl:with-param name="lab_rsv_for">Reserved for</xsl:with-param>
			<xsl:with-param name="lab_main_room">Main room</xsl:with-param>
			<xsl:with-param name="lab_no_of_participants">No. of participants</xsl:with-param>
			<xsl:with-param name="lab_facilities_rsv">Facilities reserved</xsl:with-param>
			<xsl:with-param name="lab_ref">These are the facilities reserved. </xsl:with-param>
			<xsl:with-param name="lab_date">Date</xsl:with-param>
			<xsl:with-param name="lab_time">Time</xsl:with-param>
			<xsl:with-param name="lab_pencil_in">Pencil-in</xsl:with-param>
			<xsl:with-param name="lab_to"> - </xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_cancel">Cancelled</xsl:with-param>
			<xsl:with-param name="lab_cancel_type">Cancellation type</xsl:with-param>
			<xsl:with-param name="lab_cancel_reason">Cancellation reason</xsl:with-param>
			<xsl:with-param name="lab_last_modified_by">Last modified by</xsl:with-param>
			<xsl:with-param name="lab_last_accessed">Last accessed</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="reservation">
		<xsl:param name="lab_event"/>
		<xsl:param name="lab_rsv_info"/>
		<xsl:param name="lab_venue"/>
		<xsl:param name="lab_rsv_for"/>
		<xsl:param name="lab_main_room"/>
		<xsl:param name="lab_no_of_participants"/>
		<xsl:param name="lab_facilities_rsv"/>
		<xsl:param name="lab_ref"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_time"/>
		<xsl:param name="lab_pencil_in"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_cancel"/>
		<xsl:param name="lab_cancel_type"/>
		<xsl:param name="lab_cancel_reason"/>
		<xsl:param name="lab_last_modified_by"/>
		<xsl:param name="lab_last_accessed"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:param name="lab_g_txt_btn_remove_rsv"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_rsv_record"/>
		<xsl:call-template name="fm_head"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_rsv_info"/>
				<xsl:text> - </xsl:text>
				<xsl:value-of select="reservation_details/purpose"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:apply-templates>
			<xsl:with-param name="lab_event" select="$lab_event"/>
			<xsl:with-param name="lab_rsv_info" select="$lab_rsv_info"/>
			<xsl:with-param name="lab_venue" select="$lab_venue"/>
			<xsl:with-param name="lab_rsv_for" select="$lab_rsv_for"/>
			<xsl:with-param name="lab_main_room" select="$lab_main_room"/>
			<xsl:with-param name="lab_no_of_participants" select="$lab_no_of_participants"/>
			<xsl:with-param name="lab_last_modified_by" select="$lab_last_modified_by"/>
			<xsl:with-param name="lab_last_accessed" select="$lab_last_accessed"/>
			<xsl:with-param name="lab_facilities_rsv" select="$lab_facilities_rsv"/>
			<xsl:with-param name="lab_ref" select="$lab_ref"/>
			<xsl:with-param name="lab_date" select="$lab_date"/>
			<xsl:with-param name="lab_time" select="$lab_time"/>
			<xsl:with-param name="lab_pencil_in" select="$lab_pencil_in"/>
			<xsl:with-param name="lab_to" select="$lab_to"/>
			<xsl:with-param name="lab_status" select="$lab_status"/>
			<xsl:with-param name="lab_cancel" select="$lab_cancel"/>
			<xsl:with-param name="lab_cancel_type" select="$lab_cancel_type"/>
			<xsl:with-param name="lab_cancel_reason" select="$lab_cancel_reason"/>
			<xsl:with-param name="lab_g_form_btn_close" select="$lab_g_form_btn_close"/>
			<xsl:with-param name="lab_g_form_btn_back" select="$lab_g_form_btn_back"/>
			<xsl:with-param name="lab_g_txt_btn_remove_rsv" select="$lab_g_txt_btn_remove_rsv"/>
			<xsl:with-param name="lab_g_txt_btn_edit" select="$lab_g_txt_btn_edit"/>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="facility_schedule_list">
		<xsl:param name="lab_facilities_rsv"/>
		<xsl:param name="lab_ref"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_time"/>
		<xsl:param name="lab_pencil_in"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_cancel_reason"/>
		<xsl:param name="lab_last_modified_by"/>
		<xsl:param name="lab_last_accessed"/>
		<xsl:param name="lab_g_txt_btn_remove_rsv"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:if test="$rsv_status != 'CANCEL'">
			<table>
				<tr>
					<td align="right">
						<xsl:if test="$page_variant_root/@hasEditRsvBtn = 'true'">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_edit"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:fm.rsv_upd_prep(<xsl:value-of select="/fm/reservation/@id"/>)</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
							</xsl:call-template>
						</xsl:if>
						<xsl:if test="$page_variant_root/@hasDelRsvBtn = 'true'">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_remove_rsv"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:fm.cancel_rsv_prep(<xsl:value-of select="/fm/reservation/@id"/>,'<xsl:value-of select="/fm/reservation/reservation_details/update_user/@timestamp"/>')</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
							</xsl:call-template>
						</xsl:if>
					</td>
				</tr>
			</table>
		</xsl:if>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_facilities_rsv"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_ref"/>
		</xsl:call-template>
		<table>
			<tr>
				<td>
					<xsl:apply-templates mode="rsv">
						<xsl:with-param name="hasAddBtn">false</xsl:with-param>
						<xsl:with-param name="lab_ref" select="$lab_ref"/>
						<xsl:with-param name="lab_date" select="$lab_date"/>
						<xsl:with-param name="lab_time" select="$lab_time"/>
						<xsl:with-param name="lab_pencil_in" select="$lab_pencil_in"/>
						<xsl:with-param name="lab_to" select="$lab_to"/>
						<xsl:with-param name="rsv_status" select="$rsv_status"/>
						<xsl:with-param name="lab_cancel_reason" select="$lab_cancel_reason"/>
					</xsl:apply-templates>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="reservation_details">
		<xsl:param name="lab_event"/>
		<xsl:param name="lab_rsv_info"/>
		<xsl:param name="lab_venue"/>
		<xsl:param name="lab_rsv_for"/>
		<xsl:param name="lab_main_room"/>
		<xsl:param name="lab_no_of_participants"/>
		<xsl:param name="lab_last_modified_by"/>
		<xsl:param name="lab_last_accessed"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_cancel"/>
		<xsl:param name="lab_cancel_type"/>
		<xsl:param name="lab_cancel_reason"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:call-template name="wb_ui_space"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_rsv_info"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<xsl:if test="$rsv_status = 'CANCEL'">
				<tr>
					<td class="wzb-form-label">
						<xsl:value-of select="$lab_status"/>:
					</td>
					<td class="wzb-form-control">
						<xsl:value-of select="$lab_cancel"/>
					</td>
				</tr>
				<tr>
					<td class="wzb-form-label">
						<xsl:value-of select="$lab_cancel_type"/>:
					</td>
					<td class="wzb-form-control">
						<xsl:value-of select="cancel_user/@type"/>
					</td>
				</tr>
				<tr>
					<td class="wzb-form-label">
						<xsl:value-of select="$lab_cancel_reason"/>:
					</td>
					<td class="wzb-form-control">
						<xsl:value-of select="cancel_user/@reason"/>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_event"/>:
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="purpose"/>
				</td>
			</tr>
			<xsl:if test="$main_room != ''">
				<tr>
					<td class="wzb-form-label">
						<xsl:value-of select="$lab_main_room"/>:
					</td>
					<td class="wzb-form-control">
						<xsl:for-each select="/fm/reservation/facility_schedule_list/facility_type[@main = 'YES']/facility_schedule">
							<xsl:value-of select="facility/basic/title"/>
							<xsl:if test="position() != last()">, </xsl:if>
						</xsl:for-each>
						<!-- <xsl:value-of select="$main_room"/> -->
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_venue"/>:
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="unescape_html_linefeed">
						<xsl:with-param name="my_right_value">
							<xsl:value-of select="desc"/>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_rsv_for"/>:
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="reserve_user/@display_name"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_no_of_participants"/>:
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="participant_no"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_total_cost"/>:
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="$fac_total_cost"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_last_modified_by"/>:
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="update_user/@display_name"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_last_accessed"/>:
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="display_time">
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="update_user/@timestamp"/>
						</xsl:with-param>
						<xsl:with-param name="dis_time">T</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:choose>
				<xsl:when test="substring(//role/@id,0,4) = 'ADM'">
<!--  						<xsl:call-template name="wb_gen_form_button"> -->
<!-- 							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_back"/> -->
<!-- 							<xsl:with-param name="wb_gen_btn_href">javascript:history.go(-1)</xsl:with-param> -->
<!-- 						</xsl:call-template> -->
				</xsl:when>
				<xsl:otherwise>
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_close"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:parent.close()</xsl:with-param>
				</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
