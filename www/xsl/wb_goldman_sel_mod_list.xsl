<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:output indent="no"/>
	<xsl:variable name="width">
		<xsl:choose>
			<xsl:when test="/selmod/sel_mod_option/width/text() != '' and /selmod/sel_mod_option/width/text() != 'null'">
				<xsl:value-of select="/selmod/sel_mod_option/width/text()"/>
			</xsl:when>
			<xsl:otherwise>482</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
	<!-- =============================================================== -->
	<xsl:template match="/selmod">
		<xsl:call-template name="main"/>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<xsl:call-template name="wb_init_lab"/>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_no_mod">沒有模塊</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_on">已發佈</xsl:with-param>
			<xsl:with-param name="lab_off">未發佈</xsl:with-param>
			<xsl:with-param name="lab_no_status"> -- </xsl:with-param>
			<xsl:with-param name="lab_alt_RDG">教材</xsl:with-param>
			<xsl:with-param name="lab_alt_REF">參考</xsl:with-param>
			<xsl:with-param name="lab_alt_GLO">詞彙表</xsl:with-param>
			<xsl:with-param name="lab_alt_VOD">視頻點播</xsl:with-param>
			<xsl:with-param name="lab_alt_ASS">作業</xsl:with-param>
			<xsl:with-param name="lab_alt_SVY">課程評估問卷</xsl:with-param>
			<xsl:with-param name="lab_alt_FOR">討論區</xsl:with-param>
			<xsl:with-param name="lab_alt_FAQ">解答欄</xsl:with-param>
			<xsl:with-param name="lab_alt_CHT">聊天室</xsl:with-param>
			<xsl:with-param name="lab_alt_AICC_AU">AICC 課件</xsl:with-param>
			<xsl:with-param name="lab_alt_NETG_COK">NETg 課件</xsl:with-param>
			<xsl:with-param name="lab_alt_SCO">SCORM Courseware</xsl:with-param>
			<xsl:with-param name="lab_alt_TST">測驗</xsl:with-param>
			<xsl:with-param name="lab_alt_DXT">測驗</xsl:with-param>
			<xsl:with-param name="lab_alt_FDR">文件夾</xsl:with-param>
			<xsl:with-param name="lab_OK">確定</xsl:with-param>
			<xsl:with-param name="lab_close">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_no_mod">没有模块</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_on">已发布</xsl:with-param>
			<xsl:with-param name="lab_off">未发布</xsl:with-param>
			<xsl:with-param name="lab_no_status"> -- </xsl:with-param>
			<xsl:with-param name="lab_alt_RDG">教材</xsl:with-param>
			<xsl:with-param name="lab_alt_REF">参考</xsl:with-param>
			<xsl:with-param name="lab_alt_GLO">词汇表</xsl:with-param>
			<xsl:with-param name="lab_alt_VOD">视频点播</xsl:with-param>
			<xsl:with-param name="lab_alt_ASS">作业</xsl:with-param>
			<xsl:with-param name="lab_alt_SVY">课程评估问卷</xsl:with-param>
			<xsl:with-param name="lab_alt_FOR">论坛</xsl:with-param>
			<xsl:with-param name="lab_alt_FAQ">答疑栏</xsl:with-param>
			<xsl:with-param name="lab_alt_CHT">聊天室</xsl:with-param>
			<xsl:with-param name="lab_alt_AICC_AU">AICC 课件</xsl:with-param>
			<xsl:with-param name="lab_alt_NETG_COK">NETg 课件</xsl:with-param>
			<xsl:with-param name="lab_alt_SCO">SCORM Courseware</xsl:with-param>
			<xsl:with-param name="lab_alt_TST">测验</xsl:with-param>
			<xsl:with-param name="lab_alt_DXT">测验</xsl:with-param>
			<xsl:with-param name="lab_alt_FDR">文件夹</xsl:with-param>
			<xsl:with-param name="lab_OK">确定</xsl:with-param>
			<xsl:with-param name="lab_close">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_no_mod">No modules found</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_on">Published</xsl:with-param>
			<xsl:with-param name="lab_off">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_no_status"> -- </xsl:with-param>
			<xsl:with-param name="lab_alt_RDG">Learning material</xsl:with-param>
			<xsl:with-param name="lab_alt_REF">Reference links</xsl:with-param>
			<xsl:with-param name="lab_alt_GLO">English glossary</xsl:with-param>
			<xsl:with-param name="lab_alt_VOD">Video on demand</xsl:with-param>
			<xsl:with-param name="lab_alt_ASS">Assignment</xsl:with-param>
			<xsl:with-param name="lab_alt_SVY">Course evaluation</xsl:with-param>
			<xsl:with-param name="lab_alt_FOR">Forum</xsl:with-param>
			<xsl:with-param name="lab_alt_FAQ">Q&amp;A</xsl:with-param>
			<xsl:with-param name="lab_alt_CHT">Chat room</xsl:with-param>
			<xsl:with-param name="lab_alt_AICC_AU">AICC courseware</xsl:with-param>
			<xsl:with-param name="lab_alt_NETG_COK">NETg courseware</xsl:with-param>
			<xsl:with-param name="lab_alt_SCO">SCORM courseware</xsl:with-param>
			<xsl:with-param name="lab_alt_TST">Test</xsl:with-param>
			<xsl:with-param name="lab_alt_DXT">Test</xsl:with-param>
			<xsl:with-param name="lab_alt_FDR">Folder</xsl:with-param>
			<xsl:with-param name="lab_OK">OK</xsl:with-param>
			<xsl:with-param name="lab_close">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_on"/>
		<xsl:param name="lab_off"/>
		<xsl:param name="lab_no_status"/>
		<xsl:param name="lab_no_mod"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_OK"/>
		<xsl:param name="lab_close"/>
		<xsl:param name="lab_alt_RDG"/>
		<xsl:param name="lab_alt_REF"/>
		<xsl:param name="lab_alt_GLO"/>
		<xsl:param name="lab_alt_VOD"/>
		<xsl:param name="lab_alt_ASS"/>
		<xsl:param name="lab_alt_SVY"/>
		<xsl:param name="lab_alt_FOR"/>
		<xsl:param name="lab_alt_FAQ"/>
		<xsl:param name="lab_alt_CHT"/>
		<xsl:param name="lab_alt_AICC_AU"/>
		<xsl:param name="lab_alt_NETG_COK"/>
		<xsl:param name="lab_alt_SCO"/>
		<xsl:param name="lab_alt_TST"/>
		<xsl:param name="lab_alt_DXT"/>
		<xsl:param name="lab_alt_FDR"/>
		<xsl:choose>
			<xsl:when test="modules">
				<xsl:choose>
					<xsl:when test="sel_mod_option/sel_type = 'import_mod'">
						<xsl:choose>
						<!--
							<xsl:when test="modules/tableofcontents/item[@identifierref = /selmod/modules/mod_list/module/@id]  or modules/tableofcontents/itemtype/text() = 'FDR'">
							-->
							<xsl:when test="modules/tableofcontents/item">
								<xsl:call-template name="dis_mod">
									<xsl:with-param name="lab_off">
										<xsl:value-of select="$lab_off"/>
									</xsl:with-param>
									<xsl:with-param name="lab_on">
										<xsl:value-of select="$lab_on"/>
									</xsl:with-param>
									<xsl:with-param name="lab_no_status">
										<xsl:value-of select="$lab_no_status"/>
									</xsl:with-param>
									<xsl:with-param name="lab_type">
										<xsl:value-of select="$lab_type"/>
									</xsl:with-param>
									<xsl:with-param name="lab_status">
										<xsl:value-of select="$lab_status"/>
									</xsl:with-param>
									<xsl:with-param name="lab_title">
										<xsl:value-of select="$lab_title"/>
									</xsl:with-param>
									<xsl:with-param name="lab_OK">
										<xsl:value-of select="$lab_OK"/>
									</xsl:with-param>
									<xsl:with-param name="lab_close">
										<xsl:value-of select="$lab_close"/>
									</xsl:with-param>
									<xsl:with-param name="lab_alt_RDG">
										<xsl:value-of select="$lab_alt_RDG"/>
									</xsl:with-param>
									<xsl:with-param name="lab_alt_REF">
										<xsl:value-of select="$lab_alt_REF"/>
									</xsl:with-param>
									<xsl:with-param name="lab_alt_GLO">
										<xsl:value-of select="$lab_alt_GLO"/>
									</xsl:with-param>
									<xsl:with-param name="lab_alt_VOD">
										<xsl:value-of select="$lab_alt_VOD"/>
									</xsl:with-param>
									<xsl:with-param name="lab_alt_ASS">
										<xsl:value-of select="$lab_alt_ASS"/>
									</xsl:with-param>
									<xsl:with-param name="lab_alt_SVY">
										<xsl:value-of select="$lab_alt_SVY"/>
									</xsl:with-param>
									<xsl:with-param name="lab_alt_FOR">
										<xsl:value-of select="$lab_alt_FOR"/>
									</xsl:with-param>
									<xsl:with-param name="lab_alt_FAQ">
										<xsl:value-of select="$lab_alt_FAQ"/>
									</xsl:with-param>
									<xsl:with-param name="lab_alt_CHT">
										<xsl:value-of select="$lab_alt_CHT"/>
									</xsl:with-param>
									<xsl:with-param name="lab_alt_AICC_AU">
										<xsl:value-of select="$lab_alt_AICC_AU"/>
									</xsl:with-param>
									<xsl:with-param name="lab_alt_NETG_COK">
										<xsl:value-of select="$lab_alt_NETG_COK"/>
									</xsl:with-param>
									<xsl:with-param name="lab_alt_SCO">
										<xsl:value-of select="$lab_alt_SCO"/>
									</xsl:with-param>
									<xsl:with-param name="lab_alt_TST">
										<xsl:value-of select="$lab_alt_TST"/>
									</xsl:with-param>
									<xsl:with-param name="lab_alt_DXT">
										<xsl:value-of select="$lab_alt_DXT"/>
									</xsl:with-param>
									<xsl:with-param name="lab_alt_FDR">
										<xsl:value-of select="$lab_alt_FDR"/>
									</xsl:with-param>
									<xsl:with-param name="lab_no_mod">
										<xsl:value-of select="$lab_no_mod"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="no_mod">
									<xsl:with-param name="lab_no_mod">
										<xsl:value-of select="$lab_no_mod"/>
									</xsl:with-param>
									<xsl:with-param name="lab_OK">
										<xsl:value-of select="$lab_OK"/>
									</xsl:with-param>
									<xsl:with-param name="lab_close">
										<xsl:value-of select="$lab_close"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="sel_mod_option/dis_mod_type = '2'">
								<xsl:choose>
									<xsl:when test="modules/tableofcontents/item[@identifierref = /selmod/modules/mod_list/module/@id]">
										<xsl:call-template name="dis_mod">
											<xsl:with-param name="lab_off">
												<xsl:value-of select="$lab_off"/>
											</xsl:with-param>
											<xsl:with-param name="lab_on">
												<xsl:value-of select="$lab_on"/>
											</xsl:with-param>
											<xsl:with-param name="lab_no_status">
												<xsl:value-of select="$lab_no_status"/>
											</xsl:with-param>
											<xsl:with-param name="lab_type">
												<xsl:value-of select="$lab_type"/>
											</xsl:with-param>
											<xsl:with-param name="lab_status">
												<xsl:value-of select="$lab_status"/>
											</xsl:with-param>
											<xsl:with-param name="lab_title">
												<xsl:value-of select="$lab_title"/>
											</xsl:with-param>
											<xsl:with-param name="lab_OK">
												<xsl:value-of select="$lab_OK"/>
											</xsl:with-param>
											<xsl:with-param name="lab_close">
												<xsl:value-of select="$lab_close"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_RDG">
												<xsl:value-of select="$lab_alt_RDG"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_REF">
												<xsl:value-of select="$lab_alt_REF"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_GLO">
												<xsl:value-of select="$lab_alt_GLO"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_VOD">
												<xsl:value-of select="$lab_alt_VOD"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_ASS">
												<xsl:value-of select="$lab_alt_ASS"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_SVY">
												<xsl:value-of select="$lab_alt_SVY"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_FOR">
												<xsl:value-of select="$lab_alt_FOR"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_FAQ">
												<xsl:value-of select="$lab_alt_FAQ"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_CHT">
												<xsl:value-of select="$lab_alt_CHT"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_AICC_AU">
												<xsl:value-of select="$lab_alt_AICC_AU"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_NETG_COK">
												<xsl:value-of select="$lab_alt_NETG_COK"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_SCO">
												<xsl:value-of select="$lab_alt_SCO"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_TST">
												<xsl:value-of select="$lab_alt_TST"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_DXT">
												<xsl:value-of select="$lab_alt_DXT"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_FDR">
												<xsl:value-of select="$lab_alt_FDR"/>
											</xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="no_mod">
											<xsl:with-param name="lab_no_mod">
												<xsl:value-of select="$lab_no_mod"/>
											</xsl:with-param>
											<xsl:with-param name="lab_OK">
												<xsl:value-of select="$lab_OK"/>
											</xsl:with-param>
											<xsl:with-param name="lab_close">
												<xsl:value-of select="$lab_close"/>
											</xsl:with-param>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="modules/tableofcontents/descendant::item[@identifierref = /selmod/modules/mod_list/module/@id]">
										<xsl:call-template name="dis_mod">
											<xsl:with-param name="lab_off">
												<xsl:value-of select="$lab_off"/>
											</xsl:with-param>
											<xsl:with-param name="lab_on">
												<xsl:value-of select="$lab_on"/>
											</xsl:with-param>
											<xsl:with-param name="lab_no_status">
												<xsl:value-of select="$lab_no_status"/>
											</xsl:with-param>
											<xsl:with-param name="lab_type">
												<xsl:value-of select="$lab_type"/>
											</xsl:with-param>
											<xsl:with-param name="lab_status">
												<xsl:value-of select="$lab_status"/>
											</xsl:with-param>
											<xsl:with-param name="lab_title">
												<xsl:value-of select="$lab_title"/>
											</xsl:with-param>
											<xsl:with-param name="lab_OK">
												<xsl:value-of select="$lab_OK"/>
											</xsl:with-param>
											<xsl:with-param name="lab_close">
												<xsl:value-of select="$lab_close"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_RDG">
												<xsl:value-of select="$lab_alt_RDG"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_REF">
												<xsl:value-of select="$lab_alt_REF"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_GLO">
												<xsl:value-of select="$lab_alt_GLO"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_VOD">
												<xsl:value-of select="$lab_alt_VOD"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_ASS">
												<xsl:value-of select="$lab_alt_ASS"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_SVY">
												<xsl:value-of select="$lab_alt_SVY"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_FOR">
												<xsl:value-of select="$lab_alt_FOR"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_FAQ">
												<xsl:value-of select="$lab_alt_FAQ"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_CHT">
												<xsl:value-of select="$lab_alt_CHT"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_AICC_AU">
												<xsl:value-of select="$lab_alt_AICC_AU"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_NETG_COK">
												<xsl:value-of select="$lab_alt_NETG_COK"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_SCO">
												<xsl:value-of select="$lab_alt_SCO"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_TST">
												<xsl:value-of select="$lab_alt_TST"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_DXT">
												<xsl:value-of select="$lab_alt_DXT"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_FDR">
												<xsl:value-of select="$lab_alt_FDR"/>
											</xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="no_mod">
											<xsl:with-param name="lab_no_mod">
												<xsl:value-of select="$lab_no_mod"/>
											</xsl:with-param>
											<xsl:with-param name="lab_OK">
												<xsl:value-of select="$lab_OK"/>
											</xsl:with-param>
											<xsl:with-param name="lab_close">
												<xsl:value-of select="$lab_close"/>
											</xsl:with-param>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="no_mod">
					<xsl:with-param name="lab_no_mod">
						<xsl:value-of select="$lab_no_mod"/>
					</xsl:with-param>
					<xsl:with-param name="lab_OK">
						<xsl:value-of select="$lab_OK"/>
					</xsl:with-param>
					<xsl:with-param name="lab_close">
						<xsl:value-of select="$lab_close"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="no_mod">
		<xsl:param name="lab_no_mod"/>
		<xsl:param name="lab_OK"/>
		<xsl:param name="lab_close"/>
		<table>
			<tr>
				<td width="8">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
				<td align="center">
					<div class="losedata" style="margin-top:5px">
						<i class="fa fa-folder-open-o"></i>
						<p><xsl:value-of select="$lab_no_mod"/></p>
					</div>
				</td>
				<td width="8">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="dis_mod">
		<xsl:param name="lab_on"/>
		<xsl:param name="lab_off"/>
		<xsl:param name="lab_no_status"/>
		<xsl:param name="lab_no_mod"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_OK"/>
		<xsl:param name="lab_close"/>
		<xsl:param name="lab_lang"/>
		<xsl:param name="lab_alt_RDG"/>
		<xsl:param name="lab_alt_REF"/>
		<xsl:param name="lab_alt_GLO"/>
		<xsl:param name="lab_alt_VOD"/>
		<xsl:param name="lab_alt_ASS"/>
		<xsl:param name="lab_alt_SVY"/>
		<xsl:param name="lab_alt_FOR"/>
		<xsl:param name="lab_alt_FAQ"/>
		<xsl:param name="lab_alt_CHT"/>
		<xsl:param name="lab_alt_AICC_AU"/>
		<xsl:param name="lab_alt_NETG_COK"/>
		<xsl:param name="lab_alt_SCO"/>
		<xsl:param name="lab_alt_TST"/>
		<xsl:param name="lab_alt_DXT"/>
		<xsl:param name="lab_alt_FDR"/>
		<xsl:variable name="path">/selmod/modules/mod_list/module</xsl:variable>
		<table>
			<tr>
				<td width="1%">
					<xsl:if test="/selmod/sel_mod_option/is_multiple = 'true'">
						<input name="sel_all_checkbox" type="checkbox" onclick="javascript:gen_frm_sel_all_checkbox(document.frmXml,this)"/>
					</xsl:if>
				</td>
				<td align="left" nowrap="nowrap" width="5%" style="color:#999999;">
					<xsl:value-of select="$lab_type"/>
				</td>
				<td width="33%" align="left" style="color:#999999;">
					<span class="TitleText">
						<xsl:value-of select="$lab_title"/>
					</span>
				</td>
				<td align="left" nowrap="nowrap" width="33%" style="color:#999999;">
					<xsl:value-of select="$lab_status"/>
				</td>
			</tr>
			<tr style="hight:5px;">
			   <td  colSpan="20"><!-- <hr/> --><xsl:call-template name="wb_ui_line"/></td>
			</tr>
			<xsl:choose>
				<xsl:when test="/selmod/sel_mod_option/sel_type = 'import_mod'">
					<xsl:choose>
						<xsl:when test="/selmod/modules/mod_list/module">
								<xsl:for-each select="/selmod/modules/tableofcontents/item[@identifierref = /selmod/modules/mod_list/module/@id or itemtype = 'FDR']">
									<tr>
										<xsl:call-template name="item">
											<xsl:with-param name="lab_off">
												<xsl:value-of select="$lab_off"/>
											</xsl:with-param>
											<xsl:with-param name="lab_on">
												<xsl:value-of select="$lab_on"/>
											</xsl:with-param>
											<xsl:with-param name="lab_no_status">
												<xsl:value-of select="$lab_no_status"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_RDG">
												<xsl:value-of select="$lab_alt_RDG"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_REF">
												<xsl:value-of select="$lab_alt_REF"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_GLO">
												<xsl:value-of select="$lab_alt_GLO"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_VOD">
												<xsl:value-of select="$lab_alt_VOD"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_ASS">
												<xsl:value-of select="$lab_alt_ASS"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_SVY">
												<xsl:value-of select="$lab_alt_SVY"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_FOR">
												<xsl:value-of select="$lab_alt_FOR"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_FAQ">
												<xsl:value-of select="$lab_alt_FAQ"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_CHT">
												<xsl:value-of select="$lab_alt_CHT"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_AICC_AU">
												<xsl:value-of select="$lab_alt_AICC_AU"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_NETG_COK">
												<xsl:value-of select="$lab_alt_NETG_COK"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_SCO">
												<xsl:value-of select="$lab_alt_SCO"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_TST">
												<xsl:value-of select="$lab_alt_TST"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_DXT">
												<xsl:value-of select="$lab_alt_DXT"/>
											</xsl:with-param>
											<xsl:with-param name="lab_alt_FDR">
												<xsl:value-of select="$lab_alt_FDR"/>
											</xsl:with-param>
										</xsl:call-template>
									</tr>
								</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="no_mod">
								<xsl:with-param name="lab_no_mod">
									<xsl:value-of select="$lab_no_mod"/>
								</xsl:with-param>
								<xsl:with-param name="lab_OK">
									<xsl:value-of select="$lab_OK"/>
								</xsl:with-param>
								<xsl:with-param name="lab_close">
									<xsl:value-of select="$lab_close"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="/selmod/sel_mod_option/dis_mod_type = '2'">
					<xsl:for-each select="/selmod/modules/tableofcontents/item[@identifierref = /selmod/modules/mod_list/module/@id]">
						<tr>
							<xsl:call-template name="item">
								<xsl:with-param name="lab_off">
									<xsl:value-of select="$lab_off"/>
								</xsl:with-param>
								<xsl:with-param name="lab_on">
									<xsl:value-of select="$lab_on"/>
								</xsl:with-param>
								<xsl:with-param name="lab_no_status">
									<xsl:value-of select="$lab_no_status"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_RDG">
									<xsl:value-of select="$lab_alt_RDG"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_REF">
									<xsl:value-of select="$lab_alt_REF"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_GLO">
									<xsl:value-of select="$lab_alt_GLO"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_VOD">
									<xsl:value-of select="$lab_alt_VOD"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_ASS">
									<xsl:value-of select="$lab_alt_ASS"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_SVY">
									<xsl:value-of select="$lab_alt_SVY"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_FOR">
									<xsl:value-of select="$lab_alt_FOR"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_FAQ">
									<xsl:value-of select="$lab_alt_FAQ"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_CHT">
									<xsl:value-of select="$lab_alt_CHT"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_AICC_AU">
									<xsl:value-of select="$lab_alt_AICC_AU"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_NETG_COK">
									<xsl:value-of select="$lab_alt_NETG_COK"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_SCO">
									<xsl:value-of select="$lab_alt_SCO"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_TST">
									<xsl:value-of select="$lab_alt_TST"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_DXT">
									<xsl:value-of select="$lab_alt_DXT"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_FDR">
									<xsl:value-of select="$lab_alt_FDR"/>
								</xsl:with-param>
							</xsl:call-template>
						</tr>
					</xsl:for-each>
				</xsl:when>
				<xsl:otherwise>
					<xsl:for-each select="/selmod/modules/tableofcontents/descendant::item[@identifierref = /selmod/modules/mod_list/module/@id]">
						<tr>
							<xsl:call-template name="item">
								<xsl:with-param name="lab_off">
									<xsl:value-of select="$lab_off"/>
								</xsl:with-param>
								<xsl:with-param name="lab_on">
									<xsl:value-of select="$lab_on"/>
								</xsl:with-param>
								<xsl:with-param name="lab_no_status">
									<xsl:value-of select="$lab_no_status"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_RDG">
									<xsl:value-of select="$lab_alt_RDG"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_REF">
									<xsl:value-of select="$lab_alt_REF"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_GLO">
									<xsl:value-of select="$lab_alt_GLO"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_VOD">
									<xsl:value-of select="$lab_alt_VOD"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_ASS">
									<xsl:value-of select="$lab_alt_ASS"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_SVY">
									<xsl:value-of select="$lab_alt_SVY"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_FOR">
									<xsl:value-of select="$lab_alt_FOR"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_FAQ">
									<xsl:value-of select="$lab_alt_FAQ"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_CHT">
									<xsl:value-of select="$lab_alt_CHT"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_AICC_AU">
									<xsl:value-of select="$lab_alt_AICC_AU"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_NETG_COK">
									<xsl:value-of select="$lab_alt_NETG_COK"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_SCO">
									<xsl:value-of select="$lab_alt_SCO"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_TST">
									<xsl:value-of select="$lab_alt_TST"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_DXT">
									<xsl:value-of select="$lab_alt_DXT"/>
								</xsl:with-param>
								<xsl:with-param name="lab_alt_FDR">
									<xsl:value-of select="$lab_alt_FDR"/>
								</xsl:with-param>
							</xsl:call-template>
						</tr>
					</xsl:for-each>
				</xsl:otherwise>
			</xsl:choose>
		</table>
	</xsl:template>
	<!-- ===============================================================-->
	<xsl:template name="alt_label">
		<xsl:param name="lab_alt_RDG"/>
		<xsl:param name="lab_alt_REF"/>
		<xsl:param name="lab_alt_GLO"/>
		<xsl:param name="lab_alt_VOD"/>
		<xsl:param name="lab_alt_ASS"/>
		<xsl:param name="lab_alt_SVY"/>
		<xsl:param name="lab_alt_FOR"/>
		<xsl:param name="lab_alt_FAQ"/>
		<xsl:param name="lab_alt_CHT"/>
		<xsl:param name="lab_alt_AICC_AU"/>
		<xsl:param name="lab_alt_NETG_COK"/>
		<xsl:param name="lab_alt_SCO"/>
		<xsl:param name="lab_alt_TST"/>
		<xsl:param name="lab_alt_DXT"/>
		<xsl:param name="lab_alt_FDR"/>
		<xsl:param name="type"/>
		<xsl:choose>
			<xsl:when test="$type='RDG'">
				<xsl:value-of select="$lab_alt_RDG"/>
			</xsl:when>
			<xsl:when test="$type='REF'">
				<xsl:value-of select="$lab_alt_REF"/>
			</xsl:when>
			<xsl:when test="$type='GLO'">
				<xsl:value-of select="$lab_alt_GLO"/>
			</xsl:when>
			<xsl:when test="$type='VOD'">
				<xsl:value-of select="$lab_alt_VOD"/>
			</xsl:when>
			<xsl:when test="$type='ASS'">
				<xsl:value-of select="$lab_alt_ASS"/>
			</xsl:when>
			<xsl:when test="$type='SVY'">
				<xsl:value-of select="$lab_alt_SVY"/>
			</xsl:when>
			<xsl:when test="$type='FOR'">
				<xsl:value-of select="$lab_alt_FOR"/>
			</xsl:when>
			<xsl:when test="$type='FAQ'">
				<xsl:value-of select="$lab_alt_FAQ"/>
			</xsl:when>
			<xsl:when test="$type='CHT'">
				<xsl:value-of select="$lab_alt_CHT"/>
			</xsl:when>
			<xsl:when test="$type='AICC_AU'">
				<xsl:value-of select="$lab_alt_AICC_AU"/>
			</xsl:when>
			<xsl:when test="$type='NETG_COK'">
				<xsl:value-of select="$lab_alt_NETG_COK"/>
			</xsl:when>
			<xsl:when test="$type='SCO'">
				<xsl:value-of select="$lab_alt_SCO"/>
			</xsl:when>
			<xsl:when test="$type='TST'">
				<xsl:value-of select="$lab_alt_TST"/>
			</xsl:when>
			<xsl:when test="$type='DXT'">
				<xsl:value-of select="$lab_alt_DXT"/>
			</xsl:when>
			<xsl:when test="$type='FDR'">
				<xsl:value-of select="$lab_alt_FDR"/>
			</xsl:when>
			<xsl:otherwise/>
		</xsl:choose>
	</xsl:template>
	<!-- ===============================================================-->
	<xsl:template name="image_name">
		<xsl:param name="type"/>
		<xsl:choose>
			<xsl:when test="$type='RDG'">sico_rdg.gif</xsl:when>
			<xsl:when test="$type='REF'">sico_ref.gif</xsl:when>
			<xsl:when test="$type='GLO'">sico_glo.gif</xsl:when>
			<xsl:when test="$type='VOD'">sico_vod.gif</xsl:when>
			<xsl:when test="$type='ASS'">sico_ass.gif</xsl:when>
			<xsl:when test="$type='SVY'">sico_svy.gif</xsl:when>
			<xsl:when test="$type='FOR'">sico_for.gif</xsl:when>
			<xsl:when test="$type='FAQ'">sico_faq.gif</xsl:when>
			<xsl:when test="$type='CHT'">sico_cht.gif</xsl:when>
			<xsl:when test="$type='AICC_AU'">sico_aicc_au.gif</xsl:when>
			<xsl:when test="$type='NETG_COK'">sico_netg_cok.gif</xsl:when>
			<xsl:when test="$type='SCO'">sico_sco.gif</xsl:when>
			<xsl:when test="$type='TST'">sico_tst.gif</xsl:when>
			<xsl:when test="$type='DXT'">sico_dxt.gif</xsl:when>
			<xsl:when test="$type='FDR'">tree/images/default/folder.png</xsl:when>
			<xsl:otherwise/>
		</xsl:choose>
	</xsl:template>
	<!-- ===============================================================-->
	<xsl:template name="item">
		<xsl:param name="lab_alt_RDG"/>
		<xsl:param name="lab_alt_REF"/>
		<xsl:param name="lab_alt_GLO"/>
		<xsl:param name="lab_alt_VOD"/>
		<xsl:param name="lab_alt_ASS"/>
		<xsl:param name="lab_alt_SVY"/>
		<xsl:param name="lab_alt_FOR"/>
		<xsl:param name="lab_alt_FAQ"/>
		<xsl:param name="lab_alt_CHT"/>
		<xsl:param name="lab_alt_AICC_AU"/>
		<xsl:param name="lab_alt_NETG_COK"/>
		<xsl:param name="lab_alt_SCO"/>
		<xsl:param name="lab_alt_TST"/>
		<xsl:param name="lab_alt_DXT"/>
		<xsl:param name="lab_alt_FDR"/>
		<xsl:param name="lab_on"/>
		<xsl:param name="lab_off"/>
		<xsl:param name="lab_no_status"/>
		<xsl:variable name="path">/selmod/modules/mod_list/module</xsl:variable>
		<xsl:variable name="alt_label">
			<xsl:call-template name="alt_label">
				<xsl:with-param name="lab_alt_RDG">
					<xsl:value-of select="$lab_alt_RDG"/>
				</xsl:with-param>
				<xsl:with-param name="lab_alt_REF">
					<xsl:value-of select="$lab_alt_REF"/>
				</xsl:with-param>
				<xsl:with-param name="lab_alt_GLO">
					<xsl:value-of select="$lab_alt_GLO"/>
				</xsl:with-param>
				<xsl:with-param name="lab_alt_VOD">
					<xsl:value-of select="$lab_alt_VOD"/>
				</xsl:with-param>
				<xsl:with-param name="lab_alt_ASS">
					<xsl:value-of select="$lab_alt_ASS"/>
				</xsl:with-param>
				<xsl:with-param name="lab_alt_SVY">
					<xsl:value-of select="$lab_alt_SVY"/>
				</xsl:with-param>
				<xsl:with-param name="lab_alt_FOR">
					<xsl:value-of select="$lab_alt_FOR"/>
				</xsl:with-param>
				<xsl:with-param name="lab_alt_FAQ">
					<xsl:value-of select="$lab_alt_FAQ"/>
				</xsl:with-param>
				<xsl:with-param name="lab_alt_CHT">
					<xsl:value-of select="$lab_alt_CHT"/>
				</xsl:with-param>
				<xsl:with-param name="lab_alt_AICC_AU">
					<xsl:value-of select="$lab_alt_AICC_AU"/>
				</xsl:with-param>
				<xsl:with-param name="lab_alt_NETG_COK">
					<xsl:value-of select="$lab_alt_NETG_COK"/>
				</xsl:with-param>
				<xsl:with-param name="lab_alt_SCO">
					<xsl:value-of select="$lab_alt_SCO"/>
				</xsl:with-param>
				<xsl:with-param name="lab_alt_TST">
					<xsl:value-of select="$lab_alt_TST"/>
				</xsl:with-param>
				<xsl:with-param name="lab_alt_DXT">
					<xsl:value-of select="$lab_alt_DXT"/>
				</xsl:with-param>
				<xsl:with-param name="lab_alt_FDR">
					<xsl:value-of select="$lab_alt_FDR"/>
				</xsl:with-param>
				<xsl:with-param name="type">
					<xsl:choose>
						<xsl:when test="restype">
							<xsl:value-of select="restype"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="itemtype"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="check_value">
			<xsl:choose>
				<xsl:when test="itemtype = 'FDR'">
					<xsl:for-each select="descendant::item[itemtype = 'MOD']"/>
					<xsl:value-of select="@identifierref"/>,
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="@identifierref"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="image">
			<xsl:call-template name="image_name">
				<xsl:with-param name="type">
					<xsl:choose>
						<xsl:when test="restype">
							<xsl:value-of select="restype"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="itemtype"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="id">
			<xsl:choose>
				<xsl:when test="@identifierref != ''">
					<xsl:value-of select="@identifierref"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="@identifier"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<td>
			<xsl:choose>
				<xsl:when test="/selmod/sel_mod_option/is_multiple = 'true'">
					<input type="checkbox" name="mod_list" value="{$id}"/>
				</xsl:when>
				<xsl:otherwise>
					<input type="radio" name="mod_list" value="{$id}"/>
				</xsl:otherwise>
			</xsl:choose>
		</td>
		<td align="left" nowrap="nowrap">
			<img src="{$wb_img_path}{$image}" alt="{$alt_label}"/>
		</td>
		<td align="left">
			<input type="hidden" name="mod_title" value="{@title}"/>
			<xsl:value-of select="@title"/>
		</td>
		<td align="left" nowrap="nowrap">
			<xsl:choose>
				<xsl:when test="/selmod/modules/mod_list/module[@id = $id]/status = 'ON'">
					<xsl:value-of select="$lab_on"/>
				</xsl:when>
				<xsl:when test="/selmod/modules/mod_list/module[@id = $id]/status = 'OFF'">
					<xsl:value-of select="$lab_off"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$lab_no_status"/>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
</xsl:stylesheet>
