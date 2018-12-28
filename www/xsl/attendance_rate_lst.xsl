<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_win_hdr.xsl"/>
	<xsl:import href="utils/check_client.xsl"/>
	<xsl:import href="utils/wb_form_select_action.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="utils/display_score.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl "/>
    <xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="page_variant" select="/evalmanagement/meta/page_variant"/>
	<!-- paginatoin variables -->
	<xsl:variable name="page_size" select="/evalmanagement/attendance_maintance/pagination/@page_size"/>
	<xsl:variable name="cur_page" select="/evalmanagement/attendance_maintance/pagination/@cur_page"/>
	<xsl:variable name="total" select="/evalmanagement/attendance_maintance/pagination/@total_rec"/>
	<xsl:variable name="itm_id" select="/evalmanagement/attendance_maintance/item/@id"/>
	<xsl:variable name="create_session_ind" select="/evalmanagement/attendance_maintance/item/@create_session_ind"/>
	<xsl:variable name="session_ind" select="/evalmanagement/attendance_maintance/item/@session_ind"/>
	<xsl:variable name="curr_attd_id">
		<xsl:value-of select="/evalmanagement/attendance_maintance/status_info/@current_id"/>
	</xsl:variable>
	<xsl:variable name="curr_attd_idx">
		<xsl:choose>
			<xsl:when test="$curr_attd_id = 0 or $curr_attd_id = -1">0</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="/evalmanagement/attendance_maintance/status_info/status_list/status">
					<xsl:if test="@id = $curr_attd_id">
						<xsl:value-of select="position()"/>
					</xsl:if>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="current_role" select="/evalmanagement/current_role"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="evalmanagement"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="evalmanagement">
		<xsl:call-template name="wb_init_lab"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="attendance_maintance">
			<xsl:with-param name="lab_attd">考勤</xsl:with-param>
			<xsl:with-param name="lab_performance">成績</xsl:with-param>
			<xsl:with-param name="lab_attd_rate">出席率</xsl:with-param>
			<xsl:with-param name="lab_remark">備註</xsl:with-param>
			<xsl:with-param name="lab_no_attd">沒有學員</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
			<xsl:with-param name="lab_view">查看</xsl:with-param>
			<xsl:with-param name="lab_auto_update">自動更新</xsl:with-param>
			<xsl:with-param name="lab_mark_as">記為</xsl:with-param>
			<xsl:with-param name="lab_attend">已出席</xsl:with-param>
			<xsl:with-param name="lab_attendpartly">部分已出席</xsl:with-param>
			<xsl:with-param name="lab_absent">缺席</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_import">匯入</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_export">匯出全部</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">保存</xsl:with-param>
			<xsl:with-param name="lab_score">分數</xsl:with-param>
			<xsl:with-param name="lab_run_info">班級信息</xsl:with-param>
			<xsl:with-param name="lab_learner">學員</xsl:with-param>
			<xsl:with-param name="lab_attd_status">出席狀態</xsl:with-param>
			<xsl:with-param name="lab_check_popup_window">您的瀏覽器不允許彈出窗口！點擊這裡</xsl:with-param>
			<xsl:with-param name="lab_search">搜索</xsl:with-param>
			<xsl:with-param name="lab_qiandao_lst">簽到記錄</xsl:with-param>
			<xsl:with-param name="lab_g_lst_btn_per">成績</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="attendance_maintance">
			<xsl:with-param name="lab_attd">考勤</xsl:with-param>
			<xsl:with-param name="lab_performance">成绩</xsl:with-param>
			<xsl:with-param name="lab_attd_rate">出席率</xsl:with-param>
			<xsl:with-param name="lab_remark">备注</xsl:with-param>
			<xsl:with-param name="lab_no_attd">没有学员</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
			<xsl:with-param name="lab_view">查看</xsl:with-param>
			<xsl:with-param name="lab_auto_update">自动更新</xsl:with-param>
			<xsl:with-param name="lab_mark_as">记为</xsl:with-param>
			<xsl:with-param name="lab_attend">已出席</xsl:with-param>
			<xsl:with-param name="lab_attendpartly">部分已出席</xsl:with-param>
			<xsl:with-param name="lab_absent">缺席</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_import">导入</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_export">导出全部</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">保存</xsl:with-param>
			<xsl:with-param name="lab_score">分数</xsl:with-param>
			<xsl:with-param name="lab_run_info">班级信息</xsl:with-param>
			<xsl:with-param name="lab_learner">学员</xsl:with-param>
			<xsl:with-param name="lab_attd_status">出席状态</xsl:with-param>
			<xsl:with-param name="lab_check_popup_window">您的浏览器不允许弹出窗口！点击这里</xsl:with-param>
			<xsl:with-param name="lab_search">搜索</xsl:with-param>
			<xsl:with-param name="lab_qiandao_lst">签到记录</xsl:with-param>
			<xsl:with-param name="lab_g_lst_btn_per">成绩</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="attendance_maintance">
			<xsl:with-param name="lab_attd">Result</xsl:with-param>
			<xsl:with-param name="lab_performance">Performance</xsl:with-param>
			<xsl:with-param name="lab_attd_rate">Attendance rate</xsl:with-param>
			<xsl:with-param name="lab_remark">Remark</xsl:with-param>
			<xsl:with-param name="lab_no_attd">No learners found</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">Recycle bin</xsl:with-param>
			<xsl:with-param name="lab_view">View</xsl:with-param>
			<xsl:with-param name="lab_auto_update">Auto update</xsl:with-param>
			<xsl:with-param name="lab_mark_as">Mark as </xsl:with-param>
			<xsl:with-param name="lab_attend"> attended</xsl:with-param>
			<xsl:with-param name="lab_attendpartly">partly attended</xsl:with-param>
			<xsl:with-param name="lab_absent">absent</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_import">Import</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_export">Export</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">Save</xsl:with-param>
			<xsl:with-param name="lab_score">Score</xsl:with-param>
			<xsl:with-param name="lab_run_info">Class information</xsl:with-param>
			<xsl:with-param name="lab_learner">Learner</xsl:with-param>
			<xsl:with-param name="lab_attd_status">Attendance status</xsl:with-param>
			<xsl:with-param name="lab_check_popup_window">Your browser does not allow popup windows! Click here</xsl:with-param>
			<xsl:with-param name="lab_search">search</xsl:with-param>
			<xsl:with-param name="lab_qiandao_lst">签到记录</xsl:with-param>
			<xsl:with-param name="lab_g_lst_btn_per">成绩</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ========================================================================================= -->
	<xsl:template match="attendance_maintance">
		<xsl:param name="lab_attd"/>
		<xsl:param name="lab_performance"/>
		<xsl:param name="lab_attd_rate"/>
		<xsl:param name="lab_no_attd"/>
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_remark"/>
		<xsl:param name="lab_view"/>
		<xsl:param name="lab_select_status"/>
		<xsl:param name="lab_auto_update"/>
		<xsl:param name="lab_g_form_btn_save"/>
		<xsl:param name="lab_score"/>	
		<xsl:param name="lab_mark_as"/>
		<xsl:param name="lab_attend"/>
		<xsl:param name="lab_attendpartly"/>
		<xsl:param name="lab_absent"/>
		<xsl:param name="lab_g_txt_btn_import"/>
		<xsl:param name="lab_g_txt_btn_export"/>	
		<xsl:param name='lab_run_info'/>
		<xsl:param name="lab_learner"/>	
		<xsl:param name="lab_attd_status"/>
		<xsl:param name="lab_check_popup_window"/>
		<xsl:param name="lab_search"/>
		<xsl:param name="lab_qiandao_lst"/>
		<xsl:param name="lab_g_lst_btn_per"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_batchprocess.js"/>	
			<script language="JavaScript" TYPE="text/javascript"><![CDATA[
			var itm_lst = new wbItem
			var attd = new wbAttendance
			var Batch = new wbBatchProcess
			
			cur_page = ]]><xsl:value-of select="$cur_page"/><![CDATA[
			
			function submit_remark(frm,attd_remark,attd_rate){
				frm.attd_remark.value=attd_remark;
				frm.attd_rate.value=attd_rate;
				attd.chg_attd_rate_exec(frm,']]><xsl:value-of select="$wb_lang"/><![CDATA[')
			}
			
			function status(){
				return false;
			}
			
			function reload_me(){
				window.location.reload();
			}		
	
					function doFeedParam(){
						param = new Array();
						tmpObj1 = new Array();
						tmpObj2 = new Array();
						tmpObj3 = new Array();
						tmpObj4 = new Array();
						tmpObj5 = new Array();
						
							tmpObj1[0] = 'cmd';
							tmpObj1[1] = 'upd_multi_att_rate';
							param[param.length] = tmpObj1;
							
							tmpObj2[0] = 'module';
							tmpObj2[1]='course.EvalManagementModule';
							param[param.length] = tmpObj2;
							
							tmpObj3[0] = 'app_id_lst';
							app_id_lst = _wbAttendanceGetAppIdCheckLst(document.frmXml);
							tmpObj3[1] = app_id_lst.split("~");
							param[param.length] = tmpObj3;
							
							tmpObj4[0] = 'attd_remark';
							tmpObj4[1] = document.frmXml.attd_remark.value;
							param[param.length] = tmpObj4;
							
							tmpObj5[0] = 'attd_rate';
							tmpObj5[1] = document.frmXml.attd_rate.value;
							param[param.length] = tmpObj5;		
							
							return param;
						}
			]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" >
			<form onsubmit="return status()" name="frmXml">
				<input type="hidden" name="cmd" value=""/>
			    <input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="app_id_lst" value=""/>
				<input type="hidden" name="itm_id" value="{$itm_id}"/>
				<input type="hidden" name="attd_remark"/>
				<input type="hidden" name="attd_rate"/>
				<input type="hidden" name="module"/>
				<input type="hidden" name="att_status" />
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
					<xsl:with-param name="parent_code" select="$parent_code"/>
				</xsl:call-template>
		
				<xsl:call-template name="itm_action_nav">
					<xsl:with-param  name="cur_node_id">116</xsl:with-param>
				</xsl:call-template>
			<div class="wzb-item-main">
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">
						<xsl:value-of select="//itm_action_nav/@itm_title"/>
					</xsl:with-param>
				</xsl:call-template>	
	
				<xsl:call-template name="wb_ui_nav_link">
					<xsl:with-param name="text">
						<xsl:choose>
							<xsl:when test="$current_role='INSTR_1'">
							    <xsl:choose>
							    	<xsl:when test="item/item_type_meta/@run_ind='true'">
							    		<a href="javascript:itm_lst.get_itm_instr_view({item/parent/@id})" class="NavLink">
											<xsl:value-of select="item/title"/>
										</a>
									</xsl:when>
							    	<xsl:otherwise>
							    		<a href="javascript:itm_lst.get_itm_instr_view({$itm_id})" class="NavLink">
											<xsl:value-of select="item/title"/>
										</a>
							    	</xsl:otherwise>
							    </xsl:choose>
							</xsl:when>
							<xsl:when test="item/item_type_meta/@run_ind='true'">
								<a href="Javascript:itm_lst.get_item_detail({item/parent/@id})" class="NavLink">
									<xsl:value-of select="item/parent/title"/>
								</a>
								<xsl:text>&#160;&gt;&#160;</xsl:text>
								<a href="Javascript:itm_lst.get_item_run_list({item/parent/@id})" class="NavLink">
									<xsl:choose>
										<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
										<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
									</xsl:choose>
								</a>
								<xsl:text>&#160;&gt;&#160;</xsl:text>
								<a href="Javascript:itm_lst.get_item_run_detail({item/@id})" class="NavLink">
									<xsl:value-of select="item/title"/>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
									<xsl:value-of select="item/title"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:value-of select="$lab_attd_rate"/>
					</xsl:with-param>
				</xsl:call-template>
				<table>
					<tr>
						<td align="center">
							<xsl:call-template name="check_client">
								<xsl:with-param name="lab_check_client"><xsl:value-of select="$lab_check_popup_window"/></xsl:with-param>
								<xsl:with-param name="img_path" select="$wb_img_path"/>
								<xsl:with-param name="check_item">POPUP</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
				</table>
				<xsl:if test="count(attendance_list/attendance) &gt;= 1">
					<table>
						<tr>
							<td align="right" width="40%">
								<table>
									<tr>
										<td width="90%" align="right">
										   <input type="text"  name="user_code" class="wzb-inputText" style="width:110px;" onkeypress=""/>
								    	   <xsl:call-template name="wb_gen_button">
								    	   		<xsl:with-param name="style">margin-top:-2px;</xsl:with-param>
								        	    <xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_search"/></xsl:with-param>
								        	    <xsl:with-param name="wb_gen_btn_href">javascript:attd.search_user_list_rate(document.frmXml)</xsl:with-param>
								  	   	    </xsl:call-template>
								    	</td>
								     </tr>
								   </table>
								</td>
							</tr>
						</table>
						<table class="wzb-ui-head">
							<tr>
								<td width="50%" align="right">
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_name">
												<xsl:value-of select="$lab_mark_as"/>100%<xsl:value-of select="$lab_attend"/>
											</xsl:with-param>																			<xsl:with-param name="wb_gen_btn_href">javascript:attd.chg_attd_rate_prep(document.frmXml,'<xsl:value-of select="$wb_lang"/>',1)</xsl:with-param>
										</xsl:call-template>
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_name">
												<xsl:value-of select="$lab_mark_as"/>
												<xsl:value-of select="$lab_attendpartly"/>
											</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_href">javascript:attd.chg_attd_rate_prep(document.frmXml,'<xsl:value-of select="$wb_lang"/>',2)</xsl:with-param>
										</xsl:call-template>
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_name">
												<xsl:value-of select="$lab_mark_as"/>
												<xsl:value-of select="$lab_absent"/>
											</xsl:with-param>																			<xsl:with-param name="wb_gen_btn_href">javascript:attd.chg_attd_rate_prep(document.frmXml,'<xsl:value-of select="$wb_lang"/>',3)</xsl:with-param>
										</xsl:call-template>
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_import"/>
											<xsl:with-param name="wb_gen_btn_href">javascript:Batch.AttdRate.Import.prep(<xsl:value-of select="item/@id"/>)</xsl:with-param>
										</xsl:call-template>
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_export"/>
											<xsl:with-param name="wb_gen_btn_href">javascript:attd.dl_att_rate_lst(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
										</xsl:call-template>
									</td>
								</tr>
						</table>
				</xsl:if>
				<!-- List Header -->
				<xsl:choose>
					<xsl:when test="count(attendance_list/attendance) &gt;= 1">
						<table class="table wzb-ui-table">
							<tr class="wzb-ui-table-head">
								<td width="25%">
									<xsl:call-template name="select_all_checkbox">
										<xsl:with-param name="chkbox_lst_cnt">
											<xsl:value-of select="count(attendance_list/attendance)"/>
										</xsl:with-param>
										<xsl:with-param name="display_icon">false</xsl:with-param>
									</xsl:call-template>
									<xsl:value-of select="$lab_learner"/>
								</td>
								<td width="25%" align="center">
										<xsl:value-of select="$lab_attd_status"/>
								</td>
								<td width="25%" align="center">
								   <xsl:value-of select="$lab_attd_rate"/><xsl:text>(%)</xsl:text>
								</td>
								<td width="25%" align="right">
									<xsl:value-of select="$lab_remark"/>
								</td>
							</tr>
							<xsl:apply-templates select="attendance_list/attendance">
								<xsl:with-param name="lab_attend" select="$lab_attend"/>
								<xsl:with-param name="lab_absent" select="$lab_absent"/>
								<xsl:with-param name="lab_attendpartly" select="$lab_attendpartly"/>
								<xsl:with-param name="lab_remark" select="$lab_remark"/>
								<xsl:with-param name="lab_view" select="$lab_view"/>
								<xsl:with-param name="lab_lost_and_found" select="$lab_lost_and_found"/>
							</xsl:apply-templates>
						</table>
						<xsl:call-template name="wb_ui_pagination">
							<xsl:with-param name="cur_page" select="$cur_page"/>
							<xsl:with-param name="page_size" select="$page_size"/>
							<xsl:with-param name="total" select="$total"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_no_attd"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</div>
			</form>
			<script>
				<![CDATA[
				if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion .split(";")[1].replace(/[ ]/g,"")=="MSIE9.0") 
				{ 
					$(".wzb-btn-blue").css("margin-top","-6px");
				}
				]]>
			</script>
		</body>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template match="attendance">
		<xsl:param name="lab_attend"/>
		<xsl:param name="lab_absent"/>
		<xsl:param name="lab_attendpartly"/>
		<xsl:param name="lab_remark"/>
		<xsl:param name="lab_view"/>
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="att_rate"><xsl:value-of select="number(substring-before(@rate,'.'))"/></xsl:param>
		<tr>
			<td>
				<input type="checkbox" name="app_id" value="{@app_id}" id="{user/name/@display_name}"/>
				<xsl:value-of select="user/name/@display_name"/>
			</td>
			<td  align="center">
				<xsl:choose>
					<xsl:when test="@rate=''">--</xsl:when>
					<xsl:when test="$att_rate='100'"><xsl:value-of select="$lab_attend"/></xsl:when>
					<xsl:when test="$att_rate='0'"><xsl:value-of select="$lab_absent"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$lab_attendpartly"/></xsl:otherwise>
				</xsl:choose>
			</td>
			<td  align="center">
				<xsl:choose>
					<xsl:when test="@rate=''">--</xsl:when>
					<xsl:otherwise><xsl:value-of select="$att_rate"/></xsl:otherwise>
				</xsl:choose>
			</td>
			<!-- commented for display an input box -->
			<td align="right">
				<xsl:choose>
					<xsl:when test="att_rate_remark != ''">
						<xsl:call-template name="unescape_html_linefeed">
							<xsl:with-param name="my_right_value">
								<xsl:value-of select="att_rate_remark"/>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>--</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
	<!-- ====================================================================================================== -->

	<!-- ====================================================================================================== -->
</xsl:stylesheet>
