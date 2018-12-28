<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="share/wb_module_type_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="course_id" select="/course/@id"/>
	<!-- =============================================================== -->
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<!--==============================================-->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="course"/>
		</html>
	</xsl:template>
	<!--==============================================-->
	<xsl:template match="course">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="wb_wizbank"/>
			</title>
			<xsl:call-template name="new_css"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_resource.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<!--alert样式  -->
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			
			 <link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				course_lst = new wbCourse;		
				module_lst = new wbModule;
				cos_name = wb_utils_get_cookie("title_prev");
				res = new wbResource;
				
				function emptyPickAsm(frm){
					frm.mod_type.value = '';
					frm.copy_media_from.value = '';
					frm.mod_title.value = '';
				}
			]]></SCRIPT>
		</head>
		<xsl:call-template name="wb_init_lab"/>
	</xsl:template>
	<!--==============================================-->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_create_new_test">添加測驗</xsl:with-param>
			<xsl:with-param name="lab_create_new_test_instruct">從以下選項中選擇一個方法：</xsl:with-param>
			<xsl:with-param name="lab_create_from_lrm">從資源庫複製一個現成的測驗。</xsl:with-param>
			<xsl:with-param name="lab_create_dxt">建立一個動態測驗。動態測驗按預先設定的條件從資源庫中隨機抽取題目。試卷將於用戶開始測驗時即時生成。</xsl:with-param>
			<xsl:with-param name="lab_create_tst">建立一個靜態測驗。你需要從資源庫中選取指定的題目按指定的次序放進試卷。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_select">選擇</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_create_new_test">添加测验</xsl:with-param>
			<xsl:with-param name="lab_create_new_test_instruct">从以下选项中选择一个方法：</xsl:with-param>
			<xsl:with-param name="lab_create_from_lrm">从资源库复制一个现成的测验。</xsl:with-param>
			<xsl:with-param name="lab_create_dxt">创建一个新的动态测验。动态测验是在用户开始测验时即时生成试卷。试卷是根据预先设定的抽题条件，即时从资源库中抽取题目生成的。</xsl:with-param>
			<xsl:with-param name="lab_create_tst">创建一个新的静态测验。静态测验的试卷是一套固定的，事先从资源库中选取好的题目。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_select">选择</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_create_new_test">Add test</xsl:with-param>
			<xsl:with-param name="lab_create_new_test_instruct">Select one of the following methods:</xsl:with-param>
			<xsl:with-param name="lab_create_from_lrm">Copy an existing test from the Learning Resource Management </xsl:with-param>
			<xsl:with-param name="lab_create_dxt">Create a dynamic Test. A dynamic test picks up questions from one or more question pools randomly  according to the pre-defined criteria. The test paper will be generated  in real time when user begin the test. </xsl:with-param>
			<xsl:with-param name="lab_create_tst">Create a fixed Test. You need to pick up a set of questions and define their sequence in the test paper.</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next">Next</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_select">Select</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!--==============================================================================-->
	<xsl:template name="content">
		<xsl:param name="lab_create_new_test"/>
		<xsl:param name="lab_create_new_test_instruct"/>
		<xsl:param name="lab_create_from_lrm"/>
		<xsl:param name="lab_create_dxt"/>
		<xsl:param name="lab_create_tst"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_next"/>
		<xsl:param name="lab_g_form_btn_select"/>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml">
				<input type="hidden" name="course_id" value="{$course_id}"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="module"/>
				<input type="hidden" name="res_id"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="source_content"/>
				<input type="hidden" name="source_type"/>
				<input type="hidden" name="copy_media_from"/>
				<input type="hidden" name="mod_type"/>
				<input type="hidden" name="mod_desc"/>
				<input type="hidden" name="mod_annotation"/>
				<input type="hidden" name="asHTML"/>
				<input type="hidden" name="tpl_type"/>
				<input type="hidden" name="tpl_subtype"/>
				<input type="hidden" name="dpo_view"/>
				<input type="hidden" name="stylesheet"/>
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_create_new_test_instruct"/>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_line"/>
				<table width="{$wb_gen_table_width}" border="0" cellpadding="3" cellspacing="0">
					<tr>
						<td colspan="2" height="10">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<!-- <tr>
						<td colspan="2" height="10" class="wzb-ui-desc-text">
							<span class="Text">
								<xsl:copy-of select="$tst_que_instr"/>
							</span>
						</td>
					</tr> -->
					<tr>
						<td>
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td>
							<table cellpadding="3" cellspacing="0" border="0">
								<tr>
									<td >
										<input type="radio" name="create_test" id="rdo_create_test_1"/>
									</td>
									<td valign="middle">
										<label for="rdo_create_test_1">
											<span class="Text">
												<xsl:value-of select="$lab_create_from_lrm"/>
											</span>
										</label>
										</td>
								</tr>
								<tr>
									<td >
									</td>
									<td>
										<input type="text" name="mod_title" style="width:300px" readonly="readonly" class="wzb-inputText"  />   <!-- class="btn wzb-inputText" -->
										
										<input style="margin-top: 0px;" onClick="res.pick_res('TST', 'ASM','{$course_id}');document.frmXml.create_test[0].checked = true" class="btn wzb-btn-blue" value="{$lab_g_form_btn_select}" name="genadd_asm" type="button"/>
									</td>
								</tr>
								<tr>
									<td valign="top" style="padding-top: 8px;">
										<input type="radio" name="create_test" id="rdo_create_test_2" onclick="emptyPickAsm(document.frmXml)"/>
									</td>
									<td valign="middle">
										<label for="rdo_create_test_2">
											<span class="Text">
												<xsl:value-of select="$lab_create_dxt"/>
											</span>
										</label>
									</td>
								</tr>
								<tr>
									<td valign="top" style="padding-top: 8px;">
										<input type="radio" name="create_test" id="rdo_create_test_3" onclick="emptyPickAsm(document.frmXml)"/>
									</td>
									<td valign="middle" >
										<label for="rdo_create_test_3">
											<span class="Text">
												<xsl:value-of select="$lab_create_tst"/>
											</span>
										</label>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="2" height="10">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
				</table>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_next"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:course_lst.ins_mod_test_prep_step_2(document.frmXml)</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.cancel_add('<xsl:value-of select="$course_id"/>')</xsl:with-param>
					</xsl:call-template>
			</div>
			</form>
		</body>
	</xsl:template>
	<!--==============================================-->
</xsl:stylesheet>
