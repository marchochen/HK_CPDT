<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
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
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="share/itm_gen_details_share.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="share/label_rpt.xsl"/>
	<xsl:import href="share/label_role.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="att_status_num" select="count(report/report_body/meta/attendance_status_list/status)"/>
	<xsl:variable name="rpt_name" select="report/report_body/spec/title"/>
	<xsl:variable name="rsp_id" select="/report/report_body/spec/@spec_id"/>
	<xsl:variable name="role_list" select="/report/report_body/meta/role_list"/>
	<xsl:variable name="attendance_status_list" select="/report/report_body/meta/attendance_status_list"/>
	<xsl:variable name="rpt_type" select="/report/report_body/template/@type"/>
	<xsl:variable name="ent_id" select="/report/meta/cur_usr/@ent_id"/>
	<xsl:variable name="que_cnt" select="count(/report/report_body/presentation/data[@name='mod_id']/survey/question/body/interaction[@type = 'MC'])"/>
	<xsl:variable name="tc_enabled" select="/report/meta/tc_enabled"/>
	<xsl:variable name="col_size">
		<xsl:choose>
			<xsl:when test="count(/report/report_body/report_list/data) > 0">
				<xsl:choose>
					<xsl:when test="/report/report_body/spec/data_list/data[@name = 'content_lst' and @value = 'question']">
						<xsl:value-of select="count(/report/report_body/spec/data_list/data[@name = 'itm_content_lst' or @name = 'content_lst']) + $que_cnt"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="count(/report/report_body/spec/data_list/data[@name = 'itm_content_lst' or @name = 'content_lst'])"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>2</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/report">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
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
		<BODY leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
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
			<div id="overDiv" style="position:absolute; visibility:hide;z-index:2;"/>
		</BODY>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_svy_cos_rst">調查問卷結果</xsl:with-param>
			<xsl:with-param name="lab_inst">報告標準</xsl:with-param>
			<xsl:with-param name="lab_rpt_details">報告資料</xsl:with-param>
			<xsl:with-param name="lab_cos_title">課程標題</xsl:with-param>
			<xsl:with-param name="lab_cos_catalog">課程目錄</xsl:with-param>
			<xsl:with-param name="lab_course_type">課程類型</xsl:with-param>
			<xsl:with-param name="lab_cos">課程</xsl:with-param>
			<xsl:with-param name="lab_all_cos">所有課程</xsl:with-param>
			<xsl:with-param name="lab_version">版本</xsl:with-param>
			<xsl:with-param name="lab_latest_version">顯示最新版本</xsl:with-param>
			<xsl:with-param name="lab_all_version">顯示所有版本</xsl:with-param>
			<xsl:with-param name="lab_period">期限</xsl:with-param>
			<xsl:with-param name="lab_to"> 至 </xsl:with-param>
			<xsl:with-param name="lab_not_specified">未指定</xsl:with-param>
			<xsl:with-param name="lab_survey">課程評估表</xsl:with-param>
			<xsl:with-param name="lab_content">內容</xsl:with-param>
			<xsl:with-param name="lab_item_content">課程內容</xsl:with-param>
			<xsl:with-param name="lab_run_content">
				<xsl:value-of select="$lab_const_run"/>內容</xsl:with-param>
			<xsl:with-param name="lab_others_content">調查問卷結果</xsl:with-param>
			<xsl:with-param name="lab_attendance">考勤</xsl:with-param>
			<xsl:with-param name="lab_run_num">
				<xsl:value-of select="$lab_const_run"/>數目</xsl:with-param>
			<xsl:with-param name="lab_question_rating">平均評分</xsl:with-param>
			<xsl:with-param name="lab_overall_rating">總平均</xsl:with-param>
			<xsl:with-param name="lab_no_item">沒有記錄</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_plz_click">請按</xsl:with-param>
			<xsl:with-param name="lab_export">這裡</xsl:with-param>
			<xsl:with-param name="lab_csv_format">匯出為MS Excel相容的格式</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_detail">詳情</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export_subn">提交匯出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ex_rpt">匯出</xsl:with-param>
			<xsl:with-param name="lab_user_content">學員資料</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">儲存</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">我負責的課程</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">我負責學員報讀的課程</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_svy_cos_rst">评估问卷结果</xsl:with-param>
			<xsl:with-param name="lab_inst">报告标准</xsl:with-param>
			<xsl:with-param name="lab_rpt_details">报告资料</xsl:with-param>
			<xsl:with-param name="lab_cos_title">课程标题</xsl:with-param>
			<xsl:with-param name="lab_cos_catalog">课程目录</xsl:with-param>
			<xsl:with-param name="lab_course_type">课程类型</xsl:with-param>
			<xsl:with-param name="lab_cos">课程</xsl:with-param>
			<xsl:with-param name="lab_all_cos">所有課程</xsl:with-param>
			<xsl:with-param name="lab_version">版本</xsl:with-param>
			<xsl:with-param name="lab_latest_version">显示最新版本</xsl:with-param>
			<xsl:with-param name="lab_all_version">显示所有版本</xsl:with-param>
			<xsl:with-param name="lab_period">期限</xsl:with-param>
			<xsl:with-param name="lab_to"> 至 </xsl:with-param>
			<xsl:with-param name="lab_not_specified">未指定</xsl:with-param>
			<xsl:with-param name="lab_survey">课程评估问卷</xsl:with-param>
			<xsl:with-param name="lab_content">内容</xsl:with-param>
			<xsl:with-param name="lab_item_content">课程内容</xsl:with-param>
			<xsl:with-param name="lab_run_content">
				<xsl:value-of select="$lab_const_run"/>内容</xsl:with-param>
			<xsl:with-param name="lab_others_content">评估问卷结果</xsl:with-param>
			<xsl:with-param name="lab_attendance">考勤</xsl:with-param>
			<xsl:with-param name="lab_run_num">
				<xsl:value-of select="$lab_const_run"/>数目</xsl:with-param>
			<xsl:with-param name="lab_question_rating">平均评分</xsl:with-param>
			<xsl:with-param name="lab_overall_rating">总平均</xsl:with-param>
			<xsl:with-param name="lab_no_item">没有记录</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_plz_click">请点击</xsl:with-param>
			<xsl:with-param name="lab_export">这里</xsl:with-param>
			<xsl:with-param name="lab_csv_format">导出为MS Excel兼容的格式</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_detail">详情</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export_subn">导出提交</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ex_rpt">导出</xsl:with-param>
			<xsl:with-param name="lab_user_content">学员资料</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">保存</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">我负责的课程</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">我负责学员报读的课程</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_svy_cos_rst">Evaluation results</xsl:with-param>
			<xsl:with-param name="lab_inst">Report criteria</xsl:with-param>
			<xsl:with-param name="lab_rpt_details">Report details</xsl:with-param>
			<xsl:with-param name="lab_cos_catalog">Course catalog</xsl:with-param>
			<xsl:with-param name="lab_cos_title">Course title</xsl:with-param>
			<xsl:with-param name="lab_not_specified">-- All --</xsl:with-param>
			<xsl:with-param name="lab_content">Content</xsl:with-param>
			<xsl:with-param name="lab_item_content">Course content</xsl:with-param>
			<xsl:with-param name="lab_run_content">
				<xsl:value-of select="$lab_const_run"/> content</xsl:with-param>
			<xsl:with-param name="lab_others_content">Evaluation results</xsl:with-param>
			<xsl:with-param name="lab_course_type">Course type</xsl:with-param>
			<xsl:with-param name="lab_cos">Course</xsl:with-param>
			<xsl:with-param name="lab_all_cos">All courses</xsl:with-param>
			<xsl:with-param name="lab_user_content">User information</xsl:with-param>
			<xsl:with-param name="lab_all_version">All vesrion</xsl:with-param>
			<xsl:with-param name="lab_latest_version">Latest version</xsl:with-param>
			<xsl:with-param name="lab_version">Version</xsl:with-param>
			<xsl:with-param name="lab_period">Period</xsl:with-param>
			<xsl:with-param name="lab_to"> to </xsl:with-param>
			<xsl:with-param name="lab_no_item">No record found</xsl:with-param>
			<xsl:with-param name="lab_attendance">Attendance</xsl:with-param>
			<xsl:with-param name="lab_run_num">Number of <xsl:value-of select="$lab_const_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_plz_click">Please click</xsl:with-param>
			<xsl:with-param name="lab_export">here</xsl:with-param>
			<xsl:with-param name="lab_csv_format">to export the report to MS Excel compatible format.</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_detail">Detail</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export_subn">Export submission</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ex_rpt">Export</xsl:with-param>
			<xsl:with-param name="lab_survey">Course evaluation form</xsl:with-param>
			<xsl:with-param name="lab_question_rating">Average ratings</xsl:with-param>
			<xsl:with-param name="lab_overall_rating">Overall average</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">Save</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">My responsible sourses</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">Courses that have been enrolled by my responsible learners</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_rpt_details"/>
		<xsl:param name="lab_inst"/>
		<xsl:param name="lab_cos_catalog"/>
		<xsl:param name="lab_cos_title"/>
		<xsl:param name="lab_not_specified"/>
		<xsl:param name="lab_item_content"/>
		<xsl:param name="lab_user_content"/>
		<xsl:param name="lab_run_content"/>
		<xsl:param name="lab_others_content"/>
		<xsl:param name="lab_content"/>
		<xsl:param name="lab_course_type"/>
		<xsl:param name="lab_all_version"/>
		<xsl:param name="lab_latest_version"/>
		<xsl:param name="lab_version"/>
		<xsl:param name="lab_period"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_attendance"/>
		<xsl:param name="lab_run_num"/>
		<xsl:param name="lab_plz_click"/>
		<xsl:param name="lab_export"/>
		<xsl:param name="lab_csv_format"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_g_form_btn_detail"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_g_form_btn_export_subn"/>
		<xsl:param name="lab_g_form_btn_ex_rpt"/>
		<xsl:param name="lab_survey"/>
		<xsl:param name="lab_question_rating"/>
		<xsl:param name="lab_overall_rating"/>
		<xsl:param name="lab_g_form_btn_save"/>
		<xsl:param name="lab_cos"/>
		<xsl:param name="lab_all_cos"/>
		<xsl:param name="lab_answer_for_course"/>
		<xsl:param name="lab_answer_for_lrn_course"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$rpt_name !=''">
						<xsl:value-of select="$rpt_name"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="get_rte_title">
							<xsl:with-param name="rte_type" select="$rpt_type"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<!-- Start Item List -->
		<xsl:choose>
			<xsl:when test="count(/report/report_body/report_list/data) = 0">
				<xsl:call-template name="report_criteria">
					<xsl:with-param name="this_width" select="$wb_gen_table_width"/>
					<xsl:with-param name="lab_cos_title" select="$lab_cos_title"/>
					<xsl:with-param name="lab_cos_catalog" select="$lab_cos_catalog"/>
					<xsl:with-param name="lab_course_type" select="$lab_course_type"/>
					<xsl:with-param name="lab_survey" select="$lab_survey"/>
					<xsl:with-param name="lab_cos" select="$lab_cos"/>
					<xsl:with-param name="lab_all_cos" select="$lab_all_cos"/>
					<xsl:with-param name="lab_answer_for_course" select="$lab_answer_for_course"/>
					<xsl:with-param name="lab_answer_for_lrn_course" select="$lab_answer_for_lrn_course"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_item"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="title">
					<xsl:call-template name="escape_js">
						<xsl:with-param name="input_str" select="report_body/spec/title/text()"/>
					</xsl:call-template>
				</xsl:variable>
				<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr>
						<td colspan="{$col_size + 2}">
							<xsl:call-template name="report_criteria">
								<xsl:with-param name="this_width">100%</xsl:with-param>
								<xsl:with-param name="lab_cos_title" select="$lab_cos_title"/>
								<xsl:with-param name="lab_cos_catalog" select="$lab_cos_catalog"/>
								<xsl:with-param name="lab_course_type" select="$lab_course_type"/>
								<xsl:with-param name="lab_survey" select="$lab_survey"/>
								<xsl:with-param name="lab_cos" select="$lab_cos"/>
								<xsl:with-param name="lab_all_cos" select="$lab_all_cos"/>
								<xsl:with-param name="lab_answer_for_course" select="$lab_answer_for_course"/>
								<xsl:with-param name="lab_answer_for_lrn_course" select="$lab_answer_for_lrn_course"/>
							</xsl:call-template>
						</td>
					</tr>
				</table>
				<!-- start draw table header -->
				<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="table wzb-ui-table">
					
					<xsl:if test="count(/report/report_body/spec/data_list/data[@name = 'content_lst' and @value = 'attendance']) &gt; 0 or count(/report/report_body/spec/data_list/data[@name = 'content_lst' and @value = 'question']) &gt; 0">
						<tr class="SecBg">
							<xsl:for-each select="/report/report_body/spec/data_list/data[@name = 'content_lst' or @name = 'itm_content_lst' or @name='run_content_lst']">
								<xsl:choose>
									<xsl:when test="@name = 'content_lst' and @value = 'attendance'">
										<td colspan="4" align="center">
											<span class="TitleText">
												<xsl:value-of select="$lab_attendance"/>
											</span>
										</td>
									</xsl:when>
									<xsl:when test="@name = 'content_lst' and @value = 'question' and $que_cnt &gt; 0">
										<td colspan="{$que_cnt}" align="center">
											<span class="TitleText">
												<xsl:value-of select="$lab_question_rating"/>
											</span>
										</td>
									</xsl:when>
									<xsl:otherwise>
										<td>
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:for-each>
						</tr>
					</xsl:if>
					<tr class="SecBg wzb-ui-table-head">
						<xsl:apply-templates select="/report/report_body/spec/data_list/data[@name = 'itm_content_lst']" mode="title"/>
						<xsl:apply-templates select="/report/report_body/spec/data_list/data[@name = 'run_content_lst']" mode="title"/>
						<xsl:apply-templates select="/report/report_body/spec/data_list/data[@name = 'content_lst']" mode="title">
							<xsl:with-param name="lab_attendance" select="$lab_attendance"/>
							<xsl:with-param name="lab_run_num" select="$lab_run_num"/>
							<xsl:with-param name="lab_overall_rating" select="$lab_overall_rating"/>
						</xsl:apply-templates>
					</tr>
					<xsl:apply-templates select="report_body/report_list/data">
						<xsl:with-param name="lab_na" select="$lab_na"/>
						<xsl:with-param name="lab_g_form_btn_detail" select="$lab_g_form_btn_detail"/>
						<xsl:with-param name="lab_g_form_btn_export_subn" select="$lab_g_form_btn_export_subn"/>
						<xsl:with-param name="lab_g_form_btn_ex_rpt" select="$lab_g_form_btn_ex_rpt"/>
					</xsl:apply-templates>
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="report_body/pagination/@cur_page"/>
					<xsl:with-param name="page_size" select="report_body/pagination/@page_size"/>
					<xsl:with-param name="total" select="report_body/pagination/@total_rec"/>
					<xsl:with-param name="timestamp" select="report_body/pagination/@timestamp"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<div class="wzb-bar">
			<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
				<tr>
					<td align="center">
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ex_rpt"/>
							<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.rslt_dl_rpt_adv('<xsl:value-of select="/report/report_body/template/xsl_list/xsl[@type='download']/."/>', '<xsl:value-of select="$rpt_name"/>')</xsl:with-param>
						</xsl:call-template>
						<!--					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_save"/>
							<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.ins_rpt_res_prep_popup(document.frmXml,'<xsl:value-of select="$rsp_id"/>','','<xsl:value-of select="$ent_id"/>','<xsl:value-of select="$rpt_type"/>')</xsl:with-param>
						</xsl:call-template>-->
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
	<!-- =============================================================== -->
	<xsl:template match="data">
		<xsl:param name="my_class"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_g_form_btn_detail"/>
		<xsl:param name="lab_g_form_btn_export_subn"/>
		<xsl:param name="lab_g_form_btn_ex_rpt"/>
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="$my_class != ''">
					<xsl:value-of select="$my_class"/>
				</xsl:when>
				<xsl:when test="position() mod 2">RowsEven</xsl:when>
				<xsl:otherwise>RowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- DENNIS -->
		<xsl:variable name="font_style">Text</xsl:variable>
		<tr class="{$row_class}">
			<xsl:apply-templates select="item/valued_template/section/*" mode="value">
				<xsl:with-param name="lab_na" select="$lab_na"/>
				<xsl:with-param name="this" select="."/>
			</xsl:apply-templates>
			<xsl:choose>
				<xsl:when test="run">
					<xsl:apply-templates select="run/valued_template/section/*" mode="value">
						<xsl:with-param name="this" select="."/>
						<xsl:with-param name="lab_na" select="$lab_na"/>
					</xsl:apply-templates>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="/report/report_body/spec/data_list/data[@name = 'run_content_lst']" mode="no_value">
						<xsl:with-param name="lab_na" select="$lab_na"/>
					</xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="/report/report_body/spec/data_list/data[@name = 'content_lst']" mode="value">
				<xsl:with-param name="this" select="."/>
				<xsl:with-param name="lab_na" select="$lab_na"/>
			</xsl:apply-templates>
		</tr>
		<!--  randy do it   
		<xsl:if test="count(/report/report_body/spec/data_list/data[@name='run_content_lst'])!=0">
			<xsl:apply-templates select="item">
				<xsl:with-param name="my_class" select="$row_class"/>
				<xsl:with-param name="is_run">true</xsl:with-param>
				<xsl:with-param name="lab_na" select="$lab_na"/>
				<xsl:with-param name="lab_g_form_btn_detail" select="$lab_g_form_btn_detail"/>
				<xsl:with-param name="lab_g_form_btn_export_subn" select="$lab_g_form_btn_export_subn"/>
			</xsl:apply-templates>
		</xsl:if>
		-->
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data[@name = 'itm_content_lst']" mode="title">
		<xsl:variable name="my_value" select="@value"/>
		<xsl:choose>
			<xsl:when test="contains($my_value,'item_access')">
				<td nowrap="nowrap" align="left">
					<xsl:variable name="_role" select="substring-after($my_value,'item_access_')"/>
					<span class="TitleText">
						<xsl:call-template name="get_rol_title">
							<xsl:with-param name="rol_ext_id" select="$_role"/>
						</xsl:call-template>
					</span>
				</td>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="header_name" select="/report/report_body/display_option/item/template_view/section/*[name() = $my_value]/title/desc[@lan = $wb_lang_encoding]/@name"/>
				<xsl:variable name="field_type" select="/report/report_body/report_list/item/valued_template/section/*[name() = $my_value]/@type"/>
				<td nowrap="nowrap">
					<xsl:choose>
						<xsl:when test="$field_type = 'constant_label' or $field_type = 'textarea' or $field_type = 'text' or $field_type = 'item_access_pickup' or $field_type = 'catalog_attachment'">
							<xsl:attribute name="align">LEFT</xsl:attribute>
						</xsl:when>
						<xsl:when test="$field_type = 'pos_amount'">
							<xsl:attribute name="align">CENTER</xsl:attribute>
						</xsl:when>
					</xsl:choose>
					<span class="TitleText">
						<xsl:value-of select="$header_name"/>
					</span>
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data[@name = 'run_content_lst']" mode="title">
		<xsl:variable name="my_value" select="@value"/>
		<xsl:choose>
			<xsl:when test="contains($my_value,'item_access')">
				<td nowrap="nowrap" align="left">
					<xsl:variable name="_role" select="substring-after($my_value,'item_access_')"/>
					<span class="TitleText">
						<xsl:text>&#160;</xsl:text>
						<xsl:call-template name="get_rol_title">
							<xsl:with-param name="rol_ext_id" select="$_role"/>
						</xsl:call-template>
						<xsl:text>&#160;</xsl:text>
					</span>
				</td>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="header_name" select="/report/report_body/display_option/run/template_view/section/*[name() = $my_value]/title/desc[@lan = $wb_lang_encoding]/@name"/>
				<xsl:if test="$header_name != 'Course Title'">
					<td nowrap="nowrap">
						<xsl:variable name="field_type" select="/report/report_body/report_list/data/run/valued_template/section/*[name() = $my_value]/@type"/>
						<xsl:choose>
							<xsl:when test="$field_type = 'textarea' ">
								<xsl:attribute name="align">LEFT</xsl:attribute>
							</xsl:when>
							<xsl:when test="$field_type = 'constant_datetime' or $field_type = 'date' or $field_type = 'pos_int'">
								<xsl:attribute name="align">CENTER</xsl:attribute>
							</xsl:when>
						</xsl:choose>
						<span class="TitleText">&#160;<xsl:value-of select="$header_name"/>&#160;</span>
					</td>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data[@name = 'content_lst']" mode="title">
		<xsl:param name="lab_attendance"/>
		<xsl:param name="lab_run_num"/>
		<xsl:param name="lab_overall_rating"/>
		<xsl:choose>
			<xsl:when test="@value ='attendance'">
				<xsl:for-each select="$attendance_status_list/status">
					<td nowrap="nowrap" align="center">
						<span class="TitleText">&#160;
							<xsl:call-template name="get_ats_title">
								<xsl:with-param name="ats_id" select="@id"/>
							</xsl:call-template>&#160;
						</span>
					</td>
				</xsl:for-each>
			</xsl:when>
			<xsl:when test="@value = 'question'">
				<xsl:apply-templates select="/report/report_body/presentation/data[@name = 'mod_id']/survey"/>
			</xsl:when>
			<xsl:otherwise>
				<td nowrap="nowrap" align="center">
					<span class="TitleText">&#160;
					<xsl:choose>
							<xsl:when test="@value ='run_num'">
								<xsl:value-of select="$lab_run_num"/>
							</xsl:when>
							<xsl:when test="@value ='overall'">
								<xsl:value-of select="$lab_overall_rating"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="@value"/>
							</xsl:otherwise>
						</xsl:choose>&#160;
				</span>
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="survey">
		<xsl:for-each select="question">
			<xsl:if test="body/interaction/@type='MC'">
				<xsl:variable name="que_desc">
					<xsl:choose>
						<xsl:when test="string-length(header/desc) &gt; 0">
							<xsl:call-template name="escape_js">
								<xsl:with-param name="input_str">
									<xsl:call-template name="unescape_html_linefeed">
										<xsl:with-param name="my_right_value">
											<xsl:value-of select="header/desc"/>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:variable name="que_desc"></xsl:variable>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<td nowrap="nowrap" align="center">
					<span class="TitleText" title="{$que_desc}">
						<xsl:variable name="que_title_length">
							<xsl:value-of select="string-length(header/title)"/>
						</xsl:variable>
						<xsl:variable name="que_title">
							<xsl:choose>
								<xsl:when test="$que_title_length &gt; 20">
									<xsl:value-of select="substring(header/title,1,20)"/>...
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="header/title"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:value-of select="$que_title"/>
					</span>
				</td>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="value">
		<xsl:param name="is_run"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="this"/>
		<xsl:variable name="text_class">
			<xsl:choose>
				<xsl:when test="$is_run = 'true'">TextIt</xsl:when>
				<xsl:otherwise>Text</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<td nowrap="nowrap" valign="top">
			<xsl:choose>
				<xsl:when test="@type = 'constant_label' or @type = 'textarea' or @type = 'text' or @type = 'item_access_pickup' or @type = 'catalog_attachment' or @type = 'iac_select' ">
					<xsl:attribute name="align">LEFT</xsl:attribute>
				</xsl:when>
				<xsl:when test="@type = 'pos_amount' or @type = 'constant_datetime' or @type = 'date' or @type = 'pos_int' ">
					<xsl:attribute name="align">CENTER</xsl:attribute>
				</xsl:when>
			</xsl:choose>
			<span class="{$text_class}">
				<!--get value from itm_gen_frm_utils.xsl -->
				<xsl:variable name="content">
					<xsl:choose>
						<xsl:when test="@type">
							<xsl:apply-templates select="." mode="gen_field">
								<xsl:with-param name="this" select="$this"/>
							</xsl:apply-templates>
						</xsl:when>
						<xsl:when test="not(@type)">
							<xsl:choose>
								<xsl:when test="@value">
									<xsl:value-of select="@value"/>
								</xsl:when>
								<xsl:when test="not(@value) and @name='Code' ">
									<xsl:value-of select="../../../../item/@code"/>
								</xsl:when>
								<xsl:when test="not(@value) and @name='Title'">
									<xsl:value-of select="../../../../item/@title"/>
								</xsl:when>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="$content != ''">
						<xsl:copy-of select="$content"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_na"/>
					</xsl:otherwise>
				</xsl:choose>
			</span>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data[@name = 'content_lst']" mode="value">
		<xsl:param name="this"/>
		<xsl:param name="is_run"/>
		<xsl:param name="is_parent"/>
		<xsl:param name="lab_na"/>
		<xsl:variable name="text_class">
			<xsl:choose>
				<xsl:when test="$is_run = 'true'">TextIt</xsl:when>
				<xsl:otherwise>SmallText</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="@value = 'attendance'">
				<xsl:for-each select="$attendance_status_list/status">
					<xsl:variable name="my_id" select="@id"/>
					<td align="center" valign="top">
						<span class="{$text_class}">
							<xsl:variable name="value">
								<xsl:value-of select="$this/attendance_list/attendance[@id = $my_id]/@count"/>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$value = ''">0</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$value"/>
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
				</xsl:for-each>
			</xsl:when>
			<xsl:when test="@value = 'question'">
				<xsl:for-each select="/report/report_body/presentation/data[@name='mod_id']/survey/question[body/interaction[@type = 'MC']]">
					<xsl:variable name="order">
						<xsl:value-of select="@order"/>
					</xsl:variable>
					<td valign="top" align="center">
						<span class="{$text_class}">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							<xsl:choose>
								<xsl:when test="count($this/survey/question[@order = $order]) = 0">
									<xsl:value-of select="$lab_na"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="format-number($this/survey/question[@order = $order]/@response_avg_score, '0.0')"/>
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<td nowrap="nowrap" valign="top" align="center">
					<span class="{$text_class}">&#160;
						<xsl:choose>
							<xsl:when test="@value ='run_num'">
								<xsl:choose>
									<xsl:when test="$this/survey/num_of_run/@count">
										<xsl:value-of select="$this/survey/num_of_run/@count"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_na"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:when test="@value = 'overall'">
								<xsl:choose>
									<xsl:when test="$que_cnt &gt; 0 and $this/survey/@attempt_count &gt; 0">
										<xsl:value-of select="format-number(number($this/survey/@avg_svy_score) div $que_cnt, '0.0')"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_na"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="@*[name() = @value]">
										<xsl:value-of select="@*[name() = @value]"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_na"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
			&#160;</span>
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="no_value">
		<xsl:param name="is_run"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="value"/>
		<xsl:variable name="text_class">
			<xsl:choose>
				<xsl:when test="$is_run = 'true'">TextIt</xsl:when>
				<xsl:otherwise>Text</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="field_no" select="@value"/>
		<xsl:variable name="field_align">
			<xsl:choose>
				<xsl:when test="@name = 'itm_type'">LEFT</xsl:when>
				<xsl:when test="@name = 'itm_content_lst'">
					<xsl:variable name="field_type" select="/report/report_body/report_list/data/survey/valued_template/section/*[name() = $field_no]/@type"/>
					<xsl:choose>
						<xsl:when test="$field_type = 'constant_label' or $field_type = 'textarea' or $field_type = 'text' or $field_type = 'item_access_pickup' or $field_type = 'catalog_attachment'">LEFT</xsl:when>
						<xsl:when test="$field_type = 'pos_amount'">CENTER</xsl:when>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="@name = 'run_content_lst'">
					<xsl:variable name="field_type" select="/report/report_body/report_list/data/survey/valued_template/section/*[name() = $field_no]/@type"/>
					<xsl:choose>
						<xsl:when test="$field_type = 'textarea' ">LEFT</xsl:when>
						<xsl:when test="$field_type = 'constant_datetime' or $field_type = 'date' or $field_type = 'pos_int'">CENTER</xsl:when>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="@name = 'content_lst'">CENTER</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<td nowrap="nowrap" valign="top" align="{$field_align}">
			<span class="{$text_class}">
				<xsl:choose>
					<xsl:when test="$value!='' ">
						<xsl:value-of select="$value"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_na"/>
					</xsl:otherwise>
				</xsl:choose>
			</span>
		</td>
	</xsl:template>
	<!-- =================================================================================== -->
	<xsl:template name="report_criteria">
		<xsl:param name="this_width"/>
		<xsl:param name="lab_cos_title"/>
		<xsl:param name="lab_cos_catalog"/>
		<xsl:param name="lab_course_type"/>
		<xsl:param name="lab_survey"/>
		<xsl:param name="lab_cos"/>
		<xsl:param name="lab_all_cos"/>
		<xsl:param name="lab_answer_for_course"/>
		<xsl:param name="lab_answer_for_lrn_course"/>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width" select="$this_width"/>
		</xsl:call-template>
		<table cellpadding="0" cellspacing="0" border="0" width="{$this_width}" class="Bg">
			<xsl:choose>
				<!-- course id -->
				<xsl:when test="report_body/spec/data_list/data[@name='itm_id']">
					<tr>
						<td align="right" valign="top" class="wzb-form-label">
							<span class="TitleText">
								<xsl:value-of select="$lab_cos_title"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td valign="top" class="wzb-form-control">
							<span class="Text">
								<xsl:for-each select="report_body/spec/data_list/data[@name='itm_id']">
									<xsl:variable name="itm_id">
										<xsl:value-of select="@value"/>
									</xsl:variable>
									<xsl:value-of select="/report/report_body/presentation/data[@name='itm_id' and @value=$itm_id]/@display"/>
									<xsl:if test="position()!=last()">, </xsl:if>
								</xsl:for-each>
							</span>
						</td>
					</tr>
				</xsl:when>
				<!-- course catalog -->
				<xsl:when test="report_body/spec/data_list/data[@name='tnd_id']">
					<tr>
						<td align="right" valign="top" class="wzb-form-label">
							<span class="TitleText">
								<xsl:value-of select="$lab_cos_catalog"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td valign="top" class="wzb-form-control">
							<span class="Text">
								<xsl:for-each select="report_body/spec/data_list/data[@name='tnd_id']">
									<xsl:variable name="tnd_id">
										<xsl:value-of select="@value"/>
									</xsl:variable>
									<xsl:value-of select="/report/report_body/presentation/data[@name='tnd_id' and @value=$tnd_id]/@display"/>
									<xsl:if test="position()!=last()">, </xsl:if>
								</xsl:for-each>
							</span>
						</td>
					</tr>
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td align="right" valign="top" class="wzb-form-label">
							<span class="TitleText">
								<xsl:value-of select="$lab_cos"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td class="wzb-form-control">
							<span class="Text">
								<xsl:choose>
									<xsl:when test="$tc_enabled='true'">
										<xsl:choose>
											<xsl:when test="report_body/spec/data_list/data[@name='answer_for_course']/@value = '1' and report_body/spec/data_list/data[@name='answer_for_lrn_course']/@value = '0' ">
												<xsl:value-of select="$lab_answer_for_course"/>
											</xsl:when>
											<xsl:when test="report_body/spec/data_list/data[@name='answer_for_course']/@value = '0' and report_body/spec/data_list/data[@name='answer_for_lrn_course']/@value = '1' ">
												<xsl:value-of select="$lab_answer_for_lrn_course"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="$lab_all_cos"/>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_all_cos"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
			<!--
			<xsl:if test="report_body/spec/data_list/data[@name='itm_id']">
				<tr>
					<td align="right" valign="top">
						<span class="TitleText">
							<xsl:value-of select="$lab_cos_title"/>
							<xsl:text>：</xsl:text>
						</span>
					</td>
					<td valign="top">
						<span class="Text">
							<xsl:for-each select="report_body/spec/data_list/data[@name='itm_id']">
								<xsl:variable name="itm_id">
									<xsl:value-of select="@value"/>
								</xsl:variable>
								<xsl:value-of select="/report/report_body/presentation/data[@name='itm_id' and @value=$itm_id]/@display"/>
								<xsl:if test="position()!=last()">, </xsl:if>
							</xsl:for-each>
						</span>
					</td>
				</tr>
			</xsl:if>
			 course catalog 
			<xsl:if test="report_body/spec/data_list/data[@name='tnd_id']">
				<tr>
					<td align="right" valign="top">
						<span class="TitleText">
							<xsl:value-of select="$lab_cos_catalog"/>
							<xsl:text>：</xsl:text>
						</span>
					</td>
					<td valign="top">
						<span class="Text">
							<xsl:for-each select="report_body/spec/data_list/data[@name='tnd_id']">
								<xsl:variable name="tnd_id">
									<xsl:value-of select="@value"/>
								</xsl:variable>
								<xsl:value-of select="/report/report_body/presentation/data[@name='tnd_id' and @value=$tnd_id]/@display"/>
								<xsl:if test="position()!=last()">, </xsl:if>
							</xsl:for-each>
						</span>
					</td>
				</tr>
			</xsl:if>
			-->
			<!-- course type -->
			<xsl:if test="report_body/spec/data_list/data[@name='itm_type']">
				<tr>
					<td align="right" valign="top" class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_course_type"/>
							<xsl:text>：</xsl:text>
						</span>
					</td>
					<td valign="top" class="wzb-form-control">
						<span class="Text">
							<xsl:if test="/report/report_body/spec/data_list/data[@name='itm_type']">
								<xsl:for-each select="/report/report_body/spec/data_list/data[@name='itm_type']">
									<xsl:call-template name="get_ity_title">
										<xsl:with-param name="itm_type" select="@value"/>
									</xsl:call-template>
									<xsl:if test="position() != last()">, </xsl:if>
								</xsl:for-each>
							</xsl:if>
						</span>
					</td>
				</tr>
			</xsl:if>
			<!-- survey -->
			<xsl:if test="report_body/spec/data_list/data[@name='mod_id']">
				<tr>
					<td align="right" valign="top" class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_survey"/>
							<xsl:text>：</xsl:text>
						</span>
					</td>
					<td valign="top" class="wzb-form-control">
						<span class="Text">
							<xsl:value-of select="/report/report_body/presentation/data[@name='mod_id']/survey/title"/>
						</span>
					</td>
				</tr>
			</xsl:if>
		</table>
		<xsl:call-template name="wb_ui_space"/>
	</xsl:template>
</xsl:stylesheet>
