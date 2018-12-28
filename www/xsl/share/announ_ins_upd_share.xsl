<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- ========================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_add_new_ann">新增通告</xsl:with-param>
			<xsl:with-param name="lab_adm_home">管理員平台</xsl:with-param>
			<xsl:with-param name="lab_upd_ann">
			<xsl:choose>
					<xsl:when test="$isMobile='true'">修改移动通告</xsl:when>
					<xsl:otherwise>修改通告</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="lab_msg_title">標題</xsl:with-param>
			<xsl:with-param name="lab_msg_icon">圖示</xsl:with-param>
			<xsl:with-param name="lab_msg_body">內容</xsl:with-param>
			<xsl:with-param name="lab_msg_begin_date">開始時間</xsl:with-param>
			<xsl:with-param name="lab_msg_end_date">結束時間</xsl:with-param>
			<xsl:with-param name="lab_msg_receipt">是否需要已讀回條</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_msg_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_on">已發佈</xsl:with-param>
			<xsl:with-param name="lab_off">未發佈</xsl:with-param>
			<xsl:with-param name="lab_unlimit">不限</xsl:with-param>
			<xsl:with-param name="lab_immediate">即時</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">儲存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_tc">培訓中心</xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">你可以在“我的喜好”中設置“主要培訓中心”<br/>設置後，在任何指定培訓中心的操作中，將默認的指定爲設定的主要培訓中心</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_add_new_ann">添加公告</xsl:with-param>
			<xsl:with-param name="lab_adm_home">管理员平台</xsl:with-param>
			<xsl:with-param name="lab_upd_ann">
				<xsl:choose>
					<xsl:when test="$isMobile='true'">修改移动公告</xsl:when>
					<xsl:otherwise>修改公告</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="lab_msg_title">标题</xsl:with-param>
			<xsl:with-param name="lab_msg_icon">图示</xsl:with-param>
			<xsl:with-param name="lab_msg_body">内容</xsl:with-param>
			<xsl:with-param name="lab_msg_begin_date">开始时间</xsl:with-param>
			<xsl:with-param name="lab_msg_end_date">结束时间</xsl:with-param>
			<xsl:with-param name="lab_msg_receipt">是否收回执</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_msg_status">状态</xsl:with-param>
			<xsl:with-param name="lab_on">已发布</xsl:with-param>
			<xsl:with-param name="lab_off">未发布</xsl:with-param>
			<xsl:with-param name="lab_unlimit">不限</xsl:with-param>
			<xsl:with-param name="lab_immediate">即时</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">保存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_tc">培训中心</xsl:with-param>
	       <xsl:with-param name="lab_def_tc_desc">你可以在“我的喜好”中设置“主要培训中心”<br/>设置后，在任何指定培训中心的操作中，将默认的指定为设定的主要培训中心</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_add_new_ann">Add notice</xsl:with-param>
			<xsl:with-param name="lab_adm_home">Administrator home</xsl:with-param>
			<xsl:with-param name="lab_upd_ann">
				<xsl:choose>
					<xsl:when test="$isMobile='true'">Edit mobile notice</xsl:when>
					<xsl:otherwise>Edit notice</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="lab_msg_title">Title</xsl:with-param>
			<xsl:with-param name="lab_msg_icon">Icon</xsl:with-param>
			<xsl:with-param name="lab_msg_body">Content</xsl:with-param>
			<xsl:with-param name="lab_msg_begin_date">From</xsl:with-param>
			<xsl:with-param name="lab_msg_end_date">To</xsl:with-param>
			<xsl:with-param name="lab_msg_receipt">Require Receipt</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
			<xsl:with-param name="lab_msg_status">Status</xsl:with-param>
			<xsl:with-param name="lab_on">Published</xsl:with-param>
			<xsl:with-param name="lab_off">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_unlimit">Unlimited</xsl:with-param>
			<xsl:with-param name="lab_immediate">Immediate</xsl:with-param>			
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_tc">Training center </xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">You can specify “Major training center” in “My preference”. <br/>After that, your major training center will be default selected wherever a selection of training center is needed.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ========================================================= -->
	<xsl:template name="ann_body">
		<xsl:param name="mode"/>
		<xsl:param name="lab_msg_title"/>	
		<xsl:param name="lab_msg_icon"/>	
		<xsl:param name="lab_msg_body"/>
		<xsl:param name="lab_msg_begin_date"/>
		<xsl:param name="lab_msg_end_date"/>
		<xsl:param name="lab_msg_receipt"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_msg_status"/>
		<xsl:param name="lab_on"/>
		<xsl:param name="lab_off"/>
		<xsl:param name="lab_unlimit"/>
		<xsl:param name="lab_immediate"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_tc"/>
		<xsl:param name="gen_table_width"/>
		<xsl:param name="tc_enabled"/>
		<xsl:param name="is_show_all"/>
		<xsl:param name="lab_def_tc_desc"/>
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
		<table>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_msg_title"/>：
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:320px;height:30px;line-height:30px;border:1px solid #ccc;" name="msg_title" maxlength="255" prompt="请输入标题，不能超过80个字"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_msg_body"/>：
				</td>
				<td class="wzb-form-control">
					<!-- ===================================Start Content=================================== -->
					<xsl:call-template name="kindeditor_panel">
						<xsl:with-param name="body" select="body/text()"/>
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						<xsl:with-param name="fld_name">msg_body</xsl:with-param>
					</xsl:call-template>
					<!-- ====================================End Content===================================-->
				</td>
			</tr>
			<!-- 回执 -->
			<xsl:if test="$lab_msg_receipt">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_msg_receipt"/>：
				</td>
				<td class="wzb-form-control">
					<select name="msg_receipt" class="wzb-form-select">
					    <option value="No">
							<xsl:if test="@isReceipt='false' and $mode = 'upd'">
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$lab_no"/>
						</option>
						<option value="Yes">
							<xsl:if test="@isReceipt='true' and $mode = 'upd'">
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$lab_yes"/>
						</option>
					</select>
				</td>
			</tr>
			</xsl:if>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_msg_status"/>：
				</td>
				<td class="wzb-form-control">
					<select name="msg_status" class="wzb-form-select">
						<option value="ON">
							<xsl:if test="@status='ON' and $mode = 'upd'">
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$lab_on"/>
						</option>
						<option value="OFF">
							<xsl:if test="@status='OFF' and $mode = 'upd'">
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$lab_off"/>
						</option>
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
							<xsl:when test="not(//default_training_center)"><xsl:value-of select="//training_center/@id"/></xsl:when>
							<xsl:otherwise><xsl:value-of select="//default_training_center/@id"/></xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="cur_tcr_title">
						<xsl:choose>
							<xsl:when test="not(//default_training_center)"><xsl:value-of select="//training_center/title"/></xsl:when>
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
					<input type="hidden" name="msg_tcr_id"/>
				</tr>
			</xsl:if>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_msg_begin_date"/>：
				</td>
				<td class="wzb-form-control">
				<label for="rdo_start_date_0">
					<input id="rdo_start_date_0" type="radio" name="start_date" value="0">
					<xsl:if test="$mode != 'upd'"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
					</input>
					<xsl:value-of select="$lab_immediate"/>
				</label>
				</td>
			</tr>
			<tr>
				<td>&#160;</td>
				<td class="wzb-form-control">
					<label for="rdo_start_date_1">
						<input type="radio" name="start_date" value="1">
							<xsl:if test="$mode = 'upd'"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
						</input>
						
						<xsl:call-template name="display_form_input_time">
							<xsl:with-param name="fld_name">start</xsl:with-param>
							<xsl:with-param name="hidden_fld_name">msg_begin_date</xsl:with-param>
							<xsl:with-param name="frm">document.frmXml</xsl:with-param>
							<xsl:with-param name="show_label">Y</xsl:with-param>
							<xsl:with-param name="display_form_input_hhmm">Y</xsl:with-param>
							<xsl:with-param name="focus_rad_btn_name">start_date[1]</xsl:with-param>
						</xsl:call-template>
					</label>
					<xsl:if test="$mode = 'upd'">
						<xsl:if test="@begin_date != 'UNLIMITED'">
							<xsl:variable name="sub_begin_date" select="translate(substring-before(@begin_date,'.'),':- ','')"/>
							<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[										
								frm= document.frmXml										
								frm.start_dd.value=]]>'<xsl:value-of select="substring($sub_begin_date,7,2)"/>'<![CDATA[
								frm.start_mm.value=]]>'<xsl:value-of select="substring($sub_begin_date,5,2)"/>'<![CDATA[
								frm.start_yy.value=]]>'<xsl:value-of select="substring($sub_begin_date,1,4)"/>'<![CDATA[
								frm.start_hour.value=]]>'<xsl:value-of select="substring($sub_begin_date,9,2)"/>'<![CDATA[
								frm.start_min.value=]]>'<xsl:value-of select="substring($sub_begin_date,11,2)"/>'
							</script>
						</xsl:if>
					</xsl:if>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_msg_end_date"/>：
				</td>
				<td class="wzb-form-control">
				<label for="rdo_start_date_2">
					<input id="rdo_start_date_2" type="radio" name="end_date" value="0">
					<xsl:choose>
						<xsl:when test="$mode = 'upd'">
							<xsl:if test="@end_date = 'UNLIMITED'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise><xsl:attribute name="checked">checked</xsl:attribute></xsl:otherwise>
						</xsl:choose>
					</input>
					<xsl:value-of select="$lab_unlimit"/>
				</label>
				</td>
			</tr>
			<tr>
				<td>&#160;</td>
				<td class="wzb-form-control">
					<label for="rdo_start_date_3">
					<input id="rdo_start_date_3" type="radio" name="end_date" value="1">
						<xsl:if test="$mode = 'upd'">
							<xsl:if test="@end_date != 'UNLIMITED'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</xsl:if>
					</input>
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">end</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">msg_end_date</xsl:with-param>
						<xsl:with-param name="frm">document.frmXml</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
						<xsl:with-param name="display_form_input_hhmm">Y</xsl:with-param>
						<xsl:with-param name="focus_rad_btn_name">end_date[1]</xsl:with-param>
					</xsl:call-template>
					</label>
					<xsl:if test="$mode = 'upd'">
						<xsl:if test="@end_date != 'UNLIMITED'">
							<xsl:variable name="sub_end_date" select="translate(substring-before(@end_date,'.'),':- ','')"/>
							<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[										
									frm= document.frmXml										
									frm.end_dd.value=]]>'<xsl:value-of select="substring($sub_end_date,7,2)"/>'<![CDATA[
									frm.end_mm.value=]]>'<xsl:value-of select="substring($sub_end_date,5,2)"/>'<![CDATA[
									frm.end_yy.value=]]>'<xsl:value-of select="substring($sub_end_date,1,4)"/>'<![CDATA[
									frm.end_hour.value=]]>'<xsl:value-of select="substring($sub_end_date,9,2)"/>'<![CDATA[
									frm.end_min.value=]]>'<xsl:value-of select="substring($sub_end_date,11,2)"/>'
							</script>
						</xsl:if>
					</xsl:if>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
				</td>
				<td class="wzb-ui-module-text">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:add_ann()</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
			</xsl:call-template>
		</div>
		<input type="hidden" name="lan_encoding_cnt" value=""/>
		<xsl:if test="$mode = 'upd'">
			<input type="hidden" name="msg_id" value="{@id}"/>
		</xsl:if>
		<input type="hidden" name="res_id" value="{@res_id}"/>
		<input type="hidden" name="msg_type" value="{@type}"/>
		<input type="hidden" name="env" value="wizb"/>
		<input name="cmd" type="hidden">
			<xsl:attribute name="value"><xsl:choose><xsl:when test="$mode = 'upd'">upd_msg</xsl:when><xsl:otherwise>ins_msg</xsl:otherwise></xsl:choose></xsl:attribute>
		</input>
		<input value="{@timestamp}" name="msg_timestamp" type="hidden"/>
		<input type="hidden" name="url_success" value=""/>
		<input type="hidden" name="url_failure" value=""/>
		<input type="hidden" name="cur_dt" value=""/>
		<input type="hidden" name="cur_dt_dd" value=""/>
		<input type="hidden" name="cur_dt_mm" value=""/>
		<input type="hidden" name="cur_dt_yy" value=""/>
		<input type="hidden" name="cur_dt_hour" value=""/>
		<input type="hidden" name="cur_dt_min" value=""/>
		<input type="hidden" name="msg_text" value=""/>
		<input type="hidden" name="isReceipt" value=""/>
		<input name="isMobile" type="hidden">
			<xsl:attribute name="value"><xsl:value-of select="$isMobile"/></xsl:attribute>
		</input>
	</xsl:template>
