<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="share/sys_tab_share.xsl"/>
	<!-- usr utils -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="lab_sys_setting" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_sys_setting')" />
	<!-- ================================================================ -->
	<xsl:variable name="lab_sys_wechat" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_sys_wechat')" />
	<xsl:variable name="label_core_system_setting_145" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_145')"/>
	<xsl:variable name="label_core_system_setting_149" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_149')"/>
<xsl:template match="/">
	<html><xsl:call-template name="applyeasy"/></html>
</xsl:template>
<!-- ============================================================= -->
<xsl:template name="applyeasy">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		<TITLE><xsl:value-of select="$wb_wizbank"/></TITLE>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_resource.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_threshold.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_system_setting.js" />
			<script>
			<![CDATA[
				systemSetting = new wbSystemSetting();
			
				threshold = new wbThreshold();
				function ins_default_date(){
				var frm = document.frmXml
				//Get the server current time
				str = "]]><xsl:value-of select="/user/cur_time"/><![CDATA["
				
				cur_day = str.substring((str.lastIndexOf('-') + 1), str.indexOf(' '))
				cur_month = str.substring((str.indexOf('-') + 1), str.lastIndexOf('-'))
				cur_year = str.substring(0, str.indexOf('-'))
				
				frm.cur_dt.value = str
				frm.cur_dt_dd.value = cur_day
				frm.cur_dt_mm.value = cur_month
				frm.cur_dt_yy.value = cur_year
				
				if (frm.sys_log_start_date){	
						frm.start_dd.value = cur_day
						frm.start_mm.value = cur_month
						frm.start_yy.value = cur_year
					
				}

				if (frm.sys_log_end_date){							
						frm.end_dd.value = cur_day
						frm.end_mm.value = cur_month
						frm.end_yy.value = cur_year
				}
			}	
			
			function setLogType(frm){
				logType = getUrlParam('logType');
				if(logType == null || logType == ''){
					logType = 'User_Operation_Log';
				}
				for(var i = 0; i < frm.radio_log_type.length; i++){
					if(frm.radio_log_type[i].value == logType){
						frm.radio_log_type[i].checked = true;
						return;
					}
				}
			} 
			
			function export_time_change(obj){
				if(obj.id=='rdo_time_7days' || obj.id=='rdo_time_all'){
				    frm.days.value='';
				    frm.last_days.value='';
					frm.end_yy.value='';
					frm.end_mm.value='';
					frm.end_dd.value='';
					frm.start_yy.value='';
					frm.start_mm.value='';
					frm.start_dd.value='';
					frm.sys_log_start_date.value='';
					frm.sys_log_end_date.value='';
				}
				
				if(obj.id=='rdo_time_last_days'){
					frm.end_yy.value='';
					frm.end_mm.value='';
					frm.end_dd.value='';
					frm.start_yy.value='';
					frm.start_mm.value='';
					frm.start_dd.value='';
					frm.sys_log_start_date.value='';
					frm.sys_log_end_date.value='';
				}
				
				if(obj.id=='rdo_time_from_to'){
					frm.days.value='';
					frm.last_days.value='';
				}
			}
			
		]]></script>
	</head>
	<BODY leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="ins_default_date(); setLogType(document.frmXml)">
		<FORM name="frmXml">	
			<input type="hidden" name="module"/>
			<input type="hidden" name="cmd"/>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<input type="hidden" name="log_type"/>
			<input type="hidden" name="last_days"/>	
			<input type="hidden" name="sys_log_start_date"/>
			<input type="hidden" name="sys_log_end_date"/>
			<input type="hidden" name="cur_dt" value=""/>
			<input type="hidden" name="cur_dt_dd" value=""/>
			<input type="hidden" name="cur_dt_mm" value=""/>
			<input type="hidden" name="cur_dt_yy" value=""/>
			<input type="hidden" name="cur_dt_hour" value=""/>
			<input type="hidden" name="cur_dt_min" value=""/>
			<input type="hidden" name="select_all"/>
			<xsl:call-template name="wb_init_lab"/>
		</FORM>
	</BODY>
</xsl:template>
<xsl:template name="lang_ch">
		<xsl:call-template name="content">
		<xsl:with-param name="lab_threshold_set">設置</xsl:with-param>
		<xsl:with-param name="lab_active_user">當前線上用戶</xsl:with-param>
		<xsl:with-param name="lab_log">系統日誌</xsl:with-param>
		<xsl:with-param name="lab_threshold">用量警告日誌</xsl:with-param>
		<xsl:with-param name="lab_data_integration">數據同步日誌</xsl:with-param>
		<xsl:with-param name="lab_last_7days">過去7天</xsl:with-param>
		<xsl:with-param name="lab_last">過去</xsl:with-param>
		<xsl:with-param name="lab_days">天 </xsl:with-param>
		<xsl:with-param name="lab_all">所有</xsl:with-param>
		<xsl:with-param name="lab_export">導出</xsl:with-param>
		<xsl:with-param name="lab_log_type_title">導岀類型</xsl:with-param>
		<xsl:with-param name="lab_time_title">導岀範圍</xsl:with-param>
		<xsl:with-param name="lab_user_operation_log">重要功能操作日誌</xsl:with-param>
		<xsl:with-param name="lab_user_login_log">用戶登錄日誌</xsl:with-param>
		<xsl:with-param name="lab_prompt">平臺只會保存一年內的操作記錄，已經超過一年的記錄，系統會自動删除。所以在這裡只能匯出一年內的記錄。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
