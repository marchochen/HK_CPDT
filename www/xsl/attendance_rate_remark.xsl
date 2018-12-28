<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<!-- =============================================================== -->
	<xsl:variable name="wb_gen_table_width">600</xsl:variable>
	<xsl:variable name="itm_id" select="/evalmanagement/item/@id"/>
	<xsl:variable name="app_id_lst" select="/evalmanagement/app_id_lst"/>
	<xsl:variable name="att_rate_status" select="/evalmanagement/att_rate_status"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<head>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				
				<xsl:call-template name="new_css" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
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
				
				<script language="JavaScript" TYPE="text/javascript"><![CDATA[
					var attd = new wbAttendance
					function submit_remark(lang,txtFldName){
						frm = window.opener.document.frmXml;
						frm1 = document.frmXml;
						frm1.attd_remark.value = wbUtilsTrimString(frm1.attd_remark.value);
						frm1.attd_rate.value = wbUtilsTrimString(frm1.attd_rate.value);
						
						if(getChars(document.frmXml.attd_remark.value) > 400){
							Dialog.alert(fetchLabel("course_remarks") + fetchLabel("label_title_length_warn_400"),function(){
								frm1.attd_remark.focus();
							});
							return false;			
						}else if(!gen_validate_pencentage(frm1.attd_rate,txtFldName,lang)){
							frm1.attd_rate.focus();
							return false;
						}else{
							window.opener.submit_remark(frm,frm1.attd_remark.value,frm1.attd_rate.value);
						}
						self.close();
					}
			
			]]></script>
			</head>
			<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
				<form name="frmXml">
					<xsl:call-template name="wb_init_lab"/>
					<input type="hidden" name="cmd" value=""/>
					<input type="hidden" name="url_success" value=""/>
					<input type="hidden" name="url_failure" value=""/>
					<input type="hidden" name="app_id_lst" value=""/>
					<input type="hidden" name="itm_id" value="{$itm_id}"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="evalmanagement">
			<xsl:with-param name="lab_mark_as">記為</xsl:with-param>
			<xsl:with-param name="lab_attend">已出席</xsl:with-param>
			<xsl:with-param name="lab_attendpartly">部分已出席</xsl:with-param>
			<xsl:with-param name="lab_absent">缺席</xsl:with-param>
			<xsl:with-param name="lab_attd_rate">出席率</xsl:with-param>
			<xsl:with-param name="lab_remark">備註</xsl:with-param>
			<xsl:with-param name="lab_no_more">(不超過400個字元)</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_go">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="evalmanagement">
			<xsl:with-param name="lab_mark_as">记为</xsl:with-param>
			<xsl:with-param name="lab_attend">已出席</xsl:with-param>
			<xsl:with-param name="lab_attendpartly">部分已出席</xsl:with-param>
			<xsl:with-param name="lab_absent">缺席</xsl:with-param>
			<xsl:with-param name="lab_attd_rate">出席率</xsl:with-param>
			<xsl:with-param name="lab_remark">备注</xsl:with-param>
			<xsl:with-param name="lab_no_more">(不超过400个字符)</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_go">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="evalmanagement">
			<xsl:with-param name="lab_mark_as">Mark as</xsl:with-param>
			<xsl:with-param name="lab_attend">attended</xsl:with-param>
			<xsl:with-param name="lab_attendpartly"> partly attended</xsl:with-param>
			<xsl:with-param name="lab_absent"> absent</xsl:with-param>
			<xsl:with-param name="lab_attd_rate">Attendance rate</xsl:with-param>
			<xsl:with-param name="lab_remark">Remark</xsl:with-param>
			<xsl:with-param name="lab_no_more">(Not more than 400 characters)</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_go">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="evalmanagement">
		<xsl:param name="lab_mark_as"/>
		<xsl:param name="lab_attend"/>
		<xsl:param name="lab_attendpartly"/>
		<xsl:param name="lab_absent"/>
		<xsl:param name="lab_attd_rate"/>
		<xsl:param name="lab_remark"/>
		<xsl:param name="lab_no_more"/>
		<xsl:param name="lab_g_form_btn_go"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_mark_as"/>
				<xsl:choose>
					<xsl:when test="att_rate_status='1'">
						<xsl:text> 100% </xsl:text><xsl:value-of select="$lab_attend"/>
					</xsl:when>
					<xsl:when test="att_rate_status='3'">
						<xsl:value-of select="$lab_absent"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_attendpartly"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="text_class">margin-left10 margin-top0 wzb-title-2</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table  width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="0">
			<xsl:choose>
				<xsl:when test="att_rate_status='1'">
					<input type="hidden" name="attd_rate" value="100"/>
				</xsl:when>
				<xsl:when test="att_rate_status='3' ">
					<input type="hidden" name="attd_rate" value="0"/>
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td width="20%" align="right" class="wzb-form-label">
							<span class="TitleText">
								<xsl:value-of select="$lab_attd_rate"/>：</span>
						</td>
						<td width="80%"  class="wzb-form-control">
							<span class="Text">
								<input type="text" name="attd_rate" maxlength="2" size="2" class="inputFrm"/>
								<xsl:text>&#160;%</xsl:text>
							</span>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
			<tr>
				<td width="20%" align="right" valign="top"  class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_remark"/>：</span>
				</td>
				<td width="80%"  class="wzb-form-control">
					<span class="Text">
						<textarea class="InputFrm" name="attd_remark" style="width:300px;" rows="6"/>
					</span>
				</td>
			</tr>
			<tr>
				<td><img height="1" width="1" src="{$wb_img_path}tp.gif"/></td>
				<td class="color-gray999"><xsl:value-of select="$lab_no_more"/></td>
			</tr>
			
		</table>
		<div class="wzb-bar">
			<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="5">
				<tr>
					<td align="center">
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_go"/>
							<xsl:with-param name="wb_gen_btn_href">submit_remark('<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$lab_attd_rate"/>')</xsl:with-param>
						</xsl:call-template>
						<xsl:text>&#160;</xsl:text>
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:parent.close()</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</table>
		</div>		
		<script type="text/javascript" language="javascript">
		<![CDATA[
			function _go() {
				var btns = document.getElementsByName("frmSubmitBtn");
				return submit_remark(']]><xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$lab_attd_rate"/><![CDATA[');
			}
			frmXml.onsubmit = _go;]]>
		</script>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- =============================================================== -->
</xsl:stylesheet>
