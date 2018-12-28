<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	
	
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- ============================================================= -->
	<xsl:variable name="cur_page" select="//pagination/@cur_page"/>
	<xsl:variable name="total" select="//pagination/@total_rec"/>
	<xsl:variable name="page_size" select="//pagination/@page_size"/>
	<xsl:variable name="order_by" select="//pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="//pagination/@sort_order"/>
	
	<xsl:variable name="cnt" select="count(//templates/message_template)"/>
	
	<xsl:variable name="page_variant" select="//meta/page_variant"/>
	<xsl:variable name="sort_order">
		<xsl:choose>
			<xsl:when test="$cur_order = 'asc'">desc</xsl:when>
			<xsl:otherwise>asc</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_template_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1137')"/>
	<xsl:variable name="lab_template_subject" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1138')"/>
	<xsl:variable name="lab_template_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1139')"/>
	<xsl:variable name="lab_template_status_on" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1140')"/>
	<xsl:variable name="lab_template_status_off" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1141')"/>
	<xsl:variable name="lab_template_update_datetime" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1142')"/>
	<xsl:variable name="lab_template_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1143')"/>
	<xsl:variable name="lab_ftn_message_template" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_ftn_MESSAGE_TEMPLATE')"/> 
	<xsl:variable name="lab_no_template" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_table_empty')"/> 
	<xsl:variable name="lab_update_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '708')"/>
	
	<!-- =============================================================== -->
	<xsl:template match="/message_module">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>

	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_new_message.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				wbmsg = new wbNewMessage; 
 			]]></script> 
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="content"/>
		</body>
	</xsl:template>
	
	<!-- =============================================================== -->
	<xsl:template name="content">
		<form name="frmXml">
			<input type="hidden" name="module"/>
			<input type="hidden" name="cmd"/>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<input type="hidden" name="stylesheet"/>
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_MESSAGE_TEMPLATE_MAIN</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text" select="$lab_ftn_message_template"/>
			</xsl:call-template>
			
			<xsl:choose>
				<xsl:when test="count(templates/message_template) = 0">
					<xsl:call-template name="wb_ui_show_no_item">
						<xsl:with-param name="text" select="$lab_no_template"/>
					</xsl:call-template>
				</xsl:when>
			<xsl:otherwise>
					

			<table class="table wzb-ui-table">
						<tr class="wzb-ui-table-head">
							<td width="30%">
								<xsl:choose>
									<xsl:when test="$order_by = 'mtp_subject' ">
										<a href="javascript:wbmsg.sort_message_template_list(document.frmXml,'mtp_subject','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_template_subject"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>							
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wbmsg.sort_message_template_list(document.frmXml,'mtp_subject','asc')" class="TitleText">
											<xsl:value-of select="$lab_template_subject"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="30%">
								<xsl:choose>
									<xsl:when test="$order_by = 'mtp_type' ">
										<a href="javascript:wbmsg.sort_message_template_list(document.frmXml,'mtp_type','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_template_title"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>							
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wbmsg.sort_message_template_list(document.frmXml,'mtp_type','asc')" class="TitleText">
											<xsl:value-of select="$lab_template_title"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="20%">
								<xsl:choose>
									<xsl:when test="$order_by = 'mtp_active_ind' ">
										<a href="javascript:wbmsg.sort_message_template_list(document.frmXml,'mtp_active_ind','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_template_status"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>							
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wbmsg.sort_message_template_list(document.frmXml,'mtp_active_ind','asc')" class="TitleText">
											<xsl:value-of select="$lab_template_status"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="25%" align="center">
								<xsl:choose>
									<xsl:when test="$order_by = 'mtp_update_timestamp' ">
										<a href="javascript:wbmsg.sort_message_template_list(document.frmXml,'mtp_update_timestamp','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_template_update_datetime"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>							
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wbmsg.sort_message_template_list(document.frmXml,'mtp_update_timestamp','asc')" class="TitleText">
											<xsl:value-of select="$lab_template_update_datetime"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td>
							</td>
						</tr>
						<xsl:for-each select="templates/message_template">
							<xsl:if test="mtp_type != 'KNOW_ADD_MESSAGE'">
							<tr>
								<td>
									<a class="Text" href="javascript:wbmsg.get_message_template_view({mtp_id});" >
										<xsl:value-of select="mtp_subject"/>
									</a>
								</td>	
								<td>
									<xsl:value-of select="mtp_type"/>
								</td>								
								<td>
									<xsl:choose>
										<xsl:when test="mtp_active_ind = 'true'">
											<xsl:value-of select="$lab_template_status_on"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$lab_template_status_off"/>
										</xsl:otherwise>
									</xsl:choose>
								</td>
								<td align="center">
									<xsl:call-template name="display_time">
										<xsl:with-param name="my_timestamp"><xsl:value-of select="mtp_update_timestamp"/></xsl:with-param>
										<xsl:with-param name="dis_time">T</xsl:with-param>
									</xsl:call-template>
								</td>
								<td align="center">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="class">btn wzb-btn-blue</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_update_button"/></xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">javascript:wbmsg.get_message_template_prep('<xsl:value-of select="mtp_id"/>')</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
									</xsl:call-template>
								</td>	
							</tr>
							</xsl:if>
						</xsl:for-each>
					</table>
				<xsl:variable name="page_total">
					<xsl:choose>
						<xsl:when test="$total = 0">
							<xsl:value-of select="$total"/>
						</xsl:when>
						<xsl:otherwise>
						    <xsl:value-of select="$total"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="total" select="$page_total"/>
					<xsl:with-param name="width" select="$wb_gen_table_width"/>
					<xsl:with-param name="cur_page_name">cur_page</xsl:with-param>
				</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:call-template name="wb_ui_footer"/>
		</form>
	</xsl:template>
</xsl:stylesheet>