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
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
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
	<xsl:variable name="tc_enabled" select="/report/meta/tc_enabled"/>
	<xsl:variable name="profile_attributes" select="/report/meta/profile_attributes"/>
	<!-- =============================================================== -->
	<xsl:variable name="lab_include_del_usr" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '832')"/>
	<xsl:variable name="lab_by_all_group_in_my_tc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '834')"/>
	<xsl:variable name="lab_is_detail_jifen" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '833')"/>
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
			
			function detail_change(frm, obj){
				if(obj.value == 0){
					var pos = false;
				}else if (obj.value == 1){
					var pos = true;
				}
				var radio = document.getElementsByName("show_usg_only"); 
				for(var i = 0; i < radio.length; i++){ 
					radio[i].disabled = pos; 
				}
			}
			
			function usr_change(frm, obj){
				if(obj.value == 0){
					var pos = false;
					var neg = true;
				}else if (obj.value == 1){
					var pos = true;
					var neg = false;
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

				if(frm.all_usg_ind && frm.all_usg_ind[0].checked){
					usr_change(frm,frm.all_usg_ind[0])
				}else{
					usr_change(frm,frm.all_usg_ind[1])
				}

				if(frm.is_detail_ind && frm.is_detail_ind[0].checked){
					detail_change(frm, frm.is_detail_ind[0])
				}else{
					detail_change(frm, frm.is_detail_ind[1])
				}
			}				
			
			function redothis(){
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
				<input type="hidden" name="rpt_type" value="{$rpt_type}"/>
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
			<xsl:with-param name="lab_instruction">你可以透過選擇以下屬性建立自己的報告，按<b>執行</b>可以直接查看報告內容。按<b>儲存</b>可以將指定的查詢條件儲存為設定報告。</xsl:with-param>
			<xsl:with-param name="lab_edit_instruction">選擇以下屬性編輯報告，按<b>確定</b>儲存報告。</xsl:with-param>
			<xsl:with-param name="lab_standard_rpt_inst">指定報告標準並按<b>執行</b>瀏覽你的下屬的學習報告。</xsl:with-param>
			<!-- form text -->
			<xsl:with-param name="lab_rpt_name">報告名稱</xsl:with-param>
			<xsl:with-param name="lab_rpt_name_length">（不可超過200位數）</xsl:with-param>
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn_group">用戶組</xsl:with-param>
			<xsl:with-param name="lab_by_selected_usg">指定用戶組</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_show_usr_grp_only">只顯示用戶組積分統計信息</xsl:with-param>
			<xsl:with-param name="lab_show_all">顯示用戶組積分統計信息以及其下面的用戶的積分統計信息</xsl:with-param>
			<xsl:with-param name="lab_enollment_date">獲得日期</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<!-- content attribute text -->
			<xsl:with-param name="lab_content">報告欄</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">儲存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_view_rpt">執行</xsl:with-param>
			<xsl:with-param name="lab_by_all_group">所有用戶組</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="report_body">
			<!-- header text -->
			<xsl:with-param name="lab_instruction">您可以通过选择以下属性定制自己的报告。点击“查看”可以直接查看报告内容。点击“保存”可以将指定的查询条件保存为新的模板。</xsl:with-param>
			<xsl:with-param name="lab_edit_instruction">选择以下属性编辑报告，点击“确定”保存报告。</xsl:with-param>
			<xsl:with-param name="lab_standard_rpt_inst">指定报告查看标准，并点击 <b>查看</b> 浏览学习报告</xsl:with-param>
			<!-- form text -->
			<xsl:with-param name="lab_rpt_name">报告名称</xsl:with-param>
			<xsl:with-param name="lab_rpt_name_length">（长度不超过200）</xsl:with-param>
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn_group">用户组</xsl:with-param>
			<xsl:with-param name="lab_by_selected_usg">指定用户组</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_show_usr_grp_only">只显示用户组积分统计信息</xsl:with-param>
			<xsl:with-param name="lab_show_all">显示用户组积分统计信息以及其下面的用户的积分统计信息</xsl:with-param>
			<xsl:with-param name="lab_enollment_date">获得日期</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<!-- content attribute text -->
			<xsl:with-param name="lab_content">报告栏</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_view_rpt">查看</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">保存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">删除</xsl:with-param>
			<xsl:with-param name="lab_by_all_group">所有用户组</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="report_body">
			<!-- header text -->
			<xsl:with-param name="lab_instruction">Build your custom report by selecting specific criteria and report data. Click <b>Run</b> to view the report. Click <b>Save</b> to save as report template.</xsl:with-param>
			<xsl:with-param name="lab_edit_instruction">Modify the report template and click <b>Run</b> to view the report. Click <b>OK</b> to save any changes in the template. </xsl:with-param>
			<xsl:with-param name="lab_standard_rpt_inst">Specify the report criteria and click <b>Run</b> to view the report</xsl:with-param>
			<!-- form text -->
			<xsl:with-param name="lab_rpt_name">Template name</xsl:with-param>
			<xsl:with-param name="lab_rpt_name_length">(Not more than 200 characters.)</xsl:with-param>
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn_group">User group</xsl:with-param>
			<xsl:with-param name="lab_by_selected_usg">Only learners in these groups</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
			<xsl:with-param name="lab_show_usr_grp_only">Show summary statistics of User groups only</xsl:with-param>
			<xsl:with-param name="lab_show_all">Show detailed statistics of each user account (list by User group)</xsl:with-param>
			<xsl:with-param name="lab_enollment_date">Obtain date</xsl:with-param>
			<xsl:with-param name="lab_to">to</xsl:with-param>
			<xsl:with-param name="lab_item_page">Groups per page</xsl:with-param>
			<!-- content attribute text -->
			<xsl:with-param name="lab_content">Report columns</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">Save</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_view_rpt">Run</xsl:with-param>
			<xsl:with-param name="lab_by_all_group">All user group</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ===========================report _body==================================== -->
	<xsl:template match="report_body">
		<!-- header text -->
		<xsl:param name="lab_instruction"/>
		<xsl:param name="lab_edit_instruction"/>
		<xsl:param name="lab_standard_rpt_inst"/>
		<!-- form text -->
		<xsl:param name="lab_rpt_name"/>
		<xsl:param name="lab_rpt_name_length"/>
		<!-- search criteria text -->
		<xsl:param name="lab_lrn_group"/>
		<xsl:param name="lab_by_selected_usg"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_show_usr_grp_only"/>
		<xsl:param name="lab_show_all"/>
		<xsl:param name="lab_enollment_date"/>
		<xsl:param name="lab_to"/>
		<!-- content attribute text -->
		<xsl:param name="lab_content"/>
		<!-- button text -->
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_remove"/>
		<xsl:param name="lab_g_form_btn_save"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_view_rpt"/>
		<xsl:param name="lab_by_all_group"/>
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
					<xsl:with-param name="rte_type" select="$rpt_type"/>
				</xsl:call-template>
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
				<xsl:with-param name="lab_lrn_group" select="$lab_lrn_group"/>
				<xsl:with-param name="lab_by_selected_usg" select="$lab_by_selected_usg"/>
				<xsl:with-param name="lab_to" select="$lab_to"/>
				<xsl:with-param name="lab_yes" select="$lab_yes"/>
				<xsl:with-param name="lab_no" select="$lab_no"/>
				<xsl:with-param name="lab_show_usr_grp_only" select="$lab_show_usr_grp_only"/>
				<xsl:with-param name="lab_show_all" select="$lab_show_all"/>
				<xsl:with-param name="lab_by_all_group" select="$lab_by_all_group"/>
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
				<xsl:otherwise>
					<xsl:apply-templates select="display_option">
						<xsl:with-param name="lab_content" select="$lab_content"/>
						<xsl:with-param name="lab_show_usr_grp_only" select="$lab_show_usr_grp_only"/>
						<xsl:with-param name="lab_show_all" select="$lab_show_all"/>
					</xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
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
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_show_usr_grp_only"/>
		<xsl:param name="lab_show_all"/>
		<xsl:param name="lab_enollment_date"/>
		<xsl:param name="lab_lrn_group"/>
		<xsl:param name="lab_by_selected_usg"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_by_all_group"/>
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
				<td width="80%"  class="wzb-form-control">
					<span class="Text">
						<input type="text" name="rsp_title" value="{title}" size="30" style="width:350px;" class="wzb-inputText" maxlength="200"/>
						<br/>
						<xsl:value-of select="$lab_rpt_name_length"/>
					</span>
				</td>
			</tr>
		</xsl:if>
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
			<td width="80%"  class="wzb-form-control">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<!--All User Group-->
					<tr>
						<td valign="top">
							<span>
								<input type="radio" name="all_usg_ind" value="1" id="rdo_sel_by_all_grp" onclick="usr_change(document.frmXml,this);">
									<xsl:if test="$report_body/spec/data_list/data[@name='all_usg_ind']/@value = '1' or not($report_body/spec/data_list/data[@name='all_usg_ind']/@value)">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
							</span>
							<label for="rdo_sel_by_all_grp">
								<xsl:choose>
									<xsl:when test="$tc_enabled = 'true'">
										<span class="Text">
											<xsl:value-of select="$lab_by_all_group_in_my_tc"/>
										</span>
									</xsl:when>
									<xsl:otherwise>
										<span class="Text">
											<xsl:value-of select="$lab_by_all_group"/>
										</span>
									</xsl:otherwise>
								</xsl:choose>
							</label>
						</td>
					</tr>
					<!--Select User Group-->
					<tr>
						<td valign="top">
							<span>
								<input type="radio" name="all_usg_ind" value="0" id="rdo_sel_by_selected_usg" onclick="usr_change(document.frmXml,this);">
									<xsl:if test="$report_body/spec/data_list/data[@name='all_usg_ind']/@value = '0'">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
							</span>
							<label for="rdo_sel_by_selected_usg">
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
			<td width="20%" align="right"  class="wzb-form-label">
					<input type='hidden' name="special_time_title" value='{$lab_enollment_date}'/>
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
		<!--Include Deleted User-->
		<tr>
			<td width="20%" align="right"  class="wzb-form-label">
					<span class="wzb-form-star">*</span>
					<span class="TitleText">
						<xsl:value-of select="$lab_include_del_usr"/>
						<xsl:text>：</xsl:text>
					</span>
			</td>
			<td width="80%"  class="wzb-form-control">
					<label for="include_yes">
						<input type="radio" name="include_del_usr_ind" value="1" id="include_yes">
							<xsl:if test="$report_body/spec/data_list/data[@name='include_del_usr_ind' and @value='1']">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$lab_yes"/>
						</input>
					</label>
					<label for="include_no"  style="margin-left:15px;">
						<input type="radio" name="include_del_usr_ind" value="0" id="include_no">
							<xsl:if test="$report_body/spec/data_list/data[@name='include_del_usr_ind' and @value='0'] or not(/report/report_body/spec/data_list) ">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$lab_no"/>
						</input>
					</label>
			</td>
		</tr>
		<!--Is Detail jifen-->
		<tr>
			<td width="20%" align="right"  class="wzb-form-label">
					<span class="wzb-form-star">*</span>
					<span class="TitleText">
						<xsl:value-of select="$lab_is_detail_jifen"/>
						<xsl:text>：</xsl:text>
					</span>
			</td>
			<td width="80%" class="wzb-form-control">
				<label for="detail_yes">
					<input type="radio" name="is_detail_ind" value="1" id="detail_yes" onclick="detail_change(document.frmXml,this);" >
						<xsl:if test="$report_body/spec/data_list/data[@name='is_detail_ind' and @value='1']">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_yes"/>
					</input>
				</label>
				<label for="detail_no"  style="margin-left:15px;">
					<input type="radio" name="is_detail_ind" value="0" id="detail_no" onclick="detail_change(document.frmXml,this);">
						<xsl:if test="$report_body/spec/data_list/data[@name='is_detail_ind' and @value='0'] or not(/report/report_body/spec/data_list) ">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_no"/>
					</input>
				</label>
			</td>
		</tr>
	</xsl:template>
	<!--==================================================================-->
	<!--display option-->
	<xsl:template match="display_option">
		<xsl:param name="lab_content"/>
		<xsl:param name="lab_show_usr_grp_only"/>
		<xsl:param name="lab_show_all"/>
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
							<td>
								<input id="display" value="true" name="show_usg_only" type="radio">
									<xsl:if test="/report/report_body/spec/data_list/data[@name='show_usg_only' and @value='true'] or not(/report/report_body/spec/data_list/data[@name='show_usg_only'])">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
								<label for="display">
									<span class="Text">
										<xsl:value-of select="$lab_show_usr_grp_only"/>
									</span>
								</label>
							</td>
						</tr>
						<tr>
							<td>
								<input id="undisplay" value="false" name="show_usg_only" type="radio">
									<xsl:if test="/report/report_body/spec/data_list/data[@name='show_usg_only' and @value!='true']">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
								<label for="undisplay">
									<span class="Text">
										<xsl:value-of select="$lab_show_all"/>
									</span>
								</label>
							</td>
						</tr>
					</table>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data">
		<option value="{@value}">
			<xsl:variable name="name" select="@name"/>
			<xsl:variable name="value" select="@value"/>
			<xsl:value-of select="/report/report_body/presentation/data[@name=$name and @value = $value]/@display"/>
		</option>
	</xsl:template>
</xsl:stylesheet>
