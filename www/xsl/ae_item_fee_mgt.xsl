<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="itm_type" select="/applyeasy/item/@type"/>
	<xsl:variable name="cost_mgt" select="/applyeasy/meta/item_cost_management"/>
	<xsl:variable name="cur_lang">
	  <xsl:choose>
		<xsl:when test="$wb_lang= 'ch'">zh-hk</xsl:when>
		<xsl:when test="$wb_lang= 'gb'">zh-cn</xsl:when>
		<xsl:when test="$wb_lang= 'en'">en-us</xsl:when>
		<xsl:otherwise>en</xsl:otherwise>
	  </xsl:choose>
   </xsl:variable>
   <xsl:variable name="item_cost" select="/applyeasy/item/item_cost"/>
	<xsl:output indent="yes" />
	<!-- ================================================================== -->
	<xsl:template match="/">
		<xsl:call-template name="main"/>
	</xsl:template>
	<!--=================================================================== -->
	<xsl:template name="main">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title> 
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[
				itm_lst = new wbItem;
				function calcu_budget(frm){
					var total = 0;
					for(var i=0;i<frm.length;i++){
						if(frm[i].name.indexOf('budget')>-1 && frm[i].type == "text" && !frm[i].disabled && frm[i].type != "hidden" && frm[i].value.length > 0){
							if (!validate_float(frm[i])){
								return;
							} else {
								total = total + Number(frm[i].value);
							}
						}
					}
					frm.total_budget.value = Number((total).toFixed(2));
            }

				function calcu_actual(frm){	 
	               var total = 0;
	               for(var i=0;i<frm.length;i++){
	                    if(frm[i].name.indexOf('actual')>-1 && frm[i].type == "text" && !frm[i].disabled && frm[i].type != "hidden" && frm[i].value.length > 0){
							if (!validate_float(frm[i])){
								return;
							}
							total = total + Number(frm[i].value);
		                }
	                }
	               frm.total_actual.value = Number(total);
                }

              function getFocus(){
              				for(i=0;i<document.frmXml.elements.length;i++){
              					if (document.frmXml.elements[i].type == 'text' && document.frmXml.elements[i].disabled!="disabled") {
              						document.frmXml.elements[i].focus();
              						return;
              						}
              				}
              		}
              		
              	function validate_float(fld) {
					var val = wbUtilsTrimString(fld.value);
					if ( (val.search(/^\d{1,9}$/) == -1 && val.search(/^\d{1,9}\.\d{1,2}$/) == -1)) {
						return false;
					}
					return true;
				}
				 
			]]></script>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="getFocus();">
				<form name="frmXml">
					<input type="hidden" name="itm_id"/>
					<input type="hidden" name="cmd"/>
					<input type="hidden" name="url_success"/>
					<input name="url_failure" type="hidden"/>
					<input type="hidden" name="lang_value" value="{$cur_lang}"/>
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- ========================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="applyeasy/item">
			 <xsl:with-param name="lab_fee_mgt_title">課程費用</xsl:with-param>
			 <xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_budget_fee">預算</xsl:with-param>
			<xsl:with-param name="lab_actual_fee">實際</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_total_fee">合計</xsl:with-param>
		</xsl:apply-templates> 
	</xsl:template>

	<xsl:template name="lang_gb">
		<xsl:apply-templates select="applyeasy/item">
			<xsl:with-param name="lab_fee_mgt_title">课程费用</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_budget_fee">预算</xsl:with-param>
			<xsl:with-param name="lab_actual_fee">实际</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_total_fee">合计</xsl:with-param>
		</xsl:apply-templates> 
	</xsl:template>
	
	<xsl:template name="lang_en">
		<xsl:apply-templates select="applyeasy/item">
			<xsl:with-param name="lab_fee_mgt_title">Expenditure</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>information</xsl:with-param>
			<xsl:with-param name="lab_budget_fee">Budget</xsl:with-param>
			<xsl:with-param name="lab_actual_fee">Actual</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_total_fee">Total</xsl:with-param>
		</xsl:apply-templates> 
	</xsl:template>
	
	<!-- ============================================================================  -->
	
	<xsl:template match="applyeasy/item">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_fee_mgt_title"/>
		<xsl:param name="lab_budget_fee"/>
		<xsl:param name="lab_actual_fee"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_total_fee"/>
		
	    <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">107</xsl:with-param>
		</xsl:call-template>
        <div class="wzb-item-main">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="//itm_action_nav/hasTeachingCourse/text()='true'">
						<a href="javascript:itm_lst.get_itm_instr_view({//itm_action_nav/@itm_id})" class="NavLink">
							<xsl:value-of select="//itm_action_nav/@itm_title"/>
						</a>
					</xsl:when>
					<xsl:when test="@run_ind = 'false'">
						<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
							<xsl:value-of select="/applyeasy/item/title"/>
						</a>
		         	

					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="/applyeasy/item/nav/item" mode="nav">
							<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
							<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
						</xsl:apply-templates>
						
					</xsl:otherwise>
				</xsl:choose>
				<span class="NavLink">
		         		&#160;&gt;&#160;<xsl:value-of select="$lab_fee_mgt_title"/>
		         	</span>
			</xsl:with-param>
		</xsl:call-template>
		
		
		<xsl:call-template name="match_item_type">
			<xsl:with-param name="itm_type" select="item_type/@id"/>
			<xsl:with-param name="lab_budget_fee" select="$lab_budget_fee"/>
			<xsl:with-param name="lab_actual_fee" select="$lab_actual_fee"/>
			<xsl:with-param name="lab_g_form_btn_ok" select="$lab_g_form_btn_ok"/>
			<xsl:with-param name="lab_g_form_btn_cancel" select="$lab_g_form_btn_cancel"/>
			<xsl:with-param name="lab_total_fee" select="$lab_total_fee"/>
		</xsl:call-template>	
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="match_item_type">
		<xsl:param name="itm_type"/>
		<xsl:param name="lab_budget_fee"/>
		<xsl:param name="lab_actual_fee"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_total_fee"/>
		<xsl:for-each select="$cost_mgt/item_cost">
			<xsl:if test="@itm_type = $itm_type">
			    <xsl:call-template name="draw_fee_table">
					<xsl:with-param name="itm_type" select="$itm_type"/>
					<xsl:with-param name="lab_budget_fee" select="$lab_budget_fee"/>
			        <xsl:with-param name="lab_actual_fee" select="$lab_actual_fee"/>
					<xsl:with-param name="lab_total_fee" select="$lab_total_fee"/>
			    </xsl:call-template>
			</xsl:if>
		</xsl:for-each>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_g_form_btn_ok"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">Javascript:itm_lst.commit_change(frmXml,'<xsl:value-of select="$itm_id"/>', '<xsl:value-of select="$itm_type"/>')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
					<xsl:value-of select="$lab_g_form_btn_cancel"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">Javascript:history.back()</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="draw_fee_table">
		<xsl:param name="itm_type"/>
		<xsl:param name="lab_budget_fee"/>
		<xsl:param name="lab_actual_fee"/>
		<xsl:param name="lab_total_fee"/>
		<xsl:variable name="total_budget" select="format-number(sum($item_cost/cost/budget_fee[text()]),'#.00')"/>
		<xsl:variable name="total_actual" select="format-number(sum($item_cost/cost/actual_fee[text()]),'#.00')"/>
		<xsl:variable name="total_budget1">
			<xsl:choose>
				<xsl:when test="$total_budget > 0">
					<xsl:value-of select="format-number($total_budget,'#.00')"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="format-number($total_budget,'0.00')"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="total_actual1">
			<xsl:choose>
				<xsl:when test="$total_actual > 0">
					<xsl:value-of select="format-number($total_actual,'#.00')"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="format-number($total_actual,'0.00')"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<table>
		  <tr>
			<td height="10" width="30%">
			</td>
			<td align="left" width="15%" style="padding-left:10px;font-size:16px;"><xsl:value-of select="$lab_budget_fee"/></td>
			<td align="left" width="45%" style="font-size:16px;"><xsl:value-of select="$lab_actual_fee"/></td>
		  </tr>
			<xsl:apply-templates select="$cost_mgt/item_cost[@itm_type=$itm_type]/cost" />
			<!--caculate the total-->
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_total_fee"/><xsl:text>：</xsl:text>
			   </td>
			   <td align="left" style="padding:10px 0 10px 10px;">
				     <input class="wzb-inputText" style="width:150px;" disabled="disabled" type="text" name="total_budget" maxlength="12" value="{$total_budget1}"/>
			    </td>
				<td align="left">
			     	<input class="wzb-inputText" style="width:150px;" disabled="disabled" type="text" name="total_actual" maxlength="12" value="{$total_actual1}"/>
			    </td>
			</tr>
		</table>
		<!--<xsl:for-each select="$item_cost/cost">
			<input name="type_value{position()}" type="hidden">
				<xsl:attribute name="value">
					<xsl:value-of select="@type"/>
				</xsl:attribute>
			</input> 
		</xsl:for-each>-->
		<input name="cost_type_num" type="hidden">
			<xsl:attribute name="value">
				<xsl:value-of select="count($cost_mgt/item_cost[@itm_type=$itm_type]/cost)"/>
			</xsl:attribute>
		</input>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="cost">
		<xsl:variable name="budget_lab">budget</xsl:variable>
		<xsl:variable name="actual_lab">actual</xsl:variable>
		<xsl:variable name="item_type"><xsl:value-of select="@type"/></xsl:variable>
		<xsl:variable name="budget_fee"><xsl:value-of select="$item_cost/cost[@type=$item_type]/budget_fee"/></xsl:variable>
		<xsl:variable name="actual_fee"><xsl:value-of select="$item_cost/cost[@type=$item_type]/actual_fee"/></xsl:variable>
		<xsl:variable name="lab_cost"><xsl:value-of select="label[@xml:lang=$cur_lang]/text()"/></xsl:variable>
		<tr>
			<td class="wzb-form-label">
				<xsl:value-of select="$lab_cost"/><xsl:text>：</xsl:text>
			</td>
			<td align="left" style="padding:10px 0 10px 10px;">
			   <input class="wzb-inputText" style="width:150px;" type="text" name="{concat($budget_lab,@type)}" maxlength="12" value="{$budget_fee}" id="{$lab_cost}" onKeyUp="calcu_budget(frmXml)"/>
			</td>
			<td align="left">
			   <input class="wzb-inputText" style="width:150px;" type="text" name="{concat($actual_lab,@type)}" maxlength="12" value="{$actual_fee}" id="{$lab_cost}" onKeyUp="calcu_actual(frmXml)"/>
			</td>
			<input type="hidden" name="change_type{@type}">
				<xsl:attribute name="value">
					<xsl:choose>
						<!-- 1 for update;0 for insert-->
						<xsl:when test="$item_cost/cost/@type=@type">1</xsl:when>
						<xsl:otherwise>0</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
			</input>
			<input name="type_value{position()}" type="hidden">
				<xsl:attribute name="value">
					<xsl:value-of select="@type"/>
				</xsl:attribute>
			</input>
			<input type="hidden" name="updated_timestamp{@type}">
				<xsl:attribute name="value">
					<xsl:choose>
					  <xsl:when test="$item_cost/cost/@type=@type"><xsl:value-of select="$item_cost/cost[@type=$item_type]/last_updated/@timestamp"/></xsl:when>
					</xsl:choose>
				</xsl:attribute>
			</input>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<!--this template can be reused in aeItem's Moudle to draw nav-link of items which can run-->
	<xsl:template match="item" mode="nav">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_session_info"/>
		<xsl:variable name="_count" select="count(preceding-sibling::item)"/>
		<xsl:choose>
			<xsl:when test="@run_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=$_count">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.get_item_run_list({$value})" class="NavLink">
					<xsl:choose>
						<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
					</xsl:choose>
				</a>
				<span class="NavLink">&#160;&gt;&#160;</span>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
				<xsl:value-of select="title"/> 
				</a>
			</xsl:when>
			<xsl:when test="@session_ind = 'true'">
				<span class="NavLink">&#160;&gt;&#160;</span>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=last()">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.session.get_session_list({$value})" class="NavLink">
					<xsl:value-of select="$lab_session_info"/>
				</a>
				<span>&#160;&gt;&#160;</span>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a href="javascript:itm_lst.get_item_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>