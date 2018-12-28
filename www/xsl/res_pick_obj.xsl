<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:import href="share/pick_obj_share.xsl"/>
<!-- ====================================================================== -->
<xsl:template name="lang_ch">
	<xsl:call-template name="course">		
		<xsl:with-param name="lab_now_loading">請稍等 ...</xsl:with-param>
		<xsl:with-param name="lab_catalog">目錄</xsl:with-param>
		<xsl:with-param name="lab_item">課程</xsl:with-param>
		<xsl:with-param name="lab_competence">能力</xsl:with-param>
		<xsl:with-param name="lab_user_group"><xsl:value-of select="$lab_group"/></xsl:with-param>
		<xsl:with-param name="lab_user_group_and_user"><xsl:value-of select="$lab_group"/>和用戶</xsl:with-param>
		<xsl:with-param name="lab_appr_root">所有下屬</xsl:with-param>
		<xsl:with-param name="lab_grade"><xsl:value-of select="$lab_grade"/></xsl:with-param>
		<xsl:with-param name="lab_grade_and_user"><xsl:value-of select="$lab_grade"/>和用戶</xsl:with-param>
		<xsl:with-param name="lab_class1">Class 1</xsl:with-param>
		<xsl:with-param name="lab_class1_and_user">Class 1 and user</xsl:with-param>
		<xsl:with-param name="lab_industry">行業</xsl:with-param>
		<xsl:with-param name="lab_industry_and_user">行業和用戶</xsl:with-param>
		<xsl:with-param name="lab_knowledge_object">教學資源</xsl:with-param>
		<xsl:with-param name="lab_root_catalog">所有目錄</xsl:with-param>
		<xsl:with-param name="lab_root_item">所有課程</xsl:with-param>
		<xsl:with-param name="lab_root_competence">所有能力</xsl:with-param>
		<xsl:with-param name="lab_root_user_group">所有<xsl:value-of select="$lab_group"/></xsl:with-param>
		<xsl:with-param name="lab_root_user_group_and_user">所有<xsl:value-of select="$lab_group"/>和用戶</xsl:with-param>
		<xsl:with-param name="lab_root_grade">所有<xsl:value-of select="$lab_grades"/></xsl:with-param>
		<xsl:with-param name="lab_root_grade_and_user">所有<xsl:value-of select="$lab_grades"/>和用戶</xsl:with-param>
		<xsl:with-param name="lab_root_class1">All Class 1</xsl:with-param>
		<xsl:with-param name="lab_root_class1_and_user">All Class 1 and user</xsl:with-param>
		<xsl:with-param name="lab_root_industry">所有行業</xsl:with-param>
		<xsl:with-param name="lab_root_industry_and_user">所有行業和用戶</xsl:with-param>
		<xsl:with-param name="lab_root_knowledge_object">所有教學資源</xsl:with-param>
		<xsl:with-param name="lab_root_lib_catalogue">目錄</xsl:with-param>
		<xsl:with-param name="lab_root_domain"><xsl:value-of select="$lab_const_domains"/></xsl:with-param>
		<xsl:with-param name="lab_root_folders">文件夾</xsl:with-param>
		<xsl:with-param name="lab_organizational">組織 </xsl:with-param>
		<!--xsl:with-param name="lab_global">共享</xsl:with-param-->
		<xsl:with-param name="lab_desc">
		<script><![CDATA[
		if(res_type == 'GEN'){
			document.write('以下內容來自教學資源管理。要選取教材請先選擇適當的文件夾。文件夾後面的括號中顯示的數字是其中包含的教材數目。')
		}else if(res_type == 'AICC'){
			document.write('以下內容來自教學資源管理。要選取AICC 課件請先選擇適當的文件夾。文件夾後面的括號中顯示的數字是其中包含的AICC 課件數目。')
		}else if(res_type == 'ASM'){
			document.write('以下內容來自教學資源管理。要選取測驗請先選擇適當的文件夾。文件夾後面的括號中顯示的數字是其中包含的測驗數目。')
		}
		]]></script>			
		</xsl:with-param>
	<xsl:with-param name="lab_step_1">第一步 - 選擇資源文件夾</xsl:with-param>
	<xsl:with-param name="lab_next">下一步</xsl:with-param>
	<xsl:with-param name="lab_cancel">關閉</xsl:with-param>
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
		<xsl:with-param name="lab_user_group"><xsl:value-of select="$lab_group"/></xsl:with-param>
		<xsl:with-param name="lab_user_group_and_user"><xsl:value-of select="$lab_group"/>和用户</xsl:with-param>
		<xsl:with-param name="lab_appr_root">所有下属</xsl:with-param>
		<xsl:with-param name="lab_grade"><xsl:value-of select="$lab_grade"/></xsl:with-param>
		<xsl:with-param name="lab_grade_and_user"><xsl:value-of select="$lab_grade"/>和用户</xsl:with-param>
		<xsl:with-param name="lab_class1">Class 1</xsl:with-param>
		<xsl:with-param name="lab_class1_and_user">Class 1 and user</xsl:with-param>
		<xsl:with-param name="lab_industry">行业</xsl:with-param>
		<xsl:with-param name="lab_industry_and_user">行业和用户</xsl:with-param>
		<xsl:with-param name="lab_knowledge_object">教学资源</xsl:with-param>
		<xsl:with-param name="lab_root_catalog">所有目录</xsl:with-param>
		<xsl:with-param name="lab_root_item">所有课程</xsl:with-param>
		<xsl:with-param name="lab_root_competence">所有能力</xsl:with-param>
		<xsl:with-param name="lab_root_user_group">所有<xsl:value-of select="$lab_group"/></xsl:with-param>
		<xsl:with-param name="lab_root_user_group_and_user">所有<xsl:value-of select="$lab_group"/>和用户</xsl:with-param>
		<xsl:with-param name="lab_root_grade">所有<xsl:value-of select="$lab_grades"/></xsl:with-param>
		<xsl:with-param name="lab_root_grade_and_user">所有<xsl:value-of select="$lab_grades"/>和用户</xsl:with-param>
		<xsl:with-param name="lab_root_class1">All Class 1</xsl:with-param>
		<xsl:with-param name="lab_root_class1_and_user">All Class 1 and user</xsl:with-param>
		<xsl:with-param name="lab_root_industry">所有行业</xsl:with-param>
		<xsl:with-param name="lab_root_industry_and_user">所有行业和用户</xsl:with-param>
		<xsl:with-param name="lab_root_knowledge_object">所有教学资源</xsl:with-param>
		<xsl:with-param name="lab_root_lib_catalogue">目录</xsl:with-param>
		<xsl:with-param name="lab_root_domain"><xsl:value-of select="$lab_const_domains"/></xsl:with-param>
		<xsl:with-param name="lab_root_folders">文件夹</xsl:with-param>
		<xsl:with-param name="lab_organizational">组织 </xsl:with-param>
		<xsl:with-param name="lab_desc">
		<script><![CDATA[
		if(res_type == 'GEN'){
			document.write('以下内容来自资源管理。要选取学习资源请先选择适当的文件夹。文件夹后面的括号中显示的数字是其中包含的学习资源的数目。')
		}else if(res_type == 'AICC'){
			document.write('以下内容来自资源管理。要选取AICC课件请先选择适当的文件夹。文件夹后面的括号中显示的数字是其中包含的AICC课件的数目。')
		}else if(res_type == 'ASM'){
			document.write('以下内容来自资源管理。要选取测验请先选择适当的文件夹。文件夹后面的括号中显示的数字是其中包含的测验的数目。')
		}
		]]></script>			
		</xsl:with-param>
	<xsl:with-param name="lab_step_1">第一步 - 选择资源文件夹</xsl:with-param>
	<xsl:with-param name="lab_next">下一步</xsl:with-param>
