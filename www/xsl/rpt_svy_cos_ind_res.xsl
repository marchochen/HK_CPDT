<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>

<xsl:import href="utils/wb_css.xsl"/>
<xsl:import href="utils/wb_init_lab.xsl"/>
<xsl:import href="utils/wb_win_hdr.xsl"/>
<xsl:import href="utils/wb_gen_button.xsl"/>
<xsl:import href="utils/trun_date.xsl"/>
<xsl:import href="utils/wb_pagination.xsl"/>

<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="rpt_name" select="/report/report_body/spec/title"/>
	<xsl:variable name="mod_id" select="/report/report_body/spec/data_list/data[@name='mod_id']/@value"/>
	<xsl:variable name="itm_id" select="/report/report_body/spec/data_list/data[@name='itm_id']/@value"/>
	<xsl:variable name="mov_status" select="report/report_body/module_submission/submission_list/@cur_status"/>
	<xsl:variable name="cur_page" select="report/report_body/module_submission/pagination/@cur_page"/>
	<xsl:variable name="total_rec" select="report/report_body/module_submission/pagination/@total_rec"/>
	<xsl:variable name="total_page" select="report/report_body/module_submission/pagination/@total_page"/>
	<xsl:variable name="sort_col" select="report/report_body/module_submission/pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="report/report_body/module_submission/pagination/@sort_order"/>
	<xsl:variable name="page_size" select="report/report_body/module_submission/pagination/@page_size"/>
	<xsl:variable name="tc_enabled" select="/report/meta/tc_enabled"/>
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
			<title><xsl:value-of select="$wb_wizbank"/></title>
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
				<xsl:with-param name="view">gen</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">lstview</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">srh</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">parinf</xsl:with-param>
			</xsl:call-template>
			
		</head>
		<BODY leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml" onsubmit="lrn_soln.srh_lrn_history_exec(frmXml,'{$wb_lang}');return false;">
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="download"/>
				<input type="hidden" name="rpt_type"/>
				<input type="hidden" name="rpt_type_lst"/>
				<input type="hidden" name="rpt_name"/>
				<input type="hidden" name="rsp_id"/>
				<input type="hidden" name="rte_id"/>
				<input type="hidden" name="usr_ent_id"/>
				<input type="hidden" name="tnd_id_lst"/>
				<input type="hidden" name="show_run_ind"/>
				<input type="hidden" name="spec_name"/>
				<input type="hidden" name="spec_value"/>
				<input type="hidden" name="module"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="page_size"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</BODY>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_evn_result">調查問卷結果</xsl:with-param>
			<xsl:with-param name="lab_submission_list">提交列表</xsl:with-param>
			<xsl:with-param name="lab_group">小組</xsl:with-param>
			<xsl:with-param name="lab_grade">職位等級</xsl:with-param>
			<xsl:with-param name="lab_learner">學員</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_submission_date">提交日期</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_no_record">沒有提交</xsl:with-param>
			<xsl:with-param name="lab_status_completed">已完成</xsl:with-param>
			<xsl:with-param name="lab_status_not_attempted">尚未完成</xsl:with-param>
			<xsl:with-param name="lab_g_lst_btn_view_sub">查看提交</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>		
			<xsl:with-param name="lab_g_txt_btn_export_all">匯出所有提交</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_evn_result">调查问卷结果</xsl:with-param>
			<xsl:with-param name="lab_submission_list">提交列表</xsl:with-param>
			<xsl:with-param name="lab_group">组</xsl:with-param>
			<xsl:with-param name="lab_grade">职位等级</xsl:with-param>
			<xsl:with-param name="lab_learner">学员</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_submission_date">提交日期</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_no_record">没有提交</xsl:with-param>
			<xsl:with-param name="lab_status_completed">已完成</xsl:with-param>
			<xsl:with-param name="lab_status_not_attempted">未完成</xsl:with-param>
			<xsl:with-param name="lab_g_lst_btn_view_sub">查看提交</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_export_all">导出所有提交</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_evn_result">Evaluation results</xsl:with-param>
			<xsl:with-param name="lab_submission_list">Submission list</xsl:with-param>
			<xsl:with-param name="lab_group">Group</xsl:with-param>
			<xsl:with-param name="lab_grade">Grade</xsl:with-param>
			<xsl:with-param name="lab_learner">Learner</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_submission_date">Submission date</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_no_record">No submission</xsl:with-param>
			<xsl:with-param name="lab_status_completed">Completed</xsl:with-param>
			<xsl:with-param name="lab_status_not_attempted">Incompleted</xsl:with-param>
			<xsl:with-param name="lab_g_lst_btn_view_sub">View submission</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_export_all">Export all submissions</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_evn_result"/>
		<xsl:param name="lab_submission_list"/>
		<xsl:param name="lab_group"/>
		<xsl:param name="lab_grade"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_learner"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_submission_date"/>
		<xsl:param name="lab_no_record"/>
		<xsl:param name="lab_status_completed"/>
		<xsl:param name="lab_status_not_attempted"/>
		<xsl:param name="lab_g_lst_btn_view_sub"/>
		<xsl:param name="lab_g_txt_btn_export_all"/>
		<xsl:param name="lab_g_form_btn_close"/>

		<xsl:call-template name="wb_win_hdr">
			<xsl:with-param name="navigation"><span class="wbGenNavigationTextBold"><xsl:value-of select="$lab_evn_result"/> - <xsl:value-of select="/report/report_body/item/@title"/></span></xsl:with-param>
		</xsl:call-template>

		<table cellpadding="0" cellspacing="0" border="0" width="760">
		<tr><td>
		<!--newnewnew-->
		<table cellpadding="3" cellspacing="0" border="0" width="100%">
			<tr class="wbGenButtonBarBg">
				<td height="19">
					<span class="wbGenButtonBarText"><xsl:value-of select="$lab_submission_list"/></span>
				</td>
				<td align="right">
					<xsl:choose>
						<xsl:when test="count(/report/report_body/module_submission/submission_list/submission)=0"><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></xsl:when>
						<xsl:otherwise>
