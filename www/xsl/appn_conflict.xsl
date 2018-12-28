<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<!-- cust-->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<xsl:variable name="conflicts" select="/applyeasy/application_conflict"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="applyeasy/application_conflict"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="application_conflict">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>			
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			app =  new wbApplication;
			]]></SCRIPT>
			<xsl:call-template name="new_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<script>$(function(){
			$(".wzb-ui-table tr:last-child td").css("borderBottom","none")
			
			$(".table > thead > tr > th, .table > tbody > tr > th, .table > tfoot > tr > th, .table > thead > tr > td, .table > tbody > tr > td, .table > tfoot > tr > td").css("borderTop","1px solid #ddd")
			})</script>
		
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0"  onload="javascript:document.frmXml.elements[1].focus();">
			<form name="frmXml">
				<input type="hidden" name="back_confirm" value=""/>																
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_app_conflict">報名衝突</xsl:with-param>
			<xsl:with-param name="lab_info_l">由於以下原因，</xsl:with-param>
			<xsl:with-param name="lab_info_r">的報名存在衝突！點確認以確認報名，或點下一個以跳過此報名：</xsl:with-param>
			<xsl:with-param name="lab_info_lrn">由於以下原因，您的報名存在衝突！請聯繫該課程的培訓管理員：</xsl:with-param>
			<xsl:with-param name="lab_app_conflict_1">學員在同一門課程的以下班級中有學習記錄，課程不允許重讀</xsl:with-param>
			<xsl:with-param name="lab_app_conflict_2">不是課程指定的目標學員</xsl:with-param>
			<xsl:with-param name="lab_app_conflict_3">沒有通過課程的先修條件</xsl:with-param>
			<xsl:with-param name="lab_app_conflict_5">日程表跟以下已報名課程的日程表有衝突</xsl:with-param>
			<xsl:with-param name="lab_ok">確認</xsl:with-param>
			<xsl:with-param name="lab_cancel">下一個</xsl:with-param>
			<xsl:with-param name="lab_or">或</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_app_conflict">报名冲突</xsl:with-param>
			<xsl:with-param name="lab_info_l">由于以下原因，</xsl:with-param>
			<xsl:with-param name="lab_info_r">的报名存在冲突！点确认以确认报名，或点下一个以跳过此报名：</xsl:with-param>
			<xsl:with-param name="lab_info_lrn">由于以下原因，您的报名存在冲突！请联系该课程的培训管理员：</xsl:with-param>
			<xsl:with-param name="lab_app_conflict_1">学员在同一门课程的一下班级中有学习记录，课程不允许重读</xsl:with-param>
			<xsl:with-param name="lab_app_conflict_2">不是课程指定的目标学员</xsl:with-param>
			<xsl:with-param name="lab_app_conflict_3">没有通过课程的先修条件</xsl:with-param>
			<xsl:with-param name="lab_app_conflict_5">日程表与以下已报名课程的日程表有冲突</xsl:with-param>
			<xsl:with-param name="lab_ok">确认</xsl:with-param>
			<xsl:with-param name="lab_cancel">下一个</xsl:with-param>
			<xsl:with-param name="lab_or">或</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_app_conflict">Application conflict</xsl:with-param>
			<xsl:with-param name="lab_info_l">Application cannot be added for </xsl:with-param>
			<xsl:with-param name="lab_info_r"> because of the following reason(s). Click Confirm to add the application, or click next to pass this application and add the next application.</xsl:with-param>
			<xsl:with-param name="lab_info_lrn">Your application cannot be added because of the following reason(s). Please contact the training administrator of this Learning Solution.</xsl:with-param>
			<xsl:with-param name="lab_app_conflict_1">Already finished the following learning solution and the course does not allow retake:</xsl:with-param>
			<xsl:with-param name="lab_app_conflict_2">Not one of the targeted learner</xsl:with-param>
			<xsl:with-param name="lab_app_conflict_3">Not fulfilling the course prerequisite:</xsl:with-param>
			<xsl:with-param name="lab_app_conflict_5">Course timetable crashed with that of another applied course:</xsl:with-param>
			<xsl:with-param name="lab_ok">Confirm</xsl:with-param>
			<xsl:with-param name="lab_cancel">Next</xsl:with-param>
			<xsl:with-param name="lab_or">or</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_app_conflict"/>
		<xsl:param name="lab_info_l"/>
		<xsl:param name="lab_info_r"/>
		<xsl:param name="lab_info_lrn"/>
		<xsl:param name="lab_ok"/>
		<xsl:param name="lab_cancel"/>
		<xsl:param name="lab_app_conflict_1"/>
		<xsl:param name="lab_app_conflict_2"/>
		<xsl:param name="lab_app_conflict_3"/>
		<xsl:param name="lab_app_conflict_5"/>
		<xsl:param name="lab_or"/>
		<xsl:choose>
			<xsl:when test="../learner">
				<table class="table wzb-ui-table">
				
					<tr>
						<td width="50">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td>
							<xsl:call-template name="wb_ui_title">
								<xsl:with-param name="text" select="$lab_app_conflict"/>
							</xsl:call-template>

							<table cellpadding="0" cellspacing="0" width="100%" border="0">
								<tr>
									<td width="1" >
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
									<td width="100%">
										<xsl:call-template name="detail">
											<xsl:with-param name="lab_info_l" select="$lab_info_l"/>
											<xsl:with-param name="lab_info_r" select="$lab_info_r"/>
											<xsl:with-param name="lab_info_lrn" select="$lab_info_lrn"/>
											<xsl:with-param name="lab_app_conflict_1" select="$lab_app_conflict_1"/>
											<xsl:with-param name="lab_app_conflict_2" select="$lab_app_conflict_2"/>
											<xsl:with-param name="lab_app_conflict_3" select="$lab_app_conflict_3"/>
											<xsl:with-param name="lab_app_conflict_5" select="$lab_app_conflict_5"/>
											<xsl:with-param name="lab_or" select="$lab_or"/>
										</xsl:call-template>
									</td>
									<td width="1" >
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
								</tr>
							</table>
						</td>
						<td width="50">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text" select="$lab_app_conflict"/>
				</xsl:call-template>
			
				<xsl:call-template name="detail">
					<xsl:with-param name="lab_info_l" select="$lab_info_l"/>
					<xsl:with-param name="lab_info_r" select="$lab_info_r"/>
					<xsl:with-param name="lab_info_lrn" select="$lab_info_lrn"/>
					<xsl:with-param name="lab_app_conflict_1" select="$lab_app_conflict_1"/>
					<xsl:with-param name="lab_app_conflict_2" select="$lab_app_conflict_2"/>
					<xsl:with-param name="lab_app_conflict_3" select="$lab_app_conflict_3"/>
					<xsl:with-param name="lab_app_conflict_5" select="$lab_app_conflict_5"/>
					<xsl:with-param name="lab_or" select="$lab_or"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:call-template name="wb_ui_space"></xsl:call-template>
		<table class="table wzb-ui-table">
			<TR>
				<TD align="right" width="50%">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_ok"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">
							<xsl:choose>
								<xsl:when test="../learner">Javascript:history.back();</xsl:when>
								<xsl:otherwise>Javascript:app.back_to_confirm(frmXml);</xsl:otherwise>
							</xsl:choose>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>
				</TD>
				<TD align="left" width="50%">
					<xsl:choose>
						<xsl:when test="../learner"></xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$lab_cancel"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:app.confirm_cancel(frmXml);</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</TD>
			</TR>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="detail">
		<xsl:param name="lab_info_l"/>
		<xsl:param name="lab_info_r"/>
		<xsl:param name="lab_info_lrn"/>
		<xsl:param name="lab_app_conflict_1"/>
		<xsl:param name="lab_app_conflict_2"/>
		<xsl:param name="lab_app_conflict_3"/>
		<xsl:param name="lab_app_conflict_5"/>
		<xsl:param name="lab_or"/>
		<table class="table wzb-ui-table">
		
			<tr>
				<td>
					<table align="left" width="90%" border="0" cellspacing="0" cellpadding="5">
						<tr>
							<td>
								<span class="Text">
									<xsl:choose>
										<xsl:when test="../learner"><xsl:value-of select="$lab_info_lrn"/></xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$lab_info_l" disable-output-escaping="yes"/><xsl:value-of select="@usr_display_bil"/><xsl:value-of 	select="$lab_info_r"/>
										</xsl:otherwise>
									</xsl:choose>
								</span>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<xsl:apply-templates select="$conflicts/conflict">
			<xsl:with-param name="lab_app_conflict_1" select="$lab_app_conflict_1"/>
			<xsl:with-param name="lab_app_conflict_2" select="$lab_app_conflict_2"/>
			<xsl:with-param name="lab_app_conflict_3" select="$lab_app_conflict_3"/>
			<xsl:with-param name="lab_app_conflict_5" select="$lab_app_conflict_5"/>
			<xsl:with-param name="lab_or" select="$lab_or"/>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="conflict">
		<xsl:param name="lab_app_conflict_1"/>
		<xsl:param name="lab_app_conflict_2"/>
		<xsl:param name="lab_app_conflict_3"/>
		<xsl:param name="lab_app_conflict_5"/>
		<xsl:param name="lab_or"/>
		<table >
			
				<xsl:if test="@id = 'CONF_1'">
					<tr>
						<td width="10">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td align="left">
							<span class="Text">
								<li>
									<xsl:value-of select="$lab_app_conflict_1"/>
								</li>
							</span>
						</td>
					</tr>
					<xsl:apply-templates select="retake_item"/>
				</xsl:if>
				<xsl:if test="@id = 'CONF_2'">
					<tr>
						<td width="10">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td align="left">
							<span class="Text">
								<li>
									<xsl:value-of select="$lab_app_conflict_2"/>
								</li>
							</span>
						</td>
					</tr>
					<xsl:apply-templates select="conflict_item"/>
				</xsl:if>
				<xsl:if test="@id = 'CONF_3'">
					<tr>
						<td width="10">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td align="left">
							<span class="Text">
								<li>
									<xsl:value-of select="$lab_app_conflict_3"/>
								</li>
							</span>
						</td>
					</tr>
					<xsl:apply-templates select="require_item">
						<xsl:with-param name="lab_or" select="$lab_or" />
					</xsl:apply-templates>
				</xsl:if>
				<xsl:if test="@id = 'CONF_5'">
					<tr>
						<td width="10">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td align="left">
							<span class="Text">
								<li>
									<xsl:value-of select="$lab_app_conflict_5"/>
								</li>
							</span>
						</td>
					</tr>
					<xsl:apply-templates select="conflict_item"/>
				</xsl:if>
			
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="require_item">
		<xsl:param name="lab_or" />
		<xsl:variable name="itm_cnt" select="count(rule_details/item)"/>
		<tr>
			<td width="10">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td align="left">
				<img src="{$wb_img_path}tp.gif" width="30" height="1" border="0"/>
				<span class="Text">
					<xsl:text>(</xsl:text>
					<xsl:for-each select="rule_details/item">
						<xsl:value-of select="@display_bil"/>
						<xsl:if test="position() &lt; $itm_cnt">
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$lab_or"/>
							<xsl:text>&#160;</xsl:text>
						</xsl:if>
					</xsl:for-each>
					<xsl:text>)</xsl:text>
				</span>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="conflict_item">
		<tr>
			<td width="10">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td align="left">
				<img src="{$wb_img_path}tp.gif" width="30" height="1" border="0"/>
				<span class="Text">
					<xsl:text>(</xsl:text>
					<xsl:value-of select="@itm_code"/>
					<xsl:text>-</xsl:text>
					<xsl:value-of select="@run_code"/>
					<xsl:text>)</xsl:text>
				</span>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="retake_item">
		<tr>
			<td width="10">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td align="left">
				<img src="{$wb_img_path}tp.gif" width="30" height="1" border="0"/>
				<span class="Text">
					<xsl:text>(</xsl:text>
					<xsl:value-of select="@itm_code"/>
					<xsl:text>)</xsl:text>
				</span>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
