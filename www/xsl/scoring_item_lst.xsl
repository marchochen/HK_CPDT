<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
    <xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	
	<!--custom-->
	<!--label for this module-->
	<xsl:import href="share/label_for_eval_mgt.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="mod_cnt" select="count(CourseCriteria/marking_scheme_list/item)"/>
	<xsl:variable name="offline_cnt" select="count(CourseCriteria/marking_scheme_list/item/cmt_mod_title)"/>
	<xsl:variable name="cur_usr">
		<xsl:value-of select="CourseCriteria/meta/cur_usr/@id"/>
	</xsl:variable>
	<xsl:variable name="itm_id" select="/CourseCriteria/item/@id"/>
	<xsl:variable name="run_ind" select="/CourseCriteria/item/@run_ind"/>
	<xsl:variable name="usr_id" select="/CourseCriteria/meta/cur_usr/@ent_id"/>
	<xsl:variable name="ccr_id" select="/CourseCriteria/marking_scheme_list/@ccr_id"/>
	<xsl:variable name="current_role" select="/CourseCriteria/current_role"/>
	<!-- ================================================================ -->
	<xsl:template match="/">
		<xsl:apply-templates select="CourseCriteria"/>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="CourseCriteria">
		<html>
			<xsl:call-template name="draw_header"/>
			<xsl:call-template name="draw_body"/>
		</html>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_header">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_scorescheme.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_evalmgt.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript"><![CDATA[
			cmt=new wbScoreScheme;
			evalmgt = new wbEvalManagement;
			attn = new wbAttendance;
			itm_lst = new wbItem;
		]]></script>
		</head>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_body">
		<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0">
			<form name="frm">
				<xsl:call-template name="wb_init_lab"/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="ccr_id" value=""/>
				<input type="hidden" name="itm_id" value="{$itm_id}"/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="module" value="course.CourseCriteriaModule"/>
			</form>
		</body>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>訊息</xsl:with-param>
			<xsl:with-param name="lab_performance">成績</xsl:with-param>
			<xsl:with-param name="lab_attendance">考勤</xsl:with-param>
			<xsl:with-param name="lab_desc">你可以修改學員在學習模塊的分數。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_performance">成绩</xsl:with-param>
			<xsl:with-param name="lab_attendance">考勤</xsl:with-param>
			<xsl:with-param name="lab_desc">您可以修改学员在学习模块的分数。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
			<xsl:with-param name="lab_performance">Performance</xsl:with-param>
			<xsl:with-param name="lab_attendance">Result</xsl:with-param>
			<xsl:with-param name="lab_desc">You can update the scores of learners in the learning modules.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="content">
		<xsl:param name="lab_performance"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_attendance"/>
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
		<xsl:if test="$mod_cnt != $offline_cnt">
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
					<xsl:when test="/CourseCriteria/item/@run_ind = 'false'">
						<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
							<xsl:value-of select="/CourseCriteria/item/title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="/CourseCriteria/item/nav/item" mode="nav">
							<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
							<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
<!--							<xsl:with-param name="lab_content_info" select="$lab_content"/>
							<xsl:with-param name="lab_attendance_info" select="$lab_attendance"/>
-->						</xsl:apply-templates>
						
					</xsl:otherwise>
				</xsl:choose>
				<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_mark_recording"/></span>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="not($mod_cnt &gt;=1)">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_scoring_item"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_head">
				<!--<xsl:with-param name="text" select="$lab_scoring_item"/>-->
					<xsl:with-param name="extra_td">
							<td align="right">
								<xsl:if test="$offline_cnt != $mod_cnt">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_name">
											<xsl:value-of select="$lab_import"/>
										</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">javascript:evalmgt.eval_import_mark_prep(frm)</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
									</xsl:call-template>
								</xsl:if>
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_export"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:evalmgt.eval_export_mark(frm);</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
								</xsl:call-template>
							</td>
					</xsl:with-param>
				</xsl:call-template>
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td align="left" width="40%">
							<xsl:value-of select="$lab_cmt_title"/>
						</td>
						<td align="left" width="25%">
							<xsl:value-of select="$lab_cmt_max_score"/>
						</td>
						<td align="left" width="25%">
							<xsl:value-of select="$lab_cmt_pass_score"/>
						</td>
						<td align="right" width="">
							<xsl:value-of select="$lab_cmt_contri_rate"/>
						</td>
				</tr>
					<xsl:apply-templates select="marking_scheme_list/item"/>
				</table>
			</xsl:otherwise>
		</xsl:choose>
	</div>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="item">
		<xsl:variable name="cmt_id" select="@cmt_id"/>
		<tr>
			<xsl:attribute name="class"><xsl:choose><xsl:when test="position() mod 2 = 1">RowsOdd</xsl:when><xsl:otherwise>RowsEven</xsl:otherwise></xsl:choose></xsl:attribute>
			<td align="left">
				<a href="javascript:evalmgt.get_marking_lst_by_cmt_id({$itm_id},{$cmt_id})" class="Text">
					<xsl:value-of select="cmt_title"/>
				</a>
			</td>
			<td align="left">
				<xsl:value-of select="number(cmt_max_score)"/>
			</td>
			<td align="left">
				<xsl:value-of select="number(cmt_pass_score)"/>
			</td>
			<td align="right">
					<xsl:value-of select="number(cmt_contri_rate)"/>
		</td>
			<input type="hidden" name="all_cmt_id" value="{@cmt_id}"/>
		</tr>
	</xsl:template>
	<!-- ================================================================ -->
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
