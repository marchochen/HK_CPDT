<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0" xmlns:java="http://xml.apache.org/xalan/java"
	exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
		
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:import href="share/itm_nav_share.xsl"/>

	<xsl:output indent="yes"/>
	<!-- =========================================================================== -->
	<xsl:variable name="cur_page" select="/applyeasy/pagination/@cur_page"/>
	<xsl:variable name="total" select="/applyeasy/pagination/@total_rec"/>
	<xsl:variable name="page_size" select="/applyeasy/pagination/@page_size"/>
	<xsl:variable name="order_by" select="/applyeasy/pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="/applyeasy/pagination/@sort_order"/>
	<xsl:variable name="sort_order">
		<xsl:choose>
			<xsl:when test="$cur_order = 'ASC' or $cur_order = 'asc' ">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:variable name="itm_title" select="/applyeasy/itm_action_nav/@itm_title"/>
	<xsl:variable name="mod_type" select="/applyeasy/mod_type/text()"/>
	<xsl:variable name="itm_id" select="/applyeasy/itm_action_nav/@itm_id"/>
	<xsl:variable name="cos_res_id" select="/applyeasy/itm_action_nav/@cos_res_id"/>
	<xsl:variable name="tst_count" select="/applyeasy/module_count/@TST"/>
	<xsl:variable name="svy_count" select="/applyeasy/module_count/@SVY"/>
	<xsl:variable name="ass_count" select="/applyeasy/module_count/@ASS"/>
	<xsl:variable name="dxt_count" select="/applyeasy/module_count/@DXT"/>
	
	<xsl:variable name="label_core_training_management_21" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_21')"/>
	<xsl:variable name="label_core_training_management_22" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_22')"/>
	<xsl:variable name="label_core_training_management_28" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_28')"/>
	<xsl:variable name="lab_eva_report" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_393')" />
	<xsl:variable name="label_core_training_management_20" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_20')"/>
	<xsl:variable name="label_core_training_management_250" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_250')"/>
	<xsl:variable name="label_core_training_management_394" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_394')"/>
	<xsl:variable name="label_core_training_management_395" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_395')"/>
	<xsl:variable name="label_core_training_management_396" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_396')"/>
	<xsl:variable name="label_core_training_management_397" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_397')"/>
	<xsl:variable name="label_core_training_management_398" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_398')"/>
	<xsl:variable name="label_core_training_management_399" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_399')"/>
	<xsl:variable name="label_core_training_management_400" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_400')"/>
	<xsl:variable name="label_core_training_management_401" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_401')"/>
	<xsl:variable name="label_core_training_management_402" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_402')"/>
	<xsl:variable name="label_core_training_management_403" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_403')"/>
	<xsl:variable name="label_core_training_management_404" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_404')"/>
	<!-- =========================================================================== -->
	<xsl:template match="/">
		<html>
			<head>
				<title>
					<xsl:value-of select="$wb_wizbank" />
				</title>
				<meta http-equiv="X-UA-Compatible" content="IE=edge" />
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js" />
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js" />
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js" />
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_assignment.js"/>
				<script type="text/javascript"><![CDATA[
					itm = new wbItem;
					module_lst = new wbModule;	
					ass = new wbAssignment;
					
					var type = "]]><xsl:value-of select="$mod_type"/><![CDATA[";
					$(function(){
						$("#"+type).addClass("active").siblings().removeClass("active");
					});
					
					function exportModuleResultReport(mod_id){
						window.location.href = wb_utils_controller_base + "report/export/eaxmModuleResultStatistic?mod_id=" + mod_id;
					}
					
					function exportQuestionReport(mod_id){
						var win = "rpt_win"+new Date().getTime();
						var itm_id = "]]><xsl:value-of select="$itm_id"/><![CDATA[";
						var url = wb_utils_disp_servlet_url+"?env=wizb&module=report.ReportModule&cmd=get_rpt&rpt_type=EXP_QUE_STATISTIC&window_name="+win+"&page_size=-1&download=4&itm_id="+itm_id+"&mod_id="+mod_id;
						window.location.href = url;
					}
				]]>
				</script>
				 <xsl:call-template name="new_css"/>
			 
			</head>
			<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0">
				<xsl:call-template name="content" />
			</body>
		</html>
	</xsl:template>
	<!-- =========================================================================== -->
	<xsl:template name="content">
		
		<!-- header -->
		<xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">120</xsl:with-param>
		</xsl:call-template>
		
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module"><xsl:value-of select="$belong_module"></xsl:value-of></xsl:with-param>
			<xsl:with-param name="parent_code"><xsl:value-of select="$parent_code"></xsl:value-of></xsl:with-param>
		</xsl:call-template>
		
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$itm_title"/>
			</xsl:with-param>
		</xsl:call-template>
		
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:apply-templates select="/applyeasy/item/nav/item" mode="nav">
					<xsl:with-param name="lab_run_info"><xsl:value-of select="$label_core_training_management_250" /></xsl:with-param>
				</xsl:apply-templates>
				<span class="NavLink">
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:value-of select="$lab_eva_report" />
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<!-- header end -->
		
		<xsl:choose>
			<xsl:when test="(not($tst_count) or $tst_count = 0) and (not($ass_count) or $ass_count = 0) and (not($svy_count) or $svy_count = 0)">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$label_core_training_management_404"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table>
					<tr>
						<td>
							<ul class="nav nav-tabs page-tabs" role="tablist">
								<xsl:if test="$tst_count > 0" >
									<li role="presentation" class="active" id="TST">
										<a aria-controls="userGroupLish" role="tab"
											href="javascript:itm.itm_evaluation_report({$cos_res_id},'TST')"><xsl:value-of select="$label_core_training_management_394" /></a>
									</li>
								</xsl:if>
								<xsl:if test="$ass_count > 0" >
									<li role="presentation" id="ASS">
										<a aria-controls="recycleLish" role="tab"
											href="javascript:itm.itm_evaluation_report({$cos_res_id},'ASS')"><xsl:value-of select="$label_core_training_management_398" /></a>
									</li>
								</xsl:if>
								<xsl:if test="$svy_count > 0" >
									<li role="presentation" id="SVY">
										<a aria-controls="recycleLish" role="tab"
											href="javascript:itm.itm_evaluation_report({$cos_res_id},'SVY')"><xsl:value-of select="$label_core_training_management_399" /></a>
									</li>
								</xsl:if>
							</ul>
						</td>
						<td align="right"  style="border-bottom: 1px solid #ddd;">
						</td>
					</tr>
				</table>
				
				<table class="table wzb-ui-table margin-top28">
					<tr class="wzb-ui-table-head">
						<td><xsl:value-of select="$label_core_training_management_28" /></td>
						<td><xsl:value-of select="$label_core_training_management_20" /></td>
						<xsl:if test="$mod_type != 'SVY'" >
							<td><xsl:value-of select="$label_core_training_management_395" /></td>
						</xsl:if>
						<td><xsl:value-of select="$label_core_training_management_396" /></td>
						<xsl:if test="$mod_type != 'SVY'" >
							<td><xsl:value-of select="$label_core_training_management_397" /></td>
						</xsl:if>
						<td></td>
					</tr>
					<xsl:apply-templates select="applyeasy" />
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="total" select="$total"/>
					<xsl:with-param name="cur_page_name">cur_page</xsl:with-param>
					<xsl:with-param name="page_size_name">pagesize</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	
	<xsl:template match="applyeasy">
		<xsl:for-each select="/applyeasy/module_list/module">
			<tr>
				<td>
					<xsl:value-of select="@res_title"/>
				</td>
				<td>
					<xsl:choose>
						<xsl:when test="@res_status = 'ON'"><xsl:value-of select="$label_core_training_management_21" /></xsl:when>
						<xsl:otherwise><xsl:value-of select="$label_core_training_management_22" /></xsl:otherwise>
					</xsl:choose>
				</td>
				<xsl:if test="$mod_type != 'SVY'" >
					<td>
						<xsl:value-of select="@pgr_status_all"/>
					</td>
				</xsl:if>
				<td>
					<xsl:value-of select="@pgr_status_submit"/>
				</td>
				<xsl:if test="$mod_type != 'SVY'" >
					<td>
						<xsl:value-of select="@pgr_status_not_graded"/>
					</td>
				</xsl:if>
				<td align="right">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-blue margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$label_core_training_management_400" /></xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href"><xsl:choose>
								<xsl:when test="$mod_type = 'SVY'" >Javascript:module_lst.get_subn_list('<xsl:value-of select="@mod_res_id" />',<xsl:value-of select="$cos_res_id" />);</xsl:when>
								<xsl:when test="$mod_type = 'ASS'" >Javascript:ass.view_submission(<xsl:value-of select="$cos_res_id" />,<xsl:value-of select="@mod_res_id" />,'all',1,'true');</xsl:when>
								<xsl:otherwise>Javascript:module_lst.view_submission_lst(<xsl:value-of select="$cos_res_id" />,<xsl:value-of select="@mod_res_id" />,'all');</xsl:otherwise>
							</xsl:choose></xsl:with-param>
					</xsl:call-template>
					<xsl:if test="@pgr_status_submit > 0" >
						<xsl:choose>
							<xsl:when test="$mod_type = 'SVY'" >
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-blue margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$label_core_training_management_401" /></xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">Javascript:module_lst.export_report(<xsl:value-of select="@mod_res_id"/>, "")</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:when test="$mod_type = 'TST'" >
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-blue margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$label_core_training_management_402" /></xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:exportModuleResultReport(<xsl:value-of select="@mod_res_id"/>)</xsl:with-param>
								</xsl:call-template>
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-blue margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$label_core_training_management_403" /></xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:exportQuestionReport(<xsl:value-of select="@mod_res_id"/>)</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
				</td>
			</tr>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>