<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/wb_module_status_const.xsl"/>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="module"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="module">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<link rel="stylesheet" href="../static/css/three.css"/>
			<link rel="stylesheet" href="../static/js/font-awesome/css/font-awesome.min.css"/>
			<script type="text/javascript" SRC="{$wb_js_path}jquery.min.js" LANGUAGE="JavaScript"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_{$wb_cur_lang}.js"/>
			
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_reference.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				var ref = new wbReference();
			]]></SCRIPT>
<!-- 			<xsl:call-template name="wb_css"> -->
<!-- 				<xsl:with-param name="view">wb_ui</xsl:with-param> -->
<!-- 			</xsl:call-template> -->
				<xsl:call-template name="new_css" />
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_reference">參考</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_description">內容簡介</xsl:with-param>
			<xsl:with-param name="lab_no_reference">沒有任何參考被加到此單元</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_reference">参考</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_description">内容简介</xsl:with-param>
			<xsl:with-param name="lab_no_reference">没有任何参考被加到此单元</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_reference">References</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_description">Description</xsl:with-param>
			<xsl:with-param name="lab_no_reference">No reference has been added for this module.</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_reference"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_description"/>
		<xsl:param name="lab_no_reference"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<form name="frmXml">
			<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="text" select="$lab_reference"/>
				<xsl:with-param name="new_template">true</xsl:with-param>
			</xsl:call-template>
			<xsl:choose>
				<xsl:when test="count(reference_list/reference) = 0">
					<xsl:call-template name="wb_ui_show_no_item">
						<xsl:with-param name="text" select="$lab_no_reference"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<div class="content_info">
						<table cellpadding="3" cellspacing="0" border="0" width="100%">
							<tr class="report_title">
								<td>
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								<td width="30%">
									<span class="TitleText">
										<xsl:value-of select="$lab_title"/>
									</span>
								</td>
								<td>
									<span class="TitleText">
										<xsl:value-of select="$lab_description"/>
									</span>
								</td>
								<td>
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
							</tr>
							<xsl:for-each select="reference_list/reference">
	<!-- 							<xsl:variable name="row_class"> -->
	<!-- 								<xsl:choose> -->
	<!-- 									<xsl:when test="position() mod 2">RowsEven</xsl:when> -->
	<!-- 									<xsl:otherwise>RowsOdd</xsl:otherwise> -->
	<!-- 								</xsl:choose> -->
	<!-- 							</xsl:variable> -->
								<tr class="report_content_tr">
									<td width="8">
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
									<td>
										<xsl:choose>
											<xsl:when test="@ref_url = ''">
												<span class="Text">
													<xsl:value-of select="@ref_title"/>
												</span>
											</xsl:when>
											<xsl:otherwise>
												<a href="javascript:ref.go_ref('{@ref_url}')" class="Text">
													<xsl:value-of select="@ref_title"/>
												</a>
											</xsl:otherwise>
										</xsl:choose>
									</td>
									<td>
										<span class="Text">
											<xsl:call-template name="unescape_html_linefeed">
												<xsl:with-param name="my_right_value"><xsl:value-of select="description/text()"/></xsl:with-param>
											</xsl:call-template>
										</span>
									</td>
									<td width="8">
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
								</tr>
							</xsl:for-each>
						</table>
					</div>
				</xsl:otherwise>
			</xsl:choose>

			<input type="hidden" name="cmd" value=""/>
			<input type="hidden" name="module" value=""/>
			<input type="hidden" name="url_success" value=""/>
			<input type="hidden" name="url_failure" value=""/>
			<input type="hidden" name="mod_id" value="{/module/@id}"/>
			<input type="hidden" name="ref_id_list" value=""/>
		</form>
	</xsl:template>
</xsl:stylesheet>
