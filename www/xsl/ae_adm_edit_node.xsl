<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>

<xsl:import href="wb_const.xsl"/>
<xsl:import href="cust/wb_cust_const.xsl"/><xsl:import href="utils/wb_utils.xsl"/>
<xsl:output indent="yes"/>
<!-- =============================================================== -->
<xsl:variable name="creator" select="/applyeasy/node/creator/@usr_id"/>
<!-- =============================================================== -->
<xsl:template match="/">
	<xsl:apply-templates select="applyeasy"/>
</xsl:template>
<!-- =============================================================== -->
<xsl:template match="applyeasy">
	<html><xsl:call-template name="main"/></html>
</xsl:template>
<!-- ============================================================= -->
<xsl:template name="main">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		<TITLE><xsl:value-of select="$wb_wizbank"/></TITLE>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_cata_lst.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
		<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
		<script language="JavaScript"><![CDATA[	
			cata_lst = new wbCataLst
			var goldenman = new wbGoldenMan
			function status(){return false;}
		]]></script>
	</head>
	<BODY leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="document.frmAction.tnd_title.focus(); ">
		<FORM name="frmAction" onsubmit="return status()">	
			<xsl:call-template name="wb_init_lab"/>
		</FORM>
	</BODY>
</xsl:template>
<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_cata">全目錄</xsl:with-param>
			<xsl:with-param name="lab_add_node">新增子目錄</xsl:with-param>
			<xsl:with-param name="lab_edit_node">修改子目錄</xsl:with-param>
			<xsl:with-param name="lab_node_nm">子目錄名稱</xsl:with-param>
			<xsl:with-param name="lab_create_node">新子目錄</xsl:with-param>
			<xsl:with-param name="lab_link_public">連結至共享目錄</xsl:with-param>
			<xsl:with-param name="lab_pick">選取</xsl:with-param>
			<xsl:with-param name="lab_link">連結</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_status_on">已發佈</xsl:with-param>
			<xsl:with-param name="lab_status_off">未發佈</xsl:with-param>
			<xsl:with-param name="lab_create_method">製作方法</xsl:with-param>
			<xsl:with-param name="lab_node_desc">描述</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_desc_limit">(不超過2000個字元(1000個中文字))</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_cata">课程目录</xsl:with-param>
			<xsl:with-param name="lab_add_node">添加子目录</xsl:with-param>
			<xsl:with-param name="lab_edit_node">修改子目录</xsl:with-param>
			<xsl:with-param name="lab_node_nm">子目录名称</xsl:with-param>
			<xsl:with-param name="lab_create_node">新子目录</xsl:with-param>
			<xsl:with-param name="lab_link_public">连结至共享目录</xsl:with-param>
			<xsl:with-param name="lab_pick">选取</xsl:with-param>
			<xsl:with-param name="lab_link">连结</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_status_on">已发布</xsl:with-param>
			<xsl:with-param name="lab_status_off">未发布</xsl:with-param>
			<xsl:with-param name="lab_create_method">制作方法</xsl:with-param>
			<xsl:with-param name="lab_node_desc">描述</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_desc_limit">(不超过2000个字符(1000个中文字))</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_cata">Learning catalogs</xsl:with-param>
			<xsl:with-param name="lab_add_node">Add sub-catalog</xsl:with-param>
			<xsl:with-param name="lab_edit_node">Edit subcategory</xsl:with-param>
			<xsl:with-param name="lab_node_nm">Name</xsl:with-param>
			<xsl:with-param name="lab_create_node">Create a new category</xsl:with-param>
			<xsl:with-param name="lab_link_public">Link to existing category</xsl:with-param>
			<xsl:with-param name="lab_pick">pick</xsl:with-param>
			<xsl:with-param name="lab_link">Link</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_status_on">Published</xsl:with-param>
			<xsl:with-param name="lab_status_off">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_create_method">Creation method</xsl:with-param>
			<xsl:with-param name="lab_node_desc">Description</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_desc_limit">(Not more than 2000 characters)</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
