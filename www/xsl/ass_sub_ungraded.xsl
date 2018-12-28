<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java"
	exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="share/itm_nav_share.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>	
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="pos">0</xsl:variable>
	<xsl:variable name="mytype" select="/enrolment_list/header/@subtype"/>
	<xsl:variable name="mod_id" select="/enrolment_list/@mod_id"/>
	<xsl:variable name="cos_id" select="/enrolment_list/@cos_id"/>
	<xsl:variable name="isFromIframe" select="/enrolment_list/isFromIframe"/>
	<xsl:variable name="ass_timestamp" select="/enrolment_list/stat/@time"/>
	<xsl:variable name="due_date" select="translate(substring-before(/enrolment_list/header/@due_datetime,'.'),' -:','')"/>
	<xsl:variable name="count_last">0<xsl:value-of select="count(/enrolment_list/record[progress/@status = 'NOT GRADED'])"/>
	</xsl:variable>
	<xsl:variable name="page_size" select="/enrolment_list/stat/@page_size"/>
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
			<xsl:if test="@name = 'not_graded' ">
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
			
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js" language="JavaScript"/>
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js" language="JavaScript"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			var course_lst = new wbCourse;
			var ass = new wbAssignment;
			var url;
			cur_page = 	]]><xsl:value-of select="$cur_page"/><![CDATA[
			ass_timestamp = ']]><xsl:value-of select="$ass_timestamp"/><![CDATA['
			queue = 'not_graded'	


			function selectAll() {
				frm = document.frmXml
				
				if (frm.select_all.checked)
				checkedVal = 1
				else
				checkedVal = 0; 
				var i, n, ele
				n = frm.elements.length;
				for (i = 0; i < n; i++) {
					ele = frm.elements[i]
					if (ele.type == "checkbox"){
						if(ele.name != "select_all")
			
						ele.checked = checkedVal;
						}
					}
			}
	
			function select_type(frm){
				select_val = frm.type_sel_frm.options[frm.type_sel_frm.selectedIndex].value
				
				if (select_val == 'graded')
					ass.view_submission_graded(]]><xsl:value-of select="$cos_id"/><![CDATA[,]]><xsl:value-of select="$mod_id"/><![CDATA[,'graded',1,']]><xsl:value-of select="$ass_timestamp"/><![CDATA[',']]><xsl:value-of select="$isFromIframe"/><![CDATA[')
				else if (select_val == 'ungraded')
					ass.view_submission_ungraded(]]><xsl:value-of select="$cos_id"/><![CDATA[,]]><xsl:value-of select="$mod_id"/><![CDATA[,'not_graded',1,']]><xsl:value-of select="$ass_timestamp"/><![CDATA[',']]><xsl:value-of select="$isFromIframe"/><![CDATA[')
				else if (select_val == 'not_submit')
					ass.view_submission_not_submit(]]><xsl:value-of select="$cos_id"/><![CDATA[,]]><xsl:value-of select="$mod_id"/><![CDATA[,'not_submit',1,']]><xsl:value-of select="$ass_timestamp"/><![CDATA[',']]><xsl:value-of select="$isFromIframe"/><![CDATA[')
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
		<BODY marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="wb_utils_set_cookie('url_prev',self.location.href)">
			<FORM name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
			</FORM>
		</BODY>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_general">其他</xsl:with-param>
			<xsl:with-param name="lab_view_submission">提交詳情</xsl:with-param>
			<xsl:with-param name="lab_submission_date">提交日期</xsl:with-param>
			<xsl:with-param name="lab_download">下載</xsl:with-param>
			<xsl:with-param name="lab_late_submission">遲提交</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_no_item">還沒有任何提交</xsl:with-param>
			<xsl:with-param name="lab_item_type">請選擇狀態:</xsl:with-param>
			<xsl:with-param name="lab_sub_all">全部</xsl:with-param>
			<xsl:with-param name="lab_sub_graded">已評分</xsl:with-param>
			<xsl:with-param name="lab_sub_ungraded">尚未評分</xsl:with-param>
			<xsl:with-param name="lab_sub_not_submit">尚未遞交</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_download">下載</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_general">其他</xsl:with-param>
			<xsl:with-param name="lab_view_submission">提交详情</xsl:with-param>
			<xsl:with-param name="lab_submission_date">提交日期</xsl:with-param>
			<xsl:with-param name="lab_download">下载</xsl:with-param>
			<xsl:with-param name="lab_late_submission">迟提交</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_no_item">还没有任何提交</xsl:with-param>
			<xsl:with-param name="lab_item_type">请选择状态：</xsl:with-param>
			<xsl:with-param name="lab_sub_all">全部</xsl:with-param>
			<xsl:with-param name="lab_sub_graded">已评分</xsl:with-param>
			<xsl:with-param name="lab_sub_ungraded">尚未评分</xsl:with-param>
			<xsl:with-param name="lab_sub_not_submit">尚未递交</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_download">下载</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_general">General</xsl:with-param>
			<xsl:with-param name="lab_view_submission">Submission details</xsl:with-param>
			<xsl:with-param name="lab_submission_date">Submission date</xsl:with-param>
			<xsl:with-param name="lab_download">Download</xsl:with-param>
			<xsl:with-param name="lab_late_submission">Late submission</xsl:with-param>
			<xsl:with-param name="lab_yes">Y</xsl:with-param>
			<xsl:with-param name="lab_no">N</xsl:with-param>
			<xsl:with-param name="lab_no_item">No submission found</xsl:with-param>
			<xsl:with-param name="lab_item_type">Please select status:</xsl:with-param>
			<xsl:with-param name="lab_sub_all">All</xsl:with-param>
			<xsl:with-param name="lab_sub_graded">Graded</xsl:with-param>
			<xsl:with-param name="lab_sub_ungraded">Not graded</xsl:with-param>
			<xsl:with-param name="lab_sub_not_submit">Not submitted</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_download">Download</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_general"/>
		<xsl:param name="lab_view_submission"/>
		<xsl:param name="lab_submission_date"/>
		<xsl:param name="lab_download"/>
		<xsl:param name="lab_late_submission"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_item_type"/>
		<xsl:param name="lab_sub_all"/>
		<xsl:param name="lab_sub_graded"/>
		<xsl:param name="lab_sub_ungraded"/>
		<xsl:param name="lab_sub_not_submit"/>
		<xsl:param name="lab_g_txt_btn_download"/>
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
			<!-- <xsl:with-param name="text" select="$lab_view_submission"/> -->
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
						<select name="type_sel_frm" class="Select" onchange="select_type(document.frmXml)">
							<option value="graded">
								<xsl:value-of select="$lab_sub_graded"/> (<xsl:value-of select="stat/queue[@name = 'graded']/@count"/>)									
								</option>
							<option value="ungraded">
								<xsl:attribute name="selected">selected</xsl:attribute>
								<xsl:value-of select="$lab_sub_ungraded"/> (<xsl:value-of select="stat/queue[@name = 'not_graded']/@count"/>)									
								</option>
							<option value="not_submit">
								<xsl:value-of select="$lab_sub_not_submit"/> (<xsl:value-of select="stat/queue[@name = 'not_submitted']/@count"/>)									
								</option>
							<option value="all">
								<xsl:value-of select="$lab_sub_all"/> (<xsl:value-of select="stat/queue[@name = 'all']/@count"/>)									
								</option>
						</select>
					</span>
				</td>
				<td align="right">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
					<xsl:if test="$count_last > 0">
						<xsl:call-template name="wb_gen_button">
						    <xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_download"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:ass.download(document.frmXml,'<xsl:value-of select="$mod_id"/>','<xsl:value-of select="$wb_lang"/>','','true')</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
				</td>
			</tr>
		</table>
		<!--=========check no item=========-->
		<xsl:choose>
			<xsl:when test="count(record[progress/@status = 'NOT GRADED'])">
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td>
							<xsl:call-template name="select_all_checkbox">
								<xsl:with-param name="chkbox_lst_cnt"><xsl:value-of select="count(record[progress/@status = 'NOT GRADED'])"/></xsl:with-param>
								<xsl:with-param name="display_icon">false</xsl:with-param>
								<xsl:with-param name="chkbox_lst_nm">tkh_id</xsl:with-param>
								<xsl:with-param name="frm_name">frmXml</xsl:with-param>
							</xsl:call-template>
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
						<td align="right">
							<span class="TitleText">
								<xsl:value-of select="$lab_late_submission"/>
							</span>
						</td>
					</tr>
					<xsl:call-template name="do_rol">
						<xsl:with-param name="lab_yes" select="$lab_yes"/>
						<xsl:with-param name="lab_no" select="$lab_no"/>
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
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:for-each select="record[progress/@status = 'NOT GRADED'] ">
			<xsl:variable name="row_class">
				<xsl:choose>
					<xsl:when test="position() mod 2">RowsEven</xsl:when>
					<xsl:otherwise>RowsOdd</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<tr class="{$row_class}">
				<td>
					<input type="checkbox" name="tkh_id" value="{progress/@tkh_id}" id="tkh_id"/>
					<a href="javascript:ass.grade({$mod_id},'{entity/@ent_id}','{progress/@tkh_id}','','',{$cos_id})" class="Text">
						<xsl:value-of select="entity"/>   (<xsl:value-of select="@usr_id"/>)</a>
				</td>
				<td>
					<span class="Text">
						<xsl:choose>
							<xsl:when test="full_path != ''">
								<xsl:value-of select="full_path"/>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
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
				<td align="right">
					<span class="Text">
						<xsl:choose>
							<xsl:when test=" (translate(substring-before(progress/@due_datetime,'.'),' -:','')) &lt; (translate(substring-before(progress/@complete_datetime,'.'),' -:',''))">
								<xsl:value-of select="$lab_yes"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_no"/>
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
