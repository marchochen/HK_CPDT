<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
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
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/usr_search_form_share.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:output indent="yes"/>
	
	<xsl:variable name="profile_attributes" select="/applyeasy/meta/profile_attributes"/>
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="ent_id" select="/applyeasy/meta/cur_usr/@root_ent_id"/>
	<xsl:variable name="filter_user_group">1</xsl:variable>
	<xsl:variable name="columns" select="/applyeasy/columns"/>
	<xsl:variable name="label" select="/applyeasy/label_reference_data_list"/>
	<!-- =============================================================== -->
	<xsl:template match="/applyeasy">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_enrollment">處理報名</xsl:with-param>
			<xsl:with-param name="lab_adv_search">高級篩選</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_enroll_upd_date">報名記錄修改日期</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_enroll_status">報名記錄狀態</xsl:with-param>
			<xsl:with-param name="lab_all_type">所有</xsl:with-param>
			<xsl:with-param name="lab_sort_order">篩選次序</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_pending_approver">等候審批者</xsl:with-param>
			<xsl:with-param name="lab_modify">修改日期</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_enrollment">处理报名</xsl:with-param>
			<xsl:with-param name="lab_adv_search">高级筛选</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_enroll_upd_date">报名记录修改日期</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_enroll_status">报名记录状态</xsl:with-param>
			<xsl:with-param name="lab_all_type">所有</xsl:with-param>
			<xsl:with-param name="lab_sort_order">排序</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_pending_approver">下一审批者</xsl:with-param>
			<xsl:with-param name="lab_modify">修改日期</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_enrollment">Process enrollment</xsl:with-param>
			<xsl:with-param name="lab_adv_search">Advanced filter</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
			<xsl:with-param name="lab_enroll_upd_date">Enrollment modified</xsl:with-param>
			<xsl:with-param name="lab_to">to</xsl:with-param>
			<xsl:with-param name="lab_enroll_status">Enrollment status</xsl:with-param>
			<xsl:with-param name="lab_all_type">All</xsl:with-param>
			<xsl:with-param name="lab_sort_order">Sort order</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_pending_approver">Pending approver</xsl:with-param>
			<xsl:with-param name="lab_modify">Modified</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_adv_search"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_enrollment"/>
		<xsl:param name="lab_enroll_upd_date"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_enroll_status"/>
		<xsl:param name="lab_all_type"/>
		<xsl:param name="lab_sort_order"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_pending_approver"/>
		<xsl:param name="lab_modify"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
			itm_lst = new wbItem;
			app =  new wbApplication
			usr = new wbUserGroup;
			var goldenman = new wbGoldenMan;
			function status(){return false;}		
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml" method="post" onsubmit="return status()">
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="ent_id" value="{$ent_id}"/>
				<input type="hidden" name="app_process_status"/>
			   <input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure"  value=""/>
				<input type="hidden" name="filter_type" value="advanced_filter"/>
				<!-- navigation -->
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
					<xsl:with-param name="parent_code" select="$parent_code"/>
				</xsl:call-template>
			    <xsl:call-template name="itm_action_nav">
					<xsl:with-param  name="cur_node_id">111</xsl:with-param>
				</xsl:call-template>
				<div class="wzb-item-main">
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">
						<xsl:value-of select="//itm_action_nav/@itm_title"/>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_nav_link">
					<xsl:with-param name="text">
						<xsl:choose>
							<xsl:when test="/applyeasy/item/@run_ind = 'false'">
								<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
									<xsl:value-of select="/applyeasy/item/title"/>
								</a>
								<span class="NavLink"><xsl:text>&#160;&gt;&#160;</xsl:text></span>
								<a href="javascript:app.get_application_list('',{$itm_id})" class="NavLink">
									<xsl:value-of select="$lab_enrollment"/>
								</a>
								<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_adv_search"/>
								</span>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="/applyeasy/item/nav/item" mode="nav">
									<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
									<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
								</xsl:apply-templates>
								<span class="NavLink"><xsl:text>&#160;&gt;&#160;</xsl:text></span>
								<a href="javascript:app.get_application_list('',{$itm_id})" class="NavLink">
									<xsl:value-of select="$lab_enrollment"/>
								</a>
								<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_adv_search"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
				</xsl:call-template>
				<table>
					<xsl:apply-templates select="$profile_attributes/*[(not(@active) or @active = 'true') and (@searchable = 'adv' or @searchable = 'all') and (name() != 'role')]" mode="profile_attributes"/>
					<xsl:apply-templates select="/applyeasy/meta/workflow">
						<xsl:with-param name="lab_enroll_status" select="$lab_enroll_status"/>
						<xsl:with-param name="lab_all_type" select="$lab_all_type"/>
					</xsl:apply-templates>
					<xsl:call-template name="draw_layout">
						<xsl:with-param name="title"><xsl:value-of select="$lab_enroll_upd_date"/>:</xsl:with-param>
						<xsl:with-param name="content">
							<xsl:call-template name="display_form_input_time">
							<xsl:with-param name="fld_name">upddate_from</xsl:with-param>
							<xsl:with-param name="hidden_fld_name">upddate_fr</xsl:with-param>
							<xsl:with-param name="frm">document.frmXml</xsl:with-param>
							</xsl:call-template>
							<xsl:text>&#160;</xsl:text><xsl:value-of select="$lab_to"/><xsl:text>&#160;</xsl:text>
							<xsl:call-template name="display_form_input_time">
								<xsl:with-param name="fld_name">upddate_to</xsl:with-param>
								<xsl:with-param name="hidden_fld_name">upddate_to</xsl:with-param>
								<xsl:with-param name="frm">document.frmXml</xsl:with-param>
							</xsl:call-template>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="appn_sort">
						<xsl:with-param name="lab_status" select="$lab_status"/>
						<xsl:with-param name="lab_pending_approver" select="$lab_pending_approver"/>
						<xsl:with-param name="lab_modify" select="$lab_modify"/>
					</xsl:call-template>
					<xsl:call-template name="draw_layout"/>
				</table>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_g_form_btn_ok"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:app.set_appn_adv_filter('<xsl:value-of select="$itm_id"/>',document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_g_form_btn_cancel"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:app.get_application_list('','<xsl:value-of select="$itm_id"/>')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>
				</div>
				</div>
			</form>
		</body>
	</xsl:template>
	<xsl:template name="get_label">
		<xsl:param name="label_name"/>
		<xsl:variable name="label_usr" select="$profile_attributes/*[name() = $label_name]/label[@xml:lang = $cur_lang]"/>
		<xsl:choose>
			<xsl:when test="$label_usr !=''"><xsl:value-of select="$label_usr"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="$label/label[@name = $label_name]"/></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="appn_sort">
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_pending_approver"/>
		<xsl:param name="lab_modify"/>
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_usr_sort"/>:</xsl:with-param>
			<xsl:with-param name="content">
			<select name="appn_sort_by" size="1" class="wzb-form-select">
				<xsl:for-each select="$columns/col">
					<xsl:if test="@active='true' and @extra='no'">
						<xsl:variable name="label_name" select="@label"/>
						<xsl:variable name="name_" select="db_col/@name"/>
						<option value="{$name_}">	
							<xsl:if test="$name_='usr_ste_usr_id'">
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
							<xsl:call-template name="get_label">
								<xsl:with-param name="label_name" select="$label_name"/>
							</xsl:call-template>
						</option>
					</xsl:if>
				</xsl:for-each>
			</select>
			<select name="appn_order_by" size="1" class="wzb-form-select">
				<option selected="selected" value="asc"><xsl:value-of select="$lab_sort_value_1"/></option>
				<option value="desc"><xsl:value-of select="$lab_sort_value_2"/></option>
			</select>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="workflow">
		<xsl:param name="lab_enroll_status"/>
		<xsl:param name="lab_all_type"/>
		<xsl:call-template name="draw_layout">
			<xsl:with-param name="title"><xsl:value-of select="$lab_enroll_status"/>:</xsl:with-param>
			<xsl:with-param name="content">
			<select name="type_sel_frm" size="1" class="wzb-form-select">
				<option value="" selected="selected">
					<xsl:value-of select="$lab_all_type"/>
				</option>
				<xsl:for-each select="process/status[@name!='_Exit' and  @name != '_Init']">
					
					<xsl:if test="(@id != 6 and @id != 5 )">
					<option value="{@id}">
						<xsl:value-of select="@name"/>
					</option>
					</xsl:if>
					
				</xsl:for-each>
			</select>
			</xsl:with-param>
		</xsl:call-template>
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
	<!-- =============================================================== -->
</xsl:stylesheet>
