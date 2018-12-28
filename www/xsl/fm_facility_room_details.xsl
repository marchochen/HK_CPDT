<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="share/fm_share.xsl"/>
	<xsl:output  indent="yes"/>
	<xsl:strip-space elements="*"/>
	<!-- ================================================================================ -->
	<xsl:variable name="page_variant_root" select="/fm/meta/page_variant"/>
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
					]]></script>
					<xsl:call-template name="new_css"/>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
				<form name="frmAction">
					<input type="hidden" name="url_success"/>
					<input type="hidden" name="module"/>
					<input type="hidden" name="cmd"/>
					<input type="hidden" name="fac_id"/>
					<input type="hidden" name="fac_upd_timestamp"/>
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_room_detail">房間詳細資料</xsl:with-param>
			<xsl:with-param name="lab_basic_information">基本資料</xsl:with-param>
			<xsl:with-param name="lab_title">標題:</xsl:with-param>
			<xsl:with-param name="lab_description">簡介:</xsl:with-param>
			<xsl:with-param name="lab_status">狀態:</xsl:with-param>
			<xsl:with-param name="lab_remarks">備註:</xsl:with-param>
			<xsl:with-param name="lab_additional_information">其他資料</xsl:with-param>
			<xsl:with-param name="lab_size">大小:</xsl:with-param>
			<xsl:with-param name="lab_capacity">可容納人數:</xsl:with-param>
			<xsl:with-param name="lab_white_board">白板:</xsl:with-param>
			<xsl:with-param name="lab_network_port">網絡埠:</xsl:with-param>
			<xsl:with-param name="lab_power_port">插頭:</xsl:with-param>
			<xsl:with-param name="lab_projector_screen">投影機螢幕:</xsl:with-param>
			<xsl:with-param name="lab_lcd_projector">LCD 投影機:</xsl:with-param>
			<xsl:with-param name="lab_other_facilities">其他設施:</xsl:with-param>
			<xsl:with-param name="lab_status_on">可用於預訂</xsl:with-param>
			<xsl:with-param name="lab_status_off">不可用於預訂</xsl:with-param>
			<xsl:with-param name="lab_status_deleted">已删除，不可用于預訂</xsl:with-param>
			<xsl:with-param name="lab_cost">費用:</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">删除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_room_detail">房间详细资料</xsl:with-param>
			<xsl:with-param name="lab_basic_information">基本资料</xsl:with-param>
			<xsl:with-param name="lab_title">标题：</xsl:with-param>
			<xsl:with-param name="lab_description">简介：</xsl:with-param>
			<xsl:with-param name="lab_status">状态：</xsl:with-param>
			<xsl:with-param name="lab_remarks">备注：</xsl:with-param>
			<xsl:with-param name="lab_additional_information">其他资料</xsl:with-param>
			<xsl:with-param name="lab_size">大小：</xsl:with-param>
			<xsl:with-param name="lab_capacity">可容纳人数：</xsl:with-param>
			<xsl:with-param name="lab_white_board">白板：</xsl:with-param>
			<xsl:with-param name="lab_network_port">网口：</xsl:with-param>
			<xsl:with-param name="lab_power_port">插头：</xsl:with-param>
			<xsl:with-param name="lab_projector_screen">投影屏幕：</xsl:with-param>
			<xsl:with-param name="lab_lcd_projector">LCD投影仪：</xsl:with-param>
			<xsl:with-param name="lab_other_facilities">其他设施：</xsl:with-param>
			<xsl:with-param name="lab_status_on">可用于预订</xsl:with-param>
			<xsl:with-param name="lab_status_off">不可用于预订</xsl:with-param>
			<xsl:with-param name="lab_status_deleted">已删除，不可用于预订</xsl:with-param>
			<xsl:with-param name="lab_cost">费用：</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_room_detail">Room details</xsl:with-param>
			<xsl:with-param name="lab_basic_information">Basic information</xsl:with-param>
			<xsl:with-param name="lab_title">Title :</xsl:with-param>
			<xsl:with-param name="lab_description">Description :</xsl:with-param>
			<xsl:with-param name="lab_status">Status :</xsl:with-param>
			<xsl:with-param name="lab_remarks">Remarks :</xsl:with-param>
			<xsl:with-param name="lab_additional_information">Additional information</xsl:with-param>
			<xsl:with-param name="lab_size">Size :</xsl:with-param>
			<xsl:with-param name="lab_capacity">Capacity :</xsl:with-param>
			<xsl:with-param name="lab_white_board">White board :</xsl:with-param>
			<xsl:with-param name="lab_network_port">Network port :</xsl:with-param>
			<xsl:with-param name="lab_power_port">Power port :</xsl:with-param>
			<xsl:with-param name="lab_projector_screen">Projector screen :</xsl:with-param>
			<xsl:with-param name="lab_lcd_projector">LCD projector :</xsl:with-param>
			<xsl:with-param name="lab_other_facilities">Other facilities :</xsl:with-param>
			<xsl:with-param name="lab_status_on">Available for reservation</xsl:with-param>
			<xsl:with-param name="lab_status_off">Unavailable for reservation</xsl:with-param>
			<xsl:with-param name="lab_status_deleted">Deleted. Unavailable for reservation</xsl:with-param>
			<xsl:with-param name="lab_cost">Cost :</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="fm">
		<xsl:param name="lab_room_detail"/>
		<xsl:param name="lab_basic_information"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_description"/>
		<xsl:param name="lab_remarks"/>
		<xsl:param name="lab_additional_information"/>
		<xsl:param name="lab_size"/>
		<xsl:param name="lab_capacity"/>
		<xsl:param name="lab_white_board"/>
		<xsl:param name="lab_network_port"/>
		<xsl:param name="lab_power_port"/>
		<xsl:param name="lab_projector_screen"/>
		<xsl:param name="lab_lcd_projector"/>
		<xsl:param name="lab_other_facilities"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_status_deleted"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_cost"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_room_detail"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_basic_information"/>
			<xsl:with-param name="extra_td">
				<td align="right">
					<xsl:if test="$page_variant_root/@hasEditFacBtn = 'true' ">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_edit"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:fm.edit_facility_details_prep(<xsl:value-of select="facility/@id"/>,'<xsl:value-of select="facility/facility_type/@xsl_prefix"/>')</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="$page_variant_root/@hasDelFacBtn = 'true' ">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_remove"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:fm.del_facility_details(document.frmAction,<xsl:value-of select="facility/@id"/>,'<xsl:value-of select="facility/update_user/@timestamp"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
				</td>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<!-- for basic information -->
			<tr>
				<td class="wzb-form-label" valign="top" >
					<xsl:value-of select="$lab_title"/>
				</td>
				<td valign="top" width="30%" style="padding:10px 0 10px 10px; color:#333;">
					<xsl:value-of select="facility/basic/title"/>
				</td>
				<td rowspan="5" valign="top" width="40%">
					<!-- for facility thumbnail uploaded -->
					<xsl:variable name="url">../<xsl:value-of select="concat('facility/', facility/@id, '/', facility/basic/url)"/></xsl:variable>
					<xsl:choose>
						<xsl:when test="facility/basic/url/@type = 'SCENE'">
							<applet codebase="../applet/upixscreen" code="uPixScreen.class" archive="uPixScreen.jar" width="400" height="150">
								<param name="url" value="{$url}"/>
								<param name="autoPan" value="yes"/>
								<param name="startYaw" value="0.0"/>
								<param name="startPitch" value="0.0"/>
								<param name="startFov" value="90.0"/>
								<param name="partialStitch" value="no"/>
								<param name="horFov" value="360.0"/>
							</applet>
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="facility/basic/url != ''">
									<img border="0" src="{$url}"/>
								</xsl:when>
								<xsl:otherwise>
									<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_description"/>
				</td>
				<td valign="top" width="30%" style="padding:10px 0 10px 10px; color:#333;">
					<xsl:call-template name="unescape_html_linefeed">
						<xsl:with-param name="my_right_value">
							<xsl:value-of select="facility/basic/facility_desc"/>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_cost"/>
				</td>
				<td valign="top" width="30%" style="padding:10px 0 10px 10px; color:#333;">
					<xsl:if test="facility/basic/fac_fee > 0">
						<xsl:value-of select="facility/basic/fac_fee"/>
					</xsl:if>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_status"/>
				</td>
				<td valign="top" width="30%" style="padding:10px 0 10px 10px; color:#333;">
					<xsl:choose>
						<xsl:when test="facility/@status = 'ON'">
							<xsl:value-of select="$lab_status_on"/>
						</xsl:when>
						<xsl:when test="facility/@status = 'OFF'">
							<xsl:value-of select="$lab_status_off"/>
						</xsl:when>
						<xsl:when test="facility/@status = 'DELETED'">
							<xsl:value-of select="$lab_status_deleted"/>
						</xsl:when>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_remarks"/>
				</td>
				<td valign="top" width="30%" style="padding:10px 0 10px 10px; color:#333;">
					<xsl:call-template name="unescape_html_linefeed">
						<xsl:with-param name="my_right_value">
							<xsl:value-of select="facility/basic/remarks"/>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_space"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_additional_information"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_size"/>
				</td>
				<td colspan="2" valign="top" class="wzb-form-control">
					<xsl:value-of select="facility/additional/addfac_size"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_capacity"/>
				</td>
				<td colspan="2" valign="top" class="wzb-form-control">
					<xsl:call-template name="unescape_html_linefeed">
						<xsl:with-param name="my_right_value">
							<xsl:value-of select="facility/additional/addfac_capacity"/>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="Text">
						<xsl:value-of select="$lab_white_board"/>
					</span>
				</td>
				<td colspan="2" valign="top" class="wzb-form-control">
					<xsl:value-of select="facility/additional/addfac_white_board"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_network_port"/>
				</td>
				<td colspan="2" valign="top"  class="wzb-form-control">
					<xsl:value-of select="facility/additional/addfac_network_port"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_power_port"/>
				</td>
				<td colspan="2" valign="top"  class="wzb-form-control">
					<xsl:value-of select="facility/additional/addfac_power_port"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_projector_screen"/>
				</td>
				<td colspan="2" valign="top"  class="wzb-form-control">
					<xsl:value-of select="facility/additional/addfac_projector_screen"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_lcd_projector"/>
				</td>
				<td colspan="2" valign="top"  class="wzb-form-control">
					<xsl:value-of select="facility/additional/addfac_LCD_projector"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_other_facilities"/>
				</td>
				<td colspan="2" valign="top" class="wzb-form-control">
					<xsl:call-template name="unescape_html_linefeed">
						<xsl:with-param name="my_right_value">
							<xsl:value-of select="facility/additional/addfac_other_facilities"/>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_close"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:parent.close()</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
</xsl:stylesheet>