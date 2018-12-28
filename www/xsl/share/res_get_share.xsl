<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="../utils/wb_css.xsl"/>
	<xsl:import href="../cust/wb_hdr.xsl"/>
	<xsl:import href="../cust/wb_cust_const.xsl"/>
	<xsl:import href="../utils/wb_gen_button.xsl"/>
	<xsl:import href="../utils/trun_timestamp.xsl"/>
	<xsl:import href="../utils/wb_ui_hdr.xsl"/>
	<xsl:import href="../utils/wb_ui_line.xsl"/>
	<xsl:import href="../utils/wb_ui_title.xsl"/>
	<xsl:import href="../utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="../utils/wb_ui_head.xsl"/>
	<xsl:import href="../utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="res_label_share.xsl"/>
	<xsl:variable name="is_evn_que">
		<xsl:choose>
			<xsl:when test="/question/@mod_type = 'EVN' or /question/@mod_type = 'SVY'">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="deleted">DELETED</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template name="html_header">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script type="text/javascript" src="{$wb_js_path}wb_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}gen_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_objective.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}urlparam.js" language="JavaScript"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_scenario.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<xsl:choose>
				<xsl:when test="header/@type='GEN'">
					<script src="{$wb_js_path}wb_resource.js" language="JavaScript"/>
					<script language="JavaScript"><![CDATA[		
					res = new wbResource
					obj = new wbObjective
					sc = new wbScControl
				]]></script>
				</xsl:when>
				<xsl:otherwise>
					<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_question.js"/>
					<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_aicc.js"/>
					<script language="JavaScript" type="text/javascript" src="{$wb_media_path}app/wb_applet_skin.js"/>
					<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_resource.js"/>
					<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_scenario.js"/>
					<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
					<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_cos_wizard.js"/>
					<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
					<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_assessment.js"/>
					<script language="JavaScript"><![CDATA[
					asm = new wbAssessment
					que = new wbQuestion
					res = new wbResource
					obj = new wbObjective
					aicc = new wbAicc
					sc = new wbScControl
					module_lst = new wbModule
					wiz = new wbCosWizard
					cos = new wbCourse
				]]></script>
				</xsl:otherwise>
			</xsl:choose>
		</head>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="draw_header">
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:call-template name="dh_lang_ch"/>
			</xsl:when>
			<xsl:when test="$wb_lang ='gb'">
				<xsl:call-template name="dh_lang_gb"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="dh_lang_en"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="dh_lang_ch">
		<xsl:call-template name="draw_header_content">
			<xsl:with-param name="lab_res_manager">教材管理</xsl:with-param>
			<xsl:with-param name="res_search_result">搜尋結果</xsl:with-param>
			<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
			<xsl:with-param name="page_title"></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="dh_lang_gb">
		<xsl:call-template name="draw_header_content">
			<xsl:with-param name="lab_res_manager">教材管理</xsl:with-param>
			<xsl:with-param name="res_search_result">搜索结果</xsl:with-param>
			<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
			<xsl:with-param name="page_title"></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="dh_lang_en">
		<xsl:call-template name="draw_header_content">
			<xsl:with-param name="lab_res_manager">Knowledge manager</xsl:with-param>
			<xsl:with-param name="res_search_result">Search result</xsl:with-param>
			<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
			<xsl:with-param name="page_title"></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="draw_header_content">
		<xsl:param name="lab_res_manager"/>
		<xsl:param name="res_search_result"/>
		<xsl:param name="belong_module"/>
		<xsl:param name="parent_code"/>
		<xsl:param name="page_title"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="//question/header/title">
						<xsl:value-of select="//question/header/title"/>
					</xsl:when>
					<xsl:when test="//resource/body/title">
						<xsl:value-of select="//resource/body/title"/>
					</xsl:when>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
					<xsl:with-param name="parent_code" select="$parent_code"></xsl:with-param>
					<xsl:with-param name="page_title" select="$page_title"></xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<span class="navLink">
					<a href="javascript:window.location.href=wb_utils_get_cookie('search_result_url')" class="navLink">
						<xsl:value-of select="$res_search_result"/>
					</a>
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:choose>
						<xsl:when test="//question/header/title">
							<xsl:value-of select="//question/header/title"/>
						</xsl:when>
						<xsl:when test="//resource/body/title">
							<xsl:value-of select="//resource/body/title"/>
						</xsl:when>
					</xsl:choose>
				</span>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="additional_information">
		<xsl:param name="timestamp"/>
		<xsl:param name="id_que"/>
		<xsl:param name="width"/>
		<xsl:param name="preview_function"/>
		<xsl:param name="upd_function"/>
		<xsl:param name="del_function"/>
		<xsl:param name="cp_function"/>
		<xsl:param name="sc_add_que_function"/>
		<xsl:param name="header"/>
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:apply-templates select="header">
					<xsl:with-param name="mange_source">管理資源</xsl:with-param>
					<xsl:with-param name="res_id">資源編號</xsl:with-param>
					<xsl:with-param name="res_type_undefined">沒有定義</xsl:with-param>
					<xsl:with-param name="res_type">類型</xsl:with-param>
					<xsl:with-param name="res_personal">個人</xsl:with-param>
					<xsl:with-param name="res_public">共享</xsl:with-param>
					<xsl:with-param name="res_easy">容易</xsl:with-param>
					<xsl:with-param name="res_normal">一般</xsl:with-param>
					<xsl:with-param name="res_hard">困難</xsl:with-param>
					<xsl:with-param name="res_diff">難度</xsl:with-param>
					<xsl:with-param name="res_online">已發佈</xsl:with-param>
					<xsl:with-param name="res_offline">未發佈</xsl:with-param>
					<xsl:with-param name="res_status">狀態</xsl:with-param>
					<xsl:with-param name="res_minute">分鐘</xsl:with-param>
					<xsl:with-param name="res_minutes">分鐘</xsl:with-param>
					<xsl:with-param name="res_duration">限時</xsl:with-param>
					<xsl:with-param name="res_modified">修訂</xsl:with-param>
					<xsl:with-param name="res_modified_by">最後修改者</xsl:with-param>
					<xsl:with-param name="res_owner">建立者</xsl:with-param>
					<xsl:with-param name="res_objective">資源文件夹</xsl:with-param>
					<xsl:with-param name="res_mode">型式</xsl:with-param>
					<xsl:with-param name="res_mode_multi_and">多項選擇：全對才得分</xsl:with-param>
					<xsl:with-param name="res_mode_multi_or">多項選擇：答對一個即得分</xsl:with-param>
					<xsl:with-param name="res_mode_single_one">單項選擇：只有一個正確答案</xsl:with-param>
					<xsl:with-param name="res_mode_single_more">單項選擇：有多個正確答案</xsl:with-param>
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="preview_function" select="$preview_function"/>
					<xsl:with-param name="upd_function" select="$upd_function"/>
					<xsl:with-param name="del_function" select="$del_function"/>
					<xsl:with-param name="cp_function" select="$cp_function"/>
					<xsl:with-param name="sc_add_que_function" select="$sc_add_que_function"/>
					<xsl:with-param name="id_que" select="$id_que"/>
					<xsl:with-param name="timestamp" select="$timestamp"/>
					<xsl:with-param name="res_undefined">Undefined</xsl:with-param>
					<xsl:with-param name="res_shuffle">可更改答案次序</xsl:with-param>
					<xsl:with-param name="lab_true">是</xsl:with-param>
					<xsl:with-param name="lab_false">否</xsl:with-param>
					<xsl:with-param name="lab_g_txt_btn_preview">預覽</xsl:with-param>
					<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
					<xsl:with-param name="lab_g_txt_btn_remove">刪除</xsl:with-param>
					<xsl:with-param name="lab_g_txt_btn_cut">剪下</xsl:with-param>
					<xsl:with-param name="lab_g_txt_btn_copy">複製</xsl:with-param>
					<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
					<xsl:with-param name="lab_desc">簡介</xsl:with-param>
					<xsl:with-param name="lab_answer_with_file">允許學員提交檔案附件</xsl:with-param>
					<xsl:with-param name="res_deleted">!!!Deleted</xsl:with-param>
					<xsl:with-param name="lab_sc_add_que">新增題目</xsl:with-param>
					<xsl:with-param name="lab_sc_shuffle_ind">題目是否允許被打亂次序</xsl:with-param>
					<xsl:with-param name="lab_score">分數</xsl:with-param>
					<xsl:with-param name="header" select="$header"></xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="$wb_lang='gb'">
				<xsl:apply-templates select="header">
					<xsl:with-param name="mange_source">管理资源</xsl:with-param>
					<xsl:with-param name="res_id">资源编号</xsl:with-param>
					<xsl:with-param name="res_type_undefined">没有定义</xsl:with-param>
					<xsl:with-param name="res_type">类型</xsl:with-param>
					<xsl:with-param name="res_personal">个人</xsl:with-param>
					<xsl:with-param name="res_public">共享</xsl:with-param>
					<xsl:with-param name="res_easy">容易</xsl:with-param>
					<xsl:with-param name="res_normal">一般</xsl:with-param>
					<xsl:with-param name="res_hard">困难</xsl:with-param>
					<xsl:with-param name="res_diff">难度</xsl:with-param>
					<xsl:with-param name="res_online">已发布</xsl:with-param>
					<xsl:with-param name="res_offline">未发布</xsl:with-param>
					<xsl:with-param name="res_status">状态</xsl:with-param>
					<xsl:with-param name="res_minute">分钟</xsl:with-param>
					<xsl:with-param name="res_minutes">分钟</xsl:with-param>
					<xsl:with-param name="res_duration">时限</xsl:with-param>
					<xsl:with-param name="res_modified">修订</xsl:with-param>
					<xsl:with-param name="res_modified_by">最后修改者</xsl:with-param>
					<xsl:with-param name="res_owner">建立者</xsl:with-param>
					<xsl:with-param name="res_objective">资源文件夹</xsl:with-param>
					<xsl:with-param name="res_mode">模式</xsl:with-param>
					<xsl:with-param name="res_mode_multi_and">多项选择：全对才得分</xsl:with-param>
					<xsl:with-param name="res_mode_multi_or">多项选择：每答对一个即得分</xsl:with-param>
					<xsl:with-param name="res_mode_single_one">单项选择：只有一个正确答案</xsl:with-param>
					<xsl:with-param name="res_mode_single_more">单项选择：有多个正确答案</xsl:with-param>
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="preview_function" select="$preview_function"/>
					<xsl:with-param name="upd_function" select="$upd_function"/>
					<xsl:with-param name="del_function" select="$del_function"/>
					<xsl:with-param name="cp_function" select="$cp_function"/>
					<xsl:with-param name="sc_add_que_function" select="$sc_add_que_function"/>
					<xsl:with-param name="id_que" select="$id_que"/>
					<xsl:with-param name="timestamp" select="$timestamp"/>
					<xsl:with-param name="res_undefined">未定义</xsl:with-param>
					<xsl:with-param name="res_shuffle">可更改答案次序</xsl:with-param>
					<xsl:with-param name="lab_true">是</xsl:with-param>
					<xsl:with-param name="lab_false">否</xsl:with-param>
					<xsl:with-param name="lab_g_txt_btn_preview">预览</xsl:with-param>
					<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
					<xsl:with-param name="lab_g_txt_btn_remove">删除</xsl:with-param>
					<xsl:with-param name="lab_g_txt_btn_cut">剪切</xsl:with-param>
					<xsl:with-param name="lab_g_txt_btn_copy">复制</xsl:with-param>
					<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
					<xsl:with-param name="lab_desc">简介</xsl:with-param>
					<xsl:with-param name="lab_answer_with_file">允许学员提交文档附件</xsl:with-param>
					<xsl:with-param name="res_deleted">!!!Deleted</xsl:with-param>
					<xsl:with-param name="lab_sc_add_que">添加题目</xsl:with-param>
					<xsl:with-param name="lab_sc_shuffle_ind">题目是否允许被打乱次序</xsl:with-param>
					<xsl:with-param name="lab_score">分数</xsl:with-param>
					<xsl:with-param name="header" select="$header"></xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="header">
					<xsl:with-param name="mange_source">Management resources</xsl:with-param>
					<xsl:with-param name="res_id">Resource ID</xsl:with-param>
					<xsl:with-param name="res_type_undefined">Undefined</xsl:with-param>
					<xsl:with-param name="res_type">Type</xsl:with-param>
					<xsl:with-param name="res_personal">Personal</xsl:with-param>
					<xsl:with-param name="res_public">Public</xsl:with-param>
					<xsl:with-param name="res_easy">Easy</xsl:with-param>
					<xsl:with-param name="res_normal">Normal</xsl:with-param>
					<xsl:with-param name="res_hard">Hard</xsl:with-param>
					<xsl:with-param name="res_diff">Difficulty</xsl:with-param>
					<xsl:with-param name="res_online">Published</xsl:with-param>
					<xsl:with-param name="res_offline">Unpublished</xsl:with-param>
					<xsl:with-param name="res_status">Status</xsl:with-param>
					<xsl:with-param name="res_mode">Mode</xsl:with-param>
					<xsl:with-param name="res_mode_multi_and">Multiple choices for all correct answers</xsl:with-param>
					<xsl:with-param name="res_mode_multi_or">Multiple choices for each correct answer</xsl:with-param>
					<xsl:with-param name="res_mode_single_one">Single choice with one correct answer</xsl:with-param>
					<xsl:with-param name="res_mode_single_more">Single choice with more than one correct answer</xsl:with-param>
					<xsl:with-param name="res_minute">min</xsl:with-param>
					<xsl:with-param name="res_minutes">mins</xsl:with-param>
					<xsl:with-param name="res_duration">Duration</xsl:with-param>
					<xsl:with-param name="res_modified">Last modified</xsl:with-param>
					<xsl:with-param name="res_modified_by">Modified by</xsl:with-param>
					<xsl:with-param name="res_owner">Creator</xsl:with-param>
					<xsl:with-param name="res_objective">Folder</xsl:with-param>
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="preview_function" select="$preview_function"/>
					<xsl:with-param name="upd_function" select="$upd_function"/>
					<xsl:with-param name="del_function" select="$del_function"/>
					<xsl:with-param name="cp_function" select="$cp_function"/>
					<xsl:with-param name="sc_add_que_function" select="$sc_add_que_function"/>
					<xsl:with-param name="id_que" select="$id_que"/>
					<xsl:with-param name="timestamp" select="$timestamp"/>
					<xsl:with-param name="res_undefined">Undefined</xsl:with-param>
					<xsl:with-param name="res_shuffle">Answers can be shuffled</xsl:with-param>
					<xsl:with-param name="lab_true">Yes</xsl:with-param>
					<xsl:with-param name="lab_false">No</xsl:with-param>
					<xsl:with-param name="lab_g_txt_btn_preview">Preview</xsl:with-param>
					<xsl:with-param name="lab_g_txt_btn_edit">Edit</xsl:with-param>
					<xsl:with-param name="lab_g_txt_btn_remove">Remove</xsl:with-param>
					<xsl:with-param name="lab_g_txt_btn_cut">Cut</xsl:with-param>
					<xsl:with-param name="lab_g_txt_btn_copy">Copy</xsl:with-param>
					<xsl:with-param name="lab_lost_and_found">Deleted Resources</xsl:with-param>
					<xsl:with-param name="lab_desc">Description</xsl:with-param>
					<xsl:with-param name="lab_answer_with_file">Allow File as Answer</xsl:with-param>
					<xsl:with-param name="res_deleted">Deleted</xsl:with-param>
					<xsl:with-param name="lab_sc_add_que">Add Question</xsl:with-param>
					<xsl:with-param name="lab_score">Score</xsl:with-param>
					<xsl:with-param name="lab_sc_shuffle_ind">Question will be shuffled</xsl:with-param>
					<xsl:with-param name="header" select="$header"></xsl:with-param>
				</xsl:apply-templates>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="header">
		<xsl:param name="res_id"/>
		<xsl:param name="res_type_undefined"/>
		<xsl:param name="res_type"/>
		<xsl:param name="res_personal"/>
		<xsl:param name="res_public"/>
		<xsl:param name="res_easy"/>
		<xsl:param name="res_normal"/>
		<xsl:param name="res_hard"/>
		<xsl:param name="res_diff"/>
		<xsl:param name="res_online"/>
		<xsl:param name="res_offline"/>
		<xsl:param name="res_status"/>
		<xsl:param name="res_minute"/>
		<xsl:param name="res_minutes"/>
		<xsl:param name="res_duration"/>
		<xsl:param name="res_modified"/>
		<xsl:param name="res_modified_by"/>
		<xsl:param name="res_owner"/>
		<xsl:param name="res_objective"/>
		<xsl:param name="res_mode"/>
		<xsl:param name="res_mode_multi_and"/>
		<xsl:param name="res_mode_multi_or"/>
		<xsl:param name="res_mode_single_one"/>
		<xsl:param name="res_mode_single_more"/>
		<xsl:param name="width"/>
		<xsl:param name="preview_function"/>
		<xsl:param name="upd_function"/>
		<xsl:param name="del_function"/>
		<xsl:param name="cp_function"/>
		<xsl:param name="sc_add_que_function"/>
		<xsl:param name="id_que"/>
		<xsl:param name="timestamp"/>
		<xsl:param name="res_undefined"/>
		<xsl:param name="res_shuffle"/>
		<xsl:param name="lab_true"/>
		<xsl:param name="lab_false"/>
		<xsl:param name="lab_g_txt_btn_preview"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_g_txt_btn_cut"/>
		<xsl:param name="lab_g_txt_btn_copy"/>
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_answer_with_file"/>
		<xsl:param name="res_deleted" />
		<xsl:param name="lab_sc_add_que"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_sc_shuffle_ind"/>
		<xsl:param name="mange_source"/>
		<xsl:param name="header"></xsl:param>
		
		<xsl:variable name="syb_id" select="objective/syllabus/@id"/>
		<xsl:variable name="obj_id" select="objective/@id"/>
		<xsl:variable name="show_all">false</xsl:variable>
		<xsl:variable name="foder" select="objective"/>
		
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
		</xsl:call-template>
		
		<xsl:if test="$header != 'NO'">
			<xsl:call-template name="wb_ui_nav_link">
				<xsl:with-param name="text">
					<span class="NavLink">
						<xsl:for-each select="objective/path/node">
							<a href="javascript:obj.manage_obj_lst('','{@id}','','','false')" class="NavLink">
								<xsl:value-of select="."/>
							</a>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
						</xsl:for-each>
						<a href="javascript:obj.manage_obj_lst('','{objective/@id}','','','false')" class="NavLink">
							<xsl:value-of select="objective/desc"/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$mange_source"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:obj.show_obj_lst('','<xsl:value-of select="objective/@id"/>','','','false')</xsl:with-param>
									<xsl:with-param name="class">NavLink</xsl:with-param>
						</xsl:call-template>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:choose>
							<xsl:when test="title">
								<xsl:value-of select="title"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="../body/title"/>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</xsl:with-param>
			 </xsl:call-template>
		</xsl:if>
		
		
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="extra_td">
				<xsl:if test="$is_evn_que != 'false'">
					<td style="text-align:left;">
						<xsl:value-of select="title"></xsl:value-of>
					</td>
				</xsl:if>
				<td align="right" nowarp="nowarp" width="220px">
					<xsl:if test="$sc_add_que_function!=''">
						<xsl:if test="@type='FSC' or @type='DSC'">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_sc_add_que"/>
								<xsl:with-param name="wb_gen_btn_href" select="$sc_add_que_function"/>
							</xsl:call-template>
						</xsl:if>
					</xsl:if>
					<xsl:if test="$preview_function!=''">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_preview"/>
							<xsl:with-param name="wb_gen_btn_href" select="$preview_function"/>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="$upd_function!=''">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_edit"/>
							<xsl:with-param name="wb_gen_btn_href" select="$upd_function"/>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="$del_function!=''">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_remove"/>
							<xsl:with-param name="wb_gen_btn_href" select="$del_function"/>
						</xsl:call-template>
					</xsl:if>
				</td>
			</xsl:with-param>
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<xsl:variable name="col_width">
				<xsl:choose>
					<xsl:when test="$is_evn_que = 'true'">80%</xsl:when>
					<xsl:otherwise>30%</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="$is_evn_que = 'false'">
					<tr>
						<!-- ===========-->
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$res_id"/>：
						</td>
						<td width="30%" style="padding:6px 0 10px 10px; color:#333;" valign="top">
							<xsl:value-of select="../@id"/>
						</td>
						<!-- ============-->
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$res_owner"/>：
						</td>
						<td width="30%" style="padding:6px 0 10px 10px; color:#333;" valign="top">
							<xsl:value-of select="../creation/user/display_bil/text()"/>
						</td>
					</tr>
					<!-- ===========-->
					<tr>
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$res_type"/>：
						</td>
						<td width="{$col_width}" style="padding:6px 0 10px 10px; color:#333;">
							<xsl:choose>
								<xsl:when test="//interaction/@type='MC' or @type='MC'">
									<xsl:value-of select="$lab_mc"/>
								</xsl:when>
								<xsl:when test="//interaction/@type='FB'">
									<xsl:choose>
										<xsl:when test="$is_evn_que = 'true'">
											<xsl:value-of select="$lab_fb_evn"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$lab_fb"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:when>
								<xsl:when test="//interaction/@type='TF'">
									<xsl:value-of select="$lab_tf"/>
								</xsl:when>
								<xsl:when test="//interaction/@type='MT'">
									<xsl:value-of select="$lab_mt"/>
								</xsl:when>
								<xsl:when test="//interaction/@type='ES'">
									<xsl:value-of select="$lab_es"/>
								</xsl:when>																
								<xsl:when test="@subtype='WCT'">
									<xsl:value-of select="$lab_wct"/>
								</xsl:when>
								<xsl:when test="@subtype='FIG'">
									<xsl:value-of select="$lab_fig"/>
								</xsl:when>
								<xsl:when test="@subtype='SSC'">
									<xsl:value-of select="$lab_ssc"/>
								</xsl:when>
								<xsl:when test="@subtype='NAR'">
									<xsl:value-of select="$lab_nar"/>
								</xsl:when>
								<xsl:when test="@subtype='ADO'">
									<xsl:value-of select="$lab_ado"/>
								</xsl:when>
								<xsl:when test="@subtype='VDO'">
									<xsl:value-of select="$lab_vdo"/>
								</xsl:when>
								<xsl:when test="@type='FSC'">
									<xsl:value-of select="$lab_fixed_sc"/>
								</xsl:when>
								<xsl:when test="@type='DSC'">
									<xsl:value-of select="$lab_dna_sc"/>
								</xsl:when>
								<xsl:when test="@type='SCORM'">
									<xsl:value-of select="$lab_sco"/>
								</xsl:when>
								<xsl:when test="@type='NETGCOK'">
									<xsl:value-of select="$lab_netg_cok"/>
								</xsl:when>									
								<xsl:otherwise>
									<xsl:value-of select="$res_type_undefined"/>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<!-- ===========-->
						<xsl:choose>
						   <xsl:when test="@type = 'SCORM' or @type = 'AICC'">
						      <td class="wzb-form-label" valign="top">
							<xsl:value-of select="$res_objective"/>：
						</td>
						<td width="30%" style="padding:6px 0 10px 10px; color:#333;" valign="top">
							<xsl:choose>
								<xsl:when test="objective/text()">
									<xsl:choose>
										<xsl:when test="objective/text() = 'LOST&amp;FOUND'">
											<xsl:value-of select="$lab_lost_and_found"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="objective/desc"/>
											<xsl:if test="objective/@status = $deleted">
											  (<xsl:value-of select="$res_deleted" />)                                                    											   </xsl:if>
                               </xsl:otherwise>
									</xsl:choose>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_empty"/>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						   </xsl:when>
						   <xsl:otherwise>
						       <td class="wzb-form-label" valign="top">
								<xsl:value-of select="$res_diff"/>：
							</td>
							<td width="30%" style="padding:6px 0 10px 10px; color:#333;" valign="top">
								<xsl:choose>
									<xsl:when test="@difficulty='1'">
										<xsl:value-of select="$res_easy"/>
									</xsl:when>
									<xsl:when test="@difficulty='2'">
										<xsl:value-of select="$res_normal"/>
									</xsl:when>
									<xsl:when test="@difficulty='3'">
										<xsl:value-of select="$res_hard"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_empty"/>
									</xsl:otherwise>
								</xsl:choose>
							</td>
						   </xsl:otherwise>
						</xsl:choose>
					</tr>
					
					<xsl:if test="@type != 'SCORM' and @type != 'AICC'"> 
					<tr>
						<!--============-->
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$res_status"/>：
						</td>
						<td width="30%" style="padding:6px 0 10px 10px; color:#333;" valign="top">
							<xsl:choose>
								<xsl:when test="@status = 'ON'">
									<xsl:value-of select="$res_online"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$res_offline"/>
								</xsl:otherwise>
							</xsl:choose>
						</td> 
						
						<!-- ============-->
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$res_objective"/>：
						</td>
						<td width="30%" style="padding:6px 0 10px 10px; color:#333;" valign="top">
							<xsl:choose>
								<xsl:when test="objective/text()">
									<xsl:choose>
										<xsl:when test="objective/text() = 'LOST&amp;FOUND'">
											<xsl:value-of select="$lab_lost_and_found"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="objective/desc"/>
											<xsl:if test="objective/@status = $deleted">
											  (<xsl:value-of select="$res_deleted" />)                                                    											   </xsl:if>
                               </xsl:otherwise>
									</xsl:choose>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_empty"/>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
				    </xsl:if>
					<tr>
						<!-- ============-->
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$res_modified"/>：
						</td>
						<td width="{$col_width}" style="padding:6px 0 10px 10px; color:#333;" valign="top">
							<xsl:call-template name="trun_timestamp">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="$timestamp"/>
								</xsl:with-param>
							</xsl:call-template>
						</td>
						<!-- ============-->
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$res_modified_by"/>：
						</td>
						<td width="{$col_width}" style="padding:6px 0 10px 10px; color:#333;" valign="top">
							<xsl:value-of select="../last_update/user/display_bil/text()"/>
						</td>
					</tr>
					<tr>
						<xsl:choose>
							<xsl:when test="//interaction/@type='MC'">
								<td class="wzb-form-label" valign="top">
									<xsl:value-of select="$res_shuffle"/>：
								</td>
								<td width="30%" style="padding:6px 0 10px 10px; color:#333;" valign="top">
									<xsl:choose>
										<xsl:when test="/question/body/interaction/@shuffle = 'Y'">
											<xsl:value-of select="$lab_true"/>
										</xsl:when>
										<xsl:when test="/question/body/interaction/@shuffle = 'N'">
											<xsl:value-of select="$lab_false"/>
										</xsl:when>
									</xsl:choose>
								</td>
							</xsl:when>
							<xsl:when test="@type='FSC'">
								<td class="wzb-form-label" valign="top">
									<xsl:value-of select="$lab_sc_shuffle_ind"/>：
								</td>
								<td width="30%" style="padding:6px 0 10px 10px; color:#333;" valign="top">
									<xsl:choose>
										<xsl:when test="container_attribute/allow_shuffle_ind/text() = 1"><xsl:value-of select="$lab_true"/></xsl:when>
										<xsl:otherwise><xsl:value-of select="$lab_false"/></xsl:otherwise>
									</xsl:choose>
								</td>
							</xsl:when>
							<xsl:otherwise>
								<td>
								</td>
								<td>
								</td>
							</xsl:otherwise>
						</xsl:choose>
						<td>
						</td>
						<td>
						</td>
					</tr>
					<xsl:if test="//interaction/@type='MC'">
						<tr>
							<td class="wzb-form-label" valign="top">
								<xsl:value-of select="$res_mode"/>：
							</td>
							<td width="30%" style="padding:6px 0 10px 10px; color:#333;" valign="top" colspan="3">
								<xsl:choose>
									<xsl:when test="/question/outcome/@logic = 'AND'">
										<xsl:value-of select="$res_mode_multi_and"/>
									</xsl:when>
									<xsl:when test="/question/outcome/@logic = 'OR'">
										<xsl:value-of select="$res_mode_multi_or"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:choose>
											<xsl:when test="count(/question/outcome/feedback/@score) > 1">
												<xsl:value-of select="$res_mode_single_more"/>
											</xsl:when>
											<xsl:when test="count(/question/outcome/feedback/@score) = 1">
												<xsl:value-of select="$res_mode_single_one"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="$lab_empty"/>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:otherwise>
								</xsl:choose>
							</td>
						</tr>
					</xsl:if>
				
				</xsl:when>
				<xsl:otherwise>
				<!-- do Evalation case-->
					<tr>
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$lab_desc"/>：
						</td>
						<td width="{$col_width}" style="padding:6px 0 10px 10px; color:#333;" valign="top">
							<xsl:choose>
							   <xsl:when test="desc != ''">
							        <xsl:call-template name="unescape_html_linefeed">
										<xsl:with-param name="my_right_value" select="desc"/>								
									</xsl:call-template>
							   </xsl:when>
							   <xsl:otherwise>
							      --
							   </xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>						
					<tr>
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$res_type"/>：
						</td>
						<td width="{$col_width}" style="padding:6px 0 10px 10px; color:#333;">
							<xsl:choose>
								<xsl:when test="//interaction/@type='MC'">
									<xsl:value-of select="$lab_mc"/>
								</xsl:when>
								<xsl:when test="//interaction/@type='FB'">
									<xsl:choose>
										<xsl:when test="$is_evn_que = 'true'">
											<xsl:value-of select="$lab_fb_evn"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$lab_fb"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:when>
								<xsl:when test="//interaction/@type='TF'">
									<xsl:value-of select="$lab_tf"/>
								</xsl:when>
								<xsl:when test="//interaction/@type='MT'">
									<xsl:value-of select="$lab_mt"/>
								</xsl:when>
								<xsl:when test="@subtype='WCT'">
									<xsl:value-of select="$lab_wct"/>
								</xsl:when>
								<xsl:when test="@subtype='FIG'">
									<xsl:value-of select="$lab_fig"/>
								</xsl:when>
								<xsl:when test="@subtype='SSC'">
									<xsl:value-of select="$lab_ssc"/>
								</xsl:when>
								<xsl:when test="@subtype='NAR'">
									<xsl:value-of select="$lab_nar"/>
								</xsl:when>
								<xsl:when test="@subtype='ADO'">
									<xsl:value-of select="$lab_ado"/>
								</xsl:when>
								<xsl:when test="@subtype='VDO'">
									<xsl:value-of select="$lab_vdo"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$res_type_undefined"/>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$res_modified"/>：
						</td>
						<td width="{$col_width}" style="padding:6px 0 10px 10px; color:#333;">
							<xsl:call-template name="trun_timestamp">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="$timestamp"/>
								</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$res_owner"/>：
						</td>
						<td width="{$col_width}" style="padding:6px 0 10px 10px; color:#333;" valign="top">
							<xsl:value-of select="../creation/user/display_bil/text()"/>
						</td>
					</tr>			
				</xsl:otherwise>
			</xsl:choose>
			<tr>
				<td colspan="4" height="10">
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_line"/>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
