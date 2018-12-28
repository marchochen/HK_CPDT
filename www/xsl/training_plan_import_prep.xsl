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
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_input_file.xsl" />
	<xsl:output indent="yes"/>
	<xsl:variable name="tcr_id" select="/tptrainingplan/upload_plan/training_center/@id"/>
	<xsl:variable name="tcr_title" select="//tptrainingplan/upload_plan/training_center/title/text()"/>
	<xsl:variable name="year" select="/tptrainingplan/upload_plan/@year"/>
	<xsl:variable name="ypn_year" select="/tptrainingplan/upload_plan/@ypn_year"/>
	<xsl:variable name="upd_timestamp" select="/tptrainingplan/meta/upd_timestamp"/>
	<xsl:variable name="cur_lan" select="/tptrainingplan/meta/cur_usr/@curLan"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="tptrainingplan">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_training_plan.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" TYPE="text/javascript"><![CDATA[
			plan = new wbTrainingPlan;
		]]></script>
		    <!--alert样式  -->
			<!-- <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> -->
			
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			<!--alert样式  end -->
		  
		  
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="new_css"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frmXml" onsubmit="return status()" enctype="multipart/form-data">
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="rename" value=""/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="que_type"/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="tcr_id" value="{$tcr_id}"/>
				<input type="hidden" name="year" value="{$year}"/>
				<input type="hidden" name="ypn_year" value="{$ypn_year}"/>
				<input type="hidden" name="upd_timestamp" value="{$upd_timestamp}"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_file_location">文檔位置</xsl:with-param>
			<xsl:with-param name="lab_desc_1">您將上傳</xsl:with-param>
			<xsl:with-param name="lab_desc_2">的</xsl:with-param>
			<xsl:with-param name="lab_desc_5">年年度培訓計劃</xsl:with-param>
			<xsl:with-param name="lab_desc_3">，該培訓計劃提交截止時間為</xsl:with-param>
			<xsl:with-param name="lab_desc_4">。請根據以下步驟來進行年度培訓計劃文件導入</xsl:with-param>
			<xsl:with-param name="lab_title">導入年度計劃–第一步：文件上載</xsl:with-param>
			<xsl:with-param name="lab_frist_step">1.點擊<a class="Text" href="../htm/year_plan_template.xls" target="_blank">
					<b>此處</b>
				</a>下載模板</xsl:with-param>
			<xsl:with-param name="lab_second_step">2.按模板的要求錄入培訓計劃，並儲存</xsl:with-param>
			<xsl:with-param name="lab_third_step">3.在下面的輸入框內指定所上傳的文件(Excel文件)</xsl:with-param>
			<xsl:with-param name="lab_description">描述</xsl:with-param>
			<xsl:with-param name="lab_template">文檔範本</xsl:with-param>
			<xsl:with-param name="lab_instr">範本說明</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_file_location">文件位置</xsl:with-param>
			<xsl:with-param name="lab_desc_1">您将上传</xsl:with-param>
			<xsl:with-param name="lab_desc_2">的</xsl:with-param>
			<xsl:with-param name="lab_desc_5">年年度培训计划</xsl:with-param>
			<xsl:with-param name="lab_desc_3">，该培训计划提交截止时间为</xsl:with-param>
			<xsl:with-param name="lab_desc_4">。请根据以下步骤来进行年度培训计划文件导入</xsl:with-param>
			<xsl:with-param name="lab_title">导入年度计划–第一步：文件上载</xsl:with-param>
			<xsl:with-param name="lab_frist_step">1.点击<a class="Text" href="../htm/year_plan_template.xls" target="_blank">
					<b>此处</b>
				</a>下载模板</xsl:with-param>
			<xsl:with-param name="lab_second_step">2.按模板的要求录入培训计划，并保存</xsl:with-param>
			<xsl:with-param name="lab_third_step">3.在下面的输入框内指定所上传的文件(Excel文件)</xsl:with-param>
			<xsl:with-param name="lab_description">描述</xsl:with-param>
			<xsl:with-param name="lab_template">文件模板</xsl:with-param>
			<xsl:with-param name="lab_instr">模板说明</xsl:with-param>
			</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_next">Next</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_file_location">File location</xsl:with-param>
			<xsl:with-param name="lab_desc_1">You are going to upload the annual training plan for </xsl:with-param>
			<xsl:with-param name="lab_desc_2"> for the year </xsl:with-param>
			<xsl:with-param name="lab_desc_5"></xsl:with-param>
			<xsl:with-param name="lab_desc_3">.  The submission due date is </xsl:with-param>
			<xsl:with-param name="lab_desc_4">. Please follow the steps below:</xsl:with-param>
			<xsl:with-param name="lab_title">Import annual training plan – step 1: file upload</xsl:with-param>
			<xsl:with-param name="lab_frist_step">1. Click<a class="Text" href="../htm/year_plan_template.xls" target="_blank">
					<b>here</b>
				</a>download template</xsl:with-param>
			<xsl:with-param name="lab_second_step">2. Input the training plan with the format specified in the spreadsheet template.</xsl:with-param>
			<xsl:with-param name="lab_third_step">3. Browse and upload the spreadsheet (as excel file)</xsl:with-param>
			<xsl:with-param name="lab_description">Description</xsl:with-param>
			<xsl:with-param name="lab_template">File template</xsl:with-param>
			<xsl:with-param name="lab_instr">Template instruction</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_g_form_btn_next"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_file_location"/>
		<xsl:param name="lab_desc_1"/>
		<xsl:param name="lab_desc_2"/>
		<xsl:param name="lab_desc_5"/>
		<xsl:param name="lab_desc_3"/>
		<xsl:param name="lab_desc_4"/>
		<xsl:param name="lab_frist_step"/>
		<xsl:param name="lab_second_step"/>
		<xsl:param name="lab_third_step"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_description"/>
		<xsl:param name="lab_option_1"/>
		<xsl:param name="lab_option_2"/>
		<xsl:param name="lab_template"/>
		<xsl:param name="lab_instr"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_title"/>
		</xsl:call-template>
		<xsl:variable name="desc_">
			<xsl:value-of select="$lab_desc_1"/>
			<xsl:value-of select="$tcr_title"/>
			<xsl:value-of select="$lab_desc_2"/>
			<xsl:value-of select="$year"/>
			<xsl:value-of select="$lab_desc_5"/>
			<xsl:if test="$ypn_year !=''">
				<xsl:value-of select="$lab_desc_3"/>
				<xsl:value-of select="substring($ypn_year,0,11)"/>
			</xsl:if>
			<xsl:value-of select="$lab_desc_4"/>
		</xsl:variable>
		<!--
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$desc_"/>
		</xsl:call-template>
		-->
		<table>
			<tr>
				<td width="90%">
					<xsl:value-of select="$desc_"/>
				</td>
				<td height="10" width="1%">
				</td>
			</tr>
			<tr>
			<!---
				<td width="90%">
					<xsl:copy-of select="$lab_frist_step"/>
				</td>
			-->
				<td>
					<xsl:text>1.&#160;</xsl:text>
					<a href="javascript:plan.Import.get_template('{$cur_lan}')" class="Text"><xsl:value-of select="$lab_template"/></a>					
					<xsl:text>&#160;|&#160;</xsl:text>
					<a href="javascript:plan.Import.get_instr()" class="Text"><xsl:value-of select="$lab_instr"/></a>							
				
				</td>
				<td height="10" width="1%">
				</td>
			</tr>
			<tr>
				<td width="90%">
					<xsl:value-of select="$lab_second_step"/>
				</td>
				<td height="10" width="1%">
				</td>
			</tr>
			<tr>
				<td width="90%">
					<xsl:value-of select="$lab_third_step"/>
				</td>
				<td height="10" width="1%">
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_file_location"/>:
				</td>
				<td class="wzb-form-control">
				    <xsl:call-template name="wb_gen_input_file">
						<xsl:with-param name="name">src_filename_path</xsl:with-param>
					</xsl:call-template>
					<!-- <input type="file" name="src_filename_path" class="wzb-inputText" style="width: 300;"/>-->
					<input type="hidden" name="src_filename"/> 
				</td>
			</tr>
			<tr>
				<td width="20%" align="right">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="80%" class="wzb-ui-module-text">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		</table>
		<div class='wzb-bar'>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_next"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:plan.Import.confirm(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
					<xsl:value-of select="$lab_g_form_btn_cancel"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:window.close();</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
