<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>	
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/display_score.xsl"/>	
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="share/wb_module_status_const.xsl"/>
	<xsl:output indent="yes"/>
	
	<xsl:template name="DOCTYPE">
		<![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"/>]]>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="/report">
		<xsl:value-of select="document('')/*/xsl:template[@name='DOCTYPE']/node()" disable-output-escaping="yes"/> 
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<link rel="stylesheet" href="../static/css/three.css"/>
			<link rel="stylesheet" href="../static/js/font-awesome/css/font-awesome.min.css"/>
			
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_report.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			var rpt = new wbReport;
			function select_type(frm){
				select_val = frm.sel_frm.options[frm.sel_frm.selectedIndex].value
				
				if (select_val == 'user')
					rpt.cos_lrn_lst();
				else
					rpt.usage_mod_lst();
			}
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmSearch" onSubmit="return status()">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_time_spent">訪問總時間</xsl:with-param>
			<xsl:with-param name="lab_last_access">上次訪問時間</xsl:with-param>
			<xsl:with-param name="lab_topic">標題</xsl:with-param>
			<xsl:with-param name="lab_score">成績</xsl:with-param>
			<xsl:with-param name="lab_no_rpt">還沒有任何報告</xsl:with-param>
			<xsl:with-param name="lab_item_type">請選擇類型:</xsl:with-param>
			<xsl:with-param name="lab_user_report">用戶報告</xsl:with-param>
			<xsl:with-param name="lab_usage_report">使用報告</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">跟蹤報告</xsl:with-param>
			<xsl:with-param name="lab_hits">訪問次數</xsl:with-param>
			<xsl:with-param name="lab_score_percent">模塊成績</xsl:with-param>
			<xsl:with-param name="lab_mod_info">模塊資訊</xsl:with-param>
			<xsl:with-param name="lab_obj_info">目標資訊</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_time_spent">访问总时长</xsl:with-param>
			<xsl:with-param name="lab_last_access">上次访问时间</xsl:with-param>
			<xsl:with-param name="lab_topic">标题</xsl:with-param>
			<xsl:with-param name="lab_score">成绩</xsl:with-param>
			<xsl:with-param name="lab_no_rpt">还没有任何报告</xsl:with-param>
			<xsl:with-param name="lab_item_type">请选择类型</xsl:with-param>
			<xsl:with-param name="lab_user_report">用户报告</xsl:with-param>
			<xsl:with-param name="lab_usage_report">使用报告</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">跟踪报告</xsl:with-param>
			<xsl:with-param name="lab_hits">访问次数</xsl:with-param>
			<xsl:with-param name="lab_score_percent">模块成绩</xsl:with-param>
			<xsl:with-param name="lab_mod_info">模块信息</xsl:with-param>
			<xsl:with-param name="lab_obj_info">目标信息</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_time_spent">Time spent</xsl:with-param>
			<xsl:with-param name="lab_last_access">Last accessed</xsl:with-param>
			<xsl:with-param name="lab_topic">Topic</xsl:with-param>
			<xsl:with-param name="lab_score">Score</xsl:with-param>
			<xsl:with-param name="lab_no_rpt">No progress result found</xsl:with-param>
			<xsl:with-param name="lab_item_type">Please select type :</xsl:with-param>
			<xsl:with-param name="lab_user_report">User report</xsl:with-param>
			<xsl:with-param name="lab_usage_report">Usage report</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">Tracking report</xsl:with-param>
			<xsl:with-param name="lab_hits">Hits</xsl:with-param>
			<xsl:with-param name="lab_score_percent">Overall score</xsl:with-param>
			<xsl:with-param name="lab_mod_info">Module information</xsl:with-param>
			<xsl:with-param name="lab_obj_info">Objective information</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_time_spent"/>
		<xsl:param name="lab_last_access"/>
		<xsl:param name="lab_topic"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_no_rpt"/>
		<xsl:param name="lab_item_type"/>
		<xsl:param name="lab_user_report"/>
		<xsl:param name="lab_usage_report"/>
		<xsl:param name="lab_tracking_report"/>
		<xsl:param name="lab_hits"/>
		<xsl:param name="lab_score_percent"/>
		<xsl:param name="lab_mod_info"/>
		<xsl:param name="lab_obj_info"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text"><xsl:value-of select="$lab_tracking_report"/>&#160;-&#160;<xsl:value-of select="aicc_au_report/course/module/@title"/></xsl:with-param>
			<xsl:with-param name="new_template">true</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text"><img src="{$wb_img_path}user.gif" border="0" align="absmiddle"/><xsl:text>&#160;</xsl:text><xsl:value-of select="aicc_au_report/learner/@name"/></xsl:with-param>
			<xsl:with-param name="new_template">true</xsl:with-param>
		</xsl:call-template>		
		<xsl:choose>
			<xsl:when test="aicc_au_report/course/module">
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text"/>
					<xsl:with-param name="new_template">true</xsl:with-param>
				</xsl:call-template>
				<div class="report_info clean_margin" style="height:150px;">
				<!--  1-->
					<div class="clearfix">
						<div class="left_div_width">
							<span class="grayC999">
								<xsl:value-of select="$lab_status"/>
								<xsl:text>：</xsl:text>
							</span>
						</div>
						<div class="right_div_width">
							<span>
								<xsl:if test="aicc_au_report/course/module">
									<xsl:call-template name="display_progress_tracking">
										<xsl:with-param name="status"><xsl:value-of select="aicc_au_report/course/module/@status"/></xsl:with-param>
										<xsl:with-param name="mod_type">AICC_AU</xsl:with-param>
										<xsl:with-param name="type">module</xsl:with-param>
										<xsl:with-param name="show_text">true</xsl:with-param>
									</xsl:call-template>
								</xsl:if>
							</span>
						</div>
					</div>
		<!-- 2 -->
					<div class="clearfix">
						<div class="left_div_width">
							<span class="grayC999">
								<xsl:value-of select="$lab_hits"/>
								<xsl:text>：</xsl:text>
							</span>
						</div>
						<div class="right_div_width">
							<span>
								<xsl:if test="aicc_au_report/course/module/@hits = ''">--</xsl:if>
								<xsl:value-of select="aicc_au_report/course/module/@hits"/>
							</span>
						</div>
					</div>
		<!--  3-->
					<div class="clearfix">
						<div class="left_div_width">
							<span class="grayC999">
								<xsl:value-of select="$lab_time_spent"/>
								<xsl:text>：</xsl:text>
							</span>
						</div>
						<div class="right_div_width">
							<span>
								<xsl:if test="aicc_au_report/course/module/@total_time = ''">--</xsl:if>
								<xsl:value-of select="aicc_au_report/course/module/@total_time"/>
							</span>
						</div>
					</div>
		<!--4  -->
					<div class="clearfix">
						<div class="left_div_width">
							<span class="grayC999">
								<xsl:value-of select="$lab_last_access"/>
								<xsl:text>：</xsl:text>
							</span>
						</div>
						<div class="right_div_width">
							<span>
								<xsl:if test="aicc_au_report/course/module/@last_access = ''">--</xsl:if>
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp"><xsl:value-of select="aicc_au_report/course/module/@last_access"/></xsl:with-param>
									<xsl:with-param name="dis_time">T</xsl:with-param>
									</xsl:call-template>								
							</span>
						</div>
					</div>
	<!--  5-->
					<div class="clearfix">
						<div class="left_div_width">
							<span class="grayC999">
								<xsl:value-of select="$lab_score_percent"/>
								<xsl:text>：</xsl:text>
							</span>
						</div>
						<div class="right_div_width">
							<span>
								<xsl:if test="aicc_au_report/course/module/@score = ''">--</xsl:if>
								<xsl:call-template name="display_score">
									<xsl:with-param name="score"><xsl:value-of select="aicc_au_report/course/module/@score"/></xsl:with-param>
								</xsl:call-template>
							</span>
						</div>
					</div>
				</div>

				<img src="{$wb_img_path}tp.gif" width="1" height="20" border="0"/>
				<xsl:choose>
					<xsl:when test="count(aicc_au_report/course/objective)=0">
						
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text"/>
							<xsl:with-param name="new_template">true</xsl:with-param>
						</xsl:call-template>
						<div class="content_info">
							<table cellpadding="3" cellspacing="0" border="0" width="100%">
								<tr class="report_title">
									<td width="3%">
										<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
									</td>
									<td width="30%">
										<span class="grayC999">
											<xsl:value-of select="$lab_topic"/>
										</span>
									</td>
									<td align="center" width="30%">
										<span class="grayC999">
											<xsl:value-of select="$lab_status"/>
										</span>
									</td>
									<td align="center" width="30%">
										<span class="grayC999">
											<xsl:value-of select="$lab_score"/>
										</span>
									</td>
									<td width="3%">
										<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
									</td>
								</tr>
								<xsl:apply-templates select="aicc_au_report/course/objective"/>
							</table>
						</div>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_rpt"/>
					<xsl:with-param name="new_template">true</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<div class="report_botton">
			<a href="javascript:;" onclick="javascript:mobile_close(self,'{aicc_au_report/learner/@tkh_id}')">
				<xsl:value-of select="$lab_g_form_btn_close"/>
			</a>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="aicc_au_report/course/objective">
		<tr class="report_content_tr">
			<td>
				<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
			</td>
			<td>
				<span class="Text">
					<xsl:value-of select="@title"/>
				</span>
			</td>
			<td align="center">
				<span class="Text">
					<xsl:choose>
						<xsl:when test="@status='' or not(@status)">
							<xsl:text>--</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="display_progress_tracking">
								<xsl:with-param name="status"><xsl:value-of select="@status"/></xsl:with-param>
								<xsl:with-param name="mod_type">AICC_AU</xsl:with-param>
								<xsl:with-param name="type">module</xsl:with-param>
								<xsl:with-param name="show_text">true</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</td>
			<td align="center">
				<span class="Text">
					<xsl:choose>
						<xsl:when test="@score='' or not(@score)">
							<xsl:text>--</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="format-number(@score,'###.#')"/>
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</td>
			<td>
				<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
			</td>			
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
