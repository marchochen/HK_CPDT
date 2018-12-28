<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="root_ent_id" select="/objective/meta/cur_usr/@root_ent_id"/>
	<xsl:variable name="cur_role" select="/objective/meta/cur_usr/role/@id"/>
	<xsl:variable name="tc_title" select="/objective/access_control/training_center/title"/>
	<xsl:variable name="tc_id" select="/objective/access_control/training_center/@id"/>
	<xsl:variable name="tc_enabled" select="/objective/meta/tc_enabled"/>
	<xsl:variable name="nlrn_cm_center_view" select="/objective/meta/nlrn_cm_center_view"/>
	<!-- label -->
	<xsl:variable name="lab_lrn_view" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '796')"/>
	<xsl:variable name="lab_lrn_view_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '797')"/>
	<xsl:variable name="lab_yes" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_yes')"/>
	<xsl:variable name="lab_no" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_no_1')"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
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
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_objective.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_lrn_soln.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_mgt_rpt.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			goldenman = new wbGoldenMan;
			var obj = new wbObjective;	
			mgt_rpt = new wbManagementReport;
			usr = new wbUserGroup;
			current = 0
			
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
					frm.reader_id_lst.options.length = 0;
					frm.author_id_lst.options.length = 0;
					frm.owner_id_lst.options.length = 0;
				}
				function del_tc() {
					if(!confirm(wb_msg_obj_reset_dependent)) {
						return false;
					}
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
					document.frmXml.usr_group_lst.options[0].text = ']]><xsl:value-of select="$tc_title"/><![CDATA['
					document.frmXml.usr_group_lst_single.value =']]><xsl:value-of select="$tc_title"/><![CDATA['
					document.frmXml.usr_group_lst.options[0].value =']]><xsl:value-of select="$tc_id"/><![CDATA['
				   ]]></xsl:if><![CDATA[
				}
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="init()">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_edit_sys_obj">訪問控制 - </xsl:with-param>
			<xsl:with-param name="lab_inst">文件夾名稱</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_access_right">訪問控制</xsl:with-param>
			<xsl:with-param name="lab_only_learner">指定用戶</xsl:with-param>
			<xsl:with-param name="lab_access_right_desc">訪問控制僅分配給最上層文件夾（如當前文件夾）。指定后， 訪問控制對所有其子文件夾及資源都有效。</xsl:with-param>
			<xsl:with-param name="lab_reader">讀者</xsl:with-param>
			<xsl:with-param name="lab_reader_desc">擁有讀者權限的用戶可查看文件夾內容。</xsl:with-param>
			<xsl:with-param name="lab_contributor">編者</xsl:with-param>
			<xsl:with-param name="lab_contributor_desc">擁有編者權限的用戶可查看、建立及維護文件夾內容。</xsl:with-param>
			<xsl:with-param name="lab_owner">所有者</xsl:with-param>
			<xsl:with-param name="lab_owner_desc">擁有所有者權限的用戶擁有編者的權利。還能編輯文件夾的訪問控制權限。</xsl:with-param>
			<xsl:with-param name="lab_all_learner">使用“資源管理”的所有用戶</xsl:with-param>
			<xsl:with-param name="lab_none">無</xsl:with-param>
			<xsl:with-param name="lab_description">文件夾名稱</xsl:with-param>
			<xsl:with-param name="lab_required">為必填</xsl:with-param>
			<xsl:with-param name="lab_tc">培訓中心</xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">你可以在“我的喜好”中設置“主要培訓中心”</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_edit_sys_obj">访问控制 - </xsl:with-param>
			<xsl:with-param name="lab_inst">文件名</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_access_right">访问控制</xsl:with-param>
			<xsl:with-param name="lab_only_learner">指定用户</xsl:with-param>
			<xsl:with-param name="lab_access_right_desc">访问控制仅分配给顶层文件夹（如当前文件夹）。指定后, 访问控制对所有其子文件夹及资源都有效。</xsl:with-param>
			<xsl:with-param name="lab_reader">读者</xsl:with-param>
			<xsl:with-param name="lab_reader_desc">拥有读者权限的用户可查看文件夹内容。</xsl:with-param>
			<xsl:with-param name="lab_contributor">编者</xsl:with-param>
			<xsl:with-param name="lab_contributor_desc">拥有编者权限的用户可查看、建立及维护文件夹内容。</xsl:with-param>
			<xsl:with-param name="lab_owner">所有者</xsl:with-param>
			<xsl:with-param name="lab_owner_desc">拥有所有者权限的用户拥有编者的权利。还能编辑文件夹的访问控制权限。</xsl:with-param>
			<xsl:with-param name="lab_all_learner">使用“资源管理”的所有用户</xsl:with-param>
			<xsl:with-param name="lab_none">无</xsl:with-param>
			<xsl:with-param name="lab_description">文件名</xsl:with-param>
			<xsl:with-param name="lab_required">为必填</xsl:with-param>
			<xsl:with-param name="lab_tc">培训中心</xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">你可以在“我的喜好”中设置“主要培训中心”</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_edit_sys_obj">Access rights - </xsl:with-param>
			<xsl:with-param name="lab_inst">Folder name</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_access_right">Access rights</xsl:with-param>
			<xsl:with-param name="lab_only_learner">Only these users</xsl:with-param>
			<xsl:with-param name="lab_access_right_desc">Access rights are only assigned in the top-level folder such as this one. Once assigned, these access rights will take effect to all the subfolders and resources of this folder.</xsl:with-param>
			<xsl:with-param name="lab_reader">Readers</xsl:with-param>
			<xsl:with-param name="lab_reader_desc">A reader can view the subfolders and the resources.</xsl:with-param>
			<xsl:with-param name="lab_contributor">Contributors</xsl:with-param>
			<xsl:with-param name="lab_contributor_desc">A contributor can manage the subfolders and the resources.</xsl:with-param>
			<xsl:with-param name="lab_owner">Owners</xsl:with-param>
			<xsl:with-param name="lab_none">None</xsl:with-param>
			<xsl:with-param name="lab_owner_desc">An owner has all rights of the contributor. In addition, he/she can also manage the folder's access rights.</xsl:with-param>
			<xsl:with-param name="lab_all_learner">All users using learning resource management</xsl:with-param>
			<xsl:with-param name="lab_description">Folder name</xsl:with-param>
			<xsl:with-param name="lab_required">Required</xsl:with-param>
			<xsl:with-param name="lab_tc">Training center</xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">Default training center can be set in "my preference" </xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_edit_sys_obj"/>
		<xsl:param name="lab_inst"/>
		<xsl:param name="lab_description"/>
		<xsl:param name="lab_reader"/>
		<xsl:param name="lab_reader_desc"/>
		<xsl:param name="lab_access_right"/>
		<xsl:param name="lab_access_right_desc"/>
		<xsl:param name="lab_all_learner"/>
		<xsl:param name="lab_contributor"/>
		<xsl:param name="lab_contributor_desc"/>
		<xsl:param name="lab_none"/>
		<xsl:param name="lab_owner"/>
		<xsl:param name="lab_owner_desc"/>
		<xsl:param name="lab_required"/>
		<xsl:param name="lab_only_learner"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_tc"/>
		<xsl:param name="lab_def_tc_desc"/>
		<form name="frmXml">
			<input type="hidden" name="cmd" value="upd_obj_access_control"/>
			<input type="hidden" name="obj_id" value="{objective/@id}"/>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<input type="hidden" name="reader_ent_id_lst" value=""/>
			<input type="hidden" name="author_ent_id_lst" value=""/>
			<input type="hidden" name="owner_ent_id_lst" value=""/>
			<input type="hidden" name="obj_tcr_id" value=""/>
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
				<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
				
			</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text">
					<xsl:value-of select="$lab_edit_sys_obj"/>
					<xsl:value-of select="objective/desc/text()"/>
				</xsl:with-param>
			</xsl:call-template>
			<!--For access Control-->
			<xsl:if test="objective/access_control">
				<!--Access Control For Reader    width="{$wb_gen_table_width}-->
				<table>
					<xsl:if test="$tc_enabled='true'">
					<tr>
						<td class="wzb-form-label">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_tc"/>:
							
						</td>
						<td class="wzb-form-control">
							<xsl:choose>
								<xsl:when test="$cur_role ='INSTR_1'"><xsl:value-of select="$tc_title"/><input type="hidden" name="usr_group_lst_single" value="{$tc_title}"/></xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="wb_goldenman">
										<xsl:with-param name="frm">document.frmXml</xsl:with-param>
										<xsl:with-param name="name">usr_group_lst</xsl:with-param>
										<xsl:with-param name="box_size">1</xsl:with-param>
										<xsl:with-param name="field_name">usr_group_lst</xsl:with-param>
										<xsl:with-param name="tree_type">training_center</xsl:with-param>
										<xsl:with-param name="select_type">2</xsl:with-param>
										<xsl:with-param name="pick_leave">1</xsl:with-param>
										<xsl:with-param name="pick_root">0</xsl:with-param>
										<xsl:with-param name="confirm_function">resetForTcAlter</xsl:with-param>
										<xsl:with-param name="confirm_msg">confirm_msg</xsl:with-param>
										<xsl:with-param name="remove_function">del_tc()</xsl:with-param>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>	
					</xsl:if>
					<tr>
						<td class="wzb-form-label" valign="top">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_reader"/>:
						</td>
						<td class="wzb-form-control">
							<table>
								<tr>
									<td valign="top" align="left" width="30%">
										<input type="radio" name="user_sel_all_usr" value="-1" id="select_none_readers" onclick="usr_change                               		(document.frmXml,this)">
											<xsl:if test="count(objective/access_control/reader/entity) = 0">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</input>
										<label for="select_none_readers">
											<xsl:value-of select="$lab_none"/>
										</label>
									</td>
									<td valign="top" width="70%" align="left">
										<xsl:value-of select="$lab_reader_desc"/>
									</td>
								</tr>
								<tr>
									<td valign="top" align="left" width="5%">
										<input type="radio" name="user_sel_all_usr" value="1" id="all_learner" onclick="usr_change	(document.frmXml,this)">
											<xsl:if test="objective/access_control/reader/entity/@id =0">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</input>
										<label for="all_learner">
											<xsl:value-of select="$lab_all_learner"/>
										</label>
									</td>
								</tr>
								<tr>
									<td valign="top" align="left">
										<input type="radio" name="user_sel_all_usr" value="0" id="all_sel_learner" onclick="usr_change	(document.frmXml,this)">
											<xsl:if test="objective/access_control/reader/entity/@id !=0">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</input>
										<label for="all_sel_learner">
											<xsl:value-of select="$lab_only_learner"/>:
										</label>
									</td>
								</tr>
								<tr>
									<td>
										<xsl:call-template name="wb_goldenman">
											<xsl:with-param name="frm">document.frmXml</xsl:with-param>
											<xsl:with-param name="width">320</xsl:with-param>
											<xsl:with-param name="field_name">reader_id_lst</xsl:with-param>
											<xsl:with-param name="name">reader_ent_id_lst</xsl:with-param>
											<xsl:with-param name="tree_type">user_group_and_user</xsl:with-param>
											<xsl:with-param name="select_type">4</xsl:with-param>
											<xsl:with-param name="box_size">5</xsl:with-param>
											<xsl:with-param name="pick_leave">0</xsl:with-param>
											<xsl:with-param name="option_list">
												<xsl:apply-templates select="objective/access_control/reader/entity"/>
											</xsl:with-param>
											<xsl:with-param name="search">true</xsl:with-param>
											<xsl:with-param name="ftn_ext_id">FTN_AMD_RES_MAIN</xsl:with-param>

											<xsl:with-param name="search_function">
												<xsl:choose>
													<xsl:when test="$tc_enabled='true'">goldenman.openitemaccsearchwin(<xsl:value-of select="$root_ent_id"/>,'TADM_<xsl:value-of select="$root_ent_id"/>~INSTR_<xsl:value-of select="$root_ent_id"/><xsl:value-of select="objective/role/text()"/>','reader_id_lst','<xsl:value-of select="$wb_lang"/>','5',document.frmXml.usr_group_lst.options[0].value,'' )</xsl:when>
											   		<xsl:otherwise>javascript:usr.search.popup_search_prep('reader_id_lst','','1', '0', '', '', '0', '0','','','FTN_AMD_RES_MAIN') </xsl:otherwise>
												</xsl:choose>
											</xsl:with-param>		
											<xsl:with-param name="add_function">
											    <xsl:choose>
													<xsl:when test="$tc_enabled='true'">goldenman.openitemaccwin(<xsl:value-of select="$root_ent_id"/>,'TADM_<xsl:value-of select="$root_ent_id"/>~INSTR_<xsl:value-of select="$root_ent_id"/><xsl:value-of select="objective/role/text()"/>','reader_id_lst','5','<xsl:value-of select="$wb_lang"/>',document.frmXml.usr_group_lst.options[0].value,'')</xsl:when>
													<xsl:otherwise>goldenman.opentree('user_group_and_user',4,'reader_id_lst','','0','','','1','1', '0', '0', '0','1', '0', 'FTN_AMD_RES_MAIN')</xsl:otherwise>
												</xsl:choose>				
											</xsl:with-param>

										</xsl:call-template>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label" valign="top">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_contributor"/>:
						</td>
						<td class="wzb-form-control" align="center">
							<table>
								<tr>
									<td valign="top" align="left">
										<input type="radio" name="user_sel_all_author" value="-1" id="select_none_author" onclick="author_change(document.frmXml,this)">
											<xsl:if test="count(objective/access_control/author/entity) = 0">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</input>
										<label for="select_none_author">
											<xsl:value-of select="$lab_none"/>
										</label>
									</td>
									<td valign="top" align="left" width="70%">
										<xsl:value-of select="$lab_contributor_desc"/>
									</td>
								</tr>
								<tr>
									<td valign="top" align="left" width="5%">
										<input type="radio" name="user_sel_all_author" value="1" id="all_author" onclick="author_change	(document.frmXml,this)">
											<xsl:if test="objective/access_control/author/entity/@id =0">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</input>
										<label for="all_author">
											<xsl:value-of select="$lab_all_learner"/>
										</label>
									</td>
								</tr>
								<tr>
									<td valign="top" align="left">
										<input type="radio" name="user_sel_all_author" value="0" id="all_sel_author" onclick="author_change	(document.frmXml,this)">
											<xsl:if test="objective/access_control/author/entity/@id != 0">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</input>
										<label for="all_sel_author">
											<xsl:value-of select="$lab_only_learner"/>:
										</label>
									</td>
								</tr>
								<tr>
									<td>
										<xsl:call-template name="wb_goldenman">
											<xsl:with-param name="frm">document.frmXml</xsl:with-param>
											<xsl:with-param name="width">320</xsl:with-param>
											<xsl:with-param name="field_name">author_id_lst</xsl:with-param>
											<xsl:with-param name="name">author_ent_id_lst</xsl:with-param>
											<xsl:with-param name="tree_type">user_group_and_user</xsl:with-param>
											<xsl:with-param name="select_type">4</xsl:with-param>
											<xsl:with-param name="box_size">5</xsl:with-param>
											<xsl:with-param name="pick_leave">0</xsl:with-param>
											<xsl:with-param name="option_list">
												<xsl:apply-templates select="objective/access_control/author/entity"/>
											</xsl:with-param>
											<xsl:with-param name="search">true</xsl:with-param>
											<xsl:with-param name="ftn_ext_id">FTN_AMD_RES_MAIN</xsl:with-param>

											<xsl:with-param name="search_function">
												<xsl:choose>
													<xsl:when test="$tc_enabled='true'">goldenman.openitemaccsearchwin(<xsl:value-of select="$root_ent_id"/>,'TADM_<xsl:value-of select="$root_ent_id"/>~INSTR_<xsl:value-of select="$root_ent_id"/><xsl:value-of select="objective/role/text()"/>','author_id_lst','<xsl:value-of select="$wb_lang"/>','5',document.frmXml.usr_group_lst.options[0].value,'' )</xsl:when>
											   		<xsl:otherwise>javascript:usr.search.popup_search_prep('author_id_lst','','1', '0', '', '', '0', '0','','','FTN_AMD_RES_MAIN') </xsl:otherwise>
												</xsl:choose>
											</xsl:with-param>		
											<xsl:with-param name="add_function">
											    <xsl:choose>
													<xsl:when test="$tc_enabled='true'">goldenman.openitemaccwin(<xsl:value-of select="$root_ent_id"/>,'TADM_<xsl:value-of select="$root_ent_id"/>~INSTR_<xsl:value-of select="$root_ent_id"/><xsl:value-of select="objective/role/text()"/>','author_id_lst','5','<xsl:value-of select="$wb_lang"/>',document.frmXml.usr_group_lst.options[0].value,'')</xsl:when>
													<xsl:otherwise>goldenman.opentree('user_group_and_user',4,'author_id_lst','','0','','','1','1', '0', '0', '0','1', '0', 'FTN_AMD_RES_MAIN')</xsl:otherwise>
												</xsl:choose>				
											</xsl:with-param>

										</xsl:call-template>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<!--Access Control For Owners-->
					<tr>
						<td class="wzb-form-label" valign="top">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_owner"/>:
						</td>
						<td class="wzb-form-control" valign="top" align="left">
							<table>
								<tr>
									<td align="left" width="30%">
										<xsl:call-template name="wb_goldenman">
											<xsl:with-param name="frm">document.frmXml</xsl:with-param>
											<xsl:with-param name="width">320</xsl:with-param>
											<xsl:with-param name="field_name">owner_id_lst</xsl:with-param>
											<xsl:with-param name="tree_type">user_group_and_user</xsl:with-param>
											<xsl:with-param name="select_type">4</xsl:with-param>
											<xsl:with-param name="box_size">5</xsl:with-param>
											<xsl:with-param name="pick_leave">0</xsl:with-param>
											<xsl:with-param name="pick_root">0</xsl:with-param>
											<xsl:with-param name="label_add_btn">
												<xsl:value-of select="$lab_gen_select"/>
											</xsl:with-param>
											<xsl:with-param name="search">true</xsl:with-param>
											<xsl:with-param name="option_list">
												<xsl:apply-templates select="objective/access_control/owner/entity"/>
											</xsl:with-param>
											<xsl:with-param name="ftn_ext_id">FTN_AMD_RES_MAIN</xsl:with-param>
											<xsl:with-param name="search_function">
												<xsl:choose>
													<xsl:when test="$tc_enabled='true'">goldenman.openitemaccsearchwin(<xsl:value-of select="$root_ent_id"/>,'TADM_<xsl:value-of select="$root_ent_id"/>~INSTR_<xsl:value-of select="$root_ent_id"/><xsl:value-of select="objective/role/text()"/>','owner_id_lst','<xsl:value-of select="$wb_lang"/>','5',document.frmXml.usr_group_lst.options[0].value,'' )</xsl:when>
											   		<xsl:otherwise>javascript:usr.search.popup_search_prep('owner_id_lst','','1', '0', '', '', '0', '0','','','FTN_AMD_RES_MAIN') </xsl:otherwise>
												</xsl:choose>
											</xsl:with-param>		
											<xsl:with-param name="add_function">
											    <xsl:choose>
													<xsl:when test="$tc_enabled='true'">goldenman.openitemaccwin(<xsl:value-of select="$root_ent_id"/>,'TADM_<xsl:value-of select="$root_ent_id"/>~INSTR_<xsl:value-of select="$root_ent_id"/><xsl:value-of select="objective/role/text()"/>','owner_id_lst','5','<xsl:value-of select="$wb_lang"/>',document.frmXml.usr_group_lst.options[0].value,'')</xsl:when>
													<xsl:otherwise>goldenman.opentree('user_group_and_user',4,'owner_id_lst','','0','','','1','1', '0', '0', '0','1', '0', 'FTN_AMD_RES_MAIN')</xsl:otherwise>
												</xsl:choose>				
											</xsl:with-param>

										</xsl:call-template>
									</td>
									<td valign="top" width="70%" align="left">
										<xsl:value-of select="$lab_owner_desc"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<!-- 
					<xsl:if test="$nlrn_cm_center_view='true'">
						<tr>
							<td width="20%" align="right" height="10" valign="top">
								<span class="TitleText">
									<xsl:value-of select="$lab_lrn_view"/>
									<xsl:text>：</xsl:text>
								</span>
							</td>
							<td width="40%" align="left" height="10" valign="top">
								<span class="Text">
									<input type="radio" name="lrn_view_ind" id="id_view_ind_yes" value="true">
										<xsl:if test="/objective/access_control/lrn_view_ind/text() = 'true'">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<label for="id_view_ind_yes">
										<span class="Text">
											<xsl:value-of select="$lab_yes"/>
										</span>
									</label>
									<br/>
									<input type="radio" name="lrn_view_ind" id="id_view_ind_no" value="false">
										<xsl:if test="not(/objective/access_control/lrn_view_ind)">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<label for="id_view_ind_no">
										<span class="Text">
											<xsl:value-of select="$lab_no"/>
										</span>
									</label>
								</span>
							</td>
							<td width="40%" valign="top" align="left">
								<span class="Text">
									<xsl:value-of select="$lab_lrn_view_desc"/>
								</span>
							</td>
						</tr>
					</xsl:if>
					 -->
					<tr>
						<td>&#160;</td>
						<td align="left">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_required"/>
						</td>
					</tr>
				</table>
			</xsl:if>
			<!--end for access control-->
			<div class="wzb-bar">
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:obj.upd_acl_exec(document.frmXml)</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
				</xsl:call-template>
			</div>
		</form>
	</xsl:template>
	<xsl:template match="cur_usr"/>
	<!-- ============================================================= -->
	<xsl:template match="entity">
		<option value="{@id}">
			<xsl:value-of select="@display_bil"/>
		</option>
	</xsl:template>
	<!-- ===================================================================== -->
</xsl:stylesheet>
