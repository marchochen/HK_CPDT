<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="share/div_tree_share.xsl"/>
	<xsl:import href="share/gen_tree_js.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<!-- cust utils -->
	<!-- -->
	<xsl:output indent="yes"/>
	<xsl:variable name="mod_cnt" select="count(evaluation/evaluation_list/module_list/module)"/>
	<xsl:variable name="question_list_node" select="evaluation/question_list"/>
	<xsl:variable name="cur_usr">
		<xsl:value-of select="evaluation/meta/cur_usr/@id"/>
	</xsl:variable>
	<xsl:variable name="tc_enabled" select="evaluation/meta/tc_enabled"/>
	<xsl:variable name="cur_tcr_id" select="evaluation/cur_training_center/@id"/>
	<xsl:variable name="search_title" select="evaluation/search_title"/>
	<!-- pagination -->
	<xsl:variable name="cur_page" select="evaluation/evaluation_list/pagination/@cur_page"/>
	<xsl:variable name="total" select="evaluation/evaluation_list/pagination/@total_rec"/>
	<xsl:variable name="page_size" select="evaluation/evaluation_list/pagination/@page_size"/>
	<xsl:variable name="tcr_id" select="/evaluation/cur_training_center/@id"/>
	
	<!-- ================================================================ -->
	<xsl:template match="/">
		<xsl:apply-templates select="evaluation"/>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="evaluation">
		<html>
			<xsl:call-template name="draw_header"/>
			<xsl:call-template name="draw_body"/>
		</html>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_header">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_evaluation.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_mgt_rpt.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_assessment.js"/>
			<script language="Javascript"><![CDATA[
			var evlua = new wbEvaluation;
			var module_lst = new wbModule;
			mgt_rpt = new wbManagementReport;
			asm = new wbAssessment
			
			function show_content(tcr_id) {
				evlua.show_mgt_content(tcr_id);
			}

			function load_tree() {
				if (frmXml.tc_enabled_ind) {
					page_onload(250);
				}
			}
			
		]]></script>
		</head>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_body">
		<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0" onload="load_tree()">
			<form name="frmXml" onsubmit="return false;">
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="rpt_type" value=""/>
				<input type="hidden" name="spec_name" value=""/>
				<input type="hidden" name="spec_value" value=""/>
				<input type="hidden" name="download" value=""/>
				<input type="hidden" name="window_name" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_main_eval">公共調查問卷</xsl:with-param>
			<xsl:with-param name="lab_eval_lst">問卷列表</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_attempt">作答次數</xsl:with-param>
			<xsl:with-param name="lab_status">發佈狀態</xsl:with-param>
			<xsl:with-param name="lab_shown">已發佈</xsl:with-param>
			<xsl:with-param name="lab_hidden">未發佈</xsl:with-param>
			<xsl:with-param name="lab_time">開始時間</xsl:with-param>
			<xsl:with-param name="lab_no_eval">沒有問卷</xsl:with-param>
			<xsl:with-param name="lab_edit_question">題目</xsl:with-param>
			<xsl:with-param name="lab_edit_schedule">修改</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_add">新增問卷</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_view_results">結果</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_preview">預覽</xsl:with-param>
			<xsl:with-param name="lab_evn_survey_desc">列出所有公共調查問卷如下</xsl:with-param>
			<xsl:with-param name="lab_cur_tcr">當前培訓中心</xsl:with-param>
			<xsl:with-param name="lab_root_training_center">所有培訓中心</xsl:with-param>
			<xsl:with-param name="lab_target_object">發佈對象</xsl:with-param>
			<xsl:with-param name="lab_response_count">問卷回應數目</xsl:with-param>
			<xsl:with-param name="lab_public_date">發佈時間</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_evaluation">問卷列表</xsl:with-param>
			<xsl:with-param name="lab_tc">培訓中心</xsl:with-param>
				<xsl:with-param name="lab_desc">簡介</xsl:with-param>
				<xsl:with-param name="lab_name_desc">標題，簡介</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_main_eval">公共调查问卷</xsl:with-param>
			<xsl:with-param name="lab_eval_lst">调查问卷列表</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_attempt">提交次数</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_shown">已发布</xsl:with-param>
			<xsl:with-param name="lab_hidden">未发布</xsl:with-param>
			<xsl:with-param name="lab_time">开始时间</xsl:with-param>
			<xsl:with-param name="lab_no_eval">没有调查问卷</xsl:with-param>
			<xsl:with-param name="lab_edit_question">题目</xsl:with-param>
			<xsl:with-param name="lab_edit_schedule">修改</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_add">添加问卷</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">删除</xsl:with-param>
			<xsl:with-param name="lab_view_results">结果</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_preview">预览</xsl:with-param>
			<xsl:with-param name="lab_evn_survey_desc">列出所有公共调查问卷如下</xsl:with-param>
			<xsl:with-param name="lab_cur_tcr">当前培训中心</xsl:with-param>
			<xsl:with-param name="lab_root_training_center">所有培训中心</xsl:with-param>
			<xsl:with-param name="lab_target_object">发布对象</xsl:with-param>
			<xsl:with-param name="lab_response_count">问卷回应数目</xsl:with-param>
			<xsl:with-param name="lab_public_date">发布时间</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_evaluation">问卷列表</xsl:with-param>
			<xsl:with-param name="lab_tc">培训中心</xsl:with-param>
				<xsl:with-param name="lab_desc">简介</xsl:with-param>
				<xsl:with-param name="lab_name_desc">标题,简介</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_main_eval">Survey Management</xsl:with-param>
			<xsl:with-param name="lab_eval_lst">Evaluation list</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_attempt">Attempts</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_shown">Published</xsl:with-param>
			<xsl:with-param name="lab_hidden">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_time">Start time</xsl:with-param>
			<xsl:with-param name="lab_no_eval">No evaluation here</xsl:with-param>
			<xsl:with-param name="lab_edit_question">Question</xsl:with-param>
			<xsl:with-param name="lab_edit_schedule">Edit</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_add">Create survey</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_view_results">Results</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_preview">Preview</xsl:with-param>
			<xsl:with-param name="lab_evn_survey_desc">Listed below are all the survey.</xsl:with-param>
			<xsl:with-param name="lab_cur_tcr">Current training center</xsl:with-param>
			<xsl:with-param name="lab_root_training_center">All training centers</xsl:with-param>
			<xsl:with-param name="lab_target_object">Target</xsl:with-param>
			<xsl:with-param name="lab_response_count">Responses count</xsl:with-param>
			<xsl:with-param name="lab_public_date">Publish date</xsl:with-param>
			<xsl:with-param name="lab_to">To</xsl:with-param>
			<xsl:with-param name="lab_evaluation">Survey List</xsl:with-param>
			<xsl:with-param name="lab_tc">Training center </xsl:with-param>
				<xsl:with-param name="lab_desc">Description</xsl:with-param>
				<xsl:with-param name="lab_name_desc">Title,Description</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="content">
		<xsl:param name="lab_main_eval"/>
		<xsl:param name="lab_eval_lst"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_attempt"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_shown"/>
		<xsl:param name="lab_hidden"/>
		<xsl:param name="lab_time"/>
		<xsl:param name="lab_no_eval"/>
		<xsl:param name="lab_edit_question"/>
		<xsl:param name="lab_edit_schedule"/>
		<xsl:param name="lab_g_form_btn_add"/>
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_view_results"/>
		<xsl:param name="lab_g_txt_btn_preview"/>
		<xsl:param name="lab_evn_survey_desc"/>
		<xsl:param name="lab_cur_tcr"/>
		<xsl:param name="lab_root_training_center"/>
		<xsl:param name="lab_target_object"/>
		<xsl:param name="lab_response_count"/>
		<xsl:param name="lab_public_date"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_evaluation"/>
		<xsl:param name="lab_tc"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_name_desc"/>
		
		<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_EVN_MAIN</xsl:with-param>
			</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_main_eval"/>
			</xsl:with-param>
		</xsl:call-template>
		
		<!-- tree nav -->
		<xsl:variable name="cur_tcr_title">
			<xsl:choose>
				<xsl:when test="$cur_tcr_id != 0"><xsl:value-of select="//cur_training_center/title/text()"/></xsl:when>
				<xsl:otherwise><xsl:value-of select="$lab_root_training_center"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<table>
			<tr>
				<td width="35%">
				 
					<xsl:if test="$tc_enabled='true'">
						<xsl:call-template name="div_tree">
							<xsl:with-param name="lab_cur_tcr" select="$lab_cur_tcr"/>
							<xsl:with-param name="lab_root_training_center" select="$lab_root_training_center"/>
							<xsl:with-param name="title" select="$cur_tcr_title"/>
						</xsl:call-template>
						<input type="hidden" name="tc_enabled_ind"/>
					</xsl:if>
				</td>
				<td align="right" width="50%">
				  <div class="wzb-form-search">
				    <input type="text" value="{$search_title}" size="11" id="title_code" name="title_code" class="form-control" style="width:130px;" placeholder="{$lab_name_desc}"/>
					 <input type="button" class="form-submit margin-right4" value="" onclick="evlua.search_result_survey('{$wb_lang}')"/>
					
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_add"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:evlua.ins_evn_prep(<xsl:value-of select="$tcr_id" />)</xsl:with-param>
					</xsl:call-template>
				</div>
					<!--
					<xsl:if test="$mod_cnt&gt;0">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_remove"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:evlua.del_evn(frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					-->
				</td>
			</tr>
		</table>
		<!-- list view -->
		<xsl:choose>
			<xsl:when test="$mod_cnt='0'">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_eval"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<div class="margin-t28b10">
					<span class="wzb-ot-title-1"><xsl:value-of select="$lab_evaluation"/></span>
				</div>
					<xsl:apply-templates select="evaluation_list/module_list/module">
						<xsl:with-param name="lab_shown" select="$lab_shown"/>
						<xsl:with-param name="lab_hidden" select="$lab_hidden"/>
						<xsl:with-param name="lab_edit_question" select="$lab_edit_question"/>
						<xsl:with-param name="lab_edit_schedule" select="$lab_edit_schedule"/>
						<xsl:with-param name="lab_view_results" select="$lab_view_results"/>
						<xsl:with-param name="lab_g_txt_btn_remove" select="$lab_g_txt_btn_remove"/>
						<xsl:with-param name="lab_g_txt_btn_preview" select="$lab_g_txt_btn_preview"/>
						<xsl:with-param name="lab_status" select="$lab_status"/>
						<xsl:with-param name="lab_target_object" select="$lab_target_object"/>
						<xsl:with-param name="lab_response_count" select="$lab_response_count"/>
						<xsl:with-param name="lab_public_date" select="$lab_public_date"/>
						<xsl:with-param name="lab_to" select="$lab_to"/>
						<xsl:with-param name="lab_tc" select="$lab_tc"/>
							<xsl:with-param name="lab_desc" select="$lab_desc"/>
					
					</xsl:apply-templates>
					<xsl:call-template name="wb_ui_pagination">
						<xsl:with-param name="cur_page" select="$cur_page"/>
						<xsl:with-param name="page_size" select="$page_size"/>
						<xsl:with-param name="total" select="$total"/>
						<xsl:with-param name="width" select="$wb_gen_table_width"/>
						<xsl:with-param name="cur_page_name">cur_page</xsl:with-param>
					</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="module">
		<xsl:param name="lab_shown"/>
		<xsl:param name="lab_hidden"/>
		<xsl:param name="lab_edit_question"/>
		<xsl:param name="lab_edit_schedule"/>
		<xsl:param name="lab_view_results"/>
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_g_txt_btn_preview"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_target_object"/>
		<xsl:param name="lab_response_count"/>
		<xsl:param name="lab_public_date"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_tc"/>
		<xsl:param name="lab_desc"/>
		
		<xsl:variable name="mod_id" select="@id"/>
		<table class="table wzb-ui-table">
			<tr>
				<td>
					<xsl:value-of select="title"/>
				</td>
				<td nowrap="nowrap" align="right" style="color:#fff;padding: 10px 0px 10px 8px;">
				<!--预览按钮 -->
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_preview"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:evlua.evn_opn('<xsl:value-of select="@type"/>',<xsl:value-of select="$mod_id"/>,'svy_player.xsl')</xsl:with-param>
				</xsl:call-template>
				<!-- 修改按钮 -->
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_edit_schedule"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:evlua.upd_evn_prep('<xsl:value-of select="@id"/>')</xsl:with-param>
				</xsl:call-template>
				<!--题目按钮 -->
				<xsl:choose>
					<xsl:when test="attempt_nbr/text() &lt;= 0 or not(attempt_nbr/text())">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_edit_question"/>
							<!--<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.upd_tst_prep(<xsl:value-of select="$mod_id"/>,'','<xsl:value-of select="$cur_usr"/>','<xsl:value-of select="@status"/>','<xsl:value-of select="@attempted"/>','EVN','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>-->
							<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.upd_svy_prep(<xsl:value-of select="$mod_id"/>,'','<xsl:value-of select="$cur_usr"/>','<xsl:value-of select="@status"/>','<xsl:value-of select="@attempted"/>','EVN','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
				</xsl:choose>
				<!-- -->
				<!--删除按钮 -->
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_remove"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:evlua.del_evn_sng(<xsl:value-of select="$mod_id"/>,'<xsl:value-of select="@timestamp"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
				</xsl:call-template>
				<!--结果按钮 -->	
				<xsl:choose>
					<xsl:when test="attempt_nbr/text() &gt;= 1">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_view_results"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:mgt_rpt.get_evn_svy_rpt(frmXml, '<xsl:value-of select="@id"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
				</xsl:choose>
				<!-- 导出按钮 -->	
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="class">btn wzb-btn-blue</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '692')"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:asm.export_evn('<xsl:value-of select="@id"/>')</xsl:with-param>
				</xsl:call-template>
			</td>
		</tr>
		</table>
		<table class="wzb-ui-table-no-border" border="0">
			<xsl:if test="desc/text() != ''">
				<tr>
				
					<td class="wzb-color-list" valign="top" width="30%" align="right">
							<xsl:value-of select="$lab_desc"/>：
					</td>
						
					<td  width="70%"  align="left" > <!-- class="wzb-color-list"  -->
						<xsl:variable name="escaped_mod_desc1">
						<xsl:call-template name="escape_js">
							<xsl:with-param name="input_str">
								<xsl:value-of select="desc/text()"/>
							</xsl:with-param>
						</xsl:call-template>
						</xsl:variable>
						<xsl:variable name="escaped_mod_desc">
						<xsl:call-template name="unescape_html_linefeed">
							<xsl:with-param name="my_right_value">
								<xsl:value-of select="$escaped_mod_desc1"/>
							</xsl:with-param>
						</xsl:call-template>
						</xsl:variable>
						<xsl:value-of select="$escaped_mod_desc" disable-output-escaping="yes"/>
					</td>
					<td></td>
				</tr>
			</xsl:if>
			<xsl:if test="$cur_tcr_id = 0 and tcr_title/text() != ''">
				<tr>
					<td class="wzb-color-list" valign="top" align="right">
						<xsl:value-of select="$lab_tc"/>：
					</td>
					<td>
						<xsl:value-of select="tcr_title/text()"/>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td class="wzb-color-list" valign="top" align="right">
					<xsl:value-of select="$lab_target_object"/>：
				</td>
				<td  align="left">
					<xsl:for-each select="eval_target_lst/target">
						<xsl:value-of select="./text()"/><xsl:if test="position() != last()">、</xsl:if>
					</xsl:for-each>
				</td>
			</tr>
			<tr >
				<td class="wzb-color-list" align="right">
					<xsl:value-of select="$lab_status"/>：
				</td>
				<td >
					<xsl:choose>
						<xsl:when test="@status='ON'">
							<xsl:value-of select="$lab_shown"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_hidden"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td class="wzb-color-list" align="right">
					<xsl:value-of select="$lab_response_count"/>：
				</td>
				<td >
					<xsl:value-of select="response_count"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-color-list" align="right">
					<xsl:value-of select="$lab_public_date"/>：
				</td>
				<td >
					<xsl:choose>
						<xsl:when test="@eff_start_datetime = ''">--</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="@eff_start_datetime"/>
								</xsl:with-param>
							</xsl:call-template>&#160;
						</xsl:otherwise>
					</xsl:choose>
					
					<xsl:value-of select="$lab_to"/>&#160;
					<xsl:choose>
						<xsl:when test="@eff_end_datetime = ''">--</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="@eff_end_datetime"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</table>
		<xsl:if test="not(attempt_nbr/text() &gt;= 1)">
			<input type="hidden" name="mod_timestamp" value="{@timestamp}"/>
		</xsl:if>
	</xsl:template>
	<!-- ================================================================ -->
</xsl:stylesheet>
