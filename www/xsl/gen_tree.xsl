<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:import href="utils/wb_init_lab.xsl"/>
<xsl:import href="wb_const.xsl"/>
<xsl:import href="utils/wb_css.xsl"/>
<xsl:import href="utils/wb_utils.xsl"/>
<xsl:import href="cust/wb_cust_const.xsl"/>
<xsl:import href="share/usr_detail_label_share.xsl"/>

<xsl:import href="share/gen_tree_js.xsl"/>
<!-- ====================================================================== -->
<!--<xsl:output method="html" indent="yes"/>-->
<xsl:variable name="tree_count" select="//tree/tree_info/count"/>
<xsl:variable name="js" select="//tree/tree_info/js"/>
<xsl:variable name="tree_xml" select="//tree/tree_info/xml"/>
<xsl:variable name="tree_width">
	<xsl:choose>
		<xsl:when test="$tree_count = 1">610</xsl:when>
		<xsl:when test="$tree_count = 2">300</xsl:when>
		<xsl:otherwise>200</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<xsl:variable name="tree_height">300</xsl:variable>
<xsl:variable name="button_width">98</xsl:variable>
<xsl:variable name="button_height">18</xsl:variable>
<xsl:variable name="applet_width"><xsl:value-of select="5+($tree_width+5)*$tree_count"/></xsl:variable>
<xsl:variable name="applet_height"><xsl:value-of select="$tree_height+$button_height+30"/></xsl:variable>	
<!-- ====================================================================== -->
<xsl:template match="/tree">
	<html><xsl:call-template name="wb_init_lab"/></html>
</xsl:template>
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
		<xsl:with-param name="lab_root_objective">資源文件夾</xsl:with-param>
		<xsl:with-param name="lab_root_lib_catalogue">目錄</xsl:with-param>
		<xsl:with-param name="lab_root_domain"><xsl:value-of select="$lab_const_domains"/></xsl:with-param>
		<xsl:with-param name="lab_root_folders">文件夾</xsl:with-param>
		<xsl:with-param name="lab_root_training_center">所有培訓中心</xsl:with-param>
		<xsl:with-param name="lab_root_skill">所有能力</xsl:with-param>
		<xsl:with-param name="lab_organizational">組織 </xsl:with-param>
		<xsl:with-param name="lab_global">共享</xsl:with-param>	
		<xsl:with-param name="lab_close">關閉</xsl:with-param>
		<xsl:with-param name="lab_move_objective">選擇你需要粘貼的文件夾。文件夾只能在同一個文件夾樹裡面移動。</xsl:with-param>
		<xsl:with-param name="lab_all_skillset">所有<xsl:value-of select="$lab_competency"/></xsl:with-param>
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
		<xsl:with-param name="lab_root_objective">资源文件夹</xsl:with-param>
		<xsl:with-param name="lab_root_lib_catalogue">目录</xsl:with-param>
		<xsl:with-param name="lab_root_domain"><xsl:value-of select="$lab_const_domains"/></xsl:with-param>
		<xsl:with-param name="lab_root_folders">文件夹</xsl:with-param>
		<xsl:with-param name="lab_root_training_center">所有培训中心</xsl:with-param>
		<xsl:with-param name="lab_root_skill">所有能力</xsl:with-param>
		<xsl:with-param name="lab_organizational">组织 </xsl:with-param>
		<xsl:with-param name="lab_global">!!!全球化</xsl:with-param>	
		<xsl:with-param name="lab_close">关闭</xsl:with-param>	
		<xsl:with-param name="lab_move_objective">选择您需要粘贴的文件夹。文件夹只能在同一个文件夹树里面移动。</xsl:with-param>
		<xsl:with-param name="lab_all_skillset">所有<xsl:value-of select="$lab_competency"/></xsl:with-param>
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
		<xsl:with-param name="lab_root_objective">Resource folders</xsl:with-param>
		<xsl:with-param name="lab_root_lib_catalogue">All catalogs</xsl:with-param>
		<xsl:with-param name="lab_root_domain"><xsl:value-of select="$lab_const_domains"/></xsl:with-param>		
		<xsl:with-param name="lab_root_folders">Folders</xsl:with-param>
		<xsl:with-param name="lab_root_training_center">All training centers</xsl:with-param>
		<xsl:with-param name="lab_root_skill">所有能力</xsl:with-param>
		<xsl:with-param name="lab_organizational">Organization </xsl:with-param>
		<xsl:with-param name="lab_global">Global </xsl:with-param>
		<xsl:with-param name="lab_close">Close</xsl:with-param>	
		<xsl:with-param name="lab_move_objective">Select the folder you want to paste into. Moving of a folder is confined within the same tree of folders.</xsl:with-param>
		<xsl:with-param name="lab_all_skillset">All <xsl:value-of select="$lab_competency"/></xsl:with-param>
	</xsl:call-template>
