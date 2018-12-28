<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_gen_input_file.xsl"/>

	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>

	<xsl:import href="share/fm_share.xsl"/>
	<xsl:output  indent="yes"/>
	<xsl:strip-space elements="*"/>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_cost_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '805')"/>
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
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				
					var isExcludes = getUrlParam('isExcludes');
					fm = new wbFm(isExcludes);
					
										
					function _ValidateAdditionalInfoFrm(frm,lang){
						if (frm.addfac_capacity.value.length > 0 && frm.addfac_capacity.value.length > 1000){
							alert(eval('wb_msg_'+lang+'_fm_capacity_too_long'))
							frm.addfac_capacity.focus()
							return false	
						}

						if (frm.addfac_other_facilities.value.length > 0 && frm.addfac_other_facilities.value.length > 1000){
								alert(eval('wb_msg_'+lang+'_fm_other_fac_too_long'))
								frm.addfac_other_facilities.focus()
								return false	
						}
						
						return true;					
					}
					
					function submit_frm(){
					
						document.frmXml.fac_add_xml.value = _wbFmAdditioalInfoXML(document.frmXml,'addfac_')
						fm.ins_facility_details_exec(document.frmXml,']]><xsl:value-of select="$wb_lang"/><![CDATA[')
					}
			]]></script>
				<xsl:call-template name="new_css"/>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<!-- navigation -->
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_FACILITY_MGT</xsl:with-param>
					<xsl:with-param name="parent_code">FTN_AMD_FACILITY_INFO</xsl:with-param>
				</xsl:call-template>
				<form name="frmXml" enctype="multipart/form-data">
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_ins_room">添加房間</xsl:with-param>
			<xsl:with-param name="lab_basic_information">基本資料</xsl:with-param>
			<xsl:with-param name="lab_title">標題：</xsl:with-param>
			<xsl:with-param name="lab_description">簡介：</xsl:with-param>
			<xsl:with-param name="lab_status">狀態：</xsl:with-param>
			<xsl:with-param name="lab_remarks">備註：</xsl:with-param>
			<xsl:with-param name="lab_additional_information">其他資料</xsl:with-param>
			<xsl:with-param name="lab_size">面積：</xsl:with-param>
			<xsl:with-param name="lab_capacity">可容納人數：</xsl:with-param>
			<xsl:with-param name="lab_white_board">白板：</xsl:with-param>
			<xsl:with-param name="lab_network_port">網口：</xsl:with-param>
			<xsl:with-param name="lab_power_port">插頭：</xsl:with-param>
			<xsl:with-param name="lab_projector_screen">投影螢幕：</xsl:with-param>
			<xsl:with-param name="lab_lcd_projector">膠片投影儀：</xsl:with-param>
			<xsl:with-param name="lab_other_facilities">其他設施：</xsl:with-param>
			<xsl:with-param name="lab_status_on">可用於預訂</xsl:with-param>
			<xsl:with-param name="lab_status_off">不可用於預訂</xsl:with-param>
			<xsl:with-param name="lab_url">縮略圖URL：</xsl:with-param>
			<xsl:with-param name="lab_thumbnail_type">縮略圖類型：</xsl:with-param>
			<xsl:with-param name="lab_full_scene">360&#176;錄影</xsl:with-param>
			<xsl:with-param name="lab_image">圖片</xsl:with-param>
			<xsl:with-param name="lab_star">*</xsl:with-param>
			<xsl:with-param name="lab_cost">費用:</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_ins_room">添加房间</xsl:with-param>
			<xsl:with-param name="lab_basic_information">基本资料</xsl:with-param>
			<xsl:with-param name="lab_title">标题：</xsl:with-param>
			<xsl:with-param name="lab_description">简介：</xsl:with-param>
			<xsl:with-param name="lab_status">状态：</xsl:with-param>
			<xsl:with-param name="lab_remarks">备注：</xsl:with-param>
			<xsl:with-param name="lab_additional_information">其他资料</xsl:with-param>
			<xsl:with-param name="lab_size">面积：</xsl:with-param>
			<xsl:with-param name="lab_capacity">可容纳人数：</xsl:with-param>
			<xsl:with-param name="lab_white_board">白板：</xsl:with-param>
			<xsl:with-param name="lab_network_port">网口：</xsl:with-param>
			<xsl:with-param name="lab_power_port">插头：</xsl:with-param>
			<xsl:with-param name="lab_projector_screen">投影屏幕：</xsl:with-param>
			<xsl:with-param name="lab_lcd_projector">胶片投影仪：</xsl:with-param>
			<xsl:with-param name="lab_other_facilities">其他设施：</xsl:with-param>
			<xsl:with-param name="lab_status_on">可用于预订</xsl:with-param>
			<xsl:with-param name="lab_status_off">不可用于预订</xsl:with-param>
			<xsl:with-param name="lab_url">缩略图URL：</xsl:with-param>
			<xsl:with-param name="lab_thumbnail_type">缩略图类型：</xsl:with-param>
			<xsl:with-param name="lab_full_scene">360&#176;录像</xsl:with-param>
			<xsl:with-param name="lab_image">图片</xsl:with-param>
			<xsl:with-param name="lab_star">*</xsl:with-param>
			<xsl:with-param name="lab_cost">费用：</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_ins_room">Insert room</xsl:with-param>
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
			<xsl:with-param name="lab_url">Thumbnail image :</xsl:with-param>
			<xsl:with-param name="lab_thumbnail_type">Thumbnail type :</xsl:with-param>
			<xsl:with-param name="lab_full_scene">360&#176; SCENE</xsl:with-param>
			<xsl:with-param name="lab_image">IMAGE</xsl:with-param>
			<xsl:with-param name="lab_star">*</xsl:with-param>
			<xsl:with-param name="lab_cost">Cost :</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="user">
		<xsl:param name="lab_ins_room"/>
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
		<xsl:param name="lab_url"/>
		<xsl:param name="lab_thumbnail_type"/>
		<xsl:param name="lab_full_scene"/>
		<xsl:param name="lab_image"/>
		<xsl:param name="lab_star"/>		
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_cost"/>
		
		<xsl:call-template name="fm_head"/>
			
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_ins_room"/>
		</xsl:call-template>
		<input type="hidden" name="fac_type"/>
		<input type="hidden" name="fac_add_xml"/>
		<input type="hidden" name="cmd"/>
		<input type="hidden" name="module"/>
		<input type="hidden" name="url_success"/>
		<input type="hidden" name="url_failure"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_basic_information"/>
		</xsl:call-template>
		<!-- <xsl:call-template name="wb_ui_line"/>  -->
		<table>
			<!-- for basic information -->
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star"><xsl:value-of select="$lab_star"/></span><xsl:value-of select="$lab_title"/>
				</td>
				<td class="wzb-form-control">
					<input maxlength="255" style="width:300px;" name="fac_title" type="text" class="wzb-inputText"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_description"/>
				</td>
				<td valign="top" class="wzb-form-control">
					<textarea name="fac_desc" rows="4" style="width:300px" class="wzb-inputTextArea"/>
				</td>
			</tr>
			<tr> 
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_cost"/>
					<input name="lab_fac_fee" type="hidden" value="{$lab_cost_name}"/>
				</td>
				<td valign="top" class="wzb-form-control">
					<input maxlength="12" style="width:200px;" name="fac_fee" type="text" class="wzb-inputText"/>				
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_status"/>
				</td>
				<td valign="top" class="wzb-form-control">
					<input  name="status" type="radio" value="ON" checked="checked" >

					</input><xsl:value-of select="$lab_status_on"/><br/>
					<input type="radio" name="status" value="OFF" />
					<xsl:value-of select="$lab_status_off"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_url"/>
				</td>
				<td valign="top" class="wzb-form-control">
				     <xsl:call-template name="wb_gen_input_file">
									<xsl:with-param name="name">fac_url</xsl:with-param>
									<xsl:with-param name="onfocus">javascript:get_focus()</xsl:with-param>
								</xsl:call-template>
				<!-- 	<input maxlength="255" style="width:300px;" name="fac_url" type="file" class="wzb-inputText"/>   -->
				</td>
			</tr>
			<!--<tr>
				<td class="wzb-form-label" valign="top">
					<span class="TitleText"><xsl:value-of select="$lab_thumbnail_type"/></span>
				</td>
				<td valign="top" width="80%">
					<xsl:variable name="fac_url_type" select="facility/basic/url"/>
					<span class="Text"><select class="Select" name="fac_url_type"><option value="IMAGE"><xsl:if test="$fac_url_type = 'image'"><xsl:attribute name="selected">selected</xsl:attribute></xsl:if><xsl:value-of select="$lab_image"/></option><option value="SCENE"><xsl:if test="$fac_url_type = 'scene'"><xsl:attribute name="selected">selected</xsl:attribute></xsl:if><xsl:value-of select="$lab_full_scene"/></option></select></span>
				</td>
			</tr>-->
			<input type="hidden" name="fac_url_type" value="IMAGE"/>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_remarks"/>
				</td>
				<td valign="top" class="wzb-form-control">
					<textarea name="fac_remarks" rows="4" style="width:300px" class="wzb-inputTextArea"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_additional_information"/>
		</xsl:call-template>
		<!-- <xsl:call-template name="wb_ui_line"/>  -->
		<table>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_size"/>
				</td>
				<td valign="top" class="wzb-form-control">
					<input maxlength="120" style="width:300px;" name="addfac_size" type="text" class="wzb-inputText"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_capacity"/>
				</td>
				<td class="wzb-form-control">
					<textarea name="addfac_capacity" rows="4" style="width:300px" class="wzb-inputTextArea"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_white_board"/>
				</td>
				<td class="wzb-form-control">
					<input maxlength="120" style="width:300px;" name="addfac_white_board" type="text" class="wzb-inputText"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_network_port"/>
				</td>
				<td class="wzb-form-control">
					<input maxlength="120" style="width:300px;" name="addfac_network_port" type="text" class="wzb-inputText"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_power_port"/>
				</td>
				<td class="wzb-form-control">
					<input maxlength="120" style="width:300px;" name="addfac_power_port" type="text" class="wzb-inputText"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_projector_screen"/>
				</td>
				<td class="wzb-form-control">
					<input maxlength="120" style="width:300px;" name="addfac_projector_screen" type="text" class="wzb-inputText"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_lcd_projector"/>
				</td>
				<td class="wzb-form-control">
					<input maxlength="120" style="width:300px;" name="addfac_LCD_projector" type="text" class="wzb-inputText"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_other_facilities"/>
				</td>
				<td class="wzb-form-control">
					<textarea name="addfac_other_facilities" rows="4" style="width:300px" class="wzb-inputTextArea"/>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:submit_frm()</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:window.history.back()</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
</xsl:stylesheet>
