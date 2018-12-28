<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_gen_input_file.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/supplier_module">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_supplier_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '994')"/> 	
	<xsl:variable name="lab_supplier_title_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '994_1')"/>
	<xsl:variable name="lab_supplier_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '995')"/> 	
	<xsl:variable name="lab_link_url" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'sp1025')"/> 	
	<xsl:variable name="lab_supplier_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '996')"/> 	
	<xsl:variable name="lab_supplier_course" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '998')"/> 	
<!-- 	<xsl:variable name="lab_sce_itm_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1000')"/> 	 -->
	<xsl:variable name="lab_scm_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1002')"/> 	
	<xsl:variable name="lab_supplier_address" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1003')"/> 	
	<xsl:variable name="lab_operate" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1004')"/> 	
	<xsl:variable name="lab_search_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '999')"/> 	
	<xsl:variable name="lab_view_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1005')"/> 	
	<!-- 法定代表人 -->
	<xsl:variable name="lab_spl_representative" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1006')"/> 	
	<!-- 成立时间 -->
	<xsl:variable name="lab_spl_established_date" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1007')"/> 	
	<!-- 注册资金 -->
	<xsl:variable name="lab_spl_registered_capital" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1008')"/> 	
	<!-- 机构类型 -->
	<xsl:variable name="lab_spl_type" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1009')"/> 	
	<!-- 机构地址 -->
	<xsl:variable name="lab_spl_address" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1010')"/> 	
	<!-- 联系人 -->
	<xsl:variable name="lab_spl_contact" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1011')"/> 	
	<!-- 联系人办公电话 -->
	<xsl:variable name="lab_spl_tel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1012')"/> 	
	<!-- 联系人手机号码 -->
	<xsl:variable name="lab_spl_mobile" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1013')"/> 	
	<!-- 联系邮箱 -->
	<xsl:variable name="lab_spl_email" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1014')"/> 		
	<!-- 机构总人数 -->
	<xsl:variable name="lab_spl_total_staff" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1015')"/> 	
	<!-- 专职讲师人数 -->
	<xsl:variable name="lab_spl_full_time_inst" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1016')"/> 	
	<!-- 兼职讲师人数-->
	<xsl:variable name="lab_spl_part_time_inst" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1017')"/> 	
	<!-- 擅长领域 -->
	<xsl:variable name="lab_spl_expertise" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1018')"/> 	
	<!-- 核心课程-->
	<xsl:variable name="lab_spl_course" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1019')"/> 	
	<!-- 附件 -->
	<xsl:variable name="lab_spl_attachment" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1020')"/> 	
		
	<xsl:variable name="lab_spl_experience" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1024')"/>
	<xsl:variable name="lab_spl_experience_2" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1024_1')"/> 	

	<xsl:variable name="lab_btn_delete" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '257')"/> 	
	
	<xsl:variable name="lab_sce_startdate" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1025')"/> 	
	<xsl:variable name="lab_sce_enddate" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1026')"/> 	
	<xsl:variable name="lab_sce_itm_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1027')"/> 	
	<xsl:variable name="lab_sce_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1028')"/> 	
	<xsl:variable name="lab_sce_dpt" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1029')"/> 	
	<xsl:variable name="lab_smc_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1030')"/> 	
	<xsl:variable name="lab_smc_inst" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1031')"/> 	
	<xsl:variable name="lab_smc_price" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1032')"/> 		
	
	
	<xsl:variable name="lab_have_cooperation" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'a_have')"/> 	
	<xsl:variable name="lab_had_meeting" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'c_meet')"/> 	
	<xsl:variable name="lab_bid_pass" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'b_pass')"/> 	
	<xsl:variable name="lab_first_contact" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'd_contact')"/> 	
	<xsl:variable name="lab_exited" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'e_exited')"/> 	
	<xsl:variable name="lab_none" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'NONE')"/> 	
		
	<xsl:variable name="lab_file_original" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '801')"/> 	
	<xsl:variable name="lab_file_delete" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '802')"/> 	
	<xsl:variable name="lab_file_update" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '803')"/> 	
	
	<xsl:variable name="lab_supplier_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '704')"/> 	
	<xsl:variable name="lab_update_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '708')"/> 	
	<xsl:variable name="lab_add_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '473')"/> 	
	<xsl:variable name="lab_del_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '257')"/> 	
	
	<xsl:variable name="lab_supplier_status_on" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '765')"/> 	
	<xsl:variable name="lab_supplier_status_off" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '766')"/> 	
	<xsl:variable name="lab_info_required" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '335')"/> 	
	<xsl:variable name="lab_g_form_btn_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '590')"/> 	
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/> 	
	
	<xsl:variable name="lab_keep_media" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '801')"/> 	
	<xsl:variable name="lab_remove_media" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '802')"/> 	
	<xsl:variable name="lab_change_to" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '803')"/>
	
	
	<!-- ============================================================= -->
	<xsl:variable name="ins_or_upd">
		<xsl:choose>
			<xsl:when test="/supplier_module/supplier/spl_id &gt;= 1">UPD</xsl:when>
			<xsl:otherwise>INS</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_supplier.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
 				var wbSupplier = new wbSupplier;
				 				
				$(function(){
						none_title()
						course_none_title();		
				})			
				$(function() {
					for ( var i = 1; i <= 10; i++) {
						$("input[name=r_splAttachment" + i + "]:eq(0)").attr("checked",
								'checked');
					}
				/*	$("input[type=radio]").each(function(){ var aName = $(this).attr("name");
						aName = aName.substring(2,aName.length);
						$("input[name='"+aName+"']").attr("disabled",true);
					});*/
					 
					$("input[ref=attach]").attr("disabled", true);
					$("input[type=radio]").click(function() {
						// alert($(this).attr("value"));
						var aVal = $(this).val();
						var aName = $(this).attr("name");
						aName = aName.substring(2, aName.length);
						if (aVal == 'update') {
							$("input[name='" + aName + "']").attr("disabled", false);
						} else {
							$("input[name='" + aName + "']").attr("disabled", true);
						}
					});
				})
 				
 			]]></script> 
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="content"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<form enctype="multipart/form-data" name="frmXml">
			<input name="module" type="hidden"/>
			<input name="cmd" type="hidden">			
				<xsl:attribute name="value">
					<xsl:choose>
						<xsl:when test="$ins_or_upd = 'INS'">add_supplier</xsl:when>
						<xsl:otherwise>add_supplier</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
			</input>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<input type="hidden" name="splId" value="{//spl_id}"/>
			<input type="hidden" name="sceString" />
			<input type="hidden" name="smcString" />
			<input type="hidden" name="curLan" value="{//cur_usr/@curLan}" />
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_SUPPLIER_MAIN</xsl:with-param>
				<xsl:with-param name="parent_code">FTN_AMD_SUPPLIER_MAIN</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text">
					<xsl:choose>
						<xsl:when test="$ins_or_upd='INS'">
							<xsl:value-of select="$lab_add_button"/>
						</xsl:when>
						<xsl:when test="$ins_or_upd='UPD'">
							<xsl:value-of select="$lab_update_button"/>
						</xsl:when>
					</xsl:choose>

					<xsl:value-of select="$lab_supplier_title"/>
				</xsl:with-param>
			</xsl:call-template>
			
