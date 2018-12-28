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
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
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
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_cata_lst.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			itm_lst = new wbItem
			//cata_lst = new wbCataLst
						
			//gen_set_cookie('url_success',self.location.href)
			function del_confirm(id, upd_time){
				if(window.confirm(wb_msg_confirm)){
					frmXml.upd_timestamp.value = upd_time;
					itm_lst.ae_upd_course_lesson(frmXml, id, 'delete');
				}
			}
		]]></script>
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
			<xsl:with-param name="lab_desc">設定此課程的日程表，由多個指定日期和時間的培訓單元組成。此處設定的日程表會成為其後建立的班級的默認日程表，但不會影響到之前已建立的班級。</xsl:with-param>
			<xsl:with-param name="lab_day_l">第</xsl:with-param>
			<xsl:with-param name="lab_day_r">天</xsl:with-param>
			<xsl:with-param name="lab_day">天數</xsl:with-param>
			<xsl:with-param name="lab_start_time">開始時間</xsl:with-param>
			<xsl:with-param name="lab_end_time">結束時間</xsl:with-param>
			<xsl:with-param name="lab_l_name">標題</xsl:with-param>
			<xsl:with-param name="lab_l_txt_btn_add">新增培訓單元</xsl:with-param>
			<xsl:with-param name="lab_l_lst_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_l_lst_btn_del">刪除</xsl:with-param>
			<xsl:with-param name="lab_no_lesson">沒有培訓單元</xsl:with-param>
			
			<xsl:with-param name="lab_riqi">日期</xsl:with-param>
			<xsl:with-param name="lab_chidao_time">迟到</xsl:with-param>
			<xsl:with-param name="lab_queqin_time">缺勤</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_youxiaoqi">有效签到时间</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">日程表</xsl:with-param>
			<xsl:with-param name="lab_desc">设定此课程的日程表，由多个指定日期和时间的培训单元组成。此处设定的日程表会成为其后建立的班级的默认日程表，但不会影响到之前已建立的班级。</xsl:with-param>
			<xsl:with-param name="lab_day_l">第</xsl:with-param>
			<xsl:with-param name="lab_day_r">天</xsl:with-param>
			<xsl:with-param name="lab_day">天数</xsl:with-param>
			<xsl:with-param name="lab_start_time">开始时间</xsl:with-param>
			<xsl:with-param name="lab_end_time">结束时间</xsl:with-param>
			<xsl:with-param name="lab_l_name">标题</xsl:with-param>
			<xsl:with-param name="lab_l_txt_btn_add">添加培训单元</xsl:with-param>
			<xsl:with-param name="lab_l_lst_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_l_lst_btn_del">删除</xsl:with-param>
			<xsl:with-param name="lab_no_lesson">没有培训单元</xsl:with-param>
			
			<xsl:with-param name="lab_riqi">日期</xsl:with-param>
			<xsl:with-param name="lab_chidao_time">迟到</xsl:with-param>
			<xsl:with-param name="lab_queqin_time">缺勤</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_youxiaoqi">有效签到时间</xsl:with-param>
			
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">Timetable</xsl:with-param>
			<xsl:with-param name="lab_desc">Define the default timetable of the course here. A timetable is consisted of multiple training units with their date and time information. Once defined here, it will become the default timetable of the classes of this course. Note that making changes here will affect the classes to be created thereafter, but will not affect the classes created in the past.</xsl:with-param>
			<xsl:with-param name="lab_day_l"/>
			<xsl:with-param name="lab_day_r"/>
			<xsl:with-param name="lab_day">Day</xsl:with-param>
			<xsl:with-param name="lab_start_time">Start time</xsl:with-param>
			<xsl:with-param name="lab_end_time">End time</xsl:with-param>
			<xsl:with-param name="lab_l_name">Title</xsl:with-param>
			<xsl:with-param name="lab_l_txt_btn_add">Add training unit</xsl:with-param>
			<xsl:with-param name="lab_l_lst_btn_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_l_lst_btn_del">Remove</xsl:with-param>
			<xsl:with-param name="lab_no_lesson">No training units found</xsl:with-param>
			
			<xsl:with-param name="lab_riqi">Date</xsl:with-param>
			<xsl:with-param name="lab_chidao_time">Late</xsl:with-param>
			<xsl:with-param name="lab_queqin_time">Absent</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_youxiaoqi">Valid attendance period</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_desc"/>
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
		<xsl:param name="lab_riqi"/>
		<xsl:param name="lab_chidao_time"/>
		<xsl:param name="lab_queqin_time"/>
		<xsl:param name="lab_ils_qiandao_youxiaoqi"/>
		
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
	    <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">108</xsl:with-param>
		</xsl:call-template>
        <div class="wzb-item-main">
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text"><xsl:value-of select="//itm_action_nav/@itm_title"/></xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_desc"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="//itm_action_nav/hasTeachingCourse/text()='true'">
						<a href="javascript:itm_lst.get_itm_instr_view({//itm_action_nav/@itm_id})" class="NavLink">
							<xsl:value-of select="//itm_action_nav/@itm_title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a href="javascript:itm_lst.get_item_detail({/applyeasy/item/@id})" class="NavLink">
							<xsl:value-of select="/applyeasy/item/title"/>
						</a>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:value-of select="$lab_title"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:if test="meta/page_variant/@hasForAddBtn='true'">
			<xsl:call-template name="wb_ui_line"/>
		</xsl:if>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="extra_td">
				<td align="right">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_l_txt_btn_add"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">Javascript:itm_lst.ae_upd_course_lesson(frmXml,'<xsl:value-of select="$itm_id"/>','new')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
					</xsl:call-template>
				</td>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="count(itm_lessons/lesson)=0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_lesson"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td align="center" width="12%">
							<xsl:value-of select="$lab_riqi"/>
						</td>
						<td align="center" width="10%">
							<xsl:value-of select="$lab_start_time"/>
						</td>
						<td align="center" width="10%">
							<xsl:value-of select="$lab_end_time"/>
						</td>
						<td align="center" width="18%">
							<xsl:value-of select="$lab_l_name"/>
						</td>
						<td align="center" width="10%">
							<xsl:value-of select="$lab_chidao_time"/>
						</td>
						<td align="center" width="10%">
							<xsl:value-of select="$lab_queqin_time"/>
						</td>
						<td align="center" width="12%">
							<xsl:value-of select="$lab_ils_qiandao_youxiaoqi"/>
						</td>
						<td width="18%">
						</td>
					</tr>
					<xsl:apply-templates select="/applyeasy/itm_lessons/lesson">
						<xsl:with-param name="lab_l_lst_btn_edit" select="$lab_l_lst_btn_edit"/>
						<xsl:with-param name="lab_l_lst_btn_del" select="$lab_l_lst_btn_del"/>
					</xsl:apply-templates>
				</table>
			</xsl:otherwise>
		</xsl:choose>
		<input type="hidden" name="cmd"/>
		<input type="hidden" name="url_success"/>
		<input type="hidden" name="url_failure"/>
		<input type="hidden" name="upd_timestamp" value=""/>
		<input type="hidden" name="itm_id" value="{$itm_id}"/>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="/applyeasy/itm_lessons/lesson">
		<xsl:param name="lab_l_lst_btn_edit"/>
		<xsl:param name="lab_l_lst_btn_del"/>
		<xsl:param name="lab_day_l"/>
		<xsl:param name="lab_day_r"/>
		<tr>
			<td align="center">
				<xsl:value-of select="@ils_date"/>
			</td>
			<td align="center">
				<xsl:call-template name="display_hhmm_sample">
					<xsl:with-param name="my_timestamp" select="@start_time"/>
				</xsl:call-template>
				<!--<xsl:value-of select="@start_time"/>-->
			</td>
			<td align="center">
				<xsl:call-template name="display_hhmm_sample">
					<xsl:with-param name="my_timestamp" select="@end_time"/>
				</xsl:call-template>
				<!--<xsl:value-of select="@end_time"/>-->
			</td>
			<td align="center">
				<input type="hidden" name="{concat('lab_title_', @ils_id)}" value="{title}"></input>
				<xsl:value-of select="title"/>
			</td>
			<xsl:choose>
				<xsl:when test="@ils_qiandao = '1'">
					<td align="center">
						<xsl:call-template name="display_hhmm_sample">
							<xsl:with-param name="my_timestamp" select="@ils_qiandao_chidao"/>
						</xsl:call-template>
					</td>
					<td align="center">
						<xsl:call-template name="display_hhmm_sample">
							<xsl:with-param name="my_timestamp" select="@ils_qiandao_queqin"/>
						</xsl:call-template>
					</td>
					<td align="center">
						<xsl:call-template name="display_hhmm_sample">
							<xsl:with-param name="my_timestamp" select="@ils_qiandao_youxiaoqi"/>
						</xsl:call-template>
						<xsl:text>-</xsl:text>
						<xsl:call-template name="display_hhmm_sample">
							<xsl:with-param name="my_timestamp" select="@end_time"/>
						</xsl:call-template>
					</td>
				</xsl:when>
				<xsl:otherwise>
					<td align="center">
						--
					</td>
					<td align="center">
						--
					</td>
					<td align="center">
						--
					</td>
				</xsl:otherwise>
			</xsl:choose>
			
			<td align="right">
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name">
						<xsl:value-of select="$lab_l_lst_btn_edit"/>
					</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.ae_upd_course_lesson(frmXml, '<xsl:value-of select="@ils_id"/>','edit')</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_gen_button">	
					<xsl:with-param name="class">btn wzb-btn-blue</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_name">
						<xsl:value-of select="$lab_l_lst_btn_del"/>
					</xsl:with-param>
					<!--<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.ae_upd_course_lesson(frmXml, '<xsl:value-of select="@ils_id"/>','delete')</xsl:with-param>-->
					<xsl:with-param name="wb_gen_btn_href">javascript:del_confirm('<xsl:value-of select="@ils_id"/>','<xsl:value-of select="last_updated/@timestamp"/>')</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
				</xsl:call-template>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
