<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:output indent="yes"/>
	<!-- ====================================================================================================== -->
	<xsl:variable name="plan_id" select="/applyeasy/plan/@id"/>
	<xsl:variable name="tcr_id" select="/applyeasy/plan/@tcr_id"/>
	<xsl:variable name="tcr_title" select="/applyeasy/plan/@tcr_title"/>
	<xsl:variable name="tpn_upd_timestamp" select="/applyeasy/plan/@tpn_upd_timestamp"/>
	<xsl:variable name="entrance" select="/applyeasy/plan/@entrance"/>
	<xsl:variable name="tpn_cos_type" select="/applyeasy/tpn_cos_type"/>
	<!-- ====================================================================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="applyeasy"/>
		</html>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template match="applyeasy">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_home.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_training_plan.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
				var goldenman = new wbGoldenMan;
				var tpplan = new wbTrainingPlan;		
				var entrance= getUrlParam('entrance');
				function change_impl_type(frm, type) {
					if (type == 'ADD') {		
						if (frm.ity_id.checked){
							frm.ity_id.disabled = true;
						} else {	
							for (var i = 0; i < frm.ity_id.length; i++) {
								frm.ity_id[i].disabled = true;	
							}														
						}		
						frm.genadditm_id.disabled = false;	
						frm.genremoveitm_id.disabled = false;
					} else if (type == 'NEW') {
						if (frm.ity_id.checked){
							frm.ity_id.disabled = false;
						} else {	
							for (var i = 0; i < frm.ity_id.length; i++) {
								frm.ity_id[i].disabled = false;	
							}														
						}	
						frm.genadditm_id.disabled = true;
						frm.genremoveitm_id.disabled = true;
					}
				}
													
				function init(frm){
					if (frm.impl_type_new.checked){
						if (frm.ity_id.checked){
							frm.ity_id.checked = true;
						} else {	
							frm.ity_id[0].checked = true;									
						}	
					}
					frm.genadditm_id.disabled = true;
					frm.genremoveitm_id.disabled = true;								
				}
			]]></script>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="init(frmXml)">
			<form name="frmXml">
				<input type="hidden" name="module"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="tcr_id" value="{$tcr_id}"/>
				<input type="hidden" name="plan_id" value="{$plan_id}"/>
				<input type="hidden" name="itm_dummy_type"/>
				<input type="hidden" name="training_plan" value="true"/>
				<input type="hidden" name="tpn_update_timestamp" value="{$tpn_upd_timestamp}"/>
				<input type="hidden" name="entrance" value="{$entrance}"/>
				<input type="hidden" name="tvw_id"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">培訓計劃實施</xsl:with-param>
			<xsl:with-param name="lab_how_to">如何實施該計劃?</xsl:with-param>
			<xsl:with-param name="lab_cos_type">計劃培訓類型</xsl:with-param>
			<xsl:with-param name="lab_new_course">建立一個全新的活動</xsl:with-param>
			<xsl:with-param name="lab_add_classroom">在以下已有的活動上建立班級</xsl:with-param>
			<xsl:with-param name="lab_add_classroom_desc">只適用有班別的活動</xsl:with-param>
			<xsl:with-param name="lab_name">活動名稱</xsl:with-param>
			<xsl:with-param name="lab_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_msg_no_type_sel">請選擇一個活動類型</xsl:with-param>
			<xsl:with-param name="lab_msg_no_name_spec">請指定活動名稱</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">培训计划实施</xsl:with-param>
			<xsl:with-param name="lab_how_to">如何实施该计划?</xsl:with-param>
			<xsl:with-param name="lab_cos_type">计划培训类型</xsl:with-param>
			<xsl:with-param name="lab_new_course">建立一个全新的活动</xsl:with-param>
			<xsl:with-param name="lab_add_classroom">在以下已有的活动上建立班级</xsl:with-param>
			<xsl:with-param name="lab_add_classroom_desc">只适用有班别的活动</xsl:with-param>
			<xsl:with-param name="lab_name">活动名称</xsl:with-param>
			<xsl:with-param name="lab_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_msg_no_type_sel">请选择一个活动类型</xsl:with-param>
			<xsl:with-param name="lab_msg_no_name_spec">请指定活动名称</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">Implement training</xsl:with-param>
			<xsl:with-param name="lab_how_to">How to implement this training?</xsl:with-param>
			<xsl:with-param name="lab_cos_type">Planned training type</xsl:with-param>
			<xsl:with-param name="lab_new_course">Add a brand new training</xsl:with-param>
			<xsl:with-param name="lab_add_classroom">Add a class under an existing training</xsl:with-param>
			<xsl:with-param name="lab_add_classroom_desc">applicable on Classroom / Blended Training only</xsl:with-param>
			<xsl:with-param name="lab_name">Select a training</xsl:with-param>
			<xsl:with-param name="lab_btn_next">Next</xsl:with-param>
			<xsl:with-param name="lab_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_msg_no_type_sel">Please select a training type</xsl:with-param>
			<xsl:with-param name="lab_msg_no_name_spec">Please select a training</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_how_to"/>
		<xsl:param name="lab_cos_type"/>
		<xsl:param name="lab_new_course"/>
		<xsl:param name="lab_add_classroom"/>
		<xsl:param name="lab_add_classroom_desc"/>
		<xsl:param name="lab_name"/>
		<xsl:param name="lab_btn_next"/>
		<xsl:param name="lab_btn_cancel"/>
		<xsl:param name="lab_msg_no_type_sel"/>
		<xsl:param name="lab_msg_no_name_spec"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_PLAN_CARRY_OUT</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_title"/>
		</xsl:call-template>
		<table>
			<tr>
				<td>
				</td>
				<td>
					<xsl:text>&#160;&#160;</xsl:text>
					<xsl:value-of select="$lab_how_to"/>
					<xsl:if test="$tpn_cos_type !=''">
						<span class="desc">
							<xsl:text>【&#160;&#160;</xsl:text>
							<xsl:value-of select="$lab_cos_type"/>
							<xsl:text>： </xsl:text>
							<xsl:value-of select="$tpn_cos_type"/>
							<xsl:text>&#160;&#160;】</xsl:text>
						</span>
					</xsl:if>
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td>
					<table>
						<tr>
							<td class="wzb-form-label" valign="top">
							</td>
							<td class="wzb-form-control">
								<input id="impl_type_new" type="radio" name="impl_type" checked="checked" onclick="change_impl_type(frmXml, 'NEW');"/>
								<input type="hidden" name="lab_msg_no_type_sel" value="{$lab_msg_no_type_sel}" />	
								<label for="impl_type_new">
									<xsl:value-of select="$lab_new_course"/>
								</label>
							</td>
						</tr>
						<xsl:for-each select="item_type_list/training_type[@id = 'COS' or @id = 'EXAM' or @id = 'INTEGRATED']">
							<xsl:for-each select="item">
							<tr>
								<td>
								</td>
								<td style="margin:0 0 5px 10px">
									<span style="margin:30px;">
										<xsl:variable name="item_id" select="@id"/>
										<xsl:variable name="dummy_type" select="@dummy_type"/>
									
										<input id="ity_{@sort}" type="radio" name="ity_id" value="{$item_id}" dummy_type="{$dummy_type}"/>
										<xsl:if test="@id = 'INTEGRATED'">
											<input type="hidden" name="itm_integrated_ind" value="true"/>
										</xsl:if>
										<label for="ity_{@sort}" style="margin-bottom:10px;">
											<xsl:call-template name="get_ity_title">
												<xsl:with-param name="dummy_type" select="$dummy_type"/>
											</xsl:call-template>
										</label>
									</span>
								</td>
							</tr>
							</xsl:for-each>
						</xsl:for-each>
						<tr>
							<td class="wzb-form-label" valign="top">
							</td>
							<td class="wzb-form-control">
								<input id="impl_type_add" type="radio" name="impl_type" onclick="change_impl_type(frmXml, 'ADD');"/>
								<input type="hidden" name="lab_msg_no_name_spec" value="{$lab_msg_no_name_spec}" />
								<label for="impl_type_add">
									<xsl:value-of select="$lab_add_classroom"/>
									<xsl:text>&#160;</xsl:text>
									<span style="color: blue;">(<xsl:value-of select="$lab_add_classroom_desc"/>)</span>
								</label>
							</td>
						</tr>
						<tr>
							<td class="wzb-form-label" valign="top">
							</td>
							<td align="left" class="wzb-form-control">
								<table>
									<tr>
										<td style="width:80px; margin:auto; "><xsl:value-of select="$lab_name"/>：</td>
										<td >
											<xsl:call-template name="wb_goldenman">
												<xsl:with-param name="field_name">itm_id</xsl:with-param>
												<xsl:with-param name="name">itm_id</xsl:with-param>
												<xsl:with-param name="tree_type">tc_catalog_item_run</xsl:with-param>
												<xsl:with-param name="box_size">1</xsl:with-param>
												<xsl:with-param name="select_type">5</xsl:with-param>
												<xsl:with-param name="args_type">row</xsl:with-param>
												<xsl:with-param name="complusory_tree">0</xsl:with-param>
												<xsl:with-param name="pick_root">0</xsl:with-param>
												<xsl:with-param name="parent_tcr_id">
													<xsl:value-of select="$tcr_id"/>
												</xsl:with-param>
											</xsl:call-template>
										</td>
									</tr>
								</table>
							</td>
							<input type="hidden" name="label_name" value="{$lab_name}"/>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:text>&#160;&#160;&#160;&#160;&#160;&#160;</xsl:text>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
					<xsl:value-of select="$lab_btn_next"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript: tpplan.impl_plan(frmXml);</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
					<xsl:value-of select="$lab_btn_cancel"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript: tpplan.get_plan_detail('<xsl:value-of select="$tcr_id"/>','<xsl:value-of select="$plan_id"/>','<xsl:value-of select="$entrance"/>');</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- ====================================================================================================== -->
</xsl:stylesheet>
