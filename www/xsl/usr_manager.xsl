<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/select_all_enabled_checkbox.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_gen_tab.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<!-- others -->
	<xsl:import href="share/usr_gen_tab_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/label_role.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:output indent="yes"/>
	<!-- ====================================================================================================== -->
	<!-- paginatoin variables -->
	<xsl:variable name="page_size" select="/user_manager/group_member_list/group/@pagesize"/>
	<xsl:variable name="cur_page" select="/user_manager/group_member_list/group/@cur_page"/>
	<xsl:variable name="total" select="/user_manager/group_member_list/group/@total"/>
	<xsl:variable name="page_timestamp" select="/user_manager/group_member_list/group/@timestamp"/>
	<xsl:variable name="parent_id" select="/user_manager/group_member_list/@id"/>
	<xsl:variable name="parent_role" select="/user_manager/group_member_list/@grp_role"/>
	<xsl:variable name="cur_usr" select="/user_manager/group_member_list/cur_usr/@id"/>
	<xsl:variable name="root_ent_id" select="/user_manager/meta/cur_usr/@root_ent_id"/>
	<xsl:variable name="page_variant" select="/user_manager/meta/page_variant"/>
	<xsl:variable name="approver_cnt" select="count(/user_manager/group_member_list/assigned_user_role/role/entity)"/>
	<xsl:variable name="meta_cur_usr" select="/user_manager/meta/cur_usr/@ent_id"/>
	<xsl:variable name="sort_order_by" select="/user_manager/group_member_list/group/@sort_col"/>
	<!-- 用户组信息 -->
	<xsl:variable name="label_core_user_management_2" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_um.label_core_user_management_2')"/>
	<!-- 子用户组列表 -->
	<xsl:variable name="label_core_user_management_3" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_um.label_core_user_management_3')"/>
	<!-- 用户列表 -->
	<xsl:variable name="label_core_user_management_4" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_um.label_core_user_management_4')"/>
	<!-- 用户组列表 -->
	<xsl:variable name="label_core_user_management_5" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_um.label_core_user_management_5')"/>
	<!-- 请选择需要操作用户 -->
	<xsl:variable name="label_core_user_management_6" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_um.label_core_user_management_6')"/>
	<!-- 批量导出用户 -->
	<xsl:variable name="label_core_user_management_49" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_um.label_core_user_management_49')"/>
	<xsl:variable name="cur_sort_order">
		<xsl:choose>
			<xsl:when test="$sort_order_by = 'ASC'">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- ====================================================================================================== -->
	<xsl:template match="/user_manager">
		<html>
			<xsl:apply-templates select="group_member_list"/>
		</html>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template match="group_member_list">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_batchprocess.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			Batch = new wbBatchProcess
			usr = new wbUserGroup;
			cur_page = ]]><xsl:value-of select="$cur_page"/><![CDATA[		
			function status(){
				usr.search.search_exec(document.searchFrm,']]><xsl:value-of select="$wb_lang"/><![CDATA[')
				return false;
			}
			function doFeedParam(){
				var param = new Array();
				
				var tmpObj1 = new Array();
				tmpObj1[0] = "cmd";
				]]><xsl:choose>
					<xsl:when test="$parent_role='SYSTEM'"><![CDATA[
					tmpObj1[1] = "trash_usr";
					]]></xsl:when>
					<xsl:otherwise><![CDATA[
					tmpObj1[1] = "del_usr";
					]]></xsl:otherwise>
				</xsl:choose><![CDATA[
				param[param.length] = tmpObj1;
				
				var tmpObj2 = new Array();
				]]><xsl:choose>
					<xsl:when test="$parent_role='SYSTEM'"><![CDATA[
					tmpObj2[0] = "usr_ent_id_lst";
					]]></xsl:when>
					<xsl:otherwise><![CDATA[
					tmpObj2[0] = "usr_ent_id";
					]]></xsl:otherwise>
				</xsl:choose><![CDATA[
				var tmpLst1 = _wbUserGetCheckedUserLst(document.frmXml);
				tmpObj2[1] = tmpLst1.split("~");
				param[param.length] = tmpObj2;
				
				var tmpObj3 = new Array();
				]]><xsl:choose>
					<xsl:when test="$parent_role='SYSTEM'"><![CDATA[
					tmpObj3[0] = "usr_timestamp_lst";
					]]></xsl:when>
					<xsl:otherwise><![CDATA[
					tmpObj3[0] = "usr_timestamp";
					]]></xsl:otherwise>
				</xsl:choose><![CDATA[
				var tmpLst2 = _wbUserGetCheckedUserTimestampLst(document.frmXml);
				tmpObj3[1] = tmpLst2.split("~");
				param[param.length] = tmpObj3;

				]]><xsl:if test="$parent_role!='SYSTEM'"><![CDATA[
				var tmpObj4 = new Array();
				tmpObj4[0] = "ent_id_parent";
				tmpObj4[1] = "]]><xsl:value-of select="$parent_id"/><![CDATA[";
				param[param.length] = tmpObj4;
				]]></xsl:if><![CDATA[
				
				var tmpObj5 = new Array();
				tmpObj5[0] = "multi_del";
				tmpObj5[1] = "true";
				param[param.length] = tmpObj5;

				var tmpObj6 = new Array();
				tmpObj6[0] = "ent_nm_lst";
				var tmpLst3 = _wbUserGetCheckedUserNameLst(document.frmXml);
				tmpObj6[1] = tmpLst3.split("~");
				param[param.length] = tmpObj6;
				
				return param;
			}
			
			function exec_prep(frm, lang, cmd){
				var usr_id_lst_str = '';
				var isMultiple = false;
				//alert(frm.usr_id_lst.length);
				if(frm.usr_id_lst.length){
					for(var i = 0;i<frm.usr_id_lst.length;i++){
						if(frm.usr_id_lst[i].checked){
							isMultiple = true;
							usr_id_lst_str += frm.usr_id_lst[i].value + "~";
						}
					}
					if(isMultiple == true){
						usr_id_lst_str = usr_id_lst_str.substr(0, usr_id_lst_str.length - 1);
					} else {
						alert(']]><xsl:value-of select="$label_core_user_management_6"/><![CDATA[')
						return;
					}
				}else if(frm.usr_id_lst){
					if(frm.usr_id_lst.checked){
						usr_id_lst_str = frm.usr_id_lst.value;
					}else{
						alert(']]><xsl:value-of select="$label_core_user_management_6"/><![CDATA[')
						return;
					}
				}
				if(cmd == 'cut'){
					usr.utils.cut('0',usr_id_lst_str,'USR','',lang);
				} else if(cmd == 'restore'){
					usr.user.restore_exec2(usr_id_lst_str,lang, 'usr_manager');
				}
			}
			var active_tab = '';
			function changeTabs(tab){
				$("input[name='active_tab']").val(tab);
				active_tab = tab;
			}
			
			$(function(){
				active_tab = getUrlParam('active_tab');
				if(active_tab != undefined && active_tab != null && active_tab != '' && active_tab != 'undefined'){
					$("ul[role='tablist'] li").each(function(){
						$(this).removeClass('active');
						if($(this).children('a').attr('aria-controls') == active_tab){
							$(this).addClass('active');
						}
					});
					$(".tab-pane").each(function(){
						$(this).removeClass('active');
					});
					$("#" + active_tab).addClass('active');
				}
			})
		]]>
			
		</script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="wb_utils_set_cookie('patent_id',{$parent_id});">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_adv_search">進階搜索</xsl:with-param>
			<xsl:with-param name="lab_copy">複製</xsl:with-param>
			<xsl:with-param name="lab_cut">剪下</xsl:with-param>
			<xsl:with-param name="lab_desc">簡介</xsl:with-param>
			<xsl:with-param name="lab_g_lst_btn_cut">剪下</xsl:with-param>
			<xsl:with-param name="lab_g_lst_btn_restore">復原</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_group">新增<xsl:value-of select="$lab_group"/></xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_sub_group">新增子<xsl:value-of select="$lab_group"/></xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_user">新增用戶</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_upd_batch_user">批量修改用戶</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_copy">複製</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del_grp">刪除
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del_usr">删除用户</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_paste">貼上</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_search">搜索</xsl:with-param>
			<xsl:with-param name="lab_grp_code">
				<xsl:value-of select="$lab_group"/>編號</xsl:with-param>
			<xsl:with-param name="lab_last_modify">最近更新日期</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
			<xsl:with-param name="lab_no_usr">沒有發現用戶</xsl:with-param>
			<xsl:with-param name="lab_no_grp">沒有發現子<xsl:value-of select="$lab_group"/>
			</xsl:with-param>
			<xsl:with-param name="lab_org">學校</xsl:with-param>
			<xsl:with-param name="lab_restore">復原</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_user">用戶</xsl:with-param>
			<xsl:with-param name="lab_user_list">用戶清單</xsl:with-param>
			<xsl:with-param name="lab_usr_grp_list">
				<xsl:value-of select="$lab_group"/>清單</xsl:with-param>
			<xsl:with-param name="lab_all_users">用戶資訊</xsl:with-param>
			<xsl:with-param name="lab_usr_management">檔案維護</xsl:with-param>
			<xsl:with-param name="lab_grp_supervisor">用戶組上司</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_import">批量匯入用戶</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_delete">批量刪除用戶</xsl:with-param>
			<xsl:with-param name="lab_batch_delete">批量刪除</xsl:with-param>
			<xsl:with-param name="lab_permanent_delete">徹底刪除</xsl:with-param>
			<xsl:with-param name="lab_default_page">首頁</xsl:with-param>
			<xsl:with-param name="lab_user_placeholder">請輸入用戶名搜索</xsl:with-param>
			<xsl:with-param name="lab_usg_inf">用戶組資訊</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_batch_delete">批量刪除</xsl:with-param>
			<xsl:with-param name="lab_adv_search">高级搜索</xsl:with-param>
			<xsl:with-param name="lab_copy">复制</xsl:with-param>
			<xsl:with-param name="lab_cut">剪切</xsl:with-param>
			<xsl:with-param name="lab_desc">简介</xsl:with-param>
			<xsl:with-param name="lab_g_lst_btn_cut">剪切</xsl:with-param>
			<xsl:with-param name="lab_g_lst_btn_restore">还原</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_group">添加<xsl:value-of select="$lab_group"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_sub_group">添加子<xsl:value-of select="$lab_group"/></xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_user">添加用户</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_upd_batch_user">批量修改用户</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_copy">复制</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del">删除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del_grp">删除
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del_usr">刪除用戶</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_paste">粘贴</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_search">搜索</xsl:with-param>
			<xsl:with-param name="lab_grp_code">
				<xsl:value-of select="$lab_group"/>编号</xsl:with-param>
			<xsl:with-param name="lab_last_modify">最近更新日期</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
			<xsl:with-param name="lab_no_usr">没有用户</xsl:with-param>
			<xsl:with-param name="lab_no_grp">没有子<xsl:value-of select="$lab_group"/>
			</xsl:with-param>
			<xsl:with-param name="lab_org">机构</xsl:with-param>
			<xsl:with-param name="lab_restore">还原</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_user">用户</xsl:with-param>
			<xsl:with-param name="lab_user_list">用户列表</xsl:with-param>
			<xsl:with-param name="lab_usr_grp_list">
				<xsl:value-of select="$lab_group"/>列表</xsl:with-param>
			<xsl:with-param name="lab_all_users">用户信息</xsl:with-param>
			<xsl:with-param name="lab_usr_management">信息维护</xsl:with-param>
			<xsl:with-param name="lab_grp_supervisor">用户组领导</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_import">批量导入用户</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_delete">批量删除用户</xsl:with-param>
			<xsl:with-param name="lab_permanent_delete">彻底删除</xsl:with-param>
			<xsl:with-param name="lab_default_page">首页</xsl:with-param>
			<xsl:with-param name="lab_user_placeholder">请输入用户名搜索</xsl:with-param>
			<xsl:with-param name="lab_usg_inf">用户组信息</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_batch_delete">Batch delete user</xsl:with-param>
			<xsl:with-param name="lab_permanent_delete">Permanent delete</xsl:with-param>
			<xsl:with-param name="lab_adv_search">Advanced search</xsl:with-param>
			<xsl:with-param name="lab_copy">Copy</xsl:with-param>
			<xsl:with-param name="lab_cut">Cut</xsl:with-param>
			<xsl:with-param name="lab_desc">Description</xsl:with-param>
			<xsl:with-param name="lab_g_lst_btn_cut">Cut</xsl:with-param>
			<xsl:with-param name="lab_g_lst_btn_restore">Restore</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_group">Add group</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_sub_group">Add Sub user group</xsl:with-param>
			
			<xsl:with-param name="lab_g_txt_btn_add_user">Add user</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_upd_batch_user">Batch edit user</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_copy">Copy</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del">Delete</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del_grp">Delete</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del_usr">Delete user</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_paste">Paste user</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_search">Search</xsl:with-param>
			<xsl:with-param name="lab_grp_code">
				<xsl:value-of select="$lab_group"/> code</xsl:with-param>
			<xsl:with-param name="lab_last_modify">Last modified</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">Recycle bin</xsl:with-param>
			<xsl:with-param name="lab_no_usr">No users found.</xsl:with-param>
			<xsl:with-param name="lab_no_grp">No subgroups found.</xsl:with-param>
			<xsl:with-param name="lab_org">Organization</xsl:with-param>
			<xsl:with-param name="lab_restore">Restore</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_user">User</xsl:with-param>
			<xsl:with-param name="lab_user_list">User list</xsl:with-param>
			<xsl:with-param name="lab_usr_grp_list">
				<xsl:value-of select="$lab_group"/> list</xsl:with-param>
			<xsl:with-param name="lab_all_users">User profile</xsl:with-param>
			<xsl:with-param name="lab_usr_management">Profile maintenance</xsl:with-param>
			<xsl:with-param name="lab_grp_supervisor">Group supervisor</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_import">Import user</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_delete">Batch delete user</xsl:with-param>
			<xsl:with-param name="lab_default_page">Default page</xsl:with-param>
			<xsl:with-param name="lab_user_placeholder">User name</xsl:with-param>
			<xsl:with-param name="lab_usg_inf">Group info</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_permanent_delete"/>
		<xsl:param name="lab_batch_delete"/>
		<xsl:param name="lab_all_users"/>
		<xsl:param name="lab_usr_management"/>
		<xsl:param name="lab_usr_grp"/>
		<xsl:param name="lab_grp_code"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_user_list"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_copy"/>
		<xsl:param name="lab_cut"/>
		<xsl:param name="lab_restore"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_user"/>
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_no_usr"/>
		<xsl:param name="lab_no_grp"/>
		<xsl:param name="lab_org"/>
		<xsl:param name="lab_last_modify"/>
		<xsl:param name="lab_g_txt_btn_add_user"/>
		<xsl:param name="lab_g_txt_btn_upd_batch_user"/>
		<xsl:param name="lab_g_txt_btn_add_group"/>
		<xsl:param name="lab_g_txt_btn_paste"/>
		<xsl:param name="lab_g_txt_btn_copy"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_del_usr"/>
		<xsl:param name="lab_g_txt_btn_del_grp"/>
		<xsl:param name="lab_g_lst_btn_cut"/>
		<xsl:param name="lab_g_lst_btn_restore"/>
		<xsl:param name="lab_g_txt_btn_search"/>
		<xsl:param name="lab_adv_search"/>
		<xsl:param name="lab_usr_grp_list"/>
		<xsl:param name="lab_grp_supervisor"/>
		<xsl:param name="lab_g_txt_btn_import"/>
		<xsl:param name="lab_g_txt_btn_delete"/>
		<xsl:param name="lab_default_page"/>
		<xsl:param name="lab_user_placeholder" />
		<xsl:param name="lab_g_txt_btn_add_sub_group" />
		<xsl:param name="lab_usg_inf" />
		
		
		<xsl:variable name="parent_ent_id">
			<xsl:choose>
				<xsl:when test="count(ancestor_node_list/node) = 0">
					<xsl:value-of select="$root_ent_id"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="ancestor_node_list/node[last()]/@id"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_USR_INFO</xsl:with-param>
			<xsl:with-param name="page_title">
				<xsl:call-template name="get_lab">
					<xsl:with-param name="lab_title">268</xsl:with-param>
				</xsl:call-template>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
		<xsl:when test="$parent_role = 'SYSTEM'">
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text" select="$lab_lost_and_found"/>
			</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_usr_management"/>
		</xsl:call-template>
		</xsl:otherwise>
		</xsl:choose>
		<form onsubmit="return status()" name="searchFrm" >
		<table>
			<tr>
				<td>
					<xsl:call-template name="draw_search">
						<xsl:with-param name="lab_g_txt_btn_search" select="$lab_g_txt_btn_search"/>
						<xsl:with-param name="lab_adv_search" select="$lab_adv_search"/>
						<xsl:with-param name="lab_lost_and_found" select="$lab_lost_and_found"/>
						<xsl:with-param name="lab_all_users" select="$lab_all_users"/>
						<xsl:with-param name="lab_default_page" select="$lab_default_page"/>
						<xsl:with-param name="lab_user_list" select="$lab_user_list"/>
						<xsl:with-param name="lab_group" select="$lab_group"/>
						<xsl:with-param name="lab_g_txt_btn_import" select="$lab_g_txt_btn_import"/>
						<xsl:with-param name="lab_g_txt_btn_delete" select="$lab_g_txt_btn_delete"/>
						<xsl:with-param name="lab_user_placeholder"  select="$lab_user_placeholder"/>
					</xsl:call-template>
					</td>
			</tr>
		</table>
		</form>
		<form name="frmXml" style="margin-top: -30px;">
		<input type="hidden" name="meta_usr_id" value="{$meta_cur_usr}"/>
		<input type="hidden" name="sort_order" id="sort_order" value="{$cur_sort_order}"/>
		<input type="hidden" name="cur_user_group_id" value="{$parent_id}"/>
		<input type="hidden" name="active_tab" value="subUserList"/>
		
	   <xsl:call-template name="wb_ui_space"/>
		<xsl:choose>
			<xsl:when test="$parent_id != $root_ent_id and $parent_role != 'SYSTEM'">
			
					<xsl:call-template name="draw_detail">
							<xsl:with-param name="lab_grp_code" select="$lab_grp_code"/>
							<xsl:with-param name="lab_title" select="$lab_title"/>
							<xsl:with-param name="lab_grp_supervisor" select="$lab_grp_supervisor"/>
							<xsl:with-param name="lab_desc" select="$lab_desc"/>
							<xsl:with-param name="lab_g_txt_btn_edit" select="$lab_g_txt_btn_edit"/>
							<xsl:with-param name="lab_g_txt_btn_del_grp" select="$lab_g_txt_btn_del_grp"/>
							<xsl:with-param name="parent_ent_id" select="$parent_ent_id"/>
							<xsl:with-param name="lab_g_txt_btn_add_sub_group" select="$lab_g_txt_btn_add_sub_group"/>
							<xsl:with-param name="lab_usg_inf" select="$lab_usg_inf"/>
					</xsl:call-template>
					<xsl:call-template name="draw_subGroup">
						<xsl:with-param name="lab_lost_and_found" select="$lab_lost_and_found"/>
						<xsl:with-param name="lab_no_usr" select="$lab_no_usr"/>
						<xsl:with-param name="lab_no_grp" select="$lab_no_grp"/>
						<xsl:with-param name="lab_user_list" select="$lab_user_list"/>
						<xsl:with-param name="lab_last_modify" select="$lab_last_modify"/>
						<xsl:with-param name="lab_g_txt_btn_del_usr" select="$lab_g_txt_btn_del_usr"/>
						<xsl:with-param name="lab_usr_grp_list" select="$lab_usr_grp_list"/>
						<xsl:with-param name="lab_g_txt_btn_add_group" select="$lab_g_txt_btn_add_group"/>
						<xsl:with-param name="lab_g_txt_btn_add_user" select="$lab_g_txt_btn_add_user"/>
						<xsl:with-param name="lab_g_txt_btn_upd_batch_user" select="$lab_g_txt_btn_upd_batch_user"/>
						<xsl:with-param name="lab_g_txt_btn_paste" select="$lab_g_txt_btn_paste"/>
						<xsl:with-param name="lab_g_txt_btn_import" select="$lab_g_txt_btn_import"/>
						<xsl:with-param name="lab_g_txt_btn_delete" select="$lab_g_txt_btn_delete"/>
					</xsl:call-template>
			
					
					<xsl:call-template name="draw_user">
						<xsl:with-param name="lab_lost_and_found" select="$lab_lost_and_found"/>
						<xsl:with-param name="lab_no_usr" select="$lab_no_usr"/>
						<xsl:with-param name="lab_no_grp" select="$lab_no_grp"/>
						<xsl:with-param name="lab_user_list" select="$lab_user_list"/>
						<xsl:with-param name="lab_last_modify" select="$lab_last_modify"/>
						<xsl:with-param name="lab_g_txt_btn_del_usr" select="$lab_g_txt_btn_del_usr"/>
						<xsl:with-param name="lab_usr_grp_list" select="$lab_usr_grp_list"/>
						<xsl:with-param name="lab_g_txt_btn_add_group" select="$lab_g_txt_btn_add_group"/>
						<xsl:with-param name="lab_g_txt_btn_add_user" select="$lab_g_txt_btn_add_user"/>
						<xsl:with-param name="lab_g_txt_btn_upd_batch_user" select="$lab_g_txt_btn_upd_batch_user"/>
						<xsl:with-param name="lab_g_txt_btn_paste" select="$lab_g_txt_btn_paste"/>
						<xsl:with-param name="lab_g_txt_btn_delete" select="$lab_g_txt_btn_delete"/>
					</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table>
					<tr>
						<td>
							<ul class="nav nav-tabs page-tabs" role="tablist">
								<li role="presentation" class="active"><a
									aria-controls="userGroupLish" role="tab"
									href="javascript:wb_utils_nav_go('FTN_AMD_USR_INFO',{../meta/cur_usr/@ent_id},'{$wb_lang}')"><xsl:value-of select="$label_core_user_management_5" /></a></li>
								<li role="presentation"><a aria-controls="recycleLish" role="tab"
									 href="javascript:usr.group.manage_grp_del_usr('1','','','recycleLish')"><xsl:value-of select="$lab_lost_and_found" /></a></li>
							</ul>
						</td>
						<td align="right"  style="border-bottom: 1px solid #ddd;">
							<xsl:choose>
								<xsl:when test="$parent_role !='SYSTEM' and $page_variant/@hasUsgAddBtn = 'true' and $page_variant/@canMaitainUsg = 'true'">
									
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="class">btn wzb-btn-orange margin-right0</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_add_group"/>
											<xsl:with-param name="wb_gen_btn_href">javascript:usr.group.ins_prep('<xsl:value-of select="$parent_id"/>',active_tab)</xsl:with-param>
										</xsl:call-template>
								
								</xsl:when>
								<xsl:when test="$parent_role='SYSTEM'">
									<xsl:if test="desc != 'LOST&amp;FOUND'">
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="wb_gen_btn_name">
												<xsl:value-of select="$lab_g_txt_btn_del_usr"/>
											</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.del_multi_trash_usr(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
											<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
										</xsl:call-template>
									</xsl:if>
									<!-- 剪切 还原按钮 -->
									<xsl:if test="desc = 'LOST&amp;FOUND'">
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_name" select="$lab_g_lst_btn_cut"/>
											<xsl:with-param name="wb_gen_btn_href">javascript:exec_prep(document.frmXml,'<xsl:value-of select="$wb_lang"/>','cut')</xsl:with-param>
										</xsl:call-template>
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_name" select="$lab_g_lst_btn_restore"/>
											<xsl:with-param name="wb_gen_btn_href">javascript:exec_prep(document.frmXml,'<xsl:value-of select="$wb_lang"/>','restore')</xsl:with-param>
										</xsl:call-template>
									</xsl:if>
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_name" select="$lab_batch_delete"></xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.all_delete_user(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
									</xsl:call-template>
								</xsl:when>
							</xsl:choose>
						</td>
					</tr>
				</table>
				<div class="tab-content">
					<div role="tabpanel" class="tab-pane active" id="userGroupLish">
						<xsl:call-template name="draw_general">
							<xsl:with-param name="lab_lost_and_found" select="$lab_lost_and_found"/>
							<xsl:with-param name="lab_no_usr" select="$lab_no_usr"/>
							<xsl:with-param name="lab_no_grp" select="$lab_no_grp"/>
							<xsl:with-param name="lab_user_list" select="$lab_user_list"/>
							<xsl:with-param name="lab_last_modify" select="$lab_last_modify"/>
							<xsl:with-param name="lab_g_txt_btn_del_usr" select="$lab_g_txt_btn_del_usr"/>
							<xsl:with-param name="lab_usr_grp_list" select="$lab_usr_grp_list"/>
							<xsl:with-param name="lab_g_txt_btn_add_group" select="$lab_g_txt_btn_add_group"/>
							<xsl:with-param name="lab_g_txt_btn_add_user" select="$lab_g_txt_btn_add_user"/>
							<xsl:with-param name="lab_g_txt_btn_upd_batch_user" select="$lab_g_txt_btn_upd_batch_user"/>
							<xsl:with-param name="lab_g_txt_btn_paste" select="$lab_g_txt_btn_paste"/>
							<xsl:with-param name="lab_g_txt_btn_delete" select="$lab_g_txt_btn_delete"/>
						</xsl:call-template>
					</div>
					<div role="tabpanel" class="tab-pane" id="recycleLish">
						<xsl:if test="$parent_role='SYSTEM'">
							<xsl:call-template name="draw_system">
								<xsl:with-param name="lab_lost_and_found" select="$lab_lost_and_found"/>
								<xsl:with-param name="lab_no_usr" select="$lab_no_usr"/>
								<xsl:with-param name="lab_no_grp" select="$lab_no_grp"/>
								<xsl:with-param name="lab_type" select="$lab_type"/>
								<xsl:with-param name="lab_user" select="$lab_user"/>
								<xsl:with-param name="lab_copy" select="$lab_copy"/>
								<xsl:with-param name="lab_cut" select="$lab_cut"/>
								<xsl:with-param name="lab_restore" select="$lab_restore"/>
								<xsl:with-param name="lab_user_list" select="$lab_user_list"/>
								<xsl:with-param name="lab_last_modify" select="$lab_last_modify"/>
								<xsl:with-param name="lab_g_txt_btn_del_usr" select="$lab_g_txt_btn_del_usr"/>
								<xsl:with-param name="lab_g_lst_btn_cut" select="$lab_g_lst_btn_cut"/>
								<xsl:with-param name="lab_g_lst_btn_restore" select="$lab_g_lst_btn_restore"/>
								<xsl:with-param name="lab_usr_grp_list" select="$lab_usr_grp_list"/>
								<xsl:with-param name="lab_permanent_delete" select="$lab_permanent_delete"/>
								<xsl:with-param name="lab_batch_delete" select="$lab_batch_delete"/>
							</xsl:call-template>
						</xsl:if>
					</div>
				</div>
				<!-- childlst start-->
			</xsl:otherwise>
		</xsl:choose>
		</form>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template name="draw_general">
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_no_usr"/>
		<xsl:param name="lab_copy"/>
		<xsl:param name="lab_user_list"/>
		<xsl:param name="lab_last_modify"/>
		<xsl:param name="lab_g_txt_btn_del_usr"/>
		<xsl:param name="lab_usr_grp_list"/>
		<xsl:param name="lab_g_txt_btn_add_group"/>
		<xsl:param name="lab_g_txt_btn_add_user"/>
		<xsl:param name="lab_g_txt_btn_upd_batch_user"/>
		<xsl:param name="lab_no_grp"/>
		<xsl:param name="lab_g_txt_btn_paste"/>
		<xsl:param name="lab_g_txt_btn_import"/>
		<xsl:param name="lab_g_txt_btn_delete"/>
		<!-- user group -->
		<div class="margin-top14"></div>
		<xsl:choose>
			<xsl:when test="count(entity[@type = 'USG' and not(@role='SYSTEM')]) &gt;= 1">
				<table >
					<xsl:apply-templates select="entity[@type = 'USG'and not(@role='SYSTEM')]" mode="draw_usr_grp">
						<xsl:with-param name="lab_lost_and_found" select="$lab_lost_and_found"/>
					</xsl:apply-templates>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_grp"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- ====================================================================================================== -->
	<xsl:template name="draw_subGroup">
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_no_usr"/>
		<xsl:param name="lab_copy"/>
		<xsl:param name="lab_user_list"/>
		<xsl:param name="lab_last_modify"/>
		<xsl:param name="lab_g_txt_btn_del_usr"/>
		<xsl:param name="lab_usr_grp_list"/>
		<xsl:param name="lab_g_txt_btn_add_group"/>
		<xsl:param name="lab_g_txt_btn_add_user"/>
		<xsl:param name="lab_g_txt_btn_upd_batch_user"/>
		<xsl:param name="lab_no_grp"/>
		<xsl:param name="lab_g_txt_btn_paste"/>
		<xsl:param name="lab_g_txt_btn_import"/>
		<xsl:param name="lab_g_txt_btn_delete"/>
		<!-- user group -->

		<xsl:choose>
			<xsl:when test="count(entity[@type = 'USG']) &gt;= 1">
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text">
						<xsl:value-of select="$label_core_user_management_3"/>
					</xsl:with-param>
				</xsl:call-template>
				
				<xsl:call-template name="wb_ui_line"/>
				<table >
					<xsl:apply-templates select="entity[@type = 'USG'and not(@role='SYSTEM')]" mode="draw_usr_grp">
						<xsl:with-param name="lab_lost_and_found" select="$lab_lost_and_found"/>
					</xsl:apply-templates>
					<!-- Access Control -->
					<xsl:choose>
						<xsl:when test="$page_variant/@hasUsgLostFoundLink = 'true'">
							<xsl:apply-templates select="entity[@type = 'USG'and @role='SYSTEM']" mode="draw_usr_grp">
								<xsl:with-param name="lab_lost_and_found" select="$lab_lost_and_found"/>
								<xsl:with-param name="usg_role_type">SYSTEM</xsl:with-param>
								<xsl:with-param name="usg_cnt">
									<xsl:value-of select="count(entity[@type = 'USG' and not(@role = 'SYSTEM')])"/>
								</xsl:with-param>
							</xsl:apply-templates>
						</xsl:when>
					</xsl:choose>
					<!-- Access Control -->
				</table>
			</xsl:when>
	
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="draw_detail">
		<xsl:param name="lab_grp_code"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_grp_supervisor"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_del_grp"/>
		<xsl:param name="parent_ent_id"/>
		<xsl:param name="lab_g_txt_btn_add_sub_group"/>
		<xsl:param name="lab_usg_inf"/>
		<div class="margin-top14"></div>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_usg_inf"/>
			</xsl:with-param>
			<xsl:with-param name="extra_td">
				<td align="right">
					<xsl:if test="$page_variant/@canMaitainUsg = 'true'">
						<xsl:if test="$page_variant/@hasUsgEditBtn = 'true'">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_edit"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:usr.group.edit_prep('<xsl:value-of select="$parent_id"/>',active_tab)</xsl:with-param>
							</xsl:call-template>
						</xsl:if>
						
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_del_grp"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:usr.group.del_grp('<xsl:value-of select="$parent_id"/>','<xsl:value-of select="$parent_ent_id"/>','<xsl:value-of select="@timestamp"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
							</xsl:call-template>
					
						
						<xsl:choose>
							<xsl:when test="$page_variant/@hasUsgAddBtn = 'true' and $page_variant/@canMaitainUsg = 'true' and $parent_id != $root_ent_id">
						
									
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="class">btn wzb-btn-orange margin-right0</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_add_sub_group"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:usr.group.ins_prep('<xsl:value-of select="$parent_id"/>',active_tab)</xsl:with-param>			</xsl:call-template>
					
								</xsl:when>
								
						</xsl:choose>
						
					</xsl:if>
				</td>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<!-- buttons -->
		<xsl:choose>
			<xsl:when test="$parent_id != $root_ent_id and $parent_role != 'SYSTEM'">
				<table>
					<xsl:choose>
						<xsl:when test="@id != '0'">
							<!-- group code-->
							<tr>
								<td class="wzb-form-label">
									<xsl:value-of select="$lab_grp_code"/>
									<xsl:text>：</xsl:text>
								</td>
								<td class="wzb-form-control">
									<xsl:value-of select="uid"/>
									<xsl:if test="string-length(uid) = 0">
										-</xsl:if>
								</td>
							</tr>
							<!-- group name -->
							<tr>
								<td class="wzb-form-label">
									<xsl:value-of select="$lab_title"/>
									<xsl:text>：</xsl:text>
								</td>
								<td class="wzb-form-control">
									<xsl:value-of select="desc"/>
								</td>
							</tr>
							<tr>
								<td class="wzb-form-label">
									<xsl:value-of select="$lab_grp_supervisor"/>
									<xsl:text>：</xsl:text>
								</td>
								<td class="wzb-form-control">
									<xsl:if test="count(supervisor_list/supervisor) = 0">
										--
									</xsl:if>
									<xsl:for-each select="supervisor_list/supervisor">
										<a href="javascript:usr.user.manage_usr('{@ent_id}')" class="Text">
											<xsl:value-of select="display_bil/text()"/>
										</a>
										<xsl:if test="position()!= last()">,<xsl:text>&#160;</xsl:text>
										</xsl:if>
										<!-- 用户组领导过多时自动换行 -->
										<xsl:if test="position()"><xsl:text>&#10;</xsl:text></xsl:if>
									</xsl:for-each>
								</td>
							</tr>
						</xsl:when>
					</xsl:choose>
					<!-- group desc -->
					<xsl:if test="long_desc != ''">
						<tr>
							<td class="wzb-form-label"  valign="top">
								<xsl:value-of select="$lab_desc"/>
								<xsl:text>：</xsl:text>
							</td>
							<td class="wzb-form-control">
								<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:value-of select="long_desc"/>
									</xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
					</xsl:if>
				</table>
				<!--
		</xsl:if>
		-->
			</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- ====================================================================================================== -->
	<xsl:template name="draw_user">
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_no_usr"/>
		<xsl:param name="lab_copy"/>
		<xsl:param name="lab_user_list"/>
		<xsl:param name="lab_last_modify"/>
		<xsl:param name="lab_g_txt_btn_del_usr"/>
		<xsl:param name="lab_usr_grp_list"/>
		<xsl:param name="lab_g_txt_btn_add_group"/>
		<xsl:param name="lab_g_txt_btn_add_user"/>
		<xsl:param name="lab_g_txt_btn_upd_batch_user"/>
		<xsl:param name="lab_no_grp"/>
		<xsl:param name="lab_g_txt_btn_paste"/>
		<xsl:param name="lab_g_txt_btn_import"/>
		<xsl:param name="lab_g_txt_btn_delete"/>
		<div class="margin-top14"></div>
		<!-- user -->
		<xsl:choose>
			<xsl:when test="@id != $root_ent_id">
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_user_list"/>
					</xsl:with-param>
					<xsl:with-param name="extra_td">
						<td align="right">
							<xsl:if test="$page_variant/@canMaitainUsg = 'true'">
							<!--新添加批量修改用户信息功能-->
							<xsl:if test="count(entity[@type = 'USR']) != 0">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_upd_batch_user"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.upd_batch_Prep(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
								</xsl:call-template>
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</xsl:if>
							<xsl:if test="$page_variant/@hasUsrAddBtn = 'true' and @id != $root_ent_id">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_add_user"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.ins_prep('<xsl:value-of select="$parent_id"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="$page_variant/@hasUsgPasteBtn = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_paste"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:usr.utils.paste('<xsl:value-of select="$parent_id"/>','<xsl:value-of select="$parent_role"/>','<xsl:value-of select="$root_ent_id"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="count(entity[@type = 'USR']) != 0">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_del_usr"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.del_multi_usr(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							</xsl:if>
						</td>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_line"/>
				<xsl:choose>
					<xsl:when test="count(entity[@type = 'USR']) = 0">
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_no_usr"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<table class="table wzb-ui-table">
							<tr class="wzb-ui-table-head">
								<td width="40%">
									<span>
										<xsl:if test="$page_variant/@canMaitainUsg = 'true'">
										<xsl:call-template name="select_all_enabled_checkbox">
											<xsl:with-param name="chkbox_lst_cnt" select="count(entity[@type='USR'])"/>
											<xsl:with-param name="chkbox_lst_nm">entity_id_chkbox</xsl:with-param>
											<xsl:with-param name="display_icon">false</xsl:with-param>
										</xsl:call-template>
										</xsl:if>
									</span>
									<xsl:value-of select="$lab_dis_name"/>
								</td>
								<td width="40%">
									<xsl:value-of select="$lab_login_id"/>
								</td>
								
								<!--<td width="300">
							<span class="TitleText">
								<xsl:value-of select="$lab_role"/>
							</span>
						</td>-->
								<td width="20%" align="right">
								<xsl:value-of select="$lab_last_modify"/>
									<!-- <a href="javascript:usr.group.manage_grp('{@id}','','{$parent_id}')" class="Text">
									<xsl:value-of select="$lab_last_modify"/>
									<xsl:call-template name="wb_sortorder_cursor">
										<xsl:with-param name="img_path" select="$wb_img_path"/>
										<xsl:with-param name="sort_order" select="$cur_sort_order"/>
									</xsl:call-template>
									</a> -->
								</td>
							</tr>
							<xsl:apply-templates select="entity[@type = 'USR']" mode="grp_general"/>
						</table>
						<!-- Pagination -->
						<xsl:call-template name="wb_ui_pagination">
							<xsl:with-param name="cur_page">
								<xsl:value-of select="$cur_page"/>
							</xsl:with-param>
							<xsl:with-param name="page_size">
								<xsl:value-of select="$page_size"/>
							</xsl:with-param>
							<xsl:with-param name="page_size_name">pagesize</xsl:with-param>
							<xsl:with-param name="total">
								<xsl:value-of select="$total"/>
							</xsl:with-param>
							<xsl:with-param name="width">
								<xsl:value-of select="$wb_gen_table_width"/>
							</xsl:with-param>
							<xsl:with-param name="timestamp">
								<xsl:value-of select="$page_timestamp"/>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- ====================================================================================================== -->
	<xsl:template name="draw_system">
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_no_usr"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_user"/>
		<xsl:param name="lab_copy"/>
		<xsl:param name="lab_cut"/>
		<xsl:param name="lab_restore"/>
		<xsl:param name="lab_user_list"/>
		<xsl:param name="lab_last_modify"/>
		<xsl:param name="lab_g_txt_btn_del_usr"/>
		<xsl:param name="lab_g_lst_btn_cut"/>
		<xsl:param name="lab_g_lst_btn_restore"/>
		<xsl:param name="lab_permanent_delete"/>
		<xsl:param name="lab_batch_delete"/>
		<xsl:choose>
			<xsl:when test="count(entity) = 0">
				<xsl:call-template name="wb_ui_space"/>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_usr"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td></td>
						<td>
							<xsl:if test="count(/user_manager/group_member_list/entity)!=0">
								<xsl:call-template name="select_all_checkbox">
									<xsl:with-param name="chkbox_lst_cnt"><xsl:value-of select="count(/user_manager/group_member_list/entity)"/></xsl:with-param>
									<xsl:with-param name="display_icon">false</xsl:with-param>
									<xsl:with-param name="sel_all_chkbox_nm">sel_all_checkbox</xsl:with-param>
									<xsl:with-param name="frm_name">frmXml</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
						</td>
						<td width="25%">
							<xsl:value-of select="$lab_login_id"/>
						</td>
						<td width="25%">
							<xsl:value-of select="$lab_dis_name"/>
						</td>
						<!--<td>
							<span class="TitleText" width="80">
								<xsl:value-of select="$lab_type"/>
							</span>
						</td>
						<td width="300">
							<span class="TitleText">
								<xsl:value-of select="$lab_role"/>
							</span>
						</td>-->
						<td nowrap="nowrap" width="25%">
							<xsl:value-of select="$lab_last_modify"/>
						</td>
						<td align="right" width="20%"></td>
<!-- 						<td align="center"> -->
<!-- 							<span class="TitleText"> -->
<!-- 								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/> -->
<!-- 							</span> -->
<!-- 						</td> -->
					</tr>
					<xsl:apply-templates select="entity" mode="grp_system">
						<xsl:with-param name="lab_user" select="$lab_user"/>
						<xsl:with-param name="lab_copy" select="$lab_copy"/>
						<xsl:with-param name="lab_g_lst_btn_cut" select="$lab_g_lst_btn_cut"/>
						<xsl:with-param name="lab_g_lst_btn_restore" select="$lab_g_lst_btn_restore"/>
						<xsl:with-param name="lab_permanent_delete" select="$lab_permanent_delete"/>
					</xsl:apply-templates>
				</table>
				<!-- Pagination -->
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page">
						<xsl:value-of select="$cur_page"/>
					</xsl:with-param>
					<xsl:with-param name="page_size">
						<xsl:value-of select="$page_size"/>
					</xsl:with-param>
					<xsl:with-param name="page_size_name">pagesize</xsl:with-param>
					<xsl:with-param name="total">
						<xsl:value-of select="$total"/>
					</xsl:with-param>
					<xsl:with-param name="width">
						<xsl:value-of select="$wb_gen_table_width"/>
					</xsl:with-param>
					<xsl:with-param name="timestamp">
						<xsl:value-of select="$page_timestamp"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template match="entity" mode="draw_usr_grp">
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="usg_cnt"/>
	
		<xsl:if test="position() mod 4 = 1 ">
			<xsl:text disable-output-escaping="yes">&lt;tr&gt;</xsl:text>
		</xsl:if>
		<td width="24%" valign="top" >
			<table>
				<tr>	
					<td width="32" align="center">
						<xsl:choose>
							<xsl:when test=" @role = 'SYSTEM'">
								<img src="{$wb_img_path}icon_recyc.gif" border="0"/>
							</xsl:when>
							<xsl:otherwise>
								<img src="{$wb_img_path}ico_big_folder.gif" border="0"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
					<td>
						<xsl:choose>
							<xsl:when test="@role = 'SYSTEM'">
								<a href="javascript:usr.group.manage_grp_del_usr('1')" class="btn wzb-btn-blue">
									<xsl:value-of select="$lab_lost_and_found"/>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a href="javascript:usr.group.manage_grp('{@id}','','{$parent_id}')" class="Text">
									<xsl:value-of select="@display_bil"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</table>
		</td>
		<xsl:if test="position() mod 4 = 0 or position() = last()">
			<xsl:text disable-output-escaping="yes">&lt;/tr&gt;</xsl:text>
			<tr>
				<td height="5" colspan="4">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template match="entity" mode="grp_general">
		<xsl:param name="lab_lost_and_found"/>
		<tr>
			<td width="40%">
				<span>
					<xsl:if test="$page_variant/@canMaitainUsg = 'true'">
					<xsl:choose>
						<xsl:when test="@id != /user_manager/meta/cur_usr/@ent_id">
							<input type="checkbox" name="entity_id_chkbox" value="{@id}"/>
						</xsl:when>
						<xsl:otherwise>
							<input type="checkbox" name="entity_id_chkbox" value="{@id}">
								<xsl:attribute name="disabled">disabled</xsl:attribute>
							</input>
						</xsl:otherwise>
					</xsl:choose>
					</xsl:if>
				</span>
				<input type="hidden" name="usr_timestamp" value="{@timestamp}"/>
				<input type="hidden" name="usr_name" value="{@display_bil}"/>
				<a href="javascript:usr.user.manage_usr('{@id}','{$parent_id}','','')" class="Text">
					<xsl:value-of select="@display_bil"/>
					<!--(<xsl:value-of select="@usr_id"/>)-->
				</a>
			</td>
			<td width="40%">
				<xsl:choose>
					<xsl:when test="@usr_id != ''">
						<xsl:value-of select="@usr_id"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>&#160;</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			
			<!--<td width="300">
				<span class="Text">
					<xsl:apply-templates select="role_list/role"/>
					<xsl:if test="count(role_list/role) &lt;= 0">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</xsl:if>
				</span>
			</td>-->
			<td width="20%" align="right">
				<xsl:call-template name="display_time">
					<xsl:with-param name="my_timestamp">
						<xsl:value-of select="@timestamp"/>
					</xsl:with-param>
				</xsl:call-template>
			</td>
		</tr>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template match="entity" mode="grp_system">
		<xsl:param name="lab_user"/>
		<xsl:param name="lab_g_lst_btn_cut"/>
		<xsl:param name="lab_g_lst_btn_restore"/>
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_permanent_delete"/>
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2">RowsEven</xsl:when>
				<xsl:otherwise>RowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr valign="middle" class="{$row_class}">
			<td width="8">
				<input type="hidden" name="entity_id_chkbox" value="{@id}"/>
				<input type="hidden" name="usr_timestamp" value="{@timestamp}"/>
				<input type="hidden" name="usr_name" value="{@display_bil}"/>
			</td>
			<xsl:choose>
				<xsl:when test="@type = 'USG'">
					<td>
						<xsl:choose>
							<xsl:when test="@role = 'SYSTEM'">
								<xsl:value-of select="$lab_lost_and_found"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="text()"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
					<td>
						<xsl:value-of select="$lab_group"/>
					</td>
				</xsl:when>
				<xsl:when test="@type = 'USR'">
					<td>
						<input type="checkbox" name="usr_id_lst" value="{@id}"/>
					</td>
					<td width="25%">
						<a href="javascript:usr.user.manage_del_usr('{@id}','{$parent_id}')">
							<xsl:value-of select="@usr_id"/>
						</a>
					</td>
					<td width="25%">
						
							<xsl:value-of select="text()"/>
							<xsl:value-of select="@display_bil"/>
					</td>
					<!--<td width="80">
						<span class="Text">
							<xsl:value-of select="$lab_user"/>
						</span>
					</td>
					<td width="300">
						<span class="Text">
							<xsl:apply-templates select="role_list/role"/>
						</span>
					</td>-->
					<td width="25%">
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp">
								<xsl:value-of select="@timestamp"/>
							</xsl:with-param>
						</xsl:call-template>
					</td>
				</xsl:when>
			</xsl:choose>
			<td align="right" width="20%">
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_permanent_delete"></xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.delete_user('<xsl:value-of select="./@id" />','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
				</xsl:call-template>
			</td>
<!-- 			<td align="right"> -->
<!-- 				<xsl:call-template name="wb_gen_button"> -->
<!-- 					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_lst_btn_cut"/> -->
<!-- 					<xsl:with-param name="wb_gen_btn_href">javascript:usr.utils.cut('<xsl:value-of select="$parent_id"/>','<xsl:value-of select="@id"/>','<xsl:value-of select="@type"/>','<xsl:value-of select="@role"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param> -->
<!-- 				</xsl:call-template> -->
<!-- 				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/> -->
<!-- 				<xsl:call-template name="wb_gen_button"> -->
<!-- 					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_lst_btn_restore"/> -->
<!-- 					<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.restore_exec('<xsl:value-of select="./@id"/>', 'usr_manager')</xsl:with-param> -->
<!-- 				</xsl:call-template> -->
<!-- 			</td> -->
		</tr>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template name="draw_search">
		<xsl:param name="lab_g_txt_btn_search"/>
		<xsl:param name="lab_adv_search"/>
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_all_users"/>
		<xsl:param name="lab_default_page"></xsl:param>
		<xsl:param name="extra_td"/>
		<xsl:param name="lab_user_list"/>
		<xsl:param name="lab_g_txt_btn_import"/>
		<xsl:param name="lab_g_txt_btn_delete"/>
		<xsl:param name="lab_user_placeholder" />
		
		<xsl:param name="lab_group"/>
		

				<td width="30%" >
					<xsl:call-template name="wb_ui_nav_link">
						<xsl:with-param name="width">100%</xsl:with-param>
						<xsl:with-param name="text">
							<xsl:choose>
								<xsl:when test="$parent_id = $root_ent_id">
									<xsl:value-of select="$lab_all_users"/>
								</xsl:when>
								<xsl:otherwise>
									<a class="NavLink" href="javascript:wb_utils_nav_go('FTN_AMD_USR_INFO',{../meta/cur_usr/@ent_id},'{$wb_lang}')">
										<xsl:value-of select="$lab_all_users"/>
									</a>
									<xsl:text>&#160;&gt;&#160;</xsl:text>
									<xsl:for-each select="ancestor_node_list/node">
										<a class="NavLink" href="javascript:usr.group.manage_grp('{@id}','','{$root_ent_id}')">
											<xsl:value-of select="title/."/>
										</a>
										<xsl:text>&#160;&gt;&#160;</xsl:text>
									</xsl:for-each>
									<xsl:choose>
										<xsl:when test="$parent_role = 'SYSTEM'">
											<xsl:value-of select="$lab_lost_and_found"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:choose>
												<xsl:when test="$parent_id = $root_ent_id">
													<xsl:value-of select="../meta/cur_usr/@root_display"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="desc"/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:with-param>
					</xsl:call-template>
				</td>
				<td width="70%" align="right">
							<div class="wzb-form-search form-search">
									<input type="text" size="25" name="s_usr_id_display_bil" class="form-control" placeholder="{$lab_user_placeholder}" style="width:175px;"/>
									<input type="button" class="form-submit margin-right4" value="" onclick="usr.search.search_exec(document.searchFrm, '{$wb_lang}')"/>
								<a href="javascript:usr.search.adv_search_prep('','{$wb_lang}')" class="btn wzb-btn-blue margin-right4">
									<xsl:value-of select="$lab_adv_search"/>
								</a>
							
							<xsl:if test="$parent_id=0 and $parent_role='SYSTEM'">
								<input type="hidden" name="s_status" value="deleted"/>
							</xsl:if>
							<input type="hidden" name="stylesheet"/>
							<input type="hidden" name="cmd"/>
							<!--<input type="hidden" name="ent_id" value="{$parent_id}"/>-->
							<input type="hidden" name="filter_user_group" value="1"/>
						
							<xsl:choose>
								<xsl:when test="$page_variant/@hasUsgAddBtn = 'true' and $page_variant/@canMaitainUsg = 'true' and $page_variant/@hasUsrImporBtn = 'true'">
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="class">
												btn wzb-btn-orange  margin-right4
											</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_import" />
											<xsl:with-param name="wb_gen_btn_href">javascript:Batch.User.Import.prep()</xsl:with-param>
										</xsl:call-template>
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="class">
											btn wzb-btn-orange  margin-right4
										</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_delete" />
										<xsl:with-param name="wb_gen_btn_href">javascript:Batch.User.Import.del()</xsl:with-param>
									</xsl:call-template>

									<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="class">
												btn wzb-btn-orange 
											</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_name" select="$label_core_user_management_49" />
											<xsl:with-param name="wb_gen_btn_href">javascript:Batch.User.Import.export_user()</xsl:with-param>
										</xsl:call-template>
								</xsl:when>
						
							</xsl:choose>
							</div>
				</td>

	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template match="role_list/role">
		<xsl:call-template name="get_rol_title"/>
		<xsl:if test="position() != last()">
			<xsl:text>,&#160;</xsl:text>
		</xsl:if>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template match="cur_usr"/>
</xsl:stylesheet>
