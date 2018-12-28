<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="utils/wb_utils_process.xsl"/>
	<xsl:output indent="yes"/>
	<!-- 请选择LABEL -->
	<xsl:variable name="lab_g_select_ls" select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_select_ls']"/>
	<xsl:variable name="lab_g_select_enrol_wf" select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_select_enrol_wf']"/>
	<xsl:variable name="lab_g_enrol_wf" select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_enrol_wf']"/>
	<xsl:variable name="lab_g_none" select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_none']"/>
	<xsl:variable name="lab_g_form_btn_next_step" select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_form_btn_next_step']"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_form_btn_cancel']"/>
	<xsl:variable name="cur_training_type">
		<xsl:choose>
			<xsl:when test="/applyeasy/cur_training_type ='COS'">COS</xsl:when><!-- 课程 -->
			<xsl:when test="/applyeasy/cur_training_type ='EXAM'">EXAM</xsl:when><!-- 考试 -->
			<xsl:when test="/applyeasy/cur_training_type ='REF'">REF</xsl:when><!--公开课  -->
			<xsl:when test="/applyeasy/cur_training_type ='INTEGRATED'">INTEGRATED</xsl:when><!-- 项目式培训 -->
			<xsl:otherwise>ALL</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:variable name="itm_dummy_type" select="/applyeasy/itm_dummy_type"/>
	<xsl:variable name="itm_type" select="/applyeasy/itm_type"/>
	<xsl:variable name="parent_code">
		<xsl:choose>
			<xsl:when test="/applyeasy/cur_training_type ='COS'">FTN_AMD_ITM_COS_MAIN</xsl:when>
			<xsl:when test="/applyeasy/cur_training_type ='EXAM'">FTN_AMD_EXAM_MGT</xsl:when>
			<xsl:when test="/applyeasy/cur_training_type ='REF'"></xsl:when>
			<xsl:when test="/applyeasy/cur_training_type ='INTEGRATED'">FTN_AMD_ITM_COS_MAIN</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- 是否显示选择创建的课程类型，例如网上课程，离线课程(true显示，false不显示) -->
	<xsl:variable name="is_integrated">true</xsl:variable>

	<!-- =============================================================== -->
	<xsl:template match="/applyeasy">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				var itm_lst = new wbItem;
				itemWorkflow = new Array();
				itemWorkflow[0] = ''; //Dummy value, for position() is start from 1, so ignore the value in position 0			
				function checkValidTemplate(index){
					if( itemWorkflow[index].length > 0 ) {
						if( itemWorkflow[index].length == 1 ){
							for(i=0; i<document.frmXml.wrk_tpl_id.length; i++) {
								if( document.frmXml.wrk_tpl_id[i].value == itemWorkflow[index][0] ) {
									if(document.frmXml.wrk_tpl_id[i].id == 'wrk_tpl_id_'+document.frmXml.wrk_tpl_id[i].value+'_no_approval' ) {
										document.frmXml.wrk_tpl_id[i].checked = true;
									} 
									document.frmXml.wrk_tpl_id[i].disabled = false;
								} else {
									document.frmXml.wrk_tpl_id[i].checked = false;
									document.frmXml.wrk_tpl_id[i].disabled = true;
								}
								disableChk(null, document.frmXml.wrk_tpl_id[i].value);
							}
						} else {
							for(i=0; i<document.frmXml.wrk_tpl_id.length; i++) {
							//alert(' i = ' + i + ' : ' + itemWorkflow[index] + ' , ' +  document.frmXml.wrk_tpl_id[i].value );
								if( checkPos(itemWorkflow[index], document.frmXml.wrk_tpl_id[i].value ) == -1 ) {
									document.frmXml.wrk_tpl_id[i].checked = false;
									document.frmXml.wrk_tpl_id[i].disabled = true;
								} else {
									document.frmXml.wrk_tpl_id[i].disabled = false;
									if( checkPos(itemWorkflow[index], document.frmXml.wrk_tpl_id[i].value )  == 0 )
										document.frmXml.wrk_tpl_id[i].checked = true;
								}
								disableChk(null, document.frmXml.wrk_tpl_id[i].value);
							}
						}
					} else {
						disableWorkflowRadioBtn();
						// handle the case when there is only 1 radio button and thus is not an array.
						if (document.frmXml.wrk_tpl_id.length) {
							document.frmXml.wrk_tpl_id[document.frmXml.wrk_tpl_id.length - 1].checked = true;
						}
						else {
							document.frmXml.wrk_tpl_id.checked = true;
						}
						
						// handle the case when there is only 1 radio button and thus is not an array.
						if (document.frmXml.wrk_tpl_id.length) {
							document.frmXml.wrk_tpl_id[document.frmXml.wrk_tpl_id.length - 1].disabled = false;
						}
						else {
							document.frmXml.wrk_tpl_id.disabled = false;
						}
					}
					return;
				}
				
				function checkPos(arg1, arg2){
					for(j=0; j<arg1.length; j++){
						if( arg1[j] == arg2 ) {
							return j;
						}
					}
					return  -1;
				}
				
				function disableWorkflowRadioBtn(){
				if(]]><xsl:value-of select="$is_integrated='false'"/><![CDATA[) {
					for(i=0; i<document.frmXml.wrk_tpl_id.length; i++) {
						document.frmXml.wrk_tpl_id[i].disabled = true;
						disableChk(null, document.frmXml.wrk_tpl_id[i].value);
					}
				} else {
					document.frmXml.wrk_tpl_id[0].checked = true;
					var len = document.frmXml.wrk_tpl_id.length;
					for(i=0; i<len; i++) {
						document.frmXml.wrk_tpl_id[len-1].disabled = true;
					}
					disableChk(null, document.frmXml.wrk_tpl_id[1].value);
					
				}
					return;
				}

				function radioHandler(obj) 
				{
					if(isEnabled(obj)) {
						return true;
					} else {
						obj.checked = false;
						return true;
					}
				}
			
				function isEnabled(obj)
				{
					return !obj.disabled;
				}
				
				function enableChk(obj, tpl_id) {
					if(obj && !isEnabled(obj)) {
						return false;
					}
					else {
						chk_supervise = eval('document.frmXml.chk_'+tpl_id+'_supervise');
						chk_tadm = eval('document.frmXml.chk_'+tpl_id+'_tadm');
						if(chk_supervise) {
							chk_supervise.disabled = false;
						}
						if(chk_tadm) {
							chk_tadm.disabled = false;
						}
						//enableRdo(null, tpl_id);
						return true;
					}
				}

				function disableChk(obj, tpl_id) {
					if(obj && !isEnabled(obj)) {
						return false;
					}
					else {
						chk_supervise = eval('document.frmXml.chk_'+tpl_id+'_supervise');
						chk_tadm = eval('document.frmXml.chk_'+tpl_id+'_tadm');
						if(chk_supervise) {
							chk_supervise.disabled = true;
							chk_supervise.checked = false;
						}
						if(chk_tadm) {
							chk_tadm.disabled = true;
							chk_tadm.checked = false;
						}
						if(chk_supervise) {
							disableRdo(null, tpl_id);
						}
						return true;
					}
				}

				function enableRdo(obj, tpl_id) {
					if(obj && !isEnabled(obj)) {
						return false;
					}
					else {
						rdo_ds = eval('document.frmXml.rdo_'+tpl_id+'[0]');
						rdo_ds_gs = eval('document.frmXml.rdo_'+tpl_id+'[1]');
						if(rdo_ds) {
							rdo_ds.disabled = false;
						}
						if(rdo_ds_gs) {
							rdo_ds_gs.disabled = false;
						}
						return true;
					}
				}

				function disableRdo(obj, tpl_id) {
					if(obj && !isEnabled(obj)) {
						return false;
					} else {
						rdo_ds = eval('document.frmXml.rdo_'+tpl_id+'[0]');
						rdo_ds_gs = eval('document.frmXml.rdo_'+tpl_id+'[1]');
						if(rdo_ds) {
							rdo_ds.checked = false;
							rdo_ds.disabled = true;
						}
						if(rdo_ds_gs) {
							rdo_ds_gs.checked = false;
							rdo_ds_gs.disabled = true;
						}
						return true;
					}
				}
				
				function toggleRdo(obj, tpl_id) {
					if(obj.checked) {
						return enableRdo(obj, tpl_id);
					} else {
						return disableRdo(obj, tpl_id);
					}
				}
				
				function checkSingleOption() {
					]]><xsl:if test="count(/applyeasy/item_type_list/item) = 1"><![CDATA[
					checkValidTemplate(1);
					]]></xsl:if><![CDATA[
				}
				
			]]></SCRIPT>
		</head>
		<BODY leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="javascript:disableWorkflowRadioBtn();checkSingleOption();">
			<FORM name="frmXml">
				<xsl:call-template name="content"/>
			</FORM>
		</BODY>
	</xsl:template>
	<!--	=============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_select_solutions"/>
		
		<xsl:variable name="lab_g_create">
			<!-- 获取新增文字 -->
			<xsl:call-template name="get_lab">
				<xsl:with-param name="lab_title">create</xsl:with-param>
			</xsl:call-template>
			
			<!-- 获取需要创建的课程类型文字 -->
			<xsl:variable name="type_name">
				<xsl:call-template name="get_lab">
					<xsl:with-param name="lab_title" select="/applyeasy/cur_training_type"/>
				</xsl:call-template>
			</xsl:variable>
			
			<!-- 获取需要创建的课程类型文字替换成相应的大小写 -->	
			<xsl:value-of select="translate($type_name,$uppercase,$lowercase)"/>
		</xsl:variable>
		
		<!-- 合并文字成为标题-->
		<xsl:variable name="lab_g_create_ls">
			<xsl:value-of select="concat($lab_g_create,' - 第一步：选择类型及报名工作流')"></xsl:value-of>
		</xsl:variable>
		
		<xsl:variable name="lab_g_ls">
			<!-- 获取需要创建的课程类型文字 -->
			<xsl:call-template name="get_lab">
				<xsl:with-param name="lab_title" select="/applyeasy/cur_training_type"/>
			</xsl:call-template>
			<xsl:if test="$wb_lang = 'en'">&#160;</xsl:if>
			
			<!--获取类型文字 -->
			<xsl:variable name="type_name">
				<xsl:call-template name="get_lab">
					<xsl:with-param name="lab_title">lab_type</xsl:with-param>
				</xsl:call-template>
			</xsl:variable>	
			<xsl:value-of select="translate($type_name,$uppercase,$lowercase)"/>	
		</xsl:variable>
		<!-- Check whether any item template has no workflow template -->
		<xsl:variable name="show_no_wf_template">
			<xsl:for-each select="item_type_list/training_type/item">
					<xsl:if test="not(template_type_list/template_type[@id=1])">true</xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
			<xsl:with-param name="page_title"><xsl:value-of select="$lab_g_create"></xsl:value-of></xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_g_create_ls"/>
		</xsl:call-template>
		<xsl:call-template name="wb_utils_process">
			<xsl:with-param name="itm_type" select="$itm_type"/>
			<xsl:with-param name="cur_tabs">1</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
		<xsl:when test="$is_integrated = 'false'"><!-- 如果不是项目式培训 -->
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_g_ls"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		
		<table class="Bg" width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<IMG border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
		</table>
		<table class="Bg" cellspacing="0" cellpadding="3" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td width="20%" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="10px"><img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/></td>
				<td width="80%" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
			<tr>
				<td colspan="3">
					<table cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td width="120"><img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/></td>
							<td>
								<span class="TitleText">
									<xsl:value-of select="$lab_g_select_ls"/>
									<xsl:text>：</xsl:text>
								</span>
							</td>
						</tr>
					</table>	

				</td>
			</tr>
			<tr>
				<td height="10" colspan="3">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
			<!-- 循环查找需要创建的课程类型 -->
			<xsl:for-each select="item_type_list/training_type[@id = $cur_training_type or $cur_training_type ='ALL']">
				<xsl:variable name="training_id" select="@id"/>
				
				<tr>
					<td align="right">
							<span class="TitleText" >
								<span class="Text">
									<xsl:call-template name="get_lab">
										<xsl:with-param name="lab_title" select="@id"/><!-- 课程，或者考试，项目式培训 -->
									</xsl:call-template>
									<xsl:text>：</xsl:text>
								</span>
							</span>
					</td>
					<td colspan="2"></td>			
				</tr>
				<!-- 循环加载出课程的子类型，例如课程分网上课程，和离线课程 -->
				<xsl:for-each select="item">
					<!-- 课程的子类型 -->
					<xsl:variable name="dummy_type" select="@dummy_type"></xsl:variable>
					<!-- 类型ID -->
					<xsl:variable name="item_id" select="@id"/>
					<!-- 是否为考试 -->
					<xsl:variable name="itm_exam_ind" select="@exam_ind"/>
					<!-- 是否为公开课程 -->
					<xsl:variable name="itm_ref_ind" select="@ref_ind"/>
					<!-- 是否为混合课程 -->
					<xsl:variable name="itm_blend_ind" select="@blend_ind"/>
					<!-- 排序 -->
					<xsl:variable name="sort" select="@sort"/>
					<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
							template_id = new Array();
						]]></script>
					<xsl:choose>
						<xsl:when test="count( template_type_list/template_type[@title='WORKFLOW']/template ) &gt; 0">
									<xsl:for-each select="template_type_list/template_type[@title='WORKFLOW']/template">
									<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
										template_id[template_id.length] = ]]><xsl:value-of select="@id"/><![CDATA[
									]]></script>
									</xsl:for-each>
						</xsl:when>
					</xsl:choose>
					<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
							itemWorkflow[]]><xsl:value-of select="$sort"/><![CDATA[] = template_id;
						]]></script>
					<tr>
						<!--<xsl:variable name="pos" select="position()"/>	-->		
						<td> </td>
						<td valign="top" align="right">
							<span class="Text">
								<xsl:choose>
									<xsl:when test="count(../../../item_type_list/training_type[@id=$training_id]/item) &gt; 0">
									<input type="radio" name="ity_id" value="{$item_id}" id="ity_id_{$sort}" onclick="javascript:checkValidTemplate({$sort})"/>
									<span id="dummy_type_{$sort}" style="display:none" value="{$dummy_type}"/>
									</xsl:when>
									<xsl:otherwise>
										<img border="0" height="1" src="{$wb_img_path}tp.gif" width="20"/>
										<input type="hidden" name="ity_id" value="{$item_id}" id="ity_id_{$sort}"/>
										<span id="dummy_type_{$sort}" style="display:none" value="{$dummy_type}"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
						<td>
							<span class="Text">
								<b>
									<label for="ity_id_{$sort}">
										<xsl:call-template name="get_ity_title">
											
											<xsl:with-param name="dummy_type" select="$dummy_type"/>
											<!--
											<xsl:with-param name="itm_type" select="$item_id"/>
											<xsl:with-param name="itm_exam_ind" select="$itm_exam_ind"/>
											<xsl:with-param name="itm_ref_ind" select="$itm_ref_ind"/>
											<xsl:with-param name="itm_blend_ind" select="$itm_blend_ind"/>
