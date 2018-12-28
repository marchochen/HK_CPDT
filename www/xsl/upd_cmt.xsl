<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
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
	<xsl:import href="utils/display_time.xsl"/>
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
	<xsl:variable name="ccr_id" select="/CourseCriteria/score_item/cmt_ccr_id"/>
	<xsl:variable name="cmt_id" select="/CourseCriteria/score_item/@cmt_id"/>
	<xsl:variable name="cmt_cmr_id" select="/CourseCriteria/score_item/cmt_cmr_id"/>
	<xsl:variable name="cmt_title" select="/CourseCriteria/score_item/cmt_title"/>
	<xsl:variable name="mod_res_id" select="/CourseCriteria/score_item/mod_res_id"/>
	<xsl:variable name="cmt_max_score" select="/CourseCriteria/score_item/cmt_max_score"/>
	<xsl:variable name="cmt_pass_score" select="/CourseCriteria/score_item/cmt_pass_score"/>
	<xsl:variable name="upd_timestamp" select="/CourseCriteria/score_item/cmt_update_timestamp"/>
	<xsl:variable name="current_role" select="/CourseCriteria/current_role"/>
	
	<!-- 课程分数设置 -->
	<xsl:variable name="label_core_training_management_239" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1163')"/>
	
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
			function offline_click(document){
				document.frmXml.cmt_max_score.disabled=false;
				document.frmXml.cmt_pass_score.disabled=false;
				if (document.frmXml.online_module){
					document.frmXml.online_module.disabled=true;
				}
			}
			function online_click(document){
				document.frmXml.cmt_max_score.disabled=true;
				document.frmXml.cmt_pass_score.disabled=true;
				document.frmXml.online_module.disabled=false;
			}			
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
				<input type="hidden" name="cmd" value="upd_cmt_exec"/>
				<input type="hidden" name="module" value="course.CourseCriteriaModule"/>
				<input type="hidden" name="itm_id" value=""/>
				<input type="hidden" name="cmt_cmr_id" value="{$cmt_cmr_id}"/>
				<input type="hidden" name="mod_res_id" value=""/>
				<input type="hidden" name="ccr_id" value=""/>
				<input type="hidden" name="cmt_id" value=""/>
				<input type="hidden" name="upd_comp_date" value=""/>
				<input type="hidden" name="re_evaluate_ind" value=""/>
				<input type="hidden" name="upd_timestamp" value="{$upd_timestamp}"/>
			</form>
		</body>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>訊息</xsl:with-param>
			<xsl:with-param name="lab_main_eval">修改計分項目</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_max_score">滿分</xsl:with-param>
			<xsl:with-param name="lab_pass_score">合格分數</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_method">計分方式</xsl:with-param>
			<xsl:with-param name="lab_offline">作爲一個離線的學習單元，有以下計分標準:</xsl:with-param>
			<xsl:with-param name="lab_online">作爲一個網上的學習單元，在下面指定:</xsl:with-param>
			<xsl:with-param name="lab_scope">應用範圍</xsl:with-param>
			<xsl:with-param name="lab_not_re_evaluate">此修改只應用於日後的分數計算，但並不應用於以往的分數及其結訓條件。</xsl:with-param>
			<xsl:with-param name="lab_re_evaluate">此修改除應用於日後的分數計算外，亦及時應用於以往的分數及其結訓記錄，並有以下選項：</xsl:with-param>
			<xsl:with-param name="lab_mod_date">原有的結訓日期改為更新設定當日</xsl:with-param>
			<xsl:with-param name="lab_message">*此修改只會影響日後所開班別的預設值，並不會影響已有班別的設定。</xsl:with-param>
			<xsl:with-param name="lab_content">内容</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_main_eval">修改计分项目</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_max_score">满分</xsl:with-param>
			<xsl:with-param name="lab_pass_score">合格分数</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_method">计分方式</xsl:with-param>
			<xsl:with-param name="lab_offline">作为一个离线的学习单元，有以下计分标准:</xsl:with-param>
			<xsl:with-param name="lab_online">作为一个网上的学习单元，在下面指定:</xsl:with-param>
			<xsl:with-param name="lab_scope">应用范围</xsl:with-param>
			<xsl:with-param name="lab_not_re_evaluate">此修改只应用于日后的分数计算，但并不应用于以往的分数及其结训条件。</xsl:with-param>
			<xsl:with-param name="lab_re_evaluate">此修改除应用于日后的分数计算外，亦及时应用于以往的分数及其结训记录，并有以下选项：</xsl:with-param>
			<xsl:with-param name="lab_mod_date">原有的结训日期改为更新设定当日</xsl:with-param>
			<xsl:with-param name="lab_message">*此修改只会影响日后所开班別的预设值，并不会影响已有班別的设定。</xsl:with-param>
			<xsl:with-param name="lab_content">内容</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
			<xsl:with-param name="lab_main_eval">Edit scoring item</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_max_score">Maximum score</xsl:with-param>
			<xsl:with-param name="lab_pass_score">Passing Score</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_method">Implementation</xsl:with-param>
			<xsl:with-param name="lab_offline">As an offline learning module with the following scoring scheme:</xsl:with-param>
			<xsl:with-param name="lab_online">As the online learning module specified below:</xsl:with-param>
			<xsl:with-param name="lab_scope">Records affected</xsl:with-param>
			<xsl:with-param name="lab_not_re_evaluate">Change in setting only affects the scores obtained thereafter. The previous scores are not affected.</xsl:with-param>
			<xsl:with-param name="lab_re_evaluate">Change in setting affects the scores obtained thereafter, as well as the scores obtained and the result evaluated previously.</xsl:with-param>
			<xsl:with-param name="lab_mod_date">Change the completion date to today</xsl:with-param>
			<xsl:with-param name="lab_message">*Changes in scoring item will only affect the classes of this course created thereafter. The corresponding setting of class created in the past remains unchanged.</xsl:with-param>
			<xsl:with-param name="lab_content">Content</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="content">
		<xsl:param name="lab_main_eval"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_max_score"/>
		<xsl:param name="lab_g_txt_btn_ok"/>
		<xsl:param name="lab_g_txt_btn_cancel"/>
		<xsl:param name="lab_message"/>
		<xsl:param name="lab_pass_score"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_method"/>
		<xsl:param name="lab_online"/>
		<xsl:param name="lab_offline"/>
		<xsl:param name="lab_not_re_evaluate"/>
		<xsl:param name="lab_re_evaluate"/>
		<xsl:param name="lab_mod_date"/>
		<xsl:param name="lab_scope"/>
		<xsl:param name="lab_content"/>
		
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
					<xsl:when test="/CourseCriteria/item/@run_ind = 'false'">
					
						<xsl:choose>
							<xsl:when test="$current_role='TADM_1'">
								<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
									<xsl:value-of select="/CourseCriteria/item/title"/>
								</a>
							</xsl:when>
							<xsl:when test="$current_role='INSTR_1'">
								<a href="javascript:itm_lst.get_itm_instr_view({$itm_id})" class="NavLink">
									<xsl:value-of select="/CourseCriteria/item/title"/>
								</a>
							</xsl:when>
						</xsl:choose>
						
						<span class="TitleText">&#160;&gt;&#160;</span>
						<a href="javascript:cmt.get_score_scheme_list({$itm_id})" class="NavLink">
							<xsl:value-of select="$label_core_training_management_239"/>
						</a>
						<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_main_eval"/>
						</span>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="/CourseCriteria/item/nav/item" mode="nav">
							<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
							<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
						</xsl:apply-templates>
						<span class="NavLink">
							<span class="TitleText">&#160;&gt;&#160;</span>
							<a href="javascript:cmt.get_score_scheme_list({$itm_id})" class="NavLink">
								<xsl:value-of select="$cmt_title"/>
							</a>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<xsl:value-of select="$lab_main_eval"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<!--<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="extra_td">
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>-->
		<table>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_title"/>：
				</td>
				<td class="wzb-form-control">
					<input size="35" name="cmt_title" type="text" style="width:150px;" maxlength="50" class="wzb-inputText" value="{$cmt_title}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_method"/>：
				</td>
				<td class="wzb-form-control">
					<input name="cmt_method" value="offline" type="radio" onclick="javascript:offline_click(document);"/>
					<xsl:value-of select="$lab_offline"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
				</td>
				<td class="wzb-form-control">
					<table>
						<tr>
							<td width="10%" style="text-align:right;">
								<xsl:value-of select="$lab_max_score"/>：
							</td>
							<td class="wzb-form-control">
								<input size="35" name="cmt_max_score" title="{$lab_max_score}" disabled="disabled" type="text" style="width:70px;" maxlength="50" class="wzb-inputText" value="{number($cmt_max_score)}"/>
							</td>
						</tr>
						<tr>
							<td width="10%" style="text-align:right;">
								<xsl:value-of select="$lab_pass_score"/>：
							</td>
							<td class="wzb-form-control">
								<input size="35" name="cmt_pass_score" title="{$lab_pass_score}" disabled="disabled" type="text" style="width:70px;" maxlength="50" class="wzb-inputText" value="{number($cmt_pass_score)}"/>
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
						<input name="cmt_method" value="online" checked="checked" type="radio" onclick="javascript:online_click(document);"/>
						<xsl:value-of select="$lab_online"/>
					</td>
				</tr>
				<tr>
					<td class="wzb-form-label">
					</td>
					<td class="wzb-form-control">
						<span class="margin-left20"></span>
						<select name="online_module" size="1" class="select" onchange="change_res_status(this)">
							<xsl:for-each select="/CourseCriteria/online_module/module">
								<xsl:choose>
									<xsl:when test="@id = $mod_res_id">
										<option value="{@id}" selected="selected">
											<xsl:value-of select="title"/>&#160;(<xsl:value-of select="$lab_max_score"/>
											<xsl:text>：</xsl:text>
											<xsl:choose>
												<xsl:when test="max_score/text() = '0.0'">--</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="number(max_score)"/>
												</xsl:otherwise>
											</xsl:choose>
											<xsl:text>;&#160;</xsl:text>
											<xsl:value-of select="$lab_pass_score"/>
											<xsl:text>：</xsl:text>
											<xsl:choose>
												<xsl:when test="pass_score/text() = '0.0'">--</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="number(pass_score)"/>
												</xsl:otherwise>
											</xsl:choose>)
										   </option>
									</xsl:when>
									<xsl:otherwise>
										<option value="{@id}">
											<xsl:value-of select="title"/>&#160;(<xsl:value-of select="$lab_max_score"/>
											<xsl:text>：</xsl:text>
											<xsl:choose>
												<xsl:when test="max_score/text() = '0.0'">--</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="number(max_score)"/>
												</xsl:otherwise>
											</xsl:choose>
											<xsl:text>;&#160;</xsl:text>
											<xsl:value-of select="$lab_pass_score"/>
											<xsl:text>：</xsl:text>
											<xsl:choose>
												<xsl:when test="pass_score/text() = '0.0'">--</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="number(pass_score)"/>
												</xsl:otherwise>
											</xsl:choose>)
										   </option>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:for-each>
						</select>
						<xsl:for-each select="/CourseCriteria/online_module/module">
							<input type="hidden" id="mod_public_{@id}" value="{status}"/>
						</xsl:for-each>
						<input type="hidden" id="selected_mod_status" value="{/CourseCriteria/online_module/module[@id=$mod_res_id]/status}"/>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="$mod_res_id = '0'">
				<script language="Javascript"><![CDATA[
											if(document.frmXml.online_module){
												document.frmXml.online_module.disabled = true;
												document.frmXml.cmt_method[0].checked = true;
											} else {
												document.frmXml.cmt_method.checked = true;
											}
												document.frmXml.cmt_max_score.disabled = false;
												document.frmXml.cmt_pass_score.disabled = false;												
												]]></script>
			</xsl:if>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_scope"/>：
				</td>
				<td class="wzb-form-control">
					<table>
						<tr>
							<td width="1" valign="top">
								<input name="re_evaluate" value="false" type="radio" id="re_rvaluate_1" checked="checked" onclick="javascript:upd_date.disabled=true;"/>
							</td>
							<td>
								<label for="re_rvaluate_1">
									<xsl:value-of select="$lab_not_re_evaluate"/>
								</label>
							</td>
						</tr>
						<tr>
							<td width="1" valign="top">
								<input name="re_evaluate" value="true" type="radio" id="re_evaluate" onclick="javascript:upd_date.disabled=false;"/>
							</td>
							<td>
								<label for="re_evaluate">
									<xsl:value-of select="$lab_re_evaluate"/>
								</label>
							</td>
						</tr>
						<tr>
							<td>
							</td>
							<td>
								<input name="upd_date" type="checkbox" disabled="disabled"/>
								<xsl:value-of select="$lab_mod_date"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
				</td>
				<td class="wzb-ui-module-text">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">Javascript:cmt.upd_exec(frmXml,'<xsl:value-of select="$cmt_id"/>','<xsl:value-of select="$itm_id"/>','<xsl:value-of select="$ccr_id"/>','<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$content_def"/>')</xsl:with-param>
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
