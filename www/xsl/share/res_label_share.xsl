<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- First Column -->
	<xsl:variable name="lab_gen">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">內容</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">内容</xsl:when>
			<xsl:otherwise>Content</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_wct">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">教材</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">教材</xsl:when>
			<xsl:otherwise>Learning material</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_nar">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">文字解說</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">文字解说</xsl:when>
			<xsl:otherwise>Document</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_fig">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">圖像</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">图像</xsl:when>
			<xsl:otherwise>Image</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_vdo">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">錄像</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">视频</xsl:when>
			<xsl:otherwise>Video</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_ado">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">錄音</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">音频</xsl:when>
			<xsl:otherwise>Audio</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- Second Column -->
	<xsl:variable name="lab_que">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">題目</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">题目</xsl:when>
			<xsl:otherwise>Question</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_mc">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">選擇題</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">选择题</xsl:when>
			<xsl:otherwise>Multiple choice</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_mt">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">配對題</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">配对题</xsl:when>
			<xsl:otherwise>Matching</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_fb">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">填充題</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">填空题</xsl:when>
			<xsl:otherwise>Fill in the blank</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_tf">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">是非題</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">判断题</xsl:when>
			<xsl:otherwise>True or false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>	
	<xsl:variable name="lab_es">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">文章</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">文章</xsl:when>
			<xsl:otherwise>Essay</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>	
	<xsl:variable name="lab_fixed_sc">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">靜態情景題</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">静态情景题</xsl:when>
			<xsl:otherwise>Fixed scenario</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>	
	<xsl:variable name="lab_dna_sc">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">動態情景題</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">动态情景题</xsl:when>
			<xsl:otherwise>Dynamic scenario</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>	
	<xsl:variable name="lab_asm">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">測驗</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">测验</xsl:when>
			<xsl:otherwise>Test</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>	
	<xsl:variable name="lab_fas">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">靜態測驗</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">静态测验</xsl:when>
			<xsl:otherwise> Fixed test</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>	
	<xsl:variable name="lab_das">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">動態測驗</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">动态测验</xsl:when>
			<xsl:otherwise> Dynamic test</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>	
	<xsl:variable name="lab_fb_evn">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">問答題</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">问答题</xsl:when>
			<xsl:otherwise>Short question</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>	
	<!-- Third Column -->
	<xsl:variable name="lab_aicc">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">課件</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">课件</xsl:when>
			<xsl:otherwise>Courseware</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_ssc">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">AICC 課件</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">AICC 课件</xsl:when>
			<xsl:otherwise>AICC courseware</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_sco">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">SCORM 課件</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">SCORM 课件</xsl:when>
			<xsl:otherwise>SCORM courseware</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_netg_cok">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">NETg 課件</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">NETg 课件</xsl:when>
			<xsl:otherwise>NETg courseware</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>	
	<xsl:variable name="lab_operate_add">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">添加</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">添加</xsl:when>
			<xsl:otherwise>Add</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_operate_upd">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">修改</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">修改</xsl:when>
			<xsl:otherwise>Add</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
</xsl:stylesheet>