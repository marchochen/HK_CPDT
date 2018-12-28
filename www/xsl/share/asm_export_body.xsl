<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="draw_export_body">
		<xsl:param name="width"/>
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:call-template name="draw_export_body_">
					<xsl:with-param name="width">
						<xsl:value-of select="$width"/>
					</xsl:with-param>
					<xsl:with-param name="lab_export">導出</xsl:with-param>
					<xsl:with-param name="lab_download">馬上下載</xsl:with-param>
					<xsl:with-param name="lab_note">注</xsl:with-param>
					<xsl:with-param name="lab_export_instruct_1">您可以把本測驗的內容導出用於離線測試。點擊下面的Zip文件小圖示，您可以下載本測驗的離線測驗包，包括：</xsl:with-param>
					<xsl:with-param name="lab_export_instruct_2">1. 試卷 - 供學生在離線測驗時作答用<br/>2. 參考答案 - 供學生在離線測驗後核對答案用<br/>3. 答案紙 - 供評分人員批改測驗用</xsl:with-param>
					<xsl:with-param name="lab_export_note">我們建議您在發佈之前把這個離線測驗包保存一個備份。因爲根據測驗設置的不同，測驗中的題目組合在每一次下載時都有可能不同。</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$wb_lang='gb'">
				<xsl:call-template name="draw_export_body_">
					<xsl:with-param name="width">
						<xsl:value-of select="$width"/>
					</xsl:with-param>
					<xsl:with-param name="lab_export">导出</xsl:with-param>
					<xsl:with-param name="lab_download">马上下载</xsl:with-param>
					<xsl:with-param name="lab_note">注</xsl:with-param>
					<xsl:with-param name="lab_export_instruct_1">您可以把本测验的内容导出用于离线测试。点击下面的Zip文件小图标，您可以下载本测验的离线测验包，包括：</xsl:with-param>
					<xsl:with-param name="lab_export_instruct_2">1. 试卷 - 供学生在离线测验时作答用<br/>2. 参考答案 - 供学生在离线测验后核对答案用<br/>3. 答案纸 - 供评分人员批改测验用</xsl:with-param>
					<xsl:with-param name="lab_export_note">我们建议您在发布之前把这个离线测验包保存一个备份。因为根据测验设置的不同，测验中的题目组合在每一次下载时都有可能不同。</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="draw_export_body_">
					<xsl:with-param name="width">
						<xsl:value-of select="$width"/>
					</xsl:with-param>
					<xsl:with-param name="lab_export">Export</xsl:with-param>
					<xsl:with-param name="lab_download">Download</xsl:with-param>
					<xsl:with-param name="lab_note">Note</xsl:with-param>
					<xsl:with-param name="lab_export_instruct_1">You can export the materials of this test for the use of offline assessment. By clicking the Zip file icon below, you can download the offline assessment kit containing these documents:</xsl:with-param>
					<xsl:with-param name="lab_export_instruct_2">1. Test paper - for learners to participate in offline assessment<br/>2. Model answers - for learners to review the answers after offline assessment<br/>3. Answer spreadsheet - for graders to do the test scoring</xsl:with-param>
					<xsl:with-param name="lab_export_note">You are recommended to keep a copy of the offline assessment kit in your computer before making the distribution because the set of questions inside the kit could be different in every download depending on the test design.</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="draw_export_body_">
		<xsl:param name="width"/>
		<xsl:param name="lab_export"/>
		<xsl:param name="lab_download"/>
		<xsl:param name="lab_note"/>
		<xsl:param name="lab_export_instruct_1"/>
		<xsl:param name="lab_export_instruct_2"/>
		<xsl:param name="lab_export_note"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_export"/>
			</xsl:with-param>
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		
		<!-- Footer 1px Line-->
		<table width="{$width}" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<IMG border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
		</table>
		<!-- Footer 1px Line-->
		
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_export_instruct_1"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text">
				<ul>
					<xsl:copy-of select="$lab_export_instruct_2"/>
				</ul>
			</xsl:with-param>
		</xsl:call-template>
		<table class="Bg" cellpadding="3" cellspacing="0" border="0" width="{$width}">
			<tr>
				<td width="90%">
					<a href="javascript:asm.export_exec({@id})">
						<img src="{$wb_img_path}icon_zip.gif" border="0"/>
						&#160;<xsl:value-of select="$lab_download"/>
					</a>
				</td>
			</tr>
			<tr>
				<td width="90%">
					<span class="Text wzb-ui-module-text">
						<xsl:value-of select="$lab_note"/>：
						<xsl:value-of select="$lab_export_note"/>
					</span>
				</td>
			</tr>
			<tr>
				<td width="90%">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="draw_export_svy_body">
		<xsl:param name="width"/>
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:call-template name="draw_export_body_svy_">
					<xsl:with-param name="width">
						<xsl:value-of select="$width"/>
					</xsl:with-param>
					<xsl:with-param name="lab_download">馬上下載</xsl:with-param>
					<xsl:with-param name="lab_export">導出</xsl:with-param>
					<xsl:with-param name="lab_export_instruct_1">您可以把本問卷的內容導出用於課程評估。點擊下面的Zip文件小圖示，您可以下載該課程評估問卷包，包括：</xsl:with-param>
					<xsl:with-param name="lab_export_instruct_2">1. 問卷 - 供學生在填寫課程評估問卷時作答用<br/>2. 參考答案 - 供學生在填寫課程評估問卷後核對答案用</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$wb_lang='gb'">
				<xsl:call-template name="draw_export_body_svy_">
					<xsl:with-param name="width">
						<xsl:value-of select="$width"/>
					</xsl:with-param>
					<xsl:with-param name="lab_download">马上下载</xsl:with-param>
					<xsl:with-param name="lab_export">导出</xsl:with-param>
					<xsl:with-param name="lab_export_instruct_1">您可以把本问卷的内容导出用于课程评估。点击下面的Zip文件小图标，您可以下载该课程评估问卷，包括：</xsl:with-param>
					<xsl:with-param name="lab_export_instruct_2">1. 问卷 - 供学生在填写课程评估问卷时作答用<br/>2. 参考答案 - 供学生在填写课程评估问卷后核对答案用</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="draw_export_body_svy_">
					<xsl:with-param name="width">
						<xsl:value-of select="$width"/>
					</xsl:with-param>
					<xsl:with-param name="lab_download">Download</xsl:with-param>
					<xsl:with-param name="lab_export">Export</xsl:with-param>
					<xsl:with-param name="lab_export_instruct_1">You can export the materials of this survey for the use of survey. By clicking the Zip file icon below, you can download the survey kit containing these documents:</xsl:with-param>
					<xsl:with-param name="lab_export_instruct_2">1. Survey Paper - for learners to participate in survey<br/>2. Model Answers - for learners to review the answers after survey</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="draw_export_body_svy_">
		<xsl:param name="width"/>
		<xsl:param name="lab_export"/>
		<xsl:param name="lab_download"/>
		<xsl:param name="lab_export_instruct_1"/>
		<xsl:param name="lab_export_instruct_2"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_export"/>
			</xsl:with-param>
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_export_instruct_1"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text">
				<ul>
					<xsl:copy-of select="$lab_export_instruct_2"/>
				</ul>
			</xsl:with-param>
		</xsl:call-template>
		<!-- <xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template> -->
		<table class="Bg" cellpadding="3" cellspacing="0" border="0" width="{$width}">
			<tr>
				<td height="10" width="10%">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="90%">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
			<tr>
				<!-- <td height="10" width="10%">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td> -->
				<td width="90%">
					<a href="javascript:asm.export_evn({@id})">
						<img src="{$wb_img_path}icon_zip.gif" border="0"/>
						&#160;<xsl:value-of select="$lab_download"/>
					</a>
				</td>
			</tr>
			<tr>
				<td height="10" width="10%">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="90%">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
			<tr>
				<td height="10" width="10%">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="90%">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
	</xsl:template>
</xsl:stylesheet>
