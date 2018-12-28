<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="user_count" select="count(/user_postion/user_list/user)"/>
	<xsl:variable name="del_upt" select="/user_postion/del_upt"/>
	<xsl:variable name="upt_code_list" select="/user_postion/upt_code_list"/>
	<xsl:variable name="lab_usr_position" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '949')"/>
	
	<!-- ===================================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- ===================================================================== -->
	<xsl:template match="user_postion">
		<head>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_ugr.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_userposition.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				ugr = new wbUgr;
				upt = new wbUserPosition;
				
			]]></SCRIPT>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="wb_css">	<xsl:with-param name="view">wb_ui</xsl:with-param></xsl:call-template>
		</head>
		<BODY marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<FORM name="frmXml">
				<input type="hidden" name="module"/>
				<input type="hidden" name="cmd"/>	
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="upt_code_list" value="{$upt_code_list}"/>
				<xsl:call-template name="wb_init_lab"/>
			</FORM>
		</BODY>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
		<xsl:with-param name="lab_nav_title">刪除<xsl:value-of select="$lab_usr_position"/></xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_1"><xsl:value-of select="$lab_usr_position"/>“<b><xsl:value-of select="$del_upt"/></b>”正在使用。下面列出被影響的用戶（由於<xsl:value-of select="$lab_usr_position"/>的設置）。</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_2">如果點擊<b>確定</b>，包含將會刪除所有被影響的用戶的<xsl:value-of select="$lab_usr_position"/>設置，會被改成<i>Unspecified</i>。請在確定刪除之前檢視被影響的地方。</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_3">如果點擊<b>列印</b>，您可以把在下面顯示的被影響課程及用戶列印出來，進行跟進。</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_4"><xsl:value-of select="$lab_usr_position"/>“<b><xsl:value-of select="$del_upt"/></b>”將被刪除，是否確定？</xsl:with-param>
		<xsl:with-param name="lab_affected_lrn_soln">被影響課程/班別</xsl:with-param>
		<xsl:with-param name="lab_no_affected_lrn_soln">-- 沒有被影響的課程/班別 --</xsl:with-param>
		<xsl:with-param name="lab_affected_users">被影響用戶</xsl:with-param>
		<xsl:with-param name="lab_no_affected_users">-- 沒有被影響的用戶 --</xsl:with-param>
		<xsl:with-param name="lab_code">編號</xsl:with-param>
		<xsl:with-param name="lab_title">名稱</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_print">列印</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_confirm">確定</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
		<xsl:with-param name="lab_nav_title">删除<xsl:value-of select="$lab_usr_position"/></xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_1"><xsl:value-of select="$lab_usr_position"/>“<b><xsl:value-of select="$del_upt"/></b>”正在使用。下面列出被影响的用户（由于<xsl:value-of select="$lab_usr_position"/>的设置）。</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_2">如果点击<b>确定</b>，包含将会删除所有被影响的用户的<xsl:value-of select="$lab_usr_position"/>设置。请在确定删除之前检视被影响的地方。</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_3">如果点击<b>打印</b>，您可以把在下面显示的被影响课程及用户打印出来，进行跟进。</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_4"><xsl:value-of select="$lab_usr_position"/>“<b><xsl:value-of select="$del_upt"/></b>”将被删除，是否确定？</xsl:with-param>
		<xsl:with-param name="lab_affected_lrn_soln">被影响课程/班级</xsl:with-param>
		<xsl:with-param name="lab_no_affected_lrn_soln">-- 没有被影响的课程/班级 --</xsl:with-param>
		<xsl:with-param name="lab_affected_users">被影响用户</xsl:with-param>
		<xsl:with-param name="lab_no_affected_users">-- 没有被影响的用户 --</xsl:with-param>
		<xsl:with-param name="lab_code">编号</xsl:with-param>
		<xsl:with-param name="lab_title">名称</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_print">打印</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_confirm">确定</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
		<xsl:with-param name="lab_nav_title">Remove grade</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_1"><xsl:value-of select="$lab_usr_position"/> "<b><xsl:value-of select="$del_upt"/></b>" is already in use. Listed below are the affected users (because of <xsl:value-of select="$lab_usr_position"/> settings).</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_2">By clicking <b>Confirm</b>, the target learner/target enrollments settings related to the <xsl:value-of select="$lab_usr_position"/> settings of all affected users. Please review the impact before confirming the removal.</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_3">By clicking <b>Print</b>, you can print the list of affected learning solutions and users shown on the screen for follow-up actions.</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_4"><xsl:value-of select="$lab_usr_position"/> "<b><xsl:value-of select="$del_upt"/></b>" is to be removed, are you sure?</xsl:with-param>
		<xsl:with-param name="lab_affected_lrn_soln">Affected learning solutions/class</xsl:with-param>
		<xsl:with-param name="lab_no_affected_lrn_soln">-- No affected learning solutions/Class found --</xsl:with-param>
		<xsl:with-param name="lab_affected_users">Affected users</xsl:with-param>
		<xsl:with-param name="lab_no_affected_users">-- No affected users found --</xsl:with-param>
		<xsl:with-param name="lab_code">Code</xsl:with-param>
		<xsl:with-param name="lab_title">Title</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_print">Print</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_confirm">Confirm</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_nav_title"/>
		<xsl:param name="lab_instruction_desc_1"/>
		<xsl:param name="lab_instruction_desc_2"/>
		<xsl:param name="lab_instruction_desc_3"/>
		<xsl:param name="lab_instruction_desc_4"/>
		<xsl:param name="lab_affected_lrn_soln"/>
		<xsl:param name="lab_no_affected_lrn_soln"/>
		<xsl:param name="lab_affected_users"/>
		<xsl:param name="lab_no_affected_users"/>
		<xsl:param name="lab_code"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_g_form_btn_print"/>
		<xsl:param name="lab_g_form_btn_confirm"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<table border="0" cellspacing="0" cellpadding="10" width="{$wb_gen_table_width}">
			<tr><td colspan="2">
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text"><xsl:value-of select="$lab_nav_title"/></xsl:with-param>
			</xsl:call-template>
			</td></tr>
			<tr>
				<td align="right" width="10">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td>
					<span class="Text">
						<p><xsl:copy-of select="$lab_instruction_desc_1"/></p>
						<p><xsl:copy-of select="$lab_instruction_desc_2"/></p>
					</span>
					
					<table cellspacing="0" width="100%" border="0" cellpadding="0">
						<tr>
							<td valign="bottom">
								<span class="Head"><xsl:value-of select="$lab_affected_users"/></span>
							</td>
						</tr>
						<tr class="Line">
							<td height="1"><img width="1" src="{$wb_img_path}tp.gif" height="1" border="0"/></td>
						</tr>
					</table>
					
					<table cellspacing="0" width="100%" border="0" cellpadding="0">
						<xsl:choose>
							<xsl:when test="$user_count=0">
								<tr>
									<td align="center" class="Text" colspan="3">
										<xsl:call-template name="wb_ui_show_no_item">
											<xsl:with-param name="text" select="$lab_no_affected_users" />
										</xsl:call-template>
									</td>
								</tr>
							</xsl:when>
							<xsl:otherwise>
								<tr>
									<td class="TitleTextBold" width="20%" align="left"><xsl:value-of select="$lab_login_id"/></td>
									<td class="TitleTextBold" width="25%" align="left"><xsl:value-of select="$lab_dis_name"/></td>
									<td class="TitleTextBold" width="55%" align="left"><xsl:value-of select="$lab_group"/></td>
								</tr>
								<xsl:for-each select="user_list/user">
									<tr>
										<td class="Text"><xsl:value-of select="@usr_ste_usr_id"/></td>
										<td class="Text"><xsl:value-of select="@usr_display_bil"/></td>
										<td class="Text"><xsl:value-of select="@user_group"/></td>
									</tr>
								</xsl:for-each>
							</xsl:otherwise>
						</xsl:choose>
					</table>

				</td>
			</tr>
		</table>
		<table cellpadding="3" cellspacing="0" width="{$wb_gen_table_width}" height="19">
			<tr>
				<td align="center" >
					<input type="hidden" name="confirm_del_upt" value="{$lab_instruction_desc_4}"/>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_confirm"/>	
						<xsl:with-param name="wb_gen_btn_href">javascript:upt.del(document.frmXml)</xsl:with-param>
					</xsl:call-template>
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:upt.search(document.frmXml)</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="env"/>
	<xsl:template match="cur_usr"/>
</xsl:stylesheet>
