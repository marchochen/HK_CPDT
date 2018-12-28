<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="width">
		<xsl:choose>
			<xsl:when test="/selmod/sel_mod_option/width/text() != '' and /selmod/sel_mod_option/width/text() != 'null'">
				<xsl:value-of select="/selmod/sel_mod_option/width/text()"/>
			</xsl:when>
			<xsl:otherwise>482</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>	
	<!-- =============================================================== -->
	<xsl:template match="/credit">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<meta http-equiv="X-UA-Compatible" content="IE=edge" />
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
			<script language="JavaScript"><![CDATA[
			var course_lst = new wbCourse;
			var attd = new wbAttendance
			function ini() {
				frmXml.title_code.focus();
			 }
			]]></script>
			<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="ini()">
			<form name="frmXml" onSubmit="return false">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_step_1">步驟1. 搜索你需要設置積分的課程。請你選擇搜索標準，輸入搜索內容，並點擊<b>搜索</b>。</xsl:with-param>
			<xsl:with-param name="lab_sel">額外獎勵課程積分</xsl:with-param>
			<xsl:with-param name="lab_search">搜索</xsl:with-param>
			<xsl:with-param name="lab_step_2">步驟2. 點擊下面的一個課程，將會顯示該課程學習狀態爲已完成的學員。</xsl:with-param>
			<xsl:with-param name="lab_all">所有學員</xsl:with-param>			
			<xsl:with-param name="lab_on">已積分的學員</xsl:with-param>
			<xsl:with-param name="lab_off">未積分的學員</xsl:with-param>
			<xsl:with-param name="lab_step_3">步骤3. 額外給下面列表裏學員獎勵課程積分。</xsl:with-param>
			<xsl:with-param name="lab_no_rec">沒有記錄</xsl:with-param>
			<xsl:with-param name="lab_close">取消</xsl:with-param>
			<xsl:with-param name="lab_no_result">-- 沒有找到記錄 --</xsl:with-param>
			<xsl:with-param name="lab_searching">-- 搜索中... --</xsl:with-param>
			<xsl:with-param name="lab_loading">下載中...</xsl:with-param>
			<xsl:with-param name="lab_cct">課程（班別）編號或標題</xsl:with-param>
			<xsl:with-param name="lab_cc">課程（班別）編號</xsl:with-param>
			<xsl:with-param name="lab_ct">課程（班別）標題</xsl:with-param>
			<xsl:with-param name="lab_filter_name">用戶名或全名</xsl:with-param>
			<xsl:with-param name="lab_set_credit">獎勵積分</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_step_1">步骤1. 搜索您需要设置积分的课程。请您选择搜索标准，输入搜索内容，并点击<b>搜索</b>。</xsl:with-param>
			<xsl:with-param name="lab_sel">额外奖励课程积分</xsl:with-param>
			<xsl:with-param name="lab_search">搜索</xsl:with-param>
			<xsl:with-param name="lab_step_2">步骤2. 点击下面的一个课程，将会显示该课程学习状态为已完成的学员。</xsl:with-param>
			<xsl:with-param name="lab_all">所有学员</xsl:with-param>
			<xsl:with-param name="lab_on">已积分的学员</xsl:with-param>
			<xsl:with-param name="lab_off">未积分的学员</xsl:with-param>
			<xsl:with-param name="lab_step_3">步骤3. 额外给下面列表里学员奖励课程积分。</xsl:with-param>
			<xsl:with-param name="lab_no_rec">没有记录</xsl:with-param>
			<xsl:with-param name="lab_close">取消</xsl:with-param>
			<xsl:with-param name="lab_no_result">-- 没有找到记录 --</xsl:with-param>
			<xsl:with-param name="lab_searching">-- 搜索中... --</xsl:with-param>
			<xsl:with-param name="lab_loading">下载中...</xsl:with-param>
			<xsl:with-param name="lab_cct">课程（班别）编号或标题</xsl:with-param>
			<xsl:with-param name="lab_cc">课程（班别）编号</xsl:with-param>
			<xsl:with-param name="lab_ct">课程（班别）标题</xsl:with-param>
			<xsl:with-param name="lab_filter_name">用户名或全名</xsl:with-param>
			<xsl:with-param name="lab_set_credit">奖励积分</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_step_1">Step 1. search credits setting required sourses. You can select search criteria, enter search text and then click <b>Search</b>.</xsl:with-param>
			<xsl:with-param name="lab_sel">Set course points</xsl:with-param>
			<xsl:with-param name="lab_search">Search</xsl:with-param>
			<xsl:with-param name="lab_step_2">Step 2. click course below and learners with completed learning status will be displayed.</xsl:with-param>
			<xsl:with-param name="lab_all">All learners</xsl:with-param>
			<xsl:with-param name="lab_on">Credited learners</xsl:with-param>
			<xsl:with-param name="lab_off">Non-credited learners</xsl:with-param>
			<xsl:with-param name="lab_step_3">Step 3. Grant additional bonus points to the users on the list。</xsl:with-param>
			<xsl:with-param name="lab_no_rec">No modules found</xsl:with-param>
			<xsl:with-param name="lab_close">Cancel</xsl:with-param>
			<xsl:with-param name="lab_no_result">-- No results found --</xsl:with-param>
			<xsl:with-param name="lab_searching">-- Searching... -- </xsl:with-param>
			<xsl:with-param name="lab_loading">Loading...</xsl:with-param>
			<xsl:with-param name="lab_cct">Course(class) code or title</xsl:with-param>
			<xsl:with-param name="lab_cc">Course(class) code</xsl:with-param>
			<xsl:with-param name="lab_ct">Course(class) title</xsl:with-param>
			<xsl:with-param name="lab_filter_name">User ID or full name</xsl:with-param>
			<xsl:with-param name="lab_set_credit">Bonus points</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_step_1"/>
		<xsl:param name="lab_sel"/>
		<xsl:param name="lab_search"/>
		<xsl:param name="lab_step_2"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_on"/>
		<xsl:param name="lab_off"/>
		<xsl:param name="lab_no_rec"/>
		<xsl:param name="lab_step_3"/>
		<xsl:param name="lab_close"/>
		<xsl:param name="lab_no_result"/>
		<xsl:param name="lab_searching"/>
		<xsl:param name="lab_loading"/>
		<xsl:param name="lab_cct"/>
		<xsl:param name="lab_cc"/>
		<xsl:param name="lab_ct"/>
		<xsl:param name="lab_filter_name"/>
		<xsl:param name="lab_set_credit"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_sel"/>
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<input type="hidden" name="cmd"/>
		<input type="hidden" name="module"/>
		<input type="hidden" name="dis_cos_type" value="{/selmod/sel_mod_option/dis_cos_type/text()}"/>
		<input type="hidden" name="dis_mod_type" value="{/selmod/sel_mod_option/dis_mod_type/text()}"/>
		<input type="hidden" name="itm_tcr_id" value="{/selmod/sel_mod_option/training_center/@id}"/>
		<input type="hidden" name="sel_app_id_list"/>
		<input type="hidden" name="sel_usr_ent_id_list"/>
		<input type="hidden" name="url_success"/>
		<input type="hidden" name="url_failure"/>
		<input type="hidden" name="itm_id"/>
		<!--================================================================================-->
		<script language="JavaScript"><![CDATA[
		var req;
		var width = trimvar ("]]><xsl:value-of select="$width"/><![CDATA[");
	

		function returnResult(frm, keyCode) {
		  if(keyCode == null || keyCode == 13) {
		      /*
		      var e;
		      for(var i=0;i<frm.search_type.options.length;i++){
					if(frm.search_type.options[i].selected){
						e = frm.search_type.options[i];
					}
				}
				*/
		      if(!wbUtilsValidateEmptyField(frm.title_code,wb_msg_enter_condition )) {
		      		frm.title_code.focus();
		      		return;
		      }
			   search_type = frm.search_type.value;
			   title_code = frm.title_code.value
			  
			   title_code = trimvar (title_code);
			   dis_cos_type = frm.dis_cos_type.value;
			   itm_tcr_id = frm.itm_tcr_id.value;
			   clearSearchResult(frm);	   
			   var url = wb_utils_invoke_disp_servlet();
             var str_paramer = 'module=course.ModuleSelectXmlModule&cmd=search_cos'
                             + '&search_type=' + search_type 
                             + '&dis_cos_type=' + dis_cos_type 
                             + '&title_code=' + title_code
                             +'&itm_tcr_id=' + itm_tcr_id;
        
           	req = getXMLHttpRequest();
				req.onreadystatechange = buildResultList;
				req.open("POST", url,true);
             req.setRequestHeader("CONTENT-TYPE","application/x-www-form-urlencoded");
			   req.setRequestHeader("Content-Length",str_paramer.length);  	
				req.send(str_paramer);
			}
			
		}
		function trimvar(varstring) {
		   temp = '';
			for (i = 0; i<varstring.length; i++){
				varchar = varstring.substring(i,i+1);
				if(varchar == '%') {
					temp = temp + escape(varchar);
				}else {
					temp = temp + varchar;
				}
			}
			return temp;
		}
		
		function clearSearchResult(frm) {
		    var select = frm.search_result;
		    while (select .length > 0) {
		        select .remove(0);
		    }
		    select[0] = new Option("]]><xsl:value-of select="$lab_searching"/><![CDATA[", "");
		     //select [0].text = "]]><xsl:value-of select="$lab_searching"/><![CDATA[";
		     select.disabled = true;
		     frm.cov_status[0].disabled = true;
			  frm.cov_status[1].disabled = true;
			  frm.cov_status[2].disabled = true;
			  // clear detail display
			  defaultHTML ();
      }
		      
      function buildResultList() {
      // only if req shows "loaded"
		   if (req.readyState == 4) {
			// only if "OK"
				if (req.status == 200) {
					    var select = document.frmXml.search_result;
					    var items = req.responseXML.getElementsByTagName("item");
					    if(items != null && items.length > 0) {
					        select.disabled = false;
						    for (var i = 0; i < items.length; i++) {
						      value = getElementTextNS("id", items[i], 0) ;
						      text =  getElementTextNS("title", items[i], 0) ;
						      select[i] = new Option(text, value);
						    }
						     document.frmXml.cov_status[0].disabled = false;
							  document.frmXml.cov_status[1].disabled = false;
							  document.frmXml.cov_status[2].disabled = false;
				
						  } 
						  else{
						     select[0] = new Option("]]><xsl:value-of select="$lab_no_result"/><![CDATA[", "");
						  }
						  
				}
			}
		}
		
		function getElementTextNS(local, parentElem, index) {
		    var result = "";
          result = parentElem.getElementsByTagName(local)[index];
		  
		    if (result) {
		        if (result.childNodes.length > 1) {
		            return result.childNodes[1].nodeValue;
		        } else {
		            return result.firstChild.nodeValue;    		
		        }
		    } else {
		        return "";
		    }
		}
		
		function defaultHTML (loading){
	      result = '<table cellspacing="0" cellpadding="0" border="0" width="' + width  + '">' +
							'<tr class="Line">' +
									'<td height="1">' +
										'<img width="1" src="]]><xsl:value-of select="$wb_img_path"/><![CDATA[tp.gif" height="1" border="0"/>' +
									'</td>' +
							'</tr>' +
					'</table>' +
					'<table width="' + width  + '" border="0" cellpadding="3" cellspacing="0">' +
							'<tr class="RowsEven" valign="middle">' +
									'<td colspan="3">' +
										'<img width="1" src="]]><xsl:value-of select="$wb_img_path"/><![CDATA[tp.gif" height="8" border="0"/>' +
									'</td>' +
							'</tr>' +
							'<tr class="RowsEven" valign="middle">' +
									'<td width="8">' +
										'<img width="1" src="]]><xsl:value-of select="$wb_img_path"/><![CDATA[tp.gif" height="1" border="0"/>' +
									'</td>' +
									'<td align="center">' +
										'<span class="Text">' ;
		if(loading == 'true') {		
			result = result + ']]><xsl:value-of select="$lab_loading"/><![CDATA[' ;
		} else {
		 	result = result + '<div class="losedata"><i class="fa fa-folder-open-o"></i><p>'+
		 	']]><xsl:value-of select="$lab_no_rec"/><![CDATA[' + '</p></div>';
		}
	       result = result + '</span>' +
									'</td>' +
									'<td width="8">' +
										'<img width="1" src="]]><xsl:value-of select="$wb_img_path"/><![CDATA[tp.gif" height="1" border="0"/>' +
									'</td>' +
							'</tr>' +
							'<tr class="RowsEven" valign="middle">' +
									'<td colspan="3">' +
										'<img width="1" src="]]><xsl:value-of select="$wb_img_path"/><![CDATA[tp.gif" height="8" border="0"/>' +
									'</td>' +
							'</tr>' +
					'</table>' +
					'<table cellspacing="0" cellpadding="0" border="0" width="' + width  + '">' +
							'<tr class="Line">' +
									'<td height="1">' +
										'<img width="1" src="]]><xsl:value-of select="$wb_img_path"/><![CDATA[tp.gif" height="1" border="0"/>' +
									'</td>' +
							'</tr>' +
					'</table>';

					
				document.getElementById("attend_app_list").innerHTML = result;
		}
		
		function getCovUser(frm, itm, keyCode) {
			if(keyCode == null || keyCode == 13) {
				var status;
				 
				if(itm.value == null || itm.value == ''){
					return;
				}
				var cov_status = document.frmXml.cov_status;
				for(i = 0; i < cov_status.length; i++) {
					if(cov_status[i].checked){
						status = cov_status[i].value;
					}
				}
				var usr_steid_or_diplaybil = wbUtilsTrimString(document.frmXml.usr_steid_or_diplaybil.value);
				defaultHTML ('true');
				var url = wb_utils_invoke_disp_servlet();
				var str_paramer = 'module=JsonMod.credit.CreditModule&cmd=GET_ATTEND_USER_LST&stylesheet=credit_attendance_lst.xsl'
                             + '&itm_id=' + itm.value 
                             + '&ucd_itm_status=' + status 
                             + '&usr_steid_or_diplaybil=' + usr_steid_or_diplaybil;
        
           	    req = getXMLHttpRequest();
				req.onreadystatechange = function() {    	if (req.readyState == 4) {
																   if (req.status == 200) {
																		 document.getElementById("attend_app_list").innerHTML = req.responseText;
																	}
															 }
														  }
				req.open("POST", url,true);
                req.setRequestHeader("CONTENT-TYPE","application/x-www-form-urlencoded");
			    req.setRequestHeader("Content-Length",str_paramer.length);  	
				req.send(str_paramer);
			}
		}
		
		function setAttendUserCredit(frm){
			var app_id_list = document.getElementsByName('app_id');
			var usr_ent_id_list = document.getElementsByName('usr_ent_id')
			if (app_id_list != null && app_id_list.length > 0) {
				var had_sel = false;
				for (i = 0; i < app_id_list.length; i++) {
					if (app_id_list[i].checked) {
						had_sel = true;
					}
				}
				if (!had_sel) {
					alert(wb_msg_pls_sel_usr);
					return;
				}
				var valPass = true
				var val = wbUtilsTrimString(frm.input_point.value)
				
				if(val<=0 || (val.indexOf('.') >= 0) ){
					valPass = false;
				}else if(val.length == 0 || val.search(/[^0-9]|\-[^0-9]/) != -1){
					if(isNaN(Number(val))){
						valPass = false;
					}
				}
				if(valPass == false){
					alert(wb_msg_pls_enter_nonzero_integer_score);	
					frm.input_point.focus();
					return false;
				}
				
				frm.sel_app_id_list.value = '';
				frm.sel_usr_ent_id_list.value = '';
				for (i = 0; i < app_id_list.length; i++) {
					if (app_id_list[i].checked) {
						frm.sel_app_id_list.value = frm.sel_app_id_list.value + '~' + app_id_list[i].value
						frm.sel_usr_ent_id_list.value = frm.sel_usr_ent_id_list.value + '~' + usr_ent_id_list[i].value
					}
				}
			}
			if (confirm(wb_msg_confirm)) {
				frm.action = wb_utils_disp_servlet_url + "?isExcludes=true";
				frm.module.value = 'JsonMod.credit.CreditModule';
				frm.cmd.value = 'set_attend_usr_credit';
				frm.itm_id.value = frm.search_result.value;
				frm.url_success.value = self.location.href;
				frm.url_failure.value = self.location.href;
				frm.method = 'post';		
				frm.submit();
			}
		}
        ]]></script>
		<!--================================================================================-->
		<!-- step 1===================================================== -->
		<table>
			<tr>
				<td>
				</td>
				<td colspan="2" width="100%" align="left" class="Desc">
					<xsl:copy-of select="$lab_step_1"/>
				</td>
			</tr>
			<tr>
				<td>
					<img width="12" src="{$wb_img_path}tp.gif" height="1" border="0"/>
				</td>
				<td nowrap="nowrap">
					<select class="wzb-form-select margin-right4" name="search_type" >
						<option value="ct_cc" selected="selected">
							<xsl:value-of select="$lab_cct"/>
						</option>
						<option value="cc">
							<xsl:value-of select="$lab_cc"/>
						</option>
						<option value="ct">
							<xsl:value-of select="$lab_ct"/>
						</option>
					</select>
					<input type="text" size="11" name="title_code" class="wzb-inputText margin-right4" style="width:130px;" onKeyPress="returnResult(frmXml,event.keyCode)"/>
				</td>
				<td nowrap="nowrap" width="100%" align="left">
					<input class="btn wzb-btn-blue" value="{$lab_search}" type="button" href="#" onclick="javascript:returnResult(frmXml)"/>
				</td>
			</tr>
		</table>
		<!-- step 2 =======================================================================-->
		<table>
			<tr>
				<td>
					<img width="12" src="{$wb_img_path}tp.gif" height="1" border="0"/>
				</td>
				<td width="100%" class="Desc">
					<xsl:value-of select="$lab_step_2"/>
					<br/>
					<select onChange="getCovUser(frmXml,this)" style="width:400px;height:100px" class="wzb-select" size="6" name="search_result" disabled="disabled">
						<option value="0">
							<xsl:value-of select="$lab_no_result"/>
						</option>
					</select>
					<br/>
					<input type="radio" name="cov_status" value="ALL" id="cov_status_all" disabled="true" checked="checked" onclick="getCovUser(frmXml,frmXml.search_result)">
						<label for="cov_status_all">
							<xsl:value-of select="$lab_all"/>
						</label>
					</input>&#160;
						<input type="radio" name="cov_status" value="YES" id="cov_status_c" disabled="true" onclick="getCovUser( frmXml,frmXml.search_result )">
						<label for="cov_status_c">
							<xsl:value-of select="$lab_on"/>
						</label>
					</input>&#160;
						<input type="radio" name="cov_status" value="NO" id="cov_status_i" disabled="true" onclick="getCovUser(frmXml, frmXml.search_result)">
						<label for="cov_status_i">
							<xsl:value-of select="$lab_off"/>
						</label>
					</input>&#160;
				</td>
			</tr>
		</table>
		<!-- step 3 ===================================================-->
		<table>
			<tr>
				<td>
					<img width="12" src="{$wb_img_path}tp.gif" height="1" border="0"/>
				</td>
				<td width="100%" class="Desc">
					<xsl:value-of select="$lab_step_3"/>
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td width="10%" align="right">
					<input type="text" size="{$wb_gen_num_input_length}"  maxlength="{$wb_gen_num_input_length}" name="input_point" class="wzb-inputText margin-right4"/>
				</td>
				<td width="40%" align="left">
					<input class="btn wzb-btn-blue" value="{$lab_set_credit}" type="button" href="#" onclick="javascript:setAttendUserCredit(frmXml)"/>
				</td>
				<td width="5%" align="left">
					<input type="text" size="10"  name="usr_steid_or_diplaybil" class="wzb-inputText margin-right4" style="width:130px;" title="{$lab_filter_name}" onKeyPress="getCovUser(frmXml, frmXml.search_result, event.keyCode)"/>
				</td>
				<td width="45%" align="left">
					<input class="btn wzb-btn-blue" value="{$lab_search}" type="button" href="#" onclick="javascript:getCovUser( frmXml,frmXml.search_result)"/>
				</td>
			</tr>
		</table>
		<div id="attend_app_list">
			<script language="JavaScript">
			defaultHTML ();
			</script>
		</div>
		<div class="wzb-bar">
			<xsl:variable name="cancel_action">
				<xsl:choose>
					<xsl:when test="/selmod/sel_mod_option/sel_type = 'import_mod'">history.back()</xsl:when>
					<xsl:otherwise>window.close()</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_close"/>
				<xsl:with-param name="wb_gen_btn_href" select="$cancel_action"/>
			</xsl:call-template>
	</div>
		<!--==========================================================================================-->
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
