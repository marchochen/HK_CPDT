<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<!-- itm utils -->
	<xsl:import href="share/itm_gen_frm_share.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="itm_updated_timestamp" select="/applyeasy/item/last_updated/@timestamp"/>
	<xsl:variable name="itm_type_lst" select="/applyeasy/item/item_type/@id"/>
	<xsl:variable name="run_ind" select="/applyeasy/item/@run_ind"/>
	<xsl:variable name="itm_blend_ind" select="/applyeasy/item/item_type_meta/@blend_ind"/>
	<xsl:variable name="itm_exam_ind" select="/applyeasy/item/item_type_meta/@exam_ind"/>
	<xsl:variable name="itm_ref_ind" select="/applyeasy/item/item_type_meta/@ref_ind"/>	
	<xsl:variable name="itm_dummy_type" select="/applyeasy/item/item_type/@dummy_type"/>
	<xsl:variable name="session_ind" select="/applyeasy/item/@session_ind"/>
	<xsl:variable name="error_code" select="/applyeasy/meta/error_message/code/text()"/>
	<xsl:variable name="error_msg" select="/applyeasy/meta/error_message/content/text()"/>
	<xsl:variable name="tc_enabled" select="/applyeasy/meta/tc_enabled"/>
	<xsl:variable name="training_type" select="/applyeasy/training_type"/>
	
	<xsl:variable name="lab_select_default_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_select_default_image')"/>
	<xsl:variable name="lab_default_images" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_default_images')"/>
	<xsl:variable name="lab_upload_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_upload_image')"/>
	<xsl:variable name="lab_button_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, '329')"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/> 	
	
	<!-- 基本信息 -->
	<xsl:variable name="label_core_training_management_229" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_229')"/>
	<!-- 高级信息 -->
	<xsl:variable name="label_core_training_management_230" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_230')"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:apply-templates select="applyeasy"/>
	</xsl:template>
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
			<link rel="stylesheet" type="text/css" href="../static/css/thickbox.css" />
			
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>  
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_certificate.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_instructor.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js"/>
			
			<script type="text/javascript" src="../static/js/thickbox-compressed.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_defaultImage.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			itm_lst = new wbItem;
			goldenman = new wbGoldenMan;
			cert = new wbCertificate;
			inst = new wbInstructor;
			var last_instr_set='';
			var lang = ']]><xsl:value-of select="$wb_lang"/><![CDATA[';
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
		
		if(frm.field02_ !== undefined) {
			if (frm.field02_.value.indexOf("\"")>-1) {
				alert('"' + eval('wb_msg_usr_title') + '"' + eval('wb_msg_' + lang + '_double_quotation_marks'));
				frm.field02_.focus()
				return false;
			}
		}	
		
		
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
	
	//Form Caching =========================================================
	function onBlurEvent(obj){
	
	}
	//Start draw preloading for uploading files ===========================================
	function draw_preloading(){
		]]><xsl:if test="item/valued_template/*/*[name()!='title']/subfield_list/subfield[@type='image' or @type='file']"><![CDATA[if(wb_utils_get_cookie("preloadFlag")=="true"){wb_utils_preloading(document["gen_btn_ok0"],"]]><xsl:value-of select="$wb_lang"/><![CDATA[");}]]></xsl:if><![CDATA[
	}
		]]></SCRIPT>
		</head>
		<BODY leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onunload="wb_utils_close_preloading()" onload="wb_utils_set_cookie('preloadFlag','false');">
			<FORM name="frmXml" method="post" enctype="multipart/form-data">
				<input type="hidden" name="rename" value="no"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="itm_xml"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="tvw_id"/>
				<input type="hidden" name="itm_upd_timestamp" value="{$itm_updated_timestamp}"/>
				<input type="hidden" name="itm_id" value="{$itm_id}"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="save_submitted_params" value="true"/>
				<input type="hidden" name="itm_blend_ind" value="{$itm_blend_ind}"/>
				<input type="hidden" name="itm_exam_ind" value="{$itm_exam_ind}"/>
				<input type="hidden" name="itm_ref_ind" value="{$itm_ref_ind}"/>
				<input type="hidden" name="itm_dummy_type" value="{$itm_dummy_type}"/>				
				<input type="hidden" name="org_tcr_id" value="{/applyeasy/item/@tcr_id}"/>
				<!--<input type="hidden" name="itm_type" value="{$itm_type_lst}"/>-->
				<xsl:call-template name="wb_init_lab"/>
			</FORM>
		</BODY>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="/applyeasy/item">
			<xsl:with-param name="lab_run_info">
					<xsl:choose>
						<xsl:when test="$training_type='EXAM'"><xsl:value-of select="$lab_const_exam_run"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_run"/></xsl:otherwise>
					</xsl:choose>
					訊息
			</xsl:with-param>
			<xsl:with-param name="lab_session_info">
				<xsl:value-of select="$lab_const_session"/> 訊息</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_edit">編輯</xsl:with-param>
			<xsl:with-param name="lab_run">
				<xsl:choose>
					<xsl:when test="$training_type = 'EXAM'"><xsl:value-of select="$lab_const_exam_run"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$lab_const_run"/></xsl:otherwise>
				</xsl:choose>	
			</xsl:with-param>
			<xsl:with-param name="lab_session">
				<xsl:value-of select="$lab_const_session"/>
			</xsl:with-param>
			<xsl:with-param name="lab_edit_learning_solution">編輯課程</xsl:with-param>
			<xsl:with-param name="lab_star">*</xsl:with-param>
			<xsl:with-param name="lab_in_bold"> 加粗 </xsl:with-param>
			<xsl:with-param name="lab_bold_desc">- 進行下一步之前要完成的執行主管概要項。</xsl:with-param>
			<xsl:with-param name="lab_left">（</xsl:with-param>
			<xsl:with-param name="lab_right">）</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="/applyeasy/item">
			<xsl:with-param name="lab_run_info">
				<xsl:choose>
					<xsl:when test="$training_type='EXAM'"><xsl:value-of select="$lab_const_exam_run"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$lab_const_run"/></xsl:otherwise>
				</xsl:choose>信息
			</xsl:with-param>
			<xsl:with-param name="lab_session_info">
				<xsl:value-of select="$lab_const_session"/> 信息</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_edit">编辑</xsl:with-param>
			<xsl:with-param name="lab_run">
				<xsl:choose>
					<xsl:when test="$training_type = 'EXAM'"><xsl:value-of select="$lab_const_exam_run"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$lab_const_run"/></xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="lab_session">
				<xsl:value-of select="$lab_const_session"/>
			</xsl:with-param>
			<xsl:with-param name="lab_edit_learning_solution">编辑课程</xsl:with-param>
			<xsl:with-param name="lab_star">*</xsl:with-param>
			<xsl:with-param name="lab_in_bold"> 加粗 </xsl:with-param>
			<xsl:with-param name="lab_bold_desc">- 进行下一步之前要完成的执行主管概要项。</xsl:with-param>
			<xsl:with-param name="lab_left">（</xsl:with-param>
			<xsl:with-param name="lab_right">）</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="/applyeasy/item">
			<xsl:with-param name="lab_run_info">
				<xsl:choose>
					<xsl:when test="$training_type='EXAM'"><xsl:value-of select="$lab_const_exam_run"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$lab_const_run"/></xsl:otherwise>
				</xsl:choose> information
			</xsl:with-param>
			<xsl:with-param name="lab_session_info">
				<xsl:value-of select="$lab_const_session"/> information</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_run">
				<xsl:choose>
					<xsl:when test="$training_type = 'EXAM'"><xsl:value-of select="$lab_const_exam_run"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$lab_const_run"/></xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="lab_session">
				<xsl:value-of select="$lab_const_session"/>
			</xsl:with-param>
			<xsl:with-param name="lab_edit_learning_solution">Edit learning solution</xsl:with-param>
			<xsl:with-param name="lab_star">*</xsl:with-param>
			<xsl:with-param name="lab_in_bold"> IN-BOLD </xsl:with-param>
			<xsl:with-param name="lab_bold_desc">- fields of the executive summary that are required to be completed prior to approval.</xsl:with-param>
			<xsl:with-param name="lab_left">(</xsl:with-param>
			<xsl:with-param name="lab_right">)</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="/applyeasy/item">
		<xsl:param name="lab_run_info" />
		<xsl:param name="lab_session_info" />
		<xsl:param name="lab_run"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_select_solutions"/>
		<xsl:param name="lab_star"/>
		<xsl:param name="lab_in_bold"/>
		<xsl:param name="lab_bold_desc"/>
		<xsl:param name="lab_left"/>
		<xsl:param name="lab_right"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_session"/>
		<xsl:param name="lab_edit"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
		
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="/applyeasy/meta/submitted_params_list">
						<xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = 'itm_title']/value/text()"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="//itm_action_nav/@itm_title"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:apply-templates select="nav/item" mode="nav">
					<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
					<xsl:with-param name="lab_session_info" select="$lab_session_info"/>
				</xsl:apply-templates>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="itm_action_nav">
			<xsl:with-param name="view_mode">simple</xsl:with-param>
			<xsl:with-param name="is_add">false</xsl:with-param>
			<xsl:with-param  name="cur_node_id">01</xsl:with-param>
		</xsl:call-template>
	<div class="wzb-item-main">
	
		<ul class="nav nav-tabs page-tabs" role="tablist">
			<li role="presentation" class="active"><a
				aria-controls="basic" role="tab" data-toggle="tab"
				href="#basic"><xsl:value-of select="$label_core_training_management_229"/></a></li>
				<xsl:if test="$itm_type != 'AUDIOVIDEO'">
					<li role="presentation"><a aria-controls="senior" role="tab"
						data-toggle="tab" href="#senior"><xsl:value-of select="$label_core_training_management_230"/></a></li>
				</xsl:if>
		</ul>
		
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="basic">
				<xsl:apply-templates select="valued_template/section[@id = 1]">
					<xsl:with-param name="lab_yes" select="$lab_yes"/>
					<xsl:with-param name="lab_no" select="$lab_no"/>
				</xsl:apply-templates>
			</div>
			<xsl:if test="$itm_type != 'AUDIOVIDEO'">
				<div role="tabpanel" class="tab-pane" id="senior">
					<xsl:apply-templates select="valued_template/section[@id = 2]">
						<xsl:with-param name="lab_yes" select="$lab_yes"/>
						<xsl:with-param name="lab_no" select="$lab_no"/>
					</xsl:apply-templates>
				</div>
			</xsl:if>
		</div>
		<xsl:comment>hidden start</xsl:comment>
		<xsl:apply-templates select="valued_template/hidden"/>
		<xsl:comment>hidden end</xsl:comment>
		<xsl:if test="$run_ind != 'true'">
			<table>
				<tr>
					<td class="wzb-form-label">
					</td>
					<td class="wzb-ui-module-text">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
					</td>
				</tr>
				<tr>
					<td width="20%" align="right" height="10">
					</td>
					<td width="80%" height="10">
					</td>
				</tr>
			</table>
		</xsl:if>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
					<xsl:value-of select="$lab_g_form_btn_ok"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">
					<xsl:choose>
						<xsl:when test="$error_code = 'invalid_timestamp'">javascript:itm_lst.upd_item_prep(<xsl:value-of select="$itm_id"/>)</xsl:when>
						<xsl:otherwise>javascript:itm_lst.upd_item_exec(document.frmXml,<xsl:value-of select="$itm_id"/>,<xsl:value-of select="$run_ind"/>,'',<xsl:value-of select="$session_ind"/>)</xsl:otherwise>
					</xsl:choose>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
					<xsl:value-of select="$lab_g_form_btn_cancel"/>
				</xsl:with-param>
				<!--<xsl:with-param name="wb_gen_btn_href">javascript:window.history.back()</xsl:with-param>-->
				<xsl:with-param name="wb_gen_btn_href">
					<xsl:choose>
						<xsl:when test="$run_ind = 'true'">javascript:itm_lst.get_item_run_detail(<xsl:value-of select="$itm_id"/>)</xsl:when>
						<xsl:otherwise>javascript:itm_lst.get_item_detail(<xsl:value-of select="$itm_id"/>)</xsl:otherwise>
					</xsl:choose>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
		</div>
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
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:choose>
					<xsl:when test="position()!=last()">
						<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
							<xsl:value-of select="title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="title"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@session_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
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
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:choose>
					<xsl:when test="position()!=last()">
						<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
							<xsl:value-of select="title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="title"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="position()!=last()">
						<a href="javascript:itm_lst.get_item_detail({@id})" class="NavLink">
							<xsl:value-of select="title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="title"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
