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
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:output indent="yes"/>
	<!-- ====================================================================================================== -->
	<xsl:variable name="tc_enabled" select="/tptrainingplan/meta/tc_enabled"/>
	<xsl:variable name="sort_col" select="/tptrainingplan/pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="/tptrainingplan/pagination/@sort_order"/>
	<xsl:variable name="cur_page" select="/tptrainingplan/pagination/@cur_page"/>
	<xsl:variable name="total" select="/tptrainingplan/pagination/@total_rec"/>
	<xsl:variable name="page_size" select="/tptrainingplan/pagination/@page_size"/>
	<xsl:variable name="sort_order">
		<xsl:choose>
			<xsl:when test="$cur_order = 'ASC' or $cur_order='asc' ">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- ====================================================================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="tptrainingplan"/>
		</html>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template match="tptrainingplan">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_home.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_training_plan.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			plan = new wbTrainingPlan;
			
			function show_content(tcr_id) {
				if(tcr_id > 0) {
					plan.get_year_plan(tcr_id);
				}
			}
			
			]]></script>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="">
			<form name="frmXml">
				<input type="hidden" name="module"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="url_success"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_year_plan_title">年度計劃</xsl:with-param>
			<xsl:with-param name="lab_year_plan_desc">以下列出了當前培訓中心的年度計劃。您可以通過下載查看先前添加的年度計劃。如您的上級設定了年度計劃提交日期，在提交截止時間前，您可以添加指定年度的年度計劃，也可以替換您添加的年度計劃（如果計劃未提交給上級審批）。一旦過了截止時間，您將無法添加或更改年度計劃。</xsl:with-param>
			<xsl:with-param name="lab_plan_year">年度</xsl:with-param>
			<xsl:with-param name="lab_beging_time">提交開始時間</xsl:with-param>
			<xsl:with-param name="lab_end_time">提交截止時間</xsl:with-param>
			<xsl:with-param name="lab_plan_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_declined">審批未通過</xsl:with-param>
			<xsl:with-param name="lab_approved">審批通過</xsl:with-param>
			<xsl:with-param name="lab_pending">等待審批</xsl:with-param>
			<xsl:with-param name="lab_preparing">準備中</xsl:with-param>
			<xsl:with-param name="wb_ui_show_no_item">暫無年度計劃</xsl:with-param>
			<xsl:with-param name="lab_btn_plan_submit">提交</xsl:with-param>
			<xsl:with-param name="lab_btn_plan_replace">更換</xsl:with-param>
			<xsl:with-param name="lab_btn_plan_delete">刪除</xsl:with-param>
			<xsl:with-param name="lab_btn_plan_download">下載</xsl:with-param>
			<xsl:with-param name="lab_btn_plan_add">添加</xsl:with-param>
			<xsl:with-param name="lab_tcr_title">負責培訓中心</xsl:with-param>
			<xsl:with-param name="lab_btn_sup_plan_add">添加</xsl:with-param>
			<xsl:with-param name="lab_sup_tc_part1">為</xsl:with-param>
			<xsl:with-param name="lab_sup_tc_part2">添加</xsl:with-param>
			<xsl:with-param name="lab_sup_tc_part3">年&#160;&#160;&#160;&#160;年度計劃</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_year_plan_title">年度计划</xsl:with-param>
			<xsl:with-param name="lab_year_plan_desc">以下罗列了当前培训中心的年度计划。您可以通过下载查看先前添加的年度计划。如您的上级设定了年度计提交日期，在提交截止时间前，您可以添加指定年度的年度计划，也可以替换您添加的年度计划（如果计划未提交给上级审批）。一旦过了截止时间，您将无法添加或更改年度计划。</xsl:with-param>
			<xsl:with-param name="lab_plan_year">年度</xsl:with-param>
			<xsl:with-param name="lab_beging_time">提交开始时间</xsl:with-param>
			<xsl:with-param name="lab_end_time">提交截止时间</xsl:with-param>
			<xsl:with-param name="lab_plan_status">状态</xsl:with-param>
			<xsl:with-param name="lab_declined">审批未通过</xsl:with-param>
			<xsl:with-param name="lab_approved">审批通过</xsl:with-param>
			<xsl:with-param name="lab_pending">等待审批</xsl:with-param>
			<xsl:with-param name="lab_preparing">准备中</xsl:with-param>
			<xsl:with-param name="wb_ui_show_no_item">暂无年度计划</xsl:with-param>
			<xsl:with-param name="lab_btn_plan_submit">提交</xsl:with-param>
			<xsl:with-param name="lab_btn_plan_replace">更换</xsl:with-param>
			<xsl:with-param name="lab_btn_plan_delete">删除</xsl:with-param>
			<xsl:with-param name="lab_btn_plan_download">下载</xsl:with-param>
			<xsl:with-param name="lab_btn_plan_add">添加</xsl:with-param>
			<xsl:with-param name="lab_tcr_title">负责培训中心</xsl:with-param>
			<xsl:with-param name="lab_btn_sup_plan_add">添加</xsl:with-param>
			<xsl:with-param name="lab_sup_tc_part1">为</xsl:with-param>
			<xsl:with-param name="lab_sup_tc_part2">添加</xsl:with-param>
			<xsl:with-param name="lab_sup_tc_part3">年&#160;&#160;&#160;&#160;年度计划</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_year_plan_title">Annual training plan</xsl:with-param>
			<xsl:with-param name="lab_year_plan_desc">Listed below are annual training plan requested by upper level training center. Before preparing the training plan, you might download the previous training plan for reference. You can add your training plan, and submit it before due date.</xsl:with-param>
			<xsl:with-param name="lab_plan_year">Year</xsl:with-param>
			<xsl:with-param name="lab_beging_time">Submission start date</xsl:with-param>
			<xsl:with-param name="lab_end_time">Submission due date</xsl:with-param>
			<xsl:with-param name="lab_plan_status">Status</xsl:with-param>
			<xsl:with-param name="lab_declined">Declined</xsl:with-param>
			<xsl:with-param name="lab_approved">Approved</xsl:with-param>
			<xsl:with-param name="lab_pending">Pending approval</xsl:with-param>
			<xsl:with-param name="lab_preparing">Preparing</xsl:with-param>
			<xsl:with-param name="wb_ui_show_no_item">No annual training plan requested</xsl:with-param>
			<xsl:with-param name="lab_btn_plan_submit">Submit</xsl:with-param>
			<xsl:with-param name="lab_btn_plan_replace">Change</xsl:with-param>
			<xsl:with-param name="lab_btn_plan_delete">Delete</xsl:with-param>
			<xsl:with-param name="lab_btn_plan_download">Download</xsl:with-param>
			<xsl:with-param name="lab_btn_plan_add">Add</xsl:with-param>
			<xsl:with-param name="lab_tcr_title">Training center</xsl:with-param>
			<xsl:with-param name="lab_btn_sup_plan_add">Add</xsl:with-param>
			<xsl:with-param name="lab_sup_tc_part1">Add training plan for </xsl:with-param>
			<xsl:with-param name="lab_sup_tc_part2"> for year </xsl:with-param>
			<xsl:with-param name="lab_sup_tc_part3"></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_year_plan_title"/>
		<xsl:param name="lab_year_plan_desc"/>
		<xsl:param name="lab_plan_year"/>
		<xsl:param name="lab_beging_time"/>
		<xsl:param name="lab_end_time"/>
		<xsl:param name="lab_plan_status"/>
		<xsl:param name="lab_declined"/>
		<xsl:param name="lab_approved"/>
		<xsl:param name="lab_pending"/>
		<xsl:param name="lab_preparing"/>
		<xsl:param name="wb_ui_show_no_item"/>
		<xsl:param name="lab_btn_plan_submit"/>
		<xsl:param name="lab_btn_plan_replace"/>
		<xsl:param name="lab_btn_plan_delete"/>
		<xsl:param name="lab_btn_plan_download"/>
		<xsl:param name="lab_btn_plan_add"/>
		<xsl:param name="lab_tcr_title"/>
		<xsl:param name="lab_btn_sup_plan_add"/>
		<xsl:param name="lab_sup_tc_part1"/>
		<xsl:param name="lab_sup_tc_part2"/>
		<xsl:param name="lab_sup_tc_part3"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_YEAR_PALN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_year_plan_title"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_year_plan_desc"/>
		</xsl:call-template>
		<xsl:if test="count(sup_tc_year_lst/sup_tc_year) > 0">
			<xsl:call-template name="sup_tc_add">
				<xsl:with-param name="lab_btn_sup_plan_add" select="$lab_btn_sup_plan_add"/>
				<xsl:with-param name="lab_sup_tc_part1" select="$lab_sup_tc_part1"/>
				<xsl:with-param name="lab_sup_tc_part2" select="$lab_sup_tc_part2"/>
				<xsl:with-param name="lab_sup_tc_part3" select="$lab_sup_tc_part3"/>
			</xsl:call-template>
		</xsl:if>
		<xsl:choose>
			<xsl:when test="year_plan_lst/year_plan">
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td align="left" width="10%">
							<xsl:choose>
								<xsl:when test="$sort_col = 'ysg_year'">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','ysg_year','sort_order','{$sort_order}')" class="TitleText">
										<xsl:value-of select="$lab_plan_year"/>
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
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','ysg_year','sort_order','ASC')" class="TitleText">
										<xsl:value-of select="$lab_plan_year"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td align="left" width="20%">
							<xsl:choose>
								<xsl:when test="$sort_col = 'ysg_submit_start_datetime'">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','ysg_submit_start_datetime','sort_order','{$sort_order}')" class="TitleText">
										<xsl:value-of select="$lab_beging_time"/>
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
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','ysg_submit_start_datetime','sort_order','ASC')" class="TitleText">
										<xsl:value-of select="$lab_beging_time"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td align="left" width="20%">
							<xsl:choose>
								<xsl:when test="$sort_col = 'ysg_submit_end_datetime'">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','ysg_submit_end_datetime','sort_order','{$sort_order}')" class="TitleText">
										<xsl:value-of select="$lab_end_time"/>
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
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','ysg_submit_end_datetime','sort_order','ASC')" class="TitleText">
										<xsl:value-of select="$lab_end_time"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<xsl:if test="$tc_enabled = 'true'">
							<td align="left" width="20%">
								<xsl:choose>
									<xsl:when test="$sort_col = 'tcr_title'">
										<a href="javascript:wb_utils_nav_get_urlparam('sort_col','tcr_title','sort_order','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_tcr_title"/>
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
										<a href="javascript:wb_utils_nav_get_urlparam('sort_col','tcr_title','sort_order','ASC')" class="TitleText">
											<xsl:value-of select="$lab_tcr_title"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
						</xsl:if>
						<td align="left" width="30%">
							<xsl:choose>
								<xsl:when test="$sort_col = 'ypn_status'">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','ypn_status','sort_order','{$sort_order}')" class="TitleText">
										<xsl:value-of select="$lab_plan_status"/>
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
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','ypn_status','sort_order','ASC')" class="TitleText">
										<xsl:value-of select="$lab_plan_status"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
					<xsl:for-each select="year_plan_lst/year_plan">
						<xsl:variable name="row_class">
							<xsl:choose>
								<xsl:when test="position() mod 2">RowsOdd</xsl:when>
								<xsl:otherwise>RowsEven</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<tr>
							<td align="left">
								<xsl:value-of select="@year"/>
							</td>
							<td align="left">
								<xsl:choose>
									<xsl:when test="@submit_start_time != ''">
										<xsl:call-template name="display_time">
											<xsl:with-param name="my_timestamp">
												<xsl:value-of select="@submit_start_time"/>
											</xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>--</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td align="left">
								<xsl:choose>
									<xsl:when test="@submit_end_time != ''">
										<xsl:call-template name="display_time">
											<xsl:with-param name="my_timestamp">
												<xsl:value-of select="@submit_end_time"/>
											</xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>--</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<xsl:if test="$tc_enabled = 'true'">
								<td align="left">
									<xsl:value-of select="@tcr_title"/>
								</td>
							</xsl:if>
							<td align="right">
								 <span style="float:left">
									<xsl:choose>
									<xsl:when test="@status != ''">
										<xsl:choose>
											<xsl:when test="@status ='PREPARED'">
												<xsl:value-of select="$lab_preparing"/>
											</xsl:when>
											<xsl:when test="@status ='PENDING'">
												<xsl:value-of select="$lab_pending"/>
											</xsl:when>
											<xsl:when test="@status ='APPROVED'">
												<xsl:value-of select="$lab_approved"/>
											</xsl:when>
											<xsl:when test="@status ='DECLINED'">
												<xsl:value-of select="$lab_declined"/>
											</xsl:when>
											<xsl:otherwise>--</xsl:otherwise>
										</xsl:choose>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>--</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
								 </span>
							
						
								<xsl:variable name="year" select="@year"/>
								<xsl:variable name="ypn_year" select="@submit_end_time"/>
								<xsl:variable name="last_upd_timestamp" select="@update_time"/>
								<xsl:variable name="tcr_id" select="@tcr_id"/>
								<xsl:if test="@hasSubBtn = 'true'">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_plan_submit"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:plan.submit_year_plan('<xsl:value-of select="$tcr_id"/>','<xsl:value-of select="$year"/>','<xsl:value-of select="$last_upd_timestamp"/>');</xsl:with-param>
									</xsl:call-template>
								</xsl:if>
								<xsl:if test="@hasRepBtn = 'true'">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_plan_replace"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:plan.Import.prep('<xsl:value-of select="$tcr_id"/>','<xsl:value-of select="$year"/>','<xsl:value-of select="$ypn_year"/>','<xsl:value-of select="$last_upd_timestamp"/>');</xsl:with-param>
									</xsl:call-template>
								</xsl:if>
								<xsl:if test="@hasDelBtn = 'true'">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_plan_delete"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:plan.del_year_plan('<xsl:value-of select="$tcr_id"/>','<xsl:value-of select="$year"/>','<xsl:value-of select="$last_upd_timestamp"/>');</xsl:with-param>
									</xsl:call-template>
								</xsl:if>
								<xsl:if test="@hasDwlBtn = 'true'">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="class">btn wzb-btn-blue margin-right4</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_plan_download"/>
										<xsl:with-param name="wb_gen_btn_href">..<xsl:value-of select="uri"/>
										</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_target">_blank</xsl:with-param>
									</xsl:call-template>
								</xsl:if>
								<xsl:if test="@hasAddBtn = 'true'">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_plan_add"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:plan.Import.prep('<xsl:value-of select="$tcr_id"/>','<xsl:value-of select="$year"/>','<xsl:value-of select="$ypn_year"/>');</xsl:with-param>
									</xsl:call-template>
								</xsl:if>
							</td>
						</tr>
					</xsl:for-each>
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="total" select="$total"/>
					<xsl:with-param name="width" select="$wb_gen_table_width"/>
					<xsl:with-param name="cur_page_name">cur_page</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text">
						<xsl:value-of select="$wb_ui_show_no_item"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template name="sup_tc_add">
		<xsl:param name="lab_btn_sup_plan_add"/>
		<xsl:param name="lab_sup_tc_part1"/>
		<xsl:param name="lab_sup_tc_part2"/>
		<xsl:param name="lab_sup_tc_part3"/>
		<table>
			<tr>
				<td align="right">
					<table>
						<tr>
							<td align="right">
								<xsl:value-of select="$lab_sup_tc_part1"/>
								<xsl:value-of select="sup_tc_year_lst/@title"/>
								<xsl:value-of select="$lab_sup_tc_part2"/>
							</td>
							<td align="center" width="7%">
								<select name="ypn_year_sel" class="wzb-form-select">
									<xsl:for-each select="sup_tc_year_lst/sup_tc_year">
										<option value="{.}">
											<xsl:if test="position() = last()">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="."/>
										</option>
									</xsl:for-each>
								</select>
							</td>
							<td width="9%" align="left">
								<xsl:value-of select="$lab_sup_tc_part3"/>
							</td>
							<td width="5%">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_sup_plan_add"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:plan.Import.prep_sup(document.frmXml, '<xsl:value-of select="sup_tc_year_lst/@sup_tcr_id"/>');</xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- ====================================================================================================== -->
</xsl:stylesheet>
