<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:import href="../utils/wb_css.xsl"/>
<xsl:import href="../utils/wb_gen_button.xsl"/>
<xsl:import href="../utils/wb_ui_hdr.xsl"/>
<xsl:import href="../utils/wb_ui_title.xsl"/>
<xsl:import href="../utils/wb_ui_desc.xsl"/> 
<xsl:import href="../utils/wb_ui_nav_link.xsl"/> 
<xsl:import href="../utils/trun_timestamp.xsl"/>
<xsl:import href="../utils/wb_gen_button.xsl"/>
<xsl:import href="../share/res_label_share.xsl"/> 

<xsl:variable name="deleted">DELETED</xsl:variable>

<xsl:template name="html_header">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		<xsl:call-template name="wb_css">
			<xsl:with-param name="view">wb_ui</xsl:with-param>
		</xsl:call-template>
		<title><xsl:value-of select="$wb_wizbank"/></title>
		<script src="{$wb_js_path}wb_utils.js" language="JavaScript"/>
		<script src="{$wb_js_path}gen_utils.js" language="JavaScript"/>
		<script src="{$wb_js_path}wb_objective.js" language="JavaScript"/>
		<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
		<xsl:choose>
			<xsl:when test="header/@type='GEN'">
				<script src="{$wb_js_path}wb_resource.js" language="JavaScript"/>
				<script language="JavaScript"><![CDATA[		
					res = new wbResource
					obj = new wbObjective
				]]></script>
			</xsl:when>
			<xsl:otherwise>
				<script language="JavaScript" src="{$wb_js_path}wb_question.js"/>
				<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
				<script language="JavaScript" src="{$wb_js_path}wb_aicc.js"/>
				<script src="{$wb_media_path}app/wb_applet_skin.js" language="JavaScript"/>
				<script language="JavaScript" src="{$wb_js_path}wb_resource.js"/>
				<script language="JavaScript" src="{$wb_js_path}wb_assessment.js"/>
				<script src="{$wb_js_path}urlparam.js" language="JavaScript"/>
				<script language="JavaScript"><![CDATA[
					que = new wbQuestion
					res = new wbResource
					asm = new wbAssessment
					obj = new wbObjective
					aicc = new wbAicc
				]]></script>
			</xsl:otherwise>
		</xsl:choose>
	</head>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="draw_header">
	<xsl:param name="upd_function"/>
	<xsl:param name="edit_q_function"/>
	<xsl:param name="preview_function"/>
	<xsl:param name="header"/>
	<xsl:choose>
		<xsl:when test="$wb_lang='ch'">
			<xsl:call-template name="dh_lang_ch">
				<xsl:with-param name="preview_function"><xsl:value-of select="$preview_function"/></xsl:with-param>
				<xsl:with-param name="upd_function"><xsl:value-of select="$upd_function"/></xsl:with-param>
				<xsl:with-param name="edit_q_function"><xsl:value-of select="$edit_q_function"/></xsl:with-param>
				<xsl:with-param name="header" select="$header"></xsl:with-param>
			</xsl:call-template>			
		</xsl:when>
		<xsl:when test="$wb_lang ='gb'">
			<xsl:call-template name="dh_lang_gb">
				<xsl:with-param name="preview_function"><xsl:value-of select="$preview_function"/></xsl:with-param>
				<xsl:with-param name="upd_function"><xsl:value-of select="$upd_function"/></xsl:with-param>
				<xsl:with-param name="edit_q_function"><xsl:value-of select="$edit_q_function"/></xsl:with-param>
				<xsl:with-param name="header" select="$header"></xsl:with-param>
			</xsl:call-template>			
		</xsl:when>
		<xsl:otherwise>
			<xsl:call-template name="dh_lang_en">
				<xsl:with-param name="preview_function"><xsl:value-of select="$preview_function"/></xsl:with-param>
				<xsl:with-param name="upd_function"><xsl:value-of select="$upd_function"/></xsl:with-param>
				<xsl:with-param name="edit_q_function"><xsl:value-of select="$edit_q_function"/></xsl:with-param>
				<xsl:with-param name="header" select="$header"></xsl:with-param>
			</xsl:call-template>			
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="dh_lang_ch">
	<xsl:param name="header"/>
	<xsl:call-template name="draw_header_content">
		<xsl:with-param name="lab_res_manager">教材管理</xsl:with-param>
		<xsl:with-param name="res_search_result">搜尋結果</xsl:with-param>
		<xsl:with-param name="lab_adm_home">管理員平台</xsl:with-param>
		<xsl:with-param name="lab_syb_manager">資源結構圖</xsl:with-param>
		<xsl:with-param name="mange_source">管理資源</xsl:with-param>
		<xsl:with-param name="header" select="$header"></xsl:with-param>
	</xsl:call-template>
