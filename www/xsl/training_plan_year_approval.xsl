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
	<xsl:variable name="sort_col" select="/tptrainingplan/pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="/tptrainingplan/pagination/@sort_order"/>
	<xsl:variable name="sort_order">
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
				plan.get_year_plan_lst(select_val,frm.tcr_id.value)				
			}
		]]></script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="">
			<form name="frmXml">
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
			<xsl:with-param name="lab_title">年度計劃審批</xsl:with-param>
			<xsl:with-param name="lab_title_desc">以下是你的下級培訓中心提交的年度計劃。您可以通過下載來查看您所審核的年度計劃，並進行審批操作，一旦審批通過，該年度計劃完成製作流程。</xsl:with-param>
			<xsl:with-param name="lab_approved">審批通過</xsl:with-param>
			<xsl:with-param name="lab_declined">審批未通過</xsl:with-param>
			<xsl:with-param name="lab_check_sta">請選擇狀態</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_pending">等待審批</xsl:with-param>
			<xsl:with-param name="lab_year">年度</xsl:with-param>
			<xsl:with-param name="lab_time">提交截止時間</xsl:with-param>
			<xsl:with-param name="lab_submit_timestamp">提交時間</xsl:with-param>
			<xsl:with-param name="lab_down">下載</xsl:with-param>
			<xsl:with-param name="lab_department">負責培訓中心</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_no_yearplan">暫無年度計劃</xsl:with-param>
			<xsl:with-param name="lab_no_yearplan_of">沒有屬於此狀態的年度計劃</xsl:with-param>
			<xsl:with-param name="lab_btn_approved">批准</xsl:with-param>
			<xsl:with-param name="lab_btn_declined">拒絕</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">年度计划审批</xsl:with-param>
			<xsl:with-param name="lab_title_desc">以下是你的下级培训中心提交的年度计划。您可以通过下载来查看您所审核的年度计划，并进行审批操作，一旦审批通过，该年度计划完成制作流程。</xsl:with-param>
			<xsl:with-param name="lab_approved">审批通过</xsl:with-param>
			<xsl:with-param name="lab_declined">审批未通过</xsl:with-param>
			<xsl:with-param name="lab_check_sta">请选择状态</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_pending">等待审批</xsl:with-param>
			<xsl:with-param name="lab_year">年度</xsl:with-param>
			<xsl:with-param name="lab_time">提交截止时间</xsl:with-param>
			<xsl:with-param name="lab_submit_timestamp">提交时间</xsl:with-param>
			<xsl:with-param name="lab_down">下载</xsl:with-param>
			<xsl:with-param name="lab_department">负责培训中心</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_no_yearplan">暂无年度计划</xsl:with-param>
			<xsl:with-param name="lab_no_yearplan_of">没有属于此状态的年度计划</xsl:with-param>
			<xsl:with-param name="lab_btn_approved">批准</xsl:with-param>
			<xsl:with-param name="lab_btn_declined">拒绝</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">Annual training plan approval</xsl:with-param>
			<xsl:with-param name="lab_title_desc">Listed below are the annual training plan submitted by your subordinate training center. You can download the training plan and approve it. Once the training plan is approved, subordinate training center can implement the training.</xsl:with-param>
			<xsl:with-param name="lab_approved">Approved</xsl:with-param>
			<xsl:with-param name="lab_declined">Declined</xsl:with-param>
			<xsl:with-param name="lab_check_sta">Please select status</xsl:with-param>
			<xsl:with-param name="lab_all">All</xsl:with-param>
			<xsl:with-param name="lab_pending">Pending approval</xsl:with-param>
			<xsl:with-param name="lab_year">Year</xsl:with-param>
			<xsl:with-param name="lab_time">Submission due date</xsl:with-param>
			<xsl:with-param name="lab_submit_timestamp">Submission date</xsl:with-param>
			<xsl:with-param name="lab_down">Download</xsl:with-param>
			<xsl:with-param name="lab_department">Training center</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_no_yearplan">No annual training plans</xsl:with-param>
			<xsl:with-param name="lab_no_yearplan_of">No annual training plans matched with the status </xsl:with-param>
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
		<xsl:param name="lab_year"/>
		<xsl:param name="lab_time"/>
		<xsl:param name="lab_submit_timestamp"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_down"/>
		<xsl:param name="lab_department"/>
		<xsl:param name="lab_no_yearplan"/>
		<xsl:param name="lab_no_yearplan_of"/>
		<xsl:param name="lab_btn_approved"/>
		<xsl:param name="lab_btn_declined"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_YEAR_PLAN_APPR</xsl:with-param>
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
			<xsl:with-param name="lab_all" select="$lab_all"/>
			<xsl:with-param name="lab_pending" select="$lab_pending"/>
			<xsl:with-param name="lab_approved" select="$lab_approved"/>
			<xsl:with-param name="lab_declined" select="$lab_declined"/>
			<xsl:with-param name="lab_check_sta" select="$lab_check_sta"/>
			<xsl:with-param name="lab_btn_approved" select="$lab_btn_approved"/>
			<xsl:with-param name="lab_btn_declined" select="$lab_btn_declined"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_space"/>
		<xsl:apply-templates select="trainingplan_list">
			<xsl:with-param name="lab_year" select="$lab_year"/>
			<xsl:with-param name="lab_time" select="$lab_time"/>
			<xsl:with-param name="lab_submit_timestamp" select="$lab_submit_timestamp"/>
			<xsl:with-param name="lab_status" select="$lab_status"/>
			<xsl:with-param name="lab_down" select="$lab_down"/>
			<xsl:with-param name="lab_department" select="$lab_department"/>
			<xsl:with-param name="lab_approved" select="$lab_approved"/>
			<xsl:with-param name="lab_declined" select="$lab_declined"/>
			<xsl:with-param name="lab_pending" select="$lab_pending"/>
			<xsl:with-param name="lab_no_yearplan" select="$lab_no_yearplan"/>
			<xsl:with-param name="lab_no_yearplan_of" select="$lab_no_yearplan_of"/>
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
			<tr height="30">
				<td align="left">
					
							<!-- 
							<td width="8%" align="left">
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$lab_check_sta"/><xsl:text>：</xsl:text>
							</td>
							 -->
							
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
											<li class="{$class}"> <a href="javaScript:select_status(document.frmXml,'{@id}')" >
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
				  								<!--  
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
												-->
												<input type="hidden" name="status" value=""/>
										
							
						
				</td>
				<xsl:if test="$train_process_status = 'PENDING' or $train_process_status = 'DECLINED'">
					<td align="right">
						<xsl:if test="$train_process_status = 'PENDING'">
							<span>
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_btn_approved"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:plan.upd_multi_status_exec(document.frmXml,'A','<xsl:value-of select="$wb_lang"/>','YEAR')</xsl:with-param>
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								</xsl:call-template>
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_btn_declined"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:plan.upd_multi_status_exec(document.frmXml,'D','<xsl:value-of select="$wb_lang"/>','YEAR')</xsl:with-param>
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								</xsl:call-template>
							</span>
							<div style="height:9px;border-bottom: 1px solid #ddd;margin-bottom:-4px;"></div>
							</xsl:if>
					</td>
				</xsl:if>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="trainingplan_list">
		<xsl:param name="lab_year"/>
		<xsl:param name="lab_time"/>
		<xsl:param name="lab_submit_timestamp"/>
		<xsl:param name="lab_down"/>
		<xsl:param name="lab_department"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_approved"/>
		<xsl:param name="lab_declined"/>
		<xsl:param name="lab_pending"/>
		<xsl:param name="lab_no_yearplan"/>
		<xsl:param name="lab_no_yearplan_of"/>
		<xsl:choose>
			<xsl:when test="count(trainingplan) = 0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text">
						<xsl:choose>
							<xsl:when test="$train_process_status = ''">
								<xsl:value-of select="$lab_no_yearplan"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_no_yearplan_of"/>
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
				<table class="table wzb-ui-table">
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
						
							<xsl:choose>
								<xsl:when test="$sort_col = 'ypn_year'">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','ypn_year','sort_order','{$sort_order}')" class="TitleText">
										<xsl:value-of select="$lab_year"/>
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
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','ypn_year','sort_order','ASC')" class="TitleText">
										<xsl:value-of select="$lab_year"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td width="20%" align="left">
							<xsl:choose>
								<xsl:when test="$sort_col = 'ysg_submit_end_datetime'">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','ysg_submit_end_datetime','sort_order','{$sort_order}')" class="TitleText">
										<xsl:value-of select="$lab_time"/>
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
										<xsl:value-of select="$lab_time"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td width="20%" align="left">
							<xsl:choose>
								<xsl:when test="$sort_col = 'tcr_title'">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','tcr_title','sort_order','{$sort_order}')" class="TitleText">
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
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','tcr_title','sort_order','ASC')" class="TitleText">
										<xsl:value-of select="$lab_department"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td width="20%" align="left">
							<xsl:choose>
								<xsl:when test="$sort_col = 'ypn_status'">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','ypn_status','sort_order','{$sort_order}')" class="TitleText">
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
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','ypn_status','sort_order','ASC')" class="TitleText">
										<xsl:value-of select="$lab_status"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td width="15%" align="left">
							<xsl:choose>
								<xsl:when test="$sort_col = 'ypn_submit_timestamp'">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','ypn_submit_timestamp','sort_order','{$sort_order}')" class="TitleText">
										<xsl:value-of select="$lab_submit_timestamp"/>
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
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','ypn_submit_timestamp','sort_order','ASC')" class="TitleText">
										<xsl:value-of select="$lab_submit_timestamp"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td align="right" width="5%"></td>
					</tr>
					<xsl:for-each select="trainingplan">
						<xsl:variable name="row_class">
							<xsl:choose>
								<xsl:when test="position() mod 2">RowsEven</xsl:when>
								<xsl:otherwise>RowsOdd</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<tr>
					
							<td>
									<xsl:if test="$train_process_status = 'PENDING'">
							 
									<input type="checkbox" value="{@ypn_year}~{@ypn_tcr_id}" name="planId_chkbox" id="{@ypn_update_timestamp}"/>
								 
									</xsl:if>
								<xsl:value-of select="@ypn_year"/>
							</td>
							<td align="left">
								<xsl:choose>
									<xsl:when test="@ysg_submit_end_datetime !=''">
										<xsl:call-template name="display_time">
											<xsl:with-param name="my_timestamp">
												<xsl:value-of select="@ysg_submit_end_datetime"/>
											</xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>--</xsl:otherwise>
								</xsl:choose>
							</td>
							<td align="left">
								<xsl:value-of select="@tcr_title"/>
							</td>
							<td align="left">
								<xsl:if test="@ypn_status = 'PENDING'">
									<xsl:value-of select="$lab_pending"/>
								</xsl:if>
								<xsl:if test="@ypn_status = 'APPROVED'">
									<xsl:value-of select="$lab_approved"/>
								</xsl:if>
								<xsl:if test="@ypn_status = 'DECLINED'">
									<xsl:value-of select="$lab_declined"/>
								</xsl:if>
							</td>
							<td align="left">
								<xsl:choose>
									<xsl:when test="@ypn_submit_timestamp !=''">
										<xsl:call-template name="display_time">
											<xsl:with-param name="my_timestamp">
											<xsl:value-of select="@ypn_submit_timestamp"/>
											</xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>--</xsl:otherwise>
								</xsl:choose>
							</td>
							<td nowrap="nowrap" align="right">
								<xsl:variable name="file_path">
									<xsl:value-of select="$wb_planFile_path"/>
									<xsl:value-of select="@ypn_tcr_id"/>/<xsl:value-of select="@ypn_year"/>/<xsl:value-of select="@ypn_file_name"/>
								</xsl:variable>
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_down" />
									<xsl:with-param name="wb_gen_btn_href" select="$file_path"/>
									<xsl:with-param name="wb_gen_btn_target">
										<xsl:text>_blank</xsl:text>
									</xsl:with-param>
								</xsl:call-template>
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
