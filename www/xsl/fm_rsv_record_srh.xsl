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
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="share/fm_share.xsl"/>

	<xsl:import href="utils/display_form_input_time.xsl"/>

	<xsl:output  indent="yes"/>
	<xsl:strip-space elements="*"/>
	<xsl:variable name="cur_type_id" select="/fm/facility_list/@cur_type_id"/>
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
						fm.get_rsv_record_srh(fac_type)
				}

				function init(){
					REC_START_DATE = wb_utils_fm_get_cookie("FM_REC_START_DATE");
					REC_END_DATE = wb_utils_fm_get_cookie("FM_REC_END_DATE");
					if(REC_START_DATE=="" || REC_END_DATE==""){
						var today = new Date
						var yy = today.getFullYear()
						var mm = today.getMonth() +1
						var dd = today.getDate()
						var frm = document.frmAction

						frm.start_date_yy.value = yy
						frm.start_date_mm.value = mm
						frm.start_date_dd.value = dd
						frm.start_date_dd.focus()

						frm.end_date_yy.value = yy
						frm.end_date_mm.value = mm
						frm.end_date_dd.value = dd
					}else{
						var frm = document.frmAction;
						start_date = REC_START_DATE.split("-");
						end_date = REC_END_DATE.split("-");

						frm.start_date_yy.value = start_date[0];
						frm.start_date_mm.value = start_date[1];
						frm.start_date_dd.value = start_date[2];

						frm.end_date_yy.value = end_date[0];
						frm.end_date_mm.value = end_date[1];
						frm.end_date_dd.value = end_date[2];
					}
					reformDatetimeFormat(frm)
					frm.end_date_dd.focus()
					var i, n, ele
					var check_name = 'fm_fac_id_lst_'
					n = frm.elements.length
					// default all facilies are not selected
//					for (i = 0; i < n; i++) {
//						ele = frm.elements[i]
//						if (ele.type == "checkbox"  && (ele.name.search(check_name) != -1)) {
//							ele.checked = true;
//						}
//					}
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
					<input type="hidden" name="fac_id"/>
					<input type="hidden" name="status"/>
					<input type="hidden" name="url_failure"/>
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
			<xsl:with-param name="lab_rsv_record">預訂記錄</xsl:with-param>
			<xsl:with-param name="lab_select_facility_type">設施類型:</xsl:with-param>
			<xsl:with-param name="lab_all">-- 全部 --</xsl:with-param>
			<xsl:with-param name="lab_date">日期:</xsl:with-param>
			<xsl:with-param name="lab_ownership">擁有者:</xsl:with-param>
			<xsl:with-param name="lab_status">狀態:</xsl:with-param>
			<xsl:with-param name="lab_view_from">從</xsl:with-param>
			<xsl:with-param name="lab_to">到</xsl:with-param>
			<xsl:with-param name="lab_week">週</xsl:with-param>
			<xsl:with-param name="lab_view_create_myself">查看我的建立的預訂</xsl:with-param>
			<xsl:with-param name="lab_view_reserved_myself">查看為我建立的預訂</xsl:with-param>
			<xsl:with-param name="lab_view_all">查看所有預訂</xsl:with-param>
			<xsl:with-param name="lab_reserved">已預訂</xsl:with-param>
			<xsl:with-param name="lab_pencilled_in">暫定</xsl:with-param>
			<xsl:with-param name="lab_cancelled">已取消</xsl:with-param>
			<xsl:with-param name="lab_select_facility_below">選擇下面的設施</xsl:with-param>
			<xsl:with-param name="lab_no_facility">沒有可用設施</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="fm">
			<xsl:with-param name="lab_g_form_btn_view">查看</xsl:with-param>
			<xsl:with-param name="lab_rsv_record">预订记录</xsl:with-param>
			<xsl:with-param name="lab_select_facility_type">设施类型：</xsl:with-param>
			<xsl:with-param name="lab_all">-- 全部 --</xsl:with-param>
			<xsl:with-param name="lab_date">日期：</xsl:with-param>
			<xsl:with-param name="lab_ownership">拥有者：</xsl:with-param>
			<xsl:with-param name="lab_status">状态：</xsl:with-param>
			<xsl:with-param name="lab_view_from">从</xsl:with-param>
			<xsl:with-param name="lab_to">到</xsl:with-param>
			<xsl:with-param name="lab_week">周</xsl:with-param>
			<xsl:with-param name="lab_view_create_myself">查看我创建的预订</xsl:with-param>
			<xsl:with-param name="lab_view_reserved_myself">查看为我创建的预订</xsl:with-param>
			<xsl:with-param name="lab_view_all">查看所有预订</xsl:with-param>
			<xsl:with-param name="lab_reserved">预订</xsl:with-param>
			<xsl:with-param name="lab_pencilled_in">暂定</xsl:with-param>
			<xsl:with-param name="lab_cancelled">取消</xsl:with-param>
			<xsl:with-param name="lab_select_facility_below">选择下面的设施</xsl:with-param>
			<xsl:with-param name="lab_no_facility">没有可用设施</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="fm">
			<xsl:with-param name="lab_g_form_btn_view">View</xsl:with-param>
			<xsl:with-param name="lab_rsv_record">Reservation record</xsl:with-param>
			<xsl:with-param name="lab_select_facility_type">Facility type :</xsl:with-param>
			<xsl:with-param name="lab_all">-- All --</xsl:with-param>
			<xsl:with-param name="lab_date">Date :</xsl:with-param>
			<xsl:with-param name="lab_ownership">Ownership :</xsl:with-param>
			<xsl:with-param name="lab_status">Status :</xsl:with-param>
			<xsl:with-param name="lab_view_from">From </xsl:with-param>
			<xsl:with-param name="lab_to"> to </xsl:with-param>
			<xsl:with-param name="lab_week"> week</xsl:with-param>
			<xsl:with-param name="lab_view_create_myself">View reservation created by myself</xsl:with-param>
			<xsl:with-param name="lab_view_reserved_myself">View reservation reserved for myself</xsl:with-param>
			<xsl:with-param name="lab_view_all">View all reservations</xsl:with-param>
			<xsl:with-param name="lab_reserved">Reserved</xsl:with-param>
			<xsl:with-param name="lab_pencilled_in">Pencilled-in</xsl:with-param>
			<xsl:with-param name="lab_cancelled">Cancelled</xsl:with-param>
			<xsl:with-param name="lab_select_facility_below">Select facilities below</xsl:with-param>
			<xsl:with-param name="lab_no_facility">No facility avaiable</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="fm">
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_rsv_record"/>
		<xsl:param name="lab_select_facility_type"/>
		<xsl:param name="lab_select_facility_below"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_ownership"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_view_from"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_week"/>
		<xsl:param name="lab_view_all"/>
		<xsl:param name="lab_view_create_myself"/>
		<xsl:param name="lab_view_reserved_myself"/>
		<xsl:param name="lab_reserved"/>
		<xsl:param name="lab_cancelled"/>
		<xsl:param name="lab_pencilled_in"/>
		<xsl:param name="lab_g_form_btn_view"/>
		<xsl:param name="lab_no_facility"/>			

		<xsl:call-template name="fm_head"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_rsv_record"/>
		</xsl:call-template>
		<table>
			<tr>
				<td width="660">
					<xsl:text>&#160;</xsl:text><xsl:value-of select="$lab_select_facility_type"/>
					<xsl:text>&#160;</xsl:text>
					<span><select class="wzb-form-select" name="select_type" onchange="_refresh()"><option value=""><xsl:value-of select="$lab_all"/></option><xsl:for-each select="facility_list/facility_type"><option value="{@id}"><xsl:if test="$cur_type_id = @id"><xsl:attribute name="selected">selected</xsl:attribute></xsl:if><xsl:call-template name="fm_display_title"/></option></xsl:for-each></select></span>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_rsv_record"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<tr>
			</tr>
			<tr>
				<td class="wzb-form-label" >
					<xsl:value-of select="$lab_date"/>
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="display_form_input_time">
					<xsl:with-param name="frm">document.frmAction</xsl:with-param>
					<xsl:with-param name="fld_name">start_date</xsl:with-param>
					<xsl:with-param name="hidden_fld_name">start_date</xsl:with-param>
					<xsl:with-param name="show_label">Y</xsl:with-param>
					</xsl:call-template>
					<xsl:value-of  select="$lab_to"/>
					<xsl:text disable-output-escaping="yes">&#160;</xsl:text> <!-- &amp;nbsp;  -->
					<xsl:call-template name="display_form_input_time">
					<xsl:with-param name="frm">document.frmAction</xsl:with-param>
					<xsl:with-param name="fld_name">end_date</xsl:with-param>
					<xsl:with-param name="hidden_fld_name">end_date</xsl:with-param>
					<xsl:with-param name="show_label">Y</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_status"/>
				</td>
				<td class="wzb-form-control">
					<label for="status1">
						<input id="status1" type="checkbox" name="status_reserved" value="RESERVED"
							checked="checked" />
						<xsl:value-of select="$lab_reserved" />
					</label>
					<label for="status2" class="margin-left10">
						<input id="status2" type="checkbox" name="status_pencilled"
							value="PENCILLED_IN" checked="checked" />
						<xsl:value-of select="$lab_pencilled_in" />
					</label>
					<label for="status3" class="margin-left10">
						<input id="status3" type="checkbox" name="status_cancelled"
							value="CANCELLED" />
						<xsl:value-of select="$lab_cancelled" />
					</label>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_ownership"/>
				</td>
				<td class="wzb-form-control">
					<div class="Text">
						<label for="owner1">
							<input id="owner1" type="radio" name="own_type" value="created" checked="checked"/>
							<xsl:value-of select="$lab_view_create_myself"/>
						</label>
					</div>
					<div class="Text">
						<label for="owner2">
							<input id="owner2" type="radio" name="own_type" value="reserved" />
							<xsl:value-of select="$lab_view_reserved_myself" />
						</label>
					</div>
					<div class="Text">
						<label for="owner3">
							<input id="owner3" type="radio" name="own_type" value="" />
							<xsl:value-of select="$lab_view_all" />
						</label>
					</div>
				</td>
			</tr>
		</table>
		<xsl:apply-templates select="facility_list">
			<xsl:with-param name="lab_select_facility_below"><xsl:value-of select="$lab_select_facility_below"/></xsl:with-param>
			<xsl:with-param name="lab_no_facility"><xsl:value-of select="$lab_no_facility"/></xsl:with-param>
		</xsl:apply-templates>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_view"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:fm.get_rsv_record_srh_exec(document.frmAction,'<xsl:value-of select="$wb_lang"/>');</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="facility_list">
	<xsl:param name="lab_select_facility_below"/>
	<xsl:param name="lab_no_facility"/>
		<xsl:apply-templates mode="fm">
			<xsl:with-param name="lab_select_facility_below" select="$lab_select_facility_below"/>
			<xsl:with-param name="lab_no_facility" select="$lab_no_facility"/>
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
		<xsl:choose>
		<xsl:when test="count(facility_subtype) = 0">
			<xsl:call-template name="wb_ui_show_no_item">
				<xsl:with-param name="top_line">false</xsl:with-param>
				<xsl:with-param name="bottom_line">false</xsl:with-param>
				<xsl:with-param name="text" select="$lab_no_facility"/>
				<xsl:with-param name="bg_class">Bg</xsl:with-param>
			</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
		<table>
		<tr>
			<td>
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text" select="$lab_select_facility_below"/>
				</xsl:call-template>                
			</td>
		</tr>
		</table>
		<table>
			<tr>
				<td>
					<xsl:for-each select="facility_subtype">
						<xsl:if test="position() mod  $col_count = 1">
							<xsl:text disable-output-escaping="yes">&lt;table border="0" cellpadding="0" cellspacing="0"&gt;&lt;tr&gt;</xsl:text>
						</xsl:if>
						<td valign="top" align="left" width="25%">
							<xsl:apply-templates select="." mode="fm">
								<xsl:with-param name="xsl_prefix" ><xsl:value-of select="../@xsl_prefix"/></xsl:with-param>
								<xsl:with-param name="hasSelectAll">true</xsl:with-param>
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
