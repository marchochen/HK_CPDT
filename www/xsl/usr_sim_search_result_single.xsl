<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<!-- others -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/label_role.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<!-- paginatoin variables -->
	<xsl:variable name="page_size" select="user_manager/group_member_list/search/@page_size"/>
	<xsl:variable name="cur_page" select="user_manager/group_member_list/search/@cur_page"/>
	<xsl:variable name="page_total" select="user_manager/group_member_list/search/@total"/>
	<xsl:variable name="page_timestamp" select="user_manager/group_member_list/search/@time"/>
	<!-- sorting variable -->
	<xsl:variable name="sort_by" select="user_manager/group_member_list/search/@sort_by"/>
	<xsl:variable name="cur_order" select="user_manager/group_member_list/search/@order_by"/>
	<xsl:variable name="order_by">
		<xsl:choose>
			<xsl:when test="$cur_order = 'ASC' ">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="parent_id" select="user_manager/group_member_list/@id"/>
	<xsl:variable name="parent_role" select="user_manager/group_member_list/@grp_role"/>
	<xsl:variable name="cur_usr" select="user_manager/group_member_list/cur_usr/@id"/>
	<xsl:variable name="root_ent_id" select="user_manager/group_member_list/cur_usr/@root_ent_id"/>
	<xsl:variable name="entity_cnt">
		<xsl:value-of select="count(user_manager/group_member_list/user)"/>
	</xsl:variable>
	<xsl:variable name="page_variant" select="/user_manager/meta/page_variant"/>
	<!-- =============================================================== -->
	<!-- =============================================================== -->
	<xsl:template match="/user_manager">
		<xsl:apply-templates select="group_member_list"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="group_member_list">
		<html>
			<head>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[
				usr = new wbUserGroup;
				window.onunload = unloadHandler;
				page_timestamp = ']]><xsl:call-template name="escape_js">
						<xsl:with-param name="input_str"><xsl:value-of select="$page_timestamp"/></xsl:with-param></xsl:call-template><![CDATA['
				function status(){
					usr.search_exec(document.frmXml,']]><xsl:value-of select="$wb_lang"/><![CDATA[')
					return false;
				}		
				function unloadHandler(funcName){
					//use in new enrolllment
					if (window.opener != null){
						if (window.opener.unloadPSrh != null) {
							window.opener.unloadPSrh()
						}
					}
				}
			]]></script>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<xsl:call-template name="new_css"/>
			</head>
			<BODY marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
				<form onsubmit="return status()" name="frmXml">
					<input type="hidden" name="stylesheet"/>
					<input type="hidden" name="cmd"/>
					<input type="hidden" name="ent_id" value="{$parent_id}"/>
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</BODY>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_manager">用戶管理</xsl:with-param>
			<xsl:with-param name="lab_user_list">用戶管理</xsl:with-param>
			<xsl:with-param name="lab_name"><xsl:value-of select="$lab_dis_name"/></xsl:with-param>
			<xsl:with-param name="lab_grp_name">小組名稱</xsl:with-param>
			<xsl:with-param name="lab_grp_info">小組資料</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_copy">複製</xsl:with-param>
			<xsl:with-param name="lab_total">總數</xsl:with-param>
			<xsl:with-param name="lab_admin_grp">管理員</xsl:with-param>
			<xsl:with-param name="lab_stu_grp">學生組</xsl:with-param>
			<xsl:with-param name="lab_sys_grp">系統組</xsl:with-param>
			<xsl:with-param name="lab_tch_grp">導師組</xsl:with-param>
			<xsl:with-param name="lab_user">用戶</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
			<xsl:with-param name="lab_group"><xsl:value-of select="$lab_group"/></xsl:with-param>
			<xsl:with-param name="lab_not_match">很抱歉，找不到符合您檢索條件的用戶！</xsl:with-param>
			<xsl:with-param name="lab_search_result">檢索結果</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_login_id">用戶名稱</xsl:with-param>
			<xsl:with-param name="lab_sel_usr">請選擇用戶</xsl:with-param>
			<xsl:with-param name="lab_role">身份</xsl:with-param>
			<xsl:with-param name="lab_legend">注意:此列表中不包含已經<xsl:value-of select="$lab_const_enrolled_sm"/>課程的學員</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_select">選擇</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_manager">用户管理</xsl:with-param>
			<xsl:with-param name="lab_user_list">用户管理</xsl:with-param>
			<xsl:with-param name="lab_name"><xsl:value-of select="$lab_dis_name"/></xsl:with-param>
			<xsl:with-param name="lab_grp_name">小组名称</xsl:with-param>
			<xsl:with-param name="lab_grp_info">小组资料</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_copy">复制</xsl:with-param>
			<xsl:with-param name="lab_total">总计</xsl:with-param>
			<xsl:with-param name="lab_admin_grp">管理员组</xsl:with-param>
			<xsl:with-param name="lab_stu_grp">学员组</xsl:with-param>
			<xsl:with-param name="lab_sys_grp">系统管理</xsl:with-param>
			<xsl:with-param name="lab_tch_grp">教员组</xsl:with-param>
			<xsl:with-param name="lab_user">用户</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
			<xsl:with-param name="lab_group"><xsl:value-of select="$lab_group"/></xsl:with-param>
			<xsl:with-param name="lab_not_match">很抱歉，找不到符合您检索条件的用户！</xsl:with-param>
			<xsl:with-param name="lab_search_result">检索结果</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_login_id">用户名</xsl:with-param>
			<xsl:with-param name="lab_sel_usr">请选择一个用户</xsl:with-param>
			<xsl:with-param name="lab_role">身份</xsl:with-param>
			<xsl:with-param name="lab_legend">注意：此列表中不包含已经<xsl:value-of select="$lab_const_enrolled_sm"/>课程的学员</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_select">选择</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_manager">User manager</xsl:with-param>
			<xsl:with-param name="lab_user_list">User manager</xsl:with-param>
			<xsl:with-param name="lab_name"><xsl:value-of select="$lab_dis_name"/></xsl:with-param>
			<xsl:with-param name="lab_grp_name">Group name</xsl:with-param>
			<xsl:with-param name="lab_grp_info">Group information</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_copy">Copy</xsl:with-param>
			<xsl:with-param name="lab_total">Total</xsl:with-param>
			<xsl:with-param name="lab_admin_grp">ADMIN GROUP</xsl:with-param>
			<xsl:with-param name="lab_stu_grp">STUDENT GROUP</xsl:with-param>
			<xsl:with-param name="lab_sys_grp">SYSTEM GROUP</xsl:with-param>
			<xsl:with-param name="lab_tch_grp">TEACHER GROUP</xsl:with-param>
			<xsl:with-param name="lab_user">User</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">Deleted items</xsl:with-param>
			<xsl:with-param name="lab_group"><xsl:value-of select="$lab_group"/></xsl:with-param>
			<xsl:with-param name="lab_not_match">No results found</xsl:with-param>
			<xsl:with-param name="lab_search_result">Search result</xsl:with-param>
			<xsl:with-param name="lab_all">All</xsl:with-param>
			<xsl:with-param name="lab_login_id">Login ID</xsl:with-param>
			<xsl:with-param name="lab_sel_usr">Please select users</xsl:with-param>
			<xsl:with-param name="lab_role">Role</xsl:with-param>
			<xsl:with-param name="lab_legend">Note: Learners that have <xsl:value-of select="$lab_const_enrolled_sm"/> the course are excluded from the list</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_select">Select</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">Back</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_usr_manager"/>
		<xsl:param name="lab_user_list"/>
		<xsl:param name="lab_name"/>
		<xsl:param name="lab_grp_name"/>
		<xsl:param name="lab_grp_info"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_copy"/>
		<xsl:param name="lab_total"/>
		<xsl:param name="lab_admin_grp"/>
		<xsl:param name="lab_stu_grp"/>
		<xsl:param name="lab_sys_grp"/>
		<xsl:param name="lab_tch_grp"/>
		<xsl:param name="lab_user"/>
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_group"/>
		<xsl:param name="lab_search_result"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_login_in"/>
		<xsl:param name="lab_role"/>
		<xsl:param name="lab_legend"/>
		<xsl:param name="lab_g_form_btn_select"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_sel_usr"/>
		<xsl:param name="lab_not_match"/>
	<xsl:call-template name="wb_ui_title">
		<xsl:with-param name="width">510</xsl:with-param>
		<xsl:with-param name="text">
			<xsl:value-of select="$lab_sel_usr"/>
		</xsl:with-param>
	</xsl:call-template>		
		
			<xsl:choose>
				<xsl:when test="$entity_cnt &gt;= 1">
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td>
							<xsl:value-of select="$lab_name"/>
						</td>
						<script language="javascript" type="text/javascript"><![CDATA[
						var str = ''
						str += '<td><span class="TitleText">]]><xsl:value-of select="$lab_role"/><![CDATA[</span></td>'
						if (getUrlParam('s_search_role') != '0') {document.write(str);}
					]]></script>
						<td>
							<xsl:value-of select="$lab_group"/>
						</td>
					</tr>				
						<xsl:apply-templates select="user">
							<xsl:with-param name="lab_admin_grp" select="$lab_admin_grp"/>
							<xsl:with-param name="lab_stu_grp" select="$lab_stu_grp"/>
							<xsl:with-param name="lab_sys_grp" select="$lab_sys_grp"/>
							<xsl:with-param name="lab_tch_grp" select="$lab_tch_grp"/>
							<xsl:with-param name="lab_user" select="$lab_user"/>
							<xsl:with-param name="lab_lost_and_found" select="$lab_lost_and_found"/>
							<xsl:with-param name="lab_group" select="$lab_group"/>
						</xsl:apply-templates>
					</table>
			<xsl:call-template name="wb_ui_pagination">
				<xsl:with-param name="cur_page" select="$cur_page"/>
				<xsl:with-param name="page_size" select="$page_size"/>
				<xsl:with-param name="total" select="$page_total"/>
				<xsl:with-param name="width">510</xsl:with-param>
				<xsl:with-param name="timestamp" select="$page_timestamp"/>
			</xsl:call-template>					
				</xsl:when>
				<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="width">510</xsl:with-param>
					<xsl:with-param name="text" select="$lab_not_match"/>
				</xsl:call-template>				
				</xsl:otherwise>
			</xsl:choose>
		
		<div class="wzb-bar">
			<xsl:choose>
				<xsl:when test="$page_total&gt;0">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_select"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:usr.search.get_popup_usr_lst(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_back"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_close"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:window.close()</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="user">
		<xsl:param name="lab_stu_grp"/>
		<xsl:param name="lab_sys_grp"/>
		<xsl:param name="lab_tch_grp"/>
		<xsl:param name="lab_user"/>
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_group"/>
		<tr valign="middle">
			<script><![CDATA[ent_name]]><xsl:value-of select="./@ent_id"/><![CDATA[=']]><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:choose><xsl:when test="@usg_role = 'SYSTEM'"><xsl:value-of select="$lab_lost_and_found"/></xsl:when><xsl:otherwise><xsl:value-of select="name/@display_name"/></xsl:otherwise></xsl:choose></xsl:with-param></xsl:call-template><![CDATA[';]]></script>
			<xsl:variable name="ent_name"><![CDATA[ent_name]]><xsl:value-of select="./@ent_id"/>
			</xsl:variable>
			<td>
				<span>
					<input type="radio" name="usr_id" value="{@ent_id}"/>
					<input type="hidden" name="usr_id_nm_{@ent_id}" value="{name/@display_name}"/>
					<input type="hidden" name="usr_id_usr_id_{@ent_id}" value="{@id}"/>
				</span>
				<xsl:choose>
					<xsl:when test="@usg_role = 'SYSTEM'">
						<xsl:value-of select="."/>(<xsl:value-of select="@usr_id"/>)
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="name/@display_name"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<script language="javascript" type="text/javascript"><![CDATA[
			var str = ''
			str += '<td>'
			str += '<span class="Text">'
			str += ']]><xsl:for-each select="roles/role"><xsl:if test="position()!=1"><xsl:text>; </xsl:text></xsl:if><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:call-template name="get_rol_title"/></xsl:with-param></xsl:call-template></xsl:for-each><![CDATA['
			str += '</span>'
			str += '</td>'
			if (getUrlParam('s_search_role') != '0') {document.write(str);}
		]]></script>
			<td>
				<xsl:for-each select="user_attribute_list/attribute_list[@type='USG']/entity">
					<xsl:choose>
						<xsl:when test="@role='SYSTEM'">
							<xsl:value-of select="$lab_lost_and_found"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="@display_bil"/>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="not(position()=last())">
						<xsl:text>;&#160;</xsl:text>
					</xsl:if>
				</xsl:for-each>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="cur_usr"/>
	<!-- =============================================================== -->
</xsl:stylesheet>
