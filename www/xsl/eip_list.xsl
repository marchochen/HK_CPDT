<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
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
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- ============================================================= -->
	<xsl:variable name="cur_page" select="//pagination/@cur_page"/>
	<xsl:variable name="total" select="//pagination/@total_rec"/>
	<xsl:variable name="page_size" select="//pagination/@page_size"/>
	<xsl:variable name="order_by" select="//pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="//pagination/@sort_order"/>
	<xsl:variable name="sort_order">
		<xsl:choose>
			<xsl:when test="$cur_order = 'asc'">desc</xsl:when>
			<xsl:otherwise>asc</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_eip_mgt" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN001')"/> 	
	<xsl:variable name="lab_eip_add_botton" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN002')"/> 	
	<xsl:variable name="lab_eip_code" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN003')"/> 	
	<xsl:variable name="lab_eip_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN004')"/> 	
	<xsl:variable name="lab_eip_account_num" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN005')"/> 	
	<xsl:variable name="lab_eip_account_used" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN006')"/> 	
	<xsl:variable name="lab_eip_account_left" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN007')"/> 	
	<xsl:variable name="lab_eip_domain" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN008')"/> 	
	<xsl:variable name="lab_eip_login_bg" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN009')"/>
	<xsl:variable name="lab_eip_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN010')"/>  
	<xsl:variable name="lab_no_eip" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN011')"/>  	
	<xsl:variable name="lab_del_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '257')"/> 
	<xsl:variable name="lab_view_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN328')"/> 	
	<xsl:variable name="lab_update_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '873')"/> 
	<xsl:variable name="lab_eip_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '704')"/> 		
	<xsl:variable name="lab_eip_status_on" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '765')"/> 	
	<xsl:variable name="lab_eip_status_off" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '766')"/> 
	<xsl:variable name="lab_eip_update_user" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '705')"/> 	
	<xsl:variable name="lab_eip_update_timestamp" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '706')"/> 
	<xsl:variable name="lab_tcr_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_tc')"/> 
	<xsl:variable name="lab_eip_peak" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1205')"/> 
	<!-- =============================================================== -->
	<xsl:template match="/eip_module">
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
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_eip.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				eip = new wbEIP;
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
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_EIP_MAIN</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text" select="$lab_eip_mgt"/>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="extra_td">
					<td align="right">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_eip_add_botton"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:eip.ins_upd_eip_prep()</xsl:with-param>
						</xsl:call-template>
					</td>
				</xsl:with-param>
			</xsl:call-template>
			
			<xsl:choose>
				<xsl:when test="count(eip_list/eip) = 0">
					<xsl:call-template name="wb_ui_show_no_item">
						<xsl:with-param name="text" select="$lab_no_eip"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<table class="table wzb-ui-table">
						<tr class="wzb-ui-table-head">
							<td width="10%">
								<xsl:choose>
									<xsl:when test="$order_by = 'eip_code' ">
										<a href="javascript:eip.sort_eip_list('eip_code','{$sort_order}')">
											<xsl:value-of select="$lab_eip_code"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:eip.sort_eip_list('eip_code','asc')">
											<xsl:value-of select="$lab_eip_code"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="10%">
								<xsl:choose>
									<xsl:when test="$order_by = 'eip_name' ">
										<a href="javascript:eip.sort_eip_list('eip_name','{$sort_order}')">
											<xsl:value-of select="$lab_eip_name"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:eip.sort_eip_list('eip_name','asc')">
											<xsl:value-of select="$lab_eip_name"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="10%">
								<xsl:choose>
									<xsl:when test="$order_by = 'eip_tcr_id' ">
										<a href="javascript:eip.sort_eip_list('eip_tcr_id','{$sort_order}')">
											<xsl:value-of select="$lab_tcr_title"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:eip.sort_eip_list('eip_tcr_id','asc')">
											<xsl:value-of select="$lab_tcr_title"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="20%" align="center">
								<xsl:choose>
									<xsl:when test="$order_by = 'eip_account_num' ">
										<a href="javascript:eip.sort_eip_list('eip_account_num','{$sort_order}')">
											<xsl:value-of select="$lab_eip_account_num"/>(<xsl:value-of select="$lab_eip_account_used"/>/<xsl:value-of select="$lab_eip_account_left"/>)
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:eip.sort_eip_list('eip_account_num','asc')">
											<xsl:value-of select="$lab_eip_account_num"/>(<xsl:value-of select="$lab_eip_account_used"/>/<xsl:value-of select="$lab_eip_account_left"/>)
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="15%" align="center">
								<xsl:choose>
									<xsl:when test="$order_by = 'usr_display_bil' ">
										<a href="javascript:eip.sort_eip_list('usr_display_bil','{$sort_order}')">
											<xsl:value-of select="$lab_eip_update_user"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:eip.sort_eip_list('usr_display_bil','asc')" class="">
											<xsl:value-of select="$lab_eip_update_user"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="15%" align="center">
								<xsl:choose>
									<xsl:when test="$order_by = 'eip_update_timestamp' ">
										<a href="javascript:eip.sort_eip_list('eip_update_timestamp','{$sort_order}')" class="">
											<xsl:value-of select="$lab_eip_update_timestamp"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:eip.sort_eip_list('eip_update_timestamp','asc')" class="">
											<xsl:value-of select="$lab_eip_update_timestamp"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="10%">
								<xsl:choose>
									<xsl:when test="$order_by = 'eip_status' ">
										<a href="javascript:eip.sort_eip_list('eip_status','{$sort_order}')" class="">
											<xsl:value-of select="$lab_eip_status"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:eip.sort_eip_list('eip_status','asc')" class="">
											<xsl:value-of select="$lab_eip_status"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="20%">
								<xsl:choose>
									<xsl:when test="$order_by = 'peak_count' ">
										<a href="javascript:eip.sort_eip_list('peak_count','{$sort_order}')" class="">
											<xsl:value-of select="$lab_eip_peak"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:eip.sort_eip_list('peak_count','asc')" class="">
											<xsl:value-of select="$lab_eip_peak"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							
						</tr>
						<xsl:for-each select="eip_list/eip">
							<tr>
								<td>
									<xsl:value-of select="@code"/>
								</td>
								<td>
									<xsl:value-of select="@name"/>
								</td>
								<td>
									<xsl:value-of select="@tcr_title"/>
								</td>
								<td align="center">
									<xsl:value-of select="@account_num"/>(<xsl:value-of select="@account_used"/>/<xsl:value-of select="@account_leaving"/>)
								</td>
								<td align="center">
									<xsl:value-of select="@update_user"/>
								</td>
								<td align="center">
									<xsl:call-template name="display_time">
										<xsl:with-param name="my_timestamp">
											<xsl:value-of select="@update_timestamp"/>
										</xsl:with-param>
									</xsl:call-template>
									<input type="hidden" name="eip_update_timestamp" value="{@update_timestamp}"/>
								</td>
								<td>
									<xsl:choose>
										<xsl:when test="@status='ON'">
											<xsl:value-of select="$lab_eip_status_on"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$lab_eip_status_off"/>
										</xsl:otherwise>
									</xsl:choose>
								</td>
								<td align="center">
									<xsl:value-of select="@peak_count"/>/<xsl:value-of select="@eip_max_peak_count"/>
									<input type="hidden" name="eip_max_peak_count" value="{@eip_max_peak_count}"/>
								</td>
								<td align="center" nowrap="true">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_view_button"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:eip.get_eip(<xsl:value-of select="@id"/>)</xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="class">btn wzb-btn-blue</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_name" select="$lab_update_button"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:eip.ins_upd_eip_prep(<xsl:value-of select="@id"/>)</xsl:with-param>
									</xsl:call-template>
								</td>
							</tr>
						</xsl:for-each>
					</table>
					<xsl:call-template name="wb_ui_pagination">
						<xsl:with-param name="cur_page" select="$cur_page"/>
						<xsl:with-param name="page_size" select="$page_size"/>
						<xsl:with-param name="total" select="$total"/>
						<xsl:with-param name="width" select="$wb_gen_table_width"/>
						<xsl:with-param name="cur_page_name">cur_page</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</form>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>