<!-- 			<xsl:call-template name="wb_ui_line"/> -->
			<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="text" select="$lab_supplier_title_desc"/>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_line"/>
			<xsl:apply-templates select="supplier"/>
		</form>
	</xsl:template>

	
	<!-- =============================================================== -->
	<xsl:template match="supplier">
		<table>
			<!--状态-->
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_supplier_status"/>：
				</td>
				<td class="wzb-form-control">
						<select name="splStatus" class="wzb-select" >
						<xsl:for-each select="//status">
									<option value="{value}"><xsl:value-of select="name"/></option>	
						</xsl:for-each>
						</select>
				</td>
			</tr>
			
			<!--名称-->
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_supplier_name"/>：
					<input type="hidden" name="lab_spl_name" value="{$lab_supplier_name}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:300px;" maxlength="50" name="splName" value="{spl_name}" class="wzb-inputText"/>
				</td>
			</tr>
			
			<!--法定代表人-->
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_spl_representative"/>：
					<input type="hidden" name="lab_spl_representative" value="{$lab_spl_representative}"></input>
				</td>
				<td class="wzb-form-control">
						<input type="Text" style="width:300px;" maxlength="16" name="splRepresentative" value="{spl_representative}" class="wzb-inputText"/>
				</td>
			</tr>

			<!-- 成立时间 -->
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_spl_established_date"/>：
					<input type="hidden" name="lab_spl_established_date" value="{$lab_spl_established_date}"></input>
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">splEstablishedDate</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">splEstablishedDate</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
						<xsl:with-param name="timestamp">
							<xsl:value-of select="//spl_established_date"/>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<!-- 注册资金 -->
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_spl_registered_capital"/>：
					<input type="hidden" name="lab_spl_registered_capital" value="{$lab_spl_registered_capital}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:150px;" maxlength="20" name="splRegisteredCapital" value="{spl_registered_capital}" class="wzb-inputText"/>
				</td>
			</tr>
			<!-- 机构类型 -->
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_spl_type"/>：
					<input type="hidden" name="lab_spl_type" value="{$lab_spl_type}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:150px;" maxlength="16" name="splType" value="{spl_type}" class="wzb-inputText"/>
				</td>
			</tr>
	
			<!-- 机构地址 -->
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_spl_address"/>：
					<input type="hidden" name="lab_spl_address" value="{$lab_spl_address}"></input>
				</td>
				<td class="wzb-form-control">
					<textarea rows="4" cols="20" style="width:300px;" onKeyUp="if(this.value.length > 200) this.value=this.value.substr(0,200)" maxlength="200" name="splAddress" value="{spl_address}" class="wzb-inputTextArea" ><xsl:value-of select="spl_address"/></textarea>
				</td>
			</tr>	
			<!-- 联系人 -->
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_spl_contact"/>：
					<input type="hidden" name="lab_spl_contact" value="{$lab_spl_contact}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:300px;" maxlength="16" name="splContact" value="{spl_contact}" class="wzb-inputText"/>
				</td>
			</tr>	
	
			<!-- 联系人办公电话 -->
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_spl_tel"/>：
					<input type="hidden" name="lab_spl_tel" value="{$lab_spl_tel}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:300px;" maxlength="16" name="splTel" value="{spl_tel}" class="wzb-inputText"/>
				</td>
			</tr>
			<!-- 联系人手机号码 -->
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_spl_mobile"/>：
					<input type="hidden" name="lab_spl_mobile" value="{$lab_spl_mobile}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:300px;" maxlength="16" name="splMobile" value="{spl_mobile}" class="wzb-inputText"/>
				</td>
			</tr>	
			<!-- 联系邮箱 -->
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_spl_email"/>：
					<input type="hidden" name="lab_spl_email" value="{$lab_spl_email}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:300px;" maxlength="50" name="splEmail" value="{spl_email}" class="wzb-inputText"/>
				</td>
			</tr>	
			<!-- 机构总人数 -->
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_spl_total_staff"/>：
					<input type="hidden" name="lab_spl_total_staff" value="{$lab_spl_total_staff}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:150px;" maxlength="9" name="splTotalStaff" value="{spl_total_staff}" class="wzb-inputText"/>
				</td>
			</tr>	
			<!-- 专职讲师人数 -->
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_spl_full_time_inst"/>：
					<input type="hidden" name="lab_spl_full_time_inst" value="{$lab_spl_full_time_inst}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:150px;" maxlength="9" name="splFullTimeInst" value="{spl_full_time_inst}" class="wzb-inputText"/>
				</td>
			</tr>	
			<!-- 兼职讲师人数-->
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_spl_part_time_inst"/>：
					<input type="hidden" name="lab_spl_part_time_inst" value="{$lab_spl_part_time_inst}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:150px;" maxlength="9" name="splPartTimeInst" value="{spl_part_time_inst}" class="wzb-inputText"/>
				</td>
			</tr>	
			<!-- 擅长领域 -->
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_spl_expertise"/>：
					<input type="hidden" name="lab_spl_expertise" value="{$lab_spl_expertise}"></input>
				</td>
				<td class="wzb-form-control">
					<textarea cols="20" onKeyUp="if(this.value.length > 500) this.value=this.value.substr(0,500)" rows="4" style="width:300px;" maxlength="500" name="splExpertise" value="{spl_expertise}" class="wzb-inputTextArea"><xsl:value-of select="spl_expertise"/></textarea>
				</td>
			</tr>
			<!-- 核心课程-->
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_supplier_course"/>：
					<input type="hidden" name="lab_spl_course" value="{$lab_supplier_course}"></input>
				</td>
				<td class="wzb-form-control">
					<textarea cols="20" onKeyUp="if(this.value.length > 500) this.value=this.value.substr(0,500)" rows="4" style="width:300px;" maxlength="500" name="splCourse" value="{spl_course}" class="wzb-inputTextArea" ><xsl:value-of select="spl_course"/></textarea>
				</td>
			</tr>
			<!-- 附件 -->

			<xsl:call-template name="fileField">
				<xsl:with-param name="fieldName">splAttachment1</xsl:with-param>
				<xsl:with-param name="fieldValue"><xsl:value-of select="//spl_attachment_1"/></xsl:with-param>
				<xsl:with-param name="index">1：</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="fileField">
				<xsl:with-param name="fieldName">splAttachment2</xsl:with-param>
				<xsl:with-param name="fieldValue"><xsl:value-of select="//spl_attachment_2"/></xsl:with-param>
				<xsl:with-param name="index">2：</xsl:with-param>
			</xsl:call-template>			
			<xsl:call-template name="fileField">
				<xsl:with-param name="fieldName">splAttachment3</xsl:with-param>
				<xsl:with-param name="fieldValue"><xsl:value-of select="//spl_attachment_3"/></xsl:with-param>
				<xsl:with-param name="index">3：</xsl:with-param>
			</xsl:call-template>			
			<xsl:call-template name="fileField">
				<xsl:with-param name="fieldName">splAttachment4</xsl:with-param>
				<xsl:with-param name="fieldValue"><xsl:value-of select="//spl_attachment_4"/></xsl:with-param>
				<xsl:with-param name="index">4：</xsl:with-param>
			</xsl:call-template>			
			<xsl:call-template name="fileField">
				<xsl:with-param name="fieldName">splAttachment5</xsl:with-param>
				<xsl:with-param name="fieldValue"><xsl:value-of select="//spl_attachment_5"/></xsl:with-param>
				<xsl:with-param name="index">5：</xsl:with-param>
			</xsl:call-template>			
			<xsl:call-template name="fileField">
				<xsl:with-param name="fieldName">splAttachment6</xsl:with-param>
				<xsl:with-param name="fieldValue"><xsl:value-of select="//spl_attachment_6"/></xsl:with-param>
				<xsl:with-param name="index">6：</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="fileField">
				<xsl:with-param name="fieldName">splAttachment7</xsl:with-param>
				<xsl:with-param name="fieldValue"><xsl:value-of select="//spl_attachment_7"/></xsl:with-param>
				<xsl:with-param name="index">7：</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="fileField">
				<xsl:with-param name="fieldName">splAttachment8</xsl:with-param>
				<xsl:with-param name="fieldValue"><xsl:value-of select="//spl_attachment_8"/></xsl:with-param>
				<xsl:with-param name="index">8：</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="fileField">
				<xsl:with-param name="fieldName">splAttachment9</xsl:with-param>
				<xsl:with-param name="fieldValue"><xsl:value-of select="//spl_attachment_9"/></xsl:with-param>
				<xsl:with-param name="index">9：</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="fileField">
				<xsl:with-param name="fieldName">splAttachment10</xsl:with-param>
				<xsl:with-param name="fieldValue"><xsl:value-of select="//spl_attachment_10"/></xsl:with-param>
				<xsl:with-param name="index">10：</xsl:with-param>
			</xsl:call-template>
			
			<tr>
				<td class="wzb-form-label">
				</td>
				<td class="wzb-ui-module-text">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		</table>
	<!-- 合作经历 -->
		<table class="margin-top28">
		<tr>
			<td>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_spl_experience"/>
		</xsl:call-template>
		</td>
		<td  align="right">
			<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name" select="concat($lab_add_button,$lab_spl_experience_2)"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:add_proj_input()</xsl:with-param>
						<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
			</xsl:call-template>
		</td>
		</tr>
		</table>
		<xsl:call-template name="wb_ui_line"/>
		
		<xsl:call-template name="spl_project"/>
	<!-- 核心课程 -->
		<table class="margin-top28">
		<tr>
			<td>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_supplier_course"/>
		</xsl:call-template>
		</td>
		<td   align="right">
			<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name" select="concat($lab_add_button,$lab_spl_course)"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:add_course_input()</xsl:with-param>
						<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
			</xsl:call-template>
		</td>
		</tr>
		</table>
		<xsl:call-template name="wb_ui_line"/>
		
		<xsl:call-template name="spl_course"/>
		
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:wbSupplier.ins_upd_supplier(document.frmXml,  '<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
			</xsl:call-template>
		</div>
		
	</xsl:template>
	
	<!-- =============================================================== -->
	
		<xsl:template name="spl_project">
		<table>
			<tr>
				<td>
					<table id="spl_project_table">
						<tr>
							<td  align="left" style="width:190px;display:black;color:#999999;">
								<xsl:value-of select="$lab_sce_startdate"/>						
								<input type="hidden" name="lab_sce_startdate" value="{$lab_sce_startdate}"></input>
							</td>
							<td  align="left" style="width:190px;display:black;color:#999999;">
								<xsl:value-of select="$lab_sce_enddate"/>
								<input type="hidden" name="lab_sce_enddate" value="{$lab_sce_enddate}"></input>
							</td>
							<td  align="center" style="width:150px;display:black;color:#999999;">
								<xsl:value-of select="$lab_sce_itm_name"/>
								<input type="hidden" name="lab_sce_itm_name" value="{$lab_sce_itm_name}"></input>
							</td>
							<td align="center" style="width:150px;display:black;color:#999999;">
								<xsl:value-of select="$lab_sce_desc"/>
								<input type="hidden" name="lab_sce_desc" value="{$lab_sce_desc}"></input>
							</td>
							<td align="center" style="width:150px;display:black;color:#999999;">
								<xsl:value-of select="$lab_sce_dpt"/>
								<input type="hidden" name="lab_sce_dpt" value="{$lab_sce_dpt}"></input>
							</td>
							<td align="right" style="width:40px;display:black;color:#999999;">
								<xsl:value-of select="$lab_operate"/>
								<input type="hidden" name="lab_operate" value="{$lab_operate}"></input>
							</td>																
						</tr>
						<xsl:for-each select="//supplier_cooperation_experience">
						<xsl:variable name="count"><xsl:value-of select="position()-1"></xsl:value-of></xsl:variable>
						<tr class="proj_tr" >
								<td align="left"  valign="top" style="padding:10px 0 0 0;">
									<xsl:call-template name="display_form_input_time">
										<xsl:with-param name="fld_name">sceStartDate_s<xsl:value-of select="$count"/></xsl:with-param>
										<xsl:with-param name="hidden_fld_name">sceStartDate_s<xsl:value-of select="$count"/></xsl:with-param>
										<xsl:with-param name="show_label">Y</xsl:with-param>
										<xsl:with-param name="timestamp">
											<xsl:value-of select="sce_start_date"/>
										</xsl:with-param>
										<xsl:with-param name="class">wzb-inputText sceStartDate</xsl:with-param>
									</xsl:call-template>
								</td>
								
								<td  align="left" valign="top" style="padding:10px 0 0 0;">
								<!-- <input type="Text" style="width:135px;" maxlength="50" name="sceEndDate" value="{sce_end_date}" class="wzb-inputText"/> -->
									<xsl:call-template name="display_form_input_time">
										<xsl:with-param name="fld_name">sceEndDate_e<xsl:value-of select="$count"></xsl:value-of></xsl:with-param>
										<xsl:with-param name="hidden_fld_name">sceEndDate_e<xsl:value-of select="$count"></xsl:value-of></xsl:with-param>
										<xsl:with-param name="show_label">Y</xsl:with-param>
										<xsl:with-param name="timestamp">
											<xsl:value-of select="sce_end_date"/>
										</xsl:with-param>
										<xsl:with-param name="class">wzb-inputText sceEndDate</xsl:with-param>
									</xsl:call-template>
								</td>
								<td align="center"  valign="top" width="150" style="padding:10px 0 0 5px;">
									<input type="Text" maxlength="30" style="width:135px;" name="sceItmName{$count}" value="{sce_itm_name}" class="wzb-inputText sceItmName"/>
								</td>									
								<td  align="center" valign="top" width="150" style="padding:10px 0 0 5px;">
									<input type="Text"  maxlength="200" style="width:135px;" name="sceDesc{$count}" value="{sce_desc}" class="wzb-inputText sceDesc"/>
								</td>
								<td align="center" valign="top" width="150" style="padding:10px 0 0 5px;">
									<input type="hidden"  maxlength="50" style="width:135px;" name="sceUpdateDatetime" value="{sce_update_datetime}" class="wzb-inputText sceUpdateDatetime"/>
									<input type="Text"  maxlength="30" style="width:135px;" name="sceDpt{$count}" value="{sce_dpt}" class="wzb-inputText sceDpt"/>
								</td>				
								<td  align="right" valign="top" style="padding:10px 0 0 0;">
									<label style="display:none;visibility:hidden;">[</label><a class="btn wzb-btn-blue" href="javascript:;" onclick="del_proj_input(this);"><xsl:value-of select="$lab_btn_delete"/></a><label style="display:none;visibility:hidden;">]</label>
								</td>			
						</tr>
						</xsl:for-each>						
					</table>
				</td>
			</tr>
		</table>
		</xsl:template>
		
		<!-- =============================================================== -->
		
		<xsl:template name="spl_course">
		<table>
			<tr>
				<td >
					<table id="spl_course_table">
						<tr style="color:#999999;">
							<td  align="left" width="30%" >
								<xsl:value-of select="$lab_smc_name"/>
								<input type="hidden" name="lab_smc_name" value="{$lab_smc_name}"></input>
							</td>
							<td  align="left" width="30%">
								<xsl:value-of select="$lab_smc_inst"/>
								<input type="hidden" name="lab_smc_inst" value="{$lab_smc_inst}"></input>
							</td>
							<td  align="left" width="30%">
								<xsl:value-of select="$lab_smc_price"/>
								<input type="hidden" name="lab_smc_price" value="{$lab_smc_price}"></input>
							</td>
							<td align="right" width="10%">
								<xsl:value-of select="$lab_operate"/>
								<input type="hidden" name="lab_operate" value="{$lab_operate}"></input>
							</td>															
						</tr>
						<xsl:for-each select="//supplier_main_course">
						<xsl:variable name="count"><xsl:value-of select="position()-1"></xsl:value-of></xsl:variable>						
						<tr class="course_tr" >
								<td align="left"  valign="top" style="padding:10px 0 0 0;">
									<input type="Text" style="width:135px;" maxlength="50" name="smcName{$count}" value="{smc_name}" class="wzb-inputText smcName"/>
								</td>
								<td  align="left" valign="top" style="padding:10px 0 0 0;">
									<input type="Text" style="width:135px;" maxlength="30" name="smcInst{$count}" value="{smc_inst}" class="wzb-inputText smcInst"/>
								</td>
								<td align="left" valign="top" style="padding:10px 0 0 0;">
									<input type="Text" style="width:135px;" maxlength="10" name="smcPrice{$count}" value="{smc_price_show}" class="wzb-inputText smcPrice"/>
									<input type="hidden" style="width:135px;" maxlength="50" name="smcUpdateDatetime" value="{smc_update_datetime}" class="wzb-inputText smcUpdateDatetime"/>
								</td>				
								<td  align="right" valign="top" style="padding:10px 0 0 0;">
									<label style="display:none;visibility:hidden;">[</label><a class="btn wzb-btn-blue" href="javascript:;" onclick="del_course_input(this);"><xsl:value-of select="$lab_btn_delete"/></a><label style="display:none;visibility:hidden;">]</label>		
								</td>			
						</tr>
						</xsl:for-each>						
					</table>
				</td>
			</tr>
		</table>
		</xsl:template>	
		
		
		<xsl:template name="fileField">
			<xsl:param name="fieldName"/>
		    <xsl:param name="fieldValue"/>
			<xsl:param name="index"/>
			<xsl:if test="$fieldValue != ''">
				<tr>
					<xsl:if test="$fieldValue != ''">			
					<td style="padding:10px 0; text-align:right; color:#666;"  valign="top" rowspan="2">
						<xsl:value-of select="$lab_spl_attachment"/><xsl:value-of select="$index"/>
						<input type="hidden" name="lab_spl_attachment" value="{$lab_spl_attachment}"></input>
	
					</td>
					</xsl:if>
					<td valign="top" style="padding:10px 0 10px 10px; color:#333;">
						<a class="Text" href="{$fieldValue}" target="_blank"><xsl:value-of select="substring-after(substring-after(substring-after($fieldValue,'/'),'/'),'/')"/></a>
					</td>
				</tr>				
			</xsl:if>
			<tr>
				<xsl:if test="$fieldValue = ''">			
				<td style="padding:10px 0; text-align:right; color:#666;"  valign="top">
					<xsl:value-of select="$lab_spl_attachment"/><xsl:value-of select="$index"/>
					<input type="hidden" name="lab_spl_attachment" value="{$lab_spl_attachment}"></input>

				</td>
				</xsl:if>
				<td  valign="top" style="padding:10px 0 10px 10px; color:#333;">
					<xsl:choose>
						<xsl:when test="$fieldValue != ''">
							<input class="attach" type="radio"   name="r_{$fieldName}" value="original" /><xsl:value-of select="$lab_file_original"/>&#160;
							<input class="attach" type="radio"   name="r_{$fieldName}" value="delete" /><xsl:value-of select="$lab_file_delete"/>&#160;
							<input class="attach" type="radio"   name="r_{$fieldName}" value="update" /><xsl:value-of select="$lab_file_update"/>&#160;
							<xsl:call-template name="wb_gen_input_file">
								<xsl:with-param name="name" select="$fieldName"/>
							</xsl:call-template>
							<!-- <input type="file" ref="attach" style="width:300px;" maxlength="50" name="{$fieldName}"  class="wzb-inputText"/> -->
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="wb_gen_input_file">
								<xsl:with-param name="name" select="$fieldName"/>
							</xsl:call-template>
							<!-- <input type="file" style="width:300px;" maxlength="50" name="{$fieldName}"  class="wzb-inputText"/> -->
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:template>
	
</xsl:stylesheet>


