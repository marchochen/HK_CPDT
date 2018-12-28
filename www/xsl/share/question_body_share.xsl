<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="../utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="../share/wb_layout_share.xsl"/>
	<xsl:import href="../share/wb_object_share.xsl"/>
	<xsl:import href="../utils/wb_ui_space.xsl"/>
	<xsl:import href="../utils/wb_ui_head.xsl"/>
	<!-- ==========Question Body For wizBank============================================-->
	<xsl:template name="question_body">
		<xsl:param name="width"/>
		<xsl:param name="que_id"/>
		<xsl:param name="view"/>
		<xsl:param name="grade"/>
		<xsl:param name="introduction"/>
		<!-- view : "RPT" for Training Admin -->
		<!-- view : "LRNRPT" for Learner Report -->
		<!--           Default Read Question View -->
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:apply-templates select="body" mode="que">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="que_id" select="$que_id"/>
					<xsl:with-param name="view" select="$view"/>
					<xsl:with-param name="grade" select="$grade"/>
					<xsl:with-param name="introduction" select="$introduction"/>
					<xsl:with-param name="res_answer">答案</xsl:with-param>
					<xsl:with-param name="res_question">題目</xsl:with-param>
					<xsl:with-param name="res_source">來源</xsl:with-param>
					<xsl:with-param name="res_target">對象</xsl:with-param>
					<xsl:with-param name="res_true">正確</xsl:with-param>
					<xsl:with-param name="res_false">錯誤</xsl:with-param>
					<xsl:with-param name="res_your_answer">(您選擇的答案)</xsl:with-param>
					<xsl:with-param name="res_no_exp">沒有簡介</xsl:with-param>
					<xsl:with-param name="res_exp">簡介</xsl:with-param>
					<xsl:with-param name="res_desc">說明</xsl:with-param>
					<xsl:with-param name="lab_or">或</xsl:with-param>
					<xsl:with-param name="lab_score">分數</xsl:with-param>
					<xsl:with-param name="lab_percentage">百分比</xsl:with-param>
					<xsl:with-param name="res_tf_true">是</xsl:with-param>
					<xsl:with-param name="res_tf_false">否</xsl:with-param>
					<xsl:with-param name="lab_model_ans">參考答案</xsl:with-param>
					<xsl:with-param name="lab_learner_ans">學員答案</xsl:with-param>
					<xsl:with-param name="lab_score_awarded">所得分數</xsl:with-param>
					<xsl:with-param name="lab_correct_ans">正確答案</xsl:with-param>
					<xsl:with-param name="lab_attach">附件</xsl:with-param>
					<xsl:with-param name="lab_attachment">學員提交檔案附件</xsl:with-param>
					<xsl:with-param name="lab_blank">空格</xsl:with-param>
					<xsl:with-param name="lab_noexp">--</xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="$wb_lang='gb'">
				<xsl:apply-templates select="body" mode="que">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="que_id" select="$que_id"/>
					<xsl:with-param name="view" select="$view"/>
					<xsl:with-param name="grade" select="$grade"/>
					<xsl:with-param name="introduction" select="$introduction"/>
					<xsl:with-param name="res_answer">答案</xsl:with-param>
					<xsl:with-param name="res_question">题目</xsl:with-param>
					<xsl:with-param name="res_source">来源</xsl:with-param>
					<xsl:with-param name="res_target">对象</xsl:with-param>
					<xsl:with-param name="res_true">正确</xsl:with-param>
					<xsl:with-param name="res_false">错误</xsl:with-param>
					<xsl:with-param name="res_your_answer">(您选择的答案)</xsl:with-param>
					<xsl:with-param name="res_no_exp">没有简介</xsl:with-param>
					<xsl:with-param name="res_exp">简介</xsl:with-param>
					<xsl:with-param name="res_desc">说明</xsl:with-param>
					<xsl:with-param name="lab_or">或</xsl:with-param>
					<xsl:with-param name="lab_score">分数</xsl:with-param>
					<xsl:with-param name="lab_percentage">百分比</xsl:with-param>
					<xsl:with-param name="res_tf_true">对</xsl:with-param>
					<xsl:with-param name="res_tf_false">错</xsl:with-param>
					<xsl:with-param name="lab_model_ans">参考答案</xsl:with-param>
					<xsl:with-param name="lab_learner_ans">学员答案</xsl:with-param>
					<xsl:with-param name="lab_score_awarded">所得分数</xsl:with-param>
					<xsl:with-param name="lab_correct_ans">正确答案</xsl:with-param>
					<xsl:with-param name="lab_attach">附件</xsl:with-param>
					<xsl:with-param name="lab_attachment">学员提交文档附件</xsl:with-param>
					<xsl:with-param name="lab_blank">空格</xsl:with-param>
					<xsl:with-param name="lab_noexp">--</xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="body" mode="que">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="que_id" select="$que_id"/>
					<xsl:with-param name="view" select="$view"/>
					<xsl:with-param name="grade" select="$grade"/>
					<xsl:with-param name="introduction" select="$introduction"/>
					<xsl:with-param name="res_answer">Answer</xsl:with-param>
					<xsl:with-param name="res_question">Question</xsl:with-param>
					<xsl:with-param name="res_source">SOURCE</xsl:with-param>
					<xsl:with-param name="res_target">TARGET</xsl:with-param>
					<xsl:with-param name="res_true">True</xsl:with-param>
					<xsl:with-param name="res_false">False</xsl:with-param>
					<xsl:with-param name="res_your_answer">(Your answer)</xsl:with-param>
					<xsl:with-param name="res_no_exp">No description</xsl:with-param>
					<xsl:with-param name="res_exp">Description</xsl:with-param>
					<xsl:with-param name="res_desc">Explanation</xsl:with-param>
					<xsl:with-param name="lab_or">OR</xsl:with-param>
					<xsl:with-param name="lab_score">Score</xsl:with-param>
					<xsl:with-param name="lab_percentage">percentage</xsl:with-param>
					<xsl:with-param name="res_tf_true">True</xsl:with-param>
					<xsl:with-param name="res_tf_false">False</xsl:with-param>
					<xsl:with-param name="lab_model_ans">Model answer</xsl:with-param>
					<xsl:with-param name="lab_learner_ans">Learner's answer</xsl:with-param>
					<xsl:with-param name="lab_score_awarded">Score awarded</xsl:with-param>
					<xsl:with-param name="lab_correct_ans">Correct answer</xsl:with-param>
					<xsl:with-param name="lab_attach">Attachment</xsl:with-param>
					<xsl:with-param name="lab_attachment">Attachments</xsl:with-param>
					<xsl:with-param name="lab_blank">blank</xsl:with-param>
					<xsl:with-param name="lab_noexp">--</xsl:with-param>
				</xsl:apply-templates>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="body" mode="que">
		<xsl:param name="view"/>
		<xsl:param name="width"/>
		<xsl:param name="grade"/>
		<xsl:param name="introduction"/>
		<xsl:param name="que_id"/>
		<xsl:param name="res_question"/>
		<xsl:param name="res_answer"/>
		<xsl:param name="res_source"/>
		<xsl:param name="res_target"/>
		<xsl:param name="res_your_answer"/>
		<xsl:param name="res_no_exp"/>
		<xsl:param name="res_exp"/>
		<xsl:param name="lab_or"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_percentage"/>
		<xsl:param name="res_tf_true"/>
		<xsl:param name="res_tf_false"/>
		<xsl:param name="lab_model_ans"/>
		<xsl:param name="lab_learner_ans"/>
		<xsl:param name="lab_score_awarded"/>
		<xsl:param name="lab_correct_ans"/>
		<xsl:param name="lab_attachment"/>
		<xsl:param name="lab_attach"/>
		<xsl:param name="lab_blank"/>
		<xsl:param name="lab_noexp"/>
		<xsl:param name="res_desc" />
		<xsl:variable name="mod_type">
			<xsl:choose>
				<xsl:when test="/question/@mod_type">
					<xsl:value-of select="/question/@mod_type"/>
				</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="is_evn_que">
			<xsl:choose>
				<xsl:when test="$mod_type='SVY' or $mod_type='EVN'">true</xsl:when>
				<xsl:otherwise>false</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<!-- MC  -->
			<xsl:when test="interaction[@type='MC']">
				<xsl:variable name="mod_type_1" select="/question_form/@mod_type"/>
				<div class="report_info clean_margin">
					<table style="width: 80%;">
						<xsl:if test="$is_evn_que = 'true'">					
							<xsl:variable name="que_mc_type" select="interaction/@logic"/>
							<tr>
								<td height="10">  
									<xsl:text>(</xsl:text>
									<xsl:choose>
										<xsl:when test="$que_mc_type = 'SINGLE'">
											<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '685')"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '689')"/>
										</xsl:otherwise>
									</xsl:choose>
									<xsl:text>)</xsl:text>
								</td>
							</tr>
						</xsl:if>
						<tr>
							<td>
								<table  style="width: 80%;">
									<tr>
										<td valign="top" class="fuwenben-list">
											<xsl:for-each select="text()|html">
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
										<td align="right" rowspan="2" valign="top">
											<xsl:apply-templates select="object"/>
										</td>
									</tr>
									<tr><td><xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;&nbsp;]]></xsl:text></td></tr>
									<tr>
										<td>
											<table  style="width: 80%;">
												<xsl:for-each select="interaction/option">
													<xsl:variable name="option_id" select="@id"/>
													<tr>
														<td width="50%">
															<table>
																<xsl:attribute name="class">
																	<xsl:choose>
																		<xsl:when test="../../../outcome/feedback[@condition=$option_id and @score >= '1'] or $is_evn_que='true'">Correct</xsl:when>
																		<xsl:otherwise>Incorrect</xsl:otherwise>
																	</xsl:choose>
																</xsl:attribute>
																<tr>
																	<td valign="top" width="2%">
																		<!--<xsl:value-of select="translate(@id,'1234567890','ABCDEFGHIJ')"/>-->
																		<xsl:number value="@id" format="A" lang="en"/>.<xsl:text>&#160;</xsl:text>
																	</td>
																	<td width="98%" align="left">
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
																	</td>
																</tr>
															</table>
														</td>
														<td>
															<xsl:apply-templates select="object"/>
														</td>
														<td style="width: 35px;"><xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;&nbsp;]]></xsl:text></td>
														<xsl:choose>
															<xsl:when test="$is_evn_que = 'false'">
																<!-- ====================================-->
																<xsl:if test="$view = 'RPT' or $view = 'LRNRPT'">
																	<!--== TA View==-->
																	<td  style="width: 110px;">
																		<span class="Text">
																			<i>
																				<xsl:choose>
																					<xsl:when test="//result[@id = $que_id]">
																						<xsl:choose>
																							<xsl:when test="$option_id=//result[@id = $que_id]/interaction/response">
																								<xsl:choose>
																									<xsl:when test="$view = 'RPT'">
																										<xsl:value-of select="$lab_learner_ans"/>
																									</xsl:when>
																									<xsl:otherwise>
																										<xsl:value-of select="$res_your_answer"/>
																									</xsl:otherwise>
																								</xsl:choose>
																							</xsl:when>
																							<xsl:otherwise>
																								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
																							</xsl:otherwise>
																						</xsl:choose>
																					</xsl:when>
																					<xsl:otherwise>
																						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
																					</xsl:otherwise>
																				</xsl:choose>
																			</i>
																		</span>
																	</td>
																	<td>
																		<xsl:choose>
																			<xsl:when test="$option_id=//result[@id = $que_id]/interaction/response">
																				<xsl:choose>
																					<xsl:when test="../../../outcome/feedback[@condition=$option_id and @score >= '1'] ">
																						<img src="{$wb_img_path}correct.gif" align="bottom"/>
																					</xsl:when>
																					<xsl:otherwise>
																						<img src="{$wb_img_path}incorrect.gif"/>
																					</xsl:otherwise>
																				</xsl:choose>
																			</xsl:when>
																			<xsl:otherwise>
																				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
																			</xsl:otherwise>
																		</xsl:choose>
																	</td>
																	<!--== TA View==-->
																</xsl:if>
																<!-- ====================================-->
															</xsl:when>
															<xsl:otherwise>
																<td>
																	<span class="TitleText">
																		<xsl:text>(</xsl:text>
																		<xsl:value-of select="$lab_score"/>
																		<xsl:text>：</xsl:text>
																		<xsl:variable name="score">
																			<xsl:value-of select="../../../outcome/feedback[@condition=$option_id]/@score"/>
																		</xsl:variable>
																		<xsl:choose>
																			<xsl:when test="$score = ''">0</xsl:when>
																			<xsl:otherwise>
																				<xsl:value-of select="$score"/>
																			</xsl:otherwise>
																		</xsl:choose>
																		<xsl:text>)</xsl:text>
																	</span>
																</td>
																<td>
																	<span class="Text">
																		<xsl:choose>
																			<xsl:when test="//result[@id = $que_id]">
																				<xsl:choose>
																					<xsl:when test="$option_id=//result[@id = $que_id]/interaction/response">
																						<xsl:text>&#160;</xsl:text>
																						<xsl:choose>
																							<xsl:when test="$view = 'RPT'">
																								<xsl:value-of select="$lab_learner_ans"/>
																							</xsl:when>
																							<xsl:otherwise>
																								<xsl:value-of select="$res_your_answer"/>
																							</xsl:otherwise>
																						</xsl:choose>
																					</xsl:when>
																					<xsl:otherwise>
																						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
																					</xsl:otherwise>
																				</xsl:choose>
																			</xsl:when>
																			<xsl:otherwise>
																				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
																			</xsl:otherwise>
																		</xsl:choose>
																	</span>
																</td>
															</xsl:otherwise>
														</xsl:choose>
													</tr>
													<tr><td><xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;&nbsp;]]></xsl:text></td></tr>
												</xsl:for-each>
												<xsl:choose>
													<xsl:when test="$view = 'RPT' or $view = 'LRNRPT'">
														<tr>
															<td colspan="5">
																<span class="TitleText">
																	<xsl:value-of select="$lab_score_awarded"/>
																	<xsl:text>：</xsl:text>
																	<xsl:choose>
																		<xsl:when test="../../result/@id">
																			<xsl:value-of select="../../result[@id = $que_id]/interaction/@usr_score"/>/<xsl:value-of select="interaction/@score"/>
																		</xsl:when>
																		<xsl:otherwise>
																			<xsl:value-of select="../../../../result[@id = $que_id]/interaction/@usr_score"/>/<xsl:value-of select="interaction/@score"/>
																		</xsl:otherwise>
																	</xsl:choose>
																</span>
															</td>
														</tr>
													</xsl:when>
													<xsl:otherwise>
														<xsl:if test="$is_evn_que = 'false'">
															<tr>
																<td colspan="3">
																	<span class="wzb-form-label">
																		<xsl:value-of select="$lab_score"/>
																		<xsl:text>：</xsl:text>
																	</span>
																	 <xsl:value-of select="interaction/@score"/>
																</td>
															</tr>
														</xsl:if>
													</xsl:otherwise>
												</xsl:choose>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td height="10">
								<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
							</td>
						</tr>
					</table>
				</div>
				<!--简介-->
				<xsl:if test="../header/desc and ../header/desc != ''">
					<xsl:call-template name="wb_ui_space">
						<xsl:with-param name="width" select="$width"/>
					</xsl:call-template>
					<xsl:if test="$is_evn_que = 'false'" >
					    <div>
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text" select="$res_exp"/>
							<xsl:with-param name="width" select="$width"/>
						</xsl:call-template>
						</div>
						<div class="report_info clean_margin">
							<xsl:choose>
								<xsl:when test="../header/desc">
									<xsl:call-template name="unescape_html_linefeed">
										<xsl:with-param name="my_right_value">
											<xsl:value-of select="../header/desc"/>
										</xsl:with-param>
									</xsl:call-template>
									<xsl:if test="../header/desc = ''">
										<xsl:value-of select="$res_no_exp"/>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$res_no_exp"/>
								</xsl:otherwise>
							</xsl:choose>
						</div>
					</xsl:if>
				</xsl:if>
			</xsl:when>
			<!-- ==============================================-->
			<!-- TF -->
			<xsl:when test="interaction[@type='TF']">
			<div class="report_info clean_margin">
				<table border="0" width="{$width}" cellspacing="0" cellpadding="0" class="Bg">
					<tr>
						<td height="10">
							<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
						</td>
					</tr>
					<tr>
						<td>
							<table border="0" width="100%" cellspacing="0" cellpadding="10">
								<tr>
									<td valign="top">
										<span class="Text fuwenben-list">
											<xsl:for-each select="text()|html">
												<xsl:choose>
													<xsl:when test="name() = 'html'">
														<xsl:value-of disable-output-escaping="yes" select="."/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:call-template name="unescape_html_linefeed">
															<xsl:with-param name="my_right_value" select="."/>
														</xsl:call-template>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:for-each>
										</span>
									</td>
									<td align="right" rowspan="2" valign="top">
										<xsl:apply-templates select="object"/>
										<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
									</td>
								</tr>
								<tr>
									<td>
										<table border="0" cellspacing="0" cellpadding="5">
											<tr>
												<td>
													<table cellspacing="0" border="0" cellpadding="3">
														<xsl:attribute name="class">
															<xsl:choose>
																<xsl:when test="../outcome/feedback[@condition='1' and @score >= '1'] ">Correct</xsl:when>
																<xsl:otherwise>Incorrect</xsl:otherwise>
															</xsl:choose>
														</xsl:attribute>
														<tr>
															<td>
																<span class="Text">
																	<xsl:value-of select="$res_tf_true"/>
																</span>
															</td>
														</tr>
													</table>
												</td>
												<xsl:if test="$view = 'RPT' or  $view = 'LRNRPT'">
													<td>
														<span class="Text">
															<i>
																<xsl:if test="//result[@id = $que_id]">
																	<xsl:choose>
																		<xsl:when test="//result[@id = $que_id]/interaction/response='1'">
																			<xsl:choose>
																				<xsl:when test="$view = 'RPT'">
																					<xsl:value-of select="$lab_learner_ans"/>
																				</xsl:when>
																				<xsl:otherwise>
																					<xsl:value-of select="$res_your_answer"/>
																				</xsl:otherwise>
																			</xsl:choose>
																		</xsl:when>
																		<xsl:otherwise>
																			<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
																		</xsl:otherwise>
																	</xsl:choose>
																</xsl:if>
															</i>
														</span>
													</td>
													<td>
														<xsl:choose>
															<xsl:when test="//result[@id = $que_id]/interaction/response='1'">
																<xsl:choose>
																	<xsl:when test="../outcome/feedback[@condition='1' and @score >= '1']">
																		<img src="{$wb_img_path}correct.gif"/>
																	</xsl:when>
																	<xsl:otherwise>
																		<img src="{$wb_img_path}incorrect.gif"/>
																	</xsl:otherwise>
																</xsl:choose>
															</xsl:when>
															<xsl:otherwise>
																<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
															</xsl:otherwise>
														</xsl:choose>
													</td>
												</xsl:if>
											</tr>
											<tr>
												<td>
													<table cellspacing="0" border="0" cellpadding="3">
														<xsl:attribute name="class">
															<xsl:choose>
																<xsl:when test="../outcome/feedback[@condition='2' and @score >= '1'] ">Correct</xsl:when>
																<xsl:otherwise>Incorrect</xsl:otherwise>
															</xsl:choose>
														</xsl:attribute>
														<tr>
															<td>
																<span class="Text">
																	<xsl:value-of select="$res_tf_false"/>
																</span>
															</td>
														</tr>
													</table>
												</td>
												<td>
													<span class="Text">
														<i>
															<xsl:if test="//result[@id = $que_id]">
																<xsl:choose>
																	<xsl:when test="//result[@id = $que_id]/interaction/response='2'">
																		<xsl:choose>
																			<xsl:when test="$view = 'RPT'">
																				<xsl:value-of select="$lab_learner_ans"/>
																			</xsl:when>
																			<xsl:otherwise>
																				<xsl:value-of select="$res_your_answer"/>
																			</xsl:otherwise>
																		</xsl:choose>
																	</xsl:when>
																	<xsl:otherwise>
																		<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
																	</xsl:otherwise>
																</xsl:choose>
															</xsl:if>
														</i>
													</span>
												</td>
												<td>
													<xsl:choose>
														<xsl:when test="//result[@id = $que_id]/interaction/response='2'">
															<xsl:choose>
																<xsl:when test="../outcome/feedback[@condition='2' and @score >= '1']">
																	<img src="{$wb_img_path}correct.gif" align="bottom"/>
																</xsl:when>
																<xsl:otherwise>
																	<img src="{$wb_img_path}incorrect.gif"/>
																</xsl:otherwise>
															</xsl:choose>
														</xsl:when>
														<xsl:otherwise>
															<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
														</xsl:otherwise>
													</xsl:choose>
												</td>
											</tr>
											<xsl:choose>
												<xsl:when test="$view = 'RPT' or $view = 'LRNRPT'">
													<tr>
														<td colspan="3">
															<span class="TitleText">
																<xsl:value-of select="$lab_score_awarded"/>
																<xsl:text>：</xsl:text>
																<xsl:value-of select="../../result[@id = $que_id]/interaction/@usr_score"/>/<xsl:value-of select="interaction/@score"/>
															</span>
														</td>
													</tr>
												</xsl:when>
												<xsl:otherwise>
													<tr>
														<td colspan="3">
															<span class="TitleText">
																<xsl:value-of select="$lab_score"/>
																<xsl:text>：</xsl:text>
																<xsl:value-of select="interaction/@score"/>
															</span>
														</td>
													</tr>
												</xsl:otherwise>
											</xsl:choose>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td height="10">
							<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
						</td>
					</tr>
				</table>
				</div>
				<!--简介-->
				<xsl:if test="../header/desc and ../header/desc != '' ">
					<xsl:call-template name="wb_ui_space">
						<xsl:with-param name="width" select="$width"/>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="$res_exp"/>
						<xsl:with-param name="width" select="$width"/>
						<!-- <xsl:with-param name="new_template">true</xsl:with-param> -->
					</xsl:call-template>
					<table cellpadding="0" cellspacing="0" border="0" class="Bg" width="{$width}">
						<tr>
							<td>
								<span class="Text">
									<xsl:choose>
										<xsl:when test="../header/desc">
											<xsl:call-template name="unescape_html_linefeed">
												<xsl:with-param name="my_right_value">
													<xsl:value-of select="../header/desc"/>
												</xsl:with-param>
											</xsl:call-template>
											<xsl:if test="../header/desc = '' ">
												<xsl:value-of select="$res_no_exp"/>
											</xsl:if>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$res_no_exp"/>
										</xsl:otherwise>
									</xsl:choose>
								</span>
							</td>
						</tr>
						<tr>
							<td height="10">
								<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
							</td>
						</tr>
					</table>
				</xsl:if>
			</xsl:when>
			<!-- ==============================================-->
			<!-- FB -->
			<xsl:when test="interaction[@type='FB']">
				<div class="report_info clean_margin">
				<table border="0" width="{$width}" cellspacing="0" cellpadding="0" class="Bg">
					<tr>
						<td height="10">
							<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
						</td>
					</tr>
					<tr>
						<td>
							<table border="0" width="100%" cellspacing="0" cellpadding="10">
								<tr>
									<td valign="top">
										<span class="fuwenben-list">
											<xsl:for-each select="text()|html|interaction">
												<xsl:choose>
													<xsl:when test="name() = 'interaction' and $is_evn_que = 'false'">
														<xsl:call-template name="FB_interaction"/>
													</xsl:when>
													<xsl:when test="name() = 'html'">
														<xsl:value-of disable-output-escaping="yes" select="."/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of disable-output-escaping="yes" select="."/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:for-each>
										</span>
