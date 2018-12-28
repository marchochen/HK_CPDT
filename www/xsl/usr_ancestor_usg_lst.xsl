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
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<!-- usr utils -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:apply-templates select="//ancestor_node_list"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="ancestor_node_list">
		<html>
			<head>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
				<script language="JavaScript" src="{$wb_js_path}wb_usergroup.js"/>
				<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js"/>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				
				<link rel="stylesheet" href="../static/css/base.css" />
				
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> 
				<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
				<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
				<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
				
				<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
				<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
				
				<xsl:call-template name="wb_css">
					<xsl:with-param name="view">wb_ui</xsl:with-param>
				</xsl:call-template>
				
 
				
				<script language="Javascript">
				usr = new wbUserGroup;

			</script>
			<xsl:call-template name="new_css" />
			</head>
			<BODY marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
				<form onsubmit="return status()" name="frmXml">
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</BODY>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_grp_name">小組名稱</xsl:with-param>
			<xsl:with-param name="lab_group">小組</xsl:with-param>
			<xsl:with-param name="lab_not_match">很抱歉，找不到符合您檢索條件的用戶！</xsl:with-param>
			<xsl:with-param name="lab_search_result">檢索結果</xsl:with-param>
			<xsl:with-param name="lab_sel_grp">請選擇用戶組</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_select">選擇</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_sel_grp_desc">以下列表由高至低順序顯示了該用戶所在的全部用戶組，請從中選擇最高層的報名審批用戶組。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_grp_name">小组名称</xsl:with-param>
			<xsl:with-param name="lab_group">小组</xsl:with-param>
			<xsl:with-param name="lab_not_match">很抱歉，找不到符合您搜索条件的用户！</xsl:with-param>
			<xsl:with-param name="lab_search_result">搜索结果</xsl:with-param>
			<xsl:with-param name="lab_sel_grp">请选择用户组</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_select">选择</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_sel_grp_desc">以下列表按照从高至低的顺序显示了该用户所在的全部用户组，请从中选择最高层的报名审批用户组。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_grp_name">Group name</xsl:with-param>
			<xsl:with-param name="lab_group">Group</xsl:with-param>
			<xsl:with-param name="lab_not_match">No results found</xsl:with-param>
			<xsl:with-param name="lab_search_result">Search result</xsl:with-param>
			<xsl:with-param name="lab_sel_grp">Please select group</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_select">Select</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_sel_grp_desc">Please select the highest approval group from the list below. The list shows the groups that this user belongs, from the highest to the lowest level.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_grp_name"/>
		<xsl:param name="lab_group"/>
		<xsl:param name="lab_not_match"/>
		<xsl:param name="lab_search_result"/>
		<xsl:param name="lab_sel_grp"/>
		<xsl:param name="lab_sel_grp_desc"/>
		<xsl:param name="lab_g_form_btn_select"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:call-template name="wb_ui_title">
				
			<xsl:with-param name="text_class" >pop-up-title padding05</xsl:with-param>
			<xsl:with-param name="text" select="$lab_sel_grp"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_sel_grp_desc"/>
		</xsl:call-template>
		<input type="hidden" name="lab_group" value="{$lab_group}"/>
		<xsl:choose>
			<xsl:when test="count(node) &gt;= 1">
				<table width="{$wb_gen_table_width}" border="0" cellpadding="3" cellspacing="0">
					<tr class="SecBg">
						<td width="98%" style="padding:0 8px;">
							<span class="TitleText">
								<xsl:value-of select="$lab_grp_name"/>
							</span>
						</td>
					</tr>
				
				<xsl:apply-templates select="node"/>
				</table>
				<!-- <xsl:call-template name="wb_ui_line"/> -->
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_not_match"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<table width="{$wb_gen_table_width}" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center">
					<div class="wzb-bar">
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_select"/>
			   	 			<xsl:with-param name="style">line-height:normal</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:usr.user.usr_ancestor_usg_lst_add(document.frmXml)</xsl:with-param>
						</xsl:call-template>
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_close"/>
							<xsl:with-param name="style">line-height:normal</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:window.close()</xsl:with-param>
						</xsl:call-template>
					</div>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="node">
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2">RowsEven</xsl:when>
				<xsl:otherwise>RowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr class="{$row_class}">
			<td width="98%" valign="middle" class="wzb-form-control" style="padding: 0 10px;">
				<span style="margin-left:-2px">
					<input type="radio" name="usg_id" value="{@id}" id="rdo_{@id}"/>
					<input type="hidden" name="usg_id_{@id}" value="{title}"/>
				</span>
				<span class="Text">
					<label for="rdo_{@id}"><xsl:value-of select="title"/></label>
				</span>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="cur_usr"/>
	<!-- =============================================================== -->
</xsl:stylesheet>
