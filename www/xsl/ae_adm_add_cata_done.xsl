<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>

	<!-- customized utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================================== -->	
	<xsl:template match="/">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================================== -->	
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="applyeasy">
			<xsl:with-param name="lab_status">狀況</xsl:with-param>
			<xsl:with-param name="lab_msg1">你已成功加入目錄。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_all_cata">課程目錄</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="applyeasy">
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_msg1">目录添加成功。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_all_cata">课程目录</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="applyeasy">
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_msg1">Catalog has been added successfully.</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_all_cata">Learning catalog</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================================== -->	
	<xsl:template match="applyeasy">
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_msg1"/>
		<xsl:param name="lab_status"/>
			<xsl:param name="lab_all_cata"/>
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_cata_lst.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="javascript" type="text/javascript"><![CDATA[
			cata_lst = new wbCataLst
			]]></script>
		</head>
		

		
		<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0">
	 
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_CAT_MAIN</xsl:with-param>
			<xsl:with-param name="page_title"><xsl:value-of select="$lab_all_cata"/></xsl:with-param>
		</xsl:call-template>

			<form name="frmXml" onsubmit="enter();return false;">
				<table border="0" cellpadding="0" cellspacing="0" width="{$wb_gen_msg_box_width}">
					<tr>
						<td>
							<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
						</td>
					</tr>
					<tr>
						<td>
							<xsl:call-template name="wb_ui_head">
								<xsl:with-param name="width">100%</xsl:with-param>
								<xsl:with-param name="text"><xsl:value-of select="$lab_status"/></xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="wb_ui_line">
								<xsl:with-param name="width">100%</xsl:with-param>
							</xsl:call-template>
							
		
							
							<table cellpadding="0" cellspacing="0" width="100%" border="0">
								<tr>
									<td width="1" class="Line">
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
									<td width="100%">
										<table cellpadding="15" cellspacing="0" width="100%" border="0">
											<tr><td height="10"><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></td></tr>
											<tr>
												<td>
													<span class="TitleText">
														<xsl:value-of select="$lab_msg1"/>
													</span>
												</td>
											</tr>
											<tr><td height="10"><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></td></tr>
										</table>
									</td>
									<td width="1" class="Line">
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
								</tr>
							</table>
							<xsl:call-template name="wb_ui_line">
								<xsl:with-param name="width">100%</xsl:with-param>
							</xsl:call-template>
							<table cellpadding="3" cellspacing="0" width="100%" border="0">
								<tr>
									<td align="center">
										<xsl:variable name="_view">
											<xsl:choose>
												<xsl:when test="node/assigned_user/@public_ind = 'true' or count(node/assigned_user//entity) = 1">item</xsl:when>
												<xsl:otherwise>item</xsl:otherwise>
											</xsl:choose>
										</xsl:variable>
										<xsl:call-template name="wb_gen_form_button">
											<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>												
											<xsl:with-param name="wb_gen_btn_href">javascript:wb_utils_node_lst('<xsl:value-of select="node/@node_id"/>','<xsl:value-of select="$_view"/>')</xsl:with-param>
										</xsl:call-template>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<script language="javascript" type="text/javascript">
				<![CDATA[
				if(document.all){
				 document.write('<input type="submit" value="" size="0" style="height:0px;width:0px;visibility:hidden;">')
				}
				]]>
				</script>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>
