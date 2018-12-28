<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"  xmlns:java="http://xml.apache.org/xalan/java"
	exclude-result-prefixes="java">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/itm_nav_share.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>	
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="pos">0</xsl:variable>
	<xsl:variable name="mytype" select="/enrolment_list/header/@subtype"/>
	<xsl:variable name="mod_id" select="/enrolment_list/@mod_id"/>
	<xsl:variable name="cos_id" select="/enrolment_list/@cos_id"/>
	<xsl:variable name="ass_timestamp" select="/enrolment_list/stat/@time"/>
	<xsl:variable name="due_date" select="translate(substring-before(/enrolment_list/header/@due_datetime,'.'),' -:','')"/>
	<xsl:variable name="page_size" select="/enrolment_list/stat/@page_size"/>
	<xsl:variable name="isFromIframe" select="/enrolment_list/isFromIframe"/>
	<xsl:variable name="cur_page">
		<xsl:choose>
			<xsl:when test="/enrolment_list/stat/@cur_page = '0'">1</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="/enrolment_list/stat/@cur_page"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="total">
		<xsl:for-each select="/enrolment_list/stat/queue">
			<xsl:if test="@name = 'graded' ">
				<xsl:value-of select="@count"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:variable>
	<xsl:variable name="label_core_training_management_250" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_250')"/>
	<xsl:variable name="label_core_training_management_393" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_393')"/>
	<xsl:variable name="label_core_training_management_30" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_30')"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="enrolment_list"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="enrolment_list">
		<head>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_assignment.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[var course_lst = new wbCourse;
var ass = new wbAssignment;
var isExcludes = getUrlParam('isExcludes') ? getUrlParam('isExcludes') : 'false';
cur_page = 	]]><xsl:value-of select="$cur_page"/><![CDATA[
ass_timestamp = ']]><xsl:value-of select="$ass_timestamp"/><![CDATA['
queue = 'graded'



				function select_type(frm){
					select_val = frm.type_sel_frm.options[frm.type_sel_frm.selectedIndex].value
					if (select_val == 'graded')
						ass.view_submission_graded(]]><xsl:value-of select="$cos_id"/><![CDATA[,]]><xsl:value-of select="$mod_id"/><![CDATA[,'graded',1,']]><xsl:value-of select="$ass_timestamp"/><![CDATA[',']]><xsl:value-of select="$isFromIframe"/><![CDATA[')
					else if (select_val == 'ungraded')
						ass.view_submission_ungraded(]]><xsl:value-of select="$cos_id"/><![CDATA[,]]><xsl:value-of select="$mod_id"/><![CDATA[,'not_graded',1,']]><xsl:value-of select="$ass_timestamp"/><![CDATA[',']]><xsl:value-of select="$isFromIframe"/><![CDATA[')
					else if (select_val == 'not_submitted')
						ass.view_submission_not_submit(]]><xsl:value-of select="$cos_id"/><![CDATA[,]]><xsl:value-of select="$mod_id"/><![CDATA[,'not_submitted',1,']]><xsl:value-of select="$ass_timestamp"/><![CDATA[',']]><xsl:value-of select="$isFromIframe"/><![CDATA[')
					else if (select_val == 'all')
						ass.view_submission_all(]]><xsl:value-of select="$cos_id"/><![CDATA[,]]><xsl:value-of select="$mod_id"/><![CDATA[,'all',1,']]><xsl:value-of select="$ass_timestamp"/><![CDATA[',']]><xsl:value-of select="$isFromIframe"/><![CDATA[')

			}
			
			$(function(){
				var td  = $(".wzb-ui-table tr:last td")
				for(var i=0;i<td.length;i++){
					$(".wzb-ui-table tr:last td")[i].style.borderBottom="none"
				}
			})
				
]]></SCRIPT>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="new_css"/>
		</head>
		<!-- =============================================================== -->
		<BODY marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="wb_utils_set_cookie('url_prev',self.location.href)">
			<form name="frmSearch" onSubmit="return status()">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</BODY>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_course_list">課程目錄</xsl:with-param>
			<xsl:with-param name="lab_general">其他</xsl:with-param>
			<xsl:with-param name="lab_view_submission">提交詳情</xsl:with-param>
			<xsl:with-param name="lab_score">分數</xsl:with-param>
			<xsl:with-param name="lab_submission_date">提交日期</xsl:with-param>
			<xsl:with-param name="lab_grade">等級</xsl:with-param>
			<xsl:with-param name="lab_A">甲</xsl:with-param>
			<xsl:with-param name="lab_B">乙</xsl:with-param>
			<xsl:with-param name="lab_C">丙</xsl:with-param>
			<xsl:with-param name="lab_D">丁</xsl:with-param>
			<xsl:with-param name="lab_F">不合格</xsl:with-param>
			<xsl:with-param name="lab_late_submission">遲提交</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_no_item">還沒有任何提交</xsl:with-param>
			<xsl:with-param name="lab_item_type">請選擇狀態:</xsl:with-param>
			<xsl:with-param name="lab_sub_all">全部</xsl:with-param>
			<xsl:with-param name="lab_sub_graded">已評分</xsl:with-param>
			<xsl:with-param name="lab_sub_ungraded">尚未評分</xsl:with-param>
			<xsl:with-param name="lab_sub_not_submit">尚未遞交</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_return">返回</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_course_list">课程目录</xsl:with-param>
			<xsl:with-param name="lab_general">其他</xsl:with-param>
			<xsl:with-param name="lab_view_submission">提交详情</xsl:with-param>
			<xsl:with-param name="lab_score">分数</xsl:with-param>
			<xsl:with-param name="lab_grade">等级</xsl:with-param>
			<xsl:with-param name="lab_submission_date">提交日期</xsl:with-param>
			<xsl:with-param name="lab_A">甲</xsl:with-param>
			<xsl:with-param name="lab_B">乙</xsl:with-param>
			<xsl:with-param name="lab_C">丙</xsl:with-param>
			<xsl:with-param name="lab_D">丁</xsl:with-param>
			<xsl:with-param name="lab_F">不合格</xsl:with-param>
			<xsl:with-param name="lab_late_submission">迟提交</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_no_item">还没有任何记录</xsl:with-param>
			<xsl:with-param name="lab_item_type">请选择状态：</xsl:with-param>
			<xsl:with-param name="lab_sub_all">全部</xsl:with-param>
			<xsl:with-param name="lab_sub_graded">已评分</xsl:with-param>
			<xsl:with-param name="lab_sub_ungraded">尚未评分</xsl:with-param>
			<xsl:with-param name="lab_sub_not_submit">尚未递交</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_return">返回</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_course_list">Course list</xsl:with-param>
			<xsl:with-param name="lab_general">General</xsl:with-param>
			<xsl:with-param name="lab_view_submission">Submission details</xsl:with-param>
			<xsl:with-param name="lab_score">Score</xsl:with-param>
			<xsl:with-param name="lab_submission_date">Submission date</xsl:with-param>
			<xsl:with-param name="lab_grade">Grade</xsl:with-param>
			<xsl:with-param name="lab_A">A</xsl:with-param>
			<xsl:with-param name="lab_B">B</xsl:with-param>
			<xsl:with-param name="lab_C">C</xsl:with-param>
			<xsl:with-param name="lab_D">D</xsl:with-param>
			<xsl:with-param name="lab_F">Fail</xsl:with-param>
			<xsl:with-param name="lab_late_submission">Late submission</xsl:with-param>
			<xsl:with-param name="lab_yes">Y</xsl:with-param>
			<xsl:with-param name="lab_no">N</xsl:with-param>
			<xsl:with-param name="lab_no_item">No submission found</xsl:with-param>
			<xsl:with-param name="lab_item_type">Please select status:</xsl:with-param>
			<xsl:with-param name="lab_sub_all">All</xsl:with-param>
			<xsl:with-param name="lab_sub_graded">Graded</xsl:with-param>
			<xsl:with-param name="lab_sub_ungraded">Not graded</xsl:with-param>
			<xsl:with-param name="lab_sub_not_submit">Not submitted</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_return">return</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_course_list"/>
		<xsl:param name="lab_general"/>
		<xsl:param name="lab_view_submission"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_submission_date"/>
		<xsl:param name="lab_grade"/>
		<xsl:param name="lab_A"/>
		<xsl:param name="lab_B"/>
		<xsl:param name="lab_C"/>
		<xsl:param name="lab_D"/>
		<xsl:param name="lab_F"/>
		<xsl:param name="lab_late_submission"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_item_type"/>
		<xsl:param name="lab_sub_all"/>
		<xsl:param name="lab_sub_graded"/>
		<xsl:param name="lab_sub_ungraded"/>
		<xsl:param name="lab_sub_not_submit"/>
		<xsl:param name="lab_g_form_btn_close"/>
		
		<!-- nav -->
		<xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">120</xsl:with-param>
		</xsl:call-template>
		
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">HOMEWORK_CORRECTION</xsl:with-param>
		</xsl:call-template>
		
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:apply-templates select="/enrolment_list/item/nav/item" mode="nav">
					<xsl:with-param name="lab_run_info"><xsl:value-of select="$label_core_training_management_250" /></xsl:with-param>
				</xsl:apply-templates>
				<span class="NavLink">
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<a href="javascript:itm_lst.itm_evaluation_report({$cos_id },'TST')"><xsl:value-of select="$label_core_training_management_393"/></a>
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:value-of select="$lab_view_submission"/><xsl:text>&#160;-&#160;</xsl:text><xsl:value-of select="header/title/text()"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<!-- nav end -->
		
		<xsl:call-template name="wb_ui_desc">
			<!-- <xsl:with-param name="text" select="$lab_view_submission" /> -->
		</xsl:call-template>
		<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td width="660">
					<span class="TitleText">
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$lab_item_type"/>
					</span>
					<xsl:text>&#160;</xsl:text>
					<span>
						<select name="type_sel_frm" class="Select" onchange="select_type(document.frmSearch)">
							<option value="graded">
								<!--<xsl:attribute name="selected">selected</xsl:attribute>-->
								<xsl:value-of select="$lab_sub_graded"/> (<xsl:value-of select="stat/queue[@name = 'graded']/@count"/>)									
								</option>
							<option value="ungraded">
								<xsl:value-of select="$lab_sub_ungraded"/> (<xsl:value-of select="stat/queue[@name = 'not_graded']/@count"/>)									
								</option>
							<option value="not_submitted">
								<xsl:value-of select="$lab_sub_not_submit"/> (<xsl:value-of select="stat/queue[@name = 'not_submitted']/@count"/>)									
								</option>
							<option value="all">
								<!--<xsl:attribute name="selected">selected</xsl:attribute>-->
								<xsl:value-of select="$lab_sub_all"/> (<xsl:value-of select="stat/queue[@name = 'all']/@count"/>)									
								</option>
						</select>
					</span>
				</td>
			</tr>
		</table>
		<xsl:choose>
			<xsl:when test="count(record[progress/@status = 'GRADED']) > 0">
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td>
							<span class="TitleText">
								<xsl:value-of select="$lab_dis_name"/>
							</span>
						</td>
						<td>
							<span class="TitleText">
								<xsl:value-of select="$lab_group"/>
							</span>
						</td>
						<td>
							<span class="TitleText">
								<xsl:value-of select="$lab_submission_date"/>
							</span>
						</td>
						<td align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_late_submission"/>
							</span>
						</td>
						<td align="right">
							<span class="TitleText">
								<xsl:choose>
									<xsl:when test="header/@max_score = -1 ">
										<xsl:value-of select="$lab_grade"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_score"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
					</tr>
					<xsl:call-template name="do_rol">
						<xsl:with-param name="lab_A"><xsl:value-of select="$lab_A"/></xsl:with-param>
						<xsl:with-param name="lab_B"><xsl:value-of select="$lab_B"/></xsl:with-param>
						<xsl:with-param name="lab_C"><xsl:value-of select="$lab_C"/></xsl:with-param>
						<xsl:with-param name="lab_D"><xsl:value-of select="$lab_D"/></xsl:with-param>
						<xsl:with-param name="lab_F"><xsl:value-of select="$lab_F"/></xsl:with-param>
						<xsl:with-param name="lab_yes"><xsl:value-of select="$lab_yes"/></xsl:with-param>
						<xsl:with-param name="lab_no"><xsl:value-of select="$lab_no"/></xsl:with-param>
					</xsl:call-template>
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="total" select="$total"/>
					<xsl:with-param name="cur_page_name">page</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_item"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="do_rol">
		<xsl:param name="lab_A"/>
		<xsl:param name="lab_B"/>
		<xsl:param name="lab_C"/>
		<xsl:param name="lab_D"/>
		<xsl:param name="lab_F"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:for-each select="record[progress/@status = 'GRADED'] ">
			<xsl:variable name="row_class">
				<xsl:choose>
					<xsl:when test="position() mod 2">RowsEven</xsl:when>
					<xsl:otherwise>RowsOdd</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<tr class="{$row_class}">
				<td>
					<a href="javascript:ass.grade({$mod_id},'{entity/@ent_id}','{progress/@tkh_id}','','',{$cos_id})" class="Text">
						<xsl:value-of select="entity"/>  (<xsl:value-of select="@usr_id"/>)</a>
				</td>
				<td>
					<span class="Text">
						<xsl:value-of select="full_path"/>
					</span>
				</td>
				<td>
					<span class="Text">
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp"><xsl:value-of select="progress/@complete_datetime"/></xsl:with-param>
							<xsl:with-param name="dis_time">T</xsl:with-param>
						</xsl:call-template>
					</span>
				</td>
				<td align="center">
					<span class="Text">
						<xsl:choose>
							<xsl:when test=" (translate(substring-before(progress/@due_datetime,'.'),' -:','')) &lt; translate(substring-before(progress/@complete_datetime,'.'),' -:','')">
								<xsl:value-of select="$lab_yes"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_no"/>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
				<td align="right">
					<span class="Text">
						<xsl:choose>
							<xsl:when test="progress/@pgr_max_score = -1">
								<xsl:variable name="grade" select="progress/@grade"/>
								<xsl:choose>
									<xsl:when test="$grade = 'A+' ">
										<xsl:value-of select="$lab_A"/>+</xsl:when>
									<xsl:when test="$grade = 'A' ">
										<xsl:value-of select="$lab_A"/>
									</xsl:when>
									<xsl:when test="$grade = 'A-' ">
										<xsl:value-of select="$lab_A"/>-</xsl:when>
									<xsl:when test="$grade = 'B+' ">
										<xsl:value-of select="$lab_B"/>+</xsl:when>
									<xsl:when test="$grade = 'B' ">
										<xsl:value-of select="$lab_B"/>
									</xsl:when>
									<xsl:when test="$grade = 'B-' ">
										<xsl:value-of select="$lab_B"/>-</xsl:when>
									<xsl:when test="$grade = 'C+' ">
										<xsl:value-of select="$lab_C"/>+</xsl:when>
									<xsl:when test="$grade = 'C' ">
										<xsl:value-of select="$lab_C"/>
									</xsl:when>
									<xsl:when test="$grade = 'C-' ">
										<xsl:value-of select="$lab_C"/>-</xsl:when>
									<xsl:when test="$grade = 'D' ">
										<xsl:value-of select="$lab_D"/>
									</xsl:when>
									<xsl:when test="$grade = 'F' ">
										<xsl:value-of select="$lab_F"/>
									</xsl:when>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="progress/@pgr_score"/>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
			</tr>
		</xsl:for-each>
		
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="env"/>
	<xsl:template match="cur_usr"/>
</xsl:stylesheet>
