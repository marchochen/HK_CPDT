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
<xsl:import href="share/fm_share.xsl"/>
<xsl:import href="cust/wb_cust_const.xsl"/>

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
				<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				fm = new wbFm
			]]></script>
				<xsl:call-template name="wb_glb_css"/>
				<xsl:call-template name="wb_css">
					<xsl:with-param name="view">wb_ui</xsl:with-param>
				</xsl:call-template>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0"  >
				<form name="frmAction">
					<input type="hidden" name="module"/>
					<input type="hidden" name="cmd"/>
					<input type="hidden" name="url_success"/>
					<input type="hidden" name="url_failure"/>
					<input type="hidden" name="rsv_id" value="{/fm/reservation/@id}"/>
					<input type="hidden" name="fsh_fac_id">
					<xsl:attribute name="value">
						<xsl:for-each select="/fm/reservation/facility_schedule_list/facility_type/facility_schedule"><xsl:value-of select="facility/@id"/><xsl:if test="position() != last()">~</xsl:if></xsl:for-each>
					</xsl:attribute>
					</input>
					<input type="hidden" name="fsh_start_time">
					<xsl:attribute name="value">
						<xsl:for-each select="/fm/reservation/facility_schedule_list/facility_type/facility_schedule"><xsl:value-of select="@start_time"/><xsl:if test="position() != last()">~</xsl:if></xsl:for-each>
					</xsl:attribute>
					</input>
					<input type="hidden" name="fsh_upd_timestamp">
					<xsl:attribute name="value">
						<xsl:for-each select="/fm/reservation/facility_schedule_list/facility_type/facility_schedule"><xsl:value-of select="update_user/@timestamp"/><xsl:if test="position() != last()">~</xsl:if></xsl:for-each>					
					</xsl:attribute>
					</input>
					<xsl:call-template name="wb_init_lab"/>
					<xsl:call-template name="wb_ui_footer"/>
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
			<xsl:with-param name="lab_facilities_rsv">已預訂設施</xsl:with-param>
			<xsl:with-param name="lab_ref">要取消的設施</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_time">時間</xsl:with-param>
			<xsl:with-param name="lab_pencil_in">暫定</xsl:with-param>
			<xsl:with-param name="lab_to"> - </xsl:with-param>
			<xsl:with-param name="lab_cancellation_type">取消類型</xsl:with-param>
			<xsl:with-param name="lab_reason">原因</xsl:with-param>
			<xsl:with-param name="lab_please_select">-- 請選擇 --</xsl:with-param>
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
	<xsl:param name="lab_g_form_btn_cancel"/>
	<xsl:param name="lab_g_form_btn_reset"/>
	<xsl:param name="lab_g_form_btn_ok"/>
	
	<xsl:call-template name="fm_head"/>
	<xsl:call-template name="wb_ui_title">
		<xsl:with-param name="text" select="$lab_cancel_rsv"/>
	</xsl:call-template>
	<xsl:call-template name="wb_ui_space"/>
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
	
	<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
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
	<xsl:call-template name="wb_ui_space"/>
	
	<xsl:call-template name="wb_ui_head">
		<xsl:with-param name="text" select="$lab_cancellation_reason"/>
	</xsl:call-template>
	<xsl:call-template name="wb_ui_line"/>
	
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
			<tr>
				<td width="20%" align="right" height="10" >
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="80%"  height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right" >
					<span class="TitleText"><xsl:value-of select="$lab_cancellation_type"/><xsl:text>：</xsl:text></span>
				</td>
				<td width="80%" >
				<span class="Text">
					<select class="Select" name="cancel_type">
						<option value=""><xsl:value-of select="$lab_please_select"/></option>
						<xsl:for-each select="/fm/cancel_type_list/codes/code">
						<option value="{@title}"><xsl:value-of select="@title"/></option>
						</xsl:for-each>
					</select>
					</span>
				</td>
			</tr>		
			<tr>
				<td width="20%" align="right"  valign="top">
					<span class="TitleText"><xsl:value-of select="$lab_reason"/><xsl:text>：</xsl:text></span>
				</td>
				<td width="80%" >
					<textarea name="cancel_reason" rows="6" cols="50" style="width:300px;" class="InputFrm">
					</textarea>
				</td>
			</tr>					
			<tr>
				<td width="20%" align="right" height="10" >
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="80%"  height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>			
		</table>
		<xsl:call-template name="wb_ui_line"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td align="center" valign="middle">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:fm.cancel_rsv_fac_exec(document.frmAction,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
					</xsl:call-template>
					<img border="0" width="1" src="{$wb_img_path}tp.gif"/>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_reset"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:document.frmAction.reset()</xsl:with-param>
					</xsl:call-template>
					<img border="0" width="1" src="{$wb_img_path}tp.gif"/>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>			
	</xsl:template>	
	<!-- =============================================================== -->
</xsl:stylesheet>
