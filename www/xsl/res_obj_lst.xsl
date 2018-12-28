<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/div_tree_share.xsl"/>
	<xsl:import href="share/gen_tree_js.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl" />
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="syb_id" select="objective_list/syllabus/@id"/>
   <xsl:variable name="curr" select="/objective_list/folders/text()" />
   <xsl:variable name="def_tc" select="/objective_list/training_center/title/text()"/>
   <xsl:variable name="cur_role" select="/objective_list/meta/cur_usr/role/@id"/>
   <xsl:variable name="folders" select="/objective_list/folders"/>
   <xsl:variable name="isTcIndependent" select="/objective_list/syllabus/isTcIndependent"/>
   <xsl:variable name="isAdmin">
   		<xsl:choose>
			<xsl:when test="/objective_list/meta/cur_usr/granted_functions/functions/function[@id = 'LRN_RES_ADMIN']">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
   </xsl:variable>
	<xsl:variable name="is_show_all" select="/objective_list/syllabus/show_all/text()"/>
	<xsl:variable name="cata_cnt" select="count(/objective_list/syllabus/body/node)"/>
	<xsl:variable name="shared_cnt" select="count(/objective_list/syllabus/shared_folders/node)"/>
	<xsl:variable name="sort_order_by">
		<xsl:choose>
			<xsl:when test="$cur_sort_order = 'ASC' or $cur_sort_order ='asc'">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- paginatoin variables -->
	<xsl:variable name="page_size" select="/objective_list/syllabus/pagination/@page_size"/>
	<xsl:variable name="cur_page" select="/objective_list/syllabus/pagination/@cur_page"/>
	<xsl:variable name="total" select="/objective_list/syllabus/pagination/@total_rec"/>
	<xsl:variable name="timestamp" select="/objective_list/pagination/@timestamp"/>
	<xsl:variable name="tc_enabled" select="/objective_list/meta/tc_enabled"/>
	<xsl:variable name="cur_sort_col" select="/objective_list/syllabus/pagination/@sort_col"/>
	<xsl:variable name="cur_sort_order" select="/objective_list/syllabus/pagination/@sort_order"/>
	<xsl:variable name="lrn_view">LRN_VIEW</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="objective_list"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="objective_list">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}wb_objective.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}wb_search.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="Javascript" src="{$wb_js_path}wb_batchprocess.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js" type="text/javascript"/>
			<script language="Javascript" TYPE="text/javascript"><![CDATA[
			obj = new wbObjective;
			seh = new wbSearch;
			Batch = new wbBatchProcess;
			obj_tcr_id  = getUrl('obj_tcr_id')?getUrlParam('obj_tcr_id'):''
			function status(){
				seh.simple(document.frmXml,']]><xsl:value-of select="$wb_lang"/><![CDATA[')
				return false;
			}
			window.onload=load_tree;
			function load_tree() {
				if (frmXml.tc_enabled_ind) {
					page_onload(250);
				}
			}
			function show_content(tcr_id) {

			   if(tcr_id ==0){
			   		show_all = true;
			   } else {
			   		show_all = false;
			   }
       		javascript:obj.show_all_obj_lst('',']]><xsl:value-of select="$folders"/><![CDATA[',show_all, tcr_id)
			}
		]]></script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="load_tree();">
			<form name="frmXml" onsubmit="return status()">
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="search_title"/>
				<input type="hidden" name="search_items_per_page"/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="obj_id_lst"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="res_type"/>
				<xsl:call-template name="content"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	
	
