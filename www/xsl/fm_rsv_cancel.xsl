<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="share/fm_share.xsl"/>
	<xsl:output  indent="yes"/>
	<xsl:strip-space elements="*"/>
	<xsl:template match="*"/>
	<!-- =============================================================== -->
	<xsl:template match="/fm">
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
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
				<form name="frmAction">
					<input type="hidden" name="module"/>
					<input type="hidden" name="cmd"/>
					<input type="hidden" name="url_success"/>
					<input type="hidden" name="url_failure"/>
					<input type="hidden" name="rsv_id" value="{/fm/reservation/@id}"/>
					<input type="hidden" name="rsv_upd_timestamp" value="{/fm/reservation/reservation_details/update_user/@timestamp}"/>
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_reset">重新設置</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_cancel_rsv">取消預訂</xsl:with-param>
			<xsl:with-param name="lab_cancellation_reason">取消原因</xsl:with-param>
			<xsl:with-param name="lab_facilities_rsv">已預訂的設施</xsl:with-param>
			<xsl:with-param name="lab_ref">要取消的設施</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_time">時間</xsl:with-param>
			<xsl:with-param name="lab_pencil_in">暫定</xsl:with-param>
			<xsl:with-param name="lab_to"> - </xsl:with-param>
			<xsl:with-param name="lab_cancellation_type">取消類型</xsl:with-param>
			<xsl:with-param name="lab_reason">原因</xsl:with-param>
			<xsl:with-param name="lab_please_select">-- 請選擇 --</xsl:with-param>
			<xsl:with-param name="lab_star">*</xsl:with-param>
			<xsl:with-param name="lab_required">必須填寫</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_reset">重新设置</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_cancel_rsv">取消预订</xsl:with-param>
			<xsl:with-param name="lab_cancellation_reason">取消原因</xsl:with-param>
			<xsl:with-param name="lab_facilities_rsv">已预订的设施</xsl:with-param>
			<xsl:with-param name="lab_ref">要取消的设施</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_time">时间</xsl:with-param>
			<xsl:with-param name="lab_pencil_in">暂定</xsl:with-param>
			<xsl:with-param name="lab_to"> - </xsl:with-param>
			<xsl:with-param name="lab_cancellation_type">取消类型</xsl:with-param>
			<xsl:with-param name="lab_reason">原因</xsl:with-param>
			<xsl:with-param name="lab_please_select">-- 请选择 --</xsl:with-param>
			<xsl:with-param name="lab_star">*</xsl:with-param>
			<xsl:with-param name="lab_required">如果取消类型选择为"其他"，请填写具体原因</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_reset">Reset</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_cancel_rsv">Cancel reservation</xsl:with-param>
			<xsl:with-param name="lab_cancellation_reason">Cancellation reason</xsl:with-param>
			<xsl:with-param name="lab_facilities_rsv">Facilities reserved</xsl:with-param>
			<xsl:with-param name="lab_ref">Facilities to be cancelled</xsl:with-param>
			<xsl:with-param name="lab_date">Date</xsl:with-param>
			<xsl:with-param name="lab_time">Time</xsl:with-param>
			<xsl:with-param name="lab_pencil_in">Pencil-in</xsl:with-param>
			<xsl:with-param name="lab_to"> - </xsl:with-param>
			<xsl:with-param name="lab_cancellation_type">Cancellation type</xsl:with-param>
			<xsl:with-param name="lab_reason">Reason</xsl:with-param>
			<xsl:with-param name="lab_please_select">-- Please select --</xsl:with-param>
			<xsl:with-param name="lab_star">*</xsl:with-param>
			<xsl:with-param name="lab_required">Required</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="reservation">
		<xsl:param name="lab_cancel_rsv"/>
		<xsl:param name="lab_cancellation_reason"/>
		<xsl:param name="lab_facilities_rsv"/>
		<xsl:param name="lab_ref"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_time"/>
		<xsl:param name="lab_pencil_in"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_cancellation_type"/>
		<xsl:param name="lab_reason"/>
		<xsl:param name="lab_please_select"/>
		<xsl:param name="lab_star"/>
		<xsl:param name="lab_required"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_reset"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:call-template name="fm_head"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_cancel_rsv"/>
		</xsl:call-template>
		<xsl:apply-templates>
			<xsl:with-param name="lab_cancellation_reason" select="$lab_cancellation_reason"/>
			<xsl:with-param name="lab_facilities_rsv" select="$lab_facilities_rsv"/>
			<xsl:with-param name="lab_ref" select="$lab_ref"/>
			<xsl:with-param name="lab_date" select="$lab_date"/>
			<xsl:with-param name="lab_time" select="$lab_time"/>
			<xsl:with-param name="lab_pencil_in" select="$lab_pencil_in"/>
			<xsl:with-param name="lab_to" select="$lab_to"/>
			<xsl:with-param name="lab_cancellation_type" select="$lab_cancellation_type"/>
			<xsl:with-param name="lab_reason" select="$lab_reason"/>
			<xsl:with-param name="lab_please_select" select="$lab_please_select"/>
			<xsl:with-param name="lab_star" select="$lab_star"/>
			<xsl:with-param name="lab_required" select="$lab_required"/>
			<xsl:with-param name="lab_g_form_btn_cancel" select="$lab_g_form_btn_cancel"/>
			<xsl:with-param name="lab_g_form_btn_reset" select="$lab_g_form_btn_reset"/>
			<xsl:with-param name="lab_g_form_btn_ok" select="$lab_g_form_btn_ok"/>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="facility_schedule_list">
		<xsl:param name="lab_cancellation_reason"/>
		<xsl:param name="lab_facilities_rsv"/>
		<xsl:param name="lab_ref"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_time"/>
		<xsl:param name="lab_pencil_in"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_cancellation_type"/>
		<xsl:param name="lab_reason"/>
		<xsl:param name="lab_please_select"/>
		<xsl:param name="lab_star"/>
		<xsl:param name="lab_required"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_reset"/>
		<xsl:param name="lab_g_form_btn_ok"/>
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
					</xsl:apply-templates>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_cancellation_reason"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_cancellation_type"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<select class="wzb-form-select" name="cancel_type">
						<option value="">
							<xsl:value-of select="$lab_please_select"/>
						</option>
						<xsl:for-each select="/fm/cancel_type_list/codes/code">
							<option value="{@title}">
								<xsl:value-of select="@title"/>
							</option>
						</xsl:for-each>
					</select>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="wzb-form-star">
						<xsl:value-of select="$lab_star"/>
					</span>
						<xsl:value-of select="$lab_reason"/>
						<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<textarea name="cancel_reason" rows="6" cols="50" style="width:300px;" class="wzb-inputTextArea"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
				</td>
				<td class="wzb-form-control">
					<span class="wzb-form-star">
						<xsl:value-of select="$lab_star"/>&#160;
					</span>
					<xsl:value-of select="$lab_required"/>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:fm.cancel_rsv_exec(document.frmAction,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_reset"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:document.frmAction.reset()</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
