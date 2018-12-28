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
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_gen_input_file.xsl"/>
	<xsl:import href="utils/kindeditor.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/message_module">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_template_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1144')"/> 	
	<xsl:variable name="lab_template_title_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1144')"/>
	<xsl:variable name="lab_template_type" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1137')"/>
	<xsl:variable name="lab_template_subject" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1138')"/>
	<xsl:variable name="lab_content" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_content')"/> 
	<xsl:variable name="lab_template_web_message" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 1146)"/> 
	<xsl:variable name="lab_template_web_message_yes" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 1147)"/> 
	<xsl:variable name="lab_template_web_message_no" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 1148)"/> 
		
	<xsl:variable name="lab_template_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1139')"/>
	<xsl:variable name="lab_template_status_on" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1140')"/>
	<xsl:variable name="lab_template_status_off" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1141')"/>
	<xsl:variable name="lab_template_update_datetime" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1142')"/>
	<xsl:variable name="lab_remark" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1145')"/>
	<xsl:variable name="mtp_remark_label" select="/message_module/message_template/mtp_remark_label/text()"/>
	<xsl:variable name="lab_remark_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, $mtp_remark_label)"/>
	<xsl:variable name="lab_condition" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1150')"/>
	<xsl:variable name="mtp_type" select="/message_module/message_template/mtp_type/text()"/>
	<xsl:variable name="lab_condition_lable" select="concat('lab_condition_', $mtp_type)"/>
	<xsl:variable name="lab_condition_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding,$lab_condition_lable)"/>
	<xsl:variable name="lab_param_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1149')"/>
	
	<xsl:variable name="lab_update_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '708')"/> 	
	<xsl:variable name="lab_info_required" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '335')"/> 	
	<xsl:variable name="lab_g_form_btn_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '590')"/> 	
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/> 	
	<xsl:variable name="lab_msg_header_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_msg_header_image')"/> 	
	<xsl:variable name="lab_msg_footer_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_msg_footer_image')"/> 	
	<xsl:variable name="lab_keep_img" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_keep_img')"/> 	
	<xsl:variable name="lab_use_default" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_use_default')"/> 	
	<xsl:variable name="lab_upload_img" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_upload_img')"/> 	
	<xsl:variable name="lab_header_img_size" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_header_img_size')"/> 	
	<xsl:variable name="lab_footer_img_size" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_footer_img_size')"/> 	
	
	<xsl:variable name="lab_upd_msg_template" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_upd_msg_template')"/> 	
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_new_message.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" SRC="{$wb_js_path}wb_goldenman.js"/>
			<!--alert样式  -->
			<!-- <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> -->
			
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			<!--alert样式  end -->
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_{$wb_cur_lang}.js"></script>
			
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
 				var wbmsg = new wbNewMessage; 

				 function msg_icon_preview_change (file)
				{
					var id=$(file).attr('id');
					if (file.files[0] != '' ) {
						var file_ext =file.files[0].name.substring(
								file.files[0].name.lastIndexOf(".") + 1);
						if (file_ext != 'jpg' && file_ext != 'gif' && file_ext != 'png'
								&& file_ext != 'jpeg') {
							//alert(wb_msg_img_type_limit);
							return;
						}
					} else {
						return;
					}
					if(id =='msg_header_file' ) //邮件头部
					{
						 $('#header_img').attr('src', window.URL.createObjectURL(file.files[0]));
					}
					else // 邮件底部
					{
						$('#footer_img').attr('src', window.URL.createObjectURL(file.files[0]));
					}
				}
				
 			]]></script> 
 			<xsl:call-template name="kindeditor_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="content"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<form enctype="multipart/form-data" name="frmXml">
			<input name="module" type="hidden"/>
			<input name="cmd" type="hidden"/>			
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<input type="hidden" name="mtp_id" value="{//mtp_id}"/>
			<input type="hidden" name="curLan" value="{//cur_usr/@curLan}" />
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_MESSAGE_TEMPLATE_MAIN</xsl:with-param>
				<xsl:with-param name="parent_code">FTN_AMD_MESSAGE_TEMPLATE_MAIN</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text">
					<xsl:value-of select="$lab_update_button"/>
					<xsl:value-of select="$lab_template_title"/>
				</xsl:with-param>
			</xsl:call-template>
			
			<!-- <xsl:call-template name="wb_ui_head">
				<xsl:with-param name="text" select="$lab_template_title_desc"/>
			</xsl:call-template> -->
			<xsl:apply-templates select="message_template"/>
		</form>
	</xsl:template>
	
	<!-- =============================================================== -->
	<xsl:template match="message_template">
		<table>
			<tr>
				<td width="80%">
					<table>
				<!--主题-->
						<tr>
							<td class="wzb-form-label">
								<span class="wzb-form-star">*</span><xsl:value-of select="$lab_template_subject"/>：
								<input type="hidden" name="lab_template_subject" value="{$lab_template_subject}"></input>
							</td>
							<td class="wzb-form-control">
								<input type="Text" style="width:300px;" maxlength="50" name="mtp_subject" value="{mtp_subject}" class="wzb-inputText"/>
							</td>
						</tr>
				<!-- 内容 -->
						<tr>
							<td class="wzb-form-label" valign="top">
								<span class="wzb-form-star">*</span><xsl:value-of select="$lab_content"/>：
								<input type="hidden" name="lab_content" value="{$lab_content}"></input>
							</td>
							<td class="wzb-form-control">
								<xsl:call-template name="kindeditor_panel">
									<xsl:with-param name="body" select="body"/>
									<xsl:with-param name="frm">document.frmXml</xsl:with-param>
									<xsl:with-param name="fld_name">mtp_content</xsl:with-param>
									<xsl:with-param name="body">
										<xsl:value-of select="mtp_content"/>
									</xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
						<!--邮件头部图片-->
						<tr>
							<td class="wzb-form-label" valign="top">
								<xsl:value-of select="$lab_msg_header_image"/>：
							</td>
							<td class="wzb-form-control">
								<table>
									<tr>
										<td rowspan="4" valign="top">
											<img id="header_img" src="{mtp_header_img}" width="200px" height="50px"></img>
											<input type="hidden" name="header_img" value="{mtp_header_img}"/>
										</td>
										<td>
												
											<label for="remain_image">
												<input  type="radio" name="header_img_select" value="remain" checked="" onclick="changeImage(this,'header_img')"/>
												<xsl:value-of select="$lab_keep_img"/>
												<input type="hidden" value="{mtp_header_img}"/>
											</label>
											
										</td>
									</tr>
									<tr>
										<td>
											<label for="default_image">
												<input type="radio" name="header_img_select" value="default" onclick="changeImage(this,'header_img')"/>
												<xsl:value-of select="$lab_use_default"/>
												<input type="hidden" value="/msg/emailHeader.jpg"/>
											</label>
										</td>
									</tr>
									<tr>
										<td>
											<label for="local_image">
												<input type="radio" name="header_img_select" value="local" onclick="changeImage(this,'header_img')"/>
												<xsl:value-of select="$lab_upload_img"/>
											</label>
										</td>
									</tr>
									<tr>
										<td>	
											
							 				  	<xsl:call-template name="wb_gen_input_file">
													<xsl:with-param name="id">msg_header_file</xsl:with-param>
													<xsl:with-param name="name">mtp_header_img</xsl:with-param>
													<xsl:with-param name="onclick">$("input[name='header_img_select']").eq(2).click();</xsl:with-param>
													<xsl:with-param name="onchange">msg_icon_preview_change(this)</xsl:with-param>
												</xsl:call-template>
							 			 	<xsl:value-of select="$lab_header_img_size"/> 
							 			 		<!-- 
									 			 		<input   type="file" name="mtp_header_img" class="wzb-inputText " disabled=""/>
														<xsl:value-of select="$lab_header_img_size"/> 
							 			 		 -->
											 
											 
									 		 
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<!--邮件底部图片-->
						<tr>
							<td class="wzb-form-label" valign="top">
								<xsl:value-of select="$lab_msg_footer_image"/>：
							</td>
							<td class="wzb-form-control">
								<table>
									<tr>
										<td rowspan="4" valign="top">
											<img id="footer_img" src="{mtp_footer_img}" width="200px" height="20px"></img>
											<input type="hidden" name="footer_img" value="{mtp_footer_img}"/>
										</td>
										<td>
											<label for="remain_image">
												<input type="radio" name="footer_img_select" value="remain" checked="" onclick="changeImage(this,'footer_img')"/>
												<xsl:value-of select="$lab_keep_img"/>
												<input type="hidden" value="{mtp_footer_img}"/>
											</label>
										</td>
									</tr>
									<tr>
										<td>
											<label for="default_image">
												<input type="radio" name="footer_img_select" value="default" onclick="changeImage(this,'footer_img')"/>
												<xsl:value-of select="$lab_use_default"/>
												<input type="hidden" value="/msg/emailFooter.jpg"/>
											</label>
										</td>
									</tr>
									<tr>
										<td>
											<label for="local_image">
												<input type="radio"  name="footer_img_select" value="local" onclick="changeImage(this,'footer_img')"/>
												<xsl:value-of select="$lab_upload_img"/>
											</label>
										</td>
									</tr>
									<tr>
										<td>
											
												<xsl:call-template name="wb_gen_input_file">
													<xsl:with-param name="id">msg_footer_file</xsl:with-param>
													<xsl:with-param name="name">mtp_footer_img</xsl:with-param>
													<xsl:with-param name="onclick">$("input[name='footer_img_select']").eq(2).click();</xsl:with-param>
													<xsl:with-param name="onchange">msg_icon_preview_change(this)</xsl:with-param>	
												</xsl:call-template>
							 			 	<xsl:value-of select="$lab_footer_img_size"/>
							 			 	
							 			 	
							 			 	<!-- 
							 			 		<input type="file" name="mtp_footer_img" class="wzb-inputText" disabled=""/>
							 			 		<xsl:value-of select="$lab_footer_img_size"/>
											 -->
											
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<!--是否站内信-->
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_template_web_message"/>：
							</td>
							<td class="wzb-form-control">
								<label class="margin-right10">
									<input type="radio"  name="mtp_web_message_ind" value="1">
										<xsl:if test="mtp_web_message_ind='true'">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_template_web_message_yes"/>
								</label>
								<label >
									<input  type="radio" name="mtp_web_message_ind" value="0" >
										<xsl:if test="mtp_web_message_ind='false'">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_template_web_message_no"/>
								</label>
							</td>
						</tr>
						
						<!--是否启用-->
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_template_status"/>：
							</td>
							<td class="wzb-form-control">
								<label class="margin-right10">
									<input type="radio"  name="mtp_active_ind" value="1">
										<xsl:if test="mtp_active_ind='true'">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_template_status_on"/>
								</label>
								<label>
									<input  type="radio" name="mtp_active_ind" value="0">
										<xsl:if test="mtp_active_ind='false'">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_template_status_off"/>
								</label>
							</td>
						</tr>
						<tr>
							<td>
							</td>
							<td class="wzb-ui-module-text">
								<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
							</td>
						</tr>
						
						<!--条件-->
						<xsl:if test="$lab_condition_desc !=''">
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_condition"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:value-of select="$lab_condition_desc" disable-output-escaping="yes"/>
							</td>
						</tr>
						</xsl:if>
						
						<!--备注-->
						<xsl:if test="$lab_remark_desc !=''">
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_remark"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:value-of select="$lab_remark_desc" disable-output-escaping="yes"/>
							</td>
						</tr>
						</xsl:if>
					</table>
				</td>
				
				<td width="1%">
				
				
				</td>
				
				<td valign="top" width="19%" >
					<!--模板参数-->
					<xsl:if test="count(/message_module/params/message_param_name)>0">
						<table>
							<tr>
								<td align="left">
									<xsl:value-of select="$lab_param_title"/>：
								</td>
							</tr>
							<xsl:for-each select="/message_module/params/message_param_name">
							<xsl:variable name="mpn_name_desc_label" select="mpn_name_desc/text()"/>
							<xsl:variable name="lab_mpn_name_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, $mpn_name_desc_label)"/> 
								<tr>
									<td align="left">
										<span class="Text" style="color:blue"><xsl:value-of select="mpn_name"/>：</span><xsl:value-of select="$lab_mpn_name_desc"/>
									</td>
								</tr>
							</xsl:for-each>
						</table>
					</xsl:if>
				</td>
			</tr>
		</table>
	<!-- =============================================================== -->

		
	<!-- =============================================================== -->
								
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:wbmsg.upd_template(document.frmXml,  '<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
			</xsl:call-template>
		</div>
		
	</xsl:template>
</xsl:stylesheet>


