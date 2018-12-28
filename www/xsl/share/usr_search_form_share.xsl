<?xml version="1.0" encoding="UTF-8"?>
<!--this XSL should included with usr_details_label_utils.xsl-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="../utils/display_form_input_time.xsl"/>
<xsl:import href="../utils/wb_gen_drop_down_box.xsl"/>
<xsl:import href="../utils/wb_gen_checkbox.xsl"/>
<!-- <xsl:import href="../utils/wb_goldenman.xsl" />
<xsl:import href="../utils/wb_ui_line.xsl" /> -->
<xsl:import href="label_role.xsl"/>
	<!-- const -->
	<xsl:variable name="field_width">300</xsl:variable>
	
	<!-- ======================================================================== -->
	<!-- line spacing-->
	<xsl:template name="empty_row">
		<xsl:param name="tableLeftWidth">20%</xsl:param>
		<xsl:param name="tableRightWidth">80%</xsl:param>
		<xsl:param name="tableLeftText">TitleText</xsl:param>
		<xsl:param name="tableRightText">Text</xsl:param>
		<tr>
			<td height="10" width="{$tableLeftWidth}">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td height="10" width="{$tableRightWidth}">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
		</tr>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="*" mode="profile_attributes">
		<xsl:param name="searchable"/>
		<xsl:choose>
			<xsl:when test="not(starts-with(name(), 'extension'))">
				<xsl:apply-templates select="." mode="attributes">
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="." mode="exten_attributes"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="*" mode="exten_attributes">
		<xsl:variable name="attr_name" select="name()"/><xsl:value-of select="@id"/>
		<xsl:variable name="lab_attr" select="label[@xml:lang = $cur_lang]"/>
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_attr"/>：</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:choose>
					<xsl:when test="@type='text'">
						<input type="text" class="wzb-inputText" name="{concat('s_ext_', $attr_name, '_text')}" maxlength="20" size="30" style="width:{$field_width}px" value=""/>
					</xsl:when>
					<xsl:when test="@type='date'">
						<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name"><xsl:value-of select="concat($attr_name, '_from')"/></xsl:with-param>
						<xsl:with-param name="hidden_fld_name"><xsl:value-of select="concat('s_ext_', $attr_name, '_fr')"/></xsl:with-param>
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						</xsl:call-template>
						<xsl:text>&#160;</xsl:text><xsl:value-of select="$lab_to"/><xsl:text>&#160;</xsl:text>
						<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name"><xsl:value-of select="concat($attr_name, '_to')"/></xsl:with-param>
						<xsl:with-param name="hidden_fld_name"><xsl:value-of select="concat('s_ext_', $attr_name, '_to')"/></xsl:with-param>
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="@type='comboBox'">
						<xsl:call-template name="gen_drop_down_box">
							<xsl:with-param name="name"><xsl:value-of select="concat('s_ext_', $attr_name, '_select')"/></xsl:with-param>
							<xsl:with-param name="options" select="option_list"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="@type='checkBox'">
						<xsl:call-template name="gen_check_box">
							<xsl:with-param name="name"><xsl:value-of select="concat('s_ext_', $attr_name, '_check')"/></xsl:with-param>
							<xsl:with-param name="options" select="option_list"/>
							<xsl:with-param name="cur_att_name" select="name()"/>
						</xsl:call-template>
					</xsl:when>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<input type="hidden" name="{$attr_name}" value="{$lab_attr}"/>
	</xsl:template>
	<!-- usr_id -->
	<xsl:template match="user_id" mode="attributes">
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_login_id"/>：</xsl:with-param>
			<xsl:with-param name="content"><input type="text" class="wzb-inputText" name="s_usr_id" maxlength="20" size="30" style="width:{$field_width}px" value=""/></xsl:with-param>
		</xsl:call-template>
		<input type="hidden" name="lab_login_id" value="{$lab_login_id}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_display_bil -->
	<xsl:template match="name" mode="attributes">
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_dis_name"/>：</xsl:with-param>
			<xsl:with-param name="content"><input type="text" class="wzb-inputText" name="s_usr_display_bil" maxlength="255" size="30" style="width:{$field_width}px" value=""/></xsl:with-param>
		</xsl:call-template>
		<input type="hidden" name="lab_dis_name" value="{$lab_dis_name}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_pwd -->
	<xsl:template match="password" mode="attributes"/>
	<!-- ======================================================================== -->
	<!-- confirm_usr_pwd_read -->
	<xsl:template match="confirm_usr_pwd_read"/>
	<!-- ======================================================================== -->
	<!-- ent_syn_ind -->
	<xsl:template match="ent_syn_ind_read"/>
	<!-- ======================================================================== -->
	<!-- ent_syn_rol_ind -->
	<xsl:template match="ent_syn_rol_ind_read"/>
	<!-- ======================================================================== -->
	<!-- direct_supervisors -->
	<xsl:template match="direct_supervisors" mode="attributes"/>
	<!-- ======================================================================== -->
	<!-- app_approval_usg_ent_id -->
	<xsl:template match="app_approval_usg_ent_id" mode="attributes"/>
	<!-- ======================================================================== -->
	<!-- ent_syn_rol_ind -->
	<xsl:template match="supervised_groups" mode="attributes"/>
	<!-- ======================================================================== -->
	<!-- usr_gender -->
	<xsl:template match="gender" mode="attributes">
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title">
			<xsl:value-of select="$lab_gender"/>：</xsl:with-param>
			<xsl:with-param name="content">
				<label for="usr_gender_m">
				<input id="usr_gender_m" type="radio" name="usr_gender" value="M"/>
				<xsl:value-of select="$lab_gender_m"/>&#160;</label>
				<label for="usr_gender_f">
				<input id="usr_gender_f" type="radio" name="usr_gender" value="F"/>
				<xsl:value-of select="$lab_gender_f"/>&#160;</label>
				<input type="hidden" name="s_usr_gender" value=""/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_bday -->
	<xsl:template match="date_of_birth" mode="attributes">
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_bday"/>：</xsl:with-param>
			<xsl:with-param name="content"><xsl:call-template name="display_form_input_time"><xsl:with-param name="fld_name">bday_from</xsl:with-param><xsl:with-param name="hidden_fld_name">s_usr_bday_fr</xsl:with-param><xsl:with-param name="frm">document.frmXml</xsl:with-param></xsl:call-template><xsl:text>&#160;</xsl:text><xsl:value-of select="$lab_to"/><xsl:text>&#160;</xsl:text><xsl:call-template name="display_form_input_time"><xsl:with-param name="fld_name">bday_to</xsl:with-param><xsl:with-param name="hidden_fld_name">s_usr_bday_to</xsl:with-param><xsl:with-param name="frm">document.frmXml</xsl:with-param></xsl:call-template></xsl:with-param>
		</xsl:call-template>
		<input type="hidden" name="lab_dis_name_bday" value="{$lab_bday}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_email -->
	<xsl:template match="email" mode="attributes">
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_e_mail"/>：</xsl:with-param>
			<xsl:with-param name="content"><input type="text" class="wzb-inputText" name="s_usr_email" maxlength="255" size="30" style="width:{$field_width}px" value=""/></xsl:with-param>
		</xsl:call-template>
		<input type="hidden" name="lab_e_mail" value="{$lab_e_mail}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_tel_1 -->
	<xsl:template match="phone" mode="attributes">
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_tel_1"/>：</xsl:with-param>
			<xsl:with-param name="content"><input type="text" class="wzb-inputText" name="s_usr_tel" maxlength="50" size="30" style="width:{$field_width}px" value=""/></xsl:with-param>
		</xsl:call-template>
		<input type="hidden" name="lab_tel_1" value="{$lab_tel_1}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_fax_1 -->
	<xsl:template match="fax" mode="attributes">
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_fax_1"/>：</xsl:with-param>
			<xsl:with-param name="content"><input type="text" class="wzb-inputText" name="s_usr_fax" maxlength="50" size="30" style="width:{$field_width}px" value=""/></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_role_lst -->
	<xsl:template match="role" mode="attributes">
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_role"/>：</xsl:with-param>
			<xsl:with-param name="content">
				<table>
					<xsl:apply-templates select="/user_manager/group_member_list/all_role_list/role"/>
				</table>
			</xsl:with-param>
		</xsl:call-template>
		<input type="hidden" name="lab_role" value="{$lab_role}"/>
	</xsl:template>
	<!--grade-->
	<xsl:template match="grade" mode="attributes">
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_grade"/>：</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:call-template name="wb_goldenman">
					<xsl:with-param name="frm">document.frmXml</xsl:with-param>
					<xsl:with-param name="width">200</xsl:with-param>
					<xsl:with-param name="field_name">s_grade_lst</xsl:with-param>
					<xsl:with-param name="tree_type">grade</xsl:with-param>
					<xsl:with-param name="select_type">2</xsl:with-param>
					<xsl:with-param name="box_size">1</xsl:with-param>
				</xsl:call-template>
				<input type="hidden" name="s_grade" value=""/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ======================================================================== -->
	<!--group-->
	<xsl:template match="group" mode="attributes">
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_group"/>：</xsl:with-param>
			<xsl:with-param name="content"><xsl:call-template name="wb_goldenman"><xsl:with-param name="frm">document.frmXml</xsl:with-param><xsl:with-param name="width">200</xsl:with-param><xsl:with-param name="field_name">usr_group_lst</xsl:with-param><xsl:with-param name="tree_type">user_group</xsl:with-param><xsl:with-param name="select_type">2</xsl:with-param><xsl:with-param name="box_size">1</xsl:with-param><xsl:with-param name="single_option_text"><xsl:value-of select="group_member_list/desc"/></xsl:with-param><xsl:with-param name="single_option_value"><xsl:value-of select="group_member_list/@id"/></xsl:with-param><xsl:with-param name="label_add_btn"><xsl:value-of select="$lab_gen_select"/></xsl:with-param><xsl:with-param name="filter_user_group"><xsl:value-of select="$filter_user_group"/></xsl:with-param></xsl:call-template></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ======================================================================== -->
	<!--group for supervise-->
	<xsl:template match="usr_group_for_supervise">
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_group"/>：</xsl:with-param>
			<xsl:with-param name="content"><xsl:call-template name="wb_goldenman"><xsl:with-param name="frm">document.frmXml</xsl:with-param><xsl:with-param name="width">200</xsl:with-param><xsl:with-param name="field_name">usr_group_lst</xsl:with-param><xsl:with-param name="tree_type">user_group</xsl:with-param><xsl:with-param name="select_type">2</xsl:with-param><xsl:with-param name="box_size">1</xsl:with-param><xsl:with-param name="single_option_text"><xsl:value-of select="group_member_list/desc"/></xsl:with-param><xsl:with-param name="single_option_value"><xsl:value-of select="group_member_list/@id"/></xsl:with-param><xsl:with-param name="label_add_btn"><xsl:value-of select="$lab_gen_select"/></xsl:with-param><xsl:with-param name="get_supervise_group">1</xsl:with-param></xsl:call-template></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ======================================================================== -->

	<!--group and trigger the user status-->
	<xsl:template match="usr_group_trigger_status">
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_group"/>：</xsl:with-param>
			<xsl:with-param name="content"><xsl:call-template name="wb_goldenman"><xsl:with-param name="frm">document.frmXml</xsl:with-param><xsl:with-param name="width">200</xsl:with-param><xsl:with-param name="field_name">usr_group_lst</xsl:with-param><xsl:with-param name="tree_type">user_group</xsl:with-param><xsl:with-param name="select_type">2</xsl:with-param><xsl:with-param name="box_size">1</xsl:with-param><xsl:with-param name="single_option_text"><xsl:value-of select="group_member_list/desc"/></xsl:with-param><xsl:with-param name="single_option_value"><xsl:value-of select="group_member_list/@id"/></xsl:with-param><xsl:with-param name="label_add_btn"><xsl:value-of select="$lab_gen_select"/></xsl:with-param><xsl:with-param name="custom_js_code">
					document.frmXml.s_status_rad[0].checked=true;
				</xsl:with-param></xsl:call-template></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ======================================================================== -->
	<!--usr status -->
	<xsl:template name="usr_status">
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_usr_status"/>：</xsl:with-param>
			<xsl:with-param name="content">
				<label for="rdo_s_status_rad_1">
				<input type="checkbox" id="rdo_s_status_rad_1" name="s_status_rad" value="ok" checked="checked"/><xsl:value-of select="$lab_usr_status_active"/></label>
				<xsl:text>&#160;</xsl:text>
				<label for="rdo_s_status_rad_2">
				<input type="checkbox" id="rdo_s_status_rad_2" name="s_status_rad" value="deleted"/><xsl:value-of select="$lab_usr_status_deleted"/>
				</label>
				<input type="hidden" name="s_status" value=""/>
				</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ======================================================================== -->
	<!--usr status or-->
	<!-- must draw the usergroup box-->
	<xsl:template name="usr_status_or">
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_usr_status"/>：</xsl:with-param>
			<xsl:with-param name="content">
			<label  for="rdo_s_status_rad_1">
			<input id="rdo_s_status_rad_1" type="radio" name="s_status_rad" value="OK" checked="checked" onClick="javascript:enable_usergroup()"/><xsl:value-of select="$lab_usr_status_active"/>
			</label>
			<xsl:text>&#160;</xsl:text>
			<label for="rdo_s_status_rad_2">
			<input id="rdo_s_status_rad_2" type="radio" name="s_status_rad" value="DELETED" onClick="javascript:disable_usergroup()"/><xsl:value-of select="$lab_usr_status_deleted"/>
			</label>

			<input type="hidden" name="s_status" value=""/></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ======================================================================== -->
	<!--usr sort -->
	<xsl:template name="usr_sort">
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_usr_sort"/>：</xsl:with-param>
			<xsl:with-param name="content"><select name="s_order_by" size="1" class="Select margin-right4"><option selected="selected" value="asc"><xsl:value-of select="$lab_sort_value_1"/></option><option value="desc"><xsl:value-of select="$lab_sort_value_2"/></option></select><select name="s_sort_by" size="1" class="Select"><option selected="selected" value="usr_display_bil"><xsl:value-of select="$lab_dis_name"/></option><option value="usg_display_bil"><xsl:value-of select="$lab_group"/></option></select></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_jday -->
	<xsl:template match="join_date" mode="attributes">
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_join_date"/>：</xsl:with-param>
			<xsl:with-param name="content"><xsl:call-template name="display_form_input_time"><xsl:with-param name="fld_name">jday_from</xsl:with-param><xsl:with-param name="hidden_fld_name">s_usr_jday_fr</xsl:with-param><xsl:with-param name="frm">document.frmXml</xsl:with-param></xsl:call-template><xsl:text>&#160;</xsl:text><xsl:value-of select="$lab_to"/><xsl:text>&#160;</xsl:text><xsl:call-template name="display_form_input_time"><xsl:with-param name="fld_name">jday_to</xsl:with-param><xsl:with-param name="hidden_fld_name">s_usr_jday_to</xsl:with-param><xsl:with-param name="frm">document.frmXml</xsl:with-param></xsl:call-template></xsl:with-param>
		</xsl:call-template>
		<input type="hidden" name="lab_dis_name_jday" value="{$lab_join_date}"/>
	</xsl:template>
	<!-- job_title -->
	<xsl:template match="job_title" mode="attributes">
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_job_title"/>：</xsl:with-param>
			<xsl:with-param name="content"><input type="text" class="wzb-inputText" name="s_usr_job_title" maxlength="255" size="30" style="width:{$field_width}px" value=""/></xsl:with-param>
		</xsl:call-template>
		<input type="hidden" name="lab_job_title" value="{$lab_job_title}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!--usr source -->
	<xsl:template match="source" mode="attributes">
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_usr_source"/>：</xsl:with-param>
			<xsl:with-param name="content">
				<select name="s_usr_source" size="1" class="Select">
					<option value=""><xsl:value-of select="$lab_not_specified"/></option>
						<xsl:for-each select="option_list/*">
							<xsl:variable name="id" select="@id"/>
							<option value="{$id}"><xsl:value-of select="label[@xml:lang = $cur_lang]"/></option>
						</xsl:for-each>
				</select>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template name="draw_layout">
		<xsl:param name="title">
		</xsl:param>
		<xsl:param name="content">
		</xsl:param>
		<xsl:param name="tableLeftWidth">20%</xsl:param>
		<xsl:param name="tableRightWidth">80%</xsl:param>
		<xsl:param name="tableLeftText">TitleText</xsl:param>
		<xsl:param name="tableRightText">Text</xsl:param>
		<tr>
			<td class="wzb-form-label" valign="top">
				<xsl:copy-of select="$title"/>
			</td>
			<td class="wzb-form-control">
				<xsl:copy-of select="$content"/>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="group_member_list/all_role_list/role">
		<xsl:variable name="cur_role">
			<xsl:value-of select="@id"/>
		</xsl:variable>
		<xsl:if test="not(position() mod 2 = 0)">
			<xsl:text disable-output-escaping="yes">&lt;tr&gt;</xsl:text>
		</xsl:if>
		<td align="left">
			<label for="rdo_usr_role{@id}">
			<span class="Text">
				<input type="checkbox" name="usr_role" value="{@id}" id="rdo_usr_role{@id}"/>
			</span>
				<xsl:text>&#160;</xsl:text>
				<xsl:call-template name="get_rol_title"/>
				<xsl:text>&#160;</xsl:text>
			</label>
		</td>
		<xsl:choose>
			<xsl:when test="position() mod 2 = 0">
				<xsl:text disable-output-escaping="yes">&lt;/tr&gt;</xsl:text>
			</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template name="usr_detail_seperate_hdr">
		<xsl:param name="tableLeftText">TitleText</xsl:param>
		<xsl:param name="tableRightText">Text</xsl:param>
		<xsl:param name="hdr_title"/>
		<xsl:param name="initial">false</xsl:param>
		<xsl:call-template name="wb_ui_line"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
			<xsl:call-template name="empty_row"/>
		</table>
	</xsl:template>
</xsl:stylesheet>
