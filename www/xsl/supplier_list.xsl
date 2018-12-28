<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
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
	<xsl:import href="suppliercomment_list.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- ============================================================= -->
	<xsl:variable name="cur_page" select="//pagination/@cur_page"/>
	<xsl:variable name="total" select="//pagination/@total_rec"/>
	<xsl:variable name="page_size" select="//pagination/@page_size"/>
	<xsl:variable name="order_by" select="//pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="//pagination/@sort_order"/>
	
	<xsl:variable name="sort_order">
		<xsl:choose>
			<xsl:when test="$cur_order = 'asc'">desc</xsl:when>
			<xsl:otherwise>asc</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_ftn_supplier_main" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '991')"/> 	
	<xsl:variable name="lab_supplier_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '994')"/> 	
	<xsl:variable name="lab_supplier_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '995')"/> 	
	<xsl:variable name="lab_link_url" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'sp1025')"/> 	
	<xsl:variable name="lab_supplier_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '996')"/> 	
	<xsl:variable name="lab_supplier_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '997')"/> 	
	<xsl:variable name="lab_supplier_course" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '998')"/> 	
	<xsl:variable name="lab_sce_itm_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1000')"/> 	
	<xsl:variable name="lab_scm_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1002')"/> 	
	<xsl:variable name="lab_supplier_address" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1003')"/> 	
	<xsl:variable name="lab_operate" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1004')"/> 	
	<xsl:variable name="lab_no_supplier" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1001')"/> 	
	<xsl:variable name="lab_no_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1021')"/> 	
	<xsl:variable name="lab_search_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '999')"/> 	
	<xsl:variable name="lab_view_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1005')"/> 	
	<xsl:variable name="lab_have_cooperation" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'a_have')"/> 	
	<xsl:variable name="lab_had_meeting" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'c_meet')"/> 	
	<xsl:variable name="lab_bid_pass" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'b_pass')"/> 	
	<xsl:variable name="lab_first_contact" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'd_contact')"/> 	
	<xsl:variable name="lab_exited" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'e_exited')"/> 	
	<xsl:variable name="lab_none" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'NONE')"/> 	
		
	<xsl:variable name="lab_link_update_user" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '705')"/> 	
	<xsl:variable name="lab_link_update_date" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '706')"/> 	
	<xsl:variable name="lab_update_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '708')"/> 	
	
	<xsl:variable name="lab_del_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '257')"/> 	
	<xsl:variable name="lab_download_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '692')"/> 	
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
				function searchSupplier(){
					var search_text=document.getElementById('search_text').value;
					url = wb_utils_invoke_disp_servlet("module","supplier.SupplierModule","cmd", 'get_supplier_list', 'stylesheet', 'supplier_list.xsl', 'search_text',search_text,'isSearch','1' );
					window.location.href = url;
				}
 			]]></script> 
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="content"/>
		</body>
	</xsl:template>
	
	<!-- =============================================================== -->
	<xsl:template name="content">
		<form name="frmXml" onsubmit="return false;">
			
			<input type="hidden" name="module"/>
			<input type="hidden" name="cmd"/>
			<input type="hidden" name="stylesheet"/>
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_SUPPLIER_MAIN</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text" select="$lab_ftn_supplier_main"/>
			</xsl:call-template>

			<!-- 搜索区域 -->
			<table style="margin-bottom: 28px;">
				<!--  
				<tr>	
					<td align="right" width="5%">
						<xsl:value-of select="$lab_supplier_status"/>:
					</td>
					<td width="5%" style="padding:10px 0 10px 10px;">
						<select name="splStatus" class="wzb-form-select" >
							
							<option value="" selected="selected" ><xsl:value-of select="$lab_no_status"></xsl:value-of></option>
							<xsl:for-each select="//status">
								<xsl:call-template name="supplier_sel_status">
											<xsl:with-param name="lab_name"><xsl:value-of select="name"/> </xsl:with-param>
											<xsl:with-param name="lab_value"><xsl:value-of select="value"/> </xsl:with-param>
											<xsl:with-param name="sel_name"><xsl:value-of select="@sel"/></xsl:with-param>
								</xsl:call-template>
							</xsl:for-each>
							
						</select>
					</td>
					<td align="right" width="10%">
						<xsl:value-of select="$lab_supplier_name"/>:
					</td>
					<td width="10%" style="padding:10px 0 10px 10px;">
						<input type="text" name="splName" class="wzb-inputText" value="{//select_params/@p_spl_name}"/>
					</td>
					<td align="right" width="25%">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_search_button"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:wbSupplier.search_supplier_list(document.frmXml)</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_download_button"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:wbSupplier.down_supplier_list()</xsl:with-param>
						</xsl:call-template>	
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_add_button"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:wbSupplier.get_supplier_prep()</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
				<tr>
					
					<td align="right" width="10%">
						<xsl:value-of select="$lab_supplier_course"/>:
					</td>
					<td width="10%" align="left" style="padding:10px 0 10px 10px;">
						<input type="text" name="splCourse" value="{//select_params/@p_spl_course}" class="wzb-inputText"/>
					</td>

					<td align="right" width="15%">
						<xsl:value-of select="$lab_sce_itm_name"/>:
					</td>
					<td width="10%" style="padding:10px 0 10px 10px;">
						<input type="text" name="sceItmName" value="{//select_params/@p_sce_itm_name}" class="wzb-inputText"/>
					</td>
					
				</tr>
				-->
				<tr>
					<td colspan="3" align="right">
					<from class="form-search">
							<input class="form-control" id="search_text" type="text" placeholder="{$lab_supplier_course}/{$lab_supplier_name}" value="{//search_text}"/>
							<input id="searchBtn" class="form-submit" type="button" onClick="searchSupplier();"/>
					</from>
					<!-- 
					<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-yellow margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_search_button"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:wbSupplier.search_supplier_list(document.frmXml)</xsl:with-param>
						</xsl:call-template>
						
						-->
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-yellow margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_download_button"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:wbSupplier.down_supplier_list()</xsl:with-param>
						</xsl:call-template>	
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-yellow margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_add_button"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:wbSupplier.get_supplier_prep()</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</table>
			<xsl:choose>
				<xsl:when test="count(suppliers/supplier) = 0">
					<xsl:call-template name="wb_ui_show_no_item">
						<xsl:with-param name="text" select="$lab_no_supplier"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