<!-- 									</td> -->
<!-- 									<td valign="top" align="right"> -->
										<br/>
										<xsl:apply-templates select="object"/>
										<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
									</td>
								</tr>
								<xsl:choose>
									<xsl:when test="$view = 'RPT' or $view = 'LRNRPT'">
										<tr>
											<td colspan="2">
												<span class="TitleText">
													<xsl:value-of select="$lab_score_awarded"/>
													<xsl:text>：</xsl:text>
													<xsl:value-of select="sum(../../result[@id = $que_id]/interaction/@usr_score)"/>/<xsl:value-of select="sum(interaction/@score)"/>
												</span>
											</td>
										</tr>
									</xsl:when>
									<xsl:otherwise>
										<!--<tr>
											<td colspan="2">
												<span class="TitleText">
													<xsl:value-of select="$lab_score"/>
													<xsl:text>：</xsl:text>
													<xsl:value-of select="sum(interaction/@score)"/>
												</span>
											</td>
										</tr>-->
									</xsl:otherwise>
								</xsl:choose>
							</table>
						</td>
					</tr>
					<xsl:if test="$view = 'RPT' or $view = 'LRNRPT'">
						<tr>
							<td>
								<table cellpadding="3" cellspacing="0" border="0" width="100%" class="Bg">
								<tr class="SecBg">
									<td width="8"><img src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/></td>
									<td nowrap="nowrap" align="left"><span class="TitleText" style="color:#999999;"><xsl:value-of select="$lab_blank"/></span></td>
									<td width="45%"><span class="TitleText" style="color:#999999;"><xsl:value-of select="$lab_correct_ans"/></span></td>
									<td nowrap="nowrap" align="right"><span class="TitleText" style="color:#999999;"><xsl:value-of select="$lab_score"/></span></td>
									<td width="45%"><span class="TitleText" style="color:#999999;"><xsl:value-of select="$res_exp"/></span></td>
									<td width="8"><img src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/></td>
								</tr>
								<xsl:call-template name="fb_draw_correct_answer"/>								
								</table>
								<!--
									<tr>
										<td>
											<fieldset style="background:none;" class="Box">
												<legend>
													<xsl:value-of select="$lab_correct_ans"/>
												</legend>
												<xsl:call-template name="fb_show_correct_answer">
													<xsl:with-param name="width">100%</xsl:with-param>
													<xsl:with-param name="lab_score" select="$lab_score"/>
													<xsl:with-param name="lab_or" select="$lab_or"/>
													<xsl:with-param name="res_no_exp" select="$res_no_exp"/>
													<xsl:with-param name="view" select="$view"/>
												</xsl:call-template>
											</fieldset>
										</td>
									</tr>
									
								</table>
								-->
							</td>
						</tr>
					</xsl:if>
				</table>
				</div>
				<xsl:if test="$view != 'RPT' and $view != 'LRNRPT'">
					<xsl:if test="$is_evn_que = 'false'">
						<xsl:call-template name="wb_ui_space">
							<xsl:with-param name="width" select="$width"/>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text" select="$res_answer"/>
							<xsl:with-param name="width" select="$width"/>
							<!-- <xsl:with-param name="new_template">true</xsl:with-param> -->
						</xsl:call-template>
						<table width="{$width}" cellspacing="0" cellpadding="" border="0" class="Bg">
							<tr>
								<td>
									<xsl:call-template name="fb_show_correct_answer">
										<xsl:with-param name="width" select="$width"/>
										<xsl:with-param name="lab_score" select="$lab_score"/>
										<xsl:with-param name="lab_or" select="$lab_or"/>
										<xsl:with-param name="res_no_exp" select="$res_no_exp"/>
										<xsl:with-param name="view" select="$view"/>
										<xsl:with-param name="res_exp" select="$res_desc"/>
									</xsl:call-template>
								</td>
							</tr>
							<tr>
							  <td>
								   <xsl:value-of select="$lab_score"/>：<xsl:value-of select="../header/@score"/>
							   </td>
							</tr>
						</table>
					</xsl:if>
				</xsl:if>
				<!--简介-->
				<xsl:if test="$introduction != 'false'">
					<xsl:if test="../header/desc and ../header/desc != '' ">
						<xsl:call-template name="wb_ui_space">
							<xsl:with-param name="width" select="$width"/>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text" select="$res_exp"/>
							<xsl:with-param name="width" select="$width"/>
							<!-- <xsl:with-param name="new_template">true</xsl:with-param> -->
						</xsl:call-template>
						<table cellpadding="0" cellspacing="0" border="0" class="Bg" width="{$width}">
							<tr>
								<td>
									<span class="Text">
										<xsl:choose>
											<xsl:when test="../header/desc">
												<xsl:call-template name="unescape_html_linefeed">
													<xsl:with-param name="my_right_value">
														<xsl:value-of select="../header/desc"/>
													</xsl:with-param>
												</xsl:call-template>
												<xsl:if test="../header/desc = '' ">
													<xsl:value-of select="$res_no_exp"/>
												</xsl:if>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="$res_no_exp"/>
											</xsl:otherwise>
										</xsl:choose>
									</span>
								</td>
							</tr>
							<tr>
								<td height="10">
									<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
								</td>
							</tr>
						</table>
					</xsl:if>
			  </xsl:if>
			</xsl:when>
			<!-- ==============================================-->
			<!-- ES -->
			<xsl:when test="interaction[@type='ES']">
				<div class="report_info clean_margin" style="padding:0 10px;">
				<table border="0" width="{$width}" cellspacing="0" cellpadding="0" class="Bg">
					<tr>
						<td height="10">
							<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
						</td>
					</tr>
					<tr>
						<td>
							<table border="0" width="100%" cellspacing="0" cellpadding="10">
								<tr>
									<td valign="top">
										<span class="Text fuwenben-list">
											<xsl:for-each select="text()|html">
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
										</span>
									</td>
									<td valign="top" align="right">
										<xsl:apply-templates select="object"/>
										<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td height="10">
							<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
						</td>
					</tr>
				</table>
				<xsl:variable name="with_result">
					<xsl:choose>
						<!-- in group report, dont show the learners' answer -->
						<xsl:when test="/group_report/@id != ''">false</xsl:when>
						<xsl:when test="//result[@id = $que_id]">true</xsl:when>
						<xsl:otherwise>false</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="td_width">
					<xsl:choose>
						<xsl:when test="$with_result = 'true'">50%</xsl:when>
						<xsl:otherwise>100%</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<table width="{$width}" cellspacing="0" cellpadding="0" border="0" class="Bg">
					<tr>
						<td>

							<table width="100%" cellspacing="0" cellpadding="10" border="0">
								<tr>
									<td width="{$td_width}" valign="top">
										<fieldset style="background:none;margin:0 16px 0 0;" class="Box">
											<legend style="font-size:14px;margin-bottom:4px;padding:2px 5px;color:#999;">
												<xsl:value-of select="$lab_model_ans"/>
											</legend>
											<table cellpadding="5" cellspacing="0" border="0" width="95%" align="center">
												<tr>
													<td style="padding:5px;color:#666;">
														<span class="Text">
															<xsl:choose>
																<xsl:when test="../explanation/rationale">
																	<xsl:call-template name="unescape_html_linefeed">
																		<xsl:with-param name="my_right_value">
																			<xsl:value-of select="../explanation/rationale"/>
																		</xsl:with-param>
																	</xsl:call-template>
																	<xsl:if test="../explanation/rationale = '' ">
																		<xsl:text>--</xsl:text>
																	</xsl:if>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:text>--</xsl:text>
																</xsl:otherwise>
															</xsl:choose>
														</span>
													</td>
												</tr>
											</table>
										</fieldset>
									</td>
									<xsl:if test="$with_result = 'true'">
										<td width="{$td_width}" valign="top">
											<fieldset style="background:none;margin-left:16px;" class="Box">
												<legend style="font-size:14px;margin-bottom:4px;padding:2px 5px;color:#999;">
													<xsl:choose>
														<xsl:when test="$view = 'LRNRPT'">
															<xsl:value-of select="$res_your_answer"/>
														</xsl:when>
														<xsl:otherwise>
															<xsl:value-of select="$lab_learner_ans"/>
														</xsl:otherwise>
													</xsl:choose>
												</legend>
												<table cellpadding="5" cellspacing="0" border="0" width="95%" align="center">
													<tr>
														<td style="padding:5px;color:#666;">
															<span class="Text">
																<xsl:call-template name="unescape_html_linefeed">
																	<xsl:with-param name="my_right_value">
																		<xsl:value-of select="../../result[@id = $que_id]/interaction/response"/>
																	</xsl:with-param>
																</xsl:call-template>
																<xsl:if test="not(../../result[@id = $que_id]/interaction/response)"><br/></xsl:if>
															</span>
														</td>
													</tr>
												</table>
											</fieldset>
											<xsl:if test="count(../../file_list[@id = $que_id]/file) &gt; 0">
												<br/>
												<fieldset style="background:none;" class="Box">
													<legend>
														<xsl:value-of select="$lab_attachment"/>
													</legend>
													<xsl:for-each select="../../file_list[@id = $que_id]/file">
														<table border="0" cellspacing="5" cellpadding="0">
															<tr>
																<td valign="top">
																	<span class="Text">
																		<xsl:value-of select="$lab_attach"/>
																		<xsl:text>&#160;</xsl:text>
																		<xsl:value-of select="position()"/> : </span>
																</td>
																<td valign="middle">
																	<a class="Text" target="_blank">
																		<xsl:attribute name="href">../resource/<xsl:value-of select="/student_report/student/test/@id"/>/<xsl:value-of select="/student_report/student/@ent_id"/>/<xsl:value-of select="$que_id"/>/<xsl:value-of select="/student_report/attempt_list/@current"/>/<xsl:value-of select="@data"/>
																		</xsl:attribute>
																		<xsl:value-of select="@data"/>
																	</a>
																</td>
															</tr>
														</table>
													</xsl:for-each>
												</fieldset>
											</xsl:if>
										</td>
									</xsl:if>
								</tr>
								<tr>
									<td>
										<xsl:choose>
											<xsl:when test="$view = 'RPT' or $view = 'LRNRPT'">
												<tr>
													<td style="padding:10px 0 0 0;">
														<span class="TitleText">
															<xsl:value-of select="$lab_score_awarded"/>
															<xsl:text>：</xsl:text>
															<xsl:variable name="my_score">
																<xsl:value-of select="sum(../../result[@id = $que_id]/interaction/@usr_score)"/>
															</xsl:variable>
															<xsl:choose>
																<xsl:when test="$grade = 'true'">
																	<xsl:variable name="score">
																		<xsl:if test="../../result[@id = $que_id]/interaction/@usr_score != -1">
																			<xsl:value-of select="../../result[@id = $que_id]/interaction/@usr_score"/>
																		</xsl:if>
																	</xsl:variable>
																	<input type="text" class="wzb-inputText" name="score_{$que_id}" size="{string-length(sum(interaction/@score))}" maxlength="{string-length(sum(interaction/@score))}" value="{$score}"/>
																	<input type="hidden" name="max_{$que_id}" value="{sum(interaction/@score)}"/>
																</xsl:when>
																<xsl:when test="$my_score = -1">--</xsl:when>
																<xsl:otherwise>
																	<xsl:value-of select="$my_score"/>
																</xsl:otherwise>
															</xsl:choose>&#160;/&#160;<xsl:value-of select="sum(interaction/@score)"/>
														</span>
													</td>
												</tr>
											</xsl:when>
											<xsl:otherwise>
												<tr>
													<td>
														<span class="TitleText">
															<xsl:value-of select="$lab_score"/>
															<xsl:text>：</xsl:text>
															<xsl:value-of select="sum(interaction/@score)"/>
														</span>
													</td>
												</tr>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				</div>
				<!--简介-->
				<xsl:if test="../header/desc and ../header/desc != '' ">
					<xsl:call-template name="wb_ui_space">
						<xsl:with-param name="width" select="$width"/>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="$res_exp"/>
						<xsl:with-param name="width" select="$width"/>
						<!-- <xsl:with-param name="new_template">true</xsl:with-param> -->
					</xsl:call-template>
					<table cellpadding="0" cellspacing="0" border="0" class="Bg" width="{$width}">
						<tr>
							<td style="padding:10px;">
								<span class="Text" style="color:#666;">
									<xsl:choose>
										<xsl:when test="../header/desc">
											<xsl:call-template name="unescape_html_linefeed">
												<xsl:with-param name="my_right_value">
													<xsl:value-of select="../header/desc"/>
												</xsl:with-param>
											</xsl:call-template>
											<xsl:if test="../header/desc = '' ">
												<xsl:value-of select="$res_no_exp"/>
											</xsl:if>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$res_no_exp"/>
										</xsl:otherwise>
									</xsl:choose>
								</span>
							</td>
						</tr>
						<tr>
							<td height="10">
								<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
							</td>
						</tr>
					</table>
				</xsl:if>
			</xsl:when>
			<!-- ==============================================-->
			<!-- MT  -->
			<xsl:when test="interaction[@type='MT']">
				<div class="report_info clean_margin">
				<table border="0" width="{$width}" cellspacing="0" cellpadding="0" class="Bg">
					<tr>
						<td>
							<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
						</td>
					</tr>
					<tr>
						<td>
							<table border="0" width="{$width}" cellspacing="0" cellpadding="10">
								<tr>
									<td valign="top">
										<span class="Text fuwenben-list">
											<xsl:for-each select="text()|html|interaction">
												<xsl:choose>
													<xsl:when test="name() = 'interaction'"/>
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
										</span>
									</td>
									<td align="right" valign="top">
										<xsl:apply-templates select="object"/>
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
						</td>
					</tr>
					<tr>
						<td align="center">
							<xsl:if test="//result[@id = $que_id]">
								<table cellpadding="10" cellspacing="0" border="0" width="{$width}" class="Bg">
									<tr>
										<td>
											<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
										</td>
									</tr>
									<tr>
										<td>
											<fieldset style="background:none;" class="Box">
												<legend style="font-size:16px;margin-bottom:10px;">
													<xsl:choose>
														<xsl:when test="$view = 'LRNRPT'">
															<xsl:value-of select="$res_your_answer"/>
														</xsl:when>
														<xsl:otherwise>
															<xsl:value-of select="$lab_learner_ans"/>
														</xsl:otherwise>
													</xsl:choose>
												</legend>
												<table border="0" cellspacing="0" cellpadding="5">
													<tr>
														<td valign="top">
															<xsl:for-each select="//result[@id = $que_id]/interaction">
																<xsl:for-each select="response">
																	<xsl:variable name="media_width" select="//question[@id=$que_id]/body/media/@width"/>
																	<xsl:variable name="media_height" select="//question[@id=$que_id]/body/media/@height"/>
																	<xsl:variable name="option_id" select="../@order"/>
																	<xsl:variable name="usr_rep" select="."/>
																	<xsl:variable name="qid" select="../../@id"/>
																	<xsl:variable name="usr_ans" select="//question[@id=$qid]/body/source/item[@id=$usr_rep]/text"/>
																	<p>
																	<table cellpadding="0" cellspacing="0" border="0">
																		<tr><td>
																		<div style="position:relative;width:{$media_width}px;height:{$media_height}px;border-width:1px;border-style:solid;overflow:auto">
																			<xsl:if test="//question[@id=$qid]/body/source/item[@id=$usr_rep]/object">
																				<xsl:apply-templates select="//question[@id=$qid]/body/source/item[@id=$usr_rep]/object"/>
																			</xsl:if>
																			<xsl:if test="//question[@id=$qid]/body/source/item[@id=$usr_rep]/text">
																				<div class="Text" align="center">
																					<xsl:value-of select="//question[@id=$qid]/body/source/item[@id=$usr_rep]/text"/>
																				</div>
																			</xsl:if>
																		</div></td><td>
																		<img src="{$wb_img_path}mt_line.gif" width="85" height="1" border="0" alt="" align="absmiddle"/>
																		</td><td>
																		<div style="position:relative;width:{$media_width}px;height:{$media_height}px;border-width:1px;border-style:solid;overflow: auto">
																			<xsl:if test="//question[@id=$qid]/body/interaction[@order=$option_id]/object">
																				<xsl:apply-templates select="//question[@id=$qid]/body/interaction[@order=$option_id]/object"/>
																			</xsl:if>
																			<xsl:if test="//question[@id=$qid]/body/interaction[@order=$option_id]/text">
																				<div class="Text" align="center">
																					<xsl:value-of select="//question[@id=$qid]/body/interaction[@order=$option_id]/text"/>
																				</div>
																			</xsl:if>
																		</div></td><td>
																		<xsl:choose>
																			<xsl:when test="//question[@id=$qid]/outcome[@order=$option_id]/feedback[@condition=$usr_rep]">
																				<span class="SmallText">
																					<img src="{$wb_img_path}correct.gif" align="bottom" hspace="2"/>
																					<xsl:text>&#160;(</xsl:text>
																					<xsl:value-of select="$lab_score"/> : <xsl:value-of select="//question[@id=$qid]/outcome[@order=$option_id]/feedback[@condition=$usr_rep]/@score"/>
																					<xsl:text>)</xsl:text>
																				</span>
																			</xsl:when>
																			<xsl:otherwise>
																				<img src="{$wb_img_path}incorrect.gif" align="bottom" hspace="2"/>
																			</xsl:otherwise>
																		</xsl:choose>
																		</td></tr>
																		</table>
																	</p>
																</xsl:for-each>
															</xsl:for-each>
														</td>
													</tr>
												</table>
											</fieldset>
										</td>
									</tr>
								</table>
							</xsl:if>
						</td>
					</tr>
				</table>
				<xsl:if test="not(//result[@id = $que_id])">
					<table border="0" cellpadding="10" cellspacing="0" class="Bg" width="{$width}">
						<tr>
							<td align="center" width="48%" valign="top">
								<span class="TextBold">
									<xsl:value-of select="$res_source"/>
									<br/>
									<xsl:for-each select="source/item">
										<xsl:variable name="media_width" select="../../media/@width"/>
										<xsl:variable name="media_height" select="../../media/@height"/>
										<div STYLE="position:relative;width:{$media_width}px;height:{$media_height}px;border-width:1px;border-style:solid;overflow: auto">
											<xsl:if test="object">
												<xsl:apply-templates select="object"/>
												<br/>
											</xsl:if>
											<xsl:if test="text">
												<span class="SmallText">
													<xsl:call-template name="unescape_html_linefeed">
														<xsl:with-param name="my_right_value" select="text"/>
													</xsl:call-template>
												</span>
											</xsl:if>
										</div>
										<br/>
										<br/>
									</xsl:for-each>
								</span>
							</td>
							<td width="4%" height="100%" style="border-left:1px solid #333">
								<table cellpadding="0" cellspacing="0" border="0" width="1" height="100%">
									<tr>
										<td bgcolor="#000000" >
											
										</td>
									</tr>
								</table>
							</td>
							<td align="center" width="48%" valign="top">
								<span class="TextBold">
									<xsl:value-of select="$res_target"/>
									<br/>
									<xsl:for-each select="interaction">
										<xsl:variable name="media_width" select="../media/@width"/>
										<xsl:variable name="media_height" select="../media/@height"/>
										<div STYLE="position:relative;width:{$media_width}px;height:{$media_height}px;border-width:1px;border-style:solid;overflow:auto">
											<xsl:if test="object">
												<xsl:apply-templates select="object"/>
											</xsl:if>
											<xsl:if test="text">
												<span class="SmallText">
													<xsl:call-template name="unescape_html_linefeed">
														<xsl:with-param name="my_right_value" select="text"/>
													</xsl:call-template>
												</span>
											</xsl:if>
										</div>
										<br/>
										<br/>
									</xsl:for-each>
								</span>
							</td>
						</tr>
					</table>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="$view != 'RPT' and $view != 'LRNRPT'">
						<xsl:call-template name="wb_ui_space">
							<xsl:with-param name="width" select="$width"/>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text" select="$res_answer"/>
							<xsl:with-param name="width" select="$width"/>
							<xsl:with-param name="new_template">true</xsl:with-param>
						</xsl:call-template>
						<table cellpadding="0" cellspacing="0" border="0" width="{$width}" class="Bg">
							<tr>
								<td align="center">
									<table border="0" cellpadding="10" cellspacing="0" class="Bg">
										<tr>
											<td>
												<table border="0" cellpadding="0" cellspacing="0">
													<!--xsl:value-of select="name()"/-->
													<xsl:for-each select="../outcome">
														<xsl:variable name="order" select="@order"/>
														<xsl:variable name="obj_id" select="../@id"/>
														<xsl:for-each select="feedback">
															<tr>
																<xsl:variable name="media_width" select="//question[@id=$que_id]/body/media/@width"/>
																<xsl:variable name="media_height" select="//question[@id=$que_id]/body/media/@height"/>
																<xsl:variable name="usr_rep" select="@condition"/>
																<td align="center" valign="top">
																	<div STYLE="position:relative;width:{$media_width}px;height:{$media_height}px;border-width:1px;border-style:solid;overflow:auto">
																		<xsl:if test="../../body/source/item[@id=$usr_rep]/object">
																			<xsl:apply-templates select="../../body/source/item[@id=$usr_rep]/object"/>
																		</xsl:if>
																		<xsl:if test="../../body/source/item[@id=$usr_rep]/text">
																			<div class="Text" align="center">
																				<xsl:call-template name="unescape_html_linefeed">
																					<xsl:with-param name="my_right_value" select="../../body/source/item[@id=$usr_rep]/text"/>
																				</xsl:call-template>
																			</div>
																		</xsl:if>
																	</div>
																</td>
																<td align="center" valign="middle">
																	<img src="{$wb_img_path}mt_line.gif" width="85" height="1" border="0" alt="" align="absmiddle"/>
																</td>
																<td align="left" valign="top">
																	<div STYLE="position: relative; width:{$media_width}px; height:{$media_height}px; border-width:1; border-style: solid; z-index: 1;overflow: auto">
																		<xsl:if test="../../body/interaction[@order=$order]/object">
																			<xsl:apply-templates select="../../body/interaction[@order=$order]/object"/>
																		</xsl:if>
																		<xsl:if test="../../body/interaction[@order=$order]/text">
																			<div class="Text" align="center">
																				<xsl:call-template name="unescape_html_linefeed">
																					<xsl:with-param name="my_right_value">
																						<xsl:value-of select="../../body/interaction[@order=$order]/text"/>
																					</xsl:with-param>
																				</xsl:call-template>
																			</div>
																		</xsl:if>
																	</div>
																</td>
															</tr>
															<tr>
																<td colspan="3" align="right">
																	<span class="SmallText">
																		<xsl:text>&#160;(</xsl:text><xsl:value-of select="$lab_score"/> : 
																		<xsl:choose>
																			<xsl:when test="@score > 0"><xsl:value-of select="@score"/></xsl:when>
																			<xsl:otherwise>
																				0
																			</xsl:otherwise>																		
																		</xsl:choose>
																		<!--<xsl:if test="//result[@id = $que_id]">
														<xsl:text>&#160;</xsl:text>
														<xsl:value-of select="$lab_percentage"/>：<xsl:variable name="penc" select="//result[@id = 	$que_id]/response_stat/item[@response=$usr_rep and @int_order=$order]/@percentage"/>
														<xsl:choose>
															<xsl:when test="$penc != ''">
																<xsl:value-of select="$penc"/>
															</xsl:when>
															<xsl:otherwise>0</xsl:otherwise>
														</xsl:choose>
														<xsl:text>%</xsl:text>
													</xsl:if>-->
																		<xsl:text>)</xsl:text>
																	</span>
																</td>
															</tr>
															<tr>
																<td colspan="3">
																	<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
																</td>
															</tr>
														</xsl:for-each>
													</xsl:for-each>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</xsl:when>
					<xsl:otherwise>
						<table cellpadding="10" cellspacing="0" border="0" width="{$width}" class="Bg">
							<tr>
								<td>
									<fieldset style="background:none;" class="Box">
										<legend style="font-size:16px;margin-bottom:10px;padding:28px 0 0 0;">
											<xsl:value-of select="$lab_correct_ans"/>
										</legend>
										<table border="0" cellpadding="10" cellspacing="0" class="Bg">
											<tr>
												<td>
													<table border="0" cellpadding="0" cellspacing="0">
														<!--xsl:value-of select="name()"/-->
														<xsl:for-each select="../outcome">
															<xsl:variable name="order" select="@order"/>
															<xsl:variable name="obj_id" select="../@id"/>
															<xsl:for-each select="feedback">
																<tr>
																	<xsl:variable name="media_width" select="//question[@id=$que_id]/body/media/@width"/>
																	<xsl:variable name="media_height" select="//question[@id=$que_id]/body/media/@height"/>
																	<xsl:variable name="usr_rep" select="@condition"/>
																	<td align="right" valign="top">
																		<div style="position:relative;width:{$media_width}px;height:{$media_height}px;border-width:1px;border-style:solid;overflow:auto">
																			<xsl:if test="../../body/source/item[@id=$usr_rep]/object">
																				<xsl:apply-templates select="../../body/source/item[@id=$usr_rep]/object"/>
																			</xsl:if>
																			<xsl:if test="../../body/source/item[@id=$usr_rep]/text">
																				<div class="Text" align="center">
																					<xsl:call-template name="unescape_html_linefeed">
																						<xsl:with-param name="my_right_value" select="../../body/source/item[@id=$usr_rep]/text"/>
																					</xsl:call-template>
																				</div>
																			</xsl:if>
																		</div>
																	</td>
																	<td align="center" valign="middle">
																		<img src="{$wb_img_path}mt_line.gif" width="85" height="1" border="0" alt="" align="absmiddle"/>
																	</td>
																	<td align="left" valign="top">
																		<div style="position: relative; width:{$media_width}px; height:{$media_height}px; border-width:1; border-style: solid; z-index: 1;overflow: auto">
																			<xsl:if test="../../body/interaction[@order=$order]/object">
																				<xsl:apply-templates select="../../body/interaction[@order=$order]/object"/>
																			</xsl:if>
																			<xsl:if test="../../body/interaction[@order=$order]/text">
																				<div class="Text" align="center">
																					<xsl:call-template name="unescape_html_linefeed">
																						<xsl:with-param name="my_right_value">
																							<xsl:value-of select="../../body/interaction[@order=$order]/text"/>
																						</xsl:with-param>
																					</xsl:call-template>
																				</div>
																			</xsl:if>
																		</div>
																	</td>
																	<td valign="bottom">
																	<span class="SmallText">
																			<xsl:text>&#160;( </xsl:text><xsl:value-of select="$lab_score"/> :
																			<xsl:choose>
																			<xsl:when test="@score > 0"><xsl:value-of select="@score"/></xsl:when>
																			<xsl:otherwise>
																				0
																			</xsl:otherwise>																		
																		</xsl:choose>																												
																		<xsl:text>)</xsl:text>
																	</span>
																	</td>			
																</tr>
																<!--
																<tr>
																	<td colspan="3" align="right">
																		<span class="SmallText">
																			<xsl:text>&#160;(</xsl:text>
																			<xsl:value-of select="$lab_score"/> : <xsl:value-of select="@score"/>
																			<xsl:text>)</xsl:text>
																		</span>
																	</td>
																</tr>
																-->
																<!--说明-->
