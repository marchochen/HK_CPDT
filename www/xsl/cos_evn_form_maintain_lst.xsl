<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="share/div_tree_share.xsl"/>
	<xsl:import href="share/gen_tree_js.xsl"/>
	<!-- cust utils -->
	<!-- -->
	<xsl:output indent="yes"/>
	<xsl:variable name="page_size" select="/cosevaluation/evaluation_list/cwPage/@page_size"/>
	<xsl:variable name="cur_page" select="/cosevaluation/evaluation_list/cwPage/@page"/>
	<xsl:variable name="page_total" select="/cosevaluation/evaluation_list/cwPage/@total"/>
	
	<xsl:variable name="mod_cnt" select="count(cosevaluation/evaluation_list/module_list/module)"/>
	<xsl:variable name="question_list_node" select="cosevaluation/question_list"/>
	<xsl:variable name="cur_usr">
		<xsl:value-of select="cosevaluation/meta/cur_usr/@id"/>
	</xsl:variable>
	<xsl:variable name="is_show_all" select="/cosevaluation/evaluation_list/module_list/show_all/text()"/>
	<xsl:variable name="tc_enabled" select="/cosevaluation/meta/tc_enabled"/>
	<xsl:variable name="cur_tcr_id" select="//cur_training_center/@id"/>
	<!-- ================================================================ -->
	<xsl:template match="/">
		<xsl:apply-templates select="cosevaluation"/>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="cosevaluation">
		<html>
			<xsl:call-template name="draw_header"/>
			<xsl:call-template name="draw_body"/>
		</html>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_header">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_cos_evaluation.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_assessment.js"/>
			<script language="Javascript"><![CDATA[
			var coseval = new wbCosEvaluation;
			var module_lst = new wbModule;
			asm = new wbAssessment;
			function show_content(tcr_id) {
				module_lst.show_mgt_content(tcr_id,1,']]><xsl:value-of select="$page_size"/><![CDATA[');
			}
			
			function load_tree() {
				if (frmXml.tc_enabled_ind) {
					page_onload(250);
				}
			}
		]]></script>
		</head>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_body">
		<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0" onload="load_tree();">
			<form name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_main_eval">課程評估問卷管理</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_attempt">作答次數</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_active">已發佈</xsl:with-param>
			<xsl:with-param name="lab_inactive">未發佈</xsl:with-param>
			<xsl:with-param name="lab_no_eval">沒有問卷</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_question">題目</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_schedule">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">新增</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_preview">預覽</xsl:with-param>
			<xsl:with-param name="lab_cos_evn_by_tcr">按培訓中心顯示</xsl:with-param>
			<xsl:with-param name="lab_root_training_center">所有培訓中心</xsl:with-param>
			<xsl:with-param name="lab_cur_tcr">當前培訓中心</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_main_eval">课程评估问卷管理</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_attempt">提交次数</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_active">已发布</xsl:with-param>
			<xsl:with-param name="lab_inactive">未发布</xsl:with-param>
			<xsl:with-param name="lab_no_eval">没有评估问卷</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_question">题目</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_schedule">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">添加</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">删除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_preview">预览</xsl:with-param>
			<xsl:with-param name="lab_cos_evn_by_tcr">按培训中心显示</xsl:with-param>
			<xsl:with-param name="lab_root_training_center">所有培训中心</xsl:with-param>
			<xsl:with-param name="lab_cur_tcr">当前培训中心</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_main_eval">Course evaluation builder</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_attempt">Attempts</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_active">Published</xsl:with-param>
			<xsl:with-param name="lab_inactive">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_no_eval">No evaluation here</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_question">Questions</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_schedule">Edit</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">Add</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_preview">Preview</xsl:with-param>
			<xsl:with-param name="lab_cos_evn_by_tcr">Show by training center</xsl:with-param>
			<xsl:with-param name="lab_root_training_center">All training center</xsl:with-param>
			<xsl:with-param name="lab_cur_tcr">Current training center</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="content">
		<xsl:param name="lab_main_eval"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_attempt"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_active"/>
		<xsl:param name="lab_inactive"/>
		<xsl:param name="lab_no_eval"/>
		<xsl:param name="lab_g_txt_btn_edit_question"/>
		<xsl:param name="lab_g_txt_btn_edit_schedule"/>
		<xsl:param name="lab_g_txt_btn_add"/>
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_g_txt_btn_preview"/>
		<xsl:param name="lab_cos_evn_by_tcr"/>
		<xsl:param name="lab_root_training_center"/>
		<xsl:param name="lab_cur_tcr"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_main_eval"/>
		</xsl:call-template>
		<!-- tree nav -->
		<xsl:variable name="cur_tcr_title">
			<xsl:choose>
				<xsl:when test="$cur_tcr_id != 0"><xsl:value-of select="//cur_training_center/title/text()"/></xsl:when>
				<xsl:otherwise><xsl:value-of select="$lab_root_training_center"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<table>
			<tr>
				<td width="40%">
					<xsl:if test="$tc_enabled='true'">
						<xsl:call-template name="div_tree">
							<xsl:with-param name="lab_cur_tcr" select="$lab_cur_tcr"/>
							<xsl:with-param name="lab_root_training_center" select="$lab_root_training_center"/>
							<xsl:with-param name="title" select="$cur_tcr_title"/>
						</xsl:call-template>
						<input type="hidden" name="tc_enabled_ind"/>
					</xsl:if>
				</td>
				<td width="60%">
					<!-- tree nav end -->
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="table_style">border-bottom:none;</xsl:with-param>
						<xsl:with-param name="extra_td">
							<td align="right">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_g_txt_btn_add"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:coseval.ins_cos_evn_prep(<xsl:value-of select="$cur_tcr_id" />)</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								</xsl:call-template>
							<!--按培训中心显示-->
								<xsl:if test="$tc_enabled = 'true' and $is_show_all = 'true'">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_cos_evn_by_tcr"/></xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">#</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
										<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									</xsl:call-template>
								</xsl:if>
							</td>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		<xsl:choose>
			<xsl:when test="$mod_cnt='0'">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_eval"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="table wzb-ui-table">
					<tr class="SecBg wzb-ui-table-head">
						<td>
							<span class="TitleText">
								<xsl:value-of select="$lab_title"/>
							</span>
						</td>
						<td align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_status"/>
							</span>
						</td>
						<td>
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<xsl:apply-templates select="evaluation_list/module_list/module">
						<xsl:with-param name="lab_active" select="$lab_active"/>
						<xsl:with-param name="lab_inactive" select="$lab_inactive"/>
						<xsl:with-param name="lab_g_txt_btn_edit_question" select="$lab_g_txt_btn_edit_question"/>
						<xsl:with-param name="lab_g_txt_btn_edit_schedule" select="$lab_g_txt_btn_edit_schedule"/>
						<xsl:with-param name="lab_g_txt_btn_remove" select="$lab_g_txt_btn_remove"/>
						<xsl:with-param name="lab_g_txt_btn_preview" select="$lab_g_txt_btn_preview"/>
					</xsl:apply-templates>
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="page_size_name">page_size</xsl:with-param>
					<xsl:with-param name="cur_page_name">cur_page</xsl:with-param>
					<xsl:with-param name="total" select="$page_total"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="module">
		<xsl:param name="lab_active"/>
		<xsl:param name="lab_inactive"/>
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_g_txt_btn_preview"/>
		<xsl:param name="lab_g_txt_btn_edit_schedule"/>
		<xsl:param name="lab_g_txt_btn_edit_question"/>
		<xsl:variable name="mod_id" select="@id"/>
		<tr>
			<xsl:attribute name="class"><xsl:choose><xsl:when test="position() mod 2 = 1">RowsOdd</xsl:when><xsl:otherwise>RowsEven</xsl:otherwise></xsl:choose></xsl:attribute>
			<td width="50%">
				<span class="Text">
					<xsl:value-of select="title"/>
				</span>
			</td>
			<td align="center" width="10%">
				<span class="Text">
					<xsl:choose>
						<xsl:when test="@status='ON'">
							<xsl:value-of select="$lab_active"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_inactive"/>
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</td>
			<td align="right" valign="middle" width="40%">
				<!-- -->
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_preview"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.preview_exec('<xsl:value-of select="@type"/>',<xsl:value-of select="$mod_id"/>,'svy_player.xsl' ,'')</xsl:with-param>
				</xsl:call-template>
				<!-- -->
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_edit_schedule"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:coseval.upd_cos_evn_prep('<xsl:value-of select="@id"/>')</xsl:with-param>
				</xsl:call-template>
				<!-- -->
				<xsl:choose>
					<xsl:when test="@public_used != 'true'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_edit_question"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:module_lst.upd_svy_prep(<xsl:value-of select="$mod_id"/>,'','<xsl:value-of select="$cur_usr"/>','<xsl:value-of select="@status"/>','<xsl:value-of select="@attempted"/>','SVY','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="@public_used != 'true'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_remove"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:coseval.del_cos_evn(<xsl:value-of select="$mod_id"/>,'<xsl:value-of select="@timestamp"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
				</xsl:choose>
				<!-- -->
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '692')"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:asm.export_evn('<xsl:value-of select="@id"/>')</xsl:with-param>
				</xsl:call-template>
		</td>
		</tr>
	</xsl:template>
	<!-- ================================================================ -->
</xsl:stylesheet>
