<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/trun_date.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="rpt_name" select="/report/report_body/spec/title"/>
	<xsl:variable name="mod_id" select="/report/report_body/spec/data_list/data[@name='mod_id']/@value"/>
	<xsl:variable name="mov_status" select="report/report_body/module_submission/submission_list/@cur_status"/>
	<xsl:variable name="cur_page" select="report/report_body/module_submission/pagination/@cur_page"/>
	<xsl:variable name="total_rec" select="report/report_body/module_submission/pagination/@total_rec"/>
	<xsl:variable name="total_page" select="report/report_body/module_submission/pagination/@total_page"/>
	<xsl:variable name="sort_col" select="report/report_body/module_submission/pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="report/report_body/module_submission/pagination/@sort_order"/>
	<xsl:variable name="page_size" select="report/report_body/module_submission/pagination/@page_size"/>
	<xsl:variable name="ts" select="report/report_body/module_submission/pagination/@timestamp"/>
	<xsl:variable name="sort_order">
		<xsl:choose>
			<xsl:when test="$cur_order = 'asc' or $cur_order = 'ASC' ">desc</xsl:when>
			<xsl:otherwise>asc</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="cur_status" select="report/report_body/module_submission/submission_list/@cur_status"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:apply-templates select="report"/>
	</xsl:template>
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
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_lrn_soln.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_mgt_rpt.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
				mod = new wbModule;


				var mgt_rpt = new wbManagementReport;
				unit = 0;
				
				lrn_soln = new wbLearnSolution;
				
				function chgYear(year){
					url = parent.location.href;
					url = setUrlParam("calendar_year",year,url);
					url = setUrlParam("timestamp","",url);
					parent.location.href = url;
				}
				
				function sortCol(colName, order){
					url = parent.location.href;
					if (order=="ASC")
						url = setUrlParam("sort_order","DESC",url);
					else
						url = setUrlParam("sort_order","ASC",url);
					url = setUrlParam("sort_col",colName,url);
					url = setUrlParam("timestamp","]]><xsl:value-of select="report_body/pagination/@timestamp"/><![CDATA[",url);
					parent.location.href = url;
				}
				
				function getPage(pg){
					url = parent.location.href;
					url = setUrlParam("cur_page",pg,url);
					url = setUrlParam("timestamp","]]><xsl:value-of select="report_body/pagination/@timestamp"/><![CDATA[",url);
					parent.location.href = url;
				}
			]]></script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
		</head>
		<BODY leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml" onsubmit="lrn_soln.srh_lrn_history_exec(frmXml,'{$wb_lang}');return false;">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</BODY>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_home">首頁</xsl:with-param>
			<xsl:with-param name="lab_evn_result">調查問卷結果</xsl:with-param>
			<xsl:with-param name="lab_submission_list">提交列表</xsl:with-param>
			<xsl:with-param name="lab_svy_ind_rpt">個人調查問卷報告</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_group">小組</xsl:with-param>
			<xsl:with-param name="lab_grade">職位等級</xsl:with-param>
			<xsl:with-param name="lab_category">子目錄</xsl:with-param>
			<xsl:with-param name="lab_duration">時限</xsl:with-param>
			<xsl:with-param name="lab_result">結果</xsl:with-param>
			<xsl:with-param name="lab_showing">顯示</xsl:with-param>
			<xsl:with-param name="lab_prev">前5個結果</xsl:with-param>
			<xsl:with-param name="lab_next">後5個結果</xsl:with-param>
			<xsl:with-param name="lab_total">合計</xsl:with-param>
			<xsl:with-param name="lab_learner">學員</xsl:with-param>
			<xsl:with-param name="lab_no_item">沒有查到報告</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_recycle_bin">資源回收筒</xsl:with-param>
			<xsl:with-param name="lab_lrn_group">用戶/用戶組</xsl:with-param>
			<xsl:with-param name="lab_not_specified">所有</xsl:with-param>
			<xsl:with-param name="lab_survey">調查問卷</xsl:with-param>
			<xsl:with-param name="lab_inst">報告查詢條件</xsl:with-param>
			<xsl:with-param name="lab_mgmt_rpt">管理報告</xsl:with-param>
			<xsl:with-param name="lab_submission_date">提交日期</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_no_record">沒有提交</xsl:with-param>
			<xsl:with-param name="lab_status_completed">已完成</xsl:with-param>
			<xsl:with-param name="lab_status_not_attempted">尚未完成</xsl:with-param>
			<xsl:with-param name="lab_view">查看提交</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_export_all">匯出所有提交</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_home">首页</xsl:with-param>
			<xsl:with-param name="lab_evn_result">调查问卷结果</xsl:with-param>
			<xsl:with-param name="lab_submission_list">提交列表</xsl:with-param>
			<xsl:with-param name="lab_svy_ind_rpt">调查问卷提交报告</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_group">组</xsl:with-param>
			<xsl:with-param name="lab_grade">职位等级</xsl:with-param>
			<xsl:with-param name="lab_category">子目录</xsl:with-param>
			<xsl:with-param name="lab_duration">时限</xsl:with-param>
			<xsl:with-param name="lab_result">结果</xsl:with-param>
			<xsl:with-param name="lab_showing">显示</xsl:with-param>
			<xsl:with-param name="lab_prev">前5个结果</xsl:with-param>
			<xsl:with-param name="lab_next">后5个结果</xsl:with-param>
			<xsl:with-param name="lab_total">合计</xsl:with-param>
			<xsl:with-param name="lab_learner">学员</xsl:with-param>
			<xsl:with-param name="lab_no_item">没有查到报告</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_recycle_bin">回收站</xsl:with-param>
			<xsl:with-param name="lab_lrn_group">用户/用户组</xsl:with-param>
			<xsl:with-param name="lab_not_specified">所有</xsl:with-param>
			<xsl:with-param name="lab_survey">调查问卷</xsl:with-param>
			<xsl:with-param name="lab_inst">报告查询条件</xsl:with-param>
			<xsl:with-param name="lab_mgmt_rpt">管理报告</xsl:with-param>
			<xsl:with-param name="lab_submission_date">提交日期</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_no_record">没有提交</xsl:with-param>
			<xsl:with-param name="lab_status_completed">已完成</xsl:with-param>
			<xsl:with-param name="lab_status_not_attempted">未完成</xsl:with-param>
			<xsl:with-param name="lab_view">查看提交</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_export_all">导出所有提交</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_home">Home</xsl:with-param>
			<xsl:with-param name="lab_evn_result">Survey results</xsl:with-param>
			<xsl:with-param name="lab_submission_list">Submission list</xsl:with-param>
			<xsl:with-param name="lab_svy_ind_rpt">Survey submission report</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_group">Group</xsl:with-param>
			<xsl:with-param name="lab_grade">Grade</xsl:with-param>
			<xsl:with-param name="lab_category">Category</xsl:with-param>
			<xsl:with-param name="lab_duration">Duration (hrs)</xsl:with-param>
			<xsl:with-param name="lab_result">Result</xsl:with-param>
			<xsl:with-param name="lab_showing">Showing</xsl:with-param>
			<xsl:with-param name="lab_prev">prev 5</xsl:with-param>
			<xsl:with-param name="lab_next">next 5</xsl:with-param>
			<xsl:with-param name="lab_total">Total</xsl:with-param>
			<xsl:with-param name="lab_learner">Learner</xsl:with-param>
			<xsl:with-param name="lab_no_item">No report found</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_recycle_bin">Recycle bin</xsl:with-param>
			<xsl:with-param name="lab_lrn_group">Learner/group</xsl:with-param>
			<xsl:with-param name="lab_not_specified">All</xsl:with-param>
			<xsl:with-param name="lab_survey">Survey</xsl:with-param>
			<xsl:with-param name="lab_inst">Report criteria</xsl:with-param>
			<xsl:with-param name="lab_mgmt_rpt">Management report</xsl:with-param>
			<xsl:with-param name="lab_submission_date">Submission date</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_no_record">No submission</xsl:with-param>
			<xsl:with-param name="lab_status_completed">Completed</xsl:with-param>
			<xsl:with-param name="lab_status_not_attempted">Incompleted</xsl:with-param>
			<xsl:with-param name="lab_view">View submission</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_export_all">Export all submissions</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_home"/>
		<xsl:param name="lab_evn_result"/>
		<xsl:param name="lab_submission_list"/>
		<xsl:param name="lab_svy_ind_rpt"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_group"/>
		<xsl:param name="lab_grade"/>
		<xsl:param name="lab_category"/>
		<xsl:param name="lab_duration"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_result"/>
		<xsl:param name="lab_prev"/>
		<xsl:param name="lab_next"/>
		<xsl:param name="lab_total"/>
		<xsl:param name="lab_showing"/>
		<xsl:param name="lab_learner"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_recycle_bin"/>
		<xsl:param name="lab_lrn_group"/>
		<xsl:param name="lab_not_specified"/>
		<xsl:param name="lab_survey"/>
		<xsl:param name="lab_inst"/>
		<xsl:param name="lab_mgmt_rpt"/>
		<xsl:param name="lab_submission_date"/>
		<xsl:param name="lab_no_record"/>
		<xsl:param name="lab_status_completed"/>
		<xsl:param name="lab_status_not_attempted"/>
		<xsl:param name="lab_view"/>
		<xsl:param name="lab_g_txt_btn_export_all"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_evn_result"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_inst"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
			<tr>
				<td width="20%" align="right" height="10">
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td width="80%">
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
			<!-- learner / group -->
			<tr>
				<td align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_lrn_group"/>:</span>
				</td>
				<td>
					<span class="TitleText">
						<xsl:if test="not(report_body/spec/data_list/data[@name='ent_id'])">
							<xsl:value-of select="$lab_not_specified"/>
						</xsl:if>
						<xsl:for-each select="report_body/spec/data_list/data[@name='ent_id']">
							<xsl:variable name="ent_id">
								<xsl:value-of select="@value"/>
							</xsl:variable>
							<xsl:value-of select="/report/report_body/presentation/data[@name='ent_id' and @value=$ent_id]/@display"/>
							<xsl:if test="position()!=last()">, </xsl:if>
						</xsl:for-each>
					</span>
				</td>
			</tr>
			<!-- module title -->
			<tr>
				<td align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_survey"/>:</span>
				</td>
				<td>
					<span class="Text">
						<xsl:choose>
							<xsl:when test="not(report_body/spec/data_list/data[@name='mod_id'])">
								<xsl:value-of select="$lab_not_specified"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="/report/report_body/presentation/data[@name='mod_id']/survey/title"/>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right" height="10">
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td width="80%">
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_space"/>
		<xsl:variable name="ent_id_lst">
			<xsl:for-each select="/report/report_body/spec/data_list/data[@name='ent_id']">
				<xsl:if test="not(position()=1)">
					<xsl:text>~</xsl:text>
				</xsl:if>
				<xsl:value-of select="@value"/>
			</xsl:for-each>
		</xsl:variable>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_submission_list"/>
			</xsl:with-param>
			<xsl:with-param name="extra_td">
				<xsl:if test="count(/report/report_body/module_submission/submission_list/submission)!= 0">
					<td align="right">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_g_txt_btn_export_all"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">Javascript:mod.dl_report('<xsl:value-of select="$mod_id"/>','',  '<xsl:value-of select="$ent_id_lst"/>', '', '', '')</xsl:with-param>
						</xsl:call-template>
					</td>
				</xsl:if>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<!--newnewnew-->
		<!--
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
			<tr>
				<td align="right">
					<xsl:choose>
						<xsl:when test="count(/report/report_body/module_submission/submission_list/submission)=0">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:variable name="ent_id_lst">
								<xsl:for-each select="/report/report_body/spec/data_list/data[@name='ent_id']">
									<xsl:if test="not(position()=1)">
										<xsl:text>~</xsl:text>
									</xsl:if>
									<xsl:value-of select="@value"/>
								</xsl:for-each>
							</xsl:variable>
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$lab_g_txt_btn_export_all"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">Javascript:mod.dl_report_by_itm('<xsl:value-of select="$mod_id"/>','',  '<xsl:value-of select="$ent_id_lst"/>', '', '', '')</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</table>-->
		<xsl:apply-templates select="/report/report_body/module_submission/submission_list">
			<xsl:with-param name="lab_learner">
				<xsl:value-of select="$lab_learner"/>
			</xsl:with-param>
			<xsl:with-param name="lab_group">
				<xsl:value-of select="$lab_group"/>
			</xsl:with-param>
			<xsl:with-param name="lab_submission_date">
				<xsl:value-of select="$lab_submission_date"/>
			</xsl:with-param>
			<xsl:with-param name="lab_status">
				<xsl:value-of select="$lab_status"/>
			</xsl:with-param>
			<xsl:with-param name="lab_no_record">
				<xsl:value-of select="$lab_no_record"/>
			</xsl:with-param>
			<xsl:with-param name="lab_status_not_attempted">
				<xsl:value-of select="$lab_status_not_attempted"/>
			</xsl:with-param>
			<xsl:with-param name="lab_status_completed">
				<xsl:value-of select="$lab_status_completed"/>
			</xsl:with-param>
			<xsl:with-param name="lab_view">
				<xsl:value-of select="$lab_view"/>
			</xsl:with-param>
		</xsl:apply-templates>
		<xsl:call-template name="wb_ui_line"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td align="center">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_g_form_btn_close"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">Javascript:window.close()</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="module_submission/submission_list">
		<xsl:param name="lab_learner"/>
		<xsl:param name="lab_group"/>
		<xsl:param name="lab_submission_date"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_g_lst_btn_edit_sub"/>
		<xsl:param name="lab_no_record"/>
		<xsl:param name="lab_status_not_attempted"/>
		<xsl:param name="lab_status_completed"/>
		<xsl:param name="lab_view"/>
		<xsl:choose>
			<xsl:when test="count(submission) = 0">
				<xsl:call-template name="show_no_record">
					<xsl:with-param name="lab_no_record">
						<xsl:value-of select="$lab_no_record"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr class="SecBg">
						<td width="10">
							<IMG src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="$sort_col = 'usr_display_bil' ">
									<a href="javascript:mod.sort_subn_list('usr_display_bil','{$sort_order}')" class="TitleText">
										<xsl:value-of select="$lab_learner"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path">
												<xsl:value-of select="$wb_img_path"/>
											</xsl:with-param>
											<xsl:with-param name="sort_order">
												<xsl:value-of select="$cur_order"/>
											</xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:mod.sort_subn_list('usr_display_bil','asc')" class="TitleText">
										<xsl:value-of select="$lab_learner"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td>
							<span class="TitleText">
								<xsl:value-of select="$lab_group"/>
							</span>
						</td>
						<td>
							<span class="TitleText">
								<xsl:value-of select="$lab_submission_date"/>
							</span>
						</td>
						<td>
							<span class="TitleText">
								<xsl:value-of select="$lab_status"/>
							</span>
						</td>
						<td>
							<IMG src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/>
						</td>
					</tr>
					<xsl:for-each select="submission">
						<xsl:variable name="row_class">
							<xsl:choose>
								<xsl:when test="position() mod 2">RowsEven</xsl:when>
								<xsl:otherwise>RowsOdd</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<tr class="{$row_class}">
							<td width="10">
								<img src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/>
							</td>
							<!--						
						<td>
							<input type="checkbox" value="{user/@ent_id}" name="usr_ent_id" id="{user/@usr_ent_id}"/>
						</td>
	-->
							<td>
								<span class="RowText">
									<xsl:value-of select="user/name/@display_name"/>
								</span>
							</td>
							<td>
								<span class="RowText">
									<xsl:for-each select="user/user_attribute_list/attribute_list/entity[@type = 'USG']">
										<xsl:value-of select="@display_bil"/>
										<xsl:if test="position() != last()">,</xsl:if>
									</xsl:for-each>
								</span>
							</td>
							<td>
								<span class="RowText">
									<xsl:choose>
										<xsl:when test="@submission_timestamp != ''">
											<xsl:call-template name="trun_date">
												<xsl:with-param name="my_timestamp">
													<xsl:value-of select="@submission_timestamp"/>
												</xsl:with-param>
											</xsl:call-template>
										&#160;
										<xsl:call-template name="display_hhmm">
												<xsl:with-param name="my_timestamp">
													<xsl:value-of select="@submission_timestamp"/>
												</xsl:with-param>
											</xsl:call-template>
										</xsl:when>
										<xsl:otherwise>--</xsl:otherwise>
									</xsl:choose>
								</span>
							</td>
							<td>
								<span class="RowText">
									<xsl:choose>
										<xsl:when test="@status = 'C'">
											<xsl:value-of select="$lab_status_completed"/>
										</xsl:when>
										<xsl:when test="@status = 'N' or @status = '' or @status = 'I'">
											<xsl:value-of select="$lab_status_not_attempted"/>
										</xsl:when>
									</xsl:choose>
								</span>
							</td>
							<td width="135">
								<span class="RowText">
									<xsl:call-template name="wb_gen_form_button">
										<xsl:with-param name="wb_gen_btn_name">
											<xsl:value-of select="$lab_view"/>
										</xsl:with-param>
										<!--<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.get_rpt_svy_res('<xsl:value-of select="$mod_id"/>','<xsl:value-of select="user/@ent_id"/>')	</xsl:with-param>-->
										<xsl:with-param name="wb_gen_btn_href">Javascript:mod.view_evn_res('<xsl:value-of select="$mod_id"/>','<xsl:value-of select="user/@ent_id"/>')	</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
									</xsl:call-template>
								</span>
							</td>
							<input type="hidden" name="timestamp_{user/@ent_id}" value="{@last_update_timestamp}"/>
						</tr>
					</xsl:for-each>
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="total" select="$total_rec"/>
					<xsl:with-param name="timestamp" select="$ts"/>
					<xsl:with-param name="width" select="760"/>
					<xsl:with-param name="cur_page_name">cur_page</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--====================================================-->
	<xsl:template name="show_no_record">
		<xsl:param name="lab_no_record"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
			<tr>
				<td height="10">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
			<tr>
				<td align="center">
					<span class="Text">
						<xsl:value-of select="$lab_no_record"/>
					</span>
				</td>
			</tr>
			<tr>
				<td height="10">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
			<tr>
				<td height="19">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!--====================================================-->
</xsl:stylesheet>
