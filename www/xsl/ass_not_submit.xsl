<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java"
	exclude-result-prefixes="java">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_win_hdr.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>	
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="share/itm_nav_share.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>	
	<xsl:output indent="yes"/>
	<xsl:variable name="pos">0</xsl:variable>
	<xsl:variable name="mytype" select="/enrolment_list/header/@subtype"/>
	<xsl:variable name="mod_id" select="/enrolment_list/@mod_id"/>
	<xsl:variable name="cos_id" select="/enrolment_list/@cos_id"/>
	<xsl:variable name="ass_timestamp" select="/enrolment_list/stat/@time"/>
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
			<xsl:if test="@name = 'not_submitted' ">
				<xsl:value-of select="@count"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:variable>
	<xsl:variable name="_ent_id" select="/enrolment_list/record/entity/@ent_id"/>
	<xsl:variable name="label_core_training_management_250" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_250')"/>
	<xsl:variable name="label_core_training_management_393" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_393')"/>
	<xsl:variable name="label_core_training_management_30" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_30')"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="enrolment_list">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_assignment.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
			var course_lst = new wbCourse;
			var ass = new wbAssignment;
			cur_page = 	]]><xsl:value-of select="$cur_page"/><![CDATA[
			ass_timestamp = ']]><xsl:value-of select="$ass_timestamp"/><![CDATA['
			queue = 'not_submitted'	
	
	
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
			
					
		]]></script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="new_css"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="wb_utils_set_cookie('url_prev',self.location.href)">
			<form name="frmSearch" onSubmit="return status()">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_course_list">課程目錄</xsl:with-param>
			<xsl:with-param name="lab_view_submission">提交詳情</xsl:with-param>
			<xsl:with-param name="lab_no_item">還沒有任何提交</xsl:with-param>
			<xsl:with-param name="lab_item_type">請選擇狀態:</xsl:with-param>
			<xsl:with-param name="lab_sub_all">全部</xsl:with-param>
			<xsl:with-param name="lab_sub_graded">已評分</xsl:with-param>
			<xsl:with-param name="lab_sub_ungraded">尚未評分</xsl:with-param>
			<xsl:with-param name="lab_sub_not_submit">尚未遞交</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_course_list">课程目录</xsl:with-param>
			<xsl:with-param name="lab_view_submission">提交详情</xsl:with-param>
			<xsl:with-param name="lab_no_item">还没有任何提交</xsl:with-param>
			<xsl:with-param name="lab_item_type">请选择状态：</xsl:with-param>
			<xsl:with-param name="lab_sub_all">全部</xsl:with-param>
			<xsl:with-param name="lab_sub_graded">已评分</xsl:with-param>
			<xsl:with-param name="lab_sub_ungraded">尚未评分</xsl:with-param>
			<xsl:with-param name="lab_sub_not_submit">尚未递交</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_course_list">Course list</xsl:with-param>
			<xsl:with-param name="lab_view_submission">Submission details</xsl:with-param>
			<xsl:with-param name="lab_no_item">All submitted</xsl:with-param>
			<xsl:with-param name="lab_item_type">Please select status:</xsl:with-param>
			<xsl:with-param name="lab_sub_all">All</xsl:with-param>
			<xsl:with-param name="lab_sub_graded">Graded</xsl:with-param>
			<xsl:with-param name="lab_sub_ungraded">Not graded</xsl:with-param>
			<xsl:with-param name="lab_sub_not_submit">Not submitted</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_course_list"/>
		<xsl:param name="lab_view_submission"/>
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
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">HOMEWORK_CORRECTION</xsl:with-param>
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
								<xsl:value-of select="$lab_sub_graded"/> (<xsl:value-of select="stat/queue[@name = 'graded']/@count"/>)									
						</option>
							<option value="ungraded">
								<xsl:value-of select="$lab_sub_ungraded"/> (<xsl:value-of select="stat/queue[@name = 'not_graded']/@count"/>)									
						</option>
							<option value="not_submitted">
								<xsl:attribute name="selected">selected</xsl:attribute>
								<xsl:value-of select="$lab_sub_not_submit"/> (<xsl:value-of select="stat/queue[@name = 'not_submitted']/@count"/>)									
						</option>
							<option value="all">
								<xsl:value-of select="$lab_sub_all"/> (<xsl:value-of select="stat/queue[@name = 'all']/@count"/>)									
						</option>
						</select>
					</span>
				</td>
			</tr>
		</table>
		<!-- check no item -->
		<xsl:choose>
			<xsl:when test="count(record[progress/@status = 'Not Submitted Yet'])">
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td>
							<span class="TitleText">
								<xsl:value-of select="$lab_dis_name"/>
							</span>
						</td>
						<td align="right">
							<span class="TitleText">
								<xsl:value-of select="$lab_group"/>
							</span>
						</td>
					</tr>
					<xsl:call-template name="do_rol"/>
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
		<xsl:for-each select="record[progress/@status = 'Not Submitted Yet'] ">
			<xsl:variable name="row_class">
				<xsl:choose>
					<xsl:when test="position() mod 2">RowsEven</xsl:when>
					<xsl:otherwise>RowsOdd</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<tr class="{$row_class}">
				<td>
					<span class="wbRowText">
						<xsl:value-of select="entity"/>(<xsl:value-of select="@usr_id"/>)</span>
				</td>
				<td  align="right">
					<span class="wbRowText">
						<xsl:choose>
							<xsl:when test="full_path != ''">
								<xsl:value-of select="full_path"/>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
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