</xsl:template>
<!-- ====================================================================== -->
<xsl:template name="course">
		<xsl:param name="lab_now_loading"/>
		<xsl:param name="lab_catalog"/>
		<xsl:param name="lab_item"/>
		<xsl:param name="lab_competence"/>
		<xsl:param name="lab_user_group"/>
		<xsl:param name="lab_user_group_and_user"/>
		<xsl:param name="lab_appr_root"/>
		<xsl:param name="lab_grade"/>
		<xsl:param name="lab_grade_and_user"/>
		<xsl:param name="lab_class1"/>
		<xsl:param name="lab_class1_and_user"/>
		<xsl:param name="lab_industry"/>
		<xsl:param name="lab_industry_and_user"/>
		<xsl:param name="lab_knowledge_object"/>
		<xsl:param name="lab_root_catalog"/>
		<xsl:param name="lab_root_item"/>
		<xsl:param name="lab_root_competence"/>
		<xsl:param name="lab_root_user_group"/>
		<xsl:param name="lab_root_user_group_and_user"/>
		<xsl:param name="lab_root_grade"/>
		<xsl:param name="lab_root_grade_and_user"/>
		<xsl:param name="lab_root_class1"/>
		<xsl:param name="lab_root_class1_and_user"/>
		<xsl:param name="lab_root_industry"/>
		<xsl:param name="lab_root_industry_and_user"/>
		<xsl:param name="lab_root_knowledge_object"/>
		<xsl:param name="lab_root_objective"/>
		<xsl:param name="lab_root_lib_catalogue"/>
		<xsl:param name="lab_root_domain"/>
		<xsl:param name="lab_root_folders"/>
		<xsl:param name="lab_root_skill"/>
		<xsl:param name="lab_root_training_center"/>
		<xsl:param name="lab_organizational"/>
		<xsl:param name="lab_global"/>
		<xsl:param name="lab_close"/>
		<xsl:param name="lab_move_objective"/>
		<xsl:param name="lab_all_skillset"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title><xsl:value-of select="$wb_wizbank"/></title>
			<xsl:call-template name="new_css"/>	
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
		
				// from Applet to JS
				function pick_ok() {					  
					args = pick_ok.arguments
					/*
					for(i=0;i<args.length;i++){
						alert(args[i]);
					}<!--comment-->
					*/
					//alert('function  name '  +  args[0])<!--comment-->
					my_function = new Function()
					//alert(window.opener.name);<!--comment-->
					my_function = eval('window.opener.' + args[0])
					//alert(my_function);<!--comment-->
					args_str =''
					if(args.length ==2){
						args_str+="args[1]"
					}else if(args.length ==3){
						args_str+="args[1],args[2]"
					}else{
						args_str+="args[1],args[2],args[3]"
					}		
					eval("my_function("+ args_str +")")
					
					if (getUrlParam('close_option') == '1') {self.close();}	
						//parent.show_frame_content(url);
				}
	
				function pick_cancel() {
				//	setTimeout('self.close()',500);
				//	parent.show_frame_content(url);
				
				/*
				str='<html><head><script>self.close();<'
				str+='/script></head></html>'
				document.write(str);
				*/
				
				window.location = "../htm/close_window.htm";
				}
	
				function finish_loading() {
					parent.finish_loading();
				}
	
			]]></script>
		</head>		
		<xsl:choose>
			<xsl:when test="/tree/tree_list_js">
				<xsl:call-template name="tree_js">
					<xsl:with-param name="lab_now_loading" select="$lab_now_loading"/>
					<xsl:with-param name="lab_catalog" select="$lab_catalog"/>
					<xsl:with-param name="lab_item" select="$lab_item"/>
					<xsl:with-param name="lab_competence" select="$lab_competence"/>
					<xsl:with-param name="lab_user_group" select="$lab_user_group"/>
					<xsl:with-param name="lab_user_group_and_user" select="$lab_user_group_and_user"/>
					<xsl:with-param name="lab_appr_root" select="$lab_appr_root"/>
					<xsl:with-param name="lab_grade" select="$lab_grade"/>
					<xsl:with-param name="lab_grade_and_user" select="$lab_grade_and_user"/>
					<xsl:with-param name="lab_class1" select="$lab_class1"/>
					<xsl:with-param name="lab_class1_and_user" select="$lab_class1_and_user"/>
					<xsl:with-param name="lab_industry" select="$lab_industry"/>
					<xsl:with-param name="lab_industry_and_user" select="$lab_industry_and_user"/>
					<xsl:with-param name="lab_knowledge_object" select="$lab_knowledge_object"/>
					<xsl:with-param name="lab_root_catalog" select="$lab_root_catalog"/>
					<xsl:with-param name="lab_root_item" select="$lab_root_item"/>
					<xsl:with-param name="lab_root_competence" select="$lab_root_competence"/>
					<xsl:with-param name="lab_root_user_group" select="$lab_root_user_group"/>
					<xsl:with-param name="lab_root_user_group_and_user" select="$lab_root_user_group_and_user"/>
					<xsl:with-param name="lab_root_grade" select="$lab_root_grade"/>
					<xsl:with-param name="lab_root_grade_and_user" select="$lab_root_grade_and_user"/>
					<xsl:with-param name="lab_root_class1" select="$lab_root_class1"/>
					<xsl:with-param name="lab_root_class1_and_user" select="$lab_root_class1_and_user"/>
					<xsl:with-param name="lab_root_industry" select="$lab_root_industry"/>
					<xsl:with-param name="lab_root_industry_and_user" select="$lab_root_industry_and_user"/>
					<xsl:with-param name="lab_root_knowledge_object" select="$lab_root_knowledge_object"/>
					<xsl:with-param name="lab_root_objective" select="$lab_root_objective"/>
					<xsl:with-param name="lab_root_lib_catalogue" select="$lab_root_lib_catalogue"/>
					<xsl:with-param name="lab_root_domain" select="$lab_root_domain"/>
					<xsl:with-param name="lab_root_folders" select="$lab_root_folders"/>
					<xsl:with-param name="lab_root_training_center" select="$lab_root_training_center"/>
					<xsl:with-param name="lab_root_skill" select="$lab_root_skill"/>
					<xsl:with-param name="lab_organizational" select="$lab_organizational"/>
					<xsl:with-param name="lab_global" select="$lab_global"/>
					<xsl:with-param name="lab_close" select="$lab_close"/>
					<xsl:with-param name="lab_move_objective" select="$lab_move_objective"/>					
					<xsl:with-param name="lab_all_skillset" select="$lab_all_skillset"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<center>
					<div style="left:0;top:0;position:absolute; width:230px; height:420px; z-index:1; visibility: visible" id="DIV2" name="DIV2">
						<br/><br/><br/>
						<xsl:value-of select="$lab_gen_loading"/>
					</div>
				</center>
			 
				<script language="JavaScript" type="text/javascript"><![CDATA[
				 if(document.all){
				  document.writeln('<div style="left:-1500;top:0;position:absolute; width:{$applet_width}; height:{$applet_height}; z-index:1; visibility: visible" id="DIV1" name="DIV1">');
				 }
				]]></script>
				<body onload="wb_utils_finish_loading_applet();" marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" bgcolor="silver">
					<APPLET CODEBASE="../applet/gen_tree" CODE="genTree.class" archive="genTree.jar,pvTree.jar" WIDTH="{$applet_width}" HEIGHT="{$applet_height}" mayscript="mayscript">
						
						<PARAM NAME="CODE" VALUE="genTree.class"/>
						<PARAM NAME="JAVA_CODEBASE" VALUE="../applet/gen_tree"/>
						<PARAM NAME="JAVA_ARCHIVE" VALUE="genTree.jar,pvTree.jar"/>
						<PARAM NAME="MAYSCRIPT" VALUE="true"/>
						<PARAM NAME="SCRIPTABLE" VALUE="true"/>
						<PARAM NAME="TYPE" VALUE="application/x-java-applet;jpi-version=1.3.0_01"/>
						<PARAM NAME="BGCOLOR" VALUE=""/>
						
						<PARAM NAME="DEBUG_MODE" VALUE="0"/>
						<PARAM NAME="JS_NAME" VALUE="{$js}"/>
						<PARAM NAME="TREE_WIDTH" VALUE="{$tree_width}"/>
						<PARAM NAME="TREE_HEIGHT" VALUE="{$tree_height}"/>
						<PARAM NAME="BUTTON_WIDTH" VALUE="{$button_width}"/>
						<PARAM NAME="BUTTON_HEIGHT" VALUE="{$button_height}"/>
						<PARAM NAME="LABEL_OK" VALUE="{$lab_gen_ok}"/>
						<PARAM NAME="LABEL_CANCEL" VALUE="{$lab_close}"/>
						<PARAM NAME="LABEL_NOW_LOADING" VALUE="{$lab_now_loading}"/>
						<!--<PARAM NAME="LABEL_TREE_ROOT" VALUE="{$lab_tree_root}"/>-->
						<PARAM NAME="SERVLET_URL" VALUE="../../servlet/{$wb_servlet_package_qdbAction}"/>
						<PARAM NAME="GET_ENC_QUERY_PREFIX_GEN_TREE" VALUE="cmd=gen_tree"/>
						<PARAM NAME="GET_ENC_QUERY_PREFIX_GET_TREE_DATA" VALUE="cmd=get_tree_data"/>
						<xsl:choose>
							<xsl:when test="$encoding != 'UTF-8'">
								<PARAM NAME="ENCODING" VALUE="{$encoding}"/>
							</xsl:when>
							<xsl:otherwise>
								<PARAM NAME="ENCODING" VALUE="UTF8"/>				
							</xsl:otherwise>
						</xsl:choose>
						<PARAM NAME="TREE_XML" VALUE="{$tree_xml}"/>
						<PARAM NAME="LABEL_GLOBAL" VALUE="{$lab_global}"/>
						<PARAM NAME="LABEL_ORGANIZATIONAL" VALUE="{$lab_organizational}"/>			
						<PARAM NAME="LABEL_CATALOG" VALUE="{$lab_catalog}"/>
						<PARAM NAME="LABEL_ITEM" VALUE="{$lab_item}"/>
						<PARAM NAME="LABEL_COMPETENCE" VALUE="{$lab_competence}"/>
						<PARAM NAME="LABEL_USER_GROUP" VALUE="{$lab_user_group}"/>
						<PARAM NAME="LABEL_USER_GROUP_AND_USER" VALUE="{$lab_user_group_and_user}"/>
						<PARAM NAME="LABEL_appr_ROOT" VALUE="{$lab_appr_root}"/>
						<PARAM NAME="LABEL_GRADE" VALUE="{$lab_grade}"/>
						<PARAM NAME="LABEL_GRADE_AND_USER" VALUE="{$lab_grade_and_user}"/>
						<PARAM NAME="LABEL_INDUSTRY" VALUE="{$lab_industry}"/>
						<PARAM NAME="LABEL_INDUSTRY_AND_USER" VALUE="{$lab_industry_and_user}"/>
						<PARAM NAME="LABEL_CLASS1" VALUE="{$lab_class1}"/>
						<PARAM NAME="LABEL_CLASS1_AND_USER" VALUE="{$lab_class1_and_user}"/>
						<PARAM NAME="LABEL_KNOWLEDGE_OBJECT" VALUE="{$lab_knowledge_object}"/>
						<PARAM NAME="LABEL_MOVE_OBJECTIVE" VALUE	="{$lab_move_objective}"/>			
						
						<PARAM NAME="LABEL_ROOT_CATALOG" VALUE="{$lab_root_catalog}"/>
						<PARAM NAME="LABEL_ROOT_ITEM" VALUE="{$lab_root_item}"/>
						<PARAM NAME="LABEL_ROOT_COMPETENCE" VALUE="{$lab_root_competence}"/>
						<PARAM NAME="LABEL_ROOT_USER_GROUP" VALUE="{$lab_root_user_group}"/>
						<PARAM NAME="LABEL_ROOT_USER_GROUP_AND_USER" VALUE="{$lab_root_user_group_and_user}"/>
						<PARAM NAME="LABEL_ROOT_GRADE" VALUE="{$lab_root_grade}"/>
						<PARAM NAME="LABEL_ROOT_GRADE_AND_USER" VALUE="{$lab_root_grade_and_user}"/>
						<PARAM NAME="LABEL_ROOT_INDUSTRY" VALUE="{$lab_root_industry}"/>
						<PARAM NAME="LABEL_ROOT_INDUSTRY_AND_USER" VALUE="{$lab_root_industry_and_user}"/>
						<PARAM NAME="LABEL_ROOT_USER_CLASSIFICATION" VALUE="{$lab_root_class1}"/>
						<PARAM NAME="LABEL_ROOT_USER_CLASSIFICATION_AND_USER" VALUE="{$lab_root_class1_and_user}"/>
						<PARAM NAME="LABEL_ROOT_KNOWLEDGE_OBJECT" VALUE="{$lab_root_knowledge_object}"/>				
						<PARAM NAME="LABEL_ROOT_SYLLABUS_AND_OBJECT" VALUE="{$lab_root_objective}"/>				
						<PARAM NAME="LABEL_ROOT_KM_DOMAIN_AND_OBJECT" VALUE="{$lab_root_lib_catalogue}"/>
						<PARAM NAME="LABEL_ROOT_KM_DOMAIN" VALUE="{$lab_root_domain}"/>
						<PARAM NAME="LABEL_ROOT_KM_WORK_FOLDER_ONLY_EDIT" VALUE="{$lab_root_folders}"/>
						<PARAM NAME="LABEL_ROOT_KM_WORK_FOLDER" VALUE="{$lab_root_folders}"/>
						<SCRIPT LANGUAGE="JavaScript"><![CDATA[
						document.write("<PARAM name=\"BROWSERCOOKIE\" value=\"" + document.cookie + "\"/>");
						]]></SCRIPT>
					</APPLET>
					<script language="JavaScript" type="text/javascript"><![CDATA[			
					  if(document.all){
					   document.writeln('</div>');
					  }
					]]></script>
				</body>
			
			
			</xsl:otherwise>
		</xsl:choose>
		
		
</xsl:template>
<!-- ====================================================================== -->
</xsl:stylesheet>