</xsl:template>
<xsl:template name="dh_lang_gb">
	<xsl:param name="header"/>
	<xsl:call-template name="draw_header_content">
		<xsl:with-param name="lab_res_manager">教材管理</xsl:with-param>
		<xsl:with-param name="res_search_result">搜索结果</xsl:with-param>
		<xsl:with-param name="lab_adm_home">管理员平台</xsl:with-param>
		<xsl:with-param name="lab_syb_manager">资源结构图</xsl:with-param>
		<xsl:with-param name="mange_source">管理资源</xsl:with-param>
		<xsl:with-param name="header" select="$header"></xsl:with-param>
	</xsl:call-template>
</xsl:template>
<xsl:template name="dh_lang_en">
	<xsl:param name="header"/>
	<xsl:call-template name="draw_header_content">
		<xsl:with-param name="lab_res_manager">Knowledge manager</xsl:with-param>
		<xsl:with-param name="res_search_result">Search result</xsl:with-param>
		<xsl:with-param name="lab_adm_home">Administrator home</xsl:with-param>
		<xsl:with-param name="lab_syb_manager">Knowledge manager</xsl:with-param>
		<xsl:with-param name="mange_source">Management resources</xsl:with-param>
		<xsl:with-param name="header" select="$header"></xsl:with-param>
	</xsl:call-template>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="draw_header_content">
	<xsl:param name="lab_res_manager"/>
	<xsl:param name="res_search_result"/>
	<xsl:param name="mange_source"/>
	<xsl:param name="header"/>
	<xsl:call-template name="wb_ui_hdr"/>
	<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="//question/header/title">
						<xsl:value-of select="//question/header/title"/>
					</xsl:when>
					<xsl:when test="//body/title">
						<xsl:value-of select="//body/title"/>
					</xsl:when>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<h1><xsl:value-of select="$header"/></h1>
		<xsl:choose>
			<xsl:when test="$header ='YES'">
				<xsl:call-template name="wb_ui_nav_link">
					<xsl:with-param name="text">
						<span class="NavLink">
							<xsl:for-each select="//header/objective/path/node">
								<a href="javascript:obj.manage_obj_lst('','{@id}','','','false')" class="NavLink">
									<xsl:value-of select="."/>
								</a>
								<xsl:text>&#160;&gt;&#160;</xsl:text>
							</xsl:for-each>
							<a href="javascript:obj.manage_obj_lst('','{//header/objective/@id}','','','')" class="NavLink">
								<xsl:value-of select="//header/objective/desc"/>
							</a>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name" select="$mange_source"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:obj.show_obj_lst('','<xsl:value-of select="//header/objective/@id"/>','','','false')</xsl:with-param>
										<xsl:with-param name="class">NavLink</xsl:with-param>
							</xsl:call-template>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<xsl:value-of select="//body/title"/>
						</span>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_nav_link">
					<xsl:with-param name="text">
						<span class="navLink">
							<a href="javascript:window.location.href=wb_utils_get_cookie('search_result_url')" class="navLink">
								<xsl:value-of select="$res_search_result"/>
							</a>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<xsl:choose>
								<xsl:when test="//question/header/title">
									<xsl:value-of select="//question/header/title"/>
								</xsl:when>
								<xsl:when test="//body/title">
									<xsl:value-of select="//body/title"/>
								</xsl:when>
							</xsl:choose>
						</span>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="additional_information">
	<xsl:param name="timestamp"/>
	<xsl:param name="id_que"/>
	<xsl:param name="width"/>
	<xsl:param name="preview_function"/>
	<xsl:param name="upd_function"/>
	<xsl:param name="edit_q_function"/>
	<xsl:param name="del_function"/>
	<xsl:param name="type"/>
	<xsl:param name="mode"/>
	<xsl:param name="header"/>
	<xsl:choose>
		<xsl:when test="$wb_lang='ch'">
			<xsl:apply-templates select="header">
				<xsl:with-param name="res_type_undefined">沒有定義</xsl:with-param>
				<xsl:with-param name="res_id">資源編號</xsl:with-param>
				<xsl:with-param name="res_type">類型</xsl:with-param>
				<xsl:with-param name="res_online">已發佈</xsl:with-param>
				<xsl:with-param name="res_offline">未發佈</xsl:with-param>
				<xsl:with-param name="res_status">狀態</xsl:with-param>
				<xsl:with-param name="res_modified">修訂</xsl:with-param>
				<xsl:with-param name="res_modified_by">最後修改者</xsl:with-param>
				<xsl:with-param name="res_objective">資源文件夹</xsl:with-param>
				<xsl:with-param name="res_owner">建立者</xsl:with-param>
				
				<xsl:with-param name="width"><xsl:value-of select="$width"/></xsl:with-param>
				<xsl:with-param name="preview_function"><xsl:value-of select="$preview_function"/></xsl:with-param>
				<xsl:with-param name="upd_function"><xsl:value-of select="$upd_function"/></xsl:with-param>
				<xsl:with-param name="edit_q_function"><xsl:value-of select="$edit_q_function"/></xsl:with-param>
				<xsl:with-param name="del_function"><xsl:value-of select="$del_function"/></xsl:with-param>
				<xsl:with-param name="id_que"><xsl:value-of select="$id_que"/></xsl:with-param>
				<xsl:with-param name="timestamp"><xsl:value-of select="$timestamp"/></xsl:with-param>
				<xsl:with-param name="type"><xsl:value-of select="$type"/></xsl:with-param>
				<xsl:with-param name="mode" select="$mode"/>
				<xsl:with-param name="res_undefined">Undefined</xsl:with-param>
				<xsl:with-param name="lab_select_logic">選擇邏輯</xsl:with-param>
				<xsl:with-param name="lab_adaptive_logic">適應性</xsl:with-param>
				<xsl:with-param name="lab_random_logic">隨機性</xsl:with-param>
				<xsl:with-param name="lab_preview">預覽</xsl:with-param>
				<xsl:with-param name="lab_edit">修改</xsl:with-param>
				<xsl:with-param name="lab_asm_q">制作試卷</xsl:with-param>
				<xsl:with-param name="lab_asm_content">測驗內容</xsl:with-param>
				<xsl:with-param name="lab_remove">刪除</xsl:with-param>
				<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
				<xsl:with-param name="res_deleted">已刪除</xsl:with-param>
				<xsl:with-param name="mange_source">管理資源</xsl:with-param>
				<xsl:with-param name="header" select="$header"></xsl:with-param>
			</xsl:apply-templates>
		</xsl:when>
		<xsl:when test="$wb_lang='gb'">
			<xsl:apply-templates select="header">
				<xsl:with-param name="res_type_undefined">没有定义</xsl:with-param>
				<xsl:with-param name="res_id">资源编号</xsl:with-param>
				<xsl:with-param name="res_type">类型</xsl:with-param>
				<xsl:with-param name="res_online">已发布</xsl:with-param>
				<xsl:with-param name="res_offline">未发布</xsl:with-param>
				<xsl:with-param name="res_status">状态</xsl:with-param>
				<xsl:with-param name="res_modified">修订</xsl:with-param>
				<xsl:with-param name="res_modified_by">最后修改者</xsl:with-param>
				<xsl:with-param name="res_objective">资源文件夹</xsl:with-param>
				<xsl:with-param name="res_owner">建立者</xsl:with-param>

				<xsl:with-param name="width"><xsl:value-of select="$width"/></xsl:with-param>
				<xsl:with-param name="preview_function"><xsl:value-of select="$preview_function"/></xsl:with-param>
				<xsl:with-param name="upd_function"><xsl:value-of select="$upd_function"/></xsl:with-param>
				<xsl:with-param name="edit_q_function"><xsl:value-of select="$edit_q_function"/></xsl:with-param>
				<xsl:with-param name="del_function"><xsl:value-of select="$del_function"/></xsl:with-param>
				<xsl:with-param name="id_que"><xsl:value-of select="$id_que"/></xsl:with-param>
				<xsl:with-param name="timestamp"><xsl:value-of select="$timestamp"/></xsl:with-param>
				<xsl:with-param name="type"><xsl:value-of select="$type"/></xsl:with-param>
				<xsl:with-param name="mode" select="$mode"/>
				<xsl:with-param name="res_undefined">Undefined</xsl:with-param>
				<xsl:with-param name="lab_select_logic">选择逻辑</xsl:with-param>
				<xsl:with-param name="lab_adaptive_logic">适应性</xsl:with-param>
				<xsl:with-param name="lab_random_logic">随机性</xsl:with-param>
				<xsl:with-param name="lab_preview">预览</xsl:with-param>				
				<xsl:with-param name="lab_edit">修改</xsl:with-param>				
				<xsl:with-param name="lab_asm_q">制作试卷</xsl:with-param>
				<xsl:with-param name="lab_asm_content">测验内容</xsl:with-param>
				<xsl:with-param name="lab_remove">删除</xsl:with-param>
				<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
				<xsl:with-param name="res_deleted">已删除</xsl:with-param>
				<xsl:with-param name="mange_source">管理资源</xsl:with-param>
				<xsl:with-param name="header" select="$header"></xsl:with-param>
			</xsl:apply-templates>
		</xsl:when>
		<xsl:otherwise>
			<xsl:apply-templates select="header">
				<xsl:with-param name="res_id">Resource ID</xsl:with-param>
				<xsl:with-param name="res_type">Type</xsl:with-param>
				<xsl:with-param name="res_type_undefined">Undefined</xsl:with-param>
				<xsl:with-param name="res_online">Published</xsl:with-param>
				<xsl:with-param name="res_offline">Unpublished</xsl:with-param>
				<xsl:with-param name="res_status">Status</xsl:with-param>
				<xsl:with-param name="res_modified">Last modified</xsl:with-param>
				<xsl:with-param name="res_modified_by">Modified by</xsl:with-param>
				<xsl:with-param name="res_objective">Folder</xsl:with-param>
				<xsl:with-param name="res_owner">Creator</xsl:with-param>
				
				<xsl:with-param name="width"><xsl:value-of select="$width"/></xsl:with-param>
				<xsl:with-param name="preview_function"><xsl:value-of select="$preview_function"/></xsl:with-param>
				<xsl:with-param name="upd_function"><xsl:value-of select="$upd_function"/></xsl:with-param>
				<xsl:with-param name="edit_q_function"><xsl:value-of select="$edit_q_function"/></xsl:with-param>
				<xsl:with-param name="del_function"><xsl:value-of select="$del_function"/></xsl:with-param>
				<xsl:with-param name="id_que"><xsl:value-of select="$id_que"/></xsl:with-param>
				<xsl:with-param name="timestamp"><xsl:value-of select="$timestamp"/></xsl:with-param>
				<xsl:with-param name="type"><xsl:value-of select="$type"/></xsl:with-param>
				<xsl:with-param name="mode" select="$mode"/>
				<xsl:with-param name="res_undefined">Undefined</xsl:with-param>
				<xsl:with-param name="lab_select_logic">Question selection logic</xsl:with-param>
				<xsl:with-param name="lab_adaptive_logic">Adaptive</xsl:with-param>
				<xsl:with-param name="lab_random_logic">Random</xsl:with-param>
				<xsl:with-param name="lab_preview">Preview</xsl:with-param>
				<xsl:with-param name="lab_edit">Edit</xsl:with-param>
				<xsl:with-param name="lab_asm_q">Test builder</xsl:with-param>
				<xsl:with-param name="lab_asm_content">Test content</xsl:with-param>
				<xsl:with-param name="lab_remove">Remove</xsl:with-param>
				<xsl:with-param name="lab_lost_and_found">Deleted resources</xsl:with-param>
				<xsl:with-param name="res_deleted">Deleted</xsl:with-param>
				<xsl:with-param name="mange_source">Management resources</xsl:with-param>
				<xsl:with-param name="header" select="$header"></xsl:with-param>
			</xsl:apply-templates>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>
