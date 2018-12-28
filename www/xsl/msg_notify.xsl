<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="usr_id" select="/message/cur_usr/@usr_id"/>
	<xsl:variable name="msg_type" select="/message/@type"/>
	<xsl:variable name="id" select="/message/@id"/>
	<xsl:variable name="id_type" select="/message/@id_type"/>
	<xsl:variable name="encoding" select="$wb_lang_encoding"/>
	<xsl:variable name="root_ent_id" select="/message/cur_usr/@root_ent_id"/>
	<xsl:variable name="style" select="/message/cur_usr/@style"/>
	<xsl:variable name="recipient_cnt" select="count(/message/default_recipients/recipient)"/>
	<xsl:variable name="timestamp" select="/message/@timestamp"/>
	<xsl:variable name="wb_gen_table_width">
		<xsl:choose>
			<xsl:when test="$msg_type = 'course_approval_request'">760</xsl:when>
			<xsl:otherwise>100%</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:apply-templates select="message"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="message">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<head>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_message.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			
			<!--alert样式  -->
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			<!--alert样式  end -->
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_rm_{$wb_cur_lang}.js"></script>
			
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[	
			var msg = new wbMessage
			var goldenman = new wbGoldenMan
			usr = new wbUserGroup
			
			function getPopupUsrLst(fld_name, id_lst, nm_lst) {wbMsgInsPopupSrchUsrLst(fld_name, id_lst, nm_lst);}			
			
			function status() {return false;}
			
			function init(){
				frm = document.frmXml
				]]><xsl:if test="$recipient_cnt = 1 and  $msg_type = 'comment'"><![CDATA[
					if (frm.ent_ids_lst_single) {frm.ent_ids_lst_single.value = ']]><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="default_recipients/recipient/@display_bil"/></xsl:with-param></xsl:call-template><![CDATA['}
					if (frm.ent_ids_lst.options[0]) {frm.ent_ids_lst.options[0].text = ']]><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="default_recipients/recipient/@display_bil"/></xsl:with-param></xsl:call-template><![CDATA['}
					if (frm.ent_ids_lst.options[0]) {frm.ent_ids_lst.options[0].value = ']]><xsl:value-of select="default_recipients/recipient/@ent_id"/><![CDATA['}
				]]></xsl:if><![CDATA[				
			}								
		]]></SCRIPT>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="new_css"/>
			<style type="text/css">
			    html {overflow:-moz-scrollbars-vertical;}   <!--火狐浏览器强制显示滚动条  -->
			</style>
		</head>
		<BODY style="overflow:auto;" marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="init()">
			<FORM onsubmit="return status()" name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="msg_type" value="{$msg_type}"/>
				<input type="hidden" name="sender_id" value="{$usr_id}"/>
				<input type="hidden" name="url_redirect" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="cur_timestamp" value="{$timestamp}"/>
				<input type="hidden" name="msg_bcc_sys_ind" value=""/>
				<xsl:if test="$msg_type != 'comment' and $msg_type != 'notify'">
					<input type="hidden" name="id" value="{$id}"/>
					<input type="hidden" name="id_type" value="{$id_type}"/>
					<input type="hidden" name="encoding" value="{$encoding}"/>
					<input type="hidden" name="root_ent_id" value="{$root_ent_id}"/>
					<input type="hidden" name="style" value="{$style}"/>
					<input type="hidden" name="url_cmd" value=""/>
				</xsl:if>
			</FORM>
		</BODY>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_comment">意見</xsl:with-param>
			<xsl:with-param name="lab_notify">通知</xsl:with-param>
			<xsl:with-param name="lab_required">為必填</xsl:with-param>
			<xsl:with-param name="lab_details">詳情</xsl:with-param>
			<xsl:with-param name="lab_request_appr">請求批准</xsl:with-param>
			<xsl:with-param name="lab_invit_notify">邀請目標學員</xsl:with-param>
			<xsl:with-param name="lab_to">接收用戶/小組</xsl:with-param>
			<xsl:with-param name="lab_email_cc">抄送</xsl:with-param>
			<xsl:with-param name="lab_email_cc_tip">(如果需要填寫多個電郵地址請以逗號“,”分隔)</xsl:with-param>
			<xsl:with-param name="lab_subject">主題</xsl:with-param>
			<xsl:with-param name="lab_by">方法</xsl:with-param>
			<xsl:with-param name="lab_msg_bdy">內容</xsl:with-param>
			<xsl:with-param name="lab_sched">發送日期</xsl:with-param>
			<xsl:with-param name="lab_now">現在發送</xsl:with-param>
			<xsl:with-param name="lab_on">在</xsl:with-param>
			<xsl:with-param name="lab_date_format">年-月-日</xsl:with-param>
			<xsl:with-param name="lab_append">增加</xsl:with-param>
			<xsl:with-param name="lab_clear">清除</xsl:with-param>
			<xsl:with-param name="lab_cc">副本</xsl:with-param>
			<xsl:with-param name="lab_to_be_send">將發送日期</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_send">傳送</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_comment">意见</xsl:with-param>
			<xsl:with-param name="lab_notify">通知</xsl:with-param>
			<xsl:with-param name="lab_required">为必填</xsl:with-param>
			<xsl:with-param name="lab_details">详情</xsl:with-param>
			<xsl:with-param name="lab_request_appr">请求批准</xsl:with-param>
			<xsl:with-param name="lab_invit_notify">邀请目标学员</xsl:with-param>
			<xsl:with-param name="lab_to">接收用户/小组</xsl:with-param>
			<xsl:with-param name="lab_email_cc">抄送</xsl:with-param>
			<xsl:with-param name="lab_email_cc_tip">(如果需要填写多个电邮地址请以逗号“,”分隔)</xsl:with-param>
			<xsl:with-param name="lab_subject">主题</xsl:with-param>
			<xsl:with-param name="lab_by">方法</xsl:with-param>
			<xsl:with-param name="lab_msg_bdy">内容</xsl:with-param>
			<xsl:with-param name="lab_sched">发送日期</xsl:with-param>
			<xsl:with-param name="lab_now">现在发送</xsl:with-param>
			<xsl:with-param name="lab_on">在</xsl:with-param>
			<xsl:with-param name="lab_date_format">年-月-日</xsl:with-param>
			<xsl:with-param name="lab_append">增加</xsl:with-param>
			<xsl:with-param name="lab_clear">清除</xsl:with-param>
			<xsl:with-param name="lab_cc">副本</xsl:with-param>
			<xsl:with-param name="lab_to_be_send">将发送日期</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_send">发送</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_comment">Comment</xsl:with-param>
			<xsl:with-param name="lab_notify">Notify</xsl:with-param>
			<xsl:with-param name="lab_required">Required</xsl:with-param>
			<xsl:with-param name="lab_details">Details</xsl:with-param>
			<xsl:with-param name="lab_request_appr">Request approval</xsl:with-param>
			<xsl:with-param name="lab_invit_notify">Invite target learners</xsl:with-param>
			<xsl:with-param name="lab_to">To</xsl:with-param>
			<xsl:with-param name="lab_email_cc">CC</xsl:with-param>
			<xsl:with-param name="lab_email_cc_tip">(Please use comma "," to separate them if more than 1 email address)</xsl:with-param>
			<xsl:with-param name="lab_subject">Subject</xsl:with-param>
			<xsl:with-param name="lab_by">By</xsl:with-param>
			<xsl:with-param name="lab_msg_bdy">Content</xsl:with-param>
			<xsl:with-param name="lab_sched">Schedule</xsl:with-param>
			<xsl:with-param name="lab_now">Immediate</xsl:with-param>
			<xsl:with-param name="lab_on">on</xsl:with-param>
			<xsl:with-param name="lab_date_format">YYYY-MM-DD</xsl:with-param>
			<xsl:with-param name="lab_append">Append</xsl:with-param>
			<xsl:with-param name="lab_clear">Remove</xsl:with-param>
			<xsl:with-param name="lab_cc">CC</xsl:with-param>
			<xsl:with-param name="lab_to_be_send">To be sent</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_send">Send</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">Back</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_comment"/>
		<xsl:param name="lab_notify"/>
		<xsl:param name="lab_required"/>
		<xsl:param name="lab_details"/>
		<xsl:param name="lab_request_appr"/>
		<xsl:param name="lab_invit_notify"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_email_cc"/>
		<xsl:param name="lab_email_cc_tip"/>
		<xsl:param name="lab_subject"/>
		<xsl:param name="lab_by"/>
		<xsl:param name="lab_msg_bdy"/>
		<xsl:param name="lab_sched"/>
		<xsl:param name="lab_now"/>
		<xsl:param name="lab_on"/>
		<xsl:param name="lab_date_format"/>
		<xsl:param name="lab_append"/>
		<xsl:param name="lab_clear"/>
		<xsl:param name="lab_cc"/>
		<xsl:param name="lab_to_be_send"/>
		<xsl:param name="lab_g_form_btn_send"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<!-- 导航栏 -->
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
		</xsl:call-template>
		<!-- header -->
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$msg_type = 'comment'">
						<xsl:value-of select="$lab_comment"/>
					</xsl:when>
					<xsl:when test="$msg_type = 'notify'">
						<xsl:value-of select="$lab_notify"/>
					</xsl:when>
					<xsl:when test="$msg_type = 'enrollment_notify'">
						<xsl:value-of select="$lab_notify"/>
					</xsl:when>
					<xsl:when test="$msg_type = 'course_approval_request'">
						<xsl:value-of select="$lab_request_appr"/>
						<xsl:text>&#160;-&#160;</xsl:text>
						<xsl:value-of select="@title"/>
					</xsl:when>
					<xsl:when test="$msg_type = 'invite_target_learner'">
						<xsl:value-of select="$lab_invit_notify"/>
					</xsl:when>
					<xsl:when test="$msg_type = 'link_notify'">
						<xsl:value-of select="$lab_notify"/>
						<xsl:text>&#160;-&#160;</xsl:text>
						<xsl:value-of select="@title"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_notify"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width">
				<xsl:value-of select="$wb_gen_table_width"/>
			</xsl:with-param>
		</xsl:call-template>
		<table class="Bg" cellspacing="0" cellpadding="3" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td width="20%" align="right" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="80%" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
			<xsl:choose>
				<xsl:when test="$msg_type = 'comment'">
					<script language="javascript"><![CDATA[
					function FakeOption(){
						this.length = 1;
						this.value = null;
						this.text = null;
					}					
					function FakeSelect(){
						this.options = new Array();
						this.options[0] =  new FakeOption();
					}					
					document.frmXml.ent_ids_lst = new FakeSelect()
				]]></script>
					<input type="hidden" value="" name="ent_ids_lst_single"/>
					<input type="hidden" name="ent_ids" value=""/>
				</xsl:when>
				<xsl:otherwise>
					<!-- To -->
					<tr>
						<td class="wzb-form-label" valign="top">
						   <span class="wzb-form-star">*</span>
							<span class="TitleText">
								<xsl:value-of select="$lab_to"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td class="wzb-form-control" >
							<span class="Text">
								<xsl:call-template name="wb_goldenman">
									<xsl:with-param name="frm">document.frmXml</xsl:with-param>
									<xsl:with-param name="width">320</xsl:with-param>
									<xsl:with-param name="height">120</xsl:with-param>
									<xsl:with-param name="field_name">ent_ids_lst</xsl:with-param>
									<xsl:with-param name="tree_type">user_group_and_user</xsl:with-param>
									<xsl:with-param name="select_type">1</xsl:with-param>
									<xsl:with-param name="box_size">
										<xsl:choose>
											<xsl:when test="$msg_type = 'comment' and $recipient_cnt = 1">1</xsl:when>
											<xsl:otherwise>3</xsl:otherwise>
										</xsl:choose>
									</xsl:with-param>
									<xsl:with-param name="add_btn">
										<xsl:choose>
											<xsl:when test="$msg_type = 'comment'">false</xsl:when>
											<xsl:otherwise>true</xsl:otherwise>
										</xsl:choose>
									</xsl:with-param>
									<xsl:with-param name="remove_btn">
										<xsl:choose>
											<xsl:when test="$msg_type = 'comment'">false</xsl:when>
											<xsl:otherwise>true</xsl:otherwise>
										</xsl:choose>
									</xsl:with-param>
									<xsl:with-param name="option_list">
										<xsl:if test="$recipient_cnt &gt;= 1">
											<xsl:for-each select="default_recipients/recipient[not(preceding-sibling::*/@ent_id = ./@ent_id)]">
												<option value="{@ent_id}">
													<xsl:value-of select="@display_bil"/>
												</option>
											</xsl:for-each>
										</xsl:if>
									</xsl:with-param>
									<xsl:with-param name="search">true</xsl:with-param>
									<xsl:with-param name="pick_leave">0</xsl:with-param>
									<xsl:with-param name="search_function">javascript:usr.search.popup_search_prep('ent_ids_lst','','<xsl:value-of select="$root_ent_id"/>')</xsl:with-param>
								</xsl:call-template>
							</span>
							<input type="hidden" name="ent_ids" value=""/>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
			<!-- <xsl:if test="$msg_type != 'comment'">
				CC
				 <tr><td><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td></tr>
				<tr>
					<td width="20%" align="center" valign="top">
						<span class="TitleText">
							<xsl:value-of select="$lab_cc"/>
							<xsl:text>：</xsl:text>
						</span>
					</td>
					<td width="80%">
						<span class="Text">
							<xsl:call-template name="wb_goldenman">
								<xsl:with-param name="frm">document.frmXml</xsl:with-param>
								<xsl:with-param name="width">320</xsl:with-param>
								<xsl:with-param name="field_name">cc_ent_ids_lst</xsl:with-param>
								<xsl:with-param name="tree_type">user_group_and_user</xsl:with-param>
								<xsl:with-param name="select_type">1</xsl:with-param>
								<xsl:with-param name="box_size">3</xsl:with-param>
								<xsl:with-param name="search">false</xsl:with-param>
								<xsl:with-param name="search">true</xsl:with-param>
								<xsl:with-param name="pick_leave">0</xsl:with-param>
								<xsl:with-param name="search_function">javascript:usr.search.popup_search_prep('cc_ent_ids_lst','','<xsl:value-of select="$root_ent_id"/>')</xsl:with-param>
							</xsl:call-template>
						</span>
						<input type="hidden" name="cc_ent_ids" value=""/>
					</td>
				</tr>
			</xsl:if> -->
			
			<!-- CC -->
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_email_cc"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td class="wzb-form-control">
					<input type="text" class="wzb-inputText" name="cc_email_address" style="width:320px;" size="20" maxlength="2000"/>
					<br/>
					<xsl:value-of select="$lab_email_cc_tip"/>
				</td>
			</tr>
			
			<!-- Subject -->
			<xsl:choose>
				<xsl:when test="$msg_type = 'course_approval_request'">
					<input type="hidden" name="msg_subject" value="From LENS (Learning &amp; Education NetworkS) - Approval of Executive Summary"/>
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td class="wzb-form-label">
						    <span class="wzb-form-star">*</span>
							<span class="TitleText">
								<xsl:value-of select="$lab_subject"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td class="wzb-form-control">
							<input type="text" class="wzb-inputText" name="msg_subject" style="width:320px;" size="20" maxlength="100"/>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
			<!-- Content -->
			<xsl:choose>
				<xsl:when test="$msg_type = 'course_approval_request'">
					<input type="hidden" name="msg_body"/>
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td class="wzb-form-label" valign="top">
						   <span class="wzb-form-star">*</span>
							<span class="TitleText">
								<xsl:choose>
									<xsl:when test="$msg_type = 'comment'">
										<xsl:value-of select="$lab_comment"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_msg_bdy"/>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td class="wzb-form-control">
							<textarea class="wzb-inputTextArea" name="msg_body" style="width:320;" wrap="VIRTUAL" cols="50" rows="7">
								<xsl:value-of select="message_body/body"/>
							</textarea>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:if test="$msg_type != 'comment'">
				<input type="hidden" name="msg_datetime_date" value=""/>
			</xsl:if>
			<!-- *required -->
			<tr>
				<td class="wzb-form-label" align="right">
					<span class="TitleText">
						<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					</span>
				</td>
				<td class="wzb-form-control">
					<span class="wzb-ui-desc-text">
						<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
						<span class="wzb-form-star">*</span>
						<xsl:value-of select="$lab_required"/>
					</span>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="80%" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_line"/>
		<br></br>
		<table cellpadding="3" cellspacing="0" width="{$wb_gen_table_width}" border="0">
			<tr>
				<td align="center">
					<xsl:choose>
						<xsl:when test="$msg_type = 'link_notify' or $msg_type = 'course_approval_request' or $msg_type = 'invite_target_learner'">
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_send"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:msg.ins_lnk_notify_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$msg_type"/>')</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_send"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:msg.ins_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$msg_type"/>','true')</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					<xsl:choose>
						<xsl:when test="$msg_type = 'course_approval_request'">
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_back"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:history.go(-2)</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:self.close()</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
