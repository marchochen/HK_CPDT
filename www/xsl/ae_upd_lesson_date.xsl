<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="itm_apply_ind" select="/applyeasy/item/@apply_ind"/>
	<xsl:variable name="run_ind" select="/applyeasy/item/@run_ind"/>
	<xsl:variable name="session_ind" select="/applyeasy/item/@session_ind"/>
	<!-- =============================================================== -->
	<xsl:template match="/applyeasy">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}ils_lesson.js"/>
			<script language="JavaScript"><![CDATA[
			itm_lst = new wbItem
			//cata_lst = new wbCataLst
			var ipt_name_content = "eff_date";			
			//gen_set_cookie('url_success',self.location.href)
			
			function form_submit(){
				try{
					var day_seq = (frmXml.day_seq.value).split(",");
					frmXml.ipt_name_content.value = ipt_name_content;
					for(var i = 0 ; i  < day_seq.length; i ++){
						var ipt_name = ipt_name_content+day_seq[i];
						clear_date_value(ipt_name_content+day_seq[i]);
						if(eval("frmXml." + ipt_name + "_required.value == 'true'")){
							if(isEmptyDate(ipt_name)){
								alert(wb_msg_ils_day_required.replace("[day]", day_seq[i]));
								return;
							}								
							if(ValidateDate("frmXml." + ipt_name_content+day_seq[i],wb_msg_ils_orderdate_required)){
								set_date_value(ipt_name_content+day_seq[i]);
							} else {
								return;
							}	
						} else {
							if(!isEmptyDate(ipt_name)){
								if(ValidateDate("frmXml." + ipt_name_content+day_seq[i],wb_msg_ils_orderdate_required)){
									set_date_value(ipt_name_content+day_seq[i]);
								} else {
									return;
								}		
							}
						}
						//}
					}
					itm_lst.ae_upd_run_lesson(frmXml, '', "set_date_save");
				}catch(e){
					return;
				}
			}
			function set_order_date(day_id){
				if(confirm(wb_msg_ils_orderdate_confirm)){
					try{
						var day_seq = (frmXml.day_seq.value).split(",");
						if (isEmptyDate(ipt_name_content+day_seq[day_id - 2])){
							alert(wb_msg_ils_day_pre_needed);
							return;
						}
						if(!ValidateDate("frmXml." + ipt_name_content+day_seq[day_id - 2],wb_msg_ils_orderdate_required)){
							return;
						}
						var day;
						var date_year,date_month,date_day;
						var start_date;
						start_date = get_date_byday(day_seq[day_id - 2], ipt_name_content);
						for(var i=day_id-1; i < day_seq.length; i++){
							day = day_seq[i];
							var tmpDate = new Date(Date.parse(start_date) + (day - day_seq[day_id - 2]) * 24 * 60 * 60 * 1000);
							eval("frmXml." + ipt_name_content + day + "_yy.value='" + tmpDate.getFullYear() + "'");
							eval("frmXml." + ipt_name_content + day + "_mm.value='" + fill_str(tmpDate.getMonth() + 1) + "'");
							eval("frmXml." + ipt_name_content + day + "_dd.value='" + fill_str(tmpDate.getDate()) + "'");
						}
					}catch(e){
						return;
					}
				}
			}
			
			function get_date_byday(day, ipt_name_content){
				try{
					var yy,mm,dd;
					yy = eval("frmXml." + ipt_name_content + day + "_yy.value");
					mm = eval("frmXml." + ipt_name_content + day + "_mm.value - 1");
					dd = eval("frmXml." + ipt_name_content + day + "_dd.value");
					var tmpDate = new Date(yy, mm , dd, 0, 0, 0);
					return tmpDate;
				}catch(e){
					return null;
				}
			}
			
			function set_date_value(ipt_name){
				try{
				var el = eval("frmXml."+ipt_name);
				el.value = eval("frmXml." + ipt_name + "_yy.value") + "-";
				el.value += eval("frmXml." + ipt_name + "_mm.value") + "-";
				el.value += eval("frmXml." + ipt_name + "_dd.value");
				}catch(e){
					//alert(e);
				}
			}
			
			function clear_date_value(ipt_name){
				eval("frmXml." + ipt_name + ".value=''");
			}
			
			function fill_str(tmpstr){
				if(0 < tmpstr && tmpstr < 10){
					return "0" + tmpstr;
				}else{
					return tmpstr;
				}	
			}
			
			function ValidateDate(fldName, txtFldName){
				// validate year
				var fld = eval(fldName + '_yy')
			
				fld.value = wbUtilsTrimString(fld.value)
			
				if(fld.value.length != 4 || Number(fld.value) < 1800){
					alert(wb_msg_usr_enter_valid_year_1 + txtFldName + wb_msg_usr_enter_valid_year_2)
					fld.focus();
					fld.select();
					return false;
				}
			
				if(!wbUtilsValidateInteger(fld, txtFldName)) return false;
			
				// validate month
				var fld = eval(fldName + '_mm')
				fld.value = wbUtilsTrimString(fld.value)
			
				if(Number(fld.value) < 10 && fld.value.length == 1){
					fld.value = '0' + fld.value
				}
			
				if(fld.value.length != 2 || fld.value > 12 || fld.value < 1){
					alert(wb_msg_usr_enter_valid_month_1 + txtFldName + wb_msg_usr_enter_valid_month_2)
					fld.focus();
					fld.select();
					return false;
				}
			
				if(!wbUtilsValidateInteger(fld, txtFldName)) return false;
			
				// validate day
				var fld = eval(fldName + '_dd')
			
				fld.value = wbUtilsTrimString(fld.value)
			
				if(Number(fld.value) < 10 && fld.value.length == 1){
					fld.value = '0' + fld.value
				}
			
				if(fld.value.length != 2 || fld.value > gen_Month_Length(Number(eval(fldName + '_mm.value')), eval(fldName + '_yy.value')) || fld.value < 1){
					alert(wb_msg_usr_enter_valid_day_1 + txtFldName + wb_msg_usr_enter_valid_day_2);
					fld.focus();
					fld.select();
					return false;
				}
			
				if(!wbUtilsValidateInteger(fld, txtFldName)) return false;
				return true;
			}
			
			function isEmptyDate(ipt_name){
				if(eval("frmXml." + ipt_name + "_yy.value==''") && eval("frmXml." + ipt_name + "_mm.value==''") && eval("frmXml." + ipt_name + "_dd.value==''")){
					return true;
				} else {
					return false;
				}		
			}
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">設定班別日期</xsl:with-param>
			<xsl:with-param name="lab_desc">設定以下依課程表産岀的班別日期，以下設定的第一天以及最后一天的日期，會自動更新實際班別時段。另外，要簡單設定帶有順序日期的課程表，在需要開始順序日期的欄位徬邊，按帶岀順序日期，繫統會自動帶岀順序日期，並設定于欄位及以下的所有日期欄位中。</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/></xsl:with-param>
			<xsl:with-param name="lab_pre">日程表</xsl:with-param>
			<xsl:with-param name="lab_day_l">第</xsl:with-param>
			<xsl:with-param name="lab_day_r">天</xsl:with-param>
			<xsl:with-param name="lab_save">確定</xsl:with-param>
			<xsl:with-param name="lab_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_order_date">帶岀順序日期</xsl:with-param>
			<xsl:with-param name="lab_no_list">請先增加課程單元!</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">设定班级日期</xsl:with-param>
			<xsl:with-param name="lab_desc">设定以下课程表产出的班级日期，以下设定的第一天以及最后一天的日期，会自动更新实际班级时段。另外，要简单设定带有顺序日期的课程表，在需要开始顺序日期的栏位旁边，按带岀顺序日期，系统会自动带岀顺序日期，并设定于该栏位及以下的所有日期栏位中。</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/></xsl:with-param>
			<xsl:with-param name="lab_pre">日程表</xsl:with-param>
			<xsl:with-param name="lab_day_l">第</xsl:with-param>
			<xsl:with-param name="lab_day_r">天</xsl:with-param>
			<xsl:with-param name="lab_save">确定</xsl:with-param>
			<xsl:with-param name="lab_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_order_date">带岀顺序日期</xsl:with-param>
			<xsl:with-param name="lab_no_list">请先增加课程单元!</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">Set date</xsl:with-param>
			<xsl:with-param name="lab_desc">Set the class dates according to the course timetable. Changing the dates of the first day and last day will change the class period automatically. Moreover, to set the dates in sequence, click  Set Consecutive Dates next to the date you want to start the sequence, and the system will automatically bring up the consecutive dates in the date input boxes that follow.</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/></xsl:with-param>
			<xsl:with-param name="lab_pre">Timetable</xsl:with-param>
			<xsl:with-param name="lab_day_l">Day</xsl:with-param>
			<xsl:with-param name="lab_day_r"/>
			<xsl:with-param name="lab_save">OK</xsl:with-param>
			<xsl:with-param name="lab_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_order_date">Set consecutive dates</xsl:with-param>
			<xsl:with-param name="lab_no_list">Please add training unit first!</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_pre"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_day_l"/>
		<xsl:param name="lab_day_r"/>
		<xsl:param name="lab_save"/>
		<xsl:param name="lab_cancel"/>
		<xsl:param name="lab_order_date"/>
		<xsl:param name="lab_no_list"/>
		
		<xsl:call-template name="itm_action_nav"/>
    <div class="wzb-item-main">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_desc"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="item/@run_ind = 'false'">
						<a href="javascript:itm_lst.get_item_detail('{$itm_id}')" class="NavLink">
							<xsl:value-of select="item/title"/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="javascript:itm_lst.ae_get_course_lesson('{$itm_id}')" class="NavLink">
							<xsl:value-of select="$lab_pre"/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:value-of select="$lab_title"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="item/nav/item" mode="nav">
							<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
							<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
						</xsl:apply-templates>
						<span class="NavLink">
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<a href="javascript:itm_lst.ae_get_run_lesson('{$itm_id}')" class="NavLink">
								<xsl:value-of select="$lab_pre"/>
							</a>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<xsl:value-of select="$lab_title"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="count(lesson_date_list/lesson_date)=0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_list"/>
				</xsl:call-template>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_cancel"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:history.back();</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
					</xsl:call-template>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<table>
					<!--<tr class="SecBg">
						<td colspan="4">
							<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
						</td>
					</tr>
					-->
					<xsl:apply-templates select="lesson_date_list/lesson_date">
						<xsl:with-param name="lab_day_l" select="$lab_day_l"/>
						<xsl:with-param name="lab_day_r" select="$lab_day_r"/>
						<xsl:with-param name="lab_order_date" select="$lab_order_date"/>
					</xsl:apply-templates>
				</table>
						<!-- =============================================================== -->
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_save"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:form_submit();</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_cancel"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:history.back();</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
					</xsl:call-template>
					<input type="hidden" name="day_seq">
						<xsl:attribute name="value"><xsl:apply-templates select="lesson_date_list/lesson_date" mode="day_seq"/></xsl:attribute>
					</input>
				</div>
			</xsl:otherwise>
		</xsl:choose>
		<input type="hidden" name="cmd"/>
		<input type="hidden" name="url_success"/>
		<input type="hidden" name="url_failure"/>
		<input type="hidden" name="forum_timestamp" value="{@timestamp}"/>
		<input type="hidden" name="itm_id" value="{$itm_id}"/>
		<input type="hidden" name="ipt_name_content" value="eff_date"/>
		<xsl:call-template name="wb_ui_footer"/>
	</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="lesson_date_list/lesson_date">
		<xsl:param name="lab_day_l"/>
		<xsl:param name="lab_day_r"/>
		<xsl:param name="lab_order_date"/>
		<tr>
			<td align="right" width="40%" style="padding:10px 0; text-align:right; color:#666;">
				<xsl:value-of select="$lab_day_l"/>
				<xsl:value-of select="@ils_day"/>
				<xsl:value-of select="$lab_day_r"/>:
			</td>
			<td align="left" style="padding:10px 0 10px 10px; color:#333;">
				<xsl:call-template name="display_form_input_time">
					<xsl:with-param name="fld_name">eff_date<xsl:value-of select="@ils_day"/>
					</xsl:with-param>
					<xsl:with-param name="hidden_fld_name">eff_date<xsl:value-of select="@ils_day"/>
					</xsl:with-param>
					<xsl:with-param name="timestamp">
						<xsl:choose>
							<xsl:when test="@org_date = '' and position()=1">
								<xsl:value-of select="/applyeasy/item/@eff_start_datetime"/>
							</xsl:when>
							<xsl:when test="@org_date = '' and position()=last()">
								<xsl:value-of select="/applyeasy/item/@eff_end_datetime"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="@org_date"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
					<xsl:with-param name="show_label">Y</xsl:with-param>
				</xsl:call-template>
				<xsl:if test="position()!=1">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_order_date"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:set_order_date('<xsl:value-of select="position()"/>')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
					</xsl:call-template>
				</xsl:if>
				<input type="hidden" name="eff_date{@ils_day}_required" value="{@required}"/>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="lesson_date_list/lesson_date" mode="day_seq">
		<xsl:value-of select="@ils_day"/>
		<xsl:if test="position() != last()">,</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="item" mode="nav">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_session_info"/>
		<xsl:variable name="_count" select="count(preceding-sibling::item)"/>
		<xsl:choose>
			<xsl:when test="@run_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=$_count">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.get_item_run_list({$value})" class="NavLink">
					<xsl:choose>
						<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
					</xsl:choose>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:when test="@session_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=$_count">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.session.get_session_list({$value})" class="NavLink">
					<xsl:value-of select="$lab_session_info"/>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a href="javascript:itm_lst.get_item_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
