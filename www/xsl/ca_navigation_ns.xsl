<?xml version='1.0' encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:import href="utils/wb_init_lab.xsl"/>
<xsl:import href="utils/escape_applet_param_doub_quo.xsl"/>
<xsl:import href="wb_const.xsl"/>
<xsl:import href="cust/wb_cust_const.xsl"/>
<xsl:import href="utils/wb_utils.xsl"/>
<xsl:import href="share/wb_applet_color.xsl"/>

<xsl:import href="share/ca_navigation_ns_js.xsl"/>

<xsl:output  indent="yes" />
<xsl:variable name="isEnrollment_related">
	<xsl:choose>
		<xsl:when test="not (/course/header/enrollment_related)">0</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="/course/header/enrollment_related"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<!-- =============================================================== -->
<xsl:template match="/">
	<html><xsl:call-template name="wb_init_lab"/></html>
</xsl:template>  
<!-- =============================================================== -->
	<xsl:template name="lang_ch" >
		<xsl:call-template name="course">
			<xsl:with-param name="lab_add_folder_tooltip">新增模塊夾</xsl:with-param>
			<xsl:with-param name="lab_add_module_tooltip">新增模塊</xsl:with-param>
			<xsl:with-param name="lab_add_test_tooltip">新增測驗</xsl:with-param>
			<xsl:with-param name="lab_add_vido_tooltip">新增視頻</xsl:with-param>
			<xsl:with-param name="lab_edit_node_tooltip">修改模塊</xsl:with-param>
			<xsl:with-param name="lab_remove_node_tooltip">刪除模塊</xsl:with-param>
			<xsl:with-param name="lab_reorder_node_tooltip">重整課程結構</xsl:with-param>
			<xsl:with-param name="lab_move_up_node_tooltip">將模塊或模塊夾上移</xsl:with-param>
			<xsl:with-param name="lab_move_down_node_tooltip">將模塊或模塊夾下移</xsl:with-param>
			<xsl:with-param name="lab_save_reorder_tooltip">儲存</xsl:with-param>
			<xsl:with-param name="lab_new_node_default_label">新模塊</xsl:with-param>
			<xsl:with-param name="lab_loading_applet">裝載中，請稍候......</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb" >
		<xsl:call-template name="course">
			<xsl:with-param name="lab_add_folder_tooltip">添加模块夹</xsl:with-param>
			<xsl:with-param name="lab_add_module_tooltip">添加模块</xsl:with-param>
			<xsl:with-param name="lab_add_test_tooltip">添加测验</xsl:with-param>
			<xsl:with-param name="lab_add_vido_tooltip">添加视频</xsl:with-param>
			<xsl:with-param name="lab_edit_node_tooltip">修改模块</xsl:with-param>
			<xsl:with-param name="lab_remove_node_tooltip">删除模块</xsl:with-param>
			<xsl:with-param name="lab_reorder_node_tooltip">重整课程结构</xsl:with-param>
			<xsl:with-param name="lab_move_up_node_tooltip">将模块或模块夹上移</xsl:with-param>
			<xsl:with-param name="lab_move_down_node_tooltip">将模块或模块夹下移</xsl:with-param>
			<xsl:with-param name="lab_save_reorder_tooltip">保存</xsl:with-param>
			<xsl:with-param name="lab_new_node_default_label">新模块</xsl:with-param>
			<xsl:with-param name="lab_loading_applet">装载中，请稍候......</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en" >
		<xsl:call-template name="course">
			<xsl:with-param name="lab_add_folder_tooltip">Add folder</xsl:with-param>
			<xsl:with-param name="lab_add_module_tooltip">Add module</xsl:with-param>
			<xsl:with-param name="lab_add_test_tooltip">Add test</xsl:with-param>
			<xsl:with-param name="lab_add_vido_tooltip">Add video</xsl:with-param>
			<xsl:with-param name="lab_edit_node_tooltip">Edit</xsl:with-param>
			<xsl:with-param name="lab_remove_node_tooltip">Remove</xsl:with-param>
			<xsl:with-param name="lab_reorder_node_tooltip">Reorder</xsl:with-param>
			<xsl:with-param name="lab_move_up_node_tooltip">Move up</xsl:with-param>
			<xsl:with-param name="lab_move_down_node_tooltip">Move down</xsl:with-param>
			<xsl:with-param name="lab_save_reorder_tooltip">Save</xsl:with-param>
			<xsl:with-param name="lab_new_node_default_label">New</xsl:with-param>
			<xsl:with-param name="lab_loading_applet">Loading, please wait...</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
