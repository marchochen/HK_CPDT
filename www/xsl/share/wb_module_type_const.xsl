<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:variable name="lab_eval">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">調查問卷</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">调查问卷</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Survey</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_aicc_au">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">AICC 課件</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">AICC 课件</xsl:when>
			<xsl:when test="$wb_lang = 'en'">AICC courseware</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_vcr">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">虛擬教室</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">虚拟教室</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Virtual classroom</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_faq">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">解答欄</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">答疑栏</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Q&amp;A</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_cht">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">聊天室</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">聊天室</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Chat room</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_test">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">測驗</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">测验</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Test</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_tst">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">靜態測驗</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">静态测验</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Fixed test</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_dxt">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">動態測驗</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">动态测验</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Dynamic test</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_rdg">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">教材</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">教材</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Learning material</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_for">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">討論區</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">论坛</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Forum</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_ass">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">作業</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">作业</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Assignment</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_vod">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">視頻點播</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">视频点播</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Video on demand</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_ref">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">參考</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">参考</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Reference links</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_glo">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">詞彙表</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">词汇表</xsl:when>
			<xsl:when test="$wb_lang = 'en'">English glossary</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_svy">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">課程評估問卷</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">课程评估问卷</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Course evaluation</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_evn">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">調查問卷</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">调查问卷</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Evaluation</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_netg_cok">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">NETg 課件</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">NETg 课件</xsl:when>
			<xsl:when test="$wb_lang = 'en'">NETg courseware</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_scorm">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">SCORM 課件</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">SCORM 课件</xsl:when>
			<xsl:when test="$wb_lang = 'en'">SCORM  courseware</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_mbl">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">移動課件</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">移动课件</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Mobile Courseware</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<!-- ===============================================================-->
	<xsl:template name="return_module_label">
		<xsl:param name="mod_type"/>
		<xsl:choose>
			<xsl:when test="$mod_type =  'RDG' ">
				<xsl:value-of select="$lab_rdg"/>
			</xsl:when>
			<xsl:when test="$mod_type =  'TST' ">
				<xsl:value-of select="$lab_tst"/>
			</xsl:when>
			<xsl:when test="$mod_type =  'DXT' ">
				<xsl:value-of select="$lab_dxt"/>
			</xsl:when>
			<xsl:when test="$mod_type =  'ASS' ">
				<xsl:value-of select="$lab_ass"/>
			</xsl:when>
			<xsl:when test="$mod_type =  'VCR' ">
				<xsl:value-of select="$lab_vcr"/>
			</xsl:when>
			<xsl:when test="$mod_type =  'FOR' ">
				<xsl:value-of select="$lab_for"/>
			</xsl:when>
			<xsl:when test="$mod_type =  'CHT' ">
				<xsl:value-of select="$lab_cht"/>
			</xsl:when>
			<xsl:when test="$mod_type =  'FAQ' ">
				<xsl:value-of select="$lab_faq"/>
			</xsl:when>
			<xsl:when test="$mod_type =  'VOD' ">
				<xsl:value-of select="$lab_vod"/>
			</xsl:when>
			<xsl:when test="$mod_type =  'AICC_AU' ">
				<xsl:value-of select="$lab_aicc_au"/>
			</xsl:when>
			<xsl:when test="$mod_type =  'GLO' ">
				<xsl:value-of select="$lab_glo"/>
			</xsl:when>
			<xsl:when test="$mod_type =  'REF' ">
				<xsl:value-of select="$lab_ref"/>
			</xsl:when>
			<xsl:when test="$mod_type = 'EVN'">
				<xsl:value-of select="$lab_evn"/>
			</xsl:when>
			<xsl:when test="$mod_type = 'SVY'">
				<xsl:value-of select="$lab_svy"/>
			</xsl:when>
			<xsl:when test="$mod_type = 'NETG_COK'">
				<xsl:value-of select="$lab_netg_cok"/>
			</xsl:when>
			<xsl:when test="$mod_type = 'SCO'">
				<xsl:value-of select="$lab_scorm"/>
			</xsl:when>
			<xsl:when test="$mod_type = 'MBL'">
				<xsl:value-of select="$lab_mbl"/>
			</xsl:when>
			<xsl:otherwise>Unknown Module Type</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
