<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_win_hdr.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="share/res_label_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/list">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_objective.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_course.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_module.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_scenario.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js" type="text/javascript"/>
			<!--alert样式  -->
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			<!--alert样式  end -->
			<script language="JavaScript" type="text/javascript"><![CDATA[
	
	var obj = new wbObjective
	var course_lst = new wbCourse;
	var module_lst = new wbModule;
	var tst = new wbTst;
	var sc = new wbScControl;
	var cos_title = parent.hidden.document.frmXml.cos_title.value;
	var mod_title = parent.hidden.document.frmXml.mod_title.value;
	var mod_id  = parent.hidden.document.frmXml.mod_id.value;
	var cos_id  = parent.hidden.document.frmXml.cos_id.value;
	var mod_subtype  = parent.hidden.document.frmXml.mod_subtype.value;
	function pick_cancel() {
		window.location = "../htm/close_window.htm";
	}
	
	]]></script>
			<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml">
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="mod_id" value=""/>
				<input type="hidden" name="obj_id" value=""/>
				<input type="hidden" name="msp_type" value=""/>
				<input type="hidden" name="msp_difficulty" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="res_id" value=""/>
				<input type="hidden" name="qcs_obj_id" value=""/>
				<input type="hidden" name="qcs_type" value=""/>
				<input type="hidden" name="qcs_score" value=""/>
				<input type="hidden" name="qcs_difficulty" value=""/>
				<input type="hidden" name="qcs_qcount" value=""/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_inst">請註明選取題目的標準</xsl:with-param>
			<xsl:with-param name="lab_diff_level">難度</xsl:with-param>
			<xsl:with-param name="lab_type">題目類型</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_hard">困難</xsl:with-param>
			<xsl:with-param name="lab_score_que">每題分數</xsl:with-param>
			<xsl:with-param name="lab_total_que">題目總數</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_step_2">第二步 - 指定題目選拔準則</xsl:with-param>
			<xsl:with-param name="lab_step_2_title">請指定題目選拔準則</xsl:with-param>
			<xsl:with-param name="lab_selected_category">文件夾名稱</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_inst">请选择所需题目</xsl:with-param>
			<xsl:with-param name="lab_diff_level">难度</xsl:with-param>
			<xsl:with-param name="lab_type">题目类型</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_hard">困难</xsl:with-param>
			<xsl:with-param name="lab_score_que">每题分值</xsl:with-param>
			<xsl:with-param name="lab_total_que">题目数量</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_step_2">第二步 – 定义抽题条件</xsl:with-param>
			<xsl:with-param name="lab_step_2_title">请指定抽取题目的条件</xsl:with-param>
			<xsl:with-param name="lab_selected_category">文件夹名称</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_inst">Please specify criteria for selecting questions</xsl:with-param>
			<xsl:with-param name="lab_diff_level">Difficulty</xsl:with-param>
			<xsl:with-param name="lab_type">Question type</xsl:with-param>
			<xsl:with-param name="lab_all">All</xsl:with-param>
			<xsl:with-param name="lab_easy">Easy</xsl:with-param>
			<xsl:with-param name="lab_normal">Normal</xsl:with-param>
			<xsl:with-param name="lab_hard">Hard</xsl:with-param>
			<xsl:with-param name="lab_score_que">Score per question</xsl:with-param>
			<xsl:with-param name="lab_total_que">No. of questions</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_step_2">Step 2 – define selection criteria</xsl:with-param>
			<xsl:with-param name="lab_step_2_title">Please specify criteria for selecting questions</xsl:with-param>
			<xsl:with-param name="lab_selected_category">Selected folder</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_course_list"/>
		<xsl:param name="lab_inst"/>
		<xsl:param name="lab_diff_level"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_score_que"/>
		<xsl:param name="lab_total_que"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_step_2"/>
		<xsl:param name="lab_step_2_title"/>
		<xsl:param name="lab_selected_category"/>
		
		<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
		</xsl:call-template>

		
		
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_step_2"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_step_2_title"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_sub_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_selected_category"/>：
					<xsl:value-of select="header/objective/desc"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table border="0" width="{$wb_gen_table_width}" class="Bg">
			<input type="hidden" name="msp_privilege" value="CW"/>
			<input type="hidden" name="isExcludes" value="false" />
			<tr>
				<td class="wzb-form-label" valign="top" style="width: 20%;">
					<span class="TitleText">
						<xsl:value-of select="$lab_type"/>：</span>
				</td>
				<td class="wzb-form-control" style="width: 30%;">
					<label for="mod_qtype_mc">
						<input type="radio" name="mod_qtype_rdo" id="mod_qtype_mc" value="MC"/>
						<xsl:value-of select="$lab_mc"/>
					</label>
				</td>
				<td class="wzb-form-control">
				   <label for="mod_qtype_fb">
						<input type="radio" name="mod_qtype_rdo" id="mod_qtype_fb" value="FB"/>
						<xsl:value-of select="$lab_fb"/>
					</label>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
				</td>
				<td class="wzb-form-control">
					<label for="mod_qtype_mt">
						<input type="radio" name="mod_qtype_rdo" id="mod_qtype_mt" value="MT"/>
						<xsl:value-of select="$lab_mt"/>
					</label>
				</td>
				<td class="wzb-form-control">
				   <label for="mod_qtype_tf">
						<input type="radio" name="mod_qtype_rdo" id="mod_qtype_tf" value="TF"/>
						<xsl:value-of select="$lab_tf"/>
					</label>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
				</td>
				<td class="wzb-form-control">
					<label for="mod_qtype_es">
						<input type="radio" name="mod_qtype_rdo" id="mod_qtype_es" value="ES"/>
						<xsl:value-of select="$lab_es"/>
					</label>
					<!--  屏蔽静态情景题
					<span style="margin-right:100px"></span>
					<label for="mod_qtype_fsc">
						<input type="radio" name="mod_qtype_rdo" id="mod_qtype_fsc" value="FSC"/>
						<xsl:value-of select="$lab_fixed_sc"/>
					</label> -->
				</td>
			</tr>
			<!-- 屏蔽动态情景题
			   <tr>
				<td class="wzb-form-label" valign="top">
				</td>
				<td class="wzb-form-control">
					<label for="mod_qtype_dsc">
						<input type="radio" name="mod_qtype_rdo" id="mod_qtype_dsc" value="DSC"/>
						<xsl:value-of select="$lab_dna_sc"/>
					</label>
					<span style="margin-right:100px"></span>
				</td>
			</tr>	 -->									
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_diff_level"/>：</span>
				</td>
				<td class="wzb-form-control">
					<label for="mod_qdiff_0">
						<input type="radio" name="mod_qdiff" id="mod_qdiff_0" value="0" checked="checked"/>
						<xsl:value-of select="$lab_all"/>
					</label>
				</td>
				<td class="wzb-form-control">
				   <label for="mod_qdiff_1">
						<input type="radio" name="mod_qdiff" id="mod_qdiff_1" value="1"/>
						<xsl:value-of select="$lab_easy"/>
					</label>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
				</td>
				<td class="wzb-form-control">
					<label for="mod_qdiff_2">
						<input type="radio" name="mod_qdiff" id="mod_qdiff_2" value="2"/>
						<xsl:value-of select="$lab_normal"/>
					</label>
				</td>
				<td class="wzb-form-control">
				  <label for="mod_qdiff_3">
						<input type="radio" name="mod_qdiff" id="mod_qdiff_3" value="3"/>
						<xsl:value-of select="$lab_hard"/>
					</label>
				</td>
			</tr>
			<input type="hidden" name="msp_duration" value=""/>
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_score_que"/>：</span>
				</td>
				<td class="wzb-form-control">
					<span class="Text">
						<input size="4" name="msp_score" type="text" value="" class="wzb-inputText"/>
					</span>
				</td>
				<td></td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_total_que"/>：</span>
				</td>
				<td class="wzb-form-control">
					<span class="Text">
						<input size="4" name="msp_qcount" type="text" value="" class="wzb-inputText"/>
					</span>
				</td>
				<td></td>
			</tr>
			<tr>
				<td class="wzb-form-label" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td class="wzb-form-control" height="10">
					<span class="wzb-ui-desc-text"><xsl:value-of select="$lab_all_info_required"/></span>
				</td>
				<td></td>
			</tr>			
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:obj.gen_que_exec(document.frmXml,parent.hidden.document.frmXml.mod_id.value,'<xsl:value-of select="header/objective/@id"/>','<xsl:value-of select="$wb_lang"/>',mod_subtype,cos_id)</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:window.history.back();</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<xsl:template match="env"/>
	<xsl:template match="cur_usr"/>
</xsl:stylesheet>
