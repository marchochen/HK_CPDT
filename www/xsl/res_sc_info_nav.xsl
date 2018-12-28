<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="module_id" select="/*/@id"/>
	<xsl:variable name="course_id" select="/*/header/@course_id"/>
	<xsl:variable name="mod_type" select="/*/header/@subtype"/>	
	<xsl:variable name="cur_tpl_nm" select="/*/header/template_list/@cur_tpl"/>
	<xsl:variable name="stylesheet" select="/*/header/template_list/template[@name=$cur_tpl_nm]/stylesheet"/>
	<xsl:variable name="mode" select="/*/@view_mode"/>
	
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="fixed_scenario | dynamic_scenario">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_module.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_assessment.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js" type="text/javascript"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				var module_lst = new wbModule 				
				var wb_tst = new wbTst;
				var asm = new wbAssessment;
			function go_summary(){
				parent.content.location.href = wb_tst.info_content_url(]]>'<xsl:value-of select="$module_id"/>','<xsl:value-of select="$course_id"/>', '<xsl:value-of select="$mod_type"/>', '<xsl:value-of select="@view_mode"/>'<![CDATA[);
			}	
				
			function resize_frame(){
				if(document.all){
					if(parent && parent.window && parent.window.resize_fs){
						parent.window.resize_fs(document.all['bottom_img'].offsetTop +5 );	
					}
				}else if(document.getElementById != null){
					if(parent && parent.window && parent.window.resize_fs){
						parent.window.resize_fs(document.getElementById('bottom_img').offsetTop +5 );	
					}			
				}
			}
			]]></SCRIPT>
			<xsl:call-template name="new_css"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="resize_frame()">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_tst_inst">
			<xsl:choose>
				<xsl:when test="$mod_type='FSC'">靜態情景題裡的所有題目都會添加到測驗中。點擊題目部分的<b>新增</b>按鈕，根據提示的步驟可以完成題目的添加。</xsl:when>
				<xsl:when test="$mod_type='DSC'">動態情景題裡的題目是在學員參加測驗時即時生成的。抽題條件用於指定題目的生成範圍。點擊抽題條件中的<b>新增條件</b>按鈕可以指定題目的範圍。</xsl:when>
			</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="lab_tst_builder">
				<xsl:choose>
					<xsl:when test="$mode = 'READONLY'">測驗內容</xsl:when>
					<xsl:otherwise>新增題目</xsl:otherwise>
				</xsl:choose>			
			</xsl:with-param>
			<xsl:with-param name="lab_dxt_builder">
				<xsl:choose>
					<xsl:when test="$mode = 'READONLY'">測驗內容</xsl:when>
					<xsl:otherwise>制作試卷</xsl:otherwise>
				</xsl:choose>			
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_finish">
				<xsl:choose>
					<xsl:when test="$mode = 'READONLY'">關閉</xsl:when>
					<xsl:otherwise>完成</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_bth_preview_test">預覽試卷</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_test_summary">題目摘要</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_tst_inst">
				<xsl:choose>
					<xsl:when test="$mod_type='FSC'">静态情景题里的所有题目都会添加到测验中。点击题目部分的<b>添加</b>按钮，根据提示的步骤可以完成题目的添加。</xsl:when>
					<xsl:when test="$mod_type='DSC'">动态情景题里的题目是在学员参加测验时按照抽题条件实时生成的。抽题条件用于指定题目的生成范围。点击抽题条件中的<b>添加条件</b>按钮可以指定题目的范围。</xsl:when>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="lab_tst_builder">
				<xsl:choose>
					<xsl:when test="$mode = 'READONLY'">测验内容</xsl:when>
					<xsl:otherwise>添加题目</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="lab_dxt_builder">
				<xsl:choose>
					<xsl:when test="$mode = 'READONLY'">测验内容</xsl:when>
					<xsl:otherwise>制作试卷</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_finish">
				<xsl:choose>
					<xsl:when test="$mode = 'READONLY'">关闭</xsl:when>
					<xsl:otherwise>完成</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_bth_preview_test">试卷预览</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_test_summary">题目摘要</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_tst_inst">
				<xsl:choose>
					<xsl:when test="$mod_type='FSC'">All questions in a fixed scenario are added into a test. To create a question, click <b>Add</b> in the question section and follow the steps.</xsl:when>
					<xsl:when test="$mod_type='DSC'">A dynamic scenario question generates questions in real time when users take the test. It is difined by a set of question selection criteria. Click <b>Add Criteria</b> in the selection criteria section to specify the criteria.</xsl:when>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="lab_tst_builder">
				<xsl:choose>
					<xsl:when test="$mode = 'READONLY'">Test content</xsl:when>
					<xsl:otherwise>Add question</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="lab_dxt_builder">
				<xsl:choose>
					<xsl:when test="$mode = 'READONLY'">Test content</xsl:when>
					<xsl:otherwise>Dynamic test builder</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_finish">
				<xsl:choose>
					<xsl:when test="$mode = 'READONLY'">Close</xsl:when>
					<xsl:otherwise>Finish</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_bth_preview_test">Preview test</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_test_summary">Question summary</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_tst_inst"/>
		<xsl:param name="lab_tst_builder"/>
		<xsl:param name="lab_dxt_inst"/>
		<xsl:param name="lab_g_txt_btn_finish"/>
		<xsl:param name="lab_g_txt_bth_preview_test"/>
		<xsl:param name="lab_g_txt_btn_test_summary"/>
		<xsl:param name="lab_dxt_builder"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_tst_builder"/>
				<xsl:text>&#160;-&#160;</xsl:text>
				<xsl:choose>
					<xsl:when test="header/title">
						<xsl:value-of select="header/title/text()"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="body/title/text()"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text">
				<xsl:copy-of select="$lab_tst_inst"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="extra_td">
				<td style="text-align:left">
					<xsl:if test="$mod_type != 'DXT' and $mod_type != 'DAS' and not($mode = 'READONLY') ">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_test_summary"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:go_summary();</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
				</td>
				<td align="right">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_finish"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:parent.window.close()</xsl:with-param>
					</xsl:call-template>
				</td>
			</xsl:with-param>
		</xsl:call-template>
		<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0" id="bottom_img"/>
		<xsl:call-template name="wb_ui_line"/>
	</xsl:template>
</xsl:stylesheet>
