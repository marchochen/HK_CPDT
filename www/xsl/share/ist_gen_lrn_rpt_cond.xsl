<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="content">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_tracking_report"/>
		<xsl:param name="lab_online_content"/>
		<xsl:param name="lab_search_criteria"/>
		<xsl:param name="lab_criteria_report"/>
		<xsl:param name="lab_data_type"/>
		<xsl:param name="lab_date_range"/>
		<xsl:param name="lab_no_restriction"/>
		<xsl:param name="lab_start_date"/>
		<xsl:param name="lab_end_date"/>
		<xsl:param name="lab_users_results_between"/>
		<xsl:param name="lab_view_report"/>
		<xsl:param name="lab_enrolled_users"/>
		<xsl:param name="lab_qr_users"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_any_date"/>
		<xsl:param name="lab_first_acc_date"/>
		<xsl:param name="lab_last_acc_date"/>
		<xsl:param name="lab_complete_date"/>
		<xsl:param name="lab_any_acc_date"/>
		<xsl:param name="lab_criteria_desc"/>
		<xsl:param name="nav_link"/>
		<xsl:param name="btn_view_rpt_link"/>
		<xsl:param name="parent_code"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_report.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
			rpt = new wbReport
			itm_lst = new wbItem
			itm_id = ]]><xsl:value-of select="$itm_id"/><![CDATA[
			wb_utils_set_cookie('itm_id', itm_id);

			function set_date_range_onchange(frmXml) {
					frmXml.start_datetime_yy.onchange = new Function("if(!isEnabled(document.frmXml.start_datetime_yy)) {document.frmXml.start_datetime_yy.value = ''; }");
					frmXml.start_datetime_mm.onchange = new Function("if(!isEnabled(document.frmXml.start_datetime_mm)) {document.frmXml.start_datetime_mm.value = ''; }");
					frmXml.start_datetime_dd.onchange = new Function("if(!isEnabled(document.frmXml.start_datetime_dd)) {document.frmXml.start_datetime_dd.value = ''; }");
					
					frmXml.end_datetime_yy.onchange = new Function("if(!isEnabled(document.frmXml.end_datetime_yy)) {document.frmXml.end_datetime_yy.value = ''; }");
					frmXml.end_datetime_mm.onchange = new Function("if(!isEnabled(document.frmXml.end_datetime_mm)) {document.frmXml.end_datetime_mm.value = ''; }");
					frmXml.end_datetime_dd.onchange = new Function("if(!isEnabled(document.frmXml.end_datetime_dd)) {document.frmXml.end_datetime_dd.value = ''; }");
			}

			function disable_date_range(frmXml){
					n = frmXml.date_range_subtype.length
					for (i = 0; i < n  ; i++){
						frmXml.date_range_subtype[i].disabled= 'disabled';
						frmXml.date_range_subtype[i].checked= '';
					}
					frmXml.start_datetime_yy.disabled = 'disabled';
					frmXml.start_datetime_yy.value = '';
					frmXml.start_datetime_mm.disabled = 'disabled';
					frmXml.start_datetime_mm.value = '';
					frmXml.start_datetime_dd.disabled = 'disabled';
					frmXml.start_datetime_dd.value = '';

					frmXml.end_datetime_yy.disabled = 'disabled';
					frmXml.end_datetime_yy.value = '';
					frmXml.end_datetime_mm.disabled = 'disabled';
					frmXml.end_datetime_mm.value = '';
					frmXml.end_datetime_dd.disabled = 'disabled';
					frmXml.end_datetime_dd.value = '';
			}
			
			function enable_date_range(frmXml){
					n = frmXml.date_range_subtype.length
					for (i = 0; i < n  ; i++){
						frmXml.date_range_subtype[i].disabled= '';
						frmXml.date_range_subtype[i].checked= 'checked';
					}
					frmXml.date_range_subtype[frmXml.date_range_subtype.length-1].checked= 'checked';
					frmXml.start_datetime_yy.disabled = '';
					frmXml.start_datetime_mm.disabled = '';
					frmXml.start_datetime_dd.disabled = '';
					frmXml.end_datetime_yy.disabled = '';
					frmXml.end_datetime_mm.disabled = '';
					frmXml.end_datetime_dd.disabled = '';
			}

			function selectTimeRange(frmXml){
				$('#date_range_ind_true').attr('checked', 'checked')
				enable_date_range(frmXml);
			}

			function radioHandler(obj) 
			{
				if(isEnabled(obj)) {
					return true;
				} else {
					obj.checked = '';
					return true;
				}
			}

			function isEnabled(obj)
			{
				return !obj.disabled;
			}

			]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="disable_date_range(document.frmXml); set_date_range_onchange(document.frmXml);">
			<form name="frmXml">
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
					<xsl:with-param name="parent_code" select="$parent_code"/>
				</xsl:call-template>
		      <xsl:call-template name="itm_action_nav">
				<xsl:with-param  name="cur_node_id">117</xsl:with-param>
			</xsl:call-template>
			<div class="wzb-item-main">
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_tracking_report"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_nav_link">
					<xsl:with-param name="text">
					<xsl:copy-of select="$nav_link"/>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="tracking_rpt_gen_tab">
					<xsl:with-param name="rpt_target_tab">1</xsl:with-param>
					<xsl:with-param name="itm_id" select="$itm_id"/>
				</xsl:call-template>
				<table>
					<xsl:choose>
						<xsl:when test="$itm_can_qr_ind = 'true'">
							<tr>
								<td class="wzb-form-label">
									<xsl:value-of select="$lab_data_type"/>：
								</td>
								<td class="wzb-form-control">
									<input type="radio" id="rpt_type_app" name="rpt_type" value="APP" checked="checked"/>
									<label for="rpt_type_app">
										<xsl:value-of select="$lab_enrolled_users"/>
									</label>
									<br/>
									<input type="radio" id="rpt_type_qr" name="rpt_type" value="QR"/>
									<label for="rpt_type_qr">
										<xsl:value-of select="$lab_qr_users"/>
									</label>
									<br/>
								</td>
							</tr>
						</xsl:when>
						<xsl:otherwise>
							<input type="hidden" name="rpt_type" value="APP"/>
						</xsl:otherwise>
					</xsl:choose>
					<tr>
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$lab_date_range"/>：
						</td>
						<td class="wzb-form-control">
							<input type="radio" id="date_range_ind_false" name="date_range_ind" value="FALSE" checked="checked" onclick="javascript:disable_date_range(document.frmXml)"/>
							<label for="date_range_ind_false">
								<xsl:value-of select="$lab_no_restriction"/>
							</label>
							<br/>
							<input type="radio" id="date_range_ind_true" name="date_range_ind" value="TRUE" onclick="javascript:enable_date_range(document.frmXml)"/>
							<label for="date_range_ind_true">
								<xsl:value-of select="$lab_users_results_between"/>：
							</label>
								<table style="margin-left:22px">
									<tr>
										<td width="70" >
											<xsl:value-of select="$lab_start_date"/>：
										</td>
										<td class="wzb-form-control">
											<xsl:call-template name="display_form_input_time">
												<xsl:with-param name="fld_name">start_datetime</xsl:with-param>
												<xsl:with-param name="hidden_fld_name">start_datetime</xsl:with-param>
												<xsl:with-param name="show_label">Y</xsl:with-param>
												<xsl:with-param name="clickAction">selectTimeRange(document.frmXml)</xsl:with-param>
											</xsl:call-template>
										</td>
									</tr>
									<tr>
										<td>
											<xsl:value-of select="$lab_end_date"/>：
										</td>
										<td class="wzb-form-control">
											<xsl:call-template name="display_form_input_time">
												<xsl:with-param name="fld_name">end_datetime</xsl:with-param>
												<xsl:with-param name="hidden_fld_name">end_datetime</xsl:with-param>
												<xsl:with-param name="show_label">Y</xsl:with-param>
												<xsl:with-param name="clickAction">selectTimeRange(document.frmXml)</xsl:with-param>
											</xsl:call-template>
										</td>
									</tr>
									<tr>
										<td></td>
										<td >
											<input type="radio" id="date_range_subtype_COMMENCE" name="date_range_subtype" value="COMMENCE" onclick="return radioHandler(this);"/>
											<label for="date_range_subtype_COMMENCE">
												<xsl:value-of select="$lab_first_acc_date"/>
											</label>
											<br/>
											<input type="radio" id="date_range_subtype_LAST_ACC" name="date_range_subtype" value="LAST_ACC" onclick="return radioHandler(this);"/>
											<label for="date_range_subtype_LAST_ACC">
												<xsl:value-of select="$lab_last_acc_date"/>
											</label>
											<br/>
											<input type="radio" id="date_range_subtype_COMPLETE" name="date_range_subtype" value="COMPLETE" onclick="return radioHandler(this);"/>
											<label for="date_range_subtype_COMPLETE">
												<xsl:value-of select="$lab_complete_date"/>
											</label>
											<br/>
											<input type="radio" id="date_range_subtype_ANY" name="date_range_subtype" value="ANY" onclick="return radioHandler(this);"/>
											<label for="date_range_subtype_ANY">
												<xsl:value-of select="$lab_any_acc_date"/>
											</label>
										</td>
									</tr>
								</table>
						</td>
					</tr>
				</table>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_view_report"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:<xsl:value-of select="$btn_view_rpt_link"/>(frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
					</xsl:call-template>
				</div>
				<input name="cmd" type="hidden"/>
				<input name="course_id" type="hidden" value="{$course_id}"/>
				<input name="rpt_date_type" type="hidden"/>
				<input name="url_failure" type="hidden"/>
				<input name="stylesheet" type="hidden"/>
				<input name="cur_page" type="hidden" value="1"/>
				<input name="page_size" type="hidden" value="20"/>
			</div>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================================================== -->
	<xsl:template match="cur_usr"/>
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
</xsl:stylesheet>
