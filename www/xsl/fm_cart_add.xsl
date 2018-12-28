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
<xsl:import href="utils/display_form_input_hhmm.xsl"/>
<xsl:import href="share/fm_share.xsl"/>
<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output  indent="yes"/>
	<xsl:strip-space elements="*"/>
<!-- ==================================================================== -->	
	<xsl:variable name="cur_type_id" select="/fm/facility_list/@cur_type_id"/>
<!-- ==================================================================== -->
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
				<script LANGUAGE="JavaScript" TYPE="text/javascript">
				
					<![CDATA[
						var isExcludes = getUrlParam('isExcludes');
						fm = new wbFm(isExcludes);
						$(function(){
							init();
						})
						function init(){
							if(wb_utils_fm_get_cookie('rsv_itm_title') == '')
								return;
							var fac_id  = unescape(getUrlParam('fac_id'))
							var start_date = unescape(getUrlParam('start_date'))
							var end_date = unescape(getUrlParam('end_date'))
							if(fac_id != ''){
								fm.feed_fac_id(document.frmAction, fac_id)
							}
							if(start_date != ''){
								fm.feed_start_date(document.frmAction,start_date)
								document.frmAction.start_time_hour.value = start_date.substring(11,13);
								if(document.frmAction.start_time_hour.value == '00')
									document.frmAction.start_time_hour.value = '08';
								document.frmAction.start_time_min.value = start_date.substring(14,16);
								if(document.frmAction.start_time_min.value == '00')
									document.frmAction.start_time_min.value = '30';
							}	
							if(end_date != ''){
								fm.feed_end_date(document.frmAction,end_date)
								document.frmAction.end_time_hour.value = end_date.substring(11,13);
								if(document.frmAction.end_time_hour.value == '00')
									document.frmAction.end_time_hour.value = '17';
								document.frmAction.end_time_min.value = end_date.substring(14,16);
								if(document.frmAction.end_time_min.value == '00')
									document.frmAction.end_time_min.value = '30';
							}	
							if(start_date == '' || end_date == ''){
								NEW_START_DATETIME = wb_utils_fm_get_cookie("FM_NEW_START_DATETIME");
								NEW_END_DATETIME = wb_utils_fm_get_cookie("FM_NEW_END_DATETIME");
								if(NEW_START_DATETIME=="" || NEW_END_DATETIME==""){
									var today = new Date
									var yy = today.getFullYear()
									var mm = today.getMonth() +1
									var dd = today.getDate()
									var frm = document.frmAction
									
										frm.start_date_yy.value = yy
										frm.start_date_mm.value = mm
										frm.start_date_dd.value = dd
									
										frm.end_date_yy.value = yy
										frm.end_date_mm.value = mm
										frm.end_date_dd.value = dd	
									
									var slot = this.fm.check_timeslot()	
										frm.start_time_hour.value = '8'
										frm.start_time_min.value = '30'
										frm.end_time_hour.value = '17'
										frm.end_time_min.value = '30'
								}else{
									var frm = document.frmAction;
										start_datetime = NEW_START_DATETIME.split(" ");
										start_date = start_datetime[0].split("-");
										start_time = start_datetime[1].split(":");
										
										frm.start_date_yy.value = start_date[0];
										frm.start_date_mm.value = start_date[1];
										frm.start_date_dd.value = start_date[2];
										
										end_datetime = NEW_END_DATETIME.split(" ");
										end_date = end_datetime[0].split("-");
										end_time = end_datetime[1].split(":");
										
										frm.end_date_yy.value = end_date[0];
										frm.end_date_mm.value = end_date[1];
										frm.end_date_dd.value = end_date[2];
									
										frm.start_time_hour.value = start_time[0];
										frm.start_time_min.value = start_time[1];
										frm.end_time_hour.value = end_time[0];
										frm.end_time_min.value = end_time[1];
								}
								
									/*
									if(slot == 1){	
										frm.start_time_hour.value = '8'
										frm.start_time_min.value = '30'
										frm.end_time_hour.value = '17'
										frm.end_time_min.value = '30'
									}else if(slot == 2){
										frm.start_time_hour.value = '8'
										frm.start_time_min.value = '30'
										frm.end_time_hour.value = '17'
										frm.end_time_min.value = '30'							
									}else if(slot == 3){
										frm.start_time_hour.value = '8'
										frm.start_time_min.value = '30'
										frm.end_time_hour.value = '17'
										frm.end_time_min.value = '30'	
															
									}
									*/
								reformDatetimeFormat(frm)
								frm.start_time_hour.focus()	
							} 									
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
						
						function _refresh(){
							var fac_type= document.frmAction.select_type[document.frmAction.select_type.selectedIndex].value
								fm.get_new_rsv(fac_type,'',unescape(getUrlParam('start_date')),unescape(getUrlParam('end_date')),getUrlParam('rsv_id'))
						}		
												
						function toggle_btn(type){
							if(type=="MEETING"){
								_img = new Image();
								_img.src = "]]><xsl:value-of select="$wb_img_path"/><![CDATA[tp.gif";
								document["search_btn"].width = 0;
								document["search_btn"].height = 0;
								document["search_btn"].src = _img.src;
								document.frmAction.rsv_itm_title.value = "";
								//document.frmAction.rsv_itm_title.readOnly = false;
							}else{
								_img = new Image();
								_img.src = "]]><xsl:value-of select="$wb_img_path"/><![CDATA[gen_btn_search_topic_off.gif";
								document["search_btn"].width = 46;
								document["search_btn"].height = 15;
								document["search_btn"].src = _img.src;
								document.frmAction.rsv_itm_title.value = "";
								//document.frmAction.rsv_itm_title.readOnly = false;
							}
						}
						
						function loadPage(){
							wb_utils_fm_set_cookie("rsv_return_url",wb_utils_invoke_ae_servlet('cmd','ae_upd_itm_rsv','itm_id',document.frmAction.cos_run_id.value));
							wb_utils_fm_set_cookie("rsv_itm_title",document.frmAction.rsv_itm_title.value);
							wb_utils_fm_set_cookie('work_rsv_itm_title',document.frmAction.rsv_itm_title.value);
							wb_utils_fm_set_cookie("rsv_itm_id",document.frmAction.cos_run_id.value);
							wb_utils_fm_set_cookie("url_success",fm.get_rsv_details_url(document.frmAction.rsv_itm_id.value));
							wb_utils_fm_set_cookie("work_rsv_id",document.frmAction.rsv_itm_id.value);
							wb_utils_fm_set_cookie("cur_rsv_id","");
							wb_utils_fm_set_cookie("cart","");
							if(wb_utils_fm_get_cookie("work_rsv_id")=="")
								setTimeout('self.location.reload()',500);
							else
								self.location.href = fm.get_rsv_details_url(wb_utils_fm_get_cookie("work_rsv_id"));
						}
						
						function newBooking(){
							//var t_title=$('#target_page_title').val();
							var z_title=document.frmAction.rsv_itm_title.value;
						
							if (document.frmAction.rsv_itm_title) {
								document.frmAction.rsv_itm_title.value = wbUtilsTrimString(document.frmAction.rsv_itm_title.value);
							}
							if(gen_validate_empty_field(document.frmAction.rsv_itm_title, wb_msg_event, "]]><xsl:value-of select="$wb_lang"/><![CDATA[")){
								wb_utils_fm_set_cookie('rsv_return_url','');
								wb_utils_fm_set_cookie("rsv_itm_title", document.frmAction.rsv_itm_title.value);
								wb_utils_fm_set_cookie('work_rsv_itm_title', document.frmAction.rsv_itm_title.value);
								wb_utils_fm_set_cookie("url_success","");
								wb_utils_fm_set_cookie("work_rsv_id","");
								wb_utils_fm_set_cookie("cur_rsv_id","");
								wb_utils_fm_set_cookie("cart","");
								self.location.href =  self.location.href + "&title=" + encodeURIComponent(z_title);
								//self.location.reload();
								return;
							}
						}
					]]>
				</script>
				<xsl:call-template name="new_css"/>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" >
				<form name="frmAction">
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="fm">
			<xsl:with-param name="lab_g_form_btn_show_conflicts">顯示衝突</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_check_avail">顯示可用設施</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_rsv_facility">預約設施</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_new_rsv">新增預訂</xsl:with-param>
			<xsl:with-param name="lab_select_facility_type">設施類型：</xsl:with-param>
			<xsl:with-param name="lab_date">日期：</xsl:with-param>
			<xsl:with-param name="lab_time">時間：</xsl:with-param>
			<xsl:with-param name="lab_all">-- 全部 --</xsl:with-param>
			<xsl:with-param name="lab_to">到</xsl:with-param>
			<xsl:with-param name="lab_select_facility_below">選擇下面的設施：</xsl:with-param>
			<xsl:with-param name="lab_no_facility">沒有設施</xsl:with-param>
			<xsl:with-param name="lab_rsv_purpose">預訂目的</xsl:with-param>
			<xsl:with-param name="lab_purpose">目的</xsl:with-param>
			<xsl:with-param name="lab_event">輸入活動名稱</xsl:with-param>
			<xsl:with-param name="lab_meeting">會議</xsl:with-param>
			<xsl:with-param name="lab_cos_running">課程班級</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="fm">
			<xsl:with-param name="lab_g_form_btn_show_conflicts">显示冲突</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_check_avail">显示可用设施</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_rsv_facility">预约设施</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_new_rsv">新增预订</xsl:with-param>
			<xsl:with-param name="lab_select_facility_type">设施类型：</xsl:with-param>
			<xsl:with-param name="lab_date">日期：</xsl:with-param>
			<xsl:with-param name="lab_time">时间：</xsl:with-param>
			<xsl:with-param name="lab_all">-- 全部 --</xsl:with-param>
			<xsl:with-param name="lab_to">到</xsl:with-param>
			<xsl:with-param name="lab_select_facility_below">选择下面的设施：</xsl:with-param>
			<xsl:with-param name="lab_no_facility">没有设施</xsl:with-param>
			<xsl:with-param name="lab_rsv_purpose">预订目的</xsl:with-param>
			<xsl:with-param name="lab_purpose">目的</xsl:with-param>
			<xsl:with-param name="lab_event">输入活动名称</xsl:with-param>
			<xsl:with-param name="lab_meeting">会议</xsl:with-param>
			<xsl:with-param name="lab_cos_running">课程班级</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="fm">
			<xsl:with-param name="lab_g_form_btn_show_conflicts">Show conflicts</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_check_avail">Check availability</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_rsv_facility">Reservation facility</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next">Next</xsl:with-param>
			<xsl:with-param name="lab_new_rsv">New reservation</xsl:with-param>
			<xsl:with-param name="lab_select_facility_type">Facility type :</xsl:with-param>
			<xsl:with-param name="lab_date">Date :</xsl:with-param>
			<xsl:with-param name="lab_time">Time :</xsl:with-param>
			<xsl:with-param name="lab_all">-- All --</xsl:with-param>
			<xsl:with-param name="lab_to">to</xsl:with-param>
			<xsl:with-param name="lab_select_facility_below">Select facilities below</xsl:with-param>
			<xsl:with-param name="lab_no_facility">There is no facility</xsl:with-param>
			<xsl:with-param name="lab_rsv_purpose">Reservation purpose</xsl:with-param>
			<xsl:with-param name="lab_purpose">Purpose</xsl:with-param>
			<xsl:with-param name="lab_event">Enter event name</xsl:with-param>
			<xsl:with-param name="lab_meeting">Meeting</xsl:with-param>
			<xsl:with-param name="lab_cos_running">Training</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="fm">
		<xsl:param name="lab_new_rsv"/>
		<xsl:param name="lab_select_facility_type"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_time"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_select_facility_below"/>
		<xsl:param name="lab_no_facility"/>
		<xsl:param name="lab_rsv_purpose"/>
		<xsl:param name="lab_purpose"/>
		<xsl:param name="lab_event"/>
		<xsl:param name="lab_meeting"/>
		<xsl:param name="lab_cos_running"/>
		<xsl:param name="lab_g_form_btn_show_conflicts"/>
		<xsl:param name="lab_g_form_btn_check_avail"/>
		<xsl:param name="lab_g_form_btn_rsv_facility"/>
		<xsl:param name="lab_g_form_btn_next"/>
		
		
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_new_rsv"/>
		        <xsl:if test="/fm/showtitle != ''">
					-
				</xsl:if>
		       <xsl:value-of select="/fm/showtitle"/>
			</xsl:with-param>
		</xsl:call-template>
		
		
		<xsl:call-template name="fm_head"/>
		<div class="margin-top28"></div>
		<span id="span1" style="">
		<script language="Javascript">
			<![CDATA[
				if(wb_utils_fm_get_cookie('rsv_itm_title') != ''){
					//document.write('<span style="position:absolute;display:none;">');
					$("#span1").css({
						position:"absolute", display:"none"
					})
				} 
			]]>
		</script>
		
		<input type="hidden" name="rsv_itm_id" value=""/>
		<input type="hidden" name="cos_run_id"/>
		
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_rsv_purpose"/>
		</xsl:call-template>
	<!-- 	<xsl:call-template name="wb_ui_line"/>	 -->	
		
		<table>
			<input type="hidden" name="rsv_itm_type" value="MEETING"/> 			
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_event"/>：
				</td>
				<td class="wzb-form-control">
					<table>
						<tr>
							<td width="5%">
								<input size="60" type="text" name="rsv_itm_title" class="wzb-inputText"/>
							</td>
							<td width="95%">
								<a href="Javascript:fm.pick_run_prep(frmAction)">
									<img src="{$wb_img_path}tp.gif" name="search_btn" id="search_btn" width="1" height="1" border="0"/>
								</a>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			
		</table>			
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_next"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:newBooking()</xsl:with-param>
			</xsl:call-template>
		</div>
		
		<!-- ===================================== -->
		</span>
		
		<script language="Javascript">
			<![CDATA[ 
				//if(wb_utils_fm_get_cookie('rsv_itm_title') != '') {
					//document.write('</span>');
				//} 
			]]>
		</script>
		
		<span id="span2">
		
		<script language="Javascript">
			<![CDATA[ 
				if(wb_utils_fm_get_cookie('rsv_itm_title') == ''){
					//document.write('<span style="position:absolute;display:none;">');
					$("#span2").css({
						position:"absolute", display:"none"
					})
				} 
			]]>
		</script>
		
		<table>
			<tr>
				<td>
					<xsl:text>&#160;</xsl:text><xsl:value-of select="$lab_select_facility_type"/>
					<xsl:text>&#160;</xsl:text>
					<select class="wzb-form-select" name="select_type" onchange="_refresh()">
						<option value=""><xsl:value-of select="$lab_all"/></option>
						<xsl:for-each select="facility_list/facility_type">
							<option value="{@id}">
								<xsl:if test="$cur_type_id = @id">
									<xsl:attribute name="selected">selected</xsl:attribute>
								</xsl:if>
								<xsl:call-template name="fm_display_title"/>
							</option>
						</xsl:for-each>
					</select>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_new_rsv"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>		
		
		<table>
			<tr>
				<td colspan="2" height="10">
				</td>
			</tr>
		
			<tr>
				<td width="20%" align="right">
					<xsl:value-of select="$lab_date"/>
				</td>
				<td width="80%" style="padding:0 0 5px 0;">
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="frm">document.frmAction</xsl:with-param>
						<xsl:with-param name="fld_name">start_date</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">start_date</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
					</xsl:call-template>
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$lab_to"/>
					<xsl:text>&#160;</xsl:text>
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="frm">document.frmAction</xsl:with-param>
						<xsl:with-param name="fld_name">end_date</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">end_date</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			
			<tr>
				<td width="20%" align="right">
					<span class="Text"><xsl:value-of select="$lab_time"/></span>
				</td>
				<td width="80%">
					<xsl:call-template name="display_form_input_hhmm">
						<xsl:with-param name="frm">document.frmAction</xsl:with-param>
						<xsl:with-param name="fld_name">start_time</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">start_time</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
					</xsl:call-template>
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$lab_to"/>
					<xsl:text>&#160;</xsl:text>
					<xsl:call-template name="display_form_input_hhmm">
						<xsl:with-param name="frm">document.frmAction</xsl:with-param>
						<xsl:with-param name="fld_name">end_time</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">end_time</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
					</xsl:call-template>
					<input type="hidden" name="start_time"/>
					<input type="hidden" name="end_time"/>
				</td>
			</tr>

			<tr>
				<td colspan="2" align="right" height="10">
				</td>
			</tr>
		</table>
		
		<xsl:apply-templates select="facility_list">
			<xsl:with-param name="lab_select_facility_below"><xsl:value-of select="$lab_select_facility_below"/></xsl:with-param>
			<xsl:with-param name="lab_no_facility"><xsl:value-of select="$lab_no_facility"/></xsl:with-param>
		</xsl:apply-templates>

		
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_rsv_facility"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:fm.new_rsv_exec(document.frmAction,'reserve','<xsl:value-of select="$wb_lang"/>');</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_check_avail"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:fm.new_rsv_exec(document.frmAction,'search','<xsl:value-of select="$wb_lang"/>');</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_show_conflicts"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:fm.new_rsv_exec(document.frmAction,'check','<xsl:value-of select="$wb_lang"/>');</xsl:with-param>
			</xsl:call-template>
		</div>
	    </span>
		<script language="Javascript">
			<![CDATA[ 
				//if(wb_utils_fm_get_cookie('rsv_itm_title') == ''){
					//document.write('</span>');
				//} 
			]]>
		</script>
			
		<xsl:call-template name="wb_ui_footer"/>	
		<input type="hidden" name="cmd"/>
		<input type="hidden" name="module"/>
		<input type="hidden" name="url_success"/>
		<input type="hidden" name="url_failure"/>
		<input type="hidden" name="act"/>
		<input type="hidden" name="fac_id"/>	
		<input type="hidden" name="stylesheet"/>
		<input type="hidden" name="rsv_id"/>
	</xsl:template>
	<!-- =============================================================== -->	
	<xsl:template match="facility_list">
	<xsl:param name="lab_no_facility"/>
	<xsl:param name="lab_select_facility_below"/>
		<xsl:apply-templates mode="fm">
			<xsl:with-param name="lab_no_facility"><xsl:value-of select="$lab_no_facility"/></xsl:with-param>
			<xsl:with-param name="lab_select_facility_below"><xsl:value-of select="$lab_select_facility_below"/></xsl:with-param>
			<xsl:with-param name="cur_type_id"><xsl:value-of select="@cur_type_id"/></xsl:with-param>
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
			<xsl:with-param name="text"><xsl:call-template name="fm_display_title"/></xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>


	
		<table>
			<tr>
				<td>
					<xsl:call-template name="wb_ui_desc">
						<xsl:with-param name="text"><xsl:value-of select="$lab_select_facility_below"/></xsl:with-param>
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
			</xsl:when>
			
		<xsl:otherwise>		
			<table>
				<tr>
					<td>
						<xsl:for-each select="facility_subtype">
							<xsl:if test="position() mod  $col_count = 1">
								<xsl:text disable-output-escaping="yes">&lt;table border="0" cellpadding="0" cellspacing="0"&gt;&lt;tr&gt;</xsl:text>
							</xsl:if>
							<td valign="top" align="left">
								<xsl:apply-templates select="." mode="fm">
									<xsl:with-param name="xsl_prefix" ><xsl:value-of select="../@xsl_prefix"/></xsl:with-param>
									<xsl:with-param name="hasSelectAll">true</xsl:with-param>
									<xsl:with-param name="hideOffItem">true</xsl:with-param>
									<xsl:with-param name="frm_name">frmAction</xsl:with-param>
									<xsl:with-param name="col_count"><xsl:value-of select="$col_count"/></xsl:with-param>
									
								</xsl:apply-templates>
							</td>
							<xsl:if test="(position() mod  $col_count = 0) or (position() = last())">
								<xsl:text disable-output-escaping="yes">&lt;/tr&gt;&lt;/table&gt;</xsl:text><br/>
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
