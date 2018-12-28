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
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/escape_carriage_return.xsl"/>

	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="share/fm_share.xsl"/>

	<xsl:output  indent="yes"/>
	<xsl:variable name="rsv_id" select="/fm/reservation/@id"/>
	<xsl:variable name="rsv_upd_timestamp" select="/fm/reservation/reservation_details/update_user/@timestamp"/>
	<xsl:variable name="main_fac_id" select="/fm/reservation/@main_fac_id"/>
	<xsl:variable name="main_room_facility" select="/fm/reservation/facility_schedule_list/facility_type[@main = 'YES']/facility_schedule/facility"/>
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
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				var goldenman = new wbGoldenMan
				usr = new wbUserGroup
			
				frm = document.frmAction;
				
				function getPopupUsrLst(fld_name, id_lst, nm_lst) {
					var args = 'X~%~' + id_lst + '~%~' +nm_lst 
					rsv_ent_id_box(args)
				}			
				var isExcludes = getUrlParam('isExcludes');
				fm = new wbFm(isExcludes);
				
				function FmSetRsvId(){
					wb_utils_fm_set_cookie('_fm_rsv_id',]]><xsl:value-of select="$rsv_id"/><![CDATA[)
					wb_utils_fm_set_cookie('rsv_itm_title',']]><xsl:call-template name="escape_js"><xsl:with-param name="input_str" select="reservation/reservation_details/purpose"/></xsl:call-template><![CDATA[');
				}
				
				function FshItem(){
					var args = FshItem.arguments
					this.fsh_fac_id = args[0]
					this.fsh_start_time = args[1]
					this.fsh_upd_timestamp = args[2]
					this.fsh_status =args[3]
					this.fsh_checkbox_id = args[4]
					this.fsh_fac_type_id = args[5]
				}
				
				function _change_venue(){
				]]><xsl:if test="count(/fm/reservation/facility_schedule_list/facility_type[@main = 'YES']/facility_schedule) != 0"><![CDATA[
					var _name = eval('_name' + document.frmAction.rsv_main_fac_id[document.frmAction.rsv_main_fac_id.selectedIndex].value)
					document.frmAction.rsv_desc.value = _name
				]]></xsl:if><![CDATA[
				}				

				Fsh = new Array
				]]><xsl:for-each select="reservation/facility_schedule_list/facility_type/facility_schedule">Fsh[Fsh.length] = new FshItem('<xsl:value-of select="facility/@id"/>','<xsl:value-of select="@start_time"/>','<xsl:value-of select="update_user/@timestamp"/>','<xsl:value-of select="@status"/>',<xsl:value-of select="position()"/>,<xsl:value-of select="../@id"/>)
				</xsl:for-each><![CDATA[
				ear_start_date = '';
				ear_start_time = '';
				late_end_date = '';
				late_end_time = '';
				]]><xsl:for-each select="reservation/facility_schedule_list/facility_type/facility_schedule"><xsl:sort select="substring-before(@start_time,' ')" order="ascending"/><xsl:if test="position()=1">ear_start_date = '<xsl:value-of select="substring-before(@start_time,' ')"/>';</xsl:if></xsl:for-each>
				<xsl:for-each select="reservation/facility_schedule_list/facility_type/facility_schedule"><xsl:sort select="substring-after(@start_time,' ')" order="ascending"/><xsl:if test="position()=1">ear_start_time = '<xsl:value-of select="substring-after(@start_time,' ')"/>';</xsl:if></xsl:for-each>
				<xsl:for-each select="reservation/facility_schedule_list/facility_type/facility_schedule"><xsl:sort select="substring-before(@end_time,' ')" order="descending"/><xsl:if test="position()=1">late_end_date = '<xsl:value-of select="substring-before(@end_time,' ')"/>';</xsl:if></xsl:for-each>
				<xsl:for-each select="reservation/facility_schedule_list/facility_type/facility_schedule"><xsl:sort select="substring-after(@end_time,' ')" order="descending"/><xsl:if test="position()=1">late_end_time = '<xsl:value-of select="substring-after(@end_time,' ')"/>';</xsl:if></xsl:for-each>
				<![CDATA[
				
				
				
				
				/*========*/
				]]><xsl:apply-templates select="/fm/reservation/facility_schedule_list/facility_type[@main = 'YES']/facility_schedule" mode="venue_details"/><![CDATA[				
			]]></script>
			<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="FmSetRsvId();">
			<form name="frmAction">
				<input type="hidden" name="module"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="rsv_id" value="{$rsv_id}"/>
				<input type="hidden" name="rsv_upd_timestamp" value="{$rsv_upd_timestamp}"/>
				<input type="hidden" name="fsh_fac_id"/>
				<input type="hidden" name="fsh_start_time"/>
				<input type="hidden" name="fsh_upd_timestamp"/>
				<input type="hidden" name="fsh_status"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="stylesheet"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_event">活動t</xsl:with-param>
			<xsl:with-param name="lab_rsv_info">預訂資料</xsl:with-param>
			<xsl:with-param name="lab_venue">地點</xsl:with-param>
			<xsl:with-param name="lab_rsv_for">預訂者</xsl:with-param>
			<xsl:with-param name="lab_main_room">主要房間</xsl:with-param>
			<xsl:with-param name="lab_no_of_participants">參加人數</xsl:with-param>
			<xsl:with-param name="lab_facilities_rsv">預訂的設施</xsl:with-param>
			<xsl:with-param name="lab_ref">以下是你已預訂的設施。你可以新增或刪除此預訂。</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_time">時間</xsl:with-param>
			<xsl:with-param name="lab_pencil_in">暫定</xsl:with-param>
			<xsl:with-param name="lab_to"> - </xsl:with-param>
			<xsl:with-param name="lab_add">新增</xsl:with-param>
			<xsl:with-param name="lab_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_required">必須填寫</xsl:with-param>
			<xsl:with-param name="lab_cancel_reason">取消原因</xsl:with-param>
			<xsl:with-param name="lab_please_select">-- 請選擇 --</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_event">活动</xsl:with-param>
			<xsl:with-param name="lab_rsv_info">预订信息</xsl:with-param>
			<xsl:with-param name="lab_venue">地点</xsl:with-param>
			<xsl:with-param name="lab_rsv_for">预订者</xsl:with-param>
			<xsl:with-param name="lab_main_room">主房间</xsl:with-param>
			<xsl:with-param name="lab_no_of_participants">参加人数</xsl:with-param>
			<xsl:with-param name="lab_facilities_rsv">预订的设施</xsl:with-param>
			<xsl:with-param name="lab_ref">以下是您已预订的设施。您可以添加或删除此预订。</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_time">时间</xsl:with-param>
			<xsl:with-param name="lab_pencil_in">暂定</xsl:with-param>
			<xsl:with-param name="lab_to"> - </xsl:with-param>
			<xsl:with-param name="lab_add">添加</xsl:with-param>
			<xsl:with-param name="lab_remove">删除</xsl:with-param>
			<xsl:with-param name="lab_required">必须填写</xsl:with-param>
			<xsl:with-param name="lab_cancel_reason">取消原因</xsl:with-param>
			<xsl:with-param name="lab_please_select">-- 请选择 --</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_event">Event</xsl:with-param>
			<xsl:with-param name="lab_rsv_info">Reservation info</xsl:with-param>
			<xsl:with-param name="lab_venue">Venue</xsl:with-param>
			<xsl:with-param name="lab_rsv_for">Reserved for</xsl:with-param>
			<xsl:with-param name="lab_main_room">Main room</xsl:with-param>
			<xsl:with-param name="lab_no_of_participants">No. of participants</xsl:with-param>
			<xsl:with-param name="lab_facilities_rsv">Facilities reserved</xsl:with-param>
			<xsl:with-param name="lab_ref">These are the facilities you have reserved. You may add or remove the reservation.</xsl:with-param>
			<xsl:with-param name="lab_date">Date</xsl:with-param>
			<xsl:with-param name="lab_time">Time</xsl:with-param>
			<xsl:with-param name="lab_pencil_in">Pencil-in</xsl:with-param>
			<xsl:with-param name="lab_to"> - </xsl:with-param>
			<xsl:with-param name="lab_add">Add</xsl:with-param>
			<xsl:with-param name="lab_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_required">Required</xsl:with-param>
			<xsl:with-param name="lab_cancel_reason">Cancellation reason</xsl:with-param>
			<xsl:with-param name="lab_please_select">-- Please select --</xsl:with-param>
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
		<xsl:param name="lab_add"/>
		<xsl:param name="lab_remove"/>
		<xsl:param name="lab_required"/>
		<xsl:param name="lab_cancel_reason"/>
		<xsl:param name="lab_please_select"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		
		<xsl:call-template name="fm_head"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_rsv_info"/>
		</xsl:call-template>
		
		<xsl:apply-templates>
			<xsl:with-param name="lab_event" select="$lab_event"/>
			<xsl:with-param name="lab_rsv_info" select="$lab_rsv_info"/>
			<xsl:with-param name="lab_venue" select="$lab_venue"/>
			<xsl:with-param name="lab_rsv_for" select="$lab_rsv_for"/>
			<xsl:with-param name="lab_main_room" select="$lab_main_room"/>
			<xsl:with-param name="lab_no_of_participants" select="$lab_no_of_participants"/>
			<xsl:with-param name="lab_facilities_rsv" select="$lab_facilities_rsv"/>
			<xsl:with-param name="lab_ref" select="$lab_ref"/>
			<xsl:with-param name="lab_date" select="$lab_date"/>
			<xsl:with-param name="lab_time" select="$lab_time"/>
			<xsl:with-param name="lab_pencil_in" select="$lab_pencil_in"/>
			<xsl:with-param name="lab_to" select="$lab_to"/>
			<xsl:with-param name="lab_add" select="$lab_add"/>
			<xsl:with-param name="lab_remove" select="$lab_remove"/>
			<xsl:with-param name="lab_required" select="$lab_required"/>
			<xsl:with-param name="lab_cancel_reason" select="$lab_cancel_reason"/>
			<xsl:with-param name="lab_please_select" select="$lab_please_select"/>
			<xsl:with-param name="lab_g_form_btn_cancel" select="$lab_g_form_btn_cancel"/>
			<xsl:with-param name="lab_g_form_btn_ok" select="$lab_g_form_btn_ok"/>
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
		<xsl:param name="lab_add"/>
		<xsl:param name="lab_remove"/>
		<xsl:param name="lab_cancel_reason"/>
		
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
						<xsl:with-param name="mode">edit</xsl:with-param>
						<xsl:with-param name="rsv_id" select="$rsv_id"/>
						<xsl:with-param name="lab_ref" select="$lab_ref"/>
						<xsl:with-param name="lab_date" select="$lab_date"/>
						<xsl:with-param name="lab_time" select="$lab_time"/>
						<xsl:with-param name="lab_pencil_in" select="$lab_pencil_in"/>
						<xsl:with-param name="lab_to" select="$lab_to"/>
						<xsl:with-param name="lab_add" select="$lab_add"/>
						<xsl:with-param name="lab_remove" select="$lab_remove"/>
						<xsl:with-param name="lab_cancel_reason" select="$lab_cancel_reason"/>
						<xsl:with-param name="start_datetime">ear_start_date+' '+ear_start_time</xsl:with-param>
						<xsl:with-param name="end_datetime">late_end_date+' '+late_end_time</xsl:with-param>
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
		<xsl:param name="lab_required"/>
		<xsl:param name="lab_please_select"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_rsv_info"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>

		<table>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_event"/><xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="purpose"/>
					<input name="rsv_purpose" type="hidden" value="{purpose}"/>
				</td>
			</tr>
			<xsl:choose>
				<xsl:when test="count(../facility_schedule_list/facility_type[@main = 'YES']/facility_schedule) = 0">
					<input name="rsv_main_fac_id" type="hidden" value=""/>
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td class="wzb-form-label">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_main_room"/><xsl:text>：</xsl:text>
						</td>
						<td class="wzb-form-control">
							<select class="wzb-form-select" name="rsv_main_fac_id" onchange="_change_venue()">
								<option value=""><xsl:value-of select="$lab_please_select"/></option>
								<xsl:apply-templates select="../facility_schedule_list/facility_type[@main = 'YES']/facility_schedule" mode="rsv_main"/>
							</select>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_venue"/><xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<textarea name="rsv_desc" rows="6" cols="50" style="width:300px;"  class="wzb-inputTextArea">
						<xsl:value-of select="desc"/>
					</textarea>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_rsv_for"/><xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<input type="hidden" name="rsv_ent_id"/>
					<xsl:call-template name="wb_goldenman">
						<xsl:with-param name="frm">document.frmAction</xsl:with-param>
						<xsl:with-param name="width">300</xsl:with-param>
						<xsl:with-param name="field_name">rsv_ent_id_box</xsl:with-param>
						<xsl:with-param name="tree_type">user_group_and_user</xsl:with-param>
						<xsl:with-param name="select_type">6</xsl:with-param>
						<xsl:with-param name="box_size">1</xsl:with-param>
						<xsl:with-param name="add_btn">true</xsl:with-param>
						<xsl:with-param name="remove_btn">true</xsl:with-param>
						<xsl:with-param name="search">true</xsl:with-param>
						<xsl:with-param name="search_function">javascript:usr.search.popup_search_prep('ent_ids_lst','0',<xsl:value-of select="/fm/meta/cur_usr/@root_ent_id"/>)</xsl:with-param>
						<xsl:with-param name="single_option_text"><xsl:value-of select="reserve_user/@display_name"/></xsl:with-param>
						<xsl:with-param name="single_option_value"><xsl:value-of select="reserve_user/@ent_id"/></xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>			
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_no_of_participants"/><xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<input type="text" name="rsv_participant_no" value="{participant_no}" style="width:50px;" maxlength="255" class="wzb-inputText"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_total_cost"/><xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="$fac_total_cost"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
				</td>
				<td width="80%">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_required"/>
				</td>
			</tr>			
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_ok"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:fm.rsv_upd_exec(document.frmAction,'<xsl:value-of select="$wb_lang"/>',Fsh,<xsl:value-of select="$rsv_id"/>)</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_cancel"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:fm.get_rsv_details(wb_utils_fm_get_cookie('cur_rsv_id'));wb_utils_fm_set_cookie('cur_rsv_id','');</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
		</div>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<xsl:template match="facility_schedule" mode="rsv_main">
		<xsl:if test="count($main_room_facility) != 0">
			<xsl:variable name="my_id">
				<xsl:value-of select="facility/@id"/>
			</xsl:variable>
			<xsl:if test="@status!='CANCELLED' and count(preceding-sibling::*[@status!='CANCELLED']/facility[@id = $my_id]) = 0">
				<option value="{facility/@id}">
					<xsl:if test="$main_fac_id = facility/@id">
						<xsl:attribute name="selected">selected</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="facility/basic/title"/>
				</option>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	<xsl:template match="facility_schedule" mode="venue_details">
	<xsl:variable name="my_id" ><xsl:value-of select="facility/@id"/></xsl:variable>
	<xsl:if test="@status!='CANCELLED' and count(preceding-sibling::*[@status!='CANCELLED']/facility[@id = $my_id]) = 0">
		_name<xsl:value-of select="facility/@id" />='<xsl:call-template name="escape_carriage_return"><xsl:with-param name="my_right_value"><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="facility/basic/desc"/></xsl:with-param></xsl:call-template></xsl:with-param></xsl:call-template>'
	</xsl:if>
	</xsl:template>		
	<!-- =============================================================== -->
</xsl:stylesheet>