<!--	<xsl:template name="escape_upper_start_script">
		<xsl:param name="my_right_value"/>
		<xsl:param name="my_left_value"/>
		<xsl:variable name="bef_value" select="substring-before($my_right_value,'&lt;SCRIPT')"/>
		<xsl:variable name="aft_value" select="substring-after($my_right_value,'&lt;SCRIPT')"/>
		<xsl:choose>
			<xsl:when test="$bef_value = '' and $aft_value = '' and not(contains($my_right_value,'&lt;SCRIPT'))">
				<xsl:value-of select="concat($my_left_value, $my_right_value)"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="my_left_value" select="concat($my_left_value, $bef_value, '&lt;start_script')"/>
				<xsl:call-template name="escape_upper_start_script">
					<xsl:with-param name="my_right_value"><xsl:value-of select="$aft_value"/></xsl:with-param>
					<xsl:with-param name="my_left_value"><xsl:value-of select="$my_left_value"/></xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>-->
	<!-- ========================================================= -->
	<!--<xsl:template name="escape_lower_start_script">
		<xsl:param name="my_right_value"/>
		<xsl:param name="my_left_value"/>
		<xsl:variable name="bef_value" select="substring-before($my_right_value,'&lt;script')"/>
		<xsl:variable name="aft_value" select="substring-after($my_right_value,'&lt;script')"/>
		<xsl:choose>
			<xsl:when test="$bef_value = '' and $aft_value = '' and not(contains($my_right_value,'&lt;script'))">
				<xsl:value-of select="concat($my_left_value, $my_right_value)"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="my_left_value" select="concat($my_left_value, $bef_value, '&lt;start_script')"/>
				<xsl:call-template name="escape_lower_start_script">
					<xsl:with-param name="my_right_value"><xsl:value-of select="$aft_value"/></xsl:with-param>
					<xsl:with-param name="my_left_value"><xsl:value-of select="$my_left_value"/></xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>-->
	<!-- ========================================================= -->
	<!--<xsl:template name="escape_upper_end_script">
		<xsl:param name="my_right_value"/>
		<xsl:param name="my_left_value"/>
		<xsl:variable name="bef_value" select="substring-before($my_right_value,'/SCRIPT')"/>
		<xsl:variable name="aft_value" select="substring-after($my_right_value,'/SCRIPT')"/>
		<xsl:choose>
			<xsl:when test="$bef_value = '' and $aft_value = '' and not(contains($my_right_value,'/SCRIPT'))">
				<xsl:value-of select="concat($my_left_value, $my_right_value)"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="my_left_value" select="concat($my_left_value, $bef_value, '/end_script')"/>
				<xsl:call-template name="escape_upper_end_script">
					<xsl:with-param name="my_right_value"><xsl:value-of select="$aft_value"/></xsl:with-param>
					<xsl:with-param name="my_left_value"><xsl:value-of select="$my_left_value"/></xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>-->
	<!-- ========================================================= -->
	<!--<xsl:template name="escape_lower_end_script">
		<xsl:param name="my_right_value"/>
		<xsl:param name="my_left_value"/>
		<xsl:variable name="bef_value" select="substring-before($my_right_value,'/script')"/>
		<xsl:variable name="aft_value" select="substring-after($my_right_value,'/Script')"/>
		<xsl:choose>
			<xsl:when test="$bef_value = '' and $aft_value = '' and not(contains($my_right_value,'/script'))">
				<xsl:value-of select="concat($my_left_value, $my_right_value)"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="my_left_value" select="concat($my_left_value, $bef_value, '/end_script')"/>
				<xsl:call-template name="escape_lower_end_script">
					<xsl:with-param name="my_right_value"><xsl:value-of select="$aft_value"/></xsl:with-param>
					<xsl:with-param name="my_left_value"><xsl:value-of select="$my_left_value"/></xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>-->
	<!-- ========================================================= -->
	<!--<xsl:template name="escape_script_word">
		<xsl:param name="input_str"/>
		<xsl:variable name="next_str">
			<xsl:call-template name="escape_upper_start_script">
				<xsl:with-param name="my_right_value"><xsl:value-of select="$input_str"/></xsl:with-param>
				<xsl:with-param name="my_left_value"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="next_next_str">
			<xsl:call-template name="escape_lower_start_script">
				<xsl:with-param name="my_right_value"><xsl:value-of select="$next_str"/></xsl:with-param>
				<xsl:with-param name="my_left_value"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="next_next_next_str">
			<xsl:call-template name="escape_upper_end_script">
				<xsl:with-param name="my_right_value"><xsl:value-of select="$next_next_str"/></xsl:with-param>
				<xsl:with-param name="my_left_value"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="final_str">
			<xsl:call-template name="escape_lower_end_script">
				<xsl:with-param name="my_right_value"><xsl:value-of select="$next_next_next_str"/></xsl:with-param>
				<xsl:with-param name="my_left_value"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:value-of select="$final_str"/>
	</xsl:template>-->
	<!-- ========================================================= -->
</xsl:stylesheet>
