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
	<xsl:import href="utils/wb_goldenman_sel_mod.xsl"/>
	<xsl:import href="share/label_rpt.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="tc_enabled" select="/report/meta/tc_enabled"/>
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
			
			function init(){
					frm = document.frmXml
					if(frm.usr_sel_all_user && frm.usr_sel_all_user[0].checked){
						usr_change(frm,frm.usr_sel_all_user[0])
					}else if(frm.usr_sel_all_user && frm.usr_sel_all_user[1].checked){
						usr_change(frm,frm.usr_sel_all_user[1])
					}else{
						usr_change(frm,frm.usr_sel_all_user[2])
					}
					
             changeRptCol()
				}
								function redothis( ){
				   frm=document.frmXml;
				   }
				   
				   
  function changeRptCol() {
		var selectObj = document.frmXml.group_by;
		var groupBy = selectObj[selectObj.selectedIndex].value;
		if (groupBy == 'QUE') {
			document.frmXml.content_lst[0].disabled = false;
			document.frmXml.content_lst[1].disabled = false;
			document.frmXml.content_lst[2].disabled = false;
			document.frmXml.content_lst[9].disabled = true;
			document.frmXml.content_lst[10].disabled = true;
		} else
		if (groupBy == 'RES_FDR' || groupBy == 'QUE_TYPE') {
			document.frmXml.content_lst[0].disabled = true;
			document.frmXml.content_lst[1].disabled = true;
			document.frmXml.content_lst[2].disabled = true;
			document.frmXml.content_lst[9].disabled = false;
			document.frmXml.content_lst[10].disabled = false;
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
			<xsl:with-param name="lab_lrn_group">學員</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">所有學員</xsl:with-param>
			<xsl:with-param name="lab_by_selected_user">只有這些學員</xsl:with-param>
			<xsl:with-param name="lab_by_selected_usg">指定用戶組</xsl:with-param>
			<xsl:with-param name="lab_sub_date">提交日期</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">報名日期</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">儲存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_view_rpt">執行</xsl:with-param>
			<xsl:with-param name="lab_lrn_mod">測驗模塊</xsl:with-param>
			<xsl:with-param name="lab_attemp_type">嘗試類別</xsl:with-param>
			<xsl:with-param name="lab_enollment_date">註冊日期</xsl:with-param>
			<!--    =====================-->
			<xsl:with-param name="lab_group_by">分組統計</xsl:with-param>
			<xsl:with-param name="lab_group_by_Q">題目</xsl:with-param>
			<xsl:with-param name="lab_group_by_RF">資源檔案夾</xsl:with-param>
			<xsl:with-param name="lab_group_by_QT">題目類型</xsl:with-param>
			<xsl:with-param name="lab_res_folder">資源檔案夾</xsl:with-param>
			<xsl:with-param name="lab_qu_type">題目類型</xsl:with-param>
			<xsl:with-param name="lab_dif">難度</xsl:with-param>
			<xsl:with-param name="lab_attempts">嘗試次數</xsl:with-param>
			<xsl:with-param name="lab_correct">答對次數</xsl:with-param>
			<xsl:with-param name="lab_incorrect">答錯次數</xsl:with-param>
			<xsl:with-param name="lab_partial_correct">未完全答對次數</xsl:with-param>
			<xsl:with-param name="lab_N_graded">尚未評分次數</xsl:with-param>
			<xsl:with-param name="lab_avg_score">平均分數 (%)</xsl:with-param>
			<xsl:with-param name="lab_per_att">全部答對次數</xsl:with-param>
			<xsl:with-param name="lab_que">題目數</xsl:with-param>
			<xsl:with-param name="lab_lrn">學員人數</xsl:with-param>
			<xsl:with-param name="lab_content">報告欄</xsl:with-param>
			<xsl:with-param name="lab_num_attempts">個別嘗試</xsl:with-param>
			<xsl:with-param name="lab_all_attempts">所有嘗試</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">我負責的學員</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">報讀我負責課程的學員</xsl:with-param>
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
			<xsl:with-param name="lab_by_selected_user">指定学员</xsl:with-param>
			<xsl:with-param name="lab_by_selected_usg">指定用户组</xsl:with-param>
			<xsl:with-param name="lab_attemp_type">尝试类别</xsl:with-param>
			<xsl:with-param name="lab_enollment_date">录取日期</xsl:with-param>
			
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">录取日期</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_view_rpt">查看</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">保存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">删除</xsl:with-param>
			<xsl:with-param name="lab_lrn_mod">测验模块</xsl:with-param>
			<!--    =====================-->
			<xsl:with-param name="lab_group_by">分组统计</xsl:with-param>
			<xsl:with-param name="lab_group_by_Q">题目</xsl:with-param>
			<xsl:with-param name="lab_group_by_RF">资源文件夹</xsl:with-param>
			<xsl:with-param name="lab_group_by_QT">题目类型</xsl:with-param>
			<xsl:with-param name="lab_res_folder">资源文件夹</xsl:with-param>
			<xsl:with-param name="lab_qu_type">题目类型</xsl:with-param>
			<xsl:with-param name="lab_dif">难度</xsl:with-param>
			<xsl:with-param name="lab_attempts">尝试次数</xsl:with-param>
			<xsl:with-param name="lab_correct">答对次数</xsl:with-param>
			<xsl:with-param name="lab_incorrect">答错次数</xsl:with-param>
			<xsl:with-param name="lab_partial_correct">未完全答对次数</xsl:with-param>
			<xsl:with-param name="lab_N_graded">尚未评分次数</xsl:with-param>
			<xsl:with-param name="lab_avg_score">平均分数 (%)</xsl:with-param>
			<xsl:with-param name="lab_per_att">全部答对次数</xsl:with-param>
			<xsl:with-param name="lab_que">题目数</xsl:with-param>
			<xsl:with-param name="lab_lrn">学员人数</xsl:with-param>
			<xsl:with-param name="lab_content">报告栏</xsl:with-param>
			<xsl:with-param name="lab_num_attempts">个别尝试</xsl:with-param>
			<xsl:with-param name="lab_all_attempts">所有尝试</xsl:with-param>
			<xsl:with-param name="lab_sub_date">提交日期</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">我负责的学员</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">报读我负责课程的学员</xsl:with-param>
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
			<xsl:with-param name="lab_attemp_type">Attempt type</xsl:with-param>
			<xsl:with-param name="lab_enollment_date">Enrollment date</xsl:with-param>
		
			<xsl:with-param name="lab_to">to</xsl:with-param>
			<xsl:with-param name="lab_content">Report columns</xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">Enrollment date</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">Save</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_view_rpt">Run</xsl:with-param>
			<xsl:with-param name="lab_lrn_mod">Test module</xsl:with-param>
			<!--    =====================-->
			<xsl:with-param name="lab_group_by">Group by</xsl:with-param>
			<xsl:with-param name="lab_group_by_Q">Question</xsl:with-param>
			<xsl:with-param name="lab_group_by_RF">Resource folder</xsl:with-param>
			<xsl:with-param name="lab_group_by_QT">Question type</xsl:with-param>
			<xsl:with-param name="lab_res_folder">Resource folder</xsl:with-param>
			<xsl:with-param name="lab_qu_type">Question type</xsl:with-param>
			<xsl:with-param name="lab_dif">Difficulty</xsl:with-param>
			<xsl:with-param name="lab_attempts">Attempts</xsl:with-param>
			<xsl:with-param name="lab_correct">Correct</xsl:with-param>
			<xsl:with-param name="lab_incorrect">Incorrect</xsl:with-param>
			<xsl:with-param name="lab_partial_correct">Partial correct</xsl:with-param>
			<xsl:with-param name="lab_N_graded">Not graded</xsl:with-param>
			<xsl:with-param name="lab_avg_score">Average score (%)</xsl:with-param>
			<xsl:with-param name="lab_per_att">Perfect attempts</xsl:with-param>
			<xsl:with-param name="lab_que">Questions</xsl:with-param>
			<xsl:with-param name="lab_lrn">Learners</xsl:with-param>
			<xsl:with-param name="lab_num_attempts">Numbered attempts</xsl:with-param>
			<xsl:with-param name="lab_all_attempts">All attempts</xsl:with-param>
				<xsl:with-param name="lab_sub_date">Submission date</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">My responsible learners</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">Learners that have enrolled in my responsible courses</xsl:with-param>
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
		<xsl:param name="lab_attemp_type"/>
		<xsl:param name="lab_enollment_date"/>
		
		<xsl:param name="lab_to"/>
		<!--<xsl:param name="lab_cost_center"/>-->
		<xsl:param name="lab_att_create_timestamp"/>
		<!-- button text -->
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_remove"/>
		<xsl:param name="lab_g_form_btn_save"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_view_rpt"/>
		<xsl:param name="lab_lrn_mod"/>
		<!--    =====================-->
		<xsl:param name="lab_group_by"/>
		<xsl:param name="lab_group_by_Q"/>
		<xsl:param name="lab_group_by_RF"/>
		<xsl:param name="lab_group_by_QT"/>
		<xsl:param name="lab_res_folder"/>
		<xsl:param name="lab_qu_type"/>
		<xsl:param name="lab_dif"/>
		<xsl:param name="lab_attempts"/>
		<xsl:param name="lab_correct"/>
		<xsl:param name="lab_incorrect"/>
		<xsl:param name="lab_partial_correct"/>
		<xsl:param name="lab_N_graded"/>
		<xsl:param name="lab_avg_score"/>
		<xsl:param name="lab_per_att"/>
		<xsl:param name="lab_que"/>
		<xsl:param name="lab_lrn"/>
		<xsl:param name="lab_content"/>
		<xsl:param name="lab_num_attempts"/>
		<xsl:param name="lab_all_attempts"/>
		<xsl:param name="lab_sub_date"/>
	
		<xsl:param name="lab_answer_for_lrn"/>
		<xsl:param name="lab_answer_for_course_lrn"/>
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
				<xsl:with-param name="lab_enollment_date" select="$lab_enollment_date"/>
				<xsl:with-param name="lab_sub_date" select="$lab_sub_date"/>
				<xsl:with-param name="lab_lrn_group" select="$lab_lrn_group"/>
				<xsl:with-param name="lab_by_all_user" select="$lab_by_all_user"/>
				<xsl:with-param name="lab_by_selected_usg" select="$lab_by_selected_usg"/>
				<xsl:with-param name="lab_by_selected_user" select="$lab_by_selected_user"/>
				<xsl:with-param name="lab_to" select="$lab_to"/>
				<xsl:with-param name="lab_attemp_type" select="$lab_attemp_type"/>
				<xsl:with-param name="lab_lrn_mod" select="$lab_lrn_mod"/>
				<xsl:with-param name="lab_g_form_btn_remove" select="$lab_g_form_btn_remove"/>
					<xsl:with-param name="lab_num_attempts" select="$lab_num_attempts"/>
				<xsl:with-param name="lab_all_attempts" select="$lab_all_attempts"/>
				<xsl:with-param name="lab_answer_for_lrn" select="$lab_answer_for_lrn"/>
				<xsl:with-param name="lab_answer_for_course_lrn" select="$lab_answer_for_course_lrn"/>
			</xsl:apply-templates>
			<!--sort para-->
			<xsl:apply-templates select="spec" mode="sort">
				<!--    =====================-->
				<xsl:with-param name="lab_group_by" select="$lab_group_by"/>
				<xsl:with-param name="lab_group_by_Q" select="$lab_group_by_Q"/>
				<xsl:with-param name="lab_group_by_RF" select="$lab_group_by_RF"/>
				<xsl:with-param name="lab_group_by_QT" select="$lab_group_by_QT"/>
				<xsl:with-param name="lab_res_folder" select="$lab_res_folder"/>
				<xsl:with-param name="lab_qu_type" select="$lab_qu_type"/>
				<xsl:with-param name="lab_dif" select="$lab_dif"/>
				<xsl:with-param name="lab_correct" select="$lab_correct"/>
				<xsl:with-param name="lab_attempts" select="$lab_attempts"/>
				<xsl:with-param name="lab_incorrect" select="$lab_incorrect"/>
				<xsl:with-param name="lab_partial_correct" select="$lab_partial_correct"/>
				<xsl:with-param name="lab_N_graded" select="$lab_N_graded"/>
				<xsl:with-param name="lab_avg_score" select="$lab_avg_score"/>
				<xsl:with-param name="lab_per_att" select="$lab_per_att"/>
				<xsl:with-param name="lab_que" select="$lab_que"/>
				<xsl:with-param name="lab_lrn" select="$lab_lrn"/>
				<xsl:with-param name="lab_content" select="$lab_content"/>
				<xsl:with-param name="lab_answer_for_lrn" select="$lab_answer_for_lrn"/>
				<xsl:with-param name="lab_answer_for_course_lrn" select="$lab_answer_for_course_lrn"/>
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
		<xsl:param name="lab_attemp_type"/>
		<xsl:param name="lab_enollment_date"/>
		<xsl:param name="lab_sub_date"/>
		<xsl:param name="lab_lrn_group"/>
		<xsl:param name="lab_by_all_user"/>
		<xsl:param name="lab_by_selected_user"/>
		<xsl:param name="lab_by_selected_usg"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_lrn_mod"/>
		<xsl:param name="lab_g_form_btn_remove"/>
		<xsl:param name="lab_num_attempts"/>
		<xsl:param name="lab_all_attempts"/>
		<xsl:param name="lab_answer_for_lrn"/>
		<xsl:param name="lab_answer_for_course_lrn"/>
		<!--== Spec Name ==-->
		<xsl:if test="title != '' and $spec_ent_id != '0'">
			<tr>
				<td width="20%" align="right" valign="top"  class="wzb-form-label">
					<span class="wzb-form-star">*</span>
					<span class="TitleText">
						<xsl:value-of select="$lab_rpt_name"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td width="80%" class="wzb-form-control">
					<span class="Text">
						<input type="text" name="rsp_title" value="{title}" size="30" style="width:350px;" class="wzb-inputText" maxlength="200"/>
						<br/>
						<xsl:value-of select="$lab_rpt_name_length"/>
					</span>
				</td>
			</tr>
		</xsl:if>
		<!--== Catalog ==-->
		<!--catolog has been deleted-->
		<!--== module ==-->
		<tr>
			<td width="20%" align="right" class="wzb-form-label" >
				<span class="wzb-form-star">*</span>
				<span class="TitleText">
					<xsl:value-of select="$lab_lrn_mod"/>
					<xsl:text>：</xsl:text>
				</span>
				<!--
				<input type="hidden" name="lab_group" value="{$lab_group}"/>
				-->
			</td>
			<td width="80%" class="wzb-form-control" >
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td>
							<xsl:call-template name="wb_goldenman_sel_mod">
								<xsl:with-param name="field_name">mod_id</xsl:with-param>
								<xsl:with-param name="label_add_btn">
									<xsl:value-of select="$lab_gen_select"/>
								</xsl:with-param>
								<xsl:with-param name="name">mod_id</xsl:with-param>
								<xsl:with-param name="label_remove_btn">
									<xsl:value-of select="$lab_g_form_btn_remove"/>
								</xsl:with-param>
								<xsl:with-param name="is_multiple">false</xsl:with-param>
								<xsl:with-param name="close_pop_win">true</xsl:with-param>
								<xsl:with-param name="sel_type">sel_mod</xsl:with-param>
								<!-- sel_mod / import_mod  ，不给值时默认为sel_mod -->
								<xsl:with-param name="dis_cos_type">3</xsl:with-param>
								<!--1: 所有 ；2：不显示Course ; 3: 不显示独立内容Course-->
								<xsl:with-param name="dis_mod_type">3</xsl:with-param>
								<!--1: 所有 ；2：只第一层 ; 3: 只测验类  -->
								<xsl:with-param name="cos_title">
									<xsl:apply-templates select="$data_list/data[@name='itm_id']" mode="title"/>
								</xsl:with-param>
									<xsl:with-param name="single_option_value"><xsl:value-of select="$data_list/data[@name='mod_id']/@value"/></xsl:with-param>
									<xsl:with-param name="single_option_desc"><xsl:value-of select="/report/report_body/presentation/data[@name='mod_id' and @value =$data_list/data[@name='mod_id']/@value]/@display"/></xsl:with-param>
									<!--itm id  -->
								<xsl:with-param name="itm_id_field_name">itm_id</xsl:with-param>
								<xsl:with-param name="itm_id_field_value">
									<xsl:value-of select="$data_list/data[@name='itm_id']/@value"/>
								</xsl:with-param>
								<xsl:with-param name="width">300</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<!--== Learner Group ==-->
		<tr>
			<td width="20%" align="right" valign="top"  class="wzb-form-label">
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
							<label for="rdo_sel_by_all_user" onclick="changelrn_cos_ContentStatus(false,'lrn')">
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
								<span style="padding:0 0 0 20px;">
									<input type="checkbox" name="lrn_content" id="chk_answer_for_lrn">
									<xsl:if test="$report_body/spec/data_list/data[@name='answer_for_lrn' and @value='1'] or not(/report/report_body/spec/data_list) ">
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
								<span style="padding:0 0 0 20px;">
									<input type="checkbox" name="lrn_content" id="chk_answer_for_course_lrn">
										<xsl:if test="$report_body/spec/data_list/data[@name='answer_for_course_lrn' and @value='1'] or not(/report/report_body/spec/data_list) ">
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
							<label for="rdo_sel_by_selected_user" onclick="changelrn_cos_ContentStatus(true,'lrn')">
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
									<xsl:if test="count($report_body/presentation/data[@name='usr_ent_id']) &gt;= 1">
										<xsl:for-each select="$report_body/presentation/data[@name='usr_ent_id']">
											<option value="{./@value}"><xsl:value-of select="./@display"/></option>
										</xsl:for-each>
									</xsl:if>
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
									<xsl:if test="count($report_body/presentation/data[@name='usg_ent_id']) &gt;= 1">
									<xsl:for-each select="$report_body/presentation/data[@name='usg_ent_id']">
										<option value="{./@value}"><xsl:value-of select="./@display"/></option>
									</xsl:for-each>
									</xsl:if>
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
			<td width="20%" align="right"  class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_enollment_date"/>
						<xsl:text>：</xsl:text>
					</span>
			</td>
			<td width="80%"  class="wzb-form-control">
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
		<!--== Period ==-->
		<tr>
			<td width="20%" align="right"  class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_sub_date"/>
						<xsl:text>：</xsl:text>
					</span>
			</td>
			<td width="80%"  class="wzb-form-control">
				<span class="Text">
					<xsl:value-of select="$lab_const_from"/>
					<xsl:text>&#160;</xsl:text>
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">attempt_start_datetime</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">attempt_start_datetime</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
						<xsl:with-param name="timestamp">
							<xsl:value-of select="data_list/data[@name='attempt_start_datetime']/@value"/>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$lab_to"/>
					<xsl:text>&#160;</xsl:text>
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">attempt_end_datetime</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">attempt_end_datetime</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
						<xsl:with-param name="timestamp">
							<xsl:value-of select="data_list/data[@name='attempt_end_datetime']/@value"/>
						</xsl:with-param>
					</xsl:call-template>
				</span>
				<input type="hidden" name="lab_sub_date" value="{$lab_sub_date}"/>
			</td>
		</tr>
		<!-- completion_status-->
		<tr>
			<td width="20%" align="right"  class="wzb-form-label">
					<span class="wzb-form-star">*</span>
					<span class="TitleText">
					 <xsl:value-of select="$lab_attemp_type"/>
						<xsl:text>：</xsl:text>
					</span>
			</td>
			<td width="80%" class="wzb-form-control">
				<label for="NUMBERED">
					<input type="checkbox" name="attempt_type" value="NUMBERED" id="NUMBERED">
						<xsl:if test="data_list/data[@name='attempt_type']/@value = 'NUMBERED' or $spec_id=0">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
							<xsl:value-of select="$lab_num_attempts"/>  
						</input>
				</label>
				<label for="ALL" style="margin-left:15px;">
					<input type="checkbox" name="attempt_type" value="ALL" id="ALL">
						<xsl:if test="data_list/data[@name='attempt_type']/@value = 'ALL' or $spec_id=0">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
							<xsl:value-of select="$lab_all_attempts"/>    
						</input>
				</label>
			</td>
		</tr>
	</xsl:template>
	<!-- ===========================sort order section ==============================================-->
	<xsl:template match="spec" mode="sort">
		<!--    =====================-->
		<xsl:param name="lab_group_by"/>
		<xsl:param name="lab_group_by_Q"/>
		<xsl:param name="lab_group_by_RF"/>
		<xsl:param name="lab_group_by_QT"/>
		<xsl:param name="lab_res_folder"/>
		<xsl:param name="lab_qu_type"/>
		<xsl:param name="lab_dif"/>
		<xsl:param name="lab_attempts"/>
		<xsl:param name="lab_correct"/>
		<xsl:param name="lab_incorrect"/>
		<xsl:param name="lab_partial_correct"/>
		<xsl:param name="lab_N_graded"/>
		<xsl:param name="lab_avg_score"/>
		<xsl:param name="lab_per_att"/>
		<xsl:param name="lab_que"/>
		<xsl:param name="lab_lrn"/>
		<xsl:param name="lab_content"/>
		<!--======================================-->
		<xsl:variable name="sort_value" select="data_list/data[@name='sort_col']/@value"/>
		<xsl:variable name="index" select="data_list/data[@name='sort_order']/@value"/>
		<xsl:variable name="itemno" select="data_list/data[@name='page_size']/@value"/>
		<tr>
			<td width="20%" align="right"  class="wzb-form-label">
					<span class="wzb-form-star">*</span>
					<span class="TitleText">
						<xsl:value-of select="$lab_group_by"/>
						<xsl:text>：</xsl:text>
					</span>
			</td>
			<td width="20%"  class="wzb-form-control">
				<select name="group_by" class="select" onChange="changeRptCol()">
					<option value="QUE">
						<xsl:if test="data_list/data[@name='group_by']/@value = 'QUE'">
							<xsl:attribute name="selected">selected</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_group_by_Q"/>
					</option>
					<option value="RES_FDR">
						<xsl:if test="data_list/data[@name='group_by']/@value = 'RES_FDR'">
							<xsl:attribute name="selected">selected</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_group_by_RF"/>
					</option>
					<option value="QUE_TYPE">
						<xsl:if test="data_list/data[@name='group_by']/@value = 'QUE_TYPE'">
							<xsl:attribute name="selected">selected</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_group_by_QT"/>
					</option>
				</select>
			</td>
		</tr>
		<tr>
			<td width="20%" align="right" valign="top"  class="wzb-form-label">
					<span class="wzb-form-star">*</span>
					<span class="TitleText">
						<xsl:value-of select="$lab_content"/>
						<xsl:text>：</xsl:text>
					</span>
			</td>
			<td width="80%"  class="wzb-form-control">
				<table cellpadding="0" cellspacing="0" width="100%" border="0">
					<tr>
						<td width="33%">
							<span class="Text">
								<label for="label_01">
									<input type="checkbox" name="content_lst" value="res_fdr" id="label_01">
										<xsl:if test="data_list/data[@name='content_lst']/@value = 'res_fdr' or $spec_id=0">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_res_folder"/>
								</label>
							</span>
						</td>
						<td width="33%">
							<span class="Text">
								<label for="label_02">
									<input type="checkbox" name="content_lst" value="res_type" id="label_02">
										<xsl:if test="data_list/data[@name='content_lst']/@value = 'res_type' or $spec_id=0">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_qu_type"/>
								</label>
							</span>
						</td>
						<td width="34%">
							<span class="Text">
								<label for="label_03">
									<input type="checkbox" name="content_lst" value="res_diff" id="label_03">
										<xsl:if test="data_list/data[@name='content_lst']/@value = 'res_diff' or $spec_id=0">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_dif"/>
								</label>
							</span>
						</td>
					</tr>
					<tr>
						<td width="33%">
							<span class="Text">
								<label for="label_04">
									<input type="checkbox" name="content_lst" value="attempt_cnt" id="label_04">
										<xsl:if test="data_list/data[@name='content_lst']/@value = 'attempt_cnt' or $spec_id=0">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_attempts"/>
								</label>
							</span>
						</td>
						<td width="33%">
							<span class="Text">
								<label for="label_05">
									<input type="checkbox" name="content_lst" value="correct" id="label_05">
										<xsl:if test="data_list/data[@name='content_lst']/@value = 'correct' or $spec_id=0">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_correct"/>
								</label>
							</span>
						</td>
						<td width="34%">
							<span class="Text">
								<label for="label_06">
									<input type="checkbox" name="content_lst" value="incorrect" id="label_06">
										<xsl:if test="data_list/data[@name='content_lst']/@value = 'incorrect' or $spec_id=0">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_incorrect"/>
								</label>
							</span>
						</td>
					</tr>
					<tr>
						<td width="33%">
							<span class="Text">
								<label for="label_07">
									<input type="checkbox" name="content_lst" value="partial_correct" id="label_07">
										<xsl:if test="data_list/data[@name='content_lst']/@value = 'partial_correct' or $spec_id=0">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_partial_correct"/>
								</label>
							</span>
						</td>
						<td width="33%">
							<span class="Text">
								<label for="label_08">
									<input type="checkbox" name="content_lst" value="not_graded" id="label_08">
										<xsl:if test="data_list/data[@name='content_lst']/@value = 'not_graded' or $spec_id=0">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_N_graded"/>
								</label>
							</span>
						</td>
						<td width="34%">
							<span class="Text">
								<label for="label_09">
									<input type="checkbox" name="content_lst" value="avg_sore" id="label_09">
										<xsl:if test="data_list/data[@name='content_lst']/@value = 'avg_sore' or $spec_id=0">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_avg_score"/>
								</label>
							</span>
						</td>
					</tr>
					<tr>
						<td width="33%">
							<span class="Text">
								<label for="label_10">
									<input type="checkbox" name="content_lst" value="perfect_attempts" id="label_10">
										<xsl:if test="data_list/data[@name='content_lst']/@value = 'perfect_attempts' or $spec_id=0">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_per_att"/>
								</label>
							</span>
						</td>
						<td width="33%">
							<span class="Text">
								<label for="label_11">
									<input type="checkbox" name="content_lst" value="questions" id="label_11">
										<xsl:if test="data_list/data[@name='content_lst']/@value = 'questions' or $spec_id=0">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_que"/>
								</label>
							</span>
						</td>
						<td width="34%">
							<span class="Text">
								<label for="label_12">
									<input type="checkbox" name="content_lst" value="learners" id="label_12">
										<xsl:if test="data_list/data[@name='content_lst']/@value = 'learners' or $spec_id=0">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_lrn"/>
								</label>
							</span>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
