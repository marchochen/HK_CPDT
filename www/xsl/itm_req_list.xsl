<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/> 
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<!-- cust utils -->
	<!-- ================================================================= -->
	<xsl:output indent="yes"/>
	<xsl:variable name="req_set" select="/applyeasy/item/requirement_set"/>
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="is_class">
		<xsl:choose>
			<xsl:when test="/applyeasy/item/@create_run_ind='false' and /applyeasy/item/@itm_run_ind='true'">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- ================================================================ -->
	<xsl:template match="/">
		<xsl:call-template name="item_req"/>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="item_req">
		<html>
			<xsl:call-template name="draw_header"/>
			<xsl:call-template name="draw_body"/>
		</html>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_header">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_itm_req.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript"><![CDATA[
			itmReq = new wbItemReq;
			itm_lst = new wbItem;
		]]></script>
		</head>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_body">
		<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0">
			<form name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
	        <xsl:with-param name="lab_desc">爲了可以報讀此課程，學員必須通過在這裡設置的每一個先修條件。每一個先修條件包含一門或者多門課程，要通過一個先修條件，學員最少必須完成該先修條件裡面的一門課程。</xsl:with-param>
			<xsl:with-param name="lab_row_title">先修條件</xsl:with-param>
			<xsl:with-param name="lab_add_req">新增</xsl:with-param>
			<xsl:with-param name="lab_edit_req">修改</xsl:with-param>
			<xsl:with-param name="lab_or">或</xsl:with-param>
			<xsl:with-param name="lab_del_req">刪除</xsl:with-param>
			<xsl:with-param name="lab_no_prerequisite">沒有先修條件.</xsl:with-param>
			<xsl:with-param name="lab_prerequisite">先修課程設置</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>訊息</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
		    <xsl:with-param name="lab_desc">为了可以报读此课程，学员必须通过在这里设置的每一个先修条件。每一个先修条件包含一门或者多门课程，要通过一个先修条件，学员最少必须完成该先修条件里面的一门课程。</xsl:with-param>
			<xsl:with-param name="lab_row_title">先修条件</xsl:with-param>
			<xsl:with-param name="lab_add_req">添加</xsl:with-param>
			<xsl:with-param name="lab_edit_req">修改</xsl:with-param>
			<xsl:with-param name="lab_or">或</xsl:with-param>
			<xsl:with-param name="lab_del_req">删除</xsl:with-param>
			<xsl:with-param name="lab_no_prerequisite">没有先修条件.</xsl:with-param>
			<xsl:with-param name="lab_prerequisite">先修课程设置</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
		    <xsl:with-param name="lab_desc">In order to apply this learning solution, a learner must fulfill every prerequisite set here. Each prerequisite is composed of one or more learning solutions. To fulfill one prerequisite, a learner must complete at least one of the learning solutions prescribed.</xsl:with-param>
			<xsl:with-param name="lab_row_title">Prerequisite</xsl:with-param>
			<xsl:with-param name="lab_add_req">Add</xsl:with-param>
			<xsl:with-param name="lab_edit_req">Edit</xsl:with-param>
			<xsl:with-param name="lab_or">or</xsl:with-param>
			<xsl:with-param name="lab_del_req">Remove</xsl:with-param>
			<xsl:with-param name="lab_no_prerequisite">No prerequisite found.</xsl:with-param>
			<xsl:with-param name="lab_prerequisite">Prerequisite course</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="content">
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_row_title"/>
		<xsl:param name="lab_add_req"/>
		<xsl:param name="lab_edit_req"/>
		<xsl:param name="lab_or"/>
		<xsl:param name="lab_del_req"/>
		<xsl:param name="lab_no_prerequisite"/>
		<xsl:param name="lab_prerequisite"/>
		<xsl:param name="lab_run_info"/>
		<xsl:variable name="itrOrder">
		   <xsl:value-of select="count($req_set/requirement) + 1"/>
	    </xsl:variable>
	    <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">105</xsl:with-param>
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
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_desc"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="//itm_action_nav/hasTeachingCourse/text()='true' ">
						<a href="javascript:itm_lst.get_itm_instr_view({//itm_action_nav/@itm_id})" class="NavLink">
							<xsl:value-of select="//itm_action_nav/@itm_title"/>
						</a>
					</xsl:when>
			
					<xsl:when test="$is_class='false'">
						<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
							<xsl:value-of select="/applyeasy/item/@title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="/applyeasy/item/nav/item" mode="nav">
							<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
							
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
				<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_prerequisite"/></span>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:if test="$is_class='false'">
			<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="extra_td">
					<td align="right">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_add_req"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:itmReq.itm_pre_ins_prep(<xsl:value-of select="$itm_id"/>,<xsl:value-of select="$itrOrder"/>)</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
						</xsl:call-template>
					</td>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
			<xsl:call-template name="draw_req_data">
				<xsl:with-param name="lab_edit_req" select="$lab_edit_req"/>
				<xsl:with-param name="lab_or" select="$lab_or"/>
				<xsl:with-param name="lab_del_req" select="$lab_del_req"/>
				<xsl:with-param name="lab_no_prerequisite" select="$lab_no_prerequisite"/>
				<xsl:with-param name="lab_prerequisite" select="$lab_prerequisite"/>
				 <xsl:with-param name="lab_add_req" select="$lab_add_req"/>
				  <xsl:with-param name="lab_add_req" select="$lab_add_req"/>
				  <xsl:with-param name="itrOrder" select="$itrOrder"/>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_req_data"> 
		<xsl:param name="lab_edit_req"/>
		<xsl:param name="lab_or"/>
		<xsl:param name="lab_del_req"/>
		<xsl:param name="lab_no_prerequisite"/>
		<xsl:param name="lab_prerequisite"/>
		<xsl:param name="lab_add_req"/>
		<xsl:param name="itrOrder"/>
		<xsl:choose>
			<xsl:when test="count($req_set/requirement[@type='PREREQUISITE'])=0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text"><xsl:value-of select="$lab_no_prerequisite"/></xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table>
					<xsl:apply-templates select="$req_set/requirement" mode="PREREQUISITE">
					  <xsl:with-param name="lab_edit_req" select="$lab_edit_req"/>
					  <xsl:with-param name="lab_or" select="$lab_or"/>
					  <xsl:with-param name="lab_del_req" select="$lab_del_req"/>
					  <xsl:with-param name="lab_prerequisite" select="$lab_prerequisite"/>
					  <xsl:with-param name="lab_add_req" select="$lab_add_req"/>
					  <xsl:with-param name="itrOrder" select="$itrOrder"/>
			       </xsl:apply-templates>
				</table>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="requirement" mode="PREREQUISITE">
		<xsl:param name="lab_edit_req"/>
		<xsl:param name="lab_or"/>
		<xsl:param name="lab_del_req"/>
		<xsl:param name="lab_prerequisite"/>
		<xsl:param name="lab_add_req"/>
		<xsl:param name="itrOrder"/>
		<xsl:choose>
			<xsl:when test="$is_class='false'">
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text"><xsl:value-of select="$lab_prerequisite"/>&#160;<xsl:value-of select="position()"/></xsl:with-param>
					<xsl:with-param name="extra_td">
						<td align="right" width="20%">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_edit_req"/></xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">Javascript:itmReq.itm_pre_upd_prep(<xsl:value-of select="../../@id"/>, <xsl:value-of select="@order"/>)</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_del_req"/></xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">Javascript:itmReq.itm_req_del_exec(<xsl:value-of select="../../@id"/>, <xsl:value-of select="@order"/>)</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
							</xsl:call-template>
						</td>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text"><xsl:value-of select="$lab_prerequisite"/>&#160;<xsl:value-of select="position()"/></xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:call-template name="wb_ui_line"/>
		<table>
		<xsl:for-each select="condition/rule_details/item">
		<tr>
			<td class="wzb-form-label">
				<xsl:choose>
					<xsl:when test="position() = 1"></xsl:when>
					<xsl:otherwise>
							<b>
								<xsl:value-of select="$lab_or"/>&#160;
							</b>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td class="wzb-form-control">
				<xsl:value-of select="@display_bil"/>
				<xsl:text>(</xsl:text><xsl:value-of select="@itm_code"/><xsl:text>)</xsl:text>
			</td>
		</tr>
		</xsl:for-each>
		</table>
		<xsl:if test="$is_class='true'">
			<xsl:call-template name="wb_ui_space"/>
		</xsl:if>
	</xsl:template>
	<!-- ================================================================ -->

