<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>

	<!-- share -->
	<xsl:import href="share/sys_tab_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	
	<xsl:variable name="is_new_cos" select="item_target/is_new_cos"/>
	<xsl:variable name="itm_id" select="item_target/item/@id"/>
	<xsl:variable name="rule_type" select="item_target/target_rule_lst/@type"/>
	
	<xsl:variable name="lab_next_btn" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '101')"/>
	<xsl:variable name="lab_ok_btn" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '872')"/>
	<xsl:output indent="yes"/>
	<!-- ============================================================= -->
	<xsl:template match="/item_target">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript"><![CDATA[	
				itm = new wbItem;
				var is_new_cos = getUrlParam("is_new_cos");
				$(document).ready(function(){
					if(is_new_cos=='true'){
						$('#wzb-item-main').addClass('aa').removeClass('wzb-item-main');
					}
				});
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmResult">
				<input type="hidden" name="module"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="itm_id" value="{$itm_id}"/>
				<input type="hidden" name="itm_target_type" value="{$rule_type}"/>
				<input type="hidden" name="last_upd_time" value="{target_rule_lst/@last_upd_time}"/>
				<input type="hidden" name="last_target_enrol_type" value="{item/@target_enrol_type}"/>
				<input type="hidden" name="target_enrol_type" value=""/>
				<input type="hidden" name="is_del_all" value="false"/>
			</form>
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_target_learner">目標學員</xsl:with-param>
			<xsl:with-param name="lab_target_enrollment">可報名學員</xsl:with-param>
			<xsl:with-param name="lab_add_rule">添加規則</xsl:with-param>
			<xsl:with-param name="lab_export">匯出</xsl:with-param>
			<xsl:with-param name="lab_no_rule">沒有規則</xsl:with-param>
			<xsl:with-param name="lab_no_enroll_rule">沒有規則，所有學員皆可報名</xsl:with-param>
			<xsl:with-param name="lab_run_info">班別訊息</xsl:with-param>
			<xsl:with-param name="lab_rule">規則</xsl:with-param>
			<xsl:with-param name="lab_modified_date">修改時間</xsl:with-param>
			<xsl:with-param name="lab_modified_by">修改人</xsl:with-param>
			<xsl:with-param name="lab_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_btn_delete">刪除</xsl:with-param>
			<xsl:with-param name="lab_btn_reset_all">重置為所有學員</xsl:with-param>
			<xsl:with-param name="lab_same_as_tarlrn">與課堂目標學員相同</xsl:with-param>
			<xsl:with-param name="lab_by_self">自定義規則</xsl:with-param>
			<xsl:with-param name="lab_tab_publish">在培訓目錄中發佈</xsl:with-param>
			<xsl:with-param name="lab_tab_autoenrol">自動報名</xsl:with-param>
			<xsl:with-param name="lab_all_group">所有<xsl:value-of select="$lab_group"/>
			</xsl:with-param>
			<xsl:with-param name="lab_all_grade">所有<xsl:value-of select="$lab_grades"/>
			</xsl:with-param>
			<xsl:with-param name="lab_skill">崗位</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_target_learner">目标学员</xsl:with-param>
			<xsl:with-param name="lab_target_enrollment">可报名学员</xsl:with-param>
			<xsl:with-param name="lab_add_rule">添加规则</xsl:with-param>
			<xsl:with-param name="lab_export">导出</xsl:with-param>
			<xsl:with-param name="lab_no_rule">没有规则</xsl:with-param>
			<xsl:with-param name="lab_no_enroll_rule">没有规则，所有学员皆可报名</xsl:with-param>
			<xsl:with-param name="lab_rule">规则</xsl:with-param>
			<xsl:with-param name="lab_run_info">班级信息</xsl:with-param>
			<xsl:with-param name="lab_modified_date">修改时间</xsl:with-param>
			<xsl:with-param name="lab_modified_by">修改人</xsl:with-param>
			<xsl:with-param name="lab_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_btn_delete">删除</xsl:with-param>
			<xsl:with-param name="lab_btn_reset_all">重置为所有学员</xsl:with-param>
			<xsl:with-param name="lab_same_as_tarlrn">与课堂目标学员相同</xsl:with-param>
			<xsl:with-param name="lab_by_self">自定义规则</xsl:with-param>
			<xsl:with-param name="lab_tab_publish">在培训目录中发布</xsl:with-param>
			<xsl:with-param name="lab_tab_autoenrol">自动报名</xsl:with-param>
			<xsl:with-param name="lab_all_group">所有<xsl:value-of select="$lab_group"/>
			</xsl:with-param>
			<xsl:with-param name="lab_all_grade">所有<xsl:value-of select="$lab_grades"/>
			</xsl:with-param>
			<xsl:with-param name="lab_skill">岗位</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_target_learner">Target learner</xsl:with-param>
			<xsl:with-param name="lab_target_enrollment">Target enrollment</xsl:with-param>
			<xsl:with-param name="lab_add_rule">Add rule</xsl:with-param>
			<xsl:with-param name="lab_export">Export</xsl:with-param>
			<xsl:with-param name="lab_no_enroll_rule">No rule, all learners can enroll.</xsl:with-param>
			<xsl:with-param name="lab_no_rule">No rule found.</xsl:with-param>
			<xsl:with-param name="lab_rule">Rule</xsl:with-param>
			<xsl:with-param name="lab_run_info">Course information</xsl:with-param>
			<xsl:with-param name="lab_modified_date">Modified date</xsl:with-param>
			<xsl:with-param name="lab_modified_by">Modified by</xsl:with-param>
			<xsl:with-param name="lab_btn_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_btn_delete">Delete</xsl:with-param>
			<xsl:with-param name="lab_btn_reset_all">Reset to all learners</xsl:with-param>
			<xsl:with-param name="lab_same_as_tarlrn">Same as target learner of the course</xsl:with-param>
			<xsl:with-param name="lab_by_self">Custom rule</xsl:with-param>
			<xsl:with-param name="lab_tab_publish">Publish to catalog</xsl:with-param>
			<xsl:with-param name="lab_tab_autoenrol">Auto enrollment</xsl:with-param>
			<xsl:with-param name="lab_all_group">All groups</xsl:with-param>
			<xsl:with-param name="lab_all_grade">All grades</xsl:with-param>
			<xsl:with-param name="lab_skill">Position</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_target_learner"/>
		<xsl:param name="lab_target_enrollment"/>
		<xsl:param name="lab_add_rule"/>
		<xsl:param name="lab_export"/>
		
		<xsl:param name="lab_no_rule"/>
		<xsl:param name="lab_no_enroll_rule"/>
		<xsl:param name="lab_rule"/>
		<xsl:param name="lab_modified_date"/>
		<xsl:param name="lab_modified_by"/>
		<xsl:param name="lab_btn_edit"/>
		<xsl:param name="lab_btn_delete"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_btn_reset_all"/>
		<xsl:param name="lab_same_as_tarlrn"/>
		<xsl:param name="lab_by_self"/>
		<xsl:param name="lab_tab_publish"/>
		<xsl:param name="lab_tab_autoenrol"/>
		<xsl:param name="lab_all_group"/>
		<xsl:param name="lab_all_grade"/>
		<xsl:param name="lab_skill"/>
		<xsl:variable name="lab_title">
			<xsl:choose>
				<xsl:when test="$rule_type = 'TARGET_LEARNER'">
					<xsl:value-of select="$lab_target_learner"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$lab_target_enrollment"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="$is_new_cos = 'false'">
		    <xsl:call-template name="itm_action_nav">
				<xsl:with-param  name="cur_node_id">106</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
        <div class="wzb-item-main" id="wzb-item-main">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="$is_new_cos = 'true'">
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">
						<xsl:value-of select="//itm_action_nav/@itm_title"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">
						<xsl:value-of select="//itm_action_nav/@itm_title"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:apply-templates select="nav/item" mode="nav">
					<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
				</xsl:apply-templates>
				<span class="NavLink">
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:value-of select="$lab_title"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		
		<xsl:if test="$is_new_cos = 'false'">
			<xsl:variable name="lab_tab_2">
				<xsl:if test="item/@type = 'SELFSTUDY' or item/@type = 'VIDEO' or item/@type='MOBILE'">
					<xsl:value-of select="$lab_tab_autoenrol"/>
				</xsl:if>
			</xsl:variable>
			<xsl:if test="$rule_type = 'TARGET_LEARNER'">
				<xsl:call-template name="sys_gen_tab">
					<xsl:with-param name="tab_1" select="$lab_target_learner"/>
					<xsl:with-param name="tab_1_href">javascript:itm.get_target_rule(<xsl:value-of select="$itm_id"/>, 'TARGET_LEARNER');</xsl:with-param>
					<xsl:with-param name="tab_2" select="$lab_tab_2"/>
					<xsl:with-param name="tab_2_href">javascript:itm.get_item_auto_enrol(<xsl:value-of select="$itm_id"/>);</xsl:with-param>
					<xsl:with-param name="current_tab" select="$lab_target_learner"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
		<div class="margin-top14"></div>
		<table>
			<tr>
				<td align="left">
					<xsl:if test="item/@target_enrol_type">
						<input type="radio" name="target_enrol_type" value="ASSIGNED" id="by_self" onclick="itm.change_tarenrol_rule(document.frmResult, 'ASSIGNED')">
							<xsl:if test="item/@target_enrol_type = '' or item/@target_enrol_type = 'ASSIGNED'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<label for="by_self">
							<xsl:value-of select="$lab_by_self"/>
						</label>
						<input type="radio" name="target_enrol_type" value="TARGET_LEARNER" id="same_as_tarlrn" onclick="itm.change_tarenrol_rule(document.frmResult, 'TARGET_LEARNER')">
							<xsl:if test="item/@target_enrol_type = 'TARGET_LEARNER'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<label for="same_as_tarlrn">
							<xsl:value-of select="$lab_same_as_tarlrn"/>
						</label>
					</xsl:if>
				</td>
				<td align="right">
					<xsl:if test="item/@target_enrol_type = '' or item/@target_enrol_type = 'ASSIGNED'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_btn_reset_all"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:itm.change_tarenrol_rule(document.frmResult, 'ASSIGNED', true)</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="target_rule_lst/@type = 'TARGET_LEARNER' or item/@target_enrol_type = '' or item/@target_enrol_type = 'ASSIGNED'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_add_rule"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:itm.start_target_rule_prev(<xsl:value-of select="$itm_id"/>, '<xsl:value-of select="$rule_type"/>', 0, '<xsl:value-of select="$is_new_cos"/>');</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="count(target_rule_lst/target_rule)">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_export"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:itm.target_lrn_export(<xsl:value-of select="$itm_id"/>);</xsl:with-param>
					</xsl:call-template>
					</xsl:if>
				</td>
			</tr>
		</table>
		<xsl:apply-templates select="target_rule_lst">
			<xsl:with-param name="lab_no_rule">
				<xsl:value-of select="$lab_no_rule"/>
			</xsl:with-param>
			<xsl:with-param name="lab_no_enroll_rule">
				<xsl:value-of select="$lab_no_enroll_rule"/>
			</xsl:with-param>
			<xsl:with-param name="lab_rule">
				<xsl:value-of select="$lab_rule"/>
			</xsl:with-param>
			<xsl:with-param name="lab_modified_date">
				<xsl:value-of select="$lab_modified_date"/>
			</xsl:with-param>
			<xsl:with-param name="lab_modified_by">
				<xsl:value-of select="$lab_modified_by"/>
			</xsl:with-param>
			<xsl:with-param name="lab_btn_edit">
				<xsl:value-of select="$lab_btn_edit"/>
			</xsl:with-param>
			<xsl:with-param name="lab_btn_delete">
				<xsl:value-of select="$lab_btn_delete"/>
			</xsl:with-param>
			<xsl:with-param name="lab_all_group">
				<xsl:value-of select="$lab_all_group"/>
			</xsl:with-param>
			<xsl:with-param name="lab_all_grade">
				<xsl:value-of select="$lab_all_grade"/>
			</xsl:with-param>
			<xsl:with-param name="lab_skill">
				<xsl:value-of select="$lab_skill"/>
			</xsl:with-param>
		</xsl:apply-templates>
		</div>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template match="target_rule_lst">
		<xsl:param name="lab_no_rule"/>
		<xsl:param name="lab_no_enroll_rule"/>
		<xsl:param name="lab_rule"/>
		<xsl:param name="lab_enroll_rule"/>
		<xsl:param name="lab_modified_date"/>
		<xsl:param name="lab_modified_by"/>
		<xsl:param name="lab_btn_edit"/>
		<xsl:param name="lab_btn_delete"/>
		<xsl:param name="lab_all_group"/>
		<xsl:param name="lab_all_grade"/>
		<xsl:param name="lab_skill"/>
		<xsl:variable name="lab_no_lst">
			<xsl:choose>
				<xsl:when test="$rule_type = 'TARGET_LEARNER'">
					<xsl:value-of select="$lab_no_rule"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$lab_no_enroll_rule"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="not(target_rule)">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_lst"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td width="30%">
							<xsl:value-of select="$lab_rule"/>
						</td>
						<xsl:if test="@type = 'TARGET_LEARNER' or ../item/@target_enrol_type = '' or ../item/@target_enrol_type = 'ASSIGNED'">
							<td width="30%" align="center">
								<xsl:value-of select="$lab_modified_date"/>
							</td>
							<td width="20%">
								<xsl:value-of select="$lab_modified_by"/>
							</td>
						</xsl:if>
					</tr>
					<xsl:for-each select="target_rule">
						<tr>
							<td>
								<xsl:value-of select="$lab_group"/>
								<xsl:text>：&#160;</xsl:text>
								<xsl:for-each select="group_lst/group">
									<xsl:choose>
										<xsl:when test="@is_root_group = 'true'">
											<xsl:value-of select="$lab_all_group"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="@name"/>
											<xsl:if test="position() != last()">
												<xsl:text>、</xsl:text>
											</xsl:if>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:for-each>
								<br/>
								<xsl:value-of select="$lab_grade"/>
								<xsl:text>：&#160;</xsl:text>
								<xsl:for-each select="grade_lst/grade">
									<xsl:choose>
										<xsl:when test="@is_root_grade = 'true'">
											<xsl:value-of select="$lab_all_grade"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="@name"/>
											<xsl:if test="position() != last()">
												<xsl:text>、</xsl:text>
											</xsl:if>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:for-each>
								<xsl:if test="count(skill_lst/skill)!=0">
									<br/>
									<xsl:value-of select="$lab_skill"/>
									<xsl:text>：&#160;</xsl:text>
									<xsl:for-each select="skill_lst/skill">
										<xsl:value-of select="@name"/>
										<xsl:if test="position() != last()">
											<xsl:text>、</xsl:text>
										</xsl:if>
									</xsl:for-each>
								</xsl:if>
							</td>
							<xsl:if test="../@type = 'TARGET_LEARNER' or ../../item/@target_enrol_type = '' or ../../item/@target_enrol_type = 'ASSIGNED'">
								<td align="center">
									<xsl:call-template name="display_time">
										<xsl:with-param name="my_timestamp">
											<xsl:value-of select="@modified_date"/>
										</xsl:with-param>
									</xsl:call-template>
								</td>
								<td>
									<xsl:value-of select="@modified_by"/>
								</td>
								<td align="right">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name">
											<xsl:value-of select="$lab_btn_edit"/>
										</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">javascript:itm.start_target_rule_prev(<xsl:value-of select="$itm_id"/>, '<xsl:value-of select="$rule_type"/>', <xsl:value-of select="@id"/>, '<xsl:value-of select="$is_new_cos"/>');</xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="class">btn wzb-btn-blue</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_name">
											<xsl:value-of select="$lab_btn_delete"/>
										</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">javascript:itm.del_target_rule(<xsl:value-of select="$itm_id"/>, '<xsl:value-of select="$rule_type"/>', <xsl:value-of select="@id"/>, '<xsl:value-of select="@modified_date"/>');</xsl:with-param>
									</xsl:call-template>
								</td>
							</xsl:if>
						</tr>
					</xsl:for-each>
				</table>
			</xsl:otherwise>
		</xsl:choose>
		
		<xsl:if test="$is_new_cos = 'true'">
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_ok_btn"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:itm.get_item_detail(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
			</xsl:call-template>
		</div>
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
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
				<a href="javascript:itm.get_item_run_list({$value})" class="NavLink">
					<xsl:choose>
						<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
					</xsl:choose>
				</a>
				<span class="NavLink">&#160;&gt;&#160;</span>
				<a href="javascript:itm.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a href="javascript:itm.get_item_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ============================================================= -->
</xsl:stylesheet>
