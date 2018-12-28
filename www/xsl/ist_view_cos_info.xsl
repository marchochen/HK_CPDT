<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_eff_date.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="my_privilege">AUTHOR</xsl:variable>
	<xsl:variable name="pos">0</xsl:variable>
	<xsl:variable name="ent_id" select="/course_list/cur_usr/@ent_id"/>
	<xsl:variable name="itm_type" select="/course/@type"/>
	<xsl:variable name="itm_content_def" select="/course/content_def/itm_content_def"/>
	<xsl:variable name="itm_run_ind" select="/course/content_def/itm_run_ind"/>
	<xsl:variable name="current_date">
		<xsl:choose>
			<xsl:when test="count(/resource_content/cur_time) = 0"/>
			<xsl:otherwise>
				<xsl:value-of select="/resource_content/cur_time"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="aicc_export_enable">
		<xsl:choose>
			<xsl:when test="/course[(@timestamp = @import_datetime) and @AICC ='true'] and not(//restype/text() != 'AICC_AU')">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="course_id">
		<xsl:value-of select="/course/@id"/>
	</xsl:variable>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<xsl:variable name="isEnrollment_related">
		<xsl:choose>
			<xsl:when test="not (/course/header/enrollment_related)">all</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="/course/header/enrollment_related"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="itm_exam_ind" select="/course/itm_exam_ind"/>
	<!-- ===================================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- ===================================================================== -->
	<xsl:template match="course">
		<head>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_course.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
					var course_lst = new wbCourse;
					
