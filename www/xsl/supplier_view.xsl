<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- ============================================================= -->
	<xsl:variable name="supplierName" select="//spl_name"/>
	<xsl:variable name="order_by" select="//pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="//pagination/@sort_order"/>
	<xsl:variable name="sel_status" select="//sel_statu/@name"/>
	<xsl:variable name="sort_order">
		<xsl:choose>
			<xsl:when test="$cur_order = 'asc'">desc</xsl:when>
			<xsl:otherwise>asc</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_ftn_supplier_main" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_ftn_SUPPLIER_MAIN')"/> 	
	<xsl:variable name="lab_supplier_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '994')"/> 	
	<xsl:variable name="lab_supplier_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '995')"/> 	
	<xsl:variable name="lab_link_url" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'sp1025')"/> 	
	<xsl:variable name="lab_supplier_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '996')"/> 	
	<xsl:variable name="lab_supplier_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '997')"/> 	
	<xsl:variable name="lab_supplier_course" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '998')"/> 		
	<xsl:variable name="lab_scm_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1002')"/> 	
	<xsl:variable name="lab_supplier_address" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1003')"/> 	
	<xsl:variable name="lab_operate" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1004')"/> 	
	<xsl:variable name="lab_no_supplier" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1001')"/> 	
	<xsl:variable name="lab_no_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1021')"/> 	
	
	<xsl:variable name="lab_search_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '999')"/> 	
	<xsl:variable name="lab_view_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1005')"/> 	
	
	
			
	<xsl:variable name="lab_spl_experience" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1024')"/> 	
	
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
	
	
	<xsl:variable name="lab_supplier_list" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1021')"/> 	
	<xsl:variable name="lab_go_sup_discuss" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1023')"/> 	
	
	<xsl:variable name="lab_update_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '708')"/> 	
	<xsl:variable name="lab_del_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '257')"/> 	
	<xsl:variable name="lab_add_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '473')"/> 	
	<xsl:variable name="lab_supplier_status_on" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '765')"/> 	
	<xsl:variable name="lab_supplier_status_off" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '766')"/> 	
	<!-- =============================================================== -->
	<xsl:template match="/supplier_module">
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
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>			
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_supplier.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				wbSupplier = new wbSupplier; 
 			]]></script> 
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="content"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	
	<xsl:template name="supplier_sel_status">
		<xsl:param name="s_statu"></xsl:param>
		<xsl:param name="lab_name"></xsl:param>
		<xsl:param name="lab_value"></xsl:param>
		<xsl:choose>
			<xsl:when test="$s_statu != '' ">
				<option value="{$lab_value}" selected="selected" ><xsl:value-of select="$lab_name"/></option>	
			</xsl:when>
			<xsl:otherwise>
				<option value="{$lab_value}" ><xsl:value-of select="$lab_name"/></option>							
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- =============================================================== -->
	<xsl:template name="content" >
		<form name="frmXml">
			<input type="hidden" name="splId" value="{//spl_id}"/>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<input type="hidden" name="module"/>
			<input type="hidden" name="cmd"/>
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_SUPPLIER_MAIN</xsl:with-param>
				<xsl:with-param name="parent_code">FTN_AMD_SUPPLIER_MAIN</xsl:with-param>
				<xsl:with-param name="page_title"><xsl:value-of select="//spl_name"/></xsl:with-param>
			</xsl:call-template>
			<!-- <xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text" select="$lab_ftn_supplier_main"/>
			</xsl:call-template> -->
			<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="text" select="$lab_supplier_title"/>
				<xsl:with-param name="extra_td">
					<td align="right">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_go_sup_discuss"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:wbSupplier.comm.get_supplier_comment_view('<xsl:value-of select="supplier/spl_id"/>')</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_update_button"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:wbSupplier.get_supplier_prep('<xsl:value-of select="supplier/spl_id"/>')</xsl:with-param>
						</xsl:call-template>
						<xsl:choose>
							<xsl:when test="//spl_status = 'f_deleted'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_del_button"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:;</xsl:with-param>								
									<xsl:with-param name="style"></xsl:with-param>																
								</xsl:call-template>
							</xsl:when>
							
							<xsl:otherwise>
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_del_button"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:wbSupplier.supplier_del_id(document.frmXml)</xsl:with-param>								
								</xsl:call-template>
							
							</xsl:otherwise>
						</xsl:choose>

					</td>
				</xsl:with-param>
			</xsl:call-template>
		<!-- <xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_supplier_title"/>
		</xsl:call-template> -->
		<xsl:call-template name="wb_ui_line"/>
		
				<table>
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_supplier_status"/>：
							</td>
							<td class="wzb-form-control">							
								<xsl:choose>
									<xsl:when test="//spl_status = 'a_have' ">
											<xsl:value-of select="$lab_have_cooperation"/>
									</xsl:when>
									<xsl:when test="//spl_status = 'c_meet'">
											<xsl:value-of select="$lab_had_meeting"/>
									</xsl:when>
									<xsl:when test="//spl_status = 'b_pass'">
											<xsl:value-of select="$lab_bid_pass"/>
									</xsl:when>
									<xsl:when test="//spl_status = 'd_contact'">
											<xsl:value-of select="$lab_first_contact"/>
									</xsl:when>
									<xsl:when test="//spl_status = 'e_exited'">
										<xsl:value-of select="$lab_exited"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_none"></xsl:value-of>
									</xsl:otherwise>
								</xsl:choose>

							</td>
						</tr>
						<tr>
							<td class="wzb-form-label" >
								<xsl:value-of select="$lab_supplier_name"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:value-of select="//spl_name"/>
							</td>
						</tr>
						
						<!-- 法定代表人 -->
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_spl_representative"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:value-of select="//spl_representative"/>
							</td>
						</tr>
	<!-- 成立时间 -->
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_spl_established_date"/>：
							</td>
							<td class="wzb-form-control">
							<xsl:value-of select="substring(//spl_established_date,0,11)"/>
							</td>
						</tr>	
	<!-- 注册资金 -->
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_spl_registered_capital"/>：
							</td>
							<td class="wzb-form-control">
							<xsl:value-of select="//spl_registered_capital"/>
							</td>
						</tr>		
	<!-- 机构类型 -->
						<tr>
							<td class="wzb-form-label">
								<span class="TitleText">
									<xsl:value-of select="$lab_spl_type"/>：
								</span>								
							</td>
							<td class="wzb-form-control">
							<xsl:value-of select="//spl_type"/>
							</td>
						</tr>				
	<!-- 机构地址 -->
						<tr>
							<td class="wzb-form-label">							
								<xsl:value-of select="$lab_spl_address"/>：
							</td>
							<td class="wzb-form-control">
							<xsl:value-of select="//spl_address"/>
							</td>
						</tr>
	<!-- 联系人 -->
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_spl_contact"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:value-of select="//spl_contact"/>
							</td>
						</tr>	
	<!-- 联系人办公电话 -->
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_spl_tel"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:value-of select="//spl_tel"/>
							</td>
						</tr>
	<!-- 联系人手机号码 -->
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_spl_mobile"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:value-of select="//spl_mobile"/>
							</td>
						</tr>	
	<!-- 联系邮箱 -->
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_spl_email"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:value-of select="//spl_email"/>
							</td>
						</tr>	
	<!-- 机构总人数 -->
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_spl_total_staff"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:value-of select="//spl_total_staff"/>
							</td>
						</tr>	
	<!-- 专职讲师人数 -->
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_spl_full_time_inst"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:value-of select="//spl_full_time_inst"/>
							</td>
						</tr>	
	<!-- 兼职讲师人数-->
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_spl_part_time_inst"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:value-of select="//spl_part_time_inst"/>
							</td>
						</tr>		
	<!-- 擅长领域 -->
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_spl_expertise"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:value-of select="//spl_expertise"/>
							</td>
						</tr>		
	<!-- 核心课程-->
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_spl_course"/>：
							</td>
							<td class="wzb-form-control" >
								<xsl:value-of select="//spl_course"/>
							</td>
						</tr>		
	<!-- 附件 -->			
						<xsl:if test="//spl_attachment_1">
							<tr>
								<td class="wzb-form-label">
									<xsl:value-of select="$lab_spl_attachment"/>1：
								</td>
								<td class="wzb-form-control">
									<a class="Text" target="_blank" href="{//spl_attachment_1}"><xsl:value-of select="substring-after(substring-after(substring-after(//spl_attachment_1,'/'),'/'),'/')"/></a>							
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="//spl_attachment_2">	
							<tr>
								<td class="wzb-form-label">
									<xsl:value-of select="$lab_spl_attachment"/>2：
								</td>
								<td class="wzb-form-control">
									<a class="Text" target="_blank" href="{//spl_attachment_2}"><xsl:value-of select="substring-after(substring-after(substring-after(//spl_attachment_2,'/'),'/'),'/')"/></a>							
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="//spl_attachment_3">
							<tr>
								<td class="wzb-form-label" >
									<xsl:value-of select="$lab_spl_attachment"/>3：
								</td>
								<td class="wzb-form-control">
									<a class="Text" target="_blank" href="{//spl_attachment_3}"><xsl:value-of select="substring-after(substring-after(substring-after(//spl_attachment_3,'/'),'/'),'/')"/></a>							
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="//spl_attachment_4">
							<tr>
								<td class="wzb-form-label">
									<xsl:value-of select="$lab_spl_attachment"/>4：
								</td>
								<td class="wzb-form-control">
									<a class="Text" target="_blank" href="{//spl_attachment_4}"><xsl:value-of select="substring-after(substring-after(substring-after(//spl_attachment_4,'/'),'/'),'/')"/></a>							
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="//spl_attachment_5">						
							<tr>
								<td class="wzb-form-label">
									<xsl:value-of select="$lab_spl_attachment"/>5：
								</td>
								<td class="wzb-form-control">
									<a class="Text" target="_blank" href="{//spl_attachment_5}"><xsl:value-of select="substring-after(substring-after(substring-after(//spl_attachment_5,'/'),'/'),'/')"/></a>							
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="//spl_attachment_6">						
							<tr>
								<td class="wzb-form-label">
									<xsl:value-of select="$lab_spl_attachment"/>6：
								</td>
								<td class="wzb-form-control">
									<a class="Text" target="_blank" href="{//spl_attachment_6}"><xsl:value-of select="substring-after(substring-after(substring-after(//spl_attachment_6,'/'),'/'),'/')"/></a>							
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="//spl_attachment_7">						
							<tr>
								<td class="wzb-form-label">
									<xsl:value-of select="$lab_spl_attachment"/>7：
								</td>
								<td class="wzb-form-control">
									<a class="Text" target="_blank" href="{//spl_attachment_7}"><xsl:value-of select="substring-after(substring-after(substring-after(//spl_attachment_7,'/'),'/'),'/')"/></a>							
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="//spl_attachment_8">						
							<tr>
								<td class="wzb-form-label">
									<xsl:value-of select="$lab_spl_attachment"/>8：
								</td>
								<td class="wzb-form-control">
									<a class="Text" target="_blank" href="{//spl_attachment_8}"><xsl:value-of select="substring-after(substring-after(substring-after(//spl_attachment_8,'/'),'/'),'/')"/></a>							
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="//spl_attachment_9">						
							<tr>
								<td class="wzb-form-label">
									<xsl:value-of select="$lab_spl_attachment"/>9：
								</td>
								<td class="wzb-form-control">
									<a class="Text" target="_blank" href="{//spl_attachment_9}"><xsl:value-of select="substring-after(substring-after(substring-after(//spl_attachment_9,'/'),'/'),'/')"/></a>							
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="//spl_attachment_10">						
							<tr>
								<td class="wzb-form-label">
									<xsl:value-of select="$lab_spl_attachment"/>10：
								</td>
								<td class="wzb-form-control">
									<a class="Text" target="_blank" href="{//spl_attachment_10}"><xsl:value-of select="substring-after(substring-after(substring-after(//spl_attachment_10,'/'),'/'),'/')"/></a>							
								</td>
							</tr>
						</xsl:if>						
				</table>
				
					<!-- =============================================================== -->
	<!-- =============================================================== -->
		<xsl:if test="count(//supplier_cooperation_experience)>0">
		<div class="margin-top28"></div>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_spl_experience"/>
		</xsl:call-template>
		
		
		<xsl:call-template name="spl_project"/>
								
		</xsl:if>
	<!-- =============================================================== -->
		<xsl:if test="count(//supplier_main_course)>0">
		<div class="margin-top28"></div>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_spl_course"/>
		</xsl:call-template>
		
		<xsl:call-template name="spl_course"/>
			
		</xsl:if>	
	<!-- =============================================================== -->
		</form>
	</xsl:template>

	<!-- =============================================================== -->
	
	<xsl:template name="spl_project">
	<table class="table wzb-ui-table">
		<tr class="wzb-ui-table-head">
			<td align="center" width="20%">
				<xsl:value-of select="$lab_sce_startdate" />
				<input type="hidden" name="lab_sce_startdate" value="{$lab_spl_email}"></input>
			</td>
			<td align="center" width="20%">
				<xsl:value-of select="$lab_sce_enddate" />
				<input type="hidden" name="lab_sce_enddate" value="{$lab_sce_enddate}"></input>
			</td>
			<td align="center" width="20%">
					
				<xsl:value-of select="$lab_sce_itm_name" />
				<input type="hidden" name="lab_sce_itm_name" value="{$lab_sce_itm_name}"></input>
			</td>
			<td align="center" width="20%">
				<xsl:value-of select="$lab_sce_desc" />
				<input type="hidden" name="lab_sce_desc" value="{$lab_sce_desc}"></input>
			</td>
			<td align="center" width="20%">
				<xsl:value-of select="$lab_sce_dpt" />
				<input type="hidden" name="lab_sce_dpt" value="{$lab_sce_dpt}"></input>
			</td>
		</tr>
		<xsl:for-each select="//supplier_cooperation_experience">
			<tr>
				<td align="center">
					<xsl:value-of select="substring(sce_start_date,0,11)" />
				</td>
				<td align="center">
					<xsl:value-of select="substring(sce_end_date,0,11)" />
				</td>
				<td align="center">
					<xsl:value-of select="sce_itm_name" />
				</td>
				<td align="center">
					<xsl:value-of select="sce_desc" />
				</td>
				<td align="center">
					<xsl:value-of select="sce_dpt" />
				</td>
			</tr>
		</xsl:for-each>
	</table>
		</xsl:template>
		
		<!-- =============================================================== -->
		
		<xsl:template name="spl_course">
		<table class="table wzb-ui-table">
			<tr class="wzb-ui-table-head">
				<td  align="center" width="33%" valign="top" class="TitleText">
					<xsl:value-of select="$lab_smc_name"/>
					<input type="hidden" name="lab_smc_name" value="{$lab_smc_name}"></input>
				</td>
				<td  align="center" width="33%" valign="top" class="TitleText">
					<xsl:value-of select="$lab_smc_inst"/>
					<input type="hidden" name="lab_smc_inst" value="{$lab_smc_inst}"></input>
				</td>
				<td  align="center" width="34%" valign="top" class="TitleText">
					<xsl:value-of select="$lab_smc_price"/>
					<input type="hidden" name="lab_smc_price" value="{$lab_smc_price}"></input>
				</td>	
			</tr>
			<xsl:for-each select="//supplier_main_course">
			<tr>
					<td align="center"  valign="top">
						<xsl:value-of select="smc_name"/>
					</td>
					<td  align="center" valign="top">
						<xsl:value-of select="smc_inst"/>
					</td>
					<td align="center" valign="top">
						<xsl:value-of select="smc_price_show"/>
					</td>	
			</tr>
			</xsl:for-each>		
		</table>
		</xsl:template>	
		
		
</xsl:stylesheet>