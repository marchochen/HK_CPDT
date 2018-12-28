<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:import href="wb_const.xsl"/>
<xsl:import href="utils/wb_init_lab.xsl"/>
<xsl:import href="utils/escape_applet_param_doub_quo.xsl"/>
<xsl:import href="utils/wb_css.xsl"/>
<xsl:import href="utils/wb_utils.xsl"/>
<xsl:import href="cust/wb_cust_const.xsl"/>

<xsl:variable name="pfs_count" select="count(/profession/pfs_list/pfs)"/>
<!-- =============================================================== -->
<xsl:template match="/">
	<html><xsl:call-template name="wb_init_lab"/></html>
</xsl:template>  
<!-- =============================================================== -->
	<xsl:template name="lang_ch" >
		<xsl:call-template name="main">
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
			<xsl:with-param name="lab_none">暂无信息</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb" >
		<xsl:call-template name="main">
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
			<xsl:with-param name="lab_none">暂无信息</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en" >
		<xsl:call-template name="main">
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
			<xsl:with-param name="lab_none">No information</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
<!-- =============================================================== -->
<xsl:template name="main">
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
	<xsl:param name="lab_none"/>
		<head>
			<title><xsl:value-of select="$wb_wizbank" /></title>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<script language="JavaScript" src="{$wb_js_path}wb_pfs.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"  type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
		
			
			<script language="JavaScript" type="text/javascript">
					pfs = new wbpfs();
					function go_add() {
						var url = wb_utils_invoke_disp_servlet('module', 'profession.ProfessionModule', 'cmd', 'PFS_INS_UPD_PREP', 'stylesheet', 'pfs_ins_upd_prep.xsl');
						window.parent.show_frame_content(url);
					}
					function go_edit(pfs_id) {
						var url = wb_utils_invoke_disp_servlet('module', 'profession.ProfessionModule', 'cmd', 'PFS_INS_UPD_PREP', 'stylesheet', 'pfs_ins_upd_prep.xsl', 'pfs_id', pfs_id);
						window.parent.show_frame_content(url);
					}
			</script>
			</head>
			<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
				<script language="JavaScript" type="text/javascript"><![CDATA[
					menu_width = screen.width - 50;
					nav_height = screen.height - 180 - 40;
					applet_width = menu_width;
					applet_height = screen.height - 180;
				]]></script>
				<div style="height:40px;">
					<a href="javascript:go_add();"><img border="none" src="{$wb_img_path}/add_document.gif"/></a>
				</div>
				<div style="border: 1px solid silver; overflow: auto; width: 273px; height: 320px;">
					<table>
						<xsl:choose>
							<xsl:when test="$pfs_count &gt; 0">
								<xsl:for-each select="/profession/pfs_list/pfs">
									<tr>
										<td height="10">
											<xsl:value-of select="position()"/>.<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
										</td>
										<td height="20">
											<a href="javascript:go_edit({@id})" class="Text"><xsl:value-of select="pfs_title"/></a>
										</td>
									</tr>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>
								<span class="Text"><xsl:value-of select="$lab_none"/></span>
							</xsl:otherwise>
						</xsl:choose>
					</table>
				</div>
			</body>
</xsl:template>
<!-- =============================================================== -->
</xsl:stylesheet>
