<?xml version="1.0" encoding="UTF-8" ?>
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
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/wb_module_status_const.xsl"/>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<xsl:variable name="height">20</xsl:variable>
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
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_reference.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				var ref = new wbReference();
			]]></SCRIPT>
			<xsl:call-template name="new_css"/>
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
			<xsl:with-param name="lab_no_reference">此模塊尚未包含任何參考內容</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">添加</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_reference">参考</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_description">内容简介</xsl:with-param>
			<xsl:with-param name="lab_no_reference">此模块尚未包含任何参考内容</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">添加</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">删除</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_reference">References</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_description">Description</xsl:with-param>
			<xsl:with-param name="lab_no_reference">No reference has been added for this module.</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">Add</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_reference"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_description"/>
		<xsl:param name="lab_no_reference"/>
		<xsl:param name="lab_g_txt_btn_add"/>
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<form name="frmXml">
			<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="text" select="$lab_reference"/>
				<xsl:with-param name="extra_td"><td align="right"><xsl:call-template name="wb_gen_button_orange"><xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_txt_btn_add"/></xsl:with-param><xsl:with-param name="wb_gen_btn_href">javascript:ref.ins_ref_prep(document.frmXml)</xsl:with-param></xsl:call-template><xsl:if test="count(reference_list/reference) &gt; 0"><img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/><xsl:call-template name="wb_gen_button_orange"><xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_remove"/><xsl:with-param name="wb_gen_btn_href">javascript:ref.del_ref(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param></xsl:call-template></xsl:if></td></xsl:with-param>
			</xsl:call-template>
			<xsl:choose>
				<xsl:when test="count(reference_list/reference) = 0">
				    <xsl:call-template name="wb_ui_line"/>
					<xsl:call-template name="wb_ui_show_no_item">
						<xsl:with-param name="text" select="$lab_no_reference"/>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="wb_ui_line"/>
					<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
						<tr class="SecBg">
							<td>
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</td>
							<td style="width:10px;">
								<xsl:call-template name="select_all_checkbox">
									<xsl:with-param name="chkbox_lst_cnt"><xsl:value-of select="count(reference_list/reference)"/></xsl:with-param>
									<xsl:with-param name="display_icon">false</xsl:with-param>
									<xsl:with-param name="sel_all_chkbox_nm">sel_all_checkbox</xsl:with-param>
									<xsl:with-param name="frm_name">frmXml</xsl:with-param>
								</xsl:call-template>
							</td>
							<td>
								<span class="TitleText"  style="width:40px;display: inline-block; color:#999999;">
									<xsl:value-of select="$lab_title" />
								</span>
							</td>
							<td>
								<span class="TitleText" style="color:#999999;">
									<xsl:value-of select="$lab_description"/>
								</span>
							</td>
							<td>
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</td>
						</tr>
						<tr><td colSpan="10"><xsl:call-template name="wb_ui_line"/></td></tr>
						<xsl:for-each select="reference_list/reference">
							<xsl:variable name="row_class">
								<xsl:choose>
									<xsl:when test="position() mod 2">RowsEven</xsl:when>
									<xsl:otherwise>RowsOdd</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<tr class="{$row_class}">
								<td width="8">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								<td>
									<input type="checkbox" name="rdo_ref_id" value="{@ref_id}"/>
								</td>
								<td>
									<span class="Text">
										<a href="javascript:ref.upd_ref_prep(document.frmXml, '{@ref_id}')" class="Text">
											<xsl:value-of select="@ref_title"/>
										</a>
									</span>
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
					<xsl:call-template name="wb_ui_line">
						<xsl:with-param name="height" select="$height"/>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			<table border="0" cellpadding="3" cellspacing="0" width="{$wb_gen_table_width}">
				<tr>
					<td align="center">
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_close"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:self.close()</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</table>
			<input type="hidden" name="cmd" value=""/>
			<input type="hidden" name="module" value=""/>
			<input type="hidden" name="url_success" value=""/>
			<input type="hidden" name="url_failure" value=""/>
			<input type="hidden" name="mod_id" value="{/module/@id}"/>
			<input type="hidden" name="ref_id_list" value=""/>
		</form>
	</xsl:template>
</xsl:stylesheet>