<!-- =============================================================== -->
<xsl:template match="header">
	<xsl:param name="res_type_undefined"/>
	<xsl:param name="res_id"/>
	<xsl:param name="res_type"/>
	<xsl:param name="res_online"/>
	<xsl:param name="res_offline"/>
	<xsl:param name="res_status"/>
	<xsl:param name="res_modified"/>
	<xsl:param name="res_modified_by"/>
	<xsl:param name="res_objective"/>
	<xsl:param name="res_owner"/>

	<xsl:param name="width"/>
	<xsl:param name="preview_function"/>
	<xsl:param name="upd_function"/>
	<xsl:param name="edit_q_function"/>
	<xsl:param name="del_function"/>
	<xsl:param name="type"/>
	<xsl:param name="mode"/>
	<xsl:param name="id_que"/>
	<xsl:param name="timestamp"/>
	<xsl:param name="res_undefined"/>	
	<xsl:param name="lab_select_logic"/>
	<xsl:param name="lab_adaptive_logic"/>
	<xsl:param name="lab_random_logic"/>
	<xsl:param name="lab_preview"/>
	<xsl:param name="lab_edit"/>
	<xsl:param name="lab_asm_q"/>
	<xsl:param name="lab_asm_content"/>
	<xsl:param name="lab_remove"/>
	<xsl:param name="lab_lost_and_found"/>
	<xsl:param name="res_deleted"/>
	<xsl:param name="mange_source"/>
	<xsl:param name="header"/>
	<xsl:call-template name="wb_ui_hdr">
		<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
		<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
	</xsl:call-template>
	<xsl:if test="$header != 'NO'">
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<span class="NavLink">
					<xsl:for-each select="objective/path/node">
						<a href="javascript:obj.manage_obj_lst('','{@id}','','','false')" class="NavLink">
							<xsl:value-of select="."/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
					</xsl:for-each>
					<a href="javascript:obj.manage_obj_lst('','{objective/@id}','','','false')" class="NavLink">
						<xsl:value-of select="objective/desc"/>
					</a>
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name" select="$mange_source"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:obj.show_obj_lst('','<xsl:value-of select="objective/@id"/>','','','false')</xsl:with-param>
								<xsl:with-param name="class">NavLink</xsl:with-param>
					</xsl:call-template>
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:choose>
						<xsl:when test="title">
							<xsl:value-of select="title"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="../body/title"/>
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</xsl:with-param>
		 </xsl:call-template>
	</xsl:if>
 	<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="../body/title"/>
			</xsl:with-param>
			<xsl:with-param name="extra_td">
				<td align="right">
					<xsl:if test="$preview_function!=''">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_preview"/>
							<xsl:with-param name="wb_gen_btn_href" select="$preview_function"/>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="$upd_function!=''">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_edit"/>
							<xsl:with-param name="wb_gen_btn_href" select="$upd_function"/>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="$edit_q_function!=''">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:choose>
									<xsl:when test="$mode = 'READONLY'"><xsl:value-of select="$lab_asm_content"/></xsl:when>
									<xsl:otherwise><xsl:value-of select="$lab_asm_q"/></xsl:otherwise>
								</xsl:choose>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href" select="$edit_q_function"/>
						</xsl:call-template>
					</xsl:if>
