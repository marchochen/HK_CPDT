<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="utils/display_filetype_icon.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>	
	<xsl:import href="utils/kindeditor.xsl"/>	
	<xsl:import href="utils/display_form_input_time.xsl"/>	
	<xsl:import href="utils/escape_js.xsl"/>	 
	<xsl:import href="share/wb_module_type_const.xsl"/>	
	<!--=========================================================-->
	<xsl:template name="table_seperator">
		<tr>
			<td width="20%" align="right" height="10">
			</td>
			<td width="80%" height="10">
			</td>
		</tr>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_time_event">
		<xsl:param name="lab_duration"/>
		<xsl:param name="lab_mins"/>
		<xsl:param name="lab_time_limit"/>
		<xsl:param name="lab_suggested_time"/>
		<xsl:param name="dur">30</xsl:param>
		<xsl:if test="display/option/progress/@duration = 'true'">
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_duration"/>:</span>
				</td>
				<td width="80%">
					<span class="Text">
						<input size="4" name="mod_duration" type="text" value="{$dur}" class="wzb-inputText"/>
							&#160;<xsl:value-of select="$lab_mins"/>
					</span>
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="display/option/progress/@time_limit = 'true'">
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_time_limit"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td width="80%">
					<span class="Text">
						<input size="4" name="mod_duration" type="text" value="{$dur}" class="wzb-inputText"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$lab_mins"/>
					</span>
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="display/option/progress/@suggested_time = 'true'">
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_suggested_time"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td width="80%">
					<span class="Text">
						<input size="4" name="mod_duration" type="text" value="{$dur}" class="wzb-inputText"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$lab_mins"/>
					</span>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_mod_type">
		<xsl:param name="lab_type"/>
		<xsl:param name="mod_type"/>
		<tr>
			<td width="20%" align="right" valign="top">
				<span class="TitleText">
					<xsl:value-of select="$lab_type"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="80%" align="left">
				<span class="Text">
					<xsl:call-template name="return_module_label">
						<xsl:with-param name="mod_type" select="$mod_type"/>
					</xsl:call-template>
				</span>
			</td>
		</tr>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_title">
		<xsl:param name="lab_title"/>
		<xsl:param name="value"/>
		<xsl:param name="isEnrollment_related">false</xsl:param>
		<xsl:if test="display/option/general/@title = 'true'">
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_title"/>:</span>
				</td>
				<xsl:choose>
					<xsl:when test="$isEnrollment_related = 'true'">
						<td width="80%">
							<span class="Text">
								<xsl:value-of select="$value"/>
							</span>
						</td>
						<input value="{$value}" name="mod_title" type="hidden"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="escaped_mod_title">
							<xsl:choose>
								<xsl:when test="$value != ''">
									<xsl:call-template name="escape_js">
										<xsl:with-param name="input_str">
											<xsl:value-of select="$value"/>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise></xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<td width="80%">
							<input class="wzb-inputText" value="{$escaped_mod_title}" size="35" name="mod_title" type="text" style="width:300px;" maxlength="255"/>
						</td>

					</xsl:otherwise>
				</xsl:choose>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_duration">
		<xsl:param name="lab_title"/>
		<xsl:param name="value"/>
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_title"/>:</span>
				</td>
				<td width="80%">
					<input class="wzb-inputText" value="{$value}" size="35" name="mod_vod_duration" type="text" style="width:300px;" maxlength="120"/>
				</td>
			</tr>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_desc">
		<xsl:param name="lab_desc"/>
		<xsl:param name="value"/>
		<xsl:if test="display/option/general/@desc = 'true'">
			<tr>
				<td width="20%" valign="top" align="right">
					<span class="TitleText">
						<xsl:value-of select="$lab_desc"/>:</span>
				</td>
				<td width="80%">
					<textarea rows="6" wrap="VIRTUAL" style="width:300px;" cols="35" name="mod_desc" class="wzb-inputTextArea">
						<xsl:value-of select="$value"/>
					</textarea>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_req_time">
		<xsl:param name="lab_req_time" />
		<xsl:param name="value" />
		<xsl:if test="display/option/general/@desc = 'true'">
			<tr>
				<td width="20%" valign="top" align="right">
					<span class="TitleText"><xsl:value-of select="$lab_req_time"/>:</span>
				</td>
				<td width="80%">
					<span class="TitleText">
						<input type="text" name="mod_required_time" class="wzb-inputText" style="width:300px;" value="{$value}" />
					</span>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_download">
		<xsl:param name="lab_download" />
		<xsl:param name="lab_prohibited" />
		<xsl:param name="lab_allow" />
		<xsl:param name="download_ind" />
		<xsl:if test="display/option/general/@desc = 'true'">
			<tr>
				<td width="20%" valign="top" align="right">
					<span class="TitleText"><xsl:value-of select="$lab_download"/>:</span>
				</td>
				<td width="80%">
					<span class="TitleText">
					     <!-- <input type="radio" name="mod_download_ind" value="0" />
							<xsl:value-of select="$lab_prohibited"/><br />
						<input type="radio" name="mod_download_ind" value="1" checked="checked"/>
							<xsl:value-of select="$lab_allow"/> -->
						 <xsl:choose>
							<xsl:when test="$download_ind = 0">
								<input type="radio" name="mod_download_ind" value="0" checked="checked"/>
									<xsl:value-of select="$lab_prohibited"/><br />
								<input type="radio" name="mod_download_ind" value="1"/>
									<xsl:value-of select="$lab_allow"/>
							</xsl:when>
							<xsl:otherwise>
								<input type="radio" name="mod_download_ind" value="0"/>
									<xsl:value-of select="$lab_prohibited"/><br />
								<input type="radio" name="mod_download_ind" value="1" checked="checked"/>
									<xsl:value-of select="$lab_allow"/>
							</xsl:otherwise>
						</xsl:choose> 
					</span>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_push_to_mobile">
		<xsl:param name="lab_push_to_mobile" />
		<xsl:param name="lab_no" />
		<xsl:param name="lab_yes" />
		<xsl:param name="mod_mobile_ind">1</xsl:param>
		
        <xsl:param name="lal_push_mobile_tip">
            <xsl:choose>
                 <xsl:when test="//cur_usr/@curLan='zh-cn'">
                                                                              请注意，如果要发布到移动端，为了不影响正常学习，要先确保你的学习资料能在移动端正常播放。
                 </xsl:when>
                 <xsl:when test="//cur_usr/@curLan='zh-hk'">
                                                                              請注意，如果要發佈到移動端，為了不影響正常學習，要先確保你的學習資料能在移動端正常播放。
                 </xsl:when>
                 <xsl:otherwise>
                      Please make sure that the material can be viewed under mobile device.
                 </xsl:otherwise>
            </xsl:choose>
        </xsl:param>
			<tr>
				<td width="20%" valign="top" align="right">
					<span class="TitleText"><xsl:value-of select="$lab_push_to_mobile"/>:</span>
				</td>
				<td width="80%">
					<span class="TitleText">
						<xsl:choose>
							<xsl:when test="$mod_mobile_ind = 1">
								<input type="radio" name="mod_mobile_ind" value="1" checked="checked"/>
									<xsl:value-of select="$lab_yes"/><br />
								<input type="radio" name="mod_mobile_ind" value="0"/>
									<xsl:value-of select="$lab_no"/>
							</xsl:when>
							<xsl:otherwise>
								<input type="radio" name="mod_mobile_ind" value="1"/>
									<xsl:value-of select="$lab_yes"/><br />
								<input type="radio" name="mod_mobile_ind" value="0"  checked="checked"/>
									<xsl:value-of select="$lab_no"/>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
			</tr>
			<tr>
			    <td width="20%" valign="top" align="right"></td>
			    <td width="80%"><span class="TitleText"><xsl:value-of select="$lal_push_mobile_tip"/></span></td>
			</tr>
	</xsl:template>
	
	
	<!--=========================================================-->
	<xsl:template name="draw_event_datetime">
		<xsl:param name="lab_evt_datetime"/>
		<xsl:param name="value"/>
		<xsl:if test="display/option/event/@datetime = 'true'">
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_evt_datetime"/>
					</span>
				</td>
				<td width="80%">
					<span class="Text">
						<xsl:call-template name="display_form_input_time">
							<xsl:with-param name="fld_name">evt</xsl:with-param>
							<xsl:with-param name="hidden_fld_name">evt_datetime</xsl:with-param>
							<xsl:with-param name="display_form_input_hhmm">Y</xsl:with-param>
							<xsl:with-param name="frm">document.frmXml</xsl:with-param>
							<xsl:with-param name="timestamp">
								<xsl:value-of select="$value"/>
							</xsl:with-param>
						</xsl:call-template>
					</span>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_event_venue">
		<xsl:param name="lab_evt_venue"/>
		<xsl:param name="value"/>
		<xsl:if test="display/option/event/@venue = 'true'">
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_evt_venue"/>
					</span>
				</td>
				<td width="80%">
					<textarea class="wzb-inputTextArea" rows="3" wrap="VIRTUAL" style="width:300px;" cols="35" name="evt_venue">
						<xsl:value-of select="$value"/>
					</textarea>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_selection_logic">
		<xsl:param name="lab_selection_logic"/>
		<xsl:param name="lab_adaptive"/>
		<xsl:param name="lab_random"/>
		<xsl:param name="value"/>
		<tr>
			<td width="20%" align="right" valign="top">
				<span class="TitleText">
					<xsl:value-of select="$lab_selection_logic"/>:</span>
			</td>
			<td width="80%" height="10">
				<span class="Text">
					<label for="dmod_logic_id_2">
						<input type="radio" name="dmod_logic" value="RND" id="dmod_logic_id_2">
							<xsl:if test="not($value = 'ADT')">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<xsl:value-of select="$lab_random"/>
					</label>
					<label for="dmod_logic_id_1">
						<input type="radio" name="dmod_logic" value="ADT" id="dmod_logic_id_1">
							<xsl:if test="$value = 'ADT'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
						<xsl:value-of select="$lab_adaptive"/>
					</label>
				</span>
			</td>
		</tr>
		<input type="hidden" name="mod_logic"/>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_cdf_file">
		<xsl:param name="lab_file_cdf"/>
		<xsl:param name="value"/>
		<tr>
			<td width="20%" align="right" valign="top">
				<!--<span class="TitleText">
					<xsl:value-of select="$lab_file_cdf"/>
					<xsl:text>：</xsl:text>
				</span>-->
			</td>
			<td width="80%">
				<span class="Text">
					<input type="radio" name="src_type" id="rdo_src_type_pick_from_file"/>
					<label for="rdo_src_type_pick_from_file">
					<xsl:value-of select="$lab_file_cdf"/></label>
				</span>
				<br/>
				<input value="" type="file" class="wzb-inputText" size="20" style="width:300px;" name="netg_cdf"/>
			</td>
		</tr>
		<input value="" name="netg_cdf_filename" type="hidden"/>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_src_wizpack">
		<xsl:param name="lab_wizpack"/>
		<xsl:param name="lab_change_to"/>
		<xsl:param name="source_type"/>
		<xsl:param name="source_link"/>
		<tr>
			<td width="20%" align="right" valign="top">
				<span class="TitleText">
				</span>
			</td>
			<td width="80%" height="10">
				<script language="JavaScript">
						if(document.frmXml.src_type){
							if(document.frmXml.src_type.length){
								var src_type_wizpack_id = document.frmXml.src_type.length;
							}else{			
							var src_type_wizpack_id = 1
							}
						}else{
							var src_type_wizpack_id = 0
						}
					</script>
				<xsl:choose>
					<xsl:when test="$source_type != 'WIZPACK'">
						<span class="Text">
							<label for="rdo_src_type_wizpack">
								<input type="radio" name="src_type" value="WIZPACK" id="rdo_src_type_wizpack"/>
								<xsl:value-of select="$lab_wizpack"/>
								<xsl:text>：</xsl:text>
								<br/>
							</label>
							<input type="file" class="wzb-inputText" value="" style="width:300px;" name="mod_wizpack" onFocus="this.form.src_type[src_type_wizpack_id].checked=true"/>
							<input type="hidden" name="org_wizpack" value=""/>
						</span>
					</xsl:when>
					<xsl:otherwise>
						<label for="rdo_src_type_wizpack">
							<input type="radio" name="src_type" value="WIZPACK" id="rdo_src_type_wizpack" checked="checked"/>
							<xsl:value-of select="$lab_wizpack"/>
							<xsl:text>：</xsl:text>
							<br/>
							<xsl:value-of select="$source_link"/>
							<br/>
						</label>
						<xsl:value-of select="$lab_change_to"/>&#160;	<input style="width:300;" type="file" class="wzb-inputText" name="mod_wizpack" onFocus="this.form.src_type[src_type_wizpack_id].checked=true"/>
						<input type="hidden" value="{$source_link}" name="org_wizpack"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_img_file">
		<xsl:param name="lab_upload_file"/>
		<xsl:param name="lab_change_to"/>
		<xsl:param name="source_link"/>
		<tr>
			<input type="hidden" name="mod_vod_img_link" />
			<input type="hidden" name="mod_vod_type" />	
			<td width="20%" align="right" valign="top">
				<span class="TitleText">
					<xsl:value-of select="$lab_upload_file"/>:</span>
			</td>
			<td width="80%" height="10">
				<xsl:choose>
					<xsl:when test="$source_link = ''">
						<span class="Text">
							<input value="" type="file" class="wzb-inputText" style="width:300px;" name="mod_vod_img"/>
							<input type="hidden" name="org_file_img" value=""/>
						</span>
					</xsl:when>
					<xsl:otherwise>
						<span class="Text">
								<a href="../resource/{/module/@id}/{$source_link}" class="Text" target="_blank">
										<xsl:call-template name="display_filetype_icon">	
											<xsl:with-param name="fileName"><xsl:value-of select="$source_link"/></xsl:with-param>
										</xsl:call-template>									
									</a>
								<br/>
							<xsl:value-of select="$lab_change_to"/>
							<xsl:text>&#160;</xsl:text>
								<input style="width:300;" type="file" class="wzb-inputText" name="mod_vod_img" />
							<input type="hidden" name="org_file_img" value="{$source_link}"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_editor">
		<xsl:param name="lab_upload_file"/>
		<tr>
			<td width="20%" align="right" valign="top">
				<span class="TitleText">
						<xsl:value-of select="$lab_upload_file"/>:</span>
			</td>
			<td width="80%" height="10">
				<xsl:call-template name="kindeditor_panel">
					<xsl:with-param name="body" select="res_vod_main"/>
					<xsl:with-param name="frm">document.frmXml</xsl:with-param>
					<xsl:with-param name="fld_name">msg_body</xsl:with-param>
					<xsl:with-param name="cols">58</xsl:with-param>
				</xsl:call-template>
			</td>
		</tr>
	</xsl:template>
	<xsl:template name="draw_src_file">
		<xsl:param name="lab_upload_file"/>
		<xsl:param name="lab_uploaded_file"/>
		<xsl:param name="lab_change_to"/>
		<xsl:param name="source_type"/>
		<xsl:param name="source_link"/>
		<script language="JavaScript">
			if(document.frmXml.src_type){
				if(document.frmXml.src_type.length){
				var src_type_file_id = document.frmXml.src_type.length;
				}else{			
				var src_type_file_id = 1
				}
			}else{
				var src_type_file_id = 0
			}			
		</script>
		<tr>
			<td class="wzb-form-label" valign="top">
			</td>
			<td class="wzb-form-control">
				<xsl:choose>
					<xsl:when test="$source_type != 'FILE'">
						<span class="Text">
							<label for="rdo_src_type_file">
								<input type="radio" name="src_type" value="FILE" id="rdo_src_type_file"/>
								<xsl:value-of select="$lab_upload_file"/>
								<xsl:text>：</xsl:text>
								<br/>
							</label>
							<input value="" type="file" class="wzb-inputText" style="width:300px;" name="mod_file" onFocus="this.form.src_type[src_type_file_id].checked=true"/>
							<input type="hidden" name="org_file" value=""/>
						</span>
					</xsl:when>
					<xsl:otherwise>
					<xsl:variable name="url_value">
						<xsl:choose>
							<xsl:when test="string-length($source_link) > 60">
								<xsl:value-of select="substring($source_link,0,60)"/>... 
				  			</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$source_link"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
						<span class="Text">
							<label for="rdo_src_type_file">
								<input type="radio" id="rdo_src_type_file" name="src_type" value="FILE" checked="checked"/>
								<xsl:value-of select="$lab_uploaded_file"/>
								<xsl:text>：</xsl:text>
								<br/>
								<xsl:choose>
									<xsl:when test="$source_type != 'URL'"><a href="../resource/{/module/@id}/{$source_link}" class="Text" target="_blank">
								<xsl:call-template name="display_filetype_icon">	
									<xsl:with-param name="fileName"><xsl:value-of select="$source_link"/></xsl:with-param>
								</xsl:call-template>									
									</a></xsl:when>
									<xsl:otherwise>
									<a href="{$source_link}" class="Text" target="_blank">
									<xsl:call-template name="display_filetype_icon">	
									<xsl:with-param name="fileName"><xsl:value-of select="$source_link"/></xsl:with-param>
								</xsl:call-template></a>
									</xsl:otherwise>
								</xsl:choose>
								
								<br/>
							</label>
							<xsl:value-of select="$lab_change_to"/>
							<xsl:text>&#160;</xsl:text>
								<input style="width:300;" type="file" class="wzb-inputText" name="mod_file" onFocus="this.form.src_type[src_type_file_id].checked=true" value=""/>
							<input type="hidden" name="org_file" value="{$source_link}"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_src_zipfile">
		<xsl:param name="lab_zip"/>
		<xsl:param name="lab_default_page"/>
		<xsl:param name="source_type"/>
		<xsl:param name="source_link"/>
		<xsl:param name="lab_change_to"/>
		<script language="JavaScript">
				if(document.frmXml.src_type){
					if(document.frmXml.src_type.length){
					var src_type_zipfile_id = document.frmXml.src_type.length;
					}else{			
					var src_type_zipfile_id = 1
					}
				}else{
					var src_type_zipfile_id = 0
				}
		</script>
		<tr>
			<td class="wzb-form-label" valign="top">
			</td>
			<td class="wzb-form-control">
				<xsl:choose>
					<xsl:when test="$source_type != 'ZIPFILE'">
						<label for="rdo_src_type_zipfile">
							<input type="radio" name="src_type" value="ZIPFILE" id="rdo_src_type_zipfile"/>
							<xsl:value-of select="$lab_zip"/>
							<xsl:text>：</xsl:text>
							<br/>
						</label>
						<input type="file" class="wzb-inputText" style="width:300px;" name="mod_zipfilename" onFocus="this.form.src_type[src_type_zipfile_id].checked=true"/>
						<input type="hidden" name="zip_filename" value=""/>
						<br/>
						<xsl:value-of select="$lab_default_page"/>
						<br/>
						<input type="text" class="wzb-inputText" value="" name="mod_default_page" maxlength="120" style="width:300px;"/>
						<input type="hidden" name="org_zipfile" value=""/>
						<input type="hidden" name="zip_filename" value=""/>
					</xsl:when>
					<xsl:otherwise>
					<xsl:variable name="url_value">
						<xsl:choose>
							<xsl:when test="string-length($source_link) > 60">
								<xsl:value-of select="substring($source_link,0,60)"/>... 
				  			</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$source_link"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>					
						<span class="Text">
							<label for="rdo_src_type_zipfile">
								<input type="radio" name="src_type" value="ZIPFILE" id="rdo_src_type_zipfile" checked="checked"/>
								<xsl:value-of select="$lab_zip"/>
								<xsl:text>：</xsl:text>
								
								<br/>

								<xsl:choose>
									<xsl:when test="$source_type != 'URL'"><a href="../resource/{/module/@id}/{$source_link}" class="Text" target="_blank">
								<xsl:call-template name="display_filetype_icon">	
									<xsl:with-param name="fileName"><xsl:value-of select="$source_link"/></xsl:with-param>
								</xsl:call-template>									
									</a></xsl:when>
									<xsl:otherwise>
									<a href="{$source_link}" class="Text" target="_blank">
									<xsl:call-template name="display_filetype_icon">	
									<xsl:with-param name="fileName"><xsl:value-of select="$source_link"/></xsl:with-param>
								</xsl:call-template></a>
									</xsl:otherwise>
								</xsl:choose>
								<br/>
								</label>
								<xsl:value-of select="$lab_change_to"/>
								<xsl:text>&#160;</xsl:text>
								<input style="width:300;" type="file" class="wzb-inputText" name="mod_zipfilename" onFocus="this.form.src_type[src_type_zipfile_id].checked=true"/>
								<input type="hidden" value="{$source_link}" name="org_zipfile"/>
								<input type="hidden" name="zip_filename" value=""/>
								<br/>
								<xsl:value-of select="$lab_default_page"/>
								<br/>
								<input type="text" class="wzb-inputText" name="mod_default_page" maxlength="120" style="width:300px;" value="{$source_link}"/>
							
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_annotation">
		<xsl:param name="lab_annotation"/>
		<xsl:param name="lab_ashtml"/>
		<xsl:param name="ashtml">false</xsl:param>
		<xsl:param name="value"/>
		<tr>
			<td width="20%" valign="top" align="right">
				<span class="TitleText">
					<xsl:value-of select="$lab_annotation"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="80%">
				<textarea rows="3" wrap="VIRTUAL" style="width:300px;" cols="35" name="mod_annotation" class="wzb-inputTextArea">
					<xsl:copy-of select="$value"/>
				</textarea>
				<br/>
				<span class="Text">
					<xsl:value-of select="$lab_ashtml"/>
				</span>
				<input value="" type="checkbox" name="asHTML">
					<xsl:if test="$ashtml = 'true'">
						<xsl:attribute name="checked">checked</xsl:attribute>
					</xsl:if>
				</input>
			</td>
		</tr>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_src_aicc">
		<xsl:param name="lab_enter_aicc"/>
		<xsl:param name="lab_enter_aicc_zip"/>
		<xsl:param name="lab_file_crs"/>
		<xsl:param name="lab_file_cst"/>
		<xsl:param name="lab_file_des"/>
		<xsl:param name="lab_file_au"/>
		<xsl:param name="lab_file_ort"/>
		<xsl:param name="lab_source"/>
		<xsl:param name="checked">true</xsl:param>
		<xsl:param name="show_title">false</xsl:param>
		<script language="JavaScript">
				if(document.frmXml.src_type){
					if(document.frmXml.src_type.length){
						var src_type_aicc_id = document.frmXml.src_type.length - 1;
						var src_type_aicc_zip_id = document.frmXml.src_type.length;
					}else{			
					var src_type_aicc_id = 1;
					var src_type_aicc_zip_id = 2;
					}
				}else{
					var src_type_aicc_id = 0;
					var src_type_aicc_zip_id = 1;
				}
		</script>
		<tr>
			<td width="20%" align="right" valign="top">
				<xsl:choose>
					<xsl:when test="$show_title = 'true'">
						<span class="TitleText">
							<xsl:value-of select="$lab_source"/>
							<xsl:text>：</xsl:text>
						</span>
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td width="80%" height="10">
				<span class="Text">
					<label for="rdo_src_type_7">
						<input type="radio" id="rdo_src_type_7" name="src_type" value="AICC_FILES">
							<xsl:if test="$checked  = 'true'"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
						</input>
						<xsl:value-of select="$lab_enter_aicc"/>
						<xsl:text>：</xsl:text>
						</label>
						<input type="hidden" name="mod_src_link" value=""/>
					
				</span>
			</td>
		</tr>
		<tr>
			<td class="wzb-form-label" valign="top">
				<span class="TitleText">
				</span>
			</td>
			<td class="wzb-form-control">
					<table cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td width="20">
							</td>
							<td>
								<xsl:value-of select="$lab_file_crs"/>
								<br/>
								<input value="" type="file" class="wzb-inputText" size="20" style="width:300px;" name="aicc_crs" onFocus="this.form.src_type[src_type_aicc_id].checked=true"/>
							</td>
						</tr>
						<tr>
							<td width="20">
							</td>
							<td>
								<xsl:value-of select="$lab_file_cst"/>
								<br/>
								<input value="" type="file" class="wzb-inputText" size="20" style="width:300px;" name="aicc_cst" onFocus="this.form.src_type[src_type_aicc_id].checked=true"/>
							</td>
						</tr>
						<tr>
							<td width="20">
							</td>
							<td>
								<xsl:value-of select="$lab_file_des"/>
								<br/>
								<input value="" type="file" class="wzb-inputText" size="20" style="width:300px;" name="aicc_des" onFocus="this.form.src_type[src_type_aicc_id].checked=true"/>
							</td>
						</tr>
						<tr>
							<td width="20">
							</td>
							<td>
								<xsl:value-of select="$lab_file_au"/>
								<br/>
								<input value="" type="file" class="wzb-inputText" size="20" style="width:300px;" name="aicc_au" onFocus="this.form.src_type[src_type_aicc_id].checked=true"/>
							</td>
						</tr>
						<tr>
							<td width="20">
							</td>
							<td>
								<xsl:value-of select="$lab_file_ort"/>
								<br/>
								<input value="" type="file" class="wzb-inputText" size="20" style="width:300px;" name="aicc_ort" onFocus="this.form.src_type[src_type_aicc_id].checked=true"/>
							</td>
						</tr>
					</table>
			</td>
		</tr>
		<!-- for import aicc zip files -->
		<tr>
			<td width="20%">
			</td>
			<td width="80%" height="10">
				<span class="Text">
					<label for="rdo_src_type_8">
						<input type="radio" id="rdo_src_type_8" name="src_type" value="AICC_FILES"/>
						<xsl:value-of select="$lab_enter_aicc_zip"/>
						<xsl:text>：</xsl:text>
					</label>
						<input type="hidden" name="mod_src_link" value=""/>
					
				</span>
			</td>
		</tr>
		<tr>
			<td></td>
			<td>
				<span class="Text">
					<table cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td width="20">
							</td>
							<td>
								<input type="file" size="20" style="width:300px;" name="aicc_zip" class="wzb-inputText" onFocus="this.form.src_type[src_type_aicc_zip_id].checked=true"/>
							</td>
						</tr>
							<!-- 这个是隐藏的<ifame>作为表单提交后处理的后台目标-->
						<iframe id="target_upload" name="target_upload" src="" style="display: none"/>
						<tr>
							<td colspan="2">
								<div id="upload_status"/>
							</td>
						</tr>
					</table>
				</span>
			</td>
		</tr>
		<tr>
			<td class="wzb-form-label" valign="top">
			</td>
			<td class="wzb-ui-module-text">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
			</td>
		</tr>
		<input value="" name="aicc_crs_filename" type="hidden"/>
		<input value="" name="aicc_cst_filename" type="hidden"/>
		<input value="" name="aicc_des_filename" type="hidden"/>
		<input value="" name="aicc_au_filename" type="hidden"/>
		<input value="" name="aicc_ort_filename" type="hidden"/>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_src_url">
		<xsl:param name="lab_inst"/>
		<xsl:param name="lab_source"/>
		<xsl:param name="lab_url"/>
		<xsl:param name="show_title">false</xsl:param>
		<xsl:param name="source_type"/>
		<xsl:param name="source_link"/>
		<xsl:param name="checked">false</xsl:param>
		<script language="JavaScript">
				if(document.frmXml.src_type){
					if(document.frmXml.src_type.length){
					var src_type_url_id = document.frmXml.src_type.length;
					}else{			
					var src_type_url_id = 1
					}
				}else{
					var src_type_url_id = 0
				}			
		</script>
		<tr>
			<td width="20%" align="right" valign="top">
			<span class="TitleText">
				<xsl:choose>
					<xsl:when test="$show_title = 'true'">
						<xsl:choose>
							<xsl:when test="$tpl_type = 'ASS'">
								<xsl:value-of select="$lab_inst"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_source"/>
							</xsl:otherwise>
						</xsl:choose>:
				</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
				</xsl:choose>
				</span>
			</td>
			<td width="80%" height="10">
				<span class="Text">
					<label for="rdo_src_type_url">
						<input type="radio" id="rdo_src_type_url" name="src_type" value="URL">
							<xsl:if test="$source_type='URL' or $checked = 'true'"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
						</input>
						<xsl:value-of select="$lab_url"/>:<br/>
					</label>	
						<input size="20" style="width:300px" name="mod_url" class="wzb-inputText" type="text" maxlength="255"  onFocus="this.form.src_type[src_type_url_id].checked=true">
							<xsl:attribute name="value"><xsl:choose><xsl:when test="$source_type = 'URL'"><xsl:value-of select="$source_link"/></xsl:when><xsl:otherwise>http://</xsl:otherwise></xsl:choose></xsl:attribute>						
						</input>
					
				</span>
			</td>
		</tr>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_src_vcr_url">
		<xsl:param name="source_type"/>
		<xsl:param name="source_link"/>
		<xsl:param name="lab_url"/>
		<script language="JavaScript">
				if(document.frmXml.src_type){
					if(document.frmXml.src_type.length){
					var src_type_url_id = document.frmXml.src_type.length;
					}else{			
					var src_type_url_id = 1
					}
				}else{
					var src_type_url_id = 0
				}		
		</script>
		<tr>
			<td width="20%" align="right" valign="top">
				<span class="TitleText">
					<xsl:value-of select="$lab_url"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="80%">
				<input type="hidden" name="src_type" value="URL"/>
				<input value="{$source_link}" size="35" name="mod_src_link" type="text" style="width:300px;" class="wzb-inputText" maxlength="120"/>
				<input type="hidden" name="mod_src_type" value="URL"/>
			</td>
		</tr>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_max_score">
	<xsl:param name="value"/>
	<xsl:param name="lab_max_score"/>
	<xsl:param name="mod_subtype"/>
		<xsl:if test="display/option/progress/@max_score= 'true'">
			<tr>
				<td class="wzb-form-label" align="right">
					<xsl:value-of select="$lab_max_score"/>:
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="$mod_subtype = 'EXC' or $mod_subtype = 'STX' or $mod_subtype = 'DXT' or $mod_subtype = 'TST'">
							<xsl:value-of select="$value"/>
						</xsl:when>
						<xsl:otherwise>
							<input size="4" name="mod_max_score" type="text" class="wzb-inputText" value="{$value}"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>	
	</xsl:template>
	<!--=========================================================-->	
	<xsl:template name="draw_grading">
		<xsl:param name="lab_grading"/>
		<xsl:param name="lab_grade"/>
		<xsl:param name="lab_max_score"/>
		<xsl:param name="lab_pass_score"/>
		<xsl:param name="max_score"/>
		<xsl:param name="pass_score"/>
		<xsl:param name="mode">ins</xsl:param>
		<xsl:choose>
			<xsl:when test="$mode = 'ins'">
				<tr>
					<td class="wzb-form-label" valign="top">
						<xsl:value-of select="$lab_grading"/>:
					</td>
					<td class="wzb-form-control" valign="top">
						<label for="rdo_grading_grad">
							<input type="radio" name="grading" value="0" checked="checked" id="rdo_grading_grad"/>
							<xsl:value-of select="$lab_grade"/>
						</label>
					</td>
				</tr>
			</xsl:when>
			<xsl:when test="$mode = 'eas_upd'">
					<tr>
						<td width="20%" align="right" valign="top">
							<xsl:value-of select="$lab_grading"/>:
						</td>
						<td width="80%"  valign="top">			
						<xsl:choose>
							<xsl:when test="$max_score = '-1.0' ">
								<xsl:value-of select="$lab_grade"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_max_score"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$max_score"/>
								<xsl:value-of select="$lab_pass_score"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$pass_score"/>
								<xsl:text>&#160;%</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
						<input type="hidden" name="mod_max_score" value="{$max_score}"/>
						<input type="hidden" name="mod_pass_score" value="{$pass_score}"/>
						</td>
					</tr>							
			</xsl:when>
			<xsl:otherwise>				
				<xsl:if test="$max_score = '-1.0' ">
					<tr>
						<td width="20%" align="right" valign="top">
							<span class="TitleText">
								<xsl:value-of select="$lab_grading"/>:</span>
						</td>
						<td width="80%"  valign="top">
							<span class="Text">
								<xsl:value-of select="$lab_grade"/>
								<input type="hidden" name="mod_max_score" value="{$max_score}"/>
								<input type="hidden" name="mod_pass_score" value="{$pass_score}"/>
							</span>
						</td>
					</tr>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_by_score">
		<xsl:param name="lab_by_score"/>
		<xsl:param name="lab_max_score"/>
		<xsl:param name="lab_pass_score"/>
		<xsl:param name="mode">ins</xsl:param>
		<xsl:choose>
			<xsl:when test="$mode = 'ins'">
				<!-- By Score-->
				<tr>
					<td class="wzb-form-label">
					</td>
					<td class="wzb-form-control">
					<input type="radio" name="grading" value="1" id="rdo_grading_score"/>
					<xsl:value-of select="$lab_by_score"/>
					<br/>
					
						<label for="rdo_grading_score">
						<xsl:value-of select="$lab_max_score"/><xsl:text>：&#160;</xsl:text>
						</label>
						<input value="" size="4" name="mod_max_score" type="text" onFocus="this.form.grading[1].checked=true" class="wzb-inputText"/>
						<label for="rdo_grading_score">
						<xsl:value-of select="$lab_pass_score"/><xsl:text>：&#160;</xsl:text>
						</label>
						<input value="" size="4" name="mod_pass_score" type="text" onFocus="this.form.grading[1].checked=true" class="wzb-inputText"/>
						<xsl:text>%</xsl:text>
					</td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<!-- By Score-->
				<xsl:if test="header/@max_score != '-1.0' ">
					<tr>
						<td class="wzb-form-label">
						</td>
						<td class="wzb-form-control">
								<xsl:value-of select="$lab_max_score"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="header/@max_score"/>
								<xsl:value-of select="$lab_pass_score"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="header/@pass_score"/>
								<xsl:text>&#160;%</xsl:text>
								<input type="hidden" name="mod_max_score" value="{header/@max_score}"/>
								<input type="hidden" name="mod_pass_score" value="{header/@pass_score}"/>
						</td>
					</tr>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_submission">
		<xsl:param name="lab_no_of_submission"/>
		<xsl:param name="lab_not_limited"/>
		<xsl:param name="lab_max_upload"/>
		<xsl:param name="lab_files"/>
		<xsl:param name="mode">ins</xsl:param>
			<xsl:choose>
				<xsl:when test="$mode = 'ins'">
				<input name="ass_max_upload" type="hidden" value="" />
				<input type="hidden" name="file_desc_lst" value=""/>
				<input type="hidden" name="max_uplaod_file" value=""/>	
				</xsl:when>		
				<xsl:otherwise>
						<xsl:variable name="ass_file_list">
							<xsl:for-each select="header/Description/body ">
								<xsl:value-of select="."/>
								<xsl:if test="position() != last()">:_:_:</xsl:if>
							</xsl:for-each>
						</xsl:variable>				
				<input type="hidden" name="file_desc_lst" value="{$ass_file_list}"/>
				<input name="ass_max_upload" type="hidden" value="{header/@max_upload}"/>
				<input type="hidden" name="max_uplaod_file" value=""/>				
				</xsl:otherwise>
			</xsl:choose>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_submission_msg">
		<xsl:param name="lab_submission"/>
		<xsl:param name="lab_submission_desc"/>
		<xsl:param name="lab_default_submission"/>
				<tr>
					<td width="20%" align="right" valign="top">
						<span class="TitleText">
							<xsl:value-of select="$lab_submission"/>:</span>
					</td>
					<td width="80%">
						<span class="Text">
							<textarea rows="3" wrap="VIRTUAL" style="width:300px;" class="wzb-inputTextArea" cols="35" name="ass_submission">
								<xsl:value-of select="$lab_default_submission"/>
							</textarea>
						</span>
						<br/>
						<span class="Text">
							<xsl:value-of select="$lab_submission_desc"/>
						</span>
					</td>
				</tr>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_nofity_student">
		<xsl:param name="lab_notify_student"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="value">T</xsl:param>
		<!--<tr>
			<td width="20%" align="right" valign="top">
				<span class="TitleText">
					<xsl:value-of select="$lab_notify_student"/>:</span>
			</td>
			<td width="80%" valign="top">
				<span class="Text">
					<label for="ass_notify_ind_1">
						<input type="radio" name="ass_notify_ind" id="ass_notify_ind_1" value="T">
							<xsl:if test="$value = 'T'"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
						</input>
						<xsl:value-of select="$lab_yes"/>
					</label>
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="50"/>
					<label for="ass_notify_ind_2">
						<input type="radio" name="ass_notify_ind" id="ass_notify_ind_2" value="F">
							<xsl:if test="$value = 'F'"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
						</input>
						<xsl:value-of select="$lab_no"/>
					</label>
				</span>
			</td>
		</tr>-->
		<!-- E-mail notifcation diabled -->
		<input type="hidden" name="ass_notify_ind" value="F"/>
	</xsl:template>
	<!--=========================================================-->	
	<xsl:template name="draw_ass_due_date">
		<xsl:param name="lab_due_date"/>
		<xsl:param name="lab_ass_due_date_unspecified"/>
		<xsl:param name="lab_due_date_num"/>
		<xsl:param name="lab_due_date_num_inst_1"/>
		<xsl:param name="lab_due_date_num_inst_2"/>
		<xsl:param name="lab_due_date_non_obligatory"/>
		<xsl:param name="due_date_day"/>
		<xsl:param name="due_datetime"/>
		<xsl:param name="isEnrollment_related">true</xsl:param>
		<xsl:choose>
				<xsl:when test="$isEnrollment_related = 'false'">
					<tr>
						<td width="20%" align="right" valign="top">
							<span class="TitleText">
								<xsl:value-of select="$lab_due_date"/>:</span>
						</td>
						<td width="80%">
							<xsl:text>--</xsl:text>
						</td>
					</tr>
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td width="20%" align="right" valign="top">
							<span class="TitleText">
								<xsl:value-of select="$lab_due_date"/>:<br/><xsl:value-of select="$lab_due_date_non_obligatory"/></span>
						</td>
						<td width="80%" valign="top">
							<label for="ass_due_date_format_0">
								<input id="ass_due_date_format_0" name="ass_due_date_rad" type="radio" onclick="javascript:document.frmXml.ass_due_date_rad[0].checked = true;chg_due_date_day(document.frmXml);chg_due_date_ts(document.frmXml)">
									<xsl:if test="$due_datetime = '' and ($due_date_day &lt;= 0 or $due_date_day = '')">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="$lab_ass_due_date_unspecified"/>
								</input>
							</label>
						</td>
					</tr>
					<tr>
						<td width="20%" align="right" valign="top">
						</td>
						<td width="80%" valign="top">
							<span class="Text">
								<label for="ass_due_date_format_1">
									<input id="ass_due_date_format_1" name="ass_due_date_rad" type="radio" onclick="javascript:document.frmXml.ass_due_date_rad[1].checked = true;chg_due_date_day(document.frmXml)">
										<xsl:if test="$due_datetime != ''">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
								</label>
								<xsl:call-template name="display_form_input_time">
									<xsl:with-param name="fld_name">due</xsl:with-param>
									<xsl:with-param name="hidden_fld_name">ass_due_datetime</xsl:with-param>
									<xsl:with-param name="display_form_input_hhmm">Y</xsl:with-param>
									<xsl:with-param name="timestamp" select="$due_datetime"/>
									<xsl:with-param name="focus_rad_btn_name">ass_due_date_format_1</xsl:with-param>
								</xsl:call-template>
							</span>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label" valign="top">
						</td>
						<td class="wzb-form-control" valign="top">
							<table>
								<tr>
									<td>
										<input id="ass_due_date_format_2" name="ass_due_date_rad" type="radio" onclick="javascript:document.frmXml.ass_due_date_rad[2].checked = true;chg_due_date_ts(document.frmXml)">
											<xsl:if test="$due_date_day &gt;= 1">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</input>
									</td>
									<td>
										<input class="wzb-inputText" size="4" name="ass_due_date_day" type="text" onFocus="" onclick="javascript:document.frmXml.ass_due_date_rad[2].checked = true;chg_due_date_ts(document.frmXml)">
											<xsl:if test="$due_date_day &gt;= 1">
												<xsl:attribute name="value"><xsl:value-of select="$due_date_day"/></xsl:attribute>
											</xsl:if>
										</input>
										<xsl:text>&#160;</xsl:text>
										<label for="ass_due_date_format_2">
											<span class="Text">
												<xsl:value-of select="$lab_due_date_num"/>
											</span>
										</label>
									</td>
								</tr>
								<tr>
									<td>
									</td>
									<td>
										<xsl:value-of select="$lab_due_date_num_inst_2"/>
									</td>
								</tr>
								<tr>
									<td>
									</td>
									<td>
										<xsl:value-of select="$lab_due_date_num_inst_1"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_instructor">
		<xsl:param name="lab_instructor"/>
		<xsl:param name="lab_instructor_select"/>
		<xsl:param name="lab_instructor_enter"/>
		<xsl:param name="instructor_list"/>
		<!-- display option of moderator and instructor must be mutually exclusive  -->
		<xsl:if test="display/option/general/@instructor = 'true'">
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_instructor"/>:</span>
				</td>
				<td width="80%">
					<input type="radio" name="rdo_ist" value="0" onclick="this.form.res_instructor_name.value=''" checked="true"/>
					<span class="Text">
						<select name="mod_instructor" class="Select" onFocus="this.form.rdo_ist[0].checked=true;this.form.res_instructor_name.value=''">
							<option value="" id="">
								<xsl:value-of select="$lab_instructor_select"/>
							</option>
							<xsl:for-each select="$instructor_list/user">
								<xsl:variable name="inst_ent_id" select="@ent_id"/>
								<xsl:variable name="same_inst_cnt">
									<xsl:value-of select="count(preceding-sibling::*[@ent_id = $inst_ent_id])"/>
								</xsl:variable>
								<xsl:if test="$same_inst_cnt = '0'">
									<option value="{@id}" id="{@ent_id}">
										<xsl:if test="@selected='true'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>									
										<!-- Man: Options.id can't use at Netscape, replaced with 'mod_instructor_ent_id' Array Below -->
										<xsl:value-of select="@display"/>
									</option>
								</xsl:if>
							</xsl:for-each>
						</select>
						<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">
								mod_instructor_ent_id = new Array()
								mod_instructor_ent_id[0] = ''										
							<xsl:for-each select="$instructor_list/user">
								<xsl:variable name="inst_ent_id" select="@ent_id"/>
								<xsl:variable name="same_inst_cnt">
									<xsl:value-of select="count(preceding-sibling::*[@ent_id = $inst_ent_id])"/>
								</xsl:variable>
								<xsl:if test="$same_inst_cnt = '0'">
									mod_instructor_ent_id[mod_instructor_ent_id.length] = '<xsl:value-of select="@ent_id"/>'
								</xsl:if>
							</xsl:for-each>
						</SCRIPT>
					</span>
				</td>
			</tr>
			<input type="hidden" value="" name="mod_instructor_ent_id_lst"/>
			<tr>
				<td width="20%" align="right">
				</td>
				<td width="80%">
					<label for="rdo_ist_1">
						<input type="radio" name="rdo_ist" id="rdo_ist_1" value="1" onclick="this.form.mod_instructor.options[0].selected='true'"/>
						<span class="Text">
							<xsl:value-of select="$lab_instructor_enter"/>&#160;
				</span>
					</label>
					<input value="" name="res_instructor_name" type="text" class="wzb-inputText" maxlength="120" onFocus="this.form.rdo_ist[1].checked=true;this.form.mod_instructor.options[0].selected=true"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_moderator">
		<xsl:param name="lab_instructor"/>
		<xsl:param name="lab_instructor_select"/>
		<xsl:param name="instructor_list"/>
		<!-- display option of moderator and instructor must be mutually exclusive  -->
		<!-- moderator is implemented as a mandatory instructor field -->
		<xsl:if test="display/option/general/@moderator = 'true'">
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_instructor"/>:</span>
				</td>
				<td width="80%">
					<span class="Text">
						<select name="mod_instructor" class="Select">
							<option value="">
								<xsl:value-of select="$lab_instructor_select"/>
							</option>
							<xsl:for-each select="$instructor_list/user">
								<xsl:variable name="inst_ent_id" select="@ent_id"/>
								<xsl:variable name="same_inst_cnt">
									<xsl:value-of select="count(preceding-sibling::*[@ent_id = $inst_ent_id])"/>
								</xsl:variable>
								<xsl:if test="$same_inst_cnt = '0'">
									<option value="{@id}">
										<xsl:if test="@selected='true'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>									
										<xsl:value-of select="@display"/>
									</option>
								</xsl:if>
							</xsl:for-each>
						</select>
					</span>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_organization">
		<xsl:param name="lab_organization"/>
		<xsl:param name="value"/>
		<!-- Organization -->
		<xsl:if test="display/option/general/@organization = 'true'">
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_organization"/>:</span>
				</td>
				<td width="80%">
					<span class="Text">
						<input value="{$value}" size="35" name="res_instructor_organization" type="text" class="wzb-inputText" style="width:300px;" maxlength="120"/>
					</span>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_language">
		<xsl:param name="lab_lang"/>
		<xsl:param name="lab_gb_chinese"/>
		<xsl:param name="lab_ch_chinese"/>
		<xsl:param name="lab_english"/>
		<xsl:param name="language"/>
		<!-- Language -->
		
		<xsl:if test="display/option/general/@language = 'true'">
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_lang"/>:</span>
				</td>
				<td width="80%">
					<span class="Text">
						<select name="res_lan" class="Select">
							<xsl:if test="$encoding='GB2312'">
								<option value="GB2312">
									<xsl:if test="$language='GB2312'">
										<xsl:attribute name="selected">selected</xsl:attribute>
									</xsl:if>								
									<xsl:value-of select="$lab_gb_chinese"/>
								</option>
							</xsl:if>
							<xsl:if test="$encoding='Big5'">
								<option value="Big5">
									<xsl:if test="$language='Big5'">
										<xsl:attribute name="selected">selected</xsl:attribute>
									</xsl:if>								
									<xsl:value-of select="$lab_ch_chinese"/>
								</option>
							</xsl:if>
							<option value="ISO-8859-1">
								<xsl:if test="$language='ISO-8859-1' or $language = ''">
									<xsl:attribute name="selected">selected</xsl:attribute>
								</xsl:if>							
								<xsl:value-of select="$lab_english"/>
							</option>
						</select>
					</span>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_difficulty">
		<xsl:param name="lab_diff"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="diff"/>
		<!-- Difficulty -->
		<xsl:if test="display/option/progress/@difficulty = 'true'">
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_diff"/>:</span>
				</td>
				<td width="80%">
					<span class="Text">
						<select name="mod_difficulty" class="Select">
							<option value="1">
								<xsl:if test="$diff = '1'">
									<xsl:attribute name="selected">selected</xsl:attribute>
								</xsl:if>							
								<xsl:value-of select="$lab_easy"/>
							</option>
							<option value="2">
								<xsl:if test="$diff = '2' or $diff = ''">
									<xsl:attribute name="selected">selected</xsl:attribute>
								</xsl:if>										
								<xsl:value-of select="$lab_normal"/>
							</option>
							<option value="3">
								<xsl:if test="$diff = '3'">
									<xsl:attribute name="selected">selected</xsl:attribute>
								</xsl:if>										
								<xsl:value-of select="$lab_hard"/>
							</option>
						</select>
					</span>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_max_usr_attempt">
		<xsl:param name="lab_max_attempt_num"/>
		<xsl:param name="lab_max_attempt_num_unlimited"/>
		<xsl:param name="lab_max_attempt_num_limited"/>
		<xsl:param name="lab_max_attempt_num_times"/>
		<xsl:param name="max_usr_attempt_num"/>
		<xsl:if test="display/option/progress/@max_usr_attempt = 'true'">
			<tr>
				<td width="20%" align="right">
					<span class="TitleText">
						<xsl:value-of select="$lab_max_attempt_num"/>:</span>
				</td>
				<td width="80%">
					<label for="max_usr_attempt_rad_1">
						<input id="max_usr_attempt_rad_1" type="radio" name="max_usr_attempt_rad" value="0" checked="checked" onfocus="javascript:chg_max_attp_num(document.frmXml)">
						<xsl:if test="$max_usr_attempt_num = '0' or $max_usr_attempt_num = ''">
								<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>						
						</input>
						<span class="Text">
							<xsl:value-of select="$lab_max_attempt_num_unlimited"/>
						</span>
					</label>
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td>
					<input type="radio" name="max_usr_attempt_rad" value="1">
						<xsl:if test="$max_usr_attempt_num != '0' and $max_usr_attempt_num != ''">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</input>
					<label for="max_usr_attempt_rad_2">
						<span class="Text">
							<xsl:value-of select="$lab_max_attempt_num_limited"/>
						</span>
					</label>	
						<xsl:text>&#160;</xsl:text>
						<input id="max_usr_attempt_rad_2" class="wzb-inputText" maxlength="3" size="4" name="max_usr_attempt_unlimited_num" type="text" onclick="javascript:document.frmXml.max_usr_attempt_rad[1].checked = true">
							<xsl:if test="$max_usr_attempt_num != '0' and $max_usr_attempt_num != ''">
								<xsl:attribute name="value"><xsl:value-of select="$max_usr_attempt_num"/></xsl:attribute>
							</xsl:if>
						</input>
						<xsl:text>&#160;</xsl:text>
						<span class="Text">
							<xsl:value-of select="$lab_max_attempt_num_times"/>
						</span>
					
				</td>
			</tr>
			<input type="hidden" name="mod_max_usr_attempt" value=""/>
		</xsl:if>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_pass_score">
		<xsl:param name="lab_pass_score"/>
		<xsl:param name="value"/>
		<xsl:param name="readonly">false</xsl:param>
		<xsl:param name="mod_subtype"/>
		<!-- Pass Score -->
		<tr>
			<td width="20%" align="right" valign="top">
				<span class="TitleText">
					<xsl:value-of select="$lab_pass_score"/>:</span>
			</td>
			<td width="80%">
				<span class="Text">
				<xsl:choose>
					<xsl:when test="$readonly = 'TRUE'">
						<xsl:value-of select="$value"/>
						<input value="{$value}" size="4" name="mod_pass_score" type="hidden"/>
					</xsl:when>
					<xsl:otherwise>
					<input value="{$value}" size="4" name="mod_pass_score" type="text" class="wzb-inputText"/>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$mod_subtype = 'DXT' or $mod_subtype = 'TST'">&#160;%</xsl:when>
					<xsl:otherwise></xsl:otherwise>
				</xsl:choose>
			</span>
			</td>
		</tr>
	</xsl:template>
	<!--=========================================================-->
	  <xsl:template name="netg_upd_pass_score">
		<xsl:param name="lab_pass_score"/>
		<xsl:param name="value"/>
		<!-- Pass Score -->
		<tr>
			<td width="20%" align="right" valign="top">
				<span class="TitleText">
					<xsl:value-of select="$lab_pass_score"/>:</span>
			</td>
			<td width="80%">
				<span class="Text">
					<!--<input value="{$value}" size="4" name="mod_pass_score" type="text" disabled="disable" class="wzb-inputText"/>-->
					<xsl:value-of select="$value"/>
					<input value="{$value}" size="4" name="mod_pass_score" type="hidden"/>
			&#160;%
			</span>
			</td>
		</tr>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_status">
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_online"/>
		<xsl:param name="lab_offline"/>
		<xsl:param name="status"/>
		<xsl:param name="readonly">FALSE</xsl:param> 
		<xsl:param name="isEnrollment_related">true</xsl:param>
		<xsl:if test="display/option/general/@status = 'true'">
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_status"/>:</span>
				</td>
				<td width="80%">
					<span class="Text">
						<xsl:choose>
							<xsl:when test="$isEnrollment_related = 'false'">
								<xsl:text>--</xsl:text>
								<input type="hidden" name="mod_status" value="OFF"/>
							</xsl:when>
							<xsl:when test="$readonly = 'TRUE'">
								<xsl:value-of select="$lab_offline"/>
								<input type="hidden" name="mod_status" value='OFF'/>
							</xsl:when>
							<xsl:otherwise>
								<select name="mod_status" class="Select">
									<option value="ON">
											<xsl:if test="$status='ON'">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
										<xsl:value-of select="$lab_online"/>
									</option>
									<option value="OFF">
										<xsl:if test="$status='OFF' or $status = ''">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_offline"/>
									</option>
								</select>							
							</xsl:otherwise>
						</xsl:choose>

					</span>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_effective_start">
		<xsl:param name="lab_start_date"/>
		<xsl:param name="lab_immediate"/>
		<xsl:param name="timestamp"/>
		<xsl:param name="show_hhmm">Y</xsl:param>
		<xsl:param name="readonly">false</xsl:param>
		<xsl:param name="isEnrollment_related">true</xsl:param>
		<xsl:choose>
			<!-- Effective Start -->
			<xsl:when test="display/option/datetime/@eff_start = 'true'">
				<tr>
					<td width="20%" align="right" valign="top">
						<span class="TitleText">
							<xsl:value-of select="$lab_start_date"/>:</span>
					</td>
					<td width="80%">
						<span class="Text">
							<xsl:choose>
								<xsl:when test="$isEnrollment_related = 'false'">
									<xsl:text>--</xsl:text>
									<input type="hidden" name="mod_eff_start_datetime" value="IMMEDIATE"/>
								</xsl:when>
								<xsl:when test="$readonly = 'true'">
									<span class="Text">
										<xsl:call-template name="display_time">
											<xsl:with-param name="my_timestamp" select="$timestamp"/>
											<xsl:with-param name="dis_time">T</xsl:with-param>
										</xsl:call-template>
									</span>
									<input type="hidden" name="mod_eff_start_datetime" value="{$timestamp}"/>
								</xsl:when>
								<xsl:otherwise>
										<label for="rdo_start_date_1">
											<input type="radio" id="rdo_start_date_1" name="start_date" value="0">
												<xsl:if test="$timestamp = ''"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
											</input>
											<xsl:value-of select="$lab_immediate"/>
										</label>
										<br/>										
										<label for="rdo_start_date_2">
											<input type="radio" name="start_date" value="1" id="rdo_start_date_2">
											<xsl:if test="$timestamp != ''"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
											</input>
										</label>	
											<xsl:choose>
												<xsl:when test="$timestamp ='UNLIMITED'">
													<xsl:call-template name="display_form_input_time">
														<xsl:with-param name="fld_name">start</xsl:with-param>
														<xsl:with-param name="display_form_input_hhmm">Y</xsl:with-param>
														<xsl:with-param name="hidden_fld_name">mod_eff_start_datetime</xsl:with-param>
														<xsl:with-param name="focus_rad_btn_name">start_date[1]</xsl:with-param>
													</xsl:call-template>
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="display_form_input_time">
														<xsl:with-param name="fld_name">start</xsl:with-param>
														<xsl:with-param name="timestamp" select="$timestamp"/>
														<xsl:with-param name="display_form_input_hhmm">Y</xsl:with-param>
														<xsl:with-param name="hidden_fld_name">mod_eff_start_datetime</xsl:with-param>
														<xsl:with-param name="focus_rad_btn_name">start_date[1]</xsl:with-param>
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>											
																		
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<input type="hidden" name="mod_eff_start_datetime" value=""/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_effective_end">
		<xsl:param name="lab_end_date"/>
		<xsl:param name="lab_unlimit"/>
		<xsl:param name="timestamp"/>
		<xsl:param name="isEnrollment_related">true</xsl:param>
		<!--  Effective End -->
		<xsl:choose>
			<xsl:when test="display/option/datetime/@eff_end = 'true'">
				<tr>
					<td width="20%" align="right" valign="top">
						<span class="TitleText">
							<xsl:value-of select="$lab_end_date"/>:</span>
					</td>
					<td width="80%">
						<xsl:choose>
							<xsl:when test="$isEnrollment_related = 'false'">
								<xsl:text>--</xsl:text>
								<input type="hidden" name="mod_eff_end_datetime" value="UNLIMITED"/>
							</xsl:when>
							<xsl:otherwise>
								<span class="Text">
									<label for="rdo_end_date_1">
										<input id="rdo_end_date_1" type="radio" name="end_date" value="0">
											<xsl:if test="$timestamp = 'UNLIMITED' or $timestamp = ''">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</input>
										<xsl:value-of select="$lab_unlimit"/>
									</label>
									<br/>
									<label for="rdo_end_date_2">
										<input id="rdo_end_date_2" type="radio" name="end_date" value="1">
											<xsl:if test="$timestamp != '' and $timestamp != 'UNLIMITED'">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</input>
									</label>
									<xsl:choose>
										<xsl:when test="$timestamp = 'UNLIMITED'">
											<xsl:call-template name="display_form_input_time">
												<xsl:with-param name="fld_name">end</xsl:with-param>
												<xsl:with-param name="display_form_input_hhmm">Y</xsl:with-param>
												<xsl:with-param name="focus_rad_btn_name">end_date[1]</xsl:with-param>
												<xsl:with-param name="hidden_fld_name">mod_eff_end_datetime</xsl:with-param>
											</xsl:call-template>
										</xsl:when>
										<xsl:otherwise>
											<xsl:call-template name="display_form_input_time">
												<xsl:with-param name="fld_name">end</xsl:with-param>
												<xsl:with-param name="focus_rad_btn_name">end_date[1]</xsl:with-param>
												<xsl:with-param name="timestamp" select="$timestamp"/>
												<xsl:with-param name="display_form_input_hhmm">Y</xsl:with-param>
												<xsl:with-param name="hidden_fld_name">mod_eff_end_datetime</xsl:with-param>
											</xsl:call-template>
										</xsl:otherwise>
									</xsl:choose>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<input type="hidden" name="mod_eff_end_datetime" value=""/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_template">
		<xsl:param name="lab_template"/>
		<xsl:param name="template_list"/>
		<xsl:for-each select="$template_list/template">
			<input type="hidden">
				<xsl:attribute name="name"><xsl:value-of select="concat(@name, 'desc')"/></xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="desc"/></xsl:attribute>
			</input>
			<input type="hidden">
				<xsl:attribute name="name"><xsl:value-of select="concat(@name, 'note')"/></xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="note"/></xsl:attribute>
			</input>
			<input type="hidden">
				<xsl:attribute name="name"><xsl:value-of select="concat(@name, 'thumb')"/></xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="url"/></xsl:attribute>
			</input>
		</xsl:for-each>
		<tr>
			<td width="20%" align="right" valign="top">
				<span class="TitleText">
					<xsl:value-of select="$lab_template"/>:</span>
			</td>
			<td width="80%">
				<table cellpadding="0" border="0" cellspacing="0" width="100%">
					<tr>
						<td width="30%" valign="top">
							<span class="Text">
								<select name="tpl_name" onChange="changeTemplate()" class="Select" size="9" style="width:160 px;">
									<xsl:for-each select="$template_list/template">
										<xsl:variable name="cur_tpl" select="../@cur_tpl"/>
										<option>
											<xsl:choose>
												<xsl:when test="@name = $cur_tpl">
														<xsl:attribute name="selected">selected</xsl:attribute>
												</xsl:when>
												<xsl:when test="not($cur_tpl)">
													<xsl:if test="(position() = 1)">
														<xsl:attribute name="selected">selected</xsl:attribute>
													</xsl:if>
												</xsl:when>
											</xsl:choose>
											<xsl:value-of select="@name"/>
										</option>
									</xsl:for-each>
								</select>
							</span>
						</td>
						<td width="70%">
							<xsl:text>&#160;</xsl:text>
							<img height="150" width="200" name="templatethumb" src="{$template_list/template[position()=1]/url}" border="1"/>
							<br/>
							<span id="tpldesc" class="Text">
								<xsl:text>&#160;</xsl:text>
								<script language="JavaScript" type="text/javascript"><![CDATA[
					  			if (document.layers)
	            				document.write('<input type="text" class="wzb-inputText" size="20" name="tpl_desc" onfocus="this.blur()" value="]]><xsl:value-of select="$template_list/template[position()=1]/desc"/><![CDATA["  disable="disable" />');
							]]></script>
							</span>
							<br/>
							<span id="tplnotes" class="Text">
								<xsl:text>&#160;</xsl:text>
								<script language="JavaScript" type="text/javascript"><![CDATA[
								if (document.layers)
									document.write('<textarea wrap="soft"  rows="2" cols="20" onfocus="this.blur()" name="tpl_notes" disable="disable" >]]><xsl:value-of select="$template_list/template[position()=1]/note"/><![CDATA[</textarea>');
							]]></script>
							</span>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_ass_inst">
		<xsl:param name="lab_inst"/>
		<xsl:param name="lab_ass_inst"/>
		<xsl:param name="lab_ass_inst_desc"/>
		<xsl:param name="mod">ins</xsl:param>
		<xsl:param name="inst"/>
		<xsl:param name="source_type"/>
		<xsl:param name="mode">ins</xsl:param>
		<script language="JavaScript">
			if(document.frmXml.src_type){
				if(document.frmXml.src_type.length){
					var src_type_ass_inst_id = document.frmXml.src_type.length;
				}else{			
				var src_type_ass_inst_id = 1
				}
			}else{
				var src_type_ass_inst_id = 0
			}	
		</script>		
		<xsl:variable name="all_not_checked">
			<xsl:if test="($inst = '') and ($source_type != 'URL') and ($source_type != 'FILE') and ($source_type != 'ZIPFILE')">yes</xsl:if>
		</xsl:variable>		
		<tr>
			<td width="20%" align="right" valign="top">
				<span class="TitleText">
					<xsl:value-of select="$lab_inst"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="80%" height="10">
				<span class="Text">
					<label for="rdo_src_type_2">
						<input type="radio" name="src_type" id="rdo_src_type_2" value="">
							<xsl:if test="($inst != '') or ($all_not_checked = 'yes') or $mode = 'ins'"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
						</input>		
						<xsl:value-of select="$lab_ass_inst"/>
						<br/>
					</label>	
						<textarea class="wzb-inputTextArea" rows="3" wrap="VIRTUAL" style="width:300px;" cols="35" name="mod_instr" onFocus="this.form.src_type[src_type_ass_inst_id].checked=true">
							<xsl:value-of select="$inst"/>
						</textarea>
						<br/>
						<span class="Text">
							<xsl:value-of select="$lab_ass_inst_desc"/>
						</span>
				</span>
			</td>
		</tr>
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
				var src_type_pick_aicc_res_id = 1
				}
			}else{
				var src_type_pick_aicc_res_id = 0
			}	
		</script>			
		<tr>
			<td width="20%" align="right" valign="top">
				<xsl:choose>
					<xsl:when test="$show_title = 'true'">
						<span class="TitleText">
							<xsl:value-of select="$lab_source"/>
							<xsl:text>：</xsl:text>
						</span>
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
				</xsl:choose>

			</td>
			<td width="80%" height="10">
				<span class="Text">
					<label for="rdo_src_type_pick_aicc_res">
						<input type="radio" name="src_type" id="rdo_src_type_pick_aicc_res" value="">
							<xsl:if test="$checked  = 'true'"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
						</input>
						<xsl:value-of select="$lab_from_resource"/>
						<xsl:text>&#160;</xsl:text>
					</label>
					<a href="javascript:document.frmXml.src_type[src_type_pick_aicc_res_id].checked=true;res.pick_res(tpl_type,'',cookie_course_id);">
					</a>
					<input type="hidden" name="source_type" value=""/>
					<input type="hidden" name="source_content" value=""/>
				</span>
				
			</td>
		</tr>
		<tr><td class="wzb-form-label"></td>
		<td class="wzb-form-control">
		<span class="Text" id="src_display">
		<script type="text/javascript" language="JavaScript"><![CDATA[
			if (document.layers)
				document.write('<input type="text" size="20" name="src_type_display"  onfocus="if(this.value!= \'\'){window.open(this.value,\'_blank\')}"/>');
		]]>
		</script>
		</span>		
		</td>
		</tr>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_pick_res">
		<xsl:param name="lab_source"/>
		<xsl:param name="show_title">false</xsl:param>
		<xsl:param name="lab_from_resource"/>
		<xsl:param name="checked">true</xsl:param>		
		<script language="JavaScript">
			if(document.frmXml.src_type){
				if(document.frmXml.src_type.length){
					var src_type_pick_res_id = document.frmXml.src_type.length;
				}else{			
				var src_type_pick_res_id = 1
				}
			}else{
				var src_type_pick_res_id = 0
			}	
		</script>				
		<tr>
			<td width="20%" align="right" valign="top">
				<span class="TitleText">
					<xsl:value-of select="$lab_source"/>
					<xsl:text>：</xsl:text>
				</span>
			</td>
			<td width="80%" height="10">
				<span class="Text">
					<label for="rdo_src_type_1">
						<input type="radio" name="src_type" id="rdo_src_type_1" value="">
							<xsl:if test="$checked  = 'true'"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
						</input>
						<xsl:value-of select="$lab_from_resource"/>
						<xsl:text>&#160;</xsl:text>
					</label>
					<a href="javascript:document.frmXml.src_type[src_type_pick_res_id].checked=true;res.pick_res(tpl_type,'',cookie_course_id)">
						<img src="{$wb_img_path}ico_pick.gif" border="0" align="absmiddle"/>
					</a>
					<input type="hidden" name="source_type" value=""/>
					<input type="hidden" name="source_content" value=""/>
				</span>
			</td>
		</tr>
		<tr><td width="20%"><img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/></td>
		<td width="80%">
		<span class="Text" id="src_display">
		<script type="text/javascript" language="JavaScript"><![CDATA[
			if (document.layers)
				document.write('<input type="text" size="20" name="src_type_display"  onfocus="if(this.value!= \'\'){window.open(this.value,\'_blank\')}"/>');
		]]>
		</script>
		</span>		
		</td>
		</tr>		
	</xsl:template>
	<!--=========================================================-->	
	<xsl:template name="draw_keep_res">
	<xsl:param name="lab_previous"/>
	<xsl:param name="res_src_link"/>
	<xsl:param name="lab_source"/>
		<script language="JavaScript">
			if(document.frmXml.src_type){
				if(document.frmXml.src_type.length){
					var src_type_res_id = document.frmXml.src_type.length;
				}else{			
				var src_type_res_id = 1
				}
			}else{
				var src_type_res_id = 0
			}	
		</script>		
		<tr>
			<td width="20%" valign="top" align="right">
				<span class="TitleText">
					<xsl:value-of select="$lab_source"/>:</span>
			</td>
			<td>
				<label for="rdo_src_type_res">
					<input type="radio" name="src_type" id="rdo_src_type_res" checked="checked"/>
					<span class="Text">
						<xsl:value-of select="$lab_previous"/>
					</span>
				</label>
			</td>
		</tr>
		<tr>
			<td width="20%" valign="top">
				<img border="0" height="1" src="{$}tp.gif" width="1"/>
			</td>
			<td width="80%">
				<xsl:variable name="url_value">
					<xsl:choose>
						<xsl:when test="string-length($res_src_link) > 60">
							<xsl:value-of select="substring($res_src_link,0,60)"/>... 
			  			</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$res_src_link"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<a href="{$res_src_link}" target="_blank" class="Text">
					<xsl:value-of select="$url_value"/>
				</a>
			</td>
		</tr>	
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_sub_after_passed_ind">
		<xsl:param name="checked">1</xsl:param>
		<xsl:param name="lab_text"/>
		<tr>
			<td width="20%" align="right" valign="top">
				<input type="hidden" name="mod_sub_after_passed_ind"/>
				<input type="checkbox" name="mod_sub_after_passed_chk" id="mod_sub_after_passed_chk_id" value="0">
					<xsl:if test="$checked = '0'">
						<xsl:attribute name="checked"/>
					</xsl:if>				
				</input>
			</td>
			<td width="80%" align="left">
				<span class="Text">
					<label for ="mod_sub_after_passed_chk_id"><xsl:value-of select="$lab_text"/></label>
				</span>
			</td>
		</tr>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_show_answer_ind">
		<xsl:param name="checked">0</xsl:param>
		<xsl:param name="lab_text"/>
		<tr>
			<td width="20%" align="right" valign="top">
				<input type="hidden" name="mod_show_answer_ind"/>
				<input type="checkbox" name="mod_show_answer_ind_chk" id="mod_show_answer_ind_chk_id" value="1">
					<xsl:if test="$checked = '1'">
						<xsl:attribute name="checked"/>
					</xsl:if>
				</input>
			</td>
			<td width="80%" align="left">
				<span class="Text">
					<label for ="mod_show_answer_ind_chk_id"><xsl:value-of select="$lab_text"/></label>
				</span>
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template name="draw_show_answer_ind_temp">
		<xsl:param name="checked">0</xsl:param>
		<xsl:param name="lab_text"/>
		<xsl:param name="watch_out"/>
		<xsl:if test="$watch_out != ''">
			<tr>
				<td colSpan="2"><xsl:value-of select="$watch_out"></xsl:value-of></td>
			</tr>
		</xsl:if>
		<tr>
			<td width="20%" align="right" valign="top">
				<!--Jebe	<input type="hidden" name="mod_show_answer_ind"/>-->
				<input type="radio" name="mod_show_answer_ind_chk" id="mod_show_answer_ind_chk_id" value="1" >
					<xsl:if test="$checked = '1'">
						<xsl:attribute name="checked"/>
					</xsl:if>
				</input>
			</td>
			<td width="80%" align="left">
				<span class="Text">
					<label for ="mod_show_answer_ind_chk_id"><xsl:value-of select="$lab_text"/></label>
				</span>
			</td>
		</tr>
	</xsl:template>
	<xsl:template name="mod_show_answer_after_passed_ind">
		<xsl:param name="checked">0</xsl:param>
		<xsl:param name="lab_text"/>
		<tr>
			<td width="20%" align="right" valign="top">
				<input type="hidden" name="mod_show_answer_after_passed_ind"/>
				<input type="radio" name="mod_show_answer_ind_chk" id="mod_show_answer_after_passed_ind_chk_id" value="1">
					<xsl:if test="$checked = '1'">
						<xsl:attribute name="checked"/>
					</xsl:if>
				</input>
			</td>
			<td width="80%" align="left">
				<span class="Text">
					<label for ="mod_show_answer_after_passed_ind_chk_id"><xsl:value-of select="$lab_text"/></label>
				</span>
			</td>
		</tr>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_show_save_and_suspend_ind">
		<xsl:param name="checked">0</xsl:param>
		<xsl:param name="lab_text"/>
		<tr>
			<td width="20%" align="right" valign="top">
				<input type="hidden" name="mod_show_save_and_suspend_ind"/>
				<input type="checkbox" name="mod_show_save_and_suspend_ind_chk" id="mod_show_save_and_suspend_ind_chk_id" value="1">
					<xsl:if test="$checked = '1'">
						<xsl:attribute name="checked"/>
					</xsl:if>
				</input>
			</td>
			<td width="80%" align="left">
				<span class="Text">
					<label for ="mod_show_save_and_suspend_ind_chk_id"><xsl:value-of select="$lab_text"/></label>
				</span>
			</td>
		</tr>
	</xsl:template>
	<!--=========================================================-->
	<xsl:template name="draw_show_managed_ind">
		<xsl:param name="checked">0</xsl:param>
		<xsl:param name="lab_text"/>
		<tr>
			<td width="20%" align="right" valign="top">
				<input type="hidden" name="mod_managed_ind"/>
				<input type="checkbox" name="mod_managed_ind_chk" id="mod_managed_ind_chk_id" value="1">
					<xsl:if test="$checked = '1'">
						<xsl:attribute name="checked"/>
					</xsl:if>
				</input>
			</td>
			<td width="80%" align="left">
				<span class="Text">
					<label for ="mod_managed_ind_chk_id"><xsl:value-of select="$lab_text"/></label>
				</span>
			</td>
		</tr>
	</xsl:template>
	<!--=========================================================-->
		<xsl:template name="draw_text_style">
		<xsl:param name="select"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_style1"/>
		<xsl:param name="lab_style2"/>
		<tr>
			<td width="20%" align="right" valign="top">
				<xsl:value-of select="$lab_title"/>:
			</td>
			<td width="80%" align="left">
			</td>
		</tr>
		<tr>
			<td width="20%" align="right" valign="top">
				<input type="hidden" name="text_style"/>
				<input type="radio" name="mod_test_style" id="text_style_only_id" value="only">
				<xsl:if test="$select='' or $select='only'">
					<xsl:attribute name="checked"/>
				</xsl:if>
				</input>
			</td>
			<td width="80%" align="left">
				<span class="Text">
					<label for ="text_style_only_id"><xsl:value-of select="$lab_style1"/></label>
				</span>
			</td>
		</tr>
		<tr>
			<td width="20%" align="right" valign="top">
				<input type="radio" name="mod_test_style" id="text_style_many_id" value="many">
				<xsl:if test=" $select='many'">
					<xsl:attribute name="checked"/>
				</xsl:if>
				</input>
			</td>
			<td width="80%" align="left">
				<span class="Text">
					<label for ="text_style_many_id"><xsl:value-of select="$lab_style2"/></label>
				</span>
			</td>
		</tr>
	</xsl:template>
	<!--=========================================================-->
</xsl:stylesheet>
