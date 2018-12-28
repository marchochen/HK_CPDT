<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="../utils/escape_js.xsl"/>
	<xsl:import href="../utils/display_filetype_icon.xsl"/>
	<xsl:import href="../utils/change_lowercase.xsl"/>
	<xsl:import href="../utils/display_form_input_time.xsl"/>
	<xsl:import href="../utils/display_time.xsl"/>
	<xsl:import href="../utils/wb_goldenman.xsl"/>
	<xsl:import href="../utils/wb_gen_input_file.xsl"/>
	<xsl:import href="../utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="label_lrn_soln.xsl"/>
	<xsl:import href="label_role.xsl"/>
	<xsl:variable name="text_class">Text</xsl:variable>
	<xsl:variable name="itm_type_lst"/>
	<xsl:variable name="tcEnabled" select="//meta/tc_enabled"/>
	<xsl:variable name="training_plan" select="/applyeasy/meta/training_plan"/>
	<xsl:variable name="meta_blend_ind" select="/applyeasy/item/item_type_meta/@blend_ind"/>
	<xsl:variable name="lab_responser">
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">負責人</xsl:when>
			<xsl:when test="$wb_lang='gb'">负责人</xsl:when>
			<xsl:otherwise>Responsible person</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_expect">
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">計劃</xsl:when>
			<xsl:when test="$wb_lang='gb'">计划</xsl:when>
			<xsl:otherwise>Planned </xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_sel">
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">--請選擇--</xsl:when>
			<xsl:when test="$wb_lang='gb'">--请选择--</xsl:when>
			<xsl:otherwise>--Please select--</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- = wizBank Item Template Gen Form Utils ========================================== -->
	<!-- = Gereric Function Template ============================================== -->
	<xsl:template name="get_desc_name">
		<xsl:call-template name="escape_js">
			<xsl:with-param name="input_str">
				<xsl:call-template name="get_desc"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template name="get_desc_name_2">
		<xsl:call-template name="escape_js">
			<xsl:with-param name="input_str">
				<xsl:call-template name="get_desc_2"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =Get Desciption Template===================================================== -->
	<xsl:template name="get_desc">
		<xsl:choose>
			<xsl:when test="title">
				<xsl:choose>
					<xsl:when test="title/desc[@lan]">
						<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="title/desc/@name"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="../../title/desc[@lan = $wb_lang_encoding]/@name">
				<xsl:value-of select="../../title/desc[@lan = $wb_lang_encoding]/@name"/>
			</xsl:when>
			<xsl:when test="role">
				<xsl:call-template name="get_rol_title">
					<xsl:with-param name="rol_ext_id" select="role/@id"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="../../role">
				<xsl:call-template name="get_rol_title">
					<xsl:with-param name="rol_ext_id" select="../../role/@id"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="@name"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="get_desc_2">
		<xsl:choose>
			<xsl:when test="name(..) = 'subfield_list'">
				<xsl:value-of select="../../title/desc[@lan = $wb_lang_encoding]/@name"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =Get Name Template===================================================== -->
	<xsl:template name="get_name">
		<xsl:choose>
			<xsl:when test="name(..) = 'subfield_list'">
				<xsl:value-of select="concat(name(../..),'_',@id)"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="concat(name(),'_',@id)"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =================================================================== -->
	<xsl:template match="*" mode="get_dependant_name">
		<xsl:call-template name="get_name"/>
	</xsl:template>
	<xsl:template match="*" mode="get_dependant_desc_name">
		<xsl:call-template name="get_desc"/>
	</xsl:template>
	<!-- =JS Dependant======================================================== -->
	<xsl:template match="*[@dependant or @dependent]" mode="js_dependant">
		<xsl:variable name="dependant">
			<xsl:choose>
				<xsl:when test="@dependant">
					<xsl:value-of select="@dependant"/>
				</xsl:when>
				<xsl:when test="@dependent">
					<xsl:value-of select="@dependent"/>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc_name"/>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="_dependant_name">
			<xsl:choose>
				<xsl:when test="name() = 'subfield'">
					<xsl:value-of select="concat(name(../..), '_', @dependant)"/>
				</xsl:when>
				<xsl:when test="../../section/*[name() = $dependant]">
					<xsl:apply-templates select="../../section/*[name() = $dependant]" mode="get_dependant_name"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="../*[name() = $dependant]" mode="get_dependant_name"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="_dependant_desc_name">
			<xsl:apply-templates select="../*[name() = $dependant]" mode="get_dependant_desc_name"/>
		</xsl:variable>
		<!-- Date -->
		<xsl:choose>
			<xsl:when test="@type = 'date'">	
			//check for <xsl:value-of select="$_name"/>
			if (frm.<xsl:value-of select="$_name"/>_yy.value != '' &amp;&amp; frm.<xsl:value-of select="$_name"/>_mm.value != '' &amp;&amp; frm.<xsl:value-of select="$_name"/>_dd.value != ''){
				if(!wb_utils_validate_date_compare({
					frm : 'document.' + frm.name, 
					start_obj : '<xsl:value-of select="$_name"/>', 
					end_obj : '<xsl:value-of select="$_dependant_name"/>'
					})) {
					return false;
				}
			}
			

			//check for <xsl:value-of select="$_dependant_name"/>
			if (frm.<xsl:value-of select="$_dependant_name"/>_yy.value != '' &amp;&amp; frm.<xsl:value-of select="$_dependant_name"/>_mm.value != '' &amp;&amp; frm.<xsl:value-of select="$_dependant_name"/>_dd.value != ''){				
				if(!wb_utils_validate_date_compare({
					frm : 'document.' + frm.name, 
					start_obj : '<xsl:value-of select="$_name"/>', 
					end_obj : '<xsl:value-of select="$_dependant_name"/>'
					})) {
					return false;
				}
			}			

		</xsl:when>
			<xsl:when test="@type = 'datetime'">
			//check for <xsl:value-of select="$_name"/>
				<!--if (frm.<xsl:value-of select="$_name"/>_yy.value != '' &amp;&amp; frm.<xsl:value-of select="$_name"/>_mm.value != '' &amp;&amp; frm.<xsl:value-of select="$_name"/>_dd.value != '' &amp;&amp; frm.<xsl:value-of select="$_dependant_name"/>_radio[1].checked  ){-->
			if (frm.<xsl:value-of select="$_name"/>_yy.value != '' &amp;&amp; frm.<xsl:value-of select="$_name"/>_mm.value != '' &amp;&amp; frm.<xsl:value-of select="$_name"/>_dd.value != '' ){
				if(frm.<xsl:value-of select="$_dependant_name"/>_radio &amp;&amp; frm.<xsl:value-of select="$_dependant_name"/>_radio[1] &amp;&amp; !frm.<xsl:value-of select="$_dependant_name"/>_radio[1].checked) {
					//nothing to do
				} else {
					if(!wb_utils_validate_date_compare({
						frm : 'document.' + frm.name, 
						start_obj : '<xsl:value-of select="$_name"/>', 
						end_obj : '<xsl:value-of select="$_dependant_name"/>'
						})) {
						return false;
					}
				}
			}
			//check for <xsl:value-of select="$_dependant_name"/>
				if(frm.<xsl:value-of select="$_dependant_name"/>_radio &amp;&amp; frm.<xsl:value-of select="$_dependant_name"/>_radio[1] &amp;&amp; !frm.<xsl:value-of select="$_dependant_name"/>_radio[1].checked) {
					//nothing to do
				} else {
					if (frm.<xsl:value-of select="$_dependant_name"/>_yy.value != '' &amp;&amp; frm.<xsl:value-of select="$_dependant_name"/>_mm.value != '' &amp;&amp; frm.<xsl:value-of select="$_dependant_name"/>_dd.value != ''){
						if(!wb_utils_validate_date_compare({
							frm : 'document.' + frm.name, 
							start_obj : '<xsl:value-of select="$_name"/>', 
							end_obj : '<xsl:value-of select="$_dependant_name"/>'
							})) {
							return false;
						}
					}
				}
		</xsl:when>
			<xsl:when test="@type='pos_int'">
		_min_capacity = frm.<xsl:value-of select="$_name"/>.value;
		_max_capacity = frm.<xsl:value-of select="$_dependant_name"/>.value;
		if (parseInt(_min_capacity) &gt; parseInt(_max_capacity)){
			alert('<xsl:value-of select="$desc_name"/>'+' '+eval('wb_msg_'+'<xsl:value-of select="$wb_lang"/>'+'_cannot_be_larger')+' '+'<xsl:value-of select="$_dependant_desc_name"/>');
			frm.<xsl:value-of select="$_name"/>.focus();
			return false;
		}
		</xsl:when>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="valued_template" mode="js_dependant">
		<xsl:param name="itm_blend_ind"/>
		<xsl:param name="itm_exam_ind"/>
		<xsl:apply-templates select="*" mode="js_dependant">
			<xsl:with-param name="itm_blend_ind" select="$itm_blend_ind"/>
			<xsl:with-param name="itm_exam_ind" select="$itm_exam_ind"/>
		</xsl:apply-templates>
	</xsl:template>
	
	<xsl:template match="*" mode="js_dependant">
		<xsl:param name="itm_blend_ind"/>
		<xsl:param name="itm_exam_ind"/>
		<xsl:choose>
			<xsl:when test="$itm_blend_ind != 'true' and $itm_exam_ind != 'true'">
				<xsl:apply-templates select="*[not(@blend_ind and @blend_ind !=$itm_blend_ind) and not(@exam_ind and @exam_ind != $itm_exam_ind)]" mode="js_dependant"/>
			</xsl:when>
			<xsl:when test="$itm_exam_ind != 'true'">
				<xsl:apply-templates select="*[not(@exam_ind and @exam_ind != $itm_exam_ind)]" mode="js_dependant"/>
			</xsl:when>
			<xsl:when test="$itm_blend_ind != 'true'">
				<xsl:apply-templates select="*[not(@blend_ind and @blend_ind !=$itm_blend_ind)]" mode="js_dependant"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="*" mode="js_dependant"/>
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	<xsl:template match="text()" mode="js_dependant" priority="10"/>
	<!-- =Item Field======================================================== -->
	<xsl:template match="*[not(@type)]" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:if test="not(subfield_list)">
			<span class="{$text_class}">Error: No Type at &quot;<xsl:value-of select="name()"/>&quot;</span>
		</xsl:if>
	</xsl:template>
	<!-- =Constant Label===================================================== -->
	<xsl:template match="*[@type = 'constant_label']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:choose>
			<xsl:when test="name() = 'workflow_template'">
				<xsl:variable name="tpl_id" select="id/@value"/>
				<xsl:variable name="reference_tag" select="@ext_value_label_tag"/>
				<span class="{$text_class}">
					<xsl:value-of select="/applyeasy/meta/label_reference_data_list/label[@name = $reference_tag]/element[@id = $tpl_id]/title"/>
					<br/>
					<xsl:call-template name="unescape_html_linefeed">
						<xsl:with-param name="my_right_value">
							<xsl:value-of select="/applyeasy/meta/label_reference_data_list/label[@name = $reference_tag]/element[@id = $tpl_id]/description"/>
						</xsl:with-param>
					</xsl:call-template>
				</span>
			</xsl:when>
			<xsl:when test="*[(name() != 'title') and (name() != 'label')and (name() != 'item_type')]">
				<xsl:variable name="my_val" select="*[(name() != 'title') and (name() != 'label') and (name() != 'item_type')]/@value"/>
				<span class="{$text_class}">
					<xsl:value-of select="*[@id = $my_val]/desc[@lan = $wb_lang_encoding]/@name"/>&#160;</span>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="my_val" select="@value"/>
				<span class="{$text_class}">
					<xsl:value-of select="*[@id = $my_val]/desc[@lan = $wb_lang_encoding]/@name"/>&#160;</span>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<input type="hidden" name="{$_name}">
			<xsl:apply-templates select="@*" mode="attribute"/>
		</input>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}" value="{@value}"/>
		</xsl:if>
		<xsl:if test="@value">
			<xsl:variable name="my_val" select="@value"/>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<xsl:template match="item_status" mode="item_status"/>
	<!-- =Text / Email Field=================================================== -->
	<xsl:template match="*[@type = 'text'] | *[@type = 'email']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="style_width">
			<xsl:choose>
				<xsl:when test="(@size * 4) &gt; 450">450</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="@size * 4"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$training_plan ='true' and @paramname='itm_title'">
				<input type="text" style="width:{$style_width}px" name="{$_name}" onblur="javascript:onBlurEvent(this)" class="wzb-inputText">
					<xsl:attribute name="value"><xsl:value-of select="/applyeasy/plan/name"/></xsl:attribute>
				</input>
			</xsl:when>
			<xsl:when test="$training_plan ='true' and @paramname='itm_plan_code'">
				<xsl:value-of select="/applyeasy/plan/code"/>
				<input type="hidden" name="{$_name}" value="{/applyeasy/plan/code}"/>
			</xsl:when>
			<xsl:when test="@paramname='itm_plan_code'">
				<xsl:choose>
					<xsl:when test="@value !=''">
						<xsl:value-of select="@value"/>
					</xsl:when>
					<xsl:otherwise>--</xsl:otherwise>
				</xsl:choose>
				<input type="hidden" name="{$_name}" value="{@value}"/>
			</xsl:when>
			<xsl:when test="/applyeasy/meta/submitted_params_list">
				<xsl:choose>
					<xsl:when test="@paramname='itm_code'">
						<input type="text" style="width:200px" name="{$_name}" onblur="javascript:onBlurEvent(this)" class="wzb-inputText" maxlength="{@size}">
							<xsl:attribute name="value"><xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $_name]/value/text()"/></xsl:attribute>
						</input>
					</xsl:when>
					<xsl:otherwise>
						<input type="text" style="width:{$style_width}px" name="{$_name}" onblur="javascript:onBlurEvent(this)" class="wzb-inputText">
							<xsl:attribute name="value"><xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $_name]/value/text()"/></xsl:attribute>
						</input>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="@paramname='itm_title'">
						<input type="text" onkeydown="return noMaxlength(event,this,200)" style="width:{$style_width}px" name="{$_name}" onblur="javascript:onBlurEvent(this)" class="wzb-inputText">
							<xsl:apply-templates select="@*" mode="attribute"/>
						</input>
					</xsl:when>
					<xsl:when test="@paramname='itm_code'">
						<input type="text" style="width:200px" name="{$_name}" onblur="javascript:onBlurEvent(this)" class="wzb-inputText">
							<xsl:apply-templates select="@*" mode="attribute"/>
						</input>
					</xsl:when>
					<xsl:otherwise>
						<input type="text" style="width:{$style_width}px" name="{$_name}" onblur="javascript:onBlurEvent(this)" class="wzb-inputText">
							<xsl:apply-templates select="@*" mode="attribute"/>
						</input>
					</xsl:otherwise>
				</xsl:choose>
					
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- =================== ITEM TYPE ======================================== -->
	<xsl:template match="*[@type ='itm_type']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="itm_type" select="/applyeasy/item/item_type/@id"/>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}" value="{$itm_type}"/>
		</xsl:if>
		<xsl:call-template name="get_ity_title">
			<xsl:with-param name="itm_type" select="/applyeasy/item/item_type/@dummy_type"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ==================== Approval Request ============================== -->
	<xsl:template match="*[@type = 'approval_ind']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="lab_yes">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">是</xsl:when>
				<xsl:when test="$wb_lang='gb'">是</xsl:when>
				<xsl:otherwise>Yes</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_no">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">否</xsl:when>
				<xsl:when test="$wb_lang='gb'">否</xsl:when>
				<xsl:otherwise>No</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<span class="{$text_class}">
			<xsl:choose>
				<xsl:when test="//workflow_template/@approval_ind = 'true'">
					<xsl:value-of select="$lab_yes"/>
				</xsl:when>
				<xsl:when test="//workflow_template/@approval_ind = 'false'">
					<xsl:value-of select="$lab_no"/>
				</xsl:when>
				<xsl:otherwise><xsl:value-of select="$lab_no"/>
				</xsl:otherwise>
			</xsl:choose>
		</span>
	</xsl:template>
	<!-- ============================URL =======================-->
	<xsl:template match="*[@type = 'url']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="style_width">
			<xsl:choose>
				<xsl:when test="(@size * 4) &gt; 450">450</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="@size * 4"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<input type="text" style="width:{$style_width}px" name="{$_name}" class="wzb-inputText">
			<xsl:attribute name="onblur">javascript:onBlurEvent(this)</xsl:attribute>
			<xsl:choose>
				<xsl:when test="/applyeasy/meta/submitted_params_list">
					<xsl:attribute name="value"><xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $_name]/value/text()"/></xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="@*" mode="attribute"/>
					<xsl:if test="not(@value) or @value = ''">
						<xsl:attribute name="value">http://</xsl:attribute>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
		</input>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- =================== File ==========================-->
	<xsl:template match="*[@type = 'file']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="lab_remove_file">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">刪除檔案</xsl:when>
				<xsl:when test="$wb_lang='gb'">删除文档</xsl:when>
				<xsl:otherwise>Remove file</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_keep_file">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">保留檔案</xsl:when>
				<xsl:when test="$wb_lang='gb'">保留文档</xsl:when>
				<xsl:otherwise>Keep file</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_change_to">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">更改檔案為</xsl:when>
				<xsl:when test="$wb_lang='gb'">更改文档为</xsl:when>
				<xsl:otherwise>Change file to</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="@paramname != ''">
			<xsl:variable name="_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="@value != ''">
					<table cellpadding="3" cellspacing="0" border="0">
						<tr>
							<td rowspan="3">
								<a target="_blank">
									<xsl:attribute name="href"><xsl:choose><xsl:when test="/km">../object/<xsl:value-of select="/km/node/@id"/>/<xsl:value-of select="/km/node/version"/>/<xsl:value-of select="@value"/></xsl:when><xsl:when test="/applyeasy">../item/<xsl:value-of select="/applyeasy/item/@id"/>/<xsl:value-of select="@value"/></xsl:when><xsl:otherwise>#</xsl:otherwise></xsl:choose></xsl:attribute>
									<xsl:call-template name="display_filetype_icon">
										<xsl:with-param name="fileName">
											<xsl:value-of select="@value"/>
										</xsl:with-param>
									</xsl:call-template>
								</a>
							</td>
							<td>
								<label for="{$_name}_select0">
									<input type="radio" checked="checked" id="{$_name}_select0" name="{$_name}_select" onclick="document.frmXml.{@paramname}.disabled = true;"/>
									<span class="{$text_class}">
										<xsl:value-of select="$lab_keep_file"/>
									</span>
									<input name="{$_name}_hidden" type="hidden" value="{@value}"/>
								</label>
							</td>
						</tr>
						<tr>
							<td>
								<label for="{$_name}_select1">
									<input type="radio" id="{$_name}_select1" name="{$_name}_select" onclick="document.frmXml.{@paramname}.disabled = true;"/>
									<span class="{$text_class}">
										<xsl:value-of select="$lab_remove_file"/>
									</span>
								</label>
							</td>
						</tr>
						<tr>
							<td>
								<label for="{$_name}_select2">
									<input type="radio" id="{$_name}_select2" name="{$_name}_select" onclick="document.frmXml.{@paramname}.disabled = false;"/>
									<span class="{$text_class}">
										<xsl:value-of select="$lab_change_to"/>
									</span>
									<xsl:text>&#160;</xsl:text>
									<xsl:call-template name="wb_gen_input_file">
										<xsl:with-param name="name" select="@paramname"/>
										<xsl:with-param name="title" select="@value"/>
										<xsl:with-param name="onclick">document.frmXml.{$_name}_select2.checked=true;this.parentNode.firstChild.checked=true;</xsl:with-param>
										<xsl:with-param name="disabled">disabled</xsl:with-param>
									</xsl:call-template>
								</label>
							</td>
						</tr>
					</table>
					<input type="hidden" name="{$_name}"/>
					<input type="hidden" name="{@paramname}_fieldname" value="{$_name}"/>
					<input type="hidden" name="{@paramname}_del_ind" value=""/>
					<input type="hidden" name="{@paramname}_name" value=""/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="style_width">
						<xsl:choose>
							<xsl:when test="(@size * 4) > 450">450</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="@size * 4"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:call-template name="wb_gen_input_file">
										<xsl:with-param name="name" select="@paramname"/>
					</xsl:call-template>
						<xsl:apply-templates select="@*" mode="attribute"/>
					<xsl:if test="@paramname">
						<input type="hidden" name="{$_name}"/>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- ==================Image ===========-->
	<xsl:template match="*[@type = 'image']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="lab_remove_image">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">刪除媒體檔案</xsl:when>
				<xsl:when test="$wb_lang='gb'">删除媒体文档</xsl:when>
				<xsl:otherwise>Remove media file</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_image_file">
			<xsl:choose>
				<xsl:when test="$wb_lang = 'ch'">媒體檔案(JPG,GIF,PNG)</xsl:when>
				<xsl:when test="$wb_lang = 'gb'">媒体文档(JPG,GIF,PNG)</xsl:when>
				<xsl:when test="$wb_lang = 'en'">Media file(JPG,GIF,PNG)</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_keep_image">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">保留媒體檔案</xsl:when>
				<xsl:when test="$wb_lang='gb'">保留媒体文档</xsl:when>
				<xsl:otherwise>Keep media file</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_change_to">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">更改媒體檔案為</xsl:when>
				<xsl:when test="$wb_lang='gb'">更改媒体文档为</xsl:when>
				<xsl:otherwise>Media file change to</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="@paramname != ''">
			<xsl:variable name="_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>
			<script LANGUAGE="JavaScript" TYPE="text/javascript">
				function <xsl:value-of select="$_name"/>_remove(){
					document.all.<xsl:value-of select="$_name"/>_file.disabled = true;
					document.all.<xsl:value-of select="@paramname"/>_del_ind.value='true';
					document.all.<xsl:value-of select="$_name"/>_div_img.style.display = 'none';
					document.all.<xsl:value-of select="$_name"/>_div_swf.style.display = 'none';
				}
				
				function <xsl:value-of select="$_name"/>_img_change(frm) {
					file_ext = frm.<xsl:value-of select="@paramname"/>.value.substring( frm.<xsl:value-of select="@paramname"/>.value.lastIndexOf(".")+1,frm.<xsl:value-of select="@paramname"/>.value.length)
					file_ext =file_ext.toLowerCase()
					document.all.<xsl:value-of select="@paramname"/>_del_ind.value='true';
					if(file_ext == 'jpg' || file_ext == 'gif' || file_ext == 'png'){
						document.images.<xsl:value-of select="$_name"/>_preview.src = frm.<xsl:value-of select="@paramname"/>.value;
						<xsl:if test="@width !='' and @height != ''">
							document.images.<xsl:value-of select="$_name"/>_preview.width=<xsl:value-of select="@width"/>
							document.images.<xsl:value-of select="$_name"/>_preview.height=<xsl:value-of select="@height"/>
						</xsl:if>	
						if(document.all){
							document.all.<xsl:value-of select="$_name"/>_div_img.style.display = '';
							document.all.<xsl:value-of select="$_name"/>_div_swf.style.display = 'none';
						}
						if(document.all.<xsl:value-of select="$_name"/>_file.files &amp;&amp; document.all.<xsl:value-of select="$_name"/>_file.files[0]){
							document.images.<xsl:value-of select="$_name"/>_preview.src = window.URL.createObjectURL(document.all.<xsl:value-of select="$_name"/>_file.files[0]);
						}
					}else if(file_ext == 'swf' ){
						//to do , replace flash
						if(document.all){
							document.<xsl:value-of select="$_name"/>_movie.setAttribute('movie',frm.<xsl:value-of select="@paramname"/>.value)									
							document.all.<xsl:value-of select="$_name"/>_div_img.style.display = 'none';
							document.all.<xsl:value-of select="$_name"/>_div_swf.style.display = '';
						}
					}
				}

				function <xsl:value-of select="$_name"/>_file_status_change(frm){
					document.all.<xsl:value-of select="$_name"/>_file.disabled = false;
					
					
				<!-- 	$("#default_btn").prev().attr("disabled","disabled"); -->
					
					if(frm.<xsl:value-of select="@paramname"/>.value !='' ){
						file_ext = frm.<xsl:value-of select="@paramname"/>.value.substring( frm.<xsl:value-of select="@paramname"/>.value.lastIndexOf(".")+1,frm.<xsl:value-of select="@paramname"/>.value.length)
						file_ext =file_ext.toLowerCase();
						if(file_ext == 'jpg' || file_ext == 'gif' || file_ext == 'png'){
							document.images.<xsl:value-of select="$_name"/>_preview.src = frm.<xsl:value-of select="@paramname"/>.value;
							<xsl:if test="@width !='' and @height != ''">
								document.images.<xsl:value-of select="$_name"/>_preview.width=<xsl:value-of select="@width"/>
								document.images.<xsl:value-of select="$_name"/>_preview.height=<xsl:value-of select="@height"/>
							</xsl:if>	
							document.all.<xsl:value-of select="$_name"/>_div_img.style.display = '';	
							if(document.all.<xsl:value-of select="$_name"/>_file.files &amp;&amp; document.all.<xsl:value-of select="$_name"/>_file.files[0]){
								document.images.<xsl:value-of select="$_name"/>_preview.src = window.URL.createObjectURL(document.all.<xsl:value-of select="$_name"/>_file.files[0]);
							}										
						}
					}
				}
			</script>
			<xsl:choose>
				<xsl:when test="@value != ''">
					<table cellpadding="3" cellspacing="0" border="0">
						<tr>
							<td rowspan="3" width="252px">
								<xsl:variable name="file_ext">
									<xsl:call-template name="change_lowercase">
										<xsl:with-param name="input_value">
											<xsl:value-of select="substring-after(@value,'.')"/>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:variable>
								<script LANGUAGE="JavaScript" TYPE="text/javascript">
									function <xsl:value-of select="$_name"/>_show(type){
									
										//document.all.<xsl:value-of select="$_name"/>_file.disabled = true;
										<![CDATA[
										if($("#default_btn")!=null && $("#default_btn").prev()!=null)
										{
									<!-- 	<!--$("#default_btn").prev().attr('disabled','disabled'); --> -->
										}
									]]>
										<xsl:choose>
											<xsl:when test="$file_ext = 'swf'">
												<!-- 目前swf不被支持-->
												document.<xsl:value-of select="$_name"/>_movie.setAttribute('movie','../item/<xsl:value-of select="/applyeasy/item/@id"/>/<xsl:value-of select="@value"/>')
												document.all.<xsl:value-of select="$_name"/>_div_swf.style.display = '';										
											</xsl:when>
											<xsl:when test="$file_ext = 'gif' or $file_ext = 'jpg' or $file_ext = 'png'">
												document.images.<xsl:value-of select="$_name"/>_preview.src = '../item/<xsl:value-of select="/applyeasy/item/@id"/>/<xsl:value-of select="@value"/>';
												<xsl:if test="@width !='' and @height != ''">
													document.images.<xsl:value-of select="$_name"/>_preview.width=<xsl:value-of select="@width"/>
													document.images.<xsl:value-of select="$_name"/>_preview.height=<xsl:value-of select="@height"/>
												</xsl:if>
												document.all.<xsl:value-of select="$_name"/>_div_img.style.display = '';										
											</xsl:when>
											<xsl:otherwise/>
										</xsl:choose>
									} 
							
									$(function(){
										initDefaultImage('item', '<xsl:value-of select="$_name"/>', false);
									})
									
			
									
								</script>
		
								<xsl:choose>
									<xsl:when test="$file_ext = 'swf'">
									<!-- 不支持swf -->
										<div id="{$_name}_div_swf">
											<OBJECT name="{$_name}_movie" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0">
												<PARAM NAME="movie" VALUE="../item/{/applyeasy/item/@id}/{@value}"/>
												<EMBED src="../item/{/applyeasy/item/@id}/{@value}" TYPE="application/x-shockwave-flash" PLUGINSPAGE="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash"/>
											</OBJECT>
										</div>
										<div id="{$_name}_div_img" style="display:none">
											<img src="{$wb_img_path}tp.gif" name="{$_name}_preview" border="0"/>
										</div>
									</xsl:when>
									<xsl:when test="($file_ext = 'gif') or ($file_ext = 'jpg') or ($file_ext = 'png')">
										<div id="{$_name}_div_img">
											<img src="../item/{/applyeasy/item/@id}/{@value}" name="{$_name}_preview" border="0" width ="{@width}" height ="{@height}">
												<xsl:if test="name() = 'field113'">
													<xsl:attribute name="width">
														<xsl:text>120</xsl:text>
													</xsl:attribute>
												</xsl:if>
											</img>
										</div>
									</xsl:when>
									<xsl:otherwise/>
								</xsl:choose>
							</td>
							<td>
								<label for="{$_name}_select0">
									<input type="radio" checked="checked" id="{$_name}_select0" name="{$_name}_select" onclick="{$_name}_show();document.frmXml.{@paramname}.disabled = true;document.frmXml.select_file.value='';"/>
									<span class="{$text_class}">
										<xsl:value-of select="$lab_keep_image"/>
									</span>
									<input name="{$_name}_hidden" type="hidden" value="{@value}"/>
								</label>
							</td>
						</tr>
						<tr>
							<td>
								<label for="{$_name}_select1">
									<input type="radio" id="{$_name}_select1" name="{$_name}_select" value="use_default_image" onclick="useDefaultImage();document.frmXml.{@paramname}.disabled = true;document.frmXml.select_file.value='';"/>
									<input  name="" type="button" class="wzb-btn-blue" onclick="show_default_image();this.form.{$_name}_select1.checked=true;useDefaultImage()" value="{$lab_select_default_image}" style="border: 1px solid transparent;padding:3px 8px;"/>
									<a id="default_btn" href="#TB_inline?height=380&amp;width=580&amp;inlineId=myOnPageContent" class="thickbox" style="display: none;"></a>
									<input type="hidden" name="default_image"/>
									<input type="hidden" name="{@paramname}_del_ind"/>
								</label>
								<br/>
								<div id="myOnPageContent" style="display: none;">
								
									<div class="thickbox-big" >
									
										<div class="thickbox-tit thickbox-tit-1" style="text-align:center;">
											<xsl:value-of select="$lab_default_images"/>
										</div>
										
				 						<div class="thickbox-cont clearfix thickbox-user thickbox-content-2" id="defaultImages"></div>
				 						
										<div class="norm-border  thickbox-footer ">
											<input type="button" class="btn wzb-btn-blue wzb-btn-big  margin-right10" name="pertxt" onclick="selectImage();document.frmXml.{@paramname}.disabled = true;document.frmXml.select_file.value='';" value="{$lab_button_ok}" />
											<input    type="button" class="TB_closeWindowButton wzb-btn-big  btn wzb-btn-blue " name="pertxt" value="{$lab_g_form_btn_cancel}" />
										</div>
											
									</div>
							
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<label for="{$_name}_select2">
									<input type="radio" id="{$_name}_select2" name="{$_name}_select" onclick="{$_name}_file_status_change(document.frmXml);document.frmXml.{@paramname}.disabled = false;"/>
									<span class="{$text_class}">
										<xsl:value-of select="$lab_change_to"/>
									</span>
									<xsl:text>&#160;</xsl:text>
									<xsl:call-template name="wb_gen_input_file">
										<xsl:with-param name="name" select="@paramname"/>
										<xsl:with-param name="id" ><xsl:value-of select="$_name"/>_file</xsl:with-param>
										<xsl:with-param name="onclick">this.parentNode.parentNode.parentNode.firstChild.checked=true;this.parentNode.parentNode.firstChild.checked=true;</xsl:with-param>
										<!-- <xsl:with-param name="disabled">disabled</xsl:with-param> -->
										<xsl:with-param name="onchange"><xsl:value-of select="$_name"/>_img_change(document.frmXml)</xsl:with-param>
									</xsl:call-template>
									
									<xsl:variable name="img_des">
										<xsl:choose>
											<xsl:when test="$wb_lang='ch'">圖片規格建議：寬470px，高285px，支持JPG,GIF,PNG格式圖片</xsl:when>
											<xsl:when test="$wb_lang='gb'">图片规格建议：宽470px，高285px，支持JPG,GIF,PNG格式图片</xsl:when>
											<xsl:otherwise>Image size recommendation: width 470px, height 285px.Support JPG,GIF,PNG files.</xsl:otherwise>
										</xsl:choose>
									</xsl:variable>
									<xsl:if test="@width !='' and @height != ''">
										<span class="wzb-ui-module-text">
											&#160;&#160;<xsl:value-of select="$img_des"/><br/>
										</span>
									</xsl:if>
								</label>
							</td>
						</tr>
					</table>
					<input type="hidden" name="{$_name}"/>
					<input type="hidden" name="{@paramname}_name"/>
				</xsl:when>
				<xsl:otherwise>
					<script LANGUAGE="JavaScript" TYPE="text/javascript">
						$(function(){
							initDefaultImage('item', '<xsl:value-of select="$_name"/>', true);
						})
					</script>
					<table cellpadding="3" cellspacing="0" border="0">
						<tr>
							<td rowspan="2" style="width:252px;">
								<div id="{$_name}_div_img">
									<img name="{$_name}_preview" border="0" width ="{@width}" height ="{@height}">
										<xsl:if test="name() = 'field113'">
											<xsl:attribute name="width">
												<xsl:text>120</xsl:text>
											</xsl:attribute>
										</xsl:if>
									</img>
								</div>
							</td>
							<td>
								<label for="{$_name}_select1">
									<input type="radio" id="{$_name}_select1" checked="checked" name="{$_name}_select" value="use_default_image" onclick="useDefaultImage()"/>
									<input name="" type="button" class="wzb-btn-blue" style="border: 1px solid transparent;padding:3px 8px;" onclick="show_default_image();this.form.{$_name}_select1.checked=true;useDefaultImage()" value="{$lab_select_default_image}"/>
									<a id="default_btn" href="#TB_inline?height=380&amp;width=580&amp;inlineId=myOnPageContent" class="thickbox" style="display: none;"></a>
									<input type="hidden" name="default_image"/>
									<input type="hidden" name="{@paramname}_del_ind"/>
								</label>
								<br/>
								<div id="myOnPageContent" style="display: none;">
								
									<div class="thickbox-big">
									
										<div class="thickbox-tit  thickbox-tit-1" style="text-align:center;">
											<xsl:value-of select="$lab_default_images"/>
										</div>
		
										<div class="thickbox-cont clearfix thickbox-user thickbox-content-2" id="defaultImages"></div>
		
										<div class="norm-border thickbox-footer " >
											<input type="button" class="btn wzb-btn-blue wzb-btn-big  margin-right10" name="pertxt" onclick="selectImage()" value="{$lab_button_ok}" />
											<input    type="button" class=" TB_closeWindowButton wzb-btn-big   btn wzb-btn-blue " name="pertxt" value="{$lab_g_form_btn_cancel}" />
										</div>
									</div>
					
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<label for="{$_name}_select2">
									<input type="radio" id="{$_name}_select2" name="{$_name}_select" onclick="{$_name}_file_status_change(document.frmXml)"/>
									<span class="{$text_class}">
										<xsl:value-of select="$lab_change_to"/>
									</span>
									<xsl:text>&#160;</xsl:text>
									<xsl:call-template name="wb_gen_input_file">
										<xsl:with-param name="name" select="@paramname"/>
										<xsl:with-param name="id" ><xsl:value-of select="$_name"/>_file</xsl:with-param> <!-- document.frmXml.{$_name}_select2.checked=true; -->
										<xsl:with-param name="onclick">this.parentNode.parentNode.firstChild.checked=true;</xsl:with-param>
										<!-- <xsl:with-param name="disabled">disabled</xsl:with-param> -->
										<xsl:with-param name="onchange"><xsl:value-of select="$_name"/>_img_change(document.frmXml)</xsl:with-param>
									</xsl:call-template>
									<xsl:variable name="img_des">
										<xsl:choose>
											<xsl:when test="$wb_lang='ch'">圖片規格建議：寬470px，高285px，支持JPG,GIF,PNG格式圖片</xsl:when>
											<xsl:when test="$wb_lang='gb'">图片规格建议：宽470px，高285px，支持JPG,GIF,PNG格式图片</xsl:when>
											<xsl:otherwise>Image size recommendation: width 470px, height 285px.Support JPG,GIF,PNG files.</xsl:otherwise>
										</xsl:choose>
									</xsl:variable>
									<xsl:if test="@width !='' and @height != ''">
										<span class="text">
											&#160;&#160;<xsl:value-of select="$img_des"/><br/>
										</span>
									</xsl:if>
								</label>
							</td>
						</tr>
					</table>
					<input type="hidden" name="{$_name}"/>
					<input type="hidden" name="{@paramname}_name"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- ====================== TextArea ===============================-->
	<xsl:template match="*[@type = 'textarea']" mode="field_type">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc"/>
		</xsl:variable>
		<xsl:if test="$training_plan = 'true' and @name = 'Duration'">
			<xsl:if test="/applyeasy/plan/duration !=''">
				<span class="Desc">
					<xsl:text>【</xsl:text>
					<xsl:value-of select="$lab_expect"/>
					<xsl:value-of select="$desc_name"/>
					<xsl:text>：</xsl:text>
					<xsl:value-of select="/applyeasy/plan/duration"/>
					<xsl:text>】</xsl:text>
				</span>
				<br/>
			</xsl:if>
		</xsl:if>
		<textarea style="width:400px" rows="4" name="{$_name}" onblur="javascript:onBlurEvent(this)" class="wzb-inputTextArea">
			<xsl:choose>
				<xsl:when test="$training_plan = 'true' and @paramname = 'itm_desc'">
					<xsl:value-of select="/applyeasy/plan/introduction"/>
				</xsl:when>
				<xsl:when test="$training_plan = 'true' and @name='Objective'">
					<xsl:value-of select="/applyeasy/plan/aim"/>
				</xsl:when>
				<xsl:when test="$training_plan = 'true' and @name = 'Target audience'">
					<xsl:value-of select="/applyeasy/plan/target"/>
				</xsl:when>
				<xsl:when test="$training_plan = 'true' and @name = 'Remarks'">
					<xsl:value-of select="/applyeasy/plan/remarks"/>
				</xsl:when>
				<xsl:when test="/applyeasy/meta/submitted_params_list">
					<xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $_name]/value/text()"/>
				</xsl:when>	
				<xsl:otherwise>
					<xsl:value-of select="@value"/>
				</xsl:otherwise>
			</xsl:choose>
		</textarea>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- ==Pos Int & Pos Ammount===-->
	<xsl:template match="*[@type = 'pos_int'] | *[@type = 'pos_amount'] | *[@type='pos_amount_bonus']" mode="field_type">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<input type="text" name="{$_name}" class="wzb-inputText" onblur="javascript:onBlurEvent(this)">
			<xsl:choose>
				<xsl:when test="/applyeasy/meta/submitted_params_list">
					<xsl:attribute name="value">
						<xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $_name]/value/text()"/>
					</xsl:attribute>
				</xsl:when>
				<xsl:otherwise><xsl:apply-templates select="@*" mode="attribute"/></xsl:otherwise>
			</xsl:choose>
			<xsl:if test="@paramname = 'itm_diff_factor'">
				<xsl:attribute name="size">5</xsl:attribute>
				<xsl:if test="not(@value) or @value = ''">
					<xsl:attribute name="value">1</xsl:attribute>
				</xsl:if>
			</xsl:if>
		</input>
		<xsl:if test="@type = 'pos_amount_bonus'">
			<script type="text/javascript"><![CDATA[
				if(document.frmXml.field25_ && document.frmXml.field26_){
					if(document.frmXml.field25_[0].checked==true) {
						document.frmXml.field26_.disabled = false;
					} else if(document.frmXml.field25_[1].checked==true){
						document.frmXml.field26_.disabled = true;
					}		
				}
			]]></script>
		</xsl:if>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- ==Pos Int & Pos Ammount External===-->
	<xsl:template match="*[@type = 'pos_int'] | *[@type = 'pos_amount'] | *[@type='pos_amount_bonus']" mode="ext_field_type">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<input type="text" name="{$_name}" class="wzb-inputText">
			<xsl:attribute name="onblur">javascript:onBlurEvent(this)</xsl:attribute>
			<!--<xsl:apply-templates select="@*" mode="attribute"/>-->
			<xsl:attribute name="value"><xsl:value-of select="rating_target/@value"/></xsl:attribute>
		</input>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- ==Check Box ==========-->
	<xsl:template match="*[@type = 'checkbox']" mode="field_type">
		<xsl:param name="text_class"/>
		<span class="{$text_class}">11
			<xsl:for-each select="*[name() != 'title']">
				<xsl:variable name="desc_name">
					<xsl:call-template name="get_desc"/>
				</xsl:variable>
				<xsl:variable name="_name">
					<xsl:choose>
						<xsl:when test="../@paramname">
							<xsl:value-of select="../@paramname"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="get_name"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="@selected = 'true'">
						<label for="{concat(name(),@id,'id')}">
							<input type="checkbox" name="{$_name}" checked="checked" id="{concat(name(),@id,'id')}">
								<xsl:apply-templates select="@*" mode="attribute"/>
							</input>	&#160;<xsl:value-of select="$desc_name"/>
						</label>
					</xsl:when>
					<xsl:otherwise>
						<label for="{concat(name(),@id,'id')}">
							<input type="checkbox" name="{$_name}" id="{concat(name(),@id,'id')}">
								<xsl:apply-templates select="@*" mode="attribute"/>
							</input>	&#160;<xsl:value-of select="$desc_name"/>
						</label>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="position() != last()">
					<br/>
				</xsl:if>
			</xsl:for-each>
		</span>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- ==================== Radio Button ================================================== -->
	<xsl:template match="*[@type = 'radio']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="node_name"><xsl:value-of select="@name"/></xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="desc_name_n">
			<xsl:call-template name="get_desc_name"/>
		</xsl:variable>
			<xsl:variable name="lab_download">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">預覽</xsl:when>
				<xsl:when test="$wb_lang='gb'">预览</xsl:when>
				<xsl:otherwise>Preview</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_not_cert">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">找不到證書</xsl:when>
				<xsl:when test="$wb_lang='gb'">找不到证书</xsl:when>
				<xsl:otherwise>No certificate is defined</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:variable name="lab_desc">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">（你可以在"證書管理"功能中管理證書）</xsl:when>
				<xsl:when test="$wb_lang='gb'">（你可以在"证书管理"功能中管理证书）</xsl:when>
				<xsl:otherwise>(You can maintain the list in Certificate Management)</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<span class="{$text_class}">
			<xsl:for-each select="*[name() != 'title']">
				<xsl:variable name="desc_name">
					<xsl:call-template name="get_desc"/>
				</xsl:variable>
				<xsl:choose>
					<!-- as request hardcode item status to invisible  -->
					<xsl:when test="name() = 'item_status'and count(../*[@selected = 'true']) = 0 ">
						<label for="{concat(name(),@id,'id')}">
							<input type="radio" name="{$_name}" id="{concat(name(),@id,'id')}">
								<xsl:choose>
									<xsl:when test="/applyeasy/meta/submitted_params_list">
										<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_name]/value/text() = @value">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<xsl:if test="../../../../@status = @value">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:apply-templates select="@*" mode="attribute"/>
							</input>
							<xsl:value-of select="$desc_name"/>
						</label>
					</xsl:when>
					<!--as request hardcode item status to invisible-->
					<xsl:when test="position()=1 and count(../*[@selected = 'true']) = 0 ">
						<label for="{concat(name(),@id,'id')}">
							<!--<input type="radio" name="{$_name}" id="{concat(name(),@id,'id')}" checked="checked">-->
							<input type="radio" name="{$_name}" id="{concat(name(),@id,'id')}">
							    <xsl:if test="$node_name= 'Certificate_of_completion'">
									<xsl:attribute name="onclick">javascript:cert.change_cert_set(document.frmXml,this,'<xsl:value-of select="$_name"/>','<xsl:value-of select="$lab_sel"/>')</xsl:attribute>
							    </xsl:if>
							     <xsl:if test="$node_name= 'inst_set'">
									<xsl:attribute name="onclick">javascript:inst.inst_set_change(document.frmXml,this)</xsl:attribute>
							    </xsl:if>
								<xsl:choose>
									<xsl:when test="/applyeasy/meta/submitted_params_list">
										<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_name]/value/text() = @value">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:apply-templates select="@*" mode="attribute"/>
							</input>
							<xsl:value-of select="$desc_name"/>
						</label>
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="val" select="@value"/>
						<xsl:variable name="radio_selected">
							<xsl:choose>
								<xsl:when test="/applyeasy/meta/submitted_params_list">
									<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_name]/value/text() = @value">true</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="@selected"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:choose>
							<!--<xsl:when test="@selected = 'true'">-->
							<xsl:when test="$radio_selected = 'true'">
								<label for="{concat(name(),@id,'id')}">
									<input type="radio" name="{$_name}" id="{concat(name(),@id,'id')}" checked="checked">
									    <xsl:if test="$node_name= 'Certificate_of_completion'">
											<xsl:attribute name="onclick">javascript:cert.change_cert_set(document.frmXml,this,'<xsl:value-of select="$_name"/>','<xsl:value-of select="$lab_sel"/>')</xsl:attribute>
										</xsl:if>
										<xsl:if test="$node_name= 'inst_set'">
											<xsl:attribute name="onclick">javascript:inst.inst_set_change(document.frmXml,this)</xsl:attribute>
									    </xsl:if>
										<xsl:apply-templates select="@*" mode="attribute"/>
									</input>
									<xsl:value-of select="$desc_name"/>
								</label>
							</xsl:when>
							<xsl:otherwise>
								<label for="{concat(name(),@id,'id')}">
									<input type="radio" name="{$_name}" id="{concat(name(),@id,'id')}">
										<xsl:if test="$node_name= 'Certificate_of_completion'">
											<xsl:attribute name="onclick">javascript:cert.change_cert_set(document.frmXml,this,'<xsl:value-of select="$_name"/>','<xsl:value-of select="$lab_sel"/>')</xsl:attribute>
										</xsl:if>
										<xsl:if test="$node_name= 'inst_set'">
											<xsl:attribute name="onclick">javascript:inst.inst_set_change(document.frmXml,this)</xsl:attribute>
									    </xsl:if>
									    <xsl:if test="../@paramname = 'itm_retake_ind' and count(../*[@selected = 'true']) = 0 ">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
										<xsl:apply-templates select="@*" mode="attribute"/>
									</input>
									<xsl:value-of select="$desc_name"/>
								</label>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="@value='true' and $node_name= 'Certificate_of_completion'">
				    <img border="0" height="1" src="{$wb_img_path}tp.gif" width="5"/><span style="display:none" id="cert_sel_form"><select name="{$_name}_box" class="Select"></select><img border="0" height="1" src="{$wb_img_path}tp.gif" width="5"/>
				    <xsl:call-template name="wb_gen_form_button">
				    <xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_download"/></xsl:with-param>
				    <xsl:with-param name="field_name"><xsl:value-of select="$_name"/>_button</xsl:with-param>
				    <xsl:with-param name="wb_gen_btn_href">javascript:cert.itm_preview_cert(document.frmXml,'<xsl:value-of select="$_name"/>','<xsl:value-of select="$desc_name_n"/>','<xsl:value-of select="/applyeasy/item/@id"/>')</xsl:with-param>
				    </xsl:call-template>
				    </span><span style="display:none" id="no_cert"><xsl:value-of select="$lab_not_cert"/></span>
				    <input type="hidden" name="itm_cfc_id"/>
					<br/> <img border="0" height="1" src="{$wb_img_path}tp.gif" width="5"/><span class="wzb-ui-module-text"><xsl:value-of select="$lab_desc"/></span>
				</xsl:if>

				<xsl:if test="position() != last() and $node_name != 'inst_set'">
					<br/>
				</xsl:if>
			</xsl:for-each>
			<xsl:if test="@paramname">
				<input type="hidden" name="{@paramname}"/>
			</xsl:if>
		</span>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
		<xsl:if test="$node_name= 'Certificate_of_completion'">
			<script LANGUAGE="JavaScript" TYPE="text/javascript">
		 
		    var pre_tcr_id;
		    var count = <xsl:value-of select="count(//cert_lst/cert)"/>
		    var cert_id_list = new Array(count);
		    var cert_title_list = new Array(count);
		    var cert_tcr_id_list = new Array(count);
		    var field_name = '<xsl:value-of select="$_name"/>';
		    <xsl:choose>
				<xsl:when test="/applyeasy/item/@itm_cfc_id"> var itm_cfc_id = <xsl:value-of select="/applyeasy/item/@itm_cfc_id"/>;</xsl:when>
				<xsl:otherwise> var itm_cfc_id = 0;</xsl:otherwise>
			</xsl:choose>
		    var i = 0;
		  <xsl:for-each select="//cert_lst/cert">
				cert_id_list[i] = <xsl:value-of select="@cfc_id"/>;
				cert_title_list[i] = '<xsl:value-of select="@cfc_title"/>';
				cert_tcr_id_list[i] = '<xsl:value-of select="@cfc_tcr_id"/>';
				i = i + 1;
		</xsl:for-each>
 
        cert.init_cert_set(document.frmXml,'<xsl:value-of select="$lab_sel"/>','<xsl:value-of select="$_name"/>');
       
	   </script>
		</xsl:if>
		
		 <xsl:if test="$node_name = 'inst_set'">
			 <script LANGUAGE="JavaScript" TYPE="text/javascript">
	       		 for(rad=0;rad &lt;  document.frmXml.<xsl:value-of select="$_name"/>.length;rad++){
					if(document.frmXml.<xsl:value-of select="$_name"/>[rad].checked == true){
						last_instr_set = document.frmXml.<xsl:value-of select="$_name"/>[rad].value;
					}
				}
      	  </script>
		</xsl:if>
	</xsl:template>
	<!-- ==================== Radio Button ================================================== -->
	<xsl:template match="*[@type = 'radio_bonus']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:for-each select="*[name() != 'title']">
			<xsl:variable name="desc_name">
				<xsl:call-template name="get_desc"/>
			</xsl:variable>
			<script language="Javascript" type="text/javascript">
				function change_diff(val) {
					if(document.frmXml.<xsl:value-of select="$_name"/>[0].checked==true) {
						document.frmXml.field26_.disabled = false;
					} else if(document.frmXml.<xsl:value-of select="$_name"/>[1].checked==true){				
						document.frmXml.field26_.disabled = true;
					}
				}
			</script>
			<xsl:choose>
				<xsl:when test="position()=2 and count(../*[@selected = 'true']) = 0 ">
					<label for="{concat(name(),@id,'id')}">
						<input type="radio" name="{$_name}" id="{concat(name(),@id,'id')}" onclick="javascript:change_diff(this)">
							<xsl:choose>
								<xsl:when test="/applyeasy/meta/submitted_params_list">
									<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_name]/value/text() = @value">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:apply-templates select="@*" mode="attribute"/>
						</input>
						<xsl:value-of select="$desc_name"/>
					</label>
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="val" select="@value"/>
					<xsl:variable name="radio_selected">
						<xsl:choose>
							<xsl:when test="/applyeasy/meta/submitted_params_list">
								<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_name]/value/text() = @value">true</xsl:if>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="@selected"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="$radio_selected = 'true'">
							<label for="{concat(name(),@id,'id')}">
								<input type="radio" name="{$_name}" id="{concat(name(),@id,'id')}" checked="checked" onclick="javascript:change_diff(this)">
									<xsl:apply-templates select="@*" mode="attribute"/>
								</input>
								<xsl:value-of select="$desc_name"/>
							</label>
						</xsl:when>
						<xsl:otherwise>
							<label for="{concat(name(),@id,'id')}">
								<input type="radio" name="{$_name}" id="{concat(name(),@id,'id')}" onclick="javascript:change_diff(this)">
									<xsl:apply-templates select="@*" mode="attribute"/>
								</input>
								<xsl:value-of select="$desc_name"/>
							</label>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:if test="position() != last()">
				<br/>
			</xsl:if>
		</xsl:for-each>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- == UnOrder List ========-->
	<xsl:template match="*[@type = 'ul']" mode="field_type">
		<xsl:param name="text_class"/>
		<span class="{$text_class}">
			<ul>
				<xsl:for-each select="*[name() != 'title']">
					<li>
						<xsl:apply-templates select="." mode="field_type"/>
					</li>
				</xsl:for-each>
			</ul>
		</span>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- == UnOrder List ========-->
	<xsl:template match="*[@type = 'ol']" mode="field_type">
		<xsl:param name="text_class"/>
		<span class="{$text_class}">
			<ol>
				<xsl:for-each select="*[name() != 'title']">
					<li>
						<xsl:apply-templates select="." mode="field_type"/>
					</li>
				</xsl:for-each>
			</ol>
		</span>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- == Named Url ========-->
	<xsl:template match="*[@type = 'named_url']" mode="field_type">
		<xsl:param name="text_class"/>
		<span class="{$text_class}">
			<xsl:for-each select="*[name() != 'title']">
				<xsl:call-template name="get_desc"/>&#160;<xsl:apply-templates select="." mode="field_type"/>
				<xsl:if test="position() != last()">
					<br/>
				</xsl:if>
			</xsl:for-each>
		</span>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- ==Select ===========-->
	<xsl:template match="*[@type = 'select'or @type='reloading_select']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<select class="Select" name="{$_name}">
			<xsl:if test="@type='reloading_select'">
				<xsl:attribute name="onchange">parent.location.href=setUrlParam('<xsl:value-of select="@paramname"/>',this.options[this.selectedIndex].value,parent.location.href)</xsl:attribute>
			</xsl:if>
			<xsl:apply-templates select="@*" mode="attribute"/>
			<xsl:apply-templates select="*" mode="option">
				<xsl:with-param name="parent_value">
					<xsl:value-of select="@value"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</select>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- ==Select Option ===========-->
	<xsl:template match="*" mode="option">
		<xsl:param name="parent_value"/>
		<option>
			<xsl:if test="@value">
				<xsl:attribute name="value"><xsl:value-of select="@value"/></xsl:attribute>
			</xsl:if>
			<xsl:if test="$parent_value = @value">
				<xsl:attribute name="selected">selected</xsl:attribute>
			</xsl:if>
			<xsl:variable name="desc_name">
				<xsl:call-template name="get_desc"/>
			</xsl:variable>
			<xsl:apply-templates select="@*" mode="attribute"/>
			<xsl:value-of select="$desc_name"/>
		</option>
	</xsl:template>
	<xsl:template match="title" mode="option"/>
	<!-- ==IAC Select ===========-->
	<!--<xsl:template match="*[@type = 'iac_select']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="count(assigned_role_list/role/entity) != 0">
				<span class="Text">
					<select class="Select" name="{$_name}">
						<xsl:apply-templates select="@*" mode="attribute"/>
						<xsl:for-each select="assigned_role_list/role/entity">
							<option value="{@id}">
								<xsl:value-of select="@display_bil"/>
							</option>
						</xsl:for-each>
					</select>
				</span>
			</xsl:when>
			<xsl:otherwise>
				<input type="hidden" name="{$_name}"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>-->
	<!-- ================ Date =============================-->
	<xsl:template match="*[@type = 'date']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<span class="{$text_class}">
			<input type="hidden" name="{$_name}" value="{@value}">
				<xsl:apply-templates select="@*" mode="attribute"/>
			</input>
			<xsl:call-template name="display_form_input_time">
				<xsl:with-param name="fld_name">
					<xsl:value-of select="$_name"/>
				</xsl:with-param>
				<xsl:with-param name="frm">document.frmXml</xsl:with-param>
				<xsl:with-param name="show_label">Y</xsl:with-param>
				<xsl:with-param name="timestamp">
					<xsl:choose>
						<xsl:when test="/applyeasy/meta/submitted_params_list">
							<xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $_name]/value/text()"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="@value"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:with-param>
				<xsl:with-param name="def_time">
					<xsl:value-of select="@def_time"/>
				</xsl:with-param>
				<xsl:with-param name="display_form_input_hhmm">N</xsl:with-param>
				<xsl:with-param name="caching_function">javascript:onBlurEvent(this)</xsl:with-param>
			</xsl:call-template>
		</span>
		<xsl:if test="$training_plan = 'true'">
			<span class="Desc">
				<xsl:choose>
					<xsl:when test="@paramname = 'itm_eff_start_datetime' and /applyeasy/plan/ftf_start_date!=''">
						<xsl:text>【</xsl:text>
						<xsl:value-of select="$lab_expect"/>
						<xsl:text>：</xsl:text>
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp" select="/applyeasy/plan/ftf_start_date"/>
						</xsl:call-template>
						<xsl:text>】</xsl:text>
					</xsl:when>
					<xsl:when test="@paramname = 'itm_eff_end_datetime' and /applyeasy/plan/ftf_end_date !=''">
						<xsl:text>【</xsl:text>
						<xsl:value-of select="$lab_expect"/>
						<xsl:text>：</xsl:text>
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp" select="/applyeasy/plan/ftf_end_date"/>
						</xsl:call-template>
						<xsl:text>】</xsl:text>
					</xsl:when>
					<xsl:when test="@paramname = 'itm_content_eff_start_datetime' and /applyeasy/plan/wb_start_date !=''">
						<xsl:text>【</xsl:text>
						<xsl:value-of select="$lab_expect"/>
						<xsl:text>：</xsl:text>
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp" select="/applyeasy/plan/wb_start_date"/>
						</xsl:call-template>
						<xsl:text>】</xsl:text>
					</xsl:when>
					<xsl:when test="@paramname = 'itm_content_eff_end_datetime' and /applyeasy/plan/wb_end_date!=''">
						<xsl:text>【</xsl:text>
						<xsl:value-of select="$lab_expect"/>
						<xsl:text>：</xsl:text>
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp" select="/applyeasy/plan/wb_end_date"/>
						</xsl:call-template>
						<xsl:text>】</xsl:text>
					</xsl:when>
				</xsl:choose>
			</span>
		</xsl:if>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- =================Date Time========================================-->
	<xsl:template match="*[@type = 'datetime']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
			<xsl:variable name="timestamp">
				<xsl:choose>
					<xsl:when test="/applyeasy/meta/submitted_params_list">
						<xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $_name]/value/text()"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="@value"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<input type="hidden" name="{$_name}" value="{@value}">
				<xsl:apply-templates select="@*" mode="attribute"/>
			</input>
			<xsl:call-template name="display_form_input_time">
				<xsl:with-param name="fld_name">
					<xsl:value-of select="$_name"/>
				</xsl:with-param>
				<xsl:with-param name="frm">document.frmXml</xsl:with-param>
				<xsl:with-param name="show_label">Y</xsl:with-param>
				<xsl:with-param name="timestamp">
					<xsl:value-of select="$timestamp"/>
				</xsl:with-param>
				<xsl:with-param name="def_date">
					<xsl:value-of select="@def_date"/>
				</xsl:with-param>
				<xsl:with-param name="def_time">
					<xsl:value-of select="@def_time"/>
				</xsl:with-param>
				<xsl:with-param name="display_form_input_hhmm">Y</xsl:with-param>
				<xsl:with-param name="caching_function">javascript:onBlurEvent(this)</xsl:with-param>
			</xsl:call-template>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
		<br/>
	</xsl:template>
	<!-- =================== Date Time & Unlimited =======================-->
	<xsl:template match="*[@type = 'datetime_unlimited']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="lab_unlimited">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">不限</xsl:when>
				<xsl:when test="$wb_lang='gb'">不限</xsl:when>
				<xsl:otherwise>Unlimited</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="_fieldname">
			<xsl:value-of select="$_name"/>_radio</xsl:variable>
		<span class="{$text_class}">
			<label for="{$_name}_radio_id">
				<input type="radio" name="{$_name}_radio" value="1" id="{$_name}_radio_id">
					<xsl:choose>
						<xsl:when test="/applyeasy/meta/submitted_params_list">
							<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text() = '1'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:if test=" @value = '9999-12-31 23:59:59.000' or not(@value) or  @value ='9999-12-31 23:59:59.0'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<xsl:value-of select="$lab_unlimited"/>
			</label>
			<br/>
			<input type="hidden" name="{$_name}" value="{@value}">
				<xsl:apply-templates select="@*" mode="attribute"/>
			</input>
			<input type="radio" name="{$_name}_radio" value="2">
				<xsl:choose>
					<xsl:when test="/applyeasy/meta/submitted_params_list">
						<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text() = '2'">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test=" @value != '' and @value != '9999-12-31 23:59:59.000' and  @value !='9999-12-31 23:59:59.0'">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</input>
			<xsl:variable name="timestamp">
				<xsl:choose>
					<xsl:when test="/applyeasy/meta/submitted_params_list">
						<xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $_name]/value/text()"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="@value != '9999-12-31 23:59:59.000' and @value != '9999-12-31 23:59:59.0'">
							<xsl:value-of select="@value"/>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:call-template name="display_form_input_time">
				<xsl:with-param name="fld_name">
					<xsl:value-of select="$_name"/>
				</xsl:with-param>
				<xsl:with-param name="frm">document.frmXml</xsl:with-param>
				<xsl:with-param name="show_label">Y</xsl:with-param>
				<xsl:with-param name="def_date">
					<xsl:value-of select="@def_date"/>
				</xsl:with-param>
				<xsl:with-param name="def_time">
					<xsl:value-of select="@def_time"/>
				</xsl:with-param>
				<xsl:with-param name="timestamp">
					<xsl:value-of select="$timestamp"/>
				</xsl:with-param>
				<xsl:with-param name="display_form_input_hhmm">Y</xsl:with-param>
				<xsl:with-param name="caching_function">javascript:onBlurEvent(this)</xsl:with-param>
				<xsl:with-param name="focus_rad_btn_name"><xsl:value-of select="$_name"/>_radio[1]</xsl:with-param>
			</xsl:call-template>
		</span>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
	</xsl:template>
	<!-- == content_eff_start_end ============-->
	<xsl:template match="*[@type = 'content_eff_start_end']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="value" select="*[name() != 'title']/@value"/>
		<xsl:variable name="lab_unlimited">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">不限</xsl:when>
				<xsl:when test="$wb_lang='gb'">不限</xsl:when>
				<xsl:otherwise>Unlimited</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_content_eff_start">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">當學員被成功報名</xsl:when>
				<xsl:when test="$wb_lang='gb'">当学员被成功报名</xsl:when>
				<xsl:otherwise>When the learner is successfully enrolled</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_days">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">天</xsl:when>
				<xsl:when test="$wb_lang='gb'">天</xsl:when>
				<xsl:otherwise>days</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_from">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">由</xsl:when>
				<xsl:when test="$wb_lang='gb'">由</xsl:when>
				<xsl:otherwise>From</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
			<xsl:variable name="lab_to">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">至</xsl:when>
				<xsl:when test="$wb_lang='gb'">至</xsl:when>
				<xsl:otherwise>To</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_duration_suffix">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">(從學員被成功報名後開始計算)</xsl:when>
				<xsl:when test="$wb_lang='gb'">(从学员被成功报名后开始计算)</xsl:when>
				<xsl:otherwise>(After the learner is successfully enrolled)</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc"/>
		</xsl:variable>
		<xsl:variable name="_fieldname">
			<xsl:value-of select="$_name"/>_radio</xsl:variable>

		<table border="0" cellpadding="0" cellspacing="0">
			<xsl:if test="$training_plan = 'true'">
				<xsl:if test="/applyeasy/plan/wb_start_date !='' or /applyeasy/plan/wb_end_date !=''">
					<tr>
						<td colspan="2" class="Desc">
							<xsl:text>【</xsl:text>
							<xsl:value-of select="$lab_expect"/>
							<xsl:value-of select="$desc_name"/>
							<xsl:text>：</xsl:text>
							<xsl:value-of select="$lab_from"/>
							<xsl:text> </xsl:text>
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp" select="/applyeasy/plan/wb_start_date"/>			
							</xsl:call-template>
							<xsl:text> </xsl:text>
							<xsl:value-of select="$lab_to"/>
							<xsl:text> </xsl:text>
								<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp" select="/applyeasy/plan/wb_end_date"/>			
							</xsl:call-template>
							<xsl:text>】</xsl:text>
						</td>
					</tr>
				</xsl:if>
			</xsl:if>
			<tr>
				<td valign="top" width="2%"><xsl:value-of select="$lab_const_from"/><xsl:text>：</xsl:text></td>
				<td valign="top" width="98%"><xsl:value-of select="$lab_content_eff_start"/></td>
			</tr>
			<tr>
				<td valign="top"><xsl:value-of select="$lab_const_cap_to"/><xsl:text>：</xsl:text></td>
				<td valign="top">
					<span class="{$text_class}">
						<label for="{$_name}_radio_id1">
							<input type="radio" name="{$_name}_radio" value="1" id="{$_name}_radio_id1">
								<xsl:choose>
									<xsl:when test="/applyeasy/meta/submitted_params_list">
										<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text() = '1'">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<xsl:if test="not($value) or $value = ''">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>
							</input>
							<xsl:value-of select="$lab_unlimited"/>
						</label>
						<br/>
						<input type="hidden" name="{$_name}" value="{$value}">
							<xsl:apply-templates select="@*" mode="attribute"/>
						</input>
						<input type="radio" name="{$_name}_radio" value="2">
							<xsl:choose>
								<xsl:when test="/applyeasy/meta/submitted_params_list">
									<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text() = '2'">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test=" $value != '' and string-length(substring-after($value,'-')) &gt; 0">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
						</input>
						<xsl:variable name="timestamp">
							<xsl:choose>
								<xsl:when test="/applyeasy/meta/submitted_params_list">
									<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text() = '2'">
										<xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $_name]/value/text()"/>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test=" $value != '' and string-length(substring-after($value,'-')) &gt; 0">
										<xsl:value-of select="$value"/>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:call-template name="display_form_input_time">
							<xsl:with-param name="fld_name">
								<xsl:value-of select="$_name"/><xsl:text>_end_datetime</xsl:text>
							</xsl:with-param>
							<xsl:with-param name="frm">document.frmXml</xsl:with-param>
							<xsl:with-param name="show_label">Y</xsl:with-param>
							<xsl:with-param name="def_date">
								<xsl:value-of select="@def_date"/>
							</xsl:with-param>
							<xsl:with-param name="def_time">
								<xsl:value-of select="@def_time"/>
							</xsl:with-param>
							<xsl:with-param name="timestamp">
								<xsl:value-of select="$timestamp"/>
							</xsl:with-param>
							<xsl:with-param name="display_form_input_hhmm">N</xsl:with-param>
							<xsl:with-param name="caching_function">javascript:onBlurEvent(this)</xsl:with-param>
							<xsl:with-param name="focus_rad_btn_name"><xsl:value-of select="$_name"/>_radio[1]</xsl:with-param>
						</xsl:call-template>
						<br/>
						<div style="margin-top:3px"></div>
						<!--  <label for="{$_name}_radio_id3">  -->
							<input type="radio" name="{$_name}_radio" value="3" id="{$_name}_radio_id3">
								<xsl:choose>
									<xsl:when test="/applyeasy/meta/submitted_params_list">
										<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text() = '3'">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<xsl:if test="string-length($value)&gt; 0 and string-length(substring-after($value,'-')) = 0">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>
							</input>
							<input type="text" name="{$_name}_duration" class="wzb-inputText" onfocus="{$_name}_radio[2].checked = 'true'" size="4">
								<xsl:attribute name="onblur">javascript:onBlurEvent(this)</xsl:attribute>
								<!-- Man: attribute "value" should get value from "/applyeasy/meta/submitted_params_list" , if node absent, use "@value" -->
								<xsl:if test="string-length($value)&gt; 0 and string-length(substring-after($value,'-')) = 0">
									<xsl:choose>
										<xsl:when test="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text() = '3'">
											<xsl:attribute name="value"><xsl:choose><xsl:when test="/applyeasy/meta/submitted_params_list"><xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $_name]/value/text()"/></xsl:when><xsl:otherwise><xsl:if test="string-length($value)&gt; 0 and string-length(substring-after($value,'-')) = 0"><xsl:value-of select="$value"/></xsl:if></xsl:otherwise></xsl:choose></xsl:attribute>
										</xsl:when>
										<xsl:when test="content_eff_start_end">
											<xsl:attribute name="value">
												<xsl:value-of select="content_eff_start_end/@value"/>
											</xsl:attribute>
										</xsl:when>
									</xsl:choose>
								</xsl:if>
								<!-- Man: Since attribute "value" handle by special case, we didn't copy value attribute here -->
								<xsl:apply-templates select="@*[name() != 'value']" mode="attribute"/>
							</input>
							<xsl:text>&#160;</xsl:text><xsl:value-of select="$lab_days"/><xsl:text>&#160;</xsl:text> <span class="wzb-ui-module-text"><xsl:value-of select="$lab_duration_suffix"/></span>
							<!--DENNIS-->
				    <!--   </label>  -->
					</span>
				</td>
			</tr>
		</table>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
	</xsl:template>
	<!-- == Positive Int. & Unlimited============-->
	<xsl:template match="*[@type = 'pos_int_unlimited']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="lab_unlimited">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">不限</xsl:when>
				<xsl:when test="$wb_lang='gb'">不限</xsl:when>
				<xsl:otherwise>Unlimited</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_limited_to">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">限制</xsl:when>
				<xsl:when test="$wb_lang='gb'">限制</xsl:when>
				<xsl:otherwise>Limited to</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_waitlist_ind">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">允許等待名單</xsl:when>
				<xsl:when test="$wb_lang='gb'">允许等待名单</xsl:when>
				<xsl:otherwise>Allow waiting list.</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="_fieldname">
			<xsl:value-of select="$_name"/>radio</xsl:variable>
		<span class="{$text_class}">
			<label for="{$_name}radio_id">
				<input type="radio" name="{$_name}radio" value="1" id="{$_name}radio_id" onfocus="waitlst_ind.disabled = true;waitlst_ind.checked = ''">
					<xsl:choose>
						<xsl:when test="/applyeasy/meta/submitted_params_list">
							<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text() = '1'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:if test="not(@value) or @value =''">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<xsl:value-of select="$lab_unlimited"/>
			</label>
			<br/>
			<!--<input type="hidden" name="{$_name}" value="{@value}"><xsl:apply-templates select="@*" mode="attribute"/></input>-->
			<input type="radio" name="{$_name}radio" value="2" onfocus="waitlst_ind.disabled = false">
				<xsl:choose>
					<xsl:when test="/applyeasy/meta/submitted_params_list">
						<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text() = '2'">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="@value != ''">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</input>
			<xsl:value-of select="$lab_limited_to"/>
			<xsl:text>：</xsl:text>
			<input type="text" name="{$_name}" class="wzb-inputText margin-right4" onfocus="{$_name}radio[1].checked = 'true';waitlst_ind.disabled = false">
				<xsl:attribute name="onblur">javascript:onBlurEvent(this)</xsl:attribute>
				<!-- Man: attribute "value" should get value from "/applyeasy/meta/submitted_params_list" , if node absent, use "@value" -->
				<xsl:attribute name="value"><xsl:choose><xsl:when test="/applyeasy/meta/submitted_params_list"><xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $_name]/value/text()"/></xsl:when><xsl:otherwise><xsl:value-of select="@value"/></xsl:otherwise></xsl:choose></xsl:attribute>
				<!-- Man: Since attribute "value" handle by special case, we didn't copy value attribute here -->
				<xsl:apply-templates select="@*[name() != 'value']" mode="attribute"/>
			</input>
			<input type="checkbox" name="waitlst_ind_checkbox" value="" onfocus="{$_name}radio[1].checked = 'true'" id="waitlst_id">
				<xsl:if test="../../../@itm_not_allow_waitlist_ind = 'false' and @value != ''">
					<xsl:attribute name="checked">checked</xsl:attribute>
				</xsl:if>
			</input>
			<label for="waitlst_id">
				<xsl:value-of select="$lab_waitlist_ind"/>
			</label>
			<input type="hidden" name="waitlst_ind"/>
		</span>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
	</xsl:template>
	<!-- ==Pos Ammount & Optional===-->
	<xsl:template match="*[@type = 'pos_amount_optional']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="lab_optional">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">不收費</xsl:when>
				<xsl:when test="$wb_lang='gb'">不收费</xsl:when>
				<xsl:otherwise>Free</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_specify_to">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">指定為</xsl:when>
				<xsl:when test="$wb_lang='gb'">指定为</xsl:when>
				<xsl:otherwise>Specified to</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="_fieldname">
			<xsl:value-of select="$_name"/>radio</xsl:variable>
		<span class="{$text_class}">
			<label for="{$_name}radio_id">
				<input type="radio" name="{$_name}radio" value="1" id="{$_name}radio_id">
					<xsl:choose>
						<xsl:when test="/applyeasy/meta/submitted_params_list">
							<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text() = '1'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:if test="not(@value) or @value =''">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</xsl:otherwise>
					</xsl:choose>
				</input>
				<xsl:value-of select="$lab_optional"/>
			</label>
			<br/>
			<!--<input type="hidden" name="{$_name}" value="{@value}"><xsl:apply-templates select="@*" mode="attribute"/></input>-->
			<input type="radio" name="{$_name}radio" value="2">
				<xsl:choose>
					<xsl:when test="/applyeasy/meta/submitted_params_list">
						<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text() = '2'">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="@value != ''">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</input>
			<xsl:value-of select="$lab_specify_to"/>
			<xsl:text>：</xsl:text>
			<input type="text" name="{$_name}" class="wzb-inputText" onfocus="{$_name}radio[1].checked = 'true'">
				<xsl:attribute name="onblur">javascript:onBlurEvent(this)</xsl:attribute>
				<!-- Man: attribute "value" should get value from "/applyeasy/meta/submitted_params_list" , if node absent, use "@value" -->
				<xsl:attribute name="value"><xsl:choose><xsl:when test="/applyeasy/meta/submitted_params_list"><xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $_name]/value/text()"/></xsl:when><xsl:otherwise><xsl:value-of select="@value"/></xsl:otherwise></xsl:choose></xsl:attribute>
				<!-- Man: Since attribute "value" handle by special case, we didn't copy value attribute here -->
				<xsl:apply-templates select="@*[name() != 'value']" mode="attribute"/>
			</input>
		</span>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
		<!--
		<input type="text" name="{$_name}" class="wzb-inputText">
			<xsl:attribute name="onblur">javascript:onBlurEvent(this)</xsl:attribute>
		</input>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
		-->
	</xsl:template>
	<!-- ================= Constant ==================================-->
	<xsl:template match="*[@type = 'constant']" mode="field_type">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
			<xsl:choose>
				<xsl:when test="@ext_valuelabel">
					<xsl:value-of select="@ext_valuelabel"/>
				</xsl:when>
				<xsl:when test="@external_field  ='yes'">
					<xsl:choose>
						<xsl:when test="@format">
							 <xsl:value-of select="format-number( *[name() != 'title']/@value, @format)"/>
						</xsl:when>
						<xsl:otherwise >
							<xsl:choose >
								<xsl:when test="*[name() != 'title']/@value!=''">
									<xsl:value-of select="*[name() != 'title']/@value"/>
								</xsl:when>
								<xsl:otherwise >--</xsl:otherwise>
							</xsl:choose>
						<!--	<xsl:value-of select="*[name() != 'title']/@value"/>-->
						</xsl:otherwise>
					</xsl:choose>
 				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="@value"/>
				</xsl:otherwise>
			</xsl:choose>
			<input type="hidden" name="{$_name}" value="{@value}">
				<xsl:apply-templates select="@*" mode="attribute"/>
				<xsl:if test="@external_field='yes'">
					<xsl:attribute name="value"><xsl:value-of select="format-number(*[name() != 'title']/@value, @format)"/></xsl:attribute>
				</xsl:if>
			</input>
			<xsl:if test="@paramname">
				<input type="hidden" name="{@paramname}" value="{@value}">
					<xsl:if test="@external_field='yes'">
						<xsl:attribute name="value"><xsl:value-of select="format-number(*[name() != 'title']/@value, @format)"/></xsl:attribute>
					</xsl:if>
				</input>
			</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- ==Constant DateTime==========-->
	<xsl:template match="*[@type = 'constant_datetime']" mode="field_type">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<span class="{$text_class}">
			<xsl:choose>
				<xsl:when test="@ext_valuelabel">
					<xsl:value-of select="@ext_valuelabel"/>
				</xsl:when>
				<xsl:when test="@external_field  ='yes'">
					<xsl:call-template name="display_time">
						<xsl:with-param name="wb_lang">
							<xsl:value-of select="$wb_lang"/>
						</xsl:with-param>
						<xsl:with-param name="dis_time">T</xsl:with-param>
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="*[name() != 'title']/@value"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="display_time">
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="@value"/>
						</xsl:with-param>
						<xsl:with-param name="wb_lang">
							<xsl:value-of select="$wb_lang"/>
						</xsl:with-param>
						<xsl:with-param name="dis_time">T</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			<input type="hidden" name="{$_name}" value="{@value}">
				<xsl:apply-templates select="@*" mode="attribute"/>
			</input>
			<xsl:if test="@paramname">
				<input type="hidden" name="{@paramname}" value="{@value}"/>
			</xsl:if>
		</span>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- ==IAC Constant ==========-->
	<xsl:template match="*[@type = 'iac_constant']" mode="field_type">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<span class="{$text_class}">
			<xsl:for-each select="assigned_role_list/role/entity">
				<xsl:value-of select="@display_bil"/>
			</xsl:for-each>
			<xsl:variable name="_val">
				<xsl:for-each select="assigned_role_list/role/entity">
					<xsl:value-of select="@id"/>
					<xsl:if test="position() != last()">~</xsl:if>
				</xsl:for-each>
			</xsl:variable>
			<input type="hidden" name="{$_name}" value="{$_val}">
				<xsl:apply-templates select="@*" mode="attribute"/>
			</input>
			<xsl:if test="@paramname">
				<input type="hidden" name="{@paramname}" value="{assigned_role_list/role/@id}~{$_val}"/>
			</xsl:if>
		</span>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- ==Single Checkbox=====-->
	<xsl:template match="*[@type = 'single_checkbox']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc"/>
		</xsl:variable>
		<xsl:variable name="my_id">
			<xsl:value-of select="@id"/>
		</xsl:variable>
		<label for="{concat($_name,'id')}" class="{$text_class}">
			<input type="checkbox" id="{concat($_name,'id')}" name="{$_name}">
				<xsl:apply-templates select="@*" mode="attribute"/>
				<xsl:if test="../../mote_level_list/mote_level[@id = $my_id]/@selected = 'true'">
					<xsl:attribute name="checked">checked</xsl:attribute>
				</xsl:if>
				<xsl:value-of select="$desc_name"/>
			</input>
		</label>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
		&#160;
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- ==Resource===========-->
	<xsl:template match="*[@type = 'resource']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc"/>
		</xsl:variable>
		<xsl:variable name="dummy_name">
			<xsl:value-of select="concat($_name,'_box')"/>
		</xsl:variable>
		<input type="hidden" name="{$_name}"/>
		<xsl:call-template name="wb_goldenman">
			<xsl:with-param name="frm">document.frmXml</xsl:with-param>
			<xsl:with-param name="max_size">
				<xsl:value-of select="@size"/>
			</xsl:with-param>
			<xsl:with-param name="field_name">
				<xsl:choose>
					<xsl:when test="@arrayparam">
						<xsl:value-of select="concat($dummy_name,@arrayparam)"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$dummy_name"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="tree_type">knowledge_object</xsl:with-param>
			<xsl:with-param name="select_type">4</xsl:with-param>
			<xsl:with-param name="option_list">
				<xsl:for-each select="resource_list/resource">
					<option value="{@id}">
						<xsl:value-of select="."/>
					</option>
				</xsl:for-each>
			</xsl:with-param>
			<xsl:with-param name="single_option_text">
				<xsl:value-of select="resource_list/resource[1]"/>
			</xsl:with-param>
			<xsl:with-param name="single_option_value">
				<xsl:value-of select="resource_list/resource[1]/@id"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- ==Resource===========-->
	<xsl:template match="*[@type = 'resource']" mode="ext_field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc"/>
		</xsl:variable>
		<xsl:variable name="dummy_name">
			<xsl:value-of select="concat($_name,'_box')"/>
		</xsl:variable>
		<input type="hidden" name="{$_name}"/>
		<xsl:call-template name="wb_goldenman">
			<xsl:with-param name="frm">document.frmXml</xsl:with-param>
			<xsl:with-param name="max_size">
				<xsl:value-of select="@size"/>
			</xsl:with-param>
			<xsl:with-param name="field_name">
				<xsl:choose>
					<xsl:when test="@arrayparam">
						<xsl:value-of select="concat($dummy_name,@arrayparam)"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$dummy_name"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="tree_type">knowledge_object</xsl:with-param>
			<xsl:with-param name="select_type">4</xsl:with-param>
			<xsl:with-param name="option_list">
				<xsl:for-each select="resource_list/resource">
					<option value="{@id}">
						<xsl:value-of select="."/>
					</option>
				</xsl:for-each>
			</xsl:with-param>
			<xsl:with-param name="single_option_text">
				<xsl:value-of select="resource_list/resource[1]"/>
			</xsl:with-param>
			<xsl:with-param name="single_option_value">
				<xsl:value-of select="resource_list/resource[1]/@id"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- ==targeted_lrn_pickup ===========-->
	<!--<xsl:template match="*[@type = 'targeted_lrn_pickup']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="lab_Unassigned">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">未指定</xsl:when>
				<xsl:when test="$wb_lang='gb'">未指定</xsl:when>
				<xsl:otherwise>Unassigned</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_all_learners">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">所有學員</xsl:when>
				<xsl:when test="$wb_lang='gb'">所有学员</xsl:when>
				<xsl:otherwise>All learners</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc"/>
		</xsl:variable>
		<xsl:variable name="dummy_name">
			<xsl:value-of select="concat($_name,'_box')"/>
		</xsl:variable>
		<input type="hidden" name="{$_name}"/>
		<input type="hidden" name="{$_name}arg">
			<xsl:variable name="_fieldname">
				<xsl:value-of select="$_name"/>arg</xsl:variable>
			<xsl:attribute name="value"><xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text()"/></xsl:attribute>
		</input>
		<script LANGUAGE="JavaScript" TYPE="text/javascript">
			function targeted_lrn_radio_change(val){
				if( val == 1 ) {
					document.frmXml.<xsl:value-of select="$dummy_name"/>.disabled = true;
					document.frmXml.genaddtargeted_lrn.disabled = true;
					document.frmXml.genremovetargeted_lrn.disabled = true;
					document.frmXml.<xsl:value-of select="$_name"/>arg.value = '';
				} else {
					document.frmXml.<xsl:value-of select="$dummy_name"/>.disabled = false;
					document.frmXml.genaddtargeted_lrn.disabled = false;
					document.frmXml.genremovetargeted_lrn.disabled = false;
				}
				return;
			}
		</script>
		<table border="0" cellpadding="0" cellspacing="0">
			<xsl:variable name="_fieldname">
				<xsl:value-of select="$_name"/>radio</xsl:variable>
			<tr>
				<td valign="top">
					<label for="{$_name}radio_id">
						<input type="radio" name="{$_name}radio" value="1" id="{$_name}radio_id" onclick="targeted_lrn_radio_change(1)">
							<xsl:choose>
								<xsl:when test="/applyeasy/meta/submitted_params_list">
									<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text() = '1'">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test="not(target_list/target)">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
						</input>
					</label>
				</td>
				<td>
					<label for="{$_name}radio_id">
						<xsl:choose>
							<xsl:when test="@label = 'lab_all_learners'">
								<xsl:value-of select="$lab_all_learners"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_Unassigned"/>
							</xsl:otherwise>
						</xsl:choose>
					</label>
				</td>
			</tr>
			<tr>
				<td valign="top">
					<input type="radio" name="{$_name}radio" value="2" onclick="targeted_lrn_radio_change(2)">
						<xsl:choose>
							<xsl:when test="/applyeasy/meta/submitted_params_list">
								<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text() = '2'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test="target_list/target">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
							</xsl:otherwise>
						</xsl:choose>
					</input>
				</td>
				<td>
					<span class="Text">
						<xsl:value-of select="$lab_const_assigned_to"/>
					</span>
				</td>
			</tr>
			<tr>
				<td valign="top">
					<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
				</td>
				<td>
					<xsl:call-template name="wb_goldenman">
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						<xsl:with-param name="name">targeted_lrn</xsl:with-param>
						<xsl:with-param name="width">560</xsl:with-param>
						<xsl:with-param name="max_size">
							<xsl:value-of select="@size"/>
						</xsl:with-param>
						<xsl:with-param name="field_name">
							<xsl:choose>
								<xsl:when test="@arrayparam">
									<xsl:value-of select="concat($dummy_name,@arrayparam)"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$dummy_name"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:with-param>
						<xsl:with-param name="tree_type">targeted_learner</xsl:with-param>
						<xsl:with-param name="select_type">
							<xsl:choose>
								<xsl:when test="@select_mode = 'single'">2</xsl:when>
								<xsl:when test="@select_mode = 'multiple'">1</xsl:when>
								<xsl:otherwise>2</xsl:otherwise>
							</xsl:choose>
						</xsl:with-param>
						<xsl:with-param name="option_list">
							<xsl:if test="not(/applyeasy/meta/submitted_params_list)">
								<xsl:for-each select="target_list/target">
									<option>
										<xsl:attribute name="value"><xsl:for-each select="entity"><xsl:value-of select="@id"/><xsl:if test="position() != last()">~</xsl:if></xsl:for-each></xsl:attribute>
										<xsl:value-of select="entity[@type = 'USG']/@display_bil"/>
										<xsl:if test="../../@type = 'targeted_lrn_pickup'">
											<xsl:if test="entity[@type = 'UGR']">,&#160;<xsl:value-of select="entity[@type = 'UGR']/@display_bil"/>
											</xsl:if>
											<xsl:if test="entity[@type = 'IDC']">,&#160;<xsl:value-of select="entity[@type = 'IDC']/@display_bil"/>
											</xsl:if>
										</xsl:if>
									</option>
								</xsl:for-each>
							</xsl:if>
						</xsl:with-param>
						<xsl:with-param name="single_option_text">
							<xsl:for-each select="target_list/target[1]/entity">
								<xsl:value-of select="@display_bil"/>
								<xsl:if test="position() != last()">,&#160;</xsl:if>
							</xsl:for-each>
						</xsl:with-param>
						<xsl:with-param name="single_option_value">
							<xsl:for-each select="target_list/target[1]/entity">
								<xsl:value-of select="@id"/>
								<xsl:if test="position() != last()">~</xsl:if>
							</xsl:for-each>
						</xsl:with-param>
						<xsl:with-param name="close_option">0</xsl:with-param>
						<xsl:with-param name="filter_user_group">0</xsl:with-param>
					</xsl:call-template>
					<xsl:if test="@paramname">
						<input type="hidden" name="{@paramname}"/>
					</xsl:if>
					<xsl:if test="link_list">
						<xsl:apply-templates select="link_list" mode="field_type">
							<xsl:with-param name="text_class">
								<xsl:value-of select="$text_class"/>
							</xsl:with-param>
						</xsl:apply-templates>
					</xsl:if>
				</td>
			</tr>
		</table>
		<xsl:choose>
			<xsl:when test="/applyeasy/meta/submitted_params_list">
				<xsl:variable name="_fieldname">
					<xsl:value-of select="$_name"/>radio</xsl:variable>
				<script LANGUAGE="JavaScript" TYPE="text/javascript">
					row_set = new Array()
					row_set = document.frmXml.<xsl:value-of select="$_name"/>arg.value.split('[~!~!~]')
					for(i=0; i&lt;row_set.length; i++) {
						tl_set = new Array()
						tl_set = row_set[i].split("[~!~]")
						if( tl_set.length == 1 ) {
							<xsl:value-of select="$dummy_name"/>(tl_set[0])
						} else if ( tl_set.length == 2 ) {
							<xsl:value-of select="$dummy_name"/>(tl_set[0], tl_set[1])
						} else if( tl_set.length == 3 ) {
							<xsl:value-of select="$dummy_name"/>(tl_set[0], tl_set[1], tl_set[2])
						}
					}
					targeted_lrn_radio_change(<xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text()"/>)
				</script>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="target_list/target">
						<script LANGUAGE="JavaScript" TYPE="text/javascript">targeted_lrn_radio_change(2)</script>
					</xsl:when>
					<xsl:otherwise>
						<script LANGUAGE="JavaScript" TYPE="text/javascript">targeted_lrn_radio_change(1)</script>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>-->
	<!-- == user_group_pickup == -->
	<xsl:template match="*[@type = 'user_group_pickup']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc"/>
		</xsl:variable>
		<xsl:variable name="dummy_name">
			<xsl:value-of select="concat($_name,'_box')"/>
		</xsl:variable>
		<input type="hidden" name="{$_name}"/>
		<xsl:call-template name="wb_goldenman">
			<xsl:with-param name="frm">document.frmXml</xsl:with-param>
			<xsl:with-param name="max_size">
				<xsl:value-of select="@size"/>
			</xsl:with-param>
			<xsl:with-param name="field_name">
				<xsl:choose>
					<xsl:when test="@arrayparam">
						<xsl:value-of select="concat($dummy_name,@arrayparam)"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$dummy_name"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="tree_type">user_group</xsl:with-param>
			<xsl:with-param name="select_type">
				<xsl:choose>
					<xsl:when test="@select_mode = 'single'">2</xsl:when>
					<xsl:when test="@select_mode = 'multiple'">1</xsl:when>
					<xsl:otherwise>
						<!-- the hardcode in this otherwise is deprecated (2002.09.02 kawai) -->1</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="option_list">
				<xsl:for-each select="target_list/target">
					<xsl:apply-templates select="entity[@type='USG']" mode="usr_pickup_lst_template"/>
				</xsl:for-each>
			</xsl:with-param>
			<xsl:with-param name="single_option_text">
				<xsl:for-each select="target_list/target[1]/entity">
					<xsl:value-of select="@display_bil"/>
					<xsl:if test="position() != last()">,</xsl:if>
				</xsl:for-each>
			</xsl:with-param>
			<xsl:with-param name="single_option_value">
				<xsl:for-each select="target_list/target[1]/entity">
					<xsl:value-of select="@id"/>
					<xsl:if test="position() != last()">~</xsl:if>
				</xsl:for-each>
			</xsl:with-param>
			<xsl:with-param name="close_option">1</xsl:with-param>
		</xsl:call-template>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- == notify_email_limited == -->
	<xsl:template match="*[@type='notify_email_limited']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="nty_days" select="/applyeasy/item/@notify_days"/>
		<xsl:variable name="lab_optional">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">否</xsl:when>
				<xsl:when test="$wb_lang='gb'">否</xsl:when>
				<xsl:otherwise>No</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_specify_to">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">是，在課程結束前    </xsl:when>
				<xsl:when test="$wb_lang='gb'">是，在课程结束前   </xsl:when>
				<xsl:otherwise>Yes,</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_send_by">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">天</xsl:when>
				<xsl:when test="$wb_lang='gb'">天</xsl:when>
				<xsl:otherwise>days before course end date.</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="_fieldname">
			<xsl:value-of select="$_name"/>_radio
		</xsl:variable>
		<table border="0" cellpadding="0" cellspacing="0">
			<div>
				<input type="radio" name="{$_name}radio" value="1" id="{$_name}radio_id">
					<xsl:if test="not($nty_days) or $nty_days = '' or $nty_days = -1">
						<xsl:attribute name="checked">checked</xsl:attribute>
					</xsl:if>
				</input>
				<label for="{$_name}radio_id">
					<xsl:value-of select="$lab_optional"/>
				</label>
				<br/>
				<input type="radio" name="{$_name}radio" value="1" id="{$_name}radio_id">
					<xsl:if test="$nty_days >= 0">
						<xsl:attribute name="checked">checked</xsl:attribute>
					</xsl:if>
				</input>
				
				<label>
					<xsl:value-of select="$lab_specify_to"/>
				</label>
				<input style="margin: 0 5px;" type="text" name="{$_name}" class="wzb-inputText" onfocus="{$_name}radio[1].checked = 'true'" size="5" maxlength="5">
					<xsl:attribute name="onblur">javascript:onBlurEvent(this)</xsl:attribute>
					<xsl:attribute name="value"><xsl:if test="$nty_days != '' and $nty_days >= 0"><xsl:value-of select="$nty_days"/></xsl:if></xsl:attribute>
				</input>
				<label>
					<xsl:value-of select="$lab_send_by"/>
				</label>
			</div>
		</table>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
	</xsl:template>
	<xsl:template match="*[@type='notify_support_email']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="nty_email" select="/applyeasy/item/@notify_email"/>		
		<xsl:variable name="sys_email" select="./sys_email/text()"/>
		<xsl:variable name="sys_email_1" select="/applyeasy/item/sys_email/text()"/><!-- for add an item -->
		<xsl:variable name="lab_system">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">系統</xsl:when>
				<xsl:when test="$wb_lang='gb'">系统</xsl:when>
				<xsl:otherwise>System</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_others">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">其他</xsl:when>
				<xsl:when test="$wb_lang='gb'">其他</xsl:when>
				<xsl:otherwise>Others</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_desc">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">這個郵件會被用來作為提醒郵件的發件人和回覆地址</xsl:when>
				<xsl:when test="$wb_lang='gb'">这个邮件会被用来作为提醒邮件的发件人和回复地址</xsl:when>
				<xsl:otherwise>It will be used as the “From address” and “Reply-to address” for notification</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="_fieldname">
			<xsl:value-of select="$_name"/>_radio
		</xsl:variable>
		<table border="0" cellpadding="0" cellspacing="0">
			<span class="{$text_class}">
				<input type="radio" name="{$_name}radio" value="1" id="{$_name}radio_id">
					<xsl:if test="not($nty_email) or $nty_email = ''">
						<xsl:attribute name="checked">checked</xsl:attribute>
					</xsl:if>
				</input>
				<label>
					<xsl:value-of select="$lab_system"/> (<xsl:value-of select="$sys_email"/><xsl:value-of select="$sys_email_1"/>)
				</label>
				<br/>
				<input type="radio" name="{$_name}radio" value="1" id="{$_name}radio_id">
					<xsl:if test="$nty_email != ''">
						<xsl:attribute name="checked">checked</xsl:attribute>
					</xsl:if>
				</input>
				<xsl:value-of select="$lab_others"/>
				<xsl:text>：</xsl:text>
				<input type="text" name="itm_support_email" class="wzb-inputText" onfocus="{$_name}radio[1].checked = 'true'" size="50" maxlength="255">
					<xsl:attribute name="onblur">javascript:onBlurEvent(this)</xsl:attribute>
					<xsl:attribute name="value"><xsl:if test="$nty_email != ''"><xsl:value-of select="$nty_email"/></xsl:if></xsl:attribute>
				</input>
				<br />
				<label class="wzb-ui-module-text">
					(<xsl:value-of select="$lab_desc"/>）
				</label>
			</span>
		</table>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
	</xsl:template>
	<!-- usr_pickup_lst_template -->
	<xsl:template match="entity" mode="usr_pickup_lst_template">
		<xsl:variable name="usg_id" select="@id"/>
		<xsl:if test="count(../preceding-sibling::*/entity[$usg_id=@id])=0">
			<option>
				<xsl:attribute name="value"><xsl:value-of select="@id"/></xsl:attribute>
				<xsl:value-of select="../entity[@type = 'USG']/@display_bil"/>
				<xsl:if test="../../../@type = 'targeted_lrn_pickup'">
					,<xsl:value-of select="../entity[@type = 'UGR']/@display_bil"/>,<xsl:value-of select="../entity[@type = 'IDC']/@display_bil"/>
				</xsl:if>
			</option>
		</xsl:if>
	</xsl:template>
	<!-- ==competency_pickup ===========-->
	<xsl:template match="*[@type = 'competency_pickup']" mode="ext_field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc"/>
		</xsl:variable>
		<xsl:variable name="dummy_name">
			<xsl:value-of select="concat($_name,'_box')"/>
		</xsl:variable>
		<input type="hidden" name="{$_name}"/>
		<xsl:call-template name="wb_goldenman">
			<xsl:with-param name="frm">document.frmXml</xsl:with-param>
			<xsl:with-param name="max_size">
				<xsl:value-of select="@size"/>
			</xsl:with-param>
			<xsl:with-param name="field_name">
				<xsl:choose>
					<xsl:when test="@arrayparam">
						<xsl:value-of select="concat($dummy_name,@arrayparam)"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$dummy_name"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="tree_type">competence</xsl:with-param>
			<xsl:with-param name="select_type">4</xsl:with-param>
			<xsl:with-param name="option_list">
				<xsl:for-each select="skill_list/skill">
					<option>
						<xsl:attribute name="value"><xsl:value-of select="@id"/></xsl:attribute>
						<xsl:value-of select="@title"/>
					</option>
				</xsl:for-each>
			</xsl:with-param>
			<xsl:with-param name="single_option_text">
				<xsl:value-of select="skill_list/skill[1]/@title"/>
			</xsl:with-param>
			<xsl:with-param name="single_option_value">
				<xsl:value-of select="skill_list/skill[1]/@id"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- ==item access pickup ===========-->
	<xsl:template match="*[@type = 'item_access_pickup']" mode="ext_field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc"/>
		</xsl:variable>
		<xsl:variable name="dummy_name">
			<xsl:value-of select="concat($_name,'_box')"/>
		</xsl:variable>
		<xsl:variable name="field_name">
			<xsl:choose>
				<xsl:when test="@arrayparam">
					<xsl:value-of select="concat($dummy_name,@arrayparam)"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$dummy_name"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- for training center dependant -->
		<xsl:variable name="dependant">
			<xsl:choose>
				<xsl:when test="@dependant">
					<xsl:value-of select="@dependant"/>
				</xsl:when>
				<xsl:when test="@dependent">
					<xsl:value-of select="@dependent"/>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="_dependant_name">
			<xsl:choose>
				<xsl:when test="name() = 'subfield'">
					<xsl:value-of select="concat(name(../..), '_', @dependant)"/>
				</xsl:when>
				<xsl:when test="../../section/*[name() = $dependant]">
					<xsl:apply-templates select="../../section/*[name() = $dependant]" mode="get_dependant_name"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="../*[name() = $dependant]" mode="get_dependant_name"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="_dependant_desc_name">
			<xsl:choose>
				<xsl:when test="../../section/*[name() = $dependant]">
					<xsl:apply-templates select="../../section/*[name() = $dependant]" mode="get_dependant_desc_name"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="../*[name() = $dependant]" mode="get_dependant_desc_name"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="_dummy_dependant_name">
			<xsl:choose>
				<xsl:when test="@id = 'INSTR'">
					<xsl:value-of select="concat($dependant,'_')"/>
				</xsl:when>
				<xsl:otherwise><xsl:value-of select="concat($_dependant_name,'_box')"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:variable name="temp_root_ent_id"><xsl:value-of select="/applyeasy/meta/cur_usr/@root_ent_id"/></xsl:variable>
		<xsl:variable name="temp_role_id"><xsl:value-of select="concat(@id,'_',$temp_root_ent_id)"/></xsl:variable>
		
		<input type="hidden" name="{$_name}"/>
		<input type="hidden" name="{$_name}arg">
			<xsl:if test="/applyeasy/meta/submitted_params_list">
				<xsl:variable name="_fieldname">
					<xsl:value-of select="$_name"/>arg</xsl:variable>
				<xsl:attribute name="value"><xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text()"/></xsl:attribute>
			</xsl:if>
		</input>
		<xsl:if test="@id='TADM' and $training_plan ='true'">
			<xsl:if test="/applyeasy/plan/responser !=''">
				<span class="Desc">
					<xsl:text>【</xsl:text>
					<xsl:value-of select="$lab_expect"/>
					<xsl:value-of select="$lab_responser"/>
					<xsl:text>：</xsl:text>
					<xsl:value-of select="/applyeasy/plan/responser"/>
					<xsl:text>】</xsl:text>
				</span>
			</xsl:if>
		</xsl:if>
		
		<xsl:call-template name="wb_goldenman">
			<xsl:with-param name="frm">document.frmXml</xsl:with-param>
			<xsl:with-param name="max_size">
				<xsl:value-of select="@size"/>
			</xsl:with-param>
			<xsl:with-param name="field_name">
				<xsl:choose>
					<xsl:when test="@arrayparam">
						<xsl:value-of select="concat($dummy_name,@arrayparam)"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$dummy_name"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="tree_type">user_group_and_user</xsl:with-param>
			<xsl:with-param name="select_type">4</xsl:with-param>
			<xsl:with-param name="option_list">
				<xsl:if test="not(/applyeasy/meta/submitted_params_list)">
					<xsl:for-each select="assigned_role_list/role/entity">
						<option value="{@id}">
							<xsl:value-of select="@display_bil"/>
						</option>
					</xsl:for-each>
				</xsl:if>
			</xsl:with-param>
			<xsl:with-param name="search">
				<xsl:choose>
					<xsl:when test="@id = 'INSTR'">false</xsl:when>
					<xsl:otherwise>true</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="search_function">
				<xsl:choose>
					<xsl:when test="(@dependant or @dependent) and $tcEnabled='true' ">goldenman.openitemaccsearchwin(<xsl:value-of select="$temp_root_ent_id"/>,'<xsl:value-of select="$temp_role_id"/>','<xsl:value-of select="$field_name"/>','<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="@size"/>',document.frmXml.<xsl:value-of select="$_dummy_dependant_name"/>.options[0].value,'<xsl:value-of select="$_dependant_desc_name"/>')</xsl:when>
					<xsl:otherwise>goldenman.openitemaccsearchwin(<xsl:value-of select="$temp_root_ent_id"/>,'<xsl:value-of select="$temp_role_id"/>','<xsl:value-of select="$field_name"/>','<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="@size"/>')</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="add_function">
				<xsl:choose>
					<xsl:when test="(@dependant or @dependent) and @id = 'INSTR' ">goldenman.openitemaccwininst(<xsl:value-of select="$temp_root_ent_id"/>,'<xsl:value-of select="$temp_role_id"/>','<xsl:value-of select="$field_name"/>','<xsl:value-of select="@size"/>','<xsl:value-of select="$wb_lang"/>',document.frmXml.<xsl:value-of select="$_dummy_dependant_name"/>,'<xsl:value-of select="$_dependant_desc_name"/>')</xsl:when>
					<xsl:when test="(@dependant or @dependent) and $tcEnabled='true' ">goldenman.openitemaccwin(<xsl:value-of select="$temp_root_ent_id"/>,'<xsl:value-of select="$temp_role_id"/>','<xsl:value-of select="$field_name"/>','<xsl:value-of select="@size"/>','<xsl:value-of select="$wb_lang"/>',document.frmXml.<xsl:value-of select="$_dummy_dependant_name"/>.options[0].value,'<xsl:value-of select="$_dependant_desc_name"/>')</xsl:when>
					<xsl:otherwise>goldenman.openitemaccwin(<xsl:value-of select="$temp_root_ent_id"/>,'<xsl:value-of select="$temp_role_id"/>','<xsl:value-of select="$field_name"/>','<xsl:value-of select="@size"/>')</xsl:otherwise>			
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="single_option_text">
				<xsl:value-of select="assigned_role_list/role/entity[1]/@display_bil"/>
			</xsl:with-param>
			<xsl:with-param name="single_option_value">
				<xsl:value-of select="assigned_role_list/role/entity[1]/@id"/>
			</xsl:with-param>
			<xsl:with-param name="box_size">
				<xsl:value-of select="@size"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:if test="/applyeasy/meta/submitted_params_list">
			<script LANGUAGE="JavaScript" TYPE="text/javascript">
				<xsl:value-of select="$field_name"/><![CDATA[(document.frmXml.]]><xsl:value-of select="$_name"/><![CDATA[arg.value);]]>
			</script>
		</xsl:if>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}"/>
		</xsl:if>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- ==Catalog Attachment===-->
	<xsl:template match="*[@type = 'catalog_attachment']" mode="ext_field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="lab_Unassigned">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">未指定</xsl:when>
				<xsl:when test="$wb_lang='gb'">未指定</xsl:when>
				<xsl:otherwise>Unassigned</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc"/>
		</xsl:variable>
		<xsl:variable name="dummy_name">
			<xsl:value-of select="concat($_name,'_box')"/>
		</xsl:variable>
		<!-- for training center dependant -->
		<xsl:variable name="dependant">
			<xsl:choose>
				<xsl:when test="@dependant">
					<xsl:value-of select="@dependant"/>
				</xsl:when>
				<xsl:when test="@dependent">
					<xsl:value-of select="@dependent"/>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="_dependant_name">
			<xsl:choose>
				<xsl:when test="name() = 'subfield'">
					<xsl:value-of select="concat(name(../..), '_', @dependant)"/>
				</xsl:when>
				<xsl:when test="../../section/*[name() = $dependant]">
					<xsl:apply-templates select="../../section/*[name() = $dependant]" mode="get_dependant_name"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="../*[name() = $dependant]" mode="get_dependant_name"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="_dependant_desc_name">
			<xsl:choose>
				<xsl:when test="../../section/*[name() = $dependant]">
					<xsl:apply-templates select="../../section/*[name() = $dependant]" mode="get_dependant_desc_name"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="../*[name() = $dependant]" mode="get_dependant_desc_name"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="_dummy_dependant_name"><xsl:value-of select="concat($_dependant_name,'_box')"/></xsl:variable>
		<xsl:variable name="tcr_id">
			<xsl:choose>
				<xsl:when test="$tcEnabled = 'true'">
					<![CDATA[document.frmXml.]]><xsl:value-of select="$_dummy_dependant_name"/><![CDATA[.options[0].value]]>
				</xsl:when>
				<xsl:otherwise><xsl:text>''</xsl:text></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<script LANGUAGE="JavaScript" TYPE="text/javascript">
			function catalog_radio_change(val){
			/*
				if( val == 1 ) {
					document.frmXml.<xsl:value-of select="$dummy_name"/>.disabled = true;
					document.frmXml.genaddcatalog.disabled = true;
					document.frmXml.genremovecatalog.disabled = true;
					document.frmXml.<xsl:value-of select="$_name"/>arg.value = ''
				} else {
					document.frmXml.<xsl:value-of select="$dummy_name"/>.disabled = false;
					document.frmXml.genaddcatalog.disabled = false;
					document.frmXml.genremovecatalog.disabled = false;
				}
				*/
				return;
			}
			
			function get_add_function(){
				<xsl:if test="$tcEnabled = 'true'">
					if(document.frmXml.<xsl:value-of select="$_dummy_dependant_name"/>.options[0].value == '') {
						alert(eval('wb_msg_'+ '<xsl:value-of select="$wb_lang"/>' +'_sel') + ' ' + '<xsl:value-of select="$_dependant_desc_name"/>')
						return false;
					}
				</xsl:if>
				goldenman.opentree('catalog',1,'<xsl:value-of select="$dummy_name"/>','<xsl:value-of select="$itm_type_lst"/>','','','','','','','','','0', '', '', '', <xsl:value-of select="$tcr_id"/>);
			}
			
		</script>
		<table border="0" cellpadding="0" cellspacing="0">
			<xsl:if test="$training_plan = 'true' and /applyeasy/plan/tnd_title !=''">		
				<tr>
					<td colspan="2">
						<span class="Desc">
							<xsl:text>【</xsl:text>
							<xsl:value-of select="$lab_expect"/>
							<xsl:value-of select="$desc_name"/>
							<xsl:text>：</xsl:text>
							<xsl:value-of select="/applyeasy/plan/tnd_title"/>
							<xsl:text>】</xsl:text>
						</span>		
					</td>
				</tr>
			</xsl:if>
			<!-- 把课程目录改为必填项
			<tr>
				<td valign="top" width="2%">
					<label for="{$_name}radio_id">
						<input type="radio" name="{$_name}radio" value="1" id="{$_name}radio_id" onclick="javascript:catalog_radio_change(1)">
							<xsl:choose>
								<xsl:when test="/applyeasy/meta/submitted_params_list">
									<xsl:variable name="_fieldname">
										<xsl:value-of select="$_name"/>radio</xsl:variable>
									<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text() = '1'">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test="not(node_list/node/nav)">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
						</input>
					</label>
				</td>
				<td width="98%">
					<label for="{$_name}radio_id">
						<xsl:value-of select="$lab_Unassigned"/>
					</label>
				</td>
			</tr>
			<tr>
				<td valign="top">
					<input type="radio" name="{$_name}radio" value="2" onclick="javascript:catalog_radio_change(2)">
						<xsl:choose>
							<xsl:when test="/applyeasy/meta/submitted_params_list">
								<xsl:variable name="_fieldname">
									<xsl:value-of select="$_name"/>radio</xsl:variable>
								<xsl:if test="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text() = '2'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test="node_list/node/nav">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
							</xsl:otherwise>
						</xsl:choose>
					</input>
				</td>
				<td>
					<span class="Text">
						<xsl:value-of select="$lab_const_assigned_to"/>
					</span>
				</td>
			</tr>
			 -->
			<tr><!--
				<td valign="top">
					<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
				</td>
				-->
				<td>
					<input type="hidden" name="{$_name}"/>
					<input type="hidden" name="{$_name}arg">
						<xsl:if test="/applyeasy/meta/submitted_params_list">
							<xsl:variable name="_fieldname">
								<xsl:value-of select="$_name"/>arg</xsl:variable>
							<xsl:attribute name="value"><xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text()"/></xsl:attribute>
						</xsl:if>
					</input>
					<xsl:call-template name="wb_goldenman">
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						<xsl:with-param name="name">catalog</xsl:with-param>
						<xsl:with-param name="max_size">
							<xsl:value-of select="@size"/>
						</xsl:with-param>
						<xsl:with-param name="field_name">
							<xsl:value-of select="$dummy_name"/>
						</xsl:with-param>
						<xsl:with-param name="tree_type">catalog</xsl:with-param>
						<xsl:with-param name="select_type">1</xsl:with-param>
						<xsl:with-param name="args_type">row</xsl:with-param>
						<xsl:with-param name="complusory_tree">0</xsl:with-param>
						<xsl:with-param name="custom_js_code_extra">args = goldenman.set_global_catalog_label(args,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						<xsl:with-param name="custom_js_code">catalog_radio_change(2);</xsl:with-param>
						<xsl:with-param name="add_function">get_add_function();</xsl:with-param>
						<xsl:with-param name="option_list">
							<xsl:if test="not(/applyeasy/meta/submitted_params_list)">
								<xsl:for-each select="node_list/node/nav">
									<xsl:for-each select="node">
										<xsl:if test="position() = (last() -1)">
											<option>
												<xsl:attribute name="value"><xsl:value-of select="@node_id"/></xsl:attribute>
												<xsl:value-of select="title"/>
											</option>
										</xsl:if>
									</xsl:for-each>
								</xsl:for-each>
							</xsl:if>
						</xsl:with-param>
						<xsl:with-param name="single_option_text">
							<xsl:value-of select="assigned_role_list/role/entity[1]/title"/>
						</xsl:with-param>
						<xsl:with-param name="single_option_value">
							<xsl:value-of select="assigned_role_list/role/entity[1]/@node_id"/>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:if test="/applyeasy/meta/submitted_params_list">
						<script LANGUAGE="JavaScript" TYPE="text/javascript">
							<xsl:value-of select="$dummy_name"/><![CDATA[(document.frmXml.]]><xsl:value-of select="$_name"/><![CDATA[arg.value);]]></script>
					</xsl:if>
					<xsl:if test="@paramname">
						<input type="hidden" name="{@paramname}"/>
						<input type="hidden" name="{@paramname}_value"/>
					</xsl:if>
					<xsl:if test="link_list">
						<xsl:apply-templates select="link_list" mode="field_type">
							<xsl:with-param name="text_class">
								<xsl:value-of select="$text_class"/>
							</xsl:with-param>
						</xsl:apply-templates>
					</xsl:if>
				</td>
			</tr>
		</table>
		<!-- 
		<xsl:choose>
			<xsl:when test="/applyeasy/meta/submitted_params_list">
				<xsl:variable name="_fieldname">
					<xsl:value-of select="$_name"/>radio</xsl:variable>
				<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[catalog_radio_change(]]><xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text()"/><![CDATA[);]]></script>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="node_list/node/nav">
						<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[catalog_radio_change(2);]]></script>
					</xsl:when>
					<xsl:otherwise>
						<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[catalog_radio_change(1);]]></script>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
		 -->
	</xsl:template>

	<!-- ==tcr_pickup===-->
	<xsl:template match="*[@type = 'tcr_pickup']" mode="ext_field_type">
		<xsl:param name="text_class"/>

		<xsl:variable name="_name"><xsl:call-template name="get_name"/></xsl:variable>
		<xsl:variable name="desc_name"><xsl:call-template name="get_desc"/></xsl:variable>
		<xsl:variable name="dummy_name"><xsl:value-of select="concat($_name,'_box')"/></xsl:variable>
		
		<xsl:variable name="_node_name" select="name()"/>
		<xsl:variable name="s_exam_ind" select="/applyeasy/item/item_type_meta/@exam_ind"/>

		<xsl:if test="../../section/*[@dependant=$_node_name]">
			<xsl:variable name="dependant_display_name">
				<xsl:for-each select="../../section/*[@dependant=$_node_name and (not(@exam_ind) or @exam_ind = $s_exam_ind)]">
					<xsl:call-template name="get_desc"/>
					<xsl:if test="position() != last()"><xsl:text> , </xsl:text></xsl:if>
				</xsl:for-each>			
			</xsl:variable>
			<script LANGUAGE="JavaScript" TYPE="text/javascript">
