<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="share/pick_obj_share.xsl"/>
	<!-- ====================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="course">
			<xsl:with-param name="lab_now_loading">請稍等 ...</xsl:with-param>
			<xsl:with-param name="lab_catalog">目錄</xsl:with-param>
			<xsl:with-param name="lab_item">課程</xsl:with-param>
			<xsl:with-param name="lab_competence">能力</xsl:with-param>
			<xsl:with-param name="lab_user_group">
				<xsl:value-of select="$lab_group"/>
			</xsl:with-param>
			<xsl:with-param name="lab_user_group_and_user">
				<xsl:value-of select="$lab_group"/>和用戶</xsl:with-param>
			<xsl:with-param name="lab_appr_root">所有下屬</xsl:with-param>
			<xsl:with-param name="lab_grade">
				<xsl:value-of select="$lab_grade"/>
			</xsl:with-param>
			<xsl:with-param name="lab_grade_and_user">
				<xsl:value-of select="$lab_grade"/>和用戶</xsl:with-param>
			<xsl:with-param name="lab_class1">Class 1</xsl:with-param>
			<xsl:with-param name="lab_class1_and_user">Class 1 and user</xsl:with-param>
			<xsl:with-param name="lab_industry">行業</xsl:with-param>
			<xsl:with-param name="lab_industry_and_user">行業和用戶</xsl:with-param>
			<xsl:with-param name="lab_knowledge_object">教學資源</xsl:with-param>
			<xsl:with-param name="lab_root_catalog">所有目錄</xsl:with-param>
			<xsl:with-param name="lab_root_item">所有課程</xsl:with-param>
			<xsl:with-param name="lab_root_competence">所有能力</xsl:with-param>
			<xsl:with-param name="lab_root_user_group">所有<xsl:value-of select="$lab_group"/>
			</xsl:with-param>
			<xsl:with-param name="lab_root_user_group_and_user">所有<xsl:value-of select="$lab_group"/>和用戶</xsl:with-param>
			<xsl:with-param name="lab_root_grade">所有<xsl:value-of select="$lab_grades"/>
			</xsl:with-param>
			<xsl:with-param name="lab_root_grade_and_user">所有<xsl:value-of select="$lab_grades"/>和用戶</xsl:with-param>
			<xsl:with-param name="lab_root_class1">All Class 1</xsl:with-param>
			<xsl:with-param name="lab_root_class1_and_user">All Class 1 and user</xsl:with-param>
			<xsl:with-param name="lab_root_industry">所有行業</xsl:with-param>
			<xsl:with-param name="lab_root_industry_and_user">所有行業和用戶</xsl:with-param>
			<xsl:with-param name="lab_root_knowledge_object">所有教學資源</xsl:with-param>
			<xsl:with-param name="lab_root_lib_catalogue">目錄</xsl:with-param>
			<xsl:with-param name="lab_root_domain">
				<xsl:value-of select="$lab_const_domains"/>
			</xsl:with-param>
			<xsl:with-param name="lab_root_folders">文件夾</xsl:with-param>
			<xsl:with-param name="lab_organizational">組織</xsl:with-param>
			<xsl:with-param name="lab_desc">以下內容來自教學資源管理。要選取題目請先選擇適當的文件夾。文件夾後面的括號中顯示的數字是其中包含的題目數目。</xsl:with-param>
			<xsl:with-param name="lab_step_1">第一步 - 選擇資源文件夾</xsl:with-param>
			<xsl:with-param name="lab_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_tree_root">所有資源文件夾</xsl:with-param>
			<xsl:with-param name="lab_count_suffix"></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="course">
			<xsl:with-param name="lab_now_loading">请稍等 ...</xsl:with-param>
			<xsl:with-param name="lab_catalog">目录</xsl:with-param>
			<xsl:with-param name="lab_item">课程</xsl:with-param>
			<xsl:with-param name="lab_competence">能力</xsl:with-param>
			<xsl:with-param name="lab_user_group">
				<xsl:value-of select="$lab_group"/>
			</xsl:with-param>
			<xsl:with-param name="lab_user_group_and_user">
				<xsl:value-of select="$lab_group"/>和用户</xsl:with-param>
			<xsl:with-param name="lab_appr_root">所有下属</xsl:with-param>
			<xsl:with-param name="lab_grade">
				<xsl:value-of select="$lab_grade"/>
			</xsl:with-param>
			<xsl:with-param name="lab_grade_and_user">
				<xsl:value-of select="$lab_grade"/>和用户</xsl:with-param>
			<xsl:with-param name="lab_class1">Class 1</xsl:with-param>
			<xsl:with-param name="lab_class1_and_user">Class 1 and user</xsl:with-param>
			<xsl:with-param name="lab_industry">行业</xsl:with-param>
			<xsl:with-param name="lab_industry_and_user">行业和用户</xsl:with-param>
			<xsl:with-param name="lab_knowledge_object">教学资源</xsl:with-param>
			<xsl:with-param name="lab_root_catalog">所有目录</xsl:with-param>
			<xsl:with-param name="lab_root_item">所有课程</xsl:with-param>
			<xsl:with-param name="lab_root_competence">所有能力</xsl:with-param>
			<xsl:with-param name="lab_root_user_group">所有<xsl:value-of select="$lab_group"/>
			</xsl:with-param>
			<xsl:with-param name="lab_root_user_group_and_user">所有<xsl:value-of select="$lab_group"/>和用户</xsl:with-param>
			<xsl:with-param name="lab_root_grade">所有<xsl:value-of select="$lab_grades"/>
			</xsl:with-param>
			<xsl:with-param name="lab_root_grade_and_user">所有<xsl:value-of select="$lab_grades"/>和用户</xsl:with-param>
			<xsl:with-param name="lab_root_class1">All Class 1</xsl:with-param>
			<xsl:with-param name="lab_root_class1_and_user">All Class 1 and user</xsl:with-param>
			<xsl:with-param name="lab_root_industry">所有行业</xsl:with-param>
			<xsl:with-param name="lab_root_industry_and_user">所有行业和用户</xsl:with-param>
			<xsl:with-param name="lab_root_knowledge_object">所有教学资源</xsl:with-param>
			<xsl:with-param name="lab_root_lib_catalogue">目录</xsl:with-param>
			<xsl:with-param name="lab_root_domain">
				<xsl:value-of select="lab_const_domains"/>
			</xsl:with-param>
			<xsl:with-param name="lab_root_folders">文件夹</xsl:with-param>
			<xsl:with-param name="lab_organizational">组织 </xsl:with-param>
			<xsl:with-param name="lab_desc">以下内容来自资源管理。要选取题目请先选择适当的文件夹。文件夹后面的括号中显示的数字是其中包含的题目的数目。</xsl:with-param>
			<xsl:with-param name="lab_step_1">第一步 - 选择题目所在的资源文件夹</xsl:with-param>
			<xsl:with-param name="lab_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_tree_root">所有资源文件夹</xsl:with-param>
			<xsl:with-param name="lab_count_suffix"></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="course">
			<xsl:with-param name="lab_now_loading">Now loading ...</xsl:with-param>
			<xsl:with-param name="lab_catalog">Catalogs</xsl:with-param>
			<xsl:with-param name="lab_item">Catalogs</xsl:with-param>
			<xsl:with-param name="lab_competence">Competence</xsl:with-param>
			<xsl:with-param name="lab_user_group">Group</xsl:with-param>
			<xsl:with-param name="lab_user_group_and_user">Group and user</xsl:with-param>
			<xsl:with-param name="lab_appr_root">All subordinates</xsl:with-param>
			<xsl:with-param name="lab_grade">Grade</xsl:with-param>
			<xsl:with-param name="lab_grade_and_user">Grade and user</xsl:with-param>
			<xsl:with-param name="lab_class1">Class 1</xsl:with-param>
			<xsl:with-param name="lab_class1_and_user">Class 1 and user</xsl:with-param>
			<xsl:with-param name="lab_industry">Industry</xsl:with-param>
			<xsl:with-param name="lab_industry_and_user">Industry and user</xsl:with-param>
			<xsl:with-param name="lab_knowledge_object">Knowledge object</xsl:with-param>
			<xsl:with-param name="lab_root_catalog">All catalogs</xsl:with-param>
			<xsl:with-param name="lab_root_item">All learning solutions</xsl:with-param>
			<xsl:with-param name="lab_root_competence">All competencies</xsl:with-param>
			<xsl:with-param name="lab_root_user_group">All groups</xsl:with-param>
			<xsl:with-param name="lab_root_user_group_and_user">All groups and users</xsl:with-param>
			<xsl:with-param name="lab_root_grade">All grades</xsl:with-param>
			<xsl:with-param name="lab_root_grade_and_user">All grades and users</xsl:with-param>
			<xsl:with-param name="lab_root_class1">All class 1</xsl:with-param>
			<xsl:with-param name="lab_root_class1_and_user">All class 1 and user</xsl:with-param>
			<xsl:with-param name="lab_root_industry">All industries</xsl:with-param>
			<xsl:with-param name="lab_root_industry_and_user">All industries and users</xsl:with-param>
			<xsl:with-param name="lab_root_knowledge_object">All knowledge objects</xsl:with-param>
			<xsl:with-param name="lab_root_lib_catalogue">All catalogs</xsl:with-param>
			<xsl:with-param name="lab_root_domain">
				<xsl:value-of select="$lab_const_domains"/>
			</xsl:with-param>
			<xsl:with-param name="lab_root_folders">Folders</xsl:with-param>
			<xsl:with-param name="lab_organizational">Organization </xsl:with-param>
			<xsl:with-param name="lab_step_1">Step 1 - select question folder</xsl:with-param>
			<xsl:with-param name="lab_desc">Below is the content of Learning Resource Management. Please select the folder in which you will pick the questions. The number in the bracket shows the count of questions in that folder.</xsl:with-param>
			<xsl:with-param name="lab_next">Next</xsl:with-param>
			<xsl:with-param name="lab_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_tree_root">All resource folders</xsl:with-param>
			<xsl:with-param name="lab_count_suffix"></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ====================================================================== -->
	<xsl:template name="pick_js_function">
		<script language="JavaScript" type="text/javascript"><![CDATA[
			var obj = new wbObjective;
			// from Applet to JS
			function pick_ok() {
				var args = pick_ok.arguments
				var tmp_val = new String(args[1]) //Man: For Netscape , Define data type.
				var split_val = tmp_val.split("~%~")	//Extract value
				var selected_obj_id = split_val[1]			//objective id at second elemen
				parent.hidden.document.frmXml.obj_title.value = split_val[2]
				obj.select_obj(selected_obj_id,parent.hidden.document.frmXml.mod_subtype.value)					//go to Next Page
			}

			function pick_cancel() {
				parent.window.location.href = obj.pick_obj_frame_url(parent.hidden.document.frmXml.cos_id.value,parent.hidden.document.frmXml.mod_id.value)
			}

			function finish_loading() {
				parent.finish_loading();
			}

		]]></script>
	</xsl:template>
</xsl:stylesheet>
