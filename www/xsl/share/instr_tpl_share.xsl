<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="../utils/display_form_input_time.xsl"/>
	<xsl:import href="../utils/display_time.xsl"/>
	<xsl:import href="../utils/wb_gen_input_file.xsl"/>
	
	<xsl:template name="text_tpl">
		<xsl:param name="lab" />
		<xsl:param name="val" />
		<xsl:param name="required" />
		<xsl:param name="field_name" />
		<xsl:param name="maxlength">255</xsl:param>
		<xsl:param name="colspan">1</xsl:param>
		<xsl:param name="width">150</xsl:param>
		<td class="wzb-form-label">
			<xsl:if test="$required = 'true'"><span class="wzb-form-star">*</span></xsl:if><xsl:value-of select="$lab" />：
		</td>
		<td class="wzb-form-control">
			<input value="{$val}" class="wzb-inputText" style="width: {$width}" maxlength="{$maxlength}" type="text" name="{$field_name}" />
			<input type="hidden" name="lab_{$field_name}" value="{$lab}" />
		</td>
	</xsl:template>
	
	<xsl:template name="datetime_tpl">
		<xsl:param name="lab" />
		<xsl:param name="val" />
		<xsl:param name="required" />
		<xsl:param name="field_name" />
		<xsl:param name="colspan">1</xsl:param>
		<td style="padding:10px 0; text-align:right; color:#666;">
			<xsl:if test="$required = 'true'"></xsl:if><xsl:value-of select="$lab" />：
		</td>
		<td style="padding:10px 0 10px 10px; color:#333;" colspan="{$colspan}">
			<xsl:call-template name="display_form_input_time">
				<xsl:with-param name="fld_name"><xsl:value-of select="$field_name"/>_input</xsl:with-param>
				<xsl:with-param name="hidden_fld_name" select="$field_name"/>
				<xsl:with-param name="frm">document.frmXml</xsl:with-param>
				<xsl:with-param name="timestamp" select="$val"/>
			</xsl:call-template>
			<input type="hidden" name="lab_iti_bday" value="{$lab}" />
		</td>
	</xsl:template>

	<xsl:template name="select_tpl">
		<xsl:param name="lab" />
		<xsl:param name="val" />
		<xsl:param name="options" />
		<xsl:param name="required" />
		<xsl:param name="field_name" />
		<xsl:param name="colspan">1</xsl:param>
		<td class="wzb-form-label">
			<xsl:if test="$required = 'true'"><span class="wzb-form-star">*</span></xsl:if><xsl:value-of select="$lab" />：
		</td>
		<td class="wzb-form-control">
			<select name="{$field_name}" class="wzb-select">
				<option value="">
					<xsl:value-of select="$lab_not_specified" />
				</option>
				<xsl:if test="$options and count($options/option) &gt;0">
					<xsl:for-each select="$options/option">
						<option value="{@value}">
							<xsl:if test="$val = @value">
								<xsl:attribute name="selected">true</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="text()" />
						</option>
					</xsl:for-each>
				</xsl:if>
			</select>
			<input type="hidden" name="lab_{$field_name}" value="{$lab}" />
		</td>
	</xsl:template>

	<xsl:template name="textarea_tpl">
		<xsl:param name="lab" />
		<xsl:param name="val" />
		<xsl:param name="required" />
		<xsl:param name="field_name" />
		<xsl:param name="colspan">1</xsl:param>
		<xsl:param name="width">500</xsl:param>
		<td class="wzb-form-label" valign="top">
			<xsl:if test="$required = 'true'"><span class="wzb-form-star">*</span></xsl:if><xsl:value-of select="$lab" />：
		</td>
		<td class="wzb-form-control"><!-- style="width: {$width}; height: 80px;" -->
			<textarea class="wzb-inputTextArea" name="{$field_name}" style="width:410px;" rows="4">
				<xsl:value-of select="$val"></xsl:value-of>
			</textarea>
			<input type="hidden" name="lab_{$field_name}" value="{$lab}" />
		</td>
	</xsl:template>

	<xsl:template name="lab_val_tpl">
		<xsl:param name="lab" />
		<xsl:param name="val" />
		<xsl:param name="colspan">1</xsl:param>
		<td style="padding:10px 0; text-align:right; color:#666;">
			<xsl:value-of select="$lab" />
			<xsl:text>：</xsl:text>
		</td>
		<td style="padding:10px 0 10px 10px; color:#333;" colspan="{$colspan}">
			<xsl:choose>
				<xsl:when test="$val != ''">
					<xsl:value-of select="$val" />
				</xsl:when>
				<xsl:otherwise>0.0</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	
	<xsl:template name="instr_icon_tpl">
		<xsl:param name="lab" />
		<xsl:param name="val" />
		<xsl:param name="lab_color"/>
		<xsl:variable name="lab_remain_image">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">保留現有頭像</xsl:when>
				<xsl:when test="$wb_lang='gb'">保留现有头像</xsl:when>
				<xsl:otherwise>Keep this thumbnail</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_default_image">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">使用默認頭像</xsl:when>
				<xsl:when test="$wb_lang='gb'">使用默认头像</xsl:when>
				<xsl:otherwise>Restore to default thumbnail</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="lab_local_image">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">上傳一個本地圖片作為顯示的圖像</xsl:when>
				<xsl:when test="$wb_lang='gb'">上传一个本地图片作为显示的图像</xsl:when>
				<xsl:otherwise>Upload an image as my thumbnail</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="img_des">
			<xsl:choose>
				<xsl:when test="$wb_lang='ch'">(圖片規格建議：寬120px，高120px，支持JPG,GIF,PNG格式圖片)</xsl:when>
				<xsl:when test="$wb_lang='gb'">(图片规格建议：宽120px，高120px，支持JPG,GIF,PNG格式图片)</xsl:when>
				<xsl:otherwise>(Image size recommendation: width 120px, height 120px ,Support JPG,GIF,PNG files)</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr>
			<td width="20%" align="right" valign="middle" style="{$lab_color}">
				<span class="wzb-form-star">*</span>
				<xsl:value-of select="$lab"/>
				<xsl:text>：</xsl:text>
			</td>
			<td width="80%" style="padding-left:10px">
				<script language="JavaScript" type="text/javascript">
					function clearFileInput(file_obj){
						var file_obj2= file_obj.cloneNode(false);
						file_obj2.onchange= file_obj.onchange; 
						//file_obj2.disabled = true;
						file_obj.parentNode.replaceChild(file_obj2,file_obj);
					}

					function iti_img_change(obj) {
                        $("#default_btn").prev().removeAttr("disabled");
						var img_obj = document.getElementById("iti_img_id");
						var file_obj = document.getElementById("file_photo_url");
						if(obj.id === "remain_image") {
							<!-- $("#default_btn").prev().attr("disabled","disabled"); -->
							img_obj.src = "../<xsl:value-of select="$val"/>";
							document.frmXml.remain_photo_ind.value = "true";
						} else if(obj.id === "default_image") {
							clearFileInput(file_obj);
							//img_obj.src = wb_utils_app_base + "<xsl:value-of select="$default_usr_icon"/>";
							document.frmXml.remain_photo_ind.value = "false";
						} else {
							<!-- $("#default_btn").prev().attr("disabled","disabled"); -->
							file_obj.disabled = false;
							document.frmXml.remain_photo_ind.value = "false";
							if(document.all.iti_img.files &amp;&amp; document.all.iti_img.files[0]){
								img_obj.src = window.URL.createObjectURL(document.all.iti_img.files[0]);
							}
						}
					}
					
					function iti_img_change_byId(id) {
                        $("#default_btn").prev().removeAttr("disabled");
						var img_obj = document.getElementById("iti_img_id");
						var file_obj = document.getElementById("file_photo_url");
						if(id === "remain_image") {
							<!-- $("#default_btn").prev().attr("disabled","disabled"); -->
							img_obj.src = "../<xsl:value-of select="$val"/>";
							document.frmXml.remain_photo_ind.value = "true";
						} else if(id === "default_image") {
							clearFileInput(file_obj);
							//img_obj.src = wb_utils_app_base + "<xsl:value-of select="$default_usr_icon"/>";
							document.frmXml.remain_photo_ind.value = "false";
						} else {
							<!-- $("#default_btn").prev().attr("disabled","disabled"); -->
							file_obj.disabled = false;
							document.frmXml.remain_photo_ind.value = "false";
							if(document.all.iti_img.files &amp;&amp; document.all.iti_img.files[0]){
								img_obj.src = window.URL.createObjectURL(document.all.iti_img.files[0]);
							}
						}
					}
					
					function previewLocalImage(obj) {
						var img_obj = document.getElementById("iti_img_id");
						var types = ["jpg", "gif", "png"];
						var file_type = obj.files[0].name.substring(obj.files[0].name.lastIndexOf('.') + 1).toLowerCase();
						var ret = false;
						//alert(obj.files[0].name.substring(obj.files[0].name.lastIndexOf('.') + 1).toLowerCase());
					    for(var i=0; i &lt; types.length; i++) {
							if(file_type === types[i]) {
								ret = true;
							}
						}
						if(ret){
						 img_obj.src = window.URL.createObjectURL(obj.files[0]);
						}
						//img_obj.src = window.URL.createObjectURL(obj.files[0]);
					}
					
				    var defaultImage="<xsl:value-of select="$val"/>";
					$(function(){
						//初始化图库,使用回调函数是因为避免图库未加载好，默认图片的函数已经先调用了
						initDefaultImage('user', 'user', false, function(){
						    if(defaultImage == 'user/instructor_default.png'){
						       useDefaultImage();
						    }
						});
					})
					
				</script>
				<table cellpadding="0" cellspacing="0" border="0">
					
					<tr>
						 <xsl:choose>
						     <xsl:when test="$val != 'user/instructor_default.png'">
						        <td rowspan="4" width="108px">
							      <img src="../{$val}" id="iti_img_id" name="user_preview" border="0" width="82" height="82"  style="border-radius: 50%;"/>
							    </td>
								<td>
									<label for="remain_image">
										<input type="radio" checked="checked" id="remain_image" name="iti_img_select" onclick="iti_img_change(this)"/>
										<span class="Text">
											<xsl:value-of select="$lab_remain_image"/>
										</span>
										<input name="iti_img_hidden" type="hidden" value="{$val}"/>
										<input name="remain_photo_ind" type="hidden" value="true"/>
									</label>
								</td>
						     </xsl:when>
						     <xsl:otherwise>
						        <td rowspan="4" width="108px">
							      <img src="" id="iti_img_id" name="user_preview" border="0" width="82" height="82" style="border-radius: 50%;"/>
							    </td>
						     </xsl:otherwise>
						 </xsl:choose>
					</tr>
					
					<tr>
						<td>
							<label for="default_image">
							     <xsl:choose>
								     <xsl:when test="$val != 'user/instructor_default.png'">
								        <input  type="radio" id="default_image" name="iti_img_select" value="use_default_image" onclick="useDefaultImage();iti_img_change(this);"/>
								     </xsl:when>
								     <xsl:otherwise>
								        <input  type="radio" checked="checked" id="default_image" name="iti_img_select" value="use_default_image" onclick="useDefaultImage();iti_img_change(this);"/>
								     </xsl:otherwise>
								 </xsl:choose>
								<input name="" type="button"  class="wzb-btn-blue "  style="border: 1px solid transparent;padding:3px 8px;" onclick="this.parentNode.firstChild.checked=true;show_default_image();useDefaultImage();iti_img_change_byId('default_image');" value="{$lab_select_default_image}"/>
								<a id="default_btn" href="#TB_inline?height=380&amp;width=580&amp;inlineId=myOnPageContent" class="thickbox" style="display: none;"></a>
								<input type="hidden" name="default_image"/>
								<input type="hidden" name="{@paramname}_del_ind"/>
							</label>
							<br/>
							<div id="myOnPageContent" style="display: none;">
								<div class="thickbox-big ">
									<div class="thickbox-tit  thickbox-tit-1" >
											<xsl:value-of select="$lab_default_images"/>
									</div>
								</div>
	
								<div class="thickbox-cont thickbox-user clearfix thickbox-content-2" id="defaultImages"></div>
	
								<div class="norm-border thickbox-footer ">
									<input type="button" class="btn wzb-btn-blue wzb-btn-big margin-right10" name="pertxt" onclick="selectImage()" value="{$lab_button_ok}" />
									<input type="button" class="norm-btn-1 TB_closeWindowButton swop_bg btn wzb-btn-blue wzb-btn-big" name="pertxt" value="{$lab_g_form_btn_cancel}" />
								</div>
							</div>
						</td>
					</tr>
					
					
					
					
					<tr>
						<td>
							<label for="local_image">
								<input type="radio" id="local_image" name="iti_img_select" onclick="iti_img_change(this)"/>
								<span class="Text">
									<xsl:value-of select="$lab_local_image"/>
								</span>
							</label>
						</td>
					</tr>
					<tr>
						<td>
							<xsl:call-template name="wb_gen_input_file">
								<xsl:with-param name="id">file_photo_url</xsl:with-param>
								<xsl:with-param name="name">iti_img</xsl:with-param>
								<!-- <xsl:with-param name="disabled">disabled</xsl:with-param> -->
								<xsl:with-param name="onclick">document.frmXml.local_image.checked=true;iti_img_change_byId('local_image');</xsl:with-param>
								<xsl:with-param name="onchange">previewLocalImage(this)</xsl:with-param>
							</xsl:call-template>
							<span class="text">
								&#160;<xsl:value-of select="$img_des"/>
							</span>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<input type="hidden" name="lab_iti_img" value="{$lab}"/>
		<input type="hidden" name="upd_iti_img" value="true" />
	</xsl:template>

	<xsl:template name="view_field_tpl">
		<xsl:param name="lab" />
		<xsl:param name="val" />
		<td class="wzb-form-label">
			<xsl:value-of select="$lab" />
			<xsl:text>：</xsl:text>
		</td>
		<td class="wzb-form-control">
			<xsl:choose>
				<xsl:when test="$val != ''"><xsl:value-of select="$val"></xsl:value-of></xsl:when>
				<xsl:otherwise>--</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>

	<xsl:template name="view_cust_field_tpl">
		<xsl:param name="lab" />
		<xsl:param name="val" />
		<xsl:param name="width">80%</xsl:param>
		<xsl:param name="colspan">1</xsl:param>
		<td style="padding:10px 0; text-align:right; color:#666;" valign="top">
			<xsl:value-of select="$lab" />
			<xsl:text>：</xsl:text>
		</td>
		<td style="padding:10px 0 10px 10px; color:#333;" colspan="{$colspan}">
			<xsl:copy-of select="$val" />
		</td>
	</xsl:template>
	
	<xsl:template name="view_field_opt_tpl">
		<xsl:param name="lab" />
		<xsl:param name="val" />
		<xsl:param name="options" />
		<xsl:param name="width">80%</xsl:param>
		<xsl:param name="colspan">1</xsl:param>
		
		<xsl:variable name="opt_text">
			<xsl:choose>
				<xsl:when test="$options and count($options/option[@value=$val]) &gt;0">
					<xsl:value-of select="$options/option[@value=$val]/text()" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>--</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:call-template name="view_field_tpl">
			<xsl:with-param name="lab" select="$lab"/>
			<xsl:with-param name="width" select="$width"/>
			<xsl:with-param name="val" select="$opt_text"/>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="view_field_date_tpl">
		<xsl:param name="lab" />
		<xsl:param name="val" />
		<xsl:param name="width">80%</xsl:param>
		<xsl:param name="colspan">1</xsl:param>

		<xsl:call-template name="view_field_tpl">
			<xsl:with-param name="lab" select="$lab" />
			<xsl:with-param name="width" select="$width"/>
			<xsl:with-param name="val">
				<xsl:if test="$val != ''">
					<xsl:call-template name="display_time">
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="$val" />
						</xsl:with-param>
					</xsl:call-template>
				</xsl:if>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template name="view_field_text_tpl">
		<xsl:param name="lab" />
		<xsl:param name="val" />
		<xsl:param name="colspan">1</xsl:param>
		<xsl:param name="width">80%</xsl:param>
		<td style="padding:10px 0; text-align:right; color:#666;" valign="top">
			<xsl:value-of select="$lab" />
			<xsl:text>：</xsl:text>
		</td>
		<td style="padding:10px 0 10px 10px; color:#333;" colspan="{$colspan}" valign="top">
			<xsl:choose>
				<xsl:when test="$val != ''"><xsl:value-of select="$val"></xsl:value-of></xsl:when>
				<xsl:otherwise>--</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	
	<xsl:template name="view_field_attr_tpl">
		<xsl:param name="lab" />
		<xsl:param name="attrs" />
		<xsl:param name="type" />
		<xsl:param name="relation_type" />
		<xsl:param name="colspan">1</xsl:param>
		<xsl:param name="width">80%</xsl:param>
		<td style="padding:10px 0; text-align:right; color:#666;" valign="top">
			<xsl:value-of select="$lab" />
			<xsl:text>：</xsl:text>
		</td>
		<td style="padding:10px 0 10px 10px; color:#333;" colspan="{$colspan}" valign="top">
			<xsl:choose>
				<xsl:when test="count($attrs/attribute_list[@type = $type]/entity[@relation_type = $relation_type]) &gt;0">
					<xsl:for-each select="$attrs/attribute_list[@type = 'UGR']/entity[@relation_type = 'USR_CURRENT_UGR']">
						<xsl:if test="@display_bil and @display_bil != ''">
							<xsl:value-of select="@display_bil" />
						</xsl:if>
						<xsl:if test="position() != last()">
							<xsl:text>,&#160;</xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:when>
			</xsl:choose>
		</td>
	</xsl:template>
</xsl:stylesheet>