<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="no"/>
	<xsl:variable name="ity_id" select="/applyeasy/item/@type"/>
	<xsl:variable name="exam_ind" select="/applyeasy/item/@exam_ind"/>
	<xsl:variable name="page_variant" select="/applyeasy/meta/page_variant"/>
	
	<xsl:variable name="cur_page" select="/applyeasy/item/pagination/@cur_page"/>
	<xsl:variable name="total" select="/applyeasy/item/pagination/@total_rec"/>
	<xsl:variable name="page_size" select="/applyeasy/item/pagination/@page_size"/>
	
	<xsl:variable name="has_ftn_amd_itm_cos_main_view" select="/applyeasy/permission/@has_ftn_amd_itm_cos_main_view"/>
	<xsl:variable name="has_ftn_amd_itm_cos_main_application" select="/applyeasy/permission/@has_ftn_amd_itm_cos_main_application"/>
	<xsl:variable name="has_ftn_amd_itm_cos_main_performance" select="/applyeasy/permission/@has_ftn_amd_itm_cos_main_performance"/>

	<!-- sorting variable -->
	<xsl:variable name="cur_sort_col" select="/applyeasy/item/pagination/@sort_col"/>
	<xsl:variable name="cur_sort_order" select="/applyeasy/item/pagination/@sort_order"/>
	<xsl:variable name="sort_order_by">
		<xsl:choose>
			<xsl:when test="$cur_sort_order = 'ASC' or $cur_sort_order = 'asc'">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_run">
		<xsl:choose>
			<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_run"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="$lab_const_run"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/applyeasy">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript"><![CDATA[
				itm_lst = new wbItem;
				$(function(){
					$(".quick-container").hover(function(){
		        		$(this).children(".wzb-quick").toggle();
		        		$(this).children(".quick-icon").toggleClass('active');
	        		});
				});
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="item">
			<xsl:with-param name="lab_g_txt_btn_new_run">添加<xsl:value-of select="$lab_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_no_run">沒有<xsl:value-of select="$lab_run"/>已找到</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_code">編號</xsl:with-param>
			<xsl:with-param name="lab_start_date">
				<xsl:value-of select="$lab_run"/>開始</xsl:with-param>
			<xsl:with-param name="lab_end_date">
				<xsl:value-of select="$lab_run"/>結束</xsl:with-param>
			<xsl:with-param name="lab_enrollment_end_date">
				<xsl:value-of select="$lab_const_enrollment"/>結束</xsl:with-param>
			<xsl:with-param name="lab_enrollment_start_date">
				<xsl:value-of select="$lab_const_enrollment"/>開始</xsl:with-param>
			<xsl:with-param name="lab_enrollment">
				<xsl:value-of select="$lab_const_enrollment"/>
			</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_progress_status">進行狀態</xsl:with-param>
			<xsl:with-param name="lab_status_not_started">未開始</xsl:with-param>
			<xsl:with-param name="lab_status_in_progress">進行中</xsl:with-param>
			<xsl:with-param name="lab_status_finished">已結束</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_status_on">已發佈</xsl:with-param>
			<xsl:with-param name="lab_status_off">未發佈</xsl:with-param>
			<xsl:with-param name="lab_pending">審批中</xsl:with-param>
			<xsl:with-param name="lab_confirm">已錄取</xsl:with-param>
			<xsl:with-param name="lab_waitlisted">候補名單</xsl:with-param>
			<xsl:with-param name="lab_pending_short">審...</xsl:with-param>
			<xsl:with-param name="lab_confirm_short">已...</xsl:with-param>
			<xsl:with-param name="lab_waitlisted_short">候...</xsl:with-param>
			<xsl:with-param name="lab_created_by">創建者</xsl:with-param>
			<xsl:with-param name="lab_last_modified">修改日期</xsl:with-param>
			<xsl:with-param name="lab_g_btn_performance">成績</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="item">
			<xsl:with-param name="lab_g_txt_btn_new_run">添加<xsl:value-of select="$lab_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_no_run">没有<xsl:value-of select="$lab_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_title">名称</xsl:with-param>
			<xsl:with-param name="lab_code">编号</xsl:with-param>
			<xsl:with-param name="lab_start_date">
				<xsl:value-of select="$lab_run"/>开始</xsl:with-param>
			<xsl:with-param name="lab_end_date">
				<xsl:value-of select="$lab_run"/>结束</xsl:with-param>
			<xsl:with-param name="lab_enrollment_end_date">
				<xsl:value-of select="$lab_const_enrollment"/>结束</xsl:with-param>
			<xsl:with-param name="lab_enrollment_start_date">
				<xsl:value-of select="$lab_const_enrollment"/>开始</xsl:with-param>
			<xsl:with-param name="lab_enrollment">
				<xsl:value-of select="$lab_const_enrollment"/>
			</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_progress_status">进行状态</xsl:with-param>
			<xsl:with-param name="lab_status_not_started">未开始</xsl:with-param>
			<xsl:with-param name="lab_status_in_progress">进行中</xsl:with-param>
			<xsl:with-param name="lab_status_finished">已结束</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_status_on">已发布</xsl:with-param>
			<xsl:with-param name="lab_status_off">未发布</xsl:with-param>
			<xsl:with-param name="lab_pending">审批中</xsl:with-param>
			<xsl:with-param name="lab_confirm">已录取</xsl:with-param>
			<xsl:with-param name="lab_waitlisted">等待名单中</xsl:with-param>
			<xsl:with-param name="lab_pending_short">审...</xsl:with-param>
			<xsl:with-param name="lab_confirm_short">已...</xsl:with-param>
			<xsl:with-param name="lab_waitlisted_short">等...</xsl:with-param>
			<xsl:with-param name="lab_created_by">创建者</xsl:with-param>
			<xsl:with-param name="lab_last_modified">修改日期</xsl:with-param>
			<xsl:with-param name="lab_g_btn_performance">成绩</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="item">
			<xsl:with-param name="lab_g_txt_btn_new_run">Create <xsl:value-of select="$lab_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_no_run">No <xsl:value-of select="$lab_run"/> found</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_code">Code</xsl:with-param>
			<xsl:with-param name="lab_start_date">
				<xsl:value-of select="$lab_run"/> start</xsl:with-param>
			<xsl:with-param name="lab_end_date">
				<xsl:value-of select="$lab_run"/> end</xsl:with-param>
			<xsl:with-param name="lab_enrollment_end_date">
				<xsl:value-of select="$lab_const_enrollment"/> end</xsl:with-param>
			<xsl:with-param name="lab_enrollment_start_date">
				<xsl:value-of select="$lab_const_enrollment"/> start</xsl:with-param>
			<xsl:with-param name="lab_enrollment">
				<xsl:value-of select="$lab_const_enrollment"/>
			</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_progress_status">Progress status</xsl:with-param>
			<xsl:with-param name="lab_status_not_started">Not started</xsl:with-param>
			<xsl:with-param name="lab_status_in_progress">In progress</xsl:with-param>
			<xsl:with-param name="lab_status_finished">Finished</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_status_on">Published</xsl:with-param>
			<xsl:with-param name="lab_status_off">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_pending">Pending approval</xsl:with-param>
			<xsl:with-param name="lab_confirm">Enrolled</xsl:with-param>
			<xsl:with-param name="lab_waitlisted">Waitlisted</xsl:with-param>
			<xsl:with-param name="lab_pending_short">P...</xsl:with-param>
			<xsl:with-param name="lab_confirm_short">E...</xsl:with-param>
			<xsl:with-param name="lab_waitlisted_short">W...</xsl:with-param>
			<xsl:with-param name="lab_created_by">Created by</xsl:with-param>
			<xsl:with-param name="lab_last_modified">Modified</xsl:with-param>
			<xsl:with-param name="lab_g_btn_performance">Performance</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="item">
		<xsl:param name="lab_no_run"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_code"/>
		<xsl:param name="lab_start_date"/>
		<xsl:param name="lab_end_date"/>
		<xsl:param name="lab_run_list"/>
		<xsl:param name="lab_enrollment_start_date"/>
		<xsl:param name="lab_enrollment_end_date"/>
		<xsl:param name="lab_enrollment"/>
		<xsl:param name="lab_progress_status"/>
		<xsl:param name="lab_status_not_started"/>
		<xsl:param name="lab_status_in_progress"/>
		<xsl:param name="lab_status_finished"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_pending"/>
		<xsl:param name="lab_confirm"/>
		<xsl:param name="lab_waitlisted"/>
		<xsl:param name="lab_pending_short"/>
		<xsl:param name="lab_confirm_short"/>
		<xsl:param name="lab_waitlisted_short"/>
		<xsl:param name="lab_g_txt_btn_new_run"/>
		<xsl:param name="lab_created_by"/>
		<xsl:param name="lab_last_modified"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_g_btn_performance"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<a href="javascript:itm_lst.get_item_detail({@id})" class="NavLink">
					<xsl:value-of select="@title"/>
				</a>
				<span class="NavLink">
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:choose>
						<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
					</xsl:choose>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		 <xsl:call-template name="itm_action_nav">
	    	<xsl:with-param name="view_mode">simple</xsl:with-param>
			<xsl:with-param name="is_add">false</xsl:with-param>
			<xsl:with-param  name="cur_node_id">04</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="extra_td">
				<td align="right">
					<xsl:if test="$page_variant/@hasItmAddRunBtn= 'true'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_new_run"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.ins_item_run_prep(<xsl:value-of select="@id"/>,'<xsl:value-of select="$ity_id"/>','<xsl:value-of select="@tcr_id"/>')</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
				</td>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="count(run_item_list/item) = 0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_run"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td>
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'itm_code' ">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_code','sort_order','{$sort_order_by}')" class="TitleText">
										<xsl:value-of select="$lab_code"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_sort_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_code','sort_order','asc')" class="TitleText">
										<xsl:value-of select="$lab_code"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'itm_title' ">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_title','sort_order','{$sort_order_by}')" class="TitleText">
										<xsl:value-of select="$lab_title"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_sort_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_title','sort_order','asc')" class="TitleText">
										<xsl:value-of select="$lab_title"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>						
						<td align="center">
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'itm_eff_start_datetime' ">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_eff_start_datetime','sort_order','{$sort_order_by}')" class="TitleText">
										<xsl:value-of select="$lab_start_date"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_sort_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_eff_start_datetime','sort_order','asc')" class="TitleText">
										<xsl:value-of select="$lab_start_date"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td align="center">
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'itm_eff_end_datetime' ">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_eff_end_datetime','sort_order','{$sort_order_by}')" class="TitleText">
										<xsl:value-of select="$lab_end_date"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_sort_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_eff_end_datetime','sort_order','asc')" class="TitleText">
										<xsl:value-of select="$lab_end_date"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td align="center">
							<xsl:value-of select="$lab_progress_status"/>
						</td>
						<td align="center">
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'itm_appn_start_datetime' ">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_appn_start_datetime','sort_order','{$sort_order_by}')" class="TitleText">
										<xsl:value-of select="$lab_enrollment_start_date"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_sort_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_appn_start_datetime','sort_order','asc')" class="TitleText">
										<xsl:value-of select="$lab_enrollment_start_date"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td align="center">
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'itm_appn_end_datetime' ">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_appn_end_datetime','sort_order','{$sort_order_by}')" class="TitleText">
										<xsl:value-of select="$lab_enrollment_end_date"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_sort_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_appn_end_datetime','sort_order','asc')" class="TitleText">
										<xsl:value-of select="$lab_enrollment_end_date"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>

						<td align="center">
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'itm_status' ">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_status','sort_order','{$sort_order_by}')" class="TitleText">
										<xsl:value-of select="$lab_status"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_sort_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_status','sort_order','asc')" class="TitleText">
										<xsl:value-of select="$lab_status"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						
						<td align="right">
							
						</td>						
					</tr>
					<xsl:for-each select="run_item_list/item">
						<tr>
							<td>
								<a href="javascript:itm_lst.get_item_run_detail({@item_id},'{@life_status}')" class="Text">
									<xsl:value-of select="@item_code"/>
								</a>
							</td>
							<td>
								<xsl:value-of select="title"/>
							</td>							
							<td align="center" nowrap="nowrap">
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="@eff_start_datetime"/>
									</xsl:with-param>
								</xsl:call-template>
							</td>
							<td align="center" nowrap="nowrap">
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="@eff_end_datetime"/>
									</xsl:with-param>
								</xsl:call-template>
							</td>
							<td align="center" nowrap="nowrap">
								<xsl:choose>
									<xsl:when test="@progress_status='NOT_STARTED'">
									<xsl:value-of select="$lab_status_not_started"/>
									</xsl:when>
									<xsl:when test="@progress_status='IN_PROGRESS'">
									<xsl:value-of select="$lab_status_in_progress"/>
									</xsl:when>
									<xsl:when test="@progress_status='FINISHED'">
									<xsl:value-of select="$lab_status_finished"/>
									</xsl:when>
								</xsl:choose>
							</td>
							<td align="center" nowrap="nowrap">
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="@appn_start_datetime"/>
									</xsl:with-param>
								</xsl:call-template>
							</td>
							<td align="center" nowrap="nowrap">
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="@appn_end_datetime"/>
									</xsl:with-param>
								</xsl:call-template>
							</td>
							<td align="center" nowrap="nowrap">
								<xsl:choose>
									<xsl:when test="@status='ON'">
										<xsl:value-of select="$lab_status_on"/>
									</xsl:when>
									<xsl:when test="@status='OFF'">
										<xsl:value-of select="$lab_status_off"/>
									</xsl:when>
								</xsl:choose>
							</td>
							<td align="right" style="overflow:visible;">
								<xsl:if test="$has_ftn_amd_itm_cos_main_view = 'true' or $has_ftn_amd_itm_cos_main_application = 'true' or $has_ftn_amd_itm_cos_main_performance = 'true'">
									<span style="position:relative;" class="quick-container">
										<i class="quick-icon"></i>
										<div class="wzb-exam-choose wzb-quick" style="display:none;">
											<ul style="list-style:none;">
												<xsl:if test="$has_ftn_amd_itm_cos_main_view = 'true'">
													<li onclick="javascript:itm_lst.get_item_publish({@item_id})">
														<a href="javascript:itm_lst.get_item_publish({@item_id})">
															<xsl:choose>
																<xsl:when test="@status='ON'">
																	<xsl:value-of select="$label_core_training_management_256"></xsl:value-of>
																</xsl:when>
																<xsl:when test="@status='OFF'">
																	<xsl:value-of select="$label_core_training_management_255"/>
																</xsl:when>
															</xsl:choose>
														</a>
													</li>
												</xsl:if>
												
												<xsl:if test="$has_ftn_amd_itm_cos_main_application = 'true'">
													<li onclick="javascript:app.get_application_list('',{@item_id},'','','','')">
														<a href="javascript:app.get_application_list('',{@item_id},'','','','')">
															<xsl:value-of select="$lab_g_txt_btn_process_enrol"/>
														</a>
													</li>
												</xsl:if>
												
												<xsl:if test="$has_ftn_amd_itm_cos_main_performance = 'true'">
													<li onclick="javascript:attn.get_grad_record({@item_id})">
														<a href="javascript:attn.get_grad_record({@item_id})">
															<xsl:value-of select="$lab_grad_record" />
														</a>
													</li>
												</xsl:if>
										    </ul>
									    </div>
									</span>
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

			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
