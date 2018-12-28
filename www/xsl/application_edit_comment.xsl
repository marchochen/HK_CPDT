<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<!-- itm utils -->
	<xsl:import href="share/itm_gen_details_share.xsl"/>
	<xsl:variable name="itm_id" select="/applyeasy/application/item/nav/item/@id"/>
	<xsl:variable name="itm_title" select="/applyeasy/application/item/nav/item/title"/>
	<xsl:variable name="is_exam_ind" select="/applyeasy/application/item/nav/item/@exam_ind"/>
	
	<xsl:variable name="app_name" select="/applyeasy/application/applicant/display_name"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="no"/>
	<!-- ============================================= Var field===================================== -->
	<xsl:variable name="ach_upd_timestamp">
		<xsl:choose>
			<xsl:when test="(/applyeasy/application/comment)">
				<xsl:value-of select="/applyeasy/application/comment/@upd_timestamp"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="/applyeasy/application/action/@upd_timestamp"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- ===========================================generate HTML with template===============================================-->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ==========================================define the template=========================================== -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_lrn_soln.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_{$wb_cur_lang}.js"></script>
			<script language="Javascript" type="text/javascript"><![CDATA[app = new wbApplication; itm_lst = new wbItem;]]></script>
		</head>
		<!-- =======================================Main Body=================================================-->
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmSearch">
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="app_id" value="{/applyeasy/application/@app_id}"/>
				<input type="hidden" name="ach_id" value="{/applyeasy/application/comment/@id}"/>
				<input type="hidden" name="ach_aah_id" value="{/applyeasy/application/action/@id}"/>
				<input type="hidden" name="ach_upd_timestamp" value="{$ach_upd_timestamp}"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!--============== GB2312 & BIG5 & Eng  Here===================-->
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="applyeasy/application">
			<xsl:with-param name="lab_learner">学员</xsl:with-param>
			<xsl:with-param name="lab_process_application" >处理报名</xsl:with-param>
			<xsl:with-param name="lab_course">课程</xsl:with-param>
			<xsl:with-param name="lab_action">操作</xsl:with-param>
			<xsl:with-param name="lab_actBy">操作者</xsl:with-param>
			<xsl:with-param name="lab_actDate">操作日期</xsl:with-param>
			<xsl:with-param name="lab_modified">修改日期</xsl:with-param>
			<xsl:with-param name="lab_modifiedBy">修改者</xsl:with-param>
			<xsl:with-param name="lab_edit_com">修改备注</xsl:with-param>
			<xsl:with-param name="lab_desc">添加或修改备注。如果要删除备注，请清空备注的内容。</xsl:with-param>
			<xsl:with-param name="lab_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_reset">重置</xsl:with-param>
			<xsl:with-param name="lab_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_remark">修改备注</xsl:with-param>
			<xsl:with-param name="lab_add_remark">添加备注</xsl:with-param>
			<xsl:with-param name="lab_DIRECT_SUPERVISE">直属领导</xsl:with-param>
			<xsl:with-param name="lab_SUPERVISE">用户组领导</xsl:with-param>
			<xsl:with-param name="lab_TADM">培训管理员</xsl:with-param>
			<xsl:with-param name="lab_remark2">备注</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="applyeasy/application">
			<xsl:with-param name="lab_learner">學員</xsl:with-param>
			<xsl:with-param name="lab_course">課程</xsl:with-param>
			<xsl:with-param name="lab_process_application" >處理報名</xsl:with-param>
			<xsl:with-param name="lab_action">操作</xsl:with-param>
			<xsl:with-param name="lab_actBy">操作者</xsl:with-param>
			<xsl:with-param name="lab_actDate">操作日期</xsl:with-param>
			<xsl:with-param name="lab_modified">修改日期</xsl:with-param>
			<xsl:with-param name="lab_modifiedBy">修改者</xsl:with-param>
			<xsl:with-param name="lab_edit_com">修改備註</xsl:with-param>
			<xsl:with-param name="lab_desc">添加或修改備註信息。如果要刪除備註，請清空備註的內容。</xsl:with-param>
			<xsl:with-param name="lab_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_reset">重置</xsl:with-param>
			<xsl:with-param name="lab_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_remark">修改備註</xsl:with-param>
			<xsl:with-param name="lab_add_remark">添加備註</xsl:with-param>
			<xsl:with-param name="lab_DIRECT_SUPERVISE">直屬上司</xsl:with-param>
			<xsl:with-param name="lab_SUPERVISE">用戶組上司</xsl:with-param>
			<xsl:with-param name="lab_TADM">培訓管理員</xsl:with-param>
			<xsl:with-param name="lab_remark2">備註</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="applyeasy/application">
			<xsl:with-param name="lab_learner">Learner</xsl:with-param>
			<xsl:with-param name="lab_course">Course</xsl:with-param>
			<xsl:with-param name="lab_action">Action</xsl:with-param>
			<xsl:with-param name="lab_process_application" >Process enrollment</xsl:with-param>
			<xsl:with-param name="lab_actBy">Action by</xsl:with-param>
			<xsl:with-param name="lab_actDate">Action date</xsl:with-param>
			<xsl:with-param name="lab_modified">Modified</xsl:with-param>
			<xsl:with-param name="lab_modifiedBy">Modified by</xsl:with-param>
			<xsl:with-param name="lab_edit_com">Edit remark</xsl:with-param>
			<xsl:with-param name="lab_desc">To insert or edit the remark. To delete the remark, please clear its content.</xsl:with-param>
			<xsl:with-param name="lab_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_reset">Reset</xsl:with-param>
			<xsl:with-param name="lab_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_remark">Edit remark</xsl:with-param>
			<xsl:with-param name="lab_add_remark">Add new remark</xsl:with-param>
			<xsl:with-param name="lab_DIRECT_SUPERVISE">Direct supervisor</xsl:with-param>
			<xsl:with-param name="lab_SUPERVISE">Group supervisor</xsl:with-param>
			<xsl:with-param name="lab_TADM">Training administrator</xsl:with-param>
			<xsl:with-param name="lab_remark2">remark</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ==========================================title and headers======================================== -->
	<xsl:template match="applyeasy/application">
		<xsl:param name="lab_learner"/>
		<xsl:param name="lab_course"/>
		<xsl:param name="lab_action"/>
		<xsl:param name="lab_actBy"/>
		<xsl:param name="lab_actDate"/>
		<xsl:param name="lab_modified"/>
		<xsl:param name="lab_modifiedBy"/>
		<xsl:param name="lab_edit_com"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_ok"/>
		<xsl:param name="lab_cancel"/>
		<xsl:param name="lab_reset"/>
		<xsl:param name="lab_remark"/>
		<xsl:param name="lab_add_remark"/>
		<xsl:param name="lab_DIRECT_SUPERVISE"/>
		<xsl:param name="lab_SUPERVISE"/>
		<xsl:param name="lab_TADM"/>
		<xsl:param name="lab_remark2"/>
		<xsl:param name="lab_process_application" />
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">
				<xsl:choose>
					<xsl:when test="$is_exam_ind='true'">FTN_AMD_EXAM_MGT</xsl:when>
					<xsl:otherwise>FTN_AMD_ITM_COS_MAIN</xsl:otherwise>
				</xsl:choose>
			
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_edit_com"/>
		</xsl:call-template>
	<!-- <xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_desc"/>
		</xsl:call-template>   	
		<xsl:call-template name="wb_ui_space"/>   -->
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
			<!-- 返回课程详细信息连接 -->
			<a href="Javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
				<xsl:value-of select="$itm_title"/>
			</a>
			<xsl:text>&#160;&gt;&#160;</xsl:text>
			<!-- 返回处理报名连接 -->
			<a href="Javascript:app.get_application_list('',{$itm_id},'','','','')" class="NavLink">
				<xsl:value-of select="$lab_process_application"/>
			</a>
			<xsl:text>&#160;&gt;&#160;</xsl:text>
			<!-- 返回当前的报名用户名连接 -->
			<a href="javascript:app.process_application('{@app_id}')" class="NavLink">
				<xsl:value-of select="$app_name"/>
			</a>
			<xsl:text>&#160;&gt;&#160;</xsl:text>
			<xsl:value-of select="$lab_edit_com"/>
			</xsl:with-param>
		</xsl:call-template>
		<!-- <xsl:call-template name="wb_ui_line"/>  -->
		<!-- ===================================Main Content================================ -->
		<table>
			<tr>
				<td>
					<input type="hidden" name="lab_remark" value="{$lab_remark2}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
						<xsl:value-of select="$lab_learner"/>
						<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="applicant/display_name"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_course"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="item/nav/item/title"/>
					<xsl:if test="(item/nav/item[2])"> - <xsl:value-of select="item/nav/item[2]/title"/>
					</xsl:if>
				</td>
			</tr>
			<xsl:if test="(action)">
				<tr>
					<td class="wzb-form-label">
						<xsl:value-of select="$lab_action"/>
						<xsl:text>：</xsl:text>
					</td>
					<td class="wzb-form-control">
						<xsl:value-of select="action/@verb"/>
					</td>
				</tr>
				<tr>
					<td class="wzb-form-label">
						<xsl:value-of select="$lab_actDate"/>
						<xsl:text>：</xsl:text>
					</td>
					<td class="wzb-form-control">
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp">
								<xsl:value-of select="action/@upd_timestamp"/>
							</xsl:with-param>
							<xsl:with-param name="dis_time">T</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
				<tr>
					<td class="wzb-form-label">
						<xsl:value-of select="$lab_actBy"/>
						<xsl:text>：</xsl:text>
					</td>
					<td class="wzb-form-control">
						<xsl:value-of select="action/display_name"/>
						<xsl:choose>
							<xsl:when test="action/approval_role = 'DIRECT_SUPERVISE'">
												(<xsl:value-of select="$lab_DIRECT_SUPERVISE"/>)
											</xsl:when>
							<xsl:when test="action/approval_role = 'TADM'">
												(<xsl:value-of select="$lab_TADM"/>)
											</xsl:when>
							<xsl:when test="action/approval_role = 'SUPERVISE'">
												(<xsl:value-of select="$lab_SUPERVISE"/>)
											</xsl:when>
						</xsl:choose>
					</td>
				</tr>
			</xsl:if>
			<xsl:choose>
				<xsl:when test="not (comment)">
					<td class="wzb-form-label" valign="top">
						<xsl:value-of select="$lab_add_remark"/>
						<xsl:text>：</xsl:text>
					</td>
					<td class="wzb-form-control">
						<textarea cols="50" rows="10" class="wzb-inputTextArea" name="ach_content"/>
					</td>
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_modified"/>
							<xsl:text>：</xsl:text>
						</td>
						<td class="wzb-form-control">
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="comment/@upd_timestamp"/>
								</xsl:with-param>
								<xsl:with-param name="dis_time">T</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_modifiedBy"/>
							<xsl:text>：</xsl:text>
						</td>
						<td class="wzb-form-control">
							<xsl:value-of select="comment/display_name"/>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$lab_remark"/>
							<xsl:text>：</xsl:text>
						</td>
						<td class="wzb-form-control">
							<textarea cols="50" rows="10" class="wzb-inputTextArea" name="ach_content">
								<xsl:value-of select="comment/content"/>
							</textarea>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
		</table>
		<div class='wzb-bar'>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:app.upd_comment(frmSearch)</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:app.process_application('<xsl:value-of select="@app_id"/>')</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
</xsl:stylesheet>
