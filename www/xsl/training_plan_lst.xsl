<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="tc_enabled" select="/tptrainingplan/meta/tc_enabled"/>
	<xsl:variable name="cur_tcr_id" select="/tptrainingplan/plan_lst/cur_specify/@tcr_id"/>
	<xsl:variable name="search_year" select="/tptrainingplan/plan_lst/cur_specify/@year"/>
	<xsl:variable name="search_type" select="/tptrainingplan/plan_lst/cur_specify/@plan_type"/>
	<xsl:variable name="search_month" select="/tptrainingplan/plan_lst/cur_specify/@month"/>
	<xsl:variable name="search_status" select="/tptrainingplan/plan_lst/cur_specify/@plan_status"/>
	<xsl:variable name="is_filter" select="/tptrainingplan/plan_lst/cur_specify/is_filter/text()"/>
	<!-- pagination -->
	<xsl:variable name="cur_page" select="/tptrainingplan/pagination/@cur_page"/>
	<xsl:variable name="total" select="/tptrainingplan/pagination/@total_rec"/>
	<xsl:variable name="page_size" select="/tptrainingplan/pagination/@page_size"/>
	<!-- sorting variable -->
	<xsl:variable name="sort_by" select="/tptrainingplan/pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="/tptrainingplan/pagination/@sort_order"/>
	<xsl:variable name="order_by">
		<xsl:choose>
			<xsl:when test="$cur_order = 'ASC' ">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/tptrainingplan">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_training_plan.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript"><![CDATA[	
			plan = new wbTrainingPlan
			//set other info for the same view (condition selection)
			function filter(frmSearch){
				var tpn_status = frmSearch.tpn_status.options[frmSearch.tpn_status.selectedIndex].value;
				var tpn_status_lst =tpn_status;
				if(tpn_status =='' || tpn_status ==null || tpn_status =='all' || tpn_status =='ALL') {
					tpn_status_lst = '';
				}

				var tpn_tcr_id = frmSearch.tpn_tcr_id.value;
				var year = frmSearch.search_year.options[frmSearch.search_year.selectedIndex].value;
				var month = frmSearch.search_month.options[frmSearch.search_month.selectedIndex].value;
				var tpn_type = frmSearch.search_type.options[frmSearch.search_type.selectedIndex].value;
				plan.get_plan_by_code(frmSearch,tpn_tcr_id, year, month, tpn_status, tpn_type, tpn_status_lst, false);
			}
			function cancel_filter(frmSearch){
				var tpn_tcr_id = frmSearch.tpn_tcr_id.value;
				plan.get_plan_by_code(frmSearch,tpn_tcr_id, 0, 0, 'all', 'all', 'APPROVED~IMPLEMENTED', false);
			}
		
			function show_content(tcr_id) {
				if(tcr_id > 0) {
					plan.get_plan_lst(tcr_id);
				}
			}
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="">
			<form name="frmSearch" onsubmit="return false;">
				<input type="hidden" name="tpn_tcr_id" value="{$cur_tcr_id}"/>
				<input type="hidden" name="stylesheet" value="training_plan_lst.xsl"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_plan">計劃實施</xsl:with-param>
			<xsl:with-param name="lab_plan_desc">以下是您有權限管理的已經通過上級審批或已實施的培訓計劃。您可以實施"審批通過"的計劃，也可以查看自己"已實施"的計劃。</xsl:with-param>
			<xsl:with-param name="lab_plan_empty">暫無計劃</xsl:with-param>
			<xsl:with-param name="lab_search_year">年度</xsl:with-param>
			<xsl:with-param name="lab_search_month">月份</xsl:with-param>
			<xsl:with-param name="lab_search_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_search_type">類型</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_filter">篩選</xsl:with-param>
			<xsl:with-param name="lab_cancel_filter">取消篩選</xsl:with-param>
			<xsl:with-param name="lab_code">計劃編號</xsl:with-param>
			<xsl:with-param name="lab_name">計劃活動名稱</xsl:with-param>
			<xsl:with-param name="lab_type">計劃類型</xsl:with-param>
			<xsl:with-param name="lab_tcr_title">負責培訓中心</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_year_type">年度</xsl:with-param>
			<xsl:with-param name="lab_makeup_type">編外</xsl:with-param>
			<xsl:with-param name="lab_prepared">準備中</xsl:with-param>
			<xsl:with-param name="lab_pending">等待審批</xsl:with-param>
			<xsl:with-param name="lab_approved">審批通過</xsl:with-param>
			<xsl:with-param name="lab_declined">審批未通過</xsl:with-param>
			<xsl:with-param name="lab_implemented">已實施</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_plan">计划实施</xsl:with-param>
			<xsl:with-param name="lab_plan_desc">以下是您有权限管理的已经通过上级审批或已实施的培训计划。您可以实施"审批通过"的计划，也可以查看自己"已实施"的计划。</xsl:with-param>
			<xsl:with-param name="lab_plan_empty">暂无计划</xsl:with-param>
			<xsl:with-param name="lab_search_year">年度</xsl:with-param>
			<xsl:with-param name="lab_search_month">月份</xsl:with-param>
			<xsl:with-param name="lab_search_status">状态</xsl:with-param>
			<xsl:with-param name="lab_search_type">类型</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_filter">筛选</xsl:with-param>
			<xsl:with-param name="lab_cancel_filter">取消筛选</xsl:with-param>
			<xsl:with-param name="lab_code">计划编号</xsl:with-param>
			<xsl:with-param name="lab_name">计划活动名称</xsl:with-param>
			<xsl:with-param name="lab_type">计划类型</xsl:with-param>
			<xsl:with-param name="lab_tcr_title">负责培训中心</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_year_type">年度</xsl:with-param>
			<xsl:with-param name="lab_makeup_type">编外</xsl:with-param>
			<xsl:with-param name="lab_prepared">准备中</xsl:with-param>
			<xsl:with-param name="lab_pending">等待审批</xsl:with-param>
			<xsl:with-param name="lab_approved">审批通过</xsl:with-param>
			<xsl:with-param name="lab_declined">审批未通过</xsl:with-param>
			<xsl:with-param name="lab_implemented">已实施</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_plan">Training plan implementation</xsl:with-param>
			<xsl:with-param name="lab_plan_desc">Listed below are all the training plans approved by upper level training center. You can implement the "approved" training plan, or view the “implemented” training.</xsl:with-param>
			<xsl:with-param name="lab_plan_empty">No training plans are currently available</xsl:with-param>
			<xsl:with-param name="lab_search_year">Year</xsl:with-param>
			<xsl:with-param name="lab_search_month">Month</xsl:with-param>
			<xsl:with-param name="lab_search_status">Status</xsl:with-param>
			<xsl:with-param name="lab_search_type">Type</xsl:with-param>
			<xsl:with-param name="lab_all">All</xsl:with-param>
			<xsl:with-param name="lab_filter">Filter</xsl:with-param>
			<xsl:with-param name="lab_cancel_filter">Show all</xsl:with-param>
			<xsl:with-param name="lab_code">Code</xsl:with-param>
			<xsl:with-param name="lab_name">Training title</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_tcr_title">Training center</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_year_type">Annual</xsl:with-param>
			<xsl:with-param name="lab_makeup_type">Additional</xsl:with-param>
			<xsl:with-param name="lab_prepared">Preparing</xsl:with-param>
			<xsl:with-param name="lab_pending">Pending approval</xsl:with-param>
			<xsl:with-param name="lab_approved">Approved</xsl:with-param>
			<xsl:with-param name="lab_declined">Declined</xsl:with-param>
			<xsl:with-param name="lab_implemented">Implemented</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="content">
		<xsl:param name="lab_all_plan"/>
		<xsl:param name="lab_plan_desc"/>
		<xsl:param name="lab_plan_empty"/>
		<xsl:param name="lab_search_year"/>
		<xsl:param name="lab_search_month"/>
		<xsl:param name="lab_search_status"/>
		<xsl:param name="lab_search_type"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_filter"/>
		<xsl:param name="lab_cancel_filter"/>
		<xsl:param name="lab_code"/>
		<xsl:param name="lab_name"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_tcr_title"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_year_type"/>
		<xsl:param name="lab_makeup_type"/>
		<xsl:param name="lab_prepared"/>
		<xsl:param name="lab_pending"/>
		<xsl:param name="lab_approved"/>
		<xsl:param name="lab_declined"/>
		<xsl:param name="lab_implemented"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_PLAN_CARRY_OUT</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_all_plan"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_plan_desc"/>
		</xsl:call-template>
		<xsl:call-template name="search_bar">
			<xsl:with-param name="lab_search_year" select="$lab_search_year"/>
			<xsl:with-param name="lab_search_month" select="$lab_search_month"/>
			<xsl:with-param name="lab_search_status" select="$lab_search_status"/>
			<xsl:with-param name="lab_search_type" select="$lab_search_type"/>
			<xsl:with-param name="lab_year_type" select="$lab_year_type"/>
			<xsl:with-param name="lab_makeup_type" select="$lab_makeup_type"/>
			<xsl:with-param name="lab_prepared" select="$lab_prepared"/>
			<xsl:with-param name="lab_pending" select="$lab_pending"/>
			<xsl:with-param name="lab_approved" select="$lab_approved"/>
			<xsl:with-param name="lab_declined" select="$lab_declined"/>
			<xsl:with-param name="lab_implemented" select="$lab_implemented"/>
			<xsl:with-param name="lab_filter" select="$lab_filter"/>
			<xsl:with-param name="lab_cancel_filter" select="$lab_cancel_filter"/>
			<xsl:with-param name="lab_all" select="$lab_all"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"></xsl:call-template>
		<xsl:call-template name="plan_lst">
			<xsl:with-param name="lab_plan_empty" select="$lab_plan_empty"/>
			<xsl:with-param name="lab_code" select="$lab_code"/>
			<xsl:with-param name="lab_name" select="$lab_name"/>
			<xsl:with-param name="lab_type" select="$lab_type"/>
			<xsl:with-param name="lab_year_type" select="$lab_year_type"/>
			<xsl:with-param name="lab_makeup_type" select="$lab_makeup_type"/>
			<xsl:with-param name="lab_status" select="$lab_status"/>
			<xsl:with-param name="lab_prepared" select="$lab_prepared"/>
			<xsl:with-param name="lab_pending" select="$lab_pending"/>
			<xsl:with-param name="lab_approved" select="$lab_approved"/>
			<xsl:with-param name="lab_declined" select="$lab_declined"/>
			<xsl:with-param name="lab_implemented" select="$lab_implemented"/>
			<xsl:with-param name="lab_tcr_title" select="$lab_tcr_title"/>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="plan_lst">
		<xsl:param name="lab_plan_empty"/>
		<xsl:param name="lab_code"/>
		<xsl:param name="lab_name"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_year_type"/>
		<xsl:param name="lab_makeup_type"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_prepared"/>
		<xsl:param name="lab_pending"/>
		<xsl:param name="lab_approved"/>
		<xsl:param name="lab_declined"/>
		<xsl:param name="lab_implemented"/>
		<xsl:param name="lab_tcr_title"/>
		<xsl:choose>
			<xsl:when test="count(plan_lst/plan) = 0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_plan_empty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td width="20%" align="left">
							<xsl:choose>
								<xsl:when test="$sort_by = 'tpn_code'">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_by','tpn_code','order_by','{$order_by}')" class="TitleText">
										<xsl:value-of select="$lab_code"/>
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
									<a href="javascript:wb_utils_nav_get_urlparam('sort_by','tpn_code','order_by','ASC')" class="TitleText">
										<xsl:value-of select="$lab_code"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td width="20%" align="left">
							<xsl:choose>
								<xsl:when test="$sort_by = 'tpn_name'">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_by','tpn_name','order_by','{$order_by}')" class="TitleText">
										<xsl:value-of select="$lab_name"/>
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
									<a href="javascript:wb_utils_nav_get_urlparam('sort_by','tpn_name','order_by','ASC')" class="TitleText">
										<xsl:value-of select="$lab_name"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td width="20%" align="left">
							<xsl:choose>
								<xsl:when test="$sort_by = 'tpn_type'">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_by','tpn_type','order_by','{$order_by}')" class="TitleText">
										<xsl:value-of select="$lab_type"/>
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
									<a href="javascript:wb_utils_nav_get_urlparam('sort_by','tpn_type','order_by','ASC')" class="TitleText">
										<xsl:value-of select="$lab_type"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<xsl:if test="$tc_enabled = 'true'">
							<td width="20%" align="left">
								<xsl:choose>
									<xsl:when test="$sort_by = 'tcr_title'">
										<a href="javascript:wb_utils_nav_get_urlparam('sort_by','tcr_title','order_by','{$order_by}')" class="TitleText">
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
										<a href="javascript:wb_utils_nav_get_urlparam('sort_by','tcr_title','order_by','ASC')" class="TitleText">
											<xsl:value-of select="$lab_tcr_title"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
						</xsl:if>
						<td width="20%" align="right">
	
							<xsl:choose>
								<xsl:when test="$sort_by = 'tpn_status'">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_by','tpn_status','order_by','{$order_by}')" class="TitleText">
										<xsl:value-of select="$lab_status"/>
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
									<a href="javascript:wb_utils_nav_get_urlparam('sort_by','tpn_status','order_by','ASC')" class="TitleText">
										<xsl:value-of select="$lab_status"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
					<xsl:for-each select="plan_lst/plan">
						<xsl:variable name="row_class">
							<xsl:choose>
								<xsl:when test="position() mod 2">RowsOdd</xsl:when>
								<xsl:otherwise>RowsEven</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<tr>
							<td align="left">
								<xsl:choose>
									<xsl:when test="code/text() != ''">
										<xsl:value-of select="code/text()"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>&#160;</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td align="left">
								<xsl:choose>
									<xsl:when test="name/text() != ''">
										<a href="javascript:plan.get_plan_detail('{training_center/@tcr_id}','{@id}','FTN_AMD_PLAN_CARRY_OUT')" class="Text">
											<xsl:value-of select="name/text()"/>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>--</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td align="left">
								<xsl:choose>
									<xsl:when test="@type != ''">
										<xsl:choose>
											<xsl:when test="@type='YEAR'">
												<xsl:value-of select="$lab_year_type"/>
											</xsl:when>
											<xsl:when test="@type='MAKEUP'">
												<xsl:value-of select="$lab_makeup_type"/>
											</xsl:when>
											<xsl:otherwise>--</xsl:otherwise>
										</xsl:choose>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>--</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<xsl:if test="$tc_enabled = 'true'">
								<td align="left">
									<xsl:choose>
										<xsl:when test="training_center != ''">
											<xsl:value-of select="training_center/text()"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:text>--</xsl:text>
										</xsl:otherwise>
									</xsl:choose>
								</td>
							</xsl:if>
							<td align="right">
								<xsl:choose>
									<xsl:when test="status/text() != ''">
										<xsl:choose>
											<xsl:when test="status/text()='PREPARED'">
												<xsl:value-of select="$lab_prepared"/>
											</xsl:when>
											<xsl:when test="status/text()='PENDING'">
												<xsl:value-of select="$lab_pending"/>
											</xsl:when>
											<xsl:when test="status/text()='APPROVED'">
												<xsl:value-of select="$lab_approved"/>
											</xsl:when>
											<xsl:when test="status/text()='DECLINED'">
												<xsl:value-of select="$lab_declined"/>
											</xsl:when>
											<xsl:when test="status/text()='IMPLEMENTED'">
												<xsl:value-of select="$lab_implemented"/>
											</xsl:when>
											<xsl:otherwise>--</xsl:otherwise>
										</xsl:choose>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>--</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
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
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="search_bar">
		<xsl:param name="lab_search_year"/>
		<xsl:param name="lab_search_month"/>
		<xsl:param name="lab_search_status"/>
		<xsl:param name="lab_search_type"/>
		<xsl:param name="lab_year_type"/>
		<xsl:param name="lab_makeup_type"/>
		<xsl:param name="lab_prepared"/>
		<xsl:param name="lab_pending"/>
		<xsl:param name="lab_approved"/>
		<xsl:param name="lab_declined"/>
		<xsl:param name="lab_implemented"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_filter"/>
		<xsl:param name="lab_cancel_filter"/>
		<table>
			<tr>
				<td align="left">
					<table>
						<tr>
							<td style="padding:10px 0 10px 0px;" width="15%">
								<xsl:value-of select="$lab_search_year"/>：
								<select name="search_year" class="wzb-form-select" onchange="filter(frmSearch)">
									<option value="0">
										<xsl:if test="plan_lst/cur_specify/@year='0'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_all"/>
									</option>
									<xsl:for-each select="plan_lst/cur_specify/year_lst/year">
										<option value="{text()}">
											<xsl:if test="text() = $search_year">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="text()"/>
										</option>
									</xsl:for-each>
								</select>
							</td>
							<td style="padding:10px 0 10px 0px;" width="15%">
								<xsl:value-of select="$lab_search_month"/>：
								<select name="search_month" class="wzb-form-select" onchange="filter(frmSearch)">
									<option value="0">
										<xsl:if test="plan_lst/cur_specify/@month='0'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_all"/>
									</option>
									<xsl:for-each select="plan_lst/cur_specify/month_lst/month">
										<option value="{text()}">
											<xsl:if test="text() = $search_month">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="text()"/>
										</option>
									</xsl:for-each>
								</select>
							</td>
							<td style="padding:10px 0 10px 0px;" width="18%">
								<xsl:value-of select="$lab_search_status"/>：
								<select name="tpn_status" class="wzb-form-select" onchange="filter(frmSearch)">
									<option value="all">
										<xsl:if test="$search_status='ALL'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_all"/>
									</option>
									<!--
									<option value="PREPARED">
										<xsl:if test="$search_status='PREPARED'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_prepared"/>
									</option>
									<option value="PENDING">
										<xsl:if test="$search_status='PENDING'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_pending"/>
									</option>
									-->
									<option value="APPROVED">
										<xsl:if test="$search_status='APPROVED'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_approved"/>
									</option>
									<!--
									<option value="DECLINED">
										<xsl:if test="$search_status='DECLINED'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_declined"/>
									</option>
									-->
									<option value="IMPLEMENTED">
										<xsl:if test="$search_status='IMPLEMENTED'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_implemented"/>
									</option>
								</select>
							</td>
							<td style="padding:10px 0 10px 0px;" width="15%">
								<xsl:value-of select="$lab_search_type"/>：
								<select name="search_type" class="wzb-form-select" onchange="filter(frmSearch)">
									<option value="all">
										<xsl:if test="$search_type='ALL'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_all"/>
									</option>
									<option value="YEAR">
										<xsl:if test="$search_type='YEAR'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_year_type"/>
									</option>
									<option value="MAKEUP">
										<xsl:if test="$search_type='MAKEUP'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_makeup_type"/>
									</option>
								</select>
							</td>
							<td align="right">
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
