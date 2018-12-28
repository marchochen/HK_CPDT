<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<!--custom-->
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/evalmanagement">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_confirm">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_import_mark_prep_title">導入成績 - 第二步：確認</xsl:with-param>
			<xsl:with-param name="lab_desc">以下是文件中的成績信息。點擊屏幕底部的<b>確定</b>按鈕完成導入，或單擊<b>取消</b>按鈕停止操作。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_confirm">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_import_mark_prep_title">导入成绩 - 第二步：确认</xsl:with-param>
			<xsl:with-param name="lab_desc">以下是文件中的成绩信息。点击屏幕底部的<b>确定</b>按钮完成导入，或单击<b>取消</b>按钮停止操作。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_confirm">Confirm</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_import_mark_prep_title">Import score – step 2: confirmation</xsl:with-param>
			<xsl:with-param name="lab_desc">Listed below are the details detected from the file. Click <b>Confirm</b> at the bottom to complete the import, or <b>Cancel</b> to stop the process.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_g_form_btn_confirm"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_import_mark_prep_title"/>
		<xsl:param name="lab_desc"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_evalmgt.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
				evalmgt = new wbEvalManagement;
			]]></script>
			<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onLoad="feedParam(frm)">
			<form name="frm" method="post" onsubmit="javascript:" enctype="multipart/form-data">
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="lrn_ent_id" value=""/>
				<input type="hidden" name="cmt_id" value=""/>
				<input type="hidden" name="cmt_tkh_id" value=""/>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_import_mark_prep_title"/>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text" select="$lab_desc"/>
				</xsl:call-template>
				<xsl:apply-templates select="eval_item_lst"/>
				<xsl:call-template name="wb_ui_line"/>
				<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr>
						<td align="center">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$lab_g_form_btn_confirm"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:evalmgt.exec_import_mark(frm,'true')</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
							</xsl:call-template>
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$lab_g_form_btn_cancel"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:window.close();</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
				</table>
				<xsl:call-template name="wb_ui_footer"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[
					str='<input type="submit" value="" size="0" style="height : 0px;width : 0px; visibility: hidden;"/>'
					if (document.all || document.getElementById!=null){
						document.write(str);
					}
				]]></script>
			</form>
		</body>
	</xsl:template>
	<xsl:template match="eval_item_lst">
		<table class="table wzb-ui-table">
			<tr class="wzb-ui-table-head">
				<xsl:for-each select="col_name">
					<td nowrap="nowrap" class="TitleText"><xsl:value-of select="@name"/></td>
				</xsl:for-each>
			</tr>
			<xsl:apply-templates select="eval_item"/>
		</table>
	</xsl:template>
	<xsl:template match="eval_item">
		<tr class="SecBg">
			<xsl:attribute name="class"><xsl:choose><xsl:when test="position() mod 2 = 1">RowsEven</xsl:when><xsl:otherwise>RowsOdd</xsl:otherwise></xsl:choose></xsl:attribute>
			<xsl:for-each select="col_value">
					<td nowrap="nowrap"><xsl:value-of select="@value"/></td>
			</xsl:for-each>
		</tr>
	</xsl:template>
</xsl:stylesheet>