<!-- 				<xsl:call-template name="wb_ui_line"/> -->
					<table class="table wzb-ui-table">
						<tr class="wzb-ui-table-head">
							<td width="8%">
								<xsl:choose>
									<xsl:when test="$order_by = 'spl_status' ">
										<a href="javascript:wbSupplier.sort_supplier_list(document.frmXml,'spl_status','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_supplier_status"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>							
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wbSupplier.sort_supplier_list(document.frmXml,'spl_status','asc')" class="TitleText">
											<xsl:value-of select="$lab_supplier_status"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="22%">
								<xsl:choose>
									<xsl:when test="$order_by = 'spl_name' ">
										<a href="javascript:wbSupplier.sort_supplier_list(document.frmXml,'spl_name','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_supplier_name"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wbSupplier.sort_supplier_list(document.frmXml,'spl_name','asc')" class="TitleText">
											<xsl:value-of select="$lab_supplier_name"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							
							<td width="30%">
								<xsl:choose>
									<xsl:when test="$order_by = 'spl_course' ">
										<a href="javascript:wbSupplier.sort_supplier_list(document.frmXml,'spl_course','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_supplier_course"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wbSupplier.sort_supplier_list(document.frmXml,'spl_course','asc')" class="TitleText">
											<xsl:value-of select="$lab_supplier_course"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="10%">
								<xsl:choose>
									<xsl:when test="$order_by = 'scm_score' ">
										<a href="javascript:wbSupplier.sort_supplier_list(document.frmXml,'scm_score','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_scm_score"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wbSupplier.sort_supplier_list(document.frmXml,'scm_score','asc')" class="TitleText">
											<xsl:value-of select="$lab_scm_score"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="30%" align="right">
								<xsl:choose>
									<xsl:when test="$order_by = 'spl_address' ">
										<a href="javascript:wbSupplier.sort_supplier_list(document.frmXml,'spl_address','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_supplier_address"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wbSupplier.sort_supplier_list(document.frmXml,'spl_address','asc')" class="TitleText">
											<xsl:value-of select="$lab_supplier_address"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
			
						</tr>
						<xsl:for-each select="suppliers/supplier">
							<xsl:variable name="row_class">
								<xsl:choose>
									<xsl:when test="position() mod 2">RowsOdd</xsl:when>
									<xsl:otherwise>RowsEven</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<tr>
								<td>
									<xsl:variable name="supplier_statu" select="spl_status"></xsl:variable>
									<xsl:choose>
										<xsl:when test="$supplier_statu = 'a_have'"><xsl:value-of select="$lab_have_cooperation"/></xsl:when>
										<xsl:when test="$supplier_statu = 'b_pass'"><xsl:value-of select="$lab_bid_pass"/></xsl:when>
										<xsl:when test="$supplier_statu = 'c_meet'"><xsl:value-of select="$lab_had_meeting"/></xsl:when>
										<xsl:when test="$supplier_statu = 'd_contact'"><xsl:value-of select="$lab_first_contact"/></xsl:when>
										<xsl:when test="$supplier_statu = 'e_exited'"><xsl:value-of select="$lab_exited"/></xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$lab_none"/>
										</xsl:otherwise>										
									</xsl:choose>
								</td>
								<td>
									<a class="Text" href="javascript:wbSupplier.get_supplier_view({spl_id});" ><xsl:value-of select="spl_name"/></a>
								</td>	
								<td>
									<xsl:choose>
										<xsl:when test="string-length(spl_course)>30">
											<span class="Text" title="{spl_course}">
												<xsl:value-of select="substring(spl_course,0,30)"/>...
											</span>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="spl_course"/>
										</xsl:otherwise>
									</xsl:choose>
								</td>																	
								<td id="scm_score">
									<xsl:call-template name="supplier_star">
								       	<xsl:with-param name="score" select="scm_score"/>
							       	</xsl:call-template>
								</td>								
								<td align="right">
									<xsl:choose>
										<xsl:when test="string-length(spl_address)>20">
											<span class="Text"  title="{spl_address}">
												<xsl:value-of select="substring(spl_address,0,20)"/>...
											</span>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="spl_address"/>
										</xsl:otherwise>
									</xsl:choose>
								</td>
							</tr>
						</xsl:for-each>
					</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="total" select="$total"/>
					<xsl:with-param name="width" select="$wb_gen_table_width"/>
					<xsl:with-param name="cur_page_name">cur_page</xsl:with-param>
				</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</form>
	</xsl:template>
	<!-- =============================================================== -->
	
	<xsl:template name="supplier_sel_status" >
		<xsl:param name="lab_name"/>
		<xsl:param name="lab_value"/>
		<xsl:param name="sel_name"/>
		<xsl:if test="$sel_name = ''">
				<option value="{$lab_value}" ><xsl:value-of select="$lab_name"/></option>	
		</xsl:if>
		<xsl:if test="$sel_name = 'selected'">
			<option value="{$lab_value}" selected="selected"><xsl:value-of select="$lab_name"/></option>	
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>