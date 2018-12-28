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
	<xsl:variable name="item_type_list" select="/report/report_body/meta/item_type_list"/>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_tc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_tc')"/>
	<xsl:variable name="lab_train_end_date" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '711')"/>
	<xsl:variable name="lab_train_type" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'wb_imp_tp_plan_type')"/>
	<xsl:variable name="lab_train_scope" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '713')"/>
	<xsl:variable name="lab_sort_order" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '321')"/>
	<xsl:variable name="lab_ascend_order" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '793')"/>
	<xsl:variable name="lab_descend_order" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '794')"/>
	<xsl:variable name="lab_all_train" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '64')"/>
	<xsl:variable name="lab_plan_train" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '714')"/>
	<xsl:variable name="lab_unplan_train" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '715')"/>
	<xsl:variable name="lab_g_form_btn_view_rpt" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '588')"/>
	<xsl:variable name="lab_g_form_btn_save" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '590')"/>
	<xsl:variable name="lab_g_form_btn_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '329')"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/>
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
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			goldenman = new wbGoldenMan;
			mgt_rpt = new wbManagementReport;
			
			var frm;
			function init() {
				frm = document.frmXml;
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
				<input type="hidden" name="spec_name" value=""/>
				<input type="hidden" name="spec_value" value=""/>
				<input type="hidden" name="usr_ent_id" value=""/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="window_name" value=""/>
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
			</xsl:apply-templates>
			<xsl:choose>
				<xsl:when test="$spec_ent_id = '0'  and $spec_id!='0'">
					<xsl:if test="count(spec/data_list/data[@name = 'content_lst']) != 0">
						<input type="hidden" name="hidden_spec_name" value="content_lst"/>
						<input type="hidden" name="hidden_spec_value">
							<xsl:attribute name="value"><xsl:for-each select="spec/data_list/data[@name = 'content_lst']"><xsl:value-of select="@value"/><xsl:if test="position()!= last()">~</xsl:if></xsl:for-each></xsl:attribute>
						</input>
					</xsl:if>
					<xsl:if test="count(spec/data_list/data[@name = 'run_content_lst']) != 0">
						<input type="hidden" name="hidden_spec_name" value="run_content_lst"/>
						<input type="hidden" name="hidden_spec_value">
							<xsl:attribute name="value"><xsl:for-each select="spec/data_list/data[@name = 'run_content_lst']"><xsl:value-of select="@value"/><xsl:if test="position()!= last()">~</xsl:if></xsl:for-each></xsl:attribute>
						</input>
					</xsl:if>
					<xsl:if test="count(spec/data_list/data[@name = 'itm_content_lst']) != 0">
						<input type="hidden" name="hidden_spec_name" value="itm_content_lst"/>
						<input type="hidden" name="hidden_spec_value">
							<xsl:attribute name="value"><xsl:for-each select="spec/data_list/data[@name = 'itm_content_lst']"><xsl:value-of select="@value"/><xsl:if test="position()!= last()">~</xsl:if></xsl:for-each></xsl:attribute>
						</input>
					</xsl:if>
					<xsl:if test="count(spec/data_list/data[@name = 'usr_content_lst']) != 0">
						<input type="hidden" name="hidden_spec_name" value="usr_content_lst"/>
						<input type="hidden" name="hidden_spec_value">
							<xsl:attribute name="value"><xsl:for-each select="spec/data_list/data[@name = 'usr_content_lst']"><xsl:value-of select="@value"/><xsl:if test="position()!= last()">~</xsl:if></xsl:for-each></xsl:attribute>
						</input>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
			<!--sort para-->
			<xsl:apply-templates select="spec" mode="sort">
				<xsl:with-param name="lab_rpt_name" select="$lab_rpt_name"/>
				<xsl:with-param name="lab_rpt_name_length" select="$lab_rpt_name_length"/>
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
							<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.get_rpt_adv(document.frmXml,'<xsl:value-of select="$rpt_type"/>','<xsl:value-of select="$template_id"/>','<xsl:value-of select="$report_body/template/xsl_list/xsl[@type='execute']/."/>','false','',"<xsl:value-of select="$wb_lang"/>")</xsl:with-param>
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
										<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.upd_rpt_exec(document.frmXml,'<xsl:value-of select="$template_id"/>','<xsl:value-of select="$spec_id"/>','<xsl:value-of select="$ent_id"/>','<xsl:value-of select="$rpt_type"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
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
		<!--== TC ==-->
		<tr>
			<td width="20%" align="right" valign="top" class="wzb-form-label">
					<span class="wzb-form-star">*</span>
					<span class="TitleText">
						<xsl:value-of select="$lab_tc"/>
						<xsl:text>：</xsl:text>
					</span>
			</td>
			<td width="80%" class="wzb-form-control">
				<xsl:call-template name="wb_goldenman">
					<xsl:with-param name="frm">document.frmXml</xsl:with-param>
					<xsl:with-param name="name">int_training_provider</xsl:with-param>
					<xsl:with-param name="max_size">1</xsl:with-param>
					<xsl:with-param name="field_name">training_center</xsl:with-param>
					<xsl:with-param name="tree_type">training_center</xsl:with-param>
					<xsl:with-param name="select_type">2</xsl:with-param>
					<xsl:with-param name="pick_root">0</xsl:with-param>
					<xsl:with-param name="single_option_text">
						<xsl:apply-templates select="$data_list/data[@name='tcr_id']"/>
					</xsl:with-param>
					<xsl:with-param name="single_option_value">
						<xsl:value-of select="$data_list/data[@name='tcr_id']/@value"/>
					</xsl:with-param>
				</xsl:call-template>
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
		<!--== Train Type ==-->
		<tr>
			<td width="20%" align="right" valign="top" class="wzb-form-label">
					<span class="wzb-form-star">*</span>
					<span class="TitleText">
						<xsl:value-of select="$lab_train_type"/>
						<xsl:text>：</xsl:text>
					</span>
			</td>
			<td width="80%" class="wzb-form-control">
				<table cellpadding="0" cellspacing="0" width="100%" border="0">
					<tr>
						<xsl:for-each select="$item_type_list/item_type[not(@group_ind)]">
							<xsl:variable name="id" select="@id"/>
							<xsl:if test="@id !='SELFSTUDY|-|EXAM' and @id !='CLASSROOM|-|EXAM'"> 
							<td>
								<xsl:attribute name="width"><xsl:choose><xsl:when test="position() mod 2 = 0">75%</xsl:when><xsl:otherwise>25%</xsl:otherwise></xsl:choose></xsl:attribute>
								<xsl:if test="position() = last()">
									<xsl:attribute name="colspan"><xsl:value-of select="(2-(position() mod  2))+1"/></xsl:attribute>
								</xsl:if>
								<span class="Text">
									<label for="label_{@id}">
										<xsl:variable name="_my_name" select="."/>
										<input type="checkbox" name="itm_type" value="{@id}" id="label_{@id}">
											<xsl:choose>
												<xsl:when test="not($data_list)">
													<xsl:attribute name="checked">checked</xsl:attribute>
												</xsl:when>	
												<xsl:otherwise>
													<xsl:if test="$data_list/data[@name = 'itm_type' and @value = $id]">
														<xsl:attribute name="checked">checked</xsl:attribute>
													</xsl:if>
												</xsl:otherwise>
											</xsl:choose>
										</input>
										<xsl:call-template name="get_ity_title">
											<xsl:with-param name="dummy_type" select="@title"/>
										</xsl:call-template>
									</label>
								</span>
							</td>
							</xsl:if>
							<xsl:if test="position() mod 2 = 0 and position() != last()">
								<xsl:text disable-output-escaping="yes">&lt;/tr&gt;&lt;tr&gt;</xsl:text>
							</xsl:if>
						</xsl:for-each>
					</tr>
				</table>
			</td>
		</tr>
		<!--== Train Scope ==-->
		<xsl:if test="$has_plan_mgt='true'">
			<tr>
				<td width="20%" align="right" valign="top" class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_train_scope"/>
							<xsl:text>：</xsl:text>
						</span>
				</td>
				<td width="80%" class="wzb-form-control">
					<table cellpadding="0" cellspacing="0" width="100%" border="0">
						<tr>
							<td width="1">
								<input id="all_train_scope" value="ALL" name="train_scope" type="radio">
									<xsl:if test="not($data_list) or $data_list/data[@name='train_scope' and @value = 'ALL']">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
							</td>
							<td><label for="all_train_scope"><span class="Text"><xsl:value-of select="$lab_all_train"/></span></label></td>
						</tr>
						<tr>
							<td>
								<input id="plan_train_scope" value="PLAN" name="train_scope" type="radio">
									<xsl:if test="$data_list/data[@name='train_scope' and @value = 'PLAN']">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
							</td>
							<td><label for="plan_train_scope"><span class="Text"><xsl:value-of select="$lab_plan_train"/><xsl:text>：</xsl:text></span></label></td>
						</tr>
						<tr>
							<td>
								<input id="unplan_train_scope" value="UNPLAN" name="train_scope" type="radio">
									<xsl:if test="$data_list/data[@name='train_scope' and @value = 'UNPLAN']">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
							</td>
							<td><label for="unplan_train_scope"><span class="Text"><xsl:value-of select="$lab_unplan_train"/><xsl:text>：</xsl:text></span></label></td>
						</tr>
					</table>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ===========================sort order section ==============================================-->
	<xsl:template match="spec" mode="sort">
		<xsl:variable name="sort_value" select="data_list/data[@name='sort_col']/@value"/>
		<xsl:variable name="index" select="data_list/data[@name='sort_order']/@value"/>
		<xsl:variable name="itemno" select="data_list/data[@name='page_size']/@value"/>
		<tr>
			<td width="20%" align="right" class="wzb-form-label">
				<span class="wzb-form-star">*</span>
				<span class="TitleText">
					<xsl:value-of select="$lab_sort_order"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="20%" class="wzb-form-control">
				<!-- item中的字段-->
				<select name="sort_col" class="select">
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
				<!--其他隐藏域-->
				<xsl:for-each select="/report/report_body/display_option/user/object_view/attribute[@sortable='yes']">
					<xsl:variable name="curr" select="."/>
					<input type="hidden">
						<xsl:attribute name="name"><xsl:value-of select="$curr"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:choose><xsl:when test="$curr='usr_id'"><xsl:value-of select="$lab_login_id"/></xsl:when><xsl:when test="$curr='usr_display_bil'"><xsl:value-of select="$lab_dis_name"/></xsl:when><!--更多的判断--><!--<xsl:when test=""></xsl:when>--><xsl:otherwise> </xsl:otherwise></xsl:choose></xsl:attribute>
					</input>
				</xsl:for-each>
				<!-- ================================DENNIS END============================================= -->
				<select name="sort_order" class="select">
					<option value="asc">
						<xsl:if test="$index = 'asc'">
							<xsl:attribute name="selected">selected</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_ascend_order"/>
					</option>
					<option value="desc">
						<xsl:if test="$index = 'desc'">
							<xsl:attribute name="selected">selected</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_descend_order"/>
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
	<!-- =============================================================== -->
	<xsl:template match="data">
		<xsl:variable name="name" select="@name"/>
		<xsl:variable name="value" select="@value"/>
		<xsl:value-of select="/report/report_body/presentation/data[@name=$name and @value = $value]/@display"/>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