<!-- =============================================================== -->
<xsl:template name="content">
	<xsl:param name="lab_all_cata"/>
	<xsl:param name="lab_add_node"/>
	<xsl:param name="lab_edit_node"/>
	<xsl:param name="lab_node_nm"/>
	<xsl:param name="lab_create_node"/>
	<xsl:param name="lab_link_public"/>
	<xsl:param name="lab_pick"/>
	<xsl:param name="lab_link"/>
	<xsl:param name="lab_status"/>
	<xsl:param name="lab_status_on"/>
	<xsl:param name="lab_status_off"/>
	<xsl:param name="lab_create_method"/>
	<xsl:param name="lab_node_desc"/>
	<xsl:param name="lab_g_form_btn_ok"/>
	<xsl:param name="lab_g_form_btn_cancel"/>
	<xsl:param name="lab_desc_limit"/>
	
	<xsl:call-template name="header">		
		<xsl:with-param name="lab_all_cata"><xsl:value-of select="$lab_all_cata"/></xsl:with-param>
		<xsl:with-param name="lab_add_node"><xsl:value-of select="$lab_add_node"/></xsl:with-param>
		<xsl:with-param name="lab_edit_node"><xsl:value-of select="$lab_edit_node"/></xsl:with-param>
	</xsl:call-template>
	<xsl:apply-templates select="node">
		<xsl:with-param name="lab_all_cata"><xsl:value-of select="$lab_all_cata"/></xsl:with-param>
		<xsl:with-param name="lab_node_nm"><xsl:value-of select="$lab_node_nm"/></xsl:with-param>
		<xsl:with-param name="lab_create_node"><xsl:value-of select="$lab_create_node"/></xsl:with-param>
		<xsl:with-param name="lab_link_public"><xsl:value-of select="$lab_link_public"/></xsl:with-param>
		<xsl:with-param name="lab_pick"><xsl:value-of select="$lab_pick"/></xsl:with-param>
		<xsl:with-param name="lab_link"><xsl:value-of select="$lab_link"/></xsl:with-param>
		<xsl:with-param name="lab_status"><xsl:value-of select="$lab_status"/></xsl:with-param>
		<xsl:with-param name="lab_status_on"><xsl:value-of select="$lab_status_on"/></xsl:with-param>
		<xsl:with-param name="lab_status_off"><xsl:value-of select="$lab_status_off"/></xsl:with-param>
		<xsl:with-param name="lab_create_method"><xsl:value-of select="$lab_create_method"/></xsl:with-param>
		<xsl:with-param name="lab_add_node"><xsl:value-of select="$lab_add_node"/></xsl:with-param>
		<xsl:with-param name="lab_edit_node"><xsl:value-of select="$lab_edit_node"/></xsl:with-param>
		<xsl:with-param name="lab_node_desc"><xsl:value-of select="$lab_node_desc"/></xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_ok"><xsl:value-of select="$lab_g_form_btn_ok"/></xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_cancel"><xsl:value-of select="$lab_g_form_btn_cancel"/></xsl:with-param>
		<xsl:with-param name="lab_desc_limit"><xsl:value-of select="$lab_desc_limit"/></xsl:with-param>
	</xsl:apply-templates>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="header">
	<xsl:param name="lab_all_cata"/>
	<xsl:param name="lab_add_node"/>
	<xsl:param name="lab_edit_node"/>
	<xsl:call-template name="wb_ui_hdr">
		<xsl:with-param name="belong_module">FTN_AMD_CAT_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_CAT_MAIN</xsl:with-param>
	</xsl:call-template>
	<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				 <!-- <a href="javascript:wb_utils_cata_lst()" class="NavLink">
					<xsl:value-of select="$lab_all_cata"/>
				</a>  -->
				<xsl:for-each select="node/nav/node">
					<!-- <xsl:choose>
						<xsl:when test="position() != last()"> -->
							<a href="javascript:wb_utils_node_lst({@node_id})" class="NavLink">
								<xsl:value-of select="title"/>
							</a>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
						<!-- </xsl:when>
						<xsl:otherwise>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<xsl:value-of select="title"/>
						</xsl:otherwise>
					</xsl:choose> -->
				</xsl:for-each>
				
				<xsl:choose>
				  <xsl:when test="node/title[text()]">
				     <xsl:value-of select="$lab_edit_node"/><xsl:text>&#160;-&#160;</xsl:text>
				     <xsl:value-of select="node/nav/node[position() = last()]/title"/>
				  </xsl:when>
				  <xsl:otherwise>
				    <xsl:value-of select="$lab_add_node"/>
				  </xsl:otherwise>
			    </xsl:choose>
			</xsl:with-param>
	</xsl:call-template>
	
	<!--<xsl:call-template name="wb_ui_title">
		<xsl:with-param name="text">
			<xsl:choose>
				<xsl:when test="node/title[text()]"><xsl:value-of select="$lab_edit_node"/><xsl:text>&#160;-&#160;</xsl:text><xsl:value-of select="node/nav/node[position() = last()]/title"/></xsl:when>
				<xsl:otherwise><xsl:value-of select="$lab_add_node"/></xsl:otherwise>
			</xsl:choose>
		</xsl:with-param>				
	</xsl:call-template>-->	
			
