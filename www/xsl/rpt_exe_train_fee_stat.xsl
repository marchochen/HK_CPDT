<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
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
	<xsl:variable name="total" select="count(report/report_body/report_list/report_group)"/>
	<xsl:variable name="cur_page" select="/report/report_body/pagination/@cur_page"/>
	<xsl:variable name="page_size" select="/report/report_body/pagination/@page_size"/>
	<xsl:variable name="total_rec" select="/report/report_body/report_list/report_summary/cosCnt"/>
	<xsl:variable name="rpt_xls" select="/report/report_body/template/xsl_list/xsl[@type = 'download']"/>
	<xsl:variable name="data_list" select="/report/report_body/spec/data_list"/>
	<xsl:variable name="item_cost" select="/report/report_body/item_cost"/>
	<xsl:variable name="report_summary" select="/report/report_body/report_list/report_summary"/>
	<xsl:variable name="report_list" select="/report/report_body/report_list"/>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_na">--</xsl:variable>
	<xsl:variable name="lab_tc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_tc')"/>
	<xsl:variable name="lab_train_end_date" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '711')"/>
	<xsl:variable name="lab_train_type" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'wb_imp_tp_plan_type')"/>
	<xsl:variable name="lab_train_scope" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '713')"/>
	<xsl:variable name="lab_all_train" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '64')"/>
	<xsl:variable name="lab_plan_train" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '714')"/>
	<xsl:variable name="lab_unplan_train" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '715')"/>
	<xsl:variable name="lab_train_code" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '804')"/>
	<xsl:variable name="lab_train_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '562')"/>
	<xsl:variable name="lab_class_code" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_c_code')"/>
	<xsl:variable name="lab_class_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_c_title')"/>
	<xsl:variable name="lab_type" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_type')"/>
	<xsl:variable name="lab_budget" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '718')"/>
	<xsl:variable name="lab_actual" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '719')"/>
	<xsl:variable name="lab_exec_rate" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '720')"/>
	<xsl:variable name="lab_venue" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '721')"/>
	<xsl:variable name="lab_transport" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '722')"/>
	<xsl:variable name="lab_instructor" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '723')"/>
	<xsl:variable name="lab_material" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '724')"/>
	<xsl:variable name="lab_accommodation" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '725')"/>
	<xsl:variable name="lab_meal" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '726')"/>
	<xsl:variable name="lab_others" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '727')"/>
	<xsl:variable name="lab_total" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '728')"/>
	<xsl:variable name="lab_train_total_fee" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '716')"/>
	<xsl:variable name="lab_train_fee_detail" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '717')"/>
	<xsl:variable name="lab_training" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'TRAINING')"/>
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
			
			lrn_soln = new wbLearnSolution;
			
			function sortCol(colName, order){
				wb_utils_nav_get_urlparam('sort_col',colName,'sort_order',order,'timestamp','');
			}
		
		]]></script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="new_css"/>
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
			<!-- content attribute text of others -->
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<!-- content result text -->
			<xsl:with-param name="lab_no_item">沒有記錄</xsl:with-param>
			<xsl:with-param name="lab_more_record">（使用匯出查看全部記錄）</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_export">匯出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">儲存</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<!-- content attribute text of others -->
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<!-- content result text -->
			<xsl:with-param name="lab_no_item">没有记录</xsl:with-param>
			<xsl:with-param name="lab_more_record">（使用导出查看全部记录）</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_export">导出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">保存</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_from">From</xsl:with-param>
			<xsl:with-param name="lab_to">To</xsl:with-param>
			<!-- content result text -->
			<xsl:with-param name="lab_no_item">No record found</xsl:with-param>
			<xsl:with-param name="lab_more_record">(Use export to show all records)</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_export">Export</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">Save</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ==================================content===================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_more_record"/>
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_g_form_btn_export"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_g_form_btn_save"/>
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
		<xsl:choose>
			<xsl:when test="count(/report/report_body/report_list/report_group) = 0">
				<xsl:call-template name="report_criteria">
					<xsl:with-param name="this_width" select="$wb_gen_table_width"/>
					<xsl:with-param name="lab_from" select="$lab_from"/>
					<xsl:with-param name="lab_to" select="$lab_to"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_item"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr>
						<td>
							<xsl:call-template name="report_criteria">
								<xsl:with-param name="this_width">100%</xsl:with-param>
								<xsl:with-param name="lab_from" select="$lab_from"/>
								<xsl:with-param name="lab_to" select="$lab_to"/>
							</xsl:call-template>
						</td>
					</tr>
				</table>
				<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<xsl:call-template name="summary"/>
					<xsl:call-template name="cos_detail"/>
					<tr class="SecBg">
						<td width="60%" valign="top">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							<xsl:choose>
								<xsl:when test="$total_rec = 0 ">
									<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>		
								</xsl:when>
								<xsl:otherwise>
							   		<span class="SecBg">
										<xsl:value-of select="$lab_showing"/>
										<xsl:text>&#160;1</xsl:text>
										<xsl:text>&#160;-&#160;</xsl:text>
										<xsl:value-of select="$total"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="$lab_page_of"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="$total_rec"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="$lab_page_piece"/>
										<xsl:text>&#160;</xsl:text>
									   <xsl:value-of select="$lab_training"/>
										<xsl:text>&#160;</xsl:text>
									   <xsl:value-of select="$lab_more_record"/>
									</span>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
				</table>
				<xsl:call-template name="wb_ui_space">
					<xsl:with-param name="height">15</xsl:with-param>
				</xsl:call-template>
			 </xsl:otherwise>
		</xsl:choose>
		<div class="wzb-bar">
			<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
				<tr>
					<td align="center">
						<xsl:if test="count(report_body/report_list/report_group)&gt;0">
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
	<!-- =========================== summary ==================================== -->
	<xsl:template name="summary">
		<tr>
			<td>
				<table cellpadding="0" cellspacing="0" border="0" width="100%">
					<tr>
						<td>
							<xsl:call-template name="wb_ui_head">
								<xsl:with-param name="text">
									<xsl:value-of select="$lab_train_total_fee"/>
								</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="wb_ui_line">
								<xsl:with-param name="width">100%</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="wb_ui_space">
								<xsl:with-param name="height">5</xsl:with-param>
							</xsl:call-template>
							<table cellpadding="0" cellspacing="0" border="0" width="100%"  class="table wzb-ui-table">
								<tr class="SecBg  wzb-ui-table-head">
									<td>
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
									<td>
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
									<td nowrap="nowrap">
										<span class="SmallText">
											<xsl:value-of select="$lab_budget"/>
										</span>
									</td>
									<td nowrap="nowrap">
										<span class="SmallText">
											<xsl:value-of select="$lab_actual"/>
										</span>
									</td>
									<td nowrap="nowrap">
										<span class="SmallText">
											<xsl:value-of select="$lab_exec_rate"/>(=<xsl:value-of select="$lab_actual"/>/<xsl:value-of select="$lab_budget"/>)
										</span>
									</td>
									<td>
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
								</tr>
								<xsl:for-each select="$item_cost/cost">
									<xsl:variable name="type" select="@type"/>
									<xsl:variable name="summary_cost" select="$report_summary/cost[@type = $type]"/>
									<xsl:variable name="budget" select="$summary_cost/@budget"/>
									<xsl:variable name="actual" select="$summary_cost/@actual"/>
									<xsl:variable name="exce_rate" select="$summary_cost/@exce_rate"/>
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
										<td nowrap="nowrap">
											<span class="SmallText">
												<xsl:value-of select="."/>
											</span>
										</td>
										<td nowrap="nowrap">
											<span class="SmallText">
												<xsl:choose>
													<xsl:when test="$budget != ''">
														<xsl:value-of select="$budget"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="$lab_na"/>
													</xsl:otherwise>
												</xsl:choose>
											</span>
										</td>
										<td nowrap="nowrap">
											<span class="SmallText">
												<xsl:choose>
													<xsl:when test="$actual != ''">
														<xsl:value-of select="$actual"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="$lab_na"/>
													</xsl:otherwise>
												</xsl:choose>
											</span>
										</td>
										<td nowrap="nowrap">
											<span class="SmallText">
												<xsl:choose>
													<xsl:when test="$exce_rate != ''">
														<xsl:value-of select="$exce_rate"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="$lab_na"/>
													</xsl:otherwise>
												</xsl:choose>
											</span>
										</td>
										<td>
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
									</tr>
								</xsl:for-each>
								<tr class="StatRowsEven">
									<td>
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
									<td nowrap="nowrap">
										<span class="SmallText">
											<xsl:value-of select="$lab_total"/>
										</span>
									</td>
									<td nowrap="nowrap">
										<span class="SmallText">
											<xsl:choose>
												<xsl:when test="$report_summary/total_cost/@budget != ''">
													<xsl:value-of select="$report_summary/total_cost/@budget"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$lab_na"/>
												</xsl:otherwise>
											</xsl:choose>
										</span>
									</td>
									<td nowrap="nowrap">
										<span class="SmallText">
											<xsl:choose>
												<xsl:when test="$report_summary/total_cost/@actual != ''">
													<xsl:value-of select="$report_summary/total_cost/@actual"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$lab_na"/>
												</xsl:otherwise>
											</xsl:choose>
										</span>
									</td>
									<td nowrap="nowrap">
										<span class="SmallText">
											<xsl:choose>
												<xsl:when test="$report_summary/total_cost/@exce_rate != ''">
													<xsl:value-of select="$report_summary/total_cost/@exce_rate"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$lab_na"/>
												</xsl:otherwise>
											</xsl:choose>
										</span>
									</td>
									<td>
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
								</tr>
							</table>
							<!-- <xsl:call-template name="wb_ui_line">
								<xsl:with-param name="width">100%</xsl:with-param>
							</xsl:call-template> -->
							<xsl:call-template name="wb_ui_space">
								<xsl:with-param name="height">15</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="cos_detail">
		<tr>
			<td>
				<table cellpadding="0" cellspacing="0" border="0" width="100%">
					<tr>
						<td>
							<xsl:call-template name="wb_ui_head">
								<xsl:with-param name="text">
									<xsl:value-of select="$lab_train_fee_detail"/>
								</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="wb_ui_line">
								<xsl:with-param name="width">100%</xsl:with-param>
							</xsl:call-template>
							<table cellpadding="1" cellspacing="0" border="1" width="100%">
								<tr class="SecBg">
									<td nowrap="nowrap" valign="bottom" rowspan="2">
										<span class="SmallText">
											<xsl:value-of select="$lab_train_code"/>
										</span>
									</td>
									<td nowrap="nowrap" valign="bottom" rowspan="2">
										<span class="SmallText">
											<xsl:value-of select="$lab_train_title"/>
										</span>
									</td>
									<td nowrap="nowrap" valign="bottom" rowspan="2" width="50">
										<span class="SmallText">
											<xsl:value-of select="$lab_class_code"/>
										</span>
									</td>
									<td nowrap="nowrap" valign="bottom" rowspan="2" width="50">
										<span class="SmallText">
											<xsl:value-of select="$lab_class_title"/>
										</span>
									</td>
									<td nowrap="nowrap" valign="bottom" rowspan="2" width="50">
										<span class="SmallText">
											<xsl:value-of select="$lab_type"/>
										</span>
									</td>
									<td nowrap="nowrap" valign="bottom" rowspan="2" align="center">
										<span class="SmallText">
											<xsl:value-of select="$lab_budget"/>
										</span>
									</td>
									<td nowrap="nowrap" valign="bottom" rowspan="2" align="center">
										<span class="SmallText">
											<xsl:value-of select="$lab_actual"/>
										</span>
									</td>
									<td nowrap="nowrap" valign="bottom" rowspan="2" align="center">
										<span class="SmallText">
											<xsl:value-of select="$lab_exec_rate"/>(=<xsl:value-of select="$lab_actual"/>/<xsl:value-of select="$lab_budget"/>)
										</span>
									</td>
									<td nowrap="nowrap" valign="top" colspan="{count($item_cost/cost)}">
										<span class="SmallText">
											<xsl:value-of select="$lab_budget"/>/<xsl:value-of select="$lab_actual"/>
										</span>
									</td>
								</tr>
								<tr class="SecBg">
									<xsl:for-each select="$item_cost/cost">
										<td nowrap="nowrap" valign="bottom" width="50">
											<span class="SmallText">
												<xsl:value-of select="."/>
											</span>
										</td>
									</xsl:for-each>
								</tr>
								<xsl:apply-templates select="$report_list/report_group"/>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</xsl:template>
	<!-- ===========================report group==================================== -->
	<xsl:template match="report_group">
		<xsl:variable name="cost" select="cost"/>
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2">StatRowsEven</xsl:when>
				<xsl:otherwise>StatRowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr class="{$row_class}" height="20">
			<td nowrap="nowrap">
				<span class="SmallText">
					<xsl:value-of select="p_itm_code"/>
				</span>
			</td>
			<td nowrap="nowrap">
				<span class="SmallText">
					<xsl:value-of select="p_itm_title"/>
				</span>
			</td>
			<td nowrap="nowrap">
				<span class="SmallText">
					<xsl:choose>
						<xsl:when test="c_itm_code != ''">
							<xsl:value-of select="c_itm_code"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_na"/>
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</td>
			<td nowrap="nowrap">
				<span class="SmallText">
					<xsl:choose>
						<xsl:when test="c_itm_title != ''">
							<xsl:value-of select="c_itm_title"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_na"/>
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</td>
			<td nowrap="nowrap" align="center">
				<span class="SmallText">
					<xsl:call-template name="get_ity_title">
						<xsl:with-param name="dummy_type" select="itm_dummy_type"/>
					</xsl:call-template>	
				</span>
			</td>
			<td nowrap="nowrap" align="left">
				<span class="SmallText">
					<xsl:choose>
						<xsl:when test="cost/total_cos/@budget != '' and cost/total_cos/@budget != '0'">
							<xsl:value-of select="cost/total_cos/@budget"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_na"/>
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</td>
			<td nowrap="nowrap" align="left">
				<span class="SmallText">
					<xsl:choose>
						<xsl:when test="cost/total_cos/@actual != '' and cost/total_cos/@actual != '0'">
							<xsl:value-of select="cost/total_cos/@actual"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_na"/>
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</td>
			<td nowrap="nowrap" align="left">
				<span class="SmallText">
					<xsl:choose>
						<xsl:when test="cost/total_cos/@exce_rate != '' and cost/total_cos/@exce_rate != '0%'">
							<xsl:value-of select="cost/total_cos/@exce_rate"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_na"/>
						</xsl:otherwise>
					</xsl:choose>	
				</span>
			</td>
				<xsl:for-each select="$item_cost/cost">
					<xsl:variable name="type" select="@type"/>
					<xsl:variable name="cos_data" select="$cost/cos_data[@type = $type]"/>
					<xsl:variable name="budget" select="$cos_data/@budget"/>
					<xsl:variable name="actual" select="$cos_data/@actual"/>
					<td nowrap="nowrap">
						<span class="SmallText">
							<xsl:choose>
								<xsl:when test="($budget != '' and $budget != 0) or ($actual != '' and $actual != 0)">
									<xsl:value-of select="$budget"/>/<xsl:value-of select="$actual"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_na"/>
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
				</xsl:for-each>
		</tr>
	</xsl:template>
	<!-- =================================================================================== -->
	<xsl:template name="report_criteria">
		<xsl:param name="this_width"/>
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_to"/>
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
			<!-- Training Center -->
			<xsl:if test="count(report_body/spec/data_list/data[@name='tcr_id']) != 0">
				<tr>
					<td align="right" valign="top" class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_tc"/>：</span>
					</td>
					<td class="wzb-form-control">
						<span class="Text">
							<xsl:variable name="tcr_id">
								<xsl:value-of select="report_body/spec/data_list/data[@name='tcr_id']/@value"/>
							</xsl:variable>
							<xsl:value-of select="/report/report_body/presentation/data[@name='tcr_id' and @value=$tcr_id]/@display"/>
						</span>
					</td>
				</tr>
			</xsl:if>
			<!-- date -->
			<xsl:if test="count(report_body/spec/data_list/data[@name='start_datetime']) != 0 or count(report_body/spec/data_list/data[@name='end_datetime']) != 0">
				<tr>
					<td align="right" valign="top" class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_train_end_date"/>：</span>
					</td>
					<td class="wzb-form-control">
						<span class="Text">
							<xsl:value-of select="$lab_from"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:choose>
								<xsl:when test="report_body/spec/data_list/data[@name='start_datetime']">
									<xsl:call-template name="display_time">
										<xsl:with-param name="my_timestamp" select="report_body/spec/data_list/data[@name='start_datetime']/@value"/>
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
								<xsl:when test="report_body/spec/data_list/data[@name='end_datetime']">
									<xsl:call-template name="display_time">
										<xsl:with-param name="my_timestamp" select="report_body/spec/data_list/data[@name='end_datetime']/@value"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_na"/>
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
				</tr>
			</xsl:if>
			<!-- Training Type -->
			<xsl:if test="count(report_body/spec/data_list/data[@name='itm_type']) != 0">
				<tr>
					<td align="right" valign="top" class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_train_type"/>：</span>
					</td>
					<td class="wzb-form-control">
						<span class="Text">
							<xsl:for-each select="report_body/spec/data_list/data[@name='itm_type']">
								<xsl:call-template name="get_ity_title">
									<xsl:with-param name="dummy_type" select="@value"/>
								</xsl:call-template>	
								<xsl:if test="position()!=last()">, </xsl:if>
							</xsl:for-each>
						</span>
					</td>
				</tr>
			</xsl:if>
			<!-- Training Scope -->
			<xsl:if test="count(report_body/spec/data_list/data[@name='train_scope']) != 0">
				<tr>
					<td align="right" valign="top" class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_train_scope"/>：</span>
					</td>
					<td class="wzb-form-control">
						<span class="Text">
							<xsl:choose>
								<xsl:when test="$data_list/data[@name='train_scope']/@value = 'PLAN'">
									<xsl:value-of select="$lab_plan_train"/>
								</xsl:when>
								<xsl:when test="UNPLAN">
									<xsl:value-of select="$lab_unplan_train"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_all_train"/>
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
</xsl:stylesheet>
