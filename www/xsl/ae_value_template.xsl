<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="cust/km_obj_label.xsl"/>
	<!-- Valued Template for wizbank 3.5-->
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*"/>
	<!-- globle variable-->
	<xsl:variable name="template_root" select="/applyeasy/template"/>
	<xsl:variable name="detail_root" select="/applyeasy/detail"/>
	<xsl:variable name="parent_detail_root" select="/applyeasy/parent_item/detail"/>
	<xsl:variable name="catalog_node_list_root" select="/applyeasy/catalog/node_list"/>
	<xsl:variable name="targeted_lrn_list_root" select="/applyeasy/targeted_lrn/target_list"/>
	<xsl:variable name="targeted_lrn_num_root" select="/applyeasy/targeted_lrn_num/num"/>
	<xsl:variable name="comp_targeted_lrn_list_root" select="/applyeasy/comp_targeted_lrn/target_list"/>
	<xsl:variable name="item_access_root" select="/applyeasy/item_access"/>
	<xsl:variable name="learning_solution_root" select="/applyeasy/item_type_list"/>
	<xsl:variable name="learning_solution_label" select="/applyeasy/learning_solution/meta/cur_usr/@label"/>
	<xsl:variable name="mote_plan_resourse_list_root" select="/applyeasy/mote_plan/resource_list"/>
	<xsl:variable name="mote_level_list_root" select="/applyeasy/mote_level/mote_level_list"/>
	<xsl:variable name="mote_due_date_root" select="/applyeasy/mote_due_date/date"/>
	<xsl:variable name="mote_cost_target_root" select="/applyeasy/mote_cost_target/cost_target"/>
	<xsl:variable name="mote_time_target_root" select="/applyeasy/mote_time_target/time_target"/>
	<xsl:variable name="mote_target_rating_root" select="/applyeasy/mote_target_rating/rating_target"/>
	<xsl:variable name="link_criteria_root" select="/applyeasy/link_criteria"/>
	<xsl:variable name="competency_root" select="/applyeasy/competency/skill_list"/>
	<xsl:variable name="run_item_access_root" select="/applyeasy/run_item_access"/>
	<xsl:variable name="child_item_access_root" select="/applyeasy/child_item_access"/>
	<xsl:variable name="item_life_status_root" select="/applyeasy/item_life_status/status/@value"/>
	<xsl:variable name="item_cancel_reason_root" select="/applyeasy/item_cancel_reason/reason/@value"/>
	<xsl:variable name="cancelled_item_life_status_root" select="/applyeasy/item_life_status"/>
	<xsl:variable name="cos_eff_start_datetime_root" select="/applyeasy/cos_eff_start_datetime/course/@eff_start_datetime"/>
	<xsl:variable name="cos_eff_end_datetime_root" select="/applyeasy/cos_eff_end_datetime/course/@eff_end_datetime"/>
	<xsl:variable name="item_status_root" select="/applyeasy/item_status/status/@value"/>
	<xsl:variable name="figure_type" select="/applyeasy/figure_type"/>
	<!-- =============================================================== -->
	<!-- Apply root element -->
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- Apply applyeasy element & create valued_template root element-->
	<xsl:template match="applyeasy">
		<valued_template>
			<xsl:apply-templates select="template_view"/>
		</valued_template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="template_view">
		<xsl:apply-templates select="*"/>
	</xsl:template>
	<xsl:template match="hidden | section">
		<xsl:element name="{name()}">
			<xsl:for-each select="@*">
				<xsl:attribute name="{name()}"><xsl:value-of select="."/></xsl:attribute>
			</xsl:for-each>
			<xsl:apply-templates select="*" mode="section"/>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- title element will match this template -->
	<xsl:template match="title" mode="section" priority="10">
		<xsl:copy-of select="."/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="competency" mode="section" priority="10">
		<xsl:element name="{name()}">
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:apply-templates mode="section"/>
			<xsl:copy-of select="$competency_root"/>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="catalog" mode="section" priority="10">
		<xsl:element name="{name()}">
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:apply-templates mode="section"/>
			<xsl:copy-of select="$catalog_node_list_root"/>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="targeted_lrn" mode="section" priority="10">
		<xsl:element name="{name()}">
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:apply-templates mode="section"/>
			<xsl:copy-of select="$targeted_lrn_list_root"/>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="comp_targeted_lrn" mode="section" priority="10">
		<xsl:element name="{name()}">
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:apply-templates mode="section"/>
			<xsl:copy-of select="$comp_targeted_lrn_list_root"/>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->	
	<xsl:template match="targeted_lrn_num" mode="section" priority="10">
		<xsl:element name="{name()}">
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:apply-templates mode="section"/>
			<xsl:copy-of select="$targeted_lrn_num_root"/>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->	
	<xsl:template match="item_status" mode="section" priority="10">
		<xsl:element name="{name()}">
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:variable name="my_name" select="name()"/>
			<xsl:variable name="my_value" select="@value"/>
			<xsl:choose>
				<xsl:when test="name(..) != $my_name">
				<!-- parent element -->
					<xsl:choose>
						<xsl:when test="$item_status_root != ''">
							<xsl:attribute name="value"><xsl:value-of select="$item_status_root"/></xsl:attribute>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
				<!-- child element -->
					<xsl:choose>
						<xsl:when test="$item_status_root != ''">
								<xsl:if test="$item_status_root = $my_value">
									<xsl:attribute name="selected">true</xsl:attribute>
								</xsl:if>
						</xsl:when>
						<xsl:otherwise>
								<xsl:if test="../@value = $my_value"><xsl:attribute name="selected">true</xsl:attribute></xsl:if>
						</xsl:otherwise>
					</xsl:choose>
				
				</xsl:otherwise>
			</xsl:choose>	
			<xsl:apply-templates mode="section"/>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<!--
	<xsl:template match="item_life_status" mode="section" priority="10">
		<xsl:element name="{name()}">
			<xsl:attribute name="value"><xsl:value-of select="$item_life_status_root"/></xsl:attribute>
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>	
			<xsl:apply-templates mode="section"/>
		</xsl:element>
	</xsl:template>
	-->
	<!-- =============================================================== -->
	<!--
	<xsl:template match="item_cancel_reason" mode="section" priority="10">
		<xsl:element name="{name()}">
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:attribute name="value"><xsl:value-of select="$item_cancel_reason_root"/></xsl:attribute>
			<xsl:apply-templates mode="section"/>
		</xsl:element>
	</xsl:template>
	-->
	<!-- =============================================================== -->
	<xsl:template match="item_access" mode="section" priority="10">
		<xsl:variable name="my_id" select="@id"/>
		<xsl:element name="{name()}">
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:if test="@arrayparam = 'true'">
				<xsl:attribute name="arrayparam"><xsl:call-template name="get_position"><xsl:with-param name="my_id"><xsl:value-of select="$my_id"/></xsl:with-param></xsl:call-template></xsl:attribute>
			</xsl:if>
			<xsl:apply-templates mode="section"/>
			<xsl:apply-templates select="$item_access_root" mode="item_access">
				<xsl:with-param name="my_id"><xsl:value-of select="$my_id"/></xsl:with-param>
			</xsl:apply-templates>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->	
	<xsl:template name="get_position">
		<xsl:param name="my_id"/>
		<xsl:for-each select="//item_access">
			<xsl:if test="@id = $my_id">
				<xsl:value-of select="position() - 1"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="run_item_access" mode="section" priority="10">
		<xsl:variable name="my_id" select="@id"/>
		<xsl:element name="{name()}">
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:if test="@arrayparam = 'true'">
				<xsl:attribute name="arrayparam"><xsl:call-template name="run_get_position"><xsl:with-param name="my_id"><xsl:value-of select="$my_id"/></xsl:with-param></xsl:call-template></xsl:attribute>
			</xsl:if>
			<xsl:apply-templates mode="section"/>
			<xsl:apply-templates select="$run_item_access_root" mode="run_item_access">
				<xsl:with-param name="my_id"><xsl:value-of select="$my_id"/></xsl:with-param>
			</xsl:apply-templates>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->	
	<xsl:template match="child_item_access" mode="section" priority="10">
		<xsl:variable name="my_id" select="@id"/>
		<xsl:element name="{name()}">
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:if test="@arrayparam = 'true'">
				<xsl:attribute name="arrayparam"><xsl:call-template name="child_get_position"><xsl:with-param name="my_id"><xsl:value-of select="$my_id"/></xsl:with-param></xsl:call-template></xsl:attribute>
			</xsl:if>
			<xsl:apply-templates mode="section"/>
			<xsl:apply-templates select="$child_item_access_root" mode="run_item_access">
				<xsl:with-param name="my_id"><xsl:value-of select="$my_id"/></xsl:with-param>
			</xsl:apply-templates>
		</xsl:element>
	</xsl:template>	
	<!-- =============================================================== -->	
	<xsl:template name="run_get_position">
		<xsl:param name="my_id"/>
		<xsl:for-each select="//run_item_access">
			<xsl:if test="@id = $my_id">
				<xsl:value-of select="position() - 1"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="child_get_position">
		<xsl:param name="my_id"/>
		<xsl:for-each select="//child_item_access">
			<xsl:if test="@id = $my_id">
				<xsl:value-of select="position() - 1"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>	
	<!-- =============================================================== -->
	<xsl:template match="mote_plan" mode="section" priority="10">
		<xsl:element name="{name()}">
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:apply-templates mode="section"/>
			<xsl:copy-of select="$mote_plan_resourse_list_root"/>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="mote_level" mode="section" priority="10">
		<xsl:element name="{name()}">
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:apply-templates mode="section"/>
			<xsl:copy-of select="$mote_level_list_root"/>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="mote_due_date" mode="section" priority="10">
		<xsl:element name="{name()}">
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:apply-templates mode="section"/>
			<xsl:copy-of select="$mote_due_date_root"/>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="mote_target_rating" mode="section" priority="10">
		<xsl:element name="{name()}">
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:apply-templates mode="section"/>
			<xsl:copy-of select="$mote_target_rating_root"/>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="cancelled_item_life_status" mode="section" priority="10">
		<xsl:element name="{name()}">
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:apply-templates mode="section" select="title"/>
			<xsl:for-each select="subfield_list">
				<xsl:element name="subfield_list">
					<xsl:apply-templates mode="section"/>
				</xsl:element>
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="mote_cost_target" mode="section" priority="10">
		<xsl:element name="{name()}">
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:apply-templates mode="section"/>
			<xsl:if test="$mote_cost_target_root/@value != ''"><xsl:attribute name="value"><xsl:value-of select="$mote_cost_target_root/@value"/></xsl:attribute></xsl:if>
			<xsl:copy-of select="$mote_cost_target_root"/>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="figure_type_list" mode="section" priority="10">
		<xsl:variable name="figure_count"><xsl:apply-templates select="figure_type[1]" mode="check_exist"/></xsl:variable>
		<xsl:if test="$figure_count &gt; 0">
			<xsl:element name="{name()}">
				<xsl:attribute name="type"><xsl:value-of select="@type"/></xsl:attribute>
				<xsl:attribute name="pos"><xsl:value-of select="@pos"/></xsl:attribute>
				<xsl:copy-of select="title"/>
				<xsl:apply-templates select="figure_type" mode="section"/>
			</xsl:element>
		</xsl:if>
	</xsl:template>

	<xsl:template match="figure_type" mode="check_exist">
		<xsl:variable name="id" select="@id"/>
		<xsl:variable name="figure_count" select="count($figure_type[@id = $id])"/>
		<xsl:if test="$figure_count = 0">
			<xsl:apply-templates select="following-sibling::figure_type" mode="check_exist"/>
		</xsl:if>
		<xsl:value-of select="$figure_count"/>
	</xsl:template>

	<xsl:template match="figure_type" mode="section" priority="10">
		<xsl:variable name="id" select="@id"/>
		<xsl:if test="$figure_type[@id = $id]">
		<xsl:element name="{name()}">
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:apply-templates mode="section"/>
			<xsl:if test="$figure_type[@id = $id]/num">
				<xsl:copy-of select="$figure_type[@id = $id]/num"/>
			</xsl:if>
			<xsl:copy-of select="$figure_type[@id = $id]/title"/>
		</xsl:element>
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="mote_time_target" mode="section" priority="10">
		<xsl:element name="{name()}">
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:apply-templates mode="section"/>
			<xsl:if test="$mote_cost_target_root/@value != ''"><xsl:attribute name="value"><xsl:value-of select="$mote_time_target_root/@value"/></xsl:attribute></xsl:if>
			<xsl:copy-of select="$mote_time_target_root"/>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="cos_eff_start_datetime" mode="section" priority="10">
		<xsl:element name="{name()}">
			<xsl:attribute name="value"><xsl:value-of select="$cos_eff_start_datetime_root"/></xsl:attribute>
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			
			<xsl:apply-templates mode="section"/>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="cos_eff_end_datetime" mode="section" priority="10">
		<xsl:element name="{name()}">
			<xsl:attribute name="value"><xsl:value-of select="$cos_eff_end_datetime_root"/></xsl:attribute>
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:apply-templates mode="section"/>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="link_list" mode="field">
		<xsl:variable name="hasCriteria"><xsl:apply-templates select="link/criteria/*" mode="hascriteria"/></xsl:variable>
		<xsl:variable name="hasLink"><xsl:for-each select="link"><xsl:if test="not(criteria)">1</xsl:if></xsl:for-each></xsl:variable>
		<xsl:if test="($hasCriteria != '') or ($hasLink != '')">
			<xsl:element name="link_list">
				<xsl:apply-templates mode="criteria"/>
			</xsl:element>
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->	
	<xsl:template match="link_list" mode="section" priority="10">
		<xsl:variable name="hasCriteria"><xsl:apply-templates select="link/criteria/*" mode="hascriteria"/></xsl:variable>
		<xsl:variable name="hasLink"><xsl:for-each select="link"><xsl:if test="not(criteria)">1</xsl:if></xsl:for-each></xsl:variable>
		<xsl:if test="($hasCriteria != '') or ($hasLink != '')">
			<xsl:element name="link_list">
				<xsl:apply-templates mode="criteria"/>
			</xsl:element>
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->	
	<xsl:template match="*" mode="criteria">
		<xsl:choose>
			<xsl:when test="criteria">
				<xsl:choose>
					<xsl:when test="criteria/@type = 'and'">
						<xsl:variable name="no_of_criteria_to_be_match"><xsl:value-of select="count(criteria/*)"/></xsl:variable>
						<!--Create a String of each criteria match-->
						<xsl:variable name="string_of_criteria_matched"><xsl:for-each select="criteria/*"><xsl:variable name="my_name" select="name()"/><xsl:variable name="my_val" select="@value"/><xsl:variable name="match_val"><xsl:value-of select="count($link_criteria_root/*[name() = $my_name][@value = $my_val])"/></xsl:variable><xsl:if test="$match_val != 0"><xsl:value-of select="$match_val"/></xsl:if></xsl:for-each></xsl:variable>
						<!-- compare the length of the matched string with total no of criteria-->
						<!-- and show the criteria if equal(all match)-->
						<xsl:if test="string-length($string_of_criteria_matched) = $no_of_criteria_to_be_match">
							<xsl:element name="{name()}">
								<xsl:for-each select="@*">
									<xsl:copy-of select="."/>
								</xsl:for-each>
							</xsl:element>
						</xsl:if>
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="my_name" select="name(criteria/*)"/>
						<xsl:variable name="my_val" select="criteria/*/@value"/>
						<xsl:variable name="match_val"><xsl:value-of select="count($link_criteria_root/*[name() = $my_name][@value = $my_val])"/></xsl:variable>
						<xsl:if test="$match_val != 0">
							<xsl:element name="{name()}">
								<xsl:for-each select="@*">
									<xsl:copy-of select="."/>
								</xsl:for-each>
							</xsl:element>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->	
	<xsl:template match="*" mode="hascriteria">
		<xsl:variable name="my_name" select="name()"/>
		<xsl:value-of select="count($link_criteria_root/*[name() = $my_name])"/>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- other's element will match this template -->
	<xsl:template match="*" mode="section" priority="1">
		<xsl:variable name="my_name" select="name()"/>
		<xsl:element name="{name()}">
			<xsl:choose>
				<xsl:when test="@external_field = 'yes'">
								<xsl:for-each select="@*">
									<xsl:copy-of select="."/>
								</xsl:for-each>				
					<!-- External Generic Field-->
					<!--=================-->
					<!-- write subfield node and title node-->
					<xsl:apply-templates mode="field">
						<xsl:with-param name="field_name"><xsl:value-of select="name()"/></xsl:with-param>
					</xsl:apply-templates>
					<xsl:copy-of select="/applyeasy/*[name() = $my_name]/*"/>
					<!--=================-->
				</xsl:when>
				<xsl:otherwise>
					<!-- Normal Field =======-->
					<!--=================-->
					<!-- write view attribute-->
					<xsl:variable name="value"><xsl:for-each select="attribute::*"><xsl:if test="name() = 'value'"><xsl:value-of select="."/></xsl:if></xsl:for-each></xsl:variable>
					<xsl:for-each select="@*">
						<xsl:attribute name="{name()}"><xsl:value-of select="."/></xsl:attribute>
					</xsl:for-each>
					<xsl:variable name="tpl_field_name" select="$template_root/*[name() = $my_name]"/>
					<xsl:variable name="detail_field_name" select="$detail_root/*[name() = $my_name]"/>
					<xsl:variable name="parent_detail_field_name" select="$parent_detail_root/*[name() = $my_name]"/>
					<!-- write template attribute -->
					<xsl:apply-templates select="$tpl_field_name" mode="write_attribute"/>
					<!-- write detail attribute , if no subfield inside -->
					<xsl:apply-templates select="$detail_field_name" mode="write_detail_value"/>
					<xsl:variable name="my_inherit" select="$template_root/*[name() = $my_name]/@inheritence"/>
					<xsl:if test="$my_inherit = 'true'">
						<xsl:apply-templates select="$parent_detail_field_name" mode="write_detail_value"/>
					</xsl:if>
					<xsl:if test="$my_inherit and $my_inherit != 'true' and $my_inherit != 'false'">
						<xsl:apply-templates select="$parent_detail_root/*[name() = $my_inherit]" mode="write_detail_value"/>
					</xsl:if>
					<!-- write subfield node and title node-->
					<xsl:apply-templates mode="field">
						<xsl:with-param name="field_name"><xsl:value-of select="name()"/></xsl:with-param>
					</xsl:apply-templates>
					<!-- check for ext_value_lable_tag attribute-->
					<xsl:variable name="ext_value_label_tag"><xsl:value-of select="@ext_value_label_tag"/></xsl:variable>
					<xsl:choose>
						<xsl:when test="@ext_value_label_tag and not(/applyeasy/*[name()=$ext_value_label_tag]/*/desc)">
							<xsl:call-template name="obj_type">
								<xsl:with-param name="ext_value_label"><xsl:value-of select="@ext_value_label_tag"/></xsl:with-param>
								<xsl:with-param name="field_name"><xsl:value-of select="name()"/></xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="@ext_value_label_tag">
							<xsl:call-template name="item_type">
								<xsl:with-param name="ext_value_label"><xsl:value-of select="@ext_value_label_tag"/></xsl:with-param>
								<xsl:with-param name="value"><xsl:value-of select="$value"/></xsl:with-param>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>					
					<!-- DENNIS -->
					<!-- check for option_src -->
					<xsl:call-template name="write_option_src"/>
					<xsl:apply-templates select="$detail_root/*[name() = $my_name]/resource_list" mode="resource"/>
					<!--=================-->
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="write_option_src">
		<xsl:variable name="field_name" select="name()"/>
		<!--<xsl:text>enter write_option_src</xsl:text>-->
		<xsl:if test="$template_root/*[name() = $field_name]/@option_src_type">
			<xsl:variable name="option_src_type" select="$template_root/*[name() = $field_name]/@option_src_type"/>
			<xsl:variable name="option_src" select="$template_root/*[name() = $field_name]/@option_src"/>
		<!--<xsl:text>has option src</xsl:text>-->
			<xsl:choose>
				<xsl:when test="$option_src_type='code_table'">
					<xsl:call-template name="write_option_src_code_table">
						<xsl:with-param name="option_src">
							<xsl:value-of select="$option_src"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="write_option_src_code_table">
		<xsl:param name="option_src"/>
		<xsl:variable name="field_name" select="name()"/>
		<xsl:variable name="option_src_root" select="/applyeasy/*[name() = $option_src]"/>
		<xsl:for-each select="$option_src_root/title/ctb">
			<xsl:element name="{$field_name}">	
				<xsl:attribute name="id"><xsl:value-of select="position()"/></xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="@id"/></xsl:attribute>
				<xsl:element name="title">
					<xsl:copy-of select="desc"/>
				</xsl:element>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="resource">
		<xsl:copy-of select="."/>
	</xsl:template>
	<!-- =============================================================== -->	
	<xsl:template match="*" mode="write_attribute">
		<xsl:for-each select="@*">
			<xsl:copy-of select="."/>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="write_detail_value">
		<xsl:choose>
			<xsl:when test="child::*"/>
			<xsl:otherwise>
				<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- match node equal to title-->
	<xsl:template match="title" mode="field">
		<xsl:copy-of select="."/>
	</xsl:template>
	<xsl:template match="resource_list" mode="field" priority="10"/>
	<!-- =============================================================== -->
	<!-- match all the node except title node -->
	<xsl:template match="*" mode="field">
		<xsl:param name="field_name"/>
		<xsl:param name="subfield_list"/>
		<!-- create subfield view element -->
		<xsl:variable name="my_id" select="@id"/>
		<xsl:variable name="my_name" select="name()"/>
		<xsl:element name="{name()}">
			<!-- add subfield view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:for-each select="$template_root/*[name() = $my_name]/*[@id = $my_id]/@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:if test="$detail_root/*[name() = $my_name]/*[@id = $my_id]">
				<xsl:attribute name="selected">true</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="$detail_root/*[name() = $my_name]/*[@id = $my_id]/text()"/></xsl:attribute>
			</xsl:if>
			<!-- process subfield child-->
			<xsl:apply-templates mode="subfield">
				<xsl:with-param name="field_name"><xsl:value-of select="$field_name"/></xsl:with-param>
				<xsl:with-param name="subfield_list"><xsl:value-of select="name()"/></xsl:with-param>
			</xsl:apply-templates>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="subfield">
		<xsl:param name="field_name"/>
		<xsl:param name="subfield_list"/>
		<xsl:element name="{name()}">
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:apply-templates mode="subfield">
				<xsl:with-param name="field_name"><xsl:value-of select="$field_name"/></xsl:with-param>
				<xsl:with-param name="subfield_list"><xsl:value-of select="$subfield_list"/></xsl:with-param>
			</xsl:apply-templates>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="subfield_list/*" mode="subfield">
		<xsl:param name="subfield_list"/>
		<xsl:param name="field_name"/>
		<xsl:variable name="my_id" select="@id"/>
		<xsl:element name="{name()}">
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<!-- apply template attributes-->
			<xsl:apply-templates select="$template_root/*[name() = $field_name]/*[name() = $subfield_list]/*[@id = $my_id]" mode="write_attribute"/>
			<xsl:apply-templates select="$detail_root/*[name() = $field_name]/*[name() = $subfield_list]/*[@id = $my_id]" mode="write_subfield_detail_value"/>
			<xsl:if test="$template_root/*[name() = $field_name]/*[name() = $subfield_list]/*[@id = $my_id]/@inheritance = 'true'">
				<xsl:apply-templates select="$parent_detail_root/*[name() = $field_name]/*[name() = $subfield_list]/*[@id = $my_id]" mode="write_subfield_detail_value"/>
			</xsl:if>
			<!-- recursive-->
			<xsl:apply-templates mode="subfield">
				<xsl:with-param name="field_name"><xsl:value-of select="$field_name"/></xsl:with-param>
				<xsl:with-param name="subfield_list"><xsl:value-of select="$subfield_list"/></xsl:with-param>
			</xsl:apply-templates>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="write_subfield_detail_value">
		<xsl:choose>
			<xsl:when test="child::*">
				<xsl:copy-of select="child::*"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="item_access">
		<xsl:param name="my_id"/>
		<xsl:apply-templates mode="item_access">
			<xsl:with-param name="my_id"><xsl:value-of select="$my_id"/></xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="role" mode="item_access">
		<xsl:copy-of select="."/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="role_list" mode="item_access">
		<xsl:param name="my_id"/>
		<xsl:apply-templates select="*[substring-before(@id,'_') = $my_id]" mode="item_access"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="assigned_role_list" mode="item_access">
		<xsl:param name="my_id"/>
		<xsl:element name="{name()}">
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:apply-templates select="*[substring-before(@id,'_') = $my_id]" mode="item_access"/>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="run_item_access">
		<xsl:param name="my_id"/>
		<xsl:apply-templates mode="run_item_access">
			<xsl:with-param name="my_id"><xsl:value-of select="$my_id"/></xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="role" mode="run_item_access">
		<xsl:copy-of select="."/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="role_list" mode="run_item_access">
		<xsl:param name="my_id"/>
		<xsl:apply-templates select="*[@id = $my_id]" mode="run_item_access"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="assigned_role_list" mode="run_item_access">
		<xsl:param name="my_id"/>
		<xsl:element name="{name()}">
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:apply-templates select="*[@id = $my_id]" mode="run_item_access"/>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="item_type">
		<xsl:param name="ext_value_label"/>
		<xsl:param name="value"/>
		<xsl:copy-of select="/applyeasy/*[name() = $ext_value_label]/*"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="item_type">
		<xsl:param name="value"/>
		<xsl:param name="label"/>
		<xsl:attribute name="ext_valuelabel"><xsl:value-of select="*/*[@id = $value]/*[@lan = $label]/@name"/></xsl:attribute>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="cost_center" mode="section" priority="10">
		<xsl:element name="{name()}">
			<!-- write view attribute-->
			<xsl:for-each select="@*">
				<xsl:copy-of select="."/>
			</xsl:for-each>
			<xsl:apply-templates mode="section"/>
			<xsl:copy-of select="/applyeasy/cost_center/target_list"/>
		</xsl:element>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="obj_type">
		<xsl:param name="ext_value_label"/>
		<xsl:param name="field_name"/>
		<xsl:for-each select="/applyeasy/*[name() = $ext_value_label]/*">
			<xsl:element name="{$field_name}">
				<xsl:attribute name="value"><xsl:value-of select="text()"/></xsl:attribute>
				<xsl:element name="title">
					<xsl:call-template name="obj_label">
						<xsl:with-param name="site_id"><xsl:value-of select="/applyeasy/site/@id"/></xsl:with-param>
						<xsl:with-param name="label"><xsl:value-of select="text()"/></xsl:with-param>
					</xsl:call-template>
				</xsl:element>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->	
</xsl:stylesheet>