<xsl:template name="lang_gb">
	<xsl:call-template name="content">
		<xsl:with-param name="lab_threshold_set">设置</xsl:with-param>
		<xsl:with-param name="lab_active_user">当前在线用户</xsl:with-param>
		<xsl:with-param name="lab_log">系统日志</xsl:with-param>
		<xsl:with-param name="lab_threshold">用量警告日志</xsl:with-param>
		<xsl:with-param name="lab_data_integration">数据同步日志</xsl:with-param>
		<xsl:with-param name="lab_last_7days">过去7天</xsl:with-param>
		<xsl:with-param name="lab_last">过去</xsl:with-param>
		<xsl:with-param name="lab_days">天 </xsl:with-param>
		<xsl:with-param name="lab_all">所有</xsl:with-param>
		<xsl:with-param name="lab_export">导出</xsl:with-param>
		<xsl:with-param name="lab_log_type_title">导出类型</xsl:with-param>
		<xsl:with-param name="lab_time_title">导出范围</xsl:with-param>
		<xsl:with-param name="lab_user_operation_log">重要功能操作日志</xsl:with-param>
		<xsl:with-param name="lab_user_login_log">用户登录日志</xsl:with-param>
		<xsl:with-param name="lab_prompt">平台只会保存一年内的操作记录，已经超过一年的记录，系统会自动删除。所以在这里只能导出一年内的记录。</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<xsl:template name="lang_en">
	<xsl:call-template name="content">
		<xsl:with-param name="lab_threshold_set">Configuration</xsl:with-param>
		<xsl:with-param name="lab_active_user">Active user</xsl:with-param>
		<xsl:with-param name="lab_log">Logs</xsl:with-param>
		<xsl:with-param name="lab_threshold">Performance warning log</xsl:with-param>
		<xsl:with-param name="lab_data_integration">Data integration log</xsl:with-param>
		<xsl:with-param name="lab_last_7days">Last 7 days </xsl:with-param>
		<xsl:with-param name="lab_last">Last</xsl:with-param>
		<xsl:with-param name="lab_days">Days </xsl:with-param>
		<xsl:with-param name="lab_all">All</xsl:with-param>
		<xsl:with-param name="lab_export">Export</xsl:with-param>
		<xsl:with-param name="lab_log_type_title">Export type</xsl:with-param>
		<xsl:with-param name="lab_time_title">Export scope</xsl:with-param>
		<xsl:with-param name="lab_user_operation_log">Important function operation log</xsl:with-param>
		<xsl:with-param name="lab_user_login_log">User login log</xsl:with-param>
		<xsl:with-param name="lab_prompt">System will save one year of operation records only, all the records which over one years will be deleted automatically. So you can export the records within a year.</xsl:with-param>
		</xsl:call-template>
</xsl:template>

