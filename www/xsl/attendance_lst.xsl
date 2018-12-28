<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_win_hdr.xsl"/>
	<xsl:import href="utils/wb_form_select_action.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl "/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="share/rpt_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="utils/display_score.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
    <xsl:import href="share/itm_gen_action_nav_share.xsl"/>
    
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="page_variant" select="/applyeasy/meta/page_variant"/>
	<!-- paginatoin variables -->
	<xsl:variable name="page_size" select="/applyeasy/attendance_maintance/pagination/@page_size"/>
	<xsl:variable name="cur_page" select="/applyeasy/attendance_maintance/pagination/@cur_page"/>
	<xsl:variable name="total" select="/applyeasy/attendance_maintance/pagination/@total_rec"/>
	<xsl:variable name="search_code" select="/applyeasy/attendance_maintance/@search_code"/>
	<!-- sorting variable -->
	<xsl:variable name="cur_sort_col" select="/applyeasy/attendance_maintance/pagination/@sort_col"/>
	<xsl:variable name="cur_sort_order" select="/applyeasy/attendance_maintance/pagination/@sort_order"/>
	<xsl:variable name="sort_order_by">
		<xsl:choose>
			<xsl:when test="$cur_sort_order = 'ASC'">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="itm_id" select="/applyeasy/attendance_maintance/item/@id"/>
	<xsl:variable name="create_session_ind" select="/applyeasy/attendance_maintance/item/@create_session_ind"/>
	<xsl:variable name="session_ind" select="/applyeasy/attendance_maintance/item/@session_ind"/>
	<xsl:variable name="curr_attd_id">
		<xsl:value-of select="/applyeasy/attendance_maintance/status_info/@current_id"/>
	</xsl:variable>
	<xsl:variable name="curr_attd_status">
		<xsl:choose>
			<xsl:when test="$curr_attd_id = 0 or $curr_attd_id = -1">
				<xsl:value-of select="$lab_all"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="get_ats_title">
					<xsl:with-param name="ats_id" select="$curr_attd_id"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="curr_attd_idx">
		<xsl:choose>
			<xsl:when test="$curr_attd_id = 0 or $curr_attd_id = -1">0</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="/applyeasy/attendance_maintance/status_info/status_list/status">
					<xsl:if test="@id = $curr_attd_id">
						<xsl:value-of select="position()"/>
					</xsl:if>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="is_integrated" select="/applyeasy/attendance_maintance/item/@itm_integrated_ind"/>
	<xsl:variable name="lab_862" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN085')"/>
	<xsl:variable name="current_role" select="/applyeasy/current_role"/>
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
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" TYPE="text/javascript"><![CDATA[
		var attd = new wbAttendance
		var itm_lst = new wbItem
		cur_page = ]]><xsl:value-of select="$cur_page"/><![CDATA[

		function status(){
			return false;
		}
		
		function reload_me(){
			window.location.reload();
		}
		
		function init(){
			frm = document.frmXml;
			var status_select = $("select[name='get_attd_status']")[0];
			if(']]><xsl:value-of select="$curr_attd_idx"/><![CDATA['=='0'){
				$(status_select).val('-1');
				$('#att_status').val('-1');
			}else{
				$(status_select).val(']]><xsl:value-of select="$curr_attd_idx"/><![CDATA[');
				$('#att_status').val(']]><xsl:value-of select="$curr_attd_idx"/><![CDATA[');
			}
			wb_utils_set_cookie('maintain_attd_url_prev',self.location.href)
		}		

				function doFeedParam(){
					param = new Array();
					tmpObj1 = new Array();
					tmpObj2 = new Array();
					tmpObj3 = new Array();
					tmpObj4 = new Array();
					tmpObj5 = new Array();
					
					tmpObj1[0] = 'cmd';
					tmpObj1[1] = 'ae_upd_multi_att_status';
					param[param.length] = tmpObj1;
					
					tmpObj2[0] = 'app_id_lst';
					app_id_lst = _wbAttendanceGetAppIdCheckLst(document.frmXml);
					tmpObj2[1] = app_id_lst.split("~");
					param[param.length] = tmpObj2;
					
					//tmpObj3[0] = 'app_nm_lst';
					//app_nm_lst = _wbAttendanceGetAppNmCheckLst(document.frmXml);
					//tmpObj3[1] = app_nm_lst.split("~");
					//param[param.length] = tmpObj3;
					
					tmpObj4[0] = 'att_status';
					tmpObj4[1] = document.frmXml.att_status.value;
					param[param.length] = tmpObj4;
					
					tmpObj5[0] = 'winName';
					tmpObj5[1] = 'cos_lrn_lst';
					param[param.length] = tmpObj5;
					
					return param;
				}
			//upd_remark();add for eastcom
			function upd_remark(){
			attd.upd_remark(document.frmXml,']]><xsl:value-of select="$wb_lang"/><![CDATA[')
			}
		
		//]]><xsl:value-of select="$curr_attd_id"/><![CDATA[
		//]]><xsl:value-of select="$cur_sort_order"/><![CDATA[
		//]]><xsl:value-of select="$curr_attd_idx"/><![CDATA[
	]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="init()">
			<form onsubmit="return status()" name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="app_id_lst" value=""/>
				<input type="hidden" name="att_remark_lst" value=""/>
				<input type="hidden" name="att_update_timestamp_lst" value=""/>
				<input type="hidden" name="itm_id" value="{$itm_id}"/>
				<xsl:call-template name="wb_ui_footer"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="attendance_maintance">
			<xsl:with-param name="lab_title">結訓記錄</xsl:with-param>
			<xsl:with-param name="lab_select_status">請選擇狀態</xsl:with-param>
			<xsl:with-param name="lab_status">學習狀態</xsl:with-param>
			<xsl:with-param name="lab_change_status">改變狀態</xsl:with-param>
			<xsl:with-param name="lab_lrn_with">學員具有</xsl:with-param>
			<xsl:with-param name="lab_session_attd">
				<xsl:value-of select="$lab_const_session"/>到課率</xsl:with-param>
			<xsl:with-param name="lab_remark">備註</xsl:with-param>
			<xsl:with-param name="lab_due_date">期限尚餘（天）</xsl:with-param>
			<xsl:with-param name="lab_no_attd">沒有學員</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
			<xsl:with-param name="lab_view">查看</xsl:with-param>
			<xsl:with-param name="lab_auto_update">自動更新</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_export">匯出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">保存</xsl:with-param>
			<xsl:with-param name="lab_score">分數</xsl:with-param>
			<xsl:with-param name="lab_mark_as">記為</xsl:with-param>
			<xsl:with-param name="lab_att_rate">出席率</xsl:with-param>
			<xsl:with-param name="lab_criteria">已滿足結訓條件</xsl:with-param>
			<xsl:with-param name="lab_ccr_date">狀態日期</xsl:with-param>
			<xsl:with-param name="lab_run_info">班級信息</xsl:with-param>
			<xsl:with-param name="lab_performance">成績</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_search">搜索</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="attendance_maintance">
			<xsl:with-param name="lab_title">结训记录</xsl:with-param>
			<xsl:with-param name="lab_select_status">请选择状态</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_change_status">改变状态</xsl:with-param>
			<xsl:with-param name="lab_lrn_with">学员具有</xsl:with-param>
			<xsl:with-param name="lab_session_attd">
				<xsl:value-of select="$lab_const_session"/>到课率</xsl:with-param>
			<xsl:with-param name="lab_remark">备注</xsl:with-param>
			<xsl:with-param name="lab_due_date">期限尚余（天）</xsl:with-param>
			<xsl:with-param name="lab_no_attd">没有学员</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
			<xsl:with-param name="lab_view">查看</xsl:with-param>
			<xsl:with-param name="lab_auto_update">自动更新</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_export">导出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">保存</xsl:with-param>
			<xsl:with-param name="lab_score">分数</xsl:with-param>
			<xsl:with-param name="lab_mark_as">记为</xsl:with-param>
			<xsl:with-param name="lab_att_rate">出席率</xsl:with-param>
			<xsl:with-param name="lab_criteria">已满足学习模块条件</xsl:with-param>
			<xsl:with-param name="lab_ccr_date">状态日期</xsl:with-param>
			<xsl:with-param name="lab_run_info">班级信息</xsl:with-param>
			<xsl:with-param name="lab_performance">成绩</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_search">搜索</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="attendance_maintance">
			<xsl:with-param name="lab_title">Completion result</xsl:with-param>
			<xsl:with-param name="lab_select_status">Status</xsl:with-param>
			<xsl:with-param name="lab_status">Learning status</xsl:with-param>
			<xsl:with-param name="lab_change_status">Change status</xsl:with-param>
			<xsl:with-param name="lab_lrn_with">Learner with</xsl:with-param>
			<xsl:with-param name="lab_session_attd">
				<xsl:value-of select="$lab_const_session"/> attendance rate</xsl:with-param>
			<xsl:with-param name="lab_remark">Remark</xsl:with-param>
			<xsl:with-param name="lab_due_date">Due date(day)</xsl:with-param>
			<xsl:with-param name="lab_no_attd">No learners found</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">Recycle bin</xsl:with-param>
			<xsl:with-param name="lab_view">View</xsl:with-param>
			<xsl:with-param name="lab_auto_update">Auto update</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_export">Export</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">Save</xsl:with-param>
			<xsl:with-param name="lab_score">Score</xsl:with-param>
			<xsl:with-param name="lab_mark_as">Mark as </xsl:with-param>
			<xsl:with-param name="lab_att_rate">Attendance(%)</xsl:with-param>
			<xsl:with-param name="lab_criteria">Fulfilled completion criteria</xsl:with-param>
			<xsl:with-param name="lab_ccr_date">Status date</xsl:with-param>
			<xsl:with-param name="lab_run_info">Class information</xsl:with-param>
			<xsl:with-param name="lab_performance">Performance</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
			<xsl:with-param name="lab_search">search</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ========================================================================================= -->
	<xsl:template match="attendance_maintance">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_change_status"/>
		<xsl:param name="lab_lrn_with"/>
		<xsl:param name="lab_session_attd"/>
		<xsl:param name="lab_no_attd"/>
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_remark"/>
		<xsl:param name="lab_view"/>
		<xsl:param name="lab_due_date"/>
		<xsl:param name="lab_select_status"/>
		<xsl:param name="lab_auto_update"/>
		<xsl:param name="lab_g_txt_btn_export"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_g_form_btn_save"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_mark_as"/>
		<xsl:param name="lab_att_rate"/>
		<xsl:param name="lab_criteria"/>
		<xsl:param name="lab_ccr_date"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_performance"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_search"/>
		
	    <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">113</xsl:with-param>
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
		
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="//itm_action_nav/hasTeachingCourse/text()='true'">
						<a href="javascript:itm_lst.get_itm_instr_view({//itm_action_nav/@itm_id})" class="NavLink">
							<xsl:value-of select="//itm_action_nav/@itm_title"/>
						</a>
					</xsl:when>
					<xsl:when test="@run_ind = 'false'">
						<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
							<xsl:value-of select="/applyeasy/attendance_maintance/item/title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="/applyeasy/attendance_maintance/nav/item" mode="nav">
							<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
							<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
							<xsl:with-param name="current_role" select="$current_role"/>
						</xsl:apply-templates>
				
						
					</xsl:otherwise>
				</xsl:choose>
					<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_title"/>
						</span>
			</xsl:with-param>
		</xsl:call-template>
		<div class="margin-top14"></div>
			<table>
				<tr>
					<td width="40%" valign="middle">
						<xsl:call-template name="wb_form_select_action">
							<xsl:with-param name="sel_width">140</xsl:with-param>
							<xsl:with-param name="frm">document.frmXml</xsl:with-param>
							<xsl:with-param name="field_name">get_attd_status</xsl:with-param>
							<xsl:with-param name="onChangeFunction">attd.get_status(this.options[this.selectedIndex].value)</xsl:with-param>
							<xsl:with-param name="show_submit_btn">false</xsl:with-param>
							<xsl:with-param name="submit_btn_name">sel_frm_submit_btn0</xsl:with-param>
							<xsl:with-param name="option_list">
								<option value="-1">
									<xsl:value-of select="$lab_all"/>
									<xsl:text>&#160;</xsl:text>
									<xsl:text>(</xsl:text>
									<xsl:value-of select="sum(status_info/count_list/count)"/>
									<xsl:text>)</xsl:text>
								</option>
								<xsl:apply-templates select="status_info/status_list/status"/>
							</xsl:with-param>
							<xsl:with-param name="sel_title">
								<xsl:value-of select="$lab_select_status"/>
								<xsl:text>：</xsl:text>
								<xsl:text>&#160;</xsl:text>
							</xsl:with-param>
						</xsl:call-template>
					</td>
					<td width="55%" align="right">	
					<div class="wzb-form-search">
						<input type="text"  name="user_code" class="form-control" style="width:110px;" onkeypress="" value="{$search_code}"/>
						<input type="button" class="form-submit margin-right4" value="" onclick="attd.search_user_list(document.frmXml);"/>
						<xsl:for-each select="status_info/status_list/status">
							<xsl:if test="$curr_attd_id != @id">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_mark_as"/>
										<xsl:variable name="status_name">
											<xsl:call-template name="get_ats_title">
											<xsl:with-param name="ats_id" select="@id"/>
										</xsl:call-template>
										</xsl:variable>	
										<xsl:value-of select="translate($status_name,$uppercase,$lowercase)"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:attd.chg_status_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="@id"/>')</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
						</xsl:for-each>
						<input type="hidden" name="att_status" id="att_status" value=""/>
						</div>
					</td>
					<xsl:if test="count(attendance_list/attendance) &gt;= 1">
						<td width="5%" align="right">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_export"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:attd.export_usr_lst(document.frmXml,<xsl:value-of select="$itm_id"/>, '<xsl:value-of select="$curr_attd_id"/>', '<xsl:value-of select="@timestamp"/>','<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$cur_sort_col"/>', '<xsl:value-of select="$sort_order_by"/>')</xsl:with-param>
							</xsl:call-template>
						</td>
					</xsl:if>
				</tr>
			</table>
		<!-- List Header -->
		<xsl:choose>
			<xsl:when test="count(attendance_list/attendance) &gt;= 1">
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td style="width:8px;">
							<xsl:call-template name="select_all_checkbox">
								<xsl:with-param name="chkbox_lst_cnt">
									<xsl:value-of select="count(attendance_list/attendance)"/>
								</xsl:with-param>
								<xsl:with-param name="display_icon">false</xsl:with-param>
							</xsl:call-template>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'user' ">
									<a href="javascript:attd.get_grad_record('{$itm_id}','{$curr_attd_id}','user','{$sort_order_by}','{$cur_page}')" class="TitleText">
										<xsl:value-of select="$lab_dis_name"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path">
												<xsl:value-of select="$wb_img_path"/>
											</xsl:with-param>
											<xsl:with-param name="sort_order">
												<xsl:value-of select="$cur_sort_order"/>
											</xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:attd.get_grad_record('{$itm_id}','{$curr_attd_id}','user','ASC','{$cur_page}')" class="TitleText">
										<xsl:value-of select="$lab_dis_name"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'usr_id' ">
									<a href="javascript:attd.get_grad_record('{$itm_id}','{$curr_attd_id}','usr_id','{$sort_order_by}','{$cur_page}')" class="TitleText">
										<xsl:value-of select="$lab_usr_id"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_sort_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:attd.get_grad_record('{$itm_id}','{$curr_attd_id}','usr_id','ASC','{$cur_page}')" class="TitleText">
										<xsl:value-of select="$lab_usr_id"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td align="left">
							<xsl:value-of select="$lab_group"/>
						</td>
						<td>
							<xsl:value-of select="$lab_grade"/>
						</td>
						<!-- <xsl:if test="$is_integrated != 'true'">
							<td>
								<xsl:value-of select="$lab_862"/>
							</td>
						</xsl:if> -->
						<xsl:if test="$create_session_ind = 'true'">
							<td align="center">
								<xsl:value-of select="$lab_session_attd"/>
							</td>
						</xsl:if>
						<xsl:if test="$session_ind = 'false'">
							<td align="right">
								<xsl:value-of select="$lab_score"/>
							</td>
						</xsl:if>
						<xsl:if test="//itm_action_nav/@itm_type = 'CLASSROOM'">
							<td align="right">
								<xsl:value-of select="$lab_att_rate"/>
							</td>
						</xsl:if>
						<td align="center">
							<xsl:value-of select="$lab_criteria"/>
						</td>
						<td align="center">
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'status' ">
									<a href="javascript:attd.get_grad_record('{$itm_id}','{$curr_attd_id}','status','{$sort_order_by}','{$cur_page}')" class="TitleText">
										<xsl:value-of select="$lab_status"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_sort_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:attd.get_grad_record('{$itm_id}','{$curr_attd_id}','status','ASC','{$cur_page}')" class="TitleText">
										<xsl:value-of select="$lab_status"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<xsl:for-each select="figure_list/figure">
							<td align="center">
								<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
							</td>
						</xsl:for-each>
												<!--结训记录当中的结训日期添加排序功能-->
						<td align="center">
							<xsl:choose>
								<xsl:when  test="$curr_attd_id!='2'">
									<a href="javascript:attd.get_finished_record('{$itm_id}','{$curr_attd_id}','att_update_timestamp','{$sort_order_by}','{$cur_page}')" class="TitleText">
										<xsl:value-of select="$lab_ccr_date"/>
										
										<xsl:if test="$cur_sort_col = 'att_update_timestamp'"> 
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_sort_order"/>
										</xsl:call-template>
										</xsl:if>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:attd.get_finished_record('{$itm_id}','{$curr_attd_id}','att_update_timestamp','ASC','{$cur_page}')" class="TitleText">
										<xsl:value-of select="$lab_ccr_date"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<xsl:for-each select="figure_list/figure">
							<td align="center">
								<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
							</td>
						</xsl:for-each>
					</tr>
					<xsl:apply-templates select="attendance_list/attendance">
						<xsl:with-param name="lab_remark" select="$lab_remark"/>
						<xsl:with-param name="lab_view" select="$lab_view"/>
						<xsl:with-param name="lab_lost_and_found" select="$lab_lost_and_found"/>
						<xsl:with-param name="lab_yes" select="$lab_yes"/>
						<xsl:with-param name="lab_no" select="$lab_no"/>
					</xsl:apply-templates>
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="total" select="$total"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_attd"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</div>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template match="attendance">
		<xsl:param name="lab_remark"/>
		<xsl:param name="lab_view"/>
		<xsl:param name="lab_due_date"/>
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2 = 1">RowsOdd</xsl:when>
				<xsl:otherwise>RowsEven</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr>
			<td valign="top">
				<input type="checkbox" name="app_id" value="{@app_id}" id="{user/name/@display_name}"/>
			</td>
			<!-- 2 -->
			<td>
				<a class="Text" href="javascript:attd.get_report_card({@app_id},'{$itm_id}','{$curr_attd_id}')">
					<xsl:value-of select="user/name/@display_name"/>
				</a>
			</td>
			<!-- 3 -->
			<!-- william -->
			<td>
				<xsl:value-of select="user/@id"/>
				<!--	<xsl:if test="user/tel/@tel_1 = ''">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</xsl:if>-->
			</td>
			<!-- 4 -->
			<td align="left">
				<xsl:choose>
					<xsl:when test="not(user/user_attribute_list/attribute_list[@type='USG']/entity[@relation_type = 'USR_PARENT_USG'])">
						<span class="Text">
							<xsl:value-of select="$lab_lost_and_found"/>
						</span>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="full_path"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<!-- 5 -->
			<td align="left">
				<xsl:for-each select="user/user_attribute_list/attribute_list[@type='UGR']/entity[@relation_type = 'USR_CURRENT_UGR']">
					<xsl:choose>
						<xsl:when test="position() != last()">
							<xsl:value-of select="@display_bil"/>
							<xsl:text>,&#160;</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="@display_bil"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
				<xsl:if test="not(user/user_attribute_list/attribute_list[@type='UGR']/entity[@relation_type = 'USR_current_ug'])">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</xsl:if>
			</td>
			<xsl:if test="$create_session_ind = 'true'">
				<td align="right">
					<xsl:choose>
						<xsl:when test="@rate != ''">
							<xsl:call-template name="display_score">
								<xsl:with-param name="score" select="@rate"/>
							</xsl:call-template>
							<!--<xsl:value-of select="format-number(@rate,'0.00')"/>%-->
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>--</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
			<xsl:if test="$session_ind = 'false'">
				<td align="right">
					<xsl:choose>
						<xsl:when test="cov_score != ''">
							<xsl:call-template name="display_score">
								<xsl:with-param name="score" select="cov_score"/>
							</xsl:call-template>
							<!--<xsl:value-of select="format-number(cov_score,'0.00')"/>%-->
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>--</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
			<!--    william    -->
			<xsl:if test="//itm_action_nav/@itm_type = 'CLASSROOM'">
				<td align="right">
					<xsl:choose>
						<xsl:when test="@rate = ''">--</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="number(substring-before(@rate,'.'))"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
			<td align="center">
				<xsl:choose>
					<xsl:when test="has_other_criteria='true'">
						<xsl:choose>
							<xsl:when test="pass_other_criteria='true'">
								<xsl:value-of select="$lab_yes"/>
							</xsl:when>
							<xsl:otherwise><xsl:value-of select="$lab_no"/></xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise><xsl:text>--</xsl:text></xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:variable name="usr_attd_id">
					<xsl:value-of select="@status"/>
				</xsl:variable>
				<xsl:call-template name="get_ats_title">
					<xsl:with-param name="ats_id" select="$usr_attd_id"/>
				</xsl:call-template>
			</td>
			<xsl:variable name="app_id" select="@app_id"/>
			<xsl:for-each select="../../figure_list/figure">
				<td align="center">
					<xsl:variable name="ict_id" select="@id"/>
					<xsl:variable name="icv_value" select="../../attendance_list/attendance[@app_id = $app_id]/figure_value_list/figure_value[@fgt_id = $ict_id]"/>
					<xsl:choose>
						<xsl:when test="$icv_value/@fig_value">
							<xsl:value-of select="format-number($icv_value/@fig_value, '0.00')"/>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:for-each>
			<!--    william    -->
			<td align="center" nowrap="nowrap">
				<xsl:choose>
					<xsl:when test="@att_update_timestamp = ''">--</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp" select="@att_update_timestamp"/>
							<xsl:with-param name="dis_time">T</xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template match="status">
		<xsl:variable name="attd_id">
			<xsl:value-of select="@id"/>
		</xsl:variable>
		<option value="{@id}">
			<xsl:call-template name="get_ats_title">
				<xsl:with-param name="ats_id" select="@id"/>
			</xsl:call-template>
			<xsl:text>&#160;</xsl:text>
			<xsl:text>(</xsl:text>
			<xsl:value-of select="../../../status_info/count_list/count[@status_id = $attd_id]/."/>
			<xsl:text>)</xsl:text>
		</option>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<!--this template can be reused in aeItem's Moudle to draw nav-link of items which can run-->
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
					<xsl:choose>
						<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
					</xsl:choose>
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
	<!-- ======================================================================================-->
</xsl:stylesheet>
