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
	<xsl:import href="utils/draw_res_option_list.xsl"/>
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
	var course_id = parent.parent.hidden.document.frmXml.cos_id.value
	var course_name = parent.parent.hidden.document.frmXml.cos_title.value
	var module_name = parent.parent.hidden.document.frmXml.mod_title.value	

	gType		= getParentUrlParam('res_type')
	gSubType	= getParentUrlParam('res_subtype')
	gPrivilege	= getParentUrlParam('res_privilege')	
	gOrder	= getParentUrlParam('sort_order')
	gDiff		= getParentUrlParam('res_difficulty')
	gCol		= getParentUrlParam('sort_col')
		
	function getIndex(gType,gSubType){
		var idx = 0;
		]]>
		<!-- Call draw_res_option_lst.xsl -->
		<xsl:call-template name="draw_js_que_array"/>	
		<![CDATA[
		for(var i =0;i<TypeList.length;i++){
			if(TypeList[i] == gType){
				idx = i;
			}
			if(TypeList[i] == gSubType){
				idx = i;
			}					
		}
		return idx;
	}	

	function init() {
			resize_frame();
			frm = document.forms[0];
			var res_index = getIndex(gType,gSubType);
			frm.res_subtype.selectedIndex = res_index;			

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
			<xsl:with-param name="lab_step_2">第二步 - 選擇題目</xsl:with-param>
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
			<xsl:with-param name="lab_step_2">第二步 - 选择题目</xsl:with-param>
			<xsl:with-param name="lab_selected_category">资源文件夹</xsl:with-param>
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
			<xsl:with-param name="lab_step_2">Step 2 - select questions</xsl:with-param>
			<xsl:with-param name="lab_selected_category">Selected folder</xsl:with-param>
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
		<xsl:param name="lab_step_2"/>
		<xsl:param name="lab_selected_category"/>
		<xsl:param name="lab_g_txt_btn_cancel"/>
		<xsl:param name="lab_myself"/>
		<xsl:param name="lab_descending"/>
		<xsl:param name="lab_ascending"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="wihe_class"></xsl:with-param>
			<xsl:with-param name="text" ><xsl:value-of select="$lab_step_2"/>  - (
				<xsl:value-of select="$lab_selected_category"/>
				<xsl:text> : </xsl:text>
				<xsl:for-each select="objective_path/objective">
				<xsl:value-of select="text()"/>
				<xsl:if test="not(position()=last())">
					<xsl:text> > </xsl:text>
				</xsl:if>
			</xsl:for-each>
			)
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="extra_td">
				<td align="right">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right6</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:obj.pick_obj_frame(parent.parent.hidden.document.frmXml.cos_id.value,parent.parent.hidden.document.frmXml.mod_id.value)</xsl:with-param>
					</xsl:call-template>
				</td>
			</xsl:with-param>
		</xsl:call-template>
		
		<xsl:call-template name="wb_ui_line"/>
		<table cellpadding="2" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr class="Bg">
				<td valign="middle">
					<table cellpadding="0" cellspacing="0" border="0">
						<tr class="Bg">
							<td valign="middle">
								<img src="{$wb_img_path}tp.gif" width="5" height="1" border="0"/>
							</td>
							<!-- Type -->
							<td valign="middle">
								<span class="SmallText">
									<xsl:value-of select="$lab_type"/>：
								</span>
								<span class="Text">
									<input type="hidden" name="res_type" value="QUE"/>
									<input type="hidden" name="res_status" value="ON"/>
									<select name="res_subtype" class="Select" onchange = "javascript:obj.refresh_obj_lst(document.forms[0])">
										<xsl:call-template name="draw_que_option_list"/>
									</select>
								</span>
								
							</td>
							<td width="10">
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</td>
							<!-- Diffuculty-->
							<td valign="middle">
								<span class="SmallText">
									<xsl:value-of select="$lab_diff"/>：
								</span>
								<span class="Text">
									<select name="res_difficulty" class="Select" onchange = "javascript:obj.refresh_obj_lst(document.forms[0])">
										<option value="">
											<xsl:value-of select="$lab_select_all"/>
										</option>
										<option value="1">
											<xsl:value-of select="$lab_easy"/>
										</option>
										<option value="2">
											<xsl:value-of select="$lab_normal"/>
										</option>
										<option value="3">
											<xsl:value-of select="$lab_hard"/>
										</option>
									</select>
								</span>
								
							</td>
							<td width="10">
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</td>
							<!-- Language -->
							<input type="hidden" name="res_lan" value=""/>
							<!-- order-->
							<td valign="middle">
								<span class="SmallText">
									<xsl:value-of select="$lab_order"/>：
								</span>
								<span class="Text">
									<select name="sort_order" class="Select" onchange ="javascript:obj.refresh_obj_lst(document.forms[0])">
										<option value="~ASC">
											<xsl:value-of select="$lab_mod_time"/>
											<xsl:value-of select="$lab_ascending"/>
										</option>
										<option value="~DESC">
											<xsl:value-of select="$lab_mod_time"/>
											<xsl:value-of select="$lab_descending"/>
										</option>
										<option value="res_id~ASC">
											<xsl:value-of select="$lab_res_id"/>
											<xsl:value-of select="$lab_ascending"/>
										</option>
										<option value="res_id~DESC">
											<xsl:value-of select="$lab_res_id"/>
											<xsl:value-of select="$lab_descending"/>
										</option>
										<option value="res_title~ASC">
											<xsl:value-of select="$lab_title"/>
											<xsl:value-of select="$lab_ascending"/>
										</option>
										<option value="res_title~DESC">
											<xsl:value-of select="$lab_title"/>
											<xsl:value-of select="$lab_descending"/>
										</option>
									</select>
								</span>
							
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0" id="bottom_img"/>
	</xsl:template>
<!--	
	<xsl:template name="treenode" match="tree/xml/tree_list/tree/node">
		
		<xsl:when test="!/@type='ROOT_0_0_0' ">
		<xsl:value-of select="/tree/position(1)/@title"/>	
		</xsl:when>
	</xsl:template>
-->	
</xsl:stylesheet>
