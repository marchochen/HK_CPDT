<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>	
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="share/fm_share.xsl"/>
	<xsl:output  indent="yes"/>
	<xsl:strip-space elements="*"/>
	<xsl:variable name="cur_type_id" select="/fm/facility_list/@cur_type_id"/>
	<!-- =========================================================== -->
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
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				var isExcludes = getUrlParam('isExcludes');
				fm = new wbFm(isExcludes);
				
				function FshItem(){
					var args = FshItem.arguments
					this.fsh_fac_id = args[0]
					this.fsh_date = args[1]
					this.fsh_start_time = args[2]
					this.fsh_end_time =args[3]
					this.fsh_checkbox_id = args[4]
				}

				Fsh = new Array
				
				]]><xsl:for-each select="fm/facility_availability/facility_schedule_list/facility_schedule">Fsh[Fsh.length] = new FshItem('<xsl:value-of select="facility/@id"/>','<xsl:value-of select="@date"/>','<xsl:value-of select="@start_time"/>','<xsl:value-of select="@end_time"/>',<xsl:value-of select="position()"/>)
				</xsl:for-each><![CDATA[					
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
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_confirm">確認</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_new_reservation">新增預訂</xsl:with-param>
			<xsl:with-param name="lab_rsv_facilities">預訂設施</xsl:with-param>
			<xsl:with-param name="lab_facility_avail">設施可用情況</xsl:with-param>
			<xsl:with-param name="lab_facility">設施</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_time">時間</xsl:with-param>
			<xsl:with-param name="lab_conflict">衝突</xsl:with-param>
			<xsl:with-param name="lab_to">到</xsl:with-param>
			<xsl:with-param name="lab_commar">, </xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_by">由</xsl:with-param>
			<xsl:with-param name="lab_no_of_participant">參加人數:</xsl:with-param>
			<xsl:with-param name="lab_no_record">沒有預訂記錄</xsl:with-param>
			<xsl:with-param name="lab_reserved_by_other">設施正被其他用戶預訂</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_confirm">确认</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_new_reservation">新增预订</xsl:with-param>
			<xsl:with-param name="lab_rsv_facilities">预订设施</xsl:with-param>
			<xsl:with-param name="lab_facility_avail">设施可用情况</xsl:with-param>
			<xsl:with-param name="lab_facility">设施</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_time">时间</xsl:with-param>
			<xsl:with-param name="lab_conflict">冲突</xsl:with-param>
			<xsl:with-param name="lab_to">到</xsl:with-param>
			<xsl:with-param name="lab_commar">，</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_by">由</xsl:with-param>
			<xsl:with-param name="lab_no_of_participant">参加人数：</xsl:with-param>
			<xsl:with-param name="lab_no_record">没有预订记录</xsl:with-param>
			<xsl:with-param name="lab_reserved_by_other">设施正被其他用户预订</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_confirm">Confirm</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_new_reservation">New reservation</xsl:with-param>
			<xsl:with-param name="lab_rsv_facilities">Reserve facilities </xsl:with-param>
			<xsl:with-param name="lab_facility_avail">Facility availability</xsl:with-param>
			<xsl:with-param name="lab_facility">Facility</xsl:with-param>
			<xsl:with-param name="lab_date">Date</xsl:with-param>
			<xsl:with-param name="lab_time">Time</xsl:with-param>
			<xsl:with-param name="lab_conflict">Conflict</xsl:with-param>
			<xsl:with-param name="lab_to">to</xsl:with-param>
			<xsl:with-param name="lab_commar">, </xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_by"> by </xsl:with-param>
			<xsl:with-param name="lab_no_of_participant">No of participants: </xsl:with-param>
			<xsl:with-param name="lab_no_record">No record</xsl:with-param>
			<xsl:with-param name="lab_reserved_by_other">Facility is being reserved by another new reservation</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="fm">
		<xsl:param name="lab_new_reservation"/>
		<xsl:param name="lab_rsv_facilities"/>
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
		<xsl:param name="lab_no_record"/>
		<xsl:param name="lab_reserved_by_other"/>
		<xsl:param name="lab_g_form_btn_confirm"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:call-template name="fm_head"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_new_reservation"/> - <xsl:value-of select="$lab_rsv_facilities"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_facility_avail"/>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="count(facility_availability/facility_schedule_list/facility_schedule) != 0">
				<xsl:apply-templates select="facility_availability" mode="fac_avail">
					<xsl:with-param name="lab_facility_avail" select="$lab_facility_avail"/>
					<xsl:with-param name="lab_facility" select="$lab_facility"/>
					<xsl:with-param name="lab_date" select="$lab_date"/>
					<xsl:with-param name="lab_time" select="$lab_time"/>
					<xsl:with-param name="lab_conflict" select="$lab_conflict"/>
					<xsl:with-param name="lab_to" select="$lab_to"/>
					<xsl:with-param name="lab_by" select="$lab_by"/>
					<xsl:with-param name="lab_na" select="$lab_na"/>
					<xsl:with-param name="lab_commar" select="$lab_commar"/>
					<xsl:with-param name="lab_no_of_participant" select="$lab_no_of_participant"/>
					<xsl:with-param name="lab_reserved_by_other" select="$lab_reserved_by_other"/>
					<xsl:with-param name="default_checked">true</xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_no_record"/>
					</xsl:with-param>
					<xsl:with-param name="top_line">false</xsl:with-param>
					<xsl:with-param name="bottom_line">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<div class="wzb-bar">
			<xsl:choose>
				<xsl:when test="count(facility_availability/facility_schedule_list/facility_schedule[not(conflict_list)]) = 0">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_confirm"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:fm.new_rsv_conflict_exec(document.frmAction,'<xsl:value-of select="$wb_lang"/>',Fsh);</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</div>
		<input type="hidden" name="cmd"/>
		<input type="hidden" name="module"/>
		<input type="hidden" name="url_success"/>
		<input type="hidden" name="url_failure"/>
		<input type="hidden" name="act"/>
		<input type="hidden" name="stylesheet"/>
		<input type="hidden" name="fsh_fac_id"/>
		<input type="hidden" name="fsh_date"/>
		<input type="hidden" name="fsh_start_time"/>
		<input type="hidden" name="fsh_end_time"/>
		<input type="hidden" name="rsv_id"/>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
</xsl:stylesheet>
