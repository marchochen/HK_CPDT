<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="../utils/wb_init_lab.xsl"/>
	<xsl:import href="../wb_const.xsl"/>
	<xsl:import href="../utils/wb_css.xsl"/>
	<xsl:import href="../utils/wb_utils.xsl"/>
	<xsl:import href="../utils/wb_ui_title.xsl"/>
	<xsl:import href="../utils/wb_ui_desc.xsl"/>
	<xsl:import href="../cust/wb_cust_const.xsl"/>
	<xsl:import href="../share/usr_detail_label_share.xsl"/>
<xsl:import href="../share/pick_obj_share_js.xsl"/>
<!--<xsl:import href="../gen_tree_js.xsl"/>-->

	<!-- ====================================================================== -->
	<xsl:output  indent="yes"/>
	<xsl:variable name="tree_count" select="//tree/tree_info/count"/>
	<xsl:variable name="js" select="//tree/tree_info/js"/>
	<xsl:variable name="tree_xml" select="//tree/tree_info/xml"/>
	<xsl:variable name="tree_width">750</xsl:variable>
	<xsl:variable name="tree_height">300</xsl:variable>
	<xsl:variable name="button_width">98</xsl:variable>
	<xsl:variable name="button_height">18</xsl:variable>
	<xsl:variable name="applet_width">
		<xsl:value-of select="5+($tree_width+5)*$tree_count"/>
	</xsl:variable>
	<xsl:variable name="applet_height">
		<xsl:value-of select="$tree_height+$button_height+30"/>
	</xsl:variable>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<!-- ====================================================================== -->
	<xsl:template match="/tree">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<xsl:template name="course">
		<xsl:param name="lab_tree_root"/>
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
		<xsl:param name="lab_root_lib_catalogue"/>
		<xsl:param name="lab_root_domain"/>
		<xsl:param name="lab_root_folders"/>
		<xsl:param name="lab_organizational"/>
		<xsl:param name="lab_global"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_step_1"/>
		<xsl:param name="lab_next"/>
		<xsl:param name="lab_cancel"/>
		<xsl:param name="lab_count_suffix"/>
			<xsl:choose>
				<xsl:when test="/tree/tree_list_js">
					  <xsl:call-template name="tree_js">
							<xsl:with-param name="lab_tree_root" select="$lab_tree_root"/>
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
							<xsl:with-param name="lab_root_lib_catalogue" select="$lab_root_lib_catalogue"/>
							<xsl:with-param name="lab_root_domain" select="$lab_root_domain"/>
							<xsl:with-param name="lab_root_folders" select="$lab_root_folders"/>
							<xsl:with-param name="lab_organizational" select="$lab_organizational"/>
							<xsl:with-param name="lab_global" select="$lab_global"/>
							<xsl:with-param name="lab_desc" select="$lab_desc"/>
							<xsl:with-param name="lab_step_1" select="$lab_step_1"/>
							<xsl:with-param name="lab_next" select="$lab_next"/>
							<xsl:with-param name="lab_cancel" select="$lab_cancel"/>
							<xsl:with-param name="lab_count_suffix" select="$lab_count_suffix"/>			
					</xsl:call-template>					
				</xsl:when>
				<xsl:otherwise>		
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<xsl:call-template name="new_css"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_objective.js"/>
			<xsl:call-template name="pick_js_function"/>
		</head>
		<center>
			<div style="left:0;top:0;position:absolute; width:230px; height:420px; z-index:1; visibility: visible" id="DIV2" name="DIV2">
				<br/>
				<br/>
				<br/>
				<span class="Text">
					<xsl:value-of select="$lab_gen_loading"/>
				</span>
			</div>
		</center>
		<script language="JavaScript" type="text/javascript"><![CDATA[
	 if(document.all){
	  document.writeln('<div style="left:-1500;top:0;position:absolute; width:{$applet_width}; height:{$applet_height}; z-index:1; visibility: visible" id="DIV1" name="DIV1">');
	 }
	]]></script>
		<body onload="wb_utils_finish_loading_applet();" marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" bgcolor="silver">
			<table cellpadding="0" cellspacing="0" border="0" bgcolor="white" width="100%">
				<tr>
					<td>
						<xsl:call-template name="wb_ui_title">
							<xsl:with-param name="text" select="$lab_step_1"/>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_desc">
							<xsl:with-param name="text" select="$lab_desc"/>
						</xsl:call-template>
					</td>
				</tr>
			</table>
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
				<PARAM NAME="LABEL_OK" VALUE="{$lab_next}"/>
				<PARAM NAME="LABEL_CANCEL" VALUE="{$lab_cancel}"/>
				<PARAM NAME="LABEL_NOW_LOADING" VALUE="{$lab_now_loading}"/>
				<PARAM NAME="LABEL_TREE_ROOT" VALUE="{$lab_tree_root}"/>
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
				<PARAM NAME="LABEL_ROOT_KM_DOMAIN_AND_OBJECT" VALUE="{$lab_root_lib_catalogue}"/>
				<PARAM NAME="LABEL_ROOT_KM_DOMAIN" VALUE="{$lab_root_domain}"/>
				<PARAM NAME="LABEL_ROOT_KM_WORK_FOLDER_ONLY_EDIT" VALUE="{$lab_root_folders}"/>
				<PARAM NAME="LABEL_ROOT_KM_WORK_FOLDER" VALUE="{$lab_root_folders}"/>
				<param name="LABEL_ROOT_SYLLABUS_AND_OBJECT" value="{$lab_tree_root}"/>
				<param name="LABEL_NODE_COUNT_SUFFIX" value="{$lab_count_suffix}"/>
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
