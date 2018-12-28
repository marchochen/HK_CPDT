<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!--const-->
	<xsl:variable name="tableLeftWidth">25%</xsl:variable>
	<xsl:variable name="tableRightWidth">75%</xsl:variable>
	<xsl:variable name="tableLeftText">TitleText</xsl:variable>
	<xsl:variable name="tableRightText">Text</xsl:variable>
	<xsl:variable name="field_width">300</xsl:variable>
	<xsl:variable name="profile_attributes" select="//profile_attributes"/>
	<xsl:variable name="cur_lang">
		<xsl:choose>
			<xsl:when test="$wb_lang= 'ch'">zh-hk</xsl:when>
			<xsl:when test="$wb_lang= 'gb'">zh-cn</xsl:when>
			<xsl:when test="$wb_lang= 'en'">en-us</xsl:when>
			<xsl:otherwise>en</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!--variable definition area -->
	<xsl:variable name="lab_mark_recording">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">計分記錄</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">计分记录</xsl:when>
			<xsl:otherwise>Scoring record</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_lrn">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">學員</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">学员</xsl:when>
			<xsl:otherwise>Learner</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_scoring_item">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">計分項目</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">计分项目</xsl:when>
			<xsl:otherwise>Scoring item</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_no_scoring_item">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'"> 沒有發現計分項目</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">没有记分项目</xsl:when>
			<xsl:otherwise>No scoring items found.</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_no_mark_record">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">沒有學員</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">没有学员</xsl:when>
			<xsl:otherwise>No learners found.</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_usr_display_bil">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">姓名</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">昵称</xsl:when>
			<xsl:otherwise>Name</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_usr_ste_usr_id">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">用戶帳號</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">用户名</xsl:when>
			<xsl:otherwise>User Id</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_export">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">匯出</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">导出</xsl:when>
			<xsl:otherwise>Export</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_import">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">匯入</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">导入</xsl:when>
			<xsl:otherwise>Import</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:variable name="lab_btn_ok">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">確定</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">确定</xsl:when>
			<xsl:otherwise>OK</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_btn_cancel">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">取消</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">取消</xsl:when>
			<xsl:otherwise>Cancel</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_btn_edit">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">修改</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">修改</xsl:when>
			<xsl:otherwise>Edit</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_btn_reset">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">重定</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">重置</xsl:when>
			<xsl:otherwise>Reset</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_cmt_contri_rate">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">比重(%)</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">比重(%)</xsl:when>
			<xsl:otherwise>Weight(%)</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_cmt_pass_score">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">合格分數</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">合格分数</xsl:when>
			<xsl:otherwise>Passing score</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_cmt_max_score">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">滿分</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">满分</xsl:when>
			<xsl:otherwise>Maximum score</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_cmt_score">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">分數</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">分数</xsl:when>
			<xsl:otherwise>Score</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_cmt_score_max">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">分數 / 滿分</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">分数 / 满分</xsl:when>
			<xsl:otherwise>Score / Max</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_cmt_title">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">標題</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">标题</xsl:when>
			<xsl:otherwise>Title</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_app_id">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">申請序號</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">申请序号</xsl:when>
			<xsl:otherwise>Application ID</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_avg_mark">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">加權平均分數</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">加权平均分数</xsl:when>
			<xsl:otherwise>Weighted average score</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:template name="empty_row">
		<tr>
			<td height="10" width="{$tableLeftWidth}">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td height="10" width="{$tableRightWidth}">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
