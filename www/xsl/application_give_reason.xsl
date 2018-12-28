<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="share/itm_gen_details_share.xsl"/>
	
<xsl:strip-space elements="*"/>
<xsl:output indent="yes"/>
<!-- =============================================================== -->
<xsl:variable name="itm_id" select="applyeasy/item/@id"/>
<xsl:variable name="app_id" select="/applyeasy/application/@id"/>
<xsl:variable name="app_name" select="/applyeasy/application/applicant/display_name"/>
<xsl:variable name="app_update_timestamp" select="/applyeasy/application/@update_datetime"/>
<!-- =============================================================== -->
<xsl:template match="/">
	<html><xsl:call-template name="main"/></html>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="main">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		<title><xsl:value-of select="$wb_wizbank"/></title>
		<xsl:call-template name="wb_css"><xsl:with-param name="view">wb_ui</xsl:with-param></xsl:call-template>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
		<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
		<script language="Javascript" type="text/javascript"><![CDATA[
				app =  new wbApplication		
				itm_lst = new wbItem
		]]></script>
	</head>
	<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
		<form name="frmAction">
			<input type="hidden" name="cmd"/>
			<input type="hidden" name="itm_id" value="{$itm_id}"/>
			<input type="hidden" name="app_id" value="{$app_id}"/>
			<input type="hidden" name="app_id_lst"/>
			<input type="hidden" name="app_upd_timestamp_lst"/>
			<input type="hidden" name="upd_timestamp" value="{$app_update_timestamp}"/>
			<input type="hidden" name="process_id"/>
			<input type="hidden" name="status_id"/>
			<input type="hidden" name="action_id"/>
			<input type="hidden" name="fr"/>
			<input type="hidden" name="to"/>
			<input type="hidden" name="verb"/>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<xsl:call-template name="wb_init_lab"/>
		</form>
	</body>
</xsl:template>
<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates>
			<xsl:with-param name="lab_enrollment_for_workflow"><xsl:value-of select="$lab_const_enrollment"/></xsl:with-param>
			<xsl:with-param name="lab_remarks">原因</xsl:with-param>
			<xsl:with-param name="lab_learner">學員</xsl:with-param>
			<xsl:with-param name="lab_course">課程</xsl:with-param>
			<xsl:with-param name="lab_instruction">所輸入的原因只會向上司及培訓管理員顯示。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm">確認</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates>
			<xsl:with-param name="lab_enrollment_for_workflow"><xsl:value-of select="$lab_const_enrollment"/></xsl:with-param>
			<xsl:with-param name="lab_remarks">原因</xsl:with-param>
			<xsl:with-param name="lab_learner">学员</xsl:with-param>
			<xsl:with-param name="lab_course">课程</xsl:with-param>
			<xsl:with-param name="lab_instruction">所输入的原因只会向领导及培训管理员显示。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm">确认</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates>
			<xsl:with-param name="lab_enrollment_for_workflow"><xsl:text> </xsl:text><xsl:value-of select="$lab_const_enrollment"/></xsl:with-param>
			<xsl:with-param name="lab_remarks">Remarks</xsl:with-param>
			<xsl:with-param name="lab_learner">Learner</xsl:with-param>
			<xsl:with-param name="lab_course">Course</xsl:with-param>
			<xsl:with-param name="lab_instruction">This remarks will be displayed to supervisors and training administrators only.</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm">Confirm</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