<!--							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name">type_lst_btn_export</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.rslt_dl_rpt_adv('rpt_dl_lrn.xsl','<xsl:value-of select="report_body/spec/title/text()"/>')</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
							</xsl:call-template>-->
			<xsl:variable name="start_scope">
				<xsl:choose>
					<xsl:when test="/report/report_body/item/@eff_start_datetime = ''">
							<xsl:value-of select="/report/report_body/spec/data_list/data[@name='start_datetime']/@value"/>
					</xsl:when>
					<xsl:otherwise>
							<xsl:value-of select="/report/report_body/item/@eff_start_datetime"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:variable name="end_scope">
				<xsl:choose>
					<xsl:when test="/report/report_body/item/@eff_end_datetime = ''">
							<xsl:value-of select="/report/report_body/spec/data_list/data[@name='end_datetime']/@value"/>
					</xsl:when>
					<xsl:otherwise>
							<xsl:value-of select="/report/report_body/item/@eff_end_datetime"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
							
						<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_txt_btn_export_all"/></xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">Javascript:mod.dl_report_by_itm('<xsl:value-of select="$mod_id"/>', '<xsl:value-of select="$itm_id"/>', '',  '<xsl:value-of select="$start_scope"/>', '<xsl:value-of select="$end_scope"/>', '')</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
						</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</table>
		</td></tr>
		<tr><td>
	<xsl:apply-templates select="/report/report_body/module_submission/submission_list">
		<xsl:with-param name="lab_learner"><xsl:value-of select="$lab_learner"/></xsl:with-param>
		<xsl:with-param name="lab_group"><xsl:value-of select="$lab_group"/></xsl:with-param>
		<xsl:with-param name="lab_submission_date"><xsl:value-of select="$lab_submission_date"/></xsl:with-param>
		<xsl:with-param name="lab_status"><xsl:value-of select="$lab_status"/></xsl:with-param>
		<xsl:with-param name="lab_no_record"><xsl:value-of select="$lab_no_record"/></xsl:with-param>
		<xsl:with-param name="lab_status_not_attempted"><xsl:value-of select="$lab_status_not_attempted"/></xsl:with-param>
		<xsl:with-param name="lab_status_completed"><xsl:value-of select="$lab_status_completed"/></xsl:with-param>
		<xsl:with-param name="lab_g_lst_btn_view_sub"><xsl:value-of select="$lab_g_lst_btn_view_sub"/></xsl:with-param>
	</xsl:apply-templates>
		</td></tr>
		<tr><td align="center" class="wbGenButtonBarBg">
		
			<table cellpadding="3" cellspacing="0" border="0" width="100%"><tr><td align="center">
						<xsl:call-template name="wb_gen_form_button">	
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_close"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">Javascript:window.close()</xsl:with-param>
						</xsl:call-template>
			</td></tr></table>
		</td></tr>
		
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="module_submission/submission_list">
	<xsl:param name="lab_learner"/>
	<xsl:param name="lab_group"/>
	<xsl:param name="lab_submission_date"/>
	<xsl:param name="lab_status"/>
	<xsl:param name="lab_g_lst_btn_edit_sub"/>
	<xsl:param name="lab_g_lst_btn_view_sub"/>
	<xsl:param name="lab_no_record"/>
	<xsl:param name="lab_status_not_attempted"/>
	<xsl:param name="lab_status_completed"/>
	<xsl:choose>
		<xsl:when test="count(submission) = 0">
			<xsl:call-template name="show_no_record">
				<xsl:with-param name="lab_no_record">
					<xsl:value-of select="$lab_no_record"/>
				</xsl:with-param>
			</xsl:call-template>				
		</xsl:when>
		<xsl:otherwise>
			<table cellpadding="3" cellspacing="0" border="0" width="760">
				<tr class="wbRowHeadBarBg">
					<td width="10">
						<IMG src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/>
					</td>
					