<!--					<xsl:if test="$upd_function!=''">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_export"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:asm.export_prep('<xsl:value-of select="$id_que"/>', '<xsl:value-of select="$type"/>')</xsl:with-param>
						</xsl:call-template>
					</xsl:if>-->
					<xsl:if test="$del_function!=''">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_remove"/>
								<xsl:with-param name="wb_gen_btn_href" select="$del_function"/>
							</xsl:call-template>
					</xsl:if>
				</td>
			</xsl:with-param>
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<table border="0" cellpadding="3" cellspacing="0" width="{$width}" class="Bg">
			<xsl:variable name="col_width">30%</xsl:variable>
			<tr>
				<td colspan="4" height="10">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
					<tr>
						<!-- ===========-->
						<td width="20%" align="right" valign="top">
							<span class="TitleText">
								<xsl:value-of select="$res_id"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td width="30%" valign="top">
							<span class="Text">
								<xsl:value-of select="../@id"/>
							</span>
						</td>
						<!-- ============-->
						<td width="20%" align="right" valign="top">
							<span class="TitleText">
								<xsl:value-of select="$res_owner"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td width="30%" valign="top">
							<span class="Text">
								<xsl:value-of select="../creation/user/display_bil/text()"/>
								<xsl:text> (</xsl:text><xsl:value-of select="../creation/user/@id"/><xsl:text>)</xsl:text>
							</span>
						</td>
					</tr>
					<!-- ===========-->
					<tr>
						<td width="20%" align="right" valign="top">
							<span class="TitleText">
								<xsl:value-of select="$res_type"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td width="{$col_width}">
							<span class="Text">
								<xsl:choose>
									<xsl:when test="@subtype='FAS'">
										<xsl:value-of select="$lab_fas"/>
									</xsl:when>
									<xsl:when test="@subtype='DAS'">
										<xsl:value-of select="$lab_das"/>
									</xsl:when>															
									<xsl:otherwise>
										<xsl:value-of select="$res_type_undefined"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
						<td width="20%" align="right" valign="top">
							<span class="TitleText">
								<xsl:value-of select="$res_objective"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td width="30%" valign="top">
							<span class="Text">
								<xsl:choose>
									<xsl:when test="objective/text()">
										<xsl:choose>
											<xsl:when test="objective/text() = 'LOST&amp;FOUND'">
												<xsl:value-of select="$lab_lost_and_found"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="objective/desc"/>
												<xsl:if test="objective/@status = $deleted">
												  (<xsl:value-of select="$res_deleted" />)                                                    											   </xsl:if>
                                </xsl:otherwise>
										</xsl:choose>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_empty"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
					</tr>
					<tr>
						<td width="20%" align="right" valign="top">
							<span class="TitleText">
								<xsl:value-of select="$res_status"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td width="30%" valign="top">
							<span class="Text">
								<xsl:choose>
									<xsl:when test="@status = 'ON'">
										<xsl:value-of select="$res_online"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$res_offline"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
						<td width="20%" align="right" valign="top">
							<span class="TitleText">
								<xsl:value-of select="$res_modified"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td width="{$col_width}" valign="top">
							<span class="Text">
								<xsl:call-template name="trun_timestamp">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="$timestamp"/>
									</xsl:with-param>
								</xsl:call-template>
							</span>
						</td>
					</tr>
					<tr>
						<xsl:choose>
							<xsl:when test="@subtype= 'DAS'">
								<td width="20%" align="right" valign="top">
									<span class="TitleText">
										<xsl:value-of select="$lab_select_logic"/>
										<xsl:text>：</xsl:text>
									</span>
								</td>
								<td width="30%" valign="top">
									<span class="Text">
								<xsl:choose>
									<xsl:when test="@selection_logic = 'ADT'"><xsl:value-of select="$lab_adaptive_logic"/></xsl:when>
									<xsl:otherwise><xsl:value-of select="$lab_random_logic"/></xsl:otherwise>
								</xsl:choose>
									</span>
								</td>
							</xsl:when>
							<xsl:otherwise>
								<td colspan="2">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
								</td>
							</xsl:otherwise>
						</xsl:choose>
						<td width="20%" align="right" valign="top">
							<span class="TitleText">
								<xsl:value-of select="$res_modified_by"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td width="{$col_width}" valign="top">
							<span class="Text">
								<xsl:value-of select="../last_update/user/display_bil/text()"/>
								<xsl:text> (</xsl:text><xsl:value-of select="../last_update/user/@id"/><xsl:text>)</xsl:text>
							</span>
						</td>
					</tr>
		</table>
</xsl:template>
<!-- =============================================================== -->
</xsl:stylesheet>