<![CDATA[
//confirm_msg & confirm_function for wb_goldenman(tree_type = training_center)
var confirm_msg = wb_msg_itp_reset_dependent_1 + ']]><xsl:value-of select="$dependant_display_name"/><![CDATA[' + wb_msg_itp_reset_dependent_2;
function resetForTcAlter() {]]>
				<xsl:for-each select="../../section/*[@dependant=$_node_name and (not(@exam_ind) or @exam_ind = $s_exam_ind)]">
					<xsl:variable name="_ele_name"><xsl:call-template name="get_name"/></xsl:variable>
					<xsl:variable name="_ele_desc_name"><xsl:call-template name="get_desc"/></xsl:variable>
					<xsl:variable name="dummy_ele_name"><xsl:value-of select="concat($_ele_name,'_box')"/></xsl:variable>
					<xsl:variable name="dummy_field_name">
						<xsl:choose>
							<xsl:when test="@arrayparam">
								<xsl:value-of select="concat($dummy_ele_name,@arrayparam)"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$dummy_ele_name"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>

<![CDATA[
RemoveAllOptions(document.frmXml.]]><xsl:value-of select="$dummy_field_name"/><![CDATA[);]]>
				</xsl:for-each>
<![CDATA[
}
function removeTcrDependant() {
	if(!confirm(confirm_msg)) {
		return false;
	}]]>
				<xsl:for-each select="../../section/*[@dependant=$_node_name and (not(@exam_ind) or @exam_ind = $s_exam_ind)]">
					<xsl:variable name="_ele_name"><xsl:call-template name="get_name"/></xsl:variable>
					<xsl:variable name="_ele_desc_name"><xsl:call-template name="get_desc"/></xsl:variable>
					<xsl:variable name="dummy_ele_name"><xsl:value-of select="concat($_ele_name,'_box')"/></xsl:variable>
					<xsl:variable name="dummy_field_name">
						<xsl:choose>
							<xsl:when test="@arrayparam">
								<xsl:value-of select="concat($dummy_ele_name,@arrayparam)"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$dummy_ele_name"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>

<![CDATA[
RemoveAllOptions(document.frmXml.]]><xsl:value-of select="$dummy_field_name"/><![CDATA[);]]>
				</xsl:for-each>
<![CDATA[
RemoveSingleOption(document.frmXml.]]><xsl:value-of select="$dummy_name"/><![CDATA[_single,document.frmXml.]]><xsl:value-of select="$dummy_name"/><![CDATA[);

var selfFormLabel = $('#cert_sel_form').prevAll('label')[1];
$(selfFormLabel).find('input[type="radio"]')[0].click();
}]]>
			</script>

		</xsl:if>

		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td valign="top">
					<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
				</td>
				<td>
					<input type="hidden" name="{$_name}"/>
					<input type="hidden" name="{$_name}arg">
						<xsl:if test="/applyeasy/meta/submitted_params_list">
							<xsl:variable name="_fieldname"><xsl:value-of select="$_name"/>arg</xsl:variable>
							<xsl:attribute name="value"><xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $_fieldname]/value/text()"/></xsl:attribute>
						</xsl:if>
					</input>
					<xsl:choose>
						<xsl:when test="/applyeasy/item/@class = 'false'">
							<xsl:call-template name="wb_goldenman">
								<xsl:with-param name="frm">document.frmXml</xsl:with-param>
								<xsl:with-param name="name">int_training_provider</xsl:with-param>
								<xsl:with-param name="max_size">1</xsl:with-param>
								<xsl:with-param name="field_name"><xsl:value-of select="$dummy_name"/></xsl:with-param>
								<xsl:with-param name="tree_type">training_center</xsl:with-param>
								<xsl:with-param name="select_type">2</xsl:with-param>
								<xsl:with-param name="confirm_function">resetForTcAlter</xsl:with-param>
								<xsl:with-param name="confirm_msg">confirm_msg</xsl:with-param>
								<xsl:with-param name="pick_root">0</xsl:with-param>
								<xsl:with-param name="remove_btn">false</xsl:with-param>
								<xsl:with-param name="remove_function">
									<xsl:choose>
										<xsl:when test="../../section/*[@dependant=$_node_name]">removeTcrDependant()</xsl:when>
										<xsl:otherwise></xsl:otherwise>
									</xsl:choose>
								</xsl:with-param>
								<xsl:with-param name="single_option_text">
									<xsl:choose>
										<xsl:when test="$training_plan ='true'">
											<xsl:value-of select="/applyeasy/plan/training_center/title"/>
										</xsl:when>
										<xsl:when test="/applyeasy/meta/submitted_params_list">
											<xsl:variable name="tmp_paramname"><xsl:value-of select="@paramname"/>_value</xsl:variable>
											<xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $tmp_paramname]/value/text()"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="center/text()"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:with-param>
								<xsl:with-param name="single_option_value">
									<xsl:choose>
										<xsl:when test="$training_plan ='true'">
											<xsl:value-of select="/applyeasy/plan/training_center/@id"/>
										</xsl:when>
										<xsl:when test="/applyeasy/meta/submitted_params_list">
											<xsl:variable name="tmp_paramname" select="@paramname"/>
											<xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $tmp_paramname]/value/text()"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="center/@id"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="wb_goldenman_no_btn">
								<xsl:with-param name="frm">document.frmXml</xsl:with-param>
								<xsl:with-param name="name">int_training_provider</xsl:with-param>
								<xsl:with-param name="max_size">1</xsl:with-param>
								<xsl:with-param name="field_name"><xsl:value-of select="$dummy_name"/></xsl:with-param>
								<xsl:with-param name="tree_type">training_center</xsl:with-param>
								<xsl:with-param name="select_type">2</xsl:with-param>
								<xsl:with-param name="confirm_function">resetForTcAlter</xsl:with-param>
								<xsl:with-param name="confirm_msg">confirm_msg</xsl:with-param>
								<xsl:with-param name="pick_root">0</xsl:with-param>
								<xsl:with-param name="remove_function">
									<xsl:choose>
										<xsl:when test="../../section/*[@dependant=$_node_name]">removeTcrDependant()</xsl:when>
										<xsl:otherwise></xsl:otherwise>
									</xsl:choose>
								</xsl:with-param>
								<xsl:with-param name="single_option_text">
									<xsl:choose>
										<xsl:when test="$training_plan ='true'">
											<xsl:value-of select="/applyeasy/plan/training_center/title"/>
										</xsl:when>
										<xsl:when test="/applyeasy/meta/submitted_params_list">
											<xsl:variable name="tmp_paramname"><xsl:value-of select="@paramname"/>_value</xsl:variable>
											<xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $tmp_paramname]/value/text()"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="center/text()"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:with-param>
								<xsl:with-param name="single_option_value">
									<xsl:choose>
										<xsl:when test="$training_plan ='true'">
											<xsl:value-of select="/applyeasy/plan/training_center/@id"/>
										</xsl:when>
										<xsl:when test="/applyeasy/meta/submitted_params_list">
											<xsl:variable name="tmp_paramname" select="@paramname"/>
											<xsl:value-of select="/applyeasy/meta/submitted_params_list/param[@name = $tmp_paramname]/value/text()"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="center/@id"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="@paramname">
						<input type="hidden" name="{@paramname}"/>
						<input type="hidden" name="{@paramname}_value"/>
					</xsl:if>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- == External Field Type ===== -->
	<!--Catalog External-->
	<xsl:template match="catalog" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:choose>
			<xsl:when test="@type = 'catalog_attachment'">
				<xsl:apply-templates select="." mode="ext_field_type">
					<xsl:with-param name="text_class">
						<xsl:value-of select="$text_class"/>
					</xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="@type = 'constant'">
				todo: catalog value
			</xsl:when>
			<xsl:otherwise>
				<span class="{$text_class}">Error: catalog support "constant" and "catalog_attachement".</span>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- == External Field Type ===== -->
	<xsl:template match="*[@type = 'filesize']" mode="field_type">
		<xsl:param name="text_class"/>
		<span class="{$text_class}">
			<xsl:for-each select="file">
				<xsl:value-of select="@name"/>
				<xsl:text>&#160;(</xsl:text>
				<xsl:value-of select="@size"/>
				<xsl:text>)</xsl:text>
				<xsl:if test="position() != last()">
					<br/>
				</xsl:if>
			</xsl:for-each>
		</span>
	</xsl:template>
	<!-- == External Field Type ===== -->
	<!-- Competency -->
	<xsl:template match="competency" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:choose>
			<xsl:when test="@type = 'competency_pickup'">
				<xsl:apply-templates select="." mode="ext_field_type">
					<xsl:with-param name="text_class">
						<xsl:value-of select="$text_class"/>
					</xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="@type = 'constant'">
				todo: competency_pickup value
			</xsl:when>
			<xsl:otherwise>
				<span class="{$text_class}">Error: competency support "constant" and "competency_pickup".</span>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- item access -->
	<xsl:template match="item_access" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:choose>
			<xsl:when test="@type = 'item_access_pickup'">
				<xsl:apply-templates select="." mode="ext_field_type">
					<xsl:with-param name="text_class">
						<xsl:value-of select="$text_class"/>
					</xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="@type = 'constant'">
				<xsl:for-each select="assigned_role_list/role/entity">
					<span class="{$text_class}">
						<xsl:value-of select="@display_bil"/>
					</span>
					<xsl:if test="position() != last()">
						<br/>
					</xsl:if>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<span class="{$text_class}">Error: item access support "constant" and "item_access_pickup".</span>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="targeted_lrn_num[@type = 'constant']" mode="field_type" priority="1">
		<xsl:param name="text_class"/>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<input type="hidden" name="{$_name}" value="{num/@value}"/>
		<span class="{$text_class}">
			<xsl:value-of select="num/@value"/>
		</span>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- == Item Figure == -->
	<xsl:template match="*[@type = 'figure_value']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:if test="figure_type/title">
			<xsl:variable name="run_ind" select="/applyeasy/item/@run_ind"/>
			<xsl:if test="@pos = 1">
				<input type="hidden" name="fgt_id_list" value=""/>
				<input type="hidden" name="fig_val_list" value=""/>
			</xsl:if>
			<table cellpadding="2" cellspacing="0" border="0">
				<xsl:for-each select="figure_type">
					<tr>
						<td valign="top" width="100">
							<span class="{$text_class}">
								<xsl:call-template name="get_desc"/>：
						</span>
						</td>
						<td valign="top">
							<span class="{$text_class}">
								<xsl:variable name="_name">
									<xsl:call-template name="get_name"/>
								</xsl:variable>
								<xsl:choose>
									<xsl:when test="num/@value">
										<xsl:variable name="fig_val" select="format-number(num/@value, '0.00')"/>
										<input type="text" value="{$fig_val}" name="{$_name}" class="wzb-inputText"/>
									</xsl:when>
									<xsl:otherwise>
										<input type="text" value="" name="{$_name}" class="wzb-inputText"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
					</tr>
				</xsl:for-each>
			</table>
		</xsl:if>
	</xsl:template>
	<!--Training Center-->
	<xsl:template match="training_center" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:choose>
			<xsl:when test="@type = 'tcr_pickup'">
				<xsl:apply-templates select="." mode="ext_field_type"><xsl:with-param name="text_class"><xsl:value-of select="$text_class"/></xsl:with-param></xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<span class="{$text_class}">Error: training_center supports type "tcr_pickup" only.</span>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- == auto enroll target learners == -->
	<xsl:template match="*[@type = 'auto_enroll_target_learners']" mode="field_type">
		<xsl:param name="text_class"/>
		<xsl:variable name="_value" select="../../../@itm_enroll_type"/>
		<xsl:variable name="lab_no">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">否</xsl:when>
				<xsl:when test="$wb_lang='gb'">否</xsl:when>
				<xsl:otherwise>No</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_auto_confirm">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">是，目標學員可以立刻開始學習此課程</xsl:when>
				<xsl:when test="$wb_lang='gb'">是，目标学员可以立刻开始学习此课程</xsl:when>
				<xsl:otherwise>Yes and enrolled learner can start learning immediately.</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_auto_enroll">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">是，目標學員在通過審批後才能開始學習該課程</xsl:when>
				<xsl:when test="$wb_lang='gb'">是，目标学员在通过审批后才能开始学习该课程</xsl:when>
				<xsl:otherwise>Yes and enrolled learner can start learning only after being approved</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_quota">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">此功能不受名額限制。</xsl:when>
				<xsl:when test="$wb_lang='gb'">此功能不受名额限制。</xsl:when>
				<xsl:otherwise>Auto-enrollment is not limited by quota.</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_note">
			<xsl:choose>
				<xsl:when test="../../../auto_enroll_interval > 0">
					<xsl:choose>
						<xsl:when test="$wb_lang='ch'">(自動報名功能每天執行一次。<xsl:value-of select="$lab_quota"/>)</xsl:when>
						<xsl:when test="$wb_lang='gb'">(自动报名功能每天执行一次。<xsl:value-of select="$lab_quota"/>)</xsl:when>
						<xsl:otherwise>(Auto-enrollment will be executed every day.&#160;<xsl:value-of select="$lab_quota"/>)</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="$wb_lang='ch'">(自動報名功能未啟用。<xsl:value-of select="$lab_quota"/>)</xsl:when>
						<xsl:when test="$wb_lang='gb'">(自动报名功能未启用。<xsl:value-of select="$lab_quota"/>)</xsl:when>
						<xsl:otherwise>(Auto-enrollment is not in use.<xsl:value-of select="$lab_quota"/>)</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>		
		<table>
			<tr>
				<td>
					<label for="auto_erl_1">
						<input type="radio" name="{@paramname}" id="auto_erl_1" value="">
							<xsl:if test="$_value = '' or not($_value)">
								<xsl:attribute name="checked">true</xsl:attribute>
							</xsl:if>
						</input>
						<xsl:value-of select="$lab_no"/>
					</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="auto_erl_2">
						<input type="radio" name="{@paramname}" id="auto_erl_2" value="TARGET_AUTO_CONFIRM">
							<xsl:if test="$_value = 'TARGET_AUTO_CONFIRM'">
								<xsl:attribute name="checked">true</xsl:attribute>
							</xsl:if>
						</input>
						<xsl:value-of select="$lab_auto_confirm"/>
					</label>
				</td>
			</tr>
			<tr>
				<td>
					<label for="auto_erl_3">
						<input type="radio" name="{@paramname}" id="auto_erl_3" value="TARGET_AUTO_ENROLL">
							<xsl:if test="$_value = 'TARGET_AUTO_ENROLL'">
								<xsl:attribute name="checked">true</xsl:attribute>
							</xsl:if>
						</input>
						<xsl:value-of select="$lab_auto_enroll"/>
					</label>				
				</td>
			</tr>
			<tr>
				<td>
					<xsl:value-of select="$lab_note"/>
				</td>
			</tr>
		</table>
	</xsl:template>
	
	<!-- ==Otherwise Field======-->
	<xsl:template match="*" mode="field_type">
		<xsl:param name="text_class"/>
		<div class="{$text_class}">Unknown Item Type (<xsl:value-of select="@type"/> )</div>
		<xsl:if test="link_list">
			<xsl:apply-templates select="link_list" mode="field_type">
				<xsl:with-param name="text_class">
					<xsl:value-of select="$text_class"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	<!-- =Hidden Field======================================================= -->
	<xsl:template match="*" mode="hidden_field">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<input type="hidden" name="{$_name}">
			<xsl:apply-templates select="@*" mode="attribute"/>
		</input>
		<xsl:if test="@paramname">
			<input type="hidden" name="{@paramname}">
				<xsl:apply-templates select="@*" mode="attribute"/>
			</input>
		</xsl:if>
	</xsl:template>
	<!-- =Prefix & Suffix Field======================================================= -->
	<xsl:template name="suffix">
		<xsl:text>&#160;</xsl:text>
		<xsl:value-of select="suffix/desc[@lan = $wb_lang_encoding]/@name"/>
	</xsl:template>
	<xsl:template name="prefix">
		<xsl:text>&#160;</xsl:text>
		<xsl:value-of select="prefix/desc[@lan = $wb_lang_encoding]/@name"/>
	</xsl:template>
	<!-- =Attributes======================================================= -->
	<xsl:template match="@size" mode="attribute">
		<xsl:attribute name="maxlength"><xsl:value-of select="."/></xsl:attribute>
	</xsl:template>
	<xsl:template match="@paramname" mode="attribute"/>
	<xsl:template match="@value" mode="attribute">
		<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
	</xsl:template>
	<xsl:template match="@*" mode="attribute"/>
	<!-- ************************************************************************************************** -->
	<!-- *JS*Validate***************************************************** -->
	<xsl:template match="valued_template" mode="js">
		<xsl:param name="marked_validate"/>
		<xsl:param name="itm_blend_ind"/>
		<xsl:param name="itm_exam_ind"/>
		<xsl:apply-templates select="*" mode="js">
			<xsl:with-param name="marked_validate">
				<xsl:value-of select="$marked_validate"/>
			</xsl:with-param>
			<xsl:with-param name="itm_blend_ind" select="$itm_blend_ind"/>
			<xsl:with-param name="itm_exam_ind" select="$itm_exam_ind"/>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template match="*" mode="js">
		<xsl:param name="marked_validate"/>
		<xsl:param name="itm_blend_ind"/>
		<xsl:param name="itm_exam_ind"/>
		<xsl:choose>
		
			<xsl:when test="$itm_blend_ind != 'true' and $itm_exam_ind != 'true'">
				<xsl:apply-templates select="*[not(@blend_ind and @blend_ind !=$itm_blend_ind) and not(@exam_ind and @exam_ind != $itm_exam_ind)]" mode="validate_js">
					<xsl:with-param name="marked_validate">
						<xsl:value-of select="$marked_validate"/>
					</xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="$itm_exam_ind != 'true'">
				<xsl:apply-templates select="*[not(@exam_ind and @exam_ind != $itm_exam_ind)]" mode="validate_js">
					<xsl:with-param name="marked_validate">
						<xsl:value-of select="$marked_validate"/>
					</xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="$itm_blend_ind != 'true'">
				<xsl:apply-templates select="*[not(@blend_ind and @blend_ind !=$itm_blend_ind)]" mode="validate_js">
					<xsl:with-param name="marked_validate">
						<xsl:value-of select="$marked_validate"/>
					</xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="*" mode="validate_js">
					<xsl:with-param name="marked_validate">
						<xsl:value-of select="$marked_validate"/>
					</xsl:with-param>
				</xsl:apply-templates>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="title | desc" mode="js"/>
	<!-- ***Text Area , Text Field *******************************************-->
	<xsl:template match="*[@type = 'text'] | *[@type = 'textarea'] | *[@type ='url']" mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="marked_required">
			<xsl:choose>
				<xsl:when test="$marked_validate = 'yes' and @marked = 'yes'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc_name"/>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		
		if(frm.<xsl:value-of select="$_name"/> !== undefined) {
		frm.<xsl:value-of select="$_name"/>.value = wbUtilsTrimString(frm.<xsl:value-of select="$_name"/>.value);
		}
		<xsl:if test="(@required = 'yes') or ($marked_required = 'yes')">
		
			if(!gen_validate_empty_field(frm.<xsl:value-of select="$_name"/>,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')){
				frm.<xsl:value-of select="$_name"/>.focus()
				return false;	
			}
			
			<xsl:if test="$_name = 'field01_' or $_name = 'field51_' ">
				if(getChars(frm.<xsl:value-of select="$_name"/>.value) &gt; 20)
				{
				    alert('<xsl:value-of select="$desc_name"/>' + fetchLabel('label_core_training_management_381'))
					frm.<xsl:value-of select="$_name"/>.focus()
					return false;
				}
			</xsl:if>
			<xsl:if test="$_name = 'field02_' or $_name = 'field52_' ">
				if(getChars(frm.<xsl:value-of select="$_name"/>.value) &gt; 200)
				{
				    alert('<xsl:value-of select="$desc_name"/>' + fetchLabel('label_core_training_management_533'))
					frm.<xsl:value-of select="$_name"/>.focus()
					return false;
				}
			</xsl:if>
			
	</xsl:if>
		<xsl:if test="@type = 'textarea' and @size">
		//alert('<xsl:value-of select="$desc_name"/>'+getChars(frm.<xsl:value-of select="$_name"/>.value));
		if(getChars(frm.<xsl:value-of select="$_name"/>.value) &gt; 2000)
		{
		    alert('<xsl:value-of select="$desc_name"/>' + fetchLabel('label_core_training_management_372'))
			frm.<xsl:value-of select="$_name"/>.focus()
			return false;
		}
		<!-- if ( frm.<xsl:value-of select="$_name"/>.value.length &gt; <xsl:value-of select="@size"/>){
			alert('<xsl:value-of select="$desc_name"/>' + wb_msg_<xsl:value-of select="$wb_lang"/>_too_long + ', ' + wb_msg_<xsl:value-of select="$wb_lang"/>_word_limit + ' <xsl:value-of select="@size"/>')					
			frm.<xsl:value-of select="$_name"/>.focus()
			return false;

		} -->
	</xsl:if>
	</xsl:template>
	<!-- ***File****** *******************************************-->
	<xsl:template match="*[@type = 'file']" mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="marked_required">
			<xsl:choose>
				<xsl:when test="$marked_validate = 'yes' and @marked = 'yes'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="(@paramname != '') and ((@required = 'yes') or ($marked_required = 'yes'))">
			<xsl:variable name="desc_name">
				<xsl:call-template name="get_desc_name"/>
			</xsl:variable>
			<xsl:variable name="_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>
		if (frm.<xsl:value-of select="name()"/>__select2 &amp;&amp; frm.<xsl:value-of select="name()"/>__select2.checked == true){
			if(!gen_validate_empty_field(frm.<xsl:value-of select="@paramname"/>,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')){
				frm.<xsl:value-of select="@paramname"/>.focus()
				return false;	
			}
		}
	</xsl:if>
	</xsl:template>
	<!-- ***File****** *******************************************-->
	<xsl:template match="*[@type = 'image']" mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="marked_required">
			<xsl:choose>
				<xsl:when test="$marked_validate = 'yes' and @marked = 'yes'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc_name"/>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:if test="(@paramname != '')">
		if(frm.<xsl:value-of select="@paramname"/>.disabled === false &amp;&amp; frm.<xsl:value-of select="@paramname"/>.value != ''){
			file_ext = frm.<xsl:value-of select="@paramname"/>.value.substring( frm.<xsl:value-of select="@paramname"/>.value.lastIndexOf(".")+1,frm.<xsl:value-of select="@paramname"/>.value.length)
			file_ext =file_ext.toLowerCase()
			if(!(file_ext == 'jpg' || file_ext == 'gif' || file_ext == 'png')){
				alert(wb_msg_<xsl:value-of select="$wb_lang"/>_media_not_support)
				frm.<xsl:value-of select="@paramname"/>.focus()
				return false;
			}
		}
		</xsl:if>
		<xsl:if test="(@paramname != '') and ((@required = 'yes') or ($marked_required = 'yes'))">
			if(!gen_validate_empty_field(frm.<xsl:value-of select="@paramname"/>,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')){
				frm.<xsl:value-of select="@paramname"/>.focus()
				return false;	
			}
	
	</xsl:if>
	</xsl:template>
	<!-- *Email ********************************************************** -->
	<xsl:template match="*[@type = 'email']" mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="marked_required">
			<xsl:choose>
				<xsl:when test="$marked_validate = 'yes' and @marked = 'yes'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc_name"/>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		
		frm.<xsl:value-of select="$_name"/>.value = wbUtilsTrimString(frm.<xsl:value-of select="$_name"/>.value);
		<xsl:choose>
			<xsl:when test="(@required = 'yes') or ($marked_required = 'yes')">
				if(!gen_validate_empty_field(frm.<xsl:value-of select="$_name"/>,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')){
					frm.<xsl:value-of select="$_name"/>.focus()
					return false;	
				}		
			
				if (!gen_validate_email(frm.<xsl:value-of select="$_name"/>,'<xsl:value-of select="$wb_lang"/>')) {
						return false
				}
			</xsl:when>
			<xsl:otherwise>
				if ( frm.<xsl:value-of select="$_name"/>.value.length != 0 ){
					if (!gen_validate_email(frm.<xsl:value-of select="$_name"/>,'<xsl:value-of select="$wb_lang"/>')) {
						return false
					}
				}
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- *Positive Integer*********************************************************-->
	<xsl:template match="*[@type = 'pos_int']" mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="marked_required">
			<xsl:choose>
				<xsl:when test="$marked_validate = 'yes' and @marked = 'yes'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc_name"/>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<!-- process checking -->
		frm.<xsl:value-of select="$_name"/>.value = wbUtilsTrimString(frm.<xsl:value-of select="$_name"/>.value);
		<xsl:choose>
			<xsl:when test="(@required = 'yes') or ($marked_required = 'yes')">
			if (!gen_validate_empty_field(frm.<xsl:value-of select="$_name"/>,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')) {
				frm.<xsl:value-of select="$_name"/>.focus()
				return false;
			}
			
			
			if (!gen_validate_positive_integer(frm.<xsl:value-of select="$_name"/>,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')) {
						frm.<xsl:value-of select="$_name"/>.focus()
						return false
			}					
		</xsl:when>
			<xsl:otherwise>
			if ( frm.<xsl:value-of select="$_name"/>.value.length != 0 ){
				if (!gen_validate_positive_integer(frm.<xsl:value-of select="$_name"/>, '<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')){
						frm.<xsl:value-of select="$_name"/>.focus()
						return false
				}
			}							
		</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- *Positive Integer & Unlimited *********************************************************-->
	<xsl:template match="*[@type = 'pos_int_unlimited']" mode="validate_js">
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc_name"/>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<!-- process checking -->
		if( frm.<xsl:value-of select="$_name"/>radio[1].checked ) {
			frm.<xsl:value-of select="$_name"/>.value = wbUtilsTrimString(frm.<xsl:value-of select="$_name"/>.value);
			if (!gen_validate_empty_field(frm.<xsl:value-of select="$_name"/>,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')) {
				frm.<xsl:value-of select="$_name"/>.focus()
				return false;
			}
			if (!gen_validate_max_integer(frm.<xsl:value-of select="$_name"/>,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')) {
				frm.<xsl:value-of select="$_name"/>.focus()
				return false;
			}
			if (!wbUtilsValidateNonZeroPositiveInteger(frm.<xsl:value-of select="$_name"/>,'<xsl:value-of select="$desc_name"/>')) {
				frm.<xsl:value-of select="$_name"/>.focus()
				return false
			}
			if(!frm.waitlst_ind.disabled) {
				frm.waitlst_ind.value = !frm.waitlst_ind_checkbox.checked;
			}
		}
	</xsl:template>
	<!-- *Positive Amount & Optional *********************************************************-->
	<xsl:template match="*[@type = 'pos_amount_unlimited']" mode="validate_js">
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc_name"/>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<!-- process checking -->
		if( frm.<xsl:value-of select="$_name"/>radio[1].checked ) {
				frm.<xsl:value-of select="$_name"/>.value = wbUtilsTrimString(frm.<xsl:value-of select="$_name"/>.value);
				if (!gen_validate_empty_field(frm.<xsl:value-of select="$_name"/>,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')) {
					frm.<xsl:value-of select="$_name"/>.focus()
					return false;
				}
				
				
				if (!gen_validate_float(frm.<xsl:value-of select="$_name"/>, '<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')){
							frm.<xsl:value-of select="$_name"/>.focus()
							return false
				}					
		}
	</xsl:template>
	<!-- *Positive Amount********************************************* -->
	<xsl:template match="*[@type = 'pos_amount']" mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="marked_required">
			<xsl:choose>
				<xsl:when test="$marked_validate = 'yes' and @marked = 'yes'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- define desc name-->
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc_name"/>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<!-- process checking -->
		frm.<xsl:value-of select="$_name"/>.value = wbUtilsTrimString(frm.<xsl:value-of select="$_name"/>.value);
		<xsl:choose>
			<xsl:when test="(@required = 'yes') or ($marked_required = 'yes')">
				if (!gen_validate_empty_field(frm.<xsl:value-of select="$_name"/>,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')) {
					frm.<xsl:value-of select="$_name"/>.focus()
					return false;
				}
				
				
				if (!gen_validate_float(frm.<xsl:value-of select="$_name"/>, '<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')){
							frm.<xsl:value-of select="$_name"/>.focus()
							return false
				}					
			</xsl:when>
			<xsl:otherwise>
				if ( frm.<xsl:value-of select="$_name"/>.value.length != 0 ){
					if (!gen_validate_float(frm.<xsl:value-of select="$_name"/>, '<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')){
							frm.<xsl:value-of select="$_name"/>.focus()
							return false
					}
				}				
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- *Positive Amount********************************************* -->
	<xsl:template match="*[@type = 'pos_amount_bonus']" mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="marked_required">
			<xsl:choose>
				<xsl:when test="$marked_validate = 'yes' and @marked = 'yes'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- define desc name-->
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc_name"/>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<!-- process checking -->
		<xsl:choose>
			<xsl:when test="(@required = 'yes') or ($marked_required = 'yes')">
				if(frm.field25_[0].checked==true) {
					if (!gen_validate_empty_field(frm.<xsl:value-of select="$_name"/>,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')) {
						frm.<xsl:value-of select="$_name"/>.focus()
						return false;
					}
					if(wbUtilsTrimString(frm.<xsl:value-of select="$_name"/>.value) == 0){
						alert(wb_msg_pls_enter_positive_integer_1 + '"' + '<xsl:value-of select="$desc_name"/>' + '"' + wb_msg_pls_enter_positive_integer_2);
						frm.<xsl:value-of select="$_name"/>.focus()
						return false;
					}	
					if (!wbUtilsValidateAllInteger(frm.<xsl:value-of select="$_name"/>, '<xsl:value-of select="$desc_name"/>')){
						frm.<xsl:value-of select="$_name"/>.focus()
						return false
					}
					if(!gen_validate_max_integer(frm.<xsl:value-of select="$_name"/>,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')){
					    frm.<xsl:value-of select="$_name"/>.focus()
						return false
					}
				} else {
					frm.<xsl:value-of select="$_name"/>.value = '';
				}
						
			</xsl:when>
			<xsl:otherwise>
				if(frm.field25_[0].checked==true) {
					if ( frm.<xsl:value-of select="$_name"/>.value.length != 0 ){
						if (!gen_validate_float_less_than_100(frm.<xsl:value-of select="$_name"/>, '<xsl:value-of select="$desc_name"/>')){
							frm.<xsl:value-of select="$_name"/>.focus()
							return false
						}
					}	
				} else {
					frm.<xsl:value-of select="$_name"/>.value = '';
				}			
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- *Positive Amount Optional ********************************************* -->
	<xsl:template match="*[@type = 'pos_amount_optional']" mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="marked_required">
			<xsl:choose>
				<xsl:when test="$marked_validate = 'yes' and @marked = 'yes'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- define desc name-->
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc_name"/>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<!-- process checking -->
		<xsl:choose>
			<xsl:when test="(@required = 'yes') or ($marked_required = 'yes')">
				if( frm.<xsl:value-of select="$_name"/>radio[1].checked ) {
					frm.<xsl:value-of select="$_name"/>.value = wbUtilsTrimString(frm.<xsl:value-of select="$_name"/>.value);
					if (!gen_validate_empty_field(frm.<xsl:value-of select="$_name"/>,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')) {
						frm.<xsl:value-of select="$_name"/>.focus()
						return false;
					}
					if (!gen_validate_float(frm.<xsl:value-of select="$_name"/>, '<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')){
								frm.<xsl:value-of select="$_name"/>.focus()
								return false
					}
					if (!wbUtilsValidateNonZeroPositiveFloat(frm.<xsl:value-of select="$_name"/>,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')) {
							frm.<xsl:value-of select="$_name"/>.focus()
							return false
				    }
									
				} 
			</xsl:when>
			<xsl:otherwise>
				if( frm.<xsl:value-of select="$_name"/>radio[1].checked ) {
					if ( frm.<xsl:value-of select="$_name"/>.value.length != 0 ){
						frm.<xsl:value-of select="$_name"/>.value = wbUtilsTrimString(frm.<xsl:value-of select="$_name"/>.value);
						if (!gen_validate_float(frm.<xsl:value-of select="$_name"/>, '<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')){
								frm.<xsl:value-of select="$_name"/>.focus()
								return false
						}
					}
				}
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- *CheckBox *************************************************************-->
	<xsl:template match="*[@type = 'checkbox']" mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="marked_required">
			<xsl:choose>
				<xsl:when test="$marked_validate = 'yes' and @marked = 'yes'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc_name"/>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:if test="(@required = 'yes') or ($marked_required = 'yes')"><![CDATA[
				var i, n, ele, str
				str = ""
				n = frm.elements.length;
				for (i = 0; i < n; i++) {
					ele = frm.elements[i]
					if (ele.type == "checkbox" && ele.checked  && ele.name.indexOf(']]><xsl:value-of select="name()"/><![CDATA[') != -1) {
						if (ele.value !="")
							str = str + ele.value + "~"
					}
				}
				
				if (str != "") {
					str = str.substring(0, str.length-1);
				}
				
				if (str == "") {
					alert(eval('wb_msg_]]><xsl:value-of select="$wb_lang"/><![CDATA[_select') + '"]]><xsl:value-of select="$desc_name"/><![CDATA["')
					return
				}
				
		]]></xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*[@paramname='itm_access_type']" mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		var el = frm.<xsl:value-of select="$_name"/>;
		if (el[1].checked) {
			if (frm.targeted_lrn_radio[0].checked == true) {
				alert(wb_msg_itm_access_type);
				frm.targeted_lrn_radio[0].focus();
				return;
			}
		}
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*[@type='auto_enroll_target_learners']" mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="_name">
			<xsl:value-of select="@paramname"/>
		</xsl:variable>
		var el = frm.<xsl:value-of select="$_name"/>;
		//if (!el[0].checked) {
			//if (frm.targeted_lrn_radio[0].checked == true) {
				//alert(wb_msg_itm_auto_enrol);
				//frm.targeted_lrn_radio[0].focus();
				//return;
			//}
		//}
	</xsl:template>	
	<!-- * Radio Button *************************************************** -->
	<!--<xsl:template match="*[@type = 'radio']" mode="validate_js"/>-->
	<!-- * Order List & Unorder List ****************************************** -->
	<xsl:template match="*[@type = 'ul'] | *[@type = 'ol'] |  *[@type = 'named_url']" mode="validate_js">
		<xsl:apply-templates mode="validate_js"/>
	</xsl:template>
	<!-- *Date Time & Unlimited *********************************************************-->
	<xsl:template match="*[@type = 'datetime_unlimited'] " mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="marked_required">
			<xsl:choose>
				<xsl:when test="$marked_validate = 'yes' and @marked = 'yes'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc_name_2"/>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:if test="(@required = 'yes') or ($marked_required = 'yes')">
			if( frm.<xsl:value-of select="$_name"/>_radio[1].checked ) {
				if (!wbUtilsValidateDate('document.' + frm.name + '.<xsl:value-of select="$_name"/>','<xsl:value-of select="$desc_name"/>','','ymdhm'))
					return false	
			}
		</xsl:if>
	</xsl:template>
	<!-- *Content effective start/end *********************************************************-->
	<xsl:template match="*[@type = 'content_eff_start_end'] " mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="marked_required">
			<xsl:choose>
				<xsl:when test="$marked_validate = 'yes' and @marked = 'yes'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc_name"/>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:if test="(@required = 'yes') or ($marked_required = 'yes')">
			if( frm.<xsl:value-of select="$_name"/>_radio[1].checked ) {
				if (!wbUtilsValidateDate('document.' + frm.name + '.<xsl:value-of select="$_name"/>_end_datetime','<xsl:value-of select="$desc_name"/>'))
					return false		
			}

			if( frm.<xsl:value-of select="$_name"/>_radio[2].checked ) {
				if (!gen_validate_empty_field(frm.<xsl:value-of select="$_name"/>_duration,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')) {
					frm.<xsl:value-of select="$_name"/>_duration.focus()
					return false;
				}
				if (!wbUtilsValidateNonZeroPositiveInteger(frm.<xsl:value-of select="$_name"/>_duration,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')) {
							frm.<xsl:value-of select="$_name"/>_duration.focus()
							return false
				}
			}
			<!--DENNIS -->
		</xsl:if>
	</xsl:template>
	<!-- *Date Time*********************************************************-->
	<xsl:template match="*[@type = 'datetime'] | *[@type = 'date'] " mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="marked_required">
			<xsl:choose>
				<xsl:when test="$marked_validate = 'yes' and @marked = 'yes'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc_name_2"/>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:variable name="dType">
			<xsl:choose>
				<xsl:when test="@type = 'datetime'">ymdhm</xsl:when>
				<xsl:otherwise>ymd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="(@required = 'yes') or ($marked_required = 'yes')">
				if (!wbUtilsValidateDate('document.' + frm.name + '.<xsl:value-of select="$_name"/>','<xsl:value-of select="$desc_name"/>','','<xsl:value-of select="$dType"/>'))
					return false		
		</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="@def_time">
						<xsl:variable name="hrs_default" select="substring(@def_time,0,3)"/>
						<xsl:variable name="min_default" select="substring(@def_time,4,2)"/>
				 if((frm.<xsl:value-of select="$_name"/>_yy.value != '') || (frm.<xsl:value-of select="$_name"/>_mm.value != '') || (frm.<xsl:value-of select="$_name"/>_dd.value != '')){
					if (!wbUtilsValidateDate('document.' + frm.name + '.<xsl:value-of select="$_name"/>','<xsl:value-of select="$desc_name"/>','','<xsl:value-of select="$dType"/>'))
						return false							
				}else if((frm.<xsl:value-of select="$_name"/>_hour.value != '<xsl:value-of select="$hrs_default"/>') || (frm.<xsl:value-of select="$_name"/>_min.value != '<xsl:value-of select="$min_default"/>')){
					if (!wbUtilsValidateDate('document.' + frm.name + '.<xsl:value-of select="$_name"/>','<xsl:value-of select="$desc_name"/>','','<xsl:value-of select="$dType"/>'))
						return false						
				}
				</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="@type = 'datetime'">
						if((frm.<xsl:value-of select="$_name"/>_yy.value != '') || (frm.<xsl:value-of select="$_name"/>_mm.value != '') || (frm.<xsl:value-of select="$_name"/>_dd.value != '')||(frm.<xsl:value-of select="$_name"/>_hour.value != '') || (frm.<xsl:value-of select="$_name"/>_min.value != '')){
							if (!wbUtilsValidateDate('document.' + frm.name + '.<xsl:value-of select="$_name"/>','<xsl:value-of select="$desc_name"/>','','<xsl:value-of select="$dType"/>'))
								return false							
						}						
						</xsl:when>
							<xsl:otherwise>
						if((frm.<xsl:value-of select="$_name"/>_yy.value != '') || (frm.<xsl:value-of select="$_name"/>_mm.value != '') || (frm.<xsl:value-of select="$_name"/>_dd.value != '')){
							if (!wbUtilsValidateDate('document.' + frm.name + '.<xsl:value-of select="$_name"/>','<xsl:value-of select="$desc_name"/>','','<xsl:value-of select="$dType"/>'))
								return false							
						}						
						</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- *Multiple & Constant & Single Checkbox**********************************-->
	<xsl:template match="*[@type = 'multiple'] | *[@type = 'constant'] |  *[@type = 'constant_datetime'] |*[@type = 'single_checkbox'] | *[@type = 'read'] | *[@type = 'read_datetime']" mode="validate_js"/>
	<!-- *Misc Type**************************************************************-->
	<xsl:template match="*[subfield_list]" mode="validate_js" priority="10">
		<xsl:param name="marked_validate"/>
		<xsl:apply-templates select="subfield_list/*" mode="validate_js">
			<xsl:with-param name="marked_validate">
				<xsl:value-of select="$marked_validate"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template match="*[not(@type)]" mode="validate_js"/>
	<xsl:template match="*[@type ='cos_announcement'] | *[@type ='cos_content'] | *[@type ='cos_report'] | *[@type ='view_mote'] " mode="validate_js"/>
	<!-- *Resource**************************************************** -->
	<xsl:template match="title" mode="validate_js"/>
	<xsl:template match="*[@type = 'resource']" mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="marked_required">
			<xsl:choose>
				<xsl:when test="$marked_validate = 'yes' and @marked = 'yes'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="(@required = 'yes') or ($marked_required = 'yes')">
			<xsl:variable name="_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>
			<xsl:variable name="desc_name">
				<xsl:call-template name="get_desc_name"/>
			</xsl:variable>
			if(frm.<xsl:value-of select="$_name"/>_box.options.length == 0){
				alert(wb_msg_<xsl:value-of select="$wb_lang"/>_enter_value + ' <xsl:value-of select="$desc_name"/>')
				return false;
			}
		</xsl:if>
	</xsl:template>
	<!-- ====================================== -->
	<xsl:template match="*[@type = 'catalog_attachment']" mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="marked_required">
			<xsl:choose>
				<xsl:when test="$marked_validate = 'yes' and @marked = 'yes'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="(@required = 'yes') or ($marked_required = 'yes')">
			<xsl:variable name="_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>
			<xsl:variable name="desc_name">
				<xsl:call-template name="get_desc_name"/>
			</xsl:variable>
			//if( frm.<xsl:value-of select="$_name"/>radio[1].checked ) {
				if(frm.<xsl:value-of select="$_name"/>_box.options.length == 0){
					alert(wb_msg_<xsl:value-of select="$wb_lang"/>_sel + ' <xsl:value-of select="$desc_name"/>')
					return false;
				}
			//}
		</xsl:if>
	</xsl:template>
	<!-- ====================================== -->
	<xsl:template match="*[@type = 'tcr_pickup']" mode="validate_js">
	<xsl:param name="marked_validate"></xsl:param>
	<xsl:if test="$tcEnabled = 'true'">
		<xsl:variable name="marked_required">
			<xsl:choose>
				<xsl:when test="$marked_validate = 'yes' and @marked = 'yes'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="(@required = 'yes') or ($marked_required = 'yes')">
		<xsl:variable name="_name"><xsl:call-template name="get_name"/></xsl:variable>
		<xsl:variable name="desc_name"><xsl:call-template name="get_desc_name"/></xsl:variable>
			if(frm.<xsl:value-of select="$_name"/>_box.options[0].value == ''){
				alert(wb_msg_<xsl:value-of select="$wb_lang"/>_sel + ' <xsl:value-of select="$desc_name"/>')
				return false;
			}
		</xsl:if>
	</xsl:if>
	</xsl:template>
	<!-- ====================================== -->
	<xsl:template match="*[@type = 'targeted_lrn_pickup']" mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="marked_required">
			<xsl:choose>
				<xsl:when test="$marked_validate = 'yes' and @marked = 'yes'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="(@required = 'yes') or ($marked_required = 'yes')">
			<xsl:variable name="_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>
			<xsl:variable name="desc_name">
				<xsl:call-template name="get_desc_name"/>
			</xsl:variable>
			if( frm.<xsl:value-of select="$_name"/>radio[1].checked ) {
				if(frm.<xsl:value-of select="$_name"/>_box.options.length == 0){
					alert(wb_msg_<xsl:value-of select="$wb_lang"/>_enter_value + ' <xsl:value-of select="$desc_name"/>')
					return false;
				}
			}
		</xsl:if>
	</xsl:template>
	<xsl:template match="*[@type = 'competency_pickup']" mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="marked_required">
			<xsl:choose>
				<xsl:when test="$marked_validate = 'yes' and @marked = 'yes'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="(@required = 'yes') or ($marked_required = 'yes')">
			<xsl:variable name="_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>
			<xsl:variable name="desc_name">
				<xsl:call-template name="get_desc_name"/>
			</xsl:variable>
			if(frm.<xsl:value-of select="$_name"/>_box.options.length == 0){
				alert(wb_msg_<xsl:value-of select="$wb_lang"/>_enter_value + ' <xsl:value-of select="$desc_name"/>')
				return false;
			}
		</xsl:if>
	</xsl:template>
	<xsl:template match="*[@type = 'item_access_pickup']" mode="validate_js">
		<xsl:param name="marked_validate"/>
		<xsl:variable name="marked_required">
			<xsl:choose>
				<xsl:when test="$marked_validate = 'yes' and @marked = 'yes'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="(@required = 'yes') or ($marked_required = 'yes')">
			<xsl:variable name="_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>
			<xsl:variable name="dummy_name">
				<xsl:value-of select="concat($_name,'_box')"/>
			</xsl:variable>
			<xsl:variable name="field_name">
				<xsl:choose>
					<xsl:when test="@arrayparam">
						<xsl:value-of select="concat($dummy_name,@arrayparam)"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$dummy_name"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:variable name="desc_name">
				<xsl:call-template name="get_desc_name"/>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="@size = '1'">
					if(frm.<xsl:value-of select="$field_name"/>_single.value == ""){
						alert(wb_msg_<xsl:value-of select="$wb_lang"/>_enter_value + ' <xsl:value-of select="$desc_name"/>')
						return false;
					}
				</xsl:when>
				<xsl:otherwise>
					if(frm.<xsl:value-of select="$field_name"/>.options.length == 0){
						alert(wb_msg_<xsl:value-of select="$wb_lang"/>_enter_value + ' <xsl:value-of select="$desc_name"/>')
						return false;
					}
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	<!-- notify email limited *********************************************************-->
	<xsl:template match="*[@type = 'notify_email_limited']" mode="validate_js">
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc_name"/>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		if( frm.<xsl:value-of select="$_name"/>radio[1].checked ) {
			frm.<xsl:value-of select="$_name"/>.value = wbUtilsTrimString(frm.<xsl:value-of select="$_name"/>.value);
			if (!gen_validate_empty_field(frm.<xsl:value-of select="$_name"/>,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')) {
				frm.<xsl:value-of select="$_name"/>.focus()
				return false;
			}
			if (!gen_validate_positive_integer(frm.<xsl:value-of select="$_name"/>,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')) {
				frm.<xsl:value-of select="$_name"/>.focus()
				return false
			}
			if (!wbUtilsValidateNonZeroPositiveInteger(frm.<xsl:value-of select="$_name"/>,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')) {
					frm.<xsl:value-of select="$_name"/>.focus()
					return false
		    }
		}
	</xsl:template>
	<xsl:template match="*[@type = 'notify_support_email']" mode="validate_js">
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc_name"/>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		if( frm.<xsl:value-of select="$_name"/>radio[1].checked ) {
			frm.itm_support_email.value = wbUtilsTrimString(frm.itm_support_email.value);
			if(!gen_validate_empty_field(frm.itm_support_email,'<xsl:value-of select="$desc_name"/>','<xsl:value-of select="$wb_lang"/>')){
					frm.itm_notify_email.focus()
					return false;	
			}		
			if (!gen_validate_email(frm.itm_support_email,'<xsl:value-of select="$wb_lang"/>')) {
						return false
		   }
		}
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="resource" mode="validate_js"/>
	<!-- =============================================================== -->
	<!-- #Generate Form XML ########################################################################### -->
	<xsl:template match="valued_template" mode="gen_xml">
		<xsl:param name="itm_blend_ind"/>
		<xsl:param name="itm_exam_ind"/>
		str+='<detail>'
		<xsl:choose>
			<xsl:when test="$itm_blend_ind != 'true' and $itm_exam_ind != 'true'">
				<xsl:apply-templates select="section/*[not(@blend_ind and @blend_ind !=$itm_blend_ind) and not(@exam_ind and @exam_ind != $itm_exam_ind)]" mode="gen_xml_form">
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="$itm_exam_ind != 'true'">
				<xsl:apply-templates select="section/*[not(@exam_ind and @exam_ind != $itm_exam_ind)]" mode="gen_xml_form">
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="$itm_blend_ind != 'true'">
				<xsl:apply-templates select="section/*[not(@blend_ind and @blend_ind !=$itm_blend_ind)]" mode="gen_xml_form">
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="section/*" mode="gen_xml_form">
				</xsl:apply-templates>
			</xsl:otherwise>
		</xsl:choose>
		str+='</detail>'
	</xsl:template>
	<!-- ##Blocker for External Field############################-->
	<xsl:template match="*[@external_field = 'yes']" mode="gen_xml_form" priority="10"/>
	<xsl:template match="catalog | compentency | targeted_lrn | targeted_lrn_num | item_access | mote_plan | mote_level | mote_due_date | mote_target_rating | mote_cost_target | mote_time_target | link_list | item_status | cos_eff_start_datetime | cos_eff_end_datetime | mote_plan_desc " mode="gen_xml_form" priority="5"/>
	<!-- ##Text , Email , Textarea & Constant#####################-->
	<xsl:template match="*[@type = 'text'] | *[@type = 'textarea'] | *[@type = 'email'] | *[@type = 'constant'] | *[@type = 'constant_datetime']|*[@type = 'pos_int' ] | *[@type='pos_amount'] | *[@type ='date' ] | *[@type = 'datetime'] |*[@type = 'url']  | *[@type = 'constant_label'] | *[@type='pos_amount_bonus']" mode="gen_xml_form">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>		
		str+='<xsl:element name="{name()}">
			<xsl:if test="@id">
				<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
			</xsl:if>'
		if(frm.<xsl:value-of select="$_name"/> !== undefined) {
		str+= wb_utils_XmlEscape(frm.<xsl:value-of select="$_name"/>.value) 
		}
		str+='</xsl:element>'
	</xsl:template>
	<xsl:template match="*[@type = 'datetime_unlimited']" mode="gen_xml_form">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		str+='<xsl:element name="{name()}">
			<xsl:if test="@id">
				<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
			</xsl:if>'
		if( frm.<xsl:value-of select="$_name"/>_radio[1].checked ) {
			str+= wb_utils_XmlEscape(frm.<xsl:value-of select="$_name"/>.value) 
		} else {
			str += '9999-12-31 23:59:59.000'
		}
		str+='</xsl:element>'		
	</xsl:template>
	<xsl:template match="*[@type = 'pos_int_unlimited']" mode="gen_xml_form">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		str+='<xsl:element name="{name()}">
			<xsl:if test="@id">
				<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
			</xsl:if>'
		if( frm.<xsl:value-of select="$_name"/>radio[1].checked ) {
			str+= wb_utils_XmlEscape(frm.<xsl:value-of select="$_name"/>.value) 
		}
		str+='</xsl:element>'	
	</xsl:template>
	<xsl:template match="*[@type = 'pos_amount_optional']" mode="gen_xml_form">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		str+='<xsl:element name="{name()}">
			<xsl:if test="@id">
				<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
			</xsl:if>'
		if( frm.<xsl:value-of select="$_name"/>radio[1].checked ) {
			str+= wb_utils_XmlEscape(frm.<xsl:value-of select="$_name"/>.value) 
		}
		str+='</xsl:element>'	
	</xsl:template>
	<!--## File########################################-->
	<xsl:template match="*[@type = 'file']" mode="gen_xml_form">
		<xsl:if test="@paramname != ''">
			<xsl:variable name="_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>		
		str+='<xsl:element name="{name()}">
				<xsl:if test="@id">
					<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
				</xsl:if>'
		filename = frm.<xsl:value-of select="$_name"/>.value.substring(frm.<xsl:value-of select="$_name"/>.value.lastIndexOf("/") +1,frm.<xsl:value-of select="$_name"/>.value.length)
		filename = filename.substring(filename.lastIndexOf("\\") +1 ,filename.length);
		str+= wb_utils_XmlEscape(filename) 
		str+='</xsl:element>'
	</xsl:if>
	</xsl:template>
	<!--##Image########################################-->
	<xsl:template match="*[@type = 'image']" mode="gen_xml_form">
		<xsl:if test="@paramname != ''">
			<xsl:variable name="_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>		
		str+='<xsl:element name="{name()}">
				<xsl:if test="@id">
					<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
				</xsl:if>'
		filename = frm.<xsl:value-of select="$_name"/>.value.substring(frm.<xsl:value-of select="$_name"/>.value.lastIndexOf("/") +1,frm.<xsl:value-of select="$_name"/>.value.length)
		filename = filename.substring(filename.lastIndexOf("\\") +1 ,filename.length);
		str+= wb_utils_XmlEscape(filename) 
		str+='</xsl:element>'
	</xsl:if>
	</xsl:template>
	<!-- ##Order List & UnOrder List###############-->
	<xsl:template match="*[@type = 'ul'] | *[@type = 'ol'] | *[@type = 'named_url']" mode="gen_xml_form">
		str+='<xsl:element name="{name()}">
			<xsl:if test="@id">
				<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
			</xsl:if>'
			<xsl:apply-templates mode="gen_xml_form"/>
		str+='</xsl:element>'
	</xsl:template>
	<!-- ##Check Box########################-->
	<xsl:template match="*[@type = 'checkbox']" mode="gen_xml_form">
		str+='<xsl:element name="{name()}">'
		<xsl:for-each select="*[name() != 'title']">
				<xsl:variable name="_name">
					<xsl:call-template name="get_name"/>
				</xsl:variable>
				<xsl:variable name="my_name" select="name()"/>
				<xsl:variable name="my_id" select="@id"/>
			if(frm.<xsl:value-of select="$_name"/>.checked == true)
			{
				str+='<xsl:element name="{name()}">
					<xsl:if test="@id">
						<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
					</xsl:if>'
				str+=wb_utils_XmlEscape('<xsl:value-of select="@value"/>')				
				str+='</xsl:element>'
			}
		</xsl:for-each>
		str+='</xsl:element>'
	</xsl:template>
	<!-- ##Select Box########################-->
	<xsl:template match="*[@type = 'select' or @type='reloading_select' ]" mode="gen_xml_form">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>		
		str+='<xsl:element name="{name()}">
			<xsl:if test="@id">
				<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
			</xsl:if>'
		str+= wb_utils_XmlEscape(frm.<xsl:value-of select="$_name"/>.options[frm.<xsl:value-of select="$_name"/>.selectedIndex].value) 
		str+='</xsl:element>'
	</xsl:template>
	<!-- ## Radio Button ###################### -->
	<xsl:template match="*[@type = 'radio'] | *[@type='radio_bonus']" mode="gen_xml_form">
		<xsl:variable name="my_name" select="name()"/>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		var radioidx = 0;
		var checked_idx=0;
		var name_list = new Array()
		<xsl:for-each select="*[name() = $my_name]">
			<xsl:variable name="my_desc">
				<xsl:call-template name="get_desc"/>
			</xsl:variable>
			name_list[<xsl:value-of select="position() - 1"/>] = '<xsl:call-template name="escape_js">
				<xsl:with-param name="input_str">
					<xsl:value-of select="@value"/>
				</xsl:with-param>
			</xsl:call-template>'
		</xsl:for-each>
		for(radioidx = 0 ;radioidx <xsl:text disable-output-escaping="yes"><![CDATA[<]]></xsl:text> frm.<xsl:value-of select="$_name"/>.length;radioidx++){
			if(frm.<xsl:value-of select="$_name"/>[radioidx].checked == true)
			{
				checked_idx = radioidx
			}
		}		
		str+='<xsl:element name="{name()}">
			<xsl:if test="@id">
				<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
			</xsl:if>'
				str+='<xsl:text disable-output-escaping="yes"><![CDATA[<]]></xsl:text>
			<xsl:value-of select="name()"/> id="'
				str+=checked_idx +1
				str+='"<xsl:text disable-output-escaping="yes"><![CDATA[>]]></xsl:text>'
				str+=wb_utils_XmlEscape(name_list[checked_idx])
				str+='<xsl:text disable-output-escaping="yes"><![CDATA[<]]></xsl:text>/<xsl:value-of select="name()"/>
			<xsl:text disable-output-escaping="yes"><![CDATA[>]]></xsl:text>'
		str+='</xsl:element>'
	</xsl:template>
	<!-- ## Resource ######################## -->
	<xsl:template match="*[@type = 'resource']" mode="gen_xml_form">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>	
		str+='<xsl:element name="{name()}">
			<xsl:if test="@id">
				<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
			</xsl:if>'
		<![CDATA[if(frm.]]><xsl:value-of select="$_name"/>
			<xsl:text disable-output-escaping="yes">_box.options.length > 0)</xsl:text>
		{
			
			str+='<resource_list>'
			var residx = 0;
			for(residx=0;residx<xsl:text disable-output-escaping="yes"><![CDATA[<]]></xsl:text>frm.<xsl:value-of select="$_name"/>_box.options.length;residx++){
				str+='<xsl:text disable-output-escaping="yes"><![CDATA[<]]></xsl:text>resource id="'
				str+= frm.<xsl:value-of select="$_name"/>_box.options[residx].value
				str+='"<xsl:text disable-output-escaping="yes"><![CDATA[>]]></xsl:text>'
				str+= wb_utils_XmlEscape(frm.<xsl:value-of select="$_name"/>_box.options[residx].text)
				str+='<xsl:text disable-output-escaping="yes"><![CDATA[<]]></xsl:text>/resource<xsl:text disable-output-escaping="yes"><![CDATA[>]]></xsl:text>'
			} 
			str+='</resource_list>'
		}
		str+='</xsl:element>'
	</xsl:template>
	<xsl:template match="resource" mode="gen_xml_form"/>
	<!-- ##Subfield List########################-->
	<xsl:template match="*[subfield_list]" mode="gen_xml_form">
		str+='<xsl:element name="{name()}">
			<xsl:if test="@id">
				<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
			</xsl:if>'	
			<xsl:apply-templates mode="gen_xml_form"/>
		str+='</xsl:element>'
	</xsl:template>
	<xsl:template match="*[name() = 'subfield_list']" mode="gen_xml_form">
	str+='<xsl:element name="subfield_list">'
		<xsl:apply-templates mode="gen_xml_form"/>
	str+='</xsl:element>'
	</xsl:template>
	<xsl:template match="mote_level" mode="gen_xml_form" priority="1"/>
	<!-- ####################################################################################### -->
	<!-- # Feed JS Value#-->
	<xsl:template match="valued_template" mode="feed_param_value">
		<xsl:param name="itm_blend_ind"/>
		<xsl:param name="itm_exam_ind"/>
		<xsl:apply-templates mode="feed_param">
			<xsl:with-param name="itm_blend_ind" select="$itm_blend_ind"/>
			<xsl:with-param name="itm_exam_ind" select="$itm_exam_ind"/>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template match="*" mode="feed_param">
		<xsl:param name="itm_blend_ind"/>
		<xsl:param name="itm_exam_ind"/>
		<xsl:choose>
			<xsl:when test="$itm_blend_ind != 'true' and $itm_exam_ind != 'true'">
				<xsl:apply-templates select="*[not(@blend_ind and @blend_ind !=$itm_blend_ind) and not(@exam_ind and @exam_ind != $itm_exam_ind)]" mode="feed_param"/>
			</xsl:when>
			<xsl:when test="$itm_exam_ind != 'true'">
				<xsl:apply-templates select="*[not(@exam_ind and @exam_ind != $itm_exam_ind)]" mode="feed_param"/>
			</xsl:when>
			<xsl:when test="$itm_blend_ind != 'true'">
				<xsl:apply-templates select="*[not(@blend_ind and @blend_ind !=$itm_blend_ind)]" mode="feed_param"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="*" mode="feed_param"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- #Text , Email , and Textarea ###########-->
	<xsl:template match="*[@type = 'text'] | *[@type = 'email'] | *[@type='textarea'] | *[@type = 'pos_int'] | *[@type = 'pos_amount'] | *[@type='constant'] |*[@type='constant_datetime'] | *[@type = 'pos_amount_bonus']" mode="feed_param">
		<xsl:choose>
			<xsl:when test="@paramformat='xml'">
				<xsl:variable name="_name">
					<xsl:call-template name="get_name"/>
				</xsl:variable>
				mote_str = '';
				mote_str += '<xsl:element name="{name()}">';
				mote_str += wb_utils_XmlEscape(frm.<xsl:value-of select="$_name"/>.value);
				mote_str += '</xsl:element>';
				frm.<xsl:value-of select="@paramname"/>.value  = mote_str;
			</xsl:when>
			<xsl:when test="@paramname">
				<xsl:variable name="_name">
					<xsl:call-template name="get_name"/>
				</xsl:variable>
				if(frm.<xsl:value-of select="@paramname"/> !== undefined) {
				frm.<xsl:value-of select="@paramname"/>.value = frm.<xsl:value-of select="$_name"/>.value
				}
			</xsl:when>
		</xsl:choose>
		<xsl:apply-templates mode="feed_param"/>
	</xsl:template>
	<xsl:template match="*[@type = 'read'] | *[@type = 'read_datetime'] " mode="feed_param"/>
	<!-- #Positive Integer & Unlimited ###########-->
	<xsl:template match=" *[@type='pos_int_unlimited']" mode="feed_param">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		if( frm.<xsl:value-of select="$_name"/>radio[1].checked ) {
			frm.<xsl:value-of select="@paramname"/>.value = frm.<xsl:value-of select="$_name"/>.value
		} else {
			frm.<xsl:value-of select="@paramname"/>.value = 0
		}
		<xsl:apply-templates mode="feed_param"/>
	</xsl:template>
	<!-- #Positive Amount & Optional ###########-->
	<xsl:template match=" *[@type='pos_amount_optional']" mode="feed_param">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		if( frm.<xsl:value-of select="$_name"/>radio[1].checked ) {
			frm.<xsl:value-of select="@paramname"/>.value = frm.<xsl:value-of select="$_name"/>.value
		} else {
			frm.<xsl:value-of select="@paramname"/>.value = 0
		}
		<xsl:apply-templates mode="feed_param"/>
	</xsl:template>
	<!-- #URL ###########################-->
	<xsl:template match="*[@type = 'url']" mode="feed_param">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
			if(frm.<xsl:value-of select="$_name"/>.value == 'http://'){
				frm.<xsl:value-of select="$_name"/>.value = ''
			}	
		<xsl:if test="@paramname">
			frm.<xsl:value-of select="@paramname"/>.value = frm.<xsl:value-of select="$_name"/>.value
		</xsl:if>
		<xsl:apply-templates mode="feed_param"/>
	</xsl:template>
	<!-- #File##########################-->
	<xsl:template match="*[@type = 'file'] | *[@type = 'image']" mode="feed_param">
		<xsl:if test="@paramname!= ''">
			<xsl:variable name="_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="@value !=''">
				if(frm.<xsl:value-of select="$_name"/>_select0.checked == true){
					frm.<xsl:value-of select="$_name"/>.value = frm.<xsl:value-of select="$_name"/>_hidden.value
					frm.<xsl:value-of select="@paramname"/>_name.value = ''
					wb_utils_set_cookie("preloadFlag","true");
				}else if(frm.<xsl:value-of select="$_name"/>_select1.checked == true){
					frm.<xsl:value-of select="$_name"/>.value = frm.default_image.value;
					frm.<xsl:value-of select="@paramname"/>_name.value = frm.<xsl:value-of select="$_name"/>_hidden.value
					document.all.<xsl:value-of select="@paramname"/>_del_ind.value='true';
					wb_utils_set_cookie("preloadFlag","true");
				}
				else{
					frm.<xsl:value-of select="$_name"/>.value = frm.<xsl:value-of select="@paramname"/>.value
					frm.<xsl:value-of select="@paramname"/>_name.value = frm.<xsl:value-of select="$_name"/>_hidden.value
					if(frm.<xsl:value-of select="@paramname"/>.value!="")
						wb_utils_set_cookie("preloadFlag","true");
				}
			</xsl:when>
				<xsl:otherwise>
					if(frm.<xsl:value-of select="$_name"/>_select1.checked == true){
						frm.<xsl:value-of select="$_name"/>.value = frm.default_image.value;
						document.all.<xsl:value-of select="@paramname"/>_del_ind.value='true';
						wb_utils_set_cookie("preloadFlag","true");
					} else {
						frm.<xsl:value-of select="$_name"/>.value = frm.<xsl:value-of select="@paramname"/>.value
						if(frm.<xsl:value-of select="@paramname"/>.value!="")
							wb_utils_set_cookie("preloadFlag","true");
					}
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
		<xsl:apply-templates mode="feed_param"/>
	</xsl:template>
	<!-- #Date , Datetime ##################-->
	<xsl:template match="*[@type = 'date'] | *[@type='datetime'] " mode="feed_param">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		frm.<xsl:value-of select="$_name"/>.value = "";
		if(!(frm.<xsl:value-of select="$_name"/>_yy.value == '' || frm.<xsl:value-of select="$_name"/>_mm.value == '' || frm.<xsl:value-of select="$_name"/>_dd.value == '' || frm.<xsl:value-of select="$_name"/>_hour.value== '' || frm.<xsl:value-of select="$_name"/>_min.value == '')){
			frm.<xsl:value-of select="$_name"/>.value = frm.<xsl:value-of select="$_name"/>_yy.value + '-' + frm.<xsl:value-of select="$_name"/>_mm.value + '-' + frm.<xsl:value-of select="$_name"/>_dd.value + ' '
			frm.<xsl:value-of select="$_name"/>.value += frm.<xsl:value-of select="$_name"/>_hour.value +  ":" + frm.<xsl:value-of select="$_name"/>_min.value + ":00.000"
		}
	<xsl:if test="@paramname">
		frm.<xsl:value-of select="@paramname"/>.value = frm.<xsl:value-of select="$_name"/>.value
	</xsl:if>
	</xsl:template>
	<!-- #Datetime_unlimited ##################-->
	<xsl:template match=" *[@type = 'datetime_unlimited']" mode="feed_param">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		frm.<xsl:value-of select="$_name"/>.value = "";
		if(!(frm.<xsl:value-of select="$_name"/>_yy.value == '' || frm.<xsl:value-of select="$_name"/>_mm.value == '' || frm.<xsl:value-of select="$_name"/>_dd.value == '' || frm.<xsl:value-of select="$_name"/>_hour.value== '' || frm.<xsl:value-of select="$_name"/>_min.value == '')){
			frm.<xsl:value-of select="$_name"/>.value = frm.<xsl:value-of select="$_name"/>_yy.value + '-' + frm.<xsl:value-of select="$_name"/>_mm.value + '-' + frm.<xsl:value-of select="$_name"/>_dd.value + ' '
			frm.<xsl:value-of select="$_name"/>.value += frm.<xsl:value-of select="$_name"/>_hour.value +  ":" + frm.<xsl:value-of select="$_name"/>_min.value + ":00.000"
		}
	<xsl:if test="@paramname">
		if(frm.<xsl:value-of select="$_name"/>_radio[1].checked )  {
			frm.<xsl:value-of select="@paramname"/>.value = frm.<xsl:value-of select="$_name"/>.value
		} else {
			frm.<xsl:value-of select="@paramname"/>.value = '9999-12-31 23:59:59.000';
		}
	</xsl:if>
	</xsl:template>
	<!-- #Datetime_unlimited ##################-->
	<xsl:template match=" *[@type = 'content_eff_start_end']" mode="feed_param">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		frm.<xsl:value-of select="$_name"/>.value = "";
	<xsl:if test="@paramname">
		if(frm.<xsl:value-of select="$_name"/>_radio[1].checked )  {
			if(!(frm.<xsl:value-of select="$_name"/>_end_datetime_yy.value == '' || frm.<xsl:value-of select="$_name"/>_end_datetime_mm.value == '' || frm.<xsl:value-of select="$_name"/>_end_datetime_dd.value == '')){
				frm.<xsl:value-of select="$_name"/>.value = frm.<xsl:value-of select="$_name"/>_end_datetime_yy.value + '-' + frm.<xsl:value-of select="$_name"/>_end_datetime_mm.value + '-' + frm.<xsl:value-of select="$_name"/>_end_datetime_dd.value + ' '
				frm.<xsl:value-of select="$_name"/>.value += "23:59:59.000"
			}
		} else if (frm.<xsl:value-of select="$_name"/>_radio[2].checked) {
			frm.<xsl:value-of select="$_name"/>.value = frm.<xsl:value-of select="$_name"/>_duration.value
		} else {
			frm.<xsl:value-of select="$_name"/>.value = '';
		}
		frm.<xsl:value-of select="@paramname"/>.value = frm.<xsl:value-of select="$_name"/>.value
	</xsl:if>
	</xsl:template>
	<!-- #Select Box #####################-->
	<xsl:template match="*[@type='select' or @type='reloading_select']" mode="feed_param">
		<xsl:if test="@paramname">
			<xsl:variable name="_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>
			frm.<xsl:value-of select="@paramname"/>.value = frm.<xsl:value-of select="$_name"/>.options[frm.<xsl:value-of select="$_name"/>.selectedIndex].value
		</xsl:if>
		<xsl:apply-templates mode="feed_param"/>
	</xsl:template>
	<!-- #Radio Button ################### -->
	<xsl:template match="*[@type = 'radio' or @type='radio_bonus']" mode="feed_param">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<xsl:if test="@paramname">
			var rad=0;
			if(frm.<xsl:value-of select="$_name"/>) {
			for(rad=0;rad &lt;  frm.<xsl:value-of select="$_name"/>.length;rad++){
				if(frm.<xsl:value-of select="$_name"/>[rad].checked == true){
					frm.<xsl:value-of select="@paramname"/>.value = frm.<xsl:value-of select="$_name"/>[rad].value
				}
			}
			}
		</xsl:if>
		<xsl:apply-templates mode="feed_param"/>
	</xsl:template>
	<!-- = notify_email_limited = -->
	<xsl:template match=" *[@type='notify_email_limited']" mode="feed_param">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		if( frm.<xsl:value-of select="$_name"/>radio[1].checked ) {
			frm.<xsl:value-of select="@paramname"/>.value = frm.<xsl:value-of select="$_name"/>.value;
		} else {
			frm.<xsl:value-of select="@paramname"/>.value = -1;
		}
	</xsl:template>
	<xsl:template match=" *[@type='notify_support_email']" mode="feed_param">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		if( frm.<xsl:value-of select="$_name"/>radio[1].checked ) {
			frm.<xsl:value-of select="@paramname"/>.value = frm.itm_support_email.value;
		} else {
			frm.<xsl:value-of select="@paramname"/>.value = '';
		}
	</xsl:template>
	<!-- #CheckBox ##################### -->
	<xsl:template match="*[@type = 'checkbox']" mode="feed_param"/>
	<!-- #Resource ##################### -->
	<!-- #Resource Field use resource XML as value #-->
	<xsl:template match="*[@type = 'resource']" mode="feed_param">
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>	
		res_str=''
		res_str+='<xsl:element name="{name()}">
			<xsl:if test="@id">
				<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
			</xsl:if>'
		<![CDATA[if(frm.]]><xsl:value-of select="$_name"/>
			<xsl:text disable-output-escaping="yes">_box.options.length > 0)</xsl:text>
		{
			
			res_str+='<resource_list>'
			var residx = 0;
			for(residx=0;residx<xsl:text disable-output-escaping="yes"><![CDATA[<]]></xsl:text>frm.<xsl:value-of select="$_name"/>_box.options.length;residx++){
				res_str+='<xsl:text disable-output-escaping="yes"><![CDATA[<]]></xsl:text>resource id="'
				res_str+= frm.<xsl:value-of select="$_name"/>_box.options[residx].value
				res_str+='"<xsl:text disable-output-escaping="yes"><![CDATA[>]]></xsl:text>'
				res_str+= wb_utils_XmlEscape(frm.<xsl:value-of select="$_name"/>_box.options[residx].text)
				res_str+='<xsl:text disable-output-escaping="yes"><![CDATA[<]]></xsl:text>/resource<xsl:text disable-output-escaping="yes"><![CDATA[>]]></xsl:text>'
			} 
			res_str+='</resource_list>'
		}
		res_str+='</xsl:element>'
		
		<xsl:choose>
			<xsl:when test="@paramname">
				frm.<xsl:value-of select="@paramname"/>.value  = res_str;
			</xsl:when>
			<xsl:otherwise>
				frm.<xsl:value-of select="$_name"/>.value = res_str;
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--##External Field Start ###############-->
	<!--#Single CheckBox #################-->
	<xsl:template match="*[@type= 'single_checkbox']" mode="feed_param">
		<xsl:if test="@paramname">
			<xsl:variable name="_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>
		if(frm.<xsl:value-of select="$_name"/>.checked == true)
		{
			frm.<xsl:value-of select="@paramname"/>.value = frm.<xsl:value-of select="$_name"/>.value
		}
		</xsl:if>
	</xsl:template>
	<!--#targeted_lrn_pickup ###############-->
	<xsl:template match="*[@type= 'user_group_pickup' or @type = 'targeted_lrn_pickup']" mode="feed_param">
		<xsl:if test="@paramname">
			<xsl:variable name="_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>
			if( frm.<xsl:value-of select="$_name"/>radio[1].checked ) {
				var tl_list = ''
				var tl_list_arg = ''
				var tl_idx = 0
				for (tl_idx = 0; tl_idx &lt; frm.<xsl:value-of select="$_name"/>_box.options.length;tl_idx++){
					tl_set = new Array()
					tl_set = frm.<xsl:value-of select="$_name"/>_box.options[tl_idx].value.split("~")
					tl_set_text = new Array()
					tl_set_text = frm.<xsl:value-of select="$_name"/>_box.options[tl_idx].text.split(",")
					var j=0;
					temp_str ='';
					temp_text = '';
					for(j=0;j&lt;tl_set.length;j++){
						if(tl_set[j] != '0'){
							 temp_str += tl_set[j] + ","
						}
						if( j > 0 ) {
							tl_set_text[j] = tl_set_text[j].substring(1, tl_set_text[j].length)
						}
						temp_text += 'TYPE~%~' + tl_set[j] + '~%~' + tl_set_text[j] + '~%~TYPE' + '[~!~]'
					}
					temp_str = temp_str.substring(0,temp_str.length -1)
					temp_text = temp_text.substring(0, temp_text.length - 5)
					tl_list += temp_str 
					tl_list_arg += temp_text
					if(tl_idx != (frm.<xsl:value-of select="$_name"/>_box.options.length -1)){
						tl_list += '~'
						tl_list_arg += '[~!~!~]'
					}
				}
				frm.<xsl:value-of select="@paramname"/>.value = tl_list
				frm.<xsl:value-of select="$_name"/>arg.value = tl_list_arg
			}
		</xsl:if>
	</xsl:template>
	<!--#item_access_pickup ###############-->
	<!-- DENNIS -->
	<xsl:template match="*[@type= 'item_access_pickup']" mode="feed_param">
		<xsl:if test="@paramname">
			<xsl:variable name="raw_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>
			<xsl:variable name="_name">
				<xsl:choose>
					<xsl:when test="@arrayparam">
						<xsl:value-of select="concat($raw_name,'_box',@arrayparam)"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="concat($raw_name,'_box')"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			
			var iac_list = '<xsl:value-of select="@id"/>'+'_'+<xsl:value-of select="/applyeasy/meta/cur_usr/@root_ent_id"/>
			var iac_list_arg = 'user_group_and_user'
			var iac_idx = 0
			for (iac_idx = 0; iac_idx &lt; frm.<xsl:value-of select="$_name"/>.options.length;iac_idx++){
				iac_list += '~'
				iac_list += frm.<xsl:value-of select="$_name"/>.options[iac_idx].value
				iac_list_arg += '~%~' + frm.<xsl:value-of select="$_name"/>.options[iac_idx].value + '~%~' + frm.<xsl:value-of select="$_name"/>.options[iac_idx].text +'~%~USR'
			}	
			<xsl:choose>
				<xsl:when test="@arrayparam">frm.<xsl:value-of select="@paramname"/>[<xsl:value-of select="@arrayparam"/>].value = iac_list</xsl:when>
				<xsl:otherwise>frm.<xsl:value-of select="@paramname"/>.value = iac_list</xsl:otherwise>
			</xsl:choose>	
			frm.<xsl:value-of select="$raw_name"/>arg.value = iac_list_arg;
		</xsl:if>
	</xsl:template>
	<!--#run_item_access ###############-->
	<xsl:template match="run_item_access | child_item_access" mode="feed_param" priority="10">
		<xsl:if test="@paramname">
			<xsl:variable name="raw_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>
			<xsl:variable name="_name" select="$raw_name"/>
			var riac_list = '<xsl:value-of select="@id"/>~'
			var riac_idx = 0
			<xsl:choose>
				<xsl:when test="@type= 'iac_select'">
					<xsl:choose>
						<xsl:when test="count(assigned_role_list/role/entity) != 0">				
					riac_list += frm.<xsl:value-of select="$_name"/>.options[frm.<xsl:value-of select="$_name"/>.selectedIndex].value
				</xsl:when>
						<xsl:otherwise>
					riac_list += frm.<xsl:value-of select="$_name"/>.value
				</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
				riac_list += frm.<xsl:value-of select="$_name"/>.value
			</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="@arrayparam != ''">
					frm.<xsl:value-of select="@paramname"/>[<xsl:value-of select="@arrayparam"/>].value = riac_list
				</xsl:when>
				<xsl:otherwise>
					frm.<xsl:value-of select="@paramname"/>.value = riac_list
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	<!--#compentency_pickup ##############-->
	<xsl:template match="*[@type= 'competency_pickup']" mode="feed_param">
		<xsl:if test="@paramname">
			<xsl:variable name="_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>
			var cm_list = ''
			var cm_idx = 0
			for (cm_idx = 0; cm_idx &lt; frm.<xsl:value-of select="$_name"/>_box.options.length;cm_idx++){
				cm_list += frm.<xsl:value-of select="$_name"/>_box.options[cm_idx].value + ",1"
				if(cm_idx != (frm.<xsl:value-of select="$_name"/>_box.length -1)){
				cm_list += '~'
				}
			}
			frm.<xsl:value-of select="@paramname"/>.value = cm_list
		</xsl:if>
	</xsl:template>
	<!--#catalog_attachment ###############-->
	<xsl:template match="*[@type= 'catalog_attachment']" mode="feed_param">
		<xsl:if test="@paramname">
			<xsl:variable name="_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>
			//if( frm.<xsl:value-of select="$_name"/>radio[1].checked ) {
				var cata_list_arg = '';
				var cata_list_value = '';
				var cata_list = '';
				var cata_idx = 0;
				for (cata_idx = 0; cata_idx &lt; frm.<xsl:value-of select="$_name"/>_box.options.length;cata_idx++) {
					cata_list += frm.<xsl:value-of select="$_name"/>_box.options[cata_idx].value
					cata_list_value += frm.<xsl:value-of select="$_name"/>_box.options[cata_idx].text
					cata_list_arg += 'CATALOG~%~' + frm.<xsl:value-of select="$_name"/>_box.options[cata_idx].value + '~%~' + frm.<xsl:value-of select="$_name"/>_box.options[cata_idx].text
					if(cata_idx != (frm.<xsl:value-of select="$_name"/>_box.length -1)){
						cata_list += '~'
						cata_list_value += '~'
					}
					cata_list_arg += '~%~'
				}
				frm.<xsl:value-of select="@paramname"/>.value = cata_list
				frm.<xsl:value-of select="@paramname"/>_value.value = cata_list_value
				frm.<xsl:value-of select="$_name"/>arg.value = cata_list_arg
		//	}
		</xsl:if>
	</xsl:template>
	<!--#tcr_pickup ###############-->
	<xsl:template match="*[@type= 'tcr_pickup']" mode="feed_param">
		<xsl:if test="$tcEnabled = 'true'">
			<xsl:if test="@paramname">
				<xsl:variable name="_name"><xsl:call-template name="get_name"/></xsl:variable>
				frm.<xsl:value-of select="@paramname"/>.value = frm.<xsl:value-of select="$_name"/>_box.options[0].value
				frm.<xsl:value-of select="@paramname"/>_value.value = frm.<xsl:value-of select="$_name"/>_box.options[0].text
				frm.<xsl:value-of select="$_name"/>arg.value = 'TrainingCenter~%~' + frm.<xsl:value-of select="$_name"/>_box.options[0].value + '~%~' + frm.<xsl:value-of select="$_name"/>_box.options[0].text
			</xsl:if>
		</xsl:if>
	</xsl:template>
	<!-- #resource node value blocking #########-->
	<xsl:template match="resource" mode="feed_param"/>
	<xsl:template match="*[@type = 'figure_value']" mode="feed_param">
		<xsl:for-each select="figure_type">
			<xsl:variable name="_name">
				<xsl:call-template name="get_name"/>
			</xsl:variable>
			if( frm.<xsl:value-of select="$_name"/>.value != '' ) {
				if( frm.fgt_id_list.value == '' ){
					frm.fgt_id_list.value = <xsl:value-of select="@id"/>
					frm.fig_val_list.value = frm.<xsl:value-of select="$_name"/>.value
				} else {			
					frm.fgt_id_list.value += '~' + <xsl:value-of select="@id"/>
					frm.fig_val_list.value += '~' + frm.<xsl:value-of select="$_name"/>.value
				}
			}
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="*[@type = 'radio' and @name= 'Certificate_of_completion']" mode="validate_js">
		<xsl:variable name="desc_name">
			<xsl:call-template name="get_desc_name"/>
		</xsl:variable>
		<xsl:variable name="_name">
			<xsl:call-template name="get_name"/>
		</xsl:variable>
		<!-- process checking -->
		if( frm.<xsl:value-of select="$_name"/>[1].checked ) {
			//alert("String:"+frm.itm_cfc_id.value)
			frm.itm_cfc_id.value = wbUtilsTrimString(frm.<xsl:value-of select="$_name"/>_box.value);
			if (frm.itm_cfc_id.value == '') {
				alert(wb_msg_usr_please_select_a + ' ' + '<xsl:value-of select="$desc_name"/>' + ' ');
			   	if(!frm.<xsl:value-of select="$_name"/>_box.disabled) {
					frm.<xsl:value-of select="$_name"/>_box.focus()
				}
				return false;
			}
		}
	</xsl:template>
</xsl:stylesheet>
