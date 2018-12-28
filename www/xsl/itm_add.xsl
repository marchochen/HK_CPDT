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
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<!-- itm utils -->
	<xsl:import href="share/itm_gen_frm_share.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
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
	<xsl:variable name="training_plan" select="/applyeasy/meta/training_plan"/>
	<xsl:variable name="plan_tc" select="/applyeasy/plan/training_center/@id"/>
	<xsl:variable name="plan_id" select="/applyeasy/plan/@id"/>
	<xsl:variable name="plan_type">
		<xsl:choose>
			<xsl:when test="/applyeasy/plan/@type='YEAR'">FTN_AMD_PLAN_CARRY_OUT</xsl:when>
			<xsl:when test="/applyeasy/plan/@type='MAKEUP'">FTN_AMD_MAKEUP_PLAN</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="training_type" select="/applyeasy/meta/training_type"/>
	<xsl:variable name="create_run_ind" select="/applyeasy/item/item_type_meta/@create_run_ind"/>
	<xsl:variable name="tpn_upd_timestamp" select="/applyeasy/plan/upd_timestamp"/>
	<xsl:variable name="itm_integrated_ind" select="/applyeasy/meta/itm_integrated_ind"/>
	
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
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<link rel="stylesheet" type="text/css" href="../static/css/thickbox.css" />
			
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_certificate.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_instructor.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js"/>
			
			<script type="text/javascript" src="../static/js/thickbox-compressed.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_defaultImage.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			var lang = ']]><xsl:value-of select="$wb_lang"/><![CDATA[';
			submit_trigger = 'true'
			itm_lst = new wbItem
			var goldenman = new wbGoldenMan
			cert = new wbCertificate; 
			
			inst = new wbInstructor;
			var last_instr_set='';
	//Form caching =============================================================
	function onBlurEvent(obj){
		frm = document.frmXml
		itm_lst.ins_itm_caching(frm,obj)
	}
	
	//Assign form caching  =============================================================
	function frmCache(frm){
		//itm_lst.asn_itm_caching(frm)
	}
	
	//Start Validate Form =========================================================		
	function ValidateFrm(frm){
		
			]]><xsl:apply-templates select="item/valued_template" mode="js">
				   <xsl:with-param name="itm_exam_ind" select="$itm_exam_ind"/>
			   </xsl:apply-templates>
			// js dependant
			<xsl:apply-templates select="item/valued_template" mode="js_dependant">
				<xsl:with-param name="itm_exam_ind" select="$itm_exam_ind"/>
			</xsl:apply-templates>
			//
			<![CDATA[
			
		if(frm.field02_ !== undefined) {
			if (frm.field02_.value.indexOf("\"")>-1) {
				alert('"' + eval('wb_msg_usr_title') + '"' + eval('wb_msg_' + lang + '_double_quotation_marks'));
				frm.field02_.focus()
				return false;
			}
		}	
			
		if(frm.itm_icon){
			var check = frm.itm_icon.parentNode.parentNode.firstChild.checked;
			if(check){
				if(!gen_validate_empty_field(frm.itm_icon,eval('wb_msg_' + lang + '_icon_upload'),lang)){
						frm.itm_icon.nextSibling.focus();
						return false;	
				}
			}
		}

		if((frm.item_access_TADM_box && frm.item_access_TADM_box.options.length == 0) 
			|| (frm.item_access_TADM_box0 && frm.item_access_TADM_box0.options.length == 0)) {
			if(confirm(wb_msg_default_ta)) {
				return true;
			}
		} else {
			return true;
		}
	}
	//Start Feed Paramname Value ==================================================
	function feed_param_value(frm){
			]]><xsl:apply-templates select="item/valued_template" mode="feed_param_value">
				   <xsl:with-param name="itm_exam_ind" select="$itm_exam_ind"/>
			   </xsl:apply-templates>
			<![CDATA[
	}
	//Start Generate XML  =========================================================	
	function GenerateXML(frm){
			str=''
		]]><xsl:apply-templates select="item/valued_template" mode="gen_xml">
			   <xsl:with-param name="itm_exam_ind" select="$itm_exam_ind"/>
		   </xsl:apply-templates>
		<![CDATA[
		
		frm.itm_xml.value = str;
	}
	//Start draw preloading for uploading files ===========================================
	function draw_preloading(){
		]]><xsl:if test="item/valued_template/*/*[name()!='title']/subfield_list/subfield[@type='image' or @type='file']"><![CDATA[if(wb_utils_get_cookie("preloadFlag")=="true"){wb_utils_preloading(document["gen_btn_ok0"],"]]><xsl:value-of select="$wb_lang"/><![CDATA[");}]]></xsl:if><![CDATA[
	}
		]]></SCRIPT>
		</head>
		<BODY leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onunload="wb_utils_close_preloading()" onload="wb_utils_set_cookie('preloadFlag','false');frmCache(document.frmXml)">
			<FORM name="frmXml" enctype="multipart/form-data">
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
				<input type="hidden" name="training_plan" value="{$training_plan}"/>
				<input type="hidden" name="save_submitted_params" value="true"/>
				<input type="hidden" name="tpn_create_run_ind" value="{$create_run_ind}"/>
				<input type="hidden" name="plan_id" value="{$plan_id}"/>
				<input type="hidden" name="plan_tcr_id" value="{$plan_tc}"/>
				<input type="hidden" name="tpn_update_timestamp" value="{$tpn_upd_timestamp}"/>
				<input type="hidden" name="training_type" value="{$training_type}"/>
				<input type="hidden" name="itm_integrated_ind" value="{$itm_integrated_ind}"/>
				<xsl:call-template name="wb_init_lab"/>
			</FORM>
		</BODY>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates>
			<xsl:with-param name="lab_create">建立</xsl:with-param>
			<xsl:with-param name="lab_create_learning_solution">建立課程</xsl:with-param>
			<xsl:with-param name="lab_star">*</xsl:with-param>
			<xsl:with-param name="lab_in_bold">粗體顯示</xsl:with-param>
			<xsl:with-param name="lab_bold_desc">- 進行下一步之前要完成的執行主管概要項</xsl:with-param>
			<xsl:with-param name="lab_left">(</xsl:with-param>
			<xsl:with-param name="lab_right">)</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates>
			<xsl:with-param name="lab_create">创建</xsl:with-param>
			<xsl:with-param name="lab_create_learning_solution">创建课程</xsl:with-param>
			<xsl:with-param name="lab_star">*</xsl:with-param>
			<xsl:with-param name="lab_in_bold"> 粗体显示 </xsl:with-param>
			<xsl:with-param name="lab_bold_desc">- 进行下一步之前要完成的执行主管概要项</xsl:with-param>
			<xsl:with-param name="lab_left">（</xsl:with-param>
			<xsl:with-param name="lab_right">）</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates>
			<xsl:with-param name="lab_create">Create</xsl:with-param>
			<xsl:with-param name="lab_create_learning_solution">Create learning solution</xsl:with-param>
			<xsl:with-param name="lab_star">*</xsl:with-param>
			<xsl:with-param name="lab_in_bold"> IN-BOLD </xsl:with-param>
			<xsl:with-param name="lab_bold_desc">- fields of the executive summary that are required to be completed prior to approval.</xsl:with-param>
			<xsl:with-param name="lab_left">(</xsl:with-param>
			<xsl:with-param name="lab_right">)</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next">Next</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="item">
		<xsl:param name="lab_create"/>
		<xsl:param name="lab_create_learning_solution"/>
		<xsl:param name="lab_star"/>
		<xsl:param name="lab_in_bold"/>
		<xsl:param name="lab_bold_desc"/>
		<xsl:param name="lab_left"/>
		<xsl:param name="lab_right"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_next"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_create"/>
				<xsl:if test="$wb_lang = 'en'">&#160;</xsl:if>
				<xsl:variable name="type_name">
					<xsl:call-template name="get_ity_title">
						<xsl:with-param name="itm_type" select="$itm_dummy_type"/> 
					</xsl:call-template>
				</xsl:variable>	
			<xsl:value-of select="translate($type_name,$uppercase,$lowercase)"/>
			
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="itm_action_nav">
			<xsl:with-param name="view_mode">simple</xsl:with-param>
			<xsl:with-param name="is_add">true</xsl:with-param>
			<xsl:with-param  name="cur_node_id">01</xsl:with-param>
		</xsl:call-template>
		<xsl:if test="$error_msg != ''">
			<xsl:call-template name="wb_ui_sub_title">
				<xsl:with-param name="text">
					<p align="center">
						<font color="red">
							<xsl:value-of select="$error_msg"/>
						</font>
					</p>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<!--  
		<xsl:apply-templates select="valued_template/section">
			<xsl:with-param name="lab_yes" select="$lab_yes"/>
			<xsl:with-param name="lab_no" select="$lab_no"/>
		</xsl:apply-templates>
		-->
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
		
		<xsl:apply-templates select="valued_template/hidden"/>
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
			<xsl:variable name="lab_btn_ok">
				<xsl:choose>
					<xsl:when test="$training_plan ='true' and $create_run_ind = 'true'"><xsl:value-of select="$lab_g_form_btn_next"/></xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_g_form_btn_ok"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_ok"></xsl:with-param>
					<xsl:with-param name="id">submit_btn</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.ins_item_exec(document.frmXml,'<xsl:value-of select="$itm_type_lst"/>')</xsl:with-param>
				</xsl:call-template>
				
				<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
					</xsl:call-template>
					<!-- 
				<xsl:choose>
					<xsl:when test="$training_type='REF'">
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.select_add_item_type_prep('<xsl:value-of select="$training_type"/>','<xsl:value-of select="$training_plan"/>','<xsl:value-of select="$plan_tc"/>','<xsl:value-of select="$plan_id"/>','<xsl:value-of select="$tpn_upd_timestamp"/>','<xsl:value-of select="$plan_type"/>')</xsl:with-param>
					</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
				 -->
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="meta"/>
	<!-- =============================================================== -->
	<xsl:template match="hidden">
		<xsl:apply-templates mode="hidden_field"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="section">
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:if test="position() != 1">
			<xsl:call-template name="wb_ui_space"/>
		</xsl:if>
	
		<table>
			<xsl:apply-templates select="*[(not(@type) or (@type != 'read' and @type != 'read_datetime' and @type != 'tcr_pickup') or (@type = 'tcr_pickup' and $tc_enabled = 'true')) and not(@exam_ind and @exam_ind != $itm_exam_ind)]" mode="field">
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
							<td valign="top" width="98%">
								<xsl:apply-templates select="." mode="field_type">
									<xsl:with-param name="text_class">wbFormRightText</xsl:with-param>
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
	<xsl:template match="node"/>
	<xsl:template match="link_list" mode="field"/>
	<xsl:template match="link_list" mode="field_type"/>
	<xsl:template match="title" mode="field"/>
	<xsl:template match="title" mode="subfield"/>
	<xsl:template match="plan"/>
	<xsl:template match="*"/>
	<!-- =============================================================== -->
</xsl:stylesheet>
