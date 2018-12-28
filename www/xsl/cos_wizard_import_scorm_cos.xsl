<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	
	<xsl:import href="utils/wb_ui_space.xsl"/>

	<xsl:import href="utils/tst_ins_upd_utils.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="share/wb_module_type_const.xsl"/>
	<xsl:import href="utils/wb_gen_input_file.xsl"/>
	
	<xsl:output indent="yes"/>
	
	<xsl:variable name="url">javascript:parent.location.reload();</xsl:variable>
	<xsl:variable name="tpl_type"/>
		<!--<xsl:choose>
			<xsl:when test="user/is_inner">
				<xsl:text>javascript:window.opener.location.reload();</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>javascript:window.close();window.opener.location.reload()</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>-->
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_cos_wiz">製作課程單元</xsl:with-param>
			<xsl:with-param name="lab_step_inst">第二步:匯入課程</xsl:with-param>
			<xsl:with-param name="lab_file_crs">清單檔案</xsl:with-param>
			<xsl:with-param name="lab_required">注意: 標有*的必須填寫。</xsl:with-param>
			<xsl:with-param name="lab_from_resource">選取自資源庫</xsl:with-param>
			<xsl:with-param name="lab_source">來源</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_finish">完成</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_file_zip">壓縮文件</xsl:with-param>
			<xsl:with-param name="lab_sco_ver">請選擇Scorm版本</xsl:with-param>
			<xsl:with-param name="lab_sco_ver_1.2">1.2</xsl:with-param>
			<xsl:with-param name="lab_sco_ver_2004">2004</xsl:with-param>
			<xsl:with-param name="lab_sco_ver_notes">請和您的課件供應商確定該課件的版本，選擇不正確的版本可能會導致課件無法播放。</xsl:with-param>
			<xsl:with-param name="lab_push_to_mobile">發佈到移動端</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_text_style_sco">移動端顯示方式</xsl:with-param>
			<xsl:with-param name="lab_text_style_only_sco">豎屏</xsl:with-param>
			<xsl:with-param name="lab_text_style_many_sco">橫屏</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_cos_wiz">制作课程单元</xsl:with-param>
			<xsl:with-param name="lab_step_inst">第二步：汇入课程</xsl:with-param>
			<xsl:with-param name="lab_file_crs">清单文件</xsl:with-param>
			<xsl:with-param name="lab_required">注意：标有*的必须填写。</xsl:with-param>
			<xsl:with-param name="lab_from_resource">选取自资源库</xsl:with-param>
			<xsl:with-param name="lab_source">来源</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_finish">完成</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_file_zip">压缩文件</xsl:with-param>
			<xsl:with-param name="lab_sco_ver">请选择Scorm版本</xsl:with-param>
			<xsl:with-param name="lab_sco_ver_1.2">1.2</xsl:with-param>
			<xsl:with-param name="lab_sco_ver_2004">2004</xsl:with-param>
			<xsl:with-param name="lab_sco_ver_notes">请和您的课件供应商确定该课件的版本，选择不正确的版本可能会导致课件无法播放。</xsl:with-param>
			<xsl:with-param name="lab_push_to_mobile">发布到移动端</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_text_style_sco">移动端显示方式</xsl:with-param>
			<xsl:with-param name="lab_text_style_only_sco">竖屏</xsl:with-param>
			<xsl:with-param name="lab_text_style_many_sco">横屏</xsl:with-param>

		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_cos_wiz">Design learning module</xsl:with-param>
			<xsl:with-param name="lab_step_inst">Step 2: import course</xsl:with-param>
			<xsl:with-param name="lab_file_crs">Manifest file</xsl:with-param>
			<xsl:with-param name="lab_required">* Indicates the field is required</xsl:with-param>
			<xsl:with-param name="lab_from_resource">From learning resource management</xsl:with-param>
			<xsl:with-param name="lab_source">Source</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_finish">Finish</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">close</xsl:with-param>
			<xsl:with-param name="lab_file_zip">Compressed files</xsl:with-param>
			<xsl:with-param name="lab_sco_ver">Please choose <b>scorm</b> version</xsl:with-param>
			<xsl:with-param name="lab_sco_ver_1.2">1.2</xsl:with-param>
			<xsl:with-param name="lab_sco_ver_2004">2004</xsl:with-param>
			<xsl:with-param name="lab_sco_ver_notes">Please confirm the right version with your courseware provider, for the courseware wont't be played with wrong version.</xsl:with-param>
			<xsl:with-param name="lab_push_to_mobile">Publish to mobile</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
			<xsl:with-param name="lab_text_style_sco">Orientation of mobile learning</xsl:with-param>
			<xsl:with-param name="lab_text_style_only_sco">Portrait</xsl:with-param>
			<xsl:with-param name="lab_text_style_many_sco">Landscape</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_cos_wiz"/>
		<xsl:param name="lab_step_inst"/>
		<xsl:param name="lab_file_crs"/>
		<xsl:param name="lab_required"/>
		<xsl:param name="lab_from_resource"/>
		<xsl:param name="lab_source"/>
		<xsl:param name="lab_g_form_btn_finish"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_file_zip"/>
		<xsl:param name="lab_sco_ver"/>
		<xsl:param name="lab_sco_ver_1.2"/>
		<xsl:param name="lab_sco_ver_2004"/>
		<xsl:param name="lab_sco_ver_notes"/>
		<xsl:param name="lab_push_to_mobile"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		
		<xsl:param name="lab_text_style_sco"/>
		<xsl:param name="lab_text_style_only_sco"/>
		<xsl:param name="lab_text_style_many_sco"/>
		
		
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<style>			  
				.prog-border {
					height: 15px;
					width: 205px;
					background: #fff;
					border: 1px solid #000;				  
					margin: 0;
					padding: 0;
				}
				.prog-bar {
					height: 11px;
					margin: 2px;
					padding: 0px;
					background: #178399;
					font-size: 10pt;
				}
			</style>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_cos_wizard.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"></script>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_resource.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_upload_util.js"/>
			<script language="JavaScript" type="text/javascript" src="../static/js/layer/layer.js"/>
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			
			<!--alert样式  -->
			 <link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			<script language="JavaScript"><![CDATA[
			wiz = new wbCosWizard
			cos = new wbCourse
			res = new wbResource
			course_lst = new wbCourse
			tpl_type = 'SCORM'
			tpl_subtype = 'RES_SCO'
			var cookie_course_id = getUrlParam('cos_id');
			if (cookie_course_id <= 0) {
				cookie_course_id = wb_utils_get_cookie('course_id');
			}
			function status(){
				return false;
			}
			function add_cancel(){
				if(window.document["gen_btn_cancel0"]){
					window.document["gen_btn_cancel0"].width = 0;
					window.document["gen_btn_cancel0"].height = 0;
				}
					window.parent.cancelAdd();
					window.location.href = course_lst.view_info_url(cookie_course_id);
			}
			/*
			var updater = null;
	  function startStatusCheck(frm, lang)
	  {
	  	if(!_wbCosWizardCheckScormZipFile(frm, lang)){
	  		return false;
	  	}
	  	$('submitButton').disabled = true;
	  	$('cancelButton').disabled = true;
	  	frm.src_type[0].disabled = true;
	  	frm.src_type[1].disabled = true;
	  	frm.src_type[2].disabled = true;
	  	
	    // 设置上传按钮为不可用状态，避免多次提交
	    // new Ajax.Updater('upload_status',
	    // '/servlet/scoupload',
	    // {asynchronous:true, method: 'get', parameters: 'cmd=rm_status',
		// onFailure: reportError});
	    // 创建周期性发送请求的Ajax对象
	    updater = new Ajax.PeriodicalUpdater(
	                                'upload_status',
	                                '/servlet/scoupload',
	                                {asynchronous:true, frequency:1, method: 'get', parameters: 'cmd=get_status&t='+new Date(), onFailure: reportError});
	    return true;
	  }
	  // 出错时处理方法
	  function reportError(request)
	  {
	    $('submitButton').disabled = false;
			$('cancelButton').disabled = false;
	    $('upload_status').innerHTML = '<div class="error"><b>Error communicating with server. Please try again.</b></div>';
	  }
		// 上传完毕后,取消周期性获取进度状态，将最终的状态显示在客户端
	  function killUpdate(message)
	  {
	    $('submitButton').disabled = true;
	    $('cancelButton').disabled = false;
	    if(null!=updater)
	    {
	    	// 停止刷新获取进度
	    	updater.stop();
	    }
	    if(message != '')// 如果有错误信息，则显示出来
	    {
	      // $('upload_status').innerHTML = '<div class="error"><b>Error
			// processing results: ' + message + '</b></div>';
	      var url = "../servlet/scoupload?cmd=finish_import&type=add_itm&code="+ message +"&url_success=]]><xsl:value-of select="$url"/><![CDATA[&is_inner=]]><xsl:value-of select="user/is_inner"/><![CDATA[";
		    window.location.href = url;
	    }
	    else// 如果没有错误信息
	    {
	      // 获取上传文件的完成状态，显示到客户端
	      new Ajax.Updater('upload_status',
	                     '/servlet/scoupload',
	                     {asynchronous:true, method: 'get', parameters: 'cmd=get_status', onFailure: reportError});
	      
	      var url = "../servlet/scoupload?cmd=finish_import&type=add_itm&cos_id="+cookie_course_id+"&url_success=]]><xsl:value-of select="$url"/><![CDATA[&is_inner=]]><xsl:value-of select="user/is_inner"/><![CDATA[";
	      window.location.href = url;
	    }
	  }
	  */
	  function change_btn(frm){
	  	if(frm.src_type){
	  		if (frm.src_type.length>=3 && frm.src_type[2].checked) {
	  			frm.aicc_crs.outerHTML = frm.aicc_crs.outerHTML;
	  			toggle_sco_ver(frm,true);
	  		}else if (frm.src_type[1].checked){
	  			frm.aicc_zip.outerHTML = frm.aicc_zip.outerHTML;
	  			toggle_sco_ver(frm,true);
	  		} else {
	  			toggle_sco_ver(frm,false);
	  		}
	  	}
	  }
	  function toggle_sco_ver(frm,flag) {
     	document.getElementById('sco_ver_chooser').style.display = (flag ? '' :'none');
		frm.sco_ver[0].disabled = !flag;
		frm.sco_ver[1].disabled = !flag;
	  }
	  /*
	  function stopUpload(){
	  	if(updater!=null)
	    {
	    	// 停止刷新获取进度
	    	updater.stop();
	    }
	    window.parent.cancelAdd();
		window.location.href = cos.view_info_url(cookie_course_id);

	  }
	  */
		]]></script>
			<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="wb_utils_gen_form_focus(document.frmXml);">
			<form name="frmXml" enctype="multipart/form-data" onsubmit="return status()">
				<input value="" name="rename" type="hidden"/>
				<input value="" name="cmd" type="hidden"/>
				<input value="" name="env" type="hidden"/>
				<input value="" name="module" type="hidden"/>
				<input value="" name="imsmanifest_file_name" type="hidden"/>
				<input value="" name="aicc_crs_filename" type="hidden"/>
				<input value="" name="cos_id" type="hidden"/>
				<input value="" name="src_res_id" type="hidden"/>
				<input type="hidden" name="url_failure" value="javascript:window.close()"/>
				<input type="hidden" name="url_success" value="{$url}"/>
				<input type="hidden" name="is_inner" value="{user/is_inner}"/>
				<!--<input type="hidden" name="url_success" value="javascript:window.close();window.opener.location.reload()"/>-->
				
				<!-- <xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_cos_wiz"/>
						<xsl:text>&#160;-&#160;</xsl:text>
						<script language="JavaScript">document.write(wiz.get_cos_title())</script>
					</xsl:with-param>
					<xsl:with-param name="width">100%</xsl:with-param>
				</xsl:call-template> -->
					
				<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="$lab_cos_wiz"/>
				</xsl:call-template>
				
				<xsl:if test="count(user/is_inner)= 0">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="$lab_step_inst"/>
						<xsl:with-param name="width">100%</xsl:with-param>
					</xsl:call-template>
				</xsl:if>
				<xsl:call-template name="wb_ui_line">
					<xsl:with-param name="width">100%</xsl:with-param>
				</xsl:call-template>
				<table>
					<xsl:call-template name="draw_pick_aicc_res">
						<xsl:with-param name="lab_source" select="$lab_source"/>
						<xsl:with-param name="lab_from_resource" select="$lab_from_resource"/>
					</xsl:call-template>
					<tr>
						<td class="wzb-form-label"/>
						<td class="wzb-form-control">
							<input type="radio" name="src_type" id="rdo_src_type_pick_from_file" value="" onclick="javascript:change_btn(document.frmXml)"/>&#160;
							<label for="rdo_src_type_pick_from_file">
								<xsl:value-of select="$lab_file_crs"/>
							</label>
								 
						  	<xsl:call-template name="wb_gen_input_file">
								<xsl:with-param name="name">aicc_crs</xsl:with-param>
								<xsl:with-param name="onchange">javascript:frmXml.src_type[1].checked=true;change_btn(document.frmXml);</xsl:with-param>
							 </xsl:call-template>
					         
						</td>
					</tr>
					<input type="hidden" size="20" name="cos_url_prefix" value=""/>
					<tr>
						<td class="wzb-form-label"/>
						<td class="wzb-form-control">
							<input type="radio" name="src_type" id="rdo_src_type_pick_from_zip_file" value="" onclick="javascript:change_btn(document.frmXml)"/>&#160;
							<label for="rdo_src_type_pick_from_zip_file">
								<xsl:value-of select="$lab_file_zip"/>
							</label>
							<xsl:call-template name="wb_gen_input_file">
								<xsl:with-param name="name">aicc_zip</xsl:with-param>
								<xsl:with-param name="onchange">javascript:frmXml.src_type[2].checked=true;change_btn(document.frmXml);</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$lab_push_to_mobile"/>：
						</td>
						<td class="wzb-form-control">
							<input type="radio" name="mod_mobile_ind" value="1" checked="checked"/>
								<xsl:value-of select="$lab_yes"/><br />
							<input type="radio" name="mod_mobile_ind" value="0"/>
								<xsl:value-of select="$lab_no"/>
						</td>
					</tr>
					 
					<tr>
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$lab_text_style_sco"/>：
						</td>
						<td class="wzb-form-control">
						
							<input type="hidden" name="text_style"/>
							<input type="radio" checked="checked"  name="mod_test_style" id="text_style_only_id" value="only"/>
			                <span class="Text">
								<label for ="text_style_only_id"><xsl:value-of select="$lab_text_style_only_sco"/></label>
							</span>
							<br/>
							
							<input type="radio" name="mod_test_style" id="text_style_many_id" value="many"/>
							<span class="Text">
							   <label for ="text_style_many_id"><xsl:value-of select="$lab_text_style_many_sco"/></label> 
			                </span>
			               
						</td>
						
					</tr>
					<tr id="sco_ver_chooser" style="display:none">
						<td valign="top" class="wzb-form-label"><span class="Text"><xsl:value-of select="$lab_sco_ver"/>：</span></td>
						<td class="wzb-form-control"><input type="radio" name="sco_ver" id="sco_ver_12" value="1.2" checked="checked"/>
							<label for="sco_ver12"><xsl:value-of select="$lab_sco_ver_1.2"/></label>
							<br/>
							<input type="radio" name="sco_ver" id="sco_ver2004" value="2004"/>
							<label for="sco_ver_2004"><xsl:value-of select="$lab_sco_ver_2004"/></label><br/><br/>
							<span class="Text" style="color:red"><xsl:value-of select="$lab_sco_ver_notes"/></span>
						 </td>
					</tr>
					<!-- 这个是隐藏的<ifame>作为表单提交后处理的后台目标-->
					<iframe id="target_upload" name="target_upload" src="" style="display: none"/>
					<tr>
						<td align="right" height="10"/>
						<td width="80%" height="10">
							<div id="upload_status"/>
						</td>
					</tr>
					<tr>
						<td align="right" height="10">
							<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
						</td>
						<td width="80%" height="10">
							<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
						</td>
					</tr>
				</table>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_finish"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:wiz.import_scorm_cos(document.frmXml,'<xsl:value-of select="$wb_lang"/>','true');
						</xsl:with-param>
						<xsl:with-param name="id">submitButton</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:add_cancel();</xsl:with-param>
						<xsl:with-param name="id">cancelButton</xsl:with-param>
					</xsl:call-template>
				</div>
			</form>
		</body>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_pick_aicc_res">
		<xsl:param name="lab_source"/>
		<xsl:param name="show_title">false</xsl:param>
		<xsl:param name="lab_from_resource"/>
		<xsl:param name="checked">true</xsl:param>
		<script language="JavaScript">			
			if(document.frmXml.src_type){
				if(document.frmXml.src_type.length){
					var src_type_pick_aicc_res_id = document.frmXml.src_type.length;
				}else{
					var src_type_pick_aicc_res_id = 1;
				}
			}else{
				var src_type_pick_aicc_res_id = 0;
			}
		</script>
		<tr>
			<td class="wzb-form-label" valign="top">
				<xsl:choose>
					<xsl:when test="$show_title = 'true'">
						<xsl:value-of select="$lab_source"/>
						<xsl:text>：</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td class="wzb-form-control">
				<label for="rdo_src_type_pick_aicc_res">
					<input type="radio" name="src_type" id="rdo_src_type_pick_aicc_res" value="" onclick="javascript:change_btn(document.frmXml)">
						<xsl:if test="$checked  = 'true'">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</input>&#160;
					<xsl:value-of select="$lab_from_resource"/>
					<xsl:text>&#160;</xsl:text>
				</label>
				<a href="javascript:document.frmXml.src_type[src_type_pick_aicc_res_id].checked=true;res.pick_res(tpl_type,'{$wb_lang}',cookie_course_id);">
					<img src="{$wb_img_path}ico_pick.gif" border="0" align="absmiddle"/>
				</a>
				<input type="hidden" name="source_type" value=""/>
				<input type="hidden" name="source_content" value=""/>
			</td>
		</tr>
		<tr>
			<td width="20%">
				<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
			</td>
			<td width="80%">
				<img name="src_type_img" src="{$wb_img_path}tp.gif" border="0" align="absmiddle"/>
				<span class="Text" id="src_display">
					<script type="text/javascript" language="JavaScript"><![CDATA[			if (document.layers)				document.write('<input type="text" size="20" name="src_type_display"  onfocus="if(this.value!= \'\'){window.open(this.value,\'_blank\')}"/>');]]></script>
				</span>
			</td>
		</tr>
	</xsl:template>
	
	<!--=========================================================-->
		
	<!--=========================================================-->
	
	
	
</xsl:stylesheet>
