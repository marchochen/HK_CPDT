<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/itm_gen_details_share.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="share/label_role.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="att_status_num" select="count(report/report_body/meta/attendance_status_list/status)"/>
	<xsl:variable name="rpt_name" select="report/report_body/spec/title"/>
	<xsl:variable name="role_list" select="/report/report_body/meta/role_list"/>
	<xsl:variable name="attendance_status_list" select="/report/report_body/meta/attendance_status_list"/>
	<xsl:variable name="que_cnt" select="count(/report/report_body/presentation/data[@name='mod_id']/survey/question/body/interaction[@type = 'MC'])"/>
	<xsl:variable name="tc_enabled" select="/report/meta/tc_enabled"/>
	<xsl:variable name="col_size">
		<xsl:choose>
			<xsl:when test="count(/report/report_body/report_list/data) > 0">
				<xsl:value-of select="count(/report/report_body/spec/data_list/data[@name = 'content_lst' or @name = 'itm_content_lst' or @name='run_content_lst'])"/>
			</xsl:when>
			<xsl:otherwise>2</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="col_offset">
		<xsl:choose>
			<xsl:when test="count(/report/report_body/report_list/item) > 0">
				<xsl:choose>
					<xsl:when test="count(/report/report_body/spec/data_list/data[@name = 'content_lst' and @value = 'attendance']) > 0 and count(/report/report_body/spec/data_list/data[@name = 'content_lst' and @value = 'question']) > 0">
						<xsl:value-of select="$que_cnt + 2"/>
					</xsl:when>
					<xsl:when test="count(/report/report_body/spec/data_list/data[@name = 'content_lst' and @value = 'attendance']) = 0 and count(/report/report_body/spec/data_list/data[@name = 'content_lst' and @value = 'question']) > 0">
						<xsl:value-of select="$que_cnt - 1"/>
					</xsl:when>
					<xsl:when test="count(/report/report_body/spec/data_list/data[@name = 'content_lst' and @value = 'attendance']) > 0 and count(/report/report_body/spec/data_list/data[@name = 'content_lst' and @value = 'question']) = 0">3</xsl:when>
					<xsl:otherwise>0</xsl:otherwise>
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
			<style type="text/css">
				<xsl:comment>
				.Title{
					font-size: 20px; 
				}
				.bg{
					background-color: #CCCCCC;
				}
			</xsl:comment>
			</style>
		</head>
		<BODY leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="wb_init_lab"/>
		</BODY>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_svy_cos_rpt">課程調查問卷報告</xsl:with-param>
			<xsl:with-param name="lab_svy_cos_rst">調查問卷結果</xsl:with-param>
			<xsl:with-param name="lab_inst">報告標準</xsl:with-param>
			<xsl:with-param name="lab_cos_catalog">課程目錄</xsl:with-param>
			<xsl:with-param name="lab_cos_title">課程標題</xsl:with-param>
			<xsl:with-param name="lab_not_specified">未指定</xsl:with-param>
			<xsl:with-param name="lab_content">內容</xsl:with-param>
			<xsl:with-param name="lab_item_content">課程內容</xsl:with-param>
			<xsl:with-param name="lab_run_content">
				<xsl:value-of select="$lab_const_run"/>內容</xsl:with-param>
			<xsl:with-param name="lab_others_content">調查問卷結果</xsl:with-param>
			<xsl:with-param name="lab_course_type">課程類型</xsl:with-param>
			<xsl:with-param name="lab_user_content">學員資料</xsl:with-param>
			<xsl:with-param name="lab_all_version">所有版本</xsl:with-param>
			<xsl:with-param name="lab_latest_version">最新版本</xsl:with-param>
			<xsl:with-param name="lab_version">版本</xsl:with-param>
			<xsl:with-param name="lab_period">週期</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_no_item">找不到報告</xsl:with-param>
			<xsl:with-param name="lab_attendance">出勤</xsl:with-param>
			<xsl:with-param name="lab_run_num">
				<xsl:value-of select="$lab_const_run"/>數目</xsl:with-param>
			<xsl:with-param name="lab_plz_click">請按</xsl:with-param>
			<xsl:with-param name="lab_export">這裡</xsl:with-param>
			<xsl:with-param name="lab_csv_format">匯出為MS Excel兼容的格式</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_detail">詳情</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export_subn">匯出提交</xsl:with-param>
			<xsl:with-param name="lab_survey">課程調查問卷</xsl:with-param>
			<xsl:with-param name="lab_question_rating">平均評分</xsl:with-param>
			<xsl:with-param name="lab_overall_rating">總平均</xsl:with-param>
			<xsl:with-param name="lab_cos">課程</xsl:with-param>
			<xsl:with-param name="lab_all_cos">所有課程</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">我負責的課程</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">我負責學員報讀的課程</xsl:with-param>			
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<!-- header text -->
			<xsl:with-param name="lab_svy_cos_rpt">课程评估问卷报告</xsl:with-param>
			<xsl:with-param name="lab_svy_cos_rst">评估问卷结果</xsl:with-param>
			<xsl:with-param name="lab_inst">报告标准</xsl:with-param>
			<!-- search criteria text -->
			<xsl:with-param name="lab_cos_title">课程标题</xsl:with-param>
			<xsl:with-param name="lab_cos_catalog">课程目录</xsl:with-param>
			<xsl:with-param name="lab_course_type">课程类型</xsl:with-param>
			<xsl:with-param name="lab_version">版本</xsl:with-param>
			<xsl:with-param name="lab_latest_version">显示最新版本</xsl:with-param>
			<xsl:with-param name="lab_all_version">显示所有版本</xsl:with-param>
			<xsl:with-param name="lab_period">期限</xsl:with-param>
			<xsl:with-param name="lab_to"> 至 </xsl:with-param>
			<xsl:with-param name="lab_not_specified">未指定</xsl:with-param>
			<xsl:with-param name="lab_survey">课程评估问卷</xsl:with-param>
			<!-- content attribute text -->
			<xsl:with-param name="lab_content">内容</xsl:with-param>
			<!-- content attribute text of course -->
			<xsl:with-param name="lab_item_content">课程内容</xsl:with-param>
			<!-- content attribute text of run -->
			<xsl:with-param name="lab_run_content">
				<xsl:value-of select="$lab_const_run"/>内容</xsl:with-param>
			<!-- content attribute text of others -->
			<xsl:with-param name="lab_others_content">评估问卷结果</xsl:with-param>
			<xsl:with-param name="lab_attendance">考勤</xsl:with-param>
			<xsl:with-param name="lab_run_num">
				<xsl:value-of select="$lab_const_run"/>数目</xsl:with-param>
			<xsl:with-param name="lab_question_rating">平均评分</xsl:with-param>
			<xsl:with-param name="lab_overall_rating">总平均</xsl:with-param>
			<!-- content result text -->
			<xsl:with-param name="lab_no_item">没有记录</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<!-- export result text -->
			<xsl:with-param name="lab_plz_click">请点击</xsl:with-param>
			<xsl:with-param name="lab_export">这里</xsl:with-param>
			<xsl:with-param name="lab_csv_format">导出为MS Excel兼容的格式</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_detail">详情</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export_subn">导出提交</xsl:with-param>
			<!-- unused? -->
			<xsl:with-param name="lab_user_content">学员资料</xsl:with-param>
			<xsl:with-param name="lab_cos">课程</xsl:with-param>
			<xsl:with-param name="lab_all_cos">所有課程</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">我负责的课程</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">我负责学员报读的课程</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_svy_cos_rpt">Course evaluation report</xsl:with-param>
			<xsl:with-param name="lab_svy_cos_rst">Evaluation results</xsl:with-param>
			<xsl:with-param name="lab_inst">Report criteria:</xsl:with-param>
			<xsl:with-param name="lab_cos_catalog">Course catalog</xsl:with-param>
			<xsl:with-param name="lab_cos_title">Course title</xsl:with-param>
			<xsl:with-param name="lab_not_specified">-- All --</xsl:with-param>
			<xsl:with-param name="lab_content">Content</xsl:with-param>
			<xsl:with-param name="lab_item_content">Course content</xsl:with-param>
			<xsl:with-param name="lab_run_content">
				<xsl:value-of select="$lab_const_run"/> content</xsl:with-param>
			<xsl:with-param name="lab_others_content">Evaluation results</xsl:with-param>
			<xsl:with-param name="lab_course_type">Course type</xsl:with-param>
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
			<xsl:with-param name="lab_survey">Course evaluation form</xsl:with-param>
			<xsl:with-param name="lab_question_rating">Average ratings</xsl:with-param>
			<xsl:with-param name="lab_overall_rating">Overall average</xsl:with-param>
			<xsl:with-param name="lab_cos">Course</xsl:with-param>
			<xsl:with-param name="lab_all_cos">All courses</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">My responsible courses</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">Courses that have been enrolled by my responsible learners</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_svy_cos_rpt"/>
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
		<xsl:param name="lab_survey"/>
		<xsl:param name="lab_question_rating"/>
		<xsl:param name="lab_overall_rating"/>
		<xsl:param name="lab_cos"/>
		<xsl:param name="lab_all_cos"/>
		<xsl:param name="lab_answer_for_course"/>
		<xsl:param name="lab_answer_for_lrn_course"/>
		<table cellpadding="3" cellspacing="0" border="1">
			<tr>
				<td colspan="{$col_size + $col_offset}">
					<span class="Title">
						<xsl:choose>
							<xsl:when test="$rpt_name !=''">
								<xsl:value-of select="$rpt_name"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_svy_cos_rpt"/>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
			</tr>
			
						<xsl:choose>
				<!-- course id -->
				<xsl:when test="report_body/spec/data_list/data[@name='itm_id']">
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
				</xsl:when>
				<!-- course catalog -->
				<xsl:when test="report_body/spec/data_list/data[@name='tnd_id']">
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
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td align="right" valign="top">
							<span class="TitleText">
								<xsl:value-of select="$lab_cos"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td>
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
					<td>
						<xsl:value-of select="$lab_cos_title"/>:
					</td>
					<td colspan="{$col_size + $col_offset - 1}">
						<xsl:for-each select="report_body/spec/data_list/data[@name='itm_id']">
							<xsl:variable name="itm_id">
								<xsl:value-of select="@value"/>
							</xsl:variable>
							<xsl:value-of select="/report/report_body/presentation/data[@name='itm_id' and @value=$itm_id]/@display"/>
							<xsl:if test="position()!=last()">, </xsl:if>
						</xsl:for-each>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="report_body/spec/data_list/data[@name='tnd_id']">
				<tr>
					<td>
						<xsl:value-of select="$lab_cos_catalog"/>:
					</td>
					<td colspan="{$col_size + $col_offset - 1}">
						<xsl:for-each select="report_body/spec/data_list/data[@name='tnd_id']">
							<xsl:variable name="tnd_id">
								<xsl:value-of select="@value"/>
							</xsl:variable>
							<xsl:value-of select="/report/report_body/presentation/data[@name='tnd_id' and @value=$tnd_id]/@display"/>
							<xsl:if test="position()!=last()">, </xsl:if>
						</xsl:for-each>
					</td>
				</tr>
			</xsl:if>
			-->
			<!-- course type -->
			<xsl:if test="report_body/spec/data_list/data[@name='itm_type']">
				<tr>
					<td>
						<xsl:value-of select="$lab_course_type"/>:
					</td>
					<td colspan="{$col_size + $col_offset - 1}">
						<xsl:if test="/report/report_body/spec/data_list/data[@name='itm_type']">
							<xsl:for-each select="/report/report_body/spec/data_list/data[@name='itm_type']">
								<xsl:call-template name="get_ity_title">
									<xsl:with-param name="itm_type" select="@value"/> 
								</xsl:call-template>
								<xsl:if test="position() != last()">, </xsl:if>
							</xsl:for-each>
						</xsl:if>
					</td>
				</tr>
			</xsl:if>
			<!-- survey -->
			<tr>
				<td>
					<xsl:value-of select="$lab_survey"/>:
				</td>
				<td colspan="{$col_size + $col_offset - 1}">
					<xsl:choose>
						<xsl:when test="not(report_body/spec/data_list/data[@name='mod_id'])">
							<xsl:value-of select="$lab_not_specified"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="/report/report_body/presentation/data[@name='mod_id']/survey/title"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td colspan="{$col_size + $col_offset}">
					<xsl:choose>
						<xsl:when test="count(/report/report_body/report_list/data) = 0">
							<xsl:call-template name="show_no_item">
								<xsl:with-param name="lab_no_item" select="$lab_no_item"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<!-- start draw table header -->
							<table cellpadding="3" cellspacing="0" border="1" width="100%">
								<xsl:if test="count(/report/report_body/spec/data_list/data[@name = 'content_lst' and @value = 'attendance']) &gt; 0 or count(/report/report_body/spec/data_list/data[@name = 'content_lst' and @value = 'question']) &gt; 0">
									<tr class="bg">
										<xsl:for-each select="/report/report_body/spec/data_list/data[@name = 'content_lst' or @name = 'itm_content_lst' or @name='run_content_lst']">
											<xsl:choose>
												<xsl:when test="@name = 'content_lst' and @value = 'attendance'">
													<td colspan="4">
														<xsl:value-of select="$lab_attendance"/>
													</td>
												</xsl:when>
												<xsl:when test="@name = 'content_lst' and @value = 'question'">
													<td colspan="{$que_cnt}">
														<xsl:value-of select="$lab_question_rating"/>
													</td>
												</xsl:when>
												<xsl:otherwise>
													<td/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:for-each>
									</tr>
								</xsl:if>
								<tr class="bg">
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
								</xsl:apply-templates>
							</table>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data">
		<xsl:param name="my_class"/>
		<xsl:param name="is_run">false</xsl:param>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_g_form_btn_detail"/>
		<xsl:param name="lab_g_form_btn_export_subn"/>
		<tr>
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
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data[@name = 'itm_content_lst']" mode="title">
		<xsl:variable name="my_value" select="@value"/>
		<xsl:choose>
			<xsl:when test="contains($my_value,'item_access')">
				<td>
					<xsl:variable name="_role" select="substring-after($my_value,'item_access_')"/>
					<xsl:call-template name="get_rol_title">
						<xsl:with-param name="rol_ext_id" select="$role_list/role[@id = $_role]/@id"/>
					</xsl:call-template>
				</td>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="header_name" select="/report/report_body/display_option/item/template_view/section/*[name() = $my_value]/title/desc[@lan = $wb_lang_encoding]/@name"/>
				<xsl:variable name="field_type" select="/report/report_body/report_list/data/item/valued_template/section/*[name() = $my_value]/@type"/>
				<td>
					<xsl:value-of select="$header_name"/>
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data[@name = 'run_content_lst']" mode="title">
		<xsl:variable name="my_value" select="@value"/>
		<xsl:choose>
			<xsl:when test="contains($my_value,'item_access')">
				<td>
					<xsl:variable name="_role" select="substring-after($my_value,'item_access_')"/>
					<xsl:call-template name="get_rol_title">
						<xsl:with-param name="rol_ext_id" select="$role_list/role[@id = $_role]/@id"/>
					</xsl:call-template>
				</td>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="header_name" select="/report/report_body/display_option/run/template_view/section/*[name() = $my_value]/title/desc[@lan = $wb_lang_encoding]/@name"/>
				<xsl:variable name="field_type" select="/report/report_body/report_list/data/run/valued_template/section/*[name() = $my_value]/@type"/>
				<xsl:if test="$header_name != 'Course Title'">
					<td>
						<xsl:value-of select="$header_name"/>
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
					<td>
						<xsl:call-template name="get_ats_title"/>
					</td>
				</xsl:for-each>
			</xsl:when>
			<xsl:when test="@value = 'question'">
				<xsl:apply-templates select="/report/report_body/presentation/data[@name = 'mod_id']/survey"/>
			</xsl:when>
			<xsl:otherwise>
				<td>
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
					</xsl:choose>
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="survey">
		<xsl:for-each select="question">
			<xsl:if test="body/interaction/@type='MC'">
				<td>
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
					<xsl:choose>
						<xsl:when test="string-length(header/desc) &gt; 0">
							<xsl:value-of select="$que_title"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$que_title"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="value">
		<xsl:param name="is_run"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="this"/>
		<td>
			<!--get value from itm_gen_frm_utils.xsl -->
			<xsl:variable name="content">
			<!--
				<xsl:apply-templates select="." mode="gen_field">
					<xsl:with-param name="xls">true</xsl:with-param>
				</xsl:apply-templates>
				-->
				<xsl:choose>
						<xsl:when test="@type">
							<xsl:apply-templates select="." mode="gen_field">
								<xsl:with-param name="this" select="$this"/>
								<xsl:with-param name="xls">true</xsl:with-param>
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
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data[@name = 'content_lst']" mode="value">
		<xsl:param name="this"/>
		<xsl:param name="is_run"/>
		<xsl:param name="is_parent"/>
		<xsl:param name="lab_na"/>
		<xsl:choose>
			<xsl:when test="@value = 'attendance'">
				<xsl:for-each select="$attendance_status_list/status">
					<xsl:variable name="my_id" select="@id"/>
					<td>
						<xsl:variable name="value">
							<xsl:value-of select="$this/attendance_list/attendance[@id = $my_id]/@count"/>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="$value = ''">0</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$value"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</xsl:for-each>
			</xsl:when>
			<xsl:when test="@value = 'question'">
				<xsl:for-each select="/report/report_body/presentation/data[@name='mod_id']/survey/question[body/interaction[@type = 'MC']]">
					<xsl:variable name="order">
						<xsl:value-of select="@order"/>
					</xsl:variable>
					<td>
						<xsl:choose>
							<xsl:when test="count($this/survey/question[@order = $order]) = 0">
								<xsl:value-of select="$lab_na"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="format-number($this/survey/question[@order = $order]/@response_avg_score, '0.0')"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<td>
					<xsl:choose>
						<xsl:when test="@value ='run_num'">
							<xsl:choose>
								<xsl:when test="$this/num_of_run/@count">
									<xsl:value-of select="$this/num_of_run/@count"/>
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
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="no_value">
		<xsl:param name="is_run"/>
		<xsl:param name="lab_na"/>
		<xsl:variable name="field_no" select="@value"/>
		<td>
			<xsl:value-of select="$lab_na"/>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="show_no_item">
		<xsl:param name="lab_no_item"/>
		<table cellpadding="3" cellspacing="0" border="0" width="760">
			<tr>
				<td colspan="2"/>
			</tr>
			<tr>
				<td colspan="2">
					<xsl:value-of select="$lab_no_item"/>
				</td>
			</tr>
			<tr>
				<td colspan="2"/>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
