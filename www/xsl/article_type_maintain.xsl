<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
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
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="share/div_tree_share.xsl"/>
	<xsl:import href="share/gen_tree_js.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	
	
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- ============================================================= -->
	<xsl:variable name="cur_page" select="//page/@cur_page"/>
	<xsl:variable name="total" select="//page/@total_rec"/>
	<xsl:variable name="page_size" select="//page/@page_size"/>
	
	<xsl:variable name="cnt" select="count(//aty_list/aty)"/>
	
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_ftn_article_main" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_ftn_ARTICLE_MAIN')"/>
	<xsl:variable name="lab_aty_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '79')"/> 	
	<xsl:variable name="lab_aty_content" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_content')"/> 	
	<xsl:variable name="lab_aty_create_datetime" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1131')"/> 	
	<xsl:variable name="lab_aty_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '996')"/> 	
	<xsl:variable name="lab_update_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '708')"/> 	
	<xsl:variable name="lab_no_aty" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_table_empty')"/> 	
	<xsl:variable name="lab_create_date" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '331')"/> 	
	<xsl:variable name="lab_update_date" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lan2')"/> 	
	
	<xsl:variable name="lab_del_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '257')"/> 	
	<xsl:variable name="lab_add_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '473')"/>
	<xsl:variable name="lab_handle" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '561')"/> 	
	<xsl:variable name="lab_article_type_maintain" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lan7')"/> 	
	<!-- =============================================================== -->
	<xsl:template match="/article_module">
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
			<script language="Javascript" type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_im_{$wb_cur_lang}.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_article.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				wbarticle = new wbArticle; 
				wbencrytor = new wbEncrytor;
				function load_tree() {
					if (frmXml.tc_enabled_ind) {
						page_onload(250);
					}
				}
				function show_content(tcr_id) {
					wbarticle.search_article_list(tcr_id);
				}
 			]]></script> 
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="load_tree()">
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
			<input type="hidden" name="aty_id_list"/>
			<input type="hidden" name="stylesheet"/>
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_ARTICLE_MAIN</xsl:with-param>
				<xsl:with-param name="parent_code">FTN_AMD_ARTICLE_MAIN</xsl:with-param>
							<xsl:with-param name="page_title">
				<xsl:call-template name="get_lab">
					<xsl:with-param name="lab_title">label_im.label_core_information_management_48</xsl:with-param>
				</xsl:call-template>
			</xsl:with-param>
			</xsl:call-template>
  			<xsl:call-template name="wb_ui_title"> 
 				 <xsl:with-param name="text" >
 				  	<xsl:value-of select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_48')"/>	
 				 </xsl:with-param>
 			</xsl:call-template>  
 			
 			
 			<!-- 
			<table>
				<tr>
					<td class="Title" align="left">
						<a href="javascript:wb_utils_nav_go('FTN_AMD_ARTICLE_MAIN')" class="Title">
							<xsl:value-of select="$lab_ftn_article_main"/>
						</a>
						>
						<xsl:value-of select="$lab_article_type_maintain"/>
					</td>
				</tr>
			</table>
				-->
			
			<table>
				<tr>
					<td align="right">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_add_button"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:wbarticle.get_article_type_prep()</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_del_button"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:wbarticle.del_article_type(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						</xsl:call-template>	
					</td>
				</tr>
			</table>
			
			<xsl:choose>
				<xsl:when test="count(aty_list/aty) = 0">
					<xsl:call-template name="wb_ui_show_no_item">
						<xsl:with-param name="text" select="$lab_no_aty"/>
					</xsl:call-template>
				</xsl:when>
			<xsl:otherwise>
					

			<table class="table wzb-ui-table">
						<tr class="wzb-ui-table-head">
							<td width="30%">
								<xsl:call-template name="select_all_checkbox">
									<xsl:with-param name="chkbox_lst_cnt"><xsl:value-of select="$cnt"/></xsl:with-param>
									<xsl:with-param name="display_icon">false</xsl:with-param>
								</xsl:call-template>
								<xsl:value-of select="$lab_aty_title"/>
							</td>
							<td width="30%" align="center">
								<xsl:value-of select="$lab_create_date"/>
							</td>
							<td width="30%" align="center">
								<xsl:value-of select="$lab_update_date"/>
							</td>
							<td width="10%" align="right">
								<xsl:value-of select="$lab_handle"/>
							</td>
						</tr>
						<xsl:for-each select="aty_list/aty">
							<xsl:variable name="row_class">
								<xsl:choose>
									<xsl:when test="position() mod 2">RowsOdd</xsl:when>
									<xsl:otherwise>RowsEven</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<tr>

								<td>
									<input type="checkbox" name="aty_id" value="{@id}"/>
									<xsl:choose>
										<xsl:when test="string-length(@title)>30">
											<span class="Text" title="{aty_title}">
												<xsl:value-of select="substring(@title,0,30)"/>...
											</span>
										</xsl:when>
										<xsl:otherwise>
											<span class="Text" >
												<xsl:value-of select="@title"/>
											</span>
										</xsl:otherwise>
									</xsl:choose>
								</td>																	
								<td align="center">
									<xsl:value-of select="substring(@create_date,0,17)"/>
								</td>								
								<td align="center">
									<xsl:choose>
										<xsl:when test="@update_date = ''">
											--
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="substring(@update_date,0,17)"/>
										</xsl:otherwise>
									</xsl:choose>
								</td>
								<td align="right">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="class">btn wzb-btn-blue</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_update_button"/></xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">javascript:wbarticle.get_article_type_prep(wbencrytor.cwnEncrypt('<xsl:value-of select="@id"/>'))</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
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
	
	<xsl:template name="article_sel_status" >
		<xsl:param name="lab_name"/>
		<xsl:param name="lab_value"/>
		<xsl:param name="sel_name"/>
		<xsl:if test="$sel_name = ''">
				<option value="{$lab_value}" ><xsl:value-of select="$lab_name"/></option>	
		</xsl:if>
		<xsl:if test="$sel_name = 'selected'">
			<option value="{$lab_value}" selected="selected"><xsl:value-of select="$lab_name"/></option>	
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>