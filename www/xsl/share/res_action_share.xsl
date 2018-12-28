<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../utils/wb_css.xsl"/>
	<xsl:import href="../utils/escape_js.xsl"/>
	<xsl:import href="../utils/display_filetype_icon.xsl"/>
	<xsl:import href="../utils/escape_doub_quo.xsl"/>
	<xsl:import href="../utils/wb_gen_form_button.xsl"/>
	<xsl:import href="../utils/wb_ui_line.xsl"/>
	<xsl:import href="../utils/wb_ui_head.xsl"/>
	<xsl:import href="../utils/wb_ui_space.xsl"/>
	<xsl:import href="../utils/wb_ui_hdr.xsl"/>
	<xsl:import href="../utils/wb_ui_title.xsl"/>		
	<xsl:import href="../utils/wb_ui_nav_link.xsl"/>	
	<xsl:import href="res_label_share.xsl"/>	
	<!-- =============================================================== -->
	<xsl:variable name="res_id" select="/resource/@id"/>
	<xsl:variable name="timestamp" select="/resource/@timestamp"/>
	<xsl:variable name="subtype" select="/resource/header/@subtype"/>
	<!-- =============================================================== -->
	<xsl:template name="html_header">
		<xsl:param name="src"/>
		<xsl:param name="mode"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script type="text/javascript" src="{$wb_js_path}gen_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_question.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_resource.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}urlparam.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_media.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_utils.js" language="JavaScript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<xsl:choose>
				<xsl:when test="$mode='INS'">
					<script language="JavaScript"><![CDATA[			
				res = new wbResource
				sub_type = getUrlParam('sub_type')
												
				function init(){
					return;									
				}
			]]></script>
				</xsl:when>
				<xsl:otherwise>
					<script language="JavaScript"><![CDATA[			
				res = new wbResource	
				function read_res() {
					res.get_in_search(']]><xsl:value-of select="/resource/@id"/><![CDATA[')
				}
								
				function init(){
					return;
				}
			]]></script>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:variable name="src_escaped">
				<xsl:call-template name="escape_js">
					<xsl:with-param name="input_str">
						<xsl:value-of select="$src"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:variable>
			<script language="Javascript"><![CDATA[
				function feeddata(){
					frm= document.frmXml;
						frm.res_src_link.value=']]><xsl:value-of select="$src_escaped"/><![CDATA[';
						frm.res_src_type.value=']]><xsl:value-of select="/resource/body/source/@type"/><![CDATA[';
				}
				
				function radioChange(){
					var mode = ']]><xsl:value-of select="$mode"/><![CDATA[';
					frm= document.frmXml;
					if(frm.res_format[0].checked){
						frm.upload_file.disabled = true;
						frm.upload_zipfile.disabled = true;
						if(mode !== 'INS') {
							frm.res_zipfile1.disabled = true;	
						}
						frm.res_url.disabled = false;
					}
					else if(frm.res_format[1].checked){
						frm.upload_file.disabled = false;
						frm.upload_zipfile.disabled = true;
						if(mode !== 'INS') {
							frm.res_zipfile1.disabled = true;	
						}
						frm.res_url.disabled = true;
					}
					else if(frm.res_format[2].checked){
						frm.upload_file.disabled = true;
						frm.upload_zipfile.disabled = false;
						if(mode !== 'INS') {
							frm.res_zipfile1.disabled = false;	
						}	
						frm.res_url.disabled = true;
					}
					else if(frm.res_format[3].checked){
						frm.upload_file.disabled = true;
						frm.upload_zipfile.disabled = true;
						if(mode !== 'INS') {
							frm.res_zipfile1.disabled = true;	
						}
						frm.res_url.disabled = true;
					}
				}
				
				]]></script>
		</head>
	</xsl:template>
	<!-- ================================================================================================== -->
	<xsl:template name="res_body">
		<xsl:param name="width"/>
		<xsl:param name="mode"/>
		<xsl:param name="url_success"/>
		<xsl:param name="url_failure"/>
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:call-template name="res_body_">
					<xsl:with-param name="res_header">資源</xsl:with-param>
					<xsl:with-param name="res_change_to">更改檔案為: </xsl:with-param>
					<xsl:with-param name="res_change_to_wizpack">更改wizPack為: </xsl:with-param>
					<xsl:with-param name="res_change_to_zipfile">更改Zip檔案為: </xsl:with-param>
					<xsl:with-param name="res_keep_file">保留檔案: </xsl:with-param>
					<xsl:with-param name="res_keep_wizpack">保留wizPack: </xsl:with-param>
					<xsl:with-param name="res_keep_zipfile">保留Zip檔案: </xsl:with-param>
					<xsl:with-param name="res_first_page">Zip檔案中首頁檔案: </xsl:with-param>
					<xsl:with-param name="res_source">來源</xsl:with-param>
					<xsl:with-param name="res_annotation">註釋</xsl:with-param>
					<xsl:with-param name="res_annotation_desc">註釋只會顯示在顯示樣板選擇為“Annotated Browser”時才會顯示在頁面左側。請注意註釋的長度不能超過1000個字元。</xsl:with-param>
					<xsl:with-param name="res_upload_file">上傳檔案: </xsl:with-param>
					<xsl:with-param name="res_upload_wizpack">上傳wizPack: </xsl:with-param>
					<xsl:with-param name="res_upload_zipfile">上傳Zip檔案: </xsl:with-param>
					<xsl:with-param name="res_as_html">將內容以HTML處理</xsl:with-param>
					<xsl:with-param name="res_upload_url">網址: </xsl:with-param>
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="url_success" select="$url_success"/>
					<xsl:with-param name="url_failure" select="$url_failure"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$wb_lang='gb'">
				<xsl:call-template name="res_body_">
					<xsl:with-param name="res_header">资源</xsl:with-param>
					<xsl:with-param name="res_keep_file">保留文档: </xsl:with-param>
					<xsl:with-param name="res_keep_wizpack">保留wizPack: </xsl:with-param>
					<xsl:with-param name="res_keep_zipfile">保留Zip文档: </xsl:with-param>
					<xsl:with-param name="res_first_page">Zip文档中首页文档: </xsl:with-param>
					<xsl:with-param name="res_change_to">更改文档为: </xsl:with-param>
					<xsl:with-param name="res_change_to_wizpack">更改wizPack为: </xsl:with-param>
					<xsl:with-param name="res_change_to_zipfile">更改Zip文档为: </xsl:with-param>
					<xsl:with-param name="res_source">来源</xsl:with-param>
					<xsl:with-param name="res_annotation">注释</xsl:with-param>
					<xsl:with-param name="res_annotation_desc">当您选择“Annotated Browser”作为课程内容的显示模板时，以上注释将会显示页面左侧。请注意注释的长度不能超过1000。</xsl:with-param>
					<xsl:with-param name="res_upload_file">上传文档: </xsl:with-param>
					<xsl:with-param name="res_upload_wizpack">上传wizPack: </xsl:with-param>
					<xsl:with-param name="res_upload_zipfile">上传zip文档: </xsl:with-param>
					<xsl:with-param name="res_as_html">将内容以HTML处理</xsl:with-param>
					<xsl:with-param name="res_upload_url">网址: </xsl:with-param>
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="url_success" select="$url_success"/>
					<xsl:with-param name="url_failure" select="$url_failure"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="res_body_">
					<xsl:with-param name="res_header">Resource</xsl:with-param>
					<xsl:with-param name="res_keep_file">Keep file:</xsl:with-param>
					<xsl:with-param name="res_keep_wizpack">Keep wizPack:</xsl:with-param>
					<xsl:with-param name="res_keep_zipfile">Keep zip file:</xsl:with-param>
					<xsl:with-param name="res_first_page">Index file of Zip files:</xsl:with-param>
					<xsl:with-param name="res_change_to">Change file to:</xsl:with-param>
					<xsl:with-param name="res_change_to_wizpack">Change wizPack to:</xsl:with-param>
					<xsl:with-param name="res_change_to_zipfile">Change zip file to:</xsl:with-param>
					<xsl:with-param name="res_source">Source</xsl:with-param>
					<xsl:with-param name="res_annotation">Annotation</xsl:with-param>
					<xsl:with-param name="res_annotation_desc">This annotation will appear only when the “Annotated browser” presentation template has been selected in displaying this item as a course content. Use no more than 1000 characters.</xsl:with-param>
					<xsl:with-param name="res_upload_file">Upload file:</xsl:with-param>
					<xsl:with-param name="res_upload_wizpack">Upload wizPack:</xsl:with-param>
					<xsl:with-param name="res_upload_zipfile">Upload zip file:</xsl:with-param>
					<xsl:with-param name="res_as_html">Treat content as HTML</xsl:with-param>
					<xsl:with-param name="res_upload_url">URL:</xsl:with-param>
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="url_success" select="$url_success"/>
					<xsl:with-param name="url_failure" select="$url_failure"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="res_body_">
		<xsl:param name="width"/>
		<xsl:param name="mode"/>
		<xsl:param name="url_success"/>
		<xsl:param name="url_failure"/>
		<xsl:param name="res_header"/>
		<xsl:param name="res_change_to"/>
		<xsl:param name="res_keep_file"/>
		<xsl:param name="res_source"/>
		<xsl:param name="res_annotation"/>
		<xsl:param name="res_annotation_desc"/>
		<xsl:param name="res_upload_file"/>
		<xsl:param name="res_as_html"/>
		<xsl:param name="res_upload_url"/>
		<xsl:param name="res_upload_zipfile"/>
		<xsl:param name="res_first_page"/>
		<xsl:param name="res_change_to_zipfile"/>
		<xsl:param name="res_keep_wizpack"/>
		<xsl:param name="res_keep_zipfile"/>
		<input type="hidden" name="res_src_type"/>
		<xsl:choose>
			<xsl:when test="$mode='INS'">
				<input type="hidden" name="cmd" value="ins_res"/>
			</xsl:when>
			<xsl:when test="$mode='UPD'">
				<input type="hidden" name="cmd" value="upd_res"/>
				<input type="hidden" name="res_id" value="{$res_id}"/>
				<input type="hidden" name="res_timestamp" value="{$timestamp}"/>
				<input type="hidden" name="res_subtype" value="{$subtype}"/>
			</xsl:when>
			<xsl:when test="$mode='PST'">
				<input type="hidden" name="cmd" value="ins_res"/>
				<input type="hidden" name="res_id" value="{$res_id}"/>
				<input type="hidden" name="res_timestamp" value="{$timestamp}"/>
				<input type="hidden" name="res_subtype" value="{$subtype}"/>
				<input type="hidden" name="copy_media_from" value="{$res_id}"/>
			</xsl:when>
		</xsl:choose>
		<input type="hidden" name="res_type" value="GEN"/>
		<input type="hidden" name="obj_id"/>
		<input type="hidden" name="annotation_html"/>
		<input type="hidden" name="res_src_link"/>
		<input type="hidden" name="zip_filename"/>
		<input type="hidden" name="url_failure" value="{$url_failure}"/>
		<input type="hidden" name="url_success" value="{$url_success}"/>
		<xsl:choose>
			<xsl:when test="$mode='INS'">
				<input type="hidden" name="res_subtype" value="WCT"/>
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text" select="$res_source"/>
					<xsl:with-param name="width" select="$width"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text" select="$res_source"/>
					<xsl:with-param name="width" select="$width"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<table border="0" cellpadding="0" cellspacing="0" width="{$width}" class="Bg">
			<tr>
				<td>
					<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
				</td>
			</tr>
			<tr>
				<td>
					<table border="0" cellpadding="5" cellspacing="0" width="100%" class="Bg">
						<tr>
							<td align="right" width="150">
								<input>
									<xsl:attribute name="type">radio</xsl:attribute>
									<xsl:attribute name="name">res_format</xsl:attribute>
									<xsl:attribute name="value">1</xsl:attribute>
									<xsl:if test="/resource/body/source/@type = 'URL' or $mode= 'INS' ">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
									<xsl:attribute name="onclick">javascript:radioChange()</xsl:attribute>
								</input>
								<span class="Text">
									<xsl:value-of select="$res_upload_url"/>
								</span>
							</td>
							<td align="left">
								<input class="wzb-inputText" type="text" name="res_url" style="width:360px;" onfocus="this.form.res_format[0].checked=true;radioChange();">
									<xsl:attribute name="value"><xsl:choose><xsl:when test="/resource/body/source/@type = 'URL'"><xsl:value-of select="/resource/body/source"/></xsl:when><xsl:otherwise>http://</xsl:otherwise></xsl:choose></xsl:attribute>
									<xsl:if test="/resource/body/source/@type != 'URL' and $mode != 'INS' ">
										<xsl:attribute name="disabled">disabled</xsl:attribute>
									</xsl:if>
								</input>
							</td>
						</tr>
						<xsl:choose>
							<xsl:when test="$mode = 'INS'">
								<tr>
									<td align="right" width="150">
										<input type="radio" name="res_format" value="2" onclick="radioChange()"/>
										<span class="Text">
											<xsl:value-of select="$res_upload_file"/>
										</span>
									</td>
									<td align="left">
										<input class="wzb-inputText" type="file" name="upload_file" style="width:360px;" onfocus="this.form.res_format[1].checked=true"/>
									</td>
								</tr>
								<tr>
									<td align="right">
										<input type="radio" name="res_format" value="3" onclick="radioChange()"/>
										<span class="Text">
											<xsl:value-of select="$res_upload_zipfile"/>
										</span>
									</td>
									<td align="left">
										<input class="wzb-inputText" type="file" style="width:360px;" name="upload_zipfile" onfocus="this.form.res_format[2].checked=true;radioChange();"/>
									</td>
								</tr>
								<tr>
									<td align="right">
										<span class="Text">
											<xsl:value-of select="$res_first_page"/>
										</span>
									</td>
									<td align="left">
										<input class="wzb-inputText" type="text" name="res_zipfile" style="width:360px;" onfocus="this.form.res_format[2].checked=true;radioChange();"/>
									</td>
								</tr>
							</xsl:when>
							<xsl:otherwise>
								<input type="hidden" name="res_zipfile"/>
								<tr>
									<td align="right" width="150">
										<input>
											<xsl:attribute name="type">radio</xsl:attribute>
											<xsl:attribute name="name">res_format</xsl:attribute>
											<xsl:attribute name="value">2</xsl:attribute>
											<xsl:attribute name="onclick">javascript:radioChange()</xsl:attribute>
										</input>
										<xsl:choose>
											<xsl:when test="/resource/body/source/@type = 'FILE'">
												<span class="Text">
													<xsl:value-of select="$res_change_to"/>
												</span>
											</xsl:when>
											<xsl:otherwise>
												<span class="Text">
													<xsl:value-of select="$res_upload_file"/>
												</span>
											</xsl:otherwise>
										</xsl:choose>
									</td>
									<td align="left">
										<input class="wzb-inputText" type="file" name="upload_file" style="width:360px;" onfocus="this.form.res_format[1].checked=true" disabled="disabled"/>
									</td>
								</tr>
								<tr>
									<td align="right">
										<input>
											<xsl:attribute name="type">radio</xsl:attribute>
											<xsl:attribute name="name">res_format</xsl:attribute>
											<xsl:attribute name="value">4</xsl:attribute>
											<xsl:attribute name="onclick">javascript:radioChange()</xsl:attribute>
										</input>
										<xsl:choose>
											<xsl:when test="/resource/body/source/@type='ZIPFILE'">
												<span class="Text">
													<xsl:value-of select="$res_change_to_zipfile"/>
												</span>
											</xsl:when>
											<xsl:otherwise>
												<span class="Text">
													<xsl:value-of select="$res_upload_zipfile"/>
												</span>
											</xsl:otherwise>
										</xsl:choose>
									</td>
									<td align="left">
										<input class="wzb-inputText" type="file" style="width:360px;" name="upload_zipfile" onfocus="this.form.res_format[2].checked=true" disabled="disabled"/>
									</td>
								</tr>
								<tr>
									<td align="right">
										<span class="Text">
											<xsl:value-of select="$res_first_page"/>
										</span>
									</td>
									<td align="left">
										<input class="wzb-inputText" type="text" name="res_zipfile1" style="width:360px;" onfocus="this.form.res_format[2].checked=true" disabled="disabled"/>
									</td>
								</tr>
								<tr>
									<xsl:choose>
										<xsl:when test="/resource/body/source/@type = 'WIZPACK'">
											<td align="right" width="150">
												<input type="radio" name="res_format" value="4" checked="checked"/>
												<span class="Text">
													<xsl:value-of select="$res_keep_wizpack"/>
												</span>
											</td>
										</xsl:when>
										<xsl:when test="/resource/body/source/@type = 'FILE'">
											<td align="right">
												<input type="radio" name="res_format" value="4" checked="checked" onclick="radioChange()"/>
												<span class="Text">
													<xsl:value-of select="$res_keep_file"/>
												</span>
											</td>
										</xsl:when>
										<xsl:when test="/resource/body/source/@type = 'ZIPFILE'">
											<td align="right">
												<input type="radio" name="res_format" value="5" checked="checked" onclick="radioChange()"/>
												<span class="Text">
													<xsl:value-of select="$res_keep_zipfile"/>
												</span>
											</td>
										</xsl:when>
										<xsl:otherwise>
											<td>
												<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
											</td>
										</xsl:otherwise>
									</xsl:choose>
									<xsl:variable name="fileName">
										<xsl:value-of select="/resource/body/source"/>
									</xsl:variable>
									<xsl:variable name="URL">
										<xsl:value-of select="/resource/body/source"/>
									</xsl:variable>
									<xsl:variable name="fullPath">../<xsl:value-of select="concat('resource/', /resource/@id, '/', $fileName)"/>
									</xsl:variable>
									<td align="left">
										<xsl:choose>
											<xsl:when test="/resource/body/source/@type != 'URL'">
												<xsl:choose>
													<xsl:when test="/resource/body/source/@type = 'WIZPACK'">
														<a href="javascript:res.read_wizpack({/resource/@id})">
															<img src="{$wb_img_path}icon_web_browser.gif" border="0" hspace="4" vspace="4"/>
														</a>
													</xsl:when>
													<xsl:when test="/resource/body/source/@type = 'AICC_FILES'">
														<img src="{$wb_img_path}icol_ssc.gif" border="0" hspace="4" vspace="4"/>
													</xsl:when>
													<xsl:otherwise>
														<a href="{$fullPath}" target="_blank">
															<xsl:call-template name="display_filetype_icon">
																<xsl:with-param name="fileName">
																	<xsl:value-of select="$fileName"/>
																</xsl:with-param>
															</xsl:call-template>
														</a>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:when>
											<xsl:otherwise>
												<a href="{$URL}" target="_blank">
													<img src="{$wb_img_path}icon_web_browser.gif" border="0" hspace="4" vspace="4"/>
												</a>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
								<xsl:if test="/resource/body/source/@type='ZIPFILE'">
									<tr>
										<td align="right">
											<span class="Text">
												<xsl:value-of select="$res_first_page"/>
											</span>
										</td>
										<td align="left">
											<input class="wzb-inputText" type="text" name="res_zipfile2" value="{/resource/body/source}" style="width:360px;" onfocus="this.form.res_format[3].checked=true;radioChange();"/>
										</td>
									</tr>
								</xsl:if>
								<tr>
									<xsl:choose>
										<xsl:when test="/resource/body/source/@type != 'URL'">
											<td width="150">
												<img src="{$wb_img_path}tp.gif" width="10" height="1" border="0"/>
											</td>
											<td align="center">
												<xsl:choose>
													<xsl:when test="/resource/body/source/@type = 'FILE' or (/resource/body/source/@type != 'AICC_FILES' and /resource/body/source/@type != 'WIZPACK')">&#160;</xsl:when>
													<xsl:otherwise>
														<span class="Text">
															<xsl:value-of select="/resource/body/source"/>
														</span>
													</xsl:otherwise>
												</xsl:choose>
											</td>
										</xsl:when>
										<xsl:otherwise>
											<xsl:variable name="URL">
												<xsl:value-of select="/resource/body/source"/>
											</xsl:variable>
											<td>
												<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
											</td>
											<td>
												<a class="Text" href="{$URL}" target="_blank">
													<xsl:value-of select="/resource/body/source"/>
												</a>
											</td>
										</xsl:otherwise>
									</xsl:choose>
								</tr>
							</xsl:otherwise>
						</xsl:choose>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_space">
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$res_annotation"/>
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<table border="0" cellpadding="0" cellspacing="0" width="{$width}" class="Bg">
			<tr>
				<td>
					<img border="0" height="10" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
			<tr>
				<td>
					<table cellpadding="5" cellspacing="0" border="0" width="100%">
						<tr>
							<td>
								<div>
								<textarea name="res_annotation" style="width:100%;" rows="6" class="wzb-inputTextArea" cols="50">
									<xsl:choose>
										<xsl:when test="/resource/body/annotation/html">
											<xsl:value-of select="/resource/body/annotation/html"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="/resource/body/annotation"/>
										</xsl:otherwise>
									</xsl:choose>
								</textarea>
								</div>
								<div>
									<span class="Text">
										<xsl:value-of select="$res_annotation_desc"/>
									</span>
								</div>
								<div>
									<input type="checkbox" name="asHTML" id="_as_html">
										<xsl:if test="/resource/body/annotation/html">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<label for="_as_html">
										<span class="Text">
											<xsl:value-of select="$res_as_html"/>
										</span>
									</label>
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<img border="0" height="10" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- ================================================================================================== -->
	<xsl:template name="res_additional_information">
		<xsl:param name="mode"/>
		<xsl:param name="width"/>
		<xsl:param name="save_function"/>
		<xsl:param name="cancel_function"/>
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:call-template name="res_additional_information_">
					<xsl:with-param name="res_add_info">其他資料</xsl:with-param>
					<xsl:with-param name="res_title_keyword">標題：</xsl:with-param>
					<xsl:with-param name="res_desc">簡介：</xsl:with-param>
					<xsl:with-param name="res_diff">難度：</xsl:with-param>
					<xsl:with-param name="res_diff_easy">容易</xsl:with-param>
					<xsl:with-param name="res_diff_normal">一般</xsl:with-param>
					<xsl:with-param name="res_diff_hard">困難</xsl:with-param>
					<xsl:with-param name="res_status">狀態：</xsl:with-param>
					<xsl:with-param name="res_status_on">在線</xsl:with-param>
					<xsl:with-param name="res_status_off">離線</xsl:with-param>
					<xsl:with-param name="lab_please_select">-請選擇-</xsl:with-param>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="save_function" select="$save_function"/>
					<xsl:with-param name="cancel_function" select="$cancel_function"/>
					<xsl:with-param name="lab_g_form_btn_save">儲存</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$wb_lang='gb'">
				<xsl:call-template name="res_additional_information_">
					<xsl:with-param name="res_add_info">其他资料</xsl:with-param>
					<xsl:with-param name="res_title_keyword">标题：</xsl:with-param>
					<xsl:with-param name="res_desc">简介：</xsl:with-param>
					<xsl:with-param name="res_diff">难度：</xsl:with-param>
					<xsl:with-param name="res_diff_easy">容易</xsl:with-param>
					<xsl:with-param name="res_diff_normal">一般</xsl:with-param>
					<xsl:with-param name="res_diff_hard">困难</xsl:with-param>
					<xsl:with-param name="res_status">状态：</xsl:with-param>
					<xsl:with-param name="res_status_on">在线</xsl:with-param>
					<xsl:with-param name="res_status_off">离线</xsl:with-param>
					<xsl:with-param name="lab_please_select">-请选择-</xsl:with-param>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="save_function" select="$save_function"/>
					<xsl:with-param name="cancel_function" select="$cancel_function"/>
					<xsl:with-param name="lab_g_form_btn_save">保存</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="res_additional_information_">
					<xsl:with-param name="res_add_info">Basic information</xsl:with-param>
					<xsl:with-param name="res_title_keyword">Title:</xsl:with-param>
					<xsl:with-param name="res_desc">Description:</xsl:with-param>
					<xsl:with-param name="res_diff">Difficulty:</xsl:with-param>
					<xsl:with-param name="res_diff_easy">Easy</xsl:with-param>
					<xsl:with-param name="res_diff_normal">Normal</xsl:with-param>
					<xsl:with-param name="res_diff_hard">Hard</xsl:with-param>
					<xsl:with-param name="res_status">Status:</xsl:with-param>
					<xsl:with-param name="res_status_on">Online</xsl:with-param>
					<xsl:with-param name="res_status_off">Offiline</xsl:with-param>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="lab_please_select">-Please select-</xsl:with-param>
					<xsl:with-param name="save_function" select="$save_function"/>
					<xsl:with-param name="cancel_function" select="$cancel_function"/>
					<xsl:with-param name="lab_g_form_btn_save">Save</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="res_additional_information_">
	<xsl:param name="res_add_info"/>
	<xsl:param name="res_title_keyword"/>
	<xsl:param name="res_desc"/>
	<xsl:param name="res_diff"/>
	<xsl:param name="res_diff_easy"/>
	<xsl:param name="res_diff_normal"/>
	<xsl:param name="res_diff_hard"/>
	<xsl:param name="res_status"/>
	<xsl:param name="res_status_on"/>
	<xsl:param name="res_status_off"/>
	<xsl:param name="mode"/>
	<xsl:param name="width"/>
	<xsl:param name="lab_please_select"/>
	<xsl:param name="save_function"/>
	<xsl:param name="cancel_function"/>
	<xsl:param name="lab_g_form_btn_save"/>
	<xsl:param name="lab_g_form_btn_cancel"/>
		
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$res_add_info"/>
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<table>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$res_title_keyword"/>
				</td>
				<td class="wzb-form-control">
					<input class="wzb-inputText" type="text" name="res_title" style="width:380px;" maxlength="255"/>
					<xsl:variable name="escaped_title">
						<xsl:call-template name="escape_doub_quo">
							<xsl:with-param name="my_right_value">
								<xsl:value-of select="/resource/body/title"/>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:variable>
					<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[document.frmXml.res_title.value="]]><xsl:value-of select="$escaped_title"/><![CDATA[";]]></SCRIPT>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$res_desc"/>
				</td>
				<td class="wzb-form-control">
					<textarea class="wzb-inputTextArea" rows="4" cols="65" style="width:380px;" name="res_desc">
						<xsl:value-of select="/resource/body/desc"/>
					</textarea>
				</td>
			</tr>
				<xsl:choose>
					<xsl:when test="$mode='INS'">
						<input class="wzb-inputText" type="hidden" name="res_duration" value="1.0" size="5"/>
						<input type="hidden" name="res_privilege" value="CW"/>
						<input type="hidden" name="res_lan" value="ISO-8859-1"/>
					</xsl:when>
					<xsl:otherwise>
						<input class="wzb-inputText" type="hidden" name="res_duration" value="{/resource/header/@duration}" size="5"/>
						<input type="hidden" name="res_privilege" value="{/resource/header/@privilege}"/>
						<input type="hidden" name="res_lan" value="{/resource/@language}"/>
					</xsl:otherwise>
				</xsl:choose>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_save"/>
				<xsl:with-param name="wb_gen_btn_href" select="$save_function"/>
				<xsl:with-param name="wb_gen_btn_target">_parent</xsl:with-param>
				<xsl:with-param name="id">submitButton</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href" select="$cancel_function"/>
				<xsl:with-param name="wb_gen_btn_target">_parent</xsl:with-param>
				<xsl:with-param name="id">cancelButton</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- ================================================================================================== -->
	<xsl:template name="draw_header">
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:call-template name="dh_lang_ch"/>
			</xsl:when>
			<xsl:when test="$wb_lang ='gb'">
				<xsl:call-template name="dh_lang_gb"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="dh_lang_en"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ================================================================================================== -->
	<xsl:template name="dh_lang_ch">
		<xsl:call-template name="draw_header_content">
			<xsl:with-param name="res_search_result">搜索結果</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="dh_lang_gb">
		<xsl:call-template name="draw_header_content">
			<xsl:with-param name="res_search_result">搜索结果</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="dh_lang_en">
		<xsl:call-template name="draw_header_content">
			<xsl:with-param name="res_search_result">Search Result</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ================================================================================================== -->
	<xsl:template name="draw_header_content">
		<xsl:param name="res_search_result"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="//question/header/title">
						<xsl:value-of select="//question/header/title"/>
					</xsl:when>
					<xsl:when test="//resource/body/title">
						<xsl:value-of select="//resource/body/title"/>
					</xsl:when>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<span class="NavLink">
					<a href="javascript:window.location.href=wb_utils_get_cookie('search_result_url')" class="NavLink">
						<xsl:value-of select="$res_search_result"/>
					</a>
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:choose>
						<xsl:when test="//question/header/title">
							<xsl:value-of select="//question/header/title"/>
						</xsl:when>
						<xsl:when test="//resource/body/title">
							<xsl:value-of select="//resource/body/title"/>
						</xsl:when>
					</xsl:choose>
				</span>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ================================================================================================== -->
</xsl:stylesheet>
