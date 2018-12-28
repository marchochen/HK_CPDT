<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/trun_date.xsl"/>
	<xsl:import href="utils/wb_form_select_action.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="item_type_list" select="/applyeasy/item_type_list"></xsl:variable>
	<xsl:variable name="ent_id" select="/applyeasy/meta/cur_usr/@ent_id"></xsl:variable>
	<xsl:variable name="role_id" select="/applyeasy/meta/cur_usr/role/@id"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="applyeasy"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="applyeasy">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_announcement.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_instructor.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			var insc = new wbInstructor;
			var itm = new wbItem;
			var ann = new wbAnnouncement;
			var attn = new wbAttendance;
			
		]]></script>
		
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml">
				<xsl:call-template name="content"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->

	<xsl:variable name="lab_teaching_courses" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_126')"/>
	<xsl:variable name="lab_teaching_courses_desc" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_127')"/>
	<xsl:variable name="lab_title" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_128')"/>
	<xsl:variable name="lab_status" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_129')"/>
	<xsl:variable name="lab_start_date" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_130')"/>
	<xsl:variable name="lab_end_date" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_131')"/>
	<xsl:variable name="lab_no_item" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_132')"/>
	<xsl:variable name="lab_type" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_133')"/>
	<xsl:variable name="lab_status_on" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_134')"/>
	<xsl:variable name="lab_status_off" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_135')"/>
	<xsl:variable name="lab_g_lst_btn_content" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_136')"/>
	<xsl:variable name="lab_grad_record" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_138')"/>
	<xsl:variable name="lab_g_txt_btn_ann" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_139')"/>
	<xsl:variable name="lab_g_lst_btn_per" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_137')"/>
	<xsl:variable name="lab_g_lst_btn_info" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_140')"/>
	<xsl:variable name="lab_g_txt_btn_courses" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_141')"/>
	<xsl:variable name="lab_g_txt_btn_inscomment" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_142')"/>
	<xsl:variable name="lab_g_txt_btn_inscomment1" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_143')"/>
	
		<!-- paginatoin variables -->
	<xsl:variable name="page_size" select="/applyeasy/pagination/@page_size"/>
	<xsl:variable name="cur_sort_order" select="/applyeasy/pagination/@sort_order"/>
	<xsl:variable name="cur_page" select="/applyeasy/pagination/@cur_page"/>
	<xsl:variable name="total" select="/applyeasy/pagination/@total_rec"/>
	<xsl:variable name="timestamp" select="/applyeasy/pagination/@timestamp"/>


	<xsl:template name="content">
		<xsl:apply-templates select="item_list">
		
		</xsl:apply-templates>
	</xsl:template>
	
	<!-- =============================================================== -->
	<xsl:template match="item_list">
	 	<xsl:variable name="item_cnt" select="count(item)"/>	
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">ITM_OFFINE_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_teaching_courses"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_teaching_courses_desc"/>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="(count(item) &lt;= 0)">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_item"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td width="20%">
							<xsl:value-of select="$lab_title"/>
						</td>
						<td width="10%">
							<xsl:value-of select="$lab_type"/>
						</td>
						<td width="10%">
							<xsl:value-of select="$lab_status"/>
						</td>
						<td align="center" width="10%">
							<xsl:value-of select="$lab_start_date"/>
						</td>
						<td align="center" width="10%">
							<xsl:value-of select="$lab_end_date"/>
						</td>
						<td width="40%"></td>
					</tr>
					<xsl:apply-templates select="item">
						<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
						<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
						<xsl:with-param name="lab_g_lst_btn_content" select="$lab_g_lst_btn_content"/>
						<xsl:with-param name="lab_g_lst_btn_per" select="$lab_g_lst_btn_per"/>
						<xsl:with-param name="lab_g_lst_btn_info" select="$lab_g_lst_btn_info"/>
						<xsl:with-param name="lab_grad_record" select="$lab_grad_record"/>
						<xsl:with-param name="lab_g_txt_btn_ann" select="$lab_g_txt_btn_ann"/>
						<xsl:with-param name="lab_g_txt_btn_courses" select="$lab_g_txt_btn_courses"/>
						<xsl:with-param name="lab_g_txt_btn_inscomment" select="$lab_g_txt_btn_inscomment"/>
						<xsl:with-param name="lab_g_txt_btn_inscomment1" select="$lab_g_txt_btn_inscomment1"/>
					</xsl:apply-templates>
				</table>
				<!-- Pagination -->
				<xsl:if test=" $item_cnt&gt;0">
					<xsl:call-template name="wb_ui_pagination">
						<xsl:with-param name="cur_page" select="$cur_page"/>
						<xsl:with-param name="page_size" select="$page_size"/>
						<xsl:with-param name="timestamp" select="$timestamp"/>
						<xsl:with-param name="total" select="$total"/>
						<xsl:with-param name="width"><xsl:value-of select="$wb_gen_table_width"/></xsl:with-param>
					</xsl:call-template>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--************************************************-->
	<xsl:template match="item">
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_g_lst_btn_content"/>
		<xsl:param name="lab_g_lst_btn_per"/>
		<xsl:param name="lab_g_lst_btn_info"/>
		<xsl:param name="lab_grad_record"/>
		<xsl:param name="lab_g_txt_btn_ann"/>
		<xsl:param name="lab_g_txt_btn_courses"/>
		<xsl:param name="lab_g_txt_btn_inscomment"/>
		<xsl:param name="lab_g_txt_btn_inscomment1"/>
		<xsl:variable name="ins_comment_itm_id">
			<xsl:choose>
				<xsl:when test="child_item/@id >0"><xsl:value-of select="child_item/@id"/></xsl:when>
				<xsl:otherwise><xsl:value-of select="@id"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr>
			<td>
				<xsl:value-of select="title/text()"/>
				<xsl:if test="child_item">
					<xsl:text>&#160;-&#160;</xsl:text>
					<xsl:value-of select="child_item/title/text()"/>
				</xsl:if>
			</td>
			<td>
				<xsl:call-template name="get_ity_title">
					<xsl:with-param name="itm_type" select="@dummy_type"/>
				</xsl:call-template>
			</td>
			<td>
				<xsl:variable name="status">
					<xsl:choose>
						<xsl:when test="child_item">
							<xsl:value-of select="child_item/status"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="status"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="$status = 'ON'">
						<xsl:value-of select="$lab_status_on"/>
					</xsl:when>
					
					<xsl:otherwise>
						<xsl:value-of select="$lab_status_off"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:variable name="start_date">
					<xsl:choose>
						<xsl:when test="child_item">
							<xsl:value-of select="child_item/eff_start_datetime"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="eff_start_datetime"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="$start_date != ''">
						<xsl:call-template name="trun_date">
							<xsl:with-param name="my_timestamp">
								<xsl:value-of select="$start_date"/>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>--</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:variable name="end_date">
					<xsl:choose>
						<xsl:when test="child_item">
							<xsl:value-of select="child_item/eff_end_datetime"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="eff_end_datetime"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="$end_date != ''">
						<xsl:call-template name="trun_date">
							<xsl:with-param name="my_timestamp">
								<xsl:value-of select="$end_date"/>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>--</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td nowrap="true" align="right">
				<xsl:variable name="itm_id">
					<xsl:choose>
						<xsl:when test="child_item">
							<xsl:value-of select="child_item/@id"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="@id"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name">
						<xsl:value-of select="$lab_g_txt_btn_inscomment"/>
					</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">Javascript:insc.comment.get_ins_comment_view('<xsl:value-of select="$ins_comment_itm_id"/>','<xsl:value-of select="$ent_id"/>')</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name">
						<xsl:value-of select="$lab_g_lst_btn_info"/>
					</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">Javascript:itm.get_itm_instr_view('<xsl:value-of select="$itm_id"/>')</xsl:with-param>
				</xsl:call-template>
				<!-- 
					<xsl:choose>
						<xsl:when test="@itm_integrated_ind = 'true'">
							<xsl:variable name="cos_res_id"><xsl:value-of select="resourse/@id"/></xsl:variable>
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$lab_g_txt_btn_ann"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:ann.sys_lst('all','RES','<xsl:value-of select="$cos_res_id"/>','','','','','',true)</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$lab_grad_record"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:attn.get_grad_record(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_courses"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:itm.get_course_list(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$lab_g_lst_btn_content"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">Javascript:itm.get_itm_content_instr_view('<xsl:value-of select="$itm_id"/>')</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$lab_g_lst_btn_per"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:attn.get_grad_record('<xsl:value-of select="$itm_id"/>')</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
					 -->
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
