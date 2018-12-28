<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" >
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/change_lowercase.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>       
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="share/wb_module_status_const.xsl"/>
	<xsl:import href="utils/display_score.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl" />
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="tkh_id" select="/report/course/module_list/@tkh_id"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->	
	<xsl:template match="/report/course/module_list/module" mode="jump">
		<xsl:choose>
			<xsl:when test="@mod_vendor = 'SkillSoft'">
				<meta http-equiv="refresh" content="1;url=javascript:rpt.lrn_skillsoft_lst({/report/course/@id},{@id},'{/report/learner/@ent_id}','{$tkh_id}')"/>
			</xsl:when>
			<xsl:when test="not(@mod_vendor = 'SkillSoft')">
				<meta http-equiv="refresh" content="1;url=javascript:rpt.aicc_rpt('{/report/course/@id}','{@id}','{/report/learner/@ent_id}',false,'{$tkh_id}')"/>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_name">標題</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_hits">訪問次數</xsl:with-param>
			<xsl:with-param name="lab_total_time">訪問總時長</xsl:with-param>
			<xsl:with-param name="lab_last_access">上次訪問時間</xsl:with-param>
			<xsl:with-param name="lab_score">成績</xsl:with-param>
			<xsl:with-param name="lab_detail">詳情</xsl:with-param>
			<xsl:with-param name="lab_status_viewed">己查閱</xsl:with-param>
			<xsl:with-param name="lab_status_not_viewed">未查閱</xsl:with-param>
			<xsl:with-param name="lab_item_type">請選擇類型:</xsl:with-param>
			<xsl:with-param name="lab_user_report">用戶報告</xsl:with-param>
			<xsl:with-param name="lab_usage_report">使用報告</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">跟蹤報告</xsl:with-param>
			<xsl:with-param name="lab_tracking_report_desc">按用戶或課程單元查看在線內容的跟蹤報告。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_A">甲</xsl:with-param>
			<xsl:with-param name="lab_B">乙</xsl:with-param>
			<xsl:with-param name="lab_C">丙</xsl:with-param>
			<xsl:with-param name="lab_D">丁</xsl:with-param>
			<xsl:with-param name="lab_F">不合格</xsl:with-param>
			<xsl:with-param name="lab_no_data">沒有數據顯示</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_name">标题</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_hits">访问次数</xsl:with-param>
			<xsl:with-param name="lab_total_time">访问总时长</xsl:with-param>
			<xsl:with-param name="lab_last_access">上次访问时间</xsl:with-param>
			<xsl:with-param name="lab_score">成绩</xsl:with-param>
			<xsl:with-param name="lab_detail">详情</xsl:with-param>
			<xsl:with-param name="lab_status_viewed">己查阅</xsl:with-param>
			<xsl:with-param name="lab_status_not_viewed">未查阅</xsl:with-param>
			<xsl:with-param name="lab_item_type">请选择类型：</xsl:with-param>
			<xsl:with-param name="lab_user_report">用户报告</xsl:with-param>
			<xsl:with-param name="lab_usage_report">使用报告</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">跟踪报告</xsl:with-param>
			<xsl:with-param name="lab_tracking_report_desc">按用户或按课程模块查看在线内容的跟踪报告。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_A">甲</xsl:with-param>
			<xsl:with-param name="lab_B">乙</xsl:with-param>
			<xsl:with-param name="lab_C">丙</xsl:with-param>
			<xsl:with-param name="lab_D">丁</xsl:with-param>
			<xsl:with-param name="lab_F">不合格</xsl:with-param>
			<xsl:with-param name="lab_no_data">没有数据显示</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_name">Title</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_hits">Hits</xsl:with-param>
			<xsl:with-param name="lab_total_time">Total time spent</xsl:with-param>
			<xsl:with-param name="lab_last_access">Last accessed</xsl:with-param>
			<xsl:with-param name="lab_score">Score</xsl:with-param>
			<xsl:with-param name="lab_detail">Details</xsl:with-param>
			<xsl:with-param name="lab_status_viewed">Viewed</xsl:with-param>
			<xsl:with-param name="lab_status_not_viewed">Not viewed</xsl:with-param>
			<xsl:with-param name="lab_item_type">Please select type :</xsl:with-param>
			<xsl:with-param name="lab_user_report">User report</xsl:with-param>
			<xsl:with-param name="lab_usage_report">Usage report</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">Tracking report</xsl:with-param>
			<xsl:with-param name="lab_tracking_report_desc">View the online content tracking report by users or course modules.</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_A">A</xsl:with-param>
			<xsl:with-param name="lab_B">B</xsl:with-param>
			<xsl:with-param name="lab_C">C</xsl:with-param>
			<xsl:with-param name="lab_D">D</xsl:with-param>
			<xsl:with-param name="lab_F">Fail</xsl:with-param>
			<xsl:with-param name="lab_no_data">No data is displayed</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
	<xsl:param name="lab_name"/>
	<xsl:param name="lab_status"/>
	<xsl:param name="lab_hits"/>
	<xsl:param name="lab_total_time"/>
	<xsl:param name="lab_last_access"/>
	<xsl:param name="lab_score"/>
	<xsl:param name="lab_detail"/>
	<xsl:param name="lab_status_viewed"/>
	<xsl:param name="lab_status_not_viewed"/>
	<xsl:param name="lab_item_type"/>
	<xsl:param name="lab_user_report"/>
	<xsl:param name="lab_usage_report"/>
	<xsl:param name="lab_tracking_report"/>
	<xsl:param name="lab_tracking_report_desc"/>
	<xsl:param name="lab_g_form_btn_close"/>
	<xsl:param name="lab_A"/>
	<xsl:param name="lab_B"/>
	<xsl:param name="lab_C"/>
	<xsl:param name="lab_D"/>
	<xsl:param name="lab_F"/>
	<xsl:param name="lab_no_data" />
		<xsl:variable name="escaped_course_title">
			<xsl:call-template name="escape_js">
				<xsl:with-param name="input_str" select="/report/course/@title"/>
			</xsl:call-template>
		</xsl:variable>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_report.js"/>
			<script src="{$wb_js_path}wb_assignment.js" language="JavaScript" type="text/javascript"/>
			<script src="{$wb_js_path}wb_module.js" language="JavaScript" type="text/javascript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
			var module_lst = new wbModule;
			var rpt = new wbReport;
			var ass = new wbAssignment;

				wb_utils_set_cookie('usr_ent_id' , ']]><xsl:value-of select="/report/learner/@ent_id"/><![CDATA['); 
				wb_utils_set_cookie('course_name' , ']]><xsl:value-of select="$escaped_course_title"/><![CDATA['); 

		]]></script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmSearch" onSubmit="return status()">
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_tracking_report"/>&#160;-&#160;<xsl:value-of select="report/course/@title"/>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_sub_title">
					<xsl:with-param name="text">
						<img src="{$wb_img_path}user.gif" border="0" align="absmiddle"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="report/learner/@name"/> (<xsl:value-of select="report/learner/@usr_id"/>)</xsl:with-param>
				</xsl:call-template>
				<!-- list view -->
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td width="8">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td>
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td valign="top">
							<span class="TitleText">
								<xsl:value-of select="$lab_name"/>
							</span>
						</td>
						<td valign="top">
							<span class="TitleText">
								<xsl:value-of select="$lab_status"/>
							</span>
						</td>
						<td align="center" valign="top">
							<span class="TitleText">
								<xsl:value-of select="$lab_hits"/>
							</span>
						</td>
						<td align="center" valign="top">
							<span class="TitleText">
								<xsl:value-of select="$lab_total_time"/>
							</span>
						</td>
						<td valign="top">
							<span class="TitleText">
								<xsl:value-of select="$lab_last_access"/>
							</span>
						</td>
						<td align="center" valign="top">
							<span class="TitleText">
								<xsl:value-of select="$lab_score"/>
							</span>
						</td>
						<td align="center" valign="top">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td width="8">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<xsl:apply-templates select="report/course/tableofcontents">
							<xsl:with-param name="lab_status_not_viewed" select="$lab_status_not_viewed"/>
							<xsl:with-param name="lab_detail" select="$lab_detail"/>
							<xsl:with-param name="lab_A" select="$lab_A"/>
							<xsl:with-param name="lab_B" select="$lab_B"/>
							<xsl:with-param name="lab_C" select="$lab_C"/>
							<xsl:with-param name="lab_D" select="$lab_D"/>
							<xsl:with-param name="lab_F" select="$lab_F"/>
							<xsl:with-param name="lab_no_data" select="$lab_no_data" />
						</xsl:apply-templates>
				</table>
				
				<div class="wzb-bar">
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_close"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:self.close()</xsl:with-param>
							</xsl:call-template>
				</div>
			</form>
		</body>
	</xsl:template>
	<!--=======================================================================-->
	<xsl:template match="tableofcontents">
		<xsl:param name="lab_status_not_viewed"/>
		<xsl:param name="lab_detail"/>
		<xsl:param name="lab_A"/>
		<xsl:param name="lab_B"/>
		<xsl:param name="lab_C"/>
		<xsl:param name="lab_D"/>
		<xsl:param name="lab_F"/>
		<xsl:param name="lab_no_data" />
		<xsl:choose>
			<xsl:when test="count(/report/course/module_list/module) = 0">
				<tr>
					<td colspan="10">
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text">
								<xsl:value-of select="$lab_no_data"/>
							</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="/report/course/tableofcontents//item[@identifierref!='']">
					<xsl:apply-templates select="/report/course/module_list">
						<xsl:with-param name="title" select="@title"/>
						<xsl:with-param name="id" select="@identifierref"/>
						<xsl:with-param name="pos" select="position()"/>
						<xsl:with-param name="lab_status_not_viewed" select="$lab_status_not_viewed"/>
						<xsl:with-param name="lab_detail" select="$lab_detail"/>
						<xsl:with-param name="lab_A" select="$lab_A"/>
						<xsl:with-param name="lab_B" select="$lab_B"/>
						<xsl:with-param name="lab_C" select="$lab_C"/>
						<xsl:with-param name="lab_D" select="$lab_D"/>
						<xsl:with-param name="lab_F" select="$lab_F"/>
					</xsl:apply-templates>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="module_list">
		<xsl:param name="lab_status_not_viewed"/>
		<xsl:param name="title"/>
		<xsl:param name="id"/>
		<xsl:param name="pos"/>
		<xsl:param name="lab_detail"/>
		<xsl:param name="lab_A"/>
		<xsl:param name="lab_B"/>
		<xsl:param name="lab_C"/>
		<xsl:param name="lab_D"/>
		<xsl:param name="lab_F"/>
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="$pos mod 2">RowsEven</xsl:when>
				<xsl:otherwise>RowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="count(module[@id = $id]) = 1">
				<xsl:apply-templates select="module[@id = $id]">
					<xsl:with-param name="row_class" select="$row_class"/>
					<xsl:with-param name="lab_status_not_viewed" select="$lab_status_not_viewed"/>
					<xsl:with-param name="lab_detail" select="$lab_detail"/>
					<xsl:with-param name="lab_A" select="$lab_A"/>
					<xsl:with-param name="lab_B" select="$lab_B"/>
					<xsl:with-param name="lab_C" select="$lab_C"/>
					<xsl:with-param name="lab_D" select="$lab_D"/>
					<xsl:with-param name="lab_F" select="$lab_F"/>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="module">
		<xsl:param name="row_class"/>
		<xsl:param name="lab_status_not_viewed"/>
		<xsl:param name="lab_detail"/>
		<xsl:param name="lab_A"/>
		<xsl:param name="lab_B"/>
		<xsl:param name="lab_C"/>
		<xsl:param name="lab_D"/>
		<xsl:param name="lab_F"/>
		<xsl:variable name="escaped_title">
			<xsl:call-template name="escape_js">
				<xsl:with-param name="input_str" select="@title"/>
			</xsl:call-template>
		</xsl:variable>
		<script><![CDATA[mod_name]]><xsl:value-of select="@id"/><![CDATA[ = ']]><xsl:value-of select="$escaped_title"/><![CDATA[']]></script>
		<tr class="{$row_class}">
			<td width="8">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td valign="middle">
				<xsl:variable name="item_type_lowercase">
					<xsl:call-template name="change_lowercase">
						<xsl:with-param name="input_value" select="@type"/>
					</xsl:call-template>
				</xsl:variable>
				<img src="{$wb_img_path}icol_{$item_type_lowercase}.gif" border="0" align="absmiddle"/>
			</td>
			<td>
				<span class="Text">
					<xsl:value-of select="@title"/>
				</span>
			</td>
			<td>
				<xsl:variable name="is_wizpack">
					<xsl:choose>
						<xsl:when test="@src_type != 'WIZPACK'"/>
						<xsl:otherwise>true</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<span class="Text">
					<xsl:call-template name="display_progress_tracking">
						<xsl:with-param name="status" select="@status"/>
						<xsl:with-param name="type">module</xsl:with-param>
						<xsl:with-param name="mod_type" select="@type"/>
						<xsl:with-param name="show_text">true</xsl:with-param>
						<xsl:with-param name="score" select="@score"/>
						<xsl:with-param name="is_wizpack" select="$is_wizpack"/>
						<xsl:with-param name="attempted" select="header/@attempted" />
					</xsl:call-template>
				</span>
			</td>
			<td align="center">
				<span class="Text">
					<xsl:value-of select="@hits"/>
				</span>
			</td>
			<td align="center">
				<span class="Text">
					<xsl:choose>
						<xsl:when test="substring-before(@total_time,'.') = ''  or substring-before(@total_time,'.') = 'null'">
							<xsl:value-of select="@total_time"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="substring-before(@total_time,'.')"/>
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</td>
			<td>
				<span class="Text">
					<xsl:choose>
						<xsl:when test="@last_access = '' or @last_access = 'null'">--</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="@last_access"/>
								</xsl:with-param>
								<xsl:with-param name="dis_time">T</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</td>
			<td align="center">
				<span class="Text">
					<xsl:choose>
						<xsl:when test="@type = 'SVY'">
							<xsl:text>--</xsl:text>
						</xsl:when>
						<!-- handle not submitted case -->
						<xsl:when test="@type = 'TST' and @status='I'">
							<xsl:text>--</xsl:text>
						</xsl:when>
						<xsl:when test="@type = 'DXT' and @status='I'">
							<xsl:text>--</xsl:text>
						</xsl:when>
						<!-- ========== -->
						<xsl:when test="@score != ''">
							<xsl:call-template name="display_score">
								<xsl:with-param name="score">
									<xsl:choose>
										<xsl:when test="string(number(@score)) = 'NaN'">
											<xsl:choose>
												<xsl:when test="@score = 'A+'"><xsl:value-of select="$lab_A"/>+</xsl:when>
												<xsl:when test="@score = 'A'"><xsl:value-of select="$lab_A"/></xsl:when>
												<xsl:when test="@score = 'A-'"><xsl:value-of select="$lab_A"/>-</xsl:when>
												<xsl:when test="@score = 'B+'"><xsl:value-of select="$lab_B"/>+</xsl:when>
												<xsl:when test="@score = 'B'"><xsl:value-of select="$lab_B"/></xsl:when>
												<xsl:when test="@score = 'B-'"><xsl:value-of select="$lab_B"/></xsl:when>
												<xsl:when test="@score = 'C+'"><xsl:value-of select="$lab_C"/>+</xsl:when>
												<xsl:when test="@score = 'C'"><xsl:value-of select="$lab_C"/></xsl:when>
												<xsl:when test="@score = 'C-'"><xsl:value-of select="$lab_C"/>-</xsl:when>
												<xsl:when test="@score = 'D'"><xsl:value-of select="$lab_D"/></xsl:when>
												<xsl:when test="@score = 'F'"><xsl:value-of select="$lab_F"/></xsl:when>
											</xsl:choose>
										</xsl:when>
										<xsl:otherwise>
											<!--<xsl:value-of select="number(@score)"/>-->
											<xsl:value-of select="@score"/>  
										</xsl:otherwise>
									</xsl:choose>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="@mod_vendor = 'SkillSoft'">--</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
				</span>
			</td>
			<td align="right">
				<span class="Text">
					<xsl:choose>
						<xsl:when test="@src_type != 'WIZPACK'">
							<xsl:choose>
								<xsl:when test="(@type = 'TST' or @type = 'DXT') and  @status  = 'I' ">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</xsl:when>
								<xsl:when test="(@type = 'TST' or @type = 'DXT' or @type= 'STX') and  @score  != '' ">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_href">javascript:wb_utils_set_cookie('mod_name',mod_name<xsl:value-of select="@id"/>);rpt.report_usr('<xsl:value-of select="/report/learner/@ent_id"/>',<xsl:value-of select="@id"/>,'<xsl:value-of select="$tkh_id"/>',0)</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_name" select="$lab_detail"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:when test="@type='AICC_AU' and @mod_vendor = 'SkillSoft' and (@hits != '' and @hits != '0') ">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_href">javascript:rpt.lrn_skillsoft_lst(<xsl:value-of select="/report/course/@id"/>,<xsl:value-of select="@id"/>,'<xsl:value-of select="/report/learner/@ent_id"/>','<xsl:value-of select="$tkh_id"/>')</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_name" select="$lab_detail"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:when test="@type='AICC_AU' and not(@mod_vendor='SkillSoft') and (@hits != '' and @hits != '0') ">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_href">javascript:rpt.aicc_rpt('<xsl:value-of select="/report/course/@id"/>','<xsl:value-of select="@id"/>','<xsl:value-of select="/report/learner/@ent_id"/>',true,'<xsl:value-of select="$tkh_id"/>',true)</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_name" select="$lab_detail"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:when test="@type='NETG_COK' and (@hits != '' and @hits != '0') ">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_href">javascript:rpt.netg_rpt('<xsl:value-of select="/report/course/@id"/>','<xsl:value-of select="@id"/>','<xsl:value-of select="/report/learner/@ent_id"/>',true,'<xsl:value-of select="$tkh_id"/>')</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_name" select="$lab_detail"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_href">javascript:wb_utils_set_cookie('mod_name',mod_name<xsl:value-of select="@id"/>); rpt.lrn_loc_lst(<xsl:value-of select="/report/learner/@ent_id"/>,<xsl:value-of select="@id"/>,'<xsl:value-of select="$tkh_id"/>')</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_detail"/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</td>
			<td width="8">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
		</tr>
	</xsl:template>
	<!--==============================================================================================-->
</xsl:stylesheet>
