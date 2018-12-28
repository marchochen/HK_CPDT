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
	<xsl:output  indent="yes"/>
	<xsl:variable name="total" select="/user_manager/group_member_list/search/@total"/>
	<xsl:variable name="cur_page" select="/user_manager/group_member_list/search/@cur_page"/>
	<xsl:variable name="page_size" select="/user_manager/group_member_list/search/@page_size"/>
	<xsl:variable name="sort_by" select="/user_manager/group_member_list/search/@sort_by"/>
	<xsl:variable name="cur_order" select="/user_manager/group_member_list/search/@order_by"/>
	<xsl:variable name="order_by">
		<xsl:choose>
			<xsl:when test="$cur_order = 'ASC' or $cur_order = 'asc' ">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<!--====================================================-->
	<xsl:template match="/user_manager">
		<html>
			<head>
			<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
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
				goldenman =  new wbGoldenMan
				
				max_select = getUrlParam('max_select')
				//alert(max_select)
									
			]]></script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
		</head>
			<xsl:call-template name="new_css"/>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="group_member_list">
			<xsl:with-param name="lab_select_usr">請選擇一個用戶</xsl:with-param>
			<xsl:with-param name="lab_no_usr">很抱歉，找不到用戶！</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_search_result">查詢結果</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="group_member_list">
			<xsl:with-param name="lab_select_usr">请选择一个用户</xsl:with-param>
			<xsl:with-param name="lab_no_usr">很抱歉，找不到用户！</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_search_result">查询结果</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="group_member_list">
			<xsl:with-param name="lab_select_usr">Please select users</xsl:with-param>
			<xsl:with-param name="lab_no_usr">No users found</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">close</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_search_result">Search result</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!--====================================================-->
	<xsl:template match="group_member_list">
		<xsl:param name="lab_select_usr"/>
		<xsl:param name="lab_no_usr"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_search_result"/>
		
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frmXml">
				<xsl:choose>
					<xsl:when test="count(user) = 0 ">
						<xsl:call-template name="wb_ui_title">
							<xsl:with-param name="text_class" >pop-up-title</xsl:with-param>
							<xsl:with-param name="text" select="$lab_search_result"/>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_no_usr"/>
						</xsl:call-template>
						<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
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
							<xsl:with-param name="text_class" >pop-up-title</xsl:with-param>
							<xsl:with-param name="text" select="$lab_select_usr"/>
						</xsl:call-template>					
						<table width="{$wb_gen_table_width}" cellpadding="0" cellspacing="0" border="0" class="wzb-ui-table">
							<tr class="SecBg wzb-ui-table-head">
								<TD align="left">
										<script type="text/javascript" language="JavaScript"><![CDATA[
									if(max_select == '1'){
										document.write('<span style="display:none;">')
									}]]></script>
										<xsl:call-template name="select_all_checkbox">
											<xsl:with-param name="chkbox_lst_cnt"><xsl:value-of select="count(user)"/></xsl:with-param>
											<xsl:with-param name="display_icon">false</xsl:with-param>
											<xsl:with-param name="sel_all_chkbox_nm">sel_all_checkbox</xsl:with-param>
										</xsl:call-template>
										<script type="text/javascript" language="JavaScript"><![CDATA[
									if(max_select == '1'){
										document.write('</span>')
									}]]></script>
									<span class="TitleText">
										<xsl:value-of select="$lab_login_id"/>
									</span>
								</TD>								
								<TD align="left">
									<a href="javascript:goldenman.openitemacc('{$order_by}')" class="TitleText">
										<xsl:value-of select="$lab_dis_name"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
											<xsl:with-param name="sort_order"><xsl:value-of select="$cur_order"/></xsl:with-param>
										</xsl:call-template>
									</a>
								</TD>
								<TD align="right">
									<span class="TitleText">
										<xsl:value-of select="$lab_group"/>
									</span>
								</TD>
							</tr>
							<xsl:for-each select="user">
								<xsl:variable name="row_class">
								<xsl:choose>
									<xsl:when test="position() mod 2">RowsEven</xsl:when>
									<xsl:otherwise>RowsOdd</xsl:otherwise>
								</xsl:choose>								
								</xsl:variable>
									<tr valign="middle" class="{$row_class}">
										<td>
											<script language="JavaScript"><![CDATA[
										usr_]]><xsl:value-of select="@ent_id"/><![CDATA[_id = ]]><xsl:value-of select="@ent_id"/><![CDATA[
										usr_]]><xsl:value-of select="@ent_id"/><![CDATA[_type = ]]><xsl:value-of select="@ent_id"/><![CDATA[
										usr_]]><xsl:value-of select="@ent_id"/><![CDATA[_name = ']]><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="name/@display_name"/></xsl:with-param></xsl:call-template><![CDATA['
									]]></script>
											<script type="text/javascript" language="JavaScript">
										if(max_select != '1'){
											document.write('<input type="checkbox" name="usr" value="{@ent_id}"/>')
										}else{
											document.write('<input type="radio" name="usr" value="{@ent_id}"/>')
										}
										</script>
											<span class="Text"><xsl:value-of select="@id"/></span>
										</td>										
										<td align="left">
											<span class="Text">
												<xsl:value-of select="name/@display_name"/>
											</span>
										</td>
										<td align="right">
											<span class="Text">
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
											</span>
										</td>
									</tr>
							</xsl:for-each>
						</table>
						<xsl:call-template name="wb_ui_pagination">
							<xsl:with-param name="cur_page" select="$cur_page"/>
							<xsl:with-param name="page_size" select="$page_size"/>
							<xsl:with-param name="total" select="$total"/>
						</xsl:call-template>
						<div class="wzb-bar" style="margin-top:0px">
							<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
								<tr>
									<td align="center" valign="middle">
										<xsl:call-template name="wb_gen_form_button">
											<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
											<xsl:with-param name="wb_gen_btn_href">javascript:goldenman.pickitemacc(document.frmXml,max_select)</xsl:with-param>
										</xsl:call-template>
										<img border="0" width="1" src="{$wb_img_path}tp.gif"/>
										<xsl:call-template name="wb_gen_form_button">
											<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
											<xsl:with-param name="wb_gen_btn_href">javascript:window.close()</xsl:with-param>
										</xsl:call-template>
									</td>
								</tr>
							</table>
						</div>
					</xsl:otherwise>
				</xsl:choose>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>
