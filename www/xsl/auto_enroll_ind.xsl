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
			<xsl:apply-templates select="applyeasy/auto_enroll_ind"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="auto_enroll_ind">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">
			<![CDATA[
			function resize_announce(){
				var height_cap = 600 ;// Max height
				if(document.page_end && (document.all || document.getElementById!=null)){
					var new_height = document.page_end.offsetTop + 80
					if(new_height > height_cap){
						//new_height = height_cap
					}
					window.resizeTo(600,new_height);
				}
			}
			]]>
			</SCRIPT>
			<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="resize_announce()">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_info">你正準備增加新的報名到這個課程。由於報名可能需要先被審批，所以你可以選擇：</xsl:with-param>
			<xsl:with-param name="lab_auto_enroll">所增加的用戶將越過所有需要的審批過程直接錄取</xsl:with-param>
			<xsl:with-param name="lab_not_auto">所增加的用戶將等待所需的審批</xsl:with-param>
			<xsl:with-param name="lab_add_new">新增報名</xsl:with-param>
			<xsl:with-param name="lab_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_info">您正准备添加新的报名到这个课程。由于报名可能需要先被审批，所以您可以选择：</xsl:with-param>
			<xsl:with-param name="lab_auto_enroll">所增加的用户将越过所有需要的审批过程直接录取</xsl:with-param>
			<xsl:with-param name="lab_not_auto">所增加的用户将等待所需的审批</xsl:with-param>
			<xsl:with-param name="lab_add_new">添加报名</xsl:with-param>
			<xsl:with-param name="lab_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_info">You are going to add new enrollment to this learning solution. Since approval may be required for new enrollment to be confirmed. You can choose:</xsl:with-param>
			<xsl:with-param name="lab_auto_enroll">To by-pass all required approval steps and confirm all selected enrollments immediately </xsl:with-param>
			<xsl:with-param name="lab_not_auto">To go through all required approval steps for all selected enrollments</xsl:with-param>
			<xsl:with-param name="lab_add_new">Add enrollment</xsl:with-param>
			<xsl:with-param name="lab_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_info"/>
		<xsl:param name="lab_ok"/>
		<xsl:param name="lab_cancel"/>
		<xsl:param name="lab_auto_enroll"/>
		<xsl:param name="lab_not_auto"/>
		<xsl:param name="lab_add_new"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_add_new"/>
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
				<td width="5">
				</td>
				<td width="5" align="right">
					<input type="radio" name="auto_enroll" value="true" id="auto_enroll" checked="checked"/>
				</td>
				<td align="left">
					<label for="auto_enroll">
						<xsl:value-of select="$lab_auto_enroll"/>
					</label>
				</td>
			</tr>
			<tr>
				<td width="5">
				</td>
				<td width="5" align="right">
					<input type="radio" name="auto_enroll" value="false" id="not_auto"/>
				</td>
				<td align="left">
					<label for="not_auto">
							<xsl:value-of select="$lab_not_auto"/>
					</label>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_ok"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">Javascript:window.close();opener.new_enrollment(document.getElementsByName('auto_enroll'));</xsl:with-param>
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
