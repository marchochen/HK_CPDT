<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/change_lowercase.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_input_file.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>	
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/poster_info">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="rpt_type" select="/poster_info/poster/rpt_type"/> 
	<xsl:variable name="lab_update" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '708')"/> 	
	<xsl:variable name="lab_poster_ad" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '798')"/> 	
	<xsl:variable name="lab_poster_mobile_ad" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_ftn2_MOBILE_POSTER_MAIN')"/> 	
	<xsl:variable name="lab_poster_mobile_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_MOBILE_POSTER_title')" />
	<xsl:variable name="lab_poster" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '799')"/> 	
	<xsl:variable name="lab_poster_file" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '800')"/> 
	<xsl:variable name="lab_poster_file_mobile" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '800_m')"/> 
	<xsl:variable name="lab_keep_media" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '801')"/> 	
	<xsl:variable name="lab_remove_media" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '802')"/> 	
	<xsl:variable name="lab_change_to" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '803')"/>
	<xsl:variable name="lab_poster_url" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '702')"/>
	<xsl:variable name="lab_poster_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '704')"/> 	
	<xsl:variable name="lab_poster_status_on" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '765')"/> 	
	<xsl:variable name="lab_poster_status_off" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '766')"/> 	
	<xsl:variable name="lab_g_form_btn_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '329')"/> 	
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/> 	
	<xsl:variable name="lab_ico_des" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_mobile_poster_des')"/> 
	<xsl:variable name="lab_ico_client" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_poster_des')"/>
	<xsl:variable name="lab_logo_client" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_logo_des')"/>
	<xsl:variable name="lab_logo_mobile1" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_logo_mobile_des1')"/>
	<xsl:variable name="lab_logo_mobile2" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_logo_mobile_des2')"/>
	<xsl:variable name="lab_login_mobile" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_login_mobile_des')"/>
	<xsl:variable name="lab_logo_mobile_login" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_logo_mobile_login')"/>
	<xsl:variable name="lab_logo_mobile_about" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_logo_mobile_about')"/>
	<xsl:variable name="lab_mobile_login_logo" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_mobile_login_logo')"/>
	<xsl:variable name="lab_mobile_about_logo" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_mobile_about_logo')"/>
	<xsl:variable name="lab_index_welcome" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_index_welcome')"/>
	<xsl:variable name="lab_login_bg" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding,'LN332')"/>
	<xsl:variable name="lab_logo" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lan4')"/> 	
	<xsl:variable name="lab_lang_cn">
		<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '892')"/>
		<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lan6')"/>
	</xsl:variable>
	<xsl:variable name="lab_lang_hk">
		<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '893')"/>
		<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lan6')"/>
	</xsl:variable>
	<xsl:variable name="lab_lang_us">
		<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '891')"/>
		<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lan6')"/>
	</xsl:variable>
	<xsl:variable name="lab_logo_file_type" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lan5')"/>
    <xsl:variable name="lab_wizbank_default" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN012')"/>
    <xsl:variable name="lab_login_bg_picture" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN009')"/>
    <xsl:variable name="lab_login_bg_size" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_login_bg_size')"/>
    <xsl:variable name="lab_poster_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_poster_image')"/>
    <xsl:variable name="lab_welcome_tmp" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_welcome_tmp')"/>
    <xsl:variable name="lab_welcome_mobile" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_welcome_mobile')"/> 		
	<xsl:variable name="lab_welcome_pc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_welcome_pc')"/>
	<xsl:variable name="lab_show_header" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_show_header')"/>
	<xsl:variable name="lab_show_footer" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_show_footer')"/>
	<xsl:variable name="lab_page_title">
		<xsl:choose>
			<xsl:when test="$rpt_type = 'FTN_AMD_MOBILE_POSTER_MAIN'">
				<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_ftn_MOBILE_POSTER_MAIN')"></xsl:value-of>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_ftn_POSTER_MAIN')"></xsl:value-of>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_login_bg_vod_img" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_login_bg_vod_img')"/> 
	<xsl:variable name="lab_login_bg_vod" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_login_bg_vod')"/> 
	<xsl:variable name="lab_vod_poster" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_vod_poster')"/> 
	<xsl:variable name="lab_poster_vod_file" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_poster_vod_file')"/>
	<xsl:variable name="lab_bg_poster_type_img" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_bg_poster_type_img')"/> 
	<xsl:variable name="lab_bg_poster_type_vod" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_bg_poster_type_vod')"/>
	<!-- ============================================================= -->
	<xsl:variable name="site_name" select="/poster_info/meta/cur_usr/@root_id"/>
	<xsl:variable name="media_file_path" select="concat('../poster/', $site_name, '/', /poster_info/poster/media_file/text())"/>
	<xsl:variable name="media_file_path1" select="concat('../poster/', $site_name, '/', /poster_info/poster/media_file_1/text())"/>	
	<xsl:variable name="media_file_path2" select="concat('../poster/', $site_name, '/', /poster_info/poster/media_file_2/text())"/>	
	<xsl:variable name="media_file_path3" select="concat('../poster/', $site_name, '/', /poster_info/poster/media_file_3/text())"/>	
	<xsl:variable name="media_file_path4" select="concat('../poster/', $site_name, '/', /poster_info/poster/media_file_4/text())"/>		
	<xsl:variable name="logo_file_cn_path" select="concat('../poster/', $site_name, '/', /poster_info/poster/logo_file_cn/text())"/>
	<xsl:variable name="logo_file_hk_path" select="concat('../poster/', $site_name, '/', /poster_info/poster/logo_file_hk/text())"/>
	<xsl:variable name="logo_file_us_path" select="concat('../poster/', $site_name, '/', /poster_info/poster/logo_file_us/text())"/>
	<xsl:variable name="login_bg_file1_path" select="concat('../poster/loginPage/',$site_name,'/', /poster_info/poster/login_bg_file1/text())"/>
	<xsl:variable name="login_bg_file2_path" select="concat('../poster/loginPage/',$site_name,'/', /poster_info/poster/login_bg_file2/text())"/>
	<xsl:variable name="login_bg_file3_path" select="concat('../poster/loginPage/',$site_name,'/', /poster_info/poster/login_bg_file3/text())"/>
	<xsl:variable name="login_bg_file4_path" select="concat('../poster/loginPage/',$site_name,'/', /poster_info/poster/login_bg_file4/text())"/>
	<xsl:variable name="login_bg_file5_path" select="concat('../poster/loginPage/',$site_name,'/', /poster_info/poster/login_bg_file5/text())"/>
	<xsl:variable name="login_bg_video_path" select="concat('../poster/loginPage/',$site_name,'/', /poster_info/poster/login_bg_video/text())"/>
	<xsl:variable name="guide_file1_path" select="concat('../poster/guide/',$site_name,'/', /poster_info/poster/guide_file1/text())"/>
	<xsl:variable name="guide_file2_path" select="concat('../poster/guide/',$site_name,'/', /poster_info/poster/guide_file2/text())"/>
	<xsl:variable name="guide_file3_path" select="concat('../poster/guide/',$site_name,'/', /poster_info/poster/guide_file3/text())"/>
	<xsl:variable name="lab_default_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'poster_details_1187')"/>
	<xsl:variable name="lab_default_media" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'poster_details_1188')"/>
	<xsl:variable name="label_core_system_setting_138" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_138')"/>
	<xsl:variable name="label_core_system_setting_139" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_139')"/>
	<xsl:variable name="label_core_system_setting_141" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_141')"/>
	<xsl:variable name="label_core_system_setting_142" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_142')"/>
	<xsl:variable name="label_core_system_setting_143" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_143')"/>
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_media.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_poster.js"/>
			<script language="JavaScript" src="../static/js/cwn_utils.js"/>
			<script language="JavaScript" src="../static/js/i18n/{$wb_cur_lang}/label_ss_{$wb_cur_lang}.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			
				$(document).ready(function(){  
					
					var tabName = getUrlParam('tabId');
					if("undefined" == tabName){
						return;
					}
					if(tabName == ""){
						tabName = "logo";
					}
					var $_a = $("[tabName='"+tabName+"']")
					
    				showTab($_a[0],tabName);
			   });
			
				var ste_poster = new wbPoster;
				var tabVal = '';
				//选项卡的ID，加一个选项卡就要把id加到这个数组里
				var ids=['logo','poster','loginBg','guide','manageText'];
			    function showTab(obj,tabId){
			   	   tabVal = tabId;
			       var style=obj.parentNode.className;
			       if(style.indexOf('active')==-1){		                    
			          for(var i=0;i<ids.length;i++){
			             if(ids[i]!=tabId){
			                document.getElementById(ids[i]).style.display='none';
			              }
			          }			         
			        document.getElementById(tabId).style.display='';			          
			        var divs=document.getElementById('tab_menu').getElementsByTagName('li');
			        for(var i=0;i<divs.length;i++){
			          	if(divs[i].className.indexOf('active')!=-1){
			          		divs[i].className='';
			          		break;
			          	}		
		          	}	  
			        obj.parentNode.className='active ';  
			        
			       }
			    }
			    
			    function changeTab(obj,tabId){
			    	var url =  wb_utils_invoke_servlet('cmd', 'get_poster', 'stylesheet', 'poster_details.xsl', 'rpt_type', ']]><xsl:value-of select="$rpt_type" /><![CDATA[','tabId',tabId);
			        if (url != null) {
						if (window.parent == null) {
							window.location.href =  url;
						} else {
							window.parent.location.href =  url;
						}
					}
			    }
			    
			    
			    function get_check(divnode)
			    {
			     var radio=divnode.previousSibling.previousSibling.previousSibling;
			     radio.checked="checked";
			    }
			    
			]]></script>
			<style>
			.div_tab{
			}
			.div_tab div{
			 
			  height: 24px;
			  float:left;
			  text-align:center;
			  border-right:1px solid;
			 
			}
			.div_tab div:hover{
			    background-color: #d8d8d8;			    
			    color: #000000;
                font: bold 8pt Verdana,Geneva,Arial,Helvetica,sans-serif;
			}
			.div_tab a{
			  display:inline-block;
			  margin:4px;			  
			  color: #8a8a8a;
			  font: bold 8pt Verdana,Geneva,Arial,Helvetica,sans-serif;
			  text-decoration:none;		
			}
			.div_tab a:hover{
			    color: #000000;
			}
			
			#tab_menu tr td{
			   padding:0px;
			   border-top:1px solid black;
			   border-bottom:1px solid black;			  
			}
			.show {
			  background-color: #d8d8d8;				 
              font: bold 8pt Verdana,Geneva,Arial,Helvetica,sans-serif;			  
			}
			.show a{
			  color: #000000;
			}
			.TabHide{
			    background-color: #efefef;
			}
			</style>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="content"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<form name="frmXml" enctype="multipart/form-data">
			<input name="cmd" type="hidden"/>
			<input name="rpt_type" type="hidden"/>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>	 
            <input type="hidden" name="lab_poster_url" value="{$lab_poster_url}"></input>            
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_POSTER_MAIN</xsl:with-param>
                <xsl:with-param name="page_title">
				  <xsl:call-template name="get_lab">
					<xsl:with-param name="lab_title">
					<xsl:if test="$rpt_type='FTN_AMD_POSTER_MAIN'">global.FTN_AMD_POSTER_MAIN</xsl:if>
					<xsl:if test="$rpt_type='FTN_AMD_MOBILE_POSTER_MAIN'">global.FTN_AMD_MOBILE_POSTER_MAIN</xsl:if>
					</xsl:with-param>
				  </xsl:call-template>
			    </xsl:with-param>				
			</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_page_title" />
				</xsl:call-template>

           <ul   id='tab_menu' class="nav nav-tabs page-tabs" role="tablist">		
				
				<li role="presentation" class="active">
						<a tabName="logo" href="javascript:void(0)"  onclick="changeTab(this,'logo');" aria-controls="faq" role="tab" data-toggle="tab"><xsl:value-of select="$lab_logo" /></a>
				</li>											
				<li role="presentation" class="">					
					<a tabName="poster" href="javascript:void(0)"  onclick="changeTab(this,'poster');" aria-controls="faq" role="tab" data-toggle="tab">									
						<xsl:value-of select="$lab_poster_mobile_title" />
					</a>
				</li>
				<xsl:if test="$rpt_type='FTN_AMD_POSTER_MAIN'">
					<li role="presentation" class="">
					     <a tabName="loginBg" href="javascript:void(0)" name="loginBg" onclick="changeTab(this,'loginBg');" aria-controls="faq" role="tab" data-toggle="tab">
					        <xsl:value-of select="$lab_login_bg"/>
						</a>
					</li>
				</xsl:if>
				<xsl:if test="false and $rpt_type='FTN_AMD_MOBILE_POSTER_MAIN'">
				<li role="presentation" class="">
				    <a tabName="guide" href="javascript:void(0)"  onclick="changeTab(this,'guide');" aria-controls="faq" role="tab" data-toggle="tab">
				        <xsl:value-of select="$lab_poster_image"/>
					</a>
				</li>
				</xsl:if>
				<xsl:if test="$rpt_type='FTN_AMD_POSTER_MAIN'">
					<li role="presentation" class="">
						<a tabName="manageText" href="javascript:void(0);" name="manageText" onclick="changeTab(this,'manageText');" aria-controls="faq" role="tab" data-toggle="tab">
							<xsl:value-of select="$lab_index_welcome"/>
						</a>
					</li>
				</xsl:if>
		
			</ul>

			<table id="tab_table">
			    <tr id="logo">
					<td>
		            	<xsl:call-template name="logo"/>
		        	</td>
				</tr>
				<tr id="poster" style='display:none;'>
					<td>				
			            <xsl:apply-templates select="poster"/>
					</td>
				</tr>
				<tr id="loginBg" style="display:none;">
				     <td>
				        <xsl:call-template name="loginBg" />
				     </td>
				</tr>				
				<tr id="guide"  style="display:none;">
					<td>
						<xsl:call-template name="guide" />
					</td>
				</tr>
				<tr id="manageText" style="display:none;">
				 	<td>
				 		<xsl:call-template name="manageText"/>
				 	</td>
				</tr>
			</table>
			<xsl:call-template name="submit"/>			
		</form>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="poster">
		<table>
			<xsl:call-template name="poster_list">
				<xsl:with-param name="media_file" select="media_file"/>
				<xsl:with-param name="url" select="url"/>
				<xsl:with-param name="status" select="status"/>
				<xsl:with-param name="file_name">sp_media_file</xsl:with-param>
				<xsl:with-param name="rdo_name">rdo_sp_media01</xsl:with-param>
				<xsl:with-param name="input_file_name">tmp_sp_media01</xsl:with-param>
				<xsl:with-param name="input_name_url">sp_url</xsl:with-param>
				<xsl:with-param name="rdo_status_name">sp_status</xsl:with-param>
				<xsl:with-param name="media_file_path" select="$media_file_path"/>
				<xsl:with-param name="number" > 1</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="poster_list">
				<xsl:with-param name="media_file" select="media_file_1"/>
				<xsl:with-param name="url" select="url_1"/>
				<xsl:with-param name="status" select="status_1"/>
				<xsl:with-param name="file_name">sp_media_file_1</xsl:with-param>
				<xsl:with-param name="rdo_name">rdo_sp_media01_1</xsl:with-param>
				<xsl:with-param name="input_file_name">tmp_sp_media01_1</xsl:with-param>
				<xsl:with-param name="input_name_url">sp_url_1</xsl:with-param>
				<xsl:with-param name="rdo_status_name">sp_status_1</xsl:with-param>
				<xsl:with-param name="media_file_path" select="$media_file_path1"/>
				<xsl:with-param name="number" > 2</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="poster_list">
				<xsl:with-param name="media_file" select="media_file_2"/>
				<xsl:with-param name="url" select="url_2"/>
				<xsl:with-param name="status" select="status_2"/>
				<xsl:with-param name="file_name">sp_media_file_2</xsl:with-param>
				<xsl:with-param name="rdo_name">rdo_sp_media01_2</xsl:with-param>
				<xsl:with-param name="input_file_name">tmp_sp_media01_2</xsl:with-param>
				<xsl:with-param name="input_name_url">sp_url_2</xsl:with-param>
				<xsl:with-param name="rdo_status_name">sp_status_2</xsl:with-param>
				<xsl:with-param name="media_file_path" select="$media_file_path2"/>
				<xsl:with-param name="number" > 3</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="poster_list">
				<xsl:with-param name="media_file" select="media_file_3"/>
				<xsl:with-param name="url" select="url_3"/>
				<xsl:with-param name="status" select="status_3"/>
				<xsl:with-param name="file_name">sp_media_file_3</xsl:with-param>
				<xsl:with-param name="rdo_name">rdo_sp_media01_3</xsl:with-param>
				<xsl:with-param name="input_file_name">tmp_sp_media01_3</xsl:with-param>
				<xsl:with-param name="input_name_url">sp_url_3</xsl:with-param>
				<xsl:with-param name="rdo_status_name">sp_status_3</xsl:with-param>
				<xsl:with-param name="media_file_path" select="$media_file_path3"/>
				<xsl:with-param name="number" > 4</xsl:with-param>
			</xsl:call-template>
		</table>
		
	</xsl:template>
	<xsl:template name='submit'>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:ste_poster.poster_send_form(document.frmXml,  '<xsl:value-of select="$wb_lang"/>', '<xsl:value-of select="$rpt_type"/>')</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:wb_utils_gen_home(true);</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	
	<!-- ========================================================= -->	
	<xsl:template name="flash">
		<xsl:param name="media_file_path"/>
		<OBJECT name="{$site_name}_movie" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0" width="165" height="165">
			<PARAM NAME="movie" VALUE="{$media_file_path}"/>
			<EMBED src="{$media_file_path}" TYPE="application/x-shockwave-flash" PLUGINSPAGE="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash" width="165" height="165"/>
		</OBJECT>
	</xsl:template>
	<xsl:template name="poster_list">
		<xsl:param name="media_file" />
		<xsl:param name="url"/>
		<xsl:param name="status" />
		<xsl:param name="file_name"/>
		<xsl:param name="rdo_name"/>
		<xsl:param name="input_file_name"/>
		<xsl:param name="input_name_url"/>
		<xsl:param name="rdo_status_name"/>
		<xsl:param name="media_file_path" />
		<xsl:param name="number"/>
		<!--广告-->
			<tr>
				<td class="wzb-form-label" valign="top">				
				</td>
				<td class="wzb-form-control">
					<label class="wzb-form-weight"> <xsl:value-of select="$lab_poster"/><xsl:value-of select="$number"/></label>
					<input type="hidden" name="lab_poster" value="{$lab_poster}"></input>
					
					<xsl:choose>
						<xsl:when test="not($media_file) or $media_file=''">
						<div style="margin:0 0 10px 18px;" >
							<xsl:call-template name="wb_gen_input_file">
								<xsl:with-param name="name" select="$file_name"/>
							</xsl:call-template>
						</div>
							(
                    		<xsl:choose>
								<xsl:when test="rpt_type='FTN_AMD_MOBILE_POSTER_MAIN'">
									<xsl:value-of select="$lab_ico_des"/>,
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_ico_client"/>,
								</xsl:otherwise>
							</xsl:choose>
							<xsl:if test="$rpt_type='FTN_AMD_POSTER_MAIN'">
								<xsl:value-of select="$lab_poster_file"/>
							</xsl:if>
							<xsl:if test="$rpt_type='FTN_AMD_MOBILE_POSTER_MAIN'">
								<xsl:value-of select="$lab_poster_file_mobile"/>
							</xsl:if>
							)
						</xsl:when>
						<xsl:otherwise>
							<xsl:variable name="file_ext">
								<xsl:call-template name="change_lowercase">
									<xsl:with-param name="input_value">
										<xsl:value-of select="substring-after($media_file,'.')"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:variable>
							<tr>
								<td nowrap="nowrap" valign="top" align="center" width="30%">
						
									<xsl:choose>
										<xsl:when test="$file_ext = 'swf'">
											<xsl:call-template name="flash">
												<xsl:with-param name="media_file_path" select="$media_file_path"/>
											</xsl:call-template>
										</xsl:when>
										<xsl:otherwise>
											<xsl:choose>
												<xsl:when test="$url='http://'"><img src="{$media_file_path}" width="200" height="100"/>
												</xsl:when>
												<xsl:when test="$url!= ''">
													<!-- 把点击图片的链接去掉 -->
													<!-- <a href="{$url}" target="_blank"> --><img src="{$media_file_path}" width="200" height="100" border="0" /><!-- </a> -->
												</xsl:when>
												<xsl:otherwise>
													<img src="{$media_file_path}" width="200" height="100" border="0" />
												</xsl:otherwise>
											</xsl:choose>
										</xsl:otherwise>
									</xsl:choose>
								</td>
								<td width="70%" valign="top">
									<input type="radio"  name="{$rdo_name}" value="1" checked="checked" onClick="this.form.{$file_name}.value='{$media_file}';"/>
									<label class="label-weight">
										<xsl:value-of select="$lab_keep_media"/>
									</label>
									<br/>
									<input type="radio"  name="{$rdo_name}" value="2" onClick="this.form.{$file_name}.value=''; "/>
									<label class="label-weight">
										<xsl:value-of select="$lab_remove_media"/>
									</label>
									<br/>
									<input type="radio"  name="{$rdo_name}" value="3" onclick="this.form.{$input_file_name}.disabled = false"/>
									<label class="label-weight">
										<xsl:value-of select="$lab_change_to"/>：
									</label>
									<div style="margin:0 0 10px 18px;" onClick="get_check(this)">
										<xsl:call-template name="wb_gen_input_file">	
											<xsl:with-param name="name" select="$input_file_name"/>
											<xsl:with-param name="onBlur">doAutoCheck(this,this.form.{$rdo_name}[2], this.form.{$rdo_name}[0], this.form.{$file_name})</xsl:with-param>
											<xsl:with-param name="onfocus">this.form.{$rdo_name}[2].checked=true</xsl:with-param>
											<!-- <xsl:with-param name="disabled">disabled</xsl:with-param>  -->
										</xsl:call-template>
									</div>
									(
									<xsl:choose>
									<xsl:when test="rpt_type='FTN_AMD_MOBILE_POSTER_MAIN'">
										<xsl:value-of select="$lab_ico_des"/>,
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_ico_client"/>,
									</xsl:otherwise>
									</xsl:choose>
									<xsl:if test="$rpt_type='FTN_AMD_POSTER_MAIN'">
										<xsl:value-of select="$lab_poster_file"/>
									</xsl:if>
									<xsl:if test="$rpt_type='FTN_AMD_MOBILE_POSTER_MAIN'">
										<xsl:value-of select="$lab_poster_file_mobile"/>
									</xsl:if>
									)
									<input type="hidden" name="{$file_name}" value="{$media_file}"/>
								</td>
							</tr>						
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<!--url-->
			<xsl:if test="$rpt_type='FTN_AMD_POSTER_MAIN'">
			<tr>
				<td class="wzb-form-label">
					
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="$lab_poster_url"/>：
					<xsl:choose>
						<xsl:when test="$url != ''">
							<input type="Text" style="width:450px;" maxlength="255" name="{$input_name_url}" value="{$url}" class="wzb-inputText"/>						  </xsl:when>
						<xsl:otherwise>
							<input type="Text" style="width:450px;" maxlength="255" name="{$input_name_url}" value="" class="wzb-inputText"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			</xsl:if>
			<xsl:if test="$rpt_type='FTN_AMD_MOBILE_POSTER_MAIN'">
			<tr style="display:none">
				<td class="wzb-form-label">
					
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="$lab_poster_url"/>：
					<xsl:choose>
						<xsl:when test="$url != ''">
							<input type="Text" style="width:450px;" maxlength="255" name="{$input_name_url}" value="{$url}" class="wzb-inputText"/>						  </xsl:when>
						<xsl:otherwise>
							<input type="Text" style="width:450px;" maxlength="255" name="{$input_name_url}" value="" class="wzb-inputText"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			</xsl:if>
			<!--状态-->
			<tr>
				<td class="wzb-form-label">
					
				</td>
				<td class="wzb-form-control">
				<xsl:value-of select="$lab_poster_status"/>：
					<label >
						<input type="radio"  name="{$rdo_status_name}" value="ON">
							<xsl:if test="not($status) or $status = 'ON' or $status = ''">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<xsl:value-of select="$lab_poster_status_on"/>
					</label>
					<label class="margin-left10">
						<input  type="radio" name="{$rdo_status_name}" value="OFF">
							<xsl:if test="$status = 'OFF' ">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<xsl:value-of select="$lab_poster_status_off"/>
					</label>
				</td>
			</tr>
	</xsl:template>
	<xsl:template name="logo">
		<table>
			<xsl:if test="$rpt_type='FTN_AMD_POSTER_MAIN'">
				<xsl:call-template name="logo_template">
					<xsl:with-param name="logo_edition" select="$lab_lang_cn"/>
					<xsl:with-param name="logo_file" select="/poster_info/poster/logo_file_cn/text()"/>
					<xsl:with-param name="file_name">sp_logo_file_cn</xsl:with-param>
					<xsl:with-param name="rdo_name">rdo_sp_logo_cn</xsl:with-param>
					<xsl:with-param name="input_file_name">tmp_sp_logo_cn</xsl:with-param>
					<xsl:with-param name="logo_file_path" select="$logo_file_cn_path"/>
					<xsl:with-param name="logo_size_desc" select="$lab_logo_client"/>					
				</xsl:call-template>
				<xsl:call-template name="logo_template">
					<xsl:with-param name="logo_edition" select="$lab_lang_hk"/>
					<xsl:with-param name="logo_file" select="/poster_info/poster/logo_file_hk/text()"/>
					<xsl:with-param name="file_name">sp_logo_file_hk</xsl:with-param>
					<xsl:with-param name="rdo_name">rdo_sp_logo_hk</xsl:with-param>
					<xsl:with-param name="input_file_name">tmp_sp_logo_hk</xsl:with-param>
					<xsl:with-param name="logo_file_path" select="$logo_file_hk_path"/>
					<xsl:with-param name="logo_size_desc" select="$lab_logo_client"/>					
				</xsl:call-template>			
				<xsl:call-template name="logo_template">
					<xsl:with-param name="logo_edition" select="$lab_lang_us"/>
					<xsl:with-param name="logo_file" select="/poster_info/poster/logo_file_us/text()"/>
					<xsl:with-param name="file_name">sp_logo_file_us</xsl:with-param>
					<xsl:with-param name="rdo_name">rdo_sp_logo_us</xsl:with-param>
					<xsl:with-param name="input_file_name">tmp_sp_logo_us</xsl:with-param>
					<xsl:with-param name="logo_file_path" select="$logo_file_us_path"/>
					<xsl:with-param name="logo_size_desc" select="$lab_logo_client"/>
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$rpt_type='FTN_AMD_MOBILE_POSTER_MAIN'">
				<xsl:call-template name="logo_template">
					<xsl:with-param name="logo_file" select="/poster_info/poster/logo_file_cn/text()"/>
					<xsl:with-param name="file_name">sp_logo_file_cn</xsl:with-param>
					<xsl:with-param name="rdo_name">rdo_sp_logo_cn</xsl:with-param>
					<xsl:with-param name="input_file_name">tmp_sp_logo_cn</xsl:with-param>
					<xsl:with-param name="logo_file_path" select="$logo_file_cn_path"/>
					<xsl:with-param name="logo_size_desc" select="$lab_logo_mobile1"/>
					<xsl:with-param name="logo_use_desc" select="$lab_logo_mobile_login"/>
					<xsl:with-param name="logo_use_title" select="$lab_mobile_login_logo" />
					<xsl:with-param name="logo_use_title_desc" select="$label_core_system_setting_138" />
					<xsl:with-param name="img_width">226</xsl:with-param>
					<xsl:with-param name="img_height">102</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="logo_template">
					<xsl:with-param name="logo_file" select="/poster_info/poster/logo_file_hk/text()"/>
					<xsl:with-param name="file_name">sp_logo_file_hk</xsl:with-param>
					<xsl:with-param name="rdo_name">rdo_sp_logo_hk</xsl:with-param>
					<xsl:with-param name="input_file_name">tmp_sp_logo_hk</xsl:with-param>
					<xsl:with-param name="logo_file_path" select="$logo_file_hk_path"/>
					<xsl:with-param name="logo_size_desc" select="$lab_logo_mobile2"/>
					<xsl:with-param name="logo_use_desc" select="$lab_logo_mobile_about"/>
					<xsl:with-param name="logo_use_title" select="$lab_mobile_about_logo" />
					<xsl:with-param name="logo_use_title_desc" select="$label_core_system_setting_139" />	
					<xsl:with-param name="img_width">279</xsl:with-param>
					<xsl:with-param name="img_height">200</xsl:with-param>			
				</xsl:call-template>
			</xsl:if>
		</table>
	</xsl:template>
	<!-- logo模版 -->
	<xsl:template name="logo_template">
		<xsl:param name="logo_edition" />
		<xsl:param name="file_name" />
		<xsl:param name="logo_file" />
		<xsl:param name="input_file_name" />
		<xsl:param name="rdo_name" />
		<xsl:param name="logo_file_path" />
		<xsl:param name="logo_size_desc"/>
		<xsl:param name="logo_use_desc"/>
		<xsl:param name="logo_use_title" />
		<xsl:param name="logo_use_title_desc" />
		<xsl:param name="img_width">400</xsl:param>
		<xsl:param name="img_height">92</xsl:param>
		<tr>
			<td class="wzb-form-label" valign="top">
				
			</td>
			<td class="wzb-form-control">
			   
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="center" width="30%">
				<img src="{$logo_file_path}" width="{$img_width}" height="{$img_height}" border="0" />
			</td>
				<td width="70%" valign="top">
				
				<label >
					<xsl:if test="$logo_edition">
				    <span class="TitleText"><strong><xsl:value-of select="$logo_edition"/>：</strong></span>
				   	 </xsl:if>
				   	 </label>
					<span class="text">
						<xsl:choose>
							<!-- 判断是否是移动端 -->
							<xsl:when test="$logo_use_title">
								<label class="wzb-form-weight"><xsl:value-of select="$logo_use_title"/></label>
								<span class="color-gray999">&#160;<xsl:value-of select="$logo_use_title_desc" /></span>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_logo_file_type"/>
							</xsl:otherwise>
						</xsl:choose>
					</span>
					<br/>				
					<input type="hidden" name="{$file_name}" value="{$logo_file}"/>
					
					<input type="radio"  name="{$rdo_name}" value="1" checked="checked" onClick="this.form.{$file_name}.value='{$logo_file}';this.form.{$input_file_name}.disabled = false"/>
					<label>
						<xsl:value-of select="$lab_keep_media"/>
					</label>
					<br/>
					<input type="radio"  name="{$rdo_name}" value="3" onclick="this.form.{$input_file_name}.disabled = false"/>
					<label>
						<xsl:value-of select="$lab_change_to"/>：
					</label>
					<div style="margin:0 0 10px 18px;" onClick="get_check(this)" >
						<xsl:call-template name="wb_gen_input_file">
							<xsl:with-param name="name" select="$input_file_name"/>
							<xsl:with-param name="onBlur">doAutoCheck(this,this.form.{$rdo_name}[1], this.form.{$rdo_name}[0], this.form.{$file_name})</xsl:with-param>
							<xsl:with-param name="onfocus" >this.form.{$rdo_name}[1].checked=true</xsl:with-param>
							<!-- <xsl:with-param name="disabled" >disabled</xsl:with-param> -->
						</xsl:call-template>
					</div>
					
					(<xsl:value-of select="$logo_size_desc"/>，<xsl:value-of select="$lab_logo_file_type"/>)
					
				</td>
			
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- loginBg template -->

	<xsl:template name="loginBg">
		<table>
		     <tr>
		       <td colspan="10">
		            <xsl:call-template name="wb_ui_head">
						 <xsl:with-param name="text" select="$label_core_system_setting_141"/>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>
		       </td>
		     </tr>
			<tr>
				<td></td>
				<td>
					<input type="checkbox" name="show_login_header_ind" >
						<xsl:if test="/poster_info/poster/show_header_ind = 'true'">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
						
					</input>
					
					<label class="label-weight"><xsl:value-of select="$lab_show_header"/></label>
				 					<br/>
					<input type="checkbox" name="show_all_footer_ind">
						<xsl:if test="/poster_info/poster/show_footer_ind = 'true'">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</input>
					<label class="label-weight"><xsl:value-of select="$lab_show_footer"/></label>
				</td>
			</tr>
			
			<!-- 视频配置开始 -->
			<xsl:if test="$rpt_type !='MOBILE_POSTER_MAIN'">
			 <td colspan="10">
		            <xsl:call-template name="wb_ui_head">
						 <xsl:with-param name="text" select="$label_core_system_setting_142"/>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>
		      </td>
			<tr>
				<td><input type="radio" name="login_bg_type" value="VOD"  >
					<xsl:if test="/poster_info/poster/login_bg_type = 'VOD'">
						<xsl:attribute name="checked">checked</xsl:attribute>
					</xsl:if>
					</input>
					<xsl:value-of select="$lab_bg_poster_type_vod"/>
				</td>
				<td>
				</td>
			</tr>
			<xsl:call-template name="loginBg_template">
				<xsl:with-param name="login_bg_file" select="/poster_info/poster/login_bg_file5"/>
				<xsl:with-param name="file_name">login_bg_file5</xsl:with-param>
				<xsl:with-param name="rdo_name">rdo_login_bg5</xsl:with-param>
				<xsl:with-param name="input_file_name">tmp_login_bg_file5</xsl:with-param>
				<xsl:with-param name="login_bg_file_path" select="$login_bg_file5_path"/>
				<xsl:with-param name="title" select="$label_core_system_setting_143"/>
			</xsl:call-template>		
			<xsl:call-template name="loginBg_template">
				<xsl:with-param name="login_bg_file" select="/poster_info/poster/login_bg_video"/>
				<xsl:with-param name="file_name">login_bg_video</xsl:with-param>
				<xsl:with-param name="rdo_name">rdo_login_video</xsl:with-param>
				<xsl:with-param name="input_file_name">tmp_login_bg_video</xsl:with-param>
				<xsl:with-param name="login_bg_file_path" select="$login_bg_video_path"/>
			</xsl:call-template>			
			<!-- 轮播图片配置开始 -->
			<tr><td>
				<input type="radio" name="login_bg_type" value="PIC" >
					<xsl:if test="/poster_info/poster/login_bg_type = 'PIC'">
						<xsl:attribute name="checked">checked</xsl:attribute>
					</xsl:if>
				</input>
				<xsl:value-of select="$lab_bg_poster_type_img"/>
				</td></tr>
			</xsl:if>
			
			<xsl:call-template name="loginBg_template">
				<xsl:with-param name="login_bg_file" select="/poster_info/poster/login_bg_file1"/>
				<xsl:with-param name="file_name">login_bg_file1</xsl:with-param>
				<xsl:with-param name="rdo_name">rdo_login_bg1</xsl:with-param>
				<xsl:with-param name="input_file_name">tmp_login_bg_file1</xsl:with-param>
				<xsl:with-param name="login_bg_file_path" select="$login_bg_file1_path"/>
				<xsl:with-param name="number" > 1</xsl:with-param>
				
			</xsl:call-template>
			<xsl:if test="$rpt_type='FTN_AMD_POSTER_MAIN'">	
				<xsl:call-template name="loginBg_template">
					<xsl:with-param name="login_bg_file" select="/poster_info/poster/login_bg_file2"/>
					<xsl:with-param name="file_name">login_bg_file2</xsl:with-param>
					<xsl:with-param name="rdo_name">rdo_login_bg2</xsl:with-param>
					<xsl:with-param name="input_file_name">tmp_login_bg_file2</xsl:with-param>
					<xsl:with-param name="login_bg_file_path" select="$login_bg_file2_path"/>
					<xsl:with-param name="number" > 2</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="loginBg_template">
					<xsl:with-param name="login_bg_file" select="/poster_info/poster/login_bg_file3"/>
					<xsl:with-param name="file_name">login_bg_file3</xsl:with-param>
					<xsl:with-param name="rdo_name">rdo_login_bg3</xsl:with-param>
					<xsl:with-param name="input_file_name">tmp_login_bg_file3</xsl:with-param>
					<xsl:with-param name="login_bg_file_path" select="$login_bg_file3_path"/>
					<xsl:with-param name="number" > 3</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="loginBg_template">
					<xsl:with-param name="login_bg_file" select="/poster_info/poster/login_bg_file4"/>
					<xsl:with-param name="file_name">login_bg_file4</xsl:with-param>
					<xsl:with-param name="rdo_name">rdo_login_bg4</xsl:with-param>
					<xsl:with-param name="input_file_name">tmp_login_bg_file4</xsl:with-param>
					<xsl:with-param name="login_bg_file_path" select="$login_bg_file4_path"/>
					<xsl:with-param name="number" > 4</xsl:with-param>
				</xsl:call-template>
			</xsl:if>
		</table>
	</xsl:template>
	<xsl:template name="flash_loginBg">
		<xsl:param name="login_file_path"/>
		<OBJECT name="{$site_name}_movie" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0" width="165" height="165">
			<PARAM NAME="movie" VALUE="{$login_file_path}"/>
			<EMBED src="{$login_file_path}" TYPE="application/x-shockwave-flash" PLUGINSPAGE="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash" width="165" height="165"/>
		</OBJECT>
	</xsl:template>
	<xsl:template name="loginBg_template">
	    <xsl:param name="login_bg_file" />
	    <xsl:param name="file_name"/>
	    <xsl:param name="rdo_name"/>
	    <xsl:param name="input_file_name"/>
	    <xsl:param name="login_bg_file_path"/>
	    <xsl:param name="title"/>
	    <xsl:param name="number"/>
		<tr>
				<td class="wzb-form-label" valign="top">
				</td>
				<td class="wzb-form-control">
				    <span class="wzb-form-weight">
				    	<xsl:if test="$rpt_type='FTN_AMD_POSTER_MAIN' and $file_name!='login_bg_video'">
				    	   <xsl:choose>
				    	     <xsl:when test="$title != ''">
				    	       <xsl:value-of select="$title"/>
				    	     </xsl:when>
				    	     <xsl:otherwise>
				    	       <xsl:value-of select="$lab_poster"/>
				    	     </xsl:otherwise>
				    	   </xsl:choose>
				    		<xsl:value-of select="$number"/>
				    	</xsl:if>
				    	<xsl:if test="$file_name ='login_bg_video'">
				    		<xsl:value-of select="$lab_vod_poster"/>
				    	</xsl:if>
				    </span>
					<input type="hidden" name="lab_poster" value="{$lab_poster}"></input>
					<span class="text">
						<!-- <xsl:if test="$rpt_type='POSTER_MAIN' and $file_name!='login_bg_video'">
							<xsl:value-of select="$lab_poster_file"/>
						</xsl:if>
						<xsl:if test="$file_name ='login_bg_video'">
							<xsl:value-of select="$lab_poster_vod_file"/>
						</xsl:if>
						<xsl:if test="$rpt_type='MOBILE_POSTER_MAIN'">
							<xsl:value-of select="$lab_poster_file_mobile"/>
						</xsl:if> -->
						<br/>
					</span>
					<xsl:choose>
						<xsl:when test="not($login_bg_file) or $login_bg_file=''">
							<xsl:choose>
								<xsl:when test="$file_name!='login_bg_file5' and $file_name!='login_bg_video'">
								    <div style="margin:0 0 10px 18px;" >
										<xsl:call-template name="wb_gen_input_file">
											<xsl:with-param name="name" select="$file_name"/>
											<xsl:with-param name="onBlur">doAutoCheck(this,this.form.{$rdo_name}[2], this.form.{$rdo_name}[0], this.form.{$file_name})</xsl:with-param>
											<xsl:with-param name="onfocus" >this.form.{$rdo_name}[2].checked=true</xsl:with-param>
										</xsl:call-template>
									</div>
								<!-- 	<input type="file" style="width:450px" name="{$file_name}" class="wzb-inputText"/>  -->
								</xsl:when>
								<xsl:otherwise>
									<input type="radio" checked="checked"  name="{$rdo_name}" value="2" onClick="this.form.{$file_name}.value='';"/>
									<span class="Text">
										<label >
											<xsl:if test="$file_name ='login_bg_video'">
												<xsl:value-of select="$lab_default_media"/>
											</xsl:if>
											<xsl:if test="$file_name ='login_bg_file5'">
												<xsl:value-of select="$lab_default_image"/>
											</xsl:if>
										</label>
									</span><br/>
									<input type="radio"  name="{$rdo_name}" value="3" onclick="this.form.{$input_file_name}.disabled = false"/>
								<!-- 	<span class="Text"> -->
										<label >
											<xsl:value-of select="$lab_change_to"/>：
										</label>
								<!--   </span> -->
									 <div style="margin:0 0 10px 18px;" onClick="get_check(this)" >
										<xsl:call-template name="wb_gen_input_file">
											<xsl:with-param name="name" select="$input_file_name"/>
											<xsl:with-param name="onBlur">doAutoCheck(this,this.form.{$rdo_name}[2], this.form.{$rdo_name}[0], this.form.{$file_name})</xsl:with-param>
											<xsl:with-param name="onfocus" >this.form.{$rdo_name}[2].checked=true</xsl:with-param>
											<!-- <xsl:with-param name="disabled" >disabled</xsl:with-param> -->
										</xsl:call-template>
									</div>
								<!-- 	<input type="file" class="wzb-inputText" name="{$input_file_name}" style="width:260px;" onBlur="doAutoCheck(this,this.form.{$rdo_name}[2], this.form.{$rdo_name}[0], this.form.{$file_name})" onfocus="this.form.{$rdo_name}[2].checked=true" disabled="disabled"/> -->
								</xsl:otherwise>
							</xsl:choose>
							<span class="text">
								(
								<xsl:if test="$rpt_type='FTN_AMD_POSTER_MAIN'  and $file_name!='login_bg_file5' and $file_name!='login_bg_video'">
									<xsl:value-of select="$lab_login_bg_size"/>,
								</xsl:if>
								<xsl:if test="$rpt_type='FTN_AMD_MOBILE_POSTER_MAIN'">
									<xsl:value-of select="$lab_login_mobile"/>,
								</xsl:if>
								<xsl:if test="$rpt_type='FTN_AMD_POSTER_MAIN' and $file_name!='login_bg_file5' and $file_name!='login_bg_video'">
									<xsl:value-of select="$lab_poster_file"/>
								</xsl:if>
								<xsl:if test="$file_name='login_bg_file5'">
										<xsl:value-of select="$lab_login_bg_vod_img"/>
								</xsl:if>
								<xsl:if test="$file_name='login_bg_video'">
										<xsl:value-of select="$lab_login_bg_vod"/>
								</xsl:if>
								<xsl:if test="$rpt_type='FTN_AMD_MOBILE_POSTER_MAIN'">
									<xsl:value-of select="$lab_poster_file_mobile"/>
								</xsl:if>	
								)																
							</span>
						</xsl:when>
						<xsl:otherwise>
							<xsl:variable name="login_file_ext">
								<xsl:call-template name="change_lowercase">
									<xsl:with-param name="input_value">
										<xsl:value-of select="substring-after($login_bg_file,'.')"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:variable>
						 <tr>
						    <td nowrap="nowrap" valign="top" align="center" width="30%">
						            <xsl:choose>
										<xsl:when test="$login_file_ext = 'swf'">
											<xsl:call-template name="flash_loginBg">
												<xsl:with-param name="login_file_path" select="$login_bg_file_path"/>
											</xsl:call-template>
										</xsl:when>
										<xsl:when test="$login_file_ext = 'mp4' or $login_file_ext = 'MP4'">
											<video width="200" height="100" controls="controls">
	  											<source src="{$login_bg_file_path}" type="video/mp4" />
	  										</video>
										</xsl:when>
										<xsl:otherwise>
											<img src="{$login_bg_file_path}" width="200" height="100"/>												
										</xsl:otherwise>
									</xsl:choose>
						    </td>						    
						    <td width="70%" valign="top">
						            <input type="radio"  name="{$rdo_name}" value="1" checked="checked" onClick="this.form.{$file_name}.value='{$login_bg_file}';this.form.{$input_file_name}.disabled = false"/>
										<label class="label-weight">
											<xsl:value-of select="$lab_keep_media"/>
										</label>
									<br/>
									<input type="radio"  name="{$rdo_name}" value="2" onClick="this.form.{$file_name}.value=''; this.form.{$input_file_name}.disabled = false;"/>
										<label class="label-weight">
											<xsl:if test="$file_name ='login_bg_video'">
												<xsl:value-of select="$lab_default_media"/>
											</xsl:if>
											<xsl:if test="$file_name ='login_bg_file5'">
												<xsl:value-of select="$lab_default_image"/>
											</xsl:if>
											<xsl:if test="$file_name !='login_bg_file5' and $file_name !='login_bg_video'">
											<xsl:value-of select="$lab_remove_media"/>
											</xsl:if>
										</label>
									<br/>
									<input type="radio"  name="{$rdo_name}" value="3" onclick="this.form.{$input_file_name}.disabled = false"/>
									<label  class="label-weight">
										<xsl:value-of select="$lab_change_to"/>：
									</label>
									<div style="margin:0 0 10px 18px;"  onclick="get_check(this)">
										<xsl:call-template name="wb_gen_input_file">
											<xsl:with-param name="name" select="$input_file_name"/>
											<xsl:with-param name="onBlur">doAutoCheck(this,this.form.{$rdo_name}[2], this.form.{$rdo_name}[0], this.form.{$file_name})</xsl:with-param>
											<xsl:with-param name="onfocus">this.form.{$rdo_name}[2].checked=true</xsl:with-param>
											<!-- <xsl:with-param name="disabled">disabled</xsl:with-param>  -->
										</xsl:call-template>
									</div>
									
									(
									<xsl:if test="$rpt_type='FTN_AMD_POSTER_MAIN' and $file_name!='login_bg_file5' and $file_name!='login_bg_video'">
										<xsl:value-of select="$lab_login_bg_size"/>
									</xsl:if>
									<xsl:if test="$rpt_type='FTN_AMD_MOBILE_POSTER_MAIN'">
										<xsl:value-of select="$lab_login_mobile"/>
									</xsl:if>
									<xsl:if test="$rpt_type='FTN_AMD_POSTER_MAIN' and $file_name!='login_bg_file5' and $file_name!='login_bg_video'">
										<xsl:value-of select="$lab_poster_file"/>
									</xsl:if>
									
									<xsl:if test="$file_name='login_bg_file5'">
										<xsl:value-of select="$lab_login_bg_vod_img"/>
									</xsl:if>
									<xsl:if test="$file_name='login_bg_video'">
										<xsl:value-of select="$lab_login_bg_vod"/>
									</xsl:if>
									
									<xsl:if test="$rpt_type='FTN_AMD_MOBILE_POSTER_MAIN'">
										<xsl:value-of select="$lab_poster_file_mobile"/>
									</xsl:if>
									)
									<input type="hidden" name="{$file_name}" value="{$login_bg_file}"/>
						    </td>
						 </tr>
						</xsl:otherwise>
					</xsl:choose>
					
				</td>
		</tr>	
	</xsl:template>
	<!-- =============================================================== -->
	
	<!-- ============================移动端指引图片============================== -->
	<xsl:template name="guide">
		<table>
			<xsl:call-template name="guide_template">
				<xsl:with-param name="guide_file" select="/poster_info/poster/guide_file1"/>
				<xsl:with-param name="file_name">guide_file1</xsl:with-param>
				<xsl:with-param name="rdo_name">rdo_guide_bg1</xsl:with-param>
				<xsl:with-param name="input_file_name">tmp_guide_file1</xsl:with-param>
				<xsl:with-param name="guide_file_path" select="$guide_file1_path"/>
			</xsl:call-template>
			<xsl:call-template name="guide_template">
				<xsl:with-param name="guide_file" select="/poster_info/poster/guide_file2"/>
				<xsl:with-param name="file_name">guide_file2</xsl:with-param>
				<xsl:with-param name="rdo_name">rdo_guide_bg2</xsl:with-param>
				<xsl:with-param name="input_file_name">tmp_guide_file2</xsl:with-param>
				<xsl:with-param name="guide_file_path" select="$guide_file2_path"/>
			</xsl:call-template>
			<xsl:call-template name="guide_template">
				<xsl:with-param name="guide_file" select="/poster_info/poster/guide_file3"/>
				<xsl:with-param name="file_name">guide_file3</xsl:with-param>
				<xsl:with-param name="rdo_name">rdo_guide_bg3</xsl:with-param>
				<xsl:with-param name="input_file_name">tmp_guide_file3</xsl:with-param>
				<xsl:with-param name="guide_file_path" select="$guide_file3_path"/>
			</xsl:call-template>
		</table>
	</xsl:template>
	<xsl:template name="flash_guide">
		<xsl:param name="guide_bg_file_path"/>
		<OBJECT name="{$site_name}_movie" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0" width="165" height="165">
			<PARAM NAME="movie" VALUE="{$guide_bg_file_path}"/>
			<EMBED src="{$guide_bg_file_path}" TYPE="application/x-shockwave-flash" PLUGINSPAGE="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash" width="165" height="165"/>
		</OBJECT>
	</xsl:template>
	<xsl:template name="guide_template">
			<xsl:param name="guide_file" />
	    	<xsl:param name="file_name"/>
	    	<xsl:param name="rdo_name"/>
	    	<xsl:param name="input_file_name"/>
	    	<xsl:param name="guide_file_path"/>	
	    	<tr>
				<td class="wzb-form-label" valign="top">
					
				</td>
				<td class="wzb-form-label">
				    <xsl:value-of select="$lab_poster"/>：
					<input type="hidden" name="lab_poster" value="{$lab_poster}"></input>
						<xsl:if test="$rpt_type='FTN_AMD_POSTER_MAIN'">
							<xsl:value-of select="$lab_poster_file"/><br/>
						</xsl:if>
						<xsl:if test="$rpt_type='FTN_AMD_MOBILE_POSTER_MAIN'">
							<xsl:value-of select="$lab_poster_file_mobile"/><br/>
						</xsl:if>
					<xsl:choose>
						<xsl:when test="not($guide_file) or $guide_file=''">
							<input type="file" style="width:450px" name="{$file_name}" class="wzb-inputText"/>
							<br/>
							(<xsl:value-of select="$lab_login_bg_size"/>)<br/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:variable name="login_file_ext">
								<xsl:call-template name="change_lowercase">
									<xsl:with-param name="input_value">
										<xsl:value-of select="substring-after($guide_file,'.')"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:variable>
						 <tr>
						    <td nowrap="nowrap" valign="top" align="center" width="30%">
						            <xsl:choose>
										<xsl:when test="$login_file_ext = 'swf'">
											<xsl:call-template name="flash_guide">
												<xsl:with-param name="guide_bg_file_path" select="$guide_file_path"/>
											</xsl:call-template>
										</xsl:when>
										<xsl:otherwise>
											<img src="{$guide_file_path}" width="200" height="100"/>												
										</xsl:otherwise>
									</xsl:choose>
						    </td>						    
						    <td width="70%" valign="top">
						            <input type="radio"  name="{$rdo_name}" value="1" checked="checked" onClick="this.form.{$file_name}.value='{$guide_file}';"/>
										<label >
											<xsl:value-of select="$lab_keep_media"/>
										</label>
									<br/>
									<input type="radio"  name="{$rdo_name}" value="2" onClick="this.form.{$file_name}.value=''; "/>
									<label >
										<xsl:value-of select="$lab_remove_media"/>
									</label>
									<br/>
									<input type="radio"  name="{$rdo_name}" value="3" onclick="this.form.{$input_file_name}.disabled = false"/>
									<label >
										<xsl:value-of select="$lab_change_to"/>：
									</label>
									<input type="file" class="wzb-inputText" name="{$input_file_name}" style="width:260px;" onBlur="doAutoCheck(this,this.form.{$rdo_name}[2], this.form.{$rdo_name}[0], this.form.{$file_name})" onfocus="this.form.{$rdo_name}[2].checked=true" disabled="disabled"/>
									<br/>
									<xsl:value-of select="$lab_login_bg_size"/><br/>
									<input type="hidden" name="{$file_name}" value="{$guide_file}"/>
						    </td>
						 </tr>
						</xsl:otherwise>
					</xsl:choose>
					
				</td>
		</tr>	
	</xsl:template>
	
	<xsl:template name="manageText">
		<table>
			<xsl:call-template name="manageText_template">
				<xsl:with-param name="title_pc" select="$lab_welcome_pc"/>
				<xsl:with-param name="title_mb" select="$lab_welcome_mobile"/>
				<xsl:with-param name="input_name_pc">sp_welcome_word</xsl:with-param>
				<xsl:with-param name="input_name_mb">mb_welcome_word</xsl:with-param>
				<xsl:with-param name="sp_welcome_word" select="/poster_info/poster/sp_welcome_word"/>
				<xsl:with-param name="mb_welcome_word" select="/poster_info/poster/mb_welcome_word"/>
			</xsl:call-template>
		</table>
	</xsl:template>
	<xsl:template name="manageText_template">
		<xsl:param name="title_pc"/>
		<xsl:param name="title_mb"/>
		<xsl:param name="input_name_pc"/>
		<xsl:param name="input_name_mb"/>
		<xsl:param name="sp_welcome_word"/>
		<xsl:param name="mb_welcome_word"/>
		<tr>
			<td class="wzb-form-label">
				
			</td>
			<td class="wzb-form-control">
				<span class="TitleText"><xsl:value-of select="$title_pc"/>：</span>&#160;&#160;<input type="text" name="{$input_name_pc}" size="50"  value="{$sp_welcome_word}" style="  height: 35px; line-height:35 px;"></input>
				<input type="hidden" name="tmp_welcome_pc" value="{$lab_welcome_tmp}"></input>			
			</td>
		</tr>
		<tr style="display:none">
			<td class="wzb-form-label">
				
			</td>
			<td class="wzb-form-control">
				<xsl:value-of select="$title_mb"/>：<input type="text" name="{$input_name_mb}" size="50" value="{$mb_welcome_word}"></input>
				<input type="hidden" name="tmp_welcome_mb" value="{$lab_welcome_tmp}"></input>			
			</td>
		</tr>		
	</xsl:template>
	
</xsl:stylesheet>

