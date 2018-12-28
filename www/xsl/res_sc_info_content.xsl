<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/trun_timestamp.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/escape_sing_quo.xsl"/>
	<xsl:import href="share/wb_module_type_const.xsl"/>
	<xsl:import href="share/res_label_share.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="que_id" select="/*/@id"/>
	<xsl:variable name="course_id" select="/*/header/@course_id"/>		
	<xsl:variable name="timestamp" select="/*/@timestamp"/>
	<xsl:variable name="status" select="/*/header/@status"/>
	<xsl:variable name="mod_type" select="/*/header/@subtype"/>
	<xsl:variable name="cur_tpl_nm" select="/*/header/template_list/@cur_tpl"/>
	<xsl:variable name="cur_tpl" select="/*/header/template_list/template[@name=$cur_tpl_nm]"/>
	<xsl:variable name="stylesheet" select="/*/header/template_list/template[@name=$cur_tpl_nm]/stylesheet"/>
	<xsl:variable name="mode" select="/*/@view_mode"/>
	
	<xsl:variable name="write_enable">
		<xsl:choose>
			<xsl:when test="/*/cur_usr/@ent_id =  /*/acl/@ent_id and /*/acl/@write = 'true'">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="wb_gen_table_width" select="$wb_frame_table_width"/>
	
	<!-- =============================================================== -->
	<xsl:variable name="deleted">DELETED</xsl:variable>
	<xsl:variable name="ok">OK</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="fixed_scenario | dynamic_scenario">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_module.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_objective.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_course.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_scenario.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js" type="text/javascript"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				wb_tst = new  wbTst
				course_lst = new wbCourse
				module_lst = new wbModule 
				obj = new wbObjective
				sc = new wbScControl;
				
				function sum_score(q_score,q_count) {
					return parseInt(q_score) * parseInt(q_count);
				}

				]]></SCRIPT>
			<xsl:call-template name="new_css"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frm_obj" method="post">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_test_summary">題目摘要</xsl:with-param>
			<xsl:with-param name="lab_no_of_que_header">題目總數</xsl:with-param>
			<xsl:with-param name="lab_dsc_spec_score">每題分數</xsl:with-param>
			<xsl:with-param name="lab_no_of_que_body">題目數量</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_total_score">總分</xsl:with-param>
			<xsl:with-param name="lab_spec_que_req">抽題數量</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>	
			<xsl:with-param name="lab_g_txt_btn_add_criteria">新增條件</xsl:with-param>	
			<xsl:with-param name="lab_g_txt_btn_validate">檢查條件</xsl:with-param>	
			<xsl:with-param name="lab_section_criteria">抽題條件</xsl:with-param>	
			<xsl:with-param name="lab_spec_que_count">符合條件題目數量</xsl:with-param>
			<xsl:with-param name="lab_que_categories">題目分佈</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_que">新增題目</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove_que">刪除題目</xsl:with-param>
			<xsl:with-param name="lab_no_que_added">還沒有任何題目</xsl:with-param>
			<xsl:with-param name="lab_question_type">題目類型</xsl:with-param>
			<xsl:with-param name="lab_difficulty">難度</xsl:with-param>
			<xsl:with-param name="lab_score_of_each_que">每題分數</xsl:with-param>
			<xsl:with-param name="lab_hard">困難</xsl:with-param>
			<xsl:with-param name="lab_obj_status">已刪除</xsl:with-param>		
			<xsl:with-param name="lab_spec_desc">當符合條件題目數量以紅色顯示時，表明當前抽題條件無法被滿足。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_test_summary">题目摘要</xsl:with-param>
			<xsl:with-param name="lab_no_of_que_header">题目总数</xsl:with-param>
			<xsl:with-param name="lab_que_categories">题目分布</xsl:with-param>
			<xsl:with-param name="lab_dsc_spec_score">每题分数</xsl:with-param>
			<xsl:with-param name="lab_no_of_que_body">题目</xsl:with-param>
			<xsl:with-param name="lab_total_score">总分</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_que">添加题目</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">删除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove_que">删除题目</xsl:with-param>			
			<xsl:with-param name="lab_no_que_added">尚未添加题目</xsl:with-param>
			<xsl:with-param name="lab_question_type">题目类型</xsl:with-param>
			<xsl:with-param name="lab_difficulty">难度</xsl:with-param>
			<xsl:with-param name="lab_score_of_each_que">每题分数</xsl:with-param>	
			<xsl:with-param name="lab_spec_que_req">抽题数量</xsl:with-param>
			<xsl:with-param name="lab_spec_que_count">符合条件题目数量</xsl:with-param>
			<xsl:with-param name="lab_hard">困难</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>	
			<xsl:with-param name="lab_g_txt_btn_add_criteria">添加条件</xsl:with-param>	
			<xsl:with-param name="lab_g_txt_btn_validate">检查条件</xsl:with-param>	
			<xsl:with-param name="lab_section_criteria">抽题条件</xsl:with-param>	
			<xsl:with-param name="lab_obj_status">已删除</xsl:with-param>
			<xsl:with-param name="lab_spec_desc">当符合条件题目数量以红色显示时，表明当前抽题条件无法被满足。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_test_summary">Question summary</xsl:with-param>
			<xsl:with-param name="lab_no_of_que_header">Total number of questions</xsl:with-param>
			<xsl:with-param name="lab_que_categories">Question folders</xsl:with-param>
			<xsl:with-param name="lab_dsc_spec_score">Score per question</xsl:with-param>
			<xsl:with-param name="lab_no_of_que_body">No. of questions</xsl:with-param>
			<xsl:with-param name="lab_total_score">Total score</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_que">Add question</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove_que">Remove question</xsl:with-param>
			<xsl:with-param name="lab_no_que_added">No questions added</xsl:with-param>
			<xsl:with-param name="lab_question_type">Question type</xsl:with-param>
			<xsl:with-param name="lab_difficulty">Difficulty</xsl:with-param>
			<xsl:with-param name="lab_score_of_each_que">Score</xsl:with-param>	
			<xsl:with-param name="lab_spec_que_req">Questions required</xsl:with-param>
			<xsl:with-param name="lab_spec_que_count">Questions available</xsl:with-param>
			<xsl:with-param name="lab_hard">Hard</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">Edit</xsl:with-param>	
			<xsl:with-param name="lab_g_txt_btn_add_criteria">Add criteria</xsl:with-param>	
			<xsl:with-param name="lab_g_txt_btn_validate">Validate</xsl:with-param>	
			<xsl:with-param name="lab_section_criteria">Selection criteria</xsl:with-param>
			<xsl:with-param name="lab_obj_status">Deleted</xsl:with-param>		
			<xsl:with-param name="lab_spec_desc">If a criterion cannot be fulfilled, its value under "Questions available" will be shown in red.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_test_summary"/>
		<xsl:param name="lab_no_of_que_header"/>
		<xsl:param name="lab_que_categories"/>
		<xsl:param name="lab_dsc_spec_score"/>
		<xsl:param name="lab_no_of_que_body"/>
		<xsl:param name="lab_total_score"/>
		<xsl:param name="lab_g_txt_btn_add_que"/>
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_g_txt_btn_remove_que"/>
		<xsl:param name="lab_no_que_added"/>
		<xsl:param name="lab_question_type"/>
		<xsl:param name="lab_difficulty"/>
		<xsl:param name="lab_score_of_each_que"/>
		<xsl:param name="lab_spec_que_req"/>
		<xsl:param name="lab_spec_que_count"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_add_criteria"/>
		<xsl:param name="lab_g_txt_btn_validate"/>
		<xsl:param name="lab_section_criteria"/>
		<xsl:param name="lab_obj_status"/>
		<xsl:param name="lab_spec_desc"/>
		
		<xsl:apply-templates select="header">
			<xsl:with-param name="lab_test_summary" select="$lab_test_summary"/>
			<xsl:with-param name="lab_no_of_que" select="$lab_no_of_que_header"/>
			<xsl:with-param name="lab_total_score" select="$lab_total_score"/>
		</xsl:apply-templates>
		<xsl:if test="$mod_type='DSC'">
			<xsl:apply-templates select="body">
				<xsl:with-param name="lab_que_categories" select="$lab_que_categories"/>
				<xsl:with-param name="lab_dsc_spec_score" select="$lab_dsc_spec_score"/>
				<xsl:with-param name="lab_no_of_que" select="$lab_no_of_que_body"/>
				<xsl:with-param name="lab_total_score" select="$lab_total_score"/>
				<xsl:with-param name="lab_g_txt_btn_add_que" select="$lab_g_txt_btn_add_que"/>
				<xsl:with-param name="lab_g_txt_btn_remove" select="$lab_g_txt_btn_remove"/>
				<xsl:with-param name="lab_g_txt_btn_remove_que" select="$lab_g_txt_btn_remove_que"/>
				<xsl:with-param name="lab_no_que_added" select="$lab_no_que_added"/>
				<xsl:with-param name="lab_question_type" select="$lab_question_type"/>
				<xsl:with-param name="lab_difficulty" select="$lab_difficulty"/>
				<xsl:with-param name="lab_score_of_each_que" select="$lab_score_of_each_que"/>
				<xsl:with-param name="lab_spec_que_req" select="$lab_spec_que_req"/>
				<xsl:with-param name="lab_spec_que_count" select="$lab_spec_que_count"/>
				<xsl:with-param name="lab_hard" select="$lab_hard"/>
				<xsl:with-param name="lab_g_txt_btn_edit" select="$lab_g_txt_btn_edit"/>
				<xsl:with-param name="lab_g_txt_btn_add_criteria" select="$lab_g_txt_btn_add_criteria"/>
				<xsl:with-param name="lab_g_txt_btn_validate" select="$lab_g_txt_btn_validate"/>
				<xsl:with-param name="lab_section_criteria" select="$lab_section_criteria"/>
				<xsl:with-param name="lab_obj_status" select="$lab_obj_status"/>
				<xsl:with-param name="lab_spec_desc" select="$lab_spec_desc"/>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="header">
		<xsl:param name="lab_test_summary"/>
		<xsl:param name="lab_no_of_que"/>
		<xsl:param name="lab_total_score"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_test_summary"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
			<tr>
				<td height="10" colspan="2">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
			<tr>
				<td align="right" width="25%" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_no_of_que"/>:</span>
				</td>
				<td width="75%">
					<span class="Text">
						<xsl:choose>
							<xsl:when test="$mod_type = 'DSC'"><xsl:value-of select="sum(../criterion_list/criterion/@q_count)"/></xsl:when>
							<xsl:when test="$mod_type='FSC'"><xsl:value-of select="count(/fixed_scenario/question_list/question)"/></xsl:when>
						</xsl:choose>
					</span>
				</td>
			</tr>
			<tr>
				<td align="right" width="25%" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_total_score"/>:</span>
				</td>
				<td width="75%">
					<span class="Text">
					<xsl:choose>
						<xsl:when test="$mod_type = 'DSC'">
							<script>
								<![CDATA[var sum=0]]>
								<xsl:for-each select="//criterion">
									<![CDATA[sum+= sum_score(']]><xsl:value-of select="@score"/><![CDATA[',']]><xsl:value-of select="@q_count"/><![CDATA[')]]>
								</xsl:for-each>
								<![CDATA[document.write(sum)]]>
							</script>
						</xsl:when>
						<xsl:when test="$mod_type='FSC'">
							<xsl:value-of select="sum(/fixed_scenario/question_list/question/@score)"/>
						</xsl:when>
					</xsl:choose>						
					</span>
				</td>
			</tr>
			<tr>
				<td height="10" colspan="2">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_line"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="body">
		<xsl:param name="lab_que_categories"/>
		<xsl:param name="lab_dsc_spec_score"/>
		<xsl:param name="lab_no_of_que"/>
		<xsl:param name="lab_total_score"/>
		<xsl:param name="lab_g_txt_btn_add_que"/>
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_g_txt_btn_remove_btn"/>
		<xsl:param name="lab_no_que_added"/>
		<xsl:param name="lab_question_type"/>
		<xsl:param name="lab_difficulty"/>
		<xsl:param name="lab_score_of_each_que"/>
		<xsl:param name="lab_spec_que_req"/>
		<xsl:param name="lab_spec_que_count"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_add_criteria"/>
		<xsl:param name="lab_g_txt_btn_validate"/>
		<xsl:param name="lab_g_txt_btn_remove_que"/>
		<xsl:param name="lab_section_criteria"/>
		<xsl:param name="lab_obj_status"/>
		<xsl:param name="lab_spec_desc"/>
		<xsl:call-template name="wb_ui_space"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_section_criteria"/>
			</xsl:with-param>
			<xsl:with-param name="extra_td">
			<td align="right">
			<xsl:if test="$mod_type = 'DSC'">
				<xsl:if test="not($mode = 'READONLY')">
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_add_criteria"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:sc.add_dsc_spec_prep('<xsl:value-of select="/dynamic_scenario/@id"/>');</xsl:with-param>
				</xsl:call-template>
				</xsl:if>
			</xsl:if>
			</td>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="$mod_type = 'DSC' and count(../criterion_list/criterion) = 0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_que_added"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_line"/>
						<!--=== Draw Objective List ===-->
						<table cellspacing="0" cellpadding="3" border="0" width="{$wb_gen_table_width}" class="Bg">
							<tr class="SecBg">
								<td width="8">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								<td align="right">
									<span class="TitleText">
										<xsl:value-of select="$lab_dsc_spec_score"/>
									</span>
								</td>
								<td align="right">
									<span class="TitleText">
										<xsl:value-of select="$lab_spec_que_req"/>
									</span>
								</td>
								<td align="right">
									<span class="TitleText">
										<xsl:value-of select="$lab_spec_que_count"/>
									</span>
								</td>
								<td>
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								<td width="8">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
							</tr>
							<xsl:for-each select="../criterion_list/criterion">
								<xsl:variable name="row_class">
									<xsl:choose>
										<xsl:when test="position() mod 2 = 1">RowsEven</xsl:when>
										<xsl:otherwise>RowsOdd</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<tr class="{$row_class}">
								<td width="8">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								<td align="right">
									<span class="Text">
										<xsl:value-of select="@score"/>
									</span>
								</td>
								<td align="right">
									<span class="Text">
										<xsl:value-of select="@q_count"/>
									</span>
								</td>
								<td align="right">
								<xsl:choose>
									<xsl:when test="(@valid_q_count &gt; @q_count or @valid_q_count=@q_count) and @valid_q_count!=0">
										<span class="Text">
											<xsl:value-of select="@valid_q_count"/>
										</span>
									</xsl:when>
									<xsl:otherwise>
										<span class="Text">
											<font color="#FF0000">
												<xsl:value-of select="@valid_q_count"/>
											</font>
										</span>
									</xsl:otherwise>
								</xsl:choose>
								</td>
								<td align="right">
								<xsl:if test="not($mode = 'READONLY')">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_edit"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:sc.edit_dsc_spec(<xsl:value-of select="$que_id"/>,<xsl:value-of select="@id"/>)</xsl:with-param>
									</xsl:call-template>
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_remove"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:sc.del_dsc_spec('<xsl:value-of select="//dynamic_scenario/@id"/>','<xsl:value-of select="@id"/>', '<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
									</xsl:call-template>
								</xsl:if>
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								<td width="8">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								</tr>
							</xsl:for-each>
						</table>
				<xsl:call-template name="wb_ui_line"/>
				<span class="Desc"><xsl:value-of select="$lab_spec_desc"/></span>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="cur_usr"/>
	<xsl:template match="acl"/>
</xsl:stylesheet>
