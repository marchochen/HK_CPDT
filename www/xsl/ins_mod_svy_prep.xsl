<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="share/wb_module_type_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<!--==============================================-->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="surveyModule"/>
		</html>
	</xsl:template>
	<!--==============================================-->
	<xsl:template match="surveyModule">
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
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script>
				$(function(){
					$(".wzb-ui-table tr:last-child td").css("border-bottom","none")
					$(".wzb-ui-table>tbody>tr:first-child>td").css("border-top","1px solid #ddd")
				})
			</script>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				course_lst = new wbCourse;		
				module_lst = new wbModule;
				course_id = getUrlParam("course_id");
				cos_name = wb_utils_get_cookie("title_prev");
				
				function ins_mod_svy_exec(frm,course_id){
					if(frm.create_svy[1].checked)	
						course_lst.ins_mod_prep(course_id,'SVY','SVY');
					else if(frm.create_svy[0].checked){
						frm.cmd.value = "paste_public_survey";
						frm.course_id.value = course_id;
						frm.course_struct_xml_cnt.value = parent.course_struct_xml_cnt;
						frm.course_struct_xml_1.value = parent.course_struct_xml_1;
						frm.url_success.value = wb_utils_get_cookie("url_prev");
						frm.url_failure.value = parent.location.href;
						frm.target = "_parent";
						frm.method = "post";
						frm.action = wb_utils_servlet_url + "?isExcludes=true";
						frm.submit();
					}						
				}
			]]></SCRIPT>
		</head>
		<xsl:call-template name="wb_init_lab"/>
	</xsl:template>
	<!--==============================================-->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_create_new_svy">創建<xsl:value-of select="$lab_svy"/>
			</xsl:with-param>
			<xsl:with-param name="lab_pick_public_svy">選擇一個課程評估問卷</xsl:with-param>
			<xsl:with-param name="lab_instruct">創建課程評估問卷</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_preivew">預覽</xsl:with-param>
			<xsl:with-param name="lab_option_desc">要用同一評估問卷分析當前課程和其他課程的結果，請選擇此選項。</xsl:with-param>
			<xsl:with-param name="lab_no_eval_form">-- 尚未設定課程評估問卷 --</xsl:with-param>
			<xsl:with-param name="lab_create_new_test_instruct">從以下選項中選擇一個方法：</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_create_new_svy">创建<xsl:value-of select="$lab_svy"/>
			</xsl:with-param>
			<xsl:with-param name="lab_pick_public_svy">选择一个课程评估问卷</xsl:with-param>
			<xsl:with-param name="lab_instruct">创建课程评估问卷</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_preivew">预览</xsl:with-param>
			<xsl:with-param name="lab_option_desc">要用同一评估问卷分析当前课程和其他课程的结果，请选择此选项。</xsl:with-param>
			<xsl:with-param name="lab_no_eval_form">-- 尚未定义课程评估问卷 --</xsl:with-param>
			<xsl:with-param name="lab_create_new_test_instruct">从以下选项中选择一个方法：</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_create_new_svy">Create a new evaluation form</xsl:with-param>
			<xsl:with-param name="lab_create_new_test_instruct">Select one of the following methods:</xsl:with-param>
			<xsl:with-param name="lab_pick_public_svy">Select an evaluation form</xsl:with-param>
			<xsl:with-param name="lab_instruct">Add <xsl:value-of select="$lab_svy"/></xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">Next step</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_preivew">Preview</xsl:with-param>
			<xsl:with-param name="lab_option_desc">Choose this option if you want to analyze the results of this course and other courses using the same evaluation form in the <b>Management Report</b>. If you want to add more evaluation forms in the dropdown box, use the <b>Course Evaluation Builder</b> function.</xsl:with-param>
			<xsl:with-param name="lab_no_eval_form">-- No evaluation forms defined --</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!--==============================================================================-->
	<xsl:template name="content">
		<xsl:param name="lab_create_new_svy"/>
		<xsl:param name="lab_pick_public_svy"/>
		<xsl:param name="lab_instruct"/>
		<xsl:param name="lab_option_desc"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_txt_btn_preivew"/>
		<xsl:param name="lab_no_eval_form"/>
		<xsl:param name="lab_create_new_test_instruct"/>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml">
				<input type="hidden" name="course_id"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="course_struct_xml_cnt"/>
				<input type="hidden" name="course_struct_xml_1"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_create_new_test_instruct"/>
					</xsl:with-param>
				</xsl:call-template>
				<table class="table wzb-ui-table">
					<tr>
						<td>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="count(module_list/module) &gt; 0">
								
												<input type="radio" name="create_svy" checked="checked" id="rdo_create_svy_1"/>
								
															<label for="rdo_create_svy_1">
																<xsl:value-of select="$lab_pick_public_svy"/>：
															</label>
															<select name="mod_id" class="wzb-form-select">
																<xsl:for-each select="module_list/module">
																	<option value="{@id}">
																		<xsl:value-of select="title"/>
																	</option>
																</xsl:for-each>
															</select>
															<xsl:text>&#160;</xsl:text>
															<xsl:call-template name="wb_gen_button">
																<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_preivew"/>
																<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.preview_exec('SVY',document.frmXml.mod_id.options[document.frmXml.mod_id.selectedIndex].value,'svy_player.xsl',course_id)</xsl:with-param>
															</xsl:call-template>
										<xsl:call-template name="wb_ui_desc">
											<xsl:with-param name="text"><xsl:copy-of select="$lab_option_desc"/></xsl:with-param>
										</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									
												<input type="radio" name="create_svy" disabled="disabled" id="rdo_create_svy_1"/>
											
												<label for="rdo_create_svy_1">
													<xsl:value-of select="$lab_pick_public_svy"/>：
												</label>
											
												<select name="mod_id" class="Select">
													<option value="">
														<xsl:value-of select="$lab_no_eval_form"/>
													</option>
												</select>
								
									
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
					<tr>
						<td>
						</td>
						<td>
							
										<input type="radio" name="create_svy" id="rdo_create_svy_2">
											<xsl:if test="count(module_list/module) = 0">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</input>
										<label for="rdo_create_svy_2">
											<xsl:value-of select="$lab_create_new_svy"/>
										</label>
							
						</td>
					</tr>
				</table>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:ins_mod_svy_exec(frmXml,getUrlParam('course_id'))</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.cancel_add(getUrlParam('course_id'))</xsl:with-param>
					</xsl:call-template>
			</div>
			</form>
		</body>
	</xsl:template>
	<!--==============================================-->
</xsl:stylesheet>
