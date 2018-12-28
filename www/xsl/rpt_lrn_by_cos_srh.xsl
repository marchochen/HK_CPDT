<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="share/label_rpt.xsl"/>
	<xsl:import href="share/label_role.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:strip-space elements="*"/>
	<!-- =============================================================== -->
	<xsl:variable name="rpt_type" select="report/report_body/template/@type"/>
	<xsl:variable name="role_list" select="/report/report_body/meta/role_list"/>
	<xsl:variable name="data_list" select="/report/report_body/spec/data_list"/>
	<xsl:variable name="ent_id" select="/report/meta/cur_usr/@ent_id"/>
	<xsl:variable name="spec_ent_id" select="/report/report_body/spec/@ent_id"/>
	<xsl:variable name="spec_id" select="/report/report_body/spec/@spec_id"/>
	<xsl:variable name="template_id" select="/report/template/@id"/>
	<xsl:variable name="report_body" select="/report/report_body"/>
	<xsl:variable name="root_ent_id" select="/report/meta/cur_usr/@root_ent_id"/>
	<xsl:variable name="profile_attributes" select="/report/meta/profile_attributes"/>
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
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_lrn_soln.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_mgt_rpt.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			goldenman = new wbGoldenMan;
			mgt_rpt = new wbManagementReport;
			usr = new wbUserGroup;
			current = 0
			
			function getPopupUsrLst(fld_name,id_lst,nm_lst, usr_argv){
				usr_ent_id_lst(usr_argv);
			}
			
			function changeContentStatus(status) {
				frm = document.frmXml;
				var ctn = [frm.usr_content, frm.content];
				for (i = 0; i < ctn.length; i++) {
					if (ctn[i]) {
						if (ctn[i].length) {					
							for (j = 0; j < ctn[i].length; j++) {
								ctn[i][j].disabled = status;
							}
						} else {
							ctn[i].disabled = status;
						}
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

			function usr_change(frm, obj){
					if(obj.value == 0){
						var pos = true;
						var neg = false;
					}else if (obj.value == 1){
						var pos = false;
						var neg = true;
					}else{
						var pos =true;
						var neg = true;
					}
					if(frm.usr_ent_id_lst.type == 'select-multiple'){
						frm.usr_ent_id_lst.disabled = neg;
						if(frm.genaddusr_ent_id_lst){
							frm.genaddusr_ent_id_lst.disabled = neg;
						}
						if(frm.genremoveusr_ent_id_lst){
							frm.genremoveusr_ent_id_lst.disabled = neg;
						}		
						if(frm.gensearchusr_ent_id_lst){
							frm.gensearchusr_ent_id_lst.disabled = neg;
						}		
						if(neg == true){
							frm.usr_ent_id_lst.options.length = 0
						}
					}
					if(frm.usg_ent_id_lst.type == 'select-multiple'){
						frm.usg_ent_id_lst.disabled = pos ;
						if(frm.genaddusg_ent_id_lst){
							frm.genaddusg_ent_id_lst.disabled = pos ;
						}
						if(frm.genremoveusg_ent_id_lst){
							frm.genremoveusg_ent_id_lst.disabled = pos ;
						}		
						if(frm.gensearchusg_ent_id_lst){
							frm.gensearchusg_ent_id_lst.disabled = pos ;
						}		
						if(pos == true){
							frm.usg_ent_id_lst.options.length = 0
						}
					}


			}
			function course_change(frm,obj){
					if(obj.value == 0){
						var pos =true;
						var neg = false;
					}else if (obj.value == 1){
						var pos = false;
						var neg = true;
					}else{
						var pos = true;
						var neg = true;					
					}

					if(frm.tnd_id.type == 'select-multiple'){
						frm.tnd_id.disabled = pos;
						if(frm.genaddtnd_id){
							frm.genaddtnd_id.disabled = pos;
						}
						if(frm.genremovetnd_id){
							frm.genremovetnd_id.disabled = pos;
						}		
						if(pos == true){
							frm.tnd_id.options.length = 0
						}
					}
					if(frm.itm_id_lst.type == 'select-multiple'){
						frm.itm_id_lst.disabled = neg;
						if(frm.genadditm_id_lst){
							frm.genadditm_id_lst.disabled = neg;
						}
						if(frm.genremoveitm_id_lst){
							frm.genremoveitm_id_lst.disabled = neg;
						}		
						if(neg == true){
							frm.itm_id_lst.options.length = 0
						}
					}
				}
				
				function init(){
					frm = document.frmXml
					if(frm.course_sel_type && frm.course_sel_type[0].checked){
						course_change(frm,frm.course_sel_type[0])
					}else if(frm.course_sel_type && frm.course_sel_type[1].checked){
						course_change(frm,frm.course_sel_type[1])
					}else{
						course_change(frm,frm.course_sel_type[2])
					}

					if(frm.usr_sel_all_user && frm.usr_sel_all_user[0].checked){
						usr_change(frm,frm.usr_sel_all_user[0])
					}else if(frm.usr_sel_all_user && frm.usr_sel_all_user[1].checked){
						usr_change(frm,frm.usr_sel_all_user[1])
					}else{
						usr_change(frm,frm.usr_sel_all_user[2])
					}
					
				}
				
				function redothis( ){
				   frm=document.frmXml;
				   }
				   

		]]></script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="init()">
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
				<input type="hidden" name="window_name" value=""/>
				<!--<input type="hidden" name="page_size" value=""/>-->
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="report_body">
			<!-- header text -->
			<xsl:with-param name="lab_lrn_report">學習報告</xsl:with-param>
			<xsl:with-param name="lab_wizard">建立精靈</xsl:with-param>
			<xsl:with-param name="lab_instruction">你可以透過選擇以下屬性建立自己的報告，按<b>執行</b>可以直接查看報告內容。按<b>儲存</b>可以將指定的查詢條件儲存為設定報告。</xsl:with-param>
			<xsl:with-param name="lab_edit_instruction">選擇以下屬性編輯報告，按<b>確定</b>儲存報告。</xsl:with-param>
			<xsl:with-param name="lab_standard_rpt_inst">指定報告標準並按<b>執行</b>瀏覽你的下屬的學習報告。</xsl:with-param>
			<xsl:with-param name="lab_instruct">請填入報告的查詢條件。</xsl:with-param>
			<!-- form text -->
			<xsl:with-param name="lab_rpt_name">報告名稱</xsl:with-param>
			<xsl:with-param name="lab_rpt_name_length">（不超過80字元）</xsl:with-param>
			<!-- search criteria text -->
			<xsl:with-param name="lab_all_courses">所有課程</xsl:with-param>
			<xsl:with-param name="lab_lrn_group">學員</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">所有學員</xsl:with-param>
			<xsl:with-param name="lab_by_selected_user">只有這些學員</xsl:with-param>
			<xsl:with-param name="lab_by_selected_usg">指定用戶組</xsl:with-param>
			<xsl:with-param name="lab_itm_catalog">*課程目錄</xsl:with-param>
			<xsl:with-param name="lab_itm_title">課程標題</xsl:with-param>
			<xsl:with-param name="lab_itm">課程</xsl:with-param>
			<xsl:with-param name="lab_by_itm">只有這些課程</xsl:with-param>
			<xsl:with-param name="lab_by_catalog">只包含此目錄中的課程</xsl:with-param>
			<xsl:with-param name="lab_partial">部分匹配</xsl:with-param>
			<xsl:with-param name="lab_exact">完全匹配</xsl:with-param>
			<xsl:with-param name="lab_type_lst">課程類型</xsl:with-param>
			<xsl:with-param name="lab_select_all">-- 全部 --</xsl:with-param>
			<xsl:with-param name="lab_completion_all">所有</xsl:with-param>
			<xsl:with-param name="lab_ats_id_lst">完成狀況</xsl:with-param>
			<xsl:with-param name="lab_include_no_record_course">包含無學習記錄課程</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_show_in_progress_attendance">顯示 <b>進行中</b> 活動</xsl:with-param>
			<xsl:with-param name="lab_enollment_date">註冊日期</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<!-- content attribute text -->
			<xsl:with-param name="lab_content">報告欄</xsl:with-param>
			<!-- content attribute text of course -->
			<xsl:with-param name="lab_item_content">課程信息</xsl:with-param>
			<!-- content attribute text of run -->
			<xsl:with-param name="lab_run_content">
				<xsl:value-of select="$lab_const_run"/>內容</xsl:with-param>
			<!-- content attribute text of learner -->
			<xsl:with-param name="lab_user_content">用戶</xsl:with-param>
			<!-- content attribute text of others -->
			<xsl:with-param name="lab_app_ext_4">Application Ext 4</xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">報名日期</xsl:with-param>
			<xsl:with-param name="lab_att_date">完成日期</xsl:with-param>
			<xsl:with-param name="lab_att_rate">
				<xsl:value-of select="$lab_const_session"/>到課率</xsl:with-param>
			<xsl:with-param name="lab_att_remark">考勤備註</xsl:with-param>
			<xsl:with-param name="lab_att_status">考勤狀況</xsl:with-param>
			<xsl:with-param name="lab_sort_order">排序</xsl:with-param>
			<xsl:with-param name="lab_sort_ordera">順序</xsl:with-param>
			<xsl:with-param name="lab_sort_orderd">逆序</xsl:with-param>
			<xsl:with-param name="lab_cov_commence_datetime">第一次訪問</xsl:with-param>
			<xsl:with-param name="lab_cov_last_acc_datetime">最後一次訪問</xsl:with-param>
			<xsl:with-param name="lab_cov_score">成績</xsl:with-param>
			<xsl:with-param name="lab_cov_total_time">學習時間</xsl:with-param>
			<xsl:with-param name="lab_others_content">其他</xsl:with-param>
			<xsl:with-param name="lab_total_attempt">嘗試</xsl:with-param>
			<xsl:with-param name="lab_item_page">每頁分組數</xsl:with-param>
			<xsl:with-param name="lab_columns_info">每個課程分組顯示以下信息</xsl:with-param>
			<xsl:with-param name="lab_show_course_only">只顯示課程統計摘要</xsl:with-param>
			<xsl:with-param name="lab_show_course_all">顯示課程統計摘要以及包含以下信息的學習記錄</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">儲存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_view_rpt">執行</xsl:with-param>
			<!-- unused? -->
			<xsl:with-param name="lab_attendance">考勤</xsl:with-param>
			<xsl:with-param name="lab_run_num">
				<xsl:value-of select="$lab_const_run"/>數目</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">我負責的學員</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">我負責的課程</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">報讀我負責課程的學員</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">我負責學員報讀的課程</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="report_body">
			<!-- header text -->
			<xsl:with-param name="lab_lrn_report">学员学习报告</xsl:with-param>
			<xsl:with-param name="lab_wizard">创建向导</xsl:with-param>
			<xsl:with-param name="lab_instruction">您可以通过选择以下属性定制自己的报告。点击“查看”可以直接查看报告内容。点击“保存”可以将指定的查询条件保存为新的模板。</xsl:with-param>
			<xsl:with-param name="lab_edit_instruction">选择以下属性编辑报告，点击“确定”保存报告。</xsl:with-param>
			<xsl:with-param name="lab_standard_rpt_inst">指定报告查看标准，并点击 <b>查看</b> 浏览学习报告</xsl:with-param>
			<xsl:with-param name="lab_instruct">请填入报告的查询条件</xsl:with-param>
			<!-- form text -->
			<xsl:with-param name="lab_rpt_name">报告名称</xsl:with-param>
			<xsl:with-param name="lab_rpt_name_length">（长度不超过80字符）</xsl:with-param>
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn_group">学员</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">所有学员</xsl:with-param>
			<xsl:with-param name="lab_all_courses">所有课程</xsl:with-param>
			<xsl:with-param name="lab_by_selected_user">指定学员</xsl:with-param>
			<xsl:with-param name="lab_by_selected_usg">指定用户组</xsl:with-param>
			<xsl:with-param name="lab_itm_catalog">*课程目录</xsl:with-param>
			<xsl:with-param name="lab_itm_title">课程标题</xsl:with-param>
			<xsl:with-param name="lab_itm">课程</xsl:with-param>
			<xsl:with-param name="lab_by_itm">指定课程</xsl:with-param>
			<xsl:with-param name="lab_by_catalog">指定课程目录</xsl:with-param>
			<xsl:with-param name="lab_partial">部分匹配</xsl:with-param>
			<xsl:with-param name="lab_exact">完全匹配</xsl:with-param>
			<xsl:with-param name="lab_type_lst">课程类型</xsl:with-param>
			<xsl:with-param name="lab_select_all">-- 全部 --</xsl:with-param>
			<xsl:with-param name="lab_ats_id_lst">完成状态</xsl:with-param>
			<xsl:with-param name="lab_include_no_record_course">包含无学习记录课程</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_show_in_progress_attendance">显示 <b>进行中</b>活动</xsl:with-param>
			<xsl:with-param name="lab_enollment_date">起始日期</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<!-- content attribute text -->
			<xsl:with-param name="lab_content">报告栏</xsl:with-param>
			<!-- content attribute text of course -->
			<xsl:with-param name="lab_item_content">课程内容</xsl:with-param>
			<!-- content attribute text of run -->
			<xsl:with-param name="lab_run_content">
				<xsl:value-of select="$lab_const_run"/>内容</xsl:with-param>
			<!-- content attribute text of learner -->
			<xsl:with-param name="lab_user_content">学员资料</xsl:with-param>
			<!-- content attribute text of others -->
			<xsl:with-param name="lab_app_ext_4">培训合同</xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">录取日期</xsl:with-param>
			<xsl:with-param name="lab_att_date">完成日期</xsl:with-param>
			<xsl:with-param name="lab_att_rate">
				<xsl:value-of select="$lab_const_session"/>到课率</xsl:with-param>
			<xsl:with-param name="lab_item_page">每页分组数</xsl:with-param>
			<xsl:with-param name="lab_att_remark">考勤備註</xsl:with-param>
			<xsl:with-param name="lab_att_status">完成状态</xsl:with-param>
			<xsl:with-param name="lab_sort_order">排序</xsl:with-param>
			<xsl:with-param name="lab_cov_commence_datetime">首次访问</xsl:with-param>
			<xsl:with-param name="lab_cov_last_acc_datetime">上次访问</xsl:with-param>
			<xsl:with-param name="lab_cov_score">成绩</xsl:with-param>
			<xsl:with-param name="lab_cov_total_time">学习时长</xsl:with-param>
			<xsl:with-param name="lab_others_content">其他</xsl:with-param>
			<xsl:with-param name="lab_total_attempt">尝试</xsl:with-param>
			<xsl:with-param name="lab_completion_all">全部</xsl:with-param>
			<!-- 排序的顺序-->
			<xsl:with-param name="lab_sort_ordera">顺序</xsl:with-param>
			<xsl:with-param name="lab_sort_orderd">逆序</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_view_rpt">查看</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">保存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">删除</xsl:with-param>
			<xsl:with-param name="lab_columns_info">每个课程分组显示以下信息</xsl:with-param>
			<xsl:with-param name="lab_show_course_only">只显示课程统计摘要</xsl:with-param>
			<xsl:with-param name="lab_show_course_all">显示课程统计摘要以及包含以下信息的学习记录</xsl:with-param>
			<!-- unused? -->
			<xsl:with-param name="lab_attendance">考勤</xsl:with-param>
			<xsl:with-param name="lab_run_num">
				<xsl:value-of select="$lab_const_run"/>数目</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">我负责的学员</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">我负责的课程</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">报读我负责课程的学员</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">我负责学员报读的课程</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="report_body">
			<!-- header text -->
			<xsl:with-param name="lab_wizard">Edit report template</xsl:with-param>
			<xsl:with-param name="lab_lrn_report">Management report</xsl:with-param>
			<xsl:with-param name="lab_instruction">Build your custom report by selecting specific criteria and report data. Click <b>Run</b> to view the report. Click <b>Save</b> to save as report template.</xsl:with-param>
			<xsl:with-param name="lab_edit_instruction">Modify the report template and click <b>Run</b> to view the report. Click <b>OK</b> to save any changes in the template. </xsl:with-param>
			<xsl:with-param name="lab_standard_rpt_inst">Specify the report criteria and click <b>Run</b> to view the report</xsl:with-param>
			<xsl:with-param name="lab_instruct">Report criteria</xsl:with-param>
			<!-- form text -->
			<xsl:with-param name="lab_rpt_name">Template name</xsl:with-param>
			<xsl:with-param name="lab_rpt_name_length">(Not more than 80 characters.)</xsl:with-param>
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn_group">Learner</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">All learners</xsl:with-param>
			<xsl:with-param name="lab_by_selected_user">Only these learners</xsl:with-param>
			<xsl:with-param name="lab_by_selected_usg">Only learners in these groups</xsl:with-param>
			<xsl:with-param name="lab_all_courses">All courses</xsl:with-param>
			<xsl:with-param name="lab_itm_catalog">*Course catalog</xsl:with-param>
			<xsl:with-param name="lab_itm_title">Course title</xsl:with-param>
			<xsl:with-param name="lab_itm">Course</xsl:with-param>
			<xsl:with-param name="lab_by_catalog">Only courses in these catalogs</xsl:with-param>
			<xsl:with-param name="lab_by_itm">Only these courses</xsl:with-param>
			<xsl:with-param name="lab_partial">partial</xsl:with-param>
			<xsl:with-param name="lab_exact">Exact match</xsl:with-param>
			<xsl:with-param name="lab_type_lst">Course type</xsl:with-param>
			<xsl:with-param name="lab_select_all">-- All --</xsl:with-param>
			<xsl:with-param name="lab_ats_id_lst">Learning status</xsl:with-param>
			<xsl:with-param name="lab_include_no_record_course">Include course with no enrollment</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
			<xsl:with-param name="lab_show_in_progress_attendance">Show <b>In progress</b> activities</xsl:with-param>
			<xsl:with-param name="lab_enollment_date">Enrollment date</xsl:with-param>
			<xsl:with-param name="lab_to">to</xsl:with-param>
			<xsl:with-param name="lab_sort_order">Sort order</xsl:with-param>
			<xsl:with-param name="lab_item_page">Groups per page</xsl:with-param>
			<!-- content attribute text -->
			<xsl:with-param name="lab_content">Report columns</xsl:with-param>
			<!-- content attribute text of course -->
			<xsl:with-param name="lab_item_content">Course information</xsl:with-param>
			<!-- content attribute text of run -->
			<xsl:with-param name="lab_run_content">
				<xsl:value-of select="$lab_const_run"/> content</xsl:with-param>
			<!-- content attribute text of learner -->
			<xsl:with-param name="lab_user_content">User information</xsl:with-param>
			<!-- content attribute text of others -->
			<xsl:with-param name="lab_others_content">Activity information</xsl:with-param>
			<xsl:with-param name="lab_att_status">Learning status</xsl:with-param>
			<xsl:with-param name="lab_att_date">Completion date</xsl:with-param>
			<xsl:with-param name="lab_att_remark">Attendance remarks</xsl:with-param>
			<xsl:with-param name="lab_app_ext_4">Application ext 4</xsl:with-param>
			<xsl:with-param name="lab_cov_score">Score</xsl:with-param>
			<xsl:with-param name="lab_att_rate">
				<xsl:value-of select="$lab_const_session"/> Attendance rate</xsl:with-param>
			<xsl:with-param name="lab_cov_last_acc_datetime">Last access</xsl:with-param>
			<xsl:with-param name="lab_cov_commence_datetime">First access</xsl:with-param>
			<xsl:with-param name="lab_total_attempt">Attempts</xsl:with-param>
			<xsl:with-param name="lab_cov_total_time">Time spent</xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">Enrollment date</xsl:with-param>
			<xsl:with-param name="lab_sort_ordera">Ascending order</xsl:with-param>
			<xsl:with-param name="lab_sort_orderd">Descending order</xsl:with-param>
			<xsl:with-param name="lab_completion_all">All</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">Save</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_view_rpt">Run</xsl:with-param>
			<!-- unused? -->
			<xsl:with-param name="lab_attendance">Attendance</xsl:with-param>
			<xsl:with-param name="lab_columns_info">Group by course showing the following information</xsl:with-param>
			<xsl:with-param name="lab_show_course_only">Show course summary statistics only</xsl:with-param>
			<xsl:with-param name="lab_show_course_all">Show course summary statistics and enrollment details with the following information</xsl:with-param>
			<xsl:with-param name="lab_run_num">
				<xsl:value-of select="$lab_const_run"/> number</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">My responsible learners</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">My responsible courses</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">Learners that have enrolled in my responsible courses</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">Courses that have been enrolled by my responsible learners</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ===========================report _body==================================== -->
	<xsl:template match="report_body">
		<!-- header text -->
		<xsl:param name="lab_wizard"/>
		<xsl:param name="lab_lrn_report"/>
		<xsl:param name="lab_instruction"/>
		<xsl:param name="lab_edit_instruction"/>
		<xsl:param name="lab_standard_rpt_inst"/>
		<xsl:param name="lab_instruct"/>
		<!-- form text -->
		<xsl:param name="lab_rpt_name"/>
		<xsl:param name="lab_rpt_name_length"/>
		<!-- search criteria text -->
		<xsl:param name="lab_lrn_group"/>
		<xsl:param name="lab_by_all_user"/>
		<xsl:param name="lab_by_selected_usg"/>
		<xsl:param name="lab_all_courses"/>
		<xsl:param name="lab_by_selected_user"/>
		<xsl:param name="lab_itm"/>
		<xsl:param name="lab_itm_catalog"/>
		<xsl:param name="lab_itm_title"/>
		<xsl:param name="lab_by_catalog"/>
		<xsl:param name="lab_by_itm"/>
		<xsl:param name="lab_partial"/>
		<xsl:param name="lab_exact"/>
		<xsl:param name="lab_type_lst"/>
		<xsl:param name="lab_select_all"/>
		<xsl:param name="lab_ats_id_lst"/>
		<xsl:param name="lab_include_no_record_course"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_show_in_progress_attendance"/>
		<xsl:param name="lab_enollment_date"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_sort_order"/>
		<xsl:param name="lab_item_page"/>
		<xsl:param name="lab_sort_ordera"/>
		<xsl:param name="lab_sort_orderd"/>
		<xsl:param name="lab_completion_all"/>
		<!-- content attribute text -->
		<xsl:param name="lab_content"/>
		<!-- content attribute text of course -->
		<xsl:param name="lab_item_content"/>
		<!-- content attribute text of run -->
		<xsl:param name="lab_run_content"/>
		<!-- content attribute text of learner -->
		<xsl:param name="lab_user_content"/>
		<!-- content attribute text of others -->
		<xsl:param name="lab_others_content"/>
		<xsl:param name="lab_att_status"/>
		<xsl:param name="lab_att_date"/>
		<xsl:param name="lab_att_remark"/>
		<xsl:param name="lab_app_ext_4"/>
		<xsl:param name="lab_cov_score"/>
		<xsl:param name="lab_att_rate"/>
		<xsl:param name="lab_cov_last_acc_datetime"/>
		<xsl:param name="lab_cov_commence_datetime"/>
		<xsl:param name="lab_total_attempt"/>
		<xsl:param name="lab_cov_total_time"/>
		<!--<xsl:param name="lab_cost_center"/>-->
		<xsl:param name="lab_att_create_timestamp"/>
		<!-- button text -->
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_remove"/>
		<xsl:param name="lab_g_form_btn_save"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_view_rpt"/>
		<!-- unused? -->
		<xsl:param name="lab_attendance"/>
		<xsl:param name="lab_run_num"/>
		<xsl:param name="lab_columns_info"/>
		<xsl:param name="lab_show_course_only"/>
		<xsl:param name="lab_show_course_all"/>

		<xsl:param name="lab_answer_for_lrn"/>
		<xsl:param name="lab_answer_for_course"/>
		<xsl:param name="lab_answer_for_course_lrn"/>
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
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$spec_id!='0' and $spec_ent_id!='0'">
						<xsl:copy-of select="$lab_edit_instruction"/>
					</xsl:when>
					<xsl:when test="$spec_ent_id = '0'  and $spec_id!='0'">
						<xsl:copy-of select="$lab_standard_rpt_inst"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy-of select="$lab_instruction"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
			<xsl:apply-templates select="spec">
				<xsl:with-param name="lab_rpt_name" select="$lab_rpt_name"/>
				<xsl:with-param name="lab_rpt_name_length" select="$lab_rpt_name_length"/>
				<xsl:with-param name="lab_itm" select="$lab_itm"/>
				<xsl:with-param name="lab_itm_catalog" select="$lab_itm_catalog"/>
				<xsl:with-param name="lab_itm_title" select="$lab_itm_title"/>
				<xsl:with-param name="lab_by_catalog" select="$lab_by_catalog"/>
				<xsl:with-param name="lab_by_itm" select="$lab_by_itm"/>
				<xsl:with-param name="lab_partial" select="$lab_partial"/>
				<xsl:with-param name="lab_exact" select="$lab_exact"/>
				<xsl:with-param name="lab_select_all" select="$lab_select_all"/>
				<xsl:with-param name="lab_enollment_date" select="$lab_enollment_date"/>
				<xsl:with-param name="lab_lrn_group" select="$lab_lrn_group"/>
				<xsl:with-param name="lab_by_all_user" select="$lab_by_all_user"/>
				<xsl:with-param name="lab_by_selected_usg" select="$lab_by_selected_usg"/>
				<xsl:with-param name="lab_by_selected_user" select="$lab_by_selected_user"/>
				<xsl:with-param name="lab_to" select="$lab_to"/>
				<xsl:with-param name="lab_ats_id_lst" select="$lab_ats_id_lst"/>
				<xsl:with-param name="lab_include_no_record_course" select="$lab_include_no_record_course"/>
				<xsl:with-param name="lab_yes" select="$lab_yes"/>
				<xsl:with-param name="lab_no" select="$lab_no"/>
				<xsl:with-param name="lab_show_in_progress_attendance" select="$lab_show_in_progress_attendance"/>
				<xsl:with-param name="lab_all_courses" select="$lab_all_courses"/>
				<xsl:with-param name="lab_completion_all" select="$lab_completion_all"/>
				<xsl:with-param name="lab_answer_for_lrn" select="$lab_answer_for_lrn"/>
				<xsl:with-param name="lab_answer_for_course" select="$lab_answer_for_course"/>
				<xsl:with-param name="lab_answer_for_course_lrn" select="$lab_answer_for_course_lrn"/>
				<xsl:with-param name="lab_answer_for_lrn_course" select="$lab_answer_for_lrn_course"/>
			</xsl:apply-templates>
			
			<!--sort para-->
			<xsl:apply-templates select="spec" mode="sort">
				<xsl:with-param name="lab_rpt_name" select="$lab_rpt_name"/>
				<xsl:with-param name="lab_rpt_name_length" select="$lab_rpt_name_length"/>
				<xsl:with-param name="lab_itm" select="$lab_itm"/>
				<xsl:with-param name="lab_itm_catalog" select="$lab_itm_catalog"/>
				<xsl:with-param name="lab_itm_title" select="$lab_itm_title"/>
				<xsl:with-param name="lab_by_catalog" select="$lab_by_catalog"/>
				<xsl:with-param name="lab_by_itm" select="$lab_by_itm"/>
				<xsl:with-param name="lab_partial" select="$lab_partial"/>
				<xsl:with-param name="lab_exact" select="$lab_exact"/>
				<xsl:with-param name="lab_select_all" select="$lab_select_all"/>
				<xsl:with-param name="lab_enollment_date" select="$lab_enollment_date"/>
				<xsl:with-param name="lab_lrn_group" select="$lab_lrn_group"/>
				<xsl:with-param name="lab_by_all_user" select="$lab_by_all_user"/>
				<xsl:with-param name="lab_by_selected_usg" select="$lab_by_selected_usg"/>
				<xsl:with-param name="lab_by_selected_user" select="$lab_by_selected_user"/>
				<xsl:with-param name="lab_to" select="$lab_to"/>
				<xsl:with-param name="lab_ats_id_lst" select="$lab_ats_id_lst"/>
				<xsl:with-param name="lab_include_no_record_course" select="$lab_include_no_record_course"/>
				<xsl:with-param name="lab_yes" select="$lab_yes"/>
				<xsl:with-param name="lab_no" select="$lab_no"/>
				<xsl:with-param name="lab_show_in_progress_attendance" select="$lab_show_in_progress_attendance"/>
				<xsl:with-param name="lab_all_courses" select="$lab_all_courses"/>
				<xsl:with-param name="lab_sort_order" select="$lab_sort_order"/>
				<xsl:with-param name="lab_item_page" select="$lab_item_page"/>
				<xsl:with-param name="lab_att_date" select="$lab_att_date"/>
				<xsl:with-param name="lab_att_create_timestamp" select="$lab_att_create_timestamp"/>
				<xsl:with-param name="lab_sort_ordera" select="$lab_sort_ordera"/>
				<xsl:with-param name="lab_sort_orderd" select="$lab_sort_orderd"/>
				
			</xsl:apply-templates>
			<tr>
				<td width="20%" align="right" class="wzb-form-label">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td width="80%" class="wzb-ui-module-text">
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
					<td height="19" align="center">
						<!-- view button -->
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_g_form_btn_view_rpt"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">Javascript:current++;mgt_rpt.get_rpt_adv(frmXml,'<xsl:value-of select="$rpt_type"/>','<xsl:value-of select="$template_id"/>','<xsl:value-of select="$report_body/template/xsl_list/xsl[@type='execute']/."/>','false',current,"<xsl:value-of select="$wb_lang"/>")</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
						</xsl:call-template>
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						<!-- insert/update button -->
						<xsl:choose>
							<xsl:when test="$spec_id = '0'">
								<!-- insert button -->
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_g_form_btn_save"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.ins_rpt_prep_popup(document.frmXml,'<xsl:value-of select="$template_id"/>','<xsl:value-of select="$ent_id"/>','<xsl:value-of select="$rpt_type"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
								</xsl:call-template>
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test="not($spec_ent_id = '0')">
									<!-- update button -->
									<xsl:call-template name="wb_gen_form_button">
										<xsl:with-param name="wb_gen_btn_name">
											<xsl:value-of select="$lab_g_form_btn_ok"/>
										</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.upd_rpt_exec(frmXml,'<xsl:value-of select="$template_id"/>','<xsl:value-of select="$spec_id"/>','<xsl:value-of select="$ent_id"/>','<xsl:value-of select="$rpt_type"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
									</xsl:call-template>
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
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
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- =============================spec================================== -->
	<xsl:template match="spec">
		<xsl:param name="lab_rpt_name"/>
		<xsl:param name="lab_rpt_name_length"/>
		<xsl:param name="lab_itm"/>
		<xsl:param name="lab_itm_catalog"/>
		<xsl:param name="lab_itm_title"/>
		<xsl:param name="lab_by_catalog"/>
		<xsl:param name="lab_by_itm"/>
		<xsl:param name="lab_partial"/>
		<xsl:param name="lab_exact"/>
		<xsl:param name="lab_ats_id_lst"/>
		<xsl:param name="lab_include_no_record_course"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_show_in_progress_attendance"/>
		<xsl:param name="lab_select_all"/>
		<xsl:param name="lab_enollment_date"/>
		<xsl:param name="lab_lrn_group"/>
		<xsl:param name="lab_by_all_user"/>
		<xsl:param name="lab_by_selected_user"/>
		<xsl:param name="lab_by_selected_usg"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_all_courses"/>
		<xsl:param name="lab_completion_all"/>
		<xsl:param name="lab_answer_for_lrn"/>
		<xsl:param name="lab_answer_for_course"/>
		<xsl:param name="lab_answer_for_course_lrn"/>
		<xsl:param name="lab_answer_for_lrn_course"/>
		<!--== Spec Name ==-->
		<xsl:if test="title != '' and $spec_ent_id != '0'">
			<tr>
				<td width="20%" align="right" valign="top" class="wzb-form-label">
					<span class="TitleText">
					   <xsl:text>*</xsl:text>
						<xsl:value-of select="$lab_rpt_name"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td width="80%" class="wzb-form-control">
					<span class="Text">
						<input type="text" name="rsp_title" value="{title}" size="30" style="width:350px;" class="wzb-inputText" maxlength="200"/><br/>
                   <xsl:value-of select="$lab_rpt_name_length"/>
					</span>
				</td>
			</tr>
		</xsl:if>
		<!--== Catalog ==-->
		<tr>
			<td width="20%" align="right" valign="top" class="wzb-form-label">
				<span class="wzb-form-star">*</span>
				<span class="TitleText">
					<xsl:value-of select="$lab_itm"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="80%" class="wzb-form-control">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td valign="top" >
							<span>
								<input type="radio" name="course_sel_type" value="2" id="rdo_sel_by_all_course_title" onclick="course_change(document.frmXml,this);changelrn_cos_ContentStatus(false,'cos')">
									<xsl:if test="$report_body/spec/data_list/data[@name='itm_id'] and $report_body/spec/data_list/data[@name='itm_id']/@value = '' or ( not($report_body/spec/data_list/data[@name='itm_id']) and not($report_body/spec/data_list/data[@name='tnd_id']))">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
							</span>
							<label for="rdo_sel_by_all_course_title"  onclick="changelrn_cos_ContentStatus(false,'cos')">
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
								<span style="padding:0 0px 0 20px;">
								<input type="checkbox" name="cos_content" id="chk_answer_for_course">
									<xsl:if test="$report_body/spec/data_list/data[@name='answer_for_lrn' and @value='1'] or not(/report/report_body/spec/data_list) ">
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
								<span style="padding:0 0px 0 20px;">
								<input type="checkbox" name="cos_content" id="chk_answer_for_lrn_course">
									<xsl:if test="$report_body/spec/data_list/data[@name='answer_for_course_lrn' and @value='1'] or not(/report/report_body/spec/data_list) ">
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
								<input type="radio" name="course_sel_type" value="0" id="rdo_sel_by_course_title" onclick="course_change(document.frmXml,this);changelrn_cos_ContentStatus(true,'cos')">
									<xsl:if test="$report_body/spec/data_list/data[@name='itm_id'] and $report_body/spec/data_list/data[@name='itm_id']/@value != ''">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
							</span>
							<label for="rdo_sel_by_course_title" onclick="changelrn_cos_ContentStatus(true,'cos')">
								<span class="Text">
									<xsl:value-of select="$lab_by_itm"/>
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
								<xsl:with-param name="custom_js_code">course_change(document.frmXml,document.frmXml.course_sel_type[1]);document.frmXml.course_sel_type[1].checked = true;</xsl:with-param>
								<xsl:with-param name="name">itm_id_lst</xsl:with-param>
								<xsl:with-param name="box_size">4</xsl:with-param>
								<xsl:with-param name="tree_type"><xsl:value-of select="$tree_type"/></xsl:with-param>
								<xsl:with-param name="select_type">3</xsl:with-param>
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
					<tr>
						<td valign="top">
							<span>
								<input type="radio" name="course_sel_type" value="1" id="rdo_sel_by_type_or_cata" onclick="course_change(document.frmXml,this);changelrn_cos_ContentStatus(true,'cos')">
									<xsl:if test="$report_body/spec/data_list/data[@name='tnd_id']">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
							</span>
							<label for="rdo_sel_by_type_or_cata" onclick="changelrn_cos_ContentStatus(true,'cos')">
								<span class="Text">
									<xsl:value-of select="$lab_by_catalog"/>
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
								<xsl:with-param name="custom_js_code">course_change(document.frmXml,document.frmXml.course_sel_type[2]);document.frmXml.course_sel_type[2].checked = true;</xsl:with-param>
								<xsl:with-param name="name">tnd_id</xsl:with-param>
								<xsl:with-param name="box_size">4</xsl:with-param>
								<xsl:with-param name="tree_type"><xsl:value-of select="$tree_type"/></xsl:with-param>
								<xsl:with-param name="args_type">row</xsl:with-param>
								<xsl:with-param name="complusory_tree">0</xsl:with-param>
								<xsl:with-param name="select_type">1</xsl:with-param>
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
				</table>
			</td>
		</tr>
		<tr>
			<td align="right" width="20%" class="wzb-form-label">
				<span class="TitleText">
					<span class="wzb-form-star">*</span>
					<xsl:value-of select="$lab_include_no_record_course" />
				</span>
				<xsl:text>：</xsl:text>
			</td>
			<td width="80%" class="wzb-form-control">
				<input type="radio" name="include_no_record" value="true" id="show_empty_cos1" style="margin-left:3px;">
				<xsl:if test="/report/report_body/spec/data_list/data[@name='include_no_record' and @value='true'] or not(/report/report_body/spec/data_list/data[@name='include_no_record'])">
					<xsl:attribute name="checked">checked</xsl:attribute>
				</xsl:if>
					<label for="show_empty_cos1"><xsl:value-of select="$lab_yes"/></label>
				</input>
				<input type="radio" name="include_no_record" value="false" id="not_show_empty_cos" style="margin-left:15px;">
				<xsl:if test="/report/report_body/spec/data_list/data[@name='include_no_record' and @value='false']">
					<xsl:attribute name="checked">checked</xsl:attribute>
				</xsl:if>
					<label for="not_show_empty_cos"><xsl:value-of select="$lab_no"/></label>
				</input>
			</td>
		</tr>
		<!--== Learner Group ==-->
		<tr>
			<td width="20%" align="right" valign="top" class="wzb-form-label">
				<span class="wzb-form-star">*</span>
				<span class="TitleText">
					<xsl:value-of select="$lab_lrn_group"/>
					<xsl:text>：</xsl:text>
				</span>
				<input type="hidden" name="lab_group" value="{$lab_group}"/>
			</td>
			<td width="80%" class="wzb-form-control">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td valign="top">
							<span>
							<input type="radio" name="usr_sel_all_user" value="2" id="rdo_sel_by_all_user" onclick="usr_change(document.frmXml,this);changelrn_cos_ContentStatus(false,'lrn')">
								<xsl:if test="$report_body/spec/data_list/data[@name='all_user_ind']/@value = '1' or not($report_body/spec/data_list/data[@name='all_user_ind']/@value)">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
							</input>
							</span>
							<label for="rdo_sel_by_all_user"  onclick="changelrn_cos_ContentStatus(false,'lrn')">
								<span class="Text">
									<xsl:value-of select="$lab_by_all_user"/>
								</span>
							</label>
						</td>
					</tr>
					<xsl:if test="$tc_enabled = 'true'">
						<!--我负责的学员-->
						<tr>
							<td>
								<span style="padding:0 0px 0 20px;">
									<input type="checkbox" name="lrn_content" id="chk_answer_for_lrn">
										<xsl:if test="$report_body/spec/data_list/data[@name='answer_for_course' and @value='1'] or not(/report/report_body/spec/data_list) ">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
								</span>
								<label for="chk_answer_for_lrn">
									<span class="Text">
										<xsl:value-of select="$lab_answer_for_lrn"/>
									</span>
								</label>
							</td>
						</tr>
						<!--我负责的课程的目标学员-->
						<tr>
							<td>
								<span style="padding:0 0px 0 20px;">
									<input type="checkbox" name="lrn_content" id="chk_answer_for_course_lrn">
										<xsl:if test="$report_body/spec/data_list/data[@name='answer_for_lrn_course' and @value='1'] or not(/report/report_body/spec/data_list) ">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
								</span>
								<label for="chk_answer_for_course_lrn">
									<span class="Text">
										<xsl:value-of select="$lab_answer_for_course_lrn"/>
									</span>
								</label>
							</td>
						</tr>
					</xsl:if>
					<tr>
						<td valign="top">
							<span>
								<input type="radio" name="usr_sel_all_user" value="0" id="rdo_sel_by_selected_user" onclick="usr_change(document.frmXml,this);changelrn_cos_ContentStatus(true,'lrn')">
									<xsl:if test="not($report_body/spec/data_list/data[@name='all_user_ind']/@value = '1') and ($report_body/spec/data_list/data[@name='all_user_ind']/@value) and not ($report_body/spec/data_list/data[@name='usg_ent_id']/@value)">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
							</span>
							<label for="rdo_sel_by_selected_user"  onclick="changelrn_cos_ContentStatus(true,'lrn')">
								<span class="Text">
									<xsl:value-of select="$lab_by_selected_user"/>
									<xsl:text>：</xsl:text>
								</span>
							</label>
						</td>
					</tr>
					<tr>
						<td>
							<xsl:call-template name="wb_goldenman">
								<xsl:with-param name="field_name">usr_ent_id_lst</xsl:with-param>
								<xsl:with-param name="name">usr_ent_id_lst</xsl:with-param>
								<xsl:with-param name="box_size">4</xsl:with-param>
								<xsl:with-param name="tree_type">user_group_and_user</xsl:with-param>
								<xsl:with-param name="select_type">4</xsl:with-param>
								<xsl:with-param name="pick_leave">0</xsl:with-param>
								<xsl:with-param name="pick_root">0</xsl:with-param>
								<xsl:with-param name="label_add_btn">
									<xsl:value-of select="$lab_gen_select"/>
								</xsl:with-param>
								<xsl:with-param name="option_list">
									<xsl:apply-templates select="$data_list/data[@name='usr_ent_id']"/>
								</xsl:with-param>
								<xsl:with-param name="search">true</xsl:with-param>
								<xsl:with-param name="search_function">javascript:usr.search.popup_search_prep('usr_ent_id_lst','','<xsl:value-of select="$root_ent_id"/>', '0', '', '', '0', '0')</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
					<tr>
						<td valign="top">
							<span>
								<input type="radio" name="usr_sel_all_user" value="1" id="rdo_sel_by_selected_usg" onclick="usr_change(document.frmXml,this);changelrn_cos_ContentStatus(true,'lrn')">
									<xsl:if test="not($report_body/spec/data_list/data[@name='all_user_ind']/@value = '1') and ($report_body/spec/data_list/data[@name='all_user_ind']/@value) and not ($report_body/spec/data_list/data[@name='usr_ent_id']/@value)">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
							</span>
							<label for="rdo_sel_by_selected_usg" onclick="changelrn_cos_ContentStatus(true,'lrn')">
								<span class="Text">
									<xsl:value-of select="$lab_by_selected_usg"/>
									<xsl:text>：</xsl:text>
								</span>
							</label>
						</td>
					</tr>
					<tr>
						<td>
							<xsl:call-template name="wb_goldenman">
								<xsl:with-param name="field_name">usg_ent_id_lst</xsl:with-param>
								<xsl:with-param name="name">usg_ent_id_lst</xsl:with-param>
								<xsl:with-param name="box_size">4</xsl:with-param>
								<xsl:with-param name="tree_type">user_group</xsl:with-param>
								<xsl:with-param name="select_type">1</xsl:with-param>
								<xsl:with-param name="pick_leave">0</xsl:with-param>
								<xsl:with-param name="pick_root">0</xsl:with-param>
								<xsl:with-param name="label_add_btn">
									<xsl:value-of select="$lab_gen_select"/>
								</xsl:with-param>
								<xsl:with-param name="option_list">
									<xsl:apply-templates select="$data_list/data[@name='usg_ent_id']"/>
								</xsl:with-param>
								<!--<xsl:with-param name="search">true</xsl:with-param>
								<xsl:with-param name="search_function">javascript:usr.search.popup_search_prep('usg_ent_id_lst','','<xsl:value-of select="$root_ent_id"/>', '0', '', '', '0', '0')</xsl:with-param>-->
							</xsl:call-template>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		
		<!--== Period ==-->
		<tr>
			<td width="20%" align="right" class="wzb-form-label">
				<span class="TitleText">
					<xsl:value-of select="$lab_enollment_date"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="80%" class="wzb-form-control">
				<span class="Text">
					<xsl:value-of select="$lab_const_from"/>
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
		<!-- completion_status-->
		<tr>
			<td width="20%" align="right" class="wzb-form-label">
					<span class="wzb-form-star">*</span>
					<span class="TitleText">
						<xsl:value-of select="$lab_ats_id_lst"/>
						<xsl:text>：</xsl:text>
					</span>
			</td>
			<td width="80%" class="wzb-form-control">
				<label for="ats_id_all">
					<input type="radio" name="ats_id" value="0" id="ats_id_all">
						<xsl:if test="$report_body/spec/data_list/data[@name='ats_id' and @value='0'] or not(/report/report_body/spec/data_list) ">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_completion_all"/>
					</input>
				</label>
				<xsl:variable name="choice" select="/report/report_body/spec/data_list/data[@name='ats_id']/@value"/>
				<xsl:for-each select="/report/report_body/meta/attendance_status_list/status">
				  <xsl:text>&#160;</xsl:text>
					<xsl:variable name="cs" select="@id"/>
					<label for="ats_id_{@id}" style="margin-left:15px;">
						<input type="radio" name="ats_id" value="{$cs}" id="ats_id_{@id}">
							<xsl:if test="$cs=$choice">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
							<xsl:call-template name="get_ats_title">
								<xsl:with-param name="ats_id" select="@id"/>
							</xsl:call-template>
						</input>
					</label>
					
				</xsl:for-each>
			</td>
		</tr>
	</xsl:template>
	<!-- ===========================sort order section ==============================================-->
	<xsl:template match="spec" mode="sort">
		<xsl:param name="lab_sort_order"/>
		<xsl:param name="lab_item_page"/>
		<xsl:param name="lab_rpt_name"/>
		<xsl:param name="lab_att_date"/>
		<xsl:param name="lab_att_create_timestamp"/>
		<xsl:param name="lab_sort_ordera"/>
		<xsl:param name="lab_sort_orderd"/>
		<xsl:variable name="sort_value" select="data_list/data[@name='sort_col']/@value"/>
		<xsl:variable name="index" select="data_list/data[@name='sort_order']/@value"/>
		<xsl:variable name="itemno" select="data_list/data[@name='page_size']/@value"/>
		<tr>
			<td width="20%" align="right" class="wzb-form-label">
					<span class="TitleText">
						<span class="wzb-form-star">*</span>
						<xsl:value-of select="$lab_sort_order"/>
						<xsl:text>：</xsl:text>
					</span>
			</td>
			<td width="20%" class="wzb-form-control">
				<!-- 动态提取排序的字段，若用户选择的排序方法没有在checkbox中选择则提示-->
				<select name="sort_col" class="select">
					<!-- user中的字段-->
					
					<!-- item中的字段-->
					<xsl:apply-templates select="/report/report_body/display_option/item/template_view/section">
						<xsl:with-param name="choicesort" select="$sort_value"/>
					</xsl:apply-templates>
					
				</select>
				<!-- ============DENNIS =========对sort order每个选项加隐藏域==================================-->
				<xsl:for-each select="/report/report_body/display_option/user/object_view/attribute[@sortable='yes']">
					<xsl:variable name="name1" select="@paramname"/>
					<xsl:variable name="name2" select="text()"/>
					<input type="hidden">
						<xsl:attribute name="name"><xsl:choose><xsl:when test="@paramname"><xsl:value-of select="concat($name1,'_display')"/></xsl:when><xsl:otherwise><xsl:value-of select="concat($name2,'_display')"/></xsl:otherwise></xsl:choose></xsl:attribute>
						<xsl:attribute name="value"><xsl:choose><xsl:when test=". = 'usr_id'"><xsl:value-of select="$lab_login_id"/></xsl:when><xsl:when test=". = 'usr_display_bil'"><xsl:value-of select="$lab_dis_name"/></xsl:when></xsl:choose></xsl:attribute>
					</input>
					<!--=====另一个hidden域======-->
					<xsl:choose>
						<xsl:when test="@paramname">
							<input type="hidden" name="{@paramname}_fieldname" value="{text()}"/>
						</xsl:when>
						<xsl:otherwise>
							<input type="hidden" name="{text()}_fieldname" value="{text()}"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
				<!-- item中的字段-->
				<xsl:for-each select="/report/report_body/display_option/item/template_view/section/*[@sortable='yes']">
					<input type="hidden" name="{@paramname}_display" value="{title/desc[@lan=$wb_lang_encoding]/@name}"/>
					<input type="hidden" name="{@paramname}_fieldname" value="{name()}"/>
				</xsl:for-each>
				<!-- ================================DENNIS END============================================= -->
				<select name="sort_order" class="select">
					<option value="asc">
						<xsl:if test="$index = 'asc'">
							<xsl:attribute name="selected">selected</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_sort_ordera"/>
					</option>
					<option value="desc">
						<xsl:if test="$index = 'desc'">
							<xsl:attribute name="selected">selected</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_sort_orderd"/>
					</option>
				</select>
			</td>
		</tr>
	</xsl:template>
	<!-- =======================================item===================================================== -->
	<!--处理/report/report_body/display_option/item-->
	<xsl:template match="section ">
		<xsl:param name="choicesort"/>
		<!--sort_col里写入paramname属性的值-->
		<xsl:for-each select="*[@sortable='yes']">
			<!--<xsl:if test="self :: node()[@sortable='yes']">-->
				<option>
					<xsl:if test="@paramname">
						<xsl:attribute name="value"><xsl:value-of select="@paramname"/></xsl:attribute>
						<xsl:if test="$choicesort=@paramname">
							<xsl:attribute name="selected">selected</xsl:attribute>
						</xsl:if>
					</xsl:if>
					<xsl:value-of select="./title/desc[@lan=$wb_lang_encoding]/@name"/>
				</option>
			<!--</xsl:if>-->
		</xsl:for-each>
	</xsl:template>
	<!-- Run -->
	<xsl:template match="run" mode="content">
		<xsl:param name="lab_run_content"/>
		<table cellpadding="0" cellspacing="0" width="100%" border="0">
			<tr>
				<td colspan="3">
					<span class="Text">
						<xsl:value-of select="$lab_run_content"/>:</span>
				</td>
			</tr>
			<xsl:call-template name="template_view">
				<xsl:with-param name="type">run</xsl:with-param>
			</xsl:call-template>
		</table>
		<br/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="template_view">
		<xsl:param name="type"/>
		<tr>
			<xsl:for-each select="template_view/section/*">
				<td valign="top">
					<xsl:attribute name="width"><xsl:choose><xsl:when test="position() mod 3 = 0">34%</xsl:when><xsl:otherwise>33%</xsl:otherwise></xsl:choose></xsl:attribute>
					<xsl:if test="position() = last()">
						<xsl:attribute name="colspan"><xsl:value-of select="(3-(position() mod  3))+1"/></xsl:attribute>
					</xsl:if>
					<span class="Text">
						<xsl:choose>
							<xsl:when test="name() = 'item_access' or name() = 'run_item_access' or name() = 'child_item_access' ">
								<xsl:variable name="_role" select="@id"/>
								<xsl:variable name="_my_name">
									<xsl:value-of select="name()"/>_<xsl:value-of select="@id"/>
								</xsl:variable>
								<label for="label_{name()}_{@id}">
									<input type="checkbox" name="{$type}_content" value="{name()}_{@id}" id="label_{name()}_{@id}">
										<xsl:choose>
											<xsl:when test="not($data_list)">
												<xsl:if test="@checked = 'yes'">
													<xsl:attribute name="checked">checked</xsl:attribute>
												</xsl:if>
											</xsl:when>
											<xsl:otherwise>
												<xsl:if test="$data_list/data[@value = $_my_name]">
													<xsl:attribute name="checked">checked</xsl:attribute>
												</xsl:if>
											</xsl:otherwise>
										</xsl:choose>
									</input>
									<img src="{$wb_img_path}tp.gif" width="8" height="1" border="0"/>
									<xsl:call-template name="get_rol_title">
										<xsl:with-param name="rol_ext_id" select="$role_list/role[@id = $_role]/@id"/>
									</xsl:call-template>
								</label>
							</xsl:when>
							<xsl:otherwise>
								<xsl:variable name="_my_name" select="name()"/>
								<xsl:variable name="_my_paramname" select="@paramname"/>
								<label for="label_{name()}">
									<input type="checkbox" name="{$type}_content" id="label_{name()}" value="{name()}">
										<xsl:choose>
											<!--  <xsl:when test="not(@paramname)"><xsl:attribute name="value"><xsl:value-of select="name()" /></xsl:attribute></xsl:when>
		                                       <xsl:otherwise><xsl:attribute name="value"><xsl:value-of select="@paramname" /></xsl:attribute></xsl:otherwise>																											                             </xsl:choose>
										<xsl:choose>-->
											<xsl:when test="not($data_list)">
												<xsl:if test="@checked = 'yes'">
													<xsl:attribute name="checked">checked</xsl:attribute>
												</xsl:if>
											</xsl:when>
											<xsl:otherwise>
												<!--<xsl:if test="not(@paramname)">
												   <xsl:if test="$data_list/data[@value = $_my_name]">
													  <xsl:attribute name="checked">checked</xsl:attribute>
												   </xsl:if>
												 </xsl:if>-->
												<xsl:if test="$data_list/data[@value = $_my_name]">
													<xsl:attribute name="checked">checked</xsl:attribute>
												</xsl:if>
											</xsl:otherwise>
										</xsl:choose>
									</input>
									<img src="{$wb_img_path}tp.gif" width="8" height="1" border="0"/>
									<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
								</label>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
				<xsl:if test="position() mod 3 = 0 and position() != last()">
					<xsl:text disable-output-escaping="yes">&lt;/tr&gt;&lt;tr&gt;</xsl:text>
				</xsl:if>
			</xsl:for-each>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- Others -->
	<xsl:template match="other" mode="content">
		<xsl:param name="lab_others_content"/>
		<xsl:param name="lab_attendance"/>
		<xsl:param name="lab_run_num"/>
		<xsl:param name="lab_att_status"/>
		<xsl:param name="lab_att_date"/>
		<xsl:param name="lab_att_create_timestamp"/>
		<xsl:param name="lab_att_remark"/>
		<xsl:param name="lab_app_ext_4"/>
		<xsl:param name="lab_cov_score"/>
		<xsl:param name="lab_att_rate"/>
		<xsl:param name="lab_cov_last_acc_datetime"/>
		<xsl:param name="lab_cov_commence_datetime"/>
		<xsl:param name="lab_total_attempt"/>
		<xsl:param name="lab_cov_total_time"/>
		<xsl:variable name="usr" select="$data_list/data[@name = 'usr_content_lst']/@value" />
		<xsl:variable name="itm" select="$data_list/data[@name = 'itm_content_lst']/@value" />
		<xsl:variable name="other" select="$data_list/data[@name = 'content_lst']/@value" />
		<table cellpadding="0" cellspacing="0" width="100%" border="0">
			<tr>
				<td colspan="3">
					<span class="Text">
						<xsl:value-of select="$lab_others_content"/>:</span>
				</td>
			</tr>
			<tr>
				<xsl:for-each select="object_view/attribute">
					<td>
						<xsl:attribute name="width"><xsl:choose><xsl:when test="position() mod 3 = 0">34%</xsl:when><xsl:when test="position() = last()"><xsl:value-of select="(((3-(position() mod  3))+1)*33)+1"/>%</xsl:when><xsl:otherwise>33%</xsl:otherwise></xsl:choose></xsl:attribute>
						<xsl:if test="position() = last()">
							<xsl:attribute name="colspan"><xsl:value-of select="(3-(position() mod  3))+1"/></xsl:attribute>
						</xsl:if>
						<xsl:variable name="_my_name" select="."/>
						<span class="Text">
							<label for="label_{.}">
								<input type="checkbox" name="content" value="{.}" id="label_{.}">
									<xsl:choose>
										<xsl:when test="not($data_list)">
											<xsl:if test="@checked = 'yes'">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</xsl:when>
										<xsl:otherwise>
											<xsl:if test="$usr = $_my_name or $itm = $_my_name or 
										                              $other= $_my_name">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</xsl:otherwise>
									</xsl:choose>
								</input>
								<img src="{$wb_img_path}tp.gif" width="8" height="1" border="0"/>
								<xsl:choose>
									<xsl:when test=". = 'attendance'">
										<xsl:value-of select="$lab_attendance"/>
									</xsl:when>
									<xsl:when test=". = 'run_num'">
										<xsl:value-of select="$lab_run_num"/>
									</xsl:when>
									<xsl:when test=". = 'att_status'">
										<xsl:value-of select="$lab_att_status"/>
									</xsl:when>
									<xsl:when test=". = 'att_timestamp'">
										<xsl:value-of select="$lab_att_date"/>
									</xsl:when>
									<xsl:when test=". = 'att_create_timestamp'">
										<xsl:value-of select="$lab_att_create_timestamp"/>
									</xsl:when>
									<xsl:when test=". = 'att_remark'">
										<xsl:value-of select="$lab_att_remark"/>
									</xsl:when>
									<xsl:when test=". = 'app_ext4'">
										<xsl:value-of select="$lab_app_ext_4"/>
									</xsl:when>
									<xsl:when test=". = 'cov_score'">
										<xsl:value-of select="$lab_cov_score"/>
									</xsl:when>
									<xsl:when test=". = 'att_rate'">
										<xsl:value-of select="$lab_att_rate"/>
									</xsl:when>
									<xsl:when test=". = 'cov_last_acc_datetime'">
										<xsl:value-of select="$lab_cov_last_acc_datetime"/>
									</xsl:when>
									<xsl:when test=". = 'cov_commence_datetime'">
										<xsl:value-of select="$lab_cov_commence_datetime"/>
									</xsl:when>
									<xsl:when test=". = 'total_attempt'">
										<xsl:value-of select="$lab_total_attempt"/>
									</xsl:when>
									<xsl:when test=". = 'cov_total_time'">
										<xsl:value-of select="$lab_cov_total_time"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="."/>
									</xsl:otherwise>
								</xsl:choose>
							</label>
						</span>
					</td>
					<xsl:if test="position() mod 3 = 0 and position() != last()">
						<xsl:text disable-output-escaping="yes">&lt;/tr&gt;&lt;tr&gt;</xsl:text>
					</xsl:if>
				</xsl:for-each>
			</tr>
		</table>
		<br/>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- User -->
	<xsl:template match="user" mode="content">
		<xsl:param name="lab_user_content"/>
		<table cellpadding="0" cellspacing="0" width="100%" border="0">
			<tr>
				<td colspan="3">
					<span class="Text">
						<xsl:value-of select="$lab_user_content"/>:</span>
				</td>
			</tr>
			<tr>
				<xsl:for-each select="object_view/attribute">
					<td>
						<xsl:attribute name="width"><xsl:choose><xsl:when test="position() mod 3 = 0">34%</xsl:when><xsl:when test="position() = last()"><xsl:value-of select="(((3-(position() mod  3))+1)*33)+1"/>%</xsl:when><xsl:otherwise>33%</xsl:otherwise></xsl:choose></xsl:attribute>
						<xsl:if test="position() = last()">
							<xsl:attribute name="colspan"><xsl:value-of select="(3-(position() mod  3))+1"/></xsl:attribute>
						</xsl:if>
						<span class="Text">
							<label for="label_{.}">
								<xsl:variable name="_my_name" select="."/>
								<input type="checkbox" name="usr_content" value="{.}" id="label_{.}">
									<xsl:choose>
										<xsl:when test="not($data_list)">
											<xsl:if test="@checked = 'yes'">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</xsl:when>
										<xsl:otherwise>
											<xsl:if test="$data_list/data[@value = $_my_name]">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</xsl:otherwise>
									</xsl:choose>
								</input>
								<img src="{$wb_img_path}tp.gif" width="8" height="1" border="0"/>
								<xsl:choose>
									<xsl:when test=". = 'usr_id'">
										<xsl:value-of select="$lab_login_id"/>
									</xsl:when>
									<xsl:when test=". = 'usr_pwd'">
										<xsl:value-of select="$lab_passwd"/>
									</xsl:when>
									<xsl:when test=". = 'usr_display_bil'">
										<xsl:value-of select="$lab_dis_name"/>
									</xsl:when>
									<xsl:when test=". = 'usr_gender'">
										<xsl:value-of select="$lab_gender"/>
									</xsl:when>
									<xsl:when test=". = 'usr_bday'">
										<xsl:value-of select="$lab_bday"/>
									</xsl:when>
									<xsl:when test=". = 'usr_email'">
										<xsl:value-of select="$lab_e_mail"/>
									</xsl:when>
									<xsl:when test=". = 'usr_tel_1'">
										<xsl:value-of select="$lab_tel_1"/>
									</xsl:when>
									<xsl:when test=". = 'usr_fax_1'">
										<xsl:value-of select="$lab_fax_1"/>
									</xsl:when>
									<xsl:when test=". = 'usr_job_title'">
										<xsl:value-of select="$lab_job_title"/>
									</xsl:when>
									<xsl:when test=". = 'USR_CURRENT_UGR'">
										<xsl:value-of select="$lab_grade"/>
									</xsl:when>
									<xsl:when test=". = 'USR_PARENT_USG'">
										<xsl:value-of select="$lab_group"/>
									</xsl:when>
									<xsl:when test=". = 'direct_supervisor_ent_lst'">
										<xsl:value-of select="$lab_direct_supervisors"/>
									</xsl:when>
									<xsl:when test=". = 'usr_join_date'">
										<xsl:value-of select="$lab_join_date"/>
									</xsl:when>
									<xsl:when test=". = 'usr_role'">
										<xsl:value-of select="$lab_role"/>
									</xsl:when>
									<xsl:when test=". = 'usr_extra_1'">
										<xsl:value-of select="$lab_extra_1"/>
									</xsl:when>
									<xsl:when test=". = 'usr_extra_2'">
										<xsl:value-of select="$lab_extra_2"/>
									</xsl:when>
									<xsl:when test=". = 'usr_extra_3'">
										<xsl:value-of select="$lab_extra_3"/>
									</xsl:when>
									<xsl:when test=". = 'usr_extra_4'">
										<xsl:value-of select="$lab_extra_4"/>
									</xsl:when>
									<xsl:when test=". = 'usr_extra_5'">
										<xsl:value-of select="$lab_extra_5"/>
									</xsl:when>
									<xsl:when test=". = 'usr_extra_6'">
										<xsl:value-of select="$lab_extra_6"/>
									</xsl:when>
									<xsl:when test=". = 'usr_extra_7'">
										<xsl:value-of select="$lab_extra_7"/>
									</xsl:when>
									<xsl:when test=". = 'usr_extra_8'">
										<xsl:value-of select="$lab_extra_8"/>
									</xsl:when>
									<xsl:when test=". = 'usr_extra_9'">
										<xsl:value-of select="$lab_extra_9"/>
									</xsl:when>
									<xsl:when test=". = 'usr_extra_10'">
										<xsl:value-of select="$lab_extra_10"/>
									</xsl:when>
									<xsl:when test=". = 'supervise_target_ent_lst'">
										<xsl:value-of select="$lab_supervised_groups"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="."/>
									</xsl:otherwise>
								</xsl:choose>
							</label>
						</span>
					</td>
					<xsl:if test="position() mod 3 = 0 and position() != last()">
						<xsl:text disable-output-escaping="yes">&lt;/tr&gt;&lt;tr&gt;</xsl:text>
					</xsl:if>
				</xsl:for-each>
			</tr>
		</table>
		<br/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data">
		<option value="{@value}">
			<xsl:variable name="name" select="@name"/>
			<xsl:variable name="value" select="@value"/>
			<xsl:value-of select="/report/report_body/presentation/data[@name=$name and @value = $value]/@display"/>
		</option>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
