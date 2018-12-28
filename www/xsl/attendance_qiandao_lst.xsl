<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_win_hdr.xsl"/>
	<xsl:import href="utils/check_client.xsl"/>
	<xsl:import href="utils/wb_form_select_action.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="utils/display_score.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
    <xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<!-- paginatoin variables -->
	<xsl:variable name="page_size" select="/evalmanagement/qiandao_lst/pagination/@page_size"/>
	<xsl:variable name="cur_page" select="/evalmanagement/qiandao_lst/pagination/@cur_page"/>
	<xsl:variable name="total" select="/evalmanagement/qiandao_lst/pagination/@total_rec"/>
	<xsl:variable name="itm_id" select="/evalmanagement/qiandao_lst/item/@id"/>
	<xsl:variable name="create_session_ind" select="/evalmanagement/qiandao_lst/item/@create_session_ind"/>
	<xsl:variable name="session_ind" select="/evalmanagement/qiandao_lst/item/@session_ind"/>
	

	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="evalmanagement"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="evalmanagement">
		<xsl:call-template name="wb_init_lab"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="qiandao_lst">
			<xsl:with-param name="lab_qiandao_jilu">簽到記錄</xsl:with-param>
			<xsl:with-param name="lab_qiandao_date">簽到時間</xsl:with-param>
			<xsl:with-param name="lab_no_attd">沒有學員</xsl:with-param>
			<xsl:with-param name="lab_learner">學員</xsl:with-param>
			<xsl:with-param name="lab_attd_status">簽到狀態</xsl:with-param>
			<xsl:with-param name="lab_attd_status_1">正常</xsl:with-param>
			<xsl:with-param name="lab_attd_status_2">遲到</xsl:with-param>
			<xsl:with-param name="lab_attd_status_3">缺勤</xsl:with-param>
			<xsl:with-param name="lab_run_info">班級信息</xsl:with-param>
			<xsl:with-param name="lab_attd_rate">出席率</xsl:with-param>
			<xsl:with-param name="lab_training_unit">培訓單元</xsl:with-param>
			<xsl:with-param name="lab_start_time">開始時間</xsl:with-param>
			<xsl:with-param name="lab_end_time">結束時間</xsl:with-param>
			<xsl:with-param name="lab_select_all">--<xsl:value-of select="$lab_all"/>--</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="qiandao_lst">
			<xsl:with-param name="lab_qiandao_jilu">签到记录</xsl:with-param>
			<xsl:with-param name="lab_qiandao_date">签到时间</xsl:with-param>
			<xsl:with-param name="lab_no_attd">没有学员</xsl:with-param>
			<xsl:with-param name="lab_learner">学员</xsl:with-param>
			<xsl:with-param name="lab_attd_status">签到状态</xsl:with-param>
			<xsl:with-param name="lab_attd_status_1">正常</xsl:with-param>
			<xsl:with-param name="lab_attd_status_2">迟到</xsl:with-param>
			<xsl:with-param name="lab_attd_status_3">缺勤</xsl:with-param>
			<xsl:with-param name="lab_run_info">班级信息</xsl:with-param>
			<xsl:with-param name="lab_attd_rate">出席率</xsl:with-param>
			<xsl:with-param name="lab_training_unit">培训单元</xsl:with-param>
			<xsl:with-param name="lab_start_time">开始时间</xsl:with-param>
			<xsl:with-param name="lab_end_time">结束时间</xsl:with-param>
			<xsl:with-param name="lab_select_all">--<xsl:value-of select="$lab_all"/>--</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="qiandao_lst">
			<xsl:with-param name="lab_qiandao_jilu">Sign in record</xsl:with-param>
			<xsl:with-param name="lab_qiandao_date">Sign in time</xsl:with-param>
			<xsl:with-param name="lab_no_attd">No student</xsl:with-param>
			<xsl:with-param name="lab_learner">Learner</xsl:with-param>
			<xsl:with-param name="lab_attd_status">Sign in status</xsl:with-param>
			<xsl:with-param name="lab_attd_status_1">Normal</xsl:with-param>
			<xsl:with-param name="lab_attd_status_2">Late</xsl:with-param>
			<xsl:with-param name="lab_attd_status_3">Absence</xsl:with-param>
			<xsl:with-param name="lab_run_info">Class Information</xsl:with-param>
			<xsl:with-param name="lab_attd_rate">Attendance</xsl:with-param>
			<xsl:with-param name="lab_training_unit">Training unit</xsl:with-param>
			<xsl:with-param name="lab_start_time">Start time</xsl:with-param>
			<xsl:with-param name="lab_end_time">End time</xsl:with-param>
			<xsl:with-param name="lab_select_all">--<xsl:value-of select="$lab_all"/>--</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ========================================================================================= -->
	<xsl:template match="qiandao_lst">
		<xsl:param name="lab_qiandao_jilu"/>
		<xsl:param name="lab_qiandao_date"/>		
		<xsl:param name="lab_no_attd"/>
		<xsl:param name="lab_learner"/>
		<xsl:param name="lab_attd_status"/>
		<xsl:param name="lab_attd_status_1"/>
		<xsl:param name="lab_attd_status_2"/>
		<xsl:param name="lab_attd_status_3"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_attd_rate"/>
		<xsl:param name="lab_training_unit"/>
		<xsl:param name="lab_start_time"/>
		<xsl:param name="lab_end_time"/>
		<xsl:param name="lab_select_all"/>
		<xsl:param name="select_ils_id" select="select_ils_id"/>
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
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_batchprocess.js"/>			
			<script language="JavaScript" TYPE="text/javascript"><![CDATA[
			var itm_lst = new wbItem
			var attd = new wbAttendance
			var Batch = new wbBatchProcess
			
			
			
					
	
					
			]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="">
			<form onsubmit="return status()" name="frmXml">
				<input type="hidden" name="cmd" value=""/>
			    <input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="app_id_lst" value=""/>
				<input type="hidden" name="itm_id" value="{$itm_id}"/>
				<input type="hidden" name="attd_remark"/>
				<input type="hidden" name="attd_rate"/>
				<input type="hidden" name="module"/>
				<input type="hidden" name="att_status" />
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
					<xsl:with-param name="parent_code" select="$parent_code"/>
				</xsl:call-template>
				<xsl:call-template name="itm_action_nav">
					<xsl:with-param  name="cur_node_id">115</xsl:with-param>
				</xsl:call-template>
			<div class="wzb-item-main">
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
							<xsl:when test="item/item_type_meta/@run_ind='true'">
								<a href="Javascript:itm_lst.get_item_detail({item/parent/@id})" class="NavLink">
									<xsl:value-of select="item/parent/title"/>
								</a>
								<xsl:text>&#160;&gt;&#160;</xsl:text>
								<a href="Javascript:itm_lst.get_item_run_list({item/parent/@id})" class="NavLink">
									<xsl:choose>
										<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
										<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
									</xsl:choose>
								</a>
								<xsl:text>&#160;&gt;&#160;</xsl:text>
								<a href="Javascript:itm_lst.get_item_run_detail({item/@id})" class="NavLink">
									<xsl:value-of select="item/title"/>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a href="Javascript:itm_lst.get_item_detail({item/@id})" class="NavLink">
									<xsl:value-of select="item/title"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:value-of select="$lab_qiandao_jilu"/>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:if  test="count(ils_list/ils) &gt;= 1">
					<table cellspacing="0" cellpadding="3" border="0" width="{$wb_gen_table_width}">
						<tr>
							<td align="right">
							    <xsl:value-of select="$lab_training_unit"/>：
								<select class="Select" style="width:95px" onchange="attd.get_qiandao_Lst({item/@id},this.value)">
									<option value="0">
										<xsl:if test="$select_ils_id ='0'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_select_all"/>
									</option>
									<xsl:for-each select="ils_list/ils">
										<option value="{ils_id}">
											<xsl:if test="$select_ils_id = ils_id">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="ils_title"/>
										</option>
									</xsl:for-each>
								</select>
							</td>
						</tr>
					</table>
				</xsl:if>
				<!-- List Header -->
				<xsl:choose>
					<xsl:when test="count(aeitemlesson_qiandao_lst/usr) &gt;= 1">
						<table class="table wzb-ui-table">
							<tr class="wzb-ui-table-head">
								<td>
									<xsl:value-of select="$lab_training_unit"/>
								</td>
								<td align="center">
									<xsl:value-of select="$lab_start_time"/>
								</td>
								<td align="center">
									<xsl:value-of select="$lab_end_time"/>
								</td>
								<td align="center">
									<xsl:value-of select="$lab_learner"/>
								</td>
								<td align="center">
										<xsl:value-of select="$lab_qiandao_date"/>
								</td>
								<td align="right">
								   <xsl:value-of select="$lab_attd_status"/>
								</td>
							</tr>
							<xsl:apply-templates select="aeitemlesson_qiandao_lst/usr">
								<xsl:with-param name="lab_attd_status_1"><xsl:value-of select="$lab_attd_status_1"/></xsl:with-param>						
								<xsl:with-param name="lab_attd_status_2"><xsl:value-of select="$lab_attd_status_2"/></xsl:with-param>							
								<xsl:with-param name="lab_attd_status_3"><xsl:value-of select="$lab_attd_status_3"/></xsl:with-param>
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
			</form>
		</body>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template match="usr">
		<xsl:param name="lab_attd_status_1"/>
		<xsl:param name="lab_attd_status_2"/>
		<xsl:param name="lab_attd_status_3"/>
		<xsl:variable name = "status"><xsl:value-of select="qiandao_status"/></xsl:variable>
		<tr>
			<td>
				<xsl:value-of select="ils_title"/>
			</td>
			<td align="center">
				<xsl:value-of select="ils_start_time"/>
			</td>
			<td align="center">
				<xsl:value-of select="ils_end_time"/>
			</td>
			<td align="center">
				<xsl:value-of select="usr_name"/>
			</td>
			<td align="center">
				<xsl:value-of select="qiandao_date"/>
			</td>
			<td align="right">
				<xsl:choose>
					<xsl:when test="$status=1">
						<xsl:value-of select="$lab_attd_status_1"/>
					</xsl:when>
					<xsl:when test="$status=2">
						<xsl:value-of select="$lab_attd_status_2"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_attd_status_3"/>
					</xsl:otherwise>
				</xsl:choose>
				
			</td>
		</tr>
	</xsl:template>
	<!-- ====================================================================================================== -->

	<!-- ====================================================================================================== -->
</xsl:stylesheet>
