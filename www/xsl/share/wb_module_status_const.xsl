<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="../utils/change_lowercase.xsl"/>
	<xsl:variable name="lab_viewed">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">已查閱</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">已查阅</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Viewed</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_not_viewed">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">未查閱</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">未查阅</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Not viewed</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_completed">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">已完成</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">已完成</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Completed</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_passed">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">合格</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">合格</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Passed</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_not_attempted">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">未嘗試</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">还未进行</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Not attempted</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_attempted">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">已嘗試</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">已尝试</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Attempted</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_no_show">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">缺席</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">缺席</xsl:when>
			<xsl:when test="$wb_lang = 'en'">No show</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_in_progress">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">進行中</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">进行中</xsl:when>
			<xsl:when test="$wb_lang = 'en'">In progress</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_failed">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">不合格</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">不合格</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Failed</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_graded">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">已批閱</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">已批阅</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Graded</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_not_graded">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">未批閱</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">尚未批阅</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Not graded</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_es_graded">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">已評分</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">已评分</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Graded</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_es_not_graded">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">尚未評分</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">尚未评分</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Not graded</xsl:when>
		</xsl:choose>
	</xsl:variable>	
	
	<xsl:variable name="lab_submitted">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">已遞交</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">已递交</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Submitted</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_not_submitted">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">未遞交</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">未递交</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Not submitted</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_participated">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">已參加</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">已参加</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Participated</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_not_participated">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">未參加</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">未参加</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Not participated</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_incomplete">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">未完成</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">未完成</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Incomplete</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_browsed">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">已瀏覽</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">已浏览</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Browsed</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_started">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">已開始</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">已开始</xsl:when>
			<xsl:when test="$wb_lang = 'en'">Started</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:template name="display_progress_tracking">
		<xsl:param name="status"/>
		<!-- aicc status-->
		<xsl:param name="type"/>
		<!-- module / course-->
		<xsl:param name="mod_type"/>
		<!-- LCT,EXC....etc -->
		<xsl:param name="is_wizpack"/>
		<!-- True if Module Type is wizPack -->
		<xsl:param name="show_text"/>
		<!-- true / false -->
		<xsl:param name="show_icon"/>
		<!-- true / false -->
		<xsl:param name="score"/>
		<!-- For ASS type only -->
		<xsl:param name="criteria"/>
		<!-- For Criteria use only -->
		<!-- true / false -->
		<xsl:param name="essay_grade_status"/>
		<!-- 是否提交 -->
		<xsl:param name="attempted"/>
		
		<xsl:variable name="my_status">
			<xsl:call-template name="change_lowercase">
				<xsl:with-param name="input_value">
					<xsl:value-of select="$status"/>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:if test="$show_icon = 'true'">
			<xsl:choose>
				<xsl:when test="$essay_grade_status != '' and ($essay_grade_status ='Not Graded' or $essay_grade_status = 'Being Graded')">
					<IMG src="{$wb_img_path}sico_not_finish.gif" border="0" hspace="3" align="absmiddle"/>
				</xsl:when>
				<xsl:when test="$my_status = 'f'">
					<IMG src="{$wb_img_path}sico_fail.gif" border="0" hspace="3" align="absmiddle"/>
				</xsl:when>
				<xsl:when test="$my_status = 'p'">
					<IMG src="{$wb_img_path}sico_pass.gif" border="0" hspace="3" align="absmiddle"/>
				</xsl:when>
				<xsl:when test="$my_status = 'i'">
					<IMG src="{$wb_img_path}sico_not_finish.gif" border="0" hspace="3" align="absmiddle"/>
				</xsl:when>
				<xsl:when test="$my_status = 'c'">
					<IMG src="{$wb_img_path}sico_finish.gif" border="0" hspace="3" align="absmiddle"/>
				</xsl:when>
				<xsl:when test="$my_status = 'n'">
					<IMG src="{$wb_img_path}sico_not_start.gif" border="0" hspace="3" align="absmiddle"/>
				</xsl:when>
				<xsl:when test="$my_status = 'b'">
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0" hspace="3" align="absmiddle"/>
				</xsl:when>
				<xsl:otherwise>
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0" hspace="3" align="absmiddle"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
		<xsl:if test="$show_text = 'true'">
			<xsl:choose>
				<xsl:when test="$type = 'course'">
					<xsl:choose>
						<xsl:when test="$my_status = 'p'">
							<xsl:value-of select="$lab_passed"/>
						</xsl:when>
						<xsl:when test="$my_status = 'c'">
							<xsl:value-of select="$lab_completed"/>
						</xsl:when>
						<xsl:when test="$my_status = 'i'">
							<xsl:value-of select="$lab_in_progress"/>
						</xsl:when>
						<xsl:when test="$my_status = 'n'">
							<xsl:value-of select="$lab_no_show"/>
						</xsl:when>
						<xsl:when test="$my_status = 'f'">
							<xsl:value-of select="$lab_incomplete"/>
						</xsl:when>
						<xsl:when test="$my_status = 'b'">
							<xsl:value-of select="$lab_browsed"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_not_attempted"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="$type = 'module'">
					<xsl:choose>
						<xsl:when test="$mod_type = 'GAG' or $mod_type = 'LCT' or $mod_type = 'TUT' or $mod_type = 'RDG' or $mod_type = 'EXP' or $mod_type = 'VOD' or $mod_type = 'REF'">
							<xsl:choose>
								<xsl:when test="$is_wizpack = 'true'">
									<xsl:choose>
										<xsl:when test="$my_status = 'c'">
											<xsl:value-of select="$lab_completed"/>
										</xsl:when>
										<xsl:when test="$my_status = 'i'">
											<xsl:value-of select="$lab_viewed"/>
										</xsl:when>
										<xsl:when test="$my_status = 'n'">
											<xsl:value-of select="$lab_not_viewed"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$lab_not_attempted"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:when>
								<xsl:otherwise>
									<xsl:choose>
										<xsl:when test="$my_status = 'i'">
											<xsl:value-of select="$lab_viewed"/>
										</xsl:when>
										<xsl:when test="$my_status = 'n'">
											<xsl:value-of select="$lab_not_viewed"/>
										</xsl:when>
										<xsl:when test="$my_status = 'c'">
											<xsl:value-of select="$lab_completed"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$lab_not_attempted"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="$mod_type = 'TST' or $mod_type = 'DXT' or $mod_type = 'STX' or $mod_type = 'ASS'">
							<xsl:choose>
								<xsl:when test="$essay_grade_status != ''">
									<xsl:choose>
										<xsl:when test="$essay_grade_status = 'Graded'"><xsl:value-of select="$lab_es_graded"/></xsl:when>
										<xsl:when test="$essay_grade_status = 'Not Graded'"><xsl:value-of select="$lab_es_not_graded"/></xsl:when>
										<xsl:otherwise><xsl:value-of select="$essay_grade_status"/></xsl:otherwise>
									</xsl:choose>
								</xsl:when>
								<xsl:when test="$my_status = 'i' and ($attempted = TRUE or $attempted = 'TRUE')">
									<xsl:value-of select="$lab_submitted"/>
								</xsl:when>
								<xsl:when test="$my_status = 'p'">
									<xsl:value-of select="$lab_passed"/>
								</xsl:when>
								<xsl:when test="$my_status = 'i'">
									<xsl:value-of select="$lab_attempted"/>
								</xsl:when>
								<xsl:when test="$my_status = 'n'">
									<xsl:value-of select="$lab_not_viewed"/>
								</xsl:when>
								<xsl:when test="$my_status = 'f'">
									<xsl:value-of select="$lab_failed"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_not_attempted"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="$mod_type = 'EXC'">
							<xsl:choose>
								<xsl:when test="$my_status = 'i'">
									<xsl:value-of select="$lab_attempted"/>
								</xsl:when>
								<xsl:when test="$my_status = 'n'">
									<xsl:value-of select="$lab_not_viewed"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_not_attempted"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="$mod_type = 'EAS'">
							<xsl:choose>
								<xsl:when test="$my_status ='i' or $my_status = 'n'">
									<xsl:value-of select="$lab_not_submitted"/>
								</xsl:when>
								<xsl:when test="$my_status = 'p'">
									<xsl:value-of select="$lab_passed"/>
								</xsl:when>
								<xsl:when test="$my_status = 'f'">
									<xsl:value-of select="$lab_failed"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_not_attempted"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="$mod_type = 'VCR' or $mod_type = 'FOR' or $mod_type = 'CHT' or $mod_type = 'FAQ'">
							<xsl:choose>
								<xsl:when test="$my_status = 'i'">
									<xsl:value-of select="$lab_participated"/>
								</xsl:when>
								<xsl:when test="$my_status = 'n'">
									<xsl:value-of select="$lab_not_participated"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_not_attempted"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="$mod_type = 'VST'  or $mod_type = 'EXM' or $mod_type = 'ORI' or $mod_type = 'GLO'">
							<xsl:choose>
								<xsl:when test="$my_status = 'i'">
									<xsl:value-of select="$lab_viewed"/>
								</xsl:when>
								<xsl:when test="$my_status = 'n'">
									<xsl:value-of select="$lab_not_viewed"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_not_attempted"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="$mod_type = 'AICC_AU'">
							<xsl:choose>
								<xsl:when test="$my_status = 'b'">
									<xsl:value-of select="$lab_browsed"/>
								</xsl:when>
								<xsl:when test="$my_status = 'p'">
									<xsl:value-of select="$lab_passed"/>
								</xsl:when>
								<xsl:when test="$my_status = 'c'">
									<xsl:value-of select="$lab_completed"/>
								</xsl:when>
								<xsl:when test="$my_status = 'i'">
									<xsl:value-of select="$lab_incomplete"/>
								</xsl:when>
								<xsl:when test="$my_status = 'n'">
									<xsl:value-of select="$lab_not_attempted"/>
								</xsl:when>
								<xsl:when test="$my_status = 'f'">
									<xsl:value-of select="$lab_failed"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_not_attempted"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="$mod_type = 'NETG_COK'">
							<xsl:choose>
								<xsl:when test="$my_status = 'p'">
									<xsl:value-of select="$lab_passed"/>
								</xsl:when>
								<xsl:when test="$my_status = 'c'">
									<xsl:value-of select="$lab_completed"/>
								</xsl:when>
								<xsl:when test="$my_status = 'i'">
									<xsl:value-of select="$lab_started"/>
								</xsl:when>
								<xsl:when test="$my_status = 'n'">
									<xsl:value-of select="$lab_not_attempted"/>
								</xsl:when>
								<xsl:when test="$my_status = 'f'">
									<xsl:value-of select="$lab_failed"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_not_attempted"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="$mod_type = 'SCO'">
							<xsl:choose>
								<xsl:when test="$my_status = 'b'">
									<xsl:value-of select="$lab_browsed"/>
								</xsl:when>
								<xsl:when test="$my_status = 'p'">
									<xsl:value-of select="$lab_passed"/>
								</xsl:when>
								<xsl:when test="$my_status = 'c'">
									<xsl:value-of select="$lab_completed"/>
								</xsl:when>
								<xsl:when test="$my_status = 'i'">
									<xsl:value-of select="$lab_incomplete"/>
								</xsl:when>
								<xsl:when test="$my_status = 'n'">
									<xsl:value-of select="$lab_not_attempted"/>
								</xsl:when>
								<xsl:when test="$my_status = 'f'">
									<xsl:value-of select="$lab_failed"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_not_attempted"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="$mod_type = 'SVY'  or $mod_type = 'TNA' or $mod_type = 'EVN'">
							<xsl:choose>
								<xsl:when test="$my_status = 'c'">
									<xsl:value-of select="$lab_completed"/>
								</xsl:when>
								<xsl:when test="$my_status = 'i'">
									<xsl:value-of select="$lab_attempted"/>
								</xsl:when>
								<xsl:when test="$my_status = 'n'">
									<xsl:value-of select="$lab_not_viewed"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_not_attempted"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="$wb_lang = 'ch'">模塊類型未定</xsl:when>
								<xsl:when test="$wb_lang = 'gb'">未定义模块</xsl:when>
								<xsl:otherwise>Unknown Module Type</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="$wb_lang = 'ch'">非課程或模塊，沒有AICC所適合的類型。</xsl:when>
						<xsl:when test="$wb_lang = 'gb'">非课程或模块，没有AICC所适合的类型。</xsl:when>
						<xsl:otherwise>Not a Course or Module , No AICC status match this type.</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
