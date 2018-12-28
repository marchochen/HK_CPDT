<?xml version='1.0' encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	
	<!-- 设置基本信息  --> 	
	<xsl:variable name="label_core_training_management_298" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_298')"/>
	
	<!-- 选择类型及报名工作历程  --> 	
	<xsl:variable name="label_core_training_management_299" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_299')"/>
	
	<!-- 选择类型及报名工作历程  --> 	
	<xsl:variable name="label_core_training_management_300" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_300')"/>
	
	<xsl:template name="wb_utils_process">
		<xsl:param name="itm_type">SELFSTUDY</xsl:param>
		<xsl:param name="cur_tabs">1</xsl:param>
		<script language="JavaScript" type="text/javascript"><![CDATA[
			$(document).ready(function(){
				$(".buzhou .wbz-buzhou1").each(function(){
					if($(this).attr('data') == ']]><xsl:value-of select="$cur_tabs"/><![CDATA['){
						$(this).addClass('cur');
					} else if($(this).attr('data') < ]]><xsl:value-of select="$cur_tabs"/><![CDATA[){
						$(this).addClass('over');
					}
				});
			});
		]]></script>
		<xsl:choose>
			<xsl:when test="$itm_type = 'CLASSROOM'">
				<div class="buzhou" style="width:1008px">
		             <div class="wbz-buzhou1" data="1">01 <span><xsl:value-of select="$label_core_training_management_299"/></span></div>
		             <div class="wbz-fudian"><img src="../../static/images/wzb-dian.png" alt=""/></div>
		             
		             <div class="wbz-buzhou1" data="2">
		                 02
		                 <span><xsl:value-of select="$label_core_training_management_300"/></span>
		             </div>
		             <div class="wbz-fudian">
		                 <img src="../../static/images/wzb-dian.png" alt=""/>
		             </div>
		             
		             <div class="wbz-buzhou1" data="3">
		                 03
		                 <span><xsl:value-of select="$label_core_training_management_298"/></span>
		             </div>
		             <div class="wbz-fudian">
		                 <img src="../../static/images/wzb-dian.png" alt=""/>
		             </div>
		             <div class="wbz-buzhou1">OK</div>
		         </div>
				<div class="wbz-line"></div>
			</xsl:when>
			<xsl:when test="$itm_type = 'SELFSTUDY' or $itm_type = 'INTEGRATED'">
				<div class="buzhou" style="width:1008px">
		             <div class="wbz-buzhou1" data="1">
		                 01
		                 <span><xsl:value-of select="$label_core_training_management_299"/></span>
		                 
		             </div>
		             <div class="wbz-fudian">
		                 <img src="../../static/images/wzb-dian.png" alt=""/>
		             </div>
		             <div  class="wbz-buzhou1" data="2">
		                 02
		                 <span><xsl:value-of select="$label_core_training_management_298"/></span>
		             </div>
		             <div class="wbz-fudian">
		                 <img src="../../static/images/wzb-dian.png" alt=""/>
		             </div>
		             <div class="wbz-buzhou1">OK</div>
		         </div>
				<div class="wbz-line"></div>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet> 