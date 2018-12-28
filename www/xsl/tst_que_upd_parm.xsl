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
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="share/res_label_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="msp_id" select="/modulespec/objective/@msp_id"/>
	<!-- =============================================================== -->
	<xsl:template match="/modulespec | /dynamic_que_container">
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
			<script language="JavaScript" src="{$wb_js_path}wb_assessment.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
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
	
	var asm = new wbAssessment
	var obj = new wbObjective
	var course_lst = new wbCourse;
	var tst = new wbTst;
	var cos_title = parent.hidden.document.frmXml.cos_title.value;
	var mod_title = parent.hidden.document.frmXml.mod_title.value;
	var module_id  = parent.hidden.document.frmXml.mod_id.value;
	var cos_id  = parent.hidden.document.frmXml.cos_id.value;
	var mod_subtype  = parent.hidden.document.frmXml.mod_subtype.value;

	

	]]></script>
			<xsl:call-template name="new_css"/>
		</head>
		<BODY leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml">
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="mod_id" value=""/>
				<input type="hidden" name="obj_id" value=""/>
				<input type="hidden" name="msp_type" value=""/>
				<input type="hidden" name="msp_difficulty" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="msp_id" value="{$msp_id}"/>
				<xsl:if test="criterion">
					<input type="hidden" name="qcs_id" value="{criterion/@id}"/>
					<input type="hidden" name="res_id" value="{criterion/@res_id}"/>
					<input type="hidden" name="module" value="quebank.QueBankModule"/>
					<input type="hidden" name="qcs_type" value=""/>
					<input type="hidden" name="qcs_score" value=""/>
					<input type="hidden" name="qcs_difficulty" value=""/>
					<input type="hidden" name="qcs_qcount" value=""/>
					<input type="hidden" name="isExcludes" value=""/>
				</xsl:if>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</BODY>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_upd_obj">修改課程目標</xsl:with-param>
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
			<xsl:with-param name="lab_selected_category">資源文件夾</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_upd_obj">修改抽题条件</xsl:with-param>
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
			<xsl:with-param name="lab_selected_category">资源文件夹</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_upd_obj">Edit objective</xsl:with-param>
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
			<xsl:with-param name="lab_selected_category">Selected folder</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_upd_obj"/>
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
		<xsl:param name="lab_selected_category"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_upd_obj"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_inst"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_sub_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_selected_category"/>：
					<xsl:for-each select="criterion/objective | objective">
							<xsl:value-of select="desc/text()"/>
					</xsl:for-each>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table border="0" cellpadding="3" cellspacing="0" width="{$wb_gen_table_width}" class="Bg">
			<xsl:variable name="privilege">
				<xsl:for-each select="criterion/objective | objective">
					<xsl:value-of select="@privilege"/>
				</xsl:for-each>
			</xsl:variable>
			<input type="hidden" name="msp_privilege" value="{$privilege}"/>
			<xsl:variable name="selected_type">
				<xsl:choose>
					<xsl:when test="criterion">
						<xsl:value-of select="criterion/@type"/>	
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="objective/type_list/type/text()"/>	
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_type"/>：</span>
				</td>
				<td class="wzb-form-control">
					<label for="mod_qtype_mc">
						<input type="radio" name="mod_qtype_rdo" id="mod_qtype_mc" value="MC">
							<xsl:if test="$selected_type = 'MC'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<xsl:value-of select="$lab_mc"/>
					</label>
					<span style="margin-right:100px"></span>
					<label for="mod_qtype_fb">
						<input type="radio" name="mod_qtype_rdo" id="mod_qtype_fb" value="FB">
							<xsl:if test="$selected_type = 'FB'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<xsl:value-of select="$lab_fb"/>
					</label>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
				</td>
				<td  class="wzb-form-control">
					<label for="mod_qtype_mt">
						<input type="radio" name="mod_qtype_rdo" id="mod_qtype_mt" value="MT">
							<xsl:if test="$selected_type = 'MT'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<xsl:value-of select="$lab_mt"/>
					</label>
					<span style="margin-right:100px"></span>
					<label for="mod_qtype_tf">
						<input type="radio" name="mod_qtype_rdo" id="mod_qtype_tf" value="TF">
							<xsl:if test="$selected_type = 'TF'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<xsl:value-of select="$lab_tf"/>
					</label>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
				</td>
				<td  class="wzb-form-control">
					<label for="mod_qtype_es">
						<input type="radio" name="mod_qtype_rdo" id="mod_qtype_es" value="ES">
							<xsl:if test="$selected_type = 'ES'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<xsl:value-of select="$lab_es"/>
					</label>
					<!-- <span style="margin-right:100px"></span>
					<label for="mod_qtype_fsc">
						<input type="radio" name="mod_qtype_rdo" id="mod_qtype_fsc" value="FSC">
							<xsl:if test="$selected_type = 'FSC'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<xsl:value-of select="$lab_fixed_sc"/>
					</label> -->
				</td>
			</tr>
			<!-- <tr>   屏蔽动态情景题
				<td class="wzb-form-label">
				</td>
				<td  class="wzb-form-control">
					<label for="mod_qtype_dsc">
						<input type="radio" name="mod_qtype_rdo" id="mod_qtype_dsc" value="DSC">
							<xsl:if test="$selected_type = 'DSC'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<xsl:value-of select="$lab_dna_sc"/>
					</label>
					<span style="margin-right:100px"></span>
				</td>
			</tr> -->
			<xsl:variable name="selected_difficulty">
				<xsl:choose>
					<xsl:when test="criterion">
						<xsl:value-of select="criterion/@difficulty"/>	
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="objective/@difficulty"/>	
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_diff_level"/>：</span>
				</td>
				<td class="wzb-form-control">
					<label for="mod_qdiff_0">
						<input type="radio" name="mod_qdiff" id="mod_qdiff_0" value="0">
							<xsl:if test="$selected_difficulty = '0'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<xsl:value-of select="$lab_all"/>
					</label>
					<span style="margin-right:100px"></span>
					<label for="mod_qdiff_1">
						<input type="radio" name="mod_qdiff" id="mod_qdiff_1" value="1">
							<xsl:if test="$selected_difficulty = '1'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<xsl:value-of select="$lab_easy"/>
					</label>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					
				</td>
				<td class="wzb-form-control">
					<label for="mod_qdiff_2">
						<input type="radio" name="mod_qdiff" id="mod_qdiff_2" value="2">
							<xsl:if test="$selected_difficulty = '2'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<xsl:value-of select="$lab_normal"/>
					</label>
					<span style="margin-right:100px"></span>
					<label for="mod_qdiff_3">
						<input type="radio" name="mod_qdiff" id="mod_qdiff_3" value="3">
							<xsl:if test="$selected_difficulty = '3'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<xsl:value-of select="$lab_hard"/>
					</label>
				</td>
			</tr>
			<input type="hidden" name="msp_duration">
				<xsl:variable name="selected_duration">
					<xsl:choose>
						<xsl:when test="criterion">
							<xsl:value-of select="criterion/@duration"/>	
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="objective/@duration"/>	
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:if test="substring-before($selected_duration,'.')!='0'">
					<xsl:attribute name="value">
						<xsl:value-of select="substring-before($selected_duration,'.')"/>
					</xsl:attribute>
				</xsl:if>
			</input>
			<tr valign="top">
				<td class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_score_que"/>：</span>
				</td>
				<td class="wzb-form-control">
					<xsl:variable name="selected_score">
						<xsl:choose>
							<xsl:when test="criterion">
								<xsl:value-of select="criterion/@score"/>	
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="objective/@score"/>	
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<input size="4" name="msp_score" type="text" class="wzb-inputText">
						<xsl:if test="$selected_score!='0'">
							<xsl:attribute name="value">
								<xsl:value-of select="$selected_score"/>
							</xsl:attribute>
						</xsl:if>
					</input>
				</td>
			</tr>
			<tr valign="top">
				<td class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_total_que"/>：</span>
				</td>
				<td class="wzb-form-control">
					<xsl:variable name="q_count">
						<xsl:choose>
							<xsl:when test="criterion">
								<xsl:value-of select="criterion/@q_count"/>	
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="objective/@q_count"/>	
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<input size="4" name="msp_qcount" type="text" value="{$q_count}" class="wzb-inputText"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td class="wzb-form-control">
					<span class="wzb-ui-desc-text">
						<xsl:value-of select="$lab_all_info_required"/>
					</span>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:obj.gen_que_exec(document.frmXml,module_id,'<xsl:value-of select="//objective/@id"/>','<xsl:value-of select="$wb_lang"/>',mod_subtype,cos_id)</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:obj.pick_obj_frame(cos_id,module_id);</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<xsl:template match="env"/>
	<xsl:template match="cur_usr"/>
</xsl:stylesheet>
