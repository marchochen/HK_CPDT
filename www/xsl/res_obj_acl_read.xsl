<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
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
				}
		]]></script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="init()">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_edit_sys_obj">訪問控制 - </xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_for_all_readers">使用“資源管理”的所有用戶</xsl:with-param>
			<xsl:with-param name="lab_reader">讀者</xsl:with-param>
			<xsl:with-param name="lab_reader_desc">擁有讀者權限的用戶可查看文件夾內容。</xsl:with-param>
			<xsl:with-param name="lab_contributor">編者</xsl:with-param>
			<xsl:with-param name="lab_contributor_desc">擁有編者權限的用戶可查看、建立及維護文件夾內容。</xsl:with-param>
			<xsl:with-param name="lab_owner">所有者</xsl:with-param>
			<xsl:with-param name="lab_for_none">無</xsl:with-param>
			<xsl:with-param name="lab_owner_desc">擁有所有者權限的用戶擁有所有權利。還能編輯文件夾的訪問控制權限。</xsl:with-param>
			<xsl:with-param name="lab_tc">培訓中心</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_edit_sys_obj">访问控制 - </xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">后退</xsl:with-param>
			<xsl:with-param name="lab_for_all_readers">使用“资源管理”的所有用户</xsl:with-param>
			<xsl:with-param name="lab_reader">读者</xsl:with-param>
			<xsl:with-param name="lab_reader_desc">拥有读者权限的用户可查看文件夹内容。</xsl:with-param>
			<xsl:with-param name="lab_contributor">编者</xsl:with-param>
			<xsl:with-param name="lab_contributor_desc">拥有编者权限的用户可查看、建立及维护文件夹内容。</xsl:with-param>
			<xsl:with-param name="lab_owner">所有者</xsl:with-param>
			<xsl:with-param name="lab_for_none">无</xsl:with-param>
			<xsl:with-param name="lab_owner_desc">拥有所有者权限的用户拥有所有权利。还能编辑文件夹的访问控制权限。</xsl:with-param>
			<xsl:with-param name="lab_tc">培训中心</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_edit_sys_obj">Access rights - </xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">Back</xsl:with-param>
			<xsl:with-param name="lab_for_none">None</xsl:with-param>
			<xsl:with-param name="lab_for_all_readers">All users using Learning Resource Management</xsl:with-param>
			<xsl:with-param name="lab_reader">Readers</xsl:with-param>
			<xsl:with-param name="lab_reader_desc">A reader can view the subfolders and the resources.</xsl:with-param>
			<xsl:with-param name="lab_contributor">Contributors</xsl:with-param>
			<xsl:with-param name="lab_contributor_desc">A contributor can manage the subfolders and the resources.</xsl:with-param>
			<xsl:with-param name="lab_owner">Owners</xsl:with-param>
			<xsl:with-param name="lab_owner_desc">An owner has all rights of the contributor. In addition, he/she can also manage the folder's access rights.</xsl:with-param>
			<xsl:with-param name="lab_tc">Training center</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_edit_sys_obj"/>
		<xsl:param name="lab_reader"/>
		<xsl:param name="lab_reader_desc"/>
		<xsl:param name="lab_contributor"/>
		<xsl:param name="lab_contributor_desc"/>
		<xsl:param name="lab_owner"/>
		<xsl:param name="lab_for_all_readers"/>
		<xsl:param name="lab_for_all_authors"/>
		<xsl:param name="lab_for_all_owners"/>
		<xsl:param name="lab_for_none"/>
		<xsl:param name="lab_owner_desc"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:param name="lab_tc"/>
		<form name="frmXml">
			<input type="hidden" name="cmd" value="upd_obj_access_control"/>
			<input type="hidden" name="obj_id" value="{objective/@id}"/>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<input type="hidden" name="reader_ent_id_lst" value=""/>
			<input type="hidden" name="author_ent_id_lst" value=""/>
			<input type="hidden" name="owner_ent_id_lst" value=""/>
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text">
					<xsl:value-of select="$lab_edit_sys_obj"/>
					<xsl:value-of select="objective/desc/text()"/>
				</xsl:with-param>
			</xsl:call-template>
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_reader_desc"/><br/><xsl:value-of select="$lab_contributor_desc"/><br/><xsl:value-of select="$lab_owner_desc"/>
					</xsl:with-param>
				</xsl:call-template>
				<!--
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text" select="$lab_reader_desc"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text" select="$lab_contributor_desc"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text" select="$lab_owner_desc"/>
				</xsl:call-template>
				-->
			<xsl:call-template name="wb_ui_line"/>
			<!--For access Control-->
			<xsl:if test="objective/access_control">
			  
			  
				
				<table>
			       <xsl:if test="$tc_enabled='true'">
						<tr>
							<td class="wzb-form-label" valign="top">
								<xsl:value-of select="$lab_tc"/>
								<xsl:text>：</xsl:text>
							</td>
							<td class="wzb-form-control" width="80%">
								<xsl:value-of select="objective/access_control/training_center/title"/>
							</td>
	     				</tr>
     					</xsl:if>
     					<!--Access Control For Reader-->
					<tr>
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$lab_reader"/>
							<xsl:text>：</xsl:text>
						</td>
						<td class="wzb-form-control">
							<xsl:choose>
								<xsl:when test="count(objective/access_control/reader/entity) = 0">
									<xsl:value-of select="$lab_for_none"/>
								</xsl:when>
								<xsl:when test="objective/access_control/reader/entity/@id = 0">
									<xsl:value-of select="$lab_for_all_readers"/>
								</xsl:when>
								<xsl:otherwise>
									<table cellpadding="1" cellspacing="0" border="0" width="100%" class="Bg">
										<xsl:for-each select="objective/access_control/reader/entity">
											<tr>
												<td>
													<xsl:value-of select="@display_bil"/>
												</td>
											</tr>
										</xsl:for-each>
									</table>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
					<!--Access Control For Contributor-->
					<tr>
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$lab_contributor"/>
							<xsl:text>：</xsl:text>
						</td>
						<td class="wzb-form-control">
							<xsl:choose>
								<xsl:when test="count(objective/access_control/author/entity) = 0">
									<xsl:value-of select="$lab_for_none"/>
								</xsl:when>
								<xsl:when test="objective/access_control/author/entity/@id = 0">
									<xsl:value-of select="$lab_for_all_readers"/>
								</xsl:when>
								<xsl:otherwise>
									<table cellspacing="0" cellpadding="1" border="0" width="100%" class="Bg">
										<xsl:for-each select="objective/access_control/author/entity">
											<tr>
												<td>
													<xsl:value-of select="@display_bil"/>
												</td>
											</tr>
										</xsl:for-each>
									</table>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
					<!--Access Control For Owners-->
					<tr>
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$lab_owner"/>
							<xsl:text>：</xsl:text>
						</td>
						<td class="wzb-form-control">
							<xsl:for-each select="objective/access_control/owner/entity">
								<xsl:value-of select="@display_bil"/>
							</xsl:for-each>
						</td>
					</tr>
					<!--Access Control For Learner-->
					<xsl:if test="$nlrn_cm_center_view='true'">
					<tr>
						<td width="100%" colspan="2">
						</td>
					</tr>
					
					</xsl:if>
				</table>
			</xsl:if>
			<!--end for access control-->
			<div class="wzb-bar">
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_back"/>
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
	<!-- ============================================================= -->
	<!-- =============================================================== -->
</xsl:stylesheet>
