<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:import href="wb_const.xsl"/>
<xsl:import href="utils/wb_init_lab.xsl"/>
<xsl:import href="utils/escape_applet_param_doub_quo.xsl"/>
<xsl:import href="utils/wb_css.xsl"/>
<xsl:import href="utils/wb_utils.xsl"/>
<xsl:import href="cust/wb_cust_const.xsl"/>
<xsl:import href="share/wb_applet_color.xsl"/>
<xsl:import href="share/ugr_navigation_js.xsl"/>

<xsl:variable name="root_title"><xsl:value-of select="/tree/tableofcontents/@title"/></xsl:variable>

<xsl:variable name="applet_code_base_path">../applet/user_grade/classes</xsl:variable>
<xsl:variable name="applet_archive_path">../lib/</xsl:variable>

<!--
<xsl:variable name="applet_code_base_path"></xsl:variable>
<xsl:variable name="applet_archive_path"></xsl:variable>
-->
<xsl:variable name="applet_archive"><xsl:value-of select="$applet_archive_path"/>util.jar, <xsl:value-of select="$applet_archive_path"/>PerlTools.jar, <xsl:value-of select="$applet_archive_path"/>pvTree.jar, <xsl:value-of select="$applet_archive_path"/>pvButtons.jar, <xsl:value-of select="$applet_archive_path"/>user_grade.jar</xsl:variable>

<xsl:variable name="selected_id"><xsl:value-of select="/tree/selected_item/@id"/></xsl:variable>

<xsl:output  indent="yes" />

<!-- =============================================================== -->
<xsl:template match="/">
	<html><xsl:call-template name="wb_init_lab"/></html>
</xsl:template>  
<!-- =============================================================== -->
	<xsl:template name="lang_ch" >
		<xsl:call-template name="tree">
			<xsl:with-param name="lab_add_folder_tooltip">!!!新增模塊夾</xsl:with-param>
			<xsl:with-param name="lab_add_module_tooltip">添加</xsl:with-param>
			<xsl:with-param name="lab_edit_node_tooltip">修改</xsl:with-param>
			<xsl:with-param name="lab_remove_node_tooltip">刪除</xsl:with-param>
			<xsl:with-param name="lab_reorder_node_tooltip">重整結構</xsl:with-param>
			<xsl:with-param name="lab_move_up_node_tooltip">上移</xsl:with-param>
			<xsl:with-param name="lab_move_down_node_tooltip">下移</xsl:with-param>
			<xsl:with-param name="lab_save_reorder_tooltip">儲存</xsl:with-param>
			<xsl:with-param name="lab_cancel_reorder_tooltip">取消</xsl:with-param>
			<xsl:with-param name="lab_new_node_default_label">新節點</xsl:with-param>
			<xsl:with-param name="lab_loading_applet">裝載中，請稍候......</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb" >
		<xsl:call-template name="tree">
			<xsl:with-param name="lab_add_folder_tooltip">!!!添加模块夹</xsl:with-param>
			<xsl:with-param name="lab_add_module_tooltip">添加</xsl:with-param>
			<xsl:with-param name="lab_edit_node_tooltip">修改</xsl:with-param>
			<xsl:with-param name="lab_remove_node_tooltip">删除</xsl:with-param>
			<xsl:with-param name="lab_reorder_node_tooltip">重整结构</xsl:with-param>
			<xsl:with-param name="lab_move_up_node_tooltip">上移</xsl:with-param>
			<xsl:with-param name="lab_move_down_node_tooltip">下移</xsl:with-param>
			<xsl:with-param name="lab_save_reorder_tooltip">保存</xsl:with-param>
			<xsl:with-param name="lab_cancel_reorder_tooltip">取消</xsl:with-param>
			<xsl:with-param name="lab_new_node_default_label">新节点</xsl:with-param>
			<xsl:with-param name="lab_loading_applet">装载中，请稍候......</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en" >
		<xsl:call-template name="tree">
			<xsl:with-param name="lab_add_folder_tooltip">!!!Add folder</xsl:with-param>
			<xsl:with-param name="lab_add_module_tooltip">Add</xsl:with-param>
			<xsl:with-param name="lab_edit_node_tooltip">Edit</xsl:with-param>
			<xsl:with-param name="lab_remove_node_tooltip">Remove</xsl:with-param>
			<xsl:with-param name="lab_reorder_node_tooltip">Reorder</xsl:with-param>
			<xsl:with-param name="lab_move_up_node_tooltip">Move up</xsl:with-param>
			<xsl:with-param name="lab_move_down_node_tooltip">Move down</xsl:with-param>
			<xsl:with-param name="lab_save_reorder_tooltip">Save</xsl:with-param>
			<xsl:with-param name="lab_cancel_reorder_tooltip">Cancel</xsl:with-param>
			<xsl:with-param name="lab_new_node_default_label">New node</xsl:with-param>
			<xsl:with-param name="lab_loading_applet">Loading, please wait...</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
