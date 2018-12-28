<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:variable name="wb_doc_table_width">600</xsl:variable>
	<xsl:variable name="export_mode" select="/quiz/meta/export_mode"/>
	<!-- =============================================================== -->
	<xsl:template name="answersheet">
		<xsl:param name="lab_test_no"/>
		<xsl:param name="lab_question"/>
		<xsl:param name="lab_answer"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_total_score"/>
		<xsl:param name="lab_true"/>
		<xsl:param name="lab_false"/>
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<style type="text/css">
					<xsl:comment>
				.Title{
					font-size: 20px; bold;
				}
				.bg{
					background-color: #CCCCCC;
				}
				br
					{mso-data-placement:same-cell;}
				td
					{
					padding-top:1px;
					padding-right:1px;
					padding-left:1px;
					mso-ignore:padding;
					white-space:normal;
					vertical-align:top;
					text-align:left;
					}
			</xsl:comment>
				</style>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
				<xsl:call-template name="answersheet_body">
					<xsl:with-param name="lab_test_no" select="$lab_test_no"/>
					<xsl:with-param name="lab_question" select="$lab_question"/>
					<xsl:with-param name="lab_score" select="$lab_score"/>
					<xsl:with-param name="lab_total_score" select="$lab_total_score"/>
					<xsl:with-param name="lab_answer" select="$lab_answer"/>
					<xsl:with-param name="lab_true" select="$lab_true"/>
					<xsl:with-param name="lab_false" select="$lab_false"/>
				</xsl:call-template>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="answersheet_body">
		<xsl:param name="lab_test_no"/>
		<xsl:param name="lab_question"/>
		<xsl:param name="lab_answer"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_total_score"/>
		<xsl:param name="lab_true"/>
		<xsl:param name="lab_false"/>
		<xsl:variable name="mod_type" select="header/@subtype"/>
		<table cellpadding="3" cellspacing="0" border="1">
			<tr>
				<td colspan="3">
					<span class="Title">
						<xsl:value-of select="header/title"/>
					</span>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="right">
					<xsl:choose>
						<xsl:when test="$mod_type = 'EVN' or $mod_type = 'SVY'">
							<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '693')"/>
						</xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_test_no"/></xsl:otherwise>
					</xsl:choose>:
				</td>
				<td>
					<xsl:value-of select="meta/sn"/>
				</td>
			</tr>
			<xsl:if test="$mod_type != 'EVN' and $mod_type != 'SVY'">
				<tr>
					<td colspan="2" align="right">
						<xsl:value-of select="$lab_total_score"/>:
					</td>
					<td>
						<xsl:value-of select="header/@max_score"/>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td>
					<xsl:value-of select="$lab_question"/>
				</td>
				<td>
					<xsl:value-of select="$lab_answer"/>
				</td>
				<td>
					<xsl:value-of select="$lab_score"/>
				</td>
			</tr>
			<xsl:for-each select="question">
				<xsl:choose>
					<xsl:when test="header/@type = 'MC'">
						<xsl:call-template name="mc_ans">
							<xsl:with-param name="subtype">MC</xsl:with-param>
							<xsl:with-param name="lab_true" select="$lab_true"/>
							<xsl:with-param name="lab_false" select="$lab_false"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="header/@type = 'TF'">
						<xsl:call-template name="mc_ans">
							<xsl:with-param name="subtype">TF</xsl:with-param>
							<xsl:with-param name="lab_true" select="$lab_true"/>
							<xsl:with-param name="lab_false" select="$lab_false"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="header/@type = 'FB'">
						<xsl:call-template name="fb_ans"/>
					</xsl:when>
					<xsl:when test="header/@type = 'MT'">
						<xsl:call-template name="mt_ans"/>
					</xsl:when>
					<xsl:when test="header/@type = 'ES'">
						<xsl:call-template name="es_ans"/>
					</xsl:when>
					<xsl:when test="header/@type = 'FSC' or header/@type = 'DSC'">
						<xsl:call-template name="scenario_ans"/>
					</xsl:when>
				</xsl:choose>
			</xsl:for-each>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="mc_ans">
		<xsl:param name="question_num"/>
		<xsl:param name="lab_true"/>
		<xsl:param name="lab_false"/>
		<xsl:param name="subtype">MC</xsl:param>
		<tr>
			<td>
				<xsl:if test="$question_num != ''">
					<xsl:value-of select="$question_num"/>.</xsl:if>
				<xsl:value-of select="position()"/>
			</td>
			<td>
				<xsl:choose>
					<xsl:when test="$subtype = 'MC'">
						<xsl:variable name="this_outcome" select="outcome"/>
						<xsl:variable name="ans">
						   <xsl:for-each select="body/interaction/option">
						   		<xsl:variable name="opt_id" select="@id"/>
						   		<xsl:if test="$this_outcome/feedback[@condition = $opt_id]/@score >= 1">
						   			<xsl:number value="position()" format="A"/>
						   			<xsl:text>, </xsl:text>
						   		</xsl:if>
						   	</xsl:for-each>
						</xsl:variable>
						<xsl:value-of select="substring($ans, 1, string-length($ans)-2)"/>
					</xsl:when>
					<xsl:when test="$subtype = 'TF'">
						<xsl:variable name="ans_pos" select="outcome/feedback[@score >= '1']/@condition"/>
						<xsl:variable name="answer" select="body/interaction/option[@id = $ans_pos]"/>
						<xsl:choose>
							<xsl:when test="$answer = 'True'">
								<xsl:value-of select="$lab_true"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_false"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
				</xsl:choose>
			</td>
			<td>
				<xsl:variable name="score">
					<xsl:for-each select="outcome/feedback[@score >= '1']">
						<xsl:value-of select="@score"/>
						<xsl:text>, </xsl:text>
					</xsl:for-each>
				</xsl:variable>
				<xsl:value-of select="substring($score, 1, string-length($score)-2)"/>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="fb_ans">
		<xsl:variable name="question_num" select="position()"/>
		<xsl:variable name="interaction_cnt" select="count(body/interaction)"/>
		<xsl:for-each select="outcome">
			<tr>
				<td>
					<xsl:value-of select="$question_num"/>.<xsl:if test="$interaction_cnt > 1">
						<xsl:value-of select="@order"/>
					</xsl:if>
				</td>
				<td>
					<xsl:for-each select="feedback">
						<xsl:if test="position() != 1">, </xsl:if>
						<xsl:value-of select="@condition"/>
					</xsl:for-each>
				</td>
				<td>
					<xsl:value-of select="@score"/>
				</td>
			</tr>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="mt_ans">
		<xsl:variable name="question_num" select="position()"/>
		<xsl:for-each select="outcome/feedback">
			<xsl:sort select="@condition"/>
			<xsl:variable name="target" select="../@order"/>
			<xsl:variable name="source" select="@condition"/>
			<tr>
				<td>
					<xsl:value-of select="$question_num"/>.<xsl:value-of select="position()"/>
				</td>
				<td>
					<xsl:value-of select="../../body/source/item[@id = $source]/text"/>
					<xsl:text>----------</xsl:text>
					<xsl:value-of select="../../body/interaction[@order = $target]/text"/>
				</td>
				<td>
					<xsl:value-of select="@score"/>
				</td>
			</tr>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="es_ans">
		<tr>
			<td>
				<xsl:value-of select="position()"/>
			</td>
			<td>
				<xsl:call-template name="unescape_html_linefeed">
					<xsl:with-param name="my_right_value">
						<xsl:value-of select="explanation/rationale"/>
					</xsl:with-param>
				</xsl:call-template>
			</td>
			<td>
				<xsl:value-of select="body/interaction/@score"/>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="scenario_ans">
		<xsl:variable name="question_num" select="position()"/>
		<xsl:for-each select="question_list/question">
			<xsl:call-template name="mc_ans">
				<xsl:with-param name="question_num" select="$question_num"/>
				<xsl:with-param name="subtype">MC</xsl:with-param>
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
