<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="root_ent_id" select="/applyeasy/meta/cur_usr/@root_ent_id"/>
	<xsl:variable name="checked_root">
		<xsl:choose>
			<xsl:when test="/applyeasy/catalog/assigned_user /entity/@ent_id =  $root_ent_id">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="cata_id" select="/applyeasy/catalog/@cat_id">
	</xsl:variable>
	<xsl:variable name="tc_enabled" select="/applyeasy/meta/tc_enabled"/>
	<xsl:variable name="is_show_all">true</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/applyeasy">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_cata_lst.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}cwn_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[			
				cata_lst = new wbCataLst
				var goldenman = new wbGoldenMan
				function status(){	return false;}
				var parent_tcr_id = getUrlParam('cat_tcr_id');
				if(parent_tcr_id == '' || parent_tcr_id == 'undefined' || typeof(parent_tcr_id) == 'undefined' ){

				  parent_tcr_id = 0;
				}
					
			]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="document.frmXml.cat_title.focus(); ">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_cata">全目錄</xsl:with-param>
			<xsl:with-param name="lab_add_cata">新增目錄</xsl:with-param>
			<xsl:with-param name="lab_edit_cata">修改目錄</xsl:with-param>
			<xsl:with-param name="lab_cata_nm">目錄名稱</xsl:with-param>
			<xsl:with-param name="lab_acss_restrict">存取範圍</xsl:with-param>
			<xsl:with-param name="lab_acss_public">共享</xsl:with-param>
			<xsl:with-param name="lab_select_inst">指定用戶小組/用戶</xsl:with-param>
			<xsl:with-param name="lab_cata_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_status_on">已發佈</xsl:with-param>
			<xsl:with-param name="lab_status_off">未發佈</xsl:with-param>
			<xsl:with-param name="lab_inherit_from">繼承自</xsl:with-param>
			<xsl:with-param name="lab_inherit_from_all">所有</xsl:with-param>
			<xsl:with-param name="lab_inherit_from_all_in">用戶</xsl:with-param>
			<xsl:with-param name="lab_item_type">目錄內容</xsl:with-param>
			<xsl:with-param name="lab_all">所有</xsl:with-param>
			<xsl:with-param name="lab_cata_desc">描述</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_desc_limit">(不超過2000個字元(1000個中文字))</xsl:with-param>
			<xsl:with-param name="lab_tcr">培訓中心</xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">你可以在“我的喜好”中設置“主要培訓中心”<br/>設置後，在任何指定培訓中心的操作中，將默認的指定爲設定的主要培訓中心</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_cata">课程目录</xsl:with-param>
			<xsl:with-param name="lab_add_cata">添加目录</xsl:with-param>
			<xsl:with-param name="lab_edit_cata">修改目录</xsl:with-param>
			<xsl:with-param name="lab_cata_nm">目录名称</xsl:with-param>
			<xsl:with-param name="lab_acss_restrict">存取范围</xsl:with-param>
			<xsl:with-param name="lab_acss_public">共享</xsl:with-param>
			<xsl:with-param name="lab_select_inst">指定用户小组/用户</xsl:with-param>
			<xsl:with-param name="lab_cata_status">状态</xsl:with-param>
			<xsl:with-param name="lab_status_on">已发布</xsl:with-param>
			<xsl:with-param name="lab_status_off">未发布</xsl:with-param>
			<xsl:with-param name="lab_inherit_from">继承自</xsl:with-param>
			<xsl:with-param name="lab_inherit_from_all">所有</xsl:with-param>
			<xsl:with-param name="lab_inherit_from_all_in">用户</xsl:with-param>
			<xsl:with-param name="lab_item_type">目录内容</xsl:with-param>
			<xsl:with-param name="lab_all">所有</xsl:with-param>
			<xsl:with-param name="lab_cata_desc">描述</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_desc_limit">(不超过2000个字符(1000个中文字))</xsl:with-param>
			<xsl:with-param name="lab_tcr">培训中心</xsl:with-param>
	       <xsl:with-param name="lab_def_tc_desc">你可以在“我的喜好”中设置“主要培训中心”<br/>设置后，在任何指定培训中心的操作中，将默认的指定为设定的主要培训中心</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_cata">Learning catalogs</xsl:with-param>
			<xsl:with-param name="lab_add_cata">Add learning catalog</xsl:with-param>
			<xsl:with-param name="lab_edit_cata">Edit learning catalog</xsl:with-param>
			<xsl:with-param name="lab_cata_nm">Name</xsl:with-param>
			<xsl:with-param name="lab_acss_restrict">User who should see this catalog</xsl:with-param>
			<xsl:with-param name="lab_acss_public">Public</xsl:with-param>
			<xsl:with-param name="lab_select_inst">Specify user group(s)/user(s)</xsl:with-param>
			<xsl:with-param name="lab_cata_status">Status</xsl:with-param>
			<xsl:with-param name="lab_status_on">Published</xsl:with-param>
			<xsl:with-param name="lab_status_off">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_inherit_from">Inherit form</xsl:with-param>
			<xsl:with-param name="lab_inherit_from_all">All users in </xsl:with-param>
			<xsl:with-param name="lab_inherit_from_all_in"/>
			<xsl:with-param name="lab_item_type">Content</xsl:with-param>
			<xsl:with-param name="lab_all">All user groups</xsl:with-param>
			<xsl:with-param name="lab_cata_desc">Description</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_desc_limit">(Not more than 2000 characters)</xsl:with-param>
			<xsl:with-param name="lab_tcr">Training center</xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">You can specify “Major Training Center” in “My Preference”. <br/>After that, your major training center will be default selected wherever a selection of training center is needed.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_all_cata"/>
		<xsl:param name="lab_add_cata"/>
		<xsl:param name="lab_edit_cata"/>
		<xsl:param name="lab_cata_nm"/>
		<xsl:param name="lab_acss_restrict"/>
		<xsl:param name="lab_acss_public"/>
		<xsl:param name="lab_select_inst"/>
		<xsl:param name="lab_cata_status"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_inherit_from_all"/>
		<xsl:param name="lab_inherit_from_all_in"/>
		<xsl:param name="lab_inherit_from"/>
		<xsl:param name="lab_item_type"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_cata_desc"/>
		<xsl:param name="lab_desc_limit"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_tcr"/>
		<xsl:param name="lab_def_tc_desc"/>
		<form name="frmXml" onsubmit="return status()">
			<xsl:call-template name="header">
				<xsl:with-param name="lab_all_cata" select="$lab_all_cata"/>
				<xsl:with-param name="lab_add_cata" select="$lab_add_cata"/>
				<xsl:with-param name="lab_edit_cata" select="$lab_edit_cata"/>
			</xsl:call-template>
			<xsl:apply-templates select="catalog">
				<xsl:with-param name="lab_cata_nm" select="$lab_cata_nm"/>
				<xsl:with-param name="lab_acss_restrict" select="$lab_acss_restrict"/>
				<xsl:with-param name="lab_acss_public" select="$lab_acss_public"/>
				<xsl:with-param name="lab_select_inst" select="$lab_select_inst"/>
				<xsl:with-param name="lab_cata_status" select="$lab_cata_status"/>
				<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
				<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
				<xsl:with-param name="lab_inherit_from" select="$lab_inherit_from"/>
				<xsl:with-param name="lab_inherit_from_all" select="$lab_inherit_from_all"/>
				<xsl:with-param name="lab_inherit_from_all_in" select="$lab_inherit_from_all_in"/>
				<xsl:with-param name="lab_item_type" select="$lab_item_type"/>
				<xsl:with-param name="lab_add_cata" select="$lab_add_cata"/>
				<xsl:with-param name="lab_edit_cata" select="$lab_edit_cata"/>
				<xsl:with-param name="lab_all" select="$lab_all"/>
				<xsl:with-param name="lab_cata_desc" select="$lab_cata_desc"/>
				<xsl:with-param name="lab_desc_limit" select="$lab_desc_limit"/>
				<xsl:with-param name="lab_g_form_btn_ok" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="lab_g_form_btn_cancel" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="lab_tcr" select="$lab_tcr"/>
				<xsl:with-param name="lab_def_tc_desc" select="$lab_def_tc_desc"/>
			</xsl:apply-templates>
		</form>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="header">
		<xsl:param name="lab_all_cata"/>
		<xsl:param name="lab_add_cata"/>
		<xsl:param name="lab_edit_cata"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_CAT_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_CAT_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="catalog/title[text()]">
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_edit_cata"/>
						<xsl:text>&#160;-&#160;</xsl:text>
						<xsl:value-of select="catalog/title"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_add_cata"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="catalog">
		<xsl:param name="lab_cata_nm"/>
		<xsl:param name="lab_acss_restrict"/>
		<xsl:param name="lab_acss_public"/>
		<xsl:param name="lab_select_inst"/>
		<xsl:param name="lab_cata_status"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_inherit_from"/>
		<xsl:param name="lab_inherit_from_all"/>
		<xsl:param name="lab_inherit_from_all_in"/>
		<xsl:param name="lab_item_type"/>
		<xsl:param name="lab_add_cata"/>
		<xsl:param name="lab_edit_cata"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_cata_desc"/>
		<xsl:param name="lab_desc_limit"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_tcr"/>
		<xsl:param name="lab_def_tc_desc"/>
		<!-- form option -->
		<table>
			<!-- title -->

			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_cata_nm"/>：
				</td>
				<td class="wzb-form-control" >
					<xsl:choose>
						<xsl:when test="title  !=  ''">
							<input class="wzb-inputText" size="35" name="cat_title" type="text" style="width:300px;"  value="{title}"/>
						</xsl:when>
						<xsl:otherwise>
							<input class="wzb-inputText" size="35" name="cat_title" type="text" style="width:300px;"  value=""/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<!-- training center -->
			<xsl:if test="$tc_enabled='true'">
				<tr>
					<td class="wzb-form-label">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_tcr"/>：
					</td>
					<td class="wzb-form-control">
						<xsl:call-template name="wb_goldenman">
							<xsl:with-param name="field_name">tcr_id</xsl:with-param>
							<xsl:with-param name="name">tcr_id</xsl:with-param>
							<xsl:with-param name="box_size">1</xsl:with-param>
							<xsl:with-param name="tree_type">training_center</xsl:with-param>
							<xsl:with-param name="select_type">2</xsl:with-param>
							<xsl:with-param name="pick_leave">0</xsl:with-param>
							<xsl:with-param name="pick_root">0</xsl:with-param>
							<xsl:with-param name="single_option_value"><xsl:value-of select="//training_center/@id"/></xsl:with-param>
							<xsl:with-param name="single_option_text"><xsl:value-of select="//training_center/title"/></xsl:with-param>
						</xsl:call-template>
					</td>
					<input type="hidden" name="org_tcr_id" value="{//training_center/@id}"/>
					<input type="hidden" name="cat_tcr_id" value=""/>
				</tr>
			</xsl:if>
			<!--  item type -->
			<!--<xsl:if test="not($cata_id)">
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText"><xsl:value-of select="$lab_item_type"/><xsl:text>：</xsl:text></span>
				</td>						
				<td width="80%">
					<span class="Text">
						<table cellpadding="0" cellspacing="0" border="0">									
							<xsl:choose>
								<xsl:when test="count(item_type_list/item_type) &gt;=1">
									<xsl:for-each select="item_type_list/item_type">
										<xsl:if test="not(position() mod 2 = 0)"><xsl:text disable-output-escaping="yes">&lt;tr&gt;</xsl:text></xsl:if>
										<td>
											<span class="Text">
												<label for="id_{@id}"><input type="checkbox" name="ity_id" value="{@id}" id="id_{@id}" checked="checked"/><xsl:value-of select="desc[@lan = $wb_lang_encoding]/@name"/></label>
											</span>
										</td>
										<xsl:if test="not(position() mod 2 = 0)"><td>&#160;</td></xsl:if>
										<xsl:if test="position() mod 2 = 0"><xsl:text disable-output-escaping="yes">&lt;/tr&gt;</xsl:text></xsl:if>												
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise><td>&#160;</td></xsl:otherwise>
							</xsl:choose>									
						</table>
					</span>
					<input type="hidden" name="ity_id_lst" value=""/>
				</td>
			</tr>	
		</xsl:if>-->
			<input type="hidden" name="ity_id_lst" value=""/>
			<!-- asscess control -->
			<xsl:if test="@public_ind">
			
		
				<xsl:choose>
					<xsl:when test="@public_ind = 'true'">
						<tr>
							<td class="wzb-form-label">
								<span class="wzb-form-star">*</span><xsl:value-of select="$lab_acss_restrict"/>：
							</td>
							<td class="wzb-form-control">
								<label for="id_cat_public_ind1">
									<input id="id_cat_public_ind1" type="radio" name="cat_public_ind" value="true" checked="checked"/>&#160;<xsl:value-of select="$lab_acss_public"/>
								</label>
							</td>
						</tr>
						<tr>
							<td class="wzb-form-label">
							</td>
							<td class="wzb-form-control">
								<label for="id_cat_public_ind2">
									<input id="id_cat_public_ind2" type="radio" name="cat_public_ind" value="false"/>&#160;<xsl:value-of select="$lab_inherit_from_all"/>
									<xsl:value-of select="//cur_usr/@root_display"/>
									<xsl:value-of select="$lab_inherit_from_all_in"/>
								</label>
							</td>
						</tr>
						<tr>
							<td class="wzb-form-label">
							</td>
							<td class="wzb-form-control">
								<label for="id_cat_public_ind3">
									<input id="id_cat_public_ind3" type="radio" name="cat_public_ind" value="false"/>&#160;<xsl:value-of select="$lab_select_inst"/>
								</label>
							</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<!-- Catalog Public access has been disable-->
						<!--<tr>
						<td width="20%" class="wbformLeftBg" align="right">
							<span class="wbformLeftText"><xsl:value-of select="$lab_acss_restrict"/><xsl:text>：</xsl:text></span>
						</td>
						<td width="80%" class="wbformRightBg">
							<span class="wbformRightText"><input type="radio" name="cat_public_ind" value="true" checked="checked"/><xsl:text>&#160;</xsl:text><xsl:value-of select="$lab_acss_public"/></span>
						</td>
					</tr>-->
						<!-- hidden field has been added below is to replace the radio button above -->
						<input type="hidden" name="cat_public_ind" value="false"/>
						<!--<tr>
						<td width="20%" class="wbformLeftBg" align="right">
							<span class="wbformLeftText"><xsl:value-of select="$lab_acss_restrict"/><xsl:text>：</xsl:text></span>
						</td>
						<td width="80%" class="wbformRightBg">
							<xsl:choose>
								<xsl:when test="$cata_id">
									<span class="wbformRightText">
										<xsl:choose>
											<xsl:when test="$checked_root =  'true'">
												<input type="radio" name="cat_public_ind" value="false" checked="checked"/>
											</xsl:when>
											<xsl:otherwise><input type="radio" name="cat_public_ind" value="false"/></xsl:otherwise>
										</xsl:choose>
										<xsl:text>&#160;</xsl:text>
									</span>
									<span  class="wbformRightText"><xsl:value-of select="$lab_inherit_from_all"/></span>
									<span class="wbformRightText"><xsl:value-of select="//cur_usr/@root_display"/></span>
									<xsl:text>&#160;</xsl:text>
									<span  class="wbformRightText"><xsl:value-of select="$lab_inherit_from_all_in"/></span>
									<span class="wbformRightText">
										<xsl:choose>
											<xsl:when test="$checked_root =  'true'"><input type="radio" name="cat_public_ind" value="false"/></xsl:when>
											<xsl:otherwise><input type="radio" name="cat_public_ind" value="false" checked="checked"/></xsl:otherwise>
										</xsl:choose>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="$lab_select_inst"/>
									</span>
								</xsl:when>
								<xsl:otherwise>
									<span class="wbformRightText">
										<input type="radio" name="cat_public_ind" value="false" checked="checked"/>										
										<xsl:text>&#160;</xsl:text>
									</span>
									<span  class="wbformRightText"><xsl:value-of select="$lab_inherit_from_all"/></span>
									<span class="wbformRightText"><xsl:value-of select="//cur_usr/@root_display"/></span>
									<xsl:text>&#160;</xsl:text>
									<span  class="wbformRightText"><xsl:value-of select="$lab_inherit_from_all_in"/></span>
									<span class="wbformRightText">
										<input type="radio" name="cat_public_ind" value="false"/>											
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="$lab_select_inst"/>
									</span>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>-->
						<!--<tr>
						<td width="20%" align="right" valign="top">
							<span class="TitleText"><xsl:value-of select="$lab_acss_restrict"/><xsl:text>：</xsl:text></span>
						</td>
						<td width="80%">
							<span class="Text">
							<xsl:call-template name="wb_goldenman">
								<xsl:with-param name="frm">document.frmXml</xsl:with-param>
								<xsl:with-param name="width">320</xsl:with-param>
								<xsl:with-param name="field_name">cata_usr_lst</xsl:with-param>
								<xsl:with-param name="tree_type">user_group_and_user</xsl:with-param>
								<xsl:with-param name="select_type">1</xsl:with-param>
								<xsl:with-param name="box_size">3</xsl:with-param>
								<xsl:with-param name="pick_leave">0</xsl:with-param>
								<xsl:with-param name="option_list">
									<xsl:choose>
										<xsl:when test="count(assigned_user/entity) &gt;= 1">
											<xsl:for-each select="assigned_user/entity">
												<option value="{@ent_id}"><xsl:value-of select="@name"/>
												</option>
											</xsl:for-each>
										</xsl:when>
										<xsl:otherwise><option value="0"><xsl:value-of select="$lab_all"/></option></xsl:otherwise>
									</xsl:choose>
								</xsl:with-param>
							</xsl:call-template>
							</span>
						</td>
					</tr>-->
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
			<!-- status -->
			<xsl:choose>
				<xsl:when test="not(not($cata_id))">
					<tr>
						<td class="wzb-form-label">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_cata_status"/>：
						</td>
						<td class="wzb-form-control">
							<xsl:choose>
								<xsl:when test="@status = 'ON' ">
									<label for="id_cat_status1">
										<input id="id_cat_status1" type="radio" name="cat_status" value="ON" checked="checked"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="$lab_status_on"/>
									</label>
									<xsl:text>&#160;</xsl:text>
									<label for="id_cat_status2">
										<input id="id_cat_status2" type="radio" name="cat_status" value="OFF"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="$lab_status_off"/>
									</label>
								</xsl:when>
								<xsl:otherwise>
									<label for="id_cat_status1">
										<input id="id_cat_status1" type="radio" name="cat_status" value="ON"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="$lab_status_on"/>
									</label>
									<xsl:text>&#160;</xsl:text>
									<label for="id_cat_status2">
										<input id="id_cat_status2" type="radio" name="cat_status" value="OFF" checked="checked"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="$lab_status_off"/>
									</label>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td class="wzb-form-label">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_cata_status"/>：
						</td>
						<td class="wzb-form-control">
							<label for="id_cat_status1">
								<input type="radio" id="id_cat_status1" name="cat_status" value="ON" checked="checked"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$lab_status_on"/>
							</label>
							<xsl:text>&#160;</xsl:text>
							<label for="id_cat_status2">
								<input id="id_cat_status2" type="radio" name="cat_status" value="OFF"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$lab_status_off"/>
							</label>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
				<!-- description -->
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_cata_desc"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="desc !=  ''">
							<textarea class="wzb-inputTextArea" name="cat_desc" style="width:300px;" rows="4">
								<xsl:value-of select="desc"/>
							</textarea>
						</xsl:when>
						<xsl:otherwise>
							<textarea class="wzb-inputTextArea" name="cat_desc" style="width:300px;" rows="4"/>
						</xsl:otherwise>
					</xsl:choose>
					<br/>
					<xsl:value-of select="$lab_desc_limit"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
				</td>
				<td class="wzb-ui-module-text">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		</table>
		<!-- form option end -->
		<xsl:if test="title !=''">
			<input type="hidden" name="cat_upd_timestamp" value="{last_updated/@timestamp}"/>
			<input type="hidden" name="cat_id" value="{@cat_id}"/>
			<input type="hidden" name="node_id" value="{nav/node/@node_id}"/>
		</xsl:if>
		<input type="hidden" name="url_success"/>
		<input type="hidden" name="cat_public_ind" value="false"/>
		<input type="hidden" name="cat_acc_ent_id_list"/>
		<input type="hidden" name="stylesheet"/>
		<input type="hidden" name="cmd"/>
		<input type="hidden" name="url_failure"/>
		<input type="hidden" name="root_ent_id" value="{$root_ent_id}"/>
		<!-- end -->
		<div class="wzb-bar">
			<xsl:choose>
				<xsl:when test="title  !=  ''">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_g_form_btn_ok"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:cata_lst.edit_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_g_form_btn_ok"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:cata_lst.add_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:variable name="back_id">
				<xsl:choose>
					<xsl:when test="@node_id != '' and @parent_tnd_id != ''"><xsl:value-of select="@node_id"/></xsl:when>
					<xsl:when test="@ndoe_id = '' and @parent_tnd_id != ''"><xsl:value-of select="@parent_tnd_id"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="@parent_tnd_id"/></xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
					<xsl:value-of select="$lab_g_form_btn_cancel"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">
					<!-- <xsl:choose>
						<xsl:when test="$cata_id = ''"> -->
							<xsl:choose>
								<xsl:when test="//meta/tc_enabled='false'">javascript:wb_utils_cata_lst()</xsl:when>
								<xsl:when test="//meta/tc_enabled='true'">
									<xsl:text>javascript:cata_lst.show_content(parent_tcr_id);</xsl:text>
								</xsl:when>
								<xsl:otherwise>javascript:wb_utils_cata_lst('true')</xsl:otherwise>
							</xsl:choose>
						<!-- </xsl:when>
						<xsl:otherwise>javascript:wb_utils_node_lst('<xsl:value-of select="nav/node/@node_id"/>')</xsl:otherwise>
					</xsl:choose> -->
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
