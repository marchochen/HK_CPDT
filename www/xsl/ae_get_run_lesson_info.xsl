<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/escape_all.xsl"/>
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
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	
	<xsl:variable name="itm_inst_type" select="/applyeasy/item/@itm_inst_type"/>
	
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="tcr_id" select="/applyeasy/item/@tcr_id"/>
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
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_cata_lst.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_instructor.js" />
			<script language="JavaScript"><![CDATA[
			itm_lst = new wbItem
			instr = new wbInstructor;
			//cata_lst = new wbCataLst
						
			//gen_set_cookie('url_success',self.location.href)
			function del_confirm(id, upd_time){
				if(window.confirm(wb_msg_confirm)){
					frmXml.upd_timestamp.value = upd_time;
					itm_lst.ae_upd_run_lesson(frmXml, id, 'delete');
				}
			}
			function need_set_date(){
				alert(wb_msg_ils_need_date);
			}
			function no_instructor(){
				alert(wb_msg_ils_no_instructor);
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
			<xsl:with-param name="lab_desc"><![CDATA[
				以下是此班別的日程表，妳可設定講師，然後滙岀日程表。
			]]></xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>訊息</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
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
			<xsl:with-param name="lab_btn_edit_date">設定日期</xsl:with-param>
			<xsl:with-param name="lab_btn_edit_teacher">指定講師</xsl:with-param>
			<xsl:with-param name="lab_btn_del_teacher">刪除講師</xsl:with-param>
			<xsl:with-param name="lab_teacher_name">講師</xsl:with-param>
			<xsl:with-param name="lab_btn_edit_time">修改</xsl:with-param>
			<xsl:with-param name="lab_btn_export">匯出</xsl:with-param>
			
			<xsl:with-param name="lab_riqi">日期</xsl:with-param>
			<xsl:with-param name="lab_chidao_time">遲到</xsl:with-param>
			<xsl:with-param name="lab_queqin_time">缺勤</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_youxiaoqi">有效簽到時間</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">日程表</xsl:with-param>
			<xsl:with-param name="lab_desc"><![CDATA[
				以下是此班级的日程表，你可设定讲师，然后导岀日程表。
			]]></xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
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
			<xsl:with-param name="lab_teacher_name">讲师</xsl:with-param>
			<xsl:with-param name="lab_btn_edit_date">设定日期</xsl:with-param>
			<xsl:with-param name="lab_btn_edit_teacher">指定讲师</xsl:with-param>
			<xsl:with-param name="lab_btn_del_teacher">删除讲师</xsl:with-param>
			<xsl:with-param name="lab_btn_edit_time">修改</xsl:with-param>
			<xsl:with-param name="lab_btn_export">导岀</xsl:with-param>
			
			<xsl:with-param name="lab_riqi">日期</xsl:with-param>
			<xsl:with-param name="lab_chidao_time">迟到</xsl:with-param>
			<xsl:with-param name="lab_queqin_time">缺勤</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_youxiaoqi">有效签到时间</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">Timetable</xsl:with-param>
			<xsl:with-param name="lab_desc"><![CDATA[This is the class timetable. You can set lecturer and export the timetable here.]]></xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>information</xsl:with-param>
			<xsl:with-param name="lab_date">Date</xsl:with-param>
			<xsl:with-param name="lab_day_l"/>
			<xsl:with-param name="lab_day_r"/>
			<xsl:with-param name="lab_day">Day</xsl:with-param>
			<xsl:with-param name="lab_start_time">Start time</xsl:with-param>
			<xsl:with-param name="lab_end_time">End time</xsl:with-param>
			<xsl:with-param name="lab_l_name">Title</xsl:with-param>
			<xsl:with-param name="lab_l_txt_btn_add">Add Session</xsl:with-param>
			<xsl:with-param name="lab_l_lst_btn_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_l_lst_btn_del">Remove</xsl:with-param>
			<xsl:with-param name="lab_no_lesson">No training units found</xsl:with-param>
			<xsl:with-param name="lab_teacher_name">Lecturer</xsl:with-param>
			<xsl:with-param name="lab_btn_edit_date">Set date</xsl:with-param>
			<xsl:with-param name="lab_btn_edit_teacher">Assign lecturer</xsl:with-param>
			<xsl:with-param name="lab_btn_del_teacher">Remove lecturer</xsl:with-param>
			<xsl:with-param name="lab_btn_edit_time">Edit</xsl:with-param>
			<xsl:with-param name="lab_btn_export">Export</xsl:with-param>
			
			<xsl:with-param name="lab_riqi">Date</xsl:with-param>
			<xsl:with-param name="lab_chidao_time">Late</xsl:with-param>
			<xsl:with-param name="lab_queqin_time">Absent</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_youxiaoqi">Valid time for attendance sign in</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_run_info"/>
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
		<xsl:param name="lab_btn_del_teacher"/>
		<xsl:param name="lab_btn_edit_time"/>
		<xsl:param name="lab_btn_export"/>
		<xsl:param name="lab_teacher_name"/>
		
		<xsl:param name="lab_riqi"/>
		<xsl:param name="lab_chidao_time"/>
		<xsl:param name="lab_queqin_time"/>
		<xsl:param name="lab_ils_qiandao_youxiaoqi"/>
	<xsl:call-template name="itm_action_nav">
		<xsl:with-param  name="cur_node_id">108</xsl:with-param>
	</xsl:call-template>
	<div class="wzb-item-main">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_desc"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="@run_ind = 'false'">
						<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
							<xsl:value-of select="title"/>
						</a>&#160;&gt;&#160;<xsl:value-of select="$lab_title"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="item/nav/item" mode="nav">
							<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
							<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
						</xsl:apply-templates>
						<span class="NavLink">
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<xsl:value-of select="$lab_title"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:if test="meta/page_variant/@hasForAddBtn='true'">
			<xsl:call-template name="wb_ui_line"/>
		</xsl:if>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="extra_td">
				<td align="right">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_l_txt_btn_add"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">Javascript:itm_lst.ae_upd_run_lesson(frmXml,'<xsl:value-of select="$itm_id"/>','new')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
					</xsl:call-template>
					<!--  <xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_btn_edit_date"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">Javascript:itm_lst.ae_upd_run_lesson(frmXml,'<xsl:value-of select="$itm_id"/>','set_date')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
					</xsl:call-template>-->
					
				
					
				</td>
			</xsl:with-param>
	        <xsl:with-param name="table_style"></xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="count(itm_lessons/lesson)=0">
		    	<xsl:call-template name="wb_ui_line"/>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_lesson"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td width="12%">
							<xsl:value-of select="$lab_riqi"/>
						</td>
						<td width="8%" align="center">
							<xsl:value-of select="$lab_start_time"/>
						</td>
						<td width="8%" align="center">
							<xsl:value-of select="$lab_end_time"/>
						</td>
						<td width="15%" align="center">
							<xsl:value-of select="$lab_l_name"/>
						</td>
						<td align="center" width="8%">
							<xsl:value-of select="$lab_chidao_time"/>
						</td>
						<td align="center" width="8%">
							<xsl:value-of select="$lab_queqin_time"/>
						</td>
						<td align="center" width="12%">
							<xsl:value-of select="$lab_ils_qiandao_youxiaoqi"/>
						</td>
						<td width="10%" align="center">
							<xsl:value-of select="$lab_teacher_name"/>
						</td>
						<td width="19%">
						</td>
					</tr>
					<xsl:apply-templates select="/applyeasy/itm_lessons/lesson">
						<xsl:with-param name="lab_day_l" select="$lab_day_l"/>
						<xsl:with-param name="lab_day_r" select="$lab_day_r"/>
						<xsl:with-param name="lab_l_lst_btn_edit" select="$lab_l_lst_btn_edit"/>
						<xsl:with-param name="lab_l_lst_btn_del" select="$lab_l_lst_btn_del"/>
						<xsl:with-param name="lab_btn_edit_teacher" select="$lab_btn_edit_teacher"/>
						<xsl:with-param name="lab_btn_del_teacher" select="$lab_btn_del_teacher"/>
						<xsl:with-param name="lab_btn_export" select="$lab_btn_export"/><!-- 导出日程表 -->
					</xsl:apply-templates>
				</table>
			</xsl:otherwise>
		</xsl:choose>
		<input type="hidden" name="cmd"/>
		<input type="hidden" name="url_success"/>
		<input type="hidden" name="url_failure"/>
		<input type="hidden" name="upd_timestamp" value=""/>
		<input type="hidden" name="itm_id" value="{$itm_id}"/>
		<input type="hidden" name="itm_tcr_id" value="{$tcr_id}"/>
	</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="/applyeasy/itm_lessons/lesson">
		<xsl:param name="lab_l_lst_btn_edit"/>
		<xsl:param name="lab_l_lst_btn_del"/>
		<xsl:param name="lab_day_l"/>
		<xsl:param name="lab_day_r"/>
		<xsl:param name="lab_btn_edit_teacher"/>
		<xsl:param name="lab_btn_del_teacher"/>
		<xsl:param name="lab_btn_export"/><!-- 导出日程表 -->
		<tr>
			<xsl:attribute name="class"><xsl:choose><xsl:when test="position() mod 2=1">RowsOdd</xsl:when><xsl:otherwise>RowsEven</xsl:otherwise></xsl:choose></xsl:attribute>
			<td>
				<xsl:value-of select="@ils_date"/>
			</td>
			<td align="center">
				<xsl:call-template name="display_hhmm_sample">
					<xsl:with-param name="my_timestamp" select="@start_time"/>
				</xsl:call-template>
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
			<td align="center">
				<xsl:choose>
					<xsl:when test="count(teachers/teacher) = 0">--</xsl:when>
					<xsl:otherwise>
					<xsl:for-each select="teachers/teacher">
						<xsl:value-of select="text()"/><br/>
					</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td width="40%" align="right" nowrap="nowrap">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-blue margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_btn_export"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">Javascript:itm_lst.ae_upd_run_lesson(frmXml,'<xsl:value-of select="@ils_id"/>','export_lesson','<xsl:value-of select="$itm_id"/>')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
					</xsl:call-template>
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name">
						<xsl:value-of select="$lab_btn_edit_teacher"/>
					</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">
						<xsl:choose>      
					                                                                
						<xsl:when test="substring-before(@start_time,'-') != '1900'">javascript:instr.int_search_poup(0,'','','','<xsl:value-of select="$itm_inst_type"/>', 'ADD' ,'<xsl:value-of select="@ils_id"/>')
						</xsl:when>
							<xsl:otherwise>javascript:need_set_date();</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name">
						<xsl:value-of select="$lab_btn_del_teacher"/>
					</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">
						<xsl:choose>
							<xsl:when test="count(teachers/teacher) != 0">javascript:instr.int_search_poup(0,'','','','','DEL','<xsl:value-of select="@ils_id"/>')</xsl:when>
							<xsl:otherwise>javascript:no_instructor();</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name">
						<xsl:value-of select="$lab_l_lst_btn_edit"/>
					</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.ae_upd_run_lesson(frmXml, '<xsl:value-of select="@ils_id"/>','edit')</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="class">btn wzb-btn-blue</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_name">
						<xsl:value-of select="$lab_l_lst_btn_del"/>
					</xsl:with-param>
					<!--						<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.ae_upd_course_lesson(frmXml, '<xsl:value-of select="@ils_id"/>','delete')</xsl:with-param>-->
					<xsl:with-param name="wb_gen_btn_href">javascript:del_confirm('<xsl:value-of select="@ils_id"/>','<xsl:value-of select="last_updated/@timestamp"/>')</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
				</xsl:call-template>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="item" mode="nav">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_session_info"/>
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
					<xsl:choose>
						<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
					</xsl:choose>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:when test="@session_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=$_count">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.session.get_session_list({$value})" class="NavLink">
					<xsl:value-of select="$lab_session_info"/>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a href="javascript:itm_lst.get_item_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