<!-- =============================================================== -->
<xsl:template name="tree">
	<xsl:param name="lab_add_folder_tooltip"/>
	<xsl:param name="lab_add_module_tooltip"/>
	<xsl:param name="lab_edit_node_tooltip"/>
	<xsl:param name="lab_remove_node_tooltip"/>
	<xsl:param name="lab_reorder_node_tooltip"/>
	<xsl:param name="lab_move_up_node_tooltip"/>
	<xsl:param name="lab_move_down_node_tooltip"/>
	<xsl:param name="lab_save_reorder_tooltip"/>
	<xsl:param name="lab_cancel_reorder_tooltip"/>
	<xsl:param name="lab_new_node_default_label"/>
	<xsl:param name="lab_loading_applet"/>
	<xsl:choose>
		<xsl:when test="/tree/enableAppletTree/text() = 'false'">
			<xsl:call-template name="tree_js">
				<xsl:with-param name="srcid" select="/tree/@id"/>
				<xsl:with-param name="title" select="/tree/tableofcontents/@title"/>
				<xsl:with-param name="identifier" select="/tree/tableofcontents/@identifier"/>
				<xsl:with-param name="lab_add_folder_tooltip" select="$lab_add_folder_tooltip"/>
				<xsl:with-param name="lab_add_module_tooltip" select="$lab_add_module_tooltip"/>
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
			<script language="JavaScript" src="{$wb_js_path}wb_ugr.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"  type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			
			<xsl:call-template name="new_css"/>
			
			<script language="JavaScript" type="text/javascript"><![CDATA[
				ugr = new wbUgr();
		
				// used for delete module
				mod_id = 0;
				mod_status = '';
				mod_timestamp = '';
				mod_attempted = '';
				mod_lang = '';
				//
				
				function wb_utils_finish_loading_applet(){
					// I.E.
					if(document.all){
						self.DIV2.style.left = -1500;
						self.DIV1.style.left = 0;
					}
					// NS6
						else if(document.getElementById){
						document.getElementById("DIV2").style.left = -500;
					}
					// NS other than 6
						else{
						document.layers["DIV2"].left = -500;
					}
				}
				
				/////////////////////////////////////////////////////////////////////////////
				// from Applet to JS
				
				function finish_loading() {
				}
				
				function home() {
					parent.home();
				}
				
				function refresh() {
					window.location.reload();
				}
				
				function ugr_read_mode(ugr_ent_id) {
					// Store the usergrade currently selected.
					wb_utils_set_cookie('ugr_ent_id',ugr_ent_id);
					url = ugr.ugr_url(ugr_ent_id);
					parent.show_frame_content(url);
				}
		
				function add_ugr(ugr_ent_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
					//alert("add_ugr\nugr_ent_id:"+ugr_ent_id+"\niin_course_timestamp:"+in_course_timestamp+"\nin_course_struct_xml_cnt:"+in_course_struct_xml_cnt+"\nin_course_struct_xml_1:"+in_course_struct_xml_1);
					url = ugr.ugr_ins_prep_url(ugr_ent_id);
					//alert(url);
					parent.add_ugr(url);
				}
		
				function ugr_edit_mode(ugr_ent_id, in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
					//alert("ugr_edit_mode\nugr_ent_id:"+ugr_ent_id+"\niin_course_timestamp:"+in_course_timestamp+"\nin_course_struct_xml_cnt:"+in_course_struct_xml_cnt+"\nin_course_struct_xml_1:"+in_course_struct_xml_1);
					url =  ugr.ugr_upd_prep_url(ugr_ent_id)
					parent.ugr_edit_mode(url);
				}
				
				function delete_ugr(ugr_ent_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
					//alert("delete_ugr\nugr_ent_id:"+ugr_ent_id+"\niin_course_timestamp:"+in_course_timestamp+"\nin_course_struct_xml_cnt:"+in_course_struct_xml_cnt+"\nin_course_struct_xml_1:"+in_course_struct_xml_1);
					url = ugr.ugr_del_url(']]><xsl:value-of select="$wb_lang"/><![CDATA[', ugr_ent_id);
					if (url=='') {
						cancel_delete();
					}
					else {
						parent.show_frame_content(url)
					}
				}
				
				function save_ugr_order(order) {
					//alert("save_ugr_order\norder:"+order);
					parent.save_ugr_order(order)
				}
				
				/////////////////////////////////////////////////////////////////////////////
				// from JS to Applet
				
				function cancel_add() {
					//alert("Applet cancel_add");
					document.UserGrade.cancelAdd();
				}
				
				function add_node(node_title, mod_type, mod_id, course_timestamp) {
					//alert("Applet add_node\nnode_title:"+node_title+"\nmod_type:"+mod_type+"\nmod_id:"+mod_id+"course_timestamp:"+course_timestamp);
					document.UserGrade.addNodeValues(node_title, mod_type, mod_id, course_timestamp);
				}
				
				function cancel_edit() {
					//alert("Applet cancel_edit");
					document.UserGrade.cancelEdit();
				}
				
				function edit_node(node_title) {
					//alert("Applet edit_node\nnode_title:"+node_title);
					document.UserGrade.setNodeValues(node_title);
				}
		
				function cancel_delete() {
					//alert("Applet cancel_delete");
					document.UserGrade.cancelDelete();
				}
						
				function confirm_delete() {
					//alert("Applet confirm_delete");
					document.UserGrade.confirmDelete(0);
				}
		
				function save_ugr_success() {
					//alert("Applet save_ugr_success");
					document.UserGrade.saveUgrSuccess();
				}
				]]></script>
				</head>
			<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="wb_utils_finish_loading_applet()">
				<script language="JavaScript" type="text/javascript"><![CDATA[
					menu_width = screen.width - 50;
					nav_height = screen.height - 180 - 40;
					applet_width = menu_width;
					applet_height = screen.height - 180;
				
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
				
				<xsl:call-template name="applet">
					<xsl:with-param name="lab_add_folder_tooltip"><xsl:value-of select="$lab_add_folder_tooltip"/></xsl:with-param>
					<xsl:with-param name="lab_add_module_tooltip"><xsl:value-of select="$lab_add_module_tooltip"/></xsl:with-param>
					<xsl:with-param name="lab_edit_node_tooltip"><xsl:value-of select="$lab_edit_node_tooltip"/></xsl:with-param>
					<xsl:with-param name="lab_remove_node_tooltip"><xsl:value-of select="$lab_remove_node_tooltip"/></xsl:with-param>
					<xsl:with-param name="lab_reorder_node_tooltip"><xsl:value-of select="$lab_reorder_node_tooltip"/></xsl:with-param>
					<xsl:with-param name="lab_move_up_node_tooltip"><xsl:value-of select="$lab_move_up_node_tooltip"/></xsl:with-param>
					<xsl:with-param name="lab_move_down_node_tooltip"><xsl:value-of select="$lab_move_down_node_tooltip"/></xsl:with-param>
					<xsl:with-param name="lab_save_reorder_tooltip"><xsl:value-of select="$lab_save_reorder_tooltip"/></xsl:with-param>
					<xsl:with-param name="lab_cancel_reorder_tooltip"><xsl:value-of select="$lab_cancel_reorder_tooltip"/></xsl:with-param>
					<xsl:with-param name="lab_new_node_default_label"><xsl:value-of select="$lab_new_node_default_label"/></xsl:with-param>
					<xsl:with-param name="lab_loading_applet"><xsl:value-of select="$lab_loading_applet"/></xsl:with-param>
				</xsl:call-template>
				
				<script language="JavaScript" type="text/javascript"><![CDATA[if(document.all) {document.writeln('</div>');}]]></script>	
			</body>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="applet">
	<xsl:param name="lab_add_folder_tooltip"/>
	<xsl:param name="lab_add_module_tooltip"/>
	<xsl:param name="lab_edit_node_tooltip"/>
	<xsl:param name="lab_remove_node_tooltip"/>
	<xsl:param name="lab_reorder_node_tooltip"/>
	<xsl:param name="lab_move_up_node_tooltip"/>
	<xsl:param name="lab_move_down_node_tooltip"/>
	<xsl:param name="lab_save_reorder_tooltip"/>
	<xsl:param name="lab_cancel_reorder_tooltip"/>
	<xsl:param name="lab_new_node_default_label"/>
	<xsl:param name="lab_loading_applet"/>

		<applet code="UserGrade.class" codebase = "{$applet_code_base_path}" archive = "{$applet_archive}" name="UserGrade" width="230" height="420" bgcolor="white" mayscript="mayscript">
			<PARAM NAME="MENU_WIDTH" VALUE="230" /> 
			<PARAM NAME="MENU_HEIGHT" VALUE="50" />
			<PARAM NAME="NAV_WIDTH" VALUE="230" />
			<PARAM NAME="NAV_HEIGHT" VALUE="370" />
			<PARAM NAME="CODE" VALUE = "UserGrade.class" />
			<PARAM NAME="java_codebase" VALUE="{$applet_code_base_path}" />
			<PARAM NAME="java_archive" VALUE="{$applet_archive}" />
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
			<PARAM NAME="CANCEL_REORDER_TOOLTIP" VALUE="{$lab_cancel_reorder_tooltip}" />
		
			<PARAM NAME="NEW_NODE_DEFAULT_LABEL" VALUE="{$lab_new_node_default_label}" />
		
			<PARAM NAME="SERVER_URL" VALUE="../../../servlet/qdbAction"/>
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
		
			<PARAM NAME="ROOT_NODE_TITLE" VALUE="{$root_title}" />
			<PARAM NAME="ROOT_NODE_TYPE" VALUE="ROT" />	
			
			<PARAM NAME="MENU_BAR_IMAGE" VALUE="../images/{$menu_bar_image}" />

			<PARAM NAME="SELECTED_ID" VALUE="{$selected_id}" />
			<xsl:apply-templates/>
		</applet>
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
		<xsl:text disable-output-escaping="yes">&lt;</xsl:text><![CDATA[PARAM NAME="]]><xsl:value-of select="concat($node_position,'_',position(),'_TYPE')"/><![CDATA[" VALUE="]]><xsl:value-of select="@itemtype"/><![CDATA[" /]]><xsl:text disable-output-escaping="yes">&gt;</xsl:text>
      	<xsl:if test="@identifierref">
			<xsl:text disable-output-escaping="yes">&lt;</xsl:text><![CDATA[PARAM NAME="]]><xsl:value-of select="concat($node_position,'_',position(),'_MOD_ID')"/><![CDATA[" VALUE="]]><xsl:value-of select="@identifierref"/><![CDATA[" /]]><xsl:text disable-output-escaping="yes">&gt;</xsl:text>
		</xsl:if>
      	<xsl:if test="@restype">
			<xsl:text disable-output-escaping="yes">&lt;</xsl:text><![CDATA[PARAM NAME="]]><xsl:value-of select="concat($node_position,'_',position(),'_RES_TYPE')"/><![CDATA[" VALUE="]]><xsl:value-of select="@restype"/><![CDATA[" /]]><xsl:text disable-output-escaping="yes">&gt;</xsl:text>
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
