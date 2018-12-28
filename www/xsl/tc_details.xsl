<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
    <!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>

	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="share/usr_gen_tab_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/div_tree_share.xsl"/>
	<xsl:import href="share/gen_tree_js.xsl"/>
	<xsl:import href="share/label_role.xsl"/>
	
	<xsl:variable select="tc_module/training_center" name="tc"/>
	<xsl:variable name="admin_role" select="tc_module/training_center/role_list"/>
	<xsl:variable name="has_upd_btn" select="tc_module/acControl/@hasUpdBtn"/>
	<xsl:variable name="has_set_adm_btn" select="tc_module/acControl/@hasSetAdmBtn"/>
	<xsl:variable name="has_temp_btn" select="tc_module/acControl/@hasTempBtn"/>
	<xsl:variable name="has_del_btn" select="tc_module/acControl/@hasDelBtn"/>
	<xsl:variable name="lab_880" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '880')"/><!-- Edit Website Style -->
	<!-- ================================================================== -->
	<xsl:template match="/">
		<xsl:call-template name="main"/>
	</xsl:template>
	<!--=================================================================== -->
	<xsl:template name="main">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_tc_mgt.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}tcr_template_defined.js"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[
				    ct_mgt = new wbTcMgt;				    
		
				    function show_content(tcr_id) {
					    if(tcr_id != 0) {
					    	 ct_mgt.tc_detail(tcr_id);
   			    	 }
				    }
			    ]]></script>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="page_onload(302)">
				<form name="frmXml">
					<xsl:call-template name="draw_info"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- ========================================================================== -->
	
	
	
	<xsl:variable name="lab_tc_mgt_title" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_91')"/>
	<xsl:variable name="lab_tc_code" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_92')"/>
	<xsl:variable name="lab_tc_name" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_93')"/>
	<xsl:variable name="lab_del" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_94')"/>
	<xsl:variable name="lab_edit" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_95')"/>
	<xsl:variable name="lab_target_group" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_96')"/>
	<xsl:variable name="lab_info" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_97')"/>
	<xsl:variable name="lab_btn_set_ta" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_98')"/>
	<xsl:variable name="lab_btn_add_sub_tc" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_99')"/>
	<xsl:variable name="lab_mgt_user" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_100')"/>
	<xsl:variable name="lab_mgt_user_yes" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_101')"/>
	<xsl:variable name="lab_mgt_user_no" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_102')"/>
	<xsl:variable name="lab_all_usergroups" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_103')"/>
	<xsl:variable name="lab_root_training_center" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_104')"/>
	<xsl:variable name="lab_subordinat_lst" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_105')"/>
	<xsl:variable name="wb_ui_show_no_item" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_106')"/>
	<xsl:variable name="lab_template" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_107')"/>
		

	
	<!-- ============================================================================  -->
	<xsl:template name="draw_info">

		 <xsl:param name="lab_basic_info"/>
	
		 
		 <xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_POSTER_MAIN</xsl:with-param>
		</xsl:call-template>
		 <xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_tc_mgt_title"/>
			</xsl:with-param>
		 </xsl:call-template>	
		 <xsl:variable name="nav_text">
			<xsl:for-each select="tc_module/training_center/ancestor_node_list/node">
				<a class="NavLink" href="javascript:ct_mgt.tc_detail({@id})">
					<xsl:value-of select="title"/>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
			</xsl:for-each>
	      <a class="NavLink" href="javascript:ct_mgt.tc_detail({$tc/@id})"><xsl:value-of select="$tc/name"/></a>
		 </xsl:variable>
		 <xsl:call-template name="div_tree">
        	<xsl:with-param name="title" select="tc_module/training_center/name"/>
			<xsl:with-param name="lab_root_training_center" select="$lab_root_training_center"/>
			<xsl:with-param name="nav_text"><xsl:copy-of select="$nav_text"/></xsl:with-param>
        </xsl:call-template>
		 <xsl:apply-templates select="tc_module/training_center">
			<xsl:with-param name="lab_basic_info" select="$lab_basic_info"/>
			<xsl:with-param name="lab_tc_code" select="$lab_tc_code"/>
			<xsl:with-param name="lab_tc_name" select="$lab_tc_name"/>
			<xsl:with-param name="lab_target_group" select="$lab_target_group"/>
			<xsl:with-param name="lab_edit" select="$lab_edit"/>
			<xsl:with-param name="lab_del" select="$lab_del"/>
			<xsl:with-param name="lab_info" select="$lab_info"/>
			<xsl:with-param name="lab_btn_set_ta" select="$lab_btn_set_ta"/>
			<xsl:with-param name="lab_mgt_user" select="$lab_mgt_user"/>
			<xsl:with-param name="lab_mgt_user_yes" select="$lab_mgt_user_yes"/>
			<xsl:with-param name="lab_mgt_user_no" select="$lab_mgt_user_no"/>	
			<xsl:with-param name="lab_all_usergroups" select="$lab_all_usergroups"/>
			<xsl:with-param name="lab_subordinat_lst" select="$lab_subordinat_lst"/>
			<xsl:with-param name="lab_btn_add_sub_tc" select="$lab_btn_add_sub_tc"/>
			<xsl:with-param name="wb_ui_show_no_item" select="$wb_ui_show_no_item"/>
			<xsl:with-param name="lab_template" select="$lab_template"></xsl:with-param>
		 </xsl:apply-templates>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template match="tc_module/training_center">
		<xsl:param name="lab_basic_info"/>
		<xsl:param name="lab_tc_code"/>
		<xsl:param name="lab_tc_name"/>
		<xsl:param name="lab_target_group"/>
		<xsl:param name="lab_edit"/>
		<xsl:param name="lab_del"/>
		<xsl:param name="lab_info"/>
		<xsl:param name="lab_btn_set_ta"/>
		 <xsl:param name="lab_mgt_user"/>
		 <xsl:param name="lab_mgt_user_yes"/>
		 <xsl:param name="lab_mgt_user_no"/>	
		<xsl:param name="lab_all_usergroups"/>	 
		 <xsl:param name="lab_subordinat_lst"/>
		 <xsl:param name="lab_btn_add_sub_tc"/>
		 <xsl:param name="wb_ui_show_no_item"/>
		 <xsl:param name="lab_template"></xsl:param>
		<xsl:call-template name="wb_ui_space"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_info"/>
			</xsl:with-param>
			<xsl:with-param name="extra_td">
				<td align="right">
					<xsl:if test="$has_upd_btn='true'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_edit"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:ct_mgt.upd_tc_prep(<xsl:value-of select="$tc/@id"/>)</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
						</xsl:call-template>	
					
					</xsl:if>
					<xsl:if test="$has_set_adm_btn='true'">		
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_btn_set_ta"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:ct_mgt.set_ta_prep(<xsl:value-of select="$tc/@id"/>)</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="$has_del_btn = 'true'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_del"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:ct_mgt.del_tc_exe(<xsl:value-of select="$tc/@id"/>, <xsl:value-of select="$tc/parent_tcr/@id"/>)</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
								
				</td>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_tc_code"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="@code"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_tc_name"/><xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="name"/>
				</td>
			</tr>
			<xsl:call-template name="draw_officers"/>
			<xsl:apply-templates select="$tc/target_entity_list">
				<xsl:with-param name="lab_target_group" select="$lab_target_group"/>
				<xsl:with-param name="lab_all_usergroups" select="$lab_all_usergroups"/>
			</xsl:apply-templates>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_mgt_user"/><xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="@user_mgt_ind='true'">
							<xsl:value-of select="$lab_mgt_user_yes"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_mgt_user_no"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<!--
			<tr>
				<td height="10" align="right" width="20%">
					<IMG border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
				<td width="80%">
					<IMG border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
			-->
		</table>
		<xsl:apply-templates select="child_tc_list">
			<xsl:with-param name="lab_btn_add_sub_tc" select="$lab_btn_add_sub_tc"/>
			<xsl:with-param name="lab_subordinat_lst" select="$lab_subordinat_lst"/>
			<xsl:with-param name="wb_ui_show_no_item" select="$wb_ui_show_no_item"/>
		</xsl:apply-templates>
	</xsl:template>
    <!-- ============================================================================  -->
	<xsl:template name="draw_officers">
		<xsl:if test="count($admin_role/role)>0">
			<xsl:apply-templates select="$admin_role/role"/>
		</xsl:if>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template match="role">
		<xsl:variable name="id" select="@id"/>
		<tr>
			<td class="wzb-form-label">
				<xsl:call-template name="get_rol_title"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<xsl:choose>
					<xsl:when test="$tc/officer_list/role[@id = $id]/entity">
						<xsl:apply-templates select="$tc/officer_list/role[@id = $id]/entity"/>
					</xsl:when>
					<xsl:otherwise>--</xsl:otherwise>
				</xsl:choose>					
			</td>
		</tr>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template match="target_entity_list">
		<xsl:param name="lab_target_group"/>
		<xsl:param name="lab_all_usergroups"/>
		<tr>
			<td class="wzb-form-label">
			   <xsl:value-of select="$lab_target_group"/><xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<xsl:choose>
					<xsl:when test="$tc/@is_super='true'">
						<xsl:value-of select="$lab_all_usergroups"/>
					</xsl:when>
					<xsl:when test="$tc/target_entity_list/entity">
						<xsl:apply-templates select="$tc/target_entity_list/entity"/>
					</xsl:when>
					<xsl:otherwise>--</xsl:otherwise>
				</xsl:choose>					
			</td>
		</tr>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template match="entity">
		 <xsl:choose>
			<xsl:when test="position()!=last()">
			   <xsl:value-of select="text()"/><xsl:text>, </xsl:text>     
			</xsl:when>
			<xsl:otherwise>
			   <xsl:value-of select="text()"/>     
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template match="child_tc_list">			
		<xsl:param name="lab_btn_add_sub_tc"/>	 
		<xsl:param name="lab_subordinat_lst"/>
		<xsl:param name="wb_ui_show_no_item"/>
		<xsl:call-template name="wb_ui_space"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_subordinat_lst"/>
			</xsl:with-param>
			<xsl:with-param name="extra_td">
				<td align="right">		
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_btn_add_sub_tc"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:ct_mgt.add_tc_prep(<xsl:value-of select="$tc/@id"/>)</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
					</xsl:call-template>										
				</td>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<xsl:choose>
			<xsl:when test="training_center">
			
				<table>
					<xsl:for-each select="training_center">
						<xsl:if test="(position()-1) mod 3 = 0">
							<xsl:text disable-output-escaping="yes">&lt;tr &gt;</xsl:text>
						</xsl:if>		
						<td>
							<table cellpadding="8" cellspacing="0" border="0">
								<tr>
									<td width="348">
										<a href="javascript:ct_mgt.tc_detail({@id})" class="Text">
											<b><xsl:value-of select="name/text()"/></b>
										</a>
									</td>
								</tr>
							</table>
						</td>
						<xsl:if test="position() mod 3 = 0">
							<xsl:text disable-output-escaping="yes">&lt;/tr &gt;</xsl:text>
						</xsl:if>
					</xsl:for-each>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text"><xsl:value-of select="$wb_ui_show_no_item"/></xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ============================================================================  -->
</xsl:stylesheet>