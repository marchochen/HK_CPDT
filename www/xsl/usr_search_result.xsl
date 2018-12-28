<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>	
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>	
	<xsl:import href="utils/wb_ui_pagination.xsl"/>		
	<!-- others-->
	<xsl:import href="share/usr_gen_tab_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>

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
	<xsl:variable name="parent_id">
		<xsl:choose>
			<xsl:when test="user_manager/group_member_list/@grp_role  !=  'ROOT'">
				<xsl:value-of select="user_manager/group_member_list/@id"/>
			</xsl:when>
			<xsl:otherwise>0</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="parent_role" select="user_manager/group_member_list/@grp_role"/>
	<xsl:variable name="cur_usr" select="user_manager/group_member_list/cur_usr/@id"/>
	<xsl:variable name="root_ent_id" select="user_manager/meta/cur_usr/@root_ent_id"/>
	<xsl:variable name="entity_cnt" select="count(user_manager/group_member_list/user)"/>
	<xsl:variable name="page_variant" select="/user_manager/meta/page_variant"/>
	<xsl:variable name="s_usr_id_display_bil" select="user_manager/group_member_list/search/@s_usr_id_display_bil"/>
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
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[
				usr = new wbUserGroup;
				page_timestamp = ']]><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$page_timestamp"/></xsl:with-param></xsl:call-template><![CDATA['
				function status(){
					usr.search.search_exec(document.frmXml,']]><xsl:value-of select="$wb_lang"/><![CDATA[')
					return false;
				}	
			]]></script>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			</head>
			<BODY marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
				<form onsubmit="return status()" name="frmXml">
					<xsl:choose>
						<xsl:when test="$parent_id=1 and $parent_role=''">
							<input type="hidden" name="s_status" value="deleted"/>
							<input type="hidden" name="ent_id" value="0"/>
						</xsl:when>
						<xsl:otherwise>
							<input type="hidden" name="ent_id" value="{$parent_id}"/>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</BODY>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_manager">用戶管理</xsl:with-param>
			<xsl:with-param name="lab_user">用戶</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
			<xsl:with-param name="lab_not_match">很抱歉，找不到符合您搜索條件的用戶！</xsl:with-param>
			<xsl:with-param name="lab_search_result">搜索結果</xsl:with-param>
			<xsl:with-param name="lab_recycle_bin">回收站</xsl:with-param>
			<xsl:with-param name="lab_last_modified">最近更新日期</xsl:with-param>
			<xsl:with-param name="lab_g_lst_btn_copy">複製</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_search">搜索</xsl:with-param>
			<xsl:with-param name="lab_adv_search">高級搜索</xsl:with-param>
			<xsl:with-param name="lab_user_placeholder">請輸入用戶名搜索</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_manager">用户管理</xsl:with-param>
			<xsl:with-param name="lab_user">用户</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
			<xsl:with-param name="lab_not_match">很抱歉，找不到符合搜索条件的用户！</xsl:with-param>
			<xsl:with-param name="lab_search_result">搜索结果</xsl:with-param>
			<xsl:with-param name="lab_recycle_bin">回收站</xsl:with-param>
			<xsl:with-param name="lab_last_modified">最近更新日期</xsl:with-param>
			<xsl:with-param name="lab_g_lst_btn_copy">复制</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_search">搜索</xsl:with-param>
			<xsl:with-param name="lab_adv_search">高级搜索</xsl:with-param>
			<xsl:with-param name="lab_user_placeholder">请输入用户名搜索</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_manager">User management</xsl:with-param>
			<xsl:with-param name="lab_user">User</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">Deleted items</xsl:with-param>
			<xsl:with-param name="lab_not_match">No results found</xsl:with-param>
			<xsl:with-param name="lab_search_result">Search result</xsl:with-param>
			<xsl:with-param name="lab_recycle_bin">Recycle bin</xsl:with-param>
			<xsl:with-param name="lab_last_modified">Last modified</xsl:with-param>
			<xsl:with-param name="lab_g_lst_btn_copy">Copy</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_search">Search</xsl:with-param>
			<xsl:with-param name="lab_adv_search">Advanced search</xsl:with-param>
			<xsl:with-param name="lab_user_placeholder">User name</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_usr_manager"/>
		<xsl:param name="lab_user"/>
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_not_match"/>
		<xsl:param name="lab_search_result"/>
		<xsl:param name="lab_recycle_bin"/>
		<xsl:param name="lab_last_modified"/>
		<xsl:param name="lab_g_lst_btn_copy"/>
		<xsl:param name="lab_g_txt_btn_search"/>
		<xsl:param name="lab_adv_search"/>
		<xsl:param name="lab_user_placeholder" />
		<!-- heading -->
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_USR_INFO</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_USR_INFO</xsl:with-param>
		</xsl:call-template>

		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="($parent_id = $root_ent_id) or ($parent_id = 0) or count(ancestor_node_list/node) = 0">
						<xsl:value-of select="$lab_search_result"/>
					</xsl:when>
					<xsl:otherwise>
						<a class="NavLink" href="javascript:wb_utils_nav_go('FTN_AMD_USR_INFO',{../meta/cur_usr/@ent_id},'{$wb_lang}')">
							<xsl:value-of select="$lab_search_result"/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:for-each select="ancestor_node_list/node">
							<a class="NavLink" href="javascript:usr.group.manage_grp('{@id}','{title/.}','{$root_ent_id}')">
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
	<!--	<xsl:call-template name="usr_gen_tab">
			<xsl:with-param name="usr_target_tab">1</xsl:with-param>
		</xsl:call-template>-->
		<xsl:call-template name="draw_search">
			<xsl:with-param name="lab_g_txt_btn_search" select="$lab_g_txt_btn_search"/>
			<xsl:with-param name="lab_adv_search" select="$lab_adv_search"/>
			<xsl:with-param name="lab_user_placeholder" select="$lab_user_placeholder" />
		</xsl:call-template>

		<xsl:choose>
			<xsl:when test="$entity_cnt &gt;= 1">
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td width="25%">
							<xsl:variable name="_order">
								<xsl:choose>
									<xsl:when test="$sort_by = 'usr_display_bil' ">
										<xsl:value-of select="$order_by"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$cur_order"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>						
							<xsl:choose>
								<xsl:when test="$sort_by = 'usr_display_bil' ">
									<a class="TitleText" href="javascript:usr.search.search_result({$root_ent_id},'{$page_timestamp}','usr_display_bil','{$order_by}',{$cur_page},{$page_size},'{$s_usr_id_display_bil}')">
										<xsl:value-of select="$lab_dis_name"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path">
												<xsl:value-of select="$wb_img_path"/>
											</xsl:with-param>
											<xsl:with-param name="sort_order">
												<xsl:value-of select="$cur_order"/>
											</xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a class="TitleText" href="javascript:usr.search.search_result({$root_ent_id},'{$page_timestamp}','usr_display_bil','ASC',{$cur_page},{$page_size},'{$s_usr_id_display_bil}')">
										<xsl:value-of select="$lab_dis_name"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>						
						</td>
						<td width="25%">
							<xsl:value-of select="$lab_login_id"/>
						</td>
						<td width="25%">
							<xsl:value-of select="$lab_group"/>
						</td>
						<td nowrap="nowrap" width="25%">
							<xsl:value-of select="$lab_last_modified"/>
						</td>
					</tr>
					<xsl:apply-templates select="user">
						<xsl:with-param name="lab_user" select="$lab_user"/>
						<xsl:with-param name="lab_lost_and_found" select="$lab_lost_and_found"/>
						<xsl:with-param name="lab_recycle_bin" select="$lab_recycle_bin"/>
						<xsl:with-param name="lab_g_lst_btn_copy" select="$lab_g_lst_btn_copy"/>
					</xsl:apply-templates>
				</table>
					<xsl:call-template name="wb_ui_pagination">
						<xsl:with-param name="cur_page" select="$cur_page"/>
						<xsl:with-param name="cur_page_name">page</xsl:with-param>
						<xsl:with-param name="page_size" select="$page_size"/>
						<xsl:with-param name="total" select="$page_total"/>
						<xsl:with-param name="timestamp" select="$page_timestamp"/>
					</xsl:call-template>				
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_not_match"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template name="draw_search">
		<xsl:param name="lab_g_txt_btn_search"/>
		<xsl:param name="lab_adv_search"/>
		<xsl:param name="lab_user_placeholder" />
		<table>
			<tr>
				<td width="70%" align="right">
					<table>
						<tr>
							<td  align="right">
								<div class="wzb-form-search form-search">
									<input type="text" size="11" name="s_usr_id_display_bil" class="form-control" placeholder="{$lab_user_placeholder}" style="width:175px;" value="{$s_usr_id_display_bil}"/>
									<input type="button" class="form-submit" value="" onclick="usr.search.search_exec(document.frmXml, '{$wb_lang}')"/>
									<xsl:text>&#160;</xsl:text>
								<a href="javascript:usr.search.adv_search_prep('{$parent_id}','{$wb_lang}')" class="btn wzb-btn-blue vbtm"><xsl:value-of select="$lab_adv_search"/></a>
								</div>
							</td>
							<xsl:if test="$parent_id=0 and $parent_role='SYSTEM'">
								<input type="hidden" name="s_status" value="deleted"/>
							</xsl:if>
							<input type="hidden" name="stylesheet"/>
							<input type="hidden" name="cmd"/>
							<input type="hidden" name="ent_id" value="{$parent_id}"/>							
							<input type="hidden" name="filter_user_group" value="1"/>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="user">
		<xsl:param name="lab_stu_grp"/>
		<xsl:param name="lab_sys_grp"/>
		<xsl:param name="lab_tch_grp"/>
		<xsl:param name="lab_user"/>
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_recycle_bin"/>
		<xsl:param name="lab_g_lst_btn_copy"/>
		<tr valign="middle">
			<script><![CDATA[ent_name]]><xsl:value-of select="@ent_id"/><![CDATA[=']]><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:choose><xsl:when test="@usg_role = 'SYSTEM'"><xsl:value-of select="$lab_lost_and_found"/></xsl:when><xsl:otherwise><xsl:value-of select="name/@display_name"/></xsl:otherwise></xsl:choose></xsl:with-param></xsl:call-template><![CDATA[';]]></script>
			<xsl:variable name="ent_name"><![CDATA[ent_name]]><xsl:value-of select="@ent_id"/>
			</xsl:variable>
			<td>
				<xsl:choose>
					<xsl:when test="@usg_role = 'SYSTEM'">
						<xsl:value-of select="."/>(<xsl:value-of select="@usr_id"/>)
					</xsl:when>
					<xsl:otherwise>
						<a href="javascript:usr.user.manage_usr('{@ent_id}','{$parent_id}','search',page_timestamp,{$ent_name})" class="Text"><xsl:value-of select="name/@display_name"/></a>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td width="80">
				<xsl:value-of select="@id"/>
			</td>
			<td width="300">
				<xsl:choose>
					<xsl:when test="@status = 'DELETED' and count(user_attribute_list/attribute_list[@type='USG']/entity) = 0">
						<xsl:value-of select="$lab_recycle_bin"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="full_path"/>
						<!--
				<xsl:for-each select="user_attribute_list/attribute_list[@type='USG']/entity">
					<xsl:choose>
						<xsl:when test="@display_bil='LOST&amp;FOUND'"><xsl:value-of select="$lab_recycle_bin"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="@display_bil"/></xsl:otherwise>
					</xsl:choose>
					<xsl:if test="not(position()=last())"><xsl:text>;&#160;</xsl:text></xsl:if>
				</xsl:for-each>
				-->
					</xsl:otherwise>
				</xsl:choose><IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<!--<td>
				<span class="Text">
					<xsl:for-each select="roles/role">
						<xsl:if test="position()!=1">
							<xsl:text>;&#160;</xsl:text>
						</xsl:if>
						<xsl:value-of select="desc[@lan=$wb_lang_encoding]/@name"/>
					</xsl:for-each>
				</span><IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>-->
			<td>
				<xsl:call-template name="display_time">
					<xsl:with-param name="my_timestamp"><xsl:value-of select="@timestamp"/></xsl:with-param>
				</xsl:call-template>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="cur_usr"/>
	<!-- =============================================================== -->
</xsl:stylesheet>
