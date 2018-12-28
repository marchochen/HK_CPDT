<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_gen_input_file.xsl"/>

	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/display_filetype_icon.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="wb_const.xsl"/>
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
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
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
					
						feedUrl(document.frmXml);
						fm.edit_facility_details_exec(document.frmXml,']]><xsl:value-of select="$wb_lang"/><![CDATA[')
					}

					// added to upload facility thumbnail
					function feedUrl(frm) {
						if (frm.fac_url_type) {
							if (frm.fac_url_type[0].checked) {
								frm.fac_url.value = frm.fac_url_upload.value;
							}
							if (frm.fac_url_type[1].checked) {
								frm.fac_url.value = frm.fac_url_keep.value;
							}
						}
					}
			]]></script>
				<xsl:call-template name="new_css"/>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" >
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
		<!-- "keep_file" label added to upload facility thumbnail -->
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_room_detail">房間詳細資料</xsl:with-param>
			<xsl:with-param name="lab_basic_information">基本資料</xsl:with-param>
			<xsl:with-param name="lab_title">標題:</xsl:with-param>
			<xsl:with-param name="lab_description">簡介:</xsl:with-param>
			<xsl:with-param name="lab_status">狀態:</xsl:with-param>
			<xsl:with-param name="lab_remarks">備註:</xsl:with-param>
			<xsl:with-param name="lab_additional_information">其他資料</xsl:with-param>
			<xsl:with-param name="lab_size">面積:</xsl:with-param>
			<xsl:with-param name="lab_capacity">可容納人數:</xsl:with-param>
			<xsl:with-param name="lab_white_board">白板:</xsl:with-param>
			<xsl:with-param name="lab_network_port">網絡埠:</xsl:with-param>
			<xsl:with-param name="lab_power_port">插頭:</xsl:with-param>
			<xsl:with-param name="lab_projector_screen">投影機螢幕:</xsl:with-param>
			<xsl:with-param name="lab_lcd_projector">液晶體投影機:</xsl:with-param>
			<xsl:with-param name="lab_other_facilities">其他設施:</xsl:with-param>
			<xsl:with-param name="lab_status_on">可用於預訂</xsl:with-param>
			<xsl:with-param name="lab_status_off">不可用於預訂</xsl:with-param>
			<xsl:with-param name="lab_url">縮略圖URL:</xsl:with-param>
			<xsl:with-param name="lab_thumbnail_type">縮略圖類型:</xsl:with-param>
			<xsl:with-param name="lab_full_scene">360&#176; 錄像</xsl:with-param>
			<xsl:with-param name="lab_image">圖片</xsl:with-param>
			<xsl:with-param name="lab_keep_file">保留現有文件：</xsl:with-param>
			<xsl:with-param name="lab_cost">費用:</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_room_detail">房间详细资料</xsl:with-param>
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
			<xsl:with-param name="lab_keep_file">保存现有文件：</xsl:with-param>
			<xsl:with-param name="lab_cost">费用：</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
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
			<xsl:with-param name="lab_url">Thumbnail image :</xsl:with-param>
			<xsl:with-param name="lab_thumbnail_type">Thumbnail type :</xsl:with-param>
			<xsl:with-param name="lab_full_scene">360&#176; SCENE</xsl:with-param>
			<xsl:with-param name="lab_image">IMAGE</xsl:with-param>
			<xsl:with-param name="lab_keep_file">Keep :</xsl:with-param>
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
		<xsl:param name="lab_url"/>
		<xsl:param name="lab_thumbnail_type"/>
		<xsl:param name="lab_full_scene"/>
		<xsl:param name="lab_image"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_keep_file"/>
		<xsl:param name="lab_cost"/>
		
		<xsl:call-template name="fm_head"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_room_detail"/>
		</xsl:call-template>
		<input type="hidden" name="fac_id" value="{facility/@id}"/>
		<input type="hidden" name="stylesheet" value="{facility/facility_type/@xsl_prefix}"/>
		<input type="hidden" name="fac_upd_timestamp" value="{facility/update_user/@timestamp}"/>
		<input type="hidden" name="url_success"/>
		<input type="hidden" name="url_failure"/>
		<input type="hidden" name="fac_add_xml"/>
		<input type="hidden" name="cmd"/>
		<input type="hidden" name="module"/>
		<input type="hidden" name="fac_url"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_basic_information"/>
		</xsl:call-template>
		<table>
			<!-- for basic information -->
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_title"/>
				</td>
				<td class="wzb-form-control" valign="top">
					<xsl:variable name="_title">
						<xsl:call-template name="escape_js">
							<xsl:with-param name="input_str"><xsl:value-of select="facility/basic/title"/></xsl:with-param>
						</xsl:call-template>
					</xsl:variable>
					<input maxlength="255" style="width:300px;" name="fac_title" type="text" class="wzb-inputText"/>
					<script><![CDATA[
						document.frmXml.fac_title.value = ']]><xsl:value-of select="$_title"/><![CDATA['
					]]></script>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_description"/>
				</td>
				<td class="wzb-form-control" valign="top">
					<textarea name="fac_desc" rows="4" style="width:300px" class="wzb-inputTextArea">
						<xsl:value-of select="facility/basic/facility_desc"/>
					</textarea>
				</td>
			</tr>
			<tr> 
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_cost"/>
					<input name="lab_fac_fee" type="hidden" value="{$lab_cost_name}"/>
				</td>
				<td class="wzb-form-control" valign="top">
					<input maxlength="12" style="width:200px;" name="fac_fee" type="text" class="wzb-inputText">
						<xsl:if test="facility/basic/fac_fee > 0">
							<xsl:attribute name="value"><xsl:value-of select="facility/basic/fac_fee"/></xsl:attribute>
						</xsl:if>
					</input>				
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_status"/>
				</td>
				<td class="wzb-form-control" valign="top">
					<input  name="status" type="radio" value="ON">
						<xsl:if test="(facility/@status = 'ON')  or (facility/@status = '')">
						<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</input><xsl:value-of select="$lab_status_on"/><br/>
					<input type="radio" name="status" value="OFF">
						<xsl:if test="facility/@status = 'OFF'">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</input><xsl:value-of select="$lab_status_off"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_url"/>
				</td>
				<td class="wzb-form-control" valign="top">
					<!--
					<xsl:variable name="_url">
						<xsl:call-template name="escape_js">
							<xsl:with-param name="input_str"><xsl:value-of select="facility/basic/url"/></xsl:with-param>
						</xsl:call-template>
					</xsl:variable>
					<input maxlength="255" style="width:300px;" name="fac_url" type="file"/>
					<script><![CDATA[
						document.frmXml.fac_url.value = ']]><xsl:value-of select="$_url"/><![CDATA['
					]]></script>
					<br/>
					-->
					<!-- modified to upload facility thumbnail -->
					<xsl:variable name="url"><xsl:value-of select="concat('../',//fac_dir_url,'/', facility/@id, '/', facility/basic/url)"/></xsl:variable>
					<xsl:choose>
						<xsl:when test="facility/basic/url = ''">
							<!-- uploade new file -->
							<!-- <input maxlength="255" style="width:300px;" name="fac_url_upload" type="file" class="wzb-inputText"/>  -->
						     <xsl:call-template name="wb_gen_input_file">
									<xsl:with-param name="name">fac_url_upload</xsl:with-param>
									<xsl:with-param name="onfocus">javascript:get_focus()</xsl:with-param>
								</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<!-- uploade new file -->
							<input type="radio" style="float:left;" name="fac_url_type" value="UPLOAD" checked="checked"/>
							<!-- <input maxlength="255" style="width:300px;" name="fac_url_upload" type="file" onfocus="this.form.fac_url_type[0].checked=true" class="wzb-inputText"/> -->
							<div style="margin:-6px -4px 0px 22px;">
							<xsl:call-template name="wb_gen_input_file">
									<xsl:with-param name="name">fac_url_upload</xsl:with-param>
									<xsl:with-param name="onfocus">this.form.fac_url_type[0].checked=true</xsl:with-param>
								</xsl:call-template>
							</div>
							<br/>
							<!-- keep the current file-->
							<input type="radio" name="fac_url_type" value="KEEP" checked="checked"/>
							<xsl:value-of select="$lab_keep_file"/>
							<a href="{$url}" target="_blank">
								<xsl:call-template name="display_filetype_icon">
									<xsl:with-param name="fileName">
										<xsl:value-of select="$url"/>
									</xsl:with-param>
									<xsl:with-param name="vspace">0</xsl:with-param>
									<xsl:with-param name="align">absmiddle</xsl:with-param>
								</xsl:call-template>
							</a>
							<input type="hidden" name="fac_url_keep" value="{facility/basic/url}"/>
						</xsl:otherwise>
					</xsl:choose>
					<!-- ********** -->
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_remarks"/>
				</td>
				<td class="wzb-form-control" valign="top">
					<textarea name="fac_remarks" rows="4" style="width:300px" class="wzb-inputTextArea">
						<xsl:value-of select="facility/basic/remarks"/>
					</textarea>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_additional_information"/>
		</xsl:call-template>
	<!-- 	<xsl:call-template name="wb_ui_line"/>  -->
		<table >
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_size"/>
				</td>
				<td class="wzb-form-control" valign="top">
					<xsl:variable name="_size">
						<xsl:call-template name="escape_js">
							<xsl:with-param name="input_str"><xsl:value-of select="facility/additional/addfac_size"/></xsl:with-param>
						</xsl:call-template>
					</xsl:variable>
					<input maxlength="120" style="width:300px;" name="addfac_size" type="text" class="wzb-inputText"/>
					<script><![CDATA[
						document.frmXml.addfac_size.value = ']]><xsl:value-of select="$_size"/><![CDATA['
					]]></script>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_capacity"/>
				</td>
				<td class="wzb-form-control" valign="top">
					<textarea name="addfac_capacity" rows="4" style="width:300px" class="wzb-inputTextArea">
						<xsl:value-of select="facility/additional/addfac_capacity"/>
					</textarea>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_white_board"/>
				</td>
				<td class="wzb-form-control" valign="top">
					<xsl:variable name="_white_board">
						<xsl:call-template name="escape_js">
							<xsl:with-param name="input_str"><xsl:value-of select="facility/additional/addfac_white_board"/></xsl:with-param>
						</xsl:call-template>
					</xsl:variable>
					<input maxlength="120" style="width:300px;" name="addfac_white_board" type="text" class="wzb-inputText"/>
					<script><![CDATA[
						document.frmXml.addfac_white_board.value = ']]><xsl:value-of select="$_white_board"/><![CDATA['
					]]></script>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_network_port"/>
				</td>
				<td class="wzb-form-control" valign="top">
					<xsl:variable name="_network_port">
						<xsl:call-template name="escape_js">
							<xsl:with-param name="input_str"><xsl:value-of select="facility/additional/addfac_network_port"/></xsl:with-param>
						</xsl:call-template>
					</xsl:variable>
					<input maxlength="120" style="width:300px;" name="addfac_network_port" type="text" class="wzb-inputText"/>
					<script><![CDATA[
						document.frmXml.addfac_network_port.value = ']]><xsl:value-of select="$_network_port"/><![CDATA['
					]]></script>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_power_port"/>
				</td>
				<td class="wzb-form-control" valign="top">
					<xsl:variable name="_power_port">
						<xsl:call-template name="escape_js">
							<xsl:with-param name="input_str"><xsl:value-of select="facility/additional/addfac_power_port"/></xsl:with-param>
						</xsl:call-template>
					</xsl:variable>
					<input maxlength="120" style="width:300px;" name="addfac_power_port" type="text" class="wzb-inputText"/>
					<script><![CDATA[
						document.frmXml.addfac_power_port.value = ']]><xsl:value-of select="$_power_port"/><![CDATA['
					]]></script>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_projector_screen"/>
				</td>
				<td class="wzb-form-control" valign="top">
					<xsl:variable name="_projector_screen">
						<xsl:call-template name="escape_js">
							<xsl:with-param name="input_str"><xsl:value-of select="facility/additional/addfac_projector_screen"/></xsl:with-param>
						</xsl:call-template>
					</xsl:variable>
					<input maxlength="120" style="width:300px;" name="addfac_projector_screen" type="text" class="wzb-inputText"/>
					<script><![CDATA[
						document.frmXml.addfac_projector_screen.value = ']]><xsl:value-of select="$_projector_screen"/><![CDATA['
					]]></script>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_lcd_projector"/>
				</td>
				<td class="wzb-form-control" valign="top">
					<xsl:variable name="_LCD_projector">
						<xsl:call-template name="escape_js">
							<xsl:with-param name="input_str"><xsl:value-of select="facility/additional/addfac_LCD_projector"/></xsl:with-param>
						</xsl:call-template>
					</xsl:variable>
					<input maxlength="120" style="width:300px;" name="addfac_LCD_projector" type="text" class="wzb-inputText"/>
					<script><![CDATA[
						document.frmXml.addfac_LCD_projector.value = ']]><xsl:value-of select="$_LCD_projector"/><![CDATA['
					]]></script>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_other_facilities"/>
				</td>
				<td class="wzb-form-control" valign="top">
					<textarea name="addfac_other_facilities" rows="4" style="width:300px" class="wzb-inputTextArea">
						<xsl:value-of select="facility/additional/addfac_other_facilities"/>
					</textarea>
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
