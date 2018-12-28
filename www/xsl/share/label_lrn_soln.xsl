<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- get attendance status title -->
	<xsl:template name="get_ats_title">
		<xsl:param name="ats_id"/>
		<xsl:variable name="label_ats">
			<xsl:choose>
				<xsl:when test="$ats_id = '1'">lab_completed</xsl:when>
				<xsl:when test="$ats_id = '2'">lab_enroll</xsl:when>
				<xsl:when test="$ats_id = '3'">lab_incompleted</xsl:when>
				<xsl:when test="$ats_id = '4'">lab_withdrawn</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$ats_id"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, $label_ats)"/>
	</xsl:template>
	<!-- get item type title -->
	<xsl:template name="get_ity_title">
		<xsl:param name="dummy_type"/>
		<xsl:param name="itm_type"/>
		<xsl:choose>
			<xsl:when test="$dummy_type =''"><xsl:value-of select="java:com.cw.wizbank.ae.aeItemDummyType.getItemLabelByDummyType($wb_lang_encoding, $itm_type)"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="java:com.cw.wizbank.ae.aeItemDummyType.getItemLabelByDummyType($wb_lang_encoding, $dummy_type)"/></xsl:otherwise>
		</xsl:choose>
		

	</xsl:template>
	
	<xsl:template name="get_lab">
		<xsl:param name="lab_title"/>
		<xsl:variable name="lab_tc_mgt_title" />
		
		<xsl:value-of   select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, $lab_title) "/>	
	</xsl:template>
	<xsl:template name="get_class_lab">
		<xsl:param name="training_type">
			<xsl:value-of select="/applyeasy/cur_training_type"/>
		</xsl:param>
		<xsl:choose>
			<xsl:when test="$training_type = 'COS'">
				<xsl:call-template name="get_lab">
					<xsl:with-param name="lab_title">COS_CLASS</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$training_type = 'EXAM'">
				<xsl:call-template name="get_lab">
					<xsl:with-param name="lab_title">EXAM_CLASS</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="get_course_lab">
		<xsl:param name="training_type">
			<xsl:value-of select="/applyeasy/cur_training_type"/>
		</xsl:param>
		<xsl:choose>
			<xsl:when test="$training_type = 'COS'">
				<xsl:call-template name="get_lab">
					<xsl:with-param name="lab_title">COS</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$training_type = 'EXAM'">
				<xsl:call-template name="get_lab">
					<xsl:with-param name="lab_title">EXAM</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>