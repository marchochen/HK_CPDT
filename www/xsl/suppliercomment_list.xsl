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
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- ============================================================= -->
	<xsl:variable name="cur_page" select="/supplier_module/pagination/@cur_page"/>
	<xsl:variable name="total" select="/supplier_module/pagination/@total_rec"/>
	<xsl:variable name="page_size" select="/supplier_module/pagination/@page_size"/>
	<xsl:variable name="order_by" select="/supplier_module/pagination/@orderby"/>
	<xsl:variable name="cur_order" select="/supplier_module/pagination/@sortorder"/>
	<xsl:variable name="spl_id" select="/supplier_module/suppliers/@id"/>
	<xsl:variable name="ent_id" select="/supplier_module/meta/cur_usr/@ent_id"/>
	<xsl:variable name="spl_name" select="/supplier_module/suppliers/nav/supplier/@name"/>
	<xsl:variable name="sort_order">
		<xsl:choose>
			<xsl:when test="$cur_order = 'asc'">desc</xsl:when>
			<xsl:otherwise>asc</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =========================== Label =========================== 
	980	评论人
	981	项目设计
	982	现场管理
	983	师资设备
	984	价格
	985	评语
	986	评论人
	987	供应商
	988	评论信息
	989  添加评论
	977 评论时间 
	991 供应商管理-->
	<xsl:variable name="lab_comment" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '980')"/> 
	<xsl:variable name="lab_scm_design_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '981')"/> 	
	<xsl:variable name="lab_scm_management_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '982')"/> 	
	<xsl:variable name="lab_scm_teaching_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '983')"/> 	
	<xsl:variable name="lab_scm_price_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '984')"/> 	
	<xsl:variable name="lab_scm_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '928')"/> 	
	<xsl:variable name="lab_scm_comment" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '985')"/> 	
	<xsl:variable name="lab_supplier" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '987')"/> 	
	<xsl:variable name="lab_supplier_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '988')"/> 	
	<xsl:variable name="lab_no_result" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '974')"/>
	<xsl:variable name="lab_add" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '989')"/>  
	<xsl:variable name="lab_comment_datetime" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '977')"/>  
	<xsl:variable name="lab_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '991')"/>  
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
	<xsl:template name="content">
		<form name="frmXml">
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_SUPPLIER_MAIN</xsl:with-param>
			</xsl:call-template>
			
			<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_supplier_desc"/>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_nav_link">
				<xsl:with-param name="text">
					<a href="javascript:wb_utils_nav_go('FTN_AMD_SUPPLIER_MAIN');" class="NavLink">
							<xsl:value-of select="$lab_title"/>
					</a>
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<a href="javascript:wbSupplier.get_supplier_view({$spl_id});" class="NavLink">
							<xsl:value-of select="$spl_name"/>
					</a>
					<span class="NavLink">
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:value-of select="$lab_supplier_desc"/>
					</span>
				</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="extra_td">
					<td align="right">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_add"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:wbSupplier.comm.supplier_comment_add('<xsl:value-of select="$spl_id"/>','<xsl:value-of select="$ent_id"/>')</xsl:with-param>
						</xsl:call-template>	
					</td>
				</xsl:with-param>
			</xsl:call-template>
			<xsl:choose>
				<xsl:when test="count(suppliers/supplier_comment) = 0">
					<xsl:call-template name="wb_ui_show_no_item">
						<xsl:with-param name="text" select="$lab_no_result"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