function modSelect() {
	var itm_exam_ind = ']]><xsl:value-of select="$itm_exam_ind"/><![CDATA[';
	var dis_mod_type;
	if (itm_exam_ind === 'true' ) {
		dis_mod_type = '3';
	} else {
		dis_mod_type = '2';
	}
	var url = wb_utils_invoke_disp_servlet('module','course.ModuleSelectModule','cmd','gen_sel_mod_win','is_multiple','true','dis_cos_type','3','dis_mod_type',dis_mod_type,'sel_type','import_mod','stylesheet','wb_goldman_sel_mod.xsl','width','100%','course_id',']]><xsl:value-of select="$course_id"/><![CDATA[');
	self.location.href = url;
}
				]]></SCRIPT>
				<xsl:call-template name="new_css"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<BODY marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="wb_utils_set_cookie('url_prev',self.location.href);">
			<FORM name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
			</FORM>
		</BODY>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_course_content_desc_5">線上學習的內容以樹形結構顯示在頁面左側。</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_6">由於該離線課程是統一內容模式，即線上學習內容在課程內設置，課程下所有班級共享一套學習內容。</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_7">從左側選擇對應的學習模塊可以查看模塊詳細信息，修改模塊的發布狀態及發佈時間等。</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_8">如果您要添加、刪除在線學習內容，請到課程下面操作。</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_1">線上課程的內容以樹形結構顯示在頁面左側。</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_2">要添加模塊夾或模塊，首先應點擊課程標題節點，然後點擊上面的“添加模塊夾”或“添加模塊”按鈕。要加模塊到某一模塊夾內，應點擊某一模塊夾節點，然後點擊上面的按鈕。</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_3">要查看或編輯模塊的屬性，則按一下模塊，這時會顯示模塊的詳細情況。</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_4">要匯出用來建立整個課程有關的AICC檔案，你可以按頁底的“匯出AICC Files”按鈕。請注意，如果你已利用左邊的窗格更改了課程結構，這個匯出功能將不可使用。</xsl:with-param>
			<xsl:with-param name="lab_instruction">說明</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_export">匯出 AICC 檔案</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_import_mod">匯入模塊</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_course_content_desc_5">在线学习的内容以树形结构显示在页面左侧。</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_6">由于该离线课程是统一内容模式，即网上学习内容在课程内设置，课程下所有班级共享一套学习内容。</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_7">从左侧选择对应的学习模块可以查看模块详细信息，修改模块的发布状态及发布时间等。</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_8">如果您要添加、删除在线学习内容，请到课程下面操作。</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_1">在线课程的内容以树形结构显示在页面左侧。</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_2">要添加模块夹或模块，首先应点击课程标题节点，然后点击上面的“添加模块夹”或“添加模块”按钮。要加模块夹或模块加到某一模块夹内，应点击某一模块夹，然后点击上面的按钮。</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_3">要查看或编辑模块的属性，应使相应的节点高亮显示，这时会显示模块的详细情况。</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_4">要导出用来建立整个课程有关的AICC文档，你可以点击下面的“导出AICC文档”按钮。请注意，如果您已经利用左边的窗格更改了课程结构，这个导出功能将不可使用。</xsl:with-param>
			<xsl:with-param name="lab_instruction">说明</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_export">导出 AICC 文档</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_import_mod">导入模块</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
		<xsl:with-param name="lab_course_content_desc_5">Online course content is presented in a tree structure from left hand side.</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_6">As this classroom course is using the common course content composition, which means all classes in this course share the same set of online content. So please set up the online content in course level.</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_7">By the way, you can click the module name on the left hand side to view module detail, modify publish status and publish time.</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_8">Please go back to course level if you would like to add or delete online content.</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_1">The online course content is shown on the left panel as a tree format.</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_2">To add a folder or a learning module, first highlight the course title node, then click the "Add folder" or "Add module" button above. To add a module within a folder, highlight the folder node first before adding the item.</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_3">To view or edit the properties of a module, click and choose the node on the tree and the details will be displayed.</xsl:with-param>
			<xsl:with-param name="lab_course_content_desc_4">To export the set of AICC files you used to create the entire course, click export AICC files at the bottom of the page. Note that this export function will not be available once you have changed the course structure using the left panel.</xsl:with-param>
			<xsl:with-param name="lab_instruction">Instruction</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_export">Export AICC files</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_import_mod">Import module</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_course_content_desc_1"/>
		<xsl:param name="lab_course_content_desc_2"/>
		<xsl:param name="lab_course_content_desc_3"/>
		<xsl:param name="lab_g_txt_btn_export"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_course_content_desc_4"/>
		<xsl:param name="lab_course_content_desc_5"/>
		<xsl:param name="lab_course_content_desc_6"/>
		<xsl:param name="lab_course_content_desc_7"/>
		<xsl:param name="lab_course_content_desc_8"/>
		<xsl:param name="lab_g_txt_btn_import_mod"/>
		<xsl:variable name="title_escaped">
			<xsl:call-template name="escape_js">
				<xsl:with-param name="input_str" select="title"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:if test="$aicc_export_enable = 'true'">
			<input value="wizb" name="env" type="hidden"/>
			<input value="" name="url_failure" type="hidden"/>
			<input value="" name="cmd" type="hidden"/>
			<input value="../resource/{$course_id}/aicc_course{$course_id}.zip" name="url_success" type="hidden"/>
			<input value="{$course_id}" name="course_id" type="hidden"/>
		</xsl:if>
		<xsl:if test="$isEnrollment_related != 'true' and $itm_type != 'VIDEO' and $itm_type != 'MOBILE' and $itm_type != 'AUDIOVIDEO'">
			<table  width="100%" >
				<tr>
					<td align="right" height="35">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_import_mod" />
							<xsl:with-param name="wb_gen_btn_href">javascript:modSelect()</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</table>
		</xsl:if>
		<xsl:call-template name="wb_ui_line"/>
		<!-- 追加 class="wzb-ui-desc-text"
		<table>
			<tr>
				<td height="10" >
					<xsl:value-of select="$lab_course_content_desc_1"/>
					<ul>
						<li>
							<xsl:copy-of select="$lab_course_content_desc_2"/>
						</li>
						<li>
							<xsl:copy-of select="$lab_course_content_desc_3"/>
						</li>
						<xsl:if test="$aicc_export_enable = 'true'">
							<li>
								<xsl:copy-of select="$lab_course_content_desc_4"/>
							</li>
						</xsl:if>
					</ul>
				</td>
			</tr>
		</table> -->
		<xsl:choose>
			<xsl:when test="$itm_content_def = 'PARENT' and $itm_run_ind = '1'">
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text" select="$lab_course_content_desc_5" />
				</xsl:call-template>
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text" select="$lab_course_content_desc_6" />
				</xsl:call-template>
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text" select="$lab_course_content_desc_7" />
				</xsl:call-template>
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text" select="$lab_course_content_desc_8" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="$itm_type != 'AUDIOVIDEO'">
					<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text" select="$lab_course_content_desc_1" />
					</xsl:call-template>
					<xsl:call-template name="wb_ui_desc">
						<xsl:with-param name="text" select="$lab_course_content_desc_2" />
					</xsl:call-template>
				</xsl:if>
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text" select="$lab_course_content_desc_3" />
				</xsl:call-template>
				<xsl:if test="$aicc_export_enable = 'true'">
					<xsl:call-template name="wb_ui_desc">
						<xsl:with-param name="text" select="$lab_course_content_desc_4" />
					</xsl:call-template>
				</xsl:if>
				<div class="wzb-bar">
					<xsl:if test="$aicc_export_enable = 'true'">
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_export"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:course_lst.export_aicc(document.frmXml)</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					<!--<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_close"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:parent.window.close()</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>-->
				</div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="env"/>
	<xsl:template match="cur_usr"/>
</xsl:stylesheet>