-->
										</xsl:call-template>
									</label>
								</b>
								<br/>
								<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value"><xsl:value-of select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_item_type']/element[@id = $item_id]/description"/></xsl:with-param>
								</xsl:call-template>
							</span>
						</td>
					</tr>
				</xsl:for-each>
			
			</xsl:for-each>

			<tr>
				<td height="10" colspan="2">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
		</table>
		
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="item_id">
					<xsl:value-of select="item_type_list/training_type[@id = $cur_training_type]/item[@id=$itm_type]/@id"/>
				</xsl:variable>
				<xsl:variable name="sort">
					<xsl:value-of select="item_type_list/training_type[@id = $cur_training_type]/item[@id=$itm_type]/@sort"/>
				</xsl:variable>
				<xsl:variable name="dummy_type">
					<xsl:choose>
						<xsl:when test="$itm_dummy_type != '' ">
							<xsl:value-of select="$itm_dummy_type"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="item_type_list/training_type[@id = $cur_training_type]/item[@id=$itm_type]/@dummy_type"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<input type="hidden" name="ity_id" value="{$item_id}" id="ity_id_{$sort}"/>
				<span id="dummy_type_{$sort}" style="display:none" value="{$dummy_type}"/>
			</xsl:otherwise>
		</xsl:choose>

		<!-- Workflow Selection -->
		<table class="Bg" cellspacing="0" cellpadding="3" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td width="20%" align="right" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="10px"><img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/></td>
				<td width="80%" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
			<tr>
				<td colspan="3">
					<table cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td width="120"><img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/></td>
						<td>
						<span class="TitleText">
							<xsl:value-of select="$lab_g_select_enrol_wf"/><!-- 请选择 -->
							<xsl:text>：</xsl:text>
						</span>
						</td>
					</tr>
					</table>					
				</td>
			</tr>
			<!-- DENNIS -->
			<xsl:for-each select="workflow_list/workflow">
				<xsl:variable name="wrk_tpl_id" select="@tpl_id"/>
				<xsl:variable name="wrk_tpl_pos" select="position()"/>
				<!-- 无需经过审批 -->
				<tr>
					<td></td>
					<td valign="top" align="right">
						<span class="Text">
							<input type="radio" name="wrk_tpl_id" id="wrk_tpl_id_{$wrk_tpl_id}_no_approval" value="{$wrk_tpl_id}" onclick="return disableChk(this,{$wrk_tpl_id})"/>
						</span>
					</td>
					<td align="left">
						<span class="Text">
							<b>
								<label for="wrk_tpl_id_{$wrk_tpl_id}_no_approval">
									<xsl:value-of select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_template_workflow']/element/title[@id='1']"/>
								</label>
							</b>
							<br/>
							<xsl:call-template name="unescape_html_linefeed">
								<xsl:with-param name="my_right_value"><xsl:value-of select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_template_workflow']/element/description[@id='1']"/></xsl:with-param>
							</xsl:call-template>
						</span>
					</td>
				</tr>
				<!-- 需要经过以下审批 -->
				<tr>
					<td></td>
					<td valign="top" align="right">
						<span class="Text">
							<input type="radio" name="wrk_tpl_id" id="wrk_tpl_id_{$wrk_tpl_id}_approval" value="{$wrk_tpl_id}" onclick="return enableChk(this,{$wrk_tpl_id})"/>
						</span>
					</td>
					<td align="left">
						<span class="Text">
							<b>
								<label for="wrk_tpl_id_{$wrk_tpl_id}_approval">
									<xsl:value-of select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_template_workflow']/element/title[@id='2']"/>
								</label>
							</b>
							<br/>
							<xsl:call-template name="unescape_html_linefeed">
								<xsl:with-param name="my_right_value"><xsl:value-of select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_template_workflow']/element/description[@id='2']"/></xsl:with-param>
							</xsl:call-template>
						</span>
					</td>
				</tr>
				<!-- 领导审批 -->
				<tr>
					<td></td>
					<td valign="top" align="right">
						<span class="Text">
							<xsl:text>&#160;</xsl:text>
						</span>
					</td>
					<td align="left">
						<span class="Text">
							<table class="Bg" cellspacing="0" cellpadding="0" border="0" width="100%">
								<tr>
									<td valign="top" align="right" width="5%">
										<span class="Text">
											<input type="checkbox" name="chk_{$wrk_tpl_id}_supervise" id="chk_{$wrk_tpl_id}_supervise" onclick="return toggleRdo(this, {$wrk_tpl_id})"/> 
										</span>
									</td>
									<td align="left" width="95%">
										<span class="Text">
											<b>
												<label for="chk_{$wrk_tpl_id}_supervise">
													<xsl:value-of select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_template_workflow']/element/title[@id='3']"/>
												</label>
											</b>
											<br/>
											<xsl:call-template name="unescape_html_linefeed">
												<xsl:with-param name="my_right_value"><xsl:value-of select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_template_workflow']/element/description[@id='3']"/></xsl:with-param>
											</xsl:call-template>
										</span>
									</td>
								</tr>							
							</table>
						</span>					
					</td>
				</tr>
				<!-- Title 4 -->
				<tr>
					<td></td>
					<td valign="top" align="right">
						<span class="Text">
							<xsl:text>&#160;</xsl:text>
						</span>
					</td>
					<td align="left">
						<span class="Text">
							<table class="Bg" cellspacing="0" cellpadding="0" border="0" width="100%">
								<tr>
									<td width="5%">
										<span class="Text">
											<xsl:text>&#160;</xsl:text>
										</span>
									</td>
									<td align="left" width="95%">
										<span class="Text">
											<table class="Bg" cellspacing="0" cellpadding="0" border="0" width="100%">
												<tr>
													<td valign="top" align="right" width="8%">
														<span class="Text">
															<input type="radio" name="rdo_{$wrk_tpl_id}" id="rdo_{$wrk_tpl_id}_ds" onclick="return isEnabled(this);"/>
														</span>
													</td>
													<td align="left" width="92%">
														<span class="Text">
															<b>
																<label for="rdo_{$wrk_tpl_id}_ds">
																	<xsl:value-of select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_template_workflow']/element/title[@id='4']"/>
																</label>
															</b>
															<br/>
															<xsl:call-template name="unescape_html_linefeed">
																<xsl:with-param name="my_right_value"><xsl:value-of select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_template_workflow']/element/description[@id='4']"/></xsl:with-param>
															</xsl:call-template>
														</span>
													</td>
												</tr>
											</table>
										</span>
									</td>
								</tr>							
							</table>
						</span>					
					</td>
				</tr>
				<!-- Title 5 -->
				<tr>
					<td></td>
					<td valign="top" align="right">
						<span class="Text">
							<xsl:text>&#160;</xsl:text>
						</span>
					</td>
					<td align="left">
						<span class="Text">
							<table class="Bg" cellspacing="0" cellpadding="0" border="0" width="100%">
								<tr>
									<td width="5%">
										<span class="Text">
											<xsl:text>&#160;</xsl:text>
										</span>
									</td>
									<td align="left" width="95%">
										<span class="Text">
											<table class="Bg" cellspacing="0" cellpadding="0" border="0" width="100%">
												<tr>
													<td valign="top" align="right" width="8%">
														<span class="Text">
															<input type="radio" name="rdo_{$wrk_tpl_id}" id="rdo_{$wrk_tpl_id}_ds_gs" onclick="return isEnabled(this);"/>
														</span>
													</td>
													<td align="left" width="92%">
														<span class="Text">
															<b>
																<label for="rdo_{$wrk_tpl_id}_ds_gs">
																	<xsl:value-of select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_template_workflow']/element/title[@id='5']"/>
																</label>
															</b>
															<br/>
															<xsl:call-template name="unescape_html_linefeed">
																<xsl:with-param name="my_right_value"><xsl:value-of select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_template_workflow']/element/description[@id='5']"/></xsl:with-param>
															</xsl:call-template>
														</span>
													</td>
												</tr>
											</table>
										</span>
									</td>
								</tr>							
							</table>
						</span>					
					</td>
				</tr>
				<!-- Title 6 -->
				<tr>
					<td></td>
					<td valign="top" align="right">
						<span class="Text">
							<xsl:text>&#160;</xsl:text>
						</span>
					</td>
					<td align="left">
						<span class="Text">
							<table class="Bg" cellspacing="0" cellpadding="0" border="0" width="100%">
								<tr>
									<td valign="top" align="right" width="5%">
										<span class="Text">
											<input type="checkbox" name="chk_{$wrk_tpl_id}_tadm" id="chk_{$wrk_tpl_id}_tadm" onclick="return isEnabled(this);"/> 
										</span>
									</td>
									<td align="left" width="95%">
										<span class="Text">
											<b>
												<label for="chk_{$wrk_tpl_id}_tadm">
													<xsl:value-of select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_template_workflow']/element/title[@id='6']"/>
												</label>
											</b>
											<br/>
											<xsl:call-template name="unescape_html_linefeed">
												<xsl:with-param name="my_right_value"><xsl:value-of select="/applyeasy/meta/label_reference_data_list/label[@name = 'lab_g_template_workflow']/element/description[@id='6']"/></xsl:with-param>
											</xsl:call-template>
										</span>
									</td>
								</tr>							
							</table>
						</span>					
					</td>
				</tr>
			</xsl:for-each>
			<!-- Option None radio button -->
			<xsl:if test="contains($show_no_wf_template, 'true')">
				<tr>
					<td></td>
					<td valign="top" align="right">
						<xsl:choose>
							<xsl:when test="count(workflow_list/workflow) &gt; 0"><input type="radio" name="wrk_tpl_id" value="" onclick="return isEnabled(this);"/></xsl:when>
							<xsl:otherwise>
								<img border="0" height="1" src="{$wb_img_path}tp.gif" width="20"/>
								<input type="hidden" name="wrk_tpl_id" value=""/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
					<td>
						<span class="Text">
							<b>
								<xsl:value-of select="$lab_g_none"/>
							</b>
						</span>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td height="10" colspan="3">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
		</table>
		<input type="hidden" name="tvw_id"/>
		<input type="hidden" name="stylesheet"/>
		<input type="hidden" name="cmd"/>
		<input type="hidden" name="itm_app_approval_type"/>
		<input type="hidden" name="itm_dummy_type"/>
		<input type="hidden" name="training_type" value="{$cur_training_type}"/>
		<input type="hidden" name="itm_integrated_ind" value="false"/>
		<xsl:call-template name="wb_ui_line"/>
		
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td align="center" valign="middle">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name"  select="$lab_g_form_btn_next_step"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.select_add_item_type_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
					</xsl:call-template>
					<img border="0" width="1" src="{$wb_img_path}tp.gif"/>
					<xsl:variable name="back_ftn">
						<xsl:choose>
							<xsl:when test="$cur_training_type ='COS'">ITM_MAIN</xsl:when>
							<xsl:when test="$cur_training_type ='EXAM'">ITM_EXAM_MAIN</xsl:when>
							<xsl:when test="$cur_training_type ='REF'">ITM_REF_MAIN</xsl:when>
							<xsl:when test="$cur_training_type ='INTEGRATED'">INTEGRATED_ITEM_MAIN</xsl:when>
							<xsl:otherwise>ITM_MAIN</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
						<!-- <xsl:with-param name="wb_gen_btn_href">javascript:history.back();</xsl:with-param> -->
					</xsl:call-template>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
