<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../share/res_label_share.xsl"/>
	<xsl:variable name="lab_select_all">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">--請選擇--</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">--请选择--</xsl:when>
			<xsl:otherwise>--Please select--</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- = For Questions and Resources  ========== -->
	<xsl:template name="draw_res_option_list">
		<option value="QUE~GEN~AICC~ASM~SCORM~NETGCOK">
			<xsl:value-of select="$lab_select_all"/>
		</option>
		<option value="GEN~AICC~SCORM~NETGCOK">
			<xsl:value-of select="$lab_gen"/>
		</option>
<!-- 		<option value="WCT"> -->
<!-- 			<xsl:text>&#160;&#160;</xsl:text> -->
<!-- 			<xsl:value-of select="$lab_wct"/> -->
<!-- 		</option> -->
		<option value="SSC">
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:value-of select="$lab_ssc"/>
		</option>
		<option value="SCORM">
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:value-of select="$lab_sco"/>
		</option>
		<!--<option value="NETGCOK">
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:value-of select="$lab_netg_cok"/>
		</option>
		<option value="NAR">
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:value-of select="$lab_nar"/>
		</option>
		<option value="FIG">
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:value-of select="$lab_fig"/>
		</option>
		<option value="VDO">
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:value-of select="$lab_vdo"/>
		</option>
		<option value="ADO">
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:value-of select="$lab_ado"/>
		</option>-->
		<option value="QUE">
			<xsl:value-of select="$lab_que"/>
		</option>
		<option value="MC">
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:value-of select="$lab_mc"/>
		</option>
		<option value="FB">
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:value-of select="$lab_fb"/>
		</option>
		<option value="MT">
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:value-of select="$lab_mt"/>
		</option>
		<option value="TF">
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:value-of select="$lab_tf"/>
		</option>
		<option value="ES">
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:value-of select="$lab_es"/>
		</option>
		<!--  屏蔽静/动态情景题
		  <option value="FSC">     
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:value-of select="$lab_fixed_sc"/>
		</option>
		<option value="DSC">
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:value-of select="$lab_dna_sc"/>
		</option> -->
		<option value="ASM">
			<xsl:text/>
			<xsl:value-of select="$lab_asm"/>
		</option>
		<option value="FAS">
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:value-of select="$lab_fas"/>
		</option>
		<option value="DAS">
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:value-of select="$lab_das"/>
		</option>
	</xsl:template>
	<!-- = For Questions only ========== -->
	<xsl:template name="draw_que_option_list">
		<option value="">
			<xsl:value-of select="$lab_select_all"/>
		</option>
		<option value="MC">
			<xsl:value-of select="$lab_mc"/>
		</option>
		<option value="FB">
			<xsl:value-of select="$lab_fb"/>
		</option>
		<option value="MT">
			<xsl:value-of select="$lab_mt"/>
		</option>
		<option value="TF">
			<xsl:value-of select="$lab_tf"/>
		</option>
		<option value="ES">
			<xsl:value-of select="$lab_es"/>
		</option>
	   <!-- 	屏蔽静/动态情景题
	    <option value="FSC">
			<xsl:value-of select="$lab_fixed_sc"/>
		</option>
		<option value="DSC">
			<xsl:value-of select="$lab_dna_sc"/>
		</option> -->
	</xsl:template>
	<xsl:template name="draw_que_option_list_content">
		<option value="">
			<xsl:value-of select="$lab_select_all"/>
		</option>
		<option value="MC">
			<xsl:value-of select="$lab_mc"/>
		</option>
		<option value="ES">
			<xsl:value-of select="$lab_es"/>
		</option>
	</xsl:template>
	<!-- For JS Index Array , Questions and Resources -->
	<xsl:template name="draw_js_res_que_array"><!-- 屏蔽静/动态情景题  ,'FSC','DSC'-->
		var TypeList = new Array('QUE~GEN~AICC~ASM~SCORM~NETGCOK','GEN~AICC~SCORM~NETGCOK','SSC','SCORM', 'QUE','MC','FB','MT','TF','ES','ASM','FAS','DAS')
	</xsl:template>
	<!-- For JS Index Array , Questions Only -->
	<xsl:template name="draw_js_que_array">
	var TypeList = new Array('QUE','MC','FB','MT','TF','ES','FSC','DSC')
	</xsl:template>
	<xsl:template name="getResText">
		<xsl:param name="resType"/>
		<xsl:choose>
			<xsl:when test="$resType = 'WCT'">
				<xsl:value-of select="$lab_wct"/>
			</xsl:when>
			<xsl:when test="$resType = 'SSC'">
				<xsl:value-of select="$lab_ssc"/>
			</xsl:when>
			<xsl:when test="$resType = 'SCORM'">
				<xsl:value-of select="$lab_sco"/>
			</xsl:when>
			<xsl:when test="$resType = 'RES_SCO'">
				<xsl:value-of select="$lab_sco"/>
			</xsl:when>
			<xsl:when test="$resType = 'MC'">
				<xsl:value-of select="$lab_mc"/>
			</xsl:when>
			<xsl:when test="$resType = 'FB'">
				<xsl:value-of select="$lab_fb"/>
			</xsl:when>
			<xsl:when test="$resType = 'MT'">
				<xsl:value-of select="$lab_mt"/>
			</xsl:when>
			<xsl:when test="$resType = 'TF'">
				<xsl:value-of select="$lab_tf"/>
			</xsl:when>
			<xsl:when test="$resType = 'ES'">
				<xsl:value-of select="$lab_es"/>
			</xsl:when>
			<!--屏蔽静/动态情景题
			 <xsl:when test="$resType = 'FSC'">
				<xsl:value-of select="$lab_fixed_sc"/>
			</xsl:when>
			<xsl:when test="$resType = 'DSC'">
				<xsl:value-of select="$lab_dna_sc"/>
			</xsl:when> -->
			<xsl:when test="$resType = 'ASM'">
				<xsl:value-of select="$lab_asm"/>
			</xsl:when>
			<xsl:when test="$resType = 'FAS' or $resType = 'TST'">
				<xsl:value-of select="$lab_fas"/>
			</xsl:when>
			<xsl:when test="$resType = 'DAS'">
				<xsl:value-of select="$lab_das"/>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
