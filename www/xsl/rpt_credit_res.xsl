<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
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
	<xsl:import href="share/lab_credit_type.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="rpt_name" select="/report/report_body/spec/title"/>
	<xsl:variable name="total" select="report/report_body/report_list/pagination/@total_rec"/>
	<xsl:variable name="cur_page" select="/report/report_body/report_list/pagination/@cur_page"/>
	<xsl:variable name="page_size" select="/report/report_body/report_list/pagination/@page_size"/>
	<xsl:variable name="tc_enabled" select="/report/meta/tc_enabled"/>
	<xsl:variable name="rpt_xls" select="/report/report_body/template/xsl_list/xsl[@type = 'download']"/>
	<xsl:variable name="report_type" select="/report/report_body/report_list/@type"/>
	<xsl:variable name="is_detail_rpt">
		<xsl:choose>
				<xsl:when test="/report/report_body/spec/data_list/data[@name='is_detail_ind']/@value= 1">true</xsl:when>
				<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:variable name="lab_total_jifen" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '836')"/>
	<xsl:variable name="lab_train_jifen" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '829')"/>
	<xsl:variable name="lab_activity_jifen" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '828')"/>
	<xsl:variable name="lab_add_jifen" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '830')"/>
	<xsl:variable name="lab_reduce_jifen" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '831')"/>
	<xsl:variable name="lab_usr_jf_type" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '822')"/>
	<xsl:variable name="lab_usr_jf_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '823')"/>
	<xsl:variable name="lab_usr_jifen" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '563')"/>
	<xsl:variable name="lab_usr_jf_act" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '820')"/>
	<xsl:variable name="lab_usr_jf_act_type" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '821')"/>
	<xsl:variable name="lab_usr_jf_source" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '824')"/>
	<xsl:variable name="lab_usr_jf_create_timestamp" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '825')"/>
	<xsl:variable name="lab_auto" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '827')"/>
	<xsl:variable name="lab_manual" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '826')"/>
	<xsl:variable name="lab_jifen_detail_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '835')"/>
	<xsl:variable name="lab_jifen_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_rte_2_CREDIT')"/>
	<xsl:variable name="lab_include_del_usr" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '832')"/>
	<xsl:variable name="lab_is_detail" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '833')"/>
	<xsl:variable name="lab_all_usg" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '834')"/>
	
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
			<xsl:with-param name="lab_grp_code">用戶組編號</xsl:with-param>
			<xsl:with-param name="lab_grp_title">用戶組名稱</xsl:with-param>
			<xsl:with-param name="lab_no_item">沒有記錄</xsl:with-param>
			<xsl:with-param name="lab_usr_display_bil">昵稱</xsl:with-param>
			<xsl:with-param name="lab_usr_name">用戶名</xsl:with-param>
			<xsl:with-param name="lab_usr_group_name">用戶組</xsl:with-param>
			<xsl:with-param name="lab_usr_grade_name">職級</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export">導出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_unknown">未知</xsl:with-param>
			<xsl:with-param name="lab_total_lrn">學員總數</xsl:with-param>
			<xsl:with-param name="lab_not_specified">未指定</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_period">起始日期</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_to"> 至 </xsl:with-param>
			<xsl:with-param name="lab_more_record">（使用導出查看全部記錄）</xsl:with-param>
			<xsl:with-param name="lab_empty">扣分</xsl:with-param>
			<xsl:with-param name="lab_credit_empty">積分清空</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_grp_title">用户组名称</xsl:with-param>
			<xsl:with-param name="lab_no_item">没有记录</xsl:with-param>
			<xsl:with-param name="lab_usr_display_bil">昵称</xsl:with-param>
			<xsl:with-param name="lab_usr_name">用户名</xsl:with-param>
			<xsl:with-param name="lab_usr_group_name">用户组</xsl:with-param>
			<xsl:with-param name="lab_usr_grade_name">职级</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export">导出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_unknown">未知</xsl:with-param>
			<xsl:with-param name="lab_total_lrn">学员总数</xsl:with-param>
			<xsl:with-param name="lab_not_specified">未指定</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_period">起始日期</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_to"> 至 </xsl:with-param>
			<xsl:with-param name="lab_more_record">（使用导出查看全部记录）</xsl:with-param>
			<xsl:with-param name="lab_empty">扣分</xsl:with-param>
			<xsl:with-param name="lab_credit_empty">积分清空</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_grp_title">User group</xsl:with-param>
			<xsl:with-param name="lab_no_item">No record found</xsl:with-param>
			<xsl:with-param name="lab_usr_display_bil">Name</xsl:with-param>
			<xsl:with-param name="lab_usr_name">User Id</xsl:with-param>
			<xsl:with-param name="lab_usr_group_name">User group</xsl:with-param>
			<xsl:with-param name="lab_usr_grade_name">Grade</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export">Export</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_unknown">Unknown</xsl:with-param>
			<xsl:with-param name="lab_total_lrn">Total learners</xsl:with-param>
			<xsl:with-param name="lab_not_specified">Not specified</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
			<xsl:with-param name="lab_period">Start/end date</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_to"> To </xsl:with-param>
			<xsl:with-param name="lab_more_record">(View all record with export function)</xsl:with-param>
			<xsl:with-param name="lab_empty">Deduction</xsl:with-param>
			<xsl:with-param name="lab_credit_empty">Points empty</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_grp_title"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_usr_display_bil"/>
		<xsl:param name="lab_usr_name"/>
		<xsl:param name="lab_usr_group_name"/>
		<xsl:param name="lab_usr_grade_name"/>
		<xsl:param name="lab_g_form_btn_export"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_unknown"/>
		<xsl:param name="lab_total_lrn"/>
		<xsl:param name="lab_not_specified"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_period"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_more_record"/>
		<xsl:param name="lab_empty"/>
		<xsl:param name="lab_credit_empty"/>
		<xsl:variable name="report_title">
			<xsl:choose>
				<xsl:when test="$is_detail_rpt = 'true'"><xsl:value-of select="$lab_jifen_detail_title"/></xsl:when>
				<xsl:otherwise><xsl:value-of select="$lab_jifen_title"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!--报告标题-->
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$rpt_name != ''">
						<xsl:value-of select="$rpt_name"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$report_title"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<!--报告摘要-->
		<xsl:call-template name="wb_ui_line"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
			<tr>
				<td width="150" align="right" height="10">
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td>
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
			<!-- user group -->
			<tr>
				<td align="right" valign="top" class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_usr_group_name"/>：</span>
				</td>
				<td class="wzb-form-control">
					<span class="Text">
						<xsl:choose>
							<xsl:when test="count(report_body/presentation/data) > 0">
								<xsl:for-each select="report_body/presentation/data">
									<xsl:value-of select="@display"/>
									<xsl:if test="position()!=last()">, </xsl:if>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_all_usg"/>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
			</tr>
			<!-- Include del usr -->
			<tr>
				<td align="right" valign="top" class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_include_del_usr"/>：</span>
				</td>
				<td class="wzb-form-control">
					<span class="Text">
						<xsl:choose>
							<xsl:when test="report_body/spec/data_list/data[@name='include_del_usr_ind']/@value= 1">
								<xsl:value-of select="$lab_yes"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_no"/>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
			</tr>
			<!-- Is detail -->
			<tr>
				<td align="right" valign="top" class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_is_detail"/>：</span>
				</td>
				<td class="wzb-form-control">
					<span class="Text">
						<xsl:choose>
							<xsl:when test="report_body/spec/data_list/data[@name='is_detail_ind']/@value= 1">
								<xsl:value-of select="$lab_yes"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_no"/>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
			</tr>
			<!-- Period -->
			<tr>
				<td align="right" valign="top" class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_period"/>：</span>
				</td>
				<td class="wzb-form-control">
					<span class="Text">
						<xsl:choose>
							<xsl:when test="not(report_body/spec/data_list/data[@name='att_create_start_datetime'] or report_body/spec/data_list/data[@name='att_create_end_datetime'])">
								<xsl:value-of select="$lab_not_specified"/>
							</xsl:when>
							<xsl:otherwise>
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
								<xsl:value-of select="$lab_to"/>
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
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
			</tr>
			<tr>
				<td width="150" align="right" height="10">
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td>
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</table>
		<xsl:choose>
			<xsl:when test="count(/report/report_body/report_list/record) = 0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_item"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<!--按用户组分组（用户组以及其用户）-->
					<xsl:when test="$report_type = 'usg_n_usr'">
						<xsl:call-template name="wb_ui_space">
							<xsl:with-param name="height">30</xsl:with-param>
						</xsl:call-template>
						<xsl:for-each select="/report/report_body/report_list/record">
							<!-- start draw table header -->
							<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
								<tr>
									<td colspan="10">
										<xsl:apply-templates select="group" mode="usg_n_usr">
											<xsl:with-param name="this_width">100%</xsl:with-param>
											<xsl:with-param name="lab_grp_title" select="$lab_grp_title"/>
											<xsl:with-param name="lab_total_lrn" select="$lab_total_lrn"/>
										</xsl:apply-templates>
									</td>
								</tr>
								<xsl:if test="count(usr) > 0">
									<xsl:call-template name="usr_title">
										<xsl:with-param name="lab_usr_name" select="$lab_usr_name"/>
										<xsl:with-param name="lab_usr_display_bil" select="$lab_usr_display_bil"/>
										<xsl:with-param name="lab_usr_group_name" select="$lab_usr_group_name"/>
										<xsl:with-param name="lab_usr_grade_name" select="$lab_usr_grade_name"/>
									</xsl:call-template>
									<xsl:apply-templates select="usr">
										<xsl:with-param name="lab_unknown" select="$lab_unknown"/>
										<xsl:with-param name="lab_empty" select="$lab_empty"/>
										<xsl:with-param name="lab_credit_empty" select="$lab_credit_empty"/>
									</xsl:apply-templates>
									<!--usr page-->
									<tr>
										<td valign="middle" width="1">
										</td>
										<td align="right" colspan="10">
											<span class="Text">
												<xsl:value-of select="$lab_showing"/>
												<xsl:text>&#160;</xsl:text>
												<xsl:choose>
													<xsl:when test="pagination/@cur_page = 1">1</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="(pagination/@cur_page - 1)*pagination/@page_size + 1"/>
													</xsl:otherwise>
												</xsl:choose>
												<xsl:text>&#160;-&#160;</xsl:text>
												<xsl:choose>
													<xsl:when test="pagination/@total_rec &lt; (pagination/@cur_page * pagination/@page_size)">
														<xsl:value-of select="pagination/@total_rec"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="pagination/@cur_page * pagination/@page_size"/>
													</xsl:otherwise>
												</xsl:choose>
												<xsl:text>&#160;</xsl:text>
												<xsl:value-of select="$lab_page_of"/>
												<xsl:text>&#160;</xsl:text>
												<xsl:value-of select="pagination/@total_rec"/>
												<xsl:text>&#160;</xsl:text>
												<xsl:value-of select="$lab_page_piece"/>
												<xsl:text>&#160;</xsl:text>
												<xsl:value-of select="$lab_more_record"/>
											</span>
										</td>
									</tr>
								</xsl:if>
							</table>
							<xsl:call-template name="wb_ui_space">
								<xsl:with-param name="height">30</xsl:with-param>
							</xsl:call-template>
						</xsl:for-each>
						<!--group page-->
						<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
							<tr class="SecBg">
								<td>
									<span class="Text">
										<xsl:value-of select="$lab_showing"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:choose>
											<xsl:when test="pagination/@cur_page = 1">1</xsl:when>
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
										<xsl:text>&#160;</xsl:text>
									    <xsl:value-of select="$lab_more_record"/>
									</span>
								</td>
							</tr>	
						</table>
					</xsl:when>
					<!--用户组列表-->
					<xsl:when test="$report_type = 'usg'">
						<xsl:call-template name="wb_ui_space"/>
						<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="table wzb-ui-table">
							<xsl:call-template name="usg_title">
								<xsl:with-param name="this_width">100%</xsl:with-param>
								<xsl:with-param name="lab_grp_title" select="$lab_grp_title"/>
								<xsl:with-param name="lab_total_lrn" select="$lab_total_lrn"/>
							</xsl:call-template>
							<xsl:for-each select="/report/report_body/report_list/record">
								<xsl:apply-templates select="group" mode="usg"/>
							</xsl:for-each>
							<!--page-->
							
						</table>
						<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
							<tr class="SecBg">
									<td valign="middle" width="1">
									</td>
									<td colspan="7">
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
											<xsl:text>&#160;</xsl:text>
										    <xsl:value-of select="$lab_more_record"/>
										</span>
									</td>
								</tr>
						</table>
					</xsl:when>
					<!--积分明细-->
					<xsl:otherwise>
						<xsl:call-template name="wb_ui_space"/>
						<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
							<xsl:call-template name="usr_title">
								<xsl:with-param name="lab_usr_name" select="$lab_usr_name"/>
								<xsl:with-param name="lab_usr_display_bil" select="$lab_usr_display_bil"/>
								<xsl:with-param name="lab_usr_group_name" select="$lab_usr_group_name"/>
								<xsl:with-param name="lab_usr_grade_name" select="$lab_usr_grade_name"/>
							</xsl:call-template>
							<xsl:for-each select="/report/report_body/report_list/record">
								<xsl:apply-templates select="usr">
									<xsl:with-param name="lab_unknown" select="$lab_unknown"/>
									<xsl:with-param name="lab_empty" select="$lab_empty"/> 
									<xsl:with-param name="lab_credit_empty" select="$lab_credit_empty"/>								
								</xsl:apply-templates>
							</xsl:for-each>
							<!--page-->
							<tr class="SecBg">
								<td valign="middle" width="1">
								</td>
								<td colspan="15">
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
										<xsl:text>&#160;</xsl:text>
									    <xsl:value-of select="$lab_more_record"/>
									</span>
								</td>
							</tr>
						</table>
					</xsl:otherwise>
				</xsl:choose>
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
								<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.rslt_dl_rpt_adv('<xsl:value-of select="$rpt_xls"/>','<xsl:value-of select="$report_title"/>')</xsl:with-param>
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
		<!-- =================================================================================== -->
	<xsl:template name="usr_title">
		<xsl:param name="lab_usr_display_bil"/>
		<xsl:param name="lab_usr_name"/>
		<xsl:param name="lab_usr_group_name"/>
		<xsl:param name="lab_usr_grade_name"/>
		<tr class="SecBg">
			<td width="8">
				<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
			</td>
			<td>
				<span class="SmallText">
					<xsl:value-of select="$lab_usr_name"/>
				</span>
			</td>
			<td align="center">
				<span class="SmallText">
					<xsl:value-of select="$lab_usr_display_bil"/>
				</span>
			</td>
			<td align="center">
				<span class="SmallText">
					<xsl:value-of select="$lab_usr_group_name"/>
				</span>
			</td>
			<td align="center">
				<span class="SmallText">
					<xsl:value-of select="$lab_usr_grade_name"/>
				</span>
			</td>
			<xsl:choose>
				<xsl:when test="$report_type = 'usg_n_usr'">
					<td align="center">
						<span class="SmallText">
							<xsl:value-of select="$lab_train_jifen"/>
						</span>
					</td>
					<td align="center">
						<span class="SmallText">
							<xsl:value-of select="$lab_activity_jifen"/>
						</span>
					</td>
					<td align="center">
						<span class="SmallText">
							<xsl:value-of select="$lab_total_jifen"/>
						</span>
					</td>
				</xsl:when>
				<xsl:otherwise>
					<td align="center">
						<span class="SmallText">
							<xsl:value-of select="$lab_usr_jifen"/>
						</span>
					</td>
					<td align="center">
						<span class="SmallText">
							<xsl:value-of select="$lab_usr_jf_act"/>
						</span>
					</td>
					<td align="center">
						<span class="SmallText">
							<xsl:value-of select="$lab_usr_jf_act_type"/>
						</span>
					</td>
					<td align="center">
						<span class="SmallText">
							<xsl:value-of select="$lab_usr_jf_type"/>
						</span>
					</td>
					<td align="center">
						<span class="SmallText">
							<xsl:value-of select="$lab_usr_jf_name"/>
						</span>
					</td>
					<td align="center">
						<span class="SmallText">
							<xsl:value-of select="$lab_usr_jf_source"/>
						</span>
					</td>
					<td align="center">
						<span class="SmallText">
							<xsl:value-of select="$lab_usr_jf_create_timestamp"/>
						</span>
					</td>
				</xsl:otherwise>
			</xsl:choose>
			<td width="8">
				<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="usr">
		<xsl:param name="lab_unknown"/>
		<xsl:param name="lab_empty"/>
		<xsl:param name="lab_credit_empty"/>
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2">StatRowsEven</xsl:when>
				<xsl:otherwise>StatRowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr class="{$row_class}">
			<td width="8">
				<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
			</td>
			<td>
				<span class="Text">
					<xsl:value-of select="@usr_ste_usr_id"/>
				</span>
			</td>
			<td align="center">
				<span class="Text">
					<xsl:value-of select="@displaybil"/>
				</span>
			</td>
			<td align="center">
				<span class="Text">
					<xsl:value-of select="@group"/>
				</span>
			</td>
			<td align="center">
				<span class="Text">
					<xsl:value-of select="@grade"/>
				</span>
			</td>
			<xsl:choose>
				<xsl:when test="$report_type = 'usg_n_usr'">
					<td align="center">
						<span class="Text">
							<xsl:value-of select="@train_jifen"/>
						</span>
					</td>
					<td align="center">
						<span class="Text">
							<xsl:value-of select="@activity_jifen"/>
						</span>
					</td>
					<td align="center">
						<span class="Text">
							<xsl:value-of select="@total_jifen"/>
						</span>
					</td>
				</xsl:when>
				<xsl:otherwise>
					<!--积分-->
					<td align="center">
						<span class="Text">
							<xsl:choose>
								<xsl:when test="@jf_name ='INTEGRAL_EMPTY' and @act_ind=2"> <xsl:value-of select="@jifen"/></xsl:when>
								<xsl:otherwise> <xsl:value-of select="@jifen"/></xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
					<!--积分操作类型-->
					<td align="center">
						<span class="Text">
							<xsl:choose>
								<xsl:when test="@act_ind = '1'"><xsl:value-of select="$lab_reduce_jifen"/></xsl:when>
								<xsl:when test="@act_ind = '2'"><xsl:value-of select="$lab_empty"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="$lab_add_jifen"/></xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
					<!--手动/自动-->
					<td align="center">
						<span class="Text">
							<xsl:choose>
								<xsl:when test="@jf_manual = 'true'"><xsl:value-of select="$lab_manual"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="$lab_auto"/></xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
					<!--积分类型-->
					<td align="center">
						<span class="Text">
							<xsl:choose>
								<xsl:when test="@jifen_type = 'A'"><xsl:value-of select="$lab_activity_jifen"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="$lab_train_jifen"/></xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
					<!--积分名称-->
					<td align="center">
						<span class="Text">
							<xsl:choose>
								<xsl:when test="(@jf_name ='INTEGRAL_EMPTY' and @act_ind=2) or @jf_name ='ITM_INTEGRAL_EMPTY'">
									<xsl:value-of select="$lab_credit_empty"/>
								</xsl:when>
								 
								<xsl:when test="@jf_manual = 'true' and @jf_name != 'ITM_IMPORT_CREDIT' and @jf_name != 'SYS_ANWSER_BOUNTY'">
									<xsl:value-of select="@jf_name"/>
								</xsl:when>
								
								
								<xsl:otherwise>
								
									<xsl:call-template name="get_cty_name">
										<xsl:with-param name="cty_code"><xsl:value-of select="@jf_name"/></xsl:with-param>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
					<!--积分来源-->
					<td align="center">
						<span class="Text">
							<xsl:choose>
							<xsl:when test="@jf_source != ''"><xsl:value-of select="@jf_source" /></xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
						</span>
					</td>
					<!--积分时间-->
					<td align="center">
						<span class="Text">
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp"><xsl:value-of select="@jf_create_timestamp"/></xsl:with-param>
								<xsl:with-param name="dis_time">T</xsl:with-param>
							</xsl:call-template>
						</span>
					</td>
				</xsl:otherwise>
			</xsl:choose>
			<td width="8">
				<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
			</td>
		</tr>
	</xsl:template>
	<!-- =================================================================================== -->
	<xsl:template match="group" mode="usg">
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
			<td>
				<span class="Text">
					<xsl:value-of select="@name"/>
				</span>
			</td>
			<td>
				<span class="Text">
					<xsl:value-of select="@count"/>
				</span>
			</td>
			<td align="center">
				<span class="Text">
					<xsl:value-of select="@train_jifen"/>
				</span>
			</td>
			<td align="center">
				<span class="Text">
					<xsl:value-of select="@activity_jifen"/>
				</span>
			</td>
			<td align="center">
				<span class="Text">
					<xsl:value-of select="@total_jifen"/>
				</span>
			</td>
			<td>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
		</tr>
	</xsl:template>
	<!-- =================================================================================== -->
	<xsl:template match="group" mode="usg_n_usr">
		<xsl:param name="this_width"/>
		<xsl:param name="lab_grp_title"/>
		<xsl:param name="lab_total_lrn"/>
		<span class="TitleText">
			<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="text">
					<xsl:value-of select="@name"/>
				</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_line">
				<xsl:with-param name="width">100%</xsl:with-param>
			</xsl:call-template>
		</span>
		<xsl:call-template name="wb_ui_space">
			<xsl:with-param name="height">5</xsl:with-param>
		</xsl:call-template>
		<span class="TitleText">
			<xsl:value-of select="$lab_total_lrn"/>： 
			<span class="StatDataText">
				<xsl:value-of select="@count"/>
			</span>
			<xsl:text>,&#160;</xsl:text>
			<xsl:value-of select="$lab_train_jifen"/>：
			<span class="StatDataText">
				<xsl:value-of select="@train_jifen"/>
			</span>
			<xsl:text>,&#160;</xsl:text>
			<xsl:value-of select="$lab_activity_jifen"/>:
			<span class="StatDataText">
				<xsl:value-of select="@activity_jifen"/>
			</span>
			<xsl:text>,&#160;</xsl:text>
			<xsl:value-of select="$lab_total_jifen"/> ： 
			<span class="StatDataText">
				<xsl:value-of select="@total_jifen"/>
			</span>
		</span><br></br>
	</xsl:template>
	<!-- =================================================================================== -->
	<xsl:template name="usg_title">
		<xsl:param name="this_width"/>
		<xsl:param name="lab_grp_title"/>
		<xsl:param name="lab_total_lrn"/>
		<tr class="SecBg  wzb-ui-table-head">
			<td width="8">
				<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
			</td>
			<td>
				<span class="SmallText">
					<xsl:value-of select="$lab_grp_title"/>
				</span>
			</td>
			<td>
				<span class="SmallText">
					<xsl:value-of select="$lab_total_lrn"/>
				</span>
			</td>
			<td align="center">
				<span class="SmallText">
					<xsl:value-of select="$lab_train_jifen"/>
				</span>
			</td>
			<td align="center">
				<span class="SmallText">
					<xsl:value-of select="$lab_activity_jifen"/>
				</span>
			</td>
			<td align="center">
				<span class="SmallText">
					<xsl:value-of select="$lab_total_jifen"/>
				</span>
			</td>
			<td width="8">
				<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
