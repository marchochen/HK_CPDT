<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="online_cnt" select="count(CourseCriteria/online_module/module)"/>
	<xsl:variable name="itm_id" select="/CourseCriteria/item/@id"/>
	<xsl:variable name="run_ind" select="/CourseCriteria/item/@run_ind"/>
	<xsl:variable name="content_def" select="/CourseCriteria/item/@content_def"/>
	<xsl:variable name="ccr_id" select="/CourseCriteria/marking_scheme_list/@ccr_id"/>
	<xsl:variable name="current_role" select="/CourseCriteria/current_role"/>
	<!-- ================================================================ -->
	<xsl:template match="/">
		<xsl:apply-templates select="CourseCriteria"/>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="CourseCriteria">
		<html>
			<xsl:call-template name="draw_header"/>
			<xsl:call-template name="draw_body"/>
		</html>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_header">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_scorescheme.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript"><![CDATA[
			var cmt=new wbScoreScheme;
			attd = new wbAttendance;
			itm_lst = new wbItem;
		]]></script>
		</head>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_body">
		<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0" onLoad="wb_utils_gen_form_focus(document.frmXml)">
			<form name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="cmd" value="add_new_cmt"/>
				<input type="hidden" name="module" value="course.CourseCriteriaModule"/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="ccr_id" value=""/>
				<input type="hidden" name="itm_id" value=""/>
				<input type="hidden" name="mod_res_id" value=""/>
			</form>
		</body>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>訊息</xsl:with-param>
			<xsl:with-param name="lab_main_eval">新增計分項目</xsl:with-param>
			<xsl:with-param name="lab_score">計分規則</xsl:with-param>
			<xsl:with-param name="lab_content">内容</xsl:with-param>
			<xsl:with-param name="lab_main">新增</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_max_score">滿分</xsl:with-param>
			<xsl:with-param name="lab_pass_score">合格分數</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_method">計分方式</xsl:with-param>
			<xsl:with-param name="lab_offline">作爲一個離線的學習單元，有以下計分標準：</xsl:with-param>
			<xsl:with-param name="lab_online">作爲一個網上的學習單元，在下面指定：</xsl:with-param>
			<xsl:with-param name="lab_message">*計分項目設定將會成爲此課程日後所開班別的計分項目預設值，線上單元可先設定於課程中，再在班別管理中指定。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_main_eval">添加计分项目</xsl:with-param>
			<xsl:with-param name="lab_score">计分规则</xsl:with-param>
			<xsl:with-param name="lab_content">内容</xsl:with-param>
			<xsl:with-param name="lab_main">增加</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_max_score">满分</xsl:with-param>
			<xsl:with-param name="lab_pass_score">合格分数</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_method">计分方式</xsl:with-param>
			<xsl:with-param name="lab_offline">作为一个离线的学习单元，有以下计分标准：</xsl:with-param>
			<xsl:with-param name="lab_online">作为一个网上的学习单元，在下面指定：</xsl:with-param>
			<xsl:with-param name="lab_message">*计分项目设定将会成为此课程日后所开班别的计分项目预设值，线上单元可先设定于课程中，再在班別管理中指定。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
			<xsl:with-param name="lab_main_eval">Add scoring item</xsl:with-param>
			<xsl:with-param name="lab_content">Content</xsl:with-param>
			<xsl:with-param name="lab_score">Scoring rule</xsl:with-param>
			<xsl:with-param name="lab_main">Create scoring item</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_max_score">Maximum score</xsl:with-param>
			<xsl:with-param name="lab_pass_score">Passing Score</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_next">Next</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_method">Implementation</xsl:with-param>
			<xsl:with-param name="lab_offline">As an offline learning module with the following scoring scheme：</xsl:with-param>
			<xsl:with-param name="lab_online">As the online learning module specified below：</xsl:with-param>
			<xsl:with-param name="lab_message">*Scoring item setting will be defaulted to the classes of this course created thereafter. Online module can be set as descriptive information in the course and actual module will be assigned in the class.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="content">
		<xsl:param name="lab_main_eval"/>
		<xsl:param name="lab_main"/>
		<xsl:param name="lab_content"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_max_score"/>
		<xsl:param name="lab_no_eval"/>
		<xsl:param name="lab_g_txt_btn_next"/>
		<xsl:param name="lab_g_txt_btn_cancel"/>
		<xsl:param name="lab_message"/>
		<xsl:param name="lab_pass_score"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_method"/>
		<xsl:param name="lab_online"/>
		<xsl:param name="lab_offline"/>
		<xsl:param name="lab_score"/>
		 <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">103</xsl:with-param>
		</xsl:call-template>
	<div class="wzb-item-main">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:choose>
					
					<xsl:when test="//itm_action_nav/hasTeachingCourse/text()='true'">
						<a href="javascript:itm_lst.get_itm_instr_view({//itm_action_nav/@itm_id})" class="NavLink">
							<xsl:value-of select="//itm_action_nav/@itm_title"/>
						</a>
					</xsl:when>
					<xsl:when test="/CourseCriteria/item/@run_ind = 'false' and $current_role='TAMD_1'">
						<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
							<xsl:value-of select="/CourseCriteria/item/title"/>
						</a>
					</xsl:when>
				
					<xsl:otherwise>
						<xsl:apply-templates select="/CourseCriteria/item/nav/item" mode="nav">
							<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
							<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
				<span class="NavLink">
					<span class="TitleText">&#160;&gt;&#160;</span>
					<a href="javascript:cmt.get_score_scheme_list({$itm_id})" class="NavLink">
						<xsl:value-of select="$lab_score"/>
					</a>
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:value-of select="$lab_main_eval"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_head">
			<!--<xsl:with-param name="text" select="$lab_main"/>-->
			<xsl:with-param name="extra_td">
			</xsl:with-param>
		</xsl:call-template>
		<table>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_title"/>：
				</td>
				<td class="wzb-form-control">
					<input size="35" name="cmt_title" title="{$lab_title}" type="text" style="width:150px;" maxlength="50" class="wzb-inputText"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_method"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:if test="$online_cnt != '0'">
						<input name="cmt_method" value="offline" type="radio" checked="checked" onclick="javascript:cmt_max_score.disabled=false;cmt_pass_score.disabled=false;online_module.disabled=true;"/>
					</xsl:if>
					<xsl:value-of select="$lab_offline"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
				</td>
				<td class="wzb-form-control">
					<table>
						<tr>
							<td width="12%" style="text-align:right;">
								<xsl:value-of select="$lab_max_score"/>：
							</td>
							<td class="wzb-form-control">
								<input size="35" name="cmt_max_score" type="text" title="{$lab_max_score}" style="width:70px;" maxlength="50" class="wzb-inputText"/>
							</td>
						</tr>
						<tr>
							<td style="text-align:right;">
								<xsl:value-of select="$lab_pass_score"/>：
							</td>
							<td class="wzb-form-control">
								<input size="35" name="cmt_pass_score" title="{$lab_pass_score}" type="text" style="width:70px;" maxlength="50" class="wzb-inputText"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<xsl:if test="$online_cnt != '0'">
				<tr>
					<td class="wzb-form-label">
					</td>
					<td class="wzb-form-control">
						<input name="cmt_method" value="online" type="radio" onclick="javascript:cmt_max_score.disabled=true;cmt_pass_score.disabled=true;online_module.disabled=false;"/>
						<xsl:value-of select="$lab_online"/>
					</td>
				</tr>
				<tr>
					<td class="wzb-form-label">
					</td>
					<td class="wzb-form-control">
							<select name="online_module" size="1" class="select" disabled="disabled" onchange="change_res_status(this)">
								<xsl:for-each select="/CourseCriteria/online_module/module">
									<option value="{@id}">
										<xsl:value-of select="title"/>&#160;(<xsl:value-of select="$lab_max_score"/>
										<xsl:text>：</xsl:text>
										<xsl:choose>
											<xsl:when test="max_score/text() = '0.0'">--</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="max_score"/>
											</xsl:otherwise>
										</xsl:choose>
										<xsl:text>;&#160;</xsl:text>
										<xsl:value-of select="$lab_pass_score"/>
										<xsl:text>：</xsl:text>
										<xsl:choose>
											<xsl:when test="pass_score/text() = '0.0'">--</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="pass_score"/>
											</xsl:otherwise>
										</xsl:choose>)
											   </option>
								</xsl:for-each>
							</select>
							<xsl:for-each select="/CourseCriteria/online_module/module">
								<input type="hidden" id="mod_public_{@id}" value="{status}"/>
							</xsl:for-each>
							<input type="hidden" id="selected_mod_status" value="{/CourseCriteria/online_module/module[position()=1]/status}"/>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td align="right"  class="wzb-form-label">
				</td>
				<td  class="wzb-ui-module-text" align="left">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_next"/>
				<xsl:with-param name="wb_gen_btn_href">Javascript:cmt.add_new_cmt_exec(frmXml,'<xsl:value-of select="$ccr_id"/>','<xsl:value-of select="$itm_id"/>','<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="/CourseCriteria/item/@cos_res_id"/>','<xsl:value-of select="$content_def"/>');</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:history.back();</xsl:with-param>
			</xsl:call-template>
		</div>
	</div>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="item" mode="nav">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_session_info"/>
		<xsl:variable name="_count" select="count(preceding-sibling::item)"/>
		<xsl:choose>
			<xsl:when test="@run_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=$_count">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.get_item_run_list({$value})" class="NavLink">
					<xsl:choose>
						<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
					</xsl:choose>
				</a>
				<span class="NavLink">&#160;&gt;&#160;</span>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:when test="@session_ind = 'true'">
				<span class="NavLink">&#160;&gt;&#160;</span>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=last()">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.session.get_session_list({$value})" class="NavLink">
					<xsl:value-of select="$lab_session_info"/>
				</a>
				<span>&#160;&gt;&#160;</span>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a href="javascript:itm_lst.get_item_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ================================================================ -->
</xsl:stylesheet>