<xsl:template name="content">
	<xsl:param name="lab_threshold_set"/>
	<xsl:param name="lab_active_user"/>
	<xsl:param name="lab_log"/>
	<xsl:param name="lab_threshold"/>
	<xsl:param name="lab_data_integration"/>
	<xsl:param name="lab_last_7days"/>
	<xsl:param name="lab_last"/>
	<xsl:param name="lab_days"/>
	<xsl:param name="lab_all"/>
	<xsl:param name="lab_export"/>
	<xsl:param name="lab_log_type_title"/>
	<xsl:param name="lab_time_title"/>
	<xsl:param name="lab_user_operation_log"/>
	<xsl:param name="lab_user_login_log"/>
	<xsl:param name="lab_prompt"/>
	<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_SYS_SETTING_LOG</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_SYS_SETTING_LOG</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_log"/>
			</xsl:with-param>
		</xsl:call-template>
	<xsl:call-template name="sys_gen_tab">
	    <xsl:with-param name="tab_1" select="$lab_log"/>
		<xsl:with-param name="tab_1_href">javascript:threshold.get_thd_syn_log_prep()</xsl:with-param>
		<xsl:with-param name="tab_2" select="$lab_active_user"/>
		<xsl:with-param name="tab_2_href">javascript:threshold.get_cur_act_user()</xsl:with-param>
		<xsl:with-param name="current_tab" select="$lab_log"/>
	</xsl:call-template>
	
	 <div class="tab-content">
         <div class="margin-top28 tab-pane active">

	
	<table>
	    <tr>
			<td class="wzb-form-label">
			<xsl:value-of select="$lab_log_type_title"/>：
			</td>
			<td class="wzb-form-control">
				<input id="rdo_operation_log" name="radio_log_type" type="radio" value="User_Operation_Log" checked="checked">	
				</input>
				<label for="rdo_operation_log">
					<xsl:value-of select="$lab_user_operation_log"/>
				</label>
			</td>
		</tr>
		
		<tr>
			<td class="wzb-form-label">
			</td>
			<td class="wzb-form-control">
				<input id="rdo_login_log" name="radio_log_type" type="radio" value="User_Login_Log" ></input>
				<label for="rdo_login_log">
					<xsl:value-of select="$lab_user_login_log"/>
				</label>
				<br></br>
				<div class="wzb-ui-desc-text" style="margin:0 0 0 20px;">
	                <xsl:value-of select="$lab_prompt"/>
	             </div>
			</td>
		</tr>
		
		<tr>
			<td class="wzb-form-label">
			</td>
			<td class="wzb-form-control">
				<input id="rdo_thd_log" name="radio_log_type" type="radio" value="Perf_Warning_Log" >	
				</input>
				<label for="rdo_thd_log">
					<xsl:value-of select="$lab_threshold"/>
				</label>
			</td>
		</tr>
	<!-- 	<tr>
			<td class="wzb-form-label"></td>
			<td class="wzb-form-control">
				<input id="rdo_di_log" name="radio_log_type" type="radio" value="Data_Integration_Log">	</input>	
				<label for="rdo_di_log">
					<xsl:value-of select="$lab_data_integration"/>
				</label>			
			</td>	
		</tr> -->
		
		<tr>
			<td class="wzb-form-label">
				<xsl:value-of select="$lab_time_title"/>：
			</td>
			
			<td class="wzb-form-control">
				<input id="rdo_time_7days" name="radio_time" type="radio" value="" checked="checked" onchange="export_time_change(this);"/>
				<label for="rdo_time_7days">
					<xsl:value-of select="$lab_last_7days"/>
				</label>
			</td>
		</tr>
		<tr>
			<td class="wzb-form-label"></td>
			<td class="wzb-form-control">
				<label for="rdo_time_last_days">
					<input id="rdo_time_last_days" name="radio_time" type="radio" value="" onchange="export_time_change(this);"/><xsl:value-of select="$lab_last"/>&#160;
					<input name="days" type="text" size="5" class="wzb-inputText" onfocus="javascript:frmXml.radio_time[1].checked=true;"/>&#160;<xsl:value-of select="$lab_days"/>
					<input name="lab_days" type="hidden" value="{$lab_days}"/>
				</label>
			</td>
		</tr>
		<tr>
			<td class="wzb-form-label"></td>
			<td class="wzb-form-control">
				<label for="rdo_time_from_to">
					<input id="rdo_time_from_to" name="radio_time" type="radio" value="" onchange="export_time_change(this);"/>
					<xsl:value-of select="$lab_const_from"/>
					<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">start</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">msg_begin_date</xsl:with-param>
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
						<xsl:with-param name="format">2</xsl:with-param>
						<xsl:with-param name="display_form_input_hhmm">N</xsl:with-param>	
						<xsl:with-param name="focus_rad_btn_name">radio_time[2]</xsl:with-param>
					</xsl:call-template>
					<xsl:variable name="sub_begin_date" select="translate(substring-before(@begin_date,'.'),':- ','')"/>
					<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[										
						frm= document.frmXml										
						frm.start_dd.value=]]>'<xsl:value-of select="substring($sub_begin_date,7,2)"/>'<![CDATA[
						frm.start_mm.value=]]>'<xsl:value-of select="substring($sub_begin_date,5,2)"/>'<![CDATA[
						frm.start_yy.value=]]>'<xsl:value-of select="substring($sub_begin_date,1,4)"/>'
					</script>	
					<xsl:value-of select="$lab_const_to"/>
					<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">end</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">msg_end_date</xsl:with-param>
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
						<xsl:with-param name="format">2</xsl:with-param>
						<xsl:with-param name="display_form_input_hhmm">N</xsl:with-param>
						<xsl:with-param name="focus_rad_btn_name">radio_time[2]</xsl:with-param>
					</xsl:call-template>
					<xsl:variable name="sub_end_date" select="translate(substring-before(@end_date,'.'),':- ','')"/>
					<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[										
							frm= document.frmXml										
							frm.end_dd.value=]]>'<xsl:value-of select="substring($sub_end_date,7,2)"/>'<![CDATA[
							frm.end_mm.value=]]>'<xsl:value-of select="substring($sub_end_date,5,2)"/>'<![CDATA[
							frm.end_yy.value=]]>'<xsl:value-of select="substring($sub_end_date,1,4)"/>'
					</script>
				</label>
			</td>
		</tr>
		<tr>
			<td class="wzb-form-label"></td>
			<td class="wzb-form-control">
				<input id="rdo_time_all" type="radio" name="radio_time" value="" onchange="export_time_change(this);"/>
				<label for="rdo_time_all">
					<xsl:value-of select="$lab_all"/>
				</label>
			</td>
		</tr>
	</table>
	  </div>
	</div>
	<div class="wzb-bar">
		<xsl:call-template name="wb_gen_form_button">
			<xsl:with-param name="wb_gen_btn_name" select="$lab_export"/>
			<xsl:with-param name="wb_gen_btn_href">javascript:wbGetThresholdLog(frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
		</xsl:call-template>
	</div>
</xsl:template>

</xsl:stylesheet>
