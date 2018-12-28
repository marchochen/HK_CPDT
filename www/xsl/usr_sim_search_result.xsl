<?xml version="1.0" encoding="UTF-8"?>
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
	<!-- usr utils -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/label_role.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<!-- paginatoin variables -->
	<xsl:variable name="group_member_list" select="//group_member_list"/>
	<xsl:variable name="page_size" select="$group_member_list/search/@page_size"/>
	<xsl:variable name="cur_page" select="$group_member_list/search/@cur_page"/>
	<xsl:variable name="page_total" select="$group_member_list/search/@total"/>
	<xsl:variable name="page_timestamp" select="$group_member_list/search/@time"/>
	<!-- sorting variable -->
	<xsl:variable name="sort_by" select="$group_member_list/search/@sort_by"/>
	<xsl:variable name="cur_order" select="$group_member_list/search/@order_by"/>
	<xsl:variable name="order_by">
		<xsl:choose>
			<xsl:when test="$cur_order = 'ASC' ">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="parent_id" select="$group_member_list/@id"/>
	<xsl:variable name="parent_role" select="$group_member_list/@grp_role"/>
	<xsl:variable name="cur_usr" select="$group_member_list/cur_usr/@id"/>
	<xsl:variable name="root_ent_id" select="$group_member_list/cur_usr/@root_ent_id"/>
	<xsl:variable name="entity_cnt">
		<xsl:value-of select="count($group_member_list/user)"/>
	</xsl:variable>
	<xsl:variable name="page_variant" select="//meta/page_variant"/>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:apply-templates select="//group_member_list"/>
	</xsl:template>
	<!-- =============================================================== -->
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
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_mystaff.js"/>
				<!--alert样式  -->
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> 
				
				<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
				<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
				<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
				
				<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
				<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
				<!--alert样式  end -->
				<script language="JavaScript" type="text/javascript"><![CDATA[
				var mystaff = new wbMyStaff;
				if (getUrlParam('close_when_empty') == '1') {
					entity_cnt = ]]><xsl:value-of select="$entity_cnt"/><![CDATA[;
					if (entity_cnt < 1) {
						self.close();
					}
				}
				if (getUrlParam('auto_enroll_ind') != null )
					auto_enroll_ind = getUrlParam('auto_enroll_ind');
				usr = new wbUserGroup;
				goldenman = new wbGoldenMan;
				window.onunload = unloadHandler;
				page_timestamp = ']]><xsl:call-template name="escape_js"><xsl:with-param name="input_str" select="$page_timestamp"/></xsl:call-template><![CDATA['

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
				function alive() { return true; }

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
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
			<xsl:with-param name="lab_not_match">很抱歉，找不到符合您檢索條件的用戶！</xsl:with-param>
			<xsl:with-param name="lab_sel_usr">請選擇用戶</xsl:with-param>
			<xsl:with-param name="lab_role">身份</xsl:with-param>
			<xsl:with-param name="lab_legend">注意:此列表中不包含已經<xsl:value-of select="$lab_const_enrolled_sm"/>課程的學員</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_select">選擇</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_new_search">重新檢索</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
			<xsl:with-param name="lab_not_match">很抱歉，找不到符合您搜索条件的用户！</xsl:with-param>
			<xsl:with-param name="lab_sel_usr">请选择用户</xsl:with-param>
			<xsl:with-param name="lab_role">身份</xsl:with-param>
			<xsl:with-param name="lab_legend">注意：此列表中不包含已经<xsl:value-of select="$lab_const_enrolled_sm"/>该课程的学员</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_select">选择</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_new_search">重新搜索</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_lost_and_found">Deleted items</xsl:with-param>
			<xsl:with-param name="lab_not_match">No results found</xsl:with-param>
			<xsl:with-param name="lab_sel_usr">Please select users</xsl:with-param>
			<xsl:with-param name="lab_role">Role</xsl:with-param>
			<xsl:with-param name="lab_legend">Note: Learners that have <xsl:value-of select="$lab_const_enrolled_sm"/> the course are excluded from the list</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_select">Select</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_new_search">New search</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">Back</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_role"/>
		<xsl:param name="lab_legend"/>
		<xsl:param name="lab_sel_usr"/>
		<xsl:param name="lab_not_match"/>
		<xsl:param name="lab_g_form_btn_select"/>
		<xsl:param name="lab_g_form_btn_new_search"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:param name="lab_g_form_btn_close"/>
		
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_sel_usr"/>
			<xsl:with-param name="text_class" >pop-up-title</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="$entity_cnt &gt;= 1">
				<table class="table wzb-ui-table" style="width:94%">
					<tr class="wzb-ui-table-head">
						<td>
							<xsl:call-template name="select_all_checkbox">
								<xsl:with-param name="chkbox_lst_cnt">
									<xsl:value-of select="count(user)"/>
								</xsl:with-param>
								<xsl:with-param name="chkbox_lst_nm">usr_id</xsl:with-param>
								<xsl:with-param name="display_icon">false</xsl:with-param>
							</xsl:call-template>
							<xsl:value-of select="$lab_login_id"/>
						</td>						
						<td align="center">
							<xsl:value-of select="$lab_dis_name"/>
						</td>
						<td align="right">
							<xsl:value-of select="$lab_group"/>
						</td>
					</tr>
					<xsl:apply-templates select="user">
						<xsl:with-param name="lab_lost_and_found" select="$lab_lost_and_found"/>
					</xsl:apply-templates>
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
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
		<div class="wzb-bar">
			<table>
				<tr>
					<td align="center">
						<xsl:choose>
							<xsl:when test="$page_total&gt;0">
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_select"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:usr.search.get_popup_usr_lst(document.frmXml,'<xsl:value-of select="$wb_lang"/>',auto_enroll_ind)</xsl:with-param>
								</xsl:call-template>
								<xsl:choose>
									<xsl:when test="//search_in_mystaff = 'true'">
										<xsl:call-template name="wb_gen_form_button">
											<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_new_search"/>
											<xsl:with-param name="wb_gen_btn_href">javascript:mystaff.popup_search_prep()</xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="wb_gen_form_button">
											<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_new_search"/>
											<xsl:with-param name="wb_gen_btn_href">javascript:usr.search.popup_search(0)</xsl:with-param>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
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
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="user">
		<xsl:param name="lab_lost_and_found"/>
		<tr valign="middle">
			<script><![CDATA[ent_name]]><xsl:value-of select="./@ent_id"/><![CDATA[=']]><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:choose><xsl:when test="@usg_role = 'SYSTEM'"><xsl:value-of select="$lab_lost_and_found"/></xsl:when><xsl:otherwise><xsl:value-of select="name/@display_name"/></xsl:otherwise></xsl:choose></xsl:with-param></xsl:call-template><![CDATA[';]]></script>

			<xsl:variable name="ent_name"><![CDATA[ent_name]]><xsl:value-of select="./@ent_id"/>
			</xsl:variable>
			<td>
				<input type="checkbox" name="usr_id" value="{@ent_id}"/>
				<input type="hidden" name="usr_id_nm_{@ent_id}" value="{name/@display_name}"/>
				<xsl:value-of select="@id"/>
			</td>			
			<td align="center">
				<xsl:value-of select="name/@display_name"/>
			</td>
			<td align="right">
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
