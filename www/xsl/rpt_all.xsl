<?xml version="1.0" encoding="UTF-8"?>
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
    <xsl:import href="utils/wb_ui_head.xsl"/>
    <xsl:import href="utils/wb_ui_desc.xsl"/>
    <xsl:import href="utils/wb_ui_line.xsl"/>
    <xsl:import href="utils/wb_ui_space.xsl"/>
    <xsl:import href="utils/wb_ui_hdr.xsl"/>
    <xsl:import href="utils/wb_gen_button.xsl"/>
    <xsl:import href="utils/escape_js.xsl"/>
    <xsl:import href="share/label_rpt.xsl"/>
    <xsl:output indent="yes"/>
    <!-- label variable -->
    <xsl:variable name="lab_rpt_credit" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '808')"/>
    
    <xsl:variable name="cur_ent_id" select="/report/meta/cur_usr/@ent_id"/>
    <xsl:variable name="enableCpd" select="/report/enableCPD"/>
    <!-- 学员学习进度报表 （新） -->
    <xsl:variable name="label_core_report_154" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_rp.label_core_report_154')"/>
    <xsl:variable name="label_core_report_155" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_rp.label_core_report_155')"/>
    <!-- 学员获得CPT/D时数报表  -->
    <xsl:variable name="label_core_cpt_d_management_149" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_cpt_d_management_149')"/>
    <!-- 培训管理员可查询所在培训中心学员获得的CPT/D时数 -->
    <xsl:variable name="label_core_cpt_d_management_161" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_cpt_d_management_161')"/>
    <!--培训管理员可查询所在培训中心学员未完成的CPT/D时数-->
    <xsl:variable name="label_core_cpt_d_management_178" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_cpt_d_management_178')"/>
    <!-- 学员牌照注册报表  -->
    <xsl:variable name="label_core_cpt_d_management_163" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_cpt_d_management_163')"/>
    <!-- 培训管理员可查询所在培训中心学员的牌照注册情况-->
    <xsl:variable name="label_core_cpt_d_management_164" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_cpt_d_management_164')"/>
    <!-- 个人COT/D时数报表 -->
    <xsl:variable name="label_core_cpt_d_management_177" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_cpt_d_management_177')"/>
    
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
            <script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_mgt_rpt.js"/>
            <script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
            <script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
            <script type="text/javascript" src="{ctx}/static/js/i18n/{$wb_cur_lang}/label_rp_{$wb_cur_lang}.js"></script>
            <script language="JavaScript"><![CDATA[
            var mgt_rpt = new wbManagementReport;
            current = 0;
            
            function down_excel(spec_id){
                var downloadPath ='';
                layer.open({
			          type: 1,//弹出类型 
			          area: ['500px', '274px'], //宽高
			          title : fetchLabel("label_core_report_140"),//标题 
			          content: '<div class="pop-up-word">'+
			                        '<span id="successMsg" style="display:none;">'+fetchLabel("label_core_report_163")+'</span>'+
			                        '<div id="download_loading" class="layer-loading"></div>'+
			                    '</div>'+
			                    '<div class="wzb-bar">'+
			                        '<input id="downloadBtn" disabled="disabled" value="'+fetchLabel("label_core_report_139")+'" type="button" class="btn wzb-btn-blue margin-right10 wzb-btn-big">'+
			                    '</div>',
                      success: function(layero, index){
                            $.ajax({
                                url : "/app/admin/learnerReport/exporJsp?rsp_id="+spec_id,
                                type : 'POST',
                                dataType : 'json',
                                traditional : true,
                                success : function(data) {
                                    downloadPath = data.fileUri;
			                        $('#download_loading').hide();
			                        $('#successMsg').show();
			                        $('#downloadBtn').removeAttr("disabled");
			                        $('#downloadBtn').click(function(){
			                            if(downloadPath!=''){
			                                window.location.href = downloadPath;
			                            }
			                        });
                                }
                             });
                      }
                });
            }
            ]]></script>
            <xsl:call-template name="wb_css">
                <xsl:with-param name="view">wb_ui</xsl:with-param>
            </xsl:call-template>
        </head>
        <body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
            <form name="frmXml">
                <xsl:call-template name="wb_init_lab"/>
            </form>
        </body>
    </xsl:template>
    <!-- ============================================================= -->
    <xsl:template name="lang_ch">
        <xsl:call-template name="content">
            <xsl:with-param name="lab_mgmt_report">培訓報表</xsl:with-param>
            <xsl:with-param name="lab_instruction">所有報告可供查看和導出系統，你也可以保存報告條件作爲你的個人報告模板。</xsl:with-param>
            <xsl:with-param name="lab_instruction_training">顯示學員的學習進展情况，並以課程或學員分組統計資料。</xsl:with-param>
            <xsl:with-param name="lab_instruction_cos_eval">顯示課程評估的結果。</xsl:with-param>
            <xsl:with-param name="lab_instruction_lrn_cos">以課程分組的形式，顯示學員的學習情況。</xsl:with-param>
            <xsl:with-param name="lab_instruction_lrn_lrn">以學員分組的形式，顯示學員的學習情況。</xsl:with-param>
            <xsl:with-param name="lab_instruction_cos_rpt">以題目分組的形式，顯示課程評估的結果。</xsl:with-param>
            <xsl:with-param name="lab_instruction_mgmt">為指定用戶顯示考勤狀況和成績。</xsl:with-param>
            <xsl:with-param name="lab_instruction_mod_rpt">顯示學員在每個模塊的學習情況。</xsl:with-param>
            <xsl:with-param name="lab_instruction_ass_que_rpt">顯示一個測驗模塊的題目統計。</xsl:with-param>
            <xsl:with-param name="lab_instruction_exam_paper_stat">以測驗分組的形式，顯示學員的學習情況。</xsl:with-param>
            <xsl:with-param name="lab_instruction_train_fee_stat">顯示培訓的總費用和各種費用的明細。</xsl:with-param>
            <xsl:with-param name="lab_instruction_train_cost_stat">顯示個人或某部門、某職務的培訓成本。</xsl:with-param>
            <xsl:with-param name="lab_wizard">報告生成</xsl:with-param>
            <xsl:with-param name="lab_standard_report">標準報告：</xsl:with-param>
            <xsl:with-param name="lab_personal_report">個人報告模板:</xsl:with-param>
            <xsl:with-param name="lab_run">執行</xsl:with-param>
            <xsl:with-param name="lab_export">匯出</xsl:with-param>
            <xsl:with-param name="lab_edit">編輯</xsl:with-param>
            <xsl:with-param name="lab_delete">刪除</xsl:with-param>
            <xsl:with-param name="lab_no_rpt">找不到報告</xsl:with-param>
            <xsl:with-param name="lab_run_standard_rpt">查看標準報告</xsl:with-param>
            <xsl:with-param name="lab_build_custom_rpt">設定報告內容</xsl:with-param>
            <xsl:with-param name="lab_outstanding_hours_rpt">CPT/D未完成時數報表</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <xsl:template name="lang_gb">
        <xsl:call-template name="content">
            <xsl:with-param name="lab_mgmt_report">培训报表</xsl:with-param>
            <xsl:with-param name="lab_instruction">所有报告可供查看和导出系统，您也可以保存报告条件作为您的个人报告模板。</xsl:with-param>
            <xsl:with-param name="lab_instruction_training">显示学员的学习进展情况，并以课程或学员分组统计数据。</xsl:with-param>
            <xsl:with-param name="lab_instruction_cos_eval">显示课程评估的结果。</xsl:with-param>
            <xsl:with-param name="lab_instruction_lrn_cos">以课程分组的形式，显示学员的学习情况。</xsl:with-param>
            <xsl:with-param name="lab_instruction_lrn_lrn">以学员分组的形式，显示学员的学习情况。</xsl:with-param>
            <xsl:with-param name="lab_instruction_cos_rpt">以题目分组的形式，显示课程评估的结果。</xsl:with-param>
            <xsl:with-param name="lab_instruction_mgmt">显示指定用户的考勤状态和结果。</xsl:with-param>
            <xsl:with-param name="lab_instruction_mod_rpt">显示学员在每个模块的学习情况。</xsl:with-param>
            <xsl:with-param name="lab_instruction_ass_que_rpt">显示一个测验模块的题目统计。</xsl:with-param>
            <xsl:with-param name="lab_instruction_exam_paper_stat">以测验分组的形式，显示学员的学习情况。</xsl:with-param>
            <xsl:with-param name="lab_instruction_train_fee_stat">显示培训的总费用和各种费用的明细。</xsl:with-param>
            <xsl:with-param name="lab_instruction_train_cost_stat">显示个人或某部门、某职务的培训成本。</xsl:with-param>
            <xsl:with-param name="lab_wizard">报告生成</xsl:with-param>
            <xsl:with-param name="lab_standard_report">标准报告：</xsl:with-param>
            <xsl:with-param name="lab_personal_report">个人报告模板:</xsl:with-param>
            <xsl:with-param name="lab_run">查看</xsl:with-param>
            <xsl:with-param name="lab_export">导出</xsl:with-param>
            <xsl:with-param name="lab_edit">修改</xsl:with-param>
            <xsl:with-param name="lab_delete">删除</xsl:with-param>
            <xsl:with-param name="lab_no_rpt">没有报告模板，请使用“定制向导”定制您所需要的报告。</xsl:with-param>
            <xsl:with-param name="lab_run_standard_rpt">查看标准报告</xsl:with-param>
            <xsl:with-param name="lab_build_custom_rpt">定制报告内容</xsl:with-param>
            <xsl:with-param name="lab_outstanding_hours_rpt">CPT/D未完成时数报表</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <xsl:template name="lang_en">
        <xsl:call-template name="content">
            <xsl:with-param name="lab_mgmt_report">Training reports</xsl:with-param>
            <xsl:with-param name="lab_instruction">The following reports are available for viewing and exporting. You can also save the report criteria as your personal report templates.</xsl:with-param>
            <xsl:with-param name="lab_instruction_training">Show the learning activities of learners, and sorting result by course or learner.</xsl:with-param>
            <xsl:with-param name="lab_instruction_cos_eval">Show the results of course evaluations.</xsl:with-param>
            <xsl:with-param name="lab_instruction_lrn_cos">Show the learning activities by course.</xsl:with-param>
            <xsl:with-param name="lab_instruction_lrn_lrn">Show the learning activities by learner.</xsl:with-param>
            <xsl:with-param name="lab_instruction_cos_rpt">Show the results of course evaluations by question.</xsl:with-param>
            <xsl:with-param name="lab_instruction_mgmt">Displays attendance status and results for specified users.</xsl:with-param>
            <xsl:with-param name="lab_instruction_mod_rpt">Show the progress of learners in each module.</xsl:with-param>
            <xsl:with-param name="lab_instruction_ass_que_rpt">Show the statistics of questions in a test module.</xsl:with-param>
            <xsl:with-param name="lab_instruction_exam_paper_stat">show the learning activities by test.</xsl:with-param>
            <xsl:with-param name="lab_instruction_train_fee_stat">Show the total training expenses and item details.</xsl:with-param>
            <xsl:with-param name="lab_instruction_train_cost_stat">Show the learners' training cost.</xsl:with-param>
            <xsl:with-param name="lab_wizard">Report builder</xsl:with-param>
            <xsl:with-param name="lab_standard_report">Standard reports:</xsl:with-param>
            <xsl:with-param name="lab_personal_report">Personal report templates:</xsl:with-param>
            <xsl:with-param name="lab_run">Run</xsl:with-param>
            <xsl:with-param name="lab_export">Export</xsl:with-param>
            <xsl:with-param name="lab_edit">Edit</xsl:with-param>
            <xsl:with-param name="lab_delete">Remove</xsl:with-param>
            <xsl:with-param name="lab_no_rpt">No reports found.</xsl:with-param>
            <xsl:with-param name="lab_run_standard_rpt">Run standard report</xsl:with-param>
            <xsl:with-param name="lab_build_custom_rpt">Build custom report</xsl:with-param>
            <xsl:with-param name="lab_outstanding_hours_rpt">CPT/D outstanding hours report </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- =============================================================== -->
    <xsl:template name="content">
        <xsl:param name="lab_mgmt_report"/>
        <xsl:param name="lab_instruction"/>
        <xsl:param name="lab_instruction_training"/>
        <xsl:param name="lab_instruction_cos_eval"/>
        <xsl:param name="lab_instruction_lrn_cos"/>
        <xsl:param name="lab_instruction_lrn_lrn"/>
        <xsl:param name="lab_instruction_cos_rpt"/>
        <xsl:param name="lab_instruction_mgmt"/>
        <xsl:param name="lab_instruction_exam_paper_stat"/>
        <xsl:param name="lab_instruction_train_fee_stat"/>
        <xsl:param name="lab_instruction_train_cost_stat"/>
        <xsl:param name="lab_wizard"/>
        <xsl:param name="lab_standard_report"/>
        <xsl:param name="lab_personal_report"/>
        <xsl:param name="lab_run"/>
        <xsl:param name="lab_export"/>
        <xsl:param name="lab_edit"/>
        <xsl:param name="lab_delete"/>
        <xsl:param name="lab_no_rpt"/>
        <xsl:param name="lab_run_standard_rpt"/>
        <xsl:param name="lab_build_custom_rpt"/>
        <xsl:param name="lab_instruction_mod_rpt"/>
        <xsl:param name="lab_instruction_ass_que_rpt"/>
        <xsl:param name="lab_outstanding_hours_rpt"/>
        <xsl:call-template name="wb_ui_hdr">
            <xsl:with-param name="belong_module">FTN_AMD_TRAINING_REPORT_MGT</xsl:with-param>
            <xsl:with-param name="page_title" select="$lab_mgmt_report"/>
        </xsl:call-template>
        
        <xsl:call-template name="wb_ui_title">
            <xsl:with-param name="text" select="$lab_mgmt_report"/>
        </xsl:call-template>
        <xsl:call-template name="wb_ui_desc">
            <xsl:with-param name="text" select="$lab_instruction"/>
        </xsl:call-template>
        <!-- 静态数据 -->
        <xsl:if test="$enableCpd = 'true'">
            <table class="Bg" width="984" border="0" cellspacing="0" cellpadding="3">
            <tr>
              <td><span class="DESC DESC_header"><i class="report-13"></i>
              <a class="TitleTextBold color-blue108 font-size18" href="/app/admin/cpdAwardedHoursReport"><xsl:value-of select="$label_core_cpt_d_management_149" /><a name="CREDIT"></a></a>
              <br/><xsl:value-of select="$label_core_cpt_d_management_161" /></span></td>
             </tr>
            </table>
            <div style="margin-bottom:10px;width: 100%; height: 1px;" class="wzb-ui-line"></div>
            
             <table class="Bg" width="984" border="0" cellspacing="0" cellpadding="3">
            <tr>
              <td><span class="DESC DESC_header"><i class="report-15"></i>
              <a class="TitleTextBold color-blue108 font-size18" href="/app/admin/cpdLicenseRegistrationReport"><xsl:value-of select="$label_core_cpt_d_management_163" /><a name="CREDIT"></a></a>
              <br/><xsl:value-of select="$label_core_cpt_d_management_164" /></span></td>
             </tr>
            </table>
            <div style="margin-bottom:10px;width: 100%; height: 1px;" class="wzb-ui-line"></div>
            
            <!-- cpd未完成时数报表（TA） -->
            <table class="Bg" width="984" border="0" cellspacing="0" cellpadding="3">
            <tr>
              <td><span class="DESC DESC_header"><i class="report-12"></i>
              <a class="TitleTextBold color-blue108 font-size18" href="/app/admin/cpdOutstandingReportTa/cpdOutstandingReportTa"><xsl:value-of select="$lab_outstanding_hours_rpt" /><a name="CREDIT"></a></a>
              <br/><xsl:value-of select="$label_core_cpt_d_management_178" /></span></td>
             </tr>
            </table>
            <div style="margin-bottom:10px;width: 100%; height: 1px;" class="wzb-ui-line"></div>
            
            <table class="Bg" width="984" border="0" cellspacing="0" cellpadding="3">
            <tr>
              <td><span class="DESC DESC_header"><i class="report-14"></i>
              <a class="TitleTextBold color-blue108 font-size18" href="/app/admin/cpdIndividualReport"><xsl:value-of select="$label_core_cpt_d_management_177" /><a name="CREDIT"></a></a>
              <br/><xsl:value-of select="$label_core_cpt_d_management_177" /></span></td>
             </tr>
            </table>
            <!-- <div style="margin-bottom:10px;width: 100%; height: 1px;" class="wzb-ui-line"></div> -->
        </xsl:if>
        
        <!-- <table class="Bg" width="984" border="0" cellspacing="0" cellpadding="3">
        <tr>
          <td><span class="DESC DESC_header"><i class="report-1"></i>
          <a class="TitleTextBold color-blue108 font-size18" href="/app/admin/learnerReport"><xsl:value-of select="$label_core_report_154" /><a name="CREDIT"></a></a>
          <br/><xsl:value-of select="$label_core_report_155" /></span></td>
         </tr>
        </table> -->
        
        
        <xsl:apply-templates select="report_detail/template_list/template">
            <xsl:with-param name="lab_instruction" select="$lab_instruction"/>
            <xsl:with-param name="lab_instruction_training" select="$lab_instruction_training"/>
            <xsl:with-param name="lab_instruction_cos_eval" select="$lab_instruction_cos_eval"/>
            <xsl:with-param name="lab_instruction_lrn_cos" select="$lab_instruction_lrn_cos"/>
            <xsl:with-param name="lab_instruction_lrn_lrn" select="$lab_instruction_lrn_lrn"/>
            <xsl:with-param name="lab_instruction_cos_rpt" select="$lab_instruction_cos_rpt"/>
            <xsl:with-param name="lab_instruction_mgmt" select="$lab_instruction_mgmt"/>
            <xsl:with-param name="lab_instruction_exam_paper_stat" select="$lab_instruction_exam_paper_stat"/>
            <xsl:with-param name="lab_instruction_train_fee_stat" select="$lab_instruction_train_fee_stat"/>
            <xsl:with-param name="lab_instruction_train_cost_stat" select="$lab_instruction_train_cost_stat"/>
            <xsl:with-param name="lab_wizard" select="$lab_wizard"/>
            <xsl:with-param name="lab_standard_report" select="$lab_standard_report"/>
            <xsl:with-param name="lab_personal_report" select="$lab_personal_report"/>
            <xsl:with-param name="lab_run" select="$lab_run"/>
            <xsl:with-param name="lab_export" select="$lab_export"/>
            <xsl:with-param name="lab_edit" select="$lab_edit"/>
            <xsl:with-param name="lab_delete" select="$lab_delete"/>
            <xsl:with-param name="lab_no_rpt" select="$lab_no_rpt"/>
            <xsl:with-param name="lab_run_standard_rpt" select="$lab_run_standard_rpt"/>
            <xsl:with-param name="lab_build_custom_rpt" select="$lab_build_custom_rpt"/>
            <xsl:with-param name="lab_instruction_mod_rpt" select="$lab_instruction_mod_rpt"/>
            <xsl:with-param name="lab_instruction_ass_que_rpt" select="$lab_instruction_ass_que_rpt"/>
        </xsl:apply-templates>
        <xsl:call-template name="wb_ui_footer"/>
    </xsl:template>
    
    
    <!-- =============================================================== -->
    <xsl:template match="template">
        <xsl:param name="lab_instruction"/>
        <xsl:param name="lab_instruction_training"/>
        <xsl:param name="lab_instruction_cos_eval"/>
        <xsl:param name="lab_instruction_lrn_cos"/>
        <xsl:param name="lab_instruction_lrn_lrn"/>
        <xsl:param name="lab_instruction_cos_rpt"/>
        <xsl:param name="lab_instruction_mgmt"/>
        <xsl:param name="lab_instruction_exam_paper_stat"/>
        <xsl:param name="lab_instruction_train_fee_stat"/>
        <xsl:param name="lab_instruction_train_cost_stat"/>
        <xsl:param name="lab_wizard"/>
        <xsl:param name="lab_standard_report"/>
        <xsl:param name="lab_personal_report"/>
        <xsl:param name="lab_run"/>
        <xsl:param name="lab_export"/>
        <xsl:param name="lab_edit"/>
        <xsl:param name="lab_delete"/>
        <xsl:param name="lab_no_rpt"/>
        <xsl:param name="lab_run_standard_rpt"/>
        <xsl:param name="lab_build_custom_rpt"/>
        <xsl:param name="lab_instruction_mod_rpt"/>
        <xsl:param name="lab_instruction_ass_que_rpt"/>
        <xsl:call-template name="wb_ui_line"/>
        <xsl:if test="position() != 1">

        </xsl:if>
        <table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
            <tr>
                <td>
                    <span class="DESC DESC_header">
                        <!-- 图标 -->
                        <xsl:choose>
                            <xsl:when test="@type='LEARNER'">
                                <i class="report-1"></i>
                            </xsl:when>
                            <xsl:when test="@type='COURSE'">
                                <i class="report-1"></i>
                            </xsl:when>
                            <xsl:when test="@type='SURVEY_COS_GRP'">
                                <i class="report-3"></i>
                            </xsl:when>
                            <xsl:when test="@type='LEARNING_ACTIVITY_COS'">
                                <i class="report-1"></i>
                            </xsl:when>
                            <xsl:when test="@type='LEARNING_ACTIVITY_BY_COS'">
                                <i class="report-9"></i>
                            </xsl:when>
                            <xsl:when test="@type='LEARNING_ACTIVITY_LRN'">
                                <i class="report-10"></i>
                            </xsl:when>
                            <xsl:when test="@type='SURVEY_QUE_GRP'">
                                <i class="report-11"></i>
                            </xsl:when>
                            <xsl:when test="@type='LEARNING_MODULE'">
                                <i class="report-2"></i>
                            </xsl:when>
                            <xsl:when test="@type='ASSESSMENT_QUE_GRP'">
                                <i class="report-4"></i>
                            </xsl:when>
                            <xsl:when test="@type='EXAM_PAPER_STAT'">
                                <i class="report-5"></i>
                            </xsl:when>
                            <xsl:when test="@type='TRAIN_FEE_STAT'">
                                <i class="report-6"></i>
                            </xsl:when>
                            <xsl:when test="@type='TRAIN_COST_STAT'">
                                <i class="report-7"></i>
                            </xsl:when>
                            <xsl:when test="@type='CREDIT'">
                                <i class="report-8"></i>
                            </xsl:when>
                        </xsl:choose>
                        <!-- 大标题 -->
                        <xsl:if test="@type='LEARNER'">
                            <a href="/app/admin/learnerReport" class="TitleTextBold color-blue108 font-size18">
                            <xsl:call-template name="get_rte_title"/>
                            <a name="{@type}"/>
                        </a>
                        </xsl:if>
                        <xsl:if test="@type!='LEARNER'">
                            <a href="javascript:mgt_rpt.ins_rpt_prep('{@id}','{@type}','{xsl_list/xsl[@type='get']/text()}')" class="TitleTextBold color-blue108 font-size18">
                                <xsl:call-template name="get_rte_title"/>
                                <a name="{@type}"/>
                            </a>
                        </xsl:if>
                        <br/>
                        <!-- 小标题 -->
                        <xsl:choose>
                            <xsl:when test="@type='LEARNER'">
                                <xsl:value-of select="$lab_instruction_training"/>
                            </xsl:when>
                            <xsl:when test="@type='COURSE'">
                                <xsl:value-of select="$lab_instruction_mgmt"/>
                            </xsl:when>
                            <xsl:when test="@type='SURVEY_COS_GRP'">
                                <xsl:value-of select="$lab_instruction_cos_eval"/>
                            </xsl:when>
                            <xsl:when test="@type='LEARNING_ACTIVITY_COS'">
                                <xsl:value-of select="$lab_instruction_lrn_cos" />
                            </xsl:when>
                            <xsl:when test="@type='LEARNING_ACTIVITY_BY_COS'">
                                <xsl:value-of select="$lab_instruction_lrn_cos" />
                            </xsl:when>
                            <xsl:when test="@type='LEARNING_ACTIVITY_LRN'">
                                <xsl:value-of select="$lab_instruction_lrn_lrn"/>
                            </xsl:when>
                            <xsl:when test="@type='SURVEY_QUE_GRP'">
                                <xsl:value-of select="$lab_instruction_cos_rpt"/>
                            </xsl:when>
                            <xsl:when test="@type='LEARNING_MODULE'">
                                <xsl:value-of select="$lab_instruction_mod_rpt"/>
                            </xsl:when>
                            <xsl:when test="@type='ASSESSMENT_QUE_GRP'">
                                <xsl:value-of select="$lab_instruction_ass_que_rpt"/>
                            </xsl:when>
                            <xsl:when test="@type='EXAM_PAPER_STAT'">
                                <xsl:value-of select="$lab_instruction_exam_paper_stat"/>
                            </xsl:when>
                            <xsl:when test="@type='TRAIN_FEE_STAT'">
                                <xsl:value-of select="$lab_instruction_train_fee_stat"/>
                            </xsl:when>
                            <xsl:when test="@type='TRAIN_COST_STAT'">
                                <xsl:value-of select="$lab_instruction_train_cost_stat"/>
                            </xsl:when>
                            <xsl:when test="@type='CREDIT'">
                                <xsl:value-of select="$lab_rpt_credit"/>
                            </xsl:when>
                        </xsl:choose>
                    </span>
                </td>
            </tr>
        </table>
        <xsl:variable name="tpl_id">
            <xsl:value-of select="@id"/>
        </xsl:variable>
        
        <xsl:if test="count(/report/report_detail/spec_list/spec[@template_id=$tpl_id][@ent_id = $cur_ent_id] )">
        
        <table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
            <tr>
                <td width="2%">
                    <img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
                </td>
                <td width="93%" valign="top">
                    <dl>
                        <dt>
                            <table cellpadding="3" cellspacing="0" border="0" width="100%" class="Bg  wzb-ui-table Bg-report">
                                <tr>
                                    <td colspan="4">
                                        <span class="TitleText" style="margin-left:10px">
                                            <xsl:value-of select="$lab_personal_report"/>
                                        </span>
                                    </td>
                                </tr>
                                <xsl:apply-templates select="/report/report_detail/spec_list/spec[@template_id=$tpl_id][@ent_id = $cur_ent_id]">
                                    <xsl:with-param name="tpl_type" select="@type"/>
                                    <xsl:with-param name="xsl_get" select="xsl_list/xsl[@type='get']/text()"/>
                                    <xsl:with-param name="xsl_exe" select="xsl_list/xsl[@type='execute']/text()"/>
                                    <xsl:with-param name="xsl_dl" select="xsl_list/xsl[@type='download']/text()"/>
                                    <xsl:with-param name="lab_run" select="$lab_run"/>
                                    <xsl:with-param name="lab_export" select="$lab_export"/>
                                    <xsl:with-param name="lab_edit" select="$lab_edit"/>
                                    <xsl:with-param name="lab_delete" select="$lab_delete"/>
                                </xsl:apply-templates>
                            </table>
                        </dt>
                    </dl>
                </td>
            </tr>
        </table>
        </xsl:if>
    </xsl:template>
    <!-- =============================================================== -->
    <xsl:template match="spec">
        <xsl:param name="tpl_type"/>
        <xsl:param name="xsl_get"/>
        <xsl:param name="xsl_exe"/>
        <xsl:param name="xsl_dl"/>
        <xsl:param name="lab_run"/>
        <xsl:param name="lab_export"/>
        <xsl:param name="lab_edit"/>
        <xsl:param name="lab_delete"/>
        <xsl:variable name="title">
            <xsl:call-template name="escape_js">
                <xsl:with-param name="input_str" select="title"/>
            </xsl:call-template>
        </xsl:variable>
        <tr>
            <td width="5%">
                <img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
            </td>
            <td width="50%">
                <span class="Text" style="font-weight: bold;">
                    <xsl:value-of select="title"/>
                </span>
            </td>
            <xsl:if test="$tpl_type='LEARNER'">
                <td width="30%" align="middle">
                    <a href="/app/admin/learnerReport/getLearnerReport?rsp_id={@spec_id}" class="Text btn wzb-btn-blue">
                        <xsl:value-of select="$lab_run"/>
                    </a>
                    <img src="{$wb_img_path}tp.gif" width="8" height="1" border="0"/>
                    <a href="javascript:;"  onclick="down_excel('{@spec_id}')" class="Text btn wzb-btn-blue">
                        <xsl:value-of select="$lab_export"/>
                    </a>
                    <xsl:if test="@ent_id = $cur_ent_id">
                        <img src="{$wb_img_path}tp.gif" width="8" height="1" border="0"/>
                        <a href="/app/admin/learnerReport/update?rsp_id={@spec_id}" class="Text btn wzb-btn-blue">
                            <xsl:value-of select="$lab_edit"/>
                        </a>
                    </xsl:if>
                    <xsl:if test="@ent_id = $cur_ent_id">
                        <img src="{$wb_img_path}tp.gif" width="8" height="1" border="0"/>
                        <a href="javascript:mgt_rpt.del_rpt('{@spec_id}','{$wb_lang}')" class="Text btn wzb-btn-blue">
                            <xsl:value-of select="$lab_delete"/>
                        </a>
                    </xsl:if>
                </td>
            </xsl:if>
            <xsl:if test="$tpl_type!='LEARNER'">
                <td width="30%" align="middle">
                    <a href="javascript:current++;mgt_rpt.get_rpt('{@spec_id}','{$tpl_type}','{$title}','{$xsl_exe}')" class="Text btn wzb-btn-blue">
                        <xsl:value-of select="$lab_run"/>
                    </a>
                    <img src="{$wb_img_path}tp.gif" width="8" height="1" border="0"/>
                    <a href="javascript:mgt_rpt.dl_rpt('{@spec_id}','{$tpl_type}','{$xsl_dl}')" class="Text btn wzb-btn-blue">
                        <xsl:value-of select="$lab_export"/>
                    </a>
                    <xsl:if test="@ent_id = $cur_ent_id">
                        <img src="{$wb_img_path}tp.gif" width="8" height="1" border="0"/>
                        <a href="javascript:mgt_rpt.upd_rpt_prep('{@spec_id}','{@template_id}','{$tpl_type}','{$xsl_get}')" class="Text btn wzb-btn-blue">
                            <xsl:value-of select="$lab_edit"/>
                        </a>
                    </xsl:if>
                    <xsl:if test="@ent_id = $cur_ent_id">
                        <img src="{$wb_img_path}tp.gif" width="8" height="1" border="0"/>
                        <a href="javascript:mgt_rpt.del_rpt('{@spec_id}','{$wb_lang}')" class="Text btn wzb-btn-blue">
                            <xsl:value-of select="$lab_delete"/>
                        </a>
                    </xsl:if>
                </td>
            </xsl:if>
            <td width="20%">
                <img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
            </td>
        </tr>
    </xsl:template>
    <!-- =============================================================== -->
</xsl:stylesheet>
