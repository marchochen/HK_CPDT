<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- others -->
	<xsl:import href="wb_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_gen_tab.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<!-- cust utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- others -->
	<xsl:import href="share/usr_gen_tab_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<!-- paginatoin variables -->
	<xsl:variable name="page_size" select="user_manager/group_member_list/search/@page_size"/>
	<xsl:variable name="cur_page" select="user_manager/group_member_list/search/@cur_page"/>
	<xsl:variable name="page_total" select="user_manager/group_member_list/search/@total"/>
	<xsl:variable name="page_timestamp" select="user_manager/group_member_list/search/@time"/>
	<!-- sorting variable -->
	<xsl:variable name="sort_by" select="user_manager/group_member_list/search/@sort_by"/>
	<xsl:variable name="cur_order" select="user_manager/group_member_list/search/@order_by"/>
	<xsl:variable name="order_by">
		<xsl:choose>
			<xsl:when test="$cur_order = 'ASC' ">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="parent_id" select="user_manager/group_member_list/@id"/>
	<xsl:variable name="parent_role" select="user_manager/group_member_list/@grp_role"/>
	<xsl:variable name="cur_usr" select="user_manager/group_member_list/cur_usr/@id"/>
	<xsl:variable name="root_ent_id" select="user_manager/group_member_list/cur_usr/@root_ent_id"/>
	<xsl:variable name="entity_cnt">
		<xsl:value-of select="count(user_manager/group_member_list/user)"/>
	</xsl:variable>
	<xsl:variable name="page_variant" select="/user_manager/meta/page_variant"/>
	<!-- -->
	<xsl:variable name="tmp_page_timestamp" select="translate($page_timestamp,':- ','')"/>
	<xsl:variable name="compare_timestamp_year" select="number(substring($tmp_page_timestamp,1,4))"/>
	<xsl:variable name="compare_timestamp_month" select="number(substring($tmp_page_timestamp,5,2))"/>
	<xsl:variable name="compare_timestamp_day">
		<xsl:value-of select="number(substring($tmp_page_timestamp,7,2))"/>
	</xsl:variable>
	<xsl:variable name="compare_timestamp_hour">
		<xsl:value-of select="number(substring($tmp_page_timestamp,9,2))"/>
	</xsl:variable>
	<xsl:variable name="compare_timestamp_min">
		<xsl:value-of select="number(substring($tmp_page_timestamp,11,2))"/>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/user_manager">
		<xsl:apply-templates select="group_member_list"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="group_member_list">
		<xsl:param name="lab_desc"/>
		<html>
			<head>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[			
				usr = new wbUserGroup;
				function status(){
					usr.search_exec(document.frmXml,']]><xsl:value-of select="$wb_lang"/><![CDATA[')
					return false;
				}
				
			]]></script>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			</head>
			<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
				<form onsubmit="return status()" name="frmXml">
					<input type="hidden" name="stylesheet"/>
					<input type="hidden" name="cmd"/>
					<input type="hidden" name="ent_id" value="{$parent_id}"/>
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_reg_approval">登記審批</xsl:with-param>	
			<xsl:with-param name="lab_registration">登記</xsl:with-param>
			<xsl:with-param name="lab_reg_date">登記日期</xsl:with-param>
			<xsl:with-param name="lab_not_match">找不到登記</xsl:with-param>
			<xsl:with-param name="lab_desc">以下是你要審批的登記，請按用戶的姓名來批准或拒絕登記。</xsl:with-param>
			<xsl:with-param name="lab_footnote">* 符有報讀課程的要求</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_reg_approval">注册审批</xsl:with-param>		
			<xsl:with-param name="lab_registration">注册</xsl:with-param>
			<xsl:with-param name="lab_reg_date">注册日期</xsl:with-param>
			<xsl:with-param name="lab_not_match">没有需要审批的新注册用户</xsl:with-param>
			<xsl:with-param name="lab_desc">以下是你要审批的新注册用户，请点击用户的昵称来批准或拒绝注册。</xsl:with-param>
			<xsl:with-param name="lab_footnote">* 对未发布课程的注册要求</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_reg_approval">Registration approval</xsl:with-param>
			<xsl:with-param name="lab_registration">Registrations</xsl:with-param>
			<xsl:with-param name="lab_reg_date">Registration date</xsl:with-param>
			<xsl:with-param name="lab_not_match">No registrations found</xsl:with-param>
			<xsl:with-param name="lab_desc">Listed below are the registrations pending your approval. Click the name of the user to approve or decline the registration.</xsl:with-param>
			<xsl:with-param name="lab_footnote">* With pending course enrollment request</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_usr_reg_approval"/>
		<xsl:param name="lab_registration"/>
		<xsl:param name="lab_reg_date"/>
		<xsl:param name="lab_not_match"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_footnote"/>
		<!-- heading -->
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_USR_INFO</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_usr_reg_approval"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_desc"/>
			</xsl:with-param>
		</xsl:call-template>
		<!--  
		<xsl:call-template name="usr_gen_tab">
			<xsl:with-param name="usr_target_tab">2</xsl:with-param>
		</xsl:call-template>-->

		<xsl:choose>
			<xsl:when test="$entity_cnt &gt;= 1">
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td >
							<xsl:value-of select="$lab_login_id"/>
						</td>						
						<td>
							<xsl:variable name="_order">
								<xsl:choose>
									<xsl:when test="$sort_by = 'usr_display_bil' ">
										<xsl:value-of select="$order_by"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$cur_order"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$sort_by = 'usr_display_bil' ">
									<a href="javascript:usr.register.reg_usr_approval_lst({$cur_page},{$page_size},'usr_display_bil','{$order_by}','{$page_timestamp}')">
										<xsl:value-of select="$lab_dis_name"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path">
												<xsl:value-of select="$wb_img_path"/>
											</xsl:with-param>
											<xsl:with-param name="sort_order">
												<xsl:value-of select="$cur_order"/>
											</xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:usr.register.reg_usr_approval_lst({$cur_page},{$page_size},'usr_display_bil','asc','{$page_timestamp}')">
										<xsl:value-of select="$lab_dis_name"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td>
							<xsl:value-of select="$lab_group"/>
						</td>
						<td align="right">
							<xsl:variable name="_order">
								<xsl:choose>
									<xsl:when test="$sort_by = 'usr_signup_date' ">
										<xsl:value-of select="$order_by"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$cur_order"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$sort_by = 'usr_signup_date' ">
									<a href="javascript:usr.register.reg_usr_approval_lst({$cur_page},{$page_size},'usr_signup_date','{$order_by}','{$page_timestamp}')">
										<xsl:value-of select="$lab_reg_date"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path">
												<xsl:value-of select="$wb_img_path"/>
											</xsl:with-param>
											<xsl:with-param name="sort_order">
												<xsl:value-of select="$cur_order"/>
											</xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:usr.register.reg_usr_approval_lst({$cur_page},{$page_size},'usr_signup_date','asc','{$page_timestamp}')">
										<xsl:value-of select="$lab_reg_date"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
					<xsl:apply-templates select="user"/>
				</table>
				<!-- Pagination -->
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="total" select="$page_total"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_not_match"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="user">
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2">RowsEven</xsl:when>
				<xsl:otherwise>RowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr>
			<td >
			<a href="javascript:usr.register.reg_usr_approval({@ent_id})" class="Text">
				<xsl:value-of select="@id"/>
			</a>
			</td>
			<td>
				
					<xsl:value-of select="name/@display_name"/>
				
				<xsl:if test="count(item_list/item) &gt; 0">
					*
				</xsl:if>
			</td>
			<td>
				<xsl:choose>
					<xsl:when test="count(user_attribute_list/attribute_list[@type='USG']/entity) &gt;= 1">
						<xsl:value-of select="full_path"/>
						<!--<xsl:for-each select="user_attribute_list/attribute_list[@type='USG']/entity">
							<xsl:value-of select="@display_bil"/>
							<xsl:if test="position() != last()">
								<xsl:text>, </xsl:text>
							</xsl:if>
						</xsl:for-each>-->
					</xsl:when>
					<xsl:otherwise>
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="right">
				<xsl:call-template name="display_time">
					<xsl:with-param name="my_timestamp">
						<xsl:value-of select="@signup_date"/>
					</xsl:with-param>
				</xsl:call-template>
			</td>			
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="display_tel_number">
		<xsl:param name="numVal"/>
		<xsl:choose>
			<xsl:when test="contains($numVal,'-n/a')">
				<xsl:value-of select="substring-before($numVal,'-n/a-')"/>
				<xsl:text>-</xsl:text>
				<xsl:value-of select="substring-after($numVal,'-n/a-')"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$numVal"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="cur_usr"/>
	<!-- =============================================================== -->
</xsl:stylesheet>
