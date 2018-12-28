<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/display_form_input_hhmm.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- ================================================================================== -->
	<xsl:variable name="wb_gen_table_width">500</xsl:variable>
	<xsl:variable name="usr_cur_attd_id" select="/applyeasy/attendance_maintance/attendance/@status"/>
	<xsl:variable name="usr_remark" select="/applyeasy/attendance_maintance/attendance/remark"/>
	<xsl:variable name="usr_display_name" select="/applyeasy/attendance_maintance/attendance/user/name/@display_name"/>
	<xsl:variable name="itm_id" select="/applyeasy/attendance_maintance/item/@id"/>
	<xsl:variable name="att_timestamp" select="/applyeasy/attendance_maintance/attendance/@att_timestamp"/>
	<!-- ================================================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="applyeasy"/>
		</html>
	</xsl:template>
	<!-- ================================================================================== -->
	<xsl:template match="applyeasy">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<!--alert样式  -->
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_{$wb_cur_lang}.js"></script>
			<!--alert样式  end -->
				
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
		
				var attd = new wbAttendance
				
				default_value = new Array();
				
				function init(){
					var completion_date = unescape(getUrlParam('completion_date'));
					if(completion_date == ''){
						COMPLETION_DATETIME = ']]><xsl:value-of select="$att_timestamp"/><![CDATA['
						var frm = document.frmXml;
						if (COMPLETION_DATETIME !='') {
						completion_datetime = COMPLETION_DATETIME.split(" ");
						completion_date =completion_datetime[0].split("-");
						completion_time = completion_datetime[1].split(":");
						
						frm.completion_date_yy.value = completion_date[0];
						frm.completion_date_mm.value = completion_date[1];
						frm.completion_date_dd.value = completion_date[2];
					
						frm.completion_date_hour.value = completion_time[0];
						frm.completion_date_min.value = completion_time[1];
						
						reformDatetimeFormat(frm);
						}
						frm.completion_date_yy.focus();
					} 									
				}
				
				function reformDatetimeFormat(frm){
					if (frm.completion_date_dd){
						if (frm.completion_date_dd.value.length == 1) { frm.completion_date_dd.value = "0" + frm.completion_date_dd.value;}
					}
					if (frm.completion_date_mm){
						if (frm.completion_date_mm.value.length == 1) { frm.completion_date_mm.value = "0" + frm.completion_date_mm.value;}
					}
					if (frm.completion_date_hour){
						if (frm.completion_date_hour.value.length == 1) { frm.completion_date_hour.value = "0" + frm.completion_date_hour.value;}
					}
					if (frm.completion_date_min){
						if (frm.completion_date_min.value.length == 1) { frm.completion_date_min.value = "0" + frm.completion_date_min.value;}
					}
				}
						
				function status(){
					return false;
				}

				function change_val(val){
					for(i=1; i<default_value.length; i++){
						eval('document.frmXml.type_' + i + '_value').value = default_value[i][val];
					}
					return;
				}
				
				function selectRadio(val){
				//alert(val);
					ele = eval('document.frmXml.type_' + val);
					ele[1].checked = true;
					return;
				}
		
			]]></script>
			<xsl:call-template name="new_css"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="init()">
			<FORM onsubmit="return status()" name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="app_id" value="{/applyeasy/attendance_maintance/attendance/@app_id}"/>
				<input type="hidden" name="itm_id" value="{$itm_id}"/>
				<input type="hidden" name="att_update_timestamp" value="{/applyeasy/attendance_maintance/attendance/att_update_timestamp}"/>
				<input type="hidden" name="usr_ent_id" value="{/applyeasy/attendance_maintance/attendance/user/@ent_id}"/>
				<input type="hidden" name="att_status" value="{$usr_cur_attd_id}"/>
				<input type="hidden" name="att_timestamp"/>
			</FORM>
		</body>
	</xsl:template>
	<!-- ================================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_status">學習狀態</xsl:with-param>
			<xsl:with-param name="lab_remark">備註</xsl:with-param>
			<xsl:with-param name="lab_remark_requirement">(不超過400個字元)</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_change_to">改為</xsl:with-param>
			<xsl:with-param name="lab_completion_results">考勤</xsl:with-param>
			<xsl:with-param name="lab_date">結訓日期</xsl:with-param>
			<xsl:with-param name="lab_time">時間</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_remark">备注</xsl:with-param>
			<xsl:with-param name="lab_remark_requirement">(不超过400个的字符)</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_change_to">改为</xsl:with-param>
			<xsl:with-param name="lab_completion_results">考勤</xsl:with-param>
			<xsl:with-param name="lab_date">结训日期</xsl:with-param>
			<xsl:with-param name="lab_time">时间</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_status">Learning status</xsl:with-param>
			<xsl:with-param name="lab_remark">Remark</xsl:with-param>
			<xsl:with-param name="lab_remark_requirement">(Not more than 400 characters)</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_change_to">Change to</xsl:with-param>
			<xsl:with-param name="lab_completion_results">Result</xsl:with-param>
			<xsl:with-param name="lab_date">Completion date</xsl:with-param>
			<xsl:with-param name="lab_time">Time</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_remark"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_change_to"/>
		<xsl:param name="lab_remark_requirement"/>
		<xsl:param name="lab_completion_results"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_time"/>
		<xsl:variable name="width">500</xsl:variable>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_completion_results"/>
		</xsl:call-template>
		<table>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_dis_name"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="attendance_maintance/attendance/user/name/@display_name"/>
				</td>
			</tr>
			<!-- edit by william  start-->
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_date"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						<xsl:with-param name="fld_name">completion_date</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">completion_date</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
						<xsl:with-param name="display_form_input_hhmm">Y</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<!-- edit by william  end-->
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_remark"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<textarea name="remark" style="width:300px;" rows="5" wrap="VIRTUAL" cols="20" class="wzb-inputTextArea">
						<xsl:value-of select="$usr_remark"/>
					</textarea>
					<br/>
					<xsl:value-of select="$lab_remark_requirement"/>
				</td>
			</tr>
			<xsl:for-each select="figure_value_list/figure_value">
				<xsl:variable name="outter_loop_pos" select="position()"/>
				<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[default_value[]]><xsl:value-of select="position()"/><![CDATA[] = new Array() ]]></script>
				<xsl:for-each select="default_value/value">
					<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[default_value[]]><xsl:value-of select="$outter_loop_pos"/><![CDATA[][]]><xsl:value-of select="@ats_id"/><![CDATA[] = ']]><xsl:value-of select="format-number(text(),'0.00')"/><![CDATA[']]></script>
				</xsl:for-each>
				<tr>
					<td class="wzb-form-label" valign="top" rowspan="2">
						<input type="hidden" name="icv_name" value="{title/desc[@lan = $wb_lang_encoding]/@name}"/>
						<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
					</td>
					<td class="wzb-form-control">
						<xsl:variable name="fig_value" select="format-number(@fig_value,'0.00')"/>
						<input type="radio" name="type_{position()}" checked="checked">
							<xsl:value-of select="$fig_value"/>
						</input>
						<input type="hidden" name="type_{position()}_default_val" value="{$fig_value}"/>
					</td>
				</tr>
				<tr>
					<td>
						<input type="radio" name="type_{position()}" style="width : 20;">
							<xsl:variable name="fig_value" select="format-number(default_value/value[@ats_id = $usr_cur_attd_id],'0.00')"/>
							<xsl:value-of select="$lab_change_to"/>: <input type="text" class="wzb-inputText" name="type_{position()}_value" value="{$fig_value}" onclick="javascript:selectRadio({position()})"/>
						</input>
					</td>
				</tr>
				<input type="hidden" name="ict_id" value="{@fgt_id}"/>
			</xsl:for-each>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:attd.chg_completion_date(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:parent.close()<!--window.location.href = wb_utils_get_cookie('maintain_attd_url_prev')-->
				</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- ====================================================================================================== -->
</xsl:stylesheet>
