<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/trun_date.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<!-- cust utils -->
	<xsl:output indent="yes" />
	<xsl:variable name="instr" select="/instructor/instructor" />
	<xsl:variable name="mod_cnt" select="count(instructor/item)" />
	<xsl:variable name="ent_id" select="$instr/iti_ent_id"></xsl:variable>

	<xsl:variable name="page_size" select="/instructor/pagination/@page_size" />
	<xsl:variable name="cur_page" select="/instructor/pagination/@cur_page" />
	<xsl:variable name="total" select="/instructor/pagination/@total_rec" />
	<xsl:variable name="timestamp" select="/instructor/pagination/@timestamp" />

	<xsl:variable name="lab_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '962')" />
	<xsl:variable name="lab_type" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '975')" />
	<xsl:variable name="lab_targer" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '976')" />
	<xsl:variable name="lab_eff_datetime" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '977')" />
	<xsl:variable name="lab_handle" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '978')" />
	<xsl:variable name="lab_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '960')" />
	<xsl:variable name="lab_no_result" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '974')" />
	<xsl:variable name="lab_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '979')" />
	
	<xsl:variable name="lab_1070" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1070')" />
	<xsl:variable name="lab_1071" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1071')" />
	<!-- ================================================================ -->
	<xsl:template match="/">
		<xsl:apply-templates select="instructor"/>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="instructor">
		<html>
			<head>
				<title>
					<xsl:value-of select="$wb_wizbank" />
				</title>
				<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}" />
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js" />
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js" />
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js" />
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_instructor.js" />
				<script language="Javascript"><![CDATA[
				var insc = new wbInstructor;
			]]></script>
			</head>
			<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0">
				<form name="frmXml">
					<xsl:call-template name="content" />
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="content">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_INT_INSTRUCTOR_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_title" />
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<a href="javascript: insc.list('{$instr/iti_type_mark}');" class="NavLink">
					<xsl:choose>
						<xsl:when test="$instr/iti_type_mark = 'IN'">
							<xsl:value-of select="$lab_1070" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_1071" />
						</xsl:otherwise>
					</xsl:choose>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<a href="javascript: insc.get('{$instr/iti_ent_id}', '{$instr/iti_type_mark}');" class="NavLink">
					<xsl:value-of select="$instr/iti_name" />
				</a>
				<span class="NavLink">
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:value-of select="$lab_title" />
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="$mod_cnt='0'">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_result" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td width="25%">
							<xsl:value-of select="$lab_name" />
						</td>
						<td width="15%" align="center">
							<xsl:value-of select="$lab_type" />
						</td>
						<td width="15%" align="center">
							<xsl:value-of select="$lab_targer" />
						</td>
						<td width="15%" align="center">
							<xsl:value-of select="$lab_eff_datetime" />
						</td>
						<td  width="15%" align="center">
							<xsl:value-of select="$lab_score" />
						</td>
						<td width="15%" align="right">
							<xsl:value-of select="$lab_handle" />
						</td>
					</tr>
					<xsl:apply-templates select="item" />
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page" />
					<xsl:with-param name="page_size" select="$page_size" />
					<xsl:with-param name="timestamp" select="$timestamp" />
					<xsl:with-param name="total" select="$total" />
					<xsl:with-param name="width">
						<xsl:value-of select="$wb_gen_table_width" />
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="item">
		<xsl:variable name="ins_comment_itm_id">
			<xsl:choose>
				<xsl:when test="child_item/@id >0">
					<xsl:value-of select="child_item/@id" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="@id" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr>
			<td>
				<xsl:value-of select="title/text()" />
				<xsl:if test="child_item">
					<xsl:text>&#160;-&#160;</xsl:text>
					<xsl:value-of select="child_item/title/text()" />
				</xsl:if>
			</td>
			<td align="center">
				<xsl:call-template name="get_ity_title">
					<xsl:with-param name="itm_type" select="@dummy_type" />
				</xsl:call-template>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test="target !=''">
						<xsl:value-of select="target" />
					</xsl:when>
					<xsl:otherwise><xsl:text>--</xsl:text></xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:variable name="end_date">
					<xsl:choose>
						<xsl:when test="child_item">
							<xsl:value-of select="child_item/eff_end_datetime" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="eff_end_datetime" />
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="$end_date != ''">
						<xsl:call-template name="trun_date">
							<xsl:with-param name="my_timestamp">
								<xsl:value-of select="$end_date" />
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>--</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test="score >0">
						<xsl:value-of select="score" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>--</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="right">
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="class">btn wzb-btn-blue</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_name">
						<xsl:value-of select="$lab_handle" />
					</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">Javascript:insc.comment.get_ins_comment_view('<xsl:value-of select="$ins_comment_itm_id" />', '<xsl:value-of select="$ent_id" />')</xsl:with-param>
				</xsl:call-template>
			</td>
		</tr>
	</xsl:template>
	<!-- ================================================================ -->
</xsl:stylesheet>
