<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:variable name="success_entity" select="/ims_log/data_import/success_entity/total"/>
	<xsl:variable name="unsuccess_entity" select="/ims_log/data_import/unsuccess_entity/total"/>
	<xsl:variable name="src_file" select="/ims_log/data_import/@src_file"/>
	<xsl:output indent="yes"/>
	<xsl:strip-space elements="*"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="//data_import"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data_import">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_batchprocess.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" TYPE="text/javascript"><![CDATA[
			Batch = new wbBatchProcess
			app = new wbApplication
			itm_lst = new wbItem
		]]></script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_title">匯入登記記錄和完成結果</xsl:with-param>
			<xsl:with-param name="lab_success_entries">成功輸入項</xsl:with-param>
			<xsl:with-param name="lab_failed_entries">未成功輸入項</xsl:with-param>
			<xsl:with-param name="lab_view_log_file">查看日誌檔</xsl:with-param>
			<xsl:with-param name="lab_failed">匯入過程失敗</xsl:with-param>
			<xsl:with-param name="lab_reason">原因</xsl:with-param>
			<xsl:with-param name="lab_enrollment_approval">處理报名</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:choose>
				<xsl:when test="$itm_exam_ind='true'">考試場次</xsl:when>
				<xsl:otherwise>班級資訊</xsl:otherwise>
			    </xsl:choose>
			</xsl:with-param>    
			<xsl:with-param name="lab_msg1">
				<xsl:value-of select="$src_file"/> 正在處理中。</xsl:with-param>
			<xsl:with-param name="lab_msg2">當完成上載的請求後﹐系統會以電郵通知你。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_title">导入报名记录和完成结果</xsl:with-param>
			<xsl:with-param name="lab_success_entries">成功输入项</xsl:with-param>
			<xsl:with-param name="lab_failed_entries">未成功输入项</xsl:with-param>
			<xsl:with-param name="lab_view_log_file">查看日志文件</xsl:with-param>
			<xsl:with-param name="lab_failed">导入过程失败</xsl:with-param>
			<xsl:with-param name="lab_reason">原因</xsl:with-param>
			<xsl:with-param name="lab_enrollment_approval">处理报名</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:choose>
				<xsl:when test="$itm_exam_ind='true'">考试场次</xsl:when>
				<xsl:otherwise>班級信息</xsl:otherwise>
			    </xsl:choose>
			</xsl:with-param> 
			<xsl:with-param name="lab_msg1">
				<xsl:value-of select="$src_file"/> 正在处理中。</xsl:with-param>
			<xsl:with-param name="lab_msg2">当完成上载的请求后,系统会以邮件通知你。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_title">Import enrollment and completion result - result</xsl:with-param>
			<xsl:with-param name="lab_success_entries">Success entries</xsl:with-param>
			<xsl:with-param name="lab_failed_entries">Failed entries</xsl:with-param>
			<xsl:with-param name="lab_view_log_file">View log file</xsl:with-param>
			<xsl:with-param name="lab_failed">Import process failed to complete. </xsl:with-param>
			<xsl:with-param name="lab_reason">Reason</xsl:with-param>
			<xsl:with-param name="lab_enrollment_approval">Enrollment</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:choose>
				<xsl:when test="$itm_exam_ind='true'">Class</xsl:when>
				<xsl:otherwise>Class information</xsl:otherwise>
			    </xsl:choose>
			</xsl:with-param> 
			<xsl:with-param name="lab_msg1">
				<xsl:value-of select="$src_file"/> is being processed at the server.</xsl:with-param>
			<xsl:with-param name="lab_msg2">A notification email will be sent to you upon completion.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_success_entries"/>
		<xsl:param name="lab_failed_entries"/>
		<xsl:param name="lab_view_log_file"/>
		<xsl:param name="lab_failed"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_reason"/>
		<xsl:param name="lab_enrollment_approval"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_msg1"/>
		<xsl:param name="lab_msg2"/>
		<xsl:call-template name="wb_ui_hdr">
		    <xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
		    <xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_title"/>
		</xsl:call-template>
		<!--<xsl:call-template name="wb_ui_desc">
				<xsl:with-param name="text" select="$lab_desc"/>
			</xsl:call-template>-->
		<!-- <xsl:call-template name="wb_ui_line"/> -->
		
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
			 	<xsl:choose>
			 	    <xsl:when test="../../item/@run_ind='true'">
						<a href="Javascript:itm_lst.get_item_detail({../item/parent/@id})" class="NavLink">
							<xsl:value-of select="../item/parent/@title"/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="Javascript:itm_lst.get_item_run_list({../item/parent/@id})" class="NavLink">
							<xsl:choose>
								<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
							</xsl:choose>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="Javascript:itm_lst.get_item_run_detail({../item/@id})" class="NavLink">
							<xsl:value-of select="../item/@title"/>
						</a>
					 </xsl:when>
					 <xsl:when test="../item/@run_ind='true'">
						<a href="Javascript:itm_lst.get_item_detail({../item/parent/@id})" class="NavLink">
							<xsl:value-of select="../item/parent/@title"/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="Javascript:itm_lst.get_item_run_list({../item/parent/@id})" class="NavLink">
							<xsl:choose>
								<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
							</xsl:choose>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="Javascript:itm_lst.get_item_run_detail({../item/@id})" class="NavLink">
							<xsl:value-of select="../item/@title"/>
						</a>
					</xsl:when>  
					<xsl:otherwise>
						<a href="Javascript:itm_lst.get_item_detail({../item/@id})" class="NavLink">
							<xsl:value-of select="../item/@title"/>
						</a>
					</xsl:otherwise>
				</xsl:choose>  
				<xsl:text>&#160;&gt;&#160;</xsl:text>
			    <a href="Javascript:app.get_application_list('',{../item/@id})" class="NavLink"> 
					<xsl:value-of select="$lab_enrollment_approval"/>
				 </a> 
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:value-of select="$lab_title"/>
			</xsl:with-param>
		</xsl:call-template>
		
		
		
		<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="3" class="Bg">
			<tr>
				<td colspan="3" height="10">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
			<xsl:if test="/ims_log/data_import/@src_file">
				<tr>
					<td width="10%">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
					<td width="90%" colspan="2">
						<span class="Text">
							<p>
								<xsl:value-of select="$lab_msg1"/>
							</p>
							<p>
								<xsl:value-of select="$lab_msg2"/>
							</p>
						</span>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="success_entity/total != 0 and success_entity/total != '' ">
				<tr>
					<td width="10%">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
					<td width="20%">
						<span class="Text">
							<xsl:value-of select="$lab_success_entries"/>
							<xsl:text>&#160;:&#160;</xsl:text>
							<xsl:value-of select="$success_entity"/>
						</span>
					</td>
					<td width="70%">
						<xsl:choose>
							<xsl:when test="/ims_log/data_import/success_entity/log_file/uri != ''">
								<a class="Text" href="..{/ims_log/data_import/success_entity/log_file/uri}" target="_blank">
									<xsl:value-of select="$lab_view_log_file"/>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="unsuccess_entity/total != 0 and unsuccess_entity/total != '' ">
				<tr>
					<td width="10%">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
					<td width="20%">
						<span class="Text">
							<font color="red">
								<xsl:value-of select="$lab_failed_entries"/>
								<xsl:text>&#160;:&#160;</xsl:text>
								<xsl:value-of select="$unsuccess_entity"/>
							</font>
						</span>
					</td>
					<td width="70%">
						<xsl:choose>
							<xsl:when test="/ims_log/data_import/unsuccess_entity/log_file/uri != ''">
								<a class="Text" href="..{/ims_log/data_import/unsuccess_entity/log_file/uri}" target="_blank">
									<font color="red">
										<xsl:value-of select="$lab_view_log_file"/>
									</font>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="error/log_file/reason != ''">
				<tr class="wbRowRefBg">
					<td colspan="3">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
				</tr>
				<tr>
					<td width="10%">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
					<td colspan="2">
						<span class="Text">
							<font color="red">
								<xsl:value-of select="$lab_failed"/>
							</font>
						</span>
					</td>
				</tr>
				<tr>
					<td width="10%">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
					<td colspan="2">
						<span class="Text">
							<font color="red">
								<xsl:value-of select="$lab_reason"/>
								<xsl:text>&#160;:&#160;</xsl:text>
								<xsl:value-of select="/ims_log/data_import/error/reason"/>
							</font>
						</span>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td colspan="3" height="10">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_line"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td align="center" height="19" class="wbGenFooterBarBg">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Enrol.Import.prep('<xsl:value-of select="/ims_log/item/@id"/>')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
</xsl:stylesheet>
