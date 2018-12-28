<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<!-- customize utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- other -->
	<xsl:import href="share/itm_gen_details_share.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:output indent="yes" />
	<!-- =============================================================== -->
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="itm_type" select="/applyeasy/item/@type"/>
	<xsl:variable name="itm_title" select="/applyeasy/item/title"/>
	<xsl:variable name="itm_updated_timestamp" select="/applyeasy/item/last_updated/@timestamp"/>
	<xsl:variable name="itm_status" select="/applyeasy/item/@status"/>
	<xsl:variable name="turn_itm_status">
		<xsl:choose>
			<xsl:when test="$itm_status = 'OFF'">ON</xsl:when>
			<xsl:otherwise>OFF</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="page_variant" select="/applyeasy/meta/page_variant"/>
	<xsl:variable name="create_run_ind" select="/applyeasy/item/@create_run_ind"/>
	<xsl:variable name="cur_month" select="/applyeasy/item/child_items/cur_time/@month"/>
	<xsl:variable name="cur_year" select="/applyeasy/item/child_items/cur_time/@year"/>
	<xsl:variable name="itm_type_list_root" select="/applyeasy/item/child_items/item_type_list"/>
	<xsl:variable name="itm_apply_ind" select="/applyeasy/item/@apply_ind"/>
	<xsl:variable name="run_ind" select="/applyeasy/item/@run_ind"/>
	<xsl:variable name="usr_id" select="/applyeasy/meta/cur_usr/@ent_id"/>
	<xsl:variable name="escaped_item_title">
		<xsl:call-template name="escape_js">
			<xsl:with-param name="input_str"><xsl:value-of select="/applyeasy/item/title"/></xsl:with-param>
		</xsl:call-template>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:apply-templates select="applyeasy/item"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="applyeasy/item">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_message.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_announcement.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_report.js"/>
				<script language="javascript" type="text/javascript" src="{$wb_js_path}wb_criteria.js"/>
				<script language="javascript" type="text/javascript" src="{$wb_js_path}wb_mote.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[
				
				msg = new wbMessage
				itm_lst = new wbItem
				course_lst = new wbCourse	
				
				window.onunload = unloadHandler;
				function unloadHandler(){
					wb_utils_set_cookie('lrn_soln_itm_title','')
				}
				
				function init(){
					wb_utils_set_cookie('lrn_soln_itm_title',']]><xsl:value-of select="$escaped_item_title"/><![CDATA[')
				}
			]]></script>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="javascript:init()">
				<form name="frmXml">
					<xsl:call-template name="wb_init_lab"/>
					<input type="hidden" name="itm_id" value="{@id}"/>
					<input type="hidden" name="run_ind" value="{@run_ind}"/>
					<input type="hidden" name="ji_msg_id" value="{ji_message/message[@type = 'JI']/@id}"/>
					<input type="hidden" name="ji_reminder_msg_id" value="{ji_message/message[@type = 'REMINDER']/@id}"/>
					<input type="hidden" name="url_success" value=""/>
					<input type="hidden" name="sender_ent_id" value=""/>
					<input type="hidden" name="cc_to_approver_ind" value="false"/>
					<input type="hidden" name="cc_to_approver_rol_ext_id" value=""/>
					<input type="hidden" name="msg_subject" value=""/>
					<input type="hidden" name="reminder_msg_subject" value=""/>
					<input type="hidden" name="bcc_to" value=""/>
					<input type="hidden" name="ji_no_change" value="false"/>
					<input type="hidden" name="reminder_no_change" value="false"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_ji">開課通知</xsl:with-param>
			<xsl:with-param name="lab_sch_details">時間表詳情</xsl:with-param>
			<xsl:with-param name="lab_ji_send_date">開課通知日期</xsl:with-param>
			<xsl:with-param name="lab_ji_reminder_date">提示日期</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_run_info"><xsl:value-of select="$lab_const_run"/>訊息</xsl:with-param>
			<xsl:with-param name="lab_change_to">更改為</xsl:with-param>
			<xsl:with-param name="lab_not_send">不發送</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_ji">开课通知</xsl:with-param>
			<xsl:with-param name="lab_sch_details">时间表详情</xsl:with-param>
			<xsl:with-param name="lab_ji_send_date">开课通知日期</xsl:with-param>
			<xsl:with-param name="lab_ji_reminder_date">提示日期</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_run_info"><xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_change_to">更改为</xsl:with-param>
			<xsl:with-param name="lab_not_send">不发送</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_ji">Joining instruction</xsl:with-param>
			<xsl:with-param name="lab_sch_details">Schedule details</xsl:with-param>
			<xsl:with-param name="lab_ji_send_date">Joining instruction date</xsl:with-param>
			<xsl:with-param name="lab_ji_reminder_date">Reminder date</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_run_info"><xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
			<xsl:with-param name="lab_change_to">Change to</xsl:with-param>
			<xsl:with-param name="lab_not_send">Do not send</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_ji"/>
		<xsl:param name="lab_ji_send_date"/>
		<xsl:param name="lab_ji_reminder_date"/>
		<xsl:param name="lab_sch_details"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_change_to"/>
		<xsl:param name="lab_not_send"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>

	<xsl:call-template name="itm_action_nav">
		<xsl:with-param  name="cur_node_id">112</xsl:with-param>
	</xsl:call-template>
	<div class="wzb-item-main">
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
			   <xsl:choose>
			      <xsl:when test="@run_ind = 'false'">
			        
			        <a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
			          <xsl:value-of select="title"/>
			        </a>&#160;&gt;&#160;<xsl:value-of select="$lab_ji"/>
			     </xsl:when>
			     <xsl:otherwise>
			       <xsl:apply-templates select="nav/item" mode="nav">
			       <xsl:with-param name="lab_run_info" select="$lab_run_info"/>
			       <xsl:with-param name="lab_session_info" select="$lab_run_info"/>
			       </xsl:apply-templates><span class="NavLink"><xsl:text>&#160;&gt;&#160;</xsl:text>
			       <xsl:value-of select="$lab_ji"/></span>
			     </xsl:otherwise>
			 </xsl:choose>
		 </xsl:with-param>
		</xsl:call-template>
		<table>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_ji_send_date"/>：
				</td>
				<td class="wzb-form-control">
						<xsl:if test="ji_message/message[@type = 'JI']">
							<xsl:choose>
								<xsl:when test="(ji_message/message[@type = 'JI']/@target_date = '') or (ji_message/message[@type = 'JI']/@target_date = 'null')">
									<input type="radio" name="ji_value" value="new"/>&#160;
									<xsl:call-template name="display_form_input_time">
										<xsl:with-param name="timestamp"/>
										<xsl:with-param name="format">2</xsl:with-param>
										<xsl:with-param name="fld_name">ji</xsl:with-param>
										<xsl:with-param name="focus_rad_btn_name">ji_value[0]</xsl:with-param>
										<xsl:with-param name="hidden_fld_name">ji_target_datetime</xsl:with-param>
										<xsl:with-param name="display_form_input_hhmm">Y</xsl:with-param>
										<xsl:with-param name="def_time">23:59:59</xsl:with-param>
									</xsl:call-template>
									<br/>
									<label for="rdo_ji_value_never1">
									<input id="rdo_ji_value_never1" type="radio" name="ji_value" value="never" checked="checked"/>&#160;
									<xsl:value-of select="$lab_not_send"/>
									</label>
								</xsl:when>
								<xsl:otherwise>
									<input type="radio" name="ji_value" value="old" checked="checked"/>
									<xsl:call-template name="display_time">
										<xsl:with-param name="my_timestamp"><xsl:value-of select="ji_message/message[@type = 'JI']/@target_date"/></xsl:with-param>
										<xsl:with-param name="date_format">4</xsl:with-param>
										<xsl:with-param name="dis_time">T</xsl:with-param>
									</xsl:call-template>
									<input type="hidden" name="ji_target_datetime_old" value="{ji_message/message[@type = 'JI']/@target_date}"/>
									<br/>
									<input type="radio" name="ji_value" value="new"/>
									<xsl:value-of select="$lab_change_to"/>：
							<br/>
									<xsl:call-template name="display_form_input_time">
										<xsl:with-param name="timestamp"/>
										<xsl:with-param name="format">2</xsl:with-param>
										<xsl:with-param name="fld_name">ji</xsl:with-param>
										<xsl:with-param name="focus_rad_btn_name">ji_value[1]</xsl:with-param>
										<xsl:with-param name="hidden_fld_name">ji_target_datetime</xsl:with-param>
										<xsl:with-param name="display_form_input_hhmm">Y</xsl:with-param>
										<xsl:with-param name="def_time">23:59:59</xsl:with-param>
									</xsl:call-template>
									<br/>
									<label for="rdo_ji_value_never2">
									<input id="rdo_ji_value_never2" type="radio" name="ji_value" value="never"/>
									<xsl:value-of select="$lab_not_send"/>
									</label>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_ji_reminder_date"/>：
				</td>
				<td class="wzb-form-control">
						<xsl:if test="/applyeasy/item/ji_message/message[@type = 'REMINDER']">
							<xsl:choose>
								<xsl:when test="(/applyeasy/item/ji_message/message[@type = 'REMINDER']/@target_date = '') or (/applyeasy/item/ji_message/message[@type = 'REMINDER']/@target_date = 'null')">
									<input type="radio" name="reminder_value" value="new"/>&#160;
									<xsl:call-template name="display_form_input_time">
										<xsl:with-param name="timestamp"><xsl:value-of select="/applyeasy/item/ji_message/message[@type = 'REMINDER']/@target_date"/></xsl:with-param>
										<xsl:with-param name="format">2</xsl:with-param>
										<xsl:with-param name="fld_name">reminder</xsl:with-param>
										<xsl:with-param name="focus_rad_btn_name">reminder_value[0]</xsl:with-param>
										<xsl:with-param name="hidden_fld_name">ji_reminder_target_datetime</xsl:with-param>
										<xsl:with-param name="display_form_input_hhmm">Y</xsl:with-param>
										<xsl:with-param name="def_time">23:59:59</xsl:with-param>
									</xsl:call-template>
									<br/>
									<label for="rdo_reminder_value_never1">
									<input id="rdo_reminder_value_never1" type="radio" name="reminder_value" value="never" checked="checked"/>&#160;
									<xsl:value-of select="$lab_not_send"/>
									</label>
								</xsl:when>
								<xsl:otherwise>
									<input type="radio" name="reminder_value" value="old" checked="checked"/>
									<xsl:call-template name="display_time">
										<xsl:with-param name="my_timestamp"><xsl:value-of select="/applyeasy/item/ji_message/message[@type = 'REMINDER']/@target_date"/></xsl:with-param>
										<xsl:with-param name="date_format">4</xsl:with-param>
										<xsl:with-param name="dis_time">T</xsl:with-param>
									</xsl:call-template>
									<br/>
									<input type="radio" name="reminder_value" value="new"/>
									<input type="hidden" name="ji_reminder_target_datetime_old" value="{/applyeasy/item/ji_message/message[@type = 'REMINDER']/@target_date}"/>
									<xsl:value-of select="$lab_change_to"/>：
								<br/>
									<xsl:call-template name="display_form_input_time">
										<xsl:with-param name="timestamp"/>
										<xsl:with-param name="format">2</xsl:with-param>
										<xsl:with-param name="fld_name">reminder</xsl:with-param>
										<xsl:with-param name="focus_rad_btn_name">reminder_value[1]</xsl:with-param>
										<xsl:with-param name="hidden_fld_name">ji_reminder_target_datetime</xsl:with-param>
										<xsl:with-param name="display_form_input_hhmm">Y</xsl:with-param>
										<xsl:with-param name="def_time">23:59:59</xsl:with-param>
									</xsl:call-template>
									<br/>
									<label for="rdo_reminder_value_never1">
									<input id="rdo_reminder_value_never1" type="radio" name="reminder_value" value="never"/>
									<xsl:value-of select="$lab_not_send"/>
									</label>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
				</td>
			</tr>
			<tr>
				<td width="35%" align="right">
				</td>
				<td width="65%" align="left">
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.itm_ji_upd (document.frmXml,document.frmXml.itm_id.value, '<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_target">_parent</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_target">_parent</xsl:with-param>
			</xsl:call-template>
		</div>
	</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="item" mode="nav">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_session_info"/>
		<xsl:choose>
			<xsl:when test="@run_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=last()">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.get_item_run_list({$value})" class="NavLink">
					<xsl:choose>
						<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
					</xsl:choose>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:when test="@session_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=last()">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.session.get_session_list({$value})" class="NavLink">
					<xsl:value-of select="$lab_session_info"/>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a href="javascript:itm_lst.get_item_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
