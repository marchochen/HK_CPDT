<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- ====================================================================================================== -->
	<xsl:variable name="lab_863" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '863')"/><!-- Specifty the courses/exams that this Integrated Learning consists of  -->
	<xsl:variable name="lab_864" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '864')"/><!-- Overall Completion Criteria  -->
	<xsl:variable name="lab_865" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '865')"/>
	<xsl:variable name="lab_866" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '866')"/>
	<xsl:variable name="lab_867" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '867')"/>
	<xsl:variable name="lab_868" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '868')"/><!-- Elective Requirements -->
	<xsl:variable name="lab_869" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '869')"/><!-- Compulsory Requirements  -->
	<xsl:variable name="lab_870" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '870')"/><!-- Courses/Exams -->
	<xsl:variable name="lab_879" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '879')"/><!-- courses/exams -->
	<xsl:variable name="lab_871" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '871')"/><!-- Requirements -->
	<xsl:variable name="lab_872" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '872')"/><!-- Complete -->
	<xsl:variable name="lab_873" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '873')"/><!-- 修改 -->
	<xsl:variable name="lab_874" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1185')"/><!-- courses -->
	<xsl:variable name="lab_657" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '657')"/><!-- 内容为空 -->
	<xsl:variable name="lab_705" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '705')"/><!-- 修改者 -->
	<xsl:variable name="lab_706" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '706')"/><!-- 修改时间 -->
	<xsl:variable name="lab_473" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '473')"/><!-- 添加 -->
	<xsl:variable name="lab_257" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '257')"/><!-- 删除 -->
	<xsl:variable name="lab_329" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '329')"/><!-- OK -->
	<xsl:variable name="lab_330" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/><!-- Cancel -->
	<xsl:variable name="itm_id" select="integrated_learning/criteria/@itm_id"/>
	<xsl:variable name="icc_id" select="integrated_learning/criteria/@icc_id"/>
	<!-- ====================================================================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="integrated_learning"/>
		</html>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template match="integrated_learning">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			itm_lst = new wbItem;
			]]></script>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frmXml">
				<input type="hidden" name="module"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="icd_id"/>
				<input type="hidden" name="itm_id" value="{$itm_id}"/>
				<input type="hidden" name="icc_update_timestamp" value="{criteria/@icc_update_timestamp}"/>
				<xsl:call-template name="content"/>
			</form>
		</body>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template name="content">
		<xsl:call-template name="itm_action_nav">
	    	<xsl:with-param name="view_mode">simple</xsl:with-param>
			<xsl:with-param name="is_add">false</xsl:with-param>
			<xsl:with-param  name="cur_node_id">06</xsl:with-param>
		</xsl:call-template>
	<div class="wzb-item-main">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_863"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
					<xsl:value-of select="criteria/@itm_title"/>
				</a>
				<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_874"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_864"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table class="Bg" cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td width="8" colspan="2">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
			<tr>
				<td width="5%" align="right">
					<img src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/>
				</td>
				<td height="8" width="75%">
					<span class="Text"><xsl:value-of select="$lab_865"/></span>
				</td>
			</tr>
			<tr>
				<td width="5%" align="right">
					<b><xsl:value-of select="$lab_866"/></b>
				</td>
				<td height="8" width="75%">
					<span class="Text">
						<xsl:value-of select="$lab_867"/>
						<xsl:text> </xsl:text>
						<input name="icc_completed_elective_count" type="text" class="wzb-inputText" style="width:20px " value="{criteria/@completed_elective_count}"/>
						<xsl:text> </xsl:text>
						<xsl:value-of select="$lab_868"/>
						<input type="hidden" name="lab_868" value="{$lab_868}"/>
					</span>
				</td>
			</tr>
			<tr>
				<td width="8" colspan="2">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_space"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_869"/>
			</xsl:with-param>
			<xsl:with-param name="extra_td">
				<td align="right">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_473"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.set_condition_prev('COMPULSORY',<xsl:value-of select="$itm_id"/>,<xsl:value-of select="$icc_id"/>);</xsl:with-param>
					    <xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
					</xsl:call-template>		
				</td>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="count(criteria/icd_lst/icd[@type='COMPULSORY'])>0">
				<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
					<xsl:call-template name="condition_header"/>
					<xsl:apply-templates select="criteria/icd_lst/icd[@type='COMPULSORY']"/>
				</table>	
			<!-- 	<xsl:call-template name="wb_ui_line"/> -->
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_657"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_line"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:call-template name="wb_ui_space"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_868"/>
			</xsl:with-param>
			<xsl:with-param name="extra_td">
				<td align="right">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_473"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.set_condition_prev('ELECTIVE',<xsl:value-of select="$itm_id"/>,<xsl:value-of select="$icc_id"/>);</xsl:with-param>
					    <xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
					</xsl:call-template>		
				</td>
			</xsl:with-param>
		</xsl:call-template>
		<!-- this hidden only used in js check -->
		<input type="hidden" name="elective_total_cnt" value="{count(criteria/icd_lst/icd[@type='ELECTIVE'])}"/>
		<xsl:choose>
			<xsl:when test="count(criteria/icd_lst/icd[@type='ELECTIVE'])>0">
				<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
					<xsl:call-template name="condition_header"/>
					<xsl:apply-templates select="criteria/icd_lst/icd[@type='ELECTIVE']"/>
				</table>	
				<!-- <xsl:call-template name="wb_ui_line"/> -->
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_657"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_line"/>
			</xsl:otherwise>
		</xsl:choose>
		 <!-- <xsl:call-template name="wb_ui_line"/> -->
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td align="center">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_329"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.set_criteria(frmXml);</xsl:with-param>
					</xsl:call-template>
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_330"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.get_item_detail(<xsl:value-of select="$itm_id"/>);</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		</div>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template name="condition_header">
		<tr class="SecBg">
			<td width="8">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<!-- Course/Exam -->
			<td align="left" class="TitleText" width="35%" style="color:#999999;">
				<xsl:value-of select="$lab_870"/>
			</td>
			<!-- Requirements -->
			<td align="left" class="TitleText" width="20%" style="color:#999999;">
				<xsl:value-of select="$lab_871"/>
			</td>
			<!-- 修改时间 -->
			<td align="left"  class="TitleText" width="15%" style="color:#999999;">
				<xsl:value-of select="$lab_706"/>
			</td>
			<!-- 修改者 -->
			<td align="left" class="TitleText" width="15%" style="color:#999999;">
				<xsl:value-of select="$lab_705"/>
			</td>
			<!-- button -->
			<td align="left"  class="TitleText" width="15%">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
		</tr>
		<tr>
		  <td colspan="10"><xsl:call-template name="wb_ui_line"/></td>
		</tr>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template match="icd">
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2">RowsEven</xsl:when>
				<xsl:otherwise>RowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr class="{$row_class}">
			<td width="8">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<!-- Course/Exam -->
			<td align="left" class="Text">
				<xsl:for-each select="item">
					<xsl:text>(</xsl:text>
					<xsl:value-of select="@code"/>
					<xsl:text>)</xsl:text>
					<xsl:value-of select="@title"/>
					<xsl:if test="position() != last()">
						<br/>
					</xsl:if>
				</xsl:for-each>
			</td>
			<!-- Requirements -->
			<td align="left" class="Text">
				<xsl:value-of select="$lab_872"/>
				<xsl:text> </xsl:text>
				<b><xsl:value-of select="@completed_item_count"/></b>
				<xsl:text> </xsl:text>
				<xsl:value-of select="$lab_879"/>
			</td>
			<!-- 修改时间 -->
			<td align="left"  class="Text">
				<xsl:call-template name="display_time">
					<xsl:with-param name="my_timestamp">
						<xsl:value-of select="@update_timestamp"/>
					</xsl:with-param>
				</xsl:call-template>
			</td>
			<!-- 修改者 -->
			<td align="left" class="Text">
				<xsl:value-of select="@update_usr_id"/>
			</td>
			<!-- button -->
			<td align="right"  class="Text">
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name">
						<xsl:value-of select="$lab_873"/>
					</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.set_condition_prev('<xsl:value-of select="@type"/>',<xsl:value-of select="$itm_id"/>,<xsl:value-of select="$icc_id"/>, <xsl:value-of select="@id"/>);</xsl:with-param>
				</xsl:call-template>		
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name">
						<xsl:value-of select="$lab_257"/>
					</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.del_condition(frmXml, <xsl:value-of select="@id"/>);</xsl:with-param>
				</xsl:call-template>		
			</td>
		</tr>
		<tr>
		  <td colspan="10"><xsl:call-template name="wb_ui_line"/></td>
		</tr>
	</xsl:template>
	<!-- ====================================================================================================== -->
</xsl:stylesheet>