<xsl:variable name="lab_sys_list" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_94')"/>
<xsl:variable name="lab_lost_and_found" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_95')"/>
<xsl:variable name="lab_item" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_96')"/>
<xsl:variable name="lab_g_txt_btn_import_res" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_97')"/>
<xsl:variable name="lab_g_txt_btn_export_res" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_98')"/>
<xsl:variable name="lab_g_txt_btn_add" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_99')"/>
<xsl:variable name="lab_g_txt_btn_paste" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_100')"/>
<xsl:variable name="lab_no_syb" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_101')"/>
<xsl:variable name="lab_desc" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_102')"/>
<xsl:variable name="lab_desc_admin" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_103')"/>
<xsl:variable name="lab_all_categories" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_104')"/>
<xsl:variable name="lab_subcategories" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_105')"/>
<xsl:variable name="lab_view" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_106')"/>
<xsl:variable name="lab_myfolder" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_107')"/>
<xsl:variable name="lab_allfolder" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_108')"/>
<xsl:variable name="lab_view_desc" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_109')"/>
<xsl:variable name="lab_view_all_desc" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_110')"/>
<xsl:variable name="lab_reader_icon" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_111')"/>
<xsl:variable name="lab_contri_icon" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_112')"/>
<xsl:variable name="lab_owner_icon" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_113')"/>
<xsl:variable name="lab_noaccess_icon" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_114')"/>
<xsl:variable name="lab_all_folder" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_115')"/>
<xsl:variable name="lab_del_folder" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_116')"/>
<xsl:variable name="lab_by_ctr_folder" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_117')"/>
<xsl:variable name="lab_title" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_118')"/>
<xsl:variable name="lab_child_cata" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_119')"/>
<xsl:variable name="lab_course_count" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_120')"/>
<xsl:variable name="lab_tcr_title" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_121')"/>
<xsl:variable name="lab_edit" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_122')"/>
<xsl:variable name="lab_cur_tcr" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_123')"/>
<xsl:variable name="lab_root_training_center" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_124')"/>
<xsl:variable name="lab_shared_folder" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_125')"/>
<xsl:variable name="lab_search_text" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_32')"/>
	
	
	<!-- =============================================================== -->
	<xsl:template name="content">
		
		<xsl:variable name="reader">READER</xsl:variable>
		<xsl:variable name="author">AUTHOR</xsl:variable>
		<xsl:variable name="owner">OWNER</xsl:variable>
		<xsl:variable name="noaccess"></xsl:variable>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_sys_list"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text">
			  	<xsl:choose>
					<xsl:when test="$isAdmin = 'true'">
						<xsl:value-of select="$lab_desc_admin"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_desc"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_space">
	      <xsl:with-param name="height">15</xsl:with-param>
	   </xsl:call-template>
	  	<xsl:choose>
			<xsl:when test="$isAdmin = 'true'">
				<xsl:call-template name="adm_page_head" >
					<xsl:with-param name="lab_sys_list" select="$lab_sys_list"/>
					<xsl:with-param name="lab_lost_and_found" select="$lab_lost_and_found"/>
					<xsl:with-param name="lab_item" select="$lab_item"/>
					<xsl:with-param name="lab_g_txt_btn_add" select="$lab_g_txt_btn_add"/>
					<xsl:with-param name="lab_g_txt_btn_paste" select="$lab_g_txt_btn_paste"/>
					<xsl:with-param name="lab_no_syb" select="$lab_no_syb"/>
					<xsl:with-param name="lab_desc" select="$lab_desc"/>
					<xsl:with-param name="lab_all_categories" select="$lab_all_categories"/>
					<xsl:with-param name="lab_subcategories" select="$lab_subcategories"/>
					<xsl:with-param name="tc_enabled" select="$tc_enabled"/>
					<xsl:with-param name="is_show_all" select="$is_show_all"/>
					<xsl:with-param name="lab_root_training_center" select="$lab_root_training_center"/>
					<xsl:with-param name="def_tc" select="$def_tc"/>
					<xsl:with-param name="lab_cur_tcr" select="$lab_cur_tcr"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="tadm_page_head">
					<xsl:with-param name="lab_sys_list" select="$lab_sys_list"/>
					<xsl:with-param name="lab_lost_and_found" select="$lab_lost_and_found"/>
					<xsl:with-param name="lab_item" select="$lab_item"/>
					<xsl:with-param name="lab_g_txt_btn_add" select="$lab_g_txt_btn_add"/>
					<xsl:with-param name="lab_g_txt_btn_paste" select="$lab_g_txt_btn_paste"/>
					<xsl:with-param name="lab_no_syb" select="$lab_no_syb"/>
					<xsl:with-param name="lab_desc" select="$lab_desc"/>
					<xsl:with-param name="lab_all_categories" select="$lab_all_categories"/>
					<xsl:with-param name="lab_subcategories" select="$lab_subcategories"/>
					<xsl:with-param name="lab_view" select="$lab_view"/>
					<xsl:with-param name="lab_myfolder" select="$lab_myfolder"/>
					<xsl:with-param name="lab_allfolder" select="$lab_allfolder"/>
					<xsl:with-param name="lab_reader_icon" select="$lab_reader_icon" />
					<xsl:with-param name="lab_contri_icon" select="$lab_contri_icon" />
					<xsl:with-param name="lab_owner_icon" select="$lab_owner_icon" />
					<xsl:with-param name="lab_noaccess_icon" select="$lab_noaccess_icon" />
					<xsl:with-param name="lab_view_desc" select="$lab_view_desc" />
					<xsl:with-param name="lab_view_all_desc" select="$lab_view_all_desc" />
					<xsl:with-param name="tc_enabled" select="$tc_enabled"/>
					<xsl:with-param name="is_show_all" select="$is_show_all"/>
					<xsl:with-param name="lab_root_training_center" select="$lab_root_training_center"/>
					<xsl:with-param name="def_tc" select="$def_tc"/>
					<xsl:with-param name="lab_cur_tcr" select="$lab_cur_tcr"/>
				 </xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<!-- simple & advance search
		<table cellspacing="0" cellpadding="0" border="0" width="{$wb_gen_table_width}">
			<tr>
			   <td align="left">
			      <table cellpadding="1" cellspacing="0" border="0">
			           <tr>
			                        <td>
			                             <xsl:value-of select="$lab_view" />
			                        </td>
								<td>
								   <select name="folders" class="select">
								      <option value="0"><xsl:value-of select="$lab_myfolder" /></option>
								      <option value="1"><xsl:value-of select="$lab_allfolder" /></option>
								   </select >
								</td>
							</tr>
               </table>
			   </td>
				<td align="right">
					<table cellpadding="1" cellspacing="0" border="0">
						<tr>
							<td>
								<input type="text" size="11" name="search" class="wzb-inputText" style="width:130px;"/>
							</td>
							<td>
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_gen_search"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:seh.simple(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
								</xsl:call-template>
							</td>
							<td>
								<a href="javascript:seh.adv_prep()" class="smallText">
									<xsl:value-of select="$lab_adv_search"/>
								</a>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_line"/>
		<table cellspacing="0" cellpadding="0" border="0" width="{$wb_gen_table_width}">
		  <tr><td align="left" width="100%"><xsl:value-of select="$lab_view_desc" /></td></tr>
		</table>
		<table cellspacing="0" cellpadding="0" border="0" width="{$wb_gen_table_width}">
		  <tr><td width="5%"></td><td><xsl:value-of select="$lab_reader_icon" /></td></tr>
		  <tr><td width="5%"></td><td><xsl:value-of select="$lab_contri_icon" /></td></tr>
		  <tr><td width="5%"></td><td><xsl:value-of select="$lab_owner_icon" /></td></tr>
		</table> -->

		<table class="wzb-ui-head">
			<tr>
				<td align="right">
					<xsl:if test="$cur_role !='INSTR_1' ">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_add"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:obj.ins_prep(<xsl:value-of select="$syb_id"/>,'0',frmXml,'<xsl:value-of select="$folders"/>','<xsl:value-of select="$is_show_all"/>',obj_tcr_id)</xsl:with-param>
					</xsl:call-template>
					</xsl:if>
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_del_folder"/>
						<xsl:with-param name="wb_gen_btn_href">javascript: obj.del_mutil_obj(frmXml, '<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_import_res"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Res.Import.prep_page()</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_export_res"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Res.Export.prep()</xsl:with-param>
					</xsl:call-template>
					<!--
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_by_ctr_folder"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:obj.show_all_obj_lst('','<xsl:value-of select="$folders"/>','false')</xsl:with-param>
					</xsl:call-template>
					-->
				</td>
			</tr>
		</table>
		<!--显示所有文件夹-->
		<xsl:call-template name="show_all">
	       <xsl:with-param name="lab_del_folder" select="$lab_del_folder"/>
	       <xsl:with-param name="lab_by_ctr_folder" select="$lab_by_ctr_folder"/>
	       <xsl:with-param name="lab_title" select="$lab_title"/>
	       <xsl:with-param name="lab_child_cata" select="$lab_child_cata"/>
	       <xsl:with-param name="lab_course_count" select="$lab_course_count"/>
	       <xsl:with-param name="lab_tcr_title" select="$lab_tcr_title"/>
	       <xsl:with-param name="lab_no_syb" select="$lab_no_syb"/>
	       <xsl:with-param name="lab_edit" select="$lab_edit"/>
			<xsl:with-param name="reader" select="$reader" />
			<xsl:with-param name="author" select="$author" />
			<xsl:with-param name="owner" select="$owner" />
			<xsl:with-param name="noaccess" select="$noaccess" />
		</xsl:call-template>

		<!-- 二级培训中心独立时，不显示共享文件夹 -->
		<xsl:if test="$isTcIndependent = 'false'">
			<xsl:if test="$shared_cnt >0">
				<xsl:call-template name="wb_ui_space">
					<xsl:with-param name="height">5</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="show_shared_folder">
					<xsl:with-param name="lab_shared_folder" select="$lab_shared_folder"/>
					<xsl:with-param name="lab_subcategories" select="$lab_subcategories"/>
					<xsl:with-param name="lab_item" select="$lab_item"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
		
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="syllabus">
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_item"/>
		<xsl:param name="lab_subcategories"/>
		<xsl:param name="reader" />
		<xsl:param name="author" />
		<xsl:param name="owner" />
		<xsl:param name="noaccess" />
		<!-- hide the objective which have the attribute of SYS-->
		<xsl:for-each select="body/node[@type = 'SYB']">
		   <xsl:variable name="access" select="@access" />
			<xsl:if test="not(position() mod 2 = 0) ">
				<xsl:text disable-output-escaping="yes">&lt;tr&gt;</xsl:text>
			</xsl:if>
			<td width="50%" valign="top" style="padding:5px 0;">
				<table>
					<tr>
						<xsl:choose>
							<xsl:when test="@type = 'SYB'">
								<td width="35" valign="top" align="left">
									<img src="{$wb_img_path}ico_big_folder.gif" border="0"/>
								</td>
								<td>
									<table>
										<tr>
											<td>
												<xsl:choose>
													<xsl:when test="$access = $noaccess or $access = $lrn_view">
														<a class="TitleTextBold color-word" href="javascript:obj.view_right_read('{@id}','{$curr}')">
															<xsl:value-of select="obj_desc"/>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<a class="TitleTextBold color-word" href="javascript:obj.manage_obj_lst(frmXml,'{@id}','','','{$is_show_all}')">
															<xsl:value-of select="obj_desc"/>
														</a>
														<xsl:text>&#160;(</xsl:text>
														<xsl:value-of select="@obj_count"/>
														<xsl:text/>
														<xsl:text>)</xsl:text>
													</xsl:otherwise>
												</xsl:choose>
											</td>
										</tr>
										<tr>
											<td>
												<xsl:call-template name="draw_cnt">
													<xsl:with-param name="cnt" select="@count"/>
													<xsl:with-param name="id" select="@id"/>
													<xsl:with-param name="lab_item" select="$lab_item"/>
													<xsl:with-param name="access" select="$access" />
													<xsl:with-param name="noaccess" select="$noaccess" />
												</xsl:call-template>
											</td>
										</tr>
									</table>
								</td>
							</xsl:when>
							<xsl:when test="@type = 'SYS'">
								<td width="35" valign="top" align="center">
									<img src="{$wb_img_path}icon_recyc.gif" border="0" align="absmiddle"/>
								</td>
								<td>
									<a class="TitleTextBold" href="javascript:obj.show_trash_obj_lst('{@id}')">
										<xsl:value-of select="$lab_lost_and_found"/>
									</a>
									<br/>
									<xsl:choose>
										<xsl:when test="@count &gt;= 1">
											<a class="SmallText" href="javascript:obj.show_trash_obj_lst('{@id}')">
												<xsl:value-of select="@count + @own_count"/>
												<xsl:text>&#160;</xsl:text>
												<xsl:value-of select="$lab_item"/>
											</a>
										</xsl:when>
									</xsl:choose>
								</td>
							</xsl:when>
						</xsl:choose>
					</tr>
				</table>
			</td>
			<xsl:if test="position() = last() and not(position() mod 2 = 0 )">
				<xsl:text disable-output-escaping="yes">&lt;td&gt;&amp;nbsp;&lt;/td&gt;&lt;/tr&gt;</xsl:text>
			</xsl:if>
			<xsl:if test="position() mod 2 = 0">
				<xsl:text disable-output-escaping="yes">&lt;/tr&gt;</xsl:text>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!--================================================================ -->
	<!--<xsl:template name="draw_desc">
	   <xsl:param name="desc_type" />
	   <xsl:param name="acess" />
	   <xsl:param name="reader" />
		<xsl:param name="author" />
		<xsl:param name="owner" />
		<xsl:param name="noaccess" />
		<xsl:variable name="img">img</xsl:variable>
		<xsl:variable name="link">link</xsl:variable>
      <xsl:choose>
			<xsl:when test="/objective_list/meta/cur_usr/role/@id = 'ADM_1'">
			    <xsl:if test="">
						<img src="{$wb_img_path}ico_big_folder.gif" border="0"/>
				 </xsl:if>
		  </xsl:when>
			<xsl:otherwise>
					<xsl:if test="$access = $reader">
						<img src="{$wb_img_path}ico_big_folder.gif" border="0"/>
					</xsl:if>
					<xsl:if test="$access = $author">
						<img src="{$wb_img_path}ico_big_folder.gif" border="0"/>
					</xsl:if>
					<xsl:if test="$access = $owner">
						<img src="{$wb_img_path}ico_big_folder.gif" border="0"/>
					</xsl:if>
					<xsl:if test="$access = $noaccess">
						<img src="{$wb_img_path}ico_workfolder.gif" border="0"/>
					</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	 </xsl:template>-->
	<!-- =============================================================== -->
	<xsl:template name="draw_cnt">
		<xsl:param name="cnt"/>
		<xsl:param name="id"/>
		<xsl:param name="lab_public"/>
		<xsl:param name="lab_item"/>
		<xsl:param name="access" />
		<xsl:param name="noaccess" />
		<!--<xsl:if test="$access = $noaccess">
		     <a class="SmallText"  href="javascript:obj.view_right_read('{@id}','{$curr}')">
		     <xsl:choose>
				<xsl:when test="$cnt &gt;= 1">
					<xsl:value-of select="$cnt"/>
				</xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="$lab_item"/>
	     	</a>
		</xsl:if>-->
		<xsl:if test="not($access = $noaccess) and not($access = $lrn_view)">
		    <a href="javascript:obj.show_obj_lst('{$syb_id}','{$id}',frmXml,'')" class="SmallText ">
		    <xsl:choose>
				<xsl:when test="$cnt &gt;= 1">
					<xsl:value-of select="$cnt"/>
				</xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="$lab_item"/>
		</a>
		</xsl:if>

	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="tadm_page_head">
        <xsl:param name="lab_sys_list"/>
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_item"/>
	    <xsl:param name="lab_g_txt_btn_add"/>
		<xsl:param name="lab_g_txt_btn_paste"/>
	    <xsl:param name="lab_no_syb"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_all_categories"/>
		<xsl:param name="lab_subcategories"/>
		<xsl:param name="lab_view"/>
		<xsl:param name="lab_myfolder"/>
		<xsl:param name="lab_allfolder"/>
		<xsl:param name="lab_reader_icon" />
		<xsl:param name="lab_contri_icon" />
		<xsl:param name="lab_owner_icon" />
		<xsl:param name="lab_noaccess_icon" />
		<xsl:param name="lab_view_desc" />
		<xsl:param name="lab_view_all_desc"/>
		<xsl:param name="tc_enabled"/>
		<xsl:param name="is_show_all"/>
		<xsl:param name="lab_root_training_center"/>
		<xsl:param name="def_tc"/>
		<xsl:param name="lab_cur_tcr"/>
		<xsl:variable name="curr" select="/objective_list/folders/text()" />
		<xsl:variable name="myFolder">myFolder</xsl:variable>
		<xsl:variable name="allFolder">allFolder</xsl:variable>
        <table>
			<tr>
			   	<td width="40%">
			   		<xsl:if test="$tc_enabled='true'">
						<xsl:variable name="title_var">
							<xsl:choose>
								<xsl:when test="$is_show_all='true'"><xsl:value-of select="$lab_root_training_center"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="$def_tc"/></xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:call-template name="div_tree">
	        				<xsl:with-param name="lab_cur_tcr" select="$lab_cur_tcr"/>
	        				<xsl:with-param name="title" select="$title_var"/>
	        				<xsl:with-param name="lab_root_training_center" select="$lab_root_training_center"/>
	     				</xsl:call-template>
	     				<input type="hidden" name="tc_enabled_ind"/>
     				</xsl:if>
			   </td>
				<td width="60%" align="right">
					<table>
						<tr>
							<td align="right" width="90%">
								<div class="wzb-form-search">
									<input type="text" size="11" name="search" class="form-control" style="width:190px;" placeholder="{$lab_search_text}"/>
									<input type="button" class="form-submit margin-right4" value="" onclick="seh.simple(document.frmXml, '{$wb_lang}')"/>
									<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-blue margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_adv_search"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:seh.adv_prep()</xsl:with-param>
								</xsl:call-template>
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
   <xsl:template name="adm_page_head">
        <xsl:param name="lab_sys_list"/>
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_item"/>
	    <xsl:param name="lab_g_txt_btn_add"/>
		<xsl:param name="lab_g_txt_btn_paste"/>
	    <xsl:param name="lab_no_syb"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_all_categories"/>
		<xsl:param name="lab_subcategories"/>
		<xsl:param name="lab_view"/>
		<xsl:param name="lab_myfolder"/>
		<xsl:param name="lab_allfolder"/>
		<xsl:param name="lab_reader_icon" />
		<xsl:param name="lab_contri_icon" />
		<xsl:param name="lab_owner_icon" />
		<xsl:param name="lab_view_desc" />
		<xsl:param name="tc_enabled"/>
		<xsl:param name="is_show_all"/>
		<xsl:param name="lab_root_training_center"/>
		<xsl:param name="def_tc"/>
		<xsl:param name="lab_cur_tcr"/>
           <!-- simple & advance search -->
		<table>
			<tr>
				<td align="left">
			   		<xsl:if test="$tc_enabled='true'">
						<xsl:variable name="title_var">
							<xsl:choose>
								<xsl:when test="$is_show_all='true'"><xsl:value-of select="$lab_root_training_center"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="$def_tc"/></xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:call-template name="div_tree">
	        				<xsl:with-param name="lab_cur_tcr" select="$lab_cur_tcr"/>
	        				<xsl:with-param name="title" select="$title_var"/>
	        				<xsl:with-param name="lab_root_training_center" select="$lab_root_training_center"/>
	     				</xsl:call-template>
	     				<input type="hidden" name="tc_enabled_ind"/>
     				</xsl:if>
			   </td>
				<td align="right">
					<table>
						<tr>
							<td>
								<input type="text" size="11" name="search" class="inputText vbtm" style="width:130px;" placeholder="{$lab_search_text}"/>
								<input type="image" src="/static/admin/images/wzb-search.png" class="wzb-search vbtm" onclick="seh.simple(document.frmXml, '{$wb_lang}')"/>
								<a href="javascript:seh.adv_prep()" class="wzb-btn wzb-btn-blue ml5 vbtm">
									<xsl:value-of select="$lab_adv_search" />
								</a>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_line"/>
     </xsl:template>
	<!-- 显示全部资源============================================================= -->
	<xsl:template name="show_all">
       <xsl:param name="lab_del_folder"/>
       <xsl:param name="lab_by_ctr_folder"/>
       <xsl:param name="lab_title"/>
       <xsl:param name="lab_child_cata"/>
       <xsl:param name="lab_course_count"/>
       <xsl:param name="lab_tcr_title"/>
       <xsl:param name="lab_edit"/>
       <xsl:param name="lab_no_syb"/>
       <xsl:param name="reader" />
		<xsl:param name="author" />
		<xsl:param name="owner" />
		<xsl:param name="noaccess" />
		<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg table wzb-ui-table">
			<xsl:choose>
				<xsl:when test="$cata_cnt = 0">
					<xsl:call-template name="wb_ui_show_no_item">
						<xsl:with-param name="text" select="$lab_no_syb"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<tr class="SecBg ">
						<td align="left">
							<span class="margin-right5">
								<!-- Access Control -->
								<xsl:call-template name="select_all_checkbox">
									<xsl:with-param name="chkbox_lst_cnt"><xsl:value-of select="$cata_cnt"/></xsl:with-param>
									<xsl:with-param name="display_icon">false</xsl:with-param>
									<xsl:with-param name="chkbox_lst_nm">obj_id</xsl:with-param>
								</xsl:call-template>
							</span>
							<xsl:variable name="_order">
								<xsl:choose>
									<xsl:when test="$cur_sort_col = 'obj_desc' ">
										<xsl:value-of select="$sort_order_by"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$cur_sort_order"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'obj_desc'">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','obj_desc','sort_order','{$_order}','cur_page','1')" class="TitleText">
										<xsl:value-of select="$lab_title"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
											<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','obj_desc','sort_order','asc','cur_page','1')" class="TitleText">
										<xsl:value-of select="$lab_title"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td align="center">
							<span class="TitleText" style="color:#999;">
								<xsl:value-of select="$lab_child_cata"/>
							</span>
						</td>
						<td align="center">
							<span class="TitleText" style="color:#999;">
								<xsl:value-of select="$lab_course_count"/>
							</span>
						</td>
						<!--培训中心-->
						<td align="center">
							<xsl:variable name="_order">
								<xsl:choose>
									<xsl:when test="$cur_sort_col = 'tcr_title' ">
										<xsl:value-of select="$sort_order_by"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$cur_sort_order"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'tcr_title' ">
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','tcr_title','sort_order','{$_order}','cur_page','1')" class="TitleText">
										<xsl:value-of select="$lab_tcr_title"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
											<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_nav_get_urlparam('sort_col','tcr_title','sort_order','asc','cur_page','1')" class="TitleText">
										<xsl:value-of select="$lab_tcr_title"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td>
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:apply-templates select="syllabus/body/node">
					<xsl:with-param name="lab_edit" select="$lab_edit"/>
					<xsl:with-param name="reader" select="$reader" />
					<xsl:with-param name="author" select="$author" />
					<xsl:with-param name="owner" select="$owner" />
					<xsl:with-param name="noaccess" select="$noaccess" />
				</xsl:apply-templates>
			</table>
			<!-- Pagination -->
			<xsl:if test="$cata_cnt != 0">
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="timestamp" select="$timestamp"/>
					<xsl:with-param name="total" select="$total"/>
					<xsl:with-param name="width"><xsl:value-of select="$wb_gen_table_width"/></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
		</xsl:template>
	<!-- ============================================================= -->
	<xsl:template match="node">
		<xsl:param name="lab_edit"/>
		<xsl:param name="reader" />
		<xsl:param name="author" />
		<xsl:param name="owner" />
		<xsl:param name="noaccess" />
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2">RowsEven</xsl:when>
				<xsl:otherwise>RowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!--
		<xsl:variable name="picture">
			<xsl:choose>
				<xsl:when test="@access='READER'">
					<xsl:value-of select="ico_small_reader_folder.gif"></xsl:value-of>
				</xsl:when>
				<xsl:when test="@access='AUTHOR'">
					<xsl:value-of select="ico_small_author_folder.gif"></xsl:value-of>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="ico_small_owner_folder.gif"></xsl:value-of>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		-->
		<tr>
			<td align="left">
				<span class="margin-right5">
					<input type="checkbox" name="obj_id" value="{@id}"/>
					<img src="{$wb_img_path}ico_small_reader_folder.gif" border="0"/>
				</span>
				<xsl:choose>
					<xsl:when test="@access=$noaccess">
						<a class="TitleTextBold" href="javascript:obj.view_right_read('{@id}','{$curr}')">
							<xsl:value-of select="obj_desc"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a class="TitleTextBold color-word" href="javascript:obj.manage_obj_lst(frmXml,'{@id}','','','{$is_show_all}')">
							<xsl:value-of select="obj_desc"/>
						</a>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<span class="Text">
					<xsl:value-of select="@obj_count"/>
				</span>
			</td>
			<td align="center">
				<span class="Text">
					<xsl:value-of select="@count"/>
				</span>
			</td>
			<!--显示培训中心-->
			<td align="center">
				<span class="Text">
					<xsl:value-of select="training_center"/>
				</span>
			</td>
			<td align="right" style="padding-right:0px;">
				<xsl:choose>
					<xsl:when test="@access=$noaccess or @access=$reader ">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</xsl:when>
					<xsl:otherwise>
						<span class="Text">
							<!-- Access Control -->
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_edit"/></xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:obj.upd_prep(<xsl:value-of select="@id"/>,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
							</xsl:call-template>
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="show_shared_folder">
		<xsl:param name="lab_shared_folder"/>
		<xsl:param name="lab_subcategories"/>
		<xsl:param name="lab_item"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_shared_folder"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<xsl:call-template name="wb_ui_space">
			<xsl:with-param name="height">15</xsl:with-param>
		</xsl:call-template>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
			<xsl:call-template name="shared_node">
				<xsl:with-param name="lab_subcategories" select="$lab_subcategories"/>
				<xsl:with-param name="lab_item" select="$lab_item"/>
			</xsl:call-template>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="shared_node">
		<xsl:param name="lab_edit"/>
		<xsl:param name="reader" />
		<xsl:param name="author" />
		<xsl:param name="owner" />
		<xsl:param name="noaccess" />
		<xsl:param name="lab_subcategories"/>
		<xsl:param name="lab_item"/>
		<!-- hide the objective which have the attribute of SYS-->
		<xsl:for-each select="syllabus/shared_folders/node[@type = 'SYB']">
		   <xsl:variable name="access" select="@access" />
			<xsl:if test="not(position() mod 2 = 0) ">
				<xsl:text disable-output-escaping="yes">&lt;tr&gt;</xsl:text>
			</xsl:if>
			<td width="50%" valign="top">
				<table cellpadding="3" cellspacing="0" border="0" width="100%">
					<tr>
						<xsl:choose>
							<xsl:when test="@type = 'SYB'">
								<td width="35" valign="top" align="left">
									<img src="{$wb_img_path}ico_big_folder.gif" border="0"/>
								</td>
								<td>
									<table cellspacing="0" cellpadding="3" border="0">
										<tr>
											<td >
												<!--<xsl:choose>
													<xsl:when test="$access = $noaccess or $access = $lrn_view">
														<a class="TitleTextBold" href="javascript:obj.view_right_read('{@id}','{$curr}')">
															<xsl:value-of select="obj_desc"/>
														</a>
													</xsl:when>
													<xsl:otherwise>-->
														<!--<a class="TitleTextBold" href="javascript:obj.manage_obj_lst(frmXml,'{@id}','','','{$is_show_all}')">
															<xsl:value-of select="obj_desc"/>
														</a>-->
														<a href="javascript:obj.show_obj_lst('{$syb_id}','{@id}',frmXml,'')" class="TitleTextBold color-word">
															<xsl:value-of select="obj_desc"/>
														</a>
													<!--
														<span class="SmallText">
															<xsl:text>&#160;(</xsl:text>
															<xsl:value-of select="@obj_count"/>
															<xsl:text/>
															<xsl:value-of select="$lab_subcategories"/>
															<xsl:text>)</xsl:text>
														</span>
													</xsl:otherwise>
												</xsl:choose>-->
											</td>
										</tr>
										<tr>
											<td>
												<xsl:call-template name="draw_cnt">
													<xsl:with-param name="cnt" select="@count"/>
													<xsl:with-param name="id" select="@id"/>
													<xsl:with-param name="lab_item" select="$lab_item"/>
													<xsl:with-param name="access" select="$access" />
													<xsl:with-param name="noaccess" select="$noaccess" />
												</xsl:call-template>
											</td>
										</tr>
									</table>
								</td>
							</xsl:when>
						</xsl:choose>
					</tr>
				</table>
			</td>
			<xsl:if test="position() = last() and not(position() mod 2 = 0 )">
				<xsl:text disable-output-escaping="yes">&lt;td&gt;&amp;nbsp;&lt;/td&gt;&lt;/tr&gt;</xsl:text>
			</xsl:if>
			<xsl:if test="position() mod 2 = 0">
				<xsl:text disable-output-escaping="yes">&lt;/tr&gt;</xsl:text>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
