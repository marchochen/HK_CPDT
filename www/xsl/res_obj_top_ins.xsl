<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<!-- customize utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- ============================================================= -->
	<xsl:variable name="obj_id" select="/objective_list/objective/@id"/>
	<xsl:variable name="obj_parent_id">
		<xsl:choose>
			<xsl:when test="count(/objective_list/header/node) = 0">0</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="/objective_list/header/node[position() = last()]/@id"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="tc_enabled" select="/objective_list/meta/tc_enabled"/>
	<xsl:variable name="nlrn_cm_center_view" select="/objective_list/meta/nlrn_cm_center_view"/>
	<xsl:variable name="syl_id" select="/objective_list/objective/syllabus/@id"/>
	<xsl:variable name="root_ent_id" select="/objective_list/meta/cur_usr/@root_ent_id"/>
	<xsl:variable name="def_tc_title" select="/objective_list/default_training_center/title"/>
	<xsl:variable name="def_tc_id" select="/objective_list/default_training_center/@id"/>
	<xsl:variable name="owner_ent_ids" select="/objective_list/objective/access_control/owner/entity/@id"/>
	<!-- label -->
	<xsl:variable name="lab_lrn_view" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '796')"/>
	<xsl:variable name="lab_lrn_view_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '797')"/>
	<xsl:variable name="lab_yes" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_yes')"/>
	<xsl:variable name="lab_no" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_no_1')"/>
	<xsl:output indent="yes"/>
	<!-- ============================================================= -->
	<xsl:template match="/">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_lrn_soln.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_mgt_rpt.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_objective.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script language="JavaScript"><![CDATA[
			goldenman = new wbGoldenMan;
			usr = new wbUserGroup;
			current = 0
			show_all = getUrl('show_all')?getUrlParam('show_all'):'';
			
			function getPopupUsrLst(fld_name,id_lst,nm_lst, usr_argv){
				if(fld_name == 'reader_id_lst') {					
					reader_id_lst(usr_argv);
				}
				if(fld_name == 'author_id_lst' ){
					author_id_lst(usr_argv);
				}	
				if(fld_name == 'owner_id_lst' ){
					owner_id_lst(usr_argv);
				}					
			}
			

			function reader_id_lst() {
				var args = reader_id_lst.arguments
				AddTreeOption(document.frmXml.reader_id_lst,0,args,'col')
			}
			function author_id_lst() {
				var args = author_id_lst.arguments
				AddTreeOption(document.frmXml.author_id_lst,0,args,'col')
			}
			function owner_id_lst() {
				var args = owner_id_lst.arguments
				AddTreeOption(document.frmXml.owner_id_lst,0,args,'col')
			}
			
			
			function usr_change(frm, obj){
					if(obj.value == 0){
						var pos = false;
						var neg = true;
					}else{
						var pos =true;
						var neg = false;
					}
					if(frm.reader_id_lst.type == 'select-multiple'){
						frm.reader_id_lst.disabled = pos;
						if(frm.genaddreader_ent_id_lst){
							frm.genaddreader_ent_id_lst.disabled = pos;
						}
						if(frm.genremovereader_ent_id_lst){
							frm.genremovereader_ent_id_lst.disabled = pos;
						}		
						if(frm.gensearchreader_ent_id_lst){
							frm.gensearchreader_ent_id_lst.disabled = pos;
						}		
						if(pos == true){
							frm.reader_id_lst.options.length = 0
						}
					}

			}
			function author_change(frm,obj){
					if(obj.value == 0){
						var pos =false;
						var neg = true;
					}else{
						var pos = true;
						var neg = false;					
					}
					if(frm.author_id_lst.type == 'select-multiple'){
						 frm.author_id_lst.disabled = pos;
						if(frm.genaddauthor_ent_id_lst){
							frm.genaddauthor_ent_id_lst.disabled = pos;
						}
						if(frm.genremoveauthor_ent_id_lst){
							frm.genremoveauthor_ent_id_lst.disabled = pos;
						}		
						if(frm.gensearchauthor_ent_id_lst){
							frm.gensearchauthor_ent_id_lst.disabled = pos;
						}		
						if(pos == true){
							frm.author_id_lst.options.length = 0
						}
					}
				}
				]]><xsl:if test="$tc_enabled='true'"><![CDATA[
				
				//confirm_msg & confirm_function for wb_goldenman(tree_type = training_center)
				var confirm_msg = wb_msg_obj_reset_dependent;
				function resetForTcAlter(){
					if (!frm.reader_id_lst && typeof(frm.reader_id_lst)!="undefined"){
					    frm.reader_id_lst.options.length = 0;
					}　
					if (!frm.author_id_lst && typeof(frm.author_id_lst)!="undefined"){
					    frm.author_id_lst.options.length = 0;
					}
					if (!frm.owner_id_lst && typeof(frm.owner_id_lst)!="undefined"){
					    frm.owner_id_lst.options.length = 0;
					}
				}
				function del_tc() {
					/*
					if(!confirm(wb_msg_obj_reset_dependent)) {
						return false;
					}
					*/
					resetForTcAlter();
					RemoveSingleOption(document.frmXml.usr_group_lst_single,document.frmXml.usr_group_lst);
				}
				]]></xsl:if><![CDATA[
				
				function init(){
					frm = document.frmXml
					if(frm.user_sel_all_usr && frm.user_sel_all_usr[0].checked){
						usr_change(frm,frm.user_sel_all_usr[0])
					}else if(frm.user_sel_all_usr && frm.user_sel_all_usr[1].checked){
						usr_change(frm,frm.user_sel_all_usr[1])
					}
					if(frm.user_sel_all_author && frm.user_sel_all_author[0].checked){
						author_change(frm,frm.user_sel_all_author[0])
					}else if(frm.user_sel_all_author && frm.user_sel_all_author[1].checked){
						author_change(frm,frm.user_sel_all_author[1])
					}
					]]><xsl:if test="$tc_enabled='true'"><![CDATA[
					document.frmXml.usr_group_lst = new wbSingleGoldenMan
					document.frmXml.usr_group_lst.options[0].text = ']]><xsl:value-of select="$def_tc_title"/><![CDATA['
					document.frmXml.usr_group_lst_single.value =']]><xsl:value-of select="$def_tc_title"/><![CDATA['
					document.frmXml.usr_group_lst.options[0].value =']]><xsl:value-of select="$def_tc_id"/><![CDATA['
					]]></xsl:if><![CDATA[
				}
				obj = new wbObjective
		
			function status(){
				return false
			}

		]]></script>
			</head>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_add_sys_obj">建立資源文件夾</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_access_right">訪問控制</xsl:with-param>
			<xsl:with-param name="lab_only_learner">指定用戶</xsl:with-param>
			<xsl:with-param name="lab_access_right_desc">訪問控制僅分配給最上層文件夾（如當前文件夾）。指定后, 訪問控制對所有其子文件夾及資源都有效。</xsl:with-param>
			<xsl:with-param name="lab_reader">讀者</xsl:with-param>
			<xsl:with-param name="lab_reader_desc">擁有讀者權限的用戶可查看文件夾內容.</xsl:with-param>
			<xsl:with-param name="lab_contributor">編者</xsl:with-param>
			<xsl:with-param name="lab_contributor_desc">擁有編者權限的用戶可查看、建立及維護文件夾內容。</xsl:with-param>
			<xsl:with-param name="lab_owner">所有者</xsl:with-param>
			<xsl:with-param name="lab_owner_desc">擁有所有者權限的用戶擁有編者的權利。還能編輯文件夾的訪問控制權限。</xsl:with-param>
			<xsl:with-param name="lab_all_learner">使用“資源管理”的所有用戶</xsl:with-param>
			<xsl:with-param name="lab_none">無</xsl:with-param>
			<xsl:with-param name="lab_description">文件夾名稱</xsl:with-param>
			<xsl:with-param name="lab_required">為必填</xsl:with-param>
			<xsl:with-param name="lab_tc">培訓中心</xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">你可以在“我的喜好”中設置“主要培訓中心”；設置後，在任何指定培訓中心的操作中，將默認的指定爲設定的主要培訓中心</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_add_sys_obj">建立资源文件夹</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_access_right">访问控制</xsl:with-param>
			<xsl:with-param name="lab_only_learner">指定用户</xsl:with-param>
			<xsl:with-param name="lab_access_right_desc">访问控制仅分配给顶层文件夹（如当前文件夹）。指定后, 访问控制对所有其子文件及资源都有效。</xsl:with-param>
			<xsl:with-param name="lab_reader">读者</xsl:with-param>
			<xsl:with-param name="lab_reader_desc">拥有读者权限的用户可查看文件夹内容.</xsl:with-param>
			<xsl:with-param name="lab_contributor">编者</xsl:with-param>
			<xsl:with-param name="lab_contributor_desc">拥有编者权限的用户可查看、建立及维护文件夹内容。</xsl:with-param>
			<xsl:with-param name="lab_owner">所有者</xsl:with-param>
			<xsl:with-param name="lab_owner_desc">拥有所有者权限的用户拥有编者的权利。还能编辑文件夹的访问控制权限。</xsl:with-param>
			<xsl:with-param name="lab_all_learner">使用“资源管理”的所有用户</xsl:with-param>
			<xsl:with-param name="lab_none">无</xsl:with-param>
			<xsl:with-param name="lab_description">文件夹名称</xsl:with-param>
			<xsl:with-param name="lab_required">为必填</xsl:with-param>
			<xsl:with-param name="lab_tc">培训中心</xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">你可以在“我的喜好”中设置“主要培训中心”；设置后，在任何指定培训中心的操作中，将默认的指定为设定的主要培训中心</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_add_sys_obj">Create resource folder</xsl:with-param>
			<xsl:with-param name="lab_access_right">Access rights</xsl:with-param>
			<xsl:with-param name="lab_only_learner">Only these users</xsl:with-param>
			<xsl:with-param name="lab_access_right_desc">Access rights are only assigned in the top-level folder such as this one. Once assigned, these access rights will take effect to all the subfolders and resources of this folder.</xsl:with-param>
			<xsl:with-param name="lab_reader">Readers</xsl:with-param>
			<xsl:with-param name="lab_reader_desc">A reader can view the subfolders and the resources.</xsl:with-param>
			<xsl:with-param name="lab_contributor">Contributors</xsl:with-param>
			<xsl:with-param name="lab_none">None</xsl:with-param>
			<xsl:with-param name="lab_contributor_desc">A contributor can manage the subfolders and the resources.</xsl:with-param>
			<xsl:with-param name="lab_owner">Owners</xsl:with-param>
			<xsl:with-param name="lab_owner_desc">An owner has all rights of the contributor. In addition, he/she can also manage the folder's access rights.</xsl:with-param>
			<xsl:with-param name="lab_all_learner">All users using learning resource management</xsl:with-param>
			<xsl:with-param name="lab_description">Folder name</xsl:with-param>
			<xsl:with-param name="lab_required">Required</xsl:with-param>
			<xsl:with-param name="lab_tc">Training center</xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">You can specify “Major Training Center” in “My Preference”. After that, your major training center will be default selected wherever a selection of training center is needed.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="content">
		<xsl:param name="lab_add_sys_obj"/>
		<xsl:param name="lab_description"/>
		<xsl:param name="lab_reader"/>
		<xsl:param name="lab_reader_desc"/>
		<xsl:param name="lab_access_right"/>
		<xsl:param name="lab_access_right_desc"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_all_learner"/>
		<xsl:param name="lab_contributor"/>
		<xsl:param name="lab_none"/>
		<xsl:param name="lab_contributor_desc"/>
		<xsl:param name="lab_owner"/>
		<xsl:param name="lab_owner_desc"/>
		<xsl:param name="lab_required"/>
		<xsl:param name="lab_only_learner"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_tc"/>
		<xsl:param name="lab_def_tc_desc"/>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="init();wb_utils_gen_form_focus(document.frmXml)">
			<form name="frmXml" onSubmit="return status()">
				<input type="hidden" name="cmd" value="ins_obj"/>
				<input type="hidden" name="obj_id_parent" value="{$obj_id}"/>
				<input type="hidden" name="syl_id" value="{$syl_id}"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="reader_ent_id_lst" value="-1"/>
				<input type="hidden" name="author_ent_id_lst" value="-1"/>
				<input type="hidden" name="owner_ent_id_lst" value=""/>
				<input type="hidden" name="obj_tcr_id" value=""/>
				<input type="hidden" name="owner_id_lst" value="{$owner_ent_ids}"/>
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
					<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
					<xsl:with-param name="page_title" select="$lab_add_sys_obj"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_add_sys_obj"/>
				</xsl:call-template>
				<table>
					<tr>
						<td class="wzb-form-label">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_description"/>：
						</td>
						<td class="wzb-form-control">
							<input type="text" name="obj_desc" size="27" maxlength="255" style="width:350px;" class="wzb-inputText"/>
						</td>
					</tr>
					<input type="hidden" name="obj_type" value="SYB"/>
					<xsl:if test="$tc_enabled='true'">
						<tr>
							<td class="wzb-form-label">
								<span class="wzb-form-star">*</span><xsl:value-of select="$lab_tc"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:call-template name="wb_goldenman">
									<xsl:with-param name="frm">document.frmXml</xsl:with-param>
									<xsl:with-param name="name">usr_group_lst</xsl:with-param>
									<xsl:with-param name="box_size">1</xsl:with-param>
									<xsl:with-param name="field_name">usr_group_lst</xsl:with-param>
									<xsl:with-param name="tree_type">training_center</xsl:with-param>
									<xsl:with-param name="select_type">2</xsl:with-param>
									<xsl:with-param name="pick_leave">1</xsl:with-param>
									<xsl:with-param name="pick_root">0</xsl:with-param>
									<!-- 
									<xsl:with-param name="confirm_function">resetForTcAlter</xsl:with-param>
									<xsl:with-param name="confirm_msg">confirm_msg</xsl:with-param>
									 -->
									<xsl:with-param name="remove_function">del_tc()</xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
						<tr>
							<td width="20%" align="right"></td>
							<td width="65%" align="left">
								<span class="wzb-form-star">*</span><xsl:value-of select="$lab_required"/>
							</td>
						</tr>	
					</xsl:if>
				</table>
				<!--  
				<xsl:call-template name="wb_ui_space"/>
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text" select="$lab_access_right"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_line"/>
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_access_right_desc"/>
					</xsl:with-param>
				</xsl:call-template>-->
				<!--Reader-->
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:obj.ins_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="/objective_list/folders/text()"/>', show_all)</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
					</xsl:call-template>
				</div>
				<xsl:call-template name="wb_ui_footer"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template match="entity">
		<option value="{@id}">
			<xsl:value-of select="@display_bil"/>
		</option>
	</xsl:template>
	<!--===================================================================== -->
</xsl:stylesheet>
