<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl" />
	<xsl:import href="cust/wb_cust_const.xsl" />
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl" />
	<xsl:import href="utils/wb_gen_form_button.xsl" />
	<xsl:import href="utils/wb_css.xsl" />
	<xsl:import href="utils/wb_ui_hdr.xsl" />
	<xsl:import href="utils/wb_ui_title.xsl" />
	<xsl:import href="utils/wb_ui_line.xsl" />
	<xsl:import href="utils/wb_ui_footer.xsl" />
	<xsl:import href="utils/wb_ui_head.xsl" />
	<xsl:import href="utils/wb_goldenman.xsl" />
	<xsl:import href="utils/wb_gen_input_file.xsl" />
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<!-- cust utils -->
	<xsl:output indent="yes" />
	<xsl:variable name="tc_enabled" select="/certificate_mod/meta/tc_enabled" />
	<xsl:template match="/certificate_mod">
		<html>
			<xsl:call-template name="content" />
		</html>
	</xsl:template>
	<!--===========================label===============================-->
	<xsl:variable name="lab_ins_cert" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_62')"/>
	<xsl:variable name="lab_upd_cert" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_63')"/>
	<xsl:variable name="lab_title" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_28')"/>
	<xsl:variable name="lab_core" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_38')"/>
	<xsl:variable name="lab_training_center" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_64')"/>
	<xsl:variable name="lab_bg_imgae" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_65')"/>
	<xsl:variable name="lab_upd_bg_imgae" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_66')"/>
	<xsl:variable name="lab_status" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_20')"/>
	<xsl:variable name="lab_active" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_21')"/>
	<xsl:variable name="lab_inactive" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_22')"/>
	<xsl:variable name="lab_g_form_btn_ok" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_67')"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_68')"/>
	<xsl:variable name="lab_title_desc" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_69')"/>
	<xsl:variable name="lab_cfc_end_date" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_70')"/>
	<xsl:variable name="lab_bg_imgae_desc" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_71')"/>
	<xsl:variable name="lab_bg_imgae_desc_style" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_357')"/>
	<!--===================================================================-->
	<xsl:template name="content">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}" />
			<title>
				<xsl:value-of select="$wb_wizbank" />
			</title>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript" />
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript" />
			<script language="JavaScript" src="{$wb_js_path}wb_cos_evaluation.js" type="text/javascript" />
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript" />
			<script language="JavaScript" src="{$wb_js_path}wb_goldenman.js" type="text/javascript" />
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js" />
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_certificate.js" />
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_rm_{$wb_cur_lang}.js"></script>
			<script language="JavaScript" type="text/javascript"><![CDATA[
				var cert = new wbCertificate;
			    var goldenman = new wbGoldenMan;
				function del_tc() {
					RemoveSingleOption(document.frmXml.tc_id_single,document.frmXml.tc_id);
				}
			]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0"
			onLoad="wb_utils_gen_form_focus(document.frmXml)">
			<form enctype="multipart/form-data" name="frmXml">
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
					<xsl:with-param name="parent_code">FTN_AMD_CERTIFICATE_MAIN</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">
						<xsl:choose>
							<xsl:when test="certInfo/@id != ''">
								<xsl:value-of select="$lab_upd_cert" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_ins_cert" />
							</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
				</xsl:call-template>
				<!-- 1 -->
				<table>
					<tr>
						<td class="wzb-form-label">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_core" />：
						</td>
						<td class="wzb-form-control">
							<input size="35" name="cert_core" value="{certInfo/cfc_core}"
								id="cert_core" type="text" style="width:300px;" maxlength="20" class="wzb-inputText" />
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_title" />：
						</td>
						<td class="wzb-form-control">
							<input size="35" name="cert_title" value="{certInfo/title}"
								id="cert_title" type="text" style="width:300px;margin-right:10px" maxlength="50" class="wzb-inputText" />
								<span class="wzb-ui-module-text"><xsl:value-of select="$lab_title_desc" /></span>
						</td>
					</tr>
					<tr>
								<td class="wzb-form-label">
									<span class="wzb-form-star">*</span><input type="hidden" name="lab_cfc_end_time" value=""/>
									<xsl:value-of select="$lab_cfc_end_date"/>
									<xsl:text>：</xsl:text>
								</td>
								<td class="wzb-form-control">
									<xsl:call-template name="display_form_input_time">
										<xsl:with-param name="fld_name">cert_end</xsl:with-param>
										<xsl:with-param name="hidden_fld_name">cert_end</xsl:with-param>
										<xsl:with-param name="show_label">Y</xsl:with-param>
										<xsl:with-param name="timestamp">
										 <xsl:value-of select="certInfo/cfc_end_datetime"></xsl:value-of>
										</xsl:with-param>
									</xsl:call-template>
								</td>
					</tr>
					<xsl:if test="$tc_enabled='true'">
						<tr>
							<td class="wzb-form-label">
								<span class="wzb-form-star">*</span><xsl:value-of select="$lab_training_center" />：
							</td>
							<td class="wzb-form-control">
								<xsl:call-template name="wb_goldenman">
									<xsl:with-param name="frm">document.frmXml</xsl:with-param>
									<xsl:with-param name="name">tc_id</xsl:with-param>
									<xsl:with-param name="box_size">1</xsl:with-param>
									<xsl:with-param name="field_name">tc_id</xsl:with-param>
									<xsl:with-param name="tree_type">training_center</xsl:with-param>
									<xsl:with-param name="select_type">2</xsl:with-param>
									<xsl:with-param name="pick_leave">1</xsl:with-param>
									<xsl:with-param name="pick_root">0</xsl:with-param>
									<xsl:with-param name="remove_function">del_tc()</xsl:with-param>
									<xsl:with-param name="single_option_value">
										<xsl:value-of select="cur_training_center/@id" />
									</xsl:with-param>
									<xsl:with-param name="single_option_text">
										<xsl:value-of select="cur_training_center/title" />
									</xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
					</xsl:if>

					<xsl:if test="certInfo/@id != ''">
						<tr>
							<td class="wzb-form-label" valign="top">
								<span class="wzb-form-star">*</span><xsl:value-of select="$lab_bg_imgae" />：
							</td>
							<td class="wzb-form-control">
								<img width="100px" height="100px" border="0" src="{certInfo/img_url}" />
							</td>
						</tr>
					</xsl:if>

					<tr>
						<td class="wzb-form-label">
							<xsl:choose>
								<xsl:when test="certInfo/@id != ''">
									<span class="wzb-form-star">*</span><xsl:value-of select="$lab_upd_bg_imgae" />：
								</xsl:when>
								<xsl:otherwise>
									<span class="wzb-form-star">*</span><xsl:value-of select="$lab_bg_imgae" />：
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td class="wzb-form-control" valign="top">
						<xsl:call-template name="wb_gen_input_file">
							<xsl:with-param name="name">cert_img</xsl:with-param>
						</xsl:call-template>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							&#160;
						</td>
						<td class="wzb-form-control">
							<span class="wzb-ui-module-text"><xsl:value-of select="$lab_bg_imgae_desc"/></span>
							<br/>
							<span class="wzb-ui-module-text">
							（<xsl:value-of select="$lab_bg_imgae_desc_style" />）
							</span>
						</td>
					</tr>

					<tr>
						<td class="wzb-form-label" valign="top">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_status" />：
						</td>
						<td class="wzb-form-control">
							<label for="rdo_mod_status_ind_1">
								<input id="rdo_mod_status_ind_1" type="radio" name="mod_status_ind"
									value="ON">
									<xsl:if test="certInfo/status = 'ON'">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
								<xsl:value-of select="$lab_active" />
							</label>
							<br />
							<label for="rdo_mod_status_ind_2">
								<input id="rdo_mod_status_ind_2" type="radio" name="mod_status_ind"
									value="OFF">
									<xsl:choose>
										<xsl:when test="certInfo/@id != ''">
											<xsl:if test="certInfo/status = 'OFF'">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</xsl:when>
										<xsl:otherwise>
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:otherwise>
									</xsl:choose>
								</input>
								<xsl:value-of select="$lab_inactive" />
							</label>
							<input type="hidden" name="mod_status" />
						</td>
					</tr>
					<tr>
						<td width="35%" align="right">
						</td>
						<td width="65%" align="left">
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
						</td>
						<td class="wzb-ui-module-text">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
						</td>
				   </tr>
				</table>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok" />
						<xsl:with-param name="wb_gen_btn_href">Javascript:cert.ins_cert_exec(frmXml,'<xsl:value-of select="$wb_lang" />','<xsl:value-of select="certInfo/@id" />')</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel" />
						<xsl:with-param name="wb_gen_btn_href">javascript:history.back();</xsl:with-param>
					</xsl:call-template>
				</div>
				<input type="hidden" name="cmd" value="ins_cert" />
				<input type="hidden" name="url_success" value="" />
				<input type="hidden" name="url_failure" value="" />
				<input type="hidden" name="module" value="" />
				<input type="hidden" name="cert_tc_id" value="" />
				<input type="hidden" name="cert_id" />
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>