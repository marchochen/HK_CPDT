<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_form_select_action.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="process_status_cnt_list_root" select="/tptrainingplan/process_status_cnt_list"/>
	<xsl:variable name="train_process_status" select="/tptrainingplan/trainingplan_list/@status"/>
	<xsl:variable name="total_process_status_cnt" select="/tptrainingplan/process_status_cnt_list/total_process_status_cnt/@cnt"/>
	<xsl:variable name="cur_page" select="/tptrainingplan/pagination/@cur_page"/>
	<xsl:variable name="total" select="/tptrainingplan/pagination/@total_rec"/>
	<xsl:variable name="page_size" select="/tptrainingplan/pagination/@page_size"/>
	<xsl:variable name="tcr_name" select="/tptrainingplan/training_center/name/text()"/>
	<xsl:variable name="tcr_id" select="/tptrainingplan/training_center/@id"/>
	<xsl:variable name="tc_enabled" select="/tptrainingplan/meta/tc_enabled"/>
	<xsl:variable name="sort_by" select="/tptrainingplan/pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="/tptrainingplan/pagination/@sort_order"/>
	<xsl:variable name="order_by">
		<xsl:choose>
			<xsl:when test="$cur_order = 'ASC' ">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="tptrainingplan"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="tptrainingplan">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}wb_objective.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}wb_search.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}wb_batchprocess.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js" type="text/javascript"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_training_plan.js"/>
			<script language="Javascript" TYPE="text/javascript"><![CDATA[
			obj = new wbObjective;
			seh = new wbSearch;
			Batch = new wbBatchProcess;
			plan = new wbTrainingPlan;
						
			function select_status(frm,select_val){
				plan.get_out_plan_lst(select_val,frm.tcr_id.value)				
			}
			
			function search(frm){
				plan.simple_search_plan_exec(frm,'MAKEUP');
			}	

			function show_content(tcr_id) {
				if(tcr_id != 0) {
					document.frmXml.tcr_id.value = tcr_id;
					plan.simple_search_plan_exec(document.frmXml,'MAKEUP');
				}
			}
			
		]]></script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="">
			<form name="frmXml" onsubmit="search(this)">
				<input type="hidden" name="module"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="tcr_id" value="{$tcr_id}"/>
				<input type="hidden" name="page_size" value="10"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">編外計劃審批</xsl:with-param>
			<xsl:with-param name="lab_title_desc">以下是您下級培訓中心提交的編外培訓計劃，請根據計劃情況進行審批操作。</xsl:with-param>
			<xsl:with-param name="lab_pending">等待審批</xsl:with-param>
			<xsl:with-param name="lab_approved">審批通過</xsl:with-param>
			<xsl:with-param name="lab_declined">審批未通過</xsl:with-param>
			<xsl:with-param name="lab_check_sta">請選擇狀態</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_code">計劃編號</xsl:with-param>
			<xsl:with-param name="lab_create_timestamp">提交時間</xsl:with-param>
			<xsl:with-param name="lab_name">培訓活動名稱</xsl:with-param>
			<xsl:with-param name="lab_department">負責培訓中心</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_no_trainingplan">暫無編外計劃</xsl:with-param>
			<xsl:with-param name="lab_no_trainingplan_of">沒有屬於此狀態的編外培訓計劃</xsl:with-param>
			<xsl:with-param name="lab_btn_approved">批准</xsl:with-param>
			<xsl:with-param name="lab_btn_declined">拒絕</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">编外计划审批</xsl:with-param>
			<xsl:with-param name="lab_title_desc">以下是您下级培训中心提交的编外培训计划，请根据计划情况进行审批操作。</xsl:with-param>
			<xsl:with-param name="lab_pending">等待审批</xsl:with-param>
			<xsl:with-param name="lab_approved">审批通过</xsl:with-param>
			<xsl:with-param name="lab_declined">审批未通过</xsl:with-param>
			<xsl:with-param name="lab_check_sta">请选择状态</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_code">计划编号</xsl:with-param>
			<xsl:with-param name="lab_create_timestamp">提交时间</xsl:with-param>
			<xsl:with-param name="lab_name">培训活动名称</xsl:with-param>
			<xsl:with-param name="lab_department">负责培训中心</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_no_trainingplan">暂无编外计划</xsl:with-param>
			<xsl:with-param name="lab_no_trainingplan_of">没有属于此状态的编外培训计划</xsl:with-param>
			<xsl:with-param name="lab_btn_approved">批准</xsl:with-param>
			<xsl:with-param name="lab_btn_declined">拒绝</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">Additional training plan approval</xsl:with-param>
			<xsl:with-param name="lab_title_desc">Listed below are the additional training plan submitted by your subordinate training center. Please approve or decline.</xsl:with-param>
			<xsl:with-param name="lab_pending">Pending approval</xsl:with-param>
			<xsl:with-param name="lab_approved">Approved</xsl:with-param>
			<xsl:with-param name="lab_declined">Declined</xsl:with-param>
			<xsl:with-param name="lab_check_sta">Please select status</xsl:with-param>
			<xsl:with-param name="lab_all">All</xsl:with-param>
			<xsl:with-param name="lab_code">Code</xsl:with-param>
			<xsl:with-param name="lab_create_timestamp">Submission date</xsl:with-param>
			<xsl:with-param name="lab_name">Training title</xsl:with-param>
			<xsl:with-param name="lab_department">Training center</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_no_trainingplan">No additional training plans are currently available</xsl:with-param>
			<xsl:with-param name="lab_no_trainingplan_of">No additional training Plans matched with the status </xsl:with-param>
			<xsl:with-param name="lab_btn_approved">Approve</xsl:with-param>
			<xsl:with-param name="lab_btn_declined">Decline</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_title_desc"/>
		<xsl:param name="lab_approved"/>
		<xsl:param name="lab_declined"/>
		<xsl:param name="lab_check_sta"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_pending"/>
		<xsl:param name="lab_code"/>
		<xsl:param name="lab_name"/>
		<xsl:param name="lab_department"/>
		<xsl:param name="lab_create_timestamp"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_no_trainingplan"/>
		<xsl:param name="lab_no_trainingplan_of"/>
		<xsl:param name="lab_btn_approved"/>
		<xsl:param name="lab_btn_declined"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_MAKEUP_PLAN_APPR</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_title"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_title_desc"/>
			</xsl:with-param>
		</xsl:call-template>
		
		<xsl:call-template name="search_head">
			<xsl:with-param name="lab_approved" select="$lab_approved"/>
			<xsl:with-param name="lab_declined" select="$lab_declined"/>
			<xsl:with-param name="lab_check_sta" select="$lab_check_sta"/>
			<xsl:with-param name="lab_all" select="$lab_all"/>
			<xsl:with-param name="lab_pending" select="$lab_pending"/>
			<xsl:with-param name="lab_btn_approved" select="$lab_btn_approved"/>
			<xsl:with-param name="lab_btn_declined" select="$lab_btn_declined"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_space"/>
		<xsl:apply-templates select="trainingplan_list">
			<xsl:with-param name="lab_code" select="$lab_code"/>
			<xsl:with-param name="lab_name" select="$lab_name"/>
			<xsl:with-param name="lab_department" select="$lab_department"/>
			<xsl:with-param name="lab_status" select="$lab_status"/>
			<xsl:with-param name="lab_no_trainingplan" select="$lab_no_trainingplan"/>
			<xsl:with-param name="lab_no_trainingplan_of" select="$lab_no_trainingplan_of"/>
			<xsl:with-param name="lab_approved" select="$lab_approved"/>
			<xsl:with-param name="lab_declined" select="$lab_declined"/>
			<xsl:with-param name="lab_pending" select="$lab_pending"/>
			<xsl:with-param name="lab_create_timestamp" select="$lab_create_timestamp"/>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="search_head">
		<xsl:param name="lab_approved"/>
		<xsl:param name="lab_declined"/>
		<xsl:param name="lab_check_sta"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_pending"/>
		<xsl:param name="lab_btn_approved"/>
		<xsl:param name="lab_btn_declined"/>
		<table>
			<tr>
				<td align="left">
					<table>
						<tr height="30">
							<!--  
							<td width="8%" align="left">
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$lab_check_sta"/>:
							</td>
							-->
							<td align="left">
								<xsl:variable name="class">
									<xsl:if test="$train_process_status = ''">active</xsl:if>
								</xsl:variable>
								<div role="tabpanel">
               						<ul class="nav nav-tabs page-tabs" >
               							<li class="{$class}">
                   							<a href="javaScript:select_status(document.frmXml,'ALL')" >	
										<xsl:value-of select="$lab_all"/>
										<xsl:text> (</xsl:text>
										<xsl:value-of select="$total_process_status_cnt"/>
										<xsl:text>)</xsl:text>
											</a>
										</li>
									<xsl:for-each select="process/status">
											<xsl:variable name="class">
												<xsl:if test="$train_process_status = @id">active</xsl:if>
											</xsl:variable>
											<li class="{$class}">
                   							<a href="javaScript:select_status(document.frmXml,'{@id}')" >
                   							<xsl:choose>	
											<xsl:when test="@id = 'PENDING'">
												<xsl:value-of select="$lab_pending"/>
											</xsl:when>
											<xsl:when test="@id = 'APPROVED'">
												<xsl:value-of select="$lab_approved"/>
											</xsl:when>
											<xsl:when test="@id = 'DECLINED'">
												<xsl:value-of select="$lab_declined"/>
											</xsl:when>
											</xsl:choose>
											<xsl:variable name="my_name" select="@id"/>
											<xsl:variable name="my_cnt" select="$process_status_cnt_list_root/process_status_cnt[@name = $my_name]/@cnt"/>
											<xsl:choose>
												<xsl:when test="$my_cnt">
													<xsl:text>&#160;(</xsl:text>
													<xsl:value-of select="$my_cnt"/>
													<xsl:text>)</xsl:text>
												</xsl:when>
												<xsl:otherwise>
													<xsl:text>&#160;(0)</xsl:text>
												</xsl:otherwise>
											</xsl:choose>
										</a></li>
									</xsl:for-each>
								</ul>
								</div>
							</td>
							<!--  
							<td width="92%" align="left">
								<select class="Select" name="status_sel_frm" onchange="select_status(document.frmXml)">
									<option value="ALL">
										<xsl:value-of select="$lab_all"/>
										<xsl:text> (</xsl:text>
										<xsl:value-of select="$total_process_status_cnt"/>
										<xsl:text>)</xsl:text>
									</option>
									<xsl:for-each select="process/status">
										<option value="{@id}">
											<xsl:if test="$train_process_status = @id">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
											<xsl:if test="@id = 'PENDING'">
												<xsl:value-of select="$lab_pending"/>
											</xsl:if>
											<xsl:if test="@id = 'APPROVED'">
												<xsl:value-of select="$lab_approved"/>
											</xsl:if>
											<xsl:if test="@id = 'DECLINED'">
												<xsl:value-of select="$lab_declined"/>
											</xsl:if>
											<xsl:variable name="my_name" select="@id"/>
											<xsl:variable name="my_cnt" select="$process_status_cnt_list_root/process_status_cnt[@name = $my_name]/@cnt"/>
											<xsl:choose>
												<xsl:when test="$my_cnt">
													<xsl:text>&#160;(</xsl:text>
													<xsl:value-of select="$my_cnt"/>
													<xsl:text>)</xsl:text>
												</xsl:when>
												<xsl:otherwise>
													<xsl:text>&#160;(0)</xsl:text>
												</xsl:otherwise>
											</xsl:choose>
										</option>
									</xsl:for-each>
								</select>
							</td>
							-->
							<input type="hidden" name="status" value=""/>
							<xsl:if test="$train_process_status = 'PENDING'or $train_process_status = 'DECLINED'">
							<td align="right" >
								<xsl:if test="$train_process_status = 'PENDING'">
									<span>
											<xsl:call-template name="wb_gen_button">
												<xsl:with-param name="wb_gen_btn_name">
													<xsl:value-of select="$lab_btn_approved"/>
												</xsl:with-param>
												<xsl:with-param name="wb_gen_btn_href">javascript:plan.upd_multi_status_exec(document.frmXml,'A','<xsl:value-of select="$wb_lang"/>','MAKEUP')</xsl:with-param>
												<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
											</xsl:call-template>
											<xsl:call-template name="wb_gen_button">
												<xsl:with-param name="wb_gen_btn_name">
													<xsl:value-of select="$lab_btn_declined"/>
												</xsl:with-param>
												<xsl:with-param name="wb_gen_btn_href">javascript:plan.upd_multi_status_exec(document.frmXml,'D','<xsl:value-of select="$wb_lang"/>','MAKEUP')</xsl:with-param>
												<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
											</xsl:call-template>
									</span>
									<div style="height:9px;border-bottom: 1px solid #ddd;margin-bottom:-4px;"></div>
								</xsl:if>
							</td>
						</xsl:if>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="trainingplan_list">
		<xsl:param name="lab_code"/>
		<xsl:param name="lab_name"/>
		<xsl:param name="lab_department"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_approved"/>
		<xsl:param name="lab_declined"/>
		<xsl:param name="lab_pending"/>
		<xsl:param name="lab_create_timestamp"/>
		<xsl:param name="lab_no_trainingplan"/>
		<xsl:param name="lab_no_trainingplan_of"/>
		<xsl:choose>
			<xsl:when test="count(trainingplan) = 0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text">
						<xsl:choose>
							<xsl:when test="$train_process_status = ''">
								<xsl:value-of select="$lab_no_trainingplan"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_no_trainingplan_of"/>
								<xsl:text>"</xsl:text>
								<xsl:if test="$train_process_status = 'PENDING'">
									<xsl:value-of select="$lab_pending"/>
								</xsl:if>
								<xsl:if test="$train_process_status = 'APPROVED'">
									<xsl:value-of select="$lab_approved"/>
								</xsl:if>
								<xsl:if test="$train_process_status = 'DECLINED'">
									<xsl:value-of select="$lab_declined"/>
								</xsl:if>
								<xsl:text>"</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table class="table wzb-ui-table" cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr class="wzb-ui-table-head">						
						<td width="20%">
						<xsl:if test="$train_process_status = 'PENDING'">
							 
								<xsl:call-template name="select_all_checkbox">
									<xsl:with-param name="chkbox_lst_cnt">
										<xsl:value-of select="count(trainingplan)"/>
									</xsl:with-param>
									<xsl:with-param name="display_icon">false</xsl:with-param>
									<xsl:with-param name="sel_all_chkbox_nm">sel_all_checkbox</xsl:with-param>
								</xsl:call-template>
							 
						</xsl:if>
						
							<span class="TitleText">
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
							</span>
						</td>
						<td width="30%" align="left">
							<span class="TitleText">
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
							</span>
						</td>
						<xsl:if test="$tc_enabled = 'true'">
							<td width="15%" align="left">
								<span class="TitleText">
									<xsl:choose>
										<xsl:when test="$sort_by = 'tcr_title'">
											<a href="javascript:wb_utils_nav_get_urlparam('sort_by','tcr_title','order_by','{$order_by}')" class="TitleText">
												<xsl:value-of select="$lab_department"/>
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
												<xsl:value-of select="$lab_department"/>
											</a>
										</xsl:otherwise>
									</xsl:choose>
								</span>
							</td>
						</xsl:if>
						<td width="20%" align="left">
							<span class="TitleText">
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
							</span>
						</td>
						<td width="15%" align="right">
							<span class="TitleText">
								<xsl:choose>
									<xsl:when test="$sort_by = 'tpn_submit_timestamp'">
										<a href="javascript:wb_utils_nav_get_urlparam('sort_by','tpn_submit_timestamp','order_by','{$order_by}')" class="TitleText">
											<xsl:value-of select="$lab_create_timestamp"/>
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
										<a href="javascript:wb_utils_nav_get_urlparam('sort_by','tpn_submit_timestamp','order_by','ASC')" class="TitleText">
											<xsl:value-of select="$lab_create_timestamp"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
					 
					</tr>
					<xsl:for-each select="trainingplan">
						<xsl:variable name="row_class">
							<xsl:choose>
								<xsl:when test="position() mod 2">RowsEven</xsl:when>
								<xsl:otherwise>RowsOdd</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<tr class="{$row_class}">
 
							 
						
							<td width="20%">
								<xsl:if test="$train_process_status = 'PENDING'">
								 
 
									<input type="checkbox" value="{@tpn_id}~{@tpn_tcr_id}" name="planId_chkbox" id="{@tpn_update_timestamp}"/>
								 
								</xsl:if>
								<span class="Text">
									<xsl:value-of select="@tpn_code"/>
								</span>
							</td>
							<td width="30%" align="left">
								<span class="Text">
									<a href="javascript:plan.get_plan_detail('{@tpn_tcr_id}','{@tpn_id}','FTN_AMD_MAKEUP_PLAN_APPR')" class="Text color-blue108">
										<xsl:value-of select="@tpn_name"/>
									</a>
								</span>
							</td>
							<xsl:if test="$tc_enabled = 'true'">
								<td   width="20%" align="left">
									<span class="Text">
										<xsl:value-of select="@tcr_title"/>
									</span>
								</td>
							</xsl:if>
								<td  width="20%" align="left">
								<span class="Text">
									<xsl:if test="@tpn_status = 'PENDING'">
										<xsl:value-of select="$lab_pending"/>
									</xsl:if>
									<xsl:if test="@tpn_status = 'APPROVED'">
										<xsl:value-of select="$lab_approved"/>
									</xsl:if>
									<xsl:if test="@tpn_status = 'DECLINED'">
										<xsl:value-of select="$lab_declined"/>
									</xsl:if>
								</span>
							</td>
								<td   width="15%" align="right">
								<span class="Text">
									<xsl:choose>
										<xsl:when test="@tpn_submit_timestamp !=''">
											<xsl:call-template name="display_time">
												<xsl:with-param name="my_timestamp">
													<xsl:value-of select="@tpn_submit_timestamp"/>
												</xsl:with-param>
											</xsl:call-template>
										</xsl:when>
										<xsl:otherwise>
											<xsl:text>--</xsl:text>
										</xsl:otherwise>
									</xsl:choose>
								</span>
							</td>
					 
						</tr>
					</xsl:for-each>
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="total" select="$total"/>
					<xsl:with-param name="width" select="$wb_gen_table_width"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
