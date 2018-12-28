<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="content">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_name"/>
		<xsl:param name="lab_online_content"/>
		<xsl:param name="lab_group"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_total_time"/>
		<xsl:param name="lab_last_access"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_detail"/>
		<xsl:param name="lab_status_viewed"/>
		<xsl:param name="lab_status_not_viewed"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_showing"/>
		<xsl:param name="lab_comma"/>
		<xsl:param name="lab_prev"/>
		<xsl:param name="lab_next"/>
		<xsl:param name="lab_no_rpt_grp"/>
		<xsl:param name="lab_item_type"/>
		<xsl:param name="lab_user_report"/>
		<xsl:param name="lab_usage_report"/>
		<xsl:param name="lab_tracking_report"/>
		<xsl:param name="lab_tracking_report_desc"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_enrolled_users"/>
		<xsl:param name="lab_qr_users"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_any_date"/>
		<xsl:param name="lab_first_acc_date"/>
		<xsl:param name="lab_last_acc_date"/>
		<xsl:param name="lab_complete_date"/>
		<xsl:param name="lab_any_acc_date"/>
		<xsl:param name="lab_no_record"/>
		<xsl:param name="nav_link"/>
		<xsl:param name="btn_ok_link"/>
		<xsl:param name="parent_code"/>
		<xsl:param name="lab_search_val"/>
		<xsl:variable name="itm_id" select="//itm_action_nav/@itm_id"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_report.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
			rpt = new wbReport
			itm_lst = new wbItem
				function status(){
					rpt.open_cos_lrn_lst_search(document.frmXml,']]><xsl:value-of select="$itm_id"/><![CDATA[')
					return false;
				}
			]]>
			</script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form onsubmit="return status()" name="frmXml">
		    <xsl:call-template name="itm_action_nav">
				<xsl:with-param  name="cur_node_id">117</xsl:with-param>
			</xsl:call-template>
			<div class="wzb-item-main">
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
					<xsl:with-param name="parent_code" select="$parent_code"/>
					
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_tracking_report"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_nav_link">
					<xsl:with-param name="text">
						<xsl:copy-of select="$nav_link"/>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="tracking_rpt_gen_tab">
					<xsl:with-param name="rpt_target_tab">1</xsl:with-param>
					<xsl:with-param name="course_id" select="$course_id"/>
				</xsl:call-template>
				<table>
					<tr>
						<td width="70%" align="right">
								<div class="wzb-form-search form-search">
										<input type="text" size="25" name="rpt_search_full_name" class="form-control" placeholder="{$lab_search_val}" style="width:175px;" value="{//search_value/text()}"/>
										<input type="button" class="form-submit margin-right4" value="" onclick="javascript:rpt.open_cos_lrn_lst_search(frmXml,{//itm_action_nav/@itm_id})"/>
								</div>
						</td>
					</tr>
				</table>
				<table>
					<tr>
						<td colspan="3" width="100%">
	
							<xsl:if test="/report/course/user_list/@date_range != 'NOT_SPECIFIED'">
								<xsl:choose>
									<xsl:when test="/report/course/user_list/@start_datetime != ''">
										<xsl:call-template name="display_time">
											<xsl:with-param name="my_timestamp">
												<xsl:value-of select="/report/course/user_list/@start_datetime"/>
											</xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_any_date"/>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$lab_to"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:choose>
									<xsl:when test="/report/course/user_list/@end_datetime != ''">
										<xsl:call-template name="display_time">
											<xsl:with-param name="my_timestamp">
												<xsl:value-of select="/report/course/user_list/@end_datetime"/>
											</xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_any_date"/>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="/report/course/user_list/@date_range='LAST_ACC'">
									( <xsl:value-of select="$lab_last_acc_date"/> )
								</xsl:when>
									<xsl:when test="/report/course/user_list/@date_range='COMMENCE'">
									( <xsl:value-of select="$lab_first_acc_date"/> )
								</xsl:when>
									<xsl:when test="/report/course/user_list/@date_range='COMPLETE'">
									( <xsl:value-of select="$lab_complete_date"/> )
								</xsl:when>
									<xsl:otherwise/>
								</xsl:choose>
							</xsl:if>
						</td>
					</tr>
				</table>
				<xsl:choose>
					<xsl:when test="count(report/course/user_list/user) = 0">
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_no_record"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<table class="table wzb-ui-table">
							<tr class="wzb-ui-table-head">
								<td width="8">
								</td>
								<td>
									<xsl:choose>
										<xsl:when test="$order_by = 'r_name' ">
											<a href="javascript:rpt.sort_list('r_name','{$sort_order}')" class="TitleText">
												<xsl:value-of select="$lab_name"/>
												<xsl:call-template name="wb_sortorder_cursor">
													<xsl:with-param name="img_path" select="$wb_img_path"/>
													<xsl:with-param name="sort_order" select="$cur_order"/>
												</xsl:call-template>
											</a>
										</xsl:when>
										<xsl:otherwise>
											<a href="javascript:rpt.sort_list('r_name','asc')" class="TitleText">
												<xsl:value-of select="$lab_name"/>
											</a>
										</xsl:otherwise>
									</xsl:choose>
									<!--
									<span class="TitleText">
										<xsl:value-of select="$lab_name"/>
									</span>
									-->
								</td>
								<td>
									<xsl:choose>
										<xsl:when test="$order_by = 'r_group' ">
											<a href="javascript:rpt.sort_list('r_group','{$sort_order}')" class="TitleText">
												<xsl:value-of select="$lab_group"/>
												<xsl:call-template name="wb_sortorder_cursor">
													<xsl:with-param name="img_path" select="$wb_img_path"/>
													<xsl:with-param name="sort_order" select="$cur_order"/>
												</xsl:call-template>
											</a>
										</xsl:when>
										<xsl:otherwise>
											<a href="javascript:rpt.sort_list('r_group','asc')" class="TitleText">
												<xsl:value-of select="$lab_group"/>
											</a>
										</xsl:otherwise>
									</xsl:choose>
									<!--
									<span class="TitleText">
										<xsl:value-of select="$lab_group"/>
									</span>
									-->
								</td>
								<td align="center">
									<xsl:choose>
										<xsl:when test="$order_by = 'r_status' ">
											<a href="javascript:rpt.sort_list('r_status','{$sort_order}')" class="TitleText">
												<xsl:value-of select="$lab_status"/>
												<xsl:call-template name="wb_sortorder_cursor">
													<xsl:with-param name="img_path" select="$wb_img_path"/>
													<xsl:with-param name="sort_order" select="$cur_order"/>
												</xsl:call-template>
											</a>
										</xsl:when>
										<xsl:otherwise>
											<a href="javascript:rpt.sort_list('r_status','asc')" class="TitleText">
												<xsl:value-of select="$lab_status"/>
											</a>
										</xsl:otherwise>
									</xsl:choose>
									<!--
									<span class="TitleText">
										<xsl:value-of select="$lab_status"/>
									</span>
									-->
								</td>
								<td align="center">
									<xsl:choose>
										<xsl:when test="$order_by = 'r_total_time' ">
											<a href="javascript:rpt.sort_list('r_total_time','{$sort_order}')" class="TitleText">
												<xsl:value-of select="$lab_total_time"/>
												<xsl:call-template name="wb_sortorder_cursor">
													<xsl:with-param name="img_path" select="$wb_img_path"/>
													<xsl:with-param name="sort_order" select="$cur_order"/>
												</xsl:call-template>
											</a>
										</xsl:when>
										<xsl:otherwise>
											<a href="javascript:rpt.sort_list('r_total_time','asc')" class="TitleText">
												<xsl:value-of select="$lab_total_time"/>
											</a>
										</xsl:otherwise>
									</xsl:choose>
									<!--
									<span class="TitleText">
										<xsl:value-of select="$lab_total_time"/>
									</span>
									-->
								</td>
								<td align="center">
									<xsl:choose>
										<xsl:when test="$order_by = 'r_last_access' ">
											<a href="javascript:rpt.sort_list('r_last_access','{$sort_order}')" class="TitleText">
												<xsl:value-of select="$lab_last_access"/>
												<xsl:call-template name="wb_sortorder_cursor">
													<xsl:with-param name="img_path" select="$wb_img_path"/>
													<xsl:with-param name="sort_order" select="$cur_order"/>
												</xsl:call-template>
											</a>
										</xsl:when>
										<xsl:otherwise>
											<a href="javascript:rpt.sort_list('r_last_access','asc')" class="TitleText">
												<xsl:value-of select="$lab_last_access"/>
											</a>
										</xsl:otherwise>
									</xsl:choose>
									<!--
									<span class="TitleText">
										<xsl:value-of select="$lab_last_access"/>
									</span>
									-->
								</td>
								<td align="center">
									<xsl:choose>
										<xsl:when test="$order_by = 'r_score' ">
											<a href="javascript:rpt.sort_list('r_score','{$sort_order}')" class="TitleText">
												<xsl:value-of select="$lab_score"/>
												<xsl:call-template name="wb_sortorder_cursor">
													<xsl:with-param name="img_path" select="$wb_img_path"/>
													<xsl:with-param name="sort_order" select="$cur_order"/>
												</xsl:call-template>
											</a>
										</xsl:when>
										<xsl:otherwise>
											<a href="javascript:rpt.sort_list('r_score','asc')" class="TitleText">
												<xsl:value-of select="$lab_score"/>
											</a>
										</xsl:otherwise>
									</xsl:choose>
									<!--
									<span class="TitleText">
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										<xsl:value-of select="$lab_score"/>
									</span>
									-->
								</td>
								<td align="center" width="60">
								</td>
							</tr>
							<xsl:apply-templates select="report/course/user_list/user">
								<xsl:with-param name="lab_detail" select="$lab_detail"/>
							</xsl:apply-templates>
						</table>
						<xsl:call-template name="wb_ui_pagination">
							<xsl:with-param name="cur_page" select="$cur_page"/>
							<xsl:with-param name="page_size" select="$page_size"/>
							<xsl:with-param name="total" select="$total"/>
							<xsl:with-param name="width" select="$wb_gen_table_width"/>
							<xsl:with-param name="timestamp" select="$timestamp"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
				<div class="wzb-bar">
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$lab_gen_ok"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:<xsl:value-of select="$btn_ok_link"/>(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
							</xsl:call-template>
				</div>
				</div>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================================================== -->
	<xsl:template match="user">
		<xsl:param name="lab_detail"/>
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2">RowsEven</xsl:when>
				<xsl:otherwise>RowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="not_access">
			<xsl:choose>
				<xsl:when test="@total_time = '0:00:00.0'">true</xsl:when>
				<xsl:otherwise>false</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr>
			<td width="8">
			</td>
			<td style="word-break:normal;">
				<xsl:value-of select="@name"/>
			</td>
			<td>
				<xsl:value-of select="@full_path"/>
			</td>
			<td align="center">
				<xsl:call-template name="display_progress_tracking">
					<xsl:with-param name="status" select="@status"/>
					<xsl:with-param name="type">course</xsl:with-param>
					<xsl:with-param name="mod_type">
						<xsl:value-of select="@type"/>
					</xsl:with-param>
					<xsl:with-param name="show_text">true</xsl:with-param>
					<xsl:with-param name="score">
						<xsl:value-of select="@score"/>
					</xsl:with-param>
				</xsl:call-template>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test="$not_access = 'true'">--</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="substring-before(@total_time,'.') = ''  or substring-before(@total_time,'.') = 'null'">
								<xsl:value-of select="@total_time"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="substring-before(@total_time,'.')"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center" nowrap="nowrap">
				<xsl:choose>
					<xsl:when test="@last_access = '' or @last_access = 'null'">--</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp">
								<xsl:value-of select="@last_access"/>
							</xsl:with-param>
							<xsl:with-param name="dis_time">T</xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test="@score = ''">--</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="display_score">
							<xsl:with-param name="score">
								<xsl:value-of select="@score"/>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td width="80" align="center" nowrap="nowrap">
				<xsl:choose>
					<xsl:when test="$not_access = 'true'">
						--
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_detail"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:rpt.lrn_mod_lst('<xsl:value-of select="@id"/>','<xsl:value-of select="/report/course/@id"/>','<xsl:value-of select="@tkh_id"/>')</xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
	<!-- ============================================================================================== -->
	<xsl:template match="cur_usr"/>
	<!-- ============================================================================================== -->
	<xsl:template match="item" mode="nav">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_session_info"/>
		<xsl:param name="current_role"/>
		<xsl:variable name="_count" select="count(preceding-sibling::item)"/>
		<xsl:choose>
			<xsl:when test="@run_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=$_count">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.get_item_run_list({$value})" class="NavLink">
					<xsl:value-of select="$lab_run_info"/>
				</a>
				<span class="NavLink">&#160;&gt;&#160;</span>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:when test="@session_ind = 'true'">
				<span class="NavLink">&#160;&gt;&#160;</span>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=last()">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.session.get_session_list({$value})" class="NavLink">
					<xsl:value-of select="$lab_session_info"/>
				</a>
				<span>&#160;&gt;&#160;</span>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$current_role='INSTR_1'">
						<a href="javascript:itm_lst.get_itm_instr_view({@id})" class="NavLink">
							<xsl:value-of select="title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a href="javascript:itm_lst.get_item_detail({@id})" class="NavLink">
							<xsl:value-of select="title"/>
						</a>
					</xsl:otherwise>
				</xsl:choose>
				
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
