<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/exam_module">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:variable name="order_by" select="//pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="//pagination/@sort_order"/>
	<xsl:variable name="sort_order">
		<xsl:choose>
			<xsl:when test="$cur_order = 'asc'">desc</xsl:when>
			<xsl:otherwise>asc</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="cur_page" select="//pagination/@cur_page"/>
	<xsl:variable name="total_rec" select="//pagination/@total_rec"/>
	<xsl:variable name="page_size" select="//pagination/@page_size"/>
	<!-- =========================== Label =========================== -->

	<xsl:variable name="lab_exam_item" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_216')"/>
	<xsl:variable name="lab_no_exam" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_217')"/>
	<xsl:variable name="lab_itm_code" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_218')"/>
	<xsl:variable name="lab_itm_name" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang,'label_tm.label_core_training_management_219')"/>
	<xsl:variable name="lab_itm_status" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang,'label_tm.label_core_training_management_220')"/>
	<xsl:variable name="lab_itm_status_on" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_221')"/>
	<xsl:variable name="lab_itm_status_off" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_222')"/>
	<xsl:variable name="lab_tc_title" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'global.global_traning_center')"/>
	<xsl:variable name="lab_create_usr" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_223')"/>
	<xsl:variable name="lab_update_date" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_224')"/>
	<xsl:variable name="lab_invigilate" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_225')"/>
	
	
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				var wb_item = new wbItem;
			]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="content"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<form name="frmXml">
			<input type="hidden" name="cmd" value="upd_obj"/>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_COS_EVN_MAIN</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text" select="$lab_exam_item"/>
			</xsl:call-template>
			<table class="table wzb-ui-table">
				<xsl:choose>
					<xsl:when test="count(exam_itm_lst/exam_itm) = 0">
						<!-- <tr>
							<td align="center">
								999<xsl:value-of select="$lab_no_exam"/>
							</td>
						</tr> -->
						<xsl:call-template name="wb_ui_show_no_item">
						  <xsl:with-param name="text" select="$lab_no_exam"/>
					    </xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<tr class="wzb-ui-table-head">
							<td width="10%">
								<xsl:choose>
									<xsl:when test="$order_by = 'itm_code' ">
										<a href="javascript:wb_item.exam.sort_exam_list('itm_code','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_itm_code"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wb_item.exam.sort_exam_list('itm_code','asc')" class="TitleText">
											<xsl:value-of select="$lab_itm_code"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="10%">
								<xsl:choose>
									<xsl:when test="$order_by = 'itm_title' ">
										<a href="javascript:wb_item.exam.sort_exam_list('itm_title','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_itm_name"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wb_item.exam.sort_exam_list('itm_title','asc')" class="TitleText">
											<xsl:value-of select="$lab_itm_name"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="10%">
								<xsl:choose>
									<xsl:when test="$order_by = 'itm_status' ">
										<a href="javascript:wb_item.exam.sort_exam_list('itm_status','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_itm_status"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wb_item.exam.sort_exam_list('itm_status','asc')" class="TitleText">
											<xsl:value-of select="$lab_itm_status"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="10%">
								<xsl:choose>
									<xsl:when test="$order_by = 'tcr_title' ">
										<a href="javascript:wb_item.exam.sort_exam_list('tcr_title','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_tc_title"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wb_item.exam.sort_exam_list('tcr_title','asc')" class="TitleText">
											<xsl:value-of select="$lab_tc_title"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="10%">
								<xsl:value-of select="$lab_create_usr"/>
							</td>
							<td align="center" width="10%">
								<xsl:choose>
									<xsl:when test="$order_by = 'itm_upd_timestamp' ">
										<a href="javascript:wb_item.exam.sort_exam_list('itm_upd_timestamp','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_update_date"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wb_item.exam.sort_exam_list('itm_upd_timestamp','asc')" class="TitleText">
											<xsl:value-of select="$lab_update_date"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="40%" align="right">
							</td>
						</tr>
						<xsl:for-each select="exam_itm_lst/exam_itm">
							<tr>
								<td>
									<xsl:value-of select="code"/>
								</td>
								<td>
									<xsl:value-of select="title"/>
								</td>
								<td>
									<xsl:choose>
										<xsl:when test="status='ON'"><xsl:value-of select="$lab_itm_status_on"/></xsl:when>
										<xsl:otherwise><xsl:value-of select="$lab_itm_status_off"/></xsl:otherwise>
									</xsl:choose>
								</td>
								<td>
									<xsl:value-of select="tcraining_center"/>
								</td>
								<td>
									<xsl:value-of select="usr_display_bil"/>
								</td>
								<td>
									<xsl:call-template name="display_time">
										<xsl:with-param name="my_timestamp">
											<xsl:value-of select="itm_upd_timestamp"/>
										</xsl:with-param>
									</xsl:call-template>
								</td>
								<td align="right">
									<xsl:call-template name="wb_gen_form_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_invigilate"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:wb_item.exam.get_exam_online_list(document.frmXml,'<xsl:value-of select="@id"/>')</xsl:with-param>
									</xsl:call-template>
								</td>
							</tr>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</table>
			<xsl:call-template name="wb_ui_pagination">
				<xsl:with-param name="cur_page" select="$cur_page"/>
				<xsl:with-param name="page_size" select="$page_size"/>
				<xsl:with-param name="total" select="$total_rec"/>
				<xsl:with-param name="width" select="$wb_gen_table_width"/>
				<xsl:with-param name="cur_page_name">cur_page</xsl:with-param>
			</xsl:call-template>
		</form>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
