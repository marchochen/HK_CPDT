<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<!--====================================================-->
	<xsl:output  indent="yes"/>
	<xsl:variable name="total" select="/user_manager/group_member_list/search/@total"/>
	<xsl:variable name="cur_page" select="/user_manager/group_member_list/search/@cur_page"/>
	<xsl:variable name="page_size" select="/user_manager/group_member_list/search/@page_size"/>
	<xsl:variable name="sort_by" select="/user_manager/group_member_list/search/@sort_by"/>
	<xsl:variable name="cur_order" select="/user_manager/group_member_list/search/@order_by"/>
	<xsl:variable name="order_by">
		<xsl:choose>
			<xsl:when test="$cur_order = 'ASC' ">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="wb_gen_table_width">500</xsl:variable>
	<!--====================================================-->
	<xsl:template match="/user_manager">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="group_member_list">
			<xsl:with-param name="lab_select_usr">請選擇一個用戶</xsl:with-param>
			<xsl:with-param name="lab_no_usr">很抱歉，找不到符合您檢索條件的結果</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="group_member_list">
			<xsl:with-param name="lab_select_usr">请选择一个用户</xsl:with-param>
			<xsl:with-param name="lab_no_usr">很抱歉，找不到符合您检索条件的结果</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="group_member_list">
			<xsl:with-param name="lab_select_usr">Please select users</xsl:with-param>
			<xsl:with-param name="lab_no_usr">Sorry, no results were found</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!--====================================================-->
	<xsl:template match="group_member_list">
		<xsl:param name="lab_select_usr"/>
		<xsl:param name="lab_no_usr"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
				usr = new wbUserGroup;
				goldenman =  new wbGoldenMan;
				max_select = getUrlParam("max_select");
									
	]]></script>
			<xsl:call-template name="new_css"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frmXml">
				<xsl:choose>
					<xsl:when test="count(user) = 0 ">
						<xsl:call-template name="wb_ui_title">
							<xsl:with-param name="text"  select="$lab_select_usr"/>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_no_usr"/>
						</xsl:call-template>
						<table>
							<tr>
								<td align="center" valign="middle">
									<xsl:call-template name="wb_gen_form_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_close"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:window.close()</xsl:with-param>
									</xsl:call-template>
								</td>
							</tr>
						</table>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wb_ui_title">
							<xsl:with-param name="text" select="$lab_select_usr"/>
						</xsl:call-template>					
						<table class="table wzb-ui-table" >
							<tr class="wzb-ui-table-head">
								<td>
								</td>
								<td>
									<script language="Javascript"><![CDATA[
									if (max_select==1)
										document.write('<span style="display:none">');
								]]></script>
									<xsl:call-template name="select_all_checkbox">
										<xsl:with-param name="chkbox_lst_cnt"  select="count(user)"/>
										<xsl:with-param name="display_icon">false</xsl:with-param>
										<xsl:with-param name="sel_all_chkbox_nm">sel_all_checkbox</xsl:with-param>
									</xsl:call-template>
									<script language="Javascript"><![CDATA[
									if (max_select==1)
										document.write('</span>');
								]]></script>
								</td>
								<TD align="left">
									<xsl:value-of select="$lab_login_id"/>
								</TD>									
								<TD align="left">
									<a href="javascript:goldenman.openitemacc('{$order_by}')" class="TitleText">
										<xsl:value-of select="$lab_dis_name"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_order"/>
										</xsl:call-template>
									</a>
								</TD>
								<TD align="left">
									<span class="TitleText">
										<xsl:value-of select="$lab_nickname"/>
									</span>
								</TD>
								<TD align="left">
									<xsl:value-of select="$lab_group"/>
								</TD>
							</tr>
							<xsl:for-each select="user">
								<tr valign="middle">
									<td>
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										<script language="JavaScript"><![CDATA[
									usr_]]><xsl:value-of select="@ent_id"/><![CDATA[_id = ]]><xsl:value-of select="@ent_id"/><![CDATA[
									usr_]]><xsl:value-of select="@ent_id"/><![CDATA[_type = ]]><xsl:value-of select="@ent_id"/><![CDATA[
									usr_]]><xsl:value-of select="@ent_id"/><![CDATA[_name = ']]><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="name/@display_name"/></xsl:with-param></xsl:call-template><![CDATA['
								]]></script>
									</td>
									<td>
										<script language="Javascript"><![CDATA[
										if (max_select==1)
											document.write('<input type="radio" name="usr" value="]]><xsl:value-of select="@ent_id"/><![CDATA["/>');
										else
											document.write('<input type="checkbox" name="usr" value="]]><xsl:value-of select="@ent_id"/><![CDATA["/>');
									]]></script>
									</td>
										<td>
											<xsl:value-of select="@id"/>
										</td>											
									<td align="left">
										<xsl:value-of select="name/@display_name"/>
									</td>
									<td align="left">
										<xsl:value-of select="name/@usr_nickname"/>
									</td>
									<td align="left">
										<xsl:choose>
											<xsl:when test="full_path">
												<xsl:value-of select="full_path/text()"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:for-each select="user_attribute_list/attribute_list[@type='USG']/entity">
													<xsl:if test="position()!='1'">;&#160;</xsl:if>
													<xsl:value-of select="@display_bil"/>
												</xsl:for-each>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</xsl:for-each>
						</table>
						<xsl:call-template name="wb_ui_pagination">
							<xsl:with-param name="cur_page" select="$cur_page"/>
							<xsl:with-param name="page_size" select="$page_size"/>
							<xsl:with-param name="total" select="$total"/>
							<xsl:with-param name="width" select="$wb_gen_table_width"/>
						</xsl:call-template>
						<div class="wzb-bar">
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:goldenman.pickitemacc(document.frmXml,max_select)</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name"  select="$lab_g_form_btn_cancel"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:window.close()</xsl:with-param>
							</xsl:call-template>
						</div>
					</xsl:otherwise>
				</xsl:choose>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>
