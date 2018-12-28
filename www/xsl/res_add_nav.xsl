<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_win_hdr.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="share/wb_module_type_const.xsl"/>
	<xsl:import href="share/res_label_share.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="user"/>
		</html>
	</xsl:template>
	<!-- =============================================================================== -->
	<xsl:template match="user">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_objective.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_resource.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT language="JavaScript"><![CDATA[
	
	var obj = new wbObjective
	var course_lst = new wbCourse;
	var tst = new wbTst;
	var res = new wbResource;

	var obj_title = parent.parent.hidden.document.frmXml.obj_title.value
	var module_type = parent.parent.hidden.document.frmXml.mod_subtype.value

	gType		= getParentUrlParam('res_type')
	gSubType	= getParentUrlParam('res_subtype')
	gPrivilege	= getParentUrlParam('res_privilege')	
	gOrder	= getParentUrlParam('sort_order')
	gDiff		= getParentUrlParam('res_difficulty')
	gCol		= getParentUrlParam('sort_col')
		
	

	function init() {
			resize_frame();
			frm = document.forms[0];			
				
			if (gSubType == 'WCT'){
				frm.res_subtype.selectedIndex = 1
			}
			
			
			if (gSubType == 'SSC'){
				frm.res_subtype.selectedIndex = 1
			}		
			
	

			if (gCol == '') {
				frm.sort_order.selectedIndex = (gOrder == 'ASC') ? 0 : 1;
			}else if (gCol == 'res_id')	{
				frm.sort_order.selectedIndex =  (gOrder == 'ASC') ? 2 : 3;
			}else if(gCol == 'res_title'){
				frm.sort_order.selectedIndex =  (gOrder == 'ASC') ? 4 : 5;
			}

			if (gDiff == '1'){
				frm.res_difficulty.selectedIndex = 1;
			}else if(gDiff =='2'){
				frm.res_difficulty.selectedIndex = 2; 
			}else if(gDiff == '3'){
				frm.res_difficulty.selectedIndex = 3;
			}
			
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
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="new_css"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<base target="_parent"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onLoad="init()">
			<form>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_course_list">課程目錄</xsl:with-param>
			<xsl:with-param name="lab_edit_module">修改單元</xsl:with-param>
			<xsl:with-param name="lab_add_que">新增題目</xsl:with-param>
			<xsl:with-param name="lab_owner">建立者</xsl:with-param>
			<xsl:with-param name="lab_select_all">--全部--</xsl:with-param>
			<xsl:with-param name="lab_personal">個人</xsl:with-param>
			<xsl:with-param name="lab_public">共享</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_order">次序</xsl:with-param>
			<xsl:with-param name="lab_mod_time">修改時間</xsl:with-param>
			<xsl:with-param name="lab_res_id">資源編號</xsl:with-param>
			<xsl:with-param name="lab_pick_res">選取資源</xsl:with-param>
			<xsl:with-param name="lab_diff">難度</xsl:with-param>
			<xsl:with-param name="lab_hard">困難</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_refresh">更新</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_step_2_gen">第二步 - 選擇學習資源</xsl:with-param>
			<xsl:with-param name="lab_step_2_aicc">第二步 - 選擇AICC課件</xsl:with-param>
			<xsl:with-param name="lab_step_2_scorm">第二步 - 選擇SCORM課件 </xsl:with-param>
			<xsl:with-param name="lab_step_2_test">第二步 - 選擇測驗</xsl:with-param>
			<xsl:with-param name="lab_selected_category">資源文件夾</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_myself">本人</xsl:with-param>
			<xsl:with-param name="lab_descending">(倒序)</xsl:with-param>
			<xsl:with-param name="lab_ascending">(順序)</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_course_list">课程目录</xsl:with-param>
			<xsl:with-param name="lab_edit_module">修改模块</xsl:with-param>
			<xsl:with-param name="lab_add_que">添加题目</xsl:with-param>
			<xsl:with-param name="lab_owner">拥有者</xsl:with-param>
			<xsl:with-param name="lab_select_all">--全部--</xsl:with-param>
			<xsl:with-param name="lab_personal">个人</xsl:with-param>
			<xsl:with-param name="lab_public">共享</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_order">排序</xsl:with-param>
			<xsl:with-param name="lab_mod_time">修改时间</xsl:with-param>
			<xsl:with-param name="lab_res_id">资源编号</xsl:with-param>
			<xsl:with-param name="lab_pick_res">选取资源</xsl:with-param>
			<xsl:with-param name="lab_diff">难度</xsl:with-param>
			<xsl:with-param name="lab_hard">困难</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_refresh">刷新</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_step_2_gen">第二步 - 选择学习资源</xsl:with-param>
			<xsl:with-param name="lab_step_2_aicc">第二步 - 选择AICC课件</xsl:with-param>
			<xsl:with-param name="lab_step_2_scorm">第二步 - 选择SCORM课件</xsl:with-param>
			<xsl:with-param name="lab_step_2_test">第二步 - 选择测验</xsl:with-param>
			<xsl:with-param name="lab_selected_category">当前文件夹</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_myself">我的</xsl:with-param>
			<xsl:with-param name="lab_descending">(逆序)</xsl:with-param>
			<xsl:with-param name="lab_ascending">(顺序)</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_course_list">Course list</xsl:with-param>
			<xsl:with-param name="lab_edit_module">Edit module</xsl:with-param>
			<xsl:with-param name="lab_add_que">Add question</xsl:with-param>
			<xsl:with-param name="lab_owner">Creator</xsl:with-param>
			<xsl:with-param name="lab_select_all">--All--</xsl:with-param>
			<xsl:with-param name="lab_personal">Personal</xsl:with-param>
			<xsl:with-param name="lab_public">Public</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_order">Order</xsl:with-param>
			<xsl:with-param name="lab_mod_time">Last modified</xsl:with-param>
			<xsl:with-param name="lab_res_id">Resource ID</xsl:with-param>
			<xsl:with-param name="lab_pick_res">Pick knowledge objects</xsl:with-param>
			<xsl:with-param name="lab_diff">Difficulty</xsl:with-param>
			<xsl:with-param name="lab_hard">Hard</xsl:with-param>
			<xsl:with-param name="lab_normal">Normal</xsl:with-param>
			<xsl:with-param name="lab_easy">Easy</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_refresh">Refresh</xsl:with-param>
			<xsl:with-param name="lab_all">All</xsl:with-param>
			<xsl:with-param name="lab_step_2_gen">Step 2 - select learning material</xsl:with-param>
			<xsl:with-param name="lab_step_2_aicc">Step 2 - select AICC courseware</xsl:with-param>
			<xsl:with-param name="lab_step_2_scorm">Step 2 - select SCORM courseware</xsl:with-param>
			<xsl:with-param name="lab_step_2_test">Step 2 - select test</xsl:with-param>
			<xsl:with-param name="lab_selected_category">Selected category</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_myself">Myself</xsl:with-param>
			<xsl:with-param name="lab_descending"> (descending)</xsl:with-param>
			<xsl:with-param name="lab_ascending"> (ascending)</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_course_list"/>
		<xsl:param name="lab_edit_module"/>
		<xsl:param name="lab_add_que"/>
		<xsl:param name="lab_owner"/>
		<xsl:param name="lab_select_all"/>
		<xsl:param name="lab_personal"/>
		<xsl:param name="lab_public"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_order"/>
		<xsl:param name="lab_mod_time"/>
		<xsl:param name="lab_res_id"/>
		<xsl:param name="lab_pick_res"/>
		<xsl:param name="lab_diff"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_g_form_btn_refresh"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_step_2_gen"/>
		<xsl:param name="lab_step_2_aicc"/>
		<xsl:param name="lab_step_2_test"/>
		<xsl:param name="lab_selected_category"/>
		<xsl:param name="lab_g_txt_btn_cancel"/>
		<xsl:param name="lab_myself"/>
		<xsl:param name="lab_descending"/>
		<xsl:param name="lab_ascending"/>
		<xsl:param name="lab_step_2_scorm"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="wihe_class"></xsl:with-param>
			<xsl:with-param name="text">
			<script language="JavaScript" TYPE="text/javascript"><![CDATA[
			if ( gType == 'GEN' ){
				document.write(']]><xsl:value-of select="$lab_step_2_gen"/><![CDATA[');
			}else if ( gType == 'AICC' ){
				document.write(']]><xsl:value-of select="$lab_step_2_aicc"/><![CDATA[');
			}else if ( gType == 'ASM'){
				document.write(']]><xsl:value-of select="$lab_step_2_test"/><![CDATA[');
			}else if ( gType == 'SCORM'){
				document.write(']]><xsl:value-of select="$lab_step_2_scorm"/><![CDATA[');
			}
			]]>
			
			</script>
			 - (<xsl:value-of select="$lab_selected_category"/>:
<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[document.write(obj_title);]]></SCRIPT>)
			</xsl:with-param>
		</xsl:call-template>
	
	<!-- <xsl:call-template name="wb_ui_line"/>  -->	
	<!--  -->
	
	</xsl:template>
</xsl:stylesheet>
