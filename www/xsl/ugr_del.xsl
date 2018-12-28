<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>

	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="wb_gen_table_width" select="$wb_frame_table_width"/>
	<xsl:variable name="grade"><xsl:value-of select="/tree/item/@title"/></xsl:variable>
	
	<xsl:variable name="ugr_timestamp" select="/tree/item/@timestamp"/>
	
	<xsl:variable name="lrn_soln_count"><xsl:value-of select="count(/tree/affected_lrn_soln_list/item)"/></xsl:variable>
	<xsl:variable name="user_count"><xsl:value-of select="count(/tree/affected_user_list/user)"/></xsl:variable>
	
	<!-- ===================================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- ===================================================================== -->
	<xsl:template match="tree">
		<head>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_ugr.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				ugr = new wbUgr
			
				function cancel() {
					ugr_ent_id = getUrlParam('ugr_ent_id');
					window.location = ugr.ugr_url(ugr_ent_id);
				}
			]]></SCRIPT>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="new_css"/>
		</head>
		<BODY marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<FORM name="frmXml">
				<input type="hidden" name="ugr_timestamp" value="{$ugr_timestamp}"/>
				<xsl:call-template name="wb_init_lab"/>
			</FORM>
		</BODY>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
		<xsl:with-param name="lab_nav_title">刪除<xsl:value-of select="$lab_grade"/></xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_1"><xsl:value-of select="$lab_grade"/>“<b><xsl:value-of select="$grade"/></b>”正在使用。下面列出被影響的課程（由於目標學員的設置）及用戶（由於<xsl:value-of select="$lab_grade"/>的設置）。</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_2">如果點擊<b>確定</b>，包含將被刪除的<xsl:value-of select="$lab_grade"/>的目標學員設置，會從所有被影響的課程/班別中移走；而所有被影響的用戶的<xsl:value-of select="$lab_grade"/>設置，會被改成<i>Unspecified</i>。請在確定刪除之前檢視被影響的地方。</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_3">如果點擊<b>列印</b>，您可以把在下面顯示的被影響課程及用戶列印出來，進行跟進。</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_4"><xsl:value-of select="$lab_grade"/>“<b><xsl:value-of select="$grade"/></b>”將被刪除，是否確定？</xsl:with-param>
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
		<xsl:with-param name="lab_nav_title">删除<xsl:value-of select="$lab_grade"/></xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_1"><xsl:value-of select="$lab_grade"/>“<b><xsl:value-of select="$grade"/></b>”正在使用。下面列出被影响的课程（由于目标学员）及用户（由于<xsl:value-of select="$lab_grade"/>的设置）。</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_2">如果点击<b>确定</b>，包含将被删除的<xsl:value-of select="$lab_grade"/>的目标学员设置，会从所有被影响的课程/班级中移走；而所有被影响的用户的<xsl:value-of select="$lab_grade"/>设置，会被改成<i>Unspecified</i>。请在确定删除之前检视被影响的地方。</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_3">如果点击<b>打印</b>，您可以把在下面显示的被影响课程及用户打印出来，进行跟进。</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_4"><xsl:value-of select="$lab_grade"/>“<b><xsl:value-of select="$grade"/></b>”将被删除，是否确定？</xsl:with-param>
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
		<xsl:with-param name="lab_instruction_desc_1"><xsl:value-of select="$lab_grade"/> "<b><xsl:value-of select="$grade"/></b>" is already in use. Listed below are the affected learning solutions (because of target learner settings) and users (because of <xsl:value-of select="$lab_grade"/> settings).</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_2">By clicking <b>Confirm</b>, the target learner settings related to the to-be-removed <xsl:value-of select="$lab_grade"/> will be removed for all affected learning solutions/class; the <xsl:value-of select="$lab_grade"/> settings of all affected users will be set to <i>Unspecified</i>. Please review the impact before confirming the removal.</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_3">By clicking <b>Print</b>, you can print the list of affected learning solutions and users shown on the screen for follow-up actions.</xsl:with-param>
		<xsl:with-param name="lab_instruction_desc_4"><xsl:value-of select="$lab_grade"/> "<b><xsl:value-of select="$grade"/></b>" is to be removed, are you sure?</xsl:with-param>
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
		<table>
			<tr>
				<td colspan="2">					
					<div style="padding:0;" align="left" class="wzb-title-11 wzb-module-title"><xsl:value-of select="$lab_nav_title" /></div>
				</td>
			</tr>
			<tr>
				<td align="right" width="10"></td>
				<td>
					<xsl:choose>
						<xsl:when test="$lrn_soln_count=0 and $user_count=0">
							<p style="text-align: center; margin-top: 30px;">
								<xsl:copy-of select="$lab_instruction_desc_4" />
							</p>
						</xsl:when>
						<xsl:otherwise>
							<p>
								<xsl:copy-of select="$lab_instruction_desc_1" />
							</p>
							<p>
								<xsl:copy-of select="$lab_instruction_desc_2" />
							</p>
							<p>
								<xsl:copy-of select="$lab_instruction_desc_3" />
							</p>
						</xsl:otherwise>
					</xsl:choose>
					
					<xsl:if test="$lrn_soln_count>0 or $user_count>0">
			
						<table cellspacing="0" width="480" border="0" cellpadding="0">
							<tr>
								<td valign="bottom">
									<span class="Head">
										<xsl:value-of select="$lab_affected_lrn_soln" />
									</span>
								</td>
							</tr>
							<tr class="Line">
								<td height="1"></td>
							</tr>
						</table>
			
						<table>
							<xsl:choose>
								<xsl:when test="$lrn_soln_count=0">
									<tr>
										<td align="center" class="Text" colspan="2">
											<xsl:value-of select="$lab_no_affected_lrn_soln" />
										</td>
									</tr>
								</xsl:when>
								<xsl:otherwise>
									<tr>
										<td class="TitleTextBold" width="25%" align="left">
											<xsl:value-of select="$lab_code" />
										</td>
										<td class="TitleTextBold" width="75%" align="left">
											<xsl:value-of select="$lab_title" />
										</td>
									</tr>
									<xsl:for-each select="affected_lrn_soln_list/item">
										<tr>
											<td class="Text">
												<xsl:value-of select="@itm_code" />
											</td>
											<td class="Text">
												<xsl:value-of select="@itm_title" />
											</td>
										</tr>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</table>
			
						<br />
			
						<table>
							<tr>
								<td valign="bottom">
									<xsl:value-of select="$lab_affected_users" />
								</td>
							</tr>
							<tr class="Line">
								<td height="1"></td>
							</tr>
						</table>
			
						<table>
							<xsl:choose>
								<xsl:when test="$user_count=0">
									<tr>
										<td align="center" class="Text" colspan="3">
											<xsl:value-of select="$lab_no_affected_users" />
										</td>
									</tr>
								</xsl:when>
								<xsl:otherwise>
									<tr>
										<td class="TitleTextBold" width="20%" align="left">
											<xsl:value-of select="$lab_login_id" />
										</td>
										<td class="TitleTextBold" width="25%" align="left">
											<xsl:value-of select="$lab_dis_name" />
										</td>
										<td class="TitleTextBold" width="55%" align="left">
											<xsl:value-of select="$lab_group" />
										</td>
									</tr>
									<xsl:for-each select="affected_user_list/user">
										<tr>
											<td class="Text">
												<xsl:value-of select="@usr_ste_usr_id" />
											</td>
											<td class="Text">
												<xsl:value-of select="@usr_display_bil" />
											</td>
											<td class="Text">
												<xsl:value-of select="@user_group" />
											</td>
										</tr>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</table>
					</xsl:if>
			
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:if test="$lrn_soln_count>0 or $user_count>0">
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_print"/>										<xsl:with-param name="wb_gen_btn_href">javascript:window.print()</xsl:with-param>
				</xsl:call-template>
			</xsl:if>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_confirm"/>									<xsl:with-param name="wb_gen_btn_href">javascript:ugr.ugr_del_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:cancel()</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="env"/>
	<xsl:template match="cur_usr"/>
</xsl:stylesheet>
