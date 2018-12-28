<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<!-- 
			<xsl:import href="utils/wb_init_lab.xsl"/>
	 -->

	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="share/div_tree_share.xsl"/>
	<xsl:import href="share/gen_tree_js.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="page_variant" select="/announcement/meta/page_variant"/>
	<xsl:variable name="training_center" select="/announcement/message/cur_training_center" />
	<xsl:variable name="ann_cnt" select="count(/announcement/message/item)"/>
	<!-- paginatoin variables -->
	<xsl:variable name="page_size" select="/announcement/message/pagination/@page_size"/>
	<xsl:variable name="cur_page" select="/announcement/message/pagination/@cur_page"/>
	<xsl:variable name="total" select="/announcement/message/pagination/@total_rec"/>
	<xsl:variable name="timestamp" select="/announcement/message/pagination/@timestamp"/>
	<!-- sorting variable -->
	<xsl:variable name="cur_sort_col" select="/announcement/message/pagination/@sort_col"/>
	<xsl:variable name="cur_sort_order" select="/announcement/message/pagination/@sort_order"/>
	<xsl:variable name="sort_order_by">
		<xsl:choose>
			<xsl:when test="$cur_sort_order = 'ASC' or $cur_sort_order ='asc'">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="msg_type" select="/announcement/message/@type"/>
	<xsl:variable name="res_id" select="/announcement/message/@res_id"/>
	<xsl:variable name="is_show_all" select="/announcement/message/show_all"/>
	<xsl:variable name="is_read_only" select="/announcement/read_only"/>
	<xsl:variable name="tc_enabled" select="/announcement/meta/tc_enabled"/>
	<xsl:variable name="isMobile" select="/announcement/isMobile"/>
	<xsl:variable name="search_title" select="/announcement/search_title"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="announcement"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="announcement">
		<xsl:apply-templates select="message"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="message">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_announcement.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				announcement = new wbAnnouncement;
				var encrytor = wbEncrytor();
				function show_content(tcr_id) {
					announcement.show_content(tcr_id,']]><xsl:value-of select="$isMobile"/><![CDATA[');
				}

				function load_tree() {
					if (frmXml.tc_enabled_ind) {
						page_onload(250);
					}
				}
			]]></SCRIPT>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="load_tree()">
			<form name="frmXml" method="" action="" onsubmit="return false;">
				<xsl:call-template name="content"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="msg_type" value=""/>
				<input type="hidden" name="msg_lst" value=""/>
				<xsl:if test="$msg_type = 'RES'">
					<input type="hidden" name="res_id" value="{$res_id}"/>
				</xsl:if>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	
	
	 <xsl:variable name="lab_g_form_btn_remove" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_1')"/>
	<xsl:variable name="lab_g_form_btn_add" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_2')"/>
	<xsl:variable name="lab_g_lst_btn_edit" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_3')"/>
	<xsl:variable name="lab_g_lst_btn_receipt" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_4')"/>
	
 	  	 
 	  	   <!-- 
 	  	
 	 <xsl:choose> 
			<xsl:when test="$isMobile='true'"><xsl:variable name="lab_ann" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_5')"/></xsl:when>
			<xsl:otherwise><xsl:variable name="lab_ann" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_6')"/></xsl:otherwise>
	</xsl:choose>
	 -->
	
	<xsl:variable name="lab_title" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_7')"/>
	<xsl:variable name="lab_create_by" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_8')"/>
	<xsl:variable name="lab_date" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_9')"/>
	<xsl:variable name="lab_view" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang,'label_im.l_im.label_core_information_management_10')"/>
	<xsl:variable name="lab_no_item" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang,'label_im.label_core_information_management_11')"/>
	<xsl:variable name="lab_status" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_12')"/>
	<xsl:variable name="lab_on" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_13')"/>
	<xsl:variable name="lab_off" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_14')"/>
	<xsl:variable name="lab_ann_desc" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_15')"/>
	<xsl:variable name="lab_ann_all" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_16')"/>
	<xsl:variable name="lab_tcr_title" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_17')"/>
	<xsl:variable name="lab_ann_by_tcr" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_18')"/>
	<xsl:variable name="lab_cur_tcr" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_19')"/>
	<xsl:variable name="lab_root_training_center" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_20')"/>

	
	
	
	
	 
	<!-- =============================================================== -->
	<xsl:template name="content">
	 
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_MSG_MAIN</xsl:with-param>
			<xsl:with-param name="page_title">
				<xsl:choose>
					<xsl:when test="$page_variant/@hasDelSysAnnBtn = 'true' and $is_read_only='false'">
						<xsl:call-template name="get_lab"> 
							<xsl:with-param name="lab_title">global.FTN_AMD_MSG_MAIN</xsl:with-param>
						 </xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="get_lab">
							<xsl:with-param name="lab_title">global.FTN_AMD_SYS_MSG_LIST</xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
						
			</xsl:with-param>
		</xsl:call-template>
		
		 
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$isMobile='true'"><xsl:value-of select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_5')"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_6')"/></xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		
		<!-- list view -->
		<table>
			<tr>
				<td width="40%">
					<xsl:if test="$tc_enabled='true' and $is_read_only = 'false'">
						<xsl:variable name="title_var">
							<xsl:choose>
								<xsl:when test="$is_show_all"><xsl:value-of select="$lab_root_training_center"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="//cur_training_center/title"/></xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:call-template name="div_tree">
							<xsl:with-param name="lab_cur_tcr" select="$lab_cur_tcr"/>
							<xsl:with-param name="lab_root_training_center" select="$lab_root_training_center"/>
							<xsl:with-param name="title" select="$title_var"/>
						</xsl:call-template>
						<input type="hidden" name="tc_enabled_ind"/>
					</xsl:if>
				</td>
				<td align="right" width="60%">
					<div class="wzb-form-search">
						<input type="text" value="{$search_title}" size="11" id="title_code" name="title_code" class="form-control" style="width:130px;" placeholder="{$lab_title}"/>
						<input type="button" class="form-submit margin-right4" value="" onclick="announcement.search_result_notice('{$isMobile}')"/>
					
					
					<!-- Access Control -->
					<xsl:variable name="from_show_all">
						<xsl:choose>
							<xsl:when test="$tc_enabled='true'">
								<xsl:choose>
									<xsl:when test="$is_show_all='true'">true</xsl:when>
									<xsl:when test="$is_show_all='false'">false</xsl:when>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise></xsl:otherwise>
						</xsl:choose>
					</xsl:variable>					
					<xsl:if test="$page_variant/@hasAddSysAnnBtn= 'true' and $is_read_only='false'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_add"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:announcement.add_sys_ann_lst('<xsl:value-of select="$msg_type"/>','<xsl:value-of select="$res_id"/>', '', '<xsl:value-of select="$from_show_all"/>', '<xsl:value-of select="$isMobile"/>', '', '<xsl:value-of select="$training_center/@id" />')</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="$page_variant/@hasDelSysAnnBtn = 'true' and $is_read_only='false'">
						<xsl:if test="$ann_cnt &gt;= 1">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_remove"/></xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:announcement.multi_del_sys_ann_lst(document.frmXml,'<xsl:value-of select="$msg_type"/>','','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
							</xsl:call-template>
						</xsl:if>
					</xsl:if>
					<!--
					<xsl:if test="$tc_enabled='true'">
						<xsl:choose>
							<xsl:when test="$is_show_all = 'true'">
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_ann_by_tcr"/></xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:announcement.group_by_tc('<xsl:value-of select="$is_read_only"/>')</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_ann_all"/></xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:announcement.show_all_ann('<xsl:value-of select="$is_read_only"/>')</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>						
					</xsl:if>
					-->
					<!-- Access Control -->
					</div>
				</td>
			</tr>
		</table>
		<xsl:choose>
			<xsl:when test="$ann_cnt &gt;= 1">
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<xsl:if test="$page_variant/@hasDelSysAnnBtn = 'true'">
							<!-- <td width="1%">
							</td> 样式调整，去掉td-->
						</xsl:if>
						<td width="30%">
							<!-- Access Control -->
							<xsl:choose>
								<xsl:when test="$page_variant/@hasDelSysAnnBtn = 'true' and $is_read_only='false'">
									<xsl:call-template name="select_all_checkbox">
										<xsl:with-param name="chkbox_lst_cnt"><xsl:value-of select="$ann_cnt"/></xsl:with-param>
										<xsl:with-param name="display_icon">false</xsl:with-param>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
								</xsl:otherwise>
							</xsl:choose>
							<!-- Access Control -->
							
							<!-- msg_title -->			
							<xsl:variable name="_order">
								<xsl:choose>
									<xsl:when test="$cur_sort_col = 'msg_title' ">
										<xsl:value-of select="$sort_order_by"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$cur_sort_order"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'msg_title'">
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','msg_title','sortOrder','{$_order}','cur_page','1')">
										<xsl:value-of select="$lab_title"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
											<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','msg_title','sortOrder','asc','cur_page','1')">
										<xsl:value-of select="$lab_title"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
							<!-- msg_title -->
						</td>
						<xsl:if test="$page_variant/@ShowSysAnnStatus = 'true'">
							<td width="10%" align="left">
								<xsl:variable name="_order">
									<xsl:choose>
										<xsl:when test="$cur_sort_col = 'usr_display_bil' ">
											<xsl:value-of select="$sort_order_by"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$cur_sort_order"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<xsl:choose>
									<xsl:when test="$cur_sort_col = 'usr_display_bil' ">
										<a href="javascript:wb_utils_nav_get_urlparam('sortCol','usr_display_bil','sortOrder','{$_order}','cur_page','1')">
											<xsl:value-of select="$lab_create_by"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
												<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wb_utils_nav_get_urlparam('sortCol','usr_display_bil','sortOrder','asc','cur_page','1')">
											<xsl:value-of select="$lab_create_by"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
						</xsl:if>
						<!-- Access Control -->
						<xsl:if test="$page_variant/@ShowSysAnnStatus = 'true'">
							<td width="8%" align="left">
								<xsl:variable name="_order">
									<xsl:choose>
										<xsl:when test="$cur_sort_col = 'msg_status' ">
											<xsl:value-of select="$sort_order_by"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$cur_sort_order"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<xsl:choose>
									<xsl:when test="$cur_sort_col = 'msg_status' ">
										<a href="javascript:wb_utils_nav_get_urlparam('sortCol','msg_status','sortOrder','{$_order}','cur_page','1')">
											<xsl:value-of select="$lab_status"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
												<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wb_utils_nav_get_urlparam('sortCol','msg_status','sortOrder','asc','cur_page','1')">
											<xsl:value-of select="$lab_status"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
						</xsl:if>
						<!-- Access Control -->
						<td width="15%" align="left">
							<xsl:variable name="_order">
								<xsl:choose>
									<xsl:when test="$cur_sort_col = 'msg_begin_date' ">
										<xsl:value-of select="$sort_order_by"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$cur_sort_order"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'msg_begin_date' ">
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','msg_begin_date','sortOrder','{$_order}','cur_page','1')">
										<xsl:value-of select="$lab_date"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
											<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','msg_begin_date','sortOrder','asc','cur_page','1')">
										<xsl:value-of select="$lab_date"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<!--培训中心-->
						<xsl:if test="$is_show_all = 'true' and $tc_enabled = 'true'">					
						<td width="15%">
							<xsl:variable name="_order">
								<xsl:choose>
									<xsl:when test="$cur_sort_col = 'tcr_title'">
										<xsl:value-of select="$sort_order_by"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$cur_sort_order"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'tcr_title'">
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','tcr_title','sortOrder','{$_order}','cur_page','1')">
										<xsl:value-of select="$lab_tcr_title"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
											<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sortCol','tcr_title','sortOrder','asc','cur_page','1')">
										<xsl:value-of select="$lab_tcr_title"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						</xsl:if>
						<xsl:if test="$tc_enabled='true' and $is_read_only = 'false'">	
							<td width="22%" align="right"></td>
						</xsl:if>
					</tr>
					<xsl:apply-templates select="item">
						<xsl:with-param name="lab_g_lst_btn_edit"><xsl:value-of select="$lab_g_lst_btn_edit"/></xsl:with-param>
						<xsl:with-param name="lab_g_lst_btn_receipt"><xsl:value-of select="$lab_g_lst_btn_receipt"/></xsl:with-param>
						<xsl:with-param name="lab_on"><xsl:value-of select="$lab_on"/></xsl:with-param>
						<xsl:with-param name="lab_off"><xsl:value-of select="$lab_off"/></xsl:with-param>
					</xsl:apply-templates>
				</table>
				
				
				<!-- Pagination -->
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="timestamp" select="$timestamp"/>
					<xsl:with-param name="total" select="$total"/>
					<xsl:with-param name="width"><xsl:value-of select="$wb_gen_table_width"/></xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_item"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="item">
		<xsl:param name="lab_g_lst_btn_edit"/>
		<xsl:param name="lab_g_lst_btn_receipt"/>
		<xsl:param name="lab_on"/>
		<xsl:param name="lab_off"/>
		<tr>
			<xsl:if test="$page_variant/@hasDelSysAnnBtn = 'true'">
				<!-- <td>样式调整，去掉td
				</td> -->
			</xsl:if>
			<td width="20%">
				<!-- Access Control -->
				<xsl:choose>
					<xsl:when test="$page_variant/@hasDelSysAnnBtn = 'true' and $is_read_only='false'">
						<input type="checkbox" name="" value="{@id}"/>
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
				</xsl:choose>
				<!-- Access Control -->
				
				<xsl:choose>
					<xsl:when test="$page_variant/@hasDelSysAnnBtn = 'true' and $is_read_only='false'">
						<a class="Text color-blue108" href="javascript:announcement.get_ann_dtl(encrytor.cwnEncrypt({@id}), '', {$is_read_only})">
							<xsl:choose>
								<xsl:when test="string-length(title)&gt;40">
									<xsl:value-of select="substring(title,1,40)"/>
									<xsl:text>...</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="title"/>
								</xsl:otherwise>
							</xsl:choose>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a class="Text color-blue108" href="javascript:announcement.get_ann_dtl(encrytor.cwnEncrypt({@id}), 'read_only', {$is_read_only})">
							<xsl:choose>
								<xsl:when test="string-length(title)&gt;40">
									<xsl:value-of select="substring(title,1,40)"/>
									<xsl:text>...</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="title"/>
								</xsl:otherwise>
							</xsl:choose>
						</a>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<xsl:if test="$page_variant/@ShowSysAnnStatus = 'true'">
				<td align="left" style="width:12%;">
					<xsl:value-of select="@usr_display_bil"/>
				</td>
			</xsl:if>
			<!-- Access Control -->
			<xsl:if test="$page_variant/@ShowSysAnnStatus = 'true'">
				<td align="left" style="width:8%;">
					<xsl:choose>
						<xsl:when test="@status = 'ON'">
							<xsl:value-of select="$lab_on"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_off"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
			<!-- Access Control -->
			<td style="width:12%;">
				<xsl:call-template name="display_time">
					<xsl:with-param name="my_timestamp"><xsl:value-of select="@begin_date"/></xsl:with-param>
					<xsl:with-param name="dis_time">T</xsl:with-param>
				</xsl:call-template>
			</td>
			<!--显示培训中心-->
			<xsl:if test="$is_show_all = 'true' and $tc_enabled = 'true'">
				<td style="width:14%;">
					<xsl:value-of select="training_center/title"/>
				</td>
			</xsl:if>
			<!-- 是否回执 -->
			<xsl:if test="($page_variant/@hasEditSysAnnBtn = 'true' and $is_read_only='false' and @isReceipt = 'true') or ($page_variant/@hasEditSysAnnBtn = 'true' and $is_read_only='false')">
			<td align="right" style="width:16%;">
				<xsl:choose>
					<xsl:when test="$page_variant/@hasEditSysAnnBtn = 'true' and $is_read_only='false' and @isReceipt = 'true' ">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_lst_btn_receipt"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:announcement.show_receipt_views('<xsl:value-of select="@id"/>')</xsl:with-param>
							<xsl:with-param name="class">btn wzb-btn-blue margin-right4</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$page_variant/@hasEditSysAnnBtn = 'true' and $is_read_only='false'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_lst_btn_edit"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:announcement.upd_sys_ann_lst('<xsl:value-of select="$msg_type"/>',encrytor.cwnEncrypt('<xsl:value-of select="@id"/>'),'<xsl:value-of select="$res_id"/>', '', '<xsl:value-of select="$is_show_all"/>','true', '<xsl:value-of select="$isMobile"/>')</xsl:with-param>
							<xsl:with-param name="class">btn wzb-btn-blue margin-right4</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			</xsl:if>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