</xsl:template>
<!-- =============================================================== -->
<xsl:template match="node">
	<xsl:param name="lab_all_cata"/>
	<xsl:param name="lab_node_nm"/>
	<xsl:param name="lab_create_node"/>
	<xsl:param name="lab_link_public"/>
	<xsl:param name="lab_pick"/>
	<xsl:param name="lab_link"/>
	<xsl:param name="lab_status"/>
	<xsl:param name="lab_status_on"/>
	<xsl:param name="lab_status_off"/>
	<xsl:param name="lab_create_method"/>
	<xsl:param name="lab_add_node"/>
	<xsl:param name="lab_edit_node"/>
	<xsl:param name="lab_node_desc"/>
	<xsl:param name="lab_g_form_btn_ok"/>
	<xsl:param name="lab_g_form_btn_cancel"/>
	<xsl:param name="lab_desc_limit"/>
	
	<!-- start -->
	<!-- form option -->
	<table>
		<!-- tnd title -->
		<tr>
			<td class="wzb-form-label">
				<span class="wzb-form-star">*</span><xsl:value-of select="$lab_node_nm"/>：
			</td>						
			<td class="wzb-form-control">
			   <input class="wzb-inputText" size="35" name="tnd_title" type="text" style="width:300px;" maxlength="100" value="{title}"/>
				<script><![CDATA[
					var title = ']]><xsl:value-of select="title"/><![CDATA[';
					if (title == '') {
						document.frmAction.tnd_title.value=wb_utils_get_cookie('tnd_title');
					}
				]]></script>
			</td>
		</tr>
		<!-- status -->
		<tr>
			<td class="wzb-form-label">
				<span class="wzb-form-star">*</span><xsl:value-of select="$lab_status"/>：
			</td>
			<td class="wzb-form-control">
				<xsl:choose>
					<xsl:when test="not($creator)">
				    <label for="tnd_status_radio_on">
						<input type="radio" id="tnd_status_radio_on" name="tnd_status" value="ON" checked="checked"/>
						<xsl:value-of select="$lab_status_on"/>
				    </label>
						<xsl:text>&#160;</xsl:text>
				    <label for="tnd_status_radio_off">
						<input type="radio" id="tnd_status_radio_off" name="tnd_status" value="OFF"/>
						<xsl:value-of select="$lab_status_off"/>										
				    </label>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="@status = 'ON'">			
						    <label for="tnd_status_radio_on_2">
								<input type="radio" id="tnd_status_radio_on_2" name="tnd_status" value="ON" checked="checked"/>
								<xsl:value-of select="$lab_status_on"/>
						    </label>
								<xsl:text>&#160;</xsl:text>
						    <label for="tnd_status_radio_off_2">
								<input type="radio" id="tnd_status_radio_off_2" name="tnd_status" value="OFF"/>
								<xsl:value-of select="$lab_status_off"/>
						    </label>
							</xsl:when>
							<xsl:otherwise>												
						    <label for="tnd_status_radio_on_3">
								<input type="radio" id="tnd_status_radio_on_3" name="tnd_status" value="ON"/>
								<xsl:value-of select="$lab_status_on"/>
						    </label>
								<xsl:text>&#160;</xsl:text>
						    <label for="tnd_status_radio_off_3">
								<input type="radio" id="tnd_status_radio_off_3" name="tnd_status" value="OFF" checked="checked"/>
								<xsl:value-of select="$lab_status_off"/>
						    </label>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
		<!-- tnd desc -->
		<tr>
			<td class="wzb-form-label" valign="top">
				<xsl:value-of select="$lab_node_desc"/><xsl:text>：</xsl:text>
			</td>						
			<td class="wzb-form-control">
				<!--<input size="35" name="tnd_desc" type="text" style="width:300px;" maxlength="49" value="{title}"/>-->
				<textarea class="wzb-inputTextArea" rows="5" name="tnd_desc"  style="width:300px;"><xsl:value-of select="desc"/></textarea>
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
	<div class="wzb-bar">					
		<xsl:choose>
			<xsl:when test="not($creator)">
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_ok"/></xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">javascript:cata_lst.node_list.add_exec(document.frmAction,'<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="@parent_tnd_id"/>','<xsl:value-of select="@cat_id"/>','<xsl:value-of select="link/@link_id"/>')</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$creator">
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_ok"/></xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">javascript:cata_lst.node_list.edit_exec(document.frmAction,'<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="@node_id"/>','<xsl:value-of select="@parent_tnd_id"/>','<xsl:value-of select="link/@link_id"/>')</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
		<xsl:variable name="back_id">
			<xsl:choose>
				<xsl:when test="@node_id != '' and @parent_tnd_id != ''"><xsl:value-of select="@node_id"/></xsl:when>
				<xsl:when test="@ndoe_id = '' and @parent_tnd_id != ''"><xsl:value-of select="@parent_tnd_id"/></xsl:when>
				<xsl:otherwise><xsl:value-of select="@parent_tnd_id"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:call-template name="wb_gen_form_button">
			<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_cancel"/></xsl:with-param>
			<xsl:with-param name="wb_gen_btn_href">javascript:cata_lst.node_list.cancel('<xsl:value-of select="$back_id"/>')</xsl:with-param>
			<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
		</xsl:call-template>
	</div>
	<input type="hidden" name="cmd"/>
	<input type="hidden" name="url_success"/>
	<input type="hidden" name="url_failure"/>
	<input type="hidden" name="stylesheet"/>
	<xsl:choose>
		<xsl:when test="not($creator)"><input type="hidden" name="tnd_cat_id"/></xsl:when>
		<xsl:when test="$creator"><input type="hidden" name="tnd_upd_timestamp" value="{last_updated/@timestamp}"/></xsl:when>
	</xsl:choose>
	<input type="hidden" name="tnd_link_tnd_id"/>
	<input type="hidden" name="tnd_parent_tnd_id"/>
	<input type="hidden" name="tnd_id"/>
	<!-- commented by marcus 
		<script language="Javascript"><![CDATA[
			
		if (']]><xsl:value-of select="//applyeasy/node/title"/><![CDATA[' !='' ){
			if (document.all != null){
				document.frmAction.tnd_title.value = decodeURI(']]><xsl:value-of select="//applyeasy/node/title"/><![CDATA[')
			}else if (document.getElementById!=null) {
				//document.frmAction.tnd_title.value = unescape(']]><xsl:value-of select="//applyeasy/node/title"/><![CDATA[')
			}
		}
		]]></script>
	-->
		<xsl:call-template name="wb_ui_footer"/>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="tndTitleInit"><xsl:value-of select="node/title"/></xsl:template>
<!-- =============================================================== -->
</xsl:stylesheet>
