<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="escape_js.xsl"/>
	<xsl:import href="goldenman_draw_button.xsl"/>

<!-- =============================================================== -->
<xsl:template name="wb_goldenman">
	<xsl:param name="search">false</xsl:param>
	<xsl:param name="search_function"></xsl:param>
	<xsl:param name="frm">document.frmXml</xsl:param>
	<xsl:param name="name">name</xsl:param>
	<xsl:param name="width">300</xsl:param>	
	<xsl:param name="height">120</xsl:param>	 <!--ie9中文本域显示高度不够  -->
	<xsl:param name="field_name"></xsl:param>
	<xsl:param name="add_btn">true</xsl:param>
	<xsl:param name="add_btn_img"></xsl:param>
	<xsl:param name="remove_btn">true</xsl:param>
	<xsl:param name="remove_btn_img"></xsl:param>
	<xsl:param name="move_up_btn">false</xsl:param>
	<xsl:param name="move_down_btn">false</xsl:param>
	<xsl:param name="label_add_btn"><xsl:value-of select="$lab_gen_select"/></xsl:param>
	<xsl:param name="label_remove_btn"><xsl:value-of select="$lab_gen_remove"/></xsl:param>
	<xsl:param name="label_search_btn"><xsl:value-of select="$lab_gen_search"/></xsl:param>
	<xsl:param name="get_supervise_group">0</xsl:param>
	<xsl:param name="get_direct_supervise">0</xsl:param>
	<xsl:param name="button_list"></xsl:param>
	<xsl:param name="button_list_img"></xsl:param>
	<xsl:param name="right_hand_side">false</xsl:param>
	<xsl:param name="gen_btn_multi_cnt">0</xsl:param>
	<xsl:param name="tree_type">item</xsl:param>
	<xsl:param name="tree_subtype"/>
	<xsl:param name="select_type">2</xsl:param>
	<xsl:param name="max_size">0</xsl:param>
	<xsl:param name="show_box">true</xsl:param>
	<xsl:param name="box_size"></xsl:param>
	<xsl:param name="option_list"></xsl:param>
	<xsl:param name="single_option_text"></xsl:param>
	<xsl:param name="single_option_value"></xsl:param>
	<xsl:param name="single_option_desc"></xsl:param>
	<xsl:param name="pick_leave">1</xsl:param>
	<xsl:param name="approved_list"></xsl:param>
	<xsl:param name="flag"></xsl:param>
	<xsl:param name="close_option">1</xsl:param>
	<xsl:param name="pick_root">1</xsl:param>
	<xsl:param name="args_type">col</xsl:param>
	<xsl:param name="complusory_tree">1</xsl:param>
	<xsl:param name="override_appr_usg">0</xsl:param>
	<xsl:param name="ftn_ext_id"></xsl:param>
	<xsl:param name="rol_ext_id"></xsl:param>
	<xsl:param name="parent_tcr_id">0</xsl:param>
	<xsl:param name="filter_user_group">1</xsl:param>
	<xsl:param name="confirm_function"/>
	<xsl:param name="confirm_msg"/>
	<xsl:param name="itm_id">0</xsl:param>
	<xsl:param name="tc_id">0</xsl:param>
	<xsl:param name="add_function">goldenman.opentree('<xsl:value-of select="$tree_type"/>',<xsl:value-of select="$select_type"/>,'<xsl:value-of select="$field_name"/>','','<xsl:value-of select="$pick_leave"/>','<xsl:value-of select="$approved_list"/>','<xsl:value-of select="$flag"/>','<xsl:value-of select="$close_option"/>','<xsl:value-of select="$pick_root"/>', '<xsl:value-of select="$override_appr_usg"/>', '<xsl:value-of select="$tree_subtype"/>', '<xsl:value-of select="$get_supervise_group"/>','<xsl:value-of select="$complusory_tree"/>', '<xsl:value-of select="$get_direct_supervise"/>', '<xsl:value-of select="$ftn_ext_id"/>', '', '<xsl:value-of select="$parent_tcr_id"/>', '<xsl:value-of select="$filter_user_group"/>', '<xsl:value-of select="$confirm_function"/>', '<xsl:value-of select="$confirm_msg"/>','','',<xsl:value-of select="$itm_id"/>, '<xsl:value-of select="$rol_ext_id"/>','<xsl:value-of select="$tc_id"/>')</xsl:param>
	<xsl:param name="custom_js_code"/>
	<xsl:param name="custom_js_code_extra"/>
	<xsl:param name="box_multiple">true</xsl:param>
	<xsl:param name="remove_function"/>
	<xsl:param name="extra_remove_function"/>
	<!-- extra remove function will call after remove button click , it is NOT replacement of remove function-->
	<xsl:param name="extra_add_function"/>
	<xsl:param name="inq_by_user">false</xsl:param>


	<xsl:variable name="size">
		<xsl:choose>
			<xsl:when test="$box_size != ''"><xsl:value-of select="$box_size"/></xsl:when>
			<xsl:when test="$max_size &gt; 5">6</xsl:when>
			<xsl:when test="$max_size = ''">6</xsl:when>
			<xsl:otherwise><xsl:value-of select="$max_size"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="size_var">
		<xsl:choose>
			<xsl:when test="$max_size = ''">0</xsl:when>
			<xsl:otherwise><xsl:value-of select="$max_size"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="_remove_function">
		<xsl:choose>
			<xsl:when test="$remove_function!=''"><xsl:value-of select="$remove_function"/></xsl:when>
			<xsl:when test="$size = 1">RemoveSingleOption(<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>_single,<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>)</xsl:when>
			<xsl:otherwise>removeSelectedOptions(<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>)</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:choose>
		<xsl:when test="$size = 1">
			<table>
				<tr>
					<td width="20%">
						<xsl:value-of select="$rol_ext_id"/>
						<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/><![CDATA[=  new wbSingleGoldenMan;
							function ]]><xsl:value-of select="$field_name"/><![CDATA[(){
								var args = ]]><xsl:value-of select="$field_name"/><![CDATA[.arguments
								]]><xsl:value-of select="$custom_js_code_extra"/><![CDATA[
								AddSingleOption(]]><xsl:value-of select="$frm"/><![CDATA[.]]><xsl:value-of select="$field_name"/><![CDATA[,args,]]>'<xsl:value-of select="$args_type"/>'<![CDATA[)
								]]><xsl:value-of select="$frm"/><![CDATA[.]]><xsl:value-of select="$field_name"/><![CDATA[_single.value = ]]><xsl:value-of select="$frm"/><![CDATA[.]]><xsl:value-of select="$field_name"/><![CDATA[.options[0].text
								]]><xsl:value-of select="$custom_js_code"/><![CDATA[
							}
						]]></SCRIPT>
						<input type="text" name="{$field_name}_single" style="width:{$width}px" class="wzb-inputText">
							<xsl:if test="$inq_by_user = 'false'">
								<xsl:attribute name="readonly">readonly</xsl:attribute>
							</xsl:if>
						</input>
					</td>
					<td width="80%" align="left">
						<xsl:if test="$add_btn = 'true'">
							<xsl:choose>
								<xsl:when test="$add_btn_img=''">
									<input onClick="{$add_function}" class="btn wzb-btn-blue margin-right4 margin-bottom1" value="{$label_add_btn}" name="genadd{$name}" type="button"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$add_btn_img"/></xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:<xsl:value-of select="$add_function"/></xsl:with-param>
										<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
										<xsl:with-param name="class">btn wzb-btn-blue margin-right4</xsl:with-param>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
						<xsl:if test="($search= 'true') and ($remove_btn = 'true')"><!--<img border="0" height="1" width="2" src="{$wb_img_path}tp.gif"/>--></xsl:if>
						<xsl:if test="$search = 'true'"><input  onClick="{$search_function}" class="btn  wzb-btn wzb-btn-blue margin-right4" value="{$label_search_btn}" type="button" name="gensearch{$name}"/></xsl:if>
						<xsl:if test="($add_btn = 'true') and ($remove_btn = 'true')"><!--  <img border="0" height="1" width="2" src="{$wb_img_path}tp.gif"/>--></xsl:if>
						<xsl:if test="$remove_btn = 'true'">
							<xsl:choose>
								<xsl:when test="$remove_btn_img=''">
									<input onClick="{$_remove_function};{$extra_remove_function}" class="btn wzb-btn-blue margin-right4 margin-bottom1" value="{$label_remove_btn}" name="genremove{$name}" type="button"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$remove_btn_img"/></xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">Javascript:<xsl:value-of select="$_remove_function"/>;<xsl:value-of select="$extra_remove_function"/></xsl:with-param>
										<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
										<xsl:with-param name="class">btn wzb-btn-blue </xsl:with-param>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
						<script LANGUAGE="JavaScript" TYPE="text/javascript">
							<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>.options =  new Array()
							<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>.options[0] =  new wbGoldenOption()
							<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>.options[0].text =  '<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$single_option_text"/></xsl:with-param></xsl:call-template>'
							<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>_single.value =  '<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$single_option_text"/></xsl:with-param></xsl:call-template>'
							<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>.options[0].value =  '<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$single_option_value"/></xsl:with-param></xsl:call-template>'
						</script>
					</td>
				</tr>
				<xsl:if test="$single_option_desc != ''">
					<tr>
						<td><xsl:value-of select="$single_option_desc"/></td>
					</tr>
				</xsl:if>
			</table>
		</xsl:when>
		<xsl:otherwise>
			<xsl:variable name="colspan_size">
				<xsl:choose>
					<xsl:when test="$right_hand_side = 'true'">2</xsl:when>
					<xsl:otherwise>1</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<table width="{$width}" border="0" cellspacing="0" cellpadding="0" height="80">
				<tr>
					<td  valign="top">  
						<xsl:choose>
							<xsl:when test="$show_box='true'">
								<table cellpadding="0" cellspacing="0" border="0">
									<tr>
										<td rowspan="2" valign="top">
											<span class="Text">
												<select width="{$width}" style="width:{$width}px;height:{$height}px;overflow-x: auto;" class="Select" size="{$size}" name="{$field_name}" >
													<xsl:if test="$box_multiple='true'">
														<xsl:attribute name="multiple">multiple</xsl:attribute>
													</xsl:if>
													<xsl:copy-of select="$option_list"/>
												</select>
											</span>
										</td>
										<td valign="top"><xsl:if test="$move_up_btn='true'"><a href="Javascript:goldenman.moveItemUp({$frm}.{$field_name})"><img src="{$wb_img_path}icon_up.gif" border="0"/></a></xsl:if></td>
									</tr>
									<tr>
										<td valign="bottom"><xsl:if test="$move_down_btn='true'"><a href="Javascript:goldenman.moveItemDown({$frm}.{$field_name})"><img src="{$wb_img_path}icon_down.gif" border="0"/></a></xsl:if></td>
									</tr>
								</table>
							</xsl:when>
							<xsl:otherwise>
								<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">
									<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/><![CDATA[= new wbMultipleGoldenMan]]>
									<xsl:value-of select="$option_list"/>
								</SCRIPT>
							</xsl:otherwise>
						</xsl:choose>
					</td>
					<xsl:if test=" $right_hand_side ='true'">
						<td valign="top">
							<!--  <img border="0" height="1" width="2" src="{$wb_img_path}tp.gif"/>-->
							<xsl:if test="$add_btn = 'true'">
								<xsl:choose>
									<xsl:when test="$add_btn_img=''">
										<input onClick="{$add_function}" class="btn wzb-btn-blue margin-right4" value="{$label_add_btn}" name="genadd{$name}" type="button"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$add_btn_img"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_href">Javascript:<xsl:value-of select="$add_function"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
											<xsl:with-param name="class">btn wzb-btn-blue margin-right4</xsl:with-param>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
							<xsl:if test="($search = 'true') and ($remove_btn = 'true')"><!-- <img border="0" height="1" width="2" src="{$wb_img_path}tp.gif"/> --></xsl:if>
							<xsl:if test="$search = 'true'"><input  onClick="{$search_function}" class="btn wzb-btn wzb-btn-blue margin-right4" value="{$label_search_btn}" type="button" name="gensearch{$name}"/></xsl:if>
								
							<xsl:if test="($add_btn = 'true') and ($remove_btn = 'true')"><!--  <img border="0" height="1" width="2" src="{$wb_img_path}tp.gif"/> --></xsl:if>
							<xsl:if test="$remove_btn = 'true'">
								<xsl:choose>
									<xsl:when test="$remove_btn_img=''">
										<input onClick="{$_remove_function};{$extra_remove_function}" class="btn wzb-btn-blue margin-right4" value="{$label_remove_btn}" name="genremove{$name}" type="button"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$remove_btn_img"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_href">Javascript:<xsl:value-of select="$_remove_function"/>;<xsl:value-of select="$extra_remove_function"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
											<xsl:with-param name="class">btn wzb-btn-blue </xsl:with-param>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
							<xsl:call-template name="goldenman_draw_button">
								<xsl:with-param name="button_list"><xsl:value-of select="$button_list"/></xsl:with-param>
								<xsl:with-param name="button_list_img"><xsl:value-of select="$button_list_img"/></xsl:with-param>
							</xsl:call-template>
						</td>
					</xsl:if>
				</tr>
				<tr>
					<td colspan="{$colspan_size}">
						<xsl:choose>
							<xsl:when test="$show_box='false'"><!--  <img border="0" height="0" width="0" src="{$wb_img_path}tp.gif"/>--></xsl:when>
							<xsl:otherwise><!--  <img border="0" height="7" width="1" src="{$wb_img_path}tp.gif"/>--></xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
				<xsl:if test="$right_hand_side != 'true'">
					<tr>
						<td colspan="{$colspan_size}" style="padding-top:5px;">
							<xsl:if test="$add_btn = 'true'">
								<xsl:choose>
									<xsl:when test="$add_btn_img=''">
										<input onClick="{$add_function};{$extra_add_function}" class="btn wzb-btn-blue margin-right4" value="{$label_add_btn}" name="genadd{$name}" type="button"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$add_btn_img"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_href">Javascript:<xsl:value-of select="$add_function"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
											<xsl:with-param name="class">btn wzb-btn-blue margin-right4</xsl:with-param>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
							<xsl:if test="($search = 'true') and ($remove_btn = 'true')"><!--  <img border="0" height="1" width="2" src="{$wb_img_path}tp.gif"/>--></xsl:if>
							<xsl:if test="$search = 'true'"><input  onClick="{$search_function}" class="btn  wzb-btn wzb-btn-blue margin-right4" value="{$label_search_btn}" type="button" name="gensearch{$name}"/></xsl:if>

							<xsl:if test="($add_btn = 'true') and ($remove_btn = 'true')"><!--  <img border="0" height="1" width="2" src="{$wb_img_path}tp.gif"/>--></xsl:if>
							<xsl:if test="$remove_btn = 'true'">
								<xsl:choose>
									<xsl:when test="$remove_btn_img=''">
										<input onClick="{$_remove_function};{$extra_remove_function}" class="btn wzb-btn-blue margin-right4" value="{$label_remove_btn}" name="genremove{$name}" type="button"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$remove_btn_img"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_href">Javascript:<xsl:value-of select="$_remove_function"/>);<xsl:value-of select="$extra_remove_function"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
											<xsl:with-param name="class">btn wzb-btn-blue </xsl:with-param>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
							<xsl:call-template name="goldenman_draw_button">
								<xsl:with-param name="button_list"><xsl:value-of select="$button_list"/></xsl:with-param>
								<xsl:with-param name="button_list_img"><xsl:value-of select="$button_list_img"/></xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
				</xsl:if>
				<tr>
					<td colspan="{$colspan_size}" style="padding-top:5px;">
						<xsl:choose>
							<xsl:when test="$show_box='false'"><!--  <img border="0" height="0" width="0" src="{$wb_img_path}tp.gif"/>--></xsl:when>
							<xsl:otherwise><!-- <img border="0" height="5" width="1" src="{$wb_img_path}tp.gif"/>--></xsl:otherwise>
						</xsl:choose>
						<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
							function ]]><xsl:value-of select="$field_name"/><![CDATA[(){
								var args = ]]><xsl:value-of select="$field_name"/><![CDATA[.arguments
								]]><xsl:value-of select="$custom_js_code_extra"/><![CDATA[
								AddTreeOption(]]><xsl:value-of select="$frm"/><![CDATA[.]]><xsl:value-of select="$field_name"/><![CDATA[,]]><xsl:value-of select="$size_var"/><![CDATA[,args,]]>'<xsl:value-of select="$args_type"/>'<![CDATA[)
								]]>
								<xsl:value-of select="$custom_js_code"/><![CDATA[
							}
						]]></SCRIPT>
					</td>
				</tr>
			</table>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>
