<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
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
	<xsl:import href="utils/wb_goldenman_sel_mod.xsl"/>
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
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_train_end_date" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '711')"/>
	<xsl:variable name="lab_train_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '562')"/>
	<xsl:variable name="lab_train_code" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '804')"/>
	<xsl:variable name="lab_class_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_c_title')"/>
	<xsl:variable name="lab_class_code" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_c_code')"/>
	<xsl:variable name="lab_budget" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '718')"/>
	<xsl:variable name="lab_actual" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '719')"/>
	<xsl:variable name="lab_exec_rate" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '720')"/>
	<xsl:variable name="lab_no_of_attend" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '731')"/>
	<xsl:variable name="lab_charge_per_head" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '732')"/>
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
				var ctn = [frm.content];
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
			
			function exam_change(frm,obj){
					var mod, pos, neg;
					if(obj.value == 0){
						changelrn_cos_ContentStatus(false,'cos');
						pos = true;
						neg = true;
					}else if (obj.value == 1){
						changelrn_cos_ContentStatus(true,'cos');
						pos = true;
						neg = false;
					}else {
						changelrn_cos_ContentStatus(true,'cos');
						var pos = false;
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
						exam_change(frm,frm.course_sel_type[0])
					}else if(frm.course_sel_type && frm.course_sel_type[1].checked){
						exam_change(frm,frm.course_sel_type[1])
					}else {
						exam_change(frm,frm.course_sel_type[2])
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
				   
		]]>
		</script>
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
				<input type="hidden" name="p_itm_code" value="{$lab_train_code}"/>
				<input type="hidden" name="p_itm_title" value="{$lab_train_title}"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="report_body">
			<!-- header text -->
			<xsl:with-param name="lab_instruction">你可以透過選擇以下屬性建立自己的報告，按<b>執行</b>可以直接查看報告內容。按<b>儲存</b>可以將指定的查詢條件儲存為設定報告。</xsl:with-param>
			<xsl:with-param name="lab_edit_instruction">選擇以下屬性編輯報告，按<b>確定</b>儲存報告。</xsl:with-param>
			<xsl:with-param name="lab_standard_rpt_inst">指定報告標準並按<b>執行</b>瀏覽你的下屬的學習報告。</xsl:with-param>
			<xsl:with-param name="lab_instruct">請填入報告的查詢條件。</xsl:with-param>
			<!-- form text -->
			<xsl:with-param name="lab_rpt_name">報告名稱</xsl:with-param>
			<xsl:with-param name="lab_rpt_name_length">（不超過80字元）</xsl:with-param>
			<!-- search criteria text -->
			<xsl:with-param name="lab_all_courses">所有培訓</xsl:with-param>
			<xsl:with-param name="lab_lrn_group">學員</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">所有學員</xsl:with-param>
			<xsl:with-param name="lab_by_selected_user">只有這些學員</xsl:with-param>
			<xsl:with-param name="lab_by_selected_usg">指定用戶組</xsl:with-param>
			<xsl:with-param name="lab_itm">培訓</xsl:with-param>
			<xsl:with-param name="lab_by_itm">只有這些培訓</xsl:with-param>
			<xsl:with-param name="lab_by_catalog">只包含此目錄中的培訓</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<!-- content attribute text -->
			<xsl:with-param name="lab_content">報告欄</xsl:with-param>
			<!-- content attribute text of course -->
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
			<xsl:with-param name="lab_answer_for_course">我負責的培訓</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">報讀我負責培訓的學員</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">我負責學員報讀的培訓</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="report_body">
			<!-- header text -->
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
			<xsl:with-param name="lab_all_courses">所有培训</xsl:with-param>
			<xsl:with-param name="lab_by_selected_user">指定学员</xsl:with-param>
			<xsl:with-param name="lab_by_selected_usg">指定用户组</xsl:with-param>
			<xsl:with-param name="lab_itm">培训</xsl:with-param>
			<xsl:with-param name="lab_by_itm">指定培训</xsl:with-param>
			<xsl:with-param name="lab_by_catalog">指定培训目录</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<!-- content attribute text -->
			<xsl:with-param name="lab_content">报告栏</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_view_rpt">查看</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">保存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">删除</xsl:with-param>
			<!-- unused? -->
			<xsl:with-param name="lab_attendance">考勤</xsl:with-param>
			<xsl:with-param name="lab_run_num">
				<xsl:value-of select="$lab_const_run"/>数目</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">我负责的学员</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">我负责的培训</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">报读我负责培训的学员</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">我负责学员报读的培训</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="report_body">
			<!-- header text -->
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
			<xsl:with-param name="lab_all_courses">All training</xsl:with-param>
			<xsl:with-param name="lab_itm">Training</xsl:with-param>
			<xsl:with-param name="lab_by_catalog">Only training in these catalogs</xsl:with-param>
			<xsl:with-param name="lab_by_itm">Only these training</xsl:with-param>
			<xsl:with-param name="lab_to">to</xsl:with-param>
			<!-- content attribute text -->
			<xsl:with-param name="lab_content">Report columns</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">Save</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_view_rpt">Run</xsl:with-param>
			<!-- unused? -->
			<xsl:with-param name="lab_attendance">Attendance</xsl:with-param>
			<xsl:with-param name="lab_run_num">
				<xsl:value-of select="$lab_const_run"/> number</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">My responsible learners</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">My responsible training</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">Learners that have enrolled in my responsible training</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">Training that have been enrolled by my responsible learners</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ===========================report _body==================================== -->
	<xsl:template match="report_body">
		<!-- header text -->
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
		<xsl:param name="lab_by_catalog"/>
		<xsl:param name="lab_by_itm"/>
		<xsl:param name="lab_show_in_progress_attendance"/>
		<xsl:param name="lab_to"/>
		<!-- content attribute text -->
		<xsl:param name="lab_content"/>
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
				<xsl:with-param name="lab_by_catalog" select="$lab_by_catalog"/>
				<xsl:with-param name="lab_by_itm" select="$lab_by_itm"/>
				<xsl:with-param name="lab_lrn_group" select="$lab_lrn_group"/>
				<xsl:with-param name="lab_by_all_user" select="$lab_by_all_user"/>
				<xsl:with-param name="lab_by_selected_usg" select="$lab_by_selected_usg"/>
				<xsl:with-param name="lab_by_selected_user" select="$lab_by_selected_user"/>
				<xsl:with-param name="lab_to" select="$lab_to"/>
				<xsl:with-param name="lab_show_in_progress_attendance" select="$lab_show_in_progress_attendance"/>
				<xsl:with-param name="lab_all_courses" select="$lab_all_courses"/>
				<xsl:with-param name="lab_answer_for_lrn" select="$lab_answer_for_lrn"/>
				<xsl:with-param name="lab_answer_for_course" select="$lab_answer_for_course"/>
				<xsl:with-param name="lab_answer_for_course_lrn" select="$lab_answer_for_course_lrn"/>
				<xsl:with-param name="lab_answer_for_lrn_course" select="$lab_answer_for_lrn_course"/>
			</xsl:apply-templates>
			
			<xsl:apply-templates select="display_option">
				<xsl:with-param name="lab_content" select="$lab_content"/>
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
		<xsl:param name="lab_by_catalog"/>
		<xsl:param name="lab_by_itm"/>
		<xsl:param name="lab_lrn_group"/>
		<xsl:param name="lab_by_all_user"/>
		<xsl:param name="lab_by_selected_user"/>
		<xsl:param name="lab_by_selected_usg"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_all_courses"/>
		<xsl:param name="lab_answer_for_lrn"/>
		<xsl:param name="lab_answer_for_course"/>
		<xsl:param name="lab_answer_for_course_lrn"/>
		<xsl:param name="lab_answer_for_lrn_course"/>
		<!--== Spec Name ==-->
		<xsl:if test="title != '' and $spec_ent_id != '0'">
			<tr>
				<td width="20%" align="right" valign="top" class="wzb-form-label">
					<span class="wzb-form-star">*</span>
					<span class="TitleText">
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
							</xsl:call-template>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<!--== Exam ==-->
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
						<td valign="top">
							<span>
								<input type="radio" name="course_sel_type" value="0" id="rdo_sel_by_all_course_title" onclick="exam_change(document.frmXml,this);">
									<xsl:if test="$report_body/spec/data_list/data[@name='itm_id'] and $report_body/spec/data_list/data[@name='itm_id']/@value = '' or ( not($report_body/spec/data_list/data[@name='itm_id']) and not($report_body/spec/data_list/data[@name='tnd_id']) and not($report_body/spec/data_list/data[@name='mod_id']))">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
							</span>
							<label for="rdo_sel_by_all_course_title">
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
									<xsl:if test="$report_body/spec/data_list/data[@name='answer_for_course' and @value='1'] or not(/report/report_body/spec/data_list)">
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
									<xsl:if test="$report_body/spec/data_list/data[@name='answer_for_lrn_course' and @value='1'] or not(/report/report_body/spec/data_list)">
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
								<input type="radio" name="course_sel_type" value="1" id="rdo_sel_by_course_title" onclick="exam_change(document.frmXml,this);">
									<xsl:if test="$report_body/spec/data_list/data[@name='itm_id'] and $report_body/spec/data_list/data[@name='itm_id']/@value != ''">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
							</span>
							<label for="rdo_sel_by_course_title">
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
								<xsl:with-param name="custom_js_code">exam_change(document.frmXml,document.frmXml.course_sel_type[1]);document.frmXml.course_sel_type[1].checked = true;</xsl:with-param>
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
								<input type="radio" name="course_sel_type" value="2" id="rdo_sel_by_type_or_cata" onclick="exam_change(document.frmXml,this);">
									<xsl:if test="$report_body/spec/data_list/data[@name='tnd_id']">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
							</span>
							<label for="rdo_sel_by_type_or_cata">
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
								<xsl:with-param name="custom_js_code">exam_change(document.frmXml,document.frmXml.course_sel_type[2]);document.frmXml.course_sel_type[2].checked = true;</xsl:with-param>
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
		<!--== Period ==-->
		<tr>
			<td width="20%" align="right" class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_train_end_date"/>
						<xsl:text>：</xsl:text>
					</span>
			</td>
			<td width="80%" class="wzb-form-control">
				<input type="hidden" name="lab_rpt_datetime" value="{$lab_train_end_date}"/>
				<span class="Text">
					<xsl:value-of select="$lab_const_from"/>
					<xsl:text>&#160;</xsl:text>
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">start_datetime</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">start_datetime</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
						<xsl:with-param name="timestamp">
							<xsl:value-of select="data_list/data[@name='start_datetime']/@value"/>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$lab_to"/>
					<xsl:text>&#160;</xsl:text>
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">end_datetime</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">end_datetime</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
						<xsl:with-param name="timestamp">
							<xsl:value-of select="data_list/data[@name='end_datetime']/@value"/>
						</xsl:with-param>
					</xsl:call-template>
				</span>
			</td>
		</tr>
	</xsl:template>
	<!--==================================================================-->
	<!--display option-->
	<xsl:template match="display_option">
		<xsl:param name="lab_content"/>
		<tr>
			<td width="20%" align="right" valign="top" class="wzb-form-label">
					<span class="wzb-form-star">*</span>
					<span class="TitleText">
						<xsl:value-of select="$lab_content"/>
						<xsl:text>：</xsl:text>
					</span>
			</td>
			<td width="80%" class="wzb-form-control">
				<xsl:apply-templates select="*[name() = 'other']" mode="content"/>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- Item -->
	<xsl:template match="other" mode="content">
		<table cellpadding="0" cellspacing="0" width="100%" border="0">
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
								<input type="checkbox" name="content" value="{.}" id="label_{.}">
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
								<xsl:choose>
									<xsl:when test=". = 'p_itm_code'">
										<xsl:value-of select="$lab_train_code"/>
									</xsl:when>
									<xsl:when test=". = 'p_itm_title'">
										<xsl:value-of select="$lab_train_title"/>
									</xsl:when>
									<xsl:when test=". = 'c_itm_code'">
										<xsl:value-of select="$lab_class_code"/>
									</xsl:when>
									<xsl:when test=". = 'c_itm_title'">
										<xsl:value-of select="$lab_class_title"/>
									</xsl:when>
									<xsl:when test=". = 'itm_cost_budget'">
										<xsl:value-of select="$lab_budget"/>
									</xsl:when>
									<xsl:when test=". = 'itm_cost_actual'">
										<xsl:value-of select="$lab_actual"/>
									</xsl:when>
									<xsl:when test=". = 'itm_cost_exec_rate'">
										<xsl:value-of select="$lab_exec_rate"/>
									</xsl:when>
									<xsl:when test=". = 'no_of_training_attend'">
										<xsl:value-of select="$lab_no_of_attend"/>
									</xsl:when>
									<xsl:when test=". = 'charge_per_head'">
										<xsl:value-of select="$lab_charge_per_head"/>
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
	<!-- =============================================================== -->
	<xsl:template match="data">
		<option value="{@value}">
			<xsl:variable name="name" select="@name"/>
			<xsl:variable name="value" select="@value"/>
			<xsl:value-of select="/report/report_body/presentation/data[@name=$name and @value = $value]/@display"/>
		</option>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data" mode="title">
		<xsl:variable name="name" select="@name"/>
		<xsl:variable name="value" select="@value"/>
		<xsl:for-each select="/report/report_body/presentation/data[@name=$name and @value = $value]/parent">
			<xsl:value-of select="@display"/>(<xsl:value-of select="@itm_code"/>) > 
		</xsl:for-each>
		<xsl:value-of select="/report/report_body/presentation/data[@name=$name and @value = $value]/@display"/>(<xsl:value-of select="/report/report_body/presentation/data[@name=$name and @value = $value]/@itm_code"/>)
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
