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
	<xsl:import href="utils/wb_ui_head.xsl"/>
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
	<!-- =============================================================== -->
	<xsl:template match="/selmod">
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
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			
			<!--alert样式  -->
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			
			<script language="JavaScript"><![CDATA[
			var course_lst = new wbCourse;
			function ini() {
				frmXml.title_code.focus();
				document.frmXml.ok_action.disabled = true;
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
			<xsl:with-param name="lab_step_1">步驟1. 搜索你需要的模塊的課程。請你選擇搜索標準，輸入搜索內容，並點擊<b>搜索</b>。</xsl:with-param>
			<xsl:with-param name="lab_import">Import Module</xsl:with-param>
			<xsl:with-param name="lab_sel">選擇模塊</xsl:with-param>
			<xsl:with-param name="lab_search">搜索</xsl:with-param>
			<xsl:with-param name="lab_step_2">步驟2. 點擊下面的一個課程，將會顯示該課程的模塊。</xsl:with-param>
			<xsl:with-param name="lab_all">任何模塊</xsl:with-param>
			<xsl:with-param name="lab_on">已發佈模塊</xsl:with-param>
			<xsl:with-param name="lab_off">未發佈模塊</xsl:with-param>
			<xsl:with-param name="lab_step_3">步驟3. 從下面的列表裡，選擇你需要的模塊。</xsl:with-param>
			<xsl:with-param name="lab_no_mod">沒有模塊</xsl:with-param>
			<xsl:with-param name="lab_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_close">取消</xsl:with-param>
			<xsl:with-param name="lab_no_result">-- 沒有找到記錄 --</xsl:with-param>
			<xsl:with-param name="lab_searching">-- 搜索中... --</xsl:with-param>
			<xsl:with-param name="lab_loading">下載中...</xsl:with-param>
			<xsl:with-param name="lab_cct">課程（班別）編號或標題</xsl:with-param>
			<xsl:with-param name="lab_cc">課程（班別）編號</xsl:with-param>
			<xsl:with-param name="lab_ct">課程（班別）標題</xsl:with-param>
			<xsl:with-param name="lab_mt">模塊標題</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_step_1">步骤1. 搜索您需要的模块的课程。请您选择搜索标准，输入搜索内容，并点击<b>搜索</b>。</xsl:with-param>
			<xsl:with-param name="lab_import">Import Module</xsl:with-param>
			<xsl:with-param name="lab_sel">选择模块</xsl:with-param>
			<xsl:with-param name="lab_search">搜索</xsl:with-param>
			<xsl:with-param name="lab_step_2">步骤2. 点击下面的一个课程，将会显示该课程的模块。</xsl:with-param>
			<xsl:with-param name="lab_all">任何模块</xsl:with-param>
			<xsl:with-param name="lab_on">已发布模块</xsl:with-param>
			<xsl:with-param name="lab_off">未发布模块</xsl:with-param>
			<xsl:with-param name="lab_step_3">步骤3. 从下面的列表里，选择你需要的模块。</xsl:with-param>
			<xsl:with-param name="lab_no_mod">没有模块</xsl:with-param>
			<xsl:with-param name="lab_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_close">取消</xsl:with-param>
			<xsl:with-param name="lab_no_result">-- 没有找到记录 --</xsl:with-param>
			<xsl:with-param name="lab_searching">-- 搜索中... --</xsl:with-param>
			<xsl:with-param name="lab_loading">下载中...</xsl:with-param>
			<xsl:with-param name="lab_cct">课程（班别）编号或标题</xsl:with-param>
			<xsl:with-param name="lab_cc">课程（班别）编号</xsl:with-param>
			<xsl:with-param name="lab_ct">课程（班别）标题</xsl:with-param>
			<xsl:with-param name="lab_mt">模块标题</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_step_1">Step 1. search for the course that contains the module you need. You can select search criteria, enter search text and then click <b>Search</b>.</xsl:with-param>
			<xsl:with-param name="lab_import">Import module</xsl:with-param>
			<xsl:with-param name="lab_sel">Select module</xsl:with-param>
			<xsl:with-param name="lab_search">Search</xsl:with-param>
			<xsl:with-param name="lab_step_2">Step 2. Click a course below to display the module of the course.</xsl:with-param>
			<xsl:with-param name="lab_all">Any modules</xsl:with-param>
			<xsl:with-param name="lab_on">Online modules</xsl:with-param>
			<xsl:with-param name="lab_off">Offline modules</xsl:with-param>
			<xsl:with-param name="lab_step_3">Step 3. from the list below, select the module you need.</xsl:with-param>
			<xsl:with-param name="lab_no_mod">No modules found</xsl:with-param>
			<xsl:with-param name="lab_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_close">Cancel</xsl:with-param>
			<xsl:with-param name="lab_no_result">-- No results found --</xsl:with-param>
			<xsl:with-param name="lab_searching">-- Searching... -- </xsl:with-param>
			<xsl:with-param name="lab_loading">Loading...</xsl:with-param>
			<xsl:with-param name="lab_cct">Course(class) code or title</xsl:with-param>
			<xsl:with-param name="lab_cc">Course(class) code</xsl:with-param>
			<xsl:with-param name="lab_ct">Course(class) title</xsl:with-param>
			<xsl:with-param name="lab_mt">Module title</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_step_1"/>
		<xsl:param name="lab_import"/>
		<xsl:param name="lab_sel"/>
		<xsl:param name="lab_search"/>
		<xsl:param name="lab_step_2"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_on"/>
		<xsl:param name="lab_off"/>
		<xsl:param name="lab_no_mod"/>
		<xsl:param name="lab_step_3"/>
		<xsl:param name="lab_ok"/>
		<xsl:param name="lab_close"/>
		<xsl:param name="lab_no_result"/>
		<xsl:param name="lab_searching"/>
		<xsl:param name="lab_loading"/>
		<xsl:param name="lab_cct"/>
		<xsl:param name="lab_cc"/>
		<xsl:param name="lab_ct"/>
		<xsl:param name="lab_mt"/>
	
		<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="text" select="$lab_sel"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>	
		<input type="hidden" name="cmd"/>
		<input type="hidden" name="sel_type" value="{/selmod/sel_mod_option/sel_type/text()}"/>
		<input type="hidden" name="multiple" value="{/selmod/sel_mod_option/is_multiple/text()}"/>
		<input type="hidden" name="dis_cos_type" value="{/selmod/sel_mod_option/dis_cos_type/text()}"/>
		<input type="hidden" name="dis_mod_type" value="{/selmod/sel_mod_option/dis_mod_type/text()}"/>
		<input type="hidden" name="re_field_name" value="{/selmod/sel_mod_option/re_field_name/text()}"/>
		<input type="hidden" name="itm_tcr_id" value="{/selmod/sel_mod_option/training_center/@id}"/>
		<input type="hidden" name="sel_mod_id_list"/>
		<input type="hidden" name="url_success"/>
		<input type="hidden" name="url_failure"/>
		<input type="hidden" name="clo_src_itm_id"/>
		<input type="hidden" name="clo_tag_cos_res_id"/>
		<!--================================================================================-->
		<script language="JavaScript"><![CDATA[
		var req;
		var width = trimvar ("]]><xsl:value-of select="$width"/><![CDATA[");
	

		function returnResult(frm, keyCode) {
		  if(keyCode == null || keyCode == 13) {
		 	  document.frmXml.ok_action.disabled = true;
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
		     frm.mod_status[0].disabled = true;
			  frm.mod_status[1].disabled = true;
			  frm.mod_status[2].disabled = true;
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
						     document.frmXml.mod_status[0].disabled = false;
							  document.frmXml.mod_status[1].disabled = false;
							  document.frmXml.mod_status[2].disabled = false;
				
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
	      result = '<table>' +
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
           result = result + '<div class="losedata" style="margin-top:5px">' +
           						'<i class="fa fa-folder-open-o"></i>'+
           							'<p>'+
            ']]><xsl:value-of select="$lab_no_mod"/><![CDATA['+'</p>'+'</div>' ;
          }
	       result = result + '</span>' +
									'</td>' +
									'<td width="8">' +
										'<img width="1" src="]]><xsl:value-of select="$wb_img_path"/><![CDATA[tp.gif" height="1" border="0"/>' +
									'</td>' +
							'</tr>' +
							'<tr>' +
									'<td colspan="3">' +
										'<img width="1" src="]]><xsl:value-of select="$wb_img_path"/><![CDATA[tp.gif" height="8" border="0"/>' +
									'</td>' +
							'</tr>' +
					'</table>' +
					'<table>' +
							'<tr class="Line">' +
									'<td height="1">' +
										'<img width="1" src="]]><xsl:value-of select="$wb_img_path"/><![CDATA[tp.gif" height="1" border="0"/>' +
									'</td>' +
							'</tr>' +
					'</table>';

					
				document.getElementById("module_list").innerHTML = result;
		}
		
		function setModule(frm, itm) {
		var status;
		 
		if(itm.value == null || itm.value == ''){
			return;
		}
		document.frmXml.ok_action.disabled = true;
		var mod_status = document.frmXml.mod_status;
		for(i = 0; i < mod_status.length; i++) {
			if(mod_status[i].checked){
				status = mod_status[i].value;
			}
		}
		
       defaultHTML ('true');
		url = wb_utils_invoke_disp_servlet('module' ,'course.ModuleSelectModule' ,'cmd', 'get_sel_mod', 'is_multiple', frm.multiple.value, 'dis_mod_type', frm.dis_mod_type.value, 'sel_type', frm.sel_type.value, 'stylesheet', 'wb_goldman_sel_mod_list.xsl', 'itm_id', itm.value, 'sel_mod_status', status, 'width', width)
	   req = getXMLHttpRequest();
		req.onreadystatechange = function() {    if (req.readyState == 4) {
							                               if (req.status == 200) {
								                                 document.getElementById("module_list").innerHTML = req.responseText;
								                                 var mod_list = document.frmXml.mod_list;
								                                 if (mod_list){
								                                 	document.frmXml.ok_action.disabled = false;
								                                 } else {
								                                 	document.frmXml.ok_action.disabled = true;
								                                 }
							                                }
						                             }
						                          }
			req.open("GET", url, true);
			req.send();
		}
		
		function returnSelMod(frm) {
		  
		   var mod_id_list = document.getElementsByName('mod_list');
		   var mod_title_list = document.getElementsByName('mod_title')
			is_multiple = frm.multiple.value;
			sel_type = frm.sel_type.value;
			if(mod_id_list != null && mod_id_list.length>0) {
			   var had_sel = false;
			   for(i = 0; i < mod_id_list.length; i++) {
					if(mod_id_list[i].checked) {
						had_sel = true;
                  }
               }
              if(!had_sel){
               Dialog.alert(wb_msg_had_not_sel_mod);
               return;
             }

			    var e = document.frmXml.search_result.options[document.frmXml.search_result.selectedIndex];
			    var itm_id =e.value;
			    var cos_info =e.text;
			    if(is_multiple == 'true') {
					if(sel_type == 'import_mod') {
					   frm.sel_mod_id_list.value = '';
					   for(i = 0; i < mod_id_list.length; i++) {
							if(mod_id_list[i].checked) {
								frm.sel_mod_id_list.value = frm.sel_mod_id_list.value + '~' + mod_id_list[i].value
                        }
                    }
					 }else{
						var parent_field = window.opener.document.getElementById(frm.re_field_name.value)
				       window.opener.remove_all(parent_field, cos_info);
						for(i = 0; i < mod_id_list.length; i++) {
							if(mod_id_list[i].checked) {
								index = parent_field.options.length;
							   is_exit = 'false';
								mod_id = mod_id_list[i].value;
								mod_title = mod_title_list[i].value;
								for(j = 0; j < parent_field.options.length; j++) {
									if(parent_field.options[j].value == mod_id) {
										is_exit = 'true';
									}	
								}
								if(is_exit == 'false') {
									window.opener.getReValue(parent_field, index, mod_title, mod_id,itm_id);
								}
							}
						}
					} 
				}else{
					if(sel_type == 'import_mod') {
					      frm.sel_mod_id_list.value = '';
						   for(i = 0; i < mod_id_list.length; i++) {
								if(mod_id_list[i].checked) {
									frm.sel_mod_id_list.value =  mod_id_list[i].value
									break;
	                    }
	                 }
					}else{
						var parent_field = window.opener.document.getElementById(frm.re_field_name.value);
						var parent_field_desc = window.opener.document.getElementById(frm.re_field_name.value + '_desc');
						 window.opener.remove_function(parent_field, parent_field_desc, cos_info);
						for(i = 0; i < mod_id_list.length; i++) {
							if(mod_id_list[i].checked) {
							   	window.opener.getReValue(parent_field, parent_field_desc, mod_title_list[i].value,  mod_id_list[i].value, itm_id);
								}
							}
					}			
				}
			}
			if (sel_type == 'import_mod') {
				frm.clo_src_itm_id.value = frm.search_result.value;
				frm.clo_tag_cos_res_id.value = gen_get_url_param('course_id');
				frm.action = wb_utils_servlet_url + '?cmd=clone_mod';
				frm.cmd.value = 'clone_mod';
				var url = window.parent.location.href;
				frm.url_success.value = "javascript:window.parent.location.reload('"+url+"')";
				
				frm.url_failure.value = course_lst.view_info_url(frm.clo_tag_cos_res_id.value);
				frm.method = 'post';
				frm.submit();
			}
			else{
				window.opener.closeWin();
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
					<select class="wzb-form-select" name="search_type">
						<option value="ct_cc" selected="selected">
							<xsl:value-of select="$lab_cct"/>
						</option>
						<option value="cc">
							<xsl:value-of select="$lab_cc"/>
						</option>
						<option value="ct">
							<xsl:value-of select="$lab_ct"/>
						</option>
						<option value="mt">
							<xsl:value-of select="$lab_mt"/>
						</option>
					</select>&#160;
					<input type="text" size="11" name="title_code" class="wzb-inputText" style="width:130px;" onKeyPress="returnResult(frmXml,event.keyCode)"/>
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
					<select onChange="setModule(frmXml,this)" style="width:400px;height:200px;" class="Select" size="6" name="search_result" disabled="disabled">
						<option value="0">
							<xsl:value-of select="$lab_no_result"/>
						</option>
					</select>
					<br/>
					<input type="radio" name="mod_status" value="" id="mod_status_all" disabled="true" checked="checked" onclick="setModule(frmXml,frmXml.search_result)">
						<label for="mod_status_all">
							<xsl:value-of select="$lab_all"/>
						</label>
					</input>&#160;
					<input type="radio" name="mod_status" value="ON" id="mod_status_on" disabled="true" onclick="setModule( frmXml,frmXml.search_result )">
						<label for="mod_status_on">
							<xsl:value-of select="$lab_on"/>
						</label>
					</input>&#160;
					<input type="radio" name="mod_status" value="OFF" id="mod_status_off" disabled="true" onclick="setModule(frmXml, frmXml.search_result)">
						<label for="mod_status_off">
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
		<div id="module_list">
			<script language="JavaScript">
			defaultHTML ();
			</script>
		</div>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_ok"/>
				<xsl:with-param name="wb_gen_btn_href">returnSelMod(frmXml)</xsl:with-param>
				<xsl:with-param name="field_name">ok_action</xsl:with-param>
			</xsl:call-template>
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