<!--========================================================================================================-->
<xsl:template name="wb_goldenman_no_btn">
	<xsl:param name="search">false</xsl:param>
	<xsl:param name="search_function"></xsl:param>
	<xsl:param name="frm">document.frmXml</xsl:param>
	<xsl:param name="name">name</xsl:param>
	<xsl:param name="width">300</xsl:param>	
	<xsl:param name="height">120</xsl:param>	 <!--ie9中文本域显示高度不够  -->
	<xsl:param name="field_name"></xsl:param>
	<xsl:param name="add_btn">true</xsl:param>
	<xsl:param name="add_btn_img"></xsl:param>
	<xsl:param name="remove_btn">true</xsl:param>
	<xsl:param name="remove_btn_img"></xsl:param>
	<xsl:param name="move_up_btn">false</xsl:param>
	<xsl:param name="move_down_btn">false</xsl:param>
	<xsl:param name="label_add_btn"><xsl:value-of select="$lab_gen_select"/></xsl:param>
	<xsl:param name="label_remove_btn"><xsl:value-of select="$lab_gen_remove"/></xsl:param>
	<xsl:param name="label_search_btn"><xsl:value-of select="$lab_gen_search"/></xsl:param>
	<xsl:param name="get_supervise_group">0</xsl:param>
	<xsl:param name="get_direct_supervise">0</xsl:param>
	<xsl:param name="button_list"></xsl:param>
	<xsl:param name="button_list_img"></xsl:param>
	<xsl:param name="right_hand_side">false</xsl:param>
	<xsl:param name="gen_btn_multi_cnt">0</xsl:param>
	<xsl:param name="tree_type">item</xsl:param>
	<xsl:param name="tree_subtype"/>
	<xsl:param name="select_type">2</xsl:param>
	<xsl:param name="max_size">0</xsl:param>
	<xsl:param name="show_box">true</xsl:param>
	<xsl:param name="box_size"></xsl:param>
	<xsl:param name="option_list"></xsl:param>
	<xsl:param name="single_option_text"></xsl:param>
	<xsl:param name="single_option_value"></xsl:param>
	<xsl:param name="single_option_desc"></xsl:param>
	<xsl:param name="pick_leave">1</xsl:param>
	<xsl:param name="approved_list"></xsl:param>
	<xsl:param name="flag"></xsl:param>
	<xsl:param name="close_option">1</xsl:param>
	<xsl:param name="pick_root">1</xsl:param>
	<xsl:param name="args_type">col</xsl:param>
	<xsl:param name="complusory_tree">1</xsl:param>
	<xsl:param name="override_appr_usg">0</xsl:param>
	<xsl:param name="ftn_ext_id"></xsl:param>
	<xsl:param name="rol_ext_id"></xsl:param>
	<xsl:param name="parent_tcr_id">0</xsl:param>
	<xsl:param name="filter_user_group">1</xsl:param>
	<xsl:param name="confirm_function"/>
	<xsl:param name="confirm_msg"/>
	<xsl:param name="itm_id">0</xsl:param>
	<xsl:param name="add_function">goldenman.opentree('<xsl:value-of select="$tree_type"/>',<xsl:value-of select="$select_type"/>,'<xsl:value-of select="$field_name"/>','','<xsl:value-of select="$pick_leave"/>','<xsl:value-of select="$approved_list"/>','<xsl:value-of select="$flag"/>','<xsl:value-of select="$close_option"/>','<xsl:value-of select="$pick_root"/>', '<xsl:value-of select="$override_appr_usg"/>', '<xsl:value-of select="$tree_subtype"/>', '<xsl:value-of select="$get_supervise_group"/>','<xsl:value-of select="$complusory_tree"/>', '<xsl:value-of select="$get_direct_supervise"/>', '<xsl:value-of select="$ftn_ext_id"/>', '', '<xsl:value-of select="$parent_tcr_id"/>', '<xsl:value-of select="$filter_user_group"/>', '<xsl:value-of select="$confirm_function"/>', '<xsl:value-of select="$confirm_msg"/>','','',<xsl:value-of select="$itm_id"/>, '<xsl:value-of select="$rol_ext_id"/>')</xsl:param>
	<xsl:param name="custom_js_code"/>
	<xsl:param name="custom_js_code_extra"/>
	<xsl:param name="box_multiple">true</xsl:param>
	<xsl:param name="remove_function"/>
	<xsl:param name="extra_remove_function"/>
	<!-- extra remove function will call after remove button click , it is NOT replacement of remove function-->
	<xsl:param name="extra_add_function"/>
	<xsl:param name="inq_by_user">false</xsl:param>


	<xsl:variable name="size">
		<xsl:choose>
			<xsl:when test="$box_size != ''"><xsl:value-of select="$box_size"/></xsl:when>
			<xsl:when test="$max_size &gt; 5">6</xsl:when>
			<xsl:when test="$max_size = ''">6</xsl:when>
			<xsl:otherwise><xsl:value-of select="$max_size"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="size_var">
		<xsl:choose>
			<xsl:when test="$max_size = ''">0</xsl:when>
			<xsl:otherwise><xsl:value-of select="$max_size"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="_remove_function">
		<xsl:choose>
			<xsl:when test="$remove_function!=''"><xsl:value-of select="$remove_function"/></xsl:when>
			<xsl:when test="$size = 1">RemoveSingleOption(<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>_single,<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>)</xsl:when>
			<xsl:otherwise>removeSelectedOptions(<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>)</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:choose>
		<xsl:when test="$size = 1">
			<table>
				<tr>
					<td width="20%">
						<xsl:value-of select="$rol_ext_id"/>
						<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/><![CDATA[=  new wbSingleGoldenMan;
							function ]]><xsl:value-of select="$field_name"/><![CDATA[(){
								var args = ]]><xsl:value-of select="$field_name"/><![CDATA[.arguments
								]]><xsl:value-of select="$custom_js_code_extra"/><![CDATA[
								AddSingleOption(]]><xsl:value-of select="$frm"/><![CDATA[.]]><xsl:value-of select="$field_name"/><![CDATA[,args,]]>'<xsl:value-of select="$args_type"/>'<![CDATA[)
								]]><xsl:value-of select="$frm"/><![CDATA[.]]><xsl:value-of select="$field_name"/><![CDATA[_single.value = ]]><xsl:value-of select="$frm"/><![CDATA[.]]><xsl:value-of select="$field_name"/><![CDATA[.options[0].text
								]]><xsl:value-of select="$custom_js_code"/><![CDATA[
							}
						]]></SCRIPT>
						<input type="text" name="{$field_name}_single" style="width:{$width}px" class="wzb-inputText">
							<xsl:if test="$inq_by_user = 'false'">
								<xsl:attribute name="readonly">readonly</xsl:attribute>
							</xsl:if>
						</input>
					</td>
					<td width="80%" align="left">
						<script LANGUAGE="JavaScript" TYPE="text/javascript">
							<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>.options =  new Array()
							<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>.options[0] =  new wbGoldenOption()
							<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>.options[0].text =  '<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$single_option_text"/></xsl:with-param></xsl:call-template>'
							<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>_single.value =  '<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$single_option_text"/></xsl:with-param></xsl:call-template>'
							<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/>.options[0].value =  '<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$single_option_value"/></xsl:with-param></xsl:call-template>'
						</script>
					</td>
				</tr>
				<xsl:if test="$single_option_desc != ''">
					<tr>
						<td><xsl:value-of select="$single_option_desc"/></td>
					</tr>
				</xsl:if>
			</table>
		</xsl:when>
		<xsl:otherwise>
			<xsl:variable name="colspan_size">
				<xsl:choose>
					<xsl:when test="$right_hand_side = 'true'">2</xsl:when>
					<xsl:otherwise>1</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<table width="{$width}" border="0" cellspacing="0" cellpadding="0" height="80">
				<tr>
					<td  valign="top">  
						<xsl:choose>
							<xsl:when test="$show_box='true'">
								<table cellpadding="0" cellspacing="0" border="0">
									<tr>
										<td rowspan="2" valign="top">
											<span class="Text">
												<select width="{$width}" style="width:{$width}px;height:{$height}px;overflow-x: auto;" class="Select" size="{$size}" name="{$field_name}" >
													<xsl:if test="$box_multiple='true'">
														<xsl:attribute name="multiple">multiple</xsl:attribute>
													</xsl:if>
													<xsl:copy-of select="$option_list"/>
												</select>
											</span>
										</td>
										<td valign="top"><xsl:if test="$move_up_btn='true'"><a href="Javascript:goldenman.moveItemUp({$frm}.{$field_name})"><img src="{$wb_img_path}icon_up.gif" border="0"/></a></xsl:if></td>
									</tr>
									<tr>
										<td valign="bottom"><xsl:if test="$move_down_btn='true'"><a href="Javascript:goldenman.moveItemDown({$frm}.{$field_name})"><img src="{$wb_img_path}icon_down.gif" border="0"/></a></xsl:if></td>
									</tr>
								</table>
							</xsl:when>
							<xsl:otherwise>
								<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">
									<xsl:value-of select="$frm"/>.<xsl:value-of select="$field_name"/><![CDATA[= new wbMultipleGoldenMan]]>
									<xsl:value-of select="$option_list"/>
								</SCRIPT>
							</xsl:otherwise>
						</xsl:choose>
					</td>
					<xsl:if test=" $right_hand_side ='true'">
						<td valign="top">
							<!--  <img border="0" height="1" width="2" src="{$wb_img_path}tp.gif"/>-->
							<xsl:if test="$add_btn = 'true'">
								<xsl:choose>
									<xsl:when test="$add_btn_img=''">
										<input onClick="{$add_function}" class="btn wzb-btn-blue margin-right4" value="{$label_add_btn}" name="genadd{$name}" type="button"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$add_btn_img"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_href">Javascript:<xsl:value-of select="$add_function"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
											<xsl:with-param name="class">btn wzb-btn-blue margin-right4</xsl:with-param>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
							<xsl:if test="($search = 'true') and ($remove_btn = 'true')"><!-- <img border="0" height="1" width="2" src="{$wb_img_path}tp.gif"/> --></xsl:if>
							<xsl:if test="$search = 'true'"><input  onClick="{$search_function}" class="btn wzb-btn wzb-btn-blue margin-right4" value="{$label_search_btn}" type="button" name="gensearch{$name}"/></xsl:if>
								
							<xsl:if test="($add_btn = 'true') and ($remove_btn = 'true')"><!--  <img border="0" height="1" width="2" src="{$wb_img_path}tp.gif"/> --></xsl:if>
							<xsl:if test="$remove_btn = 'true'">
								<xsl:choose>
									<xsl:when test="$remove_btn_img=''">
										<input onClick="{$_remove_function};{$extra_remove_function}" class="btn wzb-btn-blue margin-right4" value="{$label_remove_btn}" name="genremove{$name}" type="button"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$remove_btn_img"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_href">Javascript:<xsl:value-of select="$_remove_function"/>;<xsl:value-of select="$extra_remove_function"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
											<xsl:with-param name="class">btn wzb-btn-blue </xsl:with-param>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
							<xsl:call-template name="goldenman_draw_button">
								<xsl:with-param name="button_list"><xsl:value-of select="$button_list"/></xsl:with-param>
								<xsl:with-param name="button_list_img"><xsl:value-of select="$button_list_img"/></xsl:with-param>
							</xsl:call-template>
						</td>
					</xsl:if>
				</tr>
				<tr>
					<td colspan="{$colspan_size}">
						<xsl:choose>
							<xsl:when test="$show_box='false'"><!--  <img border="0" height="0" width="0" src="{$wb_img_path}tp.gif"/>--></xsl:when>
							<xsl:otherwise><!--  <img border="0" height="7" width="1" src="{$wb_img_path}tp.gif"/>--></xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
				<xsl:if test="$right_hand_side != 'true'">
					<tr>
						<td colspan="{$colspan_size}" style="padding-top:5px;">
							<xsl:if test="$add_btn = 'true'">
								<xsl:choose>
									<xsl:when test="$add_btn_img=''">
										<input onClick="{$add_function};{$extra_add_function}" class="btn wzb-btn-blue margin-right4" value="{$label_add_btn}" name="genadd{$name}" type="button"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$add_btn_img"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_href">Javascript:<xsl:value-of select="$add_function"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
											<xsl:with-param name="class">btn wzb-btn-blue margin-right4</xsl:with-param>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
							<xsl:if test="($search = 'true') and ($remove_btn = 'true')"><!--  <img border="0" height="1" width="2" src="{$wb_img_path}tp.gif"/>--></xsl:if>
							<xsl:if test="$search = 'true'"><input  onClick="{$search_function}" class="btn  wzb-btn wzb-btn-blue margin-right4" value="{$label_search_btn}" type="button" name="gensearch{$name}"/></xsl:if>

							<xsl:if test="($add_btn = 'true') and ($remove_btn = 'true')"><!--  <img border="0" height="1" width="2" src="{$wb_img_path}tp.gif"/>--></xsl:if>
							<xsl:if test="$remove_btn = 'true'">
								<xsl:choose>
									<xsl:when test="$remove_btn_img=''">
										<input onClick="{$_remove_function};{$extra_remove_function}" class="btn wzb-btn-blue margin-right4" value="{$label_remove_btn}" name="genremove{$name}" type="button"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$remove_btn_img"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_href">Javascript:<xsl:value-of select="$_remove_function"/>);<xsl:value-of select="$extra_remove_function"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
											<xsl:with-param name="class">btn wzb-btn-blue </xsl:with-param>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
							<xsl:call-template name="goldenman_draw_button">
								<xsl:with-param name="button_list"><xsl:value-of select="$button_list"/></xsl:with-param>
								<xsl:with-param name="button_list_img"><xsl:value-of select="$button_list_img"/></xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
				</xsl:if>
				<tr>
					<td colspan="{$colspan_size}" style="padding-top:5px;">
						<xsl:choose>
							<xsl:when test="$show_box='false'"><!--  <img border="0" height="0" width="0" src="{$wb_img_path}tp.gif"/>--></xsl:when>
							<xsl:otherwise><!-- <img border="0" height="5" width="1" src="{$wb_img_path}tp.gif"/>--></xsl:otherwise>
						</xsl:choose>
						<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
							function ]]><xsl:value-of select="$field_name"/><![CDATA[(){
								var args = ]]><xsl:value-of select="$field_name"/><![CDATA[.arguments
								]]><xsl:value-of select="$custom_js_code_extra"/><![CDATA[
								AddTreeOption(]]><xsl:value-of select="$frm"/><![CDATA[.]]><xsl:value-of select="$field_name"/><![CDATA[,]]><xsl:value-of select="$size_var"/><![CDATA[,args,]]>'<xsl:value-of select="$args_type"/>'<![CDATA[)
								]]>
								<xsl:value-of select="$custom_js_code"/><![CDATA[
							}
						]]></SCRIPT>
					</td>
				</tr>
			</table>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>
</xsl:stylesheet>
