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
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:output indent="yes"/>
	<!-- ====================================================================================================== -->
	<xsl:variable name="ysg_tcr_id" select="year_setting/setting/ysg_tcr_id"/>
	<xsl:variable name="ysg_year" select="year_setting/setting/ysg_year"/>
	<!-- ====================================================================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="year_setting"/>
		</html>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template match="year_setting">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_home.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_training_plan.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			plan = new wbTrainingPlan;
			
			function show_content(tcr_id) {
				if(tcr_id > 0) {
					var year;
					if(document.frmXml.year) {
						year = document.frmXml.year.value;
					} else {
						year = '';
					}
					plan.get_plan_config(year, tcr_id);
				}
			}
			
			]]></script>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="">
			<form name="frmXml">
				<input type="hidden" name="tcr_id" value="{$ysg_tcr_id}"/>
				<input type="hidden" name="ysg_update_timestamp" value="{setting/ysg_update_timestamp}"/>
				<input type="hidden" name="ysg_child_tcr_id_lst"/>
				<input type="hidden" name="module"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="url_success"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_year_setting_title">年度設置</xsl:with-param>
			<xsl:with-param name="lab_year_setting_desc">您可以通過年度設置，讓下級培訓中心的管理員在指定時間提交年度培訓計劃。</xsl:with-param>
			<xsl:with-param name="lab_plan_year">計劃年度</xsl:with-param>
			<xsl:with-param name="lab_tc">培訓中心</xsl:with-param>
			<xsl:with-param name="lab_year">年</xsl:with-param>
			<xsl:with-param name="lab_start_time">提交開始時間</xsl:with-param>
			<xsl:with-param name="lab_end_time">提交截止時間</xsl:with-param>
			<xsl:with-param name="lab_sub_tc">下級培訓中心</xsl:with-param>
			<xsl:with-param name="lab_sel_all">全選</xsl:with-param>
			<xsl:with-param name="wb_ui_show_no_item">暫無下級培訓中心</xsl:with-param>
			<xsl:with-param name="wb_ui_show_no_tc">沒有可管理培訓中心</xsl:with-param>
			<xsl:with-param name="lab_btn_save">儲存</xsl:with-param>
			<xsl:with-param name="lab_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_year_setting_title">年度设置</xsl:with-param>
			<xsl:with-param name="lab_year_setting_desc">您可以通过年度设置，让下级培训中心的管理员在指定时间提交年度培训计划。</xsl:with-param>
			<xsl:with-param name="lab_plan_year">计划年度</xsl:with-param>
			<xsl:with-param name="lab_tc">培训中心</xsl:with-param>
			<xsl:with-param name="lab_year">年</xsl:with-param>
			<xsl:with-param name="lab_start_time">提交开始时间</xsl:with-param>
			<xsl:with-param name="lab_end_time">提交截止时间</xsl:with-param>
			<xsl:with-param name="lab_sub_tc">下级培训中心</xsl:with-param>
			<xsl:with-param name="lab_sel_all">全选</xsl:with-param>
			<xsl:with-param name="wb_ui_show_no_item">暂无下级培训中心</xsl:with-param>
			<xsl:with-param name="wb_ui_show_no_tc">没有可管理培训中心</xsl:with-param>
			<xsl:with-param name="lab_btn_save">保存</xsl:with-param>
			<xsl:with-param name="lab_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_year_setting_title">Annual setting</xsl:with-param>
			<xsl:with-param name="lab_year_setting_desc">You can define the annual training plan submission period for your subordinate training center.</xsl:with-param>
			<xsl:with-param name="lab_plan_year">Year</xsl:with-param>
			<xsl:with-param name="lab_tc">Training center</xsl:with-param>
			<xsl:with-param name="lab_year">Year</xsl:with-param>
			<xsl:with-param name="lab_start_time">Submission start date</xsl:with-param>
			<xsl:with-param name="lab_end_time">Submission due date</xsl:with-param>
			<xsl:with-param name="lab_sub_tc">Subordinate training center</xsl:with-param>
			<xsl:with-param name="lab_sel_all">Select all</xsl:with-param>
			<xsl:with-param name="wb_ui_show_no_item">No subordinate training centers</xsl:with-param>
			<xsl:with-param name="wb_ui_show_no_tc">No responsible training centers</xsl:with-param>
			<xsl:with-param name="lab_btn_save">Save</xsl:with-param>
			<xsl:with-param name="lab_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_year_setting_title"/>
		<xsl:param name="lab_year_setting_desc"/>
		<xsl:param name="lab_tc"/>
		<xsl:param name="lab_plan_year"/>
		<xsl:param name="lab_year"/>
		<xsl:param name="lab_start_time"/>
		<xsl:param name="lab_end_time"/>
		<xsl:param name="lab_sub_tc"/>
		<xsl:param name="lab_sel_all"/>
		<xsl:param name="wb_ui_show_no_item"/>
		<xsl:param name="wb_ui_show_no_tc"/>
		<xsl:param name="lab_btn_save"/>
		<xsl:param name="lab_btn_cancel"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_YEAR_SETTING</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_year_setting_title"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_year_setting_desc"/>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="my_charge_tc_lst/tc">
				<table>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_tc"/>
							<xsl:text>：</xsl:text>
						</td>
						<td class="wzb-form-control">
							<select name="cur_tc" class="wzb-form-select" onchange="javascript:plan.get_plan_config('{$ysg_year}', this.value)">
								<xsl:for-each select="my_charge_tc_lst/tc">
									<option value="{@tcr_id}">
										<xsl:if test="@tcr_id = ../../cur_tc_id">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="text()"/>
									</option>
								</xsl:for-each>
								<xsl:value-of select="$lab_year"/>
							</select>
						</td>
					</tr>
					<xsl:choose>
						<xsl:when test="child_tc_lst/tc">
							<tr>
								<td class="wzb-form-label">
									<xsl:value-of select="$lab_plan_year"/>
									<xsl:text>：</xsl:text>
								</td>
								<td class="wzb-form-control">
									<select name="year" class="wzb-form-select" onchange="javascript:plan.get_plan_config(this.value, '{$ysg_tcr_id}')">
										<xsl:for-each select="year_option/option">
											<option value="{text()}">
												<xsl:if test="text() = ../../setting/ysg_year">
													<xsl:attribute name="selected">selected</xsl:attribute>
												</xsl:if>
												<xsl:value-of select="text()"/>
											</option>
										</xsl:for-each>
										<xsl:value-of select="$lab_year"/>
									</select>
								</td>
							</tr>
							<tr>
								<td class="wzb-form-label">
									<input type="hidden" name="lab_start_time" value="{$lab_start_time}"/>
									<span class="wzb-form-star">*</span>
									<xsl:value-of select="$lab_start_time"/>
									<xsl:text>：</xsl:text>
								</td>
								<td class="wzb-form-control">
									<xsl:call-template name="display_form_input_time">
										<xsl:with-param name="fld_name">submit_from</xsl:with-param>
										<xsl:with-param name="hidden_fld_name">submit_from</xsl:with-param>
										<xsl:with-param name="show_label">Y</xsl:with-param>
										<xsl:with-param name="timestamp">
											<xsl:value-of select="setting/ysg_submit_start_datetime"/>
										</xsl:with-param>
									</xsl:call-template>
									<input type="hidden" name="lab_start_date" value="{$lab_start_time}"/>
									<input type="hidden" name="lab_end_date" value="{$lab_end_time}"/>
								</td>
							</tr>
							<tr>
								<td class="wzb-form-label">
									<input type="hidden" name="lab_end_time" value="{$lab_end_time}"/>
									<span class="wzb-form-star">*</span>
									<xsl:value-of select="$lab_end_time"/>
									<xsl:text>：</xsl:text>
								</td>
								<td class="wzb-form-control">
									<xsl:call-template name="display_form_input_time">
										<xsl:with-param name="fld_name">submit_to</xsl:with-param>
										<xsl:with-param name="hidden_fld_name">submit_to</xsl:with-param>
										<xsl:with-param name="show_label">Y</xsl:with-param>
										<xsl:with-param name="timestamp">
											<xsl:value-of select="setting/ysg_submit_end_datetime"/>
										</xsl:with-param>
									</xsl:call-template>
								</td>
							</tr>
							<tr>
								<td class="wzb-form-label" valign="top">
									<span class="wzb-form-star">*</span>
									<xsl:value-of select="$lab_sub_tc"/>
									<xsl:text>：</xsl:text>
								</td>
								<td class="wzb-form-control">
									<table>
										<tr>
											<td>
												<label for="sel_all">
												<xsl:call-template name="select_all_checkbox">
													<xsl:with-param name="display_icon">false</xsl:with-param>
													<xsl:with-param name="sel_all_chkbox_nm">sel_all_checkbox</xsl:with-param>
													<xsl:with-param name="frm_name">frmXml</xsl:with-param>
												</xsl:call-template>
												<xsl:value-of select="$lab_sel_all"/>
												</label>
											</td>
										</tr>
										<tr>
											<td>
												<table>
													<xsl:for-each select="child_tc_lst/tc">
														<xsl:if test="(position()-1) mod 3 = 0">
															<xsl:text disable-output-escaping="yes">&lt;tr &gt;</xsl:text>
														</xsl:if>
 
														<td width="348">
															<xsl:variable name="cur_tcr_id" select="@tcr_id"/>
															<label for="subtc_checkbox_{$cur_tcr_id}">
																<input id="subtc_checkbox_{@tcr_id}" type="checkbox" name="subtc_id_{$cur_tcr_id}" value="{$cur_tcr_id}">
																	<xsl:for-each select="../../setting/sel_child_tc_lst/tc">
																		<xsl:if test="@tcr_id = $cur_tcr_id">
																			<xsl:attribute name="checked">checked</xsl:attribute>
																		</xsl:if>
																	</xsl:for-each>
																</input>
																<xsl:value-of select="text()"/>
															</label>
														</td>
														<xsl:if test="position() mod 3 = 0">
															<xsl:text disable-output-escaping="yes">&lt;/tr &gt;</xsl:text>
														</xsl:if>
													</xsl:for-each>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td class="wzb-form-label"></td>
								<td class="wzb-ui-module-text">
									<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
								</td>
							</tr>
							<table>
								<tr>
									<td>
										<div class="wzb-bar">
											<xsl:call-template name="wb_gen_form_button">
												<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_save"/>
												<xsl:with-param name="wb_gen_btn_href">javascript:plan.save_plan_config(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
											</xsl:call-template>
											<xsl:call-template name="wb_gen_form_button">
												<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_cancel"/>
												<xsl:with-param name="wb_gen_btn_href">javascript:wb_utils_gen_home(true)</xsl:with-param>
											</xsl:call-template>
										</div>
									</td>	
								</tr>
							</table>
						</xsl:when>
						<xsl:otherwise>
							<tr>
								<td align="center" colspan = "2" style="border-bottom:0px">
									<xsl:call-template name="wb_ui_show_no_item">
										<xsl:with-param name="text" select="$wb_ui_show_no_item" />
									</xsl:call-template>
								</td>
							</tr>
						</xsl:otherwise>
					</xsl:choose>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text">
						<xsl:value-of select="$wb_ui_show_no_tc"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================================================================================================== -->
</xsl:stylesheet>
