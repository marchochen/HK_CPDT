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
	<xsl:variable name="module_id" select="/*/@id"/>
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
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	
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
	<xsl:template match="module | fixed_assessment | dynamic_que_container">
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
			<script language="JavaScript" src="{$wb_js_path}wb_assessment.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js" type="text/javascript"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				wb_tst = new  wbTst
				course_lst = new wbCourse
				module_lst = new wbModule 
				obj = new wbObjective
				asm = new wbAssessment

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
			<xsl:with-param name="lab_test_summary">試卷摘要</xsl:with-param>
			<xsl:with-param name="lab_no_of_que_header">試題總數</xsl:with-param>
			<xsl:with-param name="lab_que_categories">題目分佈</xsl:with-param>
			<xsl:with-param name="lab_category_name">資源文件夾</xsl:with-param>
			<xsl:with-param name="lab_no_of_que_body">題目數量</xsl:with-param>
			<xsl:with-param name="lab_total_score">總分</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_que">添加題目</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove_que">刪除題目</xsl:with-param>
			<xsl:with-param name="lab_no_que_added">還沒有任何題目</xsl:with-param>
			<xsl:with-param name="lab_question_type">題目類型</xsl:with-param>
			<xsl:with-param name="lab_difficulty">難度</xsl:with-param>
			<xsl:with-param name="lab_score_of_each_que">每條題目分數</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_hard">困難</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_criteria">添加準則</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_validate">檢查條件</xsl:with-param>
			<xsl:with-param name="lab_section_criteria">題目選拔準則</xsl:with-param>
			<xsl:with-param name="lab_obj_status">已刪除</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_test_summary">试卷摘要</xsl:with-param>
			<xsl:with-param name="lab_no_of_que_header">试题总数</xsl:with-param>
			<xsl:with-param name="lab_que_categories">题目分布</xsl:with-param>
			<xsl:with-param name="lab_category_name">资源文件夹</xsl:with-param>
			<xsl:with-param name="lab_no_of_que_body">题目</xsl:with-param>
			<xsl:with-param name="lab_total_score">总分</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_que">添加题目</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">删除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove_que">删除题目</xsl:with-param>
			<xsl:with-param name="lab_no_que_added">尚未添加题目</xsl:with-param>
			<xsl:with-param name="lab_question_type">题目类型</xsl:with-param>
			<xsl:with-param name="lab_difficulty">难度</xsl:with-param>
			<xsl:with-param name="lab_score_of_each_que">每道题目的分值</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_hard">困难</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_criteria">添加条件</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_validate">检查条件</xsl:with-param>
			<xsl:with-param name="lab_section_criteria">抽题条件</xsl:with-param>
			<xsl:with-param name="lab_obj_status">已删除</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_test_summary">Test summary</xsl:with-param>
			<xsl:with-param name="lab_no_of_que_header">Total number of questions</xsl:with-param>
			<xsl:with-param name="lab_que_categories">Question folders</xsl:with-param>
			<xsl:with-param name="lab_category_name">Folder name</xsl:with-param>
			<xsl:with-param name="lab_no_of_que_body">No. of questions</xsl:with-param>
			<xsl:with-param name="lab_total_score">Total score</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_que">Add question</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove_que">Remove question</xsl:with-param>
			<xsl:with-param name="lab_no_que_added">No questions added</xsl:with-param>
			<xsl:with-param name="lab_question_type">Question type</xsl:with-param>
			<xsl:with-param name="lab_difficulty">Difficulty</xsl:with-param>
			<xsl:with-param name="lab_score_of_each_que">Score per question</xsl:with-param>
			<xsl:with-param name="lab_easy">Easy</xsl:with-param>
			<xsl:with-param name="lab_normal">Normal</xsl:with-param>
			<xsl:with-param name="lab_hard">Hard</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_criteria">Add criteria</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_validate">Validate</xsl:with-param>
			<xsl:with-param name="lab_section_criteria">Selection criteria</xsl:with-param>
			<xsl:with-param name="lab_obj_status">Deleted</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_test_summary"/>
		<xsl:param name="lab_no_of_que_header"/>
		<xsl:param name="lab_que_categories"/>
		<xsl:param name="lab_category_name"/>
		<xsl:param name="lab_no_of_que_body"/>
		<xsl:param name="lab_total_score"/>
		<xsl:param name="lab_g_txt_btn_add_que"/>
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_g_txt_btn_remove_que"/>
		<xsl:param name="lab_no_que_added"/>
		<xsl:param name="lab_question_type"/>
		<xsl:param name="lab_difficulty"/>
		<xsl:param name="lab_score_of_each_que"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_add_criteria"/>
		<xsl:param name="lab_g_txt_btn_validate"/>
		<xsl:param name="lab_section_criteria"/>
		<xsl:param name="lab_obj_status"/>
		
		<xsl:apply-templates select="header">
			<xsl:with-param name="lab_test_summary" select="$lab_test_summary"/>
			<xsl:with-param name="lab_no_of_que" select="$lab_no_of_que_header"/>
			<xsl:with-param name="lab_total_score" select="$lab_total_score"/>
		</xsl:apply-templates>
		<xsl:apply-templates select="body">
			<xsl:with-param name="lab_que_categories" select="$lab_que_categories"/>
			<xsl:with-param name="lab_category_name" select="$lab_category_name"/>
			<xsl:with-param name="lab_no_of_que" select="$lab_no_of_que_body"/>
			<xsl:with-param name="lab_total_score" select="$lab_total_score"/>
			<xsl:with-param name="lab_g_txt_btn_add_que" select="$lab_g_txt_btn_add_que"/>
			<xsl:with-param name="lab_g_txt_btn_remove" select="$lab_g_txt_btn_remove"/>
			<xsl:with-param name="lab_g_txt_btn_remove_que" select="$lab_g_txt_btn_remove_que"/>
			<xsl:with-param name="lab_no_que_added" select="$lab_no_que_added"/>
			<xsl:with-param name="lab_question_type" select="$lab_question_type"/>
			<xsl:with-param name="lab_difficulty" select="$lab_difficulty"/>
			<xsl:with-param name="lab_score_of_each_que" select="$lab_score_of_each_que"/>
			<xsl:with-param name="lab_easy" select="$lab_easy"/>
			<xsl:with-param name="lab_normal" select="$lab_normal"/>
			<xsl:with-param name="lab_hard" select="$lab_hard"/>
			<xsl:with-param name="lab_g_txt_btn_edit" select="$lab_g_txt_btn_edit"/>
			<xsl:with-param name="lab_g_txt_btn_add_criteria" select="$lab_g_txt_btn_add_criteria"/>
			<xsl:with-param name="lab_g_txt_btn_validate" select="$lab_g_txt_btn_validate"/>
			<xsl:with-param name="lab_section_criteria" select="$lab_section_criteria"/>
			<xsl:with-param name="lab_obj_status" select="$lab_obj_status"/>
		</xsl:apply-templates>
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
						<xsl:value-of select="$lab_no_of_que"/>：</span>
				</td>
				<td width="75%">
					<span class="Text">
						<xsl:choose>
							<xsl:when test="$mod_type = 'DAS'"><xsl:value-of select="sum(../criterion/@q_count)"/></xsl:when>
							<xsl:otherwise><xsl:value-of select="sum(//objective/@q_count)"/></xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
			</tr>
			<tr>
				<td align="right" width="25%" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_total_score"/>：</span>
				</td>
				<td width="75%">
					<span class="Text">
					<xsl:choose>
						<xsl:when test="$mod_type = 'DXT'">
						<script>
							<![CDATA[var sum=0]]>
							<xsl:for-each select="//objective">
								<![CDATA[sum+= ]]><xsl:value-of select="@q_score * @q_count"/>;
							</xsl:for-each>
							<![CDATA[document.write(sum)]]>
						</script>
						</xsl:when>
						<xsl:when test="$mod_type = 'DAS'">
							<xsl:value-of select="@total_score"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="sum(//objective/@q_score)"/>
						</xsl:otherwise>
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
		<xsl:call-template name="wb_ui_space"/>
		
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="body">
		<xsl:param name="lab_que_categories"/>
		<xsl:param name="lab_category_name"/>
		<xsl:param name="lab_no_of_que"/>
		<xsl:param name="lab_total_score"/>
		<xsl:param name="lab_g_txt_btn_add_que"/>
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_g_txt_btn_remove_btn"/>
		<xsl:param name="lab_no_que_added"/>
		<xsl:param name="lab_question_type"/>
		<xsl:param name="lab_difficulty"/>
		<xsl:param name="lab_score_of_each_que"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_add_criteria"/>
		<xsl:param name="lab_g_txt_btn_validate"/>
		<xsl:param name="lab_g_txt_btn_remove_que"/>
		<xsl:param name="lab_section_criteria"/>
		<xsl:param name="lab_obj_status"/>
		<xsl:call-template name="wb_ui_space"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$mod_type = 'DXT' or $mod_type = 'DAS'"><xsl:value-of select="$lab_section_criteria"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$lab_que_categories"/></xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			
			 
			<xsl:with-param name="extra_td">
			<td align="right">
			<xsl:if test="$mod_type = 'DXT' or $mod_type = 'DAS'">
				<xsl:if test="not($mode = 'READONLY')">
				<xsl:call-template name="wb_gen_button_orange">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_add_criteria"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:wb_tst.ins_obj('<xsl:value-of select="$course_id"/>','<xsl:value-of select="$module_id"/>','<xsl:value-of select="$mod_type"/>');</xsl:with-param>
				</xsl:call-template>
				</xsl:if>
			</xsl:if>
			<xsl:if test="(//@q_count) > 0">
				<xsl:if test="$mod_type = 'DAS'">
					<xsl:call-template name="wb_gen_button_orange">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_validate"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:asm.validate('<xsl:value-of 	select="$module_id"/>');</xsl:with-param>
					</xsl:call-template>
				</xsl:if>	
				<xsl:if test="$mod_type = 'DXT'">
					<xsl:call-template name="wb_gen_button_orange">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_validate"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:wb_tst.validate('<xsl:value-of 	select="$module_id"/>');</xsl:with-param>
					</xsl:call-template>
				</xsl:if>	
			</xsl:if>
			</td>
			
			</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="$mod_type = 'DAS' and count(../criterion) = 0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_que_added"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$mod_type != 'DAS' and count(objective) = 0 and count(../objective) = 0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_que_added"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_line"/>
				<xsl:choose>
					<xsl:when test="$mod_type = 'DXT' or $mod_type = 'DAS'">
						<!--=== Draw DXT Criteria List ===-->
						<table cellspacing="0" cellpadding="3" border="0" width="{$wb_gen_table_width}" class="Bg">
							<tr class="SecBg">
								<td width="8">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								<td>
									<span class="TitleText" style="color:#999;">
										<xsl:value-of select="$lab_category_name"/>
									</span>
								</td>
								<td align="center">
									<span class="TitleText" style="color:#999;">
										<xsl:value-of select="$lab_no_of_que"/>
									</span>
								</td>
								<td align="center">
									<span class="TitleText" style="color:#999;">
										<xsl:value-of select="$lab_question_type"/>
									</span>
								</td>
								<td align="center">
									<span class="TitleText" style="color:#999;">
										<xsl:value-of select="$lab_difficulty"/>
									</span>
								</td>
								<td align="center" >
									<span class="TitleText" style="color:#999;">
										<xsl:value-of select="$lab_score_of_each_que"/>
									</span>
								</td>																							
								<td align="center">
									<span class="TitleText" style="color:#999;">
										<xsl:value-of select="$lab_total_score"/>
									</span>
								</td>
								<td>
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								<td width="8">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
							</tr>
							
							<tr>
								<td colspan="9">
									<xsl:call-template name="wb_ui_line"/>
								</td>
							</tr>
							
							<!-- for DXT -->
							<xsl:for-each select="objective | ../objective">
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
								<td>
								<span class="Text">
									<xsl:value-of select="desc/text()"/>
										<xsl:if test="@status = $deleted"> (<xsl:value-of select="$lab_obj_status" />)</xsl:if>
									</span>
								</td>
								<td align="center">
									<span class="Text">
										<xsl:value-of select="@q_count"/>
									</span>
								</td>						
								<td align="center">
									<span class="Text">
											<xsl:choose>
												<xsl:when test="count(type_list/type) = 4 or count(type_list/type) = 0">
													<xsl:value-of select="$lab_all"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:for-each select="type_list/type">
														<xsl:choose>
															<xsl:when test="text() = 'MC'"><xsl:value-of select="$lab_mc"/></xsl:when>
															<xsl:when test="text() = 'FB'"><xsl:value-of select="$lab_fb"/></xsl:when>
															<xsl:when test="text() = 'TF'"><xsl:value-of select="$lab_tf"/></xsl:when>
															<xsl:when test="text() = 'MT'"><xsl:value-of select="$lab_mt"/></xsl:when>
															<xsl:when test="text() = 'ES'"><xsl:value-of select="$lab_es"/></xsl:when>														
															<xsl:when test="text() = 'FSC'"><xsl:value-of select="$lab_fixed_sc"/></xsl:when>														
															<xsl:when test="text() = 'DSC'"><xsl:value-of select="$lab_dna_sc"/></xsl:when>														
														</xsl:choose>
														<xsl:if test="position() != last()"><xsl:text>,&#160;</xsl:text></xsl:if>
													</xsl:for-each>
												</xsl:otherwise>
											</xsl:choose>
									</span>
								</td>
								<td align="center">
									<span class="Text">
											<xsl:choose>
												<xsl:when test="@difficulty  = '1' ">
													<xsl:value-of select="$lab_easy"/>
												</xsl:when>
												<xsl:when test="@difficulty  = '2' ">
													<xsl:value-of select="$lab_normal"/>
												</xsl:when>
												<xsl:when test="@difficulty  = '3' ">
													<xsl:value-of select="$lab_hard"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$lab_all"/>
												</xsl:otherwise>
											</xsl:choose>	
									</span>
								</td>								
								<td align="center">
									<span class="Text">
										<xsl:value-of select="@q_score"/>
									</span>
								</td>									
								<td align="center">
									<span class="Text">
										<xsl:value-of select="@q_score * @q_count"/>
									</span>
								</td>						
								<td align="right" nowrap="nowrap">
									<xsl:if test="not($mode = 'READONLY')">
								       <xsl:if test="not(@status = $deleted) ">
											<xsl:call-template name="wb_gen_button">
												<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_edit"/>
												<xsl:with-param name="wb_gen_btn_href">javascript:obj.upd_obj('<xsl:value-of select="$course_id"/>',<xsl:value-of select="$module_id"/>,<xsl:value-of select="@id"/>,<xsl:value-of select="@msp_id"/>, '<xsl:value-of select="$mod_type"/>')</xsl:with-param>
											</xsl:call-template>
										</xsl:if>
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
											<xsl:call-template name="wb_gen_button">
												<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_remove"/>
												<xsl:with-param name="wb_gen_btn_href">javascript:wb_tst.del_obj('<xsl:value-of select="@msp_id"/>',<xsl:value-of select="$module_id"/>,'<xsl:value-of select="$timestamp"/>','<xsl:value-of select="$status"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
											</xsl:call-template>
									</xsl:if>											
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								<td width="8">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								</tr>
							</xsl:for-each>
							<!-- for DAS  -->
							<xsl:for-each select="../criterion">
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
								<td>
								<span class="Text">
									<xsl:value-of select="objective/desc/."/>
										<xsl:if test="@status = $deleted"> (<xsl:value-of select="$lab_obj_status" />)</xsl:if>
									</span>
								</td>
								<td align="center">
									<span class="Text">
										<xsl:value-of select="@q_count"/>
									</span>
								</td>						
								<td align="center">
									<span class="Text">
											<xsl:choose>
												<xsl:when test="@type = ''">
													<xsl:value-of select="$lab_all"/>
												</xsl:when>
												<xsl:otherwise>
														<xsl:choose>
															<xsl:when test="@type = 'MC'"><xsl:value-of select="$lab_mc"/></xsl:when>
															<xsl:when test="@type = 'FB'"><xsl:value-of select="$lab_fb"/></xsl:when>
															<xsl:when test="@type = 'TF'"><xsl:value-of select="$lab_tf"/></xsl:when>
															<xsl:when test="@type = 'MT'"><xsl:value-of select="$lab_mt"/></xsl:when>
															<xsl:when test="@type = 'ES'"><xsl:value-of select="$lab_es"/></xsl:when>
															<xsl:when test="@type = 'FSC'"><xsl:value-of select="$lab_fixed_sc"/></xsl:when>
															<xsl:when test="@type = 'DSC'"><xsl:value-of select="$lab_dna_sc"/></xsl:when>
														</xsl:choose>
												</xsl:otherwise>
											</xsl:choose>
									</span>
								</td>
								<td align="center">
									<span class="Text">
											<xsl:choose>
												<xsl:when test="@difficulty  = '1' ">
													<xsl:value-of select="$lab_easy"/>
												</xsl:when>
												<xsl:when test="@difficulty  = '2' ">
													<xsl:value-of select="$lab_normal"/>
												</xsl:when>
												<xsl:when test="@difficulty  = '3' ">
													<xsl:value-of select="$lab_hard"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$lab_all"/>
												</xsl:otherwise>
											</xsl:choose>	
									</span>
								</td>								
								<td align="center">
									<span class="Text">
										<xsl:value-of select="@score"/>
									</span>
								</td>									
								<td align="center">
									<span class="Text">
										<xsl:value-of select="@total_score"/>
									</span>
								</td>						
								<td align="right" nowrap="nowrap">
									<xsl:if test="not($mode = 'READONLY')">
											<xsl:call-template name="wb_gen_button">
												<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_edit"/>
												<xsl:with-param name="wb_gen_btn_href">javascript:obj.upd_obj('<xsl:value-of select="$course_id"/>',<xsl:value-of select="$module_id"/>,'<xsl:value-of select="objective/@id"/>','<xsl:value-of select="@id"/>', '<xsl:value-of select="$mod_type"/>')</xsl:with-param>
											</xsl:call-template>
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
											<xsl:call-template name="wb_gen_button">
												<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_remove"/>
												<xsl:with-param name="wb_gen_btn_href">javascript:asm.del_criteria(<xsl:value-of select="../@id"/>,<xsl:value-of select="@id"/>, '<xsl:value-of select="$mod_type"/>')</xsl:with-param>
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
					</xsl:when>
					<xsl:otherwise>
						<!--=== Draw Objective List ===-->
						<table cellspacing="0" cellpadding="3" border="0" width="800px" class="Bg">
							<tr class="SecBg">
								<td width="8">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								<td>
									<span class="TitleText" style="color: #999;">
										<xsl:value-of select="$lab_category_name"/>
									</span>
								</td>
								<td align="center">
									<span class="TitleText" style="color: #999;">
										<xsl:value-of select="$lab_no_of_que"/>
									</span>
								</td>
								<td align="center">
									<span class="TitleText" style="color: #999;">
										<xsl:value-of select="$lab_total_score"/>
									</span>
								</td>
								<td>
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								<td width="8">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
							</tr>
							<tr><td colspan="9"><xsl:call-template name="wb_ui_line"/></td></tr>
							<xsl:for-each select="objective | ../objective">
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
								<td width="46%">
									<span class="Text">
									<xsl:variable name="id" select="@id"/>
										<xsl:if test="../objective_path[@id=$id]/objective">
										<xsl:variable name="objective_cnt" select="count(../objective_path[@id=$id]/objective)"/>
										<xsl:for-each select="../objective_path[@id=$id]/objective">
											<xsl:value-of select="./text()"/>
											<xsl:if test="$objective_cnt>1 and not(position()=last())">
												<xsl:text> > </xsl:text>
											</xsl:if>
										</xsl:for-each>
										</xsl:if>
									<!--<xsl:value-of select="."/> -->
									<xsl:if test="@status = $deleted"> (<xsl:value-of select="$lab_obj_status" />)</xsl:if>
									
									</span>
								</td>
								<td align="center">
									<span class="Text">
										<xsl:value-of select="@q_count"/>
									</span>
								</td>
								<td align="center">
									<span class="Text">
										<xsl:value-of select="@q_score"/>
									</span>
								</td>
								<td align="right" width="30%">
								<xsl:if test="not($mode = 'READONLY')">
									<xsl:if test="@status = $ok">

											<xsl:call-template name="wb_gen_button">
												<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_add_que"/>
												<xsl:with-param name="wb_gen_btn_href">javascript:obj.add_obj('<xsl:value-of select="$course_id"/>','<xsl:value-of select="$module_id"/>','<xsl:value-of select="@id"/>','<xsl:value-of select="$status"/>')</xsl:with-param>
											</xsl:call-template>
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</xsl:if>
											<xsl:call-template name="wb_gen_button">
												<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_remove_que"/>
												<xsl:with-param name="wb_gen_btn_href">javascript:wb_tst.del_obj('<xsl:value-of select="@id"/>','<xsl:value-of select="$module_id"/>','<xsl:value-of select="$timestamp"/>','<xsl:value-of select="$status"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
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
					</xsl:otherwise>
				</xsl:choose>
				<xsl:call-template name="wb_ui_line"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="cur_usr"/>
	<xsl:template match="acl"/>
</xsl:stylesheet>
