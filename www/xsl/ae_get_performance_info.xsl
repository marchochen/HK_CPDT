<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- customize utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<!-- other -->
	<!--<xsl:import href="share/itm_gen_details_share.xsl"/>-->
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="itm_type" select="/applyeasy/item/@type"/>
	<xsl:variable name="itm_title" select="/applyeasy/item/title"/>
	<xsl:variable name="itm_updated_timestamp" select="/applyeasy/item/last_updated/@timestamp"/>
	<xsl:variable name="itm_status" select="/applyeasy/item/@status"/>
	<xsl:variable name="turn_itm_status">
		<xsl:choose>
			<xsl:when test="$itm_status = 'OFF'">ON</xsl:when>
			<xsl:otherwise>OFF</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="page_variant" select="/applyeasy/meta/page_variant"/>
	<xsl:variable name="create_run_ind" select="/applyeasy/item/@create_run_ind"/>
	<xsl:variable name="cur_month" select="/applyeasy/item/child_items/cur_time/@month"/>
	<xsl:variable name="cur_year" select="/applyeasy/item/child_items/cur_time/@year"/>
	<xsl:variable name="itm_type_list_root" select="/applyeasy/item/child_items/item_type_list"/>
	<xsl:variable name="itm_apply_ind" select="/applyeasy/item/@apply_ind"/>
	<xsl:variable name="run_ind" select="/applyeasy/item/@run_ind"/>
	<xsl:variable name="usr_id" select="/applyeasy/meta/cur_usr/@ent_id"/>
	<xsl:variable name="escaped_title">
		<xsl:call-template name="escape_js">
			<xsl:with-param name="input_str" select="/applyeasy/item/title"/>
		</xsl:call-template>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:apply-templates select="applyeasy/item"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="applyeasy/item">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_scorescheme.js"/>				
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_announcement.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_report.js"/>
				<script language="javascript" type="text/javascript" src="{$wb_js_path}wb_criteria.js"/>
				<script language="javascript" type="text/javascript" src="{$wb_js_path}wb_mote.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[
				crit = new wbCriteria
				itm_lst = new wbItem
				ann = new wbAnnouncement
				course_lst = new wbCourse	
				rpt = new wbReport
				mote = new wbMote
				app = new wbApplication
				attn = new wbAttendance
				cmt = new wbScoreScheme;
				itm_lst = new wbItem			
				
				window.onunload = unloadHandler;
				function unloadHandler(){
					wb_utils_set_cookie('lrn_soln_itm_title','')
				}
				
				function init(){
					wb_utils_set_cookie('lrn_soln_itm_title',']]><xsl:value-of select="$escaped_title"/><![CDATA[')
				}
			]]></script>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="javascript:init()">
				<form name="frmXml">
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_online_content">線上內容</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>訊息</xsl:with-param>
			<xsl:with-param name="lab_score_record">計分記錄</xsl:with-param>
			<xsl:with-param name="lab_score_desc">在線/離線學習內容中學員的分數</xsl:with-param>
			<xsl:with-param name="lab_att_rate">出席率</xsl:with-param>
			<xsl:with-param name="lab_att_rate_desc">學員在離線活動中的出席率</xsl:with-param>
			<xsl:with-param name="lab_tracking_rpt">跟蹤報告</xsl:with-param>
			<xsl:with-param name="lab_tracking_rpt_desc">在線學習內容中學員的詳細跟蹤統計</xsl:with-param>
			<xsl:with-param name="lab_grad_record">結訓記錄</xsl:with-param>
			<xsl:with-param name="lab_grad_record_desc">
				學員完成<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>的總體情況</xsl:with-param>
			<xsl:with-param name="lab_performance">成績</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_online_content">在线内容</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_score_record">计分记录</xsl:with-param>
			<xsl:with-param name="lab_score_desc">在线/离线学习内容中学员的分数</xsl:with-param>
			<xsl:with-param name="lab_att_rate">出席率</xsl:with-param>
			<xsl:with-param name="lab_att_rate_desc">学员在离线活动中的出席率</xsl:with-param>
			<xsl:with-param name="lab_tracking_rpt">跟踪报告</xsl:with-param>
			<xsl:with-param name="lab_tracking_rpt_desc">在线学习内容中学员的详细跟踪统计</xsl:with-param>
			<xsl:with-param name="lab_grad_record">结训记录</xsl:with-param>
			<xsl:with-param name="lab_grad_record_desc">
				学员完成<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>的总体情况
			</xsl:with-param>
			<xsl:with-param name="lab_performance">成绩</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_online_content">Content</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
			<xsl:with-param name="lab_score_record">Score</xsl:with-param>
			<xsl:with-param name="lab_score_desc">Score of learners in offline/online learning content</xsl:with-param>
			<xsl:with-param name="lab_att_rate">Attendance rate</xsl:with-param>
			<xsl:with-param name="lab_att_rate_desc">Attendance rate of learners in offline activity</xsl:with-param>
			<xsl:with-param name="lab_tracking_rpt">Tracking report</xsl:with-param>
			<xsl:with-param name="lab_tracking_rpt_desc">
				Detail tracking statistics of learners in online <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">examination</xsl:when>
				<xsl:otherwise>learning</xsl:otherwise>
				</xsl:choose> content</xsl:with-param>
			<xsl:with-param name="lab_grad_record">Completion result</xsl:with-param>
			<xsl:with-param name="lab_grad_record_desc">Overall learning status of learners</xsl:with-param>
			<xsl:with-param name="lab_performance">Performance</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_online_content"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_score_record"/>
		<xsl:param name="lab_score_desc"/>
		<xsl:param name="lab_att_rate"/>
		<xsl:param name="lab_att_rate_desc"/>
		<xsl:param name="lab_tracking_rpt"/>
		<xsl:param name="lab_tracking_rpt_desc"/>
		<xsl:param name="lab_performance"/>
		<xsl:param name="lab_grad_record"/>
		<xsl:param name="lab_grad_record_desc"/>
		
	    <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">102</xsl:with-param>
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
					<xsl:when test="/applyeasy/item/@run_ind = 'false'">
						 
						<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
							<xsl:value-of select="/applyeasy/item/title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="/applyeasy/item/nav/item" mode="nav">
							<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
							<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
				<span class="NavLink"> &#160;&gt;&#160;<xsl:value-of select="$lab_performance"/></span>
			</xsl:with-param>
		</xsl:call-template>
		<table>
			<xsl:call-template name="template">
				<xsl:with-param name="lab_title" select="$lab_score_record"/>
				<xsl:with-param name="lab_link">javascript:cmt.get_scoring_itm_lst('<xsl:value-of select="/applyeasy/item/@id"/>')</xsl:with-param>
				<xsl:with-param name="lab_content" select="$lab_score_desc"/>
			</xsl:call-template>
			<xsl:call-template name="template">
				<xsl:with-param name="lab_title" select="$lab_att_rate"/>
				<xsl:with-param name="lab_link">javascript:attn.get_attd_rate_lst('<xsl:value-of select="/applyeasy/item/@id"/>')</xsl:with-param>
				<xsl:with-param name="lab_content" select="$lab_att_rate_desc"/>
			</xsl:call-template>
			<xsl:if test="/applyeasy/item/@qdb_ind = 'true'">
				<xsl:call-template name="template">
					<xsl:with-param name="lab_title" select="$lab_tracking_rpt"/>
					<xsl:with-param name="lab_link">javascript:rpt.open_cos_lrn_lst(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
					<xsl:with-param name="lab_content" select="$lab_tracking_rpt_desc"/>
				</xsl:call-template>
			</xsl:if>
			<xsl:call-template name="template">
				<xsl:with-param name="lab_title" select="$lab_grad_record"/>
				<xsl:with-param name="lab_link">javascript:attn.get_grad_record(<xsl:value-of select="/applyeasy/item/@id"/>)</xsl:with-param>
				<xsl:with-param name="lab_content" select="$lab_grad_record_desc"/>
			</xsl:call-template>
		</table>
	</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="template">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_content"/>
		<xsl:param name="lab_link"/>
		<tr>
			<td align="center">
				<dl>
					<dt>
						<a class="TitleText" href="{$lab_link}">
							<xsl:value-of select="$lab_title"/>
						</a>
						<br/>
						<xsl:value-of select="$lab_content"/>
						<br/>
						<br/>
					</dt>
				</dl>
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
	<!-- =============================================================== -->	
</xsl:stylesheet>
