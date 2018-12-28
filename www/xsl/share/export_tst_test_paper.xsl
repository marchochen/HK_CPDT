<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:variable name="wb_doc_table_width">600</xsl:variable>
	<xsl:variable name="export_mode" select="/quiz/meta/export_mode"/>
	<!-- =============================================================== -->
	<xsl:template name="test_paper">
		<xsl:param name="lab_test_no"/>
		<xsl:param name="lab_number_of_question"/>
		<xsl:param name="lab_pass_percent"/>
		<xsl:param name="lab_duration"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_total_score"/>
		<xsl:param name="lab_answer"/>
		<xsl:param name="lab_min"/>
		<xsl:param name="lab_instr"/>
		<xsl:param name="lab_question_id"/>
		<xsl:param name="lab_question_title"/>
		<xsl:param name="lab_objective"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_explanation"/>
		<xsl:param name="lab_true"/>
		<xsl:param name="lab_false"/>
		<xsl:param name="lab_unlimited"/>
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<style type="text/css">
					<xsl:comment>
				.Title{
					font-size: 20px; 
				}
				.bg{
					background-color: #CCCCCC;
				}
				.Answer{
					background:silver;
				}
				.info{
					font: Italic , Arial;
					font-size: 10pt; 
				}
			</xsl:comment>
				</style>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
				<xsl:call-template name="test_paper_body">
					<xsl:with-param name="lab_test_no" select="$lab_test_no"/>
					<xsl:with-param name="lab_number_of_question" select="$lab_number_of_question"/>
					<xsl:with-param name="lab_pass_percent" select="$lab_pass_percent"/>
					<xsl:with-param name="lab_duration" select="$lab_duration"/>
					<xsl:with-param name="lab_score" select="$lab_score"/>
					<xsl:with-param name="lab_total_score" select="$lab_total_score"/>
					<xsl:with-param name="lab_answer" select="$lab_answer"/>
					<xsl:with-param name="lab_min" select="$lab_min"/>
					<xsl:with-param name="lab_instr" select="$lab_instr"/>
					<xsl:with-param name="lab_question_id" select="$lab_question_id"/>
					<xsl:with-param name="lab_question_title" select="$lab_question_title"/>
					<xsl:with-param name="lab_objective" select="$lab_objective"/>
					<xsl:with-param name="lab_desc" select="$lab_desc"/>
					<xsl:with-param name="lab_explanation" select="$lab_explanation"/>
					<xsl:with-param name="lab_true" select="$lab_true"/>
					<xsl:with-param name="lab_false" select="$lab_false"/>
					<xsl:with-param name="lab_unlimited" select="$lab_unlimited"/>
				</xsl:call-template>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="test_paper_body">
		<xsl:param name="lab_test_no"/>
		<xsl:param name="lab_number_of_question"/>
		<xsl:param name="lab_pass_percent"/>
		<xsl:param name="lab_duration"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_total_score"/>
		<xsl:param name="lab_answer"/>
		<xsl:param name="lab_min"/>
		<xsl:param name="lab_instr"/>
		<xsl:param name="lab_question_id"/>
		<xsl:param name="lab_question_title"/>
		<xsl:param name="lab_objective"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_true"/>
		<xsl:param name="lab_false"/>
		<xsl:param name="lab_explanation"/>
		<xsl:param name="lab_unlimited"/>
		<xsl:variable name="mod_type" select="header/@subtype"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_doc_table_width}">
			<tr>
				<td align="left" colspan="2">
					<span class="Title">
						<xsl:value-of select="header/title"/>
					</span>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:choose>
						<xsl:when test="$mod_type = 'EVN' or $mod_type = 'SVY'">
							<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '693')"/>
						</xsl:when>
						<xsl:otherwise><xsl:value-of select="header/title"/></xsl:otherwise>
					</xsl:choose>:
				</td>
				<td>
					<xsl:value-of select="meta/sn"/>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:value-of select="$lab_number_of_question"/>:</td>
				<td>
					<xsl:value-of select="count(//question[header/@type!='FSC' and header/@type!='DSC'])"/>
				</td>
			</tr>
			<xsl:if test="$mod_type != 'EVN' and $mod_type != 'SVY'">
				<tr>
					<td>
						<xsl:value-of select="$lab_total_score"/>:</td>
					<td>
						<xsl:value-of select="header/@max_score"/>
					</td>
				</tr>
				<xsl:if test="not(meta/mod_source_type)">
					<tr>
						<td>
							<xsl:value-of select="$lab_pass_percent"/>:</td>
						<td>
							<xsl:value-of select="header/@pass_score"/>
						</td>
					</tr>
					<tr>
						<td>
							<xsl:value-of select="$lab_duration"/>:
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="header/@time_limit > -1">
									<xsl:value-of select="header/@time_limit"/>
									<xsl:text/>
									<xsl:value-of select="$lab_min"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_unlimited"/>
								</xsl:otherwise>
							</xsl:choose>
							
						</td>
					</tr>
				</xsl:if>
			</xsl:if>
			<tr>
				<td colspan="2" height="8">
					<xsl:text>&#160;</xsl:text>
				</td>
			</tr>
			<xsl:if test="$export_mode = '2'">
				<tr bgcolor="Silver">
					<td colspan="2">
						<xsl:value-of select="$lab_instr"/>
					</td>
				</tr>
			</xsl:if>
		</table>
		<br/>
		<xsl:for-each select="question">
			<xsl:choose>
				<xsl:when test="header/@type = 'MC'">
					<xsl:call-template name="mc_question">
						<xsl:with-param name="lab_score" select="$lab_score"/>
						<xsl:with-param name="lab_answer" select="$lab_answer"/>
						<xsl:with-param name="lab_question_id" select="$lab_question_id"/>
						<xsl:with-param name="lab_question_title" select="$lab_question_title"/>
						<xsl:with-param name="lab_objective" select="$lab_objective"/>
						<xsl:with-param name="lab_desc" select="$lab_desc"/>
						<xsl:with-param name="lab_explanation" select="$lab_explanation"/>
						<xsl:with-param name="lab_true" select="$lab_true"/>
						<xsl:with-param name="lab_false" select="$lab_false"/>
						<xsl:with-param name="subtype">MC</xsl:with-param>
						<xsl:with-param name="mod_type" select="$mod_type"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="header/@type = 'TF'">
					<xsl:call-template name="mc_question">
						<xsl:with-param name="lab_score" select="$lab_score"/>
						<xsl:with-param name="lab_answer" select="$lab_answer"/>
						<xsl:with-param name="lab_question_id" select="$lab_question_id"/>
						<xsl:with-param name="lab_question_title" select="$lab_question_title"/>
						<xsl:with-param name="lab_objective" select="$lab_objective"/>
						<xsl:with-param name="lab_desc" select="$lab_desc"/>
						<xsl:with-param name="lab_explanation" select="$lab_explanation"/>
						<xsl:with-param name="lab_true" select="$lab_true"/>
						<xsl:with-param name="lab_false" select="$lab_false"/>
						<xsl:with-param name="subtype">TF</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="header/@type = 'FB'">
					<xsl:call-template name="fb_question">
						<xsl:with-param name="lab_score" select="$lab_score"/>
						<xsl:with-param name="lab_answer" select="$lab_answer"/>
						<xsl:with-param name="lab_question_id" select="$lab_question_id"/>
						<xsl:with-param name="lab_question_title" select="$lab_question_title"/>
						<xsl:with-param name="lab_objective" select="$lab_objective"/>
						<xsl:with-param name="lab_desc" select="$lab_desc"/>
						<xsl:with-param name="lab_explanation" select="$lab_explanation"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="header/@type = 'MT'">
					<xsl:call-template name="mt_question">
						<xsl:with-param name="lab_score" select="$lab_score"/>
						<xsl:with-param name="lab_answer" select="$lab_answer"/>
						<xsl:with-param name="lab_question_id" select="$lab_question_id"/>
						<xsl:with-param name="lab_question_title" select="$lab_question_title"/>
						<xsl:with-param name="lab_objective" select="$lab_objective"/>
						<xsl:with-param name="lab_desc" select="$lab_desc"/>
						<xsl:with-param name="lab_explanation" select="$lab_explanation"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="header/@type = 'ES'">
					<xsl:call-template name="es_question">
						<xsl:with-param name="lab_score" select="$lab_score"/>
						<xsl:with-param name="lab_answer" select="$lab_answer"/>
						<xsl:with-param name="lab_question_id" select="$lab_question_id"/>
						<xsl:with-param name="lab_question_title" select="$lab_question_title"/>
						<xsl:with-param name="lab_objective" select="$lab_objective"/>
						<xsl:with-param name="lab_desc" select="$lab_desc"/>
						<xsl:with-param name="lab_explanation" select="$lab_explanation"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="header/@type = 'FSC' or header/@type = 'DSC'">
					<xsl:call-template name="scenario_question">
						<xsl:with-param name="lab_score" select="$lab_score"/>
						<xsl:with-param name="lab_answer" select="$lab_answer"/>
						<xsl:with-param name="lab_question_id" select="$lab_question_id"/>
						<xsl:with-param name="lab_question_title" select="$lab_question_title"/>
						<xsl:with-param name="lab_objective" select="$lab_objective"/>
						<xsl:with-param name="lab_desc" select="$lab_desc"/>
						<xsl:with-param name="lab_explanation" select="$lab_explanation"/>
					</xsl:call-template>
				</xsl:when>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="mc_question">
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_answer"/>
		<xsl:param name="lab_question_id"/>
		<xsl:param name="lab_question_title"/>
		<xsl:param name="lab_objective"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_explanation"/>
		<xsl:param name="lab_true"/>
		<xsl:param name="lab_false"/>
		<xsl:param name="table_width">
			<xsl:value-of select="$wb_doc_table_width"/>
		</xsl:param>
		<xsl:param name="question_num"/>
		<xsl:param name="subtype">MC</xsl:param>
		<xsl:param name="mod_type"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$table_width}">
			<tr>
				<td colspan="2">
					<xsl:if test="$question_num != ''">
						<xsl:value-of select="$question_num"/>. </xsl:if>
					<xsl:value-of select="position()"/>.<xsl:text/>
					<xsl:if test="$mod_type = 'EVN' or $mod_type = 'SVY'">
						<xsl:text>(</xsl:text>
						<xsl:choose>
							<xsl:when test="body/interaction[@order = '1']/@logic = 'SINGLE'"><xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '685')"/></xsl:when>
							<xsl:otherwise><xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '689')"/></xsl:otherwise>
						</xsl:choose>
						<xsl:text>)</xsl:text>
					</xsl:if>
					<xsl:for-each select="body/text() | body/html">
						<xsl:choose>
							<xsl:when test="name() = 'html'">
								<xsl:value-of disable-output-escaping="yes" select="."/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:value-of select="."/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
				</td>
			</tr>
			<xsl:if test="body/object">
				<tr>
					<td colspan="2">
						<img src="{@id}\{body/object/@data}"/>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td height="8" colspan="2">
					<xsl:text/>
				</td>
			</tr>
			<xsl:variable name="outcome" select="outcome"/>
			<xsl:for-each select="body/interaction[@order = '1']/option">
				<tr>
					<td width="10%">
						<xsl:text/>
					</td>
					<td>
						<xsl:variable name="option_id" select="@id"/>
						<xsl:variable name="feedback" select="$outcome[@order = '1']/feedback[@condition = $option_id]"/>
						<xsl:if test="$feedback/@score >= '1' and $export_mode = '2'">
							<xsl:attribute name="bgcolor">Silver</xsl:attribute>
						</xsl:if>
						<xsl:choose>
							<xsl:when test="$subtype = 'MC'">
								<xsl:number format="A"/>.<xsl:text/>
								<xsl:choose>
									<xsl:when test="html">
										<xsl:value-of disable-output-escaping="yes" select="."/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="unescape_html_linefeed">
											<xsl:with-param name="my_right_value">
												<xsl:value-of select="."/>
											</xsl:with-param>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:when test="$subtype = 'TF'">
								<xsl:choose>
									<xsl:when test="text() = 'True'">
										<xsl:value-of select="$lab_true"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_false"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
						<xsl:if test="object">
							<img src="{../../../@id}\{object/@data}"/>
						</xsl:if>
						<xsl:if test="$feedback/@score >= '1' and $export_mode = '2'">
							<xsl:text/>(<xsl:value-of select="$feedback/@score"/>)</xsl:if>
					</td>
				</tr>
			</xsl:for-each>
		</table>
		<xsl:if test="$export_mode = '2'">
			<table cellpadding="3" cellspacing="0" border="0" width="{$table_width}" bgcolor="#FFFF99">
				<tr>
					<td>
						<span class="info">
							<xsl:value-of select="$lab_question_id"/>: <xsl:value-of select="@id"/>
						</span>
					</td>
					<td>
						<span class="info">
							<xsl:value-of select="$lab_question_title"/>: <xsl:value-of select="header/title"/>
						</span>
					</td>
					<td>
						<xsl:if test="$question_num = ''">
							<span class="info">
								<xsl:value-of select="$lab_objective"/>: <xsl:value-of select="header/objective"/>
							</span>
						</xsl:if>
					</td>
				</tr>
				<xsl:if test="header/desc/text() != ''">
					<tr>
						<td colspan="3">
							<span class="info">
								<xsl:value-of select="$lab_desc"/>: 																			<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:value-of select="header/desc/text()"/>
									</xsl:with-param>
								</xsl:call-template>
							</span>
						</td>
					</tr>
				</xsl:if>
				<xsl:if test="explanation/rationale and explanation/rationale != '' and explanation/rationale != 'No Explanation'">
					<tr>
						<td colspan="3">
							<span class="info">
								<xsl:value-of select="$lab_explanation"/>: 
								<xsl:for-each select="body/interaction">
									<xsl:variable name="order" select="@order"/>
									<xsl:call-template name="unescape_html_linefeed">
										<xsl:with-param name="my_right_value">
											<xsl:value-of select="../../explanation[@order=$order]/rationale"/>
										</xsl:with-param>
									</xsl:call-template>
									<br/>
								</xsl:for-each>
							</span>
						</td>
					</tr>
				</xsl:if>
			</table>
			<br/>
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="fb_question">
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_answer"/>
		<xsl:param name="lab_question_id"/>
		<xsl:param name="lab_question_title"/>
		<xsl:param name="lab_objective"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_explanation"/>
		<xsl:variable name="question_num" select="position()"/>
		<xsl:variable name="interaction_cnt" select="count(body/interaction)"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_doc_table_width}">
			<tr>
				<td>
					<xsl:value-of select="$question_num"/>.<xsl:text/>
					<xsl:for-each select="body/text() | body/html | body/interaction">
						<xsl:choose>
							<xsl:when test="name() = 'interaction'">
								<xsl:call-template name="FB_interaction">
									<xsl:with-param name="question_num" select="$question_num"/>
									<xsl:with-param name="interaction_cnt" select="$interaction_cnt"/>
								</xsl:call-template>
							</xsl:when>
							<xsl:when test="name() = 'html'">
								<xsl:value-of disable-output-escaping="yes" select="."/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:value-of select="."/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
				</td>
			</tr>
			<xsl:if test="body/object">
				<tr>
					<td>
						<img src="{@id}\{body/object/@data}"/>
					</td>
				</tr>
			</xsl:if>
		</table>
		<xsl:if test="$export_mode = '2'">
			<table cellpadding="3" cellspacing="0" border="0" width="{$wb_doc_table_width}" bgcolor="#FFFF99">
				<tr>
					<td>
						<span class="info">
							<xsl:value-of select="$lab_question_id"/>: <xsl:value-of select="@id"/>
						</span>
					</td>
					<td>
						<span class="info">
							<xsl:value-of select="$lab_question_title"/>: <xsl:value-of select="header/title"/>
						</span>
					</td>
					<td>
						<span class="info">
							<xsl:value-of select="$lab_objective"/>: <xsl:value-of select="header/objective"/>
						</span>
					</td>
				</tr>
				<xsl:if test="header/desc/text() != ''">
					<tr>
						<td colspan="3">
							<span class="info">
								<xsl:value-of select="$lab_desc"/>: 
									<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:value-of select="header/desc/text()"/>
									</xsl:with-param>
								</xsl:call-template>
							</span>
						</td>
					</tr>
				</xsl:if>
				<xsl:variable name="explanation">
					<xsl:for-each select="body/interaction">
						<xsl:variable name="order" select="@order"/>
						<xsl:if test="../../explanation[@order=$order]/rationale and ../../explanation[@order=$order]/rationale != '' and ../../explanation[@order=$order]/rationale != 'No Explanation'">
							<xsl:if test="$interaction_cnt > 1">
								<xsl:value-of select="$question_num"/>.<xsl:value-of select="@order"/>.</xsl:if>
							<xsl:text/>
							<xsl:call-template name="unescape_html_linefeed">
								<xsl:with-param name="my_right_value">
									<xsl:value-of select="../../explanation[@order=$order]/rationale"/>
								</xsl:with-param>
							</xsl:call-template>
							<br/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<xsl:if test="$explanation != ''">
					<tr>
						<td colspan="3">
							<span class="info">
								<xsl:value-of select="$lab_explanation"/>: 
								<xsl:copy-of select="$explanation"/>
							</span>
						</td>
					</tr>
				</xsl:if>
			</table>
		</xsl:if>
		<br/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="mt_question">
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_answer"/>
		<xsl:param name="lab_question_id"/>
		<xsl:param name="lab_question_title"/>
		<xsl:param name="lab_objective"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_explanation"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_doc_table_width}">
			<tr>
				<td colspan="4">
					<xsl:value-of select="position()"/>.<xsl:text/>
					<xsl:for-each select="body/text() | body/html">
						<xsl:choose>
							<xsl:when test="name() = 'html'">
								<xsl:value-of disable-output-escaping="yes" select="."/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:value-of select="."/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
				</td>
			</tr>
			<xsl:if test="body/object">
				<tr>
					<td colspan="4">
						<img src="{@id}\{body/object/@data}"/>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td colspan="4" height="8">
					<xsl:text/>
				</td>
			</tr>
			<tr>
				<td width="10%">
					<xsl:text/>
				</td>
				<td align="right">
					<table cellpadding="3" cellspacing="0" border="0">
						<xsl:for-each select="body/source/item">
							<tr>
								<td align="right">
									<xsl:if test="object">
										<img src="{../../../@id}\{object/@data}"/>
									</xsl:if>
									<xsl:value-of select="text"/>•
								</td>
							</tr>
						</xsl:for-each>
					</table>
				</td>
				<td width="30%"/>
				<td>
					<table cellpadding="3" cellspacing="0" border="0">
						<xsl:for-each select="body/interaction">
							<tr>
								<td>
									•<xsl:value-of select="text"/>
									<xsl:if test="object">
										<img src="{../../@id}\{object/@data}"/>
									</xsl:if>
								</td>
							</tr>
						</xsl:for-each>
					</table>
				</td>
			</tr>
		</table>
		<xsl:if test="$export_mode = '2'">
			<table cellpadding="3" cellspacing="0" border="0" width="{$wb_doc_table_width}" bgcolor="#FFFF99">
				<tr>
					<td>
						<span class="info">
							<xsl:value-of select="$lab_question_id"/>: <xsl:value-of select="@id"/>
						</span>
					</td>
					<td>
						<span class="info">
							<xsl:value-of select="$lab_question_title"/>: <xsl:value-of select="header/title"/>
						</span>
					</td>
					<td>
						<span class="info">
							<xsl:value-of select="$lab_objective"/>: <xsl:value-of select="header/objective"/>
						</span>
					</td>
				</tr>
				<xsl:if test="header/desc/text() != ''">
					<tr>
						<td colspan="3">
							<span class="info">
								<xsl:value-of select="$lab_desc"/>: 
									<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:value-of select="header/desc/text()"/>
									</xsl:with-param>
								</xsl:call-template>
							</span>
						</td>
					</tr>
				</xsl:if>
				<tr>
					<td colspan="3" bgcolor="Silver">
						<table cellpadding="3" cellspacing="0" border="0" width="100%">
							<tr>
								<td colspan="3" valign="center">
									<xsl:value-of select="$lab_answer"/>
								</td>
								<td>
									<xsl:value-of select="$lab_score"/>
								</td>
								<td>
									<xsl:value-of select="$lab_explanation"/>
								</td>
							</tr>
							<xsl:for-each select="outcome/feedback">
								<xsl:sort select="@condition"/>
								<xsl:variable name="target" select="../@order"/>
								<xsl:variable name="source" select="@condition"/>
								<tr>
									<td>
										<xsl:value-of select="../../body/source/item[@id = $source]/text"/>
									</td>
									<td>----------</td>
									<td>
										<xsl:value-of select="../../body/interaction[@order = $target]/text"/>
									</td>
									<td>
										(<xsl:value-of select="@score"/>)
									</td>
									<td>
										<xsl:variable name="explanation" select="../../explanation[@order = $target]/rationale[@condition = $source]"/>
										<xsl:if test="$explanation != '' and $explanation != 'No Explanation'">
											<xsl:value-of select="$explanation"/>
										</xsl:if>
									</td>
								</tr>
							</xsl:for-each>
						</table>
					</td>
				</tr>
			</table>
		</xsl:if>
		<br/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="es_question">
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_answer"/>
		<xsl:param name="lab_question_id"/>
		<xsl:param name="lab_question_title"/>
		<xsl:param name="lab_objective"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_explanation"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_doc_table_width}">
			<tr>
				<td colspan="2">
					<xsl:value-of select="position()"/>.<xsl:text/>
					<xsl:for-each select="body/text() | body/html">
						<xsl:choose>
							<xsl:when test="name() = 'html'">
								<xsl:value-of disable-output-escaping="yes" select="."/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:value-of select="."/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
					<br/>
					(<xsl:value-of select="outcome/@score"/>)
			</td>
			</tr>
			<xsl:if test="body/object">
				<tr>
					<td colspan="2">
						<img src="{@id}\{body/object/@data}"/>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td colspan="2">
					<xsl:text/>
				</td>
			</tr>
		</table>
		<xsl:if test="$export_mode = '2'">
			<table cellpadding="3" cellspacing="0" border="0" width="{$wb_doc_table_width}" bgcolor="#FFFF99">
				<tr>
					<td>
						<span class="info">
							<xsl:value-of select="$lab_question_id"/>: <xsl:value-of select="@id"/>
						</span>
					</td>
					<td>
						<span class="info">
							<xsl:value-of select="$lab_question_title"/>: <xsl:value-of select="header/title"/>
						</span>
					</td>
					<td>
						<span class="info">
							<xsl:value-of select="$lab_objective"/>: <xsl:value-of select="header/objective"/>
						</span>
					</td>
				</tr>
				<xsl:if test="header/desc/text() != ''">
					<tr>
						<td colspan="3">
							<span class="info">
								<xsl:value-of select="$lab_desc"/>: 
									<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:value-of select="header/desc/text()"/>
									</xsl:with-param>
								</xsl:call-template>
							</span>
						</td>
					</tr>
				</xsl:if>
				<tr>
					<td colspan="3">
						<span class="info">
							<xsl:value-of select="$lab_answer"/>: <xsl:call-template name="unescape_html_linefeed">
								<xsl:with-param name="my_right_value">
									<xsl:value-of select="explanation[@order='1']/rationale"/>
								</xsl:with-param>
							</xsl:call-template>
						</span>
					</td>
				</tr>
			</table>
		</xsl:if>
		<br/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="scenario_question">
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_answer"/>
		<xsl:param name="lab_question_id"/>
		<xsl:param name="lab_question_title"/>
		<xsl:param name="lab_objective"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_explanation"/>
		<xsl:variable name="question_num" select="position()"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_doc_table_width}">
			<tr>
				<td colspan="2">
					<xsl:value-of select="position()"/>.<xsl:text/>
					<xsl:for-each select="body/text() | body/html">
						<xsl:choose>
							<xsl:when test="name() = 'html'">
								<xsl:value-of disable-output-escaping="yes" select="."/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:value-of select="."/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
				</td>
			</tr>
			<xsl:if test="body/object">
				<tr>
					<td colspan="2">
						<img src="{@id}\{body/object/@data}"/>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td colspan="2">
					<xsl:text/>
				</td>
			</tr>
			<tr>
				<td width="10%">
					<xsl:text/>
				</td>
				<td>
					<xsl:for-each select="question_list/question">
						<xsl:call-template name="mc_question">
							<xsl:with-param name="lab_score" select="$lab_score"/>
							<xsl:with-param name="lab_answer" select="$lab_answer"/>
							<xsl:with-param name="table_width" select="$wb_doc_table_width * 0.9"/>
							<xsl:with-param name="question_num" select="$question_num"/>
							<xsl:with-param name="lab_question_id" select="$lab_question_id"/>
							<xsl:with-param name="lab_question_title" select="$lab_question_title"/>
							<xsl:with-param name="lab_objective" select="$lab_objective"/>
							<xsl:with-param name="lab_desc" select="$lab_desc"/>
							<xsl:with-param name="lab_explanation" select="$lab_explanation"/>
						</xsl:call-template>
					</xsl:for-each>
				</td>
			</tr>
		</table>
		<br/>
		<xsl:if test="$export_mode = '2'">
			<table cellpadding="3" cellspacing="0" border="0" width="{$wb_doc_table_width}" bgcolor="#FFFF99">
				<tr>
					<td>
						<span class="info">
							<xsl:value-of select="$lab_question_id"/>: <xsl:value-of select="@id"/>
						</span>
					</td>
					<td>
						<span class="info">
							<xsl:value-of select="$lab_question_title"/>: <xsl:value-of select="header/title"/>
						</span>
					</td>
					<td>
						<span class="info">
							<xsl:value-of select="$lab_objective"/>: <xsl:value-of select="header/objective"/>
						</span>
					</td>
				</tr>
				<xsl:if test="header/desc/text() != ''">
					<tr>
						<td colspan="3">
							<span class="info">
								<xsl:value-of select="$lab_desc"/>: 
									<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:value-of select="header/desc/text()"/>
									</xsl:with-param>
								</xsl:call-template>
							</span>
						</td>
					</tr>
				</xsl:if>
			</table>
			<br/>
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="FB_interaction">
		<xsl:param name="question_num"/>
		<xsl:param name="interaction_cnt"/>
		<xsl:variable name="order" select="@order"/>
		<xsl:variable name="outcome" select="../../outcome[@order = $order]"/>
		<u>[<xsl:if test="$interaction_cnt > 1">
				<xsl:value-of select="$question_num"/>.<xsl:value-of select="@order"/>.</xsl:if>
			<xsl:text/>
			<xsl:choose>
				<xsl:when test="$export_mode = '1'">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;</xsl:when>
				<xsl:when test="$export_mode = '2'">
					<span class="Answer">
						<xsl:for-each select="$outcome/feedback">
							<xsl:if test="position() != 1"> / </xsl:if>
							<xsl:text>&#160;&#160;</xsl:text>
							<xsl:value-of select="@condition"/>
							<xsl:text>&#160;&#160;</xsl:text>(<xsl:value-of select="@score"/>)
			</xsl:for-each>
					</span>
				</xsl:when>
			</xsl:choose>
			<xsl:text>]</xsl:text>
		</u>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
