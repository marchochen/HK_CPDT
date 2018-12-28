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
	<xsl:variable name="total" select="count(report/report_body/report_list/record)"/>
	<xsl:variable name="cur_page" select="/report/report_body/pagination/@cur_page"/>
	<xsl:variable name="page_size" select="/report/report_body/pagination/@page_size"/>
	<xsl:variable name="total_rec" select="/report/report_body/report_list/report_summary/@totalRecord"/>
	<xsl:variable name="rpt_xls" select="/report/report_body/template/xsl_list/xsl[@type = 'download']"/>
	<xsl:variable name="data_list" select="/report/report_body/spec/data_list"/>
	<xsl:variable name="item_cost" select="/report/report_body/item_cost"/>
	<xsl:variable name="report_summary" select="/report/report_body/report_list/report_summary"/>
	<xsl:variable name="report_list" select="/report/report_body/report_list"/>
	<xsl:variable name="col_size">
		<xsl:choose>
			<xsl:when test="count(/report/report_body/report_list/record) > 0">
				<xsl:value-of select="count(/report/report_body/spec/data_list/data[@name = 'content_lst'])"/>
			</xsl:when>
			<xsl:otherwise>2</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_na">--</xsl:variable>
	<xsl:variable name="lab_train_end_date" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '711')"/>
	<xsl:variable name="lab_train_code" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '804')"/>
	<xsl:variable name="lab_train_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '562')"/>
	<xsl:variable name="lab_class_code" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_c_code')"/>
	<xsl:variable name="lab_class_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_c_title')"/>
	<xsl:variable name="lab_type" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_type')"/>
	<xsl:variable name="lab_budget" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '718')"/>
	<xsl:variable name="lab_actual" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '719')"/>
	<xsl:variable name="lab_exec_rate" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '720')"/>
	<xsl:variable name="lab_total_cost" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '730')"/>
	<xsl:variable name="lab_no_of_attend" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '731')"/>
	<xsl:variable name="lab_pre_cost" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '732')"/>
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
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn">學員</xsl:with-param>
			<xsl:with-param name="lab_lrn_group">用戶組</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">所有學員</xsl:with-param>
			<xsl:with-param name="lab_cos_catalog">培訓目錄</xsl:with-param>
			<xsl:with-param name="lab_all_cos">所有培訓</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">我負責的學員</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">我負責的培訓</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">報讀我負責培訓的學員</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">我負責學員報讀的培訓</xsl:with-param>
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
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn">学员</xsl:with-param>
			<xsl:with-param name="lab_lrn_group">用户组</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">所有学员</xsl:with-param>
			<xsl:with-param name="lab_cos_catalog">培训目录</xsl:with-param>
			<xsl:with-param name="lab_all_cos">所有培训</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">我负责的学员</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">我负责的培训</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">报读我负责培训的学员</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">我负责学员报读的培训</xsl:with-param>
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
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn">Learner</xsl:with-param>
			<xsl:with-param name="lab_lrn_group">Learner groups</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">All learners</xsl:with-param>
			<xsl:with-param name="lab_cos_catalog">Training catalog</xsl:with-param>
			<xsl:with-param name="lab_all_cos">All training</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">My responsible learners</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">My responsible training</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">Learners that have enrolled in my responsible training</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">Training that have been enrolled by my responsible learners</xsl:with-param>
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
		<xsl:param name="lab_lrn"/>
		<xsl:param name="lab_lrn_group"/>
		<xsl:param name="lab_by_all_user"/>
		<xsl:param name="lab_cos_catalog"/>
		<xsl:param name="lab_all_cos"/>
		<xsl:param name="lab_answer_for_lrn"/>
		<xsl:param name="lab_answer_for_course"/>
		<xsl:param name="lab_answer_for_course_lrn"/>
		<xsl:param name="lab_answer_for_lrn_course"/>
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
			<xsl:when test="count(/report/report_body/report_list/record) = 0">
				<xsl:call-template name="report_criteria">
					<xsl:with-param name="this_width" select="$wb_gen_table_width"/>
					<xsl:with-param name="lab_from" select="$lab_from"/>
					<xsl:with-param name="lab_to" select="$lab_to"/>
					<xsl:with-param name="lab_cos_catalog" select="$lab_cos_catalog"/>
					<xsl:with-param name="lab_all_cos" select="$lab_all_cos"/>
					<xsl:with-param name="lab_lrn" select="$lab_lrn"/>
					<xsl:with-param name="lab_lrn_group" select="$lab_lrn_group"/>
					<xsl:with-param name="lab_by_all_user" select="$lab_by_all_user"/>
					<xsl:with-param name="lab_answer_for_lrn" select="$lab_answer_for_lrn"/>
					<xsl:with-param name="lab_answer_for_course" select="$lab_answer_for_course"/>
					<xsl:with-param name="lab_answer_for_course_lrn" select="$lab_answer_for_course_lrn"/>
					<xsl:with-param name="lab_answer_for_lrn_course" select="$lab_answer_for_lrn_course"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_item"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr>
						<td colspan="{$col_size + 2}">
							<xsl:call-template name="report_criteria">
								<xsl:with-param name="this_width">100%</xsl:with-param>
								<xsl:with-param name="lab_from" select="$lab_from"/>
								<xsl:with-param name="lab_to" select="$lab_to"/>
								<xsl:with-param name="lab_cos_catalog" select="$lab_cos_catalog"/>
								<xsl:with-param name="lab_all_cos" select="$lab_all_cos"/>
								<xsl:with-param name="lab_lrn" select="$lab_lrn"/>
								<xsl:with-param name="lab_lrn_group" select="$lab_lrn_group"/>
								<xsl:with-param name="lab_by_all_user" select="$lab_by_all_user"/>
								<xsl:with-param name="lab_answer_for_lrn" select="$lab_answer_for_lrn"/>
								<xsl:with-param name="lab_answer_for_course" select="$lab_answer_for_course"/>
								<xsl:with-param name="lab_answer_for_course_lrn" select="$lab_answer_for_course_lrn"/>
								<xsl:with-param name="lab_answer_for_lrn_course" select="$lab_answer_for_lrn_course"/>
							</xsl:call-template>
						</td>
					</tr>
				</table>
				<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="table wzb-ui-table">
					<tr class="SecBg wzb-ui-table-head">
						<td valign="middle">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<xsl:apply-templates select="/report/report_body/spec/data_list/data[@name = 'content_lst']" mode="title"/>
						<td valign="middle">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<xsl:apply-templates select="report_body/report_list/record"/>
				</table>
				<table  cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr class="SecBg">
						<td width="60%" valign="top"  colspan="{$col_size + 2}">
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
	<!-- =============================================================== -->
	<xsl:template match="data[@name = 'content_lst']" mode="title">
		<td nowrap="nowrap">
			<xsl:if test="@value = 'itm_cost_budget' or @value = 'itm_cost_actual' or @value = 'itm_cost_exec_rate' or @value = 'no_of_training_attend' or @value = 'charge_per_head'">
				<xsl:attribute name="align">CENTER</xsl:attribute>
			</xsl:if>
			<span class="SmallText">
			<xsl:choose>
				<xsl:when test="@value = 'p_itm_code'">
					<xsl:value-of select="$lab_train_code"/>
				</xsl:when>
				<xsl:when test="@value = 'p_itm_title'">
					<xsl:value-of select="$lab_train_title"/>
				</xsl:when>
				<xsl:when test="@value = 'c_itm_code'">
					<xsl:value-of select="$lab_class_code"/>
				</xsl:when>
				<xsl:when test="@value = 'c_itm_title'">
					<xsl:value-of select="$lab_class_title"/>
				</xsl:when>
				<xsl:when test="@value = 'itm_cost_budget'">
					<xsl:value-of select="$lab_budget"/>
				</xsl:when>
				<xsl:when test="@value ='itm_cost_actual'">
					<xsl:value-of select="$lab_actual"/>
				</xsl:when>
				<xsl:when test="@value ='itm_cost_exec_rate'">
					<xsl:value-of select="$lab_exec_rate"/>(=<xsl:value-of select="$lab_actual"/>/<xsl:value-of select="$lab_budget"/>)
				</xsl:when>
				<xsl:when test="@value ='no_of_training_attend'">
					<xsl:value-of select="$lab_no_of_attend"/>
				</xsl:when>
				<xsl:when test="@value ='charge_per_head'">
					<xsl:value-of select="$lab_pre_cost"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="@value"/>
				</xsl:otherwise>
			</xsl:choose>&#160;</span>
		</td>
	</xsl:template>
	<!-- ===========================report group==================================== -->
	<xsl:template match="record">
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
			<xsl:apply-templates select="/report/report_body/spec/data_list/data[@name = 'content_lst']" mode="value">
				<xsl:with-param name="this" select="."/>
			</xsl:apply-templates>
			<td>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data[@name='content_lst']" mode="value">
		<xsl:param name="this"/>
		<td valign="top" nowrap="nowrap">
			<xsl:if test="@value = 'itm_cost_budget' or @value = 'itm_cost_actual' or @value = 'itm_cost_exec_rate' or @value = 'no_of_training_attend' or @value = 'charge_per_head'">
				<xsl:attribute name="align">RIGHT</xsl:attribute>
			</xsl:if>
			<span class="SmallText">
				<xsl:choose>
					<xsl:when test="@value = 'p_itm_code'">
						<xsl:choose>
							<xsl:when test="$this/p_itm_code != ''">
								<span class="SmallText">
									<xsl:value-of select="$this/p_itm_code"/>
								</span>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'p_itm_title'">
						<xsl:choose>
							<xsl:when test="$this/p_itm_title != ''">
								<span class="SmallText">
									<xsl:value-of select="$this/p_itm_title"/>
								</span>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'c_itm_code'">
						<xsl:choose>
							<xsl:when test="$this/c_itm_code != ''">
								<span class="SmallText">
									<xsl:value-of select="$this/c_itm_code"/>
								</span>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'c_itm_title'">
						<xsl:choose>
							<xsl:when test="$this/c_itm_title != ''">
								<span class="SmallText">
									<xsl:value-of select="$this/c_itm_title"/>
								</span>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'itm_cost_budget'">
						<xsl:choose>
							<xsl:when test="$this/budget != '' and $this/budget != 0">
								<span class="SmallText">
									<xsl:value-of select="$this/budget"/>
								</span>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'itm_cost_actual'">
						<xsl:choose>
							<xsl:when test="$this/actual != '' and $this/actual != 0">
								<span class="SmallText">
									<xsl:value-of select="$this/actual"/>
								</span>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'itm_cost_exec_rate'">
						<xsl:choose>
							<xsl:when test="$this/exce_rate != '' or $this/exce_rate != '0%'">
								<span class="SmallText">
									<xsl:value-of select="$this/exce_rate"/>
								</span>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'no_of_training_attend'">
						<xsl:choose>
							<xsl:when test="$this/app_cnt > 0">
								<span class="SmallText">
									<xsl:value-of select="$this/app_cnt"/>
								</span>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'charge_per_head'">
						<xsl:choose>
							<xsl:when test="$this/per_cost != '' and $this/per_cost != 0">
								<span class="SmallText">
									<xsl:value-of select="$this/per_cost"/>
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
			&#160;</span>
		</td>
	</xsl:template>
	<!-- =================================================================================== -->
	<xsl:template name="report_criteria">
		<xsl:param name="this_width"/>
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_cos_catalog"/>
		<xsl:param name="lab_all_cos"/>
		<xsl:param name="lab_lrn"/>
		<xsl:param name="lab_lrn_group"/>
		<xsl:param name="lab_by_all_user"/>
		<xsl:param name="lab_answer_for_lrn"/>
		<xsl:param name="lab_answer_for_course"/>
		<xsl:param name="lab_answer_for_course_lrn"/>
		<xsl:param name="lab_answer_for_lrn_course"/>
		<xsl:param name="lab_course"/>
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
			<!-- learner / group -->
			<xsl:if test="report_body/spec/data_list/data[@name='ent_id'] or report_body/spec/data_list/data[@name='s_usg_ent_id_lst'] or  report_body/spec/data_list/data[@name='all_user_ind'] or report_body/spec/data_list/data[@name='usr_ent_id'] or report_body/spec/data_list/data[@name='usg_ent_id'] ">
				<tr>
					<td align="right" valign="top" class="wzb-form-label">
						<span class="TitleText">
							<xsl:choose>
								<xsl:when test="report_body/spec/data_list/data[@name='all_user_ind']/@value = '1' or count(report_body/spec/data_list/data[@name='usr_ent_id'])  != 0 ">
									<xsl:value-of select="$lab_lrn"/>：
									</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_lrn_group"/><xsl:text>：</xsl:text></xsl:otherwise>
							</xsl:choose>
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
									<xsl:for-each select="report_body/spec/data_list/data[@name='usr_ent_id'or @name='usg_ent_id']">
										<xsl:variable name="usr_ent_id">
											<xsl:value-of select="@value"/>
										</xsl:variable>
										<xsl:variable name="usg_ent_id">
											<xsl:value-of select="@value"/>
										</xsl:variable>
										<xsl:value-of select="/report/report_body/presentation/data[ (@name='usr_ent_id' and @value=$usr_ent_id) or (@name='usg_ent_id' and @value=$usg_ent_id)]/@display"/>
										<xsl:if test="position()!=last()">, </xsl:if>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
				</tr>
			</xsl:if>
			<xsl:choose>
				<!-- course id -->
				<xsl:when test="count(report_body/spec/data_list/data[@name='itm_id']) !=0">
					<tr>
						<td align="right" valign="top"  class="wzb-form-label">
							<span class="TitleText">
								<xsl:value-of select="$lab_training"/>：</span>
						</td>
						<td  class="wzb-form-control">
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
								<xsl:value-of select="$lab_cos_catalog"/>：</span>
						</td>
						<td  class="wzb-form-control">
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
								<xsl:value-of select="$lab_training"/>：</span>
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
			<!-- date -->
			<xsl:if test="count(report_body/spec/data_list/data[@name='start_datetime']) != 0 or count(report_body/spec/data_list/data[@name='end_datetime']) != 0">
				<tr>
					<td align="right" valign="top"  class="wzb-form-label">
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
			<tr>
				<td align="right" valign="top"  class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_total_cost"/>：</span>
				</td>
				<td class="wzb-form-control">
					<span class="Text">
						<xsl:value-of select="$report_summary/total_cost"/>
					</span>
				</td>
			</tr>
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
