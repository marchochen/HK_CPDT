<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="act_type" select="/applyeasy/act_type"/>
	<xsl:variable name="maxday" select="/applyeasy/act_type/@maxday"/>
	<xsl:variable name="upd_timestamp" select="/applyeasy/lesson/last_updated/@timestamp"/>
		
	<xsl:variable name="ils_date" select="/applyeasy/lesson/@ils_date"/>
	<xsl:variable name="ils_qiandao" select="/applyeasy/lesson/@ils_qiandao"/>
	<xsl:variable name="ils_qiandao_chidao" select="/applyeasy/lesson/@ils_qiandao_chidao"/>
	<xsl:variable name="ils_qiandao_queqin" select="/applyeasy/lesson/@ils_qiandao_queqin"/>
	<xsl:variable name="ils_qiandao_youxiaoqi" select="/applyeasy/lesson/@ils_qiandao_youxiaoqi"/>
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
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_{$wb_cur_lang}.js"></script>
			<script language="JavaScript"><![CDATA[
			itm_lst = new wbItem
			//cata_lst = new wbCataLst
			var frmXml;
						
			gen_set_cookie('url_success',self.location.href)
			
			function chkDate(frm) {
				if (!((ConvertToInt(frm.ipt_start_h.value)!= -1 && !(ConvertToInt(frm.ipt_start_h.value) < 0 || ConvertToInt(frm.ipt_start_h.value) > 23 )))) {
					alert(wb_msg_pls_enter_valid_hour_1 + frm.lab_start_date.value + wb_msg_pls_enter_valid_hour_2);
					frm.ipt_start_h.focus();
					return false;
				}
				if (!((ConvertToInt(frm.ipt_start_m.value) != -1 && !(ConvertToInt(frm.ipt_start_m.value) < 0 || ConvertToInt(frm.ipt_start_m.value) > 60 )))) {
					alert(wb_msg_pls_enter_valid_minute_1 + frm.lab_start_date.value + wb_msg_pls_enter_valid_minute_2);
					frm.ipt_start_m.focus();
					return false;
				}
				if (!((ConvertToInt(frm.ipt_end_h.value) != -1 && !(ConvertToInt(frm.ipt_end_h.value) < 0 || ConvertToInt(frm.ipt_end_h.value) > 23 )))) {
					alert(wb_msg_pls_enter_valid_hour_1 + frm.lab_end_date.value + wb_msg_pls_enter_valid_hour_2);
					frm.ipt_end_h.focus();
					return false;
				}
				if (!((ConvertToInt(frm.ipt_end_m.value) != -1 && !(ConvertToInt(frm.ipt_end_m.value) < 0 || ConvertToInt(frm.ipt_end_m.value) > 60 )))) {
					alert(wb_msg_pls_enter_valid_minute_1 + frm.lab_end_date.value + wb_msg_pls_enter_valid_minute_2);
					frm.ipt_end_m.focus();
					return false;
				}
				return true;
			}
			
			function isDate(dt){
				var res = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-9]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/;
				var re = new RegExp(res);
				return re.test(dt)
			}

			function submit_check(ils_id, level, lang){
				//设定日期
				if(frmXml.ipt_day_time_yy.value !='' && frmXml.ipt_day_time_mm.value !='' && frmXml.ipt_day_time_dd.value !='' 
					&& frmXml.ipt_day_time_yy !=null && frmXml.ipt_day_time_mm !=null && frmXml.ipt_day_time_dd !=null){
					var ipt_day  = frmXml.ipt_day_time_yy.value+"-"+frmXml.ipt_day_time_mm.value+"-"+frmXml.ipt_day_time_dd.value
					if(!isDate(ipt_day)) {
						alert(wb_msg_usr_enter_valid+wb_msg_date);
						return false;
					}else{
						frmXml.ils_date.value = ipt_day + " 00:00:00.000";
					}
				}else{
					alert(wb_msg_usr_please_enter_the+wb_msg_date);
					return false;
				}
				//签到设置
				var ils_qiandao = 0;
				for(i=0;i<frmXml.ils_qiandao_radio.length;i++){ 
			    	if(frmXml.ils_qiandao_radio[i].checked){
			        	ils_qiandao = frmXml.ils_qiandao_radio[i].value;
			        }
			    }
			    frmXml.ils_qiandao.value = ils_qiandao;
			    if(ils_qiandao == '0'){
			    	frmXml.ils_qiandao_chidao.value = '';
			    	frmXml.ils_qiandao_queqin.value = '';
			    	frmXml.ils_qiandao_youxiaoqi.value = '';
			    }else{
			    	if(!wbUtilsValidateInteger(frmXml.ils_qiandao_chidao, frmXml.lab_late_time.value) 
			    		|| !wbUtilsValidateInteger(frmXml.ils_qiandao_queqin, frmXml.lab_absent_time.value) 
			    		|| !wbUtilsValidateInteger(frmXml.ils_qiandao_youxiaoqi, frmXml.lab_youxiaoqi.value)){
			    		return;
			    	}
			    }
				
				//var ipt_day, _start_h, _end_h, _start_m, _end_m;
				//if(!wbUtilsValidateInteger(frmXml.ipt_day, frmXml.lab_day.value)){
				//	return;
				//}
				_start_h = ConvertToInt(frmXml.ipt_start_h.value);
				_end_h = ConvertToInt(frmXml.ipt_end_h.value);
				_start_m = ConvertToInt(frmXml.ipt_start_m.value);
				_end_m = ConvertToInt(frmXml.ipt_end_m.value);

				if (!chkDate(frmXml)) {
					return false;
				}

				if(_end_h < _start_h || (_start_h == _end_h && _end_m < _start_m )){
					alert(eval("wb_msg_" + lang + "_start_end_time"));
					frmXml.ipt_end_h.focus();
					return;
				}
				if(_start_h == _end_h && _end_m == _start_m ){
					alert(eval("wb_msg_"+ lang +"_fm_time_intervals_same"));
					frmXml.ipt_start_h.focus();
					return;
				}
				if(frmXml.ipt_lesson_name.value == ""){
					alert(wb_msg_ils_name_empty);
					frmXml.ipt_lesson_name.focus();
					return;
				}
				
				if(frmXml.ipt_lesson_place.value != "" && getChars(frmXml.ipt_lesson_place.value) > 80){
					Dialog.alert(fetchLabel("lesson_address")+fetchLabel("label_title_length_warn_80"),function(){
						frmXml.ipt_lesson_place.focus();
					});
					return;
				}
				
				if(getChars(frmXml.ipt_lesson_name.value) > 80){
					Dialog.alert(fetchLabel("global_title")+fetchLabel("label_title_length_warn_80"),function(){
						frmXml.ipt_lesson_name.focus();
					});
					return;
				}
				
				if(level == "course"){
					itm_lst.ae_upd_course_lesson(frmXml, ils_id, "save");
				}else if(level == "run"){
					itm_lst.ae_upd_run_lesson(frmXml, ils_id, "save");
				}
			}
			
			function ConvertToInt(str) {
				var tmpint;
				try {
					if(str.indexOf(".") > 0) {
						tmpint = -1;
					} else {
						tmpint = parseInt(str, 10);
					}
				} catch (e) {
					tmpint = -1;
				}
				if (isNaN(tmpint)) {
					tmpint = -1;
					}
				return tmpint;
			}
			
			function init(){
				frmXml = document.frmXml;
				if(']]><xsl:value-of select="$ils_qiandao"/><![CDATA[' =='1'){
					chk(frmXml,'1')
				}else{
					chk(frmXml,'0')
					frmXml.ils_qiandao_chidao.value="5"
					frmXml.ils_qiandao_queqin.value="30"
					frmXml.ils_qiandao_youxiaoqi.value="15"
				}
				
			}
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="init()">
			<form name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">
				<xsl:choose>
					<xsl:when test="$act_type='edit'">修改培訓單元</xsl:when>
					<xsl:otherwise>新增培訓單元</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="lab_pre">日程表</xsl:with-param>
			<xsl:with-param name="lab_class_name">課程名稱</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_day">日程</xsl:with-param>
			<xsl:with-param name="lab_day_l">第</xsl:with-param>
			<xsl:with-param name="lab_day_r">天</xsl:with-param>
			<xsl:with-param name="lab_start_time">開始時間</xsl:with-param>
			<xsl:with-param name="lab_end_time">結束時間</xsl:with-param>
			<xsl:with-param name="lab_place">地點</xsl:with-param>
			<xsl:with-param name="lab_time_min_sec"> 時：分</xsl:with-param>
			<xsl:with-param name="lab_unit_title">標題：</xsl:with-param>
			<xsl:with-param name="lab_txt_btn_save">確定</xsl:with-param>
			<xsl:with-param name="lab_txt_btn_cancel">取消</xsl:with-param>
			
			<xsl:with-param name="lab_ils_date">日期</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao">簽到設置</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_yes">需要簽到</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_no">不需要簽到</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_chidao">，遲到</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_chidao_desc">遲到：開始時間加上設定的分鐘值，在此時間後簽到為遲到</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_queqin">，缺勤</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_queqin_desc">缺勤：開始時間加上設定的分鐘值，在此時間後簽到為缺勤</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_youxiaoqi">有效簽到時間</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_youxiaoqi_desc">默認為開始時間前15分鐘至結束時間為有效簽到時間</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_mm">分鐘</xsl:with-param>
			<xsl:with-param name="lab_ils_sel_Instructor">指定講師</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_late_time">遲到時間</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_absent_time">缺勤時間</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">
				<xsl:choose>
					<xsl:when test="$act_type='edit'">修改培训单元</xsl:when>
					<xsl:otherwise>添加培训单元</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="lab_pre">日程表</xsl:with-param>
			<xsl:with-param name="lab_class_name">课程名称:</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_day">日程</xsl:with-param>
			<xsl:with-param name="lab_day_l">第</xsl:with-param>
			<xsl:with-param name="lab_day_r">天</xsl:with-param>
			<xsl:with-param name="lab_start_time">开始时间</xsl:with-param>
			<xsl:with-param name="lab_end_time">结束时间</xsl:with-param>
			<xsl:with-param name="lab_place">地点</xsl:with-param>
			<xsl:with-param name="lab_time_min_sec"> 时：分</xsl:with-param>
			<xsl:with-param name="lab_unit_title">标题：</xsl:with-param>
			<xsl:with-param name="lab_txt_btn_save">确定</xsl:with-param>
			<xsl:with-param name="lab_txt_btn_cancel">取消</xsl:with-param>
			
			<xsl:with-param name="lab_ils_date">日期</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao">签到设置</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_yes">需要签到</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_no">不需要签到</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_chidao">，迟到</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_chidao_desc">迟到：开始时间加上设定的分钟值，在此时间后签到为迟到</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_queqin">，缺勤</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_queqin_desc">缺勤：开始时间加上设定的分钟值，在此时间后签到为缺勤</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_youxiaoqi">有效签到时间</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_youxiaoqi_desc">默认为开始时间前15分钟至结束时间为有效签到时间</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_mm">分钟</xsl:with-param>
			<xsl:with-param name="lab_ils_sel_Instructor">指定讲师</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_late_time">迟到时间</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_absent_time">缺勤时间</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">
				<xsl:choose>
					<xsl:when test="$act_type='edit'">Edit Session</xsl:when>
					<xsl:otherwise>Add Session</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="lab_pre">Timetable</xsl:with-param>
			<xsl:with-param name="lab_class_name">Title:</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>information</xsl:with-param>
			<xsl:with-param name="lab_day">Day</xsl:with-param>
			<xsl:with-param name="lab_day_l"/>
			<xsl:with-param name="lab_day_r"/>
			<xsl:with-param name="lab_start_time">Start time</xsl:with-param>
			<xsl:with-param name="lab_end_time">End time</xsl:with-param>
			<xsl:with-param name="lab_place">Venue</xsl:with-param>
			<xsl:with-param name="lab_time_min_sec"> HH:MM</xsl:with-param>
			<xsl:with-param name="lab_unit_title">Title:</xsl:with-param>
			<xsl:with-param name="lab_txt_btn_save">OK</xsl:with-param>
			<xsl:with-param name="lab_txt_btn_cancel">Cancel</xsl:with-param>
			
			<xsl:with-param name="lab_ils_date">Date</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao">Attendance settings</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_yes">Attendance required</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_no">Attendance not required</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_chidao">. Late attend after</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_chidao_desc">Late：Treat as late when the preset duration is gone since the class begin</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_queqin">, absent after</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_queqin_desc">Absent：Treat as absent when the preset duration is gone since the class begin</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_youxiaoqi">Valid attendance period</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_start_time_before">From</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_youxiaoqi_desc">minute before the class begin to the end of the class</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_mm">minutes</xsl:with-param>
			<xsl:with-param name="lab_ils_sel_Instructor">Specifies lecturer</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_late_time">Late time</xsl:with-param>
			<xsl:with-param name="lab_ils_qiandao_absent_time">Absent time</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_pre"/>
		<xsl:param name="lab_class_name"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_day"/>
		<xsl:param name="lab_day_l"/>
		<xsl:param name="lab_day_r"/>
		<xsl:param name="lab_start_time"/>
		<xsl:param name="lab_end_time"/>
		<xsl:param name="lab_place"/>
		<xsl:param name="lab_time_min_sec"/>
		<xsl:param name="lab_unit_title"/>
		<xsl:param name="lab_txt_btn_save"/>
		<xsl:param name="lab_txt_btn_cancel"/>
		
		
			
		<xsl:param name="lab_ils_date"/>
		<xsl:param name="lab_ils_qiandao"/>
		<xsl:param name="lab_ils_qiandao_yes"/>
		<xsl:param name="lab_ils_qiandao_no"/>
		<xsl:param name="lab_ils_qiandao_chidao"/>
		<xsl:param name="lab_ils_qiandao_queqin"/>
		<xsl:param name="lab_ils_qiandao_chidao_desc"/>
		<xsl:param name="lab_ils_qiandao_queqin_desc"/>
		<xsl:param name="lab_ils_qiandao_youxiaoqi"/>
		<xsl:param name="lab_ils_qiandao_youxiaoqi_desc"/>
		<xsl:param name="lab_ils_qiandao_mm"/>
		<xsl:param name="lab_ils_sel_Instructor"/>
		<xsl:param name="lab_ils_qiandao_late_time"/>
		<xsl:param name="lab_ils_qiandao_absent_time"/>
		
		<xsl:variable name="ipt_v_day" select="/applyeasy/lesson/@day"/>
		<xsl:variable name="ipt_v_start_h" select="substring(/applyeasy/lesson/@start_time,12,2)"/>
		<xsl:variable name="ipt_v_start_m" select="substring(/applyeasy/lesson/@start_time,15,2)"/>
		<xsl:variable name="ipt_v_end_h" select="substring(/applyeasy/lesson/@end_time,12,2)"/>
		<xsl:variable name="ipt_v_end_m" select="substring(/applyeasy/lesson/@end_time,15,2)"/>
		<xsl:variable name="ipt_v_name" select="/applyeasy/lesson/title"/>
		<xsl:variable name="ipt_v_place" select="/applyeasy/lesson/place"/>
		
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
		</xsl:call-template>
		
		 <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">108</xsl:with-param>
		</xsl:call-template>

		<input type="hidden" name="lab_late_time" value="{$lab_ils_qiandao_late_time}"></input>
		<input type="hidden" name="lab_absent_time" value="{$lab_ils_qiandao_absent_time}"></input>
		<input type="hidden" name="lab_youxiaoqi" value="{$lab_ils_qiandao_youxiaoqi}"></input>

    <div class="wzb-item-main">
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/>
			</xsl:with-param>
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
		<table>
			<tr>
				<!--  
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_day"/>:
				</td>
				<input type="hidden" name="lab_day" value="{$lab_day}"/>
				<td class="wzb-form-control">
					<xsl:value-of select="$lab_day_l"/>
					<input type="text" name="ipt_day" class="wzb-inputText" size="1" maxlength="2" value="{$ipt_v_day}"/>
					<xsl:value-of select="$lab_day_r"/>
				</td>-->
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_ils_date"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">ipt_day_time</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">ipt_day_time</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
						<xsl:with-param name="timestamp">
						 	<xsl:value-of select="$ils_date"></xsl:value-of>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_start_time"/>：
				</td>
				<td class="wzb-form-control">
					<input type="text" name="ipt_start_h" class="wzb-inputText" size="2" maxlength="2" value="{$ipt_v_start_h}">
						<xsl:attribute name="onkeyup">javascript:auto_focus_field(frmXml.ipt_start_h,2,frmXml.ipt_start_m)</xsl:attribute>
					</input>
					<xsl:text>：</xsl:text>
					<input type="text" name="ipt_start_m" class="wzb-inputText" size="2" maxlength="2" value="{$ipt_v_start_m}">
						<xsl:attribute name="onkeyup">javascript:auto_focus_field(frmXml.ipt_start_m,2,frmXml.ipt_end_h)</xsl:attribute>
					</input>
					<xsl:value-of select="$lab_time_min_sec"/>
				</td>
				<input type="hidden" name="lab_start_date" value="{$lab_start_time}"/>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_end_time"/>：
				</td>
				<td class="wzb-form-control">
					<input type="text" name="ipt_end_h" class="wzb-inputText" size="2" maxlength="2" value="{$ipt_v_end_h}">
						<xsl:attribute name="onkeyup">javascript:auto_focus_field(frmXml.ipt_end_h,2,frmXml.ipt_end_m)</xsl:attribute>
					</input>
					<xsl:text>：</xsl:text>
					<input type="text" name="ipt_end_m" class="wzb-inputText" size="2" maxlength="2" value="{$ipt_v_end_m}"/>
					<xsl:value-of select="$lab_time_min_sec"/>
				</td>
				<input type="hidden" name="lab_end_date" value="{$lab_end_time}"/>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_place"/>：
				</td>
				<td class="wzb-form-control">
					<input type="text" name="ipt_lesson_place" class="wzb-inputText" style="width:320px" value="{$ipt_v_place}"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_unit_title"/>
				</td>
				<td class="wzb-form-control">
					<input type="text" name="ipt_lesson_name" class="wzb-inputText" style="width:320px" value="{$ipt_v_name}"/>
				</td>
			</tr>
			
			
			<script language="JavaScript"><![CDATA[
			
			function chk(frmXml,value){
				if(value =='0'){
					frmXml.ils_qiandao_chidao.disabled="disabled"
					frmXml.ils_qiandao_queqin.disabled="disabled"
					frmXml.ils_qiandao_youxiaoqi.disabled="disabled"
				}else if(value == '1'){
					frmXml.ils_qiandao_chidao.disabled=""
					frmXml.ils_qiandao_queqin.disabled=""
					frmXml.ils_qiandao_youxiaoqi.disabled=""
				}
			}
			 
			]]></script>
			
			<!-- 签到设置 -->
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_ils_qiandao"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="$ils_qiandao = '1'">
							<input type="radio" name="ils_qiandao_radio" value="0" onclick="chk(frmXml,'0')"/><xsl:value-of select="$lab_ils_qiandao_no"/>
							<br/>
							<input type="radio" name="ils_qiandao_radio" value="1" checked="checked" onclick="chk(frmXml,'1')"/><xsl:value-of select="$lab_ils_qiandao_yes"/>
						</xsl:when>
						<xsl:otherwise>
							<input type="radio" name="ils_qiandao_radio" value="0" checked="checked"  onclick="chk(frmXml,'0')"/><xsl:value-of select="$lab_ils_qiandao_no"/>
							<br/>
							<input type="radio" name="ils_qiandao_radio" value="1" onclick="chk(frmXml,'1')"/><xsl:value-of select="$lab_ils_qiandao_yes"/>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:value-of select="$lab_ils_qiandao_chidao"/>
						<input type="text" style="margin-left: 4px; margin-right: 4px;" name="ils_qiandao_chidao" class="wzb-inputText" size="2" maxlength="2" value="{$ils_qiandao_chidao}"/>
							<xsl:value-of select="$lab_ils_qiandao_mm"/>
					<xsl:value-of select="$lab_ils_qiandao_queqin"/>
						<input type="text" style="margin-left: 4px; margin-right: 4px;" name="ils_qiandao_queqin" class="wzb-inputText" size="2" maxlength="2" value="{$ils_qiandao_queqin}"/>
							<xsl:value-of select="$lab_ils_qiandao_mm"/><br/>
					<xsl:value-of select="$lab_ils_qiandao_chidao_desc"/><br/>
					<xsl:value-of select="$lab_ils_qiandao_queqin_desc"/>	
				</td>
			</tr>
			<!-- 签到设置 -->
			<!-- 签到有效期 -->
			<tr>
				<td class="wzb-form-label"  valign="top">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_ils_qiandao_youxiaoqi"/>：
				</td>
				<td class="wzb-form-control">
					<input style="margin-left: 4px; margin-right: 4px;"  type="text" name="ils_qiandao_youxiaoqi" class="wzb-inputText" size="2" maxlength="2" value="{$ils_qiandao_youxiaoqi}"/><xsl:value-of select="$lab_ils_qiandao_mm"/><br/>
					<xsl:value-of select="$lab_ils_qiandao_youxiaoqi_desc"/>	
				</td>
			</tr>
			<!-- 签到有效期 -->
			
			<tr>
				<td width="35%" align="right">
				</td>
				<td width="65%" align="left" class="wzb-ui-module-text">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		</table>
		<!-- =============================================================== -->
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
					<xsl:value-of select="$lab_txt_btn_save"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">
					<xsl:choose>
						<xsl:when test="/applyeasy/item/@apply_ind = 'true'">javascript:submit_check('<xsl:value-of select="/applyeasy/lesson/@ils_id"/>','run', '<xsl:value-of select="$wb_lang"/>')</xsl:when>
						<xsl:otherwise>javascript:submit_check('<xsl:value-of select="/applyeasy/lesson/@ils_id"/>','course', '<xsl:value-of select="$wb_lang"/>')</xsl:otherwise>
					</xsl:choose>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
					<xsl:value-of select="$lab_txt_btn_cancel"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:history.back();</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
			</xsl:call-template>
		</div>
		<input type="hidden" name="cmd"/>
		<input type="hidden" name="url_success"/>
		<input type="hidden" name="url_failure"/>
		<input type="hidden" name="itm_id" value="{$itm_id}"/>
		<input type="hidden" name="res_id_lst"/>
		<input type="hidden" name="upd_timestamp" value="{$upd_timestamp}"/>
		<input type="hidden" name="maxday" value="{$maxday}"/>
		<input type="hidden" name="ils_date" value=""/>
		<input type="hidden" name="ils_qiandao" value=""/>
		<xsl:call-template name="wb_ui_footer"/>
	</div>
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
