<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- ============================================================= -->
	<xsl:variable name="ent_id" select="/supplier_module/meta/cur_usr/@ent_id"/>
	<xsl:variable name="cur_page" select="/supplier_module/pagination/@cur_page"/>
	<xsl:variable name="total" select="/supplier_module/pagination/@total_rec"/>
	<xsl:variable name="page_size" select="/supplier_module/pagination/@page_size"/>
	<xsl:variable name="order_by" select="/supplier_module/pagination/@orderby"/>
	<xsl:variable name="cur_order" select="/supplier_module/pagination/@sortorder"/>
	<xsl:variable name="spl_id" select="/supplier_module/suppliers/@id"/>
	<xsl:variable name="spl_ent_id" select="/supplier_module/suppliers/supplier_comment/scm_ent_id"/>
	<xsl:variable name="spl_design_score" select="/supplier_module/suppliers/supplier_comment/scm_design_score"/>
	<xsl:variable name="spl_teaching_score" select="/supplier_module/suppliers/supplier_comment/scm_teaching_score"/>
	<xsl:variable name="spl_price_score" select="/supplier_module/suppliers/supplier_comment/scm_price_score"/>
	<xsl:variable name="spl_management_score" select="/supplier_module/suppliers/supplier_comment/scm_management_score"/>
	<xsl:variable name="spl_score_desc" select="/supplier_module/suppliers/supplier_comment/scm_score"/>
	<xsl:variable name="spl_comment" select="/supplier_module/suppliers/supplier_comment/scm_comment"/>
	<xsl:variable name="sucess" select="/supplier_module/suppliers/@sucess"/>
	<xsl:variable name="spl_name" select="/supplier_module/suppliers/nav/supplier/@name"/>
	<!-- =========================== Label =========================== 
	981	项目设计
	982	现场管理
	983	师资设备
	984	价格
	985	评语
	986	评论人
	989  添加评论
	990 提交评论
	923 我要评分(5分满分)-->
	
	<xsl:variable name="lab_scm_design_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '981')"/> 	
	<xsl:variable name="lab_scm_management_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '982')"/> 	
	<xsl:variable name="lab_scm_teaching_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '983')"/> 	
	<xsl:variable name="lab_scm_price_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '984')"/> 	
	<xsl:variable name="lab_scm_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '928')"/> 	
	<xsl:variable name="lab_scm_comment" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '985')"/> 	
	<xsl:variable name="lab_add" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '989')"/>  
	<xsl:variable name="lab_scm_submit" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '990')"/> 	
	<xsl:variable name="lab_scm_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '923')"/> 
	<xsl:variable name="lab_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '991')"/>  	
	<xsl:variable name="lab_supplier_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '988')"/> 
	<!-- =============================================================== -->
	<xsl:template match="/supplier_module">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_supplier.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				wbSupplier = new wbSupplier; 
				function checkscore(frm){
					var re =/^[0-5]+(.[0-9]{1})?$/;
					var scm_design_score = frm.scm_design_score.value;
					if(scm_design_score !='' ){
						if(!scm_design_score.match(re)){
						alert(wb_supp_score);
						frm.scm_design_score.focus();
						return;
						}else{
							if(scm_design_score>5){
								alert(wb_supp_score);
								frm.scm_design_score.focus();
								return;
							}
						}
					}else{
						scm_design_score = 0;
					}
					
					var scm_management_score = frm.scm_management_score.value;
					if(scm_management_score !=''){
						if(!scm_management_score.match(re)){
							alert(wb_supp_score);
							frm.scm_management_score.focus();
							return;
						}else{
							if(scm_management_score>5){
								alert(wb_supp_score);
								frm.scm_management_score.focus();
								return;
							}
						}
					}else{
						scm_management_score = 0;
					}
						
					var scm_teaching_score = frm.scm_teaching_score.value;
					if(scm_teaching_score !=''){
						if(!scm_teaching_score.match(re)){
							alert(wb_supp_score);
							frm.scm_teaching_score.focus();
							return;
						}else{
							if(scm_teaching_score>5){
								alert(wb_supp_score);
								frm.scm_teaching_score.focus();
								return;
							}
						}
					}else{
						scm_teaching_score = 0;
					}
						
					var scm_price_score = frm.scm_price_score.value;
					if(scm_price_score !=''){
						if(!scm_price_score.match(re)){
							alert(wb_supp_score);
							frm.scm_price_score.focus();
							return;
						}else{
							if(scm_price_score>5){
								alert(wb_supp_score);
								frm.scm_price_score.focus();
								return;
							}
						}
					}else{
						scm_price_score = 0;
					}
					var scm_score =  parseFloat(scm_design_score)+ parseFloat(scm_management_score)+ parseFloat(scm_teaching_score)+ parseFloat(scm_price_score);
					if(scm_score >0){
						frm.scm_score.value = parseFloat((scm_score/4).toFixed(1));
						frm.scm_score_desc.value = frm.scm_score.value;
					}	
				}
 			]]></script> 
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="content"/>
		</body>
	</xsl:template>
	
	<!-- =============================================================== -->
	<xsl:template name="content">
		<form  name="frmXml">
			<input name="module" type="hidden"/>
			<input type="hidden" name="spl_id" value="{$spl_id}"/>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<input name="cmd" type="hidden"/>	
			<input type="hidden" name="scm_ent_id" value="{$ent_id}"/>
			<input type="hidden" name="scm_spl_id" value="{$spl_id}"/>
			<input type="hidden" name="up_scm_ent_id" value="{$spl_ent_id}"/>
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_SUPPLIER_MAIN</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text" select="$lab_scm_title"/>
			</xsl:call-template>
			
			<xsl:call-template name="wb_ui_nav_link">
				<xsl:with-param name="text">
					<a href="javascript:wb_utils_nav_go('FTN_AMD_SUPPLIER_MAIN');" class="NavLink">
							<xsl:value-of select="$lab_title"/>
					</a>
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<a href="javascript:wbSupplier.get_supplier_view({$spl_id});" class="NavLink">
							<xsl:value-of select="$spl_name"/>
					</a>
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<a href="javascript:wbSupplier.comm.get_supplier_comment_view({$spl_id});" class="NavLink">
							<xsl:value-of select="$lab_supplier_desc"/>
					</a>
					<span class="NavLink">
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:value-of select="$lab_add"/>
					</span>
				</xsl:with-param>
			</xsl:call-template>			
				<table border="0" id="star_table">
						<tr style="padding-bottom:5px;">
						 
							<td align="right" width="30%">
								<xsl:value-of select="$lab_scm_design_score" />
								<xsl:text>：</xsl:text>
							</td>
							<td align="left" width="60%">
								<span id="scm_design_score">
									<li class="xing2" data="1" title="1"></li>
									<li class="xing2" data="2" title="2"></li>
									<li class="xing2" data="3" title="3"></li>
									<li class="xing2" data="4" title="4"></li>
									<li class="xing2" data="5" title="5"></li>
								</span>
								<input type="hidden" name="scm_design_score" value="{$spl_design_score}"/>
							</td>
							<td width="10%"></td>
						</tr>
						<tr style="padding:5px 0;">
				 
							<td align="right">
								<xsl:value-of select="$lab_scm_management_score" />
								<xsl:text>：</xsl:text>
							</td>
							<td align="left">
								<span id="scm_management_score">
									<li class="xing2" data="1" title="1"></li>
									<li class="xing2" data="2" title="2"></li>
									<li class="xing2" data="3" title="3"></li>
									<li class="xing2" data="4" title="4"></li>
									<li class="xing2" data="5" title="5"></li>
								</span>
								<input type="hidden"  name="scm_management_score"  value="{$spl_management_score}"/>
							</td>
							<td width="10%"></td>
						</tr>
						<tr style="padding:5px 0;">
				 
							<td align="right">
								<xsl:value-of select="$lab_scm_teaching_score" />
								<xsl:text>：</xsl:text>
							</td>
							<td align="left">
								<span id="scm_teaching_score">
									<li class="xing2" data="1" title="1"></li>
									<li class="xing2" data="2" title="2"></li>
									<li class="xing2" data="3" title="3"></li>
									<li class="xing2" data="4" title="4"></li>
									<li class="xing2" data="5" title="5"></li>
								</span>
								<input type="hidden" name="scm_teaching_score" value="{$spl_teaching_score}"/>
							</td>
							<td></td>
						</tr>
						<tr  style="padding:5px 0;">
					 
							<td align="right">
								<xsl:value-of select="$lab_scm_price_score" />
								<xsl:text>：</xsl:text>
							</td>
							<td align="left">
								<span id="scm_price_score" >
									<li class="xing2" data="1" title="1"></li>
									<li class="xing2" data="2" title="2"></li>
									<li class="xing2" data="3" title="3"></li>
									<li class="xing2" data="4" title="4"></li>
									<li class="xing2" data="5" title="5"></li>
								</span>
								<input type="hidden" name="scm_price_score" value="{$spl_price_score}"/>
							</td>
							<td width="10%"></td>
						</tr>
						<tr  style="padding:5px 0;display:none;" >
					 
							<td align="right">
								<xsl:value-of select="$lab_scm_score" />
								<xsl:text>：</xsl:text>
							</td>
							<td align="left"  title="{$spl_score_desc}">
								<span id="scm_score_desc" >
									<li class="xing2" data="1" title="1"></li>
									<li class="xing2" data="2" title="2"></li>
									<li class="xing2" data="3" title="3"></li>
									<li class="xing2" data="4" title="4"></li>
									<li class="xing2" data="5" title="5"></li>
								</span>
								<input type="hidden" name="scm_score_desc" value="{$spl_score_desc}"/>
							</td>
							<td width="10%"></td>
							<input type="hidden" name="scm_score" />
						</tr>						
						<tr >
							<td align="right" style="padding-top:28px;" valign="top">
								<xsl:value-of select="$lab_scm_comment" />
								<xsl:text>：</xsl:text>
							</td>
							<td style="padding-top:28px;" colspan="3">
								<textarea cols="20" rows="4" style="width:422px;" maxlength="200" name="scm_comment" class="wzb-inputTextArea" onclick="checkscore(document.frmXml)" >
									<xsl:if test="$spl_comment !=''">
										<xsl:value-of select="$spl_comment"/>
									</xsl:if>
								</textarea>
							</td>
						</tr>
				</table>
				<script>
					//评论星星
					$(function(){
						$("#star_table li").mouseenter(function(){
							$(this).addClass("xing1-mouseenter").nextAll().removeClass("xing1-mouseenter").addClass("xing2-mouseout");
							$(this).prevAll().addClass("xing1-mouseenter");
							$(this).parent().find("li").mouseout(function () {
								$(this).parent().find("li").removeClass("xing1-mouseenter xing2-mouseout");
							})
						}).mouseout(function(){
							$(this).parent().find("li").removeClass("xing2-mouseout xing1-mouseenter");
							console.log($(this).prevAll().attr("class"));
						}).click(function(){
							$(this).parent().find("li").unbind("mouseout");
							$(this).addClass('xing1').prevAll().addClass("xing1");
							$(this).nextAll().removeClass("xing1");
							$(this).parent().next().val($(this).attr("data"));
							
							var scm_score_desc = parseFloat($("#scm_design_score").val()) + 
											parseFloat($("#scm_management_score").val()) + 
											parseFloat($("#scm_teaching_score").val()) + 
											parseFloat($("#scm_price_score").val());
							if(scm_score_desc > 0){ 
								$("#scm_score_desc").val(parseFloat((scm_score_desc/4).toFixed(1)));
							}
							
						});
						
						var starScore = {
							'scm_design_score':<xsl:value-of select="$spl_design_score" />,
							'scm_management_score':<xsl:value-of select="$spl_management_score" />,
							'scm_teaching_score':<xsl:value-of select="$spl_teaching_score" />,
							'scm_price_score':<xsl:value-of select="$spl_price_score" />,
							'scm_score_desc':<xsl:value-of select="$spl_score_desc" />
						};
						<![CDATA[
						for (var key in starScore){
					        if(starScore[key] > 0){
					       		var objli = $("#"+key).children();
					       		for(var i in objli){
					       			if(i < parseInt(starScore[key])){
						       			$(objli[i]).addClass('xing1');
					       			}
					       		}
					    	}
					    }
						]]>
					});
				</script>
				
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_scm_submit" />
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:checkscore(document.frmXml);wbSupplier.comm.supplier_comment_submit(document.frmXml);</xsl:with-param>
					</xsl:call-template>
				</div>
		</form>
	</xsl:template>
	<!-- =============================================================== -->
	
</xsl:stylesheet>