<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- others -->
	<xsl:import href="wb_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_gen_tab.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<!-- cust utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- others -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/usr_gen_tab_share.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<!-- paginatoin variables -->
	<xsl:variable name="page_size" select="user_manager/suspense_user_list/search/@page_size"/>
	<xsl:variable name="search_code" select="user_manager/suspense_user_list/search/@search_code"/>
	<xsl:variable name="cur_page" select="user_manager/suspense_user_list/search/@cur_page"/>
	<xsl:variable name="page_total" select="user_manager/suspense_user_list/search/@total"/>
	<xsl:variable name="page_timestamp" select="user_manager/suspense_user_list/search/@time"/>
	<!-- sorting variable -->
	<xsl:variable name="sort_by" select="user_manager/suspense_user_list/search/@sort_by"/>
	<xsl:variable name="cur_order" select="user_manager/suspense_user_list/search/@order_by"/>
	<xsl:variable name="order_by">
		<xsl:choose>
			<xsl:when test="$cur_order = 'ASC' ">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="parent_id" select="user_manager/suspense_user_list/@id"/>
	<xsl:variable name="parent_role" select="user_manager/suspense_user_list/@grp_role"/>
	<xsl:variable name="cur_usr" select="user_manager/suspense_user_list/cur_usr/@id"/>
	<xsl:variable name="root_ent_id" select="user_manager/suspense_user_list/cur_usr/@root_ent_id"/>
	<xsl:variable name="entity_cnt">
		<xsl:value-of select="count(user_manager/suspense_user_list/user)"/>
	</xsl:variable>
	<xsl:variable name="page_variant" select="/user_manager/meta/page_variant"/>
	<!-- -->
	<xsl:variable name="tmp_page_timestamp" select="translate($page_timestamp,':- ','')"/>
	<xsl:variable name="compare_timestamp_year" select="number(substring($tmp_page_timestamp,1,4))"/>
	<xsl:variable name="compare_timestamp_month" select="number(substring($tmp_page_timestamp,5,2))"/>
	<xsl:variable name="compare_timestamp_day" select="number(substring($tmp_page_timestamp,7,2))-3"/>
	<!-- =============================================================== -->
	<xsl:template match="/user_manager">
		<xsl:apply-templates select="suspense_user_list"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="suspense_user_list">
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
				}
				
				function exec_prep(frm){
					var usr_id_lst_str = '';
					var isMultiple = false;
					if(frm.usr_id_lst){
						if(frm.usr_id_lst.length){
							for(var i = 0;i<frm.usr_id_lst.length;i++){
								if(frm.usr_id_lst[i].checked){
									isMultiple = true;
									usr_id_lst_str += frm.usr_id_lst[i].value + "~";
								}
							}
							if(isMultiple == true){
								usr_id_lst_str = usr_id_lst_str.substr(0, usr_id_lst_str.length - 1);
							}
						}else{
							//列表里只有一个用户
							if(frm.usr_id_lst.checked) {
								usr_id_lst_str = frm.usr_id_lst.value;
							}
						}
					}
					if(usr_id_lst_str == ''){
						alert(wb_msg_pls_sel_usr);
						return;
					}
					
					usr.activate.reactivate_exec(document.frmXml, usr_id_lst_str);
				}
				function search_user_list(frm){
				   frm.user_code.value=wbUtilsTrimString(frm.user_code.value);
				   frm.method = 'get';
				   frm.action = wb_utils_servlet_url;
				   frm.cmd.value = 'get_suspense_usr';				  
				   frm.stylesheet.value='usr_reactivate_lst.xsl';
				   frm.submit();
				}
			]]></script>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			</head>
			<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
				<form onsubmit="return status()" name="frmXml">
					<input type="hidden" name="stylesheet"/>
					<input type="hidden" name="cmd"/>
					<input type="hidden" name="ent_id" value="{$parent_id}"/>
					<input type="hidden" name="ent_id_lst"/>
					<input type="hidden" name="url_success"/>
					<input type="hidden" name="url_failure"/>
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_acc_reactivation">帳號復原</xsl:with-param>
			<xsl:with-param name="lab_acc_reactivation_desc">以下是因多次登錄失敗而被凍結的用戶帳號。請選擇帳號進行復原。</xsl:with-param>
			<xsl:with-param name="lab_reg_date">登記日期</xsl:with-param>
			<xsl:with-param name="lab_not_match">找不到被凍結的帳號</xsl:with-param>
			<xsl:with-param name="lab_desc">以下是你要審批的登記，請按用戶的姓名來批准或拒絕登記。超過三天的登記將標為紅色，以作參考。</xsl:with-param>
			<xsl:with-param name="lab_last_login">最近一次登入日期</xsl:with-param>
			<xsl:with-param name="lab_reactivate">復原</xsl:with-param>
			<xsl:with-param name="lab_search">搜索</xsl:with-param>
			<xsl:with-param name="lab_user_list">用戶列表</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_acc_reactivation">用户激活</xsl:with-param>
			<xsl:with-param name="lab_acc_reactivation_desc">以下是因多次登录失败而被冻结的用户，要激活用户账号请选择对应的用户进行激活。</xsl:with-param>
			<xsl:with-param name="lab_reg_date">登记日期</xsl:with-param>
			<xsl:with-param name="lab_not_match">没有被冻结的用户</xsl:with-param>
			<xsl:with-param name="lab_desc">以下是你要审批的登记，请按用户的姓名来批准或拒绝登记。超过三天的登记将标为红色，以作参考.</xsl:with-param>
			<xsl:with-param name="lab_last_login">最后一次登录日期</xsl:with-param>
			<xsl:with-param name="lab_reactivate">激活</xsl:with-param>
			<xsl:with-param name="lab_search">搜索</xsl:with-param>
			<xsl:with-param name="lab_user_list">用户列表</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_acc_reactivation">Account reactivation</xsl:with-param>
			<xsl:with-param name="lab_acc_reactivation_desc">The following user account(s) are suspended due to multiple failed login attempts. You can reactivate the user account here.</xsl:with-param>
			<xsl:with-param name="lab_reg_date">Registration date</xsl:with-param>
			<xsl:with-param name="lab_not_match">No suspended account found.</xsl:with-param>
			<xsl:with-param name="lab_desc">Listed below are the registrations pending your approval. Click the name of the user to approve or decline the registration. Registrations that are open for more than 3 days are marked in red for your reference.</xsl:with-param>
			<xsl:with-param name="lab_last_login">Last login</xsl:with-param>
			<xsl:with-param name="lab_reactivate">Reactivate</xsl:with-param>		
			<xsl:with-param name="lab_search">search</xsl:with-param>
			<xsl:with-param name="lab_user_list">User List</xsl:with-param>	
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_acc_reactivation"/>
		<xsl:param name="lab_reg_date"/>
		<xsl:param name="lab_not_match"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_last_login"/>
		<xsl:param name="lab_acc_reactivation_desc"/>
		<xsl:param name="lab_reactivate"/>
		<xsl:param name="lab_search"/>
		<xsl:param name="lab_user_list"/>
		
		<!-- heading -->
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_USR_REGIETER_APP</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_acc_reactivation"/>
		</xsl:call-template>
		<!--  
		<xsl:call-template name="usr_gen_tab">
			<xsl:with-param name="usr_target_tab">3</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_space"/>-->
		<table>
			<tr>
				<td  width="60%">
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text" select="$lab_acc_reactivation_desc"/>
				</xsl:call-template>
				<!-- <div style="margin-top: -8px;">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="$lab_user_list"/>
					</xsl:call-template>
				</div> -->
				</td>
				<td align="right" width="40%">
					<div class="wzb-form-search">
						<input type="text" class="form-control" name="user_code" style="width:110px;" onkeypress="" value="{$search_code}"/>
						<input type="button" class="form-submit margin-right4" value="" onclick="search_user_list(document.frmXml)"/>
						<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-blue</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_reactivate"/></xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:exec_prep(document.frmXml)</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>
					</div>
				</td>
			</tr>
		</table>
		<xsl:choose>
			<xsl:when test="$entity_cnt &gt;= 1">
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td width="25%">
							<span>
								<xsl:call-template name="select_all_checkbox">
									<xsl:with-param name="chkbox_lst_cnt"><xsl:value-of select="$entity_cnt"/></xsl:with-param>
									<xsl:with-param name="display_icon">false</xsl:with-param>
									<xsl:with-param name="sel_all_chkbox_nm">sel_all_checkbox</xsl:with-param>
									<xsl:with-param name="frm_name">frmXml</xsl:with-param>
								</xsl:call-template>
							</span>
							<xsl:value-of select="$lab_login_id"/>
						</td>												
						<td width="25%">
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
									<a href="javascript:usr.activate.reactivate_lst({$cur_page},{$page_size},'usr_display_bil','{$order_by}','{$page_timestamp}')">
										<xsl:value-of select="$lab_dis_name"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path"  select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:usr.register.reg_usr_approval_lst({$cur_page},{$page_size},'usr_display_bil','{$order_by}','{$page_timestamp}')">
										<xsl:value-of select="$lab_dis_name"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td width="25%">
							<xsl:value-of select="$lab_group"/>
						</td>						
						<td width="25%" align="center">
							<xsl:value-of select="$lab_last_login"/>
						</td>
					</tr>
					<xsl:apply-templates select="user"/>
				</table>
		<xsl:call-template name="wb_ui_pagination">
			<xsl:with-param name="cur_page" select="$cur_page"/>
			<xsl:with-param name="page_size" select="$page_size"/>
			<xsl:with-param name="page_size_name">pagesize</xsl:with-param>
			<xsl:with-param name="cur_page_name">page</xsl:with-param>
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
				<span>
					<input type="checkbox" name="usr_id_lst" value="{@ent_id}"/>
				</span>
				<a href="javascript:usr.activate.reactivate_prep({@ent_id})" class="Text">
					<xsl:value-of select="@id"/>
				</a>
			</td>			
			<td >
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
			<td align="center">
				<xsl:call-template name="display_time">
					<xsl:with-param name="my_timestamp">
						<xsl:value-of select="@last_login"/>
					</xsl:with-param>
					<xsl:with-param name="dis_time">T</xsl:with-param>
				</xsl:call-template>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="cur_usr"/>
	<!-- =============================================================== -->
</xsl:stylesheet>
