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
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_input_file.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<!--custom-->
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/CourseCriteria">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_lead_way">根據以下步驟來進行文件導入:</xsl:with-param>
			<xsl:with-param name="lab_setp_1">1.點擊<a class="Text" href="javascript:evalmgt.eval_import_mark_prep_export_template(frm)"><b>此處</b></a>下載模板</xsl:with-param>
			<xsl:with-param name="lab_setp_2">2.用MSExcel打開模板</xsl:with-param>
			<xsl:with-param name="lab_setp_3">3.在模板中輸入分數</xsl:with-param>
			<xsl:with-param name="lab_setp_4">4.以“Unicode Text”文件類型保存模板</xsl:with-param>
			<xsl:with-param name="lab_setp_5">5.在下面的輸入框内指定所上傳的文件</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_file_location">文件位置</xsl:with-param>
			<xsl:with-param name="lab_import_mark_prep_title">導入成績 - 第一步：文件上載</xsl:with-param>
			<xsl:with-param name="lab_lang">ch</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_lead_way">根据以下步骤来进行文件导入:</xsl:with-param>
			<xsl:with-param name="lab_setp_1">1.点击<a class="Text" href="javascript:evalmgt.eval_import_mark_prep_export_template(frm)"><b>此处</b></a>下载模板</xsl:with-param>
			<xsl:with-param name="lab_setp_2">2.用MSExcel打开模板</xsl:with-param>
			<xsl:with-param name="lab_setp_3">3.在模板中输入分数</xsl:with-param>
			<xsl:with-param name="lab_setp_4">4.以”Unicode Text”文件类型保存模板</xsl:with-param>
			<xsl:with-param name="lab_setp_5">5.在下面的输入框内指定所上传的文件</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_file_location">文件位置</xsl:with-param>
			<xsl:with-param name="lab_import_mark_prep_title">导入成绩 - 第一步：文件上载</xsl:with-param>
			<xsl:with-param name="lab_lang">gb</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_lead_way">Follow the steps below to make the file for import:</xsl:with-param>
			<xsl:with-param name="lab_setp_1">1.Download the worksheet from <a class="Text" href="javascript:evalmgt.eval_import_mark_prep_export_template(frm)"><b>here</b></a></xsl:with-param>
			<xsl:with-param name="lab_setp_2">2.Open the worksheet with MSExcel</xsl:with-param>
			<xsl:with-param name="lab_setp_3">3.Enter the score in the worksheet</xsl:with-param>
			<xsl:with-param name="lab_setp_4">4.Save the worksheet as “Unicode text” file format</xsl:with-param>
			<xsl:with-param name="lab_setp_5">5.Upload the saved file by specifying it in the text box below</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next">Next</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_file_location">File location:</xsl:with-param>
			<xsl:with-param name="lab_import_mark_prep_title">Import score - step 1: file upload</xsl:with-param>
			<xsl:with-param name="lab_lang">en</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_lead_way"/>
		<xsl:param name="lab_setp_1"/>
		<xsl:param name="lab_setp_2"/>
		<xsl:param name="lab_setp_3"/>
		<xsl:param name="lab_setp_4"/>
		<xsl:param name="lab_setp_5"/>
		<xsl:param name="lab_g_form_btn_next"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_file_location"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_import_mark_prep_title"/>
		<xsl:param name="lab_lang"/>
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
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			<!--alert样式  -->
			<!-- <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> -->
			
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			<!--alert样式  end -->
			<script language="JavaScript" type="text/javascript"><![CDATA[
				evalmgt = new wbEvalManagement;
			]]></script>
			<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onLoad="feedParam(frm)">
			<form name="frm" method="post" onsubmit="javascript:evalmgt.eval_import_preview(frm,'{$lab_lang}');return false;" enctype="multipart/form-data">
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="lrn_ent_id" value=""/>
				<input type="hidden" name="cmt_id" value=""/>
				<input type="hidden" name="cmt_tkh_id" value=""/>
				<input type="hidden" name="src_filename" value=""/>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_import_mark_prep_title"/>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_line"/>
				<div style="margin:10px">
					<table>
						<tr>
							<td><xsl:copy-of select="$lab_lead_way"/></td>
						</tr>
						<tr>
							<td><xsl:copy-of select="$lab_setp_1"/></td>
						</tr>
						<tr>
							<td><xsl:copy-of select="$lab_setp_2"/></td>
						</tr>
						<tr>
							<td><xsl:copy-of select="$lab_setp_3"/></td>
						</tr>
						<tr>
							<td><xsl:copy-of select="$lab_setp_4"/></td>
						</tr>
						<tr>
							<td><xsl:copy-of select="$lab_setp_5"/></td>
						</tr>
						<tr>
							<td align="left">
								<xsl:value-of select="$lab_file_location"/>：&#160;
								<!-- <input type="file" name="src_filename_path" class="wzb-inputText"/> -->
								<xsl:call-template name="wb_gen_input_file">
									<xsl:with-param name="name">src_filename_path</xsl:with-param>
								</xsl:call-template>
								<input type="hidden" name="src_filename"/>
							</td>
						</tr>
					</table>
				</div>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_next"/></xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:evalmgt.eval_import_preview(frm,'<xsl:value-of select="$lab_lang"/>')</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_cancel"/></xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:self.close();</xsl:with-param>
					</xsl:call-template>
					
					<xsl:for-each select="marking_scheme_list/item">
						<input type="hidden" name="all_cmt_id" value="{@cmt_id}"/>
					</xsl:for-each>
				</div>
				<script language="JavaScript" type="text/javascript"><![CDATA[
					str='<input type="submit" value="" size="0" style="height : 0px;width : 0px; visibility: hidden;"/>'
					if (document.all || document.getElementById!=null){
						document.write(str);
					}
				]]></script>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>