<!-- =============================================================== -->
<xsl:template name="course">
<xsl:param name="lab_add_folder_tooltip"/>
<xsl:param name="lab_add_module_tooltip"/>
<xsl:param name="lab_edit_node_tooltip"/>
<xsl:param name="lab_remove_node_tooltip"/>
<xsl:param name="lab_reorder_node_tooltip"/>
<xsl:param name="lab_move_up_node_tooltip"/>
<xsl:param name="lab_move_down_node_tooltip"/>
<xsl:param name="lab_save_reorder_tooltip"/>
<xsl:param name="lab_new_node_default_label"/>
<xsl:param name="lab_loading_applet"/>
<xsl:param name="lab_add_test_tooltip"/>
<xsl:param name="lab_add_vido_tooltip"/>
<xsl:choose>
	<xsl:when test="/course/enableAppletTree/text() = 'false'">
		<xsl:call-template name="tree_js">
			<xsl:with-param name="srcid" select="/course/@id"/>
			<xsl:with-param name="title" select="/course/tableofcontents/@title"/>
			<xsl:with-param name="identifier" select="/course/tableofcontents/@identifier"/>
			<xsl:with-param name="lab_add_folder_tooltip" select="$lab_add_folder_tooltip"/>
			<xsl:with-param name="lab_add_module_tooltip" select="$lab_add_module_tooltip"/>
			<xsl:with-param name="lab_add_test_tooltip" select="$lab_add_test_tooltip"/>
			<xsl:with-param name="lab_add_vido_tooltip" select="$lab_add_vido_tooltip"/>
			<xsl:with-param name="lab_edit_node_tooltip" select="$lab_edit_node_tooltip"/>
			<xsl:with-param name="lab_remove_node_tooltip" select="$lab_remove_node_tooltip"/>
			<xsl:with-param name="lab_reorder_node_tooltip" select="$lab_reorder_node_tooltip"/>
			<xsl:with-param name="lab_move_up_node_tooltip" select="$lab_move_up_node_tooltip"/>
			<xsl:with-param name="lab_move_down_node_tooltip" select="$lab_move_down_node_tooltip"/>
			<xsl:with-param name="lab_save_reorder_tooltip" select="$lab_save_reorder_tooltip"/>
			<xsl:with-param name="lab_new_node_default_label" select="$lab_new_node_default_label"/>
			<xsl:with-param name="lab_loading_applet" select="$lab_loading_applet"/>
		</xsl:call-template>
	</xsl:when>
	<xsl:otherwise>
		<head>
		<title><xsl:value-of select="$wb_wizbank" /></title>
		<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		<script language="JavaScript" src="{$wb_js_path}wb_module.js" type="text/javascript"/>
		<script language="JavaScript" src="{$wb_js_path}wb_course.js" type="text/javascript"/>
		<script language="JavaScript" src="{$wb_js_path}gen_utils.js"  type="text/javascript"/>
		<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
		<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
		<script language="JavaScript" type="text/javascript"><![CDATA[
	
			// used for delete module
			mod_id = 0;
			mod_status = '';
			mod_timestamp = '';
			mod_attempted = '';
			mod_lang = '';
			//
			
			course_lst = new wbCourse
			mod = new wbModule
			cos_id = wb_utils_get_cookie('course_id');
		
			// from Applet to JS
			function show_course() {
				//alert("show course content:" + cos_id);	
				url = course_lst.view_info_url(cos_id);
				parent.show_frame_content(url);
			//	document.CourseAuthoring.cancelNavLock();
			}
	
			function add_folder() {
				//alert("add folder");
				url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','ca_ins_folder.xsl')
				parent.show_frame_content(url);
			}
	
			function folder_read_mode(name) {
				//alert("Read folder with name:" + name);
				wb_utils_set_cookie('folder_title',name);
				url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','ca_view_folder.xsl')
				parent.show_frame_content(url);
			//	document.CourseAuthoring.cancelNavLock();
			}
	
			function folder_edit_mode(name) {
				//alert("Edit folder with name:" + name);
				wb_utils_set_cookie('folder_title',name);
				url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','ca_edit_folder.xsl')
				parent.show_frame_content(url);
			}
	
			//function delete_module() {
			//	//alert("delete module");
			//	window.parent.deleteModule();
			//}
			
			function add_module(in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
				//alert("add module"+"\nin_course_id:"+in_course_id + "\n"+"in_course_timestamp:"+in_course_timestamp+ "\n"+"in_course_struct_xml_cnt:"+in_course_struct_xml_cnt+"\n"+"in_course_struct_xml_1:"+in_course_struct_xml_1);
				url = course_lst.ins_mod_select_url(cos_id)
				parent.add_module(url, in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1);
			//	parent.show_frame_content(url);
			}
	
			function mod_read_mode(mod_id) {
				//alert("Read Mode of Mod ID:" + mod_id);
				url = mod.view_info_url(mod_id)
				parent.show_frame_content(url);
			//	document.CourseAuthoring.cancelNavLock();
			}
	
			function mod_edit_mode(mod_id, in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
				//alert("Edit Mode of Mod ID:" + mod_id +"\n"+"in_course_id:"+in_course_id + "\n"+"in_course_timestamp:"+in_course_timestamp+ "\n"+"in_course_struct_xml_cnt:"+in_course_struct_xml_cnt+"\n"+"in_course_struct_xml_1:"+in_course_struct_xml_1);
				url = mod.upd_prep_url(mod_id,cos_id);
				parent.mod_edit_mode(url, in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1);
			//	parent.show_frame_content(url);
			}
			
			function get_cos_timestamp() {
				//alert("get_cos_timestamp");
				parent.get_cos_timestamp();
			}
	
			// from JS to Applet
			
			function addNode(node_title, mod_type, mod_id, course_timestamp) {
				//alert("Applet addNode\n"+"node_title:"+node_title+"\nmod_type:"+mod_type+"\nmod_id:"+mod_id+"\ncourse_timestamp:"+course_timestamp);
				document.CourseAuthoring.addNodeValues(node_title, mod_type, mod_id, course_timestamp);
			}
	
			function editNode(node_title) {
				//alert("Applet editNode\nnode_title:"+node_title);
				document.CourseAuthoring.setNodeValues(node_title);
			}
			
			function cancelAdd() {
				//alert("Applet cancelAdd");
				document.CourseAuthoring.cancelAdd();
			}
			
			function cancelEdit() {
				//alert("Applet cancelEdit");
				document.CourseAuthoring.cancelEdit();
			}
			
			function cancelDelete() {
				//alert("Applet cancelDelete");	
				document.CourseAuthoring.cancelDelete();
			}
			
			function canceNav() {
				//alert("Applet cancelNav");
				document.CourseAuthoring.cancelNavLock();
			}
			
			function confirmDelete(course_timestamp) {
				//alert("Applet confirmDelete");
				document.CourseAuthoring.confirmDelete(course_timestamp);
			}
	
			function saveCourseStructSuccess() {
				//alert("Applet saveCourseStructSuccess");
				document.CourseAuthoring.saveCourseStructSuccess();
			}
			
			function saveCourseStructFailure() {
				//alert("Applet saveCourseStructFailure");
				document.CourseAuthoring.saveCourseStructFailure();
			}
			function set_cos_timestamp(course_timestamp) {
	
				//alert("Applet set_cos_timestamp");
				document.CourseAuthoring.set_cos_timestamp(course_timestamp);
			}
			
			function getCosStructureXML(){
				document.CourseAuthoring.getCosStructureXML();	
			}
			
			// from JS to JS
			function set_del_module_var(id, status, timestamp, attempted, lang) {
				//alert("frames['NavPage' ].set_del_module_var:\nid:"+id+"\nstatus:"+status+"\ntimestamp:"+timestamp+"\nattempted:"+attempted+"\nlang:"+lang);
				mod_id = id;
				mod_status = status;
				mod_timestamp = timestamp;
				mod_attempted = attempted;
				mod_lang = lang;
			}
			
			function finish_loading() {
				parent.finish_loading();
			}
	
			// normal method
			function delete_module(in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
				//alert("aadelete module"+"\nin_course_id:"+in_course_id + "\n"+"in_course_timestamp:"+in_course_timestamp+ "\n"+"in_course_struct_xml_cnt:"+in_course_struct_xml_cnt+"\n"+"in_course_struct_xml_1:"+in_course_struct_xml_1);
				url = course_lst.del_mod_url(mod_id,cos_id,mod_status,mod_timestamp,mod_attempted,mod_lang);
				//alert("mod_status:",mod_status);
				if (url) {
			//		parent.show_frame_content(url);
					parent.delete_module(mod_id, mod_timestamp, in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1);
				}
			}
			
			function saveCourseStructXML(in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {	
				//alert("saveCourseStructXML" + "\nin_course_id:"+in_course_id + "\n"+"in_course_timestamp:"+in_course_timestamp+ "\n"+"in_course_struct_xml_cnt:"+in_course_struct_xml_cnt+"\n"+"in_course_struct_xml_1:"+in_course_struct_xml_1);
				parent.saveCourseStructXML(in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1);
			}
			
			]]></script>
			</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="wb_utils_finish_loading_applet();">
			<script language="JavaScript" type="text/javascript"><![CDATA[
				menu_width = screen.width - 50;
				nav_height = screen.height - 180 - 40;
				applet_width = menu_width;
				applet_height = screen.height - 180;
			
				//	document.writeln('<applet code="CourseAuthoring.class" codebase = "../applet/course_authoring/classes" archive = "../lib/util.jar,../lib/PerlTools.jar,../lib/pvTree.jar,../lib/pvButtons.jar,../lib/ca.jar" name="CourseAuthoring" width="' + applet_width + '" height="' + applet_height + '" bgcolor="white" mayscript>');
				//	document.writeln('<PARAM NAME="MENU_WIDTH" VALUE="' + menu_width + '" />');
				//	document.writeln('<PARAM NAME="MENU_HEIGHT" VALUE="40" />');
				//	document.writeln('<PARAM NAME="NAV_WIDTH" VALUE="200" />');
				//	document.writeln('<PARAM NAME="NAV_HEIGHT" VALUE="' + nav_height + '" />');
			]]></script>
			<center>
				<div style="left:0;top:0;position:absolute; width:230px; height:420px; z-index:1; visibility: visible" id="DIV2" name="DIV2">
					<br/><br/><br/>
					<xsl:value-of select="$lab_loading_applet" />
				</div>
			</center>
			<script language="JavaScript" type="text/javascript"><![CDATA[
				if(document.all){
					document.writeln('<div style="left:-500;top:0;position:absolute; width:230px; height:420px; z-index:1; visibility: visible" id="DIV1" name="DIV1">');
				}
			]]></script>
			<applet code="CourseAuthoring.class" codebase = "../applet/course_authoring/classes" archive = "../lib/util.jar,../lib/PerlTools.jar,../lib/pvTree.jar,../lib/pvButtons.jar,../lib/ca.jar" name="CourseAuthoring" width="230" height="420" bgcolor="white" mayscript="mayscript">
				<PARAM NAME="MENU_WIDTH" VALUE="230" />
				<PARAM NAME="MENU_HEIGHT" VALUE="50" />
				<PARAM NAME="NAV_WIDTH" VALUE="230" />
				<PARAM NAME="NAV_HEIGHT" VALUE="370" />
				<PARAM NAME="CODE" VALUE = "CourseAuthoring.class" />
				<PARAM NAME="java_codebase" VALUE="../applet/course_authoring/classes" />
				<PARAM NAME="java_archive" VALUE="../lib/util.jar,../lib/PerlTools.jar,../lib/pvTree.jar,../lib/pvButtons.jar,../lib/ca.jar" />
				<PARAM NAME="MAYSCRIPT" VALUE="true" />
				<PARAM NAME="scriptable" VALUE="true" />
				<PARAM NAME="type" VALUE="application/x-java-applet;jpi-version=1.3.0_01" />
			
				<PARAM NAME="DEBUG_MODE" VALUE="1" />
			
				<PARAM NAME="DOUBLE_QUOT_SYNTAX" VALUE="__quot__" />
				
				<PARAM NAME="BASIC_MENU_BTN_COLOR_1" VALUE="{$basic_menu_btn_color_1}" />
				<PARAM NAME="BASIC_MENU_BTN_COLOR_2" VALUE="{$basic_menu_btn_color_2}" />
				<PARAM NAME="BASIC_MENU_BTN_COLOR_3" VALUE="{$basic_menu_btn_color_3}" />
			
				<PARAM NAME="REORDER_MENU_BTN_COLOR_1" VALUE="{$reorder_menu_btn_color_1}" />
				<PARAM NAME="REORDER_MENU_BTN_COLOR_2" VALUE="{$reorder_menu_btn_color_2}" />
				<PARAM NAME="REORDER_MENU_BTN_COLOR_3" VALUE="{$reorder_menu_btn_color_3}" />
			
				<PARAM NAME="BASIC_MENU_PANEL_COLOR_1" VALUE="{$basic_menu_panel_color_1}" />
				<PARAM NAME="BASIC_MENU_PANEL_COLOR_2" VALUE="{$basic_menu_panel_color_2}" />
				<PARAM NAME="BASIC_MENU_PANEL_COLOR_3" VALUE="{$basic_menu_panel_color_3}" />
			
				<PARAM NAME="REORDER_MENU_PANEL_COLOR_1" VALUE="{$reorder_menu_panel_color_1}" />
				<PARAM NAME="REORDER_MENU_PANEL_COLOR_2" VALUE="{$reorder_menu_panel_color_2}" />
				<PARAM NAME="REORDER_MENU_PANEL_COLOR_3" VALUE="{$reorder_menu_panel_color_3}" />
			
				<PARAM NAME="ADD_FOLDER_TOOLTIP" VALUE="{$lab_add_folder_tooltip}" />
				<PARAM NAME="ADD_MODULE_TOOLTIP" VALUE="{$lab_add_module_tooltip}" />
				<PARAM NAME="EDIT_NODE_TOOLTIP" VALUE="{$lab_edit_node_tooltip}"/>
				<PARAM NAME="REMOVE_NODE_TOOLTIP" VALUE="{$lab_remove_node_tooltip}" />
				<PARAM NAME="REORDER_NODE_TOOLTIP" VALUE="{$lab_reorder_node_tooltip}" />
				<PARAM NAME="MOVE_UP_NODE_TOOLTIP" VALUE="{$lab_move_up_node_tooltip}" />
				<PARAM NAME="MOVE_DOWN_NODE_TOOLTIP" VALUE="{$lab_move_down_node_tooltip}" />
				<PARAM NAME="SAVE_REORDER_TOOLTIP" VALUE="{$lab_save_reorder_tooltip}" />
			
				<PARAM NAME="NEW_NODE_DEFAULT_LABEL" VALUE="{$lab_new_node_default_label}" />
			
				<PARAM NAME="SERVER_URL" VALUE="../../../servlet/{$wb_servlet_package_qdbAction}"/>
				<PARAM NAME="GET_ENC_QUERY_PREFIX" VALUE="cmd=get_site" />
				<PARAM NAME="GET_ENC_HEADER_FIELD" VALUE="wizbank_encoding" />
				<PARAM NAME="GET_COS_HEADER_QUERY_PREFIX" VALUE="cmd=get_cos_header" />
				<PARAM NAME="QUERY_PREFIX" VALUE="cmd=save_cos_struct" />
				<PARAM NAME="COURSE_STRUCT_XML_PARM_NAME" VALUE="course_struct_xml" />
				<PARAM NAME="COURSE_ID_PARM_NAME" VALUE="course_id" />
				<PARAM NAME="COURSE_ID_VALUE" VALUE="{//course/@id}" />
				<PARAM NAME="TIMESTAMP_PARM_NAME" VALUE="course_timestamp" />
				<PARAM NAME="COURSE_TIMESTAMP_VALUE" VALUE="{//course/@timestamp}" />
				<PARAM NAME="COURSE_STATUS_PARM_NAME" VALUE="course_status" />
			
				<PARAM NAME="ROOT_NODE_TITLE" VALUE="{//course/header/title/text()}" />
				<PARAM NAME="ROOT_NODE_TYPE" VALUE="ROT" />	
				
				<PARAM NAME="MENU_BAR_IMAGE" VALUE="../images/{$menu_bar_image}" />	
				<PARAM NAME="ISENROLLMENT_RELATED" VALUE="{$isEnrollment_related}" />	
				<xsl:apply-templates/>
			</applet>
			<script language="JavaScript" type="text/javascript"><![CDATA[if(document.all) {document.writeln('</div>');}]]></script>	
		</body>
	</xsl:otherwise>
</xsl:choose>
</xsl:template>
<!-- =============================================================== -->
<xsl:template match="tableofcontents">
	<xsl:call-template name="item"> 
		<xsl:with-param name="node_position">NODE</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="item">
	<xsl:param name="node_position"></xsl:param>
	<xsl:for-each select="item">
		<xsl:text disable-output-escaping="yes">&lt;</xsl:text><![CDATA[PARAM NAME="]]><xsl:value-of select="concat($node_position,'_',position(),'_TITLE')"/><![CDATA[" VALUE="]]><xsl:call-template name="escape_applet_param_doub_quo"><xsl:with-param name="my_right_value"><xsl:value-of select="@title"/></xsl:with-param><xsl:with-param name="my_left_value"/></xsl:call-template><![CDATA[" /]]><xsl:text disable-output-escaping="yes">&gt;</xsl:text>
		<xsl:text disable-output-escaping="yes">&lt;</xsl:text><![CDATA[PARAM NAME="]]><xsl:value-of select="concat($node_position,'_',position(),'_TYPE')"/><![CDATA[" VALUE="]]><xsl:value-of select="itemtype/text()"/><![CDATA[" /]]><xsl:text disable-output-escaping="yes">&gt;</xsl:text>
      	<xsl:if test="@identifierref">
			<xsl:text disable-output-escaping="yes">&lt;</xsl:text><![CDATA[PARAM NAME="]]><xsl:value-of select="concat($node_position,'_',position(),'_MOD_ID')"/><![CDATA[" VALUE="]]><xsl:value-of select="@identifierref"/><![CDATA[" /]]><xsl:text disable-output-escaping="yes">&gt;</xsl:text>
		</xsl:if>
      	<xsl:if test="restype">
			<xsl:text disable-output-escaping="yes">&lt;</xsl:text><![CDATA[PARAM NAME="]]><xsl:value-of select="concat($node_position,'_',position(),'_RES_TYPE')"/><![CDATA[" VALUE="]]><xsl:value-of select="restype/text()"/><![CDATA[" /]]><xsl:text disable-output-escaping="yes">&gt;</xsl:text>
		</xsl:if>
		<xsl:call-template name="item">
			<xsl:with-param name="node_position" select="concat($node_position,'_',position())"/>
		</xsl:call-template>
	</xsl:for-each>
</xsl:template>
<!-- =============================================================== -->
<xsl:template match="env"/>
<xsl:template match="cur_usr" />
<xsl:template match="header" />
<xsl:template match="itemtype" />
<!-- =============================================================== -->
</xsl:stylesheet>
