<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
   <xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="share/label_rpt.xsl"/>
	<!-- =============================================================== -->
	<xsl:variable name="rpt_type" select="/report/report_body/template/@type"/>
	<xsl:variable name="role_list" select="/report/report_body/meta/role_list"/>
	<xsl:variable name="data_list" select="/report/report_body/spec/data_list"/>
	<xsl:variable name="module_list" select="/report/report_body/meta/module_list"/>
	<xsl:variable name="ent_id" select="/report/meta/cur_usr/@ent_id"/>
	<xsl:variable name="spec_ent_id" select="/report/report_body/spec/@ent_id"/>
	<xsl:variable name="spec_id" select="/report/report_body/spec/@spec_id"/>
	<xsl:variable name="template_id" select="/report/report_body/template/@id"/>
	<xsl:variable name="report_body" select="/report/report_body"/>
	<xsl:variable name="tc_enabled" select="/report/meta/tc_enabled"/>
	<!-- =============================================================== -->
	<xsl:template match="/report">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_mgt_rpt.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[

				goldenman = new wbGoldenMan
				mgt_rpt = new wbManagementReport;
				current = 0;

				function init(){
					frm = document.frmXml;
					if(frm.course_sel_type && frm.course_sel_type[0].checked){
						course_change(frm,frm.course_sel_type[0],false);
					}else if(frm.course_sel_type && frm.course_sel_type[1].checked){
						course_change(frm,frm.course_sel_type[1],false);
					}else if(frm.course_sel_type && frm.course_sel_type[2].checked){
						course_change(frm,frm.course_sel_type[2],false);
					}
				}
				
				function course_change(frm,obj,isResetType){
					var disableItmID;
					var disableTndID;
					var disableItmType;
					
					if(obj.value == 0){
						disableItmID = true;
						disableTndID = true;
						disableItmType = false;
					} else
					if (obj.value == 1) {
						disableItmID = false;
						disableTndID = true;
						disableItmType = true;
					}else{
						disableItmID = true;
						disableTndID = false;
						disableItmType = false;
					}

					if(frm.itm_type_lst.length){
						for(i=0;i<frm.itm_type_lst.length;i++){
							if(frm.itm_type_lst[i].type == 'checkbox'){
								frm.itm_type_lst[i].disabled = disableItmType;
								if(isResetType) {
									frm.itm_type_lst[i].checked = !disableItmType;
								}
							}
						}
					}
					if(frm.tnd_id.type == 'select-multiple'){
						frm.tnd_id.disabled = disableTndID;
						if(frm.genaddtnd_id){
							frm.genaddtnd_id.disabled = disableTndID;
						}
						if(frm.genremovetnd_id){
							frm.genremovetnd_id.disabled = disableTndID;
						}
						if(disableTndID){
							frm.tnd_id.options.length = 0;
						}
					}
					if(frm.itm_id_lst.type == 'select-multiple'){
						frm.itm_id_lst.disabled = disableItmID;
						if(frm.genadditm_id_lst){
							frm.genadditm_id_lst.disabled = disableItmID;
						}
						if(frm.genremoveitm_id_lst){
							frm.genremoveitm_id_lst.disabled = disableItmID;
						}		
						if(disableItmID){
							frm.itm_id_lst.options.length = 0;
						}
					}
				}
			
			function changelrn_cos_ContentStatus(status,type) {
				frm = document.frmXml;
				var ctn;
				if (frm.lrn_content || frm.cos_content) {
					if (type == 'lrn'){
							ctn = frm.lrn_content;
					}else{
							ctn = frm.cos_content;
					}
					for (i = 0; i < ctn.length; i++) {
						if (ctn[i]) {
								ctn[i].disabled = status;
						}
					}
				}
			}

			]]></script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
		</head>
		<BODY leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="init()">
			<form name="frmXml">
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="download" value=""/>
				<input type="hidden" name="rpt_type" value=""/>
				<input type="hidden" name="rpt_type_lst" value=""/>
				<input type="hidden" name="rpt_name" value=""/>
				<input type="hidden" name="rsp_id" value=""/>
				<input type="hidden" name="rte_id" value=""/>
				<input type="hidden" name="usr_ent_id" value=""/>
				<input type="hidden" name="tnd_id_lst" value=""/>
				<input type="hidden" name="show_run_ind" value=""/>
				<input type="hidden" name="spec_name" value=""/>
				<input type="hidden" name="spec_value" value=""/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="page_size" value=""/>
				<input type="hidden" name="window_name" value=""/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</BODY>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="report_body">
			<xsl:with-param name="lab_all_courses">所有課程</xsl:with-param>
			<xsl:with-param name="lab_by_course_type_or_catalog">課程類型／課程目錄</xsl:with-param>
			<xsl:with-param name="lab_edit_instruction">選擇以下屬性編輯報告，按<b>確定</b>儲存報告。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ex_rpt">匯出學習報告</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">儲存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_view_rpt">執行</xsl:with-param>
			<xsl:with-param name="lab_instruct">請填上報告的查詢條件</xsl:with-param>
			<xsl:with-param name="lab_instruction">你可以透過選擇以下屬性建立自己的報告，按<b>執行</b>可以直接查看報告內容。按<b>儲存</b>可以將指定的查詢條件儲存為設定報告。</xsl:with-param>
			<xsl:with-param name="lab_item_info">課程信息</xsl:with-param>
			<xsl:with-param name="lab_itm_catalog">課程目錄</xsl:with-param>
			<xsl:with-param name="lab_itm_period">期限</xsl:with-param>
			<xsl:with-param name="lab_itm_title">課程標題</xsl:with-param>
			<xsl:with-param name="lab_no_module">還沒有定義課程調查問卷範本，不能建立報告。</xsl:with-param>
			<xsl:with-param name="lab_only_cata">必須是這些目錄中的課程</xsl:with-param>
			<xsl:with-param name="lab_only_these_course">課程標題</xsl:with-param>
			<xsl:with-param name="lab_only_type_cata">必須是包含在這些 類型/目錄 中的課程</xsl:with-param>
			<xsl:with-param name="lab_others_content">調查問卷結果</xsl:with-param>
			<xsl:with-param name="lab_overall_rating">總平均</xsl:with-param>
			<xsl:with-param name="lab_question_rating">平均評分</xsl:with-param>
			<xsl:with-param name="lab_rpt_name">報告名稱</xsl:with-param>
			<xsl:with-param name="lab_run_content">
				<xsl:value-of select="$lab_const_run"/>內容</xsl:with-param>
			<xsl:with-param name="lab_run_num">
				<xsl:value-of select="$lab_const_run"/>數目</xsl:with-param>
			<xsl:with-param name="lab_select">-- 請選擇 --</xsl:with-param>
			<xsl:with-param name="lab_select_all">-- 全部 --</xsl:with-param>
			<xsl:with-param name="lab_show_all">所有版本</xsl:with-param>
			<xsl:with-param name="lab_show_lastest">最新版本</xsl:with-param>
			<xsl:with-param name="lab_standard_rpt_inst">按<b>執行</b>瀏覽報告內容。</xsl:with-param>
			<xsl:with-param name="lab_survey">課程評估問卷</xsl:with-param>
			<xsl:with-param name="lab_svy_cos_rpt">課程調查問卷報告</xsl:with-param>
			<xsl:with-param name="lab_period">錄取日期</xsl:with-param>
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_type_lst">課程類型</xsl:with-param>
			<xsl:with-param name="lab_version">版本</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">我負責的課程</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">我負責學員報讀的課程</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="report_body">
			<!-- header text -->
			<xsl:with-param name="lab_all_courses">所有课程</xsl:with-param>
			<xsl:with-param name="lab_by_course_type_or_catalog">课程类型课程目录</xsl:with-param>
			<xsl:with-param name="lab_edit_instruction">选择以下属性编辑报告，点击“确定”保存报告。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ex_rpt">导出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">删除</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">保存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_view_rpt">查看</xsl:with-param>
			<xsl:with-param name="lab_instruct">请填入报告的查询条件</xsl:with-param>
			<xsl:with-param name="lab_instruction">您可以通过选择以下属性定制自己的报告。点击“查看”可以直接查看报告内容。点击“保存”可以将指定的查询条件保存为新的模板。</xsl:with-param>
			<xsl:with-param name="lab_item_info">课程信息</xsl:with-param>
			<xsl:with-param name="lab_itm_catalog">课程目录</xsl:with-param>
			<xsl:with-param name="lab_itm_period">期限</xsl:with-param>
			<xsl:with-param name="lab_itm_title">课程标题</xsl:with-param>
			<xsl:with-param name="lab_no_module">还没有定义公共课程评估问卷，不能定制报告。</xsl:with-param>
			<xsl:with-param name="lab_only_cata">指定目录课程</xsl:with-param>
			<xsl:with-param name="lab_only_these_course">课程标题</xsl:with-param>
			<xsl:with-param name="lab_only_type_cata">必须是包含在这些类型/目录中的课程</xsl:with-param>
			<xsl:with-param name="lab_others_content">评估问卷结果</xsl:with-param>
			<xsl:with-param name="lab_overall_rating">总平均</xsl:with-param>
			<xsl:with-param name="lab_question_rating">平均评分</xsl:with-param>
			<xsl:with-param name="lab_rpt_name">报告名称</xsl:with-param>
			<xsl:with-param name="lab_run_content">
				<xsl:value-of select="$lab_const_run"/>内容</xsl:with-param>
			<xsl:with-param name="lab_run_num">
				<xsl:value-of select="$lab_const_run"/>数目</xsl:with-param>
			<xsl:with-param name="lab_select">--请选择--</xsl:with-param>
			<xsl:with-param name="lab_select_all">--全部--</xsl:with-param>
			<xsl:with-param name="lab_show_all">显示所有版本</xsl:with-param>
			<xsl:with-param name="lab_show_lastest">显示最新版本</xsl:with-param>
			<xsl:with-param name="lab_standard_rpt_inst">点击<b>查看</b>浏览报告内容</xsl:with-param>
			<xsl:with-param name="lab_survey">课程评估问卷</xsl:with-param>
			<xsl:with-param name="lab_svy_cos_rpt">课程评估问卷报告</xsl:with-param>
			<xsl:with-param name="lab_period">录取日期</xsl:with-param>
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_type_lst">课程类型</xsl:with-param>
			<xsl:with-param name="lab_version">版本</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">我负责的课程</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">我负责学员报读的课程</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="report_body">
			<xsl:with-param name="lab_all_courses">All courses</xsl:with-param>
			<xsl:with-param name="lab_by_course_type_or_catalog">By type / catalog</xsl:with-param>
			<xsl:with-param name="lab_edit_instruction">Modify the report template and click <b>Run</b> to view the report. Click <b>OK</b> to save any changes in the template. </xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ex_rpt">Export report</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">Save</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_view_rpt">Run</xsl:with-param>
			<xsl:with-param name="lab_instruct">Search criteria</xsl:with-param>
			<xsl:with-param name="lab_instruction">Build your custom report by selecting specific criteria and report data. Click <b>Run</b> to view the report. Click <b>Save</b> to save as report template.</xsl:with-param>
			<xsl:with-param name="lab_item_info">Course information</xsl:with-param>
			<xsl:with-param name="lab_itm_catalog">Course catalog</xsl:with-param>
			<xsl:with-param name="lab_itm_period">Period</xsl:with-param>
			<xsl:with-param name="lab_itm_title">Course title</xsl:with-param>
			<xsl:with-param name="lab_no_module">No course evaluation report can be built as no course evaluation form is defined.</xsl:with-param>
			<xsl:with-param name="lab_only_cata">Only courses in these catalogs</xsl:with-param>
			<xsl:with-param name="lab_only_these_course">Only these courses</xsl:with-param>
			<xsl:with-param name="lab_only_type_cata">Only courses in these types/catalogs</xsl:with-param>
			<xsl:with-param name="lab_others_content">Evaluation results</xsl:with-param>
			<xsl:with-param name="lab_overall_rating">Overall average</xsl:with-param>
			<xsl:with-param name="lab_question_rating">Average ratings</xsl:with-param>
			<xsl:with-param name="lab_rpt_name">Template name</xsl:with-param>
			<xsl:with-param name="lab_run_content">
				<xsl:value-of select="$lab_const_run"/> content</xsl:with-param>
			<xsl:with-param name="lab_run_num">
				<xsl:value-of select="$lab_const_run"/> number</xsl:with-param>
			<xsl:with-param name="lab_select">-- Please select --</xsl:with-param>
			<xsl:with-param name="lab_select_all">-- All --</xsl:with-param>
			<xsl:with-param name="lab_show_all">All version</xsl:with-param>
			<xsl:with-param name="lab_show_lastest">Latest version</xsl:with-param>
			<xsl:with-param name="lab_standard_rpt_inst">Specify the report criteria and click "<b>Run</b>" to view the standard report.</xsl:with-param>
			<xsl:with-param name="lab_survey">Course evaluation form</xsl:with-param>
			<xsl:with-param name="lab_svy_cos_rpt">Course evaluation report</xsl:with-param>
			<xsl:with-param name="lab_period">Enrollment date</xsl:with-param>
			<xsl:with-param name="lab_from">From</xsl:with-param>
			<xsl:with-param name="lab_to">to</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_type_lst">Course type</xsl:with-param>
			<xsl:with-param name="lab_version">Version</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">My responsible courses</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">Courses that have been enrolled by my responsible learners</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="report_body">
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_remove"/>
		<xsl:param name="lab_g_form_btn_ex_rpt"/>
		<xsl:param name="lab_g_form_btn_view_rpt"/>
		<xsl:param name="lab_g_form_btn_save"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_instruction"/>
		<xsl:param name="lab_standard_rpt_inst"/>
		<xsl:param name="lab_edit_instruction"/>
		<xsl:param name="lab_instruct"/>
		<xsl:param name="lab_rpt_name"/>
		<xsl:param name="lab_itm_catalog"/>
		<xsl:param name="lab_itm_title"/>
		<xsl:param name="lab_type_lst"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_select_all"/>
		<xsl:param name="lab_self_study"/>
		<xsl:param name="lab_itm_period"/>

		<xsl:param name="lab_item_info"/>
		<xsl:param name="lab_run_content"/>
		<xsl:param name="lab_others_content"/>
		<xsl:param name="lab_run_num"/>

		<xsl:param name="lab_question_rating"/>
		<xsl:param name="lab_overall_rating"/>
		<xsl:param name="lab_version"/>
		<xsl:param name="lab_show_all"/>
		<xsl:param name="lab_show_lastest"/>
		<xsl:param name="lab_select"/>
		<xsl:param name="lab_survey"/>
		<xsl:param name="lab_svy_cos_rpt"/>
		<xsl:param name="lab_no_module"/>
		<xsl:param name="lab_only_these_course"/>
		<xsl:param name="lab_by_course_type_or_catalog"/>
		<xsl:param name="lab_all_courses"/>
		<xsl:param name="lab_only_cata"/>
		<xsl:param name="lab_only_type_cata"/>
		<xsl:param name="lab_period"/>
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_answer_for_course"/>
		<xsl:param name="lab_answer_for_lrn_course"/>
				<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_TRAINING_REPORT_MGT</xsl:with-param>
			<xsl:with-param name="parent_code" >FTN_AMD_TRAINING_REPORT_MGT</xsl:with-param>
			<xsl:with-param name="page_title" >
				<xsl:choose>
					<xsl:when test="$spec_ent_id = '0' and $spec_id!='0'">
						<xsl:call-template name="get_rte_title">
							<xsl:with-param name="rte_type" select="template/@type"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="get_rte_title">
							<xsl:with-param name="rte_type" select="/report/report_body/template/@type"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:call-template name="get_rte_title">
					<xsl:with-param name="rte_type" select="/report/report_body/template/@type"/>
				</xsl:call-template>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$spec_id!='0' and $spec_ent_id!='0'">
						<xsl:copy-of select="$lab_edit_instruction"/>
					</xsl:when>
					<xsl:when test="$spec_id!='0' and $spec_ent_id='0'">
						<xsl:copy-of select="$lab_standard_rpt_inst"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy-of select="$lab_instruction"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="count(meta/module_list/module)=0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_module"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
					<!-- start match data list item -->
					<xsl:apply-templates select="spec">
						<xsl:with-param name="lab_rpt_name" select="$lab_rpt_name"/>
						<xsl:with-param name="lab_itm_catalog" select="$lab_itm_catalog"/>
						<xsl:with-param name="lab_itm_title" select="$lab_itm_title"/>
						<xsl:with-param name="lab_type_lst" select="$lab_type_lst"/>
						<xsl:with-param name="lab_type" select="$lab_type"/>
						<xsl:with-param name="lab_select_all" select="$lab_select_all"/>
						<xsl:with-param name="lab_self_study" select="$lab_self_study"/>
						<xsl:with-param name="lab_itm_period" select="$lab_itm_period"/>
						<xsl:with-param name="lab_to" select="$lab_to"/>
						<xsl:with-param name="lab_version" select="$lab_version"/>
						<xsl:with-param name="lab_show_all" select="$lab_show_all"/>
						<xsl:with-param name="lab_show_lastest" select="$lab_show_lastest"/>
						<xsl:with-param name="lab_select" select="$lab_select"/>
						<xsl:with-param name="lab_survey" select="$lab_survey"/>
						<xsl:with-param name="lab_only_these_course" select="$lab_only_these_course"/>
						<xsl:with-param name="lab_by_course_type_or_catalog" select="$lab_by_course_type_or_catalog"/>
						<xsl:with-param name="lab_all_courses" select="$lab_all_courses"/>
						<xsl:with-param name="lab_only_type_cata" select="$lab_only_type_cata"/>
						<xsl:with-param name="lab_only_cata" select="$lab_only_cata"/>
						<xsl:with-param name="lab_period" select="$lab_period"/>
						<xsl:with-param name="lab_from" select="$lab_from"/>
						<xsl:with-param name="lab_to" select="$lab_to"/>
						<xsl:with-param name="lab_answer_for_course" select="$lab_answer_for_course"/>
						<xsl:with-param name="lab_answer_for_lrn_course" select="$lab_answer_for_lrn_course"/>
					</xsl:apply-templates>
					<tr>
						<td width="20%" align="right" class="wzb-form-label">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td width="80%"  class="wzb-ui-module-text">
							<span class="wzb-form-star">*</span>
							<span class="Text">
								<xsl:value-of select="$lab_info_required"/>
							</span>
						</td>
					</tr>
				</table>
				<div class="wzb-bar">
					<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
						<tr>
							<td align="center">
								<!-- view button -->
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_view_rpt"/>
									<xsl:with-param name="wb_gen_btn_href">Javascript:current++;mgt_rpt.get_rpt_adv(document.frmXml,'<xsl:value-of select="$rpt_type"/>','<xsl:value-of select="$template_id"/>','<xsl:value-of select="$report_body/template/xsl_list/xsl[@type='execute']/."/>','false',current,"<xsl:value-of select="$wb_lang"/>")</xsl:with-param>
								</xsl:call-template>
								<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								<!-- insert/update button -->
								<xsl:choose>
									<xsl:when test="$spec_id = '0'">
										<!-- insert button -->
										<xsl:call-template name="wb_gen_form_button">
											<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_save"/>
											<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.ins_rpt_prep_popup(document.frmXml,'<xsl:value-of select="$template_id"/>','<xsl:value-of select="$ent_id"/>','<xsl:value-of select="$rpt_type"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
										</xsl:call-template>
										<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:if test="not($spec_ent_id = '0')">
											<!-- update button -->
											<xsl:call-template name="wb_gen_form_button">
												<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
												<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.upd_rpt_exec(document.frmXml,'<xsl:value-of select="$template_id"/>','<xsl:value-of select="$spec_id"/>','<xsl:value-of select="$ent_id"/>','<xsl:value-of select="$rpt_type"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
											</xsl:call-template>
											<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>
								<!-- cancel button -->
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_g_form_btn_cancel"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">Javascript:history.back()</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
					</table>
				</div>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="spec">
		<xsl:param name="lab_rpt_name"/>
		<xsl:param name="lab_itm_catalog"/>
		<xsl:param name="lab_itm_title"/>
		<xsl:param name="lab_type_lst"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_select_all"/>
		<xsl:param name="lab_self_study"/>
		<xsl:param name="lab_itm_period"/>
		<xsl:param name="lab_version"/>
		<xsl:param name="lab_show_all"/>
		<xsl:param name="lab_show_lastest"/>
		<xsl:param name="lab_select"/>
		<xsl:param name="lab_survey"/>
		<xsl:param name="lab_only_these_course"/>
		<xsl:param name="lab_by_course_type_or_catalog"/>
		<xsl:param name="lab_only_type_cata"/>
		<xsl:param name="lab_only_cata"/>
		<xsl:param name="lab_all_courses"/>
		<xsl:param name="lab_period"/>
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_answer_for_course"/>
		<xsl:param name="lab_answer_for_lrn_course"/>
		<!--== Spec Name ==-->
		<xsl:if test="title != '' and $spec_ent_id != '0'">
			<tr>
				<td align="right" valign="top"  class="wzb-form-label">
					<span class="wzb-form-star">*</span>
					<span class="TitleText">
						<xsl:value-of select="$lab_rpt_name"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td  class="wzb-form-control">
					<span class="Text">
						<input type="text" name="rsp_title" value="{title}" size="30" style="width:350px;" maxlength="200" class="wzb-inputText"/>
					</span>
				</td>
			</tr>
		</xsl:if>
		<tr>
			<td align="right" valign="top"  class="wzb-form-label">
				<span class="wzb-form-star">*</span>
				<span class="TitleText">
					<xsl:value-of select="$lab_itm_title"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td  class="wzb-form-control">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td valign="top">
							<span>
								<input type="radio" name="course_sel_type" value="0" id="rdo_sel_by_all_course_title" onclick="course_change(document.frmXml,this,true);changelrn_cos_ContentStatus(false,'cos')">
									<xsl:if test="$report_body/spec/data_list/data[@name='itm_id'] and $report_body/spec/data_list/data[@name='itm_id']/@value = '' or ( not($report_body/spec/data_list/data[@name='itm_id']) and not($report_body/spec/data_list/data[@name='tnd_id']))">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
							</span>
							<label for="rdo_sel_by_all_course_title" onclick="changelrn_cos_ContentStatus(false,'cos')">
								<span class="Text">
									<xsl:value-of select="$lab_all_courses"/>
								</span>
							</label>
						</td>
					</tr>
					<xsl:if test="$tc_enabled = 'true'">
						<!--我负责的课程-->
						<tr>
							<td>
								<span style="padding:0 0 0 20px;">
									<input type="checkbox" name="cos_content" id="chk_answer_for_course">
										<xsl:if test="$report_body/spec/data_list/data[@name='answer_for_course' and @value='1'] or not(/report/report_body/spec/data_list) ">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
								</span>
								<label for="chk_answer_for_course">
									<span class="Text">
										<xsl:value-of select="$lab_answer_for_course"/>
									</span>
								</label>
							</td>
						</tr>
						<!--我负责的学员学习的课程-->
						<tr>
							<td>
								<span style="padding:0 0 0 20px;">
									<input type="checkbox" name="cos_content" id="chk_answer_for_lrn_course">
										<xsl:if test="$report_body/spec/data_list/data[@name='answer_for_lrn_course' and @value='1'] or not(/report/report_body/spec/data_list) ">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
								</span>
								<label for="chk_answer_for_lrn_course">
									<span class="Text">
										<xsl:value-of select="$lab_answer_for_lrn_course"/>
									</span>
								</label>
							</td>
						</tr>
					</xsl:if>
					<tr>
						<td valign="top">
							<span>
								<input type="radio" name="course_sel_type" value="1" id="rdo_sel_by_course_title" onclick="course_change(document.frmXml,this,true);changelrn_cos_ContentStatus(true,'cos')">
									<xsl:if test="$report_body/spec/data_list/data[@name='itm_id']">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
							</span>
							<label for="rdo_sel_by_course_title" onclick="changelrn_cos_ContentStatus(true,'cos')">
								<span class="Text">
									<xsl:value-of select="$lab_only_these_course"/>
									<xsl:text>：</xsl:text>
								</span>
							</label>
						</td>
					</tr>
					<tr>
						<td>
							<xsl:variable name="tree_type">
								<xsl:choose>
									<xsl:when test="$tc_enabled = 'true'">tc_catalog_item</xsl:when>
									<xsl:otherwise>item</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<!-- itm_id_lst -->
							<xsl:call-template name="wb_goldenman">
								<xsl:with-param name="field_name">itm_id_lst</xsl:with-param>
								<xsl:with-param name="custom_js_code">course_change(document.frmXml,document.frmXml.course_sel_type[1],true);document.frmXml.course_sel_type[1].checked = true;</xsl:with-param>
								<xsl:with-param name="name">itm_id_lst</xsl:with-param>
								<xsl:with-param name="box_size">4</xsl:with-param>
								<xsl:with-param name="tree_type"><xsl:value-of select="$tree_type"/></xsl:with-param>
								<xsl:with-param name="select_type">4</xsl:with-param>
								<xsl:with-param name="args_type">row</xsl:with-param>
								<xsl:with-param name="complusory_tree">0</xsl:with-param>
								<xsl:with-param name="custom_js_code_extra">args = goldenman.set_global_catalog_label(args,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
								<xsl:with-param name="pick_root">0</xsl:with-param>
								<xsl:with-param name="option_list">
									<xsl:apply-templates select="$report_body/spec/data_list/data[@name='itm_id']"/>
								</xsl:with-param>
								<xsl:with-param name="label_add_btn">
									<xsl:value-of select="$lab_gen_select"/>
								</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
					<xsl:choose>
						<xsl:when test="count($report_body/meta/item_type_list/item_type) &gt; 1">
							<!-- show type check box-->
							<tr>
								<td valign="top">
									<span>
										<input type="radio" name="course_sel_type" value="2" id="rdo_sel_by_type_or_cata" onclick="course_change(document.frmXml,this,true);changelrn_cos_ContentStatus(true,'cos')">
											<xsl:if test="$report_body/spec/data_list/data[@name='tnd_id']">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</input>
									</span>
									<label for="rdo_sel_by_type_or_cata"  onclick="changelrn_cos_ContentStatus(true,'cos')">
										<span class="Text">
											<xsl:value-of select="$lab_only_cata"/>
											<xsl:text>：</xsl:text>
										</span>
									</label>
								</td>
							</tr>
							<tr>
								<td>
									<xsl:variable name="tree_type">
										<xsl:choose>
											<xsl:when test="$tc_enabled = 'true'">tc_catalog</xsl:when>
											<xsl:otherwise>catalog</xsl:otherwise>
										</xsl:choose>
									</xsl:variable>
									<!-- tnd_id_lst -->
									<xsl:call-template name="wb_goldenman">
										<xsl:with-param name="field_name">tnd_id</xsl:with-param>
										<xsl:with-param name="custom_js_code">course_change(document.frmXml,document.frmXml.course_sel_type[2],true);document.frmXml.course_sel_type[2].checked = true;</xsl:with-param>
										<xsl:with-param name="name">tnd_id</xsl:with-param>
										<xsl:with-param name="box_size">4</xsl:with-param>
										<xsl:with-param name="tree_type"><xsl:value-of select="$tree_type"/></xsl:with-param>
										<xsl:with-param name="select_type">1</xsl:with-param>
										<xsl:with-param name="args_type">row</xsl:with-param>
										<xsl:with-param name="complusory_tree">0</xsl:with-param>
										<xsl:with-param name="custom_js_code_extra">args = goldenman.set_global_catalog_label(args,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
										<xsl:with-param name="option_list">
											<xsl:apply-templates select="$report_body/spec/data_list/data[@name='tnd_id']"/>
										</xsl:with-param>
										<xsl:with-param name="pick_root">0</xsl:with-param>
										<xsl:with-param name="label_add_btn">
											<xsl:value-of select="$lab_gen_select"/>
										</xsl:with-param>
									</xsl:call-template>
								</td>
							</tr>
							<!-- <tr>
								<td>
									<table cellpadding="0" cellspacing="0" border="0">
										<tr>
											<td valign="top">
												<span class="Text">
													<xsl:value-of select="$lab_type"/>
													<xsl:text>：&#160;</xsl:text>
												</span>
												itm_type_lst
											</td>
										</tr>
										<tr>
											<td valign="top">
												<xsl:choose>
													<xsl:when test="count($report_body/meta/item_type_list/item_type) &gt; 0">
														<table cellpadding="0" cellspacing="0" border="0">
															<xsl:for-each select="$report_body/meta/item_type_list/item_type">
																<xsl:if test="position() mod 2 != 0 ">
																	<xsl:text disable-output-escaping="yes">&lt;tr  valign="top" &gt;</xsl:text>
																</xsl:if>
																<td valign="middle" width="33%">
																	<input type="checkbox" name="itm_type_lst" value="{@dummy_type}" id="itm_type_{@dummy_type}">
																		<xsl:choose>
																			<xsl:when test="count($report_body/spec/data_list/data[@name='itm_id'])!= 0">
																				<xsl:attribute name="disabled">disabled</xsl:attribute>
																			</xsl:when>
																			<xsl:when test="$report_body/spec/data_list/data[@name='itm_type']/@value = @dummy_type or count($report_body/spec/data_list/data[@name='itm_type']) = 0">
																				<xsl:attribute name="checked">checked</xsl:attribute>
																			</xsl:when>
																		</xsl:choose>
																	</input>
																	<label for="itm_type_{@dummy_type}">
																		<span class="Text">
																			<xsl:call-template name="get_ity_title">
																				<xsl:with-param name="itm_type" select="@dummy_type"/>
																			</xsl:call-template>
																		</span>
																	</label>
																</td>
																<xsl:choose>
																	<xsl:when test="position() mod 2 = 0">
																		<td></td>
																		<xsl:text disable-output-escaping="yes">&lt;/tr&gt;</xsl:text>
																	</xsl:when>
																	<xsl:when test="position() mod 2 != 0 and position() = last()">
																		<xsl:text disable-output-escaping="yes">&lt;td colspan="3" &gt;</xsl:text>
																		<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
																		<xsl:text disable-output-escaping="yes">&lt;/td&gt;&lt;/tr&gt;</xsl:text>
																	</xsl:when>
																</xsl:choose>
															</xsl:for-each>
														</table>
													</xsl:when>
													<xsl:otherwise>
														<input type="hidden" name="itm_type_lst" value=""/>
													</xsl:otherwise>
												</xsl:choose>
											</td>
										</tr>
									</table>
								</td>
							</tr> -->
						</xsl:when>
						<xsl:otherwise>
							<tr>
								<td valign="top" align="right">
									<input type="radio" name="course_sel_type" value="2" id="rdo_sel_by_type_or_cata" onclick="course_change(document.frmXml,this,true)">
										<xsl:if test="$report_body/spec/data_list/data[@name='tnd_id'] or $report_body/spec/data_list/data[@name='itm_type']">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
								</td>
								<td>
									<label for="rdo_sel_by_type_or_cata">
										<span class="Text">
											<xsl:value-of select="$lab_only_cata"/>
										</span>
									</label>
								</td>
							</tr>
							<tr>
								<td>
									<input type="hidden" name="itm_type_lst" value="{$report_body/meta/item_type_list/item_type/@id}"/>
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								<td>
									<!-- tnd_id_lst -->
									<xsl:call-template name="wb_goldenman">
										<xsl:with-param name="field_name">tnd_id</xsl:with-param>
										<xsl:with-param name="custom_js_code">course_change(document.frmXml,document.frmXml.course_sel_type[2],true);document.frmXml.course_sel_type[2].checked = true;</xsl:with-param>
										<xsl:with-param name="name">tnd_id</xsl:with-param>
										<xsl:with-param name="box_size">4</xsl:with-param>
										<xsl:with-param name="tree_type">catalog</xsl:with-param>
										<xsl:with-param name="select_type">1</xsl:with-param>
										<xsl:with-param name="args_type">row</xsl:with-param>
										<xsl:with-param name="complusory_tree">0</xsl:with-param>
										<xsl:with-param name="custom_js_code_extra">args = goldenman.set_global_catalog_label(args,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
										<xsl:with-param name="option_list">
											<xsl:apply-templates select="$report_body/spec/data_list/data[@name='tnd_id']"/>
										</xsl:with-param>
										<xsl:with-param name="label_add_btn">
											<xsl:value-of select="$lab_gen_select"/>
										</xsl:with-param>
									</xsl:call-template>
								</td>
							</tr>
						</xsl:otherwise>
					</xsl:choose>
				</table>
			</td>
		</tr>
		
		<!-- 类型 -->
		<xsl:if test="count($report_body/meta/item_type_list/item_type) &gt; 1">
			<tr>
				<td align="right" width="20%"  class="wzb-form-label" valign="top">
						<span class="TitleText">
							<xsl:value-of select="$lab_type"/>
							<xsl:text>：</xsl:text>
						</span>
				</td>
				<td width="80%"  class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="count($report_body/meta/item_type_list/item_type) &gt; 0">
							<table cellpadding="0" cellspacing="0" border="0">
								<xsl:for-each select="$report_body/meta/item_type_list/item_type">
								  <xsl:if test="@dummy_type != 'INTEGRATED'">
									<xsl:if test="position() mod 2 != 0 ">
										<xsl:text disable-output-escaping="yes">&lt;tr  valign="top" &gt;</xsl:text>
									</xsl:if>
									<td valign="middle" width="33%">
										<input type="checkbox" name="itm_type_lst" value="{@dummy_type}" id="itm_type_{@dummy_type}">
											<xsl:choose>
												<xsl:when test="count($report_body/spec/data_list/data[@name='itm_id'])!= 0">
													<xsl:attribute name="disabled">disabled</xsl:attribute>
												</xsl:when>
												<xsl:when test="$report_body/spec/data_list/data[@name='itm_type']/@value = @dummy_type or count($report_body/spec/data_list/data[@name='itm_type']) = 0">
													<xsl:attribute name="checked">checked</xsl:attribute>
												</xsl:when>
											</xsl:choose>
										</input>
										<label for="itm_type_{@dummy_type}">
											<span class="Text">
												<xsl:call-template name="get_ity_title">
													<xsl:with-param name="itm_type" select="@dummy_type"/>
												</xsl:call-template>
											</span>
										</label>
									</td>
									<xsl:choose>
										<xsl:when test="position() mod 2 = 0">
											<td></td>
											<xsl:text disable-output-escaping="yes">&lt;/tr&gt;</xsl:text>
										</xsl:when>
										<xsl:when test="position() mod 2 != 0 and position() = last()">
											<xsl:text disable-output-escaping="yes">&lt;td colspan="3" &gt;</xsl:text>
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
											<xsl:text disable-output-escaping="yes">&lt;/td&gt;&lt;/tr&gt;</xsl:text>
										</xsl:when>
									</xsl:choose>
									</xsl:if>
								</xsl:for-each>
							</table>
						</xsl:when>
						<xsl:otherwise>
							<input type="hidden" name="itm_type_lst" value=""/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
		
		<!--== Item Type ==-->
		<!--== Version ==-->
		<input type="hidden" name="show_old_version" value="0"/>
		<!--== Select Survey ==-->
		<tr>
			<td align="right" width="20%"  class="wzb-form-label">
					<span class="wzb-form-star">*</span>
					<span class="TitleText">
						<xsl:value-of select="$lab_survey"/>
						<xsl:text>：</xsl:text>
					</span>
			</td>
			<td width="80%"  class="wzb-form-control">
					<span class="Text">
						<select name="mod_id" class="Select">
							<option value="">
								<xsl:value-of select="$lab_select"/>
							</option>
							<xsl:for-each select="$module_list/module">
								<option value="{@id}">
									<xsl:if test="@id = $data_list/data[@name='mod_id']/@value">
										<xsl:attribute name="selected">selected</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="title"/>
								</option>
							</xsl:for-each>
						</select>
					</span>
			</td>
		</tr>
		<!-- =============================================================== -->
		<!--== Period ==-->
		<tr>
			<td width="20%" align="right"  class="wzb-form-label">
				<span class="TitleText">
					<xsl:value-of select="$lab_period"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="80%"  class="wzb-form-control">
					<span class="Text">
						<xsl:value-of select="$lab_from"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:call-template name="display_form_input_time">
							<xsl:with-param name="fld_name">att_create_start_datetime</xsl:with-param>
							<xsl:with-param name="hidden_fld_name">att_create_start_datetime</xsl:with-param>
							<xsl:with-param name="show_label">Y</xsl:with-param>
							<xsl:with-param name="timestamp">
								<xsl:value-of select="data_list/data[@name='att_create_start_datetime']/@value"/>
							</xsl:with-param>
						</xsl:call-template>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$lab_to"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:call-template name="display_form_input_time">
							<xsl:with-param name="fld_name">att_create_end_datetime</xsl:with-param>
							<xsl:with-param name="hidden_fld_name">att_create_end_datetime</xsl:with-param>
							<xsl:with-param name="show_label">Y</xsl:with-param>
							<xsl:with-param name="timestamp">
								<xsl:value-of select="data_list/data[@name='att_create_end_datetime']/@value"/>
							</xsl:with-param>
						</xsl:call-template>
					</span>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data">
		<option value="{@value}">
			<xsl:variable name="name" select="@name"/>
			<xsl:variable name="value" select="@value"/>
			<xsl:value-of select="$report_body/presentation/data[@name=$name and @value = $value]/@display"/>
		</option>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