<xsl:with-param name="lab_cancel">关闭</xsl:with-param>
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
		<xsl:with-param name="lab_root_domain"><xsl:value-of select="$lab_const_domains"/></xsl:with-param>		
		<xsl:with-param name="lab_root_folders">Folders</xsl:with-param>
		<xsl:with-param name="lab_organizational">Organization </xsl:with-param>
		<!--xsl:with-param name="lab_global">Global </xsl:with-param-->
		<xsl:with-param name="lab_step_1">Step 1 - select learning resource folder</xsl:with-param>
		<xsl:with-param name="lab_desc">
		<script><![CDATA[
		if(res_type == 'GEN'){
			document.write('Below is the content of Learning Resource Management.  Please select the folder in which you will pick the learning material. The number in the bracket shows the count of learning materials in that folder.')
		}else if(res_type == 'AICC'){
			document.write('Below is the content of Learning Resource Management.  Please select the folder in which you will pick the AICC Courseware. The number in the bracket shows the count of AICC Coursewares in that folder. ')
		}else if(res_type == 'ASM'){
			document.write('Below is the content of Learning Resource Management.  Please select the folder in which you will pick the Test. The number in the bracket shows the count of Test in that folder. ')
		}

		]]></script>			
		</xsl:with-param>
		<xsl:with-param name="lab_next">Next</xsl:with-param>
		<xsl:with-param name="lab_cancel">Close</xsl:with-param>
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
				var split_val =  tmp_val.split("~%~")	//Extract value
				var selected_obj_id = split_val[1]			//objective id at second elemen
				parent.hidden.document.frmXml.obj_title.value = split_val[2]
				obj.select_res_obj(selected_obj_id,parent.hidden.document.frmXml.res_type.value)					//go to Next Page
			}

			function pick_cancel() {
				parent.window.close()
			}

			function finish_loading() {
				parent.finish_loading();
			}

			var res_type = parent.hidden.document.frmXml.res_type.value;
		]]></script>
</xsl:template>
</xsl:stylesheet>
