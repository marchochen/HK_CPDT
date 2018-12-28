<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>	
	<xsl:output indent="yes"/>
	<!-- paginatoin variables -->
	<xsl:variable name="page_size" select="/list/pagination/@page_size"/>
	<xsl:variable name="cur_page" select="/list/pagination/@cur_page"/>
	<xsl:variable name="total" select="/list/pagination/@total_rec"/>
	<xsl:variable name="page_timestamp" select="/list/pagination/@timestamp"/>
	<!-- sorting variable -->
	<xsl:variable name="sort_by" select="/list/pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="/list/pagination/@sort_order"/>
	<xsl:variable name="order_by">
		<xsl:choose>
			<xsl:when test="$cur_order = 'ASC' ">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>	
	<!-- =============================================================== -->
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<xsl:variable name="obj_id" select="/list/header/objective/@id"/>
	<xsl:variable name="obj_type" select="/list/header/objective/@type"/>
	<xsl:variable name="_cnt_que">
		<xsl:choose>
			<xsl:when test="/list/header/@privilege='AUTHOR'">
				<xsl:value-of select="count(/list/body/item[@owner=//cur_usr/@id])"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="count(/list/body/item)"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="list/body"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="list/body">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<meta http-equiv="pragma" content="no-cache"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_question.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_objective.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>			
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			que = new wbQuestion;
			obj = new wbObjective;
		
		]]></SCRIPT>
		<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" rightmargin="0" bottommargin="0"  class="Bg">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_select_all">全選</xsl:with-param>
			<xsl:with-param name="lab_add_to_test">加到試卷</xsl:with-param>
			<xsl:with-param name="lab_no_res">還沒有任何資源</xsl:with-param>			
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_select_all">全选</xsl:with-param>
			<xsl:with-param name="lab_add_to_test">加入测验</xsl:with-param>
			<xsl:with-param name="lab_no_res">还没有任何资源</xsl:with-param>			
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_select_all">Select all</xsl:with-param>
			<xsl:with-param name="lab_add_to_test">Add to test</xsl:with-param>
			<xsl:with-param name="lab_no_res">No resources found.</xsl:with-param>			
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_select_all"/>
		<xsl:param name="lab_add_to_test"/>
		<xsl:param name="lab_no_res"/>
		<form name="frmQue" method="post" target="_top">
		<input type="hidden" name="module" value="" />
			<input type="hidden" name="cmd" value="ins_mod_que"/>
			<input type="hidden" name="mod_id" value=""/>
			<input type="hidden" name="que_id_lst" value=""/>
			<input type="hidden" name="obj_id" value="{//objective/@id}"/>
			<input type="hidden" name="url_success" value=""/>
			<input type="hidden" name="url_failure" value=""/>
			<input type="hidden" name="res_id" value="" />
			<input type="hidden" name="res_type" value="" />
			<input type="hidden" name="res_subtype" value="" />
			
			<xsl:if test="count(item)!=0">
			<xsl:call-template name="wb_ui_line"/>
			<table width="{$wb_gen_table_width}" border="0" cellpadding="3" cellspacing="0" class="Bg">
				<tr>
					<td colspan="2" align="left" height="35">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_add_to_test"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:obj.pick_que_exec('<xsl:value-of select="$wb_lang"/>',true)</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_target">_parent</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
				<tr class="SecBg">
					<td>
						<img src="{$wb_img_path}tp.gif" width="2" height="0" border="0"/>
					</td>
					<td height="12" align="left">
						<xsl:call-template name="select_all_checkbox">
							<xsl:with-param name="display_icon">false</xsl:with-param>
							<xsl:with-param name="sel_all_chkbox_nm">sel_all_checkbox</xsl:with-param>
							<xsl:with-param name="frm_name">frmQue</xsl:with-param>
						</xsl:call-template>
						<span class="TitleText">
							<xsl:value-of select="$lab_select_all"/>
						</span>
					</td>
				</tr>
				<tr>
					<td colspan="2" height="10" align="right">
						<img src="{$wb_img_path}tp.gif" width="2" height="0" border="0"/>
					</td>
				</tr>
				<tr>
					<td width="10">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
					<td width="165" valign="top">
						<table border="0" cellpadding="0" cellspacing="0" width="155">
							<xsl:choose>
								<xsl:when test="/list/header/@privilege='AUTHOR'">
									<xsl:apply-templates select="item[@owner=//cur_usr/@id]"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates/>
								</xsl:otherwise>
							</xsl:choose>
						</table>
					</td>
				</tr>
			</table>
			</xsl:if>
				<xsl:choose>
					<xsl:when test="count(item)=0">
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_no_res"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wb_ui_pagination">
							<xsl:with-param name="cur_page" select="$cur_page"/>
							<xsl:with-param name="page_size" select="$page_size"/>
							<xsl:with-param name="total" select="$total"/>
							<xsl:with-param name="timestamp" select="$page_timestamp"/>
							<xsl:with-param name="wzb-page-style">text-align: left;padding: 17px 0 5px;</xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>			
		</form>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="item">
		<xsl:if test="text()!=''">
			<tr>
				<td valign="top">
					
				</td>
				<td  valign="top">
				       <input type="checkbox" name="que_{@id}" value="{@id}"/>
				      <xsl:choose>
						<xsl:when test="@subtype = 'FB'">
							<img src="{$wb_img_path}sico_fb.gif" border="0" hspace="3"/>
						</xsl:when>
						<xsl:when test="@subtype = 'MC'">
							<img src="{$wb_img_path}sico_mc.gif" border="0" hspace="3"/>
						</xsl:when>
						<xsl:when test="@subtype = 'MT'">
							<img src="{$wb_img_path}sico_mt.gif" border="0" hspace="3"/>
						</xsl:when>
						<xsl:when test="@subtype = 'TF'">
							<img src="{$wb_img_path}sico_tf.gif" border="0" hspace="3"/>
						</xsl:when>
						<xsl:when test="@subtype = 'ES'">
							<img src="{$wb_img_path}sico_es.gif" border="0" hspace="3"/>
						</xsl:when>												
						<xsl:when test="@subtype = 'FSC'">
							<img src="{$wb_img_path}sico_fsc.gif" border="0" hspace="3"/>
						</xsl:when>												
						<xsl:when test="@subtype = 'DSC'">
							<img src="{$wb_img_path}sico_dsc.gif" border="0" hspace="3"/>
						</xsl:when>												
					</xsl:choose>
					<a href="javascript:que.read_in_select_que({@id})" class="Text">
						<xsl:value-of select="concat(@id, '. ', text())"/>
					</a>
				</td>
			</tr>
			<tr>
				<td colaspan="2">&#160;</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<xsl:template match="cur_usr"/>
	<xsl:template match="header"/>
</xsl:stylesheet>
