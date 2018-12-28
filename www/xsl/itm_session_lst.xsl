<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/trun_date.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="cust/wb_hdr.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="page_variant" select="/applyeasy/meta/page_variant"/>
	<xsl:variable name="ity_id" select="/applyeasy/item/@type"/>
	<!-- sorting variable -->
	<xsl:variable name="cur_sort_col">
		<xsl:value-of select="/applyeasy/item/pagination/@sort_col"/>
	</xsl:variable>
	<xsl:variable name="cur_sort_order">
		<xsl:value-of select="/applyeasy/item/pagination/@sort_order"/>
	</xsl:variable>
	<xsl:variable name="sort_order_by">
		<xsl:choose>
			<xsl:when test="$cur_sort_order = 'ASC'">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="applyeasy"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="applyeasy">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">gen</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">lstview</xsl:with-param>
			</xsl:call-template>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript"><![CDATA[
			itm_lst = new wbItem
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates>
			<xsl:with-param name="lab_run_info"><xsl:value-of select="$lab_const_run"/>訊息</xsl:with-param>
			<xsl:with-param name="lab_session_info">課堂資料</xsl:with-param>
			<xsl:with-param name="lab_no_session">找不到課堂</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_start_date">開始日期</xsl:with-param>
			<xsl:with-param name="lab_end_date">結果日期</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_cancel">已取消</xsl:with-param>
			<xsl:with-param name="lab_in_prog">進行中</xsl:with-param>
			<xsl:with-param name="lab_complete">已完成</xsl:with-param>
			<xsl:with-param name="lab_pending">未決定的</xsl:with-param>
			<xsl:with-param name="lab_confirm">已確認的</xsl:with-param>
			<xsl:with-param name="lab_attendance">考勤</xsl:with-param>
			<xsl:with-param name="lab_session_status">課堂狀況</xsl:with-param>
			<xsl:with-param name="lab_status_on">在線</xsl:with-param>
			<xsl:with-param name="lab_status_off">離線</xsl:with-param>			
			<xsl:with-param name="lab_g_txt_btn_new_session">新增<xsl:value-of select="$lab_const_session"/></xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates>
			<xsl:with-param name="lab_run_info"><xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_session_info">课堂资料</xsl:with-param>
			<xsl:with-param name="lab_no_session">找不到课堂</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_start_date">开始日期</xsl:with-param>
			<xsl:with-param name="lab_end_date">结束日期</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_cancel">已取消</xsl:with-param>
			<xsl:with-param name="lab_in_prog">进行中</xsl:with-param>
			<xsl:with-param name="lab_complete">已完成</xsl:with-param>
			<xsl:with-param name="lab_pending">未决定的</xsl:with-param>
			<xsl:with-param name="lab_confirm">已确认的</xsl:with-param>
			<xsl:with-param name="lab_attendance">考勤</xsl:with-param>
			<xsl:with-param name="lab_session_status">课堂状况</xsl:with-param>	
			<xsl:with-param name="lab_status_on">在线</xsl:with-param>
			<xsl:with-param name="lab_status_off">离线</xsl:with-param>					
			<xsl:with-param name="lab_g_txt_btn_new_session">添加<xsl:value-of select="$lab_const_session"/></xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates>
			<xsl:with-param name="lab_run_info"><xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
			<xsl:with-param name="lab_session_info">Session information</xsl:with-param>
			<xsl:with-param name="lab_no_session">No session found</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_start_date">Start date</xsl:with-param>
			<xsl:with-param name="lab_end_date">End date</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_cancel">Cancelled</xsl:with-param>
			<xsl:with-param name="lab_in_prog">In progress</xsl:with-param>
			<xsl:with-param name="lab_complete">Completed</xsl:with-param>
			<xsl:with-param name="lab_pending">Pending</xsl:with-param>
			<xsl:with-param name="lab_confirm">Confirmed</xsl:with-param>
			<xsl:with-param name="lab_attendance">Attendance</xsl:with-param>
			<xsl:with-param name="lab_session_status">Session status</xsl:with-param>		
			<xsl:with-param name="lab_status_on">Shown to learner</xsl:with-param>
			<xsl:with-param name="lab_status_off">Hidden from learner</xsl:with-param>				
			<xsl:with-param name="lab_g_txt_btn_new_session">New <xsl:value-of select="$lab_const_session"/></xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="item">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_session_info"/>
		<xsl:param name="lab_no_session"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_start_date"/>
		<xsl:param name="lab_end_date"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_in_prog"/>
		<xsl:param name="lab_cancel"/>
		<xsl:param name="lab_complete"/>
		<xsl:param name="lab_pending"/>
		<xsl:param name="lab_attendance"/>
		<xsl:param name="lab_session_status"/>
		<xsl:param name="lab_g_txt_btn_new_session"/>
	<xsl:param name="lab_status_on"/>
	<xsl:param name="lab_status_off"/>		
		<xsl:call-template name="wb_hdr">
			<xsl:with-param name="navigation">
				<xsl:apply-templates select="nav/item" mode="nav">
					<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
					<xsl:with-param name="lab_session_info" select="$lab_session_info"/>
				</xsl:apply-templates>

