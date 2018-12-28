<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<!-- itm utils -->
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="utils/wb_utils_process.xsl"/>
	<xsl:import href="share/itm_gen_frm_share.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="no"/>
	<!-- =============================================================== -->
	<xsl:variable name="itm_type_lst" select="/applyeasy/item/item_type/@id"/>
	<xsl:variable name="itm_dummy_type" select="/applyeasy/item/item_type/@dummy_type"/>
	<xsl:variable name="itm_run_ind" select="/applyeasy/item/item_type_meta/@run_ind"/>
	<xsl:variable name="itm_session_ind" select="/applyeasy/item/item_type_meta/@session_ind"/>
	<xsl:variable name="itm_blend_ind" select="/applyeasy/item/item_type_meta/@blend_ind"/>
	<xsl:variable name="itm_exam_ind" select="/applyeasy/item/item_type_meta/@exam_ind"/>
	<xsl:variable name="itm_ref_ind" select="/applyeasy/item/item_type_meta/@ref_ind"/>
	<xsl:variable name="error_code" select="/applyeasy/meta/error_message/code/text()"/>
	<xsl:variable name="error_msg" select="/applyeasy/meta/error_message/content/text()"/>
	<xsl:variable name="tc_enabled" select="/applyeasy/meta/tc_enabled"/>
	<xsl:variable name="training_type" select="/applyeasy/meta/training_type"/>
	<xsl:variable name="cur_time" select="/applyeasy/meta/cur_time"/>
	<xsl:variable name="create_run_ind" select="/applyeasy/item/item_type_meta/@create_run_ind"/>
	<xsl:variable name="itm_integrated_ind" select="/applyeasy/meta/itm_integrated_ind"/>
	<xsl:variable name="itm_tcr_id"><xsl:value-of select="/applyeasy/item/valued_template/section/training_center/center/@id"/></xsl:variable>
	<xsl:variable name="cur_tcr_id"><xsl:value-of select="/applyeasy/training_center/center/@id"/></xsl:variable>
	<xsl:variable name="cur_tcr_title"><xsl:value-of select="/applyeasy/training_center/center/text()"/></xsl:variable>
	
	<xsl:variable name="plan_tc" select="/applyeasy/plan/training_center/@id"/>
	<xsl:variable name="plan_id" select="/applyeasy/plan/@id"/>
	<xsl:variable name="tpn_upd_timestamp" select="/applyeasy/plan/upd_timestamp"/>
	
		<xsl:variable name="plan_type">
		<xsl:choose>
			<xsl:when test="/applyeasy/plan/@type='YEAR'">FTN_AMD_PLAN_CARRY_OUT</xsl:when>
			<xsl:when test="/applyeasy/plan/@type='MAKEUP'">FTN_AMD_MAKEUP_PLAN</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="training_type" select="/applyeasy/meta/training_type"/>
	<xsl:variable name="itm_content_def">
		<xsl:choose>
			<xsl:when test="/applyeasy/meta/itm_content_def != ''">
				<xsl:value-of select="/applyeasy/meta/itm_content_def"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="/applyeasy/item/@content_def"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:variable name="lab_itm_type" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1189')"/>
	<xsl:variable name="lab_itm_code" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1190')"/>
	<xsl:variable name="lab_itm_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1191')"/>
	<xsl:variable name="lab_itm_tcr" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1192')"/>
	<xsl:variable name="lab_itm_node" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1193')"/>
	<xsl:variable name="lab_itm_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1194')"/>
	<xsl:variable name="lab_itm_desc_range" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1195')"/>
	<xsl:variable name="lab_unassigned" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1196')"/>
	<xsl:variable name="lab_assigned" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1197')"/>
	<xsl:variable name="lab_create_new_run" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1198')"/>
	<xsl:variable name="lab_create" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1199')"/>
	<xsl:variable name="lab_class_period" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1200')"/>
	<xsl:variable name="lab_enroll_period" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1202')"/>
	<xsl:variable name="lab_remarke" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1203')"/>
	<xsl:variable name="lab_request" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1204')"/>
	<xsl:variable name="lab_run_info_class" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1171')"/>
	<xsl:variable name="lab_run_exam_info" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1172')"/>
	<xsl:variable name="lab_reset_msg_1" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_226')"/>
	<xsl:variable name="label_core_training_management_464" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_464')"/>
	<xsl:variable name="lab_reset_msg_2" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_227')"/>
	<xsl:variable name="lab_catalog" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_84')"/>
	
	<xsl:variable name="parent_code">
		<xsl:choose>
			<xsl:when test="/applyeasy/item/item_type/@id='AUDIOVIDEO'">FTN_AMD_OPEN_COS_MAIN</xsl:when>
			<xsl:when test="/applyeasy/item/item_type_meta/@exam_ind='true'">FTN_AMD_EXAM_MGT</xsl:when>
			<xsl:otherwise>FTN_AMD_ITM_COS_MAIN</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>


	<xsl:variable name="lab_run_info">
		<xsl:choose>
			<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_run_exam_info"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="$lab_run_info_class"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:variable name="lab_from" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_from')"/> 
	<xsl:variable name="lab_to" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_to')"/> 
	<xsl:variable name="lab_next_btn" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '101')"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/>
	<!-- 完成  --> 	
	<xsl:variable name="label_core_training_management_228" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_228')"/>
	
	<!-- 基本信息 -->
	<xsl:variable name="label_core_training_management_229" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_229')"/>
	<!-- 高级信息 -->
	<xsl:variable name="label_core_training_management_230" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_230')"/> 	
	
	<xsl:variable name="lab_yes" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_214')"/>
	<xsl:variable name="lab_no" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_215')"/>
	<xsl:variable name="lab_select_default_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_select_default_image')"/>
	<xsl:variable name="lab_default_images" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_default_images')"/>
	<xsl:variable name="lab_upload_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_upload_image')"/>
	<xsl:variable name="lab_button_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, '329')"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/>
	<xsl:variable name="label_core_training_management_67" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_67')"/>
	
	<!-- =============================================================== -->
	<xsl:template match="applyeasy">
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
			<link rel="stylesheet" type="text/css" href="../static/css/thickbox.css" />
			
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="JavaScript" src="{$wb_js_path}../static/js/cwn_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
		<!-- <script type="text/javascript" src="{$wb_js_path}jquery.js"/>  -->	
			<script type="text/javascript" src="../static/js/thickbox-compressed.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_defaultImage.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_certificate.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_instructor.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script type="text/javascript" src="{$wb_js_path}jquery.prompt.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			itm_lst = new wbItem
			var goldenman = new wbGoldenMan
			submit_trigger = 'true'
			
			$(function(){
					$itm_desc = $("textarea[name='itm_desc']");
					if($.trim($itm_desc.val()) == '') {
					    $itm_desc.css('color', '#999');
						$itm_desc.val($itm_desc.attr('prompt'));
					}
					$itm_desc.click(function(){
						if($.trim($itm_desc.val()) == $itm_desc.attr('prompt')){
						    $itm_desc.css('color', '#666');
							$itm_desc.val('');	
						}
					});
					$itm_desc.blur(function(){
						if($.trim($itm_desc.val()) == ''){
						    $itm_desc.css('color', '#999');
							$itm_desc.val($itm_desc.attr('prompt'));		
						}
					});
				})
			cert = new wbCertificate;
			inst = new wbInstructor;
			var last_instr_set='';
	//Start Validate Form =========================================================		
	function ValidateFrm(frm){
			file_eles = new Array;
			paths =new Array;		
			]]><xsl:apply-templates select="item/valued_template" mode="js">
				<xsl:with-param name="itm_blend_ind" select="$itm_blend_ind"/>
				<xsl:with-param name="itm_exam_ind" select="$itm_exam_ind"/>
			</xsl:apply-templates>
			// js dependant
			<xsl:apply-templates select="item/valued_template" mode="js_dependant">
				<xsl:with-param name="itm_blend_ind" select="$itm_blend_ind"/>
				<xsl:with-param name="itm_exam_ind" select="$itm_exam_ind"/>
			</xsl:apply-templates>
			//			
			<![CDATA[
		filePaths(frm);
/* 		for(var i=0;i<file_eles.length;i++){
			if(!wb_utils_check_chinese_char(wb_utils_get_filename_from_path(paths[i])))
			return false;
		}
		if(wb_utils_check_duplicate_filename(frm,file_eles,paths)==-1){
			return false;
		} */
		if((frm.item_access_TADM_box && frm.item_access_TADM_box.options.length == 0) 
			|| (frm.item_access_TADM_box0 && frm.item_access_TADM_box0.options.length == 0)) {
			if(confirm(wb_msg_default_ta)) {
				return true;
			}
		} else {
			return true;
		}
	}
	//Get all file paths and their fieldnames of current pages 
	function filePaths(frm){
		var ele_len=0;
		var ele;
		var index=0;
		element_len = frm.elements.length;
		for(var i = 0; i < element_len; i++){
			ele = frm.elements[i]
			if(ele.type=='file'){
				file_eles[index] = ele.name;
				var fieldname = eval('frm.'+ele.name + '_fieldname');
				if (fieldname != null) {
					var radiofields = eval('frm.'+fieldname.value + '_select');
					if(radiofields[0].checked){
						paths[index]=eval('frm.'+fieldname.value + '_hidden').value;
						index++;
					}else if(radiofields[1].checked){
						paths[index]='';
						index++;
					}else{
						paths[index]=ele.value;
						index++;
					}
					
				}else{
					paths[index]=ele.value;
					index++;
				}
			}
		}
	}
	//Start Feed Paramname Value ==================================================
	function feed_param_value(frm){
			]]><xsl:apply-templates select="item/valued_template" mode="feed_param_value">
					
					<xsl:with-param name="itm_blend_ind" select="$itm_blend_ind"/>
					<xsl:with-param name="itm_exam_ind" select="$itm_exam_ind"/>
			</xsl:apply-templates><![CDATA[
	}
	//Start Generate XML  =========================================================  
	function GenerateXML(frm){
			str=''
		]]><xsl:apply-templates select="item/valued_template" mode="gen_xml">
			<xsl:with-param name="itm_blend_ind" select="$itm_blend_ind"/>
			<xsl:with-param name="itm_exam_ind" select="$itm_exam_ind"/>
		</xsl:apply-templates><![CDATA[
		frm.itm_xml.value = str;
	}
	
	//Form caching =============================================================
	function onBlurEvent(obj){
		frm = document.frmXml
		itm_lst.ins_itm_caching(frm,obj)
	}
	
	//Assign form caching  =============================================================
	function frmCache(frm){
		//itm_lst.asn_itm_caching(frm)
	}
	//Start draw preloading for uploading files ===========================================
	function draw_preloading(){
		]]><xsl:if test="item/valued_template/*/*[name()!='title']/subfield_list/subfield[@type='image' or @type='file']"><![CDATA[if(wb_utils_get_cookie("preloadFlag")=="true"){wb_utils_preloading(document["gen_btn_ok0"],"]]><xsl:value-of select="$wb_lang"/><![CDATA[");}]]></xsl:if><![CDATA[
	}
		]]></SCRIPT>
		</head>
		<BODY leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onunload="wb_utils_close_preloading()" onload="wb_utils_set_cookie('preloadFlag','false');">
			<FORM name="frmXml">
				<input type="hidden" name="rename" value="false"/>
				<input type="hidden" name="cmd" value="ae_ins_itm"/>
				<input type="hidden" name="itm_xml"/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="itm_cache_type" value="{item/item_type/@id}"/>
				<input type="hidden" name="itm_run_ind" value="{$itm_run_ind}"/>
				<input type="hidden" name="itm_session_ind" value="{$itm_session_ind}"/>
				<input type="hidden" name="itm_blend_ind" value="{$itm_blend_ind}"/>
				<input type="hidden" name="itm_exam_ind" value="{$itm_exam_ind}"/>
				<input type="hidden" name="itm_ref_ind" value="{$itm_ref_ind}"/>
				<input type="hidden" name="itm_dummy_type" value="{$itm_dummy_type}"/>
				<input type="hidden" name="training_type" value="{$training_type}"/>
				<input type="hidden" name="itm_integrated_ind" value="{$itm_integrated_ind}"/>
				<input type="hidden" name="itm_content_def" value="{$itm_content_def}"/>

				<input type="hidden" name="tpn_itm_run_ind" value="true"/>
				<input type="hidden" name="training_plan" value="{$training_plan}"/>
				<input type="hidden" name="plan_tcr_id" value="{$plan_tc}"/>
				<input type="hidden" name="plan_id" value="{$plan_id}"/>
				<input type="hidden" name="tpn_update_timestamp" value="{$tpn_upd_timestamp}"/>
				<input type="hidden" name="itm_tcr_id" value="{$itm_tcr_id}"/>
				
				<xsl:call-template name="content"/>
			</FORM>
		</BODY>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:variable name="type_name">
			<xsl:call-template name="get_ity_title">
				<xsl:with-param name="itm_type" select="$itm_dummy_type"/> 
			</xsl:call-template>
		</xsl:variable>
		
		<xsl:variable name="page_title">
			<xsl:choose>
				<xsl:when test="$itm_run_ind = 'true'">
					<xsl:value-of select="$lab_create_new_run"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$lab_create"/>
					<xsl:if test="$wb_lang = 'en'">&#160;</xsl:if>
					<xsl:value-of select="translate($type_name,$uppercase,$lowercase)"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
			
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
			<xsl:with-param name="page_title"><xsl:value-of select="$page_title"></xsl:value-of></xsl:with-param>
		</xsl:call-template>
		
		
		
		<xsl:if test="$itm_run_ind != 'true'">
			<xsl:call-template name="wb_utils_process">
				<xsl:with-param name="itm_type" select="$itm_type_lst"/>
				<xsl:with-param name="cur_tabs">
					<xsl:choose>
						<xsl:when test="$itm_type_lst = 'CLASSROOM'">3</xsl:when>
						<xsl:otherwise>2</xsl:otherwise>
					</xsl:choose>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="$itm_run_ind = 'true'">
			
			<xsl:call-template name="wb_ui_nav_link">
				<xsl:with-param name="text">
					<span class="NavLink">
						<xsl:apply-templates select="item/nav/item" mode="nav"/>
						<xsl:if test="/applyeasy/item/@parent_itm_id > 0">
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<a class="NavLink">
								<xsl:attribute name="href">javascript:itm_lst.get_item_run_list(<xsl:value-of select="/applyeasy/item/@parent_itm_id"/>)
								</xsl:attribute>
								<xsl:choose>
									<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
									<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
								</xsl:choose>
							</a>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
						</xsl:if>
						
						<xsl:choose>
							<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$label_core_training_management_464"/></xsl:when>
							<xsl:otherwise><xsl:value-of select="$lab_create_new_run"/></xsl:otherwise>
						</xsl:choose>
						
					</span>
				</xsl:with-param>
			</xsl:call-template>
			
		</xsl:if>
		
		
		 <xsl:call-template name="itm_action_nav">
	    	<xsl:with-param name="view_mode">simple</xsl:with-param>
			<xsl:with-param name="is_add">true</xsl:with-param>
			<xsl:with-param  name="cur_node_id">01</xsl:with-param>
		</xsl:call-template>

	
		
		<ul class="nav nav-tabs page-tabs" role="tablist">
			<li role="presentation" class="active"><a
				aria-controls="basic" role="tab" data-toggle="tab"
				href="#basic"><xsl:value-of select="$label_core_training_management_229"/></a></li>
			<li role="presentation"><a aria-controls="senior" role="tab"
				data-toggle="tab" href="#senior"><xsl:value-of select="$label_core_training_management_230"/></a></li>
		</ul>
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="basic">
				<xsl:apply-templates select="item/valued_template/section[@id = 1]">
					<xsl:with-param name="lab_yes" select="$lab_yes"/>
					<xsl:with-param name="lab_no" select="$lab_no"/>
				</xsl:apply-templates>
			</div>
			<div role="tabpanel" class="tab-pane" id="senior">
				<xsl:apply-templates select="item/valued_template/section[@id = 2]">
					<xsl:with-param name="lab_yes" select="$lab_yes"/>
					<xsl:with-param name="lab_no" select="$lab_no"/>
				</xsl:apply-templates>
			</div>
		</div>
		<xsl:apply-templates select="item/valued_template/hidden"/>
		<table>
			<tr>
				<td width="20%" align="right" class="wzb-form-label">
				</td>
				<td width="80%" class="wzb-ui-module-text">
					<span class="wzb-ui-module-text">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
					</span>
				</td>
			</tr>
		</table>
		
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$label_core_training_management_67"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.ins_item_run_exec(document.frmXml,'<xsl:value-of select="$plan_id"/>')</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">
				<xsl:choose>
					<xsl:when test="$training_plan = 'true'">javascript:history.back()</xsl:when>
					<xsl:when test="$run_ind  = 'true' and $training_plan != 'true'">javascript:itm_lst.get_item_run_list(<xsl:value-of select="/applyeasy/item/@parent_itm_id"/>)</xsl:when>
					
					<xsl:otherwise>javascript:itm_lst.get_item_run_list(<xsl:value-of select="/applyeasy/item/@parent_itm_id"/>)</xsl:otherwise></xsl:choose></xsl:with-param>
			</xsl:call-template>
		</div>
		
	</xsl:template>

	<!-- =============================================================== -->
	<xsl:template match="meta"/>
	<!-- =============================================================== -->
	<xsl:template match="hidden">
		<xsl:apply-templates mode="hidden_field"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="valued_template/section">
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<table>
			<xsl:apply-templates select="*[(not(@type) or (@type != 'read' and @type != 'read_datetime' and @type != 'tcr_pickup') or (@type = 'tcr_pickup' and $tc_enabled = 'true'))  and not(@blend_ind and @blend_ind !=$itm_blend_ind) and not(@exam_ind and @exam_ind != $itm_exam_ind)]" mode="field">
				<xsl:with-param name="lab_yes" select="$lab_yes"/>
				<xsl:with-param name="lab_no" select="$lab_no"/>
			</xsl:apply-templates>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="field">
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:variable name="show">
			<xsl:choose>
				<xsl:when test="name() = 'item_status'">false</xsl:when>
				<xsl:when test="name() = 'auto_enroll_target_learners'">false</xsl:when>
				<xsl:otherwise>true</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="$show = 'true'">
		<tr>
			<td class="wzb-form-label" valign="top">
				<xsl:variable name="text_class">
					<xsl:choose>
						<xsl:when test="@marked='yes'">TitleTextBold</xsl:when>
						<xsl:otherwise>TitleText</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="field_desc">
					<xsl:call-template name="get_desc"/>
				</xsl:variable>
				<xsl:if test="$field_desc != ''">
					<xsl:choose>
						<xsl:when test="@paramname = $error_code">
							<font color="red">
								<xsl:if test="@required='yes'"><span class="wzb-form-star">*</span></xsl:if>
								<xsl:value-of select="$field_desc"/>
								<xsl:text>：</xsl:text>
							</font>
						</xsl:when>
						<xsl:otherwise>
							<xsl:if test="@required='yes'"><span class="wzb-form-star">*</span></xsl:if> 
							<xsl:if test="@id = 'TADM'"><span class="wzb-form-star">*</span></xsl:if>
							<xsl:value-of select="$field_desc"/>
							<xsl:text>：</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
			</td>
			<td class="wzb-form-control">
				<xsl:if test="prefix">
					<xsl:call-template name="prefix"/>
				</xsl:if>
				<xsl:apply-templates select="." mode="field_type">
					<xsl:with-param name="text_class">wbFormRightText</xsl:with-param>
					<xsl:with-param name="lab_yes" select="$lab_yes"/>
					<xsl:with-param name="lab_no" select="$lab_no"/>
				</xsl:apply-templates>
				<xsl:if test="$error_msg != '' and @paramname = $error_code">
					<xsl:text>&#160;&#160;&#160;</xsl:text>
					<font color="red">
						<xsl:copy-of select="$error_msg"/>
					</font>
				</xsl:if>
				<xsl:if test="suffix">
					<xsl:call-template name="suffix"/>
				</xsl:if>
				<table>
					<xsl:for-each select="subfield_list/*">
						<tr>
							<xsl:if test="title">
								<td valign="top" width="2%">
									<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
									<xsl:text>：</xsl:text>
								</td>
							</xsl:if>
							<td valign="top" width="98%" style="padding:0 0 5px 0;">
								<xsl:apply-templates select="." mode="field_type">
									<xsl:with-param name="lab_yes" select="$lab_yes"/>
									<xsl:with-param name="lab_no" select="$lab_no"/>
								</xsl:apply-templates>
								<xsl:if test="suffix">
									<xsl:call-template name="suffix"/>
								</xsl:if>
								<xsl:if test="../../@arrange = 'vertical'">
									<xsl:if test="position() != last()">
										<br/>
										<br/>
									</xsl:if>
								</xsl:if>
							</td>
						</tr>
					</xsl:for-each>
				</table>				
			</td>
		</tr>
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="link_list" mode="field"/>
	<xsl:template match="title"/>
	<xsl:template match="training_type"/>
	<xsl:template match="last_updated"/>
	<xsl:template match="creator"/>
	<xsl:template match="link_list" mode="field_type"/>
	<xsl:template match="title" mode="field"/>
	<xsl:template match="title" mode="subfield"/>
	<xsl:template match="applicable"/>
	<!-- =============================================================== -->
	<!-- =============================================================== -->
	<xsl:template match="item" mode="nav">
		<xsl:choose>
			<xsl:when test="@run_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=last()">
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
				<xsl:text>&#160;&gt;&#160;</xsl:text>
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