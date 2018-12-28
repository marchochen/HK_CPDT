<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
    <!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="share/usr_gen_tab_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/function_map_share.xsl"/>
	<xsl:variable name="ent_id" select="/role_function/meta/cur_usr/@ent_id"/>
	<!-- ================================================================== -->
	<xsl:template match="/role_function">
		<xsl:call-template name="main"/>
	</xsl:template>
	<!--=================================================================== -->
	<xsl:template name="main">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
                <script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_rol_mgt.js"/>
			    <script language="JavaScript"><![CDATA[
			        rol_mgt = new wbRoleManager;
			        
			        var tc_related_function_id_lst = eval("]]><xsl:value-of select="java:com.cw.wizbank.util.RoleManager.get_tc_related_function_id_lst()"/><![CDATA[");
			        function set_function_disabled(disabled){
			           if(disabled){
			              for(var n=0; n<tc_related_function_id_lst.length; n++){
			                 document.getElementById(tc_related_function_id_lst[n]).disabled = false;
			              }
			           }else{
			              for(var n=0; n<tc_related_function_id_lst.length; n++){
			                 var processInputElement = document.getElementById(tc_related_function_id_lst[n]);
			                 processInputElement.disabled = true;
			                 processInputElement.checked = false;
			              }
			           }
			        }
			    ]]></script>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
				<form name="frmAction" class="wzb-form">
				    <input type="hidden" name="ftn_id_lst"/>
				    <input type="hidden" name="ent_id" value="{$ent_id}"/>
					<input type="hidden" name="module"/>
					<input type="hidden" name="cmd"/>
					<input type="hidden" name="url_success"/>
					<input type="hidden" name="url_failure"/>
					<input type="hidden" name="stylesheet"/>
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- ========================================================================== -->
	<xsl:template name="lang_ch">
	  <xsl:call-template name="content">
		    <xsl:with-param name="lab_add_role_title">新增角色</xsl:with-param>
		    <xsl:with-param name="lab_role_code">編號</xsl:with-param>
		    <xsl:with-param name="lab_role_name">名稱</xsl:with-param>
		    <xsl:with-param name="lab_g_form_btn_ok">確認</xsl:with-param>
		    <xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		    <xsl:with-param name="lab_role_fun">具有權限</xsl:with-param>
		    <xsl:with-param name="lab_ass_tc">與培訓中心相關</xsl:with-param>
		    <xsl:with-param name="lab_yes">是</xsl:with-param>
		    <xsl:with-param name="lab_no">否</xsl:with-param>
		    <xsl:with-param name="lab_tc_desc">註: 粗體顯示的功能與培訓中心有關，目前角色設定該類功能後必須在培訓中心管理中指定該角色才可以正常使用。</xsl:with-param>
	  </xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
            <xsl:with-param name="lab_add_role_title">新增角色</xsl:with-param>
		    <xsl:with-param name="lab_role_code">编号</xsl:with-param>
		    <xsl:with-param name="lab_role_name">名称</xsl:with-param>
		    <xsl:with-param name="lab_g_form_btn_ok">确认</xsl:with-param>
		    <xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		    <xsl:with-param name="lab_role_fun">具有权限</xsl:with-param>
		    <xsl:with-param name="lab_ass_tc">与培训中心关联</xsl:with-param>
		    <xsl:with-param name="lab_yes">是</xsl:with-param>
		     <xsl:with-param name="lab_no">否</xsl:with-param>
		     <xsl:with-param name="lab_tc_desc">注：粗体显示的功能是与培训中心相关的，当前角色指定该类功能后必须在培训中心管理中指定该角色才可以正常使用。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
            <xsl:with-param name="lab_add_role_title">Add role</xsl:with-param>
		    <xsl:with-param name="lab_role_code">Code</xsl:with-param>
		    <xsl:with-param name="lab_role_name">Name</xsl:with-param>
		    <xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
		    <xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		    <xsl:with-param name="lab_role_fun">Function(s)</xsl:with-param>
		    <xsl:with-param name="lab_ass_tc">Associated with training center</xsl:with-param>
		    <xsl:with-param name="lab_yes">Yes</xsl:with-param>
		    <xsl:with-param name="lab_no">No</xsl:with-param>
		    <xsl:with-param name="lab_tc_desc">Note: The function options in bold is associated with training centers, the role assigned with such function(s) must be specified in Training Center Management so as to use the function(s) properly.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template name="content">
		<xsl:param name="lab_add_role_title"/>
		<xsl:param name="lab_role_code"/>
	    <xsl:param name="lab_role_name"/>
	    <xsl:param name="lab_g_form_btn_ok"/>
	    <xsl:param name="lab_g_form_btn_cancel"/>
        <xsl:param name="lab_role_fun"/>
        <xsl:param name="lab_ass_tc"/>
	    <xsl:param name="lab_yes"/>
        <xsl:param name="lab_no"/>
        <xsl:param name="lab_tc_desc"/>
        		 
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_SYS_ROLE_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">	
				<xsl:value-of select="$lab_add_role_title"/>
			</xsl:with-param>
		 </xsl:call-template>
		 <xsl:call-template name="wb_ui_space">
			<xsl:with-param name="width" select="$wb_gen_table_width"/>
		 </xsl:call-template>
		 <!--detail table-->
		 <xsl:call-template name="draw_detail_lst">
		<xsl:with-param name="lab_role_code" select="$lab_role_code"/>
	    <xsl:with-param name="lab_role_name" select="$lab_role_name"/>
	    <xsl:with-param name="lab_g_form_btn_ok" select="$lab_g_form_btn_ok"/>
	    <xsl:with-param name="lab_g_form_btn_cancel" select="$lab_g_form_btn_cancel"/>
        <xsl:with-param name="lab_role_fun" select="$lab_role_fun"/>
        <xsl:with-param name="lab_ass_tc" select="$lab_ass_tc"/>
        <xsl:with-param name="lab_yes" select="$lab_yes"/>
        <xsl:with-param name="lab_no" select="$lab_no"/>
        <xsl:with-param name="lab_tc_desc" select="$lab_tc_desc"></xsl:with-param>	
		 </xsl:call-template>
		 <!--end detail table-->
		 <div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
				    <xsl:value-of select="$lab_g_form_btn_ok"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:rol_mgt.add_rol_exec(frmAction,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
				</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
				    <xsl:value-of select="$lab_g_form_btn_cancel"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:rol_mgt.cnl_add_rol()</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template name="draw_detail_lst">
		<xsl:param name="lab_role_code"/>
	    <xsl:param name="lab_role_name"/>
	    <xsl:param name="lab_g_form_btn_ok"/>
	    <xsl:param name="lab_g_form_btn_cancel"/>
        <xsl:param name="lab_role_fun"/>
        <xsl:param name="lab_ass_tc"/>
	    <xsl:param name="lab_yes"/>
        <xsl:param name="lab_no"/>
        <xsl:param name="lab_tc_desc"/>
		<table>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_role_name"/>:
				</td>
				<td class="wzb-form-control">
					<input class="wzb-inputText" type="text" name="rol_title" size="25" style="width:150px;" maxlength="25" />
				</td>
			</tr> 
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_ass_tc"/>:
				</td>
				<td class="wzb-form-control">
					<table>
						<tr>
							<label for="yes">
							<td width="2%">
								<input type="radio" name="rol_tc_ind" value="1" id="yes" checked="checked" onclick="set_function_disabled(true)"/>
							</td>
							<td width="98%" align="left">
								<xsl:value-of select="$lab_yes"/>
							</td>
							</label>
						</tr>
						<tr>
							<label for="no">
								<td width="2%">
									<input type="radio" name="rol_tc_ind" value="0" id="no" onclick="set_function_disabled(false)"/>
								</td>
								<td width="98%" align="left">
									<xsl:value-of select="$lab_no"/>
								</td>
							</label>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
			    <td class="wzb-form-label" valign="top" height="14">
				    <span class="wzb-form-star">*</span><xsl:value-of select="$lab_role_fun"/>:
			    </td>
			    <td class="wzb-form-control">
					<table>
						<xsl:apply-templates select="function_map/homepage_function"/>
					</table>
			    </td>
		    </tr>
			<tr>
			   <table>
				<tr>
				    <td width="20%" align="right" valign="top" height="14">
					  
				    </td>
				    <td width="80%">
						<table>
							<xsl:apply-templates select="function_map/other_functions"/>
						</table>
				    </td>
				</tr>
				</table>
		    </tr>
			<tr>
				<table>
					<tr>
						<td class="wzb-form-label">
						</td>
						<td class="wzb-ui-module-text">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
						</td>
						<td class="wzb-form-control">
							<xsl:value-of select="$lab_tc_desc"/>
						</td>
					</tr>					
				</table>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>