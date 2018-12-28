<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">

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
	<xsl:import href="utils/wb_gen_input_file.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:output indent="yes"/>
	
	<xsl:variable name="cur_lan" select="/Upload/meta/cur_usr/@curLan"/>
	<xsl:variable name="cur_site" select="/Upload/meta/cur_usr/@root_id"/>
	<xsl:variable name="ent_id" select="/Upload/meta/cur_usr/@ent_id"/>
	
	<xsl:variable name="label_core_user_management_50" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_um.label_core_user_management_50')"/>
	<xsl:variable name="label_core_user_management_51" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_um.label_core_user_management_51')"/>
	<xsl:variable name="label_core_user_management_52" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_um.label_core_user_management_52')"/>
	<xsl:variable name="label_core_user_management_53" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_um.label_core_user_management_53')"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="Upload">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_batchprocess.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript"  src="{$wb_js_path}wb_usergroup.js"/>
			<script language="Javascript" TYPE="text/javascript"><![CDATA[
			Batch = new wbBatchProcess;
			usr = new wbUserGroup
		]]></script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frmXml" onsubmit="return false" enctype="multipart/form-data">
				<input type="hidden" name="rename" value="NO"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="src_file"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_file_location">文檔位置</xsl:with-param>
			<xsl:with-param name="lab_desc"><i style="color:#64BF0F;">提示：</i>在下面指定包含用戶資訊的文檔。該檔必須符合指定的試算表範本格式。</xsl:with-param>
			<xsl:with-param name="lab_title">匯入用戶資訊 - 第一步：用戶檔上載</xsl:with-param>
			<xsl:with-param name="lab_description">描述</xsl:with-param>
			<xsl:with-param name="lab_option_1">對系統中已存在的記錄執行更新操作</xsl:with-param>
			<xsl:with-param name="lab_option_2">對系統中已存在的記錄作為錯誤記錄處理</xsl:with-param>
			<xsl:with-param name="lab_desc_requirement">不超過1000個中文字（2000個字元）</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction3">對於導入的新用戶：如果不指定密碼，將會使用系統默認密碼。</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction">上載文檔內的記錄數目上限為<xsl:value-of select="max_upload_count"/>。如要上載更多的記錄，請分批上載。</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction2">注意﹕系統會在背景處理上載的請求。 當完成上載的請求後﹐系統會以電郵通知你。</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_log">查看記錄</xsl:with-param>
			<xsl:with-param name="lab_template">文檔範本</xsl:with-param>
			<xsl:with-param name="lab_instr">範本說明</xsl:with-param>
			<xsl:with-param name="lab_usr_pwd_need_change_ind">新使用者第一次登錄時必須修改密碼</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_file_location">文件位置</xsl:with-param>
			<xsl:with-param name="lab_desc"><i style="color:#64BF0F;">提示：</i>在下面指定包含用户信息的文件。该文件必须符合指定的电子表格模板格式。</xsl:with-param>
			<xsl:with-param name="lab_title">导入用户信息 - 第一步：用户文件上载</xsl:with-param>
			<xsl:with-param name="lab_description">描述</xsl:with-param>
			<xsl:with-param name="lab_option_1">对系统中已存在的记录执行更新操作</xsl:with-param>
			<xsl:with-param name="lab_option_2">对系统中已存在的记录作为错误记录处理</xsl:with-param>
			<xsl:with-param name="lab_desc_requirement">不超过1000个中文字(2000个字符)</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction3">在用户导入中，未指定密码的新用户将使用默认密码作为该用户的登录密码。</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction">上载文件内的记录数目上限为<xsl:value-of select="max_upload_count"/>。如要上载更多的记录，请分批上载。</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction2">注意：系统会在后台处理上载的请求。 当完成上载的请求后，系统会以电邮通知你。</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_log">查看记录</xsl:with-param>
			<xsl:with-param name="lab_template">文件模板</xsl:with-param>
			<xsl:with-param name="lab_instr">模板说明</xsl:with-param>
			<xsl:with-param name="lab_usr_pwd_need_change_ind">新用户第一次登录时必须修改密码</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_next">Next</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_file_location">File location</xsl:with-param>
			<xsl:with-param name="lab_desc"><i style="color:#64BF0F;">Reminder：</i>Specify the file containing the user profiles below. The file must be in a specific format according to a spreadsheet template.</xsl:with-param>
			<xsl:with-param name="lab_title">Import user profile - step 1: file upload</xsl:with-param>
			<xsl:with-param name="lab_description">Description</xsl:with-param>
			<xsl:with-param name="lab_option_1">Handle records that are found existing in system as data update</xsl:with-param>
			<xsl:with-param name="lab_option_2">Handle records that are found existing in system as errors</xsl:with-param>
			<xsl:with-param name="lab_desc_requirement">Not more than 2000 characters</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction3">A new user who does not specify a password will use that default password as login password.</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction">Maximum number of records allowed in the file is <xsl:value-of select="max_upload_count"/>. Please upload in separate batches if more records are intended.</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction2">Note: The import process will be conducted in a separate background job . A notification email will be sent to you upon completion.</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_log">View history</xsl:with-param>
			<xsl:with-param name="lab_template">File template</xsl:with-param>
			<xsl:with-param name="lab_instr">Template instruction</xsl:with-param>
			<xsl:with-param name="lab_usr_pwd_need_change_ind">New user must change password at first logon</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_g_form_btn_next"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_file_location"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_desc_requirement"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_description"/>
		<xsl:param name="lab_option_1"/>
		<xsl:param name="lab_option_2"/>
		<xsl:param name="lab_upload_instruction"/>
		<xsl:param name="lab_upload_instruction2"/>
		<xsl:param name="lab_upload_instruction3"/>
		<xsl:param name="lab_g_txt_btn_log"/>
		<xsl:param name="lab_template"/>
		<xsl:param name="lab_instr"/>
		<xsl:param name="lab_usr_pwd_need_change_ind"/>
		
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_USR_INFO</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_USR_INFO</xsl:with-param>
			<xsl:with-param name="page_title">
				<xsl:call-template name="get_lab">
					<xsl:with-param name="lab_title">label_um.label_core_user_management_1</xsl:with-param>
				</xsl:call-template>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_title"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_desc"/>
		</xsl:call-template>
		<table>
		<tr>	
				<td class="wzb-form-label">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_template"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:Batch.User.Import.get_tpl('<xsl:value-of select="$cur_lan"/>', '<xsl:value-of select="$cur_site"/>')</xsl:with-param>
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_instr"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:Batch.User.Import.get_instr()</xsl:with-param>
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_log"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:Batch.User.Log.get_log()</xsl:with-param>
						<xsl:with-param name="class">btn wzb-btn-orange </xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>

		<table class=""> <!-- margin-top28 -->
		
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_file_location"/>：
				</td>
				<td class="wzb-form-control" >
					<xsl:call-template name="wb_gen_input_file">
						<xsl:with-param name="name">src_filename_path</xsl:with-param>
					</xsl:call-template>
					<table>
                       <tbody>
                           <tr>
                               <td class="wzb-ui-desc-text">
                               		
                                 	<xsl:value-of select="$lab_upload_instruction"/>
									<br/>
									<xsl:value-of select="$lab_upload_instruction2"/>
                               </td>
                           </tr>
                       </tbody>
                   </table>
					<input type="hidden" name="src_filename"/>
	
				
			   </td>
			</tr>
			
			<tr>
				<td class="wzb-form-label" valign="top"></td>
                <td class="wzb-form-control">
					<lable>
						<input name="usr_pwd_need_change_ind" type="checkbox" value="true" />
							<xsl:value-of select="$lab_usr_pwd_need_change_ind"></xsl:value-of>
					</lable>
					<table>
                       <tbody>
                           <tr>
                               <td class="wzb-ui-desc-text">
                               		<xsl:value-of select="$lab_upload_instruction3"/>
									
                               </td>
                           </tr>
                       </tbody>
                   </table>
			   </td>
			</tr>
			
			
			
			<tr>
               <td class="wzb-form-label" valign="top"></td>
               <td class="wzb-form-control">
                   <table>
                       <tbody>
                           <tr>
                               <td><input type="checkbox" name="identical_usr_no_import" value="true" /><xsl:value-of select="$label_core_user_management_50"/></td>
                           </tr>
                           <tr>
                               <td class="wzb-ui-desc-text"><xsl:value-of select="$label_core_user_management_51"/></td>
                           </tr>
                       </tbody>
                   </table>
               </td>
           </tr>
		   <tr>
               <td class="wzb-form-label" valign="top"></td>
               <td class="wzb-form-control">
                   <table>
                       <tbody>
                           <tr>
                               <td><input type="checkbox" name="oldusr_pwd_need_update_ind" value="true" /><xsl:value-of select="$label_core_user_management_52"/></td>
                           </tr>
                           <tr>
                               <td class="wzb-ui-desc-text"><xsl:value-of disable-output-escaping="yes" select="$label_core_user_management_53"/></td>
                           </tr>
                       </tbody>
                   </table>
               </td>
           </tr>
			
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_description"/>：
				</td>
				<td class="wzb-form-control" >
					<textarea name="upload_desc" style="width:300px;" rows="4"/>
					<br/>
					<span class="wzb-ui-desc-text"><xsl:value-of select="$lab_desc_requirement"/></span>
				</td>
			</tr>
			<!--  
			<tr>
				<td class="wzb-form-label">
				</td>
				<td class="wzb-form-control">
					<input type="radio" name="allow_update" id="opt_1" value="true" checked="checked"/>
					<label for="opt_1">
						<xsl:value-of select="$lab_option_1"/>
					</label><br/>
					<input type="radio" name="allow_update" id="opt_2" value="false"/>
					<label for="opt_2">
						<xsl:value-of select="$lab_option_2"/>
					</label>
				</td>
			</tr>
			-->
			<tr>
				<td class="wzb-form-label">
				</td>
				<td class="wzb-ui-module-text">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_next"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:Batch.User.Import.exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:wb_utils_nav_go('FTN_AMD_USR_INFO','<xsl:value-of select="$ent_id"/>', '<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
