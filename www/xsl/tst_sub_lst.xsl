<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0" xmlns:java="http://xml.apache.org/xalan/java"
	exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/display_score.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/> 
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>	
	<xsl:import href="share/itm_nav_share.xsl"/>
	
	<xsl:output indent="yes"/>
	<xsl:variable name="queue_type" select="/applyeasy/enrolment_list/queue/@type"/>
	<xsl:variable name="course_id" select="/applyeasy/enrolment_list/header/@course_id"/>
	<xsl:variable name="module_id" select="/applyeasy/enrolment_list/header/@mod_id"/>
	<xsl:variable name="has_eassy" select="/applyeasy/enrolment_list/header/@has_essay"/>
	
	<xsl:variable name="cur_page" select="/applyeasy/enrolment_list/pagination/@cur_page"/>
	<xsl:variable name="total" select="/applyeasy/enrolment_list/pagination/@total_rec"/>
	<xsl:variable name="page_size" select="/applyeasy/enrolment_list/pagination/@page_size"/>
	<xsl:variable name="srh_timestamp" select="/applyeasy/enrolment_list/pagination/@timestamp"/>
	<xsl:variable name="sort_col" select="/applyeasy/enrolment_list/pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="/applyeasy/enrolment_list/pagination/@sort_order"/>
	<xsl:variable name="sort_order">
		<xsl:choose>
			<xsl:when test="$cur_order = 'asc' or $cur_order = 'ASC' ">desc</xsl:when>
			<xsl:otherwise>asc</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>	
	<xsl:variable name="label_core_training_management_250" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_250')"/>
	<xsl:variable name="label_core_training_management_393" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_393')"/>
	<!-- =============================================================== -->
	<xsl:template match="/applyeasy">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="enrolment_list">
		<head>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript">
				var module_lst = new wbModule;
				
				$(document).keydown(function(event){
				  if(event.keyCode ==13){
				  	module_lst.view_submission_lst($("#courseId").val(),$("#moduleId").val(),'search');
				  }
				});
			</script>
			<xsl:call-template name="new_css"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form onsubmit="return false;">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_sub_details">下面顯示各個學員的測驗狀態和分數。如果測驗包含問答題，您需要給它們手動評分，以確定最後的分數。</xsl:with-param>
			<xsl:with-param name="lab_please_select">請選擇狀態</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_sub_graded">已評分</xsl:with-param>
			<xsl:with-param name="lab_sub_ungraded">尚未評分</xsl:with-param>
			<xsl:with-param name="lab_sub_not_submit">尚未提交</xsl:with-param>
			<xsl:with-param name="lab_no_sub">還沒有任何提交</xsl:with-param>
			<xsl:with-param name="lab_submission_date">提交日期</xsl:with-param>			
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_grade">評分</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_view">詳細報告</xsl:with-param>
			<xsl:with-param name="lab_score">分數</xsl:with-param>
			<xsl:with-param name="lab_test_result">成績</xsl:with-param>
			<xsl:with-param name="lab_enrollment_date">報名日期</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_sub_details">下面显示各个学员的测验状态和分数。如果测验包含问答题，您需要给它们手动评分，以确定最后的分数。</xsl:with-param>
			<xsl:with-param name="lab_please_select">请选择状态</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_sub_graded">已评分</xsl:with-param>
			<xsl:with-param name="lab_sub_ungraded">尚未评分</xsl:with-param>
			<xsl:with-param name="lab_sub_not_submit">尚未提交</xsl:with-param>
			<xsl:with-param name="lab_no_sub">还没有任何提交</xsl:with-param>
			<xsl:with-param name="lab_submission_date">提交日期</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_grade">评分</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_view">详细报告</xsl:with-param>
			<xsl:with-param name="lab_score">分数</xsl:with-param>
			<xsl:with-param name="lab_test_result">成绩</xsl:with-param>	
			<xsl:with-param name="lab_enrollment_date">录取日期</xsl:with-param>		
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_sub_details">The following shows the test submission status. If the test contains essay questions, you need to grade those essay questions manually to finalize the scores.</xsl:with-param>
			<xsl:with-param name="lab_please_select">Please select status</xsl:with-param>
			<xsl:with-param name="lab_all">All</xsl:with-param>
			<xsl:with-param name="lab_sub_graded">Graded</xsl:with-param>
			<xsl:with-param name="lab_sub_ungraded">Not graded</xsl:with-param>
			<xsl:with-param name="lab_sub_not_submit">Not submitted</xsl:with-param>
			<xsl:with-param name="lab_no_sub">No submission found</xsl:with-param>
			<xsl:with-param name="lab_submission_date">Submission date</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_grade">Grade</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_view">View</xsl:with-param>
			<xsl:with-param name="lab_score">Score</xsl:with-param>	
			<xsl:with-param name="lab_test_result">Test result</xsl:with-param>
			<xsl:with-param name="lab_enrollment_date">Enrollment date</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_sub_details"/>
		<xsl:param name="lab_please_select"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_sub_graded"/>
		<xsl:param name="lab_sub_ungraded"/>
		<xsl:param name="lab_sub_not_submit"/>
		<xsl:param name="lab_no_sub"/>
		<xsl:param name="lab_submission_date"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_g_txt_btn_grade"/>
		<xsl:param name="lab_g_txt_btn_view"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_test_result"/>
		<xsl:param name="lab_enrollment_date"/>
		
		<!-- header -->
		<xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">120</xsl:with-param>
		</xsl:call-template>
		
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
		</xsl:call-template>
		
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:apply-templates select="/applyeasy/item/nav/item" mode="nav">
					<xsl:with-param name="lab_run_info"><xsl:value-of select="$label_core_training_management_250" /></xsl:with-param>
				</xsl:apply-templates>
				<span class="NavLink">
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<a href="javascript:itm_lst.itm_evaluation_report({$course_id},'TST')"><xsl:value-of select="$label_core_training_management_393"/></a>
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:value-of select="$lab_test_result"/><xsl:text>&#160;-&#160;</xsl:text>
					<xsl:value-of select="header/title/text()"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<!-- header end -->
		
		
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_sub_details"/>
		</xsl:call-template>
		
		
		<table  cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td>
					<span class="TitleText">
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$lab_please_select"/>：
					</span>
					<xsl:text>&#160;</xsl:text>
					<select name="ass_queue" id="ass_queue" class="Select" onchange="javascript:module_lst.view_submission_lst({$course_id},{$module_id},this.options[this.selectedIndex].value)">
						<option value="all">
							<xsl:if test="queue/@type = 'all'">
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$lab_all"/>&#160;(<xsl:value-of select="stat/queue[@name = 'all']/@count"/>)</option>
						<option value="graded">
							<xsl:if test="queue/@type = 'graded'">
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$lab_sub_graded"/>&#160;(<xsl:value-of select="stat/queue[@name = 'graded']/@count"/>)</option>
						<xsl:if test="stat/queue[@name = 'not_graded']/@count">
						<option value="not_graded">
							<xsl:if test="queue/@type = 'not_graded'">
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$lab_sub_ungraded"/>&#160;(<xsl:value-of select="stat/queue[@name = 'not_graded']/@count"/>)</option>
						</xsl:if>
						<option value="not_submitted">
							<xsl:if test="queue/@type = 'not_submitted'">
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$lab_sub_not_submit"/>&#160;(<xsl:value-of select="stat/queue[@name = 'not_submitted']/@count"/>)</option>
					</select>
					
					
					<div class="wzb-form-search" style="float:right;">
						<input type="text" size="11" name="s_usr_id_name" id="s_usr_id_name" class="form-control"/>
						<input type="button" class="form-submit" value="" onclick="javascript:module_lst.view_submission_lst({$course_id},{$module_id},'search')"/>
						<xsl:text>&#160;</xsl:text>
					</div>
				</td>
			</tr>
		</table>
		<input  type="hidden" value="{$course_id}" id="courseId"/>
		<input  type="hidden" value="{$module_id}" id="moduleId"/>
		<xsl:choose>
			<xsl:when test="count(record) = 0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_sub"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td>
							<xsl:choose>
								<xsl:when test="$sort_col = 'usr_display_bil' ">
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','usr_display_bil','sortOrder','{$sort_order}','cur_page','1')" class="TitleText">
										<xsl:value-of select="$lab_dis_name"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','usr_display_bil','sortOrder','asc','cur_page','1');" class="TitleText">
										<xsl:value-of select="$lab_dis_name"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="$sort_col = 'usg_display_bil' ">
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','usg_display_bil','sortOrder','{$sort_order}','cur_page','1')" class="TitleText">
										<xsl:value-of select="$lab_group"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','usg_display_bil','sortOrder','asc','cur_page','1');" class="TitleText">
										<xsl:value-of select="$lab_group"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td align="center" nowrap="nowrap">
							<xsl:choose>
								<xsl:when test="$sort_col = 'enr_create_timestamp' ">
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','enr_create_timestamp','sortOrder','{$sort_order}','cur_page','1')" class="TitleText">
										<xsl:value-of select="$lab_enrollment_date"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','enr_create_timestamp','sortOrder','asc','cur_page','1');" class="TitleText">
										<xsl:value-of select="$lab_enrollment_date"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>						
						</td>
						<xsl:if test="$queue_type = 'graded' or $queue_type = 'all' or $queue_type = 'not_graded'">
							<td align="center" nowrap="nowrap">
								<xsl:choose>
									<xsl:when test="$sort_col = 'pgr_complete_datetime' ">
										<a href="javascript:wb_utils_nav_get_urlparam('sortCol','pgr_complete_datetime','sortOrder','{$sort_order}','cur_page','1')" class="TitleText">
											<xsl:value-of select="$lab_submission_date"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wb_utils_nav_get_urlparam('sortCol','pgr_complete_datetime','sortOrder','asc','cur_page','1');" class="TitleText">
											<xsl:value-of select="$lab_submission_date"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>	
							</td>
							<td align="center">
								<xsl:choose>
									<xsl:when test="$sort_col = 'pgr_score' ">
										<a href="javascript:wb_utils_nav_get_urlparam('sortCol','pgr_score','sortOrder','{$sort_order}','cur_page','1')" class="TitleText">
											<xsl:value-of select="$lab_score"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wb_utils_nav_get_urlparam('sortCol','pgr_score','sortOrder','asc','cur_page','1');" class="TitleText">
											<xsl:value-of select="$lab_score"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>												
							</td>
							<td align="right"></td>
						</xsl:if>
					</tr>
					<xsl:for-each select="record">
						<xsl:variable name="row_class">
							<xsl:choose>
								<xsl:when test="position() mod 2">RowsEven</xsl:when>
								<xsl:otherwise>RowsOdd</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<tr class="{$row_class}">
							<td>
								<span class="Text"><xsl:value-of select="entity/text()"/></span>
							</td>
							<td>
								<span class="Text">
									<xsl:for-each select="group">
										<xsl:if test="position() > 1">&#160;/&#160;</xsl:if>
										<xsl:choose>
											<xsl:when test="@name != ''">
												<xsl:value-of select="@name"/>
											</xsl:when>
											<xsl:otherwise>--</xsl:otherwise>
										</xsl:choose>
									</xsl:for-each>
								</span>
							</td>
							<td align="center" nowrap="nowrap">
								<span class="Text">
									<xsl:call-template name="display_time">
										<xsl:with-param name="my_timestamp" select="enrolment/@enr_create_timestamp"/>
									</xsl:call-template>
								</span>						
							</td>
							<xsl:if test="$queue_type = 'graded' or $queue_type = 'all' or $queue_type = 'not_graded'">
								<td align="center" nowrap="nowrap">
									<span class="Text">
										<xsl:choose>
											<xsl:when test="$queue_type='not_submitted'">
												--
											</xsl:when>
											<xsl:otherwise>
												<xsl:if test="not(progress/@complete_datetime)">--</xsl:if>
												<xsl:call-template name="display_time">
													<xsl:with-param name="my_timestamp" select="progress/@complete_datetime"/>
													<xsl:with-param name="dis_time">T</xsl:with-param>			
												</xsl:call-template>	
											</xsl:otherwise>
										</xsl:choose>
									</span>
								</td>
								<td align="center">
									<span class="Text">
										<xsl:choose>
											<xsl:when test="progress/@status = 'Not Submitted Yet' or progress/@status = 'NOT GRADED'">--</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="display_score">
													<xsl:with-param name="score" select="progress/@pgr_score"/>
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
									</span>
								</td>
							</xsl:if>
							<td align="right" nowrap="nowrap">
								<xsl:if test="progress/@status = 'OK' or progress/@status = 'NOT GRADED'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.view_submission(<xsl:value-of select="entity/@ent_id"/>,<xsl:value-of select="$module_id"/>,<xsl:value-of select="progress/@attempt_nbr"/>,0,<xsl:value-of select="progress/@tkh_id"/>,<xsl:value-of select="$course_id"/>);</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_view"/>
								</xsl:call-template>	
								<xsl:if test="$has_eassy = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.grade_submission(<xsl:value-of select="entity/@ent_id"/>,<xsl:value-of select="$module_id"/>,<xsl:value-of select="progress/@attempt_nbr"/>,0,<xsl:value-of select="progress/@tkh_id"/>,<xsl:value-of select="$course_id"/>);</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_grade"/>
								</xsl:call-template>		
								</xsl:if>
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
					<xsl:with-param name="timestamp" select="$srh_timestamp"/>
					<xsl:with-param name="page_size_name">pagesize</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
