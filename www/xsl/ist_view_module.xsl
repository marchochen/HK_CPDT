<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/trun_timestamp.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/display_eff_date.xsl"/>
	<xsl:import href="utils/change_lowercase.xsl"/>
	<xsl:import href="share/wb_module_type_const.xsl"/>
	<xsl:import href="share/asm_export_body.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<!-- ========================================================= -->
	<xsl:variable name="cur_usr" select="/module/cur_usr/@id"/>
	<xsl:variable name="mod_id" select="/module/@id"/>
	<xsl:variable name="ent_id" select="/module/cur_usr/@ent_id"/>
	<xsl:variable name="subtype" select="/module/header/@subtype"/>
	<xsl:variable name="style" select="/module/header/@test_style"/>
	<xsl:variable name="cos_type" select="/module/itm_type/text()"/>
	<xsl:variable name="item_type_lowercase">
		<xsl:call-template name="change_lowercase">
			<xsl:with-param name="input_value" select="$subtype"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="cur_tpl" select="/module/header/template_list/@cur_tpl"/>
	<xsl:variable name="cur_stylesheet" select="/module/header/template_list/template[@name = $cur_tpl]/stylesheet"/>
	<xsl:variable name="stylesheet">
		<xsl:choose>
			<xsl:when test="$subtype = 'CHT' ">chat.xsl</xsl:when>
			<xsl:when test="$subtype = 'FOR' ">forum.xsl</xsl:when>
			<xsl:when test="$subtype = 'VCR' ">ist_vcr.xsl</xsl:when>
			<xsl:when test="$subtype = 'FAQ' ">ist_faq.xsl</xsl:when>
			<xsl:when test="$subtype = 'VOD' ">ist_vod.xsl</xsl:when>
			<xsl:when test="$subtype = 'REF' ">ist_get_reference_lst.xsl</xsl:when>
			<xsl:when test="($subtype = 'TST' or $subtype = 'DXT') and $style ='only'">tst_player1.xsl</xsl:when>
			<xsl:when test="($subtype = 'TST' or $subtype = 'DXT') and $style ='many'">tst_view_many.xsl</xsl:when>
			<xsl:when test="$cur_stylesheet = '' or not(/module/header/template_list) ">blank_template.xsl</xsl:when>
			<xsl:when test="$subtype = 'RDG' ">tpl_web_browser.xsl</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$cur_stylesheet"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="page_width">
		<xsl:choose>
			<xsl:when test="//module/header/template_list/template/type = 'TNA' or //module/header/template_list/template/type = 'EVN'">
				<xsl:value-of select="$wb_gen_table_width"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>100%</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<xsl:variable name="max_usr_attempt_num" select="/module/header/@max_usr_attempt"/>
	<xsl:variable name="isEnrollment_related">
		<xsl:choose>
			<xsl:when test="not (/module/enrollment_related)">0</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="/module/enrollment_related"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- ========================================================= -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="content"/>
		</html>
	</xsl:template>
	<!--==========================================================-->
	<xsl:template name="content">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_course.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_module.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_assignment.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_assessment.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_message.js"/>
			<script language="JavaScript" src="{$wb_js_path}sso_link.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
					var ass = new wbAssignment;
					var course_lst = new wbCourse;
					var module_lst = new wbModule;
					var asm = new wbAssessment;
					var msg = new wbMessage;
					var sso = new wbSSO;
					var run_itm_id = wb_utils_get_cookie('view_evn_itm_id');

					gen_set_cookie('url_success',self.location.href)
					wb_utils_set_cookie('mod_subtype',]]>'<xsl:value-of select="$subtype"/>'<![CDATA[)
	

					course_id = wb_utils_get_cookie('course_id')

	
					function sort_lst(element){
						self.location.href = setUrlParam('order', element, self.location.href);
					}
	
					function del_module() {					
						course_lst.del_module(]]><xsl:value-of select="$mod_id"/><![CDATA[,course_id,']]><xsl:value-of select="module/header/@status"/><![CDATA[',']]><xsl:value-of select="module/@timestamp"/><![CDATA[',']]><xsl:value-of select="module/header/@attempted"/><![CDATA[',']]><xsl:value-of select="$wb_lang"/><![CDATA[')
					}
					
					function set_del_module_var() {
						window.parent.set_del_module_var(]]><xsl:value-of select="$mod_id"/><![CDATA[,']]><xsl:value-of select="module/header/@status"/><![CDATA[',']]><xsl:value-of select="module/@timestamp"/><![CDATA[',']]><xsl:value-of select="module/header/@attempted"/><![CDATA[',']]><xsl:value-of select="$wb_lang"/><![CDATA[');
					}
				
				]]></SCRIPT>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="new_css"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="wb_utils_set_cookie('url_prev',self.location.href);gen_set_cookie('url_failure',self.location.href);set_del_module_var()">
			<form name="frmXml">
				<!-- for sso link get -->
				<input type="hidden" name="learner_module" value="{//learner_module/location/text()}"/>
				<input type="hidden" name="root" value="{//root/text()}"/>
				<input type="hidden" name="sso_link" value=""/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ========================================================= -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="module">
			<xsl:with-param name="lab_lang">語言</xsl:with-param>
			<xsl:with-param name="lab_tc">繁體中文</xsl:with-param>
			<xsl:with-param name="lab_sc">簡體中文</xsl:with-param>
			<xsl:with-param name="lab_eng">英文</xsl:with-param>
			<xsl:with-param name="lab_desc">簡介</xsl:with-param>
			<xsl:with-param name="lab_req_time">時長(分鐘)</xsl:with-param>
			<xsl:with-param name="lab_download">允許下載</xsl:with-param>
			<xsl:with-param name="lab_push_to_mobile">發佈到移動端</xsl:with-param>
			<xsl:with-param name="lab_prohibited">禁止</xsl:with-param>
			<xsl:with-param name="lab_allow">允許</xsl:with-param>
			<xsl:with-param name="lab_instruct">講師</xsl:with-param>
			<xsl:with-param name="lab_instructor">講者</xsl:with-param>
			<xsl:with-param name="lab_moderator">主持人</xsl:with-param>
			<xsl:with-param name="lab_organization">機構</xsl:with-param>
			<xsl:with-param name="lab_diff">難度</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_hard">困難</xsl:with-param>
			<xsl:with-param name="lab_duration">時長</xsl:with-param>
			<xsl:with-param name="lab_attempt_nbr">參加次數</xsl:with-param>
			<xsl:with-param name="lab_minute">分鐘</xsl:with-param>
			<xsl:with-param name="lab_minutes">分鐘</xsl:with-param>
			<xsl:with-param name="lab_time_limit">限時</xsl:with-param>
			<xsl:with-param name="lab_suggested_time">建議時間</xsl:with-param>
			<xsl:with-param name="lab_max_score">滿分</xsl:with-param>
			<xsl:with-param name="lab_pass_score">合格率</xsl:with-param>
			<xsl:with-param name="lab_passing_score">合格分數</xsl:with-param>
			<xsl:with-param name="lab_eff_start">開始日期</xsl:with-param>
			<xsl:with-param name="lab_eff_end">結束日期</xsl:with-param>
			<xsl:with-param name="lab_unlimited">不限</xsl:with-param>
			<xsl:with-param name="lab_pgr_start">開始時間</xsl:with-param>
			<xsl:with-param name="lab_pgr_complete">完成時間</xsl:with-param>
			<xsl:with-param name="lab_pgr_last_acc">上次訪問時間</xsl:with-param>
			<xsl:with-param name="lab_datetime">時間</xsl:with-param>
			<xsl:with-param name="lab_venue">地點</xsl:with-param>
			<xsl:with-param name="lab_course_list">課程目錄</xsl:with-param>
			<xsl:with-param name="lab_module_list">模塊清單</xsl:with-param>
			<xsl:with-param name="lab_course_name">課程名稱</xsl:with-param>
			<xsl:with-param name="lab_course_information">課程資料</xsl:with-param>
			<xsl:with-param name="lab_name">名稱</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_author">作者</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_online">已發佈</xsl:with-param>
			<xsl:with-param name="lab_offline">未發佈</xsl:with-param>
			<xsl:with-param name="lab_total">總計</xsl:with-param>
			<xsl:with-param name="lab_grading">評分制度</xsl:with-param>
			<xsl:with-param name="lab_grade">等級制</xsl:with-param>
			<xsl:with-param name="lab_score">分數制</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_year">年</xsl:with-param>
			<xsl:with-param name="lab_month">月</xsl:with-param>
			<xsl:with-param name="lab_day">日</xsl:with-param>
			<xsl:with-param name="lab_unlimited_end">結束日期不限</xsl:with-param>
			<xsl:with-param name="lab_eff_date">有效期</xsl:with-param>
			<xsl:with-param name="lab_due_date">作業提交截止時間</xsl:with-param>
			<xsl:with-param name="lab_due_date_non_obligatory">(非強制)</xsl:with-param>
			<xsl:with-param name="lab_due_date_num">天(從課程開始日期算起)</xsl:with-param>
			<xsl:with-param name="lab_rating_que">評分</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_preview">預覽</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_enter">進入</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_ref">修改參考</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_glo">修改辭彙表</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_viewsub">查看提交詳情</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_test_builder">製作試卷</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_eval_builder">製作問卷</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_notified_usr">已通知客戶</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_notify">通知</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_report">學習報告</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_download">下載</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num">允許參加的最大次數</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num_unlimited">無限制</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num_limited">限制到</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num_times">次</xsl:with-param>
			<xsl:with-param name="lab_content_duration">內容時長</xsl:with-param>
			<xsl:with-param name="lab_sub_after_passed_checked">學員通過測驗及合格後<b>不能</b>再參加。</xsl:with-param>
			<xsl:with-param name="lab_sub_after_passed_not_checked">學員通過測驗及合格後仍可繼續參加。</xsl:with-param>
			<xsl:with-param name="lab_show_answer_checked">每次參加測驗後都顯示試題和答案。</xsl:with-param>
			<xsl:with-param name="lab_show_answer_not_checked">每次參加測驗後<b>不顯示</b>試題和答案。</xsl:with-param>
			<xsl:with-param name="lab_sub_save_and_suspend_checked">允許學員保存答案並暫停測驗。</xsl:with-param>
			<xsl:with-param name="lab_sub_save_and_suspend_not_checked_0">不</xsl:with-param>
			<xsl:with-param name="lab_sub_save_and_suspend_not_checked">允許學員保存答案並暫停測驗。</xsl:with-param>
			<xsl:with-param name="lab_notes">注意</xsl:with-param>
			<xsl:with-param name="lab_notes_tst_1">測驗中要使用的試題應事先在<b>資源管理</b>中製作。試題資源製成後，可以通過 <b>製作試卷</b> 功能為試卷出題。</xsl:with-param>
			<xsl:with-param name="lab_notes_tst_2">要發佈靜態測驗，必須先為試卷出題，然後將測驗狀態指定為'在線'。</xsl:with-param>
			<xsl:with-param name="lab_notes_dxt_1">測驗中要使用的試題應事先在<b>資源管理</b>中製作。試題資源製成後，可以通過 <b>製作試卷</b> 功能為試卷指定抽題的條件。</xsl:with-param>
			<xsl:with-param name="lab_notes_dxt_2">要發佈動態測驗，必須先為試卷指定抽題條件，然後將測驗狀態指定為'在線'。</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_test_result">成績</xsl:with-param>
			<xsl:with-param name="lab_sso_link">複製單點登錄鏈結</xsl:with-param>
			<xsl:with-param name="lab_ass_due_date_unspecified">未指定</xsl:with-param>
			<xsl:with-param name="lab_mod_managed_checked">由管理員控制測驗的開始</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_start_tst">开始测验</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_stop_tst">结束测验</xsl:with-param>
			<xsl:with-param name="lab_mod_show_answer_after_passed_ind">每次參加測驗合格後向學員顯示試題和答案</xsl:with-param>
			<xsl:with-param name="lab_vod_img_link">視頻截圖</xsl:with-param>
			<xsl:with-param name="lab_vod_point">知識要點</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			
			<xsl:with-param name="lab_text_style_sco">移動端顯示方式</xsl:with-param>
			<xsl:with-param name="lab_text_style_only_sco">豎屏</xsl:with-param>
			<xsl:with-param name="lab_text_style_many_sco">橫屏</xsl:with-param>
			<xsl:with-param name="lab_text_style">試卷樣式</xsl:with-param>
			<xsl:with-param name="lab_text_style_only">一屏單題 </xsl:with-param>
			<xsl:with-param name="lab_text_style_many">一屏多題 </xsl:with-param>
			<xsl:with-param name="lab_sco_ver">Scorm版本</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="module">
			<xsl:with-param name="lab_lang">语言</xsl:with-param>
			<xsl:with-param name="lab_tc">繁体中文</xsl:with-param>
			<xsl:with-param name="lab_sc">简体中文</xsl:with-param>
			<xsl:with-param name="lab_eng">英文</xsl:with-param>
			<xsl:with-param name="lab_desc">简介</xsl:with-param>
			<xsl:with-param name="lab_req_time">时长(分钟)</xsl:with-param>
			<xsl:with-param name="lab_download">允许下载</xsl:with-param>
			<xsl:with-param name="lab_push_to_mobile">发布到移动端</xsl:with-param>
			<xsl:with-param name="lab_prohibited">禁止</xsl:with-param>
			<xsl:with-param name="lab_allow">允许</xsl:with-param>
			<xsl:with-param name="lab_instruct">讲师</xsl:with-param>
			<xsl:with-param name="lab_instructor">讲者</xsl:with-param>
			<xsl:with-param name="lab_moderator">主持人</xsl:with-param>
			<xsl:with-param name="lab_organization">机构</xsl:with-param>
			<xsl:with-param name="lab_diff">难度</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_hard">困难</xsl:with-param>
			<xsl:with-param name="lab_duration">时长</xsl:with-param>
			<xsl:with-param name="lab_attempt_nbr">参加次数</xsl:with-param>
			<xsl:with-param name="lab_minute">分钟</xsl:with-param>
			<xsl:with-param name="lab_minutes">分钟</xsl:with-param>
			<xsl:with-param name="lab_time_limit">限时</xsl:with-param>
			<xsl:with-param name="lab_suggested_time">建议时间</xsl:with-param>
			<xsl:with-param name="lab_max_score">满分</xsl:with-param>
			<xsl:with-param name="lab_pass_score">合格率</xsl:with-param>
			<xsl:with-param name="lab_passing_score">合格分数</xsl:with-param>
			<xsl:with-param name="lab_eff_start">开始日期</xsl:with-param>
			<xsl:with-param name="lab_eff_end">结束日期</xsl:with-param>
			<xsl:with-param name="lab_unlimited">不限</xsl:with-param>
			<xsl:with-param name="lab_pgr_start">开始时间</xsl:with-param>
			<xsl:with-param name="lab_pgr_complete">完成时间</xsl:with-param>
			<xsl:with-param name="lab_pgr_last_acc">上次访问时间</xsl:with-param>
			<xsl:with-param name="lab_datetime">时间</xsl:with-param>
			<xsl:with-param name="lab_venue">地点</xsl:with-param>
			<xsl:with-param name="lab_course_list">课程目录</xsl:with-param>
			<xsl:with-param name="lab_module_list">模块清单</xsl:with-param>
			<xsl:with-param name="lab_course_name">课程名称</xsl:with-param>
			<xsl:with-param name="lab_course_information">课程资料</xsl:with-param>
			<xsl:with-param name="lab_name">名称</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_author">作者</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_online">已发布</xsl:with-param>
			<xsl:with-param name="lab_offline">未发布</xsl:with-param>
			<xsl:with-param name="lab_total">总计</xsl:with-param>
			<xsl:with-param name="lab_grading">评分方法</xsl:with-param>
			<xsl:with-param name="lab_grade">分级制</xsl:with-param>
			<xsl:with-param name="lab_score">打分制</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_year">年</xsl:with-param>
			<xsl:with-param name="lab_month">月</xsl:with-param>
			<xsl:with-param name="lab_day">日</xsl:with-param>
			<xsl:with-param name="lab_unlimited_end">结束日期不限</xsl:with-param>
			<xsl:with-param name="lab_eff_date">有效期</xsl:with-param>
			<xsl:with-param name="lab_due_date">作业提交截止时间</xsl:with-param>
			<xsl:with-param name="lab_due_date_non_obligatory">(非强制)</xsl:with-param>
			<xsl:with-param name="lab_due_date_num">天(从课程开始日期算起)</xsl:with-param>
			<xsl:with-param name="lab_rating_que">评分</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_preview">预览</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del">删除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_enter">进入</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_ref">修改参考</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_glo">修改词汇表</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_viewsub">查看提交情况</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_test_builder">制作试卷</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_eval_builder">制作问卷</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_notified_usr">已通知客户</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_notify">通知</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_report">学习报告</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_download">下载</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num">允许参加的最大次数</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num_unlimited">无限制</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num_limited">限制到</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num_times">次</xsl:with-param>
			<xsl:with-param name="lab_content_duration">内容时长</xsl:with-param>
			<xsl:with-param name="lab_sub_after_passed_checked">学员在成绩合格后<b>不能</b>再参加测验。</xsl:with-param>
			<xsl:with-param name="lab_sub_after_passed_not_checked">学员在成绩合格后仍可继续参加测验。</xsl:with-param>
			<xsl:with-param name="lab_show_answer_checked">每次参加测验后向学员显示试题和答案。</xsl:with-param>
			<xsl:with-param name="lab_show_answer_not_checked">每次参加测验后<b>不向</b>学员显示试题和答案。</xsl:with-param>
			<xsl:with-param name="lab_sub_save_and_suspend_checked">允许学员保存答案并暂停测验。</xsl:with-param>
			<xsl:with-param name="lab_sub_save_and_suspend_not_checked_0">不</xsl:with-param>
			<xsl:with-param name="lab_sub_save_and_suspend_not_checked">允许学员保存答案并暂停测验。</xsl:with-param>
			<xsl:with-param name="lab_notes">注意</xsl:with-param>
			<xsl:with-param name="lab_notes_tst_1">测验中要使用的试题应事先在<b>资源管理</b>中建立。试题资源添加成功后，可以通过 <b>制作试卷</b> 功能为试卷出题。</xsl:with-param>
			<xsl:with-param name="lab_notes_tst_2">要发布静态测验，必须先为试卷出题。</xsl:with-param>
			<xsl:with-param name="lab_notes_dxt_1">测验中要使用的试题应事先在<b>资源管理</b>中建立。试题资源添加成功后，可以通过 <b>制作试卷</b> 功能为试卷指定抽题的条件。</xsl:with-param>
			<xsl:with-param name="lab_notes_dxt_2">要发布动态测验，必须先为试卷指定抽题条件。</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_test_result">成绩</xsl:with-param>
			<xsl:with-param name="lab_sso_link">复制单点登录链接</xsl:with-param>
			<xsl:with-param name="lab_ass_due_date_unspecified">未指定</xsl:with-param>
			<xsl:with-param name="lab_mod_managed_checked">由管理员控制测验的开始</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_start_tst">开始测验</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_stop_tst">结束测验</xsl:with-param>
			<xsl:with-param name="lab_mod_show_answer_after_passed_ind">每次参加测验合格后向学员显示试题和答案</xsl:with-param>
			<xsl:with-param name="lab_vod_img_link">视频截图</xsl:with-param>
			<xsl:with-param name="lab_vod_point">知识要点</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_text_style_sco">移动端显示方式</xsl:with-param>
			<xsl:with-param name="lab_text_style_only_sco">竖屏</xsl:with-param>
			<xsl:with-param name="lab_text_style_many_sco">横屏</xsl:with-param>
			<xsl:with-param name="lab_text_style">试卷样式</xsl:with-param>
			<xsl:with-param name="lab_text_style_only">一屏单题样式</xsl:with-param>
			<xsl:with-param name="lab_text_style_many">一屏多题样式</xsl:with-param>
			<xsl:with-param name="lab_sco_ver">Scorm版本</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="module">
			<xsl:with-param name="lab_lang">Language</xsl:with-param>
			<xsl:with-param name="lab_tc">Traditional Chinese</xsl:with-param>
			<xsl:with-param name="lab_sc">Simplified Chinese</xsl:with-param>
			<xsl:with-param name="lab_eng">English</xsl:with-param>
			<xsl:with-param name="lab_desc">Description</xsl:with-param>
			<xsl:with-param name="lab_req_time">Duration(minutes)</xsl:with-param>
			<xsl:with-param name="lab_download">Allowed to download</xsl:with-param>
			<xsl:with-param name="lab_push_to_mobile">Publish to mobile</xsl:with-param>
			<xsl:with-param name="lab_prohibited">Prohibited</xsl:with-param>
			<xsl:with-param name="lab_allow">Allow</xsl:with-param>
			<xsl:with-param name="lab_instruct">Instruction</xsl:with-param>
			<xsl:with-param name="lab_instructor">Lecturer</xsl:with-param>
			<xsl:with-param name="lab_moderator">Moderator</xsl:with-param>
			<xsl:with-param name="lab_organization">Organization</xsl:with-param>
			<xsl:with-param name="lab_diff">Difficulty</xsl:with-param>
			<xsl:with-param name="lab_easy">Easy</xsl:with-param>
			<xsl:with-param name="lab_normal">Normal</xsl:with-param>
			<xsl:with-param name="lab_hard">Hard</xsl:with-param>
			<xsl:with-param name="lab_duration">Duration</xsl:with-param>
			<xsl:with-param name="lab_attempt_nbr">Attempted number</xsl:with-param>
			<xsl:with-param name="lab_minute">min</xsl:with-param>
			<xsl:with-param name="lab_minutes">mins</xsl:with-param>
			<xsl:with-param name="lab_time_limit">Time limit</xsl:with-param>
			<xsl:with-param name="lab_suggested_time">Suggested time</xsl:with-param>
			<xsl:with-param name="lab_max_score">Maximum score</xsl:with-param>
			<xsl:with-param name="lab_pass_score">Passing percentage</xsl:with-param>
			<xsl:with-param name="lab_passing_score">Passing score</xsl:with-param>
			<xsl:with-param name="lab_eff_start">Start date</xsl:with-param>
			<xsl:with-param name="lab_eff_end">End date</xsl:with-param>
			<xsl:with-param name="lab_unlimited">Unlimited</xsl:with-param>
			<xsl:with-param name="lab_pgr_start">Start time</xsl:with-param>
			<xsl:with-param name="lab_pgr_complete">Complete time</xsl:with-param>
			<xsl:with-param name="lab_pgr_last_acc">Last access</xsl:with-param>
			<xsl:with-param name="lab_datetime">Datetime</xsl:with-param>
			<xsl:with-param name="lab_venue">Venue</xsl:with-param>
			<xsl:with-param name="lab_course_list">Course list</xsl:with-param>
			<xsl:with-param name="lab_module_list">Module list</xsl:with-param>
			<xsl:with-param name="lab_course_name">Course name</xsl:with-param>
			<xsl:with-param name="lab_course_information">Course information</xsl:with-param>
			<xsl:with-param name="lab_name">Name</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_author">Author</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_online">Published</xsl:with-param>
			<xsl:with-param name="lab_offline">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_total">Total</xsl:with-param>
			<xsl:with-param name="lab_grading">Grading scheme</xsl:with-param>
			<xsl:with-param name="lab_grade">By letter grade</xsl:with-param>
			<xsl:with-param name="lab_score">By score</xsl:with-param>
			<xsl:with-param name="lab_to">to</xsl:with-param>
			<xsl:with-param name="lab_year"/>
			<xsl:with-param name="lab_month">-</xsl:with-param>
			<xsl:with-param name="lab_day">-</xsl:with-param>
			<xsl:with-param name="lab_unlimited_end">Unlimited end date</xsl:with-param>
			<xsl:with-param name="lab_eff_date">Available period</xsl:with-param>
			<xsl:with-param name="lab_due_date">Due date</xsl:with-param>
			<xsl:with-param name="lab_due_date_non_obligatory">(non-obligatory)</xsl:with-param>
			<xsl:with-param name="lab_due_date_num">days since the course started</xsl:with-param>
			<xsl:with-param name="lab_rating_que">Include rating question</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_preview">Preview</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del">Delete</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_enter">Enter</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_ref">Edit reference</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_glo">Edit glossary</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_viewsub">View submissions</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_test_builder">Test builder</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_eval_builder">Evaluation builder</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_notified_usr">Notified users</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_notify">Notify</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_report">Report</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_download">Download</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num">Attempt limit</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num_unlimited">Unlimited</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num_limited">Limited to</xsl:with-param>
			<xsl:with-param name="lab_max_attempt_num_times">attempt(s)</xsl:with-param>
			<xsl:with-param name="lab_content_duration">Content duration</xsl:with-param>
			<xsl:with-param name="lab_sub_after_passed_checked">Learners <b>cannot</b> take further attempts after passing the test.</xsl:with-param>
			<xsl:with-param name="lab_sub_after_passed_not_checked">Learners can take further attempts after passing the test.</xsl:with-param>
			<xsl:with-param name="lab_show_answer_checked">Questions and answers will be shown after each attempt.</xsl:with-param>
			<xsl:with-param name="lab_show_answer_not_checked">Questions and answers <b>will not</b> be shown after each attempt.</xsl:with-param>
			<xsl:with-param name="lab_sub_save_and_suspend_checked">Allow learner to save and suspend the test.</xsl:with-param>
			<xsl:with-param name="lab_sub_save_and_suspend_not_checked_0">Not</xsl:with-param>
			<xsl:with-param name="lab_sub_save_and_suspend_not_checked">&#160;allow learner to save and suspend the test.</xsl:with-param>
			<xsl:with-param name="lab_notes">Notes</xsl:with-param>
			<xsl:with-param name="lab_notes_tst_1">The questions used in the test should be created in <b>Learning Resource Management</b> first. When questions are available, use the <b>Test builder</b> function to specify the test questions.</xsl:with-param>
			<xsl:with-param name="lab_notes_tst_2">To turn a fixed test online, the test questions must have been specified.</xsl:with-param>
			<xsl:with-param name="lab_notes_dxt_1">The questions used in the test should be created in <b>Learning Resource Management</b> first. When questions are available, use the <b>Test builder</b> function to specify the question selection criteria for generating questions.</xsl:with-param>
			<xsl:with-param name="lab_notes_dxt_2">To turn a dynamic test online, the question selection criteria must have been specified.</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_test_result">Test result</xsl:with-param>
			<xsl:with-param name="lab_sso_link">Copy SSO link</xsl:with-param>
			<xsl:with-param name="lab_ass_due_date_unspecified">Unspecified</xsl:with-param>
			<xsl:with-param name="lab_mod_managed_checked">The test need to be started by administrator</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_start_tst">Start test</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_stop_tst">Stop test</xsl:with-param>
			<xsl:with-param name="lab_mod_show_answer_after_passed_ind">To the students passed the test each time you take the questions and answers</xsl:with-param>
			<xsl:with-param name="lab_vod_img_link">Icon</xsl:with-param>
			<xsl:with-param name="lab_vod_point">Knowledge points</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
			<xsl:with-param name="lab_text_style_sco">Mobile Display Method</xsl:with-param>
			<xsl:with-param name="lab_text_style_only_sco">Portrait</xsl:with-param>
			<xsl:with-param name="lab_text_style_many_sco">Landscape</xsl:with-param>
			<xsl:with-param name="lab_text_style">Test</xsl:with-param>
			<xsl:with-param name="lab_text_style_only">Show only one question on a screen </xsl:with-param>
			<xsl:with-param name="lab_text_style_many">Show a list of questions on a screen </xsl:with-param>
			<xsl:with-param name="lab_sco_ver">Scorm version</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="module">
		<xsl:param name="lab_sco_ver"/>
		<xsl:param name="lab_lang"/>
		<xsl:param name="lab_tc"/>
		<xsl:param name="lab_sc"/>
		<xsl:param name="lab_eng"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_req_time"/>
		<xsl:param name="lab_download"/>
		<xsl:param name="lab_push_to_mobile"/>
		<xsl:param name="lab_prohibited"/>
		<xsl:param name="lab_allow"/>
		<xsl:param name="lab_instruct"/>
		<xsl:param name="lab_instructor"/>
		<xsl:param name="lab_moderator"/>
		<xsl:param name="lab_organization"/>
		<xsl:param name="lab_diff"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_duration"/>
		<xsl:param name="lab_attempt_nbr"/>
		<xsl:param name="lab_minute"/>
		<xsl:param name="lab_minutes"/>
		<xsl:param name="lab_time_limit"/>
		<xsl:param name="lab_suggested_time"/>
		<xsl:param name="lab_max_score"/>
		<xsl:param name="lab_pass_score"/>
		<xsl:param name="lab_passing_score"/>
		<xsl:param name="lab_eff_start"/>
		<xsl:param name="lab_eff_end"/>
		<xsl:param name="lab_unlimited"/>
		<xsl:param name="lab_pgr_start"/>
		<xsl:param name="lab_pgr_complete"/>
		<xsl:param name="lab_pgr_last_acc"/>
		<xsl:param name="lab_datetime"/>
		<xsl:param name="lab_venue"/>
		<xsl:param name="lab_course_list"/>
		<xsl:param name="lab_module_list"/>
		<xsl:param name="lab_course_name"/>
		<xsl:param name="lab_course_information"/>
		<xsl:param name="lab_name"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_author"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_online"/>
		<xsl:param name="lab_offline"/>
		<xsl:param name="lab_total"/>
		<xsl:param name="lab_grading"/>
		<xsl:param name="lab_grade"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_year"/>
		<xsl:param name="lab_month"/>
		<xsl:param name="lab_day"/>
		<xsl:param name="lab_unlimited_end"/>
		<xsl:param name="lab_eff_date"/>
		<xsl:param name="lab_due_date"/>
		<xsl:param name="lab_due_date_non_obligatory"/>
		<xsl:param name="lab_due_date_num"/>
		<xsl:param name="lab_rating_que"/>
		<xsl:param name="lab_g_txt_btn_preview"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_del"/>
		<xsl:param name="lab_g_txt_btn_enter"/>
		<xsl:param name="lab_g_txt_btn_edit_ref"/>
		<xsl:param name="lab_g_txt_btn_edit_glo"/>
		<xsl:param name="lab_g_txt_btn_viewsub"/>
		<xsl:param name="lab_g_txt_btn_test_builder"/>
		<xsl:param name="lab_g_txt_btn_eval_builder"/>
		<xsl:param name="lab_g_txt_btn_notified_usr"/>
		<xsl:param name="lab_g_txt_btn_notify"/>
		<xsl:param name="lab_g_txt_btn_report"/>
		<xsl:param name="lab_g_txt_btn_download"/>
		<xsl:param name="lab_max_attempt_num"/>
		<xsl:param name="lab_max_attempt_num_unlimited"/>
		<xsl:param name="lab_max_attempt_num_limited"/>
		<xsl:param name="lab_max_attempt_num_times"/>
		<xsl:param name="lab_content_duration"/>
		<xsl:param name="lab_sub_after_passed_checked"/>
		<xsl:param name="lab_sub_after_passed_not_checked"/>
		<xsl:param name="lab_show_answer_checked"/>
		<xsl:param name="lab_show_answer_not_checked"/>
		<xsl:param name="lab_sub_save_and_suspend_checked"/>
		<xsl:param name="lab_sub_save_and_suspend_not_checked_0"/>
		<xsl:param name="lab_sub_save_and_suspend_not_checked"/>
		<xsl:param name="lab_notes"/>
		<xsl:param name="lab_notes_tst_1"/>
		<xsl:param name="lab_notes_tst_2"/>
		<xsl:param name="lab_notes_dxt_1"/>
		<xsl:param name="lab_notes_dxt_2"/>
		<xsl:param name="lab_g_txt_btn_test_result"/>
		<xsl:param name="lab_sso_link"/>
		<xsl:param name="lab_ass_due_date_unspecified"/>
		<xsl:param name="lab_mod_managed_checked"/>
		<xsl:param name="lab_g_txt_btn_start_tst"/>
		<xsl:param name="lab_g_txt_btn_stop_tst"/>
		<xsl:param name="lab_g_txt_btn_reload_tst"/>
		<xsl:param name="lab_mod_show_answer_after_passed_ind"/>
		<xsl:param name="lab_vod_img_link"/>
		<xsl:param name="lab_vod_point"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		
		<xsl:param name="lab_text_style_sco"/>
		<xsl:param name="lab_text_style_only_sco"/>
		<xsl:param name="lab_text_style_many_sco"/>
		<xsl:param name="lab_text_style"/>
		<xsl:param name="lab_text_style_only"/>
		<xsl:param name="lab_text_style_many"/>
		
		
	

		<table>
			<tr>
				<td height="35" width="35%">
					<xsl:choose>
						<xsl:when test="display/option/general/@icon = 'true'">
							<img src="{$wb_img_path}icol_{$item_type_lowercase}.gif" border="0" align="absmiddle"/>
						</xsl:when>
						<xsl:otherwise>&#160;</xsl:otherwise>
					</xsl:choose>
					<span class="TitleText">&#160;
						<xsl:choose>
							<xsl:when test="display/option/*/@title = 'true'">
								<xsl:value-of select="header/title"/>
							</xsl:when>
							<xsl:otherwise>&#160;</xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
				<td align="right" width="65%">
					<xsl:if test="/module/cur_usr/granted_functions/functions/function[@id='SSO_LINK_QUERY'] and /module/sso_link_query/links/learner_module[@active='true']">
						<xsl:choose>
							<xsl:when test="not(//enrollment_related)">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_sso_link"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:sso.get_mod_sso_link(<xsl:value-of select="$mod_id"/>, document.frmXml)</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:when test="//enrollment_related/text() = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_sso_link"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:sso.get_mod_sso_link(<xsl:value-of select="$mod_id"/>, document.frmXml)</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
						</xsl:choose>
					</xsl:if>
					<xsl:choose>
						<xsl:when test="$subtype = 'REF'">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_preview"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.preview_ref('<xsl:value-of select="$subtype"/>',<xsl:value-of select="$mod_id"/>,'ist_reference_preview.xsl',course_id)</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="$subtype = 'SCO'">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_preview"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.preview_scorm('<xsl:value-of select="header/@res_src_link"/>','<xsl:value-of select="$ent_id"/>',course_id,'<xsl:value-of select="$mod_id"/>','<xsl:value-of select="header/@mod_web_launch"/>','<xsl:value-of select="header/@mod_vendor"/>','','<xsl:value-of select="header/@res_sco_version"/>')</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="$subtype = 'NETG_COK'">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_preview"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.preview_netg(course_id, '<xsl:value-of select="$mod_id"/>','<xsl:value-of select="header/@res_src_link"/>')</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="not(($subtype = 'AICC_AU') or ($subtype = 'FAQ') or ($subtype = 'FOR') or ($subtype = 'CHT') or ($subtype = 'VST') or ($subtype = 'VCR')  or ($subtype = 'EXM') or ($subtype = 'ORI') or ($subtype = 'EAS') or ($subtype = 'MBL'))">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_preview"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.preview_exec('<xsl:value-of select="$subtype"/>',<xsl:value-of select="$mod_id"/>,'<xsl:value-of select="$stylesheet"/>',course_id)</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="$subtype = 'AICC_AU' ">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_preview"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.preview_aicc_au('<xsl:value-of select="header/@res_src_link"/>','','','','<xsl:value-of select="header/@mod_web_launch"/>')</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_edit"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.upd_prep(<xsl:value-of select="$mod_id"/>,course_id,'<xsl:value-of select="$subtype "/>','<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$isEnrollment_related"/>')</xsl:with-param>
					</xsl:call-template>
					<xsl:choose>
						<xsl:when test="($subtype = 'FAQ') or ($subtype = 'FOR') or ($subtype = 'CHT') or ($subtype = 'VST') or ($subtype = 'VCR')  or ($subtype = 'EXM') or ($subtype = 'ORI')">
							<xsl:if test="$isEnrollment_related != 'false'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_enter"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.preview_exec('<xsl:value-of select="$subtype"/>',<xsl:value-of select="$mod_id"/>,'<xsl:value-of select="$stylesheet"/>',course_id)</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
						</xsl:when>
					</xsl:choose>
					<xsl:choose>
						<xsl:when test="$subtype = 'REF' ">
							<xsl:if test="$isEnrollment_related != 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_edit_ref"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.edit_ref_exec(<xsl:value-of select="$mod_id"/>,'<xsl:value-of select="$stylesheet"/>')</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
						</xsl:when>
						<xsl:when test="$subtype = 'GLO'">
							<xsl:if test="$isEnrollment_related != 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_edit_glo"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.get_glo_keys(<xsl:value-of select="$mod_id"/>,null,null,'glo_edit.xsl')</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
						</xsl:when>
					</xsl:choose>
					<!-- 已加入到学习结果测评报告里面
					<xsl:if test="$subtype = 'ASS'">
						<xsl:if test="$isEnrollment_related != 'false'">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_viewsub"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:ass.view_submission(course_id,<xsl:value-of select="$mod_id"/>,'all',1,'true')</xsl:with-param>
							</xsl:call-template>
						</xsl:if>
					</xsl:if>
					 -->
					<xsl:if test="$subtype = 'EAS'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_viewsub"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.view_mod_eas_status_list(<xsl:value-of select="$mod_id"/>, '')</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="($subtype = 'TST') or ($subtype = 'EXC') or ($subtype = 'DXT') or ($subtype = 'STX')">
						<xsl:if test="(not((header/@is_public = 'true' and header/@public_used = 'true') or header/@mod_id_root > 0 or survey_template))">
							<xsl:if test="$isEnrollment_related != 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_test_builder"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:wb_utils_set_cookie('res_subtype','<xsl:value-of select="$subtype"/>');module_lst.upd_tst_prep(<xsl:value-of select="$mod_id"/>,course_id,'<xsl:value-of select="$cur_usr"/>','<xsl:value-of select="@status"/>','<xsl:value-of select="@attempted"/>','<xsl:value-of select="$subtype"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
						</xsl:if>
						<!-- test result, need acl -->
						<xsl:if test="($subtype = 'TST') or ($subtype = 'DXT')">
							<!-- 已加入到学习结果测评报告里面
							<xsl:if test="$isEnrollment_related != 'false'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_test_result"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.view_submission_lst(course_id,<xsl:value-of select="$mod_id"/>,'all');</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							 -->
							<xsl:if test="($isEnrollment_related = 'true' or $isEnrollment_related='0') and header/@managed_ind = '1'">
								<xsl:choose>
									<xsl:when test="header/@started_ind = '0' ">
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_start_tst"/>
											<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.set_tst_status(<xsl:value-of select="$mod_id"/>,1);</xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:when test="header/@started_ind = '1' ">
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_stop_tst"/>
											<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.set_tst_status(<xsl:value-of select="$mod_id"/>,0);</xsl:with-param>
										</xsl:call-template>
									</xsl:when>
								</xsl:choose>
							</xsl:if>
						</xsl:if>
					</xsl:if>
					<xsl:if test="$subtype = 'SVY'">
						<xsl:if test="(not((header/@is_public = 'true' and header/@public_used = 'true') or header/@mod_id_root > 0 or survey_template))">
							<xsl:if test="$isEnrollment_related!= 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_eval_builder"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.upd_svy_prep(<xsl:value-of select="$mod_id"/>,course_id,'<xsl:value-of select="$cur_usr"/>','<xsl:value-of select="@status"/>','<xsl:value-of select="@attempted"/>','<xsl:value-of select="$subtype"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
						</xsl:if>
					</xsl:if>
					<xsl:if test="($subtype = 'TST') or ($subtype = 'DXT')  or ($subtype = 'STX')">
						<xsl:variable name="mod_name">
							<xsl:call-template name="escape_js">
								<xsl:with-param name="input_str">
									<xsl:value-of select="header/title"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:variable>
					</xsl:if>
					<!-- 已加入到学习结果测评报告里面
					<xsl:if test="$subtype = 'SVY'">
						<xsl:if test="$isEnrollment_related != 'false'">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_viewsub"/>
								<xsl:with-param name="wb_gen_btn_href">Javascript:module_lst.get_subn_list('<xsl:value-of select="$mod_id"/>')</xsl:with-param>
							</xsl:call-template>
						</xsl:if>
					</xsl:if> 
					-->
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_line"/>
		<xsl:apply-templates select="header">
			<xsl:with-param name="lab_lang" select="$lab_lang"/>
			<xsl:with-param name="lab_tc" select="$lab_tc"/>
			<xsl:with-param name="lab_sc" select="$lab_sc"/>
			<xsl:with-param name="lab_eng" select="$lab_eng"/>
			<xsl:with-param name="lab_desc" select="$lab_desc"/>
			<xsl:with-param name="lab_req_time" select="$lab_req_time"/>
			<xsl:with-param name="lab_download" select="$lab_download"/>
			<xsl:with-param name="lab_push_to_mobile" select="$lab_push_to_mobile"/>
			<xsl:with-param name="lab_text_style_sco" select="$lab_text_style_sco"/>
			<xsl:with-param name="lab_text_style_only_sco" select="$lab_text_style_only_sco"/>
			<xsl:with-param name="lab_text_style_many_sco" select="$lab_text_style_many_sco"/>
			<xsl:with-param name="lab_text_style" select="$lab_text_style"/>
			<xsl:with-param name="lab_text_style_only" select="$lab_text_style_only"/>
			<xsl:with-param name="lab_text_style_many" select="$lab_text_style_many"/>
			<xsl:with-param name="lab_prohibited" select="$lab_prohibited"/>
			<xsl:with-param name="lab_allow" select="$lab_allow"/>
			<xsl:with-param name="lab_instruct" select="$lab_instruct"/>
			<xsl:with-param name="lab_instructor" select="$lab_instructor"/>
			<xsl:with-param name="lab_moderator" select="$lab_moderator"/>
			<xsl:with-param name="lab_organization" select="$lab_organization"/>
			<xsl:with-param name="lab_diff" select="$lab_diff"/>
			<xsl:with-param name="lab_easy" select="$lab_easy"/>
			<xsl:with-param name="lab_normal" select="$lab_normal"/>
			<xsl:with-param name="lab_hard" select="$lab_hard"/>
			<xsl:with-param name="lab_duration" select="$lab_duration"/>
			<xsl:with-param name="lab_attempt_nbr" select="$lab_attempt_nbr"/>
			<xsl:with-param name="lab_minute" select="$lab_minute"/>
			<xsl:with-param name="lab_minutes" select="$lab_minutes"/>
			<xsl:with-param name="lab_time_limit" select="$lab_time_limit"/>
			<xsl:with-param name="lab_suggested_time" select="$lab_suggested_time"/>
			<xsl:with-param name="lab_max_score" select="$lab_max_score"/>
			<xsl:with-param name="lab_pass_score" select="$lab_pass_score"/>
			<xsl:with-param name="lab_passing_score" select="$lab_passing_score"/>
			<xsl:with-param name="lab_eff_start" select="$lab_eff_start"/>
			<xsl:with-param name="lab_eff_end" select="$lab_eff_end"/>
			<xsl:with-param name="lab_unlimited" select="$lab_unlimited"/>
			<xsl:with-param name="lab_pgr_start" select="$lab_pgr_start"/>
			<xsl:with-param name="lab_pgr_complete" select="$lab_pgr_complete"/>
			<xsl:with-param name="lab_pgr_last_acc" select="$lab_pgr_last_acc"/>
			<xsl:with-param name="lab_datetime" select="$lab_datetime"/>
			<xsl:with-param name="lab_venue" select="$lab_venue"/>
			<xsl:with-param name="lab_grading" select="$lab_grading"/>
			<xsl:with-param name="lab_grade" select="$lab_grade"/>
			<xsl:with-param name="lab_score" select="$lab_score"/>
			<xsl:with-param name="lab_status" select="$lab_status"/>
			<xsl:with-param name="lab_online" select="$lab_online"/>
			<xsl:with-param name="lab_offline" select="$lab_offline"/>
			<xsl:with-param name="lab_to" select="$lab_to"/>
			<xsl:with-param name="lab_year" select="$lab_year"/>
			<xsl:with-param name="lab_month" select="$lab_month"/>
			<xsl:with-param name="lab_day" select="$lab_day"/>
			<xsl:with-param name="lab_eff_date" select="$lab_eff_date"/>
			<xsl:with-param name="lab_unlimited_end" select="$lab_unlimited_end"/>
			<xsl:with-param name="lab_due_date" select="$lab_due_date"/>
			<xsl:with-param name="lab_due_date_non_obligatory" select="$lab_due_date_non_obligatory"/>
			<xsl:with-param name="lab_due_date_num" select="$lab_due_date_num"/>
			<xsl:with-param name="lab_rating_que" select="$lab_rating_que"/>
			<xsl:with-param name="lab_type" select="$lab_type"/>
			<xsl:with-param name="lab_g_txt_btn_preview" select="$lab_g_txt_btn_preview"/>
			<xsl:with-param name="lab_g_txt_btn_edit" select="$lab_g_txt_btn_edit"/>
			<xsl:with-param name="lab_g_txt_btn_del" select="$lab_g_txt_btn_del"/>
			<xsl:with-param name="lab_g_txt_btn_enter" select="$lab_g_txt_btn_enter"/>
			<xsl:with-param name="lab_g_txt_btn_edit_ref" select="$lab_g_txt_btn_edit_ref"/>
			<xsl:with-param name="lab_g_txt_btn_edit_glo" select="$lab_g_txt_btn_edit_glo"/>
			<xsl:with-param name="lab_g_txt_btn_viewsub" select="$lab_g_txt_btn_viewsub"/>
			<xsl:with-param name="lab_g_txt_btn_test_builder" select="$lab_g_txt_btn_test_builder"/>
			<xsl:with-param name="lab_g_txt_btn_notified_usr" select="$lab_g_txt_btn_notified_usr"/>
			<xsl:with-param name="lab_g_txt_btn_notify" select="$lab_g_txt_btn_notify"/>
			<xsl:with-param name="lab_g_txt_btn_report" select="$lab_g_txt_btn_report"/>
			<xsl:with-param name="lab_g_txt_btn_download" select="$lab_g_txt_btn_download"/>
			<xsl:with-param name="lab_max_attempt_num" select="$lab_max_attempt_num"/>
			<xsl:with-param name="lab_max_attempt_num_unlimited" select="$lab_max_attempt_num_unlimited"/>
			<xsl:with-param name="lab_max_attempt_num_limited" select="$lab_max_attempt_num_limited"/>
			<xsl:with-param name="lab_max_attempt_num_times" select="$lab_max_attempt_num_times"/>
			<xsl:with-param name="lab_content_duration" select="$lab_content_duration"/>
			<xsl:with-param name="lab_sub_after_passed_checked" select="$lab_sub_after_passed_checked"/>
			<xsl:with-param name="lab_sub_after_passed_not_checked" select="$lab_sub_after_passed_not_checked"/>
			<xsl:with-param name="lab_show_answer_checked" select="$lab_show_answer_checked"/>
			<xsl:with-param name="lab_show_answer_not_checked" select="$lab_show_answer_not_checked"/>
			<xsl:with-param name="lab_sub_save_and_suspend_checked" select="$lab_sub_save_and_suspend_checked"/>
			<xsl:with-param name="lab_sub_save_and_suspend_not_checked_0" select="$lab_sub_save_and_suspend_not_checked_0"/>
			<xsl:with-param name="lab_sub_save_and_suspend_not_checked" select="$lab_sub_save_and_suspend_not_checked"/>
			<xsl:with-param name="lab_ass_due_date_unspecified" select="$lab_ass_due_date_unspecified"/>
			<xsl:with-param name="lab_mod_managed_checked" select="$lab_mod_managed_checked"/>
			<xsl:with-param name="lab_mod_show_answer_after_passed_ind" select="$lab_mod_show_answer_after_passed_ind"/>
			<xsl:with-param name="lab_vod_img_link" select="$lab_vod_img_link"/>
			<xsl:with-param name="lab_vod_point" select="$lab_vod_point"/>
			<xsl:with-param name="lab_no" select="$lab_no"/>
			<xsl:with-param name="lab_yes" select="$lab_yes"/>
			<xsl:with-param name="lab_sco_ver" select="$lab_sco_ver"/>
			
			
			
			
			
		</xsl:apply-templates>
		<!-- <xsl:call-template name="wb_ui_line"/> -->
		<xsl:if test="$subtype = 'DXT' or $subtype = 'TST'">
			<xsl:call-template name="draw_export_body">
				<xsl:with-param name="width"><xsl:value-of select="$wb_gen_table_width"/></xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="$subtype = 'SVY'">
			<xsl:call-template name="draw_export_svy_body">
				<xsl:with-param name="width"><xsl:value-of select="$wb_gen_table_width"/></xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<xsl:choose>
			<xsl:when test="$subtype = 'TST'">
				<table>
					<tr>
						<td width="75%" valign="left" height="10">
							<img src="{$wb_img_path}tp.gif" border="0" width="1" height="1"/>
						</td>
					</tr>
					<tr>
						<td width="75%" valign="left" height="10" class="wzb-ui-module-text">
							<xsl:value-of select="$lab_notes"/>
							<xsl:text>：</xsl:text>
							<ul>
								<li>
									<xsl:copy-of select="$lab_notes_tst_1"/>
								</li>
								<li style="height:8px;">
									<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
								</li>
								<li>
									<xsl:copy-of select="$lab_notes_tst_2"/>
								</li>
							</ul>
						</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:when test="$subtype = 'DXT'">
				<table cellpadding="5" cellspacing="0" width="{$wb_gen_table_width}" border="0">
					<tr>
						<td width="75%" valign="left" height="10" class="wzb-ui-module-text">
							<span class="Text">
								<xsl:value-of select="$lab_notes"/>
								<xsl:text>：</xsl:text>
							</span>
							<span class="Text">
								<ul>
									<li>
										<xsl:copy-of select="$lab_notes_dxt_1"/>
									</li>
									<li style="height:8px;">
									  <img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
								    </li>
									<li>
										<xsl:copy-of select="$lab_notes_dxt_2"/>
									</li>
								</ul>
							</span>
						</td>
					</tr>
					<tr>
						<td width="75%" valign="left" height="10">
							<img src="{$wb_img_path}tp.gif" border="0" width="1" height="1"/>
						</td>
					</tr>
				</table>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="header">
		<xsl:param name="lab_lang"/>
		<xsl:param name="lab_tc"/>
		<xsl:param name="lab_sc"/>
		<xsl:param name="lab_eng"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_req_time"/>
		<xsl:param name="lab_download"/>
		<xsl:param name="lab_prohibited"/>
		<xsl:param name="lab_allow"/>
		<xsl:param name="lab_instruct"/>
		<xsl:param name="lab_instructor"/>
		<xsl:param name="lab_moderator"/>
		<xsl:param name="lab_organization"/>
		<xsl:param name="lab_diff"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_duration"/>
		<xsl:param name="lab_attempt_nbr"/>
		<xsl:param name="lab_minute"/>
		<xsl:param name="lab_minutes"/>
		<xsl:param name="lab_time_limit"/>
		<xsl:param name="lab_suggested_time"/>
		<xsl:param name="lab_max_score"/>
		<xsl:param name="lab_pass_score"/>
		<xsl:param name="lab_passing_score"/>
		<xsl:param name="lab_eff_start"/>
		<xsl:param name="lab_eff_end"/>
		<xsl:param name="lab_unlimited"/>
		<xsl:param name="lab_pgr_start"/>
		<xsl:param name="lab_pgr_complete"/>
		<xsl:param name="lab_pgr_last_acc"/>
		<xsl:param name="lab_datetime"/>
		<xsl:param name="lab_venue"/>
		<xsl:param name="lab_grading"/>
		<xsl:param name="lab_grade"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_online"/>
		<xsl:param name="lab_offline"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_year"/>
		<xsl:param name="lab_month"/>
		<xsl:param name="lab_day"/>
		<xsl:param name="lab_eff_date"/>
		<xsl:param name="lab_unlimited_end"/>
		<xsl:param name="lab_due_date"/>
		<xsl:param name="lab_due_date_non_obligatory"/>
		<xsl:param name="lab_due_date_num"/>
		<xsl:param name="lab_rating_que"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_g_txt_btn_preview"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_del"/>
		<xsl:param name="lab_g_txt_btn_enter"/>
		<xsl:param name="lab_g_txt_btn_edit_ref"/>
		<xsl:param name="lab_g_txt_btn_edit_glo"/>
		<xsl:param name="lab_g_txt_btn_viewsub"/>
		<xsl:param name="lab_g_txt_btn_test_builder"/>
		<xsl:param name="lab_g_txt_btn_notified_usr"/>
		<xsl:param name="lab_g_txt_btn_notify"/>
		<xsl:param name="lab_g_txt_btn_report"/>
		<xsl:param name="lab_g_txt_btn_download"/>
		<xsl:param name="lab_max_attempt_num"/>
		<xsl:param name="lab_max_attempt_num_unlimited"/>
		<xsl:param name="lab_max_attempt_num_limited"/>
		<xsl:param name="lab_max_attempt_num_times"/>
		<xsl:param name="lab_content_duration"/>
		<xsl:param name="lab_sub_after_passed_checked"/>
		<xsl:param name="lab_sub_after_passed_not_checked"/>
		<xsl:param name="lab_show_answer_checked"/>
		<xsl:param name="lab_show_answer_not_checked"/>		
		<xsl:param name="lab_sub_save_and_suspend_checked"/>
		<xsl:param name="lab_sub_save_and_suspend_not_checked_0"/>
		<xsl:param name="lab_sub_save_and_suspend_not_checked"/>
		<xsl:param name="lab_ass_due_date_unspecified"/>
		<xsl:param name="lab_mod_managed_checked"/>
		<xsl:param name="lab_mod_show_answer_after_passed_ind"/>
		<xsl:param name="lab_vod_img_link"/>
		<xsl:param name="lab_vod_point"/>
		<xsl:param name="lab_push_to_mobile"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>	 
        <xsl:param name="lab_text_style_sco"/>
		<xsl:param name="lab_text_style_only_sco"/>
		<xsl:param name="lab_text_style_many_sco"/>
		<xsl:param name="lab_text_style"/>
		<xsl:param name="lab_text_style_only"/>
		<xsl:param name="lab_text_style_many"/>
		<xsl:param name="lab_sco_ver"/>
		
		
		<table>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_type"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="return_module_label">
						<xsl:with-param name="mod_type">
							<xsl:value-of select="$subtype"/>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<xsl:if test="../display/option/general/@desc = 'true'">
				<tr>
					<td class="wzb-form-label" valign="top">
						<xsl:value-of select="$lab_desc"/>：
					</td>
					<td class="wzb-form-control">
						<p class="word-desc">
						<xsl:choose>
							<xsl:when test="desc = ''">--</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value" select="desc/text()"/>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
						</p>
					</td>
				</tr>
			</xsl:if>
			
<!-- 			<xsl:if test="$cos_type = 'SELFSTUDY' and ($subtype = 'VOD' or $subtype = 'TST' or $subtype = 'DXT' or $subtype= 'SVY')"> -->
				<xsl:if test="not($subtype = 'AICC_AU')">
				<tr>
					<td class="wzb-form-label" valign="top">
						<xsl:value-of select="$lab_push_to_mobile"/>：
					</td>
					<td class="wzb-form-control">
						<xsl:choose>
							<xsl:when test="../header/@mod_mobile_ind = 1">
								<xsl:value-of select="$lab_yes"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_no"/><br />
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
				</xsl:if>
				<xsl:if test="$subtype = 'SCO' or $subtype = 'AICC_AU'">
					<tr>
						<td width="25%" valign="top" align="right" class="wzb-form-label">
							<span class="TitleText">
								<xsl:value-of select="$lab_text_style_sco"/>：
							</span>
						</td>
						<td width="75%" align="left" class="wzb-form-control">
							<span class="Text">
								<xsl:choose>
									<xsl:when test="../header/@mod_test_style = 'many'">
										<xsl:value-of select="$lab_text_style_many_sco"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_text_style_only_sco"/><br />
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
					</tr>
				</xsl:if>
				<xsl:if test="$subtype = 'SCO'">
					<tr>
						<td width="25%" valign="top" align="right" class="wzb-form-label">
							<span class="TitleText">
								<xsl:value-of select="$lab_sco_ver"/>：
							</span>
						</td>
						<td width="75%" align="left" class="wzb-form-control">
							<span class="Text">
								<xsl:value-of select="../header/@res_sco_version"/>

							</span>
						</td>
					</tr>
				</xsl:if>
				<xsl:if test="$subtype = 'TST' or $subtype = 'DXT' ">
					<tr>
						<td class="wzb-form-label" valign="top">
							<span class="Text">
								<xsl:value-of select="$lab_text_style"/>：
							</span>
						</td>
						<td class="wzb-form-control">
							<span class="Text">
								<xsl:choose>
									<xsl:when test="../header/@mod_test_style = 'only'">
										<xsl:value-of select="$lab_text_style_only"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_text_style_many"/><br />
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
					</tr>
			    </xsl:if>
<!-- 			</xsl:if> -->
			
			<xsl:if test="($subtype = 'VOD' or $subtype = 'RDG' or $subtype = 'REF') and ../header/@mod_required_time > 0">
				<tr>
					<td class="wzb-form-label" valign="top">
						<xsl:value-of select="$lab_req_time"/>：
					</td>
					<td class="wzb-form-control">
						<xsl:value-of select="../header/@mod_required_time"/>
					</td>
				</tr>
			</xsl:if>
			<xsl:for-each select="../display/option/*/attribute::*[. = 'true' and (name() != 'icon') and (name() != 'title') and (name() !='desc') and (name() !='organization') and (name() !='eff_start') and (name() != 'eff_end')]">
				<tr>
					<xsl:apply-templates select=".">
						<xsl:with-param name="lab_lang" select="$lab_lang"/>
						<xsl:with-param name="lab_tc" select="$lab_tc"/>
						<xsl:with-param name="lab_sc" select="$lab_sc"/>
						<xsl:with-param name="lab_eng" select="$lab_eng"/>
						<xsl:with-param name="lab_desc" select="$lab_desc"/>
						<xsl:with-param name="lab_instruct" select="$lab_instruct"/>
						<xsl:with-param name="lab_instructor" select="$lab_instructor"/>
						<xsl:with-param name="lab_moderator" select="$lab_moderator"/>
						<xsl:with-param name="lab_organization" select="$lab_organization"/>
						<xsl:with-param name="lab_diff" select="$lab_diff"/>
						<xsl:with-param name="lab_easy" select="$lab_easy"/>
						<xsl:with-param name="lab_normal" select="$lab_normal"/>
						<xsl:with-param name="lab_hard" select="$lab_hard"/>
						<xsl:with-param name="lab_duration" select="$lab_duration"/>
						<xsl:with-param name="lab_attempt_nbr" select="$lab_attempt_nbr"/>
						<xsl:with-param name="lab_minute" select="$lab_minute"/>
						<xsl:with-param name="lab_minutes" select="$lab_minutes"/>
						<xsl:with-param name="lab_time_limit" select="$lab_time_limit"/>
						<xsl:with-param name="lab_suggested_time" select="$lab_suggested_time"/>
						<xsl:with-param name="lab_max_score" select="$lab_max_score"/>
						<xsl:with-param name="lab_pass_score" select="$lab_pass_score"/>
						<xsl:with-param name="lab_passing_score" select="$lab_passing_score"/>
						<xsl:with-param name="lab_eff_start" select="$lab_eff_start"/>
						<xsl:with-param name="lab_eff_end" select="$lab_eff_end"/>
						<xsl:with-param name="lab_unlimited" select="$lab_unlimited"/>
						<xsl:with-param name="lab_pgr_start" select="$lab_pgr_start"/>
						<xsl:with-param name="lab_pgr_complete" select="$lab_pgr_complete"/>
						<xsl:with-param name="lab_pgr_last_acc" select="$lab_pgr_last_acc"/>
						<xsl:with-param name="lab_datetime" select="$lab_datetime"/>
						<xsl:with-param name="lab_venue" select="$lab_venue"/>
						<xsl:with-param name="lab_status" select="$lab_status"/>
						<xsl:with-param name="lab_online" select="$lab_online"/>
						<xsl:with-param name="lab_offline" select="$lab_offline"/>
						<xsl:with-param name="lab_eff_end" select="$lab_eff_date"/>
						<xsl:with-param name="lab_to" select="$lab_to"/>
						<xsl:with-param name="lab_year" select="$lab_year"/>
						<xsl:with-param name="lab_month" select="$lab_month"/>
						<xsl:with-param name="lab_day" select="$lab_day"/>
						<xsl:with-param name="lab_unlimited_end" select="$lab_unlimited_end"/>
						<xsl:with-param name="lab_g_txt_btn_preview" select="$lab_g_txt_btn_preview"/>
						<xsl:with-param name="lab_g_txt_btn_edit" select="$lab_g_txt_btn_edit"/>
						<xsl:with-param name="lab_g_txt_btn_del" select="$lab_g_txt_btn_del"/>
						<xsl:with-param name="lab_g_txt_btn_enter" select="$lab_g_txt_btn_enter"/>
						<xsl:with-param name="lab_g_txt_btn_edit_ref" select="$lab_g_txt_btn_edit_ref"/>
						<xsl:with-param name="lab_g_txt_btn_edit_glo" select="$lab_g_txt_btn_edit_glo"/>
						<xsl:with-param name="lab_g_txt_btn_viewsub" select="$lab_g_txt_btn_viewsub"/>
						<xsl:with-param name="lab_g_txt_btn_test_builder" select="$lab_g_txt_btn_test_builder"/>
						<xsl:with-param name="lab_g_txt_btn_notified_usr" select="$lab_g_txt_btn_notified_usr"/>
						<xsl:with-param name="lab_g_txt_btn_notify" select="$lab_g_txt_btn_notify"/>
						<xsl:with-param name="lab_g_txt_btn_report" select="$lab_g_txt_btn_report"/>
						<xsl:with-param name="lab_g_txt_btn_download" select="$lab_g_txt_btn_download"/>
						<xsl:with-param name="lab_rating_que" select="$lab_rating_que"/>
						<xsl:with-param name="lab_max_attempt_num" select="$lab_max_attempt_num"/>
						<xsl:with-param name="lab_max_attempt_num_unlimited" select="$lab_max_attempt_num_unlimited"/>
						<xsl:with-param name="lab_max_attempt_num_limited" select="$lab_max_attempt_num_limited"/>
						<xsl:with-param name="lab_max_attempt_num_times" select="$lab_max_attempt_num_times"/>
						<xsl:with-param name="lab_content_duration" select="$lab_content_duration"/>
					</xsl:apply-templates>
				</tr>
			</xsl:for-each>
			<xsl:if test="../display/option/datetime/@eff_start = 'true' and $cos_type != 'AUDIOVIDEO'">
				<tr>
					<td class="wzb-form-label" valign="top">
						 <xsl:value-of select="$lab_eff_date"/>：
					</td>
					<td class="wzb-form-control">
						<xsl:call-template name="display_eff_date">
							<xsl:with-param name="dis_time">T</xsl:with-param>
							<xsl:with-param name="from_timestamp">
								<xsl:value-of select="@eff_start_datetime"/>
							</xsl:with-param>
							<xsl:with-param name="to_timestamp">
								<xsl:value-of select="@eff_end_datetime"/>
							</xsl:with-param>
							<xsl:with-param name="isEnrollment_related" select="$isEnrollment_related"></xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="$subtype = 'VOD' and $cos_type = 'MOBILE'">
				<tr>
					<td class="wzb-form-label" valign="top">
						<xsl:value-of select="$lab_download"/>：
					</td>
					<td class="wzb-form-control">
						<xsl:choose>
							<xsl:when test="../header/@mod_download_ind = 0">
								<xsl:value-of select="$lab_prohibited"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_allow"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="@subtype='EAS'">
				<tr>
					<td class="wzb-form-label">
						<xsl:choose>
							<xsl:when test="@max_score = -1 ">
								<xsl:value-of select="$lab_grading"/>：</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_max_score"/>：</xsl:otherwise>
						</xsl:choose>
					</td>
					<td class="wzb-form-control">
						<xsl:choose>
							<xsl:when test="@max_score = -1 ">
								<xsl:value-of select="$lab_grade"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="@max_score"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
				<xsl:if test="@max_score != -1">
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_pass_score"/>：
						</td>
						<td class="wzb-form-control">
							<xsl:value-of select="@pass_score"/>&#160;
						</td>
					</tr>
				</xsl:if>
			</xsl:if>
			<xsl:if test="@subtype='ASS'">
				<tr>
					<td class="wzb-form-label">
						<xsl:value-of select="$lab_due_date"/>：
					</td>
					<td class="wzb-form-control">
						<xsl:choose>
							<xsl:when test="$isEnrollment_related = 'false'">
								<xsl:text>--</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="@due_date_day &gt;= 1">
										<xsl:value-of select="@due_date_day"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$lab_due_date_num"/>
										<img src="{$wb_img_path}tp.gif" width="5" height="1" border="0"/>
										<!-- <xsl:value-of select="$lab_due_date_non_obligatory"/> -->
									</xsl:when>
									<xsl:when test="@due_datetime != ''">
										<xsl:call-template name="trun_timestamp">
											<xsl:with-param name="my_timestamp">
												<xsl:value-of select="@due_datetime"/>
											</xsl:with-param>
										</xsl:call-template>
										<img src="{$wb_img_path}tp.gif" width="5" height="1" border="0"/>
										<!-- <xsl:value-of select="$lab_due_date_non_obligatory"/> -->
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_ass_due_date_unspecified"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
				
				<tr>
					<td class="wzb-form-label">
						<xsl:value-of select="$lab_grading"/><xsl:text>：</xsl:text>
					</td>
					<td class="wzb-form-control">
						<xsl:choose>
							<xsl:when test="@max_score = -1 ">
								<xsl:value-of select="$lab_grade"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_score"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
				<xsl:if test="@max_score != -1">
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_max_score"/>：
						</td>
						<td class="wzb-form-control">
							<xsl:value-of select="@max_score"/>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_passing_score"/>：
						</td>
						<td class="wzb-form-control">
							<xsl:value-of select="@pass_score"/>&#160;
						</td>
					</tr>
				</xsl:if>
			</xsl:if>
			<!--							
			<xsl:if test="@subtype='SVY'">
				<tr>
					<td class="wzb-form-label"  valign="top">
						<span class="TitleText"><xsl:value-of select="$lab_rating_que"/><xsl:text>：&#160;</xsl:text></span>
					</td>
					<td width="75%" align="" valign="top">
						<span class="Text">
							<xsl:choose>
								<xsl:when test="@has_rate_q='true'">Yes</xsl:when>
								<xsl:when test="@has_rate_q='false'">No</xsl:when>
							</xsl:choose>
						</span>
					</td>
				</tr>
			</xsl:if>
			-->
			<xsl:apply-templates select="@sub_after_passed_ind">
				<xsl:with-param name="lab_checked" select="$lab_sub_after_passed_checked"/>
				<xsl:with-param name="lab_not_checked" select="$lab_sub_after_passed_not_checked"/>
			</xsl:apply-templates>
			<xsl:apply-templates select="@show_answer_ind">
				<xsl:with-param name="lab_checked" select="$lab_show_answer_checked"/>
				<xsl:with-param name="lab_not_checked" select="$lab_show_answer_not_checked"/>
				<xsl:with-param name="mod_show_answer_after_passed_ind" select="@mod_show_answer_after_passed_ind"/>
				<xsl:with-param name="lab_mod_show_answer_after_passed_ind" select="$lab_mod_show_answer_after_passed_ind"/>
			</xsl:apply-templates>
			<xsl:apply-templates select="@show_save_and_suspend_ind">
				<xsl:with-param name="lab_checked" select="$lab_sub_save_and_suspend_checked"/>
				<xsl:with-param name="lab_not_checked_0" select="$lab_sub_save_and_suspend_not_checked_0"/>
				<xsl:with-param name="lab_not_checked" select="$lab_sub_save_and_suspend_not_checked"/>
			</xsl:apply-templates>
			<!--由管理员控制开始-->
			<xsl:if test="@managed_ind = 1">
				<tr>
					<td class="wzb-form-label">
						&#160;
					</td>
					<td class="wzb-form-control">
						<xsl:value-of select="$lab_mod_managed_checked"/>
					</td>
				</tr>
			</xsl:if>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@venue">
		<xsl:param name="lab_venue"/>
		<td class="wzb-form-label">
			<xsl:value-of select="$lab_venue"/>：
		</td>
		<td class="wzb-form-control">
			<xsl:choose>
				<xsl:when test="../../../../header/event/venue = ''">&#160;</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="../../../../header/event/venue"/>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@datetime">
		<xsl:param name="lab_datetime"/>
		<td class="wzb-form-label">
			<xsl:value-of select="$lab_datetime"/>：
		</td>
		<td class="wzb-form-control">
			<xsl:choose>
				<xsl:when test="../../../../header/event/datetime = ''">&#160;</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="trun_timestamp">
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="../../../../header/event/datetime"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@pgr_last_acc">
		<xsl:param name="lab_pgr_last_acc"/>
		<xsl:param name="lab_unlimited"/>
		<td class="wzb-form-label">
			<xsl:value-of select="$lab_pgr_last_acc"/>：
		</td>
		<td class="wzb-form-control">
			<xsl:choose>
				<xsl:when test="../../../../header/@pgr_last_acc = 'UNLIMITED'">
					<xsl:value-of select="$lab_unlimited"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="trun_timestamp">
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="../../../../header/@pgr_last_acc"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@pgr_complete">
		<xsl:param name="lab_pgr_complete"/>
		<xsl:param name="lab_unlimited"/>
		<td class="wzb-form-label">
			<xsl:value-of select="$lab_pgr_complete"/>：
		</td>
		<td class="wzb-form-control">
			<xsl:choose>
				<xsl:when test="../../../../header/@pgr_complete = 'UNLIMITED'">
					<xsl:value-of select="$lab_unlimited"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="trun_timestamp">
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="../../../../header/@pgr_complete"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@status">
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_online"/>
		<xsl:param name="lab_offline"/>
		<td class="wzb-form-label">
			<xsl:value-of select="$lab_status"/>：
		</td>
		<td class="wzb-form-control">
			<xsl:choose>
				<xsl:when test="$isEnrollment_related = 'false'">
					<xsl:text>--</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="../../../../header/@status = ''">&#160;</xsl:when>
						<xsl:otherwise>
							<xsl:if test="../../../../header/@status = 'ON'">
								<xsl:value-of select="$lab_online"/>
							</xsl:if>
							<xsl:if test="../../../../header/@status = 'OFF'">
								<xsl:value-of select="$lab_offline"/>
							</xsl:if>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@pgr_start">
		<xsl:param name="lab_pgr_start"/>
		<xsl:param name="lab_unlimited"/>
		<td class="wzb-form-label">
			<xsl:value-of select="$lab_pgr_start"/>：
		</td>
		<td class="wzb-form-control">
			<xsl:choose>
				<xsl:when test="../../../../header/@pgr_start = 'UNLIMITED'">
					<xsl:value-of select="$lab_unlimited"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="trun_timestamp">
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="../../../../header/@pgr_start"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@eff_end">
		<xsl:param name="lab_eff_end"/>
		<xsl:param name="lab_unlimited"/>
		<td class="wzb-form-label">
			<xsl:value-of select="$lab_eff_end"/>：
		</td>
		<td class="wzb-form-control">
			<xsl:choose>
				<xsl:when test="../../../../header/@eff_end_datetime = 'UNLIMITED'">
					<xsl:value-of select="$lab_unlimited"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="trun_timestamp">
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="../../../../header/@eff_end_datetime"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@eff_start">
		<xsl:param name="lab_eff_start"/>
		<xsl:param name="lab_unlimited"/>
		<td class="wzb-form-label">
			<xsl:value-of select="$lab_eff_start"/>：
		</td>
		<td class="wzb-form-control">
			<xsl:choose>
				<xsl:when test="../../../../header/@eff_start_datetime = 'UNLIMITED'">
					<xsl:value-of select="$lab_unlimited"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="trun_timestamp">
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="../../../../header/@eff_start_datetime"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@pass_score">
		<xsl:param name="lab_passing_score"/>
		<xsl:param name="lab_pass_score"/>
		<xsl:choose>
			<xsl:when test="../../../../header/@pass_score &gt; 0">
				<td class="wzb-form-label">
					<xsl:choose>
						<xsl:when test="../../../../header/@subtype = 'AICC_AU' or ../../../../header/@subtype = 'SCO' or ../../../../header/@subtype = 'ASS'">
							<xsl:value-of select="$lab_passing_score"/>：</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_pass_score"/>：</xsl:otherwise>
					</xsl:choose>
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="../../../../header/@pass_score"/>&#160;
				</td>
			</xsl:when>
			<xsl:otherwise>
				<td colspan="2" height="0"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@max_score">
		<xsl:param name="lab_max_score"/>
		<xsl:choose>
			<xsl:when test="../../../../header/@max_score &gt; 0 ">
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_max_score"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="../../../../header/@max_score"/>&#160;
				</td>
			</xsl:when>
			<xsl:otherwise>
				<td colspan="2" height="0"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@suggested_time">
		<xsl:param name="lab_suggested_time"/>
		<xsl:param name="lab_minutes"/>
		<xsl:param name="lab_minute"/>
		<td class="wzb-form-label">
			<xsl:value-of select="$lab_suggested_time"/>：
		</td>
		<td class="wzb-form-control">
			<xsl:choose>
				<xsl:when test="../../../../header/@suggested_time &gt; 1">
					<xsl:value-of select="../../../../header/@suggested_time"/>&#160;<xsl:value-of select="$lab_minutes"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="../../../../header/@suggested_time"/>&#160;<xsl:value-of select="$lab_minute"/>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@time_limit">
		<xsl:param name="lab_time_limit"/>
		<xsl:param name="lab_minutes"/>
		<xsl:param name="lab_minute"/>
		<xsl:choose>
			<xsl:when test="../../../../header/@time_limit &gt; 0">
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_time_limit"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="../../../../header/@time_limit &gt; 1">
							<xsl:value-of select="../../../../header/@time_limit"/>&#160;<xsl:value-of select="$lab_minutes"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="../../../../header/@time_limit"/>&#160;<xsl:value-of select="$lab_minute"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:when>
			<xsl:otherwise>
				<td colspan="2" height="0"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@show_answer_ind">
		<xsl:param name="lab_checked"/>
		<xsl:param name="lab_not_checked"/>
		<xsl:param name="mod_show_answer_after_passed_ind"/>  
		<xsl:param name="lab_mod_show_answer_after_passed_ind"/> 
		<tr>
			<xsl:choose>
				<xsl:when test=". = '1'">
					<td class="wzb-form-label">
						&#160;
					</td>
					<td class="wzb-form-control wzb-ui-module-text">
						<xsl:value-of select="$lab_checked"/>
					</td>
				</xsl:when>
				<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="$mod_show_answer_after_passed_ind = 1"> 
						<td class="wzb-form-label">
						&#160;
						</td>
						<td class="wzb-form-control wzb-ui-module-text">
							<xsl:value-of select="$lab_mod_show_answer_after_passed_ind"/>
						</td>
						</xsl:when>
						<xsl:otherwise>
								<td class="wzb-form-label">
								&#160;
								</td>
								<td class="wzb-form-control wzb-ui-module-text">
									<xsl:copy-of select="$lab_not_checked"/>
								</td>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@sub_after_passed_ind">
		<xsl:param name="lab_checked"/>
		<xsl:param name="lab_not_checked"/>
		<tr>
			<xsl:choose>
				<xsl:when test=". = '1'">
					<td class="wzb-form-label">
						&#160;
					</td>
					<td class="wzb-form-control wzb-ui-module-text">
						<xsl:value-of select="$lab_not_checked"/>
					</td>
				</xsl:when>
				<xsl:otherwise>
					<td class="wzb-form-label">
						&#160;
					</td>
					<td class="wzb-form-control wzb-ui-module-text">
						<xsl:copy-of select="$lab_checked"/>
					</td>
				</xsl:otherwise>
			</xsl:choose>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@attempt_nbr">
		<xsl:param name="lab_attempt_nbr"/>
		<td class="wzb-form-label">
			<xsl:value-of select="$lab_attempt_nbr"/>：
		</td>
		<td class="wzb-form-control">
			<xsl:value-of select="../../../../header/@attempt_nbr"/>&#160;
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@duration">
		<xsl:param name="lab_content_duration"/>
		<xsl:param name="lab_duration"/>
		<xsl:param name="lab_minutes"/>
		<xsl:param name="lab_minute"/>
		<xsl:choose>
			<xsl:when test="../../../../header/@duration &gt; 0">
				<td class="wzb-form-label">
					<xsl:choose>
						<xsl:when test="../../../../header/@subtype = 'NETG_COK' or ../../../../header/@subtype = 'AICC_AU'">
							<xsl:value-of select="$lab_content_duration"/>：</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_duration"/>：</xsl:otherwise>
					</xsl:choose>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="../../../../header/@duration &gt; 1">
							<xsl:value-of select="../../../../header/@duration"/>&#160;<xsl:value-of select="$lab_minutes"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="../../../../header/@duration"/>&#160;<xsl:value-of select="$lab_minute"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:when>
			<xsl:otherwise>
				<td colspan="2" height="0"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@difficulty">
		<xsl:param name="lab_diff"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_hard"/>
		
		<td class="wzb-form-label">
			<xsl:value-of select="$lab_diff"/>：
		</td>
		<td class="wzb-form-control">
			<xsl:choose>
				<xsl:when test="../../../../header/@difficulty = 1">
					<xsl:value-of select="$lab_easy"/>
				</xsl:when>
				<xsl:when test="../../../../header/@difficulty = 2">
					<xsl:value-of select="$lab_normal"/>
				</xsl:when>
				<xsl:when test="../../../../header/@difficulty = 3">
					<xsl:value-of select="$lab_hard"/>
				</xsl:when>
			</xsl:choose>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@max_usr_attempt">
		<xsl:param name="lab_max_attempt_num"/>
		<xsl:param name="lab_max_attempt_num_unlimited"/>
		<xsl:param name="lab_max_attempt_num_limited"/>
		<xsl:param name="lab_max_attempt_num_times"/>
		<td class="wzb-form-label">
			<xsl:value-of select="$lab_max_attempt_num"/>：
		</td>
		<td class="wzb-form-control">
			<xsl:choose>
				<xsl:when test="$max_usr_attempt_num = '0' or $max_usr_attempt_num = ''">
					<xsl:value-of select="$lab_max_attempt_num_unlimited"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$lab_max_attempt_num_limited"/>
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$max_usr_attempt_num"/>
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$lab_max_attempt_num_times"/>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@instruct">
		<xsl:param name="lab_instruct"/>
		<td class="wzb-form-label">
			<xsl:value-of select="$lab_instruct"/>：
		</td>
		<td class="wzb-form-control">
			<xsl:value-of select="../../../../header/instruct"/>&#160;
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@instructor | @moderator">
		<xsl:param name="lab_instructor"/>
		<td class="wzb-form-label">
			<xsl:value-of select="$lab_instructor"/>：
		</td>
		<td class="wzb-form-control">
			<xsl:choose>
				<xsl:when test="../../../../header/instructor = ''">
					<xsl:value-of select="../../../../header/instructor_list/user[@selected = 'true']/@display"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="../../../../header/instructor"/>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@language">
		<xsl:param name="lab_lang"/>
		<xsl:param name="lab_tc"/>
		<xsl:param name="lab_eng"/>
		<xsl:param name="lab_sc"/>
		<td class="wzb-form-label">
			<xsl:value-of select="$lab_lang"/>：
		</td>
		<td class="wzb-form-control">
			<xsl:choose>
				<xsl:when test="/module/@language='Big5'">
					<xsl:value-of select="$lab_tc"/>
				</xsl:when>
				<xsl:when test="/module/@language='ISO-8859-1'">
					<xsl:value-of select="$lab_eng"/>
				</xsl:when>
				<xsl:when test="/module/@language='GB2312'">
					<xsl:value-of select="$lab_sc"/>
				</xsl:when>
			</xsl:choose>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="@show_save_and_suspend_ind">
		<xsl:param name="lab_checked"/>
		<xsl:param name="lab_not_checked_0"/>		
		<xsl:param name="lab_not_checked"/>
		<tr>
			<td class="wzb-form-label">
				&#160;
			</td>
			<td class="wzb-form-control wzb-ui-module-text">
				<xsl:choose>
					<xsl:when test=". = '1'">
						<xsl:value-of select="$lab_checked"/>
					</xsl:when>
					<xsl:otherwise>
						<b><xsl:value-of select="$lab_not_checked_0"/></b>
						<xsl:value-of select="$lab_not_checked"/>
					</xsl:otherwise>
				</xsl:choose>							
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="cur_usr"/>
	<xsl:template match="acl"/>
	<xsl:template match="body"/>
	<!-- =============================================================== -->
</xsl:stylesheet>
