<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="obj_id" select="/objective_list/objective/@id"/>
	<xsl:variable name="syb_id" select="/objective_list/objective/syllabus/@id"/>
	<xsl:variable name="obj_id_parent" select="//header/objective/path/node[position() = last()]/@id"/>
   <xsl:variable name="page_var" select="/objective_list/page_variant" />
   <xsl:variable name="curr" select="/objective_list/folders/text()" />
   <xsl:variable name="access" select="/objective_list/objective/body/access/text()" />
   <xsl:variable name="show_all" select="/objective_list/meta/show_all"/>
   <xsl:variable name="share_mode" select="/objective_list/share_mode/text()"/>
   <xsl:variable name="reader">READER</xsl:variable>
   <xsl:variable name="owner">OWNER</xsl:variable>
   <xsl:variable name="author">AUTHOR</xsl:variable>
   	<xsl:variable name="root_obj_tcr_id" select="/objective_list/meta/root_obj_tcr_id/text()"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="/objective_list">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}wb_objective.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}wb_search.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js" type="text/javascript"/>
			<script language="Javascript" TYPE="text/javascript"><![CDATA[
			obj = new wbObjective;
			seh = new wbSearch;

			function status(){
				seh.simple(document.frmXml,']]><xsl:value-of select="$wb_lang"/><![CDATA[')
				return false;
			}

		]]></script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frmXml" onsubmit="return status()">
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="search_title" value=""/>
				<input type="hidden" name="search_items_per_page" value=""/>
				<input type="hidden" name="stylesheet" value=""/>
				<!--<input type="hidden" name="folders" value="" />-->
				<input type="hidden" name="obj_id" value="{objective/@id}"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_categories">所有文件夾</xsl:with-param>
			<xsl:with-param name="lab_category_name">文件夾名稱</xsl:with-param>
			<xsl:with-param name="lab_desc">以下是這個文件夾的基本訊息。按<b>管理資源</b>可以查看並管理這個文件夾中的資源。</xsl:with-param>
			<xsl:with-param name="lab_desc_read">以下是這個文件夾的基本訊息。按<b>查看資源</b>可以查看這個文件夾中的資源。</xsl:with-param>
			<xsl:with-param name="lab_cat_id">文件夾編號</xsl:with-param>
			<xsl:with-param name="lab_res_catalog">資源文件夾</xsl:with-param>
			<xsl:with-param name="lab_total">總數</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
			<xsl:with-param name="lab_item">資源</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_sub_cat">新增子文件夾</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_paste">貼上</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_move">剪切與粘貼</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_access">訪問控制</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_show_obj">&#160;&#160;管理文件夾下的資源&#160;&#160;</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_show_read_obj">&#160;&#160;查看資源&#160;&#160;</xsl:with-param>
			<xsl:with-param name="lab_subcategories">子資源文件夾</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_no_obj">還沒有任何資源子文件夾</xsl:with-param>
			<xsl:with-param name="lab_categories">&#160;子文件夾</xsl:with-param>
			<xsl:with-param name="lab_shared">共享</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_categories">所有文件夹</xsl:with-param>
			<xsl:with-param name="lab_category_name">文件夹名称</xsl:with-param>
			<xsl:with-param name="lab_desc">以下是当前文件夹的基本信息。点击<b>管理资源</b>可以查看并管理当前文件夹中的资源。</xsl:with-param>
			<xsl:with-param name="lab_desc_read">以下是当前文件夹的基本信息。点击<b>查看资源</b>可以查看当前文件夹中的资源。</xsl:with-param>
			<xsl:with-param name="lab_cat_id">文件夹编号</xsl:with-param>
			<xsl:with-param name="lab_res_catalog">资源文件夹</xsl:with-param>
			<xsl:with-param name="lab_total">总计</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
			<xsl:with-param name="lab_item">资源</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">删除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_sub_cat">添加子文件夹</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_paste">粘贴</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_move">剪切与粘贴</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_access">访问控制</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_show_obj">&#160;&#160;管理文件夹下的资源&#160;&#160;</xsl:with-param>
         <xsl:with-param name="lab_g_txt_btn_show_read_obj">&#160;&#160;查看资源&#160;&#160;</xsl:with-param>
			<xsl:with-param name="lab_subcategories">子资源文件夹</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_no_obj">还没有任何资源子文件夹</xsl:with-param>
			<xsl:with-param name="lab_categories">&#160;子文件夹</xsl:with-param>
			<xsl:with-param name="lab_shared">共享</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_categories">Resource folders</xsl:with-param>
			<xsl:with-param name="lab_category_name">Folder name</xsl:with-param>
			<xsl:with-param name="lab_desc">Click <b>Manage Resources</b> to create and edit resources in this folder.</xsl:with-param>
			<xsl:with-param name="lab_desc_read">Click <b>View Resources</b> to view resources available in this folder.</xsl:with-param>
			<xsl:with-param name="lab_cat_id">Folder ID</xsl:with-param>
			<xsl:with-param name="lab_res_catalog">Resource folder</xsl:with-param>
			<xsl:with-param name="lab_total">Total</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">Deleted resources</xsl:with-param>
			<xsl:with-param name="lab_item">Resources</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_sub_cat">Add subfolders</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_paste">Paste</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_move">Move</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_access">Access rights</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_show_obj">Manage resources</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_show_read_obj">View resources</xsl:with-param>
			<xsl:with-param name="lab_subcategories">Subfolders</xsl:with-param>
			<xsl:with-param name="lab_all">All</xsl:with-param>
			<xsl:with-param name="lab_no_obj">No subfolders found.</xsl:with-param>
			<xsl:with-param name="lab_categories">&#160;subfolders</xsl:with-param>
			<xsl:with-param name="lab_shared">Shared</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_all_categories"/>
		<xsl:param name="lab_category_name"/>
		<xsl:param name="lab_cat_id"/>
		<xsl:param name="lab_res_catalog"/>
		<xsl:param name="lab_total"/>
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_item"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_g_txt_btn_add_sub_cat"/>
		<xsl:param name="lab_g_txt_btn_paste"/>
		<xsl:param name="lab_g_txt_btn_move"/>
		<xsl:param name="lab_g_txt_btn_access"/>
		<xsl:param name="lab_g_txt_btn_show_obj"/>
		<xsl:param name="lab_g_txt_btn_show_read_obj"/>
		<xsl:param name="lab_subcategories"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_no_obj"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_desc_read"/>
		<xsl:param name="lab_categories"/>
		<xsl:param name="lab_shared"/>
		<xsl:param name="lab_yes"/>
		
		<xsl:variable name="choice" select="/objective_list/folders/text()" />
		<xsl:variable name="show_all" select="/objective_list/meta/show_all"/>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<span class="NavLink">
					<xsl:for-each select="/objective_list/objective/path/node">
						<a href="javascript:obj.manage_obj_lst('','{@id}','','{$choice}','{$show_all}')" class="NavLink">
							<xsl:value-of select="."/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
					</xsl:for-each>
					<xsl:value-of select="/objective_list/objective/desc"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		
		<xsl:variable name="objective_id" select="/objective_list/objective/@id" />
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
		</xsl:call-template>
		
		<!-- 
		<xsl:choose>
			<xsl:when test="$access = $reader">
				<xsl:call-template name="wb_ui_desc">
			         <xsl:with-param name="text" select="$lab_desc_read"/>
		        </xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_desc">
			         <xsl:with-param name="text" select="$lab_desc"/>
		        </xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
 -->
	

	
			
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_res_catalog"/>
					</xsl:with-param>
					<xsl:with-param name="extra_td">
						<td align="right">
							<!-- Update Button -->
							<xsl:if test="$page_var/@hasEditObjBtn = 'true' and $share_mode!='true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_edit"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:obj.upd_prep(<xsl:value-of select="$obj_id"/>,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<!--Move Button-->
							 <xsl:if test="$page_var/@hasMoveObjBtn = 'true' and $share_mode!='true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_move"/>
									<!--<xsl:with-param name="wb_gen_btn_href">javascript:obj.del('<xsl:value-of select="$obj_id"/>','<xsl:value-of select="$obj_id_parent"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>-->
			                 	<xsl:with-param name="wb_gen_btn_href">javascript:obj.move('<xsl:value-of select="objective/path/node/@id" />')</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<!-- Delete Button -->
							<xsl:if test="$page_var/@hasRemoveObjBtn = 'true' and $share_mode!='true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_remove"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:obj.del(frmXml,'<xsl:value-of select="$obj_id"/>','<xsl:value-of select="$obj_id_parent"/>','<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$curr" />')</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							
						<!-- Insert Button -->
							<xsl:if test="$page_var/@hasAddObjBtn = 'true' and $share_mode!='true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_add_sub_cat"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:obj.ins_prep('<xsl:value-of select="$syb_id"/>','<xsl:value-of select="$obj_id"/>','','<xsl:value-of select="$curr" />')</xsl:with-param>
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
						</td>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_line"/>
				<table>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_cat_id"/>：
						</td>
						<td class="wzb-form-control">
							<xsl:value-of select="objective/@id"/>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_category_name"/>：
						</td>
						<td class="wzb-form-control">
							<xsl:value-of select="objective/desc"/>
						</td>
					</tr>
					<xsl:if test="objective/shared/text() = 'true'">
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_shared"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:value-of select="$lab_yes"/>
							</td>
						</tr>
					</xsl:if>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_item"/>：
						</td>
						<td class="wzb-form-control">
							<xsl:value-of select="objective/@count"/>
						</td>
					</tr>
					<tr>
						<td></td>
						<td style="padding-left:10px;">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_show_obj"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:obj.show_obj_lst('<xsl:value-of select="$syb_id"/>','<xsl:value-of select="$obj_id"/>','','<xsl:value-of select="$curr" />','<xsl:value-of select="$show_all"/>')</xsl:with-param>
								<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
				</table>
		
			<!-- 
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="extra_td">
						<xsl:choose>
							<xsl:when test="count(objective/body/node) = 0">
								<td align="right">
									
									<xsl:if test="$page_var/@hasAddObjBtn = 'true' and $share_mode!='true'">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_add_sub_cat"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:obj.ins_prep('<xsl:value-of select="$syb_id"/>','<xsl:value-of select="$obj_id"/>','','<xsl:value-of select="$curr" />')</xsl:with-param>
										<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
									</xsl:call-template>
									</xsl:if>
								</td>
							</xsl:when>
							<xsl:otherwise>
								<td align="right">
									<xsl:if test="$page_var/@hasAddObjBtn = 'true' and $share_mode!='true'">
										
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_add_sub_cat"/>
											<xsl:with-param name="wb_gen_btn_href">javascript:obj.ins_prep('<xsl:value-of select="$syb_id"/>','<xsl:value-of select="$obj_id"/>','','<xsl:value-of select="$curr" />')</xsl:with-param>
											<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
											<xsl:with-param name="class">btn wzb-btn-orange marigin-right10</xsl:with-param>
										</xsl:call-template>
										&#160;&#160;
									</xsl:if>
								</td>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
				</xsl:call-template>
				 -->
			<xsl:choose>
				<xsl:when test="count(objective/body/node) = 0">
					
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text">
							<xsl:value-of select="$lab_subcategories"/>
						</xsl:with-param>
					</xsl:call-template>
					
					<xsl:call-template name="wb_ui_line"/>
					<table>
						<xsl:apply-templates select="objective">
							<xsl:with-param name="lab_total" select="$lab_total"/>
							<xsl:with-param name="lab_lost_and_found" select="$lab_lost_and_found"/>
							<xsl:with-param name="lab_item" select="$lab_item"/>
							<xsl:with-param name="lab_all" select="$lab_all"/>
							<xsl:with-param name="lab_categories" select="$lab_categories"/>
						</xsl:apply-templates>
					</table>
				</xsl:otherwise>
			</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="objective">
		<xsl:param name="lab_total"/>
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_item"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_categories"/>
		<xsl:variable name="curr" select="/objective_list/folders/text()" />
	
		<xsl:for-each select="body/node[@type = 'SYB']">
			<xsl:if test="position() mod 4 = 1 ">
				<xsl:text disable-output-escaping="yes">&lt;tr&gt;</xsl:text>
			</xsl:if>
			<td width="24%" valign="top" >
				<table>
					<tr>
						<xsl:variable name="obj_script">
							<xsl:call-template name="escape_js">
								<xsl:with-param name="input_str">
									<xsl:value-of select="."/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:variable>
						<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
							obj_name]]><xsl:value-of select="@id"/><![CDATA[=']]><xsl:value-of select="$obj_script"/><![CDATA[']]></script>
						<xsl:variable name="obj_name"><![CDATA[obj_name]]><xsl:value-of select="@id"/>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="@type = 'SYB'">
								<td width="35" valign="top" align="center">
									<img src="{$wb_img_path}ico_big_folder.gif" border="0"/>
								</td>
								<td>
									<table>
										<tr>
											<td>
												<a class="font gray" href="javascript:obj.manage_obj_lst(frmXml,'{@id}','{$obj_name}','{$curr}','{$show_all}')">
													<xsl:value-of select="."/>
												</a>
												<xsl:text>&#160;(</xsl:text>
												<xsl:value-of select="@obj_count"/>
												<xsl:value-of select="$lab_categories"/>
												<xsl:text>)</xsl:text>
											</td>
										</tr>
										<tr>
											<td>
												<xsl:call-template name="draw_cnt">
													<xsl:with-param name="cnt" select="@count"/>
													<xsl:with-param name="own_cnt" select="@own_count"/>
													<xsl:with-param name="obj_cnt" select="@obj_count"/>
													<xsl:with-param name="id" select="@id"/>
													<xsl:with-param name="obj_name" select="$obj_name"/>
													<xsl:with-param name="lab_item" select="$lab_item"/>
													<xsl:with-param name="lab_all" select="$lab_all"/>
												</xsl:call-template>
											</td>
										</tr>
									</table>
								</td>
							</xsl:when>
							<xsl:when test="@type = 'SYS'">
								<td width="20">
									<img src="{$wb_img_path}ico_sys_grp.gif" border="0"/>
								</td>
								<td>
									<a class="fontBold" href="javascript:obj.show_trash_obj_lst('{@id}')">
										<xsl:value-of select="$lab_lost_and_found"/>
									</a>
									<br/>
									<xsl:choose>
										<xsl:when test="@count &gt;= 1">
											<xsl:text>(&#160;</xsl:text>
											<xsl:value-of select="@count + @own_count"/>
											<xsl:text>&#160;</xsl:text>
											<xsl:value-of select="$lab_item"/>
											<xsl:text>&#160;)&#160;</xsl:text>
										</xsl:when>
									</xsl:choose>
								</td>
							</xsl:when>
						</xsl:choose>
					</tr>
				</table>
			</td>
			<td width="1%" ></td>
			<xsl:if test="position() mod 4 = 0 or position() = last()">
				<xsl:text disable-output-escaping="yes">&lt;/tr&gt;</xsl:text>
				<tr>
					<td height="10" colspan="2">
						<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
					</td>
				</tr>
			</xsl:if>
						
			
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="draw_cnt">
		<xsl:param name="cnt"/>
		<xsl:param name="own_cnt"/>
		<xsl:param name="obj_cnt"/>
		<xsl:param name="id"/>
		<xsl:param name="obj_name"/>
		<xsl:param name="lab_item"/>
		<xsl:param name="lab_all"/>
		<a href="javascript:obj.show_obj_lst('{$syb_id}','{$id}','','{$curr}','{$show_all}')" class="SmallText">
			<xsl:choose>
				<xsl:when test="$cnt &gt;= 1">
					<xsl:value-of select="$cnt"/>
				</xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="$lab_item"/>
		</a>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
