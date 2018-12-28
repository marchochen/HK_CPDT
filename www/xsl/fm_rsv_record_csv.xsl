<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:output  indent="no"/>
	<xsl:strip-space elements="*"/>
	<xsl:template match="/fm">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="reservation_record/facility_schedule_list">
			<xsl:with-param name="lab_rsv_record">預訂記錄</xsl:with-param>
			<xsl:with-param name="lab_start_time">開始時間</xsl:with-param>
			<xsl:with-param name="lab_end_time">結束時間</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_time">時間</xsl:with-param>
			<xsl:with-param name="lab_facility">設施</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_event">活動</xsl:with-param>
			<xsl:with-param name="lab_rsv_for">預訂從</xsl:with-param>
			<xsl:with-param name="lab_no_of_participants">參加人數</xsl:with-param>
			<xsl:with-param name="lab_to"> - </xsl:with-param>
			<xsl:with-param name="lab_pencilled_in">暫定</xsl:with-param>
			<xsl:with-param name="lab_reserved">已預訂</xsl:with-param>
			<xsl:with-param name="lab_cancelled">已取消</xsl:with-param>
			<xsl:with-param name="lab_unknow">未知</xsl:with-param>
			<xsl:with-param name="lab_cancel_reason">取消原因</xsl:with-param>
			<xsl:with-param name="lab_cancel_type">取消類型</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="reservation_record/facility_schedule_list">
			<xsl:with-param name="lab_rsv_record">预订记录</xsl:with-param>
			<xsl:with-param name="lab_start_time">开始时间</xsl:with-param>
			<xsl:with-param name="lab_end_time">结束时间</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_time">时间</xsl:with-param>
			<xsl:with-param name="lab_facility">设施</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_event">活动</xsl:with-param>
			<xsl:with-param name="lab_rsv_for">预订从</xsl:with-param>
			<xsl:with-param name="lab_no_of_participants">参加人数</xsl:with-param>
			<xsl:with-param name="lab_to"> - </xsl:with-param>
			<xsl:with-param name="lab_pencilled_in">暂定</xsl:with-param>
			<xsl:with-param name="lab_reserved">预订</xsl:with-param>
			<xsl:with-param name="lab_cancelled">取消</xsl:with-param>
			<xsl:with-param name="lab_unknow">未知</xsl:with-param>
			<xsl:with-param name="lab_cancel_reason">取消原因</xsl:with-param>
			<xsl:with-param name="lab_cancel_type">取消类型</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="reservation_record/facility_schedule_list">
			<xsl:with-param name="lab_rsv_record">Reservation record</xsl:with-param>
			<xsl:with-param name="lab_start_time">Start time</xsl:with-param>
			<xsl:with-param name="lab_end_time">End time</xsl:with-param>
			<xsl:with-param name="lab_date">Date</xsl:with-param>
			<xsl:with-param name="lab_time">Time</xsl:with-param>
			<xsl:with-param name="lab_facility">Facility</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_event">Event</xsl:with-param>
			<xsl:with-param name="lab_rsv_for">Reserved For</xsl:with-param>
			<xsl:with-param name="lab_no_of_participants">No. of participants</xsl:with-param>
			<xsl:with-param name="lab_to"> - </xsl:with-param>
			<xsl:with-param name="lab_pencilled_in">Pencilled-in</xsl:with-param>
			<xsl:with-param name="lab_reserved">Reserved</xsl:with-param>
			<xsl:with-param name="lab_cancelled">Cancelled</xsl:with-param>
			<xsl:with-param name="lab_unknow">Unknow</xsl:with-param>
			<xsl:with-param name="lab_cancel_reason">Cancellation reason</xsl:with-param>
			<xsl:with-param name="lab_cancel_type">Cancellation type</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="facility_schedule_list">
		<xsl:param name="lab_rsv_record"/>
		<xsl:param name="lab_start_time"/>
		<xsl:param name="lab_end_time"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_time"/>
		<xsl:param name="lab_facility"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_event"/>
		<xsl:param name="lab_rsv_for"/>
		<xsl:param name="lab_no_of_participants"/>
		<xsl:param name="lab_pencilled_in"/>
		<xsl:param name="lab_reserved"/>
		<xsl:param name="lab_cancelled"/>
		<xsl:param name="lab_unknow"/>
		<xsl:param name="lab_cancel_type"/>
		<xsl:param name="lab_cancel_reason"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<style type="text/css">

td{
	border: #CCCCCC;
}

</style>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<table border="1" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<xsl:value-of select="$lab_date"/>
					</td>
					<td>
						<xsl:value-of select="$lab_start_time"/>
					</td>
					<td>
						<xsl:value-of select="$lab_end_time"/>
					</td>
					<td>
						<xsl:value-of select="$lab_facility"/>
					</td>
					<td>
						<xsl:value-of select="$lab_status"/>
					</td>
					<td>
						<xsl:value-of select="$lab_event"/>
					</td>
					<td>
						<xsl:value-of select="$lab_rsv_for"/>
					</td>
					<td>
						<xsl:value-of select="$lab_no_of_participants"/>
					</td>
					<td>
						<xsl:value-of select="$lab_cancel_type"/>
					</td>
					<td>
						<xsl:value-of select="$lab_cancel_reason"/>
					</td>
				</tr>
				<xsl:for-each select="facility_schedule">
					<tr>
						<td>
							<xsl:call-template name="display_time">
								<xsl:with-param name="mode">csv</xsl:with-param>
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="@date"/>
								</xsl:with-param>
							</xsl:call-template>
						</td>
						<td>
							<xsl:value-of select="substring(substring-after(@start_time,' '),1,5)"/>
						</td>
						<td>
							<xsl:value-of select="substring(substring-after(@end_time,' '),1,5)"/>
						</td>
						<td>
							<xsl:value-of select="facility/basic/title"/>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="@status = 'PENCILLED_IN'">
									<xsl:value-of select="$lab_pencilled_in"/>
								</xsl:when>
								<xsl:when test="@status = 'RESERVED'">
									<xsl:value-of select="$lab_reserved"/>
								</xsl:when>
								<xsl:when test="@status = 'CANCELLED'">
									<xsl:value-of select="$lab_cancelled"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_unknow"/>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td>
							<xsl:value-of select="reservation/reservation_details/purpose"/>
						</td>
						<td>
							<xsl:value-of select="reservation/reservation_details/reserve_user/@display_name"/>
						</td>
						<td>
							<xsl:value-of select="reservation/reservation_details/participant_no"/>
						</td>
						<td>
							<xsl:value-of select="cancel_user/@type"/>
						</td>
						<td>
							<xsl:value-of select="cancel_user/@reason"/>
						</td>
					</tr>
				</xsl:for-each>
			</table>
		</body>
	</xsl:template>
</xsl:stylesheet>
