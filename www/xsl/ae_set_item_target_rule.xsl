<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<!-- share -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:variable name="itm_id" select="/item_target/item/@id"/>
	<xsl:variable name="itm_tcr_id" select="/item_target/item/@tcr_id"/>
	<xsl:variable name="rule_type" select="/item_target/target_rule/@type"/>
	<xsl:variable name="rule_id" select="/item_target/target_rule/@id"/>
	<xsl:variable name="is_new_cos" select="/item_target/is_new_cos"/>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<!-- ================================================================== -->
	<xsl:template match="/">
		<xsl:call-template name="main"/>
	</xsl:template>
	<!--=================================================================== -->
	<xsl:template name="main">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[
			      itm = new wbItem;
					goldenman = new wbGoldenMan;
					function changeMsg() {
						var preview_obj = parent.prview_target_rule;
						if(preview_obj && preview_obj.document.getElementsByTagName("div").length == 0) {
							preview_obj.changeMsg(true);
						}
					}
			    ]]></script>
			    <xsl:call-template name="new_css"/>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" style="width:100%">
				<form name="frmXml">
					<input type="hidden" name="module"/>
					<input type="hidden" name="cmd"/>
					<input type="hidden" name="url_success"/>
					<input type="hidden" name="url_failure"/>
					<input type="hidden" name="stylesheet"/>
					<input type="hidden" name="target_group_lst"/>
					<input type="hidden" name="target_grade_lst"/>
					<input type="hidden" name="target_skill_lst"/>
					<input type="hidden" name="itm_id" value="{$itm_id}"/>
					<input type="hidden" name="itm_target_type" value="{$rule_type}"/>
					<input type="hidden" name="rule_id" value="{$rule_id}"/>
					<input type="hidden" name="lab_group" value="{$lab_group}"/>
					<input type="hidden" name="lab_grade" value="{$lab_grade}"/>
					<input type="hidden" name="upd_timestamp" value="{/item_target/target_rule/@timestamp}"/>
					<input type="hidden" name="is_new_cos" value="{$is_new_cos}"/>
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- ========================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_add_rule">添加規則</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確認</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_preview">預覽</xsl:with-param>
			<xsl:with-param name="lab_all_group">所有<xsl:value-of select="$lab_group"/></xsl:with-param>
			<xsl:with-param name="lab_all_grade">所有<xsl:value-of select="$lab_grades"/></xsl:with-param>
			<xsl:with-param name="lab_skill">崗位</xsl:with-param>
			<xsl:with-param name="lab_key">推薦課程</xsl:with-param>
			<xsl:with-param name="lab_compulsory">是否必修</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_add_rule">添加规则</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_preview">预览</xsl:with-param>
			<xsl:with-param name="lab_all_group">所有<xsl:value-of select="$lab_group"/></xsl:with-param>
			<xsl:with-param name="lab_all_grade">所有<xsl:value-of select="$lab_grades"/></xsl:with-param>
			<xsl:with-param name="lab_skill">岗位</xsl:with-param>
			<xsl:with-param name="lab_key">推荐课程</xsl:with-param>
			
			<xsl:with-param name="lab_compulsory">是否必修</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_add_rule">Add rule</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_preview">Preview</xsl:with-param>
			<xsl:with-param name="lab_all_group">All groups</xsl:with-param>
			<xsl:with-param name="lab_all_grade">All grades</xsl:with-param>
			<xsl:with-param name="lab_skill">Position</xsl:with-param>
			<xsl:with-param name="lab_key">Recommended course</xsl:with-param>
			<xsl:with-param name="lab_compulsory">Set as compulsory course</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template name="content">
		<xsl:param name="lab_add_rule"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_preview"/>
		<xsl:param name="lab_all_group"/>
		<xsl:param name="lab_all_grade"/>
		<xsl:param name="lab_skill"/>
		<xsl:param name="lab_key"/>
		<xsl:param name="lab_compulsory"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<table>
			<!-- group -->
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_group"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="wb_goldenman">
						<xsl:with-param name="field_name">target_group</xsl:with-param>
						<xsl:with-param name="tree_type">user_group</xsl:with-param>
						<xsl:with-param name="select_type">1</xsl:with-param>
						<xsl:with-param name="pick_root">0</xsl:with-param>						
						<xsl:with-param name="box_size">5</xsl:with-param>
						<xsl:with-param name="width">250</xsl:with-param>
						<xsl:with-param name="extra_remove_function">changeMsg()</xsl:with-param>
						<xsl:with-param name="extra_add_function">changeMsg()</xsl:with-param>
						<xsl:with-param name="filter_user_group">1</xsl:with-param>						
						<xsl:with-param name="option_list">
							<xsl:for-each select="item_target/target_rule/group_lst/group">
								<option value="{@id}">
									<xsl:choose>
										<xsl:when test="@is_root_group = 'true'">
											<xsl:value-of select="$lab_all_group"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="@name"/>
										</xsl:otherwise>
									</xsl:choose>
								</option>
							</xsl:for-each>
						</xsl:with-param>
						<xsl:with-param name="parent_tcr_id">
							<xsl:value-of select="$itm_tcr_id"/>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td height="10" class="wzb-form-label">
				</td>
				<td class="wzb-form-control">
				<!-- <xsl:if test="$rule_type='TARGET_LEARNER'">
				<input type="checkbox" name="group_ind">
						<xsl:if test="item_target/target_rule/itr_group_ind = 1">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</input>
					<xsl:value-of select="$lab_key"/>
				</xsl:if> -->
					<p></p>
					<input type="hidden" name="itr_group_ind" value="{item_target/target_rule/itr_group_ind}"/>
				</td>
			</tr>
			<!-- grade -->
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="wzb-form-star">*</span>
					<xsl:value-of select="$lab_grade"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="wb_goldenman">
						<xsl:with-param name="field_name">target_grade</xsl:with-param>
						<xsl:with-param name="tree_type">grade</xsl:with-param>
						<xsl:with-param name="select_type">1</xsl:with-param>
						<xsl:with-param name="pick_root">1</xsl:with-param>
						<xsl:with-param name="box_size">5</xsl:with-param>
						<xsl:with-param name="width">250</xsl:with-param>
						<xsl:with-param name="extra_remove_function">changeMsg()</xsl:with-param>
						<xsl:with-param name="extra_add_function">changeMsg()</xsl:with-param>
						<xsl:with-param name="option_list">
							<xsl:for-each select="item_target/target_rule/grade_lst/grade">
								<option value="{@id}">
									<xsl:choose>
										<xsl:when test="@is_root_grade = 'true'">
											<xsl:value-of select="$lab_all_grade"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="@name"/>
										</xsl:otherwise>
									</xsl:choose>
								</option>
							</xsl:for-each>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td height="10" class="wzb-form-label">
				</td>
				<td class="wzb-form-control">
				<!-- 	<xsl:if test="$rule_type='TARGET_LEARNER'">
				<input type="checkbox" name="grade_ind">
						<xsl:if test="item_target/target_rule/itr_grade_ind = 1">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</input>
					<xsl:value-of select="$lab_key"/>
				</xsl:if> -->
				<p></p>
					<input type="hidden" name="itr_grade_ind" value="{item_target/target_rule/itr_grade_ind}"/>
				</td>
			</tr>	
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_skill"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="wb_goldenman">
						<xsl:with-param name="field_name">skill</xsl:with-param>
						<xsl:with-param name="tree_type">COMPETENCE_PROFILE</xsl:with-param>
						<xsl:with-param name="select_type">1</xsl:with-param>
						<xsl:with-param name="pick_root">0</xsl:with-param>
						<xsl:with-param name="box_size">5</xsl:with-param>
						<xsl:with-param name="width">250</xsl:with-param>
						<xsl:with-param name="extra_remove_function">changeMsg()</xsl:with-param>
						<xsl:with-param name="extra_add_function">changeMsg()</xsl:with-param>
						<xsl:with-param name="option_list">
							<xsl:for-each select="item_target/target_rule/skill_lst/skill">
								<option value="{@id}">
									<xsl:choose>
										<xsl:when test="@is_root_skill = 'true'">
											<xsl:value-of select="$lab_skill"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="@name"/>
										</xsl:otherwise>
									</xsl:choose>
								</option>
							</xsl:for-each>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td height="10" class="wzb-form-label">
				</td>
				<td class="wzb-form-control">
					<!-- <xsl:if test="$rule_type='TARGET_LEARNER'">
				<input type="checkbox" name="skill_ind">
						<xsl:if test="item_target/target_rule/itr_skill_ind = 1">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</input>
					<xsl:value-of select="$lab_key"/>
				</xsl:if> -->
				<p></p>
					<input type="hidden" name="itr_skill_ind" value="{item_target/target_rule/itr_skill_ind}"/>
				</td>
			</tr>
			
			<tr>
				<td class="wzb-form-label">
				<xsl:if test="$rule_type='TARGET_LEARNER'">
					<xsl:value-of select="$lab_compulsory"/>
					<xsl:text>：</xsl:text>
				</xsl:if>
						
				</td>
				<td class="wzb-form-control">
				<xsl:if test="$rule_type='TARGET_LEARNER'">
				 <xsl:choose>
				    	<xsl:when  test="item_target/target_rule/itr_compulsory_ind = 1">
				    			<input type="radio" name="itr_compulsory_ind" value = "1">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</input><xsl:value-of select="$lab_yes"/>
                                  &#160;&#160;
								<input type="radio" name="itr_compulsory_ind" value = "0"/><xsl:value-of select="$lab_no"/>
				    	</xsl:when>
				    	<xsl:otherwise>
				    			<input type="radio" name="itr_compulsory_ind" value = "1"/><xsl:value-of select="$lab_yes"/>
                                       &#160;&#160;
								<input type="radio" name="itr_compulsory_ind" value = "0">
										<xsl:attribute name="checked">checked</xsl:attribute>
								</input><xsl:value-of select="$lab_no"/>
				    	</xsl:otherwise>
				    </xsl:choose>
				</xsl:if>   
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_preview"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:itm.target_rule_list_preview(document.frmXml);</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_ok"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:itm.set_target_rule_exec(document.frmXml);</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_cancel"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:itm.get_target_rule(<xsl:value-of select="$itm_id"/>, '<xsl:value-of select="item_target/target_rule/@type"/>', '<xsl:value-of select="$is_new_cos"/>');</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- ============================================================================  -->
</xsl:stylesheet>