<!-- =============================================================== -->
<xsl:template match="applyeasy">
	<xsl:param name="lab_enrollment_for_workflow"/>
	<xsl:param name="lab_remarks"/>
	<xsl:param name="lab_learner"/>
	<xsl:param name="lab_course"/>
	<xsl:param name="lab_instruction"/>
	<xsl:param name="lab_g_form_btn_confirm"/>
	<xsl:param name="lab_g_form_btn_cancel"/>
	<xsl:call-template name="wb_ui_hdr"/>
	
	<xsl:variable name="process_id" select="application/attempt/action_attempt/action/@process_id"/>
	<xsl:variable name="fr_status" select="application/attempt/action_attempt/action/@fr"/>
	<xsl:variable name="verb" select="application/attempt/action_attempt/action/@verb"/>
	
	<xsl:variable name="action_name">
		<xsl:value-of select="workflow/process[@id = $process_id]/status[@name = $fr_status]/action[@verb = $verb]/@name"/>
	</xsl:variable>
	<xsl:call-template name="wb_ui_title">
		<xsl:with-param name="text"><xsl:value-of select="$action_name"/><xsl:value-of select="$lab_enrollment_for_workflow"/>
		</xsl:with-param>
	</xsl:call-template>
	<xsl:call-template name="wb_ui_line"/>
	
	<xsl:if test="count(application/attempt/action_attempt/action)!= 0">
		<xsl:apply-templates select="application/attempt/action_attempt/action" mode="action_comment">
			<xsl:with-param name="lab_remarks" select="$lab_remarks"/>
			<xsl:with-param name="lab_learner" select="$lab_learner"/>
			<xsl:with-param name="lab_course" select="$lab_course"/>
			<xsl:with-param name="lab_instruction" select="$lab_instruction"/>
			<xsl:with-param name="lab_g_form_btn_confirm" select="$lab_g_form_btn_confirm"/>
			<xsl:with-param name="lab_g_form_btn_cancel" select="$lab_g_form_btn_cancel"/>
		</xsl:apply-templates>
	</xsl:if>		
	<xsl:call-template name="wb_ui_footer"/>
</xsl:template>
 <!-- =============================================================== -->
<xsl:template match="*" mode="action_comment">
	<xsl:param name="lab_remarks"/>
	<xsl:param name="lab_learner"/>
	<xsl:param name="lab_course"/>
	<xsl:param name="lab_instruction"/>
	<xsl:param name="lab_g_form_btn_confirm"/>
	<xsl:param name="lab_g_form_btn_cancel"/>
	<table cellspacing="0" cellpadding="3" border="0" width="{$wb_gen_table_width}" class="Bg">
		<tr>
			<td width="20%" align="right" height="10" ><img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/></td>
			<td width="80%"  height="10"><img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/></td>
		</tr>
		<tr>
			<td width="20%" align="right"  valign="top"><span class="TitleText"><xsl:value-of select="$lab_learner"/>:</span></td>
			<td width="80%" ><xsl:value-of select="$app_name"/></td>
		</tr>
		<tr>
			<td width="20%" align="right"  valign="top"><span class="TitleText"><xsl:value-of select="$lab_course"/>:</span></td>
			<td width="80%" >
				<xsl:choose>
					<xsl:when test="/appleyeasy/item_template/item[@id=$itm_id]/@run_ind='true'"><xsl:value-of select="item/title"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="/applyeasy/item/title"/></xsl:otherwise>
				</xsl:choose>		
			</td>
		</tr>
		<tr>
			<td width="20%" align="right"  valign="top"><span class="TitleText"><xsl:value-of select="$lab_remarks"/>:</span></td>
			<td width="80%" ><textarea class="wzb-inputTextArea" rows="5" cols="20" style="width:400px;" name="content"/></td>
		</tr>
		<tr>
			<td width="20%" align="right" height="10" ><img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/></td>
			<td width="80%" ><xsl:value-of select="$lab_instruction"/></td>
		</tr>
		<tr>
			<td width="20%" align="right" height="10" ><img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/></td>
			<td width="80%"  height="10"><img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/></td>
		</tr>
	</table>
	<xsl:call-template name="wb_ui_line"/>
	<table cellpadding="3" cellspacing="0" width="{$wb_gen_table_width}" border="0">
		<tr>
			<td align="center">
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_confirm"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:app.lrn_ins_reason_exec(document.frmAction)</xsl:with-param>
				</xsl:call-template>
				<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:app.lrn_cancel_ins_reason_exec(document.frmAction)</xsl:with-param>
				</xsl:call-template>
			</td>
		</tr>
	</table>
</xsl:template>
<!-- =============================================================== -->
</xsl:stylesheet>