<xsl:template name="show_no_requirement">
	<xsl:param name="no_req_mesg"/>
	<tr>
		<th colspan="3" height="19" align="right"></th>
	</tr>
	<tr>
		<th colspan="3" height="10" align="center">
			<xsl:value-of select="$no_req_mesg"/>
		</th>
	</tr>
	<tr class="wbRowsOdd">
		<th colspan="3" height="19" align="center"></th>
	</tr>
</xsl:template>
<!-- =============================================================== -->
	<xsl:template match="item" mode="nav">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_session_info"/>
		<xsl:variable name="_count" select="count(preceding-sibling::item)"/>
		<xsl:choose>
			<xsl:when test="@run_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=$_count">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.get_item_run_list({$value})" class="NavLink">
					<xsl:choose>
						<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
					</xsl:choose>
				</a>
				<span class="NavLink">&#160;&gt;&#160;</span>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
				<xsl:value-of select="title"/> 
				</a>
			</xsl:when>
			<xsl:when test="@session_ind = 'true'">
				<span class="NavLink">&#160;&gt;&#160;</span>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=last()">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.session.get_session_list({$value})" class="NavLink">
					<xsl:value-of select="$lab_session_info"/>
				</a>
				<span>&#160;&gt;&#160;</span>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a href="javascript:itm_lst.get_item_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
