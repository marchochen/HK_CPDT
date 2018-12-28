<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:strip-space elements="*"/>
	<!-- Pagination -->
	<xsl:template name="wb_ui_pagination">
		<xsl:param name="cur_page"/>
		<xsl:param name="page_size"/>
		<xsl:param name="timestamp"/>
		<xsl:param name="total"/>
		<xsl:param name="width" select="$wb_gen_table_width"/>
		<xsl:param name="tr_class">SecBg</xsl:param>
		<xsl:param name="txt_class">Text</xsl:param>
		<xsl:param name="link_txt_class">Text</xsl:param>
		<xsl:param name="select_class">Select</xsl:param>
		<xsl:param name="cur_page_name">cur_page</xsl:param>
		<xsl:param name="page_size_name">page_size</xsl:param>
		<xsl:param name="layout">default</xsl:param>
		<xsl:param name="wzb-page-style" />
		<!-- default | short , Layout of pagination table -->
		<!-- navigation name -->
		<!-- =============================================================== -->
		<xsl:variable name="no_of_page" select="ceiling($total div $page_size)"/>
		<xsl:variable name="index" select="floor(($cur_page - 1) div 5)"/>
		<xsl:variable name="page_timestamp" select="$timestamp"/>
		<!-- =============================================================== -->
		
		<xsl:variable name="totalPageLabel">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">共<xsl:value-of select="$total"></xsl:value-of>條記錄</xsl:when>
				<xsl:when test="$wb_lang ='gb'">共<xsl:value-of select="$total"></xsl:value-of>条记录</xsl:when>
				<xsl:otherwise>Total <xsl:value-of select="$total"></xsl:value-of> results</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<!-- Showing Variable -->
		<xsl:variable name="_showing">
			<xsl:choose>
				<xsl:when test="$total = 0 ">
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</xsl:when>
				<xsl:otherwise>
						<xsl:value-of select="$lab_showing"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:choose>
							<xsl:when test="$cur_page = 1">1</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="($cur_page - 1)*$page_size + 1"/>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:text>&#160;-&#160;</xsl:text>
						<xsl:choose>
							<xsl:when test="$total &lt; ($cur_page * $page_size)">
								<xsl:value-of select="$total"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$cur_page * $page_size"/>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$lab_page_of"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$total"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$lab_page_piece"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- =============================================================== -->
		<xsl:variable name="_prev">
			<xsl:choose>
				<xsl:when test="$cur_page &gt; 1">
					<xsl:value-of select="($cur_page - 1)" />
				</xsl:when>
				<xsl:otherwise>1</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="_next">
			<xsl:choose>
				<xsl:when test="$cur_page &lt; $no_of_page">
					<xsl:value-of select="($cur_page + 1)" />
				</xsl:when>
				<xsl:otherwise><xsl:value-of select="$no_of_page"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- =============================================================== -->
		<!-- Previous Link Variable-->
		<!--<xsl:variable name="_previous">
			<xsl:if test="($no_of_page &gt;= 6) and ($cur_page &gt;5)">
				<xsl:choose>
					<xsl:when test="$page_timestamp != ''">
						<a href="Javascript:wb_utils_nav_get_urlparam('timestamp','{$page_timestamp}','{$cur_page_name}','{$index * 5}')" class="{$link_txt_class}">
							<xsl:value-of select="$lab_prev"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a href="Javascript:wb_utils_nav_get_urlparam('{$cur_page_name}','{$index * 5}')" class="{$link_txt_class}">
							<xsl:value-of select="$lab_prev"/>
						</a>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</xsl:variable>-->
		<!-- =============================================================== -->
		<!-- Next Link Variable -->
		<!--<xsl:variable name="_next">
			<xsl:if test="$no_of_page &gt; (($index + 1) * 5)">
				<xsl:variable name="ind" select="(($index + 1) * 5) + 1"/>
				<xsl:choose>
					<xsl:when test="$page_timestamp != ''">
						<a href="Javascript:wb_utils_nav_get_urlparam('timestamp','{$page_timestamp}','{$cur_page_name}','{$ind}')" class="{$link_txt_class}">
							<xsl:value-of select="$lab_next"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a href="Javascript:wb_utils_nav_get_urlparam('{$cur_page_name}','{$ind}')" class="{$link_txt_class}">
							<xsl:value-of select="$lab_next"/>
						</a>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</xsl:variable>-->
		<!-- =============================================================== -->
		<!-- Page Count Link Variable-->
		<!--
		<xsl:variable name="_pagelink">
			<xsl:if test="$no_of_page != 1">
				<xsl:if test="$no_of_page != 0">
					<span class="{$txt_class}">&#160;|&#160;</span>
				</xsl:if>
				<xsl:call-template name="loop_page_link">
					<xsl:with-param name="start_ind">1</xsl:with-param>
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="no_of_page" select="$no_of_page"/>
					<xsl:with-param name="total" select="$total"/>
					<xsl:with-param name="txt_class" select="$txt_class"/>
					<xsl:with-param name="cur_page_name" select="$cur_page_name"/>
					<xsl:with-param name="index" select="$index"/>
					<xsl:with-param name="page_timestamp" select="$page_timestamp"/>
					<xsl:with-param name="link_txt_class" select="$link_txt_class"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:variable>-->
		<!-- =============================================================== -->
		<!-- =============================================================== -->
		<!-- Start Table Layout -->
		<xsl:choose>
			<xsl:when test="$layout = 'short'">
				<!--<table cellpadding="3" cellspacing="0" border="0" width="{$width}">
					<tr class="{$tr_class}">
						<td>
							<xsl:copy-of select="$_showing"/>
						</td>
					</tr>
					<tr class="{$tr_class}">
						<td align="right">
							<xsl:copy-of select="$_selectbox"/>
							<span class="{$txt_class}">&#160;<xsl:value-of select="$lab_const_itm_per_page"/></span>
						</td>
					</tr>
					<tr class="{$tr_class}">
						<td align="right">
							<xsl:copy-of select="$_previous"/>
							<xsl:copy-of select="$_pagelink"/>
							<xsl:copy-of select="$_next"/>
						</td>
					</tr>
				</table>-->
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$no_of_page &gt; 1">
						<table id="wzb-ui-table-pager">
							<tr>
								<td colspan="4" style="border-bottom:none">
									<div style="{$wzb-page-style};border-top: 1px solid #EEE;verflow: hidden;text-align: center;padding: 5px 0;">
										<xsl:if test="$cur_page &gt;1">
											<a href="Javascript:wb_utils_nav_get_urlparam('timestamp','{$page_timestamp}','{$cur_page_name}','1')" class="wzb-page-btn wzb-page-first"></a>
											<a href="Javascript:wb_utils_nav_get_urlparam('timestamp','{$page_timestamp}','{$cur_page_name}','{$_prev}')" class="wzb-page-btn wzb-page-prev"></a>
										</xsl:if>
									
										
										<xsl:call-template name="loop_page_link_1">
											<xsl:with-param name="start_ind">1</xsl:with-param>
											<xsl:with-param name="cur_page" select="$cur_page"/>
											<xsl:with-param name="no_of_page" select="$no_of_page"/>
											<xsl:with-param name="total" select="$total"/>
											<xsl:with-param name="txt_class" select="$txt_class"/>
											<xsl:with-param name="cur_page_name" select="$cur_page_name"/>
											<xsl:with-param name="index" select="$index"/>
											<xsl:with-param name="page_timestamp" select="$page_timestamp"/>
											<xsl:with-param name="link_txt_class" select="$link_txt_class"/>
										</xsl:call-template>
										
										<xsl:if test="$cur_page &lt; $no_of_page">
											<a href="Javascript:wb_utils_nav_get_urlparam('timestamp','{$page_timestamp}','{$cur_page_name}','{$_next}')" class="wzb-page-btn wzb-page-next"></a>
											<a href="Javascript:wb_utils_nav_get_urlparam('timestamp','{$page_timestamp}','{$cur_page_name}','{$no_of_page}')" class="wzb-page-btn wzb-page-last"></a>
										</xsl:if>
										
										<p style='padding: 4px;display: inline-block;margin: 0;'><xsl:value-of select="$totalPageLabel"></xsl:value-of></p>
									</div>
								</td>
							</tr>
						</table>
					</xsl:when>
					<xsl:otherwise>
						<table id="wzb-ui-table-pager">
							<tr>
								<td colspan="4">
									<div style="{$wzb-page-style};border-top: 1px solid #EEE;verflow: hidden;text-align: center;padding: 5px 0;">
										<p style='padding: 4px;display: inline-block;margin: 0;'><xsl:value-of select="$totalPageLabel"></xsl:value-of></p>
									</div>
								</td>
							</tr>
						</table>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
		<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			$(function(){
				//由于XSL中数据表格是在页面中手动写的，现在想做到表格最后一行的border去掉，为了避免每个XSL都要写，所以加了这段脚本
				var $_dataTable = $("#wzb-ui-table-pager").prev(); 
				$_dataTable.find("tr:last").children().css("border-bottom","none");
				$_dataTable.addClass("margin-top28");
			});
 		]]></script>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="loop_page_link">
		<xsl:param name="start_ind"/>
		<xsl:param name="index"/>
		<xsl:param name="no_of_page"/>
		<xsl:param name="cur_page"/>
		<xsl:param name="cur_page_name"/>
		<xsl:param name="page_timestamp"/>
		<xsl:param name="txt_class"/>
		<xsl:param name="link_txt_class"/>
		<xsl:param name="total"/>
		<xsl:variable name="temp_ind" select="(($index * 5) + $start_ind)"/>
		<xsl:if test="$temp_ind &lt;= $no_of_page">
			<xsl:choose>
				<xsl:when test="$cur_page = $temp_ind">
					<xsl:value-of select="$temp_ind"/>&#160;|&#160;
				</xsl:when>
				<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="$page_timestamp != ''">
							<a href="Javascript:wb_utils_nav_get_urlparam('timestamp','{$page_timestamp}','{$cur_page_name}','{$temp_ind}')" class="{$link_txt_class}">
								<xsl:value-of select="$temp_ind"/>
							</a>
						</xsl:when>
						<xsl:otherwise>
							<a href="Javascript:wb_utils_nav_get_urlparam('{$cur_page_name}','{$temp_ind}')" class="{$link_txt_class}">
								<xsl:value-of select="$temp_ind"/>
							</a>
						</xsl:otherwise>
					</xsl:choose>
					&#160;|<xsl:if test="$temp_ind != $no_of_page">&#160;</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
		<xsl:if test="$start_ind &lt; 5">
			<xsl:call-template name="loop_page_link">
				<xsl:with-param name="start_ind">
					<xsl:value-of select="$start_ind + 1"/>
				</xsl:with-param>
				<xsl:with-param name="cur_page" select="$cur_page"/>
				<xsl:with-param name="no_of_page" select="$no_of_page"/>
				<xsl:with-param name="total" select="$total"/>
				<xsl:with-param name="txt_class" select="$txt_class"/>
				<xsl:with-param name="cur_page_name" select="$cur_page_name"/>
				<xsl:with-param name="index" select="$index"/>
				<xsl:with-param name="page_timestamp" select="$page_timestamp"/>
				<xsl:with-param name="link_txt_class" select="$link_txt_class"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<xsl:template name="loop_page_link_1">
		<xsl:param name="start_ind"/>
		<xsl:param name="index"/>
		<xsl:param name="no_of_page"/>
		<xsl:param name="cur_page"/>
		<xsl:param name="cur_page_name"/>
		<xsl:param name="page_timestamp"/>
		<xsl:param name="txt_class"/>
		<xsl:param name="link_txt_class"/>
		<xsl:param name="total"/>
		<xsl:variable name="temp_ind" select="(($index * 5) + $start_ind)"/>
		<xsl:if test="$temp_ind &lt;= $no_of_page">
			<xsl:choose>
				<xsl:when test="$cur_page = $temp_ind">
					<span class="wzb-page-btn active">
						<xsl:value-of select="$temp_ind"/></span>
				</xsl:when>
				<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="$page_timestamp != ''">
							<a href="Javascript:wb_utils_nav_get_urlparam('timestamp','{$page_timestamp}','{$cur_page_name}','{$temp_ind}')" class="wzb-page-btn">
								<xsl:value-of select="$temp_ind"/>
							</a>
						</xsl:when>
						<xsl:otherwise>
							<a href="Javascript:wb_utils_nav_get_urlparam('{$cur_page_name}','{$temp_ind}')" class="wzb-page-btn">
								<xsl:value-of select="$temp_ind"/>
							</a>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
		<xsl:if test="$start_ind &lt; 5">
			<xsl:call-template name="loop_page_link_1">
				<xsl:with-param name="start_ind">
					<xsl:value-of select="$start_ind + 1"/>
				</xsl:with-param>
				<xsl:with-param name="cur_page" select="$cur_page"/>
				<xsl:with-param name="no_of_page" select="$no_of_page"/>
				<xsl:with-param name="total" select="$total"/>
				<xsl:with-param name="txt_class" select="$txt_class"/>
				<xsl:with-param name="cur_page_name" select="$cur_page_name"/>
				<xsl:with-param name="index" select="$index"/>
				<xsl:with-param name="page_timestamp" select="$page_timestamp"/>
				<xsl:with-param name="link_txt_class" select="$link_txt_class"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
