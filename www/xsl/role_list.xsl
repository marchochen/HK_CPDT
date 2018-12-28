<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="order_by" select="/role_function/role_list/pagination/@sort_col"/>
	<xsl:variable name="sort_order" select="/role_function/role_list/pagination/@sort_order"/>
	<xsl:variable name="order">
		<xsl:choose>
			<xsl:when test="$sort_order = 'asc' or $sort_order = 'ASC' ">desc</xsl:when>
			<xsl:otherwise>asc</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="page_variant" select="/role_function/meta/page_variant"/>
	<xsl:variable name="page_size" select="/role_function/role_list/pagination/@page_size"/>
	<xsl:variable name="cur_page" select="/role_function/role_list/pagination/@cur_page"/>
	<xsl:variable name="total" select="/role_function/role_list/pagination/@total_rec"/>
	<!-- =============================================================== -->
	<xsl:template match="/role_function">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
		    <script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_rol_mgt.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
			    rol_mgt = new wbRoleManager
			    window.onload= function(){
			    	if(!document.frmAction.rol_id_lst){
			    		document.getElementById('sel_all').style.display = 'none';
			    	}
			    }				
			    ]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
				<form name="frmAction" >
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="rol_id"/>
				<input type="hidden" name="module"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="url_success"/>
				<xsl:call-template name="wb_init_lab"/>
		       </form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_cat_main">課程目錄</xsl:with-param>
			<xsl:with-param name="lab_cos_evn_main">課程評估問卷管理</xsl:with-param>
			<xsl:with-param name="lab_rpt_link">報告查看</xsl:with-param>
			<xsl:with-param name="lab_modified_date">修改日期</xsl:with-param>
			<xsl:with-param name="lab_for_link">論壇</xsl:with-param>
			<xsl:with-param name="lab_glb_rpt_link">报告管理</xsl:with-param>
			<xsl:with-param name="lab_home">首頁</xsl:with-param>
			<xsl:with-param name="lab_itm_main">課程管理</xsl:with-param>
			<xsl:with-param name="lab_run_main">班別管理</xsl:with-param>
			<xsl:with-param name="lab_lrn_blp_admin">學習藍圖管理</xsl:with-param>
			<xsl:with-param name="lab_lrn_res_main">资源管理</xsl:with-param>
			<xsl:with-param name="lab_usr_info_main">用戶管理</xsl:with-param>
			<xsl:with-param name="lab_tc_mgt">培訓中心管理</xsl:with-param>
			<xsl:with-param name="lab_code_data">參考資料管理</xsl:with-param>
			<xsl:with-param name="lab_sso_link">單點登錄鏈結查詢</xsl:with-param>
			<xsl:with-param name="lab_lrn_calendar">培訓行事曆</xsl:with-param>
			<xsl:with-param name="lab_ann_man">公告管理</xsl:with-param>
			<xsl:with-param name="lab_code">編號</xsl:with-param>
			<xsl:with-param name="lab_role_creator">創造者</xsl:with-param>
			<xsl:with-param name="lab_role_name">角色名稱</xsl:with-param>
			<xsl:with-param name="lab_role_Management">角色管理</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_role">新增</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del_role">刪除</xsl:with-param>
		    <xsl:with-param name="lab_ass_tc">與培訓中心相關</xsl:with-param>
		    <xsl:with-param name="lab_yes">是</xsl:with-param>
		    <xsl:with-param name="lab_no">否</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content"> 
			<xsl:with-param name="lab_cat_main">课程目录</xsl:with-param>
			<xsl:with-param name="lab_cos_evn_main">课程评估问卷管理</xsl:with-param>
			<xsl:with-param name="lab_rpt_link">报告查看</xsl:with-param>
			<xsl:with-param name="lab_modified_date">修改日期</xsl:with-param>
			<xsl:with-param name="lab_for_link">论坛</xsl:with-param>
			<xsl:with-param name="lab_glb_rpt_link">报告管理</xsl:with-param>
			<xsl:with-param name="lab_home">首页</xsl:with-param>
			<xsl:with-param name="lab_itm_main">课程管理</xsl:with-param>
			<xsl:with-param name="lab_run_main">班级管理</xsl:with-param>
			<xsl:with-param name="lab_lrn_blp_admin">学习蓝图管理</xsl:with-param>
			<xsl:with-param name="lab_lrn_res_main">资源管理</xsl:with-param>
			<xsl:with-param name="lab_usr_info_main">用户管理</xsl:with-param>
			<xsl:with-param name="lab_tc_mgt">培训中心管理</xsl:with-param>
			<xsl:with-param name="lab_code_data">参考数据管理</xsl:with-param>
			<xsl:with-param name="lab_sso_link">单点登录链接查询</xsl:with-param>
			<xsl:with-param name="lab_lrn_calendar">培训行事历</xsl:with-param>
			<xsl:with-param name="lab_ann_man">公告管理</xsl:with-param>
			<xsl:with-param name="lab_code">编号</xsl:with-param>
			<xsl:with-param name="lab_role_creator">创建者</xsl:with-param>
			<xsl:with-param name="lab_role_name">角色名称</xsl:with-param>
			<xsl:with-param name="lab_role_Management">角色管理</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_role">添加</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del_role">删除</xsl:with-param>
		    <xsl:with-param name="lab_ass_tc">与培训中心关联</xsl:with-param>
		    <xsl:with-param name="lab_yes">是</xsl:with-param>
		     <xsl:with-param name="lab_no">否</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_cat_main">Learning catalog</xsl:with-param>
			<xsl:with-param name="lab_cos_evn_main">Course evaluation builder</xsl:with-param>
			<xsl:with-param name="lab_rpt_link">Management report</xsl:with-param>
			<xsl:with-param name="lab_modified_date">Modified date</xsl:with-param>
			<xsl:with-param name="lab_for_link">Discussion forum</xsl:with-param>
			<xsl:with-param name="lab_glb_rpt_link">Global management report</xsl:with-param>
			<xsl:with-param name="lab_home">Home</xsl:with-param>
			<xsl:with-param name="lab_itm_main">Learning solution management</xsl:with-param>
			<xsl:with-param name="lab_run_main">Class management</xsl:with-param>
			<xsl:with-param name="lab_lrn_blp_admin">Learning roadmap management</xsl:with-param>
			<xsl:with-param name="lab_lrn_res_main">Learning resource management</xsl:with-param>
			<xsl:with-param name="lab_usr_info_main">User management</xsl:with-param>
			<xsl:with-param name="lab_tc_mgt">Training center management</xsl:with-param>
			<xsl:with-param name="lab_code_data">Reference data management</xsl:with-param>
			<xsl:with-param name="lab_sso_link">SSO link query</xsl:with-param>
			<xsl:with-param name="lab_lrn_calendar">Training calendar</xsl:with-param>
			<xsl:with-param name="lab_ann_man">Announcement management</xsl:with-param>
			<xsl:with-param name="lab_code">Code</xsl:with-param>
			<xsl:with-param name="lab_role_creator">Creator</xsl:with-param>
			<xsl:with-param name="lab_role_name">Role name</xsl:with-param>
			<xsl:with-param name="lab_role_Management">Role management</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_role">Add role</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del_role">Delete role</xsl:with-param>
		    <xsl:with-param name="lab_ass_tc">Associated with training center</xsl:with-param>
		    <xsl:with-param name="lab_yes">Yes</xsl:with-param>
		    <xsl:with-param name="lab_no">No</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_cat_main"/>
        <xsl:param name="lab_cos_evn_main"/>
		<xsl:param name="lab_rpt_link"/>
		<xsl:param name="lab_modified_date"/>
		<xsl:param name="lab_for_link"/>
		<xsl:param name="lab_glb_rpt_link"/>
		<xsl:param name="lab_home"/>
		<xsl:param name="lab_itm_main"/>
		<xsl:param name="lab_run_main"/>
		<xsl:param name="lab_lrn_blp_admin"/>
		<xsl:param name="lab_lrn_res_main"/>
		<xsl:param name="lab_usr_info_main"/>
		<xsl:param name="lab_tc_mgt"/>
		<xsl:param name="lab_code_data"/>
		<xsl:param name="lab_sso_link"/>
		<xsl:param name="lab_lrn_calendar"/>
		<xsl:param name="lab_ann_man"/>
		<xsl:param name="lab_code"/>
		<xsl:param name="lab_role_creator"/>
		<xsl:param name="lab_role_name"/>
		<xsl:param name="lab_role_Management"/>
		<xsl:param name="lab_g_txt_btn_add_role"/>
		<xsl:param name="lab_g_txt_btn_del_role"/>
	    <xsl:param name="lab_ass_tc"/>
	    <xsl:param name="lab_yes"/>
        <xsl:param name="lab_no"/>	
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_SYS_ROLE_MAIN</xsl:with-param>
		</xsl:call-template>
        <xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">			
				<xsl:value-of select="$lab_role_Management"/>
			</xsl:with-param>
		</xsl:call-template>
	
		<!-- add & delete role -->
		<table>
			<tr>
				<td>
					<table>
						<tr>
							<td>
								<input type="hidden" size="11" name="title_code"  style="width:130px;"/>
							</td>
							<td align="right">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_add_role"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:rol_mgt.add_rol()</xsl:with-param>
								</xsl:call-template>
								
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_del_role"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:rol_mgt.del_rol_checkbox(document.frmAction,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
								</xsl:call-template>
							</td>	
						</tr>
					</table>
				</td>
			</tr>
		</table>		
		<!-- role list -->
		<xsl:apply-templates select="role_list">
		    <xsl:with-param name="lab_code" select="$lab_code"/>
			<xsl:with-param name="lab_role_creator" select="$lab_role_creator"/>
			<xsl:with-param name="lab_role_name" select="$lab_role_name"/>
			<xsl:with-param name="lab_modified_date" select="$lab_modified_date"/>
		    <xsl:with-param name="lab_ass_tc" select="$lab_ass_tc"/>
			<xsl:with-param name="lab_yes" select="$lab_yes"/>
			<xsl:with-param name="lab_no" select="$lab_no"/>	
		</xsl:apply-templates>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- =============================================================== -->

	<xsl:template match="role_list">
        <xsl:param name="lab_code"/>
		<xsl:param name="lab_role_creator"/>
		<xsl:param name="lab_role_name"/>
		<xsl:param name="lab_modified_date"/>
		<xsl:param name="lab_ass_tc"/>
		<xsl:param name="lab_yes"/>
        <xsl:param name="lab_no"/>	
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td>
							<xsl:call-template name="select_all_checkbox">
								<xsl:with-param name="chkbox_lst_cnt">
									<xsl:value-of select="count(role)"/>
								</xsl:with-param>
								<xsl:with-param name="display_icon">false</xsl:with-param>
								<xsl:with-param name="sel_all_chkbox_nm">sel_all_checkbox</xsl:with-param>
								<xsl:with-param name="frm_name">frmAction</xsl:with-param>
							</xsl:call-template>
						</td>
						<td width="25%">
							<xsl:choose>
								<xsl:when test="$order_by = 'rol_title'">
									<a href="javascript:rol_mgt.get_rol_list('rol_title','{$order}','{$cur_page}','{$page_size}')">
										<xsl:value-of select="$lab_role_name"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$sort_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:rol_mgt.get_rol_list('rol_title','asc','{$cur_page}','{$page_size}')">
										<xsl:value-of select="$lab_role_name"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td width="25%">
							<a>
								<xsl:value-of select="$lab_ass_tc"/>
							</a>
						</td>
						<td width="25%" align="center">
						    <a>
								<xsl:value-of select="$lab_role_creator"/>
							</a>
						</td>
					
						<td width="25%" align="center">
							<xsl:choose>
								<xsl:when test="$order_by = 'rol_update_timestamp' ">
									<a href="javascript:rol_mgt.get_rol_list('rol_update_timestamp','{$order}','{$cur_page}','{$page_size}')">
										<xsl:value-of select="$lab_modified_date"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path" select="$wb_img_path"/>
												<xsl:with-param name="sort_order" select="$sort_order"/>
											</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:rol_mgt.get_rol_list('rol_update_timestamp','asc','{$cur_page}','{$page_size}')">
										<xsl:value-of select="$lab_modified_date"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>	
						</td>
					</tr>
					<xsl:for-each select="role">
						<xsl:variable name="row_class">
							<xsl:choose>
								<xsl:when test="position() mod 2">RowsEven</xsl:when>
								<xsl:otherwise>RowsOdd</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<tr>
							<xsl:call-template name="role_list">
								<xsl:with-param name="lab_yes" select="$lab_yes"/>
								<xsl:with-param name="lab_no" select="$lab_no"/>
						    </xsl:call-template>
						</tr>
					</xsl:for-each>
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="total" select="$total"/>
				</xsl:call-template>
	    </xsl:template>
		<xsl:template name="role_list">
		<xsl:param name="lab_yes"/>
        <xsl:param name="lab_no"/>	
		    <td>
				<xsl:if test="@rol_type != 'SYSTEM'">
					<input type="checkbox" name="rol_id_lst" value="{@id}"/>
				</xsl:if>
			</td>
		    <td>
				<xsl:choose>
				    <xsl:when test="@rol_type='SYSTEM'"><xsl:value-of select="role_title/@name"/></xsl:when>
				    <xsl:otherwise><a href="javascript:rol_mgt.get_rol_detail({@id})" class="Text"><xsl:value-of select="role_title/@name"/></a></xsl:otherwise>
			    </xsl:choose>
		    </td>
		    <td>
		       <xsl:choose>
					<xsl:when test="@rol_tc_ind = 'true'">
						<xsl:value-of select="$lab_yes"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_no"/>
					</xsl:otherwise>
				</xsl:choose> 
		    </td>
		    <td align="center">
			    <xsl:value-of select="@rol_creator"/>
		    </td>
			<td align="center">
		    	<xsl:call-template name="display_time">
					<xsl:with-param name="my_timestamp">
						<xsl:value-of select="@modified_date"/>
					</xsl:with-param>
				</xsl:call-template>   
			</td>
		</xsl:template>
</xsl:stylesheet>
