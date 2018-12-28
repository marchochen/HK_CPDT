<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ns1="http://www.cyberwisdom.net" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="share/wb_module_status_const.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="upload_success_cnt" select="count(Upload/upload_enrollment/enterprise/membership)"/>
	<xsl:variable name="upload_failure_cnt" select="count(/Upload/upload_enrollment/failure_list/failure/line)"/>
	<xsl:variable name="credit_type_list" select="Upload/upload_enrollment/credit_type_list"/>
	<xsl:variable name="item_code_ref_list" select="Upload/upload_enrollment/item_code_ref_list"/>
	<xsl:variable name="total_upload_cnt" select="$upload_success_cnt + $upload_failure_cnt"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="Upload/upload_enrollment"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="upload_enrollment">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<link rel="stylesheet" href="../static/css/base.css"/>
			<link rel="stylesheet" href="{$wb_new_admin_css}admin.css"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_no_upload_record">沒有記錄</xsl:with-param>
			<xsl:with-param name="lab_col_course_title">課程標題</xsl:with-param>
			<xsl:with-param name="lab_col_class_title">類別標題</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_no_upload_record">没有记录</xsl:with-param>
			<xsl:with-param name="lab_col_course_title">课程标题</xsl:with-param>
			<xsl:with-param name="lab_col_class_title">类别标题</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_no_upload_record">Record not found</xsl:with-param>
			<xsl:with-param name="lab_col_course_title">Course title</xsl:with-param>
			<xsl:with-param name="lab_col_class_title">Class title</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_no_upload_record"/>
		<xsl:param name="lab_col_course_title"/>
		<xsl:param name="lab_col_class_title"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:choose>
			<xsl:when test="count(enterprise/membership/member) &gt;=1">
				<table border="0" cellspacing="0" cellpadding="3" class="table wzb-ui-table margin-top28" width="{$wb_gen_table_width}">
					<tr class="wzb-ui-table-head">
						<td width="4">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<xsl:apply-templates select="used_column/column[text() != 'Role Type']" mode="draw_header">
							<xsl:with-param name="lab_col_course_title" select="$lab_col_course_title"/>
							<xsl:with-param name="lab_col_class_title" select="$lab_col_class_title"/>
						</xsl:apply-templates>
						<td width="4">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<xsl:apply-templates select="enterprise"/>
				</table>
				<xsl:call-template name="wb_ui_line"/>
                &#160;
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_upload_record"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="3">
			<tr>
				<td align="center">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_close"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:window.close()</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="enterprise">
		<xsl:apply-templates select="membership/member"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="used_column"/>
	<!-- =============================================================== -->
	<xsl:template match="member">
		<xsl:variable name="member" select="."/>
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2">RowsEven</xsl:when>
				<xsl:otherwise>RowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr valign="middle" class="{$row_class}">
			<td width="4">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<xsl:apply-templates select="../../../used_column/column" mode="draw_item">
				<xsl:with-param name="member" select="$member"/>
				<xsl:with-param name="my_id" select="../sourcedid/id"/>
			</xsl:apply-templates>
			<td width="4">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="column" mode="draw_header">
		<xsl:param name="lab_col_course_title"/>
		<xsl:param name="lab_col_class_title"/>
		<td nowrap="nowrap">
			<span class="wbRowheadBarSText">
				<xsl:value-of select="text()"/>
			</span>
		</td>
		<xsl:if test="text() = 'Learning Solution Code'">
			<td nowrap="nowrap">
				<span class="wbRowheadBarSText">
					<xsl:value-of select="$lab_col_course_title"/>
				</span>
			</td>
			<td nowrap="nowrap">
				<span class="wbRowheadBarSText">
					<xsl:value-of select="$lab_col_class_title"/>
				</span>
			</td>
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="column" mode="draw_item">
		<xsl:param name="staff_id"/>
		<xsl:param name="member"/>
		<xsl:param name="my_id"/>
		<xsl:choose>
			<xsl:when test="@id = '0'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$member/sourcedid/id"/>
					</span>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Learning Solution Code'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$member/../sourcedid/id"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td nowrap="nowrap">
					<span class="Text">
						<xsl:value-of select="$item_code_ref_list/item_code_ref[code = $my_id]/parent_title"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td nowrap="nowrap">
					<span class="Text">
						<xsl:value-of select="$item_code_ref_list/item_code_ref[code = $my_id]/title"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '1'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$member/role/finalresult[mode = 'Enrollment Status']/result"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Enrollment Date'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:variable name="str_length" select="string-length($member/role/extension/ns1:extension/ns1:enrollmentdate)"/>
						<xsl:value-of select="substring(translate($member/role/extension/ns1:extension/ns1:enrollmentdate,'T',' '),0,$str_length - 2)"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '2'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:if test="$member/role/finalresult[mode = 'Completion Status']/result != ''">
							<xsl:variable name="status">
								<xsl:value-of select="$member/role/finalresult[mode = 'Completion Status']/result"/>
							</xsl:variable>
							<xsl:variable name="status_id">
								<xsl:choose>
									<xsl:when test="$status = 'I'">2</xsl:when>
									<xsl:when test="$status = 'C'">1</xsl:when>
									<xsl:when test="$status = 'F'">3</xsl:when>
									<xsl:when test="$status = 'W'">4</xsl:when>
								</xsl:choose>
							</xsl:variable>
							<xsl:call-template name="get_ats_title">
							<xsl:with-param name="ats_id" select="$status_id"/>
						</xsl:call-template>
						</xsl:if>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '3'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:variable name="str_length" select="string-length($member/role/extension/ns1:extension/ns1:completiondate)"/>
						<xsl:value-of select="substring(translate($member/role/extension/ns1:extension/ns1:completiondate,'T',' '),0,$str_length - 2)"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '4'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$member/role/finalresult[mode = 'Completion Status']/comments"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '5'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$member/role/finalresult[mode = 'Assessment Result']/result"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '6'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$member/role/finalresult[mode = 'Attendance Rate']/result"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="substring-before(text(),' ') = 'Metric'">
				<xsl:variable name="my_id" select="substring-after(text(),' ')"/>
				<xsl:variable name="credit_type_list_id" select="$credit_type_list/credit_type[@seq_id = $my_id]/@type"/>
				<xsl:variable name="credit_type_name" select="concat('Accreditation-',$credit_type_list_id)"/>
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$member/role/finalresult[mode = $credit_type_name]/result"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<!--enrollment workflow-->
			<xsl:when test="@id = '7'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$member/role/extension/ns1:extension/ns1:enrollmentworkflow"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<!--send mail-->
			<xsl:when test="@id = '8'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$member/role/extension/ns1:extension/ns1:sendmail"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:otherwise>
				<td nowrap="nowrap">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="status">
		<xsl:value-of select="desc[@lan = $wb_lang_encoding]/@name"/>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
