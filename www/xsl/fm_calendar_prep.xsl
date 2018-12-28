<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="wb_const.xsl"/>
<xsl:import href="utils/wb_utils.xsl"/>
<xsl:import href="utils/wb_ui_title.xsl"/>
<xsl:import href="utils/wb_ui_space.xsl"/>
<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
<xsl:import href="utils/wb_ui_line.xsl"/>
<xsl:import href="utils/wb_ui_head.xsl"/>
<xsl:import href="utils/wb_ui_footer.xsl"/>
<xsl:import href="utils/wb_ui_desc.xsl"/>
<xsl:import href="utils/wb_init_lab.xsl"/>
<xsl:import href="utils/wb_gen_form_button.xsl"/>
<xsl:import href="utils/wb_css.xsl"/>
<xsl:import href="utils/display_form_input_time.xsl"/>
<xsl:import href="share/fm_share.xsl"/>
<xsl:import href="cust/wb_cust_const.xsl"/>

	<xsl:output  indent="yes"/>
	<xsl:strip-space elements="*"/>
	<!-- =============================================================== -->	
	<xsl:variable name="cur_type_id" select="/fm/facility_list/@cur_type_id"/>
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
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				var isExcludes = getUrlParam('isExcludes');
				fm = new wbFm(isExcludes);
				
				function _refresh(){
					var fac_type= document.frmAction.select_type[document.frmAction.select_type.selectedIndex].value
						fm.get_rsv_calendar_prep(fac_type)
				}			
				function init(){
					CAL_START_DATE = wb_utils_fm_get_cookie("FM_CAL_START_DATE");
					if(CAL_START_DATE==""){
						var today = new Date
						var yy = today.getFullYear()
						var mm = today.getMonth() +1
						var dd = today.getDate()
						var frm = document.frmAction
						frm.start_date_yy.value = yy
						frm.start_date_mm.value = mm
						frm.start_date_dd.value = dd
					}else{
						var frm = document.frmAction;
						start_datetime = CAL_START_DATE.split(" ");
						start_date = start_datetime[0].split("-");
						week_cnt = start_datetime[1];
						
						frm.start_date_yy.value = start_date[0];
						frm.start_date_mm.value = start_date[1];
						frm.start_date_dd.value = start_date[2];
						if(frm.week_count.length){
							for(i=0;i<frm.week_count.length;i++)
								if(frm.week_count[i].value == week_cnt)
									frm.week_count[i].selected = true;
						}
					}
					reformDatetimeFormat(frm)
					frm.start_date_dd.focus()
				}
				
				function reformDatetimeFormat(frm){
					if (frm.start_date_dd){
						if (frm.start_date_dd.value.length == 1) { frm.start_date_dd.value = "0" + frm.start_date_dd.value;}
					}
					if (frm.start_date_mm){
						if (frm.start_date_mm.value.length == 1) { frm.start_date_mm.value = "0" + frm.start_date_mm.value;}
					}
					if (frm.end_date_dd){
						if (frm.end_date_dd.value.length == 1) { frm.end_date_dd.value = "0" + frm.end_date_dd.value;}
					}
					if (frm.end_date_mm){
						if (frm.end_date_mm.value.length == 1) { frm.end_date_mm.value = "0" + frm.end_date_mm.value;}
					}
					if (frm.start_time_hour){
						if (frm.start_time_hour.value.length == 1) { frm.start_time_hour.value = "0" + frm.start_time_hour.value;}
					}
					if (frm.start_time_min){
						if (frm.start_time_min.value.length == 1) { frm.start_time_min.value = "0" + frm.start_time_min.value;}
					}
					if (frm.end_time_hour){
						if (frm.start_time_hour.value.length == 1) { frm.end_time_hour.value = "0" + frm.end_time_hour.value;}
					}
					if (frm.end_time_min){
						if (frm.end_time_min.value.length == 1) { frm.end_time_min.value = "0" + frm.end_time_min.value;}
					}
				}
					
			]]></script>
				<xsl:call-template name="new_css"/>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="init();">
				<form name="frmAction">
					<input type="hidden" name="cmd"/>
					<input type="hidden" name="module"/>
					<input type="hidden" name="end_date"/>
					<input type="hidden" name="fac_id"/>
					<input type="hidden" name="ext_fac_id"/>
					<input type="hidden" name="stylesheet"/>
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="fm">
			<xsl:with-param name="lab_g_form_btn_view">查看</xsl:with-param>
			<xsl:with-param name="lab_rsv_calendar">預定日曆</xsl:with-param>
			<xsl:with-param name="lab_select_facility_type">設施類型:</xsl:with-param>
			<xsl:with-param name="lab_all">-- 全部 --</xsl:with-param>
			<xsl:with-param name="lab_date">日期:</xsl:with-param>
			<xsl:with-param name="lab_view_from">查看從</xsl:with-param>
			<xsl:with-param name="lab_for_a_period_of">開始</xsl:with-param>
			<xsl:with-param name="lab_week">週</xsl:with-param>
			<xsl:with-param name="lab_select_facility_below">選擇下面的設施</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="fm">
			<xsl:with-param name="lab_g_form_btn_view">查看</xsl:with-param>
			<xsl:with-param name="lab_rsv_calendar">预定日历</xsl:with-param>
			<xsl:with-param name="lab_select_facility_type">设施类型：</xsl:with-param>
			<xsl:with-param name="lab_all">-- 全部 --</xsl:with-param>
			<xsl:with-param name="lab_date">日期：</xsl:with-param>
			<xsl:with-param name="lab_view_from">查看从</xsl:with-param>
			<xsl:with-param name="lab_for_a_period_of">开始</xsl:with-param>
			<xsl:with-param name="lab_week">周</xsl:with-param>
			<xsl:with-param name="lab_select_facility_below">选择下面的设施：</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="fm">
			<xsl:with-param name="lab_g_form_btn_view">View</xsl:with-param>
			<xsl:with-param name="lab_rsv_calendar">Reservation calendar</xsl:with-param>
			<xsl:with-param name="lab_select_facility_type">Facility type :</xsl:with-param>
			<xsl:with-param name="lab_all">-- All --</xsl:with-param>
			<xsl:with-param name="lab_date">Date :</xsl:with-param>
			<xsl:with-param name="lab_view_from">View from </xsl:with-param>
			<xsl:with-param name="lab_for_a_period_of"> for a period of </xsl:with-param>
			<xsl:with-param name="lab_week"> week</xsl:with-param>
			<xsl:with-param name="lab_select_facility_below">Select facilities below</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="fm">
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_rsv_calendar"/>
		<xsl:param name="lab_select_facility_type"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_view_from"/>
		<xsl:param name="lab_for_a_period_of"/>
		<xsl:param name="lab_week"/>
		<xsl:param name="lab_select_facility_below"/>
		<xsl:param name="lab_g_form_btn_view"/>
		<xsl:call-template name="fm_head"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_rsv_calendar"/>
			</xsl:with-param>
		</xsl:call-template>	
		
		
		<table>
			<tr>
				<td>
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$lab_select_facility_type"/>
					<xsl:text>&#160;</xsl:text>
					<select class="wzb-form-slect" name="select_type" onchange="_refresh()">
						<option value=""><xsl:value-of select="$lab_all"/></option>
						<xsl:for-each select="facility_list/facility_type">
							<option value="{@id}"><xsl:if test="$cur_type_id = @id"><xsl:attribute name="selected">selected</xsl:attribute></xsl:if><xsl:call-template name="fm_display_title"/></option>
						</xsl:for-each>
					</select>
				</td>
			</tr>
		</table>	
		
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_rsv_calendar"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		
		<table>
			<tr>
				<td class="wzb-form-label" valign="baseline">
					<xsl:value-of select="$lab_date"/>
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="$lab_view_from"/>
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="frm">document.frmAction</xsl:with-param>
						<xsl:with-param name="fld_name">start_date</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">start_date</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
					</xsl:call-template>
					<xsl:value-of select="$lab_for_a_period_of"/>
					<select name="week_count" class="wzb-form-select">
						<option value="1" selected="selected">1</option>
						<option value="2" selected="selected">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
						<option value="6">6</option>
						<option value="7">7</option>
						<option value="8">8</option>
					</select>
					<xsl:value-of select="$lab_week"/>
				</td>
			</tr>
		</table>
		
		<xsl:apply-templates select="facility_list">
			<xsl:with-param name="lab_select_facility_below"><xsl:value-of select="$lab_select_facility_below"/></xsl:with-param>
		</xsl:apply-templates>
		
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_view"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:fm.get_rsv_calendar_exec(document.frmAction,'<xsl:value-of select="$wb_lang"/>');</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->	
	<xsl:template match="facility_list">
	<xsl:param name="lab_select_facility_below"/>
		<xsl:apply-templates mode="fm">
			<xsl:with-param name="lab_select_facility_below"><xsl:value-of select="$lab_select_facility_below"/></xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="facility_type" mode="fm">
	<xsl:param name="lab_no_facility"/>
	<xsl:param name="lab_select_facility_below"/>
	<xsl:param name="col_count">4</xsl:param>
	<xsl:if test="($cur_type_id = '') or ($cur_type_id = @id)">
		<xsl:call-template name="wb_ui_space"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:call-template name="fm_display_title"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<tr>
				<td>
					<xsl:call-template name="wb_ui_desc">
						<xsl:with-param name="text">
							<xsl:value-of select="$lab_select_facility_below"/>
						</xsl:with-param>
					</xsl:call-template>
					
					
				</td>
			</tr>
		</table>
		<xsl:choose>
			<xsl:when test="count(facility_subtype) = 0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_no_facility"/>
					</xsl:with-param>
					<xsl:with-param name="top_line">false</xsl:with-param>
					<xsl:with-param name="bottom_line">false</xsl:with-param>
				</xsl:call-template>