<!-- 																<tr>
																	<td colspan="4">
																	<span class="SmallText">
																		<xsl:value-of select="$res_exp"/><xsl:text>：</xsl:text>
																		<xsl:value-of select="../../explanation[@order=$order]/rationale/text()"/>
																		<xsl:choose>
																			<xsl:when test="../../explanation[@order=$order]/rationale/text()">
																				<xsl:value-of select="../../explanation[@order=$order]/rationale/text()"/>
																			</xsl:when>
																			<xsl:otherwise><xsl:value-of select="$lab_noexp"/></xsl:otherwise>
																		</xsl:choose>
																	</span>
																	</td>																														</tr>
 -->																<tr>
																	<td colspan="3">
																		<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
																	</td>
																</tr>
															</xsl:for-each>
														</xsl:for-each>
													</table>
												</td>
											</tr>
										</table>
									</fieldset>
								</td>
							</tr>
						</table>
					</xsl:otherwise>
				</xsl:choose>
				<table cellpadding="0" border="0" cellspacing="0" width="{$width}" class="Bg">
					<tr>
						<td>
							<table cellpadding="10" border="0" cellspacing="0" width="100%">
								<tr>
									<td>
										<xsl:choose>
											<xsl:when test="$view = 'RPT' or $view = 'LRNRPT'">
												<tr>
													<td>
														<span class="TitleText">
															<xsl:value-of select="$lab_score_awarded"/>
															<xsl:text>：</xsl:text>
															<xsl:value-of select="sum(../../result[@id = $que_id]/interaction/@usr_score)"/>/<xsl:value-of select="sum(interaction/@score)"/>
														</span>
													</td>
												</tr>
											</xsl:when>
											<xsl:otherwise>
												<tr>
													<td>
														<span class="TitleText">
															<xsl:value-of select="$lab_score"/>
															<xsl:text>：</xsl:text>
															<xsl:value-of select="sum(interaction/@score)"/>
														</span>
													</td>
												</tr>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				</div>
				<!--简介-->
				<xsl:if test="../header/desc and ../header/desc != '' ">
					<xsl:call-template name="wb_ui_space">
						<xsl:with-param name="width" select="$width"/>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="$res_exp"/>
						<xsl:with-param name="width" select="$width"/>
						<!-- <xsl:with-param name="new_template">true</xsl:with-param> -->
					</xsl:call-template>
					<table cellpadding="0" cellspacing="0" border="0" class="Bg" width="{$width}">
						<tr>
							<td>
								<span class="Text">
									<xsl:choose>
										<xsl:when test="../header/desc">
											<xsl:call-template name="unescape_html_linefeed">
												<xsl:with-param name="my_right_value">
													<xsl:value-of select="../header/desc"/>
												</xsl:with-param>
											</xsl:call-template>
											<xsl:if test="../header/desc = '' ">
												<xsl:value-of select="$res_no_exp"/>
											</xsl:if>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$res_no_exp"/>
										</xsl:otherwise>
									</xsl:choose>
								</span>
							</td>
						</tr>
						<tr>
							<td height="10">
								<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
							</td>
						</tr>
					</table>
				</xsl:if>
			</xsl:when>
			
			
			<!-- FSC -->
			<xsl:when test="../header[@type='FSC']">
				<div class="report_info clean_margin">
				<table border="0" width="{$width}" cellspacing="0" cellpadding="0" class="bg">
					<tr>
						<td colspan="2">
							<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<table border="0" width="{$width - 8}" cellspacing="3" cellpadding="0">
								<tr>
									<td>
										<span class="wbResText">
											<xsl:for-each select="text()|html">
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
										</span>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td align="right" valign="top">
					      <table border="0" width="100%" cellspacing="3" cellpadding="0">
					       <xsl:for-each select="../body/object">
					        <tr>    
					         <td valign="top">
					          <table  border="0" cellspacing="3" cellpadding="0">
					           <tr>
					            <td valign="middle"><xsl:apply-templates select="."/></td>
					           </tr>
					          </table>
					         </td>
					        </tr>
					       </xsl:for-each>
					      </table>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
						</td>
					</tr>
					<tr>
						<td>
							<table border="0" width="{$width}" cellspacing="3" cellpadding="0">
								<tr>
									<td>
										<span class="TitleText">
											<xsl:value-of select="$lab_score"/>
											<xsl:text>：</xsl:text>
											<xsl:choose>
												<xsl:when test="../header/@score"><xsl:value-of select="../header/@score"/></xsl:when>
												<xsl:otherwise><xsl:value-of select="../@score"/></xsl:otherwise>
											</xsl:choose>
										</span>
									</td>
									<td>
										<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
						</td>
					</tr>
				</table>
				</div>
			</xsl:when>
			<!-- DSC -->
			<xsl:when test="../header[@type='DSC']">
				<div class="report_info clean_margin">
				<table border="0" width="{$width}" cellspacing="3" cellpadding="0" class="bg">
					<tr>
						<td colspan="2">
							<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<table border="0" width="{$width - 8}" cellspacing="3" cellpadding="0">
								<tr>
									<td>
										<span class="wbResText">
											<xsl:for-each select="text()|html">
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
										</span>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td align="right" valign="top">
					      <table border="0"  width="{$width}" cellspacing="3" cellpadding="0">
					       <xsl:for-each select="../body/object">
					        <tr>    
					         <td valign="top">
					          <table  border="0" cellspacing="3" cellpadding="0">
					           <tr>
					            <td valign="middle"><xsl:apply-templates select="."/></td>
					           </tr>
					          </table>
					         </td>
					        </tr>
					       </xsl:for-each>
					      </table>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
						</td>
					</tr>
					<tr>
						<td>
							<table border="0" width="{$width}" cellspacing="3" cellpadding="0">
								<tr>
									<td>
										<span class="TitleText">
											<xsl:value-of select="$lab_score"/>
											<xsl:text>：</xsl:text>
											<xsl:choose>
												<xsl:when test="../header/@score"><xsl:value-of select="../header/@score"/></xsl:when>
												<xsl:otherwise><xsl:value-of select="../@score"/></xsl:otherwise>
											</xsl:choose>
										</span>
									</td>
									<td>
										<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
						</td>
					</tr>
				</table>
				</div>
			</xsl:when>
			<!-- ================================ Not Applicatble ============================= -->			<xsl:otherwise>
				<span class="Text">Unknown Resources Type</span>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ================================FB Interaction============================= -->	<xsl:template name="FB_interaction">
		<xsl:variable name="order" select="@order"/>
		<xsl:variable name="que_id" select="../../@id"/>
		<xsl:variable name="result_interaction" select="//result[@id = $que_id]/interaction[@order=$order]"/>
		<xsl:choose>
			<xsl:when test="//result[@id = $que_id] and $result_interaction">
				<xsl:text>[</xsl:text>
				<xsl:value-of select="$order"/>
				<xsl:text>.</xsl:text>
				<xsl:value-of select="$result_interaction/response"/>
				<xsl:choose>
					<xsl:when test="$result_interaction[@correct = 'true']">
						<img src="{$wb_img_path}correct.gif"/>
					</xsl:when>
					<xsl:otherwise>
						<img src="{$wb_img_path}incorrect.gif"/>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text>]</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>[_____</xsl:text>
				<xsl:value-of select="$order"/>
				<xsl:text>_____]</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ================================FB Show Answer============================= -->	<xsl:template name="fb_show_correct_answer">
		<xsl:param name="width"/>
		<xsl:param name="lab_or"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="res_no_exp"/>
		<xsl:param name="view"/>
		<xsl:param name="res_exp"/>
		<table border="0" cellpadding="5" cellspacing="0" width="{$width}">
			<xsl:for-each select="interaction">
				<xsl:variable name="order" select="@order"/>
				<xsl:variable name="outcome" select="../../outcome[@order=$order]"/>
				<tr>
					<td>
						<span class="Text">
							<xsl:value-of select="$order"/>
							<xsl:text>&#160;.&#160;</xsl:text>
						</span>
						<span class="Text">
							<xsl:for-each select="$outcome/feedback">
								<xsl:if test="position() > 1">
									<span class="SmallText">
										<b>
											<xsl:text>&#160;</xsl:text>
											<xsl:value-of select="$lab_or"/>
											<xsl:text>&#160;</xsl:text>
										</b>
									</span>
								</xsl:if>
								<xsl:value-of select="@condition"/>
								<xsl:variable name="con" select="@condition"/>
								<xsl:text>&#160;(</xsl:text>
								<xsl:value-of select="$lab_score"/>：<xsl:value-of select="@score"/>;
								<!--说明-->
								<span class="Text">
									<xsl:choose>
										<xsl:when test="../../explanation[@order=$order]/rationale[@condition=$con]">
											<xsl:choose>
												<xsl:when test="../../explanation[@order=$order]/rationale[@condition=$con]/text() = 'No Explanation'">
													<xsl:value-of select="$res_no_exp"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$res_exp"/>：  
													<xsl:call-template name="unescape_html_linefeed">
														<xsl:with-param name="my_right_value">
														 
														    <xsl:choose>
														         <xsl:when test="../../explanation[@order=$order]/rationale[@condition=$con]/text() != '' ">
														          <xsl:value-of select="../../explanation[@order=$order]/rationale[@condition=$con]/text()"/>
														         </xsl:when>
														        <xsl:otherwise> -- </xsl:otherwise>
														    </xsl:choose>
														     
														</xsl:with-param>
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$res_no_exp"/>
										</xsl:otherwise>
									</xsl:choose>
								</span>
								<xsl:text>)</xsl:text>
							</xsl:for-each>
						</span>
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>
<!-- ================================FB Show Answer============================= --><xsl:template name="fb_draw_correct_answer">
	<xsl:for-each select="../outcome">
		<xsl:if test="position()!='1'">
			<tr>
				<td width="8" height="1"><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></td>
				<td width="8" height="1"><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></td>
				<td colspan="4" height="1"><img src="{$wb_img_path}fm_menu_sep.gif" width="100%" height="1" border="0"/></td>
			</tr>
		</xsl:if>
		<xsl:variable name="order" select="@order"/>
		<xsl:variable name="num" select="count(feedback)"/>
			<xsl:for-each select="feedback">
				<xsl:variable name="ans_pos" select="position()"/>
				<tr class="SecBg">
					<xsl:if test="position()='1'">
						<td width="8" rowspan="{$num}-1"><img src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/></td>
						<td valign="top" align="left" rowspan="{$num}"><xsl:value-of select="$order"/><xsl:text>.</xsl:text></td>
					</xsl:if>
					<td valign="top"><xsl:value-of select="@condition"/></td>
					<td align="right" valign="top"><xsl:value-of select="@score"/></td>
					<td valign="top"><xsl:value-of select="../../explanation[@order= $order]/rationale[$ans_pos]"/></td>
					<td width="8"><img src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/></td>
				</tr>
			</xsl:for-each>
	</xsl:for-each>
</xsl:template>
</xsl:stylesheet>