<!-- 				<xsl:call-template name="wb_ui_line"/> -->
					<table class="table wzb-ui-table">
						<tr class="wzb-ui-table-head">
							
							<td  width="10%">
								<xsl:value-of select="$lab_comment"/>	
							</td>
							<td  width="16%" align="center">
								<xsl:value-of select="$lab_comment_datetime"/>	
							</td>
							<td width="11%">
								<xsl:value-of select="$lab_scm_design_score"/>	
							</td>
							<td width="11%">
								<xsl:value-of select="$lab_scm_management_score"/>	
							</td>
							<td width="11%">
								<xsl:value-of select="$lab_scm_teaching_score"/>	
							</td>
							<td width="11%">
								<xsl:value-of select="$lab_scm_price_score"/>	
							</td>
							<td width="11%">
								<xsl:value-of select="$lab_scm_score"/>	
							</td>
							<td  width="19%" align="right">
								<xsl:value-of  select="$lab_scm_comment"/>	
							</td>
						</tr>	
							
					
						<xsl:for-each select="suppliers/supplier_comment">
							<tr>
								<td>
									<xsl:value-of select="usr_display_bil"/>
								</td>	
								  <td align="center">
								       <xsl:call-template name="supplier_isempty">
								       		<xsl:with-param name="lab_value" ><xsl:value-of select="substring(scm_update_datetime,1,16)"/></xsl:with-param>
								       </xsl:call-template>
								</td>	
								<td id="scm_design_score">
							       <xsl:call-template name="supplier_star">
								       	<xsl:with-param name="score" select="scm_design_score"/>
							       </xsl:call-template>
								</td>	
								<td id="scm_management_score">
							       <xsl:call-template name="supplier_star">
								       	<xsl:with-param name="score" select="scm_management_score"/>
							       </xsl:call-template>
								</td>																	
								<td id="scm_teaching_score">
							       <xsl:call-template name="supplier_star">
								       	<xsl:with-param name="score" select="scm_teaching_score"/>
							       </xsl:call-template>
								</td>								
								<td id="scm_price_score">
							       <xsl:call-template name="supplier_star">
								       	<xsl:with-param name="score" select="scm_price_score"/>
							       </xsl:call-template>
								</td>
								<td id="scm_score">
							       <xsl:call-template name="supplier_star">
								       	<xsl:with-param name="score" select="scm_score"/>
							       </xsl:call-template>
								</td>
								<td title="{scm_comment}"  align="right">
								   <xsl:call-template name="unescape_html_linefeed">
									   <xsl:with-param name="my_right_value">
									   		<xsl:choose>
									   			<xsl:when test="string-length(scm_comment)>25">
													<xsl:value-of select="substring(scm_comment,0,24)"/>...
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="scm_comment"/>
												</xsl:otherwise>
									   		</xsl:choose>
									   </xsl:with-param>
								  </xsl:call-template>
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
	
	
	<xsl:template name="supplier_isempty" >
		<xsl:param name="lab_type">str</xsl:param>
		<xsl:param name="lab_value"/>
		
		<xsl:choose>
			<xsl:when test="$lab_type ='score'">
					<xsl:choose>
						<xsl:when test="$lab_value >0"><xsl:value-of select="$lab_value"/></xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="$lab_value !=''"><xsl:value-of select="$lab_value"/></xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="supplier_star" >
		<xsl:param name="score"/>
		<xsl:choose>
			<xsl:when test="$score > 0">
				<xsl:for-each select="$score">
					<xsl:if test="$score > 0 and $score &lt; 1 "><li title="{$score}" class="xing1 active"></li></xsl:if>
					<xsl:if test="$score >= 1"><li title="{$score}" class="xing1"></li></xsl:if>
					<xsl:if test="$score > 1 and $score &lt; 2 "><li title="{$score}" class="xing1 active"></li></xsl:if>
					<xsl:if test="$score >= 2"><li title="{$score}" class="xing1"></li></xsl:if>
					<xsl:if test="$score > 2 and $score &lt; 3 "><li title="{$score}" class="xing1 active"></li></xsl:if>
					<xsl:if test="$score >= 3"><li title="{$score}" class="xing1"></li></xsl:if>
					<xsl:if test="$score > 3 and $score &lt; 4 "><li title="{$score}" class="xing1 active"></li></xsl:if>
					<xsl:if test="$score >= 4"><li title="{$score}" class="xing1"></li></xsl:if>
					<xsl:if test="$score > 4 and $score &lt; 5 "><li title="{$score}" class="xing1 active"></li></xsl:if>
					<xsl:if test="$score = 5"><li title="{$score}" class="xing1"></li></xsl:if>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>--</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
</xsl:stylesheet>