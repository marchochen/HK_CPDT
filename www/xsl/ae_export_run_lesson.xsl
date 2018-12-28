<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/display_hhmm.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="itm_apply_ind" select="/applyeasy/item/@apply_ind"/>
	<xsl:variable name="run_ind" select="/applyeasy/item/@run_ind"/>
	<xsl:variable name="session_ind" select="/applyeasy/item/@session_ind"/>
	<!-- =============================================================== -->
	<xsl:template match="/applyeasy">
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
			<!--<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			-->
			<style type="text/css">
				.Title{
					color: #000000;
					font: 13pt Verdana,Geneva,Arial,Helvetica,sans-serif;
				}
				.bg{
					background-color: #CCCCCC;
				}
			</style>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">日程表</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_day_l">第</xsl:with-param>
			<xsl:with-param name="lab_day_r">天</xsl:with-param>
			<xsl:with-param name="lab_day">天數</xsl:with-param>
			<xsl:with-param name="lab_start_time">開始時間</xsl:with-param>
			<xsl:with-param name="lab_end_time">結束時間</xsl:with-param>
			<xsl:with-param name="lab_l_name">標題</xsl:with-param>
			<xsl:with-param name="lab_l_txt_btn_add">新增單元</xsl:with-param>
			<xsl:with-param name="lab_l_lst_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_l_lst_btn_del">刪除</xsl:with-param>
			<xsl:with-param name="lab_no_lesson">沒有培訓單元</xsl:with-param>
			<xsl:with-param name="lab_btn_edit_date">設定班別日期</xsl:with-param>
			<xsl:with-param name="lab_btn_edit_teacher">設定講師</xsl:with-param>
			<xsl:with-param name="lab_teacher_name">講師</xsl:with-param>
			<xsl:with-param name="lab_btn_edit_time">修改</xsl:with-param>
			<xsl:with-param name="lab_btn_export">滙岀</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">日程表</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_day_l">第</xsl:with-param>
			<xsl:with-param name="lab_day_r">天</xsl:with-param>
			<xsl:with-param name="lab_day">天数</xsl:with-param>
			<xsl:with-param name="lab_start_time">开始时间</xsl:with-param>
			<xsl:with-param name="lab_end_time">结束时间</xsl:with-param>
			<xsl:with-param name="lab_l_name">标题</xsl:with-param>
			<xsl:with-param name="lab_l_txt_btn_add">新增单元</xsl:with-param>
			<xsl:with-param name="lab_l_lst_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_l_lst_btn_del">删除</xsl:with-param>
			<xsl:with-param name="lab_no_lesson">没有培训单元</xsl:with-param>
			<xsl:with-param name="lab_teacher_name">讲师</xsl:with-param>
			<xsl:with-param name="lab_btn_edit_date">设定班别日期</xsl:with-param>
			<xsl:with-param name="lab_btn_edit_teacher">设定讲师</xsl:with-param>
			<xsl:with-param name="lab_btn_edit_time">修改</xsl:with-param>
			<xsl:with-param name="lab_btn_export">汇岀</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">Timetable</xsl:with-param>
			<xsl:with-param name="lab_date">Date</xsl:with-param>
			<xsl:with-param name="lab_day_l"/>
			<xsl:with-param name="lab_day_r"/>
			<xsl:with-param name="lab_day">Day</xsl:with-param>
			<xsl:with-param name="lab_start_time">Start time</xsl:with-param>
			<xsl:with-param name="lab_end_time">End time</xsl:with-param>
			<xsl:with-param name="lab_l_name">Title</xsl:with-param>
			<xsl:with-param name="lab_l_txt_btn_add">Add new</xsl:with-param>
			<xsl:with-param name="lab_l_lst_btn_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_l_lst_btn_del">Delete</xsl:with-param>
			<xsl:with-param name="lab_no_lesson">No training units</xsl:with-param>
			<xsl:with-param name="lab_teacher_name">Lecturer</xsl:with-param>
			<xsl:with-param name="lab_btn_edit_date">Set date</xsl:with-param>
			<xsl:with-param name="lab_btn_edit_teacher">Assign lecturer</xsl:with-param>
			<xsl:with-param name="lab_btn_edit_time">Edit</xsl:with-param>
			<xsl:with-param name="lab_btn_export">Export</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_day_l"/>
		<xsl:param name="lab_day_r"/>
		<xsl:param name="lab_day"/>
		<xsl:param name="lab_start_time"/>
		<xsl:param name="lab_end_time"/>
		<xsl:param name="lab_l_name"/>
		<xsl:param name="lab_l_txt_btn_add"/>
		<xsl:param name="lab_l_lst_btn_edit"/>
		<xsl:param name="lab_l_lst_btn_del"/>
		<xsl:param name="lab_no_lesson"/>
		<xsl:param name="lab_btn_edit_date"/>
		<xsl:param name="lab_btn_edit_teacher"/>
		<xsl:param name="lab_btn_edit_time"/>
		<xsl:param name="lab_btn_export"/>
		<xsl:param name="lab_teacher_name"/>
		<table width="{$wb_gen_table_width}">
			<tr>
				<td class="Title" colspan="6">
					<xsl:for-each select="item/nav/item">
						<xsl:value-of select="title"/>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
					</xsl:for-each>
					<xsl:value-of select="$lab_title"/>
				</td>
			</tr>
		</table>
		<xsl:choose>
			<xsl:when test="count(itm_lessons/lesson)=0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="top_line">false</xsl:with-param>
					<xsl:with-param name="bottom_line">false</xsl:with-param>
					<xsl:with-param name="text" select="$lab_no_lesson"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table cellpadding="3" cellspacing="0" border="1" width="{$wb_gen_table_width}">
					<tr class="bg">
						<td align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_day"/>
							</span>
						</td>
						<td align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_date"/>
							</span>
						</td>
						<td align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_start_time"/>
							</span>
						</td>
						<td align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_end_time"/>
							</span>
						</td>
						<td align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_l_name"/>
							</span>
						</td>
						<td align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_teacher_name"/>
							</span>
						</td>
					</tr>
					<xsl:apply-templates select="/applyeasy/itm_lessons/lesson">
						<xsl:with-param name="lab_day_l" select="$lab_day_l"/>
						<xsl:with-param name="lab_day_r" select="$lab_day_r"/>
						<xsl:with-param name="lab_l_lst_btn_edit" select="$lab_l_lst_btn_edit"/>
						<xsl:with-param name="lab_l_lst_btn_del" select="$lab_l_lst_btn_del"/>
						<xsl:with-param name="lab_btn_edit_teacher" select="$lab_btn_edit_teacher"/>
					</xsl:apply-templates>
				</table>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="/applyeasy/itm_lessons/lesson">
		<xsl:param name="lab_l_lst_btn_edit"/>
		<xsl:param name="lab_l_lst_btn_del"/>
		<xsl:param name="lab_day_l"/>
		<xsl:param name="lab_day_r"/>
		<xsl:param name="lab_btn_edit_teacher"/>
		<tr>
			<xsl:attribute name="class"><xsl:choose><xsl:when test="position() mod 2=1">RowsOdd</xsl:when><xsl:otherwise>RowsEven</xsl:otherwise></xsl:choose></xsl:attribute>
			<td align="center">
				<span class="Text">
					<xsl:value-of select="$lab_day_l"/>
					<xsl:value-of select="@day"/>
					<xsl:value-of select="$lab_day_r"/>
				</span>
			</td>
			<td align="center">
				<span class="TitleText">
					<xsl:choose>
						<xsl:when test="substring-before(@start_time,' ') != '1900-01-01'">
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp" select="@start_time"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>- -</xsl:otherwise>
					</xsl:choose>
				</span>
			</td>
			<td align="center">
				<span class="Text">
					<xsl:call-template name="display_hhmm">
						<xsl:with-param name="my_timestamp" select="@start_time"/>
					</xsl:call-template>
					<!--<xsl:value-of select="@start_time"/>-->
				</span>
			</td>
			<td align="center">
				<span class="Text">
					<xsl:call-template name="display_hhmm">
						<xsl:with-param name="my_timestamp" select="@end_time"/>
					</xsl:call-template>
					<!--<xsl:value-of select="@end_time"/>-->
				</span>
			</td>
			<td align="left">
				<span class="Text">
					<xsl:value-of select="title"/>
				</span>
			</td>
			<td align="center">
				<span class="Text">
					<xsl:choose>
						<xsl:when test="count(teachers/teacher) = 0">- -</xsl:when>
						<xsl:otherwise>
							<table cellpadding="2" cellspacing="0" border="0">
								<xsl:for-each select="teachers/teacher">
									<tr>
										<td>
											<xsl:value-of select="text()"/>
										</td>
									</tr>
								</xsl:for-each>
							</table>
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
