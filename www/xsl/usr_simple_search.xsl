<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<!-- usr utils -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/usr_search_form_share.xsl"/>
	<xsl:import href="share/label_role.xsl"/>
	
	<xsl:variable name="wb_gen_table_width" select="510"/>
	<xsl:variable name="filter_user_group">
		<xsl:choose>
			<xsl:when test="//filter_user_group_ind = 'true'">1</xsl:when>
			<xsl:otherwise>0</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
<xsl:output indent="yes"/>

<!-- =============================================================== -->
<xsl:template match="/user_manager">
	<html><xsl:call-template name="wb_init_lab"/></html>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="lang_ch">
	<xsl:call-template name="content">
		<xsl:with-param name="lab_usr_search">用戶檢索</xsl:with-param>
		<xsl:with-param name="lab_search_inst">請選擇用戶查詢標準:</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		<xsl:with-param name="lab_legend">要替學員報讀課程，請指定以下的資料以便搜尋用戶戶口。要避免重覆報名，已申請並等候批核或正在輪候中的學員名單會自動從搜尋庫中剔刪。</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<xsl:template name="lang_gb">
	<xsl:call-template name="content">
		<xsl:with-param name="lab_usr_search">用户搜索</xsl:with-param>
		<xsl:with-param name="lab_search_inst">请选择用户搜索条件。</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		<xsl:with-param name="lab_legend">要替学员报读课程，请指定以下的资料以便搜寻用户户口。要避免重覆报名，已申请并等候批核或正在轮候中的学员名单会自动从搜寻库中剔删。</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<xsl:template name="lang_en">
	<xsl:call-template name="content">
		<xsl:with-param name="lab_usr_search">User search</xsl:with-param>
		<xsl:with-param name="lab_search_inst">Please specify the search criteria below:</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		<xsl:with-param name="lab_legend">To enroll learners to the course, find out their user accounts by specifying their information below. To avoid repeated enrollments, learners who are applying the same course with pending approval or waitlisted status will be automatically excluded from the search.</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="content">
	<xsl:param name="lab_usr_search"/>
	<xsl:param name="lab_search_inst"/>
	<xsl:param name="lab_g_form_btn_ok"/>
	<xsl:param name="lab_g_form_btn_cancel"/>
	<xsl:param name="lab_legend"/>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		<title><xsl:value-of select="$wb_wizbank"/></title>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
		<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
		
		<!--alert样式  -->
	    <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
		
		<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
		<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
		<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
		
		<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
		<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
		<!--alert样式  end -->
		<script language="JavaScript" type="text/javascript"><![CDATA[
		usr = new wbUserGroup;
		var goldenman = new wbGoldenMan;
		function status(){
			
			return false;
		}

		function alive() { return true; }

	]]></script>
	<xsl:call-template name="new_css"/>
	</head>
	<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onLoad="wb_utils_gen_form_focus(document.frmXml);">
		<form name="frmXml" method="post" onsubmit="usr.search.popup_search_exec(document.frmXml,'{$wb_lang}');return status()">
			<input type="hidden" name="cmd" value=""/>
			<input type="hidden" name="ent_id" value="{/group_member_list/@id}"/>
			<input type="hidden" name="s_role_types" value=""/>
			<input type="hidden" name="stylesheet" value=""/>
			<input type="hidden" name="fld" value=""/>
			<input type="hidden" name="sel_opt" value=""/>
			<input type="hidden" name="close_opt" value=""/>
			<input type="hidden" name="s_itm_id" value=""/>
			<input type="hidden" name="s_search_enrolled" value=""/>
			<input type="hidden" name="s_search_role" value=""/>
			<input type="hidden" name="refresh_opt" value=""/>
			<input type="hidden" name="disabled_opt" value=""/>
			<input type="hidden" name="s_ftn_ext_id" value=""/>
			<input type="hidden" name="s_instr" value=""/>
			<input type="hidden" name="s_tcr_id" value=""/>
			<input type="hidden" name="auto_enroll_ind" value=""/>
			<input type="hidden" name="filter_user_group" value=""/>
			
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text_class" >pop-up-title</xsl:with-param>
				<xsl:with-param name="text" select="$lab_usr_search"/>
			</xsl:call-template>
			
			<xsl:call-template name="wb_ui_desc">
				<xsl:with-param name="text">
							<script language="javascript" type="text/javascript"><![CDATA[
							str = ']]><xsl:value-of select="$lab_legend"/><![CDATA['
							str2 = ']]><xsl:value-of select="$lab_search_inst"/><![CDATA['
			if (getUrlParam('s_itm_id')!='' && getUrlParam('s_itm_id')!=null) {
				document.write(str);
			}else{
				document.write(str2);
			}
		]]></script>

				</xsl:with-param>
			</xsl:call-template>
			<table> 
				<xsl:apply-templates select="$profile_attributes/*[(not(@active) or @active = 'true') and @searchable = 'all']" mode="profile_attributes"/>
			</table>
			<div class="wzb-bar">
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:usr.search.popup_search_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
				</xsl:call-template>				
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:window.close()</xsl:with-param>
				</xsl:call-template>
			</div>
			<script type="text/javascript" language="JavaScript">
								str='<input type="submit" value="" size="0" style="height : 0px;width : 0px; visibility: hidden;"/>'
								if (document.all || document.getElementById!=null){
									document.write(str);
								}
			</script>			
		</form>
	</body>
</xsl:template>
<!-- =============================================================== -->
<xsl:template match="group_member_list/all_role_list/role">
	<xsl:param name="mode"/>
	<xsl:variable name="cur_role">
		<xsl:value-of select="@id"/>
	</xsl:variable>
	<xsl:if test="not(position() mod 2 = 0)">
		<xsl:text disable-output-escaping="yes">&lt;tr&gt;</xsl:text>
	</xsl:if>
	<td> 
		<input type="checkbox" name="usr_role" value="{@id}"/>
		<xsl:text>&#160;</xsl:text>
		<xsl:choose>
			<xsl:when test="$mode = 'js'">
				<xsl:call-template name="escape_js">
					<xsl:with-param name="input_str">
						<xsl:call-template name="get_rol_title"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="get_rol_title"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:text>&#160;</xsl:text>
	</td>
	<xsl:choose>
		<xsl:when test="position() mod 2 = 0">
			<xsl:text disable-output-escaping="yes">&lt;/tr&gt;</xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<td width="10">
				<xsl:text>&#160;</xsl:text>
			</td>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>
<!-- =============================================================== -->
</xsl:stylesheet>