<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="share/res_label_share.xsl"/>
	
	<!-- paginatoin variables -->
	<xsl:variable name="page_size" select="/search/item_list/pagination/@page_size"/>
	<xsl:variable name="cur_page" select="/search/item_list/pagination/@cur_page"/>
	<xsl:variable name="total" select="/search/item_list/pagination/@total_rec"/>
	<xsl:variable name="timestamp" select="/search/item_list/pagination/@timestamp"/>
	<xsl:variable name="search_title" select="/search/item_list/@search_title"/>
	
	<xsl:output indent="yes"/>
	<!-- ========================================================= -->
	<xsl:template match="/search">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- ========================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_search">搜尋結果</xsl:with-param>
			<xsl:with-param name="lab_search_result">顯示</xsl:with-param>
			<xsl:with-param name="lab_no_result">對不起，資料庫中並沒有你所搜索的關鍵字。</xsl:with-param>
			<xsl:with-param name="lab_no_desc">--</xsl:with-param>
			<xsl:with-param name="lab_of">共</xsl:with-param>
			<xsl:with-param name="lab_name">標題</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_desc">簡介</xsl:with-param>
			<xsl:with-param name="lab_res_code">編號</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_prev">上一頁</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next">下一頁</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_search">搜索结果</xsl:with-param>
			<xsl:with-param name="lab_search_result">显示 </xsl:with-param>
			<xsl:with-param name="lab_no_result">对不起，资料库中并没有您所检索的关键字。</xsl:with-param>
			<xsl:with-param name="lab_no_desc">--</xsl:with-param>
			<xsl:with-param name="lab_of">共</xsl:with-param>
			<xsl:with-param name="lab_name">标题</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_desc">简介</xsl:with-param>
			<xsl:with-param name="lab_res_code">编号</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_prev">上一页</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next">下一页</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_search">Search result</xsl:with-param>
			<xsl:with-param name="lab_search_result">Showing </xsl:with-param>
			<xsl:with-param name="lab_no_result">No results found</xsl:with-param>
			<xsl:with-param name="lab_no_desc">--</xsl:with-param>
			<xsl:with-param name="lab_of">of</xsl:with-param>
			<xsl:with-param name="lab_name">Title</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_res_code">ID</xsl:with-param>
			<xsl:with-param name="lab_desc">Description</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_prev">Previous</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next">Next</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ========================================================= -->
	<xsl:template name="content">
		<xsl:param name="lab_search"/>
		<xsl:param name="lab_search_result"/>
		<xsl:param name="lab_no_result"/>
		<xsl:param name="lab_no_desc"/>
		<xsl:param name="lab_of"/>
		<xsl:param name="lab_name"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_res_code" />
		<xsl:param name="lab_g_form_btn_prev"/>
		<xsl:param name="lab_g_form_btn_next"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_aicc.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_question.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_assessment.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_search.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_resource.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_cos_wizard.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_scenario.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[	

			aicc = new wbAicc
			cos = new wbCourse
			mod = new wbModule
			que = new wbQuestion
			asm = new wbAssessment
			seh = new wbSearch
			res = new wbResource	
			sc = new wbScControl
			wiz = new wbCosWizard
			
			window.onload = function(){
				init();
			}
			
			function init() {
				var search_result_url = window.location.href;
				wb_utils_set_cookie('search_result_url',search_result_url);
				gen_set_cookie('url_success',self.location.href);	
			}
			]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmSearch" onSubmit="seh.simple(document.frmSearch,'{$wb_lang}')">
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="search_title"/>
				<input type="hidden" name="search_items_per_page"/>
				<input type="hidden" name="stylesheet"/>
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
					<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
					<xsl:with-param name="page_title" select="$lab_search"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_search"/>
				</xsl:call-template>
				<table>
					<tr>
						<td align="right" width="95%">
							<div class="wzb-form-search">
								<input type="text" size="11" name="search" class="form-control" style="width:130px;" value="{$search_title}"/>
								<input type="button" class="form-submit margin-right4" value="" onclick="seh.simple(document.frmSearch, '{$wb_lang}')"/>
							</div>
							
						</td>
						<td align="right" width="5%">
							<a href="javascript:seh.adv_prep()" class="btn wzb-btn-blue vbtm">
								<xsl:value-of select="$lab_adv_search"/>
							</a>
						</td>
					</tr>
				</table>
				<xsl:choose>
					<xsl:when test="not(item_list/item)">
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_no_result"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<table class="table wzb-ui-table">
							<tr class="wzb-ui-table-head">
								<td width="30%">
									<xsl:value-of select="$lab_name"/>
								</td>
								<td width="20%">
									<xsl:value-of select="$lab_res_code"/>
								</td>
								<td width="25%">
									<xsl:value-of select="$lab_type"/>
								</td>
								<td width="25%">
									<xsl:value-of select="$lab_desc"/>
								</td>
							</tr>
							<xsl:for-each select="item_list/item">
								<tr>
									<xsl:choose>
										<xsl:when test="@sub_type = 'FIG'">
											<td>
												<xsl:text>&#160;</xsl:text>
												<a href="javascript:res.get_in_search({@id})" class="Text">
													<xsl:value-of select="title"/>
												</a>
											</td>
											<td>
												<a href="javascript:res.get_in_search({@id})" class="Text">
													<xsl:value-of select="@id"/>
												</a>
											</td>
											<td>
												<xsl:value-of select="$lab_fig"/>
											</td>
										</xsl:when>
										<xsl:when test="@sub_type = 'NAR'">
											<td>
												<xsl:text>&#160;</xsl:text>
												<a href="javascript:res.get_in_search({@id})" class="Text">
													<xsl:value-of select="title"/>
												</a>
											</td>
											<td>
												<a href="javascript:res.get_in_search({@id})" class="Text">
													<xsl:value-of select="@id"/>
												</a>
											</td>
											<td>
												<xsl:value-of select="$lab_nar"/>
											</td>
										</xsl:when>
										<xsl:when test="@sub_type = 'ADO'">
											<td>
												<xsl:text>&#160;</xsl:text>
												<a href="javascript:res.get_in_search({@id})" class="Text">
													<xsl:value-of select="title"/>
												</a>
											</td>
											<td>
												<a href="javascript:res.get_in_search({@id})" class="Text">
													<xsl:value-of select="@id"/>
												</a>
											</td>
											<td>
												<xsl:value-of select="$lab_ado"/>
											</td>
										</xsl:when>
										<xsl:when test="@sub_type = 'VDO'">
											<td>
												<xsl:text>&#160;</xsl:text>
												<a href="javascript:res.get_in_search({@id})" class="Text">
													<xsl:value-of select="title"/>
												</a>
											</td>
											<td>
												<a href="javascript:res.get_in_search({@id})" class="Text">
													<xsl:value-of select="@id"/>
												</a>
											</td>
											<td>
												<xsl:value-of select="$lab_vdo"/>
											</td>
										</xsl:when>
										<xsl:when test="@sub_type = 'WCT'">
											<td>
												<xsl:text>&#160;</xsl:text>
												<a href="javascript:res.get_in_search({@id})" class="Text">
													<xsl:value-of select="title"/>
												</a>
											</td>
											<td>
												<a href="javascript:res.get_in_search({@id})" class="Text">
													<xsl:value-of select="@id"/>
												</a>
											</td>
											<td>
												<xsl:value-of select="$lab_wct"/>
											</td>
										</xsl:when>
										<xsl:when test="@sub_type = 'MC'">
											<td>
												<a href="javascript:que.read({@id})" class="Text">
													<xsl:value-of select="title"/>
												</a>
											</td>
											<td>
												<a href="javascript:que.read({@id})" class="Text">
													<xsl:value-of select="@id"/>
												</a>
											</td>
											<td>
												<xsl:value-of select="$lab_mc"/>
											</td>
										</xsl:when>
										<xsl:when test="@sub_type = 'TF'">
											<td>
												<xsl:text>&#160;</xsl:text>
												<a href="javascript:que.read({@id})" class="Text">
													<xsl:value-of select="title"/>
												</a>
											</td>
											<td>
												<a href="javascript:que.read({@id})" class="Text">
													<xsl:value-of select="@id"/>
												</a>
											</td>
											<td>
												<xsl:value-of select="$lab_tf"/>
											</td>
										</xsl:when>
										<xsl:when test="@sub_type = 'MT'">
											<td>
												<xsl:text>&#160;</xsl:text>
												<a href="javascript:que.read({@id})" class="Text">
													<xsl:value-of select="title"/>
												</a>
											</td>
											<td>
												<a href="javascript:que.read({@id})" class="Text">
													<xsl:value-of select="@id"/>
												</a>
											</td>
											<td>
												<xsl:value-of select="$lab_mt"/>
											</td>
										</xsl:when>
										<xsl:when test="@sub_type = 'FB' ">
											<td>
												<xsl:text>&#160;</xsl:text>
												<a href="javascript:que.read({@id})" class="Text">
													<xsl:value-of select="title"/>
												</a>
											</td>
											<td>
												<a href="javascript:que.read({@id})" class="Text">
													<xsl:value-of select="@id"/>
												</a>
											</td>
											<td>
												<xsl:value-of select="$lab_fb"/>
											</td>
										</xsl:when>
										<xsl:when test="@sub_type = 'ES' ">
											<td>
												<xsl:text>&#160;</xsl:text>
												<a href="javascript:que.read({@id})" class="Text">
													<xsl:value-of select="title"/>
												</a>
											</td>
											<td>
												<a href="javascript:que.read({@id})" class="Text">
													<xsl:value-of select="@id"/>
												</a>
											</td>
											<td>
												<xsl:value-of select="$lab_es"/>
											</td>
										</xsl:when>								
										<xsl:when test="@sub_type = 'SSC' ">
											<td>
												<xsl:text>&#160;</xsl:text>
												<a href="javascript:res.get({@id})" class="Text">
													<xsl:value-of select="title"/>
												</a>
											</td>
											<td>
												<a href="javascript:res.get({@id})" class="Text">
													<xsl:value-of select="@id"/>
												</a>
											</td>
											<td>
												<xsl:value-of select="$lab_ssc"/>
											</td>
										</xsl:when>
										<xsl:when test="@sub_type = 'RES_SCO' ">
											<td>
												<xsl:text>&#160;</xsl:text>
												<a href="javascript:res.read({@id}, '{@sub_type}')" class="Text">
													<xsl:value-of select="title"/>
												</a>
											</td>
											<td>
												<a href="javascript:res.read({@id}, '{@sub_type}')" class="Text">
													<xsl:value-of select="@id"/>
												</a>
											</td>
											<td>
												<xsl:value-of select="$lab_sco"/>
											</td>
										</xsl:when>
										<xsl:when test="@sub_type = 'RES_NETG_COK' ">
											<td>
												<xsl:text>&#160;</xsl:text>
												<a href="javascript:res.read({@id}, '{@sub_type}')" class="Text">
													<xsl:value-of select="title"/>
												</a>
											</td>
											<td>
												<a href="javascript:res.read({@id}, '{@sub_type}')" class="Text">
													<xsl:value-of select="@id"/>
												</a>
											</td>
											<td>
												<xsl:value-of select="$lab_netg_cok"/>
											</td>
										</xsl:when>																				
										<xsl:when test="@sub_type = 'FAS' ">
											<td>
												<xsl:text>&#160;</xsl:text>
												<a href="javascript:asm.get({@id}, '{@sub_type}')" class="Text">
													<xsl:value-of select="title"/>
												</a>
											</td>
											<td>
												<a href="javascript:asm.get({@id}, '{@sub_type}')" class="Text">
													<xsl:value-of select="@id"/>
												</a>
											</td>
											<td>
												<xsl:value-of select="$lab_fas"/>
											</td>
										</xsl:when>
										<xsl:when test="@sub_type = 'DAS' ">
											<td>
												<xsl:text>&#160;</xsl:text>
												<a href="javascript:asm.get({@id}, '{@sub_type}')" class="Text">
													<xsl:value-of select="title"/>
												</a>
											</td>
											<td>
												<a href="javascript:asm.get({@id}, '{@sub_type}')" class="Text">
													<xsl:value-of select="@id"/>
												</a>
											</td>
											<td>
												<xsl:value-of select="$lab_das"/>
											</td>
										</xsl:when>
										<xsl:when test="@sub_type = 'FSC' ">
											<td>
												<xsl:text>&#160;</xsl:text>
												<a href="javascript:sc.read({@id}, '{@sub_type}')" class="Text">
													<xsl:value-of select="title"/>
												</a>
											</td>
											<td>
												<a href="javascript:sc.read({@id}, '{@sub_type}')" class="Text">
													<xsl:value-of select="@id"/>
												</a>
											</td>
											<td>
												<xsl:value-of select="$lab_fixed_sc"/>
											</td>
										</xsl:when>
										<xsl:when test="@sub_type = 'DSC' ">
											<td>
												<xsl:text>&#160;</xsl:text>
												<a href="javascript:sc.read({@id}, '{@sub_type}')" class="Text">
													<xsl:value-of select="title"/>
												</a>
											</td>
											<td>
												<a href="javascript:sc.read({@id}, '{@sub_type}')" class="Text">
													<xsl:value-of select="@id"/>
												</a>
											</td>
											<td>
												<xsl:value-of select="$lab_dna_sc"/>
											</td>
										</xsl:when>
									</xsl:choose>
									<xsl:choose>
										<xsl:when test="content and content != ''">
											<td>
												<xsl:call-template name="unescape_html_linefeed">
													<xsl:with-param name="my_right_value" select="content"/>
												</xsl:call-template>
											</td>
										</xsl:when>
										<xsl:otherwise>
											<td>
												<xsl:value-of select="$lab_no_desc"/>
											</td>
										</xsl:otherwise>
									</xsl:choose>
								</tr>
							</xsl:for-each>
						</table>
						
						
						<!-- Pagination -->
						<xsl:call-template name="wb_ui_pagination">
							<xsl:with-param name="cur_page" select="$cur_page"/>
							<xsl:with-param name="page_size" select="$page_size"/>
							<xsl:with-param name="timestamp" select="$timestamp"/>
							<xsl:with-param name="total" select="$total"/>
							<xsl:with-param name="width"><xsl:value-of select="$wb_gen_table_width"/></xsl:with-param>
						</xsl:call-template>
						
						
					</xsl:otherwise>
				</xsl:choose>
				<xsl:call-template name="wb_ui_footer"/>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>
