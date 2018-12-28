<?xml version="1.0" encoding="UTF-8"?>
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
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="share/itm_gen_details_share.xsl"/>
	<xsl:import href="utils/display_score.xsl"/>
	<!-- others -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/rpt_share.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="share/label_rpt.xsl"/>
	<xsl:import href="share/label_role.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="rpt_type" select="/report/report_body/template/@type"/>
	<xsl:variable name="ent_id" select="/report/meta/cur_usr/@ent_id"/>
	<xsl:variable name="template_id" select="/report/report_body/template/@id"/>
	<xsl:variable name="rpt_name" select="/report/report_body/spec/title"/>
	<xsl:variable name="rsp_id" select="/report/report_body/spec/@spec_id"/>
	<xsl:variable name="role_list" select="/report/report_body/meta/role_list"/>
	<xsl:variable name="attendance_status_list" select="/report/report_body/meta/attendance_status_list"/>
	<xsl:variable name="student_id" select="report/report_body/student/@ent_id"/>
	<xsl:variable name="num" select="count(report/report_body/report_list/report_group/record/student[@ent_id=$student_id])"/>
	<xsl:variable name="total" select="count(report/report_body/report_list/report_group)"/>
	<xsl:variable name="cur_page" select="/report/report_body/pagination/@cur_page"/>
	<xsl:variable name="page_size" select="/report/report_body/pagination/@page_size"/>
	<xsl:variable name="total_rec" select="/report/report_body/report_list/report_summary/total_courses/@value"/>
	<xsl:variable name="cur_sort_col" select="/report/report_body/pagination/@sort_col"/>
	<xsl:variable name="cur_sort_order" select="/report/report_body/pagination/@sort_order"/>
	<xsl:variable name="page_timestamp" select="/report/report_body/pagination/@timestamp"/>
	<xsl:variable name="itm_type_lst" select="/report/report_body/meta/item_type_list"/>
	<xsl:variable name="tc_enabled" select="/report/meta/tc_enabled"/>
	<xsl:variable name="col_size">
		<xsl:choose>
			<xsl:when test="count(/report/report_body/report_list/report_group) > 0">
				<xsl:value-of select="count(/report/report_body/spec/data_list/data[@name = 'usr_content_lst' or @name = 'itm_content_lst' or @name='run_content_lst' or @name = 'content_lst'])"/>
			</xsl:when>
			<xsl:otherwise>2</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="sort_order_by">
		<xsl:choose>
			<xsl:when test="$cur_sort_order = 'ASC' or $cur_sort_order = 'asc'">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="rpt_xls" select="/report/report_body/template/xsl_list/xsl[@type = 'download']"/>
	<!-- =============================================================== -->
	<xsl:variable name="isStudent">
		<xsl:choose>
			<xsl:when test="report/report_body/student">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
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
			<meta http-equiv="Content-Type" content="text/html"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_lrn_soln.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_mgt_rpt.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			var mgt_rpt = new wbManagementReport;
		
		]]></script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="new_css" />
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
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
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_total_enroll">學習記錄總數</xsl:with-param>
			<xsl:with-param name="lab_total_attempts">嘗試總數</xsl:with-param>
			<xsl:with-param name="lab_total_timespent">學習總時長</xsl:with-param>
			<xsl:with-param name="lab_average_score">平均成績</xsl:with-param>
			<xsl:with-param name="lab_day">天</xsl:with-param>
			
			<xsl:with-param name="lab_usr_id">用戶名</xsl:with-param>
			<xsl:with-param name="lab_full_name">全名</xsl:with-param>
			<xsl:with-param name="lab_usr_group">用戶組</xsl:with-param>
			<xsl:with-param name="lab_usr_grade">職級</xsl:with-param>
			<xsl:with-param name="lab_usr_email">電子郵件</xsl:with-param>
			<xsl:with-param name="lab_usr_phone">電話</xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">錄取日期</xsl:with-param>
			<xsl:with-param name="lab_att_status">完成狀態</xsl:with-param>
			<xsl:with-param name="lab_att_date">完成日期</xsl:with-param>
			<xsl:with-param name="lab_cov_commence_datetime">首次訪問</xsl:with-param>
			<xsl:with-param name="lab_cov_last_acc_datetime">上次訪問</xsl:with-param>
			<xsl:with-param name="lab_total_attempt">嘗試</xsl:with-param>
			<xsl:with-param name="lab_cov_score">成績</xsl:with-param>
			<xsl:with-param name="lab_cov_total_time">學習時長</xsl:with-param>
			
			<xsl:with-param name="lab_no_item">沒有記錄</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_recycle_bin">回收站</xsl:with-param>
			<xsl:with-param name="lab_course">課程</xsl:with-param>
			<xsl:with-param name="lab_show">顯示</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
		<!--statistic information-->
			<xsl:with-param name="lab_total_enroll">学习记录总数</xsl:with-param>
			<xsl:with-param name="lab_total_attempts">尝试总数</xsl:with-param>
			<xsl:with-param name="lab_total_timespent">学习总时长</xsl:with-param>
			<xsl:with-param name="lab_average_score">平均成绩</xsl:with-param>
			<xsl:with-param name="lab_day">天</xsl:with-param>
			
			<xsl:with-param name="lab_usr_id">用户名</xsl:with-param>
			<xsl:with-param name="lab_full_name">全名</xsl:with-param>
			<xsl:with-param name="lab_usr_group">用户组</xsl:with-param>
			<xsl:with-param name="lab_usr_grade">职级</xsl:with-param>
			<xsl:with-param name="lab_usr_email">电子邮件</xsl:with-param>
			<xsl:with-param name="lab_usr_phone">电话</xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">录取日期</xsl:with-param>
			<xsl:with-param name="lab_att_status">完成状态</xsl:with-param>
			<xsl:with-param name="lab_att_date">完成日期</xsl:with-param>
			<xsl:with-param name="lab_cov_commence_datetime">首次访问</xsl:with-param>
			<xsl:with-param name="lab_cov_last_acc_datetime">上次访问</xsl:with-param>
			<xsl:with-param name="lab_total_attempt">尝试</xsl:with-param>
			<xsl:with-param name="lab_cov_score">成绩</xsl:with-param>
			<xsl:with-param name="lab_cov_total_time">学习时长</xsl:with-param>
			
			<xsl:with-param name="lab_no_item">没有记录</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_recycle_bin">回收站</xsl:with-param>
			<xsl:with-param name="lab_course">课程</xsl:with-param>
			<xsl:with-param name="lab_show">显示</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<!--statistic information-->
			<xsl:with-param name="lab_total_enroll">Total enrollments</xsl:with-param>
			<xsl:with-param name="lab_total_attempts">Total attempts</xsl:with-param>
			<xsl:with-param name="lab_total_timespent">Total time spent</xsl:with-param>
			<xsl:with-param name="lab_average_score">Average score</xsl:with-param>
			<xsl:with-param name="lab_day">days</xsl:with-param>
			
			<xsl:with-param name="lab_usr_id">User ID</xsl:with-param>
			<xsl:with-param name="lab_full_name">Full name</xsl:with-param>
			<xsl:with-param name="lab_usr_group">Group</xsl:with-param>
			<xsl:with-param name="lab_usr_grade">Grade</xsl:with-param>
			<xsl:with-param name="lab_usr_email">Email</xsl:with-param>
			<xsl:with-param name="lab_usr_phone">Phone</xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">Enrollment date</xsl:with-param>
			<xsl:with-param name="lab_att_status">Learning status</xsl:with-param>
			<xsl:with-param name="lab_att_date">Completion date</xsl:with-param>
			<xsl:with-param name="lab_cov_commence_datetime">First access</xsl:with-param>
			<xsl:with-param name="lab_cov_last_acc_datetime">Last access</xsl:with-param>
			<xsl:with-param name="lab_total_attempt">Attempts</xsl:with-param>
			<xsl:with-param name="lab_cov_total_time">Time spent</xsl:with-param>
			<xsl:with-param name="lab_cov_score">Score</xsl:with-param>
			
			<xsl:with-param name="lab_no_item">No record found</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_recycle_bin">Recycle bin</xsl:with-param>
			<xsl:with-param name="lab_course">courses</xsl:with-param>
			<xsl:with-param name="lab_show">Show </xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			
		</xsl:call-template>
	</xsl:template>
	<!-- ==================================content===================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_total_enroll"/>
		<xsl:param name="lab_total_attempts"/>
		<xsl:param name="lab_total_timespent"/>
		<xsl:param name="lab_average_score"/>
		<xsl:param name="lab_day"/>
		
		<xsl:param name="lab_usr_id"/>
		<xsl:param name="lab_full_name"/>
		<xsl:param name="lab_usr_group"/>
		<xsl:param name="lab_usr_grade"/>
		<xsl:param name="lab_usr_email"/>
		<xsl:param name="lab_usr_phone"/>
		<xsl:param name="lab_att_create_timestamp"/>
		<xsl:param name="lab_att_status"/>
		<xsl:param name="lab_att_date"/>
		<xsl:param name="lab_cov_commence_datetime"/>
		<xsl:param name="lab_cov_last_acc_datetime"/>
		<xsl:param name="lab_total_attempt"/>
		<xsl:param name="lab_cov_total_time"/>
		<xsl:param name="lab_cov_score"/>
		
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_recycle_bin"/>
		<xsl:param name="lab_course"/>
		<xsl:param name="lab_show"/>
		<xsl:param name="lab_g_form_btn_close"/>
		
		<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td colspan="16">
					<xsl:call-template name="wb_ui_title">
						<xsl:with-param name="text">
							<xsl:choose>
								<xsl:when test="$rpt_name != ''">
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
				</td>
			</tr>
			<xsl:apply-templates select="report_body/report_list/report_group">
				<xsl:with-param name="lab_total_enroll" select="$lab_total_enroll"/>
				<xsl:with-param name="lab_total_attempts" select="$lab_total_attempts"/>
				<xsl:with-param name="lab_total_timespent" select="$lab_total_timespent"/>
				<xsl:with-param name="lab_average_score" select="$lab_average_score"/>
				<xsl:with-param name="lab_day" select="$lab_day"/>
				<xsl:with-param name="lab_usr_id" select="$lab_usr_id"/>
				<xsl:with-param name="lab_full_name" select="$lab_full_name"/>
				<xsl:with-param name="lab_usr_group" select="$lab_usr_group"/>
				<xsl:with-param name="lab_usr_grade" select="$lab_usr_grade"/>
				<xsl:with-param name="lab_usr_email" select="$lab_usr_email"/>
				<xsl:with-param name="lab_usr_phone" select="$lab_usr_phone"/>
				<xsl:with-param name="lab_att_create_timestamp" select="$lab_att_create_timestamp"/>
				<xsl:with-param name="lab_att_status" select="$lab_att_status"/>
				<xsl:with-param name="lab_att_date" select="$lab_att_date"/>
				<xsl:with-param name="lab_cov_last_acc_datetime" select="$lab_cov_last_acc_datetime"/>
				<xsl:with-param name="lab_cov_commence_datetime" select="$lab_cov_commence_datetime"/>
				<xsl:with-param name="lab_total_attempt" select="$lab_total_attempt"/>
				<xsl:with-param name="lab_cov_total_time" select="$lab_cov_total_time"/>
				<xsl:with-param name="lab_cov_score" select="$lab_cov_score"/>
				<xsl:with-param name="lab_no_item" select="$lab_no_item"/>
				<xsl:with-param name="lab_na" select="$lab_na"/>
				<xsl:with-param name="lab_recycle_bin" select="$lab_recycle_bin"/>
				<xsl:with-param name="lab_show" select="$lab_show"/>
				<xsl:with-param name="this" select="."/>
			</xsl:apply-templates>	
		</table>
		<div class="wzb-bar">
			<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
				<tr>
					<td align="center">
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
	<!-- ===========================report group==================================== -->
	<xsl:template match="report_group">
		<xsl:param name="lab_total_enroll"/>
		<xsl:param name="lab_total_attempts"/>
		<xsl:param name="lab_total_timespent"/>
		<xsl:param name="lab_average_score"/>
		<xsl:param name="lab_day"/>
		
		<xsl:param name="lab_usr_id"/>
		<xsl:param name="lab_full_name"/>
		<xsl:param name="lab_usr_group"/>
		<xsl:param name="lab_usr_grade"/>
		<xsl:param name="lab_usr_email"/>
		<xsl:param name="lab_usr_phone"/>
		<xsl:param name="lab_att_create_timestamp"/>
		<xsl:param name="lab_att_status"/>
		<xsl:param name="lab_att_date"/>
		<xsl:param name="lab_cov_commence_datetime"/>
		<xsl:param name="lab_cov_last_acc_datetime"/>
		<xsl:param name="lab_total_attempt"/>
		<xsl:param name="lab_cov_total_time"/>
		<xsl:param name="lab_cov_score"/>
		
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_recycle_bin"/>
		<xsl:param name="lab_course"/>
		<xsl:param name="lab_show"/>
		<xsl:param name="this"/>
		<xsl:if test="position()!=1">
			<tr>
				<td height="15" colspan="{$col_size}"/>
			</tr>
		</xsl:if>
		<!-- start draw table header -->
		<tr>
			<td colspan="16">
				<span class="TitleText">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text">
							<xsl:value-of select="item/@code"/> - <xsl:value-of select="item/title"/>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line">
						<xsl:with-param name="width">100%</xsl:with-param>
					</xsl:call-template>
				</span>
				<xsl:call-template name="wb_ui_space">
					<xsl:with-param name="height">5</xsl:with-param>
				</xsl:call-template>
				<xsl:if test="count(record) > 0">
					<img src="{group_summary/total_enrollments/enrollment_img_name}"/>
					<br/>
					<span class="TitleText">
						<table>
							<tr>
								<td class="wzb-form-label"><xsl:value-of select="$lab_total_attempts"/>：</td>
								<td class="wzb-form-control">
									<span class="StatDataText">
										<xsl:value-of select="group_summary/total_attempts/@value"/>
									</span>
								</td>
							</tr>
							<tr>
								<td class="wzb-form-label"><xsl:value-of select="$lab_total_timespent"/>：</td>
								<td class="wzb-form-control">
									<span class="StatDataText">
										<xsl:value-of select="group_summary/total_time_spent/@day"/>
										<xsl:text> </xsl:text>
										<xsl:value-of select="$lab_day"/>
										<xsl:text> </xsl:text>
										<xsl:value-of select="group_summary/total_time_spent/@time"/>
									</span>
								</td>
							</tr>
							<tr>
								<td class="wzb-form-label"><xsl:value-of select="$lab_average_score"/> ： </td>
								<td class="wzb-form-control">
									<span class="StatDataText">
										<xsl:value-of select="group_summary/average_score/@value"/>
									</span>
								</td>
							</tr>
						</table>
					</span>
				</xsl:if>
			</td>
		</tr>
		<xsl:choose>
			<xsl:when test="count(record) > 0">
				<tr>
					<td>
						<table class="table wzb-ui-table">
							<tr class="SecBg wzb-ui-table-head">
								<td align="left" nowrap="nowrap">
									<span class="SmallText">
										<xsl:value-of select="$lab_usr_id"/>
									</span>
								</td>
								<td align="left" nowrap="nowrap">
									<span class="SmallText">
										<xsl:value-of select="$lab_full_name"/>
									</span>
								</td>
								<td align="left" nowrap="nowrap">
									<span class="SmallText">
										<xsl:value-of select="$lab_usr_group"/>
									</span>
								</td>
								<td align="left" nowrap="nowrap">
									<span class="SmallText">
										<xsl:value-of select="$lab_usr_grade"/>
									</span>
								</td>
								<td align="left" nowrap="nowrap">
									<span class="SmallText">
										<xsl:value-of select="$lab_usr_email"/>
									</span>
								</td>
								<td align="left" nowrap="nowrap">
									<span class="SmallText">
										<xsl:value-of select="$lab_usr_phone"/>
									</span>
								</td>
								<td align="left" nowrap="nowrap">
									<span class="SmallText">
										<xsl:value-of select="$lab_att_create_timestamp"/>
									</span>
								</td>
								<td align="left" nowrap="nowrap">
									<span class="SmallText">
										<xsl:value-of select="$lab_att_status"/>
									</span>
								</td>
								<td align="left" nowrap="nowrap">
									<span class="SmallText">
										<xsl:value-of select="$lab_att_date"/>
									</span>
								</td>
								<td align="left" nowrap="nowrap">
									<span class="SmallText">
										<xsl:value-of select="$lab_cov_commence_datetime"/>
									</span>
								</td>
								<td align="left" nowrap="nowrap">
									<span class="SmallText">
										<xsl:value-of select="$lab_cov_last_acc_datetime"/>
									</span>
								</td>
								<td align="left" nowrap="nowrap">
									<span class="SmallText">
										<xsl:value-of select="$lab_total_attempt"/>
									</span>
								</td>
								<td align="left" nowrap="nowrap">
									<span class="SmallText">
										<xsl:value-of select="$lab_cov_total_time"/>
									</span>
								</td>
								<td align="left" nowrap="nowrap">
									<span class="SmallText">
										<xsl:value-of select="$lab_cov_score"/>
									</span>
								</td>
							</tr>
							<!-- -->
							<xsl:apply-templates select="record">
								<xsl:with-param name="lab_na" select="$lab_na"/>
								<xsl:with-param name="lab_recycle_bin" select="$lab_recycle_bin"/>
							</xsl:apply-templates>
							 
							<tr class="Line">
								<td colspan="16" height="1">
									<img width="1" src="{$wb_img_path}tp.gif" height="1" border="0"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<tr class="RowsEven">
					<td height="15" colspan="{$col_size + 2}" align="left">
						<!--<xsl:value-of select="$lab_no_item"/>-->
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_no_item"/>
							<xsl:with-param name="top_line">false</xsl:with-param>
							<xsl:with-param name="bottom_line">false</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ==============================record================================= -->
	<xsl:template match="record">
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_recycle_bin"/>
		<xsl:variable name="usr_ent_id" select="student/@ent_id"/>
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2">StatRowsEven</xsl:when>
				<xsl:otherwise>StatRowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr class="{$row_class}">
			<td valign="top" nowrap="nowrap">
				<span class="SmallText">
					<xsl:choose>
							<xsl:when test="user/@id != ''">
								<xsl:call-template name="omit_title">
									<xsl:with-param name="title">
										<xsl:value-of select="user/@id"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
				</span>
			</td>
			<td valign="top" nowrap="nowrap">
				<span class="SmallText">
					<xsl:choose>
							<xsl:when test="user/name/@display_name != ''">
								<xsl:call-template name="omit_title">
									<xsl:with-param name="title">
										<xsl:value-of select="user/name/@display_name"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
				</span>
			</td>
			<td valign="top" nowrap="nowrap">
			<span class="SmallText">
				<xsl:choose>
					<xsl:when test="group_list">
						<xsl:call-template name="omit_title">
							<xsl:with-param name="title">
								<xsl:value-of select="user/full_path"/>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<span class="SmallText">
							<xsl:value-of select="$lab_na"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</span>
			</td>
			<td valign="top" nowrap="nowrap">
			<span class="SmallText">
				<xsl:choose>
					<xsl:when test="grade_list">
						<xsl:for-each select="grade_list/group">
							<xsl:if test="position()!=1">
								<xsl:text>&#160;/&#160;</xsl:text>
							</xsl:if>
							<xsl:choose>
								<xsl:when test="not(text()) or text() = 'Unspecified'">
									<xsl:text>&#160;--&#160;</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="."/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<span class="SmallText">
							<xsl:value-of select="$lab_na"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</span>
			</td>
			<td valign="top" nowrap="nowrap">
			<span class="SmallText">
				<xsl:choose>
					<xsl:when test="user/email/@email_1 != ''">
						<xsl:call-template name="omit_title">
							<xsl:with-param name="title">
								<xsl:value-of select="user/email/@email_1"/>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<span class="SmallText">
							<xsl:value-of select="$lab_na"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</span>
			</td>
			<td valign="top" nowrap="nowrap">
			<span class="SmallText">
				<xsl:choose>
					<xsl:when test="user/tel/@tel_1 != ''">
						<xsl:call-template name="omit_title">
							<xsl:with-param name="title">
								<xsl:value-of select="user/tel/@tel_1"/>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<span class="SmallText">
							<xsl:value-of select="$lab_na"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</span>
			</td>
			<td valign="top" nowrap="nowrap">
			<span class="SmallText">
				<xsl:choose>
					<xsl:when test="attendance/@create_date!='' and attendance/@create_date">
						<span class="SmallText">
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="attendance/@create_date"/>
								</xsl:with-param>
							</xsl:call-template>
						</span>
					</xsl:when>
					<xsl:otherwise>
						<span class="SmallText">
							<xsl:value-of select="$lab_na"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</span>
			</td>
			
			<td valign="top" nowrap="nowrap">
				<span class="SmallText">
					<xsl:variable name="status_id">
							<xsl:choose>
								<xsl:when test="attendance/@status=''">0</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="attendance/@status"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="$status_id='0'">
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="$attendance_status_list/status[@id=$status_id]"/>
							</xsl:otherwise>
						</xsl:choose>
				</span>
			</td>
			<td valign="top" nowrap="nowrap">
				<span class="SmallText">
				<xsl:choose>
					<xsl:when test="attendance/@datetime!='' and attendance/@datetime">
						<span class="SmallText">
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="attendance/@datetime"/>
								</xsl:with-param>
							</xsl:call-template>
						</span>
					</xsl:when>
					<xsl:otherwise>
						<span class="SmallText">
							<xsl:value-of select="$lab_na"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
				</span>
			</td>
			<td valign="top" nowrap="nowrap">
				<span class="SmallText">
					<xsl:choose>
							<xsl:when test="aicc_data/@commence_datetime != ''">
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="aicc_data/@commence_datetime"/>
									</xsl:with-param>
									<xsl:with-param name="dis_time">T</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
				</span>
			</td>
			<td valign="top" nowrap="nowrap">
				<span class="SmallText">
						<xsl:choose>
							<xsl:when test="aicc_data/@last_acc_datetime != ''">
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="aicc_data/@last_acc_datetime"/>
									</xsl:with-param>
									<xsl:with-param name="dis_time">T</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
				</span>
			</td>
			<td valign="top" nowrap="nowrap">
				<span class="SmallText">
					<xsl:choose>
						<xsl:when test="aicc_data/@attempt != '' and aicc_data/@attempt != '0' ">
							<xsl:value-of select="aicc_data/@attempt"/>
						</xsl:when>
						<xsl:otherwise>
							<span class="SmallText">
								<xsl:value-of select="$lab_na"/>
							</span>
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</td>
			<td valign="top" nowrap="nowrap">
				<span class="SmallText">
					<xsl:choose>
							<xsl:when test="aicc_data/@used_time != ''">
								<xsl:value-of select="substring-before(aicc_data/@used_time, '.')"/>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
				</span>
			</td>
			<td valign="top" nowrap="nowrap">
				<span class="SmallText">
					<xsl:choose>
							<xsl:when test="aicc_data/@score != ''">
								<xsl:call-template name="display_score">
									<xsl:with-param name="score">
										<xsl:value-of select="aicc_data/@score"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
				</span>
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template match="status">
		<xsl:call-template name="get_ats_title">
			<xsl:with-param name="ats_id" select="@id"/>
		</xsl:call-template>
	</xsl:template>
	
</xsl:stylesheet>
