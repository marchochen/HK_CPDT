<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="tc_enabled" select="/studygroup/meta/tc_enabled"/>
	<xsl:variable name="ins_or_upd">
		<xsl:choose>
			<xsl:when test="/studygroup/sgp/@id &gt;= 1">UPD</xsl:when>
			<xsl:otherwise>INS</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="old_tcr_id" select="/studygroup/sgp/@tcr_id"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="studygroup"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="studygroup">
		<xsl:apply-templates select="sgp"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="sgp">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_study_group.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				studygroup = new wbStudyGroup;
				var goldenman = new wbGoldenMan;
				//confirm_msg & confirm_function for wb_goldenman(tree_type = training_center)
				var confirm_msg = wb_sgp_reset_manager;
				function resetForTcAlter(){
					frmXml.manager_lst.options.length = 0;
				}
				function del_tc() {
					if(!confirm(wb_sgp_reset_manager)) {
						return false;
					}
					resetForTcAlter();
					RemoveSingleOption(document.frmXml.tcr_id_lst_single,document.frmXml.tcr_id_lst);
				}
				function openManagerTreeTcEnabled(tcr_id){
					if(tcr_id === undefined || tcr_id === null || tcr_id === ''){
						alert(wb_msg_tc);
					} else {
						openManagerTree(tcr_id)
					}
				}
				function openManagerTree(tcr_id){
					goldenman.opentree('user_group_and_user',4,'manager_lst','','0','','','1','0', '0', '0', '0','1', '0', '','',tcr_id, 1)
				}
			]]></SCRIPT>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml" method="" action="">
				<xsl:call-template name="wb_init_lab"/>
				<input name="module" type="hidden"/>
				<input name="cmd" type="hidden">
					<xsl:attribute name="value">
						<xsl:choose>
							<xsl:when test="$ins_or_upd = 'INS'">ins_sgp</xsl:when>
							<xsl:otherwise>upd_sgp</xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>
				</input>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="sgp_id" value="{@id}"/>
				<input name="sgp_mgt_str" type="hidden"/>
				<input name="tcr_id" type="hidden"/>
				<input name="sgp_upd_timestamp" type="hidden" value="{@upd_timestamp}"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_study_group_upd">修改學習小組</xsl:with-param>
			<xsl:with-param name="lab_study_group_ins">新增學習小組</xsl:with-param>
			<xsl:with-param name="lab_title">小組名稱</xsl:with-param>
			<xsl:with-param name="lab_tcr_title">培訓中心</xsl:with-param>
			<xsl:with-param name="lab_desc">小組描述</xsl:with-param>
			<xsl:with-param name="lab_leader">組長</xsl:with-param>
			<xsl:with-param name="lab_pubic_type">公開設定</xsl:with-param>
			<xsl:with-param name="lab_pubic_type_desc_close">不公開 - 所有人暫時不能查看小組內容</xsl:with-param>
			<xsl:with-param name="lab_pubic_type_desc_mem">成員 - 只有成員可以查看小組內容</xsl:with-param>
			<xsl:with-param name="lab_pubic_type_desc_all">所有人 - 所有學員可以查看小組內容</xsl:with-param>
			<xsl:with-param name="lab_send_email_ind">給新成員發送郵件通知</xsl:with-param>
			<xsl:with-param name="lab_send_email_no">否</xsl:with-param>
			<xsl:with-param name="lab_send_email_yes">是</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">你可以在“我的喜好”中設置“主要培訓中心”<br/>設置後，在任何指定培訓中心的操作中，將默認的指定爲設定的主要培訓中心</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_study_group_upd">修改学习小组</xsl:with-param>
			<xsl:with-param name="lab_study_group_ins">创建学习小组</xsl:with-param>
			<xsl:with-param name="lab_title">小组名称</xsl:with-param>
			<xsl:with-param name="lab_tcr_title">培训中心</xsl:with-param>
			<xsl:with-param name="lab_desc">小组描述</xsl:with-param>
			<xsl:with-param name="lab_leader">组长</xsl:with-param>
			<xsl:with-param name="lab_pubic_type">公开设定</xsl:with-param>
			<xsl:with-param name="lab_pubic_type_desc_close">不公开 - 所有人暂时不能查看小组内容</xsl:with-param>
			<xsl:with-param name="lab_pubic_type_desc_mem">成员 - 只有成员可以查看小组内容</xsl:with-param>
			<xsl:with-param name="lab_pubic_type_desc_all">所有人 - 所有学员可以查看小组内容</xsl:with-param>
			<xsl:with-param name="lab_send_email_ind">给新成员发送邮件通知</xsl:with-param>
			<xsl:with-param name="lab_send_email_no">否</xsl:with-param>
			<xsl:with-param name="lab_send_email_yes">是</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">你可以在“我的喜好”中设置“主要培训中心”<br/>设置后，在任何指定培训中心的操作中，将默认的指定为设定的主要培训中心</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_study_group_upd">Edit study group</xsl:with-param>
			<xsl:with-param name="lab_study_group_ins">Add study group</xsl:with-param>
			<xsl:with-param name="lab_title">Group title</xsl:with-param>
			<xsl:with-param name="lab_tcr_title">Training center</xsl:with-param>
			<xsl:with-param name="lab_desc">Group description</xsl:with-param>
			<xsl:with-param name="lab_leader">Group leader</xsl:with-param>
			<xsl:with-param name="lab_pubic_type">Publicity</xsl:with-param>
			<xsl:with-param name="lab_pubic_type_desc_close">Close - no learner can participate in the study group.</xsl:with-param>
			<xsl:with-param name="lab_pubic_type_desc_mem">Member - only members can participate in the study group.</xsl:with-param>
			<xsl:with-param name="lab_pubic_type_desc_all">ALL - All learner can participate in the study group.</xsl:with-param>
			<xsl:with-param name="lab_send_email_ind">Email to new members</xsl:with-param>
			<xsl:with-param name="lab_send_email_no">No</xsl:with-param>
			<xsl:with-param name="lab_send_email_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">You can specify “Major Training Center” in “My Preference”. <br/>After that, your major training center will be default selected wherever a selection of training center is needed.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_study_group_upd"/>
		<xsl:param name="lab_study_group_ins"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_tcr_title"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_leader"/>
		<xsl:param name="lab_pubic_type"/>
		<xsl:param name="lab_pubic_type_desc_close"/>
		<xsl:param name="lab_pubic_type_desc_mem"/>
		<xsl:param name="lab_pubic_type_desc_all"/>
		<xsl:param name="lab_send_email_ind"/>
		<xsl:param name="lab_send_email_no"/>
		<xsl:param name="lab_send_email_yes"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_def_tc_desc"/>
		
		<xsl:call-template name="wb_ui_hdr"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$ins_or_upd='INS'">
						<xsl:value-of select="$lab_study_group_ins"/>
					</xsl:when>
					<xsl:when test="$ins_or_upd='UPD'">
						<xsl:value-of select="$lab_study_group_upd"/>
					</xsl:when>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<xsl:call-template name="group_body">
			<xsl:with-param name="lab_title" select="$lab_title"/>
			<xsl:with-param name="lab_tcr_title" select="$lab_tcr_title"/>
			<xsl:with-param name="lab_desc" select="$lab_desc"/>
			<xsl:with-param name="lab_leader" select="$lab_leader"/>
			<xsl:with-param name="lab_pubic_type" select="$lab_pubic_type"/>
			<xsl:with-param name="lab_pubic_type_desc_close" select="$lab_pubic_type_desc_close"/>
			<xsl:with-param name="lab_pubic_type_desc_mem" select="$lab_pubic_type_desc_mem"/>
			<xsl:with-param name="lab_pubic_type_desc_all" select="$lab_pubic_type_desc_all"/>
			<xsl:with-param name="lab_send_email_ind" select="$lab_send_email_ind"/>
			<xsl:with-param name="lab_send_email_no" select="$lab_send_email_no"/>
			<xsl:with-param name="lab_send_email_yes" select="$lab_send_email_yes"/>
			<xsl:with-param name="lab_g_form_btn_ok" select="$lab_g_form_btn_ok"/>
			<xsl:with-param name="lab_g_form_btn_cancel" select="$lab_g_form_btn_cancel"/>
			<xsl:with-param name="lab_def_tc_desc" select="$lab_def_tc_desc"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="group_body">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_tcr_title"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_leader"/>
		<xsl:param name="lab_pubic_type"/>
		<xsl:param name="lab_pubic_type_desc_close"/>
		<xsl:param name="lab_pubic_type_desc_mem"/>
		<xsl:param name="lab_pubic_type_desc_all"/>
		<xsl:param name="lab_send_email_ind"/>
		<xsl:param name="lab_send_email_no"/>
		<xsl:param name="lab_send_email_yes"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_def_tc_desc"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
			<tr>
				<td height="10" width="20%">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="80%">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
			<!--小组名称-->
			<tr>
				<td width="20%" align="right">
					<span class="TitleText">
						*<xsl:value-of select="$lab_title"/>:</span>
						<input type="hidden" name="lab_sgp_title" value="{$lab_title}"></input>
				</td>
				<td width="80%">
					<input type="Text" style="width:300px;" maxlength="50" name="sgp_title" value="{@title}" class="wzb-inputText"/>
				</td>
			</tr>
			<!--培训中心-->
			<xsl:if test="$tc_enabled='true'">
				<tr>
					<td width="20%" align="right">
						<span class="TitleText">
							*<xsl:value-of select="$lab_tcr_title"/>:</span>
					</td>
					<xsl:variable name="cur_tcr_id">
						<xsl:choose>
							<xsl:when test="not(//default_training_center)"><xsl:value-of select="//tcr/@id"/></xsl:when>
							<xsl:otherwise><xsl:value-of select="//default_training_center/@id"/></xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="cur_tcr_title">
						<xsl:choose>
							<xsl:when test="not(//default_training_center)"><xsl:value-of select="//tcr/@title"/></xsl:when>
							<xsl:otherwise><xsl:value-of select="//default_training_center/title"/></xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<td width="80%">
						<xsl:call-template name="wb_goldenman">
							<xsl:with-param name="field_name">tcr_id_lst</xsl:with-param>
							<xsl:with-param name="name">tcr_id_lst</xsl:with-param>
							<xsl:with-param name="box_size">1</xsl:with-param>
							<xsl:with-param name="tree_type">training_center</xsl:with-param>
							<xsl:with-param name="select_type">2</xsl:with-param>
							<xsl:with-param name="pick_leave">0</xsl:with-param>
							<xsl:with-param name="pick_root">0</xsl:with-param>
							<xsl:with-param name="confirm_function">resetForTcAlter</xsl:with-param>
							<xsl:with-param name="confirm_msg">confirm_msg</xsl:with-param>
							<xsl:with-param name="remove_function">del_tc()</xsl:with-param>
							<xsl:with-param name="single_option_value"><xsl:value-of select="$cur_tcr_id"/></xsl:with-param>
							<xsl:with-param name="single_option_text"><xsl:value-of select="$cur_tcr_title"/></xsl:with-param>
						</xsl:call-template>
					</td>
					<input type="hidden" name="sgp_tcr_id"/>
				</tr>
			</xsl:if>
			<!-- 组长 -->
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						*<xsl:value-of select="$lab_leader"/>:
					</span>
				</td>
				<td width="80%">
					<xsl:call-template name="wb_goldenman">
						<xsl:with-param name="field_name">manager_lst</xsl:with-param>
						<xsl:with-param name="name">manager_lst</xsl:with-param>
						<xsl:with-param name="box_size">4</xsl:with-param>
						<xsl:with-param name="tree_type">user_group_and_user</xsl:with-param>
						<xsl:with-param name="select_type">4</xsl:with-param>
						<xsl:with-param name="pick_leave">0</xsl:with-param>
						<xsl:with-param name="pick_root">0</xsl:with-param>
						<xsl:with-param name="option_list">
							<xsl:apply-templates select="/studygroup/sgp/manager_lst/manager"/>
						</xsl:with-param>
						<xsl:with-param name="ftn_ext_id"></xsl:with-param>
						<xsl:with-param name="add_function">
							<xsl:choose>
								<xsl:when test="$tc_enabled='true'">openManagerTreeTcEnabled(document.frmXml.tcr_id_lst.options[0].value)</xsl:when>
								<xsl:otherwise>penManagerTree()</xsl:otherwise>
							</xsl:choose>				
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<!--小组描述-->
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_desc"/>:</span>
				</td>
				<td width="80%">
					<textarea class="wzb-inputTextArea" rows="5" cols="50" name="sgp_desc" style="width:300px;"><xsl:value-of select="desc/text()"/></textarea>
				</td>
			</tr>
			<!--公开设定-->
			<tr>
				<td width="20%" align="right">
					<span class="TitleText">
						*<xsl:value-of select="$lab_pubic_type"/>:</span>
				</td>
				<td width="80%">
					<label for="public_type_0"> 
						<input id="public_type_0" type="radio" name="sgp_public_type" value="0">
						<xsl:if test="$ins_or_upd = 'UPD' and @public_type = 0"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
						</input>
						<span class="Text">
							<xsl:value-of select="$lab_pubic_type_desc_close"/>
						</span>
					</label>
				</td>
			</tr>
			<tr>
				<td width="20%">&#160;</td>
				<td width="80%">
					<label for="public_type_1">
						<input id="public_type_1" type="radio" name="sgp_public_type" value="1">
						<xsl:if test="$ins_or_upd = 'INS' or @public_type = 1"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
						</input>
						<span class="Text">
							<xsl:value-of select="$lab_pubic_type_desc_mem"/>
						</span>
					</label>
				</td>
			</tr>
			<!--发送email给员-->
			<tr>
				<td width="20%" align="right">
					<span class="TitleText">
						*<xsl:value-of select="$lab_send_email_ind"/>:</span>
				</td>
				<td width="80%">
					<label for="send_email_0"> 
						<input id="send_email_0" type="radio" name="sgp_send_email_ind" value="0">
						<xsl:if test="$ins_or_upd = 'INS' or @send_email_ind = 0"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
						</input>
						<span class="Text">
							<xsl:value-of select="$lab_send_email_no"/>
						</span>
					</label>
				</td>
			</tr>
			<tr>
				<td width="20%">&#160;</td>
				<td width="80%">
					<label for="send_email_1">
						<input id="send_email_1" type="radio" name="sgp_send_email_ind" value="1">
						<xsl:if test="$ins_or_upd = 'UPD' and @send_email_ind = 1"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
						</input>
						<span class="Text">
							<xsl:value-of select="$lab_send_email_yes"/>
						</span>
					</label>
				</td>
			</tr>
			<tr>
				<td height="10" width="20%">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="80%">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_line"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td  align="center">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:studygroup.ins_upd_sgp(document.frmXml,'<xsl:value-of select="$old_tcr_id"/>')</xsl:with-param>
					</xsl:call-template>
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template match="manager">
		<option value="{@id}">
			<xsl:value-of select="text()"/>
		</option>
	</xsl:template>
</xsl:stylesheet>