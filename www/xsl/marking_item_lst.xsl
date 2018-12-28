<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<!-- others-->
	<xsl:import href="share/label_for_eval_mgt.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<!-- Global variable-->
	<xsl:variable name="itm_id" select="evalmanagement/scoring_item/@itm_id"/>
	<xsl:variable name="itm_title" select="evalmanagement/scoring_item/@itm_title"/>
	<xsl:variable name="cmt_max_score" select="evalmanagement/scoring_item/@cmt_max_score"/>
	<xsl:variable name="cmt_id" select="evalmanagement/scoring_item/@cmt_id"/>
	<xsl:variable name="cmt_title" select="evalmanagement/scoring_item/@cmt_title"/>
	<xsl:variable name="type" select="evalmanagement/scoring_item/@type"/>
	<xsl:variable name="eval_item_cnt" select="count(evalmanagement/scoring_item/eval_item)"/>
	<!--variables for paginatoin -->
	<xsl:variable name="page_size" select="/evalmanagement/scoring_item/pagination/@page_size"/>
	<xsl:variable name="cur_page" select="/evalmanagement/scoring_item/pagination/@cur_page"/>
	<xsl:variable name="total" select="/evalmanagement/scoring_item/pagination/@total_rec"/>
	<xsl:variable name="timestamp" select="/evalmanagement/scoring_item/pagination/@timestamp"/>
	<!-- =============================================================== -->
	<xsl:template match="/evalmanagement">
		<xsl:apply-templates select="scoring_item"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="scoring_item">
		<html>
			<head>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_evalmgt.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_scorescheme.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[
					evalmgt = new wbEvalManagement;
					cmt = new wbScoreScheme;
					attn = new wbAttendance;
					itm_lst = new wbItem;
				]]></script>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			</head>
			<BODY marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
				<form name="frm">
					<input type="hidden" name="stylesheet" value=""/>
					<input type="hidden" name="cmd" value=""/>
					<input type="hidden" name="module" value=""/>
					<input type="hidden" name="cmt_id" value="{$cmt_id}"/>
					<input type="hidden" name="app_id" value=""/>					
					<input type="hidden" name="cmt_max_score" value="{$cmt_max_score}"/>
					<input type="hidden" name="lrn_ent_id" value=""/>
					<input type="hidden" name="lrn_name" value=""/>
					<input type="hidden" name="cmt_tkh_id" value=""/>
					<input type="hidden" name="cmt_score" value=""/>
					<input type="hidden" name="url_success" value=""/>
					<input type="hidden" name="url_failure" value=""/>
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</BODY>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>訊息</xsl:with-param>
			<xsl:with-param name="lab_performance">成績</xsl:with-param>
			<xsl:with-param name="lab_attendance">考勤</xsl:with-param>
			<xsl:with-param name="lab_mark_record">成績記錄</xsl:with-param>
			<xsl:with-param name="lab_desc">以下是課程的已錄取學員。點擊修改以修改該學員的成績，點擊重置以重置此計分項目的所有學員成績。
</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>讯息</xsl:with-param>
			<xsl:with-param name="lab_performance">成绩</xsl:with-param>
			<xsl:with-param name="lab_attendance">考勤</xsl:with-param>
			<xsl:with-param name="lab_mark_record">成绩记录</xsl:with-param>
			<xsl:with-param name="lab_desc">以下为课程的已录取学员。点击修改以修改该学员的成绩，点击重置以重置此计分项目的所有学员成绩。</xsl:with-param>			
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
			<xsl:with-param name="lab_performance">Performance</xsl:with-param>
			<xsl:with-param name="lab_attendance">Result</xsl:with-param>
			<xsl:with-param name="lab_mark_record">Score</xsl:with-param>
			<xsl:with-param name="lab_desc">Listed below are learners enrolled. Click edit to update score for specified learner. Click reset to reset all learners' scores.</xsl:with-param>			
		</xsl:call-template>		
	</xsl:template>
<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_performance"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_attendance"/>
		<xsl:param name="lab_mark_record"/>
		<xsl:param name="lab_desc"/>
	    <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">114</xsl:with-param>
			
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
		<xsl:if test="$type='offline'">
			<xsl:call-template name="wb_ui_desc">
				<xsl:with-param name="text" select="$lab_desc"/>
			</xsl:call-template>
		</xsl:if>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="//itm_action_nav/hasTeachingCourse/text()='true' ">
						<a href="javascript:itm_lst.get_itm_instr_view({//itm_action_nav/@itm_id})" class="NavLink">
							<xsl:value-of select="//itm_action_nav/@itm_title"/>
						</a>
					</xsl:when>
					<xsl:when test="/evalmanagement/scoring_item/@run_ind = 'false'">
						<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
							<xsl:value-of select="$itm_title"/>
						</a>
						
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="/evalmanagement/scoring_item/nav/item" mode="nav">
							<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
							<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
				<span class="TitleText">&#160;&gt;&#160;</span>
					<a href="javascript:cmt.get_scoring_itm_lst({$itm_id})" class="NavLink">
						<xsl:value-of select="$lab_mark_recording"/>
					</a>
				<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$cmt_title"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_space"/>
		<!--Custom content-->
		<xsl:choose>
			<xsl:when test="$eval_item_cnt &gt;= 1">
				<xsl:if test="/evalmanagement/scoring_item/@type != 'online'">
						<xsl:call-template name="wb_ui_head">
								<xsl:with-param name="extra_td">
									<td align="right">
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="wb_gen_btn_name">
												<xsl:value-of select="$lab_btn_reset"/>
											</xsl:with-param>
											<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_href">javascript:evalmgt.reset_mark(frm,<xsl:value-of select ="$cmt_id"/>)</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
										</xsl:call-template>
									</td>
								</xsl:with-param>
							<!--<xsl:with-param name="text" select="$lab_mark_recording"/>-->
						</xsl:call-template>
				</xsl:if>
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td align="left">
							<xsl:value-of select="$lab_lrn"/>
						</td>
						<td align="left">
							<xsl:value-of select="$lab_cmt_score_max"/>
						</td>
					<xsl:if test="$type='offline'">
						<td>
						</td>
					</xsl:if>
				</tr>
					<xsl:apply-templates select="eval_item"/>
				</table>
				<!--Pagination-->
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="timestamp" select="$timestamp"/>
					<xsl:with-param name="total" select="$total"/>
					<xsl:with-param name="width">
						<xsl:value-of select="$wb_gen_table_width"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_mark_record"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:call-template name="wb_ui_footer"/>
	</div>
	</xsl:template>
	<xsl:template match="eval_item">
		<tr valign="middle">
			<td align="left">
					<xsl:value-of select="@lrn_name"/>
			</td>
			<td width="40%" align="left">
					<xsl:choose>
						<xsl:when test="@cmt_score=''">
							<xsl:text>--</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="round(@cmt_score)"/>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:value-of select="concat(' / ',round($cmt_max_score))"/>
			</td>
			<xsl:if test="$type='offline'">
				<td nowrap="nowrap">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_btn_edit"/></xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:evalmgt.edit_marking_item(frm,'<xsl:value-of select='@app_id'/>','<xsl:value-of select="$cmt_id"/>','<xsl:value-of select="@lrn_ent_id"/>','<xsl:value-of select="@cmt_tkh_id"/>','<xsl:value-of select="@lrn_name"/>', '<xsl:value-of select="@cmt_score"/>',<xsl:value-of select="round($cmt_max_score)"/>)</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
					</xsl:call-template>
				</td>
			</xsl:if>
		</tr>
	</xsl:template>

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
				<a href="javascript:itm_lst.get_item_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
