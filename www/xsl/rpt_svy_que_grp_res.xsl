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
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="share/itm_gen_details_share.xsl"/>
	<xsl:import href="share/rpt_share.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="share/label_rpt.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="tc_enabled" select="/report/meta/tc_enabled"/>
	<!-- =============================================================== -->
	<xsl:template match="/report">
		<html>
			<xsl:apply-templates select="report_list"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="report_list">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_mote.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_report.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_mgt_rpt.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="javascript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}overlib.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
				var mgt_rpt = new wbManagementReport;
				var itm_lst = new wbItem;
				var rpt = new wbReport;
				var mote = new wbMote;
				var mod = new wbModule;
			]]></script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="new_css"/>
		</head>
		<BODY leftMargin="0" topMargin="0" marginheight="0" marginwidth="0">
			<xsl:if test="not(/report/report_list/que_stat_list/que_stat/ans_stat[@view_all='true'])">
				<xsl:attribute name="onload">initSummary()</xsl:attribute>
			</xsl:if>
			<form name="frmXml">
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="rsp_id" value=""/>
				<input type="hidden" name="spec_name" value=""/>
				<input type="hidden" name="spec_value" value=""/>
				<input type="hidden" name="download" value=""/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="rte_id" value=""/>
				<input type="hidden" name="usr_ent_id" value=""/>
				<input type="hidden" name="rpt_type" value=""/>
				<input type="hidden" name="rpt_type_lst" value=""/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</BODY>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_cos_catalog">課程目錄</xsl:with-param>
			<xsl:with-param name="lab_course_type">課程類型</xsl:with-param>
			<xsl:with-param name="lab_type_self">網上課程</xsl:with-param>
			<xsl:with-param name="lab_type_class">課堂培訓</xsl:with-param>
			<xsl:with-param name="lab_cos">課程</xsl:with-param>
			<xsl:with-param name="lab_all_cos">所有課程</xsl:with-param>
			<xsl:with-param name="lab_cos_eva_fom">課程評估問卷</xsl:with-param>
			<xsl:with-param name="lab_enr_period">錄取日期</xsl:with-param>
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_rep_sum">報告摘要</xsl:with-param>
			<xsl:with-param name="lab_tol_enr">已錄取學員總數</xsl:with-param>
			<xsl:with-param name="lab_tol_res">反饋總數</xsl:with-param>
			<xsl:with-param name="lab_res_rate">反饋比率</xsl:with-param>
			<xsl:with-param name="lab_no">編號</xsl:with-param>
			<xsl:with-param name="lab_que">題目</xsl:with-param>
			<xsl:with-param name="lab_avg_res">平均反饋</xsl:with-param>
			<xsl:with-param name="lab_avg_score">平均分數</xsl:with-param>
			<xsl:with-param name="lab_sd">標準方差</xsl:with-param>
			<xsl:with-param name="lab_answered">已回答</xsl:with-param>
			<xsl:with-param name="lab_unanswered">未回答</xsl:with-param>
			<xsl:with-param name="lab_que_stat">題目統計</xsl:with-param>
			<xsl:with-param name="lab_cho">選擇</xsl:with-param>
			<xsl:with-param name="lab_score">分數</xsl:with-param>
			<xsl:with-param name="lab_count">計數</xsl:with-param>
			<xsl:with-param name="lab_percent">百分比</xsl:with-param>
			<xsl:with-param name="lab_total">總數</xsl:with-param>
			<xsl:with-param name="lab_show">顯示摘要</xsl:with-param>
			<xsl:with-param name="lab_hide">隱藏摘要</xsl:with-param>
			<xsl:with-param name="lab_answer">回答</xsl:with-param>
			<xsl:with-param name="lab_view">顯示全部</xsl:with-param>
			<xsl:with-param name="lab_html_content">HTML内容</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ex_rpt">匯出</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">我負責的課程</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">我負責學員報讀的課程</xsl:with-param>
			<xsl:with-param name="lab_que_rep_title">標題</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_cos_catalog">课程目录</xsl:with-param>
			<xsl:with-param name="lab_course_type">课程类型</xsl:with-param>
			<xsl:with-param name="lab_type_self">网上课程</xsl:with-param>
			<xsl:with-param name="lab_type_class">离线课堂</xsl:with-param>
			<xsl:with-param name="lab_cos">课程</xsl:with-param>
			<xsl:with-param name="lab_all_cos">所有课程</xsl:with-param>
			<xsl:with-param name="lab_cos_eva_fom">课程评估问卷</xsl:with-param>
			<xsl:with-param name="lab_enr_period">录取日期</xsl:with-param>
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_rep_sum">报告摘要</xsl:with-param>
			<xsl:with-param name="lab_tol_enr">已录取学员总数</xsl:with-param>
			<xsl:with-param name="lab_tol_res">反馈总数</xsl:with-param>
			<xsl:with-param name="lab_res_rate">反馈比率</xsl:with-param>
			<xsl:with-param name="lab_no">编号</xsl:with-param>
			<xsl:with-param name="lab_que">题目</xsl:with-param>
			<xsl:with-param name="lab_avg_res">平均反馈</xsl:with-param>
			<xsl:with-param name="lab_avg_score">平均分数</xsl:with-param>
			<xsl:with-param name="lab_sd">标准方差</xsl:with-param>
			<xsl:with-param name="lab_answered">已回答</xsl:with-param>
			<xsl:with-param name="lab_unanswered">未回答</xsl:with-param>
			<xsl:with-param name="lab_que_stat">题目统计</xsl:with-param>
			<xsl:with-param name="lab_cho">选择</xsl:with-param>
			<xsl:with-param name="lab_score">分数</xsl:with-param>
			<xsl:with-param name="lab_count">计数</xsl:with-param>
			<xsl:with-param name="lab_percent">百分比</xsl:with-param>
			<xsl:with-param name="lab_total">总数</xsl:with-param>
			<xsl:with-param name="lab_show">显示摘要</xsl:with-param>
			<xsl:with-param name="lab_hide">隐藏摘要</xsl:with-param>
			<xsl:with-param name="lab_answer">回答</xsl:with-param>
			<xsl:with-param name="lab_view">显示全部</xsl:with-param>
			<xsl:with-param name="lab_html_content">HTML内容</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ex_rpt">导出</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">我负责的课程</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">我负责学员报读的课程</xsl:with-param>
			<xsl:with-param name="lab_que_rep_title">标题</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_cos_catalog">Course catalog</xsl:with-param>
			<xsl:with-param name="lab_course_type">Course type</xsl:with-param>
			<xsl:with-param name="lab_type_self">Web-based training</xsl:with-param>
			<xsl:with-param name="lab_type_class">Classroom training</xsl:with-param>
			<xsl:with-param name="lab_cos">Course</xsl:with-param>
			<xsl:with-param name="lab_all_cos">All courses</xsl:with-param>
			<xsl:with-param name="lab_cos_eva_fom">Course evaluation form</xsl:with-param>
			<xsl:with-param name="lab_enr_period">Enrollment date</xsl:with-param>
			<xsl:with-param name="lab_from">From</xsl:with-param>
			<xsl:with-param name="lab_to">To</xsl:with-param>
			<xsl:with-param name="lab_rep_sum">Report summary</xsl:with-param>
			<xsl:with-param name="lab_tol_enr">Total enrolled learners</xsl:with-param>
			<xsl:with-param name="lab_tol_res">Total responses</xsl:with-param>
			<xsl:with-param name="lab_res_rate">Response rate</xsl:with-param>
			<xsl:with-param name="lab_no">No.</xsl:with-param>
			<xsl:with-param name="lab_que">Question</xsl:with-param>
			<xsl:with-param name="lab_avg_res">Average response</xsl:with-param>
			<xsl:with-param name="lab_avg_score">Average score</xsl:with-param>
			<xsl:with-param name="lab_sd">Standard deviation</xsl:with-param>
			<xsl:with-param name="lab_answered">Answered</xsl:with-param>
			<xsl:with-param name="lab_unanswered">Unanswered</xsl:with-param>
			<xsl:with-param name="lab_que_stat">Question statistics</xsl:with-param>
			<xsl:with-param name="lab_cho">Choice</xsl:with-param>
			<xsl:with-param name="lab_score">Score</xsl:with-param>
			<xsl:with-param name="lab_count">Count</xsl:with-param>
			<xsl:with-param name="lab_percent">Percentage</xsl:with-param>
			<xsl:with-param name="lab_total">Total</xsl:with-param>
			<xsl:with-param name="lab_show">Show summary</xsl:with-param>
			<xsl:with-param name="lab_hide">Hide summary</xsl:with-param>
			<xsl:with-param name="lab_answer">Answer</xsl:with-param>
			<xsl:with-param name="lab_view">View all</xsl:with-param>
			<xsl:with-param name="lab_html_content">HTML content</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ex_rpt">Export</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">My responsible courses</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">Courses that have been enrolled by my responsible learners</xsl:with-param>
			<xsl:with-param name="lab_que_rep_title">Title</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_cos_catalog"/>
		<xsl:param name="lab_course_type"/>
		<xsl:param name="lab_type_self"/>
		<xsl:param name="lab_type_class"/>
		<xsl:param name="lab_cos"/>
		<xsl:param name="lab_cos_eva_fom"/>
		<xsl:param name="lab_enr_period"/>
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_rep_sum"/>
		<xsl:param name="lab_tol_enr"/>
		<xsl:param name="lab_tol_res"/>
		<xsl:param name="lab_res_rate"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_que"/>
		<xsl:param name="lab_avg_res"/>
		<xsl:param name="lab_avg_score"/>
		<xsl:param name="lab_sd"/>
		<xsl:param name="lab_answered"/>
		<xsl:param name="lab_unanswered"/>
		<xsl:param name="lab_que_stat"/>
		<xsl:param name="lab_cho"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_count"/>
		<xsl:param name="lab_percent"/>
		<xsl:param name="lab_total"/>
		<xsl:param name="lab_show"/>
		<xsl:param name="lab_hide"/>
		<xsl:param name="lab_answer"/>
		<xsl:param name="lab_view"/>
		<xsl:param name="lab_html_content"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_g_form_btn_ex_rpt"/>
		<xsl:param name="lab_all_cos"/>
		<xsl:param name="lab_answer_for_course"/>
		<xsl:param name="lab_answer_for_lrn_course"/>
		<xsl:param name="lab_que_rep_title"/>
		<xsl:if test="not(/report/report_list/que_stat_list/que_stat/ans_stat[@view_all='true'])">
			<SCRIPT language="JavaScript" type="text/javascript"><![CDATA[
function showSummary(que) {
	var obj_link = null;
	var obj_content = null;
	if (document.layers) {
	} else {
		if (document.all) {
			obj_content = document.all["summary_content_" + que];
		} else {
			obj_content = document.getElementById("summary_content_" + que);
		}
		if (obj_content != null && obj_content.style.display == "none") {
			obj_content.style.display = "block";
			eval("summary_link_" + que).innerHTML = "]]><xsl:value-of select="$lab_hide"/><![CDATA[";
		} else {
			obj_content.style.display = "none";
			eval("summary_link_" + que).innerHTML = "]]><xsl:value-of select="$lab_show"/><![CDATA[";
		}
	}
}
function initSummary() {
]]><xsl:for-each select="que_stat_list/que_stat"><![CDATA[showSummary(']]><xsl:value-of select="position()"/><![CDATA[');]]></xsl:for-each><![CDATA[}]]></SCRIPT>
		</xsl:if>
		<!-- =============================================================== -->
		<xsl:apply-templates select="report_head">
			<xsl:with-param name="lab_cos_catalog" select="$lab_cos_catalog"/>
			<xsl:with-param name="lab_course_type" select="$lab_course_type"/>
			<xsl:with-param name="lab_type_self" select="$lab_type_self"/>
			<xsl:with-param name="lab_type_class" select="$lab_type_class"/>
			<xsl:with-param name="lab_cos" select="$lab_cos"/>
			<xsl:with-param name="lab_cos_eva_fom" select="$lab_cos_eva_fom"/>
			<xsl:with-param name="lab_enr_period" select="$lab_enr_period"/>
			<xsl:with-param name="lab_from" select="$lab_from"/>
			<xsl:with-param name="lab_to" select="$lab_to"/>
			<xsl:with-param name="lab_all_cos" select="$lab_all_cos"/>
			<xsl:with-param name="lab_answer_for_course" select="$lab_answer_for_course"/>
			<xsl:with-param name="lab_answer_for_lrn_course" select="$lab_answer_for_lrn_course"/>	
		</xsl:apply-templates>
		<xsl:apply-templates select="gen_stat">
			<xsl:with-param name="lab_rep_sum" select="$lab_rep_sum"/>
			<xsl:with-param name="lab_tol_enr" select="$lab_tol_enr"/>
			<xsl:with-param name="lab_tol_res" select="$lab_tol_res"/>
			<xsl:with-param name="lab_res_rate" select="$lab_res_rate"/>
		</xsl:apply-templates>
		<xsl:apply-templates select="que_stat_list">
			<xsl:with-param name="lab_rte_title" select="/report/report_list/report_head/rte_info/@rte_dl_xsl"/>
			<xsl:with-param name="lab_rep_sum" select="$lab_rep_sum"/>
			<xsl:with-param name="lab_no" select="$lab_no"/>
			<xsl:with-param name="lab_que" select="$lab_que"/>
			<xsl:with-param name="lab_avg_res" select="$lab_avg_res"/>
			<xsl:with-param name="lab_avg_score" select="$lab_avg_score"/>
			<xsl:with-param name="lab_sd" select="$lab_sd"/>
			<xsl:with-param name="lab_answered" select="$lab_answered"/>
			<xsl:with-param name="lab_unanswered" select="$lab_unanswered"/>
			<xsl:with-param name="lab_que_stat" select="$lab_que_stat"/>
			<xsl:with-param name="lab_cho" select="$lab_cho"/>
			<xsl:with-param name="lab_score" select="$lab_score"/>
			<xsl:with-param name="lab_count" select="$lab_count"/>
			<xsl:with-param name="lab_percent" select="$lab_percent"/>
			<xsl:with-param name="lab_total" select="$lab_total"/>
			<xsl:with-param name="lab_show" select="$lab_show"/>
			<xsl:with-param name="lab_hide" select="$lab_hide"/>
			<xsl:with-param name="lab_answer" select="$lab_answer"/>
			<xsl:with-param name="lab_view" select="$lab_view"/>
			<xsl:with-param name="lab_html_content" select="$lab_html_content"/>
			<xsl:with-param name="lab_g_form_btn_close" select="$lab_g_form_btn_close"/>
			<xsl:with-param name="lab_g_form_btn_ex_rpt" select="$lab_g_form_btn_ex_rpt"/>
			<xsl:with-param name="lab_que_rep_title" select="$lab_que_rep_title"/>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- report gen_stat -->
	<xsl:template match="gen_stat">
		<xsl:param name="lab_rep_sum"/>
		<xsl:param name="lab_tol_enr"/>
		<xsl:param name="lab_tol_res"/>
		<xsl:param name="lab_res_rate"/>
		<xsl:call-template name="wb_ui_space"/>
		<a name="report_summary"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_rep_sum"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table cellspacing="0" cellpadding="3" border="0">
			<tr>
				<td class="TitleText wzb-form-label">
					<xsl:value-of select="$lab_tol_enr"/>：</td>
				<td class="StatDataText wzb-form-control">
					<xsl:value-of select="@lrn_cnt"/>
				</td>
			</tr>
			<tr>
				<td class="TitleText wzb-form-label">
					<xsl:value-of select="$lab_tol_res"/>：</td>
				<td class="StatDataText wzb-form-control">
					<xsl:value-of select="@res_cnt"/>
				</td>
			</tr>
			<tr>
				<td class="TitleText wzb-form-label">
					<xsl:value-of select="$lab_res_rate"/>(%)：</td>
				<td class="StatDataText wzb-form-control">
					<xsl:value-of select="@res_rate"/>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- report gen_stat end -->
	<!-- report que_stat_list -->
	<xsl:template match="que_stat_list">
		<xsl:param name="lab_rte_title"/>
		<xsl:param name="lab_rep_sum"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_que"/>
		<xsl:param name="lab_avg_res"/>
		<xsl:param name="lab_avg_score"/>
		<xsl:param name="lab_sd"/>
		<xsl:param name="lab_answered"/>
		<xsl:param name="lab_unanswered"/>
		<xsl:param name="lab_que_stat"/>
		<xsl:param name="lab_cho"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_count"/>
		<xsl:param name="lab_percent"/>
		<xsl:param name="lab_total"/>
		<xsl:param name="lab_show"/>
		<xsl:param name="lab_hide"/>
		<xsl:param name="lab_answer"/>
		<xsl:param name="lab_view"/>
		<xsl:param name="lab_html_content"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_g_form_btn_ex_rpt"/>
		<xsl:param name="lab_que_rep_title"/>
		<!-- for each question -->
		<xsl:if test="not(/report/report_list/que_stat_list/que_stat/ans_stat[@view_all='true'])">
			<table cellspacing="0" cellpadding="3" border="0" width="{$wb_gen_table_width}" class="table wzb-ui-table">
				<tr class="SecBg wzb-ui-table-head" valign="bottom">
					<td>
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
					<td>
						<span class="TitleText">
							<xsl:value-of select="$lab_no"/>
						</span>
					</td>
					<td with="200">
						<span class="TitleText">
							<xsl:value-of select="$lab_que_rep_title"/> 
						</span>
					</td>
					<td align="center" colspan="3">
						<span class="TitleText">
							<xsl:value-of select="$lab_avg_res"/>
						</span>
					</td>
					<td align="right">
						<span class="TitleText">
							<xsl:value-of select="$lab_avg_score"/>
						</span>
					</td>
					<td align="right">
						<span class="TitleText">
							<xsl:value-of select="$lab_sd"/>
						</span>
					</td>
					<td align="right">
						<span class="TitleText">
							<xsl:value-of select="$lab_answered"/>
							<br/>(%)</span>
					</td>
					<td align="right">
						<span class="TitleText">
							<xsl:value-of select="$lab_unanswered"/>
							<br/>(%)</span>
					</td>
					<td>
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
				</tr>
				<xsl:for-each select="que_stat">
					<xsl:variable name="position" select="(position() mod 2)"/>
					<xsl:variable name="row_class">
						<xsl:choose>
							<xsl:when test="$position = 0">RowsEven</xsl:when>
							<xsl:otherwise>RowsOdd</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<tr class="{$row_class}">
						<td>
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td>
							<span class="Text">
								<xsl:value-of select="@order"/>
							</span>
						</td>
						<td>
							<span class="Text">
								<a href="#que_{@order}">
									<xsl:choose>
										<xsl:when test="body/html">
											<span class="Text">
												<xsl:call-template name="omit_title">
													<xsl:with-param name="title" select="body/html"/>
												</xsl:call-template>
												<xsl:text>&#160;</xsl:text>
												<!--屏蔽原因:问卷题目显示错误：题目（HTML内容），去掉括号内容-->
												<!--<i>
													<xsl:text>(</xsl:text>
													<xsl:value-of select="$lab_html_content"/>
													<xsl:text>)</xsl:text>
												</i>-->
											</span>
										</xsl:when>
										<xsl:otherwise>
											<xsl:choose>
												<xsl:when test="string-length(body/text()) &gt; 23">
													<xsl:call-template name="omit_title">
														<xsl:with-param name="title" select="body/text()"/>
													</xsl:call-template>
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="unescape_html_linefeed">
														<xsl:with-param name="my_right_value" select="body/text()"/>
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:otherwise>
									</xsl:choose>
								</a>
							</span>
						</td>
						<xsl:choose>
							<xsl:when test="./res_stat/avg_res">
								<xsl:apply-templates select="./res_stat/avg_res">
									<xsl:with-param name="opt_stat" select="opt_stat"/>
									<xsl:with-param name="que_body" select="body"/>
								</xsl:apply-templates>
							</xsl:when>
							<xsl:otherwise>
								<td colspan="3" align="center">--</td>
							</xsl:otherwise>
						</xsl:choose>
						<!-- Average Score .Standard Deviation .Answered .Unanswered -->
						<td align="right">
							<span class="Text">
								<xsl:choose>
									<xsl:when test="res_stat/@avg">
										<xsl:value-of select="res_stat/@avg"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>--</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
						<td align="right">
							<span class="Text">
								<xsl:choose>
									<xsl:when test="res_stat/@sd">
										<xsl:value-of select="res_stat/@sd"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>--</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
						<td align="right">
							<span class="Text">
								<xsl:value-of select="res_stat/answered/@percent"/>
							</span>
						</td>
						<td align="right">
							<span class="Text">
								<xsl:value-of select="res_stat/unanswered/@percent"/>
							</span>
						</td>
						<td>
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
				</xsl:for-each>
			</table>
			<div class="margin-top28"></div>
			<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="text">
					<xsl:value-of select="$lab_que_stat"/>
				</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_line"/>
		</xsl:if>
		<!-- detail -->
		<table cellspacing="0" cellpadding="3" border="0" width="{$wb_gen_table_width}" class="table wzb-ui-table">
			<xsl:for-each select="que_stat">
				<xsl:choose>
					<xsl:when test="./body/interaction/@type = 'MC'">
						<tr class="SecBg wzb-ui-table-head">
							<td valign="top">
								<a name="que_{@order}"/>
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</td>
							<td>
								<span class="TitleText">
									<xsl:value-of select="$lab_no"/>
								</span>
							</td>
							<td>
								<span class="TitleText">
									<xsl:value-of select="$lab_que"/>
								</span>
							</td>
							<td>
								<span class="TitleText">
									<xsl:value-of select="$lab_cho"/>
								</span>
							</td>
							<td align="right">
								<span class="TitleText">
									<xsl:value-of select="$lab_score"/>
								</span>
							</td>
							<td align="right">
								<span class="TitleText">
									<xsl:value-of select="$lab_count"/>
								</span>
							</td>
							<td align="center" colspan="2">
								<span class="TitleText">
									<xsl:value-of select="$lab_percent"/>
								</span>
							</td>
							<td>
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</td>
						</tr>
					</xsl:when>
					<xsl:when test="./body/interaction/@type = 'FB'">
						<TR class="SecBg" vAlign="bottom">
							<TD vAlign="top">
								<A name="que_{@order}">
									<IMG height="1" src="{$wb_img_path}tp.gif" width="1" border="0"/>
								</A>
							</TD>
							<TD>
								<SPAN class="TitleText">
									<xsl:value-of select="$lab_no"/>
								</SPAN>
							</TD>
							<TD>
								<SPAN class="TitleText">
									<xsl:value-of select="$lab_que"/>
								</SPAN>
							</TD>
							<TD colSpan="5">
								<SPAN class="TitleText">
									<xsl:value-of select="$lab_answer"/>
								</SPAN>
							</TD>
							<td>
								<IMG height="1" src="{$wb_img_path}tp.gif" width="1" border="0"/>
							</td>
						</TR>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td>Unknown question type!!!</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:variable name="que_row_span">
					<xsl:choose>
						<xsl:when test="opt_stat">
							<xsl:value-of select="count(opt_stat/opt)+2"/>
						</xsl:when>
						<xsl:when test="ans_stat">
							<xsl:value-of select="count(ans_stat/ans)+2"/>
						</xsl:when>
						<xsl:otherwise>0</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<tr class="RowsOdd">
					<td rowspan="{$que_row_span}" valign="top">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
					<td rowspan="{$que_row_span}" valign="top">
						<span class="Text">
							<xsl:value-of select="@order"/>
						</span>
					</td>
					<td rowspan="{$que_row_span}" valign="top">
						<span class="Text">
							<xsl:choose>
								<xsl:when test="body/html">
									<span class="Text">
										<xsl:call-template name="omit_title">
											<xsl:with-param name="title" select="body/html"/>
										</xsl:call-template>
										<xsl:text>&#160;</xsl:text>
										<!--<i>
											<xsl:text>(</xsl:text>
											<xsl:value-of select="$lab_html_content"/>
											<xsl:text>)</xsl:text>
										</i>-->
									</span>
								</xsl:when>
								<xsl:otherwise>
									<xsl:choose>
										<xsl:when test="string-length(body/text()) &gt; 23">
											<xsl:call-template name="omit_title">
												<xsl:with-param name="title" select="body/text()"/>
											</xsl:call-template>
										</xsl:when>
										<xsl:otherwise>
											<xsl:call-template name="unescape_html_linefeed">
												<xsl:with-param name="my_right_value" select="body/text()"/>
											</xsl:call-template>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
				</tr>
				<xsl:for-each select="opt_stat/opt">
					<xsl:variable name="position" select="(position() mod 2)"/>
					<xsl:variable name="row_class">
						<xsl:choose>
							<xsl:when test="$position = 0">RowsEven</xsl:when>
							<xsl:otherwise>RowsOdd</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="rowcount" select="position()"/>
					<tr class="{$row_class}">
						<td>
							<span class="Text">
								<xsl:variable name="cur_opt_id" select="./@id"/>
								<xsl:call-template name="omit_title">
									<xsl:with-param name="title" select="../../body/interaction/option[@id = $cur_opt_id]"/>
								</xsl:call-template>
							</span>
						</td>
						<td align="right">
							<span class="Text">
								<xsl:value-of select="@score"/>
							</span>
						</td>
						<td align="right">
							<span class="Text">
								<xsl:value-of select="@cnt"/>
							</span>
						</td>
						<td align="right">
							<span class="Text">
								<xsl:value-of select="@percent"/>
							</span>
						</td>
						<xsl:variable name="bar" select="@percent_int"/>
						<td width="102">
							<img src="{$wb_img_path}percent_bar_start.gif" border="0" align="middle"/>
							<xsl:if test="$bar>0">
								<img src="{$wb_img_path}percent_bar_fill.gif" width="{$bar}" height="12" border="0" align="middle"/>
								<img src="{$wb_img_path}percent_bar_end.gif" border="0" align="middle"/>
							</xsl:if>
						</td>
						<td>
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
				</xsl:for-each>
				<xsl:for-each select="ans_stat/ans">
					<xsl:variable name="position" select="(position() mod 2)"/>
					<xsl:variable name="row_class">
						<xsl:choose>
							<xsl:when test="$position = 0">RowsEven</xsl:when>
							<xsl:otherwise>RowsOdd</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<TR class="{$row_class}">
						<TD colSpan="5">
							<SPAN class="Text">
								<xsl:attribute name="title"><xsl:value-of select="."/></xsl:attribute>
								<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:call-template name="omit_title">
											<xsl:with-param name="title" select="."/>
											<xsl:with-param name="showtitle">False</xsl:with-param>
										</xsl:call-template>
									</xsl:with-param>
								</xsl:call-template>
							</SPAN>
						</TD>
						<td>
							<IMG height="1" src="{$wb_img_path}tp.gif" width="1" border="0"/>
						</td>
					</TR>
				</xsl:for-each>
				<xsl:variable name="totalcolor">
					<xsl:choose>
						<xsl:when test="($que_row_span+1) mod 2 = 0">RowsEven</xsl:when>
						<xsl:otherwise>RowsOdd</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<tr class="{$totalcolor}">
					<td>
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
					<td align="right">
						<span class="Text">
							<xsl:value-of select="$lab_total"/>
						</span>
					</td>
					<td align="right">
						<span class="Text">
							<xsl:value-of select="./res_stat/@cnt"/>
						</span>
					</td>
					<xsl:choose>
						<xsl:when test="ans_stat[@view_all='false']">
							<TD align="middle" colSpan="2">
								<xsl:choose>
									<xsl:when test="./res_stat/@cnt != count(ans_stat/ans)">
										<A href="Javascript:mgt_rpt.get_rpt_by_que('{position()}')">
											<xsl:value-of select="$lab_view"/>
										</A>
									</xsl:when>
									<xsl:otherwise>
										<IMG height="1" src="{$wb_img_path}tp.gif" width="1" border="0"/>
									</xsl:otherwise>
								</xsl:choose>
							</TD>
						</xsl:when>
						<xsl:otherwise>
							<TD align="middle" colSpan="2">
								<IMG height="1" src="{$wb_img_path}tp.gif" width="1" border="0"/>
							</TD>
						</xsl:otherwise>
					</xsl:choose>
					<td>
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
				</tr>
				<TR vAlign="top">
					<TD>
						<IMG height="1" src="{$wb_img_path}tp.gif" width="1" border="0"/>
					</TD>
					<TD align="right" colSpan="7">
						<xsl:if test="not(ans_stat[@view_all='true'])">
							<SPAN class="Text">
								<A id="summary_link_{position()}" href="javascript:showSummary('{position()}')"/>&#160;&#160;<A href="#report_summary">
									<xsl:value-of select="$lab_rep_sum"/>
								</A>
							</SPAN>
						</xsl:if>
						<DIV id="summary_content_{position()}" style="DISPLAY:block">
							<xsl:apply-templates select="res_stat">
								<xsl:with-param name="lab_avg_res" select="$lab_avg_res"/>
								<xsl:with-param name="lab_avg_score" select="$lab_avg_score"/>
								<xsl:with-param name="lab_sd" select="$lab_sd"/>
								<xsl:with-param name="lab_answered" select="$lab_answered"/>
								<xsl:with-param name="lab_unanswered" select="$lab_unanswered"/>
								<xsl:with-param name="opt_stat" select="opt_stat"/>
								<xsl:with-param name="que_body" select="body"/>
							</xsl:apply-templates>
						</DIV>
					</TD>
					<TD>
						<IMG height="1" src="{$wb_img_path}tp.gif" width="1" border="0"/>
					</TD>
				</TR>
			</xsl:for-each>
		</table>
		<div class="wzb-bar">
			<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
				<tr>
					<td align="center">
						<xsl:choose>
							<xsl:when test="/report/report_list/que_stat_list/que_stat/ans_stat[@view_all ='true']"/>
							<xsl:otherwise>
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ex_rpt"/>
									<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.rslt_dl_rpt_adv('<xsl:value-of select="$lab_rte_title"/>','')</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_close"/>
							<xsl:with-param name="wb_gen_btn_href">Javascript:window.close()</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	<!-- report que_stat_list end -->
	<!-- report head starts here -->
	<xsl:template match="report_head">
		<xsl:param name="lab_cos_catalog"/>
		<xsl:param name="lab_course_type"/>
		<xsl:param name="lab_type_self"/>
		<xsl:param name="lab_type_class"/>
		<xsl:param name="lab_cos"/>
		<xsl:param name="lab_cos_eva_fom"/>
		<xsl:param name="lab_enr_period"/>
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_all_cos"/>
		<xsl:param name="lab_answer_for_course"/>
		<xsl:param name="lab_answer_for_lrn_course"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="/report/report_list/rsp_title">
						<xsl:value-of select="/report/report_list/rsp_title"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="get_rte_title">
							<xsl:with-param name="rte_type">SURVEY_QUE_GRP</xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
			<tr>
				<td width="200" align="right" height="10">
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td>
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
			<xsl:if test="itm_title_list/itm_title or ($tc_enabled='true' and all_cos_ind='true')">
				<tr>
					<td width="200" align="right" valign="top" class="wzb-form-label">
							<span class="TitleText">
								<xsl:value-of select="$lab_cos"/>：
							</span>
					</td>
					<td valign="top" class="wzb-form-control" >
						<xsl:choose>
							<xsl:when test="$tc_enabled='true'">
							<xsl:choose>
								<xsl:when test="all_cos_ind='true'">
								 	<xsl:choose>
										<xsl:when test="answer_for_course='true' and answer_for_lrn_course='false'">
												<span class="TitleText">
													<xsl:value-of select="$lab_answer_for_course"/>
											       </span>
										</xsl:when>
										<xsl:when test="answer_for_course='false' and answer_for_lrn_course='true'">
			  								<span class="TitleText">
												<xsl:value-of select="$lab_answer_for_lrn_course"/>
											</span>
										</xsl:when>
										<xsl:otherwise>
			  								<span class="TitleText">
												<xsl:value-of select="$lab_all_cos"/>
											</span>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:when>
								<xsl:otherwise>
									<xsl:for-each select="itm_title_list/itm_title">
										<span class="Text">
											<xsl:value-of select="."/>
											<xsl:if test="position()!=last()">,<xsl:text>&#160;</xsl:text>
											</xsl:if>
										</span>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
							<xsl:if test="itm_title_list/itm_title">
									<xsl:for-each select="itm_title_list/itm_title">
										<span class="Text">
											<xsl:value-of select="."/>
											<xsl:if test="position()!=last()">,<xsl:text>&#160;</xsl:text>
											</xsl:if>
										</span>
									</xsl:for-each>
							</xsl:if>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</xsl:if>
		
			<xsl:if test="tnd_title_list/tnd_title">
				<tr>
					<td align="right" valign="top"  class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_cos_catalog"/>：</span>
					</td>
					<td valign="top" class="wzb-form-control">
						<xsl:for-each select="tnd_title_list/tnd_title">
							<span class="Text">
								<xsl:value-of select="."/>
								<xsl:if test="position()!=last()">,<xsl:text>&#160;</xsl:text>
								</xsl:if>
							</span>
						</xsl:for-each>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="item_type_list/item_type">
				<tr>
					<td width="200" align="right" valign="top" class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_course_type"/>：</span>
					</td>
					<td valign="top" class="wzb-form-control">
						<span class="Text">
							<xsl:for-each select="item_type_list/item_type">
								<xsl:call-template name="get_ity_title">
									<xsl:with-param name="itm_type" select="@dummy_type"/>
								</xsl:call-template>
								<xsl:if test="position()!=last()">,<xsl:text>&#160;</xsl:text>
								</xsl:if>
							</xsl:for-each>
						</span>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td width="200" align="right" valign="top" class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_cos_eva_fom"/>：</span>
				</td>
				<td valign="top" class="wzb-form-control">
					<span class="Text">
						<xsl:value-of select="mod_title/text()"/>
					</span>
				</td>
			</tr>
			<xsl:if test="period">
				<tr>
					<td align="right" valign="top" class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_enr_period"/>：</span>
					</td>
					<td valign="top" class="wzb-form-control">
						<xsl:if test="period">
							<span class="Text">
								<xsl:choose>
									<xsl:when test="period/@from">
										<xsl:value-of select="$lab_from"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:call-template name="display_time">
											<xsl:with-param name="my_timestamp">
												<xsl:value-of select="period/@from"/>
											</xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_from"/>
										<xsl:text>&#160;--&#160;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="period/@to">
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="$lab_to"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:call-template name="display_time">
											<xsl:with-param name="my_timestamp">
												<xsl:value-of select="period/@to"/>
											</xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="$lab_to"/>
										<xsl:text>&#160;--</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</xsl:if>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td align="right" height="10">
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td>
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="res_stat">
		<xsl:param name="lab_avg_res"/>
		<xsl:param name="lab_avg_score"/>
		<xsl:param name="lab_sd"/>
		<xsl:param name="lab_answered"/>
		<xsl:param name="lab_unanswered"/>
		<xsl:param name="opt_stat"/>
		<xsl:param name="que_body"/>
		<TABLE cellSpacing="0" cellPadding="3" border="0">
			<xsl:if test="avg_res">
				<TR>
					<td align="right">
						<span class="TitleText">
							<xsl:value-of select="$lab_avg_res"/>
						</span>
					</td>
					<xsl:apply-templates select="avg_res">
						<xsl:with-param name="opt_stat" select="$opt_stat"/>
						<xsl:with-param name="que_body" select="$que_body"/>
					</xsl:apply-templates>
				</TR>
			</xsl:if>
			<xsl:if test="@avg">
				<TR>
					<TD align="right">
						<SPAN class="TitleText">
							<xsl:value-of select="$lab_avg_score"/>
						</SPAN>
					</TD>
					<TD align="right">
						<SPAN class="Text">
							<xsl:value-of select="@avg"/>
						</SPAN>
					</TD>
				</TR>
			</xsl:if>
			<xsl:if test="@sd">
				<TR>
					<TD align="right">
						<SPAN class="TitleText">
							<xsl:value-of select="$lab_sd"/>
						</SPAN>
					</TD>
					<TD align="right">
						<SPAN class="Text">
							<xsl:value-of select="@sd"/>
						</SPAN>
					</TD>
				</TR>
			</xsl:if>
			<TR>
				<TD align="right">
					<SPAN class="TitleText">
						<xsl:value-of select="$lab_answered"/>(%)</SPAN>
				</TD>
				<TD align="right">
					<SPAN class="Text">
						<xsl:value-of select="answered/@percent"/>
					</SPAN>
				</TD>
			</TR>
			<TR>
				<TD align="right">
					<SPAN class="TitleText">
						<xsl:value-of select="$lab_unanswered"/>(%)</SPAN>
				</TD>
				<TD align="right">
					<SPAN class="Text">
						<xsl:value-of select="unanswered/@percent"/>
					</SPAN>
				</TD>
			</TR>
		</TABLE>
	</xsl:template>
	<xsl:template match="avg_res">
		<xsl:param name="opt_stat"/>
		<xsl:param name="que_body"/>
		<xsl:variable name="bar" select="@percent_int"/>
		<td align="right" nowrap="nowrap">
			<span class="Text">
				<xsl:variable name="firstoption" select="$opt_stat/opt[1]/@score"/>
				<xsl:variable name="totalTitle">
					<xsl:for-each select="$opt_stat/opt[@score = $firstoption]">
						<xsl:variable name="nowid" select="./@id"/>
						<xsl:value-of select="$que_body/interaction/option[@id = $nowid]/text()"/>
						<xsl:if test="position()!=last()">
							<xsl:text>,&#160;</xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<xsl:call-template name="omit_title">
					<xsl:with-param name="title" select="$totalTitle"/>
					<xsl:with-param name="retain_length">5</xsl:with-param>
					<xsl:with-param name="display_length">13</xsl:with-param>
				</xsl:call-template>
			</span>
		</td>
		<td width="104">
			<xsl:if test="(100 - $bar)>0">
				<img src="{$wb_img_path}percent_bar_end.gif" border="0" align="middle"/>
				<img src="{$wb_img_path}percent_bar_fill.gif" width="{100 - $bar}" height="12" border="0" align="middle"/>
			</xsl:if>
			<img src="{$wb_img_path}percent_bar_start.gif" border="0" align="middle"/>
			<xsl:if test="$bar>0">
				<img src="{$wb_img_path}percent_bar_fill.gif" width="{$bar}" height="12" border="0" align="middle"/>
				<img src="{$wb_img_path}percent_bar_end.gif" border="0" align="middle"/>
			</xsl:if>
		</td>
		<td align="left" nowrap="nowrap">
			<span class="Text">
				<xsl:variable name="lastopt" select="count($opt_stat/opt)"/>
				<xsl:variable name="lastscore" select="$opt_stat/opt[$lastopt]/@score"/>
				<xsl:variable name="totalLastTitle">
					<xsl:for-each select="$opt_stat/opt[@score = $lastscore]">
						<xsl:variable name="nowid" select="./@id"/>
						<xsl:value-of select="$que_body/interaction/option[@id = $nowid]/text()"/>
						<xsl:if test="position()!=last()">
							<xsl:text>,&#160;</xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<xsl:call-template name="omit_title">
					<xsl:with-param name="title" select="$totalLastTitle"/>
					<xsl:with-param name="retain_length">5</xsl:with-param>
					<xsl:with-param name="display_length">13</xsl:with-param>
				</xsl:call-template>
				<!-- same score operation -->
			</span>
		</td>
	</xsl:template>
</xsl:stylesheet>