<!--
				<table border="0" cellpadding="10" cellspacing="0" width="{$wb_gen_table_width}" class="Bg">
					<tr>
						<td class="wbFmBg">
							<img src="{$wb_img_path}tp.gif" border="0" align="absmiddle" width="1" height="1"/>
						</td>
					</tr>
					<tr>
						<td class="wbFmBg" align="center">
							<span class="wbFmHeadRefText"><xsl:value-of select="$lab_no_facility"/></span>
						</td>
					</tr>
					<tr>
						<td class="wbFmBg">
							<img src="{$wb_img_path}tp.gif" border="0" align="absmiddle" width="1" height="1"/>
						</td>
					</tr>
				</table>
-->				
			</xsl:when>
			<xsl:otherwise>
				<table>
					<tr>
						<td>
							<xsl:for-each select="facility_subtype">
								<xsl:if test="position() mod  $col_count = 1">
									<xsl:text disable-output-escaping="yes">&lt;table border="0" cellpadding="0" class="Bg" cellspacing="0"&gt;&lt;tr&gt;</xsl:text>
								</xsl:if>
								<td valign="top" align="left" width="25%">
									 
									<xsl:apply-templates select="." mode="fm">
										<xsl:with-param name="xsl_prefix" ><xsl:value-of select="../@xsl_prefix"/></xsl:with-param>
										<xsl:with-param name="hasSelectAll">true</xsl:with-param>
										<xsl:with-param name="frm_name">frmAction</xsl:with-param>
										<xsl:with-param name="col_count"><xsl:value-of select="$col_count"/></xsl:with-param>
										<xsl:with-param name="hideOffItem">true</xsl:with-param>
										<!-- to be change , hardcoded with ext_venue name-->
										<xsl:with-param name="ext_venue_list">true</xsl:with-param>
										<xsl:with-param name="ext_venue_name">External Venue</xsl:with-param>
										<!-- to be change -->
									</xsl:apply-templates>
								</td>
								<xsl:if test="(position() mod  $col_count = 0) or (position() = last())">
									<xsl:text disable-output-escaping="yes">&lt;/tr&gt;&lt;/table&gt;</xsl:text><br/>
									<img src="{$wb_img_path}tp.gif" border="0" align="absmiddle"/>
								</xsl:if>									
							</xsl:for-each>	
						</td>
					</tr>
				</table>
			</xsl:otherwise>
		</xsl:choose>			
	</xsl:if>	
</xsl:template>

</xsl:stylesheet>
