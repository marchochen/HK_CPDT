<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- customize utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>	
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/> 
	<!-- other -->
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="label" select="/applyeasy/meta/label_reference_data_list"/>
	<xsl:variable name="itm_upd_timestamp" select="/applyeasy/last_updated/@timestamp"/>
	<xsl:variable name="app_approval_type" select="/applyeasy/item/@app_approval_type"/>
	<xsl:variable name="lab_upd_work_flow" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1169')"/>
	<xsl:variable name="lab_g_select_enrol_wf" select="$label/label[@name = 'lab_g_select_enrol_wf']"/>
	<xsl:variable name="lab_g_form_btn_ok" select="$label/label[@name = 'lab_g_form_btn_ok']"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="$label/label[@name = 'lab_g_form_btn_cancel']"/>	
	<xsl:variable name="label_core_training_management_341" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_341')" />
	<xsl:variable name="label_core_training_management_340" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_340')" />
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:apply-templates select="applyeasy/item"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="applyeasy/item">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[
				itm_lst = new wbItem
				
		
				function init(){
					var app_approval_type = ']]><xsl:value-of select="$app_approval_type"/><![CDATA['

					frm = document.frmXml;
					switch(app_approval_type){
						
						case "" :
							no_approval_selected();								
							break;
						
						case "DIRECT_SUPERVISE" :
							approval_selected();
							supervisor_changed(true);
							frm.rdo[0].checked = true;
							break;
							
						case "DIRECT_SUPERVISE_SUPERVISE" :
							approval_selected();
							supervisor_changed(true);
							frm.rdo[1].checked = true;
							break;
													
						case "DIRECT_SUPERVISE_SUPERVISE_TADM" :
							approval_selected();
							supervisor_changed(true);
							frm.rdo[1].checked = true;
							frm.chk_tadm.checked = true;	
							break;
								
						case "DIRECT_SUPERVISE_TADM" :
							approval_selected();
							supervisor_changed(true);
							frm.rdo[0].checked = true;		
							frm.chk_tadm.checked = true;	
							break;
						
						case "TADM" :
							approval_selected();
							supervisor_changed(false);
							frm.chk_tadm.checked = true;	
							break;
					}
				}
				
				function no_approval_selected(){
					frm = document.frmXml;
					frm.wrk_tpl_id[0].checked = true;
					frm.chk_supervise.checked = false;
					frm.chk_tadm.checked = false;					
					frm.chk_supervise.disabled = true;
					frm.chk_tadm.disabled = true;
					disable_rdo(frm,true);
					uncheck_rdo(frm);
				}
				
				function approval_selected(){
					frm = document.frmXml;
					frm.wrk_tpl_id[1].checked = true;
					frm.chk_supervise.checked = false;
					frm.chk_tadm.checked = false;
					uncheck_rdo(frm);		
					frm.chk_supervise.disabled = false;
					frm.chk_tadm.disabled = false;			
				}
				
				function supervisor_changed(state){
					if(state == true){
						frm.chk_supervise.checked = true;
					}else if(state == false){
						frm.chk_supervise.checked = false;
					}
				
					if(frm.chk_supervise.checked == true){
						frm.chk_supervise.checked = true;
						disable_rdo(frm,false)					
					}else{
						disable_rdo(frm,true)
						uncheck_rdo(frm)				
					}
				}
				
				function disable_rdo(frm,state){
					frm.rdo[0].disabled = state;
					frm.rdo[1].disabled = state;				
				}
				
				function uncheck_rdo(frm){
					frm.rdo[0].checked = false;
					frm.rdo[1].checked = false;
				}
			]]></script>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="init()">
				<form name="frmXml">
					<input type="hidden" name="itm_upd_timestamp" value="{$itm_upd_timestamp}"/>
					<input type="hidden" name="itm_id" value="{$itm_id}"/>
					<input type="hidden" name="cmd" value=""/>
					<input type="hidden" name="stylesheet" value=""/>
					<input type="hidden" name="itm_app_approval_type" value=""/>
					<input type="hidden" name="url_success" value=""/>
					<input type="hidden" name="url_failure" value=""/>
					<xsl:call-template name="content"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
	    <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">101</xsl:with-param>
		</xsl:call-template>
        <div class="wzb-item-main">
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
					<xsl:value-of select="/applyeasy/item/title"/>
				</a>
				<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_upd_work_flow"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text">
			    <xsl:choose>
			    	<xsl:when test="//itm_action_nav/@itm_exam_ind = 'true'"><xsl:value-of select="$label_core_training_management_340"/></xsl:when>
			    	<xsl:otherwise><xsl:value-of select="label_core_training_management_341"/></xsl:otherwise>
			    </xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<table>
			<tr>
				<td colspan="2">
					<table>
						<tr>
							<td width="120">
							</td>
							<td>
								<xsl:value-of select="$lab_g_select_enrol_wf"/>
								<xsl:text>：</xsl:text>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<!-- Title 1 -->
			<tr>
				<td valign="top" align="right">
					<input type="radio" name="wrk_tpl_id" id="wrk_tpl_id_no_approval" onclick="no_approval_selected()"/>
				</td>
				<td align="left">
					
						<label for="wrk_tpl_id_no_approval" style="margin-top: -3px;padding-left: 4px;">
							<xsl:value-of select="$label/label[@name = 'lab_g_template_workflow']/element/title[@id='1']"/>
						</label>
					
					<br/>
					<xsl:value-of select="$label/label[@name = 'lab_g_template_workflow']/element/description[@id='1']"/>
				</td>
			</tr>
			<!-- Title 2 -->
			<tr>
				<td valign="top" align="right">
					<input type="radio" name="wrk_tpl_id" id="wrk_tpl_id_approval" onclick="approval_selected()"/>
				</td>
				<td align="left">
					
						<label for="wrk_tpl_id_approval" style="margin-top: -3px;padding-left: 4px;">
							<xsl:value-of select="$label/label[@name = 'lab_g_template_workflow']/element/title[@id='2']"/>
						</label>
					
					<br/>
					<xsl:value-of select="$label/label[@name = 'lab_g_template_workflow']/element/description[@id='2']"/>
				</td>
			</tr>
			<!-- Title 3 -->
			<tr>
				<td valign="top" align="right">
					<xsl:text>&#160;</xsl:text>
				</td>
				<td align="left">
					<table>
						<tr>
							<td valign="top" align="right" width="5%">
								<input type="checkbox" name="chk_supervise" id="chk_supervise" onclick="supervisor_changed()"/>
							</td>
							<td align="left" width="95%">
								<b>
									<label for="chk_supervise" style="margin-top: -3px;padding-left: 4px;">
										<xsl:value-of select="$label/label[@name = 'lab_g_template_workflow']/element/title[@id='3']"/>
									</label>
								</b>
								<br/>
								<xsl:value-of select="$label/label[@name = 'lab_g_template_workflow']/element/description[@id='3']"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<!-- Title 4 -->
			<tr>
				<td valign="top" align="right">
					<xsl:text>&#160;</xsl:text>
				</td>
				<td align="left">
					<table>
						<tr>
							<td width="5%">
								<xsl:text>&#160;</xsl:text>
							</td>
							<td align="left" width="95%">
								<table>
									<tr>
										<td valign="top" align="right" width="8%">
											<input type="radio" name="rdo" id="rdo_ds"/>
										</td>
										<td align="left" width="92%">
											<b>
												<label for="rdo_ds" style="margin-top: -3px;padding-left: 4px;">
													<xsl:value-of select="$label/label[@name = 'lab_g_template_workflow']/element/title[@id='4']"/>
												</label>
											</b>
											<br/>
											<xsl:value-of select="$label/label[@name = 'lab_g_template_workflow']/element/description[@id='4']"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<!-- Title 5 -->
			<tr>
				<td valign="top" align="right">
					<xsl:text>&#160;</xsl:text>
				</td>
				<td align="left">
					<table>
						<tr>
							<td width="5%">
								<xsl:text>&#160;</xsl:text>
							</td>
							<td align="left" width="95%">
								<table>
									<tr>
										<td valign="top" align="right" width="8%">
											<input type="radio" name="rdo" id="rdo_ds_gs"/>
										</td>
										<td align="left" width="92%">
											<b>
												<label for="rdo_ds_gs" style="margin-top: -3px;padding-left: 4px;">
													<xsl:value-of select="$label/label[@name = 'lab_g_template_workflow']/element/title[@id='5']"/>
												</label>
											</b>
											<br/>
											<xsl:value-of select="$label/label[@name = 'lab_g_template_workflow']/element/description[@id='5']"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<!-- Title 6 -->
			<tr>
				<td valign="top" align="right">
					<xsl:text>&#160;</xsl:text>
				</td>
				<td align="left">
					<table>
						<tr>
							<td valign="top" align="right" width="5%">
								<input type="checkbox" name="chk_tadm" id="chk_tadm"/>
							</td>
							<td align="left" width="95%">
								<b>
									<label for="chk_tadm" style="margin-top: -3px;padding-left: 4px;">
										<xsl:value-of select="$label/label[@name = 'lab_g_template_workflow']/element/title[@id='6']"/>
									</label>
								</b>
								<br/>
								<xsl:value-of select="$label/label[@name = 'lab_g_template_workflow']/element/description[@id='6']"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right" height="10">
				</td>
				<td width="80%" height="10">
				</td>
			</tr>			
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.upd_item_workflow_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>');</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.get_item_detail(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
			</xsl:call-template>
		</div>	
	</div>
	</xsl:template>
</xsl:stylesheet>
