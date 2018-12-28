<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_gen_input_file.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/kindeditor.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/article_module">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	
	<xsl:variable name="tc_enabled" select="//meta/tc_enabled"/>
	<xsl:variable name="page_variant" select="//meta/page_variant"/>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_article_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'article_string')"/> 	
	<xsl:variable name="lab_article_title_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1129')"/>
	<xsl:variable name="lab_article_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '995')"/> 	
	<xsl:variable name="lab_article_field_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '79')"/>
	<xsl:variable name="lab_article_field_icon"  select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1151')"/> 	
	<xsl:variable name="lab_content" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_content')"/> 	
	<xsl:variable name="lab_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_status')"/> 	
	<xsl:variable name="lab_tc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_tc')"/> 	
	<xsl:variable name="lab_def_tc_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1130')"/> 	
	<xsl:variable name="lab_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'wb_imp_tem_description')"/> 	

	<xsl:variable name="lab_btn_delete" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '257')"/> 	
	<xsl:variable name="lab_select_on" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '765')"/> 	
	<xsl:variable name="lab_select_off" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '766')"/> 	

	<xsl:variable name="lab_yes" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_yes')"/> 	
	<xsl:variable name="lab_no" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_no_1')"/> 	
	<xsl:variable name="lab_publish_mobile" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_publish_mobile')"/> 	


	<xsl:variable name="lab_article_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '704')"/> 	
	<xsl:variable name="lab_update_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '708')"/> 	
	<xsl:variable name="lab_add_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '473')"/> 	
	<xsl:variable name="lab_del_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '257')"/> 	
	
	<xsl:variable name="lab_article_status_on" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '765')"/> 	
	<xsl:variable name="lab_article_status_off" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '766')"/> 	
	<xsl:variable name="lab_info_required" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '335')"/> 	
	<xsl:variable name="lab_g_form_btn_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '590')"/> 	
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/> 	
	
	<xsl:variable name="lab_keep_media" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '801')"/> 	
	<xsl:variable name="lab_remove_media" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '802')"/> 	
	<xsl:variable name="lab_change_to" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '803')"/>
	<xsl:variable name="lab_article_type" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_type')"/>
	
	<xsl:variable name="article_desc_range" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'article_desc_range')"/>
	
	<xsl:variable name="lab_select_default_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_select_default_image')"/>
	<xsl:variable name="lab_default_images" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_default_images')"/>
	<xsl:variable name="lab_upload_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_upload_image')"/>
	<xsl:variable name="lab_button_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, '329')"/>
	<!-- ============================================================= -->
	<xsl:variable name="ins_or_upd">
		<xsl:choose>
			<xsl:when test="/article_module/article/art_id &gt;= 1">UPD</xsl:when>
			<xsl:otherwise>INS</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- ============================================================= -->
	<xsl:template name="main">

		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<link rel="stylesheet" type="text/css" href="../static/css/thickbox.css" />
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_article.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" SRC="{$wb_js_path}wb_goldenman.js"/>
			<script type="text/javascript" src="../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="{$wb_js_path}jquery.prompt.js"/>
			<script type="text/javascript" src="../static/js/thickbox-compressed.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_defaultImage.js"/>
			<script language="JavaScript" type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_rm_{$wb_cur_lang}.js"/>
 
			
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
 				var wbarticle = new wbArticle;
				var goldenman = new wbGoldenMan;
				
				$(function(){
					$art_desc = $("textarea[name='art_introduction']");
					if($.trim($art_desc.val()) == '') {
						$art_desc.val($art_desc.attr('prompt'));
						$art_desc.css("color","#999");
					}
					$art_desc.click(function(){
						if($.trim($art_desc.val()) == $art_desc.attr('prompt')){
							$art_desc.val('');	
							$art_desc.css("color","#666");
						}
					});
					$art_desc.blur(function(){
						if($.trim($art_desc.val()) == ''){
							$art_desc.val($art_desc.attr('prompt'));
							$art_desc.css("color","#999");		
						}
					});
				})
				
				function msg_icon_show(type) {
					if (document.all != '') {
						//document.all.msg_icon_file.disabled = true;

						document.images.msg_icon_preview.src = '..]]><![CDATA[]]><xsl:value-of select="/article_module/article/art_icon_file"/><![CDATA[';

						document.images.msg_icon_preview.width = 226
						document.all.msg_icon_div_img.style.display = '';
					}
				}
				
				function msg_icon_remove() {
					if (document.all != '') {
						document.all.msg_icon_file.disabled = true;
						document.all.msg_icon_del_ind.value = 'true';
						document.all.msg_icon_div_img.style.display = 'none';
					}
				}

				function msg_icon_file_status_change(frm) {
					if (document.all != '') {
						document.all.msg_icon_file.disabled = '';
 						

						if (frm.msg_icon_file.value != '') {
							file_ext = frm.msg_icon_file.value.sublering(frm.msg_icon_file.value.lastIndexOf(".") + 1, frm.msg_icon_file.value.length)
							file_ext = file_ext.toLowerCase();
							if (file_ext == 'jpg' || file_ext == 'gif' || file_ext == 'png') {
								document.images.msg_icon_preview.src = window.URL.createObjectURL(document.all.msg_icon_file.files[0]);
							}
						}
					}
				}
				
				function msg_icon_preview_change(frm){
					if(document.all.msg_icon_file.files && document.all.msg_icon_file.files[0]){
						document.images.msg_icon_preview.src = window.URL.createObjectURL(document.all.msg_icon_file.files[0]);
					}
				}
 			]]></script> 
 			
 					
	 
 			<xsl:call-template name="kindeditor_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="content"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<form enctype="multipart/form-data" name="frmXml">
			<input name="module" type="hidden"/>
			<input name="cmd" type="hidden">			
				<xsl:attribute name="value">
					<xsl:choose>
						<xsl:when test="$ins_or_upd = 'INS'">add_article</xsl:when>
						<xsl:otherwise>add_article</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
			</input>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<input type="hidden" name="art_id" value="{//art_id}"/>
			<input type="hidden" name="curLan" value="{//cur_usr/@curLan}" />
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_ARTICLE_MAIN</xsl:with-param>
				<xsl:with-param name="parent_code">FTN_AMD_ARTICLE_MAIN</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text">
					<xsl:choose>
						<xsl:when test="$ins_or_upd='INS'">
							<xsl:value-of select="$lab_add_button"/>
						</xsl:when>
						<xsl:when test="$ins_or_upd='UPD'">
							<xsl:value-of select="$lab_update_button"/>
						</xsl:when>
					</xsl:choose>

					<xsl:value-of select="$lab_article_title"/>
				</xsl:with-param>
			</xsl:call-template>
			
			<xsl:apply-templates select="article">
			<xsl:with-param name="mode">upd</xsl:with-param>
			</xsl:apply-templates>
		</form>
	</xsl:template>

	<!-- =============================================================== -->
	<xsl:template match="article">
	<xsl:param name="mode"/>
	<xsl:variable name="lab_remove_image">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">刪除媒體檔案</xsl:when>
				<xsl:when test="$wb_lang='gb'">删除媒体文档</xsl:when>
				<xsl:otherwise>Remove media file</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_image_file">
			<xsl:choose>
				<xsl:when test="$wb_lang = 'ch'">媒體檔案(JPG,GIF,PNG)</xsl:when>
				<xsl:when test="$wb_lang = 'gb'">媒体文档(JPG,GIF,PNG)</xsl:when>
				<xsl:when test="$wb_lang = 'en'">Media file(JPG,GIF,PNG)</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_keep_image">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">保留媒體檔案</xsl:when>
				<xsl:when test="$wb_lang='gb'">保留媒体文档</xsl:when>
				<xsl:otherwise>Keep media file</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_change_to">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">更改媒體檔案為</xsl:when>
				<xsl:when test="$wb_lang='gb'">更改媒体文档为</xsl:when>
				<xsl:otherwise>Media filechange to</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="img_des">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">圖片規格建議：寬226px，高297px</xsl:when>
				<xsl:when test="$wb_lang='gb'">图片规格建议：宽226px，高297px</xsl:when>
				<xsl:otherwise>Image size recommendation: width 226px, height 297px</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="textarea_length">
			<xsl:choose>
				<xsl:when test="$wb_lang='en'">200</xsl:when>
				<xsl:otherwise>100</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<table>
	<!--标题-->
			<tr>
				<td class="wzb-form-label">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_article_field_title"/>：
						<input type="hidden" name="lab_article_field_title" value="{$lab_article_field_title}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:300px;" name="art_title" value="{art_title}" class="wzb-inputText"/>
				</td>
			</tr>
			<!-- 图示 -->
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_article_field_icon"/>：
					<input type="hidden" name="lab_article_field_icon" value="{$lab_article_field_icon}"></input>
				</td>
				<td class="wzb-form-control">
				<xsl:choose>
					<xsl:when test="$mode = 'upd' and art_icon_file and  art_icon_file != ''">
						<xsl:variable name="art_id" select="art_id" />
						<xsl:variable name="msg_icon" select="art_icon_file" />
						<script type="text/javascript" language="JavaScript">
							$(function(){
								initDefaultImage('article', 'msg_icon', false);
							})
						</script>
						<table>
						<input type="hidden" name="msg_icon_result" />
						<tbody>
							<tr>
								<td rowspan="4" style="width:250px;" valign="top">
									<div id="msg_icon_div_img">
										<img name="msg_icon_preview" src="{art_icon_file}" height="137px" width="226px"/>
									</div>
								</td>
								<td>
									<label for="msg_icon_select0">
										<input type="radio" id="msg_icon_select0" name="art_icon_select" checked="checked" value="msg_icon_remain" onclick="msg_icon_show()" />
										<xsl:value-of select="$lab_keep_image"/>
										<input type="hidden" name="msg_icon" value="{$msg_icon}" />
									</label>
								</td>
							</tr>
							<tr>
								<td>
									<label for="msg_icon_select1">
										<input type="radio" id="msg_icon_select1" name="art_icon_select" value="msg_icon_default" onclick="useDefaultImage(event)"/>
										<input name="" type="button" class="wzb-btn-blue" style="border: 1px solid transparent;padding:3px 8px;" onclick="this.parentNode.firstChild.checked=true;show_default_image();useDefaultImage(event)" value="{$lab_select_default_image}"/>
										<a id="default_btn"    href="#TB_inline?height=380&amp;width=580&amp;inlineId=myOnPageContent" class="thickbox" style="display: none;"></a>
										<input type="hidden" name="default_image"/>
									</label>
									<br/>
									<div id="myOnPageContent" style="display: none;">
										<div class="thickbox-big ">
											<div class="thickbox-tit   thickbox-tit-1" style=" text-align:center;">
												<xsl:value-of select="$lab_default_images"/>
											</div>
			
											<div class="thickbox-cont thickbox-user clearfix thickbox-content-2" id="defaultImages"></div>
				
											<div class="norm-border  thickbox-footer"  >
												<input type="button" class="margin-right10   wzb-btn-big
												 wzb-btn-blue btn" name="pertxt" onclick="selectImage()" value="{$lab_button_ok}" />
												<input type="button" class="wzb-btn-big	TB_closeWindowButton wzb-btn-blue btn" name="pertxt" value="{$lab_g_form_btn_cancel}" />
											</div>
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<label for="msg_icon_select2">
										<input type="radio" id="msg_icon_select2" name="art_icon_select" value="msg_icon_change" onclick="msg_icon_file_status_change(document.frmXml)" />
										<span class="wbFormRightText"><xsl:value-of select="$lab_change_to" /></span>
										<xsl:call-template name="wb_gen_input_file">
											<xsl:with-param name="id">msg_icon_file</xsl:with-param>
											<xsl:with-param name="name">art_icon_file</xsl:with-param>
											<xsl:with-param name="onclick">document.frmXml.msg_icon_select2.checked=true</xsl:with-param>
											<!-- <xsl:with-param name="disabled">disabled</xsl:with-param> -->
											<xsl:with-param name="onchange">msg_icon_preview_change(document.frmXml)</xsl:with-param>
										</xsl:call-template>
										<span class="wbFormRightText"><xsl:value-of select="$lab_image_file" /></span>
									</label>
								<!-- 	<label for="msg_icon_select1">
										<input type="radio" id="msg_icon_select1" name="msg_icon_select" value="msg_icon_del" onclick="msg_icon_remove()" />
										<span class="wbFormRightText"><xsl:value-of select="$lab_remove_image" /></span>
										<input type="hidden" name="msg_icon_del_ind" />
									</label> -->
								</td>
							</tr>
							<tr>
								<td>
									<xsl:value-of select="$img_des"/>
								</td>
							</tr>
						</tbody>
						</table>
					
					</xsl:when>
					<xsl:otherwise>
						<script type="text/javascript" language="JavaScript">
							$(function(){
								initDefaultImage('article', 'msg_icon', true);
							})
						</script>
						<table>
						<input type="hidden" name="msg_icon_result" />
							<tr>
								<td rowspan="2" style="width: 252px;">
									<div id="msg_icon_div_img">
										<img name="msg_icon_preview" height="137px" width="226px"/>
									</div>
								</td>
								<td>
									<label for="msg_icon_select1">
										<input type="radio" id="msg_icon_select1" checked="checked" name="art_icon_select" value="msg_icon_default" onclick="useDefaultImage(event)"/>
										<input name="" type="button" class="wzb-btn-blue" onclick="this.parentNode.firstChild.checked=true;show_default_image();useDefaultImage(event)" style="border: 1px solid transparent;padding:3px 8px;" value="{$lab_select_default_image}"/>
										<a id="default_btn"    href="#TB_inline?height=380&amp;width=580&amp;inlineId=myOnPageContent" class="thickbox" style="display: none;"></a>
										<input type="hidden" name="default_image"/>
									</label>
									<br/>
									<div id="myOnPageContent" style="display: none;">
									
										<div class="thickbox-big">
											<div class="thickbox-tit thickbox-tit-1" style=" text-align:center;">
												<xsl:value-of select="$lab_default_images"/>
											</div>
			
											<div class="thickbox-cont thickbox-user clearfix thickbox-content-2" id="defaultImages"></div>
			
											<div class="norm-border thickbox-footer"  >
												<input type="button" class="margin-right10 wzb-btn-big wzb-btn-blue btn" name="pertxt" onclick="selectImage()" value="{$lab_button_ok}" />
												<input type="button" class="wzb-btn-big TB_closeWindowButton wzb-btn-blue btn" name="pertxt" value="{$lab_g_form_btn_cancel}" />
											</div>
										
										</div>
	
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<label for="msg_icon_select2">
										<input type="radio" id="msg_icon_select2" name="art_icon_select" value="msg_icon_change" onclick="msg_icon_file_status_change(document.frmXml)" />
										<span class="wbFormRightText"><xsl:value-of select="$lab_upload_image"/></span>&#160;
										<xsl:call-template name="wb_gen_input_file">
											<xsl:with-param name="id">msg_icon_file</xsl:with-param>
											<xsl:with-param name="name">art_icon_file</xsl:with-param>
											<xsl:with-param name="onclick">document.frmXml.msg_icon_select2.checked=true</xsl:with-param>
											<!-- <xsl:with-param name="disabled">disabled</xsl:with-param> -->
											<xsl:with-param name="onchange">msg_icon_preview_change(document.frmXml)</xsl:with-param>
										</xsl:call-template>
										<span style="font-weight:normal;">
											&#160;<xsl:value-of select="$img_des"/>
										</span>
									</label>
								</td>
							</tr>
						</table>
					</xsl:otherwise>
				</xsl:choose>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_desc"/>：
						<input type="hidden" name="lab_desc" value="{$lab_desc}"></input>
				</td>
				<td class="wzb-form-control">
					<textarea class="wzb-inputTextArea" name="art_introduction" style="width:410px;"  prompt="{$article_desc_range}">
						<xsl:value-of select="art_introduction"/>
					</textarea>
				</td>
			</tr>
	<!-- 内容 -->
			<tr>
				<td class="wzb-form-label" valign="top">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_content"/>：
						<input type="hidden" name="lab_content" value="{$lab_content}"></input>
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="kindeditor_panel">
						<xsl:with-param name="body" select="body"/>
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						<xsl:with-param name="fld_name">art_content</xsl:with-param>
						<xsl:with-param name="body">
							<xsl:value-of select="art_content"/>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_publish_mobile"/>：
				</td>
				<td class="wzb-form-control">
					<select name="art_push_mobile" class="wzb-form-select">
							<option value="1">
							<xsl:if test="art_push_mobile='1' and $ins_or_upd = 'UPD'">
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$lab_yes"/>
						</option>
						<option value="0">
							<xsl:if test="art_push_mobile='0' and $ins_or_upd = 'UPD'">
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$lab_no"/>
						</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_status"/>：
				</td>
				<td class="wzb-form-control">
					<select name="art_status" class="wzb-form-select">
							<option value="1">
							<xsl:if test="art_status='1' and $ins_or_upd = 'UPD'">
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$lab_select_on"/>
						</option>
						<option value="0">
							<xsl:if test="art_status='0' and $ins_or_upd = 'UPD'">
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$lab_select_off"/>
						</option>
					</select>
				</td>
			</tr>	
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_article_type"/>：
				</td>
				<td class="wzb-form-control">
					<select name="art_type" class="wzb-form-select"> <!--class="wzb-select" -->
							<xsl:for-each select="//aty_list/aty">
								<option value="{@id}">
									<xsl:if test="//article/art_type = @id">
									<xsl:attribute name="selected">selected</xsl:attribute>
								</xsl:if>
									<xsl:value-of select="@title"/>
								</option>
							</xsl:for-each>
					</select>
				</td>
			</tr>
			
			<!-- tc select -->
			<xsl:if test="$tc_enabled='true'">
				<tr>
					<td class="wzb-form-label">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_tc"/>：
					</td>
					
					<xsl:variable name="cur_tcr_id">
						<xsl:choose>
							<xsl:when test="not(//default_training_center)"><xsl:value-of select="//article/art_tcr_id"/></xsl:when>
							<xsl:otherwise><xsl:value-of select="//default_training_center/@id"/></xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="cur_tcr_title">
						<xsl:choose>
							<xsl:when test="not(//default_training_center)"><xsl:value-of select="//article/tc_training_center/tcr_title"/></xsl:when>
							<xsl:otherwise><xsl:value-of select="//default_training_center/title"/></xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<td class="wzb-form-control">
						<xsl:call-template name="wb_goldenman">
							<xsl:with-param name="field_name">tcr_id</xsl:with-param>
							<xsl:with-param name="name">tcr_id</xsl:with-param>
							<xsl:with-param name="box_size">1</xsl:with-param>
							<xsl:with-param name="tree_type">training_center</xsl:with-param>
							<xsl:with-param name="select_type">2</xsl:with-param>
							<xsl:with-param name="pick_leave">0</xsl:with-param>
							<xsl:with-param name="pick_root">0</xsl:with-param>
							<xsl:with-param name="single_option_value"><xsl:value-of select="$cur_tcr_id"/></xsl:with-param>
							<xsl:with-param name="single_option_text"><xsl:value-of select="$cur_tcr_title"/></xsl:with-param>
						</xsl:call-template>
					</td>
					<input type="hidden" name="art_tcr_id"/>
				</tr>
			</xsl:if>			


			<tr>
				<td class="wzb-form-label">
				</td>
				<td class="wzb-ui-module-text">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		</table>
	<!-- =============================================================== -->
								
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:wbarticle.ins_upd_article(document.frmXml,  '<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
			</xsl:call-template>
		</div>
		
	</xsl:template>
	
	<!-- =============================================================== -->
	
</xsl:stylesheet>


