<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="share/div_tree_share.xsl"/>
	<xsl:import href="share/gen_tree_js.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<!-- cust utils -->
	<xsl:output indent="yes"/>
	<xsl:variable name="mod_cnt" select="count(certificate_mod/e_certificate/certificate)"/>
	<xsl:variable name="cur_tcr_id" select="certificate_mod/cur_training_center/@id"/>
	<xsl:variable name="page_size" select="/certificate_mod/pagination/@page_size"/>
	<xsl:variable name="cur_page" select="/certificate_mod/pagination/@cur_page"/>
	<xsl:variable name="total" select="/certificate_mod/pagination/@total_rec"/>
	<xsl:variable name="timestamp" select="/certificate_mod/pagination/@timestamp"/>
	<xsl:variable name="sea_core" select="/certificate_mod/search/@core"/>
	<xsl:variable name="sea_title" select="/certificate_mod/search/@title"/>
	<xsl:variable name="sea_status" select="/certificate_mod/search/@status"/>
	<!-- 按培训中心显示 -->
	<xsl:variable name="tc_enabled" select="/certificate_mod/meta/tc_enabled"/>
	<!-- ================================================================ -->
	<xsl:template match="/">
		<xsl:apply-templates select="certificate_mod"/>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="certificate_mod">
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
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_certificate.js"/>
			<script language="Javascript"><![CDATA[
			var cert = new wbCertificate;
			function load_tree() {
				if (frmXml.tc_enabled_ind) {
					page_onload(250);
				}
			}
			function show_content(tcr_id) {
				cert.show_mgt_content(tcr_id);
			}
		]]></script>
		</head>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_body">
		<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0" onload="load_tree()">
			<form name="frmXml" onsubmit="return false;">
				<xsl:call-template name="content"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================label================================== -->
	<xsl:variable name="lab_main_cert" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_47')"/>
	<xsl:variable name="lab_title" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_28')"/>
	<xsl:variable name="lab_status" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_20')"/>
	<xsl:variable name="lab_active" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_21')"/>
	<xsl:variable name="lab_inactive" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_22')"/>
	<xsl:variable name="lab_no_cert" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_48')"/>
	<xsl:variable name="lab_g_txt_btn_edit_schedule" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_49')"/>
	<xsl:variable name="lab_g_txt_btn_add" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_50')"/>
	<xsl:variable name="lab_g_txt_btn_remove" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_51')"/>
	<xsl:variable name="lab_g_txt_btn_preview" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_52')"/>
	<xsl:variable name="lab_cur_tcr" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_53')"/>
	<xsl:variable name="lab_root_training_center" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_54')"/>
	<xsl:variable name="lab_core" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_38')"/>
	<xsl:variable name="lab_name" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_37')"/>
	<xsl:variable name="lab_select_value_none" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_55')"/>
	<xsl:variable name="lab_select_value_valid" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_56')"/>
	<xsl:variable name="lab_select_value_out_date" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_57')"/>
	<xsl:variable name="lab_search" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_72')"/>
	<xsl:variable name="lab_create_cert_name" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_58')"/>
	<xsl:variable name="lab_create_cert_date" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_73')"/>
	<xsl:variable name="lab_create_up_name" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_59')"/>
	<xsl:variable name="lab_create_up_date" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_60')"/>
	<xsl:variable name="lab_publish_status" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_61')"/>
	<xsl:variable name="please_select" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'global.please_select')"/>
	<!-- ================================================================ -->
	<xsl:template name="content">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_CERTIFICATE_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_main_cert"/>
		</xsl:call-template>
		<xsl:variable name="cur_tcr_title">
			<xsl:choose>
				<xsl:when test="$cur_tcr_id != 0"><xsl:value-of select="//cur_training_center/title/text()"/></xsl:when>
				<xsl:otherwise><xsl:value-of select="$lab_root_training_center"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<table>
			<tr>
				<td width="35%" align="left">
					<xsl:call-template name="div_tree">
						<xsl:with-param name="lab_cur_tcr" select="$lab_cur_tcr" />
						<xsl:with-param name="lab_root_training_center" select="$lab_root_training_center"/>
						<xsl:with-param name="title" select="$cur_tcr_title" />
					</xsl:call-template>
					<input type="hidden" name="tc_enabled_ind" />
				</td>
				<td></td>
				<td width="20%" align="right" style="padding:10px 0 10px 10px;">
					<xsl:value-of select="$lab_status"/>：
					<select name="cert_status_sear" class="wzb-form-select" onchange="javascript:cert.search_cert(frmXml,{$cur_tcr_id})">
					
					  <option value ="none">
					 		
					  		<xsl:if test="$sea_status ='none'">
									<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
					  		<xsl:value-of select="$please_select"/>
					  		
					  </option>
					  
					  <option value ="valid">
					  		<xsl:if test="$sea_status ='valid'">
									<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
					  		<xsl:value-of select="$lab_select_value_valid"/>
					  		
					  </option>
					  <option value="out_date">
					  		<xsl:if test="$sea_status ='out_date'">
									<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
					  		<xsl:value-of select="$lab_select_value_out_date"/>
					  		
					  </option>
					</select>
				</td>
				<td align="right" width="22%">
					<div class="wzb-form-search">
						<input style="width:130px;" class="form-control" value="{$sea_core}" name="cert_core" size="11" type="text" />
						<input onclick="javascript:cert.search_cert(frmXml,{$cur_tcr_id})" value="" class="form-submit margin-right4" type="button" />
					</div>
				</td>
				<td align="right" width="2%">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_g_txt_btn_add"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:cert.ins_cert_prep('', <xsl:value-of select="$cur_tcr_id"/>)</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		
		<xsl:choose>
			<xsl:when test="$mod_cnt='0'">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_cert"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td width="20%">
							<xsl:value-of select="$lab_name"/>
						</td>
						<td width="6%" align="left">
							<xsl:value-of select="$lab_status"/>
						</td>
						<td width="8%" align="left">
							<xsl:value-of select="$lab_core"/>
						</td>
						<td width="10%" align="left">
							<xsl:value-of select="$lab_create_up_name"/>
						</td>
						<td width="12%" align="left">
							<xsl:value-of select="$lab_create_up_date"/>
						</td>
						<td width="7%" align="left">
							<xsl:value-of select="$lab_publish_status"/>
						</td>
						<td width="18%" align="right">
						</td>
					</tr>
					<xsl:apply-templates select="e_certificate/certificate">
						<xsl:with-param name="lab_active" select="$lab_active"/>
						<xsl:with-param name="lab_inactive" select="$lab_inactive"/>
						<xsl:with-param name="lab_g_txt_btn_edit_schedule" select="$lab_g_txt_btn_edit_schedule"/>
						<xsl:with-param name="lab_g_txt_btn_remove" select="$lab_g_txt_btn_remove"/>
						<xsl:with-param name="lab_g_txt_btn_preview" select="$lab_g_txt_btn_preview"/>
						<xsl:with-param name="lab_select_value_valid" select="$lab_select_value_valid"/>
						<xsl:with-param name="lab_select_value_out_date" select="$lab_select_value_out_date"/>
					</xsl:apply-templates>
				</table>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if test="$total > 0">
		<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="timestamp" select="$timestamp"/>
					<xsl:with-param name="total" select="$total"/>
					<xsl:with-param name="width"><xsl:value-of select="$wb_gen_table_width"/></xsl:with-param>
		</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="certificate">
		<xsl:param name="lab_active"/>
		<xsl:param name="lab_inactive"/>
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_g_txt_btn_preview"/>
		<xsl:param name="lab_g_txt_btn_edit_schedule"/>
		<xsl:param name="lab_select_value_valid"/>
		<xsl:param name="lab_select_value_out_date"/>
		<xsl:variable name="cfc_create_datetime" select="cfc_create_datetime"/>
		<xsl:variable name="update_timestamp" select="update_timestamp"/>
		<xsl:variable name="mod_id" select="@id"/>
		<tr>
			<td>
			   <xsl:value-of select="title"/>
			</td>	
			<td align="left">
			  <xsl:choose>
			       <xsl:when test="cfc_end_datetime/@id ='0'">
			          	<xsl:value-of select="$lab_select_value_valid"/>
			       </xsl:when>
			       <xsl:otherwise>
			        	<xsl:value-of select="$lab_select_value_out_date"/>
			       </xsl:otherwise>
			  </xsl:choose>
			</td>
			<td align="left">
			   <xsl:value-of select="cfc_core"/>
			</td>
			<td align="left">
			   <xsl:value-of select="cfc_update_user_name"/>
			</td>
			<td align="left">
			   <xsl:value-of select="substring($update_timestamp,1,16)"/>
			</td>
			<td align="left">
				<xsl:choose>
					<xsl:when test="status='ON'">
						<xsl:value-of select="$lab_active"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_inactive"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="right">
				<!-- preview -->
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_preview"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:cert.preview_cert('<xsl:value-of select="@id"/>')</xsl:with-param>
				</xsl:call-template>
				<!-- upd -->
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_edit_schedule"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:cert.ins_cert_prep('<xsl:value-of select="@id"/>')</xsl:with-param>
				</xsl:call-template>
				<!-- del -->
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="class">btn wzb-btn-blue</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_remove"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:cert.del_cert(<xsl:value-of select="$mod_id"/>,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
				</xsl:call-template>
			</td>
		</tr>
	</xsl:template>
		
	<!-- ================================================================ -->
</xsl:stylesheet>
