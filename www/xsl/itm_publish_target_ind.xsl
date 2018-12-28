<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<!-- cust-->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="applyeasy/item"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="item">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
            <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"></script>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">
			<![CDATA[
				var itm = new wbItem;
			]]>
			</SCRIPT>
			<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml">
				<input type="hidden" name="itm_status_lst" value="{status}"/>
				<input type="hidden" name="itm_upd_timestamp_lst" value="{timestamp}"/>
				<input type="hidden" name="itm_id_lst" value="{id}"/>
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_info">發佈對象：</xsl:with-param>
			<xsl:with-param name="lab_target_lrn">僅目標學員</xsl:with-param>
			<xsl:with-param name="lab_all_lrn">所有學員</xsl:with-param>
			<xsl:with-param name="lab_publish">發佈</xsl:with-param>
			<xsl:with-param name="lab_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_info">发布对象：</xsl:with-param>
			<xsl:with-param name="lab_target_lrn">仅目标学员</xsl:with-param>
			<xsl:with-param name="lab_all_lrn">所有学员</xsl:with-param>
			<xsl:with-param name="lab_publish">发布</xsl:with-param>
			<xsl:with-param name="lab_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_info">Publish target:</xsl:with-param>
			<xsl:with-param name="lab_target_lrn">Target learners only</xsl:with-param>
			<xsl:with-param name="lab_all_lrn">All learners</xsl:with-param>
			<xsl:with-param name="lab_publish">Publish</xsl:with-param>
			<xsl:with-param name="lab_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_info"/>
		<xsl:param name="lab_ok"/>
		<xsl:param name="lab_cancel"/>
		<xsl:param name="lab_target_lrn"/>
		<xsl:param name="lab_all_lrn"/>
		<xsl:param name="lab_publish"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_publish"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<tr>
				<td width="2%">
				</td>
				<td width="96%">				
					<xsl:value-of select="$lab_info" disable-output-escaping="yes"/>
				</td>
				<td width="2%">
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td class="wzb-form-label" valign="top">
					<input type="radio" name="itm_access_type" value="TARGET_LEARNER" id="target_lrn"/>
				</td>
				<td class="wzb-form-control">
					<label for="target_lrn">
						<xsl:value-of select="$lab_target_lrn"/>
					</label>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<input type="radio" name="itm_access_type" value="ALL" id="all_lrn" checked="checked"/>
				</td>
				<td class="wzb-form-control">
					<label for="all_lrn">
						<xsl:value-of select="$lab_all_lrn"/>
					</label>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_ok"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">Javascript:itm.upd_itm_status_on(document.frmXml);</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_cancel"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:self.close()</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
