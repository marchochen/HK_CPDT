<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- ======================================================================== -->
	<!-- utils -->
	<xsl:import href="../utils/display_form_input_time.xsl"/>
	<xsl:import href="../utils/display_time.xsl"/>
	<xsl:import href="../utils/wb_goldenman.xsl"/>
	<xsl:import href="../utils/escape_js.xsl"/>
	<xsl:import href="../utils/wb_ui_head.xsl"/>
	<xsl:import href="../utils/wb_ui_line.xsl"/>
	<xsl:import href="../utils/wb_ui_space.xsl"/>
	<xsl:import href="../utils/wb_gen_input_file.xsl"/>
	<xsl:import href="../utils/wb_ui_desc.xsl"/>
	<xsl:import href="../utils/wb_gen_drop_down_box.xsl"/>
	<xsl:import href="../utils/wb_gen_checkbox.xsl"/>
	<xsl:import href="label_role.xsl"/>
	<!-- ======================================================================== -->
	<!-- const -->
	<xsl:variable name="tableLeftWidth">20%</xsl:variable>
	<xsl:variable name="tableRightWidth">80%</xsl:variable>
	<xsl:variable name="tableLeftText">TitleText</xsl:variable>
	<xsl:variable name="tableRightText">Text</xsl:variable>
	<xsl:variable name="field_width">300</xsl:variable>
	<xsl:variable name="tableTdCont">wzb-form-control</xsl:variable>
	
	<xsl:variable name="lab_select_default_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_select_default_image')"/>
	<xsl:variable name="lab_default_images" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_default_images')"/>
	<xsl:variable name="lab_upload_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_upload_image')"/>
	<xsl:variable name="lab_button_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, '329')"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/> 	
	<!-- ======================================================================== -->
	<!-- usr_old_pwd -->
	<xsl:template name="usr_old_pwd">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="read_only">false</xsl:param>
		<xsl:param name="max_length">20</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label">
				<xsl:if test="$required_field = 'true' and $read_only != 'true'">
					<span class="wzb-form-star">*</span>
				</xsl:if>
				<xsl:value-of select="$lab_old_passwd"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<input class="wzb-inputText" type="password" name="usr_old_pwd" style="width:{$field_width}px;" size="27" maxlength="{$max_length}">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<input type="hidden" name="usr_old_pwd_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_old_passwd" value="{$lab_old_passwd}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_new_pwd -->
	<xsl:template name="usr_new_pwd">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="read_only">false</xsl:param>
		<xsl:param name="max_length">20</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label" valign="top">
				<xsl:if test="$required_field = 'true' and $read_only != 'true'">
					<span class="wzb-form-star">*</span>
				</xsl:if>
				<xsl:value-of select="$lab_new_passwd"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<input class="wzb-inputText" type="password" name="usr_new_pwd" style="width:{$field_width}px;" size="27" maxlength="{$max_length}">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<br/>
				<xsl:copy-of select="$lab_passwd_length"/>
				<input type="hidden" name="usr_new_pwd_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_new_passwd" value="{$lab_new_passwd}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_pwd_need_change_ind -->
	<xsl:template name="usr_pwd_need_change_ind">
		<xsl:param name="read_only">false</xsl:param>
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value">false</xsl:param>
		<tr>
			<td class="wzb-form-label" valign="top">
			</td>
			<td class="wzb-form-control">
				<label for="rdo_usr_pwd_need_change_ind_chk">
					<input type="checkbox" name="usr_pwd_need_change_ind_chk" value="" id="rdo_usr_pwd_need_change_ind_chk">
						<xsl:choose>
							<xsl:when test="$element_value = 'true'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:when>
						</xsl:choose>
					</input>
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$lab_usr_pwd_need_change_desc"/>
				</label>
				<input type="hidden" name="usr_pwd_need_change_ind" value="{$element_value}"/>
				<input type="hidden" name="usr_pwd_need_change_ind_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_id -->
	<xsl:template name="usr_id">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="element_value"/>
		<xsl:param name="max_length">20</xsl:param>
		<tr>
			<td class="wzb-form-label" valign="top">
				<xsl:if test="$required_field = 'true' and not($element_value != '')">
					<span class="wzb-form-star">*</span>
				</xsl:if>
				<xsl:value-of select="$lab_login_id"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<xsl:choose>
					<xsl:when test="$element_value != ''">
						<xsl:value-of select="$element_value"/>
						<input type="hidden" name="usr_id" value="{$element_value}"/>
					</xsl:when>
					<xsl:otherwise>
						<input class="wzb-inputText" type="text" name="usr_id" size="27" style="width:{$field_width}px;" maxlength="{$max_length}"/>
						<br/>
						<xsl:copy-of select="$lab_id_requirement"/>
					</xsl:otherwise>
				</xsl:choose>
				<input type="hidden" name="usr_id_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
				<input type="hidden" name="lab_login_id" value="{$lab_login_id}"/>
			</td>
		</tr>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_id_read-->
	<xsl:template name="usr_id_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_login_id"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="$element_value!=''">
							<xsl:value-of select="$element_value"/>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
		<input type="hidden" name="lab_login_id" value="{$lab_login_id}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_pwd -->
	<xsl:template name="usr_pwd">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="max_length">20</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label" valign="top">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_passwd"/>
					<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<input type="password" name="usr_pwd" style="width:{$field_width}px;" size="27" maxlength="{$max_length}" class="wzb-inputText">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<br/>
				<xsl:copy-of select="$lab_passwd_length"/>
				<input type="hidden" name="usr_pwd_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_passwd" value="{$lab_passwd}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_pwd -->
	<xsl:template name="usr_pwd_read"/>
	<!-- ======================================================================== -->
	<!-- confirm_usr_pwd -->
	<xsl:template name="confirm_usr_pwd">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="max_length">20</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_con_passwd"/>
					<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<input type="password" style="width:{$field_width}px;"  name="confirm_usr_pwd" size="27" maxlength="{$max_length}" class="wzb-inputText">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<input type="hidden" name="confirm_usr_pwd_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_con_passwd" value="{$lab_con_passwd}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- confirm_usr_pwd_read -->
	<xsl:template name="confirm_usr_pwd_read"/>
	<!-- ======================================================================== -->
	<!-- usr_display_bil -->
	<xsl:template name="usr_display_bil">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="max_length">255</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label">
				<xsl:if test="$required_field = 'true'">
					<span class="wzb-form-star">*</span>
				</xsl:if>
				<xsl:value-of select="$lab_dis_name"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<input class="wzb-inputText" type="text" name="usr_display_bil" style="width:{$field_width}px;" size="27" maxlength="{$max_length}">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<input type="hidden" name="usr_display_bil_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_dis_name" value="{$lab_dis_name}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_display_bil -->
	<xsl:template name="usr_display_bil_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_dis_name"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="$element_value!=''">
							<xsl:value-of select="$element_value"/>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_gender -->
	<xsl:template name="usr_gender">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label">
				<xsl:if test="$required_field = 'true'">
					<xsl:text>*</xsl:text>
				</xsl:if>
				<xsl:value-of select="$lab_gender"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<select name="usr_gender" class="Select">
					<option value="">
						<xsl:if test="$element_value = ''">
							<xsl:attribute name="selected">selected</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_gender_unspecified"/>
					</option>
					<option value="M">
						<xsl:if test="$element_value = 'M'">
							<xsl:attribute name="selected">selected</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_gender_m"/>
					</option>
					<option value="F">
						<xsl:if test="$element_value = 'F'">
							<xsl:attribute name="selected">selected</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="$lab_gender_f"/>
					</option>
				</select>
				<input type="hidden" name="usr_gender_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_gender" value="{$lab_gender}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_gender -->
	<xsl:template name="usr_gender_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_gender"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="$element_value = 'M'">
							<xsl:value-of select="$lab_gender_m"/>
						</xsl:when>
						<xsl:when test="$element_value = 'F'">
							<xsl:value-of select="$lab_gender_f"/>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_bday -->
	<xsl:template name="usr_bday">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label">
				<xsl:if test="$required_field = 'true'">
					<xsl:text>*</xsl:text>
				</xsl:if>
				<xsl:value-of select="$lab_bday"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<xsl:call-template name="display_form_input_time">
					<xsl:with-param name="fld_name">bday</xsl:with-param>
					<xsl:with-param name="hidden_fld_name">usr_bday</xsl:with-param>
					<xsl:with-param name="frm">document.frmXml</xsl:with-param>
					<xsl:with-param name="timestamp">
						<xsl:value-of select="$element_value"/>
					</xsl:with-param>
				</xsl:call-template>
				<input type="hidden" name="usr_bday_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_bday" value="{$lab_bday}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_bday -->
	<xsl:template name="usr_bday_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_bday"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="$element_value"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_email -->
	<xsl:template name="usr_email">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="max_length">255</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label">
				<xsl:if test="$required_field = 'true'">
					<xsl:text>*</xsl:text>
				</xsl:if>
				<xsl:value-of select="$lab_e_mail"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<input class="wzb-inputText" type="text" name="usr_email" style="width:{$field_width}px;" size="27" maxlength="{$max_length}">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<input type="hidden" name="usr_email_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_e_mail" value="{$lab_e_mail}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_email -->
	<xsl:template name="usr_email_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_e_mail"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="$element_value!=''">
							<a href="mailto:{$element_value}" class="{$tableRightText}">
								<xsl:value-of select="$element_value"/>
							</a>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_tel_1 -->
	<xsl:template name="usr_tel_1">
		<xsl:param name="frm">document.frmXml</xsl:param>
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="full_format">false</xsl:param>
		<xsl:param name="max_length">50</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label">
				<xsl:if test="$required_field = 'true'">
					<xsl:text>*</xsl:text>
				</xsl:if>
				<xsl:value-of select="$lab_tel_1"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<input name="usr_tel_1">
					<xsl:choose>
						<xsl:when test="$full_format = 'true'">
							<xsl:attribute name="type">hidden</xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="class">wzb-inputText</xsl:attribute>
							<xsl:attribute name="type">text</xsl:attribute>
							<xsl:attribute name="style">width:<xsl:value-of select="$field_width"/>px</xsl:attribute>
							<xsl:attribute name="size">27</xsl:attribute>
							<xsl:attribute name="maxlength"><xsl:value-of select="$max_length"/></xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<xsl:if test="$full_format = 'true'">
					<input type="text" class="wzb-inputText" name="usr_tel_1_country" style="width:25px;" size="3" maxlength="3" onKeyup="javascript:auto_focus_field({$frm}.usr_tel_1_country,3,{$frm}.usr_tel_1_area)"/>
					<xsl:text>-</xsl:text>
					<input type="text" class="wzb-inputText" name="usr_tel_1_area" style="width:45px;" size="5" maxlength="5" onKeyup="javascript:auto_focus_field({$frm}.usr_tel_1_area,5,{$frm}.usr_tel_1_number)"/>
					<xsl:text>-</xsl:text>
					<input type="text" class="wzb-inputText" name="usr_tel_1_number" style="width:75px;" size="40" maxlength="42"/>
					<script language="javascript" type="text/javascript"><![CDATA[
							var tmp_tel_1_country = ]]><xsl:value-of select="$frm"/><![CDATA[.usr_tel_1.value.substring(0,]]><xsl:value-of select="$frm"/><![CDATA[.usr_tel_1.value.indexOf('-'))
							var tmp_tel_1_area = ]]><xsl:value-of select="$frm"/><![CDATA[.usr_tel_1.value.substring(]]><xsl:value-of select="$frm"/><![CDATA[.usr_tel_1.value.indexOf('-')+1,]]><xsl:value-of select="$frm"/><![CDATA[.usr_tel_1.value.lastIndexOf('-'))
							var tmp_tel_1_number = ]]><xsl:value-of select="$frm"/><![CDATA[.usr_tel_1.value.substring(]]><xsl:value-of select="$frm"/><![CDATA[.usr_tel_1.value.lastIndexOf('-')+1,]]><xsl:value-of select="$frm"/><![CDATA[.usr_tel_1.value.length)
							if (tmp_tel_1_country != 'n/a') {]]><xsl:value-of select="$frm"/><![CDATA[.usr_tel_1_country.value = tmp_tel_1_country;}
							if (tmp_tel_1_area != 'n/a') {]]><xsl:value-of select="$frm"/><![CDATA[.usr_tel_1_area.value = tmp_tel_1_area;}
							if (tmp_tel_1_number != 'n/a') {]]><xsl:value-of select="$frm"/><![CDATA[.usr_tel_1_number.value = tmp_tel_1_number;}
						]]></script>
					<xsl:text>&#160;</xsl:text>
					<br/>
					<xsl:value-of select="$lab_tel_1_desc"/>
				</xsl:if>
				<input type="hidden" name="usr_tel_1_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_tel_1" value="{$lab_tel_1}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_tel_1 -->
	<xsl:template name="usr_tel_1_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_tel_1"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="$element_value != '' and $element_value != 'n/a-n/a-n/a'">
							<xsl:call-template name="display_tel_number">
								<xsl:with-param name="numVal">
									<xsl:value-of select="$element_value"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_fax_1 -->
	<xsl:template name="usr_fax_1">
		<xsl:param name="frm">document.frmXml</xsl:param>
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="full_format">false</xsl:param>
		<xsl:param name="max_length">50</xsl:param>
		<xsl:param name="read_only">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label">
				<xsl:if test="$required_field = 'true' and $read_only != 'true'">
					<xsl:text>*</xsl:text>
				</xsl:if>
				<xsl:value-of select="$lab_fax_1"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<input name="usr_fax_1">
					<xsl:choose>
						<xsl:when test="$full_format = 'true'">
							<xsl:attribute name="type">hidden</xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="type">text</xsl:attribute>
							<xsl:attribute name="style">width:<xsl:value-of select="$field_width"/>px</xsl:attribute>
							<xsl:attribute name="size">27</xsl:attribute>
							<xsl:attribute name="maxlength"><xsl:value-of select="$max_length"/></xsl:attribute>
							<xsl:attribute name="class">wzb-inputText</xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<xsl:if test="$full_format = 'true'">
					<input class="wzb-inputText" type="text" name="usr_fax_1_country" style="width:25px;" size="3" maxlength="3" onKeyup="javascript:auto_focus_field({$frm}.usr_fax_1_country,3,{$frm}.usr_fax_1_area)"/>
					<xsl:text>-</xsl:text>
					<input class="wzb-inputText" type="text" name="usr_fax_1_area" style="width:45px;" size="5" maxlength="5" onKeyup="javascript:auto_focus_field({$frm}.usr_fax_1_area,5,{$frm}.usr_fax_1_number)"/>
					<xsl:text>-</xsl:text>
					<input class="wzb-inputText" type="text" name="usr_fax_1_number" style="width:75px;" size="40" maxlength="42"/>
					<script language="javascript" type="text/javascript"><![CDATA[
							var tmp_fax_1_country = ]]><xsl:value-of select="$frm"/><![CDATA[.usr_fax_1.value.substring(0,]]><xsl:value-of select="$frm"/><![CDATA[.usr_fax_1.value.indexOf('-'))
							var tmp_fax_1_area = ]]><xsl:value-of select="$frm"/><![CDATA[.usr_fax_1.value.substring(]]><xsl:value-of select="$frm"/><![CDATA[.usr_fax_1.value.indexOf('-')+1,]]><xsl:value-of select="$frm"/><![CDATA[.usr_fax_1.value.lastIndexOf('-'))
							var tmp_fax_1_number = ]]><xsl:value-of select="$frm"/><![CDATA[.usr_fax_1.value.substring(]]><xsl:value-of select="$frm"/><![CDATA[.usr_fax_1.value.lastIndexOf('-')+1,]]><xsl:value-of select="$frm"/><![CDATA[.usr_fax_1.value.length)
							if (tmp_fax_1_country != 'n/a') {]]><xsl:value-of select="$frm"/><![CDATA[.usr_fax_1_country.value = tmp_fax_1_country;}
							if (tmp_fax_1_area != 'n/a') {]]><xsl:value-of select="$frm"/><![CDATA[.usr_fax_1_area.value = tmp_fax_1_area;}
							if (tmp_fax_1_number != 'n/a') {]]><xsl:value-of select="$frm"/><![CDATA[.usr_fax_1_number.value = tmp_fax_1_number;}
						]]></script>
					<xsl:text>&#160;</xsl:text>
					<br/>
					<xsl:value-of select="$lab_fax_1_desc"/>
				</xsl:if>
				<input type="hidden" name="usr_fax_1_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_fax_1" value="{$lab_fax_1}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_fax_1 -->
	<xsl:template name="usr_fax_1_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_fax_1"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="$element_value != '' and $element_value != 'n/a-n/a-n/a'">
							<xsl:call-template name="display_tel_number">
								<xsl:with-param name="numVal">
									<xsl:value-of select="$element_value"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- Job Title -->
	<xsl:template name="usr_job_title">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label">
				<xsl:if test="$required_field = 'true'">
					<xsl:text>*</xsl:text>
				</xsl:if>
				<xsl:value-of select="$lab_job_title"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<input class="wzb-inputText" type="text" name="usr_job_title" style="width:{$field_width}px;" size="27" maxlength="255">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<input type="hidden" name="usr_job_title_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_job_title" value="{$lab_job_title}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!--usr_job_title_read-->
	<xsl:template name="usr_job_title_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_job_title"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="$element_value!=''">
							<xsl:value-of select="$element_value"/>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_attributes_read -->
	<xsl:template name="usr_attributes_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:param name="title"/>
		<xsl:param name="attribute_type"/>
		<xsl:param name="hidden_field_value"/>
		<xsl:if test="$showempty = 'true' or count($user/user_attribute_list/attribute_list[@type = $attribute_type]/entity[@relation_type = $hidden_field_value]) != 0">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$title"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="count($user/user_attribute_list/attribute_list[@type = $attribute_type]/entity[@relation_type = $hidden_field_value]) &gt;0">
							<xsl:for-each select="$user/user_attribute_list/attribute_list[@type = $attribute_type]/entity[@relation_type = $hidden_field_value]">
								<xsl:choose>
									<xsl:when test="$attribute_type = 'USG'">
										<!--<a href="javascript:usr.user.manage_usg_popup({../../../@ent_id}, {@id})" class="Text">-->
										<xsl:choose>
											<xsl:when test="$user/full_path and $user/full_path != ''">
												<xsl:value-of select="$user/full_path"/>
											</xsl:when>
											<xsl:when test="@display_bil and @display_bil != ''">
												<xsl:value-of select="@display_bil"/>
											</xsl:when>
											<xsl:otherwise>--</xsl:otherwise>
										</xsl:choose>
										<!--</a>-->
									</xsl:when>
									<xsl:otherwise>
										<xsl:choose>
											<xsl:when test="@display_bil and @display_bil != ''">
												<xsl:value-of select="@display_bil"/>
											</xsl:when>
											<xsl:otherwise>--</xsl:otherwise>
										</xsl:choose>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:if test="position() != last()">
									<xsl:text>,&#160;</xsl:text>
								</xsl:if>
							</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>--</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_attributes -->
	<xsl:template name="usr_attributes">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="attribute_type"/>
		<xsl:param name="title"/>
		<xsl:param name="frm">document.frmXml</xsl:param>
		<xsl:param name="width" select="$field_width"/>
		<xsl:param name="field_name"/>
		<xsl:param name="tree_type"/>
		<xsl:param name="tree_subtype"/>
		<xsl:param name="select_type">1</xsl:param>
		<xsl:param name="box_size">3</xsl:param>
		<xsl:param name="pick_root"/>
		<xsl:param name="option_list"/>
		<xsl:param name="hidden_field"/>
		<xsl:param name="hidden_field_value"/>
		<xsl:param name="override_appr_usg">0</xsl:param>
		<xsl:param name="attribute_list_node"/>
		<xsl:param name="show_not_syn_option">false</xsl:param>
		<xsl:param name="not_syn_gpm_type"/>
		<xsl:variable name="entity_text" select="$user_attribute_list/attribute_list[@type = $attribute_type]/entity[@relation_type = $hidden_field_value]/@display_bil"/>
		<xsl:variable name="entity_id" select="$user_attribute_list/attribute_list[@type = $attribute_type]/entity[@relation_type = $hidden_field_value]/@id"/>
		<tr>
			<td class="wzb-form-label">
				<xsl:if test="$required_field = 'true'">
					<span class="wzb-form-star">*</span>
				</xsl:if>
				<xsl:value-of select="$title"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<table>
					<tr>
						<td>
							<xsl:variable name="pick_leave">1</xsl:variable>
							<xsl:variable name="approved_list"/>
							<xsl:variable name="flag"/>
							<xsl:variable name="close_option">1</xsl:variable>
							<xsl:variable name="get_supervise_group">0</xsl:variable>
							<xsl:variable name="complusory_tree">1</xsl:variable>
							<xsl:variable name="get_direct_supervise">0</xsl:variable>
							<!-- UDPATE MODE -->
							<xsl:choose>
								<xsl:when test="$entity_text = '' and $entity_id = '' and $option_list = '' and ($attribute_type = 'UGR' or $attribute_type = 'IDC')">
									<xsl:choose>
										<xsl:when test="$box_size = '1'">
											<xsl:call-template name="wb_goldenman">
												<xsl:with-param name="frm" select="$frm"/>
												<xsl:with-param name="width" select="$width"/>
												<xsl:with-param name="field_name" select="$field_name"/>
												<xsl:with-param name="tree_type" select="$tree_type"/>
												<xsl:with-param name="tree_subtype" select="$tree_subtype"/>
												<xsl:with-param name="select_type" select="$select_type"/>
												<xsl:with-param name="box_size">1</xsl:with-param>
												<xsl:with-param name="pick_root" select="$pick_root"/>
												<xsl:with-param name="override_appr_usg" select="$override_appr_usg"/>
												<xsl:with-param name="add_function">
													<xsl:choose>
														<xsl:when test="$attribute_type = 'USG'">usr.user.update_usg_confirm_add(document.frmXml, '<xsl:value-of select="$wb_lang"/>', '<xsl:value-of select="$tree_type"/>',<xsl:value-of select="$select_type"/>,'<xsl:value-of select="$field_name"/>','','<xsl:value-of select="$pick_leave"/>','<xsl:value-of select="$approved_list"/>','<xsl:value-of select="$flag"/>','<xsl:value-of select="$close_option"/>','<xsl:value-of select="$pick_root"/>', '<xsl:value-of select="$override_appr_usg"/>', '<xsl:value-of select="$tree_subtype"/>', '<xsl:value-of select="$get_supervise_group"/>','<xsl:value-of select="$complusory_tree"/>', '<xsl:value-of select="$get_direct_supervise"/>', '<xsl:value-of select="$filter_user_group"/>')</xsl:when>
														<xsl:otherwise>goldenman.opentree('<xsl:value-of select="$tree_type"/>',<xsl:value-of select="$select_type"/>,'<xsl:value-of select="$field_name"/>','','<xsl:value-of select="$pick_leave"/>','<xsl:value-of select="$approved_list"/>','<xsl:value-of select="$flag"/>','<xsl:value-of select="$close_option"/>','<xsl:value-of select="$pick_root"/>', '<xsl:value-of select="$override_appr_usg"/>', '<xsl:value-of select="$tree_subtype"/>', '<xsl:value-of select="$get_supervise_group"/>','<xsl:value-of select="$complusory_tree"/>', '<xsl:value-of select="$get_direct_supervise"/>')</xsl:otherwise>
													</xsl:choose>
												</xsl:with-param>
												<xsl:with-param name="remove_function">
													<xsl:if test="$attribute_type='USG'">usr.user.update_usg_confirm_remove('<xsl:value-of select="$frm"/>','<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$field_name"/>')</xsl:if>
												</xsl:with-param>
											</xsl:call-template>
										</xsl:when>
										<xsl:otherwise>
											<xsl:call-template name="wb_goldenman">
												<xsl:with-param name="frm" select="$frm"/>
												<xsl:with-param name="width" select="$width"/>
												<xsl:with-param name="field_name" select="$field_name"/>
												<xsl:with-param name="tree_type" select="$tree_type"/>
												<xsl:with-param name="tree_subtype" select="$tree_subtype"/>
												<xsl:with-param name="select_type" select="$select_type"/>
												<xsl:with-param name="box_size" select="$box_size"/>
												<xsl:with-param name="pick_root" select="$pick_root"/>
												<xsl:with-param name="option_list"/>
												<xsl:with-param name="override_appr_usg" select="$override_appr_usg"/>
												<xsl:with-param name="add_function">
													<xsl:choose>
														<xsl:when test="$attribute_type = 'USG'">usr.user.update_usg_confirm_add(document.frmXml, '<xsl:value-of select="$wb_lang"/>', '<xsl:value-of select="$tree_type"/>',<xsl:value-of select="$select_type"/>,'<xsl:value-of select="$field_name"/>','','<xsl:value-of select="$pick_leave"/>','<xsl:value-of select="$approved_list"/>','<xsl:value-of select="$flag"/>','<xsl:value-of select="$close_option"/>','<xsl:value-of select="$pick_root"/>', '<xsl:value-of select="$override_appr_usg"/>', '<xsl:value-of select="$tree_subtype"/>', '<xsl:value-of select="$get_supervise_group"/>','<xsl:value-of select="$complusory_tree"/>', '<xsl:value-of select="$get_direct_supervise"/>', '<xsl:value-of select="$filter_user_group"/>')</xsl:when>
														<xsl:otherwise>goldenman.opentree('<xsl:value-of select="$tree_type"/>',<xsl:value-of select="$select_type"/>,'<xsl:value-of select="$field_name"/>','','<xsl:value-of select="$pick_leave"/>','<xsl:value-of select="$approved_list"/>','<xsl:value-of select="$flag"/>','<xsl:value-of select="$close_option"/>','<xsl:value-of select="$pick_root"/>', '<xsl:value-of select="$override_appr_usg"/>', '<xsl:value-of select="$tree_subtype"/>', '<xsl:value-of select="$get_supervise_group"/>','<xsl:value-of select="$complusory_tree"/>', '<xsl:value-of select="$get_direct_supervise"/>')</xsl:otherwise>
													</xsl:choose>
												</xsl:with-param>
												<xsl:with-param name="remove_function">
													<xsl:if test="$attribute_type='USG'">usr.user.update_usg_confirm_remove('<xsl:value-of select="$frm"/>','<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$field_name"/>')</xsl:if>
												</xsl:with-param>
											</xsl:call-template>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:when>
								<xsl:when test="$entity_text != '' and $entity_id != ''">
									<xsl:call-template name="wb_goldenman">
										<xsl:with-param name="frm" select="$frm"/>
										<xsl:with-param name="width" select="$width"/>
										<xsl:with-param name="field_name" select="$field_name"/>
										<xsl:with-param name="tree_type" select="$tree_type"/>
										<xsl:with-param name="tree_subtype" select="$tree_subtype"/>
										<xsl:with-param name="select_type" select="$select_type"/>
										<xsl:with-param name="box_size">1</xsl:with-param>
										<xsl:with-param name="single_option_text" select="$entity_text"/>
										<xsl:with-param name="single_option_value" select="$entity_id"/>
										<xsl:with-param name="pick_root" select="$pick_root"/>
										<xsl:with-param name="override_appr_usg" select="$override_appr_usg"/>
										<xsl:with-param name="add_function">
											<xsl:choose>
												<xsl:when test="$attribute_type = 'USG'">usr.user.update_usg_confirm_add(document.frmXml, '<xsl:value-of select="$wb_lang"/>', '<xsl:value-of select="$tree_type"/>',<xsl:value-of select="$select_type"/>,'<xsl:value-of select="$field_name"/>','','<xsl:value-of select="$pick_leave"/>','<xsl:value-of select="$approved_list"/>','<xsl:value-of select="$flag"/>','<xsl:value-of select="$close_option"/>','<xsl:value-of select="$pick_root"/>', '<xsl:value-of select="$override_appr_usg"/>', '<xsl:value-of select="$tree_subtype"/>', '<xsl:value-of select="$get_supervise_group"/>','<xsl:value-of select="$complusory_tree"/>', '<xsl:value-of select="$get_direct_supervise"/>', '<xsl:value-of select="$filter_user_group"/>')</xsl:when>
												<xsl:otherwise>goldenman.opentree('<xsl:value-of select="$tree_type"/>',<xsl:value-of select="$select_type"/>,'<xsl:value-of select="$field_name"/>','','<xsl:value-of select="$pick_leave"/>','<xsl:value-of select="$approved_list"/>','<xsl:value-of select="$flag"/>','<xsl:value-of select="$close_option"/>','<xsl:value-of select="$pick_root"/>', '<xsl:value-of select="$override_appr_usg"/>', '<xsl:value-of select="$tree_subtype"/>', '<xsl:value-of select="$get_supervise_group"/>','<xsl:value-of select="$complusory_tree"/>', '<xsl:value-of select="$get_direct_supervise"/>')</xsl:otherwise>
											</xsl:choose>
										</xsl:with-param>
										<xsl:with-param name="remove_function">
											<xsl:if test="$attribute_type='USG'">usr.user.update_usg_confirm_remove('<xsl:value-of select="$frm"/>','<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$field_name"/>')</xsl:if>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="wb_goldenman">
										<xsl:with-param name="frm" select="$frm"/>
										<xsl:with-param name="width" select="$width"/>
										<xsl:with-param name="field_name" select="$field_name"/>
										<xsl:with-param name="tree_type" select="$tree_type"/>
										<xsl:with-param name="tree_subtype" select="$tree_subtype"/>
										<xsl:with-param name="select_type" select="$select_type"/>
										<xsl:with-param name="box_size" select="$box_size"/>
										<xsl:with-param name="pick_root" select="$pick_root"/>
										<xsl:with-param name="single_option_text">
											<xsl:if test="$attribute_type = 'USG'">
												<xsl:value-of select="$group_text"/>
											</xsl:if>
										</xsl:with-param>
										<xsl:with-param name="single_option_value">
											<xsl:if test="$attribute_type = 'USG'">
												<xsl:value-of select="$group_value"/>
											</xsl:if>
										</xsl:with-param>
										<xsl:with-param name="override_appr_usg" select="$override_appr_usg"/>
										<xsl:with-param name="add_function">
											<xsl:choose>
												<xsl:when test="$attribute_type = 'USG'">usr.user.update_usg_confirm_add(document.frmXml, '<xsl:value-of select="$wb_lang"/>', '<xsl:value-of select="$tree_type"/>',<xsl:value-of select="$select_type"/>,'<xsl:value-of select="$field_name"/>','','<xsl:value-of select="$pick_leave"/>','<xsl:value-of select="$approved_list"/>','<xsl:value-of select="$flag"/>','<xsl:value-of select="$close_option"/>','<xsl:value-of select="$pick_root"/>', '<xsl:value-of select="$override_appr_usg"/>', '<xsl:value-of select="$tree_subtype"/>', '<xsl:value-of select="$get_supervise_group"/>','<xsl:value-of select="$complusory_tree"/>', '<xsl:value-of select="$get_direct_supervise"/>', '<xsl:value-of select="$filter_user_group"/>')</xsl:when>
												<xsl:otherwise>goldenman.opentree('<xsl:value-of select="$tree_type"/>',<xsl:value-of select="$select_type"/>,'<xsl:value-of select="$field_name"/>','','<xsl:value-of select="$pick_leave"/>','<xsl:value-of select="$approved_list"/>','<xsl:value-of select="$flag"/>','<xsl:value-of select="$close_option"/>','<xsl:value-of select="$pick_root"/>', '<xsl:value-of select="$override_appr_usg"/>', '<xsl:value-of select="$tree_subtype"/>', '<xsl:value-of select="$get_supervise_group"/>','<xsl:value-of select="$complusory_tree"/>', '<xsl:value-of select="$get_direct_supervise"/>')</xsl:otherwise>
											</xsl:choose>
										</xsl:with-param>
										<xsl:with-param name="remove_function">
											<xsl:if test="$attribute_type='USG'">usr.user.update_usg_confirm_remove('<xsl:value-of select="$frm"/>','<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$field_name"/>')</xsl:if>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
							<input type="hidden" name="{$hidden_field}" value="{$hidden_field_value}"/>
							<input type="hidden" name="{$field_name}_req_fld">
								<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
							</input>
						</td>
					</tr>
					<xsl:if test="$show_not_syn_option = 'true'">
						<tr>
							<td>
								<input name="not_syn_gpm_type" type="checkbox" value="{$attribute_type}" id="not_syn_gpm_type{$attribute_type}">
									<xsl:if test="contains($not_syn_gpm_type, $attribute_type)">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
								<xsl:value-of select="$lab_not_syn"/>
							</td>
						</tr>
					</xsl:if>
				</table>
			</td>
		</tr>
		<input type="hidden">
			<xsl:choose>
				<xsl:when test="$attribute_type = 'USG'">
					<xsl:attribute name="name">lab_group</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="$lab_group"/></xsl:attribute>
				</xsl:when>
				<xsl:when test="$attribute_type = 'UGR'">
					<xsl:attribute name="name">lab_grade</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="$lab_grade"/></xsl:attribute>
				</xsl:when>
			</xsl:choose>
		</input>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_direct_supervisors -->
	<xsl:template name="usr_direct_supervisors">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="element_value"/>
		<xsl:param name="frm">document.frmXml</xsl:param>
		<xsl:param name="width">300</xsl:param>
		<xsl:param name="field_name"/>
		<xsl:param name="tree_type"/>
		<xsl:param name="tree_subtype"/>
		<xsl:param name="select_type">1</xsl:param>
		<xsl:param name="box_size">3</xsl:param>
		<xsl:param name="pick_root"/>
		<xsl:param name="hidden_field"/>
		<tr>
			<td class="wzb-form-label" valign="top">
				<xsl:if test="$required_field = 'true'">
					<xsl:text>*</xsl:text>
				</xsl:if>
				<xsl:value-of select="$lab_direct_supervisors"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<xsl:call-template name="wb_goldenman">
					<xsl:with-param name="frm" select="$frm"/>
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="field_name" select="$field_name"/>
					<xsl:with-param name="tree_type" select="$tree_type"/>
					<xsl:with-param name="select_type" select="$select_type"/>
					<xsl:with-param name="box_size" select="$box_size"/>
					<xsl:with-param name="pick_root">0</xsl:with-param>
					<xsl:with-param name="search">true</xsl:with-param>
					<xsl:with-param name="search_function">javascript:usr.search.popup_search_prep('<xsl:value-of select="$field_name"/>','','<xsl:value-of select="$root_ent_id"/>', '0', '', '', '0', '0', '', 'usr_supervisor_search.xsl')</xsl:with-param>
					<xsl:with-param name="option_list">
						<xsl:for-each select="$direct_supervisor/entity">
							<option value="{@id}">
								<xsl:value-of select="@display_bil"/>
								<!--<xsl:if test="@status  != 'OK'">&#160;(<xsl:value-of select="$lab_resigned"/>)</xsl:if>-->
							</option>
						</xsl:for-each>
					</xsl:with-param>
				</xsl:call-template>
				<input type="hidden" name="direct_supervisor_start_lst">
					<xsl:attribute name="value"><xsl:for-each select="$direct_supervisor/entity"><xsl:value-of select="@eff_start_date"/><xsl:if test="position()!=last()">~</xsl:if></xsl:for-each></xsl:attribute>
				</input>
				<input type="hidden" name="direct_supervisor_end_lst">
					<xsl:attribute name="value"><xsl:for-each select="$direct_supervisor/entity"><xsl:value-of select="@eff_end_date"/><xsl:if test="position()!=last()">~</xsl:if></xsl:for-each></xsl:attribute>
				</input>
				<input type="hidden" name="{$hidden_field}" value=""/>
				<input type="hidden" name="direct_supervisor_ent_lst_reg_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
				<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript"><![CDATA[
					function getPopupUsrLst(fld_name,id_lst,nm_lst, usr_argv){
						eval(']]><xsl:value-of select="$field_name"/><![CDATA[("' + usr_argv + '")');
						return;
					}
					]]></SCRIPT>
			</td>
		</tr>
		<input type="hidden" name="lab_direct_supervisors" value="{$lab_direct_supervisors}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!--usr_direct_supervisors_read-->
	<xsl:template name="usr_direct_supervisors_read">
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or count($direct_supervisor/entity) != 0">
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_direct_supervisors"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="count($direct_supervisor/entity) != 0">
							<xsl:for-each select="$direct_supervisor/entity">
								<xsl:choose>
									<xsl:when test="@type = 'USG'">
										<!--<a href="javascript:usr.user.manage_usg_popup({../../../@ent_id}, {@id})" class="Text">-->
										<xsl:value-of select="@display_bil"/>
										<!--</a>-->
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="@display_bil"/>
										<!--<xsl:if test="@status  != 'OK'">&#160;(<xsl:value-of select="$lab_resigned"/>)</xsl:if>-->
									</xsl:otherwise>
								</xsl:choose>
								<xsl:if test="position() != last()">
									<xsl:text>,&#160;</xsl:text>
								</xsl:if>
							</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!--app_approval_usg_ent_id-->
	<xsl:template name="app_approval_usg">
		<xsl:param name="element_value"/>
		<xsl:param name="frm">document.frmXml</xsl:param>
		<xsl:param name="width">300</xsl:param>
		<xsl:param name="field_name"/>
		<xsl:param name="select_type">1</xsl:param>
		<xsl:param name="box_size">3</xsl:param>
		<xsl:param name="showempty"/>
		<tr>
			<td class="wzb-form-label" valign="top">
				<xsl:value-of select="$lab_usr_app_approval_usg"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<table>
					<tr>
						<td>
							<xsl:call-template name="wb_goldenman">
								<xsl:with-param name="frm" select="$frm"/>
								<xsl:with-param name="width" select="$width"/>
								<xsl:with-param name="name">approval_usg</xsl:with-param>
								<xsl:with-param name="field_name" select="$field_name"/>
								<xsl:with-param name="select_type" select="$select_type"/>
								<xsl:with-param name="box_size">1</xsl:with-param>
								<xsl:with-param name="add_function">usr.user.usr_ancestor_usg_lst_popup(document.frmXml.usr_group_lst.options[0].value)</xsl:with-param>
								<xsl:with-param name="extra_remove_function">document.frmXml.usr_app_approval_usg_ent_id.value=''</xsl:with-param>
								<xsl:with-param name="single_option_text">
									<xsl:value-of select="$app_approval_usg/@display_bil"/>
								</xsl:with-param>
								<xsl:with-param name="single_option_value">
									<xsl:value-of select="$app_approval_usg/@ent_id"/>
								</xsl:with-param>
								<xsl:with-param name="single_option_desc">
									<xsl:copy-of select="$lab_usr_app_approval_usg_desc"/>
								</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
				</table>
				<input type="hidden" name="usr_app_approval_usg_ent_id" value="{$app_approval_usg/@ent_id}"/>
			</td>
		</tr>
	</xsl:template>
	<!-- ======================================================================== -->
	<!--app_approval_usg_ent_id_read-->
	<xsl:template name="app_approval_usg_read">
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $app_approval_usg/@display_bil!=''">
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_usr_app_approval_usg"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="$app_approval_usg/@display_bil!=''">
							<xsl:value-of select="$app_approval_usg/@display_bil"/>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_join_date -->
	<xsl:template name="usr_join_date">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label">
				<xsl:if test="$required_field = 'true'">
					<span class="wzb-form-star">*</span>
				</xsl:if>
				<xsl:value-of select="$lab_join_date"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<xsl:call-template name="display_form_input_time">
					<xsl:with-param name="fld_name">join_date</xsl:with-param>
					<xsl:with-param name="hidden_fld_name">usr_join_date</xsl:with-param>
					<xsl:with-param name="frm">document.frmXml</xsl:with-param>
					<xsl:with-param name="timestamp" select="$element_value"/>
				</xsl:call-template>
				<input type="hidden" name="usr_join_date_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_join_date" value="{$lab_join_date}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!--usr_join_date_read-->
	<xsl:template name="usr_join_date_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_join_date"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="$element_value!=''">
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="$element_value"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_role_lst -->
	<xsl:template name="usr_role_lst">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="read_only">false</xsl:param>
		<tr>
			<td class="wzb-form-label" valign="top">
				<xsl:if test="$required_field = 'true'">
					<span class="wzb-form-star">*</span>
				</xsl:if>
				<xsl:value-of select="$lab_role"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<!-- EDIT MODE -->
				<table cellpadding="0" cellspacing="0" border="0">
					<xsl:apply-templates select="$auth_role_list/role[not(entity_assignment)]" mode="non-ent-assign">
						<xsl:with-param name="tableLeftText" select="$tableLeftText"/>
						<xsl:with-param name="tableRightText" select="$tableRightText"/>
						<xsl:with-param name="auth_role_count" select="count($auth_role_list/role)"/>
					</xsl:apply-templates>
				</table>
				<table cellpadding="0" cellspacing="0" border="0">
					<xsl:apply-templates select="$auth_role_list/role[contains(entity_assignment/@type,'USG')]" mode="ent-assign">
						<xsl:with-param name="tableLeftText" select="$tableLeftText"/>
						<xsl:with-param name="tableRightText" select="$tableRightText"/>
						<xsl:with-param name="lab_to" select="$lab_to"/>
						<xsl:with-param name="lab_all_time" select="$lab_all_time"/>
						<xsl:with-param name="auth_role_count">
							<xsl:value-of select="count($auth_role_list/role)"/>
						</xsl:with-param>
					</xsl:apply-templates>
				</table>
				<input type="hidden" name="usr_role_lst" value=""/>
				<input type="hidden" name="usr_role_start_lst" value=""/>
				<input type="hidden" name="usr_role_end_lst" value=""/>
				<input type="hidden" name="usr_role_appr_lst">
					<xsl:attribute name="value"><xsl:for-each select="$role_list/role[entity_assignment/@type = 'USR']"><xsl:value-of select="@id"/>~</xsl:for-each></xsl:attribute>
				</input>
				<input type="hidden" name="usr_role_appr_start_lst">
					<xsl:attribute name="value"><xsl:for-each select="$role_list/role[entity_assignment/@type = 'USR']">IMMEDIATE~</xsl:for-each></xsl:attribute>
				</input>
				<input type="hidden" name="usr_role_appr_end_lst">
					<xsl:attribute name="value"><xsl:for-each select="$role_list/role[entity_assignment/@type = 'USR']">UNLIMITED~</xsl:for-each></xsl:attribute>
				</input>
				<input type="hidden" name="usr_role_lst_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
				<input type="hidden" name="entity_assignment_cnt" value="{count($all_role_list/role/entity_assignment)}"/>
				<input type="hidden" name="rol_target_ext_id_lst" value=""/>
				<input type="hidden" name="rol_target_ent_group_lst" value=""/>
				<input type="hidden" name="rol_target_start_lst" value=""/>
				<input type="hidden" name="rol_target_end_lst" value=""/>
			</td>
		</tr>
		<input type="hidden" name="lab_role" value="{$lab_role}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_role_lst -->
	<xsl:template name="usr_role_lst_read">
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or count($role_list/role) !=0">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_role"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="count($role_list/role) !=0">
							<xsl:for-each select="$role_list/role">
								<xsl:variable name="cur_role_id">
									<xsl:value-of select="@id"/>
								</xsl:variable>
								<xsl:call-template name="get_rol_title"/>
								<xsl:for-each select="$target_list/target_group_list[@role_id = $cur_role_id]/target_group">
									<xsl:if test="position() = 1">
										<xsl:text>(</xsl:text>
									</xsl:if>
									<xsl:for-each select="entity">
										<xsl:value-of select="@display_bil"/>
										<xsl:if test="position() != last()">
											<xsl:text>/</xsl:text>
										</xsl:if>
									</xsl:for-each>
									<xsl:if test="position() != last()">
										<xsl:text>, </xsl:text>
									</xsl:if>
									<xsl:if test="position() = last()">
										<xsl:text>)</xsl:text>
									</xsl:if>
								</xsl:for-each>
								<xsl:if test="position() != last()">,&#160;</xsl:if>
							</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template name="usr_source">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label">
				<xsl:if test="$required_field='true'">
					<xsl:text>*</xsl:text>
				</xsl:if>
				<xsl:value-of select="$lab_usr_source"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<xsl:call-template name="gen_drop_down_box">
					<xsl:with-param name="name">usr_source</xsl:with-param>
					<xsl:with-param name="options" select="$profile_attributes/source/option_list"/>
					<xsl:with-param name="default" select="$element_value"/>
				</xsl:call-template>
				<input type="hidden" name="usr_source_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_usr_source" value="{$lab_usr_source}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_source_read_mode  -->
	<xsl:template name="usr_source_read">
		<xsl:param name="showempty"/>
		<xsl:param name="element_value"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_usr_source"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test='$element_value=""'>
							<xsl:text>--</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="$profile_attributes/source/option_list/option[@id=$element_value]">
									<xsl:value-of select="$profile_attributes/source/option_list/option[@id=$element_value]/label[@xml:lang = $cur_lang]"/>
								</xsl:when>
								<xsl:otherwise>--</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_1 -->
	<xsl:template name="usr_extra_1">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="max_length">255</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_1"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<input type="text" name="usr_extra_1" style="width:{$field_width}px;" size="27" maxlength="{$max_length}" class="wzb-inputText">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<input type="hidden" name="usr_extra_1_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_1" value="{$lab_extra_1}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_1 -->
	<xsl:template name="usr_extra_1_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_1"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value!=''">
								<xsl:value-of select="$element_value"/>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_2 -->
	<xsl:template name="usr_extra_2">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="max_length">255</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_extra_2"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<input type="text" name="usr_extra_2" style="width:{$field_width}px;" size="27" maxlength="{$max_length}" class="wzb-inputText">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<input type="hidden" name="usr_extra_2_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_2" value="{$lab_extra_2}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_2_read-->
	<xsl:template name="usr_extra_2_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_2"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value!=''">
								<xsl:value-of select="$element_value"/>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_3 -->
	<xsl:template name="usr_extra_3">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="max_length">50</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_extra_3"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<input type="text" name="usr_extra_3" style="width:{$field_width}px;" size="27" maxlength="{$max_length}" class="wzb-inputText">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<input type="hidden" name="usr_extra_3_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_3" value="{$lab_extra_3}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_3_read-->
	<xsl:template name="usr_extra_3_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_3"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value!=''">
								<xsl:value-of select="$element_value"/>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_4 -->
	<xsl:template name="usr_extra_4">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="max_length">50</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_extra_4"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<input type="text" name="usr_extra_4" style="width:{$field_width}px;" size="27" maxlength="{$max_length}" class="wzb-inputText">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<input type="hidden" name="usr_extra_4_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_4" value="{$lab_extra_4}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_4_read-->
	<xsl:template name="usr_extra_4_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_4"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value!=''">
								<xsl:value-of select="$element_value"/>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_5 -->
	<xsl:template name="usr_extra_5">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="max_length">50</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_extra_5"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<input type="text" name="usr_extra_5" style="width:{$field_width}px;" size="27" maxlength="{$max_length}" class="wzb-inputText">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<input type="hidden" name="usr_extra_5_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_5" value="{$lab_extra_5}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_5_read-->
	<xsl:template name="usr_extra_5_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_5"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value!=''">
								<xsl:value-of select="$element_value"/>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_6 -->
	<xsl:template name="usr_extra_6">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="max_length">50</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_extra_6"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<input type="text" name="usr_extra_6" style="width:{$field_width}px;" size="27" maxlength="{$max_length}" class="wzb-inputText">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<input type="hidden" name="usr_extra_6_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_6" value="{$lab_extra_6}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_6_read-->
	<xsl:template name="usr_extra_6_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_6"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value!=''">
								<xsl:value-of select="$element_value"/>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_7 -->
	<xsl:template name="usr_extra_7">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="max_length">50</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_7"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<input type="text" name="usr_extra_7" style="width:{$field_width}px;" size="27" maxlength="{$max_length}" class="wzb-inputText">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<input type="hidden" name="usr_extra_7_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_7" value="{$lab_extra_7}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_7_read-->
	<xsl:template name="usr_extra_7_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_7"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value!=''">
								<xsl:value-of select="$element_value"/>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_8 -->
	<xsl:template name="usr_extra_8">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="max_length">50</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_extra_8"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<input type="text" name="usr_extra_8" style="width:{$field_width}px;" size="27" maxlength="{$max_length}" class="wzb-inputText">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<input type="hidden" name="usr_extra_8_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_8" value="{$lab_extra_8}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_8_read-->
	<xsl:template name="usr_extra_8_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_8"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value!=''">
								<xsl:value-of select="$element_value"/>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_9 -->
	<xsl:template name="usr_extra_9">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="max_length">50</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_extra_9"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<input type="text" name="usr_extra_9" style="width:{$field_width}px;" size="27" maxlength="{$max_length}" class="wzb-inputText">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<input type="hidden" name="usr_extra_9_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_9" value="{$lab_extra_9}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_9_read-->
	<xsl:template name="usr_extra_9_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_9"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value!=''">
								<xsl:value-of select="$element_value"/>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_10 -->
	<xsl:template name="usr_extra_10">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="max_length">50</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_extra_10"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<input type="text" name="usr_extra_10" style="width:{$field_width}px;" size="27" maxlength="{$max_length}" class="wzb-inputText">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<input type="hidden" name="usr_extra_10_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_10" value="{$lab_extra_10}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_10_read-->
	<xsl:template name="usr_extra_10_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_10"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value!=''">
								<xsl:value-of select="$element_value"/>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_11 -->
	<xsl:template name="usr_extra_datetime_11">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label">
				<xsl:if test="$required_field = 'true'">
					<span class="wzb-form-star">*</span>
				</xsl:if>
				<xsl:value-of select="$lab_extra_datetime_11"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<xsl:call-template name="display_form_input_time">
					<xsl:with-param name="fld_name">extra_datetime_11</xsl:with-param>
					<xsl:with-param name="hidden_fld_name">usr_extra_datetime_11</xsl:with-param>
					<xsl:with-param name="frm">document.frmXml</xsl:with-param>
					<xsl:with-param name="timestamp">
						<xsl:value-of select="$element_value"/>
					</xsl:with-param>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_datetime_11_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_datetime_11" value="{$lab_extra_datetime_11}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_11_read -->
	<xsl:template name="usr_extra_datetime_11_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_extra_datetime_11"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="$element_value"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_12 -->
	<xsl:template name="usr_extra_datetime_12">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_extra_datetime_12"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<span class="{$tableRightText}">
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">extra_datetime_12</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">usr_extra_datetime_12</xsl:with-param>
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						<xsl:with-param name="timestamp">
							<xsl:value-of select="$element_value"/>
						</xsl:with-param>
					</xsl:call-template>
				</span>
				<input type="hidden" name="usr_extra_datetime_12_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_datetime_12" value="{$lab_extra_datetime_12}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_12_read -->
	<xsl:template name="usr_extra_datetime_12_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_datetime_12"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value != ''">
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="$element_value"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_13 -->
	<xsl:template name="usr_extra_datetime_13">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_extra_datetime_13"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<span class="{$tableRightText}">
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">extra_datetime_13</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">usr_extra_datetime_13</xsl:with-param>
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						<xsl:with-param name="timestamp">
							<xsl:value-of select="$element_value"/>
						</xsl:with-param>
					</xsl:call-template>
				</span>
				<input type="hidden" name="usr_extra_datetime_13_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_datetime_13" value="{$lab_extra_datetime_13}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_13_read -->
	<xsl:template name="usr_extra_datetime_13_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_datetime_13"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value != ''">
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="$element_value"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_14 -->
	<xsl:template name="usr_extra_datetime_14">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_extra_datetime_14"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<span class="{$tableRightText}">
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">extra_datetime_14</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">usr_extra_datetime_14</xsl:with-param>
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						<xsl:with-param name="timestamp">
							<xsl:value-of select="$element_value"/>
						</xsl:with-param>
					</xsl:call-template>
				</span>
				<input type="hidden" name="usr_extra_datetime_14_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_datetime_14" value="{$lab_extra_datetime_14}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_14_read -->
	<xsl:template name="usr_extra_datetime_14_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_datetime_14"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value != ''">
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="$element_value"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_15 -->
	<xsl:template name="usr_extra_datetime_15">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_extra_datetime_15"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<span class="{$tableRightText}">
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">extra_datetime_15</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">usr_extra_datetime_15</xsl:with-param>
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						<xsl:with-param name="timestamp">
							<xsl:value-of select="$element_value"/>
						</xsl:with-param>
					</xsl:call-template>
				</span>
				<input type="hidden" name="usr_extra_datetime_15_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_datetime_15" value="{$lab_extra_datetime_15}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_15_read -->
	<xsl:template name="usr_extra_datetime_15_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_datetime_15"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value != ''">
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="$element_value"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_16 -->
	<xsl:template name="usr_extra_datetime_16">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_extra_datetime_16"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<span class="{$tableRightText}">
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">extra_datetime_16</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">usr_extra_datetime_16</xsl:with-param>
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						<xsl:with-param name="timestamp">
							<xsl:value-of select="$element_value"/>
						</xsl:with-param>
					</xsl:call-template>
				</span>
				<input type="hidden" name="usr_extra_datetime_16_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_datetime_16" value="{$lab_extra_datetime_16}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_16_read -->
	<xsl:template name="usr_extra_datetime_16_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_datetime_16"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value != ''">
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="$element_value"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_17 -->
	<xsl:template name="usr_extra_datetime_17">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_extra_datetime_17"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<span class="{$tableRightText}">
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">extra_datetime_17</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">usr_extra_datetime_17</xsl:with-param>
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						<xsl:with-param name="timestamp">
							<xsl:value-of select="$element_value"/>
						</xsl:with-param>
					</xsl:call-template>
				</span>
				<input type="hidden" name="usr_extra_datetime_17_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_datetime_17" value="{$lab_extra_datetime_17}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_17_read -->
	<xsl:template name="usr_extra_datetime_17_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_datetime_17"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value != ''">
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="$element_value"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_18 -->
	<xsl:template name="usr_extra_datetime_18">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_extra_datetime_18"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<span class="{$tableRightText}">
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">extra_datetime_18</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">usr_extra_datetime_18</xsl:with-param>
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						<xsl:with-param name="timestamp">
							<xsl:value-of select="$element_value"/>
						</xsl:with-param>
					</xsl:call-template>
				</span>
				<input type="hidden" name="usr_extra_datetime_18_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_datetime_18" value="{$lab_extra_datetime_18}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_18_read -->
	<xsl:template name="usr_extra_datetime_18_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_datetime_18"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value != ''">
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="$element_value"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_19 -->
	<xsl:template name="usr_extra_datetime_19">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_extra_datetime_19"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<span class="{$tableRightText}">
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">extra_datetime_19</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">usr_extra_datetime_19</xsl:with-param>
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						<xsl:with-param name="timestamp">
							<xsl:value-of select="$element_value"/>
						</xsl:with-param>
					</xsl:call-template>
				</span>
				<input type="hidden" name="usr_extra_datetime_19_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_datetime_19" value="{$lab_extra_datetime_19}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_19_read -->
	<xsl:template name="usr_extra_datetime_19_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_datetime_19"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:if test="$element_value != ''">
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="$element_value"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:if>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_20 -->
	<xsl:template name="usr_extra_datetime_20">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<span class="wzb-form-star">*</span>
					</xsl:if>
					<xsl:value-of select="$lab_extra_datetime_20"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<span class="{$tableRightText}">
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">extra_datetime_20</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">usr_extra_datetime_20</xsl:with-param>
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						<xsl:with-param name="timestamp">
							<xsl:value-of select="$element_value"/>
						</xsl:with-param>
					</xsl:call-template>
				</span>
				<input type="hidden" name="usr_extra_datetime_20_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_datetime_20" value="{$lab_extra_datetime_20}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- usr_extra_datetime_20_read -->
	<xsl:template name="usr_extra_datetime_20_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true'">
			<tr>
				<td align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_datetime_20"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value != ''">
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp">
										<xsl:value-of select="$element_value"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template name="usr_detail_seperate_hdr">
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
			<xsl:call-template name="empty_row"/>
		</table>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template name="usr_detail_seperate_footer">
		<xsl:param name="show_required">true</xsl:param>
		  
		<table class="Bg" cellspacing="0" cellpadding="0" border="0" width="{$wb_gen_table_width}">
			<!-- <xsl:call-template name="empty_row"/> -->
			<xsl:if test="$show_required = 'true'">
				<tr>
					<td class="wzb-form-label">
					</td>
					<td class="wzb-ui-module-text">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
					</td>
				</tr>
			</xsl:if>
		</table>
	 
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template name="empty_row">
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
	<xsl:template name="usr_supervised_groups">
		<xsl:param name="required_field">true</xsl:param>
		<xsl:param name="element_value"/>
		<xsl:param name="frm">document.frmXml</xsl:param>
		<xsl:param name="width">300</xsl:param>
		<xsl:param name="field_name"/>
		<xsl:param name="tree_type"/>
		<xsl:param name="tree_subtype"/>
		<xsl:param name="select_type">1</xsl:param>
		<xsl:param name="box_size">3</xsl:param>
		<xsl:param name="pick_root"/>
		<xsl:param name="hidden_field"/>
		<tr>
			<td class="wzb-form-label" valign="top">
				<xsl:if test="$required_field = 'true'">
					<span class="wzb-form-star">*</span>
				</xsl:if>
				<xsl:value-of select="$lab_supervised_groups"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<xsl:call-template name="wb_goldenman">
					<xsl:with-param name="frm" select="$frm"/>
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="field_name" select="$field_name"/>
					<xsl:with-param name="tree_type" select="$tree_type"/>
					<xsl:with-param name="select_type" select="$select_type"/>
					<xsl:with-param name="box_size" select="$box_size"/>
					<xsl:with-param name="hidden_field" select="$hidden_field"/>
					<xsl:with-param name="option_list">
						<xsl:for-each select="$supervise_target_list/entity">
							<option value="{@id}">
								<xsl:call-template name="usg_display_bil">
									<xsl:with-param name="role" select="@usg_role"/>
									<xsl:with-param name="display_bil" select="@display_bil"/>
								</xsl:call-template>
							</option>
						</xsl:for-each>
					</xsl:with-param>
				</xsl:call-template>
				<table width="450" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td>
							<xsl:copy-of select="$lab_supervised_groups_desc"/>
						</td>
					</tr>
				</table>
				<input type="hidden" name="supervise_target_start_lst">
					<xsl:attribute name="value"><xsl:for-each select="$supervise_target_list/entity"><xsl:value-of select="@eff_start_date"/><xsl:if test="position()!=last()">~</xsl:if></xsl:for-each></xsl:attribute>
				</input>
				<input type="hidden" name="supervise_target_end_lst">
					<xsl:attribute name="value"><xsl:for-each select="$supervise_target_list/entity"><xsl:value-of select="@eff_end_date"/><xsl:if test="position()!=last()">~</xsl:if></xsl:for-each></xsl:attribute>
				</input>
				<input type="hidden" name="{$hidden_field}" value=""/>
				<input type="hidden" name="supervise_target_ent_lst_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_supervised_groups" value="{$lab_supervised_groups}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!--usr_supervised_groups_read-->
	<!-- ======================================================================== -->
	<xsl:template name="usr_supervised_groups_read">
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or count($supervise_target_list/entity) != 0">
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_supervised_groups"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="count($supervise_target_list/entity) != 0">
							<xsl:for-each select="$supervise_target_list/entity">
								<xsl:call-template name="usg_display_bil">
									<xsl:with-param name="role" select="@usg_role"/>
									<xsl:with-param name="display_bil" select="@display_bil"/>
								</xsl:call-template>
								<xsl:if test="position() != last()">
									<xsl:text>,&#160;</xsl:text>
								</xsl:if>
							</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="role" mode="non-ent-assign">
		<xsl:param name="auth_role_count"/>
		<xsl:variable name="cur_role">
			<xsl:value-of select="@id"/>
		</xsl:variable>
		<xsl:if test="not(position() mod 2 = 0)">
			<xsl:text disable-output-escaping="yes">&lt;tr&gt;</xsl:text>
		</xsl:if>
		
		<td style="width:30%">
			<!-- start multiple goldenman code -->
			<script language="JavaScript" type="text/JavaScript">
				<xsl:value-of select="$cur_role"/><![CDATA[_has_ent_assign = null]]></script>
			<!-- end multiple goldenman code-->
			<label for="usr_role_label{@id}">
				<span class="Text">
					<input type="checkbox" name="usr_role" value="{@id}" id="usr_role_label{@id}">
						<xsl:choose>
							<xsl:when test="$usr_role_lst">
								<xsl:if test="$usr_role_lst/role[@id = $cur_role]">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
								<!--
								<xsl:if test="$disable_role_list/role[@id = $cur_role]">
									<xsl:attribute name="disabled">1</xsl:attribute>
								</xsl:if>
								-->
							</xsl:when>
							<!--
							<xsl:when test="$disable_role_list/role[@id = $cur_role]">
								<xsl:attribute name="disabled">1</xsl:attribute>
							</xsl:when>
							-->
							<xsl:when test="$auth_role_count = '1'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:when>
						</xsl:choose>
						<xsl:if test="@id = 'INSTR_1'">
							<xsl:attribute name="disabled">1</xsl:attribute>
						</xsl:if>
					</input>
				</span>
				<span class="{$tableRightText}">
					<xsl:text>&#160;</xsl:text>
					<xsl:call-template name="get_rol_title"/>
					<!-- 
					<xsl:choose>
						<xsl:when test="@name !=''">
							<xsl:value-of select="@name"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="get_rol_title"/>
						</xsl:otherwise>
					</xsl:choose>
					 -->
					<xsl:text>&#160;</xsl:text>
				</span>
			</label>
		</td>
		<xsl:choose>
			<xsl:when test="position() mod 2 = 0">
				<td style="width:30%"></td>
				<xsl:text disable-output-escaping="yes">&lt;/tr&gt;</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<!-- <td width="10px">
					<xsl:text>&#160;</xsl:text>
				</td> -->
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="role" mode="ent-assign">
		<!-- -->
		<xsl:variable name="cur_role">
			<xsl:value-of select="@id"/>
		</xsl:variable>
		<xsl:variable name="show_all_time_ele">
			<xsl:choose>
				<xsl:when test="count($usr_role_lst/role[@id = $cur_role][@eff_start_date!='IMMEDIATE'][eff_end_date!='UNLIMITED']) &gt;=1">true</xsl:when>
				<xsl:otherwise>false</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="tree_type">
			<xsl:choose>
				<xsl:when test="entity_assignment/@type = 'USR' or entity_assignment/@type = 'USR_OR_USG'">user_group_and_user</xsl:when>
				<xsl:when test="entity_assignment/@type = 'USG'">user_group</xsl:when>
				<xsl:otherwise>user_group_and_user</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="select_type">
			<xsl:choose>
				<xsl:when test="entity_assignment/@type = 'USR'">4</xsl:when>
				<xsl:when test="entity_assignment/@type = 'USG' or entity_assignment/@type = 'USR_OR_USG'">1</xsl:when>
				<xsl:otherwise>1</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="pos">
			<xsl:value-of select="position()"/>
		</xsl:variable>
		<tr>
			<td>
				<label for="usr_role_label{@id}">
					<span class="wbGenSelFrmNN">
						<input type="check" name="usr_role" value="{@id}" id="usr_role_label{@id}">
							<xsl:if test="$usr_role_lst/role[@id = $cur_role]">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
							<xsl:if test="$show_all_time_ele != 'true'">
								<xsl:attribute name="onclick">javascript:usr.user.unchk_remove_entity(document.frmXml,document.frmXml.entity_assignment_lst_<xsl:value-of select="$pos"/>,this.checked)</xsl:attribute>
							</xsl:if>
						</input>
					</span>
					<span class="{$tableRightText}">
						<xsl:text>&#160;</xsl:text>
						<xsl:call-template name="get_rol_title"/>
						<xsl:text>&#160;</xsl:text>
					</span>
				</label>
			</td>
		</tr>
		<tr>
			<td>
				<xsl:choose>
					<xsl:when test="$show_all_time_ele = 'true'">
						<span class="wbGenSelFrmNN">
							<xsl:variable name="pos">
								<xsl:value-of select="position()"/>
							</xsl:variable>
							<select class="wbGenSelFrmIE" name="multi_role_select_{$pos}" style="width:{$field_width}px;" onchange="save_list_value(document.frmXml.multi_role_select_{$pos}.selectedIndex,multiple_lst{$pos},document.frmXml.entity_assignment_lst_{$pos});show_list(document.frmXml.multi_role_select_{$pos}.selectedIndex,multiple_lst{$pos},document.frmXml.entity_assignment_lst_{$pos})">
								<xsl:if test="count($usr_role_full_lst/role[@id = $cur_role]) = 0">
									<option disabled="disabled" selected="selected">
										<xsl:value-of select="$lab_all_time"/>
									</option>
								</xsl:if>
								<xsl:for-each select="$usr_role_full_lst/role[@id = $cur_role]">
									<option>
										<xsl:if test="position() = 1">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:choose>
											<xsl:when test="@eff_start_date = 'IMMEDIATE' and @eff_end_date='UNLIMITED'">
												<xsl:value-of select="$lab_all_time"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="display_time">
													<xsl:with-param name="my_timestamp" select="@eff_start_date"/>
												</xsl:call-template>
										&#160;<xsl:value-of select="$lab_to"/>&#160;
										<xsl:call-template name="display_time">
													<xsl:with-param name="my_timestamp" select="@eff_end_date"/>
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
									</option>
								</xsl:for-each>
							</select>
						</span>
					</xsl:when>
					<xsl:otherwise/>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$show_all_time_ele = 'true'">
						<xsl:call-template name="wb_goldenman">
							<xsl:with-param name="frm">document.frmXml</xsl:with-param>
							<xsl:with-param name="width">
								<xsl:value-of select="$field_width"/>
							</xsl:with-param>
							<xsl:with-param name="field_name">entity_assignment_lst_<xsl:value-of select="position()"/>
							</xsl:with-param>
							<xsl:with-param name="tree_type">
								<xsl:value-of select="$tree_type"/>
							</xsl:with-param>
							<xsl:with-param name="select_type">
								<xsl:value-of select="$select_type"/>
							</xsl:with-param>
							<xsl:with-param name="box_size">3</xsl:with-param>
							<xsl:with-param name="extra_remove_function">save_list_value(document.frmXml.multi_role_select_<xsl:value-of select="$pos"/>.selectedIndex,multiple_lst<xsl:value-of select="$pos"/>,document.frmXml.entity_assignment_lst_<xsl:value-of select="$pos"/>);show_list(document.frmXml.multi_role_select_<xsl:value-of select="$pos"/>.selectedIndex,multiple_lst<xsl:value-of select="$pos"/>,document.frmXml.entity_assignment_lst_<xsl:value-of select="$pos"/>);</xsl:with-param>
							<xsl:with-param name="custom_js_code">document.all.usr_role_label<xsl:value-of select="@id"/>.checked=true;save_list_value(document.frmXml.multi_role_select_<xsl:value-of select="$pos"/>.selectedIndex,multiple_lst<xsl:value-of select="$pos"/>,document.frmXml.entity_assignment_lst_<xsl:value-of select="$pos"/>);show_list(document.frmXml.multi_role_select_<xsl:value-of select="$pos"/>.selectedIndex,multiple_lst<xsl:value-of select="$pos"/>,document.frmXml.entity_assignment_lst_<xsl:value-of select="$pos"/>);</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wb_goldenman">
							<xsl:with-param name="frm">document.frmXml</xsl:with-param>
							<xsl:with-param name="width">
								<xsl:value-of select="$field_width"/>
							</xsl:with-param>
							<xsl:with-param name="field_name">entity_assignment_lst_<xsl:value-of select="position()"/>
							</xsl:with-param>
							<xsl:with-param name="tree_type">
								<xsl:value-of select="$tree_type"/>
							</xsl:with-param>
							<xsl:with-param name="select_type">
								<xsl:value-of select="$select_type"/>
							</xsl:with-param>
							<xsl:with-param name="box_size">3</xsl:with-param>
							<xsl:with-param name="custom_js_code">document.all.usr_role_label<xsl:value-of select="@id"/>.checked=true;save_list_value(0,multiple_lst<xsl:value-of select="$pos"/>,document.frmXml.entity_assignment_lst_<xsl:value-of select="$pos"/>);show_list(0,multiple_lst<xsl:value-of select="$pos"/>,document.frmXml.entity_assignment_lst_<xsl:value-of select="$pos"/>);</xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
				<!-- start multiple goldenman code -->
				<script language="JavaScript">
					<xsl:variable name="pos">
						<xsl:value-of select="position()"/>
					</xsl:variable>
					<xsl:value-of select="$cur_role"/><![CDATA[_has_ent_assign = ]]><xsl:value-of select="$pos"/><![CDATA[multiple_lst]]><xsl:value-of select="$pos"/><![CDATA[ = new Array()]]><xsl:if test="count($usr_role_full_lst/role[@id = $cur_role]) = 0"><![CDATA[multiple_lst]]><xsl:value-of select="$pos"/><![CDATA[[0] = new Array()]]><![CDATA[multiple_lst]]><xsl:value-of select="$pos"/><![CDATA[.id = ']]><xsl:call-template name="escape_js">
							<xsl:with-param name="input_str" select="$cur_role"/>
						</xsl:call-template><![CDATA[']]><![CDATA[multiple_lst]]><xsl:value-of select="$pos"/><![CDATA[[0].eff_start_date = 'IMMEDIATE']]><![CDATA[multiple_lst]]><xsl:value-of select="$pos"/><![CDATA[[0].eff_end_date = 'UNLIMITED']]></xsl:if>
					<xsl:for-each select="$usr_role_full_lst/role[@id = $cur_role]">
						<xsl:variable name="my_start_date" select="@eff_start_date"/>
						<xsl:variable name="my_end_date" select="@eff_end_date"/>
						<xsl:variable name="pos2">
							<xsl:value-of select="position() -1"/>
						</xsl:variable><![CDATA[multiple_lst]]><xsl:value-of select="$pos"/><![CDATA[[]]><xsl:value-of select="$pos2"/><![CDATA[] = new Array()]]><![CDATA[multiple_lst]]><xsl:value-of select="$pos"/><![CDATA[.id = ']]><xsl:call-template name="escape_js">
							<xsl:with-param name="input_str" select="$cur_role"/>
						</xsl:call-template><![CDATA[']]><!-- define page--><![CDATA[multiple_lst]]><xsl:value-of select="$pos"/><![CDATA[[]]><xsl:value-of select="$pos2"/><![CDATA[].eff_start_date = ']]><xsl:call-template name="escape_js">
							<xsl:with-param name="input_str" select="$my_start_date"/>
						</xsl:call-template><![CDATA[']]><![CDATA[multiple_lst]]><xsl:value-of select="$pos"/><![CDATA[[]]><xsl:value-of select="$pos2"/><![CDATA[].eff_end_date = ']]><xsl:call-template name="escape_js">
							<xsl:with-param name="input_str" select="$my_end_date"/>
						</xsl:call-template><![CDATA[']]><!--end define page-->
						<xsl:for-each select="/user_manager/user/target_list/target_group_list[@role_id = $cur_role]/target_group[entity/@eff_start_date = $my_start_date and entity/@eff_end_date = $my_end_date]"><![CDATA[my_temp_option = new Option
				my_temp_option.text = ']]><xsl:call-template name="escape_js">
								<xsl:with-param name="input_str">
									<xsl:for-each select="entity">
										<xsl:value-of select="@display_bil"/>
										<xsl:if test="position()!=last()">, </xsl:if>
									</xsl:for-each>
								</xsl:with-param>
							</xsl:call-template><![CDATA['
				my_temp_option.value = ']]><xsl:call-template name="escape_js">
								<xsl:with-param name="input_str">
									<xsl:for-each select="entity">
										<xsl:value-of select="@id"/>
										<xsl:if test="position()!=last()">,</xsl:if>
									</xsl:for-each>
								</xsl:with-param>
							</xsl:call-template><![CDATA['
				multiple_lst]]><xsl:value-of select="$pos"/><![CDATA[[]]><xsl:value-of select="$pos2"/><![CDATA[][]]><xsl:value-of select="position()-1"/><![CDATA[] = my_temp_option 
				]]></xsl:for-each>
					</xsl:for-each><![CDATA[
				show_list(0,multiple_lst]]><xsl:value-of select="$pos"/><![CDATA[,document.frmXml.entity_assignment_lst_]]><xsl:value-of select="position()"/><![CDATA[)]]></script>
				<!-- end multiple goldenman code -->
				<input type="hidden" name="entity_assignment_goldenman_type_{$cur_role}" value="{entity_assignment/@type}"/>
				<input type="hidden" name="entity_assignment_name_{$cur_role}" value="{desc[@lan = $wb_lang_encoding]/@name}"/>
				<input type="hidden" name="entity_assignment_lst_type_{position()}" value="{$cur_role}"/>
			</td>
		</tr>
		<input type="hidden" name="usr_role_lst_nm" value="{desc[@lan=$wb_lang_encoding]/@name}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template name="display_tel_number">
		<xsl:param name="numVal"/>
		<xsl:choose>
			<xsl:when test="$numVal = 'n/a-n/a-n/a'">
				<xsl:value-of select="$lab_empty"/>
			</xsl:when>
			<xsl:when test="contains($numVal,'-n/a') and $numVal != 'n/a-n/a-n/a'">
				<xsl:value-of select="substring-before($numVal,'-n/a-')"/>
				<xsl:text>-</xsl:text>
				<xsl:value-of select="substring-after($numVal,'-n/a-')"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$numVal"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="user_id" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:param name="is_registration">false</xsl:param>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly = 'true' and $is_registration = 'false'">
				<xsl:call-template name="usr_id_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user='false' and $readonly='all' and $is_registration='false'">
				<xsl:call-template name="usr_id_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_id">
					<xsl:with-param name="max_length" select="@max_length"/>
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/@id"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">true</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="password" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_pwd_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/pwd"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
				<xsl:call-template name="confirm_usr_pwd_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/pwd"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_pwd">
					<xsl:with-param name="max_length" select="@max_length"/>
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/pwd"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">true</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="confirm_usr_pwd">
					<xsl:with-param name="max_length" select="@max_length"/>
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/pwd"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">true</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="name" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_display_bil_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/name/@display_name"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_display_bil_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/name/@display_name"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_display_bil">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/name/@display_name"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">true</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="gender" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_gender_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/gender"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_gender_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/gender"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_gender">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/gender"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="date_of_birth" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_bday_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/birth/@day"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_bday_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/birth/@day"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_bday">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/birth/@day"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="email" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_email_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/email/@email_1"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_email_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/email/@email_1"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_email">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/email/@email_1"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="phone" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_tel_1_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/tel/@tel_1"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_tel_1_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/tel/@tel_1"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_tel_1">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/tel/@tel_1"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="fax" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_fax_1_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/tel/@fax_1"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_fax_1_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/tel/@fax_1"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_fax_1">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/tel/@fax_1"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="job_title" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_job_title_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/job_title/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_job_title_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/job_title/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_job_title">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/job_title/text()"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="grade" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:param name="is_registration">false</xsl:param>
		<xsl:param name="not_syn_gpm_type"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false' and $is_registration='false'">
				<xsl:call-template name="usr_attributes_read">
					<xsl:with-param name="attribute_type">UGR</xsl:with-param>
					<xsl:with-param name="title" select="$lab_grade"/>
					<xsl:with-param name="hidden_field_value">USR_CURRENT_UGR</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all' and $is_registration='false'">
				<xsl:call-template name="usr_attributes_read">
					<xsl:with-param name="attribute_type">UGR</xsl:with-param>
					<xsl:with-param name="title" select="$lab_grade"/>
					<xsl:with-param name="hidden_field_value">USR_CURRENT_UGR</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_attributes">
					<xsl:with-param name="attribute_type">UGR</xsl:with-param>
					<xsl:with-param name="title" select="$lab_grade"/>
					<xsl:with-param name="field_name">usr_grade_lst</xsl:with-param>
					<xsl:with-param name="tree_type">grade</xsl:with-param>
					<xsl:with-param name="select_type">2</xsl:with-param>
					<xsl:with-param name="box_size">1</xsl:with-param>
					<xsl:with-param name="hidden_field">usr_grade_lst_type</xsl:with-param>
					<xsl:with-param name="hidden_field_value">USR_CURRENT_UGR</xsl:with-param>
					<xsl:with-param name="show_not_syn_option">false</xsl:with-param>
					<xsl:with-param name="not_syn_gpm_type" select="$not_syn_gpm_type"/>
					<xsl:with-param name="required_field">true</xsl:with-param>
					<xsl:with-param name="pick_root">0</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="group" mode="profile_attributes">
		<xsl:param name="not_syn_gpm_type"/>
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:param name="is_registration">false</xsl:param>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false' and $is_registration='false'">
				<xsl:call-template name="usr_attributes_read">
					<xsl:with-param name="title" select="$lab_group"/>
					<xsl:with-param name="hidden_field_value">USR_PARENT_USG</xsl:with-param>
					<xsl:with-param name="attribute_type">USG</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all' and $is_registration='false'">
				<xsl:call-template name="usr_attributes_read">
					<xsl:with-param name="title" select="$lab_group"/>
					<xsl:with-param name="hidden_field_value">USR_PARENT_USG</xsl:with-param>
					<xsl:with-param name="attribute_type">USG</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_attributes">
					<xsl:with-param name="attribute_type">USG</xsl:with-param>
					<xsl:with-param name="title" select="$lab_group"/>
					<xsl:with-param name="field_name">usr_group_lst</xsl:with-param>
					<xsl:with-param name="tree_type">user_group</xsl:with-param>
					<xsl:with-param name="select_type">2</xsl:with-param>
					<xsl:with-param name="box_size">1</xsl:with-param>
					<xsl:with-param name="pick_root">0</xsl:with-param>
					<xsl:with-param name="hidden_field">usr_group_lst_type</xsl:with-param>
					<xsl:with-param name="hidden_field_value">USR_PARENT_USG</xsl:with-param>
					<xsl:with-param name="required_field">true</xsl:with-param>
					<xsl:with-param name="override_appr_usg">1</xsl:with-param>
					<xsl:with-param name="show_not_syn_option">false</xsl:with-param>
					<xsl:with-param name="not_syn_gpm_type" select="$not_syn_gpm_type"/>
					<xsl:with-param name="required_field">true</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="direct_supervisors" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:param name="is_registration">false</xsl:param>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false' and $is_registration='false'">
				<xsl:call-template name="usr_direct_supervisors_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all' and $is_registration='false'">
				<xsl:call-template name="usr_direct_supervisors_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_direct_supervisors">
					<xsl:with-param name="element_value"/>
					<xsl:with-param name="tree_type">user_group_and_user</xsl:with-param>
					<xsl:with-param name="field_name">direct_supervisor_ent</xsl:with-param>
					<xsl:with-param name="hidden_field">direct_supervisor_ent_lst</xsl:with-param>
					<xsl:with-param name="select_type">4</xsl:with-param>
					<xsl:with-param name="box_size">5</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="app_approval_usg_ent_id" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="app_approval_usg_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="app_approval_usg_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="app_approval_usg">
					<xsl:with-param name="showempty" select="$showempty"/>
					<xsl:with-param name="field_name">usr_app_approval_usg</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="join_date" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_join_date_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/join_date/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_join_date_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/join_date/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_join_date">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/join_date/text()"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="role" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:param name="target_list"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_role_lst_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<!--Dennis-->
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_role_lst_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_role_lst">
					<xsl:with-param name="required_field">true</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="source" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:param name="target_list"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_source_read">
					<xsl:with-param name="showempty" select="$showempty"/>
					<xsl:with-param name="element_value" select="$user/source/text()"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_source_read">
					<xsl:with-param name="showempty" select="$showempty"/>
					<xsl:with-param name="element_value" select="$user/source/text()"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_source">
					<xsl:with-param name="element_value" select="$user/source/text()"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_1" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_1_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_1"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_1_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_1"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_1">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_1"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_2" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_2_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_2"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_2_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_2"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_2">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_2"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_3" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_3_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_3"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_3_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_3"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_3">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_3"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_4" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_4_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_4"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_4_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_4"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_4">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_4"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_5" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_5_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_5"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_5_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_5"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_5">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_5"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_6" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_6_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_6"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_6_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_6"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_6">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_6"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_7" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_7_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_7"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_7_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_7"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_7">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_7"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_8" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_8_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_8"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_8_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_8"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_8">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_8"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_9" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_9_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_9"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_9_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_9"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_9">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_9"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_10" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_10_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_10"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_10_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_10"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_10">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_10"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_11" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_datetime_11_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_11/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_datetime_11_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_11/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_datetime_11">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_11/text()"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_12" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_datetime_12_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_12/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_datetime_12_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_12/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_datetime_12">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_12/text()"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_13" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_datetime_13_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_13/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_datetime_13_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_13/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_datetime_13">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_13/text()"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_14" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_datetime_14_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_14/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_datetime_14_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_14/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_datetime_14">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_14/text()"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_15" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_datetime_15_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_15/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_datetime_15_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_15/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_datetime_15">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_15/text()"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_16" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_datetime_16_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_16/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_datetime_16_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_16/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_datetime_16">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_16/text()"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_17" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_datetime_17_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_17/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_datetime_17_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_17/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_datetime_17">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_17/text()"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_18" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_datetime_18_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_18/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_datetime_18_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_18/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_datetime_18">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_18/text()"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_19" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_datetime_19_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_19/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_datetime_19_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_19/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_datetime_19">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_19/text()"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="extension_20" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_datetime_20_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_20/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_datetime_20_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_20/text()"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_datetime_20">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_datetime_20/text()"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="supervised_groups" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_supervised_groups_read">
					<xsl:with-param name="tree_type">user_group</xsl:with-param>
					<xsl:with-param name="field_name">supervise_target_ent</xsl:with-param>
					<xsl:with-param name="hidden_field">supervise_target_ent_lst</xsl:with-param>
					<xsl:with-param name="select_type">1</xsl:with-param>
					<xsl:with-param name="box_size">5</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_supervised_groups_read">
					<xsl:with-param name="tree_type">user_group</xsl:with-param>
					<xsl:with-param name="field_name">supervise_target_ent</xsl:with-param>
					<xsl:with-param name="hidden_field">supervise_target_ent_lst</xsl:with-param>
					<xsl:with-param name="select_type">1</xsl:with-param>
					<xsl:with-param name="box_size">5</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_supervised_groups">
					<xsl:with-param name="tree_type">user_group</xsl:with-param>
					<xsl:with-param name="field_name">supervise_target_ent</xsl:with-param>
					<xsl:with-param name="hidden_field">supervise_target_ent_lst</xsl:with-param>
					<xsl:with-param name="select_type">1</xsl:with-param>
					<xsl:with-param name="box_size">5</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ======Christ:user extension for single choice========= -->
	<xsl:template match="extension_21" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_singleoption_21_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_21/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_singleoption_21_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_21/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_singleoption_21">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_21/@id"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_singleoption_21">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_singleoption_21"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_drop_down_box">
					<xsl:with-param name="name">usr_extra_singleoption_21</xsl:with-param>
					<xsl:with-param name="options" select="$profile_attributes/extension_21/option_list"/>
					<xsl:with-param name="default" select="$element_value"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_singleoption_21_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_singleoption_21" value="{$lab_extra_singleoption_21}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_21-->
	<xsl:template name="usr_extra_singleoption_21_read">
		<xsl:param name="showempty"/>
		<xsl:param name="element_value"/>
		<xsl:variable name="id" select="$user/extra_singleoption_21/@id"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_singleoption_21"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<xsl:choose>
						<xsl:when test="$element_value!=''">
							<xsl:value-of select="$profile_attributes/extension_21/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>--</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======Christ:user extension for single choice========= -->
	<xsl:template match="extension_22" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_singleoption_22_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_22/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_singleoption_22_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_22/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_singleoption_22">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_22/@id"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_singleoption_22">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_singleoption_22"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_drop_down_box">
					<xsl:with-param name="name">usr_extra_singleoption_22</xsl:with-param>
					<xsl:with-param name="options" select="$profile_attributes/extension_22/option_list"/>
					<xsl:with-param name="default" select="$element_value"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_singleoption_22_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_singleoption_22" value="{$lab_extra_singleoption_22}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_22-->
	<xsl:template name="usr_extra_singleoption_22_read">
		<xsl:param name="showempty"/>
		<xsl:param name="element_value"/>
		<xsl:variable name="id" select="$user/extra_singleoption_22/@id"/>
		<xsl:if test='$showempty = "true" or $element_value !=""'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_singleoption_22"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<xsl:choose>
						<xsl:when test="$element_value!=''">
							<xsl:value-of select="$profile_attributes/extension_22/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>--</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======Christ:user extension for single choice========= -->
	<xsl:template match="extension_23" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_singleoption_23_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_23/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_singleoption_23_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_23/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_singleoption_23">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_23/@id"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_singleoption_23">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_singleoption_23"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_drop_down_box">
					<xsl:with-param name="name">usr_extra_singleoption_23</xsl:with-param>
					<xsl:with-param name="options" select="$profile_attributes/extension_23/option_list"/>
					<xsl:with-param name="default" select="$element_value"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_singleoption_23_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_singleoption_23" value="{$lab_extra_singleoption_23}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_23-->
	<xsl:template name="usr_extra_singleoption_23_read">
		<xsl:param name="showempty"/>
		<xsl:param name="element_value"/>
		<xsl:variable name="id" select="$user/extra_singleoption_23/@id"/>
		<xsl:if test='$showempty = "true" or $element_value !=""'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_singleoption_23"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<xsl:choose>
						<xsl:when test="$element_value!=''">
							<xsl:value-of select="$profile_attributes/extension_23/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>--</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======Christ:user extension for single choice========= -->
	<xsl:template match="extension_24" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_singleoption_24_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_24/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_singleoption_24_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_24/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_singleoption_24">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_24/@id"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_singleoption_24">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_singleoption_24"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_drop_down_box">
					<xsl:with-param name="name">usr_extra_singleoption_24</xsl:with-param>
					<xsl:with-param name="options" select="$profile_attributes/extension_24/option_list"/>
					<xsl:with-param name="default" select="$element_value"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_singleoption_24_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_singleoption_24" value="{$lab_extra_singleoption_24}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_24-->
	<xsl:template name="usr_extra_singleoption_24_read">
		<xsl:param name="showempty"/>
		<xsl:param name="element_value"/>
		<xsl:variable name="id" select="$user/extra_singleoption_24/@id"/>
		<xsl:if test='$showempty = "true" or $element_value !=""'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_singleoption_24"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<xsl:choose>
						<xsl:when test="$element_value!=''">
							<xsl:value-of select="$profile_attributes/extension_24/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>--</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======Christ:user extension for single choice========= -->
	<xsl:template match="extension_25" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_singleoption_25_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_25/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_singleoption_25_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_25/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_singleoption_25">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_25/@id"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_singleoption_25">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_singleoption_25"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_drop_down_box">
					<xsl:with-param name="name">usr_extra_singleoption_25</xsl:with-param>
					<xsl:with-param name="options" select="$profile_attributes/extension_25/option_list"/>
					<xsl:with-param name="default" select="$element_value"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_singleoption_25_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_singleoption_25" value="{$lab_extra_singleoption_25}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_25-->
	<xsl:template name="usr_extra_singleoption_25_read">
		<xsl:param name="showempty"/>
		<xsl:param name="element_value"/>
		<xsl:variable name="id" select="$user/extra_singleoption_25/@id"/>
		<xsl:if test='$showempty = "true" or $element_value !=""'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_singleoption_25"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<xsl:choose>
						<xsl:when test="$element_value!=''">
							<xsl:value-of select="$profile_attributes/extension_25/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>--</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======Christ:user extension for single choice========= -->
	<xsl:template match="extension_26" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_singleoption_26_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_26/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly != 'true'">
				<xsl:call-template name="usr_extra_singleoption_26_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_26/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_singleoption_26">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_26/@id"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_singleoption_26">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_singleoption_26"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_drop_down_box">
					<xsl:with-param name="name">usr_extra_singleoption_26</xsl:with-param>
					<xsl:with-param name="options" select="$profile_attributes/extension_26/option_list"/>
					<xsl:with-param name="default" select="$element_value"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_singleoption_26_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_singleoption_26" value="{$lab_extra_singleoption_26}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_26-->
	<xsl:template name="usr_extra_singleoption_26_read">
		<xsl:param name="showempty"/>
		<xsl:param name="element_value"/>
		<xsl:variable name="id" select="$user/extra_singleoption_26/@id"/>
		<xsl:if test='$showempty = "true" or $element_value !=""'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_singleoption_26"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<xsl:choose>
						<xsl:when test="$element_value!=''">
							<xsl:value-of select="$profile_attributes/extension_26/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>--</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======Christ:user extension for single choice========= -->
	<xsl:template match="extension_27" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_singleoption_27_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_27/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_singleoption_27_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_27/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_singleoption_27">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_27/@id"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_singleoption_27">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_singleoption_27"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_drop_down_box">
					<xsl:with-param name="name">usr_extra_singleoption_27</xsl:with-param>
					<xsl:with-param name="options" select="$profile_attributes/extension_27/option_list"/>
					<xsl:with-param name="default" select="$element_value"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_singleoption_27_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_singleoption_27" value="{$lab_extra_singleoption_27}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_27-->
	<xsl:template name="usr_extra_singleoption_27_read">
		<xsl:param name="showempty"/>
		<xsl:param name="element_value"/>
		<xsl:variable name="id" select="$user/extra_singleoption_27/@id"/>
		<xsl:if test='$showempty = "true" or $element_value !=""'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_singleoption_27"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<xsl:choose>
						<xsl:when test="$element_value!=''">
							<xsl:value-of select="$profile_attributes/extension_27/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>--</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======Christ:user extension for single choice========= -->
	<xsl:template match="extension_28" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_singleoption_28_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_28/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly != 'all'">
				<xsl:call-template name="usr_extra_singleoption_28_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_28/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_singleoption_28">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_28/@id"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_singleoption_28">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_singleoption_28"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_drop_down_box">
					<xsl:with-param name="name">usr_extra_singleoption_28</xsl:with-param>
					<xsl:with-param name="options" select="$profile_attributes/extension_28/option_list"/>
					<xsl:with-param name="default" select="$element_value"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_singleoption_28_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_singleoption_28" value="{$lab_extra_singleoption_28}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_28-->
	<xsl:template name="usr_extra_singleoption_28_read">
		<xsl:param name="showempty"/>
		<xsl:param name="element_value"/>
		<xsl:variable name="id" select="$user/extra_singleoption_28/@id"/>
		<xsl:if test='$showempty = "true" or $element_value !=""'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_singleoption_28"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<xsl:choose>
						<xsl:when test="$element_value!=''">
							<xsl:value-of select="$profile_attributes/extension_28/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>--</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======Christ:user extension for single choice========= -->
	<xsl:template match="extension_29" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_singleoption_29_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_29/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_singleoption_29_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_29/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_singleoption_29">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_29/@id"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_singleoption_29">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_singleoption_29"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_drop_down_box">
					<xsl:with-param name="name">usr_extra_singleoption_29</xsl:with-param>
					<xsl:with-param name="options" select="$profile_attributes/extension_29/option_list"/>
					<xsl:with-param name="default" select="$element_value"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_singleoption_29_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_singleoption_29" value="{$lab_extra_singleoption_29}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_29-->
	<xsl:template name="usr_extra_singleoption_29_read">
		<xsl:param name="showempty"/>
		<xsl:param name="element_value"/>
		<xsl:variable name="id" select="$user/extra_singleoption_29/@id"/>
		<xsl:if test='$showempty = "true" or $element_value !=""'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_singleoption_29"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<xsl:choose>
						<xsl:when test="$element_value!=''">
							<xsl:value-of select="$profile_attributes/extension_29/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>--</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======Christ:user extension for single choice========= -->
	<xsl:template match="extension_30" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_singleoption_30_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_30/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_singleoption_30_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_30/@id"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_singleoption_30">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$user/extra_singleoption_30/@id"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_singleoption_30">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_singleoption_30"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_drop_down_box">
					<xsl:with-param name="name">usr_extra_singleoption_30</xsl:with-param>
					<xsl:with-param name="options" select="$profile_attributes/extension_30/option_list"/>
					<xsl:with-param name="default" select="$element_value"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_singleoption_30_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_singleoption_30" value="{$lab_extra_singleoption_30}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_30-->
	<xsl:template name="usr_extra_singleoption_30_read">
		<xsl:param name="showempty"/>
		<xsl:param name="element_value"/>
		<xsl:variable name="id" select="$user/extra_singleoption_30/@id"/>
		<xsl:if test='$showempty = "true" or $element_value !=""'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_singleoption_30"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value!=''">
								<xsl:value-of select="$profile_attributes/extension_30/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>--</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- christ :user extension for multiple choicese -->
	<xsl:template match="extension_31" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_multipleoption_31_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_multipleoption_31_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_multipleoption_31">
					<xsl:with-param name="required_field">false</xsl:with-param>
					<xsl:with-param name="element_value" select="$user/extra_multipleoption_31"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_multipleoption_31">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_multipleoption_31"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_check_box">
					<xsl:with-param name="name">usr_extra_multipleoption_31</xsl:with-param>
					<xsl:with-param name="default" select="$element_value"/>
					<xsl:with-param name="options" select="$profile_attributes/extension_31/option_list"/>
					<xsl:with-param name="cur_att_name" select="name($profile_attributes/extension_31)"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_multipleoption_31_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_multipleoption_31" value="{$lab_extra_multipleoption_31}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_31-->
	<xsl:template name="usr_extra_multipleoption_31_read">
		<xsl:param name="showempty"/>
		<xsl:if test='$showempty = "true" or count($user/extra_multipleoption_31/option)&gt;0'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_multipleoption_31"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="count($user/extra_multipleoption_31/option)&gt;0">
								<xsl:for-each select="$user/extra_multipleoption_31/option">
									<xsl:variable name="id" select="@id"/>
									<xsl:value-of select="$profile_attributes/extension_31/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
									<xsl:if test="position()!=last()">,&#160;</xsl:if>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--===================================================================-->
	<xsl:template match="extension_32" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_multipleoption_32_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_multipleoption_32_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_multipleoption_32">
					<xsl:with-param name="element_value" select="$user/extra_multipleoption_32"/>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_multipleoption_32">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_multipleoption_32"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_check_box">
					<xsl:with-param name="name">usr_extra_multipleoption_32</xsl:with-param>
					<xsl:with-param name="default" select="$element_value"/>
					<xsl:with-param name="options" select="$profile_attributes/extension_32/option_list"/>
					<xsl:with-param name="cur_att_name" select="name($profile_attributes/extension_32)"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_multipleoption_32_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_multipleoption_32" value="{$lab_extra_multipleoption_32}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_32-->
	<xsl:template name="usr_extra_multipleoption_32_read">
		<xsl:param name="showempty"/>
		<xsl:if test='$showempty = "true" or count($user/extra_multipleoption_32/option)&gt;0'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_multipleoption_32"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="count($user/extra_multipleoption_32/option)&gt;0">
								<xsl:for-each select="$user/extra_multipleoption_32/option">
									<xsl:variable name="id" select="@id"/>
									<xsl:value-of select="$profile_attributes/extension_32/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
									<xsl:if test="position()!=last()">,&#160;</xsl:if>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--===================================================================-->
	<!-- christ :user extension for multiple choicese -->
	<xsl:template match="extension_33" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_multipleoption_33_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly != 'true'">
				<xsl:call-template name="usr_extra_multipleoption_33_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_multipleoption_33">
					<xsl:with-param name="element_value" select="$user/extra_multipleoption_33"/>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_multipleoption_33">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_multipleoption_33"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_check_box">
					<xsl:with-param name="name">usr_extra_multipleoption_33</xsl:with-param>
					<xsl:with-param name="default" select="$element_value"/>
					<xsl:with-param name="options" select="$profile_attributes/extension_33/option_list"/>
					<xsl:with-param name="cur_att_name" select="name($profile_attributes/extension_33)"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_multipleoption_33_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_multipleoption_33" value="{$lab_extra_multipleoption_33}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_33-->
	<xsl:template name="usr_extra_multipleoption_33_read">
		<xsl:param name="showempty"/>
		<xsl:if test='$showempty = "true" or count($user/extra_multipleoption_33/option)&gt;0'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_multipleoption_33"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="count($user/extra_multipleoption_33/option)&gt;0">
								<xsl:for-each select="$user/extra_multipleoption_33/option">
									<xsl:variable name="id" select="@id"/>
									<xsl:value-of select="$profile_attributes/extension_33/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
									<xsl:if test="position()!=last()">,&#160;</xsl:if>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--===================================================================-->
	<!-- christ :user extension for multiple choicese -->
	<xsl:template match="extension_34" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_multipleoption_34_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_multipleoption_34_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_multipleoption_34">
					<xsl:with-param name="element_value" select="$user/extra_multipleoption_34"/>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_multipleoption_34">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_multipleoption_34"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_check_box">
					<xsl:with-param name="name">usr_extra_multipleoption_34</xsl:with-param>
					<xsl:with-param name="default" select="$element_value"/>
					<xsl:with-param name="options" select="$profile_attributes/extension_34/option_list"/>
					<xsl:with-param name="cur_att_name" select="name($profile_attributes/extension_34)"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_multipleoption_34_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_multipleoption_34" value="{$lab_extra_multipleoption_34}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_34-->
	<xsl:template name="usr_extra_multipleoption_34_read">
		<xsl:param name="showempty"/>
		<xsl:if test='$showempty = "true" or count($user/extra_multipleoption_34/option)&gt;0'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_multipleoption_34"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="count($user/extra_multipleoption_34/option)&gt;0">
								<xsl:for-each select="$user/extra_multipleoption_34/option">
									<xsl:variable name="id" select="@id"/>
									<xsl:value-of select="$profile_attributes/extension_34/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
									<xsl:if test="position()!=last()">,&#160;</xsl:if>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--===================================================================-->
	<!--===================================================================-->
	<!-- christ :user extension for multiple choicese -->
	<xsl:template match="extension_35" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_multipleoption_35_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_multipleoption_35_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_multipleoption_35">
					<xsl:with-param name="element_value" select="$user/extra_multipleoption_35"/>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_multipleoption_35">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_multipleoption_35"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_check_box">
					<xsl:with-param name="name">usr_extra_multipleoption_35</xsl:with-param>
					<xsl:with-param name="default" select="$element_value"/>
					<xsl:with-param name="options" select="$profile_attributes/extension_35/option_list"/>
					<xsl:with-param name="cur_att_name" select="name($profile_attributes/extension_35)"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_multipleoption_35_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_multipleoption_35" value="{$lab_extra_multipleoption_35}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_35-->
	<xsl:template name="usr_extra_multipleoption_35_read">
		<xsl:param name="showempty"/>
		<xsl:if test='$showempty = "true" or count($user/extra_multipleoption_35/option)&gt;0'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_multipleoption_35"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="count($user/extra_multipleoption_35/option)&gt;0">
								<xsl:for-each select="$user/extra_multipleoption_35/option">
									<xsl:variable name="id" select="@id"/>
									<xsl:value-of select="$profile_attributes/extension_35/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
									<xsl:if test="position()!=last()">,&#160;</xsl:if>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--===================================================================-->
	<!-- christ :user extension for multiple choicese -->
	<xsl:template match="extension_36" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_multipleoption_36_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_multipleoption_36_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_multipleoption_36">
					<xsl:with-param name="element_value" select="$user/extra_multipleoption_36"/>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_multipleoption_36">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_multipleoption_36"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_check_box">
					<xsl:with-param name="name">usr_extra_multipleoption_36</xsl:with-param>
					<xsl:with-param name="default" select="$element_value"/>
					<xsl:with-param name="options" select="$profile_attributes/extension_36/option_list"/>
					<xsl:with-param name="cur_att_name" select="name($profile_attributes/extension_36)"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_multipleoption_36_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_multipleoption_36" value="{$lab_extra_multipleoption_36}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_36-->
	<xsl:template name="usr_extra_multipleoption_36_read">
		<xsl:param name="showempty"/>
		<xsl:if test='$showempty = "true" or count($user/extra_multipleoption_36/option)&gt;0'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_multipleoption_36"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="count($user/extra_multipleoption_36/option)&gt;0">
								<xsl:for-each select="$user/extra_multipleoption_36/option">
									<xsl:variable name="id" select="@id"/>
									<xsl:value-of select="$profile_attributes/extension_36/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
									<xsl:if test="position()!=last()">,&#160;</xsl:if>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--===================================================================-->
	<!-- christ :user extension for multiple choicese -->
	<xsl:template match="extension_37" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_multipleoption_37_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_multipleoption_37_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_multipleoption_37">
					<xsl:with-param name="element_value" select="$user/extra_multipleoption_37"/>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_multipleoption_37">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_multipleoption_37"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_check_box">
					<xsl:with-param name="name">usr_extra_multipleoption_37</xsl:with-param>
					<xsl:with-param name="default" select="$element_value"/>
					<xsl:with-param name="options" select="$profile_attributes/extension_37/option_list"/>
					<xsl:with-param name="cur_att_name" select="name($profile_attributes/extension_37)"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_multipleoption_37_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_multipleoption_37" value="{$lab_extra_multipleoption_37}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_37-->
	<xsl:template name="usr_extra_multipleoption_37_read">
		<xsl:param name="showempty"/>
		<xsl:if test='$showempty = "true" or count($user/extra_multipleoption_37/option)&gt;0'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_multipleoption_37"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="count($user/extra_multipleoption_37/option)&gt;0">
								<xsl:for-each select="$user/extra_multipleoption_37/option">
									<xsl:variable name="id" select="@id"/>
									<xsl:value-of select="$profile_attributes/extension_37/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
									<xsl:if test="position()!=last()">,&#160;</xsl:if>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--===================================================================-->
	<!-- christ :user extension for multiple choicese -->
	<xsl:template match="extension_38" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_multipleoption_38_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_multipleoption_38_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_multipleoption_38">
					<xsl:with-param name="element_value" select="$user/extra_multipleoption_38"/>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_multipleoption_38">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_multipleoption_38"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_check_box">
					<xsl:with-param name="name">usr_extra_multipleoption_38</xsl:with-param>
					<xsl:with-param name="default" select="$element_value"/>
					<xsl:with-param name="options" select="$profile_attributes/extension_38/option_list"/>
					<xsl:with-param name="cur_att_name" select="name($profile_attributes/extension_38)"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_multipleoption_38_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_multipleoption_38" value="{$lab_extra_multipleoption_38}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_38-->
	<xsl:template name="usr_extra_multipleoption_38_read">
		<xsl:param name="showempty"/>
		<xsl:if test='$showempty = "true" or count($user/extra_multipleoption_38/option)&gt;0'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_multipleoption_38"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="count($user/extra_multipleoption_38/option)&gt;0">
								<xsl:for-each select="$user/extra_multipleoption_38/option">
									<xsl:variable name="id" select="@id"/>
									<xsl:value-of select="$profile_attributes/extension_38/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
									<xsl:if test="position()!=last()">,&#160;</xsl:if>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--===================================================================-->
	<!-- christ :user extension for multiple choicese -->
	<xsl:template match="extension_39" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extra_multipleoption_39_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_multipleoption_39_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_multipleoption_39">
					<xsl:with-param name="element_value" select="$user/extra_multipleoption_39"/>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_multipleoption_39">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_multipleoption_39"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:call-template name="gen_check_box">
					<xsl:with-param name="name">usr_extra_multipleoption_39</xsl:with-param>
					<xsl:with-param name="default" select="$element_value"/>
					<xsl:with-param name="options" select="$profile_attributes/extension_39/option_list"/>
					<xsl:with-param name="cur_att_name" select="name($profile_attributes/extension_39)"/>
				</xsl:call-template>
				<input type="hidden" name="usr_extra_multipleoption_39_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_multipleoption_39" value="{$lab_extra_multipleoption_39}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_39-->
	<xsl:template name="usr_extra_multipleoption_39_read">
		<xsl:param name="showempty"/>
		<xsl:if test='$showempty = "true" or count($user/extra_multipleoption_39/option)&gt;0'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_multipleoption_39"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="count($user/extra_multipleoption_39/option)&gt;0">
								<xsl:for-each select="$user/extra_multipleoption_39/option">
									<xsl:variable name="id" select="@id"/>
									<xsl:value-of select="$profile_attributes/extension_39/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
									<xsl:if test="position()!=last()">,&#160;</xsl:if>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--===================================================================-->
	<!-- christ :user extension for multiple choicese -->
	<xsl:template match="extension_40" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly = 'true'">
				<xsl:call-template name="usr_extra_multipleoption_40_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extra_multipleoption_40_read">
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extra_multipleoption_40">
					<xsl:with-param name="element_value" select="$user/extra_multipleoption_40"/>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- EDIT MODE-->
	<xsl:template name="usr_extra_multipleoption_40">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td width="{$tableLeftWidth}" align="right" valign="top">
				<span class="{$tableLeftText}">
					<xsl:if test="$required_field = 'true'">
						<xsl:text>*</xsl:text>
					</xsl:if>
					<xsl:value-of select="$lab_extra_multipleoption_40"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="{$tableRightWidth}">
				<xsl:variable name="name">usr_extra_multipleoption_40</xsl:variable>
				<xsl:variable name="default" select="$element_value"/>
				<xsl:variable name="options" select="$profile_attributes/extension_40/option_list"/>
				<xsl:variable name="cur_att_name" select="name($profile_attributes/extension_40)"/>
				<xsl:for-each select="$options/*">
					<xsl:variable name="id" select="@id"/>
					<label for="{concat($cur_att_name,$id)}">
						<input type="checkbox" name="{$name}" id="{concat($cur_att_name,$id)}" value="{$id}">
							<xsl:if test="$default/option[@id=$id]">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<span class="Text">
							<xsl:value-of select="concat(' ',label[@xml:lang=$cur_lang],' ')"/>
						</span>
					</label>
				</xsl:for-each>
				<input type="hidden" name="usr_extra_multipleoption_40_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extra_multipleoption_40" value="{$lab_extra_multipleoption_40}"/>
	</xsl:template>
	<!-- ======================================================================== -->
	<!-- READ MODE for usr_extra_singleoption_40-->
	<xsl:template name="usr_extra_multipleoption_40_read">
		<xsl:param name="showempty"/>
		<xsl:if test='$showempty = "true" or count($user/extra_multipleoption_40/option)&gt;0'>
			<tr>
				<td align="right" width="{$tableLeftWidth}" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extra_multipleoption_40"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="count($user/extra_multipleoption_40/option)&gt;0">
								<xsl:for-each select="$user/extra_multipleoption_40/option">
									<xsl:variable name="id" select="@id"/>
									<xsl:value-of select="$profile_attributes/extension_40/option_list/option[@id=$id]/label[@xml:lang = $cur_lang]"/>
									<xsl:if test="position()!=last()">,&#160;</xsl:if>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="nickname" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:variable name="nick_name" select="$user/name/@nickname"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_nickname_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$nick_name"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_nickname_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$nick_name"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_nickname">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$nick_name"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ================= -->
	<!-- usr_nickname -->
	<xsl:template name="usr_nickname">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="max_length">20</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label">
				<xsl:if test="$required_field = 'true'">
					<xsl:text>*</xsl:text>
				</xsl:if>
				<xsl:value-of select="$lab_nickname"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<input class="wzb-inputText" type="text" name="usr_nickname" style="width:{$field_width}px;" size="27" maxlength="">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<input type="hidden" name="usr_nickname_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_nickname" value="{$lab_nickname}"/>
	</xsl:template>
	<!-- ================= -->
	<!-- usr_nickname -->
	<xsl:template name="usr_nickname_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_nickname"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="$element_value!=''">
							<xsl:value-of select="$element_value"/>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="extension_41" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:variable name="extension_41" select="$user/extra_41"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extension_41_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$extension_41"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extension_41_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$extension_41"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extension_41">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$extension_41"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ================= -->
	<!-- usr_extension_41 -->
	<xsl:template name="usr_extension_41">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="max_length">500</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label">
				<xsl:if test="$required_field = 'true'">
					<xsl:text>*</xsl:text>
				</xsl:if>
				<xsl:value-of select="$lab_extension_41"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<input class="wzb-inputText" type="text" name="usr_extra_2" style="width:{$field_width}px;" size="27" maxlength="{$max_length}">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<input type="hidden" name="urx_extra_41_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extension_41" value="{$lab_extension_41}"/>
	</xsl:template>
	<!-- ================= -->
	<!-- usr_extension_41 -->
	<xsl:template name="usr_extension_41_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_extension_41"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="$element_value!=''">
							<xsl:value-of select="$element_value"/>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="extension_42" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:variable name="extension_42" select="$user/extra_42"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extension_42_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$extension_42"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extension_42_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$extension_42"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extension_42">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$extension_42"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ================= -->
	<!-- usr_extension_42 -->
	<xsl:template name="usr_extension_42">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="max_length">500</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label">
				<xsl:if test="$required_field = 'true'">
					<xsl:text>*</xsl:text>
				</xsl:if>
				<xsl:value-of select="$lab_extension_42"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<input class="wzb-inputText" type="text" name="urx_extra_42" style="width:{$field_width}px;" size="27" maxlength="{$max_length}">
					<xsl:choose>
						<xsl:when test="$element_value != ''">
							<xsl:attribute name="value"><xsl:value-of select="$element_value"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="value"/>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<input type="hidden" name="urx_extra_42_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extension_42" value="{$lab_extension_42}"/>
	</xsl:template>
	<!-- ================= -->
	<!-- usr_extension_42 -->
	<xsl:template name="usr_extension_42_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td class="wzb-form-label"  align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extension_42"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td class="{$tableTdCont}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value!=''">
								<xsl:value-of select="$element_value"/>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="extension_43" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:variable name="extension_43">
			<xsl:choose>
				<xsl:when test="$user/extra_43"><xsl:value-of select="$user/extra_43"/></xsl:when>
				<xsl:otherwise><xsl:value-of select="../../default_usr_icon"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extension_43_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$extension_43"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extension_43_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$extension_43"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extension_43">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$extension_43"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ================= -->
	<!-- usr_extension_43 -->
	<xsl:template name="usr_extension_43">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="max_length">500</xsl:param>
		<xsl:param name="element_value"/>
		<xsl:variable name="lab_remain_image">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">保留現有頭像</xsl:when>
				<xsl:when test="$wb_lang='gb'">保留现有头像</xsl:when>
				<xsl:otherwise>Keep this thumbnail</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_default_image">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">使用默認頭像</xsl:when>
				<xsl:when test="$wb_lang='gb'">使用默认头像</xsl:when>
				<xsl:otherwise>Restore to default thumbnail</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_local_image">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">上傳一個本地圖片作為顯示的圖像</xsl:when>
				<xsl:when test="$wb_lang='gb'">上传一个本地图片作为显示的图像</xsl:when>
				<xsl:otherwise>Upload an image as my thumbnail</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="img_des">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">(圖片規格建議：寬120px，高120px，支持JPG,GIF,PNG格式圖片)</xsl:when>
				<xsl:when test="$wb_lang='gb'">(图片规格建议：宽120px，高120px，支持JPG,GIF,PNG格式图片)</xsl:when>
				<xsl:otherwise>(Image size recommendation: width 120px, height 120px,Support JPG,GIF,PNG files)</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr>
			<td class="wzb-form-label" valign="top">
				<xsl:if test="$required_field = 'true'">
					<xsl:text>*</xsl:text>
				</xsl:if>
				<xsl:value-of select="$lab_extension_43"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<script LANGUAGE="JavaScript" TYPE="text/javascript">
					function clearFileInput(file_obj){
						var file_obj2= file_obj.cloneNode(false);
						file_obj2.onchange= file_obj.onchange; 
						//file_obj2.disabled = true;
						file_obj.parentNode.replaceChild(file_obj2,file_obj);
					}

					function extension_43_change(obj) {
					    $("#default_btn").prev().removeAttr("disabled");
						var img_obj = document.getElementById("extension_43_id");
						var file_obj = document.getElementById("file_photo_url");
						if(obj.id === "remain_image") {
							clearFileInput(file_obj);
						  <!--   $("#default_btn").prev().attr("disabled","disabled"); -->
							img_obj.src = "../<xsl:value-of select="$element_value"/>";
							document.frmXml.remain_photo_ind.value = "true";
						} else if(obj.id === "default_image") {
							clearFileInput(file_obj);
							img_obj.src = "../<xsl:value-of select="../../default_usr_icon"/>";
							document.frmXml.remain_photo_ind.value = "false";
						} else {
							file_obj.disabled = false;
							<!-- $("#default_btn").prev().attr("disabled","disabled"); -->
							document.frmXml.remain_photo_ind.value = "false";
							if(document.all.urx_extra_43.files &amp;&amp; document.all.urx_extra_43.files[0]){
								img_obj.src = window.URL.createObjectURL(document.all.urx_extra_43.files[0]);
							}
						}
					}
					
					function extension_43_change_byId(id) {
						var img_obj = document.getElementById("extension_43_id");
						var file_obj = document.getElementById("file_photo_url");
						if(id === "remain_image") {
							clearFileInput(file_obj);
						  <!--   $("#default_btn").prev().attr("disabled","disabled"); -->
							img_obj.src = "../<xsl:value-of select="$element_value"/>";
							document.frmXml.remain_photo_ind.value = "true";
						} else if(id === "default_image") {
							clearFileInput(file_obj);
							img_obj.src = "../<xsl:value-of select="../../default_usr_icon"/>";
							document.frmXml.remain_photo_ind.value = "false";
						} else {
							file_obj.disabled = false;
							<!-- $("#default_btn").prev().attr("disabled","disabled"); -->
							document.frmXml.remain_photo_ind.value = "false";
							if(document.all.urx_extra_43.files &amp;&amp; document.all.urx_extra_43.files[0]){
								img_obj.src = window.URL.createObjectURL(document.all.urx_extra_43.files[0]);
							}
						}
					}
					
					function previewLocalImage(obj) {
						var img_obj = document.getElementById("extension_43_id");
						var types = ["jpg", "gif", "png"];
						var file_type = obj.files[0].name.substring(obj.files[0].name.lastIndexOf('.') + 1).toLowerCase();
						var ret = false;
						//alert(obj.files[0].name.substring(obj.files[0].name.lastIndexOf('.') + 1).toLowerCase());
					    for(var i=0; i &lt; types.length; i++) {
							if(file_type === types[i]) {
								ret = true;
							}
						}
						if(ret){
						 img_obj.src = window.URL.createObjectURL(obj.files[0]);
						}
					}
					
					$(function(){
					    //console.log(1);
						initDefaultImage('user', 'user', false);
					})
					
					$(window).load(function(){
					    var defaultImage="<xsl:value-of select="$element_value"/>";
					     if(defaultImage == 'user/user.png' || defaultImage == undefined){
					         //initDefaultImage('user', 'user', true);
					           //console.log(2);
					         //useDefaultImage();
					     }
					})
				</script>
				
		 
			  <!-- 修改时 显示  “保存现有头像” -->
				       <table cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td rowspan="4" width="108px" valign="top">
									<img src="../{$element_value}" name="user_preview" id="extension_43_id" border="0" width="82" height="82"  style="border-radius:50%;"/>
								</td>
								<td>
									<label for="remain_image">
										<input type="radio" checked="checked" id="remain_image" name="extension_43_select" onclick="extension_43_change(this)"/>
										<span class="{$tableRightText}">
											<xsl:value-of select="$lab_remain_image"/>
										</span>
										<input name="extension_43_hidden" type="hidden" value="{@value}"/>
										<input name="remain_photo_ind" type="hidden" value="true"/>
									</label>
								</td>
							</tr>
							<tr>
								<td>
									<label for="default_image">
										<input type="radio" id="default_image" name="extension_43_select" value="use_default_image" onclick="useDefaultImage()"/>
										<input name="" type="button" class="wzb-btn-blue"  style="border:1px solid transparent;padding:3px 8px;" onclick="this.parentNode.firstChild.checked=true;show_default_image();useDefaultImage()" value="{$lab_select_default_image}"/>
										<a id="default_btn" href="#TB_inline?height=380&amp;width=580&amp;inlineId=myOnPageContent" class="thickbox" style="display: none;"></a>
										<input type="hidden" name="default_image"/>
										<input type="hidden" name="{@paramname}_del_ind"/>
									</label>
									<br/>
									<div id="myOnPageContent" style="display: none;">						
										<div class="thickbox-big ">
											<div class="thickbox-tit  thickbox-tit-1" >
												<xsl:value-of select="$lab_default_images"/>
											</div>
											
											<div class="thickbox-cont thickbox-user clearfix  thickbox-content-2" id="defaultImages"></div>						
										
											<div class="norm-border thickbox-footer"   >
												<input type="button" class="margin-right10    btn wzb-btn-blue wzb-btn-big" name="pertxt" onclick="selectImage()" value="{$lab_button_ok}" />
												<input  type="button" class=" TB_closeWindowButton  btn wzb-btn-blue wzb-btn-big " name="pertxt" value="{$lab_g_form_btn_cancel}" />
											</div>
										</div>							
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<label for="local_image">
										<input type="radio" id="local_image" name="extension_43_select" onclick="extension_43_change(this)"/>
										<span class="{$tableRightText}">
											<xsl:value-of select="$lab_local_image"/>
										</span>
									</label>
								</td>
							</tr>
							<tr>
								<td>
									<xsl:call-template name="wb_gen_input_file">
										<xsl:with-param name="id">file_photo_url</xsl:with-param>
										<xsl:with-param name="name">urx_extra_43</xsl:with-param>
										<!-- <xsl:with-param name="disabled">true</xsl:with-param> -->
										<xsl:with-param name="onclick">document.frmXml.local_image.checked=true;extension_43_change_byId('local_image');</xsl:with-param>
										<xsl:with-param name="onchange">previewLocalImage(this)</xsl:with-param>
									</xsl:call-template>
									<span class="text">
										&#160;<xsl:value-of select="$img_des"/>
									</span>
								</td>
							</tr>
						</table>
				
				<input type="hidden" name="urx_extra_43_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extension_43" value="{$lab_extension_43}"/>
	</xsl:template>
	<!-- ================= -->
	<!-- usr_extension_43 -->
	<xsl:template name="usr_extension_43_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extension_43"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td class="wzb-form-control">
					<img src="../{$element_value}" border="0" width="50" height="50" style="border-radius:50%;"/>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="extension_44" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:variable name="extension_44" select="$user/extra_44"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extension_44_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$extension_44"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extension_44_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$extension_44"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extension_44">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$extension_44"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ================= -->
	<!-- usr_extension_44 -->
	<xsl:template name="usr_extension_44">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="max_length">500</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label" valign="top">
				<xsl:if test="$required_field = 'true'">
					<xsl:text>*</xsl:text>
				</xsl:if>
				<xsl:value-of select="$lab_extension_44"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<textarea class="wzb-inputTextArea" rows="6" style="width:{$field_width}px;" name="urx_extra_44">
					<xsl:if test="$element_value != ''">
						<xsl:value-of select="$element_value"/>
					</xsl:if>
				</textarea>
				<input type="hidden" name="urx_extra_44_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extension_44" value="{$lab_extension_44}"/>
	</xsl:template>
	<!-- ================= -->
	<!-- usr_extension_44 -->
	<xsl:template name="usr_extension_44_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_extension_44"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="$element_value!=''">
							<xsl:value-of select="$element_value"/>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="extension_45" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:variable name="extension_45" select="$user/extra_45"/>
		<xsl:choose>
			<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
				<xsl:call-template name="usr_extension_45_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$extension_45"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
				<xsl:call-template name="usr_extension_45_read">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$extension_45"/>
					</xsl:with-param>
					<xsl:with-param name="showempty" select="$showempty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="usr_extension_45">
					<xsl:with-param name="element_value">
						<xsl:value-of select="$extension_45"/>
					</xsl:with-param>
					<xsl:with-param name="required_field">false</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ================= -->
	<!-- usr_extension_45 -->
	<xsl:template name="usr_extension_45">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="max_length">500</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label" valign="top">
				<xsl:if test="$required_field = 'true'">
					<xsl:text>*</xsl:text>
				</xsl:if>
				<xsl:value-of select="$lab_extension_45"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<textarea class="wzb-inputTextArea" rows="6" style="width:{$field_width}px;" name="urx_extra_45">
					<xsl:if test="$element_value != ''">
						<xsl:value-of select="$element_value"/>
					</xsl:if>
				</textarea>
				<input type="hidden" name="urx_extra_45_req_fld">
					<xsl:attribute name="value"><xsl:choose><xsl:when test="$required_field = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:attribute>
				</input>
			</td>
		</tr>
		<input type="hidden" name="lab_extension_45" value="{$lab_extension_45}"/>
	</xsl:template>
	<!-- ================= -->
	<!-- usr_extension_45 -->
	<xsl:template name="usr_extension_45_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td class="wzb-form-label" align="right" width="{$tableLeftWidth}">
					<span class="{$tableLeftText}">
						<xsl:value-of select="$lab_extension_45"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td align="left" width="{$tableRightWidth}">
					<span class="{$tableRightText}">
						<xsl:choose>
							<xsl:when test="$element_value!=''">
								<xsl:value-of select="$element_value"/>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ======================================================================== -->
	<xsl:template match="competency" mode="profile_attributes">
		<xsl:param name="is_end_user">true</xsl:param>
		<xsl:param name="showempty"/>
		<xsl:param name="readonly" select="@readonly"/>
		<xsl:variable name="competency" select="$user/competency/@title"/>
			<xsl:choose>
				<xsl:when test="$is_end_user = 'true' and $readonly != 'false'">
					<xsl:call-template name="usr_competency_read">
						<xsl:with-param name="element_value">
							<xsl:value-of select="$competency"/>
						</xsl:with-param>
						<xsl:with-param name="showempty" select="$showempty"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$is_end_user = 'false' and $readonly = 'all'">
					<xsl:call-template name="usr_competency_read">
						<xsl:with-param name="element_value">
							<xsl:value-of select="$competency"/>
						</xsl:with-param>
						<xsl:with-param name="showempty" select="$showempty"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="usr_competency">
						<xsl:with-param name="element_value">
							<xsl:value-of select="$competency"/>
						</xsl:with-param>
						<xsl:with-param name="required_field">false</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
	</xsl:template>
	<!-- ================= -->
	<!-- usr_competency -->
	<xsl:template name="usr_competency">
		<xsl:param name="required_field">false</xsl:param>
		<xsl:param name="max_length">500</xsl:param>
		<xsl:param name="element_value"/>
		<tr>
			<td class="wzb-form-label">
				<xsl:if test="$required_field = 'true'">
					<xsl:text>*</xsl:text>
				</xsl:if>
				<xsl:value-of select="$lab_competency"/>
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-form-control">
				<xsl:call-template name="wb_goldenman">
					<xsl:with-param name="frm">document.frmXml</xsl:with-param>
					<xsl:with-param name="width" select="$field_width"/>
					<xsl:with-param name="field_name">usr_competency_profile</xsl:with-param>
					<xsl:with-param name="tree_type">COMPETENCE_PROFILE</xsl:with-param>
					<xsl:with-param name="select_type">5</xsl:with-param>
					<xsl:with-param name="box_size">1</xsl:with-param>
					<xsl:with-param name="pick_root">0</xsl:with-param>
					<xsl:with-param name="single_option_text" select="$user/competency/@title"/>
					<xsl:with-param name="single_option_value" select="$user/competency/@id"/>
				</xsl:call-template>
			</td>
		</tr>
		<input type="hidden" name="lab_competency" value="{$lab_competency}"/>
		<input type="hidden" name="usr_competency"/>
	</xsl:template>
	<!-- ================= -->
	<!-- usr_competency -->
	<xsl:template name="usr_competency_read">
		<xsl:param name="element_value"/>
		<xsl:param name="showempty"/>
		<xsl:if test="$showempty = 'true' or $element_value!=''">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_competency"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="$element_value!=''">
							<xsl:value-of select="$element_value"/>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ====================== -->
	<xsl:template match="*" mode="profile_attributes"/>
	<!-- ======================================================================== -->
</xsl:stylesheet>

