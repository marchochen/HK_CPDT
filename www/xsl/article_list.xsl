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
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="share/div_tree_share.xsl"/>
	<xsl:import href="share/gen_tree_js.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	
	
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- ============================================================= -->
	<xsl:variable name="cur_page" select="//pagination/@cur_page"/>
	<xsl:variable name="total" select="//pagination/@total_rec"/>
	<xsl:variable name="page_size" select="//pagination/@page_size"/>
	<xsl:variable name="order_by" select="//pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="//pagination/@sort_order"/>
	
	<xsl:variable name="cnt_type" select="count(//aty_list/aty)"/>	
	<xsl:variable name="cnt" select="count(//articles/article)"/>
	
	<xsl:variable name="tc_enabled" select="//meta/tc_enabled"/>
	<xsl:variable name="art_type" select="/article_module/art_type"/>
	<xsl:variable name="art_keywords" select="/article_module/art_keywords"/>
	<xsl:variable name="page_variant" select="//meta/page_variant"/>
	<xsl:variable name="cur_tcr_id" select="/article_module/default_training_center/@id"/>
	
	<xsl:variable name="sort_order">
		<xsl:choose>
			<xsl:when test="$cur_order = 'asc'">desc</xsl:when>
			<xsl:otherwise>asc</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_art_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '79')"/> 	
	<xsl:variable name="lab_art_content" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_content')"/> 	
	<xsl:variable name="lab_art_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_status')"/> 	
	<xsl:variable name="lab_tc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_tc')"/> 	
	<xsl:variable name="lab_def_tc_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1130')"/> 	
	<xsl:variable name="lab_art_create_datetime" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1131')"/> 	
	<xsl:variable name="lab_art_author" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1132')"/> 	
	<xsl:variable name="lab_select_on" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '765')"/> 	
	<xsl:variable name="lab_select_off" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '766')"/> 	
	<xsl:variable name="lab_art_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1135')"/> 	
	<xsl:variable name="lab_root_training_center" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1134')"/> 	
	<xsl:variable name="lab_cur_tcr" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1133')"/> 	
	<xsl:variable name="lab_aty" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '265')"/> 
	<xsl:variable name="lab_all" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_all')"/> 	
	
	<xsl:variable name="lab_ftn_article_main" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding,'lab_ftn_ARTICLE_MAIN')"/> 	
	<xsl:variable name="lab_article_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '995')"/> 	
	<xsl:variable name="lab_link_url" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'sp1025')"/> 	
	<xsl:variable name="lab_article_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '996')"/> 	
	<xsl:variable name="lab_no_article" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_table_empty')"/> 	
	<xsl:variable name="lab_article_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '997')"/> 	
	<xsl:variable name="lab_link_update_user" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '705')"/> 	
	<xsl:variable name="lab_link_update_date" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '706')"/> 	
	<xsl:variable name="lab_update_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '708')"/> 	
	<xsl:variable name="lab_search_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '400')"/>
	<xsl:variable name="lab_del_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '257')"/> 	
	<xsl:variable name="lab_download_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '692')"/> 	
	<xsl:variable name="lab_add_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '473')"/> 	
	<xsl:variable name="lab_article_status_on" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '765')"/> 	
	<xsl:variable name="lab_article_status_off" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '766')"/> 	
	<xsl:variable name="lab_article_type_maintain" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lan7')"/> 	
	<xsl:variable name="lab_article_type" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_type')"/>
	<!-- =============================================================== -->
	<xsl:template match="/article_module">
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
			<!-- <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> -->
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_article.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_im_{$wb_cur_lang}.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				wbarticle = new wbArticle; 
				wbEncrytor = new wbEncrytor;
				
				var art_tcr_id = getUrlParam('art_tcr_id');
				var art_type = getUrlParam('art_type') ? getUrlParam('art_type') : '0';
				
				function load_tree() {
					if (frmXml.tc_enabled_ind) {
						page_onload(250);
					}
				}
				function show_content(tcr_id) {
					wbarticle.search_article_list(tcr_id,art_type,'');
				}
				
				function changeAtyType(art_type,is_mobile) {
					url = wb_utils_invoke_disp_servlet("module","article.ArticleModule","cmd", 'get_article_list', 'stylesheet', 'article_list.xsl', 'art_tcr_id',art_tcr_id,'art_type',art_type,'art_push_mobile',is_mobile);
					window.location.href = url;
				}
				
				
				
 			]]></script> 
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="load_tree()">
			<xsl:call-template name="content"/>
		</body>
	</xsl:template>
	
	<!-- =============================================================== -->
	<xsl:template name="content">
		<form name="frmXml" onSubmit="return false;" >
			<input type="hidden" name="module"/>
			<input type="hidden" name="cmd"/>
			<input type="hidden" name="art_push_mobile"/>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<input type="hidden" name="art_id_str"/>
			<input type="hidden" name="stylesheet"/>
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_ARTICLE_MAIN</xsl:with-param>
				<xsl:with-param name="page_title">
				<xsl:call-template name="get_lab">
					<xsl:with-param name="lab_title">global.FTN_AMD_ARTICLE_MAIN</xsl:with-param>
				</xsl:call-template>
			</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text" select="$lab_ftn_article_main"/>
			</xsl:call-template>
			<!-- <xsl:call-template name="wb_ui_desc">
				<xsl:with-param name="text" select="$lab_art_desc"/>
			</xsl:call-template> -->

			<!-- tree nav -->
			<table>
				<tr>
					<td width="32%">
						<table>
							<tr>
								<td>
									<xsl:if test="$tc_enabled='true'">
										<xsl:variable name="title_var">
											<xsl:choose>
												<xsl:when test="not(//default_training_center)">
													<xsl:value-of select="$lab_root_training_center" />
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="//default_training_center/title" />
												</xsl:otherwise>
											</xsl:choose>
										</xsl:variable>
										<xsl:call-template name="div_tree">
											<xsl:with-param name="lab_cur_tcr" select="$lab_cur_tcr" />
											<xsl:with-param name="width" select="auto" />
											<xsl:with-param name="lab_root_training_center" select="$lab_root_training_center" />
											<xsl:with-param name="title" select="$title_var" />
										</xsl:call-template>
										<input type="hidden" name="tc_enabled_ind" />
									</xsl:if>
								</td>
								
							</tr>
						</table>
					</td>
					<td align="right" width="68%">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<!-- 
								<xsl:with-param name="style">padding-left:0px</xsl:with-param>
							 -->
						
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_article_type_maintain"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:wbarticle.get_article_type_maintain()</xsl:with-param>
						</xsl:call-template>
						<xsl:if test="/article_module/TypeExist/text()='true'">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$lab_add_button"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:wbarticle.get_article_prep('', '<xsl:value-of select="$cur_tcr_id" />')</xsl:with-param>
							</xsl:call-template>
						</xsl:if>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_del_button"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:wbarticle.article_del_id(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						</xsl:call-template>	
						
					</td>
				</tr>
				<tr>
				<td width="32%" style="padding:10px 0 0 0;"></td>
				  
				  <td align="right" width="68%" style="padding:10px 0 0 0;">
				  	<xsl:if test="$cnt_type > 0">
							<xsl:value-of select="$lab_aty" />：<xsl:variable name="id" select="//aty_list/aty/@id" />
							<select name="dummy_type" style="width:220px;" size="1" class="wzb-form-select" onchange="changeAtyType(this.value,'')">
								<option>
									<xsl:attribute name="value">0</xsl:attribute>
									<xsl:if test="$art_type =''">
										<xsl:attribute name="selected">selected</xsl:attribute>
									</xsl:if>
									--
									<xsl:value-of select="$lab_all" />
									--
								</option>
								<xsl:for-each select="//aty_list/aty">
									<option>
										<xsl:attribute name="value"><xsl:value-of select="@id" /></xsl:attribute>
										<xsl:if test="$art_type = @id">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="@title" />
									</option>
								</xsl:for-each>
							</select>
						</xsl:if>
					  <div class="wzb-form-search " style="width: 150px;display: inline-block;">
							<input type="text" size="11" name="art_keywords" class="form-control" style="width:110px;height:27px;margin-top:-2px;">
								<xsl:if test="$art_keywords !=''">
									<xsl:attribute name="value"><xsl:value-of select="$art_keywords"></xsl:value-of></xsl:attribute>
								</xsl:if>
							</input>
							 <!-- onkeypress="startSearch()" -->
							<input type="button" class="form-submit" value="" style="height:27px;margin-top:-2px;" onclick="wbarticle.search_key_article_list(document.frmXml,'',art_tcr_id,art_type)"/>
						</div>
				   </td>
				</tr>
			</table>
			<xsl:choose>
				<xsl:when test="count(articles/article) = 0">
					<xsl:call-template name="wb_ui_show_no_item">
						<xsl:with-param name="text" select="$lab_no_article"/>
					</xsl:call-template>
				</xsl:when>
			<xsl:otherwise>
					

			<table class="table wzb-ui-table">
						<tr class="wzb-ui-table-head">
							<td width="25%">
								<xsl:call-template name="select_all_checkbox">
									<xsl:with-param name="chkbox_lst_cnt"><xsl:value-of select="$cnt"/></xsl:with-param>
									<xsl:with-param name="display_icon">false</xsl:with-param>
								</xsl:call-template>
								<xsl:choose>
									<xsl:when test="$order_by = 'art_title' ">
										<a href="javascript:wbarticle.sort_article_list(document.frmXml,'art_title','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_art_title"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>							
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wbarticle.sort_article_list(document.frmXml,'art_title','asc')" class="TitleText">
											<xsl:value-of select="$lab_art_title"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="15%">
								<xsl:choose>
									<xsl:when test="$order_by = 'art_user_id' ">
										<a href="javascript:wbarticle.sort_article_list(document.frmXml,'art_user_id','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_art_author"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>							
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wbarticle.sort_article_list(document.frmXml,'art_user_id','asc')" class="TitleText">
											<xsl:value-of select="$lab_art_author"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="15%">
								<xsl:choose>
									<xsl:when test="$order_by = 'art_status' ">
										<a href="javascript:wbarticle.sort_article_list(document.frmXml,'art_status','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_art_status"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>							
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wbarticle.sort_article_list(document.frmXml,'art_status','asc')" class="TitleText">
											<xsl:value-of select="$lab_art_status"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="20%">
								<xsl:choose>
									<xsl:when test="$order_by = 'art_type' ">
										<a href="javascript:wbarticle.sort_article_list(document.frmXml,'art_type','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_article_type"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>							
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wbarticle.sort_article_list(document.frmXml,'art_type','asc')" class="TitleText">
											<xsl:value-of select="$lab_article_type"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="20%">
								<xsl:choose>
									<xsl:when test="$order_by = 'art_create_datetime' ">
										<a href="javascript:wbarticle.sort_article_list(document.frmXml,'art_create_datetime','{$sort_order}')" class="TitleText">
											<xsl:value-of select="$lab_art_create_datetime"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$cur_order"/>							
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wbarticle.sort_article_list(document.frmXml,'art_create_datetime','asc')" class="TitleText">
											<xsl:value-of select="$lab_art_create_datetime"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td width="5%" align="right">
							</td>
						</tr>
						<xsl:for-each select="articles/article">
							<xsl:variable name="row_class">
								<xsl:choose>
									<xsl:when test="position() mod 2">RowsOdd</xsl:when>
									<xsl:otherwise>RowsEven</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<tr>
								<td>
									<input type="checkbox" name="art_id" value="{art_id}"/>			 
									<a class="Text color-blue108" href="javascript:wbarticle.get_article_view(wbEncrytor.cwnEncrypt({art_id}));" >
										<xsl:choose>
											<xsl:when test="string-length(art_title)>30">
												<span class="Text" title="{art_title}">
													<xsl:value-of select="substring(art_title,0,30)"/>...
												</span>
											</xsl:when>
											<xsl:otherwise>
												<span class="Text" >
													<xsl:value-of select="art_title"/>
												</span>
											</xsl:otherwise>
										</xsl:choose>
									</a>
								</td>																									
								<td align="left">
									<xsl:value-of select="reg_user/usr_display_bil"/>
								</td>								
								<td align="left">
									<xsl:choose>
										<xsl:when test="art_status = '1'">
											<xsl:value-of select="$lab_select_on"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$lab_select_off"/>
										</xsl:otherwise>
									</xsl:choose>
								</td>
								<td align="left">
									<xsl:value-of select="art_type"/>
								</td>
								<td>
									<xsl:call-template name="display_time">
										<xsl:with-param name="my_timestamp"><xsl:value-of select="art_create_datetime"/></xsl:with-param>
										<xsl:with-param name="dis_time">T</xsl:with-param>
									</xsl:call-template>
								</td>
								<td align="right">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="class">btn wzb-btn-blue margin-right4</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_update_button"/></xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">javascript:wbarticle.get_article_prep(wbEncrytor.cwnEncrypt('<xsl:value-of select="art_id"/>'))</xsl:with-param>
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
	
	<xsl:template name="article_sel_status" >
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