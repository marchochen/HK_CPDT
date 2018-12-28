<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	
	<xsl:output indent="yes"/>
	
	<!-- ============================================================= -->
	<xsl:variable name="cur_page" select="//pagination/@cur_page"/>
	<xsl:variable name="total" select="//pagination/@total_rec"/>
	<xsl:variable name="page_size" select="//pagination/@page_size"/>
	<xsl:variable name="order_by" select="//pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="//pagination/@sort_order"/>
	
	
	<xsl:variable name="mod_cnt" select="count(CourseCriteria/marking_scheme_list/item)"/>
	<xsl:variable name="itm_id" select="/CourseCriteria/item/@id"/>
	<xsl:variable name="run_ind" select="/CourseCriteria/item/@run_ind"/>
	<xsl:variable name="ccr_id" select="/CourseCriteria/marking_scheme_list/@ccr_id"/>
	<xsl:variable name="current_role" select="/CourseCriteria/current_role"/>
	<xsl:variable name="per_sum" select="sum(CourseCriteria/marking_scheme_list/item/cmt_contri_rate)"/>
	
	<xsl:variable name="editable"><xsl:value-of select="/CourseCriteria/marking_scheme_list/editable/text()"/></xsl:variable>
	<!-- 计分规则公式说明 -->
	<xsl:variable name="label_core_training_management_320" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_320')" /> 
	<xsl:variable name="label_core_training_management_321" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_321')" />
	<xsl:variable name="label_core_training_management_322" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_322')" />
	<xsl:variable name="label_core_training_management_323" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_323')" />
	<xsl:variable name="label_core_training_management_324" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_324')" />
	<xsl:variable name="label_core_training_management_325" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_325')" />
	<xsl:variable name="label_core_training_management_326" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_326')" />
	<xsl:variable name="label_core_training_management_327" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_327')" />
	<!-- ================================================================ -->
	<xsl:template match="/">
		<xsl:apply-templates select="CourseCriteria"/>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="CourseCriteria">
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
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_scorescheme.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript"><![CDATA[
			var cmt=new wbScoreScheme;
			attn = new wbAttendance;
			itm_lst = new wbItem;
		]]></script>
		</head>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_body">
		<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0">
			<form name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="cmd" value="add_new_cmt"/>
				<input type="hidden" name="itm_id" value=""/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="module" value="course.CourseCriteriaModule"/>
				<input type="hidden" name="cmt_id_del" value=""/>
				<input type="hidden" name="ccr_id" value=""/>				
			</form>
		</body>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>訊息</xsl:with-param>
			<xsl:with-param name="lab_main_eval">計分規則</xsl:with-param>
			<xsl:with-param name="lab_content">内容</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_max_score">滿分</xsl:with-param>
			<xsl:with-param name="lab_pass_score">合格分數</xsl:with-param>
			<xsl:with-param name="lab_percent">比重（%）</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_schedule">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_percent">修改比重</xsl:with-param>
			<xsl:with-param name="lab_online_module">學習單元</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">新增計分項目</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_resort">重新排序</xsl:with-param>
			<xsl:with-param name="lab_no_eval">沒有計分項目</xsl:with-param>
			<xsl:with-param name="lab_sum">合計</xsl:with-param>	
			<xsl:with-param name="lab_attendance">考勤</xsl:with-param>
			<xsl:with-param name="total_result">共<xsl:value-of select="$mod_cnt"></xsl:value-of>條記錄</xsl:with-param>						
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_main_eval">计分规则</xsl:with-param>
			<xsl:with-param name="lab_content">内容</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_max_score">满分</xsl:with-param>
			<xsl:with-param name="lab_pass_score">合格分数</xsl:with-param>
			<xsl:with-param name="lab_percent">比重（%）</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_schedule">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_percent">修改比重</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">添加计分项目</xsl:with-param>
			<xsl:with-param name="lab_online_module">学习单元</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_resort">重新排序</xsl:with-param>
			<xsl:with-param name="lab_no_eval">没有计分项目</xsl:with-param>
			<xsl:with-param name="lab_sum">合計</xsl:with-param>	
			<xsl:with-param name="lab_attendance">考勤</xsl:with-param>
			<xsl:with-param name="total_result">共<xsl:value-of select="$mod_cnt"></xsl:value-of>条记录</xsl:with-param>	
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
			<xsl:with-param name="lab_main_eval">Scoring rules</xsl:with-param>
			<xsl:with-param name="lab_content">Content</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_max_score">Maximum score</xsl:with-param>
			<xsl:with-param name="lab_pass_score">Passing score</xsl:with-param>
			<xsl:with-param name="lab_percent">Weight(%)</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_schedule">Edit</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_percent">Edit weight</xsl:with-param>
			<xsl:with-param name="lab_online_module">Learning module</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">Add scoring item</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">Delete</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_resort">Reorder</xsl:with-param>
			<xsl:with-param name="lab_no_eval">No scoring items found</xsl:with-param>
			<xsl:with-param name="lab_sum">Total</xsl:with-param>	
			<xsl:with-param name="lab_attendance">Result</xsl:with-param>
			<xsl:with-param name="total_result">Total <xsl:value-of select="$mod_cnt"></xsl:value-of> results</xsl:with-param>				
		</xsl:call-template>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="content">
		<xsl:param name="lab_main_eval"/>
		<xsl:param name="lab_content"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_max_score"/>
		<xsl:param name="lab_no_eval"/>
		<xsl:param name="lab_g_txt_btn_edit_schedule"/>
		<xsl:param name="lab_g_txt_btn_add"/>
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_g_txt_btn_edit_percent"/>
		<xsl:param name="lab_g_txt_btn_resort"/>
		<xsl:param name="lab_percent"/>
		<xsl:param name="lab_pass_score"/>
		<xsl:param name="lab_online_module"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_sum"/>		
		<xsl:param name="lab_attendance"/>
		<xsl:param name="total_result"/>
		
		 <xsl:call-template name="itm_action_nav">
				<xsl:with-param  name="cur_node_id">103</xsl:with-param>
		 </xsl:call-template>
	<div class="wzb-item-main">		
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
			
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/>
			</xsl:with-param>
		</xsl:call-template>
		
			<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text">
				<xsl:value-of select="$label_core_training_management_320"/><br/>
				<xsl:value-of select="$label_core_training_management_321"/><br/>
				<xsl:value-of select="$label_core_training_management_322"/><br/>
				<xsl:value-of select="$label_core_training_management_323"/><br/>
				<xsl:value-of select="$label_core_training_management_324"/><br/>
				<!-- <xsl:value-of select="$label_core_training_management_325"/><br/>
				<xsl:value-of select="$label_core_training_management_326"/><br/>
				<xsl:value-of select="$label_core_training_management_327"/><br/> -->
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="//itm_action_nav/hasTeachingCourse/text()='true'">
						<a href="javascript:itm_lst.get_itm_instr_view({//itm_action_nav/@itm_id})" class="NavLink">
							<xsl:value-of select="//itm_action_nav/@itm_title"/>
						</a>
					</xsl:when>
					<xsl:when test="/CourseCriteria/item/@run_ind = 'false' and $current_role='TAMD_1'">
						<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
							<xsl:value-of select="/CourseCriteria/item/title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="/CourseCriteria/item/nav/item" mode="nav">
							<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
							<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
						</xsl:apply-templates>
						
					</xsl:otherwise>
				</xsl:choose>
				<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_main_eval"/></span>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_head">
			<!--<xsl:with-param name="text" select="$lab_main"/>-->
			<xsl:with-param name="extra_td">
			
				<td align="right">
				<xsl:if test="$editable='true'">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_g_txt_btn_add"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:cmt.add_new_cmt('<xsl:value-of select="/CourseCriteria/item/@id"/>','<xsl:value-of select="/CourseCriteria/item/@cos_res_id"/>');</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
					</xsl:call-template>
					<xsl:if test="count(/CourseCriteria/marking_scheme_list/item) &gt; 1">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_g_txt_btn_edit_percent"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:cmt.mod_percent(frmXml,'<xsl:value-of select="$ccr_id"/>','<xsl:value-of select="$itm_id"/>','<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="/CourseCriteria/item/@cos_res_id"/>','<xsl:value-of select="$mod_cnt"/>','<xsl:value-of select="$wb_lang"/>');</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					<!--<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_g_txt_btn_resort"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
					</xsl:call-template>-->
						</xsl:if>
				</td>
			
			</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="$mod_cnt='0'">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_eval"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td width="16%">
							<xsl:value-of select="$lab_title"/>
						</td>
						<td width="16%">
							<xsl:value-of select="$lab_online_module"/>
						</td>
						<td width="16%">
							<xsl:value-of select="$lab_max_score"/>
						</td>
						<td width="16%">
							<xsl:value-of select="$lab_pass_score"/>
						</td>
						<td width="16%">
							<xsl:value-of select="$lab_percent"/>
						</td>
						<xsl:if test="$editable='true'">
						<td width="20%" align="right">
						</td>
						</xsl:if>
					</tr>
					<xsl:apply-templates select="marking_scheme_list/item">
						<xsl:with-param name="lab_g_txt_btn_edit_schedule" select="$lab_g_txt_btn_edit_schedule"/>
						<xsl:with-param name="lab_g_txt_btn_remove" select="$lab_g_txt_btn_remove"/>
					</xsl:apply-templates>
				</table>
				
				<div style=";border-top: 1px solid #EEE;verflow: hidden;text-align: center;padding: 5px 0;">
					<p style="padding: 4px;display: inline-block;margin: 0;"><xsl:value-of select="$total_result"></xsl:value-of></p>
				</div>
			</xsl:otherwise>
		</xsl:choose>
	</div>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="item">
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_g_txt_btn_edit_schedule"/>
		<xsl:variable name="cmt_id" select="@cmt_id"/>
		<tr>
			<td>
				<xsl:value-of select="cmt_title"/>
			</td>
			<td>
				<xsl:choose>
					<xsl:when test="cmt_mod_title">
						<xsl:value-of select="cmt_mod_title"/>
					</xsl:when>
					<xsl:otherwise>
						--
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td>
				<xsl:value-of select="number(cmt_max_score)"/>
			</td>
			<td>
				<xsl:value-of select="number(cmt_pass_score)"/>
			</td>
			<td>
				<xsl:value-of select="number(cmt_contri_rate)"/>
			</td>
			<xsl:if test="$editable='true'">
			<td align="right">
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_edit_schedule"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:cmt.upd_cmt(frmXml,'<xsl:value-of select="$cmt_id"/>','<xsl:value-of select="$itm_id"/>','<xsl:value-of select="$ccr_id"/>','<xsl:value-of select="/CourseCriteria/item/@cos_res_id"/>')</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="class">btn wzb-btn-blue</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_remove"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:cmt.del_cmt(frmXml,'<xsl:value-of select="$ccr_id"/>','<xsl:value-of select="$cmt_id"/>','<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$itm_id"/>')</xsl:with-param>
				</xsl:call-template>
				
			</td>
			</xsl:if>
		</tr>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="item" mode="nav">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_session_info"/>
		<xsl:variable name="_count" select="count(preceding-sibling::item)"/>
		<xsl:choose>
			<xsl:when test="@run_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=$_count">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.get_item_run_list({$value})" class="NavLink">
					<xsl:choose>
						<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
					</xsl:choose>
				</a>
				<span class="NavLink">&#160;&gt;&#160;</span>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:when test="@session_ind = 'true'">
				<span class="NavLink">&#160;&gt;&#160;</span>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=last()">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.session.get_session_list({$value})" class="NavLink">
					<xsl:value-of select="$lab_session_info"/>
				</a>
				<span>&#160;&gt;&#160;</span>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a href="javascript:itm_lst.get_item_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template> 
	<!-- ================================================================ -->
</xsl:stylesheet>
