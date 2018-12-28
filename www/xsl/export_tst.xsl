<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<!-- share -->
	<xsl:import href="share/export_tst_test_paper.xsl"/>
	<xsl:import href="share/export_tst_answersheet.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="export_mode" select="/quiz/meta/export_mode"/>
	<xsl:variable name="wb_doc_table_width">600</xsl:variable>
	<!--	<xsl:template match="/">
		<xsl:copy-of select="*"/>
	</xsl:template>-->
	<!-- =============================================================== -->
	<xsl:template match="/quiz ">
		<xsl:call-template name="wb_init_lab"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_test_no">測驗編號</xsl:with-param>
			<xsl:with-param name="lab_number_of_question">試題總數</xsl:with-param>
			<xsl:with-param name="lab_pass_percent">合格率</xsl:with-param>
			<xsl:with-param name="lab_duration">測驗所需時間</xsl:with-param>
			<xsl:with-param name="lab_score">分數</xsl:with-param>
			<xsl:with-param name="lab_total_score">總分</xsl:with-param>
			<xsl:with-param name="lab_answer">答案</xsl:with-param>
			<xsl:with-param name="lab_min">分鐘</xsl:with-param>
			<xsl:with-param name="lab_instr">以下的正確答案以灰色突出顯示</xsl:with-param>
			<xsl:with-param name="lab_question">題目</xsl:with-param>
			<xsl:with-param name="lab_question_id">資源編號</xsl:with-param>
			<xsl:with-param name="lab_question_title">標題</xsl:with-param>
			<xsl:with-param name="lab_objective">資源文件夾</xsl:with-param>
			<xsl:with-param name="lab_desc">簡介</xsl:with-param>
			<xsl:with-param name="lab_explanation">說明</xsl:with-param>
			<xsl:with-param name="lab_true">是</xsl:with-param>
			<xsl:with-param name="lab_false">否</xsl:with-param>
			<xsl:with-param name="lab_unlimited">不限時</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_test_no">测验编号</xsl:with-param>
			<xsl:with-param name="lab_number_of_question">试题总数</xsl:with-param>
			<xsl:with-param name="lab_pass_percent">合格率</xsl:with-param>
			<xsl:with-param name="lab_duration">测验所需时间</xsl:with-param>
			<xsl:with-param name="lab_score">分值</xsl:with-param>
			<xsl:with-param name="lab_total_score">总分</xsl:with-param>
			<xsl:with-param name="lab_answer">答案</xsl:with-param>
			<xsl:with-param name="lab_min">分钟</xsl:with-param>
			<xsl:with-param name="lab_instr">以下的正确答案以灰色突出显示</xsl:with-param>
			<xsl:with-param name="lab_question">题目</xsl:with-param>
			<xsl:with-param name="lab_question_id">资源编号</xsl:with-param>
			<xsl:with-param name="lab_question_title">标题</xsl:with-param>
			<xsl:with-param name="lab_objective">资源文件夹</xsl:with-param>
			<xsl:with-param name="lab_desc">简介</xsl:with-param>
			<xsl:with-param name="lab_explanation">说明</xsl:with-param>
			<xsl:with-param name="lab_true">对</xsl:with-param>
			<xsl:with-param name="lab_false">错</xsl:with-param>
			<xsl:with-param name="lab_unlimited">不限时</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_test_no">Test number</xsl:with-param>
			<xsl:with-param name="lab_number_of_question">Total mumber of questions</xsl:with-param>
			<xsl:with-param name="lab_pass_percent">Passing percentage</xsl:with-param>
			<xsl:with-param name="lab_duration">Test duration</xsl:with-param>
			<xsl:with-param name="lab_score">Score</xsl:with-param>
			<xsl:with-param name="lab_total_score">Total score</xsl:with-param>
			<xsl:with-param name="lab_answer">Answer</xsl:with-param>
			<xsl:with-param name="lab_min">minutes</xsl:with-param>
			<xsl:with-param name="lab_instr">The correct answers below are highlighted in grey.</xsl:with-param>
			<xsl:with-param name="lab_question">Question</xsl:with-param>
			<xsl:with-param name="lab_question_id">Question ID</xsl:with-param>
			<xsl:with-param name="lab_question_title">Title</xsl:with-param>
			<xsl:with-param name="lab_objective">Folder</xsl:with-param>
			<xsl:with-param name="lab_desc">Description</xsl:with-param>
			<xsl:with-param name="lab_explanation">Explanation</xsl:with-param>
			<xsl:with-param name="lab_true">True</xsl:with-param>
			<xsl:with-param name="lab_false">False</xsl:with-param>
			<xsl:with-param name="lab_unlimited">不限時</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_test_no"/>
		<xsl:param name="lab_number_of_question"/>
		<xsl:param name="lab_question"/>
		<xsl:param name="lab_pass_percent"/>
		<xsl:param name="lab_duration"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_total_score"/>
		<xsl:param name="lab_answer"/>
		<xsl:param name="lab_min"/>
		<xsl:param name="lab_instr"/>
		<xsl:param name="lab_question_id"/>
		<xsl:param name="lab_question_title"/>
		<xsl:param name="lab_objective"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_explanation"/>
		<xsl:param name="lab_true"/>
		<xsl:param name="lab_false"/>
		<xsl:param name="lab_unlimited"/>
		<xsl:choose>
			<xsl:when test="$export_mode = '1' or $export_mode = '2'">
				<xsl:call-template name="test_paper">
					<xsl:with-param name="lab_test_no" select="$lab_test_no"/>
					<xsl:with-param name="lab_number_of_question" select="$lab_number_of_question"/>
					<xsl:with-param name="lab_pass_percent" select="$lab_pass_percent"/>
					<xsl:with-param name="lab_duration" select="$lab_duration"/>
					<xsl:with-param name="lab_score" select="$lab_score"/>
					<xsl:with-param name="lab_total_score" select="$lab_total_score"/>
					<xsl:with-param name="lab_answer" select="$lab_answer"/>
					<xsl:with-param name="lab_min" select="$lab_min"/>
					<xsl:with-param name="lab_instr" select="$lab_instr"/>
					<xsl:with-param name="lab_question_id" select="$lab_question_id"/>
					<xsl:with-param name="lab_question_title" select="$lab_question_title"/>
					<xsl:with-param name="lab_objective" select="$lab_objective"/>
					<xsl:with-param name="lab_desc" select="$lab_desc"/>
					<xsl:with-param name="lab_explanation" select="$lab_explanation"/>
					<xsl:with-param name="lab_true" select="$lab_true"/>
					<xsl:with-param name="lab_false" select="$lab_false"/>
					<xsl:with-param name="lab_unlimited" select="$lab_unlimited"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$export_mode = '3'">
				<xsl:call-template name="answersheet">
					<xsl:with-param name="lab_test_no" select="$lab_test_no"/>
					<xsl:with-param name="lab_question" select="$lab_question"/>
					<xsl:with-param name="lab_score" select="$lab_score"/>
					<xsl:with-param name="lab_answer" select="$lab_answer"/>
					<xsl:with-param name="lab_total_score" select="$lab_total_score"/>
					<xsl:with-param name="lab_true" select="$lab_true"/>
					<xsl:with-param name="lab_false" select="$lab_false"/>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
