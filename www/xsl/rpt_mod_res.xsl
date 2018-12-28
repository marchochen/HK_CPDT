<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="share/wb_module_status_const.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="share/label_rpt.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="rpt_name" select="/report/report_body/spec/title"/>
	<xsl:variable name="attendance_status_list" select="/report/report_body/meta/attendance_status_list"/>
	<xsl:variable name="total" select="report/report_body/pagination/@total_rec"/>
	<xsl:variable name="cur_page" select="/report/report_body/pagination/@cur_page"/>
	<xsl:variable name="page_size" select="/report/report_body/pagination/@page_size"/>
	<xsl:variable name="tc_enabled" select="/report/meta/tc_enabled"/>
	<xsl:variable name="col_size">
		<xsl:choose>
			<xsl:when test="count(/report/report_body/report_list/record) > 0">
				<xsl:value-of select="count(/report/report_body/spec/data_list/data[@name = 'usr_content_lst' or @name = 'itm_content_lst' or @name='run_content_lst' or @name = 'content_lst'])+ count(/report/report_body/mod_list/mod[@type = 'ASS' or @type = 'AICC_AU' or @type = 'NETG_COK' or @type = 'SCO' or @type = 'DXT' or @type = 'TST'])*2 + count(/report/report_body/mod_list/mod[@type != 'ASS' and @type != 'AICC_AU' and @type != 'NETG_COK' and @type != 'SCO' and @type != 'DXT' and @type != 'TST'])"/>
			</xsl:when>
			<xsl:otherwise>2</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="rpt_xls" select="/report/report_body/template/xsl_list/xsl[@type = 'download']"/>
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
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_mgt_rpt.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			var mgt_rpt = new wbManagementReport;
			]]></script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<!-- header text -->
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn_group">用戶組</xsl:with-param>
			<xsl:with-param name="lab_lrn_user">用戶</xsl:with-param>
			<xsl:with-param name="lab_lrn">學員</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">所有學員</xsl:with-param>
			<xsl:with-param name="lab_cos_title">課程</xsl:with-param>
			<xsl:with-param name="lab_from">由 </xsl:with-param>
			<xsl:with-param name="lab_to"> 至 </xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">報名日期</xsl:with-param>
			<xsl:with-param name="lab_att_status">考勤狀況</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_enroll">已報名</xsl:with-param>
			<xsl:with-param name="lab_completed">已完成</xsl:with-param>
			<xsl:with-param name="lab_incompleted">未完成</xsl:with-param>
			<xsl:with-param name="lab_withdrawn">已放棄</xsl:with-param>
			<!-- content result text -->
			<xsl:with-param name="lab_no_item">沒有記錄</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_lrn_mod">模塊</xsl:with-param>
			<xsl:with-param name="lab_mod_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_mod_score">成績</xsl:with-param>
			<xsl:with-param name="lab_mod_grade">級別</xsl:with-param>
			<xsl:with-param name="lab_A">甲</xsl:with-param>
			<xsl:with-param name="lab_B">乙</xsl:with-param>
			<xsl:with-param name="lab_C">丙</xsl:with-param>
			<xsl:with-param name="lab_D">丁</xsl:with-param>
			<xsl:with-param name="lab_F">不合格</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_export">匯出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">我負責的學員</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">報讀我負責課程的學員</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn_group">用户组</xsl:with-param>
			<xsl:with-param name="lab_lrn_user">用户</xsl:with-param>
			<xsl:with-param name="lab_lrn">学员</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">所有学员</xsl:with-param>
			<xsl:with-param name="lab_cos_title">课程</xsl:with-param>
			<xsl:with-param name="lab_from">由 </xsl:with-param>
			<xsl:with-param name="lab_to"> 至 </xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">录取日期</xsl:with-param>
			<xsl:with-param name="lab_att_status">完成状态</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_enroll">已报名</xsl:with-param>
			<xsl:with-param name="lab_completed">已完成</xsl:with-param>
			<xsl:with-param name="lab_incompleted">未完成</xsl:with-param>
			<xsl:with-param name="lab_withdrawn">已放弃</xsl:with-param>
			<!-- content result text -->
			<xsl:with-param name="lab_no_item">没有记录</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_lrn_mod">模块</xsl:with-param>
			<xsl:with-param name="lab_mod_status">状态</xsl:with-param>
			<xsl:with-param name="lab_mod_score">成绩</xsl:with-param>
			<xsl:with-param name="lab_mod_grade">级别</xsl:with-param>
			<xsl:with-param name="lab_A">甲</xsl:with-param>
			<xsl:with-param name="lab_B">乙</xsl:with-param>
			<xsl:with-param name="lab_C">丙</xsl:with-param>
			<xsl:with-param name="lab_D">丁</xsl:with-param>
			<xsl:with-param name="lab_F">不合格</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_export">导出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">我负责的学员</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">报读我负责课程的学员</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn_group">Group</xsl:with-param>
			<xsl:with-param name="lab_lrn_user">Learner</xsl:with-param>
			<xsl:with-param name="lab_lrn">Learner</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">All learners</xsl:with-param>
			<xsl:with-param name="lab_cos_title">Course</xsl:with-param>
			<xsl:with-param name="lab_from">From </xsl:with-param>
			<xsl:with-param name="lab_to"> To </xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">Enrollment date</xsl:with-param>
			<xsl:with-param name="lab_att_status">Learning status</xsl:with-param>
			<xsl:with-param name="lab_all">All</xsl:with-param>
			<xsl:with-param name="lab_enroll">Enrolled</xsl:with-param>
			<xsl:with-param name="lab_completed">Completed</xsl:with-param>
			<xsl:with-param name="lab_incompleted">Incompleted</xsl:with-param>
			<xsl:with-param name="lab_withdrawn">Withdrawn</xsl:with-param>
			<!-- content result text -->
			<xsl:with-param name="lab_no_item">No record found</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_lrn_mod">Module</xsl:with-param>
			<xsl:with-param name="lab_mod_status">Status</xsl:with-param>
			<xsl:with-param name="lab_mod_score">Score</xsl:with-param>
			<xsl:with-param name="lab_mod_grade">Grade</xsl:with-param>
			<xsl:with-param name="lab_A">A</xsl:with-param>
			<xsl:with-param name="lab_B">B</xsl:with-param>
			<xsl:with-param name="lab_C">C</xsl:with-param>
			<xsl:with-param name="lab_D">D</xsl:with-param>
			<xsl:with-param name="lab_F">Fail</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_export">Export</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">My responsible learners</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">Learners that have enrolled in my responsible courses</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_lrn_group"/>
		<xsl:param name="lab_lrn_user"/>
		<xsl:param name="lab_lrn"/>
		<xsl:param name="lab_by_all_user"/>
		<xsl:param name="lab_cos_title"/>
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_att_create_timestamp"/>
		<xsl:param name="lab_att_status"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_enroll"/>
		<xsl:param name="lab_completed"/>
		<xsl:param name="lab_incompleted"/>
		<xsl:param name="lab_withdrawn"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_lrn_mod"/>
		<xsl:param name="lab_mod_status"/>
		<xsl:param name="lab_mod_score"/>
		<xsl:param name="lab_mod_grade"/>
		<xsl:param name="lab_A"/>
		<xsl:param name="lab_B"/>
		<xsl:param name="lab_C"/>
		<xsl:param name="lab_D"/>
		<xsl:param name="lab_F"/>
		<xsl:param name="lab_g_form_btn_export"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_answer_for_lrn"/>
		<xsl:param name="lab_answer_for_course_lrn"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$rpt_name != ''">
						<xsl:value-of select="$rpt_name"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="get_rte_title">
							<xsl:with-param name="rte_type" select="/report/report_body/template/@type"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="count(/report/report_body/report_list/record) = 0">
				<xsl:if test="report_body/spec/data_list/data[@name='usr_ent_id'] or report_body/spec/data_list/data[@name='s_usg_ent_id_lst'] or report_body/spec/data_list/data[@name='itm_id']  or report_body/spec/data_list/data[@name='att_create_start_datetime'] or report_body/spec/data_list/data[@name='att_create_end_datetime'] or report_body/spec/data_list/data[@name='all_user_ind'] or report_body/spec/data_list/data[@name='mod_id'] or report_body/spec/data_list/data[@name='ats_id']">
					<xsl:call-template name="report_criteria">
						<xsl:with-param name="this_width" select="$wb_gen_table_width"/>
						<xsl:with-param name="lab_cos_title" select="$lab_cos_title"/>
						<xsl:with-param name="lab_lrn" select="$lab_lrn"/>
						<xsl:with-param name="lab_lrn_group" select="$lab_lrn_group"/>
						<xsl:with-param name="lab_lrn_user" select="$lab_lrn_user"/>
						<xsl:with-param name="lab_by_all_user" select="$lab_by_all_user"/>
						<xsl:with-param name="lab_att_create_timestamp" select="$lab_att_create_timestamp"/>
						<xsl:with-param name="lab_from" select="$lab_from"/>
						<xsl:with-param name="lab_na" select="$lab_na"/>
						<xsl:with-param name="lab_to" select="$lab_to"/>
						<xsl:with-param name="lab_att_status" select="$lab_att_status"/>
						<xsl:with-param name="lab_all" select="$lab_all"/>
						<xsl:with-param name="lab_lrn_mod" select="$lab_lrn_mod"/>
						<xsl:with-param name="lab_answer_for_lrn" select="$lab_answer_for_lrn"/>
						<xsl:with-param name="lab_answer_for_course_lrn" select="$lab_answer_for_course_lrn"/>	
					</xsl:call-template>
				</xsl:if>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_item"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr>
						<td colspan="{$col_size + 2}">
							<xsl:if test="report_body/spec/data_list/data[@name='usr_ent_id'] or report_body/spec/data_list/data[@name='s_usg_ent_id_lst'] or report_body/spec/data_list/data[@name='itm_id']  or report_body/spec/data_list/data[@name='att_create_start_datetime'] or report_body/spec/data_list/data[@name='att_create_end_datetime'] or report_body/spec/data_list/data[@name='all_user_ind'] or report_body/spec/data_list/data[@name='mod_id'] or report_body/spec/data_list/data[@name='ats_id']">
								<xsl:call-template name="report_criteria">
									<xsl:with-param name="this_width">100%</xsl:with-param>
									<xsl:with-param name="lab_cos_title" select="$lab_cos_title"/>
									<xsl:with-param name="lab_lrn" select="$lab_lrn"/>
									<xsl:with-param name="lab_lrn_group" select="$lab_lrn_group"/>
									<xsl:with-param name="lab_lrn_user" select="$lab_lrn_user"/>
									<xsl:with-param name="lab_by_all_user" select="$lab_by_all_user"/>
									<xsl:with-param name="lab_att_create_timestamp" select="$lab_att_create_timestamp"/>
									<xsl:with-param name="lab_from" select="$lab_from"/>
									<xsl:with-param name="lab_na" select="$lab_na"/>
									<xsl:with-param name="lab_to" select="$lab_to"/>
									<xsl:with-param name="lab_att_status" select="$lab_att_status"/>
									<xsl:with-param name="lab_all" select="$lab_all"/>
									<xsl:with-param name="lab_lrn_mod" select="$lab_lrn_mod"/>
									<xsl:with-param name="lab_answer_for_lrn" select="$lab_answer_for_lrn"/>
									<xsl:with-param name="lab_answer_for_course_lrn" select="$lab_answer_for_course_lrn"/>	
								</xsl:call-template>
							</xsl:if>
						</td>
					</tr>
				</table>
				<!-- start draw table header -->
				<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="table wzb-ui-table">
					
					<tr class="SecBg">
						<td valign="middle">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<xsl:for-each select="/report/report_body/spec/data_list/data[@name = 'usr_content_lst' or @name = 'itm_content_lst' or @name = 'run_content_lst' or @name = 'content_lst']">
							<td valign="middle" align="center">
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</td>
						</xsl:for-each>
						<xsl:for-each select="/report/report_body/mod_list/mod">
							<xsl:variable name="mod_id" select="@id"/>
							<xsl:choose>
								<xsl:when test="@type = 'ASS' or @type = 'AICC_AU' or @type = 'NETG_COK' or @type = 'SCO' or @type = 'DXT' or @type = 'TST' ">
									<td valign="middle" colspan="2" align="center" nowrap="nowrap">
										<span class="SmallText">
											<b>
												<xsl:value-of select="/report/report_body/presentation/data[@name = 'mod_id' and @value=$mod_id]/@display"/>
											</b>
										</span>
									</td>
								</xsl:when>
								<xsl:otherwise>
									<td valign="middle" align="center" nowrap="nowrap">
										<span class="SmallText">
											<b>
												<xsl:value-of select="/report/report_body/presentation/data[@name = 'mod_id' and @value=$mod_id]/@display"/>
											</b>
										</span>
									</td>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
						<td valign="middle">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<tr class="SecBg wzb-ui-table-head">
						<td valign="middle">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<xsl:apply-templates select="/report/report_body/spec/data_list/data[@name = 'usr_content_lst']" mode="title">
							<xsl:with-param name="lab_login_id" select="$lab_login_id"/>
							<xsl:with-param name="lab_dis_name" select="$lab_dis_name"/>
							<xsl:with-param name="lab_group" select="$lab_group"/>
							<xsl:with-param name="lab_grade" select="$lab_grade"/>
							<xsl:with-param name="lab_e_mail" select="$lab_e_mail"/>
							<xsl:with-param name="lab_tel_1" select="$lab_tel_1"/>
						</xsl:apply-templates>
						<xsl:apply-templates select="/report/report_body/spec/data_list/data[@name = 'content_lst']" mode="title">
							<xsl:with-param name="lab_att_status" select="$lab_att_status"/>
							<xsl:with-param name="lab_att_create_timestamp" select="$lab_att_create_timestamp"/>
						</xsl:apply-templates>
						<xsl:for-each select="/report/report_body/mod_list/mod">
							<td valign="middle" align="center" nowrap="nowrap">
								<span class="SmallText">
									<xsl:value-of select="$lab_mod_status"/>
								</span>
							</td>
							<xsl:choose>
								<xsl:when test="@type = 'ASS' and @max_score &lt; 0">
									<td valign="middle" align="center" nowrap="nowrap">
										<span class="SmallText">
											<xsl:value-of select="$lab_mod_grade"/>
										</span>
									</td>
								</xsl:when>
								<xsl:when test="@type = 'ASS' and @max_score &gt; 0">
									<td valign="middle" align="center" nowrap="nowrap">
										<span class="SmallText">
											<xsl:value-of select="$lab_mod_score"/>
										</span>
									</td>
								</xsl:when>
								<xsl:when test="@type = 'AICC_AU' or @type = 'NETG_COK' or @type = 'SCO' or @type = 'DXT' or @type = 'TST'">
									<td valign="middle" align="center" nowrap="nowrap">
										<span class="SmallText">
											<xsl:value-of select="$lab_mod_score"/>
										</span>
									</td>
								</xsl:when>
							</xsl:choose>
						</xsl:for-each>
						<td valign="middle">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<xsl:apply-templates select="report_body/report_list/record">
						<xsl:with-param name="lab_na" select="$lab_na"/>
						<xsl:with-param name="lab_A">
							<xsl:value-of select="$lab_A"/>
						</xsl:with-param>
						<xsl:with-param name="lab_B">
							<xsl:value-of select="$lab_B"/>
						</xsl:with-param>
						<xsl:with-param name="lab_C">
							<xsl:value-of select="$lab_C"/>
						</xsl:with-param>
						<xsl:with-param name="lab_D">
							<xsl:value-of select="$lab_D"/>
						</xsl:with-param>
						<xsl:with-param name="lab_F">
							<xsl:value-of select="$lab_F"/>
						</xsl:with-param>
					</xsl:apply-templates>
					
				</table>
				<xsl:call-template name="wb_ui_space">
					<xsl:with-param name="height">15</xsl:with-param>
				</xsl:call-template>
				<table  cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr>
						<td valign="middle" width="1">
						</td>
						<td colspan="{$col_size + 2}">
							<span class="Text">
								<xsl:value-of select="$lab_showing"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:choose>
									<xsl:when test="$cur_page = 1">1</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="($cur_page - 1)*$page_size + 1"/>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:text>&#160;-&#160;</xsl:text>
								<xsl:choose>
									<xsl:when test="$total &lt; ($cur_page * $page_size)">
										<xsl:value-of select="$total"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$cur_page * $page_size"/>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$lab_page_of"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$total"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$lab_page_piece"/>
							</span>
						</td>
					</tr>
				</table>
			</xsl:otherwise>
		</xsl:choose>
		<div class="wzb-bar">
			<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
				<tr>
					<td align="center">
						<xsl:if test="count(report_body/report_list/record)&gt;0">
							<xsl:variable name="title">
								<xsl:call-template name="escape_js">
									<xsl:with-param name="input_str" select="report_body/spec/title/text()"/>
								</xsl:call-template>
							</xsl:variable>
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_export"/>
								<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.rslt_dl_rpt_adv('<xsl:value-of select="$rpt_xls"/>','<xsl:value-of select="$title"/>')</xsl:with-param>
							</xsl:call-template>
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</xsl:if>
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
	<xsl:template match="record">
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_A"/>
		<xsl:param name="lab_B"/>
		<xsl:param name="lab_C"/>
		<xsl:param name="lab_D"/>
		<xsl:param name="lab_F"/>
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2">StatRowsEven</xsl:when>
				<xsl:otherwise>StatRowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr class="{$row_class}">
			<td>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<!-- user -->
			<xsl:apply-templates select="/report/report_body/spec/data_list/data[@name = 'usr_content_lst']" mode="value">
				<xsl:with-param name="lab_na" select="$lab_na"/>
				<xsl:with-param name="this" select="."/>
			</xsl:apply-templates>
			<xsl:apply-templates select="/report/report_body/spec/data_list/data[@name = 'content_lst']" mode="value">
				<xsl:with-param name="this" select="."/>
				<xsl:with-param name="lab_na" select="$lab_na"/>
			</xsl:apply-templates>
			<xsl:variable name="this" select="."/>
			<xsl:for-each select="/report/report_body/mod_list/mod">
				<xsl:variable name="mod_id">
					<xsl:value-of select="@id"/>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="$this/mov/@mod_id = $mod_id">
						<td valign="top" nowrap="nowrap" align="center">
							<span class="SmallText">
								<xsl:variable name="score_check">
									<xsl:choose>
										<xsl:when test="$this/mov[@mod_id = $mod_id]/@grade != ''">
											<xsl:value-of select="$this/mov[@mod_id = $mod_id]/@grade"/>
										</xsl:when>
										<xsl:when test="$this/mov[@mod_id = $mod_id]/@score > 0">
											<xsl:value-of select="$this/mov[@mod_id = $mod_id]/@score"/>
										</xsl:when>
									</xsl:choose>
								</xsl:variable>
								<xsl:call-template name="display_progress_tracking">
									<xsl:with-param name="status" select="$this/mov[@mod_id = $mod_id]/@status"/>
									<xsl:with-param name="type">module</xsl:with-param>
									<xsl:with-param name="show_text">true</xsl:with-param>
									<xsl:with-param name="show_icon">false</xsl:with-param>
									<xsl:with-param name="score" select="$score_check"/>
									<xsl:with-param name="mod_type" select="@type"/>
									<xsl:with-param name="is_wizpack">
										<xsl:if test="stylesheet">true</xsl:if>
									</xsl:with-param>
								</xsl:call-template>
							</span>
						</td>
						<xsl:choose>
							<xsl:when test="@type = 'ASS' and @max_score &lt; 0">
								<td valign="top" nowrap="nowrap" align="center">
									<span class="SmallText">
										<xsl:variable name="grade" select="$this/mov[@mod_id = $mod_id]/@grade"/>
										<xsl:choose>
											<xsl:when test="$grade = 'A+' ">
												<xsl:value-of select="$lab_A"/>+</xsl:when>
											<xsl:when test="$grade = 'A' ">
												<xsl:value-of select="$lab_A"/>
											</xsl:when>
											<xsl:when test="$grade = 'A-' ">
												<xsl:value-of select="$lab_A"/>-</xsl:when>
											<xsl:when test="$grade = 'B+' ">
												<xsl:value-of select="$lab_B"/>+</xsl:when>
											<xsl:when test="$grade = 'B' ">
												<xsl:value-of select="$lab_B"/>
											</xsl:when>
											<xsl:when test="$grade = 'B-' ">
												<xsl:value-of select="$lab_B"/>-</xsl:when>
											<xsl:when test="$grade = 'C+' ">
												<xsl:value-of select="$lab_C"/>+</xsl:when>
											<xsl:when test="$grade = 'C' ">
												<xsl:value-of select="$lab_C"/>
											</xsl:when>
											<xsl:when test="$grade = 'C-' ">
												<xsl:value-of select="$lab_C"/>-</xsl:when>
											<xsl:when test="$grade = 'D' ">
												<xsl:value-of select="$lab_D"/>
											</xsl:when>
											<xsl:when test="$grade = 'F' ">
												<xsl:value-of select="$lab_F"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="$lab_na"/>
											</xsl:otherwise>
										</xsl:choose>
									</span>
								</td>
							</xsl:when>
							<xsl:when test="@type = 'ASS' and @max_score &gt; 0">
								<td valign="top" nowrap="nowrap" align="right">
									<span class="SmallText">
										<xsl:value-of select="$this/mov[@mod_id = $mod_id]/@score"/>
									</span>
								</td>
							</xsl:when>
							<xsl:when test="@type = 'AICC_AU' or @type = 'NETG_COK' or @type = 'SCO'">
								<td valign="top" nowrap="nowrap" align="right">
									<span class="SmallText">
										<xsl:value-of select="$this/mov[@mod_id = $mod_id]/@score"/>
									</span>
								</td>
							</xsl:when>
							<xsl:when test=" @type = 'DXT' or @type = 'TST'">
								<td valign="top" nowrap="nowrap" align="right">
									<span class="SmallText">
										<xsl:choose>
											<xsl:when test="$this/mov[@mod_id = $mod_id]/@status='I'">
												<xsl:value-of select="$lab_na"/>
											</xsl:when>

											<xsl:otherwise>
												<xsl:value-of select="$this/mov[@mod_id = $mod_id]/@score"/>
											</xsl:otherwise>
										</xsl:choose>
									</span>
								</td>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<td valign="top" nowrap="nowrap" align="center">
							<xsl:value-of select="$lab_na"/>
						</td>
						<xsl:if test="@type = 'AICC_AU' or @type = 'NETG_COK' or @type = 'SCO' or @type = 'DXT' or @type = 'TST' or @type = 'ASS'">
							<td valign="top" nowrap="nowrap" align="center">
								<xsl:value-of select="$lab_na"/>
							</td>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<td>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data[@name = 'content_lst']" mode="title">
		<xsl:param name="lab_att_status"/>
		<xsl:param name="lab_att_create_timestamp"/>
		<td nowrap="nowrap" align="center">
			<span class="SmallText">
				<xsl:choose>
					<xsl:when test="@value = 'att_create_timestamp'">
						<xsl:value-of select="$lab_att_create_timestamp"/>
					</xsl:when>
					<xsl:when test="@value ='att_status'">
						<xsl:value-of select="$lab_att_status"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="@value"/>
					</xsl:otherwise>
				</xsl:choose>
			</span>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data[@name = 'usr_content_lst']" mode="title">
		<xsl:param name="lab_login_id"/>
		<xsl:param name="lab_dis_name"/>
		<xsl:param name="lab_group"/>
		<xsl:param name="lab_grade"/>
		<xsl:param name="lab_e_mail"/>
		<xsl:param name="lab_tel_1"/>
		<td nowrap="nowrap">
			<span class="SmallText">
				<xsl:choose>
					<xsl:when test="@value = 'usr_id'">
						<xsl:value-of select="$lab_login_id"/>
					</xsl:when>
					<xsl:when test="@value = 'usr_display_bil'">
						<xsl:value-of select="$lab_dis_name"/>
					</xsl:when>
					<xsl:when test="@value = 'USR_PARENT_USG'">
						<xsl:value-of select="$lab_group"/>
					</xsl:when>
					<xsl:when test="@value = 'USR_CURRENT_UGR'">
						<xsl:value-of select="$lab_grade"/>
					</xsl:when>
					<xsl:when test="@value = 'usr_email'">
						<xsl:value-of select="$lab_e_mail"/>
					</xsl:when>
					<xsl:when test="@value = 'usr_tel_1'">
						<xsl:value-of select="$lab_tel_1"/>
					</xsl:when>
					<xsl:when test="@value = 'usr_extra_2'">
						<xsl:value-of select="$lab_staff_no"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="@value"/>
					</xsl:otherwise>
				</xsl:choose>
			</span>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data[@name='content_lst']" mode="value">
		<xsl:param name="lab_na"/>
		<xsl:param name="this"/>
		<td valign="top" nowrap="nowrap" align="center">
			<span class="SmallText">
				<xsl:choose>
					<xsl:when test="@value = 'att_status'">
						<xsl:variable name="status_id">
							<xsl:choose>
								<xsl:when test="$this/@att_ats_id=''">0</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$this/@att_ats_id"/>
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
					</xsl:when>
					<xsl:when test="@value = 'att_create_timestamp'">
						<xsl:choose>
							<xsl:when test="$this/@att_create_timestamp!='' and $this/@att_create_timestamp">
								<span class="SmallText">
									<xsl:call-template name="display_time">
										<xsl:with-param name="my_timestamp">
											<xsl:value-of select="$this/@att_create_timestamp"/>
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
					</xsl:when>
					<xsl:otherwise>
						<span class="SmallText">
							<xsl:value-of select="$lab_na"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text>&#160;</xsl:text>
			</span>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data[@name = 'usr_content_lst']" mode="value">
		<xsl:param name="this"/>
		<xsl:param name="is_run"/>
		<xsl:param name="lab_na"/>
		<xsl:variable name="text_class">
			<xsl:choose>
				<xsl:when test="$is_run = 'true'">SmallText</xsl:when>
				<xsl:otherwise>SmallText</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<td valign="top" nowrap="nowrap">
			<span class="SmallText">
				<xsl:choose>
					<xsl:when test="@value = 'usr_id'">
						<xsl:choose>
							<xsl:when test="$this/@usr_ste_usr_id!= ''">
								<xsl:value-of select="$this/@usr_ste_usr_id"/>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'usr_display_bil'">
						<xsl:choose>
							<xsl:when test="$this/@usr_display_bil != ''">
								<xsl:value-of select="$this/@usr_display_bil"/>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'USR_PARENT_USG'">
						<xsl:choose>
							<xsl:when test="$this/@usg_full_path  != ''">
								<xsl:value-of select="$this/@usg_full_path"/>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'USR_CURRENT_UGR'">
						<xsl:choose>
							<xsl:when test="$this/@ugr_name != ''">
								<xsl:value-of select="$this/@ugr_name"/>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'usr_email'">
						<xsl:choose>
							<xsl:when test="$this/@usr_email != ''">
								<xsl:value-of select="$this/@usr_email"/>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'usr_tel_1'">
						<xsl:choose>
							<xsl:when test="$this/@usr_tel_1 != ''">
								<xsl:value-of select="$this/@usr_tel_1"/>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'usr_extra_2'">
						<xsl:choose>
							<xsl:when test="$this/@usr_extra_2 != ''">
								<xsl:value-of select="$this/@usr_extra_2"/>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="@value"/>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text>&#160;</xsl:text>
			</span>
		</td>
	</xsl:template>
	<!-- =================================================================================== -->
	<xsl:template name="report_criteria">
		<xsl:param name="this_width"/>
		<xsl:param name="lab_cos_title"/>
		<xsl:param name="lab_lrn"/>
		<xsl:param name="lab_lrn_group"/>
		<xsl:param name="lab_lrn_user"/>
		<xsl:param name="lab_by_all_user"/>
		<xsl:param name="lab_att_create_timestamp"/>
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_att_status"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_lrn_mod"/>
		<xsl:param name="lab_answer_for_lrn"/>
		<xsl:param name="lab_answer_for_course_lrn"/>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width" select="$this_width"/>
		</xsl:call-template>
		<table cellpadding="3" cellspacing="0" border="0" width="{$this_width}" class="Bg">
			<tr>
				<td width="150" align="right" height="10">
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td>
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
			<!-- course title -->
			<xsl:if test="report_body/spec/data_list/data[@name='itm_id']">
				<tr>
					<td align="right" valign="top" class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_cos_title"/>
							<xsl:text>：</xsl:text>
						</span>
					</td>
					<td class="wzb-form-control">
						<span class="Text">
							<xsl:apply-templates select="report_body/spec//data[@name='itm_id']" mode="title"/>
						</span>
					</td>
				</tr>
			</xsl:if>
			<!-- mod list -->
			<xsl:if test="report_body/spec/data_list/data[@name='mod_id']">
				<tr>
					<td align="right" valign="top"  class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_lrn_mod"/>
							<xsl:text>：</xsl:text>
						</span>
					</td>
					<td  class="wzb-form-control">
						<span class="Text">
							<xsl:for-each select="report_body/spec/data_list/data[@name='mod_id']">
								<xsl:variable name="mod_id">
									<xsl:value-of select="@value"/>
								</xsl:variable>
								<xsl:value-of select="/report/report_body/presentation/data[@name='mod_id' and @value=$mod_id]/@display"/>
								<xsl:if test="position()!=last()">, </xsl:if>
							</xsl:for-each>
						</span>
					</td>
				</tr>
			</xsl:if>
			<!-- learner -->
			<xsl:if test="report_body/spec/data_list/data[@name='usr_ent_id'] or report_body/spec/data_list/data[@name='all_user_ind' and @value='1']">
				<tr>
					<td align="right" valign="top"  class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_lrn_user"/>
							<xsl:text>：</xsl:text>
						</span>
					</td>
					<td  class="wzb-form-control">
						<span class="Text">
							<xsl:choose>
								<xsl:when test="report_body/spec/data_list/data[@name='all_user_ind']/@value = '1'">
									<xsl:choose>
										<xsl:when test="$tc_enabled='true'">
											<xsl:choose>
												<xsl:when test="report_body/spec/data_list/data[@name='answer_for_lrn']/@value = '1' and report_body/spec/data_list/data[@name='answer_for_course_lrn']/@value = '0' ">
													<xsl:value-of select="$lab_answer_for_lrn"/>
												</xsl:when>
												<xsl:when test="report_body/spec/data_list/data[@name='answer_for_lrn']/@value = '0' and report_body/spec/data_list/data[@name='answer_for_course_lrn']/@value = '1' ">
													<xsl:value-of select="$lab_answer_for_course_lrn"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$lab_by_all_user"/>
												</xsl:otherwise>										
											</xsl:choose>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$lab_by_all_user"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:when>
								<xsl:otherwise>
									<xsl:for-each select="report_body/spec/data_list/data[@name='usr_ent_id']">
										<xsl:variable name="ent_id">
											<xsl:value-of select="@value"/>
										</xsl:variable>
										<xsl:value-of select="/report/report_body/presentation/data[@name='usr_ent_id' and @value=$ent_id]/@display"/>
										<xsl:if test="position()!=last()">, </xsl:if>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
				</tr>
			</xsl:if>
			<!-- user group -->
			<xsl:if test="report_body/spec/data_list/data[@name='usg_ent_id'] ">
				<tr>
					<td align="right" valign="top"  class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_lrn_group"/>
							<xsl:text>：</xsl:text>
						</span>
					</td>
					<td class="wzb-form-control">
						<span class="Text">
							<xsl:choose>
								<xsl:when test="count(/report/report_body/presentation/data[@name='usg_ent_id']) = 0">
									<xsl:value-of select="$lab_na"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:for-each select="report_body/spec/data_list/data[@name='usg_ent_id']">
										<xsl:variable name="usg_ent_id">
											<xsl:value-of select="@value"/>
										</xsl:variable>
										<xsl:value-of select="/report/report_body/presentation/data[@name='usg_ent_id' and @value=$usg_ent_id]/@display"/>
										<xsl:if test="position()!=last()">, </xsl:if>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
				</tr>
			</xsl:if>
			<!-- attendance date -->
			<xsl:choose>
				<xsl:when test="count(report_body/spec/data_list/data[@name='att_create_start_datetime']) = 0 and count(report_body/spec/data_list/data[@name='att_create_end_datetime']) = 0">
					<!--<tr>
						<td align="right" valign="top">
							<span class="TitleText">
								<xsl:value-of select="$lab_att_create_timestamp"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td>
							<span class="Text">
								<xsl:value-of select="$lab_na"/>
							</span>
						</td>
					</tr>-->
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td align="right" valign="top"  class="wzb-form-label">
							<span class="TitleText">
								<xsl:value-of select="$lab_att_create_timestamp"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td  class="wzb-form-control">
							<span class="Text">
								<xsl:value-of select="$lab_from"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:choose>
									<xsl:when test="report_body/spec/data_list/data[@name='att_create_start_datetime']">
										<xsl:call-template name="display_time">
											<xsl:with-param name="my_timestamp" select="report_body/spec/data_list/data[@name='att_create_start_datetime']/@value"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_na"/>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$lab_to"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:choose>
									<xsl:when test="report_body/spec/data_list/data[@name='att_create_end_datetime']">
										<xsl:call-template name="display_time">
											<xsl:with-param name="my_timestamp" select="report_body/spec/data_list/data[@name='att_create_end_datetime']/@value"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_na"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:if test="report_body/spec/data_list/data[@name='ats_id']">
				<tr>
					<td align="right" valign="top"  class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_att_status"/>
							<xsl:text>：</xsl:text>
						</span>
					</td>
					<td  class="wzb-form-control">
						<span class="Text">
							<xsl:variable name="cur_ats_id" select="/report/report_body/spec/data_list/data[@name='ats_id']/@value"/>
							<xsl:choose>
								<xsl:when test="$cur_ats_id='0'">
									<xsl:value-of select="$lab_all"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="get_ats_title">
										<xsl:with-param name="ats_id" select="$cur_ats_id"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
						</span>
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
		<xsl:call-template name="wb_ui_space"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="status">
		<xsl:call-template name="get_ats_title">
			<xsl:with-param name="ats_id" select="@id"/>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data" mode="title">
		<xsl:variable name="name" select="@name"/>
		<xsl:variable name="value" select="@value"/>
		<xsl:for-each select="/report/report_body/presentation/data[@name=$name and @value = $value]/parent">
			<xsl:value-of select="@display"/>
			<xsl:text>(</xsl:text>
			<xsl:value-of select="@itm_code"/>
			<xsl:text>) > </xsl:text>
		</xsl:for-each>
		<xsl:value-of select="/report/report_body/presentation/data[@name=$name and @value = $value]/@display"/>
		<xsl:text>(</xsl:text>
		<xsl:value-of select="/report/report_body/presentation/data[@name=$name and @value = $value]/@itm_code"/>
		<xsl:text>)</xsl:text>
	</xsl:template>
</xsl:stylesheet>