<!--			
			<a href="javascript:itm_lst.get_item_detail({@id})" class="wbGenNavigationTextBold"><xsl:value-of select="@title"/></a>-->
			<span class="wbGenNavigationTextBold"><xsl:text>&#160;&gt;&#160;</xsl:text><xsl:value-of select="$lab_session_info"/></span>
</xsl:with-param>
		</xsl:call-template>
		<table border="0" cellpadding="3" cellspacing="0" width="760">
			<tr class="wbGenButtonBarBg" height="19">
				<td>
					<span class="wbGenButtonBarText"><xsl:value-of select="$lab_session_info"/></span>
				</td>
				<td align="right">
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					<xsl:if test="$page_variant/@hasItmAddSessionBtn= 'true'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_txt_btn_new_session"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.session.ins_session_prep(<xsl:value-of select="@id"/>,'<xsl:value-of select="$ity_id"/>');</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
				</td>
			</tr>
		</table>
		<xsl:choose>
			<xsl:when test="count(child_item_list/item) = 0">
				<xsl:call-template name="show_no_item">
					<xsl:with-param name="lab_no_session"><xsl:value-of select="$lab_no_session"/></xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table cellpadding="3" cellspacing="0" border="0" width="760">
					<tr class="wbRowHeadBarBg">
						<td width="5">
							<img src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'itm_title'">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_title','sort_order','{$sort_order_by}')" class="wbRowHeadBarSorSLink">
										<xsl:value-of select="$lab_title"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
											<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_title','sort_order','ASC')" class="wbRowHeadBarSLink">
										<xsl:value-of select="$lab_title"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<xsl:if test="item_type_reference_data/@has_attendance_ind = 'true'">
							<td align="center">
								<span class="wbRowHeadBarSText"><xsl:value-of select="$lab_attendance"/></span>
							</td>
						</xsl:if>
						<td align="center">
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'itm_eff_start_datetime' ">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_eff_start_datetime','sort_order','{$sort_order_by}')" class="wbRowHeadBarSorSLink">
										<xsl:value-of select="$lab_start_date"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
											<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_eff_start_datetime','sort_order','ASC')" class="wbRowHeadBarSLink">
										<xsl:value-of select="$lab_start_date"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td align="center">
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'itm_eff_end_datetime' ">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_eff_end_datetime','sort_order','{$sort_order_by}')" class="wbRowHeadBarSorSLink">
										<xsl:value-of select="$lab_end_date"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
											<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','itm_eff_end_datetime','sort_order','ASC')" class="wbRowHeadBarSLink">
										<xsl:value-of select="$lab_end_date"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td align="center">
							<span class="wbRowHeadBarSText"><xsl:value-of select="$lab_session_status"/></span>
						</td>
						<td align="center">
							<span class="wbRowHeadBarSText"><xsl:value-of select="$lab_status"/></span>
						</td>						
						<td width="5">
							<img src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/>
						</td>
					</tr>
					<xsl:for-each select="child_item_list/item">
						<xsl:variable name="row_class">
							<xsl:choose>
								<xsl:when test="position() mod 2">wbRowsEven</xsl:when>
								<xsl:otherwise>wbRowsOdd</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<tr class="{$row_class}">
							<td width="5">
								<img src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/>
							</td>
							<td>
								<a href="javascript:itm_lst.get_item_run_detail({@item_id},'{@life_status}')" class="wbRowText">
									<xsl:value-of select="title"/>
								</a>
							</td>
							<xsl:if test="../../item_type_reference_data/@has_attendance_ind = 'true'">
								<td align="center">
									<span class="wbRowText"><xsl:for-each select="attendance_count_list/count"><xsl:if test="position() != 1">; </xsl:if><xsl:value-of select="."/>&#160;<xsl:call-template name="get_attendance_name"><xsl:with-param name="attendance_status" select="@status_id"/></xsl:call-template></xsl:for-each><xsl:if test="count(attendance_count_list/count) = 0">--</xsl:if></span>
								</td>
							</xsl:if>
							<td align="center">
								<span class="wbRowText"><xsl:call-template name="trun_date"><xsl:with-param name="my_timestamp"><xsl:value-of select="@eff_start_datetime"/></xsl:with-param></xsl:call-template></span>
							</td>
							<td align="center">
								<span class="wbRowText"><xsl:call-template name="trun_date"><xsl:with-param name="my_timestamp"><xsl:value-of select="@eff_end_datetime"/></xsl:with-param></xsl:call-template></span>
							</td>
							<td align="center">
								<span class="wbRowText"><xsl:choose><xsl:when test="@life_status=''"><xsl:value-of select="$lab_in_prog"/></xsl:when><xsl:when test="@life_status='CANCELLED'"><xsl:value-of select="$lab_cancel"/></xsl:when><xsl:when test="@life_status='COMPLETED'"><xsl:value-of select="$lab_complete"/></xsl:when><xsl:otherwise><xsl:value-of select="@life_status"/></xsl:otherwise></xsl:choose></span>
							</td>							
							<td align="center">
								<span class="wbRowText">