<!--					<td>
						<xsl:call-template name="select_all_checkbox">
							<xsl:with-param name="chkbox_lst_cnt"><xsl:value-of select="count(submission)"/></xsl:with-param>
							<xsl:with-param name="display_icon">true</xsl:with-param>
							<xsl:with-param name="chkbox_lst_nm">usr_ent_id</xsl:with-param>
							<xsl:with-param name="frm_name">frmAction</xsl:with-param>
						</xsl:call-template>
					</td>					-->
					<td>
						<xsl:choose>
							<xsl:when test="$sort_col = 'usr_display_bil' ">
								<a href="javascript:mod.sort_subn_list('usr_display_bil','{$sort_order}')" class="wbRowHeadBarSorSLink">
									<xsl:value-of select="$lab_learner"/>
									<xsl:call-template name="wb_sortorder_cursor">
										<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
										<xsl:with-param name="sort_order"><xsl:value-of select="$cur_order"/></xsl:with-param>
									</xsl:call-template>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a href="javascript:mod.sort_subn_list('usr_display_bil','asc')" class="wbRowHeadBarSLink">
									<xsl:value-of select="$lab_learner"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
					</td>

					<td>
						<span class="wbRowHeadBarSText">
							<xsl:value-of select="$lab_group"/>
						</span>
					</td>
					
					<td>
						<span class="wbRowHeadBarSLink">
							<xsl:value-of select="$lab_submission_date"/>
						</span>
					</td>
					
					<td>
						<span class="wbRowHeadBarSLink">
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
							<xsl:when test="position() mod 2">wbRowsEven</xsl:when>
							<xsl:otherwise>wbRowsOdd</xsl:otherwise>
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
							<span class="wbRowText">
								<xsl:value-of select="user/name/@display_name"/>
							</span>
						</td>
						
						
						<td>
							<span class="wbRowText">
								<xsl:for-each select="user/user_attribute_list/attribute_list/entity[@type = 'USG']">
									<xsl:value-of select="@display_bil"/>
									<xsl:if test="position() != last()">,</xsl:if>
								</xsl:for-each>
							</span>
						</td>
						
						<td>
							<span class="wbRowText">
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
							<span class="wbRowText">
								<xsl:choose>
									<xsl:when test="@status = 'C'"><xsl:value-of select="$lab_status_completed"/></xsl:when>
									<xsl:when test="@status = 'N' or @status = '' or @status = 'I'"><xsl:value-of select="$lab_status_not_attempted"/></xsl:when>
								</xsl:choose>
							</span>
						</td>
						<td width="135">
							<span class="wbRowText">
									<xsl:call-template name="wb_gen_form_button">
											<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_lst_btn_view_sub"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.get_rpt_svy_res('<xsl:value-of select="$mod_id"/>','<xsl:value-of select="user/@ent_id"/>', '<xsl:value-of select="@tkh_id"/>')	</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
									</xsl:call-template>
							</span>
						</td>
						<input type="hidden" name="timestamp_{user/@ent_id}" value="{@last_update_timestamp}"/>
					</tr>
				</xsl:for-each>
			</table>

			<xsl:call-template name="wb_pagination">
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
	<table cellpadding="3" cellspacing="0" border="0" width="760">
		<tr class="wbRowsOdd">
			<td height="10"><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></td>
		</tr>
		<tr class="wbRowsOdd">
			<td align="center"><span class="wbRowText"><xsl:value-of select="$lab_no_record"/></span></td>
		</tr>
		<tr class="wbRowsOdd">
			<td height="10"><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></td>
		</tr>
		<tr class="wbGenButtonBarBg">
			<td height="19"><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></td>
		</tr>	
	</table>
</xsl:template>
<!--====================================================-->
<xsl:template name="wb_gen_form_button">
	<xsl:param name="frm">document.frmXml</xsl:param>
	<xsl:param name="field_name">frmSubmitBtn</xsl:param>
	<xsl:param name="wb_gen_btn_name"/>
	<xsl:param name="wb_gen_btn_href"/>
	<input onClick="{$wb_gen_btn_href}" class="wbGenSubBtnFrm" value="{$wb_gen_btn_name}" name="{$field_name}" type="button"/>		
</xsl:template>
<!--====================================================-->
</xsl:stylesheet>
