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
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="share/res_label_share.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="wb_gen_table_width" select="$wb_frame_table_width"/>

	<!-- =============================================================== -->
	<xsl:template match="/">
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
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_module.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_scenario.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js" type="text/javascript"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
	
	var sc = new wbScControl;
	var continer_id = getUrlParam('res_id');
	
	]]></script>
			<xsl:call-template name="new_css"/>
				<script type="text/javascript" src="../static/js/jquery.js"></script>
				<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
				<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
				<script type="text/javascript" src="../static/js/cwn_utils.js"></script>
				<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
				<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
				<xsl:call-template name="new_css"/>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			
				<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
					//由于xsl 中 alert 比较多 样式比较丑，所以 在这里直接替换掉
				    window.alert = function(text){
					   Dialog.alert(text);
				    }
				]]></SCRIPT>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="document.frmXml.qcs_score.focus();">
			<form name="frmXml">
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="res_id" value=""/>
				<input type="hidden" name="qcs_id" value="{//criterion/@id}"/>
				<input type="hidden" name="qcs_obj_id" value=""/>
				<input type="hidden" name="qcs_type" value=""/>
				<input type="hidden" name="qcs_difficulty" value=""/>
				<input type="hidden" name="msp_type" value=""/>
				<input type="hidden" name="msp_difficulty" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
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
			<xsl:with-param name="lab_total_que">抽題數量</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_dsc_spec">抽題條件</xsl:with-param>
			<xsl:with-param name="lab_selected_category">資源文件夾</xsl:with-param>
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
			<xsl:with-param name="lab_score_que">每题分数</xsl:with-param>
			<xsl:with-param name="lab_total_que">抽题数量</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_dsc_spec">抽题条件</xsl:with-param>
			<xsl:with-param name="lab_selected_category">资源文件夹</xsl:with-param>
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
			<xsl:with-param name="lab_total_que">Questions required</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_dsc_spec">Selection criteria</xsl:with-param>
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
		<xsl:param name="lab_dsc_spec"/>
		<xsl:param name="lab_selected_category"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_dsc_spec"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<input type="hidden" name="msp_privilege" value="CW"/>
			<input type="hidden" name="msp_duration" value=""/>
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_score_que"/>:</span>
				</td>
				<td class="wzb-form-control">
				<input type="hidden" name="qcs_score_label" value="{$lab_score_que}"/>
					<span class="Text">
						<input maxlength="5" size="5" name="qcs_score" type="text" value="{//criterion/@score}" class="wzb-inputText"/>
					</span>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_total_que"/>:</span>
				</td>
				<td class="wzb-form-control">
				<input type="hidden" name="qcs_qcount_label" value="{$lab_total_que}"/>
					<span class="Text">
						<input maxlength="5" size="5" name="qcs_qcount" type="text" value="{//criterion/@q_count}" class="wzb-inputText"/>
					</span>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:variable name="wb_gen_btn_href">
				<xsl:choose>
					<xsl:when test="//criterion/@id">javascript:sc.upd_dsc_spec(document.frmXml,continer_id,'<xsl:value-of select="$wb_lang"/>')</xsl:when>
					<xsl:otherwise>javascript:sc.add_dsc_spec(document.frmXml,continer_id,'<xsl:value-of select="$wb_lang"/>')</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href"><xsl:value-of select="$wb_gen_btn_href"/></xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">Javascript:history.back()</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<xsl:template match="env"/>
	<xsl:template match="cur_usr"/>
</xsl:stylesheet>