<xsl:choose>
											<xsl:when test="@status = 'ON'"><xsl:value-of select="$lab_status_on"/></xsl:when>
											<xsl:when test="@status = 'OFF'"><xsl:value-of select="$lab_status_off"/></xsl:when>
										</xsl:choose>
								</span>
							</td>
							<td width="5">
								<img src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/>
							</td>
						</tr>
					</xsl:for-each>
				</table>
			</xsl:otherwise>
		</xsl:choose>
		<table border="0" cellpadding="3" cellspacing="0" width="760">
			<tr class="wbGenButtonBarBg" height="19">
				<td>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</table>
	</xsl:template>
		
	<!-- =============================================================== -->

		<xsl:template match="item" mode="nav">
	<xsl:param name="lab_run_info"/>
	<xsl:param name="lab_session_info"/>
			<xsl:choose>
			<xsl:when test="@run_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=last()"><xsl:value-of select="@id"/></xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.get_item_run_list({$value})" class="wbGenNavigationTextBold"><xsl:choose>
						<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
					</xsl:choose></a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="javascript:itm_lst.get_item_run_detail({@id})" class="wbGenNavigationTextBold"><xsl:value-of select="title"/></a>
			</xsl:when>
			<xsl:when test="@session_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=last()"><xsl:value-of select="@id"/></xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.session.get_session_list({$value})" class="wbGenNavigationTextBold"><xsl:value-of select="$lab_session_info"/></a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="javascript:itm_lst.get_item_run_detail({@id})" class="wbGenNavigationTextBold"><xsl:value-of select="title"/></a>
			</xsl:when>
			<xsl:otherwise>
						<a href="javascript:itm_lst.get_item_detail({@id})" class="wbGenNavigationTextBold"><xsl:value-of select="title"/></a>
			</xsl:otherwise>
			</xsl:choose>
	</xsl:template>

	<!-- =============================================================== -->
	<xsl:template name="show_no_item">
		<xsl:param name="lab_no_session"/>
		<table cellpadding="3" cellspacing="0" border="0" width="760">
			<tr class="wbRowsOdd">
				<td height="10">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
			<tr class="wbRowsOdd">
				<td align="center">
					<span class="wbRowHeadBarText"><xsl:value-of select="$lab_no_session"/></span>
				</td>
			</tr>
			<tr class="wbRowsOdd">
				<td height="10">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="get_attendance_name">
		<xsl:param name="attendance_status"/>
		<xsl:value-of select="/applyeasy/meta/attendance_status_list/status[@id = $attendance_status]/desc[@lan = $wb_lang_encoding]/@name"/>
	</xsl:template>
</xsl:stylesheet>
