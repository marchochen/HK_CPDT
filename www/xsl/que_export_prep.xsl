<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>

	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/draw_res_option_list.xsl"/>
	<xsl:variable name="tc_enabled" select="//tc_enabled"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="language" select="//cur_usr/@language"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="content"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:variable name="lab_sys_list" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_180')"/>
	<xsl:variable name="lab_res_manager" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_181')"/>
	<xsl:variable name="lab_que_res_export" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_182')"/>
	<xsl:variable name="lab_all" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_183')"/>
	<xsl:variable name="lab_res_id" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_184')"/>
	<xsl:variable name="lab_res_id_exact" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_185')"/>
	<xsl:variable name="lab_res_id_range" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_186')"/>
	<xsl:variable name="lab_from" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_187')"/>
	<xsl:variable name="lab_to" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_188')"/>
	<xsl:variable name="lab_title" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_189')"/>
	<xsl:variable name="lab_desc" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_190')"/>
	<xsl:variable name="lab_cre_time" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_191')"/>
	<xsl:variable name="lab_upd_time" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_192')"/>
	<xsl:variable name="lab_date_input_mask" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_193')"/>
	<xsl:variable name="lab_mins" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_194')"/>
	<xsl:variable name="lab_diff" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_195')"/>
	<xsl:variable name="lab_easy" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_196')"/>
	<xsl:variable name="lab_normal" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_197')"/>
	<xsl:variable name="lab_hard" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_198')"/>
	<xsl:variable name="lab_owner" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_199')"/>
	<xsl:variable name="lab_personal" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_200')"/>
	<xsl:variable name="lab_public" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_201')"/>
	<xsl:variable name="lab_status" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_202')"/>
	<xsl:variable name="lab_online" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_203')"/>
	<xsl:variable name="lab_offline" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_204')"/>
	<xsl:variable name="lab_type" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_205')"/>
	<xsl:variable name="lab_sort_by" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_206')"/>
	<xsl:variable name="lab_asc_order" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_207')"/>
	<xsl:variable name="lab_desc_order" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_208')"/>
	<xsl:variable name="lab_g_form_btn_ok" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_209')"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_210')"/>
	<xsl:variable name="lab_que_type" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_211')"/>
	<xsl:variable name="lab_res_folder" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_212')"/>
	<xsl:variable name="lab_include_sub" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_213')"/>
	<xsl:variable name="lab_yes" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_214')"/>
	<xsl:variable name="lab_no" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_215')"/>
	
	<!-- =============================================================== -->
	<xsl:template name="content">
		
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_batchprocess.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_search.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			seh = new wbSearch	
			goldenman = new wbGoldenMan;
			Batch = new wbBatchProcess;
		
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml" method="post">
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
					<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_que_res_export"/>
				</xsl:call-template>
				<table>
					<tr>
						<td class="wzb-form-label">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_que_type"/>：
						</td>
						<td class="wzb-form-control">
							<select name="que_type" class="wzb-form-select">
								<xsl:call-template name="draw_que_option_list"/>
							</select>
						</td>
						<input type="hidden" name="s_que_type" value=""/>
						<input type="hidden" name="type_fld_name" value="{$lab_que_type}"/>
					</tr>
					<tr>
						<td valign="top" class="wzb-form-label">
							<xsl:value-of select="$lab_res_folder"/>：
						</td>
						<td class="wzb-form-control">
							<!-- tnd_id_lst -->
							<xsl:variable name="tree_type">
								<xsl:choose>
									<xsl:when test="$tc_enabled = 'true'">TC_SYLLABUS_AND_OBJECT</xsl:when>
									<xsl:otherwise>SYLLABUS_AND_OBJECT</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:call-template name="wb_goldenman">
								<xsl:with-param name="field_name">res_tnd_id</xsl:with-param>
								<xsl:with-param name="name">tnd_id</xsl:with-param>
								<xsl:with-param name="box_size">4</xsl:with-param>
								<xsl:with-param name="tree_type"><xsl:value-of select="$tree_type"/></xsl:with-param>
								<xsl:with-param name="pick_root">0</xsl:with-param>
								<xsl:with-param name="tree_subtype">QUE</xsl:with-param>
								<xsl:with-param name="args_type">row</xsl:with-param>
								<xsl:with-param name="complusory_tree">0</xsl:with-param>
								<xsl:with-param name="select_type">1</xsl:with-param>
								<xsl:with-param name="custom_js_code_extra">args = goldenman.set_global_catalog_label(args,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
								<xsl:with-param name="label_add_btn">
									<xsl:value-of select="$lab_gen_select"/>
								</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$lab_include_sub"/>：
						</td>
						<td class="wzb-form-control">
							<input type="radio" id="sub_1" name="include_sub" value="1" checked="checked"/>
							<label for="sub_1" style="color: #666;font-weight: normal;"><xsl:value-of select="$lab_yes"/></label>
							<br/>
							<input type="radio" id="sub_2" name="include_sub" value="0"/>
							<label for="sub_2" style="color: #666;font-weight: normal;"><xsl:value-of select="$lab_no"/></label>
						</td>
						<input type="hidden" name="s_include_sub" value=""/>
					</tr>
					<tr>
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$lab_res_id"/>：
						</td>
						<td class="wzb-form-control">
							<input type="radio" id="rdo_search_id_1" name="rdo_search_id" onClick="seh.chg_id_criteria(document.frmXml)"/>
							<label for="rdo_search_id_1" style="color: #666;font-weight: normal;">
								<xsl:value-of select="$lab_res_id_exact"/>：
							</label>
							<!-- <img width="22" height="1" border="0" src="../wb_image/tp.gif"/> -->
							<input class="wzb-inputText" maxlength="9" type="text" name="search_id" size="9" onFocus="this.form.rdo_search_id[0].checked=true;seh.chg_id_criteria(document.frmXml)"/>
						</td>
					</tr>
					<tr>
						<td></td>
						<td class="wzb-form-control">
							<input type="radio" id="rdo_search_id_2" name="rdo_search_id" onClick="seh.chg_id_criteria(document.frmXml)"/>
							<label for="rdo_search_id_2" style="color: #666;font-weight: normal;">
								<xsl:value-of select="$lab_res_id_range"/>： <xsl:value-of select="$lab_from"/>
							</label>
							<xsl:text>&#160;</xsl:text>
							<input class="wzb-inputText" type="text" maxlength="9" name="search_id_after" size="9" onFocus="this.form.rdo_search_id[1].checked=true;seh.chg_id_criteria(document.frmXml)"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$lab_to"/>
							<xsl:text>&#160;</xsl:text>
							<input class="wzb-inputText" type="text" maxlength="9" name="search_id_before" size="9" onFocus="this.form.rdo_search_id[1].checked=true;seh.chg_id_criteria(document.frmXml)"/>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_title"/>：
						</td>
						<td class="wzb-form-control">
							<input class="wzb-inputText" type="text" name="search_title" size="18"/>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_desc"/>：
						</td>
						<td class="wzb-form-control">
							<input class="wzb-inputText" type="text" name="search_desc" size="18"/>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_cre_time"/>：
						</td>
						<td class="wzb-form-control">
							<xsl:value-of select="$lab_from"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:call-template name="display_form_input_time">
								<xsl:with-param name="fld_name">c_a</xsl:with-param>
								<xsl:with-param name="frm">frmXml</xsl:with-param>
							</xsl:call-template>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$lab_to"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:call-template name="display_form_input_time">
								<xsl:with-param name="fld_name">c_b</xsl:with-param>
								<xsl:with-param name="frm">frmXml</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_upd_time"/>：
						</td>
						<td class="wzb-form-control">
							<xsl:value-of select="$lab_from"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:call-template name="display_form_input_time">
								<xsl:with-param name="fld_name">u_a</xsl:with-param>
								<xsl:with-param name="frm">frmXml</xsl:with-param>
							</xsl:call-template>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$lab_to"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:call-template name="display_form_input_time">
								<xsl:with-param name="fld_name">u_b</xsl:with-param>
								<xsl:with-param name="frm">frmXml</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>

					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_diff"/>：
						</td>
						<td class="wzb-form-control">
							
								<input type="checkbox" name="chk_diff_easy" id="chk_diff_easy_id" checked="checked" style="margin-top:15px;"/>
								<label for="chk_diff_easy_id" style="margin-right:10px;margin-top:-34px;color: #666;font-weight: normal;">
									<xsl:value-of select="$lab_easy"/>
								</label>
								<input type="checkbox" name="chk_diff_normal" id="chk_diff_normal_id" checked="checked"/>
								<label for="chk_diff_normal_id" style="margin-right:10px;margin-top:-34px;color: #666;font-weight: normal;">
									<xsl:value-of select="$lab_normal"/>
								</label>
								<input type="checkbox" name="chk_diff_hard" id="chk_diff_hard_id" checked="checked"/>
								<label for="chk_diff_hard_id" style="margin-right:10px;margin-top:-34px;color: #666;font-weight: normal;">
									<xsl:value-of select="$lab_hard"/>							
								</label>
							
						</td>
					</tr>
					<!--  <tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_owner"/>：
						</td>
						<td class="wzb-form-control">
							<input class="wzb-inputText" type="text" name="search_owner" size="18"/>
						</td>
					</tr> -->
					<input class="wzb-inputText" type="hidden" name="search_owner" size="18"/>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_status"/>：
						</td>
						<td class="wzb-form-control">
							<select name="search_status" size="1" class="wzb-form-select">
								<option selected="selected" value="">
									<xsl:value-of select="$lab_all"/>
								</option>
								<option value="ON">
									<xsl:value-of select="$lab_online"/>
								</option>
								<option value="OFF">
									<xsl:value-of select="$lab_offline"/>
								</option>
							</select>
						</td>
					</tr>
					<tr>
						<td width="35%" align="right">
						</td>
						<td width="65%" align="left" class="wzb-ui-module-text">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
						</td>
					</tr>
				</table>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Res.Export.export_que_exe(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
					</xsl:call-template>
				</div>
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="charset" value="{$language}"/>
				<input type="hidden" name="url_success" value="{$language}"/>
				<input type="hidden" name="url_failure" value="{$language}"/>
				<input type="hidden" name="search_type"/>
				<input type="hidden" name="search_sub_type"/>
				<input type="hidden" name="search_create_time_before"/>
				<input type="hidden" name="search_create_time_after"/>
				<input type="hidden" name="search_update_time_before"/>
				<input type="hidden" name="search_update_time_after"/>
				<input type="hidden" name="search_diff_lst"/>
				<input type="hidden" name="res_tnd_id_lst"/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="search_items_per_page" value="20"/>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>
